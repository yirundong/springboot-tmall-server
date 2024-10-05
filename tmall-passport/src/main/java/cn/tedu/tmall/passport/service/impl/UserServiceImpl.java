package cn.tedu.tmall.passport.service.impl;

import cn.tedu.tmall.common.enumerator.ServiceCode;
import cn.tedu.tmall.common.ex.ServiceException;
import cn.tedu.tmall.common.pojo.authentication.CurrentPrincipal;
import cn.tedu.tmall.common.pojo.po.UserStatePO;
import cn.tedu.tmall.common.util.JwtUtils;
import cn.tedu.tmall.passport.dao.cache.IUserCacheRepository;
import cn.tedu.tmall.passport.dao.persist.repository.ILoginLogRepository;
import cn.tedu.tmall.passport.dao.persist.repository.IUserRepository;
import cn.tedu.tmall.passport.pojo.entity.LoginLog;
import cn.tedu.tmall.passport.pojo.param.UserLoginInfoParam;
import cn.tedu.tmall.passport.pojo.vo.UserLoginResultVO;
import cn.tedu.tmall.passport.security.CustomUserDetails;
import cn.tedu.tmall.passport.service.IUserService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理用户数据的业务实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Value("${tmall.jwt.secret-key}")
    private String secretKey;
    @Value("${tmall.jwt.duration-in-minute}")
    private long durationInMinute;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ILoginLogRepository loginLogRepository;
    @Autowired
    private IUserCacheRepository userCacheRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    public UserServiceImpl() {
        log.debug("创建业务类对象：UserServiceImpl");
    }

    @Override
    public UserLoginResultVO login(UserLoginInfoParam userLoginInfoParam,
                                   String remoteAddr, String userAgent) {
        log.debug("开始处理【用户登录】的业务，参数：{}", userLoginInfoParam);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userLoginInfoParam.getUsername(), userLoginInfoParam.getPassword());
        log.debug("准备调用AuthenticationManager的认证方法，判断此用户名、密码是否可以成功登录……");
        Authentication authenticateResult
                = authenticationManager.authenticate(authentication);
        log.debug("验证用户登录成功，返回的认证结果：{}", authenticateResult);

        Object principal = authenticateResult.getPrincipal();
        CustomUserDetails userDetails = (CustomUserDetails) principal;
        Long userId = userDetails.getId();
        String username = userDetails.getUsername();
        String avatar = userDetails.getAvatar();
        Integer loginCount = userDetails.getLoginCount();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        String authoritiesJsonString = JSON.toJSONString(authorities);

        LocalDateTime now = LocalDateTime.now();
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setUsername(username);
        loginLog.setIp(remoteAddr);
        loginLog.setUserAgent(userAgent);
        loginLog.setGmtLogin(now);
        int rows = loginLogRepository.insert(loginLog);
        if (rows != 1) {
            String message = "登录失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }

        rows = userRepository.updateLastLogin(userId, loginCount + 1, remoteAddr, now);
        if (rows != 1) {
            String message = "登录失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }

        Date expiration = new Date(System.currentTimeMillis() + 1L * 60 * 1000 * durationInMinute);
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, userId);
        claims.put(CLAIM_USER_NAME, username);
        claims.put(CLAIM_USER_AGENT, userAgent);
        claims.put(CLAIM_REMOTE_ADDR, remoteAddr);
        String jwt = JwtUtils.generate(claims, expiration, secretKey);
        log.debug("生成用户的JWT数据：{}", jwt);

        UserStatePO userStatePO = new UserStatePO();
        userStatePO.setEnable(userDetails.isEnabled() ? 1 : 0);
        userStatePO.setAuthoritiesJsonString(authoritiesJsonString);
        userCacheRepository.saveUserState(userId, userStatePO);
        log.debug("向缓存中存入用户状态数据：{}", userStatePO);

        String[] authorityArray = new String[authorities.size()];
        int i = 0;
        for (GrantedAuthority authority : authorities) {
            authorityArray[i++] = authority.getAuthority();
        }
        UserLoginResultVO userLoginResultVO = new UserLoginResultVO()
                .setId(userId)
                .setUsername(username)
                .setAvatar(avatar)
                .setToken(jwt)
                .setAuthorities(authorityArray);
        log.debug("即将返回用户的登录结果：{}", userLoginResultVO);
        return userLoginResultVO;
    }

    @Override
    public void logout(CurrentPrincipal currentPrincipal) {
        log.debug("开始处理【退出登录】的业务，参数：{}", currentPrincipal);
        Long userId = currentPrincipal.getId();
        boolean deleteResult = userCacheRepository.deleteUserState(userId);
        if (!deleteResult) {
            String message = "操作失败，用户数据有误！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }
    }

}
