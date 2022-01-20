package com.tuanbaol.messageserver.service;

import com.tuanbaol.messageserver.bean.Message;
import com.tuanbaol.messageserver.util.Base64GarbleUtil;
import com.tuanbaol.messageserver.util.CollectionUtil;
import com.tuanbaol.messageserver.util.JsonUtil;
import com.tuanbaol.messageserver.websocket.MyWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;

@Service
@Slf4j
public class MessageService {
    private final static String ENCRYPT_PREFIX = "Pp/eEiT[";
    @Autowired
    private MyWebSocketHandler webSocketHandler;
    private Map<String, BiFunction<MessageService, Message, Message>> analyzeStrategyMap = new HashMap<>();
    private Map<String, BiFunction<MessageService, Message, Message>> filterStrategyMap = new HashMap<>();
    private List<Message> filterPattern = new ArrayList<>();

    public MessageService() {
        analyzeStrategyMap.put("default", MessageService::defaultAnalyze);
        analyzeStrategyMap.put("com.tencent.mm", MessageService::analyzeWechatMsg);
        filterStrategyMap.put("default", MessageService::defaultFilter);

        addFilterPattern("选择输入法", "android");
        addFilterPattern("", "com.oneplus.dialer");
        addFilterPattern("", "com.oneplus.mms");
//        addFilterPattern("", "com.tencent.mm");
        addFilterPattern("", "com.tencent.qqmusic");
    }

    private void addFilterPattern(String title, String srcPack) {
        filterPattern.add(new Message(title, srcPack));
    }

    public void obtain(Message msg) {
        msg = filterMessage(msg);
        msg = analyzeMessageByDiffType(msg);
        log.info("websocket消息加密，message-【{}】", msg);
        String encryptedMsg = encryptMessage(msg);
        pushToQueue(encryptedMsg);
    }

    private Message analyzeMessageByDiffType(Message msg) {
        String srcPack = msg.getSrcPack();
        if (analyzeStrategyMap.get(srcPack) != null) {
            return analyzeStrategyMap.get(srcPack).apply(MessageService.this, msg);
        }
        return analyzeStrategyMap.get("default").apply(MessageService.this, msg);
    }

    private Message filterMessage(Message msg) {
        Set<Map.Entry<String, BiFunction<MessageService, Message, Message>>> entries = filterStrategyMap.entrySet();
        for (Map.Entry<String, BiFunction<MessageService, Message, Message>> entry : entries) {
            if (entry.getValue().apply(MessageService.this, msg) == null) {
                return null;
            }
        }
        return msg;
    }

    private String encryptMessage(Message msg) {
        String msgStr = JsonUtil.toString(msg);
        return ENCRYPT_PREFIX + Base64GarbleUtil.encode(msgStr);
    }

//    private String encryptMessage(Message msg) {
//        String msgStr = JsonUtil.toString(msg);
//        try {
//            byte[] encrypt = AESUtils.encrypt(msgStr.getBytes(CommonConstant.CHARSET_UTF8), CommonConstant.cipher.getBytes(CommonConstant.CHARSET_UTF8));
//            return ENCRYPT_PREFIX + AESUtils.parseByte2HexStr(encrypt);
//        } catch (Exception e) {
//            throw new ServiceException("加密消息失败", e);
//        }
//    }

    private void pushToQueue(String msg) {
        //no queue currently
        webSocketHandler.send(msg);

    }

    //+++++++++++++++++++++message analyze strategy start++++++++++++++++++++
    public Message analyzeWechatMsg(Message message) {
        String ticker = message.getTicker();
        String text = message.getText();
        String title = message.getTitle();
        int tickerLen = ticker.length();
        int textLen = text.length();
        if (StringUtils.isBlank(ticker) || StringUtils.isBlank(text)) {
            message.setBody(ticker + text);
        } else if (StringUtils.equals(ticker, text)) {
            message.setBody(text);
        } else if (tickerLen < textLen) {
            String prefix = text.substring(0, textLen - tickerLen);
            if (prefix.contains("[") && prefix.contains("条") && prefix.contains("]")) {
                message.setBody(ticker);
            } else {
                message.setBody("【" + ticker + "】" + text);
            }
        } else if (StringUtils.equals(ticker, title + ": " + text)) {
            message.setBody(text);
        } else {
            message.setBody("【" + ticker + "】" + text);
        }
        return message;
    }

    public Message defaultAnalyze(Message message) {
        String ticker = message.getTicker();
        String text = message.getText();
        if (StringUtils.isBlank(ticker) || StringUtils.isBlank(text)) {
            message.setBody(ticker + text);
        } else if (StringUtils.equals(ticker, text)) {
            message.setBody(text);
        } else {
            message.setBody("【" + ticker + "】" + text);
        }
        return message;
    }

    //+++++++++++++++++++++message analyze strategy end++++++++++++++++++++

    //+++++++++++++++++++++message filter strategy start++++++++++++++++++++
    public Message defaultFilter(Message message) {
        List<Message> filterList = getFilterPattern();
        boolean shouldFilter = filterList.stream().anyMatch(f ->
                (StringUtils.isBlank(f.getTitle()) ||
                        StringUtils.equals(f.getTitle(), message.getTitle()))
                        && StringUtils.equals(f.getSrcPack(), message.getSrcPack()));
        return shouldFilter ? null : message;
    }

    //+++++++++++++++++++++message filter strategy end++++++++++++++++++++
    private List<Message> getFilterPattern() {
        return filterPattern;
    }
}
