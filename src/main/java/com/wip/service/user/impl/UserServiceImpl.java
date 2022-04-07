
package com.wip.service.user.impl;

import com.wip.constant.ErrorConstant;
import com.wip.dao.TestUserMapper;
import com.wip.dao.UserDao;
import com.wip.exception.BusinessException;
import com.wip.model.TestUser;
import com.wip.model.UserDomain;
import com.wip.service.user.UserService;
import com.wip.utils.APIResponse;
import com.wip.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;//

    @Resource
    private TestUserMapper testUserMapper;

    @Override
    public TestUser login(String username, String password,Integer userType) {

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password))
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_IS_EMPTY);
        TestUser testUser = new TestUser();
        testUser.setUsername(username);
        testUser.setUsertype(userType);
        testUser.setUserpwd(password);
        testUser.setUserstate(1);

        TestUser user =testUserMapper.selectOne(testUser);
        if (null == user)
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_ERROR);
        return user;
    }

    @Override
    public TestUser getUserInfoById(Integer uid) {
        TestUser testUser = new TestUser();
        testUser.setUserid(uid);
        return testUserMapper.selectOne(testUser);
    }

    //
    @Transactional
    @Override
    public int updateUserInfo(TestUser user) {
        if (null == user.getUserid())
            throw BusinessException.withErrorCode("User number cannot be empty");
        return testUserMapper.updateByPrimaryKeySelective(user);
    }
}
