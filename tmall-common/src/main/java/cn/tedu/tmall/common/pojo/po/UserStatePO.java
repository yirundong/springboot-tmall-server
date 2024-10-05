package cn.tedu.tmall.common.pojo.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 14:51
 *
 * 存在redis中的用户状态信息
 */
@Data
public class UserStatePO implements Serializable {

    /**
     * 启用状态，0=禁用，1=启用
     */
    private Integer enable;

    /**
     * 权限列表的JSON字符串
     */
    private String authoritiesJsonString;

}