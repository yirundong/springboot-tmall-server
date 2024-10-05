package cn.tedu.tmall.passport.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 列表项VO类：用户登录日志
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class LoginLogListItemVO implements Serializable {

    /**
     * 数据id
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 浏览器信息
     */
    private String userAgent;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtLogin;

}
