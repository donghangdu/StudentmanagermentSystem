package com.wip.dao;

import com.wip.model.HomeworkQuestion;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface HomeworkQuestionMapper extends Mapper<HomeworkQuestion> {
	List<Map<String,Object>> selectHomeworkQuestionAndUserList(HashMap<String, Object> testPaperId);

	List<Map<String,Object>> selectQuestionAndUserAns(@Param("uid") Integer uid, @Param("testPaperId") Integer testPaperId);
}