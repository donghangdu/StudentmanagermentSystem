package com.wip.dao;

import com.wip.model.TestClass;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
@Component
public interface TestClassMapper extends Mapper<TestClass> {
    @Select("select a.*,b.username teachers from test_class a ,test_user b where a.id = b.classid and b.usertype=1")
    List<Map<String,Object>> selectAllClassAndTeachers();
}