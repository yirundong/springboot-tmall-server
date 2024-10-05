package cn.tedu.tmall.passport.dao.persist.repository;

import cn.tedu.tmall.passport.pojo.vo.UserLoginInfoVO;

import java.time.LocalDateTime;

/**
 * 处理用户数据的存储库接口
 *
 * @author YiRunDong
 * @version 2.0
 */
public interface IUserRepository {

    /**
     * 更新登录次数
     *
     * @param id           用户ID
     * @param loginCount   登录次数
     * @param lastLoginIp  最后登录IP
     * @param gmtLastLogin 最后登录时间
     * @return 受影响的行数
     */
    int updateLastLogin(Long id, Integer loginCount, String lastLoginIp, LocalDateTime gmtLastLogin);

    /**
     * 根据用户名查询用户的登录信息
     *
     * @param username 用户名
     * @return 匹配的用户的登录信息，如果没有匹配的数据，则返回null
     */
    UserLoginInfoVO getLoginInfoByUsername(String username);

}
