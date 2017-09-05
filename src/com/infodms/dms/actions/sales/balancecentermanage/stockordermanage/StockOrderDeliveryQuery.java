package com.infodms.dms.actions.sales.balancecentermanage.stockordermanage;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.po.TmDateSetPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;

/**   
 * @Title  : StockOrderDeliveryQuery.java
 * @Package: com.infodms.dms.actions.sales.balancecentermanage.stockordermanage
 * @Description: 采购订单发运查询
 * @date   : 2010-6-23 
 * @version: V1.0   
 */

public class StockOrderDeliveryQuery  extends BaseDao{
	public Logger logger = Logger.getLogger(StockOrderDeliveryQuery.class);
	private ActionContext act = ActionContext.getContext();
	private static final StockOrderDeliveryQuery dao = new StockOrderDeliveryQuery ();
	public static final StockOrderDeliveryQuery getInstance() {
		return dao;
	}
	OrderDeliveryDao deliveryDao  = OrderDeliveryDao.getInstance();
	
	private final String stockOrderDeliveryQueryInit = "/jsp/sales/balancecentermanage/stockordermanage/stockOrderDeliveryQueryInit.jsp";
	/** 
	* @Title	  : stockOrderDeliveryQueryInit 
	* @Description: 采购订单发运查询页面初始化
	* @throws 
	* @LastUpdate :2010-6-23
	*/
	public void stockOrderDeliveryQueryInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OrderReportDao reportDao = OrderReportDao.getInstance();
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		TmDateSetPO dateSet = reportDao.getTmDateSetPO(new java.util.Date(), oemCompanyId);

//		Calendar c = Calendar.getInstance();
//		String year = Integer.toString(c.get(Calendar.YEAR));
//		String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
		List yearList = deliveryDao.getDateYearList();
		List weekList = deliveryDao.getDateWeekList();
		act.setOutData("yearList", yearList);
		act.setOutData("weekList", weekList);
		act.setOutData("week", dateSet != null ? dateSet.getSetWeek() : "");
		act.setOutData("year", dateSet != null ? dateSet.getSetYear() : "");
//		act.setOutData("year", year);
//		act.setOutData("week", week);
		act.setForword(stockOrderDeliveryQueryInit);
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
