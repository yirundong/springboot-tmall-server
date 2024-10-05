package cn.tedu.tmall.admin.mall.dao.persist.mapper;

import cn.tedu.tmall.admin.mall.pojo.entity.Comment;
import cn.tedu.tmall.admin.mall.pojo.vo.CommentListItemVO;
import cn.tedu.tmall.admin.mall.pojo.vo.CommentStandardVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理评论评论数据的Mapper接口
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 19:56
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 根据ID查询评论
     *
     * @param id 评论ID
     * @return 匹配的评论，如果没有匹配的数据，则返回null
     */
    CommentStandardVO getStandardById(Long id);

    /**
     * 查询评论数据列表
     *
     * @return 评论数据列表
     */
    List<CommentListItemVO> list();

}
