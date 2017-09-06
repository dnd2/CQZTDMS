package com.infodms.yxdms.action.afterSales;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;





















import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.dao.afterSales.ServiceOrderAuthDao;
import com.infodms.yxdms.dao.afterSales.ServiceOrderDao;
import com.infodms.yxdms.utils.BaseAction;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 2017-08-3
 * 售后服务工单管理
 * @author free.AI
 *
 */
public class ServiceOrderAction extends BaseAction{
	  private Logger logger = Logger.getLogger(ServiceOrderAction.class);
	  private ActionContext act = ActionContext.getContext();
	  RequestWrapper request = act.getRequest();
	  AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	  private ServiceOrderDao dao = new ServiceOrderDao();
	  
	  private static final String SERVICE_ORDER_URL = "/jsp/afterSales/serviceOrder/serviceOrderList.jsp"; //售后服务工单列表
	  
	  //售后服务工单新增
	  private static final String SERVICE_ORDER_NORMAL_ADD_URL = "/jsp/afterSales/serviceOrder/serviceOrderNormalAdd.jsp"; //正常维修
	  private static final String SERVICE_ORDER_OUT_ADD_URL = "/jsp/afterSales/serviceOrder/serviceOrderOutAdd.jsp"; //外出维修
	  private static final String SERVICE_ORDER_PRE_SALE_ADD_URL = "/jsp/afterSales/serviceOrder/serviceOrderPreSaleAdd.jsp"; //售前维修
	  private static final String SERVICE_ORDER_MAINTAIN_ADD_URL = "/jsp/afterSales/serviceOrder/serviceOrderMaintainAdd.jsp"; //保养
	  private static final String SERVICE_ORDER_ACTIVITY_ADD_URL = "/jsp/afterSales/serviceOrder/serviceOrderActivityAdd.jsp"; //服务活动
      private static final String SERVICE_ORDER_SPECIAL_ADD_URL = "/jsp/afterSales/serviceOrder/serviceOrderSpecialAdd.jsp"; //特殊维修
	  private static final String SERVICE_ORDER_PDI_ADD_URL = "/jsp/afterSales/serviceOrder/serviceOrderPdiAdd.jsp"; //PDI
	  private static final String SERVICE_ORDER_CLAIM_ADD_URL = "/jsp/afterSales/serviceOrder/serviceOrderClaimAdd.jsp"; //备件维修
      //售后服务工单查看
	  private static final String SERVICE_ORDER_NORMAL_SHOW_URL = "/jsp/afterSales/serviceOrder/serviceOrderNormalShow.jsp"; //正常维修
	  private static final String SERVICE_ORDER_OUT_SHOW_URL = "/jsp/afterSales/serviceOrder/serviceOrderOutShow.jsp"; //外出维修
	  private static final String SERVICE_ORDER_PRE_SALE_SHOW_URL = "/jsp/afterSales/serviceOrder/serviceOrderPreSaleShow.jsp"; //售前维修
	  private static final String SERVICE_ORDER_SPECIAL_SHOW_URL = "/jsp/afterSales/serviceOrder/serviceOrderSpecialShow.jsp"; //特殊维修
	  private static final String SERVICE_ORDER_PDI_SHOW_URL = "/jsp/afterSales/serviceOrder/serviceOrderPdiShow.jsp"; //PDI
	  private static final String SERVICE_ORDER_CLAIM_SHOW_URL = "/jsp/afterSales/serviceOrder/serviceOrderClaimShow.jsp"; //备件维修
	  //售后服务工单修改
	  private static final String SERVICE_ORDER_NORMAL_UPDATE_URL = "/jsp/afterSales/serviceOrder/serviceOrderNormalUpdate.jsp"; //正常维修
	  private static final String SERVICE_ORDER_OUT_UPDATE_URL = "/jsp/afterSales/serviceOrder/serviceOrderOutUpdate.jsp"; //外出维修
	  private static final String SERVICE_ORDER_PRE_SALE_UPDATE_URL = "/jsp/afterSales/serviceOrder/serviceOrderPreSaleUpdate.jsp"; //售前维修
	  private static final String SERVICE_ORDER_SPECIAL_UPDATE_URL = "/jsp/afterSales/serviceOrder/serviceOrderSpecialUpdate.jsp"; //特殊维修
	  private static final String SERVICE_ORDER_PDI_UPDATE_URL = "/jsp/afterSales/serviceOrder/serviceOrderPdiUpdate.jsp"; //PDI
	  private static final String SERVICE_ORDER_CLAIM_UPDATE_URL = "/jsp/afterSales/serviceOrder/serviceOrderClaimUpdate.jsp"; //备件维修
	  
	  private static final String SERVICE_ORDER_EDIT_URL = "/jsp/afterSales/serviceOrder/serviceOrderEdit.jsp"; //售后服务工单编辑
	  
	  private static final String SERVICE_REPAIR_TYPE_WIN_URL = "/jsp/afterSales/serviceOrder/serviceRepairTypeWin.jsp"; //售后服务工单-维修类型弹窗
	  private static final String SERVICE_ACTIVITY_WIN_URL = "/jsp/afterSales/serviceOrder/serviceActivityWin.jsp"; //售后服务工单-服务活动弹窗
	  private static final String SERVICE_PROJECT_WIN_URL = "/jsp/afterSales/serviceOrder/serviceProjectWin.jsp"; //售后服务工单-维修项目弹窗
	  private static final String SERVICE_PART_WIN_URL = "/jsp/afterSales/serviceOrder/servicePartWin.jsp"; //售后服务工单-维修配件弹窗
	  private static final String SERVICE_OUT_PROJECT_WIN_URL = "/jsp/afterSales/serviceOrder/serviceOutProjectWin.jsp"; //售后服务工单-外出项目弹窗
	  private static final String SERVICE_OUT_REPAIR_WIN_URL = "/jsp/afterSales/serviceOrder/serviceOutRepairWin.jsp"; //售后服务工单-外出维修弹窗
    
	  private static final String[] EMPTY_ARRAY= new String[0];
	  
	/**
	 * @description 售后服务工单
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOrderList(){
		try {
			act.setForword(SERVICE_ORDER_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后服务工单列表");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 售后服务工单查询
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOrderQuery(){
		try {
			//页面参数
			String serviceOrderCode = CommonUtils.checkNull(request.getParamValue("serviceOrderCode"));//工单号
			String licenseNo = CommonUtils.checkNull(request.getParamValue("licenseNo"));//车牌号
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
			String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));//维修类型
			String status = CommonUtils.checkNull(request.getParamValue("status"));//工单状态
			String createDateBegin = CommonUtils.checkNull(request.getParamValue("createDateBegin"));//工单创建开始时间
			String createDateEnd = CommonUtils.checkNull(request.getParamValue("createDateEnd"));//工单创建结束时间
			
			Map<String,Object>params = new HashMap<String, Object>();
			
			params.put("serviceOrderCode", serviceOrderCode);
			params.put("licenseNo", licenseNo);
			params.put("vin", vin);
			params.put("repairType", repairType);
			params.put("status", status);
			params.put("createDateBegin", createDateBegin);
			params.put("createDateEnd", createDateEnd);
			
			params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>>ps = dao.serviceOrderQuery(params,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后服务工单查询");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 售后服务工单新增
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOrderAdd(){
		try {
			String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));//维修类型ID
			String repairTypeName = dao.getTcCodeName(repairType);
			//获取当前登录经销商信息
			String loginDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			String loginDealerCode = "";
			String loginDealerName = "";
			String loginDealerPhone = "";
			String partFareRate = "";
			
			if(!loginDealerId.equals("")){
				TmDealerPO tdPo = new TmDealerPO();
				tdPo.setDealerId(Utility.getLong(loginDealerId));
				List<PO> tmDealerList = dao.select(tdPo);
				if(tmDealerList!=null&&tmDealerList.size()>0){
					TmDealerPO tmDealerPo = (TmDealerPO) tmDealerList.get(0);
					loginDealerCode = tmDealerPo.getDealerCode();
					loginDealerName = tmDealerPo.getDealerName();
					loginDealerPhone = tmDealerPo.getPhone();
				}
			}
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
			
			Map<String,Object> partFareRateMap = dao.getPartFareRate(params);
			if(partFareRate!=null&&!CommonUtils.checkNull(partFareRateMap.get("PART_FARE_RATE")).equals("")){
				partFareRate = CommonUtils.checkNull(partFareRateMap.get("PART_FARE_RATE"));
			}
			
			act.setOutData("repairType", repairType);
			act.setOutData("repairTypeName", repairTypeName);
			act.setOutData("loginDealerCode", loginDealerCode);
			act.setOutData("loginDealerName", loginDealerName);
			act.setOutData("loginDealerPhone", loginDealerPhone);
			act.setOutData("partFareRate", partFareRate);
			
			//根据维修类型跳转到不同新增页面
			if(repairType.equals(Constant.REPAIR_TYPE_01.toString())){//正常维修
				act.setForword(SERVICE_ORDER_NORMAL_ADD_URL);
			}else if(repairType.equals(Constant.REPAIR_TYPE_02.toString())){//外出维修
				act.setForword(SERVICE_ORDER_OUT_ADD_URL);
			}else if(repairType.equals(Constant.REPAIR_TYPE_03.toString())){//售前维修
				act.setForword(SERVICE_ORDER_PRE_SALE_ADD_URL);
			}else if(repairType.equals(Constant.REPAIR_TYPE_04.toString())){//保养
				act.setForword(SERVICE_ORDER_MAINTAIN_ADD_URL);
			}else if(repairType.equals(Constant.REPAIR_TYPE_05.toString())){//服务活动
				act.setForword(SERVICE_ORDER_ACTIVITY_ADD_URL);
			}else if(repairType.equals(Constant.REPAIR_TYPE_06.toString())){//特殊维修
				act.setForword(SERVICE_ORDER_SPECIAL_ADD_URL);
			}else if(repairType.equals(Constant.REPAIR_TYPE_08.toString())){//PDI
				act.setForword(SERVICE_ORDER_PDI_ADD_URL);
			}else if(repairType.equals(Constant.REPAIR_TYPE_09.toString())){//备件维修
				act.setForword(SERVICE_ORDER_CLAIM_ADD_URL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "问卷调查");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 维修类型弹窗
	 * @Date 2017-08-16
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceRepairTypeWin(){
		try {
			String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));
			act.setOutData("repairType", repairType);
			act.setForword(SERVICE_REPAIR_TYPE_WIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修配件列表");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 根据VIN查询车辆信息
	 * @Date 2017-08-03
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
    public void getVehicleInfo() {
    	try {
	    	ActionContext act = ActionContext.getContext();
	        RequestWrapper request = act.getRequest();
	        String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));//进站里程
	        String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
	        String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));//维修类型
	        String lifeCycle = CommonUtils.checkNull(request.getParamValue("lifeCycle"));//车辆状态
	        String isPdi = CommonUtils.checkNull(request.getParamValue("isPdi"));//是否PDI
	        
	        if(vin.equals("")){
            	act.setOutData("code", "fail");
            	act.setOutData("msg", "VIN码不能为空!");
            	return;
            }
//	        if(repairType.equals("")){
//            	act.setOutData("code", "fail");
//            	act.setOutData("msg", "维修类型不能为空!");
//            	return;
//            }
	        Map<String,Object> params = new HashMap<String, Object>();
	        
	        params.clear();
			params.put("vin", vin);//工单ID
			params.put("lifeCycle", lifeCycle);//车辆状态
			params.put("isPdi", isPdi);//是否PDI
			Map<String,Object> map = dao.getVehicleInfo(params);
            if(map!=null&&map.size()>0){
            	//维修类型是正常维修、外出维修、售前维修、特殊维修、PDI、备件维修
    	        if(repairType.equals(Constant.REPAIR_TYPE_01.toString())||repairType.equals(Constant.REPAIR_TYPE_02.toString())||
    	           repairType.equals(Constant.REPAIR_TYPE_03.toString())||repairType.equals(Constant.REPAIR_TYPE_06.toString())||
    	           repairType.equals(Constant.REPAIR_TYPE_08.toString())||repairType.equals(Constant.REPAIR_TYPE_09.toString())
    	        ){
    	        	//判断是否含有技术升级和送检测类服务活动工单
    	        	params.clear();
    	        	params.put("vin", vin);//vin
    	        	params.put("mileage", mileage);//进站里程
    	        	params.put("purchasedDate", CommonUtils.checkNull(map.get("PURCHASED_DATE")));//购车时间
    	        	params.put("wrgroupId", CommonUtils.checkNull(map.get("WRGROUP_ID")));//车型组
    	        	params.put("activityType", Constant.SERVICEACTIVITY_TYPE_NEW_01+","+Constant.SERVICEACTIVITY_TYPE_NEW_03);//活动类型 技术升级和送检测
    	        	
    	        	Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
    				params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
    				params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
    				
    	        	PageResult<Map<String,Object>> ps = dao.serviceActivityQuery(params,Constant.PAGE_SIZE,curPage);
    	        	if(ps!=null&&ps.getTotalRecords()>0){
    	        		act.setOutData("code", "fail");
    	            	act.setOutData("msg", "该车辆有未完成的服务活动，点击<a href=\"#\" style=\"font-size:14px;font-weight:bold;color:#FF0000;cursor:hand;\" onclick=\"setServiceRepairType("+Constant.REPAIR_TYPE_05+")\">活动服务</a>跳转!");
    	            	return;
    	        	}
    	        }
    	        //维修类型是保养
    	        if(repairType.equals(Constant.REPAIR_TYPE_04.toString())){
    	        	//判断首保信息是否异常
    	        	params.clear();
    	        	params.put("vin", vin);//工单ID
    	        	Map<String,Object> validateMaintainInfo = dao.getValidateMaintainInfo(params);
    	        	if(validateMaintainInfo!=null&&validateMaintainInfo.get("VIN")!=null){
    	        		act.setOutData("code", "fail");
    	            	act.setOutData("msg", "该车辆首保信息异常,请联系车厂!");
    	            	return;
    	        	}
    	        }
            	act.setOutData("code", "succ");
            	act.setOutData("msg", "查询成功!");
            }else{
            	act.setOutData("code", "fail");
            	act.setOutData("msg", "无此VIN码车辆信息!");
            	return;
            }
            act.setOutData("map", map);
        } catch (Exception e) {//异常方法
        	e.printStackTrace();
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取车辆信息失败");
            act.setOutData("code", "fail");
            act.setOutData("msg", "获取车辆信息失败:"+e1);
//            act.setException(e1);
        }
    }
    
    /**
	 * @description 服务活动弹窗
	 * @Date 2017-08-24
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceActivityWin(){
		try {
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
			String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));//进站里程
			String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate"));//购车时间
			String wrgroupId = CommonUtils.checkNull(request.getParamValue("wrgroupId"));//车型组ID
			String activityType = CommonUtils.checkNull(request.getParamValue("activityType"));//活动类型
			
			act.setOutData("vin", vin);
			act.setOutData("mileage", mileage);
			act.setOutData("purchasedDate", purchasedDate);
			act.setOutData("wrgroupId", wrgroupId);
			act.setOutData("activityType", activityType);
			
			act.setForword(SERVICE_ACTIVITY_WIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动列表");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
    
	/**
	 * @description 服务活动查询
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceActivityQuery(){
		try {
			//页面参数
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
			String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));//进站里程
			String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate"));//购车时间
			String wrgroupId = CommonUtils.checkNull(request.getParamValue("wrgroupId"));//车型组ID
			
			String activityCode = CommonUtils.checkNull(request.getParamValue("activityCode"));//活动编码
			String activityName = CommonUtils.checkNull(request.getParamValue("activityName"));//活动名称
			String activityType = CommonUtils.checkNull(request.getParamValue("activityType"));//活动类型
			
			Map<String,Object>params = new HashMap<String, Object>();
			
			params.put("vin", vin);
			params.put("mileage", mileage);
			params.put("purchasedDate", purchasedDate);
			params.put("wrgroupId", wrgroupId);
			
			params.put("activityCode", activityCode);
			params.put("activityName", activityName);
			params.put("activityType", activityType);
			
			params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>> ps = dao.serviceActivityQuery(params,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修项目查询");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
    /**
	 * @description 维修项目弹窗
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceProjectWin(){
		try {
//			String pos = CommonUtils.checkNull(request.getParamValue("pos"));
			String wrgroupId = CommonUtils.checkNull(request.getParamValue("wrgroupId"));
			
//			act.setOutData("pos", pos);
			act.setOutData("wrgroupId", wrgroupId);
			act.setForword(SERVICE_PROJECT_WIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修项目列表");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 维修项目查询
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceProjectQuery(){
		try {
			//页面参数
			String wrgroupId = CommonUtils.checkNull(request.getParamValue("wrgroupId"));//车型组ID
			String labourCode = CommonUtils.checkNull(request.getParamValue("labourCode"));//工时代码
			String cnDes = CommonUtils.checkNull(request.getParamValue("cnDes"));//工时名称
			
			Map<String,Object>params = new HashMap<String, Object>();
			
			params.put("wrgroupId", wrgroupId);
			params.put("labourCode", labourCode);
			params.put("cnDes", cnDes);
			
			params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>>ps = dao.serviceProjectQuery(params,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修项目查询");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 服务活动维修项目查询
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceActivityProjectQuery(){
		try {
			//页面参数
			String activityId = CommonUtils.checkNull(request.getParamValue("activityId"));//服务活动ID
			String wrgroupId = CommonUtils.checkNull(request.getParamValue("wrgroupId"));//车型组ID
			
			Map<String,Object>params = new HashMap<String, Object>();
			
			params.put("activityId", activityId);
			params.put("wrgroupId", wrgroupId);
			
			params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
			
			List<Map<String, Object>> serviceActivityProjectList = dao.serviceActivityProjectQuery(params);
			act.setOutData("serviceActivityProjectList", serviceActivityProjectList);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修项目查询");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 维修配件弹窗
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void servicePartWin(){
		try {
			String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));//维修类型
			String ruleId = CommonUtils.checkNull(request.getParamValue("ruleId"));//三包规则ID
//			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			String arrivalDate = CommonUtils.checkNull(request.getParamValue("arrivalDate"));//进站时间
			String repairDateBegin = CommonUtils.checkNull(request.getParamValue("repairDateBegin"));//维修开始时间
			String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));//进站里程数
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
			String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate"));//购车时间
			String partFareRate = CommonUtils.checkNull(request.getParamValue("partFareRate"));//经销商加价率
			String _isThreeGuarantee = CommonUtils.checkNull(request.getParamValue("_isThreeGuarantee"));//是否三包
			
			act.setOutData("repairType", repairType);
			act.setOutData("ruleId", ruleId);
//			act.setOutData("modelId", modelId);
			act.setOutData("arrivalDate", arrivalDate);
			act.setOutData("repairDateBegin", repairDateBegin);
			act.setOutData("mileage", mileage);
			act.setOutData("vin", vin);
			act.setOutData("purchasedDate", purchasedDate);
			act.setOutData("partFareRate", partFareRate);
			act.setOutData("_isThreeGuarantee", _isThreeGuarantee);
			
			act.setForword(SERVICE_PART_WIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修配件列表");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 维修配件查询
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void servicePartQuery(){
		try {
			//页面参数
			String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));//维修类型
			String ruleId = CommonUtils.checkNull(request.getParamValue("ruleId"));//三包规则ID
//			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));//车型ID
			String arrivalDate = CommonUtils.checkNull(request.getParamValue("arrivalDate"));//进站时间
			String repairDateBegin = CommonUtils.checkNull(request.getParamValue("repairDateBegin"));//维修开始时间
			String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));//进站里程数
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
			String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate"));//购车时间
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));//配件代码
			String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));//配件名称
			String partFareRate = CommonUtils.checkNull(request.getParamValue("partFareRate"));//经销商加价率
			String _isThreeGuarantee = CommonUtils.checkNull(request.getParamValue("_isThreeGuarantee"));//是否三包
			
			Map<String,Object>params = new HashMap<String, Object>();
			
			params.put("repairType", repairType);
			params.put("ruleId", ruleId);
//			params.put("modelId", modelId);
			params.put("arrivalDate", arrivalDate);
			params.put("repairDateBegin", repairDateBegin);
			params.put("mileage", mileage);
			params.put("vin", vin);
			params.put("purchasedDate", purchasedDate);
			params.put("partCode", partCode);
			params.put("partCname", partCname);
			params.put("partFareRate", partFareRate);
			params.put("_isThreeGuarantee", _isThreeGuarantee);
			
			params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>>ps = dao.servicePartQuery(params,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修配件查询");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
    
	/**
	 * @description 服务活动维修配件查询
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceActivityPartQuery(){
		try {
			//页面参数
			String activityId = CommonUtils.checkNull(request.getParamValue("activityId"));//服务活动ID
			String wrgroupId = CommonUtils.checkNull(request.getParamValue("wrgroupId"));//车型组ID
			
			String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));//维修类型
			String ruleId = CommonUtils.checkNull(request.getParamValue("ruleId"));//三包规则ID
//			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));//车型ID
			String arrivalDate = CommonUtils.checkNull(request.getParamValue("arrivalDate"));//进站时间
			String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));//进站里程数
//			String repairDateBegin = CommonUtils.checkNull(request.getParamValue("repairDateBegin"));//维修开始时间
//			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
			String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate"));//购车时间
//			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));//配件代码
//			String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));//配件名称
			String partFareRate = CommonUtils.checkNull(request.getParamValue("partFareRate"));//经销商加价率
//			String _isThreeGuarantee = CommonUtils.checkNull(request.getParamValue("_isThreeGuarantee"));//是否三包
			
			Map<String,Object> params = new HashMap<String, Object>();
			
			params.put("activityId", activityId);
			params.put("wrgroupId", wrgroupId);
			
			params.put("repairType", repairType);
			params.put("ruleId", ruleId);
//			params.put("modelId", modelId);
			params.put("arrivalDate", arrivalDate);
			params.put("mileage", mileage);
//			params.put("repairDateBegin", repairDateBegin);
//			params.put("vin", vin);
			params.put("purchasedDate", purchasedDate);
//			params.put("partCode", partCode);
//			params.put("partCname", partCname);
			params.put("partFareRate", partFareRate);
//			params.put("_isThreeGuarantee", _isThreeGuarantee);
			
			params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
			
			List<Map<String, Object>> serviceActivityPartList = dao.serviceActivityPartQuery(params);
			act.setOutData("serviceActivityPartList", serviceActivityPartList);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修配件查询");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 根据车型查询失效模式
	 * @Date 2017-08-07
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
    public void getFailureMode() {
    	try {
	    	ActionContext act = ActionContext.getContext();
	        RequestWrapper request = act.getRequest();
	        
	        String pos = CommonUtils.checkNull(request.getParamValue("pos"));
	        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
            
	        if(partCode.equals("")){
            	act.setOutData("code", "fail");
            	act.setOutData("msg", "失效模式加载异常,配件信息不能为空!");
            	return;
            }
	        
	        Map<String,Object> params = new HashMap<String, Object>();
			params.put("partCode", partCode);//工单ID
//			params.put("lifeCycle", 10321004);
			List<Map<String,Object>> list = dao.getFailureMode(params);
			
			act.setOutData("code", "succ");
        	act.setOutData("msg", "查询成功!");
        	act.setOutData("pos", pos);
            act.setOutData("failureModeList", list);
        } catch (Exception e) {//异常方法
        	e.printStackTrace();
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取车辆信息失败");
            act.setOutData("code", "fail");
            act.setOutData("msg", "获取配件失效模式信息失败:"+e1);
//            act.setException(e1);
        }
    }
    
    /**
	 * @description 外出项目弹窗
	 * @Date 2017-08-17
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOutProjectWin(){
		try {
//			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			
//			act.setOutData("modelId", modelId);
			act.setForword(SERVICE_OUT_PROJECT_WIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修配件列表");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 外出项目查询
	 * @Date 2017-08-17
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOutProjectQuery(){
		try {
			//页面参数
			String feeCode = CommonUtils.checkNull(request.getParamValue("feeCode"));//外出项目代码
			String feeCname = CommonUtils.checkNull(request.getParamValue("feeCname"));//外出项目名称
			
			Map<String,Object>params = new HashMap<String, Object>();
			
//			params.put("modelId", modelId);
			params.put("feeCode", feeCode);
			params.put("feeCname", feeCname);
			
			params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>>ps = dao.serviceOutProjectQuery(params,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修配件查询");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
    
	/**
	 * @description 外出维修弹窗
	 * @Date 2017-08-17
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOutRepairWin(){
		try {
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			
			act.setOutData("vin", vin);
			act.setForword(SERVICE_OUT_REPAIR_WIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修配件列表");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 外出项目查询
	 * @Date 2017-08-17
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOutRepairQuery(){
		try {
			//页面参数
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//车架号
			String egressNo = CommonUtils.checkNull(request.getParamValue("egressNo"));//申请单号
			String eLicenseNo = CommonUtils.checkNull(request.getParamValue("eLicenseNo"));//派车车牌号
//			String eStartDate = CommonUtils.checkNull(request.getParamValue("eStartDate"));//救援开始时间
//			String eEndDate = CommonUtils.checkNull(request.getParamValue("eEndDate"));//救援结束时间
			
			Map<String,Object>params = new HashMap<String, Object>();
			
			params.put("vin", vin);
			params.put("egressNo", egressNo);
			params.put("eLicenseNo", eLicenseNo);
//			params.put("eStartDate", eStartDate);
//			params.put("eEndDate", eEndDate);
			
			params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>>ps = dao.serviceOutRepairQuery(params,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修配件查询");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 根据外出维修ID查询外出维修信息
	 * @Date 2017-08-18
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
    public void getServiceOutRepairInfo() {
    	try {
	    	ActionContext act = ActionContext.getContext();
	        RequestWrapper request = act.getRequest();
	        String egressId = CommonUtils.checkNull(request.getParamValue("egressId"));
            
	        if(egressId.equals("")){
            	act.setOutData("code", "fail");
            	act.setOutData("msg", "外出维修ID不能为空!");
            	return;
            }
	        
	        Map<String,Object> params = new HashMap<String, Object>();
			params.put("egressId", egressId);//外出维修ID
			Map<String,Object> map = dao.getServiceOutRepairInfo(params);
            if(map!=null&&map.size()>0){
            	act.setOutData("code", "succ");
            	act.setOutData("msg", "查询成功!");
            }else{
            	act.setOutData("code", "fail");
            	act.setOutData("msg", "无此外出维修单信息!");
            }
            act.setOutData("map", map);
        } catch (Exception e) {//异常方法
        	e.printStackTrace();
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取外出维修单信息失败");
            act.setOutData("code", "fail");
            act.setOutData("msg", "获取外出维修单信息失败:"+e1);
//            act.setException(e1);
        }
    }
	
    /**
	 * @description 售后服务工单保存
	 * @Date 2017-08-09
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOrderSave() {
		try {
			//基本信息
			String serviceOrderId = SequenceManager.getSequence("");//售后服务工单ID
			String serviceOrderCode = OrderCodeManager.getOrderCode(92291065);//售后服务工单编码
			String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));//维修类型
			String arrivalDate = CommonUtils.checkNull(request.getParamValue("arrivalDate"));//进站时间
			String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate"));//购车时间
			String purchasedDays = "0";
			if(!purchasedDate.equals("")&&!arrivalDate.equals("")){
				purchasedDays = getDaysBetween(purchasedDate+" 00:00",arrivalDate);//购车天数
			}
			String purchasedMonths = "0";
			if(!purchasedDate.equals("")&&!arrivalDate.equals("")){
				purchasedMonths = getMonthsBetween(purchasedDate,arrivalDate);//购车月份
			}
			String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));//进站里程
			String repairDateBegin = CommonUtils.checkNull(request.getParamValue("repairDateBegin"));//维修开始时间
			String repairDateEnd = CommonUtils.checkNull(request.getParamValue("repairDateEnd"));//维修结束时间
			String repairDays = "0";//维修天数
			if(!repairDateBegin.equals("")&&!repairDateEnd.equals("")){
				repairDays = getDaysBetween(repairDateBegin,repairDateEnd);//维修天数
			}
			String curFreeTimes = CommonUtils.checkNull(request.getParamValue("curFreeTimes"));//当前维修次数
			String receptionistMan = CommonUtils.checkNull(request.getParamValue("receptionistMan"));//接待员
			//车辆信息
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//VIN
			String engineNo = CommonUtils.checkNull(request.getParamValue("engineNo"));//发动机号
			String licenseNo = CommonUtils.checkNull(request.getParamValue("licenseNo"));//牌照号
			//送修人信息
			String delivererManName = CommonUtils.checkNull(request.getParamValue("delivererManName"));//送修人姓名
			String delivererManPhone = CommonUtils.checkNull(request.getParamValue("delivererManPhone"));//送修人电话
			//服务活动ID 维修类型：服务活动
			String activityId = CommonUtils.checkNull(request.getParamValue("activityId"));//外出维修ID
			//维修项目
			String[] labourIds = checkNullArray(request.getParamValues("labourId"));//工时ID
			String[] labourCodes = checkNullArray(request.getParamValues("labourCode"));//工时代码
			String[] cnDess = checkNullArray(request.getParamValues("cnDes"));//工时名称
			String[] labourHours = checkNullArray(request.getParamValues("labourHour"));//工时定额
			String[] labourPrices = checkNullArray(request.getParamValues("labourPrice"));//工时单价
			String[] labourPaymentMethods = checkNullArray(request.getParamValues("labourPaymentMethod"));//工时付费方式
			//维修配件
			String[] isThreeGuarantees = checkNullArray(request.getParamValues("isThreeGuarantee"));//是否三包
			String[] partIds = checkNullArray(request.getParamValues("partId"));//新件ID
			String[] partCodes = checkNullArray(request.getParamValues("partCode"));//新件代码
			String[] partCnames = checkNullArray(request.getParamValues("partCname"));//新件名称
			String[] partWarTypes = checkNullArray(request.getParamValues("partWarType"));//新件三包类型
			String[] partNums = checkNullArray(request.getParamValues("partNum"));//新件数量
			String[] salePrice1s = checkNullArray(request.getParamValues("salePrice1"));//配件实际调拨价
			String partFareRate = CommonUtils.checkNull(request.getParamValue("partFareRate"));//经销商配件加价率
			String[] partPrices = checkNullArray(request.getParamValues("partPrice"));//单价
			String[] failureModeCodes = checkNullArray(request.getParamValues("failureModeCode"));//失效模式
			String[] partPaymentMethods = checkNullArray(request.getParamValues("partPaymentMethod"));//配件付费方式
			String[] partUseTypes = checkNullArray(request.getParamValues("partUseType"));//配件使用类型
			String[] isMainParts = checkNullArray(request.getParamValues("isMainPart"));//是否主因件
			String[] relationMainParts = checkNullArray(request.getParamValues("relationMainPart"));//关联主因件
			String[] relationLabours = checkNullArray(request.getParamValues("relationLabour"));//关联工时
			//外出维修ID   维修类型：外出维修
			String egressId = CommonUtils.checkNull(request.getParamValue("egressId"));//外出维修ID
			//外出项目   维修类型：外出维修
			String[] feeIds = checkNullArray(request.getParamValues("feeId"));//项目ID
			String[] feeCodes = checkNullArray(request.getParamValues("feeCode"));//项目代码
			String[] feeNames = checkNullArray(request.getParamValues("feeName"));//项目名称
			String[] feePrices = checkNullArray(request.getParamValues("feePrice"));//金额(元)
			String[] feeRemarks = checkNullArray(request.getParamValues("feeRemark"));//备注
			String[] feePaymentMethods = checkNullArray(request.getParamValues("feePaymentMethod"));//付费方式
			String[] feeRelationMainParts = checkNullArray(request.getParamValues("feeRelationMainPart"));//关联主因件
			//申请内容
			String faultDesc = CommonUtils.checkNull(request.getParamValue("faultDesc"));//故障描述
			String faultReason = CommonUtils.checkNull(request.getParamValue("faultReason"));//故障原因
			String repairMethod = CommonUtils.checkNull(request.getParamValue("repairMethod"));//维修措施
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));//申请备注
			//PDI信息
			String pdiRemark = CommonUtils.checkNull(request.getParamValue("pdiRemark"));//PDI备注
			//申请费用
			String applyLabourHour = CommonUtils.checkNull(request.getParamValue("applyLabourHour"));//工时数量
			String applyLabourPrice = CommonUtils.checkNull(request.getParamValue("applyLabourPrice"));//工时金额
			String applyPartNum = CommonUtils.checkNull(request.getParamValue("applyPartNum"));//配件数量
			String applyPartPrice = CommonUtils.checkNull(request.getParamValue("applyPartPrice"));//配件金额
			String applyFeePrice = CommonUtils.checkNull(request.getParamValue("applyFeePrice"));//外出金额
			String applyActivityPrice = CommonUtils.checkNull(request.getParamValue("applyActivityPrice"));//服务活动金额
			String applyMaintainPrice = CommonUtils.checkNull(request.getParamValue("applyMaintainPrice"));//首保金额
//			String applyActivityDiscount = CommonUtils.checkNull(request.getParamValue("applyActivityDiscount"));//服务活动折扣率
			String applyPdiPrice = CommonUtils.checkNull(request.getParamValue("applyPdiPrice"));//PDI金额
			String applyPriceTotal = CommonUtils.checkNull(request.getParamValue("applyPriceTotal"));//总金额
			
			String isCanClaim = Constant.IF_TYPE_NO.toString();//是否可以转索赔单
			if(repairType.equals(Constant.REPAIR_TYPE_05.toString())||repairType.equals(Constant.REPAIR_TYPE_08.toString())){//维修类型是服务活动和PDI的直接可以转索赔单
				isCanClaim = Constant.IF_TYPE_YES.toString();
			}else{//维修类型非服务活动和PDI的,需判断含有索赔工时或者索赔配件才能转索赔单
				//工时自费,配件必定自费,如果工时有一个索赔则可以转索赔单
				for(int i=1;i<labourPaymentMethods.length;i++){
					if(labourPaymentMethods[i].equals(Constant.PAY_TYPE_02.toString())){
						isCanClaim = Constant.IF_TYPE_YES.toString();
						break;
					}
				}
			}
			
//			if(isCanClaim.equals(Constant.IF_TYPE_NO.toString())){
//				for(int i=1;i<partPaymentMethods.length;i++){
//					if(partPaymentMethods[i].equals(Constant.PAY_TYPE_02.toString())){
//						isCanClaim = Constant.IF_TYPE_YES.toString();
//						break;
//					}
//				}
//			}
			
			Map<String,Object>params = new HashMap<String, Object>();
			
			//保存售后服务工单记录
			params.put("serviceOrderId", serviceOrderId);
			params.put("serviceOrderCode", serviceOrderCode);
			params.put("dealerId", logonUser.getDealerId());
			params.put("repairType", repairType);
			params.put("arrivalDate", arrivalDate);
			params.put("purchasedDate", purchasedDate);
			params.put("purchasedDays", purchasedDays);
			params.put("purchasedMonths", purchasedMonths);
			params.put("mileage", mileage);
			params.put("repairDateBegin", repairDateBegin);
			params.put("repairDateEnd", repairDateEnd);
			params.put("repairDays", repairDays);
			params.put("curFreeTimes", curFreeTimes);
			params.put("receptionistMan", receptionistMan);
			
			params.put("vin", vin);
//			params.put("engineNo", engineNo);
//			params.put("licenseNo", licenseNo);
			
			params.put("delivererManName", delivererManName);
			params.put("delivererManPhone", delivererManPhone);
			
			params.put("activityId", activityId);
			params.put("egressId", egressId);
			
			params.put("faultDesc", faultDesc);
			params.put("faultReason", faultReason);
			params.put("repairMethod", repairMethod);
			params.put("remark", remark);
			
			params.put("pdiRemark", pdiRemark);
			
			params.put("applyLabourHour", applyLabourHour);
			params.put("applyLabourPrice", applyLabourPrice);
			params.put("applyPartNum", applyPartNum);
			params.put("applyPartPrice", applyPartPrice);
			params.put("applyFeePrice", applyFeePrice);
			params.put("applyActivityPrice", applyActivityPrice);
			params.put("applyMaintainPrice", applyMaintainPrice);
			params.put("applyPdiPrice", applyPdiPrice);
			params.put("applyPriceTotal", applyPriceTotal);
		    
			params.put("isCanClaim", isCanClaim);
			params.put("createBy", logonUser.getUserId());
        	dao.serviceOrderSave(params);
        	//修改车辆信息记录
        	params.clear();
        	params.put("vin", vin);
        	params.put("repairType", repairType);
        	params.put("mileage", mileage);
        	params.put("curFreeTimes", curFreeTimes);
        	params.put("licenseNo", licenseNo);
        	dao.vehicleUpdate(params);
        	//工单类型不同保存不同内容
        	//正常维修、外出维修、售前维修、服务活动、特殊维修、备件维修 保存售后服务工单-维修项目
        	if(repairType.equals(Constant.REPAIR_TYPE_01.toString())||repairType.equals(Constant.REPAIR_TYPE_02.toString())||
        	   repairType.equals(Constant.REPAIR_TYPE_03.toString())||repairType.equals(Constant.REPAIR_TYPE_04.toString())||
        	   repairType.equals(Constant.REPAIR_TYPE_05.toString())||repairType.equals(Constant.REPAIR_TYPE_06.toString())||
        	   repairType.equals(Constant.REPAIR_TYPE_09.toString())
        	){
        		params.clear();
            	params.put("serviceOrderId", serviceOrderId);
            	params.put("labourIds", labourIds);
    			params.put("labourCodes", labourCodes);
    			params.put("cnDess", cnDess);
    			params.put("labourHours", labourHours);
    			params.put("labourPrices", labourPrices);
    			params.put("labourPaymentMethods", labourPaymentMethods);
    			
    			params.put("createBy", logonUser.getUserId());
    			dao.serviceOrderProjectSave(params);
        	}
        	//正常维修、外出维修、售前维修、服务活动、特殊维修、备件维修 保存售后服务工单-维修配件
        	if(repairType.equals(Constant.REPAIR_TYPE_01.toString())||repairType.equals(Constant.REPAIR_TYPE_02.toString())||
        	   repairType.equals(Constant.REPAIR_TYPE_03.toString())||repairType.equals(Constant.REPAIR_TYPE_04.toString())||
        	   repairType.equals(Constant.REPAIR_TYPE_05.toString())||repairType.equals(Constant.REPAIR_TYPE_06.toString())||
        	   repairType.equals(Constant.REPAIR_TYPE_09.toString())
        	){
        		params.clear();
            	params.put("serviceOrderId", serviceOrderId);
            	params.put("isThreeGuarantees", isThreeGuarantees);
    			params.put("partIds", partIds);
    			params.put("partCodes", partCodes);
    			params.put("partCnames", partCnames);
    			params.put("partWarTypes", partWarTypes);
    			params.put("partNums", partNums);
    			params.put("salePrice1s", salePrice1s);
    			params.put("partFareRate", partFareRate);
    			params.put("partPrices", partPrices);
    			params.put("failureModeCodes", failureModeCodes);
    			params.put("partPaymentMethods", partPaymentMethods);
    			params.put("partUseTypes", partUseTypes);
    			params.put("isMainParts", isMainParts);
    			params.put("relationMainParts", relationMainParts);
    			params.put("relationLabours", relationLabours);
    			
    			params.put("createBy", logonUser.getUserId());
    			dao.serviceOrderPartSave(params);	
        	}
        	//外出维修、售前维修 保存售后服务工单-外出项目
			if(repairType.equals(Constant.REPAIR_TYPE_02.toString())||repairType.equals(Constant.REPAIR_TYPE_03.toString())){
				params.clear();
				params.put("serviceOrderId", serviceOrderId);
				params.put("feeIds", feeIds);
				params.put("feeCodes", feeCodes);
				params.put("feeNames", feeNames);
				params.put("feePrices", feePrices);
				params.put("feeRemarks", feeRemarks);
				params.put("feePaymentMethods", feePaymentMethods);
				params.put("feeRelationMainParts", feeRelationMainParts);
				
				params.put("createBy", logonUser.getUserId());
				dao.serviceOrderOutProjectSave(params);
				//修改外出维修申请单是否关联工单为已关联
				params.clear();
				params.put("egressId", egressId);
				params.put("isRlationOrder", Constant.IF_TYPE_YES);
				params.put("updateBy", logonUser.getUserId());
				dao.serviceOrderEgressUpdate(params);
			}
			
        	act.setOutData("code", "succ");
			act.setOutData("msg", "保存成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存售后服务工单信息保存数据异常.");
            act.setException(e1);
        	act.setOutData("code", "fail");
			act.setOutData("msg", "保存失败!"+e1);
			e.printStackTrace();
        }
    }
    
	/**
	 * @description 售后服务工单上报
	 * @Date 2017-08-09
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOrderSubSave() {
		try {
			//基本信息
			String serviceOrderId = CommonUtils.checkNull(request.getParamValue("serviceOrderId"));//售后服务工单ID
			String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));//维修类型
			String isCanClaim = CommonUtils.checkNull(request.getParamValue("isCanClaim"));//是否可以转索赔单
			
			String activityType = CommonUtils.checkNull(request.getParamValue("activityType"));//服务活动类型
			
			Map<String,Object> params = new HashMap<String, Object>();
			
			//当可以转索赔单即有索赔工时或者配件时,才需要预警和预授权
			if(isCanClaim.equals(Constant.IF_TYPE_YES.toString())){
				//正常维修、外出维修、服务活动(技术升级) 保存预警信息
	        	if(repairType.equals(Constant.REPAIR_TYPE_01.toString())||repairType.equals(Constant.REPAIR_TYPE_02.toString())||
	        	   (repairType.equals(Constant.REPAIR_TYPE_05.toString())&&activityType.equals(Constant.SERVICEACTIVITY_TYPE_NEW_01.toString()))
	        	){
					//保存售后服务工单-整车维修天数预警
					params.clear();
					params.put("serviceOrderId", serviceOrderId);
					params.put("createBy", logonUser.getUserId());
					dao.serviceOrderWarnDaySave(params);
					//保存售后服务工单-配件维修次数预警
					params.clear();
					params.put("serviceOrderId", serviceOrderId);
					params.put("createBy", logonUser.getUserId());
					dao.serviceOrderWarnNumSave(params);
				}
	        	//正常维修、外出维修、售前维修、特殊维修、备件维修 保存预授权内容信息
	        	if(repairType.equals(Constant.REPAIR_TYPE_01.toString())||repairType.equals(Constant.REPAIR_TYPE_02.toString())||
	        	   repairType.equals(Constant.REPAIR_TYPE_03.toString())||repairType.equals(Constant.REPAIR_TYPE_06.toString())||
	        	   repairType.equals(Constant.REPAIR_TYPE_09.toString())
	        	){
					//售后服务工单-预授权内容信息保存
					params.clear();
					params.put("serviceOrderId", serviceOrderId);
					params.put("createBy", logonUser.getUserId());
					dao.serviceOrderAuthSave(params);
	        	}
	        	//保养 保存预授权内容信息
	        	if(repairType.equals(Constant.REPAIR_TYPE_04.toString())){
					//售后服务工单-保养预授权内容信息保存,在serviceOrderAuthMaintainSave中判断了是否首保
					params.clear();
					params.put("serviceOrderId", serviceOrderId);
					params.put("createBy", logonUser.getUserId());
					dao.serviceOrderAuthMaintainSave(params);
	        	}
	        	//正常维修、外出维修、售前维修、保养、特殊维修、备件维修 保存预授权审核信息
	        	if(repairType.equals(Constant.REPAIR_TYPE_01.toString())||repairType.equals(Constant.REPAIR_TYPE_02.toString())||
	        	   repairType.equals(Constant.REPAIR_TYPE_03.toString())||repairType.equals(Constant.REPAIR_TYPE_04.toString())||
	        	   repairType.equals(Constant.REPAIR_TYPE_06.toString())||repairType.equals(Constant.REPAIR_TYPE_09.toString())
	        	){
					//售后服务工单-预授权审核信息保存
					params.clear();
					params.put("serviceOrderId", serviceOrderId);
					params.put("createBy", logonUser.getUserId());
					dao.serviceOrderAuthAuditSave(params);
	        	}
			}
			
			//售后服务工单上报
			params.clear();
			params.put("serviceOrderId", serviceOrderId);
			params.put("updateBy", logonUser.getUserId());
			dao.serviceOrderSubSave(params);
			
	    	act.setOutData("code", "succ");
			act.setOutData("msg", "上报成功!");
	    } catch (Exception e) {//异常方法
	        BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "上报售后服务工单失败.");
	        act.setException(e1);
	    	act.setOutData("code", "fail");
			act.setOutData("msg", "上报失败!"+e1);
			e.printStackTrace();
	    }
    }
	
	/**
	 * @description 售后服务工单结算
	 * @Date 2017-08-09
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOrderSettlementSave() {
		try {
			//基本信息
			String serviceOrderId = CommonUtils.checkNull(request.getParamValue("serviceOrderId"));//售后服务工单ID
			
			Map<String,Object>params = new HashMap<String, Object>();
			
			//售后服务工单结算
			params.clear();
			params.put("serviceOrderId", serviceOrderId);
			params.put("updateBy", logonUser.getUserId());
			dao.serviceOrderSettlementSave(params);
			
	    	act.setOutData("code", "succ");
			act.setOutData("msg", "结算成功!");
	    } catch (Exception e) {//异常方法
	        BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "上报售后服务工单失败.");
	        act.setException(e1);
	    	act.setOutData("code", "fail");
			act.setOutData("msg", "结算失败!"+e1);
			e.printStackTrace();
	    }
    }
	
	/**
	 * @description 售后服务工单查看
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOrderShow(){
		try {
			//页面参数
			String operationType = CommonUtils.checkNull(request.getParamValue("operationType"));//操作类型
//			String authAuditId = CommonUtils.checkNull(request.getParamValue("authAuditId"));//预授权审核ID
			String serviceOrderId = CommonUtils.checkNull(request.getParamValue("serviceOrderId"));//售后服务工单ID
			String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));//维修类型
			String partFareRate = "";//经销商加价率
			
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
			
			Map<String,Object> partFareRateMap = dao.getPartFareRate(params);
			if(partFareRate!=null&&!CommonUtils.checkNull(partFareRateMap.get("PART_FARE_RATE")).equals("")){
				partFareRate = CommonUtils.checkNull(partFareRateMap.get("PART_FARE_RATE"));
			}
			
			Map<String, Object> serviceOrderMap = new HashMap<String, Object>();
			List<Map<String, Object>> serviceOrderProjectList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> serviceOrderPartList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> serviceOrderOutProjectList = new ArrayList<Map<String, Object>>();
			
			List<Map<String, Object>> serviceOrderAuthAuditList = new ArrayList<Map<String, Object>>();
			
			params.clear();
			params.put("serviceOrderId", serviceOrderId);
			//获取售后服务工单信息
			serviceOrderMap = dao.getServiceOrderInfo(params);
			//不同的维修类型需要的元素不一样
			//正常维修、外出维修、售前维修、特殊维修、备件维修
			if(repairType.equals(Constant.REPAIR_TYPE_01.toString())||repairType.equals(Constant.REPAIR_TYPE_02.toString())||
			   repairType.equals(Constant.REPAIR_TYPE_03.toString())||repairType.equals(Constant.REPAIR_TYPE_06.toString())||
			   repairType.equals(Constant.REPAIR_TYPE_09.toString())
			){
				//获取售后服务工单维修项目列表
				serviceOrderProjectList = dao.getServiceOrderProjectList(params);
				//获取售后服务工单维修配件列表
				serviceOrderPartList = dao.getServiceOrderPartList(params);
			}
			//外出维修、售前维修
			if(repairType.equals(Constant.REPAIR_TYPE_02.toString())||repairType.equals(Constant.REPAIR_TYPE_03.toString())){
				//获取外出项目
				serviceOrderOutProjectList = dao.getServiceOrderOutProjectList(params);
			}
			//获取售后服务工单预授权授权列表
			ServiceOrderAuthDao serviceOrderAuthDao = new ServiceOrderAuthDao();
			serviceOrderAuthAuditList = serviceOrderAuthDao.getServiceOrderAuthAuditList(params);
//			//获取售后服务工单预授权内容列表
//			List<Map<String, Object>> serviceOrderAuthContentList = dao.getServiceOrderAuthContentList(params);
			
//			act.setOutData("authAuditId", authAuditId);
			act.setOutData("serviceOrderId", serviceOrderId);
			act.setOutData("partFareRate", partFareRate);//经销商加价率
			act.setOutData("serviceOrderMap", serviceOrderMap);
			act.setOutData("serviceOrderProjectList", serviceOrderProjectList);
			act.setOutData("serviceOrderPartList", serviceOrderPartList);
			act.setOutData("serviceOrderOutProjectList", serviceOrderOutProjectList);//外出维修-维修项目
			
			act.setOutData("serviceOrderAuthAuditList", serviceOrderAuthAuditList);
//			act.setOutData("serviceOrderAuthContentList", serviceOrderAuthContentList);
//			act.setOutData("partFareRate", partFareRate);
			if(operationType.equals("1")){//查看
				if(repairType.equals(Constant.REPAIR_TYPE_01.toString())){//正常维修
					act.setForword(SERVICE_ORDER_NORMAL_SHOW_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_02.toString())){//外出维修
					act.setForword(SERVICE_ORDER_OUT_SHOW_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_03.toString())){//售前维修
					act.setForword(SERVICE_ORDER_PRE_SALE_SHOW_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_06.toString())){//特殊维修
					act.setForword(SERVICE_ORDER_SPECIAL_SHOW_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_08.toString())){//PDI
					act.setForword(SERVICE_ORDER_PDI_SHOW_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_09.toString())){//备件维修
					act.setForword(SERVICE_ORDER_CLAIM_SHOW_URL);
				} 
			}else if(operationType.equals("2")){//修改
				if(repairType.equals(Constant.REPAIR_TYPE_01.toString())){//正常维修
					act.setForword(SERVICE_ORDER_NORMAL_UPDATE_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_02.toString())){//外出维修
					act.setForword(SERVICE_ORDER_OUT_UPDATE_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_03.toString())){//售前维修
					act.setForword(SERVICE_ORDER_PRE_SALE_UPDATE_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_06.toString())){//特殊维修
					act.setForword(SERVICE_ORDER_SPECIAL_UPDATE_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_08.toString())){//PDI
					act.setForword(SERVICE_ORDER_PDI_UPDATE_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_09.toString())){//备件维修
					act.setForword(SERVICE_ORDER_CLAIM_UPDATE_URL);
				} 
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "问卷调查");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 公用方法,月份差值向上取整
	 * @Date 2017-08-09
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * @throws ParseException 
	 * */
	public String getMonthsBetween(String earlyDate,String lastDate) throws ParseException {
		String months = "0";
		if(!earlyDate.equals("")&&!lastDate.equals("")){
			if(earlyDate.length()>10) earlyDate = earlyDate.substring(0,10);
			if(lastDate.length()>10) lastDate = lastDate.substring(0,10);
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
	        Calendar calendar = Calendar.getInstance();
	        
	        calendar.setTime(sdf.parse(earlyDate));
	        int earlyYear = calendar.get(Calendar.YEAR);
	        int earlyMonth = calendar.get(Calendar.MONTH);
	        int earlyDay = calendar.get(Calendar.DAY_OF_MONTH);
	        
	        calendar.setTime(sdf.parse(lastDate));
	        int lastYear = calendar.get(Calendar.YEAR);
	        int lastMonth = calendar.get(Calendar.MONTH);
	        int lastDay = calendar.get(Calendar.DAY_OF_MONTH);
	         
	        int diff;
	        if(earlyYear == lastYear) {
	        	diff = lastMonth - earlyMonth;
	        }else{
	        	diff = 12*(lastYear - earlyYear) + lastMonth - earlyMonth;
	        }
	        if(lastDay - earlyDay > 0){
	        	diff++;
	        }
	        System.out.println(diff);
	        months = diff + "";
			
		}
		return months+"";
	}
	/**
	 * @description 公用方法,天数差值向上取整
	 * @Date 2017-08-09
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * @throws ParseException 
	 * */
	public String getDaysBetween(String earlyDate,String lastDate) throws ParseException {
		String days = "0";
		if(!earlyDate.equals("")&&!lastDate.equals("")){
			if(earlyDate.length()>16) earlyDate = earlyDate.substring(0,16);
			if(lastDate.length()>16) lastDate = lastDate.substring(0,16);
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			Calendar calendar = Calendar.getInstance();
	        
	        calendar.setTime(sdf.parse(earlyDate));
	        int earlyHour = calendar.get(Calendar.HOUR_OF_DAY);
	        int earlyMinute = calendar.get(Calendar.MINUTE);
	        
	        calendar.setTime(sdf.parse(lastDate));
	        int lastHour = calendar.get(Calendar.HOUR_OF_DAY);
	        int lastMinute = calendar.get(Calendar.MINUTE);
			
			long diff = (sdf.parse(lastDate).getTime() - sdf.parse(earlyDate).getTime())/(24*60*60*1000);
			if(earlyHour<lastHour){
				diff++;
			}else if(earlyHour==lastHour&&earlyMinute<lastMinute){
				diff++;
			}
	        System.out.println(diff);
	        days = diff + "";
		}
		return days+"";
	}
	/**
	 * @description 公用方法,数组NULL转EMPTY
	 * @Date 2017-08-09
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * @throws ParseException 
	 * */
	public String[] checkNullArray(String[] array) throws ParseException {
		if(array==null){
			array = EMPTY_ARRAY;
		}
		return array;
	}
}
