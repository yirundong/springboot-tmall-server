package cn.tedu.tmall.admin.mall.service;

import cn.tedu.tmall.admin.mall.pojo.param.GoodsAddNewParam;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsListItemVO;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsStandardVO;
import cn.tedu.tmall.common.consts.data.MallConsts;
import cn.tedu.tmall.common.pojo.authentication.CurrentPrincipal;
import cn.tedu.tmall.common.pojo.vo.PageData;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理商品数据的业务接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Transactional
public interface IGoodsService extends MallConsts {

    /**
     * 发布商品
     *
     * @param currentPrincipal 当事人
     * @param remoteAddr       IP地址
     * @param goodsAddNewParam 新的商品数据
     */
    void addNew(CurrentPrincipal currentPrincipal, String remoteAddr, GoodsAddNewParam goodsAddNewParam);

    /**
     * 根据ID删除商品
     *
     * @param id 尝试删除的商品数据的ID
     */
    void delete(Long id);

    /**
     * 审核通过商品
     *
     * @param currentPrincipal 当事人
     * @param goodsId          尝试审核通过的商品的ID
     * @param remarks          备注信息
     */
    void passCheck(CurrentPrincipal currentPrincipal, Long goodsId, String remarks);

    /**
     * 拒绝审核商品
     *
     * @param currentPrincipal 当事人
     * @param goodsId          尝试拒绝审核的商品的ID
     * @param remarks          备注信息
     */
    void rejectCheck(CurrentPrincipal currentPrincipal, Long goodsId, String remarks);

    /**
     * 上架商品
     *
     * @param id 商品ID
     */
    void putOn(Long id);

    /**
     * 下架商品
     *
     * @param id 商品ID
     */
    void pullOff(Long id);

    /**
     * 推荐商品
     *
     * @param id 商品ID
     */
    void setRecommend(Long id);

    /**
     * 取消推荐商品
     *
     * @param id 商品ID
     */
    void cancelRecommend(Long id);

    /**
     * 根据ID查询商品
     *
     * @param id 商品ID
     * @return 匹配的商品数据详情，如果没有匹配的数据，则返回null
     */
    GoodsStandardVO getStandardById(Long id);

    /**
     * 查询商品列表，将使用默认的每页记录数
     *
     * @param pageNum 页码
     * @return 商品列表
     */
    PageData<GoodsListItemVO> list(Integer pageNum);

    /**
     * 查询商品列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 商品列表
     */
    PageData<GoodsListItemVO> list(Integer pageNum, Integer pageSize);

    /**
     * 根据类别查询商品列表，将使用默认的每页记录数
     *
     * @param categoryId 商品类别的ID
     * @param pageNum    页码
     * @return 商品列表
     */
    PageData<GoodsListItemVO> listByCategory(Long categoryId, Integer pageNum);

    /**
     * 根据类别查询商品列表
     *
     * @param categoryId 商品类别的ID
     * @param pageNum    页码
     * @param pageSize   每页记录数
     * @return 商品列表
     */
    PageData<GoodsListItemVO> listByCategory(Long categoryId, Integer pageNum, Integer pageSize);

    /**
     * 重建商品的搜索数据（更新ES中的商品数据）
     */
    void rebuildSearch();

}
