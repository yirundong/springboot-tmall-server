package cn.tedu.tmall.admin.mall.dao.persist.repository.impl;

import cn.tedu.tmall.admin.mall.dao.persist.mapper.CheckLogMapper;
import cn.tedu.tmall.admin.mall.dao.persist.repository.ICheckLogRepository;
import cn.tedu.tmall.admin.mall.pojo.entity.CheckLog;
import cn.tedu.tmall.admin.mall.pojo.vo.CheckLogListItemVO;
import cn.tedu.tmall.common.pojo.vo.PageData;
import cn.tedu.tmall.common.util.PageInfoToPageDataConverter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理审核日志数据的存储库实现类
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 19:55
 */
@Slf4j
@Repository
public class CheckLogRepositoryImpl implements ICheckLogRepository {

    @Autowired
    private CheckLogMapper checkLogMapper;

    public CheckLogRepositoryImpl() {
        log.info("创建存储库对象：CheckLogRepositoryImpl");
    }

    @Override
    public int insert(CheckLog checkLog) {
        log.debug("开始执行【插入审核日志数据】的数据访问，参数：{}", checkLog);
        return checkLogMapper.insert(checkLog);
    }

    @Override
    public int deleteByResource(Integer resourceType, Long resourceId) {
        log.debug("开始执行【根据资源删除审核日志】的数据访问，资源类型：{}，资源ID：{}", resourceType, resourceId);
        QueryWrapper<CheckLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resource_type", resourceType);
        queryWrapper.eq("resource_Id", resourceId);
        return checkLogMapper.delete(queryWrapper);
    }

    @Override
    public PageData<CheckLogListItemVO> listByResourceType(int resourceType, Integer pageNum, Integer pageSize) {
        log.debug("开始执行【查询审核日志列表】的数据访问，页码：{}，每页记录数：{}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<CheckLogListItemVO> list = checkLogMapper.listByResourceType(resourceType);
        PageInfo<CheckLogListItemVO> pageInfo = new PageInfo<>(list);
        return PageInfoToPageDataConverter.convert(pageInfo);
    }

}
