package cn.tedu.tmall.admin.content.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 列表项VO类：商城-审核日志
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class CheckLogListItemVO implements Serializable {

    /**
     * 数据ID
     */
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
    private String checkUsername;

    /**
     * 审核备注
     */
    private String checkRemarks;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm.ss")
    private LocalDateTime gmtCheck;

}
