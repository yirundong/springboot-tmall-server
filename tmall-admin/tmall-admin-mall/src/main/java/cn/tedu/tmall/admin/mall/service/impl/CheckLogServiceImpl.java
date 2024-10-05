package cn.tedu.tmall.admin.mall.service.impl;

import cn.tedu.tmall.admin.mall.dao.persist.repository.ICheckLogRepository;
import cn.tedu.tmall.admin.mall.pojo.vo.CheckLogListItemVO;
import cn.tedu.tmall.admin.mall.service.ICheckLogService;
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
    public PageData<CheckLogListItemVO> listGoodsCheckLog(Integer pageNum) {
        log.debug("开始处理【查询商品审核日志列表】的业务，页码：{}", pageNum);
        return checkLogRepository.listByResourceType(RESOURCE_TYPE_GOODS, pageNum, defaultQueryPageSize);
    }

    @Override
    public PageData<CheckLogListItemVO> listGoodsCheckLog(Integer pageNum, Integer pageSize) {
        log.debug("开始处理【查询商品审核日志列表】的业务，页码：{}，每页记录数：{}", pageNum, pageSize);
        return checkLogRepository.listByResourceType(RESOURCE_TYPE_GOODS, pageNum, pageSize);
    }

    @Override
    public PageData<CheckLogListItemVO> listCommentCheckLog(Integer pageNum) {
        log.debug("开始处理【查询评论审核日志列表】的业务，页码：{}", pageNum);
        return checkLogRepository.listByResourceType(RESOURCE_TYPE_COMMENT, pageNum, defaultQueryPageSize);
    }

    @Override
    public PageData<CheckLogListItemVO> listCommentCheckLog(Integer pageNum, Integer pageSize) {
        log.debug("开始处理【查询评论审核日志列表】的业务，页码：{}，每页记录数：{}", pageNum, pageSize);
        return checkLogRepository.listByResourceType(RESOURCE_TYPE_COMMENT, pageNum, pageSize);
    }

}
