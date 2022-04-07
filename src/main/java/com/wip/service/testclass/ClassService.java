package com.wip.service.testclass;


import com.github.pagehelper.PageInfo;
import com.wip.model.TestClass;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


public interface ClassService {


    public PageInfo<TestClass> getClassPageByPage(int page, int limit);

    public int deleteClassPageById(int id);

    public int addClass(TestClass testClass, MultipartFile multipartFile, HttpServletRequest request);

    public int editSave(TestClass testClass);
}
