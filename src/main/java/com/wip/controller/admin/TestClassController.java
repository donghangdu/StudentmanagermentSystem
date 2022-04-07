package com.wip.controller.admin;

import com.github.pagehelper.PageInfo;
import com.wip.dao.TestClassMapper;
import com.wip.dao.TestUserMapper;
import com.wip.model.TestClass;
import com.wip.model.TestUser;
import com.wip.service.testclass.ClassService;
import com.wip.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Api("Class management")
@Controller
@RequestMapping("/admin/class")
public class TestClassController {

    @Resource
    private TestClassMapper testClassMapper;

    @Resource
    private ClassService classService;

    @Resource
    private TestUserMapper testUserMapper;


    @ApiOperation("")
    @GetMapping(value = "/index")
    public String index(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    int page,
            @ApiParam(name = "limit", value = "QPPage", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "15")
                    int limit,
            @ApiParam(name = "isTeacher", value = "isTeacher", required = false)
            @RequestParam(name = "isTeacher", required = false, defaultValue = "0")
                    int isTeacher
    ) {
        PageInfo<TestClass> tClassPageInfo = classService.getClassPageByPage(page,limit);
        request.setAttribute("tClasses",tClassPageInfo);
        request.setAttribute("isTeacher",isTeacher);
        return "admin/class_list";
    }

    @ApiOperation("")
    @PostMapping(value = "/add")
    public String addClass(@ApiParam TestClass tclass, @RequestParam(value = "file")MultipartFile file, HttpServletRequest request) throws ServletException, IOException {
        if (file.isEmpty()) {
            System.out.println("The file is empty");
        }
        int b=classService.addClass(tclass,file,request);
        return "redirect:/admin/class/index";
    }

    @ApiOperation("")
    @PostMapping(value = "/delete")
    @ResponseBody
    public APIResponse delete(@ApiParam(name = "id", value = "classid", required = true) @RequestParam(name = "id", required = true) int id) {
        classService.deleteClassPageById(id);
        return APIResponse.success();
    }
    @ApiOperation("")
    @GetMapping(value = "/edit")
    public String edit(@ApiParam(name = "id", value = "classid", required = true) @RequestParam(name = "id", required = true)int id, Model model){
        TestClass testClass=testClassMapper.selectByPrimaryKey(id);
        model.addAttribute("testClass",testClass);
        return "admin/class_edit";
    }
    @ApiOperation("save")
    @PostMapping(value = "/edit/save")
    public String editSave(@ApiParam TestClass testClass,@RequestParam(value = "inputFile")MultipartFile file, HttpServletRequest request, Model model){
        if(!file.isEmpty()){
            String fileName=file.getOriginalFilename();
            String path = "src/main/resources/static/upload/imgs";
            String fileType=fileName.substring(fileName.lastIndexOf("."));
            fileName= UUID.randomUUID().toString()+fileType;
            System.out.println("saveip："+path);
            //项目内地址
            File dest = new File((new File(path ).getAbsolutePath()+File.separator+fileName));
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //访问地址
            String returnUrl = "/upload/imgs/";
            String filename = returnUrl + fileName;
            System.out.println(filename);
            testClass.setStatus("1");
            testClass.setHeadimage(filename);
        }
        int booleanSave=classService.editSave(testClass);
        if(booleanSave==1){
            return "redirect:/admin/class/index";
        }
        model.addAttribute("testClass",testClass);
        return "error/500";
    }
    @ApiOperation("showAllStudent")
    @GetMapping(value = "/showAllStudent")
    public String showAllStudent( Model model,@RequestParam String id) {
        TestUser t= new TestUser();
        t.setClassid(id);
        t.setUsertype(0);
        List<TestUser> testList =  testUserMapper.select(t);
        model.addAttribute("testList", testList);
        return "admin/showAllStudents";
    }
}
