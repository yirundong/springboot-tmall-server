package cn.tedu.tmall.admin.content.dao.persist.repository.impl;

import cn.tedu.tmall.admin.content.dao.persist.mapper.ArticleDetailMapper;
import cn.tedu.tmall.admin.content.dao.persist.repository.IArticleDetailRepository;
import cn.tedu.tmall.admin.content.pojo.entity.ArticleDetail;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 处理文章详情数据的存储库实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Repository
public class ArticleDetailRepositoryImpl implements IArticleDetailRepository {

    @Autowired
    private ArticleDetailMapper articleDetailMapper;

    public ArticleDetailRepositoryImpl() {
        log.info("创建存储库对象：ArticleDetailRepositoryImpl");
    }

    @Override
    public int insert(ArticleDetail articleDetail) {
        log.debug("开始执行【插入文章详情】的数据访问，参数：{}", articleDetail);
        return articleDetailMapper.insert(articleDetail);
    }

    @Override
    public int deleteByArticle(Long articleId) {
        log.debug("开始执行【根据文章ID删除文章详情数据】的数据访问，参数：{}", articleId);
        QueryWrapper<ArticleDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId);
        return articleDetailMapper.delete(queryWrapper);
    }

    @Override
    public int updateByArticle(ArticleDetail articleDetail) {
        log.debug("开始执行【更新文章详情】的数据访问，参数：{}", articleDetail);
        QueryWrapper<ArticleDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleDetail.getArticleId());
        return articleDetailMapper.update(articleDetail, queryWrapper);
    }

}
