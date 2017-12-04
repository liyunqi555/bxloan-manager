package com.coamctech.bxloan.manager.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户栏目顺序表
 * Created by Administrator on 2017/11/11.
 */
@Entity
public class UserDocColumnOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long docColumnId;
    private Long userId;
    private Integer customOrder ;//定制二级栏目的顺序
    private Date createTime;

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

    public Integer getCustomOrder() {
        return customOrder;
    }

    public void setCustomOrder(Integer customOrder) {
        this.customOrder = customOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
