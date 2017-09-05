package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;
public class TmPromotionAuditlogPO  extends PO{
private Long auditlogId;
private Long promotionApplyId;
private Integer logType;
private Long orgId;
private String auditlog;
private Long createBy;
private Date createDate;
private Long updateBy;
private Date updateDate;


 public void setAuditlogId( Long auditlogId){
     this.auditlogId=auditlogId;
}
 public Long getAuditlogId(){
      return this.auditlogId;
}
 public void setPromotionApplyId( Long promotionApplyId){
     this.promotionApplyId=promotionApplyId;
}
 public Long getPromotionApplyId(){
      return this.promotionApplyId;
}
 public void setLogType( Integer logType){
     this.logType=logType;
}
 public Integer getLogType(){
      return this.logType;
}
 public void setOrgId( Long orgId){
     this.orgId=orgId;
}
 public Long getOrgId(){
      return this.orgId;
}
 public void setAuditlog( String auditlog){
     this.auditlog=auditlog;
}
 public String getAuditlog(){
      return this.auditlog;
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
}