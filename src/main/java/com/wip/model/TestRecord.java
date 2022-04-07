package com.wip.model;

import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "test_record")
public class TestRecord {
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * testid
     */
    @ApiModelProperty("testid")
    @Column(name = "test_paper_id")
    private Integer testPaperId;

    /**
     *
     */
    @ApiModelProperty("testQuestionId")
    @Column(name = "test_question_id")
    private Integer testQuestionId;

    /**
     *
     */
    @ApiModelProperty("standard_answer")
    @Column(name = "standard_answer")
    private String standardAnswer;

    /**
     *
     */
    @ApiModelProperty("answer")
    @Column(name = "answer")
    private String answer;

    /**
     *
     */
    @Column(name = "creator")
    private Integer creator;

    /**
     * socre
     */
    @ApiModelProperty("score")
    @Column(name = "score")
    private Integer score;
}