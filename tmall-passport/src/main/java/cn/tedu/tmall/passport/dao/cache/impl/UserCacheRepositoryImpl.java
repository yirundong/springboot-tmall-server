package cn.tedu.tmall.passport.dao.cache.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.tedu.tmall.common.pojo.po.UserStatePO;
import cn.tedu.tmall.passport.dao.cache.IUserCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 处理用户缓存数据的存储库实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Repository
public class UserCacheRepositoryImpl implements IUserCacheRepository {

    @Value("${tmall.jwt.duration-in-minute}")
    private long durationInMinute;
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public void saveUserState(Long userId, UserStatePO userStatePO) {
        log.debug("开始处理【向缓存中写入用户登录信息】的数据访问，用户ID：{}，用户登录信息：{}", userId, userStatePO);
        String key = KEY_PREFIX_USER_STATE + userId;
        HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
        Map<String, Object> userLoginInfoMap = BeanUtil.beanToMap(userStatePO);
        opsForHash.putAll(key, userLoginInfoMap);
        redisTemplate.expire(key, durationInMinute, TimeUnit.MINUTES);
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
