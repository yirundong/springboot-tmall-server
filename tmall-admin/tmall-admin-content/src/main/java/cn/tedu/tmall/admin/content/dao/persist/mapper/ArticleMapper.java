package cn.tedu.tmall.admin.content.dao.persist.mapper;

import cn.tedu.tmall.admin.content.pojo.entity.Article;
import cn.tedu.tmall.admin.content.pojo.vo.ArticleListItemVO;
import cn.tedu.tmall.admin.content.pojo.vo.ArticleStandardVO;
import cn.tedu.tmall.admin.content.pojo.vo.search.ArticleSearchVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理文章数据的Mapper接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 根据ID查询文章
     *
     * @param id 文章ID
     * @return 匹配的文章，如果没有匹配的数据，则返回null
     */
    ArticleStandardVO getStandardById(Long id);

    /**
     * 查询文章数据列表
     *
     * @return 文章数据列表
     */
    List<ArticleListItemVO> list();

    /**
     * 查询文章数据列表，用于读取数据后存入到elasticsearch中
     *
     * @return 文章数据列表
     */
    List<ArticleSearchVO> listSearchVO();

    /**
     * 根据类别查询文章列表
     *
     * @param categoryId 文章类别ID
     * @return 文章列表
     */
    List<ArticleListItemVO> listByCategory(Long categoryId);

}
