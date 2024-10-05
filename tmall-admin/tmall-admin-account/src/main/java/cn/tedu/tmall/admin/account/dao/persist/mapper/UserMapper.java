package cn.tedu.tmall.admin.account.dao.persist.mapper;

import cn.tedu.tmall.admin.account.pojo.entity.User;
import cn.tedu.tmall.admin.account.pojo.vo.UserListItemVO;
import cn.tedu.tmall.admin.account.pojo.vo.UserStandardVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理用户数据的Mapper接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户ID查询用户数据详情
     *
     * @param id 用户ID
     * @return 匹配的用户数据详情，如果没有匹配的数据，则返回null
     */
    UserStandardVO getStandardById(Long id);

    /**
     * 查询用户数据列表
     *
     * @return 用户数据列表
     */
    List<UserListItemVO> list();

}
