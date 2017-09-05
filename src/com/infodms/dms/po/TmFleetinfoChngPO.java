package com.infodms.dms.po;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmFleetinfoChngPO extends PO{
	private Long recordId;
	private Long fleetId;
	private Long brandId;
	private Long orgId;
	private String orgCode;
	private String fleetName;
	private String phone;
	private Integer vocation;
	private Integer profession;
	private String mainLinkman;
	private String mainBusiness;
	private String personalSize;
	private String fundSize;
	private Integer buyCarUse;
	private String email;
	private String address;
	private String post;
	private Integer status;
	private String examineResult;
	private Date reportUpdate;
	private Date createDate;
	private Long createBy;
	private Date updateDate;
	private Long updateBy;
	private Long fleetProvince;
	private Long fleetCity;
	private Long fleetCounty;
	private String fleetWwwAddress;
	private String fleetFix;
	private String linkmanSex;
	private Integer isDown;

	 public Integer getIsDown() {
		return isDown;
	}
	public void setIsDown(Integer isDown) {
		this.isDown = isDown;
	}
	public void setRecordId( Long recordId){
	     this.recordId=recordId;
	}
	 public Long getRecordId(){
	      return this.recordId;
	}
	 public void setFleetId( Long fleetId){
	     this.fleetId=fleetId;
	}
	 public Long getFleetId(){
	      return this.fleetId;
	}
	 public void setOrgCode( String orgCode){
	     this.orgCode=orgCode;
	}
	 public String getOrgCode(){
	      return this.orgCode;
	}
	 public void setFleetName( String fleetName){
	     this.fleetName=fleetName;
	}
	 public String getFleetName(){
	      return this.fleetName;
	}
	 public void setPhone( String phone){
	     this.phone=phone;
	}
	 public String getPhone(){
	      return this.phone;
	}
	 public void setVocation( Integer vocation){
	     this.vocation=vocation;
	}
	 public Integer getVocation(){
	      return this.vocation;
	}
	 public void setProfession( Integer profession){
	     this.profession=profession;
	}
	 public Integer getProfession(){
	      return this.profession;
	}
	 public void setMainLinkman( String mainLinkman){
	     this.mainLinkman=mainLinkman;
	}
	 public String getMainLinkman(){
	      return this.mainLinkman;
	}
	 public void setBuyCarUse( Integer buyCarUse){
	     this.buyCarUse=buyCarUse;
	}
	 public Integer getBuyCarUse(){
	      return this.buyCarUse;
	}
	 public void setEmail( String email){
	     this.email=email;
	}
	 public String getEmail(){
	      return this.email;
	}
	 public void setAddress( String address){
	     this.address=address;
	}
	 public String getAddress(){
	      return this.address;
	}
	 public void setPost( String post){
	     this.post=post;
	}
	 public String getPost(){
	      return this.post;
	}
	 public void setStatus( Integer status){
	     this.status=status;
	}
	 public Integer getStatus(){
	      return this.status;
	}
	 public void setExamineResult( String examineResult){
	     this.examineResult=examineResult;
	}
	 public String getExamineResult(){
	      return this.examineResult;
	}
	 public void setReportUpdate( Date reportUpdate){
	     this.reportUpdate=reportUpdate;
	}
	 public Date getReportUpdate(){
	      return this.reportUpdate;
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
	 public void setFleetProvince( Long fleetProvince){
	     this.fleetProvince=fleetProvince;
	}
	 public Long getFleetProvince(){
	      return this.fleetProvince;
	}
	 public void setFleetCity( Long fleetCity){
	     this.fleetCity=fleetCity;
	}
	 public Long getFleetCity(){
	      return this.fleetCity;
	}
	 public void setFleetCounty( Long fleetCounty){
	     this.fleetCounty=fleetCounty;
	}
	 public Long getFleetCounty(){
	      return this.fleetCounty;
	}
	 public void setFleetWwwAddress( String fleetWwwAddress){
	     this.fleetWwwAddress=fleetWwwAddress;
	}
	 public String getFleetWwwAddress(){
	      return this.fleetWwwAddress;
	}
	 public void setFleetFix( String fleetFix){
	     this.fleetFix=fleetFix;
	}
	 public String getFleetFix(){
	      return this.fleetFix;
	}
	 public void setLinkmanSex( String linkmanSex){
	     this.linkmanSex=linkmanSex;
	}
	 public String getLinkmanSex(){
	      return this.linkmanSex;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getMainBusiness() {
		return mainBusiness;
	}
	public void setMainBusiness(String mainBusiness) {
		this.mainBusiness = mainBusiness;
	}
	public String getPersonalSize() {
		return personalSize;
	}
	public void setPersonalSize(String personalSize) {
		this.personalSize = personalSize;
	}
	public String getFundSize() {
		return fundSize;
	}
	public void setFundSize(String fundSize) {
		this.fundSize = fundSize;
	}
}