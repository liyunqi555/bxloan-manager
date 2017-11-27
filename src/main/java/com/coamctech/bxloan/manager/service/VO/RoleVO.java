package com.coamctech.bxloan.manager.service.VO;

import com.coamctech.bxloan.manager.common.BaseEntity;
import com.coamctech.bxloan.manager.utils.CommonHelper;

public class RoleVO extends BaseEntity{
	
    private Long id;
    private String englishName;
    private String roleName;
    private Integer type;
    
    public RoleVO(Object[] objs){
    	super();
    	int i=0;
    	this.id=CommonHelper.toLong(objs[i++]);
    	this.roleName=CommonHelper.toStr(objs[i++]);
    	this.englishName=CommonHelper.toStr(objs[i++]);
    	this.type=CommonHelper.toInt(objs[i++]);
    }
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
