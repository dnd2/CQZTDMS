package com.infodms.yxdms.action;

import java.util.List;
import java.util.Map;

import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsPaymentPO;
import com.infodms.yxdms.service.InvoiceService;
import com.infodms.yxdms.service.impl.InvoiceServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
/**
 * 开票action
 * @author yuewei
 *
 */
public class InvoiceAction extends BaseAction{

	private InvoiceService invoiceService = new InvoiceServiceImpl();
	
	
	public void checkticeksByBalanceNo(){
		String res=invoiceService.checkticeksByBalanceNo(request);
		act.setOutData("res", res);
	}
	
	public void AppprintFJ(){
		List<FsFileuploadPO> fileList = invoiceService.AppprintFJ(getParam("balanecNo"));// 取得附件
		act.setOutData("fileList", fileList);
		sendMsgByUrl("invioce", "appprintfj", "运单附件列表");
	}
	
	public void appprintSencondByNo(){
		String balance_oder = getParam("BALANCE_ODER");
		List<Map<String, Object>> list = invoiceService.invoiceInfoSecondTimeByNo(balance_oder);
		act.setOutData("list", list);
		sendMsgByUrl("invioce", "appprintSencondByNo", "打印二次入库");
	}
	
	public void returnShow(){
		List<Map<String, Object>> list =invoiceService.returnShow(getParam("no"));
		act.setOutData("list", list);
		sendMsgByUrl("invioce", "returnShow", "回运运费单明细");
	}
	public void addRemarkPayment(){
		String balance_oder = getParam("balance_oder");
		act.setOutData("balance_oder", balance_oder);
		 String type =getParam("type");
		   if ("add".equals(type)) {
			   act.setOutData("balance_oder",balance_oder);
			  List<TtAsPaymentPO> list = invoiceService.queryPaymentByid(request,loginUser);
			  act.setOutData("remark",list.get(0).getNotes());
			  sendMsgByUrl("invioce", "add_Remark_Payment", "结算开票备注增加");
		   }else if ("addsure".equals(type)) {
			 int res =  invoiceService.addsureRemarkPayment(request,loginUser);
			 if (res>=1) {
				 setJsonSuccByres(1);
			}else {
				 setJsonSuccByres(-1);
			}
		}
	}
	
}
