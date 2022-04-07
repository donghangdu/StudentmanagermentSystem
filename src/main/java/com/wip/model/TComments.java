package com.wip.model;

import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "t_comments")
public class TComments {
    @Id
    @Column(name = "coid")
    private Integer coid;

    @Column(name = "cid")
    private Integer cid;

    @Column(name = "created")
    private Integer created;

    @Column(name = "author")
    private String author;

    @Column(name = "authorId")
    private Integer authorid;

    @Column(name = "ownerId")
    private Integer ownerid;

    @Column(name = "mail")
    private String mail;

    @Column(name = "url")
    private String url;

    @Column(name = "ip")
    private String ip;

    @Column(name = "agent")
    private String agent;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "parent")
    private Integer parent;

    @Column(name = "content")
    private String content;
}