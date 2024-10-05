package cn.tedu.tmall.admin.mall.dao.persist.repository.impl;

import cn.tedu.tmall.admin.mall.dao.persist.mapper.GoodsMapper;
import cn.tedu.tmall.admin.mall.dao.persist.repository.IGoodsRepository;
import cn.tedu.tmall.admin.mall.pojo.entity.Goods;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsListItemVO;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsSearchVO;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsStandardVO;
import cn.tedu.tmall.common.pojo.vo.PageData;
import cn.tedu.tmall.common.util.PageInfoToPageDataConverter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 处理商品数据的存储库实现类
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 19:55
 */
@Slf4j
@Repository
public class GoodsRepositoryImpl implements IGoodsRepository {

    @Autowired
    private GoodsMapper goodsMapper;

    public GoodsRepositoryImpl() {
        log.info("创建存储库对象：GoodsRepositoryImpl");
    }

    @Override
    public int insert(Goods goods) {
        log.debug("开始执行【插入商品】的数据访问，参数：{}", goods);
        return goodsMapper.insert(goods);
    }

    @Override
    public int deleteById(Long id) {
        log.debug("开始执行【根据ID删除商品】的数据访问，参数：{}", id);
        return goodsMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Collection<Long> idList) {
        log.debug("开始执行【批量删除商品】的数据访问，参数：{}", idList);
        return goodsMapper.deleteBatchIds(idList);
    }

    @Override
    public int update(Goods goods) {
        log.debug("开始执行【更新商品】的数据访问，参数：{}", goods);
        return goodsMapper.updateById(goods);
    }

    @Override
    public int count() {
        log.debug("开始执行【统计商品的数量】的数据访问，参数：无");
        return goodsMapper.selectCount(null);
    }

    @Override
    public int countByCategory(Long categoryId) {
        log.debug("开始执行【根据类别统计商品数量】的数据访问，参数：{}", categoryId);
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        return goodsMapper.selectCount(queryWrapper);
    }

    @Override
    public GoodsStandardVO getStandardById(Long id) {
        log.debug("开始执行【根据ID查询商品信息】的数据访问，参数：{}", id);
        return goodsMapper.getStandardById(id);
    }

    @Override
    public PageData<GoodsListItemVO> list(Integer pageNum, Integer pageSize) {
        log.debug("开始执行【查询商品列表】的数据访问，页码：{}，每页记录数：{}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsListItemVO> list = goodsMapper.list();
        PageInfo<GoodsListItemVO> pageInfo = new PageInfo<>(list);
        return PageInfoToPageDataConverter.convert(pageInfo);
    }

    @Override
    public PageData<GoodsSearchVO> listSearch(Integer pageNum, Integer pageSize) {
        log.debug("开始执行【查询用于搜索的商品数据列表】的数据访问，页码：{}，每页记录数：{}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsSearchVO> list = goodsMapper.listSearch();
        PageInfo<GoodsSearchVO> pageInfo = new PageInfo<>(list);
        return PageInfoToPageDataConverter.convert(pageInfo);
    }

    @Override
    public PageData<GoodsListItemVO> listByCategory(Long categoryId, Integer pageNum, Integer pageSize) {
        log.debug("开始执行【根据类别查询商品列表】的数据访问，商品类别：{}，页码：{}，每页记录数：{}", categoryId, pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsListItemVO> list = goodsMapper.listByCategory(categoryId);
        PageInfo<GoodsListItemVO> pageInfo = new PageInfo<>(list);
        return PageInfoToPageDataConverter.convert(pageInfo);
    }

}
