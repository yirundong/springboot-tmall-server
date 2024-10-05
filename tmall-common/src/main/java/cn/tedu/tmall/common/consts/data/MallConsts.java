package cn.tedu.tmall.common.consts.data;

/**
 * 商城管理相关常量
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 15:05
 */
public interface MallConsts extends CommonConsts {

    /**
     * 资源类型：商品
     */
    int RESOURCE_TYPE_GOODS = 0;
    /**
     * 资源类型：评论
     */
    int RESOURCE_TYPE_COMMENT = 1;

    /**
     * 商品上架状态：下架
     */
    int PUT_ON_STATE_OFF = 0;
    /**
     * 商品上架状态：上架
     */
    int PUT_ON_STATE_ON = 1;
    /**
     * 商品“是否上架”的状态文本
     */
    String[] PUT_ON_STATE_TEXT = {"下架", "上架"};

    /**
     * 商品推荐：推荐
     */
    int RECOMMEND_STATE_OFF = 0;
    /**
     * 商品推荐：不推荐
     */
    int RECOMMEND_STATE_ON = 1;
    /**
     * 商品“是否推荐”的状态文本
     */
    String[] RECOMMEND_STATE_TEXT = {"取消推荐", "推荐"};

    /**
     * 评论类型：好评
     */
    int COMMENT_TYPE_POSITIVE = 0;
    /**
     * 评论类型：中评
     */
    int COMMENT_TYPE_NEUTRAL = 1;
    /**
     * 评论类型：差评
     */
    int COMMENT_TYPE_NEGATIVE = 2;
    /**
     * “评论类型”的状态文本
     */
    String[] COMMENT_TYPE_TEXT = {"好评", "中评", "差评"};

    /**
     * 支付渠道：支付宝
     */
    int PAY_CHANNEL_ALIPAY = 0;
    /**
     * 支付渠道：微信
     */
    int PAY_CHANNEL_WECHAT = 1;
    /**
     * “支付渠道”的状态文本
     */
    String[] PAY_CHANNEL_TEXT = {"支付宝", "微信"};

    /**
     * 支付方式：在线支付
     */
    int PAY_METHOD_ONLINE = 0;
    /**
     * 支付方式：货到付款
     */
    int PAY_METHOD_ON_DELIVERY = 1;
    /**
     * “支付方式”的状态文本
     */
    String[] PAY_METHOD_TEXT = {"在线支付", "货到付款"};

    /**
     * 订单状态：待支付
     */
    int ORDER_STATE_UNPAID = 0;
    /**
     * 订单状态：已支付
     */
    int ORDER_STATE_PAID = 1;
    /**
     * 订单状态：已发货
     */
    int ORDER_STATE_DELIVERED = 2;
    /**
     * 订单状态：已支付
     */
    int ORDER_STATE_COMPLETED = 3;
    /**
     * 订单状态：待支付
     */
    int ORDER_STATE_CLOSED_BY_USER = 4;
    /**
     * 订单状态：已支付
     */
    int ORDER_STATE_CLOSE_BY_SELLER = 5;
    /**
     * 订单状态：待支付
     */
    int ORDER_STATE_CLOSED_BY_SYSTEM = 6;
    /**
     * “订单状态”的状态文本
     */
    String[] ORDER_STATE_TEXT = {"待支付", "已支付，待发货", "已发货，待收货", "确认收货，已完成", "用户关闭", "平台关闭", "系统调度关闭"};

}
