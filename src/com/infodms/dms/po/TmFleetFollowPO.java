

package com.infodms.dms.po;
import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmFleetFollowPO extends PO{
private Long followId;
private Long fleetId;
private Date followDate;
private String followRemark;
private Integer status;
private Long createBy;
private Date createDate;
private Long updateBy;
private Date updateDate;
private Long intentId;

private Long followRank;
private String followTheme;
private String followPerson;


 public Long getFollowRank() {
	return followRank;
}
public void setFollowRank(Long followRank) {
	this.followRank = followRank;
}
public String getFollowTheme() {
	return followTheme;
}
public void setFollowTheme(String followTheme) {
	this.followTheme = followTheme;
}
public String getFollowPerson() {
	return followPerson;
}
public void setFollowPerson(String followPerson) {
	this.followPerson = followPerson;
}
public void setFollowId( Long followId){
     this.followId=followId;
}
 public Long getFollowId(){
      return this.followId;
}
 public void setFleetId( Long fleetId){
     this.fleetId=fleetId;
}
 public Long getFleetId(){
      return this.fleetId;
}
 public void setFollowDate( Date followDate){
     this.followDate=followDate;
}
 public Date getFollowDate(){
      return this.followDate;
}
 public void setFollowRemark( String followRemark){
     this.followRemark=followRemark;
}
 public String getFollowRemark(){
      return this.followRemark;
}
 public void setStatus( Integer status){
     this.status=status;
}
 public Integer getStatus(){
      return this.status;
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
 public void setIntentId( Long intentId){
     this.intentId=intentId;
}
 public Long getIntentId(){
      return this.intentId;
}
}
