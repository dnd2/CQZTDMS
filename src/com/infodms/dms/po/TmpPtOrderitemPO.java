package com.infodms.dms.po;
import java.util.Date;

import com.infoservice.po3.bean.PO;
@SuppressWarnings("serial")
public class TmpPtOrderitemPO extends PO{
private Integer rowNum;
private Long dealerId;
private Long userId;
private String partCode;
private String partName;
private String unit;
private Integer miniPack;
private Integer paperQuantity;
private Integer safeQuantity;
private String supplierCode;
private String supplierName;
private Integer orderQuantity;
private String remark;


 public void setRowNum( Integer rowNum){
     this.rowNum=rowNum;
}
 public Integer getRowNum(){
      return this.rowNum;
}
 public void setDealerId( Long dealerId){
     this.dealerId=dealerId;
}
 public Long getDealerId(){
      return this.dealerId;
}
 public void setUserId( Long userId){
     this.userId=userId;
}
 public Long getUserId(){
      return this.userId;
}
 public void setPartCode( String partCode){
     this.partCode=partCode;
}
 public String getPartCode(){
      return this.partCode;
}
 public void setPartName( String partName){
     this.partName=partName;
}
 public String getPartName(){
      return this.partName;
}
 public void setUnit( String unit){
     this.unit=unit;
}
 public String getUnit(){
      return this.unit;
}
 public void setMiniPack( Integer miniPack){
     this.miniPack=miniPack;
}
 public Integer getMiniPack(){
      return this.miniPack;
}
 public void setPaperQuantity( Integer paperQuantity){
     this.paperQuantity=paperQuantity;
}
 public Integer getPaperQuantity(){
      return this.paperQuantity;
}
 public void setSafeQuantity( Integer safeQuantity){
     this.safeQuantity=safeQuantity;
}
 public Integer getSafeQuantity(){
      return this.safeQuantity;
}
 public void setSupplierCode( String supplierCode){
     this.supplierCode=supplierCode;
}
 public String getSupplierCode(){
      return this.supplierCode;
}
 public void setSupplierName( String supplierName){
     this.supplierName=supplierName;
}
 public String getSupplierName(){
      return this.supplierName;
}
 public void setOrderQuantity( Integer orderQuantity){
     this.orderQuantity=orderQuantity;
}
 public Integer getOrderQuantity(){
      return this.orderQuantity;
}
 public void setRemark( String remark){
     this.remark=remark;
}
 public String getRemark(){
      return this.remark;
}

}
