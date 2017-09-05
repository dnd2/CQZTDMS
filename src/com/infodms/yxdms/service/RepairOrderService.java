package com.infodms.yxdms.service;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public interface RepairOrderService {


	PageResult<Map<String, Object>> queryRepairOrderDelet(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage);

	PageResult<Map<String, Object>> repairOrderQuery(RequestWrapper request, AclUserBean loginUser,Integer pageSize, Integer currPage);

	List<Map<String, Object>> ModelcodeQuery(RequestWrapper request,AclUserBean loginUser, Integer pageSizeMax, Integer currPage);

	PageResult<Map<String, Object>> OrderByVindata(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);

}
