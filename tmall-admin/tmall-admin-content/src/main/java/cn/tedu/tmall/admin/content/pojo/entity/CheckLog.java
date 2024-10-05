package cn.tedu.tmall.admin.content.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体类：内容-审核日志
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
@TableName("content_check_log")
public class CheckLog implements Serializable {

    /**
     * 数据ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 资源类型，0=文章，1=评论
     */
    private Integer resourceType;

    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 资源摘要，截取的文章标题或评论
     */
    private String resourceBrief;

    /**
     * 原审核状态
     */
    private Integer originalState;

    /**
     * 新审核状态
     */
    private Integer newState;

    /**
     * 审核人ID
     */
    private Long checkUserId;

    /**
     * 审核人用户名
     */
    @TableField("check_user_name")
    private String checkUsername;

    /**
     * 审核备注
     */
    private String checkRemarks;

    /**
     * 审核时间
     */
    private LocalDateTime gmtCheck;

    /**
     * 数据创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;
    /**
     * 数据最后修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

}
