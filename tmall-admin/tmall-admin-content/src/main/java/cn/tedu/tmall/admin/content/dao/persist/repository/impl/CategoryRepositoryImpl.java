package cn.tedu.tmall.admin.content.dao.persist.repository.impl;

import cn.tedu.tmall.admin.content.dao.persist.mapper.CategoryMapper;
import cn.tedu.tmall.admin.content.dao.persist.repository.ICategoryRepository;
import cn.tedu.tmall.admin.content.pojo.entity.Category;
import cn.tedu.tmall.admin.content.pojo.vo.CategoryListItemVO;
import cn.tedu.tmall.admin.content.pojo.vo.CategoryStandardVO;
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
 * 处理类别数据的存储库实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Repository
public class CategoryRepositoryImpl implements ICategoryRepository {

    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryRepositoryImpl() {
        log.info("创建存储库对象：CategoryRepositoryImpl");
    }

    @Override
    public int insert(Category category) {
        log.debug("开始执行【插入类别】的数据访问，参数：{}", category);
        return categoryMapper.insert(category);
    }

    @Override
    public int deleteById(Long id) {
        log.debug("开始执行【根据ID删除类别】的数据访问，参数：{}", id);
        return categoryMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Collection<Long> idList) {
        log.debug("开始执行【批量删除类别】的数据访问，参数：{}", idList);
        return categoryMapper.deleteBatchIds(idList);
    }

    @Override
    public int update(Category category) {
        log.debug("开始执行【更新类别】的数据访问，参数：{}", category);
        return categoryMapper.updateById(category);
    }

    @Override
    public int count() {
        log.debug("开始执行【统计类别的数量】的数据访问，参数：无");
        return categoryMapper.selectCount(null);
    }

    @Override
    public int countByName(String name) {
        log.debug("开始执行【统计匹配名称的类别的数量】的数据访问，参数：{}", name);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return categoryMapper.selectCount(queryWrapper);
    }

    @Override
    public int countByNameAndNotId(Long id, String name) {
        log.debug("开始执行【统计匹配名称但不匹配ID的类别的数量】的数据访问，ID：{}，类别：{}", id, name);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name).ne("id", id);
        return categoryMapper.selectCount(queryWrapper);
    }

    @Override
    public CategoryStandardVO getStandardById(Long id) {
        log.debug("开始执行【根据ID查询类别信息】的数据访问，参数：{}", id);
        return categoryMapper.getStandardById(id);
    }

    @Override
    public List<CategoryListItemVO> list() {
        log.debug("开始执行【查询类别列表】的数据访问，参数：无");
        return categoryMapper.list();
    }

    @Override
    public PageData<CategoryListItemVO> list(Integer pageNum, Integer pageSize) {
        log.debug("开始执行【查询类别列表】的数据访问，页码：{}，每页记录数：{}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<CategoryListItemVO> list = categoryMapper.list();
        PageInfo<CategoryListItemVO> pageInfo = new PageInfo<>(list);
        return PageInfoToPageDataConverter.convert(pageInfo);
    }

}
