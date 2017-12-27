package com.coamctech.bxloan.manager.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *  文档栏目
 * Created by Administrator on 2017/11/11.
 */
@Entity
public class DocColumn implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Integer ifSpecial;
    private Integer level;
    private Long parentId;
    private Long creator;
    private Date createTime;
    private Date updateTime;
    private Integer conditionType;//关联类型，1：普通查询，2：高级查询
    private String conditionField;//关联文章的条件

    @Transient
    private int customOrder;

    public int getCustomOrder() {
        return customOrder;
    }

    public void setCustomOrder(int customOrder) {
        this.customOrder = customOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIfSpecial() {
        return ifSpecial;
    }

    public void setIfSpecial(Integer ifSpecial) {
        this.ifSpecial = ifSpecial;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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


    public Integer getConditionType() {
        return conditionType;
    }

    public void setConditionType(Integer conditionType) {
        this.conditionType = conditionType;
    }

	public String getConditionField() {
		return conditionField;
	}

	public void setConditionField(String conditionField) {
		this.conditionField = conditionField;
	}
}
