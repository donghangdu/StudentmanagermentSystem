package com.wip.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.controller.BaseController;
import com.wip.dao.*;
import com.wip.model.*;
import com.wip.utils.APIResponse;
import com.wip.utils.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Log4j
@Api("Paper management")
@Controller
@RequestMapping("admin/homeworkCorrectRecords")
public class HomeWorkCorrectRecordsController extends BaseController {

    @Resource
    private HomeworkCorrectRecordsMapper homeworkCorrectRecordsMapper;
    @Resource
    private HomeworkPaperMapper homeworkPaperMapper;

    @Resource
    private HomeworkQuestionMapper homeworkQuestionMapper;

    @Resource
    private HomeworkRecordMapper homeworkRecordMapper;

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
        request.setAttribute("testCorrectRecordsList", new PageInfo<>(homeworkCorrectRecordsMapper.selectHomeworkCorrectRecordsAndUserList(new HashMap<String, Object>() {{
            put("testPaperId", testPaperId);
        }})));
        request.setAttribute("testPaperId", testPaperId);
        request.setAttribute("testPaper", homeworkPaperMapper.selectOne(new HomeworkPaper().setId(testPaperId)));
        return "admin/homeworkCorrectRecordsList";
    }

    @GetMapping("/look")
    public String look(HttpServletRequest request, @RequestParam Integer id) {
        HomeworkCorrectRecords testCorrectRecords = homeworkCorrectRecordsMapper.selectOne(new HomeworkCorrectRecords().setId(id));
        //
        List<HomeworkQuestion> d = homeworkQuestionMapper.select(new HomeworkQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(1));
        if(!ObjectUtils.isEmpty(d)) {
            Integer dSum = d.stream().map(HomeworkQuestion::getScore).reduce(Integer::sum).get();
            request.setAttribute("d", d.stream().sorted(Comparator.comparingInt(HomeworkQuestion::getSerialNum)).collect(Collectors.toList()));
            request.setAttribute("dSum", dSum);
        }else {
            request.setAttribute("d", new ArrayList<>());
            request.setAttribute("dSum", 0);
        }
        //
        List<HomeworkQuestion> j = homeworkQuestionMapper.select(new HomeworkQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(2));
        if(!ObjectUtils.isEmpty(j)) {
        Integer jSum = j.stream().map(HomeworkQuestion::getScore).reduce(Integer::sum).get();
        request.setAttribute("j", j.stream().sorted(Comparator.comparingInt(HomeworkQuestion::getSerialNum)).collect(Collectors.toList()));
        request.setAttribute("jSum", jSum);
        }else {
            request.setAttribute("j", new ArrayList<>());
            request.setAttribute("jSum", 0);
        }
        // key
        List<Map<String, Object>> uQ = homeworkCorrectRecordsMapper.selectUserRecordAndQuestion(id);
        request.setAttribute("uQ", uQ);
        request.setAttribute("testCorrectRecordsId", id);
        request.setAttribute("testPaperId",testCorrectRecords.getTestPaperId());
        return "admin/homeworkDetailslook";
    }

    @GetMapping("/correct")
    public String correct(HttpServletRequest request, @RequestParam Integer id) {
        HomeworkCorrectRecords testCorrectRecords = homeworkCorrectRecordsMapper.selectOne(new HomeworkCorrectRecords().setId(id));
        //
        List<HomeworkQuestion> d = homeworkQuestionMapper.select(new HomeworkQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(1));
        if(!ObjectUtils.isEmpty(d)) {
            Integer dSum = d.stream().map(HomeworkQuestion::getScore).reduce(Integer::sum).get();
            request.setAttribute("d", d.stream().sorted(Comparator.comparingInt(HomeworkQuestion::getSerialNum)).collect(Collectors.toList()));
            request.setAttribute("dSum", dSum);
        }else {
            request.setAttribute("d", new ArrayList<>());
            request.setAttribute("dSum", 0);
        }
        //
        List<HomeworkQuestion> j = homeworkQuestionMapper.select(new HomeworkQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(2));
        if(!ObjectUtils.isEmpty(j)) {
            Integer jSum = j.stream().map(HomeworkQuestion::getScore).reduce(Integer::sum).get();
            request.setAttribute("j", j.stream().sorted(Comparator.comparingInt(HomeworkQuestion::getSerialNum)).collect(Collectors.toList()));
            request.setAttribute("jSum", jSum);
        }else {
            request.setAttribute("j", new ArrayList<>());
            request.setAttribute("jSum", 0);
        }
        // key
        List<Map<String, Object>> uQ = homeworkCorrectRecordsMapper.selectUserRecordAndQuestion(id);
        request.setAttribute("uQ", uQ);
        request.setAttribute("testCorrectRecordsId", id);
        request.setAttribute("testPaperId",testCorrectRecords.getTestPaperId());
        return "admin/homeworkDetails";
    }

    @PostMapping("/save")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public APIResponse save(@RequestBody Map<String, Object> params) {

        Integer uid = (Integer) params.get("uid");
        Integer testCorrectRecordsId = (Integer) params.get("testCorrectRecordsId");
        List<HomeworkRecord> list = ConvertUtil.convert((List)params.get("list"),HomeworkRecord.class);
        //
        homeworkCorrectRecordsMapper.updateByPrimaryKeySelective(new HomeworkCorrectRecords().setId(testCorrectRecordsId).setCorrectCreator(uid).setCorrectTime(new Date()).setCorrectStatus(1));

        list.stream().forEach(testRecord -> {
            homeworkRecordMapper.updateByPrimaryKeySelective(testRecord);
        });


        return APIResponse.success();
    }
}
