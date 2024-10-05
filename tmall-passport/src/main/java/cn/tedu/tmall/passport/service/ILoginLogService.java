package cn.tedu.tmall.passport.service;

import cn.tedu.tmall.common.pojo.vo.PageData;
import cn.tedu.tmall.passport.pojo.vo.LoginLogListItemVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理用户登录日志的业务接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Transactional
public interface ILoginLogService {

    /**
     * 查询登录日志列表，将使用默认的每页记录数
     *
     * @param pageNum 页码
     * @return 登录日志列表
     */
    PageData<LoginLogListItemVO> list(Integer pageNum);

    /**
     * 查询登录日志列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 登录日志列表
     */
    PageData<LoginLogListItemVO> list(Integer pageNum, Integer pageSize);

}
