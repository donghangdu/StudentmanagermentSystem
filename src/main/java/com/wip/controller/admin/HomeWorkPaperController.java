package com.wip.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.controller.BaseController;
import com.wip.dao.*;
import com.wip.model.HomeworkPaper;
import com.wip.model.HomeworkQuestion;
import com.wip.model.TestPaper;
import com.wip.model.TestQuestion;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Log4j
@Api("Paper management")
@Controller
@RequestMapping("admin/homeworkPaper")
public class HomeWorkPaperController extends BaseController {

    @Resource
    private HomeworkCorrectRecordsMapper homeworkCorrectRecordsMapper;
    @Resource
    private HomeworkPaperMapper homeworkPaperMapper;

    @Resource
    private HomeworkQuestionMapper homeworkQuestionMapper;

    @Resource
    private HomeworkRecordMapper homeworkRecordMapper;

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
        PageHelper.startPage( page, limit);
        request.setAttribute("testPaperList",new PageInfo<>(homeworkPaperMapper.selectHomeworkPaperAndUserList(userid)));
        return "admin/homeworkPaperList";
    }
    @PostMapping("/add")
    @ResponseBody
    public APIResponse add(@RequestParam String name,@RequestParam int uid){
        try{
            if (ObjectUtils.isEmpty(homeworkPaperMapper.select(new HomeworkPaper().setName(name)))) {
                homeworkPaperMapper.insert(new HomeworkPaper().setCreateTime(new Date()).setCreator(uid).setName(name).setState(2));
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
            if (homeworkPaperMapper.selectOne(new HomeworkPaper().setId(id)).getState().equals(2)) {
                homeworkPaperMapper.deleteByPrimaryKey(id);
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

            homeworkPaperMapper.updateByPrimaryKeySelective(new HomeworkPaper().setId(id).setState(1));
        }catch (Exception e){
            log.error(e);
            return APIResponse.fail("Service exception");
        }
        return APIResponse.success();
    }
    @GetMapping("/preview")
    public String preview(HttpServletRequest request, @RequestParam Integer testPaperId) {
        //Multiplechoicequestions
        List<HomeworkQuestion> d = homeworkQuestionMapper.select(new HomeworkQuestion().setTestPaperId(testPaperId).setType(1));
        if(!ObjectUtils.isEmpty(d)) {
            Integer dSum = d.stream().map(HomeworkQuestion::getScore).reduce(Integer::sum).get();
            request.setAttribute("d", d.stream().sorted(Comparator.comparingInt(HomeworkQuestion::getSerialNum)).collect(Collectors.toList()));
            request.setAttribute("dSum", dSum);
        }else {
            request.setAttribute("d", new ArrayList<>());
            request.setAttribute("dSum", 0);
        }
        //Shortanswerquestions
        List<HomeworkQuestion> j = homeworkQuestionMapper.select(new HomeworkQuestion().setTestPaperId(testPaperId).setType(2));
        if(!ObjectUtils.isEmpty(j)) {
            Integer jSum = j.stream().map(HomeworkQuestion::getScore).reduce(Integer::sum).get();
            request.setAttribute("j", j.stream().sorted(Comparator.comparingInt(HomeworkQuestion::getSerialNum)).collect(Collectors.toList()));
            request.setAttribute("jSum", jSum);
        }else {
            request.setAttribute("j", new ArrayList<>());
            request.setAttribute("jSum", 0);
        }

        request.setAttribute("uQ", homeworkQuestionMapper.select(new HomeworkQuestion().setTestPaperId(testPaperId)));
        request.setAttribute("testPaper",homeworkPaperMapper.selectOne(new HomeworkPaper().setId(testPaperId)));
        return "admin/previewHomeworkPaper";
    }

}
