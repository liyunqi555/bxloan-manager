package com.coamctech.bxloan.manager.service.VO;

import java.io.Serializable;

import com.coamctech.bxloan.manager.common.BaseEntity;
import com.coamctech.bxloan.manager.utils.CommonHelper;

public class UserSignVO extends BaseEntity implements Serializable{
	
	
	private Long id;
	private Long parentId;
	private String columnName;
	private String parentColumnName;
	private Integer level; 
	
	public UserSignVO(){
		super();
	}
	
	public UserSignVO(Object[] objs){
		super();
		int i=0;
		this.id=CommonHelper.toLong(objs[i++]);
		this.columnName=CommonHelper.toStr(objs[i++]);
    	this.parentId=CommonHelper.toLong(objs[i++]);
    	this.level=CommonHelper.toInt(objs[i++]);
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getParentColumnName() {
		return parentColumnName;
	}

	public void setParentColumnName(String parentColumnName) {
		this.parentColumnName = parentColumnName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	

	
	
	
	

}
