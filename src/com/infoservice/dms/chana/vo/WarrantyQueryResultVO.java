package com.infoservice.dms.chana.vo;
import java.util.HashMap;

/**
 * @Title: WarrantyQueryResultVO.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-8-12
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
@SuppressWarnings("serial")
public class WarrantyQueryResultVO extends BaseVO {
	private String vin; //下端：VIN  VARCHAR(17)  上端：  
	private Double inMileage;//进厂行驶里程
	private Integer freeTimes;//保养次数
	private String gameName;//三包策略名称
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Integer getFreeTimes() {
		return freeTimes;
	}
	public void setFreeTimes(Integer freeTimes) {
		this.freeTimes = freeTimes;
	}

	private HashMap<Integer, BaseVO> partVoList; //下端：配件列表    上端：
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	
	public Double getInMileage() {
		return inMileage;
	}
	public void setInMileage(Double inMileage) {
		this.inMileage = inMileage;
	}
	public HashMap<Integer, BaseVO> getPartVoList() {
		return partVoList;
	}
	public void setPartVoList(HashMap<Integer, BaseVO> partVoList) {
		this.partVoList = partVoList;
	}
}
