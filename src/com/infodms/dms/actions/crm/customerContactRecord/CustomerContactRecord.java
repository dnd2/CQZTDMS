package com.infodms.dms.actions.crm.customerContactRecord;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.customerContactRecord.CustomerContactRecordDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class CustomerContactRecord {
	private Logger logger = Logger.getLogger(CustomerContactRecord.class);
	
	private ActionContext act = ActionContext.getContext();
	
	RequestWrapper request = act.getRequest();
	
	private final CustomerContactRecordDao dao = CustomerContactRecordDao.getInstance();

	private final String RECORD_QUERY_URL = "/jsp/crm/customerContactRecord/customerRecordInit.jsp";// 客户履历查询页面
	

	public void customerInit() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
		
			String funcStr = CommonUtils.judgeUserHasFunc(logonUser);
			String customerId = CommonUtils.checkNull(request.getParamValue("customerId"));
			
			act.setOutData("funcStr", funcStr);
			
			act.setOutData("customerId", customerId);
			
			act.setForword(RECORD_QUERY_URL);
			
		} catch (Exception e) {
			
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_ROUTE_CODE, "客户接触履历查询");
			
			logger.error(logonUser, e1);
			
			act.setException(e1);
			
		}
	}

	/**
	 * FUNCTION : 客户接触履历 履历数据
	 * @param :
	 * @return :
	 * @throws : customerQueryList :
	 */
	public void customerQueryList() {
		
		AclUserBean logonUser = null;
		
		try {
		
			RequestWrapper request = act.getRequest();
			
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String customerId = CommonUtils.checkNull(request.getParamValue("customerId")); // 客户id
			
			Map<String, String> map = new HashMap<String, String>();
			
			map.put("customerId", customerId);
			
			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));
			
			pageSize = pageSize == null || "".equals(pageSize) ? "10" : pageSize;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.getGroupQueryList(map,Integer.parseInt(pageSize), curPage);
			
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_ROUTE_CODE, "客户履历查询");
			
			logger.error(logonUser, e1);
			
			act.setException(e1);
			
		}
	}

	 

}
