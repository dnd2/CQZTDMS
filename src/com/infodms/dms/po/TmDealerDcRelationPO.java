
package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;
@SuppressWarnings("serial")
public class TmDealerDcRelationPO extends PO{
	private Long relationId;
	private Long dealerId;
	private Long dcId;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	
	
	 public void setRelationId( Long relationId){
	     this.relationId=relationId;
	}
	 public Long getRelationId(){
	      return this.relationId;
	}
	 public void setDealerId( Long dealerId){
	     this.dealerId=dealerId;
	}
	 public Long getDealerId(){
	      return this.dealerId;
	}
	 public void setDcId( Long dcId){
	     this.dcId=dcId;
	}
	 public Long getDcId(){
	      return this.dcId;
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

}
