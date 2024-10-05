package cn.tedu.tmall.admin.content.controller;

import cn.tedu.tmall.admin.content.pojo.vo.CheckLogListItemVO;
import cn.tedu.tmall.admin.content.service.ICheckLogService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理审核日志相关请求的控制器类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@RestController
@RequestMapping("/check-logs")
@Validated
@Api(tags = "4. 审核日志管理")
public class CheckLogController {

    @Autowired
    private ICheckLogService checkLogService;

    public CheckLogController() {
        log.debug("创建控制器类对象：CheckLogController");
    }

    @GetMapping("/article")
    @PreAuthorize("hasAuthority('/content/article/check')")
    @ApiOperation("查询文章审核日志列表")
    @ApiOperationSupport(order = 420)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", paramType = "query", dataType = "int")
    })
    public JsonResult listGoodsCheckLog(@Range(min = 1, message = "请提交有效的页码值！") Integer page) {
        log.debug("开始处理【查询文章审核日志列表】的请求，页码：{}", page);
        Integer pageNum = page == null ? 1 : page;
        PageData<CheckLogListItemVO> pageData = checkLogService.listArticleCheckLog(pageNum);
        return JsonResult.ok(pageData);
    }

    @GetMapping("/comment")
    @PreAuthorize("hasAuthority('/content/comment/check')")
    @ApiOperation("查询评论审核日志列表")
    @ApiOperationSupport(order = 421)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", paramType = "query", dataType = "int")
    })
    public JsonResult list(@Range(min = 1, message = "请提交有效的页码值！") Integer page) {
        log.debug("开始处理【查询评论审核日志列表】的请求，页码：{}", page);
        Integer pageNum = page == null ? 1 : page;
        PageData<CheckLogListItemVO> pageData = checkLogService.listCommentCheckLog(pageNum);
        return JsonResult.ok(pageData);
    }

}
