package com.infodms.dms.base;

import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.base.OrderDAO;
import com.infodms.dms.util.CommonUtils;

public class OrderBase {
	public Logger logger = Logger.getLogger(OrderBase.class);
	OrderDAO dao = OrderDAO.getInstance() ;
	
	public String getOldOrderId(String orderId) {
		String oldOrderId = null ;
		
		Map<String, Object> orderMap = dao.orderByIdQeury(orderId) ;

		if(!CommonUtils.isNullMap(orderMap)) {
			if(orderMap.get("OLD_ORDER_ID") != null) {
				oldOrderId = orderMap.get("OLD_ORDER_ID").toString() ;
			}
		}

		return oldOrderId ;
	}
}
