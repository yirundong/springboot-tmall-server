package cn.tedu.tmall.admin.account.dao.persist.repository;

import cn.tedu.tmall.admin.account.pojo.entity.User;
import cn.tedu.tmall.admin.account.pojo.vo.UserListItemVO;
import cn.tedu.tmall.admin.account.pojo.vo.UserStandardVO;
import cn.tedu.tmall.common.pojo.vo.PageData;

/**
 * 处理用户数据的存储库接口
 *
 * @author YiRunDong
 * @version 2.0
 */
public interface IUserRepository {

    /**
     * 插入用户数据
     *
     * @param user 用户数据
     * @return 受影响的行数
     */
    int insert(User user);

    /**
     * 根据用户ID删除用户数据
     *
     * @param id 用户id
     * @return 受影响的行数
     */
    int deleteById(Long id);

    /**
     * 根据用户ID修改用户的数据
     *
     * @param user 封装了用户id和新的数据的对象
     * @return 受影响的行数
     */
    int updateById(User user);

    /**
     * 根据用户名统计用户数据的数量
     *
     * @param username 用户名
     * @return 匹配用户名的用户数据的数量
     */
    int countByUsername(String username);

    /**
     * 根据手机号码统计用户数据的数量
     *
     * @param phone 手机号码
     * @return 匹配手机号码的用户数据的数量
     */
    int countByPhone(String phone);

    /**
     * 统计匹配手机号码但非用户ID的用户数据的数量，通常用于检查手机号码是否被其他用户占用
     *
     * @param phone  手机号码
     * @param userId 用户ID
     * @return 匹配的用户数据的数量
     */
    int countByPhoneAndNotId(String phone, Long userId);

    /**
     * 根据电子邮箱统计用户数据的数量
     *
     * @param email 电子邮箱
     * @return 匹配电子邮箱的用户数据的数量
     */
    int countByEmail(String email);

    /**
     * 统计匹配电子邮箱但非用户ID的用户数据的数量，通常用于检查电子邮箱是否被其他用户占用
     *
     * @param email  电子邮箱
     * @param userId 用户ID
     * @return 匹配的用户数据的数量
     */
    int countByEmailAndNotId(String email, Long userId);

    /**
     * 根据用户ID查询用户数据详情
     *
     * @param id 用户id
     * @return 匹配的用户数据详情，如果没有匹配的数据，则返回null
     */
    UserStandardVO getStandardById(Long id);

    /**
     * 查询用户数据列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 用户数据列表
     */
    PageData<UserListItemVO> list(Integer pageNum, Integer pageSize);

}
