package cn.tedu.tmall.admin.content.dao.persist.repository;

/**
 * 处理顶踩日志数据的存储库接口
 *
 * @author YiRunDong
 * @version 2.0
 */
public interface IUpDownLogRepository {

    /**
     * 根据资源删除顶踩记录
     *
     * @param resourceType 资源类型
     * @param resourceId   资源ID
     * @return 受影响的行数
     */
    int deleteByResource(Integer resourceType, Long resourceId);

}
