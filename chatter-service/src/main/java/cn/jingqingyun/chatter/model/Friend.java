package cn.jingqingyun.chatter.model;

import java.util.Date;

public class Friend {
    private Integer id;
    private User main;
    private User guest;
    private Date createTime;
    private Date modifyTime;

    public Date getCreateTime() {
        return createTime;
    }

    public User getGuest() {
        return guest;
    }

    public Integer getId() {
        return id;
    }

    public User getMain() {
        return main;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setGuest(User guest) {
        this.guest = guest;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMain(User main) {
        this.main = main;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}
