package com.infoservice.dms.chana.vo;


/**
 * 
 * @ClassName : DefaultParaTempVO
 * @Description : 中间VO
 * @author : luole
 * @CreateDate : 2013-7-10
 */
@SuppressWarnings("serial")
public class DefaultParaTempVO extends BaseVO {
	/**
	 * isOem OEM配件只能入OEM库 
	 * isAllocation OEM库限制配件调拨 
	 * isRechard OEM库存流水上报
	 */
	
	private String isOem;
	private String isAllocation;
	private String isRechard;
	/**
	 * 12791001 是 
	 * 12791002 否
	 */
	public String getIsOem() {
		return isOem;
	}
	public void setIsOem(String isOem) {
		this.isOem = isOem;
	}
	public String getIsAllocation() {
		return isAllocation;
	}
	public void setIsAllocation(String isAllocation) {
		this.isAllocation = isAllocation;
	}
	public String getIsRechard() {
		return isRechard;
	}
	public void setIsRechard(String isRechard) {
		this.isRechard = isRechard;
	}

}
