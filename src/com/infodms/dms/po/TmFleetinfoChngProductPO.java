package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;
@SuppressWarnings("serial")
public class TmFleetinfoChngProductPO extends PO{
private Long requireChngId;
private Long recordId;
private Long brandId;
private Long modelId;
private Long series;
private String remark;
private Integer requireNum;
private String dealerArea;
private Date giveCarDate;
private Date createDate;
private Long createBy;
private Long updateBy;
private Date updateDate;



 public void setRequireChngId( Long requireChngId){
     this.requireChngId=requireChngId;
}
 public Long getRequireChngId(){
      return this.requireChngId;
}
 public void setBrandId( Long brandId){
     this.brandId=brandId;
}
 public Long getBrandId(){
      return this.brandId;
}
 public void setModelId( Long modelId){
     this.modelId=modelId;
}
 public Long getModelId(){
      return this.modelId;
}
 public void setSeries( Long series){
     this.series=series;
}
 public Long getSeries(){
      return this.series;
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
public Long getRecordId() {
	return recordId;
}
public void setRecordId(Long recordId) {
	this.recordId = recordId;
}
}
