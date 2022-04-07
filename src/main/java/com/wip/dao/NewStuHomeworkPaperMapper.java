package com.wip.dao;

import com.wip.model.NewHomeworkPaper;
import com.wip.model.NewStuHomeworkPaper;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface NewStuHomeworkPaperMapper  extends Mapper<NewStuHomeworkPaper> {
    List<Map<String,Object>> selectNewHomeworkCorrectRecordsAndUserList(HashMap<String, Object> testPaperId);
}