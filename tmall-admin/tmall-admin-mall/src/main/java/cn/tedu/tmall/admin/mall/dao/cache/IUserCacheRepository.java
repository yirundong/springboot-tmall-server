package cn.tedu.tmall.admin.mall.dao.cache;

import cn.tedu.tmall.common.consts.cache.PassportCacheConsts;
import cn.tedu.tmall.common.pojo.po.UserStatePO;

/**
 * 用户信息的redis处理的接口
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 18:56
 */
public interface IUserCacheRepository extends PassportCacheConsts {

    boolean deleteUserState(Long userId);

    UserStatePO getUserState(Long userId);

}