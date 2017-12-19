package com.coamctech.bxloan.manager.service.VO;

import java.io.Serializable;

import com.coamctech.bxloan.manager.common.BaseEntity;
import com.coamctech.bxloan.manager.utils.CommonHelper;

public class UserStoreVO extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 1719542023955363937L;
	
	private Long id;
	private String userName;
	private String docName;
	private String createTime;
	
	public UserStoreVO(){
		super();
	}
	
	public UserStoreVO(Object[] objs){
		super();
		int i=0;
		this.id=CommonHelper.toLong(objs[i++]);
    	this.userName=CommonHelper.toStr(objs[i++]);
    	this.docName=CommonHelper.toStr(objs[i++]);
    	this.createTime=CommonHelper.date2Str(CommonHelper.toDate(objs[i++]), CommonHelper.DF_DATE);
		
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
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	
	

}
