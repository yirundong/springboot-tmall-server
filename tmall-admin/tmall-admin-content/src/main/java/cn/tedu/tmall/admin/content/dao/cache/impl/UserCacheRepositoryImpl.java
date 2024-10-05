package cn.tedu.tmall.admin.content.dao.cache.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.tedu.tmall.admin.content.dao.cache.IUserCacheRepository;
import cn.tedu.tmall.common.pojo.po.UserStatePO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Map;

/**
 * 处理用户缓存数据的存储库实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Repository
public class UserCacheRepositoryImpl implements IUserCacheRepository {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    public UserCacheRepositoryImpl() {
        log.info("创建缓存存储库对象：UserCacheRepositoryImpl");
    }

    @Override
    public boolean deleteUserState(Long userId) {
        log.debug("开始处理【从缓存中删除用户登录信息】的数据访问，用户ID：{}，", userId);
        String key = KEY_PREFIX_USER_STATE + userId;
        return redisTemplate.delete(key);
    }

    @Override
    public UserStatePO getUserState(Long userId) {
        log.debug("开始处理【从缓存中读取用户登录信息】的数据访问，用户ID：{}，", userId);
        String key = KEY_PREFIX_USER_STATE + userId;
        UserStatePO userStatePO = null;
        HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
        Map<Object, Object> entries = opsForHash.entries(key);
        if (entries.size() != 0) {
            userStatePO = BeanUtil.mapToBean(entries, UserStatePO.class, true, null);
        }
        return userStatePO;
    }

}
