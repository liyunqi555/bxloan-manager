package com.coamctech.bxloan.manager.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *  资讯置顶，app首页banner 轮播图
 * Created by Administrator on 2017/11/11.
 */
@Entity
public class TopNews implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;//标题
    private Long docInfoId;//创建人id
    private Date createTime;

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

    public Long getDocInfoId() {
        return docInfoId;
    }

    public void setDocInfoId(Long docInfoId) {
        this.docInfoId = docInfoId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
