package cn.tedu.tmall.admin.content.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 列表项VO类：内容-顶踩日志
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class UpDownLogListItemVO implements Serializable {

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
     * 操作类型，0=踩，1=顶
     */
    private Integer opType;

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
