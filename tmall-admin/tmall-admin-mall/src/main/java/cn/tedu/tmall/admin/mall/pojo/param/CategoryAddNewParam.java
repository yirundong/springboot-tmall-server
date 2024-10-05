package cn.tedu.tmall.admin.mall.pojo.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @ApiModelProperty(value = "类别名称", required = true)
    @NotNull(message = "请提交类别名称")
    @Pattern(regexp = "^.{1,6}$", message = "类别名称必须由1~6个字符组成")
    private String name;

    /**
     * 父级类别ID，如果无父级，则为0
     */
    @ApiModelProperty(value = "父级类别ID，如果无父级，则为0", required = true)
    @NotNull(message = "请选择父级类别")
    @Range(message = "请提交有效的父级类别")
    private Long parentId;

    /**
     * 关键词列表，各关键词使用英文的逗号分隔
     */
    @ApiModelProperty("关键词列表，各关键词使用英文的逗号分隔")
    private String keywords;

    /**
     * 排序序号
     */
    @ApiModelProperty("排序序号")
    @NotNull(message = "请提交排序序号")
    @Range(max = 255, message = "排序序号必须是0~255之间的数字")
    private Integer sort;

    /**
     * 图标图片的URL
     */
    @ApiModelProperty("图标图片的URL")
    private String icon;

    /**
     * 是否启用，1=启用，0=未启用
     */
    @ApiModelProperty("是否启用，1=启用，0=未启用")
    @NotNull(message = "请提交是否启用的状态")
    @Range(max = 1, message = "启用状态的值必须是0或者1")
    private Integer enable;

    /**
     * 是否显示在导航栏中，1=启用，0=未启用
     */
    @ApiModelProperty("是否显示在导航栏中，1=启用，0=未启用")
    @NotNull(message = "请提交是否显示的状态")
    @Range(max = 1, message = "显示状态的值必须是0或者1")
    private Integer isDisplay;

}
