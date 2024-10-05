package cn.tedu.tmall.admin.mall.dao.persist.mapper;

import cn.tedu.tmall.admin.mall.pojo.entity.Goods;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsListItemVO;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsSearchVO;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsStandardVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理商品数据的Mapper接口
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 19:56
 */
@Repository
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 根据ID查询商品
     *
     * @param id 商品ID
     * @return 匹配的商品，如果没有匹配的数据，则返回null
     */
    GoodsStandardVO getStandardById(Long id);

    /**
     * 查询商品数据列表
     *
     * @return 商品数据列表
     */
    List<GoodsListItemVO> list();

    /**
     * 查询用于搜索的商品数据列表，此搜索结果将用于写入到ES中
     *
     * @return 商品数据列表
     */
    List<GoodsSearchVO> listSearch();

    /**
     * 根据类别查询商品列表
     *
     * @param categoryId 商品类别的ID
     * @return 商品列表
     */
    List<GoodsListItemVO> listByCategory(Long categoryId);

}