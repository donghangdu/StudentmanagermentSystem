package com.wip.controller.admin;

import com.github.pagehelper.PageInfo;
import com.wip.api.QiNiuCloudService;
import com.wip.constant.ErrorConstant;
import com.wip.constant.LogActions;
import com.wip.constant.Types;
import com.wip.constant.WebConst;
import com.wip.controller.BaseController;
import com.wip.dto.AttAchDto;
import com.wip.exception.BusinessException;
import com.wip.model.AttAchDomain;
import com.wip.model.UserDomain;
import com.wip.service.attach.AttAchService;
import com.wip.service.log.LogService;
import com.wip.utils.APIResponse;
import com.wip.utils.Commons;
import com.wip.utils.TaleUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Api("")
@Controller
@RequestMapping("admin/attach")
public class AttachController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachController.class);

    public static final String CLASSPATH = TaleUtils.getUploadFilePath();

    @Resource
    private AttAchService attAchService;

    @Resource
    private LogService logService;



    @ApiOperation("")
    @GetMapping(value = "")
    public String index(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "PageNum", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
            int page,
            @ApiParam(name = "limit", value = "limit", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "12")
            int limit
    ) {
        PageInfo<AttAchDto> atts = attAchService.getAtts(page, limit);
        request.setAttribute("attachs", atts);
        request.setAttribute(Types.ATTACH_URL.getType(),Commons.site_option(Types.ATTACH_URL.getType(), Commons.site_url()));
        request.setAttribute("max_file_size", WebConst.MAX_FILE_SIZE / 1024);
        return "admin/attach";
    }

    @ApiOperation("")
    @PostMapping(value = "/uploadfile")
    public void fileUploadToTencentCloud(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(name = "editormd-image-file", value = "file", required = true)
            @RequestParam(name = "editormd-image-file", required = true)
            MultipartFile file
    ) {

    }


    @ApiOperation("")
    @PostMapping(value = "upload")
    @ResponseBody
    public APIResponse filesUploadToCloud(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(name = "file", value = "file", required = true)
            @RequestParam(name = "file", required = true)
            MultipartFile[] files
    ) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","text/html");

            for (MultipartFile file :files) {

                String fileName = TaleUtils.getFileKey(file.getOriginalFilename().replaceFirst("/", ""));

                QiNiuCloudService.upload(file, fileName);
                AttAchDomain attAchDomain = new AttAchDomain();
                HttpSession session = request.getSession();
                UserDomain sessionUser = (UserDomain) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
                attAchDomain.setAuthorId(sessionUser.getUid());
                attAchDomain.setFtype(TaleUtils.isImage(file.getInputStream()) ? Types.IMAGE.getType() : Types.FILE.getType());
                attAchDomain.setFname(fileName);
                attAchDomain.setFkey(QiNiuCloudService.QINIU_UPLOAD_SITE + fileName);
                attAchService.addAttAch(attAchDomain);
            }
            return APIResponse.success();

        } catch (IOException e) {
            e.printStackTrace();
            throw BusinessException.withErrorCode(ErrorConstant.Att.ADD_NEW_ATT_FAIL)
                    .withErrorMessageArguments(e.getMessage());
        }

    }

    @ApiOperation("")
    @PostMapping(value = "/delete")
    @ResponseBody
    public APIResponse deleteFileInfo(
            HttpServletRequest request,
            @ApiParam(name = "id", value = "id", required = true)
            @RequestParam(name = "id", required = true)
            Integer id
    ) {
        try {
            AttAchDto attach = attAchService.getAttAchById(id);
            if (null == attach)
                throw BusinessException.withErrorCode(ErrorConstant.Att.DELETE_ATT_FAIL + ": file does not exist");
            attAchService.deleteAttAch(id);
            //
            logService.addLog(LogActions.DEL_ATTACH.getAction(),this.user(request).getUsername()+"user",request.getRemoteAddr(),this.getUid(request));
            return APIResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.withErrorCode(e.getMessage());
        }
    }

}
