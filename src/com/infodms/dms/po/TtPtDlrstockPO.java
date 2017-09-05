package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPtDlrstockPO extends PO{
private Long stockId;
private Long dealerId;
private Long partId;
private Integer paperQuantity;
private Integer actualQuantity;
private Integer safeQuantity;
private Long createBy;
private Date createDate;
private Long updateBy;
private Date updateDate;
private Integer isDel;
private Integer isArc;


 public void setStockId( Long stockId){
     this.stockId=stockId;
}
 public Long getStockId(){
      return this.stockId;
}
 public void setDealerId( Long dealerId){
     this.dealerId=dealerId;
}
 public Long getDealerId(){
      return this.dealerId;
}
 public void setPartId( Long partId){
     this.partId=partId;
}
 public Long getPartId(){
      return this.partId;
}
 public void setPaperQuantity( Integer paperQuantity){
     this.paperQuantity=paperQuantity;
}
 public Integer getPaperQuantity(){
      return this.paperQuantity;
}
 public void setActualQuantity( Integer actualQuantity){
     this.actualQuantity=actualQuantity;
}
 public Integer getActualQuantity(){
      return this.actualQuantity;
}
 public void setSafeQuantity( Integer safeQuantity){
     this.safeQuantity=safeQuantity;
}
 public Integer getSafeQuantity(){
      return this.safeQuantity;
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
