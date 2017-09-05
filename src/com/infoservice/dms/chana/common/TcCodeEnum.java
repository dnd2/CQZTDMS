package com.infoservice.dms.chana.common;
/**
 * 
* @ClassName: TcCodeEnum 
* @Description: TODO(客户投诉信息投诉大类小类关系) 
* @author liuqiang 
* @date Sep 5, 2010 2:15:41 PM 
*
 */
public enum TcCodeEnum {
	
	//投诉状态
	COMPLIANT_Status(10641003, 10641004);
	private Integer uTcCode;
    private Integer dTcCode;
    
    /**
     * 
    * <p>Title: </p> 
    * <p>Description: </p> 
    * @param uTcCode 上端系统tc_code
    * @param dTcCode 下端系统tc_code
     */
	private TcCodeEnum(Integer uTcCode, Integer dTcCode) {
        this.uTcCode = uTcCode;
        this.dTcCode = dTcCode;
        
    }
	
	public Integer getUTcCode() {
		return uTcCode;
	}

	public Integer getDTcCode() {
		return dTcCode;
	}

	/**
	 * 
	* @Title: getDTcCode 
	* @Description: TODO(根据上端系统tc_code找下端系统tc_code) 
	* @param @param uTcCode 上端系统tc_code
	* @param @return    设定文件 
	* @return Integer    返回类型 
	* @throws
	 */
	public static Integer getDTcCode(Integer uTcCode) {
        for (TcCodeEnum code : values()) {
            if (code.getUTcCode().equals(uTcCode)) {
                return code.dTcCode;
            }
        }
        return null;
    }
	
	/**
	 * 
	* @Title: getUTcCode 
	* @Description: TODO(根据下端系统tc_code找上端系统tc_code) 
	* @param @param dTcCode 下端系统tc_code
	* @param @return    设定文件 
	* @return Integer    返回类型 
	* @throws
	 */
	public static Integer getUTcCode(Integer dTcCode) {
		for (TcCodeEnum code : values()) {
            if (code.getDTcCode().equals(dTcCode)) {
                return code.dTcCode;
            }
        }
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(TcCodeEnum.getUTcCode(10641004));
	}
	
}
