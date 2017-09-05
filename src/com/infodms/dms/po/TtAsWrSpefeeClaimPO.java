package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrSpefeeClaimPO extends PO{
private Long id;
private Long feeId;
private Long claimId;
private String claimNo;
private String series;
private String model;
private String vin;
private String engineNo;
private Date productDate;
private Double mileage;
private Date createDate;
private Long createBy;
private Date updateDate;
private Long updateBy;
private Date claimDate;


 public void setId( Long id){
     this.id=id;
}
 public Long getId(){
      return this.id;
}
 public void setFeeId( Long feeId){
     this.feeId=feeId;
}
 public Long getFeeId(){
      return this.feeId;
}
 public void setClaimId( Long claimId){
     this.claimId=claimId;
}
 public Long getClaimId(){
      return this.claimId;
}
 public void setClaimNo( String claimNo){
     this.claimNo=claimNo;
}
 public String getClaimNo(){
      return this.claimNo;
}
 public void setSeries( String series){
     this.series=series;
}
 public String getSeries(){
      return this.series;
}
 public void setModel( String model){
     this.model=model;
}
 public String getModel(){
      return this.model;
}
 public void setVin( String vin){
     this.vin=vin;
}
 public String getVin(){
      return this.vin;
}
 public void setEngineNo( String engineNo){
     this.engineNo=engineNo;
}
 public String getEngineNo(){
      return this.engineNo;
}
 public void setProductDate( Date productDate){
     this.productDate=productDate;
}
 public Date getProductDate(){
      return this.productDate;
}
 public void setMileage( Double mileage){
     this.mileage=mileage;
}
 public Double getMileage(){
      return this.mileage;
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
 public void setClaimDate( Date claimDate){
     this.claimDate=claimDate;
}
 public Date getClaimDate(){
      return this.claimDate;
}
}
