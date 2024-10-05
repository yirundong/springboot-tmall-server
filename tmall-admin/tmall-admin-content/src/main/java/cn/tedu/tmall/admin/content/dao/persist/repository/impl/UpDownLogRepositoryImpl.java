package cn.tedu.tmall.admin.content.dao.persist.repository.impl;

import cn.tedu.tmall.admin.content.dao.persist.mapper.UpDownLogMapper;
import cn.tedu.tmall.admin.content.dao.persist.repository.IUpDownLogRepository;
import cn.tedu.tmall.admin.content.pojo.entity.UpDownLog;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 处理顶踩日志数据的存储库实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Repository
public class UpDownLogRepositoryImpl implements IUpDownLogRepository {

    @Autowired
    private UpDownLogMapper upDownLogMapper;

    public UpDownLogRepositoryImpl() {
        log.info("创建存储库对象：UpDownLogRepositoryImpl");
    }

    @Override
    public int deleteByResource(Integer resourceType, Long resourceId) {
        log.debug("开始执行【根据资源删除顶踩记录】的数据访问，资源类型：{}，资源ID：{}", resourceType, resourceId);
        QueryWrapper<UpDownLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resource_type", resourceType);
        queryWrapper.eq("resource_id", resourceId);
        return upDownLogMapper.delete(queryWrapper);
    }

}
