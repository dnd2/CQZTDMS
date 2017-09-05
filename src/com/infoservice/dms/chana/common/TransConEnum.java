package com.infoservice.dms.chana.common;
/**
 * 
* @ClassName: TransConEnum 
* @Description: TODO(配件订单运输方式字符串和上端tc_code对应关系) 
* @author liuqiang 
* @date Sep 5, 2010 2:15:35 PM 
*
 */
public enum TransConEnum {

	
	//运输类型
	TRANS_TYPE_SELF("自提", 10461001),
	TRANS_TYPE_AIR("空运", 10461002),
	TRANS_TYPE_RAIL("铁路快件", 10461003),
	TRANS_TYPE_ASS("集装箱", 10461004),
	TRANS_TYPE_FB("富宝", 10461005),
	TRANS_TYPE_SLOW("慢件", 10461006),
	TRANS_TYPE_FAST("特快专递", 10461007),
	TRANS_TYPE_AUTO("汽车", 10461008),
	TRANS_TYPE_SHIP("水运", 10461009),
	TRANS_TYPE_ZTKY("中铁快运", 10461010);
	
	private String transType;
    private Integer uTransType;
    
   /**
    * 
   * <p>Title: </p> 
   * <p>Description: </p> 
   * @param transType 配件系统运输方式字符串
   * @param uTransType 对应上端系统tc_code
    */
	private TransConEnum(String transType, Integer uTransType) {
        this.transType = transType;
        this.uTransType = uTransType;
        
    }
	
	public String getTransType() {
		return transType;
	}


	public Integer getUTransType() {
		return uTransType;
	}


	/**
	 * 
	* @Title: getDTcCode 
	* @Description: TODO(根据tc_code找对应运输方式) 
	* @param @param uTcCode 上端系统tc_code
	* @param @return    设定文件 
	* @return Integer    返回类型 
	* @throws
	 */
	public static String getTransType(Integer uTransType) {
        for (TransConEnum code : values()) {
            if (code.getUTransType().equals(uTransType)) {
                return code.transType;
            }
        }
        return null;
    }
	
	/**
	 * 
	* @Title: getUTcCode 
	* @Description: TODO(根据运输方式转成上端系统tc_code) 
	* @param @param dTcCode 下端系统tc_code
	* @param @return    设定文件 
	* @return Integer    返回类型 
	* @throws
	 */
	public static Integer getUTransType(String transType) {
		for (TransConEnum code : values()) {
            if (code.getTransType().equals(transType)) {
                return code.uTransType;
            }
        }
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(TransConEnum.getUTransType("富宝"));
	}
	

}
