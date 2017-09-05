package com.infodms.yxdms.service.impl;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.yxdms.dao.RepairOrderDAO;
import com.infodms.yxdms.service.RepairOrderService;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class RepairOrderServiceImpl extends RepairOrderDAO implements RepairOrderService  {
	
	public PageResult<Map<String, Object>> queryRepairOrderDelet(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.queryRepairOrderDelet(request,loginUser,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> repairOrderQuery(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage) {
		return super.repairOrderQuery(request,loginUser,pageSize,currPage);
	}

	public List<Map<String, Object>> ModelcodeQuery(RequestWrapper request, AclUserBean loginUser, Integer pageSizeMax,Integer currPage) {
		return super.ModelcodeQuery( request,  loginUser,  pageSizeMax, currPage);
	}

	public PageResult<Map<String, Object>> OrderByVindata(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.OrderByVindata( request,  loginUser,  pageSize, currPage);
	}
	
}
