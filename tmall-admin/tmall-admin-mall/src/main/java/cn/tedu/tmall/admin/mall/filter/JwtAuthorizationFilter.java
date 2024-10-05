package cn.tedu.tmall.admin.mall.filter;

import cn.tedu.tmall.admin.mall.dao.cache.IUserCacheRepository;
import cn.tedu.tmall.common.enumerator.ServiceCode;
import cn.tedu.tmall.common.pojo.authentication.CurrentPrincipal;
import cn.tedu.tmall.common.pojo.po.UserStatePO;
import cn.tedu.tmall.common.web.JsonResult;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

/**
 * jwt认证过滤器,认证通过将权限和当事人信息存入security上下文
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 18:52
 */
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Value("${tmall.jwt.secret-key}")
    private String secretKey;
    @Autowired
    private IUserCacheRepository userCacheRepository;

    public JwtAuthorizationFilter() {
        log.debug("创建过滤器对象：JwtAuthorizationFilter");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("JwtAuthorizationFilter.doFilterInternal()");
        // 根据业内的惯例，客户端提交的JWT会放在请求头（Request Header）中名为Authorization的属性中
        String jwt = request.getHeader("Authorization");
        log.debug("获取客户端携带的JWT：{}", jwt);

        // 检查JWT的基本有效性
        if (!StringUtils.hasText(jwt)) {
            // 放行
            filterChain.doFilter(request, response);
            // 返回，避免代码继续向下执行（当前类中剩余的代码）
            return;
        }

        // 尝试解析JWT
        response.setContentType("application/json; charset=utf-8");
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (SignatureException e) {
            String message = "非法访问！";
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_SIGNATURE, message);
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter printWriter = response.getWriter();
            printWriter.println(jsonResultString);
            printWriter.close();
            return;
        } catch (MalformedJwtException e) {
            String message = "非法访问！";
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_MALFORMED, message);
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter printWriter = response.getWriter();
            printWriter.println(jsonResultString);
            printWriter.close();
            return;
        } catch (ExpiredJwtException e) {
            String message = "您的登录信息已过期，请重新登录！";
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_EXPIRED, message);
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter printWriter = response.getWriter();
            printWriter.println(jsonResultString);
            printWriter.close();
            return;
        } catch (Throwable e) {
            log.debug("全局异常处理器开始处理Throwable");
            log.debug("异常跟踪信息如下：", e);
            String message = "服务器忙，请稍后再试！【同学们，看到这句时，你应该检查服务器端的控制台，并在JwtAuthorizationFilter中补充catch代码块进行处理】";
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERROR_UNKNOWN, message);
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter printWriter = response.getWriter();
            printWriter.println(jsonResultString);
            printWriter.close();
            return;
        }

        // 获取JWT中的数据
        Long id = claims.get("id", Long.class);
        String username = claims.get("username", String.class);
        String jwtRemoteAddr = claims.get("remoteAddr", String.class);
        String jwtUserAgent = claims.get("userAgent", String.class);
        log.debug("id = {}", id);
        log.debug("username = {}", username);
        log.debug("remoteAddr = {}", jwtRemoteAddr);
        log.debug("userAgent = {}", jwtUserAgent);

        // 检查是否盗用JWT：IP地址和浏览器信息均不匹配
        String currentRemoteAddr = request.getRemoteAddr();
        String currentUserAgent = request.getHeader("User-Agent");
        if (!currentRemoteAddr.equals(jwtRemoteAddr)
                && !currentUserAgent.equals(jwtUserAgent)) {
            log.debug("本次请求疑似盗用JWT的请求，IP地址：{}，浏览器信息：{}", currentRemoteAddr, currentUserAgent);
            filterChain.doFilter(request, response);
            return;
        }

        // 从Redis中读取用户状态数据
        UserStatePO userState = userCacheRepository.getUserState(id);
        if (userState == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 判断用户账号的状态（是否启用）
        if (userState.getEnable() != 1) {
            userCacheRepository.deleteUserState(id);
            String message = "您的账号已经被禁用！";
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERROR_UNAUTHORIZED_DISABLED, message);
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter printWriter = response.getWriter();
            printWriter.println(jsonResultString);
            printWriter.close();
            return;
        }

        // 创建当事人对象
        CurrentPrincipal principal = new CurrentPrincipal();
        principal.setId(id);
        principal.setUsername(username);

        // 准备当前用户的权限列表
        String authoritiesJsonString = userState.getAuthoritiesJsonString();
        List<SimpleGrantedAuthority> authorities
                = JSON.parseArray(authoritiesJsonString, SimpleGrantedAuthority.class);

        // 创建认证信息（Authentication）
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, null, authorities);

        // 将Authentication存入到SecurityContext
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        // 放行
        filterChain.doFilter(request, response);
    }

}