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
import tk.mybatis.mapper.weekend.Weekend;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Log4j
@Api("Paper management")
@Controller
@RequestMapping("admin/homeworkQuestion")
public class HomeWorkQuestionController extends BaseController {

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
        request.setAttribute("testQuestionList", new PageInfo<>(homeworkQuestionMapper.selectHomeworkQuestionAndUserList(new HashMap<String,Object>(){{put("testPaperId",testPaperId);}})));
        request.setAttribute("testPaperId", testPaperId);
        request.setAttribute("testPaper", homeworkPaperMapper.selectOne(new HomeworkPaper().setId(testPaperId)));
        return "admin/homeworkQuestionList";
    }


    @GetMapping("/add")
    public String add(HttpServletRequest request, @RequestParam Integer testPaperId) {
        request.setAttribute("testPaper", homeworkPaperMapper.selectOne(new HomeworkPaper().setId(testPaperId)));
        request.setAttribute("testQuestion", new TestQuestion());
        request.setAttribute("operation","Add test questions");
        return "admin/homeworkQuestionSave";
    }

    @GetMapping("/update")
    public String update(HttpServletRequest request, @RequestParam Integer id) {
        request.setAttribute("testQuestion", homeworkQuestionMapper.selectOne(new HomeworkQuestion().setId(id)));
        request.setAttribute("testPaper", homeworkPaperMapper.selectOne(new HomeworkPaper().setId(homeworkQuestionMapper.selectOne(new HomeworkQuestion().setId(id)).getTestPaperId())));
        request.setAttribute("operation","Revise test questions");
        return "admin/homeworkQuestionSave";
    }

    @PostMapping("/save")
    @ResponseBody
    public APIResponse save(@RequestBody HomeworkQuestion testQuestion) {
        try {
           int all =homeworkQuestionMapper.selectCount(new HomeworkQuestion().setTestPaperId(testQuestion.getTestPaperId()));
            if(all>=60){
                return APIResponse.fail("Add up to 60 questions to each paper！");
            }

            testQuestion.setCreateTime(new Date());
            if (testQuestion.getType().equals(2)) {
                testQuestion.setC(null).setA(null).setB(null).setD(null).setAnswer(null);
            }
            if (ObjectUtils.isEmpty(testQuestion.getId())) {
                Weekend<HomeworkQuestion> weekend = Weekend.of(HomeworkQuestion.class);
                weekend.setOrderByClause(" serial_num desc");
                weekend.weekendCriteria().andEqualTo(HomeworkQuestion::getTestPaperId,testQuestion.getTestPaperId());
                List<HomeworkQuestion> maxSer = homeworkQuestionMapper.selectByExample(weekend);
                if(ObjectUtils.isEmpty(maxSer)){
                    testQuestion.setSerialNum(1);
                }else {
                    testQuestion.setSerialNum(maxSer.get(0).getSerialNum()+1);
                }
                homeworkQuestionMapper.insert(testQuestion);
            } else {
                homeworkQuestionMapper.updateByPrimaryKey(testQuestion);
            }
        } catch (Exception e) {
            log.error(e);
            return APIResponse.fail("Service exception");
        }
        return APIResponse.success();
    }


    @PostMapping("/delete")
    @ResponseBody
    public APIResponse delete(@RequestParam Integer id) {
        try {
            Integer test_paper_id =homeworkQuestionMapper.selectByPrimaryKey(id).getTestPaperId();
            Integer serial_num =homeworkQuestionMapper.selectByPrimaryKey(id).getSerialNum();
            HomeworkQuestion homeworkQuestion = new HomeworkQuestion();
            homeworkQuestion.setTestPaperId(test_paper_id);
            List<HomeworkQuestion> list = homeworkQuestionMapper.select(homeworkQuestion);
            homeworkQuestionMapper.deleteByPrimaryKey(id);
            for(int i =0;i<=list.size()-1;i++){
                if(list.get(i).getSerialNum()>serial_num){
                    HomeworkQuestion updatehomeworkQuestion = new HomeworkQuestion();
                    updatehomeworkQuestion.setId(list.get(i).getId());
                    updatehomeworkQuestion.setTestPaperId(list.get(i).getTestPaperId());
                    updatehomeworkQuestion.setType(list.get(i).getType());
                    updatehomeworkQuestion.setA(list.get(i).getA());
                    updatehomeworkQuestion.setB(list.get(i).getB());
                    updatehomeworkQuestion.setC(list.get(i).getC());
                    updatehomeworkQuestion.setD(list.get(i).getD());
                    updatehomeworkQuestion.setAnswer(list.get(i).getAnswer());
                    updatehomeworkQuestion.setCreateTime(list.get(i).getCreateTime());
                    updatehomeworkQuestion.setCreator(list.get(i).getCreator());
                    updatehomeworkQuestion.setName(list.get(i).getName());
                    updatehomeworkQuestion.setScore(list.get(i).getScore());
                    updatehomeworkQuestion.setSerialNum(list.get(i).getSerialNum()-1);
                    homeworkQuestionMapper.updateByPrimaryKey(updatehomeworkQuestion);
                }
            }
        } catch (Exception e) {
            return APIResponse.fail("Service exception");
        }
        return APIResponse.success();
    }


}
