package com.coamctech.bxloan.manager.service.VO;

import com.coamctech.bxloan.manager.domain.DocInfo;

/**   
 * 类名称：DocInfoFormVO<br/>
 * 类描述 ：<br/>
 * 创建人: zhaoqingwen<br/>
 * 创建时间：2017年12月12日 下午11:54:32<br/>  
 * 修改人：
 * 修改时间：  
 * 修改备注：
 * 版本： V1.0
 */

public class DocInfoFormVO {
	private DocInfo docInfo;
	private String sourceName;
	private String columnName;
	public DocInfo getDocInfo() {
		return docInfo;
	}
	public void setDocInfo(DocInfo docInfo) {
		this.docInfo = docInfo;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

}
