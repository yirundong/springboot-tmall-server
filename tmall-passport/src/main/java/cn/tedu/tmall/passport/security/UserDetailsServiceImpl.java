package cn.tedu.tmall.passport.security;

import cn.tedu.tmall.passport.dao.persist.repository.IUserRepository;
import cn.tedu.tmall.passport.pojo.vo.UserLoginInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security处理认证时使用到的获取用户登录详情的实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    public UserDetailsServiceImpl() {
        log.debug("创建Spring Security的UserDetailsService接口对象：UserDetailsServiceImpl");
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("Spring Security框架自动调用了UserDetailsService对象，将根据用户名获取用户详情，参数：{}", s);
        UserLoginInfoVO loginInfo = userRepository.getLoginInfoByUsername(s);
        log.debug("根据用户名【{}】从数据库中查询用户详情，查询结果：{}", s, loginInfo);

        if (loginInfo == null) {
            log.debug("即将向Spring Security框架返回null，由框架进行后续的处理");
            return null;
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> permissions = loginInfo.getPermissions();
        for (String permission : permissions) {
            GrantedAuthority authority = new SimpleGrantedAuthority(permission);
            authorities.add(authority);
        }

        CustomUserDetails userDetails = new CustomUserDetails(
                loginInfo.getId(), loginInfo.getUsername(), loginInfo.getPassword(), loginInfo.getAvatar(),
                loginInfo.getEnable() == 1, loginInfo.getLoginCount(), authorities);

        log.debug("即将向Spring Security框架返回UserDetails类型的结果：{}", userDetails);
        log.debug("接下来，将由Spring Security框架自动验证用户状态、密码等，以判断是否可以成功登录！");
        return userDetails;
    }

}
