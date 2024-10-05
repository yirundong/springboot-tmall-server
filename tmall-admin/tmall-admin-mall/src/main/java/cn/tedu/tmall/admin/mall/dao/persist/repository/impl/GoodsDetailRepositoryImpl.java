package cn.tedu.tmall.admin.mall.dao.persist.repository.impl;

import cn.tedu.tmall.admin.mall.dao.persist.mapper.GoodsDetailMapper;
import cn.tedu.tmall.admin.mall.dao.persist.repository.IGoodsDetailRepository;
import cn.tedu.tmall.admin.mall.pojo.entity.GoodsDetail;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 处理商品详情数据的存储库实现类
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 19:55
 */
@Slf4j
@Repository
public class GoodsDetailRepositoryImpl implements IGoodsDetailRepository {

    @Autowired
    private GoodsDetailMapper goodsDetailMapper;

    public GoodsDetailRepositoryImpl() {
        log.info("创建存储库对象：GoodsDetailRepositoryImpl");
    }

    @Override
    public int insert(GoodsDetail articleDetail) {
        log.debug("开始执行【插入商品详情】的数据访问，参数：{}", articleDetail);
        return goodsDetailMapper.insert(articleDetail);
    }

    @Override
    public int deleteByGoods(Long goodsId) {
        log.debug("开始执行【根据商品ID删除商品详情数据】的数据访问，参数：{}", goodsId);
        QueryWrapper<GoodsDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", goodsId);
        return goodsDetailMapper.delete(queryWrapper);
    }

    @Override
    public int updateByGoods(GoodsDetail articleDetail) {
        log.debug("开始执行【更新商品详情】的数据访问，参数：{}", articleDetail);
        QueryWrapper<GoodsDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", articleDetail.getGoodsId());
        return goodsDetailMapper.update(articleDetail, queryWrapper);
    }

}
