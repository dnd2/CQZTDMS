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
 * 2017-08-15
 * 售后服务工单审核管理
 * @author free.AI
 *
 */
public class ServiceOrderAuthAction extends BaseAction{
	  private Logger logger = Logger.getLogger(ServiceOrderAuthAction.class);
	  private ActionContext act = ActionContext.getContext();
	  RequestWrapper request = act.getRequest();
	  AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	  private ServiceOrderAuthDao dao = new ServiceOrderAuthDao();
	  
	  private static final String SERVICE_ORDER_AUTH_URL = "/jsp/afterSales/serviceOrderAuth/serviceOrderAuthList.jsp"; //售后服务工单预授权审核列表
	  private static final String SERVICE_ORDER_AUTH_NORMAL_AUDIT_URL = "/jsp/afterSales/serviceOrderAuth/serviceOrderAuthNormalAudit.jsp"; //正常维修
	  private static final String SERVICE_ORDER_AUTH_OUT_AUDIT_URL = "/jsp/afterSales/serviceOrderAuth/serviceOrderAuthOutAudit.jsp"; //外出维修
	  private static final String SERVICE_ORDER_AUTH_PRE_SALE_AUDIT_URL = "/jsp/afterSales/serviceOrderAuth/serviceOrderAuthPreSaleAudit.jsp"; //售前维修
	  private static final String SERVICE_ORDER_AUTH_MAINTAIN_AUDIT_URL = "/jsp/afterSales/serviceOrderAuth/serviceOrderAuthMaintainAudit.jsp"; //保养
	  private static final String SERVICE_ORDER_AUTH_SPECIAL_AUDIT_URL = "/jsp/afterSales/serviceOrderAuth/serviceOrderAuthSpecialAudit.jsp"; //特殊维修
	  private static final String SERVICE_ORDER_AUTH_PDI_AUDIT_URL = "/jsp/afterSales/serviceOrderAuth/serviceOrderAuthPdiAudit.jsp"; //PDI
	  private static final String SERVICE_ORDER_AUTH_CLAIM_AUDIT_URL = "/jsp/afterSales/serviceOrderAuth/serviceOrderAuthClaimAudit.jsp"; //配件维修
	  
	  private static final String SERVICE_ORDER_AUTH_NORMAL_SHOW_URL = "/jsp/afterSales/serviceOrder/serviceOrderNormalShow.jsp"; //与维修工单查看共用同一个JSP页面
	  private static final String SERVICE_ORDER_AUTH_OUT_SHOW_URL = "/jsp/afterSales/serviceOrderAuth/serviceOrderAuthOutShow.jsp"; //售后服务工单外出维修预授权审核-查看
	  private static final String SERVICE_ORDER_AUTH_PRE_SALE_SHOW_URL = "/jsp/afterSales/serviceOrderAuth/serviceOrderAuthPreSaleShow.jsp"; //售后服务工单售前维修预授权审核-查看
	  private static final String SERVICE_ORDER_AUTH_SPECIAL_SHOW_URL = "/jsp/afterSales/serviceOrderAuth/serviceOrderAuthSpecialShow.jsp"; //售后服务工单特殊维修预授权审核-查看
	  private static final String SERVICE_ORDER_AUTH_PDI_SHOW_URL = "/jsp/afterSales/serviceOrderAuth/serviceOrderAuthPdiShow.jsp"; //售后服务工单PDI预授权审核-查看
	  private static final String SERVICE_ORDER_AUTH_CLAIM_SHOW_URL = "/jsp/afterSales/serviceOrderAuth/serviceOrderAuthClaimShow.jsp"; //售后服务工单配件维修预授权审核-查看
	  
    /**
	 * @description 售后服务工单授权审核列表
	 * @Date 2017-08-15
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOrderAuthList(){
		try {
			act.setForword(SERVICE_ORDER_AUTH_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后服务工单列表");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 售后服务工单授权审核查询
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOrderAuthQuery(){
		try {
			//页面参数
			String serviceOrderCode = CommonUtils.checkNull(request.getParamValue("serviceOrderCode"));//工单号
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
			String licenseNo = CommonUtils.checkNull(request.getParamValue("licenseNo"));//车牌号
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));//经销商ID
			String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));//维修类型
			String authAuditStatus = CommonUtils.checkNull(request.getParamValue("authAuditStatus"));//预授权审核状态
			
			Map<String,Object>params = new HashMap<String, Object>();
			
			params.put("serviceOrderCode", serviceOrderCode);
			params.put("vin", vin);
			params.put("licenseNo", licenseNo);
			params.put("dealerId", dealerId);
			params.put("repairType", repairType);
			params.put("authAuditStatus", authAuditStatus);
			
			params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>> ps = dao.serviceOrderAuthQuery(params,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后服务工单查询");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 售后服务工单预授权审核
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOrderAuthShow(){
		try {
			//页面参数
			String operationType = CommonUtils.checkNull(request.getParamValue("operationType"));//操作类型
			String authAuditId = CommonUtils.checkNull(request.getParamValue("authAuditId"));//预授权审核ID
			String serviceOrderId = CommonUtils.checkNull(request.getParamValue("serviceOrderId"));//售后服务工单ID
			String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));//维修类型
			
			Map<String, Object> serviceOrderMap = new HashMap<String, Object>();
			List<Map<String, Object>> serviceOrderProjectList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> serviceOrderPartList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> serviceOrderOutProjectList = new ArrayList<Map<String, Object>>();
			
			List<Map<String, Object>> serviceOrderAuthAuditList = new ArrayList<Map<String, Object>>();
			
			ServiceOrderDao serviceOrderDao = new ServiceOrderDao();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("serviceOrderId", serviceOrderId);
			//获取售后服务工单信息
			serviceOrderMap = serviceOrderDao.getServiceOrderInfo(params);
			//不同的维修类型需要的元素不一样
			//正常维修、外出维修、售前维修、保养、特殊维修、备件维修
			if(repairType.equals(Constant.REPAIR_TYPE_01.toString())||repairType.equals(Constant.REPAIR_TYPE_02.toString())||
			   repairType.equals(Constant.REPAIR_TYPE_03.toString())||repairType.equals(Constant.REPAIR_TYPE_04.toString())||
			   repairType.equals(Constant.REPAIR_TYPE_06.toString())||repairType.equals(Constant.REPAIR_TYPE_09.toString())
			){
				//获取售后服务工单维修项目列表
				serviceOrderProjectList = serviceOrderDao.getServiceOrderProjectList(params);
				//获取售后服务工单维修配件列表
				serviceOrderPartList = serviceOrderDao.getServiceOrderPartList(params);
			}
			//外出维修、售前维修
			if(repairType.equals(Constant.REPAIR_TYPE_02.toString())||repairType.equals(Constant.REPAIR_TYPE_03.toString())){
				//获取外出项目
				serviceOrderOutProjectList = serviceOrderDao.getServiceOrderOutProjectList(params);
			}
			//获取售后服务工单预授权授权列表
			serviceOrderAuthAuditList = dao.getServiceOrderAuthAuditList(params);
//			//获取售后服务工单预授权内容列表
//			List<Map<String, Object>> serviceOrderAuthContentList = dao.getServiceOrderAuthContentList(params);
			
			act.setOutData("authAuditId", authAuditId);
			act.setOutData("serviceOrderId", serviceOrderId);
			act.setOutData("serviceOrderMap", serviceOrderMap);
			act.setOutData("serviceOrderProjectList", serviceOrderProjectList);
			act.setOutData("serviceOrderPartList", serviceOrderPartList);
			act.setOutData("serviceOrderOutProjectList", serviceOrderOutProjectList);//外出维修-维修项目
			
			act.setOutData("serviceOrderAuthAuditList", serviceOrderAuthAuditList);
//			act.setOutData("serviceOrderAuthContentList", serviceOrderAuthContentList);
//			act.setOutData("partFareRate", partFareRate);
			if(operationType.equals("1")){//查看
				if(repairType.equals(Constant.REPAIR_TYPE_01.toString())){//正常维修
					act.setForword(SERVICE_ORDER_AUTH_NORMAL_SHOW_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_02.toString())){//外出维修
					act.setForword(SERVICE_ORDER_AUTH_OUT_SHOW_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_03.toString())){//售前维修
					act.setForword(SERVICE_ORDER_AUTH_PRE_SALE_SHOW_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_06.toString())){//特殊维修
					act.setForword(SERVICE_ORDER_AUTH_SPECIAL_SHOW_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_08.toString())){//PDI
					act.setForword(SERVICE_ORDER_AUTH_PDI_SHOW_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_09.toString())){//备件维修
					act.setForword(SERVICE_ORDER_AUTH_CLAIM_SHOW_URL);
				} 
			}else if(operationType.equals("2")){//审核
				if(repairType.equals(Constant.REPAIR_TYPE_01.toString())){//正常维修
					act.setForword(SERVICE_ORDER_AUTH_NORMAL_AUDIT_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_02.toString())){//外出维修
					act.setForword(SERVICE_ORDER_AUTH_OUT_AUDIT_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_03.toString())){//售前维修
					act.setForword(SERVICE_ORDER_AUTH_PRE_SALE_AUDIT_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_04.toString())){//保养
					act.setForword(SERVICE_ORDER_AUTH_MAINTAIN_AUDIT_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_06.toString())){//特殊维修
					act.setForword(SERVICE_ORDER_AUTH_SPECIAL_AUDIT_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_08.toString())){//PDI
					act.setForword(SERVICE_ORDER_AUTH_PDI_AUDIT_URL);
				}else if(repairType.equals(Constant.REPAIR_TYPE_09.toString())){//备件维修
					act.setForword(SERVICE_ORDER_AUTH_CLAIM_AUDIT_URL);
				} 
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "问卷调查");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 售后服务工单授权内容查询
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOrderAuthContentQuery(){
		try {
			//页面参数
			String serviceOrderId = CommonUtils.checkNull(request.getParamValue("serviceOrderId"));//售后服务工单ID
			
			Map<String,Object>params = new HashMap<String, Object>();
			
			params.put("serviceOrderId", serviceOrderId);
			params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>> ps = dao.serviceOrderAuthContentQuery(params,5,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后服务工单查询");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @description 售后服务工单预授权审核-保存
	 * @Date 2017-08-16
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOrderAuthAuditSave() {
		try {
			//页面参数
			String authAuditId = CommonUtils.checkNull(request.getParamValue("authAuditId"));//预授权审核ID
			String serviceOrderId = CommonUtils.checkNull(request.getParamValue("serviceOrderId"));//售后服务工单ID
			String authAuditStatus = CommonUtils.checkNull(request.getParamValue("authAuditStatus"));//预授权审核状态
			String authAuditRemark = CommonUtils.checkNull(request.getParamValue("authAuditRemark"));//预授权审核备注
			
			String repairType = CommonUtils.checkNull(request.getParamValue("repairType"));//维修类型
			String egressId = CommonUtils.checkNull(request.getParamValue("egressId"));//外出维修单号
			String agreeMaintainCost = CommonUtils.checkNull(request.getParamValue("agreeMaintainCost"));//是否同意首保金额
			
			if(agreeMaintainCost.equals("")) agreeMaintainCost = Constant.AGREE_MAINTAIN_COST_02.toString();
			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//VIN
			
			Map<String,Object>params = new HashMap<String, Object>();
			
			//保存售后服务工单-预授权信息
			params.clear();
			params.put("authAuditId", authAuditId);
			params.put("serviceOrderId", serviceOrderId);
			params.put("authAuditStatus", authAuditStatus);
			params.put("authAuditRemark", authAuditRemark);
			params.put("authAuditBy", logonUser.getUserId());
			
			params.put("repairType", repairType);
			params.put("egressId", egressId);
			params.put("agreeMaintainCost", agreeMaintainCost);
			params.put("vin", vin);
			
			dao.serviceOrderAuthAuditSave(params);
			
	    	act.setOutData("code", "succ");
			act.setOutData("msg", "审核成功!");
	    } catch (Exception e) {//异常方法
	        BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "审核售后服务工单失败.");
	        act.setException(e1);
	    	act.setOutData("code", "fail");
			act.setOutData("msg", "审核失败!"+e1);
			e.printStackTrace();
	    }
    }
	
	
}
