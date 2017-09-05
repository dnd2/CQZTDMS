package com.infodms.dms.actions.sales.balancecentermanage.dealerordermanage;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage.DealerOrderDispatchQueryDAO;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TtVsScMatchPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : DealerOrderDispatchQuery.java
 * @Package: com.infodms.dms.actions.sales.balancecentermanage.dealerordermanage
 * @Description: 结算中心：经销商订单调拨查询
 * @date   : 2010-6-26 
 * @version: V1.0   
 */
public class DealerOrderDispatchQuery  extends BaseDao {

	public Logger logger = Logger.getLogger(DealerOrderDispatchQuery.class);
	private ActionContext act = ActionContext.getContext();
	private static final DealerOrderDispatchQuery dao = new DealerOrderDispatchQuery();

	public static final DealerOrderDispatchQuery getInstance() {
		return dao;
	}

	RequestWrapper request = act.getRequest();
	private final OrderQueryDao orderQueryDao = OrderQueryDao.getInstance();
	private final String dealerOrderDispatchQueryInit = "/jsp/sales/balancecentermanage/dealerordermanage/dealerOrderDispatchQueryInit.jsp";
	private final String vehicleListURL = "/jsp/sales/balancecentermanage/dealerordermanage/dispatchVehicleList.jsp";
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	/** 
	* @Title	  : dealerOrderDispatchQueryInit 
	* @Description: 结算中心：经销商订单调拨查询页面初始化
	* @throws 
	* @LastUpdate :2010-6-26
	*/
	public void dealerOrderDispatchQueryInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			Long orgId = logonUser.getOrgId();
			List<String> years = orderQueryDao.getYearList();
			List<String> weeks = orderQueryDao.getWeekList();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			Calendar c = Calendar.getInstance();
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(),
					oemCompanyId);

			act.setOutData("orgId", orgId);
			act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear()
					: "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek()
					: "");
			act.setOutData("areaList", areaList);
			act.setForword(dealerOrderDispatchQueryInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算中心：经销商订单调拨查询页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : dealerOrderDispatchQuery 
	* @Description: 结算中心：经销商订单调拨查询
	* @throws 
	* @LastUpdate :2010-6-26
	*/
	public void dealerOrderDispatchQuery(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String dealerId = logonUser.getDealerId();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);//订单周度
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);		  //业务范围
			map.put("groupCode", groupCode);  //物料组
			map.put("orderNo", orderNo);	  //订单号
			map.put("dealerCode", dealerCode);   
			map.put("dealerId", dealerId);    
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = DealerOrderDispatchQueryDAO.getDispatchQueryList(map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		}  catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算中心：经销商订单调拨查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dealerOrderDispatchListQuery(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String detail_id = CommonUtils.checkNull(request.getParamValue("detail_id"));
			act.setOutData("detail_id", detail_id);
			act.setForword(vehicleListURL);
		}  catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算中心：经销商订单调拨查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dispatchListQuery(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String detail_id = CommonUtils.checkNull(request.getParamValue("detail_id"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps =  DealerOrderDispatchQueryDAO.getDispatchList(detail_id, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		}  catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算中心：经销商订单调拨查询");
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
