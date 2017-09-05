package com.infodms.yxdms.service;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;


public interface OutStoreService {

	public Map<String, Double> showOutStoreNumByCondition(RequestWrapper request);

	public void expotDataOldAudit(ActionContext act, RequestWrapper request,Integer pageSizeMax, Integer currPage);

	public int auditAllRebut(RequestWrapper request);

	public int diyOutPartSure(RequestWrapper request, AclUserBean loginUser);

	public List<Map<String, Object>> linkByPartCode(String partCode);
	
	/**
	 * 记录旧件批量修改供应商日志
	 * @param logonUserBean
	 * @param claimId
	 * @param partCode
	 * @param mod_code
	 * @param mod_name
	 * @return
	 */
	public int logUpatePartProductCode(AclUserBean logonUserBean,String claimId, String partCode, String mod_code, String mod_name);
}
