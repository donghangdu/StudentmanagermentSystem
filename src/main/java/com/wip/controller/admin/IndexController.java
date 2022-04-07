package com.wip.controller.admin;


import com.github.pagehelper.PageInfo;
import com.wip.constant.LogActions;
import com.wip.constant.WebConst;
import com.wip.controller.BaseController;
import com.wip.dao.*;
import com.wip.dto.StatisticsDto;
import com.wip.exception.BusinessException;
import com.wip.model.*;
import com.wip.service.log.LogService;
import com.wip.service.site.SiteService;
import com.wip.service.user.UserService;
import com.wip.utils.APIResponse;
import com.wip.utils.GsonUtils;
import com.wip.utils.TaleUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Api("Home page")
@Controller("adminIndexController")
@RequestMapping(value = "/admin")
public class IndexController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    @Resource
    private SiteService siteService;


    @Resource
    TestStudylogsMapper studylogsMapper;

    @Resource
    TContentsMapper tContentsMapper;

    @Resource
    TestUserMapper testUserMapper;

    @Resource
    TestClassMapper testClassMapper;

    @Resource
    private TestCorrectRecordsMapper testCorrectRecordsMapper;
    @Resource
    private TestPaperMapper testPaperMapper;

    @Resource
    private TestQuestionMapper testQuestionMapper;

    @Resource
    private TestRecordMapper testRecordMapper;


    @Resource
    private HomeworkCorrectRecordsMapper homeworkCorrectRecordsMapper;
    @Resource
    private HomeworkPaperMapper homeworkPaperMapper;

    @Resource
    private HomeworkQuestionMapper homeworkQuestionMapper;

    @Resource
    private HomeworkRecordMapper homeworkRecordMapper;


    @ApiOperation("Home page")
    @GetMapping(value = {"","/index"})
    public String index(HttpServletRequest request) {

        return "admin/index";
    }


    @ApiOperation("Home page")
    @GetMapping(value = "start")
    public String start(HttpServletRequest request) {
        //
        List<HomeworkCorrectRecords> homeworkCorrectRecordsList=homeworkCorrectRecordsMapper.select(new HomeworkCorrectRecords().setCorrectStatus(2));
        //
        List<TestCorrectRecords> testCorrectRecordsList = testCorrectRecordsMapper.select(new TestCorrectRecords().setCorrectStatus(2));

        //
        List<TestPaper> testPaperList = testPaperMapper.select(new TestPaper().setState(1));

        //
        List<HomeworkPaper> homeworkPaperList =homeworkPaperMapper.select(new HomeworkPaper().setState(1));

        //
        Map<Integer,Integer> testPaperIdList=testCorrectRecordsList.stream().collect(Collectors.toMap(TestCorrectRecords::getTestPaperId, TestCorrectRecords::getTestPaperId,(key1, key2) -> key2));
        List<TestPaper>  dpgtestPaperList = new ArrayList<>();
        if(!ObjectUtils.isEmpty(testPaperIdList)){
            dpgtestPaperList=testPaperList.stream().filter(testPaper -> !ObjectUtils.isEmpty(testPaperIdList.get(testPaper.getId()))).collect(Collectors.toList());
        }

        //
        Map<Integer,Integer> homeworkIdList=homeworkCorrectRecordsList.stream().collect(Collectors.toMap(HomeworkCorrectRecords::getTestPaperId, HomeworkCorrectRecords::getTestPaperId,(key1, key2) -> key2));
        List<HomeworkPaper>  dpghomeworkPaperList = new ArrayList<>();
        if(!ObjectUtils.isEmpty(homeworkIdList)){
            dpghomeworkPaperList=homeworkPaperList.stream().filter(homeworkPaper -> !ObjectUtils.isEmpty(homeworkIdList.get(homeworkPaper.getId()))).collect(Collectors.toList());
        }




        //
        PageInfo<LogDomain> logs = logService.getLogs(1,5);
        List<LogDomain> list = logs.getList();

        request.setAttribute("homeworkCorrectRecordsList",homeworkCorrectRecordsList);
        request.setAttribute("testCorrectRecordsList",testCorrectRecordsList);
        request.setAttribute("testPaperList",testPaperList);
        request.setAttribute("homeworkPaperList",homeworkPaperList);
        request.setAttribute("dpgtestPaperList",dpgtestPaperList);
        request.setAttribute("dpghomeworkPaperList",dpghomeworkPaperList);
        request.setAttribute("logs",list);
        LOGGER.info("Exit admin index method");
        return "admin/start";
    }

    /**
     *
     */
    @GetMapping(value = "/profile")
    public String profile() {
        return "admin/profile";
    }


    /**
     *
     * @param screenName
     * @param email
     * @param request
     * @param session
     * @return
     */
    @PostMapping(value = "/profile")
    @ResponseBody
    public APIResponse saveProfile(
            @RequestParam String screenName,
            @RequestParam String email,
            HttpServletRequest request,
            HttpSession session
    ) {
        TestUser users = this.user(request);
        if (StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(email)) {
            TestUser temp = new TestUser();
            temp.setUserid(users.getUserid());
            temp.setRealname(screenName);
            temp.setEmail(email);
            //
            userService.updateUserInfo(temp);
            //
            logService.addLog(LogActions.UP_INFO.getAction(),GsonUtils.toJsonString(temp),request.getRemoteAddr(),this.getUid(request));

            //
            TestUser originAL = (TestUser) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            originAL.setRealname(screenName);
            originAL.setEmail(email);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY, originAL);
        }
        return APIResponse.success();
    }

    /**
     *
     * @param oldPassword
     * @param newPassword
     * @param request
     * @param session
     * @return
     */
    @PostMapping(value = "/password")
    @ResponseBody
    public APIResponse upPwd(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            HttpServletRequest request,
            HttpSession session
    ) {
        TestUser users = this.user(request);
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            return APIResponse.fail("Please confirm that the information is complete");
        }

        if (!users.getUserpwd().equals( oldPassword)) {
            return APIResponse.fail("Old password error");
        }

        if (newPassword.length() < 6 || newPassword.length() > 14) {
            return APIResponse.fail("Please enter a 6-14 digit password");
        }

        try {
            TestUser temp = new TestUser();
            temp.setUserid(users.getUserid());
            temp.setUserpwd(newPassword);
            userService.updateUserInfo(temp);
            logService.addLog(LogActions.UP_PWD.getAction(), null,request.getRemoteAddr(),this.getUid(request));

            //
             TestUser originAL = (TestUser) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            originAL.setUserpwd(newPassword);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY,originAL);
            return APIResponse.success();
        } catch (Exception e) {
            String msg = "Password modification failed";
            if (e instanceof BusinessException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg,e);
            }
            return APIResponse.fail(msg);
        }
    }


}
