package cn.tedu.tmall.admin.mall.service;

import cn.tedu.tmall.admin.mall.pojo.vo.CommentListItemVO;
import cn.tedu.tmall.common.consts.data.MallConsts;
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
public interface ICommentService extends MallConsts {

    /**
     * 审核通过评论
     *
     * @param currentPrincipal 当事人
     * @param goodsId          尝试审核通过的评论的ID
     * @param remarks          备注信息
     */
    void passCheck(CurrentPrincipal currentPrincipal, Long goodsId, String remarks);

    /**
     * 拒绝审核评论
     *
     * @param currentPrincipal 当事人
     * @param goodsId          尝试拒绝审核的评论的ID
     * @param remarks          备注信息
     */
    void rejectCheck(CurrentPrincipal currentPrincipal, Long goodsId, String remarks);

    /**
     * 查询商品的评论列表，将使用默认的每页记录数
     *
     * @param pageNum 页码
     * @return 商品的评论列表
     */
    PageData<CommentListItemVO> list(Integer pageNum);

    /**
     * 查询商品的评论列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 商品的评论列表
     */
    PageData<CommentListItemVO> list(Integer pageNum, Integer pageSize);

}
