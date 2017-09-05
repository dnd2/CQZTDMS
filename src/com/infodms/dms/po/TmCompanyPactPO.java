package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmCompanyPactPO extends PO{
private Long pactId;
private String pactNo;
private String pactName;
private String remart;
private Integer status;
private Long oemCompanyId;
private String parentFleetType ;
private Long isAllowApply ;
private Long createBy;
private Date createDate;
private Long updateBy;
private Date updateDate;

public String getParentFleetType() {
	return parentFleetType;
}
public void setParentFleetType(String parentFleetType) {
	this.parentFleetType = parentFleetType;
}
public Long getIsAllowApply() {
	return isAllowApply;
}
public void setIsAllowApply(Long isAllowApply) {
	this.isAllowApply = isAllowApply;
}
 public void setPactId( Long pactId){
     this.pactId=pactId;
}
 public Long getPactId(){
      return this.pactId;
}
 public void setPactNo( String pactNo){
     this.pactNo=pactNo;
}
 public String getPactNo(){
      return this.pactNo;
}
 public void setPactName( String pactName){
     this.pactName=pactName;
}
 public String getPactName(){
      return this.pactName;
}
 public void setRemart( String remart){
     this.remart=remart;
}
 public String getRemart(){
      return this.remart;
}
 public void setStatus( Integer status){
     this.status=status;
}
 public Integer getStatus(){
      return this.status;
}
 public void setOemCompanyId( Long oemCompanyId){
     this.oemCompanyId=oemCompanyId;
}
 public Long getOemCompanyId(){
      return this.oemCompanyId;
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
