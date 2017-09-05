package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;
public class TmPromotionApplyDetailPO  extends PO{
private Long preferentialApplyDetailId;
private Long promotionApplyId;
private Long vehicleId;
private Integer applayAmount;
private Integer isConfirm;
private Long createBy;
private Date createDate;
private Long updateBy;
private Date updateDate;


 public void setPreferentialApplyDetailId( Long preferentialApplyDetailId){
     this.preferentialApplyDetailId=preferentialApplyDetailId;
}
 public Long getPreferentialApplyDetailId(){
      return this.preferentialApplyDetailId;
}
 public void setPromotionApplyId( Long promotionApplyId){
     this.promotionApplyId=promotionApplyId;
}
 public Long getPromotionApplyId(){
      return this.promotionApplyId;
}
 public void setVehicleId( Long vehicleId){
     this.vehicleId=vehicleId;
}
 public Long getVehicleId(){
      return this.vehicleId;
}
 public void setApplayAmount( Integer applayAmount){
     this.applayAmount=applayAmount;
}
 public Integer getApplayAmount(){
      return this.applayAmount;
}
 public void setIsConfirm( Integer isConfirm){
     this.isConfirm=isConfirm;
}
 public Integer getIsConfirm(){
      return this.isConfirm;
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
