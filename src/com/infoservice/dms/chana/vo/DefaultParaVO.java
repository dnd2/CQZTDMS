package com.infoservice.dms.chana.vo;


/**
 * 
 * @ClassName : DefaultParaVO
 * @Description : 
 * @author : luole
 * @CreateDate : 2013-7-10
 */
@SuppressWarnings("serial")
public class DefaultParaVO extends BaseVO {
	/**
	 * 1274 OEM配件只能入OEM库 
	 * 1275 OEM库限制配件调拨 
	 * 1276 OEM库存流水上报
	 */
	private String itemCode;
	/**
	 * 12791001 是 
	 * 12791002 否
	 */
	private String defaultValue;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
