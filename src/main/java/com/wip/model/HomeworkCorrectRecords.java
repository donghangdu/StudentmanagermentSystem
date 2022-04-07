package com.wip.model;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "homework_correct_records")
public class HomeworkCorrectRecords {
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * Job ID
     */
    @Column(name = "test_paper_id")
    private Integer testPaperId;

    /**
     *
     */
    @Column(name = "creator")
    private Integer creator;

    /**
     *
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     *
     */
    @Column(name = "correct_status")
    private Integer correctStatus;

    /**
     * Correction time
     */
    @Column(name = "correct_time")
    private Date correctTime;

    /**
     * Reviser
     */
    @Column(name = "correct_creator")
    private Integer correctCreator;
}