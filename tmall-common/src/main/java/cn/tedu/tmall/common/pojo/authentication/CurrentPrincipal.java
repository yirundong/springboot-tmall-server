package cn.tedu.tmall.common.pojo.authentication;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 14:47
 *
 * authentication当中的当事人信息,表示正在使用的那个人是谁
 */
@Data
public class CurrentPrincipal implements Serializable {

    /**
     * 当事人ID
     */
    private Long id;

    /**
     * 当事人用户名
     */
    private String username;

}