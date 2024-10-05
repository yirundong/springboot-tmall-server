package cn.tedu.tmall.admin.content.service.impl;

import cn.tedu.tmall.admin.content.dao.persist.repository.*;
import cn.tedu.tmall.admin.content.dao.search.IArticleSearchRepository;
import cn.tedu.tmall.admin.content.pojo.entity.Article;
import cn.tedu.tmall.admin.content.pojo.entity.ArticleDetail;
import cn.tedu.tmall.admin.content.pojo.entity.CheckLog;
import cn.tedu.tmall.admin.content.pojo.param.ArticleAddNewParam;
import cn.tedu.tmall.admin.content.pojo.vo.ArticleListItemVO;
import cn.tedu.tmall.admin.content.pojo.vo.ArticleStandardVO;
import cn.tedu.tmall.admin.content.pojo.vo.CategoryStandardVO;
import cn.tedu.tmall.admin.content.pojo.vo.search.ArticleSearchVO;
import cn.tedu.tmall.admin.content.service.IArticleService;
import cn.tedu.tmall.common.enumerator.ServiceCode;
import cn.tedu.tmall.common.ex.ServiceException;
import cn.tedu.tmall.common.pojo.authentication.CurrentPrincipal;
import cn.tedu.tmall.common.pojo.vo.PageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 处理文章数据的业务实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Service
public class ArticleServiceImpl implements IArticleService {

    @Value("${tmall.dao.default-query-page-size}")
    private Integer defaultQueryPageSize;
    @Autowired
    private IArticleRepository articleRepository;
    @Autowired
    private IArticleSearchRepository articleSearchRepository;
    @Autowired
    private IArticleDetailRepository articleDetailRepository;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private ICheckLogRepository checkLogRepository;
    @Autowired
    private ICommentRepository commentRepository;
    @Autowired
    private IUpDownLogRepository upDownLogRepository;

    public ArticleServiceImpl() {
        log.debug("创建业务类对象：ArticleServiceImpl");
    }

    @Override
    public void addNew(CurrentPrincipal currentPrincipal, String remoteAddr, ArticleAddNewParam articleAddNewParam) {
        log.debug("开始处理【发布文章】的业务，当事人：{}，IP地址：{}，参数：{}", currentPrincipal, remoteAddr, articleAddNewParam);

        Long categoryId = articleAddNewParam.getCategoryId();
        CategoryStandardVO category = categoryRepository.getStandardById(categoryId);
        if (category == null) {
            String message = "发布文章失败，选择的类别不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        if (category.getEnable() != 1) {
            String message = "发布文章失败，选择的类别已经被禁用！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        Article article = new Article();
        BeanUtils.copyProperties(articleAddNewParam, article);
        article.setAuthorId(currentPrincipal.getId());
        article.setAuthorName(currentPrincipal.getUsername());
        article.setCategoryName(category.getName());
        article.setIp(remoteAddr);
        article.setClickCount(0);
        article.setUpCount(0);
        article.setDownCount(0);
        article.setCheckState(0);
        article.setIsDisplay(0);
        article.setIsRecommend(0);
        int rows = articleRepository.insert(article);
        if (rows != 1) {
            String message = "发布文章失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }

        ArticleDetail articleDetail = new ArticleDetail();
        articleDetail.setArticleId(article.getId());
        articleDetail.setDetail(articleAddNewParam.getDetail());
        rows = articleDetailRepository.insert(articleDetail);
        if (rows != 1) {
            String message = "发布文章失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("开始处理【根据ID删除文章】的业务，参数：{}", id);
        ArticleStandardVO queryResult = articleRepository.getStandardById(id);
        if (queryResult == null) {
            String message = "删除文章失败，尝试删除的文章数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        int rows = articleRepository.deleteById(id);
        if (rows != 1) {
            String message = "删除文章失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }

        rows = articleDetailRepository.deleteByArticle(id);
        if (rows != 1) {
            String message = "删除文章失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }

        checkLogRepository.deleteByResource(RESOURCE_TYPE_ARTICLE, id);
        commentRepository.deleteByResource(RESOURCE_TYPE_ARTICLE, id);
        upDownLogRepository.deleteByResource(RESOURCE_TYPE_ARTICLE, id);
    }

    @Override
    public void passCheck(CurrentPrincipal currentPrincipal, Long id, String remarks) {
        log.debug("开始处理【审核通过文章】的业务，当事人：{}，文章ID：{}，审核备注：{}", currentPrincipal, id, remarks);
        updateCheckById(currentPrincipal, id, CHECK_STATE_PASS, remarks);
    }

    @Override
    public void rejectCheck(CurrentPrincipal currentPrincipal, Long id, String remarks) {
        log.debug("开始处理【拒绝审核文章】的业务，当事人：{}，文章ID：{}，审核备注：{}", currentPrincipal, id, remarks);
        updateCheckById(currentPrincipal, id, CHECK_STATE_REJECT, remarks);
    }

    @Override
    public void setDisplay(Long id) {
        log.debug("开始处理【显示文章】的业务，参数：{}", id);
        updateDisplayById(id, DISPLAY_STATE_ON);
    }

    @Override
    public void setHidden(Long id) {
        log.debug("开始处理【不显示文章】的业务，参数：{}", id);
        updateDisplayById(id, DISPLAY_STATE_OFF);
    }

    @Override
    public ArticleStandardVO getStandardById(Long id) {
        log.debug("开始处理【根据ID查询文章】的业务，参数：{}", id);
        ArticleStandardVO queryResult = articleRepository.getStandardById(id);
        if (queryResult == null) {
            String message = "查询文章详情失败，文章数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }
        return queryResult;
    }

    @Override
    public PageData<ArticleListItemVO> list(Integer pageNum) {
        log.debug("开始处理【查询文章列表】的业务，页码：{}", pageNum);
        return articleRepository.list(pageNum, defaultQueryPageSize);
    }

    @Override
    public PageData<ArticleListItemVO> list(Integer pageNum, Integer pageSize) {
        log.debug("开始处理【查询文章列表】的业务，页码：{}，每页记录数：{}", pageNum, pageSize);
        return articleRepository.list(pageNum, pageSize);
    }

    @Override
    public PageData<ArticleListItemVO> listByCategory(Long categoryId, Integer pageNum) {
        log.debug("开始处理【根据类别查询文章列表】的业务，文章类别：{}, 页码：{}", categoryId, pageNum);
        return articleRepository.listByCategory(categoryId, pageNum, defaultQueryPageSize);
    }

    @Override
    public PageData<ArticleListItemVO> listByCategory(Long categoryId, Integer pageNum, Integer pageSize) {
        log.debug("开始处理【根据类别查询文章列表】的业务，文章类别：{}, 页码：{}，每页记录数：{}", categoryId, pageNum, pageSize);
        return articleRepository.listByCategory(categoryId, pageNum, pageSize);
    }

    @Override
    public void rebuildSearch() {
        log.debug("开始处理【更新搜索服务器中的文章列表】的业务，无参数");
        articleSearchRepository.deleteAll();
        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer maxPage;
        PageData<ArticleSearchVO> pageData;
        do {
            pageData = articleRepository.listSearchVO(pageNum, pageSize);
            maxPage = pageData.getMaxPage();
            articleSearchRepository.saveAll(pageData.getList());
            pageNum++;
        } while (pageNum <= maxPage);
    }

    private void updateCheckById(CurrentPrincipal currentPrincipal, Long id, Integer checkState, String remarks) {
        ArticleStandardVO currentArticle = articleRepository.getStandardById(id);
        if (currentArticle == null) {
            String message = "将文章的审核状态修改为【" + CHECK_STATE_TEXT[checkState] + "】失败，文章数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        if (currentArticle.getCheckState().equals(checkState)) {
            String message = "将文章的审核状态修改为【" + CHECK_STATE_TEXT[checkState] + "】失败，此文章已经处于" + CHECK_STATE_TEXT[checkState] + "状态！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        Article updateArticle = new Article();
        updateArticle.setId(id);
        updateArticle.setCheckState(checkState);
        int rows = articleRepository.update(updateArticle);
        if (rows != 1) {
            String message = "将文章的审核状态修改为【" + CHECK_STATE_TEXT[checkState] + "】失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }

        CheckLog checkLog = new CheckLog();
        checkLog.setResourceType(RESOURCE_TYPE_ARTICLE);
        checkLog.setResourceId(id);
        checkLog.setResourceBrief(currentArticle.getTitle());
        checkLog.setCheckUserId(currentPrincipal.getId());
        checkLog.setCheckUsername(currentPrincipal.getUsername());
        checkLog.setCheckRemarks(remarks);
        checkLog.setOriginalState(currentArticle.getCheckState());
        checkLog.setNewState(checkState);
        checkLog.setGmtCheck(LocalDateTime.now());
        rows = checkLogRepository.insert(checkLog);
        if (rows != 1) {
            String message = "将文章的审核状态修改为【" + CHECK_STATE_TEXT[checkState] + "】失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    private void updateDisplayById(Long id, Integer displayState) {
        ArticleStandardVO currentArticle = articleRepository.getStandardById(id);
        if (currentArticle == null) {
            String message = "将文章的显示状态修改为【" + DISPLAY_STATE_TEXT[displayState] + "】失败，文章数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        if (currentArticle.getIsDisplay().equals(displayState)) {
            String message = "将文章的显示状态修改为【" + DISPLAY_STATE_TEXT[displayState] + "】失败，此文章已经处于" + DISPLAY_STATE_TEXT[displayState] + "状态！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        Article updateArticle = new Article();
        updateArticle.setId(id);
        updateArticle.setIsDisplay(displayState);
        int rows = articleRepository.update(updateArticle);
        if (rows != 1) {
            String message = "将文章的显示状态修改为【" + DISPLAY_STATE_TEXT[displayState] + "】失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

}
