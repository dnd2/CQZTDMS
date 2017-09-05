package com.infodms.dms.actions.sales.fleetmanage.fleetInfoManage;

import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.FleetInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetEditLogDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:FleetInfoModifySearchForDealer.java</p>
 *
 * <p>Description: 集团客户报备更改查询(经销商端)</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-21</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class FleetInfoModifySearchForDealer {

	private static Logger logger = Logger.getLogger(FleetInfoModifySearchForDealer.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	// 集团客户报备更改查询页面
	private final String fleetModifySearchForDealerUrl = "/jsp/sales/fleetmanage/fleetinfomanage/fleetModifySearchDLR.jsp";
	
	
	
	/**
	 * 集团客户报备更改查询初始化页面
	 */
	public void searchModifyForDealerInit(){
		try{
			act.setForword(fleetModifySearchForDealerUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"集团客户报备更改查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户报备更改查询
	 */
	public void queryFleetInfoModify(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));  	    //客户名称
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));	    //客户类型
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));       //申请开始日期
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));           //申请结束日期
			String auditStatus = CommonUtils.checkNull(request.getParamValue("auditStatus"));   //审核状态
			String dlrCompanyId = CommonUtils.checkNull(logonUser.getCompanyId());              //经销商公司
			
			
			// 传入查询条件
			FleetInfoBean fibean = new FleetInfoBean();
			fibean.setFleetName(fleetName);
			fibean.setFleetType(fleetType);
			fibean.setBeginTime(beginTime);
			fibean.setEndTime(endTime);
			fibean.setStatus(auditStatus);
			fibean.setDlrCompanyId(dlrCompanyId);
			FleetEditLogDao logdao = new FleetEditLogDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String, Object>> ps = logdao.queryInfoForModify(fibean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);     
			
			act.setForword(fleetModifySearchForDealerUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户报备更改查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
