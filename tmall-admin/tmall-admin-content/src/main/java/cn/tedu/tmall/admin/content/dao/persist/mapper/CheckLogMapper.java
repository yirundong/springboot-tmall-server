package cn.tedu.tmall.admin.content.dao.persist.mapper;

import cn.tedu.tmall.admin.content.pojo.entity.CheckLog;
import cn.tedu.tmall.admin.content.pojo.vo.CheckLogListItemVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理审核日志数据的Mapper接口
 *
 * @author YiRunDong
 * @version 2.0
 */
@Repository
public interface CheckLogMapper extends BaseMapper<CheckLog> {

    /**
     * 根据资源类型查询审核日志列表
     *
     * @param resourceType 资源类型
     * @return 审核日志列表
     * @see cn.tedu.tmall.common.consts.data.ContentConsts
     */
    List<CheckLogListItemVO> listByResourceType(int resourceType);

}
