package com.wip.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@Table(name = "t_contents")
public class TContents {
    @Id
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "title")
    private String title;

    @Column(name = "titlePic")
    private String titlepic;

    @Column(name = "slug")
    private String slug;

    @Column(name = "created")
    private Integer created;

    @Column(name = "modified")
    private Integer modified;

    @Column(name = "authorId")
    private Integer authorid;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "tags")
    private String tags;

    @Column(name = "categories")
    private String categories;

    @Column(name = "hits")
    private Integer hits;

    @Column(name = "commentsNum")
    private Integer commentsnum;

    @Column(name = "allowComment")
    private Boolean allowcomment;

    @Column(name = "allowPing")
    private Boolean allowping;

    @Column(name = "allowFeed")
    private Boolean allowfeed;

    /**
     * 内容文字
     */
    @Column(name = "content")
    private String content;
}