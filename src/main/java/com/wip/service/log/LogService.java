
package com.wip.service.log;


import com.github.pagehelper.PageInfo;
import com.wip.model.LogDomain;

/**
 *
 */
public interface LogService {

    /**
     *
     * @param action
     * @param data
     * @param ip
     * @param authorId
     */
    void addLog(String action, String data, String ip, Integer authorId);

    /**
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<LogDomain> getLogs(int pageNum, int pageSize);
}
