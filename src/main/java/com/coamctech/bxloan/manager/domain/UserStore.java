package com.coamctech.bxloan.manager.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户收藏
 * Created by Administrator on 2017/11/11.
 */
@Entity
public class UserStore implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;//用户ID

    //智库报告 新闻动态 专题跟踪 相关
    private Long docInfoId;//文章ID
    private Long docColumnId;//二级栏目ID
    private Long docColumnParentId;//一级栏目ID

    //数据中心相关
    private String conceptUri;//数据概念
    private String entityId;//实体ID
    private Date createTime;//收藏时间


    public Long getDocInfoId() {
        return docInfoId;
    }

    public void setDocInfoId(Long docInfoId) {
        this.docInfoId = docInfoId;
    }

    public Long getDocColumnParentId() {
        return docColumnParentId;
    }

    public void setDocColumnParentId(Long docColumnParentId) {
        this.docColumnParentId = docColumnParentId;
    }

    public String getConceptUri() {
        return conceptUri;
    }

    public void setConceptUri(String conceptUri) {
        this.conceptUri = conceptUri;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocColumnId() {
        return docColumnId;
    }

    public void setDocColumnId(Long docColumnId) {
        this.docColumnId = docColumnId;
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

}
