package com.coamctech.bxloan.manager.service.VO;

import java.util.Date;

import com.coamctech.bxloan.manager.common.BaseEntity;
import com.coamctech.bxloan.manager.utils.CommonHelper;


public class UserVO extends BaseEntity{
	    
	private Long id;
    private String userName;
    private String nickName;
    private String password;
    private String birthday;
    private String email;
    private String officePhone;
    private String telephone;
    
    
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
	
    
    
}
