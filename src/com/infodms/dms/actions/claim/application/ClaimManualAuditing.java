package com.infodms.dms.actions.claim.application;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.balanceAuditing.BalanceAuditing;
import com.infodms.dms.actions.claim.specialExpenses.SpecialExpensesManage;
import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimListBean;
import com.infodms.dms.bean.ClaimOrderBean;
import com.infodms.dms.bean.auditBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.application.ClaimManualAuditingDao;
import com.infodms.dms.dao.claim.application.DealerBalanceDao;
import com.infodms.dms.dao.claim.auditing.BalanceAuditingDao;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmClaimContactPhonePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFineNewsPO;
import com.infodms.dms.po.TmRegionPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivitySubjectPO;
import com.infodms.dms.po.TtAsDealerTypePO;
import com.infodms.dms.po.TtAsPaymentPO;
import com.infodms.dms.po.TtAsWrAppAuditDetailPO;
import com.infodms.dms.po.TtAsWrAppauthitemPO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrApplicationPartPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrCompensationAppPO;
import com.infodms.dms.po.TtAsWrFinePO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infodms.dms.po.TtAsWrNetitemExtPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtAsWrSpefeePO;
import com.infodms.dms.po.TtAsWrTicketsPO;
import com.infodms.dms.po.TtAsWrWrauthorizationPO;
import com.infodms.dms.po.TtPartBillDefinePO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.utils.BaseUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 索赔申请单人工审核
 * 
 * @author XZM
 */
public class ClaimManualAuditing extends BaseAction {

	private Logger logger = Logger.getLogger(ClaimManualAuditingDao.class);
	private final ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
	/** 索赔申请审核作业(授权审核) 查询首页面 */
	private final String MANUAL_AUDITING = "/jsp/claim/application/claimAuditIndex.jsp";
	private final String balancetoal = "/jsp/claim/application/balancetoal.jsp";
	private final String balancetoal1 = "/jsp/claim/application/balancetoal1.jsp";
	private final String balancetoalYX = "/jsp/claim/application/balancetoalXY.jsp";
	/** 索赔单删除审核查询首页面 */
	private final String DEL_MANUAL_AUDITING = "/jsp/claim/application/deleteClaimAuditIndex.jsp";
	private final String DEL_MANUAL_FOR_AUDITING = "/jsp/claim/application/deleteClaimAuditForIndex.jsp";
	/** 索赔申请审核作业(授权审核) 首页面 */
	private final String MANUAL_AUDITING_PAGE = "/jsp/claim/application/claimAuditPage.jsp";

	private final String MANUAL_AUDITING_PAGE2 = "/jsp/claim/application/claimAuditPage2.jsp";
	private final String MANUAL_AUDITING_PAGE_PDI = "/jsp/claim/application/claimAuditPagePDI.jsp";
	/** 索赔申请审核作业（结算审核） 首页面 */
	private final String BALANCE_AUDITING_PAGE = "/jsp/claim/application/balanceAuditPage.jsp";
	/** 索赔申请审核作业（结算审核） 首页面 */
	private final String BALANCE_AUDITING_ONEBYONE = "/jsp/claim/application/balanceAuditOneByOne.jsp";
	/** 索赔申请重新审核作业(授权审核) 查询首页面 */
	private final String MANUAL_REAUDITING = "/jsp/claim/application/claimReAuditIndex.jsp";
	/** 索赔申请重新审核作业(授权审核) 首页面 */
	private final String MANUAL_REAUDITING_PAGE = "/jsp/claim/application/claimReAuditPage.jsp";
	/** 索赔申请二次复核查询页面 */
	private final String ENCOURAGE_URL = "/jsp/claim/other/encourageSecond.jsp"; // 二次复核页面
	/** 最长锁定时间（当有一用户进行结算时，其他用户不允许方法）分钟 */
	private static final Long lockLimit = 10L;

	/** 功能描述 */
	private String FUNCTION_DESC = "索赔申请审核作业";

	// 结算室审核页面
	private final String accaudit = "/jsp/claim/application/accAudit.jsp";

	/** 新结算室审核索赔单页面 */
	private final String acconeAuditClaim = "/jsp/claim/application/acconeAuditClaim.jsp";

	/** 新索赔单逐条审核页面 */
	private final String claimAuditOneByOne = "/jsp/claim/application/claimAuditOneByOne.jsp";

	// 索赔单审核页面
	private final String acconeaudit = "/jsp/claim/application/acconeAudit.jsp";
	/** 索赔申请审核作业（结算审核） 首页面(微车) */
	private final String BALANCE_AUDITING_PAGE_WC = "/jsp/claim/application/balanceAuditPageWC.jsp";

	/** **********轿车结算室审核索赔单2011-05-23kevinyin*********** */

	/** 新结算室审核索赔单页面 */
	private final String acconeAuditClaimJC = "/jsp/claim/application/acconeAuditClaimJC.jsp";
	private final String acconeAuditClaimYXJC = "/jsp/claim/application/acconeAuditClaimYXJC.jsp";
	private final String acconeAuditClaimYXZRJC = "/jsp/claim/application/acconeAuditClaimYXZRJC.jsp";
	private final String acconeAuditClaimJC01 = "/jsp/claim/application/acconeAuditClaimJC01.jsp";
	private final String acconeAuditClaimJC02 = "/jsp/claim/application/acconeAuditClaimJC02.jsp";
	private final String acconeAuditClaimJC03 = "/jsp/claim/application/acconeAuditClaimJC03.jsp";
	/** 新索赔单逐条审核页面 */
	private final String claimAuditOneByOneJC = "/jsp/claim/application/claimAuditOneByOneJC.jsp";
	/** 索赔申请审核作业（结算审核） 首页面 */
	private final String BALANCE_AUDITING_PAGE_JC = "/jsp/claim/application/balanceAuditPageJC.jsp";
	private final String BALANCE_AUDITING_PAGE_JC_30 = "/jsp/claim/application/balanceAuditPageJC30.jsp";
	private final String BALANCE_AUDITING_PAGE_JC_31 = "/jsp/claim/application/balanceAuditPageJC31.jsp";
	private final String BALANCE_AUDITING_PAGE_JC01 = "/jsp/claim/application/balanceAuditPageJC01.jsp";
	private final String BALANCE_AUDITING_PAGE_JC10 = "/jsp/claim/application/balanceAuditPageJC10.jsp";
	private final String BALANCE_AUDITING_PAGE_JC02 = "/jsp/claim/application/balanceAuditPageJC02.jsp";
	private final String BALANCE_AUDITING_PAGE_JC20 = "/jsp/claim/application/balanceAuditPageJC20.jsp";
	private final String BALANCE_AUDITING_TICKETS = "/jsp/claim/application/tickets.jsp";
	private final String BALANCE_AUDITING_TICKETSVW = "/jsp/claim/application/ticketsViw.jsp";
	private final String BALANCE_AUDITING_TICKETSPT = "/jsp/claim/application/ticketsPrint.jsp";
	private final String BALANCE_AUDITING_PAYMENTMANAGE = "/jsp/claim/application/paymentManage.jsp";
	private final String BALANCE_AUDITING_PAYMENTMANAGEOK = "/jsp/claim/application/paymentManageOk.jsp";
	private final String BALANCE_AUDITING_PAYMENTMANAGEADD = "/jsp/claim/application/paymentManageAdd.jsp";
	private final String BALANCE_AUDITING_PAYMENTMANAGEUPDATE = "/jsp/claim/application/paymentManageMfiy.jsp";
	private final String BALANCE_AUDITING_PAYMENTMANAGEINFOR = "/jsp/claim/application/paymentManageInFor.jsp";
	private final String BALANCE_AUDITING_PAYMENTMANAGEINPRINT = "/jsp/claim/application/paymentManageInPrint.jsp";

	private final String BALANCE_AUDITING_ADDTICKETS = "/jsp/claim/application/addtickets.jsp";
	private final String BALANCE_AUDITING_ADDTICKETS_DONGAN = "/jsp/claim/application/addtickets_dongan.jsp";
	private final String BALANCE_DETAIL = "/jsp/claim/application/balance_detail.jsp";
	private final String BALANCE_DETAIL_YX = "/jsp/claim/application/balance_detailyx.jsp";
	private final String ADMINISTRATIVE_CHARGE = "/jsp/claim/application/administrative_charge.jsp";

	private final String SHOW_MAL_CODE = "/jsp/claim/application/showMalCode.jsp";

	/** **********轿车结算室审核索赔单2011-05-23kevinyin*********** */
	public void administrative_charge() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			if (!"".equals(Command)) {
				String BALANCE_ODER = request.getParamValue("BALANCE_ODER");// 结算单号
				String status = request.getParamValue("status");// 状态
				String dealercode = request.getParamValue("dealerCode");
				if (logonUser.getDealerCode() != null
						&& logonUser.getDealerCode().length() > 0) {
					dealercode = logonUser.getDealerCode();
				}

				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();

				// 分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页

				Integer pageSize = 20;
				PageResult<Map<String, Object>> ps = dao.administrative_charge(
						BALANCE_ODER, status, dealercode, curPage, pageSize);
				// 分页方法 end
				act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
			} else {
				if (logonUser.getDealerCode() != null
						&& logonUser.getDealerCode().length() > 0) {
					act.setOutData("code", 0);
				} else {
					act.setOutData("code", 1);
				}
				act.setForword(ADMINISTRATIVE_CHARGE);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void balance_detail() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			if (!"".equals(Command)) {
				String report_date = request.getParamValue("report_date");// 索赔单状态
				String last_report_date = request
						.getParamValue("last_report_date");// 索赔单状态
				String DIRECTOR_DATE = request.getParamValue("DIRECTOR_DATE");// 索赔单状态
				String LAST_DIRECTOR_DATE = request
						.getParamValue("LAST_DIRECTOR_DATE");// 索赔单状态
				String balance_yieldly = request
						.getParamValue("balance_yieldly");// 索赔单状态
				String dealercode = request.getParamValue("dealercode");
				String dealerId = "";
				if (dealercode != null) {
					TmDealerPO tmDealerPO = new TmDealerPO();
					tmDealerPO.setDealerCode(dealercode);
					List<TmDealerPO> list = dao.select(tmDealerPO);
					if (list.size() > 0) {
						dealerId = "" + list.get(0).getDealerId();
					}
				}

				Long userID = logonUser.getUserId();
				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();

				// 分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页

				Integer pageSize = 20;
				PageResult<Map<String, Object>> ps = dao
						.querycarparkAuditingClaim(report_date,
								last_report_date, DIRECTOR_DATE,
								LAST_DIRECTOR_DATE, dealerId, balance_yieldly,
								userID, logonUser, curPage, pageSize);

				// 分页方法 end
				act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
			} else {
				act.setOutData("balance_yieldly", "95411001");
				act.setForword(BALANCE_DETAIL);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void balance_detailda() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			if (!"".equals(Command)) {
				String report_date = request.getParamValue("report_date");// 索赔单状态
				String balance_yieldly = request
						.getParamValue("balance_yieldly");// 索赔单状态
				String dealercode = request.getParamValue("dealercode");
				String last_report_date = request
						.getParamValue("last_report_date");// 索赔单状态
				String DIRECTOR_DATE = request.getParamValue("DIRECTOR_DATE");// 索赔单状态
				String LAST_DIRECTOR_DATE = request
						.getParamValue("LAST_DIRECTOR_DATE");// 索赔单状态
				String dealerId = "";
				if (dealercode != null) {
					TmDealerPO tmDealerPO = new TmDealerPO();
					tmDealerPO.setDealerCode(dealercode);
					List<TmDealerPO> list = dao.select(tmDealerPO);
					if (list.size() > 0) {
						dealerId = "" + list.get(0).getDealerId();
					}
				}

				Long userID = logonUser.getUserId();
				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();

				// 分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页

				Integer pageSize = 20;
				PageResult<Map<String, Object>> ps = dao
						.querycarparkAuditingClaim(report_date,
								last_report_date, DIRECTOR_DATE,
								LAST_DIRECTOR_DATE, dealerId, balance_yieldly,
								userID, logonUser, curPage, pageSize);

				// 分页方法 end
				act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
			} else {
				act.setOutData("balance_yieldly", "95411002");
				act.setForword(BALANCE_DETAIL);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void balance_detailyx() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			if (!"".equals(Command)) {
				String report_date = request.getParamValue("report_date");// 索赔单状态
				String dealercode = request.getParamValue("dealercode");
				String last_report_date = request
						.getParamValue("last_report_date");
				String dealerId = "";
				if (dealercode != null) {
					TmDealerPO tmDealerPO = new TmDealerPO();
					tmDealerPO.setDealerCode(dealercode);
					List<TmDealerPO> list = dao.select(tmDealerPO);
					if (list.size() > 0) {
						dealerId = "" + list.get(0).getDealerId();
					}
				}

				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();

				// 分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页

				Integer pageSize = 20;
				PageResult<Map<String, Object>> ps = dao
						.querycarparkAuditingClaimyx(report_date,
								last_report_date, dealerId, curPage, pageSize);

				// 分页方法 end
				act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
			} else {
				act.setForword(BALANCE_DETAIL_YX);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void judeorder() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealer_id = request.getParamValue("dealer_id");
			String balance_yieldly = request.getParamValue("balance_yieldly");
			String report_date = request.getParamValue("report_date");
			String claim_type = request.getParamValue("claim_type");
			act.setOutData("dealer_id", dealer_id);
			act.setOutData("balance_yieldly", balance_yieldly);
			act.setOutData("report_date", report_date);
			act.setOutData("claim_type", claim_type);
			act.setOutData("type", "yes");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void paymentManage() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerCode = request.getParamValue("dealerCode");
			String dealerName = request.getParamValue("dealerName");
			String balance_oder = request.getParamValue("balance_oder");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			String comm = request.getParamValue("comm");
			String balance_yieldly = request.getParamValue("balance_yieldly");
			if (comm != null) {
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1;
				PageResult<Map<String, Object>> ps = dao.get_payMent(
						dealerCode, dealerName, balance_oder, balance_yieldly,
						startDate, endDate, logonUser.getUserId(), logonUser,
						Constant.PAGE_SIZE, curPage);
				act.setOutData("ps", ps);
			} else {
				act.setOutData("balance_yieldly", Constant.PART_IS_CHANGHE_01);
				act.setForword(this.BALANCE_AUDITING_PAYMENTMANAGE);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void paymentManageDa() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerCode = request.getParamValue("dealerCode");
			String dealerName = request.getParamValue("dealerName");
			String balance_oder = request.getParamValue("balance_oder");
			String comm = request.getParamValue("comm");
			String balance_yieldly = request.getParamValue("balance_yieldly");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			if (comm != null) {
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1;
				PageResult<Map<String, Object>> ps = dao.get_payMent(
						dealerCode, dealerName, balance_oder, balance_yieldly,
						startDate, endDate, logonUser.getUserId(), logonUser,
						Constant.PAGE_SIZE, curPage);
				act.setOutData("ps", ps);
			} else {
				act.setOutData("balance_yieldly", Constant.PART_IS_CHANGHE_02);
				act.setForword(this.BALANCE_AUDITING_PAYMENTMANAGE);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void paymentManageOk() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerCode = request.getParamValue("dealerCode");
			String dealerName = request.getParamValue("dealerName");
			String balance_oder = request.getParamValue("balance_oder");
			String comm = request.getParamValue("comm");
			String balance_yieldly = request.getParamValue("balance_yieldly");
			if (comm != null) {
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1;
				PageResult<Map<String, Object>> ps = dao.get_payMentOk(
						dealerCode, dealerName, balance_oder, balance_yieldly,
						Constant.PAGE_SIZE, curPage);
				act.setOutData("ps", ps);
			} else {
				act.setOutData("balance_yieldly", Constant.PART_IS_CHANGHE_01);
				act.setForword(this.BALANCE_AUDITING_PAYMENTMANAGEOK);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void paymentManageOkDa() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerCode = request.getParamValue("dealerCode");
			String dealerName = request.getParamValue("dealerName");
			String balance_oder = request.getParamValue("balance_oder");
			String comm = request.getParamValue("comm");
			String balance_yieldly = request.getParamValue("balance_yieldly");
			if (comm != null) {
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1;
				PageResult<Map<String, Object>> ps = dao.get_payMentOk(
						dealerCode, dealerName, balance_oder, balance_yieldly,
						Constant.PAGE_SIZE, curPage);
				act.setOutData("ps", ps);
			} else {
				act.setOutData("balance_yieldly", Constant.PART_IS_CHANGHE_02);
				act.setForword(this.BALANCE_AUDITING_PAYMENTMANAGEOK);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void okcommit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String[] balance_oders = request.getParamValues("claimId");
			String status = request.getParamValue("STATUS");
			String mark = request.getParamValue("MARK");
			String balance_yieldly = request.getParamValue("balance_yieldly");

			if (status.equals("0")) {
				for (String balance_oder : balance_oders) {
					TtAsPaymentPO paymentPO = new TtAsPaymentPO();
					paymentPO.setBalanceOder(balance_oder);
					dao.delete(paymentPO);
				}

			} else {
				for (String balance_oder : balance_oders) {
					TtAsPaymentPO paymentPO = new TtAsPaymentPO();
					paymentPO.setBalanceOder(balance_oder);
					TtAsPaymentPO paymentPO1 = new TtAsPaymentPO();
					paymentPO1.setStatus(Integer.parseInt(status));
					paymentPO1.setRemark(mark);
					dao.update(paymentPO, paymentPO1);
				}
			}

			if (balance_yieldly.equals("95411001")) {
				paymentManageOk();
			} else {
				paymentManageOkDa();
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void paymentManageInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setOutData("balance_yieldly", act.getRequest().getParamValue(
					"balance_yieldly"));
			act.setForword(this.BALANCE_AUDITING_PAYMENTMANAGEADD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void updatePayMent() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {

			RequestWrapper request = act.getRequest();
			String status = request.getParamValue("status");
			String balance_oder = request.getParamValue("balance_oder");
			String SERIAL_NUMBER = "";
			TtAsPaymentPO paymentPO1 = new TtAsPaymentPO();
			paymentPO1.setBalanceOder(balance_oder);
			List<TtAsPaymentPO> list = dao.select(paymentPO1);
			if (list.size() > 0) {
				SERIAL_NUMBER = list.get(0).getSerialNumber();
			} else {
				StringBuffer sql = new StringBuffer();
				sql
						.append("select max(t.SERIAL_NUMBER) SERIAL_NUMBER from tt_as_payment t where t.BALANCE_ODER LIKE '%"
								+ balance_oder.substring(0, 6) + "%'");
				List<TtAsPaymentPO> payList = dao.select(TtAsPaymentPO.class,
						sql.toString(), null);
				if (payList.size() > 0
						&& Utility.testString(payList.get(0).getSerialNumber())) {
					paymentPO1 = (TtAsPaymentPO) dao.select(
							TtAsPaymentPO.class, sql.toString(), null).get(0);
					String type = ""
							+ (Integer.parseInt(paymentPO1.getSerialNumber()) + 1);
					for (int i = type.length(); i < 5; i++) {
						type = "0" + type;
					}
					SERIAL_NUMBER = type;
				} else {
					SERIAL_NUMBER = "00001";
				}

			}

			String TAX_RATE = request.getParamValue("TAX_RATE");
			String PAYMENT_TYPE = request.getParamValue("PAYMENT_TYPE");
			String labour_receipt = request.getParamValue("labour_receipt");
			String part_receipt = request.getParamValue("part_receipt");
			TtAsPaymentPO paymentPO = new TtAsPaymentPO();
			paymentPO.setBalanceOder(balance_oder);
			dao.delete(paymentPO);
			paymentPO.setStatus(Integer.parseInt(status));
			paymentPO.setTaxRate(Long.parseLong(TAX_RATE));
			paymentPO.setPaymentType(Long.parseLong(PAYMENT_TYPE));
			paymentPO.setLabourReceipt(labour_receipt);
			paymentPO.setPartReceipt(part_receipt);
			paymentPO.setSerialNumber(SERIAL_NUMBER);
			dao.insert(paymentPO);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public TtPartBillDefinePO getdealer(String DEALER_ID) {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.BANK, t.ACCOUNT\n");
		sql.append("  from tt_part_bill_define t\n");
		sql.append(" where t.DEALER_ID = " + DEALER_ID + "\n");
		sql.append("   and t.YIELDLY is null");
		List<TtPartBillDefinePO> list = dao.select(TtPartBillDefinePO.class,
				sql.toString(), null);

		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	public void getMessge() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String balance_oder = request.getParamValue("balance_oder");
			String tt = request.getParamValue("tt");
			String TAX_RATE = request.getParamValue("TAX_RATE");
			TtAsPaymentPO asPaymentPO = new TtAsPaymentPO();
			asPaymentPO.setBalanceOder(balance_oder);
			List<TtAsPaymentPO> list = dao.select(asPaymentPO);
			if (list.size() > 0 && tt == null) {
				act.setOutData("mes", 0);
			} else {
				// 根据结算单号读取结算信息
				List<Map<String, Object>> mList = dao.getMessge(balance_oder);
				if (mList.size() == 0) {
					act.setOutData("mes", 1);
				} else {
					Map<String, Object> map = mList.get(0);
					String CREATE_BY = String.valueOf(CommonUtils
							.getDataFromMap(map, "CREATE_BY"));
					TcUserPO userPO = new TcUserPO();
					userPO.setUser_id(Long.parseLong(CREATE_BY));
					List<TcUserPO> uList = dao.select(userPO);
					act.setOutData("userName", uList.get(0).getName());

					String DEALER_ID = String.valueOf(CommonUtils
							.getDataFromMap(map, "DEALER_ID"));
					TtPartBillDefinePO definePO = getdealer(DEALER_ID);

					TmDealerPO dealerPO = new TmDealerPO();
					dealerPO.setDealerId(Long.parseLong(DEALER_ID));
					List<TmDealerPO> dList = dao.select(dealerPO);
					dealerPO = dList.get(0);

					String AMOUNT_SUM = String.valueOf(CommonUtils
							.getDataFromMap(map, "AMOUNT_SUM"));
					String PART_AMOUNT = String.valueOf(CommonUtils
							.getDataFromMap(map, "LABOUR_AMOUNT"));
					if (TAX_RATE.equals("94121002")) {
						AMOUNT_SUM = ""
								+ Arith.add(Arith.mul(Arith.sub(Double
										.parseDouble(AMOUNT_SUM), Double
										.parseDouble(PART_AMOUNT)), 0.9),
										Double.parseDouble(PART_AMOUNT));
					} else if (TAX_RATE.equals("94121003")) {
						AMOUNT_SUM = ""
								+ (Arith.div(Double.parseDouble(AMOUNT_SUM),
										Double.parseDouble("1.17"), 2));
						PART_AMOUNT = ""
								+ (Arith.div(Double.parseDouble(PART_AMOUNT),
										Double.parseDouble("1.17"), 2));
					}

					if (!AMOUNT_SUM.contains(".")) {
						AMOUNT_SUM = AMOUNT_SUM + ".00";
					}
					String asum = AMOUNT_SUM.substring(0, AMOUNT_SUM
							.indexOf("."));
					String asum1 = AMOUNT_SUM
							.substring(AMOUNT_SUM.indexOf(".") + 1);
					char[] r = asum1.toCharArray();
					char[] c = asum.toCharArray();
					int k = 1;
					for (int j = 10; j > c.length; j--) {
						act.setOutData("m" + k, "");
						k++;
					}
					int b = 0;
					for (int i = c.length; i >= 1; i--) {
						if (Double.parseDouble(AMOUNT_SUM) < 0) {
							if (i == c.length) {
								act.setOutData("m" + k, "-");
							} else {
								act.setOutData("m" + k, Integer.parseInt(String
										.valueOf(c[b])));
							}

						} else {
							act.setOutData("m" + k, Integer.parseInt(String
									.valueOf(c[b])));
						}

						k++;
						b++;
					}

					act.setOutData("m11", String.valueOf(r[0]));
					if (r.length > 1) {
						act.setOutData("m12", String.valueOf(r[1]));
					} else {
						act.setOutData("m12", "0");
					}

					String LABOUR_AMOUNT = AMOUNT_SUM;
					if (!LABOUR_AMOUNT.contains(".")) {
						LABOUR_AMOUNT = LABOUR_AMOUNT + ".00";
					}
					String labour = LABOUR_AMOUNT.substring(0, LABOUR_AMOUNT
							.indexOf("."));
					String labour1 = LABOUR_AMOUNT.substring(LABOUR_AMOUNT
							.indexOf(".") + 1);
					char[] t = labour1.toCharArray();
					char[] d = labour.toCharArray();
					k = 1;
					for (int j = 10; j > d.length; j--) {
						act.setOutData("t" + k, "");
						k++;
					}
					b = 0;
					for (int i = d.length; i >= 1; i--) {
						if (Double.parseDouble(LABOUR_AMOUNT) < 0) {
							if (i == d.length) {
								act.setOutData("t" + k, "-");
							} else {
								act.setOutData("t" + k, Integer.parseInt(String
										.valueOf(d[b])));
							}

						} else {
							act.setOutData("t" + k, Integer.parseInt(String
									.valueOf(d[b])));
						}
						k++;
						b++;
					}

					act.setOutData("t11", String.valueOf(t[0]));
					if (t.length > 1) {
						act.setOutData("t12", String.valueOf(t[1]));
					} else {
						act.setOutData("t12", "0");
					}

					if (!PART_AMOUNT.contains(".")) {
						PART_AMOUNT = PART_AMOUNT + ".00";
					}
					String PART = PART_AMOUNT.substring(0, PART_AMOUNT
							.indexOf("."));
					String PART1 = PART_AMOUNT.substring(PART_AMOUNT
							.indexOf(".") + 1);
					char[] pa = PART1.toCharArray();
					char[] pa1 = PART.toCharArray();
					k = 1;
					for (int j = 10; j > pa1.length; j--) {
						act.setOutData("p" + k, "");
						k++;
					}
					b = 0;
					for (int i = pa1.length; i >= 1; i--) {
						if (Double.parseDouble(PART_AMOUNT) < 0) {
							if (i == pa1.length) {
								act.setOutData("p" + k, "-");
							} else {
								act.setOutData("p" + k, Integer.parseInt(String
										.valueOf(pa1[b])));
							}

						} else {
							act.setOutData("p" + k, Integer.parseInt(String
									.valueOf(pa1[b])));
						}
						k++;
						b++;
					}

					act.setOutData("p11", String.valueOf(pa[0]));
					if (pa.length > 1) {
						act.setOutData("p12", String.valueOf(pa[1]));
					} else {
						act.setOutData("p12", "0");
					}

					act.setOutData("map", map);
					act.setOutData("definePO", definePO);
					act.setOutData("dealerPO", dealerPO);

				}
			}

			act.setForword(BALANCE_AUDITING_PAYMENTMANAGEUPDATE);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void getMessgeMf() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String balance_oder = request.getParamValue("balance_oder");
			TtAsPaymentPO paymentPO = new TtAsPaymentPO();
			paymentPO.setBalanceOder(balance_oder);
			List<TtAsPaymentPO> payList = dao.select(paymentPO);
			paymentPO = payList.get(0);
			act.setOutData("paymentPO", paymentPO);

			TtAsPaymentPO asPaymentPO = new TtAsPaymentPO();
			asPaymentPO.setBalanceOder(balance_oder);
			List<TtAsPaymentPO> list = dao.select(asPaymentPO);

			List<Map<String, Object>> mList = dao.getMessge(balance_oder);
			if (mList.size() == 0) {
				act.setOutData("mes", 1);
			} else {
				Map<String, Object> map = mList.get(0);
				String CREATE_BY = String.valueOf(CommonUtils.getDataFromMap(
						map, "CREATE_BY"));
				TcUserPO userPO = new TcUserPO();
				userPO.setUser_id(Long.parseLong(CREATE_BY));
				List<TcUserPO> uList = dao.select(userPO);
				act.setOutData("userName", uList.get(0).getName());

				String DEALER_ID = String.valueOf(CommonUtils.getDataFromMap(
						map, "DEALER_ID"));
				TtPartBillDefinePO definePO = getdealer(DEALER_ID);

				TmDealerPO dealerPO = new TmDealerPO();
				dealerPO.setDealerId(Long.parseLong(DEALER_ID));
				List<TmDealerPO> dList = dao.select(dealerPO);
				dealerPO = dList.get(0);
				String TAX_RATE = "" + paymentPO.getTaxRate();
				String AMOUNT_SUM = String.valueOf(CommonUtils.getDataFromMap(
						map, "AMOUNT_SUM"));
				String PART_AMOUNT = String.valueOf(CommonUtils.getDataFromMap(
						map, "LABOUR_AMOUNT"));
				if (TAX_RATE.equals("94121002")) {
					AMOUNT_SUM = ""
							+ Arith.add(Arith.mul(Arith.sub(Double
									.parseDouble(AMOUNT_SUM), Double
									.parseDouble(PART_AMOUNT)), 0.9), Double
									.parseDouble(PART_AMOUNT));
				} else if (TAX_RATE.equals("94121003")) {
					AMOUNT_SUM = ""
							+ (Arith.div(Double.parseDouble(AMOUNT_SUM), Double
									.parseDouble("1.17"), 2));
					PART_AMOUNT = ""
							+ (Arith.div(Double.parseDouble(PART_AMOUNT),
									Double.parseDouble("1.17"), 2));
				}

				if (!AMOUNT_SUM.contains(".")) {
					AMOUNT_SUM = AMOUNT_SUM + ".00";
				}
				String asum = AMOUNT_SUM.substring(0, AMOUNT_SUM.indexOf("."));
				String asum1 = AMOUNT_SUM
						.substring(AMOUNT_SUM.indexOf(".") + 1);
				char[] r = asum1.toCharArray();
				char[] c = asum.toCharArray();
				int k = 1;
				for (int j = 10; j > c.length; j--) {
					act.setOutData("m" + k, "");
					k++;
				}
				int b = 0;
				for (int i = c.length; i >= 1; i--) {
					if (Double.parseDouble(AMOUNT_SUM) < 0) {
						if (i == c.length) {
							act.setOutData("m" + k, "-");
						} else {
							act.setOutData("m" + k, Integer.parseInt(String
									.valueOf(c[b])));
						}

					} else {
						act.setOutData("m" + k, Integer.parseInt(String
								.valueOf(c[b])));
					}

					k++;
					b++;
				}

				act.setOutData("m11", String.valueOf(r[0]));
				if (r.length > 1) {
					act.setOutData("m12", String.valueOf(r[1]));
				} else {
					act.setOutData("m12", "0");
				}

				String LABOUR_AMOUNT = AMOUNT_SUM;
				if (!LABOUR_AMOUNT.contains(".")) {
					LABOUR_AMOUNT = LABOUR_AMOUNT + ".00";
				}
				String labour = LABOUR_AMOUNT.substring(0, LABOUR_AMOUNT
						.indexOf("."));
				String labour1 = LABOUR_AMOUNT.substring(LABOUR_AMOUNT
						.indexOf(".") + 1);
				char[] t = labour1.toCharArray();
				char[] d = labour.toCharArray();
				k = 1;
				for (int j = 10; j > d.length; j--) {
					act.setOutData("t" + k, "");
					k++;
				}
				b = 0;
				for (int i = d.length; i >= 1; i--) {
					if (Double.parseDouble(LABOUR_AMOUNT) < 0) {
						if (i == d.length) {
							act.setOutData("t" + k, "-");
						} else {
							act.setOutData("t" + k, Integer.parseInt(String
									.valueOf(d[b])));
						}

					} else {
						act.setOutData("t" + k, Integer.parseInt(String
								.valueOf(d[b])));
					}
					k++;
					b++;
				}

				act.setOutData("t11", String.valueOf(t[0]));
				if (t.length > 1) {
					act.setOutData("t12", String.valueOf(t[1]));
				} else {
					act.setOutData("t12", "0");
				}
				if (!PART_AMOUNT.contains(".")) {
					PART_AMOUNT = PART_AMOUNT + ".00";
				}
				String PART = PART_AMOUNT
						.substring(0, PART_AMOUNT.indexOf("."));
				String PART1 = PART_AMOUNT
						.substring(PART_AMOUNT.indexOf(".") + 1);
				char[] pa = PART1.toCharArray();
				char[] pa1 = PART.toCharArray();
				k = 1;
				for (int j = 10; j > pa1.length; j--) {
					act.setOutData("p" + k, "");
					k++;
				}
				b = 0;
				for (int i = pa1.length; i >= 1; i--) {
					if (Double.parseDouble(PART_AMOUNT) < 0) {
						if (i == pa1.length) {
							act.setOutData("p" + k, "-");
						} else {
							act.setOutData("p" + k, Integer.parseInt(String
									.valueOf(pa1[b])));
						}

					} else {
						act.setOutData("p" + k, Integer.parseInt(String
								.valueOf(pa1[b])));
					}
					k++;
					b++;
				}

				act.setOutData("p11", String.valueOf(pa[0]));
				if (pa.length > 1) {
					act.setOutData("p12", String.valueOf(pa[1]));
				} else {
					act.setOutData("p12", "0");
				}

				act.setOutData("map", map);
				act.setOutData("definePO", definePO);
				act.setOutData("dealerPO", dealerPO);

			}
			act.setOutData("balance_yieldly", request
					.getParamValue("balance_yieldly"));
			act.setForword(BALANCE_AUDITING_PAYMENTMANAGEUPDATE);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void getMessgeInFor() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String balance_oder = request.getParamValue("balance_oder");
			TtAsPaymentPO paymentPO = new TtAsPaymentPO();
			paymentPO.setBalanceOder(balance_oder);
			List<TtAsPaymentPO> payList = dao.select(paymentPO);
			paymentPO = payList.get(0);
			act.setOutData("paymentPO", paymentPO);

			TtAsPaymentPO asPaymentPO = new TtAsPaymentPO();
			asPaymentPO.setBalanceOder(balance_oder);
			List<TtAsPaymentPO> list = dao.select(asPaymentPO);

			List<Map<String, Object>> mList = dao.getMessge(balance_oder);
			if (mList.size() == 0) {
				act.setOutData("mes", 1);
			} else {
				Map<String, Object> map = mList.get(0);
				String CREATE_BY = String.valueOf(CommonUtils.getDataFromMap(
						map, "CREATE_BY"));
				TcUserPO userPO = new TcUserPO();
				userPO.setUser_id(Long.parseLong(CREATE_BY));
				List<TcUserPO> uList = dao.select(userPO);
				act.setOutData("userName", uList.get(0).getName());

				String DEALER_ID = String.valueOf(CommonUtils.getDataFromMap(
						map, "DEALER_ID"));
				TtPartBillDefinePO definePO = getdealer(DEALER_ID);

				TmDealerPO dealerPO = new TmDealerPO();
				dealerPO.setDealerId(Long.parseLong(DEALER_ID));
				List<TmDealerPO> dList = dao.select(dealerPO);
				dealerPO = dList.get(0);
				String TAX_RATE = "" + paymentPO.getTaxRate();
				String AMOUNT_SUM = String.valueOf(CommonUtils.getDataFromMap(
						map, "AMOUNT_SUM"));
				String PART_AMOUNT = String.valueOf(CommonUtils.getDataFromMap(
						map, "LABOUR_AMOUNT"));
				if (TAX_RATE.equals("94121002")) {
					AMOUNT_SUM = ""
							+ Arith.add(Arith.mul(Arith.sub(Double
									.parseDouble(AMOUNT_SUM), Double
									.parseDouble(PART_AMOUNT)), 0.9), Double
									.parseDouble(PART_AMOUNT));
				} else if (TAX_RATE.equals("94121003")) {
					AMOUNT_SUM = ""
							+ (Arith.div(Double.parseDouble(AMOUNT_SUM), Double
									.parseDouble("1.17"), 2));
					PART_AMOUNT = ""
							+ (Arith.div(Double.parseDouble(PART_AMOUNT),
									Double.parseDouble("1.17"), 2));
				}
				if (!AMOUNT_SUM.contains(".")) {
					AMOUNT_SUM = AMOUNT_SUM + ".00";
				}

				String asum = AMOUNT_SUM.substring(0, AMOUNT_SUM.indexOf("."));
				String asum1 = AMOUNT_SUM
						.substring(AMOUNT_SUM.indexOf(".") + 1);
				char[] r = asum1.toCharArray();
				char[] c = asum.toCharArray();
				int k = 1;
				for (int j = 10; j > c.length; j--) {
					act.setOutData("m" + k, "");
					k++;
				}
				int b = 0;
				for (int i = c.length; i >= 1; i--) {
					if (Double.parseDouble(AMOUNT_SUM) < 0) {
						if (i == c.length) {
							act.setOutData("m" + k, "-");
						} else {
							act.setOutData("m" + k, Integer.parseInt(String
									.valueOf(c[b])));
						}

					} else {
						act.setOutData("m" + k, Integer.parseInt(String
								.valueOf(c[b])));
					}

					k++;
					b++;
				}

				act.setOutData("m11", String.valueOf(r[0]));
				if (r.length > 1) {
					act.setOutData("m12", String.valueOf(r[1]));
				} else {
					act.setOutData("m12", "0");
				}

				String LABOUR_AMOUNT = AMOUNT_SUM;
				if (!LABOUR_AMOUNT.contains(".")) {
					LABOUR_AMOUNT = LABOUR_AMOUNT + ".00";
				}
				String labour = LABOUR_AMOUNT.substring(0, LABOUR_AMOUNT
						.indexOf("."));
				String labour1 = LABOUR_AMOUNT.substring(LABOUR_AMOUNT
						.indexOf(".") + 1);
				char[] t = labour1.toCharArray();
				char[] d = labour.toCharArray();
				k = 1;
				for (int j = 10; j > d.length; j--) {
					act.setOutData("t" + k, "");
					k++;
				}
				b = 0;
				for (int i = d.length; i >= 1; i--) {
					if (Double.parseDouble(LABOUR_AMOUNT) < 0) {
						if (i == d.length) {
							act.setOutData("t" + k, "-");
						} else {
							act.setOutData("t" + k, Integer.parseInt(String
									.valueOf(d[b])));
						}

					} else {
						act.setOutData("t" + k, Integer.parseInt(String
								.valueOf(d[b])));
					}
					k++;
					b++;
				}

				act.setOutData("t11", String.valueOf(t[0]));
				if (t.length > 1) {
					act.setOutData("t12", String.valueOf(t[1]));
				} else {
					act.setOutData("t12", "0");
				}

				if (!PART_AMOUNT.contains(".")) {
					PART_AMOUNT = PART_AMOUNT + ".00";
				}
				String PART = PART_AMOUNT
						.substring(0, PART_AMOUNT.indexOf("."));
				String PART1 = PART_AMOUNT
						.substring(PART_AMOUNT.indexOf(".") + 1);
				char[] pa = PART1.toCharArray();
				char[] pa1 = PART.toCharArray();
				k = 1;
				for (int j = 10; j > pa1.length; j--) {
					act.setOutData("p" + k, "");
					k++;
				}
				b = 0;
				for (int i = pa1.length; i >= 1; i--) {
					if (Double.parseDouble(PART_AMOUNT) < 0) {
						if (i == pa1.length) {
							act.setOutData("p" + k, "-");
						} else {
							act.setOutData("p" + k, Integer.parseInt(String
									.valueOf(pa1[b])));
						}

					} else {
						act.setOutData("p" + k, Integer.parseInt(String
								.valueOf(pa1[b])));
					}
					k++;
					b++;
				}

				act.setOutData("p11", String.valueOf(pa[0]));
				if (pa.length > 1) {
					act.setOutData("p12", String.valueOf(pa[1]));
				} else {
					act.setOutData("p12", "0");
				}

				act.setOutData("map", map);
				act.setOutData("definePO", definePO);
				act.setOutData("dealerPO", dealerPO);

			}
			act.setForword(BALANCE_AUDITING_PAYMENTMANAGEINFOR);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 付款凭证打印
	 */
	public void getMessgeInPrint() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String balance_oder = request.getParamValue("balance_oder");
			TtAsPaymentPO paymentPO3 = new TtAsPaymentPO();
			paymentPO3.setBalanceOder(balance_oder);
			List<TtAsPaymentPO> poList = dao.select(paymentPO3);
			if (poList.get(0).getCreatDate() == null) {
				TtAsPaymentPO paymentPO1 = new TtAsPaymentPO();
				paymentPO1.setBalanceOder(balance_oder);
				TtAsPaymentPO paymentPO2 = new TtAsPaymentPO();
				paymentPO2.setCreatDate(new Date());
				dao.update(paymentPO1, paymentPO2);
			}

			TtAsPaymentPO paymentPO = new TtAsPaymentPO();
			paymentPO.setBalanceOder(balance_oder);
			List<TtAsPaymentPO> payList = dao.select(paymentPO);
			paymentPO = payList.get(0);
			act.setOutData("paymentPO", paymentPO);
			act.setOutData("CreatDate", Utility.handleDate2(paymentPO
					.getCreatDate()));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(new Date());
			act.setOutData("date", date);

			TtAsPaymentPO asPaymentPO = new TtAsPaymentPO();
			asPaymentPO.setBalanceOder(balance_oder);
			List<TtAsPaymentPO> list = dao.select(asPaymentPO);

			List<Map<String, Object>> mList = dao.getMessge(balance_oder);
			if (mList.size() == 0) {
				act.setOutData("mes", 1);
			} else {
				Map<String, Object> map = mList.get(0);
				String REMARK = String.valueOf(CommonUtils.getDataFromMap(map,
						"REMARK"));
				TtAsPaymentPO paymentPO2 = new TtAsPaymentPO();
				paymentPO2.setBalanceOder(REMARK);
				List<TtAsPaymentPO> payList1 = dao.select(paymentPO2);
				// String NO = "0"+REMARK.substring(REMARK.length() -4);
				act.setOutData("NO", payList1.get(0).getSerialNumber());

				String CREATE_BY = String.valueOf(CommonUtils.getDataFromMap(
						map, "CREATE_BY"));
				TcUserPO userPO = new TcUserPO();
				userPO.setUser_id(Long.parseLong(CREATE_BY));
				List<TcUserPO> uList = dao.select(userPO);
				act.setOutData("userName", uList.get(0).getName());

				String DEALER_ID = String.valueOf(CommonUtils.getDataFromMap(
						map, "DEALER_ID"));
				TtPartBillDefinePO definePO = getdealer(DEALER_ID);

				String sql = "select B.REGION_NAME from tm_dealer A ,tm_region B where A.PROVINCE_ID = B.REGION_CODE AND A.DEALER_ID =  "
						+ DEALER_ID;
				List<TmRegionPO> renList = dao.select(TmRegionPO.class, sql,
						null);
				if (renList.size() > 0) {
					act.setOutData("REGION_NAME", renList.get(0)
							.getRegionName());
				}
				TmDealerPO dealerPO = new TmDealerPO();
				dealerPO.setDealerId(Long.parseLong(DEALER_ID));
				List<TmDealerPO> dList = dao.select(dealerPO);
				dealerPO = dList.get(0);

				String TAX_RATE = "" + paymentPO.getTaxRate();
				String AMOUNT_SUM = String.valueOf(CommonUtils.getDataFromMap(
						map, "AMOUNT_SUM"));
				String PART_AMOUNT = String.valueOf(CommonUtils.getDataFromMap(
						map, "LABOUR_AMOUNT"));
				if (TAX_RATE.equals("94121002")) {
					AMOUNT_SUM = ""
							+ Arith.add(Arith.mul(Arith.sub(Double
									.parseDouble(AMOUNT_SUM), Double
									.parseDouble(PART_AMOUNT)), 0.9), Double
									.parseDouble(PART_AMOUNT));
				} else if (TAX_RATE.equals("94121003")) {
					AMOUNT_SUM = ""
							+ (Arith.div(Double.parseDouble(AMOUNT_SUM), Double
									.parseDouble("1.17"), 2));
					PART_AMOUNT = ""
							+ (Arith.div(Double.parseDouble(PART_AMOUNT),
									Double.parseDouble("1.17"), 2));
				}
				if (!AMOUNT_SUM.contains(".")) {
					AMOUNT_SUM = AMOUNT_SUM + ".00";
				}

				String asum = AMOUNT_SUM.substring(0, AMOUNT_SUM.indexOf("."));
				String asum1 = AMOUNT_SUM
						.substring(AMOUNT_SUM.indexOf(".") + 1);
				char[] r = asum1.toCharArray();
				char[] c = asum.toCharArray();
				int k = 1;
				for (int j = 10; j > c.length; j--) {
					act.setOutData("m" + k, "");
					k++;
				}
				int b = 0;
				for (int i = c.length; i >= 1; i--) {
					if (Double.parseDouble(AMOUNT_SUM) < 0) {
						if (i == c.length) {
							act.setOutData("m" + k, "-");
						} else {
							act.setOutData("m" + k, Integer.parseInt(String
									.valueOf(c[b])));
						}

					} else {
						act.setOutData("m" + k, Integer.parseInt(String
								.valueOf(c[b])));
					}

					k++;
					b++;
				}

				act.setOutData("m11", String.valueOf(r[0]));
				if (r.length > 1) {
					act.setOutData("m12", String.valueOf(r[1]));
				} else {
					act.setOutData("m12", "0");
				}

				String LABOUR_AMOUNT = AMOUNT_SUM;
				if (!LABOUR_AMOUNT.contains(".")) {
					LABOUR_AMOUNT = LABOUR_AMOUNT + ".00";
				}

				String labour = LABOUR_AMOUNT.substring(0, LABOUR_AMOUNT
						.indexOf("."));
				String labour1 = LABOUR_AMOUNT.substring(LABOUR_AMOUNT
						.indexOf(".") + 1);
				char[] t = labour1.toCharArray();
				char[] d = labour.toCharArray();
				k = 1;
				for (int j = 10; j > d.length; j--) {
					act.setOutData("t" + k, "");
					k++;
				}
				b = 0;
				for (int i = d.length; i >= 1; i--) {
					if (Double.parseDouble(LABOUR_AMOUNT) < 0) {
						if (i == d.length) {
							act.setOutData("t" + k, "-");
						} else {
							act.setOutData("t" + k, Integer.parseInt(String
									.valueOf(d[b])));
						}

					} else {
						act.setOutData("t" + k, Integer.parseInt(String
								.valueOf(d[b])));
					}
					k++;
					b++;
				}

				act.setOutData("t11", String.valueOf(t[0]));
				if (t.length > 1) {
					act.setOutData("t12", String.valueOf(t[1]));
				} else {
					act.setOutData("t12", "0");
				}

				if (!PART_AMOUNT.contains(".")) {
					PART_AMOUNT = PART_AMOUNT + ".00";
				}
				String PART = PART_AMOUNT
						.substring(0, PART_AMOUNT.indexOf("."));
				String PART1 = PART_AMOUNT
						.substring(PART_AMOUNT.indexOf(".") + 1);
				char[] pa = PART1.toCharArray();
				char[] pa1 = PART.toCharArray();
				k = 1;
				for (int j = 10; j > pa1.length; j--) {
					act.setOutData("p" + k, "");
					k++;
				}
				b = 0;
				for (int i = pa1.length; i >= 1; i--) {
					if (Double.parseDouble(PART_AMOUNT) < 0) {
						if (i == pa1.length) {
							act.setOutData("p" + k, "-");
						} else {
							act.setOutData("p" + k, Integer.parseInt(String
									.valueOf(pa1[b])));
						}

					} else {
						act.setOutData("p" + k, Integer.parseInt(String
								.valueOf(pa1[b])));
					}
					k++;
					b++;
				}

				act.setOutData("p11", String.valueOf(pa[0]));
				if (pa.length > 1) {
					act.setOutData("p12", String.valueOf(pa[1]));
				} else {
					act.setOutData("p12", "0");
				}

				act.setOutData("map", map);
				act.setOutData("definePO", definePO);
				act.setOutData("dealerPO", dealerPO);

			}
			act.setForword(BALANCE_AUDITING_PAYMENTMANAGEINPRINT);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void ticketIntChanghe() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setOutData("balance_yieldly", Constant.PART_IS_CHANGHE_01);
			act.setForword(this.BALANCE_AUDITING_TICKETS);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void tickteview() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			TtAsWrTicketsPO ticketsPO = new TtAsWrTicketsPO();
			ticketsPO.setId(Long.parseLong(id));

			act.setOutData("ticketsPO", dao.select(ticketsPO).get(0));
			act.setForword(this.BALANCE_AUDITING_TICKETSVW);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void delettick() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			TtAsWrTicketsPO ticketsPO = new TtAsWrTicketsPO();
			ticketsPO.setId(Long.parseLong(id));
			List<TtAsWrTicketsPO> list = dao.select(ticketsPO);
			ticketsPO = list.get(0);
			String goodsnum = ticketsPO.getGoodsnum();
			String date = goodsnum.substring(goodsnum.length() - 6);
			date = date.substring(0, 4) + "-" + date.substring(4);
			long dealerid = ticketsPO.getDealerid();
			long balanceYieldly = ticketsPO.getBalanceYieldly();
			StringBuffer sql = new StringBuffer();
			sql.append("select t.REPORT_DATE, t.STATUS\n");
			sql.append("  from tt_as_wr_application t\n");
			sql.append(" where t.BALANCE_YIELDLY =" + balanceYieldly + " \n");
			sql.append("   and to_char(t.REPORT_DATE, 'yyyy-mm') ='" + date
					+ "'\n");
			sql.append("   and t.DEALER_ID=" + dealerid);
			sql
					.append("   and t.CAMPAIGN_CODE in (SELECT a.ACTIVITY_CODE from TT_AS_ACTIVITY a ,TT_AS_ACTIVITY_SUBJECT b where a.SUBJECT_ID = b.SUBJECT_ID and  b.ACTIVITY_TYPE = 10561001 ) ");
			sql
					.append(" and t.STATUS in (10791011,10791013,10791014,10791016) ");
			if (dao.select(TtAsWrApplicationPO.class, sql.toString(), null)
					.size() > 0) {
				act.setOutData("meg", "no");
			} else {
				TtAsWrTicketsPO ticketsPO1 = new TtAsWrTicketsPO();
				ticketsPO1.setId(Long.parseLong(id));
				dao.delete(ticketsPO1);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void tickteprint() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			if (id != null) {
				TtAsWrTicketsPO ticketsPO = new TtAsWrTicketsPO();
				ticketsPO.setId(Long.parseLong(id));
				act.setOutData("ticketsPO", dao.select(ticketsPO).get(0));
				act.setOutData("ticketsList", dao.select(ticketsPO));
			} else {
				String ids = request.getParamValue("ids");
				String balance_yieldly = request
						.getParamValue("balance_yieldly");

				StringBuffer sql = new StringBuffer();
				sql.append("select sum(t.CARRIAGE) CARRIAGE,\n");
				sql.append("       sum(t.DA_CARRIAGE) DA_CARRIAGE,\n");
				sql.append("       max(t.DEALERNAME) DEALERNAME,\n");
				sql.append("       max(t.LETTER) LETTER,\n");
				sql.append("       max (t.STARTDATE) STARTDATE ,\n");
				sql.append("       max (t.ENDDATE) ENDDATE ,\n");
				sql.append("       max (t.LETTERSF)  LETTERSF,\n");
				sql
						.append("       max (t.BALANCE_YIELDLY)  BALANCE_YIELDLY \n");
				sql.append("  from tt_as_wr_tickets t\n");
				sql.append(" where t.ID in  ("
						+ ids.substring(0, ids.length() - 1) + ")\n");
				sql.append("   and t.BALANCE_YIELDLY = " + balance_yieldly);
				act.setOutData("ticketsPO", dao.select(TtAsWrTicketsPO.class,
						sql.toString(), null).get(0));
				sql = new StringBuffer();
				sql.append("select t.GOODSNUM ,\n");
				sql.append("       t.APLCOUNT ,\n");
				sql.append("       t.CLAIM_TYPE  ,\n");
				sql.append("       t.NUMBER_AP\n");
				sql.append("  from tt_as_wr_tickets t\n");
				sql.append(" where t.ID in ("
						+ ids.substring(0, ids.length() - 1) + ")\n");
				sql.append("   and t.BALANCE_YIELDLY = " + balance_yieldly);
				act.setOutData("ticketsList", dao.select(TtAsWrTicketsPO.class,
						sql.toString(), null));

			}

			act.setForword(this.BALANCE_AUDITING_TICKETSPT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void quyertickets() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String balance_yieldly = request.getParamValue("balance_yieldly");
			String dealerName = request.getParamValue("dealerName");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			Integer curPage = request.getParamValue("curPage") == null ? 1
					: Integer.parseInt(request.getParamValue("curPage"));// 分页首页代码
			Long UserId = logonUser.getUserId();
			PageResult<Map<String, Object>> ps = dao.querytickets(
					balance_yieldly, dealerName, startDate, endDate, ""
							+ UserId, logonUser, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void addtickets() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String balance_yieldly = request.getParamValue("balance_yieldly");
			act.setOutData("balance_yieldly", balance_yieldly);
			if (balance_yieldly.equals("95411001")) {
				act.setForword(this.BALANCE_AUDITING_ADDTICKETS);
			} else {
				act.setForword(this.BALANCE_AUDITING_ADDTICKETS_DONGAN);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void addwrtickets() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {

			RequestWrapper request = act.getRequest();
			String balance_yieldly = request.getParamValue("balance_yieldly");
			String dealerid = request.getParamValue("dealerid");
			String carriage = request.getParamValue("carriage");
			String sumcarriage = request.getParamValue("sumcarriage");
			String dacarriage = request.getParamValue("dacarriage");

			String dealerName = request.getParamValue("dealerName");
			String letter = request.getParamValue("letter");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			String lettersf = request.getParamValue("lettersf");
			String[] goodsnum = request.getParamValues("goodsnum");
			String[] aplcount = request.getParamValues("aplcount");
			String[] number = request.getParamValues("number");
			String[] claim_type = request.getParamValues("claim_type");
			boolean fal = true;
			if (fal) {
				TtAsWrTicketsPO asWrTicketsPO = new TtAsWrTicketsPO();
				for (int i = 0; i < goodsnum.length; i++) {
					String date = goodsnum[i]
							.substring(goodsnum[i].length() - 6);
					asWrTicketsPO.setGoodsnum(goodsnum[i]);
					asWrTicketsPO.setBalanceYieldly(Long
							.parseLong(balance_yieldly));
					if (dao.select(asWrTicketsPO).size() > 0) {
						act.setOutData("ret", "false");
						break;
					} else {
						asWrTicketsPO.setId(Long.parseLong(SequenceManager
								.getSequence("")));
						asWrTicketsPO.setBalanceYieldly(Long
								.parseLong(balance_yieldly));
						asWrTicketsPO.setDealerid(Long.parseLong(dealerid));

						asWrTicketsPO.setDealername(dealerName);
						asWrTicketsPO.setLetter(Long.parseLong(letter));
						asWrTicketsPO.setStartdate(Utility
								.getDate(startDate, 1));
						asWrTicketsPO.setEnddate(Utility.getDate(endDate, 1));
						asWrTicketsPO.setLettersf(Long.parseLong(lettersf));
						asWrTicketsPO.setGoodsnum(goodsnum[i]);
						asWrTicketsPO.setNumberAp(dao.number_ap(logonUser
								.getActn()));
						asWrTicketsPO
								.setAplcount(Integer.parseInt(aplcount[i]));
						asWrTicketsPO.setClaimType(claim_type[i]);
						if (i == 0) {
							asWrTicketsPO.setCarriage(Double
									.parseDouble(carriage));
							asWrTicketsPO.setSumCarriage(Double
									.parseDouble(sumcarriage));
							asWrTicketsPO.setDaCarriage(Double
									.parseDouble(dacarriage));
							dao.insert(asWrTicketsPO);
							dao.updateaplcount(date, dealerid, balance_yieldly);
							act.setOutData("ret", "true");
						} else {
							asWrTicketsPO.setCarriage(Double.parseDouble("0"));
							asWrTicketsPO.setSumCarriage(Double
									.parseDouble("0"));
							asWrTicketsPO
									.setDaCarriage(Double.parseDouble("0"));
							dao.insert(asWrTicketsPO);
							dao.updateaplcount(date, dealerid, balance_yieldly);
						}

					}
				}
			}

		} catch (Exception e) {
			act.setOutData("ret", "false");
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void numapplent() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String balance_yieldly = request.getParamValue("balance_yieldly");
			String goodsnum = request.getParamValue("goolname");

			String sumcarriage = request.getParamValue("sumcarriage");
			String[] goodsnums = request.getParamValues("goodsnum");

			String dealercode = goodsnum.substring(0, goodsnum.length() - 6);
			String date = goodsnum.substring(goodsnum.length() - 6);

			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerCode(dealercode);
			List<TmDealerPO> list = dao.select(dealerPO);
			if (list.size() > 0) {
				TcUserPO tcUserPO = new TcUserPO();
				tcUserPO.setUserId(logonUser.getUserId());
				act.setOutData("number", "编号在新增后自动生成");
				dealerPO = list.get(0);
				act.setOutData("dealerName", dealerPO.getDealerName());
				act.setOutData("dealerid", dealerPO.getDealerId());
				String aplcount = dao.aplcount(date, ""
						+ dealerPO.getDealerId(), balance_yieldly);
				act.setOutData("aplcount", aplcount);
				Double[] yieldlysum = dao.getyieldly(""
						+ dealerPO.getDealerId(), sumcarriage, goodsnums);
				act.setOutData("carriage", yieldlysum[0]);
				act.setOutData("dacarriage", yieldlysum[1]);
				if (aplcount.equals("0")) {
					act.setOutData("jude", 0);
				} else {
					act.setOutData("jude", 1);
				}

			} else {
				act.setOutData("jude", 0);
			}

			act.setForword(this.BALANCE_AUDITING_ADDTICKETS);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void numapplentda() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String balance_yieldly = request.getParamValue("balance_yieldly");
			String goodsnum = request.getParamValue("goolname");

			String sumcarriage = request.getParamValue("sumcarriage");
			String[] goodsnums = request.getParamValues("goodsnum");

			String dealercode = goodsnum.substring(0, goodsnum.length() - 6);
			String date = goodsnum.substring(goodsnum.length() - 6);

			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerCode(dealercode);
			List<TmDealerPO> list = dao.select(dealerPO);
			if (list.size() > 0) {
				TcUserPO tcUserPO = new TcUserPO();
				tcUserPO.setUserId(logonUser.getUserId());
				act.setOutData("number", dao.number_ap(((TcUserPO) dao.select(
						tcUserPO).get(0)).getAcnt()));
				dealerPO = list.get(0);
				act.setOutData("dealerName", dealerPO.getDealerName());
				act.setOutData("dealerid", dealerPO.getDealerId());
				String aplcount = dao.aplcount(date, ""
						+ dealerPO.getDealerId(), balance_yieldly);
				act.setOutData("aplcount", aplcount);

				StringBuffer sql = new StringBuffer();
				sql
						.append("select to_char(t.STARTDATE,'yyyy-mm-dd') STARTDATE ,to_char(t.ENDDATE,'yyyy-mm-dd')  ENDDATE from tt_as_wr_tickets t where t.GOODSNUM ='"
								+ goodsnums[0] + "'");
				List<Map<String, Object>> listmap = dao.pageQuery(sql
						.toString(), null, dao.getFunName());
				if (listmap.size() > 0) {
					Map<String, Object> map = listmap.get(0);
					act.setOutData("startDate", map.get("STARTDATE"));
					act.setOutData("endDate", map.get("ENDDATE"));
				}

				Double[] yieldlysum = dao.gettickets(goodsnums);

				act.setOutData("carriage", yieldlysum[0]);
				act.setOutData("sumcarriage", yieldlysum[1]);
				act.setOutData("dacarriage", yieldlysum[2]);
				if (aplcount.equals("0")) {
					act.setOutData("jude", 0);
				} else {
					act.setOutData("jude", 1);
				}

			} else {
				act.setOutData("jude", 0);
			}

			act.setForword(this.BALANCE_AUDITING_ADDTICKETS);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void delnumapplent() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String balance_yieldly = request.getParamValue("balance_yieldly");
			String sumcarriage = request.getParamValue("sumcarriage");
			String[] goodsnums = request.getParamValues("goodsnum");
			String dealerid = request.getParamValue("dealerid");
			Double[] yieldlysum = dao.getyieldly(dealerid, sumcarriage,
					goodsnums);
			act.setOutData("carriage", yieldlysum[0]);
			act.setOutData("dacarriage", yieldlysum[1]);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void ticketIntanglia() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 取得该用户拥有的产地权限
			act.setOutData("balance_yieldly", Constant.PART_IS_CHANGHE_02);
			act.setForword(this.BALANCE_AUDITING_TICKETS);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 索赔申请审核作业 初始化
	 */
	public void claimManualAuditingInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String sql = "SELECT * FROM tm_org WHERE org_level = 2";
			List<Map<String, Object>> org = dao.pageQuery01(sql, null, dao
					.getFunName());
			act.setOutData("org", org);
			request.setAttribute("poseId", loginUser.getPoseId());
			act.setForword(this.MANUAL_AUDITING);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * Iverson add with 2010-11-15 索赔单删除审核初始化
	 */
	public void deleteClaimManualAuditingInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUser
					.getPoseId());
			act.setOutData("yieldly", yieldly);
			act.setForword(this.DEL_MANUAL_AUDITING);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void claimManualAuditingForInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUser
					.getPoseId());
			act.setOutData("yieldly", yieldly);
			act.setForword(this.DEL_MANUAL_FOR_AUDITING);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void claimManualAuditingQuery() {
		Map<String, String> map = new HashMap<String, String>();
		ClaimManualAuditingDao maDao = new ClaimManualAuditingDao();
		try {
			// 取得查询参数
			RequestWrapper request = act.getRequest();
			String dealerId = request.getParamValue("dealerId");// 经销商代码（多个）
			String poseId = request.getParamValue("pose_id");
			String dealerName = request.getParamValue("DEALER_NAME");// 经销商名称
			String roNo = request.getParamValue("CON_RO_NO");// 工单号
			String claimType = request.getParamValue("CLAIM_TYPE");// 索赔类型
			String vin = request.getParamValue("CON_VIN");// 车辆唯一标识码
			String applyStartDate = request
					.getParamValue("CON_APPLY_DATE_START");// 申请日期范围（开始时间）
			String applyEndDate = request.getParamValue("CON_APPLY_DATE_END");// 申请日期范围（结束时间）
			String claimNo = request.getParamValue("CLAIM_NO");// 索赔申请单号

			String STATUS = request.getParamValue("STATUS");// 索赔申请单号
			String orgId = request.getParamValue("ORG_ID");// 大区
			map.put("dealerId", dealerId);
			map.put("dealerName", dealerName);
			map.put("roNo", roNo);
			map.put("claimType", claimType);
			map.put("vin", vin);
			map.put("applyStartDate", applyStartDate);
			map.put("applyEndDate", applyEndDate);
			map.put("claimNo", claimNo);

			map.put("STATUS", STATUS);

			map.put("orgId", orgId);
			map.put("pose_id", poseId);

			request.setAttribute("dealerId", dealerId);
			request.setAttribute("CON_APPLY_DATE_START", applyStartDate);
			request.setAttribute("CON_APPLY_DATE_END", applyEndDate);

			PageResult<Map<String, Object>> result = maDao.queryClaimAudit1(
					map, getCurrPage(), ActionUtil.getPageSize(request));

			act.setOutData("ps", result);
			// 自定义每页大小 by chenyub@yonyou.com
			ActionUtil.setCustomPageSizeFlag(act, true);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔申请单上报");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void claimManualAuditingForQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Map<String, String> map = new HashMap<String, String>();
		ClaimManualAuditingDao maDao = new ClaimManualAuditingDao();
		try {
			// 取得查询参数
			RequestWrapper request = act.getRequest();
			String dealerId = request.getParamValue("dealerId");// 经销商代码（多个）
			String dealerName = request.getParamValue("DEALER_NAME");// 经销商名称
			String roNo = request.getParamValue("CON_RO_NO");// 工单号
			String claimType = request.getParamValue("CLAIM_TYPE");// 索赔类型
			String vin = request.getParamValue("CON_VIN");// 车辆唯一标识码
			String partCode = request.getParamValue("partCode");// 零件代码
			String wrLabourCode = request.getParamValue("wrLabourCode");// 作业代码
			String applyStartDate = request
					.getParamValue("CON_APPLY_DATE_START");// 申请日期范围（开始时间）
			String applyEndDate = request.getParamValue("CON_APPLY_DATE_END");// 申请日期范围（结束时间）
			String claimNo = request.getParamValue("CLAIM_NO");// 索赔申请单号
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			map.put("dealerId", dealerId);
			map.put("dealerName", dealerName);
			map.put("roNo", roNo);
			map.put("claimType", claimType);
			map.put("vin", vin);
			map.put("applyStartDate", applyStartDate);
			map.put("applyEndDate", applyEndDate);
			map.put("claimNo", claimNo);
			// 增加零件代码，作业代码条件查询
			map.put("partCode", partCode);
			map.put("wrLabourCode", wrLabourCode);

			request.setAttribute("dealerId", dealerId);
			request.setAttribute("CON_APPLY_DATE_START", applyStartDate);
			request.setAttribute("CON_APPLY_DATE_END", applyEndDate);

			PageResult<Map<String, Object>> result = maDao.queryClaimAudit2(
					map, curPage, Constant.PAGE_SIZE);

			act.setOutData("ps", result);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔申请单上报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 查询出经销商端申请过来的删除索赔单
	 */
	public void deleteClaimManualAuditingQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		ClaimManualAuditingDao maDao = new ClaimManualAuditingDao();
		try {
			// 取得查询参数
			RequestWrapper request = act.getRequest();
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码（多个）
			String dealerName = request.getParamValue("DEALER_NAME");// 经销商名称
			String roNo = request.getParamValue("CON_RO_NO");// 工单号
			String lineNo = request.getParamValue("CON_LINE_NO");// 行号
			String claimType = request.getParamValue("CLAIM_TYPE");// 索赔类型
			String vin = request.getParamValue("CON_VIN");// 车辆唯一标识码
			String applyStartDate = request
					.getParamValue("CON_APPLY_DATE_START");// 申请日期范围（开始时间）
			String applyEndDate = request.getParamValue("CON_APPLY_DATE_END");// 申请日期范围（结束时间）
			String claimStatus = Constant.CLAIM_APPLY_ORD_TYPE_03.toString();// 状态
																				// 固定为
																				// "审核中"
			String claimNo = request.getParamValue("CLAIM_NO");// 索赔申请单号
			String yieldly = request.getParamValue("YIELDLY");// 查询条件--产地
			String province = request.getParamValue("province");// 省份CODE
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);// 用户所属公司
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUser
					.getPoseId());// 该用户拥有的产地权限

			Long orgId = logonUser.getOrgId();// 组织ID
			Long poseId = logonUser.getPoseId();// 职位ID
			// 查询职位中业务范围
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);

			Long userId = logonUser.getUserId();
			TcUserPO userPO = maDao.queryUserById(userId);

			Integer count = maDao.getUserRegionCount(userId);

			String authCode = "";
			if (userPO.getApprovalLevelCode() != null)
				authCode = userPO.getApprovalLevelCode().toString();// 结算授权级别代码

			Integer curPage = request.getParamValue("curPage") == null ? 1
					: Integer.parseInt(request.getParamValue("curPage"));// 分页首页代码

			if (Utility.testString(applyStartDate))
				applyStartDate = applyStartDate + " 00:00:00";

			if (Utility.testString(applyEndDate))
				applyEndDate = applyEndDate + " 23:59:59";

			ClaimOrderBean orderBean = new ClaimOrderBean();
			orderBean.setDealerCodes(dealerCode);
			orderBean.setDealerName(dealerName);
			orderBean.setRoNo(roNo);
			orderBean.setLineNo(lineNo);
			orderBean.setClaimType(claimType);
			orderBean.setVin(vin);
			orderBean.setApplyEndDate(applyEndDate);
			orderBean.setApplyStartDate(applyStartDate);
			orderBean.setClaimStatus(claimStatus);
			orderBean.setAuthCode(authCode);
			orderBean.setClaimNo(claimNo);
			orderBean.setCompanyId(companyId);
			orderBean.setPoseId(poseId);
			orderBean.setOrgId(orgId);
			orderBean.setAreaIds(CommonUtils.checkNull(areaIds));
			orderBean.setYieldly(yieldly);
			orderBean.setYieldlys(yieldlys);
			orderBean.setProvinceCode(province);
			orderBean.setUserId(String.valueOf(userId));
			PageResult<Map<String, Object>> result = null;
			// if(count==0){
			// result =
			// maDao.queryClaimAuditDelete(orderBean,curPage,Constant.PAGE_SIZE);
			// }else{
			result = maDao.queryClaimAuditDelete1(orderBean, curPage,
					Constant.PAGE_SIZE);
			// }
			// 查询索赔申请单

			act.setOutData("ps", result);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔申请单上报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 查询出经销商端申请过来的删除索赔单
	 */
	public void deleteClaimManualAuditingForQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		ClaimManualAuditingDao maDao = new ClaimManualAuditingDao();
		try {
			// 取得查询参数
			RequestWrapper request = act.getRequest();
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码（多个）
			String dealerName = request.getParamValue("DEALER_NAME");// 经销商名称
			String roNo = request.getParamValue("CON_RO_NO");// 工单号
			String lineNo = request.getParamValue("CON_LINE_NO");// 行号
			String claimType = request.getParamValue("CLAIM_TYPE");// 索赔类型
			String vin = request.getParamValue("CON_VIN");// 车辆唯一标识码
			String applyStartDate = request
					.getParamValue("CON_APPLY_DATE_START");// 申请日期范围（开始时间）
			String applyEndDate = request.getParamValue("CON_APPLY_DATE_END");// 申请日期范围（结束时间）
			String claimStatus = Constant.CLAIM_APPLY_ORD_TYPE_03.toString();// 状态
																				// 固定为
																				// "审核中"
			String claimNo = request.getParamValue("CLAIM_NO");// 索赔申请单号
			String yieldly = request.getParamValue("YIELDLY");// 查询条件--产地
			String province = request.getParamValue("province");// 省份CODE
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);// 用户所属公司
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUser
					.getPoseId());// 该用户拥有的产地权限

			Long orgId = logonUser.getOrgId();// 组织ID
			Long poseId = logonUser.getPoseId();// 职位ID
			// 查询职位中业务范围
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);

			Long userId = logonUser.getUserId();
			TcUserPO userPO = maDao.queryUserById(userId);

			Integer count = maDao.getUserRegionCount(userId);

			String authCode = "";
			if (userPO.getApprovalLevelCode() != null)
				authCode = userPO.getApprovalLevelCode().toString();// 结算授权级别代码

			Integer curPage = request.getParamValue("curPage") == null ? 1
					: Integer.parseInt(request.getParamValue("curPage"));// 分页首页代码

			if (Utility.testString(applyStartDate))
				applyStartDate = applyStartDate + " 00:00:00";

			if (Utility.testString(applyEndDate))
				applyEndDate = applyEndDate + " 23:59:59";

			ClaimOrderBean orderBean = new ClaimOrderBean();
			orderBean.setDealerCodes(dealerCode);
			orderBean.setDealerName(dealerName);
			orderBean.setRoNo(roNo);
			orderBean.setLineNo(lineNo);
			orderBean.setClaimType(claimType);
			orderBean.setVin(vin);
			orderBean.setApplyEndDate(applyEndDate);
			orderBean.setApplyStartDate(applyStartDate);
			orderBean.setClaimStatus(claimStatus);
			orderBean.setAuthCode(authCode);
			orderBean.setClaimNo(claimNo);
			orderBean.setCompanyId(companyId);
			orderBean.setPoseId(poseId);
			orderBean.setOrgId(orgId);
			orderBean.setAreaIds(CommonUtils.checkNull(areaIds));
			orderBean.setYieldly(yieldly);
			orderBean.setYieldlys(yieldlys);
			orderBean.setProvinceCode(province);
			orderBean.setUserId(String.valueOf(userId));
			PageResult<Map<String, Object>> result = null;
			// if(count==0){
			// result =
			// maDao.queryClaimAuditDelete(orderBean,curPage,Constant.PAGE_SIZE);
			// }else{
			result = maDao.queryClaimAuditDelete2(orderBean, curPage,
					Constant.PAGE_SIZE);
			// }
			// 查询索赔申请单

			act.setOutData("ps", result);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔申请单上报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 审核索赔工单 注：该审核暂时屏蔽，新审核采用技术审核和结算室审核
	 * 
	 * <pre>
	 * 步骤：
	 * 1、更新索赔之配件信息（结算金额）
	 * 2、更新索赔之工时信息（结算金额）
	 * 3、更新索赔之其他项目信息（结算金额）
	 * 4、记录审核授权状态过程
	 * 5、更新索赔申请单授权，将审核授权级别中审核完成的去掉
	 * 6、更新索赔申请单状态、下一步需要审核的级别和结算总金额
	 * </pre>
	 */
	@Deprecated
	public void manualAuditing() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		try {
			RequestWrapper request = act.getRequest();
			Long userId = logonUser.getUserId();// 登陆用户ID
			String claimId = request.getParamValue("claimId");// 索赔申请单ID
			String status = request.getParamValue("status");// 审核状态(审核通过、审核退回、审核拒绝)
			String partIds[] = request.getParamValues("PART_ID");// 索赔之配件信息ID
			String labourIds[] = request.getParamValues("LABOUR_ID");// 索赔之工时信息ID
			String otherIds[] = request.getParamValues("OTHER_ID");// 索赔之其他项目信息ID
			String partBlance[] = request.getParamValues("PARTAMOUNT");// 索赔之配件结算金额
			String labourBlance[] = request.getParamValues("LOBOURAMOUNT");// 索赔之工时结算金额
			String otherBlance[] = request.getParamValues("OTHERAMOUNT");// 索赔之其他项目结算金额
			String auditCon[] = request.getParamValues("audit_con");// 审核意见
			String remark = request.getParamValue("CON_REMARK");// 审核意见

			if (this.isAuditing(claimId, userId)) {// 不可以再次审核，该索赔单已经审核过
				BizException e1 = new BizException(act,
						ErrorCodeConstant.QUERY_FAILURE_CODE,
						"该索赔单之前已经审核过，不能再次审核！");
				act.setException(e1);
				return;
			} else {
				// 1、更新索赔之配件信息（结算金额）
				double pBlance = this.updatePartInfo(partIds, partBlance,
						userId);
				// 2、更新索赔之工时信息（结算金额）
				double lBlance = this.updateLabourInfo(labourIds, labourBlance,
						userId);
				// 3、更新索赔之其他项目信息（结算金额）
				double oBlance = this.updateOtherInfo(otherIds, otherBlance,
						userId, auditCon);

				double allBlance = pBlance + lBlance + oBlance;// 结算总金额

				// 4、记录审核授权状态过程
				this.recordAuthProcess(status, userId, claimId, remark,
						" [授权审核]", false);
				// 5、根据索赔申请单中当前审核步骤，确定下一审核步骤
				String nextCode = this.getNextAuthCode(claimId);
				// 6、更新索赔申请单状态、下一步需要审核的级别和结算总金额
				this.updateClaimOrder(status, userId, allBlance, claimId,
						nextCode, remark);
			}
			act.setOutData("ACTION_RESULT", 1);
			act.setOutData("FORWORD_URL", this.MANUAL_AUDITING);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔申请单审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

		System.out.println("人工审核");
	}

	/**
	 * 检测该用户是否可以审核该索赔单或该索赔单是否已经审核过
	 * 
	 * @param claimId
	 *            索赔单ID
	 * @param userId
	 *            用户ID
	 * @return true : 审核过或没有权限 false : 可以审核
	 */
	private boolean isAuditing(String claimId, Long userId) {
		boolean result = false;

		ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
		TcUserPO userPO = mAuditingDao.queryUserById(userId);

		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		TtAsWrApplicationPO claimPO = auditingDao.queryClaimById(claimId);

		if (userPO == null || claimPO == null) {
			result = true;
		} else {
			String userCode = "";
			if (userPO.getApprovalLevelCode() != null)
				userCode = userPO.getApprovalLevelCode().toString();// 结算授权级别代码
			String needCode = claimPO.getAuthCode();// 该索赔单需要审核的级别
			if (userCode == null || "".equals(userCode)
					|| !userCode.equals(needCode)) {
				// 该用户的审核级别同索赔单需要的审核级别不同时，不允许审核
				result = true;
			}
		}

		return result;
	}

	/**
	 * 检测该用户是否可以审核该索赔单或该索赔单是否已经审核过
	 * 
	 * @param claimId
	 *            索赔单ID
	 * @param userId
	 *            用户ID
	 * @return true : 审核过或没有权限 false : 可以审核
	 */
	private boolean isBalanceAuditing(String claimId, Long userId, String isRedo) {
		boolean result = false;
		if (isRedo.equals("YES")) {
			return false;
		}
		ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
		TcUserPO userPO = mAuditingDao.queryUserById(userId);

		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		TtAsWrApplicationPO claimPO = auditingDao.queryClaimById(claimId);

		if (userPO == null || claimPO == null) {
			result = true;
		} else {
			String userCode = "";
			if (userPO.getBalanceLevelCode() != null)
				userCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

			String needCode = claimPO.getAuthCode();// 该索赔单需要审核的级别
			if (userCode == null || "".equals(userCode)
					|| !userCode.equals(needCode)) {
				// 该用户的审核级别同索赔单需要的审核级别不同时，不允许审核
				result = true;
			}
		}

		return result;
	}

	/**
	 * 更新索赔之配件信息
	 * 
	 * @param partIds
	 *            配件ID集合
	 * @param partBlance
	 *            ID对应结算金额
	 * @param userId
	 *            审核用户ID
	 * @return double 配件结算总金额
	 */
	private double updatePartInfo(String partIds[], String partBlance[],
			Long userId) {
		double blance = 0.0;
		if (partIds != null && partBlance != null) {
			ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
			for (int i = 0; i < partIds.length; i++) {
				if (partBlance[i] != null && !"".equals(partBlance[i])) {
					blance = blance + Double.parseDouble(partBlance[i]);
					TtAsWrPartsitemPO partPO = new TtAsWrPartsitemPO();
					partPO.setBalanceAmount(Double.parseDouble(partBlance[i]));
					partPO.setUpdateBy(userId);
					partPO.setUpdateDate(new Date());
					mAuditingDao.updatePartsInfo(partPO, Long
							.parseLong(partIds[i]));
				}
			}
		}
		return blance;
	}

	/**
	 * 更新索赔之配件信息
	 * 
	 * @param partIds
	 *            配件ID集合
	 * @param partBlance
	 *            ID对应结算金额
	 * @param partCount
	 *            ID对应结算配件数量
	 * @param userId
	 *            审核用户ID
	 * @return double 配件结算总金额
	 */
	private double updatePartInfo(String partIds[], String partBlance[],
			String partCount[], String remark[], Long userId) {
		double blance = 0.0;
		if (partIds != null && partBlance != null) {
			ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
			for (int i = 0; i < partIds.length; i++) {
				if (partBlance[i] != null && !"".equals(partBlance[i])) {
					blance = blance
							+ Double
									.parseDouble(formateData(partBlance[i], "0"));
					TtAsWrPartsitemPO partPO = new TtAsWrPartsitemPO();
					partPO.setBalanceAmount(Double.parseDouble(formateData(
							partBlance[i], "0")));
					partPO.setBalanceQuantity(Float.parseFloat(formateData(
							partCount[i], "0")));
					partPO.setAuthRemark(CommonUtils.checkNull(remark[i]));
					partPO.setUpdateBy(userId);
					partPO.setUpdateDate(new Date());
					mAuditingDao.updatePartsInfo(partPO, Long
							.parseLong(partIds[i]));
				}
			}
		}
		return blance;
	}

	private double updatePartInfo(String partIds[], String partBlance[],
			String partCount[], String remark[], String dis[], Long userId) {
		double blance = 0.0;
		if (partIds != null) {
			ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
			for (int i = 0; i < partIds.length; i++) {
				if (dis[i] != null && !"".equals(dis[i])) {
					TtAsWrPartsitemPO partPO = new TtAsWrPartsitemPO();
					partPO.setDeductAmount(Double.parseDouble(formateData(
							dis[i], "0")));
					partPO.setUpdateBy(userId);
					partPO.setUpdateDate(new Date());
					TtAsWrPartsitemPO partPO1 = new TtAsWrPartsitemPO();
					partPO1.setPartCode(dis[i]);
					mAuditingDao.update(partPO1, partPO);
				}
			}
		}
		return blance;
	}

	/**
	 * 格式化数据
	 * 
	 * @param value
	 *            原数据
	 * @param defaultValue
	 *            为空时使用的默认值
	 * @return
	 */
	private String formateData(String value, String defaultValue) {
		String result = defaultValue;
		if (Utility.testString(value))
			result = value;
		return result;
	}

	/**
	 * 根据技术室审核结果，更新配件金额
	 * 
	 * @param partIds
	 *            配件ID集合
	 * @param partBlance
	 *            ID对应结算金额
	 * @param partStatus
	 *            对应配件的状态
	 * @param partCount
	 *            配件的结算数量
	 * @param userId
	 *            审核用户ID
	 * @return double 配件结算总金额
	 */
	private double updatePartInfoByStatus(String partIds[],
			String partBlance[], String partStatus[], String partCount[],
			Long userId) {
		double blance = 0.0;
		if (partIds != null && partBlance != null) {
			ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
			for (int i = 0; i < partIds.length; i++) {

				TtAsWrPartsitemPO partPO = new TtAsWrPartsitemPO();
				partPO.setUpdateBy(userId);
				partPO.setUpdateDate(new Date());
				partPO.setIsAgree(Integer.parseInt(partStatus[i]));

				if (partBlance[i] != null
						&& !"".equals(partBlance[i])
						&& (Constant.IF_TYPE_YES.toString())
								.equals(partStatus[i])) {
					// 当授权审核同意，且结算金额不为空，那么更新索赔单
					blance = blance
							+ Double
									.parseDouble(formateData(partBlance[i], "0"));
					partPO.setBalanceAmount(Double.parseDouble(formateData(
							partBlance[i], "0")));
					partPO.setBalanceQuantity(Float.parseFloat(formateData(
							partCount[i], "0")));
					/** addUser:xiongchuan addDate:2010-12-14 * */
					partPO.setQuantity(Float.parseFloat(formateData(
							partCount[i], "0")));
					partPO.setAmount(Double.parseDouble(formateData(
							partBlance[i], "0")));
					/** endUser:xiongchuan endDate:2010-12-14 * */
				} else {// 当授权审核不同意时，将结算金额修改为0
					partPO.setBalanceAmount(0.0);
					partPO.setBalanceQuantity(0.0f);
					/** addUser:xiongchuan addDate:2010-12-14 * */
					partPO.setAmount(0.0);
					partPO.setQuantity(0.0f);
					/** endUser:xiongchuan endDate:2010-12-14 * */
				}

				mAuditingDao
						.updatePartsInfo(partPO, Long.parseLong(partIds[i]));

			}
		}

		return blance;
	}

	/**
	 * 更新索赔之工时信息
	 * 
	 * @param labourIds
	 *            工时ID集合
	 * @param labourBlance
	 *            ID对应结算金额
	 * @param userId
	 *            审核用户ID
	 * @return double 工时结算总金额
	 */
	private double updateLabourInfo(String labourIds[], String labourBlance[],
			Long userId) {
		double blance = 0.0;
		if (labourIds != null && labourBlance != null) {
			ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
			for (int i = 0; i < labourIds.length; i++) {
				if (labourBlance[i] != null && !"".equals(labourBlance[i])) {
					blance = blance
							+ Double.parseDouble(formateData(labourBlance[i],
									"0"));
					TtAsWrLabouritemPO labourPO = new TtAsWrLabouritemPO();
					labourPO.setBalanceAmount(Double.parseDouble(formateData(
							labourBlance[i], "0")));
					labourPO.setUpdateBy(userId);
					labourPO.setUpdateDate(new Date());
					mAuditingDao.updateLabourInfo(labourPO, Long
							.parseLong(labourIds[i]));
				}
			}
		}
		return blance;
	}

	/**
	 * 更新索赔之工时信息
	 * 
	 * @param labourIds
	 *            工时ID集合
	 * @param labourBlance
	 *            ID对应结算金额
	 * @param labourCount
	 *            ID对应结算工时数量
	 * @param userId
	 *            审核用户ID
	 * @return double 工时结算总金额
	 */
	private double updateLabourInfo(String labourIds[], String labourBlance[],
			String labourCount[], String remark[], Long userId) {
		double blance = 0.0;
		String labour = "";
		if (labourIds != null && labourBlance != null) {
			ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
			for (int i = 0; i < labourIds.length; i++) {
				if (i == 0) {
					if (labourBlance[i] != null && !"".equals(labourBlance[i])) {
						blance = blance
								+ Double.parseDouble(formateData(
										labourBlance[i], "0"));
						TtAsWrLabouritemPO labourPO = new TtAsWrLabouritemPO();
						labourPO
								.setBalanceAmount(Double
										.parseDouble(formateData(
												labourBlance[i], "0")));
						labourPO.setBalanceQuantity(Float
								.parseFloat(formateData(labourCount[i], "0")));
						// labourPO.setAuthRemark(CommonUtils.checkNull(remark[i]));
						labourPO.setUpdateBy(userId);
						labourPO.setUpdateDate(new Date());
						labour = labourIds[i];
						mAuditingDao.updateLabourInfo(labourPO, Long
								.parseLong(labourIds[i]));
					}
				} else {
					if (!(labour.equals(labourIds[i]))) {
						if (labourBlance[i] != null
								&& !"".equals(labourBlance[i])) {
							blance = blance
									+ Double.parseDouble(formateData(
											labourBlance[i], "0"));
							TtAsWrLabouritemPO labourPO = new TtAsWrLabouritemPO();
							labourPO.setBalanceAmount(Double
									.parseDouble(formateData(labourBlance[i],
											"0")));
							labourPO
									.setBalanceQuantity(Float
											.parseFloat(formateData(
													labourCount[i], "0")));
							// labourPO.setAuthRemark(CommonUtils.checkNull(remark[i]));
							labourPO.setUpdateBy(userId);
							labourPO.setUpdateDate(new Date());
							labour = labourIds[i];
							mAuditingDao.updateLabourInfo(labourPO, Long
									.parseLong(labourIds[i]));
						}
					}
				}
			}
		}
		return blance;
	}

	/**
	 * 根据技术室审核结果，更新工时金额
	 * 
	 * @param labourIds
	 *            工时ID集合
	 * @param labourBlance
	 *            ID对应结算金额
	 * @param labourStatus
	 *            对应工时的审核状态
	 * @param labourCount
	 *            对应结算工时数
	 * @param userId
	 *            审核用户ID
	 * @return double 工时结算总金额
	 */
	private double updateLabourInfoByStatus(String labourIds[],
			String labourBlance[], String labourStatus[], String labourCount[],
			Long userId) {
		double blance = 0.0;
		if (labourIds != null && labourBlance != null) {
			ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
			for (int i = 0; i < labourIds.length; i++) {

				TtAsWrLabouritemPO labourPO = new TtAsWrLabouritemPO();
				labourPO.setUpdateBy(userId);
				labourPO.setUpdateDate(new Date());
				labourPO.setIsAgree(Integer.parseInt(labourStatus[i]));

				if (labourBlance[i] != null
						&& !"".equals(labourBlance[i])
						&& (Constant.IF_TYPE_YES.toString())
								.equals(labourStatus[i])) {
					// 当授权审核同意，且结算金额不为空，那么更新索赔单
					blance = blance
							+ Double.parseDouble(formateData(labourBlance[i],
									"0"));
					labourPO.setBalanceAmount(Double.parseDouble(formateData(
							labourBlance[i], "0")));
					labourPO.setBalanceQuantity(Float.parseFloat(formateData(
							labourCount[i], "0")));
					labourPO.setLabourAmount(Double.parseDouble(formateData(
							labourBlance[i], "0")));
					labourPO.setLabourQuantity(Float.parseFloat(formateData(
							labourCount[i], "0")));
				} else {// 当授权审核不同意时，将结算金额修改为0
					labourPO.setBalanceAmount(0.0);
					labourPO.setBalanceQuantity(0f);
					labourPO.setLabourAmount(0.0);
					labourPO.setLabourQuantity(0f);
				}

				mAuditingDao.updateLabourInfo(labourPO, Long
						.parseLong(labourIds[i]));
			}
		}
		return blance;
	}

	/**
	 * 更新索赔之其他项目信息
	 * 
	 * @param otherIds
	 *            其他项目ID集合
	 * @param otherBlance
	 *            ID对应结算金额
	 * @param userId
	 *            审核用户ID
	 * @return double 其他项目结算总金额
	 */
	private double updateOtherInfo(String otherIds[], String otherBlance[],
			Long userId, String[] auditCon) {
		double blance = 0.0;
		if (otherIds != null && otherBlance != null) {
			ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
			for (int i = 0; i < otherIds.length; i++) {
				if (otherBlance[i] != null && !"".equals(otherBlance[i])) {
					blance = blance + Double.parseDouble(otherBlance[i]);
					TtAsWrNetitemPO otherPO = new TtAsWrNetitemPO();
					otherPO
							.setBalanceAmount(Double
									.parseDouble(otherBlance[i]));
					otherPO.setUpdateBy(userId);
					otherPO.setUpdateDate(new Date());
					if (auditCon != null) {
						otherPO.setAuditCon(auditCon[i]);
					}
					mAuditingDao.updateOtherInfo(otherPO, Long
							.parseLong(otherIds[i]));
				}
			}
		}
		return blance;
	}

	/**
	 * 更新索赔之其他项目信息
	 * 
	 * @param otherIds
	 *            其他项目ID集合
	 * @param otherBlance
	 *            ID对应结算金额
	 * @param otherStatus
	 *            其他项目审核状态
	 * @param userId
	 *            审核用户ID
	 * @return double 其他项目结算总金额
	 */
	private double updateOtherInfoByStatus(String otherIds[],
			String otherBlance[], String otherStatus[], Long userId) {
		double blance = 0.0;
		if (otherIds != null && otherBlance != null) {
			ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
			for (int i = 0; i < otherIds.length; i++) {

				TtAsWrNetitemPO otherPO = new TtAsWrNetitemPO();
				otherPO.setUpdateBy(userId);
				otherPO.setUpdateDate(new Date());
				// 页面已隐藏，默认为 是
				// otherPO.setIsAgree(Integer.parseInt(otherStatus[i]));
				otherPO.setIsAgree(Constant.IF_TYPE_YES);
				if (otherBlance[i] != null
						&& !"".equals(otherBlance[i])
						&& (Constant.IF_TYPE_YES.toString())
								.equals(otherStatus[i])) {
					// 当授权审核同意，且结算金额不为空，那么更新索赔单
					blance = blance + Double.parseDouble(otherBlance[i]);
					otherPO
							.setBalanceAmount(Double
									.parseDouble(otherBlance[i]));
					otherPO.setAmount(Double.parseDouble(otherBlance[i]));
				} else {// 当授权审核不同意时，将结算金额修改为0
					otherPO.setBalanceAmount(0.0);
					otherPO.setAmount(0.0);
				}

				mAuditingDao.updateOtherInfo(otherPO, Long
						.parseLong(otherIds[i]));
			}
		}
		return blance;
	}

	/**
	 * 记录审核记录
	 * 
	 * @param status
	 *            审核结果状态
	 * @param userId
	 *            审核人员
	 * @param claimId
	 *            索赔申请单ID
	 * @param backup
	 *            备注
	 * @param authType
	 *            审核类型
	 */
	private void recordAuthProcess(String status, Long userId, String claimId,
			String backup, String authType, boolean isBalance) {

		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();

		TcUserPO userPO = mAuditingDao.queryUserById(userId);
		String authCode = "";
		if (userPO.getApprovalLevelCode() != null && !isBalance)
			authCode = userPO.getApprovalLevelCode().toString();// 结算授权级别代码
		else if (userPO.getBalanceLevelCode() != null && isBalance)
			authCode = userPO.getBalanceLevelCode().toString();

		TtAsWrAppauthitemPO appAuthPO = new TtAsWrAppauthitemPO();
		appAuthPO.setId(Long.parseLong(claimId));
		appAuthPO.setApprovalPerson(userPO.getName() + authType);
		appAuthPO.setApprovalLevelCode(authCode);
		appAuthPO.setApprovalDate(new Date());
		appAuthPO.setApprovalResult(status);// 授权结果=索赔申请单状态
		appAuthPO.setAuthorizedCode(userPO.getPersonCode());// 授权代码
		appAuthPO.setRemark(backup);// 备注=审核不通过理由
		appAuthPO.setCreateBy(userId);
		appAuthPO.setCreateDate(new Date());
		auditingDao.insertClaimAppAuth(appAuthPO);
	}

	/**
	 * 取的下一审核需要的授权级别
	 * 
	 * @param claimId
	 *            索赔工单ID
	 * @param userId
	 *            用户信息
	 * @return String 下一审核授权级别
	 */
	private String getNextAuthCode(String claimId) {

		String nextAuthCode = "";
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		ClaimBillMaintainDAO billDao = new ClaimBillMaintainDAO();

		// 索赔申请单信息
		TtAsWrApplicationPO claimPO = auditingDao.queryClaimById(claimId);
		String authCode = claimPO.getAuthCode();
		if (authCode == null)
			return nextAuthCode;
		// 索赔审核需要授权信息
		TtAsWrWrauthorizationPO authPO = billDao.queryAuthReason(claimId);
		String levelCode = "";

		if (authPO != null)
			levelCode = authPO.getApprovalLevelCode();

		// 取得下一级需要审核的代码
		if (levelCode != null && !"".equals(levelCode)) {
			int index = levelCode.indexOf(authCode);

			if (index > -1) {
				index = index + authCode.length();

				levelCode = levelCode.substring(index);

				if (levelCode != null && !"".equals(levelCode)) {
					while (levelCode.startsWith(",")) {
						levelCode = levelCode.replaceFirst(",", "");
					}
					if (levelCode.indexOf(",") > -1) {
						nextAuthCode = levelCode.split(",")[0];
					} else {
						nextAuthCode = levelCode;
					}
				}
			} else {
				nextAuthCode = "";
			}
		}
		return nextAuthCode;
	}

	/**
	 * 取的下一审核需要的授权级别
	 * 
	 * @param claimId
	 *            索赔工单ID
	 * @param userId
	 *            用户信息
	 * @return String 下一审核授权级别
	 * @throws Exception
	 */
	private String getNextAuthCode(TtAsWrApplicationPO claimPO)
			throws Exception {

		if (claimPO == null || claimPO.getId() == null)
			throw new Exception("索赔单信息空！");

		String nextAuthCode = "";
		ClaimBillMaintainDAO billDao = new ClaimBillMaintainDAO();

		// 索赔申请单信息
		String authCode = claimPO.getAuthCode();
		// 索赔审核需要授权信息
		TtAsWrWrauthorizationPO authPO = billDao.queryAuthReason(claimPO
				.getId().toString());
		String levelCode = "";

		if (authPO != null)
			levelCode = authPO.getApprovalLevelCode();

		// 取得下一级需要审核的代码
		if (levelCode != null && !"".equals(levelCode)) {
			int index = levelCode.indexOf(authCode);

			if (index > -1) {
				index = index + authCode.length();

				levelCode = levelCode.substring(index);

				if (levelCode != null && !"".equals(levelCode)) {
					while (levelCode.startsWith(",")) {
						levelCode = levelCode.replaceFirst(",", "");
					}
					if (levelCode.indexOf(",") > -1) {
						nextAuthCode = levelCode.split(",")[0];
					} else {
						nextAuthCode = levelCode;
					}
				}
			} else {
				nextAuthCode = "";
			}
		}
		return nextAuthCode;
	}

	/**
	 * 更新索赔申请单信息
	 * 
	 * <pre>
	 * 注意：
	 *     审核过程中有一个级别上做出审核拒绝或审核退回后则不再进行其他级别审核
	 *     如果审核通过则进行下一个级别的审核
	 * </pre>
	 * 
	 * @param status
	 *            索赔申请单状态
	 * @param userId
	 *            审核用户
	 * @param allBlance
	 *            结算金额
	 * @param claimId
	 *            索赔申请单ID
	 * @param nextCode
	 *            需要审核的下个级别
	 * @param remark
	 *            审核意见
	 */
	private void updateClaimOrder(String status, Long userId, double allBlance,
			String claimId, String nextCode, String remark) {
		TtAsWrApplicationPO claimPO = new TtAsWrApplicationPO();
		claimPO.setBalanceAmount(allBlance);
		claimPO.setUpdateBy(userId);
		claimPO.setAuditingMan(userId);
		claimPO.setUpdateDate(new Date());
		claimPO.setAuthCode(nextCode);
		claimPO.setAuditType(Constant.IS_MANUAL_AUDITING + "");// 人工审核标识
		if (Constant.CLAIM_APPLY_ORD_TYPE_05.toString().equals(status)) {// 审核拒绝
																			// 则
																			// 不在进行其他审核
			claimPO.setStatus(Integer.parseInt(status));
			claimPO.setLastStatus(Integer.parseInt(status));
			claimPO.setOemOption(remark);
		} else if (Constant.CLAIM_APPLY_ORD_TYPE_06.toString().equals(status)) {// 审核退回
																				// 则不在进行其他审核
			claimPO.setStatus(Integer.parseInt(status));
			claimPO.setLastStatus(Integer.parseInt(status));
			claimPO.setOemOption(remark);
		} else {// 审核通过
			if (nextCode == null || "".equals(nextCode)) {// 不存在其他需要审核的级别
				claimPO.setStatus(Integer.parseInt(status));
				claimPO.setLastStatus(Integer.parseInt(status));
			}
		}

		ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
		mAuditingDao.updateClaimInfo(claimPO, Long.parseLong(claimId));
	}

	/**
	 * 更新索赔申请单信息
	 * 
	 * <pre>
	 * 注意：
	 *     审核过程中有一个级别上做出审核拒绝或审核退回后则不再进行其他级别审核
	 *     如果审核通过则进行下一个级别的审核
	 * </pre>
	 * 
	 * @param status
	 *            索赔申请单状态
	 * @param userId
	 *            审核用户
	 * @param allBlance
	 *            结算金额
	 * @param claimId
	 *            索赔申请单ID
	 * @param nextCode
	 *            需要审核的下个级别
	 * @param remark
	 *            审核意见
	 * @param appendLabourNum
	 *            追加工时数
	 * @param companyId
	 *            公司ID
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private void updateClaimOrder(String status, Long userId, double allBlance,
			String claimId, String nextCode, String remark,
			String appendLabourNum, Long companyId) {

		TtAsWrApplicationPO claimPO = new TtAsWrApplicationPO();
		claimPO.setUpdateBy(userId);
		claimPO.setUpdateDate(new Date());
		claimPO.setAuthCode(nextCode);
		claimPO.setAuditType(Constant.IS_MANUAL_AUDITING + "");// 人工审核标识
		if (appendLabourNum == null || "".equals(appendLabourNum))
			appendLabourNum = "0";
		claimPO.setAppendlabourNum(CommonUtils.parseDouble(appendLabourNum));
		// 计算工时费用,同时将追加费用加入到结算金额中
		Double price = (new LabourUtil()).getLabourPrice(claimId, companyId);
		Double amount = price * CommonUtils.parseDouble(appendLabourNum);
		claimPO.setAppendlabourAmount(amount);

		claimPO.setBalanceAmount(allBlance + claimPO.getAppendlabourAmount());

		if (Constant.CLAIM_APPLY_ORD_TYPE_05.toString().equals(status)) {// 审核拒绝
																			// 则
																			// 不在进行其他审核
			claimPO.setStatus(Integer.parseInt(status));
			claimPO.setOemOption(remark);
		} else if (Constant.CLAIM_APPLY_ORD_TYPE_06.toString().equals(status)) {// 审核退回
																				// 则不在进行其他审核
			claimPO.setStatus(Integer.parseInt(status));
			claimPO.setOemOption(remark);
		} else {// 审核通过
			if (nextCode == null || "".equals(nextCode)) {// 不存在其他需要审核的级别
				claimPO.setStatus(Integer.parseInt(status));
			}
		}

		ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
		mAuditingDao.updateClaimInfo(claimPO, Long.parseLong(claimId));
	}

	/**
	 * 更新索赔申请单信息
	 * 
	 * <pre>
	 * 注意：
	 *     审核过程中有一个级别上做出审核拒绝或审核退回后则不再进行其他级别审核
	 *     如果审核通过则进行下一个级别的审核
	 * </pre>
	 * 
	 * @param nextCode
	 *            需要审核的下个级别
	 * @param appendLabourNum
	 *            追加工时数
	 * @param labourPrice
	 *            工时单价
	 * @param conditionPO
	 *            需要传入参数[索赔申请单状态、审核用户(更新用户ID)、结算金额、索赔申请单ID、 审核意见]
	 */
	private void updateClaimOrder(String nextCode, String appendLabourNum,
			Double labourPrice, TtAsWrApplicationPO conditionPO) {

		String status = CommonUtils.checkNull(conditionPO.getStatus());
		Long userId = conditionPO.getUpdateBy();
		double allBlance = conditionPO.getBalanceAmount();
		String claimId = CommonUtils.checkNull(conditionPO.getId());
		String remark = CommonUtils.checkNull(conditionPO.getRemark());

		TtAsWrApplicationPO claimPO = new TtAsWrApplicationPO();
		claimPO.setUpdateBy(userId);
		claimPO.setUpdateDate(new Date());
		claimPO.setAuditingMan(userId);
		claimPO.setAuthCode(nextCode);
		claimPO.setAuditType(Constant.IS_MANUAL_AUDITING + "");// 人工审核标识
		claimPO.setAuditingDate(new Date());
		if (appendLabourNum == null || "".equals(appendLabourNum))
			appendLabourNum = "0";
		claimPO.setAppendlabourNum(CommonUtils.parseDouble(appendLabourNum));
		// 计算工时费用,同时将追加费用加入到结算金额中
		Double amount = labourPrice * CommonUtils.parseDouble(appendLabourNum);
		claimPO.setAppendlabourAmount(amount);
		claimPO.setAppendLabourAmountBck(amount);// Iverson add By 2010-12-13
		/** addUser:xiongchuan addDate:2010-12-14 * */
		claimPO.setApplyAppendlabourAmount(amount);
		claimPO.setBalanceAppendlabourAmount(amount);
		/** endUser:xiongchuan endDate:2010-12-14 * */
		claimPO.setBalanceAmount(allBlance + claimPO.getAppendlabourAmount());
		claimPO
				.setBalanceAmountBck(allBlance
						+ claimPO.getAppendlabourAmount());// Iverson add By
															// 2010-12-13
		if (Constant.CLAIM_APPLY_ORD_TYPE_05.toString().equals(status)) {// 审核拒绝
																			// 则
																			// 不在进行其他审核
			claimPO.setStatus(Integer.parseInt(status));
			claimPO.setLastStatus(Integer.parseInt(status));
			claimPO.setOemOption(remark);

			// 设置标志
			claimPO.setIsAuto(0);

		} else if (Constant.CLAIM_APPLY_ORD_TYPE_06.toString().equals(status)) {// 审核退回
																				// 则不在进行其他审核
			claimPO.setStatus(Integer.parseInt(status));
			claimPO.setLastStatus(Integer.parseInt(status));
			claimPO.setOemOption(remark);

			// 设置标志
			claimPO.setIsAuto(0);
			ClaimManualAuditingDao mAuditingDao1 = new ClaimManualAuditingDao();
			mAuditingDao1.deleteBarcode(claimId);

		} else {// 审核通过
			if (nextCode == null || "".equals(nextCode)) {// 不存在其他需f要审核的级别
				claimPO.setLastStatus(Integer.parseInt(status));
				claimPO.setStatus(Integer.parseInt(status));
				// 设置标志
				claimPO.setIsAuto(1);
			}
		}

		ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
		mAuditingDao.updateClaimInfo(claimPO, Long.parseLong(claimId));

	}

	/**
	 * 跳转到索赔审核页(授权审核)
	 * 
	 * @throws
	 */
	public void claimAuditingPage() {
		AclUserBean logonUser = loginUser;
		Long provinceId = null;
		String phone = "";
		try {
			TtAsActivityPO activity = new TtAsActivityPO();
			String id = request.getParamValue("ID");
			String type = request.getParamValue("type");
			TtAsWrApplicationPO a = new TtAsWrApplicationPO();
			a.setId(Long.valueOf(id));
			a = (TtAsWrApplicationPO) dao.select(a).get(0);
			List<Map<String, Object>> mainCodeList = dao.queryMainList(id);// 得到主因件集合

			TtAsWrApplicationExtPO tawep = dao.queryApplicationById(id);
			List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
			List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时
			List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			List<Map<String, Object>> appAuthls = dao.queryAppAuthDetail(id);// 审核授权信息

			List<Map<String, Object>> accessoryDtlList = dao
					.getclaimAccessoryDtl(a.getClaimNo());
			act.setOutData("accessoryDtlList", accessoryDtlList);

			// ====================zyw 2014-8-4 补偿费查询明细
			List<Map<String, Object>> compensationMoneyList = dao
					.getCompensationMoneyAPP(a.getClaimNo());
			if (compensationMoneyList != null
					&& compensationMoneyList.size() >= 0) {
				act.setOutData("compensationMoneyList", compensationMoneyList);
			}
			// ==============================
			TmDealerPO d = new TmDealerPO();
			d.setDealerCode(tawep.getDealerCode());

			List<TmDealerPO> resList = dao.select(d);
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				phone = dealerPO.getPhone();
				provinceId = dealerPO.getProvinceId();
			}
			act.setOutData("application", tawep);
			act.setOutData("ACTIVITY", activity);
			act.setOutData("ID", id);
			act.setOutData("itemLs", itemls);
			act.setOutData("partLs", partls);
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("mainCodeList", mainCodeList);
			act.setOutData("size", mainCodeList.size());
			act.setOutData("otherLs", otherls);
			act.setOutData("attachLs", attachLs);
			act.setOutData("type", type);
			act.setOutData("phone", phone);
			String claimType = a.getClaimType().toString();
			String code = BaseUtils.checkNull(a.getCampaignCode());
			if ((claimType.equals(Constant.CLA_TYPE_06.toString()) && jugeReplce(code))
					|| claimType.equals(Constant.CLA_TYPE_02.toString())
					|| claimType.equals(Constant.CLA_TYPE_11.toString())) {
				act.setForword(this.MANUAL_AUDITING_PAGE_PDI);
				logger
						.info("=================跳转的是PDI和强保和服务活动不为切换件的页面！！！！！！！！！！！！！！");
			} else {
				act.setForword(this.MANUAL_AUDITING_PAGE2);
				logger
						.info("=================跳转的========================================正常的");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	@SuppressWarnings("rawtypes")
	public boolean jugeReplce(String code) {
		boolean flag = false;
		TtAsActivityPO po = new TtAsActivityPO();
		po.setActivityCode(code);
		po.setActivityType(Constant.SERVICEACTIVITY_TYPE_05);
		List select = dao.select(po);
		if (select != null && select.size() == 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 技术室授权审核
	 * 
	 * <pre>
	 * 步骤：
	 * 1、更新索赔之配件信息（结算金额）
	 * 2、更新索赔之工时信息（结算金额）
	 * 3、更新索赔之其他项目信息（结算金额）
	 * 4、记录审核授权状态过程
	 * 5、更新索赔申请单授权，将审核授权级别中审核完成的去掉
	 * 6、更新索赔申请单状态、下一步需要审核的级别和结算总金额
	 * </pre>
	 */
	public void engineeringAuditing() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		try {
			RequestWrapper request = act.getRequest();
			Long userId = logonUser.getUserId();// 登陆用户ID
			String claimId = request.getParamValue("claimId");// 索赔申请单ID
			String status = request.getParamValue("status");// 审核状态(审核通过、审核退回、审核拒绝)
			String partIds[] = request.getParamValues("PART_ID");// 索赔之配件信息ID
			String labourIds[] = request.getParamValues("LABOUR_ID");// 索赔之工时信息ID
			String otherIds[] = request.getParamValues("OTHER_ID");// 索赔之其他项目信息ID
			String partBlance[] = request.getParamValues("PARTAMOUNTTD");// 索赔之配件结算金额
			String labourBlance[] = request.getParamValues("LOBOURAMOUNTTD");// 索赔之工时结算金额
			String otherBlance[] = request.getParamValues("OTHERAMOUNTTD");// 索赔之其他项目结算金额
			String partStatus[] = request.getParamValues("PARTSTATUS");// 索赔之配件结算金额
			String labourStatus[] = request.getParamValues("LOBOURSTATUS");// 索赔之工时结算金额
			String otherStatus[] = request.getParamValues("OTHERSTATUS");// 索赔之其他项目结算金额
			String partCount[] = request.getParamValues("PARTBCOUNTTD");// 索赔之配件结算数量
			String labourCount[] = request.getParamValues("LABOURBCOUNTTD");// 索赔之工时结算数量
			String remark = request.getParamValue("CON_REMARK");// 审核意见
			String appendLabour = request.getParamValue("APPEND_LABOUR");// 追加工时数
			String labourPrice = request.getParamValue("labourPrice");// 工时单价

			remark = CommonUtils.checkNull(remark) + " [追加工时数：" + appendLabour
					+ "]";

			if (this.isAuditing(claimId, userId)) {// 不可以再次审核，该索赔单已经审核过
				BizException e1 = new BizException(act,
						ErrorCodeConstant.QUERY_FAILURE_CODE,
						"该索赔单之前已经审核过，不能再次审核！");
				act.setException(e1);
				return;
			} else {
				// 1、更新索赔之配件信息（结算金额）
				double pBlance = this.updatePartInfoByStatus(partIds,
						partBlance, partStatus, partCount, userId);
				// 2、更新索赔之工时信息（结算金额）
				double lBlance = this.updateLabourInfoByStatus(labourIds,
						labourBlance, labourStatus, labourCount, userId);
				// 3、更新索赔之其他项目信息（结算金额）
				double oBlance = this.updateOtherInfoByStatus(otherIds,
						otherBlance, otherStatus, userId);

				double allBlance = pBlance + lBlance + oBlance;// 结算总金额

				// 4、记录审核授权状态过程
				this.recordAuthProcess(status, userId, claimId, remark,
						" [授权审核]", false);
				// 5、根据索赔申请单中当前审核步骤，确定下一审核步骤
				String nextCode = this.getNextAuthCode(claimId);
				// 6、更新索赔申请单状态、下一步需要审核的级别和结算总金额
				TtAsWrApplicationPO conditionPO = new TtAsWrApplicationPO();
				conditionPO.setStatus(Integer.parseInt(status));
				conditionPO.setUpdateBy(userId);
				conditionPO.setBalanceAmount(allBlance);
				conditionPO.setLabourAmount(lBlance);// 更新
				conditionPO.setPartAmount(pBlance);
				conditionPO.setRemark(remark);
				conditionPO.setId(Long.parseLong(claimId));
				if (!Utility.testString(labourPrice)
						|| Constant.CLAIM_APPLY_ORD_TYPE_06.toString().equals(
								status))
					labourPrice = "0";
				if (Constant.CLAIM_APPLY_ORD_TYPE_06.toString().equals(status))
					appendLabour = "0";
				this.updateClaimOrder(nextCode, appendLabour, Double
						.parseDouble(labourPrice), conditionPO);
				// 61、当审核状态为"退回"时，修改对应工单的状态为"未上报"(现在工单转索赔单后就不可以在修改了)
				/*
				 * if(Constant.CLAIM_APPLY_ORD_TYPE_06.toString().equals(status)){
				 * ClaimAuditingDao auditingDao =
				 * ClaimAuditingDao.getInstance(); DealerClaimReportDao
				 * reportDao = new DealerClaimReportDao(); TtAsWrApplicationPO
				 * claimPO = auditingDao.queryClaimById(claimId);
				 * reportDao.modifyWorkOrderStatus(claimPO.getRoNo(),
				 * Constant.IS_NOT_REPORT, userId); }
				 */
				// 62、根据工时、配件、其他项目和附加工时回写索赔单中对应的结算金额和结算金额汇总
				this.modifyClaimAmount(Long.parseLong(claimId));
			}
			act.setOutData("ACTION_RESULT", 1);
			act.setOutData("FORWORD_URL", this.MANUAL_AUDITING);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "技术室授权审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 结算室审核 add by lishuai103@yahoo.cn
	 */
	public void claimMAccAuditInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 取得该用户拥有的产地权限
			RequestWrapper request = act.getRequest();
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUser
					.getPoseId());
			List<TmBusinessAreaPO> list = dao.getAreaName();
			String falg = request.getParamValue("falg");
			if (falg != null && falg.equals("1")) {
				String dealerCode1 = request.getParamValue("dealerCode1");
				yieldly = request.getParamValue("yieldly1");
				String balanceNo1 = request.getParamValue("balanceNo1");
				String startDate1 = request.getParamValue("startDate1");
				String endDate1 = request.getParamValue("endDate1");
				act.setOutData("balanceNo1", balanceNo1);
				act.setOutData("dealerCode1", dealerCode1);
				act.setOutData("startDate1", startDate1);
				act.setOutData("endDate1", endDate1);
				act.setOutData("falg", falg);
			}
			System.out.println(falg + "......" + yieldly);
			act.setOutData("yieldly", yieldly);
			request.setAttribute("list", list);
			act.setForword(this.accaudit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void claimAccAuditQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			// CommonUtils.checkNull() 校验是否为空
			String dealerCode = CommonUtils.checkNull(request
					.getParamValue("dealerCode")); // 经销商代码
			String yieldly = CommonUtils.checkNull(request
					.getParamValue("yieldly")); // 产地代码
			String balanceNo = CommonUtils.checkNull(request
					.getParamValue("balanceNo")); // 结算单号
			String status = CommonUtils.checkNull(request
					.getParamValue("status")); // 结算单装备
			String startDate = CommonUtils.checkNull(request
					.getParamValue("startDate")); // 创建开始时间
			String endDate = CommonUtils.checkNull(request
					.getParamValue("endDate")); // 创建结束时间
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUser
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
			bean.setStatus(status);
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setOemCompanyId(String.valueOf(GetOemcompanyId
					.getOemCompanyId(logonUser)));
			bean.setYieldlys(yieldlys);

			ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryAccAuditList(bean,
					curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 查询结算单对应索赔单 注：修改该查询条件时，需要同时调整逐条审核中的查询条件
	 */
	public void claimOneByOneAuditQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			// CommonUtils.checkNull() 校验是否为空
			String id = request.getParamValue("id");
			String claimNo = request.getParamValue("claimNo");// 索赔单ID
			String modelCode = request.getParamValue("modelCode");// 车型代码
			String vin = request.getParamValue("vin");
			String claimType = request.getParamValue("CLAIM_TYPE");// 索赔单类型
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码
			String startDate = CommonUtils.checkNull(request
					.getParamValue("startDate")); // 创建开始时间
			String endDate = CommonUtils.checkNull(request
					.getParamValue("endDate")); // 创建结束时间

			ClaimManualAuditingDao dao = new ClaimManualAuditingDao();

			// 当开始时间和结束时间相同时
			if (null != startDate && !"".equals(startDate) && null != endDate
					&& !"".equals(endDate)) {
				startDate = startDate + " 00:00:00";
				endDate = endDate + " 23:59:59";
			}
			Long userId = logonUser.getUserId();
			TcUserPO userPO = dao.queryUserById(userId);
			String authCode = "";
			if (userPO.getBalanceLevelCode() != null)
				authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

			auditBean bean = new auditBean();
			bean.setId(id);
			bean.setClaimNo(claimNo);
			bean.setModelCode(modelCode);
			bean.setVin(vin);
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setOemCompanyId(String.valueOf(GetOemcompanyId
					.getOemCompanyId(logonUser)));
			bean.setAuthCodeOrder(authCode);
			bean.setDealerCode(dealerCode);
			bean.setClaimType(CommonUtils.checkNull(claimType));

			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页

			Integer pageSize = 100;
			PageResult<Map<String, Object>> ps = dao.queryAccAuditById(bean,
					curPage, pageSize);
			// 分页方法 end
			act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 结算单批量审批
	 */
	public void auditAllClaim() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String id = request.getParamValue("id");// 结算单ID
		boolean flag = DBLockUtil.lock(id, DBLockUtil.BUSINESS_TYPE_01);
		if (!flag) {// 存在其他人在审核该结算单
			act.setOutData("returnValue", "100");
			return;
		}
		try {
			ClaimManualAuditingDao dao = new ClaimManualAuditingDao();

			Long userId = logonUser.getUserId();
			TcUserPO userPO = dao.queryUserById(userId);
			String authCode = "";
			if (userPO.getBalanceLevelCode() != null)
				authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码
			if (Utility.testString(id)) {
				// 循环审核对应结算单中的满足用户权限的索赔单通过
				this.batchAuditing(Long.parseLong(id), authCode, userId);
				/** *****mod by liuxh 20101126 批量审核结算单不更新结算单金额和状态****** */
				// 更新结算单中结算金额和结算单状态
				// BalanceAuditing bAuditing = new BalanceAuditing("","");
				// bAuditing.modifyBalanceStatus(Long.parseLong(id),
				// true,logonUser);
				/** *****mod by liuxh 20101126 批量审核结算单不更新结算单金额和状态****** */
				act.setOutData("returnValue", 1);
			} else {
				act.setOutData("returnValue", 2);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		} finally {
			DBLockUtil.freeLock(id, DBLockUtil.BUSINESS_TYPE_01);
		}
	}

	/**
	 * 批量审核
	 * 
	 * @param balanceId
	 *            结算单ID
	 * @param authCode
	 *            用户角色
	 * @param userId
	 *            用户ID
	 * @throws Exception
	 */
	private void batchAuditing(Long balanceId, String authCode, Long userId)
			throws Exception {

		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();

		// 更新索赔单同结算端关系表中的状态
		BalanceAuditingDao balanceAuditingDao = BalanceAuditingDao
				.getInstance();

		// 查询结算单对应索赔单
		List<Map<String, Object>> claimList = balanceDao.queryClaimByBalanceId(
				balanceId, Constant.CLAIM_APPLY_ORD_TYPE_08.toString(),
				authCode);
		// 循环审核每条索赔单
		for (Map<String, Object> claimMap : claimList) {
			String claimId = claimMap.get("CLAIM_ID").toString();
			// 索赔申请单信息
			TtAsWrApplicationPO claimVO = auditingDao.queryClaimById(claimId);
			if (!authCode.equals(claimVO.getAuthCode())) {// 该索赔单已经审核过
				continue;
			}
			// 2、根据索赔申请单中当前审核步骤，确定下一审核步骤
			String nextCode = this.getNextAuthCode(claimVO);
			// 3、更新索赔申请单状态、下一步需要审核的级别和结算总金额
			TtAsWrApplicationPO claimPO = new TtAsWrApplicationPO();
			claimPO.setUpdateBy(userId);
			claimPO.setUpdateDate(new Date());
			claimPO.setAuthCode(nextCode);
			claimPO.setAuditType(Constant.IS_MANUAL_AUDITING + "");// 人工审核标识

			String status = Constant.CLAIM_APPLY_ORD_TYPE_08.toString();
			// 审核通过
			if (nextCode == null || "".equals(nextCode)) {// 不存在其他需要审核的级别,将索赔单状态修改为结算支付
				claimPO.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_07);
				balanceAuditingDao.updateClaimStatus(Long.parseLong(claimId),
						Constant.STATUS_ENABLE);
				status = Constant.CLAIM_APPLY_ORD_TYPE_07.toString();
			}

			// 1、记录审核授权状态过程
			this.recordAuthProcess(status, userId, claimId, "[批量结算审核]",
					" [结算审核]", true);
			// 2、更新索赔申请单状态、下一步需要审核的级别和结算总金额
			mAuditingDao.updateClaimInfo(claimPO, Long.parseLong(claimId));
		}
	}

	/*
	 * 索赔单审核
	 */
	public void auditClaimInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");// 结算单ID
			String isConfirm = request.getParamValue("isConfirm");// 是否进行收单操作
																	// true: 是
																	// false:不是
			if (!Utility.testString(isConfirm))
				isConfirm = "false";

			ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
			TtAsWrClaimBalancePO balancePO = dao.queryBalanceOrderById(Long
					.parseLong(id));

			Long userId = logonUser.getUserId();
			TcUserPO userPO = dao.queryUserById(userId);
			String authCode = "";
			if (userPO.getBalanceLevelCode() != null)
				authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码
			String dealerCode = request.getParamValue("dealerCode");
			String yieldly = request.getParamValue("yieldly");
			String balanceNo = request.getParamValue("balanceNo");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			act.setOutData("yieldly", yieldly);
			act.setOutData("balanceNo", balanceNo);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setOutData("balancePO", balancePO);
			act.setOutData("authCode", authCode);
			act.setOutData("id", id);
			act.setOutData("isConfirm", isConfirm);
			act.setForword(this.acconeaudit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 跳转到索赔审核页(结算审核)
	 * 
	 * @throws
	 */
	public void balanceAuditingPage() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			RequestWrapper request = act.getRequest();
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			String isRedo = CommonUtils.checkNull(request
					.getParamValue("IS_REDO"));
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			String id = request.getParamValue("ID");
			String balanceId = request.getParamValue("BALANCE_ID");
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			TtAsWrApplicationExtPO tawep = auditingDao
					.queryClaimOrderDetailById(id);

			// zhumingwei 2011-03-04
			List<Map<String, Object>> list = auditingDao.getAuthInfo(id);
			String APPROVAL_PERSON = ((Map) list.get(0)).get("APPROVAL_PERSON")
					.toString();
			act.setOutData("APPROVAL_PERSON", APPROVAL_PERSON);

			String count = auditingDao.countRepairTimes(id);
			// zhumingwei 2011-03-04

			List<Map<String, Object>> customerList = dao.getVinUserName2(id);
			Map<String, Object> customerMap = new HashMap<String, Object>();
			if (customerList != null && customerList.size() > 0)
				customerMap = customerList.get(0);
			// 如果是服务活动，取出活动代码与名称
			if (Constant.CLA_TYPE_06.toString().equals(
					tawep.getClaimType().toString())) {
				List actList = auditingDao.getActivity(id);
				if (actList.size() > 0) {
					act.setOutData("activity", actList.get(0));
				}
			}
			// 取得产地名称
			String yieldly = tawep.getYieldly();
			String yieldlyName = "";
			if (yieldly != null) {
				TcCodePO codePO = CommonUtils
						.findTcCodeDetailByCodeId(CommonUtils
								.parseInteger(yieldly));
				if (codePO != null && codePO.getCodeDesc() != null)
					yieldlyName = codePO.getCodeDesc();
			}
			tawep.setYieldlyName(yieldlyName);

			List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
			List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时

			List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			List<TtAsWrAppauthitemPO> appAuthls = dao.queryAppAuthInfo(id);// 审核授权信息
			TtAsWrWrauthorizationPO authReason = dao.queryAuthReason(id);// 需要授权原因
			List<TcCodePO> statusList = CommonUtils.findTcCodeByType(Integer
					.parseInt(Constant.IF_TYPE));// 是否同意状态
			List<Map<String, Object>> listOutRepair = dao.viewOutRepair(tawep
					.getRoNo());// 根据索赔单的工单号查询工单的外出维修信息
			act.setOutData("application", tawep);
			act.setOutData("attachLs", attachLs);
			act.setOutData("ID", id);// 索赔申请单ID
			act.setOutData("BALANCE_ID", balanceId);// 结算单ID
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("statusList", statusList);// 是否同意状态
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("claimId", id);// 索赔单ID

			// zhumingwei 2011-03-04
			act.setOutData("count", count);// 维修次数
			// zhumingwei 2011-03-04

			/** ***MOD BY LIUXH 20101203 有外出维修才信息才取数据***** */
			if (listOutRepair.size() > 0) {
				act.setOutData("listOutRepair", listOutRepair.get(0));// 外出
			}
			/** ***MOD BY LIUXH 20101203 有外出维修才信息才取数据***** */

			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			act.setOutData("isRedo", isRedo);
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */

			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11

			act.setForword(this.BALANCE_AUDITING_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 结算室授权审核
	 * 
	 * <pre>
	 * 步骤：
	 * 1、更新索赔之配件信息（结算金额）
	 * 2、更新索赔之工时信息（结算金额）
	 * 3、更新索赔之其他项目信息（结算金额）
	 * 4、记录审核授权状态过程
	 * 5、更新索赔申请单授权，将审核授权级别中审核完成的去掉
	 * 6、更新索赔申请单状态、下一步需要审核的级别和结算总金额
	 * 7、当对应结算单中的索赔单都审核通过时，重新统计结算单中数据和结算单明细中数据
	 *    同时更改索赔单状态。
	 * </pre>
	 */
	public void balanceAuditing() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		try {

			RequestWrapper request = act.getRequest();
			String isReDo = CommonUtils.checkNull(request
					.getParamValue("isRedo"));
			Long userId = logonUser.getUserId();// 登陆用户ID
			String claimId = request.getParamValue("claimId");// 索赔申请单ID
			String balanceId = request.getParamValue("id");// 结算单ID
			String status = request.getParamValue("status");// 审核状态(审核通过、审核退回、审核拒绝)
			String partIds[] = request.getParamValues("PART_ID");// 索赔之配件信息ID
			String labourIds[] = request.getParamValues("LABOUR_ID");// 索赔之工时信息ID
			String otherIds[] = request.getParamValues("OTHER_ID");// 索赔之其他项目信息ID
			String partBlance[] = request.getParamValues("PARTAMOUNT");// 索赔之配件结算金额
			String partRemark[] = request.getParamValues("PARTREMARK");// 索赔之配件之备注信息
			String partBlanceCount[] = request.getParamValues("PARTCOUNT");// 索赔之配件结算数量
			String labourBlance[] = request.getParamValues("LOBOURAMOUNT");// 索赔之工时结算金额
			String labourBlanceCount[] = request.getParamValues("LOBOURCOUNT");// 索赔之工时结算数量
			String labourRemark[] = request.getParamValues("LOBOURREMARK");// 索赔之工时之备注
			String otherBlance[] = request.getParamValues("OTHERAMOUNT");// 索赔之其他项目结算金额
			String auditCon[] = request.getParamValues("audit_con");// 其它费用审核意见
			String remark = request.getParamValue("CON_REMARK");// 审核意见
			String isContinue = request.getParamValue("isContinue");// 是否连续审核
																	// true :连续
																	// 其他:单条审核
			String appendLabour = request.getParamValue("APPEND_LABOUR");// 追加工时数
			String labourPrice = request.getParamValue("labourPrice");// 工时单价

			int flag = 1;// 审核标识(1:正常 2:该单已经审核过)
			if (this.isBalanceAuditing(claimId, userId, isReDo)) {// 不可以再次审核，该索赔单已经审核过
				flag = 2;
			} else {
				// 1、更新索赔之配件信息（结算金额）
				double pBlance = this.updatePartInfo(partIds, partBlance,
						partBlanceCount, partRemark, userId);
				// 2、更新索赔之工时信息（结算金额）
				double lBlance = this.updateLabourInfo(labourIds, labourBlance,
						labourBlanceCount, labourRemark, userId);
				// 3、更新索赔之其他项目信息（结算金额）
				double oBlance = this.updateOtherInfo(otherIds, otherBlance,
						userId, auditCon);

				double allBlance = pBlance + lBlance + oBlance;// 结算总金额

				// 4、记录审核授权状态过程
				this.recordAuthProcess(status, userId, claimId, remark,
						" [结算审核]", true);
				// 5、根据索赔申请单中当前审核步骤，确定下一审核步骤
				String nextCode = this.getNextAuthCode(claimId);
				// 6、更新索赔申请单状态、下一步需要审核的级别、结算总金额和结算工时数
				TtAsWrApplicationPO conditionPO = new TtAsWrApplicationPO();
				// conditionPO.setStatus(Integer.parseInt(status));
				conditionPO.setUpdateBy(userId);
				conditionPO.setBalanceAmount(allBlance);
				conditionPO.setRemark(remark);
				conditionPO.setId(Long.parseLong(claimId));
				/** addUser:xiongchuan addDate:2010-12-14* */

				/** endUser:xiongchuan endDate:2010-12-14* */
				if (!Utility.testString(labourPrice))
					labourPrice = "0";
				this.updateBalanceClaimOrder(nextCode,
						Integer.parseInt(status), appendLabour, Double
								.parseDouble(labourPrice), conditionPO);
				// 61、根据工时、配件、其他项目和附加工时回写索赔单中对应的结算金额和结算金额汇总
				this.modifyClaimAmountByBalance(Long.parseLong(claimId));

				/** ******mod by liuxh 20101126 逐条审批时关闭更新结算单状态和金额********* */
				// 7、更新结算单中结算金额和结算单状态
				// BalanceAuditing bAuditing = new BalanceAuditing("","");
				// bAuditing.modifyBalanceStatus(Long.parseLong(balanceId),
				// true,logonUser);
				/** ******mod by liuxh 20101126 逐条审批时关闭更新结算单状态和金额********* */
			}
			act.setOutData("isContinue", isContinue);
			act.setOutData("ISAUDITING", flag);
			act.setOutData("ACTION_RESULT", 1);

			if (!"true".equals(isContinue)) {// 单条审核
				act.setOutData("FORWORD_URL", this.MANUAL_AUDITING);
			} else {
				this.auditingOneByOne();
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "技术室授权审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 更新索赔申请单信息
	 * 
	 * <pre>
	 * 注意：
	 *     审核过程中有一个级别上做出审核拒绝或审核退回后则不再进行其他级别审核
	 *     如果审核通过则进行下一个级别的审核
	 * </pre>
	 * 
	 * @param nextCode
	 *            需要审核的下个级别
	 * @param appendLabourNum
	 *            追加工时数
	 * @param labourPrice
	 *            工时单价
	 * @param conditionPO
	 *            需要传入参数[审核用户(更新用户ID)、结算金额、索赔申请单ID、 审核意见]
	 */
	private void updateBalanceClaimOrder(String nextCode, int status,
			String appendLabourNum, Double labourPrice,
			TtAsWrApplicationPO conditionPO) {

		Long userId = conditionPO.getUpdateBy();
		double allBlance = conditionPO.getBalanceAmount();
		String claimId = CommonUtils.checkNull(conditionPO.getId());
		String remark = CommonUtils.checkNull(conditionPO.getRemark());
		String appendlabourReason = CommonUtils.checkNull(conditionPO
				.getAppendlabourReason());

		TtAsWrApplicationPO claimPO = new TtAsWrApplicationPO();
		claimPO.setUpdateBy(userId);
		claimPO.setSectionDate(new Date());
		claimPO.setUpdateDate(new Date());
		claimPO.setAuthCode(nextCode);
		claimPO.setAuditType(Constant.IS_MANUAL_AUDITING + "");// 人工审核标识
		claimPO.setOemOption(remark);
		claimPO.setAppendlabourReason(appendlabourReason);
		claimPO.setAuditOpinion("");

		if (appendLabourNum == null || "".equals(appendLabourNum))
			appendLabourNum = "0";
		claimPO.setAppendlabourNum(CommonUtils.parseDouble(appendLabourNum));

		// 计算工时费用,同时将追加费用加入到结算金额中
		Double amount = labourPrice * CommonUtils.parseDouble(appendLabourNum);
		/** addUser:xiongchuan addDate:2010-12-14* */
		claimPO.setAppendlabourAmount(amount);
		claimPO.setBalanceAppendlabourAmount(amount);
		/** endUser:xiongchuan endDate:2010-12-14* */
		claimPO.setBalanceAmount(allBlance + amount);
		/** 索赔单结算时间kevinyin* */
		claimPO.setAccountDate(new Date());

		// 审核通过
		if (nextCode == null || "".equals(nextCode)) {// 不存在其他需要审核的级别,将索赔单状态修改为结算支付
			claimPO.setStatus(status);
			/**
			 * 2012-2-3 取消该环节 熊川
			 * 
			 * //更新索赔单同结算端关系表中的状态 BalanceAuditingDao balanceAuditingDao =
			 * BalanceAuditingDao.getInstance();
			 * balanceAuditingDao.updateClaimStatus(Long.parseLong(claimId),
			 * Constant.STATUS_ENABLE);
			 */
		}

		ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
		mAuditingDao.updateClaimInfo(claimPO, Long.parseLong(claimId));
	}

	/**
	 * 逐条审批结算单中的索赔单 注：按用户选择的的查询条件，在满足条件的数据中一条接一条审批
	 */
	public void auditingOneByOne() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String claimNo = request.getParamValue("claimNo");// 索赔单号
			String vin = request.getParamValue("vin");// VIN
			String wrGroup = request.getParamValue("modelCode");// 物料组
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码
			String startDate = request.getParamValue("startDate");// 制单开始日期
			String endDate = request.getParamValue("endDate");// 制单结算日期
			String balanceId = request.getParamValue("id");// 结算单ID
			String claimType = request.getParamValue("CLAIM_TYPE");// 索赔单类型

			TtAsWrApplicationExtPO tawep = new TtAsWrApplicationExtPO();
			List<ClaimListBean> partls = new LinkedList<ClaimListBean>();
			List<ClaimListBean> itemls = new LinkedList<ClaimListBean>();
			List<TtAsWrNetitemExtPO> otherls = new LinkedList<TtAsWrNetitemExtPO>();
			List<FsFileuploadPO> attachLs = new LinkedList<FsFileuploadPO>();
			List<TtAsWrAppauthitemPO> appAuthls = new LinkedList<TtAsWrAppauthitemPO>();

			// zhumingwei 2011-03-04
			String count = "";
			// zhumingwei 2011-03-04

			TtAsWrWrauthorizationPO authReason = new TtAsWrWrauthorizationPO();
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			Map<String, Object> customerMap = new HashMap<String, Object>();

			String cantNotAudit = CommonUtils.checkNull(request
					.getParamValue("cantNotAudit"));

			List<TcCodePO> statusList = CommonUtils.findTcCodeByType(Integer
					.parseInt(Constant.IF_TYPE));// 是否同意状态

			claimNo = CommonUtils.checkNull(claimNo);
			vin = CommonUtils.checkNull(vin);
			wrGroup = CommonUtils.checkNull(wrGroup);
			startDate = CommonUtils.checkNull(startDate);
			endDate = CommonUtils.checkNull(endDate);
			dealerCode = CommonUtils.checkNull(dealerCode);

			act.setOutData("claimNo", claimNo);
			act.setOutData("vin", vin);
			act.setOutData("modelCode", wrGroup);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setOutData("balanceId", balanceId);
			act.setOutData("CLAIM_TYPE", claimType);
			act.setOutData("dealerCode", dealerCode);

			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			Long userId = logonUser.getUserId();
			TcUserPO userPO = auditingDao.queryUserById(userId);
			String authCode = "";
			if (userPO.getBalanceLevelCode() != null)
				authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

			// 查询满足条件的一条索赔单(只查询未做结算审核的索赔单)
			auditBean conditionBean = new auditBean();
			conditionBean.setClaimNo(claimNo);
			conditionBean.setVin(vin);
			conditionBean.setModelCode(wrGroup);
			conditionBean
					.setStartDate(Utility.testString(startDate) ? (startDate + " 00:00:00")
							: startDate);
			conditionBean
					.setEndDate(Utility.testString(endDate) ? (endDate + " 23:59:59")
							: endDate);
			conditionBean.setId(balanceId);
			conditionBean.setAuthCode(authCode);
			conditionBean.setDealerCode(dealerCode);
			conditionBean.setClaimStatus(Constant.CLAIM_APPLY_ORD_TYPE_08
					.toString());
			conditionBean.setClaimType(claimType);
			conditionBean.setCantNotAudit(cantNotAudit);

			PageResult<Map<String, Object>> claimResult = auditingDao
					.queryAccAuditById(conditionBean, 1, 1);
			List<Map<String, Object>> claimList = claimResult.getRecords();
			String hasNext = "true";
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			if (claimList != null && claimList.size() > 0) {
				String claimId = claimList.get(0).get("CLAIM_ID").toString();

				tawep = auditingDao.queryClaimOrderDetailById(claimId);

				List<Map<String, Object>> customerList = dao
						.getVinUserName2(claimId);
				customerMap = new HashMap<String, Object>();
				if (customerList != null && customerList.size() > 0)
					customerMap = customerList.get(0);

				// 取得产地名称
				String yieldly = tawep.getYieldly();
				String yieldlyName = "";
				if (yieldly != null) {
					TcCodePO codePO = CommonUtils
							.findTcCodeDetailByCodeId(CommonUtils
									.parseInteger(yieldly));
					if (codePO != null && codePO.getCodeDesc() != null)
						yieldlyName = codePO.getCodeDesc();
				}
				tawep.setYieldlyName(yieldlyName);

				partls = dao.queryPartById(claimId); // 取配件信息
				itemls = dao.queryItemById(claimId); // 取工时
				otherls = dao.queryOtherByid(claimId);// 取其他项目
				attachLs = dao.queryAttById(claimId);// 取得附件
				appAuthls = dao.queryAppAuthInfo(claimId);// 审核授权信息
				authReason = dao.queryAuthReason(claimId);// 需要授权原因

				// zhumingwei 2011-03-04
				count = auditingDao.countRepairTimes(claimId);
				List<Map<String, Object>> list = auditingDao
						.getAuthInfo(claimId);
				String APPROVAL_PERSON = ((Map) list.get(0)).get(
						"APPROVAL_PERSON").toString();
				act.setOutData("APPROVAL_PERSON", APPROVAL_PERSON);
				// zhumingwei 2011-03-04

				statusList = CommonUtils.findTcCodeByType(Integer
						.parseInt(Constant.IF_TYPE));// 是否同意状态

			} else {
				hasNext = "false";
			}
			List<Map<String, Object>> listOutRepair = dao.viewOutRepair(tawep
					.getRoNo());// 根据索赔单的工单号查询工单的外出维修信息
			if (listOutRepair.size() > 0) {
				act.setOutData("listOutRepair", listOutRepair.get(0));// 外出
			}
			act.setOutData("application", tawep);
			act.setOutData("attachLs", attachLs);
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("statusList", statusList);// 是否同意状态
			act.setOutData("BALANCE_ID", balanceId);// 结算单ID

			// zhumingwei 2011-03-04
			act.setOutData("count", count);
			// zhumingwei 2011-03-04

			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11

			act.setOutData("HASNEXT", hasNext);
			act.setOutData("cantNotAudit", cantNotAudit);
			act.setOutData("isContinue", "true");
			act.setForword(this.BALANCE_AUDITING_ONEBYONE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室逐条审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/** ********add by liuxh 20101209 索赔单申请时跳过功能********** */
	public void auditingSkip() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {

			RequestWrapper request = act.getRequest();
			String claimNo = request.getParamValue("claimNo");// 索赔单号
			String vin = request.getParamValue("vin");// VIN
			String wrGroup = request.getParamValue("modelCode");// 物料组
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码
			String startDate = request.getParamValue("startDate");// 制单开始日期
			String endDate = request.getParamValue("endDate");// 制单结算日期
			String balanceId = request.getParamValue("id");// 结算单ID
			String claimType = request.getParamValue("CLAIM_TYPE");// 索赔单类型
			String cantNotAudit = CommonUtils.checkNull(request
					.getParamValue("cantNotAudit"));
			String claimIdCur = request.getParamValue("claimId");// 索赔申请单ID
			if (cantNotAudit.equals("")) {
				cantNotAudit = claimIdCur;
			} else {
				cantNotAudit = cantNotAudit + "," + claimIdCur;
			}
			TtAsWrApplicationExtPO tawep = new TtAsWrApplicationExtPO();
			List<ClaimListBean> partls = new LinkedList<ClaimListBean>();
			List<ClaimListBean> itemls = new LinkedList<ClaimListBean>();
			List<TtAsWrNetitemExtPO> otherls = new LinkedList<TtAsWrNetitemExtPO>();
			List<FsFileuploadPO> attachLs = new LinkedList<FsFileuploadPO>();
			List<TtAsWrAppauthitemPO> appAuthls = new LinkedList<TtAsWrAppauthitemPO>();
			TtAsWrWrauthorizationPO authReason = new TtAsWrWrauthorizationPO();
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			Map<String, Object> customerMap = new HashMap<String, Object>();

			List<TcCodePO> statusList = CommonUtils.findTcCodeByType(Integer
					.parseInt(Constant.IF_TYPE));// 是否同意状态

			claimNo = CommonUtils.checkNull(claimNo);
			vin = CommonUtils.checkNull(vin);
			wrGroup = CommonUtils.checkNull(wrGroup);
			startDate = CommonUtils.checkNull(startDate);
			endDate = CommonUtils.checkNull(endDate);
			dealerCode = CommonUtils.checkNull(dealerCode);

			act.setOutData("claimNo", claimNo);
			act.setOutData("vin", vin);
			act.setOutData("modelCode", wrGroup);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setOutData("balanceId", balanceId);
			act.setOutData("CLAIM_TYPE", claimType);
			act.setOutData("dealerCode", dealerCode);

			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			Long userId = logonUser.getUserId();
			TcUserPO userPO = auditingDao.queryUserById(userId);
			String authCode = "";
			if (userPO.getBalanceLevelCode() != null)
				authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

			// 查询满足条件的一条索赔单(只查询未做结算审核的索赔单)

			auditBean conditionBean = new auditBean();
			conditionBean.setClaimNo(claimNo);
			conditionBean.setVin(vin);
			conditionBean.setModelCode(wrGroup);
			conditionBean
					.setStartDate(Utility.testString(startDate) ? (startDate + " 00:00:00")
							: startDate);
			conditionBean
					.setEndDate(Utility.testString(endDate) ? (endDate + " 23:59:59")
							: endDate);
			conditionBean.setId(balanceId);
			conditionBean.setAuthCode(authCode);
			conditionBean.setDealerCode(dealerCode);
			conditionBean.setClaimStatus(Constant.CLAIM_APPLY_ORD_TYPE_08
					.toString());
			conditionBean.setClaimType(claimType);
			conditionBean.setCantNotAudit(cantNotAudit);

			PageResult<Map<String, Object>> claimResult = auditingDao
					.queryAccAuditSkipById(conditionBean, 1, 1);
			List<Map<String, Object>> claimList = claimResult.getRecords();
			String hasNext = "true";
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			if (claimList != null && claimList.size() > 0) {
				String claimId = claimList.get(0).get("CLAIM_ID").toString();

				tawep = auditingDao.queryClaimOrderDetailById(claimId);

				List<Map<String, Object>> customerList = dao
						.getVinUserName2(claimId);
				customerMap = new HashMap<String, Object>();
				if (customerList != null && customerList.size() > 0)
					customerMap = customerList.get(0);
				// 如果是服务活动，取出活动代码与名称
				if (Constant.CLA_TYPE_06.toString().equals(
						tawep.getClaimType().toString())) {
					List actList = auditingDao.getActivity(claimId);
					if (actList.size() > 0) {
						act.setOutData("activity", actList.get(0));
					}
				}
				// 取得产地名称
				String yieldly = tawep.getYieldly();
				String yieldlyName = "";
				if (yieldly != null) {
					TcCodePO codePO = CommonUtils
							.findTcCodeDetailByCodeId(CommonUtils
									.parseInteger(yieldly));
					if (codePO != null && codePO.getCodeDesc() != null)
						yieldlyName = codePO.getCodeDesc();
				}
				tawep.setYieldlyName(yieldlyName);

				partls = dao.queryPartById(claimId); // 取配件信息
				itemls = dao.queryItemById(claimId); // 取工时
				otherls = dao.queryOtherByid(claimId);// 取其他项目
				attachLs = dao.queryAttById(claimId);// 取得附件
				appAuthls = dao.queryAppAuthInfo(claimId);// 审核授权信息
				authReason = dao.queryAuthReason(claimId);// 需要授权原因
				statusList = CommonUtils.findTcCodeByType(Integer
						.parseInt(Constant.IF_TYPE));// 是否同意状态
				// zhumingwei 2011-03-10
				String count = auditingDao.countRepairTimes(claimId);
				act.setOutData("count", count);
				List<Map<String, Object>> list = auditingDao
						.getAuthInfo(claimId);
				String APPROVAL_PERSON = ((Map) list.get(0)).get(
						"APPROVAL_PERSON").toString();
				act.setOutData("APPROVAL_PERSON", APPROVAL_PERSON);
				// zhumingwei 2011-03-10
			} else {
				hasNext = "false";
			}
			List<Map<String, Object>> listOutRepair = dao.viewOutRepair(tawep
					.getRoNo());// 根据索赔单的工单号查询工单的外出维修信息
			if (listOutRepair.size() > 0) {
				act.setOutData("listOutRepair", listOutRepair.get(0));// 外出
			}

			act.setOutData("application", tawep);
			act.setOutData("attachLs", attachLs);
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("statusList", statusList);// 是否同意状态
			act.setOutData("BALANCE_ID", balanceId);// 结算单ID
			act.setOutData("HASNEXT", hasNext);
			act.setOutData("isContinue", "true");
			act.setOutData("cantNotAudit", cantNotAudit);

			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11
			act.setForword(this.BALANCE_AUDITING_ONEBYONE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室逐条审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/** ********add by liuxh 20101209 索赔单申请时跳过功能********** */
	/**
	 * 更新索赔单中的结算金额
	 * 
	 * @param claimId
	 */
	public void modifyClaimAmount(Long claimId) {

		ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
		// 当索赔单类型为 退回时 更新所有子表(配件、工时和其他项目)审核状态和金额
		mAuditingDao.modifyBackClaimDetail(claimId);
		// 更新结算工时、配件、和其他项目金额
		mAuditingDao.modifyClaimBalanceDetailAmount(claimId, true);
		// 更新索赔单总结算金额
		mAuditingDao.modifyClaimBalanceAmount(claimId, true);
		mAuditingDao.modifyClaimBalanceAmount2(claimId, true);
	}

	/**
	 * 结算单更新索赔单中的结算金额
	 * 
	 * @param claimId
	 */
	public void modifyClaimAmountByBalance(Long claimId) {
		ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
		// 更新结算工时、配件、和其他项目金额
		mAuditingDao.modifyClaimBalanceDetailAmount(claimId, false);
		// 更新索赔单总结算金额
		mAuditingDao.modifyClaimBalanceAmount(claimId, false);

	}

	/**
	 * 索赔申请重新审核作业 初始化
	 */
	public void claimReAuditingInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUser
					.getPoseId());
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			act.setOutData("yieldly", yieldly);
			act.setForword(this.MANUAL_REAUDITING);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, this.FUNCTION_DESC);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 索赔申请单重新审核查询 规则：
	 */
	public void claimManualReAuditingQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		ClaimManualAuditingDao maDao = new ClaimManualAuditingDao();
		try {
			// 取得查询参数
			RequestWrapper request = act.getRequest();
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码（多个）
			String dealerName = request.getParamValue("DEALER_NAME");// 经销商名称
			String roNo = request.getParamValue("CON_RO_NO");// 工单号
			String lineNo = request.getParamValue("CON_LINE_NO");// 行号
			String claimType = request.getParamValue("CLAIM_TYPE");// 索赔类型
			String vin = request.getParamValue("CON_VIN");// 车辆唯一标识码
			String applyStartDate = request
					.getParamValue("CON_APPLY_DATE_START");// 申请日期范围（开始时间）
			String applyEndDate = request.getParamValue("CON_APPLY_DATE_END");// 申请日期范围（结束时间）
			String claimStatus = Constant.CLAIM_APPLY_ORD_TYPE_04.toString();// 状态
																				// 固定为
																				// "审核通过"
			String claimNo = request.getParamValue("CLAIM_NO");// 索赔申请单号
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);// 用户所属公司
			String status = request.getParamValue("STATUS");// 索赔单状态
			String yieldly = request.getParamValue("YIELDLY");// 查询条件--产地
			/** *addUser xiongchuan 2011-09-22**start** */
			String claimZhishun = request.getParamValue("CLAIM_ZHISHUN");// 售前质损类型
			/** *addUser xiongchuan 2011-09-22**end*** */
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUser
					.getPoseId());// 该用户拥有的产地权限
			Long orgId = logonUser.getOrgId();// 组织ID
			Long poseId = logonUser.getPoseId();// 职位ID
			// 查询职位中业务范围
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			// 添加审核时间，审核人查询条件
			String approveDate = request.getParamValue("approveDate");
			String approveDate2 = request.getParamValue("approveDate2");
			String approveName = request.getParamValue("approveName");

			Integer curPage = request.getParamValue("curPage") == null ? 1
					: Integer.parseInt(request.getParamValue("curPage"));// 分页首页代码

			if (Utility.testString(applyStartDate))
				applyStartDate = applyStartDate + " 00:00:00";

			if (Utility.testString(applyEndDate))
				applyEndDate = applyEndDate + " 23:59:59";

			ClaimOrderBean orderBean = new ClaimOrderBean();
			orderBean.setDealerCodes(dealerCode);
			orderBean.setDealerName(dealerName);
			orderBean.setRoNo(roNo);
			orderBean.setLineNo(lineNo);
			orderBean.setClaimType(claimType);
			orderBean.setVin(vin);
			orderBean.setApplyEndDate(applyEndDate);
			orderBean.setApplyStartDate(applyStartDate);
			orderBean.setClaimStatus(claimStatus);
			orderBean.setClaimNo(claimNo);
			orderBean.setCompanyId(companyId);
			orderBean.setPoseId(poseId);
			orderBean.setOrgId(orgId);
			orderBean.setAreaIds(CommonUtils.checkNull(areaIds));
			orderBean.setYieldly(yieldly);
			orderBean.setYieldlys(yieldlys);
			orderBean.setStatus(status);
			orderBean.setApproveDate(approveDate);
			orderBean.setApproveDate2(approveDate2);
			orderBean.setApproveName(approveName);
			orderBean.setClaimZhishun(CommonUtils.checkNull(claimZhishun));// 售前质损
			// 查询索赔申请单
			PageResult<Map<String, Object>> result = maDao.queryClaim(
					orderBean, curPage, Constant.PAGE_SIZE);

			act.setOutData("ps", result);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔申请单上报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 重新授权审核
	 * 
	 * <pre>
	 * 步骤：
	 * 1、更新索赔之配件信息（结算金额）
	 * 2、更新索赔之工时信息（结算金额）
	 * 3、更新索赔之其他项目信息（结算金额）
	 * 4、记录审核授权状态过程
	 * 5、更新索赔申请单授权，将审核授权级别中审核完成的去掉
	 * 6、更新索赔申请单状态、下一步需要审核的级别和结算总金额
	 * </pre>
	 */
	public void reAuditing() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		try {
			RequestWrapper request = act.getRequest();
			Long userId = logonUser.getUserId();// 登陆用户ID
			String claimId = request.getParamValue("claimId");// 索赔申请单ID
			String status = request.getParamValue("status");// 审核状态(审核通过、审核退回、审核拒绝)
			String partIds[] = request.getParamValues("PART_ID");// 索赔之配件信息ID
			String labourIds[] = request.getParamValues("LABOUR_ID");// 索赔之工时信息ID
			String otherIds[] = request.getParamValues("OTHER_ID");// 索赔之其他项目信息ID
			String partBlance[] = request.getParamValues("PARTAMOUNTTD");// 索赔之配件结算金额
			String labourBlance[] = request.getParamValues("LOBOURAMOUNTTD");// 索赔之工时结算金额
			String otherBlance[] = request.getParamValues("OTHERAMOUNT");// 索赔之其他项目结算金额
			String partStatus[] = request.getParamValues("PARTSTATUS");// 索赔之配件结算金额
			String labourStatus[] = request.getParamValues("LOBOURSTATUS");// 索赔之工时结算金额
			String partCount[] = request.getParamValues("PARTBCOUNTTD");// 索赔之配件结算数量
			String labourCount[] = request.getParamValues("LABOURBCOUNTTD");// 索赔之工时结算数量
			String otherStatus[] = request.getParamValues("OTHERSTATUS");// 索赔之其他项目结算金额
			String remark = request.getParamValue("CON_REMARK");// 审核意见
			String appendLabour = request.getParamValue("APPEND_LABOUR");// 追加工时数
			String labourPrice = request.getParamValue("labourPrice");// 工时单价

			remark = CommonUtils.checkNull(remark) + " [追加工时数：" + appendLabour
					+ "]";

			// 1、更新索赔之配件信息（结算金额）
			double pBlance = this.updatePartInfoByStatus(partIds, partBlance,
					partStatus, partCount, userId);
			// 2、更新索赔之工时信息（结算金额）
			double lBlance = this.updateLabourInfoByStatus(labourIds,
					labourBlance, labourStatus, labourCount, userId);
			// 3、更新索赔之其他项目信息（结算金额）
			double oBlance = this.updateOtherInfoByStatus(otherIds,
					otherBlance, otherStatus, userId);

			double allBlance = pBlance + lBlance + oBlance;// 结算总金额

			// 4、记录审核授权状态过程
			this.recordAuthProcess(status, userId, claimId, remark, " [重授权审核]",
					false);
			// 5、更新索赔申请单状态、下一步需要审核的级别和结算总金额
			// this.updateClaimOrder(status,userId,allBlance,claimId,"",remark,appendLabour,companyId);
			TtAsWrApplicationPO conditionPO = new TtAsWrApplicationPO();
			conditionPO.setStatus(Integer.parseInt(status));
			conditionPO.setAuthCode(""); // 清空审核级别
			conditionPO.setUpdateBy(userId);
			conditionPO.setBalanceAmount(allBlance);
			conditionPO.setRemark(remark);
			conditionPO.setId(Long.parseLong(claimId));

			if (!Utility.testString(labourPrice)
					|| Constant.CLAIM_APPLY_ORD_TYPE_06.toString().equals(
							status))
				labourPrice = "0";
			if (Constant.CLAIM_APPLY_ORD_TYPE_06.toString().equals(status))
				appendLabour = "0";
			this.updateClaimOrder("", appendLabour, Double
					.parseDouble(labourPrice), conditionPO);
			// 51、根据工时、配件、其他项目和附加工时回写索赔单中对应的结算金额和结算金额汇总
			this.modifyClaimAmount(Long.parseLong(claimId));
			act.setOutData("ACTION_RESULT", 1);
			act.setOutData("FORWORD_URL", this.MANUAL_REAUDITING);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "技术室授权审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 结算室审核(批量审核)
	 */
	public void batchAuditingBlance() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		RequestWrapper request = act.getRequest();
		String claimIds[] = request.getParamValues("claimId");// 索赔单ID
		String id = request.getParamValue("id");// 结算单ID
		boolean flag = DBLockUtil.lock(id, DBLockUtil.BUSINESS_TYPE_01);
		if (!flag) {
			act.setOutData("SUCCESS", "LOCK");
			return;
		}
		try {
			ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
			BalanceAuditingDao balanceAuditingDao = BalanceAuditingDao
					.getInstance();
			ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();

			if (claimIds != null && claimIds.length > 0) {
				Long userId = logonUser.getUserId();
				TcUserPO userPO = dao.queryUserById(userId);
				String authCode = "";
				if (userPO.getBalanceLevelCode() != null)
					authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

				for (int k = 0; k < claimIds.length; k++) {
					String claimId = claimIds[k];
					// 索赔申请单信息
					TtAsWrApplicationPO claimVO = auditingDao
							.queryClaimById(claimId);
					if (!authCode.equals(claimVO.getAuthCode())) {// 该索赔单已经审核过
						continue;
					}
					// 2、根据索赔申请单中当前审核步骤，确定下一审核步骤
					String nextCode = this.getNextAuthCode(claimVO);
					// 3、更新索赔申请单状态、下一步需要审核的级别和结算总金额
					TtAsWrApplicationPO claimPO = new TtAsWrApplicationPO();
					claimPO.setUpdateBy(userId);
					claimPO.setUpdateDate(new Date());
					claimPO.setAuthCode(nextCode);
					claimPO.setAuditType(Constant.IS_MANUAL_AUDITING + "");// 人工审核标识

					String status = Constant.CLAIM_APPLY_ORD_TYPE_08.toString();
					// 审核通过
					if (nextCode == null || "".equals(nextCode)) {// 不存在其他需要审核的级别,将索赔单状态修改为结算支付
						claimPO.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_07);
						balanceAuditingDao.updateClaimStatus(Long
								.parseLong(claimId), Constant.STATUS_ENABLE);
						status = Constant.CLAIM_APPLY_ORD_TYPE_07.toString();
					}

					// 批量审核索赔单时间
					claimPO.setAccountDate(new Date());

					// 1、记录审核授权状态过程
					this.recordAuthProcess(status, userId, claimId, "[批量结算审核]",
							" [结算审核]", true);
					// 2、更新索赔申请单状态、下一步需要审核的级别和结算总金额
					dao.updateClaimInfo(claimPO, Long.parseLong(claimId));
				}
			}
			/** ****mod by liuxh 20101126 批量审核索赔单时不调用更新结算单状态和结算金额***** */
			// 更新结算单中结算金额和结算单状态
			// BalanceAuditing bAuditing = new BalanceAuditing("","");
			// bAuditing.modifyBalanceStatus(Long.parseLong(id), true,
			// logonUser);
			/** ****mod by liuxh 20101126 批量审核索赔单时不调用更新结算单状态和结算金额***** */

			act.setOutData("SUCCESS", "SUCCESS");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室批量审核");
			logger.error(logonUser, e1);
			act.setOutData("SUCCESS", "FAILURE");
			act.setException(e1);
		} finally {
			DBLockUtil.freeLock(id, DBLockUtil.BUSINESS_TYPE_01);
		}
	}

	/** ******add by liuxh 20101126 增加结算室审核完成功能 调用更新结算标志和重新计算********* */
	public void settComplete() {
		ActionContext act = ActionContext.getContext();
		try {
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			String blanceId = CommonUtils.checkNull(request
					.getParamValue("blanceId"));
			BalanceAuditing bAuditing = new BalanceAuditing("", "");

			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();

			/** *******判断索赔单是否全部审核完成********* */
			boolean flagClaim = balanceDao.queryClaimByBanlanceId(Long
					.parseLong(blanceId));
			boolean flagSp = balanceDao.querySpecByBanlanceId(Long
					.parseLong(blanceId));
			if (!flagClaim) {
				throw new BizException("结算单下的索赔单未完全审核完成!");
			}
			if (!flagSp) {
				throw new BizException("结算单下的特殊费用工单未完全审核完成!");
			}
			/** *******判断索赔单是否全部审核完成********* */

			// 2、根据标识的状态和特殊费用的状态统计特殊费用
			List<Map<String, Object>> feeList = balanceDao
					.getSpecialFeeByBalanceIdStatus(Long.parseLong(blanceId));
			double marketFee = 0;// 市场工单费用
			double outFee = 0; // 特殊外出费用
			int feeCount = 0;
			if (feeList != null && feeList.size() > 0) {
				for (Map<String, Object> feeMap : feeList) {
					if (feeMap.containsKey("FEE_TYPE")) {
						if ((Constant.FEE_TYPE_01).equals(feeMap
								.get("FEE_TYPE").toString())) {
							if (feeMap.containsKey("DECLARE_SUM")) {
								marketFee = ((BigDecimal) feeMap
										.get("DECLARE_SUM")).doubleValue();
							}
							if (feeMap.containsKey("FEETYPECOUNT")) {
								feeCount = feeCount
										+ ((BigDecimal) feeMap
												.get("FEETYPECOUNT"))
												.intValue();
							}
						} else if (Constant.FEE_TYPE_02.equals(feeMap.get(
								"FEE_TYPE").toString())) {
							if (feeMap.containsKey("DECLARE_SUM")) {
								outFee = ((BigDecimal) feeMap
										.get("DECLARE_SUM")).doubleValue();
							}
							if (feeMap.containsKey("FEETYPECOUNT")) {
								feeCount = feeCount
										+ ((BigDecimal) feeMap
												.get("FEETYPECOUNT"))
												.intValue();
							}
						}
					}
				}
			} else {
				feeCount = 0;
			}

			// 3、将特殊费用合计到结算单中
			balanceDao.addSpecialFeeToBalanceOrder(Long.parseLong(blanceId),
					marketFee, outFee, feeCount);
			balanceDao.updateMarkSpeeActiveFee(Long.parseLong(blanceId));// add
																			// by
																			// liuxh
																			// 20101227
																			// 完成时记录市场公单和外出公单费用
			bAuditing.modifyBalanceStatus(Long.parseLong(blanceId), true,
					logonUser);
			act.setOutData("msg", "true");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			act.setException(e);
		}

	}

	/** ******add by liuxh 20101126 增加结算室审核完成功能 调用更新结算标志和重新计算********* */

	/** ***************************微车流程*********************************** */
	/** ******add by kevinyin 20110413查询需要结算室审核的索赔单********* */
	/**
	 * 查询状态为结算支付和结算中的索赔单
	 * 
	 */
	public void acconeAuditClaimOneByOne() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			if (!"".equals(Command)) {
				// CommonUtils.checkNull() 校验是否为空
				String id = request.getParamValue("id");
				String claimNo = request.getParamValue("claimNo");// 索赔单ID
				String modelCode = request.getParamValue("modelCode");// 车型代码
				String vin = request.getParamValue("vin");
				String claimType = request.getParamValue("CLAIM_TYPE");// 索赔单类型
				String dealerCode = request.getParamValue("dealerCode");// 经销商代码
				String startDate = CommonUtils.checkNull(request
						.getParamValue("startDate")); // 创建开始时间
				String endDate = CommonUtils.checkNull(request
						.getParamValue("endDate")); // 创建结束时间
				String claimStatus = request.getParamValue("claimStatus");// 索赔单状态
				String userAuth = CommonUtils.checkNull(request
						.getParamValue("checkUserSel")); // 权限审核人
				String invoiceStatus = CommonUtils.checkNull(request
						.getParamValue("invoiceStatus")); // 是否开票状态
				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();

				// 当开始时间和结束时间相同时
				if (null != startDate && !"".equals(startDate)
						&& null != endDate && !"".equals(endDate)) {
					startDate = startDate + " 00:00:00";
					endDate = endDate + " 23:59:59";
				}
				Long userId = logonUser.getUserId();
				TcUserPO userPO = dao.queryUserById(userId);
				String authCode = "";
				if (userPO.getBalanceLevelCode() != null)
					authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

				auditBean bean = new auditBean();
				bean.setId(id);
				bean.setClaimNo(claimNo);
				bean.setModelCode(modelCode);
				bean.setVin(vin);
				bean.setStartDate(startDate);
				bean.setEndDate(endDate);
				bean.setOemCompanyId(String.valueOf(GetOemcompanyId
						.getOemCompanyId(logonUser)));
				if ("".equals(userAuth)) {
					bean.setAuthCodeOrder(userId.toString());
				} else {
					bean.setAuthCodeOrder(userAuth);
				}
				bean.setAuthCode(authCode);
				bean.setDealerCode(dealerCode);
				bean.setClaimType(CommonUtils.checkNull(claimType));
				bean.setClaimStatus(claimStatus);

				// 分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页

				Integer pageSize = 100;

				Long orgId = logonUser.getOrgId();// 组织ID
				Long poseId = logonUser.getPoseId();// 职位ID
				// 查询职位中业务范围
				String areaIds = MaterialGroupManagerDao
						.getAreaIdsByPoseId(poseId);
				PageResult<Map<String, Object>> ps = dao.queryAuditingClaim(
						bean, areaIds, invoiceStatus, curPage, pageSize);

				// 分页方法 end
				act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
			} else {
				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
				Long userId = logonUser.getUserId();
				TcUserPO userPO = dao.queryUserById(userId);
				String authCode = "";
				if (userPO.getBalanceLevelCode() != null)
					authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

				// 审核人员查询
				List<TcUserPO> listUser = dao.queryAuditingUser();
				act.setOutData("listUser", listUser);
				act.setOutData("currentUser", logonUser.getUserId());

				act.setOutData("authCode", authCode);
				act.setOutData("id", "111111111");
				act.setForword(acconeAuditClaim);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/** ******add by kevinyin 20110413查询需要结算室审核的索赔单********* */

	/** ******add by kevinyin 20110414查询需要结算室审核的索赔单********* */

	/**
	 * 新结算室授权审核
	 * 
	 * <pre>
	 * 步骤：
	 * 1、更新索赔之配件信息（结算金额）
	 * 2、更新索赔之工时信息（结算金额）
	 * 3、更新索赔之其他项目信息（结算金额）
	 * 4、记录审核授权状态过程
	 * 5、更新索赔申请单授权，将审核授权级别中审核完成的去掉
	 * 6、更新索赔申请单状态、下一步需要审核的级别和结算总金额
	 * 7、当对应结算单中的索赔单都审核通过时，重新统计结算单中数据和结算单明细中数据
	 *    同时更改索赔单状态。
	 * </pre>
	 */
	public void claimAuditing() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		try {
			RequestWrapper request = act.getRequest();
			String isReDo = CommonUtils.checkNull(request
					.getParamValue("isRedo"));
			Long userId = logonUser.getUserId();// 登陆用户ID
			String claimId = request.getParamValue("claimId");// 索赔申请单ID
			String balanceId = request.getParamValue("id");// 结算单ID
			String status = request.getParamValue("status");// 审核状态(审核通过、审核退回、审核拒绝)
			String partIds[] = request.getParamValues("PART_ID");// 索赔之配件信息ID
			String labourIds[] = request.getParamValues("LABOUR_ID");// 索赔之工时信息ID
			String otherIds[] = request.getParamValues("OTHER_ID");// 索赔之其他项目信息ID
			String partBlance[] = request.getParamValues("PARTAMOUNT");// 索赔之配件结算金额
			String partRemark[] = request.getParamValues("PARTREMARK");// 索赔之配件之备注信息
			String partBlanceCount[] = request.getParamValues("PARTCOUNT");// 索赔之配件结算数量
			String labourBlance[] = request.getParamValues("LOBOURAMOUNT");// 索赔之工时结算金额
			String labourBlanceCount[] = request.getParamValues("LOBOURCOUNT");// 索赔之工时结算数量
			String labourRemark[] = request.getParamValues("LOBOURREMARK");// 索赔之工时之备注
			String otherBlance[] = request.getParamValues("OTHERAMOUNT");// 索赔之其他项目结算金额
			String auditCon[] = request.getParamValues("audit_con");// 其它费用审核意见
			String remark = request.getParamValue("CON_REMARK");// 审核意见
			String isContinue = request.getParamValue("isContinue");// 是否连续审核
																	// true :连续
																	// 其他:单条审核
			String appendLabour = request.getParamValue("APPEND_LABOUR");// 追加工时数
			String labourPrice = request.getParamValue("labourPrice");// 工时单价

			int flag = 1;// 审核标识(1:正常 2:该单已经审核过)
			if (this.isBalanceAuditing(claimId, userId, isReDo)) {// 不可以再次审核，该索赔单已经审核过
				flag = 2;
			} else {
				// 1、更新索赔之配件信息（结算金额）
				double pBlance = this.updatePartInfo(partIds, partBlance,
						partBlanceCount, partRemark, userId);
				// 2、更新索赔之工时信息（结算金额）
				double lBlance = this.updateLabourInfo(labourIds, labourBlance,
						labourBlanceCount, labourRemark, userId);
				// 3、更新索赔之其他项目信息（结算金额）
				double oBlance = this.updateOtherInfo(otherIds, otherBlance,
						userId, auditCon);

				double allBlance = pBlance + lBlance + oBlance;// 结算总金额

				// 4、记录审核授权状态过程
				this.recordAuthProcess(status, userId, claimId, remark,
						" [结算审核]", true);
				// 5、根据索赔申请单中当前审核步骤，确定下一审核步骤
				String nextCode = this.getNextAuthCode(claimId);
				// 6、更新索赔申请单状态、下一步需要审核的级别、结算总金额和结算工时数
				TtAsWrApplicationPO conditionPO = new TtAsWrApplicationPO();
				// conditionPO.setStatus(Integer.parseInt(status));
				conditionPO.setUpdateBy(userId);
				conditionPO.setBalanceAmount(allBlance);
				conditionPO.setRemark(remark);
				conditionPO.setId(Long.parseLong(claimId));
				/** addUser:xiongchuan addDate:2010-12-14* */

				/** endUser:xiongchuan endDate:2010-12-14* */
				if (!Utility.testString(labourPrice))
					labourPrice = "0";
				this.updateBalanceClaimOrder(nextCode,
						Integer.parseInt(status), appendLabour, Double
								.parseDouble(labourPrice), conditionPO);
				// 61、根据工时、配件、其他项目和附加工时回写索赔单中对应的结算金额和结算金额汇总
				this.modifyClaimAmountByBalance(Long.parseLong(claimId));

				/** ******mod by liuxh 20101126 逐条审批时关闭更新结算单状态和金额********* */
				// 7、更新结算单中结算金额和结算单状态
				// BalanceAuditing bAuditing = new BalanceAuditing("","");
				// bAuditing.modifyBalanceStatus(Long.parseLong(balanceId),
				// true,logonUser);
				/** ******mod by liuxh 20101126 逐条审批时关闭更新结算单状态和金额********* */
			}
			act.setOutData("isContinue", isContinue);
			act.setOutData("ISAUDITING", flag);
			act.setOutData("ACTION_RESULT", 1);

			if (!"true".equals(isContinue)) {// 单条审核
				act.setOutData("FORWORD_URL", this.MANUAL_AUDITING);
			} else {
				this.auditingClaimOneByOne();
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "技术室授权审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 逐条审批结算单中的索赔单 注：按用户选择的的查询条件，在满足条件的数据中一条接一条审批
	 */
	public void auditingClaimOneByOne() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String claimNo = request.getParamValue("claimNo");// 索赔单号
			String vin = request.getParamValue("vin");// VIN
			String wrGroup = request.getParamValue("modelCode");// 物料组
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码
			String startDate = request.getParamValue("startDate");// 制单开始日期
			String endDate = request.getParamValue("endDate");// 制单结算日期
			String balanceId = request.getParamValue("id");// 结算单ID
			String claimType = request.getParamValue("CLAIM_TYPE");// 索赔单类型
			String claimStatus = request.getParamValue("claimStatus");// 索赔单状态
			String checkUserSel = request.getParamValue("checkUserSel");// 审核人员
			String invoiceStatus = request.getParamValue("invoiceStatus");// 是否开票

			TtAsWrApplicationExtPO tawep = new TtAsWrApplicationExtPO();
			List<ClaimListBean> partls = new LinkedList<ClaimListBean>();
			List<ClaimListBean> itemls = new LinkedList<ClaimListBean>();
			List<TtAsWrNetitemExtPO> otherls = new LinkedList<TtAsWrNetitemExtPO>();
			List<FsFileuploadPO> attachLs = new LinkedList<FsFileuploadPO>();
			List<TtAsWrAppauthitemPO> appAuthls = new LinkedList<TtAsWrAppauthitemPO>();

			// zhumingwei 2011-03-04
			String count = "";
			// zhumingwei 2011-03-04

			TtAsWrWrauthorizationPO authReason = new TtAsWrWrauthorizationPO();
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			Map<String, Object> customerMap = new HashMap<String, Object>();

			String cantNotAudit = CommonUtils.checkNull(request
					.getParamValue("cantNotAudit"));

			List<TcCodePO> statusList = CommonUtils.findTcCodeByType(Integer
					.parseInt(Constant.IF_TYPE));// 是否同意状态

			claimNo = CommonUtils.checkNull(claimNo);
			vin = CommonUtils.checkNull(vin);
			wrGroup = CommonUtils.checkNull(wrGroup);
			startDate = CommonUtils.checkNull(startDate);
			endDate = CommonUtils.checkNull(endDate);
			dealerCode = CommonUtils.checkNull(dealerCode);

			act.setOutData("claimNo", claimNo);
			act.setOutData("vin", vin);
			act.setOutData("modelCode", wrGroup);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setOutData("balanceId", balanceId);
			act.setOutData("CLAIM_TYPE", claimType);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("claimStatus", claimStatus);
			act.setOutData("checkUserSel", checkUserSel);
			act.setOutData("invoiceStatus", invoiceStatus);

			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			Long userId = logonUser.getUserId();

			TcUserPO userPO = auditingDao.queryUserById(userId);
			String authCode = "";
			if (userPO.getBalanceLevelCode() != null)
				authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

			// 查询满足条件的一条索赔单(只查询未做结算审核的索赔单)
			auditBean conditionBean = new auditBean();
			conditionBean.setClaimNo(claimNo);
			conditionBean.setVin(vin);
			conditionBean.setModelCode(wrGroup);
			conditionBean
					.setStartDate(Utility.testString(startDate) ? (startDate + " 00:00:00")
							: startDate);
			conditionBean
					.setEndDate(Utility.testString(endDate) ? (endDate + " 23:59:59")
							: endDate);
			conditionBean.setId(balanceId);
			conditionBean.setAuthCode(authCode);
			conditionBean.setDealerCode(dealerCode);
			conditionBean.setClaimStatus(Constant.CLAIM_APPLY_ORD_TYPE_08
					.toString());
			conditionBean.setClaimType(claimType);
			conditionBean.setCantNotAudit(cantNotAudit);
			conditionBean.setAuthCodeOrder(userId.toString());
			Long orgId = logonUser.getOrgId();// 组织ID
			Long poseId = logonUser.getPoseId();// 职位ID
			// 查询职位中业务范围
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			List<Map<String, Object>> claimResult = auditingDao
					.queryClaimAuditById(conditionBean, areaIds, 1, 1);
			List<Map<String, Object>> claimList = claimResult;
			String hasNext = "true";
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			if (claimList != null && claimList.size() > 0) {
				String claimId = claimList.get(0).get("CLAIM_ID").toString();

				tawep = auditingDao.queryClaimOrderDetailById(claimId);

				List<Map<String, Object>> customerList = dao
						.getVinUserName2(claimId);
				customerMap = new HashMap<String, Object>();
				if (customerList != null && customerList.size() > 0)
					customerMap = customerList.get(0);

				// 取得产地名称
				String yieldly = tawep.getYieldly();
				String yieldlyName = "";
				if (yieldly != null) {
					TcCodePO codePO = CommonUtils
							.findTcCodeDetailByCodeId(CommonUtils
									.parseInteger(yieldly));
					if (codePO != null && codePO.getCodeDesc() != null)
						yieldlyName = codePO.getCodeDesc();
				}
				tawep.setYieldlyName(yieldlyName);

				partls = dao.queryPartById(claimId); // 取配件信息
				itemls = dao.queryItemById(claimId); // 取工时
				otherls = dao.queryOtherByid(claimId);// 取其他项目
				attachLs = dao.queryAttById(claimId);// 取得附件
				appAuthls = dao.queryAppAuthInfo(claimId);// 审核授权信息
				authReason = dao.queryAuthReason(claimId);// 需要授权原因

				// zhumingwei 2011-03-04
				count = auditingDao.countRepairTimes(claimId);
				List<Map<String, Object>> list = auditingDao
						.getAuthInfo(claimId);
				String APPROVAL_PERSON = ((Map) list.get(0)).get(
						"APPROVAL_PERSON").toString();
				act.setOutData("APPROVAL_PERSON", APPROVAL_PERSON);
				// zhumingwei 2011-03-04

				statusList = CommonUtils.findTcCodeByType(Integer
						.parseInt(Constant.IF_TYPE));// 是否同意状态

			} else {
				hasNext = "false";
			}
			List<Map<String, Object>> listOutRepair = dao.viewOutRepair(tawep
					.getRoNo());// 根据索赔单的工单号查询工单的外出维修信息
			if (listOutRepair.size() > 0) {
				act.setOutData("listOutRepair", listOutRepair.get(0));// 外出
			}
			act.setOutData("application", tawep);
			act.setOutData("attachLs", attachLs);
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("statusList", statusList);// 是否同意状态
			act.setOutData("BALANCE_ID", balanceId);// 结算单ID

			// zhumingwei 2011-03-04
			act.setOutData("count", count);
			// zhumingwei 2011-03-04

			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11

			act.setOutData("HASNEXT", hasNext);
			act.setOutData("cantNotAudit", cantNotAudit);
			act.setOutData("isContinue", "true");

			// kevinyin 2011-05-07
			// 站长及三包员联系电话
			TmClaimContactPhonePO ccp = new TmClaimContactPhonePO();
			ccp.setDealerId(tawep.getDealerId());
			List<TmClaimContactPhonePO> listContact = dao.select(ccp);
			if (listContact.size() > 0) {
				act.setOutData("contact", listContact.get(0));
			}

			// kevinyin 2011-05-07

			act.setForword(this.claimAuditOneByOne);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室逐条审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 索赔单审核
	 */
	public void auditClaimInitNew() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			// String id = request.getParamValue("id");//结算单ID
			String isConfirm = request.getParamValue("isConfirm");// 是否进行收单操作
																	// true: 是
																	// false:不是
			if (!Utility.testString(isConfirm))
				isConfirm = "false";

			ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
			// TtAsWrClaimBalancePO balancePO =
			// dao.queryBalanceOrderById(Long.parseLong(id));

			Long userId = logonUser.getUserId();
			TcUserPO userPO = dao.queryUserById(userId);
			String authCode = "";
			if (userPO.getBalanceLevelCode() != null)
				authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码
			String dealerCode = request.getParamValue("dealerCode");
			String yieldly = request.getParamValue("yieldly");
			String balanceNo = request.getParamValue("balanceNo");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			act.setOutData("yieldly", yieldly);
			act.setOutData("balanceNo", balanceNo);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			// act.setOutData("balancePO", balancePO);
			act.setOutData("authCode", authCode);
			// act.setOutData("id", id);
			act.setOutData("isConfirm", isConfirm);

			// 审核人员查询
			List<TcUserPO> listUser = dao.queryAuditingUser();

			act.setOutData("listUser", listUser);
			act.setOutData("currentUser", logonUser.getUserId());

			act.setForword(this.acconeAuditClaim);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 结算室审核(批量审核)
	 */
	public void batchAuditingClaim() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		RequestWrapper request = act.getRequest();
		String claimIds[] = request.getParamValues("claimId");// 索赔单ID
		// String id = request.getParamValue("id");//结算单ID

		try {
			ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
			BalanceAuditingDao balanceAuditingDao = BalanceAuditingDao
					.getInstance();
			ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();

			if (claimIds != null && claimIds.length > 0) {
				Long userId = logonUser.getUserId();
				TcUserPO userPO = dao.queryUserById(userId);
				String authCode = "";
				if (userPO.getBalanceLevelCode() != null)
					authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

				for (int k = 0; k < claimIds.length; k++) {
					String claimId = claimIds[k];

					boolean flag = DBLockUtil.lock(claimId,
							DBLockUtil.BUSINESS_TYPE_01);
					if (!flag) {
						act.setOutData("SUCCESS", "LOCK");
						return;
					}

					// 索赔申请单信息
					TtAsWrApplicationPO claimVO = auditingDao
							.queryClaimById(claimId);
					if (!authCode.equals(claimVO.getAuthCode())) {// 该索赔单已经审核过
						continue;
					}
					// 2、根据索赔申请单中当前审核步骤，确定下一审核步骤
					String nextCode = this.getNextAuthCode(claimVO);
					// 3、更新索赔申请单状态、下一步需要审核的级别和结算总金额
					TtAsWrApplicationPO claimPO = new TtAsWrApplicationPO();
					claimPO.setUpdateBy(userId);
					claimPO.setUpdateDate(new Date());
					claimPO.setAuthCode(nextCode);
					claimPO.setAuditType(Constant.IS_MANUAL_AUDITING + "");// 人工审核标识

					String status = Constant.CLAIM_APPLY_ORD_TYPE_08.toString();
					// 审核通过
					if (nextCode == null || "".equals(nextCode)) {// 不存在其他需要审核的级别,将索赔单状态修改为结算支付
						claimPO.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_07);
						balanceAuditingDao.updateClaimStatus(Long
								.parseLong(claimId), Constant.STATUS_ENABLE);
						status = Constant.CLAIM_APPLY_ORD_TYPE_07.toString();
					}

					// 批量审核索赔单时间
					claimPO.setAccountDate(new Date());

					// 1、记录审核授权状态过程
					this.recordAuthProcess(status, userId, claimId, "[批量结算审核]",
							" [结算审核]", true);
					// 2、更新索赔申请单状态、下一步需要审核的级别和结算总金额
					dao.updateClaimInfo(claimPO, Long.parseLong(claimId));

					DBLockUtil.freeLock(claimId, DBLockUtil.BUSINESS_TYPE_01);
				}
			}
			/** ****mod by liuxh 20101126 批量审核索赔单时不调用更新结算单状态和结算金额***** */
			// 更新结算单中结算金额和结算单状态
			// BalanceAuditing bAuditing = new BalanceAuditing("","");
			// bAuditing.modifyBalanceStatus(Long.parseLong(id), true,
			// logonUser);
			/** ****mod by liuxh 20101126 批量审核索赔单时不调用更新结算单状态和结算金额***** */

			act.setOutData("SUCCESS", "SUCCESS");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室批量审核");
			logger.error(logonUser, e1);
			act.setOutData("SUCCESS", "FAILURE");
			act.setException(e1);
		}
	}

	// 新索赔单审核跳过审核下一条
	public void auditingClaimSkip() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {

			RequestWrapper request = act.getRequest();
			String claimNo = request.getParamValue("claimNo");// 索赔单号
			String vin = request.getParamValue("vin");// VIN
			String wrGroup = request.getParamValue("modelCode");// 物料组
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码
			String startDate = request.getParamValue("startDate");// 制单开始日期
			String endDate = request.getParamValue("endDate");// 制单结算日期
			// String balanceId = request.getParamValue("id");//结算单ID
			String claimType = request.getParamValue("CLAIM_TYPE");// 索赔单类型
			String cantNotAudit = CommonUtils.checkNull(request
					.getParamValue("cantNotAudit"));
			String claimIdCur = request.getParamValue("claimId");// 索赔申请单ID
			if (cantNotAudit.equals("")) {
				cantNotAudit = claimIdCur;
			} else {
				cantNotAudit = cantNotAudit + "," + claimIdCur;
			}
			TtAsWrApplicationExtPO tawep = new TtAsWrApplicationExtPO();
			List<ClaimListBean> partls = new LinkedList<ClaimListBean>();
			List<ClaimListBean> itemls = new LinkedList<ClaimListBean>();
			List<TtAsWrNetitemExtPO> otherls = new LinkedList<TtAsWrNetitemExtPO>();
			List<FsFileuploadPO> attachLs = new LinkedList<FsFileuploadPO>();
			List<TtAsWrAppauthitemPO> appAuthls = new LinkedList<TtAsWrAppauthitemPO>();
			TtAsWrWrauthorizationPO authReason = new TtAsWrWrauthorizationPO();
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			Map<String, Object> customerMap = new HashMap<String, Object>();

			List<TcCodePO> statusList = CommonUtils.findTcCodeByType(Integer
					.parseInt(Constant.IF_TYPE));// 是否同意状态

			claimNo = CommonUtils.checkNull(claimNo);
			vin = CommonUtils.checkNull(vin);
			wrGroup = CommonUtils.checkNull(wrGroup);
			startDate = CommonUtils.checkNull(startDate);
			endDate = CommonUtils.checkNull(endDate);
			dealerCode = CommonUtils.checkNull(dealerCode);

			act.setOutData("claimNo", claimNo);
			act.setOutData("vin", vin);
			act.setOutData("modelCode", wrGroup);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			// act.setOutData("balanceId", balanceId);
			act.setOutData("CLAIM_TYPE", claimType);
			act.setOutData("dealerCode", dealerCode);

			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			Long userId = logonUser.getUserId();
			TcUserPO userPO = auditingDao.queryUserById(userId);
			String authCode = "";
			if (userPO.getBalanceLevelCode() != null)
				authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

			// 查询满足条件的一条索赔单(只查询未做结算审核的索赔单)

			auditBean conditionBean = new auditBean();
			conditionBean.setClaimNo(claimNo);
			conditionBean.setVin(vin);
			conditionBean.setModelCode(wrGroup);
			conditionBean
					.setStartDate(Utility.testString(startDate) ? (startDate + " 00:00:00")
							: startDate);
			conditionBean
					.setEndDate(Utility.testString(endDate) ? (endDate + " 23:59:59")
							: endDate);
			// conditionBean.setId(balanceId);
			conditionBean.setAuthCode(authCode);
			conditionBean.setDealerCode(dealerCode);
			conditionBean.setClaimStatus(Constant.CLAIM_APPLY_ORD_TYPE_08
					.toString());
			conditionBean.setClaimType(claimType);
			conditionBean.setCantNotAudit(cantNotAudit);
			conditionBean.setAuthCodeOrder(userId.toString());
			Long orgId = logonUser.getOrgId();// 组织ID
			Long poseId = logonUser.getPoseId();// 职位ID
			// 查询职位中业务范围
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			List<Map<String, Object>> claimResult = auditingDao
					.queryClaimAccAuditSkipById(conditionBean, areaIds, 1, 1);
			List<Map<String, Object>> claimList = claimResult;
			String hasNext = "true";
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			if (claimList != null && claimList.size() > 0) {
				String claimId = claimList.get(0).get("CLAIM_ID").toString();

				tawep = auditingDao.queryClaimOrderDetailById(claimId);

				List<Map<String, Object>> customerList = dao
						.getVinUserName2(claimId);
				customerMap = new HashMap<String, Object>();
				if (customerList != null && customerList.size() > 0)
					customerMap = customerList.get(0);
				// 如果是服务活动，取出活动代码与名称
				if (Constant.CLA_TYPE_06.toString().equals(
						tawep.getClaimType().toString())) {
					List actList = auditingDao.getActivity(claimId);
					if (actList.size() > 0) {
						act.setOutData("activity", actList.get(0));
					}
				}
				// 取得产地名称
				String yieldly = tawep.getYieldly();
				String yieldlyName = "";
				if (yieldly != null) {
					TcCodePO codePO = CommonUtils
							.findTcCodeDetailByCodeId(CommonUtils
									.parseInteger(yieldly));
					if (codePO != null && codePO.getCodeDesc() != null)
						yieldlyName = codePO.getCodeDesc();
				}
				tawep.setYieldlyName(yieldlyName);

				partls = dao.queryPartById(claimId); // 取配件信息
				itemls = dao.queryItemById(claimId); // 取工时
				otherls = dao.queryOtherByid(claimId);// 取其他项目
				attachLs = dao.queryAttById(claimId);// 取得附件
				appAuthls = dao.queryAppAuthInfo(claimId);// 审核授权信息
				authReason = dao.queryAuthReason(claimId);// 需要授权原因
				statusList = CommonUtils.findTcCodeByType(Integer
						.parseInt(Constant.IF_TYPE));// 是否同意状态
				// zhumingwei 2011-03-10
				String count = auditingDao.countRepairTimes(claimId);
				act.setOutData("count", count);
				List<Map<String, Object>> list = auditingDao
						.getAuthInfo(claimId);
				String APPROVAL_PERSON = ((Map) list.get(0)).get(
						"APPROVAL_PERSON").toString();
				act.setOutData("APPROVAL_PERSON", APPROVAL_PERSON);
				// zhumingwei 2011-03-10
			} else {
				hasNext = "false";
			}
			List<Map<String, Object>> listOutRepair = dao.viewOutRepair(tawep
					.getRoNo());// 根据索赔单的工单号查询工单的外出维修信息
			if (listOutRepair.size() > 0) {
				act.setOutData("listOutRepair", listOutRepair.get(0));// 外出
			}

			act.setOutData("application", tawep);
			act.setOutData("attachLs", attachLs);
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("statusList", statusList);// 是否同意状态
			// act.setOutData("BALANCE_ID", balanceId);//结算单ID
			act.setOutData("HASNEXT", hasNext);
			act.setOutData("isContinue", "true");
			act.setOutData("cantNotAudit", cantNotAudit);

			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11

			// kevinyin 2011-05-07
			// 站长及三包员联系电话
			TmClaimContactPhonePO ccp = new TmClaimContactPhonePO();
			ccp.setDealerId(tawep.getDealerId());
			List<TmClaimContactPhonePO> listContact = dao.select(ccp);
			if (listContact.size() > 0) {
				act.setOutData("contact", listContact.get(0));
			}

			// kevinyin 2011-05-07

			act.setForword(this.claimAuditOneByOne);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室逐条审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 跳转到索赔审核页(结算审核)
	 * 
	 * @throws
	 */
	public void balanceAuditingPageWC() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			RequestWrapper request = act.getRequest();
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			String isRedo = CommonUtils.checkNull(request
					.getParamValue("IS_REDO"));
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			String id = request.getParamValue("ID");
			String balanceId = request.getParamValue("BALANCE_ID");
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			TtAsWrApplicationExtPO tawep = auditingDao
					.queryClaimOrderDetailById(id);

			// zhumingwei 2011-03-04
			List<Map<String, Object>> list = auditingDao.getAuthInfo(id);
			String APPROVAL_PERSON = ((Map) list.get(0)).get("APPROVAL_PERSON")
					.toString();
			act.setOutData("APPROVAL_PERSON", APPROVAL_PERSON);

			String count = auditingDao.countRepairTimes(id);
			// zhumingwei 2011-03-04

			List<Map<String, Object>> customerList = dao.getVinUserName2(id);
			Map<String, Object> customerMap = new HashMap<String, Object>();
			if (customerList != null && customerList.size() > 0)
				customerMap = customerList.get(0);
			// 如果是服务活动，取出活动代码与名称
			if (Constant.CLA_TYPE_06.toString().equals(
					tawep.getClaimType().toString())) {
				List actList = auditingDao.getActivity(id);
				if (actList.size() > 0) {
					act.setOutData("activity", actList.get(0));
				}
			}
			// 取得产地名称
			String yieldly = tawep.getYieldly();
			String yieldlyName = "";
			if (yieldly != null) {
				TmBusinessAreaPO ap = CommonUtils.getName(yieldly);
				if (ap != null && ap.getAreaName() != null)
					yieldlyName = ap.getAreaName();
			}
			tawep.setYieldlyName(yieldlyName);

			List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
			List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时

			List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			List<TtAsWrAppauthitemPO> appAuthls = dao.queryAppAuthInfo(id);// 审核授权信息
			TtAsWrWrauthorizationPO authReason = dao.queryAuthReason(id);// 需要授权原因
			List<TcCodePO> statusList = CommonUtils.findTcCodeByType(Integer
					.parseInt(Constant.IF_TYPE));// 是否同意状态
			List<Map<String, Object>> listOutRepair = dao.viewOutRepair(tawep
					.getRoNo());// 根据索赔单的工单号查询工单的外出维修信息
			act.setOutData("application", tawep);
			act.setOutData("attachLs", attachLs);
			act.setOutData("ID", id);// 索赔申请单ID
			act.setOutData("BALANCE_ID", balanceId);// 结算单ID
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("statusList", statusList);// 是否同意状态
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("claimId", id);// 索赔单ID

			// zhumingwei 2011-03-04
			act.setOutData("count", count);// 维修次数
			// zhumingwei 2011-03-04

			/** ***MOD BY LIUXH 20101203 有外出维修才信息才取数据***** */
			if (listOutRepair.size() > 0) {
				act.setOutData("listOutRepair", listOutRepair.get(0));// 外出
			}
			/** ***MOD BY LIUXH 20101203 有外出维修才信息才取数据***** */

			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			act.setOutData("isRedo", isRedo);
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */

			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11
			// kevinyin2011-05-04
			List listBc = dao.checkClaimIsInvoice(id);
			String flagMsg = "0";
			if (null != listBc) {
				if (listBc.size() > 0) {
					flagMsg = "1";
				}
			}
			act.setOutData("flagMsg", flagMsg);
			// kevinyin2011-05-04
			// kevinyin 2011-05-07
			// 站长及三包员联系电话
			TmClaimContactPhonePO ccp = new TmClaimContactPhonePO();
			ccp.setDealerId(tawep.getDealerId());
			List<TmClaimContactPhonePO> listContact = dao.select(ccp);
			if (listContact.size() > 0) {
				act.setOutData("contact", listContact.get(0));
			}

			// kevinyin 2011-05-07
			act.setForword(this.BALANCE_AUDITING_PAGE_WC);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 结算室授权审核
	 * 
	 * <pre>
	 * 步骤：
	 * 1、更新索赔之配件信息（结算金额）
	 * 2、更新索赔之工时信息（结算金额）
	 * 3、更新索赔之其他项目信息（结算金额）
	 * 4、记录审核授权状态过程
	 * 5、更新索赔申请单授权，将审核授权级别中审核完成的去掉
	 * 6、更新索赔申请单状态、下一步需要审核的级别和结算总金额
	 * 7、当对应结算单中的索赔单都审核通过时，重新统计结算单中数据和结算单明细中数据
	 *    同时更改索赔单状态。
	 * </pre>
	 */
	public void balanceAuditingWC() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		try {
			RequestWrapper request = act.getRequest();
			String isReDo = CommonUtils.checkNull(request
					.getParamValue("isRedo"));
			Long userId = logonUser.getUserId();// 登陆用户ID
			String claimId = request.getParamValue("claimId");// 索赔申请单ID
			String balanceId = request.getParamValue("id");// 结算单ID
			String status = request.getParamValue("status");// 审核状态(审核通过、审核退回、审核拒绝)
			String partIds[] = request.getParamValues("PART_ID");// 索赔之配件信息ID
			String labourIds[] = request.getParamValues("LABOUR_ID");// 索赔之工时信息ID
			String otherIds[] = request.getParamValues("OTHER_ID");// 索赔之其他项目信息ID
			String partBlance[] = request.getParamValues("PARTAMOUNT");// 索赔之配件结算金额
			String partRemark[] = request.getParamValues("PARTREMARK");// 索赔之配件之备注信息
			String partBlanceCount[] = request.getParamValues("PARTCOUNT");// 索赔之配件结算数量
			String labourBlance[] = request.getParamValues("LOBOURAMOUNT");// 索赔之工时结算金额
			String labourBlanceCount[] = request.getParamValues("LOBOURCOUNT");// 索赔之工时结算数量
			String labourRemark[] = request.getParamValues("LOBOURREMARK");// 索赔之工时之备注
			String otherBlance[] = request.getParamValues("OTHERAMOUNT");// 索赔之其他项目结算金额
			String auditCon[] = request.getParamValues("audit_con");// 其它费用审核意见
			String remark = request.getParamValue("CON_REMARK");// 审核意见
			String isContinue = request.getParamValue("isContinue");// 是否连续审核
																	// true :连续
																	// 其他:单条审核
			String appendLabour = request.getParamValue("APPEND_LABOUR");// 追加工时数
			String labourPrice = request.getParamValue("labourPrice");// 工时单价

			int flag = 1;// 审核标识(1:正常 2:该单已经审核过)
			if (this.isBalanceAuditing(claimId, userId, isReDo)) {// 不可以再次审核，该索赔单已经审核过
				flag = 2;
			} else {
				// 1、更新索赔之配件信息（结算金额）
				double pBlance = this.updatePartInfo(partIds, partBlance,
						partBlanceCount, partRemark, userId);
				// 2、更新索赔之工时信息（结算金额）
				double lBlance = this.updateLabourInfo(labourIds, labourBlance,
						labourBlanceCount, labourRemark, userId);
				// 3、更新索赔之其他项目信息（结算金额）
				double oBlance = this.updateOtherInfo(otherIds, otherBlance,
						userId, auditCon);

				double allBlance = pBlance + lBlance + oBlance;// 结算总金额

				// 4、记录审核授权状态过程
				// 记录审核重新审核状态区分 add by kevinyin
				String authDesc = " [结算审核]";
				if ("YES".equals(isReDo)) {
					authDesc = " [结算重新审核]";
				}
				this.recordAuthProcess(status, userId, claimId, remark,
						authDesc, true);
				// 5、根据索赔申请单中当前审核步骤，确定下一审核步骤
				String nextCode = this.getNextAuthCode(claimId);
				// 6、更新索赔申请单状态、下一步需要审核的级别、结算总金额和结算工时数
				TtAsWrApplicationPO conditionPO = new TtAsWrApplicationPO();
				// conditionPO.setStatus(Integer.parseInt(status));
				conditionPO.setUpdateBy(userId);
				conditionPO.setBalanceAmount(allBlance);
				conditionPO.setRemark(remark);
				conditionPO.setId(Long.parseLong(claimId));
				/** addUser:xiongchuan addDate:2010-12-14* */

				/** endUser:xiongchuan endDate:2010-12-14* */
				if (!Utility.testString(labourPrice))
					labourPrice = "0";
				this.updateBalanceClaimOrder(nextCode,
						Integer.parseInt(status), appendLabour, Double
								.parseDouble(labourPrice), conditionPO);
				// 61、根据工时、配件、其他项目和附加工时回写索赔单中对应的结算金额和结算金额汇总
				this.modifyClaimAmountByBalance(Long.parseLong(claimId));

				/** ******mod by liuxh 20101126 逐条审批时关闭更新结算单状态和金额********* */
				// 7、更新结算单中结算金额和结算单状态
				// BalanceAuditing bAuditing = new BalanceAuditing("","");
				// bAuditing.modifyBalanceStatus(Long.parseLong(balanceId),
				// true,logonUser);
				/** ******mod by liuxh 20101126 逐条审批时关闭更新结算单状态和金额********* */
			}
			act.setOutData("isContinue", isContinue);
			act.setOutData("ISAUDITING", flag);
			act.setOutData("ACTION_RESULT", 1);

			if (!"true".equals(isContinue)) {// 单条审核
				act.setOutData("FORWORD_URL", this.MANUAL_AUDITING);
			} else {
				this.auditingOneByOne();
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "技术室授权审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/** ******add by kevinyin 20110414查询需要结算室审核的索赔单********* */
	/** ***************************微车流程*********************************** */

	/** ***************************轿车流程*********************************** */
	/** ******add by kevinyin 20110413查询需要结算室审核的索赔单********* */
	/**
	 * 查询状态为结算支付和结算中的索赔单
	 * 
	 */
	public void acconeAuditClaimOneByOneOrgJC01() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
			Long userId = logonUser.getUserId();
			TcUserPO userPO = dao.queryUserById(userId);
			String authCode = "";
			if (userPO.getBalanceLevelCode() != null)
				authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

			TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
			subjectPO.setDutyPerson(userId);
			subjectPO.setIsDel(0);
			List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
			if (subList != null && subList.size() > 0) {
				act.setOutData("userType", 0);
			} else {
				act.setOutData("userType", 1);
			}

			act.setOutData("bansorg", 95411002);
			// 审核人员查询
			List<TcUserPO> listUser = dao.queryAuditingUser();
			act.setOutData("listUser", listUser);
			act.setOutData("currentUser", logonUser.getUserId());

			act.setOutData("authCode", authCode);
			act.setOutData("id", "111111111");

			// 取得该用户拥有的产地权限
			CommonUtilDao utilDao = new CommonUtilDao();
			List<TmBusinessAreaPO> arealist = utilDao.getYieldly();
			act.setOutData("Area", arealist);
			act.setForword(acconeAuditClaimJC01);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void acconeAuditClaimOneByOneJC01() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			if (!"".equals(Command)) {
				// CommonUtils.checkNull() 校验是否为空
				String id = request.getParamValue("id");
				String claimNo = request.getParamValue("claimNo");// 索赔单ID
				String modelCode = request.getParamValue("modelCode");// 车型代码
				String vin = request.getParamValue("vin");
				String claimType = request.getParamValue("CLAIM_TYPE");// 索赔单类型
				String dealerCode = request.getParamValue("dealerCode");// 经销商代码
				String startDate = CommonUtils.checkNull(request
						.getParamValue("startDate")); // 创建开始时间
				String endDate = CommonUtils.checkNull(request
						.getParamValue("endDate")); // 创建结束时间
				String claimStatus = request.getParamValue("claimStatus");// 索赔单状态
				String userAuth = CommonUtils.checkNull(request
						.getParamValue("checkUserSel")); // 权限审核人
				String invoiceStatus = CommonUtils.checkNull(request
						.getParamValue("invoiceStatus")); // 是否开票状态
				String balanceestartDate = CommonUtils.checkNull(request
						.getParamValue("balancestartDate")); // 结算审核开始时间
				String balanceendDate = CommonUtils.checkNull(request
						.getParamValue("balanceendDate")); // 结算审核结束时间
				String dealerName = request.getParamValue("dealerName");// 经销商名称
				String yieldly = request.getParamValue("yieldly");// 产地
				String bansorg = request.getParamValue("bansorg");

				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();

				// 当开始时间和结束时间相同时
				if (null != startDate && !"".equals(startDate)
						&& null != endDate && !"".equals(endDate)) {
					startDate = startDate + " 00:00:00";
					endDate = endDate + " 23:59:59";
				}
				if (null != balanceestartDate && !"".equals(balanceestartDate)
						&& null != balanceendDate && !"".equals(balanceendDate)) {
					balanceestartDate = balanceestartDate + " 00:00:00";
					balanceendDate = balanceendDate + " 23:59:59";
				}
				Long userId = logonUser.getUserId();
				TcUserPO userPO = dao.queryUserById(userId);
				String authCode = "";
				if (userPO.getBalanceLevelCode() != null)
					authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

				auditBean bean = new auditBean();
				bean.setId(id);
				bean.setClaimNo(claimNo);
				bean.setModelCode(modelCode);
				bean.setVin(vin);
				bean.setStartDate(startDate);
				bean.setEndDate(endDate);
				bean.setOemCompanyId(String.valueOf(GetOemcompanyId
						.getOemCompanyId(logonUser)));
				if ("".equals(userAuth)) {
					bean.setAuthCodeOrder(userId.toString());
				} else {
					bean.setAuthCodeOrder(userAuth);
				}
				bean.setBalanceEndtDate(balanceendDate);
				bean.setBalanceSatartDate(balanceestartDate);
				bean.setAuthCode(authCode);
				bean.setDealerCode(dealerCode);
				bean.setClaimType(CommonUtils.checkNull(claimType));
				bean.setClaimStatus(claimStatus);

				// 分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页

				Integer pageSize = 20;

				TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
				subjectPO.setDutyPerson(userId);
				subjectPO.setIsDel(0);
				List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
				int userType = 1;
				if (subList != null && subList.size() > 0) {
					userType = 0;
				}

				Long orgId = logonUser.getOrgId();// 组织ID
				Long poseId = logonUser.getPoseId();// 职位ID
				// 查询职位中业务范围
				String areaIds = MaterialGroupManagerDao
						.getAreaIdsByPoseId(poseId);
				PageResult<Map<String, Object>> ps = dao.queryAuditingClaimJC(
						bean, areaIds, invoiceStatus, dealerName, yieldly,
						userType, logonUser.getUserId(), bansorg, curPage,
						pageSize);

				// 分页方法 end
				act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
			} else {
				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
				Long userId = logonUser.getUserId();
				TcUserPO userPO = dao.queryUserById(userId);
				String authCode = "";
				if (userPO.getBalanceLevelCode() != null)
					authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

				TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
				subjectPO.setDutyPerson(userId);
				subjectPO.setIsDel(0);
				List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
				if (subList != null && subList.size() > 0) {
					act.setOutData("userType", 0);
				} else {
					act.setOutData("userType", 1);
				}
				act.setOutData("bansorg", 95411001);

				// 审核人员查询
				List<TcUserPO> listUser = dao.queryAuditingUser();
				act.setOutData("listUser", listUser);
				act.setOutData("currentUser", logonUser.getUserId());

				act.setOutData("authCode", authCode);
				act.setOutData("id", "111111111");

				// 取得该用户拥有的产地权限
				CommonUtilDao utilDao = new CommonUtilDao();
				List<TmBusinessAreaPO> arealist = utilDao.getYieldly();
				act.setOutData("Area", arealist);
				act.setForword(acconeAuditClaimJC01);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void acconeAuditClaimOneByOneOrgJC03() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
			Long userId = logonUser.getUserId();
			TcUserPO userPO = dao.queryUserById(userId);
			String authCode = "";
			if (userPO.getBalanceLevelCode() != null)
				authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

			TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
			subjectPO.setDutyPerson(userId);
			subjectPO.setIsDel(0);
			List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
			if (subList != null && subList.size() > 0) {
				act.setOutData("userType", 0);
			} else {
				act.setOutData("userType", 1);
			}

			// 审核人员查询
			List<TcUserPO> listUser = dao.queryAuditingUser();
			act.setOutData("listUser", listUser);
			act.setOutData("currentUser", logonUser.getUserId());
			act.setOutData("bansorg", 95411002);
			act.setOutData("authCode", authCode);
			act.setOutData("id", "111111111");

			// 取得该用户拥有的产地权限
			CommonUtilDao utilDao = new CommonUtilDao();
			List<TmBusinessAreaPO> arealist = utilDao.getYieldly();
			act.setOutData("Area", arealist);
			act.setForword(acconeAuditClaimJC03);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void acconeAuditClaimOneByOneJC03() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			if (!"".equals(Command)) {
				// CommonUtils.checkNull() 校验是否为空
				String id = request.getParamValue("id");
				String claimNo = request.getParamValue("claimNo");// 索赔单ID
				String modelCode = request.getParamValue("modelCode");// 车型代码
				String vin = request.getParamValue("vin");
				String claimType = request.getParamValue("CLAIM_TYPE");// 索赔单类型
				String dealerCode = request.getParamValue("dealerCode");// 经销商代码
				String startDate = CommonUtils.checkNull(request
						.getParamValue("startDate")); // 创建开始时间
				String endDate = CommonUtils.checkNull(request
						.getParamValue("endDate")); // 创建结束时间
				String claimStatus = request.getParamValue("claimStatus");// 索赔单状态
				String userAuth = CommonUtils.checkNull(request
						.getParamValue("checkUserSel")); // 权限审核人
				String invoiceStatus = CommonUtils.checkNull(request
						.getParamValue("invoiceStatus")); // 是否开票状态
				String balanceestartDate = CommonUtils.checkNull(request
						.getParamValue("balancestartDate")); // 结算审核开始时间
				String balanceendDate = CommonUtils.checkNull(request
						.getParamValue("balanceendDate")); // 结算审核结束时间
				String dealerName = request.getParamValue("dealerName");// 经销商名称
				String yieldly = request.getParamValue("yieldly");// 产地
				String bansorg = request.getParamValue("bansorg");// 产地

				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();

				// 当开始时间和结束时间相同时
				if (null != startDate && !"".equals(startDate)
						&& null != endDate && !"".equals(endDate)) {
					startDate = startDate + " 00:00:00";
					endDate = endDate + " 23:59:59";
				}
				if (null != balanceestartDate && !"".equals(balanceestartDate)
						&& null != balanceendDate && !"".equals(balanceendDate)) {
					balanceestartDate = balanceestartDate + " 00:00:00";
					balanceendDate = balanceendDate + " 23:59:59";
				}
				Long userId = logonUser.getUserId();
				TcUserPO userPO = dao.queryUserById(userId);
				String authCode = "";
				if (userPO.getBalanceLevelCode() != null)
					authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

				auditBean bean = new auditBean();
				bean.setId(id);
				bean.setClaimNo(claimNo);
				bean.setModelCode(modelCode);
				bean.setVin(vin);
				bean.setStartDate(startDate);
				bean.setEndDate(endDate);
				bean.setOemCompanyId(String.valueOf(GetOemcompanyId
						.getOemCompanyId(logonUser)));
				if ("".equals(userAuth)) {
					bean.setAuthCodeOrder(userId.toString());
				} else {
					bean.setAuthCodeOrder(userAuth);
				}
				bean.setBalanceEndtDate(balanceendDate);
				bean.setBalanceSatartDate(balanceestartDate);
				bean.setAuthCode(authCode);
				bean.setDealerCode(dealerCode);
				bean.setClaimType(CommonUtils.checkNull(claimType));
				bean.setClaimStatus(claimStatus);

				// 分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页

				Integer pageSize = 20;

				TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
				subjectPO.setDutyPerson(userId);
				subjectPO.setIsDel(0);
				List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
				int userType = 1;
				if (subList != null && subList.size() > 0) {
					userType = 0;
				}

				Long orgId = logonUser.getOrgId();// 组织ID
				Long poseId = logonUser.getPoseId();// 职位ID
				// 查询职位中业务范围
				String areaIds = MaterialGroupManagerDao
						.getAreaIdsByPoseId(poseId);
				PageResult<Map<String, Object>> ps = dao.queryAuditingClaimJC1(
						bean, areaIds, invoiceStatus, dealerName, yieldly,
						userType, logonUser.getUserId(), bansorg, curPage,
						pageSize);

				// 分页方法 end
				act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
			} else {
				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
				Long userId = logonUser.getUserId();
				TcUserPO userPO = dao.queryUserById(userId);
				String authCode = "";
				if (userPO.getBalanceLevelCode() != null)
					authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

				TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
				subjectPO.setDutyPerson(userId);
				subjectPO.setIsDel(0);
				List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
				if (subList != null && subList.size() > 0) {
					act.setOutData("userType", 0);
				} else {
					act.setOutData("userType", 1);
				}

				// 审核人员查询
				List<TcUserPO> listUser = dao.queryAuditingUser();
				act.setOutData("listUser", listUser);
				act.setOutData("currentUser", logonUser.getUserId());
				act.setOutData("bansorg", 95411001);
				act.setOutData("authCode", authCode);
				act.setOutData("id", "111111111");

				// 取得该用户拥有的产地权限
				CommonUtilDao utilDao = new CommonUtilDao();
				List<TmBusinessAreaPO> arealist = utilDao.getYieldly();
				act.setOutData("Area", arealist);
				act.setForword(acconeAuditClaimJC03);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/** ***************************轿车流程*********************************** */
	/** ******add by kevinyin 20110413查询需要结算室审核的索赔单********* */
	/**
	 * 查询状态为结算支付和结算中的索赔单
	 * 
	 */
	public void acconeAuditClaimOneByOneOrgJC() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long userId = logonUser.getUserId();
			TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
			subjectPO.setDutyPerson(userId);
			subjectPO.setIsDel(0);
			List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
			if (subList != null && subList.size() > 0) {
				act.setOutData("userType", 0);
			} else {
				act.setOutData("userType", 1);
			}
			act.setOutData("bansorg", 95411002);
			act.setForword(acconeAuditClaimJC);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void acconeAuditClaimOneByOneJC() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			if (!"".equals(Command)) {
				String claimStatus = request.getParamValue("claimStatus");// 索赔单状态
				String dealerName = request.getParamValue("dealerName");// 经销商名称
				String bansorg = request.getParamValue("bansorg");// 东安和昌河
				String dealerCode = request.getParamValue("dealerCode"); //
				String report_date = request.getParamValue("report_date");

				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();

				Long userId = logonUser.getUserId();
				TcUserPO userPO = dao.queryUserById(userId);
				auditBean bean = new auditBean();
				bean.setClaimStatus(claimStatus);
				bean.setDealerCode(dealerCode);
				bean.setReviewBeginDate(report_date);

				// 分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页

				Integer pageSize = 20;

				TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
				subjectPO.setDutyPerson(userId);
				subjectPO.setIsDel(0);
				List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
				int userType = 1;
				if (subList != null && subList.size() > 0) {
					userType = 0;
				}

				PageResult<Map<String, Object>> ps = dao.queryAuditingClaimJC(
						bean, dealerName, userType, logonUser.getUserId(),
						bansorg, logonUser, curPage, pageSize);

				// 分页方法 end
				act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
			} else {
				Long userId = logonUser.getUserId();
				TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
				subjectPO.setDutyPerson(userId);
				subjectPO.setIsDel(0);
				List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
				if (subList != null && subList.size() > 0) {
					act.setOutData("userType", 0);
				} else {
					act.setOutData("userType", 1);
				}
				act.setOutData("bansorg", 95411001);
				act.setForword(acconeAuditClaimJC);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void acconeAuditClaimOneByOneYXJC() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			if (!"".equals(Command)) {
				String claimStatus = request.getParamValue("claimStatus");// 索赔单状态
				String dealerName = request.getParamValue("dealerName");// 经销商名称
				String bansorg = request.getParamValue("bansorg");// 经销商名称
				String subject_id = request.getParamValue("subject_id");

				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();

				Long userId = logonUser.getUserId();
				TcUserPO userPO = dao.queryUserById(userId);
				auditBean bean = new auditBean();
				bean.setClaimStatus(claimStatus);

				// 分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页

				Integer pageSize = 20;

				TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
				subjectPO.setDutyPerson(userId);
				subjectPO.setIsDel(0);
				List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
				int userType = 1;
				if (subList != null && subList.size() > 0) {
					userType = 0;
				}

				PageResult<Map<String, Object>> ps = dao
						.queryAuditingClaimYXJC(bean, dealerName, userType,
								logonUser.getUserId(), bansorg, subject_id,
								curPage, pageSize);

				// 分页方法 end
				act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
			} else {
				Long userId = logonUser.getUserId();
				TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
				subjectPO.setDutyPerson(userId);
				subjectPO.setIsDel(0);
				List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
				if (subList != null && subList.size() > 0) {
					act.setOutData("userType", 0);
				} else {
					act.setOutData("userType", 1);
				}
				// ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
				// StringBuffer sql = new StringBuffer();
				// sql.append("SELECT E.* from TT_AS_ACTIVITY_SUBJECT E where" +
				// " E.ACTIVITY_TYPE != "+Constant.SERVICEACTIVITY_TYPE_01);
				// List<TtAsActivitySubjectPO> list=
				// dao.select(TtAsActivitySubjectPO.class, sql.toString(),null);
				// act.setOutData("list", list);
				act.setOutData("bansorg", 95411001);
				act.setForword(acconeAuditClaimYXJC);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void acconeAuditClaimOneByOneYXZRJC() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			if (!"".equals(Command)) {
				String claimStatus = request.getParamValue("claimStatus");// 索赔单状态
				String dealerName = request.getParamValue("dealerName");// 经销商名称
				String bansorg = request.getParamValue("bansorg");// 经销商名称
				String subject_id = request.getParamValue("subject_id");

				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();

				Long userId = logonUser.getUserId();
				TcUserPO userPO = dao.queryUserById(userId);
				auditBean bean = new auditBean();
				bean.setClaimStatus(claimStatus);

				// 分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页

				Integer pageSize = 10000;

				TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
				subjectPO.setDutyPerson(userId);
				subjectPO.setIsDel(0);
				List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
				int userType = 1;
				if (subList != null && subList.size() > 0) {
					userType = 0;
				}

				PageResult<Map<String, Object>> ps = dao
						.queryAuditingClaimYXJC(bean, dealerName, userType,
								logonUser.getUserId(), bansorg, subject_id,
								curPage, pageSize);

				// 分页方法 end
				act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
			} else {
				Long userId = logonUser.getUserId();
				TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
				subjectPO.setDutyPerson(userId);
				subjectPO.setIsDel(0);
				List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
				if (subList != null && subList.size() > 0) {
					act.setOutData("userType", 0);
				} else {
					act.setOutData("userType", 1);
				}
				// ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
				// StringBuffer sql = new StringBuffer();
				// sql.append("SELECT E.* from TT_AS_ACTIVITY_SUBJECT E where" +
				// " E.ACTIVITY_TYPE != "+Constant.SERVICEACTIVITY_TYPE_01);
				// List<TtAsActivitySubjectPO> list=
				// dao.select(TtAsActivitySubjectPO.class, sql.toString(),null);
				// act.setOutData("list", list);
				act.setOutData("bansorg", 95411001);
				act.setForword(acconeAuditClaimYXZRJC);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/** ***************************轿车流程*********************************** */
	/** ******add by kevinyin 20110413查询需要结算室审核的索赔单********* */
	/**
	 * 查询状态为结算支付和结算中的索赔单
	 * 
	 */

	public void acconeAuditClaimOneByOneOrgJC02() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			Long userId = logonUser.getUserId();
			TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
			subjectPO.setDutyPerson(userId);
			subjectPO.setIsDel(0);
			List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
			if (subList != null && subList.size() > 0) {
				act.setOutData("userType", 0);
			} else {
				act.setOutData("userType", 1);
			}
			act.setOutData("bansorg", 95411002);
			act.setForword(acconeAuditClaimJC02);

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void acconeAuditClaimOneByOneJC02() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			String Command = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			if (!"".equals(Command)) {
				String claimStatus = request.getParamValue("report_date");// 索赔单状态
				String dealerName = request.getParamValue("dealerName");// 经销商名称
				String person = request.getParamValue("person");// 经销商名称
				String bansorg = request.getParamValue("bansorg");// 经销商名称

				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();

				Long userId = logonUser.getUserId();
				TcUserPO userPO = dao.queryUserById(userId);
				auditBean bean = new auditBean();
				bean.setClaimStatus(claimStatus);

				// 分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页

				Integer pageSize = 20;

				TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
				subjectPO.setDutyPerson(userId);
				subjectPO.setIsDel(0);
				List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
				int userType = 1;
				if (subList != null && subList.size() > 0) {
					userType = 0;
				}

				PageResult<Map<String, Object>> ps = dao.queryAuditingClaimJC1(
						bean, dealerName, userType, logonUser.getUserId(),
						bansorg, person, curPage, pageSize);

				// 分页方法 end
				act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
			} else {
				Long userId = logonUser.getUserId();
				TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
				subjectPO.setDutyPerson(userId);
				subjectPO.setIsDel(0);
				List<TtAsActivitySubjectPO> subList = dao.select(subjectPO);
				if (subList != null && subList.size() > 0) {
					act.setOutData("userType", 0);
				} else {
					act.setOutData("userType", 1);
				}
				act.setOutData("bansorg", 95411001);
				act.setForword(acconeAuditClaimJC02);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/** ******add by kevinyin 20110413查询需要结算室审核的索赔单********* */

	/** ******add by kevinyin 20110414查询需要结算室审核的索赔单********* */

	/**
	 * 新结算室授权审核
	 * 
	 * <pre>
	 * 步骤：
	 * 1、更新索赔之配件信息（结算金额）
	 * 2、更新索赔之工时信息（结算金额）
	 * 3、更新索赔之其他项目信息（结算金额）
	 * 4、记录审核授权状态过程
	 * 5、更新索赔申请单授权，将审核授权级别中审核完成的去掉
	 * 6、更新索赔申请单状态、下一步需要审核的级别和结算总金额
	 * 7、当对应结算单中的索赔单都审核通过时，重新统计结算单中数据和结算单明细中数据
	 *    同时更改索赔单状态。
	 * </pre>
	 */
	public void claimAuditingJC() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		try {
			RequestWrapper request = act.getRequest();
			String isReDo = CommonUtils.checkNull(request
					.getParamValue("isRedo"));
			Long userId = logonUser.getUserId();// 登陆用户ID
			String claimId = request.getParamValue("claimId");// 索赔申请单ID
			String balanceId = request.getParamValue("id");// 结算单ID
			String status = request.getParamValue("status");// 审核状态(审核通过、审核退回、审核拒绝)
			String partIds[] = request.getParamValues("PART_ID");// 索赔之配件信息ID
			String labourIds[] = request.getParamValues("LABOUR_ID");// 索赔之工时信息ID
			String otherIds[] = request.getParamValues("OTHER_ID");// 索赔之其他项目信息ID
			String partBlance[] = request.getParamValues("PARTAMOUNT");// 索赔之配件结算金额
			String partRemark[] = request.getParamValues("PARTREMARK");// 索赔之配件之备注信息
			String partBlanceCount[] = request.getParamValues("PARTCOUNT");// 索赔之配件结算数量
			String labourBlance[] = request.getParamValues("LOBOURAMOUNT");// 索赔之工时结算金额
			String labourBlanceCount[] = request.getParamValues("LOBOURCOUNT");// 索赔之工时结算数量
			String labourRemark[] = request.getParamValues("LOBOURREMARK");// 索赔之工时之备注
			String otherBlance[] = request.getParamValues("OTHERAMOUNT");// 索赔之其他项目结算金额
			String auditCon[] = request.getParamValues("audit_con");// 其它费用审核意见
			String remark = request.getParamValue("CON_REMARK");// 审核意见
			String isContinue = request.getParamValue("isContinue");// 是否连续审核
																	// true :连续
																	// 其他:单条审核
			String appendLabour = request.getParamValue("APPEND_LABOUR");// 追加工时数
			String labourPrice = request.getParamValue("labourPrice");// 工时单价

			int flag = 1;// 审核标识(1:正常 2:该单已经审核过)
			if (this.isBalanceAuditing(claimId, userId, isReDo)) {// 不可以再次审核，该索赔单已经审核过
				flag = 2;
			} else {
				// 1、更新索赔之配件信息（结算金额）
				double pBlance = this.updatePartInfo(partIds, partBlance,
						partBlanceCount, partRemark, userId);
				// 2、更新索赔之工时信息（结算金额）
				double lBlance = this.updateLabourInfo(labourIds, labourBlance,
						labourBlanceCount, labourRemark, userId);
				// 3、更新索赔之其他项目信息（结算金额）
				double oBlance = this.updateOtherInfo(otherIds, otherBlance,
						userId, auditCon);

				double allBlance = pBlance + lBlance + oBlance;// 结算总金额

				// 4、记录审核授权状态过程
				this.recordAuthProcess(status, userId, claimId, remark,
						" [结算审核]", true);
				// 5、根据索赔申请单中当前审核步骤，确定下一审核步骤
				String nextCode = this.getNextAuthCode(claimId);
				// 6、更新索赔申请单状态、下一步需要审核的级别、结算总金额和结算工时数
				TtAsWrApplicationPO conditionPO = new TtAsWrApplicationPO();
				// conditionPO.setStatus(Integer.parseInt(status));
				conditionPO.setUpdateBy(userId);
				conditionPO.setBalanceAmount(allBlance);
				conditionPO.setRemark(remark);
				conditionPO.setId(Long.parseLong(claimId));
				/** addUser:xiongchuan addDate:2010-12-14* */

				/** endUser:xiongchuan endDate:2010-12-14* */
				if (!Utility.testString(labourPrice))
					labourPrice = "0";
				this.updateBalanceClaimOrder(nextCode,
						Integer.parseInt(status), appendLabour, Double
								.parseDouble(labourPrice), conditionPO);
				// 61、根据工时、配件、其他项目和附加工时回写索赔单中对应的结算金额和结算金额汇总
				this.modifyClaimAmountByBalance(Long.parseLong(claimId));

				/** ******mod by liuxh 20101126 逐条审批时关闭更新结算单状态和金额********* */
				// 7、更新结算单中结算金额和结算单状态
				// BalanceAuditing bAuditing = new BalanceAuditing("","");
				// bAuditing.modifyBalanceStatus(Long.parseLong(balanceId),
				// true,logonUser);
				/** ******mod by liuxh 20101126 逐条审批时关闭更新结算单状态和金额********* */
			}
			act.setOutData("isContinue", isContinue);
			act.setOutData("ISAUDITING", flag);
			act.setOutData("ACTION_RESULT", 1);

			if (!"true".equals(isContinue)) {// 单条审核
				act.setOutData("FORWORD_URL", this.MANUAL_AUDITING);
			} else {
				this.auditingClaimOneByOneJC();
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "技术室授权审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 逐条审批结算单中的索赔单 注：按用户选择的的查询条件，在满足条件的数据中一条接一条审批
	 */
	public void auditingClaimOneByOneJC() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String claimNo = request.getParamValue("claimNo");// 索赔单号
			String vin = request.getParamValue("vin");// VIN
			String wrGroup = request.getParamValue("modelCode");// 物料组
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码
			String dealerName = request.getParamValue("dealerName");// 经销商名称
			String startDate = request.getParamValue("startDate");// 制单开始日期
			String endDate = request.getParamValue("endDate");// 制单结算日期
			String balanceId = request.getParamValue("id");// 结算单ID
			String claimType = request.getParamValue("CLAIM_TYPE");// 索赔单类型
			String claimStatus = request.getParamValue("claimStatus");// 索赔单状态
			String checkUserSel = request.getParamValue("checkUserSel");// 审核人员
			String invoiceStatus = request.getParamValue("invoiceStatus");// 是否开票

			TtAsWrApplicationExtPO tawep = new TtAsWrApplicationExtPO();
			List<ClaimListBean> partls = new LinkedList<ClaimListBean>();
			List<ClaimListBean> itemls = new LinkedList<ClaimListBean>();
			List<TtAsWrNetitemExtPO> otherls = new LinkedList<TtAsWrNetitemExtPO>();
			List<FsFileuploadPO> attachLs = new LinkedList<FsFileuploadPO>();
			List<TtAsWrAppauthitemPO> appAuthls = new LinkedList<TtAsWrAppauthitemPO>();

			// zhumingwei 2011-03-04
			String count = "";
			// zhumingwei 2011-03-04

			TtAsWrWrauthorizationPO authReason = new TtAsWrWrauthorizationPO();
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			Map<String, Object> customerMap = new HashMap<String, Object>();

			String cantNotAudit = CommonUtils.checkNull(request
					.getParamValue("cantNotAudit"));

			List<TcCodePO> statusList = CommonUtils.findTcCodeByType(Integer
					.parseInt(Constant.IF_TYPE));// 是否同意状态

			claimNo = CommonUtils.checkNull(claimNo);
			vin = CommonUtils.checkNull(vin);
			wrGroup = CommonUtils.checkNull(wrGroup);
			startDate = CommonUtils.checkNull(startDate);
			endDate = CommonUtils.checkNull(endDate);
			dealerCode = CommonUtils.checkNull(dealerCode);
			dealerName = CommonUtils.checkNull(dealerName);

			act.setOutData("claimNo", claimNo);
			act.setOutData("vin", vin);
			act.setOutData("modelCode", wrGroup);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setOutData("balanceId", balanceId);
			act.setOutData("CLAIM_TYPE", claimType);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("dealerName", dealerName);
			act.setOutData("claimStatus", claimStatus);
			act.setOutData("checkUserSel", checkUserSel);
			act.setOutData("invoiceStatus", invoiceStatus);

			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			Long userId = logonUser.getUserId();

			TcUserPO userPO = auditingDao.queryUserById(userId);
			String authCode = "";
			if (userPO.getBalanceLevelCode() != null)
				authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

			// 查询满足条件的一条索赔单(只查询未做结算审核的索赔单)
			auditBean conditionBean = new auditBean();
			conditionBean.setClaimNo(claimNo);
			conditionBean.setVin(vin);
			conditionBean.setModelCode(wrGroup);
			conditionBean
					.setStartDate(Utility.testString(startDate) ? (startDate + " 00:00:00")
							: startDate);
			conditionBean
					.setEndDate(Utility.testString(endDate) ? (endDate + " 23:59:59")
							: endDate);
			conditionBean.setId(balanceId);
			conditionBean.setAuthCode(authCode);
			conditionBean.setDealerCode(dealerCode);
			conditionBean.setDealerName(dealerName);
			conditionBean.setClaimStatus(Constant.CLAIM_APPLY_ORD_TYPE_08
					.toString());
			conditionBean.setClaimType(claimType);
			conditionBean.setCantNotAudit(cantNotAudit);
			conditionBean.setAuthCodeOrder(userId.toString());
			Long orgId = logonUser.getOrgId();// 组织ID
			Long poseId = logonUser.getPoseId();// 职位ID
			// 查询职位中业务范围
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			List<Map<String, Object>> claimResult = auditingDao
					.queryClaimAuditById(conditionBean, areaIds, 1, 1);
			List<Map<String, Object>> claimList = claimResult;
			String hasNext = "true";
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			if (claimList != null && claimList.size() > 0) {
				String claimId = claimList.get(0).get("CLAIM_ID").toString();

				tawep = auditingDao.queryClaimOrderDetailById(claimId);

				List<Map<String, Object>> customerList = dao
						.getVinUserName2(claimId);
				customerMap = new HashMap<String, Object>();
				if (customerList != null && customerList.size() > 0)
					customerMap = customerList.get(0);

				// 取得产地名称
				String yieldly = tawep.getYieldly();
				String yieldlyName = "";
				if (yieldly != null) {
					TcCodePO codePO = CommonUtils
							.findTcCodeDetailByCodeId(CommonUtils
									.parseInteger(yieldly));
					if (codePO != null && codePO.getCodeDesc() != null)
						yieldlyName = codePO.getCodeDesc();
				}
				tawep.setYieldlyName(yieldlyName);

				partls = dao.queryPartById(claimId); // 取配件信息
				itemls = dao.queryItemById(claimId); // 取工时
				otherls = dao.queryOtherByid(claimId);// 取其他项目
				attachLs = dao.queryAttById(claimId);// 取得附件
				appAuthls = dao.queryAppAuthInfo(claimId);// 审核授权信息
				authReason = dao.queryAuthReason(claimId);// 需要授权原因

				// zhumingwei 2011-03-04
				count = auditingDao.countRepairTimes(claimId);
				List<Map<String, Object>> list = auditingDao
						.getAuthInfo(claimId);
				String APPROVAL_PERSON = ((Map) list.get(0)).get(
						"APPROVAL_PERSON").toString();
				act.setOutData("APPROVAL_PERSON", APPROVAL_PERSON);
				// zhumingwei 2011-03-04

				statusList = CommonUtils.findTcCodeByType(Integer
						.parseInt(Constant.IF_TYPE));// 是否同意状态

			} else {
				hasNext = "false";
			}
			List<Map<String, Object>> listOutRepair = dao.viewOutRepair(tawep
					.getRoNo());// 根据索赔单的工单号查询工单的外出维修信息
			if (listOutRepair.size() > 0) {
				act.setOutData("listOutRepair", listOutRepair.get(0));// 外出
			}
			act.setOutData("application", tawep);
			act.setOutData("attachLs", attachLs);
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("statusList", statusList);// 是否同意状态
			act.setOutData("BALANCE_ID", balanceId);// 结算单ID

			// zhumingwei 2011-03-04
			act.setOutData("count", count);
			// zhumingwei 2011-03-04

			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11

			act.setOutData("HASNEXT", hasNext);
			act.setOutData("cantNotAudit", cantNotAudit);
			act.setOutData("isContinue", "true");

			// kevinyin 2011-05-07
			// 站长及三包员联系电话
			TmClaimContactPhonePO ccp = new TmClaimContactPhonePO();
			ccp.setDealerId(tawep.getDealerId());
			List<TmClaimContactPhonePO> listContact = dao.select(ccp);
			if (listContact.size() > 0) {
				act.setOutData("contact", listContact.get(0));
			}

			// kevinyin 2011-05-07

			act.setForword(this.claimAuditOneByOneJC);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室逐条审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 索赔单审核
	 */
	public void auditClaimInitNewJC() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			// String id = request.getParamValue("id");//结算单ID
			String isConfirm = request.getParamValue("isConfirm");// 是否进行收单操作
																	// true: 是
																	// false:不是
			if (!Utility.testString(isConfirm))
				isConfirm = "false";

			ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
			// TtAsWrClaimBalancePO balancePO =
			// dao.queryBalanceOrderById(Long.parseLong(id));

			Long userId = logonUser.getUserId();
			TcUserPO userPO = dao.queryUserById(userId);
			String authCode = "";
			if (userPO.getBalanceLevelCode() != null)
				authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码
			String dealerCode = request.getParamValue("dealerCode");
			String yieldly = request.getParamValue("yieldly");
			String balanceNo = request.getParamValue("balanceNo");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			act.setOutData("yieldly", yieldly);
			act.setOutData("balanceNo", balanceNo);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			// act.setOutData("balancePO", balancePO);
			act.setOutData("authCode", authCode);
			// act.setOutData("id", id);
			act.setOutData("isConfirm", isConfirm);

			// 审核人员查询
			List<TcUserPO> listUser = dao.queryAuditingUser();

			act.setOutData("listUser", listUser);
			act.setOutData("currentUser", logonUser.getUserId());

			act.setForword(this.acconeAuditClaimJC);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 结算室审核(批量审核)
	 */
	public void batchAuditingClaimJC() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		RequestWrapper request = act.getRequest();
		String claimIds[] = request.getParamValues("claimId");// 索赔单ID
		// String id = request.getParamValue("id");//结算单ID

		try {
			ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
			BalanceAuditingDao balanceAuditingDao = BalanceAuditingDao
					.getInstance();
			ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();

			if (claimIds != null && claimIds.length > 0) {
				Long userId = logonUser.getUserId();
				TcUserPO userPO = dao.queryUserById(userId);
				String authCode = "";
				if (userPO.getBalanceLevelCode() != null)
					authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

				for (int k = 0; k < claimIds.length; k++) {
					String claimId = claimIds[k];

					boolean flag = DBLockUtil.lock(claimId,
							DBLockUtil.BUSINESS_TYPE_01);
					if (!flag) {
						act.setOutData("SUCCESS", "LOCK");
						return;
					}

					// 索赔申请单信息
					TtAsWrApplicationPO claimVO = auditingDao
							.queryClaimById(claimId);
					if (!authCode.equals(claimVO.getAuthCode())) {// 该索赔单已经审核过
						continue;
					}
					// 2、根据索赔申请单中当前审核步骤，确定下一审核步骤
					String nextCode = this.getNextAuthCode(claimVO);
					// 3、更新索赔申请单状态、下一步需要审核的级别和结算总金额
					TtAsWrApplicationPO claimPO = new TtAsWrApplicationPO();
					claimPO.setUpdateBy(userId);
					claimPO.setUpdateDate(new Date());
					claimPO.setAuthCode(nextCode);
					claimPO.setAuditType(Constant.IS_MANUAL_AUDITING + "");// 人工审核标识

					String status = Constant.CLAIM_APPLY_ORD_TYPE_08.toString();
					// 审核通过
					if (nextCode == null || "".equals(nextCode)) {// 不存在其他需要审核的级别,将索赔单状态修改为结算支付
						claimPO.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_07);
						balanceAuditingDao.updateClaimStatus(Long
								.parseLong(claimId), Constant.STATUS_ENABLE);
						status = Constant.CLAIM_APPLY_ORD_TYPE_07.toString();
					}

					// 批量审核索赔单时间
					claimPO.setAccountDate(new Date());

					// 1、记录审核授权状态过程
					this.recordAuthProcess(status, userId, claimId, "[批量结算审核]",
							" [结算审核]", true);
					// 2、更新索赔申请单状态、下一步需要审核的级别和结算总金额
					dao.updateClaimInfo(claimPO, Long.parseLong(claimId));

					DBLockUtil.freeLock(claimId, DBLockUtil.BUSINESS_TYPE_01);
				}
			}
			/** ****mod by liuxh 20101126 批量审核索赔单时不调用更新结算单状态和结算金额***** */
			// 更新结算单中结算金额和结算单状态
			// BalanceAuditing bAuditing = new BalanceAuditing("","");
			// bAuditing.modifyBalanceStatus(Long.parseLong(id), true,
			// logonUser);
			/** ****mod by liuxh 20101126 批量审核索赔单时不调用更新结算单状态和结算金额***** */

			act.setOutData("SUCCESS", "SUCCESS");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室批量审核");
			logger.error(logonUser, e1);
			act.setOutData("SUCCESS", "FAILURE");
			act.setException(e1);
		}
	}

	// 新索赔单审核跳过审核下一条
	public void auditingClaimSkipJC() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {

			RequestWrapper request = act.getRequest();
			String claimNo = request.getParamValue("claimNo");// 索赔单号
			String vin = request.getParamValue("vin");// VIN
			String wrGroup = request.getParamValue("modelCode");// 物料组
			String dealerCode = request.getParamValue("dealerCode");// 经销商代码
			String dealerName = request.getParamValue("dealerName");// 经销商名称
			String startDate = request.getParamValue("startDate");// 制单开始日期
			String endDate = request.getParamValue("endDate");// 制单结算日期
			// String balanceId = request.getParamValue("id");//结算单ID
			String claimType = request.getParamValue("CLAIM_TYPE");// 索赔单类型
			String cantNotAudit = CommonUtils.checkNull(request
					.getParamValue("cantNotAudit"));
			String claimIdCur = request.getParamValue("claimId");// 索赔申请单ID
			if (cantNotAudit.equals("")) {
				cantNotAudit = claimIdCur;
			} else {
				cantNotAudit = cantNotAudit + "," + claimIdCur;
			}
			TtAsWrApplicationExtPO tawep = new TtAsWrApplicationExtPO();
			List<ClaimListBean> partls = new LinkedList<ClaimListBean>();
			List<ClaimListBean> itemls = new LinkedList<ClaimListBean>();
			List<TtAsWrNetitemExtPO> otherls = new LinkedList<TtAsWrNetitemExtPO>();
			List<FsFileuploadPO> attachLs = new LinkedList<FsFileuploadPO>();
			List<TtAsWrAppauthitemPO> appAuthls = new LinkedList<TtAsWrAppauthitemPO>();
			TtAsWrWrauthorizationPO authReason = new TtAsWrWrauthorizationPO();
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			Map<String, Object> customerMap = new HashMap<String, Object>();

			List<TcCodePO> statusList = CommonUtils.findTcCodeByType(Integer
					.parseInt(Constant.IF_TYPE));// 是否同意状态

			claimNo = CommonUtils.checkNull(claimNo);
			vin = CommonUtils.checkNull(vin);
			wrGroup = CommonUtils.checkNull(wrGroup);
			startDate = CommonUtils.checkNull(startDate);
			endDate = CommonUtils.checkNull(endDate);
			dealerCode = CommonUtils.checkNull(dealerCode);
			dealerName = CommonUtils.checkNull(dealerName);

			act.setOutData("claimNo", claimNo);
			act.setOutData("vin", vin);
			act.setOutData("modelCode", wrGroup);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			// act.setOutData("balanceId", balanceId);
			act.setOutData("CLAIM_TYPE", claimType);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("dealerName", dealerName);

			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			Long userId = logonUser.getUserId();
			TcUserPO userPO = auditingDao.queryUserById(userId);
			String authCode = "";
			if (userPO.getBalanceLevelCode() != null)
				authCode = userPO.getBalanceLevelCode().toString();// 结算授权级别代码

			// 查询满足条件的一条索赔单(只查询未做结算审核的索赔单)

			auditBean conditionBean = new auditBean();
			conditionBean.setClaimNo(claimNo);
			conditionBean.setVin(vin);
			conditionBean.setModelCode(wrGroup);
			conditionBean
					.setStartDate(Utility.testString(startDate) ? (startDate + " 00:00:00")
							: startDate);
			conditionBean
					.setEndDate(Utility.testString(endDate) ? (endDate + " 23:59:59")
							: endDate);
			// conditionBean.setId(balanceId);
			conditionBean.setAuthCode(authCode);
			conditionBean.setDealerCode(dealerCode);
			conditionBean.setDealerName(dealerName);
			conditionBean.setClaimStatus(Constant.CLAIM_APPLY_ORD_TYPE_08
					.toString());
			conditionBean.setClaimType(claimType);
			conditionBean.setCantNotAudit(cantNotAudit);
			conditionBean.setAuthCodeOrder(userId.toString());
			Long orgId = logonUser.getOrgId();// 组织ID
			Long poseId = logonUser.getPoseId();// 职位ID
			// 查询职位中业务范围
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			List<Map<String, Object>> claimResult = auditingDao
					.queryClaimAccAuditSkipByIdJC(conditionBean, areaIds, 1, 1);
			List<Map<String, Object>> claimList = claimResult;
			String hasNext = "true";
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			if (claimList != null && claimList.size() > 0) {
				String claimId = claimList.get(0).get("CLAIM_ID").toString();

				tawep = auditingDao.queryClaimOrderDetailById(claimId);

				List<Map<String, Object>> customerList = dao
						.getVinUserName2(claimId);
				customerMap = new HashMap<String, Object>();
				if (customerList != null && customerList.size() > 0)
					customerMap = customerList.get(0);
				// 如果是服务活动，取出活动代码与名称
				if (Constant.CLA_TYPE_06.toString().equals(
						tawep.getClaimType().toString())) {
					List actList = auditingDao.getActivity(claimId);
					if (actList.size() > 0) {
						act.setOutData("activity", actList.get(0));
					}
				}
				// 取得产地名称
				String yieldly = tawep.getYieldly();
				String yieldlyName = "";
				if (yieldly != null) {
					TcCodePO codePO = CommonUtils
							.findTcCodeDetailByCodeId(CommonUtils
									.parseInteger(yieldly));
					if (codePO != null && codePO.getCodeDesc() != null)
						yieldlyName = codePO.getCodeDesc();
				}
				tawep.setYieldlyName(yieldlyName);

				partls = dao.queryPartById(claimId); // 取配件信息
				itemls = dao.queryItemById(claimId); // 取工时
				otherls = dao.queryOtherByid(claimId);// 取其他项目
				attachLs = dao.queryAttById(claimId);// 取得附件
				appAuthls = dao.queryAppAuthInfo(claimId);// 审核授权信息
				authReason = dao.queryAuthReason(claimId);// 需要授权原因
				statusList = CommonUtils.findTcCodeByType(Integer
						.parseInt(Constant.IF_TYPE));// 是否同意状态
				// zhumingwei 2011-03-10
				String count = auditingDao.countRepairTimes(claimId);
				act.setOutData("count", count);
				List<Map<String, Object>> list = auditingDao
						.getAuthInfo(claimId);
				String APPROVAL_PERSON = ((Map) list.get(0)).get(
						"APPROVAL_PERSON").toString();
				act.setOutData("APPROVAL_PERSON", APPROVAL_PERSON);
				// zhumingwei 2011-03-10
			} else {
				hasNext = "false";
			}
			List<Map<String, Object>> listOutRepair = dao.viewOutRepair(tawep
					.getRoNo());// 根据索赔单的工单号查询工单的外出维修信息
			if (listOutRepair.size() > 0) {
				act.setOutData("listOutRepair", listOutRepair.get(0));// 外出
			}

			act.setOutData("application", tawep);
			act.setOutData("attachLs", attachLs);
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("statusList", statusList);// 是否同意状态
			// act.setOutData("BALANCE_ID", balanceId);//结算单ID
			act.setOutData("HASNEXT", hasNext);
			act.setOutData("isContinue", "true");
			act.setOutData("cantNotAudit", cantNotAudit);

			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11

			// kevinyin 2011-05-07
			// 站长及三包员联系电话
			TmClaimContactPhonePO ccp = new TmClaimContactPhonePO();
			ccp.setDealerId(tawep.getDealerId());
			List<TmClaimContactPhonePO> listContact = dao.select(ccp);
			if (listContact.size() > 0) {
				act.setOutData("contact", listContact.get(0));
			}

			// kevinyin 2011-05-07

			act.setForword(this.claimAuditOneByOneJC);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室逐条审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 跳转到索赔审核页(结算审核)
	 * 
	 * @throws
	 */
	public void balanceAuditingPageJC() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			RequestWrapper request = act.getRequest();
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			String isRedo = CommonUtils.checkNull(request
					.getParamValue("IS_REDO"));
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			String id = request.getParamValue("ID");
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			TtAsWrApplicationExtPO tawep = auditingDao
					.queryClaimOrderDetailById(id);

			// zhumingwei 2011-03-04
			List<Map<String, Object>> list = auditingDao.getAuthInfo(id);
			String APPROVAL_PERSON = "";
			if (list != null && list.size() > 0) {
				APPROVAL_PERSON = ((Map) list.get(0)).get("APPROVAL_PERSON")
						.toString();
				act.setOutData("APPROVAL_PERSON", APPROVAL_PERSON);
			}

			String count = auditingDao.countRepairTimes(id);
			// zhumingwei 2011-03-04

			List<Map<String, Object>> customerList = dao.getVinUserName2(id);
			Map<String, Object> customerMap = new HashMap<String, Object>();
			if (customerList != null && customerList.size() > 0)
				customerMap = customerList.get(0);
			// 如果是服务活动，取出活动代码与名称
			if (Constant.CLA_TYPE_06.toString().equals(
					tawep.getClaimType().toString())) {
				List actList = auditingDao.getActivity(id);
				if (actList.size() > 0) {
					act.setOutData("activity", actList.get(0));
				}
			}
			// 取得产地名称
			String yieldly = tawep.getYieldly();
			String yieldlyName = "";
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			if (yieldly != null && yieldly.length() > 0) {
				areaPO.setAreaId(Long.parseLong(yieldly));
				List<TmBusinessAreaPO> arealist = dao.select(areaPO);
				yieldlyName = arealist.get(0).getAreaName();
			}

			// if(yieldly!=null){
			// TcCodePO codePO =
			// CommonUtils.findTcCodeDetailByCodeId(CommonUtils.parseInteger(yieldly));
			// if(codePO!=null && codePO.getCodeDesc()!=null)
			// yieldlyName = codePO.getCodeDesc();
			// }

			tawep.setYieldlyName(yieldlyName);

			List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
			List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时

			List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			List<TtAsWrAppauthitemPO> appAuthls = dao.queryAppAuthInfo(id);// 审核授权信息
			TtAsWrWrauthorizationPO authReason = dao.queryAuthReason(id);// 需要授权原因
			List<TcCodePO> statusList = CommonUtils.findTcCodeByType(Integer
					.parseInt(Constant.IF_TYPE));// 是否同意状态
			List<Map<String, Object>> listOutRepair = dao.viewOutRepair(tawep
					.getRoNo());// 根据索赔单的工单号查询工单的外出维修信息
			act.setOutData("application", tawep);
			act.setOutData("attachLs", attachLs);
			act.setOutData("ID", id);// 索赔申请单ID
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("statusList", statusList);// 是否同意状态
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("claimId", id);// 索赔单ID
			TtAsWrApplicationPO applicationPO = new TtAsWrApplicationPO();
			applicationPO.setId(Long.parseLong(id));
			List<TtAsWrApplicationPO> applicationList = dao
					.select(applicationPO);

			int ClaimType = applicationList.get(0).getStatus();

			Long dealer = applicationList.get(0).getDealerId();
			String bansorg = request.getParamValue("bansorg");
			TtAsDealerTypePO dealerTypePO = new TtAsDealerTypePO();
			dealerTypePO.setDealerId(dealer);
			dealerTypePO.setYieldly(bansorg);
			List<TtAsDealerTypePO> listT = dao.select(dealerTypePO);
			if (listT.size() > 0) {
				act.setOutData("verson", listT.get(0).getVersion());
			}
			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_11) {
				act.setOutData("ClaimType", Constant.CLAIM_APPLY_ORD_TYPE_11);
				act.setOutData("ClaimType1", Constant.CLAIM_APPLY_ORD_TYPE_08);
			}

			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_14) {
				act.setOutData("ClaimType", Constant.CLAIM_APPLY_ORD_TYPE_14);
				act.setOutData("ClaimType1", Constant.CLAIM_APPLY_ORD_TYPE_09);
			}
			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_07
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_06) {
				act.setOutData("ClaimType", Constant.CLAIM_APPLY_ORD_TYPE_07);
				act.setOutData("ClaimType1", Constant.CLAIM_APPLY_ORD_TYPE_06);
			}

			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_10
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_12) {
				act.setOutData("ClaimType", Constant.CLAIM_APPLY_ORD_TYPE_10);
				act.setOutData("ClaimType1", Constant.CLAIM_APPLY_ORD_TYPE_12);
			}

			// zhumingwei 2011-03-04
			act.setOutData("count", count);// 维修次数
			// zhumingwei 2011-03-04

			/** ***MOD BY LIUXH 20101203 有外出维修才信息才取数据***** */
			if (listOutRepair.size() > 0) {
				act.setOutData("listOutRepair", listOutRepair.get(0));// 外出
			}
			/** ***MOD BY LIUXH 20101203 有外出维修才信息才取数据***** */

			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			act.setOutData("isRedo", isRedo);
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */

			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11
			// kevinyin2011-05-04
			List listBc = dao.checkClaimIsInvoice(id);
			String flagMsg = "0";
			if (null != listBc) {
				if (listBc.size() > 0) {
					flagMsg = "1";
				}
			}
			act.setOutData("flagMsg", flagMsg);
			// kevinyin2011-05-04
			// kevinyin 2011-05-07
			// 站长及三包员联系电话
			TmClaimContactPhonePO ccp = new TmClaimContactPhonePO();
			ccp.setDealerId(tawep.getDealerId());
			List<TmClaimContactPhonePO> listContact = dao.select(ccp);
			if (listContact.size() > 0) {
				act.setOutData("contact", listContact.get(0));
			}

			// kevinyin 2011-05-07
			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_10) {
				act.setForword(this.BALANCE_AUDITING_PAGE_JC_30);
			} else if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_14) {
				act.setForword(this.BALANCE_AUDITING_PAGE_JC_31);
			} else {
				act.setForword(this.BALANCE_AUDITING_PAGE_JC);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void balanceAuditingPageJC01() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			RequestWrapper request = act.getRequest();
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			String dealer_id = request.getParamValue("dealer_id");// 经销商ID
			String balance_yieldly = request.getParamValue("balance_yieldly");// 经销商ID
			act.setOutData("balance_yieldly", balance_yieldly);
			String report_date = request.getParamValue("report_date");// 经销商ID
			String claim_type = request.getParamValue("claim_type");// 经销商ID
			String userType = request.getParamValue("userType");// 经销商ID
			String claimStatus = request.getParamValue("claimStatus");
			String total = request.getParamValue("total");
			String ids = request.getParamValue("ids");
			String one = request.getParamValue("ID");
			String curid = request.getParamValue("curidsqu");// 该审批第几个索赔单
			String claimIds[] = null;
			if (userType != null && userType.length() > 0) {
				List<TtAsWrApplicationPO> listP = auditingDao.judeDeale(
						dealer_id, userType, claimStatus, balance_yieldly,
						claim_type, report_date);
				act.setOutData("total", listP.size());
				claimIds = new String[listP.size()];
				int i = 0;
				for (TtAsWrApplicationPO po : listP) {
					if (i == 0) {
						claimIds[i] = one;
						i++;
					}
					if (!one.equals("" + po.getId())) {
						claimIds[i] = "" + po.getId();
						i++;
					}

				}
				act.setOutData("curidsqu", 0);

			}

			if (total != null && total.length() > 0) {
				act.setOutData("total", total);
			}

			int curidsqu = 0;
			int typestats = 0;
			String id = "";
			StringBuffer sb = new StringBuffer();
			if (!(ids != null && ids.length() > 2)) {
				id = claimIds[curidsqu];
				curidsqu++;
				for (int i = 0; i < claimIds.length; i++) {
					if (i == claimIds.length - 1) {
						sb.append(claimIds[i]);
					} else {
						sb.append(claimIds[i] + ",");
					}
				}
				if (claimIds.length == 1) {
					typestats = 1;
				}

			} else {
				curidsqu = Integer.parseInt(curid);
				if (curidsqu == ids.split(",").length - 1) {
					typestats = 1;
				}
				String[] buffid = ids.split(",");
				id = buffid[curidsqu];
				curidsqu++;
			}
			act.setOutData("typestats", typestats);
			act.setOutData("curidsqu", curidsqu);
			if (sb != null && sb.toString().length() > 0) {
				act.setOutData("ids", sb.toString());
			}
			if (ids != null && ids.length() > 0) {
				act.setOutData("ids", ids);
			}

			String balanceId = request.getParamValue("BALANCE_ID");
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			TtAsWrApplicationExtPO tawep = auditingDao
					.queryClaimOrderDetailById(id);

			// zhumingwei 2011-03-04
			List<Map<String, Object>> list = auditingDao.getAuthInfo(id);
			String APPROVAL_PERSON = "";
			if (list != null && list.size() > 0) {
				APPROVAL_PERSON = ((Map) list.get(0)).get("APPROVAL_PERSON")
						.toString();
				act.setOutData("APPROVAL_PERSON", APPROVAL_PERSON);
			}

			String count = auditingDao.countRepairTimes(id);
			// zhumingwei 2011-03-04

			List<Map<String, Object>> customerList = dao.getVinUserName2(id);
			Map<String, Object> customerMap = new HashMap<String, Object>();
			if (customerList != null && customerList.size() > 0)
				customerMap = customerList.get(0);
			// 如果是服务活动，取出活动代码与名称
			if (Constant.CLA_TYPE_06.toString().equals(
					tawep.getClaimType().toString())) {
				List actList = auditingDao.getActivity(id);
				if (actList.size() > 0) {
					act.setOutData("activity", actList.get(0));
				}
			}
			// 取得产地名称
			String yieldly = tawep.getYieldly();
			String yieldlyName = "";
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			if (yieldly != null && yieldly.length() > 0) {
				areaPO.setAreaId(Long.parseLong(yieldly));
				List<TmBusinessAreaPO> arealist = dao.select(areaPO);
				yieldlyName = arealist.get(0).getAreaName();
			}

			tawep.setYieldlyName(yieldlyName);

			List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
			List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时

			List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			List<TtAsWrAppauthitemPO> appAuthls = dao.queryAppAuthInfo(id);// 审核授权信息
			TtAsWrWrauthorizationPO authReason = dao.queryAuthReason(id);// 需要授权原因
			List<TcCodePO> statusList = CommonUtils.findTcCodeByType(Integer
					.parseInt(Constant.IF_TYPE));// 是否同意状态
			List<Map<String, Object>> listOutRepair = dao.viewOutRepair(tawep
					.getRoNo());// 根据索赔单的工单号查询工单的外出维修信息
			act.setOutData("application", tawep);
			act.setOutData("attachLs", attachLs);
			act.setOutData("ID", id);// 索赔申请单ID
			act.setOutData("BALANCE_ID", balanceId);// 结算单ID
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("statusList", statusList);// 是否同意状态
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("claimId", id);// 索赔单ID
			TtAsWrApplicationPO applicationPO = new TtAsWrApplicationPO();
			applicationPO.setId(Long.parseLong(id));
			List<TtAsWrApplicationPO> applicationList = dao
					.select(applicationPO);
			int ClaimType = applicationList.get(0).getStatus();
			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_08
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_07
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_06) {
				act.setOutData("ClaimType", Constant.CLAIM_APPLY_ORD_TYPE_07);
				act.setOutData("ClaimType1", Constant.CLAIM_APPLY_ORD_TYPE_06);
			}

			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_09
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_10
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_12) {
				act.setOutData("ClaimType", Constant.CLAIM_APPLY_ORD_TYPE_10);
				act.setOutData("ClaimType1", Constant.CLAIM_APPLY_ORD_TYPE_12);
			}

			// zhumingwei 2011-03-04
			act.setOutData("count", count);// 维修次数
			// zhumingwei 2011-03-04

			/** ***MOD BY LIUXH 20101203 有外出维修才信息才取数据***** */
			if (listOutRepair.size() > 0) {
				act.setOutData("listOutRepair", listOutRepair.get(0));// 外出
			}
			/** ***MOD BY LIUXH 20101203 有外出维修才信息才取数据***** */

			/** ****add by liuxh 20101127 增加是否重新审批标志******** */

			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11
			// kevinyin2011-05-04
			List listBc = dao.checkClaimIsInvoice(id);
			String flagMsg = "0";
			if (null != listBc) {
				if (listBc.size() > 0) {
					flagMsg = "1";
				}
			}
			act.setOutData("flagMsg", flagMsg);
			// kevinyin2011-05-04
			// kevinyin 2011-05-07
			// 站长及三包员联系电话
			TmClaimContactPhonePO ccp = new TmClaimContactPhonePO();
			ccp.setDealerId(tawep.getDealerId());
			List<TmClaimContactPhonePO> listContact = dao.select(ccp);
			if (listContact.size() > 0) {
				act.setOutData("contact", listContact.get(0));
			}

			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_08
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_07
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_06) {
				act.setForword(this.BALANCE_AUDITING_PAGE_JC01);
			} else {
				TtAsWrApplicationPartPO partPO = new TtAsWrApplicationPartPO();
				partPO.setApplicationPart(Long.parseLong(id));
				act.setOutData("applicationPartList", dao.select(partPO));
				act.setForword(this.BALANCE_AUDITING_PAGE_JC10);
			}
			// kevinyin 2011-05-07

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void improt() {
		OutputStream os = null;
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			RequestWrapper request = act.getRequest();
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			String dealer_id = request.getParamValue("dealer_id");// 经销商ID
			String balance_yieldly = request.getParamValue("balance_yieldly");// 经销商ID
			String report_date = request.getParamValue("report_date");// 经销商ID
			String claim_type = request.getParamValue("claim_type");// 经销商ID
			String userType = request.getParamValue("userType");// 经销商ID
			String claimStatus = request.getParamValue("claimStatus");
			String admin = request.getParamValue("admin");
			String CLAIM_NO = request.getParamValue("CLAIM_NO");
			PageResult<Map<String, Object>> listP = auditingDao
					.pageApplicattion(dealer_id, userType, claimStatus,
							balance_yieldly, claim_type, report_date, admin,
							CLAIM_NO, 10000, 1);
			List<Map<String, Object>> rslist = listP.getRecords();

			ResponseWrapper response = act.getResponse();
			// 导出的文件名
			String fileName = "结算明细.xls";
			// 导出的文字编码客户关怀活动数据明细表
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			// 定义一个集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			// 标题
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("索赔单号");
			listTemp.add("索赔单类型");
			listTemp.add("维修站");
			listTemp.add("VIN");
			listTemp.add("申请工时(元)");
			listTemp.add("结算工时(元)");
			listTemp.add("申请材料(元)");
			listTemp.add("结算材料(元)");
			listTemp.add("申请外派(元)");
			listTemp.add("结算外派(元)");
			listTemp.add("申请费用(元)");
			listTemp.add("结算费用(元)");
			listTemp.add("审核状态");
			list.add(listTemp);
			Map<String, Object> map1 = new HashMap<String, Object>();

			if (rslist != null) {

				for (int i = 0; i < rslist.size(); i++) {
					map1 = rslist.get(i);
					List<Object> listValue = new LinkedList<Object>();
					listValue.add(map1.get("CLAIM_NO") != null ? map1
							.get("CLAIM_NO") : "");
					listValue.add(map1.get("CLAIM_TYPE") != null ? desc(map1
							.get("CLAIM_TYPE").toString()) : "");
					listValue.add(map1.get("DEALER_NAME") != null ? map1
							.get("DEALER_NAME") : "");
					listValue.add(map1.get("VIN") != null ? map1.get("VIN")
							: "");
					listValue.add(map1.get("LABR_COUNT") != null ? map1
							.get("LABR_COUNT") : "");
					listValue.add(map1.get("BALANCE_LABR_COUNT") != null ? map1
							.get("BALANCE_LABR_COUNT") : "");
					listValue.add(map1.get("PART_COUNT") != null ? map1
							.get("PART_COUNT") : "");
					listValue.add(map1.get("BALANCE_PART_COUNT") != null ? map1
							.get("BALANCE_PART_COUNT") : "");
					listValue.add(map1.get("NETITEM_COUNT") != null ? map1
							.get("NETITEM_COUNT") : "");
					listValue
							.add(map1.get("BALANCE_NETITEM_COUNT") != null ? map1
									.get("BALANCE_NETITEM_COUNT")
									: "");
					listValue.add(map1.get("REPAIR_TOTAL") != null ? map1
							.get("REPAIR_TOTAL") : "");
					listValue.add(map1.get("DIS_BALANCE_AMOUNT") != null ? map1
							.get("DIS_BALANCE_AMOUNT") : "");
					listValue.add(map1.get("STATUS") != null ? desc(map1.get(
							"STATUS").toString()) : "");
					list.add(listValue);
				}
			}
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFileToName(list, os, "结算明细表");
			os.flush();

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	public String desc(String type) {
		TcCodePO codePO = new TcCodePO();
		codePO.setCodeId(type);
		return ((TcCodePO) dao.select(codePO).get(0)).getCodeDesc();
	}

	public void balancetoal() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			RequestWrapper request = act.getRequest();
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			String dealer_id = request.getParamValue("dealer_id");// 经销商ID
			String balance_yieldly = request.getParamValue("balance_yieldly");// 经销商ID
			String report_date = request.getParamValue("report_date");// 经销商ID
			String claim_type = request.getParamValue("claim_type");// 经销商ID
			String userType = request.getParamValue("userType");// 经销商ID
			String claimStatus = request.getParamValue("claimStatus");
			String admin = request.getParamValue("admin");
			String COMMAND = request.getParamValue("COMMAND");
			String CLAIM_NO = request.getParamValue("CLAIM_NO");
			if (COMMAND != null && COMMAND.length() > 0) {
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1;
				PageResult<Map<String, Object>> listP = auditingDao
						.pageApplicattion(dealer_id, userType, claimStatus,
								balance_yieldly, claim_type, report_date,
								admin, CLAIM_NO, 10000, curPage);
				act.setOutData("ps", listP);
			} else {
				act.setOutData("dealer_id", dealer_id);
				act.setOutData("balance_yieldly", balance_yieldly);
				act.setOutData("report_date", report_date);
				act.setOutData("claim_type", claim_type);
				act.setOutData("userType", userType);
				act.setOutData("claimStatus", claimStatus);
				act.setOutData("admin", admin);
				if (admin != null && admin.length() > 0) {

					TtAsDealerTypePO dealerTypePO = new TtAsDealerTypePO();
					dealerTypePO.setDealerId(Long.parseLong(dealer_id));
					dealerTypePO.setYieldly(balance_yieldly);
					List<TtAsDealerTypePO> listT = dao.select(dealerTypePO);
					if (listT.size() > 0) {
						act.setOutData("verson", listT.get(0).getVersion());
					}

					act.setOutData("quanxian", 0);
					act.setForword(balancetoal);
				}

				else if (((claim_type.equals("10661002")) || (claim_type
						.equals("10661011")))) {
					act.setOutData("section", 1);
					act.setOutData("quanxian", 1);
					act.setForword(balancetoal);
				} else {
					act.setOutData("section", 1);
					act.setForword(balancetoal1);
				}

			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void balancetoalYX() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			RequestWrapper request = act.getRequest();
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			String dealer_id = request.getParamValue("dealer_id");// 经销商ID
			String subject_id = request.getParamValue("subject_id");// 经销商ID
			String claimStatus = request.getParamValue("claimStatus");
			String admin = request.getParamValue("admin");
			String COMMAND = request.getParamValue("COMMAND");
			String VIN = request.getParamValue("VIN");
			String ACTIVITYCODE = request.getParamValue("ACTIVITYCODE");
			if (COMMAND != null && COMMAND.length() > 0) {
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1;
				PageResult<Map<String, Object>> listP = auditingDao
						.pageApplicattionYX(dealer_id, claimStatus, subject_id,
								VIN, ACTIVITYCODE, 10000, curPage);
				act.setOutData("ps", listP);
			} else {
				act.setOutData("dealer_id", dealer_id);
				act.setOutData("subject_id", subject_id);
				act.setOutData("claimStatus", claimStatus);
				if (admin != null) {
					act.setOutData("admin", admin);
				}

				if (admin != null && admin.length() > 0) {

					TtAsDealerTypePO dealerTypePO = new TtAsDealerTypePO();
					dealerTypePO.setDealerId(Long.parseLong(dealer_id));
					dealerTypePO.setYieldly("95411001");
					List<TtAsDealerTypePO> listT = dao.select(dealerTypePO);
					if (listT.size() > 0) {
						act.setOutData("verson", listT.get(0).getVersion());
					}

					act.setOutData("quanxian", 0);
					act.setForword(balancetoalYX);
				} else {
					act.setOutData("quanxian", 1);
					act.setForword(balancetoalYX);

				}

			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void balanceAuditingPageYXJC01() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			RequestWrapper request = act.getRequest();
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			String dealer_id = request.getParamValue("dealer_id");// 经销商ID
			String balance_yieldly = request.getParamValue("balance_yieldly");// 经销商ID
			String subject_id = request.getParamValue("subject_id");// 经销商ID
			String claim_type = request.getParamValue("claim_type");// 经销商ID
			String userType = request.getParamValue("userType");// 经销商ID
			String claimStatus = request.getParamValue("claimStatus");
			String total = request.getParamValue("total");
			String ids = request.getParamValue("ids");
			String curid = request.getParamValue("curidsqu");// 该审批第几个索赔单
			String claimIds[] = null;
			if (userType != null && userType.length() > 0) {
				List<TtAsWrApplicationPO> listP = auditingDao.judeDealeYX(
						dealer_id, claimStatus, subject_id);
				act.setOutData("total", listP.size());
				claimIds = new String[listP.size()];
				int i = 0;
				for (TtAsWrApplicationPO po : listP) {
					claimIds[i] = "" + po.getId();
					i++;
				}
				act.setOutData("curidsqu", 0);

			}

			if (total != null && total.length() > 0) {
				act.setOutData("total", total);
			}

			int curidsqu = 0;
			int typestats = 0;
			String id = "";
			StringBuffer sb = new StringBuffer();
			if (!(ids != null && ids.length() > 2)) {
				id = claimIds[curidsqu];
				curidsqu++;
				for (int i = 0; i < claimIds.length; i++) {
					if (i == claimIds.length - 1) {
						sb.append(claimIds[i]);
					} else {
						sb.append(claimIds[i] + ",");
					}
				}
				if (claimIds.length == 1) {
					typestats = 1;
				}

			} else {
				curidsqu = Integer.parseInt(curid);
				if (curidsqu == ids.split(",").length - 1) {
					typestats = 1;
				}
				String[] buffid = ids.split(",");
				id = buffid[curidsqu];
				curidsqu++;
			}
			act.setOutData("typestats", typestats);
			act.setOutData("curidsqu", curidsqu);
			if (sb != null && sb.toString().length() > 0) {
				act.setOutData("ids", sb.toString());
			}
			if (ids != null && ids.length() > 0) {
				act.setOutData("ids", ids);
			}

			String balanceId = request.getParamValue("BALANCE_ID");
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			TtAsWrApplicationExtPO tawep = auditingDao
					.queryClaimOrderDetailById(id);

			// zhumingwei 2011-03-04
			List<Map<String, Object>> list = auditingDao.getAuthInfo(id);
			String APPROVAL_PERSON = "";
			if (list != null && list.size() > 0) {
				APPROVAL_PERSON = ((Map) list.get(0)).get("APPROVAL_PERSON")
						.toString();
				act.setOutData("APPROVAL_PERSON", APPROVAL_PERSON);
			}

			String count = auditingDao.countRepairTimes(id);
			// zhumingwei 2011-03-04

			List<Map<String, Object>> customerList = dao.getVinUserName2(id);
			Map<String, Object> customerMap = new HashMap<String, Object>();
			if (customerList != null && customerList.size() > 0)
				customerMap = customerList.get(0);
			// 如果是服务活动，取出活动代码与名称
			if (Constant.CLA_TYPE_06.toString().equals(
					tawep.getClaimType().toString())) {
				List actList = auditingDao.getActivity(id);
				if (actList.size() > 0) {
					act.setOutData("activity", actList.get(0));
				}
			}
			// 取得产地名称
			String yieldly = tawep.getYieldly();
			String yieldlyName = "";
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			if (yieldly != null && yieldly.length() > 0) {
				areaPO.setAreaId(Long.parseLong(yieldly));
				List<TmBusinessAreaPO> arealist = dao.select(areaPO);
				yieldlyName = arealist.get(0).getAreaName();
			}

			tawep.setYieldlyName(yieldlyName);

			List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
			List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时

			List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			List<TtAsWrAppauthitemPO> appAuthls = dao.queryAppAuthInfo(id);// 审核授权信息
			TtAsWrWrauthorizationPO authReason = dao.queryAuthReason(id);// 需要授权原因
			List<TcCodePO> statusList = CommonUtils.findTcCodeByType(Integer
					.parseInt(Constant.IF_TYPE));// 是否同意状态
			List<Map<String, Object>> listOutRepair = dao.viewOutRepair(tawep
					.getRoNo());// 根据索赔单的工单号查询工单的外出维修信息
			act.setOutData("application", tawep);
			act.setOutData("attachLs", attachLs);
			act.setOutData("ID", id);// 索赔申请单ID
			act.setOutData("BALANCE_ID", balanceId);// 结算单ID
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("statusList", statusList);// 是否同意状态
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("claimId", id);// 索赔单ID
			TtAsWrApplicationPO applicationPO = new TtAsWrApplicationPO();
			applicationPO.setId(Long.parseLong(id));
			List<TtAsWrApplicationPO> applicationList = dao
					.select(applicationPO);
			int ClaimType = applicationList.get(0).getStatus();
			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_08
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_07
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_06) {
				act.setOutData("ClaimType", Constant.CLAIM_APPLY_ORD_TYPE_07);
				act.setOutData("ClaimType1", Constant.CLAIM_APPLY_ORD_TYPE_06);
			}

			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_09
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_10
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_12) {
				act.setOutData("ClaimType", Constant.CLAIM_APPLY_ORD_TYPE_10);
				act.setOutData("ClaimType1", Constant.CLAIM_APPLY_ORD_TYPE_12);
			}

			// zhumingwei 2011-03-04
			act.setOutData("count", count);// 维修次数
			// zhumingwei 2011-03-04

			/** ***MOD BY LIUXH 20101203 有外出维修才信息才取数据***** */
			if (listOutRepair.size() > 0) {
				act.setOutData("listOutRepair", listOutRepair.get(0));// 外出
			}
			/** ***MOD BY LIUXH 20101203 有外出维修才信息才取数据***** */

			/** ****add by liuxh 20101127 增加是否重新审批标志******** */

			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11
			// kevinyin2011-05-04
			List listBc = dao.checkClaimIsInvoice(id);
			String flagMsg = "0";
			if (null != listBc) {
				if (listBc.size() > 0) {
					flagMsg = "1";
				}
			}
			act.setOutData("flagMsg", flagMsg);
			// kevinyin2011-05-04
			// kevinyin 2011-05-07
			// 站长及三包员联系电话
			TmClaimContactPhonePO ccp = new TmClaimContactPhonePO();
			ccp.setDealerId(tawep.getDealerId());
			List<TmClaimContactPhonePO> listContact = dao.select(ccp);
			if (listContact.size() > 0) {
				act.setOutData("contact", listContact.get(0));
			}
			act.setOutData("YX", 0);
			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_08
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_07
					|| ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_06) {
				act.setForword(this.BALANCE_AUDITING_PAGE_JC01);
			} else {
				act.setForword(this.BALANCE_AUDITING_PAGE_JC10);
			}
			// kevinyin 2011-05-07

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public int[] getStatus(String id) {
		int[] sta = new int[3];
		TtAsWrApplicationPO applicationPO = new TtAsWrApplicationPO();
		applicationPO.setId(Long.parseLong(id));
		List<TtAsWrApplicationPO> list = dao.select(applicationPO);
		applicationPO = list.get(0);
		int status = applicationPO.getStatus();
		sta[0] = status;
		if (status == 10791006) {
			sta[1] = 10791006;
			sta[2] = 10791008;
		} else if (status == 10791007 || status == 10791011) {
			sta[1] = 10791011;
			sta[2] = 10791008;
		} else if (status == 10791010 || status == 10791014) {
			sta[1] = 10791014;
			sta[2] = 10791009;
		} else if (status == 10791012) {
			sta[1] = 10791012;
			sta[2] = 10791009;
		}
		return sta;
	}

	public void commitallZR() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			RequestWrapper request = act.getRequest();

			String[] idAll = request.getParamValues("idAll");
			String[] audit_opinion = request.getParamValues("audit_opinion");
			boolean fag = true;
			for (String audit : audit_opinion) {
				if (audit != null && audit.length() > 0) {
					fag = false;
					break;
				}
			}
			String quanxian = request.getParamValue("quanxian");
			String verson = request.getParamValue("verson");
			for (int i = 0; i < audit_opinion.length; i++) {
				if (fag) {
					TtAsWrApplicationPO wrApplicationPO = new TtAsWrApplicationPO();
					wrApplicationPO.setId(Long.parseLong(idAll[i]));
					TtAsWrApplicationPO wrApplicationPO1 = new TtAsWrApplicationPO();
					int[] sta = getStatus(idAll[i]);
					wrApplicationPO1.setStatus(sta[1]);
					// wrApplicationPO1.setAuditOpinion("");
					wrApplicationPO1.setAccountDate(new Date());
					wrApplicationPO1.setDirectorDate(new Date());
					dao.update(wrApplicationPO, wrApplicationPO1);
				} else {
					if (audit_opinion[i] != null
							&& audit_opinion[i].length() > 0) {
						TtAsWrApplicationPO wrApplicationPO = new TtAsWrApplicationPO();
						wrApplicationPO.setId(Long.parseLong(idAll[i]));
						TtAsWrApplicationPO wrApplicationPO1 = new TtAsWrApplicationPO();
						int[] sta = getStatus(idAll[i]);
						wrApplicationPO1.setStatus(sta[2]);
						wrApplicationPO1.setAuditOpinion(audit_opinion[i]);
						wrApplicationPO1.setAccountDate(new Date());
						wrApplicationPO1.setIsInvoice(0);
						List<TtAsWrApplicationPO> list = dao
								.select(wrApplicationPO);
						dao.deletBalance(list.get(0).getReportDate(), list.get(
								0).getBalanceYieldly(), list.get(0)
								.getDealerId());
						wrApplicationPO1.setDirectorDate(new Date());
						dao.update(wrApplicationPO, wrApplicationPO1);
					}

				}
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	public void commitall() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			RequestWrapper request = act.getRequest();
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */

			String claimStatus = request.getParamValue("claimStatus");
			String[] idAll = request.getParamValues("idAll");
			String[] audit_opinion = request.getParamValues("audit_opinion");
			String quanxian = request.getParamValue("quanxian");
			String verson = request.getParamValue("verson");
			String YXC = request.getParamValue("YXC");

			Integer type = 0;
			Integer type1 = 0;

			if (quanxian.equals("0")) {
				if (claimStatus.equals("" + Constant.CLAIM_APPLY_ORD_TYPE_07)) {
					type = Constant.CLAIM_APPLY_ORD_TYPE_11;
					type1 = Constant.CLAIM_APPLY_ORD_TYPE_08;
				} else if (claimStatus.equals(""
						+ Constant.CLAIM_APPLY_ORD_TYPE_10)) {
					type = Constant.CLAIM_APPLY_ORD_TYPE_14;
					type1 = Constant.CLAIM_APPLY_ORD_TYPE_09;
				} else if (claimStatus.equals(""
						+ Constant.CLAIM_APPLY_ORD_TYPE_11)) {
					type = Constant.CLAIM_APPLY_ORD_TYPE_11;
					type1 = Constant.CLAIM_APPLY_ORD_TYPE_08;

				} else if (claimStatus.equals(""
						+ Constant.CLAIM_APPLY_ORD_TYPE_14)) {
					type = Constant.CLAIM_APPLY_ORD_TYPE_14;
					type1 = Constant.CLAIM_APPLY_ORD_TYPE_09;
				}
			} else {
				if (claimStatus.equals("" + Constant.CLAIM_APPLY_ORD_TYPE_07)) {
					type = Constant.CLAIM_APPLY_ORD_TYPE_07;
					type1 = Constant.CLAIM_APPLY_ORD_TYPE_06;
				} else if (claimStatus.equals(""
						+ Constant.CLAIM_APPLY_ORD_TYPE_06)) {
					type = Constant.CLAIM_APPLY_ORD_TYPE_07;
					type1 = Constant.CLAIM_APPLY_ORD_TYPE_06;
				} else if (claimStatus.equals(""
						+ Constant.CLAIM_APPLY_ORD_TYPE_08)) {
					type = Constant.CLAIM_APPLY_ORD_TYPE_07;
					type1 = Constant.CLAIM_APPLY_ORD_TYPE_06;

				} else if (claimStatus.equals(""
						+ Constant.CLAIM_APPLY_ORD_TYPE_09)) {
					type = Constant.CLAIM_APPLY_ORD_TYPE_10;
					type1 = Constant.CLAIM_APPLY_ORD_TYPE_12;
				} else if (claimStatus.equals(""
						+ Constant.CLAIM_APPLY_ORD_TYPE_10)) {
					type = Constant.CLAIM_APPLY_ORD_TYPE_10;
					type1 = Constant.CLAIM_APPLY_ORD_TYPE_12;
				} else if (claimStatus.equals(""
						+ Constant.CLAIM_APPLY_ORD_TYPE_12)) {
					type = Constant.CLAIM_APPLY_ORD_TYPE_10;
					type1 = Constant.CLAIM_APPLY_ORD_TYPE_12;
				}
			}

			boolean flge = false;

			TtAsDealerTypePO typePO = new TtAsDealerTypePO();
			for (int i = 0; i < audit_opinion.length; i++) {
				if (audit_opinion[i] == null || audit_opinion[i].equals("")) {

					TtAsWrApplicationPO wrApplicationPO = new TtAsWrApplicationPO();
					wrApplicationPO.setId(Long.parseLong(idAll[i]));
					TtAsWrApplicationPO wrApplicationPO1 = new TtAsWrApplicationPO();
					wrApplicationPO1.setStatus(type);
					wrApplicationPO1.setAuditOpinion("");
					wrApplicationPO1.setAccountDate(new Date());
					if (quanxian.equals("0")) {
						wrApplicationPO1.setDirectorDate(new Date());
					} else {
						wrApplicationPO1.setSectionDate(new Date());
					}
					dao.update(wrApplicationPO, wrApplicationPO1);
				} else {
					boolean flg = true;

					if (quanxian.equals("0")) {
						TtAsWrApplicationPO app = new TtAsWrApplicationPO();
						app.setId(Long.parseLong(idAll[i]));
						List<TtAsWrApplicationPO> appList = dao.select(app);
						app = appList.get(0);
						Long dealer = app.getDealerId();
						int by = app.getBalanceYieldly();
						TtAsDealerTypePO asDealerTypePO = new TtAsDealerTypePO();
						asDealerTypePO.setDealerId(dealer);
						asDealerTypePO.setYieldly("" + by);
						List<TtAsDealerTypePO> list = dao
								.select(asDealerTypePO);
						if (list.size() > 0) {
							if (!(verson.equals("" + list.get(0).getVersion()))) {
								flg = false;
								flge = false;
							} else {
								flge = true;
								typePO = list.get(0);
							}
						}
					}

					if (flg) {
						TtAsWrApplicationPO wrApplicationPO = new TtAsWrApplicationPO();
						wrApplicationPO.setId(Long.parseLong(idAll[i]));
						TtAsWrApplicationPO wrApplicationPO1 = new TtAsWrApplicationPO();
						wrApplicationPO1.setStatus(type1);
						wrApplicationPO1.setAuditOpinion(audit_opinion[i]);
						wrApplicationPO1.setAccountDate(new Date());
						if (quanxian.equals("0")) {
							wrApplicationPO1.setIsInvoice(0);
						}
						if (quanxian.equals("0")) {
							wrApplicationPO1.setDirectorDate(new Date());
						} else {
							wrApplicationPO1.setSectionDate(new Date());
						}
						dao.update(wrApplicationPO, wrApplicationPO1);
					} else {
						act.setOutData("msg", "01");
					}

				}
			}

			if (flge) {
				TtAsDealerTypePO asDealerTypePO1 = new TtAsDealerTypePO();
				asDealerTypePO1.setId(typePO.getId());
				TtAsDealerTypePO asDealerTypePO2 = new TtAsDealerTypePO();
				if (verson.equals("0")) {
					verson = "1";
				} else {
					verson = "0";
				}
				if (!(YXC != null && YXC.equals("1"))) {
					if (typePO.getRubet() == 0
							&& typePO.getCollectTicket() == 1) {
						asDealerTypePO2.setBalanceDate(typePO
								.getOldBalanceDate());
						asDealerTypePO2.setOldBalanceDate(typePO
								.getOldOldBalanceDate());
						asDealerTypePO2.setRubet(1);
						asDealerTypePO2.setCollectTicket(0);
						asDealerTypePO2.setVersion(Integer.parseInt(verson));
						dao.update(asDealerTypePO1, asDealerTypePO2);
					}
				}
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	public void commitallYX() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			RequestWrapper request = act.getRequest();
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			String[] dealer_ids = request.getParamValues("recesel");// 经销商ID
			String claimStatus = request.getParamValue("claimStatus");
			Integer type = 0;
			if (claimStatus.equals("" + Constant.CLAIM_APPLY_ORD_TYPE_07)) {
				type = Constant.CLAIM_APPLY_ORD_TYPE_11;
			} else if (claimStatus
					.equals("" + Constant.CLAIM_APPLY_ORD_TYPE_10)) {
				type = Constant.CLAIM_APPLY_ORD_TYPE_14;
			} else if (claimStatus
					.equals("" + Constant.CLAIM_APPLY_ORD_TYPE_08)) {
				type = Constant.CLAIM_APPLY_ORD_TYPE_07;
			} else {
				type = Constant.CLAIM_APPLY_ORD_TYPE_10;
			}

			for (String dealer_id : dealer_ids) {
				String[] vales = dealer_id.split(",");

				List<TtAsWrApplicationPO> listP = auditingDao.judeDealeYX(
						vales[0], claimStatus, vales[1]);
				for (TtAsWrApplicationPO applicationPO : listP) {
					TtAsWrApplicationPO wrApplicationPO = new TtAsWrApplicationPO();
					wrApplicationPO.setId(applicationPO.getId());
					TtAsWrApplicationPO wrApplicationPO1 = new TtAsWrApplicationPO();
					wrApplicationPO1.setStatus(type);
					wrApplicationPO1.setAccountDate(new Date());
					dao.update(wrApplicationPO, wrApplicationPO1);
				}

			}

			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	public void balanceAuditingPageJC02() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();

			RequestWrapper request = act.getRequest();
			/** ****add by liuxh 20101127 增加是否重新审批标志******** */
			String dealer_id = request.getParamValue("dealer_id");// 经销商ID
			String balance_yieldly = request.getParamValue("balance_yieldly");// 经销商ID
			String report_date = request.getParamValue("report_date");// 经销商ID
			String claim_type = request.getParamValue("claim_type");// 经销商ID
			String userType = request.getParamValue("userType");// 经销商ID
			String claimStatus = request.getParamValue("claimStatus");
			String total = request.getParamValue("total");
			String ids = request.getParamValue("ids");
			String curid = request.getParamValue("curidsqu");// 该审批第几个索赔单
			String claimIds[] = null;
			if (userType != null && userType.length() > 0) {
				List<TtAsWrApplicationPO> listP = auditingDao.judeDeale(
						dealer_id, userType, claimStatus, balance_yieldly,
						claim_type, report_date);
				act.setOutData("total", listP.size());
				claimIds = new String[listP.size()];
				int i = 0;
				for (TtAsWrApplicationPO po : listP) {
					claimIds[i] = "" + po.getId();
					i++;
				}
				act.setOutData("curidsqu", 0);

			}

			if (total != null && total.length() > 0) {
				act.setOutData("total", total);
			}

			int curidsqu = 0;
			int typestats = 0;
			String id = "";
			StringBuffer sb = new StringBuffer();
			if (!(ids != null && ids.length() > 2)) {
				id = claimIds[curidsqu];
				curidsqu++;
				for (int i = 0; i < claimIds.length; i++) {
					if (i == claimIds.length - 1) {
						sb.append(claimIds[i]);
					} else {
						sb.append(claimIds[i] + ",");
					}
				}
				if (claimIds.length == 1) {
					typestats = 1;
				}

			} else {
				curidsqu = Integer.parseInt(curid);
				if (curidsqu == ids.split(",").length - 1) {
					typestats = 1;
				}
				String[] buffid = ids.split(",");
				id = buffid[curidsqu];
				curidsqu++;
			}
			act.setOutData("typestats", typestats);
			act.setOutData("curidsqu", curidsqu);
			if (sb != null && sb.toString().length() > 0) {
				act.setOutData("ids", sb.toString());
			}
			if (ids != null && ids.length() > 0) {
				act.setOutData("ids", ids);
			}

			String balanceId = request.getParamValue("BALANCE_ID");
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			TtAsWrApplicationExtPO tawep = auditingDao
					.queryClaimOrderDetailById(id);

			// zhumingwei 2011-03-04
			List<Map<String, Object>> list = auditingDao.getAuthInfo(id);
			String APPROVAL_PERSON = "";
			if (list != null && list.size() > 0) {
				APPROVAL_PERSON = ((Map) list.get(0)).get("APPROVAL_PERSON")
						.toString();
				act.setOutData("APPROVAL_PERSON", APPROVAL_PERSON);
			}

			String count = auditingDao.countRepairTimes(id);
			// zhumingwei 2011-03-04

			List<Map<String, Object>> customerList = dao.getVinUserName2(id);
			Map<String, Object> customerMap = new HashMap<String, Object>();
			if (customerList != null && customerList.size() > 0)
				customerMap = customerList.get(0);
			// 如果是服务活动，取出活动代码与名称
			if (Constant.CLA_TYPE_06.toString().equals(
					tawep.getClaimType().toString())) {
				List actList = auditingDao.getActivity(id);
				if (actList.size() > 0) {
					act.setOutData("activity", actList.get(0));
				}
			}
			// 取得产地名称
			String yieldly = tawep.getYieldly();
			String yieldlyName = "";
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			if (yieldly != null && yieldly.length() > 0) {
				areaPO.setAreaId(Long.parseLong(yieldly));
				List<TmBusinessAreaPO> arealist = dao.select(areaPO);
				yieldlyName = arealist.get(0).getAreaName();
			}

			tawep.setYieldlyName(yieldlyName);

			List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
			List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时

			List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			List<TtAsWrAppauthitemPO> appAuthls = dao.queryAppAuthInfo(id);// 审核授权信息
			TtAsWrWrauthorizationPO authReason = dao.queryAuthReason(id);// 需要授权原因
			List<TcCodePO> statusList = CommonUtils.findTcCodeByType(Integer
					.parseInt(Constant.IF_TYPE));// 是否同意状态
			List<Map<String, Object>> listOutRepair = dao.viewOutRepair(tawep
					.getRoNo());// 根据索赔单的工单号查询工单的外出维修信息
			act.setOutData("application", tawep);
			act.setOutData("attachLs", attachLs);
			act.setOutData("ID", id);// 索赔申请单ID
			act.setOutData("BALANCE_ID", balanceId);// 结算单ID
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("statusList", statusList);// 是否同意状态
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("claimId", id);// 索赔单ID
			TtAsWrApplicationPO applicationPO = new TtAsWrApplicationPO();
			applicationPO.setId(Long.parseLong(id));
			List<TtAsWrApplicationPO> applicationList = dao
					.select(applicationPO);
			int ClaimType = applicationList.get(0).getStatus();
			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_07) {
				act.setOutData("ClaimType", Constant.CLAIM_APPLY_ORD_TYPE_11);
				act.setOutData("ClaimType1", Constant.CLAIM_APPLY_ORD_TYPE_08);
			}

			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_10) {
				act.setOutData("ClaimType", Constant.CLAIM_APPLY_ORD_TYPE_14);
				act.setOutData("ClaimType1", Constant.CLAIM_APPLY_ORD_TYPE_09);
			}

			// zhumingwei 2011-03-04
			act.setOutData("count", count);// 维修次数
			// zhumingwei 2011-03-04

			/** ***MOD BY LIUXH 20101203 有外出维修才信息才取数据***** */
			if (listOutRepair.size() > 0) {
				act.setOutData("listOutRepair", listOutRepair.get(0));// 外出
			}
			/** ***MOD BY LIUXH 20101203 有外出维修才信息才取数据***** */

			/** ****add by liuxh 20101127 增加是否重新审批标志******** */

			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11
			// kevinyin2011-05-04
			List listBc = dao.checkClaimIsInvoice(id);
			String flagMsg = "0";
			if (null != listBc) {
				if (listBc.size() > 0) {
					flagMsg = "1";
				}
			}
			act.setOutData("flagMsg", flagMsg);
			// kevinyin2011-05-04
			// kevinyin 2011-05-07
			// 站长及三包员联系电话
			TmClaimContactPhonePO ccp = new TmClaimContactPhonePO();
			ccp.setDealerId(tawep.getDealerId());
			List<TmClaimContactPhonePO> listContact = dao.select(ccp);
			if (listContact.size() > 0) {
				act.setOutData("contact", listContact.get(0));
			}

			if (ClaimType == Constant.CLAIM_APPLY_ORD_TYPE_07) {
				act.setForword(this.BALANCE_AUDITING_PAGE_JC02);
			} else {
				act.setForword(this.BALANCE_AUDITING_PAGE_JC20);
			}
			// kevinyin 2011-05-07

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 结算室授权审核
	 * 
	 * <pre>
	 * 步骤：
	 * 1、更新索赔之配件信息（结算金额）
	 * 2、更新索赔之工时信息（结算金额）
	 * 3、更新索赔之其他项目信息（结算金额）
	 * 4、记录审核授权状态过程
	 * 5、更新索赔申请单授权，将审核授权级别中审核完成的去掉
	 * 6、更新索赔申请单状态、下一步需要审核的级别和结算总金额
	 * 7、当对应结算单中的索赔单都审核通过时，重新统计结算单中数据和结算单明细中数据
	 *    同时更改索赔单状态。
	 * </pre>
	 */
	public void balanceAuditingJC() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		try {
			RequestWrapper request = act.getRequest();
			String isReDo = CommonUtils.checkNull(request
					.getParamValue("isRedo"));
			Long userId = logonUser.getUserId();// 登陆用户ID
			String claimId = request.getParamValue("claimId");// 索赔申请单ID
			String balanceId = request.getParamValue("id");// 结算单ID
			String status = request.getParamValue("status");// 审核状态(审核通过、审核退回、审核拒绝)
			String partIds[] = request.getParamValues("PART_ID");// 索赔之配件信息ID
			String labourIds[] = request.getParamValues("LABOUR_ID");// 索赔之工时信息ID
			String otherIds[] = request.getParamValues("OTHER_ID");// 索赔之其他项目信息ID
			String partBlance[] = request.getParamValues("PARTAMOUNT");// 索赔之配件结算金额
			String partRemark[] = request.getParamValues("PARTREMARK");// 索赔之配件之备注信息
			String partBlanceCount[] = request.getParamValues("PARTCOUNT");// 索赔之配件结算数量
			String labourBlance[] = request.getParamValues("LOBOURAMOUNT");// 索赔之工时结算金额
			String labourBlanceCount[] = request.getParamValues("LOBOURCOUNT");// 索赔之工时结算数量
			String labourRemark[] = request.getParamValues("LOBOURREMARK");// 索赔之工时之备注
			String otherBlance[] = request.getParamValues("OTHERAMOUNT");// 索赔之其他项目结算金额
			String auditCon[] = request.getParamValues("audit_con");// 其它费用审核意见
			String[] DISCOUNT = request.getParamValues("DISCOUNT");
			String bohui = request.getParamValue("bohui");
			String verson = request.getParamValue("verson");
			String PART_CODE[] = request.getParamValues("PART_CODE");// 索赔之配件信息ID
			String appendlabour_reason = request
					.getParamValue("appendlabour_reason");

			boolean flg = true;
			if (bohui != null) {
				TtAsWrApplicationPO app = new TtAsWrApplicationPO();
				app.setId(Long.parseLong(claimId));
				List<TtAsWrApplicationPO> appList = dao.select(app);
				app = appList.get(0);
				Long dealer = app.getDealerId();
				int by = app.getBalanceYieldly();
				TtAsDealerTypePO asDealerTypePO = new TtAsDealerTypePO();
				asDealerTypePO.setDealerId(dealer);
				asDealerTypePO.setYieldly("" + by);
				List<TtAsDealerTypePO> list = dao.select(asDealerTypePO);
				if (list.size() > 0) {
					if (!(verson.equals("" + list.get(0).getVersion()))) {
						flg = false;
					} else {
						if (verson.equals("0")) {
							verson = "1";
						} else {
							verson = "0";
						}
						asDealerTypePO = list.get(0);
						TtAsDealerTypePO asDealerTypePO1 = new TtAsDealerTypePO();
						asDealerTypePO1.setId(asDealerTypePO.getId());
						TtAsDealerTypePO asDealerTypePO2 = new TtAsDealerTypePO();
						if (asDealerTypePO.getRubet() == 0
								&& asDealerTypePO.getCollectTicket() == 1) {
							asDealerTypePO2.setBalanceDate(asDealerTypePO
									.getOldBalanceDate());
							asDealerTypePO2.setOldBalanceDate(asDealerTypePO
									.getOldOldBalanceDate());
							asDealerTypePO2.setRubet(1);
							asDealerTypePO.setCollectTicket(0);
							asDealerTypePO2
									.setVersion(Integer.parseInt(verson));
							dao.update(asDealerTypePO1, asDealerTypePO2);
						}
					}
				}

			}

			if (flg) {
				if (DISCOUNT != null && DISCOUNT.length > 0) {
					Double discount = 0d;
					for (String dis : DISCOUNT) {
						discount = discount + Double.parseDouble(dis);
					}
					TtAsWrApplicationPO applicationPO = new TtAsWrApplicationPO();
					applicationPO.setId(Long.parseLong(claimId));
					TtAsWrApplicationPO applicationPO1 = new TtAsWrApplicationPO();
					applicationPO1.setDiscount(discount);
					dao.update(applicationPO, applicationPO1);

					TtAsWrApplicationPartPO applicationPartPO = new TtAsWrApplicationPartPO();
					applicationPartPO.setApplicationPart(Long
							.parseLong(claimId));
					dao.delete(applicationPartPO);

					for (int i = 0; i < DISCOUNT.length; i++) {
						TtAsWrApplicationPartPO applicationPartPO1 = new TtAsWrApplicationPartPO();
						applicationPartPO1.setPartDiscount(Double
								.parseDouble(DISCOUNT[i]));
						applicationPartPO1
								.setPartId(Long.parseLong(partIds[i]));
						applicationPartPO1.setApplicationPart(Long
								.parseLong(claimId));
						dao.insert(applicationPartPO1);

					}

				}

				String remark = request.getParamValue("CON_REMARK");// 审核意见
				String isContinue = request.getParamValue("isContinue");// 是否连续审核
																		// true
																		// :连续
																		// 其他:单条审核
				String appendLabour = request.getParamValue("APPEND_LABOUR");// 追加工时数
				String labourPrice = request.getParamValue("labourPrice");// 工时单价

				// 附近功能：
				String[] fjids = request.getParamValues("fjid");
				FileUploadManager.fileUploadByBusiness(claimId, fjids,
						logonUser);
				int flag = 1;// 审核标识(1:正常 2:该单已经审核过)
				// 1、更新索赔之配件信息（结算金额）
				if (DISCOUNT != null && DISCOUNT.length > 0) {
					this.updatePartInfo(PART_CODE, partBlance, partBlanceCount,
							partRemark, DISCOUNT, userId);
				}
				double pBlance = this.updatePartInfo(partIds, partBlance,
						partBlanceCount, partRemark, userId);
				// 2、更新索赔之工时信息（结算金额）
				double lBlance = this.updateLabourInfo(labourIds, labourBlance,
						labourBlanceCount, labourRemark, userId);
				// 3、更新索赔之其他项目信息（结算金额）
				double oBlance = this.updateOtherInfo(otherIds, otherBlance,
						userId, auditCon);

				double allBlance = pBlance + lBlance + oBlance;// 结算总金额

				// 4、记录审核授权状态过程

				// 记录审核重新审核状态区分 add by kevinyin
				String authDesc = " [结算审核]";
				if ("YES".equals(isReDo)) {
					authDesc = " [结算重新审核]";
				}
				this.recordAuthProcess(status, userId, claimId, remark,
						authDesc, true);
				// 5、根据索赔申请单中当前审核步骤，确定下一审核步骤
				String nextCode = this.getNextAuthCode(claimId);
				// 6、更新索赔申请单状态、下一步需要审核的级别、结算总金额和结算工时数
				TtAsWrApplicationPO conditionPO = new TtAsWrApplicationPO();
				// conditionPO.setStatus(Integer.parseInt(status));
				conditionPO.setUpdateBy(userId);
				conditionPO.setBalanceAmount(allBlance);
				conditionPO.setRemark(remark);
				conditionPO.setId(Long.parseLong(claimId));
				conditionPO.setAppendlabourReason(appendlabour_reason);
				/** addUser:xiongchuan addDate:2010-12-14* */

				/** endUser:xiongchuan endDate:2010-12-14* */
				if (!Utility.testString(labourPrice))
					labourPrice = "0";
				this.updateBalanceClaimOrder(nextCode,
						Integer.parseInt(status), appendLabour, Double
								.parseDouble(labourPrice), conditionPO);
				// 61、根据工时、配件、其他项目和附加工时回写索赔单中对应的结算金额和结算金额汇总
				this.modifyClaimAmountByBalance(Long.parseLong(claimId));

				/** ******mod by liuxh 20101126 逐条审批时关闭更新结算单状态和金额********* */
				// 7、更新结算单中结算金额和结算单状态
				// BalanceAuditing bAuditing = new BalanceAuditing("","");
				// bAuditing.modifyBalanceStatus(Long.parseLong(balanceId),
				// true,logonUser);
				/** ******mod by liuxh 20101126 逐条审批时关闭更新结算单状态和金额********* */

				// StringBuffer sql= new StringBuffer();
				// sql.append("UPDATE TT_AS_WR_APPLICATION t\n" );
				// sql.append(" set t.BALANCE_AMOUNT = (nvl(t.BALANCE_AMOUNT,0)
				// -\n" );
				// sql.append(" (nvl(t.NETITEM_AMOUNT,0) -
				// nvl(t.BALANCE_NETITEM_AMOUNT,0)))\n" );
				// sql.append(" where t.ID ="+ claimId);
				// dao.update(sql.toString(), null);
				act.setOutData("isContinue", isContinue);
				act.setOutData("ISAUDITING", flag);
				act.setOutData("ACTION_RESULT", 1);

				if (!"true".equals(isContinue)) {// 单条审核
					act.setOutData("FORWORD_URL", this.MANUAL_AUDITING);
				} else {
					this.auditingOneByOne();
				}
			} else {
				act.setOutData("fag", "0");
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "技术室授权审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/** ******add by kevinyin 20110414查询需要结算室审核的索赔单********* */
	/** ***************************轿车流程*********************************** */

	public void judeclaimdetail() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealer_id = request.getParamValue("dealer_id");
			String balance_yieldly = request.getParamValue("balance_yieldly");
			String claim_type = request.getParamValue("claim_type");
			String report_date = request.getParamValue("report_date");
			act.setOutData("id", dealer_id);
			act.setOutData("balance_yieldly", balance_yieldly);
			act.setOutData("report_date", report_date);
			act.setOutData("claim_type", claim_type);
			if (balance_yieldly.equals("95411002")) {
				act.setOutData("fag", "true");
			} else {
				StringBuffer sql = new StringBuffer();
				sql.append("select t.STATUS\n");
				sql.append("  from tt_as_wr_spefee t\n");
				sql.append(" where t.STATUS = 11841009\n");
				sql.append("   and t.DEALER_ID in\n");
				sql.append("       (select d.DEALER_ID\n");
				sql.append("          from tm_dealer d\n");
				sql.append("         start with d.DEALER_ID in (" + dealer_id
						+ ")\n");
				sql
						.append("        connect by PRIOR d.dealer_id = d.parent_dealer_d)\n");
				sql.append("   and   to_char(t.MAKE_DATE,'yyyy-mm')  = '"
						+ report_date + "'");
				if (dao.select(TtAsWrSpefeePO.class, sql.toString(), null)
						.size() > 0) {
					act.setOutData("fag", "false");
				} else {
					act.setOutData("fag", "true");
				}

			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: punishForward
	 * @Description: TODO(二次复核页面跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void secondCheckForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");
			String flag = request.getParamValue("flag");
			String feeType = request.getParamValue("feeType");
			List<Map<String, Object>> list = null;
			String oldDecode = "";
			if (flag != null && flag.equals("1")) {
				list = dao.viewClaimDealer(id);
				oldDecode = dao.viewOldDecude(id);
			} else {
				list = dao.viewSpefe(id);
				oldDecode = "0";
			}

			// 取得该用户拥有的产地权限

			act.setOutData("yieldly", list.get(0).get("YIELDLY"));
			act.setOutData("DEALER_ID", list.get(0).get("DEALER_ID"));
			act.setOutData("encourageMan", logonUser.getName());
			act.setOutData("dealerName", list.get(0).get("DEALER_NAME"));
			act.setOutData("dealerCode", list.get(0).get("DEALER_CODE"));
			act.setOutData("encourageDate", sdf.format(new Date()));
			act.setOutData("NO", list.get(0).get("NO"));
			act.setOutData("AMOUNT", list.get(0).get("BALANCE_AMOUNT"));
			act.setOutData("REPAIR_TOTAL", list.get(0).get("REPAIR_TOTAL"));
			act.setOutData("DECUTE", list.get(0).get("SECOND_DEDUCTIONS"));
			act.setOutData("oldDecode", oldDecode);
			act.setOutData("ID", id);
			act.setOutData("flag", flag);
			act.setOutData("feeType", feeType);
			act.setForword(ENCOURAGE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: secondCheckSave
	 * @Description: TODO(二次复核保存)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void secondCheckSave() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long createBy = logonUser.getUserId();
		String success = "false";
		try {
			RequestWrapper request = act.getRequest();
			@SuppressWarnings("unused")
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String dealerId = request.getParamValue("DEALER_ID"); // 经销商ID
			String fineSum = request.getParamValue("FINE_SUM"); // 罚款金额
			String fineReason = request.getParamValue("FINE_REASON"); // 罚款原因
			String remark = request.getParamValue("REMARK"); // 备注
			String yieldly = request.getParamValue("yieldly");// 奖励产地
			String documentsNo = request.getParamValue("documentsNo");// 扣款单据号
			String id = request.getParamValue("id");// 二次复核金额
			String flag = request.getParamValue("flag");
			TtAsWrFinePO tawfp = new TtAsWrFinePO();
			tawfp.setFineId(Utility.getLong(SequenceManager.getSequence("")));
			tawfp.setDealerId(Utility.getLong(dealerId));
			tawfp.setFineSum(Utility.getDouble(fineSum));
			tawfp.setFineReason(fineReason);
			tawfp.setPayStatus(Utility.getInt(Constant.PAY_STATUS_01));
			tawfp.setPayDate(new Date());
			tawfp.setRemark(remark);
			tawfp.setCreateBy(createBy);
			tawfp.setCreateDate(new Date());
			tawfp.setFineDate(new Date());
			tawfp.setYieldly(Long.parseLong(yieldly));
			tawfp.setDocumentsNo(documentsNo);
			tawfp.setDocumentsId(id);
			dao.insert(tawfp);

			/** **更新索赔单的二次复核金额*** */

			String[] newsIds = request.getParamValues("newsId");
			if (newsIds != null) {
				for (int i = 0; i < newsIds.length; i++) {
					TmFineNewsPO tfn = new TmFineNewsPO();
					tfn.setFineNewsId(Utility.getLong(SequenceManager
							.getSequence("")));
					tfn.setFineId(tawfp.getFineId());
					tfn.setNewsId(Long.valueOf(newsIds[i]));
					tfn.setCreateBy(logonUser.getUserId());
					tfn.setCreateDate(new Date());
					dao.insert(tfn);
				}
			}
			String sql = "";
			if (flag.equals("1")) {
				sql = "update tt_As_WR_application set second_deductions=nvl(second_deductions,0)+"
						+ Utility.getDouble(fineSum) + " where id=" + id + " ";
				auditClaimInitNew();
			} else {

				sql = "update Tt_As_Wr_Spefee s set s.second_deduction = nvl(s.second_deduction,0)+"
						+ Utility.getDouble(fineSum) + " where s.id=" + id + "";
				SpecialExpensesManage sem = new SpecialExpensesManage();
				sem.reauditQuerySpeciaExpensesWC();
			}

			dao.update(sql, null);

		} catch (Exception e) {
			success = "true";
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "二次复核失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 创建锁
	 * 
	 * @param key
	 * @param lockMap
	 * @return boolean false : 对应key正被锁定，true 为锁定
	 */
	@SuppressWarnings("unused")
	private boolean createLock(String key, Map<String, Long> lockMap) {

		boolean result = false;
		if (!Utility.testString(key))
			return result;

		if (lockMap == null) {
			lockMap = new HashMap<String, Long>();
		}

		if (lockMap.containsKey(key)) {
			Long lockTime = lockMap.get(key);
			Long nowTime = (new Date()).getTime();
			if ((nowTime - lockTime) / (60 * 1000) > lockLimit) {
				lockMap.remove(key);
				lockMap.put(key, nowTime);
				result = true;
			} else {
				result = false;
				;
			}
		} else {
			lockMap.put(key, (new Date()).getTime());
			result = true;
		}

		return result;
	}

	/**
	 * 释放锁
	 * 
	 * @param key
	 * @param lockMap
	 */
	@SuppressWarnings("unused")
	private void releaseLock(String key, Map<String, Long> lockMap) {
		if (Utility.testString(key) && lockMap != null) {
			if (lockMap.containsKey(key))
				lockMap.remove(key);
		}
	}

	public void selectMalCode() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			act.setForword(SHOW_MAL_CODE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void selectMalCodeQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String malCode = CommonUtils.checkNull(request
					.getParamValue("malCode"));
			String malName = CommonUtils.checkNull(request
					.getParamValue("malName"));
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.selectMalCodeQuery(
					malCode, malName, Constant.PAGE_SIZE, curPage);
			// 分页方法 end
			act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件大类");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void modifyMalCode() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = CommonUtils.checkNull(request.getParamValue("ID"));
			String modId = CommonUtils
					.checkNull(request.getParamValue("malId"));
			TtAsWrLabouritemPO l = new TtAsWrLabouritemPO();
			TtAsWrLabouritemPO l2 = new TtAsWrLabouritemPO();
			l.setLabourId(Long.valueOf(id));
			l2.setTroubleCode(modId);
			l2.setTroubleType(modId);
			dao.update(l, l2);
			act.setOutData("ACTION_RESULT", 1);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件大类");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	public void modifySuppCode() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = CommonUtils.checkNull(request.getParamValue("ID"));
			String code = CommonUtils.checkNull(request.getParamValue("code"));
			String adType = request.getParamValue("adType");
			TtPartMakerDefinePO d = new TtPartMakerDefinePO();
			d.setMakerCode(code.trim());
			d = (TtPartMakerDefinePO) dao.select(d).get(0);
			TtAsWrPartsitemPO p = new TtAsWrPartsitemPO();
			p.setPartId(Long.valueOf(id));
			p = (TtAsWrPartsitemPO) dao.select(p).get(0);

			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE Tt_As_Wr_Partsitem p SET p.down_product_code='"
					+ code.trim() + "',\n");
			/*
			 * p.producer_code='"+code.trim()+"',
			 * sql.append("p.producer_name='"+d.getMakerName()+"',\n");
			 */
			sql.append("p.down_product_name='" + d.getMakerName() + "'\n");
			sql.append("WHERE p.part_id=" + id);
			dao.update(sql.toString(), null);
			if (!"1".equalsIgnoreCase(adType)) {
				/*
				 * 记录审核记录
				 */
				TtAsWrAppAuditDetailPO dp = new TtAsWrAppAuditDetailPO();
				dp.setId(Utility.getLong(SequenceManager.getSequence("")));
				dp.setAuditBy(logonUser.getUserId());
				dp.setAuditDate(new Date());
				dp.setAuditRemark("修改【" + p.getDownPartCode() + "】供应商为" + code);
				// dp.setAuditResult(Integer.parseInt(request.getParamValue("claimStatus")));
				dp.setClaimId(Long.parseLong(request.getParamValue("claimId")));
				dp.setCreateBy(logonUser.getUserId());
				dp.setCreateDate(new Date());
				dp.setAuditType(2);
				dp.setPartCode(p.getDownPartCode());
				dao.insert(dp);
			}

			act.setOutData("partCode", p.getDownPartCode());
			act.setOutData("code", code);
			act.setOutData("part_id", id);
			act.setOutData("name", d.getMakerName());
			act.setOutData("ACTION_RESULT", 1);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void modifyPartStatus() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String part_id = CommonUtils.checkNull(request
					.getParamValue("part_id")); // 配件字表ID
			String type = CommonUtils.checkNull(request.getParamValue("type")); // 类型1挂起，
			String index = request.getParamValue("index");// 第几个fm
			String claimId = request.getParamValue("claimId");
			// 更新配件状态
			String[] str = request.getParamValues("PART_CODE");
			int auditStatus = "1".equalsIgnoreCase(type) ? Constant.PART_AUDIT_STATUS_02
					: Constant.PART_AUDIT_STATUS_01;
			StringBuffer sql = new StringBuffer();
			sql.append(" UPDATE TT_AS_WR_PARTSITEM P\n");
			sql.append(" SET P.AUDIT_STATUS = " + auditStatus + ",p.audit_by="
					+ logonUser.getUserId() + ",p.audit_date=sysdate WHERE\n");
			sql.append(" P.Part_Id IN\n");
			sql.append(" (SELECT P.Part_Id\n");
			sql.append(" FROM TT_AS_WR_PARTSITEM P where  p.id=" + claimId
					+ "\n");
			sql.append(" START WITH P.PART_ID = " + part_id + " \n");
			sql
					.append("  CONNECT BY NOCYCLE PRIOR P.DOWN_PART_CODE = P.MAIN_PART_CODE)");
			dao.update(sql.toString(), null);

			// 记录审核记录
			TcCodePO c = new TcCodePO();
			c.setCodeId(auditStatus + "");
			c = (TcCodePO) dao.select(c).get(0);
			TtAsWrPartsitemPO p = new TtAsWrPartsitemPO();
			p.setPartId(Long.valueOf(part_id));
			p = (TtAsWrPartsitemPO) dao.select(p).get(0);
			TtAsWrAppAuditDetailPO dp = new TtAsWrAppAuditDetailPO();
			dp.setId(Utility.getLong(SequenceManager.getSequence("")));
			dp.setAuditBy(logonUser.getUserId());
			dp.setAuditDate(new Date());
			dp.setAuditType(2);
			dp.setPartCode(p.getDownPartCode());
			dp.setAuditRemark("1".equalsIgnoreCase(type) ? "挂起【"
					+ p.getDownPartCode() + "】" : "取消挂起" + "【"
					+ p.getDownPartCode() + "】");
			// dp.setAuditResult(Integer.parseInt(request.getParamValue("claimStatus")));
			dp.setAuditResult(auditStatus);
			dp.setClaimId(Long.parseLong(request.getParamValue("claimId")));
			dp.setCreateBy(logonUser.getUserId());
			dp.setCreateDate(new Date());
			dao.insert(dp);

			act.setOutData("name", logonUser.getName());
			act.setOutData("part_id", part_id);
			act.setOutData("type", type);
			act.setOutData("index", index);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "挂起/取消挂起");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void auditPartStatus() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String part_id = CommonUtils.checkNull(request
					.getParamValue("part_id")); // 配件字表ID
			String type = CommonUtils.checkNull(request.getParamValue("type")); // 类型1挂起，
			String index = request.getParamValue("index");// 第几个fm
			String[] PKID = request.getParamValues("PKID" + part_id);// 补偿费ID
			String[] pass_price = request
					.getParamValues("pass_price" + part_id);// 补偿费审核金额

			String[] netItem = request.getParamValues("netItem" + part_id);// 其他项目ID
			String[] Amount = request.getParamValues("Amount" + part_id);// 其他项目审核金额
			String claimId = request.getParamValue("claimId");
			int auditStatus = 0;
			TtAsWrPartsitemPO p = new TtAsWrPartsitemPO();
			p.setPartId(Long.valueOf(part_id));
			p = (TtAsWrPartsitemPO) dao.select(p).get(0);
			if ("1".equalsIgnoreCase(type)) {// 如果是审核通过
				auditStatus = Constant.PART_AUDIT_STATUS_03;
				// 更新该配件下面的补偿费
				if (PKID != null) {
					for (int i = 0; i < PKID.length; i++) {
						TtAsWrCompensationAppPO ap = new TtAsWrCompensationAppPO();
						TtAsWrCompensationAppPO ap2 = new TtAsWrCompensationAppPO();
						ap.setPkid(Long.valueOf(Long.valueOf(PKID[i])));
						ap2.setPassPrice(Double.valueOf(pass_price[i]));
						dao.update(ap, ap2);
					}
				}
				// 更新该配件的其他项目金额
				if (netItem != null) {
					for (int i = 0; i < netItem.length; i++) {
						TtAsWrNetitemPO np = new TtAsWrNetitemPO();
						TtAsWrNetitemPO np2 = new TtAsWrNetitemPO();
						np.setNetitemId(Long.valueOf(netItem[i]));
						np2.setBalanceAmount(Double.valueOf(Amount[i]));
						dao.update(np, np2);
					}
				}
				// 更新配件状态
				StringBuffer sql = new StringBuffer();
				sql.append(" UPDATE TT_AS_WR_PARTSITEM P\n");
				sql.append(" SET P.AUDIT_STATUS = " + auditStatus
						+ ",p.audit_by=" + logonUser.getUserId()
						+ ",p.audit_date=sysdate WHERE\n");
				sql.append(" P.Part_Id IN\n");
				sql.append(" (SELECT P.Part_Id\n");
				sql.append(" FROM TT_AS_WR_PARTSITEM P where 1=1 and p.id="
						+ claimId + "\n");
				sql.append(" START WITH P.PART_ID = " + part_id + "\n");
				sql
						.append("  CONNECT BY NOCYCLE PRIOR P.DOWN_PART_CODE = P.MAIN_PART_CODE)");
				dao.update(sql.toString(), null);
			} else {// 如果是拒赔,将与该配件相关的结算价格设置为0
				auditStatus = Constant.PART_AUDIT_STATUS_04;
				// 工时
				String[] labourId = request
						.getParamValues("labourId" + part_id);
				if (labourId != null) {
					for (int i = 0; i < labourId.length; i++) {
						String sql = "UPDATE Tt_As_Wr_Labouritem l SET l.balance_amount=0,l.balance_quantity=0 WHERE l.labour_id= "
								+ labourId[i];
						dao.update(sql, null);
					}
				}
				// 配件
				StringBuffer sql = new StringBuffer();
				sql.append(" UPDATE TT_AS_WR_PARTSITEM P\n");
				sql
						.append(" SET p.balance_amount=0,p.balance_quantity=0,P.AUDIT_STATUS = "
								+ auditStatus
								+ ",p.audit_by="
								+ logonUser.getUserId()
								+ ",p.audit_date=sysdate \n");
				sql.append(" WHERE P.Part_Id IN\n");
				sql.append(" (SELECT P.Part_Id\n");
				sql.append(" FROM TT_AS_WR_PARTSITEM P where 1=1 and p.id="
						+ claimId + "\n");
				sql.append(" START WITH P.PART_ID = " + part_id + "\n");
				sql
						.append("  CONNECT BY NOCYCLE PRIOR P.DOWN_PART_CODE = P.MAIN_PART_CODE)");
				dao.update(sql.toString(), null);
				// 补偿费
				if (PKID != null) {
					for (int i = 0; i < PKID.length; i++) {
						TtAsWrCompensationAppPO ap = new TtAsWrCompensationAppPO();
						TtAsWrCompensationAppPO ap2 = new TtAsWrCompensationAppPO();
						ap.setPkid(Long.valueOf(Long.valueOf(PKID[i])));
						ap2.setPassPrice(0.0);
						dao.update(ap, ap2);
					}
				}

				// 辅料
				String[] fulId = request.getParamValues("FLID" + part_id);
				if (fulId != null) {
					for (int i = 0; i < fulId.length; i++) {
						String sql1 = "UPDATE tt_claim_accessory_dtl  d SET d.price=0 WHERE d.ID="
								+ fulId[i];
						dao.update(sql1, null);
					}
				}
				// 其他项目
				if (netItem != null) {
					for (int i = 0; i < netItem.length; i++) {
						TtAsWrNetitemPO np = new TtAsWrNetitemPO();
						TtAsWrNetitemPO np2 = new TtAsWrNetitemPO();
						np.setNetitemId(Long.valueOf(netItem[i]));
						np2.setBalanceAmount(0.0);
						dao.update(np, np2);
					}
				}
			}
			/**
			 * 更新索赔单金额
			 */
			StringBuffer sql01 = new StringBuffer();
			sql01.append("update TT_AS_WR_APPLICATION t set\n");
			sql01.append("\n");
			sql01
					.append("t.BALANCE_NETITEM_AMOUNT = ( SELECT sum(nvl(a.BALANCE_AMOUNT,0)) from TT_AS_WR_NETITEM a where a.ID = t.ID   ),\n");
			sql01.append("\n");
			sql01
					.append("t.accessories_price = (  SELECT sum(nvl(b.PRICE,0)) from  tt_claim_accessory_dtl b where b.CLAIM_NO = t.CLAIM_NO  ),\n");
			sql01.append("\n");
			sql01
					.append("t.compensation_money  = ( SELECT sum(nvl (c.PASS_PRICE,0  )  ) from TT_AS_WR_COMPENSATION_APP c where c.CLAIM_NO = t.CLAIM_NO  ),\n");
			sql01.append("\n");
			sql01
					.append("t.BALANCE_PART_AMOUNT = ( SELECT sum(nvl(d.BALANCE_AMOUNT,0)) from TT_AS_WR_PARTSITEM d where d.ID = t.ID   ),\n");
			sql01.append("\n");
			sql01
					.append("t.BALANCE_LABOUR_AMOUNT = ( SELECT sum(nvl(e.BALANCE_AMOUNT,0)) from TT_AS_WR_LABOURITEM e  where e.ID = t.ID    )\n");
			sql01.append("\n");
			sql01.append("where t.ID =  " + p.getId() + "\n");

			dao.update(sql01.toString(), null);

			StringBuffer sql05 = new StringBuffer();
			sql05.append("update TT_AS_WR_APPLICATION t set\n");
			sql05
					.append("t.BALANCE_AMOUNT = nvl(t.BALANCE_NETITEM_AMOUNT,0)+ nvl(t.accessories_price,0)+nvl(t.compensation_money,0)+nvl(t.BALANCE_PART_AMOUNT,0)\n");
			sql05
					.append("+nvl(t.BALANCE_LABOUR_AMOUNT,0)+nvl(t.campaign_fee,0)+nvl(t.free_m_price,0)+nvl(t.part_down,0)+nvl(t.labour_down,0)\n");
			sql05.append("where t.ID =  " + p.getId() + "\n");
			dao.update(sql05.toString(), null);

			// 记录审核记录
			TcCodePO c = new TcCodePO();
			c.setCodeId(auditStatus + "");
			c = (TcCodePO) dao.select(c).get(0);

			TtAsWrAppAuditDetailPO dp = new TtAsWrAppAuditDetailPO();
			dp.setId(Utility.getLong(SequenceManager.getSequence("")));
			dp.setAuditBy(logonUser.getUserId());
			dp.setAuditDate(new Date());
			dp.setAuditRemark("单件审核【" + p.getDownPartCode() + "】,状态为："
					+ c.getCodeDesc());
			// dp.setAuditResult(Integer.parseInt(request.getParamValue("claimStatus")));
			dp.setAuditResult(auditStatus);
			dp.setClaimId(Long.parseLong(request.getParamValue("claimId")));
			dp.setCreateBy(logonUser.getUserId());
			dp.setCreateDate(new Date());
			dp.setAuditType(2);
			dp.setPartCode(p.getDownPartCode());
			dao.insert(dp);

			act.setOutData("name", logonUser.getName());
			act.setOutData("part_id", part_id);
			act.setOutData("type", type);
			act.setOutData("index", index);
			act.setOutData("succ", "succ");
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "单件审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 修改换下件代码名称
	 * 
	 * @author KFQ
	 */
	public void modifyDownCode() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = CommonUtils.checkNull(request.getParamValue("ID"));
			String code = CommonUtils.checkNull(request.getParamValue("code"));
			String tempCode = CommonUtils.checkNull(request
					.getParamValue("tempCode"));
			String claimId = CommonUtils.checkNull(request
					.getParamValue("claimId"));

			String oldPartCode = CommonUtils.checkNull(request
					.getParamValue("oldPartCode"));
			TtPartDefinePO d = new TtPartDefinePO();// 由于页面传值中文可能会出现乱码，所以只需要传代码，在后台查询出名称
			d.setPartOldcode(code.trim());
			d = (TtPartDefinePO) dao.select(d).get(0);
			String sql = "UPDATE Tt_As_Wr_Partsitem p SET p.down_part_code='"
					+ d.getPartOldcode() + "',p.real_part_id=" + d.getPartId()
					+ ",p.down_part_name='" + d.getPartCname()
					+ "' WHERE p.part_id=" + id;
			dao.update(sql, null);
			// 由于修改的是换下件，以后的流程都是按照换下件进行查询处理，所以这里还要将该索赔单下的次因件对应的主因件也要更新为新的配件
			String sql2 = "UPDATE Tt_As_Wr_Partsitem p SET p.main_part_code='"
					+ code + "' WHERE p.main_part_code='" + tempCode
					+ "' AND p.Id=" + claimId;
			dao.update(sql2, null);
			updateBuChangFee(claimId, oldPartCode, d.getPartOldcode());
			act.setOutData("partcode", d.getPartOldcode());
			act.setOutData("partname", d.getPartCname());
			act.setOutData("ACTION_RESULT", 1);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @param claimId
	 *            索赔单ID
	 * @param oldPartCode
	 * @param newPartCode
	 */
	public void updateBuChangFee(String claimId, String oldPartCode,
			String newPartCode) {
		String sql1 = "update Tt_Claim_Accessory_Dtl set main_part_code='"
				+ newPartCode
				+ "' where claim_no=(select claim_no from tt_as_wr_application where id="
				+ claimId + ") and main_part_code='" + oldPartCode + "'";
		String sql2 = "update Tt_As_Wr_Netitem set main_part_code='"
				+ newPartCode + "' where id=" + claimId
				+ " and main_part_code='" + oldPartCode + "'";
		String sql3 = "update TT_AS_WR_COMPENSATION_APP set part_code='"
				+ newPartCode
				+ "' where claim_no=(select claim_no from tt_as_wr_application where id="
				+ claimId + ") and part_code='" + oldPartCode + "'";
		dao.update(sql1, null);
		dao.update(sql2, null);
		dao.update(sql3, null);
	}

	/**
	 * 工单废弃(车厂) 注：主机厂能废弃预授权审核退回、审核不同意或通过的工单。 zyw 2014-9-28
	 */
	public void repairOrderCancel() {
		String flag = DaoFactory.getParam(request, "query");
		if ("true".equals(flag)) {
			PageResult<Map<String, Object>> ps = dao.repairOrderCancel(request,
					Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", ps);
		}
		super.sendMsgByUrl(sendUrl(ClaimManualAuditing.class,
				"repairOrderCancel"), "服务站申请工单废弃");
	}
}
