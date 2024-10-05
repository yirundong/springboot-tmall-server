package cn.tedu.tmall.admin.mall.dao.persist.repository;

import cn.tedu.tmall.admin.mall.pojo.entity.CheckLog;
import cn.tedu.tmall.admin.mall.pojo.vo.CheckLogListItemVO;
import cn.tedu.tmall.common.pojo.vo.PageData;

/**
 * 处理审核日志数据的存储库接口
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 19:55
 */
public interface ICheckLogRepository {

    /**
     * 插入审核日志数据
     *
     * @param checkLog 审核日志数据
     * @return 受影响的行数
     */
    int insert(CheckLog checkLog);

    /**
     * 根据资源删除审核日志
     *
     * @param resourceType 资源类型
     * @param resourceId   资源ID
     * @return 受影响的行数
     */
    int deleteByResource(Integer resourceType, Long resourceId);

    /**
     * 查询审核日志列表
     *
     * @param resourceType 资源类型
     * @param pageNum      页码
     * @param pageSize     每页记录数
     * @return 审核日志列表
     * @see cn.tedu.tmall.common.consts.data.ContentConsts
     */
    PageData<CheckLogListItemVO> listByResourceType(int resourceType, Integer pageNum, Integer pageSize);

}
