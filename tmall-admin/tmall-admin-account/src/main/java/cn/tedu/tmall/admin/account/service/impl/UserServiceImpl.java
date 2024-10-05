package cn.tedu.tmall.admin.account.service.impl;

import cn.tedu.tmall.admin.account.dao.cache.IUserCacheRepository;
import cn.tedu.tmall.admin.account.dao.persist.repository.IUserRepository;
import cn.tedu.tmall.admin.account.dao.persist.repository.IUserRoleRepository;
import cn.tedu.tmall.admin.account.pojo.entity.User;
import cn.tedu.tmall.admin.account.pojo.entity.UserRole;
import cn.tedu.tmall.admin.account.pojo.param.UserAddNewParam;
import cn.tedu.tmall.admin.account.pojo.param.UserUpdateInfoParam;
import cn.tedu.tmall.admin.account.pojo.vo.UserListItemVO;
import cn.tedu.tmall.admin.account.pojo.vo.UserStandardVO;
import cn.tedu.tmall.admin.account.service.IUserService;
import cn.tedu.tmall.common.enumerator.ServiceCode;
import cn.tedu.tmall.common.ex.ServiceException;
import cn.tedu.tmall.common.pojo.vo.PageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static cn.tedu.tmall.common.consts.data.CommonConsts.*;

/**
 * 处理用户数据的业务实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Value("${tmall.dao.default-query-page-size}")
    private Integer defaultQueryPageSize;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserCacheRepository userCacheRepository;
    @Autowired
    private IUserRoleRepository userRoleRepository;

    public UserServiceImpl() {
        log.debug("创建业务类对象：UserServiceImpl");
    }

    @Override
    public void addNew(UserAddNewParam userAddNewParam) {
        log.debug("开始处理【添加用户】的业务，参数：{}", userAddNewParam);

        // 检查用户名是否被占用
        {
            String username = userAddNewParam.getUsername();
            int count = userRepository.countByUsername(username);
            if (count > 0) {
                String message = "添加用户失败，用户名已经被占用！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
            }
        }

        // 检查手机号码是否被占用
        {
            String phone = userAddNewParam.getPhone();
            int count = userRepository.countByPhone(phone);
            if (count > 0) {
                String message = "添加用户失败，手机号码已经被占用！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
            }
        }

        // 检查电子邮箱是否被占用
        {
            String email = userAddNewParam.getEmail();
            int count = userRepository.countByEmail(email);
            if (count > 0) {
                String message = "添加用户失败，电子邮箱已经被占用！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
            }
        }

        User user = new User();
        BeanUtils.copyProperties(userAddNewParam, user);
        user.setLoginCount(0);
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        int rows = userRepository.insert(user);
        if (rows != 1) {
            String message = "添加用户失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }

        Long[] roleIds = userAddNewParam.getRoleIds();
        List<UserRole> userRoleList = new ArrayList<>();
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            userRoleList.add(userRole);
        }
        rows = userRoleRepository.insertBatch(userRoleList);
        if (rows < 1) {
            String message = "添加用户失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("开始处理【根据ID删除用户】的业务，参数：{}", id);
        if (id == 1) {
            String message = "删除用户失败，尝试访问的数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        Object queryResult = userRepository.getStandardById(id);
        if (queryResult == null) {
            String message = "删除用户失败，尝试访问的数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        log.debug("即将执行删除数据，参数：{}", id);
        int rows = userRepository.deleteById(id);
        if (rows != 1) {
            String message = "删除用户失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }

        log.debug("即将执行删除关联数据，参数：{}", id);
        rows = userRoleRepository.deleteByUserId(id);
        if (rows < 1) {
            String message = "删除用户失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }

        log.debug("即将删除缓存中的账号状态数据，无参数");
        userCacheRepository.deleteUserState(id);
    }

    @Override
    public void updateInfo(Long userId, UserUpdateInfoParam userUpdateInfoParam) {
        log.debug("开始处理【修改基本信息】的业务，用户ID：{}，新基本信息：{}", userId, userUpdateInfoParam);
        User user = new User();
        BeanUtils.copyProperties(userUpdateInfoParam, user);
        user.setId(userId);
        int rows = userRepository.updateById(user);
        if (rows != 1) {
            String message = "修改基本信息失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    @Override
    public void updatePassword(Long userId, String newPassword) {
        log.debug("开始处理【修改密码】的业务，用户ID：{}，新密码：{}", userId, newPassword);
        String encodedPassword = passwordEncoder.encode(newPassword);
        User user = new User();
        user.setId(userId);
        user.setPassword(encodedPassword);
        int rows = userRepository.updateById(user);
        if (rows != 1) {
            String message = "修改密码失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    @Override
    public void updateAvatar(Long userId, String avatar) {
        log.debug("开始处理【修改头像】的业务，用户ID：{}，新头像：{}", userId, avatar);
        User user = new User();
        user.setId(userId);
        user.setAvatar(avatar);
        int rows = userRepository.updateById(user);
        if (rows != 1) {
            String message = "修改头像失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    @Override
    public void updatePhone(Long userId, String phone) {
        log.debug("开始处理【修改手机号码】的业务，用户ID：{}，新手机号码：{}", userId, phone);
        int count = userRepository.countByPhoneAndNotId(phone, userId);
        if (count > 0) {
            String message = "修改手机号码失败，手机号码已经被占用！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        User user = new User();
        user.setId(userId);
        user.setPhone(phone);
        int rows = userRepository.updateById(user);
        if (rows != 1) {
            String message = "修改手机号码失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    @Override
    public void updateEmail(Long userId, String email) {
        log.debug("开始处理【修改电子邮箱】的业务，用户ID：{}，新手机号码：{}", userId, email);
        int count = userRepository.countByEmailAndNotId(email, userId);
        if (count > 0) {
            String message = "修改电子邮箱失败，电子邮箱已经被占用！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        User user = new User();
        user.setId(userId);
        user.setEmail(email);
        int rows = userRepository.updateById(user);
        if (rows != 1) {
            String message = "修改电子邮箱失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    @Override
    public void setEnable(Long userId) {
        log.debug("开始处理【启用用户】的业务，参数：{}", userId);
        updateEnableById(userId, ENABLE_STATE_ON);
    }

    @Override
    public void setDisable(Long userId) {
        log.debug("开始处理【禁用用户】的业务，参数：{}", userId);
        updateEnableById(userId, ENABLE_STATE_OFF);
    }

    @Override
    public UserStandardVO getStandardById(Long userId) {
        log.debug("开始处理【根据ID查询用户】业务，参数：{}", userId);
        UserStandardVO currentUser = userRepository.getStandardById(userId);
        if (currentUser == null) {
            String message = "获取用户详情失败，尝试访问的用户数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }
        return currentUser;
    }

    @Override
    public PageData<UserListItemVO> list(Integer pageNum) {
        log.debug("开始处理【查询用户列表】的业务，页码：{}", pageNum);
        return userRepository.list(pageNum, defaultQueryPageSize);
    }

    @Override
    public PageData<UserListItemVO> list(Integer pageNum, Integer pageSize) {
        log.debug("开始处理【查询用户列表】的业务，页码：{}，每页记录数：{}", pageNum, pageSize);
        return userRepository.list(pageNum, pageSize);
    }

    private void updateEnableById(Long id, Integer enable) {
        log.debug("开始处理【{}用户】的业务，ID：{}，目标状态：{}", ENABLE_STATE_TEXT[enable], id, enable);
        if (id == 1) {
            String message = ENABLE_STATE_TEXT[enable] + "用户失败，尝试访问的数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        UserStandardVO queryResult = userRepository.getStandardById(id);
        if (queryResult == null) {
            String message = ENABLE_STATE_TEXT[enable] + "用户失败，尝试访问的数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        if (queryResult.getEnable().equals(enable)) {
            String message = ENABLE_STATE_TEXT[enable] + "用户失败，当前用户已经处于"
                    + ENABLE_STATE_TEXT[enable] + "状态！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        User user = new User();
        user.setId(id);
        user.setEnable(enable);
        log.debug("即将修改数据，参数：{}", user);
        int rows = userRepository.updateById(user);
        if (rows != 1) {
            String message = ENABLE_STATE_TEXT[enable] + "用户失败，服务器忙，请稍后再次尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }

        if (enable == 0) {
            userCacheRepository.setUserDisabled(id);
        }
    }

}
