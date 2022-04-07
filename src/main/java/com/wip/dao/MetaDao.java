
package com.wip.dao;

import com.wip.dto.MetaDto;
import com.wip.dto.cond.MetaCond;
import com.wip.model.MetaDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Mapper
public interface MetaDao {

    /**
     *
     * @param meta
     * @return
     */
    int addMeta(MetaDomain meta);

    /**
     *
     * @param meta
     * @return
     */
    int updateMeta(MetaDomain meta);

    /**
     *
     * @param metaCond
     * @return
     */
    List<MetaDomain> getMetasByCond(MetaCond metaCond);

    /**
     *
     * @param mid
     * @return
     */
    MetaDomain getMetaById(@Param("mid") Integer mid);

    /**
     *
     * @param parMap
     * @return
     */
    List<MetaDto> selectFromSql(Map<String, Object> parMap);

    /**
     *
     * @param mid
     */
    void deleteMetaById(Integer mid);

    /**
     *
     * @param type
     * @return
     */
    Long getMetasCountByType(@Param("type") String type);

    /**
     *
     * @param type
     * @param name
     * @return
     */
    MetaDomain getMetaByName(@Param("type") String type, @Param("name") String name);
}
