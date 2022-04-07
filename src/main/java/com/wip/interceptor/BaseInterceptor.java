package com.wip.interceptor;

import com.wip.constant.Types;
import com.wip.constant.WebConst;
import com.wip.dto.MetaDto;
import com.wip.dto.StatisticsDto;
import com.wip.model.OptionsDomain;
import com.wip.model.TestUser;
import com.wip.model.UserDomain;
import com.wip.service.meta.MetaService;
import com.wip.service.option.OptionService;
import com.wip.service.site.SiteService;
import com.wip.service.user.UserService;
import com.wip.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseInterceptor.class);
    private static final String USER_AGENT = "User-Agent";


    @Resource
    private UserService userService;

    @Resource
    private OptionService optionService;

    @Resource
    private Commons commons;

    @Resource
    private AdminCommons adminCommons;

    @Resource
    private MetaService metaService;

    @Resource
    private SiteService siteService;

    @Resource
    private HttpSession session;


    private MapCache cache = MapCache.single();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        //
        String uri = request.getRequestURI();
        //
        LOGGER.info("UserAgent：{}", request.getHeader(USER_AGENT));
        LOGGER.info("用户访问地址：{}，来路地址：{}",uri, IPKit.getIpAddressByRequest1(request));

        //
        TestUser user = TaleUtils.getLoginUser(request);
        if (null == user) {
            Integer uid = TaleUtils.getCookieUid(request);
            if (null != uid) {
                //,
                user = userService.getUserInfoById(uid);
                request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
            }
        }

        if (uri.startsWith("/admin") && !uri.startsWith("/admin/login") && null == user
                && !uri.startsWith("/admin/css") && !uri.startsWith("/admin/images")
                && !uri.startsWith("/admin/js") && !uri.startsWith("/admin/plugins")
                && !uri.startsWith("/admin/editormd") && !uri.startsWith("/admin/upload/imgs")
                && !uri.startsWith("/admin/editormd")) {
            response.addHeader("Access-Control-Expose-Headers", "REDIRECT,CONTEXTPATH");
            response.addHeader("REDIRECT", "REDIRECT");
            response.addHeader("CONTEXTPATH", request.getContextPath() + "/admin/login");
            String requestType = request.getHeader("X-Requested-With");
            if(!"XMLHttpRequest".equals(requestType)) {
                response.sendRedirect(request.getContextPath() + "/admin/login");
            }
            return false;
        }

        //
        if (request.getMethod().equals("GET")) {
            String csrf_token = UUID.UU64();
            //
            cache.hset(Types.CSRF_TOKEN.getType(), csrf_token, uri,30 * 60);
            request.setAttribute("_csrf_token", csrf_token);
        }
        //
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView view) throws Exception {
        OptionsDomain ov = optionService.getOptionByName("site_record");
        //
        Long categoryCount = metaService.getMetasCountByType(Types.CATEGORY.getType());
        //
        Long tagCount = metaService.getMetasCountByType(Types.TAG.getType());
        //
        StatisticsDto statistics = siteService.getStatistics();
        //
        List<MetaDto> links = metaService.getMetaList(Types.LINK.getType(),null,WebConst.MAX_POSTS);

        session.setAttribute("categoryCount",categoryCount);
        session.setAttribute("tagCount",tagCount);
        session.setAttribute("articleCount",statistics.getArticles());
        session.setAttribute("links",links);
        request.setAttribute("commons", commons);
        request.setAttribute("option", ov);
        request.setAttribute("adminCommons", adminCommons);
        initSiteConfig(request);
    }

    private void initSiteConfig(HttpServletRequest request) {
        if (WebConst.initConfig.isEmpty()) {
            List<OptionsDomain> options = optionService.getOptions();
            Map<String, String> querys = new HashMap<>();
            options.forEach(option -> {
                querys.put(option.getName(),option.getValue());
            });
            WebConst.initConfig = querys;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
