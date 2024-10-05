package cn.tedu.tmall.passport.dao.persist.repository.impl;

import cn.tedu.tmall.common.pojo.vo.PageData;
import cn.tedu.tmall.common.util.PageInfoToPageDataConverter;
import cn.tedu.tmall.passport.dao.persist.mapper.LoginLogMapper;
import cn.tedu.tmall.passport.dao.persist.repository.ILoginLogRepository;
import cn.tedu.tmall.passport.pojo.entity.LoginLog;
import cn.tedu.tmall.passport.pojo.vo.LoginLogListItemVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理登录日志数据的存储库实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Repository
public class LoginLogRepositoryImpl implements ILoginLogRepository {

    @Autowired
    private LoginLogMapper loginLogMapper;

    public LoginLogRepositoryImpl() {
        log.info("创建存储库对象：LoginLogRepositoryImpl");
    }

    @Override
    public int insert(LoginLog loginLog) {
        log.debug("开始执行【插入登录日志】的数据访问，参数：{}", loginLog);
        return loginLogMapper.insert(loginLog);
    }

    @Override
    public PageData<LoginLogListItemVO> list(Integer pageNum, Integer pageSize) {
        log.debug("开始执行【查询用户登录日志列表】的数据访问，页码：{}，每页记录数：{}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<LoginLogListItemVO> list = loginLogMapper.list();
        PageInfo<LoginLogListItemVO> pageInfo = new PageInfo<>(list);
        PageData<LoginLogListItemVO> pageData = PageInfoToPageDataConverter.convert(pageInfo);
        return pageData;
    }

}
