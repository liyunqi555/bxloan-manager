package com.coamctech.bxloan.manager.service.VO;

import com.coamctech.bxloan.manager.common.BaseEntity;
import com.coamctech.bxloan.manager.utils.CommonHelper;

public class DocSourceVO extends BaseEntity{

    private Long id;
    private String name;
    private Integer type;
    private Integer ifSpecial;
    private Integer languageType;
    public DocSourceVO(){}
    public DocSourceVO(Object[] objs){
    	super();
    	int i=0;
    	this.id=CommonHelper.toLong(objs[i++]);
    	this.name=CommonHelper.toStr(objs[i++]);
    	this.type=Integer.valueOf(CommonHelper.toStr(objs[i++]));
        this.ifSpecial=Integer.valueOf(CommonHelper.toStr(objs[i++]));
    	this.languageType=CommonHelper.toInt(objs[i++]);
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIfSpecial() {
        return ifSpecial;
    }

    public void setIfSpecial(Integer ifSpecial) {
        this.ifSpecial = ifSpecial;
    }

    public Integer getLanguageType() {
        return languageType;
    }

    public void setLanguageType(Integer languageType) {
        this.languageType = languageType;
    }
}
