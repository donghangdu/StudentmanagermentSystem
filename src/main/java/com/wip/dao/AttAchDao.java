
package com.wip.dao;

import com.wip.dto.AttAchDto;
import com.wip.model.AttAchDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
@Mapper
public interface AttAchDao {

    /**
     *
     * @param attAchDomain
     */
    void addAttAch(AttAchDomain attAchDomain);

    /**
     *
     * @return
     */
    List<AttAchDto> getAtts();

    /**
     *
     * @return
     */
    Long getAttAchCount();

    /**
     *
     * @param id
     * @return
     */
    AttAchDto getAttAchById(@Param("id") Integer id);

    /**
     *
     * @param id
     */
    void deleteAttAch(@Param("id") Integer id);
}
