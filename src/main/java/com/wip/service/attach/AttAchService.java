
package com.wip.service.attach;

import com.github.pagehelper.PageInfo;
import com.wip.dto.AttAchDto;
import com.wip.model.AttAchDomain;

/**
 *
 */
public interface AttAchService {

    /**
     *
     * @param attAchDomain
     */
    void addAttAch(AttAchDomain attAchDomain);

    /**
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<AttAchDto> getAtts(int pageNum, int pageSize);

    /**
     *
     * @param id
     * @return
     */
    AttAchDto getAttAchById(Integer id);

    /**
     *
     * @param id
     */
    void deleteAttAch(Integer id);
}
