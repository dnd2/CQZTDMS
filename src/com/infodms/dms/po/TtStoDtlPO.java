

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtStoDtlPO extends PO{
private Long dtlId;
private Long stoId;
private Long materialId;
private String batchNo;
private Integer amount;
private Double singlePrice;
private Double totalPrice;


 public void setDtlId( Long dtlId){
     this.dtlId=dtlId;
}
 public Long getDtlId(){
      return this.dtlId;
}
 public void setStoId( Long stoId){
     this.stoId=stoId;
}
 public Long getStoId(){
      return this.stoId;
}
 public void setMaterialId( Long materialId){
     this.materialId=materialId;
}
 public Long getMaterialId(){
      return this.materialId;
}
 public void setBatchNo( String batchNo){
     this.batchNo=batchNo;
}
 public String getBatchNo(){
      return this.batchNo;
}
 public void setAmount( Integer amount){
     this.amount=amount;
}
 public Integer getAmount(){
      return this.amount;
}
 public void setSinglePrice( Double singlePrice){
     this.singlePrice=singlePrice;
}
 public Double getSinglePrice(){
      return this.singlePrice;
}
 public void setTotalPrice( Double totalPrice){
     this.totalPrice=totalPrice;
}
 public Double getTotalPrice(){
      return this.totalPrice;
}

}
