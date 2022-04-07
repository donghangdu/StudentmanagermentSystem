package com.wip.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.controller.BaseController;
import com.wip.dao.TestCorrectRecordsMapper;
import com.wip.dao.TestPaperMapper;
import com.wip.dao.TestQuestionMapper;
import com.wip.dao.TestRecordMapper;
import com.wip.model.TestCorrectRecords;
import com.wip.model.TestPaper;
import com.wip.model.TestQuestion;
import com.wip.model.TestRecord;
import com.wip.utils.APIResponse;
import com.wip.utils.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.weekend.Weekend;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Log4j
@Api("Paper management")
@Controller
@RequestMapping("admin/testCorrectRecords")
public class TestCorrectRecordsController extends BaseController {

    @Resource
    private TestCorrectRecordsMapper testCorrectRecordsMapper;
    @Resource
    private TestPaperMapper testPaperMapper;

    @Resource
    private TestQuestionMapper testQuestionMapper;

    @Resource
    private TestRecordMapper testRecordMapper;

    @ApiOperation("")
    @GetMapping(value = "/list/{testPaperId}")
    public String index(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    int page,
            @ApiParam(name = "limit", value = "QPPage", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "15")
                    int limit,
            @PathVariable int testPaperId
    ) {
        PageHelper.startPage(page, limit);
        request.setAttribute("testCorrectRecordsList", new PageInfo<>(testCorrectRecordsMapper.selectTestCorrectRecordsAndUserList(new HashMap<String, Object>() {{
            put("testPaperId", testPaperId);
        }})));
        request.setAttribute("testPaperId", testPaperId);
        request.setAttribute("testPaper", testPaperMapper.selectOne(new TestPaper().setId(testPaperId)));
        return "admin/testCorrectRecordsList";
    }

    @GetMapping("/look")
    public String look(HttpServletRequest request, @RequestParam Integer id) {
        TestCorrectRecords testCorrectRecords = testCorrectRecordsMapper.selectOne(new TestCorrectRecords().setId(id));
        //Query Multiplechoicequestions
        List<TestQuestion> d = testQuestionMapper.select(new TestQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(1));
        if(!ObjectUtils.isEmpty(d)) {
            Integer dSum = d.stream().map(TestQuestion::getScore).reduce(Integer::sum).get();
            request.setAttribute("d", d.stream().sorted(Comparator.comparingInt(TestQuestion::getSerialNum)).collect(Collectors.toList()));
            request.setAttribute("dSum", dSum);
        }else {
            request.setAttribute("d", new ArrayList<>());
            request.setAttribute("dSum", 0);
        }
        //Query Shortanswerquestions
        List<TestQuestion> j = testQuestionMapper.select(new TestQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(2));
        if(!ObjectUtils.isEmpty(j)) {
        Integer jSum = j.stream().map(TestQuestion::getScore).reduce(Integer::sum).get();
        request.setAttribute("j", j.stream().sorted(Comparator.comparingInt(TestQuestion::getSerialNum)).collect(Collectors.toList()));
        request.setAttribute("jSum", jSum);
        }else {
            request.setAttribute("j", new ArrayList<>());
            request.setAttribute("jSum", 0);
        }
        //Query the content of students' answers and the questions and the right key of the questions
        List<Map<String, Object>> uQ = testCorrectRecordsMapper.selectUserRecordAndQuestion(id);
        request.setAttribute("uQ", uQ);
        request.setAttribute("testCorrectRecordsId", id);
        request.setAttribute("testPaperId",testCorrectRecords.getTestPaperId());
        return "admin/testDetailslook";
    }

    @GetMapping("/correct")
    public String correct(HttpServletRequest request, @RequestParam Integer id) {
        TestCorrectRecords testCorrectRecords = testCorrectRecordsMapper.selectOne(new TestCorrectRecords().setId(id));
        //Query Multiplechoicequestions
        List<TestQuestion> d = testQuestionMapper.select(new TestQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(1));
        if(!ObjectUtils.isEmpty(d)) {
            Integer dSum = d.stream().map(TestQuestion::getScore).reduce(Integer::sum).get();
            request.setAttribute("d", d.stream().sorted(Comparator.comparingInt(TestQuestion::getSerialNum)).collect(Collectors.toList()));
            request.setAttribute("dSum", dSum);
        }else {
            request.setAttribute("d", new ArrayList<>());
            request.setAttribute("dSum", 0);
        }
        //Query Shortanswerquestions
        List<TestQuestion> j = testQuestionMapper.select(new TestQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(2));
        if(!ObjectUtils.isEmpty(j)) {
            Integer jSum = j.stream().map(TestQuestion::getScore).reduce(Integer::sum).get();
            request.setAttribute("j", j.stream().sorted(Comparator.comparingInt(TestQuestion::getSerialNum)).collect(Collectors.toList()));
            request.setAttribute("jSum", jSum);
        }else {
            request.setAttribute("j", new ArrayList<>());
            request.setAttribute("jSum", 0);
        }
        //Query the content of students' answers and the questions and the right key of the questions
        List<Map<String, Object>> uQ = testCorrectRecordsMapper.selectUserRecordAndQuestion(id);
        request.setAttribute("uQ", uQ);
        request.setAttribute("testCorrectRecordsId", id);
        request.setAttribute("testPaperId",testCorrectRecords.getTestPaperId());
        return "admin/testDetails";
    }

    @PostMapping("/save")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public APIResponse save(@RequestBody Map<String, Object> params) {

        Integer uid = (Integer) params.get("uid");
        Integer testCorrectRecordsId = (Integer) params.get("testCorrectRecordsId");
        List<TestRecord> list = ConvertUtil.convert((List)params.get("list"),TestRecord.class);
        //update
        testCorrectRecordsMapper.updateByPrimaryKeySelective(new TestCorrectRecords().setId(testCorrectRecordsId).setCorrectCreator(uid).setCorrectTime(new Date()).setCorrectStatus(1));

        list.stream().forEach(testRecord -> {
            testRecordMapper.updateByPrimaryKeySelective(testRecord);
        });


        return APIResponse.success();
    }


    @ApiOperation("")
    @GetMapping(value = "/statisticallist/{testPaperId}")
    public String statisticallist(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    int page,
            @ApiParam(name = "limit", value = "QPPage", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "15")
                    int limit,
            @PathVariable int testPaperId
    ) {
        PageHelper.startPage(page, limit);
        request.setAttribute("testCorrectRecordsList", new PageInfo<>(testCorrectRecordsMapper.selectTestCorrectRecordsAndUserList(new HashMap<String, Object>() {{
            put("testPaperId", testPaperId);
            put("testCorrectRecordsState", 1);
            put("orderby", 1);
        }})));
        request.setAttribute("type", "testPaperStatistical");
        request.setAttribute("testPaperId", testPaperId);
        request.setAttribute("testPaper", testPaperMapper.selectOne(new TestPaper().setId(testPaperId)));
        return "admin/testCorrectRecordsList";
    }
}
