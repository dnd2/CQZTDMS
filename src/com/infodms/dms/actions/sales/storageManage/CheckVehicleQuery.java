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
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtInspectionDetailPO;
import com.infodms.dms.po.TtVsInspectionDetailPO;
import com.infodms.dms.po.TtVsInspectionPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CheckVehicleQuery extends BaseDao{
	public Logger logger = Logger.getLogger(CheckVehicleQuery.class);
	private ActionContext act = ActionContext.getContext();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO ();
	public static final CheckVehicleDAO getInstance() {
		return dao;
	}
	private final String  CheckVehicleQuery_DLR_InitUrl = "/jsp/sales/storageManage/checkVehicleQuery_DLR_Init.jsp";
	private final String  CheckVehicleQuery_DLR_DetailUrl = "/jsp/sales/storageManage/checkVehicleQuery_DLR_Detail.jsp";
	private final String  CheckVehicleQuery_OEM_InitUrl = "/jsp/sales/storageManage/checkVehicleQuery_OEM_Init.jsp";
	
	/**
	 * FUNCTION		:	车辆验收查询面初始化(经销商端)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-24
	 */
	public void CheckVehicleQueryDLRInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.setForword(CheckVehicleQuery_DLR_InitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收查询面初始化(经销商端)");
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
	public void checkVehicleQueryDLR(){
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
			PageResult<Map<String, Object>> ps = CheckVehicleQueryDAO.getCheckQueryDLR_CSC(dealerIdA, deliverystartDate, deliveryendDate, checkstartDate, checkendDate, vin, dlvNo, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收查询：结果展示(经销商端)");
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
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收查询：结果展示(经销商端)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	车辆验收查询：查看详细历史信息(经销商端)
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
			//验收基本信息
			Map<String,Object> map = CheckVehicleQueryDAO.getCheckVehicleQueryDLR_Detail_SUZUKI(vehicle_id);
			act.setOutData("checkDetail", map);
			
			/*
			 * 根据vehicle_id查询“验收ID(INSPECTION_ID)”，
			 * 再根据INSPECTION_ID查询对应的质损信息列表
			 * */
			TtVsInspectionPO inspectionPO = new TtVsInspectionPO();
			inspectionPO.setVehicleId(Long.parseLong(vehicle_id));
			List list = dao.select(inspectionPO);
			if (null != list && list.size()>0) {
				//Long inspectionId:多条质损信息对应一条验收信息
				Long inspectionId = ((TtVsInspectionPO)list.get(0)).getInspectionId();
				//止损信息
				TtVsInspectionDetailPO detailPO = new TtVsInspectionDetailPO();
				detailPO.setInspectionId(inspectionId);
				List<TtInspectionDetailPO> detailList = dao.select(detailPO);
				act.setOutData("detailList", detailList);
			}
			act.setForword(CheckVehicleQuery_DLR_DetailUrl);
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
