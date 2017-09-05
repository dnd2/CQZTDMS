

package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;
@SuppressWarnings("serial")
public class TtPtShippingsheetPO extends PO{
	private Integer signCount;
	private String deliveryPdc;
	private Date updateDate;
	private Integer shippingCondition;
	private Integer isSigned;
	private Long createBy;
	private Date createDate;
	private String orderNo;
	private String doNo;
	private Date consignmentDate;
	private String deliveryCompany;
	private Long updateBy;
	private String soNo;
	private Integer cartonCount;
	private Date signDate;
	private String signPerson;
	private String remark;

	public void setSignCount(Integer signCount){
		this.signCount=signCount;
	}

	public Integer getSignCount(){
		return this.signCount;
	}

	public void setDeliveryPdc(String deliveryPdc){
		this.deliveryPdc=deliveryPdc;
	}

	public String getDeliveryPdc(){
		return this.deliveryPdc;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setShippingCondition(Integer shippingCondition){
		this.shippingCondition=shippingCondition;
	}

	public Integer getShippingCondition(){
		return this.shippingCondition;
	}

	public void setIsSigned(Integer isSigned){
		this.isSigned=isSigned;
	}

	public Integer getIsSigned(){
		return this.isSigned;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setDoNo(String doNo){
		this.doNo=doNo;
	}

	public String getDoNo(){
		return this.doNo;
	}

	public void setConsignmentDate(Date consignmentDate){
		this.consignmentDate=consignmentDate;
	}

	public Date getConsignmentDate(){
		return this.consignmentDate;
	}

	public void setDeliveryCompany(String deliveryCompany){
		this.deliveryCompany=deliveryCompany;
	}

	public String getDeliveryCompany(){
		return this.deliveryCompany;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSoNo(String soNo){
		this.soNo=soNo;
	}

	public String getSoNo(){
		return this.soNo;
	}

	public void setCartonCount(Integer cartonCount){
		this.cartonCount=cartonCount;
	}

	public Integer getCartonCount(){
		return this.cartonCount;
	}

	public void setSignDate(Date signDate){
		this.signDate=signDate;
	}

	public Date getSignDate(){
		return this.signDate;
	}

	public void setSignPerson(String signPerson){
		this.signPerson=signPerson;
	}

	public String getSignPerson(){
		return this.signPerson;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
