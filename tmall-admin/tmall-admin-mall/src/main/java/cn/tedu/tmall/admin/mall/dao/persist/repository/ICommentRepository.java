package cn.tedu.tmall.admin.mall.dao.persist.repository;

import cn.tedu.tmall.admin.mall.pojo.entity.Comment;
import cn.tedu.tmall.admin.mall.pojo.vo.CommentListItemVO;
import cn.tedu.tmall.admin.mall.pojo.vo.CommentStandardVO;
import cn.tedu.tmall.common.pojo.vo.PageData;

/**
 * 处理商品评论数据的存储库接口
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 19:55
 */
public interface ICommentRepository {

    /**
     * 根据商品ID删除评论
     *
     * @param goodsId 商品ID
     * @return 受影响的行数
     */
    int deleteByGoods(Long goodsId);

    /**
     * 根据ID修改评论数据
     *
     * @param comment 封装了评论ID和新数据的对象
     * @return 受影响的行数
     */
    int update(Comment comment);

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
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 评论数据列表
     */
    PageData<CommentListItemVO> list(Integer pageNum, Integer pageSize);

}
