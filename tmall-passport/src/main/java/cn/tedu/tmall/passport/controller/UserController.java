package cn.tedu.tmall.passport.controller;

import cn.tedu.tmall.common.consts.web.HttpConsts;
import cn.tedu.tmall.common.pojo.authentication.CurrentPrincipal;
import cn.tedu.tmall.common.web.JsonResult;
import cn.tedu.tmall.passport.pojo.param.UserLoginInfoParam;
import cn.tedu.tmall.passport.pojo.vo.UserLoginResultVO;
import cn.tedu.tmall.passport.service.IUserService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理用户相关请求的控制器类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@RestController
@RequestMapping("/passport")
@Validated
@Api(tags = "1. 单点登录")
public class UserController implements HttpConsts {

    @Autowired
    private IUserService userService;

    public UserController() {
        log.info("创建控制器对象：UserController");
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    @ApiOperationSupport(order = 20)
    public JsonResult login(@Validated UserLoginInfoParam userLoginInfoParam,
                            @ApiIgnore HttpServletRequest request) {
        log.debug("开始处理【用户登录】的请求，参数：{}", userLoginInfoParam);
        String remoteAddr = request.getRemoteAddr();
        String userAgent = request.getHeader(HEADER_USER_AGENT);
        UserLoginResultVO userLoginResultVO = userService.login(userLoginInfoParam, remoteAddr, userAgent);
        return JsonResult.ok(userLoginResultVO);
    }

    @PostMapping("/logout")
    @ApiOperation("退出登录")
    @ApiOperationSupport(order = 90)
    public JsonResult logout(@AuthenticationPrincipal @ApiIgnore CurrentPrincipal currentPrincipal) {
        log.debug("开始处理【退出登录】的请求，无参数");
        userService.logout(currentPrincipal);
        return JsonResult.ok();
    }

}
