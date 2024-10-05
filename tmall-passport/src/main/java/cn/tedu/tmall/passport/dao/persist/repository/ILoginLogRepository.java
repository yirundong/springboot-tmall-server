package cn.tedu.tmall.passport.dao.persist.repository;

import cn.tedu.tmall.common.pojo.vo.PageData;
import cn.tedu.tmall.passport.pojo.entity.LoginLog;
import cn.tedu.tmall.passport.pojo.vo.LoginLogListItemVO;

/**
 * 处理登录日志数据的存储库接口
 *
 * @author YiRunDong
 * @version 2.0
 */
public interface ILoginLogRepository {

    /**
     * 插入登录日志
     *
     * @param loginLog 登录日志
     * @return 受影响的行数
     */
    int insert(LoginLog loginLog);

    /**
     * 查询用户登录日志列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 用户登录日志列表
     */
    PageData<LoginLogListItemVO> list(Integer pageNum, Integer pageSize);

}
