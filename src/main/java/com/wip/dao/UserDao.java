package com.wip.dao;

import com.wip.model.UserDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 */
@Mapper
public interface UserDao {

    /**
     *
     * @param username  username
     * @param password
     * @return
     */
    UserDomain getUserInfoByCond(@Param("username") String username, @Param("password") String password);

    /**
     *
     * @param uid
     * @return
     */
    UserDomain getUserInfoById(Integer uid);

    /**
     *
     * @param user
     * @return
     */
    int updateUserInfo(UserDomain user);

}
