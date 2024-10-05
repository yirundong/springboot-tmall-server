package cn.tedu.tmall.admin.account.dao.persist.mapper;

import cn.tedu.tmall.admin.account.pojo.vo.RoleListItemVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理角色数据的Mapper接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Repository
public interface RoleMapper {

    /**
     * 查询角色列表
     *
     * @return 角色列表
     */
    List<RoleListItemVO> list();

}
