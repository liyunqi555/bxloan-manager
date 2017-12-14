package com.coamctech.bxloan.manager.service.VO;

import java.io.Serializable;
import java.util.Date;

import com.coamctech.bxloan.manager.common.BaseEntity;
import com.coamctech.bxloan.manager.utils.CommonHelper;


public class UserVO extends BaseEntity implements Serializable{
	    
	private static final long serialVersionUID = 854777193056270430L;
	private Long id;
    private String userName;
    private String nickName;
    private String password;
    private String birthday;
    private String email;
    private String officePhone;
    private String telephone;
    private String columnIds;
    private String sourceIds;
    private String roleIds;
    private String startTime;
    private String endTime;
    private Long creator;
    private String creatorStr;
    
    private String operateType;
    
    public UserVO(){
    	super();
    }
    
    public UserVO(Object[] objs){
    	super();
		int i = 0;
    	this.id=CommonHelper.toLong(objs[i++]);
    	this.userName=CommonHelper.toStr(objs[i++]);
    	this.nickName=CommonHelper.toStr(objs[i++]);
    	this.birthday=CommonHelper.toStr(objs[i++]);
    	this.email=CommonHelper.toStr(objs[i++]);
    	this.officePhone=CommonHelper.toStr(objs[i++]);
    	this.telephone=CommonHelper.toStr(objs[i++]);
    	this.creator=CommonHelper.toLong(objs[i++]);
    }
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOfficePhone() {
		return officePhone;
	}
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public String getCreatorStr() {
		return creatorStr;
	}

	public void setCreatorStr(String creatorStr) {
		this.creatorStr = creatorStr;
	}
	
	
	
	
	
	
	
	
	
    
    
}
