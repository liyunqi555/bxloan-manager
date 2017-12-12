package com.coamctech.bxloan.manager.domain;

import com.coamctech.bxloan.manager.utils.CommonHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/12.
 */
@Entity
public class FeedBack implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Date createTime;
    private Date updateTime;
    @Transient
    private String createTimeStr;
    @Transient
    private String userName;

    public FeedBack(){}
    public FeedBack(Object[] objs){
        super();
        int i=0;
        this.id= CommonHelper.toLong(objs[i++]);
        this.title=CommonHelper.toStr(objs[i++]);
        this.content=CommonHelper.toStr(objs[i++]);
        this.userId = CommonHelper.toLong(objs[i++]);
        this.createTime =CommonHelper.str2Date(String.valueOf(objs[i++]), CommonHelper.DF_DATE_TIME);
        this.createTimeStr = CommonHelper.date2Str(this.createTime,CommonHelper.DF_DATE_TIME);
        this.userName = CommonHelper.toStr(objs[i++]);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
