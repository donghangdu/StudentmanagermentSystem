package com.wip.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.controller.BaseController;
import com.wip.dao.*;
import com.wip.model.*;
import com.wip.utils.APIResponse;
import com.wip.utils.ByteConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Log4j
@Api("Knowledge Point")
@Controller
@RequestMapping("admin/newknowledgepoint")
public class NewKnowledgePointController extends BaseController {
    @Resource
    private NewknowledgePointPaperMapper newknowledgePointMapper;
    @Resource
    TestUserMapper testUserMapper;
    @Resource
    TestClassMapper testClassMapper;


    @ApiOperation("newknowledgepoint")
    @GetMapping(value = "/knowledgelist")
    public String index(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    int page,
            @ApiParam(name = "limit", value = "QPPage", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "8")
                    int limit,
            @ApiParam(name = "userid", required = false)int userid,
            @ApiParam(name = "classid", required = false)int classid
    ) {
        PageHelper.startPage( page, limit);
        request.setAttribute("newKnowledgePointList",new PageInfo<>(newknowledgePointMapper.selectNewKnowledgePointList(userid)));
        request.setAttribute("userid",userid);
        request.setAttribute("classid",classid);
        return "admin/newKnowledgePoint";
    }
    @GetMapping("/addnewknowledgepointpage")
    public String addnewknowledgepointpage( HttpServletRequest request, @ApiParam(name = "userid", required = false)int userid,
                                            @ApiParam(name = "classid", required = false)int classid) {
        System.err.println(userid+classid);
        TestUser testUser = testUserMapper.selectByPrimaryKey(userid);
        TestClass testClass = testClassMapper.selectByPrimaryKey(classid);
        String username=testUser.getUsername();
        String classname=testClass.getClassname();
        request.setAttribute("userid",userid);
        request.setAttribute("classid",classid);
        request.setAttribute("username",username);
        request.setAttribute("classname",classname);
        return "admin/addKnowledgePointpage";
    }
    @ResponseBody
    @ApiOperation("saveNewknowledge")
    @PostMapping(value = "/saveNewknowledge")
    public APIResponse saveNewknowledge(@RequestParam(name = "userid", required = true)int userid, @RequestParam(name = "username", required = true)String username,
                                       @RequestParam(name = "classid", required = true)int classid,@RequestParam(name = "classname", required = true)String classname,
                                       @RequestParam(name = "created", required = true)String created,@RequestParam(name = "content", required = true)String content,
                                        @RequestParam(name = "status", required = true)String status,@RequestParam(name = "title", required = true)String title,
                                        @RequestParam(name = "tags", required = true)String tags) {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        NewKnowledgePointList newKnowledgePointList = new NewKnowledgePointList();
        newKnowledgePointList.setAuthorId(userid);
        newKnowledgePointList.setAuthorName(username);
        newKnowledgePointList.setClassid(classid);
        newKnowledgePointList.setClassname(classname);
        newKnowledgePointList.setCreatedtime(created);
        newKnowledgePointList.setStatus(status);
        newKnowledgePointList.setContent(content);
        newKnowledgePointList.setTags(tags);
        newKnowledgePointList.setTitle(title);
        int booleanSave = newknowledgePointMapper.insert(newKnowledgePointList);
        return APIResponse.success();
    }
    @PostMapping("/delete")
    @ResponseBody
    public APIResponse delete(@RequestParam Integer id) {
        try {
            newknowledgePointMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            return APIResponse.fail("Service exception");
        }
        return APIResponse.success();
    }
    @GetMapping("/editnewknowledgepointpage")
    public String editnewknowledgepointpage( HttpServletRequest request, @ApiParam(name = "id", required = false)int id) {
        System.err.println(id);
        NewKnowledgePointList newKnowledgePointList = newknowledgePointMapper.selectByPrimaryKey(id);
        request.setAttribute("newKnowledgePointList",newKnowledgePointList);
        return "admin/editKnowledgePointpage";
    }
    @ResponseBody
    @ApiOperation("queryNewknowledgePoint")
    @PostMapping(value = "/queryNewknowledgePoint")
    public APIResponse queryNewknowledgePoint(@RequestParam(name = "id", required = true)int id) {
        NewKnowledgePointList  newKnowledgePointList = newknowledgePointMapper.selectByPrimaryKey(id);
        return APIResponse.success(newKnowledgePointList);
    }
    @ResponseBody
    @ApiOperation("updateNewknowledge")
    @PostMapping(value = "/updateNewknowledge")
    public APIResponse updateNewknowledge(@RequestParam(name = "userid", required = true)int userid, @RequestParam(name = "username", required = true)String username,
                                        @RequestParam(name = "classid", required = true)int classid,@RequestParam(name = "classname", required = true)String classname,
                                        @RequestParam(name = "created", required = true)String created,@RequestParam(name = "content", required = true)String content,
                                        @RequestParam(name = "status", required = true)String status,@RequestParam(name = "title", required = true)String title,
                                        @RequestParam(name = "tags", required = true)String tags,@RequestParam(name = "id", required = true)int cid) {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        NewKnowledgePointList newKnowledgePointList = new NewKnowledgePointList();
        newKnowledgePointList.setCid(cid);
        newKnowledgePointList.setAuthorId(userid);
        newKnowledgePointList.setAuthorName(username);
        newKnowledgePointList.setClassid(classid);
        newKnowledgePointList.setClassname(classname);
        newKnowledgePointList.setCreatedtime(created);
        newKnowledgePointList.setStatus(status);
        newKnowledgePointList.setContent(content);
        newKnowledgePointList.setTags(tags);
        newKnowledgePointList.setTitle(title);
        int booleanSave = newknowledgePointMapper.updateByPrimaryKey(newKnowledgePointList);
        return APIResponse.success();
    }
}
