package com.wip.service.message.impl;

import com.wip.dao.MessageMapper;
import com.wip.dao.TestClassMapper;
import com.wip.model.MessageClass;
import com.wip.model.TestClass;
import com.wip.service.message.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageMapper messageMapper;
    @Override
    public int addMessage(MessageClass msglass, HttpServletRequest request) {
        int b=messageMapper.insert(msglass);
        return b;
    }
}
