package cn.tedu.tmall.passport.controller;

import cn.tedu.tmall.common.pojo.vo.PageData;
import cn.tedu.tmall.common.web.JsonResult;
import cn.tedu.tmall.passport.pojo.vo.LoginLogListItemVO;
import cn.tedu.tmall.passport.service.ILoginLogService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理用户登录日志相关请求的控制器类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@RestController
@RequestMapping("/login-logs")
@Validated
@Api(tags = "2. 登录日志")
public class LoginLogController {

    @Autowired
    private ILoginLogService loginLogService;

    public LoginLogController() {
        log.debug("创建控制器类对象：LoginLogController");
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('/account/user/query')")
    @ApiOperation("查询登录日志列表")
    @ApiOperationSupport(order = 420)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "long")
    })
    public JsonResult list(Integer page) {
        log.debug("开始处理【查询登录日志列表】的请求，页码：{}", page);
        Integer pageNum = page == null ? 1 : page;
        PageData<LoginLogListItemVO> pageData = loginLogService.list(pageNum);
        return JsonResult.ok(pageData);
    }

}
