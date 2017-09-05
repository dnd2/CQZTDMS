package com.infodms.dms.actions.sales.displacement;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.actions.sales.customerInfoManage.SalesReport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.sales.customerInfoManage.SalesReportDAO;
import com.infodms.dms.dao.sales.displacement.DisplacementCarDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtDealerActualSalesPO;
import com.infodms.dms.po.TtVsCarDisplacementDlrPO;
import com.infodms.dms.po.TtVsCarDisplacementPrcPO;
import com.infodms.dms.po.TtVsUsedCarDisplacementPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class DisplacementCar {
	public Logger logger = Logger.getLogger(DisplacementCar.class);
	private DisplacementCarDao dao = DisplacementCarDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	ResponseWrapper response = act.getResponse();
	private final String DisplacementCarURL = "/jsp/sales/displacement/DisplancementCar.jsp";
	private final String DisplancementCarRequerURL = "/jsp/sales/displacement/DisplancementCarRequer.jsp";
	private final String DisplancementCarRequerURL2 = "/jsp/sales/displacement/DisplancementOtherCarRequer.jsp";
	private final String DisplancementCarInsertInitURL = "/jsp/sales/displacement/DisplancementCarInsertInit.jsp";
	private final String DisplacementCarQueryURL = "/jsp/sales/displacement/DisplancementQueryCek.jsp";
	private final String DisplacementCarQueryOrgURL = "/jsp/sales/displacement/DisplancementQueryOrg.jsp";
	private final String DisplancementQueryOrgInfoURL = "/jsp/sales/displacement/DisplancementQueryOrgInfo.jsp";
	private final String DisplacementPrcCarURL = "/jsp/sales/displacement/DisplacementPrcCar.jsp";
	private final String DisplacementPrcInsertCarURL="/jsp/sales/displacement/DisplancementPrcInsertInit.jsp";
	private final String DisplacementCarPrcOneInfoURL="/jsp/sales/displacement/DisplancementQueryUpdate.jsp";
	private final String DisplacementCarSumURL="/jsp/sales/displacement/DisplacementCarSum.jsp";
	private final String DisplacementCarADDSumURL="/jsp/sales/displacement/DisplacementCarSumAdd.jsp";
	private final String OPEN_WIN_Displacement_URL="/jsp/sales/displacement/DisplacementOpenWin.jsp";
	private final String DisplacementCarDetailURL="/jsp/sales/displacement/DisplacementCarDetail.jsp";
	/**
	 * 跳转二手车置换页面
	 */
	public void DisplacementCarInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(DisplacementCarURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换申请页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 查询 新车信息 根据vehicle_id
	 */
	public void DisplacementNewCarInfo(){
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String price = "0" ;
			String NEW_VIN = request.getParamValue("NEW_VIN");
			String NEW_VIN2 = request.getParamValue("NEW_VIN2");
			if(null == NEW_VIN && null != NEW_VIN2){
				NEW_VIN = NEW_VIN2;
			}
			
			String dealerId = request.getParamValue("DEALER_ID");
			String displacement_type = request.getParamValue("displacement_type");
			// 1.查询“新车车辆信息”
			Map<String, Object> newCarInfo = dao.getDisplanceNewCarInfo(NEW_VIN,null);
				
			price = dao.getReturnPrice(dealerId, displacement_type) ;
			if(!"-1".equals(price)){
				act.setOutData("price", price);
				act.setOutData("returnFalse1", 1);
			}else{
				act.setOutData("price", "0");
				act.setOutData("returnFalse1", 2);
			}
			
			if(newCarInfo==null || newCarInfo.equals("")){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			act.setOutData("newCarInfo", newCarInfo);
			
			// 1.查询“车辆资料以及根据客户类型带出相应的客户信息”
			Map<String, Object> vehicleInfo = dao
					.getDisplanceMentVinInfo(NEW_VIN);
			act.setOutData("vehicleInfo", vehicleInfo);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换申请页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

	
	
	/**
	 * 查询 新车信息 根据vehicle_id
	 */
	public void DisplacementNewCarAddInfo(){
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//查询该车是公司客户还是个人客户
			String NEW_VIN = request.getParamValue("NEW_VIN");
			Map<String, Object> vehicleInfo = dao.getDisplanceMentVinInfo(NEW_VIN);
			act.setOutData("vehicleInfo", vehicleInfo);
			// 1.查询“新车车辆信息”
			Map<String, Object> newCarInfo = dao
					.getDisplanceNewCarInfo(NEW_VIN,logonUser.getDealerId());
			if(vehicleInfo==null || vehicleInfo.equals("")){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			act.setOutData("newCarInfo", newCarInfo);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换申请页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询所有的实销数据 选择相对应的车进行二手车置换
	 */
	public void DisplacementCarInfo() {
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String vehicle_id = request.getParamValue("vehicle_id");
			String dealerId = request.getParamValue("dealer_id");
			
			// 1.查询“车辆资料以及根据客户类型带出相应的客户信息”
			Map<String, Object> vehicleInfo = dao.getDisplanceMentInfo(vehicle_id);
			String returnRrice = dao.getReturnPrice(dealerId, Constant.DisplancementCarrequ_replace_1.toString());
			act.setOutData("vehicleInfo", vehicleInfo);
			act.setOutData("vehicle_id", vehicle_id);
			act.setOutData("dealerId", dealerId) ;
			act.setOutData("returnRrice", returnRrice) ;
			act.setForword(DisplancementCarRequerURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换申请页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询所有的实销数据 选择相对应的车进行二手车置换
	 */
	public void DisplacementCarInfo2() {
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String vehicle_id = request.getParamValue("vehicle_id");
			String dealerId = request.getParamValue("dealer_id");
			
			// 1.查询“车辆资料以及根据客户类型带出相应的客户信息”
			Map<String, Object> vehicleInfo = dao.getDisplanceMentInfo(vehicle_id);
			String returnRrice = dao.getReturnPrice(dealerId, Constant.DisplancementCarrequ_replace_1.toString());
			act.setOutData("vehicleInfo", vehicleInfo);
			act.setOutData("vehicle_id", vehicle_id);
			act.setOutData("dealerId", dealerId) ;
			act.setOutData("returnRrice", returnRrice);
			act.setForword(DisplancementCarRequerURL2);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换申请页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 2手车置换作废获取车辆价格
	 */
	public void DisplacementfeiQuery(){
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			String returnPrice = "0" ;
			String dealerId= request.getParamValue("DEALER_ID");
			String displacement_type = request.getParamValue("displacement_type");
		
			returnPrice = dao.getReturnPrice(dealerId, displacement_type) ;
			if(!"-1".equals(returnPrice)){
				act.setOutData("returnfeiPrice", returnPrice);
				act.setOutData("returnFalse1", 1);
			}else{
				act.setOutData("returnFalse1", 2);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换申请页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}

	/**
	 * 查询所有的实销数据 选择相对应的车进行二手车置换
	 */
	public void DisplacementQuery() {
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			// 得到VIN
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}else {
				vin = "L";
			}
			// 通过职位 获取经销商 id
			DealerRelation dr = new DealerRelation();
			String dealerIds__ = dr.getDealerIdByPose(logonUser.getCompanyId(),
					logonUser.getPoseId());

			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getDisplancementCar(vin, dealerIds__, Constant.PAGE_SIZE,
							curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换申请页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 针对非长安汽车插入界面跳转
	 */
	public void DisplacementInsertCarInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(DisplancementCarInsertInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换申请页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 针对非长安汽车和长安汽车进行插入界面插入操作 对于非长安汽车和长安汽车的判断逻辑在界面处理
	 */
	public void DisplacementInsertCar() {  //YH 2011.6.1
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			
			String IS_CHANA = request.getParamValue("IS_CHANA");			
			String OLD_BRAND_NAME = request.getParamValue("OLD_BRAND_NAME");
			String displacement_type = request.getParamValue("displacement_type");
			String VEHICLE_ID = request.getParamValue("VEHICLE_ID");
			String ORDER_ID = request.getParamValue("ORDER_ID");
			String OEM_COMPANY_ID = request.getParamValue("OEM_COMPANY_ID");
			String DEALER_ID = request.getParamValue("DEALER_ID");
			
			String OLD_VIN = request.getParamValue("NEW_VIN");
			String OLD_MODEL_NAME = request.getParamValue("NEW_MODEL_NAME1");
			String OLD_SLES_DATE = request.getParamValue("NEW_SALES_DATE1");
			String CTM_NAME = request.getParamValue("CTM_NAME");
			String fanliPrc = request.getParamValue("fanliPrc");
			
			
			String OLD_VIN2 = request.getParamValue("NEW_VIN2");
			String OLD_MODEL_NAME2 = request.getParamValue("NEW_MODEL_NAME2");
			String OLD_SLES_DATE2 = request.getParamValue("NEW_SALES_DATE2");
			String CTM_NAME2 = request.getParamValue("CTM_NAME2");
			String feifanliPrc = request.getParamValue("feifanliPrc");	
			String SCRAP_CERTIFY_NO = request.getParamValue("SCRAP_CERTIFY_NO");
			String SCRAP_DATE = request.getParamValue("SCRAP_DATE");

			String remark = request.getParamValue("remark");
				
			int reson = 0 ;
			
			TmVehiclePO oldTvPO = new TmVehiclePO() ;
			oldTvPO.setVehicleId(Long.parseLong(VEHICLE_ID)) ;
			TmVehiclePO newTvPO = new TmVehiclePO() ;
			newTvPO.setLockStatus(Constant.LOCK_STATUS_ZHSD) ;
			
			dao.update(oldTvPO, newTvPO) ;
			
			if(Integer.parseInt(displacement_type) == Constant.DisplancementCarrequ_replace_1 || Integer.parseInt(displacement_type) == Constant.DisplancementCarrequ_replace_3 || Integer.parseInt(displacement_type) == Constant.DisplancementCarrequ_replace_4 || Integer.parseInt(displacement_type) == Constant.DisplancementCarrequ_replace_5){
				Map<String, String> map = new HashMap<String, String>();
				map.put("OLD_BRAND_NAME", OLD_BRAND_NAME);
				map.put("displacement_type", displacement_type);
				map.put("OLD_VIN", OLD_VIN);
				map.put("OLD_MODEL_NAME", OLD_MODEL_NAME);
				map.put("OLD_SLES_DATE", OLD_SLES_DATE);
				map.put("CTM_NAME", CTM_NAME);
				map.put("fanliPrc", fanliPrc);
				map.put("ORDER_ID", ORDER_ID);
				map.put("OEM_COMPANY_ID", OEM_COMPANY_ID);
				map.put("VEHICLE_ID", VEHICLE_ID);
				map.put("OPERATE_STATUS", displacement_type);
				map.put("IS_CHANA", IS_CHANA);
		
				map.put("useid", logonUser.getUserId().toString());
				map.put("remark", remark);
				map.put("DEALERID", DEALER_ID);
				
				String []fjids = request.getParamValues("fjid");
				
		if (null != VEHICLE_ID && !VEHICLE_ID.equals("")) {  //如果是以旧换新 YH 2011.6.1
					TtDealerActualSalesPO po1 = new TtDealerActualSalesPO();
					po1.setVehicleId(Long.parseLong(VEHICLE_ID));
					po1.setIsReturn(Constant.IF_TYPE_NO) ;
					TtDealerActualSalesPO po2 = new TtDealerActualSalesPO();
					po2.setDisplacementStatus(Constant.STATUS_ENABLE);
					dao.update(po1, po2);
				}
				
				String disId= SequenceManager.getSequence("");
				map.put("disId", disId);
				map.put("displacement_no","ZH"+disId);
				//附件添加
				FileUploadManager.fileUploadByBusiness(disId, fjids, logonUser);
				
				reson = dao.insertDisplace(map);				
			}

         if(Integer.parseInt(displacement_type) == Constant.DisplancementCarrequ_replace_2){ //如果是报废 YH 2011.6.1
				Map<String, String> map = new HashMap<String, String>();
				map.put("OLD_BRAND_NAME", OLD_BRAND_NAME);
				map.put("displacement_type", displacement_type);
				map.put("OLD_VIN", OLD_VIN2);
				map.put("OLD_MODEL_NAME", OLD_MODEL_NAME2);
				map.put("OLD_SLES_DATE", OLD_SLES_DATE2);
				map.put("CTM_NAME", CTM_NAME2);
				map.put("fanliPrc", feifanliPrc);
				
				map.put("ORDER_ID", ORDER_ID);
				map.put("OEM_COMPANY_ID", OEM_COMPANY_ID);
				map.put("VEHICLE_ID", VEHICLE_ID);
				map.put("OPERATE_STATUS", Constant.DisplancementCarrequ_cek_1.toString());
				map.put("SCRAP_CERTIFY_NO", SCRAP_CERTIFY_NO);
				map.put("SCRAP_DATE", SCRAP_DATE);
				map.put("IS_CHANA", IS_CHANA);
				
				map.put("useid", logonUser.getUserId().toString());
				map.put("remark", remark);
				map.put("DEALERID", DEALER_ID);
				
				String []fjids = request.getParamValues("fjid");
				
				if (null != VEHICLE_ID && !VEHICLE_ID.equals("")) {
					TtDealerActualSalesPO po1 = new TtDealerActualSalesPO();
					po1.setVehicleId(Long.parseLong(VEHICLE_ID));
					po1.setIsReturn(Constant.IF_TYPE_NO) ;
					TtDealerActualSalesPO po2 = new TtDealerActualSalesPO();
					po2.setDisplacementStatus(1);
					dao.update(po1, po2);
				}
				
				String disId= SequenceManager.getSequence("");
				map.put("disId", disId);
				map.put("displacement_no","ZH"+disId);
				//附件添加
				FileUploadManager.fileUploadByBusiness(disId, fjids, logonUser);
				
				reson = dao.insertDisplace(map);			
			}
			if (reson == 1) {
				act.setForword(DisplacementCarURL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换申请页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 跳转二手车置换审核记录查询页面
	 */
	public void DisplacementQueryCekCarInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(DisplacementCarQueryURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置查询页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 跳转二手车置换审核记录查询页面
	 */
	public void DisplacementQueryCekCar() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String userId = logonUser.getUserId().toString();
			String displacement_type = request
					.getParamValue("displacement_type");
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao
					.getDisplancementQueryCar(userId, displacement_type,
							Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换查询页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 跳转大区二手车置换审核
	 */
	public void DisplacementQueryOrgCar() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(DisplacementCarQueryOrgURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换查询页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 二手车置换大区审核查询
	 */
	public void DisplacementQueryCekOrgCar() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String userId = logonUser.getUserId().toString();
			String displacement_type = request.getParamValue("displacement_type");
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			String orgId = logonUser.getOrgId().toString();
			PageResult<Map<String, Object>> ps = dao.getDisplancementQueryOrgCar(orgId, userId,displacement_type, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换查询页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 二手车置换大区审核
	 *//*
	public void DisplacementQueryCekOrgQueryCar() {
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			lodaoBean) act.getSession().get(Constant.LOGON_USER);
			String DISPLACEMENT_ID = request.getParamValue("DISPLACEMENT_ID");
			// 1.查询“置换车辆的原始数据和更新数据进行审核”
			Map<String, Object> displaceMentInfo = dao.getDisplanceQueryOrgInfo(DISPLACEMENT_ID);
			// 1.查询“车辆资料以及根据客户类型带出相应的客户信息”
			Map<String, Object> vehicleInfo=null;
			String DISPLACEMENT_TYPE=displaceMentInfo.get("DISPLACEMENT_TYPE").toString();
			String dealerId="";
			if(DISPLACEMENT_TYPE.equals(Constant.DisplancementCarrequ_replace_1)){
				vehicleInfo= dao.getDisplanceMentVinInfo(displaceMentInfo.get("OLD_VIN").toString());
				dealerId=vehicleInfo.get("DEALER_ID").toString();
			}else{
				vehicleInfo= dao.getDisplanceMentVinInfo(displaceMentInfo.get("NEW_VIN").toString());
				dealerId=vehicleInfo.get("DEALER_ID").toString();
			}
			TtVsCarDisplacementDlrPO po2=new TtVsCarDisplacementDlrPO();
			po2.setDealerId(Long.parseLong(dealerId));
			TtVsCarDisplacementDlrPO po3=(TtVsCarDisplacementDlrPO) dao.select(po2).get(0);
			String displacementPrc=po3.getDisplacementPrc();
			TtVsCarDisplacementPrcPO po4=new TtVsCarDisplacementPrcPO();
			po4.setDisplacementPrc(displacementPrc);
			po4.setDisplacementType(DISPLACEMENT_TYPE);
			TtVsCarDisplacementPrcPO po5=(TtVsCarDisplacementPrcPO) dao.select(po4).get(0);
			act.setOutData("price", po5.getPrice());
			
			act.setOutData("vehicleInfo", vehicleInfo);
			act.setOutData("displaceMentInfo", displaceMentInfo);
			act.setOutData("DISPLACEMENT_ID", DISPLACEMENT_ID);
			act.setForword(DisplancementQueryOrgInfoURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换申请页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}*/

	/**
	 * 二手车置换大区审核
	 */
	public void DisplacementCekOrgCar() {
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String checkres = request.getParamValue("checkres");// 审核状态
			String cekremark = request.getParamValue("cekremark");// 审核意见
			String userid = logonUser.getUserId().toString();// 用户
			String DISPLACEMENT_ID = request.getParamValue("DISPLACEMENT_ID");// 二手车置换id
			dao.insertDisplaceCek(checkres, cekremark, userid,DISPLACEMENT_ID);
			String VEHICLE_ID = request.getParamValue("VEHICLE_ID");
			String orgId=logonUser.getOrgId().toString();
			if(!orgId.equals(Constant.OEM_COM_SVC)){
			if (checkres.equals("1")) {
				TtVsUsedCarDisplacementPO po1 = new TtVsUsedCarDisplacementPO();
				po1.setDisplacementId(Long.parseLong(DISPLACEMENT_ID));
				TtVsUsedCarDisplacementPO po2 = new TtVsUsedCarDisplacementPO();
				po2.setOperateStatus(Constant.DisplancementCarrequ_cek_2);
				dao.update(po1, po2);
			} else {
				TtVsUsedCarDisplacementPO po1 = new TtVsUsedCarDisplacementPO();
				po1.setDisplacementId(Long.parseLong(DISPLACEMENT_ID));
				TtVsUsedCarDisplacementPO po2 = new TtVsUsedCarDisplacementPO();
				po2.setOperateStatus(Constant.DisplancementCarrequ_cek_3);
				dao.update(po1, po2);
				if (null != VEHICLE_ID && !VEHICLE_ID.equals("")) {
					TtDealerActualSalesPO po3 = new TtDealerActualSalesPO();
					po3.setVehicleId(Long.parseLong(VEHICLE_ID));
					po3.setIsReturn(Constant.IF_TYPE_NO) ;
					TtDealerActualSalesPO po4 = new TtDealerActualSalesPO();
					po4.setDisplacementStatus(0);
					dao.update(po3, po4);
				}
			}
			}else{
				if (checkres.equals("1")) {
					TtVsUsedCarDisplacementPO po1 = new TtVsUsedCarDisplacementPO();
					po1.setDisplacementId(Long.parseLong(DISPLACEMENT_ID));
					TtVsUsedCarDisplacementPO po2 = new TtVsUsedCarDisplacementPO();
					po2.setOperateStatus(Constant.DisplancementCarrequ_cek_4);
					dao.update(po1, po2);
					if (null != VEHICLE_ID && !VEHICLE_ID.equals("")) {
						TtDealerActualSalesPO po3 = new TtDealerActualSalesPO();
						po3.setVehicleId(Long.parseLong(VEHICLE_ID));
						po3.setIsReturn(Constant.IF_TYPE_NO) ;
						TtDealerActualSalesPO po4 = new TtDealerActualSalesPO();
						po4.setDisplacementStatus(0);
						dao.update(po3, po4);
					}
				} else {
					TtVsUsedCarDisplacementPO po1 = new TtVsUsedCarDisplacementPO();
					po1.setDisplacementId(Long.parseLong(DISPLACEMENT_ID));
					TtVsUsedCarDisplacementPO po2 = new TtVsUsedCarDisplacementPO();
					po2.setOperateStatus(Constant.DisplancementCarrequ_cek_3);
					dao.update(po1, po2);
					if (null != VEHICLE_ID && !VEHICLE_ID.equals("")) {
						TtDealerActualSalesPO po3 = new TtDealerActualSalesPO();
						po3.setVehicleId(Long.parseLong(VEHICLE_ID));
						TtDealerActualSalesPO po4 = new TtDealerActualSalesPO();
						po4.setDisplacementStatus(0);
						dao.update(po3, po4);
					}
				}
				
			}
			act.setForword(DisplacementCarQueryOrgURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换申请页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转大区二手车置换价格维护
	 */
	public void DisplacementPrcCar() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(DisplacementPrcCarURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换查询页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 跳转大区二手车置换价格维护查询
	 */
	public void DisplacementPrcCarQuery() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request=act.getRequest();
			String DISPLACEMENT_TYPE=request.getParamValue("DISPLACEMENT_TYPE");
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getDisplancementCarPrc(DISPLACEMENT_TYPE,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换资格价格维护查询页面异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转大区二手车置换价格维护
	 */
	public void DisplacementPrcInsertCar() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(DisplacementPrcInsertCarURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换资格价格查询新增异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增资格价格数据
	 */
	public void DisplacementPrcInsertCarInfo() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper  request=act.getRequest();
			String DISPLACEMENT_TYPE=request.getParamValue("DISPLACEMENT_TYPE");//置换类型
			String PRICE=request.getParamValue("PRICE");//价格
			String DISPLACEMENT_PRC=request.getParamValue("DISPLACEMENT_PRC");//维护资格
			//String dealerId=request.getParamValue("dealerId2");//获取经销商ID
			String prcId=SequenceManager.getSequence("");
			Long userId=logonUser.getUserId();
			int count = dao.insertDisplacePrc(prcId,userId,DISPLACEMENT_TYPE, PRICE, DISPLACEMENT_PRC);
			act.setForword(DisplacementPrcCarURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换资格价格查询新增异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 修改二手车置换信息
	 */
	public void DisplacementCarPrcInfo(){

		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper  request=act.getRequest();
			Long userId=logonUser.getUserId();
			String DISPLACEMENT_PRICE_ID=request.getParamValue("DISPLACEMENT_PRICE_ID");//置换资格主键
			String DISPLACEMENT_TYPE=request.getParamValue("DISPLACEMENT_TYPE");//置换类型
			String PRICE=request.getParamValue("PRICE");//价格
			String DISPLACEMENT_PRC=request.getParamValue("DISPLACEMENT_PRC");//维护资格
			int count=dao.updateDisplacePrc(DISPLACEMENT_PRICE_ID,userId,DISPLACEMENT_TYPE,PRICE,DISPLACEMENT_PRC);
			act.setForword(DisplacementPrcCarURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "修改二手车置换信息异常");
			logger.error(logonUser, e1);
			act.setException(e1);
	}
	
	}
		/**
		 * 修改二手车置换信息
		 */
		public void DisplacementCarPrcOneInfo(){
			AclUserBean logonUser = null;
			try {
				logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				RequestWrapper  request=act.getRequest();
				String displacementPriceId=request.getParamValue("DISPLACEMENT_PRICE_ID");//置换资格主键
				act.setOutData("displacementPriceId", displacementPriceId);
				Map<String, Object>  disprcCar=dao.getCarPrcOneInfo(displacementPriceId);
				act.setOutData("disprcCar", disprcCar);
				act.setForword(DisplacementCarPrcOneInfoURL);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "修改二手车置换信息异常");
				logger.error(logonUser, e1);
				act.setException(e1);
		}
		
    }
		
		/**
		 * 经销商审核二手车置换汇总
		 */
		public void DisplacementSumCarQuery() {
			AclUserBean logonUser = null;
			try {
				logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				act.setForword(DisplacementCarSumURL);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换资格价格维护查询页面异常");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		
		/**
		 * 维护二手车
		 */
		public void addUrlInit(){
			AclUserBean logonUser = null;
			try{
				logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				String seq=SequenceManager.getSequence("");
				act.setOutData("reportCode", "BH"+seq);
				act.setOutData("reportId", seq);
				act.setOutData("date", new Date());
				act.setOutData("userName", logonUser.getName());
				act.setForword(DisplacementCarADDSumURL);
			} catch(Exception e){
				BizException be = new BizException(act,e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "维护二手车");
				logger.error(logonUser, be);
				act.setException(be);
			}
		}
		
		
		/*
		 * 查询所有审核完成的 二手车置换车辆数据
		 */
		public void OpenWinDisplacementUrl(){
			AclUserBean logonUser = null;
			try{
				logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				act.setForword(OPEN_WIN_Displacement_URL);
			} catch(Exception e){
				BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "查询所有审核完成的 二手车置换车辆数据");
				logger.error(logonUser, be);
				act.setException(be);
			}
		}
		
		/**
		 * 查询所有需要新增的二手车置换数据信息
		 */
		public void DisplacementOpeUrlQuery(){
			AclUserBean logonUser = null;
			try {
				logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				RequestWrapper request=act.getRequest();
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
				String yieldly=request.getParamValue("yieldly");
				PageResult<Map<String, Object>> ps = dao.getDisplancementOpenQuery(yieldly,Constant.PAGE_SIZE,curPage);
				act.setOutData("ps", ps);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "查询所有需要新增的二手车置换数据信息");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		
		
		/**
		 * 新增单据数据
		 */
		public void DisplacementAddDj(){
			AclUserBean logonUser = null;
			try {
				logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				RequestWrapper request=act.getRequest();
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
				
				String yieldly=request.getParamValue("yieldly");
				String reportCode=request.getParamValue("reportCode");
				String userName=request.getParamValue("userName");
				String date=request.getParamValue("date");
				String [] displacementId=request.getParamValues("displacementId");
				String reportId=request.getParamValue("reportId");
				int i = dao.saveDisplanment(reportCode,userName,date,displacementId,reportId,yieldly);
				for(int j=0;j<displacementId.length;j++){
				TtVsUsedCarDisplacementPO po=new TtVsUsedCarDisplacementPO();
				po.setDisplacementId(Long.parseLong(displacementId[j]));
				TtVsUsedCarDisplacementPO po1=new TtVsUsedCarDisplacementPO();
				po1.setStatus(1);
				dao.update(po, po1);
				}
				act.setForword(DisplacementCarSumURL);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "新增单据");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		
		/**
		 * 查询单据数据加载明细并打印
		 */
		public void DisplacementDjQueryInit(){
			AclUserBean logonUser = null;
			try {
				logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				RequestWrapper request=act.getRequest();
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
				String reportCode=request.getParamValue("reportCode");//单据编码
				String dealerName=request.getParamValue("dealerName");//制单单位
				String yieldly =request.getParamValue("yieldly");//渠道
				PageResult<Map<String, Object>> ps = dao.getDisplancementDjQuery(reportCode,dealerName,yieldly,Constant.PAGE_SIZE,curPage);
				act.setOutData("ps", ps);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "总部亏总打印添加");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		
		/**
		 * 明细打印单据
		 */
		public void DisplacementDjDetailInit(){
			AclUserBean logonUser = null;
			try {
				logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				RequestWrapper request=act.getRequest();
				String reportId=request.getParamValue("id");//单据ID
				Map<String, Object> map = dao.getDisplanceDj(reportId);
				act.setOutData("mymap",map);
				List<Map<String, Object>> list=dao.getDisplanceDjList(reportId);
				act.setOutData("mylist", list);
				act.setForword(DisplacementCarDetailURL);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换 查询打印");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		
		
		
		
	

}
