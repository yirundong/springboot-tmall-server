package cn.tedu.tmall.admin.content.service;

import cn.tedu.tmall.admin.content.pojo.vo.CheckLogListItemVO;
import cn.tedu.tmall.common.consts.data.MallConsts;
import cn.tedu.tmall.common.pojo.vo.PageData;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理审核日志的业务接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Transactional
public interface ICheckLogService extends MallConsts {

    /**
     * 查询文章审核日志列表，将使用默认的每页记录数
     *
     * @param pageNum 页码
     * @return 审核日志列表
     */
    PageData<CheckLogListItemVO> listArticleCheckLog(Integer pageNum);

    /**
     * 查询文章审核日志列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 审核日志列表
     */
    PageData<CheckLogListItemVO> listArticleCheckLog(Integer pageNum, Integer pageSize);

    /**
     * 查询评论审核日志列表，将使用默认的每页记录数
     *
     * @param pageNum 页码
     * @return 审核日志列表
     */
    PageData<CheckLogListItemVO> listCommentCheckLog(Integer pageNum);

    /**
     * 查询评论审核日志列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 审核日志列表
     */
    PageData<CheckLogListItemVO> listCommentCheckLog(Integer pageNum, Integer pageSize);

}
