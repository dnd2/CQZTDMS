package com.infodms.dms.bean;

import java.util.Date;

public class CustomerIntentBean {
	
	//客户基本信息
	private String custId;                           //客户ID	
	private String custType;                         //客户类型	
	private String custName;                         //客户姓名
	private String contTel;                          //联系方式	对应修改后字段MAIN_CONT_MODE
	private String dlrId;                            //所属经销商ID	
	private String province;                         //所在省份-------------
	private String city;                             //所在城市-------------
	private String addr;                             //客户地址-------------
	private String zipCode;                          //邮编-----------------
	private String evaluater;                        //评估师
	private String salesCnsl;                        //销售顾问
	
	//个人客户信息
	private String indiCustName;                    //个人客户名称	
	private String idCardNum;                       //身份证号码	
	private String birthday;                        //客户生日
	private String custGender;                      //客户性别
	private String marriageStat;                    //婚姻状况
	private String eduBkgr;                         //学历
	private String familyMonthlyIncome;             //家庭月收入
	private String hobby;                           //个人爱好
	private String industry;                        //个人所在行业
	
	//公司客户信息
	private String compcustName;                    //公司客户名称
	private String orgCode;                         //组织机构代码
	private String comType;                         //公司性质
	private String compindustry;                    //公司所在行业

	//客户收购意向信息
	private String custIntentId;                    //客户意向ID
	private String intnType;                        //意向类型	
	private String intnStat;                        //意向状态
	private String intnLevel;                       //意向级别
	private String loseCause;                       //战败原因
	private String interestedVhcl;                  //感兴趣的车辆
	private String loseType;                        //战败类型
	private String loseDate;                        //战败类型
	private String intentEvaluater;                 //意向评估师
	private String intentSalesCnsl;                 //意向销售顾问
	private String intentremark;                    //意向备注
	private String plannedDate;						 //计划跟进时间
	private String createDate;						 //意向创建时间
	private String remark;						 	 //备注

	//意向下车辆评估表
	private String vhclEvaId;                       //车辆评估表ID
	private String chgTimes;                        //变更次数
	private String aprsDate;                        //评估日期
	private String vhclId;                          //车辆ID
	private String aprsPeriodVald;                  //评估有效期
	private String frcsMileage;                     //推测里程
	private String aprsPrice;                       //评估价格
	private String evaRemark;                       //评估表备注
	private String vhclLic;                         //车牌号
	private String fileNum;							//文档编号
	
	//车辆具体信息表
	private String vin;                             //车辆VIN
	private String modelId;                         //车辆车型ID
	private String brandName;                       //厂家品牌名称
	private String seriesName;                      //车系名称
	private String modelName;                       //车型名称
	private String color;         		            //车辆颜色
	
	private String vhclType;     					//车辆类型
	private String gearBox;      					//变速箱
	private String noticeCode;   					//公告代码
	private String modelYear;    					//车型年
	private String dspm;         					//排量
	private String engine;         					//发动机
	private String fuel;         					//燃料
	
	private String mileage;                         //里程
	private String withPlate;                       //是否带牌
	private String vhclSource;                      //推测里程	
	
	private String nextFollowDate;					//意向下次跟进时间
	private String nextFollowOverdue;				//意向下次跟进过期天数
	private String followUpDate; //跟进日期
	private String followStat; //跟进状态
	public String getFollowStat() {
		return followStat;
	}
	public void setFollowStat(String followStat) {
		this.followStat = followStat;
	}
	public String getFollowUpDate() {
		return followUpDate;
	}
	public void setFollowUpDate(String followUpDate) {
		this.followUpDate = followUpDate;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getContTel() {
		return contTel;
	}
	public void setContTel(String contTel) {
		this.contTel = contTel;
	}
	public String getDlrId() {
		return dlrId;
	}
	public void setDlrId(String dlrId) {
		this.dlrId = dlrId;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getEvaluater() {
		return evaluater;
	}
	public void setEvaluater(String evaluater) {
		this.evaluater = evaluater;
	}
	public String getSalesCnsl() {
		return salesCnsl;
	}
	public void setSalesCnsl(String salesCnsl) {
		this.salesCnsl = salesCnsl;
	}
	public String getIndiCustName() {
		return indiCustName;
	}
	public void setIndiCustName(String indiCustName) {
		this.indiCustName = indiCustName;
	}
	public String getIdCardNum() {
		return idCardNum;
	}
	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCustGender() {
		return custGender;
	}
	public void setCustGender(String custGender) {
		this.custGender = custGender;
	}
	public String getMarriageStat() {
		return marriageStat;
	}
	public void setMarriageStat(String marriageStat) {
		this.marriageStat = marriageStat;
	}
	public String getEduBkgr() {
		return eduBkgr;
	}
	public void setEduBkgr(String eduBkgr) {
		this.eduBkgr = eduBkgr;
	}
	public String getFamilyMonthlyIncome() {
		return familyMonthlyIncome;
	}
	public void setFamilyMonthlyIncome(String familyMonthlyIncome) {
		this.familyMonthlyIncome = familyMonthlyIncome;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getCompcustName() {
		return compcustName;
	}
	public void setCompcustName(String compcustName) {
		this.compcustName = compcustName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getComType() {
		return comType;
	}
	public void setComType(String comType) {
		this.comType = comType;
	}
	public String getCompindustry() {
		return compindustry;
	}
	public void setCompindustry(String compindustry) {
		this.compindustry = compindustry;
	}
	public String getCustIntentId() {
		return custIntentId;
	}
	public void setCustIntentId(String custIntentId) {
		this.custIntentId = custIntentId;
	}
	public String getIntnType() {
		return intnType;
	}
	public void setIntnType(String intnType) {
		this.intnType = intnType;
	}
	public String getIntnStat() {
		return intnStat;
	}
	public void setIntnStat(String intnStat) {
		this.intnStat = intnStat;
	}
	public String getIntnLevel() {
		return intnLevel;
	}
	public void setIntnLevel(String intnLevel) {
		this.intnLevel = intnLevel;
	}
	public String getLoseCause() {
		return loseCause;
	}
	public void setLoseCause(String loseCause) {
		this.loseCause = loseCause;
	}
	public String getInterestedVhcl() {
		return interestedVhcl;
	}
	public void setInterestedVhcl(String interestedVhcl) {
		this.interestedVhcl = interestedVhcl;
	}
	public String getLoseType() {
		return loseType;
	}
	public void setLoseType(String loseType) {
		this.loseType = loseType;
	}
	public String getIntentEvaluater() {
		return intentEvaluater;
	}
	public void setIntentEvaluater(String intentEvaluater) {
		this.intentEvaluater = intentEvaluater;
	}
	public String getIntentSalesCnsl() {
		return intentSalesCnsl;
	}
	public void setIntentSalesCnsl(String intentSalesCnsl) {
		this.intentSalesCnsl = intentSalesCnsl;
	}
	public String getIntentremark() {
		return intentremark;
	}
	public void setIntentremark(String intentremark) {
		this.intentremark = intentremark;
	}
	public String getVhclEvaId() {
		return vhclEvaId;
	}
	public void setVhclEvaId(String vhclEvaId) {
		this.vhclEvaId = vhclEvaId;
	}
	public String getChgTimes() {
		return chgTimes;
	}
	public void setChgTimes(String chgTimes) {
		this.chgTimes = chgTimes;
	}
	public String getAprsDate() {
		return aprsDate;
	}
	public void setAprsDate(String aprsDate) {
		this.aprsDate = aprsDate;
	}
	public String getVhclId() {
		return vhclId;
	}
	public void setVhclId(String vhclId) {
		this.vhclId = vhclId;
	}
	public String getAprsPeriodVald() {
		return aprsPeriodVald;
	}
	public void setAprsPeriodVald(String aprsPeriodVald) {
		this.aprsPeriodVald = aprsPeriodVald;
	}
	public String getFrcsMileage() {
		return frcsMileage;
	}
	public void setFrcsMileage(String frcsMileage) {
		this.frcsMileage = frcsMileage;
	}
	public String getAprsPrice() {
		return aprsPrice;
	}
	public void setAprsPrice(String aprsPrice) {
		this.aprsPrice = aprsPrice;
	}
	public String getEvaRemark() {
		return evaRemark;
	}
	public void setEvaRemark(String evaRemark) {
		this.evaRemark = evaRemark;
	}
	public String getVhclLic() {
		return vhclLic;
	}
	public void setVhclLic(String vhclLic) {
		this.vhclLic = vhclLic;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getNextFollowDate() {
		return nextFollowDate;
	}
	public void setNextFollowDate(String nextFollowDate) {
		this.nextFollowDate = nextFollowDate;
	}
	public String getNextFollowOverdue() {
		return nextFollowOverdue;
	}
	public void setNextFollowOverdue(String nextFollowOverdue) {
		this.nextFollowOverdue = nextFollowOverdue;
	}
	public String getVhclType() {
		return vhclType;
	}
	public void setVhclType(String vhclType) {
		this.vhclType = vhclType;
	}
	public String getGearBox() {
		return gearBox;
	}
	public void setGearBox(String gearBox) {
		this.gearBox = gearBox;
	}
	public String getNoticeCode() {
		return noticeCode;
	}
	public void setNoticeCode(String noticeCode) {
		this.noticeCode = noticeCode;
	}
	public String getModelYear() {
		return modelYear;
	}
	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}
	public String getDspm() {
		return dspm;
	}
	public void setDspm(String dspm) {
		this.dspm = dspm;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	public String getFuel() {
		return fuel;
	}
	public void setFuel(String fuel) {
		this.fuel = fuel;
	}
	public String getLoseDate() {
		return loseDate;
	}
	public void setLoseDate(String loseDate) {
		this.loseDate = loseDate;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getWithPlate() {
		return withPlate;
	}
	public void setWithPlate(String withPlate) {
		this.withPlate = withPlate;
	}
	public String getVhclSource() {
		return vhclSource;
	}
	public void setVhclSource(String vhclSource) {
		this.vhclSource = vhclSource;
	}
	public String getPlannedDate() {
		return plannedDate;
	}
	public void setPlannedDate(String plannedDate) {
		this.plannedDate = plannedDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getFileNum() {
		return fileNum;
	}
	public void setFileNum(String fileNum) {
		this.fileNum = fileNum;
	}
	
	
}
