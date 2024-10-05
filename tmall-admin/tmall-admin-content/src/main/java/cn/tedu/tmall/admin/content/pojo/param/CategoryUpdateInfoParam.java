package cn.tedu.tmall.admin.content.pojo.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改类别的参数类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class CategoryUpdateInfoParam implements Serializable {

    /**
     * 类别名称
     */
    private String name;

    /**
     * 关键词列表，各关键词使用英文的逗号分隔
     */
    private String keywords;

    /**
     * 排序序号
     */
    private Integer sort;

}
