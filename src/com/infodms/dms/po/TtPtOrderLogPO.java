package com.infodms.dms.po;
import java.util.Date;

import com.infoservice.po3.bean.PO;
@SuppressWarnings("serial")
public class TtPtOrderLogPO  extends PO{
private Long logId;
private Long orderId;
private Integer orgType;
private Long ogrId;
private Long userId;
private Integer nodeStatus;
private Date opertateDate;
private String opertateRemark;
private Integer isInterface;
private String interfaceOrg;
private String interfaceUser;
private Date createDate;
private Long createBy;
private Date updateDate;
private Long updateBy;


 public void setLogId( Long logId){
     this.logId=logId;
}
 public Long getLogId(){
      return this.logId;
}
 public void setOrderId( Long orderId){
     this.orderId=orderId;
}
 public Long getOrderId(){
      return this.orderId;
}
 public void setOrgType( Integer orgType){
     this.orgType=orgType;
}
 public Integer getOrgType(){
      return this.orgType;
}
 public void setOgrId( Long ogrId){
     this.ogrId=ogrId;
}
 public Long getOgrId(){
      return this.ogrId;
}
 public void setUserId( Long userId){
     this.userId=userId;
}
 public Long getUserId(){
      return this.userId;
}
 public void setNodeStatus( Integer nodeStatus){
     this.nodeStatus=nodeStatus;
}
 public Integer getNodeStatus(){
      return this.nodeStatus;
}
 public void setOpertateDate( Date opertateDate){
     this.opertateDate=opertateDate;
}
 public Date getOpertateDate(){
      return this.opertateDate;
}
 public void setOpertateRemark( String opertateRemark){
     this.opertateRemark=opertateRemark;
}
 public String getOpertateRemark(){
      return this.opertateRemark;
}
 public void setIsInterface( Integer isInterface){
     this.isInterface=isInterface;
}
 public Integer getIsInterface(){
      return this.isInterface;
}
 public void setInterfaceOrg( String interfaceOrg){
     this.interfaceOrg=interfaceOrg;
}
 public String getInterfaceOrg(){
      return this.interfaceOrg;
}
 public void setInterfaceUser( String interfaceUser){
     this.interfaceUser=interfaceUser;
}
 public String getInterfaceUser(){
      return this.interfaceUser;
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
