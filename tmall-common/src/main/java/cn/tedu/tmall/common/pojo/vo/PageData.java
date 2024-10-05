package cn.tedu.tmall.common.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 14:52
 *
 * 这是我们自定义的服务器返回给前端的分类数据
 */
@Data
@Accessors(chain = true)
public class PageData<T> implements Serializable {

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 记录总数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer currentPage;

    /**
     * 最大页码
     */
    private Integer maxPage;

    /**
     * 数据列表
     */
    private List<T> list;

}