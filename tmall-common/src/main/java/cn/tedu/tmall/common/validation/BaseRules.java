package cn.tedu.tmall.common.validation;

/**
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 15:00
 *
 * 通用数据相关规则配置
 */
public interface BaseRules {

    // ====== 【启用状态】 =====

    /**
     * 区间：启用状态：最小值
     */
    int RANGE_ENABLE_MIN = 0;
    /**
     * 验证失败描述文本：启用状态最小值
     */
    String MESSAGE_ENABLE_MIN = "启用状态的值必须是0或1";

    /**
     * 区间：启用状态：最大值
     */
    int RANGE_ENABLE_MAX = 1;
    /**
     * 验证失败描述文本：启用状态最大值
     */
    String MESSAGE_ENABLE_MAX = "启用状态的值必须是0或1";

    /**
     * 验证失败描述文本：非NULl：启用状态
     */
    String MESSAGE_ENABLE_NOT_NULL = "请选择用户的启用状态";

}