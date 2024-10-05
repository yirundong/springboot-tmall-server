package cn.tedu.tmall.admin.content.dao.persist.repository.impl;

import cn.tedu.tmall.admin.content.dao.persist.mapper.ArticleMapper;
import cn.tedu.tmall.admin.content.dao.persist.repository.IArticleRepository;
import cn.tedu.tmall.admin.content.pojo.entity.Article;
import cn.tedu.tmall.admin.content.pojo.vo.ArticleListItemVO;
import cn.tedu.tmall.admin.content.pojo.vo.ArticleStandardVO;
import cn.tedu.tmall.admin.content.pojo.vo.search.ArticleSearchVO;
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
 * 处理文章数据的存储库实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Repository
public class ArticleRepositoryImpl implements IArticleRepository {

    @Autowired
    private ArticleMapper articleMapper;

    public ArticleRepositoryImpl() {
        log.info("创建存储库对象：ArticleRepositoryImpl");
    }

    @Override
    public int insert(Article article) {
        log.debug("开始执行【插入文章】的数据访问，参数：{}", article);
        return articleMapper.insert(article);
    }

    @Override
    public int deleteById(Long id) {
        log.debug("开始执行【根据ID删除文章】的数据访问，参数：{}", id);
        return articleMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Collection<Long> idList) {
        log.debug("开始执行【根据若干个ID批量删除文章数据】的数据访问，参数：{}", idList);
        return articleMapper.deleteBatchIds(idList);
    }

    @Override
    public int update(Article article) {
        log.debug("开始执行【根据ID查询文章数据详情】的数据访问，参数：{}", article);
        return articleMapper.updateById(article);
    }

    @Override
    public int countByCategory(Long categoryId) {
        log.debug("开始执行【根据类别统计文章数量】的数据访问，参数：{}", categoryId);
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        return articleMapper.selectCount(queryWrapper);
    }

    @Override
    public ArticleStandardVO getStandardById(Long id) {
        log.debug("开始执行【根据ID查询文章信息】的数据访问，参数：{}", id);
        return articleMapper.getStandardById(id);
    }

    @Override
    public PageData<ArticleListItemVO> list(Integer pageNum, Integer pageSize) {
        log.debug("开始执行【查询文章列表】的数据访问，页码：{}，每页记录数：{}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<ArticleListItemVO> list = articleMapper.list();
        PageInfo<ArticleListItemVO> pageInfo = new PageInfo<>(list);
        return PageInfoToPageDataConverter.convert(pageInfo);
    }

    @Override
    public PageData<ArticleSearchVO> listSearchVO(Integer pageNum, Integer pageSize) {
        log.debug("开始执行【查询文章列表】的数据访问，页码：{}，每页记录数：{}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<ArticleSearchVO> list = articleMapper.listSearchVO();
        PageInfo<ArticleSearchVO> pageInfo = new PageInfo<>(list);
        return PageInfoToPageDataConverter.convert(pageInfo);
    }

    @Override
    public PageData<ArticleListItemVO> listByCategory(Long categoryId, Integer pageNum, Integer pageSize) {
        log.debug("开始执行【查询文章列表】的数据访问，文章类别：{}，页码：{}，每页记录数：{}", categoryId, pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<ArticleListItemVO> list = articleMapper.listByCategory(categoryId);
        PageInfo<ArticleListItemVO> pageInfo = new PageInfo<>(list);
        return PageInfoToPageDataConverter.convert(pageInfo);
    }

}
