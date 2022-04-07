package com.wip.controller.admin;

import com.wip.dao.MessageMapper;
import com.wip.dao.TestClassMapper;
import com.wip.model.MessageClass;
import com.wip.model.TestClass;
import com.wip.service.message.MessageService;
import com.wip.utils.APIResponse;
import com.wip.utils.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api("messageManage")
@RestController
@RequestMapping("/admin/message")
public class MessageController {
    @Resource
    private TestClassMapper testClassMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private MessageService messageService;


    @ApiOperation("queryMessage")
    @PostMapping(value = "/queryMessage")
    public APIResponse classSelectList(
            @ApiParam(name = "id", value = "classid", required = true) @RequestParam(name = "id", required = true)int id, Model model) {
        TestClass  testClasses = testClassMapper.selectByPrimaryKey(id);
        return APIResponse.success(testClasses);
    }
    @ApiOperation("addMessage")
    @PostMapping(value = "/addMessage")
    public APIResponse addMessage(@ApiParam MessageClass msglass, HttpServletRequest request) throws ServletException, IOException {
        if (msglass.getContent()=="") {
            System.out.println("The message is empty");
        }
        int b=messageService.addMessage(msglass,request);
        return APIResponse.success();
    }
    @ApiOperation("queryMessageByClassid")
    @PostMapping(value = "/queryMessageByClassid")
    public APIResponse queryMessageByClassid(
            @ApiParam(name = "classid", value = "classid", required = true) @RequestParam(name = "classid", required = true)int classid,@RequestParam(name = "currentPage", required = true)int currentPage, Model model) {
        Page<Map<String, Object>> page=new Page<>();
        page.setCurrentPage(currentPage);
        Map<String,Object> params=new HashMap<>();
        params.put("classid",classid);
        page.setParams(params);
        List<Map<String, Object>> messageClass = messageMapper.selectMessageByClassid(page);
        page.setList(messageClass);
        int count = messageMapper.selectMessageByClassidCount(page);
        page.setTotalCount(count);
        return APIResponse.success(page);
    }
    @ApiOperation("queryReplyByClassid")
    @PostMapping(value = "/queryReplyByClassid")
    public APIResponse queryReplyByClassid(
            @ApiParam(name = "classid", value = "classid", required = true) @RequestParam(name = "classid", required = true)int classid, Model model) {
        List<Map<String, Object>> messageClass = messageMapper.selectReplyByClassid(classid);
        return APIResponse.success(messageClass);
    }
}
