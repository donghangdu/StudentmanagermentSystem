package com.wip.dao;

import com.wip.model.TestUser;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Component
@org.apache.ibatis.annotations.Mapper
public interface TestUserMapper extends Mapper<TestUser> {
    @Select("select * from (select * from test_user where usertype=#{usertype}) as a left join (select * from test_class where status=1) as b on a.classid=b.id")
    List<Map<String,Object>> selectAllUserWithClass(Integer usertype);

    @Update("update test_user set classid = null where classid = #{classid}")
    int updateUserbyClassid(String classid);
}