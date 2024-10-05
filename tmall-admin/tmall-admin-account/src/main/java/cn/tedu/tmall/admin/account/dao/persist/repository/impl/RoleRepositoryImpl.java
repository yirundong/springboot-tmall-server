package cn.tedu.tmall.admin.account.dao.persist.repository.impl;

import cn.tedu.tmall.admin.account.dao.persist.mapper.RoleMapper;
import cn.tedu.tmall.admin.account.dao.persist.repository.IRoleRepository;
import cn.tedu.tmall.admin.account.pojo.vo.RoleListItemVO;
import cn.tedu.tmall.common.pojo.vo.PageData;
import cn.tedu.tmall.common.util.PageInfoToPageDataConverter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理角色数据的数据访问实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Repository
public class RoleRepositoryImpl implements IRoleRepository {

    @Autowired
    private RoleMapper roleMapper;

    public RoleRepositoryImpl() {
        log.debug("创建存储库对象：RoleRepositoryImpl");
    }

    @Override
    public PageData<RoleListItemVO> list(Integer pageNum, Integer pageSize) {
        log.debug("开始执行【查询用户列表】的数据访问，页码：{}，每页记录数：{}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<RoleListItemVO> list = roleMapper.list();
        PageInfo<RoleListItemVO> pageInfo = new PageInfo<>(list);
        return PageInfoToPageDataConverter.convert(pageInfo);
    }

}
