package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;

public class TmFleetPromotionProductPO  extends PO{
private Long promotionProductId;
private Long promotionId;
private String productCode;
private Integer applyAmount;
private Integer contractPrice;
private Integer guidePrice;
private Integer normalPrice;
private Double preferentialPoint;
private Integer preferentialAmount;
private Long createBy;
private Date createDate;
private Long updateBy;
private Date updateDate;


 public void setPromotionProductId( Long promotionProductId){
     this.promotionProductId=promotionProductId;
}
 public Long getPromotionProductId(){
      return this.promotionProductId;
}
 public void setPromotionId( Long promotionId){
     this.promotionId=promotionId;
}
 public Long getPromotionId(){
      return this.promotionId;
}
 public void setProductCode( String productCode){
     this.productCode=productCode;
}
 public String getProductCode(){
      return this.productCode;
}
 public void setApplyAmount( Integer applyAmount){
     this.applyAmount=applyAmount;
}
 public Integer getApplyAmount(){
      return this.applyAmount;
}
 public void setContractPrice( Integer contractPrice){
     this.contractPrice=contractPrice;
}
 public Integer getContractPrice(){
      return this.contractPrice;
}
 public void setGuidePrice( Integer guidePrice){
     this.guidePrice=guidePrice;
}
 public Integer getGuidePrice(){
      return this.guidePrice;
}
 public void setNormalPrice( Integer normalPrice){
     this.normalPrice=normalPrice;
}
 public Integer getNormalPrice(){
      return this.normalPrice;
}
 public void setPreferentialAmount( Integer preferentialAmount){
     this.preferentialAmount=preferentialAmount;
}
 public Integer getPreferentialAmount(){
      return this.preferentialAmount;
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
public Double getPreferentialPoint() {
	return preferentialPoint;
}
public void setPreferentialPoint(Double preferentialPoint) {
	this.preferentialPoint = preferentialPoint;
}
}