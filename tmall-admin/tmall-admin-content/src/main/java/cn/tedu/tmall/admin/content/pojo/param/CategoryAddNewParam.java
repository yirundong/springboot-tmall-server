package cn.tedu.tmall.admin.content.pojo.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 新增类别的参数类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class CategoryAddNewParam implements Serializable {

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

    /**
     * 是否启用，1=启用，0=未启用
     */
    private Integer enable;

    /**
     * 是否显示在导航栏中，1=启用，0=未启用
     */
    private Integer isDisplay;

}
