package cn.tedu.tmall.admin.content.dao.persist.repository.impl;

import cn.tedu.tmall.admin.content.dao.persist.mapper.CommentMapper;
import cn.tedu.tmall.admin.content.dao.persist.repository.ICommentRepository;
import cn.tedu.tmall.admin.content.pojo.entity.Comment;
import cn.tedu.tmall.admin.content.pojo.vo.CommentListItemVO;
import cn.tedu.tmall.admin.content.pojo.vo.CommentStandardVO;
import cn.tedu.tmall.common.pojo.vo.PageData;
import cn.tedu.tmall.common.util.PageInfoToPageDataConverter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 处理评论数据的存储库实现类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Repository
public class CommentRepositoryImpl implements ICommentRepository {

    @Autowired
    private CommentMapper commentMapper;

    public CommentRepositoryImpl() {
        log.info("创建存储库对象：CommentRepositoryImpl");
    }

    @Override
    public int insert(Comment comment) {
        log.debug("开始执行【插入评论】的数据访问，参数：{}", comment);
        return commentMapper.insert(comment);
    }

    @Override
    public int deleteById(Long id) {
        log.debug("开始执行【根据ID删除评论】的数据访问，参数：{}", id);
        return commentMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Collection<Long> idList) {
        log.debug("开始执行【批量删除评论】的数据访问，参数：{}", idList);
        return commentMapper.deleteBatchIds(idList);
    }

    @Override
    public int deleteByResource(Integer resourceType, Long resourceId) {
        log.debug("开始执行【根据资源删除评论数据】的数据访问，资源类型：{}，资源ID：{}", resourceType, resourceId);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resource_type", resourceType)
                .eq("resource_id", resourceId);
        return commentMapper.delete(queryWrapper);
    }

    @Override
    public int update(Comment comment) {
        log.debug("开始执行【更新评论】的数据访问，参数：{}", comment);
        return commentMapper.updateById(comment);
    }

    @Override
    public int countByResource(Integer resourceType, Long resourceId) {
        log.debug("开始执行【根据资源统计评论表中的数据的数量】的数据访问，资源类型：{}，资源ID：{}", resourceType, resourceId);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resource_type", resourceType)
                .eq("resource_id", resourceId);
        return commentMapper.selectCount(queryWrapper);
    }

    @Override
    public CommentStandardVO getStandardById(Long id) {
        log.debug("开始执行【根据ID查询评论信息】的数据访问，参数：{}", id);
        return commentMapper.getStandardById(id);
    }

    @Override
    public PageData<CommentListItemVO> listByResourceType(Integer resourceType, Integer pageNum, Integer pageSize) {
        log.debug("开始执行【查询评论列表】的数据访问，页码：{}，每页记录数：{}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<CommentListItemVO> list = commentMapper.listByResourceType(resourceType);
        PageInfo<CommentListItemVO> pageInfo = new PageInfo<>(list);
        return PageInfoToPageDataConverter.convert(pageInfo);
    }

}
