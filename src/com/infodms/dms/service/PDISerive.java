package com.infodms.dms.service;

import java.util.Map;

/**
 * 完成验收转PDI接口
 * @author yuewei
 *
 */
public interface PDISerive {

	/**
	 * @param Vin
	 * @param map 请放置 dealerid 售后经销商ID 	deliverer//送修人姓名 
	 * delivererPhone //送修人电话 delivererMobile//送修人手机 
	 * remark // PDI备注必须填写  pdiprice //PDI费用不传 默认为30
	 * @param  String[]  fjids 附件ids
	 */
	public String checkAcceptChangePDI(String vin,Map<String,String> map);
}
