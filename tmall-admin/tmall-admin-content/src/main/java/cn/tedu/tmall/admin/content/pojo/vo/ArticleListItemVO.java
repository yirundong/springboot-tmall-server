package cn.tedu.tmall.admin.content.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 列表项VO类：内容-文章
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class ArticleListItemVO implements Serializable {

    /**
     * 数据ID
     */
    private Long id;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 作者名字
     */
    private String authorName;

    /**
     * 类别ID
     */
    private Long categoryId;

    /**
     * 类别名称
     */
    private String categoryName;

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
     * 关键词列表，各关键词使用英文的逗号分隔
     */
    private String keywords;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 排序序号
     */
    private Integer sort;

    /**
     * 顶数量
     */
    private Integer upCount;

    /**
     * 踩数量
     */
    private Integer downCount;

    /**
     * 浏览量
     */
    private Integer clickCount;

    /**
     * 评论量
     */
    private Integer commentCount;

    /**
     * 审核状态，0=未审核，1=审核通过，2=拒绝审核
     */
    private Integer checkState;

    /**
     * 显示状态，0=不显示，1=显示
     */
    private Integer isDisplay;

    /**
     * 是否推荐，0=不推荐，1=推荐
     */
    private Integer isRecommend;

}
