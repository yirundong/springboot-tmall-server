package cn.tedu.tmall.admin.account.service;

import cn.tedu.tmall.admin.account.pojo.vo.RoleListItemVO;
import cn.tedu.tmall.common.pojo.vo.PageData;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理角色数据的业务接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Transactional
public interface IRoleService {

    /**
     * 查询角色列表，将使用默认的每页记录数
     *
     * @param pageNum  页码
     * @return 角色列表
     */
    PageData<RoleListItemVO> list(Integer pageNum);

    /**
     * 查询角色列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 角色列表
     */
    PageData<RoleListItemVO> list(Integer pageNum, Integer pageSize);

}
