package com.infodms.yxdms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.yxdms.dao.ClearTransferDao;
import com.infodms.yxdms.service.ClearTransferService;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class ClearTransferServiceImpl extends  ClearTransferDao implements  ClearTransferService {

	public PageResult<Map<String, Object>> clearTransferQuery(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.clearTransferQuery(loginUser,request,pageSize,currPage);
	}
	@SuppressWarnings("unchecked")
	public void clearTransferExport(AclUserBean loginUser,RequestWrapper request, Integer pageSizeMax, Integer currPage) {
		PageResult<Map<String, Object>> list =	super.clearTransferQuery(loginUser,request,pageSizeMax,currPage);
		try {
			String[] head = new String[] {"经销商代码","经销商简称", "结算编号","结算总额", "开始时间", "结束时间",
					"PDI费","保养费","工时费","材料费","其他费用" };
			List<Map<String, Object>> records = list.getRecords();
			List params = new ArrayList();
			if (records != null && records.size() > 0) {
				for (Map<String, Object> map : records) {
					String[] detail = new String[11];
					detail[0] = BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[1] = BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[2] = BaseUtils.checkNull(map.get("CLEARING_NUMBER"));
					detail[3] = BaseUtils.checkNull(map.get("TOTAL_SETTLEMENT"));
					detail[4] = BaseUtils.checkNull(map.get("START_DATE"));
					detail[5] = BaseUtils.checkNull(map.get("END_DATE"));
					detail[6] = BaseUtils.checkNull(map.get("PDI_AMOUNT"));
					detail[7] = BaseUtils.checkNull(map.get("MAINTENANCE_AMOUNT"));
					detail[8] = BaseUtils.checkNull(map.get("BALANCE_LABOUR_AMOUNT"));
					detail[9] = BaseUtils.checkNull(map.get("BALANCE_PART_AMOUNT"));
					detail[10] = BaseUtils.checkNull(map.get("OTHER_AMOUNT"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(ActionContext.getContext().getResponse(), ActionContext.getContext().getRequest(), head, params, "服务站结算转账明细"+systemDateStr+".xls", "导出数据", null);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public PageResult<Map<String, Object>> TransferWithoutTaxQuery(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		return super.TransferWithoutTaxQuery(loginUser,request,pageSize,currPage);
	}
	@SuppressWarnings("unchecked")
	public void TransferWithoutTaxExport(AclUserBean loginUser,RequestWrapper request, Integer pageSizeMax, Integer currPage) {
		PageResult<Map<String, Object>> list =	super.TransferWithoutTaxQuery(loginUser,request,pageSizeMax,currPage);
		try {
			String[] head = new String[] {"经销商代码","经销商简称", "结算编号","转账日期", "发票批号", "发票号码",
					"金额","税额","合计" };
			List<Map<String, Object>> records = list.getRecords();
			List params = new ArrayList();
			if (records != null && records.size() > 0) {
				for (Map<String, Object> map : records) {
					String[] detail = new String[9];
					detail[0] = BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[1] = BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[2] = BaseUtils.checkNull(map.get("CLEARING_NUMBER"));
					detail[3] = BaseUtils.checkNull(map.get("TRANSFER_TICKETS_DATE"));
					detail[4] = BaseUtils.checkNull(map.get("LABOUR_RECEIPT"));
					detail[5] = BaseUtils.checkNull(map.get("PART_RECEIPT"));
					detail[6] = BaseUtils.checkNull(map.get("AMOUNT_OF_MONEY"));
					detail[7] = BaseUtils.checkNull(map.get("TAX_RATE_MONEY"));
					detail[8] = BaseUtils.checkNull(map.get("AMOUNT_SUM"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(ActionContext.getContext().getResponse(), ActionContext.getContext().getRequest(), head, params, "服务站结算转账明细"+systemDateStr+".xls", "导出数据", null);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public PageResult<Map<String, Object>> InvoiceCompareQueryList(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		return super.InvoiceCompareQueryList( loginUser,  request,  pageSize, currPage);
	}
	public void InvoiceCompareExport(AclUserBean loginUser,RequestWrapper request, Integer pageSizeMax, Integer currPage) {
		PageResult<Map<String, Object>> list =super.InvoiceCompareQueryList( loginUser,  request,  pageSizeMax, currPage);
		try {
			String[] head = new String[] {"经销商代码","经销商简称", "工单号","结算金额", "开票金额", "开始时间","纳税人类型","纳税率" };
			List<Map<String, Object>> records = list.getRecords();
			List params = new ArrayList();
			if (records != null && records.size() > 0) {
				for (Map<String, Object> map : records) {
					String[] detail = new String[8];
					detail[0] = BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[1] = BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[2] = BaseUtils.checkNull(map.get("REMARK"));
					detail[3] = BaseUtils.checkNull(map.get("AMOUNT_SUM"));
					detail[4] = BaseUtils.checkNull(map.get("PAYMENT_AMOUNT"));
					detail[5] = BaseUtils.checkNull(map.get("CREATE_DATE"));
					detail[6] = BaseUtils.checkNull(map.get("TAXPAYER_NATURE"));
					detail[7] = BaseUtils.checkNull(map.get("TAX_DISRATE"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(ActionContext.getContext().getResponse(), ActionContext.getContext().getRequest(), head, params, "结算金额和开票金额比对"+systemDateStr+".xls", "导出数据", null);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	

}
