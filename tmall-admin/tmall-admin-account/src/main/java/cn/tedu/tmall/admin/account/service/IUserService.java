package cn.tedu.tmall.admin.account.service;

import cn.tedu.tmall.admin.account.pojo.param.UserAddNewParam;
import cn.tedu.tmall.admin.account.pojo.param.UserUpdateInfoParam;
import cn.tedu.tmall.admin.account.pojo.vo.UserListItemVO;
import cn.tedu.tmall.admin.account.pojo.vo.UserStandardVO;
import cn.tedu.tmall.common.consts.data.UserConsts;
import cn.tedu.tmall.common.pojo.vo.PageData;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理用户数据的业务接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Transactional
public interface IUserService extends UserConsts {

    /**
     * 添加用户
     *
     * @param userAddNewParam 用户数据
     */
    void addNew(UserAddNewParam userAddNewParam);

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    void delete(Long id);

    /**
     * 修改基本信息
     *
     * @param userId              用户ID
     * @param userUpdateInfoParam 新的基本信息
     */
    void updateInfo(Long userId, UserUpdateInfoParam userUpdateInfoParam);

    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param newPassword 新密码
     */
    void updatePassword(Long userId, String newPassword);

    /**
     * 修改头像
     *
     * @param userId 用户ID
     * @param avatar 新头像的URL
     */
    void updateAvatar(Long userId, String avatar);

    /**
     * 修改手机号码
     *
     * @param userId 用户ID
     * @param phone  新手机号码
     */
    void updatePhone(Long userId, String phone);

    /**
     * 修改电子邮箱
     *
     * @param userId 用户ID
     * @param email  新电子邮箱
     */
    void updateEmail(Long userId, String email);

    /**
     * 启用用户
     *
     * @param userId 用户ID
     */
    void setEnable(Long userId);

    /**
     * 禁用用户
     *
     * @param userId 用户ID
     */
    void setDisable(Long userId);

    /**
     * 根据ID查询用户
     *
     * @param userId 用户ID
     * @return 匹配的用户信息
     */
    UserStandardVO getStandardById(Long userId);

    /**
     * 查询用户列表，将使用默认的每页记录数
     *
     * @param pageNum 页码
     * @return 用户列表
     */
    PageData<UserListItemVO> list(Integer pageNum);

    /**
     * 查询用户列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 用户列表
     */
    PageData<UserListItemVO> list(Integer pageNum, Integer pageSize);

}
