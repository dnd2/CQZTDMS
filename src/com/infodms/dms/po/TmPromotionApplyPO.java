package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;
public class TmPromotionApplyPO extends PO{
private Long promotionApplyId;
private Long promotionId;
private Long orgId;
private Integer applyQuantity;
private Integer paymentType;
private Integer confirmQuantity;//数量
private Integer confirmAmount;//金额
private String approvalCode;
private String approvalTitle;
private String approvalRemark;
private Long accountingUnit;
private Integer accountingAccount;
private String discountCode;
private String discountTitle;
private Integer status;
private Date auditDate;
private Long chargeId;
private Long createBy;
private Date createDate;
private Long updateBy;
private Date updateDate;


 public void setPromotionApplyId( Long promotionApplyId){
     this.promotionApplyId=promotionApplyId;
}
 public Long getPromotionApplyId(){
      return this.promotionApplyId;
}
 public void setPromotionId( Long promotionId){
     this.promotionId=promotionId;
}
 public Long getPromotionId(){
      return this.promotionId;
}
 public void setApplyQuantity( Integer applyQuantity){
     this.applyQuantity=applyQuantity;
}
 public Integer getApplyQuantity(){
      return this.applyQuantity;
}
 public void setPaymentType( Integer paymentType){
     this.paymentType=paymentType;
}
 public Integer getPaymentType(){
      return this.paymentType;
}
 public void setConfirmQuantity( Integer confirmQuantity){
     this.confirmQuantity=confirmQuantity;
}
 public Integer getConfirmQuantity(){
      return this.confirmQuantity;
}
 public void setConfirmAmount( Integer confirmAmount){
     this.confirmAmount=confirmAmount;
}
 public Integer getConfirmAmount(){
      return this.confirmAmount;
}
 public void setApprovalCode( String approvalCode){
     this.approvalCode=approvalCode;
}
 public String getApprovalCode(){
      return this.approvalCode;
}
 public void setApprovalTitle( String approvalTitle){
     this.approvalTitle=approvalTitle;
}
 public String getApprovalTitle(){
      return this.approvalTitle;
}
 public void setApprovalRemark( String approvalRemark){
     this.approvalRemark=approvalRemark;
}
 public String getApprovalRemark(){
      return this.approvalRemark;
}
 public void setAccountingUnit( Long accountingUnit){
     this.accountingUnit=accountingUnit;
}
 public Long getAccountingUnit(){
      return this.accountingUnit;
}
 public void setAccountingAccount( Integer accountingAccount){
     this.accountingAccount=accountingAccount;
}
 public Integer getAccountingAccount(){
      return this.accountingAccount;
}
 public void setDiscountCode( String discountCode){
     this.discountCode=discountCode;
}
 public String getDiscountCode(){
      return this.discountCode;
}
 public void setDiscountTitle( String discountTitle){
     this.discountTitle=discountTitle;
}
 public String getDiscountTitle(){
      return this.discountTitle;
}
 public void setStatus( Integer status){
     this.status=status;
}
 public Integer getStatus(){
      return this.status;
}
 public void setAuditDate( Date auditDate){
     this.auditDate=auditDate;
}
 public Date getAuditDate(){
      return this.auditDate;
}
 public void setChargeId( Long chargeId){
     this.chargeId=chargeId;
}
 public Long getChargeId(){
      return this.chargeId;
}
 public void setCreateBy( Long createBy){
     this.createBy=createBy;
}
 public Long getCreateBy(){
      return this.createBy;
}
 public void setCreateDate( Date createDate){
     this.createDate=createDate;
}
 public Date getCreateDate(){
      return this.createDate;
}
 public void setUpdateBy( Long updateBy){
     this.updateBy=updateBy;
}
 public Long getUpdateBy(){
      return this.updateBy;
}
 public void setUpdateDate( Date updateDate){
     this.updateDate=updateDate;
}
 public Date getUpdateDate(){
      return this.updateDate;
}
public Long getOrgId() {
	return orgId;
}
public void setOrgId(Long orgId) {
	this.orgId = orgId;
}
}
