package com.wip.model;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "test_correct_records")
public class TestCorrectRecords {
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * testid
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
     * 1Corrected
     */
    @Column(name = "correct_status")
    private Integer correctStatus;

    /**
     * Correction time
     */
    @Column(name = "correct_time")
    private Date correctTime;

    /**
     * Reviserid
     */
    @Column(name = "correct_creator")
    private Integer correctCreator;
}