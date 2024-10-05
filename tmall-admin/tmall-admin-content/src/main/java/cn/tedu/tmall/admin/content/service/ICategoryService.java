package cn.tedu.tmall.admin.content.service;

import cn.tedu.tmall.admin.content.pojo.param.CategoryAddNewParam;
import cn.tedu.tmall.admin.content.pojo.param.CategoryUpdateInfoParam;
import cn.tedu.tmall.admin.content.pojo.vo.CategoryListItemVO;
import cn.tedu.tmall.admin.content.pojo.vo.CategoryStandardVO;
import cn.tedu.tmall.common.consts.data.ContentConsts;
import cn.tedu.tmall.common.pojo.vo.PageData;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理类别业务的接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Transactional
public interface ICategoryService extends ContentConsts {

    /**
     * 添加类别
     *
     * @param categoryAddNewParam 新的类别数据
     */
    void addNew(CategoryAddNewParam categoryAddNewParam);

    /**
     * 根据ID删除类别
     *
     * @param id 尝试删除的类别数据的ID
     */
    void delete(Long id);

    /**
     * 启用类别
     *
     * @param id 尝试启用的类别的ID
     */
    void setEnable(Long id);

    /**
     * 禁用类别
     *
     * @param id 尝试禁用的类别的ID
     */
    void setDisable(Long id);

    /**
     * 显示类别
     *
     * @param id 尝试显示的类别的ID
     */
    void setDisplay(Long id);

    /**
     * 隐藏类别
     *
     * @param id 尝试隐藏的类别的ID
     */
    void setHidden(Long id);

    /**
     * 修改类别数据
     *
     * @param id                      被修改的类别数据的ID
     * @param categoryUpdateInfoParam 类别的新数据
     */
    void updateInfoById(Long id, CategoryUpdateInfoParam categoryUpdateInfoParam);

    /**
     * 根据ID查询类别详情
     *
     * @param id 类别id
     * @return 匹配的类别详情，如果没有匹配的数据，则返回null
     */
    CategoryStandardVO getStandardById(Long id);

    /**
     * 查询类别列表，将使用默认的每页记录数
     *
     * @param pageNum 页码
     * @return 类别列表
     */
    PageData<CategoryListItemVO> list(Integer pageNum);

    /**
     * 查询类别列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 类别列表
     */
    PageData<CategoryListItemVO> list(Integer pageNum, Integer pageSize);

    /**
     * 重建缓存
     */
    void rebuildCache();

}
