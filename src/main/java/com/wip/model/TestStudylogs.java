package com.wip.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@Table(name = "test_studylogs")
public class TestStudylogs {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "uid")
    private Integer uid;

    @Column(name = "articleid")
    private Integer articleid;

    @Column(name = "percent")
    private Integer percent;
}