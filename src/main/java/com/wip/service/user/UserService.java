
package com.wip.service.user;

import com.wip.model.TestUser;
import com.wip.model.UserDomain;

/**
 *
 */
public interface UserService {

    /**
     * User login
     * @param username  username
     * @param password
     * @return
     */
    TestUser login(String username, String password, Integer userType);

    /**
     *
     * @param uid
     * @return
     */
    TestUser getUserInfoById(Integer uid);

    /**
     *
     * @param user
     * @return
     */
    int updateUserInfo(TestUser user);

}
