package com.wip.dao;

import com.wip.model.HomeworkPaper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface HomeworkPaperMapper extends Mapper<HomeworkPaper> {
	List<Map<String,Object>> selectHomeworkPaperAndUserList(Integer userid);

    List<Map<String,Object>> selectHomeworkAndUserHomeWorkRecord(Integer uid);
}