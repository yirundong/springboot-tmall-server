package cn.tedu.tmall.admin.mall.pojo.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 修改类别的参数类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class CategoryUpdateInfoParam  implements Serializable {

    /**
     * 类别名称
     */
    @ApiModelProperty(value = "类别名称", required = true)
    @NotNull(message = "请提交类别名称")
    @Pattern(regexp = "^.{1,6}$", message = "类别名称必须由1~6个字符组成")
    private String name;

    /**
     * 关键词列表，各关键词使用英文的逗号分隔
     */
    @ApiModelProperty("关键词列表，各关键词使用英文的逗号分隔")
    private String keywords;

    @ApiModelProperty("排序序号")
    @NotNull(message = "请提交排序序号")
    @Range(max = 255, message = "排序序号必须是0~255之间的数字")
    private Integer sort;

    /**
     * 图标图片的URL
     */
    @ApiModelProperty("图标图片的URL")
    private String icon;

}
