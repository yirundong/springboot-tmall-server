package cn.tedu.tmall.admin.account.dao.persist.repository;

import cn.tedu.tmall.admin.account.pojo.entity.UserRole;

import java.util.List;

/**
 * 处理用户角色数据的数据访问接口
 *
 * @author YiRunDong
 * @version 2.0
 */
public interface IUserRoleRepository {

    /**
     * 批量插入用户与角色的关联数据
     *
     * @param userRoleList 若干个用户与角色的关联数据的集合
     * @return 受影响的行数
     */
    int insertBatch(List<UserRole> userRoleList);

    /**
     * 根据用户id删除用户与角色的关联数据
     *
     * @param userId 用户id
     * @return 受影响的行数
     */
    int deleteByUserId(Long userId);
}
