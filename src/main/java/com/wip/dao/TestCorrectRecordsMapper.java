package com.wip.dao;

import com.wip.model.TestCorrectRecords;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TestCorrectRecordsMapper extends Mapper<TestCorrectRecords> {
    List<Map<String,Object>> selectTestCorrectRecordsAndUserList(HashMap<String, Object> testPaperId);

    List<Map<String, Object>> selectUserRecordAndQuestion(Integer id);
}