
package com.wip.service.comment.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.constant.ErrorConstant;
import com.wip.dao.CommentDao;
import com.wip.dto.cond.CommentCond;
import com.wip.exception.BusinessException;
import com.wip.model.CommentDomain;
import com.wip.model.ContentDomain;
import com.wip.service.article.ContentService;
import com.wip.service.comment.CommentService;
import com.wip.utils.DateKit;
import com.wip.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentDao commentDao;

    @Resource
    private ContentService contentService;

    private static final Map<String,String> STATUS_MAP = new ConcurrentHashMap<>();

    /**
     * Comment status：normal
     */
    private static final String STATUS_NORMAL = "approved";
    /**
     * Comment status：no shown
     */
    private static final String STATUS_BLANK = "not_audit";

    static {
        STATUS_MAP.put("approved",STATUS_NORMAL);
        STATUS_MAP.put("not_audit",STATUS_BLANK);
    }


    @Override
    @Transactional
    @CacheEvict(value = "commentCache", allEntries = true)
    public void addComment(CommentDomain comments) {
        String msg = null;

        if (null == comments) {
            msg = "comment object is empty";
        }

        if (StringUtils.isBlank(comments.getAuthor())) {
            comments.setAuthor("Mr.somebody");
        }
        if (StringUtils.isNotBlank(comments.getEmail()) && !TaleUtils.isEmail(comments.getEmail())) {
            msg =  "Please enter the correct email format";
        }
        if (StringUtils.isBlank(comments.getContent())) {
            msg = "Comment content cannot be empty";
        }
        if (comments.getContent().length() < 5 || comments.getContent().length() > 2000) {
            msg = "The number of comments need to be  within 5-2000 characters";
        }
        if (null == comments.getCid()) {
            msg = "Article cannot be empty";
        }
        if (msg != null)
            throw BusinessException.withErrorCode(msg);

        ContentDomain article = contentService.getArticleById(comments.getCid());
        if (null == article) {
            throw BusinessException.withErrorCode("The article does not exist");
        }

        comments.setOwnerId(article.getAuthorId());
        comments.setStatus(STATUS_MAP.get(STATUS_BLANK));
        comments.setCreated(DateKit.getCurrentUnixTime());
        commentDao.addComment(comments);

        ContentDomain temp = new ContentDomain();
        temp.setCid(article.getCid());
        Integer count = article.getCommentsNum();
        if (null == count) {
            count = 0;
        }
        temp.setCommentsNum(count + 1);
        contentService.updateContentByCid(temp);

    }

    @Override
    @Cacheable(value = "commentCache", key = "'commentsByCId_' + #p0")
    public List<CommentDomain> getCommentsByCId(Integer cid) {
        if (null == cid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return commentDao.getCommentByCId(cid);
    }

    @Override
    @Cacheable(value = "commentCache", key = "'commentsByCond_'+ #p1")
    public PageInfo<CommentDomain> getCommentsByCond(CommentCond commentCond, int pageNum, int pageSize) {
        if (null == commentCond)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        PageHelper.startPage(pageNum,pageSize);
        List<CommentDomain> comments = commentDao.getCommentsByCond(commentCond);
        PageInfo<CommentDomain> pageInfo = new PageInfo<>(comments);
        return pageInfo;
    }

    @Override
    @Cacheable(value = "commentCache",key = "'commentById_' + #p0")
    public CommentDomain getCommentById(Integer coid) {
        if (null == coid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return commentDao.getCommentById(coid);
    }

    @Override
    @CacheEvict(value = "commentCache", allEntries = true)
    public void updateCommentStatus(Integer coid, String status) {
        if (null == coid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        commentDao.updateCommentStatus(coid, status);
    }
}
