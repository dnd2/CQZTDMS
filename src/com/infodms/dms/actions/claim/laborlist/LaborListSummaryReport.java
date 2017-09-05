package com.infodms.dms.actions.claim.laborlist;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.SummaryReportBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.laborList.LaborListSummaryReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmRegionPO;
import com.infodms.dms.po.TtAsWrContractPO;
import com.infodms.dms.po.TtLaborListPO;
import com.infodms.dms.po.TtTaxableServiceSumPO;
import com.infodms.dms.po.TtTaxableSumAuthitemPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 劳务清单汇总报表
 * 
 * @author Administrator
 * 
 */
public class LaborListSummaryReport {
	private ActionContext act = null;
	private RequestWrapper req = null;
	private AclUserBean user = null;
	private LaborListSummaryReportDao dao = LaborListSummaryReportDao
			.getInstance();
	private Logger logger = Logger.getLogger(LaborListSummaryReport.class);
	
	// dlr
	private final String MAIN_URL = "/jsp/claim/laborlist/laborlistSummaryReport.jsp";
	private final String MAIN_URL1 = "/jsp/claim/laborlist/laborlistSummaryReport1.jsp";
	private final String PARAM_SET_URL = "/jsp/claim/laborlist/laborlistParamSetSel.jsp";
	private final String PRINT_URL = "/jsp/claim/laborlist/laborlistSummaryReportPrint.jsp";
	private final String DETAIL_URL = "/jsp/claim/laborlist/laborlistSummaryReportDetail.jsp";
	// oem
	private final String MAIN_URL_OEM = "/jsp/claim/laborlist/summaryReportOem.jsp";
	private final String DETAIL_URL_OEM = "/jsp/claim/laborlist/summaryReportOemDetail.jsp";
	
	//zhumingwei 2011-04-18
	private final String MAIN_URL_OEM11 = "/jsp/claim/laborlist/summaryReportOem11.jsp";
	private final String DETAIL_URL_OEM11 = "/jsp/claim/laborlist/summaryReportOemDetail11.jsp";
	private final String DETAIL_URL_OEM22 = "/jsp/claim/laborlist/summaryReportOemDetail22.jsp";
	
	private final String PRINT_REIM_URL = "/jsp/claim/laborlist/laborlistReimbursementPrint.jsp";
	
	/********************** DLR **********************/

	/*
	 * 劳务清单汇总报表主页面初始化
	 */
	public void mainUrlInit() {
		act = ActionContext.getContext();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List listCode = dao.select(code) ;
			if(listCode.size()>0){
				code = (TcCodePO)listCode.get(0);
			}
			if(Integer.parseInt(code.getCodeId())==Constant.chana_jc){
				act.setForword(MAIN_URL1);
			}else{
				act.setForword(MAIN_URL);
			}
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表");
			logger.error(user, be);
			act.setException(be);
		}
	}

	/*
	 * 劳务清单参数设置弹出框初始化
	 */
	public void queryReportUrlInit() {
		act = ActionContext.getContext();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PARAM_SET_URL);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表");
			logger.error(user, be);
			act.setException(be);
		}
	}

	/*
	 * 劳务清单参数设置查询方法
	 */
	public void paramSetQuery() {
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String code = req.getParamValue("code");
			String dealerCode = req.getParamValue("dealerCode");
			String dealerName = req.getParamValue("dealerName");
			StringBuffer con = new StringBuffer();
			if (StringUtil.notNull(code))
				con.append(" and l.report_code like '%").append(code).append(
						"%'\n");
			if (StringUtil.notNull(dealerCode))
				con.append(" and d.dealer_code like '%").append(dealerCode)
						.append("%'\n");
			if (StringUtil.notNull(dealerName))
				con.append(" and d.dealer_name like '%").append(dealerName)
						.append("%'\n");
			con.append(" and d.dealer_id = ").append(user.getDealerId())
					.append("\n");
			int pageSize = 10;
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getReport(con.toString(),
					pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单参数设置");
			logger.error(user, be);
			act.setException(be);
		}
	}

	/*
	 * 劳务清单汇总报表主页面主查询
	 */
	public void mainQuery() {
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reportCode = req.getParamValue("reportCode");
			String code = req.getParamValue("code");
			String name = req.getParamValue("name");
			String tax = req.getParamValue("tax");
			String yieldly = req.getParamValue("yieldly");
			String invoice_no = req.getParamValue("invoinceCode");
			String status = req.getParamValue("status");
			String yieldlys = CommonUtils.findYieldlyByPoseId(user.getPoseId());       //该用户拥有的产地权限

			StringBuffer con = new StringBuffer();
			con.append("and s.PURCHASER_ID in(").append(yieldlys).append(")\n");
			if (StringUtil.notNull(reportCode))
				con.append(" and s.sum_parameter_no like '%")
						.append(reportCode).append("%'\n");
			if (StringUtil.notNull(code))
				con.append(" and d.dealer_code like '%").append(code).append(
						"%'\n");
			if (StringUtil.notNull(name))
				con.append(" and d.dealer_name like '%").append(name).append(
						"%'\n");
			if (StringUtil.notNull(tax))
				con.append(" and s.tax_rate = ").append(tax).append("\n");
			if (StringUtil.notNull(yieldly))
				con.append(" and s.purchaser_id = ").append(yieldly).append(
						"\n");
			if (StringUtil.notNull(invoice_no))
				con.append(" and s.invoice_no like '%").append(invoice_no)
						.append("%'\n");
			// 区分经销商与车厂端
			if (StringUtil.notNull(user.getDealerId()))
				con.append(" and s.sales_id = ").append(user.getDealerId())
						.append("\n");
			if(StringUtil.notNull(status)){
				//con.append(" and s.auth_status = ").append(status).append("\n");
			}
			
			int pageSize = 15;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.mainQuery(con.toString(),
					pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	//zhumingwei 2011-04-18
	public void mainQuery11() {
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reportCode = req.getParamValue("reportCode");
			String code = req.getParamValue("code");
			String name = req.getParamValue("name");
			String tax = req.getParamValue("tax");
			String yieldly = req.getParamValue("yieldly");
			String invoice_no = req.getParamValue("invoinceCode");
			String status = req.getParamValue("status");
			
			String yieldlys = CommonUtils.findYieldlyByPoseId(user.getPoseId());       //该用户拥有的产地权限

			StringBuffer con = new StringBuffer();
			con.append("and s.PURCHASER_ID in(").append(yieldlys).append(")\n");
			if (StringUtil.notNull(reportCode))
				con.append(" and s.sum_parameter_no like '%")
						.append(reportCode).append("%'\n");
			if (StringUtil.notNull(code))
				con.append(" and d.dealer_code like '%").append(code).append(
						"%'\n");
			if (StringUtil.notNull(name))
				con.append(" and d.dealer_name like '%").append(name).append(
						"%'\n");
			if (StringUtil.notNull(tax))
				con.append(" and s.tax_rate = ").append(tax).append("\n");
			if (StringUtil.notNull(yieldly))
				con.append(" and s.purchaser_id = ").append(yieldly).append(
						"\n");
			if (StringUtil.notNull(invoice_no))
				con.append(" and s.invoice_no like '%").append(invoice_no)
						.append("%'\n");
			// 区分经销商与车厂端
			if (StringUtil.notNull(user.getDealerId()))
				con.append(" and s.sales_id = ").append(user.getDealerId())
						.append("\n");
			if(StringUtil.notNull(status)){
				con.append(" and s.auth_status = ").append(status).append("\n");
			}
			
			int pageSize = 15;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.mainQuery11(con.toString(),
					pageSize, curPage);
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表");
			logger.error(user, be);
			act.setException(be);
		}
	}

	/*
	 * 劳务清单汇总报表打印页面初始化
	 */
	@SuppressWarnings("unchecked")
	public void genUrlInit() {
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long id = Long.parseLong(req.getParamValue("id"));
			TtLaborListPO lpo = new TtLaborListPO();
			lpo.setReportId(id);
			List<TtLaborListPO> list = dao.select(lpo);
			if (list.size() > 0) {
				lpo = list.get(0);
				//List<SummaryReportBean> bean = dao.getMyMap(lpo.getReportCode());
				//act.setOutData("list", bean);

				// 如果汇总报表还没相应的记录，则执行如下操作 先查询，再插入
				TtTaxableServiceSumPO spo = new TtTaxableServiceSumPO();
				spo.setSumParameterId(lpo.getReportId());
				List<TtTaxableServiceSumPO> sumList = dao.select(spo);

				if (sumList.size() == 0) {
					// 汇总表中没相应的记录，就先执行查询，再插入
					spo.setCreateBy(user.getUserId());
					spo.setCreateDate(new Date());
					spo.setDlrCompanyId(user.getCompanyId());
					spo.setDlrId(Long.parseLong(user.getDealerId()));
					spo.setInvoiceNo(lpo.getInvoiceCode());
					spo.setOemCompanyId(Long.parseLong(user.getOemCompanyId()));
					spo.setPurchaserId(new Long(lpo.getYieldly()));
					spo.setSalesId(lpo.getDealerId());
					spo.setStatisticsDate(new Date());
					spo.setSumParameterId(lpo.getReportId());
					spo.setSumParameterNo(lpo.getReportCode());
					spo.setTaxableServiceSumId(Utility.getLong(SequenceManager.getSequence("")));
					spo.setTaxRate(Double.parseDouble(req.getParamValue("tax")));
                    spo.setAuthStatus(Constant.TAXABLE_SERVICE_SUM_WAIT); //YH 2010.12.16
                    spo.setAmount(lpo.getAmount()); //YH 2010.12.16
					dao.insert(spo);

					//执行存储过程，将行表写入tt_taxable_sum_print_tmp表中
					TcCodePO code = new TcCodePO() ;
					code.setType("8008") ;
					List listCode = dao.select(code) ;
					if(listCode.size()>0){
						code = (TcCodePO)listCode.get(0);
						act.setOutData("code", code.getCodeId()) ;
						if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
							dao.insertPrintDetail(id);
						 }
						else{
							
							dao.insertPrintDetail2(id);
						}
					}
					
					
					// 明细表暂时不写入，查询的时候可以直接调用dao.getMyMap(lpo.getReportCode()方法
					// TtTaxableServiceSumDetailPO dpo = new
					// TtTaxableServiceSumDetailPO();
					// dpo.setExclTaxAmount();
				}
			}
			//查询刚才的存储过程中写入的数据或者是以前曾写进去的数据。
			List<SummaryReportBean> bean = dao.getPrintDetail(id);
			act.setOutData("list", bean);
			
			TmDealerPO dpo = new TmDealerPO();
			dpo.setDealerId(lpo.getDealerId());
			List<TmDealerPO> dealerList = dao.select(dpo);
			if (dealerList.size() > 0)
				act.setOutData("dealer", dealerList.get(0));

			act.setOutData("tax", Double.parseDouble(req.getParamValue("tax")));
			act.setOutData("lpo", lpo);
			act.setForword(PRINT_URL);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表打印");
			logger.error(user, be);
			act.setException(be);
		}
	}

	/*
	 * 明细页面初始化方法
	 */
	@SuppressWarnings("unchecked")
	public void showDetail() {
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long id = Long.parseLong(req.getParamValue("id"));
			TtTaxableServiceSumPO po = new TtTaxableServiceSumPO();
			po.setTaxableServiceSumId(id);
			List<TtTaxableServiceSumPO> list = dao.select(po);
			if (list.size() > 0){
				po = list.get(0);
				
				TmDealerPO dpo = new TmDealerPO();
				dpo.setDealerId(po.getDlrId());
				List<TmDealerPO> dealerList = dao.select(dpo);
				if (dealerList.size() > 0)
					act.setOutData("dealer", dealerList.get(0));
				
				List<SummaryReportBean> bean = dao.getMyMap(po.getSumParameterNo());
				act.setOutData("list", bean);
			}
			act.setOutData("report", po);
			act.setForword(DETAIL_URL);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表");
			logger.error(user, be);
			act.setException(be);
		}
	}

	/********************** OEM **********************/

	/*
	 * 车厂端劳务清单汇总报表主页面初始化
	 */
	public void mainUrlInitOem() {
		act = ActionContext.getContext();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(user.getPoseId());
			
			act.setOutData("yieldly", yieldly);
			act.setForword(MAIN_URL_OEM);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表(OEM)");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	//zhumingwei 2011-04-18
	public void mainUrlInitOem11() {
		act = ActionContext.getContext();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(user.getPoseId());
			
			act.setOutData("yieldly", yieldly);
			act.setForword(MAIN_URL_OEM11);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表(OEM)");
			logger.error(user, be);
			act.setException(be);
		}
	}

	/*
	 * 针对车厂端明细按钮的操作
	 */
	@SuppressWarnings("unchecked")
	public void showDetailOem() {
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");//参数设置ID
			String code = req.getParamValue("code");// 参数设置代码
			PageResult<Map<String, Object>> ps = dao.getDetail(id);
			Map<String, Object> map = null;
			if (ps.getTotalRecords() > 0)
				map = ps.getRecords().get(0);
			act.setOutData("map", map);
			
			String sql = "SELECT distinct auth_status FROM Tt_Taxable_Sum_Authitem  WHERE 1=1  AND Balance_Id=? ";
			List params = new ArrayList();
			params.add(id);			
			List<TtTaxableSumAuthitemPO> aList = dao.select(TtTaxableSumAuthitemPO.class,sql,params);
			act.setOutData("count",aList.size());//确定按钮是否可用
			
			// 明细数据
			List<SummaryReportBean> bean = dao.getMyMap(code);
			act.setOutData("list", bean);

			act.setForword(DETAIL_URL_OEM);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表(OEM)");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	//zhumingwei 2011-04-35
	public void showDetailOem22() {
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");//参数设置ID
			String code = req.getParamValue("code");// 参数设置代码
			PageResult<Map<String, Object>> ps = dao.getDetail11(id);
			Map<String, Object> map = null;
			if (ps.getTotalRecords() > 0)
				map = ps.getRecords().get(0);
			act.setOutData("map", map);
			
			TtLaborListPO po  = new TtLaborListPO();
			po.setReportId(Long.parseLong(id));
			TtLaborListPO poValue = (TtLaborListPO)dao.select(po).get(0);
			act.setOutData("reportAmount", poValue.getReportAmount());
			act.setOutData("authAmount", poValue.getAuthAmount());
			
			String sql = "SELECT distinct auth_status FROM Tt_Taxable_Sum_Authitem  WHERE 1=1  AND Balance_Id=? ";
			List params = new ArrayList();
			params.add(id);			
			List<TtTaxableSumAuthitemPO> aList = dao.select(TtTaxableSumAuthitemPO.class,sql,params);
			act.setOutData("count",aList.size());//确定按钮是否可用
			
			// 明细数据
			List<SummaryReportBean> bean = dao.getMyMap(code);
			act.setOutData("list", bean);

			act.setForword(DETAIL_URL_OEM22);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表(OEM)");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	//zhumingwei 2011-04-18
	public void showDetailOem11() {
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");//参数设置ID
			String code = req.getParamValue("code");// 参数设置代码
			PageResult<Map<String, Object>> ps = dao.getDetail11(id);
			Map<String, Object> map = null;
			if (ps.getTotalRecords() > 0)
				map = ps.getRecords().get(0);
			act.setOutData("map", map);
			
			TtLaborListPO po  = new TtLaborListPO();
			po.setReportId(Long.parseLong(id));
			TtLaborListPO poValue = (TtLaborListPO)dao.select(po).get(0);
			act.setOutData("reportAmount", poValue.getReportAmount());
			
			String sql = "SELECT distinct auth_status FROM Tt_Taxable_Sum_Authitem  WHERE 1=1  AND Balance_Id=? and auth_status in('"+Constant.TAXABLE_SERVICE_SUM_GET+"','"+Constant.TAXABLE_SERVICE_SUM_FANCE+"')";
			List params = new ArrayList();
			params.add(id);			
			List<TtTaxableSumAuthitemPO> aList = dao.select(TtTaxableSumAuthitemPO.class,sql,params);
			int sss = aList.size();
			act.setOutData("count",aList.size());//确定按钮是否可用
			
			// 明细数据
			List<SummaryReportBean> bean = dao.getMyMap(code);
			act.setOutData("list", bean);

			act.setForword(DETAIL_URL_OEM11);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表(OEM)");
			logger.error(user, be);
			act.setException(be);
		}
	}

	/*
	 * 车厂端的收票与财务挂账操作
	 */
	@SuppressWarnings("unchecked")
	public void getInvoice(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String id = req.getParamValue("id");
			String flag = req.getParamValue("flag");//1:收票    2:财务挂账 			
			Integer status = Constant.TAXABLE_SERVICE_SUM_GET; //更新主表状态  YH 2010.12.16
			
			if("1".endsWith(flag)){
				status = Constant.TAXABLE_SERVICE_SUM_GET ;  
				TtTaxableServiceSumPO tasp1 = new TtTaxableServiceSumPO();
				tasp1.setSumParameterId(Long.parseLong(id));
				TtTaxableServiceSumPO tasp2 = new TtTaxableServiceSumPO();
				tasp2.setAuthStatus(status);
				dao.update(tasp1, tasp2);
			}
			if("2".endsWith(flag)){    //YH 2010.12.17
				status = Constant.TAXABLE_SERVICE_SUM_FANCE;
				TtTaxableServiceSumPO tasp1 = new TtTaxableServiceSumPO();
				tasp1.setSumParameterId(Long.parseLong(id));
				TtTaxableServiceSumPO tasp2 = new TtTaxableServiceSumPO();
				tasp2.setAuthStatus(status);
				int i = dao.update(tasp1, tasp2);
			}
			if("3".endsWith(flag)){
				status = Constant.TAXABLE_SERVICE_SUM_WAIT;
				TtTaxableServiceSumPO tasp1 = new TtTaxableServiceSumPO();
				tasp1.setSumParameterId(Long.parseLong(id));
				TtTaxableServiceSumPO tasp2 = new TtTaxableServiceSumPO();
				tasp2.setAuthStatus(status);
				dao.update(tasp1, tasp2);
				TtTaxableSumAuthitemPO popo = new TtTaxableSumAuthitemPO();
				popo.setBalanceId(Long.parseLong(id));
				dao.delete(popo);
				
				//添加退票记录
				TtTaxableSumAuthitemPO po = new TtTaxableSumAuthitemPO();
				po.setBalanceId(Long.parseLong(id));
				po.setAuthOrgId(user.getOrgId());
				po.setAuthPersonId(user.getUserId());
				po.setAuthPersonName(user.getName());
				po.setAuthStatus(Constant.TAXABLE_SERVICE_SUM_FANCE3);
				po.setAuthTime(new Date());
				po.setCreateBy(user.getUserId());
				po.setCreateDate(new Date());
				dao.insert(po);
			}
			if("4".endsWith(flag)){
				status = Constant.TAXABLE_SERVICE_SUM_GET;
				TtTaxableServiceSumPO tasp1 = new TtTaxableServiceSumPO();
				tasp1.setSumParameterId(Long.parseLong(id));
				TtTaxableServiceSumPO tasp2 = new TtTaxableServiceSumPO();
				tasp2.setAuthStatus(status);
				dao.update(tasp1, tasp2);
				TtTaxableSumAuthitemPO popo = new TtTaxableSumAuthitemPO();
				popo.setBalanceId(Long.parseLong(id));
				popo.setAuthStatus(Constant.TAXABLE_SERVICE_SUM_FANCE);
				dao.delete(popo);
				
				//添加退帐记录
				TtTaxableSumAuthitemPO po = new TtTaxableSumAuthitemPO();
				po.setBalanceId(Long.parseLong(id));
				po.setAuthOrgId(user.getOrgId());
				po.setAuthPersonId(user.getUserId());
				po.setAuthPersonName(user.getName());
				po.setAuthStatus(Constant.TAXABLE_SERVICE_SUM_FANCE4);
				po.setAuthTime(new Date());
				po.setCreateBy(user.getUserId());
				po.setCreateDate(new Date());
				dao.insert(po);
			}
			if(!"3".endsWith(flag) && !"4".endsWith(flag)){
				TtTaxableSumAuthitemPO po = new TtTaxableSumAuthitemPO();
				po.setBalanceId(Long.parseLong(id));
				po.setAuthOrgId(user.getOrgId());
				po.setAuthPersonId(user.getUserId());
				po.setAuthPersonName(user.getName());
				po.setAuthStatus(status);
				po.setAuthTime(new Date());
				po.setCreateBy(user.getUserId());
				po.setCreateDate(new Date());
				dao.insert(po);
			}
			
			act.setOutData("type",flag);//返回点击的按钮编号以使其不可用
			act.setOutData("flag",true);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表收票(OEM)");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//zhumingwei 2011-04-18
	public void getInvoice11(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String id = req.getParamValue("id");
			String flag = req.getParamValue("flag");//1:收票    2:财务挂账 			
			Integer status = Constant.TAXABLE_SERVICE_SUM_GET; //更新主表状态  YH 2010.12.16
			
			if("1".endsWith(flag)){      //YH 2010.12.17
				status = Constant.TAXABLE_SERVICE_SUM_GET ;  
				TtTaxableServiceSumPO tasp1 = new TtTaxableServiceSumPO();
				tasp1.setSumParameterId(Long.parseLong(id));
				TtTaxableServiceSumPO tasp2 = new TtTaxableServiceSumPO();
				tasp2.setAuthStatus(status);
				int i = dao.update(tasp1, tasp2);
				
				TtLaborListPO po = new TtLaborListPO();
				po.setReportId(Long.parseLong(id));
				TtLaborListPO poValue = new TtLaborListPO();
				poValue.setAuthAmount(Double.parseDouble(req.getParamValue("reportAmount")));
				poValue.setUpdateBy(user.getUserId());
				poValue.setUpdateDate(new Date());
				dao.update(po, poValue);
			}
			if("2".endsWith(flag)){    //YH 2010.12.17
				status = Constant.TAXABLE_SERVICE_SUM_FANCE;
				TtTaxableServiceSumPO tasp1 = new TtTaxableServiceSumPO();
				tasp1.setSumParameterId(Long.parseLong(id));
				TtTaxableServiceSumPO tasp2 = new TtTaxableServiceSumPO();
				tasp2.setAuthStatus(status);
				int i = dao.update(tasp1, tasp2);
			}
			
			boolean flag2 = DBLockUtil.lock(id, DBLockUtil.BUSINESS_TYPE_14);
			if(!flag2){
				act.setOutData("SUCCESS", "LOCK");
				return;
			}
			
			TtTaxableSumAuthitemPO po = new TtTaxableSumAuthitemPO();
			po.setBalanceId(Long.parseLong(id));
			po.setAuthOrgId(user.getOrgId());
			po.setAuthPersonId(user.getUserId());
			po.setAuthPersonName(user.getName());
			po.setAuthStatus(status);
			po.setAuthTime(new Date());
			po.setCreateBy(user.getUserId());
			po.setCreateDate(new Date());
			dao.insert(po);
			
			act.setOutData("type",flag);//返回点击的按钮编号以使其不可用
			act.setOutData("flag",true);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表收票(OEM)");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	/*
	 * 打印方法
	 */
	public void printSum(){
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long id = Long.parseLong(req.getParamValue("id"));
			TtLaborListPO lpo = new TtLaborListPO();
			lpo.setReportId(id);
			List<TtLaborListPO> list = dao.select(lpo);
			if(list.size()>0)
				lpo = list.get(0);
			
			TtTaxableServiceSumPO spo = new TtTaxableServiceSumPO();
			spo.setSumParameterId(lpo.getReportId());
			List<TtTaxableServiceSumPO> sumList = dao.select(spo);
			
			//查询刚才的存储过程中写入的数据或者是以前曾写进去的数据。
			List<SummaryReportBean> bean = dao.getPrintDetail(sumList.get(0).getSumParameterId());
			act.setOutData("list", bean);
			
			TmDealerPO dpo = new TmDealerPO();
			dpo.setDealerId(lpo.getDealerId());
			List<TmDealerPO> dealerList = dao.select(dpo);
			if (dealerList.size() > 0)
				act.setOutData("dealer", dealerList.get(0));

			act.setOutData("tax", sumList.get(0).getTaxRate());
			
			act.setOutData("lpo", lpo);
			act.setForword(PRINT_URL);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "劳务清单汇总报表打印");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	//报销单打印(经销商端)
	public void prinReimbursement(){
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long id = Long.parseLong(req.getParamValue("id"));
			TtLaborListPO lpo = new TtLaborListPO();
			lpo.setReportId(id);
			List<TtLaborListPO> list = dao.select(lpo);
			if(list.size()>0){
				lpo = list.get(0);
			}
			TtTaxableServiceSumPO spo = new TtTaxableServiceSumPO();
			spo.setSumParameterId(lpo.getReportId());
			List<TtTaxableServiceSumPO> sumList = dao.select(spo);
			
			Date time = new Date();
			SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(time);
			act.setOutData("date", date);
			TmDealerPO po = new TmDealerPO();
			po.setDealerId(lpo.getDealerId());
			TmDealerPO poValue = (TmDealerPO)dao.select(po).get(0);
			act.setOutData("name", poValue.getDealerName());
			act.setOutData("dealerCode", poValue.getDealerCode());
			
			List<Map<String, Object>> count = dao.getRegionName(poValue.getProvinceId().toString());
			Map map = (Map)count.get(0);
			String regionName = map.get("REGION_NAME").toString();
			act.setOutData("regionName", regionName);
			
			act.setOutData("amount", sumList.get(0).getAmount());
			String bigAmount = dao.numToChinese(sumList.get(0).getAmount().toString());
			act.setOutData("bigAmount", bigAmount);
			
			//查出合同号
			TtAsWrContractPO cPO = new TtAsWrContractPO();
			cPO.setDealerId(lpo.getDealerId());
			cPO.setYieldly(lpo.getYieldly());
			List contractList = dao.select(cPO);
			if(contractList.size()<=0){
				act.setOutData("contractNo", "空");
			}else{
				TtAsWrContractPO cPOValue = (TtAsWrContractPO)contractList.get(0);
				act.setOutData("contractNo", cPOValue.getContractNo());
			}
			
			act.setOutData("ID", id);
			act.setForword(PRINT_REIM_URL);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(user, be);
			act.setException(be);
		}
	}
	public void changeNum(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String count = request.getParamValue("count");
		String id = request.getParamValue("ID");
		TtTaxableServiceSumPO po = new TtTaxableServiceSumPO();
		po.setSumParameterId(Long.parseLong(id));
		TtTaxableServiceSumPO poValue = new TtTaxableServiceSumPO();
		poValue.setCount_num(Integer.parseInt(count));
		poValue.setStatus(1);
		poValue.setCreateBy(logonUser.getUserId());
		poValue.setCreateDate(new Date());
		int con=dao.update(po, poValue);
		if(con>0){
			act.setOutData("ok", "ok");
		}else{
			act.setOutData("error", "error");
		}
	}
	//报销单打印(车厂)
	public void prinReimbursement1(){
		act = ActionContext.getContext();
		req = act.getRequest();
		user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long id = Long.parseLong(req.getParamValue("id"));
			TtLaborListPO lpo = new TtLaborListPO();
			lpo.setReportId(id);
			List<TtLaborListPO> list = dao.select(lpo);
			if(list.size()>0){
				lpo = list.get(0);
			}
			TtTaxableServiceSumPO spo = new TtTaxableServiceSumPO();
			spo.setSumParameterId(lpo.getReportId());
			List<TtTaxableServiceSumPO> sumList = dao.select(spo);
			
			Date time = new Date();
			SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(time);
			act.setOutData("date", date);
			TmDealerPO po = new TmDealerPO();
			po.setDealerId(lpo.getDealerId());
			TmDealerPO poValue = (TmDealerPO)dao.select(po).get(0);
			act.setOutData("name", poValue.getDealerName());
			act.setOutData("dealerCode", poValue.getDealerCode());
			
			List<Map<String, Object>> count = dao.getRegionName(poValue.getProvinceId().toString());
			Map map = (Map)count.get(0);
			String regionName = map.get("REGION_NAME").toString();
			act.setOutData("regionName", regionName);
			
			act.setOutData("amount", sumList.get(0).getAmount());
			String bigAmount = dao.numToChinese(sumList.get(0).getAmount().toString());
			act.setOutData("bigAmount", bigAmount);
			
			//查出合同号
			TtAsWrContractPO cPO = new TtAsWrContractPO();
			cPO.setDealerId(lpo.getDealerId());
			cPO.setYieldly(lpo.getYieldly());
			List contractList = dao.select(cPO);
			if(contractList.size()<=0){
				act.setOutData("contractNo", "空");
			}else{
				TtAsWrContractPO cPOValue = (TtAsWrContractPO)contractList.get(0);
				act.setOutData("contractNo", cPOValue.getContractNo());
			}
			
			act.setOutData("ID", id);
			act.setForword(PRINT_REIM_URL);
		} catch (Exception e) {
			BizException be = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(user, be);
			act.setException(be);
		}
	}
}
