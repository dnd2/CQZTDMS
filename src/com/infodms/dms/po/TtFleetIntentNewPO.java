package com.infodms.dms.po;
import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtFleetIntentNewPO extends PO{
private Long intentId;
private Long fleetId;
private Long seriesId;
private Long intentCount;
private Double intentAmount;
private Double normAmount;
private Double countAmount;
private Double intentPoint;
private Integer status;
private Long createBy;
private Date createDate;
private Long updateBy;
private Date updateDate;
private String remark;
private Long contractId;
public Double getIntentPoint() {
	return intentPoint;
}
public void setIntentPoint(Double intentPoint) {
	this.intentPoint = intentPoint;
}
 public Long getContractId() {
	return contractId;
}
public void setContractId(Long contractId) {
	this.contractId = contractId;
}
public void setIntentId( Long intentId){
     this.intentId=intentId;
}
 public Long getIntentId(){
      return this.intentId;
}
 public void setFleetId( Long fleetId){
     this.fleetId=fleetId;
}
 public Long getFleetId(){
      return this.fleetId;
}
 public void setSeriesId( Long seriesId){
     this.seriesId=seriesId;
}
 public Long getSeriesId(){
      return this.seriesId;
}
 public void setIntentCount( Long intentCount){
     this.intentCount=intentCount;
}
 public Long getIntentCount(){
      return this.intentCount;
}
 public void setIntentAmount( Double intentAmount){
     this.intentAmount=intentAmount;
}
 public Double getIntentAmount(){
      return this.intentAmount;
}
 public void setNormAmount( Double normAmount){
     this.normAmount=normAmount;
}
 public Double getNormAmount(){
      return this.normAmount;
}
 public void setCountAmount( Double countAmount){
     this.countAmount=countAmount;
}
 public Double getCountAmount(){
      return this.countAmount;
}
 public void setStatus( Integer status){
     this.status=status;
}
 public Integer getStatus(){
      return this.status;
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
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
}
