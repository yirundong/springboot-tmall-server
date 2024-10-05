package cn.tedu.tmall.admin.content.service.impl;

import cn.tedu.tmall.admin.content.dao.persist.repository.ICheckLogRepository;
import cn.tedu.tmall.admin.content.pojo.vo.CheckLogListItemVO;
import cn.tedu.tmall.admin.content.service.ICheckLogService;
import cn.tedu.tmall.common.pojo.vo.PageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 处理审核日志的业务实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Service
public class CheckLogServiceImpl implements ICheckLogService {

    @Value("${tmall.dao.default-query-page-size}")
    private Integer defaultQueryPageSize;
    @Autowired
    private ICheckLogRepository checkLogRepository;

    public CheckLogServiceImpl() {
        log.debug("创建业务类对象：CheckLogServiceImpl");
    }

    @Override
    public PageData<CheckLogListItemVO> listArticleCheckLog(Integer pageNum) {
        log.debug("开始处理【查询文章审核日志列表】的业务，页码：{}", pageNum);
        return listByResourceType(RESOURCE_TYPE_GOODS, pageNum);
    }

    @Override
    public PageData<CheckLogListItemVO> listArticleCheckLog(Integer pageNum, Integer pageSize) {
        log.debug("开始处理【查询文章审核日志列表】的业务，页码：{}，每页记录数：{}", pageNum, pageSize);
        return listByResourceType(RESOURCE_TYPE_GOODS, pageNum, pageSize);
    }

    @Override
    public PageData<CheckLogListItemVO> listCommentCheckLog(Integer pageNum) {
        log.debug("开始处理【查询评论审核日志列表】的业务，页码：{}", pageNum);
        return listByResourceType(RESOURCE_TYPE_COMMENT, pageNum);
    }

    @Override
    public PageData<CheckLogListItemVO> listCommentCheckLog(Integer pageNum, Integer pageSize) {
        log.debug("开始处理【查询评论审核日志列表】的业务，页码：{}，每页记录数：{}", pageNum, pageSize);
        return listByResourceType(RESOURCE_TYPE_COMMENT, pageNum, pageSize);
    }

    /**
     * 查询审核日志列表，将使用默认的每页记录数
     *
     * @param resourceType 资源类型
     * @param pageNum      页码
     * @return 审核日志列表
     * @see cn.tedu.tmall.common.consts.data.ContentConsts
     */
    private PageData<CheckLogListItemVO> listByResourceType(int resourceType, Integer pageNum) {
        return checkLogRepository.listByResourceType(resourceType, pageNum, defaultQueryPageSize);
    }

    /**
     * 查询审核日志列表
     *
     * @param resourceType 资源类型
     * @param pageNum      页码
     * @param pageSize     每页记录数
     * @return 审核日志列表
     * @see cn.tedu.tmall.common.consts.data.ContentConsts
     */
    private PageData<CheckLogListItemVO> listByResourceType(int resourceType, Integer pageNum, Integer pageSize) {
        return checkLogRepository.listByResourceType(resourceType, pageNum, pageSize);
    }

}
