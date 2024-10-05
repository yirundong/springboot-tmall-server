package cn.tedu.tmall.admin.mall.dao.search;

import cn.tedu.tmall.admin.mall.pojo.vo.GoodsSearchVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理商品的es数据库的访问,只需要提供接口,框架自己实现
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 19:40
 */
@Repository
public interface IGoodsSearchRepository
        extends ElasticsearchRepository<GoodsSearchVO, Long> {
    // 【分页搜索】
    @Highlight(fields = {@HighlightField(name = "title")},
            parameters = @HighlightParameters(
                    preTags = "<font style='color: red;'>", postTags = "</font>"))
    SearchPage<GoodsSearchVO> queryByTitle(String title, Pageable pageable);

    // 【简单搜索】
    List<GoodsSearchVO> queryByTitle(String title);

    // 【自定义条件搜索】这个是在http文件中写出来再cv
    @Query("{" +
            "    \"match\": {\n" +
            "      \"title\": \"?0\"\n" +
            "    }\n" +
            "  }")
    List<GoodsSearchVO> customQuery(String title);

}