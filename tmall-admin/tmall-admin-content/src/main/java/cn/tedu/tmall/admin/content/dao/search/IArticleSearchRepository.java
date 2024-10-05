package cn.tedu.tmall.admin.content.dao.search;

import cn.tedu.tmall.admin.content.pojo.vo.search.ArticleSearchVO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 处理文章搜索的存储库接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Repository
public interface IArticleSearchRepository
        extends ElasticsearchRepository<ArticleSearchVO, Long> {

}
