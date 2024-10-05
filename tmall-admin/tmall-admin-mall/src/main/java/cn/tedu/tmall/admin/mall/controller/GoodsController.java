package cn.tedu.tmall.admin.mall.controller;

import cn.tedu.tmall.admin.mall.pojo.param.GoodsAddNewParam;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsListItemVO;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsStandardVO;
import cn.tedu.tmall.admin.mall.service.IGoodsService;
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
import javax.validation.constraints.NotBlank;

/**
 * 处理商品相关请求的控制器类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@RestController
@RequestMapping("/goods")
@Validated
@Api(tags = "2. 商品管理")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    public GoodsController() {
        log.debug("创建控制器类对象：GoodsController");
    }

    @PostMapping("/add-new")
    @PreAuthorize("hasAuthority('/mall/goods/simple')")
    @ApiOperation("发布商品")
    @ApiOperationSupport(order = 100)
    public JsonResult addNew(@AuthenticationPrincipal @ApiIgnore CurrentPrincipal currentPrincipal,
                             @ApiIgnore HttpServletRequest request,
                             @Valid GoodsAddNewParam goodsAddNewParam) {
        log.debug("开始处理【发布商品】的请求，参数：{}", goodsAddNewParam);
        String remoteAddr = request.getRemoteAddr();
        goodsService.addNew(currentPrincipal, remoteAddr, goodsAddNewParam);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/delete")
    @PreAuthorize("hasAuthority('/mall/goods/delete')")
    @ApiOperation("根据ID删除商品")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "long")
    })
    public JsonResult delete(@PathVariable @Range(min = 1, message = "请提交有效的商品ID值！") Long id) {
        log.debug("开始处理【根据ID删除商品】的请求，参数：{}", id);
        goodsService.delete(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/check-state/pass")
    @PreAuthorize("hasAuthority('/mall/goods/check')")
    @ApiOperation("审核通过商品")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "long"),
            @ApiImplicitParam(name = "remarks", value = "备注信息", required = true, paramType = "query", dataType = "string")
    })
    public JsonResult passCheck(@AuthenticationPrincipal @ApiIgnore CurrentPrincipal currentPrincipal,
                                @PathVariable @Range(min = 1, message = "请提交有效的商品ID值！") Long id,
                                @NotBlank(message = "请提交审核备注信息") String remarks) {
        log.debug("开始处理【审核通过商品】的请求，参数：{}", id);
        goodsService.passCheck(currentPrincipal, id, remarks);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/check-state/reject")
    @PreAuthorize("hasAuthority('/mall/goods/check')")
    @ApiOperation("拒绝审核商品")
    @ApiOperationSupport(order = 311)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "long"),
            @ApiImplicitParam(name = "remarks", value = "备注信息", required = true, paramType = "query", dataType = "string")
    })
    public JsonResult rejectCheck(@AuthenticationPrincipal @ApiIgnore CurrentPrincipal currentPrincipal,
                                  @PathVariable @Range(min = 1, message = "请提交有效的商品ID值！") Long id,
                                  @NotBlank(message = "请提交审核备注信息") String remarks) {
        log.debug("开始处理【拒绝审核商品】的请求，参数：{}", id);
        goodsService.rejectCheck(currentPrincipal, id, remarks);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/put-on")
    @PreAuthorize("hasAuthority('/mall/goods/simple')")
    @ApiOperation("上架商品")
    @ApiOperationSupport(order = 320)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "long")
    })
    public JsonResult putOn(@PathVariable @Range(min = 1, message = "请提交有效的商品ID值！") Long id) {
        log.debug("开始处理【上架商品】的请求，参数：{}", id);
        goodsService.putOn(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/pull-off")
    @PreAuthorize("hasAuthority('/mall/goods/simple')")
    @ApiOperation("下架商品")
    @ApiOperationSupport(order = 321)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "long")
    })
    public JsonResult pullOff(@PathVariable @Range(min = 1, message = "请提交有效的商品ID值！") Long id) {
        log.debug("开始处理【下架商品】的请求，参数：{}", id);
        goodsService.pullOff(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/set-recommend")
    @PreAuthorize("hasAuthority('/mall/goods/simple')")
    @ApiOperation("推荐商品")
    @ApiOperationSupport(order = 330)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "long")
    })
    public JsonResult setRecommend(@PathVariable @Range(min = 1, message = "请提交有效的商品ID值！") Long id) {
        log.debug("开始处理【推荐商品】的请求，参数：{}", id);
        goodsService.setRecommend(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/cancle-recommend")
    @PreAuthorize("hasAuthority('/mall/goods/simple')")
    @ApiOperation("取消推荐商品")
    @ApiOperationSupport(order = 331)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "long")
    })
    public JsonResult cancelRecommend(@PathVariable @Range(min = 1, message = "请提交有效的商品ID值！") Long id) {
        log.debug("开始处理【取消推荐商品】的请求，参数：{}", id);
        goodsService.cancelRecommend(id);
        return JsonResult.ok();
    }

    @GetMapping("/{id:[0-9]+}")
    @PreAuthorize("hasAuthority('/mall/goods/simple')")
    @ApiOperation("根据ID查询商品")
    @ApiOperationSupport(order = 410)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "long")
    })
    public JsonResult getStandardById(@PathVariable @Range(min = 1, message = "请提交有效的商品ID值！") Long id) {
        log.debug("开始处理【根据ID查询商品】的请求，参数：{}", id);
        GoodsStandardVO queryResult = goodsService.getStandardById(id);
        return JsonResult.ok(queryResult);
    }


    @GetMapping("")
    @PreAuthorize("hasAuthority('/mall/goods/simple')")
    @ApiOperation("查询商品列表")
    @ApiOperationSupport(order = 420)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", paramType = "query", dataType = "long"),
    })
    public JsonResult list(@Range(min = 1, message = "请提交有效的页码值！") Integer page) {
        log.debug("开始处理【查询商品列表】的请求，页码：{}", page);
        Integer pageNum = page == null ? 1 : page;
        PageData<GoodsListItemVO> pageData = goodsService.list(pageNum);
        return JsonResult.ok(pageData);
    }

    @GetMapping("/list-by-category")
    @PreAuthorize("hasAuthority('/mall/goods/simple')")
    @ApiOperation("根据类别查询商品列表")
    @ApiOperationSupport(order = 421)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "商品类别ID", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", paramType = "query", dataType = "long")
    })
    public JsonResult listByCategory(@Range(message = "请提交有效的商品类别ID值！") Long categoryId,
                                     @Range(min = 1, message = "请提交有效的页码值！") Integer page) {
        log.debug("开始处理【根据类别查询商品列表】的请求，父级商品：{}，页码：{}", categoryId, page);
        Integer pageNum = page == null ? 1 : page;
        PageData<GoodsListItemVO> pageData = goodsService.listByCategory(categoryId, pageNum);
        return JsonResult.ok(pageData);
    }

    @PostMapping("/rebuild-search")
    @PreAuthorize("hasAuthority('/mall/goods/rebuild-search')")
    @ApiOperation("重建商品的搜索数据")
    @ApiOperationSupport(order = 500)
    public JsonResult rebuildSearch() {
        log.debug("开始处理【重建商品的搜索数据】的请求");
        goodsService.rebuildSearch();
        return JsonResult.ok();
    }

}
