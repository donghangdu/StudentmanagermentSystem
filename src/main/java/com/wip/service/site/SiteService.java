
package com.wip.service.site;

import com.wip.dto.StatisticsDto;
import com.wip.model.CommentDomain;
import com.wip.model.ContentDomain;

import java.util.List;

/**
 *
 */
public interface SiteService {

    /**
     *
     * @param limit
     * @return
     */
    List<CommentDomain> getComments(int limit);

    /**
     *
     * @param limit
     * @return
     */
    List<ContentDomain> getNewArticles(int limit);

    /**
     *
     * @return
     */
    StatisticsDto getStatistics();
}
