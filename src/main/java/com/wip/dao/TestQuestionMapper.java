package com.wip.dao;

import com.wip.model.TestQuestion;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TestQuestionMapper extends Mapper<TestQuestion> {
    List<Map<String,Object>> selectTestQuestionAndUserList(HashMap<String, Object> testPaperId);
}