package com.infodms.dms.actions.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
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
public class OrgShow {
	public OrgShowDao dao = OrgShowDao.getInstance();
	public Logger logger = Logger.getLogger(OrgShow.class);
	ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	/**
	 * 查询组织区域、包括大区和小区（省份）
	 */
	public void queryOrgArea() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try
		{
			String[] ids = request.getParamValues("ids");
			String orgCode = CommonUtils.checkNull(request.getParamValue("ORG_CODE"));
			String orgName = CommonUtils.checkNull(request.getParamValue("ORG_NAME"));
			String orgLevel = CommonUtils.checkNull(request.getParamValue("ORG_LEVEL"));
			String dutyType = CommonUtils.checkNull(request.getParamValue("DUTY_TYPE"));
			String command = CommonUtils.checkNull(request.getParamValue("command"));
			
			HashMap<String, Object> hsMap = new HashMap<String, Object>();
			
			hsMap.put("orgCode", orgCode);
			hsMap.put("orgName", orgName);
			hsMap.put("orgLevel", orgLevel);
			hsMap.put("dutyType", dutyType);
			hsMap.put("ids", ids);
			
			int pageSize = 10 ;
			int curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.selectOrgArea(hsMap, curPage, pageSize, command);
			act.setOutData("ps", ps);
		}
		catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询组织");
			logger.error(logonUser,e);
			act.setException(e1);
		}
	}
}
