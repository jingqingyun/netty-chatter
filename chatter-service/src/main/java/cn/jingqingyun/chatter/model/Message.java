package cn.jingqingyun.chatter.model;

import java.util.Date;
import java.util.UUID;

public class Message {
    private Long id;
    private UUID uuid;
    private String from;
    private String to;
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

    public Long getId() {
        return id;
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

    public void setContent(String content) {
        this.content = content;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

}
