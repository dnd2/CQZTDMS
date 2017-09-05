package com.infodms.dms.actions.feedbackmng.query;

import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.actions.feedbackmng.apply.StandardVipApplyManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.StandardVipApplyManagerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.StandardVipApplyManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: QueryStandardVipManage.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-22
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class QueryStandardVipManage {
	public Logger logger = Logger.getLogger(StandardVipApplyManager.class);   
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/jsp/feedbackMng/query/queryStanderVip.jsp";
	
	/**
	 * 合格证/VIP卡申请表查询初始化（车厂）
	 */
	public void queryStandardVipInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId()); //取车系
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证/VIP卡申请表初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 合格证/VIP卡申请表查询（车厂）
	 */
	public void queryStandardVip(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		try {
			//CommonUtils.checkNull() 校验是否为空
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));  		//取工单号
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));		   		//VIN
			String stType = CommonUtils.checkNull(request.getParamValue("stType"));	   		//申请的类型， 合格证或VIP
			String stAction = CommonUtils.checkNull(request.getParamValue("stAction"));		//操作类型，补办，修复，更换
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));	//创建开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));		//创建结束时间
			String vehicleModel = CommonUtils.checkNull(request.getParamValue("vehicleSeriesList")); //车系
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));	//取经销商代码
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); //取经销商名称
			String orgId = String.valueOf(logonUser.getOrgId());							//组织ID
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			StandardVipApplyManagerBean smb = new StandardVipApplyManagerBean();
			smb.setOrderId(orderId);
			smb.setVin(vin);
			smb.setStType(stType);
			smb.setStAction(stAction);
			smb.setBeginTime(beginTime);
			smb.setEndTime(endTime);
			smb.setDealerCode(dealerCode);
			smb.setDealerName(dealerName);
			smb.setVehicleModel(vehicleModel);
			smb.setCompanyId(companyId);
			smb.setOrgId(Utility.getDeptOrgId(logonUser).toString());
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = smDao.queryFinalStandardVip(smb,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证/VIP卡申请表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
