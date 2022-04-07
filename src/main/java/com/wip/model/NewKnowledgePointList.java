package com.wip.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Blob;
import java.util.Date;

@Data
@Accessors(chain = true)
@Table(name = "t_contents")
public class NewKnowledgePointList {
    @Id
    @Column(name = "cid")
    private Integer cid;

    /**
     * name
     */
    @Column(name = "classid")
    private Integer classid;

    @Column(name = "classname")
    private String classname;

    /**
     */
    @Column(name = "title")
    private String title;
    /**
     */
    @Column(name = "titlePic")
    private String titlePic;
    /**
     */
    @Column(name = "created")
    private String created;

    @Column(name = "createdtime")
    private String createdtime;

    @Column(name = "modified")
    private Integer modified;

    @Column(name = "content")
    private String content;

    @Column(name = "authorId")
    private Integer authorId;

    @Column(name = "authorName")
    private String authorName;

    /**
     */
    @Column(name = "type")
    private String type;
    /**
     */
    @Column(name = "status")
    private String status;
    /**
     */
    @Column(name = "tags")
    private String tags;
    /**
     */
    @Column(name = "categories")
    private String categories;

    @Column(name = "hits")
    private Integer hits;

    @Column(name = "commentsNum")
    private Integer commentsNum;

    @Column(name = "allowComment")
    private Integer allowComment;
}
