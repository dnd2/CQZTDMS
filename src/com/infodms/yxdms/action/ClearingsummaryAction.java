package com.infodms.yxdms.action;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.yxdms.service.ClearingsummaryService;
import com.infodms.yxdms.service.OldReturnService;
import com.infodms.yxdms.service.impl.ClearingsummaryServiceImpl;
import com.infodms.yxdms.service.impl.OldReturnServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infoservice.po3.bean.PageResult;
public class ClearingsummaryAction extends BaseAction {
	private PageResult<Map<String, Object>> list = null;
	private ClearingsummaryService clearingsummaryService = new ClearingsummaryServiceImpl();
	public void DealerCountexport(){
		  list = clearingsummaryService.DealerCountexport(request,loginUser,Constant.PAGE_SIZE_MAX, getCurrPage());
		
	}
}
