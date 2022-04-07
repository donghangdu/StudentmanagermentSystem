

package com.wip.dao;

import com.wip.dto.cond.CommentCond;
import com.wip.model.CommentDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface CommentDao {

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
    List<CommentDomain> getCommentByCId(@Param("cid") Integer cid);


    /**
     *
     * @param coid
     */
    void deleteComment(@Param("coid") Integer coid);

    /**
     *
     * @return
     */
    Long getCommentCount();

    /**
     *
     * @param commentCond
     * @return
     */
    List<CommentDomain> getCommentsByCond(CommentCond commentCond);

    /**
     *
     * @param coid
     * @return
     */
    CommentDomain getCommentById(@Param("coid") Integer coid);

    /**
     *
     * @param coid
     * @param status
     */
    void updateCommentStatus(@Param("coid") Integer coid, @Param("status") String status);
}
