package com.infodms.yxdms.entity.activity;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrActivityTempletPO extends PO{

	private Double comAmount;
	private String applyRemark;
	private String troubleReason;
	private String subjectNo;
	private String subjectName;
	private Long subjectId;
	private String createBy;
	private String templetName;
	private Integer status;
	private Double partAmount;
	private Integer isDel;
	private String repairMethod;
	private String caseRemark;
	private Double allAmount;
	private Double labourAmount;
	private String appRemark;
	private Long id;
	private String troubleDesc;
	private Double accAmount;
	private String templetNo;
	private Date createDate;
	private Integer isReturn;
	private Integer isTips;

	public void setComAmount(Double comAmount){
		this.comAmount=comAmount;
	}

	public Double getComAmount(){
		return this.comAmount;
	}

	public void setApplyRemark(String applyRemark){
		this.applyRemark=applyRemark;
	}

	public String getApplyRemark(){
		return this.applyRemark;
	}

	public void setTroubleReason(String troubleReason){
		this.troubleReason=troubleReason;
	}

	public String getTroubleReason(){
		return this.troubleReason;
	}

	public void setSubjectId(Long subjectId){
		this.subjectId=subjectId;
	}

	public Long getSubjectId(){
		return this.subjectId;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setTempletName(String templetName){
		this.templetName=templetName;
	}

	public String getTempletName(){
		return this.templetName;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setPartAmount(Double partAmount){
		this.partAmount=partAmount;
	}

	public Double getPartAmount(){
		return this.partAmount;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setRepairMethod(String repairMethod){
		this.repairMethod=repairMethod;
	}

	public String getRepairMethod(){
		return this.repairMethod;
	}

	public void setCaseRemark(String caseRemark){
		this.caseRemark=caseRemark;
	}

	public String getCaseRemark(){
		return this.caseRemark;
	}

	public void setAllAmount(Double allAmount){
		this.allAmount=allAmount;
	}

	public Double getAllAmount(){
		return this.allAmount;
	}

	public void setLabourAmount(Double labourAmount){
		this.labourAmount=labourAmount;
	}

	public Double getLabourAmount(){
		return this.labourAmount;
	}

	public void setAppRemark(String appRemark){
		this.appRemark=appRemark;
	}

	public String getAppRemark(){
		return this.appRemark;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setTroubleDesc(String troubleDesc){
		this.troubleDesc=troubleDesc;
	}

	public String getTroubleDesc(){
		return this.troubleDesc;
	}

	public void setAccAmount(Double accAmount){
		this.accAmount=accAmount;
	}

	public Double getAccAmount(){
		return this.accAmount;
	}

	public void setTempletNo(String templetNo){
		this.templetNo=templetNo;
	}

	public String getTempletNo(){
		return this.templetNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public String getSubjectNo() {
		return subjectNo;
	}

	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Integer isReturn) {
		this.isReturn = isReturn;
	}

	public Integer getIsTips() {
		return isTips;
	}

	public void setIsTips(Integer isTips) {
		this.isTips = isTips;
	}

}