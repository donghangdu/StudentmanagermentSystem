package com.wip.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.controller.BaseController;
import com.wip.dao.TestPaperMapper;
import com.wip.dao.TestQuestionMapper;
import com.wip.dao.TestRecordMapper;
import com.wip.dto.cond.ContentCond;
import com.wip.model.*;
import com.wip.service.article.ContentService;
import com.wip.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Log4j
@Api("Paper management")
@Controller
@RequestMapping("admin/testPaper")
public class TestPaperController extends BaseController {

    @Resource
    private TestPaperMapper testPaperMapper;

    @Resource
    private TestQuestionMapper testQuestionMapper;

    @ApiOperation("")
    @GetMapping(value = "/list")
    public String index(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    int page,
            @ApiParam(name = "limit", value = "QPPage", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "15")
                    int limit,
            @ApiParam(name = "userid", required = false)int userid,
            @ApiParam(name = "classid", required = false)int classid
    ) {
        System.err.println(userid+"+"+classid);
        PageHelper.startPage( page, limit);
        request.setAttribute("testPaperList",new PageInfo<>(testPaperMapper.selectTestPaperAndUserList(userid)));
        return "admin/testPaperList";
    }
    @PostMapping("/add")
    @ResponseBody
    public APIResponse add(@RequestParam String name,@RequestParam int uid){
        try{
            if (ObjectUtils.isEmpty(testPaperMapper.select(new TestPaper().setName(name)))) {
                testPaperMapper.insert(new TestPaper().setCreateTime(new Date()).setCreator(uid).setName(name).setState(2));
            }else {
                return APIResponse.fail("A test paper with the same name already exists");
            }

        }catch (Exception e){
            log.error(e);
            return APIResponse.fail("Service exception");
        }
        return APIResponse.success();
    }
    @PostMapping("/delete")
    @ResponseBody
    public APIResponse delete(@RequestParam int id){
        try{
            if (testPaperMapper.selectOne(new TestPaper().setId(id)).getState().equals(2)) {
                testPaperMapper.deleteByPrimaryKey(id);
            }else {
                return APIResponse.fail("The paper has been put into use");
            }
        }catch (Exception e){
            log.error(e);
            return APIResponse.fail("Service exception");
        }
        return APIResponse.success();
    }

    @PostMapping("/state")
    @ResponseBody
    public APIResponse state(@RequestParam int id){
        try{

            testPaperMapper.updateByPrimaryKeySelective(new TestPaper().setId(id).setState(1));
        }catch (Exception e){
            log.error(e);
            return APIResponse.fail("Service exception");
        }
        return APIResponse.success();
    }
    @GetMapping("/preview")
    public String preview(HttpServletRequest request, @RequestParam Integer testPaperId) {
        //Multiplechoicequestions
        List<TestQuestion> d = testQuestionMapper.select(new TestQuestion().setTestPaperId(testPaperId).setType(1));
        if(!ObjectUtils.isEmpty(d)) {
            Integer dSum = d.stream().map(TestQuestion::getScore).reduce(Integer::sum).get();
            request.setAttribute("d", d.stream().sorted(Comparator.comparingInt(TestQuestion::getSerialNum)).collect(Collectors.toList()));
            request.setAttribute("dSum", dSum);
        }else {
            request.setAttribute("d", new ArrayList<>());
            request.setAttribute("dSum", 0);
        }
        //Shortanswerquestions
        List<TestQuestion> j = testQuestionMapper.select(new TestQuestion().setTestPaperId(testPaperId).setType(2));
        if(!ObjectUtils.isEmpty(j)) {
            Integer jSum = j.stream().map(TestQuestion::getScore).reduce(Integer::sum).get();
            request.setAttribute("j", j.stream().sorted(Comparator.comparingInt(TestQuestion::getSerialNum)).collect(Collectors.toList()));
            request.setAttribute("jSum", jSum);
        }else {
            request.setAttribute("j", new ArrayList<>());
            request.setAttribute("jSum", 0);
        }
        //
        request.setAttribute("uQ", testQuestionMapper.select(new TestQuestion().setTestPaperId(testPaperId)));
        request.setAttribute("testPaper",testPaperMapper.selectOne(new TestPaper().setId(testPaperId)));
        return "admin/previewTestPaper";
    }


    @ApiOperation("TPSlistPage")
    @GetMapping(value = "/statisticalList")
    public String statisticalList(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    int page,
            @ApiParam(name = "limit", value = "QPPage", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "15")
                    int limit,
            @ApiParam(name = "userid", required = false)int userid,
            @ApiParam(name = "classid", required = false)int classid
    ) {
        PageHelper.startPage( page, limit);
        request.setAttribute("testPaperList",new PageInfo<>(testPaperMapper.selectTestPaperStatisticalList(userid)));
        return "admin/testPaperStatisticalList";
    }


}
