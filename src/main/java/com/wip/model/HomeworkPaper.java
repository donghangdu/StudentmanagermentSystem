package com.wip.model;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "homework_paper")
public class HomeworkPaper {
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * Assignment name
     */
    @Column(name = "name")
    private String name;

    /**
     * Creation time
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     *
     */
    @Column(name = "creator")
    private Integer creator;

    /**
     * 1
     */
    @Column(name = "state")
    private Integer state;
}