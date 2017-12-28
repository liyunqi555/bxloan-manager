package com.coamctech.bxloan.manager.service.VO;

import java.io.Serializable;

import com.coamctech.bxloan.manager.common.BaseEntity;
import com.coamctech.bxloan.manager.utils.CommonHelper;

public class RoleVO extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = -38940372447971440L;
	private Long id;
    private String englishName;
    private String roleName;
    private Integer type;
    
    private String columnIds;
    private String sourceIds;
    private String userIds;
    private String operateType;
    
    public RoleVO(){
    	super();
    }
    
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

	public String getColumnIds() {
		return columnIds;
	}

	public void setColumnIds(String columnIds) {
		this.columnIds = columnIds;
	}

	public String getSourceIds() {
		return sourceIds;
	}

	public void setSourceIds(String sourceIds) {
		this.sourceIds = sourceIds;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	
	
	
}
