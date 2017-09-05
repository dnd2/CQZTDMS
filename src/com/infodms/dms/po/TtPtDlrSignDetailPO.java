

package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;
@SuppressWarnings("serial")
public class TtPtDlrSignDetailPO extends PO{
private Long detailId;
private Long signId;
private Long partId;
private Integer signQuantity;
private String remark;
private Long createBy;
private Date createDate;
private Long updateBy;
private Date updateDate;


 public void setDetailId( Long detailId){
     this.detailId=detailId;
}
 public Long getDetailId(){
      return this.detailId;
}
 public void setSignId( Long signId){
     this.signId=signId;
}
 public Long getSignId(){
      return this.signId;
}
 public void setPartId( Long partId){
     this.partId=partId;
}
 public Long getPartId(){
      return this.partId;
}
 public void setSignQuantity( Integer signQuantity){
     this.signQuantity=signQuantity;
}
 public Integer getSignQuantity(){
      return this.signQuantity;
}
 public void setRemark( String remark){
     this.remark=remark;
}
 public String getRemark(){
      return this.remark;
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
