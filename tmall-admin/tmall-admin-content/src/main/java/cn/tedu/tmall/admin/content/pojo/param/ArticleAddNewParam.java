package cn.tedu.tmall.admin.content.pojo.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 新增文章的参数类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class ArticleAddNewParam implements Serializable {

    /**
     * 类别ID
     */
    private Long categoryId;

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
     * 排序序号
     */
    private Integer sort;

    /**
     * 详情
     */
    private String detail;

}
