package com.wip.controller.admin;

import com.wip.constant.Types;
import com.wip.constant.WebConst;
import com.wip.controller.BaseController;
import com.wip.dto.MetaDto;
import com.wip.exception.BusinessException;
import com.wip.service.meta.MetaService;
import com.wip.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api("")
@Controller
@RequestMapping("/admin/category")
public class CategoryController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Resource
    private MetaService metaService;


    @ApiOperation("")
    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        List<MetaDto> categories = metaService.getMetaList(Types.CATEGORY.getType(),null,WebConst.MAX_POSTS);
        List<MetaDto> tags = metaService.getMetaList(Types.TAG.getType(), null, WebConst.MAX_POSTS);
        request.setAttribute("categories",categories);
        request.setAttribute("tags",tags);
        return "admin/category";
    }

    @ApiOperation("Save classification")
    @PostMapping(value = "/save")
    @ResponseBody
    public APIResponse addCategory(
            @ApiParam(name = "cname", value = "cname", required = true)
            @RequestParam(name = "cname", required = true)
            String cname,
            @ApiParam(name = "mid", value = "mid", required = false)
            @RequestParam(name = "mid", required = false)
            Integer mid
    ) {
        try {
            metaService.saveMeta(Types.CATEGORY.getType(),cname,mid);

        } catch (Exception e) {
            e.printStackTrace();
            String msg = "Classification save failed";
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                msg = ex.getErrorCode();
            }
            LOGGER.error(msg, e);
            return APIResponse.fail(msg);
        }
        return APIResponse.success();
    }

    @ApiOperation("")
    @PostMapping(value = "/delete")
    @ResponseBody
    public APIResponse deleteCategory(
            @ApiParam(name = "mid", value = "mid", required = true)
            @RequestParam(name = "mid", required = true)
            Integer mid
    ) {
        try {
            metaService.deleteMetaById(mid);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return APIResponse.fail(e.getMessage());
        }
        return APIResponse.success();

    }

}
