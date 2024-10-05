package cn.tedu.tmall.passport.filter;

import cn.tedu.tmall.common.consts.web.HttpConsts;
import cn.tedu.tmall.common.consts.web.JwtConsts;
import cn.tedu.tmall.common.enumerator.ServiceCode;
import cn.tedu.tmall.common.pojo.authentication.CurrentPrincipal;
import cn.tedu.tmall.common.pojo.po.UserStatePO;
import cn.tedu.tmall.common.util.JwtUtils;
import cn.tedu.tmall.common.web.JsonResult;
import cn.tedu.tmall.passport.dao.cache.IUserCacheRepository;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * <p>处理JWT的过滤器</p>
 *
 * <p>此过滤器的主要作用：</p>
 * <ul>
 *     <li>尝试接收客户端的请求中携带的JWT数据</li>
 *     <li>尝试解析JWT数据</li>
 *     <li>将解析得到的用户数据创建为Authentication对象，并存入到SecurityContext中</li>
 * </ul>
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter implements HttpConsts, JwtConsts {

    @Value("${tmall.jwt.secret-key}")
    private String secretKey;
    @Autowired
    private IUserCacheRepository userCacheRepository;

    public JwtAuthorizationFilter() {
        log.info("创建过滤器对象：JwtAuthorizationFilter");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        log.debug("处理JWT的过滤器开始处理当前请求……");
        // 直接放行登录和注册的请求
        String[] permitUrls = {"/passport/login"};
        String requestURI = request.getRequestURI();
        log.debug("请求URL：{}", requestURI);
        if (Arrays.asList(permitUrls).contains(requestURI)) {
            log.debug("直接放行");
            filterChain.doFilter(request, response);
            return;
        }

        // 尝试接收客户端的请求中携带的JWT数据
        String jwt = request.getHeader(HEADER_AUTHORIZATION);
        log.debug("客户端携带的JWT：{}", jwt);

        // 判断JWT的基本有效性（没有必要尝试解析格式明显错误的JWT数据）
        if (!StringUtils.hasText(jwt) || jwt.length() < JWT_MIN_LENGTH) {
            // 对于无效的JWT，应该直接放行
            log.warn("当前请求中，客户端没有携带有效的JWT，将放行");
            filterChain.doFilter(request, response);
            return;
        }

        // 尝试解析JWT数据
        log.debug("尝试解析JWT数据……");
        response.setContentType("application/json; charset=utf-8");
        Claims claims;
        try {
            claims = JwtUtils.parse(jwt, secretKey);
        } catch (ExpiredJwtException e) {
            log.warn("解析JWT时出现异常：ExpiredJwtException");
            String message = "操作失败，您的登录信息已经过期，请重新登录！";
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_EXPIRED, message);
            PrintWriter writer = response.getWriter();
            writer.println(JSON.toJSONString(jsonResult));
            writer.close();
            return;
        } catch (SignatureException e) {
            log.warn("解析JWT时出现异常：SignatureException");
            String message = "非法访问，你的本次操作已经被记录！";
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_SIGNATURE, message);
            PrintWriter writer = response.getWriter();
            writer.println(JSON.toJSONString(jsonResult));
            writer.close();
            return;
        } catch (MalformedJwtException e) {
            log.warn("解析JWT时出现异常：MalformedJwtException");
            String message = "非法访问，你的本次操作已经被记录！";
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_MALFORMED, message);
            PrintWriter writer = response.getWriter();
            writer.println(JSON.toJSONString(jsonResult));
            writer.close();
            return;
        } catch (Throwable e) {
            log.warn("解析JWT时出现异常：", e);
            String message = "服务器忙，请稍后再试！【同学们，看到这句时，你应该检查服务器端的控制台，并在JwtAuthorizationFilter中解析JWT时添加处理异常的catch代码块】";
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERROR_UNKNOWN, message);
            PrintWriter writer = response.getWriter();
            writer.println(JSON.toJSONString(jsonResult));
            writer.close();
            return;
        }

        // 从解析结果中获取用户的信息
        Long userId = claims.get(CLAIM_USER_ID, Long.class);
        String username = claims.get(CLAIM_USER_NAME, String.class);
        String userAgent = claims.get(CLAIM_USER_AGENT, String.class);
        String remoteAddr = claims.get(CLAIM_REMOTE_ADDR, String.class);
        log.debug("JWT中的用户id = {}", userId);
        log.debug("JWT中的用户名 = {}", username);
        log.debug("JWT中的浏览器信息 = {}", userAgent);
        log.debug("JWT中的IP地址 = {}", remoteAddr);

        // 判断此次请求，与当初登录成功时的相关信息是否相同
        log.debug("开始检查JWT是否存在盗用的问题……");
        if (!userAgent.equals(request.getHeader(HEADER_USER_AGENT))
                && !remoteAddr.equals(request.getRemoteAddr())) {
            // 本次请求的信息与当初登录时完全不同，则视为无效的JWT
            log.warn("本次请求的信息与当初登录时完全不同，将直接放行，交由后续的组件进行处理");
            filterChain.doFilter(request, response);
            return;
        }

        // 从缓存中读取用户登录信息
        log.debug("开始检查缓存中用户的状态……");
        UserStatePO userState = userCacheRepository.getUserState(userId);
        // 判断缓存中是否存在数据
        if (userState == null) {
            // 放行，不会向SecurityContext中存入认证信息，则相当于没有携带JWT
            log.warn("在缓存中无此JWT对应的信息，将直接放行，交由后续的组件进行处理");
            filterChain.doFilter(request, response);
            return;
        }

        // 检查用户的启用状态
        Integer userEnable = userState.getEnable();
        if (userEnable != 1) {
            userCacheRepository.deleteUserState(userId);
            log.warn("用户已被禁用");
            String message = "用户已被禁用";
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERROR_UNAUTHORIZED_DISABLED, message);
            PrintWriter writer = response.getWriter();
            writer.println(JSON.toJSONString(jsonResult));
            writer.close();
            return;
        }

        // 从Redis中读取当前用户的权限列表
        String authoritiesJsonString = userState.getAuthoritiesJsonString();
        log.debug("从Redis中读取当前用户的权限列表 = {}", authoritiesJsonString);

        // 将解析得到的用户数据创建为Authentication对象
        CurrentPrincipal principal = new CurrentPrincipal(); // 当事人
        principal.setId(userId);
        principal.setUsername(username);
        List<SimpleGrantedAuthority> authorities
                = JSON.parseArray(authoritiesJsonString, SimpleGrantedAuthority.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, null, authorities);

        // 将Authentication对象存入到SecurityContext中
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        // 过滤器链继续执行，即：放行
        log.debug("验证JWT完毕，已经向SecurityContext中存入认证信息，过滤器将放行");
        filterChain.doFilter(request, response);
    }

}
