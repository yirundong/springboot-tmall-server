package cn.tedu.tmall.admin.content.controller;

import cn.tedu.tmall.admin.content.pojo.param.ArticleAddNewParam;
import cn.tedu.tmall.admin.content.pojo.vo.ArticleListItemVO;
import cn.tedu.tmall.admin.content.pojo.vo.ArticleStandardVO;
import cn.tedu.tmall.admin.content.service.IArticleService;
import cn.tedu.tmall.common.pojo.authentication.CurrentPrincipal;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * 处理文章相关请求的控制器类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@RestController
@RequestMapping("/articles")
@Validated
@Api(tags = "2. 文章管理")
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    public ArticleController() {
        log.debug("创建控制器类对象：ArticleController");
    }

    @PostMapping("/add-new")
    @PreAuthorize("hasAuthority('/content/article/simple')")
    @ApiOperation("发布文章")
    @ApiOperationSupport(order = 100)
    public JsonResult addNew(@AuthenticationPrincipal @ApiIgnore CurrentPrincipal currentPrincipal,
                             @ApiIgnore HttpServletRequest request,
                             @Valid ArticleAddNewParam articleAddNewParam) {
        log.debug("开始处理【发布文章】的请求，参数：{}", articleAddNewParam);
        String remoteAddr = request.getRemoteAddr();
        articleService.addNew(currentPrincipal, remoteAddr, articleAddNewParam);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/delete")
    @PreAuthorize("hasAuthority('/content/article/delete')")
    @ApiOperation("根据ID删除文章")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "long")
    })
    public JsonResult delete(@PathVariable @Range(min = 1, message = "请提交有效的文章ID值！") Long id) {
        log.debug("开始处理【根据ID删除文章】的请求，参数：{}", id);
        articleService.delete(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/pass-check")
    @PreAuthorize("hasAuthority('/content/article/check')")
    @ApiOperation("审核通过文章")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "long"),
            @ApiImplicitParam(name = "remarks", value = "审核备注", required = true, paramType = "query")
    })
    public JsonResult passCheck(@AuthenticationPrincipal @ApiIgnore CurrentPrincipal currentPrincipal,
                                @PathVariable @Range(min = 1, message = "请提交有效的文章ID值！") Long id,
                                @NotEmpty(message = "审核备注不允许为空") String remarks) {
        log.debug("开始处理【审核通过文章】的请求，当事人：{}，文章ID：{}，审核备注：{}", currentPrincipal, id, remarks);
        articleService.passCheck(currentPrincipal, id, remarks);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/cancel-check")
    @PreAuthorize("hasAuthority('/content/article/check')")
    @ApiOperation("拒绝审核文章")
    @ApiOperationSupport(order = 311)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "long"),
            @ApiImplicitParam(name = "remarks", value = "审核备注", required = true, paramType = "query")
    })
    public JsonResult rejectCheck(@AuthenticationPrincipal @ApiIgnore CurrentPrincipal currentPrincipal,
                                  @PathVariable @Range(min = 1, message = "请提交有效的文章ID值！") Long id,
                                  @NotEmpty(message = "审核备注不允许为空") String remarks) {
        log.debug("开始处理【拒绝审核文章】的请求，当事人：{}，文章ID：{}，审核备注：{}", currentPrincipal, id, remarks);
        articleService.rejectCheck(currentPrincipal, id, remarks);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/display")
    @PreAuthorize("hasAuthority('/content/article/simple')")
    @ApiOperation("显示文章")
    @ApiOperationSupport(order = 312)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "long")
    })
    public JsonResult setDisplay(@PathVariable @Range(min = 1, message = "请提交有效的文章ID值！") Long id) {
        log.debug("开始处理【显示文章】的请求，参数：{}", id);
        articleService.setDisplay(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/hidden")
    @PreAuthorize("hasAuthority('/content/article/simple')")
    @ApiOperation("隐藏文章")
    @ApiOperationSupport(order = 313)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "long")
    })
    public JsonResult setHidden(@PathVariable @Range(min = 1, message = "请提交有效的文章ID值！") Long id) {
        log.debug("开始处理【隐藏文章】的请求，参数：{}", id);
        articleService.setHidden(id);
        return JsonResult.ok();
    }

    @GetMapping("/{id:[0-9]+}")
    @PreAuthorize("hasAuthority('/content/article/simple')")
    @ApiOperation("根据ID查询文章")
    @ApiOperationSupport(order = 410)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "long")
    })
    public JsonResult getStandardById(
            @PathVariable @Range(min = 1, message = "请提交有效的文章ID值！") Long id) {
        log.debug("开始处理【根据ID查询文章详情】的请求，参数：{}", id);
        ArticleStandardVO queryResult = articleService.getStandardById(id);
        return JsonResult.ok(queryResult);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('/content/article/simple')")
    @ApiOperation("查询文章列表")
    @ApiOperationSupport(order = 420)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", paramType = "query", dataType = "int")
    })
    public JsonResult list(@Range(min = 1, message = "请提交有效的页码值！") Integer page) {
        log.debug("开始处理【查询文章列表】的请求，页码：{}", page);
        Integer pageNum = page == null ? 1 : page;
        PageData<ArticleListItemVO> pageData = articleService.list(pageNum);
        return JsonResult.ok(pageData);
    }

    @GetMapping("/list-by-category")
    @PreAuthorize("hasAuthority('/content/article/simple')")
    @ApiOperation("根据类别查询文章列表")
    @ApiOperationSupport(order = 421)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "文章类别ID", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", paramType = "query", dataType = "int")
    })
    public JsonResult listByCategory(@Range(message = "请提交有效的文章类别ID值！") Long categoryId,
                                     @Range(min = 1, message = "请提交有效的页码值！") Integer page) {
        log.debug("开始处理【根据类别查询文章列表】的请求，父级文章：{}，页码：{}", categoryId, page);
        Integer pageNum = page == null ? 1 : page;
        PageData<ArticleListItemVO> pageData = articleService.listByCategory(categoryId, pageNum);
        return JsonResult.ok(pageData);
    }

    @PostMapping("/rebuild-search")
    @PreAuthorize("hasAuthority('/content/article/rebuild-search')")
    @ApiOperation("更新搜索服务器中的文章列表")
    @ApiOperationSupport(order = 500)
    public JsonResult rebuildSearch() {
        log.debug("开始处理【更新搜索服务器中的文章列表】的请求，无参数");
        articleService.rebuildSearch();
        return JsonResult.ok();
    }

}
