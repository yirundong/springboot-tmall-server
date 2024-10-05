package cn.tedu.tmall.passport.service.impl;

import cn.tedu.tmall.common.pojo.vo.PageData;
import cn.tedu.tmall.passport.dao.persist.repository.ILoginLogRepository;
import cn.tedu.tmall.passport.pojo.vo.LoginLogListItemVO;
import cn.tedu.tmall.passport.service.ILoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 处理用户登录日志的业务实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Service
public class LoginLogServiceImpl implements ILoginLogService {

    @Value("${tmall.dao.default-query-page-size}")
    private Integer defaultQueryPageSize;
    @Autowired
    private ILoginLogRepository loginLogRepository;

    public LoginLogServiceImpl() {
        log.info("创建业务对象：LoginLogServiceImpl");
    }

    @Override
    public PageData<LoginLogListItemVO> list(Integer pageNum) {
        log.debug("开始处理【查询登录日志列表】的业务，页码：{}", pageNum);
        PageData<LoginLogListItemVO> pageData = loginLogRepository.list(pageNum, defaultQueryPageSize);
        return pageData;
    }

    @Override
    public PageData<LoginLogListItemVO> list(Integer pageNum, Integer pageSize) {
        log.debug("开始处理【查询登录日志列表】的业务，页码：{}，每页记录数：{}", pageNum, pageSize);
        PageData<LoginLogListItemVO> pageData = loginLogRepository.list(pageNum, pageSize);
        return pageData;
    }

}
