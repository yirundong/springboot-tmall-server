package cn.tedu.tmall.admin.content.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 列表项VO类：内容-评论
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class CommentListItemVO implements Serializable {

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
     * 评论内容
     */
    private String content;

    /**
     * IP
     */
    private String ip;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 顶数量
     */
    private Integer upCount;

    /**
     * 踩数量
     */
    private Integer downCount;

    /**
     * 审核状态，0=未审核，1=审核通过，2=拒绝审核
     */
    private Integer checkState;

    /**
     * 显示状态，0=不显示，1=显示
     */
    private Integer isDisplay;

    /**
     * 数据创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm.ss")
    private LocalDateTime gmtCreate;

    /**
     * 数据最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm.ss")
    private LocalDateTime gmtModified;

}
