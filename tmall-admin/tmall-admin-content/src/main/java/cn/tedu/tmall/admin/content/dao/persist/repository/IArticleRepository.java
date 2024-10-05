package cn.tedu.tmall.admin.content.dao.persist.repository;

import cn.tedu.tmall.admin.content.pojo.entity.Article;
import cn.tedu.tmall.admin.content.pojo.vo.ArticleListItemVO;
import cn.tedu.tmall.admin.content.pojo.vo.ArticleStandardVO;
import cn.tedu.tmall.admin.content.pojo.vo.search.ArticleSearchVO;
import cn.tedu.tmall.common.pojo.vo.PageData;

import java.util.Collection;

/**
 * 处理文章数据的存储库接口
 *
 * @author YiRunDong
 * @version 2.0
 */
public interface IArticleRepository {

    /**
     * 插入文章数据
     *
     * @param article 文章数据
     * @return 受影响的行数
     */
    int insert(Article article);

    /**
     * 根据ID删除文章数据
     *
     * @param id 文章ID
     * @return 受影响的行数
     */
    int deleteById(Long id);

    /**
     * 根据若干个ID批量删除文章数据
     *
     * @param idList 若干个文章ID的数组
     * @return 受影响的行数
     */
    int deleteByIds(Collection<Long> idList);

    /**
     * 根据ID修改文章数据
     *
     * @param article 封装了文章ID和新数据的对象
     * @return 受影响的行数
     */
    int update(Article article);

    /**
     * 根据类别统计文章数量
     *
     * @param categoryId 类别ID
     * @return 归属此类别下的文章数量
     */
    int countByCategory(Long categoryId);

    /**
     * 根据ID查询文章数据详情
     *
     * @param id 文章ID
     * @return 匹配的文章数据详情，如果没有匹配的数据，则返回null
     */
    ArticleStandardVO getStandardById(Long id);

    /**
     * 查询文章数据列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 文章数据列表
     */
    PageData<ArticleListItemVO> list(Integer pageNum, Integer pageSize);

    /**
     * 查询文章数据列表，用于读取数据后存入到elasticsearch中
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 文章数据列表
     */
    PageData<ArticleSearchVO> listSearchVO(Integer pageNum, Integer pageSize);

    /**
     * 根据类别查询文章列表
     *
     * @param categoryId 文章类别的ID
     * @param pageNum    页码
     * @param pageSize   每页记录数
     * @return 文章列表
     */
    PageData<ArticleListItemVO> listByCategory(Long categoryId, Integer pageNum, Integer pageSize);

}
