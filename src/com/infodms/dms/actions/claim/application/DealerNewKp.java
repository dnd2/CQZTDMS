package com.infodms.dms.actions.claim.application;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.balanceAuditing.BalanceAuditing;
import com.infodms.dms.actions.claim.dealerClaimMng.ApplicationClaim;
import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.DealerBalanceBean;
import com.infodms.dms.bean.auditBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.application.DealerBalanceDao;
import com.infodms.dms.dao.claim.application.DealerKpDao;
import com.infodms.dms.dao.claim.application.DealerNewKpUpdateDAO;
import com.infodms.dms.dao.claim.authorization.BalanceMainDao;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.feedbackMng.MarketQuesOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmDealerInvoiceIntercalatePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmPtOlderItemPO;
import com.infodms.dms.po.TtAsDealerCheckPO;
import com.infodms.dms.po.TtAsDealerTypePO;
import com.infodms.dms.po.TtAsPaymentPO;
import com.infodms.dms.po.TtAsWrAdministrativeChargePO;
import com.infodms.dms.po.TtAsWrApplicationClaimPO;
import com.infodms.dms.po.TtAsWrAuthCheckAppPO;
import com.infodms.dms.po.TtAsWrBalanceAuthitemPO;
import com.infodms.dms.po.TtAsWrBalanceBillPO;
import com.infodms.dms.po.TtAsWrBqhsKpPO;
import com.infodms.dms.po.TtAsWrCheckApplicationPO;
import com.infodms.dms.po.TtAsWrCheckDetailPO;
import com.infodms.dms.po.TtAsWrClaimBalanceFinePO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrClaimBalanceTmpPO;
import com.infodms.dms.po.TtAsWrClaimSendTemPO;
import com.infodms.dms.po.TtAsWrFinePO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.po.TtAsWrReturnedOrderPO;
import com.infodms.dms.po.TtIfMarketPO;
import com.infodms.dms.po.TtInvoiceTaxratePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.entity.special.TtAsWrSpeGoodwillClaimPO;
import com.infodms.yxdms.entity.special.TtAsWrSpecialPO;
import com.infodms.yxdms.service.InvoiceService;
import com.infodms.yxdms.service.impl.InvoiceServiceImpl;
import com.infodms.yxdms.utils.BaseUtils;
import com.infoservice.dms.chana.actions.OSB34;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

public class DealerNewKp extends BaseAction{
	private Logger logger = Logger.getLogger(DealerBalance.class);
	protected POFactory factory = POFactoryBuilder.getInstance();
	private final  ApplicationClaim ation=new ApplicationClaim();
	private final DealerKpDao dao = DealerKpDao.getInstance();
	private final DealerNewKpUpdateDAO daoKP = DealerNewKpUpdateDAO.getInstance();
	private final String finViewUrl01 = "/jsp/claim/authorization/balanceInfoView01.jsp";
	private final String finAdd = "/jsp/claim/authorization/balanceInfoAdd.jsp";
	private final String finsp = "/jsp/claim/authorization/balanceInfoSp.jsp";
	private final String printUrl = "/jsp/claim/authorization/balancePrint.jsp";
	private final String Appprint_ALL = "/jsp/claim/authorization/AppbalancePrintAll.jsp";
	private final String AppprintXiao = "/jsp/claim/authorization/AppprintXiao.jsp";
	private final String DealerUnitKpXiao = "/jsp/claim/authorization/DealerUnitKpXiao.jsp";
	
	
	private final String AppprintRk = "/jsp/claim/authorization/AppprintRk.jsp";
	
	private final String AppprintUrl = "/jsp/claim/authorization/AppbalancePrint.jsp";
	
	private final String Appprint = "/jsp/claim/authorization/AppClaimPrint.jsp";

	//zhumingwei 2011-11-01
	private final String DEALER_CHECK_APPLICATION_URL = "/jsp/claim/authorization/dealerCheckApplication.jsp";
	private final String DEALER_CHECK_PRINT_URL = "/jsp/claim/authorization/dealerCheckPrint.jsp";
	
	//zhumingwei 2011-11-08
	private final String DEALER_CHECK_APP_AUTH_URL = "/jsp/claim/authorization/dealerCheckAppAuth.jsp";
	private final String DEALER_CHECK_APP_AUTH_INIT_URL = "/jsp/claim/authorization/dealerCheckAppAuthInit.jsp";
	private final String DEALER_CHECK_APP_AUTH_INFO_URL = "/jsp/claim/authorization/dealerCheckAppAuthInfo.jsp";
	private final String DEALER_CHECK_APP_DETAIL_INFO_URL = "/jsp/claim/authorization/getDetailInfo.jsp";
	private final String DETAIL_INFO_URL = "/jsp/claim/authorization/detailInfo.jsp";
	
	//zhumingwei 2011-12-28
	private final String DEALER_CHECK_APPLICATION_DEL_URL = "/jsp/claim/authorization/dealerCheckApplicationDetail.jsp";
	
	private final String DEALER_CHECK_APP_DEL_URL = "/jsp/claim/authorization/dealerCheckAppDetail.jsp";
	
	private final String CLAIM_DETAIL = "/jsp/claim/authorization/claimDetail.jsp";
	private final String auditByOne = "/jsp/claim/authorization/auditByOne.jsp";
	private DealerKpDao balanceDao = DealerKpDao.getInstance();
	/**
	 * 经销商结算操作首页 
	 * 2010-10-20 修改
	 * 描述：
	 *     1、查询对应经销商级别传输到查询页面
	 *        用途：用于控制查询结果中操作中控制
	 *        详细请参考页面控制流程。
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void dealerKpInit() {
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			
			//查询经销商级别
			Integer dealerLevel= Constant.DEALER_LEVEL_02;//默认为二级
			
			String dealerId = logonUser.getDealerId();
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(Long.parseLong(dealerId));
			List<TmDealerPO> dealerList = balanceDao.select(dealerPO);
			
			if(dealerList!=null && dealerList.size()>0){
				TmDealerPO tempDealerPO = dealerList.get(0);
				if(tempDealerPO.getDealerLevel()!=null)
					dealerLevel = tempDealerPO.getDealerLevel();
			}
			/*****add by liu liuxh 20101115 增加结算级别*****/
			TmDealerPO balanceDealer=balanceDao.queryBalanceMaker(Long.parseLong(dealerId));
			act.setOutData("balanceLevel", balanceDealer.getBalanceLevel()); //结算级别
			/*****add by liu liuxh 20101115 增加结算级别*****/
			
			act.setOutData("dealerLevel", dealerLevel);
			act.setOutData("dealerId", dealerId);
			act.setForword("/jsp/claim/application/dealerKpQuery.jsp");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void addDealerUnitKp()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			StringBuffer sql= new StringBuffer();
			sql.append("select  to_char(max(t.end_date) +1,'yyyy-mm-dd') end_date from tt_as_wr_bqhs_kp  t");
			Map<String, Object> map = balanceDao.pageQueryMap(sql.toString(), null, balanceDao.getFunName());
			String END_DATE = "";
			if(map != null && map.get("END_DATE") != null && Utility.testString(map.get("END_DATE").toString()) )
			{
				END_DATE = map.get("END_DATE").toString();
			}else
			{
				END_DATE = "2015-01-01";
			}
            act.setOutData("END_DATE", END_DATE);
			act.setForword("/jsp/claim/application/addDealerUnitKp.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"北汽幻速开票新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void DealerUnitKpSh()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword("/jsp/claim/application/addDealerUnitKpSh.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"北汽幻速开票新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	
	public void DealerUnitKpFp()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = request.getParamValue("id");
			
//			String INVOICE_NO = request.getParamValue("INVOICE_NO");
//			String INVOICE_BATCH_NO = request.getParamValue("INVOICE_BATCH_NO");
			
//			kpPO.setInvoiceNo(INVOICE_NO);
//			kpPO.setInvoiceBatchNo(INVOICE_BATCH_NO);
			act.setOutData("id", id);
			act.setForword("/jsp/claim/application/DealerUnitKpFp.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"北汽幻速开票生成");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void DealerUnitKpFpU()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = request.getParamValue("id");
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			String INVOICE_NO = request.getParamValue("INVOICE_NO");
			String INVOICE_BATCH_NO = request.getParamValue("INVOICE_BATCH_NO");
			 TtAsWrBqhsKpPO kpPO1 = new TtAsWrBqhsKpPO();
			 kpPO1.setId(Long.parseLong(id));
			
		    TtAsWrBqhsKpPO kpPO = new TtAsWrBqhsKpPO();
			kpPO.setInvoiceNo(INVOICE_NO);
			kpPO.setInvoiceBatchNo(INVOICE_BATCH_NO);
			
			balanceDao.update(kpPO1, kpPO);
			
			act.setForword("/jsp/claim/application/dealerUnitKp.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"北汽幻速开票生成");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	
	public void addDealerUnitKpPo()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
//			String INVOICE_NO = request.getParamValue("INVOICE_NO");
//			String INVOICE_BATCH_NO = request.getParamValue("INVOICE_BATCH_NO");
			TtAsWrBqhsKpPO kpPO = new TtAsWrBqhsKpPO();
			kpPO.setId(Long.parseLong(SequenceManager.getSequence("")));
			kpPO.setStartDate(Utility.parseString2Date(startDate, ""));
			kpPO.setEndDate(Utility.parseString2Date(endDate, ""));
			kpPO.setGunit("北汽银翔汽车有限公司");
			kpPO.setXunit("重庆北汽幻速汽车销售有限公司");
			String[] endDates = endDate.split("-");
			kpPO.setBroNo(endDates[0]+endDates[1]  + "0001");
			kpPO.setStatus(94761001);
			kpPO.setCreateBy(logonUser.getUserId());
			kpPO.setCreateDate(new Date());
//			kpPO.setInvoiceNo(INVOICE_NO);
//			kpPO.setInvoiceBatchNo(INVOICE_BATCH_NO);
			balanceDao.insert(kpPO);
			act.setForword("/jsp/claim/application/dealerUnitKp.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"北汽幻速开票生成");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void  ClaimsSettlementKp()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword("/jsp/claim/application/ClaimsSettlementKp.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算汇总");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void DealerCountKp()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword("/jsp/claim/application/DealerCountKp.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算汇总");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void DealerUSh()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = request.getParamValue("id");
			String APPROVAL_REMARKS = request.getParamValue("APPROVAL_REMARKS");
			String STATUS = request.getParamValue("STATUS");
			TtAsWrBqhsKpPO kpPO = new TtAsWrBqhsKpPO();
			kpPO.setId(Long.parseLong(id));
			TtAsWrBqhsKpPO kpPO1 = new TtAsWrBqhsKpPO();
			kpPO1.setStatus(Integer.parseInt(STATUS));
			kpPO1.setApprovalDate(new Date());
			kpPO1.setApprovalBy(logonUser.getUserId());
			if(Utility.testString(APPROVAL_REMARKS))
				kpPO1.setApprovalRemarks(APPROVAL_REMARKS);
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			balanceDao.update(kpPO, kpPO1);
			act.setForword("/jsp/claim/application/addDealerUnitKpSh.jsp");
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"北汽幻速开票");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void DealerUnitKp()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword("/jsp/claim/application/dealerUnitKp.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"北汽幻速开票");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void DealerUnitKpJsSh()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String CHECK_TICKETS_DATE_S = CommonUtils.checkNull(request.getParamValue("CHECK_TICKETS_DATE_S"));		//产地代码
			String CHECK_TICKETS_DATE = CommonUtils.checkNull(request.getParamValue("CHECK_TICKETS_DATE"));	//结算单号
			String ID = CommonUtils.checkNull(request.getParamValue("ID"));	//结算单号
			StringBuffer sql= new StringBuffer();
			sql.append("\n" );
			sql.append("select\n" );
			sql.append("sum(nvl(a.balance_part_amount,0)+nvl(a.accessories_price,0)) balance_part_amount,\n" );
			sql.append("sum(\n" );
			sql.append("case when a.claim_type = 10661002\n" );
			sql.append(" then nvl(a.balance_amount,0)\n" );
			sql.append("when a.claim_type = 10661011\n" );
			sql.append(" then nvl(a.balance_amount,0)\n" );
			sql.append(" else 0 end ) balance_amount\n" );
			sql.append("from tt_as_wr_application a , tt_as_wr_claim_balance b,(select max(t.TRANSFER_TICKETS_DATE) check_tickets_date ,balance_oder  from tt_as_payment t group by t.balance_oder) C\n" );
			sql.append("where C.balance_oder = B.remark   and  a.status != 10791005 and a.status != 10791006 and a.balance_no = b.remark\n" );
			sql.append("and  c.CHECK_TICKETS_DATE >= to_date('"+CHECK_TICKETS_DATE_S+"','yyyy-mm-dd')\n" );
			sql.append("and c.CHECK_TICKETS_DATE < to_date('"+CHECK_TICKETS_DATE+"','yyyy-mm-dd')+1");
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			Map<String, Object> aMap = balanceDao.pageQueryMap(sql.toString(), null, balanceDao.getFunName());
			
		    sql= new StringBuffer();
			sql.append("\n" );
			sql.append("select\n" );
			sql.append("sum(nvl(b.amount_sum,0)) amount_sum\n" );
			sql.append("from  tt_as_wr_claim_balance b ,(select max(t.TRANSFER_TICKETS_DATE) check_tickets_date ,balance_oder  from tt_as_payment t group by t.balance_oder) C \n" );
			sql.append("where\n" );
			sql.append(" C.balance_oder = B.remark  and  c.CHECK_TICKETS_DATE >= to_date('"+CHECK_TICKETS_DATE_S+"','yyyy-mm-dd')\n" );
			sql.append("and c.CHECK_TICKETS_DATE <= to_date('"+CHECK_TICKETS_DATE+"','yyyy-mm-dd')+1");
			Map<String, Object> bMap = balanceDao.pageQueryMap(sql.toString(), null, balanceDao.getFunName());
			String AMOUNT_SUM_01 = "0";
			String BALANCE_AMOUNT_01 = "0";
			String BALANCE_PART_AMOUNT_01 = "0";
			if(bMap.get("AMOUNT_SUM")!=null &&  bMap.get("AMOUNT_SUM").toString().length() >  0 ){
				AMOUNT_SUM_01 = bMap.get("AMOUNT_SUM").toString();
			}
			if(aMap.get("BALANCE_AMOUNT")!=null &&  aMap.get("BALANCE_AMOUNT").toString().length() >  0 ){
				BALANCE_AMOUNT_01 =  aMap.get("BALANCE_AMOUNT").toString();
			}
			if(aMap.get("BALANCE_PART_AMOUNT")!=null &&  aMap.get("BALANCE_PART_AMOUNT").toString().length() >  0  ){
				BALANCE_PART_AMOUNT_01 = aMap.get("BALANCE_PART_AMOUNT").toString();
			}
			double  AMOUNT_SUM = Arith.sub(Double.parseDouble(AMOUNT_SUM_01), Double.parseDouble(BALANCE_AMOUNT_01));
			double  BALANCE_PART_AMOUNT = Double.parseDouble(BALANCE_PART_AMOUNT_01);
			double  BALANCE_L_AMOUNT = Arith.sub(AMOUNT_SUM,BALANCE_PART_AMOUNT);
			act.setOutData("AMOUNT_SUM", AMOUNT_SUM);
			act.setOutData("BRO_NO", request.getParamValue("BRO_NO"));
			act.setOutData("BALANCE_PART_AMOUNT", BALANCE_PART_AMOUNT);
			act.setOutData("BALANCE_L_AMOUNT", BALANCE_L_AMOUNT);
			act.setOutData("ID",ID);
			act.setForword("/jsp/claim/application/DealerUnitKpJsSh.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算汇总");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void DealerUnitKpJs()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String CHECK_TICKETS_DATE_S = CommonUtils.checkNull(request.getParamValue("CHECK_TICKETS_DATE_S"));		//产地代码
			String CHECK_TICKETS_DATE = CommonUtils.checkNull(request.getParamValue("CHECK_TICKETS_DATE"));	//结算单号
			StringBuffer sql= new StringBuffer();
			sql.append("\n" );
			sql.append("select\n" );
			sql.append("sum(( case when a.claim_type=10661002 then\n" );
			sql.append("           0\n" );
			sql.append("         when a.claim_type = 10661011 then\n" );
			sql.append("           0\n" );
			sql.append("         else\n" );
			sql.append("          nvl(a.balance_part_amount, 0) end)\n" );
			sql.append("      + nvl(a.accessories_price, 0)\n" );
			sql.append("     ) balance_part_amount,");
			sql.append("sum(\n" );
			sql.append("case when a.claim_type = 10661002\n" );
			sql.append(" then nvl(a.balance_amount,0)\n" );
			sql.append("when a.claim_type = 10661011\n" );
			sql.append(" then nvl(a.balance_amount,0)\n" );
			sql.append(" else 0 end ) balance_amount\n" );
			sql.append("from tt_as_wr_application a , tt_as_wr_claim_balance b,(select max(t.TRANSFER_TICKETS_DATE) TRANSFER_TICKETS_DATE ,balance_oder  from tt_as_payment t group by t.balance_oder) C\n" );
			sql.append("where C.balance_oder = B.remark  and  a.status != 10791005 and a.status != 10791006 and a.balance_no = b.remark\n" );
			sql.append("and  c.TRANSFER_TICKETS_DATE >= to_date('"+CHECK_TICKETS_DATE_S+"','yyyy-mm-dd')\n" );
			sql.append("and c.TRANSFER_TICKETS_DATE < to_date('"+CHECK_TICKETS_DATE+"','yyyy-mm-dd')+1");
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			Map<String, Object> aMap = balanceDao.pageQueryMap(sql.toString(), null, balanceDao.getFunName());
			
		    sql= new StringBuffer();
			sql.append("\n" );
			sql.append("select\n" );
			sql.append("sum(nvl(b.amount_sum,0)) amount_sum\n" );
			sql.append("from  tt_as_wr_claim_balance b ,(select max(t.TRANSFER_TICKETS_DATE) TRANSFER_TICKETS_DATE ,balance_oder  from tt_as_payment t group by t.balance_oder) C \n" );
			sql.append("where\n" );
			sql.append(" C.balance_oder = B.remark  and  c.TRANSFER_TICKETS_DATE >= to_date('"+CHECK_TICKETS_DATE_S+"','yyyy-mm-dd')\n" );
			sql.append("and c.TRANSFER_TICKETS_DATE <= to_date('"+CHECK_TICKETS_DATE+"','yyyy-mm-dd')+1");
			Map<String, Object> bMap = balanceDao.pageQueryMap(sql.toString(), null, balanceDao.getFunName());
			String AMOUNT_SUM_01 = "0";
			String BALANCE_AMOUNT_01 = "0";
			String BALANCE_PART_AMOUNT_01 = "0";
			if(bMap.get("AMOUNT_SUM")!=null &&  bMap.get("AMOUNT_SUM").toString().length() >  0 ){
				AMOUNT_SUM_01 = bMap.get("AMOUNT_SUM").toString();//索赔结算单结算金额
			}
			if(aMap.get("BALANCE_AMOUNT")!=null &&  aMap.get("BALANCE_AMOUNT").toString().length() >  0 ){
				BALANCE_AMOUNT_01 =  aMap.get("BALANCE_AMOUNT").toString();//PDI,保养的费用
			}
			if(aMap.get("BALANCE_PART_AMOUNT")!=null &&  aMap.get("BALANCE_PART_AMOUNT").toString().length() >  0  ){
				BALANCE_PART_AMOUNT_01 = aMap.get("BALANCE_PART_AMOUNT").toString();//材料费（除pdi和保养的）
			}
			double  AMOUNT_SUM = Arith.sub(Double.parseDouble(AMOUNT_SUM_01), Double.parseDouble(BALANCE_AMOUNT_01));//索赔结算单结算费用-pdi和保养结算费用=材料费
			double  BALANCE_PART_AMOUNT = Double.parseDouble(BALANCE_PART_AMOUNT_01);//材料费（除pdi和保养的）
			double  BALANCE_L_AMOUNT = Arith.sub(AMOUNT_SUM,BALANCE_PART_AMOUNT);
			act.setOutData("AMOUNT_SUM", AMOUNT_SUM);
			act.setOutData("BRO_NO", request.getParamValue("BRO_NO"));
			act.setOutData("BALANCE_PART_AMOUNT", BALANCE_PART_AMOUNT);
			act.setOutData("BALANCE_L_AMOUNT", BALANCE_L_AMOUNT);
			act.setForword("/jsp/claim/application/DealerUnitKpJs.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算汇总");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void dealerOnKp()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword("/jsp/claim/application/dealerOnKp.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void DealerUnitKpDel()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = request.getParamValue("id");
			TtAsWrBqhsKpPO kpPO = new TtAsWrBqhsKpPO();
			kpPO.setId(Long.parseLong(id));
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			balanceDao.delete(kpPO);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void DealerUnitKpCx()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = request.getParamValue("id");
			TtAsWrBqhsKpPO kpPO = new TtAsWrBqhsKpPO();
			kpPO.setId(Long.parseLong(id));
			TtAsWrBqhsKpPO kpPO1 = new TtAsWrBqhsKpPO();
			kpPO1.setStatus(94761001 );
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			balanceDao.update(kpPO, kpPO1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void DealerUnitKpsb()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = request.getParamValue("id");
			TtAsWrBqhsKpPO kpPO = new TtAsWrBqhsKpPO();
			kpPO.setId(Long.parseLong(id));
			TtAsWrBqhsKpPO kpPO1 = new TtAsWrBqhsKpPO();
			kpPO1.setStatus(94761002);
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			balanceDao.update(kpPO, kpPO1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void DealerUnitKpXhImport()
	{
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
		OutputStream os = null;
		String CHECK_TICKETS_DATE_S= request.getParamValue("CHECK_TICKETS_DATE_S");
		String CHECK_TICKETS_DATE= request.getParamValue("CHECK_TICKETS_DATE");
		String BRO_NO= request.getParamValue("BRO_NO");
		
		
		String INVOICE_NO= request.getParamValue("INVOICE_NO");
		String INVOICE_BATCH_NO= request.getParamValue("INVOICE_BATCH_NO");
		
		DealerKpDao dealerkpdao = new DealerKpDao();
		String bf = "";
		double sl = 0.00;
		double cs = 0.00;
		bf = "17%";
		sl = 1.17;
		cs = 1;
			
		List<Map<String, Object>> list1 = dealerkpdao.DealerUnitKpXiao(CHECK_TICKETS_DATE_S,CHECK_TICKETS_DATE, sl,cs);

		
		ResponseWrapper response = act.getResponse();
		// 导出的文件名
		String fileName = "销货清单.csv";
		// 导出的文字编码
		fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
		response.setContentType("Application/text/csv");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		// 定义一个集合
		List<List<Object>> list = new LinkedList<List<Object>>();
		// 标题
		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("序号");
		listTemp.add("货物（劳务）名称");
		listTemp.add("规格型号");
		listTemp.add("单位");
		listTemp.add("数量");
		listTemp.add("单价");
		listTemp.add("金额");
		listTemp.add("税率");
		listTemp.add("税额");
		list.add(listTemp);
		List<Map<String, Object>> rslist = list1;
		Map map=new HashMap();
		if(rslist!=null){
		for (int i = 0; i < rslist.size(); i++) {
			map = rslist.get(i);
			List<Object> listValue = new LinkedList<Object>();
			listValue = new LinkedList<Object>();
			listValue.add(i+1);
			listValue.add(map.get("PART_NAME") != null ? map.get("PART_NAME") : "");
			listValue.add(map.get("PART_CODE") != null ? map.get("PART_CODE") : "");
			listValue.add("件");
			listValue.add(map.get("BALANCE_QUANTITY") != null ? map.get("BALANCE_QUANTITY") : "");
			listValue.add(map.get("PRICE") != null ? map.get("PRICE") : "");
			listValue.add(map.get("BALANCE_AMOUNT") != null ? map.get("BALANCE_AMOUNT") : "");
			listValue.add(bf);
			listValue.add(map.get("S_BALANCE_AMOUNT") != null ? map.get("S_BALANCE_AMOUNT") : "");
			list.add(listValue);
		}
		}
		os = response.getOutputStream();
		CsvWriterUtil.writeCsv(list, os);
		os.flush();	
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	
	public void  DealerUnitKpImport()
	{
		ActionContext act = ActionContext.getContext();
		OutputStream os = null;
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
		
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));		//产地代码
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));	//结算单号
			
			String startDate_1 = CommonUtils.checkNull(request.getParamValue("startDate_1"));		//产地代码
			String endDate_1 = CommonUtils.checkNull(request.getParamValue("endDate_1"));	//结算单号
			
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					
				StringBuffer sql= new StringBuffer();
				sql.append("select t.id , to_char( t.start_date,'yyyy-mm-dd') CHECK_TICKETS_DATE_S,\n" );
				sql.append("to_char(t.end_date , 'yyyy-mm-dd') CHECK_TICKETS_DATE ,\n" );
				sql.append("t.gunit , t.xunit ,t.bro_no ,tc.CODE_DESC status, a.name ,to_char(t.create_date , 'yyyy-mm-dd') create_date,\n" );
				sql.append("b.name approval_name , to_char(t.approval_date,'yyyy-mm-dd') approval_date,t.approval_remarks, t.invoice_no\n" );
				sql.append(",t.invoice_batch_no\n" );
				sql.append("from  tc_user a,tc_code tc , tt_as_wr_bqhs_kp t left join tc_user b on b.user_id = t.approval_by   where t.status = tc.CODE_ID and  a.user_id = t.create_by");

				if(Utility.testString(startDate))
					sql.append(" and   t.start_date >= to_date('"+startDate+"','yyyy-mm-dd')  ");
				if(Utility.testString(endDate))
					sql.append(" and   t.start_date <=  to_date('"+endDate+"','yyyy-mm-dd')  ");
				
				if(Utility.testString(startDate_1))
					sql.append(" and t.end_date >= to_date('"+startDate_1+"','yyyy-mm-dd')  ");
				if(Utility.testString(endDate_1))
					sql.append(" and  t.end_date <=  to_date('"+endDate_1+"','yyyy-mm-dd')  ");
			    List<Map<String, Object>> ps =balanceDao.pageQuery(sql.toString(), null, balanceDao.getFunName());
			    ResponseWrapper response = act.getResponse();
				// 导出的文件名
				String fileName = "北汽幻速开票.csv";
				// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
				response.setContentType("Application/text/csv");
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
				// 定义一个集合
				List<List<Object>> list = new LinkedList<List<Object>>();
				// 标题
				List<Object> listTemp = new LinkedList<Object>();
				listTemp.add("序号");
				listTemp.add("转账开始时间");
				listTemp.add("转账结束时间");
				listTemp.add("购货方");
				listTemp.add("销货方");
				listTemp.add("开票单号");
				listTemp.add("状态");
				listTemp.add("创建人");
				listTemp.add("创建时间");
				listTemp.add("审核人");
				listTemp.add("审核时间");
				listTemp.add("审核备注");
				listTemp.add("发票号");
				listTemp.add("发票批号");
				list.add(listTemp);
				List<Map<String, Object>> rslist = ps;
				Map map=new HashMap();
				if(rslist!=null){
				for (int i = 0; i < rslist.size(); i++) {
					map = rslist.get(i);
					List<Object> listValue = new LinkedList<Object>();
					listValue = new LinkedList<Object>();
					listValue.add(i+1);
					listValue.add(map.get("CHECK_TICKETS_DATE_S") != null ? map.get("CHECK_TICKETS_DATE_S") : "");
					listValue.add(map.get("CHECK_TICKETS_DATE") != null ? map.get("CHECK_TICKETS_DATE") : "");
					listValue.add(map.get("GUNIT") != null ? map.get("GUNIT") : "");
					listValue.add(map.get("XUNIT") != null ? map.get("XUNIT") : "");
					listValue.add(map.get("BRO_NO") != null ? map.get("BRO_NO") : "");
					listValue.add(map.get("STATUS") != null ? map.get("STATUS") : "");
					listValue.add(map.get("NAME") != null ? map.get("NAME") : "");
					listValue.add(map.get("CREATE_DATE") != null ? map.get("CREATE_DATE") : "");
					listValue.add(map.get("APPROVAL_NAME") != null ? map.get("APPROVAL_NAME") : "");
					listValue.add(map.get("APPROVAL_DATE") != null ? map.get("APPROVAL_DATE") : "");
					listValue.add(map.get("APPROVAL_REMARKS") != null ? map.get("APPROVAL_REMARKS") : "");
					listValue.add(map.get("INVOICE_NO") != null ? map.get("INVOICE_NO") : "");
					listValue.add(map.get("INVOICE_BATCH_NO") != null ? map.get("INVOICE_BATCH_NO") : "");
					
					list.add(listValue);
				}
				}
				os = response.getOutputStream();
				CsvWriterUtil.writeCsv(list, os);
				os.flush();			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void DealerUnitKpQuery()
	{

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
		
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));		//产地代码
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));	//结算单号
			
			String startDate_1 = CommonUtils.checkNull(request.getParamValue("startDate_1"));		//产地代码
			String endDate_1 = CommonUtils.checkNull(request.getParamValue("endDate_1"));	//结算单号
			
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					
				StringBuffer sql= new StringBuffer();
				sql.append("select t.id , to_char( t.start_date,'yyyy-mm-dd') CHECK_TICKETS_DATE_S,\n" );
				sql.append("to_char(t.end_date , 'yyyy-mm-dd') CHECK_TICKETS_DATE ,\n" );
				sql.append("t.gunit , t.xunit ,t.bro_no ,t.status, a.name ,to_char(t.create_date , 'yyyy-mm-dd') create_date,\n" );
				sql.append("b.name approval_name , to_char(t.approval_date,'yyyy-mm-dd') approval_date,t.approval_remarks, t.invoice_no\n" );
				sql.append(",t.invoice_batch_no\n" );
				sql.append("from  tc_user a, tt_as_wr_bqhs_kp t left join tc_user b on b.user_id = t.approval_by   where a.user_id = t.create_by");

				if(Utility.testString(startDate))
					sql.append(" and   t.start_date >= to_date('"+startDate+"','yyyy-mm-dd')  ");
				if(Utility.testString(endDate))
					sql.append(" and   t.start_date <=  to_date('"+endDate+"','yyyy-mm-dd')  ");
				
				if(Utility.testString(startDate_1))
					sql.append(" and t.end_date >= to_date('"+startDate_1+"','yyyy-mm-dd')  ");
				if(Utility.testString(endDate_1))
					sql.append(" and  t.end_date <=  to_date('"+endDate_1+"','yyyy-mm-dd')  ");
			PageResult<Map<String, Object>> ps =balanceDao.pageQuery(sql.toString(), null, balanceDao.getFunName(), Constant.PAGE_SIZE, curPage);

			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void DealerUnitKpShQuery()
	{

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
		
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));		//产地代码
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));	//结算单号
			
			String startDate_1 = CommonUtils.checkNull(request.getParamValue("startDate_1"));		//产地代码
			String endDate_1 = CommonUtils.checkNull(request.getParamValue("endDate_1"));	//结算单号
			
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					
				StringBuffer sql= new StringBuffer();
				sql.append("select t.id , to_char( t.start_date,'yyyy-mm-dd') CHECK_TICKETS_DATE_S,\n" );
				sql.append("to_char(t.end_date , 'yyyy-mm-dd') CHECK_TICKETS_DATE ,\n" );
				sql.append("t.gunit , t.xunit ,t.bro_no ,t.status, a.name ,to_char(t.create_date , 'yyyy-mm-dd') create_date,\n" );
				sql.append("b.name approval_name , to_char(t.approval_date,'yyyy-mm-dd') approval_date,t.approval_remarks, t.invoice_no\n" );
				sql.append(",t.invoice_batch_no\n" );
				sql.append("from  tc_user a, tt_as_wr_bqhs_kp t left join tc_user b on b.user_id = t.approval_by   where  t.status = 94761002  and  a.user_id = t.create_by");

				if(Utility.testString(startDate))
					sql.append(" and   t.start_date >= to_date('"+startDate+"','yyyy-mm-dd')  ");
				if(Utility.testString(endDate))
					sql.append(" and   t.start_date <=  to_date('"+endDate+"','yyyy-mm-dd')  ");
				
				if(Utility.testString(startDate_1))
					sql.append(" and t.end_date >= to_date('"+startDate_1+"','yyyy-mm-dd')  ");
				if(Utility.testString(endDate_1))
					sql.append(" and  t.end_date <=  to_date('"+endDate_1+"','yyyy-mm-dd')  ");
			PageResult<Map<String, Object>> ps =balanceDao.pageQuery(sql.toString(), null, balanceDao.getFunName(), Constant.PAGE_SIZE, curPage);

			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	
	
	public void ClaimsSettlementKpQuery()
	{

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
		
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));		//产地代码
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));	//结算单号
			
			String user_name = CommonUtils.checkNull(request.getParamValue("user_name"));		//产地代码
			String DEALER_NAME = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));	//结算单号
			String DEALER_CODE = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));	//结算单号
			
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					
			StringBuffer sql= new StringBuffer();
			sql.append("\n" );
			sql.append("select F.Dealer_Code ,F.Dealer_Shortname,  decode ( F.tax_disrate , '100%' ,'17%' , '88%' ,'3%' ,'%83%' , '0%'  ) tax_disrate  , to_char( M.End_Date,'yyyy-mm') End_Date ,NVL(M.Amount_Sum,0) Amount_Sum ,P.* from tt_as_wr_claim_balance M , (\n" );
			sql.append("select a.balance_oder, max(b.name) user_name ,\n" );
			sql.append("to_char(max(a.COLLECT_TICKETS_DATE),'yyyy-mm-dd') COLLECT_TICKETS_DATE,\n" );
			sql.append(" max(a.Labour_Receipt) Labour_Receipt,\n" );
			sql.append(" max(a.PART_RECEIPT) PART_RECEIPT ,\n" );
			sql.append("  sum(nvl(a.AMOUNT_OF_MONEY,0)) AMOUNT_OF_MONEY ,\n" );
			sql.append(" sum(nvl(a.TAX_RATE_MONEY,0)) TAX_RATE_MONEY ,\n" );
			sql.append(" sum( nvl(a.AMOUNT_OF_MONEY,0) + nvl(a.TAX_RATE_MONEY,0)  ) KP_MONEY,\n" );
			sql.append(" '' REMARK\n" );
			sql.append(" from tt_as_payment a ,tc_user  b\n" );
			sql.append("where a.COLLECT_TICKETS = b.user_id\n" );
			sql.append("group by  a.balance_oder  ) P , tm_dealer F  where M.Remark = P.balance_oder and F.Dealer_Id = M.Dealer_Id\n" );
			
			if(Utility.testString(user_name))
			sql.append(" and  P.user_name like '%"+user_name+"%'   ");
			if(Utility.testString(DEALER_CODE))
			sql.append(" and  f.Dealer_Code like '%"+DEALER_CODE+"%'   ");
			if(Utility.testString(DEALER_NAME))
			sql.append(" and  f.Dealer_Shortname like '%"+DEALER_NAME+"%'   ");
			if(Utility.testString(startDate))
				sql.append(" and to_date(p.COLLECT_TICKETS_DATE,'yyyy-mm-dd') >= to_date('"+startDate+"','yyyy-mm-dd')   ");
			if(Utility.testString(endDate))		
				sql.append("  and  to_date(p.COLLECT_TICKETS_DATE,'yyyy-mm-dd') <= to_date('"+endDate+"','yyyy-mm-dd')+1   ");
			
			sql.append("order by M.End_Date desc");
			PageResult<Map<String, Object>> ps =balanceDao.pageQuery(sql.toString(), null, balanceDao.getFunName(), Constant.PAGE_SIZE, curPage);

			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void ClaimsSettlementKpPrint()
	{

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
		
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));		//产地代码
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));	//结算单号
			String user_name = CommonUtils.checkNull(request.getParamValue("user_name"));		//产地代码
			String DEALER_NAME = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));	//结算单号
			String DEALER_CODE = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));	//结算单号
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			StringBuffer sql= new StringBuffer();
			sql.append("\n" );
			sql.append("select F.Dealer_Code ,F.Dealer_Shortname,  decode ( F.tax_disrate , '100%' ,'17%' , '88%' ,'3%' ,'%83%' , '0%'  ) tax_disrate  , to_char( M.End_Date,'yyyy-mm') End_Date ,NVL(M.Amount_Sum,0) Amount_Sum ,P.* from tt_as_wr_claim_balance M , (\n" );
			sql.append("select a.balance_oder, max(b.name) user_name ,\n" );
			sql.append("to_char(max(a.COLLECT_TICKETS_DATE),'yyyy-mm-dd') COLLECT_TICKETS_DATE,\n" );
			sql.append(" max(a.Labour_Receipt) Labour_Receipt,\n" );
			sql.append(" max(a.PART_RECEIPT) PART_RECEIPT ,\n" );
			sql.append("  sum(nvl(a.AMOUNT_OF_MONEY,0)) AMOUNT_OF_MONEY ,\n" );
			sql.append(" sum(nvl(a.TAX_RATE_MONEY,0)) TAX_RATE_MONEY ,\n" );
			sql.append(" sum( nvl(a.AMOUNT_OF_MONEY,0) + nvl(a.TAX_RATE_MONEY,0)  ) KP_MONEY,\n" );
			sql.append(" '' REMARK\n" );
			sql.append(" from tt_as_payment a ,tc_user  b\n" );
			sql.append("where a.COLLECT_TICKETS = b.user_id\n" );
			sql.append("group by  a.balance_oder  ) P , tm_dealer F  where M.Remark = P.balance_oder and F.Dealer_Id = M.Dealer_Id\n" );
			
			if(Utility.testString(user_name))
			sql.append(" and  P.user_name like '%"+user_name+"%'   ");
			if(Utility.testString(DEALER_CODE))
			sql.append(" and  f.Dealer_Code like '%"+DEALER_CODE+"%'   ");
			if(Utility.testString(DEALER_NAME))
			sql.append(" and  f.Dealer_Shortname like '%"+DEALER_NAME+"%'   ");
			if(Utility.testString(startDate))
				sql.append(" and to_date(p.COLLECT_TICKETS_DATE,'yyyy-mm-dd') >= to_date('"+startDate+"','yyyy-mm-dd')   ");
			if(Utility.testString(endDate))		
				sql.append("  and  to_date(p.COLLECT_TICKETS_DATE,'yyyy-mm-dd') <= to_date('"+endDate+"','yyyy-mm-dd')+1   ");
			
			sql.append("order by M.End_Date desc");
			List<Map<String, Object>> ps =balanceDao.pageQuery(sql.toString(), null, balanceDao.getFunName());
			act.setOutData("ps", ps);
			act.setForword("/jsp/claim/application/ClaimsSettlementKpPrint.jsp");
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	
	
	public void dealerOnKpQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerId = logonUser.getDealerId();//经销商ID
			String DEALER_NAME = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));		//产地代码
			String DEALER_CODE = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));	//结算单号
			
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			StringBuffer sql= new StringBuffer();
			sql.append("SELECT * from\n" );
			sql.append("(\n" );
			sql.append("SELECT\n" );
			sql.append("sum( case when b.STATUS is null or b.STATUS = 0 then 1\n" );
			sql.append(" else 0 end ) STATUS,\n" );
			sql.append(" max(a.DEALER_NAME) DEALER_NAME,\n" );
			sql.append(" max(a.DEALER_CODE) DEALER_CODE,\n" );
			sql.append(" max(c.PHONE) PHONE,\n" );
			sql.append("(to_char( min(a.START_DATE),'yyyy-mm') || '至' || to_char( max(a.START_DATE),'yyyy-mm')  )  START_DATE\n" );
			sql.append("\n" );
			sql.append("\n" );
			sql.append("  from  TM_DEALER c, TT_AS_WR_CLAIM_BALANCE a\n" );
			sql.append("\n" );
			sql.append("  left join TT_AS_PAYMENT b on a.REMARK = b.BALANCE_ODER\n" );
			sql.append("  where c.DEALER_ID = a.DEALER_ID\n" );
			sql.append("  group by a.DEALER_ID ) M where M.STATUS >=2");
			
			if(Utility.testString(DEALER_NAME))
				sql.append( " and  M.DEALER_NAME like '%"+DEALER_NAME+"%'  " );
			if(Utility.testString(DEALER_CODE))
				sql.append( " and  M.DEALER_CODE like '%"+DEALER_CODE+"%'  " );
			
					
					
			PageResult<Map<String, Object>> ps =balanceDao.pageQuery(sql.toString(), null, balanceDao.getFunName(), Constant.PAGE_SIZE, curPage);

			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void  DealerCountKpQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerId = logonUser.getDealerId();//经销商ID
			String ORG_NAME = CommonUtils.checkNull(request.getParamValue("ORG_NAME"));		//产地代码
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));	//结算单号
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));		    //结算单装备
			String DEALER_NAME = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));	//创建开始时间
			String DEALER_CODE = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));		//创建结束时间
			
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = balanceDao.DealerCountKpQuery(request,dealerId , ORG_NAME,startDate , endDate,DEALER_NAME , DEALER_CODE, curPage,Constant.PAGE_SIZE,logonUser);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 经销商索赔单查询
	 */
	public void dealerKpOrderQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerId = logonUser.getDealerId();//经销商ID
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));		//产地代码
			String balanceNo = CommonUtils.checkNull(request.getParamValue("balanceNo"));	//结算单号
			String status = CommonUtils.checkNull(request.getParamValue("status"));		    //结算单装备
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));	//创建开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));		//创建结束时间
			//当开始时间和结束时间相同时
			if(null!=startDate&&!"".equals(startDate)&&null!=endDate&&!"".equals(endDate)){
				startDate = startDate+" 00:00:00";
				endDate = endDate+" 23:59:59";
			}
			auditBean bean = new auditBean();
			bean.setDealerId(CommonUtils.checkNull(dealerId));
			bean.setYieldly(yieldly);
			bean.setBalanceNo(balanceNo);
			bean.setStatus(status);
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setOemCompanyId(String.valueOf(GetOemcompanyId.getOemCompanyId(logonUser)));
			
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = balanceDao.queryAccAuditList(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);//向前台传的list 名称是固定的不可改必须用 ps			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商结算操作首页 
	 * 2010-10-20 修改
	 * 1、现在二级经销商可以提报结算单（退回到最开始状态）
	 *    流程：二级经销商提报结算单到一级经销商处，然后由一级经销商再提报到车厂
	 * @throws
	 */
	public void addDealerKpInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {		
			RequestWrapper request= act.getRequest();
			String dealerId= request.getParamValue("dealerId");
			String sdate= request.getParamValue("sdate");
			String edate= request.getParamValue("edate");
			String returnId= request.getParamValue("returnId");
			act.setOutData("dealerId",dealerId);
			act.setOutData("sdate",sdate);
			act.setOutData("edate",edate);
			act.setOutData("returnId",returnId);
			act.setForword("/jsp/claim/application/dealerNewKp.jsp");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商上报结算单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void addDealerKpInit01() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {		
			RequestWrapper request= act.getRequest();
			String dealerId= request.getParamValue("dealerId");
			act.setOutData("dealerId",dealerId);
			act.setOutData("yieldly",95411002);
			act.setForword("/jsp/claim/application/dealerNewKp.jsp");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商上报结算单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void getSpecFeeList(){
		try {
			String dealerId = request.getParamValue("dealerId");//经销商ID
			String endBalanceDate = request.getParamValue("endBalanceDate");//数据库结算终止时间
			String conEndDay = request.getParamValue("CON_END_DAY");//现在结算终止时间
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			List<Map<String, String>> spList=balanceDao.getSpecialFeeToBalanceOrder(dealerId, endBalanceDate, conEndDay);
			List<Map<String, String>> claimCount=balanceDao.getArrClaim(Long.valueOf(dealerId), endBalanceDate, conEndDay);
			if(claimCount.size()>0){
				act.setOutData("canDoIt", "YES");
			}else{
				act.setOutData("canDoIt", "NO");
			}
            act.setOutData("spFeeList", spList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "取特殊费用");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 结算开票时算运费把紧急回运的费用加入 zyw 2014-8-25
	 */
	public void gettickets(){
		List  tisList=balanceDao.gettickets(request);
		if(tisList.size()>0){
			act.setOutData("canDoIt", "YES");
		}else{
			act.setOutData("canDoIt", "NO");
		}
        act.setOutData("tisList", tisList);
	}
	/**
	 * 按经销商选择时间和产地统计可以结算金额
	 * 2010-10-12 修改 
	 *     1、一级经销商可以结算对应下级经销商的索赔单
	 *     2、非一级经销商不能进行结算  
	 * 2010-10-20 修改
	 *     1、非一级经销商可以结算
	 *     2、统计需要结算索赔单按索赔单审核通过时间处理
	 *        (原：索赔单上报时间)      
	 */
	public void dealerKpStatisQuery(){
		try {
			String dealerId = request.getParamValue("dealerId");//经销商ID
			String endBalanceDate = request.getParamValue("endBalanceDate");//开始时间
			String conEndDay = request.getParamValue("CON_END_DAY");//结束时间
			DealerBalanceBean condition = new DealerBalanceBean();
			condition.setEndBalanceDate(endBalanceDate);
			condition.setConEndDay(conEndDay);
			condition.setCurPage(getCurrPage());
			condition.setPageSize(Constant.PAGE_SIZE);
			DealerKpDao KpDao = DealerKpDao.getInstance();
			PageResult<Map<String,Object>> ps = KpDao.dealerBalanceStatisQuery(condition,dealerId);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商上报结算统计查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/*
	 * 经销商查看开票结果
	 */
	
	public void dealerKpQuerydlInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String logonUserDealerCode = CommonUtils.checkNull(logonUser.getDealerCode());
		try {
			CommonUtilDao commonUtilDao = new CommonUtilDao();
			List<TmBusinessAreaPO> list = commonUtilDao.getYieldly();
			act.setOutData("Area", list);
			act.setOutData("logonUserDealerCode", logonUserDealerCode);
			System.err.println("******************"+logonUserDealerCode);
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List listCode = daoKP.select(code) ;
			if(listCode.size()>0){
				code = (TcCodePO)listCode.get(0);
			
				act.setOutData("code", code.getCodeId()) ;
				//轿车添加配件是不是监控判断
				if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
					act.setForword("/jsp/claim/authorization/dealerKpQuery.jsp");
				 }
				else{
					Date time = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String a = sdf.format(time);
					String b = a.substring(8, 10);
					act.setOutData("day", b);
					act.setForword("/jsp/claim/authorization/newDealerKpdlQuery.jsp");
				}
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void getdealerId()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			  RequestWrapper  request = act.getRequest();
			  String dealerCode = request.getParamValue("dealerCode");
			  TmDealerPO dealerPO = new TmDealerPO();
			  dealerPO.setDealerCode(dealerCode);
			  List<TmDealerPO> list= daoKP.select(dealerPO);
			  TmDealerInvoiceIntercalatePO intercalatePO = new TmDealerInvoiceIntercalatePO();
			  intercalatePO.setDealerCode(dealerCode);
			   List<TmDealerInvoiceIntercalatePO> list1= daoKP.select(intercalatePO);
			  if(list1.size() > 0)
			  {
				  act.setOutData("msg", "false2");
			  }
			  else if(list.size() == 0)
			  {
				  act.setOutData("msg", "false1");
			  }else
			  {
				  act.setOutData("dealerId", list.get(0).getDealerId());
			  }
			
			  
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"查看经销商出错请与管理员联系");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dealerKpFineInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			  act.setForword("/jsp/claim/authorization/dealerFineQuery.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"正负激励单独开票失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	
	public void KpFineInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			   act.setOutData("type",1);
			  act.setForword("/jsp/claim/authorization/dealerFineQuery.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"正负激励单独开票失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void KpFineInitzhuren()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			  act.setForword("/jsp/claim/authorization/dealerFineQueryZhuRen.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"正负激励单独开票失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void dealerKpQueryInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		String logonUserDealerCode = "";
		if(null != logonUser){
			logonUserDealerCode = CommonUtils.checkNull(logonUser.getDealerCode());
		}
		
		try {
			CommonUtilDao commonUtilDao = new CommonUtilDao();
			List<TmBusinessAreaPO> list = commonUtilDao.getYieldly();
			act.setOutData("Area", list);
			act.setOutData("logonUserDealerCode", logonUserDealerCode);
			
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List listCode = daoKP.select(code) ;
			if(listCode.size()>0){
				code = (TcCodePO)listCode.get(0);
			
				act.setOutData("code", code.getCodeId()) ;
				//轿车添加配件是不是监控判断
				if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
					act.setForword("/jsp/claim/authorization/dealerKpQuery.jsp");
				 }
				else{
					Date time = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String a = sdf.format(time);
					String b = a.substring(8, 10);
					act.setOutData("day", b);
					act.setForword("/jsp/claim/authorization/newDealerKpQuery.jsp");
				}
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void collectTickets(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
				act.setOutData("status",1);
			    act.setOutData("id","collect");
				act.setForword("/jsp/claim/authorization/newDealerTickets.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void checkTickets(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
				act.setOutData("status",2);
				act.setOutData("id","check");
				act.setForword("/jsp/claim/authorization/newDealerTickets.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void transferTickets(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("status",3);
			act.setOutData("id","transfer");
			act.setForword("/jsp/claim/authorization/newDealerTickets.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void dealerKpQueryInit01(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			CommonUtilDao commonUtilDao = new CommonUtilDao();
			List<TmBusinessAreaPO> list = commonUtilDao.getYieldly();
			act.setOutData("Area", list);
			
			StringBuffer judesql= new StringBuffer();
			judesql.append("\n" );
			judesql.append("SELECT E.DEALER_ID ,E.DEALER_NAME from TM_DEALER E where E.DEALER_ID in(");
			judesql.append("SELECT C.DEALER_ID\n" );
			judesql.append("  FROM TM_DEALER_ORG_RELATION C\n" );
			judesql.append(" where C.ORG_ID in\n" );
			judesql.append("       (SELECT B.ORG_ID\n" );
			judesql.append("          from tc_pose B\n" );
			judesql.append("         where B.POSE_ID in (SELECT A.POSE_ID\n" );
			judesql.append("                               from Tr_User_Pose A\n" );
			judesql.append("                              where A.USER_ID ="+logonUser.getUserId()+")\n" );
			judesql.append("           and B.POSE_BUS_TYPE =" + Constant.POSE_BUS_TYPE_WR+")\n" );
			judesql.append("   AND C.DEALER_ID not in (SELECT D.DEALER_ID from tr_pose_dealer D)\n" );
			judesql.append("UNION\n" );
			judesql.append("SELECT D.DEALER_ID\n" );
			judesql.append("  from tr_pose_dealer D  ,tc_pose F \n" );
			judesql.append(" where  F.POSE_ID = D.POSE_ID AND F.POSE_BUS_TYPE ="+Constant.POSE_BUS_TYPE_WR+"   AND  D.POSE_ID in\n" );
			judesql.append("       (SELECT E.POSE_ID from Tr_User_Pose E where E.USER_ID = "+logonUser.getUserId()+"))");
			DealerKpDao KpDao = DealerKpDao.getInstance();
			List<TmDealerPO> listD= KpDao.select(TmDealerPO.class, judesql.toString(), null);
			act.setOutData("listD", listD);
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List listCode = daoKP.select(code) ;
			if(listCode.size()>0){
				code = (TcCodePO)listCode.get(0);
			
				act.setOutData("code", code.getCodeId()) ;
				//轿车添加配件是不是监控判断
				if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
					act.setForword("/jsp/claim/authorization/dealerKpQuery.jsp");
				 }
				else{
					Date time = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String a = sdf.format(time);
					String b = a.substring(8, 10);
					act.setOutData("day", b);
					act.setForword("/jsp/claim/authorization/newDealerKpQuery01.jsp");
				}
			}
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 经销商确认
	 */
	public void checkBalanceFor(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			TtAsWrClaimBalancePO po = new TtAsWrClaimBalancePO();
			TtAsWrClaimBalancePO tp = new TtAsWrClaimBalancePO();
			po.setId(Long.parseLong(id));
			tp.setStatus(Integer.parseInt(Constant.ACC_STATUS_05));
			tp.setUpdateBy(logonUser.getUserId());
			tp.setUpdateDate(new Date());
			//BalanceMainDao dao = new BalanceMainDao();
			factory.update(po, tp);
			
			//记录日志表 -- 添加一个经销商确认时间
			BalanceStatusRecord.recordStatus(Long.parseLong(id), logonUser.getUserId(), logonUser.getName(), logonUser.getOrgId(), BalanceStatusRecord.STATUS_08);
			act.setRedirect("/claim/application/DealerNewKp/dealerKpQueryInit.do");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"经销商确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 车厂查询所有经销商的开票 zyw2015-2-4 重构
	 */
	public void delaerKpMainQuery(){
		try {
			DealerKpDao dao = new DealerKpDao();
			PageResult<Map<String, Object>> ps = dao.queryDealerBalanceList(request,loginUser,getCurrPage(),Constant.PAGE_SIZE);
			act.setOutData("ps", ps);     
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	public void delaerKpMainQueryExport(){
		try {
			DealerKpDao dao = new DealerKpDao();
			PageResult<Map<String, Object>> list = dao.queryDealerBalanceList(request,loginUser,getCurrPage(),Constant.PAGE_SIZE_MAX);
			dao.expotdelaerKpMainQueryData(act, list);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void newDealerTickets(){
		try {
			
			act.setOutData("id", getParam("id"));
			DealerKpDao dao = new DealerKpDao();
			PageResult<Map<String, Object>> ps = dao.queryDealerTickets(request,getCurrPage(),Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void delaerKpMaindlQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));	//创建开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));		//创建结束时间
			String BalanceNo = CommonUtils.checkNull(request.getParamValue("BalanceNo"));	//结算单号

			auditBean bean = new auditBean();
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setBalanceNo(BalanceNo);
			bean.setDealerId(logonUser.getDealerId());
			
			DealerKpDao dao = new DealerKpDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryDealerBalanceList02(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void delaerKpMainQuery01(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//CommonUtils.checkNull() 校验是否为空
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));	//创建开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));		//创建结束时间
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			
			//当开始时间和结束时间相同时
			if(null!=startDate&&!"".equals(startDate)&&null!=endDate&&!"".equals(endDate)){
				startDate = startDate+" 00:00:00";
				endDate = endDate+" 23:59:59";
			}
			auditBean bean = new auditBean();
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setDealerCode(dealerId);
			
			DealerKpDao dao = new DealerKpDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryDealerBalanceList01(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 *  开票
	 */
	/*@SuppressWarnings("unchecked")
	public void balanceView(){
		try {
			DealerKpDao balanceDao = DealerKpDao.getInstance();//DAO单例
			DealerKpDao dao = DealerKpDao.getInstance();//DAO单例
			String endBalanceDate = getParam("endBalanceDate");//结算起始时间
			String conEndDay = getParam("CON_END_DAY");//结算终止时间
			String version = getParam("version");//
			String dealerId = getParam("dealerId");//经销商ID
			String yieldly = getParam("yieldly");
			
			act.setOutData("endBalanceDate", endBalanceDate);
			act.setOutData("conEndDay", conEndDay);
			act.setOutData("version", version);
			act.setOutData("dealerId", dealerId);
			act.setOutData("yieldly", yieldly);
			Long companyId = GetOemcompanyId.getOemCompanyId(loginUser);//用户所属公司
			
			String startTime = "";
			String endTime = "";
			if(Utility.testString(endBalanceDate)){
				startTime = endBalanceDate + " 00:00:00";
			}
			if(Utility.testString(conEndDay)){
				endTime = conEndDay + " 23:59:59";
			}
			//查询经销商信息
			long dealerid = Long.parseLong(dealerId);
			TmDealerPO tm = new TmDealerPO();
			tm.setDealerId(dealerid);
			List<TmDealerPO> dealerList=balanceDao.select(tm);
	    	if(DaoFactory.checkListNull(dealerList)){
	    		tm = dealerList.get(0);
	    	}
			//根据经销商信息查询开票单位
	    	TmDealerPO invoiceMaker = new TmDealerPO();
			invoiceMaker = balanceDao.queryInvoiceMaker(dealerid); //开票经销商
				
			
			
			TtAsWrClaimBalanceTmpPO balanceTempPo = new TtAsWrClaimBalanceTmpPO();
			
			String jsNO = "";//结算单号
			double datum_sum = 0d;    
			double labour_sum = 0d; 
			DecimalFormat df=new DecimalFormat(".##");
			//取值地区产地信息 yieldly
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			List<TmBusinessAreaPO> areaList= balanceDao.select(areaPO);
			
			for(TmBusinessAreaPO area : areaList){
				Long areaId = area.getAreaId();
				double fine_sum  = 0;
				double fine_psum  = 0;
				List<Map<String,Object>> specialList = null; //现在特殊费用只有一个材料费 zyw 2015-4-16
				if("95411001".equals(yieldly)){//产地相同
					//查询特殊费用总经费材料费
					InvoiceService invoiceService = new InvoiceServiceImpl();
					specialList= invoiceService.findSpecialAmount(dealerId,startTime, endTime,areaId);
					double[] fine_sums = dao.getwrfineOrder(dealerid, startTime, endTime, areaId);
					fine_sum = fine_sums[0];//查询正负激励劳务费
					fine_psum = fine_sums[1];//查询正负激励材料费
			    }
				Map<String,Object> applMap = null;//查询除服务活动外的总经费
				Map<String,Object> fwjMap  = null; //查询服务活动的总经费
				Map<String,Object> dintMap = null; //查询索赔单抵扣总金额
				Map<String,Object> chargeMap =null;//查询行政扣款总金额
				int i = 0;
				if(i == 0){
					 if(yieldly.equals("95411001")){
						List<Map<String,Object>> dkList = dao.dealerBalanOrderJJD(dealerId , startTime, endTime,areaId,yieldly);
					 	if(DaoFactory.checkListNull(dkList)){
					 	    dintMap=new HashMap<String, Object>(); 
					 	    dintMap =dkList.get(0);
					 	}
					 }
					 List<Map<String, Object>> xzList = dao.get_administrative_charge(dealerId);
					 if(DaoFactory.checkListNull(xzList)){
						 chargeMap=new HashMap<String, Object>();
						 chargeMap = xzList.get(0);
					 }
				}
				double flcount=0d;
				double qhcount=0d;
				List<Map<String, Object>> qhprice = dao.qhprice(dealerId,startTime, endTime);//查询出索赔表的切换件费用
				List<Map<String, Object>> accessories_price = dao.accessories_price(dealerId,startTime, endTime);//查询出索赔表的辅料费
				if(DaoFactory.checkListNull(accessories_price)){
					Map<String, Object> map =accessories_price.get(0);
					BigDecimal b= (BigDecimal) map.get("ACCESSORIES_PRICE");
					flcount=b.doubleValue();
				}
				if(DaoFactory.checkListNull(qhprice)){
					Map<String, Object> map =qhprice.get(0);
					BigDecimal b= (BigDecimal) map.get("BALANCE_LABOUR_AMOUNT");
					qhcount=b.doubleValue();
				}
				//===========================================查询出反索赔表的信息
				List<Map<String, Object>> selectCliam = dao.selectCliam(dealerId,startTime, endTime);
				double balanceAmountCount=0d;
				double partAmountCount=0d;
				double labourAmountCount=0d;
				if(DaoFactory.checkListNull(selectCliam)){
					Map<String, Object> map = selectCliam.get(0);
					BigDecimal a= (BigDecimal) map.get("BALANCE_AMOUNT");
					BigDecimal b= (BigDecimal) map.get("PART_AMOUNT");
					BigDecimal c= (BigDecimal) map.get("LABOUR_AMOUNT");
					balanceAmountCount=a.doubleValue();//反索赔结算金额(反索赔为车厂找服务站赔钱)这里不做处理
					partAmountCount=-1d*b.doubleValue();//反索赔材料费
					labourAmountCount=-1d*c.doubleValue();//反索赔工时费
				}
			   //查询除服务活动外的总经费
			   List<Map<String,Object>> dealerBalanOrder = dao.dealerBalanOrder(dealerId,startTime,endTime,areaId,yieldly);
			   if(DaoFactory.checkListNull(dealerBalanOrder)){
				   applMap = new HashMap<String, Object>();
				   applMap = dealerBalanOrder.get(0);
			   }
			   //查询服务活动的总经费
			   List<Map<String,Object>> dealerBalanOrderFWJ = dao.dealerBalanOrderFWJ(dealerId,startTime, endTime,areaId,yieldly);
			   if(DaoFactory.checkListNull(dealerBalanOrderFWJ)){
				   fwjMap=new HashMap<String, Object>();
				   fwjMap =dealerBalanOrderFWJ.get(0);
			   }
			   	TtAsWrClaimBalanceTmpPO balancePO = new TtAsWrClaimBalanceTmpPO();
			   	
				String balanceNO = SequenceManager.getSequence("BO");//结算单号
				String applyName = loginUser.getName();//申请人姓名
				String applyId = String.valueOf(loginUser.getUserId());//申请人ID
				String dealerCode = tm.getDealerCode();//经销商代码
				String dealerName = tm.getDealerShortname();//经销商名称
				
				String invoiceMakerStr = invoiceMaker.getDealerName()==null?"":invoiceMaker.getDealerName();//开票单位
				//插入技术室除服务活动外的总经费
				String labourAmount = String.valueOf(CommonUtils.getDataFromMap(applMap,"LABOUR_AMOUNT"));//工时金额
				String partAmount = String.valueOf(CommonUtils.getDataFromMap(applMap,"PART_AMOUNT"));//配件金额
				String otherAmount = String.valueOf(CommonUtils.getDataFromMap(applMap,"NETITEM_AMOUNT"));//其他费用金额
				String freeAmount = String.valueOf(CommonUtils.getDataFromMap(applMap,"FREE_M_PRICE"));//免费保养金额
				String labour_price = String.valueOf(CommonUtils.getDataFromMap(applMap,"LABOUR_PRICE"));
			    String part_price  = String.valueOf(CommonUtils.getDataFromMap(applMap,"PART_PRICE"));
				//插入技术室服务活动的总经费
				String serviceLabourAmount = String.valueOf(CommonUtils.getDataFromMap(fwjMap,"LABOUR_AMOUNT"));//服务活动工时金额
				String servicePartAmount = String.valueOf(CommonUtils.getDataFromMap(fwjMap,"PART_AMOUNT"));//服务活动配件金额
				String serviceOtherAmount = String.valueOf(CommonUtils.getDataFromMap(fwjMap,"NETITEM_AMOUNT"));//服务活动其他金额
				String serviceTotalAmount = String.valueOf(CommonUtils.getDataFromMap(fwjMap,"BALANCE_AMOUNT"));//服务活动总金额
				
				String olddeduct = "0";//旧件抵扣
				if(i == 0 ){
					if(dintMap != null && dintMap.get("DISCOUNT") != null){
						olddeduct = String.valueOf(CommonUtils.getDataFromMap(dintMap,"DISCOUNT"));
					}
				}
				double special_labour_sum =0.0d;
				double special_datum_sum =0.0d; 
				//========================循环加入特殊费用
				if(DaoFactory.checkListNull(specialList)){
					for (Map<String, Object> special : specialList) {
						special_datum_sum+=CommonUtils.parseDouble(checkNull(String.valueOf(CommonUtils.getDataFromMap(special,"APPROVAL_AMOUNT")),"0"));
					}
				}
				//初次结算总费用计算= 索赔单所有除服务活动总费用+索赔单所有服务活动总费用-抵扣费用
				Double olddekouTemp = CommonUtils.parseDouble(checkNull(olddeduct,"0"));
				//除服务活动的索赔单费用总和
				Double appBalanceAmount = CommonUtils.parseDouble(checkNull(String.valueOf(CommonUtils.getDataFromMap(applMap,"BALANCE_AMOUNT")),"0"));
				//服务活动总经费
				Double fwhdBalanceAmount = CommonUtils.parseDouble(checkNull(String.valueOf(CommonUtils.getDataFromMap(fwjMap,"BALANCE_AMOUNT")),"0"));
				//除服务活动的索赔单费用总和+索赔单服务活动总经费+正负激励工时+正负激励材料（正负激励扣款跟奖励在前边有判断）+特殊费用-抵扣费用
				double balanceAmount=Double.parseDouble(df.format(appBalanceAmount+fwhdBalanceAmount+fine_sum+fine_psum+special_datum_sum+special_labour_sum-olddekouTemp));
				//反索赔结算金额计算进入结算金额（这里的反索赔结算金额需扣除）
				balanceAmount =  Arith.sub(  balanceAmount, balanceAmountCount);
				//行政扣款
				double datum_sum_temp = CommonUtils.parseDouble(checkNull(String.valueOf(CommonUtils.getDataFromMap(chargeMap,"DATUM_SUM")),"0"))*-1d;
				double labour_sum_temp = CommonUtils.parseDouble(checkNull(String.valueOf(CommonUtils.getDataFromMap(chargeMap,"LABOUR_SUM")),"0"))*-1d;
				if(chargeMap != null && chargeMap.get("ID") != null ){
					//二次结算总费用计算= 初次结算总费用+行政扣款材料费用+行政扣款工时费用
					balanceAmount = Double.parseDouble(df.format(balanceAmount+datum_sum_temp+labour_sum_temp));
					long chargeId = Long.parseLong(chargeMap.get("ID").toString());
					balancePO.setChargeId(chargeId);
					act.setOutData("datum", datum_sum_temp);
					act.setOutData("labour", labour_sum_temp);
				}else{
					act.setOutData("datum", 0);
					act.setOutData("labour", 0);
				}
				//索赔单除服务活动材料费
				Double appclAmount = CommonUtils.parseDouble(checkNull(String.valueOf(CommonUtils.getDataFromMap(applMap,"PART_AMOUNT")),"0"));
				//索赔单服务活动材料费
				Double fwhdCLAmount = CommonUtils.parseDouble(checkNull(String.valueOf(CommonUtils.getDataFromMap(fwjMap,"PART_AMOUNT")),"0"));
				//所有材料费用的算法=索赔单除服务活动材料费+索赔单服务活动材料费+正负激励材料费+特殊费用审核过的费用-抵扣费用+反索赔材料费用
				datum_sum  = Double.parseDouble(df.format(datum_sum+appclAmount+fwhdCLAmount 
						+fine_psum+special_datum_sum-olddekouTemp+ partAmountCount));
				//所有工时费用（反索赔工时费在前边儿已经做负数处理）
				labour_sum =  Double.parseDouble(df.format(labour_sum + appBalanceAmount-appclAmount
						+fwhdBalanceAmount-fwhdCLAmount+fine_sum+special_labour_sum+labour_sum_temp+labourAmountCount));
				
				
				balancePO.setBalanceNo(CommonUtils.checkNull(balanceNO));
				balancePO.setApplyPersonId(applyId==null?-1:CommonUtils.parseLong(applyId));
				balancePO.setApplyPersonName(CommonUtils.checkNull(applyName));
				balancePO.setDealerCode(CommonUtils.checkNull(dealerCode));
				balancePO.setDealerName(CommonUtils.checkNull(dealerName));
				balancePO.setInvoiceMaker(CommonUtils.checkNull(invoiceMakerStr));
				balancePO.setFreeLabourAmount(Double.parseDouble(labour_price));
				balancePO.setFreePartAmount(Double.parseDouble(part_price));
				balancePO.setLabourAmount(Arith.add(CommonUtils.parseDouble(checkNull(labourAmount,"0")), labourAmountCount));//工时除去反索赔工时
				balancePO.setPartAmount(Arith.add(CommonUtils.parseDouble(checkNull(partAmount,"0")), partAmountCount));//材料除去反索赔材料
				balancePO.setOtherAmount(CommonUtils.parseDouble(checkNull(otherAmount,"0")));
				balancePO.setFreeAmount(CommonUtils.parseDouble(checkNull(freeAmount,"0")));

				balancePO.setServiceLabourAmount(CommonUtils.parseDouble(checkNull(serviceLabourAmount,"0")));
				balancePO.setServicePartAmount(CommonUtils.parseDouble(checkNull(servicePartAmount,"0")));
				balancePO.setServiceOtherAmount(CommonUtils.parseDouble(checkNull(serviceOtherAmount,"0")));
				balancePO.setServiceTotalAmount(CommonUtils.parseDouble(checkNull(serviceTotalAmount,"0")));
				balancePO.setPlusMinusDatumSum(fine_psum)  ;
				balancePO.setPlusMinusLabourSum(fine_sum);
				balancePO.setSpecialDatumSum(special_datum_sum);  
				balancePO.setSpecialLabourSum(special_labour_sum);
				balancePO.setOldDeduct(olddekouTemp);
				balancePO.setAppendAmount(balanceAmountCount);//反索赔费用结算
				balancePO.setMarketActivityAmount(qhcount);
				//==========zyw 把二次入库补偿服务站的钱加到结算总金额里 2014-9-10
				Double compensationDealerMoney=0d;
				List<Map<String, Object>> listMap=dao.findcompensationBydealer(dealerId,"");
				if(DaoFactory.checkListNull(listMap)){
					Map<String, Object> map = listMap.get(0);
					BigDecimal  bigdecimal= (BigDecimal) map.get("AMOUNT");
					compensationDealerMoney=bigdecimal.doubleValue();
				}
				balancePO.setCompensationDealerMoney(compensationDealerMoney);
				balanceAmount =  Arith.add(balanceAmount, compensationDealerMoney);
				//=============================================
				System.out.println(balanceAmount);
				balancePO.setAmountSum(balanceAmount);
				Integer dealerLevel = Constant.DEALER_LEVEL_02;
				if(tm.getDealerLevel()!=null){
					dealerLevel = tm.getDealerLevel();
				}
				//根据经销商信息查询开票单位
				TmDealerPO invoiceMaker2 = balanceDao.queryInvoiceMaker(dealerid); //开票经销商
				TmDealerPO balanceMaker2 = balanceDao.queryBalanceMaker(dealerid); //结算经销商
				Long balanceId = DaoFactory.getPkId();//结算单ID
				//查看二级商总费用
				List<Map<String,Object>> dealerBalanOrderSend = dao.dealerBalanOrderSend(dealerId,startTime,endTime,areaId,yieldly);
				if(DaoFactory.checkListNull(dealerBalanOrderSend)){
				   for(Map<String,Object> map : dealerBalanOrderSend){
					   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					   String second_dealer_id = String.valueOf(CommonUtils.getDataFromMap(map,"SECOND_DEALER_ID"));
					   String second_dealer_code = String.valueOf(CommonUtils.getDataFromMap(map,"SECOND_DEALER_CODE"));
					   String balance_amount = String.valueOf(CommonUtils.getDataFromMap(map,"BALANCE_AMOUNT"));
					   double service_charge  =  CommonUtils.parseDouble(checkNull(String.valueOf(CommonUtils.getDataFromMap(map,"LABOUR_AMOUNT")),"0"));
					   double material_charge =  CommonUtils.parseDouble(checkNull(String.valueOf(CommonUtils.getDataFromMap(map,"PART_AMOUNT")),"0"));
					   TtAsWrClaimSendTemPO sendTemPO = new TtAsWrClaimSendTemPO();
					   sendTemPO.setBalanceId(balanceId);
					   sendTemPO.setAmountSum(Double.parseDouble(balance_amount));
					   sendTemPO.setEnddate(formatter.parse(endTime));
					   sendTemPO.setSecondDealerCode(second_dealer_code);
					   sendTemPO.setStartdate(formatter.parse(startTime));
					   sendTemPO.setSecondDealerId(Long.parseLong(second_dealer_id));
					   sendTemPO.setYieldly(areaId);
					   sendTemPO.setBalanceYieldly(Integer.parseInt(yieldly));
					   sendTemPO.setDealerId(dealerid);
					   sendTemPO.setServiceCharge(service_charge);
					   sendTemPO.setMaterialCharge(material_charge);
					   dao.insert(sendTemPO);
				   }
			   }
				this.saveOrUpateSendTem(dao, dealerId, yieldly, startTime, endTime,dealerid, areaId, i, balanceId);
				balancePO.setId(balanceId);
				balancePO.setBalanceYieldly(Integer.parseInt(yieldly));
				balancePO.setOemCompanyId(companyId);
				balancePO.setUpdateBy(loginUser.getUserId());
				balancePO.setUpdateDate(new Date());
				balancePO.setCreateBy(loginUser.getUserId());
				balancePO.setCreateDate(new Date());
				balancePO.setDealerLevel(dealerLevel);
				balancePO.setTopDealerId(balanceMaker2.getDealerId());  //结算经销商
				balancePO.setKpDealerId(invoiceMaker2.getDealerId());   //开票经销商
				balancePO.setYieldly(areaId);
				balancePO.setDealerId(CommonUtils.parseLong(dealerId));
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				balancePO.setStartDate(formatter.parse(startTime));
				balancePO.setEndDate(formatter.parse(endTime));
				balancePO.setCreateDate(new Date());
				balancePO.setAccessoriesPrice(flcount);
				dao.getvModel(CommonUtils.parseLong(dealerId), startTime, endTime, balancePO);
				//========================这一块是把结算金额的运费加入到结算里 zyw 2014-8-25
				if(i == 0){
				 String[] auth_prices= request.getParamValues("AUTH_PRICE");
				 String returned_id = DaoFactory.getParam(request, "returned_id");
				 String[] returnedids = StringUtils.split(returned_id, ";");
				 if(returnedids!=null && auth_prices!=null){
					 StringBuffer sb =new StringBuffer();
					 for(String returnedid : returnedids){
						sb.append(returnedid+",");
					 }
					 act.setOutData("ReturnedID", sb.toString());
					 double auth_priceTemp=0.0d;
					 for (String auth_price : auth_prices){
					    if(Utility.testString(auth_price)){
					    	auth_priceTemp=Arith.add(auth_priceTemp, Double.parseDouble(auth_price));
					    	labour_sum = labour_sum+Double.parseDouble(auth_price);
					    }			
					}
					 System.out.println(balancePO.getAmountSum() + auth_priceTemp);
					balancePO.setAmountSum(balancePO.getAmountSum() + auth_priceTemp);//结算金额的运费加入到结算里

					balancePO.setReturnAmount(auth_priceTemp);
					
				 }
				i++;
				balanceTempPo = balancePO;
				TcUserPO userPO = new TcUserPO();
				userPO.setUser_id(loginUser.getUserId());
				BalanceMainDao balancemaindao = new BalanceMainDao();
				jsNO= balancemaindao.number_ap(loginUser.getActn(),endTime.split("-")[0] , endTime.split("-")[1], dealerCode);
				}
				//========================
				balancePO.setRemark(jsNO);
				System.out.println(balancePO.getAmountSum());
				dao.insert(balancePO);
				
				TmBusinessAreaPO tmareaPO =new TmBusinessAreaPO();
				tmareaPO.setAreaId(balancePO.getYieldly());
				tmareaPO =(TmBusinessAreaPO) dao.select(tmareaPO).get(0);
				act.setOutData("tmareaPO"+i , tmareaPO);
			}
			if(labour_sum+datum_sum < 0){
				act.setOutData("labour_sum", labour_sum*-1);
				act.setOutData("datum_sum", datum_sum*-1);
				act.setOutData("AmountSum",Double.parseDouble(df.format( balanceTempPo.getAmountSum()  +( labour_sum*-1)+ (datum_sum*-1) )));
			}else{
				act.setOutData("labour_sum", 0);
				act.setOutData("datum_sum", 0);
				act.setOutData("AmountSum",Double.parseDouble(df.format( balanceTempPo.getAmountSum()  )));
			}
			System.out.println(balanceTempPo.getAmountSum());
			act.setOutData("balancePO1", balanceTempPo);
			act.setOutData("jsNO", jsNO);
			act.setForword("/jsp/claim/authorization/balanceKpInfo.jsp");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商上报结算");
			logger.error(loginUser, e1);
			act.setException(e1);
		}	
	}*/
	/**
	 *  开票
	 */
	@SuppressWarnings("unchecked")
	public void balanceView(){
		try {
			String dealerId = request.getParamValue("dealerId");//经销商ID
			String endBalanceDate = request.getParamValue("endBalanceDate");//开始时间
			String conEndDay = request.getParamValue("CON_END_DAY");//结束时间
			DealerBalanceBean condition = new DealerBalanceBean();
			condition.setEndBalanceDate(endBalanceDate);
			condition.setConEndDay(conEndDay);
			condition.setCurPage(getCurrPage());
			condition.setPageSize(Constant.PAGE_SIZE);
			DealerKpDao KpDao = DealerKpDao.getInstance();
			PageResult<Map<String,Object>> ps = KpDao.dealerBalanceStatisQuery(condition,dealerId);
			act.setOutData("ps", ps.getRecords().get(0));
			act.setOutData("endBalanceDate", endBalanceDate);
			act.setOutData("conEndDay", conEndDay);
			act.setOutData("dealerId", dealerId);
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
			String date = format1.format(new Date());
			act.setOutData("date", date);
			
			//查询经销商信息
			long dealerid = Long.parseLong(dealerId);
			TmDealerPO tm = new TmDealerPO();
			tm.setDealerId(dealerid);
			List<TmDealerPO> dealerList=balanceDao.select(tm);
	    	if(DaoFactory.checkListNull(dealerList)){
	    		tm = dealerList.get(0);
	    	}
	    	act.setOutData("tm", tm);
	    	//制单人
	    	TcUserPO user=new TcUserPO();
	    	user.setUser_id(loginUser.getUserId());
	    	TcUserPO seuser=new TcUserPO();
	    	List<TcUserPO> userList=balanceDao.select(seuser);
	    	act.setOutData("name", userList.get(0).getName().toString());	    	
			act.setForword("/jsp/claim/authorization/balanceKpInfo.jsp");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商上报结算");
			logger.error(loginUser, e1);
			act.setException(e1);
		}	
	}
	private void saveOrUpateSendTem(DealerKpDao dao, String dealerId,
			String yieldly, String startTime, String endTime, long dealerid,
			Long areaId, int i, Long balanceId) throws ParseException {
		if(yieldly.equals("95411001")){
		   if(i == 0){
			   StringBuffer sql= new StringBuffer();
			   sql.append("SELECT t.DEALER_ID ,t.DEALER_CODE\n" );
			   sql.append("  from TM_DEALER t\n" );
			   sql.append(" where t.DEALER_ID != "+dealerId+" and  t.DEALER_ID in\n" );
			   sql.append("       (select d.dealer_id\n" );
			   sql.append("          from tm_dealer d\n" );
			   sql.append("         start with d.dealer_id = "+dealerId+"\n" );
			   sql.append("        connect by PRIOR d.dealer_id = d.parent_dealer_d)");
			   List<TmDealerPO> tdList= dao.select(TmDealerPO.class,sql.toString(),null);
			   if(DaoFactory.checkListNull(tdList)){
				   for(TmDealerPO  tmdealer: tdList ){
					   double labour_sum_01 = 0d;
					   double datum_sum_01 = 0d;
					   List<Map<String,Object>> specialFeeToBalanceOrder = dao.getSpecialFeeToBalanceOrder(tmdealer.getDealerId() , startTime, endTime,areaId);
					   if(DaoFactory.checkListNull(specialFeeToBalanceOrder) ){
						   Map<String,Object> sfeep = specialFeeToBalanceOrder.get(0);
						   labour_sum_01 = labour_sum_01 +Double.parseDouble(sfeep.get("DECLARE_SUM1").toString());
					   } 
					   List<Map<String,Object>> specialFeeToBalancepOrder = dao.getSpecialFeeToBalancepOrder(tmdealer.getDealerId() , startTime, endTime,areaId);
					   if(DaoFactory.checkListNull(specialFeeToBalancepOrder)){
						  Map<String,Object> sfeep = specialFeeToBalancepOrder.get(0);
						  datum_sum_01 = datum_sum_01 +Double.parseDouble(sfeep.get("DECLARE_SUM1").toString());
					   } 
					  double[] wfine = dao.getwrfineOrder(tmdealer.getDealerId(), startTime, endTime, areaId);
					  labour_sum_01 = Arith.add(labour_sum_01, wfine[0]);
					  datum_sum_01 = Arith.add(datum_sum_01, wfine[1]);
					  if(labour_sum_01 > 0d || datum_sum_01 > 0d){
						  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						  TtAsWrClaimSendTemPO sendTemPO01 = new TtAsWrClaimSendTemPO();
						  sendTemPO01.setBalanceId(balanceId);
						  sendTemPO01.setSecondDealerId(tmdealer.getDealerId());
						  sendTemPO01.setYieldly(areaId);
						  List<TtAsWrClaimSendTemPO> sendTemPOList=dao.select(sendTemPO01);
						  if(DaoFactory.checkListNull(sendTemPOList)){
							  sendTemPO01 = sendTemPOList.get(0);
							  TtAsWrClaimSendTemPO sendTemPO02 = new TtAsWrClaimSendTemPO();
							  sendTemPO02.setBalanceId(balanceId);
							  sendTemPO02.setYieldly(sendTemPO01.getYieldly());
							  sendTemPO02.setSecondDealerId(tmdealer.getDealerId());
							  
							  TtAsWrClaimSendTemPO sendTemPO03 = new TtAsWrClaimSendTemPO();
							  sendTemPO03.setServiceCharge(Arith.add(sendTemPO01.getServiceCharge(), labour_sum_01));
							  sendTemPO03.setMaterialCharge(Arith.add(sendTemPO01.getMaterialCharge(),datum_sum_01) );
							  sendTemPO03.setAmountSum(Arith.add(Arith.add(sendTemPO01.getServiceCharge(), labour_sum_01),Arith.add(sendTemPO01.getMaterialCharge(),datum_sum_01)));
							  dao.update(sendTemPO02, sendTemPO03);
						  }else{
							   String second_dealer_id = "" +tmdealer.getDealerId() ;
							   String second_dealer_code = "" + tmdealer.getDealerCode();
							   String balance_amount = ""+( labour_sum_01 + datum_sum_01);
							   double service_charge  = labour_sum_01;
							   double material_charge =  datum_sum_01;
							   
							   TtAsWrClaimSendTemPO sendTemPO = new TtAsWrClaimSendTemPO();
							   sendTemPO.setBalanceId(balanceId);
							   sendTemPO.setAmountSum(Double.parseDouble(balance_amount));
							   sendTemPO.setEnddate(formatter.parse(endTime));
							   sendTemPO.setSecondDealerCode(second_dealer_code);
							   sendTemPO.setStartdate(formatter.parse(startTime));
							   sendTemPO.setSecondDealerId(Long.parseLong(second_dealer_id));
							   sendTemPO.setYieldly(areaId);
							   sendTemPO.setBalanceYieldly(Integer.parseInt(yieldly));
							   sendTemPO.setDealerId(dealerid);
							   sendTemPO.setServiceCharge(service_charge);
							   sendTemPO.setMaterialCharge(material_charge);
							   dao.insert(sendTemPO);
						  }
					  }
				   }
			   }
		   }	
		}
	}
	
	@SuppressWarnings("unchecked")
	public void delaerFineQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			DealerKpDao dao = new DealerKpDao();
			String dealerCode= request.getParamValue("dealerCode");
			if(logonUser.getDealerCode() != null )
			{
				dealerCode = logonUser.getDealerCode();
			}
			String REMARK= request.getParamValue("REMARK");
			
			StringBuffer sql= new StringBuffer();
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			sql.append("SELECT DEALER_CODE,min(DEALER_NAME) DEALER_NAME ,min(IS_ROOD) IS_ROOD, min(CREATE_DATE) CREATE_DATE,sum(PLUS_MINUS_LABOUR_SUM) PLUS_MINUS_LABOUR_SUM,sum( PLUS_MINUS_DATUM_SUM) PLUS_MINUS_DATUM_SUM,sum(AMOUNT_SUM ) AMOUNT_SUM,min(DEALER_ID)  DEALER_ID , min(REMARK) REMARK from TT_AS_WR_CLAIM_BALANCE_FINE t where  1= 1 ");
			if(dealerCode != null && dealerCode.length() > 0)
			{
				sql.append(" and t.DEALER_CODE like '%"+dealerCode+"%' ");
			}
			if(REMARK != null && REMARK.length() > 0)
			{
				sql.append(" and  t.REMARK like '%"+REMARK+"%' ");
			}
			sql.append("  group by t.DEALER_CODE ");
			act.setOutData("ps", dao.pageQuery(sql.toString(), null,dao.getFunName(), Constant.PAGE_SIZE, curPage));
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "开票失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}	
	}
	
	
	public void insertBalancefine()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			DealerKpDao dao = new DealerKpDao();
			String dealerId= request.getParamValue("dealerId");
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(Long.parseLong(dealerId));
			dealerPO =(TmDealerPO) dao.select(dealerPO).get(0);
			
			TtAsWrFinePO finePO = new TtAsWrFinePO();
			finePO.setDealerId(Long.parseLong(dealerId));
			finePO.setPayStatus(11511001);
			if(dao.select(finePO).size() == 0)
			{
				act.setOutData("msg", "false");
			}else
			{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String startTime = simpleDateFormat.format(new Date());
				DealerKpDao balanceDao = DealerKpDao.getInstance();
				TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
				List<TmBusinessAreaPO> areaList= dao.select(areaPO);
				String jsNO = "";
				BalanceMainDao dao1 = new BalanceMainDao();
				TcUserPO userPO = new TcUserPO();
				userPO.setUser_id(logonUser.getUserId());
				jsNO= dao1.number_ap(((TcUserPO)(dao.select(userPO).get(0))).getAcnt(),startTime.split("-")[0] , startTime.split("-")[1], dealerPO.getDealerCode());
				for(TmBusinessAreaPO area : areaList)
				{
					double[] fine_sums = dao.getwrfineOrder(Long.parseLong(dealerId), "", "", area.getAreaId());
					//查询正负激励劳务费
					double fine_sum = fine_sums[0];
					//查询正负激励材料费
					double fine_psum = fine_sums[1];
					TtAsWrClaimBalanceFinePO balancePO = new TtAsWrClaimBalanceFinePO();
					String balanceNO = SequenceManager.getSequence("BO");//;request.getParamValue("BALANACE_NO");//结算单号
					String applyName = logonUser.getName();//request.getParamValue("APPLY_NAME");//申请人姓名
					String applyId = String.valueOf(logonUser.getUserId());//request.getParamValue("APPLY_ID");//申请人ID
					String dealerCode = dealerPO.getDealerCode();//request.getParamValue("DEALER_CODE");//经销商代码
					String dealerName = dealerPO.getDealerName();//request.getParamValue("DEALER_NAME");//经销商名称
					balancePO.setBalanceNo(CommonUtils.checkNull(balanceNO));
					balancePO.setApplyPersonId(applyId==null?-1:CommonUtils.parseLong(applyId));
					balancePO.setApplyPersonName(CommonUtils.checkNull(applyName));
					balancePO.setDealerCode(CommonUtils.checkNull(dealerCode));
					balancePO.setDealerName(CommonUtils.checkNull(dealerName));
					balancePO.setPlusMinusDatumSum(fine_psum)  ;
					balancePO.setPlusMinusLabourSum(fine_sum);
					balancePO.setAmountSum(Arith.add(fine_psum, fine_sum));
					balancePO.setId(CommonUtils.parseLong(SequenceManager.getSequence("")));
					balancePO.setBalanceYieldly(95411001);
					balancePO.setUpdateBy(logonUser.getUserId());
					balancePO.setUpdateDate(new Date());
					balancePO.setCreateBy(logonUser.getUserId());
					balancePO.setCreateDate(new Date());
					balancePO.setYieldly(area.getAreaId());
					balancePO.setDealerId(CommonUtils.parseLong(dealerId));
					balancePO.setCreateDate(new Date());
					balancePO.setRemark(jsNO);
					dao.insert(balancePO);
				}
				StringBuffer sql= new StringBuffer();
				// 艾春 9.22 更新 特殊开票更新语句
				sql.append("update tt_as_wr_fine t set t.PAY_STATUS = 11511002 ,t.BALANCE_ODER = '"+jsNO+"' \n" );
				sql.append(" where 1=1 \n" );
				sql.append("  and  t.PAY_STATUS = 11511001 ");
				sql.append("   and  t.DEALER_ID  = "+dealerId+" \n" );
				dao.update(sql.toString(), null);
				
				
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "开票失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}	
	}
	
	
	/********add by liuxh 20101126 增加结算室审核完成功能  调用更新结算标志和重新计算**********/
	public void settComplete(String blanceId){
		ActionContext act = ActionContext.getContext();
		try
		{	
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			//String blanceId=CommonUtils.checkNull(request.getParamValue("blanceId"));
			BalanceAuditing bAuditing = new BalanceAuditing("","");
			
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			
//			/*********判断索赔单是否全部审核完成**********/
//			boolean flagClaim=balanceDao.queryClaimByBanlanceId(Long.parseLong(blanceId));
//			boolean flagSp=balanceDao.querySpecByBanlanceId(Long.parseLong(blanceId));
//			if(!flagClaim){
//				throw new BizException("结算单下的索赔单未完全审核完成!");
//			}
//			if(!flagSp){
//				throw new BizException("结算单下的特殊费用工单未完全审核完成!");
//			}
			/*********判断索赔单是否全部审核完成**********/
			
			
			//2、根据标识的状态和特殊费用的状态统计特殊费用
			List<Map<String,Object>> feeList = balanceDao.getSpecialFeeByBalanceIdStatus(Long.parseLong(blanceId));
			double marketFee = 0;//市场工单费用
			double outFee = 0;   //特殊外出费用
			int feeCount = 0;
			if(feeList!=null && feeList.size()>0){
				for (Map<String, Object> feeMap : feeList) {
					if(feeMap.containsKey("FEE_TYPE")){
						if((Constant.FEE_TYPE_01).equals(feeMap.get("FEE_TYPE").toString())){
							if(feeMap.containsKey("DECLARE_SUM")){
								marketFee = ((BigDecimal)feeMap.get("DECLARE_SUM")).doubleValue();
							}
							if(feeMap.containsKey("FEETYPECOUNT")){
								feeCount = feeCount + ((BigDecimal)feeMap.get("FEETYPECOUNT")).intValue();
							}
						}else if(Constant.FEE_TYPE_02.equals(feeMap.get("FEE_TYPE").toString())){
							if(feeMap.containsKey("DECLARE_SUM")){
								outFee = ((BigDecimal)feeMap.get("DECLARE_SUM")).doubleValue();
							}
							if(feeMap.containsKey("FEETYPECOUNT")){
								feeCount = feeCount + ((BigDecimal)feeMap.get("FEETYPECOUNT")).intValue();
							}
						}
					}
				}
			}else{
				feeCount = 0;
			}
			
			//3、将特殊费用合计到结算单中
			balanceDao.addSpecialFeeToBalanceOrder(Long.parseLong(blanceId), marketFee, outFee, feeCount);
			balanceDao.updateMarkSpeeActiveFee(Long.parseLong(blanceId));//add by liuxh 20101227 完成时记录市场公单和外出公单费用
			
			//bAuditing.modifyBalanceStatus(Long.parseLong(blanceId), true,logonUser);
			//DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			//加载保养费用中固定的工时费用
			Double fixedAmount = balanceDao.queryFreeClaimFixedAmount(Constant.FREE_PARA_TYPE);
			//更新结算主表信息
			balanceDao.reCheckBalanceAmount(new Long(blanceId),fixedAmount);
			//更新结算明细表信息
			balanceDao.reCheckBalanceDetail(new Long(blanceId),fixedAmount);
			
			act.setOutData("msg", "true");
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error(e);
			act.setException(e);
		}
		
		
	}
	/********add by liuxh 20101126 增加结算室审核完成功能  调用更新结算标志和重新计算**********/
	
	/**
	 * 取的结算单信息
	 * @param request
	 * @return
	 */
	private String checkNull(Object obj,String repalce){
		String result = "";
		if(obj==null || "".equals(obj.toString()) || "null".equalsIgnoreCase(obj.toString())){
			result = repalce;
		}else{
			result = obj.toString();
		}
		return result;
	}
	/**
	 * 将特殊费用统计对应结算单中，同时标识对应特殊费用合计到该结算单
	 * @param dealerId 经销商ID
	 * @param balanceId 结算单ID
	 */
private void addSpecialFeeToBalanceOrder(Long dealerId,Long balanceId,String startDate,String endDate,String yieldly){
		
		if(dealerId==null || balanceId==null)
			return;
		DealerKpDao balanceDao = DealerKpDao.getInstance();
		//1、根据经销商将其他对应非费用标识到该结算单中
		/********mod by liuxh 20101116 增加时间段参数*******/
		balanceDao.markSpecialFeeToBalanceOrder(dealerId, balanceId,startDate,endDate,yieldly);
		//2、根据标识的状态和特殊费用的状态统计特殊费用
		List<Map<String,Object>> feeList = balanceDao.getSpecialFeeByBalanceId(balanceId);
		double marketFee = 0;//市场工单费用
		double outFee = 0;   //特殊外出费用
		double marketFeeBak= 0;
		double outFeeBak = 0;
		int feeCount = 0;
		if(feeList!=null && feeList.size()>0){
			for (Map<String, Object> feeMap : feeList) {
				if(feeMap.containsKey("FEE_TYPE")){
					if((Constant.FEE_TYPE_01).equals(feeMap.get("FEE_TYPE").toString())){
						if(feeMap.containsKey("DECLARE_SUM")){
							marketFee = ((BigDecimal)feeMap.get("DECLARE_SUM")).doubleValue();
							marketFeeBak = ((BigDecimal)feeMap.get("DECLARE_SUM1")).doubleValue();
						}
						if(feeMap.containsKey("FEETYPECOUNT")){
							feeCount = feeCount + ((BigDecimal)feeMap.get("FEETYPECOUNT")).intValue();
						}
					}else if(Constant.FEE_TYPE_02.equals(feeMap.get("FEE_TYPE").toString())){
						if(feeMap.containsKey("DECLARE_SUM")){
							outFee = ((BigDecimal)feeMap.get("DECLARE_SUM")).doubleValue();
							outFeeBak = ((BigDecimal)feeMap.get("DECLARE_SUM")).doubleValue();
						}
						if(feeMap.containsKey("FEETYPECOUNT")){
							feeCount = feeCount + ((BigDecimal)feeMap.get("FEETYPECOUNT")).intValue();
						}
					}
				}
			}
		}else{
			feeCount = 0;
		}
		
		//3、将特殊费用合计到结算单中
		balanceDao.addSpecialFeeToBalanceOrder2(balanceId, marketFee, outFee,marketFeeBak,outFeeBak, feeCount);
	}
	/**
	 * 点击开票生成单的操作zyw 2015-6-11优化和检查逻辑
	 */
	@SuppressWarnings("unchecked")
	public void doBalanceKp(){
		try{
			String dealerId = request.getParamValue("dealerId");
			String returnId = request.getParamValue("returnId");
			String endBalanceDate=request.getParamValue("endBalanceDate");
			String CON_END_DAY=request.getParamValue("CON_END_DAY");
			String labour_sum=request.getParamValue("amount");
			String CLAIM_NUM=request.getParamValue("CLAIM_NUM");
			String HOURS_SETTLEMENT_AMOUNT=request.getParamValue("HOURS_SETTLEMENT_AMOUNT");
			String PART_SETTLEMENT_AMOUNT=request.getParamValue("PART_SETTLEMENT_AMOUNT");
			String PDI_SETTLEMENT_AMOUNT=request.getParamValue("PDI_SETTLEMENT_AMOUNT");
			String FIRST_SETTLEMENT_AMOUNT=request.getParamValue("FIRST_SETTLEMENT_AMOUNT");
			String ACTIVITIE_SETTLEMENT_AMOUNT=request.getParamValue("ACTIVITIE_SETTLEMENT_AMOUNT");
			String OUTWARD_SETTLEMENT_AMOUNT=request.getParamValue("OUTWARD_SETTLEMENT_AMOUNT");
			String PN_EXACTION=request.getParamValue("PN_EXACTION");
			String GOOD_CLAIM_AMOUNT=request.getParamValue("GOOD_CLAIM_AMOUNT");
			String LAST_ADMINISTRATION_AMOUNT=request.getParamValue("LAST_ADMINISTRATION_AMOUNT");
			String THIS_ADMINISTRATION_AMOUNT=request.getParamValue("THIS_ADMINISTRATION_AMOUNT");
			//String RETURN_AMOUNT=request.getParamValue("RETURN_AMOUNT");
			//String AMOUNT_SUM=request.getParamValue("AMOUNT_SUM");
			//生成结算主表
			TtAsWrClaimBalancePO claimBlance=new TtAsWrClaimBalancePO();
			claimBlance.setId(Long.parseLong(SequenceManager.getSequence("")));
			DateFormat format = new SimpleDateFormat("yyMMdd"); 
			String Numberdate = format.format(new Date());
			String serialNumber=ation.GenerateNumber("tt_as_wr_claim_balance", "BALANCE_NO", "CREATE_DATE");
			claimBlance.setBalanceNo("SWJS"+Numberdate+serialNumber);//单据编码
			claimBlance.setDealerId(Long.parseLong(dealerId));
			claimBlance.setStatus(Constant.CLAIM_STAUTS_01);//已保存
			claimBlance.setLabourAmount(Double.parseDouble(HOURS_SETTLEMENT_AMOUNT));//工时
			claimBlance.setPartAmount(Double.parseDouble(PART_SETTLEMENT_AMOUNT));//配件
			claimBlance.setFreeAmount(Double.parseDouble(FIRST_SETTLEMENT_AMOUNT));//保养
			claimBlance.setServiceFixedAmount(Double.parseDouble(ACTIVITIE_SETTLEMENT_AMOUNT));//服务活动
			//claimBlance.setReturnAmount(Double.parseDouble(RETURN_AMOUNT));//运费
			//claimBlance.setAmountSum(Double.parseDouble(AMOUNT_SUM));//二次抵扣
			claimBlance.setClaimCount(Long.parseLong(CLAIM_NUM));//索赔单数
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			claimBlance.setStartDate(format1.parse(endBalanceDate));//开始时间
			claimBlance.setEndDate(format1.parse(CON_END_DAY));//结束时间
			claimBlance.setCreateBy(loginUser.getUserId());//创建人
			claimBlance.setCreateDate(new Date());//创建时间
			claimBlance.setAdminDeduct(Double.parseDouble(LAST_ADMINISTRATION_AMOUNT));//上次行政抵扣
			claimBlance.setFinancialDeduct(Double.parseDouble(THIS_ADMINISTRATION_AMOUNT));//本次行政扣款
			claimBlance.setNoteAmount(Double.parseDouble(labour_sum));//开票总金额
			claimBlance.setSpeoutfeeAmount(Double.parseDouble(OUTWARD_SETTLEMENT_AMOUNT));//外出
			claimBlance.setMarketAmount(Double.parseDouble(PDI_SETTLEMENT_AMOUNT));//pdi
			claimBlance.setAppendLabourAmount(Double.parseDouble(PN_EXACTION));//正负激励
			claimBlance.setAppendAmount(Double.parseDouble(GOOD_CLAIM_AMOUNT));//善于索赔
			dao.insert(claimBlance);
			//修改上次行政扣款（未使用改为已使用）
			TtAsWrAdministrativeChargePO chargePO = new TtAsWrAdministrativeChargePO();
			chargePO.setDealerid(Long.parseLong(dealerId));
			chargePO.setStatus(Long.parseLong("94151002"));
			TtAsWrAdministrativeChargePO upChargePO = new TtAsWrAdministrativeChargePO();
			upChargePO.setStatus(Long.parseLong("94151001"));
			dao.update(chargePO, upChargePO);
			//如果本次开票总金额小于0，添加本次行政扣款记录
			if(Double.parseDouble(labour_sum)<0){
				TtAsWrAdministrativeChargePO adChargePO = new TtAsWrAdministrativeChargePO(); 
				Long id = CommonUtils.parseLong(SequenceManager.getSequence(""));
				adChargePO.setId(id);
				adChargePO.setDealerid(Long.parseLong(dealerId));
				adChargePO.setBalanceOder(claimBlance.getBalanceNo());
				adChargePO.setStatus(Long.parseLong("94151002"));//未使用
				adChargePO.setLabourSum(-1*Double.parseDouble(labour_sum));//金额
				adChargePO.setCreatBy(loginUser.getUserId());
				adChargePO.setCreatDate(new Date());
				dao.insert(adChargePO);
			}
			//修改旧件清单为已结算
			TtAsWrReturnedOrderPO order=new TtAsWrReturnedOrderPO();
			order.setId(Long.parseLong(returnId));
			TtAsWrReturnedOrderPO upOrder=new TtAsWrReturnedOrderPO();
			upOrder.setIsBill(10041001);
			dao.update(order, upOrder);
			//修改索赔单为已结算
			List<Map<String, Object>> listClaimId=dao.seClaimId(dealerId, returnId, endBalanceDate, CON_END_DAY);
			if(listClaimId.size()>0){
				for(int i=0;i<listClaimId.size();i++){
					TtAsWrApplicationClaimPO claim=new TtAsWrApplicationClaimPO();
					claim.setId(Long.parseLong(listClaimId.get(i).get("ID").toString()));
					TtAsWrApplicationClaimPO upClaim=new TtAsWrApplicationClaimPO();
					upClaim.setIsBill(10041001);
					upClaim.setBillNo(claimBlance.getBalanceNo());
					dao.update(claim, upClaim);
				}
			}
			//修改正负激励为已结算
			TtAsWrFinePO fine=new TtAsWrFinePO();
			fine.setDealerId(Long.parseLong(dealerId));
			fine.setPayStatus(11511001);
			TtAsWrFinePO upfine=new TtAsWrFinePO();
			upfine.setPayStatus(11511002);
			upfine.setPayDate(new Date());
			upfine.setClaimbalanceId(claimBlance.getId());//结算id
			dao.update(fine, upfine);
			//修改善于索赔为已结算
			TtAsWrSpecialPO spe=new TtAsWrSpecialPO();
			spe.setDealerId(Long.parseLong(dealerId));
			spe.setIsClaim(10041002);
			spe.setStatus(20331014);
			TtAsWrSpecialPO upspe=new TtAsWrSpecialPO();
			upspe.setIsClaim(10041001);
			upspe.setClaimDate(new Date());
			upspe.setClaimNo(claimBlance.getBalanceNo());//结算单号
			dao.update(spe, upspe);
			//回运关系信息
			/*if(Utility.testString(ReturnedID)){
				String[] returnedids = ReturnedID.split(",");
				for (String id : returnedids) {
					dao.findReturnAndBackUp(id,jsNO);//添加关联回运关系
					TtAsWrOldReturnedPO  returnedPO = new TtAsWrOldReturnedPO();
					returnedPO.setId(Long.parseLong(id));
					TtAsWrOldReturnedPO  returnedPO1 = new TtAsWrOldReturnedPO();
					returnedPO1.setIsInvoice(1);
					dao.update(returnedPO, returnedPO1);
				}
			}*/		
			act.setOutData("msg", "0");
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e);
			act.setException(e);
		}
	}
	/**
	 * 
	* @Title: updateMarketStatus 
	* @Description: TODO(点完复核申请,将状态更新成已结算) 
	* @param @param dealerId    经销商ID
	* @return void    返回类型 
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
	/*TT_AS_DEALER_TYPE
    开始时间=BALANCE_REVIEW_DATE+1天
    结束时间=OLD_REVIEW_DATE+通过经销商产地取出的月份>=当前月
    结束时间=当前月-1月
    OLD_REVIEW_DATE+通过经销商产地取出的月份<当前月
    结束时间=OLD_REVIEW_DATE+通过经销商产地取出的月份*/
	public void showDate() throws ParseException{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String yieldly = request.getParamValue("yieldly");
		TtAsDealerTypePO po = new TtAsDealerTypePO();
		po.setDealerId(Long.parseLong(logonUser.getDealerId()));
		po.setYieldly(yieldly);
		TtAsDealerTypePO poValue = (TtAsDealerTypePO)factory.select(po).get(0);
		
		Date balanceReViewDate=poValue.getBalanceReviewDate();
		Date oldReViewDate=poValue.getOldReviewDate();
		
		Calendar calendar = Calendar.getInstance();//公用类，加年月日的
		calendar.setTime(balanceReViewDate);
		calendar.add(Calendar.DAY_OF_MONTH,1);//当月加一天
		balanceReViewDate = calendar.getTime();//得到加一天后的值
		
		String place="";
		if(yieldly.equals(Constant.SERVICEACTIVITY_CAR_YIELDLY_01.toString())) place=Constant.PLACE_OF_CH;
		if(yieldly.equals(Constant.SERVICEACTIVITY_CAR_YIELDLY_02.toString())) place=Constant.PLACE_OF_HB;
		if(yieldly.equals(Constant.SERVICEACTIVITY_CAR_YIELDLY_03.toString())) place=Constant.PLACE_OF_NJ;
		
//		TcCodePO code=new TcCodePO();
//		code.setCodeId(place);
//		code=(TcCodePO)factory.select(code).get(0);
//		String month=code.getCodeDesc();//通过产地取得月数
		
		TmPtOlderItemPO item=new TmPtOlderItemPO();
		item.setYieldly(Long.parseLong(yieldly));
		item.setDealerId(Long.parseLong(logonUser.getDealerId()));
		item=(TmPtOlderItemPO)factory.select(item).get(0);
		String month=String.valueOf(item.getReturnDate());
		
		Calendar calendarOldRe = Calendar.getInstance(); 
		calendarOldRe.setTime(oldReViewDate);
		calendarOldRe.add(Calendar.MONTH, Integer.parseInt(month));
		Date tmpDate=calendarOldRe.getTime();
		System.out.println("--------------"+tmpDate);
		Date currDate=new Date();
		Date endDate=null;
		Date endDate2 = tmpDate;
		Date kkkDate=lastDayOfMonth(tmpDate);
		
  		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(format.parse(format.format(kkkDate)).after(format.parse(format.format(currDate)))||(!format.parse(format.format(kkkDate)).after(format.parse(format.format(currDate)))&&!format.parse(format.format(currDate)).after(format.parse(format.format(kkkDate))))){//tmpDate大于currDate或tmpDate等于currDate
			Calendar calendarOldRe2 = Calendar.getInstance();
			calendarOldRe2.setTime(new Date());
			//calendarOldRe2.add(Calendar.MONTH, -1);//当前月减1月
			calendarOldRe2.setTime(lastDayOfMonth(calendarOldRe2.getTime()));
			endDate=calendarOldRe2.getTime();
		}else{
			
			endDate=lastDayOfMonth(endDate2);
		}
		Calendar calendarDay=Calendar.getInstance();
		calendarDay.setTime(endDate);
		
		TcCodePO code=new TcCodePO();
		code.setCodeId(Constant.DAY_12);
		code=(TcCodePO)factory.select(code).get(0);
		String dayStr=code.getCodeDesc();
		int monthCur=calendarDay.get(Calendar.MONTH)+1;
		//if(monthCur==12){//如果是12月
		//	calendarDay.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayStr));
		//}
		int yearStart=calendar.get(Calendar.YEAR);
		int yearEnd=calendarDay.get(Calendar.YEAR);
		int dayStart=calendarDay.get(Calendar.DAY_OF_MONTH);
		int startDate = calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println("-----------"+yearStart+"--------------"+yearEnd);
		//判断是否跨年
		if(yearEnd>yearStart){//是跨年
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date22=sdf.parse(yearStart+"-12-"+dayStr);
			//判断跨年后开始时间天数大约结束时间天数不能强制设置为12月的结算点
			Calendar calendarDay1=Calendar.getInstance();
			calendarDay1.setTime(date22);
			Date dateAdd = null;
			int newDay = calendarDay1.get(Calendar.DAY_OF_MONTH);
			if(startDate<newDay){
				
				calendarDay=Calendar.getInstance();
				calendarDay.setTime(date22);
			}else{
				Calendar calendarDate=Calendar.getInstance();
				calendarDate.setTime(endDate);
				int year1 = calendarDate.get(Calendar.YEAR);
				int mouth1 = calendarDate.get(Calendar.MONTH)+1;
				int day1 = calendarDate.get(Calendar.DAY_OF_MONTH);
				Calendar calendarDateNow=Calendar.getInstance();
				calendarDateNow.setTime(new Date());
				int dayNow = calendarDateNow.get(Calendar.DAY_OF_MONTH);
				System.out.println("-----------"+year1+mouth1+day1);
				
				
				if(mouth1==12){
					if(Integer.valueOf(dayStr)<day1&&dayNow>Integer.valueOf(dayStr)){
						Date d =sdf.parse(year1+"-12-"+dayStr);
						calendarDay.setTime(d);
					}else{
						if(format.parse(format.format(endDate)).after(format.parse(format.format(new Date())))||(!format.parse(format.format(endDate)).after(format.parse(format.format(new Date())))&&!format.parse(format.format(new Date())).after(format.parse(format.format(endDate))))){//tmpDate大于currDate或tmpDate等于currDate
						calendarDay.setTime(lastDayOfMonth2(endDate));
						}else{
							calendarDay.setTime(endDate);	
						}
					}
				}else{
					calendarDay=Calendar.getInstance();
					System.out.println("--------------------------"+lastDayOfMonth2(endDate));
					if(format.parse(format.format(endDate)).after(format.parse(format.format(new Date())))||(!format.parse(format.format(endDate)).after(format.parse(format.format(new Date())))&&!format.parse(format.format(new Date())).after(format.parse(format.format(endDate))))){//tmpDate大于currDate或tmpDate等于currDate
						calendarDay.setTime(lastDayOfMonth2(endDate));
						}else{
							calendarDay.setTime(endDate);	
						}
				}
				
			}
			
			
		}
		if(yearEnd==yearStart){ 
			if(monthCur==12){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Date date22=sdf.parse(yearStart+"-12-"+dayStr);
				//判断跨年后开始时间天数大约结束时间天数不能强制设置为12月的结算点
				Calendar calendarDay1=Calendar.getInstance();
				Calendar calendarDayNow=Calendar.getInstance();
				calendarDayNow.setTime(new Date());
				calendarDay1.setTime(date22);
				int newDay = calendarDay1.get(Calendar.DAY_OF_MONTH);
				int nowDate2 =calendarDayNow.get(Calendar.DAY_OF_MONTH);
				if(newDay>=nowDate2 ) System.out.println("-----------------------"+format.format(date22));
				if(startDate<newDay){
					calendarDay=Calendar.getInstance();
					if(nowDate2<=newDay ){
						calendarDay.setTime(lastDayOfMonth2(date22));
					}
					else{
					  calendarDay.setTime(date22);
					  
					  
					}
					
				}else{
					calendarDay.setTime(endDate);
				}
				
			}else{
				if(format.parse(format.format(endDate)).after(format.parse(format.format(new Date())))||(!format.parse(format.format(endDate)).after(format.parse(format.format(new Date())))&&!format.parse(format.format(new Date())).after(format.parse(format.format(endDate))))){//tmpDate大于currDate或tmpDate等于currDate
					calendarDay.setTime(lastDayOfMonth2(endDate));
				}else{
					calendarDay.setTime(endDate);
				}
			}
		}
		if(yearEnd<yearStart){
			calendarDay.setTime(endDate);
			
		}
		
		act.setOutData("balanceDateBegin", balanceReViewDate);
		act.setOutData("balanceDateEnd", calendarDay.getTime());
		//act.setOutData("balanceDateEnd", "2011-03-31");
	}
	
	
	
	public Date lastDayOfMonth(Date date) { 
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
		cal.set(Calendar.DAY_OF_MONTH, 1); 
		cal.roll(Calendar.DAY_OF_MONTH, -1); 
		
		return cal.getTime(); 
	} 
	public Date lastDayOfMonth2(Date date) { 
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
		cal.set(Calendar.DAY_OF_MONTH, 1); 
		cal.add(Calendar.MONTH, -1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.format(cal.getTime());
		System.out.println("-----------------------"+format.format(cal.getTime()));
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);

		System.out.println("-----------------------"+format.format(cal.getTime()));
		return cal.getTime(); 
	} 


	/**
	 * 开票退回
	 */
	@SuppressWarnings("unchecked")
	public void kpDelete(){
		ActionContext act = ActionContext.getContext();
		try{
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request=act.getRequest();
			long banalceId=Long.parseLong(request.getParamValue("balanceId"));
			TtAsWrClaimBalancePO balancePo=new TtAsWrClaimBalancePO();
			balancePo.setId(banalceId);
			balancePo=(TtAsWrClaimBalancePO)factory.select(balancePo).get(0);
			TtAsDealerTypePO dealerType=new TtAsDealerTypePO();
			dealerType.setYieldly(balancePo.getYieldly().toString());
			dealerType.setDealerId(balancePo.getDealerId());
			dealerType=(TtAsDealerTypePO)factory.select(dealerType).get(0);
			Date balanceEnd=balancePo.getEndDate();
			Date balanceDate=dealerType.getBalanceDate();
			Date createDate=balancePo.getCreateDate();
			Date date58=new SimpleDateFormat("yyyy-MM-dd").parse("2011-05-07"); 
			if(!createDate.after(date58)){//5月8日前结算单不能删除
				act.setOutData("msg", "false1");
			}else if(!balanceEnd.after(balanceDate)&&!balanceDate.after(balanceEnd)){//可删除
				StringBuffer sbSql=new StringBuffer();
				sbSql.append("UPDATE   Tt_As_Dealer_Type \n" );
				sbSql.append("   SET   balance_date = ?, balance_review_date = ? \n");
				sbSql.append(" WHERE   DEALER_ID = ? AND YIELDLY = ? \n");
				
				Calendar cale=Calendar.getInstance();
				cale.setTime(balancePo.getStartDate());
				cale.add(Calendar.DAY_OF_MONTH, -1);
				List listPar=new ArrayList();
				listPar.add(cale.getTime());
				listPar.add(cale.getTime());
				listPar.add(balancePo.getDealerId());
				listPar.add(balancePo.getYieldly());
				factory.update(sbSql.toString(), listPar);
				
				////////更改结算单状态
			    StringBuffer sbSql2=new StringBuffer();
		        sbSql2.append("UPDATE   TT_AS_WR_APPLICATION A \n" );
		        sbSql2.append("   SET   IS_INVOICE = 0,STATUS=?, CLAIM_BLANCE_ID=NULL \n");
		        sbSql2.append(" WHERE   EXISTS (SELECT   * \n");
		        sbSql2.append("                   FROM   TR_BALANCE_CLAIM B \n");
		        sbSql2.append("                  WHERE   A.ID = B.CLAIM_ID AND B.BALANCE_ID = ?) \n");
		        List listPar2=new ArrayList();
		        listPar2.add(Constant.CLAIM_APPLY_ORD_TYPE_07);
		        listPar2.add(balancePo.getId());
		        factory.update(sbSql2.toString(), listPar2);
		        
				String sql1="DELETE FROM TR_BALANCE_CLAIM WHERE BALANCE_ID=?";
				String sql2="DELETE FROM TT_AS_WR_CLAIM_BALANCE_DETAIL WHERE BALANCE_ID=?";
				String sql3="DELETE FROM TT_AS_WR_BALANCE_DETAIL_BAK WHERE BALANCE_ID=?";
				String sql4="DELETE FROM TT_AS_WR_CLAIM_BALANCE WHERE ID=?";
				List parDel=new ArrayList();
				parDel.add(balancePo.getId());
				factory.update(sql1, parDel);
				factory.update(sql2, parDel);
				factory.update(sql3, parDel);
				factory.update(sql4, parDel);
				
				
				/*****市场公单除去子类型为市场公单******/
				StringBuffer sbSqlFee1=new StringBuffer();
				sbSqlFee1.append("UPDATE   TT_AS_WR_SPEFEE \n" );
				sbSqlFee1.append("   SET   CLAIMBALANCE_ID = NULL,CLAIMBALANCE_ID_TMP = NULL, STATUS = ? \n");
				sbSqlFee1.append(" WHERE   CLAIMBALANCE_ID = ? \n");
				sbSqlFee1.append("         AND FEE_TYPE = ? \n");
				sbSqlFee1.append("         AND FEE_CHANNEL<> ? AND FEE_CHANNEL<> ? \n");
				List parFee1=new ArrayList();
				parFee1.add(Constant.SPEFEE_STATUS_06);
				parFee1.add(balancePo.getId());
				parFee1.add(Constant.FEE_TYPE_01);
				parFee1.add(Constant.FEE_TYPE1_01);
				parFee1.add(Constant.FEE_TYPE1_03);
				factory.update(sbSqlFee1.toString(), parFee1);
				/*****市场公单除去子类型为市场公单******/
				
				StringBuffer sbSqlRet=new StringBuffer();
				sbSqlRet.append("UPDATE TT_AS_WR_AUTH_PRICE SET STATUS=?,CLAIMBALANCE_ID=-1 WHERE \n");
				sbSqlRet.append("      CLAIMBALANCE_ID=? \n");
				List parRet=new ArrayList();
				parRet.add(Constant.OLD_PRICE_1);
				parRet.add(balancePo.getId());
				factory.update(sbSqlRet.toString(), parRet);
				 
				/*****市场公单 活动公单******/
				StringBuffer sbSqlFee2=new StringBuffer();
				sbSqlFee2.append("UPDATE   TT_AS_WR_SPEFEE \n" );
				sbSqlFee2.append("   SET   CLAIMBALANCE_ID = NULL,CLAIMBALANCE_ID_TMP = NULL, STATUS = ? \n");
				sbSqlFee2.append(" WHERE   CLAIMBALANCE_ID = ? \n");
				sbSqlFee2.append("         AND FEE_TYPE = ? \n");
				sbSqlFee2.append("         AND FEE_CHANNEL IN(?,?) \n");
				List parFee2=new ArrayList();
				parFee2.add(Constant.SPEFEE_STATUS_06);
				parFee2.add(balancePo.getId());
				parFee2.add(Constant.FEE_TYPE_01);
				parFee2.add(Constant.FEE_TYPE1_01);
				parFee2.add(Constant.FEE_TYPE1_03);
				factory.update(sbSqlFee2.toString(), parFee2);
				/*****市场公单 活动公单******/
				 
				/*****特殊外出******/
				StringBuffer sbSqlFee3=new StringBuffer();
				sbSqlFee3.append("UPDATE   TT_AS_WR_SPEFEE \n" );
				sbSqlFee3.append("   SET   CLAIMBALANCE_ID = NULL,CLAIMBALANCE_ID_TMP = NULL, STATUS = ? \n");
				sbSqlFee3.append(" WHERE   CLAIMBALANCE_ID = ? \n");
				sbSqlFee3.append("         AND FEE_TYPE = ? \n");
				List parFee3=new ArrayList(); 
				parFee3.add(Constant.SPEFEE_STATUS_06);
				parFee3.add(balancePo.getId());
				parFee3.add(Constant.FEE_TYPE_02);
				factory.update(sbSqlFee3.toString(), parFee3);
				/*****特殊外出******/
				
				
				//2、旧件抵扣与此结算单的关系断掉，并更改状态为-- 有效 10011001
				StringBuffer oldpartSql = new StringBuffer("\n") ;
				oldpartSql.append("update tt_as_wr_deduct_balance d\n");
				oldpartSql.append("   set d.status = ").append(Constant.STATUS_ENABLE).append(",\n");
				oldpartSql.append("       d.claimbalance_id = '',\n");
				oldpartSql.append("       d.update_date = sysdate,\n");  
				oldpartSql.append("       d.update_by = ").append(logonUser.getUserId()).append("\n");
				oldpartSql.append(" where d.status = ").append(Constant.STATUS_DISABLE).append("\n");  
				oldpartSql.append("   and d.claimbalance_id =").append(balancePo.getId());
				factory.update(oldpartSql.toString(),null) ;
				
				//3、奖惩与此结单的关系断掉，并更改状态为-- 未支付11511001
				StringBuffer fineSql = new StringBuffer("\n") ;
				fineSql.append("update tt_as_wr_fine f\n");
				fineSql.append("   set f.claimbalance_id = '',\n");  
				fineSql.append("       f.pay_status = ").append(Constant.PAY_STATUS_01).append(",\n");  
				fineSql.append("       f.update_by = ").append(logonUser.getUserId()).append(",\n");  
				fineSql.append("       f.update_date = sysdate\n");  
				fineSql.append(" where f.pay_status = ").append(Constant.PAY_STATUS_02).append("\n");  
				fineSql.append("   and f.claimbalance_id =").append(balancePo.getId());
				factory.update(fineSql.toString(),null) ;
				
				//4、行政扣款与此结算单的关系断掉，将更改状态为-- 未支付11521001
				StringBuffer adminSql = new StringBuffer("\n");
				adminSql.append("update tt_as_wr_admin_deduct d\n");
				adminSql.append("   set d.claimbalance_id = '',\n");  
				adminSql.append("       d.deduct_status = ").append(Constant.ADMIN_STATUS_01).append(",\n");  
				adminSql.append("       d.update_by = ").append(logonUser.getUserId()).append(",\n");  
				adminSql.append("       d.update_date = sysdate\n");  
				adminSql.append(" where d.deduct_status = ").append(Constant.ADMIN_STATUS_02).append("\n");   
				adminSql.append("   and d.claimbalance_id =").append(balancePo.getId());
				factory.update(adminSql.toString(), null);
	            
				//5、清除审核此结算单的相应记录，11861006
				StringBuffer authSql = new StringBuffer("\n");
				authSql.append("delete from tt_as_wr_balance_authitem a\n");
				authSql.append(" where a.auth_status = ").append(BalanceStatusRecord.STATUS_06).append("\n");  
				authSql.append("   and a.balance_id =").append(balancePo.getId());
				factory.delete(authSql.toString(),null) ;
				
				/***清除行政扣款***xiongchuan 2011-6-11*/
				
				StringBuffer adminSql1 = new StringBuffer("\n");
				adminSql1.append("delete from Tt_As_Wr_Admin_Deduct  d \n");
				
				adminSql1.append("   where d.from_claimbalance_id=").append(balancePo.getId());
				factory.delete(adminSql1.toString(),null) ;
				/***清除行政扣款***xiongchuan 2011-6-11*/
//				//6、清空此结算单的旧件扣款，考核扣款，行政扣款
//				StringBuffer balanceSql = new StringBuffer("\n");
//				balanceSql.append("update tt_as_wr_claim_balance b\n");
//				balanceSql.append("   set b.check_deduct = 0,\n");  
//				balanceSql.append("       b.old_deduct = 0,\n");  
//				balanceSql.append("       b.admin_deduct = 0,\n");
//				balanceSql.append("       b.FREE_DEDUCT = 0,\n");
//				balanceSql.append("       b.SERVICE_DEDUCT = 0,\n");
//				balanceSql.append("       b.return_amount = b.return_amount_bak,\n");
//	            balanceSql.append("       b.status = ").append(Constant.ACC_STATUS_02).append(",\n"); 
//				balanceSql.append("       b.update_by = ").append(logonUser.getUserId()).append(",\n");  
//				balanceSql.append("       b.update_date = sysdate\n");  
//				balanceSql.append(" where b.id = ").append(balancePo.getId());
//				factory.update(balanceSql.toString(),null) ;
//				
//				//7、重新计算结算费用 -- 引用复核申请统计金额的SQL
//				StringBuffer sbSqlKK=new StringBuffer();
//				sbSqlKK.append("UPDATE TT_AS_WR_CLAIM_BALANCE SET BALANCE_AMOUNT=((NVL(LABOUR_AMOUNT,0)+NVL(PART_AMOUNT,0)+NVL(OTHER_AMOUNT,0)+NVL(FREE_AMOUNT,0)");
//				sbSqlKK.append("+NVL(RETURN_AMOUNT,0)+NVL(MARKET_AMOUNT,0)+NVL(SPEOUTFEE_AMOUNT,0)+NVL(APPEND_AMOUNT,0)+NVL(SERVICE_TOTAL_AMOUNT,0))");
//				sbSqlKK.append("-(NVL(FREE_DEDUCT,0)+NVL(SERVICE_DEDUCT,0)+NVL(CHECK_DEDUCT,0)+NVL(OLD_DEDUCT,0)+NVL(ADMIN_DEDUCT,0)+NVL(FINANCIAL_DEDUCT,0)))\n");
//				sbSqlKK.append("WHERE id = ").append(balancePo.getId());
//				factory.update(sbSqlKK.toString(), null);
				
				//记录日志表 -- 添加一个经销商确认时间
				BalanceStatusRecord.recordStatus(balancePo.getId(), logonUser.getUserId(), logonUser.getName(), logonUser.getOrgId(), BalanceStatusRecord.STATUS_BACK);
				
				//zhumingwei 2011-11-16 删除对应生成抽查单据同时回滚抽查的时间范围
				TtAsWrCheckApplicationPO poCheck = new TtAsWrCheckApplicationPO();
				poCheck.setBalanceNo(balancePo.getBalanceNo());
				List list = factory.select(poCheck);
				if(list.size()>0){
					TtAsWrCheckApplicationPO poCheckValue = (TtAsWrCheckApplicationPO)list.get(0);
					TtAsWrAuthCheckAppPO poApp = new TtAsWrAuthCheckAppPO();
					poApp.setCheckId(poCheckValue.getId());
					factory.delete(poApp);
					TtAsWrCheckDetailPO poDetail = new TtAsWrCheckDetailPO();
					poDetail.setCheckId(poCheckValue.getId());
					factory.delete(poDetail);
					factory.delete(poCheck);
					
					//回滚时间
					DealerBalanceDao dao = new DealerBalanceDao();
					String time2 = "";
					String time1 = dao.getMaxDate1(logonUser.getDealerId(),balancePo.getYieldly());//得到时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					if(sdf.parse(time1).after(sdf.parse(Constant.FRIST_TIME))){
						time2=time1;
					}else{
						time2=Constant.FRIST_TIME;
					}
					TtAsDealerCheckPO poCehck = new TtAsDealerCheckPO();
					poCehck.setDealerId(Long.parseLong(logonUser.getDealerId()));
					poCehck.setYieldly(balancePo.getYieldly());
					TtAsDealerCheckPO poCehckValue = new TtAsDealerCheckPO();
					poCehckValue.setLastCheckDate(sdf.parse(time2));
					poCehckValue.setUpdateBy(logonUser.getUserId().toString());
					poCehckValue.setUpdateDate(new Date());
					factory.update(poCehck, poCehckValue);
				}
				//zhumingwei 2011-11-16   删除对应生成抽查单据同时回滚抽查的时间范围
				
				act.setOutData("msg", "true");
				/********Iverson 2012-02-24 调用开票信息下发接口下发删除的开票信息****************/
				OSB34 osb34 = new OSB34();
				osb34.sendDate(balancePo.getBalanceNo(),balancePo.getDealerCode(),0);
			}else{//不可删除
				act.setOutData("msg", "false");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			act.setException(e);
		}
	}
	
	/*
	 * 查看开票结果明细
	 */
	public void queryDealerBalanceInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			String infoFlag = request.getParamValue("infoFlag");
			BalanceMainDao dao = new BalanceMainDao();
			Map<String, Object> map = dao.getDealerQueryInfo(id);
			List<Map<String, Object>> detail =  dao.getBalanceMainList1(id);
			//List<Map<String, Object>> unit =  dao.getinvoiceUnit(id);
			act.setOutData("infoFlag", infoFlag);
			act.setOutData("map", map);
			act.setOutData("detail", detail);
			//act.setOutData("unit", unit);
			act.setForword("/jsp/claim/authorization/dealerQueryKpInfo.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**********
	 * :开票通知单打印
	 * 
	 */
	public void invoicePrint(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			BalanceMainDao dao = new BalanceMainDao();
			String id =request.getParamValue("id");
			List<Map<String, Object>> detail = dao.getBalanceMainList(id);
			List<Map<String, Object>> list = dao.getinvoiceView(id);
			List l = dao.getDate2(id) ;
			if(l.size()>0){
				act.setOutData("audit", (TtAsWrBalanceAuthitemPO)l.get(0));
			}
			act.setOutData("list", list.get(0));
			act.setOutData("detail", detail);
			act.setForword("/jsp/claim/authorization/invoiceKpPrint.jsp");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"经销商确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void kpClaimList(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String action=request.getParamValue("action")==null?"":request.getParamValue("action");
			String balanceId=request.getParamValue("balanceId");
			if(action.equals("do")){
				DealerKpDao dao = new DealerKpDao();
				//分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页	
						
				PageResult<Map<String, Object>> ps = dao.kpClaimList(Long.valueOf(balanceId),50,curPage);
				//分页方法 end
				act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
			}else{
				act.setOutData("balanceId", balanceId);
				act.setForword("/jsp/claim/authorization/kpClaimList.jsp");
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-11-01
	public void checkApplicationInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(DEALER_CHECK_APPLICATION_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-12-28
	public void checkApplicationAuthInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(DEALER_CHECK_APP_AUTH_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-11-01
	public void dealerCheckApplicationQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Map<String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			String dealerId = request.getParamValue("dealerId");
			String yieldly = request.getParamValue("yieldly");
			String CHECK_NO = request.getParamValue("CHECK_NO");
			String STATUS = request.getParamValue("STATUS");
			String BALANCE_NO = request.getParamValue("BALANCE_NO");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			String balanceStartDate = request.getParamValue("balanceStartDate");
			String balanceEndDate = request.getParamValue("balanceEndDate");
			
			map.put("dealerId", dealerId);
			map.put("yieldly", yieldly);
			map.put("CHECK_NO", CHECK_NO);
			map.put("STATUS", STATUS);
			map.put("BALANCE_NO", BALANCE_NO);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("balanceStartDate", balanceStartDate);
			map.put("balanceEndDate", balanceEndDate);
			
			//zhumingwei 2012-08-21 Bigin
			Long userId = logonUser.getUserId();
			//查询职位中业务范围
			Long poseId = logonUser.getPoseId();//职位ID
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			map.put("areaIds", areaIds);
			//zhumingwei 2012-08-21 end
			
			DealerKpDao dao  = new DealerKpDao();
			PageResult<Map<String, Object>> ps = dao.queryDealerApplication(map,userId, 10, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-11-01
	public void dealerCheckApplicationQuery1() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		Map<String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			String BALANCE_NO = request.getParamValue("BALANCE_NO");
			String CHECK_NO = request.getParamValue("CHECK_NO");
			String STATUS = request.getParamValue("STATUS");
			
			String yieldly = request.getParamValue("yieldly");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			String balanceStartDate = request.getParamValue("balanceStartDate");
			String balanceEndDate = request.getParamValue("balanceEndDate");
			
			map.put("dealerId", dealerId);
			map.put("CHECK_NO", CHECK_NO);
			map.put("BALANCE_NO", BALANCE_NO);
			map.put("STATUS", STATUS);
			
			map.put("yieldly", yieldly);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("balanceStartDate", balanceStartDate);
			map.put("balanceEndDate", balanceEndDate);
			
			DealerKpDao dao  = new DealerKpDao();
			PageResult<Map<String, Object>> ps = dao.queryDealerApplication1(map, 10, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//zhumingwei 2011-11-01
	public void printForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		DealerKpDao dao  = new DealerKpDao();
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			
			TtAsWrCheckApplicationPO po = new TtAsWrCheckApplicationPO();
			po.setId(Long.parseLong(id));
			TtAsWrCheckApplicationPO poValue = new TtAsWrCheckApplicationPO();
			poValue.setIsFrint(1);
			poValue.setUpdateBy(logonUser.getUserId().toString());
			poValue.setUpdateDate(new Date());
			daoKP.update(po, poValue);
			
			List<Map<String, Object>> list = dao.getCheckApplication(id);
			List<Map<String, Object>> detail = dao.getCheckApplicationDetail(id);
			
			act.setOutData("list", list.get(0));
			act.setOutData("detail", detail);
			
			act.setForword(DEALER_CHECK_PRINT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-11-08
	public void dealerCheckAppAuth(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		DealerKpDao dao  = new DealerKpDao();
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			TtAsWrCheckApplicationPO po = new TtAsWrCheckApplicationPO();
			po.setId(Long.parseLong(id));
			TtAsWrCheckApplicationPO poValue = new TtAsWrCheckApplicationPO();
			poValue.setSb_status(Constant.SB_STATUS_3);
			poValue.setAuthDate(new Date());
			poValue.setStatus(Constant.CHECK_APP_STATUS_2);
			poValue.setUpdateBy(logonUser.getUserId().toString());
			poValue.setUpdateDate(new Date());
			dao.update(po,poValue);
			TtAsWrCheckDetailPO poDetail = new TtAsWrCheckDetailPO();
			poDetail.setCheckId(Long.parseLong(id));
			TtAsWrCheckDetailPO poDetailValue = new TtAsWrCheckDetailPO();
			poDetailValue.setStatus(Constant.CHECK_APP_DETAIL_STATUS_2);
			poDetailValue.setUpdateBy(logonUser.getUserId().toString());
			poDetailValue.setUpdateDate(new Date());
			dao.update(poDetail,poDetailValue);
			act.setOutData("flag", true);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-11-08
	public void dealerCheckAppAuthInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		DealerKpDao dao  = new DealerKpDao();
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			List<Map<String, Object>> list = dao.getCheckApplication(id);
			List<Map<String, Object>> detail = dao.getCheckApplicationDetailOme(id);
			
			act.setOutData("list", list.get(0));
			act.setOutData("detail", detail);
			
			act.setForword(DEALER_CHECK_APP_AUTH_INIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-11-08
	public void dealerCheckAppAuthInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		DealerKpDao dao  = new DealerKpDao();
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");//索赔单ID
			String checkId = request.getParamValue("checkId");//批次号ID
			List<Map<String, Object>> PowerOfAttorney = dao.getCodeType("9099");
			act.setOutData("detail", PowerOfAttorney);
			
			List<Map<String, Object>> PowerOfAttorney1 = dao.getCodeType1("9100");
			act.setOutData("detail1", PowerOfAttorney1);
			
			List<Map<String, Object>> PowerOfAttorney2 = dao.getCodeType2("9101");
			act.setOutData("detail2", PowerOfAttorney2);
			
			List<Map<String, Object>> PowerOfAttorney3 = dao.getCodeType3("9102");
			act.setOutData("detail3", PowerOfAttorney3);
			
			List<Map<String, Object>> PowerOfAttorney4 = dao.getCodeType4("9103");
			act.setOutData("detail4", PowerOfAttorney4);
			
			List<Map<String, Object>> claimSit = dao.getCheckSit(id);
			
			//取得备注字段
			TtAsWrCheckDetailPO po = new TtAsWrCheckDetailPO();
			po.setClaimId(Long.parseLong(id));
			TtAsWrCheckDetailPO poValue = (TtAsWrCheckDetailPO)dao.select(po).get(0);
			String rem = poValue.getRemark();
			
			act.setOutData("ID", id);
			act.setOutData("checkId", checkId);
			act.setOutData("claimSit", claimSit);
			act.setOutData("rem", rem);
			act.setForword(DEALER_CHECK_APP_AUTH_INFO_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//zhumingwei 2011-11-08
	public void save(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		DealerKpDao dao  = new DealerKpDao();
		String claimId = request.getParamValue("claimId");//索赔单ID
		
		TtAsWrAuthCheckAppPO poDel = new TtAsWrAuthCheckAppPO();
		poDel.setClaimId(Long.parseLong(claimId));
		dao.delete(poDel);
		
		String checkId = request.getParamValue("checkId");//批次号ID
		String recesel [] = request.getParamValues("recesel");//获取一个集合预授权的ID
		String recesel1 [] = request.getParamValues("recesel1");//获取一个集合预授权的ID
		String recesel2 [] = request.getParamValues("recesel2");//获取一个集合预授权的ID
		String recesel3 [] = request.getParamValues("recesel3");//获取一个集合预授权的ID
		String recesel4 [] = request.getParamValues("recesel4");//获取一个集合预授权的ID
		if(recesel!=null){
			for(int i=0;i<recesel.length;i++){
				TtAsWrAuthCheckAppPO po = new TtAsWrAuthCheckAppPO();
				po.setId(Long.parseLong(SequenceManager.getSequence("")));
				po.setCheckId(Long.parseLong(checkId));
				po.setClaimId(Long.parseLong(claimId));
				po.setCheckSituation(Integer.parseInt(recesel[i]));
				po.setCreateBy(logonUser.getUserId().toString());
				po.setCreateDate(new Date());
				dao.insert(po);
			}
		}
		if(recesel1!=null){
			for(int j=0;j<recesel1.length;j++){
				TtAsWrAuthCheckAppPO po1 = new TtAsWrAuthCheckAppPO();
				po1.setId(Long.parseLong(SequenceManager.getSequence("")));
				po1.setCheckId(Long.parseLong(checkId));
				po1.setClaimId(Long.parseLong(claimId));
				po1.setCheckSituation(Integer.parseInt(recesel1[j]));
				po1.setCreateBy(logonUser.getUserId().toString());
				po1.setCreateDate(new Date());
				dao.insert(po1);
			}
		}
		if(recesel2!=null){
			for(int k=0;k<recesel2.length;k++){
				TtAsWrAuthCheckAppPO po2 = new TtAsWrAuthCheckAppPO();
				po2.setId(Long.parseLong(SequenceManager.getSequence("")));
				po2.setCheckId(Long.parseLong(checkId));
				po2.setClaimId(Long.parseLong(claimId));
				po2.setCheckSituation(Integer.parseInt(recesel2[k]));
				po2.setCreateBy(logonUser.getUserId().toString());
				po2.setCreateDate(new Date());
				dao.insert(po2);
			}
		}
		if(recesel3!=null){
			for(int l=0;l<recesel3.length;l++){
				TtAsWrAuthCheckAppPO po3 = new TtAsWrAuthCheckAppPO();
				po3.setId(Long.parseLong(SequenceManager.getSequence("")));
				po3.setCheckId(Long.parseLong(checkId));
				po3.setClaimId(Long.parseLong(claimId));
				po3.setCheckSituation(Integer.parseInt(recesel3[l]));
				po3.setCreateBy(logonUser.getUserId().toString());
				po3.setCreateDate(new Date());
				dao.insert(po3);
			}
		}
		if(recesel4!=null){
			for(int v=0;v<recesel4.length;v++){
				TtAsWrAuthCheckAppPO po4 = new TtAsWrAuthCheckAppPO();
				po4.setId(Long.parseLong(SequenceManager.getSequence("")));
				po4.setCheckId(Long.parseLong(checkId));
				po4.setClaimId(Long.parseLong(claimId));
				po4.setCheckSituation(Integer.parseInt(recesel4[v]));
				po4.setCreateBy(logonUser.getUserId().toString());
				po4.setCreateDate(new Date());
				dao.insert(po4);
			}
		}
		
		TtAsWrCheckDetailPO poDetail = new TtAsWrCheckDetailPO();
		poDetail.setClaimId(Long.parseLong(claimId));
		TtAsWrCheckDetailPO poDetailValue = new TtAsWrCheckDetailPO();
		poDetailValue.setStatus(Constant.CHECK_APP_DETAIL_STATUS_2);
		poDetailValue.setUpdateBy(logonUser.getUserId().toString());
		poDetailValue.setUpdateDate(new Date());
		String remark = request.getParamValue("remark");
		poDetailValue.setRemark(remark);
		dao.update(poDetail, poDetailValue);
		act.setOutData("checkId", checkId);
		act.setOutData("flag", "01");
	}
	
	//zhumingwei 2011-11-21
	public void queryDetail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		DealerKpDao dao=DealerKpDao.getInstance();
		try {
			String id = request.getParamValue("id");
			List<Map<String, Object>> list = dao.getCheckApplication(id);
			List<Map<String, Object>> detail = dao.getCheckApplicationDetail(id);
			
			act.setOutData("list", list.get(0));
			act.setOutData("detail", detail);
			
			act.setForword(DEALER_CHECK_APP_DETAIL_INFO_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dealerCheckAppAuthDetailInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		DealerKpDao dao  = new DealerKpDao();
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");//索赔单ID
			String checkId = request.getParamValue("checkId");//批次号ID
			List<Map<String, Object>> PowerOfAttorney = dao.getCodeType("9099");
			act.setOutData("detail", PowerOfAttorney);
			
			List<Map<String, Object>> PowerOfAttorney1 = dao.getCodeType1("9100");
			act.setOutData("detail1", PowerOfAttorney1);
			
			List<Map<String, Object>> PowerOfAttorney2 = dao.getCodeType2("9101");
			act.setOutData("detail2", PowerOfAttorney2);
			
			List<Map<String, Object>> PowerOfAttorney3 = dao.getCodeType3("9102");
			act.setOutData("detail3", PowerOfAttorney3);
			
			List<Map<String, Object>> PowerOfAttorney4 = dao.getCodeType4("9103");
			act.setOutData("detail4", PowerOfAttorney4);
			
			List<Map<String, Object>> claimSit = dao.getCheckSit(id);
			
			//取得备注字段
			TtAsWrCheckDetailPO po = new TtAsWrCheckDetailPO();
			po.setClaimId(Long.parseLong(id));
			TtAsWrCheckDetailPO poValue = (TtAsWrCheckDetailPO)dao.select(po).get(0);
			String rem = poValue.getRemark();
			
			act.setOutData("ID", id);
			act.setOutData("checkId", checkId);
			act.setOutData("claimSit", claimSit);
			act.setOutData("rem", rem);
			act.setForword(DETAIL_INFO_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-12-19
	public void queryStatus(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request=act.getRequest();
		try{
			long banalceId=Long.parseLong(request.getParamValue("balanceId"));
			TtAsWrClaimBalancePO balancePo=new TtAsWrClaimBalancePO();
			balancePo.setId(banalceId);
			balancePo=(TtAsWrClaimBalancePO)factory.select(balancePo).get(0);
			TtAsWrCheckApplicationPO po = new TtAsWrCheckApplicationPO();
			po.setBalanceNo(balancePo.getBalanceNo());
			List list = factory.select(po);
			if(list.size()>0){
				po=(TtAsWrCheckApplicationPO)list.get(0);
				if(po.getIsFrint()==1){
					act.setOutData("msg", "ok");
				}else{
					act.setOutData("msg", "nook");
				}
			}else{
				act.setOutData("msg", "ok1");
			}
		}catch(Exception e){
			e.printStackTrace();
			act.setException(e);
		}
	}
	
	//zhumingwei 2011-12-28
	public void checkApplicationDetailInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(DEALER_CHECK_APPLICATION_DEL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-12-28
	public void dealerCheckAppDetailQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		Map<String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			
			String CHECK_NO = request.getParamValue("CHECK_NO");
			String BALANCE_NO = request.getParamValue("BALANCE_NO");
			String yieldly = request.getParamValue("yieldly");
			String checkDateBegin = request.getParamValue("checkDateBegin");
			String checkDateEnd = request.getParamValue("checkDateEnd");
			String authDateBegin = request.getParamValue("authDateBegin");
			String authDateEnd = request.getParamValue("authDateEnd");
			String STATUS = request.getParamValue("STATUS");
			
			String upfile = request.getParamValue("upfile");
			String sh_status = request.getParamValue("sh_status");
			
			map.put("dealerId", dealerId);
			map.put("CHECK_NO", CHECK_NO);
			map.put("BALANCE_NO", BALANCE_NO);
			map.put("yieldly", yieldly);
			map.put("checkDateBegin", checkDateBegin);
			map.put("checkDateEnd", checkDateEnd);
			map.put("authDateBegin", authDateBegin);
			map.put("authDateEnd", authDateEnd);
			map.put("STATUS", STATUS);
			
			map.put("upfile", upfile);
			map.put("sh_status", sh_status);
			
			DealerKpDao dao  = new DealerKpDao();
			PageResult<Map<String, Object>> ps = dao.queryDealerAppDetail(map, 10, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-12-29
	public void checkAppAuthDetailInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String duty = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			if (Constant.DUTY_TYPE_DEPT.toString().equals(duty)) {
				orgId = logonUser.getParentOrgId();
			}
			act.setOutData("orgId", orgId);
			act.setForword(DEALER_CHECK_APP_DEL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-12-28
	public void dealerCheckAppDetailQueryOme() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Map<String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			
			String dealerId = request.getParamValue("dealerId");
			String orgId = request.getParamValue("orgId");
			String CHECK_NO = request.getParamValue("CHECK_NO");
			String STATUS = request.getParamValue("STATUS");
			String BALANCE_NO = request.getParamValue("BALANCE_NO");
			String yieldly = request.getParamValue("yieldly");
			String checkDateBegin = request.getParamValue("checkDateBegin");
			String checkDateEnd = request.getParamValue("checkDateEnd");
			String authDateBegin = request.getParamValue("authDateBegin");
			String authDateEnd = request.getParamValue("authDateEnd");
			String province = request.getParamValue("province");
			
			String upfile = request.getParamValue("upfile");
			String sh_status = request.getParamValue("sh_status");
			
			map.put("dealerId", dealerId);
			map.put("orgId", orgId);
			map.put("CHECK_NO", CHECK_NO);
			map.put("STATUS", STATUS);
			map.put("BALANCE_NO", BALANCE_NO);
			map.put("yieldly", yieldly);
			map.put("checkDateBegin", checkDateBegin);
			map.put("checkDateEnd", checkDateEnd);
			map.put("authDateBegin", authDateBegin);
			map.put("authDateEnd", authDateEnd);
			map.put("province", province);
			map.put("upfile", upfile);
			map.put("sh_status", sh_status);
			
			DealerKpDao dao  = new DealerKpDao();
			PageResult<Map<String, Object>> ps = dao.queryDealerAppDetailOme(map, 10, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2012-5-21
	public void upfileDetail() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request=act.getRequest();
		try{
			long id=Long.parseLong(request.getParamValue("id"));
			DealerKpDao dao  = new DealerKpDao();
			List<Map<String, Object>> detail = dao.getDetail(id);
			act.setOutData("detail", detail);
			act.setForword(CLAIM_DETAIL);
		}catch(Exception e){
			e.printStackTrace();
			act.setException(e);
		}
	}
	
	//zhumingwei 2012-5-21
	public void addDealerKp(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request=act.getRequest();
		act.setForword("/jsp/claim/authorization/claimUpFile.jsp");
	}
	
	//zhumingwei 2012-5-21
	public void saveUpFile(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper req = act.getRequest();
			//附件功能
			String ywzj = req.getParamValue("ID").toString();
			String checkId = req.getParamValue("checkId");
			String ok = req.getParamValue("ok");
			
			TtAsWrCheckDetailPO po = new TtAsWrCheckDetailPO();
			po.setId(Long.parseLong(ywzj));
			TtAsWrCheckDetailPO poValue = new TtAsWrCheckDetailPO();
			poValue.setUpfile_status(Constant.upfile_status2);
			poValue.setUpdateBy(logonUser.getUserId().toString());
			poValue.setUpdateDate(new Date());
			DealerKpDao dao  = new DealerKpDao();
			dao.update(po, poValue);
			
			String[] fjids=null;
			if("1".equals(ok)){
				fjids = req.getParamValues("fjid");
			}else{
				fjids = req.getParamValues("fjids");
			}
			FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			act.setOutData("checkId",checkId);
		    act.setOutData("success","true");
		} catch (Exception e) {
			e.printStackTrace();
			act.setOutData("success","false");
		}
	}
	
	//zhumingwei 2012-5-21
	public void claimUpFile(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper req = act.getRequest();
			ClaimBillMaintainDAO cDao = new ClaimBillMaintainDAO();
			String ID = req.getParamValue("ID");
			String checkId = req.getParamValue("checkId");
			act.setOutData("checkId",checkId);
		    act.setOutData("ID",ID);
		    
		    FsFileuploadPO po = new FsFileuploadPO();
		    po.setYwzj(Long.parseLong(ID));
		    List list = cDao.select(po);
		    if(list.size()>0){
		    	act.setOutData("ok", "1");
		    }else{
		    	act.setOutData("ok", "2");
		    }
		    //取得附件
			List<FsFileuploadPO> attachLs = cDao.queryAttById(ID);
			act.setOutData("attachLs", attachLs);
		    
		    act.setForword("/jsp/claim/authorization/claimUpFile.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//zhumingwei 2012-5-21
	public void check(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper req = act.getRequest();
			ClaimBillMaintainDAO cDao = new ClaimBillMaintainDAO();
			
			String ID = req.getParamValue("ID");
			TtAsWrCheckApplicationPO po = new TtAsWrCheckApplicationPO();
			po.setId(Long.parseLong(ID));
			TtAsWrCheckApplicationPO poValue = (TtAsWrCheckApplicationPO)cDao.select(po).get(0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time = sdf.format(poValue.getCreateDate());//取得抽查單時間
			String now = sdf.format(new Date());//取得當前時間
			//把抽查單時間拿出來加7天
			Calendar calendar = Calendar.getInstance();//公用类，加年月日的
			calendar.setTime(sdf.parse(time));
			calendar.add(Calendar.DAY_OF_MONTH, 20);
			String date  = sdf.format(calendar.getTime());//得到加一天后的值
			//跟當前時間比較如果小于當前時間證明可以上傳附件
			if(sdf.parse(date).before(sdf.parse(now))){
				act.setOutData("ok", "no");
			}else{
				act.setOutData("ok", "ok");
			}
			act.setOutData("ID", ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//zhumingwei 2012-5-21
	public void sb(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper req = act.getRequest();
			ClaimBillMaintainDAO cDao = new ClaimBillMaintainDAO();
			
			String ID = req.getParamValue("ID");
			TtAsWrCheckApplicationPO po = new TtAsWrCheckApplicationPO();
			po.setId(Long.parseLong(ID));
			TtAsWrCheckApplicationPO poValue = new TtAsWrCheckApplicationPO();
			poValue.setSb_status(Constant.SB_STATUS_2);
			poValue.setUpdateBy(logonUser.getUserId().toString());
			poValue.setUpdateDate(new Date());
			cDao.update(po, poValue);
			act.setOutData("ok", "ok");	
		} catch (Exception e) {
			e.printStackTrace();
			act.setOutData("ok", "no");
		}
	}
	
	//zhumingwei 20120529
	public void ztAudit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			DealerKpDao dao  = new DealerKpDao();
			Map<String,String> map = new HashMap<String,String>();
			
			String id = request.getParamValue("id");
			map.put("id", id);
			List<Map<String, Object>> ps = dao.ztAudit(map);
			act.setOutData("ps", ps);
			
			//审核条数记录
			String count = request.getParamValue("count")==null?"0":request.getParamValue("count");
			
			//分页方法 end
			if(ps!=null){
				if(ps.size()>Integer.valueOf(count)){
					Map<String, Object> specialMap = ps.get(Integer.valueOf(count));//向前台传的list名称是固定的不可改必须用 ps
					String claimId = specialMap.get("CLAIM_ID").toString();
					List<Map<String, Object>> list = dao.getClaim(claimId);
					act.setOutData("list", list.get(0));
					//向下一条传查询条件
					act.setOutData("id", id);
					act.setOutData("count", count);
					
					List<Map<String, Object>> PowerOfAttorney = dao.getCodeType("9099");
					act.setOutData("detail", PowerOfAttorney);
					
					List<Map<String, Object>> PowerOfAttorney1 = dao.getCodeType1("9100");
					act.setOutData("detail1", PowerOfAttorney1);
					
					List<Map<String, Object>> PowerOfAttorney2 = dao.getCodeType2("9101");
					act.setOutData("detail2", PowerOfAttorney2);
					
					List<Map<String, Object>> PowerOfAttorney3 = dao.getCodeType3("9102");
					act.setOutData("detail3", PowerOfAttorney3);
					
					List<Map<String, Object>> PowerOfAttorney4 = dao.getCodeType4("9103");
					act.setOutData("detail4", PowerOfAttorney4);
					
					List<Map<String, Object>> claimSit = dao.getCheckSit(claimId);
					
					//取得备注字段
					TtAsWrCheckDetailPO po = new TtAsWrCheckDetailPO();
					po.setClaimId(Long.parseLong(claimId));
					TtAsWrCheckDetailPO poValue = (TtAsWrCheckDetailPO)dao.select(po).get(0);
					String rem = poValue.getRemark();
					
					act.setOutData("claimId", claimId);
					act.setOutData("claimSit", claimSit);
					act.setOutData("rem", rem);
					
					List<FsFileuploadPO> fileList= dao.queryAttachFileInfo(specialMap.get("ID").toString());
					act.setOutData("fileList", fileList);
					
					act.setForword(auditByOne);
				}else{
					act.setForword(DEALER_CHECK_APP_AUTH_URL);
				}
			}else{
				act.setForword(DEALER_CHECK_APP_AUTH_URL);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询所有开票的经销商
	 */
	public void delaerKpAllQuery(){
		try {
			DealerKpDao dao  = new DealerKpDao();
			PageResult<Map<String,Object>> list=dao.delaerKpAllQuery(request,loginUser.getDealerId(),Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps",list);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"开票选择所有");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * zyw 2014-8-25
	 * 检测经销商的索赔单金额和实际结算金额是否符合，进行提示是否开票
	 */
	public void dealerCheckMoney(){
		DealerKpDao dao  = new DealerKpDao();
		Double money= dao.dealerCheckMoney(request);
		if(money==0.0){
			act.setOutData("res", 1);
		}else{
			act.setOutData("res", 2);
			act.setOutData("money", money);
		}
		List<Map<String, Object>> findExceptionPartRepair = dao.findExceptionPartRepair(request);
		if(DaoFactory.checkListNull(findExceptionPartRepair)){
			act.setOutData("check", -1);
		}else{
			act.setOutData("check", 1);
		}
	}
	/**
	 * 记录开票数据
	 */
	@SuppressWarnings("unchecked")
	public void dealerKpQueryRecord(){
		int res=1;
		try {
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			List<TtAsWrClaimBalancePO> list=balanceDao.openclaimbalanceBystatus(getCurrDealerId());
			StringBuffer sb=new StringBuffer();
			String kpnum="";
			if(list.size()>1){
				for (TtAsWrClaimBalancePO ttAsWrClaimBalancePO : list) {
					sb.append(ttAsWrClaimBalancePO.getRemark()+";");
				}
				kpnum=sb.toString();
			}
			DealerKpDao dao  = new DealerKpDao();
			String sql="insert into tt_AS_dealer_click_record values("+DaoFactory.getPkId()+",'"+getCurrDealerId()+"','"+loginUser.getName()+"','"+kpnum+"',sysdate)";
			dao.insert(sql);
			String sql1="update TT_AS_WR_CLAIM_BALANCE  t set t.ocstatus=1 where t.dealer_id="+getCurrDealerId();
			dao.update(sql1, null);
			String sqltemp="update TT_AS_WR_CLAIM_BALANCE_TMP  t set t.ocstatus=1 where t.dealer_id="+getCurrDealerId();
			dao.update(sqltemp, null);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}finally{
			super.setJsonSuccByres(res);
		}
	}
	/**
	 * 服务站点击提示开票的记录  页面跳转
	 */
	public void dealerKpRecord(){
		super.sendMsgByUrl(sendUrl(DealerNewKp.class, "dealerKpRecord"), "服务站点击提示开票的记录");
	}
	/**
	 * 服务站点击提示开票的记录
	 * 数据的查询
	 */
	public void dealerKpRecordData(){
		DealerKpDao dao  = new DealerKpDao();
		PageResult<Map<String, Object>> list=dao.dealerKpRecordData(request,getCurrPage(),Constant.PAGE_SIZE);
		act.setOutData("ps", list);
	}
	/**
	 * 开票的明细数据
	 */
	public void queryFinancialInfoView01() {
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
				mapCLAIM.put("OTHERACCOUNT", otheraccountTemp);//不加倍
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
			List<TtAsPaymentPO> list = dao.findBalance(REMARK);
			
			TtInvoiceTaxratePO invoice=new TtInvoiceTaxratePO();
			invoice.setStatus(Long.parseLong("10011001"));
			
			List<TtInvoiceTaxratePO> voices=dao.select(invoice);
			act.setOutData("voices",voices);
			
			String invoiceValue="";
			String invoiceName="";
			double amountOfMoneySum=0;
			double taxRateMoneySum=0;
			double inovoiceTaxrateBfb = 0 ;
			double amountSumSum=0;
			TtInvoiceTaxratePO invoicePO=new TtInvoiceTaxratePO();
			TtInvoiceTaxratePO invoicePO1=new TtInvoiceTaxratePO();
			
			TmDealerPO dealerPO=new TmDealerPO();
			
			long inovoiceTaxrate=0;
			//获取结算单税率ID
			Object invoiceValueObj= mapCLAIM.get("INVOICE_ID");
			if(invoiceValueObj!=null){
				
				invoiceValue=invoiceValueObj.toString();
				dealerPO.setDealerId(Long.parseLong(invoiceValue));
				if(dao.select(dealerPO).size()>0)
				{
						dealerPO=(TmDealerPO) dao.select(dealerPO).get(0);
						if(dealerPO.getTaxDisrate() != null && dealerPO.getTaxDisrate().equals("100%"))
						{
							inovoiceTaxrate = 100;
							inovoiceTaxrateBfb = 1.00;
						}else if(dealerPO.getTaxDisrate() != null  && dealerPO.getTaxDisrate().equals("88%"))
						{
							inovoiceTaxrate = 88;
							inovoiceTaxrateBfb = 0.88;
						}else if(dealerPO.getTaxDisrate() != null  && dealerPO.getTaxDisrate().equals("83%"))
						{
							inovoiceTaxrate = 83;
							inovoiceTaxrateBfb = 0.83;
						}
						invoiceName=dealerPO.getTaxpayerNature();

				}else 
				{
					invoiceValue=invoiceValueObj.toString();
					invoicePO.setId(Long.parseLong(invoiceValue));
					if(dao.select(invoicePO).size()>0){
						invoicePO1=(TtInvoiceTaxratePO) dao.select(invoicePO).get(0);
						inovoiceTaxrate=invoicePO1.getTaxRate();
						invoiceName=invoicePO1.getInvoiceName()+"  "+invoicePO1.getTaxRate()+"%";
						inovoiceTaxrateBfb = invoicePO1.getTaxRate()/100;
					}
				}
				
				
				
				
			}
			Object selectmentAmountObj= mapCLAIM.get("SELECTMENT_AMOUNT");
			String selectmentAmount="";
			if(selectmentAmountObj!=null){
				selectmentAmount=selectmentAmountObj.toString();
			}else{
				selectmentAmount=mapCLAIM.get("AMOUNT_SUM").toString();
			}
			
			
			
			
			
			double labourAccount = Arith.sub(Double.parseDouble(mapCLAIM.get(
					"LABOUR_ACCOUT").toString()), Double.parseDouble(mapApp
					.get("CLAIM_TYPE_AMOUNT_FREE_02").toString()));
		//	int nature = 0;
			
			int STATUS = 0;
			Date date = new Date();
			String sum = "";
			//结算表保存的税率
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					
					//amountOfMoneySum=Arith.add(amountOfMoneySum,list.get(i).getAmountOfMoney());
					//taxRateMoneySum=Arith.add(taxRateMoneySum,list.get(i).getTaxRateMoney());
					
//					invoiceValue=list.get(0).getInvoice();
//					if(invoiceValue!=null){
//						invoicePO.setId(Long.parseLong(invoiceValue));
//						if(dao.select(invoicePO).size()>0){
//							invoicePO1=(TtInvoiceTaxratePO) dao.select(invoicePO).get(0);
//							inovoiceTaxrate=invoicePO1.getTaxRate();
//							invoiceName=invoicePO1.getInvoiceName()+"  "+invoicePO1.getTaxRate()+"%";
//						}
//						
//					}
					
					//sum=sum+list.get(i).getAmountSum();
				}
				DecimalFormat    df   = new DecimalFormat("######0.00");   
				sum=df.format((Double.parseDouble(mapCLAIM.get("AMOUNT_SUM").toString()))*inovoiceTaxrate/100);
				amountSumSum=Arith.add(amountOfMoneySum, taxRateMoneySum) ;
				act.setOutData("amountSumSum", amountSumSum);
				act.setOutData("amountOfMoneySum", amountOfMoneySum);
				act.setOutData("taxRateMoneySum", taxRateMoneySum);
				act.setOutData("invoiceName", invoiceName);
				act.setOutData("invoiceValue",invoiceValue);
				act.setOutData("list",list);
				
				act.setOutData("size",list.size());
				act.setOutData("inovoiceTaxrateBfb",inovoiceTaxrateBfb);
				
				
				act.setOutData("dealer_id",dealer_id);
				act.setOutData("startDate",startDate);
				act.setOutData("endDate",endDate);
				
			//paymentPO = list.get(0);
			//STATUS = paymentPO.getStatus();

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
			//act.setOutData("date", sdf.format(date));
			//act.setOutData("STATUS", STATUS);
		//	act.setOutData("nature", nature);
			act.setOutData("mapdel", mapdel);
			act.setOutData("mapApp", mapApp);
			act.setOutData("mapCLAIM", mapCLAIM);
			act.setOutData("labourAccount", labourAccount);
			act.setOutData("sum",selectmentAmount);
			act.setOutData("dealerId", loginUser.getDealerId());
			
			String res = invoiceService.checkticeksByBalanceNo(request);
			if("".equals(res)){
				act.setOutData("res", 1);
			}else{
				act.setOutData("res", -1);
			}
			act.setForword(finViewUrl01);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/*
	 * 结算单明细 财务室
	 */
	public void queryFinancialInfoView() {

		try {
			DealerKpDao dao = new DealerKpDao();
			
			String dealer_id = request.getParamValue("id");
			String startDate = request.getParamValue("START_DATE");
			String endDate = request.getParamValue("END_DATE");

			act.setOutData("dealer_id",dealer_id);
			act.setOutData("startDate",startDate);
			act.setOutData("endDate",endDate);
			
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
				mapCLAIM.put("OTHERACCOUNT", otheraccountTemp);//不要加补偿了
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
			
			
			
			double labourAccount = Arith.sub(Double.parseDouble(mapCLAIM.get(
					"LABOUR_ACCOUT").toString()), Double.parseDouble(mapApp
					.get("CLAIM_TYPE_AMOUNT_FREE_02").toString()));
		//	int nature = 0;
			Integer STATUS = 0;
			Date date = new Date();
			double sum = 0;
			double amountOfMoneySum=0;
			double taxRateMoneySum=0;
			double amountSumSum=0;
			
			String invoiceValue="";
			String invoiceName="";
			TtInvoiceTaxratePO invoicePO=new TtInvoiceTaxratePO();
			TtInvoiceTaxratePO invoicePO1=new TtInvoiceTaxratePO();
			
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
			
			
			
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					amountOfMoneySum=Arith.add(amountOfMoneySum,list.get(i).getAmountOfMoney());
					taxRateMoneySum=Arith.add(taxRateMoneySum,list.get(i).getTaxRateMoney());
//					if(sum==0.0){
//						sum=list.get(0).getAmountOfMoney()+list.get(0).getTaxRateMoney();
//						list.get(0).setAmountSum(sum);
//					}
				}
				amountSumSum=Arith.add(amountOfMoneySum, taxRateMoneySum)  ;
				act.setOutData("amountSumSum", amountSumSum);
				act.setOutData("amountOfMoneySum", amountOfMoneySum);
				act.setOutData("taxRateMoneySum", taxRateMoneySum);
				
				
				act.setOutData("invoiceName", invoiceName);
				act.setOutData("invoiceValue",invoiceValue);
				act.setOutData("list",list);
				
				
				paymentPO = list.get(0);
				STATUS = paymentPO.getStatus();

				date = paymentPO.getCreatDate();

			} else {
				STATUS=null;
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
			act.setOutData("sum",selectmentAmount);
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
	/*
	 * 复核申请打印
	 */
	public void queryPrintInfo() {

		try {

			// CommonUtils.checkNull() 校验是否为空
			String id = request.getParamValue("id");
			DealerKpDao dao  = new DealerKpDao();
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
	public void queryFinancialInfoView07() {
		try {
			String flag = request.getParamValue("flag");
			String banlanceNo = request.getParamValue("BALANCE_NO");
			auditBean bean = new auditBean();
			bean.setBalanceNo(banlanceNo);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryDealerBalanceList02(bean,curPage,Constant.PAGE_SIZE);
			act.setOutData("ps",ps.getRecords().get(0));
			//查询发票记录
			List<Map<String, Object>> listBill=dao.seBill(banlanceNo);
			if(listBill.size()>0){
				act.setOutData("listBill",listBill);
			}
			//查询美中索赔单个数,并拼接索赔id
			List<Map<String, Object>> listclaimNum=dao.claimNum(banlanceNo);	
			int by_num=0;
			String by_ids="";
			int fw_num=0;
			String fw_ids="";			
			int pdi_num=0;
			String pdi_ids="";
			int z_num=0;
			String z_ids="";
			int hj_num=0;
			if(listclaimNum.size()>0){
				for(int i=0;i<listclaimNum.size();i++){
					if("11441004".equals(listclaimNum.get(i).get("REPAIR_TYPE").toString())){//保养
						by_num=by_num+1;
						by_ids=by_ids+listclaimNum.get(i).get("ID")+',';
					}else if("11441005".equals(listclaimNum.get(i).get("REPAIR_TYPE").toString())){//服务活动	
						fw_num=fw_num+1;
						fw_ids=fw_ids+listclaimNum.get(i).get("ID")+',';
					}else if("11441008".equals(listclaimNum.get(i).get("REPAIR_TYPE").toString())){//PDI
						pdi_num=pdi_num+1;
						pdi_ids=pdi_ids+listclaimNum.get(i).get("ID")+',';
					}else{//所有维修台次
						z_num=z_num+1;
						z_ids=z_ids+listclaimNum.get(i).get("ID")+',';
					}
				}
				act.setOutData("hj_num",listclaimNum.size());//合计台次
			}			
			act.setOutData("by_num",by_num);//保养台次
			if(!"".equals(by_ids)){
				by_ids=by_ids.substring(0,by_ids.length()-1);
			}
			act.setOutData("by_ids",by_ids);
			act.setOutData("fw_num",fw_num);
			if(!"".equals(fw_ids)){
				fw_ids=fw_ids.substring(0,fw_ids.length()-1);
			}
			act.setOutData("fw_ids",fw_ids);
			act.setOutData("pdi_num",pdi_num);
			if(!"".equals(pdi_ids)){
				pdi_ids=pdi_ids.substring(0,pdi_ids.length()-1);
			}
			act.setOutData("pdi_ids",pdi_ids);
			act.setOutData("z_num",z_num);
			if(!"".equals(z_ids)){
				z_ids=z_ids.substring(0,z_ids.length()-1);
			}
			act.setOutData("z_ids",z_ids);
			if("se".equals(flag)){
				act.setForword(finViewUrl01);//查看
			}else if("add".equals(flag)){
				act.setForword(finAdd);//编辑
			}else if("sp".equals(flag)){
				act.setForword(finsp);//收票
			}else if("print".equals(flag)){
				act.setForword(Appprint_ALL);//打印
			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void addBalanceBill() {
		try {
			String banlanceNo = request.getParamValue("balanecNo");
			String[] id = DaoFactory.getParams(request, "id");
			String[] PC_NO = DaoFactory.getParams(request, "PC_NO");
			String[] BILL_NO = DaoFactory.getParams(request, "BILL_NO");
			String[] MONEY = DaoFactory.getParams(request, "MONEY");
			String[] TAX_MONEY = DaoFactory.getParams(request, "TAX_MONEY");
			String[] TOTAL = DaoFactory.getParams(request, "TOTAL");
			String[] REMARK = DaoFactory.getParams(request, "REMARK");
			if(id.length<=0){//新增				
				for(int i=0;i<BILL_NO.length-1;i++){
					TtAsWrBalanceBillPO bill=new TtAsWrBalanceBillPO();
					bill.setId(Long.parseLong(SequenceManager.getSequence("")));
					bill.setBalanceNo(banlanceNo);
					bill.setPcNo(PC_NO[i]);
					bill.setBillNo(BILL_NO[i]);
					bill.setMoney(Float.parseFloat(MONEY[i]));
					bill.setTaxMoney(Float.parseFloat(TAX_MONEY[i]));
					bill.setTotal(Float.parseFloat(TOTAL[i]));
					bill.setRemark(REMARK[i]);
					bill.setCreateBy(loginUser.getUserId());
					bill.setCreateDate(new Date());
					dao.insert(bill);
				}				
			}else{//修改				
				//先删除
				TtAsWrBalanceBillPO deBill=new TtAsWrBalanceBillPO();
				deBill.setBalanceNo(banlanceNo);
    	        dao.delete(deBill);
    	        for(int i=0;i<BILL_NO.length-1;i++){
    	        	TtAsWrBalanceBillPO bill=new TtAsWrBalanceBillPO();
					bill.setId(Long.parseLong(SequenceManager.getSequence("")));
					bill.setBalanceNo(banlanceNo);
					bill.setPcNo(PC_NO[i]);
					bill.setBillNo(BILL_NO[i]);
					bill.setMoney(Float.parseFloat(MONEY[i]));
					bill.setTaxMoney(Float.parseFloat(TAX_MONEY[i]));
					bill.setTotal(Float.parseFloat(TOTAL[i]));
					bill.setRemark(REMARK[i]);
					bill.setCreateBy(loginUser.getUserId());
					bill.setCreateDate(new Date());
					dao.insert(bill);
				}				
			}
			
			act.setOutData("msg","0");				
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算发票录入");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void reportBalanceBill() {
		try {
			String id = request.getParamValue("id");
			if(!"".equals(id)){//上报			
				TtAsWrClaimBalancePO balance=new TtAsWrClaimBalancePO();
				balance.setId(Long.parseLong(id));
				TtAsWrClaimBalancePO seBalance=(TtAsWrClaimBalancePO) dao.select(balance).get(0);
				if(!seBalance.getStatus().equals(Constant.CLAIM_STAUTS_01) && !seBalance.getStatus().equals(Constant.CLAIM_STAUTS_04) ){
					act.setOutData("msg","1");
				}else{
					TtAsWrClaimBalancePO upBalance=new TtAsWrClaimBalancePO();
					upBalance.setStatus(Constant.CLAIM_STAUTS_02);
					dao.update(balance, upBalance);
					act.setOutData("msg","0");
				}
			}							
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算上报");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void spBalanceBill() {
		try {
			String balanecNo = request.getParamValue("balanecNo");
			String commint = request.getParamValue("commint");
			String auditRemark = request.getParamValue("auditRemark");
			if("0".equals(commint)){	
				TtAsWrClaimBalancePO balance=new TtAsWrClaimBalancePO();
				balance.setBalanceNo(balanecNo);
				TtAsWrClaimBalancePO seBalance=(TtAsWrClaimBalancePO) dao.select(balance).get(0);
				if(!seBalance.getStatus().equals(Constant.CLAIM_STAUTS_02)){
					act.setOutData("msg","1");
				}else{
					TtAsWrClaimBalancePO upBalance=new TtAsWrClaimBalancePO();
					upBalance.setStatus(Constant.CLAIM_STAUTS_03);
					upBalance.setReviewApplicationBy(loginUser.getUserId().toString());
					upBalance.setReviewApplicationTime(new Date());
					upBalance.setFunancialRemark(auditRemark);
					dao.update(balance, upBalance);
					act.setOutData("msg","0");
				}
			}else if("1".equals(commint)){	
				TtAsWrClaimBalancePO balance=new TtAsWrClaimBalancePO();
				balance.setBalanceNo(balanecNo);
				TtAsWrClaimBalancePO seBalance=(TtAsWrClaimBalancePO) dao.select(balance).get(0);
				if(!seBalance.getStatus().equals(Constant.CLAIM_STAUTS_02)){
					act.setOutData("msg","1");
				}else{
					TtAsWrClaimBalancePO upBalance=new TtAsWrClaimBalancePO();
					upBalance.setStatus(Constant.CLAIM_STAUTS_04);
					upBalance.setFunancialRemark(auditRemark);
					dao.update(balance, upBalance);
					act.setOutData("msg","0");
				}
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算收票");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void AppprintRk(){
		String BALANCE_ODER= request.getParamValue("BALANCE_ODER");
		String DEALER_ID= request.getParamValue("DEALER_ID");
		TmDealerPO dealerPO = new TmDealerPO();
		dealerPO.setDealerId(Long.parseLong(DEALER_ID));
		
		
		DealerKpDao dealerkpdao = new DealerKpDao();
		List<TmDealerPO>  listtm= dealerkpdao.select(dealerPO);
		
		dealerPO = listtm.get(0);
		List<Map<String, Object>> list = dealerkpdao.AppprintRk(BALANCE_ODER);
		if(list.size() > 0 )
		{
			String CLAIM_NO = list.get(0).get("CLAIM_NO").toString();
			StringBuffer sql= new StringBuffer();
			sql.append("select  to_char(b.sign_date,'yyyy-mm-dd') sign_date,c.NAME,\n" );
			sql.append("   to_char(b.in_warhouse_date,'yyyy-mm-dd') in_warhouse_date,\n" );
			sql.append("   b.return_no\n" );
			sql.append("   from Tt_As_Wr_Old_Returned_Detail a , Tt_As_Wr_Old_Returned b,tc_user c\n" );
			sql.append("where  c.USER_ID = b.SIGN_PERSON and  a.return_id = b.id and a.claim_no = '"+CLAIM_NO+"'");
			act.setOutData("map", dealerkpdao.pageQueryMap(sql.toString(), null, dealerkpdao.getFunName()));

			
		}
		act.setOutData("date", Utility.handleDateAc(new Date()));
		act.setOutData("BALANCE_ODER", BALANCE_ODER);
		act.setOutData("list", list);
		act.setOutData("dealerPO", dealerPO);
		act.setForword(AppprintRk);
	}
	
	public void DealerUnitKpXiao()
	{
		String CHECK_TICKETS_DATE_S= request.getParamValue("CHECK_TICKETS_DATE_S");
		String CHECK_TICKETS_DATE= request.getParamValue("CHECK_TICKETS_DATE");
		
		StringBuffer sql = new StringBuffer();
		sql.append("\n" );
		sql.append("select\n" );
		sql.append("sum(( case when a.claim_type=10661002 then\n" );
		sql.append("           0\n" );
		sql.append("         when a.claim_type = 10661011 then\n" );
		sql.append("           0\n" );
		sql.append("         else\n" );
		sql.append("          nvl(a.balance_part_amount, 0) end)\n" );
		sql.append("      + nvl(a.accessories_price, 0)\n" );
		sql.append("     ) balance_part_amount,");
		sql.append("sum(\n" );
		sql.append("case when a.claim_type = 10661002\n" );
		sql.append(" then nvl(a.balance_amount,0)\n" );
		sql.append("when a.claim_type = 10661011\n" );
		sql.append(" then nvl(a.balance_amount,0)\n" );
		sql.append(" else 0 end ) balance_amount\n" );
		sql.append("from tt_as_wr_application a , tt_as_wr_claim_balance b,(select max(t.TRANSFER_TICKETS_DATE) TRANSFER_TICKETS_DATE ,balance_oder  from tt_as_payment t group by t.balance_oder) C\n" );
		sql.append("where C.balance_oder = B.remark  and  a.status != 10791005 and a.status != 10791006 and a.balance_no = b.remark\n" );
		sql.append("and  c.TRANSFER_TICKETS_DATE >= to_date('"+CHECK_TICKETS_DATE_S+"','yyyy-mm-dd')\n" );
		sql.append("and c.TRANSFER_TICKETS_DATE < to_date('"+CHECK_TICKETS_DATE+"','yyyy-mm-dd')+1");
		DealerKpDao balanceDao = DealerKpDao.getInstance();
		Map<String, Object> aMap = balanceDao.pageQueryMap(sql.toString(), null, balanceDao.getFunName());
		String BALANCE_PART_AMOUNT_01 = "0";
		if(aMap.get("BALANCE_PART_AMOUNT")!=null &&  aMap.get("BALANCE_PART_AMOUNT").toString().length() >  0  ){
			BALANCE_PART_AMOUNT_01 = aMap.get("BALANCE_PART_AMOUNT").toString();//材料费（除pdi和保养的）
		}
		BigDecimal BALANCE_PART_AMOUNT = new BigDecimal(BALANCE_PART_AMOUNT_01);//材料费（除pdi和保养的）
		
		
		String BRO_NO= request.getParamValue("BRO_NO");
		
		
		String INVOICE_NO= request.getParamValue("INVOICE_NO");
		String INVOICE_BATCH_NO= request.getParamValue("INVOICE_BATCH_NO");
		
		DealerKpDao dealerkpdao = new DealerKpDao();
		String bf = "";
		double sl = 0.00;
		double cs = 0.00;
		bf = "17%";
		sl = 1.17;
		cs = 1;

		double jcfy  = 0.00;
		double jcsl = 0.00;
		List<Map<String, Object>> list1 = dealerkpdao.DealerUnitKpXiao(CHECK_TICKETS_DATE_S,CHECK_TICKETS_DATE, sl,cs);
		// 计算配件价差
		BigDecimal partsAmount = BigDecimal.ZERO;
		BigDecimal taxAmount = BigDecimal.ZERO;
		if (!CommonUtils.isNullList(list1)) {
			BigDecimal tmpjc = BigDecimal.ZERO;
			for (Map<String, Object> map : list1) {
				// 配件金额
				Object amtObj = map.get("BALANCE_AMOUNT");
				// 配件税额
				Object samtObj = map.get("S_BALANCE_AMOUNT");
				// 税率
				BigDecimal taxRate = new BigDecimal(Constant.PART_TAX_RATE);
				BigDecimal amt = new BigDecimal(null==amtObj?"0":amtObj.toString());
				BigDecimal samt = new BigDecimal(null==samtObj?"0":samtObj.toString());
//				tmpjc = tmpjc.add(amt.multiply(taxRate).subtract(samt));
				
				partsAmount = partsAmount.add(amt);
				taxAmount = taxAmount.add(samt);
			}
//			tmpjc = tmpjc.setScale(2, RoundingMode.HALF_UP);
//			jcsl = tmpjc.doubleValue();
			BigDecimal slb = new BigDecimal(sl);
			slb = slb.setScale(2, RoundingMode.HALF_UP);
			BigDecimal tpartAmount = BALANCE_PART_AMOUNT.divide(slb,2,RoundingMode.HALF_UP);
			jcfy = tpartAmount.subtract(partsAmount).setScale(2).doubleValue();
			jcsl = BALANCE_PART_AMOUNT.subtract(tpartAmount).subtract(taxAmount).setScale(2).doubleValue();
		}
		Map<String, Object> map = dealerkpdao.DealerUnitKpXiaoZ(CHECK_TICKETS_DATE_S,CHECK_TICKETS_DATE, sl,cs);
		act.setOutData("INVOICE_NO", INVOICE_NO);
		act.setOutData("INVOICE_BATCH_NO", INVOICE_BATCH_NO);
		act.setOutData("jcfy", jcfy);
		act.setOutData("jcsl", jcsl);
		act.setOutData("bf", bf);
		act.setOutData("list", list1);
		act.setOutData("map", map);
		act.setOutData("date", Utility.handleDateAc(new Date()));
		act.setOutData("BRO_NO", BRO_NO);
		act.setForword(DealerUnitKpXiao);
		
	}
	
	
	public void AppprintXiao()
	{
		String BALANCE_ODER= request.getParamValue("BALANCE_ODER");
		String dealer_id= request.getParamValue("dealer_id");
		DealerKpDao dealerkpdao = new DealerKpDao();
		
		TmDealerPO dealerPO = new TmDealerPO();
		dealerPO.setDealerId(Long.parseLong(dealer_id));
		List<TmDealerPO>  listDealer = dealerkpdao.select(dealerPO);
		act.setOutData("DealerName", listDealer.get(0).getDealerName());
		String bf = "";
		double sl = 0.00;
		double cs = 0.00;
		if(listDealer.size() > 0 )
		{
			if(listDealer.get(0).getTaxDisrate().equals("100%"))
			{
				bf = "17%";
				sl = 1.17;
				cs = 1;
			}else if(listDealer.get(0).getTaxDisrate().equals("88%"))
			{
				bf = "3%";
				sl = 1.03;
				cs = 0.88;
			}else {
				bf = "0%";
				sl = 1.00;
				cs = 0.83;
			}
		}
		
		
		
		
		TtAsPaymentPO asPaymentPO = new TtAsPaymentPO();
		asPaymentPO.setBalanceOder(BALANCE_ODER);
		asPaymentPO.setProject("材料");
		List<TtAsPaymentPO> list= dealerkpdao.select(asPaymentPO);
		String ph = "";
		String hm = "";
		
		if(list.size() > 0 )
		{
			for(TtAsPaymentPO paymentPO : list)
			{
				if(paymentPO.getLabourReceipt() != null && paymentPO.getLabourReceipt().length() > 0 )
				{
					ph = ph + "  "+ paymentPO.getLabourReceipt();
					hm = hm + "  "+ paymentPO.getPartReceipt();
				}
			}
		}
		
		StringBuffer sql= new StringBuffer();
		sql.append("select sum(t.amount_of_money) amount_of_money,sum(t.tax_rate_money)  tax_rate_money from tt_as_payment t where t.balance_oder = '"+BALANCE_ODER+"' and t.PROJECT = '材料'\n" );
		sql.append("group by t.balance_oder");
		Map<String, Object>  jcMap = dealerkpdao.pageQueryMap(sql.toString(), null, dealerkpdao.getFunName());

		
		List<Map<String, Object>> list1 = dealerkpdao.AppprintXiao(BALANCE_ODER, sl,cs);
		
		Map<String, Object> map = dealerkpdao.AppprintXiaoZ(BALANCE_ODER, sl,cs);
		double jcfy  = 0.00;
		double jcsl = 0.00;
		if(jcMap != null  && jcMap.get("AMOUNT_OF_MONEY") != null && jcMap.get("AMOUNT_OF_MONEY").toString().length() != 0)
		{
			jcfy= Arith.sub( Double.parseDouble(jcMap.get("AMOUNT_OF_MONEY").toString())  ,Double.parseDouble(map.get("BALANCE_AMOUNT").toString()) );
			jcsl = Arith.sub(  Double.parseDouble(jcMap.get("TAX_RATE_MONEY").toString()) , Double.parseDouble(map.get("S_BALANCE_AMOUNT").toString()));
		}
		
		act.setOutData("jcfy", jcfy);
		act.setOutData("jcsl", jcsl);
		act.setOutData("jcMap", jcMap);
		act.setOutData("bf", bf);
		act.setOutData("list", list1);
		act.setOutData("ph", ph);
		act.setOutData("map", map);
		act.setOutData("hm", hm);
		
		act.setOutData("date", Utility.handleDateAc(new Date()));
		act.setForword(AppprintXiao);
		
		

	}
	//索赔单一建打印
	public void AppprintAll() {

		act.setOutData("dealerId", loginUser.getDealerId());		
		act.setForword(Appprint_ALL);
	}
	public void Appprint() {//索赔单详情
		try {
			String ids = CommonUtils.checkNull(request.getParamValue("ids"));
			String flat = CommonUtils.checkNull(request.getParamValue("flat"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			if("t".equals(flat)){
				String APP_CLAIM_NO = CommonUtils.checkNull(request.getParamValue("APP_CLAIM_NO"));
				PageResult<Map<String, Object>> ps = dao.claimSe(ids,APP_CLAIM_NO,curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);	
			}					
			act.setForword(Appprint);
			act.setOutData("ids", ids);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	private final String deleteBalanceInfoView = "/jsp/claim/authorization/deleteBalanceInfoView.jsp";
	/**
	 * 开票的明细数据
	 */
	public void deleteFinancialInfoViewPage() {

		try {
			String dealerId=loginUser.getDealerId();
			
			
			DealerKpDao dao = new DealerKpDao();

			String dealer_id = request.getParamValue("id");
			String startDate = request.getParamValue("START_DATE");
			String endDate = request.getParamValue("END_DATE");
			Map<String, Object> mapdel = dao.queryDealerMes(dealer_id);
			Map<String, Object> mapCLAIM = dao.queryCLAIM(dealer_id, startDate,endDate);
			System.out.println(mapCLAIM.get("AMOUNT_SUM")+"测试++++++++++++++++++++++++");
			String REMARK = mapCLAIM.get("REMARK").toString();
			//==========zyw 把二次入库补偿服务站的钱加到结算总金额里 2014-9-10
			Double compensationDealerMoney=0d;
			DealerKpDao dealerkpdao = new DealerKpDao();
			List<Map<String, Object>> listMap=dealerkpdao.findcompensationBydealer(dealer_id,REMARK);
			if(listMap!=null&&listMap.size()>0){
				Map<String, Object> map = listMap.get(0);
				BigDecimal  bigdecimal= (BigDecimal) map.get("AMOUNT");
				//compensationDealerMoney=bigdecimal.doubleValue();
				BigDecimal  otheraccount=(BigDecimal) mapCLAIM.get("OTHERACCOUNT");
				Double otheraccountTemp = otheraccount.doubleValue();
				mapCLAIM.put("OTHERACCOUNT", otheraccountTemp);//下面有单独加二次入库的钱，这里无需多加数据 zyw 2015-10-21 
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
			
			
			
			double labourAccount = Arith.sub(Double.parseDouble(mapCLAIM.get(
					"LABOUR_ACCOUT").toString()), Double.parseDouble(mapApp
					.get("CLAIM_TYPE_AMOUNT_FREE_02").toString()));
		//	int nature = 0;
			String invoiceValue="";
			String invoiceName="";
			TtInvoiceTaxratePO invoicePO=new TtInvoiceTaxratePO();
			TtInvoiceTaxratePO invoicePO1=new TtInvoiceTaxratePO();
			int STATUS = 0;
			Date date = new Date();
			
			double amountOfMoneySum=0;
			double taxRateMoneySum=0;
			double amountSumSum=0;
			
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
			}else{
				selectmentAmount=mapCLAIM.get("AMOUNT_SUM").toString();
			}
			
			
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					amountOfMoneySum=Arith.add(amountOfMoneySum,list.get(i).getAmountOfMoney());
					taxRateMoneySum=Arith.add(taxRateMoneySum,list.get(i).getTaxRateMoney());
					if(invoiceValue!=null){
						invoicePO.setId(Long.parseLong(invoiceValue));
						invoicePO1=(TtInvoiceTaxratePO) dao.select(invoicePO).get(0);
						invoiceName=invoicePO1.getInvoiceName()+"  "+invoicePO1.getTaxRate()+"%";
					}
					
				}
				amountSumSum=Arith.add(amountOfMoneySum, taxRateMoneySum) ;
				act.setOutData("amountSumSum", amountSumSum);
				act.setOutData("amountOfMoneySum", amountOfMoneySum);
				act.setOutData("taxRateMoneySum", taxRateMoneySum);
				act.setOutData("invoiceName", invoiceName);
				act.setOutData("invoiceValue",invoiceValue);
				act.setOutData("list",list);
				
				act.setOutData("size",list.size());
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
			act.setOutData("dealerId", dealerId);
			act.setOutData("date", sdf.format(date));
			act.setOutData("STATUS", STATUS);
		//	act.setOutData("nature", nature);
			act.setOutData("mapdel", mapdel);
			
			act.setOutData("mapApp", mapApp);
			act.setOutData("mapCLAIM", mapCLAIM);
			act.setForword(finViewUrl01);
			//校验车厂经销商
			act.setOutData("dealer_id",dealer_id);
			act.setOutData("startDate",startDate);
			act.setOutData("endDate",endDate);
			act.setOutData("date", sdf.format(date));
			act.setOutData("STATUS", STATUS);
		//	act.setOutData("nature", nature);
			act.setOutData("labourAccount", labourAccount);
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
	public void appprintSencondByNo(){
			InvoiceService invoiceService = new InvoiceServiceImpl();
			String balance_oder = getParam("BALANCE_ODER");
			List<Map<String, Object>> list = invoiceService.invoiceInfoSecondTimeByNo(balance_oder);
			act.setOutData("list", list);
			sendMsgByUrl("/jsp/claim/authorization/appprintSencondByNo.jsp", "二次入库明细");
	}
	
	
	
}