package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;
public class TmPreferentialApplyPO  extends PO{
private Long preferentialApplyId;
private Long promotionId;
private Integer applyQuantity;
private Integer confirmQuantity;
private Integer confirmAmount;
private Integer applayAmount;
private String approveRemark;
private Long chargeId;
private Integer status;
private Date auditDate;
private Date createDate;
private Long createBy;
private Long updateBy;
private Date updateDate;


 public void setPreferentialApplyId( Long preferentialApplyId){
     this.preferentialApplyId=preferentialApplyId;
}
 public Long getPreferentialApplyId(){
      return this.preferentialApplyId;
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
 public void setChargeId( Long chargeId){
     this.chargeId=chargeId;
}
 public Long getChargeId(){
      return this.chargeId;
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
 public void setCreateDate( Date createDate){
     this.createDate=createDate;
}
 public Date getCreateDate(){
      return this.createDate;
}
 public void setCreateBy( Long createBy){
     this.createBy=createBy;
}
 public Long getCreateBy(){
      return this.createBy;
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
public String getApproveRemark() {
	return approveRemark;
}
public void setApproveRemark(String approveRemark) {
	this.approveRemark = approveRemark;
}
public Integer getApplayAmount() {
	return applayAmount;
}
public void setApplayAmount(Integer applayAmount) {
	this.applayAmount = applayAmount;
}
}