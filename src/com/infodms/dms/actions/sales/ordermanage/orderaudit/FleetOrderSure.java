package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.VerFlagDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsReqCheckPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class FleetOrderSure {
	public Logger logger = Logger.getLogger(FleetOrderSure.class) ;
	
	private ActionContext act = ActionContext.getContext() ;
	RequestWrapper request = act.getRequest() ;
	OrderAuditDao dao  = OrderAuditDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	
	/************************************** 跳转页面常量申明 ***********************************************/
	private final String FLEET_ORDER_SURE_INIT = "/jsp/sales/ordermanage/orderaudit/fleetOrderSure.jsp" ;
	private final String FLEET_ORDER_SURE_DTL_INIT = "/jsp/sales/ordermanage/orderaudit/fleetOrderDetailSure.jsp";
	
	/************************************** 页面调用action ***********************************************/
	public void fleetOrderSureInit() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
		
		try {
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List yearList = dao.getDateYearList();
			List weekList = dao.getDateWeekList();
			List<Map<String, Object>> areaBusList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new java.util.Date(), oemCompanyId);
			
			Long orgId = logonUser.getOrgId() ;
			
			act.setOutData("orgId", orgId);
			act.setOutData("yearList", yearList);
			act.setOutData("weekList", weekList);
			act.setOutData("week", dateSet != null ? dateSet.getSetWeek() : "");
			act.setOutData("year", dateSet != null ? dateSet.getSetYear() : "");
		    act.setOutData("areaBusList", areaBusList);
		    
			act.setForword(FLEET_ORDER_SURE_INIT);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户代交车确认初始化") ;
			logger.error(logonUser, e1) ;
			act.setException(e1) ;
		}
	}
	
	/**
	 * 集团客户代交车确认查询
	 */
	public void fleetOrderSureQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String startYear =request.getParamValue("startYear");			//订单起始年
			String endYear =request.getParamValue("endYear");				//订单结束年
			String startWeek = request.getParamValue("startWeek");			//订单起始周
			String endWeek = request.getParamValue("endWeek");				//订单结束周
			String orderType = request.getParamValue("orderType");			//订单类型
			String orderNo =request.getParamValue("orderNo");			    //订单号码
			String areaId = request.getParamValue("areaId");				//业务范围
			String groupCode = request.getParamValue("groupCode");			//物料组CODE
			String dealerCode =request.getParamValue("dealerCode");			//经销商CODE
			String dlrCode =request.getParamValue("dlrCode");			//经销商CODE
			
			Long companyId = logonUser.getCompanyId();
			Long orgId = null ;
			
			String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId() ;
			}
			
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			
			PageResult<Map<String, Object>> ps = dao.getFleetOrderSureList(dlrCode, orgId, startYear, endYear, startWeek, endWeek, areaId, groupCode, dealerCode, orderType, orderNo, companyId, areaIds, curPage, Constant.PAGE_SIZE);
			
			act.setOutData("ps", ps);
			act.setOutData("orderType", orderType);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户代交车确认查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户代交车确认明细页面查询
	 */
	public void fleetOrderDetailSureQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");		//申请ID
			String ver = request.getParamValue("ver");
			Map<String, Object> map = dao.getFleetOrderInfo(reqId);
			List<Map<String,Object>> list = dao.getFleetOrderDetailList(reqId);
			
			act.setOutData("reqId", reqId);
			act.setOutData("ver", ver);
			act.setOutData("map", map);
			act.setOutData("list", list);
			
			act.setForword(FLEET_ORDER_SURE_DTL_INIT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户代交车确认明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户代交车确认
	 */
	public void fleetOrderSureSave(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");
			String ver = request.getParamValue("ver");
			
			boolean verFlag = VerFlagDao.getVerFlag("TT_VS_DLVRY_REQ", "REQ_ID", reqId, ver);
			if(verFlag){
				TtVsDlvryReqPO tvdrp1 =new TtVsDlvryReqPO();
				TtVsDlvryReqPO tvdrp2 =new TtVsDlvryReqPO();
				tvdrp1.setReqId(Long.parseLong(reqId));
				tvdrp2.setUpdateDate(new Date(System.currentTimeMillis()));
				tvdrp2.setUpdateBy(logonUser.getUserId());
				
				tvdrp2.setReqStatus(Constant.ORDER_REQ_STATUS_01);
				
				dao.update(tvdrp1,tvdrp2);
				
				TtVsReqCheckPO tvrcp = new TtVsReqCheckPO();
				tvrcp.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
				tvrcp.setReqId(Long.parseLong(reqId));
				tvrcp.setCheckOrgId(logonUser.getOrgId());
				tvrcp.setCheckPositionId(logonUser.getPoseId());
				tvrcp.setCheckUserId(logonUser.getUserId());
				tvrcp.setCheckStatus(Constant.CHECK_STATUS_QRWC);
				tvrcp.setCheckDate(new Date(System.currentTimeMillis())) ;
				tvrcp.setCreateBy(logonUser.getUserId());
				tvrcp.setCreateDate(new Date(System.currentTimeMillis()));
				dao.insert(tvrcp);
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户代交车确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户订单取消 
	 */
	public void FleetOrderBackSureQuery(){
		FleetOrderCheck foc = new FleetOrderCheck() ;
		foc.FleetOrderBackCheckQuery();
	}
	
	/************************************** 本类中通用方法 ***********************************************/
}
