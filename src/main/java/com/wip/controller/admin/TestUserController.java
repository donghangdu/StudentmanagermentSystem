package com.wip.controller.admin;


import com.github.pagehelper.PageInfo;
import com.wip.dao.TestClassMapper;
import com.wip.model.TestClass;
import com.wip.model.TestUser;
import com.wip.service.testuser.TestUserService;
import com.wip.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api("")
@Controller
@RequestMapping("/admin/user")
public class TestUserController {

    @Resource
    TestUserService testUserService;

    @Resource
    TestClassMapper testClassMapper;

    @ApiOperation("userPage")
    @GetMapping(value = "/index")
    public String index(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @ApiParam(name = "limit", value = "QPPage", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "8") int limit,//Limit the rowCount
            @ApiParam(name = "type", value = "Type", required = false)
            @RequestParam(name = "usertype", required = false) Integer usertype,
            Model model
    ) {
        PageInfo<Map<String, Object>> testUserPageInfo = testUserService.getUserPageByPage(page, limit, usertype);
        model.addAttribute("TestUser", testUserPageInfo);
        model.addAttribute("usertype", usertype);
        if (usertype == 1) {
            model.addAttribute("usertype", "teacher");
            model.addAttribute("grade", "Teaher management");
        } else if (usertype == 0) {
            model.addAttribute("usertype", "student");
            model.addAttribute("grade", "Student management");
        }
        return "admin/user_list";
    }

    @ApiOperation("")
    @GetMapping(value = "/add")
    public String addUser(Model model, @ApiParam String usertype) {

        List<TestClass> testClasses = testClassMapper.selectAll();
        model.addAttribute("TestClass", testClasses);
        model.addAttribute("usertype", usertype);
        //TODO
        return "admin/user_edit";
    }

    @ApiOperation("")
    @PostMapping(value = "/add/save")
    public String addUser(@ApiParam TestUser testUser, HttpServletRequest request) {
        int b = testUserService.addUser(testUser, request);
        return "redirect:/admin/user/index?usertype=" + testUser.getUsertype();
    }

    @ApiOperation("")
    @GetMapping(value = "/edit")
    public String editUser(@ApiParam TestUser testUser, Model model) {
        testUser = testUserService.selectById(testUser);
        model.addAttribute("TestUser", testUser);
        Integer usertype = testUser.getUsertype();
        List<TestClass> testClasses = testClassMapper.selectAll();
        if (usertype == 1) {
            model.addAttribute("TestClass", testClasses);
            model.addAttribute("usertype", "teacher");
            model.addAttribute("grade", "Teacher Management");
        } else if (usertype == 0) {
            model.addAttribute("TestClass", testClasses);
            model.addAttribute("usertype", "student");
            model.addAttribute("grade", "Student Management");
        }
        return "admin/user_edit";
    }

    @ApiOperation("")
    @PostMapping(value = "/edit/save")
    public String editUserSave(@ApiParam TestUser testUser, Model model) {
        System.out.println(testUser.toString());
        int booleanSave = testUserService.editSave(testUser);
        if (booleanSave == 1) {
            return "redirect:/admin/user/index?usertype=" + testUser.getUsertype();
        }
        model.addAttribute("TestUser", testUser);
        return "admin/user_edit";
    }

    @ApiOperation("")
    @PostMapping(value = "/delete")
    @ResponseBody
    public APIResponse delete(@ApiParam(name = "userid", value = "userid", required = true) @RequestParam(name = "id", required = true) int userid) {
        testUserService.deleteUserPageById(userid);
        return APIResponse.success();
    }


}
