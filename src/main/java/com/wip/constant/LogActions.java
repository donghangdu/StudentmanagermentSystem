package com.wip.constant;


public enum LogActions {

    LOGIN("backstage Log in"),
    UP_PWD("Edit PassWord"),
    UP_INFO("Edit User Info"),
    DEL_ARTICLE("Delete Article"),
    SYS_SETTING("Save System settings"),
    DEL_ATTACH("Delete File");

    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    LogActions(String action) {
        this.action = action;
    }

}
