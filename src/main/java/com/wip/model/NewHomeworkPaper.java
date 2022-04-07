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
@Table(name = "newhomework_record")
public class NewHomeworkPaper {
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * Assignment name
     */
    @Column(name = "classid")
    private Integer classid;

    /**
     * Creation time
     */
    @Column(name = "time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    /**
     */
    @Column(name = "authorid")
    private Integer authorid;

    /**
     */
    @Column(name = "authorname")
    private String authorname;
    /**
     */
    @Column(name = "content")
    private String content;
    /**
     */
    @Column(name = "status")
    private String status;
    /**
     */
    @Column(name = "classname")
    private String classname;
    /**
     */
    @Column(name = "workname")
    private String workname;
    /**
     */
    @Column(name = "filename")
    private String filename;
    /**
     */
    @Column(name = "file")
    private String file;
}
