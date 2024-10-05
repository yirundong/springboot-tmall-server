package cn.tedu.tmall.admin.mall.dao.cache.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.tedu.tmall.admin.mall.dao.cache.IUserCacheRepository;
import cn.tedu.tmall.common.pojo.po.UserStatePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Map;

/**
 * redis用户信息处理的实现类
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 18:58
 */
@Repository
public class UserCacheRepositoryImpl implements IUserCacheRepository {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public boolean deleteUserState(Long userId) {
        String key = KEY_PREFIX_USER_STATE + userId;
        return redisTemplate.delete(key);
    }

    @Override
    public UserStatePO getUserState(Long userId) {
        String key = KEY_PREFIX_USER_STATE + userId;
        HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
        Map<Object, Object> entries = opsForHash.entries(key);
        UserStatePO userStatePO = null;
        if (entries.size() > 0) {
            userStatePO = BeanUtil.mapToBean(entries, UserStatePO.class, true, null);
        }
        return userStatePO;
    }

}