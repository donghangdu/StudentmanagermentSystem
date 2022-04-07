package com.wip.service.testuser;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.dao.*;
import com.wip.model.*;
import org.aspectj.weaver.ast.Test;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public class TestUserServiceImpl implements TestUserService {

    @Resource
    TestUserMapper testUserMapper;
    @Resource
    HomeworkPaperMapper homeworkPaperMapper;
    @Resource
    TestPaperMapper testPaperMapper;
    @Resource
    NewknowledgePointPaperMapper newknowledgePointMapper;
    @Resource
    private NewHomeworkPaperMapper newHomeworkPaperMapper;
    @Resource
    private HomeworkQuestionMapper homeworkQuestionMapper;

    @Override
    public PageInfo<Map<String,Object>> getUserPageByPage(int page, int limit,Integer type) {
        PageHelper.startPage(page,limit);
        List<Map<String,Object>> testUsers = testUserMapper.selectAllUserWithClass(type);
        return new PageInfo<Map<String,Object>>(testUsers);
    }

    @Override
    public int deleteUserPageById(int id) {
        TestUser testUser=new TestUser();
        testUser.setUserid(id);
        int result=testUserMapper.delete(testUser);
        HomeworkPaper homeworkPaper = new HomeworkPaper();
        homeworkPaper.setCreator(id);
        int deleteHomePaperNum = homeworkPaperMapper.delete(homeworkPaper);
        TestPaper testPaper = new TestPaper();
        testPaper.setCreator(id);
        int deleteTestPaperNum = testPaperMapper.delete(testPaper);
        NewKnowledgePointList newKnowledgePointList = new NewKnowledgePointList();
        newKnowledgePointList.setAuthorId(id);
        int deleteNewKnowledge = newknowledgePointMapper.delete(newKnowledgePointList);
        NewHomeworkPaper newHomeworkPaper = new NewHomeworkPaper();
        newHomeworkPaper.setAuthorid(id);
        int deleteNewHomeworkPaperNum = newHomeworkPaperMapper.delete(newHomeworkPaper);
        HomeworkQuestion homeworkQuestion = new HomeworkQuestion();
        homeworkQuestion.setCreator(id);
        int deleteHomeworkQuestion = homeworkQuestionMapper.delete(homeworkQuestion);
        return result;
    }

    @Override
    public int addUser(TestUser testUser,HttpServletRequest request) {

//        String fileName=multipartFile.getOriginalFilename();
//        String path = "src/main/resources/static/upload/imgs";
//        System.out.println("save地址："+path);
//        //项目内地址
//        File dest = new File((new File(path ).getAbsolutePath()+File.separator+fileName));
//        if (!dest.getParentFile().exists()) {
//            dest.getParentFile().mkdirs();
//        }
//        try {
//            multipartFile.transferTo(dest);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //访问地址
//        String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() +"/upload/imgs/";
//        String filename = returnUrl + fileName;
//        System.out.println(filename);
        testUser.setUserstate(1);
        //testUser.setHeadimage(filename);
        int b=testUserMapper.insertSelective(testUser);
        return b;
    }

    @Override
    public int editSave(TestUser testUser) {
        return testUserMapper.updateByPrimaryKeySelective(testUser);
    }

    @Override
    public TestUser selectById(TestUser testUser) {
        return testUserMapper.selectByPrimaryKey(testUser.getUserid());
    }
}
