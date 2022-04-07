package com.wip.dao;

import com.wip.model.MessageClass;
import com.wip.model.TestClass;
import com.wip.utils.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


@org.apache.ibatis.annotations.Mapper
@Component
public interface MessageMapper extends Mapper<MessageClass>{
    List<Map<String,Object>> selectMessageByClassid(Page<Map<String, Object>> page);

    int selectMessageByClassidCount(Page<Map<String,Object>> page);

    List<Map<String,Object>> selectReplyByClassid(@Param("classid") Integer classid);
}
