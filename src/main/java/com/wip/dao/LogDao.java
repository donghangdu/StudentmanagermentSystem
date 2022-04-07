
package com.wip.dao;


import com.wip.model.LogDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 */
@Mapper
public interface LogDao {

    /**
     *
     * @param logDomain
     * @return
     */
    int addLog(LogDomain logDomain);

    /**
     *
     * @return
     */
    List<LogDomain> getLogs();
}
