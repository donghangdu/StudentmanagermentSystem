package com.wip.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@Accessors(chain = true)
@Table(name = "test_message")
public class MessageClass {
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     *
     */
    @Column(name = "content")
    private String content;

    /**
     *
     */
    @Column(name = "userid")
    private String userid;

    /**
     *
     */
    @Column(name = "time")
    private Date time;

    /**
     *
     */
    @Column(name = "parentid")
    private Integer parentid;

    @Column(name = "classid")
    private Integer classid;

    /**
     *
     */
    @Column(name = "username")
    private String username;

    @Column(name = "replyid")
    private Integer replyid;

    @Column(name = "replyname")
    private String replyname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getClassid() {
        return classid;
    }

    public void setClassid(Integer classid) {
        this.classid = classid;
    }

    public Integer getReplyid() {
        return replyid;
    }

    public void setReplyid(Integer replyid) {
        this.replyid = replyid;
    }

    public String getReplyname() {
        return replyname;
    }

    public void setReplyname(String replyname) {
        this.replyname = replyname;
    }

    @Override
    public String toString() {
        return "MessageClass{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", userid='" + userid + '\'' +
                ", time='" + time + '\'' +
                ", parentid='" + parentid + '\'' +
                ", username='" + username + '\'' +
                ", classid='" + classid + '\'' +
                ", replyid='" + replyid + '\'' +
                ", replyname='" + replyname + '\'' +
                '}';
    }
}
