package cn.jingqingyun.chatter.model;

import java.util.Date;
import java.util.UUID;

public class Message {
    private UUID uuid;
    private String to;
    private String from;
    private String content;
    private Date sendTime;

    public Message(String to, String from, String content) {
        super();
        uuid = UUID.randomUUID();
        this.to = to;
        this.from = from;
        this.content = content;
        sendTime = new Date();
    }

    public String getContent() {
        return content;
    }

    public String getFrom() {
        return from;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public String getTo() {
        return to;
    }

    public UUID getUuid() {
        return uuid;
    }

}
