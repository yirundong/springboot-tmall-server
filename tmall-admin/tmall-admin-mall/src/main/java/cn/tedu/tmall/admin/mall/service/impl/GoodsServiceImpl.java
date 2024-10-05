package cn.tedu.tmall.admin.mall.service.impl;

import cn.tedu.tmall.admin.mall.dao.persist.repository.*;
import cn.tedu.tmall.admin.mall.dao.search.IGoodsSearchRepository;
import cn.tedu.tmall.admin.mall.pojo.entity.CheckLog;
import cn.tedu.tmall.admin.mall.pojo.entity.Goods;
import cn.tedu.tmall.admin.mall.pojo.entity.GoodsDetail;
import cn.tedu.tmall.admin.mall.pojo.param.GoodsAddNewParam;
import cn.tedu.tmall.admin.mall.pojo.vo.CategoryStandardVO;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsListItemVO;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsSearchVO;
import cn.tedu.tmall.admin.mall.pojo.vo.GoodsStandardVO;
import cn.tedu.tmall.admin.mall.service.IGoodsService;
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
 * 处理商品数据的业务实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Service
public class GoodsServiceImpl implements IGoodsService {

    @Value("${tmall.dao.default-query-page-size}")
    private Integer defaultQueryPageSize;
    @Autowired
    private IGoodsRepository goodsRepository;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IGoodsDetailRepository goodsDetailRepository;
    @Autowired
    private ICommentRepository commentRepository;
    @Autowired
    private ICheckLogRepository checkLogRepository;
    @Autowired
    private IGoodsSearchRepository goodsSearchRepository;

    public GoodsServiceImpl() {
        log.debug("创建业务类对象：GoodsServiceImpl");
    }

    @Override
    public void addNew(CurrentPrincipal currentPrincipal, String remoteAddr, GoodsAddNewParam goodsAddNewParam) {
        log.debug("开始处理【发布商品】的业务，当事人：{}，IP地址：{}，参数：{}", currentPrincipal, remoteAddr, goodsAddNewParam);

        Long categoryId = goodsAddNewParam.getCategoryId();
        CategoryStandardVO category = categoryRepository.getStandardById(categoryId);
        if (category == null) {
            String message = "发布商品失败，类别数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        if (category.getIsParent() != 0) {
            String message = "发布商品失败，选择的类别必须不包含子级类别！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        if (category.getEnable() != 1) {
            String message = "发布商品失败，选择的类别已经被禁用！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsAddNewParam, goods);
        goods.setCategoryName(category.getName());
        goods.setCheckState(0);
        goods.setIsRecommend(0);
        goods.setIsPutOn(0);
        goods.setSalesCount(0);
        goods.setCommentCount(0);
        goods.setPositiveCommentCount(0);
        goods.setNegativeCommentCount(0);
        int rows = goodsRepository.insert(goods);
        if (rows != 1) {
            String message = "发布商品失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }

        GoodsDetail goodsDetail = new GoodsDetail();
        goodsDetail.setGoodsId(goods.getId());
        goodsDetail.setDetail(goodsAddNewParam.getDetail());
        rows = goodsDetailRepository.insert(goodsDetail);
        if (rows != 1) {
            String message = "发布商品失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("开始处理【根据ID删除商品】的业务，参数：{}", id);
        GoodsStandardVO queryResult = goodsRepository.getStandardById(id);
        if (queryResult == null) {
            String message = "删除商品失败，尝试删除的商品数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        int rows = goodsRepository.deleteById(id);
        if (rows != 1) {
            String message = "删除商品失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }

        rows = goodsDetailRepository.deleteByGoods(id);
        if (rows != 1) {
            String message = "删除商品失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }

        commentRepository.deleteByGoods(id);
        checkLogRepository.deleteByResource(RESOURCE_TYPE_GOODS, id);
    }

    @Override
    public void passCheck(CurrentPrincipal currentPrincipal, Long goodsId, String remarks) {
        log.debug("开始处理【审核通过商品】的业务，参数：{}", goodsId);
        updateCheckById(currentPrincipal, goodsId, CHECK_STATE_PASS, remarks);
    }

    @Override
    public void rejectCheck(CurrentPrincipal currentPrincipal, Long goodsId, String remarks) {
        log.debug("开始处理【拒绝审核商品】的业务，参数：{}", goodsId);
        updateCheckById(currentPrincipal, goodsId, CHECK_STATE_REJECT, remarks);
    }

    @Override
    public void putOn(Long id) {
        log.debug("开始处理【上架商品】的业务，参数：{}", id);
        updatePutOnById(id, PUT_ON_STATE_ON);
    }

    @Override
    public void pullOff(Long id) {
        log.debug("开始处理【下架商品】的业务，参数：{}", id);
        updatePutOnById(id, PUT_ON_STATE_OFF);
    }

    @Override
    public void setRecommend(Long id) {
        log.debug("开始处理【推荐商品】的业务，参数：{}", id);
        updateRecommendById(id, RECOMMEND_STATE_ON);
    }

    @Override
    public void cancelRecommend(Long id) {
        log.debug("开始处理【取消推荐商品】的业务，参数：{}", id);
        updateRecommendById(id, RECOMMEND_STATE_OFF);
    }

    @Override
    public GoodsStandardVO getStandardById(Long id) {
        log.debug("开始处理【根据ID查询商品】的业务，参数：{}", id);
        GoodsStandardVO queryResult = goodsRepository.getStandardById(id);
        if (queryResult == null) {
            String message = "查询商品失败，商品数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }
        return queryResult;
    }

    @Override
    public PageData<GoodsListItemVO> list(Integer pageNum) {
        log.debug("开始处理【查询商品列表】的业务，页码：{}", pageNum);
        return goodsRepository.list(pageNum, defaultQueryPageSize);
    }

    @Override
    public PageData<GoodsListItemVO> list(Integer pageNum, Integer pageSize) {
        log.debug("开始处理【查询商品列表】的业务，页码：{}，每页记录数：{}", pageNum, pageSize);
        return goodsRepository.list(pageNum, pageSize);
    }

    @Override
    public PageData<GoodsListItemVO> listByCategory(Long categoryId, Integer pageNum) {
        log.debug("开始处理【根据类别查询商品列表】的业务，商品类别：{}, 页码：{}", categoryId, pageNum);
        return goodsRepository.listByCategory(categoryId, pageNum, defaultQueryPageSize);
    }

    @Override
    public PageData<GoodsListItemVO> listByCategory(Long categoryId, Integer pageNum, Integer pageSize) {
        log.debug("开始处理【根据类别查询商品列表】的业务，商品类别：{}, 页码：{}，每页记录数：{}", categoryId, pageNum, pageSize);
        return goodsRepository.listByCategory(categoryId, pageNum, pageSize);
    }

    @Override
    public void rebuildSearch() {
        log.debug("开始处理【重建商品的搜索数据】的业务");
        goodsSearchRepository.deleteAll();
        Integer pageNum = 1;
        Integer pageSize = 3;
        Integer maxPage;
        PageData<GoodsSearchVO> pageData;
        do {
            pageData = goodsRepository.listSearch(pageNum, pageSize);
            maxPage = pageData.getMaxPage();
            goodsSearchRepository.saveAll(pageData.getList());
            pageNum++;
        } while (pageNum <= maxPage);
    }

    private void updateCheckById(CurrentPrincipal currentPrincipal, Long id, Integer checkState, String remarks) {
        GoodsStandardVO currentGoods = goodsRepository.getStandardById(id);
        if (currentGoods == null) {
            String message = "将商品的审核状态修改为【" + CHECK_STATE_TEXT[checkState] + "】失败，商品数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        if (currentGoods.getCheckState().equals(checkState)) {
            String message = "将商品的审核状态修改为【" + CHECK_STATE_TEXT[checkState] + "】失败，此商品已经处于" + CHECK_STATE_TEXT[checkState] + "状态！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        int subStringEndIndex = currentGoods.getTitle().length() < BRIEF_MAX_LENGTH ? currentGoods.getTitle().length() : BRIEF_MAX_LENGTH;

        CheckLog checkLog = new CheckLog();
        checkLog.setResourceType(RESOURCE_TYPE_GOODS);
        checkLog.setResourceId(id);
        checkLog.setResourceBrief(currentGoods.getTitle().substring(0, subStringEndIndex));
        checkLog.setCheckUserId(currentPrincipal.getId());
        checkLog.setCheckUsername(currentPrincipal.getUsername());
        checkLog.setCheckRemarks(remarks);
        checkLog.setOriginalState(currentGoods.getCheckState());
        checkLog.setNewState(checkState);
        checkLog.setGmtCheck(LocalDateTime.now());
        int rows = checkLogRepository.insert(checkLog);
        if (rows != 1) {
            String message = "将商品的审核状态修改为【" + CHECK_STATE_TEXT[checkState] + "】失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }

        Goods updateGoods = new Goods();
        updateGoods.setId(id);
        updateGoods.setCheckState(checkState);
        rows = goodsRepository.update(updateGoods);
        if (rows != 1) {
            String message = "将商品的审核状态修改为【" + CHECK_STATE_TEXT[checkState] + "】失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    private void updatePutOnById(Long id, Integer isPutOn) {
        GoodsStandardVO currentGoods = goodsRepository.getStandardById(id);
        if (currentGoods == null) {
            String message = "将商品状态修改为【" + PUT_ON_STATE_TEXT[isPutOn] + "】失败，商品数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        if (currentGoods.getIsPutOn().equals(isPutOn)) {
            String message = "将商品状态修改为【" + PUT_ON_STATE_TEXT[isPutOn] + "】失败，此商品已经处于" + PUT_ON_STATE_TEXT[isPutOn] + "状态！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        Goods updateGoods = new Goods();
        updateGoods.setId(id);
        updateGoods.setIsPutOn(isPutOn);
        int rows = goodsRepository.update(updateGoods);
        if (rows != 1) {
            String message = "将商品状态修改为【" + PUT_ON_STATE_TEXT[isPutOn] + "】失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    private void updateRecommendById(Long id, Integer isRecommend) {
        GoodsStandardVO currentGoods = goodsRepository.getStandardById(id);
        if (currentGoods == null) {
            String message = "将商品推荐状态修改为【" + RECOMMEND_STATE_TEXT[isRecommend] + "】失败，商品数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        if (currentGoods.getIsRecommend().equals(isRecommend)) {
            String message = "将商品推荐状态修改为【" + RECOMMEND_STATE_TEXT[isRecommend] + "】失败，此商品已经处于" + RECOMMEND_STATE_TEXT[isRecommend] + "状态！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        Goods updateGoods = new Goods();
        updateGoods.setId(id);
        updateGoods.setIsRecommend(isRecommend);
        int rows = goodsRepository.update(updateGoods);
        if (rows != 1) {
            String message = "将商品推荐状态修改为【" + RECOMMEND_STATE_TEXT[isRecommend] + "】失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

}
