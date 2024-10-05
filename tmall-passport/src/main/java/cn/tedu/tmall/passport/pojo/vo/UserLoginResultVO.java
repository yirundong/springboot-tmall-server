package cn.tedu.tmall.passport.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户登录结果的VO类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
@Accessors(chain = true)
public class UserLoginResultVO implements Serializable {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;

    /**
     * Token（JWT）
     */
    @ApiModelProperty("Token（JWT）")
    private String token;

    /**
     * 权限清单
     */
    @ApiModelProperty("权限清单")
    private String[] authorities;

}
