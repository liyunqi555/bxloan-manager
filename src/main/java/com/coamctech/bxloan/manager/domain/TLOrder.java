package com.coamctech.bxloan.manager.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "T_L_ORDER", schema = "DFZC_BXMC")
public class TLOrder  implements java.io.Serializable {
	@SequenceGenerator(name = "generator", sequenceName = "seq_t_l_order", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 12, scale = 0)
	private Long id;
	@Column(name = "ORDER_NO", length = 30)
	private String orderNo;
	@Column(name = "PROJECT_ID", length = 12)
	private Long projectId;
	@Column(name = "CUSTOMER_NAME", length = 30)
	private String customerName;//实际客户名称
	@Column(name = "OLD_CUSTOMER_NAME", length = 30)
	private String oldCustomerName;//原客户名称
	@Column(name = "CERTIFICATE_NUM", length = 30)
	private String certificateNum;//身份证号
	@Column(name = "MAX_LOAN_AMT", length = 12)
	private BigDecimal maxLoanAmt;//订单初始金额
	@Column(name = "MARKETING_LOAN_AMT", length = 12)
	private BigDecimal marketingLoanAmt;//营销额度
	@Column(name = "APPLY_TERM")
	private Integer applyTerm;
	@Column(name = "ORDER_AMT", length = 12)
	private BigDecimal orderAmt;//订单初始金额
	@Column(name = "ORG_ID", length = 12)
	private Long orgId;//小贷公司ID
	@Column(name = "THIRD_ORG_ID", length = 12)
	private Long thirdOrgId;//中介机构ID
	@Column(name = "THIRD_PERSON_ID", length = 12)
	private Long thirdPersonId;//中介人员ID
	@Column(name = "ORDER_STATUS", length = 2)
	private String orderStatus;//订单状态
	@Column(name = "third_org_amt", length = 12)
	private BigDecimal thirdOrgAmt;//第三方评估价值
	@Column(name="customer_manager_num",length=30)
	private Long customerManagerNum;//客户经理ID
	@Column(name="customer_manager_name",length=30)
	private String customerManagerName;//客户经理名称
	@Column(name = "Mortgage_type")
	private String mortgageType;
	@Column(name = "SYS_CREATE_TIME")
	private Date sysCreateTime;
	@Column(name = "ORDER_TIME")
	private Date orderTime;
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	@Column(name = "order_diligence_time")
	private Date orderDiligenceDate;//预约尽调时间
	@Column(name = "remark")
	private String remark;
	@Column(name = "REJECT_REASON")
	private String rejectReason;

	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCertificateNum() {
		return certificateNum;
	}
	public void setCertificateNum(String certificateNum) {
		this.certificateNum = certificateNum;
	}
	public BigDecimal getOrderAmt() {
		return orderAmt;
	}
	public void setOrderAmt(BigDecimal orderAmt) {
		this.orderAmt = orderAmt;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getThirdOrgId() {
		return thirdOrgId;
	}
	public void setThirdOrgId(Long thirdOrgId) {
		this.thirdOrgId = thirdOrgId;
	}
	public Long getThirdPersonId() {
		return thirdPersonId;
	}
	public void setThirdPersonId(Long thirdPersonId) {
		this.thirdPersonId = thirdPersonId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Date getSysCreateTime() {
		return sysCreateTime;
	}
	public void setSysCreateTime(Date sysCreateTime) {
		this.sysCreateTime = sysCreateTime;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public BigDecimal getMaxLoanAmt() {
		return maxLoanAmt;
	}
	public void setMaxLoanAmt(BigDecimal maxLoanAmt) {
		this.maxLoanAmt = maxLoanAmt;
	}
	public BigDecimal getThirdOrgAmt() {
		return thirdOrgAmt;
	}
	public void setThirdOrgAmt(BigDecimal thirdOrgAmt) {
		this.thirdOrgAmt = thirdOrgAmt;
	}
	public Long getCustomerManagerNum() {
		return customerManagerNum;
	}
	public void setCustomerManagerNum(Long customerManagerNum) {
		this.customerManagerNum = customerManagerNum;
	}
	public String getCustomerManagerName() {
		return customerManagerName;
	}
	public void setCustomerManagerName(String customerManagerName) {
		this.customerManagerName = customerManagerName;
	}
	public Integer getApplyTerm() {
		return applyTerm;
	}
	public void setApplyTerm(Integer applyTerm) {
		this.applyTerm = applyTerm;
	}
	public BigDecimal getMarketingLoanAmt() {
		return marketingLoanAmt;
	}
	public void setMarketingLoanAmt(BigDecimal marketingLoanAmt) {
		this.marketingLoanAmt = marketingLoanAmt;
	}
	public String getMortgageType() {
		return mortgageType;
	}
	public void setMortgageType(String mortgageType) {
		this.mortgageType = mortgageType;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Date getOrderDiligenceDate() {
		return orderDiligenceDate;
	}
	public void setOrderDiligenceDate(Date orderDiligenceDate) {
		this.orderDiligenceDate = orderDiligenceDate;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOldCustomerName() {
		return oldCustomerName;
	}
	public void setOldCustomerName(String oldCustomerName) {
		this.oldCustomerName = oldCustomerName;
	}
	
}
