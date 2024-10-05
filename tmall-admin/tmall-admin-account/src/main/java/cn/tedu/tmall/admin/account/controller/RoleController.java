package cn.tedu.tmall.admin.account.controller;

import cn.tedu.tmall.admin.account.pojo.vo.RoleListItemVO;
import cn.tedu.tmall.admin.account.service.IRoleService;
import cn.tedu.tmall.common.pojo.vo.PageData;
import cn.tedu.tmall.common.web.JsonResult;
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
 * 处理角色相关请求的控制器
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@RestController
@RequestMapping("/roles")
@Validated
@Api(tags = "2. 角色管理")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    public RoleController() {
        log.debug("创建控制器类对象：RoleController");
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('/account/user/query')")
    @ApiOperation("查询角色列表")
    @ApiOperationSupport(order = 420)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataType = "long"),
            @ApiImplicitParam(name = "queryType", value = "查询类型，当需要查询全部数据时，此参数值应该是all")
    })
    public JsonResult list(Integer page, String queryType) {
        log.debug("开始处理【查询角色列表】的请求，页码：{}", page);
        if (page == null) {
            page = 1;
        }
        Integer pageNum = page > 0 ? page : 1;
        PageData<RoleListItemVO> pageData;
        if ("all".equals(queryType)) {
            pageData = roleService.list(1, Integer.MAX_VALUE);
        } else {
            pageData = roleService.list(pageNum);
        }
        return JsonResult.ok(pageData);
    }

}
