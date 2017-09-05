package com.infodms.dms.actions.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.DealerShowDao;
import com.infodms.dms.dao.common.OrgShowDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 经销商选择公用模块
 * @author ZhaoLi
 *
 */
public class DealerShow {
	public DealerShowDao dao = DealerShowDao.getInstance();
	public Logger logger = Logger.getLogger(DealerShow.class);
	ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	/**
	 * 查询组织区域、包括大区和小区（省份）
	 */
	public void queryDealerArea() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try
		{
			String[] ids = request.getParamValues("ids");
			String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));
			String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
			String dealerLevel = CommonUtils.checkNull(request.getParamValue("DEALER_LEVEL"));
			String dealerType = CommonUtils.checkNull(request.getParamValue("DEALER_TYPE"));
			String command = CommonUtils.checkNull(request.getParamValue("command"));
			
			HashMap<String, Object> hsMap = new HashMap<String, Object>();
			
			hsMap.put("dealerCode", dealerCode);
			hsMap.put("dealerName", dealerName);
			hsMap.put("dealerLevel", dealerLevel);
			hsMap.put("dealerType", dealerType);
			hsMap.put("ids", ids);
			
			int pageSize = 20 ;
			int curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.selectDealer(hsMap, curPage, pageSize, command);
			act.setOutData("ps", ps);
		}
		catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询组织");
			logger.error(logonUser,e);
			act.setException(e1);
		}
	}
}
