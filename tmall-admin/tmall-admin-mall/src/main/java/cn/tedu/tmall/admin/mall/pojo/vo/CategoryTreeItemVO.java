package cn.tedu.tmall.admin.mall.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 类别完整"树"结构节点项的VO类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
@Accessors(chain = true)
public class CategoryTreeItemVO implements Serializable {

    /**
     * 数据ID，Element UI控件要求名为value
     */
    @ApiModelProperty("类别ID")
    private Long value;

    /**
     * 类别名称，Element UI控件要求名为label
     */
    @ApiModelProperty("类别名称")
    private String label;

    /**
     * 子级类别列表，Element UI控件要求名为children
     */
    @ApiModelProperty("子级类别列表")
    private List<CategoryTreeItemVO> children;

}
