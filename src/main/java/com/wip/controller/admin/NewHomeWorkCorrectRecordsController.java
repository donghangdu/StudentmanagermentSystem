package com.wip.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.controller.BaseController;
import com.wip.dao.NewStuHomeworkPaperMapper;
import com.wip.model.*;
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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Log4j
@Api("NewHomeWork management")
@Controller
@RequestMapping("admin/newhomeworkCorrectRecords")
public class NewHomeWorkCorrectRecordsController extends BaseController {
    @Resource
    private NewStuHomeworkPaperMapper newStuHomeworkPaperMapper;
    @ApiOperation("testPaperList")
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
        request.setAttribute("testCorrectRecordsList", new PageInfo<>(newStuHomeworkPaperMapper.selectNewHomeworkCorrectRecordsAndUserList(new HashMap<String, Object>() {{
            put("testPaperId", testPaperId);
        }})));
        request.setAttribute("testPaperId", testPaperId);
        request.setAttribute("testPaper", newStuHomeworkPaperMapper.selectOne(new NewStuHomeworkPaper().setId(testPaperId)));
        return "admin/newhomeworkList";
    }
    @ApiOperation("correcthomeworkpage")
    @GetMapping(value = "/correcthomeworkpage")
    public String index( HttpServletRequest request ,@RequestParam(name = "id", required = true)int id) {
        request.setAttribute("id", id);
        return "admin/newhomeworkDetails";
    }
    @ResponseBody
    @ApiOperation("updateScoreAndState")
    @PostMapping(value = "/updateScoreAndState")
    public APIResponse updateScoreAndState(@RequestParam(name = "id", required = true)int id, @RequestParam(name = "state", required = true)String state,
                                       @RequestParam(name = "score", required = true)int score) {
        NewStuHomeworkPaper newStuHomeworkPaper = new NewStuHomeworkPaper();
        newStuHomeworkPaper.setId(id);
        newStuHomeworkPaper.setState(state);
        newStuHomeworkPaper.setScore(score);
        newStuHomeworkPaper.setExaminationtime(new Date());
        System.out.println(newStuHomeworkPaper.toString());
        int booleanSave = newStuHomeworkPaperMapper.updateByPrimaryKeySelective(newStuHomeworkPaper);
        return APIResponse.success();
    }
}
