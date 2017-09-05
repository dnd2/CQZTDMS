package com.infodms.dms.actions.claim.laborlist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.application.BalanceStatusRecord;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.authorization.BalanceMainDao;
import com.infodms.dms.dao.claim.laborList.LaborListDao;
import com.infodms.dms.dao.claim.laborList.LaborListSummaryReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrStopauthPO;
import com.infodms.dms.po.TtLaborListDetailPO;
import com.infodms.dms.po.TtLaborListPO;
import com.infodms.dms.po.TtTaxableServiceSumPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 劳务清单汇总报表参数设置
 * @author Administrator
 *
 */
public class LaborListAction {
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper req = act.getRequest();
	private Logger logger = Logger.getLogger(LaborListAction.class);
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(
			Constant.LOGON_USER);
	
	private final String INIT_URL = "/jsp/claim/laborlist/laborList.jsp" ;
	private final String ADD_URL = "/jsp/claim/laborlist/laborListAdd.jsp" ;
	private final String OPEN_WIN_URL = "/dialog/showNotice.jsp" ;
	private final String DETAIL_URL = "/jsp/claim/laborlist/laborListDetail.jsp" ;
	private final String DELETE_URL_INIT = "/jsp/claim/laborlist/deleteInit.jsp" ;//汇总报表参数设置删除主页面
	private final String DELETE_URL = "/jsp/claim/laborlist/deleteDo.jsp" ;//汇总报表参数设置删除页面
	private final String REPORT_URL_INIT = "/jsp/claim/laborlist/reportInit.jsp" ;//厂端汇总报表查询打印主页面
	private final String STOP_URL = "/jsp/claim/laborlist/stopDo.jsp" ;
	private final String DETAIL_URL22 = "/jsp/claim/laborlist/laborListDetail22.jsp" ;
	private final String STOP_QX_URL = "/jsp/claim/laborlist/stopDoQx.jsp" ;
	
	//zhumingwei 2011-04-18
	private final String INIT_URL11 = "/jsp/claim/laborlist/laborList11.jsp" ;
	private final String ADD_URL11 = "/jsp/claim/laborlist/laborListAdd11.jsp" ;
	private final String DETAIL_URL11 = "/jsp/claim/laborlist/laborListDetail11.jsp" ;
	private final String DETAIL_URLBulu = "/jsp/claim/laborlist/laborListDetailBulu.jsp" ;
	
	private static LaborListDao dao = LaborListDao.getInstance() ;
	
	/*
	 * 劳务清单维护主页面初始化
	 */
	public void firstUrlInit(){
		try{
			act.setOutData("yieldly", req.getParamValue("yieldly"));
			act.setForword(INIT_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单维护");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	
	public void firstUrlInit1(){
		try{
			act.setOutData("yieldly", req.getParamValue("yieldly"));
			act.setForword(INIT_URL11);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单维护");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	
	//zhumingwei 2011-04-18
	public void firstUrlInit11(){
		try{
			act.setOutData("yieldly", req.getParamValue("yieldly"));
			act.setForword(INIT_URL11);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单维护");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	
	/*
	 * 劳务清单维护新增页面初始化
	 */
	public void addUrlInit(){
		try{
			TmDealerPO dpo = new TmDealerPO();
			dpo.setDealerId(Long.parseLong(logonUser.getDealerId()));
			dpo = dao.getDealer(dpo) ;
			
			act.setOutData("reportCode", SequenceManager.getSequence("LWQD"));
			act.setOutData("date", new Date());
			act.setOutData("userName", logonUser.getName());
			act.setOutData("dealer", dpo);
			
			act.setForword(ADD_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单维护");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	//zhumingwei 2011-04-18
	public void addUrlInit11(){
		try{
			TmDealerPO dpo = new TmDealerPO();
			dpo.setDealerId(Long.parseLong(logonUser.getDealerId()));
			dpo = dao.getDealer(dpo) ;
			
			act.setOutData("reportCode", SequenceManager.getSequence("LWQD"));
			act.setOutData("date", new Date());
			act.setOutData("userName", logonUser.getName());
			act.setOutData("dealer", dpo);
			
			act.setForword(ADD_URL11);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单维护");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	
	/*
	 * 劳务清单 弹出框页面初始化
	 */
	public void openWinUrlInit(){
		try{
			act.setOutData("code", req.getParamValue("code"));
			act.setForword(OPEN_WIN_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单维护");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	
	/*
	 * 劳务清单 单据编码 生成规则
	 * LWQD+YYYYMMDD+0000
	 */
	public synchronized String codeGen(){
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		String dStr = fmt.format(new Date());
		StringBuffer sb = new StringBuffer("LWQD");
		sb.append(dStr);
		String last ;
		String sql = "select * from tt_labor_list where report_code like 'LWQD"+dStr+"%' order by report_code desc" ;
		List<TtLaborListPO> list = dao.select(TtLaborListPO.class,sql,null);
		if(list.size()>0){
			last = String.valueOf((Integer.parseInt(list.get(0).getReportCode().substring(12,16))+1));
			if(last.length()==1)
				last = "000"+last;
			if(last.length()==2)
				last = "00"+last;
			if(last.length()==3)
				last = "0"+last;
		}
		else last = "0001" ;
		sb.append(last);
		return sb.toString() ;
	}
	
	/*
	 * 弹出框方法
	 * 开票通知单查询
	 */
	public void queryNotice(){
		try{
			act.getResponse().setContentType("application/json");
			String yieldly = req.getParamValue("yieldly");
			String code = req.getParamValue("code");
			String balance_no = req.getParamValue("balance_no");
			
			StringBuffer sql = new StringBuffer("\n");
			sql.append("select b.balance_no,id,d.dealer_code,d.dealer_name,\n");
			sql.append("b.dealer_id,b.note_amount\n");
			sql.append("from tt_as_wr_claim_balance b,tm_dealer d\n");
			sql.append("where b.kp_dealer_id ="+logonUser.getDealerId()+"\n");
			
//			sql.append("(select t.dealer_id\n");
//			sql.append("from tm_dealer t where t.dealer_type="+Constant.DEALER_TYPE_DWR+"\n");
//			sql.append("start with t.dealer_id = "+logonUser.getDealerId()+"\n");
//			sql.append("connect by prior  t.dealer_id =t.parent_dealer_d)\n");
			
			sql.append("and b.yieldly="+yieldly+"\n");
			sql.append("and b.status="+Constant.ACC_STATUS_04+"\n");
			sql.append("and not exists  (select x.balance_no from tt_labor_list_detail x where  x.balance_no=b.balance_no)\n");
			sql.append("and b.note_amount>0\n");
			if(code.length()>4)
				sql.append("and b.balance_no not in ").append(code.replace(",","','")).append("\n");
			sql.append("and b.dealer_id=d.dealer_id\n");
			if(StringUtil.notNull(balance_no))
				sql.append("and b.balance_no like '%"+balance_no+"%'\n");
			//sql.append("and b.dealer_id=").append(logonUser.getDealerId()).append("\n");
			sql.append("order by b.balance_no desc\n");
			
			int pageSize = 10 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String,Object>> ps = dao.getNotice(sql.toString(), pageSize, curPage);
			
			act.setOutData("ps",ps);			
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "开票通知单查询");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	
	/*
	 * 劳务清单主页面第一次查询
	 */
	public void mainQuery(){
		try{
			act.getResponse().setContentType("application/json");
			String reportCode = req.getParamValue("reportCode");
			String dealerName = req.getParamValue("dealerName");
			String yieldly = req.getParamValue("yieldly");
			String beginDate = req.getParamValue("beginDate");
			String endDate = req.getParamValue("endDate");
			String status = req.getParamValue("status");
			
			StringBuffer sql = new StringBuffer("\n");
			sql.append("select s.report_code,s.report_id,s.yieldly,to_char(c.auth_time,'YYYY-MM-DD') auth_time,\n");
			sql.append("c.auth_person_name,s.invoice_code,s.amount,s.status,\n");
			sql.append("d.dealer_code,d.dealer_name\n");
			sql.append("from tt_labor_list s,tm_dealer d,\n");
			sql.append("(select sa.auth_person_name ,sa.auth_time,sa.balance_id\n" );
			sql.append("       from tt_taxable_sum_authitem sa\n" );
			sql.append("      where sa.auth_status = 11881001\n" );
			sql.append("      ) c\n");

			sql.append(" where s.dealer_id=d.dealer_id\n");
			sql.append(" and c.balance_id(+)=s.report_id \n");
			if(StringUtil.notNull(reportCode))
				sql.append("and s.report_code like '%"+reportCode+"%'\n");
			if(StringUtil.notNull(dealerName))
				sql.append("and d.dealer_name like '%"+dealerName+"%'\n");
			if(StringUtil.notNull(yieldly))
				sql.append("and s.yieldly = "+yieldly+"\n");
			if(StringUtil.notNull(beginDate))
				sql.append("and s.report_date >= to_date('"+beginDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss')\n");
			if(StringUtil.notNull(endDate))
				sql.append("and s.report_dste <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss')\n");
			if(StringUtil.notNull(status))
				sql.append("and s.status = "+status+"\n");
			sql.append("  and s.dealer_id = ").append(logonUser.getDealerId()).append("\n");
			sql.append("order by s.report_code desc\n");
			
			int pageSize = 10 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String,Object>> ps = dao.getLaborList(sql.toString(), pageSize, curPage);
			act.setOutData("ps",ps);
			
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单查询");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	//zhumingwei  2011-04-18
	public void mainQuery11(){
		try{
			act.getResponse().setContentType("application/json");
			String reportCode = req.getParamValue("reportCode");
			String dealerName = req.getParamValue("dealerName");
			String yieldly = req.getParamValue("yieldly");
			String beginDate = req.getParamValue("beginDate");
			String endDate = req.getParamValue("endDate");
			String status = req.getParamValue("status");
			
			StringBuffer sql = new StringBuffer("\n");
			sql.append("select s.report_amount,s.report_code,s.report_id,s.yieldly,s.receive_date,\n");
			sql.append("s.receive_man,s.invoice_code,s.amount,s.status,\n");
			sql.append("d.dealer_code,d.dealer_name\n");
			sql.append("from tt_labor_list s,tm_dealer d\n");
			sql.append("where s.dealer_id=d.dealer_id\n");
			if(StringUtil.notNull(reportCode))
				sql.append("and s.report_code like '%"+reportCode+"%'\n");
			if(StringUtil.notNull(dealerName))
				sql.append("and d.dealer_name like '%"+dealerName+"%'\n");
			if(StringUtil.notNull(yieldly))
				sql.append("and s.yieldly = "+yieldly+"\n");
			if(StringUtil.notNull(beginDate))
				sql.append("and s.report_date >= to_date('"+beginDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss')\n");
			if(StringUtil.notNull(endDate))
				sql.append("and s.report_dste <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss')\n");
			if(StringUtil.notNull(status))
				sql.append("and s.status = "+status+"\n");
			sql.append("  and s.dealer_id = ").append(logonUser.getDealerId()).append("\n");
			sql.append("order by s.report_code desc\n");
			
			int pageSize = 10 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String,Object>> ps = dao.getLaborList(sql.toString(), pageSize, curPage);
			act.setOutData("ps",ps);
			
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单查询");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	
	/*
	 * 劳务清单 针对新增 保存方法
	 */
	public void save(){
		try{
			String reportCode = req.getParamValue("reportCode");
			String dateStr = req.getParamValue("date");
			String amountStr = req.getParamValue("amount");
			String[] dealerId = req.getParamValues("dealer_id");
			String[] dealerCode = req.getParamValues("dealer_code");
			String[] dealerName = req.getParamValues("dealer_name");
			String yieldly = req.getParamValue("yieldly");
			String invoiceCode = req.getParamValue("incoiceCode");
			String[] balanceNo = req.getParamValues("notice");
			String[] balanceId = req.getParamValues("balanceId");
			String[] money = req.getParamValues("_money");
			
			Double amount = 0d ;
			if(StringUtil.notNull(amountStr))
				amount = Double.parseDouble(amountStr);
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = new Date();
			if(StringUtil.notNull(dateStr))
				d = fmt.parse(dateStr);
			
			// 劳务清单主表插入
			TtLaborListPO lpo = new TtLaborListPO();
			lpo.setAmount(amount);
			lpo.setCreateBy(logonUser.getUserId());
			lpo.setCreateDate(new Date());
			lpo.setDealerId(Long.parseLong(logonUser.getDealerId()));
			lpo.setInvoiceCode(invoiceCode);
			// 这里还不能达到此清单号唯一
			//lpo.setReportCode(reportCode);
			lpo.setReportCode(reportCode);
			lpo.setReportDate(d);
			lpo.setMakeMan(logonUser.getName());
			lpo.setYieldly(Integer.parseInt(yieldly));
			lpo.setReportId(Utility.getLong(SequenceManager.getSequence("")));
			lpo.setStatus(Constant.LABOR_LIST_STATUS_SAVE);
			dao.insert(lpo);
			
			// 劳务清单明细表插入
			TtLaborListDetailPO dpo = null ;
			for(int i=0;i<dealerId.length;i++){
				dpo = new TtLaborListDetailPO();
				dpo.setBalanceNo(balanceNo[i]);
				dpo.setBalanceId(Long.parseLong(balanceId[i]));
				dpo.setCreateBy(logonUser.getUserId());
				dpo.setCreateDate(new Date());
				dpo.setDealerCode(dealerCode[i]);
				dpo.setDealerId(Long.parseLong(dealerId[i]));
				dpo.setDealerName(dealerName[i]);
				dpo.setInvoiceAmount(Double.parseDouble(money[i]));
				dpo.setReportId(lpo.getReportId());
				dpo.setDetailId(Utility.getLong(SequenceManager.getSequence("")));
				
				/*addUser xiongchuan 2011-10-26********防止一张开票单被生成多次参数设置***********/
				List<Map<String,Object>>  list = dao.balanceNoOnly(balanceNo[i]);
				if(list.size() <1) dao.insert(dpo) ;
				if(list.size()>0) throw new Exception("未知错误请退出系统重新登录");
				BalanceStatusRecord.recordStatus(Long.parseLong(balanceId[i]), logonUser.getUserId(), logonUser.getName(), logonUser.getOrgId(), BalanceStatusRecord.STATUS_08);
				TtAsWrClaimBalancePO po = new TtAsWrClaimBalancePO();
				TtAsWrClaimBalancePO tp = new TtAsWrClaimBalancePO();
				po.setId(Long.parseLong(balanceId[i]));
				tp.setStatus(Integer.parseInt(Constant.ACC_STATUS_05));
				tp.setUpdateBy(logonUser.getUserId());
				tp.setUpdateDate(new Date());
				BalanceMainDao dao = new BalanceMainDao();
				dao.update(po, tp);
			}
			
			act.setRedirect(INIT_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单新增");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	
	//zhumingwei 2011-04-18
	public void save11(){
		try{
			String reportCode = req.getParamValue("reportCode");
			String dateStr = req.getParamValue("date");
			String amountStr = req.getParamValue("amount");
			String[] dealerId = req.getParamValues("dealer_id");
			String[] dealerCode = req.getParamValues("dealer_code");
			String[] dealerName = req.getParamValues("dealer_name");
			String yieldly = req.getParamValue("yieldly");
			String invoiceCode = req.getParamValue("incoiceCode");
			String[] balanceNo = req.getParamValues("notice");
			String[] balanceId = req.getParamValues("balanceId");
			String[] money = req.getParamValues("_money");
			
			String report_amount = req.getParamValue("report_amount");
			
			Double amount = 0d ;
			if(StringUtil.notNull(amountStr))
				amount = Double.parseDouble(amountStr);
			
			Double report_amount1 = 0d ;
			if(StringUtil.notNull(report_amount))
				report_amount1 = Double.parseDouble(report_amount);
			
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = new Date();
			if(StringUtil.notNull(dateStr))
				d = fmt.parse(dateStr);
			
			// 劳务清单主表插入
			TtLaborListPO lpo = new TtLaborListPO();
			lpo.setAmount(amount);
			
			lpo.setReportAmount(report_amount1);
			lpo.setAuthAmount(report_amount1);
			
			lpo.setCreateBy(logonUser.getUserId());
			lpo.setCreateDate(new Date());
			lpo.setDealerId(Long.parseLong(logonUser.getDealerId()));
			lpo.setInvoiceCode(invoiceCode);
			// 这里还不能达到此清单号唯一
			//lpo.setReportCode(reportCode);
			lpo.setReportCode(reportCode);
			lpo.setReportDate(d);
			lpo.setMakeMan(logonUser.getName());
			lpo.setYieldly(Integer.parseInt(yieldly));
			lpo.setReportId(Utility.getLong(SequenceManager.getSequence("")));
			lpo.setStatus(Constant.LABOR_LIST_STATUS_SAVE);
			dao.insert(lpo);
			
			// 劳务清单明细表插入
			TtLaborListDetailPO dpo = null ;
			for(int i=0;i<dealerId.length;i++){
				dpo = new TtLaborListDetailPO();
				dpo.setBalanceNo(balanceNo[i]);
				dpo.setBalanceId(Long.parseLong(balanceId[i]));
				dpo.setCreateBy(logonUser.getUserId());
				dpo.setCreateDate(new Date());
				dpo.setDealerCode(dealerCode[i]);
				dpo.setDealerId(Long.parseLong(dealerId[i]));
				dpo.setDealerName(dealerName[i]);
				dpo.setInvoiceAmount(Double.parseDouble(money[i]));
				dpo.setReportId(lpo.getReportId());
				dpo.setDetailId(Utility.getLong(SequenceManager.getSequence("")));
				/*addUser xiongchuan 2011-10-26********防止一张开票单被生成多次参数设置***********/
				List<Map<String,Object>>  list = dao.balanceNoOnly(balanceNo[i]);
				if(list.size() <1) dao.insert(dpo) ;
				if(list.size()>0) throw new Exception("未知错误请退出系统重新登录");
				/**add xiongchuan 2011-7-18 ***生成印税清单就默认经销商确认***/
				TtAsWrClaimBalancePO po = new TtAsWrClaimBalancePO();
				TtAsWrClaimBalancePO tp = new TtAsWrClaimBalancePO();
				po.setId(Long.parseLong(balanceId[i]));
				tp.setStatus(Integer.parseInt(Constant.ACC_STATUS_05));
				tp.setUpdateBy(logonUser.getUserId());
				tp.setUpdateDate(new Date());
				BalanceMainDao dao = new BalanceMainDao();
				dao.update(po, tp);
				
				//记录日志表 -- 添加一个经销商确认时间
				BalanceStatusRecord.recordStatus(Long.parseLong(balanceId[i]), logonUser.getUserId(), logonUser.getName(), logonUser.getOrgId(), BalanceStatusRecord.STATUS_08);
			}
			

			
			act.setRedirect(INIT_URL11);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单新增");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	
	public void saveBulu(){
		String reportId = req.getParamValue("reportId");
		String Invoice = req.getParamValue("Invoice");
		String InvoiceAmount = req.getParamValue("InvoiceAmount");
		TtLaborListPO po = new TtLaborListPO();
		TtLaborListPO po1 = new TtLaborListPO();
		po.setReportId(Long.valueOf(reportId));
		po1.setAuthAmount(Double.parseDouble(InvoiceAmount));
		po1.setInvoiceCode(Invoice);
		dao.update(po, po1);
		TtTaxableServiceSumPO sumPo = new TtTaxableServiceSumPO();
		TtTaxableServiceSumPO sumPo1 = new TtTaxableServiceSumPO();
		sumPo.setSumParameterId(Long.valueOf(reportId));
		sumPo1.setInvoiceNo(Invoice);
		dao.update(sumPo, sumPo1);
		act.setForword(INIT_URL11);
		
	}
	/*
	 * 劳务清单明细查询
	 */
	public void showDetail(){
		try{
			String id = req.getParamValue("id");
			
			//劳务清单主表查询
			TtLaborListPO lpo = new TtLaborListPO();
			lpo.setReportId(Long.parseLong(id));
			lpo = (TtLaborListPO)dao.select(lpo).get(0);
			
			//劳务清单经销商查询(制单单位查询)
			TmDealerPO dpo = new TmDealerPO();
			dpo.setDealerId(lpo.getDealerId());
			List<TmDealerPO> lists = dao.select(dpo);
			if(lists.size()>0)
				dpo = lists.get(0);
			
			//劳务清单明细查询
			int pageSize = 15 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<TtLaborListDetailPO> ps = dao.getLaborListDetail(id, pageSize, curPage);

			act.setOutData("labor", lpo);
			act.setOutData("dealer", dpo);
			act.setOutData("ps", ps);
			
			act.setForword(DETAIL_URL);
		} catch (Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单新增");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	
	//zhumingwei 2011-04-19
	public void showDetail11(){
		try{
			String id = req.getParamValue("id");
			
			//劳务清单主表查询
			TtLaborListPO lpo = new TtLaborListPO();
			lpo.setReportId(Long.parseLong(id));
			lpo = (TtLaborListPO)dao.select(lpo).get(0);
			
			//劳务清单经销商查询(制单单位查询)
			TmDealerPO dpo = new TmDealerPO();
			dpo.setDealerId(lpo.getDealerId());
			List<TmDealerPO> lists = dao.select(dpo);
			if(lists.size()>0)
				dpo = lists.get(0);
			
			//劳务清单明细查询
			int pageSize = 100 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<TtLaborListDetailPO> ps = dao.getLaborListDetail(id, pageSize, curPage);

			act.setOutData("labor", lpo);
			act.setOutData("dealer", dpo);
			act.setOutData("ps", ps);
			
			List<Map<String, Object>> detail = dao.getStopAuth(Long.parseLong(id));
			act.setOutData("detail", detail);
			
			act.setForword(DETAIL_URL11);
		} catch (Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单新增");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	public void showDetail22(){
		try{
			String id = req.getParamValue("id");
			
			//劳务清单主表查询
			TtLaborListPO lpo = new TtLaborListPO();
			lpo.setReportId(Long.parseLong(id));
			lpo = (TtLaborListPO)dao.select(lpo).get(0);
			
			//劳务清单经销商查询(制单单位查询)
			TmDealerPO dpo = new TmDealerPO();
			dpo.setDealerId(lpo.getDealerId());
			List<TmDealerPO> lists = dao.select(dpo);
			if(lists.size()>0)
				dpo = lists.get(0);
			
			//劳务清单明细查询
			int pageSize = 100 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<TtLaborListDetailPO> ps = dao.getLaborListDetail(id, pageSize, curPage);

			act.setOutData("labor", lpo);
			act.setOutData("dealer", dpo);
			act.setOutData("ps", ps);
			
			List<Map<String, Object>> detail = dao.getStopAuth(Long.parseLong(id));
			act.setOutData("detail", detail);
			
			act.setForword(DETAIL_URL22);
		} catch (Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单新增");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	public void showDetailBulu(){
		try{
			String id = req.getParamValue("id");
			
			//劳务清单主表查询
			TtLaborListPO lpo = new TtLaborListPO();
			lpo.setReportId(Long.parseLong(id));
			lpo = (TtLaborListPO)dao.select(lpo).get(0);
			
			//劳务清单经销商查询(制单单位查询)
			TmDealerPO dpo = new TmDealerPO();
			dpo.setDealerId(lpo.getDealerId());
			List<TmDealerPO> lists = dao.select(dpo);
			if(lists.size()>0)
				dpo = lists.get(0);
			
			//劳务清单明细查询
			int pageSize = 50 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<TtLaborListDetailPO> ps = dao.getLaborListDetail(id, pageSize, curPage);

			act.setOutData("labor", lpo);
			act.setOutData("dealer", dpo);
			act.setOutData("ps", ps);
			
			act.setForword(DETAIL_URLBulu);
		} catch (Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单新增");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	/*
	 * 汇总报表参数设置删除
	 */
	public void deleteUrlInit(){
		act = ActionContext.getContext() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER) ;
		try{
			act.setForword(DELETE_URL_INIT);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "汇总报表删除主页面");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	public void deleteQuery(){
		try{
			act.getResponse().setContentType("application/json");
			String reportCode = req.getParamValue("reportCode");
			String dealerName = req.getParamValue("dealerName");
			String yieldly = req.getParamValue("yieldly");
			String beginDate = req.getParamValue("beginDate");
			String endDate = req.getParamValue("endDate");
			String status = req.getParamValue("status");
			
			StringBuffer sql = new StringBuffer("\n");
			sql.append("select s.report_code,s.report_id,s.yieldly,ddd.auth_person_name receive_man,\n");
			sql.append("ddd.auth_time receive_date,s.invoice_code,s.amount,s.status,\n");
			sql.append("d.dealer_code,d.dealer_name\n");
			sql.append("from tt_labor_list s,tm_dealer d,tt_taxable_sum_authitem ddd\n");
			sql.append("where s.dealer_id = d.dealer_id and ddd.balance_id(+)=s.report_id and ddd.auth_status(+)=11881001\n");
			if(StringUtil.notNull(reportCode))
				sql.append("and s.report_code like '%"+reportCode+"%'\n");
			if(StringUtil.notNull(dealerName))
				sql.append("and d.dealer_name like '%"+dealerName+"%'\n");
			if(StringUtil.notNull(yieldly))
				sql.append("and s.yieldly = "+yieldly+"\n");
			if(StringUtil.notNull(beginDate))
				sql.append("and s.report_date >= to_date('"+beginDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss')\n");
			if(StringUtil.notNull(endDate))
				sql.append("and s.report_dste <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss')\n");
			if(StringUtil.notNull(status))
				sql.append("and s.status = "+status+"\n");
			sql.append("order by s.report_code desc\n");
			
			int pageSize = 10 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String,Object>> ps = dao.getLaborList(sql.toString(), pageSize, curPage);
			act.setOutData("ps",ps);
			
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单查询");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	public void goDelete(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER) ;
		try{
			String id = req.getParamValue("id");
			
			//劳务清单主表查询
			TtLaborListPO lpo = new TtLaborListPO();
			lpo.setReportId(Long.parseLong(id));
			lpo = (TtLaborListPO)dao.select(lpo).get(0);
			
			//劳务清单经销商查询(制单单位查询)
			TmDealerPO dpo = new TmDealerPO();
			dpo.setDealerId(lpo.getDealerId());
			List<TmDealerPO> lists = dao.select(dpo);
			if(lists.size()>0)
				dpo = lists.get(0);
			
			//劳务清单明细查询
			int pageSize = 15 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<TtLaborListDetailPO> ps = dao.getLaborListDetail(id, pageSize, curPage);

			act.setOutData("labor", lpo);
			act.setOutData("dealer", dpo);
			act.setOutData("ps", ps);
			
			act.setForword(DELETE_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "汇总报表删除跳转");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	public void doDelete(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER) ;
		try{
			String id = req.getParamValue("id");
			StringBuffer uSql= new StringBuffer();
			uSql.append("\n" );
			uSql.append("update Tt_As_Wr_Claim_Balance b set b.status="+Constant.ACC_STATUS_04+" where exists (\n" );
			uSql.append("select 1 from tt_labor_list_detail d where d.report_id="+id+" and d.balance_id = b.id\n" );
			uSql.append(")");
			dao.update(uSql.toString(), null);		
			
			StringBuffer dSql= new StringBuffer();
			dSql.append("delete from Tt_As_Wr_Balance_Authitem da where da.auth_status=11861008 and exists (\n" );
			dSql.append("select 1 from tt_labor_list_detail d where d.report_id="+id+" and d.balance_id = da.balance_id\n" );
			dSql.append(")");
			dao.delete(dSql.toString(),null);
			//删除汇总报表中各费用明细表
			String sql = "delete from tt_taxable_sum_print_tmp where list_id = "+id ;
			dao.delete(sql, null);
			
			//删除汇总报表主表信息
			sql = "delete from tt_taxable_service_sum s where s.sum_parameter_id = "+id ;
			dao.delete(sql, null);
			
			StringBuffer SSql= new StringBuffer();
			SSql.append("delete from tt_taxable_service s where s.balance_id in (\n" );
			SSql.append("select d.balance_id from tt_labor_list_detail  d where d.report_id="+id+"\n" );
			SSql.append(")");
			dao.delete(SSql.toString(),null);
			//删除参数设置明细表信息
			sql = "delete from tt_labor_list_detail d where d.report_id = "+id ;
			dao.delete(sql,null);
			
			
			//删除参数设置主表信息
			sql = "delete from tt_labor_list d where d.report_id = "+id ;
			dao.delete(sql,null);
			
			act.setOutData("flag", true);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "汇总报表删除主页面");
			logger.error(logonUser, be);
			act.setOutData("flag", false);
			act.setException(be);
		}
	}
	
	public void goStop(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER) ;
		try{
			String id = req.getParamValue("id");
			
			//劳务清单主表查询
			TtLaborListPO lpo = new TtLaborListPO();
			lpo.setReportId(Long.parseLong(id));
			lpo = (TtLaborListPO)dao.select(lpo).get(0);
			
			//劳务清单经销商查询(制单单位查询)
			TmDealerPO dpo = new TmDealerPO();
			dpo.setDealerId(lpo.getDealerId());
			List<TmDealerPO> lists = dao.select(dpo);
			if(lists.size()>0)
				dpo = lists.get(0);
			
			//劳务清单明细查询
			int pageSize = 15 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<TtLaborListDetailPO> ps = dao.getLaborListDetail(id, pageSize, curPage);

			act.setOutData("labor", lpo);
			act.setOutData("dealer", dpo);
			act.setOutData("ps", ps);
			
			act.setForword(STOP_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "汇总报表删除跳转");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	
	public void goStopQx(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER) ;
		try{
			String id = req.getParamValue("id");
			
			//劳务清单主表查询
			TtLaborListPO lpo = new TtLaborListPO();
			lpo.setReportId(Long.parseLong(id));
			lpo = (TtLaborListPO)dao.select(lpo).get(0);
			
			//劳务清单经销商查询(制单单位查询)
			TmDealerPO dpo = new TmDealerPO();
			dpo.setDealerId(lpo.getDealerId());
			List<TmDealerPO> lists = dao.select(dpo);
			if(lists.size()>0)
				dpo = lists.get(0);
			
			//劳务清单明细查询
			int pageSize = 15 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<TtLaborListDetailPO> ps = dao.getLaborListDetail(id, pageSize, curPage);

			act.setOutData("labor", lpo);
			act.setOutData("dealer", dpo);
			act.setOutData("ps", ps);
			
			act.setForword(STOP_QX_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "汇总报表删除跳转");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	
	public void doStop(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER) ;
		try{
			String id = req.getParamValue("id");
			StringBuffer uSql= new StringBuffer();
			uSql.append("\n" );
			uSql.append("update tt_labor_list b set b.status="+Constant.LABOR_LIST_STATUS_STOP+" where b.report_id="+id+"\n" );
			dao.update(uSql.toString(), null);
			
			TtAsWrStopauthPO po = new TtAsWrStopauthPO();
			po.setId(Long.parseLong(SequenceManager.getSequence("")));
			po.setReportId(Long.parseLong(id));
			po.setAuthStatus(Constant.LABOR_LIST_STATUS_STOP);
			po.setAuthDate(new Date());
			po.setAuthBy(logonUser.getUserId());
			po.setRemark(req.getParamValue("remark"));
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
			dao.insert(po);
			
			act.setOutData("flag", true);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "汇总报表删除主页面");
			logger.error(logonUser, be);
			act.setOutData("flag", false);
			act.setException(be);
		}
	}
	
	public void doStopQx(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER) ;
		try{
			String id = req.getParamValue("id");
			StringBuffer uSql= new StringBuffer();
			uSql.append("\n" );
			uSql.append("update tt_labor_list b set b.status="+Constant.LABOR_LIST_STATUS_SAVE+" where b.report_id="+id+"\n" );
			dao.update(uSql.toString(), null);
			
			TtAsWrStopauthPO po = new TtAsWrStopauthPO();
			po.setId(Long.parseLong(SequenceManager.getSequence("")));
			po.setReportId(Long.parseLong(id));
			po.setAuthStatus(Constant.LABOR_LIST_STATUS_STOP1);
			po.setAuthDate(new Date());
			po.setAuthBy(logonUser.getUserId());
			po.setRemark(req.getParamValue("remark"));
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
			dao.insert(po);
			
			act.setOutData("flag", true);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "汇总报表删除主页面");
			logger.error(logonUser, be);
			act.setOutData("flag", false);
			act.setException(be);
		}
	}
	
	public void isCheck(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER) ;
		try{
			String id = req.getParamValue("id");
			
			TtTaxableServiceSumPO popo = new TtTaxableServiceSumPO();
			popo.setSumParameterId(Long.parseLong(id));
			TtTaxableServiceSumPO popoValue = (TtTaxableServiceSumPO)dao.select(popo).get(0);
			act.setOutData("authStatus", popoValue.getAuthStatus());
			act.setOutData("id", id);
			
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE, "");
			logger.error(logonUser, be);
			act.setOutData("flag", false);
			act.setException(be);
		}
	}
	
	/*
	 * 厂端查询打印汇总报表主页面
	 */
	public void reportUrlInit(){
		act = ActionContext.getContext() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER) ;
		try{
			act.setForword(REPORT_URL_INIT);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "汇总报表删除主页面");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	/*
	 * 劳务清单汇总报表厂端查询
	 */
	public void oemQuery() {
		act = ActionContext.getContext();
		req = act.getRequest();
		try {
			String reportCode = req.getParamValue("reportCode");
			String code = req.getParamValue("code");
			String name = req.getParamValue("name");
			String tax = req.getParamValue("tax");
			String yieldly = req.getParamValue("yieldly");
			String invoice_no = req.getParamValue("invoinceCode");
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId());       //该用户拥有的产地权限

			StringBuffer con = new StringBuffer();
			con.append("and s.PURCHASER_ID in(").append(yieldlys).append(")\n");
			if (StringUtil.notNull(reportCode))
				con.append(" and s.sum_parameter_no like '%")
						.append(reportCode).append("%'\n");
			if (StringUtil.notNull(code))
				con.append(" and d.dealer_code like '%").append(code).append("%'\n");
			if (StringUtil.notNull(name))
				con.append(" and d.dealer_name like '%").append(name).append("%'\n");
			if (StringUtil.notNull(tax))
				con.append(" and s.tax_rate = ").append(tax).append("\n");
			if (StringUtil.notNull(yieldly))
				con.append(" and s.purchaser_id = ").append(yieldly).append("\n");
			if (StringUtil.notNull(invoice_no))
				con.append(" and s.invoice_no like '%").append(invoice_no).append("%'\n");

			int pageSize = 15;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
					
			LaborListSummaryReportDao dao1 = LaborListSummaryReportDao.getInstance();
			PageResult<Map<String, Object>> ps = dao1.mainQuery(con.toString(),
					pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
}
