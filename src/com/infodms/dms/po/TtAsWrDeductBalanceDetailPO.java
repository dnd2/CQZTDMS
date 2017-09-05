

package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrDeductBalanceDetailPO extends PO{
private Long id;
private Long balanceId;
private Long deductId;
private Date createDate;
private Long createBy;
private Date updateDate;
private Long updateBy;
private Long oemCompanyId;


 public void setId( Long id){
     this.id=id;
}
 public Long getId(){
      return this.id;
}
 public void setBalanceId( Long balanceId){
     this.balanceId=balanceId;
}
 public Long getBalanceId(){
      return this.balanceId;
}
 public void setDeductId( Long deductId){
     this.deductId=deductId;
}
 public Long getDeductId(){
      return this.deductId;
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
 public void setOemCompanyId( Long oemCompanyId){
     this.oemCompanyId=oemCompanyId;
}
 public Long getOemCompanyId(){
      return this.oemCompanyId;
}
}
