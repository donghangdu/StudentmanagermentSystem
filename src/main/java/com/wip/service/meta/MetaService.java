
package com.wip.service.meta;

import com.wip.dto.MetaDto;
import com.wip.dto.cond.MetaCond;
import com.wip.model.MetaDomain;

import java.util.List;

/**
 *
 */
public interface MetaService {

    /**
     *
     * @param type
     * @param name
     * @param mid
     */
    void saveMeta(String type, String name, Integer mid);

    /**
     *
     * @param type
     * @param orderBy
     * @param limit
     * @return
     */
    List<MetaDto> getMetaList(String type, String orderBy, int limit);

    /**
     *
     * @param cid
     * @param name
     * @param type
     */
    void saveOrUpdate(Integer cid, String name, String type);

    /**
     * Batch addition
     * @param cid
     * @param names
     * @param type
     */
    void addMetas(Integer cid, String names, String type);

    /**
     *
     * @param mid
     */
    void deleteMetaById(Integer mid);

    /**
     *
     * @param metaCond
     * @return
     */
    List<MetaDomain> getMetas(MetaCond metaCond);

    /**
     *
     * @param meta
     */
    void addMea(MetaDomain meta);

    /**
     *
     * @param meta
     */
    void updateMeta(MetaDomain meta);

    /**
     *
     * @param type
     * @return
     */
    Long getMetasCountByType(String type);

    /**
     *
     * @param type
     * @param name
     * @return
     */
    MetaDomain getMetaByName(String type, String name);
}
