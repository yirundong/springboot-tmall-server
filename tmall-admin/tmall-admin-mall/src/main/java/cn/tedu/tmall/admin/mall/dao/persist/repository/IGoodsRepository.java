package cn.tedu.tmall.admin.mall.dao.persist.repository;

import cn.tedu.tmall.admin.mall.pojo.entity.Goods;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsListItemVO;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsSearchVO;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsStandardVO;
import cn.tedu.tmall.common.pojo.vo.PageData;

import java.util.Collection;

/**
 * 处理商品数据的存储库接口
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 19:55
 */
public interface IGoodsRepository {


    /**
     * 插入商品数据
     *
     * @param goods 商品数据
     * @return 受影响的行数
     */
    int insert(Goods goods);

    /**
     * 根据ID删除商品数据
     *
     * @param id 商品ID
     * @return 受影响的行数
     */
    int deleteById(Long id);

    /**
     * 根据若干个ID批量删除商品数据
     *
     * @param idList 若干个商品ID的数组
     * @return 受影响的行数
     */
    int deleteByIds(Collection<Long> idList);

    /**
     * 根据ID修改商品数据
     *
     * @param goods 封装了商品ID和新数据的对象
     * @return 受影响的行数
     */
    int update(Goods goods);

    /**
     * 统计商品表中的数据的数量
     *
     * @return 商品表中的数据的数量
     */
    int count();

    /**
     * 根据类别统计商品数量
     *
     * @param categoryId 类别ID
     * @return 归属此类别下的商品数量
     */
    int countByCategory(Long categoryId);

    /**
     * 根据ID查询商品数据详情
     *
     * @param id 商品ID
     * @return 匹配的商品数据详情，如果没有匹配的数据，则返回null
     */
    GoodsStandardVO getStandardById(Long id);

    /**
     * 查询商品数据列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 商品数据列表
     */
    PageData<GoodsListItemVO> list(Integer pageNum, Integer pageSize);

    /**
     * 查询用于搜索的商品数据列表，此搜索结果将用于写入到ES中
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 商品数据列表
     */
    PageData<GoodsSearchVO> listSearch(Integer pageNum, Integer pageSize);

    /**
     * 根据类别查询商品列表
     *
     * @param categoryId 商品类别的ID
     * @param pageNum    页码
     * @param pageSize   每页记录数
     * @return 商品列表
     */
    PageData<GoodsListItemVO> listByCategory(Long categoryId, Integer pageNum, Integer pageSize);

}
