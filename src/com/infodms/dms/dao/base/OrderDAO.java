package com.infodms.dms.dao.base;

import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

public class OrderDAO extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(OrderDAO.class);
	private OrderDAO() {
		
	}
	
	private static class OrderDAOSingleton {
		private static OrderDAO dao = new OrderDAO() ;
	}
	
	public static OrderDAO getInstance() {
		return OrderDAOSingleton.dao ;
	}
	
	public int orderModify(String orderId, Map<String, String> map) throws ParseException {
		String orderStatus = map.get("orderStatus") ;
		String updateBy = map.get("updateBy") ;
		String updateDate = map.get("updateDate") ;
		

		TtVsOrderPO oldOrder = new TtVsOrderPO() ;
		oldOrder.setOrderId(Long.parseLong(orderId)) ;
		TtVsOrderPO newOrder = new TtVsOrderPO() ;

		if(!CommonUtils.isNullString(orderStatus)) {
			newOrder.setOrderStatus(Integer.parseInt(orderStatus)) ;
		}

		if(!CommonUtils.isNullString(updateBy)) {
			newOrder.setUpdateBy(Long.parseLong(updateBy)) ;
		}

		if(!CommonUtils.isNullString(updateDate)) {
			newOrder.setUpdateDate(new Date((Long.parseLong(updateDate)))) ;
		}

		return super.update(oldOrder, newOrder) ;
	}
	
	public Map<String, Object> orderByIdQeury(String orderId) {
		List<Object> params = new ArrayList<Object>() ;
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select tvo.* from tt_vs_order tvo where tvo.order_id = ?\n");
		params.add(orderId) ;

		return super.pageQueryMap(sql.toString(), params, super.getFunName()) ;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
