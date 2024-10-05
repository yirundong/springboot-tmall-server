package cn.tedu.tmall.passport.dao.persist.repository.impl;

import cn.tedu.tmall.passport.dao.persist.mapper.UserMapper;
import cn.tedu.tmall.passport.dao.persist.repository.IUserRepository;
import cn.tedu.tmall.passport.pojo.entity.User;
import cn.tedu.tmall.passport.pojo.vo.UserLoginInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 处理用户数据的存储库实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Repository
public class UserRepositoryImpl implements IUserRepository {

    @Autowired
    private UserMapper userMapper;

    public UserRepositoryImpl() {
        log.info("创建存储库对象：UserRepositoryImpl");
    }

    @Override
    public int updateLastLogin(Long id, Integer loginCount, String lastLoginIp, LocalDateTime gmtLastLogin) {
        log.debug("开始执行【更新登录次数】的数据访问，用户ID：{}，登录次数：{}", id, loginCount);
        User user = new User();
        user.setId(id);
        user.setLoginCount(loginCount);
        user.setLastLoginIp(lastLoginIp);
        user.setGmtLastLogin(gmtLastLogin);
        return userMapper.updateById(user);
    }

    @Override
    public UserLoginInfoVO getLoginInfoByUsername(String username) {
        log.debug("开始执行【根据用户名查询用户登录信息】的数据访问，参数：{}", username);
        return userMapper.getLoginInfoByUsername(username);
    }

}
