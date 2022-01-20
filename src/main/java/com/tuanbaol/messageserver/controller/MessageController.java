package com.tuanbaol.messageserver.controller;

import com.tuanbaol.messageserver.bean.Message;
import com.tuanbaol.messageserver.constant.CommonConstant;
import com.tuanbaol.messageserver.service.MessageService;
import com.tuanbaol.messageserver.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping(value = "/o1btain")
    public ResponseEntity o1btain(@RequestBody Message message) {
        messageService.obtain(message);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/obtain")
    public ResponseEntity obtain(HttpServletRequest request) throws IOException {
        String messageStr = IOUtils.toString(request.getInputStream(), CommonConstant.CHARSET_UTF8);
        log.info("/obtain 参数：-【{}】", messageStr);
        messageService.obtain(JsonUtil.toObject(messageStr, Message.class));
        return ResponseEntity.ok().build();
    }
}
