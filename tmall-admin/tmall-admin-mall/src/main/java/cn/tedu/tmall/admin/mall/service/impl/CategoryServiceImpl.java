package cn.tedu.tmall.admin.mall.service.impl;

import cn.tedu.tmall.admin.mall.dao.persist.repository.ICategoryRepository;
import cn.tedu.tmall.admin.mall.dao.persist.repository.IGoodsRepository;
import cn.tedu.tmall.admin.mall.pojo.entity.Category;
import cn.tedu.tmall.admin.mall.pojo.param.CategoryAddNewParam;
import cn.tedu.tmall.admin.mall.pojo.param.CategoryUpdateInfoParam;
import cn.tedu.tmall.admin.mall.pojo.vo.CategoryListItemVO;
import cn.tedu.tmall.admin.mall.pojo.vo.CategoryStandardVO;
import cn.tedu.tmall.admin.mall.pojo.vo.CategoryTreeItemVO;
import cn.tedu.tmall.admin.mall.service.ICategoryService;
import cn.tedu.tmall.common.enumerator.ServiceCode;
import cn.tedu.tmall.common.ex.ServiceException;
import cn.tedu.tmall.common.pojo.vo.PageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 处理类别的业务实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Value("${tmall.dao.default-query-page-size}")
    private Integer defaultQueryPageSize;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IGoodsRepository goodsRepository;

    public CategoryServiceImpl() {
        log.debug("创建业务类对象：CategoryServiceImpl");
    }

    @Override
    public void addNew(CategoryAddNewParam categoryAddNewParam) {
        log.debug("开始处理【添加类别】的业务，参数：{}", categoryAddNewParam);
        String name = categoryAddNewParam.getName();
        int count = categoryRepository.countByName(name);
        log.debug("根据名称【{}】统计数量，结果：{}", name, count);
        if (count > 0) {
            String message = "添加类别失败，类别名称已经被占用！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        Long parentId = categoryAddNewParam.getParentId();
        Integer depth = 1;
        CategoryStandardVO parentCategory = null;
        if (parentId != 0) {
            parentCategory = categoryRepository.getStandardById(parentId);
            if (parentCategory == null) {
                String message = "添加类别失败，父级类别不存在！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
            } else {
                depth = parentCategory.getDepth() + 1;
            }
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryAddNewParam, category);
        category.setDepth(depth);
        category.setIsParent(0);
        int rows = categoryRepository.insert(category);
        if (rows != 1) {
            String message = "添加类别失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }

        if (parentId != 0 && parentCategory.getIsParent() == 0) {
            Category updateParentCategory = new Category();
            updateParentCategory.setId(parentId);
            updateParentCategory.setIsParent(1);
            rows = categoryRepository.update(updateParentCategory);
            if (rows != 1) {
                String message = "添加类别失败，服务器忙，请稍后再尝试！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
            }
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("开始处理【根据ID删除类别】的业务，参数：{}", id);
        CategoryStandardVO currentCategory = categoryRepository.getStandardById(id);
        if (currentCategory == null) {
            String message = "删除类别失败，尝试删除的类别数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        if (currentCategory.getIsParent() == 1) {
            String message = "删除类别失败，该类别仍包含子级类别！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        int goodsCount = goodsRepository.countByCategory(id);
        if (goodsCount > 0) {
            String message = "删除类别失败，仍有商品关联到此类别！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        int rows = categoryRepository.deleteById(id);
        if (rows != 1) {
            String message = "删除类别失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }

        Long parentId = currentCategory.getParentId();
        int count = categoryRepository.countByParent(parentId);
        if (count == 0) {
            Category parentCategory = new Category();
            parentCategory.setId(parentId);
            parentCategory.setIsParent(0);
            rows = categoryRepository.update(parentCategory);
            if (rows != 1) {
                String message = "删除类别失败，服务器忙，请稍后再尝试！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
            }
        }
    }

    @Override
    public void setEnable(Long id) {
        log.debug("开始处理【启用类别】的业务，参数：{}", id);
        updateEnableById(id, ENABLE_STATE_ON);
    }

    @Override
    public void setDisable(Long id) {
        log.debug("开始处理【禁用类别】的业务，参数：{}", id);
        updateEnableById(id, ENABLE_STATE_OFF);
    }

    @Override
    public void setDisplay(Long id) {
        log.debug("开始处理【将类别显示在导航栏】的业务，参数：{}", id);
        updateDisplayById(id, DISPLAY_STATE_ON);
    }

    @Override
    public void setHidden(Long id) {
        log.debug("开始处理【将类别不显示在导航栏】的业务，参数：{}", id);
        updateDisplayById(id, DISPLAY_STATE_OFF);
    }

    @Override
    public void updateInfoById(Long id, CategoryUpdateInfoParam categoryUpdateInfoParam) {
        log.debug("开始处理【修改类别详情】的业务，ID：{}，新数据：{}", id, categoryUpdateInfoParam);
        CategoryStandardVO queryResult = categoryRepository.getStandardById(id);
        if (queryResult == null) {
            String message = "修改类别详情失败，尝试修改的类别数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        int count = categoryRepository.countByNameAndNotId(id, categoryUpdateInfoParam.getName());
        if (count > 0) {
            String message = "修改类别详情失败，类别名称已经被占用！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryUpdateInfoParam, category);
        category.setId(id);
        int rows = categoryRepository.update(category);
        if (rows != 1) {
            String message = "修改类别详情失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    @Override
    public CategoryStandardVO getStandardById(Long id) {
        log.debug("开始处理【根据ID查询类别】的业务，参数：{}", id);
        CategoryStandardVO queryResult = categoryRepository.getStandardById(id);
        if (queryResult == null) {
            String message = "查询类别详情失败，类别数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }
        return queryResult;
    }

    @Override
    public List<CategoryTreeItemVO> listTree() {
        log.debug("开始处理【查询类别树】的业务，参数：无");
        List<CategoryTreeItemVO> categoryTree = new ArrayList<>();

        List<CategoryListItemVO> categoryList = categoryRepository.list();
        Map<Long, CategoryListItemVO> allCategoryMap = transformListToMap(categoryList);
        for (Long key : allCategoryMap.keySet()) {
            CategoryListItemVO mapItem = allCategoryMap.get(key);
            if (mapItem.getParentId() == 0) {
                CategoryTreeItemVO categoryTreeItemVO = convertListItemToTreeItem(mapItem);
                categoryTree.add(categoryTreeItemVO);

                fillChildren(mapItem, categoryTreeItemVO, allCategoryMap);
            }
        }

        return categoryTree;
    }

    private Map<Long, CategoryListItemVO> transformListToMap(List<CategoryListItemVO> categoryList) {
        Map<Long, CategoryListItemVO> categoryMap = new LinkedHashMap<>();
        for (CategoryListItemVO categoryListItemVO : categoryList) {
            if (categoryListItemVO.getEnable() == 0) {
                continue;
            }
            categoryMap.put(categoryListItemVO.getId(), categoryListItemVO);
        }
        return categoryMap;
    }

    private void fillChildren(CategoryListItemVO listItem, CategoryTreeItemVO currentTreeItem, Map<Long, CategoryListItemVO> allCategoryMap) {
        if (listItem.getIsParent() == 1) {
            currentTreeItem.setChildren(new ArrayList<>());
            Set<Long> keySet = allCategoryMap.keySet();
            for (Long key : keySet) {
                CategoryListItemVO mapItem = allCategoryMap.get(key);
                if (mapItem.getParentId().equals(listItem.getId())) {
                    CategoryTreeItemVO categoryTreeSubItemVO = convertListItemToTreeItem(mapItem);
                    currentTreeItem.getChildren().add(categoryTreeSubItemVO);
                    if (mapItem.getIsParent() == 1) {
                        fillChildren(mapItem, categoryTreeSubItemVO, allCategoryMap);
                    }
                }
            }
        }
    }

    private CategoryTreeItemVO convertListItemToTreeItem(CategoryListItemVO listItem) {
        return new CategoryTreeItemVO()
                .setValue(listItem.getId())
                .setLabel(listItem.getName());
    }

    @Override
    public PageData<CategoryListItemVO> listByParent(Long parentId, Integer pageNum) {
        log.debug("开始处理【根据父级查询子级列表】的业务，父级类别：{}, 页码：{}", parentId, pageNum);
        return categoryRepository.listByParent(parentId, pageNum, defaultQueryPageSize);
    }

    @Override
    public PageData<CategoryListItemVO> listByParent(Long parentId, Integer pageNum, Integer pageSize) {
        log.debug("开始处理【根据父级查询子级列表】的业务，父级类别：{}, 页码：{}，每页记录数：{}", parentId, pageNum, pageSize);
        return categoryRepository.listByParent(parentId, pageNum, pageSize);
    }

    private void updateEnableById(Long id, Integer enable) {
        CategoryStandardVO currentCategory = categoryRepository.getStandardById(id);
        if (currentCategory == null) {
            String message = ENABLE_STATE_TEXT[enable] + "类别失败，类别数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        if (currentCategory.getEnable().equals(enable)) {
            String message = ENABLE_STATE_TEXT[enable] + "类别失败，此类别已经处于" + ENABLE_STATE_TEXT[enable] + "状态！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        Category updateCategory = new Category();
        updateCategory.setId(id);
        updateCategory.setEnable(enable);
        int rows = categoryRepository.update(updateCategory);
        if (rows != 1) {
            String message = ENABLE_STATE_TEXT[enable] + "类别失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    private void updateDisplayById(Long id, Integer isDisplay) {
        CategoryStandardVO currentCategory = categoryRepository.getStandardById(id);
        if (currentCategory == null) {
            String message = DISPLAY_STATE_TEXT[isDisplay] + "类别失败，类别数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        if (currentCategory.getIsDisplay().equals(isDisplay)) {
            String message = DISPLAY_STATE_TEXT[isDisplay] + "类别失败，此类别已经处于" + DISPLAY_STATE_TEXT[isDisplay] + "状态！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        Category updateCategory = new Category();
        updateCategory.setId(id);
        updateCategory.setIsDisplay(isDisplay);
        int rows = categoryRepository.update(updateCategory);
        if (rows != 1) {
            String message = DISPLAY_STATE_TEXT[isDisplay] + "类别失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

}
