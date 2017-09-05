package com.infodms.yxdms.service;

import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public interface ClearingsummaryService {

	PageResult<Map<String, Object>> DealerCountexport(RequestWrapper request,AclUserBean loginUser,Integer pageSizeMax, Integer currPage);

	

}
