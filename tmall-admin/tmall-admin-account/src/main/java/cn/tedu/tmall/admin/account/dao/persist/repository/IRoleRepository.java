package cn.tedu.tmall.admin.account.dao.persist.repository;

import cn.tedu.tmall.admin.account.pojo.vo.RoleListItemVO;
import cn.tedu.tmall.common.pojo.vo.PageData;

/**
 * 处理角色数据的数据访问接口
 *
 * @author YiRunDong
 * @version 2.0
 */
public interface IRoleRepository {

    /**
     * 查询角色列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 角色列表
     */
    PageData<RoleListItemVO> list(Integer pageNum, Integer pageSize);

}
