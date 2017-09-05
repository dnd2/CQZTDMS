package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;

public class TmFleetPromotionPO extends PO{
private Long promotionId;
private Long brandId;
private Long orgId;
private Long fleetId;
private Integer promotionType;
private String promotionCode;
private Integer applyType;
private Integer isCredit;
private Integer creditType;
private Integer creditMonth;
private String remark;
private Integer status;
private Date auditDate;
private Date createDate;
private Long createBy;
private Long updateBy;
private Date updateDate;


 public void setPromotionId( Long promotionId){
     this.promotionId=promotionId;
}
 public Long getPromotionId(){
      return this.promotionId;
}
 public void setBrandId( Long brandId){
     this.brandId=brandId;
}
 public Long getBrandId(){
      return this.brandId;
}
 public void setOrgId( Long orgId){
     this.orgId=orgId;
}
 public Long getOrgId(){
      return this.orgId;
}
 public void setFleetId( Long fleetId){
     this.fleetId=fleetId;
}
 public Long getFleetId(){
      return this.fleetId;
}
 public void setPromotionType( Integer promotionType){
     this.promotionType=promotionType;
}
 public Integer getPromotionType(){
      return this.promotionType;
}
 public void setApplyType( Integer applyType){
     this.applyType=applyType;
}
 public Integer getApplyType(){
      return this.applyType;
}
 public void setIsCredit( Integer isCredit){
     this.isCredit=isCredit;
}
 public Integer getIsCredit(){
      return this.isCredit;
}
 public void setCreditType( Integer creditType){
     this.creditType=creditType;
}
 public Integer getCreditType(){
      return this.creditType;
}
 public void setCreditMonth( Integer creditMonth){
     this.creditMonth=creditMonth;
}
 public Integer getCreditMonth(){
      return this.creditMonth;
}
 public void setRemark( String remark){
     this.remark=remark;
}
 public String getRemark(){
      return this.remark;
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
public String getPromotionCode() {
	return promotionCode;
}
public void setPromotionCode(String promotionCode) {
	this.promotionCode = promotionCode;
}
}