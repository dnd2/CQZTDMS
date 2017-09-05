

package com.infodms.dms.po;
import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrSpefeeAuditingPO extends PO{
private Long id;
private Long feeId;
private Date auditingDate;
private String auditingPerson;
private Long presonDept;
private String auditingOpinion;
private Integer status;
private Date createDate;
private Long createBy;
private Date updateDate;
private Long updateBy;


 public void setId( Long id){
     this.id=id;
}
 public Long getId(){
      return this.id;
}
 public void setFeeId( Long feeId){
     this.feeId=feeId;
}
 public Long getFeeId(){
      return this.feeId;
}
 public void setAuditingDate( Date auditingDate){
     this.auditingDate=auditingDate;
}
 public Date getAuditingDate(){
      return this.auditingDate;
}
 public void setAuditingPerson( String auditingPerson){
     this.auditingPerson=auditingPerson;
}
 public String getAuditingPerson(){
      return this.auditingPerson;
}
 public void setPresonDept( Long presonDept){
     this.presonDept=presonDept;
}
 public Long getPresonDept(){
      return this.presonDept;
}
 public void setAuditingOpinion( String auditingOpinion){
     this.auditingOpinion=auditingOpinion;
}
 public String getAuditingOpinion(){
      return this.auditingOpinion;
}
 public void setStatus( Integer status){
     this.status=status;
}
 public Integer getStatus(){
      return this.status;
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
 public void setUpdateDate( Date updateDate){
     this.updateDate=updateDate;
}
 public Date getUpdateDate(){
      return this.updateDate;
}
 public void setUpdateBy( Long updateBy){
     this.updateBy=updateBy;
}
 public Long getUpdateBy(){
      return this.updateBy;
}
}
