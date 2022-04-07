
package com.wip.model;

import java.io.Serializable;

/**
 *
 */
public class MetaDomain implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Integer mid;
    /**
     *
     */
    private String name;

    /**
     *
     */
    private String slug;

    /**
     *
     */
    private String type;

    /**
     *
     */
    private String contentType;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private Integer sort;

    private Integer parent;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }
}
