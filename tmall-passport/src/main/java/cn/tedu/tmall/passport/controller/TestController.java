package cn.tedu.tmall.passport.controller;

import cn.tedu.tmall.common.pojo.authentication.CurrentPrincipal;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Deprecated
@RestController
@RequestMapping("/test")
@Api(tags = "9. 测试访问")
public class TestController {

    @GetMapping("/simple-get")
    @ApiOperation("【无需登录】简单的GET请求")
    @ApiOperationSupport(order = 100)
    public String simpleGet() {
        return "服务器端响应了简单的GET请求！";
    }

    @PostMapping("/simple-post")
    @ApiOperation("【无需登录】简单的POST请求")
    @ApiOperationSupport(order = 101)
    public String simplePost() {
        return "服务器端响应了简单的POST请求！";
    }

    @GetMapping("/authenticated-get")
    @ApiOperation("【需要登录】简单的GET请求")
    @ApiOperationSupport(order = 200)
    public String authenticatedGet(@AuthenticationPrincipal @ApiIgnore CurrentPrincipal principal) {
        return "【需要登录】服务器端响应了简单的GET请求！当事人：" + principal;
    }

    @GetMapping("/authorized-get")
    @PreAuthorize("hasAuthority('/admin')")
    @ApiOperation("【需要权限】需要权限的GET请求")
    @ApiOperationSupport(order = 300)
    public String authorizedGet() {
        return "【需要权限】服务器端响应了需要权限的GET请求！";
    }

}
