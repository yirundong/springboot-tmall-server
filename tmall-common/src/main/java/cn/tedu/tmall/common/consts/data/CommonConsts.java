package cn.tedu.tmall.common.consts.data;

/**
 * 通用相关常量
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 15:05
 */
public interface CommonConsts {

    /**
     * 截取的简介文本最大长度，通常用于记录在日志中
     */
    int BRIEF_MAX_LENGTH = 32;

    /**
     * 启用状态：禁用
     */
    int ENABLE_STATE_OFF = 0;
    /**
     * 启用状态：启用
     */
    int ENABLE_STATE_ON = 1;
    /**
     * 数据“是否启用”的状态文本
     */
    String[] ENABLE_STATE_TEXT = {"禁用", "启用"};

    /**
     * 显示状态：隐藏
     */
    int DISPLAY_STATE_OFF = 0;
    /**
     * 显示状态：显示
     */
    int DISPLAY_STATE_ON = 1;
    /**
     * 数据“是否显示”的状态文本
     */
    String[] DISPLAY_STATE_TEXT = {"隐藏", "显示"};

    /**
     * 审核状态：未审核
     */
    int CHECK_STATE_UNCHECKED = 0;
    /**
     * 审核状态：审核通过
     */
    int CHECK_STATE_PASS = 1;
    /**
     * 审核状态：拒绝审核
     */
    int CHECK_STATE_REJECT = 2;
    /**
     * 数据“审核状态”的状态文本
     */
    String[] CHECK_STATE_TEXT = {"未审核", "审核通过", "拒绝审核"};

}
