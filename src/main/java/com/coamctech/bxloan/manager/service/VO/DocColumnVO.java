package com.coamctech.bxloan.manager.service.VO;

import java.util.Date;

import com.coamctech.bxloan.manager.utils.CommonHelper;

/**   
 * 类名称：DocColumnVO<br/>
 * 类描述 ：<br/>
 * 创建人: zhaoqingwen<br/>
 * 创建时间：2017年12月11日 上午12:10:45<br/>  
 * 修改人：
 * 修改时间：  
 * 修改备注：
 * 版本： V1.0
 */

public class DocColumnVO {
	private Long id;
	private Integer order ;
    private String name;
    private Integer ifSpecial;
    private Integer level;
    private Long parentId;
    private String parentName;
    private Long creator;
    private String creatorName;
    private Date createTime;
    private Date updateTime;
    private String condtionField;
    
    public DocColumnVO(){
    	
    }
    public DocColumnVO(Object[] objs){
    	super();
		int i = 0;
		this.name = CommonHelper.toStr(objs[i++]);
		this.creatorName = CommonHelper.toStr(objs[i++]);
		this.ifSpecial = CommonHelper.toInt(objs[i++]);
		this.level= CommonHelper.toInt(objs[i++]);
		this.parentName = CommonHelper.toStr(objs[i++]);
		this.id = CommonHelper.toLong(objs[i++]);
		this.parentId = CommonHelper.toLong(objs[i++]);
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
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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
	public String getCondtionField() {
		return condtionField;
	}
	public void setCondtionField(String condtionField) {
		this.condtionField = condtionField;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}
