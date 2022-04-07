package com.wip.dao;

import com.wip.model.NewKnowledgePointList;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


public interface NewknowledgePointPaperMapper extends Mapper<NewKnowledgePointList> {
    List<Map<String,Object>> selectNewKnowledgePointList(Integer userid);
}
