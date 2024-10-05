package cn.tedu.tmall.admin.content.controller;

import cn.tedu.tmall.admin.content.pojo.vo.CommentListItemVO;
import cn.tedu.tmall.admin.content.service.ICommentService;
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

import javax.validation.constraints.NotEmpty;

/**
 * 处理评论相关请求的控制器类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@RestController
@RequestMapping("/comments")
@Validated
@Api(tags = "3. 评论管理")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    public CommentController() {
        log.debug("创建控制器类对象：CommentController");
    }

    @PostMapping("/{id:[0-9]+}/pass-check")
    @PreAuthorize("hasAuthority('/content/comment/simple')")
    @ApiOperation("审核通过评论")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评论ID", required = true, dataType = "long"),
            @ApiImplicitParam(name = "remarks", value = "审核备注", required = true, paramType = "query")
    })
    public JsonResult passCheck(@AuthenticationPrincipal @ApiIgnore CurrentPrincipal currentPrincipal,
                                @PathVariable @Range(min = 1, message = "请提交有效的评论ID值！") Long id,
                                @NotEmpty(message = "审核备注不允许为空") String remarks) {
        log.debug("开始处理【审核通过评论】的请求，当事人：{}，评论ID：{}，审核备注：{}", currentPrincipal, id, remarks);
        commentService.passCheck(currentPrincipal, id, remarks);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/cancel-check")
    @PreAuthorize("hasAuthority('/content/comment/simple')")
    @ApiOperation("拒绝审核评论")
    @ApiOperationSupport(order = 311)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评论ID", required = true, dataType = "long"),
            @ApiImplicitParam(name = "remarks", value = "审核备注", required = true, paramType = "query")
    })
    public JsonResult cancelCheck(@AuthenticationPrincipal @ApiIgnore CurrentPrincipal currentPrincipal,
                                  @PathVariable @Range(min = 1, message = "请提交有效的评论ID值！") Long id,
                                  @NotEmpty(message = "审核备注不允许为空") String remarks) {
        log.debug("开始处理【拒绝审核评论】的请求，当事人：{}，评论ID：{}，审核备注：{}", currentPrincipal, id, remarks);
        commentService.cancelCheck(currentPrincipal, id, remarks);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/display")
    @PreAuthorize("hasAuthority('/content/comment/simple')")
    @ApiOperation("显示评论")
    @ApiOperationSupport(order = 312)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "评论ID", required = true, dataType = "long")
    })
    public JsonResult setDisplay(@PathVariable @Range(min = 1, message = "请提交有效的评论ID值！") Long id) {
        log.debug("开始处理【显示评论】的请求，参数：{}", id);
        commentService.setDisplay(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/hidden")
    @PreAuthorize("hasAuthority('/content/comment/simple')")
    @ApiOperation("隐藏评论")
    @ApiOperationSupport(order = 313)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "评论ID", required = true, dataType = "long")
    })
    public JsonResult setHidden(@PathVariable @Range(min = 1, message = "请提交有效的评论ID值！") Long id) {
        log.debug("开始处理【隐藏评论】的请求，参数：{}", id);
        commentService.setHidden(id);
        return JsonResult.ok();
    }

    @GetMapping("/list-by-article")
    @PreAuthorize("hasAuthority('/content/comment/simple')")
    @ApiOperation("查询文章的评论列表")
    @ApiOperationSupport(order = 420)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "queryType", value = "查询类型", example = "all")
    })
    public JsonResult listByArticle(@Range(min = 1, message = "请提交有效的页码值！") Integer page, String queryType) {
        log.debug("开始处理【查询文章的评论列表】的请求，页码：{}", page);
        Integer pageNum = page == null ? 1 : page;
        PageData<CommentListItemVO> pageData;
        if ("all".equals(queryType)) {
            pageData = commentService.listByArticle(pageNum, Integer.MAX_VALUE);
        } else {
            pageData = commentService.listByArticle(pageNum);
        }
        return JsonResult.ok(pageData);
    }

    @GetMapping("/list-by-comment")
    @PreAuthorize("hasAuthority('/content/comment/simple')")
    @ApiOperation("查询评论的评论列表")
    @ApiOperationSupport(order = 421)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "queryType", value = "查询类型", example = "all")
    })
    public JsonResult listByComment(@Range(min = 1, message = "请提交有效的页码值！") Integer page, String queryType) {
        log.debug("开始处理【查询评论的评论列表】的请求，页码：{}", page);
        Integer pageNum = page == null ? 1 : page;
        PageData<CommentListItemVO> pageData;
        if ("all".equals(queryType)) {
            pageData = commentService.listByComment(pageNum, Integer.MAX_VALUE);
        } else {
            pageData = commentService.listByComment(pageNum);
        }
        return JsonResult.ok(pageData);
    }

}
