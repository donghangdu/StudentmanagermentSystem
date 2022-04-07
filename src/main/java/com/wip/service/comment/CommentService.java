
package com.wip.service.comment;

import com.github.pagehelper.PageInfo;
import com.wip.dto.cond.CommentCond;
import com.wip.model.CommentDomain;

import java.util.List;

public interface CommentService {

    /**
     *
     * @param comments
     */
    void addComment(CommentDomain comments);

    /**
     *
     * @param cid
     * @return
     */
    List<CommentDomain> getCommentsByCId(Integer cid);

    /**
     *
     * @param commentCond
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<CommentDomain> getCommentsByCond(CommentCond commentCond, int pageNum, int pageSize);

    /**
     *
     * @param coid
     * @return
     */
    CommentDomain getCommentById(Integer coid);

    /**
     *
     * @param coid
     * @param status
     */
    void updateCommentStatus(Integer coid, String status);
}
