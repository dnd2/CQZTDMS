package com.infodms.dms.actions.sales.storage.sendmanage;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storage.sendManage.WaybillManageDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yixg
 * 运单发票录入
 * */
public class WaybillIn {
	private ActionContext act = ActionContext.getContext();
	public Logger logger = Logger.getLogger(WaybillIn.class);
	private static final String waybillInDealerInitUrl = "/jsp/sales/storage/sendmanage/waybillIn/waybillInDealerInit.jsp";
	private static final String waybillInInitUrl = "/jsp/sales/storage/sendmanage/waybillIn/waybillInInit.jsp";
	private static final String waybillInAddUrl = "/jsp/sales/storage/sendmanage/waybillIn/waybillDealerInAdd.jsp";
    WaybillManageDao dao = WaybillManageDao.getInstance();
	/**
	 * 初始化运单发票录入页面
	 * */
	public void waybillInInit(){
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//如果说用户的经销商代码不为空，说明是经销商端
			if(null != logonUser.getDealerId() && !"".equals(logonUser.getDealerId())){
				act.setForword(waybillInDealerInitUrl);
			}else{
				//如果用户的经销商ID为空，说明为机厂端
				act.setForword(waybillInInitUrl);
				//
			}
		
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运单发票录入初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**查询所需要录入订单的运单信息*/
	public void waybillInQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
		String dealerId = "";
		RequestWrapper request = act.getRequest();
		//如果说用户的经销商代码不为空，说明是经销商端
		if(null != logonUser.getDealerId() && !"".equals(logonUser.getDealerId())){
			dealerId = logonUser.getDealerId();
		}else{
			//如果用户的经销商ID为空，说明为机厂端
			dealerId = request.getParamValue("dearlerId");
		}
		String vin = request.getParamValue("VIN");
		String billNo = request.getParamValue("BILL_NO");
		String invoiceFlag =  request.getParamValue("INVOINCE_FLAG");
		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
				.getParamValue("curPage")) : 1;
	
		
		PageResult<Map<String, Object>> wayBillList = dao.getWayBillInList(billNo,vin,"",dealerId, invoiceFlag,curPage,
				Constant.PAGE_SIZE);
		act.setOutData("ps", wayBillList);
		}catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运单发票查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**初始化录入发票号*/
	public void initAddInvoiceNo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String vehcleId = request.getParamValue("VEHICLE_ID");
			String vin = request.getParamValue("VIN");
			String billNo = request.getParamValue("BILL_NO");
			String invoiceFlag =  request.getParamValue("INVOINCE_FLAG");
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
		
			PageResult<Map<String, Object>> wayBillList = dao.getWayBillInList(billNo,vin,vehcleId,logonUser.getDealerId(), invoiceFlag,curPage,
					Constant.PAGE_SIZE);
			List<Map<String, Object>> vehcleList =  wayBillList.getRecords();
			//判断VIN对应的车辆是否存在
			if(null == vehcleList || vehcleList.size() < 0 ){
				BizException e1 = new BizException(act, ErrorCodeConstant.QUERY_FAILURE_CODE,
						"VIN对应的车辆不存在！");
				act.setException(e1);
			}else{
				Map<String, Object> data = vehcleList.get(0);
				act.setOutData("data", data);
				act.setForword(waybillInAddUrl);
			}
			
			}catch(Exception e) {
				BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
						"运单发票查询失败");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
	}
	
	/**保存发运发票号*/
	public void saveInvoiceNo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String vehcleId = request.getParamValue("VEHICLE_ID");
			String invoiceNo = request.getParamValue("INVOICE_NO");
			dao.updateInvoiceNo(vehcleId,invoiceNo,logonUser);
			act.setForword(waybillInDealerInitUrl);
		}catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE,
					"运单发票保存失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
