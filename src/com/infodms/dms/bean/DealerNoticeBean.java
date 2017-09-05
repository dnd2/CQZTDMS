package com.infodms.dms.bean;

import java.util.Date;

public class DealerNoticeBean {

	private String funcId;  // 菜单id
	private String funcName;  // 菜单名称
	private String funcCode;  // 菜单url
	
	
	public String getNmDealertype() {
		return nmDealertype;
	}

	public void setNmDealertype(String nmDealertype) {
		this.nmDealertype = nmDealertype;
	}


	private String nmDealertype;       // 提醒类型
	private String targetUserId;  //用户id
	private String targetPositionId; // 职位id
	

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public String getFuncCode() {
		return funcCode;
	}

	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}

	public String getTargetPositionId() {
		return targetPositionId;
	}

	public void setTargetPositionId(String targetPositionId) {
		this.targetPositionId = targetPositionId;
	}


	private Date dnBusinessstatetime;  // 业务单状态时间
	private Date dnHandletime;		   // 处理时间
	private String dnHandlestate;      // 处理状态
	private String dnDealerid;         // 经销商id
	private String dnBusinessid;       // 业务单id
	private Date dnCreatetime;         // 创建时间
	private String dnBusinessstate;    // 业务单状态
	private String dnHandleuser;       // 处理人	
	private String nmId;               // 模版id
	private String dnTarid;            // 提醒目标id
	private String id;                 // id主键
	private String dnNoticecontent;    // 提醒内容
	private String dnBusinessno;       // 业务单号

	public void setDnBusinessstatetime(Date dnBusinessstatetime){
		this.dnBusinessstatetime=dnBusinessstatetime;
	}

	public Date getDnBusinessstatetime(){
		return this.dnBusinessstatetime;
	}

	public void setDnHandletime(Date dnHandletime){
		this.dnHandletime=dnHandletime;
	}

	public Date getDnHandletime(){
		return this.dnHandletime;
	}

	public void setDnHandlestate(String dnHandlestate){
		this.dnHandlestate=dnHandlestate;
	}

	public String getDnHandlestate(){
		return this.dnHandlestate;
	}

	public void setDnDealerid(String dnDealerid){
		this.dnDealerid=dnDealerid;
	}

	public String getDnDealerid(){
		return this.dnDealerid;
	}

	public void setDnBusinessid(String dnBusinessid){
		this.dnBusinessid=dnBusinessid;
	}

	public String getDnBusinessid(){
		return this.dnBusinessid;
	}

	public void setDnCreatetime(Date dnCreatetime){
		this.dnCreatetime=dnCreatetime;
	}

	public Date getDnCreatetime(){
		return this.dnCreatetime;
	}

	public void setDnBusinessstate(String dnBusinessstate){
		this.dnBusinessstate=dnBusinessstate;
	}

	public String getDnBusinessstate(){
		return this.dnBusinessstate;
	}

	public void setDnHandleuser(String dnHandleuser){
		this.dnHandleuser=dnHandleuser;
	}

	public String getDnHandleuser(){
		return this.dnHandleuser;
	}

	public void setNmId(String nmId){
		this.nmId=nmId;
	}

	public String getNmId(){
		return this.nmId;
	}

	public void setDnTarid(String dnTarid){
		this.dnTarid=dnTarid;
	}

	public String getDnTarid(){
		return this.dnTarid;
	}

	public void setId(String id){
		this.id=id;
	}

	public String getId(){
		return this.id;
	}

	public void setDnNoticecontent(String dnNoticecontent){
		this.dnNoticecontent=dnNoticecontent;
	}

	public String getDnNoticecontent(){
		return this.dnNoticecontent;
	}

	public void setDnBusinessno(String dnBusinessno){
		this.dnBusinessno=dnBusinessno;
	}

	public String getDnBusinessno(){
		return this.dnBusinessno;
	}

}
