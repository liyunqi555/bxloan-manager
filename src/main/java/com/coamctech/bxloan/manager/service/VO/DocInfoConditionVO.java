package com.coamctech.bxloan.manager.service.VO;

/**   
 * 类名称：DocInfoCondition<br/>
 * 类描述 ：<br/>
 * 创建人: zhaoqingwen<br/>
 * 创建时间：2017年12月12日 上午9:56:03<br/>  
 * 修改人：
 * 修改时间：  
 * 修改备注：
 * 版本： V1.0
 */

public class DocInfoConditionVO {
		private Long docInfoId;//文章ID 
	  	private String sourceName;//来源名称
	  	private String columnName;//栏目名称
	    private Long sourceId;//来源id
	    private Long columnId;//栏目id
	    private String keyword;//关键字
	    private String conditionField;//关联文章的条件
	    private Integer conditionType;//关联类型，1：普通查询，2：高级查询
	    
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
		public Long getSourceId() {
			return sourceId;
		}
		public void setSourceId(Long sourceId) {
			this.sourceId = sourceId;
		}
		public Long getColumnId() {
			return columnId;
		}
		public void setColumnId(Long columnId) {
			this.columnId = columnId;
		}
		public Long getDocInfoId() {
			return docInfoId;
		}
		public void setDocInfoId(Long docInfoId) {
			this.docInfoId = docInfoId;
		}
		public String getKeyword() {
			return keyword;
		}
		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}
		public String getConditionField() {
			return conditionField;
		}
		public void setConditionField(String conditionField) {
			this.conditionField = conditionField;
		}
		public Integer getConditionType() {
			return conditionType;
		}
		public void setConditionType(Integer conditionType) {
			this.conditionType = conditionType;
		}


}
