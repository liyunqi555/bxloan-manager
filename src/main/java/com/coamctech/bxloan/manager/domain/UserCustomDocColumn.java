package com.coamctech.bxloan.manager.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户定制的栏目
 * 如果后台管理每个用户已经与角色建立了联系，那么是否只能定制分配给该用户的栏目。
 * 如果是这样的话，那么当后台确定该用户与某个已定制栏目的关系后，是否删除该用户已经定制的栏目？
 * Created by Administrator on 2017/11/11.
 */
@Entity
public class UserCustomDocColumn implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;//用户ID
    private Long docColumnId;//订阅的二级栏目的ID
    private Long docColumnParentId;//一级栏目ID
    //private Integer customOrder ;//定制二级栏目的顺序
    private Date createTime;//定制时间

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

    public Long getDocColumnParentId() {
        return docColumnParentId;
    }

    public void setDocColumnParentId(Long docColumnParentId) {
        this.docColumnParentId = docColumnParentId;
    }

}
