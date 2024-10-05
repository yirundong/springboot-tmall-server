package cn.tedu.tmall.admin.content.dao.persist.mapper;

import cn.tedu.tmall.admin.content.pojo.entity.Comment;
import cn.tedu.tmall.admin.content.pojo.vo.CommentListItemVO;
import cn.tedu.tmall.admin.content.pojo.vo.CommentStandardVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理评论数据的Mapper接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 根据id查询评论数据详情
     *
     * @param id 评论ID
     * @return 匹配的评论数据详情，如果没有匹配的数据，则返回null
     */
    CommentStandardVO getStandardById(Long id);

    /**
     * 查询评论数据列表
     *
     * @param resourceType 资源类型
     * @return 评论数据列表
     */
    List<CommentListItemVO> listByResourceType(Integer resourceType);

}
