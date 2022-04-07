package com.wip.dao;

import com.wip.model.NewHomeworkPaper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface NewHomeworkPaperMapper  extends Mapper<NewHomeworkPaper> {
    List<Map<String,Object>> selectNewHomeworkPaperAndUserList(Integer userid);

    List<Map<String,Object>> selectNewHomeWorkListByClassid(@Param("classid") Integer classid, @Param("userid") Integer userid);
}
