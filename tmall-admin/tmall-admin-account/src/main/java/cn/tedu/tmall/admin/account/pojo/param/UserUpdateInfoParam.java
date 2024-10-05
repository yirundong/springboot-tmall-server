package cn.tedu.tmall.admin.account.pojo.param;

import cn.tedu.tmall.common.validation.account.UserRules;
import lombok.Data;

import java.io.Serializable;

/**
 * 修改用户基本信息的参数类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class UserUpdateInfoParam implements Serializable, UserRules {

    /**
     * 简介
     */
    private String description;

}
