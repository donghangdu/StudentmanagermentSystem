package com.wip.service.message;

import com.wip.model.MessageClass;
import com.wip.model.TestClass;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


public interface MessageService {
    public int addMessage(MessageClass msglass, HttpServletRequest request);
}
