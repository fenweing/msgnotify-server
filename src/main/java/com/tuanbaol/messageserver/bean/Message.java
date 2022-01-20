package com.tuanbaol.messageserver.bean;

import lombok.Data;

@Data
public class Message {
    private String title;
    private String ticker;
    private String text;
    private String srcPack;
    private String time;
    private String body;

    public Message() {
    }

    public Message(String title, String srcPack) {
        this.title = title;
        this.srcPack = srcPack;
    }
}
