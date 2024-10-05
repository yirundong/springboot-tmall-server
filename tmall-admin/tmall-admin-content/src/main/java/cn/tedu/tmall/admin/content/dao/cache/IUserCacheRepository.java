package cn.tedu.tmall.admin.content.dao.cache;

import cn.tedu.tmall.common.consts.cache.PassportCacheConsts;
import cn.tedu.tmall.common.pojo.po.UserStatePO;

/**
 * 处理用户缓存数据的存储库接口
 *
 * @author YiRunDong
 * @version 2.0
 */
public interface IUserCacheRepository extends PassportCacheConsts {

    /**
     * 从缓存中删除用户登录信息
     *
     * @param userId 用户ID
     * @return 如果删除成功，将返回true，否则，将返回false
     */
    boolean deleteUserState(Long userId);

    /**
     * 从缓存中读取用户登录信息
     *
     * @param userId 用户ID
     * @return 匹配的用户信息，如果没有匹配的数据，则返回null
     */
    UserStatePO getUserState(Long userId);

}
