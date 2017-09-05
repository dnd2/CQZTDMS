package com.infodms.yxdms.service;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public interface WarrantyManualService {

	PageResult<Map<String, Object>> warrantyManualList(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);

	int addWarrantyManual(RequestWrapper request, AclUserBean loginUser);

	Map<String, Object> findInfoByVin(RequestWrapper request);

	Map<String, Object> findWarrantyManual(AclUserBean loginUser,RequestWrapper request);

	List<Map<String, Object>> findWarrantyManualSub(AclUserBean loginUser,RequestWrapper request);

	int auditWarrantyManual(AclUserBean loginUser, RequestWrapper request);

	PageResult<Map<String, Object>> findWarrantyData(RequestWrapper request,AclUserBean loginUser, Integer pageSizeMax, Integer currPage);

	void expotWarrantyData(ActionContext act,PageResult<Map<String, Object>> list);

}
