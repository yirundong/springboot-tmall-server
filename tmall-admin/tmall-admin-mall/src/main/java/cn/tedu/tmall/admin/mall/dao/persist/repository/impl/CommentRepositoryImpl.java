package cn.tedu.tmall.admin.mall.dao.persist.repository.impl;

import cn.tedu.tmall.admin.mall.dao.persist.mapper.CommentMapper;
import cn.tedu.tmall.admin.mall.dao.persist.repository.ICommentRepository;
import cn.tedu.tmall.admin.mall.pojo.entity.Comment;
import cn.tedu.tmall.admin.mall.pojo.vo.CommentListItemVO;
import cn.tedu.tmall.admin.mall.pojo.vo.CommentStandardVO;
import cn.tedu.tmall.common.pojo.vo.PageData;
import cn.tedu.tmall.common.util.PageInfoToPageDataConverter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理商品评论数据的存储库实现类
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 19:55
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
    public int deleteByGoods(Long goodsId) {
        log.debug("开始执行【根据商品ID删除评论】的数据访问，参数：{}", goodsId);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", goodsId);
        return commentMapper.delete(queryWrapper);
    }

    @Override
    public int update(Comment comment) {
        log.debug("开始执行【更新评论】的数据访问，参数：{}", comment);
        return commentMapper.updateById(comment);
    }

    @Override
    public CommentStandardVO getStandardById(Long id) {
        log.debug("开始执行【根据ID查询评论】的数据访问，参数：{}", id);
        return commentMapper.getStandardById(id);
    }

    @Override
    public PageData<CommentListItemVO> list(Integer pageNum, Integer pageSize) {
        log.debug("开始执行【查询评论列表】的数据访问，页码：{}，每页记录数：{}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<CommentListItemVO> list = commentMapper.list();
        PageInfo<CommentListItemVO> pageInfo = new PageInfo<>(list);
        return PageInfoToPageDataConverter.convert(pageInfo);
    }

}
