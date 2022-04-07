package com.wip.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@Table(name = "test_class")
public class TestClass {
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     *
     */
    @Column(name = "grade")
    private String grade;

    /**
     *
     */
    @Column(name = "className")
    private String classname;

    /**
     *
     */
    @Column(name = "comment")
    private String comment;

    /**
     *
     */
    @Column(name = "status")
    private String status;

    /**
     *
     */
    @Column(name = "headImage")
    private String headimage;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }


    @Override
    public String toString() {
        return "TestClass{" +
                "id=" + id +
                ", grade='" + grade + '\'' +
                ", classname='" + classname + '\'' +
                ", comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                ", headimage='" + headimage + '\'' +
                '}';
    }
}