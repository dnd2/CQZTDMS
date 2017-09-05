package com.infodms.dms.actions.sales.balancecentermanage.stockordermanage;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

/**   
 * @Title  : StockOrderQuery.java
 * @Package: com.infodms.dms.actions.sales.balancecentermanage.stockordermanage
 * @Description: 采购订单查询
 * @date   : 2010-6-22 
 * @version: V1.0   
 */
public class StockOrderQuery extends BaseDao {
	public Logger logger = Logger.getLogger(StockOrderQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final StockOrderQuery dao = new StockOrderQuery();
	private final OrderQueryDao orderQueryDao = OrderQueryDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	public static final StockOrderQuery getInstance() {
		return dao;
	}
	private final String stockOrderQueryInitURL = "/jsp/sales/balancecentermanage/stockordermanage/stockOrderQueryInit.jsp";
	/** 
	* @Title	  : stockOrderQueryInit 
	* @Description: 采购订单查询页面初始化
	* @throws 
	* @LastUpdate :2010-6-22
	*/
	public void stockOrderQueryInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			Long orgId = logonUser.getOrgId();
			List<String> years = orderQueryDao.getYearList();
			List<String> weeks = orderQueryDao.getWeekList();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerBusiness(poseId.toString());
			Calendar c = Calendar.getInstance();
			//int year = c.get(Calendar.YEAR);
			//int week = c.get(Calendar.WEEK_OF_YEAR);
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(),oemCompanyId);
			act.setOutData("orgId", orgId);
			act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear",dateSet != null ? dateSet.getSetYear(): "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek()
					: "");
			act.setOutData("areaList", areaList);
			act.setForword(stockOrderQueryInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单查询页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
