package com.infodms.dms.actions.sales.accountsmanage;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.accountsmanage.DlrPayInquiryDAO;
import com.infodms.dms.dao.sales.accountsmanage.InvoiceManageDAO;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TtPurchaseInvoicePO;
import com.infodms.dms.po.TtPurchaseInvoicesProductPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.VwMaterialInfoPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class InvoiceManage extends BaseDao{
	public Logger logger = Logger.getLogger(InvoiceManage.class);
	private ActionContext act = ActionContext.getContext();
	private InvoiceManageDAO dao = InvoiceManageDAO.getInstance();
	private static final InvoiceManage im = new InvoiceManage();
	RequestWrapper request = act.getRequest();
	private final String invoiceManageInitUrl = "/jsp/sales/accountsmanage/invoiceManage.jsp";
	private final String invoiceManageAddInitUrl = "/jsp/sales/accountsmanage/invoiceInfoAddInit.jsp";
	private final String invoiceManageAdddDetailUrl = "/jsp/sales/accountsmanage/invoiceInfoAddDetail.jsp";
	private final String invoiceManageInitQueryPageUrl = "/jsp/sales/accountsmanage/invoiceManageQueryPage.jsp";
	private final String queryInvoiceAndProduct = "/jsp/sales/accountsmanage/invoiceInfoQuery.jsp"; 
	private final String invoiceAdd = "/jsp/sales/accountsmanage/invoiceInfoAdd.jsp";
	//获取InvoiceManage实例
	public static InvoiceManage getInvoiceManage(){
		return im;
	}
/*	//发票管理页面初始化 2013-2-26
	public void invoiceManageInit(){
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			logonUser.getDutyType();//判断是否是经销商端还是车厂端
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setOutData("logonUser", logonUser);
//			String dealerIds = logonUser.getDealerId();
//			List<Map<String,Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds);
//			act.setOutData("dealerList", dealerList);
			act.setForword(invoiceManageInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    }*/
	
	
	//发票管理页面初始化 20170822 _new
	public void invoiceManageInit(){
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			logonUser.getDutyType();//判断是否是经销商端还是车厂端
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setOutData("logonUser", logonUser);
//			String dealerIds = logonUser.getDealerId();
//			List<Map<String,Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds);
//			act.setOutData("dealerList", dealerList);
			act.setForword(invoiceManageAddInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    }
	
	//发票管理页面查询 20170822 _new
	public void invoiceManageQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String areaId = request.getParamValue("areaId"); // 业务范围ID
			String area = request.getParamValue("area"); // 业务范围IDS
			String dealerCode = request.getParamValue("dealerCode"); // 经销商CODE
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String orderType = request.getParamValue("orderTypeSel"); // 订单类型
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			String reqStatus = request.getParamValue("reqStatus"); // 状态
			// String orgCode = request.getParamValue("orgCode");// 区域代码
			String orgIdN = request.getParamValue("orgId");// 区域代码
			String beginTime = request.getParamValue("startDate");// 开始日期
			String endTime = request.getParamValue("endDate");// 结束日期

			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String dutyType = logonUser.getDutyType();
			String orgId = "";

			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));

			if (Constant.DUTY_TYPE_LARGEREGION.toString().equalsIgnoreCase(dutyType)) {
				orgId = logonUser.getOrgId().toString();
			} else {
				if (!CommonUtils.isNullString(orgIdN)) {
					orgId = orgIdN;
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areaId", areaId);
			map.put("area", area);
			map.put("groupCode", groupCode);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderNo", orderNo);
			map.put("companyId", companyId.toString());
			map.put("reqStatus", reqStatus);
			// map.put("orgCode", orgCode);
			map.put("dutyType", dutyType);
			map.put("orgId", orgId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("operateType", "1");
			map.put("userId", logonUser.getUserId().toString());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOrderResourceReserveQuery(map, curPage, Integer.parseInt(pageSize));
			act.setOutData("ps", ps);
			act.setOutData("orderType", orderType);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 发票明细审核
	 */
	public void invoiceManageDetailQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {

			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String paraSys = CommonDAO.getPara(Constant.CHANA_SYS.toString());

			if (Constant.COMPANY_CODE_JC.equals(paraSys.toUpperCase())) {
				act.setOutData("returnValue", 2);
			} else if (Constant.COMPANY_CODE_CVS.equals(paraSys.toUpperCase())) {
				act.setOutData("returnValue", 1);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！");
			}

			String par = CommonDAO.getPara(Constant.CHG_SINGLE_PRICE.toString());

			act.setOutData("par", par);

			String parReserve = CommonDAO.getPara(Constant.IS_ALLOW_RESERVE_MORE.toString());

			act.setOutData("parReserve", parReserve);

			String orderId = request.getParamValue("orderId"); // 订单ID
			String reqId = request.getParamValue("reqId"); // 发车申请ID
			String orderYearWeek = request.getParamValue("orderYearWeek"); // 订单年周
			String dealerCode = request.getParamValue("dealerCode"); // 经销商CODE
			String areaId = request.getParamValue("areaId"); // 业务范围ID
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String orderType = request.getParamValue("orderType"); // 订单类型
			String orderTypeSel = request.getParamValue("orderTypeSel");
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			String reqStatus = request.getParamValue("reqStatus"); // 状态
			String orgCode = request.getParamValue("orgCode");// 区域代码
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			String historyCount = request.getParamValue("historyCount");
		
			Map<String, Object> map = dao.getInvoiceInfoByReqId(reqId);
			String  warseId= null;
			if(map.get("WAREHOUSE_ID")!=null){
				warseId=	map.get("WAREHOUSE_ID").toString();
			}

			List<Map<String, Object>> list1 = dao.getorderResourceReserveDetailList(warseId,reqId, orderType, logonUser.getCompanyId().toString());
			String priceId="";
			TtVsOrderPO tvo=new TtVsOrderPO();
			tvo.setOrderId(new Long(orderId));
			tvo=(TtVsOrderPO) dao.select(tvo).get(0);
			TmDealerPO tm=new TmDealerPO();
			tm.setDealerId(tvo.getOrderOrgId());
			tm=(TmDealerPO) dao.select(tm).get(0);
			priceId=tm.getPersonCharge();
			OrderReportDao dao1=OrderReportDao.getInstance();
			//BigDecimal rebateAmount = dao1.getRebateAccount(map.get("BILLING_ORG_ID").toString());//冻结资金
			Map<String, Object> mapLinkInfo=dao1.getAddressInfo_("", orderId);
			act.setOutData("mapLinkInfo", mapLinkInfo);
			//act.setOutData("rebateEnableAmount", rebateAmount);
			//act.setOutData("rebateAmount", rebateAmount);
			act.setOutData("priceId", priceId);
			act.setOutData("map", map);
			act.setOutData("orderId", orderId);
			act.setOutData("reqId", reqId);
			act.setOutData("list1", list1);
			//act.setOutData("list2", list2);
			act.setOutData("orderYearWeek", orderYearWeek);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("areaId", areaId);
			act.setOutData("groupCode", groupCode);
			act.setOutData("orderType", orderType);
			act.setOutData("orderTypeSel", orderTypeSel);
			act.setOutData("orderNo", orderNo);
			act.setOutData("reqStatus", reqStatus);
			act.setOutData("orgCode", orgCode);
			//act.setOutData("wareHouseList", wareHouseList);
			//act.setOutData("checkGeneral", para != null ? para.getParaValue() : "0");
			//act.setOutData("ratePara", ratePara);
			//act.setOutData("erpCode", erpCode);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setOutData("historyCount", historyCount);
			act.setOutData("sessionId", act.getSession().getId());
			act.setForword(invoiceManageAdddDetailUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	
	public void invociceManageSave(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String[] billAmount=request.getParamValues("billAmount");
			String[] billPrice=request.getParamValues("billPrice");
			String[] materialId=request.getParamValues("materialId");
			String[] billTotalPrice=request.getParamValues("billTotalPriceHid");
			String[] billTaxRate=request.getParamValues("billTaxRate");
			String[] billTotalTax=request.getParamValues("billTotalTaxHid");
			String  invoiceNO=request.getParamValue("invoiceNO");
			String reqId = request.getParamValue("reqId"); // 发运申请ID
			String invoiceDate=request.getParamValue("invoiceDate");
			String dlvryCode=request.getParamValue("invoiceDate");
			String receiveDeptId=request.getParamValue("dealerId");
			String accountTypeId=request.getParamValue("accountTypeId");
			String totalAmount=request.getParamValue("invoiceTotalHid");
			Long invoiceId=Long.valueOf(SequenceManager.getSequence(""));
		
			
			for(int i=0;i<materialId.length;i++){
				if(billAmount[i]!=null&&Integer.valueOf(billAmount[i])>0){
					TmVhclMaterialPO tmp=new TmVhclMaterialPO();
					tmp.setMaterialId(Long.valueOf(materialId[i]));
					List list=dao.select(tmp);
					tmp=(TmVhclMaterialPO) list.get(0);
					TtPurchaseInvoicesProductPO dtlPO=new TtPurchaseInvoicesProductPO();
					dtlPO.setInvoiceProductId(Long.valueOf(SequenceManager.getSequence("")));
					dtlPO.setInvoiceId(invoiceId);
					dtlPO.setProductCode(tmp.getMaterialCode());
					dtlPO.setProductName(tmp.getMaterialName());
					dtlPO.setTaxPrice(Double.valueOf(billPrice[i]));
					dtlPO.setTaxTotalSum(Double.valueOf(billTotalPrice[i]));
					dtlPO.setTaxRate(Double.valueOf(billTaxRate[i]));
					dtlPO.setTaxSum(Double.valueOf(billTotalTax[i]));
					dtlPO.setAmount(Integer.valueOf(billAmount[i]));
					dao.insert(dtlPO);
					
					//查询发运子表信息
					TtVsDlvryReqDtlPO tdrdpQ=new TtVsDlvryReqDtlPO();
					tdrdpQ.setReqId(Long.valueOf(reqId));
					tdrdpQ.setMaterialId(Long.valueOf(materialId[i]));
					List tvdrdplist=dao.select(tdrdpQ);
					tdrdpQ=(TtVsDlvryReqDtlPO)tvdrdplist.get(0);
					
					TtVsDlvryReqDtlPO tdrdp=new TtVsDlvryReqDtlPO();
					tdrdp.setReqId(Long.valueOf(reqId));
					tdrdp.setMaterialId(Long.valueOf(materialId[i]));
					
					
					Long oldBillAmount=tdrdpQ.getBillAmount();
					TtVsDlvryReqDtlPO newTdrdp=new TtVsDlvryReqDtlPO();
					newTdrdp.setBillAmount(Long.valueOf(oldBillAmount)+Long.valueOf(billAmount[i]));
					newTdrdp.setUpdateDate(new Date());
					dao.update(tdrdp, newTdrdp);
				}
			}
			
			TtPurchaseInvoicePO po=new TtPurchaseInvoicePO();
			po.setInvoiceId(invoiceId);
			po.setInvoiceCode(invoiceNO);
			po.setPurchaseInvoiceNo(invoiceNO);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date11=format.parse("2017-08-22");
			po.setBillDeptId(logonUser.getCompanyId());
			po.setDlvryCode(dlvryCode);
			po.setTicketDate(format.parse(invoiceDate));
			po.setCreateDate(new Date());
			po.setCreateUser(logonUser.getUserId());
			po.setReceiveDeptId(Long.valueOf(receiveDeptId));
			po.setAccountTypeId(Long.valueOf(accountTypeId));
			po.setTotalAmount(Double.valueOf(totalAmount));
			//根据reqId查询发运单号
			TtVsDlvryReqPO tvrp=new TtVsDlvryReqPO();
			tvrp.setReqId(Long.valueOf(reqId));
			List reqList=dao.select(tvrp);
			tvrp=(TtVsDlvryReqPO)reqList.get(0);
			
			po.setDlvryCode(tvrp.getDlvryReqNo());
			dao.insert(po);
			
			//查询该经销商该账户下的信息
			TtVsAccountPO tvap=new TtVsAccountPO();
			tvap.setAccountTypeId(Long.valueOf(accountTypeId));
			tvap.setDealerId(Long.valueOf(receiveDeptId));
			List tvapList=dao.select(tvap);
			tvap=(TtVsAccountPO)tvapList.get(0);
			
			
			//更新该经销商账户下的余额
			TtVsAccountPO oldTvap=new TtVsAccountPO();
			oldTvap.setAccountId(Long.valueOf(accountTypeId));
			oldTvap.setDealerId(Long.valueOf(receiveDeptId));
			TtVsAccountPO newTvap=new TtVsAccountPO();
			newTvap.setBalanceAmount(Double.valueOf(tvap.getBalanceAmount())-Double.valueOf(totalAmount));
			newTvap.setUpdateDate(new Date());
			//dao.update(oldTvap, newTvap);
			dao.updateBalance(accountTypeId,receiveDeptId,Double.valueOf(totalAmount));
			
			act.setOutData("returnValue", 1);
			System.out.println("billAmount=="+billAmount);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	
	//发票管理查询页面初始化 2013-2-26
	 public void invoiceManageInitQuerypage(){
			AclUserBean logonUser = null;
			try{
				logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				logonUser.getDutyType();//判断是否是经销商端还是车厂端
				act.setOutData("dutyType", logonUser.getDutyType());
				act.setOutData("orgId", logonUser.getOrgId());
				act.setOutData("logonUser", logonUser);
//				String dealerIds = logonUser.getDealerId();
//				List<Map<String,Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds);
//				act.setOutData("dealerList", dealerList);
				act.setForword(invoiceManageInitQueryPageUrl);
			}catch(Exception e){
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	    }
	/*
	 * 
	 * */
	public void invoiceAdd(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			List<Map<String, Object>> accountType =DlrPayInquiryDAO.getInstance().getAccountType();
			//Map<String, Object> firstNull = new HashMap<String, Object>();			
			TmCompanyPO companyInfo = dao.getBillDeptNameInfo();
			
			if(companyInfo!=null){
				String companyName = companyInfo.getCompanyName();
				Long companyId = companyInfo.getCompanyId();
				String companyShortname = companyInfo.getCompanyShortname();
				act.setOutData("companyName", companyName);
				act.setOutData("companyId", companyId+"");
				act.setOutData("companyShortname", companyShortname);
			}
			else{
				act.setOutData("companyName", "未查找到车厂名字信息");
				act.setOutData("companyId", "");
				act.setOutData("companyShortname", "");
			}
			act.setOutData("accountsTypeList", accountType);			
			String invoiceId=new Long(SequenceManager.getSequence(""))+"";  //发票单号
			act.setOutData("invoiceId", invoiceId);
			act.setForword(invoiceAdd);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void getBillInfoByInvoiceId(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{	
			RequestWrapper request = act.getRequest();			

			String invoiceId = request.getParamValue("saleOrderValue");
			List<Map<String, Object>> dealerInfo = dao.getDealerInfoByID(invoiceId);
			List<Map<String, Object>> accountTypeInfo = dao.getAccountTypeInfo(invoiceId);
			List<Map<String, Object>> rebateMoney = dao.getRebateMoneyInfo(invoiceId);
			List<Map<String, Object>> amountInfo = dao.getMaterialAmountInfo(invoiceId);
			act.setOutData("rebateMoney", rebateMoney);			
			act.setOutData("dealerInfo", dealerInfo);
			act.setOutData("accountTypeInfo", accountTypeInfo);
			act.setOutData("amountInfo", amountInfo);
			//act.setOutData("accountTypeName", accountTypeName);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
		
	/*
	 * 发票添加完做成
	 * */
	public void invoiceAddSave(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{	
			boolean isRebate = false;
			RequestWrapper request = act.getRequest();
			int invoiceAddLineSum = Integer.parseInt(request.getParamValue("invoiceAddLineSum"));
			
			String invoiceId = request.getParamValue("invoiceId");//单据编码
			String purchaseInvoiceNo = request.getParamValue("purchaseInvoiceNo");//采购发票号
			String billDeptId = request.getParamValue("billDeptId");//开票单位名称--id
			String dealerId = request.getParamValue("dealerId");//收票单位名称--id
			
			String accountsType = request.getParamValue("accountsType");//账户类型
			String remark = request.getParamValue("remark");//备注
			String saleOrderNo = request.getParamValue("saleOrderNo");//销售订单编号
			String totalAmount = request.getParamValue("totalAmount");//合计金额
			
			
			//产品信息
			for(int i=0;i<invoiceAddLineSum;i++){
				int lineNum = i+1;//行号
				String invoiceProductId=new Long(SequenceManager.getSequence(""))+"";  //发票单号	
				String productCode = request.getParamValue("productCode"+lineNum);//产品编码
				if(productCode==null)continue;
				String productName = request.getParamValue("productName"+lineNum);//产品名称
				String amount = request.getParamValue("amount"+lineNum);//数量
				String taxPrice = request.getParamValue("taxPrice"+lineNum);//含税单价
				String taxTotalSum = request.getParamValue("taxTotalSum"+lineNum);//含税金额
				String taxRate = request.getParamValue("taxRate"+lineNum);//税率
				String taxSum = request.getParamValue("taxSum"+lineNum);//税额
				
				TtPurchaseInvoicesProductPO invoicesProduct = new TtPurchaseInvoicesProductPO();
				invoicesProduct.setInvoiceId(Long.parseLong(invoiceId));
				invoicesProduct.setInvoiceProductId(Long.parseLong(invoiceProductId));
				invoicesProduct.setAmount(Integer.parseInt(amount));
				invoicesProduct.setTaxPrice(Double.parseDouble(taxPrice));
				invoicesProduct.setTaxTotalSum(Double.parseDouble(taxTotalSum));	
				invoicesProduct.setTaxRate(Double.parseDouble(taxRate));
				invoicesProduct.setProductName(productName);
				invoicesProduct.setProductCode(productCode);
				invoicesProduct.setTaxSum(Double.parseDouble(taxSum));
				dao.insert(invoicesProduct);			
				
//				if("FL".equals(productCode)){//如果有返利
//					isRebate = true;
//				}
				saveDlvryReqDtl(saleOrderNo,productCode, amount,isRebate,taxTotalSum);
			}
			
			//采购发票信息
			TtPurchaseInvoicePO tpi = new  TtPurchaseInvoicePO(); //采购发票信息
			tpi.setInvoiceId(Long.parseLong(invoiceId));
			
			tpi.setInvoiceCode(purchaseInvoiceNo);
			tpi.setPurchaseInvoiceNo(purchaseInvoiceNo);
			tpi.setTicketDate(new Date());
			tpi.setBillDeptId(Long.parseLong(billDeptId));
			tpi.setReceiveDeptId(Long.parseLong(dealerId));
			tpi.setAccountTypeId(Long.parseLong(accountsType));
			tpi.setRemark(remark);
			tpi.setDlvryCode(saleOrderNo);
			tpi.setTotalAmount(Double.parseDouble(totalAmount));
			dao.insert(tpi);
						
		//	act.setForword(invoiceAdd);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 保存开票数量
	 * */
	private void saveDlvryReqDtl(String saleOrderNo, String materialCode,String amount,boolean isRebate,
			String rebateTaxTotalSum){
		
		TtVsDlvryReqPO dlvryReqInfo = new TtVsDlvryReqPO();
		dlvryReqInfo.setDlvryReqNo(saleOrderNo);
		List<Object> listDlvry = dao.select(dlvryReqInfo);
		TtVsDlvryReqPO dlvryReqDate;
		if(listDlvry.size()>0){
			 dlvryReqDate = (TtVsDlvryReqPO)listDlvry.get(0);
		}
		else{
			return;
		}
		
		if(isRebate){
			TtVsDlvryReqPO dlvryReqInfo1 = new TtVsDlvryReqPO();
			dlvryReqInfo1.setIsRebateBill(Constant.IF_TYPE_YES);
			//Double rebateAmount = dlvryReqDate.getRebateAmount();
			//Double totalAmount = rebateAmount+Double.parseDouble(rebateTaxTotalSum);
			//dlvryReqInfo1.setRebateAmount(totalAmount);
			dao.update(dlvryReqInfo, dlvryReqInfo1);			
		}
		
		VwMaterialInfoPO materailInfo = new  VwMaterialInfoPO(); //
		materailInfo.setMaterialCode(materialCode);
		List<Object> listMaterial = new ArrayList<Object>();
		listMaterial = dao.select(materailInfo);
		VwMaterialInfoPO materialDate ;
		if(listMaterial.size()>0){
			materialDate = (VwMaterialInfoPO)listMaterial.get(0);		
		}
		else{
			return;
		}
				
		TtVsDlvryReqDtlPO dlvryReqDtl1 = new TtVsDlvryReqDtlPO();
		dlvryReqDtl1.setMaterialId(materialDate.getMaterialId());
		dlvryReqDtl1.setReqId(dlvryReqDate.getReqId());
		List<Object> listDlvryDtlInfo = dao.select(dlvryReqDtl1);
		if(listDlvryDtlInfo.size()==0){
			return;
		}
		TtVsDlvryReqDtlPO reqDtldata = (TtVsDlvryReqDtlPO)listDlvryDtlInfo.get(0);
		
		Long addAmount = reqDtldata.getBillAmount()  +Long.parseLong(amount);
		TtVsDlvryReqDtlPO dlvryReqDtl2 = new TtVsDlvryReqDtlPO();
		dlvryReqDtl2.setBillAmount(addAmount);
		dao.update(dlvryReqDtl1, dlvryReqDtl2);
	}  
	
	//发票管理初始化页面查询 2013-2-27
	public void invoiceManageInitQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");	
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			//String dealerId = logonUser.getDealerId(); //得到经销商ID
			StringBuffer con = new StringBuffer();
			Long orgId = logonUser.getOrgId(); //组织ID
			String dutyType = logonUser.getDutyType(); //组织类型
			String dealerCodes=""; //单个经销商code
			String dealerCode=""; //多个经销商code
			String  purchaseInvoiceNo = request.getParamValue("purchaseInvoiceNo");
			String  billDate= request.getParamValue("billDate");
			String  ticketDeptName= request.getParamValue("ticketDeptName");
			String  saleOrderNo= request.getParamValue("saleOrderNo");

			//处理经销商code
			if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())){
				dealerCodes=logonUser.getDealerCode();
			}else{
				dealerCode=act.getRequest().getParamValue("dealerCode");;
				if("".equals(dealerCode)||dealerCode==null){
					dealerCodes=dealerCode;
				}else{
					dealerCodes=dealerCode.replace(",", "','");
				}
			}

			//采购发票号
			if (purchaseInvoiceNo != null && !"".equals(purchaseInvoiceNo)) {
				con.append(" and tpi.PURCHASE_INVOICE_NO like '%" + purchaseInvoiceNo +"%'\n");  
			}
			//开票日期
			if (billDate != null && !"".equals(billDate)) {
				//con.append(" and tpi.TICKET_DATE = TO_DATE('" + billDate +"','yyyy-MM-dd')\n");  
				con.append(" and TO_CHAR(tpi.TICKET_DATE,'yyyy-MM-dd') ='" +billDate.toString()+"'");
			}
			//经销商代码
			if (dealerCodes != null && !"".equals(dealerCodes)) {
				con.append(" and td.DEALER_CODE IN ('" + dealerCodes +"')\n");  
			}
			//收票单位名称
			if (ticketDeptName != null && !"".equals(ticketDeptName)) {
				con.append(" and td.DEALER_NAME like '%" + ticketDeptName +"%'\n");  
			}
			//销售订单编号
			if (saleOrderNo != null && !"".equals(saleOrderNo)) {
				con.append(" and tPI.DLVRY_CODE like '%" + saleOrderNo +"%'\n");  
			}
			PageResult<Map<String, Object>> ps = new PageResult<Map<String, Object>>();
			if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())){
				ps = dao.invoiceManageInitQuery(con.toString(),curPage, 10); //经销商端
			}else{
				ps = dao.invoiceManageDeptInitQuery(con.toString(),dutyType,orgId,curPage, 10);//车厂端
			}
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//发票管理页面数据查看(经销商端)
	public void queryFindInvoiceAndProduct(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String invoiceId = CommonUtils.checkNull(request.getParamValue("INVOICE_ID"));
			TtPurchaseInvoicePO tpi = new  TtPurchaseInvoicePO(); //采购发票信息
			tpi.setInvoiceId(Long.parseLong(invoiceId));
			List<Object> list = new ArrayList<Object>();
			list = dao.select(tpi);
			if(list.size()>0){
				act.setOutData("map",list.get(0));
			}
			act.setOutData("returnWhere",2);
			act.setForword(queryInvoiceAndProduct);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryInvoiceAndProduct(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String invoiceId = CommonUtils.checkNull(request.getParamValue("INVOICE_ID"));
			TtPurchaseInvoicePO tpi = new  TtPurchaseInvoicePO(); //采购发票信息
			tpi.setInvoiceId(Long.parseLong(invoiceId));
			List<Object> list = new ArrayList<Object>();
			list = dao.select(tpi);
			if(list.size()>0){
				act.setOutData("map",list.get(0));
			}
			act.setOutData("returnWhere",1);
			act.setForword(queryInvoiceAndProduct);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	//根据采购发票信息id获取产品信息对象
	public List<TtPurchaseInvoicesProductPO> getProductByInvoiceId(String invoiceId){
		return dao.getProductByInvoiceId(invoiceId);
	}
	
	//根据开票单位ID获取开票单位名称
	public String getBillDeptName(){
		ActionContext act = ActionContext.getContext();
		TtPurchaseInvoicePO tpi = new TtPurchaseInvoicePO();    
		tpi = (TtPurchaseInvoicePO)act.getOutData("map");
		Long BillDeptId = tpi.getBillDeptId();
		return dao.getBillDeptNameById(BillDeptId);
	}
	
	//根据收票单位ID获取收票单位名称
	public String getReceiveDeptName(){
		ActionContext act = ActionContext.getContext();
		TtPurchaseInvoicePO tpi = new TtPurchaseInvoicePO();   
		tpi = (TtPurchaseInvoicePO)act.getOutData("map");
		Long receiveDeptId = tpi.getReceiveDeptId();
		return dao.getReceiveDeptNameById(receiveDeptId);
	}
	
	//根据销售订单ID获取销售订单编号
	public String getDlvryReqNo(){
		ActionContext act = ActionContext.getContext();
		TtPurchaseInvoicePO tpi = new TtPurchaseInvoicePO();  
		tpi = (TtPurchaseInvoicePO)act.getOutData("map");
		Long dlvryReqId = tpi.getDlvryReqId();
		return dao.getDlvryReqNoById(dlvryReqId);
	}
	
	//根据产品ID获取产品编码 PRODUCT_CODE
	public List<String> getProductCodes(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		String invoiceId = CommonUtils.checkNull(request.getParamValue("INVOICE_ID"));
		List<TtPurchaseInvoicesProductPO> invoiceProducts = dao.getProductByInvoiceId(invoiceId);
		List<String> productCodes = new ArrayList<String>();
		for(int i=0;i<invoiceProducts.size();i++){
//			Long id = invoiceProducts.get(i).getProductId();
//			productCodes.add(dao.getProductCodeById(id));
			String code = invoiceProducts.get(i).getProductCode();
			productCodes.add(code);
		}
		return productCodes;
	}
	//根据产品ID获取产品名称
	public List<String> getProductNames(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		String invoiceId = CommonUtils.checkNull(request.getParamValue("INVOICE_ID"));
		List<TtPurchaseInvoicesProductPO> invoiceProducts = dao.getProductByInvoiceId(invoiceId);
		List<String> productNames = new ArrayList<String>();
		for(int i=0;i<invoiceProducts.size();i++){
//			productNames.add(dao.getProductNameById(invoiceProducts.get(i).getProductId()));
			productNames.add(invoiceProducts.get(i).getProductName());
		}
		return productNames;
	}
	
	//根据账户类型ID获得账户类型名称
	public String getAccountName(){
		ActionContext act = ActionContext.getContext();
		TtPurchaseInvoicePO tpi = new TtPurchaseInvoicePO();  
		tpi = (TtPurchaseInvoicePO)act.getOutData("map");
		Long accountTypeId = tpi.getAccountTypeId();
		return dao.getAccountNameById(accountTypeId);
	}

	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
