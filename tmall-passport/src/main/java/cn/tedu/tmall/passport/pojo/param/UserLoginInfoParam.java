package cn.tedu.tmall.passport.pojo.param;

import cn.tedu.tmall.common.validation.account.UserRules;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户登录的参数类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class UserLoginInfoParam implements Serializable, UserRules {

    /**
     * 用户名
     */
    @NotNull(message = MESSAGE_USERNAME_NOT_NULL)
    @Pattern(regexp = PATTERN_USERNAME, message = MESSAGE_USERNAME_PATTERN)
    @ApiModelProperty(value = "用户名", required = true, example = "root")
    private String username;

    /**
     * 密码（原文）
     */
    @NotNull(message = MESSAGE_PASSWORD_NOT_NULL)
    @Pattern(regexp = PATTERN_PASSWORD, message = MESSAGE_PASSWORD_PATTERN)
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;

}
