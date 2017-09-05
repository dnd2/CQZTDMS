package com.infodms.dms.actions.sales.fleetmanage.fleetSupport;

import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.fleetmanage.fleetsupport.FleetSupportDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 集团客户合同查询Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-28
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class FleetContractsSearch {

	public Logger logger = Logger.getLogger(FleetContractsSearch.class);   
	FleetSupportDao dao  = FleetSupportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetContractsSearch.jsp";
	/**
	 * 集团客户合同查询页面初始化
	 */
	public void fleetContractsSearchInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同查询页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户合同查询
	 */
	public void fleetContractsSearch(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = request.getParamValue("fleetName");	//客户名称
			String fleetType = request.getParamValue("fleetType");	//客户类型
			String startDate = request.getParamValue("startDate");	//起始时间
			String endDate = request.getParamValue("endDate");		//结束时间
			String checkSDate = request.getParamValue("checkSDate");//签订开始时间
			String checkEDate = request.getParamValue("checkEDate");//签订结束时间
			String contractNo = request.getParamValue("contractNo");//合同编号
			String fleetStatus = request.getParamValue("fleetStatus");//合同状态
			String companyId = logonUser.getCompanyId().toString();
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.fleetContractsSearch(fleetStatus, checkSDate, checkEDate, fleetName, fleetType, startDate, endDate, companyId, contractNo,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
