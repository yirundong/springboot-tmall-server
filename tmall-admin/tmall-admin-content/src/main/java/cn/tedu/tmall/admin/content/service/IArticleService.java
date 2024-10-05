package cn.tedu.tmall.admin.content.service;

import cn.tedu.tmall.admin.content.pojo.param.ArticleAddNewParam;
import cn.tedu.tmall.admin.content.pojo.vo.ArticleListItemVO;
import cn.tedu.tmall.admin.content.pojo.vo.ArticleStandardVO;
import cn.tedu.tmall.common.consts.data.ContentConsts;
import cn.tedu.tmall.common.pojo.authentication.CurrentPrincipal;
import cn.tedu.tmall.common.pojo.vo.PageData;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理文章数据的业务接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Transactional
public interface IArticleService extends ContentConsts {

    /**
     * 发布文章
     *
     * @param currentPrincipal   当事人
     * @param remoteAddr         IP地址
     * @param articleAddNewParam 新的文章数据
     */
    void addNew(CurrentPrincipal currentPrincipal, String remoteAddr, ArticleAddNewParam articleAddNewParam);

    /**
     * 根据ID删除文章
     *
     * @param id 尝试删除的文章数据的ID
     */
    void delete(Long id);

    /**
     * 审核通过文章
     *
     * @param currentPrincipal 当事人
     * @param id               尝试审核通过的文章的ID
     * @param remarks          审核备注
     */
    void passCheck(CurrentPrincipal currentPrincipal, Long id, String remarks);

    /**
     * 拒绝审核文章
     *
     * @param currentPrincipal 当事人
     * @param id               尝试拒绝审核的文章的ID
     * @param remarks          审核备注
     */
    void rejectCheck(CurrentPrincipal currentPrincipal, Long id, String remarks);

    /**
     * 显示文章
     *
     * @param id 尝试显示的文章的ID
     */
    void setDisplay(Long id);

    /**
     * 隐藏（不显示）文章
     *
     * @param id 尝试隐藏的文章的ID
     */
    void setHidden(Long id);

    /**
     * 根据ID查询文章
     *
     * @param id 文章ID
     * @return 匹配的文章数据详情，如果没有匹配的数据，则返回null
     */
    ArticleStandardVO getStandardById(Long id);

    /**
     * 查询文章列表，将使用默认的每页记录数
     *
     * @param pageNum 页码
     * @return 文章列表
     */
    PageData<ArticleListItemVO> list(Integer pageNum);

    /**
     * 查询文章列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 文章列表
     */
    PageData<ArticleListItemVO> list(Integer pageNum, Integer pageSize);

    /**
     * 根据类别查询文章列表，将使用默认的每页记录数
     *
     * @param categoryId 文章类别的ID
     * @param pageNum    页码
     * @return 文章列表
     */
    PageData<ArticleListItemVO> listByCategory(Long categoryId, Integer pageNum);

    /**
     * 根据类别查询文章列表
     *
     * @param categoryId 文章类别的ID
     * @param pageNum    页码
     * @param pageSize   每页记录数
     * @return 文章列表
     */
    PageData<ArticleListItemVO> listByCategory(Long categoryId, Integer pageNum, Integer pageSize);

    /**
     * 重建搜索数据（重新从数据库中获取数据并写入到ES中）
     */
    void rebuildSearch();

}
