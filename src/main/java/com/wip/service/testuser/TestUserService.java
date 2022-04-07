package com.wip.service.testuser;

import com.github.pagehelper.PageInfo;
import com.wip.model.TestUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface TestUserService {


    public PageInfo<Map<String,Object>> getUserPageByPage(int page, int limit, Integer type);

    public int deleteUserPageById(int id);

    public int addUser(TestUser testUser, HttpServletRequest request);

    public int editSave(TestUser testUser);

    public TestUser selectById(TestUser testUser);

}
