package cn.tedu.tmall.passport.service;

import cn.tedu.tmall.common.consts.web.JwtConsts;
import cn.tedu.tmall.common.pojo.authentication.CurrentPrincipal;
import cn.tedu.tmall.passport.pojo.param.UserLoginInfoParam;
import cn.tedu.tmall.passport.pojo.vo.UserLoginResultVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理用户数据的业务接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Transactional
public interface IUserService extends JwtConsts {

    /**
     * 用户登录
     *
     * @param userLoginInfoParam 封装了登录信息的对象
     * @param remoteAddr         客户端的IP地址
     * @param userAgent          客户端的浏览器信息
     * @return 成功登录的用户的信息，包括：ID、用户名、头像、JWT等数据
     */
    UserLoginResultVO login(UserLoginInfoParam userLoginInfoParam, String remoteAddr, String userAgent);

    /**
     * 退出登录
     *
     * @param currentPrincipal 当事人
     */
    void logout(CurrentPrincipal currentPrincipal);

}
