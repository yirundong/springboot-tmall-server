package cn.tedu.tmall.passport.dao.persist.mapper;

import cn.tedu.tmall.passport.pojo.entity.User;
import cn.tedu.tmall.passport.pojo.vo.UserLoginInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 处理用户数据的Mapper接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户的登录信息
     *
     * @param username 用户名
     * @return 匹配的用户的登录信息，如果没有匹配的数据，则返回null
     */
    UserLoginInfoVO getLoginInfoByUsername(String username);

}
