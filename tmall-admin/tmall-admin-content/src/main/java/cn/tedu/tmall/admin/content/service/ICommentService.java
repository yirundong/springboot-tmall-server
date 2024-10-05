package cn.tedu.tmall.admin.content.service;

import cn.tedu.tmall.admin.content.pojo.vo.CommentListItemVO;
import cn.tedu.tmall.common.consts.data.ContentConsts;
import cn.tedu.tmall.common.pojo.authentication.CurrentPrincipal;
import cn.tedu.tmall.common.pojo.vo.PageData;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理评论数据的业务接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Transactional
public interface ICommentService extends ContentConsts {

    /**
     * 审核通过评论
     *
     * @param currentPrincipal 当事人
     * @param id               尝试审核通过的评论的ID
     * @param remarks          审核备注
     */
    void passCheck(CurrentPrincipal currentPrincipal, Long id, String remarks);

    /**
     * 拒绝审核评论
     *
     * @param currentPrincipal 当事人
     * @param id               尝试取消审核的评论的ID
     * @param remarks          审核备注
     */
    void cancelCheck(CurrentPrincipal currentPrincipal, Long id, String remarks);

    /**
     * 显示评论
     *
     * @param id 尝试显示的评论的ID
     */
    void setDisplay(Long id);

    /**
     * 隐藏（不显示）评论
     *
     * @param id 尝试隐藏的评论的ID
     */
    void setHidden(Long id);

    /**
     * 查询文章的评论列表，将使用默认的每页记录数
     *
     * @param pageNum 页码
     * @return 文章的评论数据列表
     */
    PageData<CommentListItemVO> listByArticle(Integer pageNum);

    /**
     * 查询文章的评论列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 文章的评论数据列表
     */
    PageData<CommentListItemVO> listByArticle(Integer pageNum, Integer pageSize);

    /**
     * 查询评论的评论列表，将使用默认的每页记录数
     *
     * @param pageNum 页码
     * @return 评论的评论列表
     */
    PageData<CommentListItemVO> listByComment(Integer pageNum);

    /**
     * 查询评论的评论列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 评论的评论列表
     */
    PageData<CommentListItemVO> listByComment(Integer pageNum, Integer pageSize);

}
