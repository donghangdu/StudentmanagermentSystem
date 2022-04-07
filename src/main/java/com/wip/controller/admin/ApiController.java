package com.wip.controller.admin;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.dao.*;
import com.wip.model.*;
import com.wip.utils.APIResponse;
import com.wip.utils.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Api("interface")
@RestController
@RequestMapping("/admin/api")
public class ApiController {
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

    @Resource
    private NewHomeworkPaperMapper newhomeworkRecordMapper;

    @ApiOperation("Query users")
    @PostMapping(value = "/user/select/{id}")
    public APIResponse select(@ApiParam(name = "id", value = "用户id", required = true) @PathVariable Integer id) {
        TestUser testUser1 = new TestUser();
        testUser1.setUserid(id);
        List<TestUser> testUser = testUserMapper.select(testUser1);
        return APIResponse.success(testUser);
    }

    @ApiOperation("")
    @PostMapping(value = "/class/select/list")
    public APIResponse classSelectList(
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @ApiParam(name = "limit", value = "QPPage", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "15") int limit

    ) {
        PageHelper.startPage(page, limit);
        List<TestClass> testClasses = testClassMapper.selectAll();
        return APIResponse.success(new PageInfo<>(testClasses));

    }
   /* @ApiOperation("")
    @PostMapping(value = "/class/select/list")
    public APIResponse classSelectList(
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @ApiParam(name = "limit", value = "QPPage", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "15") int limit

    ) {
        PageHelper.startPage(page, limit);
        List<TestClass> testClasses = testClassMapper.selectAll();
        List<TestUser> teacher = testUserMapper.selectAll();
        List<TestClassAndTeachers> result = new LinkedList<>();
        TestClassAndTeachers testClassAndTeachers = new TestClassAndTeachers();
        for (int i = 0;i<testClasses.size();i++){
            for(int j=0;j<teacher.size();j++){
                if(testClasses.get(i).getId()==Integer.parseInt(teacher.get(j).getClassid())){
                    testClassAndTeachers.setId(testClasses.get(i).getId());
                    testClassAndTeachers.setClassname(testClasses.get(i).getClassname());
                    testClassAndTeachers.setComment(testClasses.get(i).getComment());
                    testClassAndTeachers.setUsername(teacher.get(j).getUsername());
                    result.add(i,testClassAndTeachers);
                }
            }
        }
        return APIResponse.success(new PageInfo<>(result));

    }*/
    @ApiOperation("")
    @PostMapping(value = "/class/select/selectTeacher")
    public APIResponse selectTeacher(
            @ApiParam(name = "classid", value = "classid", required = false)
            @RequestParam(name = "classid", required = false, defaultValue = "15") String classid,
            @ApiParam(name = "usertype", value = "usertype", required = false)
            @RequestParam(name = "usertype", required = false, defaultValue = "15") int usertype

    ) {
        TestUser testUser1 = new TestUser();
        testUser1.setClassid(classid);
        testUser1.setUsertype(usertype);
        List<TestUser> teacher = testUserMapper.select(testUser1);
        return APIResponse.success(teacher);

    }

    @ApiOperation("")
    @PostMapping(value = "/user/addclass")
    public APIResponse classAdd(


            @ApiParam(name = "userid", value = "userid", required = true) Integer userid, @ApiParam(name = "classid", value = "班级id", required = true) Integer classid) {
        TestUser testUser = testUserMapper.selectByPrimaryKey(userid);
        TestClass testClass = testClassMapper.selectByPrimaryKey(classid);
        if (ObjectUtils.isEmpty(testClass) || ObjectUtils.isEmpty(testUser)) {
            return APIResponse.fail("Binding failed");
        } else {
            testUser.setClassid(String.valueOf(classid));
            testUserMapper.updateByPrimaryKeySelective(testUser);
            return APIResponse.success();
        }
    }

    @ApiOperation("")
    @PostMapping(value = "/content")
    public APIResponse contentList(
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @ApiParam(name = "limit", value = "QPPage", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "15") int limit,
            @ApiParam(name = "userid", value = "userid", required = true)
            @RequestParam(name = "userid", required = true, defaultValue = "1") Integer userid,
            @ApiParam(name = "search", value = "search", required = false)
            @RequestParam(name = "search", required = false) String search
    ) {
        PageHelper.startPage(page, limit);
        if (StringUtils.isNotBlank(search)) {
            search = "%" + search + "%";
        }
        TestUser user = testUserMapper.selectByPrimaryKey(userid);
        Integer classid = Integer.parseInt(user.getClassid());
        List<ContentPercent> contentPercentList = tContentsMapper.selectContentPercent(userid,search,classid);
        return APIResponse.success(new PageInfo<>(contentPercentList));
    }

    @ApiOperation("")
    @PostMapping(value = "/studylogs")
    public APIResponse studyLogs(
            @ApiParam(name = "uid", value = "uid", required = true) Integer uid,
            @ApiParam(name = "articleid", value = "articleid", required = true) Integer articleid,
            @ApiParam(name = "percent", value = "percent", required = true) Integer percent
    ) {
        try {
            TestStudylogs testStudylogs = new TestStudylogs().setUid(uid).setArticleid(articleid);
            List<TestStudylogs> testStudylogsList = studylogsMapper.select(testStudylogs);
            System.out.println("=================================" + testStudylogsList.size());
            if (testStudylogsList.size() == 0) {
                //此用户没有当前没章的学习记录
                int result = studylogsMapper.insert(testStudylogs.setPercent(percent));
                if (result == 1) {
                    return APIResponse.success("The learning progress of the current article has been entered");
                } else {
                    return APIResponse.fail("The learning progress of the current article has been entered");
                }
            } else {
                Example example = new Example(TestStudylogs.class);
                testStudylogsList.get(0).getPercent();
                if (testStudylogsList.get(0).getPercent() > percent) {
                    return APIResponse.fail("You have made greater progress");
                }
                example.createCriteria().andEqualTo("uid", uid).andEqualTo("articleid", articleid);
                int result = studylogsMapper.updateByExampleSelective(new TestStudylogs().setPercent(percent), example);
                if (result == 1) {
                    return APIResponse.success("The learning progress of the current article has been entered");
                } else {
                    return APIResponse.fail("Failed to enter the learning progress of the current article");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return APIResponse.fail("Failed to enter the learning progress of the current article");
        }

    }

    @ApiOperation("")
    @PostMapping(value = "/testPaperList")
    public APIResponse testPaperList(@RequestParam Integer uid, @RequestParam int pageNum, @RequestParam int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return APIResponse.success(new PageInfo<>(testPaperMapper.selectTestPaperAndUserTestRecord(uid)));
    }
    @ApiOperation(" testPaperStatue 3Noexamination 2 Not corrected  1 Corrected")
    @PostMapping(value = "/testPaperDetails")
    public APIResponse testPaperDetails(@RequestParam Integer uid, @RequestParam Integer testPaperId) {

        HashMap<String, Object> res = new HashMap<>();
        //
        TestCorrectRecords testCorrect = testCorrectRecordsMapper.selectOne(new TestCorrectRecords().setTestPaperId(testPaperId).setCreator(uid));
        if (ObjectUtils.isEmpty(testCorrect)) {
            //Noexamination
            //
            List<TestQuestion> d = testQuestionMapper.select(new TestQuestion().setTestPaperId(testPaperId).setType(1));
            if (!ObjectUtils.isEmpty(d)) {
                Integer dSum = d.stream().map(TestQuestion::getScore).reduce(Integer::sum).get();
                res.put("d", d.stream().sorted(Comparator.comparingInt(TestQuestion::getSerialNum)).collect(Collectors.toList()));
                res.put("dSum", dSum);
            } else {
                res.put("d", new ArrayList<>());
                res.put("dSum", 0);
            }
            //
            List<TestQuestion> j = testQuestionMapper.select(new TestQuestion().setTestPaperId(testPaperId).setType(2));
            if (!ObjectUtils.isEmpty(j)) {
                Integer jSum = j.stream().map(TestQuestion::getScore).reduce(Integer::sum).get();
                res.put("j", j.stream().sorted(Comparator.comparingInt(TestQuestion::getSerialNum)).collect(Collectors.toList()));
                res.put("jSum", jSum);
            } else {
                res.put("j", new ArrayList<>());
                res.put("jSum", 0);
            }
            //
            res.put("uQ", testQuestionMapper.select(new TestQuestion().setTestPaperId(testPaperId)));
            res.put("testPaper", testPaperMapper.selectOne(new TestPaper().setId(testPaperId)));
            res.put("testPaperStatue", 3);

        } else if (testCorrect.getCorrectStatus() == null) {
            //Not corrected
            TestCorrectRecords testCorrectRecords = testCorrectRecordsMapper.selectOne(new TestCorrectRecords().setTestPaperId(testPaperId).setCreator(uid));
            //
            List<TestQuestion> d = testQuestionMapper.select(new TestQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(1));
            if (!ObjectUtils.isEmpty(d)) {
                Integer dSum = d.stream().map(TestQuestion::getScore).reduce(Integer::sum).get();
                res.put("d", d.stream().sorted(Comparator.comparingInt(TestQuestion::getSerialNum)).collect(Collectors.toList()));
                res.put("dSum", dSum);
            } else {
                res.put("d", new ArrayList<>());
                res.put("dSum", 0);
            }
            //
            List<TestQuestion> j = testQuestionMapper.select(new TestQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(2));
            if (!ObjectUtils.isEmpty(j)) {
                Integer jSum = j.stream().map(TestQuestion::getScore).reduce(Integer::sum).get();
                res.put("j", j.stream().sorted(Comparator.comparingInt(TestQuestion::getSerialNum)).collect(Collectors.toList()));
                res.put("jSum", jSum);
            } else {
                res.put("j", new ArrayList<>());
                res.put("jSum", 0);
            }
            //
            List<Map<String, Object>> uQ = testCorrectRecordsMapper.selectUserRecordAndQuestion(testCorrectRecords.getId());
            res.put("uQ", uQ);
            res.put("testCorrectRecordsId", testCorrectRecords.getId());
            res.put("testPaperId", testCorrectRecords.getTestPaperId());
            res.put("testPaperStatue", 2);
        }else {
            //Corrected
            TestCorrectRecords testCorrectRecords = testCorrectRecordsMapper.selectOne(new TestCorrectRecords().setTestPaperId(testPaperId).setCreator(uid));
            //
            List<TestQuestion> d = testQuestionMapper.select(new TestQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(1));
            if (!ObjectUtils.isEmpty(d)) {
                Integer dSum = d.stream().map(TestQuestion::getScore).reduce(Integer::sum).get();
                res.put("d", d.stream().sorted(Comparator.comparingInt(TestQuestion::getSerialNum)).collect(Collectors.toList()));
                res.put("dSum", dSum);
            } else {
                res.put("d", new ArrayList<>());
                res.put("dSum", 0);
            }
            //
            List<TestQuestion> j = testQuestionMapper.select(new TestQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(2));
            if (!ObjectUtils.isEmpty(j)) {
                Integer jSum = j.stream().map(TestQuestion::getScore).reduce(Integer::sum).get();
                res.put("j", j.stream().sorted(Comparator.comparingInt(TestQuestion::getSerialNum)).collect(Collectors.toList()));
                res.put("jSum", jSum);
            } else {
                res.put("j", new ArrayList<>());
                res.put("jSum", 0);
            }
            //
            List<Map<String, Object>> uQ = testCorrectRecordsMapper.selectUserRecordAndQuestion(testCorrectRecords.getId());
            res.put("uQ", uQ);
            res.put("testCorrectRecordsId", testCorrectRecords.getId());
            res.put("testPaperId", testCorrectRecords.getTestPaperId());
            res.put("testPaperStatue", 1);
        }
        return APIResponse.success(res);
    }

    @ApiOperation("")
    @PostMapping(value = "/submitTestPaper")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse submitTestPaper(@ApiParam("{\"testPaperId\":\"testid\",\"uid\":\"用户id\",\"list\":{\"testQuestionId\":\"题目id\",\"answer\":\"答案\"}}") @RequestBody Map<String, Object> param) {
        Integer testPaperId = (Integer) param.get("testPaperId");
        Integer uid = (Integer) param.get("uid");
        if (!ObjectUtils.isEmpty(testCorrectRecordsMapper.select(new TestCorrectRecords().setCreator(uid).setTestPaperId(testPaperId)))) {
            return APIResponse.fail("The test paper has been submitted, and there is no need to repeat the submission");
        }
        List<TestRecord> list = ConvertUtil.convert((List<TestRecord>) param.get("list"), TestRecord.class);
        if (ObjectUtils.isEmpty(list)) {
            return APIResponse.success();
        }
        testRecordMapper.delete(new TestRecord().setTestPaperId(testPaperId).setCreator(uid));
        testCorrectRecordsMapper.insertSelective(new TestCorrectRecords().setCreator(uid).setTestPaperId(testPaperId).setCreateTime(new Date()));
        list.stream().forEach(testRecord -> testRecordMapper.insertSelective(testRecord.setCreator(uid).setTestPaperId(testPaperId)));

        return APIResponse.success();
    }


    @ApiOperation("")
    @PostMapping(value = "/submitHomeWork")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse submitHomeWork(@ApiParam("{\"testPaperId\":\"Job ID\",\"uid\":\"用户id\",\"list\":{\"testQuestionId\":\"题目id\",\"answer\":\"答案\"}}") @RequestBody Map<String, Object> param) {
        Integer testPaperId = (Integer) param.get("testPaperId");
        Integer uid = (Integer) param.get("uid");
        if (!ObjectUtils.isEmpty(homeworkCorrectRecordsMapper.select(new HomeworkCorrectRecords().setCreator(uid).setTestPaperId(testPaperId)))) {
            return APIResponse.fail("The job has been submitted. There is no need to repeat the submit");
        }
        List<HomeworkRecord> list = ConvertUtil.convert((List) param.get("list"), HomeworkRecord.class);
        if (ObjectUtils.isEmpty(list)) {
            return APIResponse.success();
        }
        homeworkRecordMapper.delete(new HomeworkRecord().setTestPaperId(testPaperId).setCreator(uid));
        homeworkCorrectRecordsMapper.insertSelective(new HomeworkCorrectRecords().setCreator(uid).setTestPaperId(testPaperId).setCreateTime(new Date()));
        list.stream().forEach(testRecord -> homeworkRecordMapper.insertSelective(testRecord.setCreator(uid).setTestPaperId(testPaperId)));

        return APIResponse.success();
    }

    @ApiOperation("")
    @PostMapping(value = "/saveHomeWork")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse saveHomeWork(@ApiParam("{\"testPaperId\":\"testid\",\"uid\":\"用户id\",\"list\":{\"testQuestionId\":\"题目id\",\"answer\":\"答案\"}}") @RequestBody Map<String, Object> param) {
        Integer testPaperId = (Integer) param.get("testPaperId");
        Integer uid = (Integer) param.get("uid");
        List<HomeworkRecord> list = ConvertUtil.convert((List) param.get("list"), HomeworkRecord.class);
        if (ObjectUtils.isEmpty(list)) {
            return APIResponse.success();
        }
        homeworkRecordMapper.delete(new HomeworkRecord().setTestPaperId(testPaperId).setCreator(uid));
        list.stream().forEach(testRecord -> homeworkRecordMapper.insertSelective(testRecord.setCreator(uid).setTestPaperId(testPaperId)));

        return APIResponse.success();
    }


    @ApiOperation("")
    @PostMapping(value = "/homeWorkList")
    public APIResponse homeWorkList(@RequestParam Integer uid, @RequestParam int pageNum, @RequestParam int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return APIResponse.success(new PageInfo<>(homeworkPaperMapper.selectHomeworkAndUserHomeWorkRecord(uid)));
    }


    @ApiOperation(" homeworkStatue 3未submit  2 Not corrected  1 Corrected")
    @PostMapping(value = "/homeWorkDetails")
    public APIResponse homeWorkDetails(@RequestParam Integer uid, @RequestParam Integer testPaperId) {

        HashMap<String, Object> res = new HashMap<>();
        //Operation status
        HomeworkCorrectRecords testCorrect = homeworkCorrectRecordsMapper.selectOne(new HomeworkCorrectRecords().setTestPaperId(testPaperId).setCreator(uid));
        if (ObjectUtils.isEmpty(testCorrect)) {
            //Noexamination
            //
            List<HomeworkQuestion> d = homeworkQuestionMapper.select(new HomeworkQuestion().setTestPaperId(testPaperId).setType(1));
            if (!ObjectUtils.isEmpty(d)) {
                Integer dSum = d.stream().map(HomeworkQuestion::getScore).reduce(Integer::sum).get();
                res.put("d", d.stream().sorted(Comparator.comparingInt(HomeworkQuestion::getSerialNum)).collect(Collectors.toList()));
                res.put("dSum", dSum);
            } else {
                res.put("d", new ArrayList<>());
                res.put("dSum", 0);
            }
            //
            List<HomeworkQuestion> j = homeworkQuestionMapper.select(new HomeworkQuestion().setTestPaperId(testPaperId).setType(2));
            if (!ObjectUtils.isEmpty(j)) {
                Integer jSum = j.stream().map(HomeworkQuestion::getScore).reduce(Integer::sum).get();
                res.put("j", j.stream().sorted(Comparator.comparingInt(HomeworkQuestion::getSerialNum)).collect(Collectors.toList()));
                res.put("jSum", jSum);
            } else {
                res.put("j", new ArrayList<>());
                res.put("jSum", 0);
            }
            //
            res.put("uQ", homeworkQuestionMapper.selectQuestionAndUserAns(uid, testPaperId));
            res.put("homeWorkPaper", homeworkPaperMapper.selectOne(new HomeworkPaper().setId(testPaperId)));
            res.put("homeWorkPaperStatue", 3);

        } else if (testCorrect.getCorrectStatus()==null) {
            //Not corrected
            HomeworkCorrectRecords testCorrectRecords = homeworkCorrectRecordsMapper.selectOne(new HomeworkCorrectRecords().setTestPaperId(testPaperId).setCreator(uid));
            //Multiplechoicequestions
            List<HomeworkQuestion> d = homeworkQuestionMapper.select(new HomeworkQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(1));
            if (!ObjectUtils.isEmpty(d)) {
                Integer dSum = d.stream().map(HomeworkQuestion::getScore).reduce(Integer::sum).get();
                res.put("d", d.stream().sorted(Comparator.comparingInt(HomeworkQuestion::getSerialNum)).collect(Collectors.toList()));
                res.put("dSum", dSum);
            } else {
                res.put("d", new ArrayList<>());
                res.put("dSum", 0);
            }
            //
            List<HomeworkQuestion> j = homeworkQuestionMapper.select(new HomeworkQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(2));
            if (!ObjectUtils.isEmpty(j)) {
                Integer jSum = j.stream().map(HomeworkQuestion::getScore).reduce(Integer::sum).get();
                res.put("j", j.stream().sorted(Comparator.comparingInt(HomeworkQuestion::getSerialNum)).collect(Collectors.toList()));
                res.put("jSum", jSum);
            } else {
                res.put("j", new ArrayList<>());
                res.put("jSum", 0);
            }
            //
            List<Map<String, Object>> uQ = homeworkCorrectRecordsMapper.selectUserRecordAndQuestion(testCorrectRecords.getId());
            res.put("uQ", uQ);
            res.put("homeWorkCorrectRecordsId", testCorrectRecords.getId());
            res.put("testPaperId", testCorrectRecords.getTestPaperId());
            res.put("homeWorkPaperStatue", 2);
        } else {
            //Corrected
            HomeworkCorrectRecords testCorrectRecords = homeworkCorrectRecordsMapper.selectOne(new HomeworkCorrectRecords().setTestPaperId(testPaperId).setCreator(uid));
            //
            List<HomeworkQuestion> d = homeworkQuestionMapper.select(new HomeworkQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(1));
            if (!ObjectUtils.isEmpty(d)) {
                Integer dSum = d.stream().map(HomeworkQuestion::getScore).reduce(Integer::sum).get();
                res.put("d", d.stream().sorted(Comparator.comparingInt(HomeworkQuestion::getSerialNum)).collect(Collectors.toList()));
                res.put("dSum", dSum);
            } else {
                res.put("d", new ArrayList<>());
                res.put("dSum", 0);
            }
            //
            List<HomeworkQuestion> j = homeworkQuestionMapper.select(new HomeworkQuestion().setTestPaperId(testCorrectRecords.getTestPaperId()).setType(2));
            if (!ObjectUtils.isEmpty(j)) {
                Integer jSum = j.stream().map(HomeworkQuestion::getScore).reduce(Integer::sum).get();
                res.put("j", j.stream().sorted(Comparator.comparingInt(HomeworkQuestion::getSerialNum)).collect(Collectors.toList()));
                res.put("jSum", jSum);
            } else {
                res.put("j", new ArrayList<>());
                res.put("jSum", 0);
            }
            //
            List<Map<String, Object>> uQ = homeworkCorrectRecordsMapper.selectUserRecordAndQuestion(testCorrectRecords.getId());
            res.put("uQ", uQ);
            res.put("homeWorkCorrectRecordsId", testCorrectRecords.getId());
            res.put("homeWorkPaperId", testCorrectRecords.getTestPaperId());
            res.put("homeWorkPaperStatue", 1);
        }
        return APIResponse.success(res);
    }
    @ApiOperation("queryWorkByclassId")
    @PostMapping(value = "/queryWorkByclassId")
    public APIResponse queryWorkByclassId(@RequestParam Integer classid,@RequestParam Integer userid, @RequestParam int pageNum, @RequestParam int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return APIResponse.success(new PageInfo<>(newhomeworkRecordMapper.selectNewHomeWorkListByClassid(classid,userid)));
    }

}
