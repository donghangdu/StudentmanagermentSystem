package com.wip.constant;

import com.wip.model.ContentDomain;

public enum Types {

    //
    CATEGORY("category"),
    //
    TAG("tag"),
    //
    ARTICLE("post"),
    // csrf_token
    CSRF_TOKEN("csrf_token"),
    //
    LINK("link"),
    //
    COMMENTS_FREQUENCY("comments:frequency"),
    //
    IMAGE("image"),
    //
    FILE("file"),
    /**
     *
     */
    // ATTACH_URL("attach_url");
    ATTACH_URL("http://pb84kab39.bkt.clouddn.com/");

    private String type;

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    Types(java.lang.String type) {
        this.type = type;
    }
}

