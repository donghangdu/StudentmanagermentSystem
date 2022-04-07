
package com.wip.dao;

import com.wip.dto.cond.ContentCond;
import com.wip.model.ContentDomain;
import com.wip.model.RelationShipDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
@Mapper
public interface ContentDao {

    /**
     *
     * @param contentDomain
     */
    void addArticle(ContentDomain contentDomain);

    /**
     *
     * @param cid
     * @return
     */
    ContentDomain getArticleById(Integer cid);

    /**
     *
     * @param contentDomain
     */
    void updateArticleById(ContentDomain contentDomain);

    /**
     *
     * @param contentCond
     * @return
     */
    List<ContentDomain> getArticleByCond(ContentCond contentCond);

    /**
     *
     * @param cid
     */
    void deleteArticleById(Integer cid);

    /**
     *
     * @return
     */
    Long getArticleCount();

    /**
     *
     * @param category
     * @return
     */
    List<ContentDomain> getArticleByCategory(@Param("category") String category);

    /**
     *
     * @param cid
     * @return
     */
    List<ContentDomain> getArticleByTags(List<RelationShipDomain> cid);
}
