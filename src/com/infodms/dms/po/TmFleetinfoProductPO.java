package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;
@SuppressWarnings("serial")
public class TmFleetinfoProductPO extends PO{
private Long fleetId;
private Long requireId;
private Long brandId;
private Long series;
private Long modelId;
private String remark;
private Integer requireNum;
private Date createDate;
private Long createBy;
private Long updateBy;
private Date updateDate;
private String dealerArea;
private Date giveCarDate;


 public void setFleetId( Long fleetId){
     this.fleetId=fleetId;
}
 public Long getFleetId(){
      return this.fleetId;
}
 public void setRequireId( Long requireId){
     this.requireId=requireId;
}
 public Long getRequireId(){
      return this.requireId;
}
 public void setBrandId( Long brandId){
     this.brandId=brandId;
}
 public Long getBrandId(){
      return this.brandId;
}
 public void setSeries( Long series){
     this.series=series;
}
 public Long getSeries(){
      return this.series;
}
 public void setModelId( Long modelId){
     this.modelId=modelId;
}
 public Long getModelId(){
      return this.modelId;
}
 public void setRemark( String remark){
     this.remark=remark;
}
 public String getRemark(){
      return this.remark;
}
 public void setRequireNum( Integer requireNum){
     this.requireNum=requireNum;
}
 public Integer getRequireNum(){
      return this.requireNum;
}
 public void setCreateDate( Date createDate){
     this.createDate=createDate;
}
 public Date getCreateDate(){
      return this.createDate;
}
 public Long getCreateBy() {
	return createBy;
}
public void setCreateBy(Long createBy) {
	this.createBy = createBy;
}
public Long getUpdateBy() {
	return updateBy;
}
public void setUpdateBy(Long updateBy) {
	this.updateBy = updateBy;
}
public void setUpdateDate( Date updateDate){
     this.updateDate=updateDate;
}
 public Date getUpdateDate(){
      return this.updateDate;
}
 public void setDealerArea( String dealerArea){
     this.dealerArea=dealerArea;
}
 public String getDealerArea(){
      return this.dealerArea;
}
 public void setGiveCarDate( Date giveCarDate){
     this.giveCarDate=giveCarDate;
}
 public Date getGiveCarDate(){
      return this.giveCarDate;
}
}
