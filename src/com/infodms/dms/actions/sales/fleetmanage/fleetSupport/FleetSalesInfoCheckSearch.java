package com.infodms.dms.actions.sales.fleetmanage.fleetSupport;

import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CompanyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.fleetmanage.fleetsupport.FleetSupportDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 集团客户实销信息审核查询Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-28
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class FleetSalesInfoCheckSearch {

	public Logger logger = Logger.getLogger(FleetSalesInfoCheckSearch.class);   
	FleetSupportDao dao  = FleetSupportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSalesInfoCheckSearch.jsp";
	
	/**
	 * 集团客户实销信息审核查询页面初始化
	 */
	public void fleetSalesInfoCheckSearchInit(){
		
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户实销信息审核查询页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户实销信息审核查询
	 */
	public void fleetSalesInfoCheckSearch(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = request.getParamValue("fleetName");		//客户名称
			String fleetType = request.getParamValue("fleetType");		//客户类型
			String startDate = request.getParamValue("startDate");		//起始时间
			String endDate = request.getParamValue("endDate");			//结束时间
			String groupCode = request.getParamValue("groupCode");		//物料代码
			String contractNo = request.getParamValue("contractNo");	//合同编号
			String vin = request.getParamValue("vin");					//底盘号
			String checkStatus = request.getParamValue("checkStatus");	//审核状态
			String companyId = logonUser.getCompanyId().toString();		//公司ID
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.fleetSalesInfoCheckSearchList(fleetName, fleetType, startDate, endDate, companyId, groupCode, contractNo, vin,checkStatus, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户实销信息审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
