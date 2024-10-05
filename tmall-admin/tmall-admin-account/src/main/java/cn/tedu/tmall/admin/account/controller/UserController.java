package cn.tedu.tmall.admin.account.controller;

import cn.tedu.tmall.admin.account.pojo.param.UserAddNewParam;
import cn.tedu.tmall.admin.account.pojo.param.UserUpdateInfoParam;
import cn.tedu.tmall.admin.account.pojo.vo.UserListItemVO;
import cn.tedu.tmall.admin.account.pojo.vo.UserStandardVO;
import cn.tedu.tmall.admin.account.service.IUserService;
import cn.tedu.tmall.common.consts.web.HttpConsts;
import cn.tedu.tmall.common.pojo.vo.PageData;
import cn.tedu.tmall.common.validation.account.UserRules;
import cn.tedu.tmall.common.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 处理用户相关请求的控制器类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@RestController
@RequestMapping("/users")
@Validated
@Api(tags = "1. 用户管理")
public class UserController implements HttpConsts {

    @Autowired
    private IUserService userService;

    public UserController() {
        log.info("创建控制器对象：UserController");
    }

    @PostMapping("/add-new")
    @PreAuthorize("hasAuthority('/account/user/add-new')")
    @ApiOperation("添加用户")
    @ApiOperationSupport(order = 100)
    public JsonResult addNew(@Valid UserAddNewParam userAddNewParam) {
        log.debug("开始处理【添加用户】的请求，参数：{}", userAddNewParam);
        userService.addNew(userAddNewParam);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/delete")
    @PreAuthorize("hasAuthority('/account/user/delete')")
    @ApiOperation("根据ID删除用户")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "long")
    })
    public JsonResult delete(@PathVariable Long id) {
        log.debug("开始处理【根据ID删除用户】的请求，参数：{}", id);
        userService.delete(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/info/update")
    @PreAuthorize("hasAuthority('/account/user/edit')")
    @ApiOperation("修改基本信息")
    @ApiOperationSupport(order = 300)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "long")
    })
    public JsonResult updateInfo(@PathVariable Long id, @Valid UserUpdateInfoParam userUpdateInfoParam) {
        log.debug("开始处理【修改基本信息】的请求，用户：{}，新基本信息：{}", id, userUpdateInfoParam);
        userService.updateInfo(id, userUpdateInfoParam);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/password/update")
    @PreAuthorize("hasAuthority('/account/user/edit')")
    @ApiOperation("修改密码")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "long"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, paramType = "query")
    })
    public JsonResult updatePassword(@PathVariable Long id,
                                     @Pattern(regexp = UserRules.PATTERN_PASSWORD, message = UserRules.MESSAGE_PASSWORD_PATTERN) String newPassword) {
        log.debug("开始处理【修改密码】的请求，用户：{}，原密码：{}，新密码：{}", id, newPassword);
        userService.updatePassword(id, newPassword);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/avatar/update")
    @PreAuthorize("hasAuthority('/account/user/edit')")
    @ApiOperation("修改头像")
    @ApiOperationSupport(order = 320)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "long"),
            @ApiImplicitParam(name = "avatar", value = "新头像的路径", required = true, paramType = "query")
    })
    public JsonResult updateAvatar(@PathVariable Long id,
                                   @NotBlank(message = "请提交新头像的路径") String avatar) {
        log.debug("开始处理【修改头像】的请求，用户：{}，新头像：{}", id, avatar);
        userService.updateAvatar(id, avatar);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/phone/update")
    @PreAuthorize("hasAuthority('/account/user/edit')")
    @ApiOperation("修改手机号码")
    @ApiOperationSupport(order = 330)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "long"),
            @ApiImplicitParam(name = "phone", value = "手机号码", required = true, paramType = "query")
    })
    public JsonResult updatePhone(@PathVariable Long id,
                                  @Pattern(regexp = UserRules.PATTERN_PHONE, message = UserRules.MESSAGE_PHONE_PATTERN) String phone) {
        log.debug("开始处理【修改手机号码】的请求，用户：{}，新手机号码：{}", id, phone);
        userService.updatePhone(id, phone);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/email/update")
    @PreAuthorize("hasAuthority('/account/user/edit')")
    @ApiOperation("修改电子邮箱")
    @ApiOperationSupport(order = 340)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "long"),
            @ApiImplicitParam(name = "email", value = "电子邮箱", required = true, paramType = "query")
    })
    public JsonResult updateEmail(@PathVariable Long id,
                                  @Pattern(regexp = UserRules.PATTERN_EMAIL, message = UserRules.MESSAGE_EMAIL_PATTERN) String email) {
        log.debug("开始处理【修改电子邮箱】的请求，用户：{}，新手机号码：{}", id, email);
        userService.updateEmail(id, email);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/enable")
    @PreAuthorize("hasAuthority('/account/user/enable')")
    @ApiOperation("启用用户")
    @ApiOperationSupport(order = 350)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "long")
    })
    public JsonResult setEnable(@PathVariable Long id) {
        log.debug("开始处理【启用用户】的请求，参数：{}", id);
        userService.setEnable(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/disable")
    @PreAuthorize("hasAuthority('/account/user/enable')")
    @ApiOperation("禁用用户")
    @ApiOperationSupport(order = 351)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "long")
    })
    public JsonResult setDisable(@PathVariable Long id) {
        log.debug("开始处理【禁用用户】的请求，参数：{}", id);
        userService.setDisable(id);
        return JsonResult.ok();
    }

    @ApiOperation("根据ID查询用户")
    @ApiOperationSupport(order = 410)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "long")
    })
    @GetMapping("/{id:[0-9]+}")
    @PreAuthorize("hasAuthority('/account/user/query')")
    public JsonResult getStandardById(@PathVariable @Range(min = 1, message = "获取用户详情失败，请提交合法的ID值！") Long id) {
        log.debug("开始处理【根据ID查询用户】的请求，参数：{}", id);
        UserStandardVO tag = userService.getStandardById(id);
        return JsonResult.ok(tag);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('/account/user/query')")
    @ApiOperation("查询用户列表")
    @ApiOperationSupport(order = 420)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "long")
    })
    public JsonResult list(Integer page) {
        log.debug("开始处理【查询用户列表】的请求，页码：{}", page);
        Integer pageNum = page == null ? 1 : page;
        PageData<UserListItemVO> pageData = userService.list(pageNum);
        return JsonResult.ok(pageData);
    }

}
