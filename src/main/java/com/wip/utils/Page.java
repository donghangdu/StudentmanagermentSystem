package com.wip.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Paging objects
 */
@Data
public class Page<T> implements Serializable {


    /**
     * Current page
     */
    private Integer currentPage = 1;

    /**
     * Number of items per page
     */
    private Integer pageSize = 10;

    /**
     * PageCount
     */
    private Integer totalPage = 0;

    /**
     * Total number
     */
    private Integer totalCount = 0;

    /**
     * data
     */
    private List<T> list;

    /**
     * Condition parameter
     */
    private Map<String, Object> params = new HashMap<>(16);

    /**
     * Sort column
     */
    private String sortColumn;

    /**
     * sort order
     */
    private String sortMethod = "asc";

    /**
     *
     *
     * Get current page
     */
    public Integer getCurrentPage() {
        if (currentPage < 1) {
            return 1;
        }
        return this.currentPage;
    }


    /**
     * Get index
     *
     * @return
     */
    public Integer getIndex() {
        return (currentPage - 1) * pageSize;
    }

    /**
     * Calculate the total number of pages when setting the total number of pages
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        this.totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
    }


}
