package cn.tedu.tmall.admin.mall.service.impl;

import cn.tedu.tmall.admin.mall.dao.persist.repository.ICheckLogRepository;
import cn.tedu.tmall.admin.mall.dao.persist.repository.ICommentRepository;
import cn.tedu.tmall.admin.mall.pojo.entity.CheckLog;
import cn.tedu.tmall.admin.mall.pojo.entity.Comment;
import cn.tedu.tmall.admin.mall.pojo.vo.CommentListItemVO;
import cn.tedu.tmall.admin.mall.pojo.vo.CommentStandardVO;
import cn.tedu.tmall.admin.mall.service.ICommentService;
import cn.tedu.tmall.common.enumerator.ServiceCode;
import cn.tedu.tmall.common.ex.ServiceException;
import cn.tedu.tmall.common.pojo.authentication.CurrentPrincipal;
import cn.tedu.tmall.common.pojo.vo.PageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 处理评论数据的业务实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Service
public class CommentServiceImpl implements ICommentService {

    @Value("${tmall.dao.default-query-page-size}")
    private Integer defaultQueryPageSize;
    @Autowired
    private ICommentRepository commentRepository;
    @Autowired
    private ICheckLogRepository checkLogRepository;

    public CommentServiceImpl() {
        log.debug("创建业务类对象：CommentServiceImpl");
    }

    @Override
    public void passCheck(CurrentPrincipal currentPrincipal, Long goodsId, String remarks) {
        log.debug("开始处理【审核通过评论】的业务，参数：{}", goodsId);
        updateCheckById(currentPrincipal, goodsId, CHECK_STATE_PASS, remarks);
    }

    @Override
    public void rejectCheck(CurrentPrincipal currentPrincipal, Long goodsId, String remarks) {
        log.debug("开始处理【拒绝审核评论】的业务，参数：{}", goodsId);
        updateCheckById(currentPrincipal, goodsId, CHECK_STATE_REJECT, remarks);
    }

    @Override
    public PageData<CommentListItemVO> list(Integer pageNum) {
        log.debug("开始处理【查询商品的评论列表】的业务，页码：{}", pageNum);
        return commentRepository.list(pageNum, defaultQueryPageSize);
    }

    @Override
    public PageData<CommentListItemVO> list(Integer pageNum, Integer pageSize) {
        log.debug("开始处理【查询商品的评论列表】的业务，页码：{}，每页记录数：{}", pageNum, pageSize);
        return commentRepository.list(pageNum, pageSize);
    }

    /**
     * 根据ID修改评论的审核状态
     *
     * @param currentPrincipal 当事人
     * @param id               评论ID
     * @param checkState       新状态
     * @param remarks          审核备注
     */
    private void updateCheckById(CurrentPrincipal currentPrincipal, Long id, Integer checkState, String remarks) {
        CommentStandardVO currentComment = commentRepository.getStandardById(id);
        if (currentComment == null) {
            String message = "将评论的审核状态修改为【" + CHECK_STATE_TEXT[checkState] + "】失败，评论数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        if (currentComment.getCheckState().equals(checkState)) {
            String message = "将评论的审核状态修改为【" + CHECK_STATE_TEXT[checkState] + "】失败，此评论已经处于" + CHECK_STATE_TEXT[checkState] + "状态！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        Comment updateComment = new Comment();
        updateComment.setId(id);
        updateComment.setCheckState(checkState);
        int rows = commentRepository.update(updateComment);
        if (rows != 1) {
            String message = "将评论的审核状态修改为【" + CHECK_STATE_TEXT[checkState] + "】失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }

        String content = currentComment.getContent();
        String brief = content.length() < BRIEF_MAX_LENGTH ? content : content.substring(0, BRIEF_MAX_LENGTH);

        CheckLog checkLog = new CheckLog();
        checkLog.setResourceType(RESOURCE_TYPE_COMMENT);
        checkLog.setResourceId(id);
        checkLog.setResourceBrief(brief);
        checkLog.setCheckUserId(currentPrincipal.getId());
        checkLog.setCheckUsername(currentPrincipal.getUsername());
        checkLog.setCheckRemarks(remarks);
        checkLog.setOriginalState(currentComment.getCheckState());
        checkLog.setNewState(checkState);
        checkLog.setGmtCheck(LocalDateTime.now());
        rows = checkLogRepository.insert(checkLog);
        if (rows != 1) {
            String message = "将评论的审核状态修改为【" + CHECK_STATE_TEXT[checkState] + "】失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }
    }

}
