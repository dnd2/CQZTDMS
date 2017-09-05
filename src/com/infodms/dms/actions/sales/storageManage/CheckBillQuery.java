package com.infodms.dms.actions.sales.storageManage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sales.storageManage.CheckBillDAO;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtInspectionDetailPO;
import com.infodms.dms.po.TtVsInspectionDetailPO;
import com.infodms.dms.po.TtVsInspectionPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CheckBillQuery extends BaseDao{
	public Logger logger = Logger.getLogger(CheckBillQuery.class);
	private ActionContext act = ActionContext.getContext();
	private static final CheckBillDAO dao = new CheckBillDAO ();
	public static final CheckBillDAO getInstance() {
		return dao;
	}
	private final String  CheckVehicleQueryInitUrl = "/jsp/sales/storageManage/checkBillQueryInit.jsp";
	private final String  CheckVehicleQueryDetailUrl = "/jsp/sales/storageManage/checkBillQueryDetail.jsp";
	private final String  CheckVehicleQuery_OEM_InitUrl = "/jsp/sales/storageManage/checkVehicleQuery_OEM_Init.jsp";
	
	/**
	 * FUNCTION		:	车辆验收单查询面初始化
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-24
	 */
	public void checkBillInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.setOutData("dutyType", logonUser.getDutyType());
            act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(CheckVehicleQueryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收单查询面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * FUNCTION		:	车辆验收单查询：结果展示
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-24
	 */
	public void checkBillQuery(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.getSession().get(Constant.LOGON_USER);
			//发车日期
			String deliverystartDate =  CommonUtils.checkNull(request.getParamValue("deliverystartDate"));
			String deliveryendDate =  CommonUtils.checkNull(request.getParamValue("deliveryendDate"));
			//验收日期
			String checkstartDate =  CommonUtils.checkNull(request.getParamValue("checkstartDate"));
			String checkendDate =  CommonUtils.checkNull(request.getParamValue("checkendDate"));
			//VIN
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			String dlvNo = CommonUtils.checkNull(request.getParamValue("dlvNo"));
			String reqNo = CommonUtils.checkNull(request.getParamValue("reqNo"));
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId() ;
			String dutyType=logonUser.getDutyType();
			String dealerId=logonUser.getDealerId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerId(comId.toString(),poseId.toString());
			
			if(Constant.DUTY_TYPE_DEALER.toString().equals(dutyType)){
				String dealerIds__ = "";
				for(int i=0; i<areaList.size();i++) {
					dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
				}
				dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;
				List<Map<String, Object>> listA = DealerInfoDao.getInstance().getDel(dealerIds__) ;	//获得二级经销商
				String dealerIdA = dealerIds__ ;
				for(int i=0; i<listA.size();i++) {
					dealerIdA += "," + listA.get(i).get("DEALER_ID").toString() ;
				}
				
				//如果是一级店
				TmDealerPO td =new TmDealerPO();
				td.setDealerId(new Long(dealerId));
				td=(TmDealerPO) dao.select(td).get(0);
				if("10851001".equals(td.getDealerLevel().toString())){
					dealerId=dealerIdA;
				}
			}
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			String orgId=logonUser.getOrgId().toString();
			PageResult<Map<String, Object>> ps = dao.getCheckQuery(dealerCode,orgId,reqNo,dutyType,dealerId, deliverystartDate, deliveryendDate, checkstartDate, checkendDate, vin, dlvNo, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收单查询：结果展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	车辆验收查询：结果展示(经销商端)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-24
	 */
	public void checkVehicleTotalQueryDLR(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.getSession().get(Constant.LOGON_USER);
			//发车日期
			String deliverystartDate =  CommonUtils.checkNull(request.getParamValue("deliverystartDate"));
			String deliveryendDate =  CommonUtils.checkNull(request.getParamValue("deliveryendDate"));
			//验收日期
			String checkstartDate =  CommonUtils.checkNull(request.getParamValue("checkstartDate"));
			String checkendDate =  CommonUtils.checkNull(request.getParamValue("checkendDate"));
			//VIN
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			String dlvNo = CommonUtils.checkNull(request.getParamValue("dlvNo"));
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerId(comId.toString(),poseId.toString());
			String dealerIds__ = "";
			for(int i=0; i<areaList.size();i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
			}
			dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;
			List<Map<String, Object>> listA = DealerInfoDao.getInstance().getDel(dealerIds__) ;	//获得二级经销商
			String dealerIdA = dealerIds__ ;
			for(int i=0; i<listA.size();i++) {
				dealerIdA += "," + listA.get(i).get("DEALER_ID").toString() ;
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = CheckVehicleQueryDAO.getCheckQueryDLR_CSCTOTAL(dealerIdA, deliverystartDate, deliveryendDate, checkstartDate, checkendDate, vin, dlvNo, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收单查询：结果展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	车辆验收单查询：查看详细历史信息(经销商端)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-24
	 */
	public void checkVehicleQueryDLR_Detail(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.getSession().get(Constant.LOGON_USER);
			
			String vehicle_id = CommonUtils.checkNull(request.getParamValue("vehicle_id"));
			Map<String ,Object>m=dao.getCheckDetailInfo(vehicle_id);
			act.setOutData("checkDetail", m);
			act.setForword(CheckVehicleQueryDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收查询：查看详细历史信息(经销商端)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	车辆验收查询面初始化(车厂端)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-6-1
	 */
	public void checkVehicleQueryOEMInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.setOutData("dutyType", logonUser.getDutyType());
            act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(CheckVehicleQuery_OEM_InitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "验收入库查询面初始化(车厂端)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/**
	 * FUNCTION		:	车辆验收查询：结果展示(车厂端)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-6-1
	 */
	public void checkVehicleQueryOEM(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.getSession().get(Constant.LOGON_USER);
			//选择经销商
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			//发车日期
			String deliverystartDate =  CommonUtils.checkNull(request.getParamValue("deliverystartDate"));
			String deliveryendDate =  CommonUtils.checkNull(request.getParamValue("deliveryendDate"));
			//验收日期
			String checkstartDate =  CommonUtils.checkNull(request.getParamValue("checkstartDate"));
			String checkendDate =  CommonUtils.checkNull(request.getParamValue("checkendDate"));
			//VIN
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			String dlvNo=CommonUtils.checkNull(request.getParamValue("dlvNo"));
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			orgId = logonUser.getOrgId().toString() ;
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
					
			PageResult<Map<String, Object>> ps = CheckVehicleQueryDAO.getCheckQueryOEM_CSC(orgId, dealerCode, deliverystartDate, deliveryendDate, checkstartDate, checkendDate, vin, areaIds,dlvNo, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "验收入库查询面初始化(车厂端)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * FUNCTION		:	车辆验收查询：结果展示(车厂端)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-6-1
	 */
	public void checkVehicleTotalQueryOEM(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.getSession().get(Constant.LOGON_USER);
			//选择经销商
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			//发车日期
			String deliverystartDate =  CommonUtils.checkNull(request.getParamValue("deliverystartDate"));
			String deliveryendDate =  CommonUtils.checkNull(request.getParamValue("deliveryendDate"));
			//验收日期
			String checkstartDate =  CommonUtils.checkNull(request.getParamValue("checkstartDate"));
			String checkendDate =  CommonUtils.checkNull(request.getParamValue("checkendDate"));
			//VIN
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			orgId = logonUser.getOrgId().toString() ;
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
					
			PageResult<Map<String, Object>> ps = CheckVehicleQueryDAO.getCheckQueryOEM_CSCTOTAL(orgId, dealerCode, deliverystartDate, deliveryendDate, checkstartDate, checkendDate, vin, areaIds, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "验收入库查询面初始化(车厂端)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
