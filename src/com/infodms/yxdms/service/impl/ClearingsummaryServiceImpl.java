package com.infodms.yxdms.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.yxdms.dao.ClearingsummaryDAO;
import com.infodms.yxdms.service.ClearingsummaryService;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class ClearingsummaryServiceImpl extends ClearingsummaryDAO implements ClearingsummaryService {

	public PageResult<Map<String, Object>> DealerCountexport(RequestWrapper request, AclUserBean loginUser, Integer pageSizeMax,Integer currPage) {
		PageResult<Map<String, Object>>  list =   super.findDate(request,loginUser,Constant.PAGE_SIZE_MAX, currPage);
		try {
			              
			String[] head = new String[] {"大区" ,"服务站简称","服务商名称", "开始日期","结束时间", "旧件回运日期", "旧件签收日期",
					"旧件审核日期","发票上报日期","开票日期","收票日期", "验票日期","转账日期", "申请总费用" , "结算总费用" , "索赔单数量" };
			List<Map<String, Object>> records = list.getRecords();
			List params = new ArrayList();
			if (records != null && records.size() > 0) {
				for (Map<String, Object> map : records) {
					String[] detail = new String[16];
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
					SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					detail[0] = BaseUtils.checkNull(map.get("ORG_NAME"));
					detail[1] = BaseUtils.checkNull(map.get("DEALER_NAME"));
					detail[2] = BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[3] = BaseUtils.checkNull(dateFormat.format(map.get("K_SUB_DATE")));
					detail[4] = BaseUtils.checkNull(dateFormat.format(map.get("J_SUB_DATE")));
					detail[5] = BaseUtils.checkNull(map.get("RETURN_DATE"));
					detail[6] = BaseUtils.checkNull(map.get("SIGN_DATE"));
					detail[7] = BaseUtils.checkNull(map.get("IN_WARHOUSE_DATE"));
					detail[8] = BaseUtils.checkNull(map.get("CREAT_DATES"));
					detail[9] = BaseUtils.checkNull(map.get("CREATE_DATE"));
					detail[10] = BaseUtils.checkNull(map.get("COLLECT_TICKETS_DATE"));
					detail[11] = BaseUtils.checkNull(map.get("CHECK_TICKETS_DATE"));
					detail[12] = BaseUtils.checkNull(map.get("TRANSFER_TICKETS_DATE"));
					detail[13] = BaseUtils.checkNull(map.get("REPAIR_TOTAL"));
					detail[14] = BaseUtils.checkNull(map.get("BALANCE_AMOUNT"));
					detail[15] = BaseUtils.checkNull(map.get("CLAIMCOUNT"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(ActionContext.getContext().getResponse(), ActionContext.getContext().getRequest(), head, params, "汇总结算"+systemDateStr+".xls", "导出数据", null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}

	

}
