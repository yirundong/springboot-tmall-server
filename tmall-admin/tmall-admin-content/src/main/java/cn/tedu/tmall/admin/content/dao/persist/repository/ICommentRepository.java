package cn.tedu.tmall.admin.content.dao.persist.repository;

import cn.tedu.tmall.admin.content.pojo.entity.Comment;
import cn.tedu.tmall.admin.content.pojo.vo.CommentListItemVO;
import cn.tedu.tmall.admin.content.pojo.vo.CommentStandardVO;
import cn.tedu.tmall.common.consts.data.ContentConsts;
import cn.tedu.tmall.common.pojo.vo.PageData;

import java.util.Collection;

/**
 * 处理评论数据的存储库接口
 *
 * @author YiRunDong
 * @version 2.0
 */
public interface ICommentRepository extends ContentConsts {

    /**
     * 插入评论数据
     *
     * @param comment 评论数据
     * @return 受影响的行数
     */
    int insert(Comment comment);

    /**
     * 根据id删除评论数据
     *
     * @param id 评论ID
     * @return 受影响的行数
     */
    int deleteById(Long id);

    /**
     * 根据若干个ID批量删除评论数据
     *
     * @param idList 若干个评论ID的数组
     * @return 受影响的行数
     */
    int deleteByIds(Collection<Long> idList);

    /**
     * 根据文章删除评论数据
     *
     * @param resourceType 资源类型
     * @param resourceId   资源ID
     * @return 受影响的行数
     */
    int deleteByResource(Integer resourceType, Long resourceId);

    /**
     * 根据id修改评论数据
     *
     * @param comment 封装了评论ID和新数据的对象
     * @return 受影响的行数
     */
    int update(Comment comment);

    /**
     * 根据资源统计评论表中的数据的数量
     *
     * @param resourceType 资源类型
     * @param resourceId   资源ID
     * @return 文章匹配的评论数据的数量
     */
    int countByResource(Integer resourceType, Long resourceId);

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
     * @param pageNum      页码
     * @param pageSize     每页记录数
     * @return 评论数据列表
     */
    PageData<CommentListItemVO> listByResourceType(Integer resourceType, Integer pageNum, Integer pageSize);

}
