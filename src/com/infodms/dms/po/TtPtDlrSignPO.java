package com.infodms.dms.po;
import java.util.Date;

import com.infoservice.po3.bean.PO;
@SuppressWarnings("serial")
public class TtPtDlrSignPO extends PO{
private Long signId;
private String signNo;
private Long orderId;
private String doNo;
private Long dealerId;
private String transNo;
private Date signDate;
private String signUserId;
private Integer status;
private Date createDate;
private Long createBy;
private Date updateDate;
private Long updateBy;
private Integer ver;
private Integer isDel;


 public void setSignId( Long signId){
     this.signId=signId;
}
 public Long getSignId(){
      return this.signId;
}
 public void setOrderId( Long orderId){
     this.orderId=orderId;
}
 public Long getOrderId(){
      return this.orderId;
}
 public void setDoNo( String doNo){
     this.doNo=doNo;
}
 public String getDoNo(){
      return this.doNo;
}
 public void setDealerId( Long dealerId){
     this.dealerId=dealerId;
}
 public Long getDealerId(){
      return this.dealerId;
}
 public void setTransNo( String transNo){
     this.transNo=transNo;
}
 public String getTransNo(){
      return this.transNo;
}
 public void setSignDate( Date signDate){
     this.signDate=signDate;
}
 public Date getSignDate(){
      return this.signDate;
}
 public void setSignUserId( String signUserId){
     this.signUserId=signUserId;
}
 public String getSignUserId(){
      return this.signUserId;
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
 public void setVer( Integer ver){
     this.ver=ver;
}
 public Integer getVer(){
      return this.ver;
}
 public void setIsDel( Integer isDel){
     this.isDel=isDel;
}
 public Integer getIsDel(){
      return this.isDel;
}
public String getSignNo() {
	return signNo;
}
public void setSignNo(String signNo) {
	this.signNo = signNo;
}
}
