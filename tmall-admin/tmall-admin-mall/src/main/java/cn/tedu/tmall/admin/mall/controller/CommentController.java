package cn.tedu.tmall.admin.mall.controller;

import cn.tedu.tmall.admin.mall.pojo.vo.CommentListItemVO;
import cn.tedu.tmall.admin.mall.service.ICommentService;
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

import javax.validation.constraints.NotBlank;

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

    @PostMapping("/{id:[0-9]+}/check-state/pass")
    @PreAuthorize("hasAuthority('/mall/comment/check')")
    @ApiOperation("审核通过评论")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评论ID", required = true, dataType = "long"),
            @ApiImplicitParam(name = "remarks", value = "备注信息", required = true, paramType = "query", dataType = "string")
    })
    public JsonResult passCheck(@AuthenticationPrincipal @ApiIgnore CurrentPrincipal currentPrincipal,
                                @PathVariable @Range(min = 1, message = "请提交有效的评论ID值！") Long id,
                                @NotBlank(message = "请提交审核备注信息") String remarks) {
        log.debug("开始处理【审核通过评论】的请求，参数：{}", id);
        commentService.passCheck(currentPrincipal, id, remarks);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/check-state/reject")
    @PreAuthorize("hasAuthority('/mall/comment/check')")
    @ApiOperation("拒绝审核评论")
    @ApiOperationSupport(order = 311)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评论ID", required = true, dataType = "long"),
            @ApiImplicitParam(name = "remarks", value = "备注信息", required = true, paramType = "query", dataType = "string")
    })
    public JsonResult rejectCheck(@AuthenticationPrincipal @ApiIgnore CurrentPrincipal currentPrincipal,
                                  @PathVariable @Range(min = 1, message = "请提交有效的评论ID值！") Long id,
                                  @NotBlank(message = "请提交审核备注信息") String remarks) {
        log.debug("开始处理【拒绝审核评论】的请求，参数：{}", id);
        commentService.rejectCheck(currentPrincipal, id, remarks);
        return JsonResult.ok();
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('/mall/comment/simple')")
    @ApiOperation("查询商品的评论列表")
    @ApiOperationSupport(order = 420)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "queryType", value = "查询类型", example = "all")
    })
    public JsonResult list(@Range(min = 1, message = "请提交有效的页码值！") Integer page, String queryType) {
        log.debug("开始处理【查询商品的评论列表】的请求，页码：{}", page);
        Integer pageNum = page == null ? 1 : page;
        PageData<CommentListItemVO> pageData;
        if ("all".equals(queryType)) {
            pageData = commentService.list(pageNum, Integer.MAX_VALUE);
        } else {
            pageData = commentService.list(pageNum);
        }
        return JsonResult.ok(pageData);
    }

}
