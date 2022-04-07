package com.wip.dao;

import com.wip.model.HomeworkCorrectRecords;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface HomeworkCorrectRecordsMapper extends Mapper<HomeworkCorrectRecords> {
	List<Map<String,Object>> selectHomeworkCorrectRecordsAndUserList(HashMap<String, Object> testPaperId);

	List<Map<String, Object>> selectUserRecordAndQuestion(Integer id);
}