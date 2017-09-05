package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmPtOrderParamPO extends PO{
private Long paramId;
private Long dealerId;
private Integer orderMaxLines;
private Integer allowSubmitTimes;
private Float discountRate;
private Integer cycleType;
private Integer isSend;
private Date createDate;
private Long createBy;
private Date updateDate;
private Long updateBy;
private Integer ver;
private Integer isDel;
private Integer isArc;


 public void setParamId( Long paramId){
     this.paramId=paramId;
}
 public Long getParamId(){
      return this.paramId;
}
 public void setDealerId( Long dealerId){
     this.dealerId=dealerId;
}
 public Long getDealerId(){
      return this.dealerId;
}
 public void setOrderMaxLines( Integer orderMaxLines){
     this.orderMaxLines=orderMaxLines;
}
 public Integer getOrderMaxLines(){
      return this.orderMaxLines;
}
 public void setAllowSubmitTimes( Integer allowSubmitTimes){
     this.allowSubmitTimes=allowSubmitTimes;
}
 public Integer getAllowSubmitTimes(){
      return this.allowSubmitTimes;
}
 public void setDiscountRate( Float discountRate){
     this.discountRate=discountRate;
}
 public Float getDiscountRate(){
      return this.discountRate;
}
 public void setCycleType( Integer cycleType){
     this.cycleType=cycleType;
}
 public Integer getCycleType(){
      return this.cycleType;
}
 public void setIsSend( Integer isSend){
     this.isSend=isSend;
}
 public Integer getIsSend(){
      return this.isSend;
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
 public void setIsArc( Integer isArc){
     this.isArc=isArc;
}
 public Integer getIsArc(){
      return this.isArc;
}
}
