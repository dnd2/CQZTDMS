package com.infodms.dms.bean;
/**
 * 公司对象
 * @author ZHANGTIANHENG
 *
 */
/**
 * @author Zhang tianheng
 *
 */
public class CompanyBean
{
	//经销商基本信息
	private String companyId;                           //经销商ID	
	private String companyCode;                         //经销商代码	
	private String companyName;                         //经销商名称
	private String companyShortname;                 //经销商简称	
	private String companyType;                     //公司类型
	private String provinceId;                      //省份ID
	private String province;                        //省份	
	private String cityId;                          //城市ID
	private String city;                            //城市
	private String address;                        //地址	
	private String zipCode;                         //邮编
	private String fax;                             //传真号	
	private String phone;                         //联系电话
	private String status;                            //状态
	private String statusName;                            //状态
	private String oemCompanyId;
	private String oemCompanyShortname; 
	private String isSamePerson; //是否同一法人
	private String isBeforeAfter; //是否前店后厂
	private String handleByHandle; //是否手拉手
	private String relationCompany; //关联公司
	
	public String getIsSamePerson() {
		return isSamePerson;
	}
	public void setIsSamePerson(String isSamePerson) {
		this.isSamePerson = isSamePerson;
	}
	public String getIsBeforeAfter() {
		return isBeforeAfter;
	}
	public void setIsBeforeAfter(String isBeforeAfter) {
		this.isBeforeAfter = isBeforeAfter;
	}
	public String getHandleByHandle() {
		return handleByHandle;
	}
	public void setHandleByHandle(String handleByHandle) {
		this.handleByHandle = handleByHandle;
	}
	public String getRelationCompany() {
		return relationCompany;
	}
	public void setRelationCompany(String relationCompany) {
		this.relationCompany = relationCompany;
	}
	public String getOemCompanyId() {
		return oemCompanyId;
	}
	public void setOemCompanyId(String oemCompanyId) {
		this.oemCompanyId = oemCompanyId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyShortname() {
		return companyShortname;
	}
	public void setCompanyShortname(String companyShortname) {
		this.companyShortname = companyShortname;
	}
	public String getCompanyType() {
		return companyType;
	}
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getOemCompanyShortname() {
		return oemCompanyShortname;
	}
	public void setOemCompanyShortname(String oemCompanyShortname) {
		this.oemCompanyShortname = oemCompanyShortname;
	}
	
	
	
	
}
