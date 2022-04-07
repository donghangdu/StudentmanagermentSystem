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
@Api("Paper management")
@Controller
@RequestMapping("admin/newhomeworkPaper")
public class NewHomeWorkPaperController extends BaseController {
    @Resource
    private NewHomeworkPaperMapper newhomeworkPaperMapper;
    @Resource
    private TestUserMapper testUserMapper;
    @Resource
    private TestClassMapper testClassMapper;

    @Resource
    private NewStuHomeworkPaperMapper newStuHomeworkPaperMapper;

    @ApiOperation("newhomeworkPaper")
    @GetMapping(value = "/homeworklist")
    public String index(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    int page,
            @ApiParam(name = "limit", value = "QPPage", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "8")
                    int limit,
            @ApiParam(name = "userid", required = false)int userid
    ) {
        PageHelper.startPage( page, limit);
        request.setAttribute("testPaperList",new PageInfo<>(newhomeworkPaperMapper.selectNewHomeworkPaperAndUserList(userid)));
        request.setAttribute("userid",userid);
        return "admin/newhomeworkPaperList";
    }
    @ApiOperation("newhomeworkPaper")
    @GetMapping(value = "/addhomeworkpage")
    public String index( HttpServletRequest request ) {
        return "admin/addhomeworkPage";
    }
    @ApiOperation("newhomeworkpreviewpage")
    @GetMapping(value = "/newhomeworkpreviewpage")
    public String newhomeworkpreviewpage( HttpServletRequest request ) {
        return "admin/newhomeworkpreviewpage";
    }
    @PostMapping("/savenewhomework")
    @ResponseBody
    public APIResponse savenewhomework(@ApiParam(name = "id", value = "id", required = true) @RequestParam(name = "id", required = true)int id){
        newhomeworkPaperMapper.insert(new NewHomeworkPaper().setTime(new Date()).setStatus("0").setId(id));
        return APIResponse.success();
    }
    @ResponseBody
    @ApiOperation("queryClassAndTeacherInfo")
    @PostMapping(value = "/queryClassAndTeacherInfo")
    public APIResponse queryClassAndTeacherInfo() {
        List<TestUser> testuser = testUserMapper.selectAll();
        List<TestClass> testclass = testClassMapper.selectAll();
        Map<String,Object> params=new HashMap<>();
        params.put("testuser",testuser);
        params.put("testclass",testclass);
        return APIResponse.success(params);
    }
    @ResponseBody
    @ApiOperation("queryNewWorkInfo")
    @PostMapping(value = "/queryNewWorkInfo")
    public APIResponse queryNewWorkInfo(@RequestParam(name = "id", required = true)int id) {
        NewHomeworkPaper  newwork = newhomeworkPaperMapper.selectByPrimaryKey(new NewHomeworkPaper().setId(id));
        return APIResponse.success(newwork);
    }
    @ResponseBody
    @ApiOperation("savenewwork")
    @PostMapping(value = "/saveNewworkInfo")
    public APIResponse saveNewworkInfo(@RequestParam(name = "id", required = true)int id, @RequestParam(name = "classid", required = true)int classid,
                                       @RequestParam(name = "authorid", required = true)int authorid,@RequestParam(name = "workname", required = true)String workname,
                                       @RequestParam(name = "classname", required = true)String classname,@RequestParam(name = "content", required = true)String content,
                                       @RequestParam(name = "authorname", required = true)String authorname,@RequestParam(name = "status", required = false)String status,
                                       @RequestParam(name = "time", required = true)String time) {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        NewHomeworkPaper newHomeworkPaper = new NewHomeworkPaper();
        newHomeworkPaper.setId(id);
        newHomeworkPaper.setClassid(classid);
        newHomeworkPaper.setClassname(classname);
        newHomeworkPaper.setAuthorid(authorid);
        newHomeworkPaper.setAuthorname(authorname);
        newHomeworkPaper.setTime(Timestamp.valueOf(time));
        newHomeworkPaper.setContent(content);
        newHomeworkPaper.setWorkname(workname);
        newHomeworkPaper.setStatus(status);
        System.out.println(newHomeworkPaper.toString());
        int booleanSave = newhomeworkPaperMapper.updateByPrimaryKeySelective(newHomeworkPaper);
        return APIResponse.success();
    }
    @RequestMapping("/upload")
    @ResponseBody
    public Result upload(@RequestParam(value="file") MultipartFile multipartFile, @RequestParam(name = "id", required = true)int id) {
        try{
            byte[] file = multipartFile.getBytes();
            NewHomeworkPaper newHomeworkPaper = new NewHomeworkPaper();
            newHomeworkPaper.setId(id);
            newHomeworkPaper.setFile(ByteConvertUtil.bytesToHexString(file));
            newHomeworkPaper.setFilename(multipartFile.getOriginalFilename());
            System.out.println(newHomeworkPaper.toString());
            int booleanSave = newhomeworkPaperMapper.updateByPrimaryKeySelective(newHomeworkPaper);
        }catch (IOException e){
            return Result.newFailureResult("upload defeat");
        }
        return Result.newResult(1,"upload success");
    }
    @PostMapping("/deletehomeworkpage")
    @ResponseBody
    public APIResponse deletehomeworkpage(@RequestParam int id){
        newhomeworkPaperMapper.deleteByPrimaryKey(id);
        return APIResponse.success();
    }
    @PostMapping("/status")
    @ResponseBody
    public APIResponse status(@RequestParam int id){
        newhomeworkPaperMapper.updateByPrimaryKeySelective(new NewHomeworkPaper().setId(id).setStatus("on"));
        return APIResponse.success();
    }
    @GetMapping("/downloadFile")
    public void downloadFiledownloadFile(@RequestParam int id, HttpServletResponse response)throws IOException {
        NewHomeworkPaper  newwork = newhomeworkPaperMapper.selectByPrimaryKey(new NewHomeworkPaper().setId(id));
        String attach_name = newwork.getFilename();
        byte[] attach_value = ByteConvertUtil.hexToByteArray(newwork.getFile());
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(attach_name, "UTF-8"));
        IOUtils.write(attach_value, response.getOutputStream());
    }
    @GetMapping("/downloadstuFile")
    public void downloadstuFile(@RequestParam int id,@RequestParam int userid, HttpServletResponse response)throws IOException {
        NewStuHomeworkPaper  newwork = newStuHomeworkPaperMapper.selectByPrimaryKey(new NewStuHomeworkPaper().setId(id));
        String attach_name = newwork.getFilename();
        byte[] attach_value = ByteConvertUtil.hexToByteArray(newwork.getFile());
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(attach_name, "UTF-8"));
        IOUtils.write(attach_value, response.getOutputStream());
    }
    @RequestMapping("/uploadfinshed")
    @ResponseBody
    public Result uploadfinshed(@RequestParam(value="file") MultipartFile multipartFile, @RequestParam(name = "id", required = true)int id,
                                @RequestParam(name = "userid", required = true)int userid) {
        try{
            byte[] file = multipartFile.getBytes();
            NewStuHomeworkPaper newstuHomeworkPaper = new NewStuHomeworkPaper();
            newstuHomeworkPaper.setUserid(userid);
            newstuHomeworkPaper.setWorkid(id);
            newstuHomeworkPaper.setFile(ByteConvertUtil.bytesToHexString(file));
            newstuHomeworkPaper.setFilename(multipartFile.getOriginalFilename());
            newstuHomeworkPaper.setState("1");
            newstuHomeworkPaper.setTime(new Date());
            System.out.println(newstuHomeworkPaper.toString());
            int booleanSave = newStuHomeworkPaperMapper.insert(newstuHomeworkPaper);
        }catch (IOException e){
            return Result.newFailureResult("上传失败");
        }
        return Result.newResult(1,"上传文件成功");
    }
    @ResponseBody
    @ApiOperation("queryStuAnswer")
    @PostMapping(value = "/queryStuAnswer")
    public APIResponse queryStuAnswer(@RequestParam(name = "id", required = true)int id,@RequestParam(name = "userid", required = true)int userid) {
        List<NewStuHomeworkPaper>  list= newStuHomeworkPaperMapper.select(new NewStuHomeworkPaper().setWorkid(id).setUserid(userid));
        return APIResponse.success(list);
    }
}
