package com.infodms.yxdms.action;

import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.yxdms.service.ClearTransferService;
import com.infodms.yxdms.service.impl.ClearTransferServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infoservice.po3.bean.PageResult;

public class ClearTransferAction extends BaseAction {
	private ClearTransferService transferService = new ClearTransferServiceImpl();
	public void  transferdetail(){
		sendMsgByUrl("claim", "Transfer_settlement", "服务站转账明细页面");
		
	}
	/**
	 * 服务站结算转账明细查询
	 */
	public void  clearTransferQuery(){
		PageResult<Map<String, Object>> list = transferService.clearTransferQuery(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
	}
	/**
	 * 服务站结算转账明细导出
	 */
	public void clearTransferExport(){
		transferService.clearTransferExport(loginUser,request,Constant.PAGE_SIZE_MAX,getCurrPage());
		
	}
	/**
	 * 服务站已转账不含税金额主页面
	 */
	public void  TransferWithoutTax(){
		sendMsgByUrl("claim", "Transfer_Without_Tax", "服务站已转账不含税金额主页面");
		
	}
	public void  TransferWithoutTaxQuery(){
		PageResult<Map<String, Object>> list =transferService.TransferWithoutTaxQuery(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
	}
	public void TransferWithoutTaxExport(){
		transferService.TransferWithoutTaxExport(loginUser,request,Constant.PAGE_SIZE_MAX,getCurrPage());
		
	}
	public void InvoiceCompareQuery(){
		sendMsgByUrl("claim", "Invoice_Compare_list", "服务站结算金额和开票金额对比查询主页面");
		
	}
	public void InvoiceCompareQueryList(){
		PageResult<Map<String, Object>> list =transferService.InvoiceCompareQueryList(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps",list);
	}
	
	
		
	public void  InvoiceCompareExport(){
		transferService.InvoiceCompareExport(loginUser,request,Constant.PAGE_SIZE_MAX,getCurrPage());
	}
	

}
