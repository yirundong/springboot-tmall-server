package cn.tedu.tmall.common.validation.account;

import cn.tedu.tmall.common.validation.BaseRules;

/**
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 15:02
 *
 * 用户数据相关规则配置
 */
public interface UserRules extends BaseRules {

    // ====== 【用户名】 =====

    /**
     * 正则：用户名
     */
    String PATTERN_USERNAME = "^[a-zA-Z]{1}[a-zA-Z0-9_]{3,14}$";
    /**
     * 验证失败描述文本：正则：用户名
     */
    String MESSAGE_USERNAME_PATTERN = "用户名必须由4~15长度的字母、数组、下划线组成，且第1个字符必须是字母";
    /**
     * 验证失败描述文本：非NULl：用户名
     */
    String MESSAGE_USERNAME_NOT_NULL = "请提交用户名";

    // ====== 【密码】 =====

    /**
     * 正则：密码
     */
    String PATTERN_PASSWORD = "^.{4,15}$";
    /**
     * 验证失败描述文本：正则：密码
     */
    String MESSAGE_PASSWORD_PATTERN = "密码必须是4~15长度的字符组成";
    /**
     * 验证失败描述文本：非NULl：密码
     */
    String MESSAGE_PASSWORD_NOT_NULL = "请提交密码";

    // ====== 【手机号码】 =====

    /**
     * 正则：手机号码
     */
    String PATTERN_PHONE = "^\\d{11}$";
    /**
     * 验证失败描述文本：正则：手机号码
     */
    String MESSAGE_PHONE_PATTERN = "手机号码必须是11位的纯数字";
    /**
     * 验证失败描述文本：非NULl：手机号码
     */
    String MESSAGE_PHONE_NOT_NULL = "请提交手机号码";

    // ====== 【电子邮箱】 =====

    /**
     * 正则：电子邮箱
     */
    String PATTERN_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    /**
     * 验证失败描述文本：正则：电子邮箱
     */
    String MESSAGE_EMAIL_PATTERN = "请输入正确格式的电子邮箱";
    /**
     * 验证失败描述文本：非NULl：电子邮箱
     */
    String MESSAGE_EMAIL_NOT_NULL = "请提交电子邮箱";

    // ====== 【角色列表】 =====

    /**
     * 验证失败描述文本：非NULl：角色列表
     */
    String MESSAGE_ROLE_IDS_NOT_NULL = "请至少选择1种角色";
    /**
     * 长度：角色列表：最小值
     */
    int SIZE_ROLE_IDS_MIN = 1;
    /**
     * 验证失败描述文本：最小值：角色列表
     */
    String MESSAGE_ROLE_IDS_MIN_SIZE = "请至少选择1种角色";

}