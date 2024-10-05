package cn.tedu.tmall.passport.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author YiRunDong
 * @version 2.0
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomUserDetails extends User {

    /**
     * 用户ID
     */
    private final Long id;
    /**
     * 用户头像
     */
    private final String avatar;
    /**
     * 登录次数
     */
    private final Integer loginCount;

    /**
     * 创建自定义用户详情类型的对象
     *
     * @param id          用户ID
     * @param username    用户名
     * @param password    密码（密文）
     * @param avatar      头像
     * @param enabled     启用状态
     * @param loginCount  登录次数
     * @param authorities 权限列表
     */
    public CustomUserDetails(Long id, String username, String password, String avatar, boolean enabled,
                             Integer loginCount, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
        this.id = id;
        this.avatar = avatar;
        this.loginCount = loginCount;
    }

}
