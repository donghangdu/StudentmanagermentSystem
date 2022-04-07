package com.wip.dao;

import com.wip.model.ContentPercent;
import com.wip.model.TContents;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
@org.apache.ibatis.annotations.Mapper
public interface TContentsMapper extends Mapper<TContents> {

    @Select("<script>select cid,title,content, created,case when percent is NULL then 0 else percent end as percent  from t_contents as a LEFT JOIN (select * from test_studylogs where uid =#{userid}) as b ON a.cid=b.articleid   where 1=1 <if test=\"search !=null and search !=''\"> and title like #{search} </if> <if test=\"classid !=null\"> and classid = #{classid} and status='publish' </if> ORDER BY created desc</script>")
    List<ContentPercent> selectContentPercent(@Param("userid") Integer userid,@Param("search") String search,@Param("classid") Integer classid);
}