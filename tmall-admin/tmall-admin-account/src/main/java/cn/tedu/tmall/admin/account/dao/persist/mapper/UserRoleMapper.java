package cn.tedu.tmall.admin.account.dao.persist.mapper;

import cn.tedu.tmall.admin.account.pojo.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理用户与角色的关联数据的Mapper接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 批量插入用户与角色的关联数据
     *
     * @param userRoleList 若干个用户与角色的关联数据的集合
     * @return 受影响的行数
     */
    int insertBatch(List<UserRole> userRoleList);


}
