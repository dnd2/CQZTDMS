package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmPtOrdparamDatePO extends PO{
private Long processId;
private Long paramId;
private Integer startDate;
private Integer endDate;
private Integer handleDate;
private Date createDate;
private Long createBy;
private Date updateDate;
private Long updateBy;


 public void setProcessId( Long processId){
     this.processId=processId;
}
 public Long getProcessId(){
      return this.processId;
}
 public void setParamId( Long paramId){
     this.paramId=paramId;
}
 public Long getParamId(){
      return this.paramId;
}
 public void setStartDate( Integer startDate){
     this.startDate=startDate;
}
 public Integer getStartDate(){
      return this.startDate;
}
 public void setEndDate( Integer endDate){
     this.endDate=endDate;
}
 public Integer getEndDate(){
      return this.endDate;
}
 public void setHandleDate( Integer handleDate){
     this.handleDate=handleDate;
}
 public Integer getHandleDate(){
      return this.handleDate;
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
