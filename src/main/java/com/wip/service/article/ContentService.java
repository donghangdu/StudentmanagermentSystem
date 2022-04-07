
package com.wip.service.article;

import com.github.pagehelper.PageInfo;
import com.wip.dto.cond.ContentCond;
import com.wip.model.ContentDomain;
import com.wip.model.MetaDomain;

import java.util.List;

/**
 *
 */
public interface ContentService {

    /***
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
     * @param page
     * @param limit
     * @return
     */
    PageInfo<ContentDomain> getArticlesByCond(ContentCond contentCond, int page, int limit);

    /**
     *
     * @param cid
     */
    void deleteArticleById(Integer cid);

    /**
     *
     * @param content
     */
    void updateContentByCid(ContentDomain content);

    /**
     *
     * @param category
     * @return
     */
    List<ContentDomain> getArticleByCategory(String category);

    /**
     *
     * @param tags
     * @return
     */
    List<ContentDomain> getArticleByTags(MetaDomain tags);
}
