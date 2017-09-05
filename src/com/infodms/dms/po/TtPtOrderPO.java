package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPtOrderPO  extends PO{
private Long orderId;
private String orderNo;
private Long dealerId;
private Long dcId;
private Integer highOperationType;
private Long operationId;
private Date requireDate;
private Date raiseDate;
private Integer transType;
private Integer orderStatus;
private Float orderPrice;
private Float discountRate;
private Integer itemCount;
private Integer submitTimes;
private String remark;
private Date dmsuploadDate;
private Integer nodeStatus;
private Date nodeDate;
private Integer finishedCount;
private Date createDate;
private Long createBy;
private Date updateDate;
private Long updateBy;
private Integer ver;
private Integer isDel;
private Integer isArc;
private Integer orderType;
private Long highDealerId;
private Integer raiseTimes;


 public Integer getOrderType() {
	return orderType;
}
public void setOrderType(Integer orderType) {
	this.orderType = orderType;
}
public Long getHighDealerId() {
	return highDealerId;
}
public void setHighDealerId(Long highDealerId) {
	this.highDealerId = highDealerId;
}
public Integer getRaiseTimes() {
	return raiseTimes;
}
public void setRaiseTimes(Integer raiseTimes) {
	this.raiseTimes = raiseTimes;
}
public void setOrderId( Long orderId){
     this.orderId=orderId;
}
 public Long getOrderId(){
      return this.orderId;
}
 public void setOrderNo( String orderNo){
     this.orderNo=orderNo;
}
 public String getOrderNo(){
      return this.orderNo;
}
 public void setDealerId( Long dealerId){
     this.dealerId=dealerId;
}
 public Long getDealerId(){
      return this.dealerId;
}
 public void setDcId( Long dcId){
     this.dcId=dcId;
}
 public Long getDcId(){
      return this.dcId;
}
 public void setHighOperationType( Integer highOperationType){
     this.highOperationType=highOperationType;
}
 public Integer getHighOperationType(){
      return this.highOperationType;
}
 public void setOperationId( Long operationId){
     this.operationId=operationId;
}
 public Long getOperationId(){
      return this.operationId;
}
 public void setRequireDate( Date requireDate){
     this.requireDate=requireDate;
}
 public Date getRequireDate(){
      return this.requireDate;
}
 public void setRaiseDate( Date raiseDate){
     this.raiseDate=raiseDate;
}
 public Date getRaiseDate(){
      return this.raiseDate;
}
 public void setTransType( Integer transType){
     this.transType=transType;
}
 public Integer getTransType(){
      return this.transType;
}
 public void setOrderStatus( Integer orderStatus){
     this.orderStatus=orderStatus;
}
 public Integer getOrderStatus(){
      return this.orderStatus;
}
 public void setOrderPrice( Float orderPrice){
     this.orderPrice=orderPrice;
}
 public Float getOrderPrice(){
      return this.orderPrice;
}
 public void setDiscountRate( Float discountRate){
     this.discountRate=discountRate;
}
 public Float getDiscountRate(){
      return this.discountRate;
}
 public void setItemCount( Integer itemCount){
     this.itemCount=itemCount;
}
 public Integer getItemCount(){
      return this.itemCount;
}
 public void setSubmitTimes( Integer submitTimes){
     this.submitTimes=submitTimes;
}
 public Integer getSubmitTimes(){
      return this.submitTimes;
}
 public void setRemark( String remark){
     this.remark=remark;
}
 public String getRemark(){
      return this.remark;
}
 public void setDmsuploadDate( Date dmsuploadDate){
     this.dmsuploadDate=dmsuploadDate;
}
 public Date getDmsuploadDate(){
      return this.dmsuploadDate;
}
 public void setNodeStatus( Integer nodeStatus){
     this.nodeStatus=nodeStatus;
}
 public Integer getNodeStatus(){
      return this.nodeStatus;
}
 public void setNodeDate( Date nodeDate){
     this.nodeDate=nodeDate;
}
 public Date getNodeDate(){
      return this.nodeDate;
}
 public void setFinishedCount( Integer finishedCount){
     this.finishedCount=finishedCount;
}
 public Integer getFinishedCount(){
      return this.finishedCount;
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
