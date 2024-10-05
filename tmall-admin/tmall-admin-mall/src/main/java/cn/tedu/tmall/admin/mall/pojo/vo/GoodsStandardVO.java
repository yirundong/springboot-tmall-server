package cn.tedu.tmall.admin.mall.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 标准VO类：商品信息
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class GoodsStandardVO implements Serializable {

    /**
     * 数据ID
     */
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
    private BigDecimal salePrice;

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
     * 审核状态，0=未审核，1=审核通过，2=拒绝审核
     */
    private Integer checkState;

    /**
     * 上架状态，0=下架，1=上架
     */
    private Integer isPutOn;

    /**
     * 详情
     */
    private String detail;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm.ss")
    private LocalDateTime gmtCreate;

    /**
     * 数据最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm.ss")
    private LocalDateTime gmtModified;

}
