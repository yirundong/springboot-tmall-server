package cn.tedu.tmall.admin.mall.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体类：商品信息
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
@TableName("mall_goods")
public class Goods implements Serializable {

    /**
     * 数据ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 类别ID
     */
    private Long categoryId;

    /**
     * 类别名称
     */
    private String categoryName;

    /**
     * 条形码
     */
    private String barCode;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String brief;

    /**
     * 封面图
     */
    private String coverUrl;

    /**
     * 售价
     */
    private Long salePrice;

    /**
     * 关键词列表
     */
    private String keywords;

    /**
     * 排序序号
     */
    private Integer sort;

    /**
     * 是否推荐
     */
    private Integer isRecommend;

    /**
     * 审核状态
     */
    private Integer checkState;

    /**
     * 上架状态，0=下架，1=上架
     */
    private Integer isPutOn;

    /**
     * 销量
     */
    private Integer salesCount;

    /**
     * 评论数量
     */
    private Integer commentCount;

    /**
     * 好评数量
     */
    private Integer positiveCommentCount;

    /**
     * 差评数量
     */
    private Integer negativeCommentCount;

    /**
     * 数据创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 数据最后修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

}
