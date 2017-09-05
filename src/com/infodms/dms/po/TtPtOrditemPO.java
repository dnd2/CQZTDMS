package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;
@SuppressWarnings("serial")
public class TtPtOrditemPO extends PO{
private Long detailId;
private Long orderId;
private Long partId;
private Integer orderCount;
private String remark;
private Integer onfittingCount;
private Integer carryingCount;
private Integer receivedCount;
private Integer canceledCount;
private Integer replacedCount;
private Integer isReplaced;
private Integer orderItemStatus;
private Date createDate;
private Long createBy;
private Date updateDate;
private Long updateBy;


 public void setDetailId( Long detailId){
     this.detailId=detailId;
}
 public Long getDetailId(){
      return this.detailId;
}
 public void setOrderId( Long orderId){
     this.orderId=orderId;
}
 public Long getOrderId(){
      return this.orderId;
}
 public void setPartId( Long partId){
     this.partId=partId;
}
 public Long getPartId(){
      return this.partId;
}
 public void setOrderCount( Integer orderCount){
     this.orderCount=orderCount;
}
 public Integer getOrderCount(){
      return this.orderCount;
}
 public void setRemark( String remark){
     this.remark=remark;
}
 public String getRemark(){
      return this.remark;
}
 public void setOnfittingCount( Integer onfittingCount){
     this.onfittingCount=onfittingCount;
}
 public Integer getOnfittingCount(){
      return this.onfittingCount;
}
 public void setCarryingCount( Integer carryingCount){
     this.carryingCount=carryingCount;
}
 public Integer getCarryingCount(){
      return this.carryingCount;
}
 public void setReceivedCount( Integer receivedCount){
     this.receivedCount=receivedCount;
}
 public Integer getReceivedCount(){
      return this.receivedCount;
}
 public void setCanceledCount( Integer canceledCount){
     this.canceledCount=canceledCount;
}
 public Integer getCanceledCount(){
      return this.canceledCount;
}
 public void setReplacedCount( Integer replacedCount){
     this.replacedCount=replacedCount;
}
 public Integer getReplacedCount(){
      return this.replacedCount;
}
 public void setIsReplaced( Integer isReplaced){
     this.isReplaced=isReplaced;
}
 public Integer getIsReplaced(){
      return this.isReplaced;
}
 public void setOrderItemStatus( Integer orderItemStatus){
     this.orderItemStatus=orderItemStatus;
}
 public Integer getOrderItemStatus(){
      return this.orderItemStatus;
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
