package cn.tedu.tmall.admin.content.dao.persist.repository;

import cn.tedu.tmall.admin.content.pojo.entity.ArticleDetail;

/**
 * 处理文章详情数据的存储库接口
 *
 * @author YiRunDong
 * @version 2.0
 */
public interface IArticleDetailRepository {

    /**
     * 插入文章详情数据
     *
     * @param articleDetail 文章详情数据
     * @return 受影响的行数
     */
    int insert(ArticleDetail articleDetail);

    /**
     * 根据文章ID删除文章详情数据
     *
     * @param articleId 文章ID
     * @return 受影响的行数
     */
    int deleteByArticle(Long articleId);

    /**
     * 根据ID修改文章详情数据
     *
     * @param articleDetail 封装了文章ID和新文章详情数据的对象
     * @return 受影响的行数
     */
    int updateByArticle(ArticleDetail articleDetail);

}
