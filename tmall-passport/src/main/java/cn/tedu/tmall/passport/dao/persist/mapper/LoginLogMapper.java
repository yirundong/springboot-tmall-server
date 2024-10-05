package cn.tedu.tmall.passport.dao.persist.mapper;

import cn.tedu.tmall.passport.pojo.entity.LoginLog;
import cn.tedu.tmall.passport.pojo.vo.LoginLogListItemVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理用户登录日志的Mapper接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Repository
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    /**
     * 查询用户登录日志列表
     *
     * @return 用户登录日志列表
     */
    List<LoginLogListItemVO> list();

}
