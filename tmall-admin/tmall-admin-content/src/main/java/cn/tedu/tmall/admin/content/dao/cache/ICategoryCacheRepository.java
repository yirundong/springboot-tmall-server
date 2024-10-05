package cn.tedu.tmall.admin.content.dao.cache;

import cn.tedu.tmall.admin.content.pojo.vo.CategoryListItemVO;
import cn.tedu.tmall.common.consts.cache.ContentCacheConsts;

import java.util.List;

public interface ICategoryCacheRepository extends ContentCacheConsts {

    void saveList(List<CategoryListItemVO> categoryList);

    boolean deleteList();

    List<CategoryListItemVO> list();

}
