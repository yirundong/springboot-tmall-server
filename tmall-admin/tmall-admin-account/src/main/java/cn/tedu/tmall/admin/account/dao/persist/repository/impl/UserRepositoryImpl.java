package cn.tedu.tmall.admin.account.dao.persist.repository.impl;

import cn.tedu.tmall.admin.account.dao.persist.mapper.UserMapper;
import cn.tedu.tmall.admin.account.dao.persist.repository.IUserRepository;
import cn.tedu.tmall.admin.account.pojo.entity.User;
import cn.tedu.tmall.admin.account.pojo.vo.UserListItemVO;
import cn.tedu.tmall.admin.account.pojo.vo.UserStandardVO;
import cn.tedu.tmall.common.pojo.vo.PageData;
import cn.tedu.tmall.common.util.PageInfoToPageDataConverter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public int insert(User user) {
        log.debug("开始执行【插入用户】的数据访问，参数：{}", user);
        return userMapper.insert(user);
    }

    @Override
    public int deleteById(Long id) {
        log.debug("开始执行【根据ID删除用户】的数据访问，参数：{}", id);
        return userMapper.deleteById(id);
    }

    @Override
    public int updateById(User user) {
        log.debug("开始执行【更新用户】的数据访问，参数：{}", user);
        return userMapper.updateById(user);
    }

    @Override
    public int countByUsername(String username) {
        log.debug("开始执行【根据用户名统计用户的数量】的数据访问，参数：{}", username);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public int countByPhone(String phone) {
        log.debug("开始执行【根据手机号码统计用户的数量】的数据访问，参数：{}", phone);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public int countByPhoneAndNotId(String phone, Long userId) {
        log.debug("开始执行【统计匹配手机号码但非用户ID的用户数据的数量】的数据访问，手机号码：{}，用户ID：{}", phone, userId);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        queryWrapper.ne("id", userId);
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public int countByEmail(String email) {
        log.debug("开始执行【根据电子邮箱统计用户的数量】的数据访问，参数：{}", email);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public int countByEmailAndNotId(String email, Long userId) {
        log.debug("开始执行【统计匹配电子邮箱但非用户ID的用户数据的数量】的数据访问，电子邮箱：{}，用户ID：{}", email, userId);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.ne("id", userId);
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public UserStandardVO getStandardById(Long id) {
        log.debug("开始执行【根据ID查询用户详情】的数据访问，参数：{}", id);
        return userMapper.getStandardById(id);
    }

    @Override
    public PageData<UserListItemVO> list(Integer pageNum, Integer pageSize) {
        log.debug("开始执行【查询用户列表】的数据访问，页码：{}，每页记录数：{}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<UserListItemVO> list = userMapper.list();
        PageInfo<UserListItemVO> pageInfo = new PageInfo<>(list);
        return PageInfoToPageDataConverter.convert(pageInfo);
    }

}
