/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-05-21 14:35:17
* CreateBy   : chenyu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmNoticemodelPO extends PO{

	private String nmBusinessidfield;  // 业务单id字段
	private String nmTartype;          // 提醒目标类型
	private String nmMenuid;           // 菜单id
	private String nmBusinessnumfield; // 业务号字段
	private String nmModelstate;       // 模版启用状态
	private String nmNoticestatevalue; // 提醒状态阀值
	private Long nmUpdateuser;         // 更新人
	private String nmNoticetimefield;  // 提醒时间字段
	private Long nmNoticemintime;      // 开始提醒时间差 单位:分钟
	private Long nmNoticemaxtime;      // 取消提醒时间差(暂未使用)
	private String nmModelname;        // 模版名称
	private String nmNoticestatefield; // 提醒状态字段
	private Date nmUpdatetime;         // 更新事件
	private String nmDealerfield;      // 经销商id字段
	private String id;                 // 主键id
	private String nmTarfield;         // 提醒目标字段
	private Date nmCreatetime;         // 创建时间
	private String nmModeldesc;        // 模版描述
	private Long nmCreateuser;         // 创建人
	private String nmNoticetype;	   // 提醒类型 0:通知类;1:处理类
	private String nmOtherstatefield1; // 其他条件字段1
	private String nmOtherstatevalue1; // 其他条件值1
	private String nmOtherstatefield2; // 其他条件字段2
	private String nmOtherstatevalue2; // 其他条件值2
	private String nmNoticestaterelation; //提醒状态条件运算符
	private String nmOtherstaterelation1; // 其他条件字段1运算符
	private String nmOtherstaterelation2; // 其他条件字段2运算符
	private String nmTarvalue;         // 提醒对象值,当提醒对象类型选择dealer/owner以外的值时有效

	public void setNmBusinessidfield(String nmBusinessidfield){
		this.nmBusinessidfield=nmBusinessidfield;
	}

	public String getNmBusinessidfield(){
		return this.nmBusinessidfield;
	}

	public void setNmTartype(String nmTartype){
		this.nmTartype=nmTartype;
	}

	public String getNmTartype(){
		return this.nmTartype;
	}

	public void setNmMenuid(String nmMenuid){
		this.nmMenuid=nmMenuid;
	}

	public String getNmMenuid(){
		return this.nmMenuid;
	}

	public void setNmBusinessnumfield(String nmBusinessnumfield){
		this.nmBusinessnumfield=nmBusinessnumfield;
	}

	public String getNmBusinessnumfield(){
		return this.nmBusinessnumfield;
	}

	public void setNmModelstate(String nmModelstate){
		this.nmModelstate=nmModelstate;
	}

	public String getNmModelstate(){
		return this.nmModelstate;
	}

	public void setNmNoticestatevalue(String nmNoticestatevalue){
		this.nmNoticestatevalue=nmNoticestatevalue;
	}

	public String getNmNoticestatevalue(){
		return this.nmNoticestatevalue;
	}

	public void setNmNoticetimefield(String nmNoticetimefield){
		this.nmNoticetimefield=nmNoticetimefield;
	}

	public String getNmNoticetimefield(){
		return this.nmNoticetimefield;
	}

	public void setNmNoticemintime(Long nmNoticemintime){
		this.nmNoticemintime=nmNoticemintime;
	}

	public Long getNmNoticemintime(){
		return this.nmNoticemintime;
	}

	public void setNmNoticemaxtime(Long nmNoticemaxtime){
		this.nmNoticemaxtime=nmNoticemaxtime;
	}

	public Long getNmNoticemaxtime(){
		return this.nmNoticemaxtime;
	}

	public void setNmModelname(String nmModelname){
		this.nmModelname=nmModelname;
	}

	public String getNmModelname(){
		return this.nmModelname;
	}

	public void setNmNoticestatefield(String nmNoticestatefield){
		this.nmNoticestatefield=nmNoticestatefield;
	}

	public String getNmNoticestatefield(){
		return this.nmNoticestatefield;
	}

	public void setNmUpdatetime(Date nmUpdatetime){
		this.nmUpdatetime=nmUpdatetime;
	}

	public Date getNmUpdatetime(){
		return this.nmUpdatetime;
	}

	public void setNmDealerfield(String nmDealerfield){
		this.nmDealerfield=nmDealerfield;
	}

	public String getNmDealerfield(){
		return this.nmDealerfield;
	}

	public void setId(String id){
		this.id=id;
	}

	public String getId(){
		return this.id;
	}

	public void setNmTarfield(String nmTarfield){
		this.nmTarfield=nmTarfield;
	}

	public String getNmTarfield(){
		return this.nmTarfield;
	}

	public void setNmCreatetime(Date nmCreatetime){
		this.nmCreatetime=nmCreatetime;
	}

	public Date getNmCreatetime(){
		return this.nmCreatetime;
	}

	public void setNmModeldesc(String nmModeldesc){
		this.nmModeldesc=nmModeldesc;
	}

	public String getNmModeldesc(){
		return this.nmModeldesc;
	}

	public Long getNmUpdateuser() {
		return nmUpdateuser;
	}

	public void setNmUpdateuser(Long nmUpdateuser) {
		this.nmUpdateuser = nmUpdateuser;
	}

	public Long getNmCreateuser() {
		return nmCreateuser;
	}

	public void setNmCreateuser(Long nmCreateuser) {
		this.nmCreateuser = nmCreateuser;
	}

	public String getNmNoticetype() {
		return nmNoticetype;
	}

	public void setNmNoticetype(String nmNoticetype) {
		this.nmNoticetype = nmNoticetype;
	}

	public String getNmOtherstatefield1() {
		return nmOtherstatefield1;
	}

	public void setNmOtherstatefield1(String nmOtherstatefield1) {
		this.nmOtherstatefield1 = nmOtherstatefield1;
	}

	public String getNmOtherstatevalue1() {
		return nmOtherstatevalue1;
	}

	public void setNmOtherstatevalue1(String nmOtherstatevalue1) {
		this.nmOtherstatevalue1 = nmOtherstatevalue1;
	}

	public String getNmOtherstatefield2() {
		return nmOtherstatefield2;
	}

	public void setNmOtherstatefield2(String nmOtherstatefield2) {
		this.nmOtherstatefield2 = nmOtherstatefield2;
	}

	public String getNmOtherstatevalue2() {
		return nmOtherstatevalue2;
	}

	public void setNmOtherstatevalue2(String nmOtherstatevalue2) {
		this.nmOtherstatevalue2 = nmOtherstatevalue2;
	}

	public String getNmNoticestaterelation() {
		return nmNoticestaterelation;
	}

	public void setNmNoticestaterelation(String nmNoticestaterelation) {
		this.nmNoticestaterelation = nmNoticestaterelation;
	}

	public String getNmOtherstaterelation1() {
		return nmOtherstaterelation1;
	}

	public void setNmOtherstaterelation1(String nmOtherstaterelation1) {
		this.nmOtherstaterelation1 = nmOtherstaterelation1;
	}

	public String getNmOtherstaterelation2() {
		return nmOtherstaterelation2;
	}

	public void setNmOtherstaterelation2(String nmOtherstaterelation2) {
		this.nmOtherstaterelation2 = nmOtherstaterelation2;
	}

	public String getNmTarvalue() {
		return nmTarvalue;
	}

	public void setNmTarvalue(String nmTarvalue) {
		this.nmTarvalue = nmTarvalue;
	}
	

}