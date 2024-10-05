package cn.tedu.tmall.common.consts.cache;

/**
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 15:12
 *
 * 单点登录相关redis内容
 */
public interface PassportCacheConsts {

    /**
     * 用户状态信息的Key前缀
     */
    String KEY_PREFIX_USER_STATE = "passport:user-state:";

    /**
     * 用户状态的Hash对象中“启用状态”的Key
     */
    String HASH_KEY_USER_ENABLE = "enable";

}
