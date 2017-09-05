package com.infodms.dms.actions.feedbackmng.query;

import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ServiceInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.ServiceInfoApproveDao;
import com.infodms.dms.dao.feedbackMng.StandardVipApplyManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: ServiceInfoQueryManager.java
 *
 * @Description:服务资料审批表查询业务逻辑层
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-23
 *
 * @author zouchao 
 * @version 1.0
 * @remark 
 */
public class ServiceInfoQueryManager {
	
	private Logger logger = Logger.getLogger(ServiceInfoQueryManager.class);   
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	// 查询初始化页面（车厂）
	private final String serviceInfoQueryInitUrl = "/jsp/feedbackMng/query/queryServiceInfo.jsp";
	
	private final String serviceInfoQueryForDealerInitUrl = "/jsp/feedbackMng/query/queryServiceInfoForDealer.jsp";
	
	/**
	 * 服务资料审批表查询初始化（车厂）
	 */
	public void queryServiceInfoInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(serviceInfoQueryInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务资料审批表查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 服务资料审批表查询（车厂）
	 */
	public void queryServiceInfo(){
		try {
			//CommonUtils.checkNull() 校验是否为空
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));  		//取工单号
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));	//创建开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));		//创建结束时间
			//modify by xiayanpeng begin 修改JSP经销商树功能，加入经销商名称查询
			//String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));		//取经销商ID
			String dealerName = request.getParamValue("DEALER_NAME");
			String dealerCode = request.getParamValue("dealerCode");
			//modify by xiayanpeng end
			String mailType = CommonUtils.checkNull(request.getParamValue("mailType"));     //邮寄方式
			String status = CommonUtils.checkNull(request.getParamValue("status"));         //工单状态
			String orgId = String.valueOf(logonUser.getOrgId());							//组织ID
			
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			
			ServiceInfoBean bean = new ServiceInfoBean();
			bean.setOrderId(orderId);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			//bean.setDealerId(dealerId);
			bean.setMailType(mailType);
			bean.setOrgId(orgId);
			bean.setStatus(status);
			//moidfy by xiayanpeng begin 
			bean.setDealerCode(dealerCode);
			bean.setDealerName(dealerName);
			bean.setCompanyId(companyId);
			//modify by xiayanpeng end
			
			ServiceInfoApproveDao appDao = new ServiceInfoApproveDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
		    PageResult<Map<String, Object>> ps = appDao.searchAllServiceInfo(logonUser,bean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务资料审批表查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

    /**
     * 服务资料审批表查询（经销商）
     */
	public void queryServiceInfoForDealerInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(serviceInfoQueryForDealerInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务资料审批表查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 服务资料审批表查询（经销商）
	 */
	public void queryServiceInfoForDealer(){
		try {
			//CommonUtils.checkNull() 校验是否为空
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));  		//取工单号
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));	//创建开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));		//创建结束时间
			String mailType = CommonUtils.checkNull(request.getParamValue("mailType"));     //邮寄方式
			String status = CommonUtils.checkNull(request.getParamValue("status"));         //工单状态
			String orgId = String.valueOf(logonUser.getOrgId());							//组织ID
			String dealerId = String.valueOf(logonUser.getDealerId());	                    //取经销商ID
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			ServiceInfoBean bean = new ServiceInfoBean();
			bean.setOrderId(orderId);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setDealerId(dealerId);
			bean.setMailType(mailType);
			bean.setOrgId(orgId);
		    bean.setStatus(status);
			
			ServiceInfoApproveDao appDao = new ServiceInfoApproveDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
					PageResult<Map<String, Object>> ps = appDao.searchAllServiceInfoForDealer(bean,Constant.PAGE_SIZE,curPage);
					//分页方法 end
					act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务资料审批表查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}