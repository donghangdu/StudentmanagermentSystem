package com.wip.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Accessors(chain = true)
@Table(name = "newhomework_stu")
public class NewStuHomeworkPaper {
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * Assignment name
     */
    @Column(name = "workid")
    private Integer workid;

    /**
     * Creation time
     */
    @Column(name = "time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    @Column(name = "examinationtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date examinationtime;

    /**
     */
    @Column(name = "userid")
    private Integer userid;

    @Column(name = "score")
    private Integer score;

    /**
     */
    @Column(name = "state")
    private String state;
    /**
     */
    @Column(name = "filename")
    private String filename;
    /**
     */
    @Column(name = "file")
    private String file;
}
