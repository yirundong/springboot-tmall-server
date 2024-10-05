package cn.tedu.tmall.admin.mall.controller;

import cn.tedu.tmall.admin.mall.pojo.param.CategoryAddNewParam;
import cn.tedu.tmall.admin.mall.pojo.param.CategoryUpdateInfoParam;
import cn.tedu.tmall.admin.mall.pojo.vo.CategoryListItemVO;
import cn.tedu.tmall.admin.mall.pojo.vo.CategoryStandardVO;
import cn.tedu.tmall.admin.mall.pojo.vo.CategoryTreeItemVO;
import cn.tedu.tmall.admin.mall.service.ICategoryService;
import cn.tedu.tmall.common.pojo.vo.PageData;
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
import java.util.List;

/**
 * 处理类别相关请求的控制器类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@RestController
@RequestMapping("/categories")
@Validated
@Api(tags = "1. 类别管理")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    public CategoryController() {
        log.debug("创建控制器类对象：CategoryController");
    }

    @PostMapping("/add-new")
    @PreAuthorize("hasAuthority('/mall/category/simple')")
    @ApiOperation("添加类别")
    @ApiOperationSupport(order = 100)
    public JsonResult addNew(@Valid CategoryAddNewParam categoryAddNewParam) {
        log.debug("开始处理【添加类别】的请求，参数：{}", categoryAddNewParam);
        categoryService.addNew(categoryAddNewParam);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/delete")
    @PreAuthorize("hasAuthority('/mall/category/delete')")
    @ApiOperation("根据ID删除类别")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "类别ID", required = true, dataType = "long")
    })
    public JsonResult delete(@PathVariable @Range(min = 1, message = "请提交有效的类别ID值！") Long id) {
        log.debug("开始处理【根据ID删除类别】的请求，参数：{}", id);
        categoryService.delete(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/enable")
    @PreAuthorize("hasAuthority('/mall/category/simple')")
    @ApiOperation("启用类别")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "类别ID", required = true, dataType = "long")
    })
    public JsonResult setEnable(@PathVariable @Range(min = 1, message = "请提交有效的类别ID值！") Long id) {
        log.debug("开始处理【启用类别】的请求，参数：{}", id);
        categoryService.setEnable(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/disable")
    @PreAuthorize("hasAuthority('/mall/category/simple')")
    @ApiOperation("禁用类别")
    @ApiOperationSupport(order = 311)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "类别ID", required = true, dataType = "long")
    })
    public JsonResult setDisable(@PathVariable @Range(min = 1, message = "请提交有效的类别ID值！") Long id) {
        log.debug("开始处理【禁用类别】的请求，参数：{}", id);
        categoryService.setDisable(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/display")
    @PreAuthorize("hasAuthority('/mall/category/simple')")
    @ApiOperation("显示类别")
    @ApiOperationSupport(order = 312)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "类别ID", required = true, dataType = "long")
    })
    public JsonResult setDisplay(@PathVariable @Range(min = 1, message = "请提交有效的类别ID值！") Long id) {
        log.debug("开始处理【显示类别】的请求，参数：{}", id);
        categoryService.setDisplay(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/hidden")
    @PreAuthorize("hasAuthority('/mall/category/simple')")
    @ApiOperation("隐藏类别")


    @ApiOperationSupport(order = 313)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "类别ID", required = true, dataType = "long")
    })
    public JsonResult setHidden(@PathVariable @Range(min = 1, message = "请提交有效的类别ID值！") Long id) {
        log.debug("开始处理【隐藏类别】的请求，参数：{}", id);
        categoryService.setHidden(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/update")
    @PreAuthorize("hasAuthority('/mall/category/simple')")
    @ApiOperation("修改类别详情")
    @ApiOperationSupport(order = 300)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "类别ID", required = true, dataType = "long")
    })
    public JsonResult updateInfoById(@PathVariable @Range(min = 1, message = "请提交有效的类别ID值！") Long id,
                                     @Valid CategoryUpdateInfoParam categoryUpdateInfoParam) {
        log.debug("开始处理【修改类别详情】的请求，ID：{}，新数据：{}", id, categoryUpdateInfoParam);
        categoryService.updateInfoById(id, categoryUpdateInfoParam);
        return JsonResult.ok();
    }

    @GetMapping("/{id:[0-9]+}")
    @PreAuthorize("hasAuthority('/mall/category/simple')")
    @ApiOperation("根据ID查询类别")
    @ApiOperationSupport(order = 410)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "类别ID", required = true, dataType = "long")
    })
    public JsonResult getStandardById(
            @PathVariable @Range(min = 1, message = "请提交有效的类别ID值！") Long id) {
        log.debug("开始处理【根据ID查询类别】的请求，参数：{}", id);
        CategoryStandardVO queryResult = categoryService.getStandardById(id);
        return JsonResult.ok(queryResult);
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('/mall/category/simple')")
    @ApiOperation("查询类别树")
    @ApiOperationSupport(order = 420)
    public JsonResult listTree() {
        log.debug("开始处理【获取类别树】的业务，参数：无");
        List<CategoryTreeItemVO> categoryTree = categoryService.listTree();
        return JsonResult.ok(categoryTree);
    }

    @GetMapping("/list-by-parent")
    @PreAuthorize("hasAuthority('/mall/category/simple')")
    @ApiOperation("根据父级查询子级列表")
    @ApiOperationSupport(order = 421)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父级类别ID", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "queryType", value = "查询类型", example = "all")
    })
    public JsonResult list(@Range(message = "请提交有效的父级类别ID值！") Long parentId,
                           @Range(min = 1, message = "请提交有效的页码值！") Integer page,
                           String queryType) {
        log.debug("开始处理【根据父级查询子级列表】的请求，父级类别：{}，页码：{}", parentId, page);
        Integer pageNum = page == null ? 1 : page;
        PageData<CategoryListItemVO> pageData;
        if ("all".equals(queryType)) {
            pageData = categoryService.listByParent(parentId, pageNum, Integer.MAX_VALUE);
        } else {
            pageData = categoryService.listByParent(parentId, pageNum);
        }
        return JsonResult.ok(pageData);
    }

}
