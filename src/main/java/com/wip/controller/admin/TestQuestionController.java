package com.wip.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.controller.BaseController;
import com.wip.dao.TestPaperMapper;
import com.wip.dao.TestQuestionMapper;
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
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Log4j
@Api("Paper management")
@Controller
@RequestMapping("admin/testQuestion")
public class TestQuestionController extends BaseController {

    @Resource
    private TestQuestionMapper testQuestionMapper;
    @Resource
    private TestPaperMapper testPaperMapper;

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
        request.setAttribute("testQuestionList", new PageInfo<>(testQuestionMapper.selectTestQuestionAndUserList(new HashMap<String,Object>(){{put("testPaperId",testPaperId);}})));
        request.setAttribute("testPaperId", testPaperId);
        request.setAttribute("testPaper", testPaperMapper.selectOne(new TestPaper().setId(testPaperId)));
        return "admin/testQuestionList";
    }


    @GetMapping("/add")
    public String add(HttpServletRequest request, @RequestParam Integer testPaperId) {
        request.setAttribute("testPaper", testPaperMapper.selectOne(new TestPaper().setId(testPaperId)));
        request.setAttribute("testQuestion", new TestQuestion());
        request.setAttribute("operation","Add test questions");
        return "admin/testQuestionSave";
    }

    @GetMapping("/update")
    public String update(HttpServletRequest request, @RequestParam Integer id) {
        request.setAttribute("testQuestion", testQuestionMapper.selectOne(new TestQuestion().setId(id)));
        request.setAttribute("testPaper", testPaperMapper.selectOne(new TestPaper().setId(testQuestionMapper.selectOne(new TestQuestion().setId(id)).getTestPaperId())));
        request.setAttribute("operation","Revise test questions");
        return "admin/testQuestionSave";
    }

    @PostMapping("/save")
    @ResponseBody
    public APIResponse save(@RequestBody TestQuestion testQuestion) {
        try {
           int all =testQuestionMapper.selectCount(new TestQuestion().setTestPaperId(testQuestion.getTestPaperId()));
            if(all>=60){
                return APIResponse.fail("Add up to 60 questions to each paper！");
            }

            testQuestion.setCreateTime(new Date());
            if (testQuestion.getType().equals(2)) {
                testQuestion.setC(null).setA(null).setB(null).setD(null).setAnswer(null);
            }
            if (ObjectUtils.isEmpty(testQuestion.getId())) {
                Weekend<TestQuestion> weekend = Weekend.of(TestQuestion.class);
                weekend.setOrderByClause(" serial_num desc");
                weekend.weekendCriteria().andEqualTo(TestQuestion::getTestPaperId,testQuestion.getTestPaperId());
                List<TestQuestion> maxSer = testQuestionMapper.selectByExample(weekend);
                if(ObjectUtils.isEmpty(maxSer)){
                    testQuestion.setSerialNum(1);
                }else {
                    testQuestion.setSerialNum(maxSer.get(0).getSerialNum()+1);
                }
                testQuestionMapper.insert(testQuestion);
            } else {
                testQuestionMapper.updateByPrimaryKey(testQuestion);
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
            Integer test_paper_id =testQuestionMapper.selectByPrimaryKey(id).getTestPaperId();
            Integer serial_num =testQuestionMapper.selectByPrimaryKey(id).getSerialNum();
            TestQuestion testQuestion = new TestQuestion();
            testQuestion.setTestPaperId(test_paper_id);
            List<TestQuestion> list = testQuestionMapper.select(testQuestion);
            testQuestionMapper.deleteByPrimaryKey(id);
            for(int i =0;i<=list.size()-1;i++){
                if(list.get(i).getSerialNum()>serial_num){
                    TestQuestion updatetestQuestion = new TestQuestion();
                    updatetestQuestion.setId(list.get(i).getId());
                    updatetestQuestion.setTestPaperId(list.get(i).getTestPaperId());
                    updatetestQuestion.setType(list.get(i).getType());
                    updatetestQuestion.setA(list.get(i).getA());
                    updatetestQuestion.setB(list.get(i).getB());
                    updatetestQuestion.setC(list.get(i).getC());
                    updatetestQuestion.setD(list.get(i).getD());
                    updatetestQuestion.setAnswer(list.get(i).getAnswer());
                    updatetestQuestion.setCreateTime(list.get(i).getCreateTime());
                    updatetestQuestion.setCreator(list.get(i).getCreator());
                    updatetestQuestion.setName(list.get(i).getName());
                    updatetestQuestion.setScore(list.get(i).getScore());
                    updatetestQuestion.setSerialNum(list.get(i).getSerialNum()-1);
                    testQuestionMapper.updateByPrimaryKey(updatetestQuestion);
                }
            }
        } catch (Exception e) {
            return APIResponse.fail("Service exception");
        }
        return APIResponse.success();
    }


}
