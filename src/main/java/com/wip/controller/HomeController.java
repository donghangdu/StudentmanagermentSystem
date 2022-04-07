package com.wip.controller;

import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import com.wip.constant.ErrorConstant;
import com.wip.constant.Types;
import com.wip.constant.WebConst;
import com.wip.dto.MetaDto;
import com.wip.dto.StatisticsDto;
import com.wip.dto.cond.ContentCond;
import com.wip.dto.cond.MetaCond;
import com.wip.exception.BusinessException;
import com.wip.model.CommentDomain;
import com.wip.model.ContentDomain;
import com.wip.model.MetaDomain;
import com.wip.service.article.ContentService;
import com.wip.service.comment.CommentService;
import com.wip.service.meta.MetaService;
import com.wip.service.site.SiteService;
import com.wip.utils.APIResponse;
import com.wip.utils.IPKit;
import com.wip.utils.TaleUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.List;

@Api("")
@Controller
public class HomeController extends BaseController {

    @Resource
    private ContentService contentService;

    @Resource
    private CommentService commentService;

    @Resource
    private MetaService metaService;


    //@GetMapping(value = "/")
    public String index(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
            int page,
            @ApiParam(name = "limit", value = "QPPage", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "5")
            int limit
    ) {
        PageInfo<ContentDomain> articles = contentService.getArticlesByCond(new ContentCond(), page, limit);

        request.setAttribute("articles",articles);
        return "blog/home";
    }

    @ApiOperation("")
    @GetMapping(value = "/archives")
    public String archives(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
            int page,
            @ApiParam(name = "limit", value = "QPPage", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "10")
            int limit
    ) {
        PageInfo<ContentDomain> articles = contentService.getArticlesByCond(new ContentCond(), page, limit);
        request.setAttribute("articles", articles);
        return "blog/archives";
    }

    @ApiOperation("")
    @GetMapping(value = "/categories")
    public String categories(HttpServletRequest request) {
        //
        List<MetaDto> categories = metaService.getMetaList(Types.CATEGORY.getType(),null,WebConst.MAX_POSTS);
        //
        Long categoryCount = metaService.getMetasCountByType(Types.CATEGORY.getType());
        request.setAttribute("categories", categories);
        request.setAttribute("categoryCount", categoryCount);
        return "blog/category";
    }

    @ApiOperation("")
    @GetMapping(value = "/categories/{name}")
    public String categoriesDetail(
            HttpServletRequest request,
            @ApiParam(name = "name", value = "name", required = true)
            @PathVariable("name")
            String name
    ) {
        MetaDomain category = metaService.getMetaByName(Types.CATEGORY.getType(),name);
        if (null == category.getName())
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        List<ContentDomain> articles = contentService.getArticleByCategory(category.getName());
        request.setAttribute("category", category.getName());
        request.setAttribute("articles", articles);
        return "blog/category_detail";
    }

    @ApiOperation("标签内容页")
    @GetMapping(value = "/tags")
    public String tags(HttpServletRequest request) {
        // 获取标签
        List<MetaDto> tags = metaService.getMetaList(Types.TAG.getType(), null, WebConst.MAX_POSTS);
        // 标签总数
        Long tagCount = metaService.getMetasCountByType(Types.TAG.getType());
        request.setAttribute("tags", tags);
        request.setAttribute("tagCount", tagCount);
        return "blog/tags";
    }

    @ApiOperation("")
    @GetMapping(value = "/tags/{name}")
    public String tagsDetail(
            HttpServletRequest request,
            @ApiParam(name = "name", value = "name", required = true)
            @PathVariable("name")
            String name
    ) {
        MetaDomain tags = metaService.getMetaByName(Types.TAG.getType(),name);
        List<ContentDomain> articles = contentService.getArticleByTags(tags);
        request.setAttribute("articles",articles);
        request.setAttribute("tags",tags.getName());
        return "blog/tags_detail";
    }

    @GetMapping(value = "/about")
    public String about() {
        return "blog/about";
    }

    @ApiOperation("")
    @GetMapping(value = "/detail/{cid}")
    public String detail(
            @ApiParam(name = "cid", value = "cid", required = true)
            @PathVariable("cid")
            Integer cid,
            HttpServletRequest request
    ) {
        ContentDomain article = contentService.getArticleById(cid);
        request.setAttribute("article", article);

        //
        this.updateArticleHits(article.getCid(),article.getHits());
        //
        List<CommentDomain> comments = commentService.getCommentsByCId(cid);
        request.setAttribute("comments", comments);

        return "blog/detail";
    }

    /**
     *
     * @param cid
     * @param chits
     */
    private void updateArticleHits(Integer cid, Integer chits) {
        Integer hits = cache.hget("article", "hits");
        if (chits == null) {
            chits = 0;
        }
        hits = null == hits ? 1 : hits + 1;
        if (hits >= WebConst.HIT_EXEED) {
            ContentDomain temp = new ContentDomain();
            temp.setCid(cid);
            temp.setHits(chits + hits);
            contentService.updateContentByCid(temp);
            cache.hset("article", "hits", 1);
        } else {
            cache.hset("article", "hits", hits);
        }

    }

    @PostMapping(value = "/comment")
    @ResponseBody
    public APIResponse comment(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(name = "cid", required = true) Integer cid,
                               @RequestParam(name = "coid", required = false) Integer coid,
                               @RequestParam(name = "author", required = false) String author,
                               @RequestParam(name = "email", required = false) String email,
                               @RequestParam(name = "url", required = false) String url,
                               @RequestParam(name = "content", required = true) String content,
                               @RequestParam(name = "csrf_token", required = true) String csrf_token
                               ) {

        String ref = request.getHeader("Referer");
        if (StringUtils.isBlank(ref) || StringUtils.isBlank(csrf_token)){
            return APIResponse.fail("fail");
        }

        String token = cache.hget(Types.CSRF_TOKEN.getType(), csrf_token);
        if (StringUtils.isBlank(token)) {
            return APIResponse.fail("fail");
        }

        if (null == cid || StringUtils.isBlank(content)) {
            return APIResponse.fail("Please enter a complete comment");
        }

        if (StringUtils.isNotBlank(author) && author.length() > 50) {
            return APIResponse.fail("The name is too long");
        }

        if (StringUtils.isNotBlank(email) && !TaleUtils.isEmail(email)) {
            return APIResponse.fail("Please enter the correct email format");
        }

        if (StringUtils.isNotBlank(url) && !TaleUtils.isURL(url)) {
            return APIResponse.fail("Please input the correct URL format");
        }

        if (content.length() > 200) {
            return APIResponse.fail("Please enter an evaluation within 200 characters");
        }

        String val = IPKit.getIpAddressByRequest1(request) + ":" + cid;
        Integer count = cache.hget(Types.COMMENTS_FREQUENCY.getType(), val);
        if (null != count && count > 0) {
            return APIResponse.fail("Your comments are too fast. Please try again later");
        }

        author = TaleUtils.cleanXSS(author);
        content = TaleUtils.cleanXSS(content);

        author = EmojiParser.parseToAliases(author);
        content = EmojiParser.parseToAliases(content);


        CommentDomain comments = new CommentDomain();
        comments.setAuthor(author);
        comments.setCid(cid);
        comments.setIp(request.getRemoteAddr());
        comments.setUrl(url);
        comments.setContent(content);
        comments.setEmail(email);
        comments.setParent(coid);

        try {
            commentService.addComment(comments);
            cookie("tale_remember_author", URLEncoder.encode(author,"UTF-8"), 7 * 24 * 60 * 60, response);
            cookie("tale_remember_mail", URLEncoder.encode(email,"UTF-8"), 7 * 24 * 60 * 60, response);
            if (StringUtils.isNotBlank(url)) {
                cookie("tale_remember_url",URLEncoder.encode(url,"UTF-8"),7 * 24 * 60 * 60, response);
            }
            // 设置对每个文章1分钟可以评论一次
            cache.hset(Types.COMMENTS_FREQUENCY.getType(),val,1,60);

            return APIResponse.success();

        } catch (Exception e) {
            throw BusinessException.withErrorCode(ErrorConstant.Comment.ADD_NEW_COMMENT_FAIL);
        }

    }

    private void cookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name,value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }


}
