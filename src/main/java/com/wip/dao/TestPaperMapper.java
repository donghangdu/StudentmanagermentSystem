package com.wip.dao;

import com.wip.model.TestPaper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TestPaperMapper extends Mapper<TestPaper> {

    List<Map<String,Object>> selectTestPaperAndUserList(Integer userid);

	List<Map<String,Object>> selectTestPaperAndUserTestRecord(Integer uid);


	List<Map<String,Object>> selectTestPaperStatisticalList(Integer userid);


}