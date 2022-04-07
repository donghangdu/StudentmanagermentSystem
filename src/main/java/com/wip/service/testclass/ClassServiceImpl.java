package com.wip.service.testclass;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.dao.*;
import com.wip.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
public class ClassServiceImpl implements  ClassService{

    @Resource
    private TestClassMapper testClassMapper;
    @Resource
    private TestUserMapper testUserMapper;
    @Resource
    private NewHomeworkPaperMapper newHomeworkPaperMapper;
    @Resource
    NewknowledgePointPaperMapper newknowledgePointMapper;

    @Override
    public PageInfo<TestClass> getClassPageByPage(int page, int limit) {
        PageHelper.startPage(page,limit);
        List<TestClass> tClasses = testClassMapper.selectAll();
        return new PageInfo<>(tClasses);
    }

    @Override
    public int deleteClassPageById(int id) {
        TestClass testClass=new TestClass();
        testClass.setId(id);
        int result=testClassMapper.delete(testClass);
        int updateUserNum = testUserMapper.updateUserbyClassid( Integer.toString(id));
        NewHomeworkPaper newHomeworkPaper = new NewHomeworkPaper();
        newHomeworkPaper.setClassid(id);
        int deleteNewHomeworkPaperNum = newHomeworkPaperMapper.delete(newHomeworkPaper);
        NewKnowledgePointList newKnowledgePointList = new NewKnowledgePointList();
        newKnowledgePointList.setClassid(id);
        int deleteNewKnowledge = newknowledgePointMapper.delete(newKnowledgePointList);
        return result;
    }

    @Override
    public int addClass(TestClass testClass, MultipartFile multipartFile, HttpServletRequest request) {

        String fileName=multipartFile.getOriginalFilename();
        String path = "src/main/resources/static/upload/imgs";
        String fileType=fileName.substring(fileName.lastIndexOf("."));
        fileName= UUID.randomUUID().toString()+fileType;
        System.out.println("saveip："+path);
        //项目内地址
        File dest = new File((new File(path ).getAbsolutePath()+File.separator+fileName));
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            multipartFile.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //访问地址
        String returnUrl = "/upload/imgs/";
        String filename = returnUrl + fileName;
        System.out.println(filename);
        testClass.setStatus("1");
        testClass.setHeadimage(filename);
        int b=testClassMapper.insert(testClass);
        return b;
    }

    @Override
    public int editSave(TestClass testClass) {
        return testClassMapper.updateByPrimaryKeySelective(testClass);
    }

}
