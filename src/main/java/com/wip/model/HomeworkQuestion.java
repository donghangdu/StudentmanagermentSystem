package com.wip.model;

import java.util.Date;
import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "homework_question")
public class HomeworkQuestion {
    @ApiModelProperty("id")
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
    @ApiModelProperty("name")
    @Column(name = "name")
    private String name;

    /**
     * Question type（ 2Shortanswerquestions）
     */
    @ApiModelProperty(" Question type（type 2Shortanswerquestions）")
    @Column(name = "type")
    private Integer type;

    /**
     * Question answer (single choice)
     */
    @ApiModelProperty(" ()")
    @Column(name = "answer")
    private String answer;

    /**
     * Correct score
     */
    @ApiModelProperty("Correct score")
    @Column(name = "score")
    private Integer score;

    /**
     *
     */
    @ApiModelProperty("a")
    @Column(name = "a")
    private String a;

    /**
     *
     */
    @ApiModelProperty("b")
    @Column(name = "b")
    private String b;

    /**
     *
     */
    @ApiModelProperty("c")
    @Column(name = "c")
    private String c;

    /**
     * d
     */
    @ApiModelProperty("d")
    @Column(name = "d")
    private String d;

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
     * Title number
     */
    @ApiModelProperty("Title number")
    @Column(name = "serial_num")
    private Integer serialNum;
}