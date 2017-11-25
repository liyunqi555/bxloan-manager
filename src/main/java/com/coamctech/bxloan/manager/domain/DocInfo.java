package com.coamctech.bxloan.manager.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * Created by Administrator on 2017/11/11.
 */
@Entity
public class DocInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;//标题
    private String cnTitle;//中文标题
    private Long sourceId;//来源id
    private Long columnId;//栏目id
    private String classification;//分类
    private String groupName;//分组
    private String website;//网址
    private String keyword;//关键字
    private String summary;//摘要
    private String body;//原文正文
    private String cnBoty;//中文正文
    private Long creator;//创建人id
    private Date createTime;
    private Date updateTime;
    private Integer ifTop;//是否置顶，数字大小代表置顶的顺序
    @Transient
    private Date viewTime;//浏览时间
    @Transient
    private Date storeTime;//收藏时间

    public Date getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(Date storeTime) {
        this.storeTime = storeTime;
    }

    public Date getViewTime() {
        return viewTime;
    }

    public void setViewTime(Date viewTime) {
        this.viewTime = viewTime;
    }

    public String getCnTitle() {
        return cnTitle;
    }

    public void setCnTitle(String cnTitle) {
        this.cnTitle = cnTitle;
    }

    public String getCnBoty() {
        return cnBoty;
    }

    public void setCnBoty(String cnBoty) {
        this.cnBoty = cnBoty;
    }

    public Integer getIfTop() {
        return ifTop;
    }

    public void setIfTop(Integer ifTop) {
        this.ifTop = ifTop;
    }

    @Transient
    private String imgUrl;//从body中检索出的图片地址

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
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
}
