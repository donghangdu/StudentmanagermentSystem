package com.wip.controller.admin;

import com.wip.constant.ErrorConstant;
import com.wip.constant.LogActions;
import com.wip.constant.WebConst;
import com.wip.controller.BaseController;
import com.wip.exception.BusinessException;
import com.wip.model.TestUser;
import com.wip.model.UserDomain;
import com.wip.service.log.LogService;
import com.wip.service.user.UserService;
import com.wip.utils.APIResponse;
import com.wip.utils.GsonUtils;
import com.wip.utils.TaleUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Api("Login related interfaces")
@Controller
@RequestMapping("/admin")
public class AuthController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;


    @ApiOperation("Jump to landing page")
    @GetMapping(value = "/login")
    public String login() {
        return "admin/login";
    }


    @ApiOperation("login")
    @PostMapping(value = "/login")
    @ResponseBody
    public APIResponse toLogin(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(name = "username", value = "username", required = true)
            @RequestParam(name = "username", required = true)
            String username,
            @ApiParam(name = "password", value = "username", required = true)
            @RequestParam(name = "password", required = true)
            String password,
            @ApiParam(name = "remember_me", value = "Remember me", required = false)
            @RequestParam(name = "remember_me", required = false)
            String remember_me,
            @RequestParam
            String userType
    ) {
        TestUser user =new TestUser();
        Integer error_count = cache.get("login_error_count");
        try {
            //
            Integer type;
            if(StringUtils.equals(userType,"Admin")){
                type=2;
            }else   if(StringUtils.equals(userType,"Teacher")){
                type =1;
            }else   if(StringUtils.equals(userType,"Student")){
                type =0;
            }else {
                throw BusinessException.withErrorCode("User type error");
            }
            TestUser userInfo = userService.login(username, password,type);
            //
            request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, userInfo);
            request.getSession().setAttribute("userType", type);

            //
            if (StringUtils.isNotBlank(remember_me)) {
                TaleUtils.setCookie(response, userInfo.getUserid());
            }
            response.addCookie(new Cookie("uid",userInfo.getUserid()+""));

            user=userInfo;
            //
            logService.addLog(LogActions.LOGIN.getAction(), userInfo.getUsername()+"user", request.getRemoteAddr(), userInfo.getUserid());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            error_count = null == error_count ? 1 : error_count + 1;
            if (error_count > 3) {
                return APIResponse.fail("Your password has been entered incorrectly for more than 3 times. Please try after 10 minutes");
            }
            System.out.println(error_count);
            //
            cache.set("login_error_count", error_count, 10 * 60);
            String msg = "Login failed";
            if (e instanceof BusinessException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg,e);
            }
            return APIResponse.fail(msg);
        }
        //
        return APIResponse.success(user);
    }

    @RequestMapping(value = "/logout")
    public void logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        //
        session.removeAttribute(WebConst.LOGIN_SESSION_KEY);
        //
        Cookie cookie = new Cookie(WebConst.USER_IN_COOKIE, "");
        cookie.setValue(null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.addCookie(new Cookie("uid",""));
        try {
            // Jump to login page
            response.addHeader("Access-Control-Expose-Headers", "REDIRECT,CONTEXTPATH");
            response.addHeader("REDIRECT", "REDIRECT");
            response.addHeader("CONTEXTPATH", request.getContextPath() + "/admin/login");
            response.sendRedirect("/admin/login");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Logout failed",e);
        }
    }



}
