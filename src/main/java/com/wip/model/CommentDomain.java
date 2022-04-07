
package com.wip.model;

/**
 *
 */
public class CommentDomain {
    /**
     *
     */
    private Integer coid;
    /**
     *
     */
    private Integer cid;
    /**
     *  unix时间戳
     */
    private Integer created;
    /**
     *
     */
    private String author;
    /**
     *
     */
    private String authorId;
    /**
     *
     */
    private Integer ownerId;
    /**
     *
     */
    private String mail;
    /**
     *
     */
    private String url;
    /**
     *
     *
     */
    private String ip;
    /**
     *
     */
    private String agent;
    /**
     *
     */
    private String type;
    /**
     * Comment status
     */
    private String status;
    /**
     *
     */
    private Integer parent;
    /**
     * Comment content
     */
    private String content;

    public Integer getCoid() {
        return coid;
    }

    public void setCoid(Integer coid) {
        this.coid = coid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getEmail() {
        return mail;
    }

    public void setEmail(String email) {
        this.mail = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
