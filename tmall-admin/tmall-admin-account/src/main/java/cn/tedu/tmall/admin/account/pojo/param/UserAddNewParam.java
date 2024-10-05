package cn.tedu.tmall.admin.account.pojo.param;

import cn.tedu.tmall.common.validation.BaseRules;
import cn.tedu.tmall.common.validation.account.UserRules;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 添加用户的参数类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class UserAddNewParam implements Serializable, UserRules {

    /**
     * 用户名
     */
    @NotEmpty(message = MESSAGE_USERNAME_NOT_NULL)
    @Pattern(regexp = PATTERN_USERNAME, message = MESSAGE_USERNAME_PATTERN)
    private String username;

    /**
     * 密码（原文）
     */
    @NotEmpty(message = MESSAGE_PASSWORD_NOT_NULL)
    @Pattern(regexp = PATTERN_PASSWORD, message = MESSAGE_PASSWORD_PATTERN)
    private String password;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否启用，1=启用，0=未启用
     */
    @NotNull(message = MESSAGE_ENABLE_NOT_NULL)
    @Min(value = BaseRules.RANGE_ENABLE_MIN, message = MESSAGE_ENABLE_MIN)
    @Max(value = BaseRules.RANGE_ENABLE_MAX, message = MESSAGE_ENABLE_MAX)
    private Integer enable;

    /**
     * 用户的角色ID的数组
     */
    @NotNull(message = MESSAGE_ROLE_IDS_NOT_NULL)
    @Size(min = UserRules.SIZE_ROLE_IDS_MIN, message = MESSAGE_ROLE_IDS_MIN_SIZE)
    private Long[] roleIds;

}
