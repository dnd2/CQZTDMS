package com.infodms.yxdms.service.impl;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.yxdms.dao.WarrantyManualDAO;
import com.infodms.yxdms.service.WarrantyManualService;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class WarrantyManualServiceImpl  extends WarrantyManualDAO implements WarrantyManualService{

	public PageResult<Map<String, Object>> warrantyManualList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.warrantyManualList(request,loginUser,pageSize,currPage);
	}

	public int addWarrantyManual(RequestWrapper request, AclUserBean loginUser) {
		return super.addWarrantyManual(request,loginUser);
	}

	public Map<String, Object> findInfoByVin(RequestWrapper request) {
		return super.findInfoByVin(request);
	}

	public Map<String, Object> findWarrantyManual(AclUserBean loginUser,RequestWrapper request) {
		return super.findWarrantyManual(loginUser,request);
	}

	public List<Map<String, Object>> findWarrantyManualSub(AclUserBean loginUser, RequestWrapper request) {
		return super.findWarrantyManualSub(loginUser,request);
	}

	public int auditWarrantyManual(AclUserBean loginUser, RequestWrapper request) {
		return super.auditWarrantyManual(loginUser,request);
	}

	public PageResult<Map<String, Object>> findWarrantyData(RequestWrapper request, AclUserBean loginUser, Integer pageSizeMax,Integer currPage) {
		return super.findWarrantyData(request,loginUser,pageSizeMax,currPage);
	}

	public void expotWarrantyData(ActionContext act,PageResult<Map<String, Object>> list) {
	   super.expotWarrantyData(act,list);
	}
	

}
