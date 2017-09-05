/**
 * 
 */
package com.infodms.dms.actions.claim.authorization;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.claim.application.BalanceStatusRecord;
import com.infodms.dms.actions.claim.application.DealerNewKp;
import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.BalanceYieldlyBean;
import com.infodms.dms.bean.TtIfMarketBean;
import com.infodms.dms.bean.auditBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.application.DealerKpDao;
import com.infodms.dms.dao.claim.application.DealerNewKpUpdateDAO;
import com.infodms.dms.dao.claim.authorization.BalanceMainDao;
import com.infodms.dms.dao.feedbackMng.MarketQuesOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsDealerCheckPO;
import com.infodms.dms.po.TtAsDealerTypePO;
import com.infodms.dms.po.TtAsPaymentPO;
import com.infodms.dms.po.TtAsWrAdminDeductPO;
import com.infodms.dms.po.TtAsWrBalanceAuthitemPO;
import com.infodms.dms.po.TtAsWrBalanceStopAuthPO;
import com.infodms.dms.po.TtAsWrBalanceStopPO;
import com.infodms.dms.po.TtAsWrCheckApplicationPO;
import com.infodms.dms.po.TtAsWrCheckDetailPO;
import com.infodms.dms.po.TtAsWrClaimBalanceFinePO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrContractPO;
import com.infodms.dms.po.TtAsWrDeductBalancePO;
import com.infodms.dms.po.TtAsWrFinePO;
import com.infodms.dms.po.TtAsWrSpefeePO;
import com.infodms.dms.po.TtIfMarketPO;
import com.infodms.dms.po.TtInvoiceTaxratePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.service.InvoiceService;
import com.infodms.yxdms.service.impl.InvoiceServiceImpl;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: DealerSupplierInfo.java
 * 
 * @Description:CHANADMS
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-8-30
 * 
 * @author lishuai
 * @mail lishuai103@yahoo.cn
 * @version 1.0
 * @remark
 */
@SuppressWarnings("unchecked")
public class BalanceMain extends BaseAction {
	private final String initUrl = "/jsp/claim/authorization/balanceMain.jsp";
	private final String delaerUrl = "/jsp/claim/authorization/dealerQuery.jsp";
	private final String financialInitUrl = "/jsp/claim/authorization/financialMain.jsp";
	private final String infoUrl = "/jsp/claim/authorization/balanceMInfo.jsp";
	private final String auditApplyUrl = "/jsp/claim/authorization/auditApply.jsp";
	private final String finInfoUrl = "/jsp/claim/authorization/balanceFInfo.jsp";
	private final String finViewUrl = "/jsp/claim/authorization/balanceInfoView.jsp";
	private final String finViewUrldao = "/jsp/claim/authorization/balanceInfoViewdao.jsp";
	private final String finViewPrintUrl = "/jsp/claim/authorization/balanceInfoViewPrint.jsp";
	private final String finViewUrl01 = "/jsp/claim/authorization/balanceInfoView01.jsp";
	private final String deleteBalanceInfoView = "/jsp/claim/authorization/deleteBalanceInfoView.jsp";
	private final String finViewUrlTickets = "/jsp/claim/authorization/balanceInfoTickets.jsp";
	private final String finViewFineUrl = "/jsp/claim/authorization/balanceInfoFineView.jsp";
	private final String delaerInfoUrl = "/jsp/claim/authorization/dealerQueryInfo.jsp";
	private final String invoicePrint = "/jsp/claim/authorization/invoicePrint.jsp";
	private final String printUrl = "/jsp/claim/authorization/balancePrint.jsp";
	private final String AppprintUrl = "/jsp/claim/authorization/AppbalancePrint.jsp";
	private final String oemUrl = "/jsp/claim/authorization/oemBanlanceQuery.jsp";
	private final String oemUrlStop = "/jsp/claim/authorization/oemBanlanceQueryStop.jsp";
	private final String oemUrlConstract = "/jsp/claim/authorization/oemContractNumber.jsp";
	private final String oemContractNumberModifily = "/jsp/claim/authorization/oemContractNumberModifily.jsp";
	private final String CLAIM_INIT_URL = "/jsp/claim/authorization/getClaimInit.jsp";// 汇总打印初始化页面
	private final String PRINTPROTER_URL = "/jsp/claim/authorization/printPorter.jsp";
	private final String CLAIM_PRINT_URL = "/jsp/claim/authorization/claimPrint.jsp";// 汇总打印页面
	private final String PORTER_PRINT_URL = "/jsp/claim/authorization/queryPrintPorter.jsp";
	private final String BALANCE_DETAIL_URL = "/jsp/claim/authorization/balanceDetail.jsp";
	private final String DEALER_BALANCE_DETAIL_URL = "/jsp/claim/authorization/dealerBalanceDetail.jsp";
	private final String DEALER_BALANCE_INFO_URL = "/jsp/claim/authorization/oemBalanceStopInfo.jsp";
	private final String Appprint_ALL = "/jsp/claim/authorization/AppbalancePrintAll.jsp";
	private final DealerNewKpUpdateDAO daoKP = DealerNewKpUpdateDAO
			.getInstance();

	/*
	 * 结算单管理
	 */
	public void balanceMainInit() {
		try {
			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId());
			act.setOutData("yieldly", yieldly);
			act.setOutData("auditMan", loginUser.getName());
			act.setForword(initUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 结算单审核(财务)
	 */
	public void financialMainInit() {
		try {
			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId());

			act.setOutData("yieldly", yieldly);
			act.setForword(financialInitUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void checkBalance() {
		try {
			String id = request.getParamValue("id");
			String type = request.getParamValue("type");
			BalanceMainDao dao = new BalanceMainDao();
			if (type.equals("0")) {
				StringBuffer sql = new StringBuffer();
				sql.append("UPDATE Tt_As_Wr_Claim_Balance_Fine t \n");
				sql.append("\n");
				sql.append("   SET t.Is_Rood=1,t.End_Date=SYSDATE  \n");
				sql.append(" WHERE 1 = 1\n");
				sql.append("   AND Remark = '" + id + "'");
				dao.update(sql.toString(), null);
			} else {
				StringBuffer sql = new StringBuffer();
				sql.append("update tt_as_wr_fine t\n");
				sql.append("   set t.BALANCE_ODER = null, t.PAY_STATUS = 11511001\n");
				sql.append(" where t.BALANCE_ODER =" + id);
				dao.update(sql.toString(), null);

				TtAsWrClaimBalanceFinePO balanceFinePO = new TtAsWrClaimBalanceFinePO();
				balanceFinePO.setRemark(id);
				dao.delete(balanceFinePO);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "确认失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 */
	public void commit_PAYMENT() {
		try {
			response.setContentType("text/html;charset=UTF-8");
			String dealerId=loginUser.getDealerId();
			String BALANCE_ODER = request.getParamValue("BALANCE_ODER");
			
			String CLAIM_AMOUNT_SUM = request.getParamValue("CLAIM_AMOUNT_SUM");
			String INVOICE = request.getParamValue("invoice_name1");
			String invoice_taxrate = request.getParamValue("invoice_taxrate");
			String[] LABOUR_RECEIPT = request.getParamValues("LABOUR_RECEIPT");
			String[] Project = request.getParamValues("Project");
			
			String[] PART_RECEIPT = request.getParamValues("PART_RECEIPT");
			String[] AMOUNT_OF_MONEY = request.getParamValues("AMOUNT_OF_MONEY");
			String[] AMOUNT_SUM = request.getParamValues("AMOUNT_SUM");
			String[] TAX_RATE_MONEY = request.getParamValues("TAX_RATE_MONEY");
			String[] REMARK = request.getParamValues("REMARK");
			String[] ID = request.getParamValues("ID");
			String SERIAL_NUMBER = request.getParamValue("SERIAL_NUMBER");
//			String FINE_AMOUNT_OF_MONEY = request
//					.getParamValue("FINE_AMOUNT_OF_MONEY");
//			String FINE_TAX_RATE_MONEY = request
//					.getParamValue("FINE_TAX_RATE_MONEY");
			String creatdate = request.getParamValue("creatdate");
			String STATUS = request.getParamValue("STATUS");
			
			TtAsPaymentPO paymentPO = new TtAsPaymentPO();
			paymentPO.setBalanceOder(BALANCE_ODER);
			BalanceMainDao dao = new BalanceMainDao();
			

			TtAsPaymentPO paymentPO1 = new TtAsPaymentPO();
			paymentPO1.setBalanceOder(BALANCE_ODER);
			paymentPO1.setCreatDate(new Date());
			
			
			
	//		Integer CLIAM_AMOUNT_SUM =0;
			for (int i = 0; i < AMOUNT_SUM.length; i++) {
				String id=ID[i];
				//设置Id
		//		CLIAM_AMOUNT_SUM=CLIAM_AMOUNT_SUM+ Integer.parseInt(AMOUNT_SUM[i]);
				if (Utility.testString(LABOUR_RECEIPT[i]))
					paymentPO1.setLabourReceipt(LABOUR_RECEIPT[i]);
				if (Utility.testString(PART_RECEIPT[i]))
					paymentPO1.setPartReceipt(PART_RECEIPT[i]);
				if (Utility.testString(AMOUNT_OF_MONEY[i]))
					paymentPO1.setAmountOfMoney(Double.parseDouble(AMOUNT_OF_MONEY[i]));
				if (Utility.testString(TAX_RATE_MONEY[i]))
					paymentPO1.setTaxRateMoney(Double.parseDouble(TAX_RATE_MONEY[i]));
				if (Utility.testString(REMARK[i]))
					paymentPO1.setRemark(REMARK[i]);
				if (Utility.testString(SERIAL_NUMBER))
					paymentPO1.setSerialNumber(SERIAL_NUMBER);
				if (Utility.testString(creatdate))
					paymentPO1.setCreatDate(Utility.getDate(creatdate, 1));
				if (Utility.testString(STATUS))
					paymentPO1.setStatus(Integer.parseInt(STATUS));
				if (Utility.testString(AMOUNT_SUM[i]))
					paymentPO1.setAmountSum(Double.parseDouble(AMOUNT_SUM[i]));
				
				paymentPO1.setProject(Project[i]);
//				if(Utility.testString(INVOICE)){
//					paymentPO1.setInvoice(INVOICE);
//				}
				List<TtAsPaymentPO> list=new ArrayList();
				if (id!=null  &&  (!id.equals(""))){
					paymentPO1.setId(Long.parseLong(ID[i]));
					paymentPO.setId(Long.parseLong(ID[i]));
					 list = dao.select(paymentPO);
				}
					
				
				if (list.size() > 0) {
					TtAsPaymentPO paymentPO2 = new TtAsPaymentPO();
					paymentPO2.setBalanceOder(BALANCE_ODER);
					paymentPO2.setId(Long.parseLong(ID[i]));
					paymentPO1.setId(Long.parseLong(ID[i]));
					dao.update(paymentPO2, paymentPO1);
				} else {
					paymentPO1.setId(Long.parseLong(SequenceManager.getSequence("")));
					paymentPO1.setCreateBy(String.valueOf(loginUser.getUserId()));
					dao.insert(paymentPO1);
				}
			}
			DealerNewKp kp = new DealerNewKp();
			kp.dealerKpQuerydlInit();
			TtAsWrClaimBalancePO cliam=new TtAsWrClaimBalancePO();
			cliam.setRemark(BALANCE_ODER);
			TtAsWrClaimBalancePO oldCliam=(TtAsWrClaimBalancePO) dao.select(cliam).get(0);
			oldCliam.setStatus(Integer.parseInt(STATUS));
			oldCliam.setInvoiceId(Long.parseLong(INVOICE));
			oldCliam.setSelectmentAmount(Double.parseDouble(CLAIM_AMOUNT_SUM));
			oldCliam.setInvoiceTaxrate(Long.parseLong(invoice_taxrate));
			dao.update(cliam, oldCliam);
			
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "开票出错请于管理员联系");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void commit_PAYMENT_TIKE() {

		try {

			String status = request.getParamValue("status");
			String STATUS = request.getParamValue("STATUS01");
			String BALANCE_ODER = request.getParamValue("BALANCE_ODER");
			TtAsPaymentPO paymentPO = new TtAsPaymentPO();
			paymentPO.setBalanceOder(BALANCE_ODER);
			String type = status;
			TtAsPaymentPO paymentPO1 = new TtAsPaymentPO();
			

			if (STATUS.equals("1")) {
				if (status.equals("1")) {
					paymentPO1.setCollectTickets(loginUser.getUserId());
					paymentPO1.setCollectTicketsDate(new Date());
				} else if (status.equals("2")) {
					paymentPO1.setCheckTickets(loginUser.getUserId());
					paymentPO1.setCheckTicketsDate(new Date());
				} else {
					paymentPO1.setTransferTickets(loginUser.getUserId());
					paymentPO1.setTransferTicketsDate(new Date());
				}
				status = "" + (Integer.parseInt(status) + 1);

			} else {
				status = "" + (Integer.parseInt(status) - 1);
			}
			paymentPO1.setStatus(Integer.parseInt(status));
			BalanceMainDao dao = new BalanceMainDao();
			dao.update(paymentPO, paymentPO1);
			DealerNewKp kp = new DealerNewKp();
			if (type.equals("1")) {
				kp.collectTickets();

			} else if (type.equals("2")) {
				kp.checkTickets();
			} else {
				kp.transferTickets();
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "开票出错请于管理员联系");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 结算单管理 根据页面参数查询
	 */
	public void BalanceMainQuery() {

		try {

			// CommonUtils.checkNull() 校验是否为空
			String dealerCode = CommonUtils.checkNull(request
					.getParamValue("dealerCode")); // 经销商代码
			String yieldly = CommonUtils.checkNull(request
					.getParamValue("yieldly")); // 产地代码
			String balanceNo = CommonUtils.checkNull(request
					.getParamValue("balanceNo")); // 结算单号
			String startDate = CommonUtils.checkNull(request
					.getParamValue("startDate")); // 创建开始时间
			String endDate = CommonUtils.checkNull(request
					.getParamValue("endDate")); // 创建结束时间
			String status = CommonUtils.checkNull(request
					.getParamValue("status")); // 状态
			String person = CommonUtils.checkNull(request
					.getParamValue("PERSON")); // 复核人
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId()); // 该用户拥有的产地权限
			// 当开始时间和结束时间相同时
			if (null != startDate && !"".equals(startDate) && null != endDate
					&& !"".equals(endDate)) {
				startDate = startDate + " 00:00:00";
				endDate = endDate + " 23:59:59";
			}
			auditBean bean = new auditBean();
			bean.setDealerCode(dealerCode);
			bean.setYieldly(yieldly);
			bean.setBalanceNo(balanceNo);
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setStatus(status);
			bean.setPerson(person);
			bean.setOemCompanyId(String.valueOf(GetOemcompanyId
					.getOemCompanyId(loginUser)));
			bean.setYieldlys(yieldlys);

			BalanceMainDao dao = new BalanceMainDao();
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryAccAuditList(bean,
					curPage, 30);
			// 分页方法 end
			act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void AppprintAll() {
		String BALANCE_ODER = CommonUtils.checkNull(request
				.getParamValue("BALANCE_ODER"));

		TtAsWrClaimBalancePO balancePO = new TtAsWrClaimBalancePO();
		balancePO.setRemark(BALANCE_ODER);
		BalanceMainDao dao = new BalanceMainDao();
		List<TtAsWrClaimBalancePO> balanceList = dao.select(balancePO);
		balancePO = balanceList.get(0);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		List[][] allList = new List[4][2];
		List nameList = new ArrayList();
		String[] valList = {"10661002","10661011","10661001","10661006"};
		
		for (int i=0;i<valList.length;i++) {
			String val = valList[i];
			if (val.equals("10661011")) { //单独为PDI展示到工时费的计算
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT t.CLAIM_TYPE, t.CLAIM_NO,nvl(FREE_M_PRICE,0) as BALANCE_NETITEM_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_AMOUNT,0) as BALANCE_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_PART_AMOUNT,0) as BALANCE_PART_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_LABOUR_AMOUNT,0) as BALANCE_LABOUR_AMOUNT,\n");
				sql.append("       nvl(t.FREE_PART_AMOUNT,0) as FREE_PART_AMOUNT,\n");
				sql.append("       nvl(t.FREE_LABOUR_AMOUNT,0) as FREE_LABOUR_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_NETITEM_AMOUNT,0) as FREE_M_PRICE,\n");
				sql.append("       nvl(t.ACCESSORIES_PRICE,0) as ACCESSORIES_PRICE,\n");
				sql.append("       nvl(t.COMPENSATION_MONEY,0) as COMPENSATION_MONEY\n");
				sql.append("  from TT_AS_WR_APPLICATION t\n");
				if (val.equals("10661001")) {
					sql.append(" where t.CLAIM_TYPE not in (10661002,10661006,10661011)");
				} else {
					sql.append(" where t.CLAIM_TYPE = " + val);
				}
				sql.append("   and t.DEALER_ID =   " + balancePO.getDealerId());
				sql.append("   and t.BALANCE_NO =   '" +BALANCE_ODER+"'");
				
				DaoFactory.getsql(sql, "t.status", "10791005", 7);
				List AppList = dao.pageQuery(sql.toString(), null,
						dao.getFunName());

				StringBuffer sb = new StringBuffer();
				sb.append("SELECT\n");
				sb.append("       sum(nvl(FREE_M_PRICE,0)) as BALANCE_NETITEM_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_AMOUNT,0)) as  BALANCE_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_PART_AMOUNT,0)) AS BALANCE_PART_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_LABOUR_AMOUNT,0)) AS BALANCE_LABOUR_AMOUNT_COUNT ,\n");
				sb.append("       sum(nvl(t.FREE_PART_AMOUNT,0)) AS FREE_PART_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.FREE_LABOUR_AMOUNT,0)) AS FREE_LABOUR_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_NETITEM_AMOUNT,0)) AS FREE_M_PRICE_COUNT,\n");
				sb.append("       sum(nvl(t.ACCESSORIES_PRICE,0)) AS ACCESSORIES_PRICE_COUNT,\n");
				sb.append("       sum(nvl(t.COMPENSATION_MONEY,0)) AS COMPENSATION_MONEY_COUNT\n");
				sb.append("  from TT_AS_WR_APPLICATION t \n");
				if (val.equals("10661001")) {
					sb.append(" where t.CLAIM_TYPE not in (10661002,10661006,10661011)");
				} else {
					sb.append(" where t.CLAIM_TYPE = " + val);
				}
				sb.append("   and t.DEALER_ID =   " + balancePO.getDealerId());
				sb.append("   and t.BALANCE_NO =   '" +BALANCE_ODER+"'");
				DaoFactory.getsql(sb, "t.status", "10791005", 7);
				List AppListTemp = dao.pageQuery(sb.toString(), null,
						dao.getFunName());
				if (val.equals("10661001")) {
					nameList.add("一般索赔");
//					act.setOutData("name", "一般索赔");
				} else if (val.equals("10661002")) {
					nameList.add("保养");
//					act.setOutData("name", "保养");
				} else if (val.equals("10661006")) {
					nameList.add("活动");
//					act.setOutData("name", "活动");
				} else {
					nameList.add("PDI");
//					act.setOutData("name", "PDI");
				}
				allList[i][0] = AppList;
				allList[i][1] = AppListTemp;
//				act.setOutData("AppList", AppList);
//				act.setOutData("AppListTemp", AppListTemp.get(0));
//				act.setForword(AppprintUrl);
			} else {
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT t.CLAIM_TYPE, t.CLAIM_NO,nvl(FREE_M_PRICE,0) as FREE_M_PRICE,\n");
				sql.append("       nvl(t.BALANCE_AMOUNT,0) as BALANCE_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_PART_AMOUNT,0) as BALANCE_PART_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_LABOUR_AMOUNT,0) as BALANCE_LABOUR_AMOUNT,\n");
				sql.append("       nvl(t.FREE_PART_AMOUNT,0) as FREE_PART_AMOUNT,\n");
				sql.append("       nvl(t.FREE_LABOUR_AMOUNT,0) as FREE_LABOUR_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_NETITEM_AMOUNT,0) as BALANCE_NETITEM_AMOUNT,\n");
				sql.append("       nvl(t.ACCESSORIES_PRICE,0) as ACCESSORIES_PRICE,\n");
				sql.append("       nvl(t.COMPENSATION_MONEY,0) as COMPENSATION_MONEY\n");
				sql.append("  from TT_AS_WR_APPLICATION t\n");
				if (val.equals("10661001")) {
					sql.append(" where t.CLAIM_TYPE not in (10661002,10661006,10661011)");
				} else {
					sql.append(" where t.CLAIM_TYPE = " + val);
				}
				sql.append("   and t.DEALER_ID =   " + balancePO.getDealerId());
				sql.append("   and t.BALANCE_NO =   '" +BALANCE_ODER+"'");
				DaoFactory.getsql(sql, "t.status", "10791005", 7);
				List AppList = dao.pageQuery(sql.toString(), null,
						dao.getFunName());

				StringBuffer sb = new StringBuffer();
				sb.append("SELECT\n");
				sb.append("       sum(nvl(FREE_M_PRICE,0)) as FREE_M_PRICE_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_AMOUNT,0)) as  BALANCE_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_PART_AMOUNT,0)) AS BALANCE_PART_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_LABOUR_AMOUNT,0)) AS BALANCE_LABOUR_AMOUNT_COUNT ,\n");
				sb.append("       sum(nvl(t.FREE_PART_AMOUNT,0)) AS FREE_PART_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.FREE_LABOUR_AMOUNT,0)) AS FREE_LABOUR_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_NETITEM_AMOUNT,0)) AS BALANCE_NETITEM_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.ACCESSORIES_PRICE,0)) AS ACCESSORIES_PRICE_COUNT,\n");
				sb.append("       sum(nvl(t.COMPENSATION_MONEY,0)) AS COMPENSATION_MONEY_COUNT\n");
				sb.append("  from TT_AS_WR_APPLICATION t \n");
				if (val.equals("10661001")) {
					sb.append(" where t.CLAIM_TYPE not in (10661002,10661006,10661011)");
				} else {
					sb.append(" where t.CLAIM_TYPE = " + val);
				}
				sb.append("   and t.DEALER_ID =   " + balancePO.getDealerId());
				sb.append("   and t.BALANCE_NO =   '" +BALANCE_ODER+"'");
				DaoFactory.getsql(sb, "t.status", "10791005", 7);
				List AppListTemp = dao.pageQuery(sb.toString(), null,
						dao.getFunName());
				if (val.equals("10661001")) {
					nameList.add("一般索赔");
//					act.setOutData("name", "一般索赔");
				} else if (val.equals("10661002")) {
					nameList.add("保养");
//					act.setOutData("name", "保养");
				} else if (val.equals("10661006")) {
					nameList.add("活动");
//					act.setOutData("name", "活动");
				} else {
					nameList.add("PDI");
//					act.setOutData("name", "PDI");
				}
				allList[i][0] = AppList;
				allList[i][1] = AppListTemp;
//				act.setOutData("AppList", AppList);
//				act.setOutData("AppListTemp", AppListTemp.get(0));
//				act.setForword(AppprintUrl);
			}
		}
		act.setOutData("allList", allList);
		act.setOutData("nameList", nameList);
		act.setForword(Appprint_ALL);
	}
	public void Appprint() {
		try {
			String val = CommonUtils.checkNull(request.getParamValue("val"));
			String BALANCE_ODER = CommonUtils.checkNull(request
					.getParamValue("BALANCE_ODER"));

			TtAsWrClaimBalancePO balancePO = new TtAsWrClaimBalancePO();
			balancePO.setRemark(BALANCE_ODER);
			BalanceMainDao dao = new BalanceMainDao();
			List<TtAsWrClaimBalancePO> balanceList = dao.select(balancePO);
			balancePO = balanceList.get(0);
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

			if (val.equals("10661011")) { //单独为PDI展示到工时费的计算
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT t.CLAIM_TYPE, t.CLAIM_NO,nvl(FREE_M_PRICE,0) as BALANCE_NETITEM_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_AMOUNT,0) as BALANCE_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_PART_AMOUNT,0) as BALANCE_PART_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_LABOUR_AMOUNT,0) as BALANCE_LABOUR_AMOUNT,\n");
				sql.append("       nvl(t.FREE_PART_AMOUNT,0) as FREE_PART_AMOUNT,\n");
				sql.append("       nvl(t.FREE_LABOUR_AMOUNT,0) as FREE_LABOUR_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_NETITEM_AMOUNT,0) as FREE_M_PRICE,\n");
				sql.append("       nvl(t.ACCESSORIES_PRICE,0) as ACCESSORIES_PRICE,\n");
				sql.append("       nvl(t.COMPENSATION_MONEY,0) as COMPENSATION_MONEY\n");
				sql.append("  from TT_AS_WR_APPLICATION t\n");
				if (val.equals("10661001")) {
					sql.append(" where t.CLAIM_TYPE not in (10661002,10661006,10661011)");
				} else {
					sql.append(" where t.CLAIM_TYPE = " + val);
				}
				sql.append("   and t.DEALER_ID =   " + balancePO.getDealerId());
				sql.append("   and t.BALANCE_NO =   '" +BALANCE_ODER+"'");
				
				DaoFactory.getsql(sql, "t.status", "10791005", 7);
				List AppList = dao.pageQuery(sql.toString(), null,
						dao.getFunName());

				StringBuffer sb = new StringBuffer();
				sb.append("SELECT\n");
				sb.append("       sum(nvl(FREE_M_PRICE,0)) as BALANCE_NETITEM_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_AMOUNT,0)) as  BALANCE_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_PART_AMOUNT,0)) AS BALANCE_PART_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_LABOUR_AMOUNT,0)) AS BALANCE_LABOUR_AMOUNT_COUNT ,\n");
				sb.append("       sum(nvl(t.FREE_PART_AMOUNT,0)) AS FREE_PART_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.FREE_LABOUR_AMOUNT,0)) AS FREE_LABOUR_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_NETITEM_AMOUNT,0)) AS FREE_M_PRICE_COUNT,\n");
				sb.append("       sum(nvl(t.ACCESSORIES_PRICE,0)) AS ACCESSORIES_PRICE_COUNT,\n");
				sb.append("       sum(nvl(t.COMPENSATION_MONEY,0)) AS COMPENSATION_MONEY_COUNT\n");
				sb.append("  from TT_AS_WR_APPLICATION t \n");
				if (val.equals("10661001")) {
					sb.append(" where t.CLAIM_TYPE not in (10661002,10661006,10661011)");
				} else {
					sb.append(" where t.CLAIM_TYPE = " + val);
				}
				sb.append("   and t.DEALER_ID =   " + balancePO.getDealerId());
				sb.append("   and t.BALANCE_NO =   '" +BALANCE_ODER+"'");
				DaoFactory.getsql(sb, "t.status", "10791005", 7);
				List AppListTemp = dao.pageQuery(sb.toString(), null,
						dao.getFunName());
				if (val.equals("10661001")) {
					act.setOutData("name", "一般索赔");
				} else if (val.equals("10661002")) {
					act.setOutData("name", "保养");
				} else if (val.equals("10661006")) {
					act.setOutData("name", "活动");
				} else {
					act.setOutData("name", "PDI");
				}
				act.setOutData("AppList", AppList);
				act.setOutData("AppListTemp", AppListTemp.get(0));
				act.setForword(AppprintUrl);
			} else {
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT t.CLAIM_TYPE, t.CLAIM_NO,nvl(FREE_M_PRICE,0) as FREE_M_PRICE,\n");
				sql.append("       nvl(t.BALANCE_AMOUNT,0) as BALANCE_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_PART_AMOUNT,0) as BALANCE_PART_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_LABOUR_AMOUNT,0) as BALANCE_LABOUR_AMOUNT,\n");
				sql.append("       nvl(t.FREE_PART_AMOUNT,0) as FREE_PART_AMOUNT,\n");
				sql.append("       nvl(t.FREE_LABOUR_AMOUNT,0) as FREE_LABOUR_AMOUNT,\n");
				sql.append("       nvl(t.BALANCE_NETITEM_AMOUNT,0) as BALANCE_NETITEM_AMOUNT,\n");
				sql.append("       nvl(t.ACCESSORIES_PRICE,0) as ACCESSORIES_PRICE,\n");
				sql.append("       nvl(t.COMPENSATION_MONEY,0) as COMPENSATION_MONEY\n");
				sql.append("  from TT_AS_WR_APPLICATION t\n");
				if (val.equals("10661001")) {
					sql.append(" where t.CLAIM_TYPE not in (10661002,10661006,10661011)");
				} else {
					sql.append(" where t.CLAIM_TYPE = " + val);
				}
				sql.append("   and t.DEALER_ID =   " + balancePO.getDealerId());
				sql.append("   and t.BALANCE_NO =   '" +BALANCE_ODER+"'");
				DaoFactory.getsql(sql, "t.status", "10791005", 7);
				List AppList = dao.pageQuery(sql.toString(), null,
						dao.getFunName());

				StringBuffer sb = new StringBuffer();
				sb.append("SELECT\n");
				sb.append("       sum(nvl(FREE_M_PRICE,0)) as FREE_M_PRICE_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_AMOUNT,0)) as  BALANCE_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_PART_AMOUNT,0)) AS BALANCE_PART_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_LABOUR_AMOUNT,0)) AS BALANCE_LABOUR_AMOUNT_COUNT ,\n");
				sb.append("       sum(nvl(t.FREE_PART_AMOUNT,0)) AS FREE_PART_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.FREE_LABOUR_AMOUNT,0)) AS FREE_LABOUR_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.BALANCE_NETITEM_AMOUNT,0)) AS BALANCE_NETITEM_AMOUNT_COUNT,\n");
				sb.append("       sum(nvl(t.ACCESSORIES_PRICE,0)) AS ACCESSORIES_PRICE_COUNT,\n");
				sb.append("       sum(nvl(t.COMPENSATION_MONEY,0)) AS COMPENSATION_MONEY_COUNT\n");
				sb.append("  from TT_AS_WR_APPLICATION t \n");
				if (val.equals("10661001")) {
					sb.append(" where t.CLAIM_TYPE not in (10661002,10661006,10661011)");
				} else {
					sb.append(" where t.CLAIM_TYPE = " + val);
				}
				sb.append("   and t.DEALER_ID =   " + balancePO.getDealerId());
				sb.append("   and t.BALANCE_NO =   '" +BALANCE_ODER+"'");
				DaoFactory.getsql(sb, "t.status", "10791005", 7);
				List AppListTemp = dao.pageQuery(sb.toString(), null,
						dao.getFunName());
				if (val.equals("10661001")) {
					act.setOutData("name", "一般索赔");
				} else if (val.equals("10661002")) {
					act.setOutData("name", "保养");
				} else if (val.equals("10661006")) {
					act.setOutData("name", "活动");
				} else {
					act.setOutData("name", "PDI");
				}
				act.setOutData("AppList", AppList);
				act.setOutData("AppListTemp", AppListTemp.get(0));
				act.setForword(AppprintUrl);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 经销商查看开票通知
	 */
	public void delaerBalanceMainQuery() {

		try {

			// CommonUtils.checkNull() 校验是否为空
			String yieldly = CommonUtils.checkNull(request
					.getParamValue("yieldly")); // 产地代码
			String balanceNo = CommonUtils.checkNull(request
					.getParamValue("balanceNo")); // 结算单号
			String startDate = CommonUtils.checkNull(request
					.getParamValue("startDate")); // 创建开始时间
			String endDate = CommonUtils.checkNull(request
					.getParamValue("endDate")); // 创建结束时间
			// 当开始时间和结束时间相同时
			if (null != startDate && !"".equals(startDate) && null != endDate
					&& !"".equals(endDate)) {
				startDate = startDate + " 00:00:00";
				endDate = endDate + " 23:59:59";
			}
			auditBean bean = new auditBean();
			bean.setYieldly(yieldly);
			bean.setBalanceNo(balanceNo);
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setDealerCode(loginUser.getDealerId());
			bean.setOemCompanyId(String.valueOf(GetOemcompanyId
					.getOemCompanyId(loginUser)));

			BalanceMainDao dao = new BalanceMainDao();
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryDealerBalanceList(
					bean, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void oemBalanceMainQuery() {

		try {

			// CommonUtils.checkNull() 校验是否为空
			String yieldly = CommonUtils.checkNull(request
					.getParamValue("yieldly")); // 产地代码
			String balanceNo = CommonUtils.checkNull(request
					.getParamValue("balanceNo")); // 结算单号
			String startDate = CommonUtils.checkNull(request
					.getParamValue("startDate")); // 创建开始时间
			String endDate = CommonUtils.checkNull(request
					.getParamValue("endDate")); // 创建结束时间
			String dealerId = CommonUtils.checkNull(request
					.getParamValue("dealerId")); // 经销商ID
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId()); // 该用户拥有的产地权限
			// 当开始时间和结束时间相同时
			if (null != startDate && !"".equals(startDate) && null != endDate
					&& !"".equals(endDate)) {
				startDate = startDate + " 00:00:00";
				endDate = endDate + " 23:59:59";
			}
			auditBean bean = new auditBean();
			bean.setYieldly(yieldly);
			bean.setBalanceNo(balanceNo);
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setDealerId(dealerId);
			bean.setOemCompanyId(String.valueOf(GetOemcompanyId
					.getOemCompanyId(loginUser)));
			bean.setYieldlys(yieldlys);

			BalanceMainDao dao = new BalanceMainDao();
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryOemBalanceList(bean,
					curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void oemBalanceMainQueryStop() {

		try {

			// CommonUtils.checkNull() 校验是否为空
			String yieldly = CommonUtils.checkNull(request
					.getParamValue("yieldly")); // 产地代码
			String balanceNo = CommonUtils.checkNull(request
					.getParamValue("balanceNo")); // 结算单号
			String startDate = CommonUtils.checkNull(request
					.getParamValue("startDate")); // 创建开始时间
			String endDate = CommonUtils.checkNull(request
					.getParamValue("endDate")); // 创建结束时间
			String dealerId = CommonUtils.checkNull(request
					.getParamValue("dealerId")); // 经销商ID
			String status = CommonUtils.checkNull(request
					.getParamValue("status")); // 是否结算
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId()); // 该用户拥有的产地权限
			// 当开始时间和结束时间相同时
			if (null != startDate && !"".equals(startDate) && null != endDate
					&& !"".equals(endDate)) {
				startDate = startDate + " 00:00:00";
				endDate = endDate + " 23:59:59";
			}
			auditBean bean = new auditBean();
			bean.setYieldly(yieldly);
			bean.setBalanceNo(balanceNo);
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setDealerId(dealerId);
			bean.setStatus(status);
			bean.setOemCompanyId(String.valueOf(GetOemcompanyId
					.getOemCompanyId(loginUser)));
			bean.setYieldlys(yieldlys);

			BalanceMainDao dao = new BalanceMainDao();
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryOemBalanceListStop(
					bean, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 结算室审核 根据页面参数查询
	 */
	public void financialMainQuery() {

		try {

			// CommonUtils.checkNull() 校验是否为空
			String dealerCode = CommonUtils.checkNull(request
					.getParamValue("dealerCode")); // 经销商代码
			String yieldly = CommonUtils.checkNull(request
					.getParamValue("yieldly")); // 产地代码
			String balanceNo = CommonUtils.checkNull(request
					.getParamValue("balanceNo")); // 结算单号
			String startDate = CommonUtils.checkNull(request
					.getParamValue("startDate")); // 创建开始时间
			String endDate = CommonUtils.checkNull(request
					.getParamValue("endDate")); // 创建结束时间
			String status = CommonUtils.checkNull(request
					.getParamValue("status")); // 状态
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId()); // 该用户拥有的产地权限

			/********* iverson add by 2011-01-20 *****************************/
			String reviewBy = CommonUtils.checkNull(request
					.getParamValue("reviewBy"));
			String reviewBeginDate = CommonUtils.checkNull(request
					.getParamValue("reviewBeginDate"));
			String reviewEndDate = CommonUtils.checkNull(request
					.getParamValue("reviewEndDate"));
			if (null != reviewBeginDate && !"".equals(reviewBeginDate)
					&& null != reviewEndDate && !"".equals(reviewEndDate)) {
				reviewBeginDate = reviewBeginDate + " 00:00:00";
				reviewEndDate = reviewEndDate + " 23:59:59";
			}
			/********* iverson add by 2011-01-20 *****************************/

			// 当开始时间和结束时间相同时
			if (null != startDate && !"".equals(startDate) && null != endDate
					&& !"".equals(endDate)) {
				startDate = startDate + " 00:00:00";
				endDate = endDate + " 23:59:59";
			}
			auditBean bean = new auditBean();
			bean.setDealerCode(dealerCode);
			bean.setYieldly(yieldly);
			bean.setBalanceNo(balanceNo);
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setStatus(status);
			/********* iverson add by 2011-01-20 *****************************/
			bean.setReviewBy(reviewBy);
			bean.setReviewBeginDate(reviewBeginDate);
			bean.setReviewEndDate(reviewEndDate);
			/********* iverson add by 2011-01-20 *****************************/
			bean.setOemCompanyId(String.valueOf(GetOemcompanyId
					.getOemCompanyId(loginUser)));
			bean.setYieldlys(yieldlys);

			BalanceMainDao dao = new BalanceMainDao();
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryFinancialMain(bean,
					curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps);// 向前台传的list 名称是固定的不可改必须用 ps
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 查看结算单明细
	 */
	public void queryBalanceInfo() {

		try {

			// CommonUtils.checkNull() 校验是否为空
			String id = request.getParamValue("id");
			BalanceMainDao dao = new BalanceMainDao();
			Map<String, Object> map = dao.getBalanceMainMapView(id);// 结算单主表信息
			List<Map<String, Object>> list = dao.getBalanceMainList(id);// 结算单子表信息
			// countMarketFee(map, act);//市场工单处理费用
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setForword(infoUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 复核申请打印
	 */
	public void queryPrintInfo() {

		try {

			// CommonUtils.checkNull() 校验是否为空
			String id = request.getParamValue("id");
			BalanceMainDao dao = new BalanceMainDao();
			Map<String, Object> map = dao.getBalancePrintMap(id);// 结算单主表信息
			List<Map<String, Object>> list = dao.getBalanceMainList1(id);// 结算单子表信息
			List l = dao.getDate(id);
			if (l.size() > 0) {
				act.setOutData("audit", (TtAsWrBalanceAuthitemPO) l.get(0));
			}
			TcCodePO code = new TcCodePO();
			code.setType("8008");
			List listCode = dao.select(code);
			if (listCode.size() > 0) {
				code = (TcCodePO) listCode.get(0);

				act.setOutData("code", code.getCodeId());

			}
			// countMarketFee(map, act);//市场工单处理费用
			act.setOutData("map", map);
			act.setOutData("list", list);

			// zhumingwei 2011-10-29 ADD 随机抽查功能
			TtAsDealerCheckPO poCheck = new TtAsDealerCheckPO();
			poCheck.setDealerId(Long.parseLong(loginUser.getDealerId()));
			poCheck.setYieldly(Long.parseLong(map.get("YIELDLY").toString()));
			poCheck.setStatus(Constant.STATUS_ENABLE);
			TtAsDealerCheckPO poCheckValue = (TtAsDealerCheckPO) daoKP.select(
					poCheck).get(0);
			Date lastDate = poCheckValue.getLastCheckDate();// 上次抽查时间
			Calendar calendar = Calendar.getInstance();// 公用类，加年月日的
			calendar.setTime(lastDate);
			calendar.add(Calendar.DAY_OF_MONTH, 1);// 当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
			lastDate = calendar.getTime();// 得到加一天后的值
			int checkCount = poCheckValue.getCheckCount();// 达到此抽查数量
			String checkPercentage = poCheckValue.getCheckPercentage();// 抽查百分比
			Date CON_END_DAY = (Date) map.get("END_DATE");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<Map<String, Object>> count = daoKP.getApplicationCount(
					sdf.format(lastDate), sdf.format(CON_END_DAY),
					loginUser.getDealerId(),
					Integer.parseInt(map.get("YIELDLY").toString()));
			Map mapCount = (Map) count.get(0);
			int countCon = ((BigDecimal) mapCount.get("COUNT")).intValue();// 得到符合条件查询出来的数量
			if (countCon > checkCount) {// 跟抽查数量对比，如果大于抽查数量，开始抽查
				int con = (int) Math
						.ceil((Integer.parseInt(checkPercentage) * countCon) * 0.01);// 得到需要抽查的数量
				act.setOutData("con", con);
			} else {
				act.setOutData("con", 0);
			}
			act.setOutData("codeType", code.getCodeId());
			// zhumingwei 2011-10-29 ADD 随机抽查功能

			act.setForword(printUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 复核申请 2010-11-04 修改 特殊外出费用和市场工单费用 已经在结算单生成时统计 现在不再继续统计
	 */
	public void queryBalanceAudit() {

		try {

			// CommonUtils.checkNull() 校验是否为空
			String id = request.getParamValue("id");
			BalanceMainDao dao = new BalanceMainDao();
			Map<String, Object> map = dao.getBalanceMainMap(id);// 结算单主表信息
			List<Map<String, Object>> list = dao.getBalanceMainList(id);// 结算单子表信息
			// countMarketFee(map, act);//市场工单处理费用
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setForword(auditApplyUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "复核申请");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: countMarketFee
	 * @Description: TODO(计算市场工单处理费用和总费用)
	 * @param @param map 主表信息
	 * @param @param act 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("unused")
	private void countMarketFee(Map<String, Object> map, ActionContext act) {
		BalanceMainDao dao = new BalanceMainDao();
		if (null != map && map.size() > 0) {
			TtIfMarketBean bean = new TtIfMarketBean();
			bean.setDealer_id(Long.parseLong(map.get("DEALER_ID").toString()));
			Map<String, Object> money = dao.getMarketFee(bean);
			act.setOutData("money", money);
			// 总金额=原来总金额+市场工单处理费用
			double amountSum = Double.parseDouble(String.valueOf(map
					.get("AMOUNT_SUM")));
			double sumMoney = Double.parseDouble(String.valueOf(money
					.get("SUM_MONEY") != null ? money.get("SUM_MONEY") : 0));
			double totalPrice = amountSum + sumMoney;
			map.put("AMOUNT_SUM", totalPrice);
		}
	}

	/*
	 * 结算单明细 财务审核
	 */
	public void queryFinancialInfo() {

		try {

			// CommonUtils.checkNull() 校验是否为空
			String id = request.getParamValue("id");
			BalanceMainDao dao = new BalanceMainDao();
			Map<String, Object> map = dao.getBalanceMainMapView(id);// 结算单主表信息
			List<Map<String, Object>> list = dao.getBalanceMainList(id);// 结算单子表信息
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setForword(finInfoUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单财务室审核");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 结算单明细 财务室
	 */
	public void queryFinancialInfoView() {

		try {
			BalanceMainDao dao = new BalanceMainDao();

			String dealer_id = request.getParamValue("id");
			String startDate = request.getParamValue("START_DATE");
			String endDate = request.getParamValue("END_DATE");

			Map<String, Object> mapdel = dao.queryDealerMes(dealer_id);
			
			Map<String, Object> mapCLAIM = dao.queryCLAIM(dealer_id, startDate,
					endDate);
			String REMARK = mapCLAIM.get("REMARK").toString();
			Map<String, Object> mapApp = dao.queryApption(dealer_id,REMARK);
			TtAsPaymentPO paymentPO = new TtAsPaymentPO();
			paymentPO.setBalanceOder(REMARK);

			List<TtAsPaymentPO> list = dao.select(paymentPO);
			double labourAccount = Arith.sub(Double.parseDouble(mapCLAIM.get(
					"LABOUR_ACCOUT").toString()), Double.parseDouble(mapApp
					.get("CLAIM_TYPE_AMOUNT_FREE_02").toString()));
			int nature = 0;
			int STATUS = 0;
			Date date = new Date();
			if (mapdel.get("TAXPAYER_NATURE").toString().equals("一般纳税人")) {
				nature = 1;
				if (list.size() > 0) {
					paymentPO = list.get(0);
					STATUS = paymentPO.getStatus();
					act.setOutData("AMOUNT_OF_MONEY",
							paymentPO.getAmountOfMoney());
					act.setOutData("TAX_RATE_MONEY",
							paymentPO.getTaxRateMoney());
					act.setOutData("FINE_AMOUNT_OF_MONEY",
							paymentPO.getFineAmountOfMoney());
					act.setOutData("FINE_TAX_RATE_MONEY",
							paymentPO.getFineTaxRateMoney());

					act.setOutData("REMARK", paymentPO.getRemark());
					act.setOutData("SERIAL_NUMBER", paymentPO.getSerialNumber());
					act.setOutData("LABOUR_RECEIPT",
							paymentPO.getLabourReceipt());
					act.setOutData("PART_RECEIPT", paymentPO.getPartReceipt());

					date = paymentPO.getCreatDate();

				} else {
					act.setOutData("AMOUNT_OF_MONEY",
							mapCLAIM.get("AMOUNT_OF_MONEY").toString());
					act.setOutData("TAX_RATE_MONEY",
							mapCLAIM.get("TAX_RATE_MONEY").toString());
					act.setOutData("FINE_AMOUNT_OF_MONEY",
							mapCLAIM.get("FINE_AMOUNT_OF_MONEY").toString());
					act.setOutData("FINE_TAX_RATE_MONEY",
							mapCLAIM.get("FINE_TAX_RATE_MONEY").toString());
				}

			} else {
				if (list.size() > 0) {
					paymentPO = list.get(0);
					STATUS = paymentPO.getStatus();
					act.setOutData("AMOUNT_OF_MONEY",
							paymentPO.getAmountOfMoney());
					act.setOutData("TAX_RATE_MONEY",
							paymentPO.getTaxRateMoney());
					act.setOutData("FINE_AMOUNT_OF_MONEY",
							paymentPO.getFineAmountOfMoney());
					act.setOutData("FINE_TAX_RATE_MONEY",
							paymentPO.getFineTaxRateMoney());
					act.setOutData("REMARK", paymentPO.getRemark());
					act.setOutData("SERIAL_NUMBER", paymentPO.getSerialNumber());
					act.setOutData("LABOUR_RECEIPT",
							paymentPO.getLabourReceipt());
					act.setOutData("PART_RECEIPT", paymentPO.getPartReceipt());
					date = paymentPO.getCreatDate();
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			act.setOutData("date", sdf.format(date));
			act.setOutData("STATUS", STATUS);
			act.setOutData("nature", nature);
			act.setOutData("mapdel", mapdel);
			act.setOutData("mapApp", mapApp);
			act.setOutData("mapCLAIM", mapCLAIM);
			act.setOutData("labourAccount", labourAccount);
			act.setForword(finViewUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void queryFineInfoPrint() {

		try {
			BalanceMainDao dao = new BalanceMainDao();

			String REMARK = request.getParamValue("id");
			TtAsWrFinePO finePO = new TtAsWrFinePO();
			finePO.setBalanceOder(REMARK);
			act.setOutData("leng", dao.select(finePO).size());

			List<Map<String, Object>> yldlist = dao
					.queryDealerBalanceList02(REMARK);
			Map<String, Object> map = yldlist.get(0);
			String startDate = map.get("CREATE_DATE").toString();
			String DEALER_CODE = map.get("DEALER_CODE").toString();
			List<BalanceYieldlyBean> delist = dao.getPartBillDefine("1");
			String sbNO = DEALER_CODE + startDate.split("-")[0]
					+ startDate.split("-")[1];
			act.setOutData("yldlist", yldlist);
			act.setOutData("delist", delist);
			act.setOutData("sbNO", sbNO);
			act.setOutData("map", map);

			StringBuffer sql = new StringBuffer();
			sql.append("SELECT min(t.DEALER_CODE) DEALER_CODE,min(t.CREATE_DATE) CREATE_DATE,  \n");
			sql.append("       sum(t.PLUS_MINUS_LABOUR_SUM) PLUS_MINUS_LABOUR_SUM,\n");
			sql.append("       sum(t.PLUS_MINUS_DATUM_SUM) PLUS_MINUS_DATUM_SUM,\n");
			sql.append(" sum(t.PLUS_MINUS_DATUM_SUM)*0.9 PLUS_MINUS_DATUM_SUM1, ");
			sql.append(" (sum(t.PLUS_MINUS_DATUM_SUM)*0.9 + sum(t.PLUS_MINUS_LABOUR_SUM) ) AOUNT  ");
			sql.append("  from TT_AS_WR_CLAIM_BALANCE_FINE t\n");
			sql.append(" where t.REMARK = '" + REMARK + "'\n");
			sql.append(" GROUP by t.REMARK");
			List<Map<String, Object>> list = dao.pageQuery(sql.toString(),
					null, dao.getFunName());
			Map<String, Object> finemap = list.get(0);
			act.setOutData("finemap", finemap);
			act.setForword(finViewPrintUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void queryFinancialInfoView08() {

		try {
			BalanceMainDao dao = new BalanceMainDao();

			String dealer_id = request.getParamValue("id");
			String startDate = request.getParamValue("START_DATE");
			String endDate = request.getParamValue("END_DATE");
			String BALANCE_YIELDLY = request.getParamValue("BALANCE_YIELDLY");

			auditBean bean = new auditBean();
			if (null != startDate && !"".equals(startDate) && null != endDate
					&& !"".equals(endDate)) {
				startDate = startDate + " 00:00:00";
				endDate = endDate + " 23:59:59";
			}
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setDealerCode(dealer_id);
			bean.setYieldly(BALANCE_YIELDLY);
			List<Map<String, Object>> list = dao.queryDealerBalanceList01(bean);
			List<Map<String, Object>> sendlist = dao
					.queryDealerBalanceSendList01(bean);
			List<Map<String, Object>> yldlist = dao
					.queryDealerBalanceList02(bean);
			List<Map<String, Object>> alldlist = dao
					.queryDealerBalanceList03(bean);
			List<BalanceYieldlyBean> delist = dao.getPartBillDefine(dealer_id);
			List<Map<String, Object>> maps = dao.getClaimtypeCount(bean);

			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE tt_as_wr_claim_balance t set t.REVIEW_APPLICATION_TIME =? where t.REMARK ='"
					+ yldlist.get(0).get("REMARK").toString() + "'");
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(new Date());
			dao.update(sql.toString(), paramList);
			List<Map<String, Object>> dealist = dao.dealerBalanOrder(dealer_id,
					startDate, endDate, BALANCE_YIELDLY);
			List<Map<String, Object>> discount = dao.dealerDiscountOrder(
					dealer_id, startDate, endDate, BALANCE_YIELDLY);
			Map<String, Object> timap = dao.gettitkes(dealer_id,
					startDate.split(" ")[0].substring(0, 7),
					endDate.split(" ")[0].substring(0, 7), BALANCE_YIELDLY);
			Map<String, Object> tiorder = dao.getorder(dealer_id, startDate,
					endDate, BALANCE_YIELDLY);
			act.setOutData("dealist", dealist);
			if (discount.size() > 0) {
				act.setOutData("discount", discount.get(0));
			}
			act.setOutData("dealist", dealist);
			act.setOutData("timap", timap);
			act.setOutData("tiorder", tiorder);

			Double onedealerl = Double.parseDouble(list.get(0)
					.get("ALL_LABOUR_AMOUNT").toString());
			Double onedealerp = Double.parseDouble(list.get(0)
					.get("ALL_PART_AMOUNT").toString());

			if (sendlist.size() > 0) {
				for (Map<String, Object> map : sendlist) {
					onedealerl = Arith.sub(onedealerl, Double.parseDouble(map
							.get("SERVICE_CHARGE").toString()));
					onedealerp = Arith.sub(onedealerp, Double.parseDouble(map
							.get("MATERIAL_CHARGE").toString()));
				}
			}

			act.setOutData("onedealerl", onedealerl);
			act.setOutData("onedealerp", onedealerp);
			int tatol1 = 0;
			int tatol2 = 0;
			int tatol3 = 0;
			int tatol4 = 0;
			int tatol5 = 0;
			int tatol6 = 0;
			int tatol7 = 0;
			for (Map<String, Object> map : maps) {
				if (map.get("CLAIM_TYPE").toString().equals("10661001")) {
					tatol2 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol2;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661002")) {
					tatol3 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol3;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661007")) {
					tatol4 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol4;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661009")) {
					tatol5 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol5;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661010")) {
					tatol6 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol6;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661006")) {
					tatol7 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol7;
				}
			}

			act.setOutData("tatol1", tatol1);
			act.setOutData("tatol2", tatol2);
			act.setOutData("tatol3", tatol3);
			act.setOutData("tatol4", tatol4);
			act.setOutData("tatol5", tatol5);
			act.setOutData("tatol6", tatol6);
			act.setOutData("tatol7", tatol7);
			TcUserPO userPO = new TcUserPO();
			userPO.setUser_id(loginUser.getUserId());
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(Long.parseLong(dealer_id));
			List<TmDealerPO> poList = dao.select(dealerPO);
			dealerPO = poList.get(0);
			String dealerName = dealerPO.getDealerName();
			String a = startDate.split("-")[1];
			String b = endDate.split("-")[1];
			String sbNO = "";
			if (a.equals(b)) {
				sbNO = dealerPO.getDealerCode() + startDate.split("-")[0] + a;
			} else {
				sbNO = dealerPO.getDealerCode() + startDate.split("-")[0] + a
						+ "-" + b;
			}
			act.setOutData("map", list.get(0));
			act.setOutData("sendlist", sendlist);
			act.setOutData("yldlist", yldlist);
			act.setOutData("allmap", alldlist.get(0));
			act.setOutData("delist", delist);
			act.setOutData("dealerName", dealerName);
			act.setOutData("sbNO", sbNO);
			if (BALANCE_YIELDLY.equals("95411001")) {
				act.setForword(finViewUrl);
			} else {
				act.setForword(finViewUrldao);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void queryFinancialInfoView07() {

		try {
			BalanceMainDao dao = new BalanceMainDao();

			String dealer_id = request.getParamValue("id");
			String startDate = request.getParamValue("START_DATE");
			String endDate = request.getParamValue("END_DATE");

			Map<String, Object> mapdel = dao.queryDealerMes(dealer_id);
			
			Map<String, Object> mapCLAIM = dao.queryCLAIM(dealer_id, startDate,endDate);
			String REMARK = mapCLAIM.get("REMARK").toString();
			//==========zyw 把二次入库补偿服务站的钱加到结算总金额里 2014-9-10
			Double compensationDealerMoney=0d;
			DealerKpDao dealerkpdao = new DealerKpDao();
			List<Map<String, Object>> listMap=dealerkpdao.findcompensationBydealer(dealer_id,REMARK);
			if(listMap!=null&&listMap.size()>0){
				Map<String, Object> map = listMap.get(0);
				BigDecimal  bigdecimal= (BigDecimal) map.get("AMOUNT");
				compensationDealerMoney=bigdecimal.doubleValue();
				BigDecimal  otheraccount=(BigDecimal) mapCLAIM.get("OTHERACCOUNT");
				Double otheraccountTemp = otheraccount.doubleValue();
				mapCLAIM.put("OTHERACCOUNT", Arith.add(otheraccountTemp, compensationDealerMoney));//把补偿服务站的钱放到其他费用里
			}
			//=============================================
			
			Map<String, Object> mapApp = dao.queryApption(dealer_id,REMARK);
			TtAsPaymentPO paymentPO = new TtAsPaymentPO();
			paymentPO.setBalanceOder(REMARK);

			List<TtAsPaymentPO> list = dao.select(paymentPO);
			double labourAccount = Arith.sub(Double.parseDouble(mapCLAIM.get(
					"LABOUR_ACCOUT").toString()), Double.parseDouble(mapApp
					.get("CLAIM_TYPE_AMOUNT_FREE_02").toString()));
			int nature = 0;
			int STATUS = 0;
			Date date = new Date();
			if (mapdel.get("TAXPAYER_NATURE").toString().equals("一般纳税人")) {
				nature = 1;
				if (list.size() > 0) {
					paymentPO = list.get(0);
					STATUS = paymentPO.getStatus();
					act.setOutData("AMOUNT_OF_MONEY",
							paymentPO.getAmountOfMoney());
					act.setOutData("TAX_RATE_MONEY",
							paymentPO.getTaxRateMoney());
					act.setOutData("FINE_AMOUNT_OF_MONEY",
							paymentPO.getFineAmountOfMoney());
					act.setOutData("FINE_TAX_RATE_MONEY",
							paymentPO.getFineTaxRateMoney());

					act.setOutData("REMARK", paymentPO.getRemark());
					act.setOutData("SERIAL_NUMBER", paymentPO.getSerialNumber());
					act.setOutData("LABOUR_RECEIPT",
							paymentPO.getLabourReceipt());
					act.setOutData("PART_RECEIPT", paymentPO.getPartReceipt());

					date = paymentPO.getCreatDate();

				} else {
					act.setOutData("AMOUNT_OF_MONEY",
							mapCLAIM.get("AMOUNT_OF_MONEY").toString());
					act.setOutData("TAX_RATE_MONEY",
							mapCLAIM.get("TAX_RATE_MONEY").toString());
					act.setOutData("FINE_AMOUNT_OF_MONEY",
							mapCLAIM.get("FINE_AMOUNT_OF_MONEY").toString());
					act.setOutData("FINE_TAX_RATE_MONEY",
							mapCLAIM.get("FINE_TAX_RATE_MONEY").toString());
				}

			} else {
				if (list.size() > 0) {
					paymentPO = list.get(0);
					STATUS = paymentPO.getStatus();
					act.setOutData("AMOUNT_OF_MONEY",
							paymentPO.getAmountOfMoney());
					act.setOutData("TAX_RATE_MONEY",
							paymentPO.getTaxRateMoney());
					act.setOutData("FINE_AMOUNT_OF_MONEY",
							paymentPO.getFineAmountOfMoney());
					act.setOutData("FINE_TAX_RATE_MONEY",
							paymentPO.getFineTaxRateMoney());
					act.setOutData("REMARK", paymentPO.getRemark());
					act.setOutData("SERIAL_NUMBER", paymentPO.getSerialNumber());
					act.setOutData("LABOUR_RECEIPT",
							paymentPO.getLabourReceipt());
					act.setOutData("PART_RECEIPT", paymentPO.getPartReceipt());
					date = paymentPO.getCreatDate();
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			act.setOutData("date", sdf.format(date));
			act.setOutData("STATUS", STATUS);
			act.setOutData("nature", nature);
			act.setOutData("mapdel", mapdel);
			act.setOutData("mapApp", mapApp);
			act.setOutData("mapCLAIM", mapCLAIM);
			act.setOutData("labourAccount", labourAccount);
			act.setForword(finViewUrl01);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 开票的明细数据
	 */
	public void queryFinancialInfoView01() {

		try {
			BalanceMainDao dao = new BalanceMainDao();

			String dealer_id = request.getParamValue("id");
			String startDate = request.getParamValue("START_DATE");
			String endDate = request.getParamValue("END_DATE");
			String typesta = request.getParamValue("typesta");

			Map<String, Object> mapdel = dao.queryDealerMes(dealer_id);
			
			Map<String, Object> mapCLAIM = dao.queryCLAIM(dealer_id, startDate,endDate);
			String REMARK = mapCLAIM.get("REMARK").toString();
			Map<String, Object> mapApp = dao.queryApption(dealer_id,REMARK);
			TtAsPaymentPO paymentPO = new TtAsPaymentPO();
			paymentPO.setBalanceOder(REMARK);

			List<TtAsPaymentPO> list = dao.select(paymentPO);
			double labourAccount = Arith.sub(Double.parseDouble(mapApp.get(
					"CLAIM_TYPE_AMOUNT_01").toString()), Double.parseDouble(mapApp
					.get("CLAIM_TYPE_AMOUNT_11").toString()));
			double othersPrice = Arith.sub(Double.parseDouble(mapCLAIM.get("AMOUNT_SUM").toString()), Double.parseDouble(mapCLAIM.get(
							"PART_ACCOUNT").toString()));
			othersPrice= Arith.add(othersPrice, Double.parseDouble(mapCLAIM.get("PLUS_MINUS_SUM").toString()));
			int nature = 0;
			int STATUS = 0;
			Date date = new Date();
			if (mapdel.get("TAXPAYER_NATURE").toString().equals("一般纳税人")) {
				nature = 1;
				if (list.size() > 0) {
					paymentPO = list.get(0);
					STATUS = paymentPO.getStatus();
					act.setOutData("AMOUNT_OF_MONEY",
							paymentPO.getAmountOfMoney());
					act.setOutData("TAX_RATE_MONEY",
							paymentPO.getTaxRateMoney());
					act.setOutData("FINE_AMOUNT_OF_MONEY",
							paymentPO.getFineAmountOfMoney());
					act.setOutData("FINE_TAX_RATE_MONEY",
							paymentPO.getFineTaxRateMoney());

					act.setOutData("REMARK", paymentPO.getRemark());
					act.setOutData("SERIAL_NUMBER", paymentPO.getSerialNumber());
					act.setOutData("LABOUR_RECEIPT",
							paymentPO.getLabourReceipt());
					act.setOutData("PART_RECEIPT", paymentPO.getPartReceipt());

					date = paymentPO.getCreatDate();

				} else {
					act.setOutData("AMOUNT_OF_MONEY",
							mapCLAIM.get("AMOUNT_OF_MONEY").toString());
					act.setOutData("TAX_RATE_MONEY",
							mapCLAIM.get("TAX_RATE_MONEY").toString());
					act.setOutData("FINE_AMOUNT_OF_MONEY",
							mapCLAIM.get("FINE_AMOUNT_OF_MONEY").toString());
					act.setOutData("FINE_TAX_RATE_MONEY",
							mapCLAIM.get("FINE_TAX_RATE_MONEY").toString());
				}

			} else {
				if (list.size() > 0) {
					paymentPO = list.get(0);
					STATUS = paymentPO.getStatus();
					act.setOutData("AMOUNT_OF_MONEY",
							paymentPO.getAmountOfMoney());
					act.setOutData("TAX_RATE_MONEY",
							paymentPO.getTaxRateMoney());
					act.setOutData("FINE_AMOUNT_OF_MONEY",
							paymentPO.getFineAmountOfMoney());
					act.setOutData("FINE_TAX_RATE_MONEY",
							paymentPO.getFineTaxRateMoney());
					act.setOutData("REMARK", paymentPO.getRemark());
					act.setOutData("SERIAL_NUMBER", paymentPO.getSerialNumber());
					act.setOutData("LABOUR_RECEIPT",
							paymentPO.getLabourReceipt());
					act.setOutData("PART_RECEIPT", paymentPO.getPartReceipt());
					date = paymentPO.getCreatDate();
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			act.setOutData("date", sdf.format(date));
			act.setOutData("STATUS", STATUS);
			act.setOutData("nature", nature);
			act.setOutData("mapdel", mapdel);
			act.setOutData("mapApp", mapApp);
			act.setOutData("typesta", typesta);
			act.setOutData("mapCLAIM", mapCLAIM);
			act.setOutData("labourAccount", labourAccount);
			act.setOutData("othersPrice", othersPrice);
			act.setForword(finViewUrl01);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: deleteFinancialInfoViewPage 
	* @author: xyfue
	* @Description: 撤销结算数据页面
	* @param     设定文件 
	* @date 2014年10月17日 下午2:14:17 
	* @return void    返回类型 
	* @throws
	 */
	public void deleteFinancialInfoViewPage() {

		try {
			BalanceMainDao dao = new BalanceMainDao();

			String dealer_id = request.getParamValue("id");
			String startDate = request.getParamValue("START_DATE");
			String endDate = request.getParamValue("END_DATE");
			String typesta = request.getParamValue("typesta");

			Map<String, Object> mapdel = dao.queryDealerMes(dealer_id);
			
			Map<String, Object> mapCLAIM = dao.queryCLAIM(dealer_id, startDate,endDate);
			String REMARK = mapCLAIM.get("REMARK").toString();
			Map<String, Object> mapApp = dao.queryApption(dealer_id,REMARK);
			TtAsPaymentPO paymentPO = new TtAsPaymentPO();
			paymentPO.setBalanceOder(REMARK);

			List<TtAsPaymentPO> list = dao.select(paymentPO);
			double labourAccount = Arith.sub(Double.parseDouble(mapApp.get(
					"CLAIM_TYPE_AMOUNT_01").toString()), Double.parseDouble(mapApp
					.get("CLAIM_TYPE_AMOUNT_11").toString()));
			double othersPrice = Arith.sub(Double.parseDouble(mapCLAIM.get("AMOUNT_SUM").toString()), Double.parseDouble(mapCLAIM.get(
							"PART_ACCOUNT").toString()));
			othersPrice= Arith.add(othersPrice, Double.parseDouble(mapCLAIM.get("PLUS_MINUS_SUM").toString()));
			int nature = 0;
			int STATUS = 0;
			Date date = new Date();
			if (mapdel.get("TAXPAYER_NATURE").toString().equals("一般纳税人")) {
				nature = 1;
				if (list.size() > 0) {
					paymentPO = list.get(0);
					STATUS = paymentPO.getStatus();
					act.setOutData("AMOUNT_OF_MONEY",
							paymentPO.getAmountOfMoney());
					act.setOutData("TAX_RATE_MONEY",
							paymentPO.getTaxRateMoney());
					act.setOutData("FINE_AMOUNT_OF_MONEY",
							paymentPO.getFineAmountOfMoney());
					act.setOutData("FINE_TAX_RATE_MONEY",
							paymentPO.getFineTaxRateMoney());

					act.setOutData("REMARK", paymentPO.getRemark());
					act.setOutData("SERIAL_NUMBER", paymentPO.getSerialNumber());
					act.setOutData("LABOUR_RECEIPT",
							paymentPO.getLabourReceipt());
					act.setOutData("PART_RECEIPT", paymentPO.getPartReceipt());

					date = paymentPO.getCreatDate();

				} else {
					act.setOutData("AMOUNT_OF_MONEY",
							mapCLAIM.get("AMOUNT_OF_MONEY").toString());
					act.setOutData("TAX_RATE_MONEY",
							mapCLAIM.get("TAX_RATE_MONEY").toString());
					act.setOutData("FINE_AMOUNT_OF_MONEY",
							mapCLAIM.get("FINE_AMOUNT_OF_MONEY").toString());
					act.setOutData("FINE_TAX_RATE_MONEY",
							mapCLAIM.get("FINE_TAX_RATE_MONEY").toString());
				}

			} else {
				if (list.size() > 0) {
					paymentPO = list.get(0);
					STATUS = paymentPO.getStatus();
					act.setOutData("AMOUNT_OF_MONEY",
							paymentPO.getAmountOfMoney());
					act.setOutData("TAX_RATE_MONEY",
							paymentPO.getTaxRateMoney());
					act.setOutData("FINE_AMOUNT_OF_MONEY",
							paymentPO.getFineAmountOfMoney());
					act.setOutData("FINE_TAX_RATE_MONEY",
							paymentPO.getFineTaxRateMoney());
					act.setOutData("REMARK", paymentPO.getRemark());
					act.setOutData("SERIAL_NUMBER", paymentPO.getSerialNumber());
					act.setOutData("LABOUR_RECEIPT",
							paymentPO.getLabourReceipt());
					act.setOutData("PART_RECEIPT", paymentPO.getPartReceipt());
					date = paymentPO.getCreatDate();
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			act.setOutData("date", sdf.format(date));
			act.setOutData("STATUS", STATUS);
			act.setOutData("nature", nature);
			act.setOutData("mapdel", mapdel);
			act.setOutData("mapApp", mapApp);
			act.setOutData("typesta", typesta);
			act.setOutData("mapCLAIM", mapCLAIM);
			act.setOutData("labourAccount", labourAccount);
			act.setOutData("othersPrice", othersPrice);
			act.setOutData("dealerId", dealer_id);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			
			act.setForword(deleteBalanceInfoView);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: deleteFinancialInfoViewPage 
	* @author: xyfue
	* @Description: 撤销结算数据
	* @param     设定文件 
	* @date 2014年10月17日 下午2:14:17 
	* @return void    返回类型 
	* @throws
	 */
	public void deleteFinancialInfoView() {

			BalanceMainDao dao = new BalanceMainDao();
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));

			System.err.println("dealer_id:"+dealerId + ";  startDate:"+startDate+ ";  endDate:"+endDate +";  remark:"+remark );
			boolean isTrue = true;
			String msg = "";
			if ("".equals(dealerId)) {
				isTrue = false;
				msg = "dealerId不能为空";
			}
			
			if ("".equals(startDate) && isTrue) {
				isTrue = false;
				msg = "startDate不能为空";
			}
			
			if ("".equals(endDate)  && isTrue) {
				isTrue = false;
				msg = "endDate不能为空";
			}
			
			if ("".equals(remark)  && isTrue) {
				isTrue = false;
				msg = "remark不能为空";
			}
			
			
			try {
				if(isTrue){
					dao.deleteFinancialInfoView(dealerId, startDate, endDate, remark);
					act.setOutData("succ", "1");
				}else {
					act.setOutData("succ", "0");
					act.setOutData("msg", msg);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(loginUser, e);
				act.setOutData("succ", "0");
				act.setOutData("msg", "撤销结算失败！");
			}
			
		
	}


	public void queryFinancialTickets() {
		try {
			Integer logonUser=0;
			String dealerId=loginUser.getDealerId();
			if(dealerId!=null){
				logonUser=1;
			}else{
				logonUser=2;
			}
			DealerKpDao dao = new DealerKpDao();
			
			String dealer_id = request.getParamValue("id");
			String startDate = request.getParamValue("START_DATE");
			String endDate = request.getParamValue("END_DATE");
			
			String typesta = request.getParamValue("typesta");
			String status = request.getParamValue("status");
			String check_status = request.getParamValue("check_status");


			Map<String, Object> mapdel = dao.queryDealerMes(dealer_id);
			
			
			
			Map<String, Object> mapCLAIM = dao.queryCLAIM(dealer_id, startDate,endDate);
			String REMARK = mapCLAIM.get("REMARK").toString();
			//==========zyw 把二次入库补偿服务站的钱加到结算总金额里 2014-9-10
			Double compensationDealerMoney=0d;
			DealerKpDao dealerkpdao = new DealerKpDao();
			List<Map<String, Object>> listMap=dealerkpdao.findcompensationBydealer(dealer_id,REMARK);
			if(listMap!=null&&listMap.size()>0){
				Map<String, Object> map = listMap.get(0);
				BigDecimal  bigdecimal= (BigDecimal) map.get("AMOUNT");
				compensationDealerMoney=bigdecimal.doubleValue();
				BigDecimal  otheraccount=(BigDecimal) mapCLAIM.get("OTHERACCOUNT");
				Double otheraccountTemp = otheraccount.doubleValue();
				mapCLAIM.put("OTHERACCOUNT", Arith.add(otheraccountTemp, compensationDealerMoney));//把补偿服务站的钱放到其他费用里
			}
			//=============================================
			Map<String, Object> mapApp = dao.queryApption(dealer_id,REMARK);
			TtAsPaymentPO paymentPO = new TtAsPaymentPO();
			paymentPO.setBalanceOder(REMARK);
			//========================================zyw 接口数据查询2014-10-31
			InvoiceService invoiceService = new InvoiceServiceImpl();
			Map<String, Double> InfoMoney = invoiceService.invoiceInfoMoneyByNO(REMARK);
			List<Map<String,Object>> InfoSecondTime = invoiceService.invoiceInfoSecondTimeByNo(REMARK);
			act.setOutData("InfoMoney", InfoMoney);
			act.setOutData("InfoSecondTime", InfoSecondTime.size());
			//========================================
			List<TtAsPaymentPO> list = dao.select(paymentPO);
			
			TtInvoiceTaxratePO invoice=new TtInvoiceTaxratePO();
			invoice.setStatus(Long.parseLong("10011001"));
			
			List<TtInvoiceTaxratePO> voices=dao.select(invoice);
			act.setOutData("voices",voices);
			
			String invoiceValue="";
			String invoiceName="";
			double amountOfMoneySum=0;
			double taxRateMoneySum=0;
			double amountSumSum=0;
			TtInvoiceTaxratePO invoicePO=new TtInvoiceTaxratePO();
			TtInvoiceTaxratePO invoicePO1=new TtInvoiceTaxratePO();
			double labourAccount = Arith.sub(Double.parseDouble(mapCLAIM.get(
					"LABOUR_ACCOUT").toString()), Double.parseDouble(mapApp
					.get("CLAIM_TYPE_AMOUNT_FREE_02").toString()));
		//	int nature = 0;
			long inovoiceTaxrate=0;
			//获取结算单税率ID
			Object invoiceValueObj= mapCLAIM.get("INVOICE_ID");
			if(invoiceValueObj!=null){
				invoiceValue=invoiceValueObj.toString();
				invoicePO.setId(Long.parseLong(invoiceValue));
				if(dao.select(invoicePO).size()>0){
					invoicePO1=(TtInvoiceTaxratePO) dao.select(invoicePO).get(0);
					inovoiceTaxrate=invoicePO1.getTaxRate();
					invoiceName=invoicePO1.getInvoiceName()+"  "+invoicePO1.getTaxRate()+"%";
				}
				
			}
			Object selectmentAmountObj= mapCLAIM.get("SELECTMENT_AMOUNT");
			String selectmentAmount="";
			if(selectmentAmountObj!=null){
				selectmentAmount=selectmentAmountObj.toString();
			}	
//			}else{
//				selectmentAmount=mapCLAIM.get("AMOUNT_SUM").toString();
//			}
			int STATUS = 0;
			Date date = new Date();
			double sum = 0;
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					amountOfMoneySum=Arith.add(amountOfMoneySum,list.get(i).getAmountOfMoney());
					taxRateMoneySum=Arith.add(taxRateMoneySum,list.get(i).getTaxRateMoney());
				//	invoiceValue=list.get(0).getInvoice();
//					if(invoiceValue!=null){
//						invoicePO.setId(Long.parseLong(invoiceValue));
//						invoicePO1=(TtInvoiceTaxratePO) dao.select(invoicePO).get(0);
//						invoiceName=invoicePO1.getInvoiceName()+"  "+invoicePO1.getTaxRate()+"%";
//					}
//					
//					sum=sum+list.get(i).getAmountSum();
				}
				amountSumSum=Arith.add(amountOfMoneySum, taxRateMoneySum);
				act.setOutData("amountSumSum", amountSumSum);
				act.setOutData("amountOfMoneySum", amountOfMoneySum);
				act.setOutData("taxRateMoneySum", taxRateMoneySum);
				act.setOutData("invoiceName", invoiceName);
				act.setOutData("invoiceValue",invoiceValue);
				act.setOutData("list",list);
				act.setOutData("sum",selectmentAmount);
				act.setOutData("status",status);
				act.setOutData("size",list.size());
				
				
				act.setOutData("dealer_id",dealer_id);
				act.setOutData("startDate",startDate);
				act.setOutData("endDate",endDate);
				act.setOutData("check_status",check_status);
				
			paymentPO = list.get(0);
			STATUS = paymentPO.getStatus();

			date = paymentPO.getCreatDate();

			} else {
				act.setOutData("AMOUNT_OF_MONEY",
						mapCLAIM.get("AMOUNT_OF_MONEY").toString());
				act.setOutData("TAX_RATE_MONEY",
						mapCLAIM.get("TAX_RATE_MONEY").toString());
				act.setOutData("FINE_AMOUNT_OF_MONEY",
						mapCLAIM.get("FINE_AMOUNT_OF_MONEY").toString());
				act.setOutData("FINE_TAX_RATE_MONEY",
						mapCLAIM.get("FINE_TAX_RATE_MONEY").toString());
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			act.setOutData("date", sdf.format(date));
			act.setOutData("STATUS", STATUS);
		//	act.setOutData("nature", nature);
			act.setOutData("mapdel", mapdel);
			act.setOutData("mapApp", mapApp);
			act.setOutData("mapCLAIM", mapCLAIM);
			act.setOutData("labourAccount", labourAccount);
			act.setOutData("dealerId", loginUser.getDealerId());
			act.setOutData("sum",selectmentAmount);

			act.setForword(finViewUrlTickets);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void queryFinancialInfoView03() {

		try {
			BalanceMainDao dao = new BalanceMainDao();

			// CommonUtils.checkNull() 校验是否为空
			String dealer_id = request.getParamValue("id");
			String startDate = request.getParamValue("START_DATE");
			String endDate = request.getParamValue("END_DATE");
			auditBean bean = new auditBean();
			if (null != startDate && !"".equals(startDate) && null != endDate
					&& !"".equals(endDate)) {
				startDate = startDate + " 00:00:00";
				endDate = endDate + " 23:59:59";
			}
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setDealerCode(dealer_id);
			bean.setYieldly("95411002");
			List<Map<String, Object>> list = dao.queryDealerBalanceList01(bean);
			List<Map<String, Object>> sendlist = dao
					.queryDealerBalanceSendList01(bean);
			List<Map<String, Object>> yldlist = dao
					.queryDealerBalanceList02(bean);
			List<Map<String, Object>> alldlist = dao
					.queryDealerBalanceList03(bean);
			List<BalanceYieldlyBean> delist = dao.getPartBillDefine(dealer_id);
			List<Map<String, Object>> maps = dao.getClaimtypeCount(bean);
			Double onedealerl = Double.parseDouble(list.get(0)
					.get("ALL_LABOUR_AMOUNT").toString());
			Double onedealerp = Double.parseDouble(list.get(0)
					.get("ALL_PART_AMOUNT").toString());

			List<Map<String, Object>> dealist = dao.dealerBalanOrder(dealer_id,
					startDate, endDate, "95411002");
			List<Map<String, Object>> discount = dao.dealerDiscountOrder(
					dealer_id, startDate, endDate, "95411002");
			Map<String, Object> timap = dao.gettitkes(dealer_id,
					startDate.split(" ")[0].substring(0, 7),
					endDate.split(" ")[0].substring(0, 7), "95411002");
			Map<String, Object> tiorder = dao.getorder(dealer_id, startDate,
					endDate, "95411002");
			act.setOutData("dealist", dealist);
			if (discount.size() > 0) {
				act.setOutData("discount", discount.get(0));
			}
			act.setOutData("dealist", dealist);
			act.setOutData("timap", timap);
			act.setOutData("tiorder", tiorder);

			if (sendlist.size() > 0) {
				for (Map<String, Object> map : sendlist) {
					onedealerl = Arith.sub(onedealerl, Double.parseDouble(map
							.get("SERVICE_CHARGE").toString()));
					onedealerp = Arith.sub(onedealerp, Double.parseDouble(map
							.get("MATERIAL_CHARGE").toString()));
				}
			}

			act.setOutData("onedealerl", onedealerl);
			act.setOutData("onedealerp", onedealerp);
			int tatol1 = 0;
			int tatol2 = 0;
			int tatol3 = 0;
			int tatol4 = 0;
			int tatol5 = 0;
			int tatol6 = 0;
			int tatol7 = 0;
			for (Map<String, Object> map : maps) {
				if (map.get("CLAIM_TYPE").toString().equals("10661001")) {
					tatol2 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol2;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661002")) {
					tatol3 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol3;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661007")) {
					tatol4 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol4;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661009")) {
					tatol5 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol5;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661010")) {
					tatol6 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol6;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661006")) {
					tatol7 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol7;
				}
			}

			act.setOutData("tatol1", tatol1);
			act.setOutData("tatol2", tatol2);
			act.setOutData("tatol3", tatol3);
			act.setOutData("tatol4", tatol4);
			act.setOutData("tatol5", tatol5);
			act.setOutData("tatol6", tatol6);
			act.setOutData("tatol7", tatol7);
			TcUserPO userPO = new TcUserPO();
			userPO.setUser_id(loginUser.getUserId());
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(Long.parseLong(dealer_id));
			List<TmDealerPO> poList = dao.select(dealerPO);
			dealerPO = poList.get(0);
			String dealerName = dealerPO.getDealerName();

			String a = startDate.split("-")[1];
			String b = endDate.split("-")[1];
			String sbNO = "";
			if (a.equals(b)) {
				sbNO = dealerPO.getDealerCode() + startDate.split("-")[0] + a;
			} else {
				sbNO = dealerPO.getDealerCode() + startDate.split("-")[0] + a
						+ "-" + b;
			}

			// String a= startDate.split("-")[1];
			// String sbNO = dealerPO.getDealerCode() +
			// startDate.split("-")[0]+a;
			act.setOutData("map", list.get(0));
			act.setOutData("sendlist", sendlist);
			act.setOutData("yldlist", yldlist);
			act.setOutData("allmap", alldlist.get(0));
			act.setOutData("delist", delist);
			act.setOutData("dealerName", dealerName);
			act.setOutData("sbNO", sbNO);
			act.setForword(finViewUrldao);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void queryFinancialInfoView02() {

		try {
			BalanceMainDao dao = new BalanceMainDao();

			// CommonUtils.checkNull() 校验是否为空
			String dealer_id = request.getParamValue("id");
			String startDate = request.getParamValue("START_DATE");
			String endDate = request.getParamValue("END_DATE");
			auditBean bean = new auditBean();
			if (null != startDate && !"".equals(startDate) && null != endDate
					&& !"".equals(endDate)) {
				startDate = startDate + " 00:00:00";
				endDate = endDate + " 23:59:59";
			}
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setDealerCode(dealer_id);
			bean.setYieldly("95411002");
			List<Map<String, Object>> list = dao.queryDealerBalanceList01(bean);
			List<Map<String, Object>> sendlist = dao
					.queryDealerBalanceSendList01(bean);
			List<Map<String, Object>> yldlist = dao
					.queryDealerBalanceList02(bean);
			List<Map<String, Object>> alldlist = dao
					.queryDealerBalanceList03(bean);
			List<BalanceYieldlyBean> delist = dao.getPartBillDefine(dealer_id);
			List<Map<String, Object>> maps = dao.getClaimtypeCount(bean);
			Double onedealerl = Double.parseDouble(list.get(0)
					.get("ALL_LABOUR_AMOUNT").toString());
			Double onedealerp = Double.parseDouble(list.get(0)
					.get("ALL_PART_AMOUNT").toString());

			List<Map<String, Object>> dealist = dao.dealerBalanOrder(dealer_id,
					startDate, endDate, "95411002");
			List<Map<String, Object>> discount = dao.dealerDiscountOrder(
					dealer_id, startDate, endDate, "95411002");
			Map<String, Object> timap = dao.gettitkes(dealer_id,
					startDate.split(" ")[0].substring(0, 7),
					endDate.split(" ")[0].substring(0, 7), "95411002");
			Map<String, Object> tiorder = dao.getorder(dealer_id, startDate,
					endDate, "95411002");
			act.setOutData("dealist", dealist);
			if (discount.size() > 0) {
				act.setOutData("discount", discount.get(0));
			}
			act.setOutData("dealist", dealist);
			act.setOutData("timap", timap);
			act.setOutData("tiorder", tiorder);

			if (sendlist.size() > 0) {
				for (Map<String, Object> map : sendlist) {
					onedealerl = Arith.sub(onedealerl, Double.parseDouble(map
							.get("SERVICE_CHARGE").toString()));
					onedealerp = Arith.sub(onedealerp, Double.parseDouble(map
							.get("MATERIAL_CHARGE").toString()));
				}
			}

			act.setOutData("onedealerl", onedealerl);
			act.setOutData("onedealerp", onedealerp);
			int tatol1 = 0;
			int tatol2 = 0;
			int tatol3 = 0;
			int tatol4 = 0;
			int tatol5 = 0;
			int tatol6 = 0;
			int tatol7 = 0;
			for (Map<String, Object> map : maps) {
				if (map.get("CLAIM_TYPE").toString().equals("10661001")) {
					tatol2 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol2;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661002")) {
					tatol3 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol3;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661007")) {
					tatol4 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol4;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661009")) {
					tatol5 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol5;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661010")) {
					tatol6 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol6;
				} else if (map.get("CLAIM_TYPE").toString().equals("10661006")) {
					tatol7 = Integer.parseInt(map.get("CONUT").toString());
					tatol1 += tatol7;
				}
			}

			act.setOutData("tatol1", tatol1);
			act.setOutData("tatol2", tatol2);
			act.setOutData("tatol3", tatol3);
			act.setOutData("tatol4", tatol4);
			act.setOutData("tatol5", tatol5);
			act.setOutData("tatol6", tatol6);
			act.setOutData("tatol7", tatol7);
			TcUserPO userPO = new TcUserPO();
			userPO.setUser_id(loginUser.getUserId());
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(Long.parseLong(dealer_id));
			List<TmDealerPO> poList = dao.select(dealerPO);
			dealerPO = poList.get(0);
			String dealerName = dealerPO.getDealerName();

			// String a= startDate.split("-")[1];
			// String sbNO = dealerPO.getDealerCode() +
			// startDate.split("-")[0]+a;

			String a = startDate.split("-")[1];
			String b = endDate.split("-")[1];
			String sbNO = "";
			if (a.equals(b)) {
				sbNO = dealerPO.getDealerCode() + startDate.split("-")[0] + a;
			} else {
				sbNO = dealerPO.getDealerCode() + startDate.split("-")[0] + a
						+ "-" + b;
			}

			act.setOutData("map", list.get(0));
			act.setOutData("sendlist", sendlist);
			act.setOutData("yldlist", yldlist);
			act.setOutData("allmap", alldlist.get(0));
			act.setOutData("delist", delist);
			act.setOutData("dealerName", dealerName);
			act.setOutData("sbNO", sbNO);
			act.setForword(finViewUrl01);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 要求财务审核 更改结算单状态 2010-11-04 修改 1、检测所有特殊费用是否都已经审核
	 * 新增：BalanceMainDao.queryAuthSpecAmount()方法 通过统计标识特殊费用工单数量和已审核特殊费用工单数量来判断
	 * 如果：标识数量为0 或 标识数据=已审核数据 那么 都已经审核 反之：存在未审核通过，不能转复合申请 2、需要根据特殊费用审核状态重新计算特殊费用
	 * 将审核通过的特殊费用和全部标识的特殊费用差累计到结算单的结算金额中 3、经审核通过的特殊费用工单修改为"已结算"
	 */
	public void financialModeAudit() {

		String SUCCESS = "SUCCESS";
		String id = "";
		String dealerId = "";
		try {

			id = request.getParamValue("id");
			String var = request.getParamValue("var");// 版本号
			dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			BalanceMainDao dao = new BalanceMainDao();
			// 检测该结算单对应特殊费用是否都已经审核
			// Map<String,Object> specFeeMap =
			// dao.queryAuthSpecAmount(Long.parseLong(id));
			// /** 标识特殊费用工单数 */
			// Integer specCount =
			// Integer.parseInt(CommonUtils.getDataFromMap(specFeeMap,
			// "ALLCOUNT").toString());
			// /** 已审核特殊费用工单数 */
			// Integer authCount =
			// Integer.parseInt(CommonUtils.getDataFromMap(specFeeMap,
			// "AUTH_COUNT").toString());
			// /** 标识特殊费用市场工单费用 */
			// Double marketFee =
			// Double.parseDouble(CommonUtils.getDataFromMap(specFeeMap,
			// "MARKETFEE").toString());
			// /** 标识特殊费用外出工单费用 */
			// Double outerFee =
			// Double.parseDouble(CommonUtils.getDataFromMap(specFeeMap,
			// "OUTFEE").toString());
			// /** 已审核特殊费用市场工单费用 */
			// Double authMarketFee =
			// Double.parseDouble(CommonUtils.getDataFromMap(specFeeMap,
			// "AUTH_MARKETFEE").toString());
			// /** 已审核特殊费用外出工单费用 */
			// Double authOuterFee =
			// Double.parseDouble(CommonUtils.getDataFromMap(specFeeMap,
			// "AUTH_OUTFEE").toString());
			// /** 特殊费用变化值 */
			// Double chanageFee =
			// (authMarketFee+authOuterFee)-(marketFee+outerFee);
			// if(specCount<1 || specCount==authCount){
			boolean isDeal = DBLockUtil.lock(dealerId,
					DBLockUtil.BUSINESS_TYPE_08);
			if (isDeal) { // 同步
				String newVar = dao.getNewVar(id, var);// 获取版本号
				if (newVar.equals(var)) {
					// 结算单信息
					TtAsWrClaimBalancePO balancePO = dao
							.getClaimBalancePOById(Long.parseLong(id));
					Long yieldly = -1L;
					if (balancePO != null && balancePO.getYieldly() != null)
						yieldly = balancePO.getYieldly();
					// 统计抵扣金额
					Map<String, Object> deductAmountMap = dao
							.getBalanceMainMap(id);

					String jiujianCount = CommonUtils.getDataFromMap(
							deductAmountMap, "TOTAL_AMOUNT").toString();// 旧件扣款
					String kaoheCount = CommonUtils.getDataFromMap(
							deductAmountMap, "FINE_SUM").toString();// 考核扣款
					String xingzhengCount = CommonUtils.getDataFromMap(
							deductAmountMap, "DAMOUNT").toString();// 行政扣款
					jiujianCount = Utility.testString(jiujianCount) ? jiujianCount
							: "0";
					kaoheCount = Utility.testString(kaoheCount) ? kaoheCount
							: "0";
					xingzhengCount = Utility.testString(xingzhengCount) ? xingzhengCount
							: "0";
					String zongjiCount = request.getParamValue("zongjiCount") == null ? "0"
							: request.getParamValue("zongjiCount");// 结算金额
					String feiyongCount = request.getParamValue("feiyongCount") == null ? "0"
							: request.getParamValue("feiyongCount");// 费用合计
					String returnAmount = request
							.getParamValue("RETURN_AMOUNT") == null ? "0"
							: request.getParamValue("RETURN_AMOUNT");// 运费
					String repairAmount = request.getParamValue("repairAmount") == null ? "0"
							: request.getParamValue("repairAmount");// 保养费扣款
					String activityAmount = request
							.getParamValue("activityAmount") == null ? "0"
							: request.getParamValue("activityAmount");// 服务活动单扣款
					String remark = CommonUtils.checkNull(request
							.getParamValue("REMARK"));// 备注
					String dealerCode = CommonUtils.checkNull(request
							.getParamValue("dealerCode"));
					TtAsWrClaimBalancePO po = new TtAsWrClaimBalancePO();
					TtAsWrClaimBalancePO tp = new TtAsWrClaimBalancePO();
					po.setId(Long.parseLong(id));
					tp.setReturnAmount(Double.parseDouble(returnAmount));
					tp.setOldDeduct(Double.parseDouble(jiujianCount));
					tp.setCheckDeduct(Double.parseDouble(kaoheCount));
					tp.setAdminDeduct(Double.parseDouble(xingzhengCount));
					tp.setFreeDeduct(Double.parseDouble(repairAmount));
					tp.setServiceDeduct(Double.parseDouble(activityAmount));
					// tp.setMarketAmount(authMarketFee);
					// tp.setSpeoutfeeAmount(authOuterFee);
					tp.setRemark(remark);
					tp.setVar(Integer.parseInt(var) + 1);
					tp.setStatus(Integer.parseInt(Constant.ACC_STATUS_03));
					// tp.setAmountSum(Double.parseDouble(feiyongCount)+chanageFee);//加入特殊费用变动
					// tp.setBalanceAmount(Double.parseDouble(zongjiCount)+chanageFee);//加入特殊费用变动
					tp.setUpdateBy(loginUser.getUserId());
					Date updateDate = new Date();
					tp.setUpdateDate(updateDate);

					/******** Ivesosn add by 2011-01-20 ****************/
					TcUserPO poUser = new TcUserPO();// 根据userId找到用户名
					poUser.setUserId(loginUser.getUserId());
					TcUserPO poValue = (TcUserPO) dao.select(poUser).get(0);
					tp.setReviewApplicationBy(poValue.getName());// 同时更新结算单复合申请人
					tp.setReviewApplicationTime(new Date());// 同时更新结算单复合申请时间
					/******** Ivesosn add by 2011-01-20 ****************/

					dao.update(po, tp);// 更新TT_AS_WR_CLAIM_BALANCE

					/******** add 20101201 zhumy **********/
					TtAsWrClaimBalancePO sel = (TtAsWrClaimBalancePO) dao
							.select(po).get(0);
					Date endDate = sel.getEndDate();
					TtAsDealerTypePO typeCon = new TtAsDealerTypePO();
					typeCon.setDealerId(sel.getDealerId());
					typeCon.setYieldly(sel.getYieldly().toString());
					TtAsDealerTypePO typeValue = new TtAsDealerTypePO();
					typeValue.setBalanceReviewDate(endDate);
					typeValue.setUpdateDate(new Date());
					typeValue.setUpdateBy(loginUser.getUserId().toString());
					dao.update(typeCon, typeValue);

					/******** add 20101201 zhumy **********/

					// 将处理过的"特殊费用"标识为"已支付"
					TtAsWrSpefeePO aa = new TtAsWrSpefeePO();
					aa.setYield(yieldly);
					aa.setDealerId(Long.parseLong(dealerId));
					aa.setClaimbalanceId(Long.parseLong(id));
					aa.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_06));// 审核后的

					TtAsWrSpefeePO bb = new TtAsWrSpefeePO();
					bb.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_07));// 锁定状态
					bb.setUpdateBy(loginUser.getUserId());
					bb.setUpdateDate(updateDate);
					dao.update(aa, bb);

					// 将处理过的抵扣单修改为"无效"
					TtAsWrDeductBalancePO tw = new TtAsWrDeductBalancePO();
					tw.setStatus(Constant.STATUS_ENABLE);
					tw.setDealerCode(dealerCode);
					tw.setYieldly(yieldly);

					TtAsWrDeductBalancePO td = new TtAsWrDeductBalancePO();
					td.setStatus(Constant.STATUS_DISABLE);
					td.setClaimbalanceId(Long.parseLong(id));
					td.setUpdateBy(tp.getUpdateBy());
					td.setUpdateDate(updateDate);

					dao.update(tw, td);// 更新TT_AS_WR_DEDUCT_BALANCE

					// 将处理过的奖惩记录修改为"无效"
					TtAsWrFinePO ta = new TtAsWrFinePO();
					TtAsWrFinePO tf = new TtAsWrFinePO();
					ta.setDealerId(Long.parseLong(dealerId));
					ta.setYieldly(yieldly);
					ta.setPayStatus(Integer.parseInt(Constant.PAY_STATUS_01));
					tf.setPayStatus(Integer.parseInt(Constant.PAY_STATUS_02));
					tf.setClaimbalanceId(Long.parseLong(id));
					tf.setUpdateBy(tp.getUpdateBy());
					tf.setUpdateDate(updateDate);
					dao.update(ta, tf);// 更新TT_AS_WR_FINE

					/*
					 * add by zuoxj 2010-12-18 14：35 每次复核申请，都必将上次行政扣款转为已结算
					 */
					TtAsWrAdminDeductPO tc1 = new TtAsWrAdminDeductPO();
					TtAsWrAdminDeductPO ts1 = new TtAsWrAdminDeductPO();
					tc1.setDealerId(Long.parseLong(dealerId));
					tc1.setYieldly(yieldly);
					tc1.setDeductStatus(Integer
							.parseInt(Constant.ADMIN_STATUS_01));
					ts1.setDeductStatus(Integer
							.parseInt(Constant.ADMIN_STATUS_02));
					ts1.setClaimbalanceId(Long.parseLong(id));
					ts1.setUpdateBy(tp.getUpdateBy());
					ts1.setUpdateDate(updateDate);// 更新TT_AS_WR_ADMIN_DEDUCT
					dao.update(tc1, ts1);

					updateMarketStatus(Long.parseLong(dealerId));// 更新TT_IF_MARKET

					/********** add by liuxh 20101127 复核申请完成更新 BALANCE_AMOUNT ************/
					// BALANCE_AMOUNT减 保养费扣款 -服务活动单扣款-考核扣款-旧件扣款-行政抵扣-财务扣款
					StringBuffer sbSql = new StringBuffer();
					/*******
					 * mod by liuxh 201011201 复核申请结算金额重新计算再
					 * -服务活动单扣款-考核扣款-旧件扣款-行政抵扣-财务扣款
					 ********/
					sbSql.append("UPDATE TT_AS_WR_CLAIM_BALANCE SET BALANCE_AMOUNT=((NVL(LABOUR_AMOUNT,0)+NVL(PART_AMOUNT,0)+NVL(OTHER_AMOUNT,0)+NVL(FREE_AMOUNT,0)");
					sbSql.append("+NVL(RETURN_AMOUNT,0)+NVL(MARKET_AMOUNT,0)+NVL(SPEOUTFEE_AMOUNT,0)+NVL(APPEND_AMOUNT,0)+NVL(SERVICE_TOTAL_AMOUNT,0))");
					sbSql.append("-(NVL(FREE_DEDUCT,0)+NVL(SERVICE_DEDUCT,0)+NVL(CHECK_DEDUCT,0)+NVL(OLD_DEDUCT,0)+NVL(ADMIN_DEDUCT,0)+NVL(FINANCIAL_DEDUCT,0)))\n");
					sbSql.append("WHERE ID=?");

					List parList = new ArrayList();
					parList.add(Long.parseLong(id));
					dao.update(sbSql.toString(), parList);

					/********** add by liuxh 20101127 复核申请完成更新 BALANCE_AMOUNT ************/

					// 记录日志表
					BalanceStatusRecord
							.recordStatus(Long.parseLong(id),
									loginUser.getUserId(), loginUser.getName(),
									loginUser.getOrgId(),
									BalanceStatusRecord.STATUS_06);
					act.setOutData("returnValue", "1");

				} else {
					act.setOutData("returnValue", 0);
				}
			} else {
				SUCCESS = "DEALED";
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.PUTIN_FAILURE_CODE, "财务复核申请");
			logger.error(loginUser, e1);
			act.setException(e1);
		} finally {
			DBLockUtil.freeLock(id, DBLockUtil.BUSINESS_TYPE_08);
			DBLockUtil.freeLock(dealerId, DBLockUtil.BUSINESS_TYPE_08);
		}
		act.setOutData("SUCCESS", SUCCESS);
	}

	/**
	 * 
	 * @Title: updateMarketStatus
	 * @Description: TODO(点完复核申请,将状态更新成已结算)
	 * @param @param dealerId 经销商ID
	 * @return void 返回类型
	 * @throws
	 */
	private void updateMarketStatus(Long dealerId) {
		TtIfMarketPO po = new TtIfMarketPO();
		po.setDealerId(dealerId);
		po.setStatus(Constant.MARKET_BACK_STATUS_TECH_PASS);
		TtIfMarketPO npo = new TtIfMarketPO();
		npo.setStatus(Constant.MARKET_BACK_STATUS_BALANCE);
		MarketQuesOrderDao dao = MarketQuesOrderDao.getInstance();
		dao.update(po, npo);
	}

	/*
	 * 生成开票通知单
	 */

	public void claimBalanceAudit() {

		String id = "";
		String SUCCESS = "SUCCESS";
		try {

			id = request.getParamValue("id");
			String var = request.getParamValue("var");// 版本号
			BalanceMainDao dao = new BalanceMainDao();
			boolean isDeal = DBLockUtil.lock(id, DBLockUtil.BUSINESS_TYPE_09);

			if (isDeal) {// 同步
				String newVar = dao.getNewVar(id, var);// 获取版本号
				if (newVar.equals(var)) {
					String financialDeduct = request
							.getParamValue("financialDeduct") == null ? "0"
							: request.getParamValue("financialDeduct");// 财务抵扣
					String noteCount = request.getParamValue("noteCount") == null ? "0"
							: request.getParamValue("noteCount");// 开票总金额
					String financialRemark = CommonUtils.checkNull(request
							.getParamValue("financialRemark"));
					TtAsWrClaimBalancePO po = new TtAsWrClaimBalancePO();
					TtAsWrClaimBalancePO tp = new TtAsWrClaimBalancePO();
					po.setId(Long.parseLong(id));
					tp.setFinancialDeduct(Double.parseDouble(financialDeduct));
					tp.setNoteAmount(Double.parseDouble(noteCount));
					tp.setFunancialRemark(financialRemark);
					tp.setVar(Integer.parseInt(var) + 1);
					tp.setStatus(Integer.parseInt(Constant.ACC_STATUS_04));
					tp.setUpdateBy(loginUser.getUserId());
					Date updateDate = new Date();
					tp.setUpdateDate(updateDate);
					dao.update(po, tp);// 更新TT_AS_WR_CLAIM_BALANCE

					// 记录日志表
					BalanceStatusRecord
							.recordStatus(Long.parseLong(id),
									loginUser.getUserId(), loginUser.getName(),
									loginUser.getOrgId(),
									BalanceStatusRecord.STATUS_07);
					act.setOutData("returnValue", "1");
				} else {
					act.setOutData("returnValue", 0);
				}
			} else {
				SUCCESS = "DEALED";
			}
		} catch (Exception e) {// 异常方法
			SUCCESS = "FAILURE";
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.PUTIN_FAILURE_CODE, "生成开票通知单");
			logger.error(loginUser, e1);
			act.setException(e1);
		} finally {
			DBLockUtil.freeLock(id, DBLockUtil.BUSINESS_TYPE_09);
		}
		act.setOutData("SUCCESS", SUCCESS);
	}

	/*
	 * 转行政扣款
	 */
	public void deductBalanceAudit() {

		String id = "";
		String SUCCESS = "SUCCESS";
		try {

			id = request.getParamValue("id");
			String var = request.getParamValue("var");// 版本号
			BalanceMainDao dao = new BalanceMainDao();
			boolean isDeal = DBLockUtil.lock(id, DBLockUtil.BUSINESS_TYPE_10);
			if (isDeal) {
				DealerNewKp kp = new DealerNewKp();
				kp.doBalanceKp();
				String newVar = dao.getNewVar(id, var);// 获取版本号
				if (newVar.equals(var)) {
					String financialDeduct = request
							.getParamValue("financialDeduct") == null ? "0"
							: request.getParamValue("financialDeduct");// 财务抵扣
					String noteCount = request.getParamValue("noteCount") == null ? "0"
							: request.getParamValue("noteCount");// 开票总金额
					String deductAmount = request.getParamValue("deductAmount") == null ? "0"
							: request.getParamValue("deductAmount");// 开票总金额
					String financialRemark = CommonUtils.checkNull(request
							.getParamValue("financialRemark"));
					String dealerId = CommonUtils.checkNull(request
							.getParamValue("dealerId"));
					String dealerCode = CommonUtils.checkNull(request
							.getParamValue("dealerCode"));
					String dealerName = CommonUtils.checkNull(request
							.getParamValue("dealerName"));
					TtAsWrClaimBalancePO po = new TtAsWrClaimBalancePO();
					TtAsWrClaimBalancePO tp = new TtAsWrClaimBalancePO();
					po.setId(Long.parseLong(id));
					tp.setFinancialDeduct(Double.parseDouble(financialDeduct));
					tp.setNoteAmount(Double.parseDouble(noteCount));
					tp.setFunancialRemark(financialRemark);
					tp.setVar(Integer.parseInt(var) + 1);
					tp.setStatus(Integer.parseInt(Constant.ACC_STATUS_04));
					tp.setUpdateBy(loginUser.getUserId());
					Date updateDate = new Date();
					tp.setUpdateDate(updateDate);
					dao.update(po, tp);// 更新TT_AS_WR_CLAIM_BALANCE

					// 结算单信息
					TtAsWrClaimBalancePO balancePO = dao
							.getClaimBalancePOById(Long.parseLong(id));
					Long yieldly = -1L;
					if (balancePO != null && balancePO.getYieldly() != null)
						yieldly = balancePO.getYieldly();

					Double zjCount = Math.abs(Utility.getDouble(deductAmount));

					TtAsWrAdminDeductPO dp = new TtAsWrAdminDeductPO();
					dp.setId(Long.parseLong(SequenceManager.getSequence("")));
					dp.setDealerId(Long.parseLong(dealerId));
					dp.setDealerCode(dealerCode);
					dp.setDealerName(dealerName);
					dp.setDeductAmount(zjCount);
					dp.setDeductStatus(Integer
							.parseInt(Constant.ADMIN_STATUS_01));
					dp.setFromClaimbalanceId(Long.parseLong(id));
					dp.setCreateBy(tp.getUpdateBy());
					dp.setCreateDate(tp.getUpdateDate());
					dp.setOemCompanyId(GetOemcompanyId
							.getOemCompanyId(loginUser));
					dp.setYieldly(yieldly);
					dao.insert(dp);// 新增TT_AS_WR_ADMIN_DEDUCT

					// zhumingwei 2011-10-29 ADD 随机抽查功能
					TtAsDealerCheckPO poCheck = new TtAsDealerCheckPO();
					poCheck.setDealerId(Long.parseLong(loginUser.getDealerId()));
					poCheck.setYieldly(yieldly);
					poCheck.setStatus(Constant.STATUS_ENABLE);
					TtAsDealerCheckPO poCheckValue = (TtAsDealerCheckPO) daoKP
							.select(poCheck).get(0);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date lastDate = poCheckValue.getLastCheckDate();// 上次抽查时间
					Calendar calendar = Calendar.getInstance();// 公用类，加年月日的
					calendar.setTime(lastDate);
					calendar.add(Calendar.DAY_OF_MONTH, 1);// 当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
					lastDate = calendar.getTime();// 得到加一天后的值
					int checkCount = poCheckValue.getCheckCount();// 达到此抽查数量
					String checkPercentage = poCheckValue.getCheckPercentage();// 抽查百分比
					String wxEndDate = request.getParamValue("endDate");// 取得页面维修时间止
					List<Map<String, Object>> count = daoKP
							.getApplicationCount(sdf.format(lastDate),
									wxEndDate, loginUser.getDealerId(), yieldly);
					Map map = (Map) count.get(0);
					int countCon = ((BigDecimal) map.get("COUNT")).intValue();// 得到符合条件查询出来的数量
					if (countCon > checkCount) {// 跟抽查数量对比，如果大于抽查数量，开始抽查
						TtAsWrCheckApplicationPO checkApp = new TtAsWrCheckApplicationPO();
						long checkId = Long.parseLong(SequenceManager
								.getSequence(""));
						checkApp.setId(checkId);
						checkApp.setCheckNo(SequenceManager.getSequence("LO"));
						int con = (int) Math.ceil((Integer
								.parseInt(checkPercentage) * countCon) * 0.01);
						checkApp.setCheckCount(con);
						checkApp.setBalanceNo(balancePO.getBalanceNo());
						checkApp.setDelaerId(Long.parseLong(loginUser
								.getDealerId()));
						checkApp.setCheckDate(new Date());
						checkApp.setStatus(Constant.CHECK_APP_STATUS_1);
						checkApp.setIsFrint(0);
						checkApp.setCreateBy(loginUser.getUserId().toString());
						checkApp.setCreateDate(new Date());
						daoKP.insert(checkApp);

						List<Map<String, Object>> detail = daoKP
								.getApplicationDetail(sdf.format(lastDate),
										wxEndDate, loginUser.getDealerId(),
										yieldly, con + 1);
						for (int i = 0; i < detail.size(); i++) {
							TtAsWrCheckDetailPO poDetail = new TtAsWrCheckDetailPO();
							poDetail.setId(Long.parseLong(SequenceManager
									.getSequence("")));
							poDetail.setCheckId(checkId);
							poDetail.setClaimId(((BigDecimal) detail.get(i)
									.get("ID")).longValue());
							poDetail.setStatus(Constant.CHECK_APP_DETAIL_STATUS_1);
							poDetail.setCreateBy(loginUser.getUserId()
									.toString());
							poDetail.setCreateDate(new Date());
							daoKP.insert(poDetail);
						}
					}
					// 记录日志表
					BalanceStatusRecord
							.recordStatus(Long.parseLong(id),
									loginUser.getUserId(), loginUser.getName(),
									loginUser.getOrgId(),
									BalanceStatusRecord.STATUS_07);

					act.setOutData("returnValue", "1");

				} else {
					act.setOutData("returnValue", 0);
				}
			} else {
				SUCCESS = "DEALED";
			}
		} catch (Exception e) {// 异常方法
			SUCCESS = "ERROR";
			act.setOutData("returnValue", "ERROR");
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.PUTIN_FAILURE_CODE, "转行政扣款单");
			logger.error(loginUser, e1);
			act.setException(e1);
		} finally {
			DBLockUtil.freeLock(id, DBLockUtil.BUSINESS_TYPE_10);
		}
		act.setOutData("SUCCESS", SUCCESS);
	}

	/*
	 * 经销商查看开票结果
	 */
	public void dealerBalanceQueryInit() {

		try {
			act.setForword(delaerUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 查看开票结果明细
	 */
	public void queryDealerBalanceInfo() {

		try {

			String id = request.getParamValue("id");
			String infoFlag = request.getParamValue("infoFlag");
			BalanceMainDao dao = new BalanceMainDao();
			Map<String, Object> map = dao.getDealerQueryInfo(id);
			List<Map<String, Object>> detail = dao.getBalanceMainList1(id);
			act.setOutData("infoFlag", infoFlag);
			act.setOutData("map", map);
			act.setOutData("detail", detail);
			List<Map<String, Object>> info = dao.getStopAuth(id);
			act.setOutData("detail1", info);
			act.setForword(delaerInfoUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void queryDealerBalanceInfoStop() {

		try {
			BalanceMainDao dao = new BalanceMainDao();

			String id = request.getParamValue("id");
			List<Map<String, Object>> info = dao.getStopAuth(id);
			act.setOutData("detail", info);
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(id));
			List<FsFileuploadPO> lists = daoKP.select(detail);
			act.setOutData("ID", id);
			act.setOutData("lists", lists);
			act.setForword(DEALER_BALANCE_INFO_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void getInvoice() {

		try {

			String id = request.getParamValue("id");
			String remake = request.getParamValue("remake");
			TtAsWrBalanceStopPO po = new TtAsWrBalanceStopPO();
			po.setBalanceId(Long.valueOf(id));
			List<TtAsWrBalanceStopPO> list = daoKP.select(po);
			if (list.size() > 0) {
				TtAsWrBalanceStopPO stopPo = new TtAsWrBalanceStopPO();
				TtAsWrBalanceStopPO stopPo1 = new TtAsWrBalanceStopPO();
				stopPo.setBalanceId(Long.valueOf(id));
				stopPo1.setStartDate(new Date());
				stopPo1.setUpdateDate(new Date());
				stopPo1.setUpdateBy(loginUser.getUserId().toString());
				stopPo1.setStatus(1);
				stopPo1.setRemake(remake);
				daoKP.update(stopPo, stopPo1);

				// 将附件保存
				String ywzj = "";
				if (id != null && !id.equals("")) {
					ywzj = id;
				} else {
					ywzj = String.valueOf(po.getBalanceId());
				}
				String[] fjids = request.getParamValues("fjid");// 获取文件ID
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);
				if (id != null && !id.equals("")) {// 修改的时候
					FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
					FileUploadManager.fileUploadByBusiness(ywzj, fjids,
							loginUser);
				} else {
					FileUploadManager.fileUploadByBusiness(ywzj, fjids,
							loginUser);
				}
				// 记录表
				TtAsWrBalanceStopAuthPO popo = new TtAsWrBalanceStopAuthPO();
				popo.setId(Long.valueOf(SequenceManager.getSequence("")));
				popo.setStopId(Long.valueOf(id));
				popo.setStatus(1);
				popo.setStopDate(new Date());
				popo.setStopBy(loginUser.getUserId().toString());
				popo.setRemark(remake);
				daoKP.insert(popo);
			} else {
				TtAsWrBalanceStopPO stopPo = new TtAsWrBalanceStopPO();
				stopPo.setId(Long.valueOf(SequenceManager.getSequence("")));
				stopPo.setBalanceId(Long.valueOf(id));
				stopPo.setStartDate(new Date());
				stopPo.setStartDate(new Date());
				stopPo.setCreateBy(loginUser.getUserId().toString());
				stopPo.setStatus(1);
				stopPo.setRemake(remake);
				daoKP.insert(stopPo);
				// 将附件保存
				String ywzj = "";
				if (id != null && !id.equals("")) {
					ywzj = id;
				} else {
					ywzj = String.valueOf(po.getBalanceId());
				}
				String[] fjids = request.getParamValues("fjid");// 获取文件ID
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);
				if (id != null && !id.equals("")) {// 修改的时候
					FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
					FileUploadManager.fileUploadByBusiness(ywzj, fjids,
							loginUser);
				} else {
					FileUploadManager.fileUploadByBusiness(ywzj, fjids,
							loginUser);
				}
				// 记录表
				TtAsWrBalanceStopAuthPO popo = new TtAsWrBalanceStopAuthPO();
				popo.setId(Long.valueOf(SequenceManager.getSequence("")));
				popo.setStopId(Long.valueOf(id));
				popo.setStatus(1);
				popo.setStopDate(new Date());
				popo.setStopBy(loginUser.getUserId().toString());
				popo.setRemark(remake);
				daoKP.insert(popo);
			}
			act.setOutData("flag", true);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void queryDealerBalanceInfoStopCancel() {

		try {

			String id = request.getParamValue("id");
			String remake = request.getParamValue("remake");
			TtAsWrBalanceStopPO stopPo = new TtAsWrBalanceStopPO();
			TtAsWrBalanceStopPO stopPo1 = new TtAsWrBalanceStopPO();
			stopPo.setBalanceId(Long.valueOf(id));
			stopPo1.setEndDate(new Date());
			stopPo1.setUpdateDate(new Date());
			stopPo1.setUpdateBy(loginUser.getUserId().toString());
			stopPo1.setStatus(0);
			daoKP.update(stopPo, stopPo1);
			// 记录表
			TtAsWrBalanceStopAuthPO popo = new TtAsWrBalanceStopAuthPO();
			popo.setId(Long.valueOf(SequenceManager.getSequence("")));
			popo.setStopId(Long.valueOf(id));
			popo.setStatus(0);
			popo.setStopDate(new Date());
			popo.setStopBy(loginUser.getUserId().toString());
			popo.setRemark(remake);
			daoKP.insert(popo);
			act.setForword(oemUrlStop);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	/*
	 * 经销商确认
	 */
	public void checkBalanceFor() {

		try {

			String id = request.getParamValue("id");
			TtAsWrClaimBalancePO po = new TtAsWrClaimBalancePO();
			TtAsWrClaimBalancePO tp = new TtAsWrClaimBalancePO();
			po.setId(Long.parseLong(id));
			tp.setStatus(Integer.parseInt(Constant.ACC_STATUS_05));
			tp.setUpdateBy(loginUser.getUserId());
			tp.setUpdateDate(new Date());
			BalanceMainDao dao = new BalanceMainDao();
			dao.update(po, tp);

			// 记录日志表 -- 添加一个经销商确认时间
			BalanceStatusRecord.recordStatus(Long.parseLong(id),
					loginUser.getUserId(), loginUser.getName(),
					loginUser.getOrgId(), BalanceStatusRecord.STATUS_08);
			act.setForword(delaerUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "经销商确认");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void oemBalanceQueryInit() {

		try {
			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId());

			act.setOutData("yieldly", yieldly);
			act.setForword(oemUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "经销商开票确认查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void oemBalanceQueryInitStop() {

		try {
			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId());

			act.setOutData("yieldly", yieldly);
			act.setForword(oemUrlStop);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "经销商开票确认查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*********
	 * 合同号维护
	 */
	public void oemContractNumber() {
		try {
			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId());

			act.setOutData("yieldly", yieldly);
			act.setForword(oemUrlConstract);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "经销商开票确认查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*********
	 * 合同号维护查询
	 */
	public void oemContractNumberView() {

		try {
			// 取得该用户拥有的产地权限

			String yieldly = request.getParamValue("YIELDLY");
			String dealerId = request.getParamValue("dealerId");
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			BalanceMainDao dao = new BalanceMainDao();
			PageResult<Map<String, Object>> ps = dao.queryConstractNumber(
					dealerId, yieldly, curPage, 15);
			act.setOutData("ps", ps);
			act.setForword(oemUrlConstract);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "经销商开票确认查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void oemContractNumberModifilyInit() {

		try {
			// 取得该用户拥有的产地权限

			String id = request.getParamValue("id");
			act.setOutData("id", id);
			TtAsWrContractPO po = new TtAsWrContractPO();
			po.setId(Long.valueOf(id));
			List<TtAsWrContractPO> listPO = daoKP.select(po);
			act.setOutData("ContractNo", listPO.get(0).getContractNo());
			act.setOutData("id", id);
			act.setForword(oemContractNumberModifily);

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "经销商开票确认查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	public void oemContractNumberModify() {

		try {
			// 取得该用户拥有的产地权限

			String id = request.getParamValue("id");
			String constractNo = request.getParamValue("CONSTRACT");// 新合同号
			TtAsWrContractPO po = new TtAsWrContractPO();
			TtAsWrContractPO po1 = new TtAsWrContractPO();
			po.setId(Long.valueOf(id));
			po1.setContractNo(constractNo);
			daoKP.update(po, po1);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "经销商开票确认查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void oemContractNumberDelete() {

		try {
			// 取得该用户拥有的产地权限

			String id = request.getParamValue("id");
			TtAsWrContractPO po = new TtAsWrContractPO();
			po.setId(Long.valueOf(id));
			daoKP.delete(po);
			oemContractNumberView();
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "经销商开票确认查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	/**********
	 * :开票通知单打印
	 * 
	 */
	public void invoicePrint() {

		try {

			BalanceMainDao dao = new BalanceMainDao();
			String id = request.getParamValue("id");
			List<Map<String, Object>> detail = dao.getBalanceMainList(id);
			List<Map<String, Object>> list = dao.getinvoiceView(id);
			List l = dao.getDate2(id);
			if (l.size() > 0) {
				act.setOutData("audit", (TtAsWrBalanceAuthitemPO) l.get(0));
			}
			act.setOutData("list", list.get(0));
			act.setOutData("detail", detail);
			act.setForword(invoicePrint);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "经销商确认");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// 根据结算单ID判断复核申请的条件
	public void dealerTimeJuge() {

		try {
			BalanceMainDao dao = new BalanceMainDao();

			String balanceId = request.getParamValue("id");// 结算单ID
			String msg = dao.dealerTimeJuge(Long.valueOf(balanceId));
			act.setOutData("msg", msg);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "ERROR");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 收单汇总的一个打印
	 */
	public void getClaimInit() {

		try {
			act.setForword(CLAIM_INIT_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "汇总打印页面查询失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void claimPrintQuery() {

		try {
			String bDate = request.getParamValue("bDate");
			String eDate = request.getParamValue("eDate");
			String name = request.getParamValue("name");
			BalanceMainDao dao = new BalanceMainDao();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 15;
			PageResult<Map<String, Object>> ps = dao.getClaimPrint(bDate,
					eDate, name, loginUser.getCompanyId(), pageSize, curPage,
					loginUser.getPoseId());
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "汇总打印页面查询失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void goPrint() {

		try {
			String bDate = request.getParamValue("bDate");
			String eDate = request.getParamValue("eDate");
			String name = request.getParamValue("name");
			BalanceMainDao dao = new BalanceMainDao();

			PageResult<Map<String, Object>> ps = dao.getClaimPrint(bDate,
					eDate, name, loginUser.getCompanyId(), 10000, 1,
					loginUser.getPoseId());
			act.setOutData("list", ps.getRecords());
			act.setOutData("bDate", bDate);
			act.setOutData("eDate", eDate);
			act.setForword(CLAIM_PRINT_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "汇总打印页面查询失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * Iverson add By 2010-12-16 打印收票报表
	 */
	public void getPrintPorterInit() {

		try {
			act.setForword(PRINTPROTER_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "汇总打印页面查询失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * zhumingwei add By 2012-08-29 结算室收票状态明细
	 */
	public void queryBalanceDetail() {

		try {
			act.setForword(BALANCE_DETAIL_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "汇总打印页面查询失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * zhumingwei add By 2012-09-03 查询经销商结算开票明细
	 */
	public void queryDealerBalanceDetail() {

		try {
			act.setForword(DEALER_BALANCE_DETAIL_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "汇总打印页面查询失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * Iverson add By 2010-12-16 打印收票报表(查询分页)
	 */
	public void printPorterQuery() {

		try {
			String bDate = request.getParamValue("bDate");
			String eDate = request.getParamValue("eDate");
			String name = request.getParamValue("name");
			String report_code = request.getParamValue("report_code");
			String financeName = request.getParamValue("financeName");
			String dealerCode = request.getParamValue("dealerCode");
			String dealerName = request.getParamValue("dealerName");
			String gzDate = request.getParamValue("gzDate");
			String gzDate1 = request.getParamValue("gzDate1");
			BalanceMainDao dao = new BalanceMainDao();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 15;
			PageResult<Map<String, Object>> ps = dao.printPorterQuery(bDate,
					eDate, name, report_code, financeName, gzDate, gzDate1,
					loginUser.getCompanyId(), pageSize, curPage,
					loginUser.getPoseId(), dealerCode, dealerName, "1");
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "汇总打印页面查询失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * zhumingwei add By 2012-09-03 查询经销商结算开票明细(查询分页)
	 */
	public void queryDealerBalanceDetailQuery() {

		try {
			String report_code = request.getParamValue("report_code");// 汇总编号
			String status = request.getParamValue("status");// 收票状态
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码
			String balance = request.getParamValue("balance");// 结算单号
			String yielyld = request.getParamValue("yielyld");

			BalanceMainDao dao = new BalanceMainDao();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 15;
			PageResult<Map<String, Object>> ps = dao
					.queryDealerBalanceDetailQuery(report_code, status,
							dealerCode, balance, yielyld, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * zhumingwei add By 2012-08-29 结算室收票状态明细(查询分页)
	 */
	public void queryBalanceDetailQuery() {

		try {
			String report_code = request.getParamValue("report_code");// 汇总编号
			String status = request.getParamValue("status");// 收票状态
			String bDate = request.getParamValue("bDate");// 收票日期起
			String eDate = request.getParamValue("eDate");// 收票日期止
			String gzDate = request.getParamValue("gzDate");// 汇总制单日期起
			String gzDate1 = request.getParamValue("gzDate1");// 汇总制单日期止
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码
			String yielyld = request.getParamValue("yielyld");// 生产基地
			String sp = request.getParamValue("sp");// 收票人

			BalanceMainDao dao = new BalanceMainDao();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 15;
			PageResult<Map<String, Object>> ps = dao.queryBalanceDetailQuery(
					report_code, status, bDate, eDate, gzDate, gzDate1,
					dealerCode, yielyld, sp, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void exportExcel() {

		try {
			String bDate = request.getParamValue("bDate");
			String eDate = request.getParamValue("eDate");
			String name = request.getParamValue("name");
			String report_code = request.getParamValue("report_code");
			String financeName = request.getParamValue("financeName");
			String gzDate = request.getParamValue("gzDate");
			String gzDate1 = request.getParamValue("gzDate1");
			BalanceMainDao dao = new BalanceMainDao();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 15;
			List<Map<String, Object>> list = dao.exportExcel(bDate, eDate,
					name, report_code, financeName, gzDate, gzDate1,
					loginUser.getCompanyId(), pageSize, curPage,
					loginUser.getPoseId());
			String[] head = new String[11];
			head[0] = "汇总单号";
			head[1] = "经销商代码";
			head[2] = "经销商名称";
			head[3] = "省份";
			head[4] = "发票号码";
			head[5] = "发票金额";
			head[6] = "税率";
			head[7] = "收票时间";
			head[8] = "挂账时间";
			head[9] = "财务人员";
			head[10] = "签收人";

			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[11];
						detail[0] = String.valueOf(map.get("REPORT_CODE"));
						detail[1] = String.valueOf(map.get("DEALER_CODE"));
						detail[2] = String.valueOf(map.get("DEALER_NAME"));
						detail[3] = String.valueOf(map.get("REGION_NAME"));
						detail[4] = String.valueOf(map.get("INVOICE_CODE"));
						detail[5] = String.valueOf(map.get("AMOUNT"));
						detail[6] = String.valueOf(map.get("TAX_RATE"));
						detail[7] = String.valueOf(map.get("AUTH_TIME"));
						detail[8] = String.valueOf(map.get("GZTIME"));
						detail[9] = String.valueOf(map.get("AUTH_PERSON_NAME"));
						detail[10] = String.valueOf(map.get("A_NAME"));

						list1.add(detail);
					}
				}
				com.infodms.dms.actions.claim.basicData.ToExcel.toNewExcel(
						ActionContext.getContext().getResponse(), request,
						head, list1, "导出收票报表.xls");
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "汇总打印页面查询失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// zhumingwei 2012-08-29
	public void exportExcel1() {

		try {
			String report_code = request.getParamValue("report_code");// 汇总编号
			String status = request.getParamValue("status");// 收票状态
			String bDate = request.getParamValue("bDate");// 收票日期起
			String eDate = request.getParamValue("eDate");// 收票日期止
			String gzDate = request.getParamValue("gzDate");// 汇总制单日期起
			String gzDate1 = request.getParamValue("gzDate1");// 汇总制单日期止
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码
			String yielyld = request.getParamValue("yielyld");// 生产基地
			String sp = request.getParamValue("sp");// 收票人
			BalanceMainDao dao = new BalanceMainDao();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 15;
			List<Map<String, Object>> list = dao.exportExcel1(report_code,
					status, bDate, eDate, gzDate, gzDate1, dealerCode, yielyld,
					sp, pageSize, curPage);
			String[] head = new String[11];
			head[0] = "分销中心";
			head[1] = "省份";
			head[2] = "经销商代码";
			head[3] = "经销商名称";
			head[4] = "开票单位名称";
			head[5] = "汇总单号";
			head[6] = "生产基地";
			head[7] = "金额";
			head[8] = "收票状态";
			head[9] = "收票时间";
			head[10] = "收票人";

			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[11];
						detail[0] = String.valueOf(map.get("ROOT_ORG_NAME"));
						detail[1] = String.valueOf(map.get("REGION_NAME"));
						detail[2] = String.valueOf(map.get("DEALER_CODE"));
						detail[3] = String.valueOf(map.get("DEALER_NAME"));
						detail[4] = String.valueOf(map.get("NAME"));
						detail[5] = String.valueOf(map.get("REPORT_CODE"));
						detail[6] = String.valueOf(map.get("YIELDLY"));
						detail[7] = String.valueOf(map.get("AUTH_AMOUNT"));
						detail[8] = String.valueOf(map.get("AUTH_STATUS"));
						detail[9] = String.valueOf(map.get("AUTH_TIME"));
						detail[10] = String
								.valueOf(map.get("AUTH_PERSON_NAME"));
						list1.add(detail);
					}
				}
				com.infodms.dms.actions.claim.basicData.ToExcel.toNewExcel(
						ActionContext.getContext().getResponse(), request,
						head, list1, "结算室收票状态明细.xls");
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// zhumingwei 2012-09-03
	public void exportExcel2() {

		try {
			String report_code = request.getParamValue("report_code");// 汇总编号
			String status = request.getParamValue("status");// 收票状态
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码
			String balance = request.getParamValue("balance");// 结算单号
			String yielyld = request.getParamValue("yielyld");
			BalanceMainDao dao = new BalanceMainDao();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 15;
			List<Map<String, Object>> list = dao.exportExcel2(report_code,
					status, dealerCode, balance, yielyld, pageSize, curPage);
			String[] head = new String[14];
			head[0] = "分销中心";
			head[1] = "省份";
			head[2] = "经销商代码";
			head[3] = "经销商名称";
			head[4] = "开票单位名称";
			head[5] = "汇总单号";
			head[6] = "结算单号";
			head[7] = "生产基地";
			head[8] = "结算起";
			head[9] = "结算止";
			head[10] = "金额";
			head[11] = "收票状态";
			head[12] = "收票时间";
			head[13] = "收票人";

			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[14];
						detail[0] = String.valueOf(map.get("ROOT_ORG_NAME"));
						detail[1] = String.valueOf(map.get("REGION_NAME"));
						detail[2] = String.valueOf(map.get("DEALER_CODE"));
						detail[3] = String.valueOf(map.get("DEALER_NAME"));
						detail[4] = String.valueOf(map.get("NAME"));
						detail[5] = String.valueOf(map.get("REPORT_CODE"));
						detail[6] = String.valueOf(map.get("BALANCE_NO"));
						detail[7] = String.valueOf(map.get("YIELDLY"));
						detail[8] = String.valueOf(map.get("START_DATE"));
						detail[9] = String.valueOf(map.get("END_DATE"));
						detail[10] = String.valueOf(map.get("INVOICE_AMOUNT"));
						detail[11] = String.valueOf(map.get("AUTH_STATUS"));
						detail[12] = String.valueOf(map.get("AUTH_TIME"));
						detail[13] = String
								.valueOf(map.get("AUTH_PERSON_NAME"));
						list1.add(detail);
					}
				}
				com.infodms.dms.actions.claim.basicData.ToExcel.toNewExcel(
						ActionContext.getContext().getResponse(), request,
						head, list1, "查询经销商结算开票明细.xls");
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * Iverson add By 2010-12-16 打印收票报表(打印)
	 */
	public void printPorter() {
		try {
			String bDate = request.getParamValue("bDate");
			String eDate = request.getParamValue("eDate");
			String name = request.getParamValue("name");
			String report_code = request.getParamValue("report_code");
			String financeName = request.getParamValue("financeName");
			String gzDate = request.getParamValue("gzDate");
			String gzDate1 = request.getParamValue("gzDate1");
			String flag = request.getParamValue("flag");
			BalanceMainDao dao = new BalanceMainDao();

			PageResult<Map<String, Object>> ps = dao.printPorterQuery(bDate,
					eDate, name, report_code, financeName, gzDate, gzDate1,
					loginUser.getCompanyId(), 10000, 1, loginUser.getPoseId(),
					null, null, flag);
			act.setOutData("list", ps.getRecords());
			act.setOutData("bDate", bDate);
			act.setOutData("eDate", eDate);
			act.setOutData("name", name);
			act.setForword(PORTER_PRINT_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "汇总打印页面查询失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*******
	 * add by liuxh 20110110 增加复核申请时退回功能
	 */
	public void backToAudit() {

		try {
			BalanceMainDao dao = new BalanceMainDao();
			String balanceId = request.getParamValue("id");// 结算单ID
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("UPDATE TT_AS_WR_CLAIM_BALANCE K SET K.STATUS=?,K.UPDATE_BY=?,K.UPDATE_DATE=SYSDATE WHERE K.ID=? ");
			List listPar = new ArrayList();
			listPar.add(Constant.ACC_STATUS_01);
			listPar.add(loginUser.getUserId());
			listPar.add(balanceId);
			dao.update(sbSql.toString(), listPar);
			String sql = "DELETE FROM TT_AS_WR_BALANCE_AUTHITEM WHERE AUTH_STATUS=? AND BALANCE_ID=? ";
			List listDel = new ArrayList();
			listDel.add(BalanceStatusRecord.STATUS_04);
			listDel.add(balanceId);
			dao.update(sql, listDel);
			BalanceStatusRecord.recordStatus(Long.parseLong(balanceId),
					loginUser.getUserId(), loginUser.getName(),
					loginUser.getOrgId(), BalanceStatusRecord.STATUS_15);
			act.setOutData("msg", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "复核申请退回失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * add by zuoxj 2011-01-26 结算单，财务在复核时的回退功能 步骤： 0、首先判断此结算单能不能回退 --
	 * 如果此结算单的结束日期与结算审核后日期相同，则允许回退，反正拒绝 1、将特殊费用状态改为-- 已结算
	 * 2、旧件抵扣与此结算单的关系断掉，并更改状态为-- 有效 10011001 3、奖惩与此结单的关系断掉，并更改状态为-- 未支付11511001
	 * 4、行政扣款与此结算单的关系断掉，将更改状态为-- 未支付11521001 5、清除审核此结算单的相应记录，11841006
	 * 6、清空此结算单的旧件扣款，考核扣款，行政扣款 7、重新计算结算费用
	 */
	public void financialBack() {
		try {
			BalanceMainDao dao = new BalanceMainDao();
			String balanceNo = request.getParamValue("balanceNo");

			// 0、首先判断此结算单能不能回退
			// -- 如果此结算单的结束日期与结算审核后日期相同，则允许回退，反正拒绝
			StringBuffer checkSql = new StringBuffer("\n");
			checkSql.append("select count(1) amount\n");
			checkSql.append("  from tt_as_wr_claim_balance b, tt_as_dealer_type d\n");
			checkSql.append(" where b.dealer_id = d.dealer_id\n");
			checkSql.append("   and b.end_date = d.balance_review_date\n");
			checkSql.append("   and b.balance_no = '").append(balanceNo)
					.append("'\n");
			Map<String, Object> map = dao.checkNum(checkSql.toString());

			// 将结算审核时期更改为此结算单起始日期的前一天
			StringBuffer viewdateSql = new StringBuffer("\n");
			viewdateSql.append("update tt_as_dealer_type t\n");
			viewdateSql
					.append("   set t.balance_review_date = (select b.start_date - 1\n");
			viewdateSql
					.append("                                  from tt_as_wr_claim_balance b\n");
			viewdateSql
					.append("                                 where b.balance_no = '")
					.append(balanceNo).append("')\n");
			viewdateSql.append(" where t.dealer_id =\n");
			viewdateSql.append("       (select b.dealer_id\n");
			viewdateSql.append("          from tt_as_wr_claim_balance b\n");
			viewdateSql.append("         where b.balance_no = '")
					.append(balanceNo).append("')\n");
			viewdateSql.append("   and t.yieldly = (select b.yieldly\n");
			viewdateSql
					.append("                      from tt_as_wr_claim_balance b\n");
			viewdateSql.append("                     where b.balance_no = '")
					.append(balanceNo).append("')\n");
			dao.update(viewdateSql.toString(), null);

			// 1、将特殊费用状态改为-- 已结算
			StringBuffer spefeeSql = new StringBuffer("\n");
			spefeeSql.append("update tt_as_wr_spefee s\n");
			spefeeSql.append("   set s.status = ")
					.append(Constant.SPEFEE_STATUS_06).append(",\n");
			spefeeSql.append("       s.update_date = sysdate,\n");
			spefeeSql.append("       s.update_by = ")
					.append(loginUser.getUserId()).append("\n");
			spefeeSql.append(" where s.status = ")
					.append(Constant.SPEFEE_STATUS_07).append("\n");
			spefeeSql.append("   and s.claimbalance_id = \n");
			spefeeSql
					.append("       (select id from tt_as_wr_claim_balance b where b.balance_no = '")
					.append(balanceNo).append("')\n");
			dao.update(spefeeSql.toString(), null);

			// 2、旧件抵扣与此结算单的关系断掉，并更改状态为-- 有效 10011001
			StringBuffer oldpartSql = new StringBuffer("\n");
			oldpartSql.append("update tt_as_wr_deduct_balance d\n");
			oldpartSql.append("   set d.status = ")
					.append(Constant.STATUS_ENABLE).append(",\n");
			oldpartSql.append("       d.claimbalance_id = '',\n");
			oldpartSql.append("       d.update_date = sysdate,\n");
			oldpartSql.append("       d.update_by = ")
					.append(loginUser.getUserId()).append("\n");
			oldpartSql.append(" where d.status = ")
					.append(Constant.STATUS_DISABLE).append("\n");
			oldpartSql.append("   and d.claimbalance_id =\n");
			oldpartSql
					.append("       (select id from tt_as_wr_claim_balance b where b.balance_no = '")
					.append(balanceNo).append("')\n");
			dao.update(oldpartSql.toString(), null);

			// 3、奖惩与此结单的关系断掉，并更改状态为-- 未支付11511001
			StringBuffer fineSql = new StringBuffer("\n");
			fineSql.append("update tt_as_wr_fine f\n");
			fineSql.append("   set f.claimbalance_id = '',\n");
			fineSql.append("       f.pay_status = ")
					.append(Constant.PAY_STATUS_01).append(",\n");
			fineSql.append("       f.update_by = ")
					.append(loginUser.getUserId()).append(",\n");
			fineSql.append("       f.update_date = sysdate\n");
			fineSql.append(" where f.pay_status = ")
					.append(Constant.PAY_STATUS_02).append("\n");
			fineSql.append("   and f.claimbalance_id =\n");
			fineSql.append(
					"       (select id from tt_as_wr_claim_balance b where b.balance_no = '")
					.append(balanceNo).append("')\n");
			dao.update(fineSql.toString(), null);

			// 4、行政扣款与此结算单的关系断掉，将更改状态为-- 未支付11521001
			StringBuffer adminSql = new StringBuffer("\n");
			adminSql.append("update tt_as_wr_admin_deduct d\n");
			adminSql.append("   set d.claimbalance_id = '',\n");
			adminSql.append("       d.deduct_status = ")
					.append(Constant.ADMIN_STATUS_01).append(",\n");
			adminSql.append("       d.update_by = ")
					.append(loginUser.getUserId()).append(",\n");
			adminSql.append("       d.update_date = sysdate\n");
			adminSql.append(" where d.deduct_status = ")
					.append(Constant.ADMIN_STATUS_02).append("\n");
			adminSql.append("   and d.claimbalance_id =\n");
			adminSql.append(
					"       (select id from tt_as_wr_claim_balance b where b.balance_no = '")
					.append(balanceNo).append("')\n");
			dao.update(adminSql.toString(), null);

			// 5、清除审核此结算单的相应记录，11861006
			StringBuffer authSql = new StringBuffer("\n");
			authSql.append("delete from tt_as_wr_balance_authitem a\n");
			authSql.append(" where a.auth_status = ")
					.append(BalanceStatusRecord.STATUS_06).append("\n");
			authSql.append("   and a.balance_id =\n");
			authSql.append(
					"       (select id from tt_as_wr_claim_balance b where b.balance_no = '")
					.append(balanceNo).append("')\n");
			dao.delete(authSql.toString(), null);

			// 6、清空此结算单的旧件扣款，考核扣款，行政扣款
			StringBuffer balanceSql = new StringBuffer("\n");
			balanceSql.append("update tt_as_wr_claim_balance b\n");
			balanceSql.append("   set b.check_deduct = 0,\n");
			balanceSql.append("       b.old_deduct = 0,\n");
			balanceSql.append("       b.admin_deduct = 0,\n");
			balanceSql.append("       b.FREE_DEDUCT = 0,\n");
			balanceSql.append("       b.SERVICE_DEDUCT = 0,\n");
			balanceSql
					.append("       b.return_amount = b.return_amount_bak,\n");
			balanceSql.append("       b.status = ")
					.append(Constant.ACC_STATUS_02).append(",\n");
			balanceSql.append("       b.update_by = ")
					.append(loginUser.getUserId()).append(",\n");
			balanceSql.append("       b.update_date = sysdate\n");
			balanceSql.append(" where b.balance_no = '").append(balanceNo)
					.append("'\n");
			dao.update(balanceSql.toString(), null);

			// 7、重新计算结算费用 -- 引用复核申请统计金额的SQL
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("UPDATE TT_AS_WR_CLAIM_BALANCE SET BALANCE_AMOUNT=((NVL(LABOUR_AMOUNT,0)+NVL(PART_AMOUNT,0)+NVL(OTHER_AMOUNT,0)+NVL(FREE_AMOUNT,0)");
			sbSql.append("+NVL(RETURN_AMOUNT,0)+NVL(MARKET_AMOUNT,0)+NVL(SPEOUTFEE_AMOUNT,0)+NVL(APPEND_AMOUNT,0)+NVL(SERVICE_TOTAL_AMOUNT,0))");
			sbSql.append("-(NVL(FREE_DEDUCT,0)+NVL(SERVICE_DEDUCT,0)+NVL(CHECK_DEDUCT,0)+NVL(OLD_DEDUCT,0)+NVL(ADMIN_DEDUCT,0)+NVL(FINANCIAL_DEDUCT,0)))\n");
			sbSql.append("WHERE balance_no = '").append(balanceNo)
					.append("'\n");
			dao.update(sbSql.toString(), null);

			// 记录日志表 -- 添加一个经销商确认时间
			TtAsWrClaimBalancePO po = new TtAsWrClaimBalancePO();
			po.setBalanceNo(balanceNo);
			BalanceStatusRecord.recordStatus(((TtAsWrClaimBalancePO) dao
					.select(po).get(0)).getId(), loginUser.getUserId(),
					loginUser.getName(), loginUser.getOrgId(),
					BalanceStatusRecord.STATUS_BACK);

			act.setOutData("flag", true);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "财务复核退回失败");
			logger.error(loginUser, e1);
			act.setOutData("flag", false);
			act.setException(e1);
		}
	}
	/**
	 * 检验每一步是否结算的钱有问题
	 */
	public void checkMoneyByBalanceNo(){
		String balanecNo = DaoFactory.getParam(request, "balanecNo");
		BalanceMainDao dao = new BalanceMainDao();
		int res=dao.checkMoneyByBalanceNo(balanecNo);
		super.setJsonSuccByres(res);
	}
	
	
	public void removePayment(){
		String id = request.getParamValue("paymentId");
		
		TtAsPaymentPO pay=new TtAsPaymentPO();
		pay.setId(Long.parseLong(id));
		BalanceMainDao dao = new BalanceMainDao();
		dao.delete(pay);
		act.setOutData("result", true);
		System.out.println(id);

		
	}
	
	
	
}
