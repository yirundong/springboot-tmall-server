package cn.tedu.tmall.admin.mall.pojo.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 新增商品的参数类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class GoodsAddNewParam implements Serializable {

    /**
     * 类别ID
     */
    private Long categoryId;

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
     * 详情
     */
    private String detail;

}
