package com.infodms.dms.actions.claim.application;
 
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.claim.balanceAuditing.BalanceAuditingProxy;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.DealerBalanceBean;
import com.infodms.dms.bean.auditBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.application.ClaimManualAuditingDao;
import com.infodms.dms.dao.claim.application.DealerBalanceDao;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.dao.claim.authorization.BalanceMainDao;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.claim.oldPart.ClaimBackListDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmRegionPO;
import com.infodms.dms.po.TrBalanceClaimPO;
import com.infodms.dms.po.TrGatherBalancePO;
import com.infodms.dms.po.TtAsDealerTypePO;
import com.infodms.dms.po.TtAsPartBorrowPO;
import com.infodms.dms.po.TtAsWrClaimBalanceDetailPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrGatherBalancePO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.po.TtPartTransPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OSB34;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DealerBalance extends BaseAction{
	
	/** 经销商结算单页面 */
	private final String DEALER_BALANCE_QUERY = "/jsp/claim/application/dealerBalanceQuery.jsp";
	/** 经销商结算查询页面 */
	private final String DEALER_BALANCE = "/jsp/claim/application/dealerBalance.jsp";
	/** 经销商结算预览页面 */
	private final String DEALER_BALANCE_VIEW = "/jsp/claim/application/dealerBalanceView.jsp";
	/** 经销商结算明细页面 */
	private final String DEALER_BALANCE_DETAIL = "/jsp/claim/application/dealerBalanceDetail.jsp";
	private final String DEALER_BALANCE_DETAIL3 = "/jsp/claim/application/dealerBalanceDetail3.jsp";
	private final String DEALER_BALANCE_DETAIL2 = "/jsp/claim/application/dealerBalanceDetail2.jsp";
	/** 车厂端结算单收单(按单张结算单) */
	@SuppressWarnings("unused")
	private final String DEALER_BALANCE_CONFIRM = "/jsp/claim/application/oemBalanceOrderConfirm.jsp";
	/** 车厂端结算单收单(按结算汇总单收单) */
	private final String DEALER_BALANCE_CONFIRM2 = "/jsp/claim/application/oemBalanceOrderConfirm2.jsp";
	/** 汇总结算单 */
	private final String CLAIM_BILL_PRINT_URL = "/jsp/claim/application/sumBalancePrint.jsp";// 主页面（查询）
	/** 经销商结算汇总单页面 */
	private final String DEALER_GATHER_BALANCE_QUERY = "/jsp/claim/application/dealerGatherBalanceQuery.jsp";
	/**Iverson add By 2010-12-21  结算单扣款明细查询 */
	private final String DEALER_CLAIM_BALANCE_QUERY = "/jsp/claim/application/dealerClaimBalanceQuery.jsp";
	/**Iverson add By 2010-12-31  三包结算统计表 */
	private final String QUERY_CLAIM_BALANCE_QUERY = "/jsp/claim/application/queryClaimBalance.jsp";
	/** 经销商结算汇总单添加页面 */
	private final String DEALER_GATHER_BALANCE_ADD = "/jsp/claim/application/addGatherBalanceOrderPage.jsp";
	/** 经销商结算汇总单添加页面 */
	private final String GATHER_BALANCE_DETAIL = "/jsp/claim/application/gatherBalanceOrderDetail.jsp";
	/** 查询此结算所有索赔单信息 Iverson add By 2010-12-24 */
	private final String CLAIM_INFO = "/jsp/claim/application/claimInfo.jsp";
	/** 查询此结算所有特殊费用信息 Iverson add By 2010-12-24 */
	private final String FEE_INFO = "/jsp/claim/application/feeInfo.jsp";
	/** 查询此旧件汇总单ID查询所有旧件信息 Iverson add By 2010-12-24 */
	private final String DEDUCT_INFO = "/jsp/claim/application/deductInfo.jsp";
	/** 经销商结算汇总单打印 */
	private final String PRINT_GATHER_BALANCE_DETAIL = "/jsp/claim/application/printBalanceOrderDetail.jsp";
	/** 结算单明细 Iverson add By 2010-12-21 */
	private final String detailByBalanceId = "/jsp/claim/application/detailByBalanceId.jsp";
	/** 根据结算单ID查询扣款明细 Iverson add By 2010-12-21 */
	private final String deductByBalanceId = "/jsp/claim/application/deductByBalanceId.jsp";
	/** 旧件回运限制月数 */
	public static final Integer RETURN_LIMIT_MONTHS = 3;
	
	/** 经销商信息查询 Iverson add By 2010-01-18 */
	private final String QUERY_DEALER_INFO = "/jsp/claim/application/queryDealerInfo.jsp";
	private final String acconeAuditClaimJC02 = "/jsp/claim/application/dealerAcconeAuditClaimJC02.jsp";
	private final String balancetoal = "/jsp/claim/application/dealerBalancetoal.jsp";
	private final String balancetoalyx = "/jsp/claim/application/dealerBalancetoalyx.jsp";
	
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
	public void dealerBalanceInit() {
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			
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
			act.setForword(this.DEALER_BALANCE_QUERY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void get_WARHOUSE_DATE()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			 String CLAIM_NO= request.getParamValue("in_warhouse_date");
			 DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			 StringBuffer sql= new StringBuffer();
			 sql.append("select  to_char(max(r.in_warhouse_date),'yyyy-mm-dd') remark\n" );
			 sql.append(" from tt_as_wr_old_returned r,tt_as_wr_old_returned_detail d ,TT_AS_WR_APPLICATION t\n" );
			 sql.append(" where r.id = d.return_id\n" );
			 sql.append(" and  t.CLAIM_NO = d.CLAIM_NO\n" );
			 sql.append(" and t.CLAIM_NO ='" + CLAIM_NO+"'");
			 TtAsWrOldReturnedPO oldReturnedPO = new TtAsWrOldReturnedPO();
			 List<TtAsWrOldReturnedPO> list= balanceDao.select(TtAsWrOldReturnedPO.class, sql.toString(), null);
			 if(list.size() > 0)
			 {
				act.setOutData("date", list.get(0).getRemark()) ;
			 }else
			 {
				 act.setOutData("date", "") ;
			 }
			 

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算单查询");
			logger.error(logonUser, e1);
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
	public void addDealerBalanceInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {			
			/*************Iverson add By 2010-12-02 显示12月份的结算日，必须选择结算日才能结算在基础表(tc_code)里面维护***********/
			TcCodePO po = new TcCodePO();
			po.setCodeId(Constant.DAY_12);
			ClaimBackListDao claimBackdao=ClaimBackListDao.getInstance();
			TcCodePO poValue = (TcCodePO)claimBackdao.select(po).get(0);
			String day = poValue.getCodeDesc();
			act.setOutData("day", Long.parseLong(day));
			
			TcCodePO po1 = new TcCodePO();
			po1.setCodeId(Constant.DAY_12_31);
			TcCodePO poValue1 = (TcCodePO)claimBackdao.select(po1).get(0);
			String day1 = poValue1.getCodeDesc();
			act.setOutData("day1", Long.parseLong(day1));
			/*************Iverson add By 2010-12-02 显示12月份的结算日，必须选择结算日才能结算在基础表(tc_code)里面维护***********/
			
			act.setForword(this.DEALER_BALANCE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商上报结算单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
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
	public void dealerBalanceStatisQuery(){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//String startTime = request.getParamValue("CON_START_DAY");//结算起始时间
			//String endTime = request.getParamValue("CON_END_DAY");//结算终止时间
			String yieldly = request.getParamValue("YIELDLY");//产地
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			String dealerId = logonUser.getDealerId();//经销商ID
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);//用户所属公司
			
			/******************************/
			//String beginBalance = request.getParamValue("beginBalance");//结算起始时间
			String endBalanceDate = request.getParamValue("endBalanceDate");//数据库结算终止时间
			String conEndDay = request.getParamValue("CON_END_DAY");//现在结算终止时间
			
			
			 //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");       
			 //Calendar lastDate = Calendar.getInstance();   
			 //lastDate.set(Calendar.DATE,1);//设为当前月的1号   
		     //lastDate.add(Calendar.MONTH,1);//加一个月，变为下月的1号   
		     //lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天    
			 //String str=sdf.format(lastDate.getTime());   
			
			
			/******************************/
			
			if(Utility.testString(endBalanceDate))
				endBalanceDate = endBalanceDate + " 00:00:00";
			if(Utility.testString(conEndDay))
				conEndDay = conEndDay + " 23:59:59";
			
			DealerBalanceBean condition = new DealerBalanceBean();
			//condition.setStartTime(startTime);
			//condition.setEndTime(endTime);
			/**********Iverson add By 2010-11-17********************/
			//condition.setStartTime(beginBalance);
			condition.setEndBalanceDate(endBalanceDate);
			condition.setConEndDay(conEndDay);
			/**********Iverson add By 2010-11-17********************/
			condition.setYieldly(yieldly);
			condition.setCurPage(curPage);
			condition.setPageSize(Constant.PAGE_SIZE);
			condition.setDealerId(Long.parseLong(dealerId));
			condition.setCompanyId(companyId);
			
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			PageResult<Map<String,Object>> ps = balanceDao.dealerBalanceStatisQuery(condition);
            
			/********************/
			TcCodePO po = new TcCodePO();
			po.setCodeId(Constant.DAY_12);
			ClaimBackListDao claimBackdao=ClaimBackListDao.getInstance();
			TcCodePO poValue = (TcCodePO)claimBackdao.select(po).get(0);
			String day = poValue.getCodeDesc();
			act.setOutData("day", day);
			/********************/
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商上报结算统计查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void getSpecFeeList(){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String yieldly = request.getParamValue("YIELDLY");//产地
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			String dealerId = logonUser.getDealerId();//经销商ID
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);//用户所属公司
			
			
			String endBalanceDate = request.getParamValue("endBalanceDate");//数据库结算终止时间
			String conEndDay = request.getParamValue("CON_END_DAY");//现在结算终止时间
			
			/******************************/
			
			if(Utility.testString(endBalanceDate))
				endBalanceDate = endBalanceDate + " 00:00:00";
			if(Utility.testString(conEndDay))
				conEndDay = conEndDay + " 23:59:59";
		
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();

			/********add by liuxh 20101211 增加特殊费用的显示********/
            act.setOutData("spFeeList", balanceDao.getSpecialFeeToBalanceOrder(Long.valueOf(dealerId), endBalanceDate, conEndDay, yieldly));
            /********add by liuxh 20101211 增加特殊费用的显示********/
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "取特殊费用");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void getFalg(){
		try{
			String endBalanceDate = request.getParamValue("endBalanceDate");
			String end_day = request.getParamValue("CON_END_DAY");
			String CON_END_DAY = request.getParamValue("CON_END_DAY");//选择的时间
			String[] start = endBalanceDate.split("-");
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sf.parse(CON_END_DAY));
			cal.add(Calendar.DAY_OF_MONTH, 1);
			CON_END_DAY = sf.format(cal.getTime());
			String[] end = CON_END_DAY.split("-");
			


			if( (! start[start.length-1].equals("01")) || (! end[end.length-1].equals("01"))){
				act.setOutData("retn", 2); 
			}
			
			CON_END_DAY = end_day;
			
			
			String dealerId = request.getParamValue("dealerId");
			String yieldly = request.getParamValue("yieldly");
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			//boolean judeSpefee = balanceDao.judeSpefee(endBalanceDate, CON_END_DAY, dealerId,yieldly);
			boolean application = balanceDao.Application(endBalanceDate, CON_END_DAY, dealerId,yieldly);
			if(application){
				act.setOutData("retn", 1); 
			}else{
				act.setOutData("retn", 0); 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 经销商结算选择条件内的索赔申请单
	 * 注意：只有审核通过的索赔申请单可以结算
	 * 2010-09-21 XZM 修改 
	 * 	描述：将统计对应配件、工时和其他项目的费用都统计结算费用
	 *        同时统计 售前数量、售后数量、服务活动数量和免费保养数量等，都不相互包含
	 *  影响：
	 *       1、DealerBalanceDao.queryBalanceStatis()  查询相关费用信息   
	 *       2、DealerBalanceDao.queryBalanceGroupSeries() 按车系汇总结算信息
	 *  注意：
	 *       同时修改结算单审核后更新结算单主从表数据（结算室审核时可以修改结算金额） 
	 * 2010-09-27 XZM 修改
	 * 描述：1、结算单对应服务活动费用显示呈现总费用（工时+配件+其他+固定费用） 
	 *       2、结算单明细中保养费用拆分为(保养工时费用、保养配件费用)
	 *          规则：长安现在：每次保养时，其中固定50元为保养工时费用，其他为保养配件费用
	 *  影响：
	 *      1、DealerBalanceDao.queryBalanceStatis()  加入特殊费用统计(追加费用)和服务活动费用合计 
	 *         a. 增加服务活动费用合计项
	 *         b. 增加追加工时费用和追加费用统计
	 *         c. 将追加工时费用统计到工时费用中 
	 *            注：免费保养：加入到免费保养总金额中
	 *                服务活动：加入到服务活动工时中
	 *                其他：加入到工时费用统计中
	 *      2、DealerBalanceDao.queryBalanceGroupSeries()  服务活动费用合并，保养费用拆分为，保养工时费用和保养配件费用
	 *         a. 增加服务活动费用合计项
	 *         b. 保养费用拆分为，保养工时费用和保养配件费用
	 *         c. 同时去掉：售后、售前其他项目费用；
	 *            注：去掉后会导致统计的总费用同明细费用相差对应费用
	 *         e. 将追加工时费用加入到工时费中
	 *            注：免费保养：加入到免费保养总金额中
	 *                服务活动：加入到服务活动工时中
	 *                其他：加入到工时费用统计中
	 *      3、jsp/claim/application/dealerBalanceView.jsp 
	 *         a. 页面格式调整
	 *         b. 将服务活动费用金额合并成一个总金额
	 *         c. 将追加工时费用加入到工时费用中
	 *         d. 增加特殊费显示（追加费用）
	 *         e. 将其他项目费用 更名为 救急费  
	 *  注意：
	 *       同时修改结算单审核后更新结算单主从表数据（结算室审核时可以修改结算金额）     
	 * 2010-10-12 XZM 修改
	 * 规则：
	 *       1、一级经销商可以结算对应下级经销商的索赔单
	 *       2、非一级经销商不能进行结算 
	 * 影响：
	 *       1、balanceDao.queryBalanceStatis()
	 *         a、加入统计二级经销商
	 *         b、统计时将所有该经销商及其下级经销商统计的结算金额，同统计到该经销商下
	 *          结算单中的结算经销商写成该经销商。
	 *       2、balanceDao.queryBalanceGroupSeries()
	 *         a、加入统计二级经销商
	 *       3、去掉检测是否回运配件规则
	 *2010-10-20 修改
	 * 规则：
	 *       1、二级经销商现在可以提报结算，提报后的结算单，先到一级经销商出
	 *          由一级经销商统一上报
	 * 影响：
	 *       1、balanceDao.queryBalanceStatis()
	 *          a、不统计其下级经销商结算信息
	 *          b、统计索赔单的时间段由上报时间修改为审核通过时间
	 *       2、balanceDao.queryBalanceGroupSeries()
	 *          a、不统计其下级经销商结算信息  
	 *          b、统计索赔单的时间段由上报时间修改为审核通过时间
	 */
	public void balanceView(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			/***********Iverson add By 2010-11-17******************/
			String endBalanceDate = request.getParamValue("endBalanceDate");//结算起始时间
			String conEndDay = request.getParamValue("CON_END_DAY");//结算终止时间
			/***********Iverson add By 2010-11-17******************/
			String yieldly = request.getParamValue("YIELDLY");//产地
			String dealerId = logonUser.getDealerId();//经销商ID
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);//用户所属公司
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			
			String startTime = "";
			String endTime = "";
			TmDealerPO dealerPO = new TmDealerPO();
			List<Map<String,Object>> feeList = new ArrayList<Map<String,Object>>();
			TcUserPO userPO = new TcUserPO();
			TmRegionPO regionPO = new TmRegionPO();
			List<Map<String,Object>> balanceDetail = new ArrayList<Map<String,Object>>();
			TmDealerPO invoiceMaker = new TmDealerPO();
			Map<String,Object> feeMap = new HashMap<String, Object>();
			String isReturn = "true";//控制是否可以生成结算单 true : 可以：; false ：不可以
			
			/***********Iverson add By 2010-11-17******************/
			if(Utility.testString(endBalanceDate))
				startTime = endBalanceDate + " 00:00:00";
			
			if(Utility.testString(conEndDay))
				endTime = conEndDay + " 23:59:59";
			/***********Iverson add By 2010-11-17******************/
			
			//取得当前系统时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String today = sdf.format(new Date());
			
			//if(partList==null || partList.size()<1){//存在需要回运但未回运的配件
				//查询经销商信息
				ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
				List<PO> dealerList = auditingDao.queryDealerById(Long.parseLong(dealerId));
				
				if(dealerList!=null && dealerList.size()>0)
					dealerPO = (TmDealerPO)dealerList.get(0);
				//根据经销商信息查询开票单位
				invoiceMaker = balanceDao.queryInvoiceMaker(Long.parseLong(dealerId)); //开票经销商
				TmDealerPO balanceMaker = balanceDao.queryBalanceMaker(Long.parseLong(dealerId)); //结算经销商
				
				//加载保养费用中固定的工时费用所占额度
				Double fixedAmount = balanceDao.queryFreeClaimFixedAmount(Constant.FREE_PARA_TYPE);
				
				//查询相关费用信息
				feeList = balanceDao.queryBalanceStatis(Long.parseLong(dealerId), yieldly,
						startTime, endTime, companyId,fixedAmount);
				if(feeList!=null && feeList.size()>0)
					feeMap = feeList.get(0);
	
				//取得登陆用户信息
				ClaimManualAuditingDao maDao = new ClaimManualAuditingDao();
				userPO = maDao.queryUserById(logonUser.getUserId());
				
				//查询省份信息
				String regionCode = "";
				if(dealerPO!=null && dealerPO.getProvinceId()!=null)
					regionCode = dealerPO.getProvinceId().toString();
				regionPO = balanceDao.queryProvince(regionCode);
				
				//按车系汇总结算信息
				balanceDetail = balanceDao.queryBalanceGroupSeries(Long.parseLong(dealerId), yieldly, 
						startTime, endTime, companyId,fixedAmount);
				
				//市场公单费用
				/******add by liuxh 20101116 修改市场公单费用*********/
				Map<String,Object> specialFeeMap=new HashMap<String,Object>();
				Map<String,Object> specialMarketFeeMap = balanceDao.queryMarketFeeToBalanceOrder2(Long.parseLong(dealerId),endBalanceDate,conEndDay,yieldly);
				//特殊公单费用
				Map<String,Object> specialOutFeeMap = balanceDao.querySpecialFeeToBalanceOrder2(Long.parseLong(dealerId),endBalanceDate,conEndDay,yieldly);
				specialFeeMap.put("MARKETFEE", specialMarketFeeMap.get("MARKETFEE"));
				specialFeeMap.put("OUTFEE", specialOutFeeMap.get("OUTFEE"));
				/******add by liuxh 20101116 修改市场公单费用*********/
				//将特殊费用加入到总费用中
				/******mod by liuxh 20101213 无索赔单也可做结算单******/
				//if(feeMap!=null && feeMap.containsKey("BALANCE_AMOUNT")){
					//Object marketFee = CommonUtils.getDataFromMap(specialMarketFeeMap, "MARKETFEE");
					//Object outFee = CommonUtils.getDataFromMap(specialOutFeeMap, "OUTFEE");
					//Object totalFee = CommonUtils.getDataFromMap(feeMap, "BALANCE_AMOUNT");
					Object marketFee = CommonUtils.getDataFromMap(specialMarketFeeMap, "MARKETFEE");
					marketFee=marketFee.toString().equals("")?"0":marketFee;
					Object outFee = CommonUtils.getDataFromMap(specialOutFeeMap, "OUTFEE").toString();
					outFee=outFee.toString().equals("")?"0":outFee;
					Object totalFee = CommonUtils.getDataFromMap(feeMap, "BALANCE_AMOUNT").toString();
					totalFee=totalFee.toString().equals("")?"0":totalFee;
					Double totalFeeInt = 0d;
					BigDecimal bd = new BigDecimal("0");
					if(!"".equals(totalFee) && totalFee!=null){
						bd = bd.add(new BigDecimal(totalFee.toString()));
					}
					if(!"".equals(marketFee) && marketFee!=null){
						bd = bd.add(new BigDecimal(marketFee.toString()));
					}
					if(!"".equals(outFee) && outFee!=null){
						bd = bd.add(new BigDecimal(outFee.toString()));
					}
					totalFeeInt = bd.doubleValue();
					feeMap.put("BALANCE_AMOUNT", totalFeeInt);
				//}
					/******mod by liuxh 20101213 无索赔单也可做结算单******/
					
			/******add by liuxh 20101211******/
			List spcFeelist=balanceDao.getSpecialFeeToBalanceOrder(Long.valueOf(dealerId), endBalanceDate, conEndDay, yieldly);
			act.setOutData("spcFeelist", spcFeelist);
			/******add by liuxh 20101211******/
			
			/***********Iverson add By 2010-11-17******************/
			act.setOutData("STARTDATE", endBalanceDate);
			act.setOutData("ENDDATE", conEndDay);
			/***********Iverson add By 2010-11-17******************/
			/******add by liuxh 20101213 修改产地******/
			act.setOutData("YIELDLY", yieldly);
			/******add by liuxh 20101213 修改产地******/
			act.setOutData("TODAY", today);
			act.setOutData("DEALERPO", dealerPO);//经销商信息
			act.setOutData("INVOICEMAKER", invoiceMaker);//开票单位信息
			act.setOutData("BALANCEEMAKER", balanceMaker);//结算经销商
			act.setOutData("FEEMAP", feeMap);//结算费用信息
			act.setOutData("USERVO", userPO);//登陆用户信息
			act.setOutData("REGION", regionPO);//省份信息
			act.setOutData("BALANCEDETAIL", balanceDetail);//结算明细
			act.setOutData("ISRETURN", isReturn);//是否需要回运的配件都回运了
			act.setOutData("specialFeeMap", specialFeeMap);
			
			act.setOutData("conEndDay", conEndDay);//把结束时间传到明细页面去
			
			act.setForword(this.DEALER_BALANCE_VIEW);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商上报结算");
			logger.error(logonUser, e1);
			act.setException(e1);
		}	
	}
	
	/**
	 * 经销商提交结算
	 * 2010-09-21 XZM 修改 
	 * 	描述：将统计对应配件、工时和其他项目的费用都统计结算费用
	 *        同时统计 售前数量、售后数量、服务活动数量和免费保养数量等，都不相互包含
	 *  影响：
	 *       1、DealerBalanceDao.saveBalanceDetail()  生成结算单明细   
	 *       2、DealerBalanceDao.reCheckBalanceAmount() 重新结算结算单中数据
	 *  注意：
	 *       同时修改结算单审核后更新结算单主从表数据（结算室审核时可以修改结算金额） 
	 * 2010-09-27 XZM 修改
	 * 描述：1、结算单对应服务活动费用显示呈现总费用（工时+配件+其他+固定费用） 
	 *       2、结算单明细中保养费用拆分为(保养工时费用、保养配件费用)
	 *          规则：长安现在：每次保养时，其中固定50元为保养工时费用，其他为保养配件费用
	 *  影响：
	 *      1、this.getbalanceOrder() 
	 *         新增服务活动总金额、追加工時費用、特殊费用(追加费用)、保养工时费用、保养配件费用
	 *      2、DealerBalanceDao.saveBalanceDetail()
	 *      
	 *         
	 *  注意：
	 *       同时修改结算单审核后更新结算单主从表数据（结算室审核时可以修改结算金额）  
	 *2010-10-12 修改
	 * 描述：
	 *       1、一级经销商可以结算对应下级经销商的索赔单
	 *       2、非一级经销商不能进行结算 
	 * 影响： 
	 *       1、balanceDao.saveRelaBetweenClaimAndBalance()
	 *          统计该经销商及其下级经销商所有该结算的索赔单
	 *       2、balanceDao.reCheckBalanceAmount()
	 *          统计时将所有该经销商及其下级经销商统计的结算金额，同统计到该经销商下
	 *          结算单中的结算经销商写成该经销商。
	 *2010-10-20 修改
	 * 描述：
	 * 		 1、二级经销商现在可以提报结算单
	 *          结算单只是提报到一级，由一级经销商一起提报到车厂
	 * 影响：
	 *       1、balanceDao.saveRelaBetweenClaimAndBalance()
	 *          a、不取该经销商对应的下级经销商
	 *          b、将统计时间由索赔单上报日期修改为审核通过日期
	 *       2、balanceDao.saveBalanceOrder();
	 *          a、加入记录其上级经销商ID
	 *          b、加入记录该经销商级别
	 *2010-10-30 修改
	 *描述：
	 *       1、先将特殊费用 加入到结算单中
	 *          注：特殊费用 的状态 为 未提报
	 *       2、标识特殊费用合计到指定的结算单中
	 *2010-11-04 修改
	 *描述：
	 *       1、加入特殊费用 工单数统计
	 *          记录统计到工单中的特殊费用数，用于控制是否可以转复合申请单
	 *影响： 
	 *       a、addSpecialFeeToBalanceOrder()
	 *          记录特殊费用工单数
	 *       b、调换将"特殊费用统计对应结算单中，同时标识对应特殊费用合计到该结算单"和
	 *          "重新结算结算单中数据"
	 */
	@SuppressWarnings("unchecked")
	public void balance(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String dealerId = "";
		String SUCCESS = "SUCCESS";
		try{
			RequestWrapper request = act.getRequest();
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);//用户所属公司
			String yieldly = request.getParamValue("YIELDLY");//产地代码
			dealerId = request.getParamValue("DEALER_ID");//经销商代码
			String startDate = request.getParamValue("START_DATE");//开始时间
			String endDate = request.getParamValue("END_DATE");//结束日期
			if(Utility.testString(startDate))
				startDate = startDate + " 00:00:00";
			if(Utility.testString(endDate))
				endDate = endDate + " 23:59:59";
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			/*********Iverson add 2010-12-01 根据经销商ID和基地查询吃此经销商是否被停止结算*********/
			String status=balanceDao.getDealerStatus(Long.valueOf(dealerId), Long.valueOf(yieldly));
			if(status.equals(Constant.IF_TYPE_YES.toString())){
				SUCCESS="NO";
			}
			/*********Iverson add 2010-12-01 根据经销商ID和基地查询吃此经销商是否被停止结算*********/
			else{			
				boolean flag = DBLockUtil.lock(dealerId, DBLockUtil.BUSINESS_TYPE_02);
				if(flag) {//加入数据库同步锁
					//1、创建结算单，保存对应信息
					TtAsWrClaimBalancePO balancePO = this.getbalanceOrder(request);
						
					Long topDealerId = -1L;
					Integer dealerLevel = Constant.DEALER_LEVEL_02;
					//11、查询对应经销商信息
					TmDealerPO conditionPO = new TmDealerPO();
					conditionPO.setDealerId(Long.parseLong(dealerId));
					List<TmDealerPO> dealerList = balanceDao.select(conditionPO);
					if(dealerList!=null && dealerList.size()>0){
						TmDealerPO tempPO = dealerList.get(0);
						if(tempPO.getDealerLevel()!=null)
							dealerLevel = tempPO.getDealerLevel();
					}
							
					//12、查询上级经销商信息
					TmDealerPO parentDealerPO = balanceDao.queryInvoiceMaker(Long.parseLong(dealerId));
					if(parentDealerPO!=null && parentDealerPO.getDealerId()!=null){
						topDealerId = parentDealerPO.getDealerId();
					}
					//根据经销商信息查询开票单位
					TmDealerPO invoiceMaker = balanceDao.queryInvoiceMaker(Long.parseLong(dealerId)); //开票经销商
					TmDealerPO balanceMaker = balanceDao.queryBalanceMaker(Long.parseLong(dealerId)); //结算经销商
					
					Long balanceId = CommonUtils.parseLong(SequenceManager.getSequence(""));//结算单ID
					balancePO.setId(balanceId);
					balancePO.setOemCompanyId(companyId);
					balancePO.setUpdateBy(logonUser.getUserId());
					balancePO.setUpdateDate(new Date());
					balancePO.setCreateBy(logonUser.getUserId());
					balancePO.setCreateDate(new Date());
					balancePO.setDealerLevel(dealerLevel);
					/*******mod by liuxh 20101115 修改结算和开票经销商*******/
					balancePO.setTopDealerId(balanceMaker.getDealerId());  //结算经销商
					balancePO.setKpDealerId(invoiceMaker.getDealerId());   //开票经销商
					/*******mod by liuxh 20101115 修改结算和开票经销商*******/
					
					balancePO.setYieldly(CommonUtils.parseLong(checkNull(yieldly,"-1")));
					balancePO.setDealerId(CommonUtils.parseLong(dealerId));
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					balancePO.setStartDate(formatter.parse(startDate));
					balancePO.setEndDate(formatter.parse(endDate));
					balanceDao.saveBalanceOrder(balancePO);
					
					//2、统计满足条件索赔单信息
					balanceDao.saveRelaBetweenClaimAndBalance(CommonUtils.parseLong(dealerId), yieldly, startDate,
							endDate, companyId, logonUser.getUserId(), balanceId.toString());
					//21、更改索赔单状态(将统计过的索赔单状态修改为"结算审核中")
					balanceDao.updateClaimStatus(balanceId,Constant.CLAIM_APPLY_ORD_TYPE_08);
						
					//加载保养费用中固定的工时费用
					Double fixedAmount = balanceDao.queryFreeClaimFixedAmount(Constant.FREE_PARA_TYPE);
						
					//3、生成结算单明细
					balanceDao.saveBalanceDetail(balanceId,logonUser.getUserId(),fixedAmount);
					//31、将备注信息更新到结算单明细中
					this.modifyBalanceDetail(request, balanceId, balanceDao);
					//5、将特殊费用统计对应结算单中，同时标识对应特殊费用合计到该结算单
					/*******mod by liuxh 20101116 增加开始时间和结束时间两参数*********/
					this.addSpecialFeeToBalanceOrder(Long.parseLong(dealerId),balanceId,startDate,endDate,yieldly);
					//6、重新结算结算单中数据
					balanceDao.reCheckBalanceAmount(balanceId,fixedAmount);
					//5、调用结算单自动审核 （ 修改 20100909 结算单生成时 先不做自动审核  当收单确认后 再进行审核）
					//this.autoAuditing(balanceId);
					/****add by liu 20101129 经销商增加结算单时保存申请金额备份字段****/
					balanceDao.updateBalanceBak(balanceId);
					/****add by liu 20101129 经销商增加结算单时保存申请金额备份字段****/
					balanceDao.balancrApplyAmount(balanceId);//重新统计APPLY_AMOUNT
					//7、记录审核结果
					BalanceStatusRecord.recordStatus(balanceId, logonUser.getUserId(), logonUser.getName(), 
							logonUser.getOrgId(), BalanceStatusRecord.STATUS_01);
					
					/********Iverson 2011-04-19 根据经销商Id和基地更新结算时间****************/
			        TtAsDealerTypePO po = new TtAsDealerTypePO();
			        po.setDealerId(Long.parseLong(logonUser.getDealerId()));//经销商
			        po.setYieldly(yieldly);//产地
			        TtAsDealerTypePO poTime = new TtAsDealerTypePO();
			        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			        String conEndDay = request.getParamValue("conEndDay");
			        poTime.setBalanceDate(sdf.parse(request.getParamValue("conEndDay")));//修改值
			        poTime.setUpdateBy(logonUser.getUserId().toString());
			        poTime.setUpdateDate(new Date());
					balanceDao.update(po, poTime);
					/********Iverson 2011-04-19 根据经销商Id和基地更新结算时间****************/
					
					/********Iverson 2012-02-24 调用开票信息下发接口下发开票信息****************/
					OSB34 osb34 = new OSB34();
					osb34.sendDate(balancePO.getBalanceNo(), balancePO.getDealerCode(), 1);
				}else{
					SUCCESS = "DEALED";
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商上报结算");
			logger.error(logonUser, e1);
			act.setException(e1);
		} finally{
			DBLockUtil.freeLock(dealerId, DBLockUtil.BUSINESS_TYPE_02);
		}
		act.setOutData("SUCCESS", SUCCESS);
	}
	
	/**
	 * 将特殊费用统计对应结算单中，同时标识对应特殊费用合计到该结算单
	 * @param dealerId 经销商ID
	 * @param balanceId 结算单ID
	 */
	private void addSpecialFeeToBalanceOrder(Long dealerId,Long balanceId,String startDate,String endDate,String yieldly){
		
		if(dealerId==null || balanceId==null)
			return;
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		//1、根据经销商将其他对应非费用标识到该结算单中
		/********mod by liuxh 20101116 增加时间段参数*******/
		balanceDao.markSpecialFeeToBalanceOrder(dealerId, balanceId,startDate,endDate,yieldly);
		//2、根据标识的状态和特殊费用的状态统计特殊费用
		List<Map<String,Object>> feeList = balanceDao.getSpecialFeeByBalanceId(balanceId);
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
		balanceDao.addSpecialFeeToBalanceOrder(balanceId, marketFee, outFee, feeCount);
	}
	
	/**
	 * 取的结算单信息
	 * @param request
	 * @return
	 */
	private TtAsWrClaimBalancePO getbalanceOrder(RequestWrapper request){
		
		TtAsWrClaimBalancePO balancePO = new TtAsWrClaimBalancePO();
		String balanceNO = request.getParamValue("BALANACE_NO");//结算单号
		String applyName = request.getParamValue("APPLY_NAME");//申请人姓名
		String applyId = request.getParamValue("APPLY_ID");//申请人ID
		String dealerCode = request.getParamValue("DEALER_CODE");//经销商代码
		String dealerName = request.getParamValue("DEALER_NAME");//经销商名称
		//String status = Constant.ACC_STATUS_06;//结算单待收单
		String status = Constant.ACC_STATUS_07;//结算单未上报
		String invoiceMaker = request.getParamValue("INVOICE_MAKER");//开票单位
		String labourAmount = request.getParamValue("LABOUR_AMOUNT");//工时金额
		String partAmount = request.getParamValue("PART_AMOUNT");//配件金额
		String otherAmount = request.getParamValue("OTHER_AMOUNT");//其他费用金额
		String freeAmount = request.getParamValue("FREE_AMOUNT");//免费保养金额
		String serviceAmount = request.getParamValue("SERVICE_AMOUNT");//服务活动金额
		String serviceLabourAmount = request.getParamValue("SERVICE_LABOUR_AMOUNT");//服务活动工时金额
		String servicePartAmount = request.getParamValue("SERVICE_PART_AMOUNT");//服务活动配件金额
		String serviceOtherAmount = request.getParamValue("SERVICE_OTHER_AMOUNT");//服务活动其他金额
		String serviceTotalAmount = request.getParamValue("SERVICE_TOTAL_AMOUNT");//服务活动总金额
		String appendAmount = request.getParamValue("APPEND_AMOUNT");//追加费用（特殊费用）
		String appendLabourAmount = request.getParamValue("APPENDLABOUR_AMOUNT");//追加工时费用（特殊费用）
		String returnAmount = request.getParamValue("RETURN_AMOUNT");//运费
		String claimCount = request.getParamValue("CLAIM_COUNT");//索赔单数
		//String totalAmount = request.getParamValue("TOTAL_AMOUNT");//总金额
		String remark = request.getParamValue("REMARK");//备注信息
		String stationerTel = request.getParamValue("STATIONER_TEL");//站长电话
		String claimerTel = request.getParamValue("CLAIMER_TEL");//索赔员电话
		String province = request.getParamValue("PROVINCE");//省份ID
		String balanceAmount = request.getParamValue("BALANCE_AMOUNT");//结算金额
		String freeLabourAmount = request.getParamValue("FREE_LABOUR_AMOUNT");//保养对应工时费用
		String freePartAmount = request.getParamValue("FREE_PART_AMOUNT");//保养对应配件费用
		
		balancePO.setBalanceNo(CommonUtils.checkNull(balanceNO));
		balancePO.setApplyPersonId(applyId==null?-1:CommonUtils.parseLong(applyId));
		balancePO.setApplyPersonName(CommonUtils.checkNull(applyName));
		balancePO.setDealerCode(CommonUtils.checkNull(dealerCode));
		balancePO.setDealerName(CommonUtils.checkNull(dealerName));
		balancePO.setStatus(CommonUtils.parseInteger(status));
		balancePO.setInvoiceMaker(CommonUtils.checkNull(invoiceMaker));
		balancePO.setLabourAmount(CommonUtils.parseDouble(checkNull(labourAmount,"0")));
		balancePO.setPartAmount(CommonUtils.parseDouble(checkNull(partAmount,"0")));
		balancePO.setOtherAmount(CommonUtils.parseDouble(checkNull(otherAmount,"0")));
		balancePO.setFreeAmount(CommonUtils.parseDouble(checkNull(freeAmount,"0")));
		balancePO.setServiceFixedAmount(CommonUtils.parseDouble(checkNull(serviceAmount,"0")));
		balancePO.setServiceLabourAmount(CommonUtils.parseDouble(checkNull(serviceLabourAmount,"0")));
		balancePO.setServicePartAmount(CommonUtils.parseDouble(checkNull(servicePartAmount,"0")));
		balancePO.setServiceOtherAmount(CommonUtils.parseDouble(checkNull(serviceOtherAmount,"0")));
		if(returnAmount!=null)
			balancePO.setReturnAmount(CommonUtils.parseDouble(checkNull(returnAmount,"0")));
		balancePO.setClaimCount(CommonUtils.parseLong(checkNull(claimCount,"0")));
		balancePO.setAmountSum(CommonUtils.parseDouble(checkNull(balanceAmount,"0")));
		balancePO.setRemark(CommonUtils.checkNull(remark));
		balancePO.setStationerTel(CommonUtils.checkNull(stationerTel));
		balancePO.setClaimerTel(CommonUtils.checkNull(claimerTel));

		if(!Utility.testString(province))
			province = "-1";
		balancePO.setProvince(CommonUtils.parseLong(checkNull(province,"-1")));
		balancePO.setBalanceAmount(CommonUtils.parseDouble(checkNull(balanceAmount,"0")));
		balancePO.setAppendAmount(CommonUtils.parseDouble(checkNull(appendAmount,"0")));
		balancePO.setAppendLabourAmount(CommonUtils.parseDouble(checkNull(appendLabourAmount,"0")));
		balancePO.setServiceTotalAmount(CommonUtils.parseDouble(checkNull(serviceTotalAmount,"0")));
		balancePO.setFreeLabourAmount(CommonUtils.parseDouble(checkNull(freeLabourAmount,"0")));
		balancePO.setFreePartAmount(CommonUtils.parseDouble(checkNull(freePartAmount,"0")));
		
		return balancePO;
	}
	
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
	 * 将备注信息更新到结算单明细
	 */
	private void modifyBalanceDetail(RequestWrapper request,Long balanceId,DealerBalanceDao balanceDao){
		
		if(balanceId==null || balanceDao==null)
			return;
		
		String seriesCode[] = request.getParamValues("SERIES_COCE");
		String remark[] = request.getParamValues("SERIESREMARK");
		
		if(seriesCode!=null && seriesCode.length>0 && remark!=null && remark.length>0){
			for (int i = 0; i < remark.length; i++) {
				if(remark[i]!=null && !"".equals(remark[i])){//有备注信息
					balanceDao.modifyBalanceDetail(seriesCode[i], remark[i], balanceId);
				}
			}
		}
	}
	
	/**
	 * 结算单自动审核
	 * @param balanceId
	 */
	public void autoAuditing(Long balanceId){
		
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		BalanceAuditingProxy proxy = new BalanceAuditingProxy();
		
		String balanceIdStr = balanceId.toString();
		//查询结算单对应索赔单
		List<Map<String,Object>> claimList = balanceDao.queryClaimByBalanceId(balanceId);
		//循环审核每条索赔单
		for (Map<String, Object> claimMap : claimList) {
			String claimId = claimMap.get("CLAIM_ID").toString();
			proxy.auditing(claimId,balanceIdStr);
		}
	}
	
	/**
	 * 经销商索赔单查询
	 */
	public void dealerBalanceOrderQuery(){
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
			
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = balanceDao.queryAccAuditList(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 查询结算单明细
	 */
	public void queryBalanceOrderDetail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String balanceId = request.getParamValue("id");//结算单ID
			String isConfirm = request.getParamValue("isConfirm");//是否是确认收单操作 true : false 不是
			if(!Utility.testString(isConfirm))
				isConfirm = "false";
	
			BalanceMainDao dao = new BalanceMainDao();
			Map<String, Object> map = dao.getBalanceMainMapView(balanceId);//结算单主表信息
			List<Map<String, Object>> list = dao.getBalanceMainList(balanceId);//结算单子表信息
			
			if(map.containsKey("STATUS")){
				String status = map.get("STATUS").toString();
				if(!Constant.ACC_STATUS_06.equals(status)){//该结算单已经收过单
					isConfirm = "false";
				}
			}
			
			//查询是否已签收
			List l = dao.getSignInfo(balanceId);
			if(l.size()>0){
				act.setOutData("l", (TtAsWrGatherBalancePO)l.get(0));
			}
			
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("isConfirm", isConfirm);
			act.setForword(this.DEALER_BALANCE_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算单明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询结算单明细（收单界面）
	 */
	public void queryBalanceOrderDetail3(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String balanceId = request.getParamValue("id");//结算单ID
			String isConfirm = request.getParamValue("isConfirm");//是否是确认收单操作 true : false 不是
			if(!Utility.testString(isConfirm))
				isConfirm = "false";
	
			BalanceMainDao dao = new BalanceMainDao();
			Map<String, Object> map = dao.getBalanceMainMapView2(balanceId);//结算单主表信息
			List<Map<String, Object>> list = dao.getBalanceMainList(balanceId);//结算单子表信息
			
			if(map.containsKey("STATUS")){
				String status = map.get("STATUS").toString();
				if(!Constant.ACC_STATUS_06.equals(status)){//该结算单已经收过单
					isConfirm = "false";
				}
			}
			
			//查询是否已签收
			List l = dao.getSignInfo(balanceId);
			if(l.size()>0){
				act.setOutData("l", (TtAsWrGatherBalancePO)l.get(0));
			}
			
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("isConfirm", isConfirm);
			act.setForword(this.DEALER_BALANCE_DETAIL3);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算单明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询结算单明细
	 */
	public void queryBalanceOrderDetail2(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String balanceId = request.getParamValue("id");//结算单ID
			String isConfirm = request.getParamValue("isConfirm");//是否是确认收单操作 true : false 不是
			if(!Utility.testString(isConfirm))
				isConfirm = "false";
	
			BalanceMainDao dao = new BalanceMainDao();
			Map<String, Object> map = dao.getBalanceMainMapView2(balanceId);//结算单主表信息
			List<Map<String, Object>> list = dao.getBalanceMainList(balanceId);//结算单子表信息
			
			if(map.containsKey("STATUS")){
				String status = map.get("STATUS").toString();
				if(!Constant.ACC_STATUS_06.equals(status)){//该结算单已经收过单
					isConfirm = "false";
				}
			}
			
			//查询是否已签收
			List l = dao.getSignInfo(balanceId);
			if(l.size()>0){
				act.setOutData("l", (TtAsWrGatherBalancePO)l.get(0));
			}
			
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("isConfirm", isConfirm);
			act.setForword(this.DEALER_BALANCE_DETAIL2);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算单明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询结算单明细
	 */
	public void querySumBalanceDetail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String balanceId = request.getParamValue("id");//结算单ID
			//balanceId = "2010092501021522";
			String isConfirm = request.getParamValue("isConfirm");//是否是确认收单操作 true : false 不是
			if(!Utility.testString(isConfirm))
				isConfirm = "false";
	
			BalanceMainDao dao = new BalanceMainDao();
			Map<String, Object> map = dao.getBalanceMainMapView2(balanceId);//结算单主表信息
			List<Map<String, Object>> list = dao.getBalanceMainList(balanceId);//结算单子表信息
			
			if(map.containsKey("STATUS")){
				String status = map.get("STATUS").toString();
				if(!Constant.ACC_STATUS_06.equals(status)){//该结算单已经收过单
					isConfirm = "false";
				}
			}
			
			act.setOutData("tawep", map);
			act.setOutData("myList", list);
			act.setOutData("isConfirm", isConfirm);
			if(CommonUtils.getCurCompanyCode().equals(Constant.COMPANY_CODE_CVS)){
				act.setForword(this.CLAIM_BILL_PRINT_URL);
			}else{
				act.setForword("/jsp/claim/application/sumBalancePrintJc.jsp");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算单明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 结算室收单操作查询
	 * 2010-10-12 修改
	 * 描述：加入产地限制
	 */
	public void balanceConfirmInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId());
			
			act.setOutData("yieldly", yieldly);
			//act.setForword(this.DEALER_BALANCE_CONFIRM);//按结算单单
			act.setForword(this.DEALER_BALANCE_CONFIRM2);//按汇总单收单
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单收单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}	
	}
	
	/**
	 * 结算室确认收单动作
	 * 步骤：
	 *      1、将结算单状态修改为"结算审核中"
	 *      2、自动审核结算单
	 */
	@SuppressWarnings("unchecked")
	public void balanceConfirm(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String operStatus = "success";
		try {
			RequestWrapper request = act.getRequest();
			String balanceId = request.getParamValue("id");//结算单ID
			if(balanceId==null || "".equals(balanceId))
				return;
			
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			//1、将结算单状态修改为"结算审核中"
			TtAsWrClaimBalancePO conditionPO = new TtAsWrClaimBalancePO();
			TtAsWrClaimBalancePO targetPO = new TtAsWrClaimBalancePO();
			conditionPO.setId(Long.parseLong(balanceId));
			//11、检测对应结算单是否已经做过收单动作
			List<TtAsWrClaimBalancePO> balanceList = balanceDao.select(conditionPO);
			if(balanceList!=null && balanceList.size()>0){
				TtAsWrClaimBalancePO balancePO = balanceList.get(0);
				if(Constant.ACC_STATUS_06.equals(CommonUtils.checkNull(balancePO.getStatus()))){
					//当对应结算单状态没有变化时操作该索赔单
					targetPO.setStatus(Integer.parseInt(Constant.ACC_STATUS_01));
					balanceDao.update(conditionPO, targetPO);
					//2、结算单审核
					this.autoAuditing(Long.parseLong(balanceId));
				}else{//已经收过单
					operStatus = "confirmed";
				}
			}
			
			//6、记录审核结果
			BalanceStatusRecord.recordStatus(Long.parseLong(balanceId), logonUser.getUserId(), logonUser.getName(), 
					logonUser.getOrgId(), BalanceStatusRecord.STATUS_03);

			act.setOutData("success", operStatus);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室确认收单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 变更结算单状态
	 * 注：结算单上报
	 *     结算单完成
	 */
	@SuppressWarnings("unchecked")
	public void chanageOrderStatus(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String operStatus = "SUCCESS";
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("orderId");//结算单ID
			String status = request.getParamValue("status");//结算单状态
			
			TtAsWrClaimBalancePO conditionPO = new TtAsWrClaimBalancePO();
			conditionPO.setId(Long.parseLong(orderId));
			
			TtAsWrClaimBalancePO targetPO = new TtAsWrClaimBalancePO();
			targetPO.setStatus(Integer.parseInt(status));
			targetPO.setUpdateBy(logonUser.getUserId());
			targetPO.setUpdateDate(new Date());
			
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			balanceDao.update(conditionPO,targetPO);
			
			Integer rStatus = -1;
			if(Constant.ACC_STATUS_08.equals(status)){//非一级经销商上报(已上报)
				rStatus = BalanceStatusRecord.STATUS_02;
			}else if(Constant.ACC_STATUS_09.equals(status)){//一级经销商完成动作(已完成)
				rStatus = BalanceStatusRecord.STATUS_14;
			}
			
			//结算单状态变化记录
			BalanceStatusRecord.recordStatus(Long.parseLong(orderId), logonUser.getUserId(), logonUser.getName(), 
					logonUser.getOrgId(), rStatus);
			
			/********Iverson 2010-11-26 根据经销商Id和基地更新结算时间****************/
			/*TtAsWrClaimBalancePO endTime = (TtAsWrClaimBalancePO)balanceDao.select(conditionPO).get(0);
	        TtAsDealerTypePO po = new TtAsDealerTypePO();
	        po.setDealerId(Long.parseLong(logonUser.getDealerId()));//经销商
	        po.setYieldly(endTime.getYieldly().toString());//产地
	        TtAsDealerTypePO poTime = new TtAsDealerTypePO();
	        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        poTime.setBalanceDate(endTime.getEndDate());//修改值
			balanceDao.update(po, poTime);*/
			/********Iverson 2010-11-26 根据经销商Id更新结算时间****************/
		} catch (Exception e) {
			operStatus = "FAILURE";
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "变更结算单状态");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		act.setOutData("SUCCESS", operStatus);
	}
	
	/**
	 * 经销商删除自己的结算单
	 * 2010-10-30 修改
	 * 加入删除相关特殊费用关系 
	 */
	@SuppressWarnings("unchecked")
	public void deleteBalanceOrder(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String operStatus = "SUCCESS";
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("orderId");//结算单ID
			
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			//删除结算单信息
			
			TtAsWrClaimBalancePO sel = new TtAsWrClaimBalancePO();
			sel.setId(Long.parseLong(orderId));
			sel=(TtAsWrClaimBalancePO)balanceDao.select(sel).get(0);
			
			
			TtAsWrClaimBalancePO conditionPO = new TtAsWrClaimBalancePO();
			conditionPO.setId(Long.parseLong(orderId));
			int count = balanceDao.delete(conditionPO);
			if(count>0){
				/********Iverson 2010-12-02删除后回滚结算时间在tt_as_dealer_type表里面************/
				
				/*******add by liuxh 20101203 删除结算单前取得最大时间*******/
				String dealerId = logonUser.getDealerId();//得到经销商
				String time = balanceDao.getMaxDate(dealerId,sel.getYieldly());//得到要更新的时间
				/*******add by liuxh 20101203 删除结算单前取得最大时间*******/
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				TtAsDealerTypePO typeCon=new TtAsDealerTypePO();
				typeCon.setDealerId(Long.parseLong(dealerId));
				typeCon.setYieldly(sel.getYieldly().toString());
				TtAsDealerTypePO typeValue=new TtAsDealerTypePO();
				typeValue.setBalanceDate(sdf.parse(time));
				balanceDao.update(typeCon, typeValue);
				/********Iverson 2010-12-02删除后回滚结算时间在tt_as_dealer_type表里面************/
			}
			//删除结算单明细信息
			TtAsWrClaimBalanceDetailPO detailPO = new TtAsWrClaimBalanceDetailPO();
			detailPO.setBalanceId(Long.parseLong(orderId));
			balanceDao.delete(detailPO);
			//修改结算单对应索赔单状态(结算审核中-->审核通过)
			balanceDao.updateClaimStatusDel(Long.parseLong(orderId),Constant.CLAIM_APPLY_ORD_TYPE_04);
			//删除结算单通索赔单关系
			TrBalanceClaimPO bcPO = new TrBalanceClaimPO();
			bcPO.setBalanceId(Long.parseLong(orderId));
			balanceDao.delete(bcPO);
			//删除特殊费用同结算单关系
			balanceDao.deleteRelationOfSpecFeeAndBalance(Long.parseLong(orderId));
		} catch (Exception e) {
			operStatus = "FAILURE";
			e.printStackTrace();
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除该结算单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		act.setOutData("SUCCESS", operStatus);
	}
	
	//次方法用于判断要删除的结算单是否在之前月份还有结算单 Iverson add 2010-11-26
	public void deleteBalanceOrder1(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("orderId");//结算单ID
			
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			TtAsWrClaimBalancePO conditionPO = new TtAsWrClaimBalancePO();
			conditionPO.setId(Long.parseLong(orderId));
			TtAsWrClaimBalancePO po = (TtAsWrClaimBalancePO)balanceDao.select(conditionPO).get(0);//根据结算单Id查出生成结算单的日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			Date endDate = po.getEndDate();//结束日期
			String time = balanceDao.getMaxDate(logonUser.getDealerId(),po.getYieldly());//得到库中最大的结算单的开始和结束日期
			
			boolean msg = false;
			if(endDate.before(sdf.parse(time))){//判断大小(如果删除的单子有比它更大的,那么就返回true,前台提示它必须从大删到小)
				msg = false;
			}else{
				msg= true;
			}
			act.setOutData("msg", msg);
			act.setOutData("orderId", orderId);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除该结算单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 结算汇总单查询
	 */
    public void dealerGatherBalanceInit(){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(this.DEALER_GATHER_BALANCE_QUERY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算汇总单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
    }
    
    /**
     * 查询结算汇总单信息
     */
    public void gatherBalanceOrderQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerId = logonUser.getDealerId();//经销商ID
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));		//产地代码
			String balanceNo = CommonUtils.checkNull(request.getParamValue("balanceNo"));	//结算汇总单号
			String status = CommonUtils.checkNull(request.getParamValue("status"));		    //结算汇总单状态
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));	//建单开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));		//建单结束时间
			String startReportDate = CommonUtils.checkNull(request.getParamValue("startDate"));	//上报开始时间
			String endReportDate = CommonUtils.checkNull(request.getParamValue("endDate"));		//上报结束时间
			//当开始时间和结束时间相同时
			if(null!=startDate&&!"".equals(startDate)&&null!=endDate&&!"".equals(endDate)){
				startDate = startDate+" 00:00:00";
				endDate = endDate+" 23:59:59";
			}
			if(null!=startReportDate&&!"".equals(startReportDate)&&null!=endReportDate&&!"".equals(endReportDate)){
				startReportDate = startReportDate+" 00:00:00";
				endReportDate = endReportDate+" 23:59:59";
			}
			auditBean bean = new auditBean();
			bean.setDealerId(CommonUtils.checkNull(dealerId));
			bean.setYieldly(yieldly);
			bean.setBalanceNo(balanceNo);
			bean.setStatus(status);
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setStartReportDate(startReportDate);
			bean.setEndReportDate(endReportDate);
			bean.setOemCompanyId(String.valueOf(GetOemcompanyId.getOemCompanyId(logonUser)));
			
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = balanceDao.queryGatherBalanceOrder(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    }
    
	/**
	 * 根据经销商创建的结算单生成物结算汇总单
	 * 注意：非一级经销商没有该类权限
	 */
	@SuppressWarnings("unchecked")
	public void createGatherBalanceOrderPage(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			
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
			/****add by liuxh 20101115 取得经销商结算级别*****/
			POFactory factory = POFactoryBuilder.getInstance();
			TmDealerPO dealer=new TmDealerPO();
			dealer.setDealerId(new Long(dealerId));
			dealer=factory.select(dealer).get(0);
			act.setOutData("balanceLevel", dealer.getBalanceLevel());
			/****add by liuxh 20101115 取得经销商结算级别*****/
			act.setOutData("dealerLevel", dealerLevel);
			act.setOutData("dealerId", dealerId);
			act.setForword(this.DEALER_GATHER_BALANCE_ADD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "结算汇总单-创建初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询对应经销商可以生产汇总单的结算单
	 */
	public void queryBalanceOrderForGather(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String SUCCESS = "SUCCESS";
		try{
			RequestWrapper request = act.getRequest();
			String yieldly = request.getParamValue("yieldly");//产地
			String dealerId = logonUser.getDealerId();//登陆经销商ID
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);//区分微车和轿车公司ID
			
			auditBean bean = new auditBean();
			bean.setYieldly(yieldly);
			bean.setDealerId(dealerId);
			bean.setOemCompanyId(oemCompanyId.toString());
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			
			int pageSize = Integer.MAX_VALUE;
			PageResult<Map<String,Object>> ps = balanceDao.queryBalanceOrderForGather(bean,1,pageSize);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			SUCCESS = "FAILURE";
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		act.setOutData("SUCCESS", SUCCESS);
	}
	
	/**
	 * 查询对应经销商可以生产汇总单的结算单
	 */
	@SuppressWarnings("unchecked")
	public void addGatherBalanceOrder(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String SUCCESS = "SUCCESS";
		String dealerId = "";
		try{
			RequestWrapper request = act.getRequest();
			String yieldly = request.getParamValue("yieldly");//产地
			dealerId = logonUser.getDealerId();//登陆经销商ID
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);//区分微车和轿车公司ID
			String balanceIds[] = request.getParamValues("balanceId");//结算单ID
			String balanceRemarks[] = request.getParamValues("bRemark");//结算单备注
			
			if(balanceIds!=null && balanceIds.length>0){
				boolean isDeal = DBLockUtil.lock(dealerId, DBLockUtil.BUSINESS_TYPE_03);
				if(isDeal) {//同步创建物流单操作
					DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
					String gatherId = SequenceManager.getSequence("");
					String gatherNO = SequenceManager.getSequence("TO");
					boolean flag = false;
					//1、先保存结算汇总单同结算单关系
					for(int i=0;i<balanceIds.length;i++){
						//11、检测对应结算单是否已经生成汇总单
						TrGatherBalancePO gbPO = new TrGatherBalancePO();
						gbPO.setBalanceId(Long.parseLong(balanceIds[i]));
						List<?> checkList = balanceDao.select(gbPO);
						if(checkList!=null && checkList.size()>0)
							continue;
						//12、记录对应结算单同汇总单关系
						TrGatherBalancePO targetPO = new TrGatherBalancePO();
						targetPO.setId(Long.parseLong(SequenceManager.getSequence("")));
						targetPO.setBalanceId(Long.parseLong(balanceIds[i]));
						targetPO.setGatherId(Long.parseLong(gatherId));
						targetPO.setCreateBy(logonUser.getUserId());
						targetPO.setCreateDate(new Date());
						targetPO.setRemark(CommonUtils.checkNull(balanceRemarks[i]));
						balanceDao.insert(targetPO);
						//13、更新结算单状态为"待签收"
						TtAsWrClaimBalancePO balancePO = new TtAsWrClaimBalancePO();
						balancePO.setStatus(Integer.parseInt(Constant.ACC_STATUS_06));
						balancePO.setUpdateBy(logonUser.getUserId());
						balancePO.setUpdateDate(new Date());
						TtAsWrClaimBalancePO balanceCPO = new TtAsWrClaimBalancePO();
						balanceCPO.setId(Long.parseLong(balanceIds[i]));
						balanceDao.update(balanceCPO, balancePO);
						//131、结算单状态变化记录
						BalanceStatusRecord.recordStatus(Long.parseLong(balanceIds[i]), logonUser.getUserId(), logonUser.getName(), 
								logonUser.getOrgId(), Integer.parseInt(Constant.ACC_STATUS_06));
						flag = true;
					}
					//2、生成结算汇总单
					//21、检测对应关系表中是否存在该汇总单对应关系
					if(flag){
						//生成对应结算汇总单
						auditBean bean = new auditBean();
						bean.setId(gatherId);
						bean.setBalanceNo(gatherNO);
						bean.setStatus(Constant.BALANCE_GATHER_TYPE_01.toString());
						bean.setOemCompanyId(oemCompanyId.toString());
						bean.setYieldly(CommonUtils.checkNull(yieldly));
						bean.setDealerId(dealerId);
						balanceDao.createGatherBalanceOrder(bean, logonUser.getUserId());
					}
				}else{
					SUCCESS = "DEALED";
				}
			}
		} catch (Exception e) {
			SUCCESS = "FAILURE";
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "结算单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}finally{
			DBLockUtil.freeLock(dealerId, DBLockUtil.BUSINESS_TYPE_03);
		}
		act.setOutData("SUCCESS", SUCCESS);
	}
	
	/**
	 * 查询结算汇总单
	 */
	@SuppressWarnings("unchecked")
	public void queryGatherBalanceOrder(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String SUCCESS = "SUCCESS";
		try{
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("id");//汇总单ID
			String isSign = request.getParamValue("");//是否是签收操作 true 是 其他不是
			if(!Utility.testString(isSign))
				isSign = "false";
			
			TtAsWrGatherBalancePO conditionPO = new TtAsWrGatherBalancePO();
			conditionPO.setId(Long.parseLong(orderId));
			//查询汇总单信息
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			List<TtAsWrGatherBalancePO> resultList = balanceDao.select(conditionPO);
			
			TtAsWrGatherBalancePO balancePO = new TtAsWrGatherBalancePO();
			if(resultList!=null && resultList.size()>0)
				balancePO = resultList.get(0);
			act.setOutData("balancePO", balancePO);
			act.setOutData("isSign", isSign);
			act.setForword(this.GATHER_BALANCE_DETAIL);
		} catch (Exception e) {
			SUCCESS = "FAILURE";
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "结算汇总单明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		act.setOutData("SUCCESS", SUCCESS);
	}
	
	/**
	 * 查询结算汇总单对应明细
	 */
	public void queryGatherBalanceOrderDetail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String SUCCESS = "SUCCESS";
		try{
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("id");//汇总单ID
			
			TtAsWrGatherBalancePO conditionPO = new TtAsWrGatherBalancePO();
			conditionPO.setId(Long.parseLong(orderId));
			//查询汇总单明细信息信息
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = balanceDao.queryGatherBalanceOrderDetail(Long.parseLong(orderId), curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			SUCCESS = "FAILURE";
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "结算汇总单明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		act.setOutData("SUCCESS", SUCCESS);
	}
	
	/**
	 * 删除未上报的结算汇总单
	 */
	@SuppressWarnings("unchecked")
	public void deleteGatherBalanceOrder(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String SUCCESS = "SUCCESS";
		try{
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("orderId");//汇总单ID
			
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			TtAsWrGatherBalancePO conditionPO = new TtAsWrGatherBalancePO();
			conditionPO.setId(Long.parseLong(orderId));
			
			//1、删除汇总单信息
			balanceDao.delete(conditionPO);
			//2、根据汇总单通结算单关系表，
			//21、一级经销商，将结算单状态修改为"已完成"("待收单" --> "未上报")
			balanceDao.modifyBalanceOrderByGatherId(Long.parseLong(orderId), Integer.parseInt(Constant.ACC_STATUS_07),Constant.DEALER_LEVEL_01);
			//22、非一级经销商，将结算单状态修改为"已完成"("待收单" --> "已上报")
			balanceDao.modifyBalanceOrderByGatherId2(Long.parseLong(orderId), Integer.parseInt(Constant.ACC_STATUS_07),Constant.DEALER_LEVEL_01);
			//3、删除汇总单通结算单关系
			TrGatherBalancePO gatherPo = new TrGatherBalancePO();
			gatherPo.setGatherId(Long.parseLong(orderId));
			balanceDao.delete(gatherPo);
		} catch (Exception e) {
			SUCCESS = "FAILURE";
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "删除结算汇总单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		act.setOutData("SUCCESS", SUCCESS);
	}
	
	/**
	 * 改变结算汇总单状态
	 */
	@SuppressWarnings("unchecked")
	public void chanageGatherOrderStatus(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String SUCCESS = "SUCCESS";
		try{
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("orderId");//汇总单ID
			String status = request.getParamValue("status");//变更结算汇总单状态
			
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			TtAsWrGatherBalancePO conditionPO = new TtAsWrGatherBalancePO();
			conditionPO.setId(Long.parseLong(orderId));
			
			TtAsWrGatherBalancePO targetPO = new TtAsWrGatherBalancePO();
			targetPO.setStatus(Integer.parseInt(status));
			targetPO.setUpdateBy(logonUser.getUserId());
			targetPO.setUpdateDate(new Date());
			
			balanceDao.update(conditionPO, targetPO);
			
			/*********Iverson add By 2010-11-18*************************/
			List listDate = balanceDao.selectTime(orderId);//通过索赔汇总单ID查询结算开始和结束时间以及哪个经销商结算的
			for(int i = 0;i<listDate.size();i++){
				Map<String, Object> map = (Map)listDate.get(i);
				long dealerId = ((BigDecimal)map.get("DEALER_ID")).longValue();
				TmDealerPO po = new TmDealerPO();
				po.setDealerId(dealerId);
				TmDealerPO poValue = new TmDealerPO();
				poValue.setBeginBalanceDate((Date)map.get("START_DATE"));
				poValue.setEndBalanceDate((Date)map.get("END_DATE"));
				balanceDao.update(po, poValue);//通过for批量修改开始及结束时间
			}
			/*********Iverson add By 2010-11-18*************************/
			
		} catch (Exception e) {
			SUCCESS = "FAILURE";
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "修改结算汇总单状态");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		act.setOutData("SUCCESS", SUCCESS);
	}
	
    /**
     * 车厂端：结算汇总单签收查询
     */
    public void oemGatherBalanceOrderQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));		//产地代码
			String balanceNo = CommonUtils.checkNull(request.getParamValue("balanceNo"));	//结算汇总单号
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); //经销商代码集合
			String status = request.getParamValue("status");
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));	//建单开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));		//建单结束时间
			String startReportDate = CommonUtils.checkNull(request.getParamValue("startDate"));	//上报开始时间
			String endReportDate = CommonUtils.checkNull(request.getParamValue("endDate"));		//上报结束时间
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId());//该用户拥有的产地权限
			//当开始时间和结束时间相同时
			if(null!=startDate&&!"".equals(startDate)&&null!=endDate&&!"".equals(endDate)){
				startDate = startDate+" 00:00:00";
				endDate = endDate+" 23:59:59";
			}
			if(null!=startReportDate&&!"".equals(startReportDate)&&null!=endReportDate&&!"".equals(endReportDate)){
				startReportDate = startReportDate+" 00:00:00";
				endReportDate = endReportDate+" 23:59:59";
			}
			auditBean bean = new auditBean();
			bean.setDealerCode(dealerCode);
			bean.setYieldly(yieldly);
			bean.setYieldlys(yieldlys);
			bean.setBalanceNo(balanceNo);
			bean.setStatus(status);
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setStartReportDate(startReportDate);
			bean.setEndReportDate(endReportDate);
			bean.setOemCompanyId(String.valueOf(GetOemcompanyId.getOemCompanyId(logonUser)));
			
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = balanceDao.queryGatherBalanceOrder(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    }
    
    /**
     * 车厂端：签收结算汇总单
     */
    @SuppressWarnings("unchecked")
	public void signGatherBalanceOrder(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String SUCCESS = "SUCCESS";
		String orderId = "";
		try{
			RequestWrapper request = act.getRequest();
			orderId = request.getParamValue("id");//汇总单ID
			
			boolean isDeal = DBLockUtil.lock(orderId, DBLockUtil.BUSINESS_TYPE_04);
			if(isDeal) {//同步结算单签收动作
				DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
				//0、检测对应结算单状态是否变化(状态是否未已上报)
				TtAsWrGatherBalancePO checkPO = new TtAsWrGatherBalancePO();
				checkPO.setId(Long.parseLong(orderId));
				checkPO.setStatus(Constant.BALANCE_GATHER_TYPE_02);
				List<?> checkList = balanceDao.select(checkPO);
				if(checkList!=null && checkList.size()>0){
					//1、修改结算汇总单状态
					TtAsWrGatherBalancePO conditionPO = new TtAsWrGatherBalancePO();
					conditionPO.setId(Long.parseLong(orderId));
					TtAsWrGatherBalancePO gatherPO = new TtAsWrGatherBalancePO();
					gatherPO.setStatus(Constant.BALANCE_GATHER_TYPE_03);//已签收
					gatherPO.setUpdateBy(logonUser.getUserId());
					Date d = new Date();
					gatherPO.setUpdateDate(d);
					gatherPO.setSignDate(d);
					gatherPO.setSignPerson(logonUser.getUserId());
					balanceDao.update(conditionPO, gatherPO);
					
					//2、查询结算汇总单对应结算单并自动审核
					/*
					TrGatherBalancePO trgbPO = new TrGatherBalancePO();
					trgbPO.setGatherId(Long.parseLong(orderId));
					List<TrGatherBalancePO> trGatherBalanceList = balanceDao.select(trgbPO);
					if(trGatherBalanceList!=null && trGatherBalanceList.size()>0){
						for (TrGatherBalancePO trGatherBalancePO : trGatherBalanceList) {
							//1、将结算单状态修改为"结算审核中"
							TtAsWrClaimBalancePO balanceConPO = new TtAsWrClaimBalancePO();
							TtAsWrClaimBalancePO targetPO = new TtAsWrClaimBalancePO();
							balanceConPO.setId(trGatherBalancePO.getBalanceId());
							//11、检测对应结算单是否已经做过收单动作
							List<TtAsWrClaimBalancePO> balanceList = balanceDao.select(balanceConPO);
							if(balanceList!=null && balanceList.size()>0){
								TtAsWrClaimBalancePO balancePO = balanceList.get(0);
								if(Constant.ACC_STATUS_06.equals(CommonUtils.checkNull(balancePO.getStatus()))){
									//当对应结算单状态没有变化时操作该索赔单
									targetPO.setStatus(Integer.parseInt(Constant.ACC_STATUS_01));
									balanceDao.update(balanceConPO, targetPO);
									//2、结算单审核
									this.autoAuditing(trGatherBalancePO.getBalanceId());
								}else{//已经收过单
									SUCCESS = "CONFIRMED";
								}
							}
							
							//12、记录审核结果
							BalanceStatusRecord.recordStatus(trGatherBalancePO.getBalanceId(), logonUser.getUserId(), logonUser.getName(), 
									logonUser.getOrgId(), BalanceStatusRecord.STATUS_03);	
						}
					}
					*/
				}else{//该结算汇总单已经签收过
					SUCCESS = "ALLCONFIRMED";
				}
			}else{//其他人正在处理
				SUCCESS = "DEALED";
			}
		} catch (Exception e) {
			SUCCESS = "FAILURE";
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "修改结算汇总单状态");
			logger.error(logonUser, e1);
			act.setException(e1);
		}finally{
			DBLockUtil.freeLock(orderId, DBLockUtil.BUSINESS_TYPE_04);
		}
		act.setOutData("SUCCESS", SUCCESS);
    }
    
	/**
	 * 车厂端退回结算汇总单
	 */
	@SuppressWarnings("unchecked")
	public void returnGatherOrderStatus(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String SUCCESS = "SUCCESS";
		String orderId = "";
		try{
			RequestWrapper request = act.getRequest();
			orderId = request.getParamValue("orderId");//汇总单ID
			
			boolean isDeal = DBLockUtil.lock(orderId, DBLockUtil.BUSINESS_TYPE_04);
			if(isDeal){//同签收动作同步，防止
				String status = Constant.BALANCE_GATHER_TYPE_01.toString();
				
				DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
				TtAsWrGatherBalancePO conditionPO = new TtAsWrGatherBalancePO();
				conditionPO.setId(Long.parseLong(orderId));
				
				TtAsWrGatherBalancePO targetPO = new TtAsWrGatherBalancePO();
				targetPO.setStatus(Integer.parseInt(status));
				targetPO.setUpdateBy(logonUser.getUserId());
				targetPO.setUpdateDate(new Date());
				
				balanceDao.update(conditionPO, targetPO);
			}
		} catch (Exception e) {
			SUCCESS = "FAILURE";
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "修改结算汇总单状态");
			logger.error(logonUser, e1);
			act.setException(e1);
		}finally{
			DBLockUtil.freeLock(orderId, DBLockUtil.BUSINESS_TYPE_04);
		}
		act.setOutData("SUCCESS", SUCCESS);
	}
	
	/**
	 * 打印结算汇总单明细
	 */
	@SuppressWarnings("unchecked")
	public void printGatherBalanceOrder(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String gatherId = request.getParamValue("id");//结算汇总单ID
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			
			TtAsWrGatherBalancePO conditionPO = new TtAsWrGatherBalancePO();
			conditionPO.setId(Long.parseLong(gatherId));
			//查询汇总单信息
			List<TtAsWrGatherBalancePO> resultList = balanceDao.select(conditionPO);
			
			TtAsWrGatherBalancePO balancePO = new TtAsWrGatherBalancePO();
			if(resultList!=null && resultList.size()>0)
				balancePO = resultList.get(0);
			
			//查询结算汇总单明细
			List<Map<String,Object>> orderDetail = balanceDao.printBalanceGatherOrderDetail(gatherId);
			if(orderDetail==null)
				orderDetail = new ArrayList<Map<String,Object>>();
			
			//查询操作经商上信息
			TmDealerPO dealerPO = new TmDealerPO();
			if(balancePO!=null && balancePO.getDealerId()!=null){
				TmDealerPO dConditionPO = new TmDealerPO();
				dConditionPO.setDealerId(balancePO.getDealerId());
				List<TmDealerPO> resList = balanceDao.select(dConditionPO);
				if(resList!=null && resList.size()>0)
					dealerPO = resList.get(0);
			}
			
			//打印时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String printDate = sdf.format(new Date());
			
			act.setOutData("balancePO", balancePO);
			act.setOutData("orderDetail", orderDetail);
			act.setOutData("printDate", printDate);
			act.setOutData("dealerPO", dealerPO);
			act.setForword(this.PRINT_GATHER_BALANCE_DETAIL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算汇总单明细打印");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void showDate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String yieldly = request.getParamValue("yieldly");
		ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
		TtAsDealerTypePO po = new TtAsDealerTypePO();
		po.setDealerId(Long.parseLong(logonUser.getDealerId()));
		po.setYieldly(yieldly);
		TtAsDealerTypePO poValue = (TtAsDealerTypePO)claimBackDao.select(po).get(0);
		
		Date balanceDate = poValue.getBalanceDate();//取出时间
		Calendar calendar = Calendar.getInstance();//公用类，加年月日的
		calendar.setTime(balanceDate);
		calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
		balanceDate = calendar.getTime();//得到加一天后的值
		
		act.setOutData("balanceDate", balanceDate);
	}
	
	/**
	 * Iverson add By 2010-12-21
	 * 结算单扣款明细查询
	 */
    public void dealerClaimBalanceInit(){
    	ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String Command = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(Command)){
				String report_date = request.getParamValue("report_date");//索赔单状态
				String YIELDLY_TYPE = request.getParamValue("YIELDLY_TYPE");//结算基地
				
				ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
				String dealerId= logonUser.getDealerId();
			
				//分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页	
			
				Integer pageSize = 20;
				PageResult<Map<String, Object>> ps = dao.queryDealerAuditingClaim(report_date, YIELDLY_TYPE,dealerId,curPage,pageSize);
				
				//分页方法 end
				act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
			}else{
				act.setForword(acconeAuditClaimJC02);
			}
			
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    }
    
    public void balancetoal() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();
			
			RequestWrapper request = act.getRequest();
			/******add by liuxh 20101127 增加是否重新审批标志*********/
			/******add by liuxh 20101127 增加是否重新审批标志*********/
			//经销商ID
			String dealer_id = "";
			dealer_id =  request.getParamValue("dealer_id");
			int type = 0;
			if(logonUser.getDealerId() != null)
			{
				type = 1;
				act.setOutData("type", 1);
			}
			String report_date = request.getParamValue("report_date");//经销商ID
			String balance_yieldly = request.getParamValue("balance_yieldly");
			String subjectno = request.getParamValue("subjectno");//经销商ID
			String activtycode = request.getParamValue("activtycode");
			String RO_NO = request.getParamValue("RO_NO");
			
			String COMMAND= request.getParamValue("COMMAND");
			if(COMMAND != null && COMMAND.length()>0)
			{
				Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> listP= auditingDao.dealerpageApplicattion(dealer_id, report_date,balance_yieldly,type,subjectno,activtycode,RO_NO,Constant.PAGE_SIZE,curPage);	
				act.setOutData("ps", listP);
			}else
			{
				act.setOutData("dealer_id", dealer_id);
				act.setOutData("report_date", report_date);
				if(Utility.testString(balance_yieldly))
				{
					act.setOutData("balance_yieldly", balance_yieldly);
				}
				
				act.setForword(balancetoal);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
    
    public void SettlementSummaryDelExport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		OutputStream os = null;
		try {
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();
			RequestWrapper request = act.getRequest();
			String dealer_id =  request.getParamValue("dealer_id");
			String report_date = request.getParamValue("report_date");//经销商ID
			String balance_yieldly = request.getParamValue("balance_yieldly");
			String subjectno = request.getParamValue("subjectno");//经销商ID
			String activtycode = request.getParamValue("activtycode");
			List<Map<String, Object>> list1 = auditingDao.SettlementSummaryDelExport(dealer_id, report_date, balance_yieldly, subjectno, activtycode);
			
			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "经销商结算明细表.xls";
 			// 导出的文字编码客户关怀活动数据明细表
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();
 			listTemp.add("经销商代码");
 			listTemp.add("索赔单号");
 			listTemp.add("维修类型");
 			listTemp.add("VIN");
 			listTemp.add("申请金额");
 			listTemp.add("结算金额");
 			listTemp.add("索赔单状态");
 			listTemp.add("上报日期");
 			listTemp.add("活动代码");
 			list.add(listTemp);
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(list1!=null){
	 			for (int i = 0; i < list1.size(); i++) {
	 				map1 = list1.get(i);
	 				List<Object> listValue = new LinkedList<Object>();
	 				listValue.add(map1.get("DEALER_CODE") != null ? map1.get("DEALER_CODE") : "");
	 				listValue.add(map1.get("CLAIM_NO") != null ? map1.get("CLAIM_NO") : "");
	 				listValue.add(map1.get("CODE_DESC") != null ? map1.get("CODE_DESC") : "");
	 				listValue.add(map1.get("VIN") != null ? map1.get("VIN") : "");
	 				listValue.add(map1.get("REPAIR_TOTAL") != null ? map1.get("REPAIR_TOTAL") : "");
	 				listValue.add(map1.get("BALANCE_AMOUNT") != null ? map1.get("BALANCE_AMOUNT") : "");
	 				listValue.add(map1.get("CODE_DESC1") != null ? map1.get("CODE_DESC1") : "");
	 				listValue.add(map1.get("REPORT_DATE") != null ? map1.get("REPORT_DATE") : "");
	 				listValue.add(map1.get("CAMPAIGN_CODE") != null ? map1.get("CAMPAIGN_CODE") : "");
	 				list.add(listValue);
	 			}
 			}	
			
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"经销商结算明细表");
 			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算明细表导出失败！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
    
    
    
    
    public void balancetoalyx() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();
			
			RequestWrapper request = act.getRequest();
			/******add by liuxh 20101127 增加是否重新审批标志*********/
			/******add by liuxh 20101127 增加是否重新审批标志*********/
			String dealer_id = request.getParamValue("dealer_id");//经销商ID
			String report_date = request.getParamValue("report_date");//经销商ID
			String balance_yieldly = request.getParamValue("balance_yieldly");
			
			String COMMAND= request.getParamValue("COMMAND");
			if(COMMAND != null && COMMAND.length()>0)
			{
				Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> listP= auditingDao.dealerpageApplicattionyx(dealer_id, report_date,Constant.PAGE_SIZE,curPage);	
				act.setOutData("ps", listP);
			}else
			{
				act.setOutData("dealer_id", dealer_id);
				act.setOutData("report_date", report_date);
				act.setForword(balancetoalyx);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
    /**
     * Iverson add By 2010-12-21
     * 结算单扣款明细查询(分页)
     */
    public void claimBalanceQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerId = logonUser.getDealerId();//经销商ID
			String balanceNo = CommonUtils.checkNull(request.getParamValue("balanceNo"));	//结算单号
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));	//结算开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));		//结算结束时间
			String dealerCode = request.getParamValue("dealerCode");//经销商代码
			String dealerName = request.getParamValue("DEALER_NAME");//经销商名称
			String yieldly = request.getParamValue("yieldly");//经销商名称
			//当开始时间和结束时间相同时
			if(null!=startDate&&!"".equals(startDate)&&null!=endDate&&!"".equals(endDate)){
				startDate = startDate+" 00:00:00";
				endDate = endDate+" 23:59:59";
			}
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = balanceDao.queryClaimBalanceOrder(dealerId,balanceNo,startDate,endDate,dealerCode,dealerName,yieldly,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);//向前台传的list名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单扣款明细查询(分页)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    }
    
    /**
     * Iverson add By 2010-12-21
	 * 根据结算单号查询明细
	 * 
	 */
	public void detailByBalanceId(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			String id =request.getParamValue("id");
			List<Map<String, Object>> balanceInfo = balanceDao.balanceInfoByBalanceId(id);//根据结算单ID查找结算单信息
			List<Map<String, Object>> claimDetail = balanceDao.detailByBalanceId(id);
			List<Map<String, Object>> feeDetail = balanceDao.spefeeInfoByBalanceId(id);
			Map map = balanceInfo.get(0);
			act.setOutData("map", map);
			act.setOutData("list", claimDetail);
			act.setOutData("feeDetail", feeDetail);
			act.setOutData("balanceId", id);
			act.setForword(detailByBalanceId);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"经销商确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单号查询索赔单明细(跳转)
	 */
	public void claimInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String balanceId = request.getParamValue("id");	//结算单ID
			act.setOutData("id", balanceId);
			act.setForword(CLAIM_INFO);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单扣款明细查询(分页)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单号查询索赔单明细
	 */
	public void queryClaimInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String balanceId = request.getParamValue("balanceId");	//结算单ID
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = balanceDao.claimInfo(balanceId,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);//向前台传的list名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单扣款明细查询(分页)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单号查询特殊费用明细(跳转)
	 */
	public void feeInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String balanceId = request.getParamValue("id");	//结算单ID
			act.setOutData("id", balanceId);
			act.setForword(FEE_INFO);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单扣款明细查询(分页)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单号查询特殊费用明细(跳转)
	 */
	public void queryFeeInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String balanceId = request.getParamValue("balanceId");	//结算单ID
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = balanceDao.feeInfo(balanceId,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);//向前台传的list名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单扣款明细查询(分页)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单ID查询旧件、考核、行政扣款汇总信息
	 */
	public void deductByBalanceId(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			String id =request.getParamValue("id");
			List<Map<String, Object>> deductInfo = balanceDao.deductInfoByBalanceId(id);
			List<Map<String, Object>> fineInfo = balanceDao.fineByBalanceId(id);
			List<Map<String, Object>> adminDeductInfo = balanceDao.adminDeductInfoByBalanceId(id);
			act.setOutData("deductInfo", deductInfo);
			act.setOutData("fineInfo", fineInfo);
			act.setOutData("adminDeductInfo", adminDeductInfo);
			act.setForword(deductByBalanceId);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"经销商确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*********Iverson add By 2010-12-25 查看旧件明细*************************/
	public void deductInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String deduct = request.getParamValue("id");	//结算单ID
			act.setOutData("id", deduct);
			act.setForword(DEDUCT_INFO);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"旧件明细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*********Iverson add By 2010-12-25 查看旧件明细(分页)*************************/
	public void queryDeductInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String deductId = request.getParamValue("deductId");	//结算单ID
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = balanceDao.deductInfo(deductId,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);//向前台传的list名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"旧件明细(分页)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Iverson add By 2010-12-31
	 * 三包结算统计表
	 */
    public void queryClaimBalanceInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(this.QUERY_CLAIM_BALANCE_QUERY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "三包结算统计表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
    }
    /**
     * Iverson add By 2010-12-31
     * 三包结算统计表(分页)
     */
    public void selectClaimBalance(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerCode = request.getParamValue("dealerCode");	
			String DEALER_NAME = request.getParamValue("DEALER_NAME");	
			String yieldly = request.getParamValue("yieldly");
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));		
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));	
			//当开始时间和结束时间相同时
			if(null!=startDate&&!"".equals(startDate)&&null!=endDate&&!"".equals(endDate)){
				startDate = startDate+" 00:00:00";
				endDate = endDate+" 23:59:59";
			}
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			
			Double fixedAmount = balanceDao.queryFreeClaimFixedAmount(Constant.FREE_PARA_TYPE);
			
			PageResult<Map<String, Object>> ps = balanceDao.selectClaimBalance(dealerCode,DEALER_NAME,yieldly,startDate,endDate,fixedAmount,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);//向前台传的list名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单扣款明细查询(分页)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    }
    
    public void selectClaimBalanceReportExcel(){
    	ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			String dealerCode = request.getParamValue("dealerCode");	
			String DEALER_NAME = request.getParamValue("DEALER_NAME");	
			String yieldly = request.getParamValue("yieldly");
			Double fixedAmount = balanceDao.queryFreeClaimFixedAmount(Constant.FREE_PARA_TYPE);
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));		
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));	
			//当开始时间和结束时间相同时
			if(null!=startDate&&!"".equals(startDate)&&null!=endDate&&!"".equals(endDate)){
				startDate = startDate+" 00:00:00";
				endDate = endDate+" 23:59:59";
			}
			String[] head=new String[12];
			head[0]="维修站编码";
			head[1]="维修站名称";
			head[2]="生产厂家";
			head[3]="走保次数";
			head[4]="总走保工时费";
			head[5]="总走保材料费";
			head[6]="走保费";
			head[7]="工时费";
			head[8]="材料费";
			head[9]="工时费合计";
			head[10]="材料费合计";
			head[11]="合计";
			List<Map<String, Object>> list= balanceDao.selectClaimBalanceExcelList(dealerCode,DEALER_NAME,yieldly,startDate,endDate,fixedAmount);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[12];
						detail[0] = String.valueOf(map.get("DEALER_CODE"));
						detail[1] = String.valueOf(map.get("DEALER_NAME"));
						detail[2] = String.valueOf(map.get("YIELDLY"));
						detail[3] = String.valueOf(map.get("CON"));
						detail[4] = String.valueOf(map.get("BY_LABOUR_AMOUNT"));
						detail[5] = String.valueOf(map.get("BY_PART_AMOUNT"));
						detail[6] = String.valueOf(map.get("FREE_AMOUNT"));
						detail[7] = String.valueOf(map.get("LABOUR_AMOUNT"));
						detail[8] = String.valueOf(map.get("PART_AMOUNT"));
						detail[9] = String.valueOf(map.get("AMOUNT_LABOUR"));
						detail[10] = String.valueOf(map.get("AMOUNT_PART"));
						detail[11] = String.valueOf(map.get("SUM_BALANCE_AMOUNT"));
						list1.add(detail);
			    	}
			    }
				com.infodms.dms.actions.claim.basicData.ToExcel.toExcel111(ActionContext.getContext().getResponse(), request, head, list1);
		    }
		    act.setForword(QUERY_CLAIM_BALANCE_QUERY);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
    
    //Iverson add by 2011-01-18
    public void dealerInfoInit(){
    	ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(this.QUERY_DEALER_INFO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
    }
    
    //Iverson add by 2011-01-18
    public void dealerInfoQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
			String DEALER_LEVEL = CommonUtils.checkNull(request.getParamValue("DEALER_LEVEL"));
			String STATUS = CommonUtils.checkNull(request.getParamValue("STATUS"));	
			String area_code = CommonUtils.checkNull(request.getParamValue("area_code"));
			String province = CommonUtils.checkNull(request.getParamValue("province"));	
			
			//String.valueOf(GetOemcompanyId.getOemCompanyId(logonUser));
			
			DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = balanceDao.queryDealerInfo(dealerCode,dealerName,DEALER_LEVEL,STATUS,area_code,province,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    }
    /**
     * 开票通知的状态修改
     */
    public void openclaimbalance(){
		try {
			this.openclaimbalanceBystatus();
			this.findHurryPartBydearid();
			this.findGxPartBydealerId();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
    }
    /**
     * 查询开票的
     * @return
     */
	private StringBuffer openclaimbalanceBystatus() {
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		List<TtAsWrClaimBalancePO> list=balanceDao.openclaimbalanceBystatus(getCurrDealerId());
		StringBuffer sb=new StringBuffer();
		String kpnum="";
		if(list!=null&&list.size()>0){
			int temp=0;
			for (TtAsWrClaimBalancePO tc : list) {
				if(temp==list.size()-1){
					sb.append(tc.getRemark());
				}else{
					sb.append(tc.getRemark()+";");
				}
				temp++;
			}
			kpnum=sb.toString();
			if(!"".equals(kpnum)){
				act.setOutData("kpnum", kpnum); 
			}
		}
		return sb;
	}
	 /**
     * 查询紧急掉件的
     * @return
     */
	private void findHurryPartBydearid() {
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		List<TtAsPartBorrowPO> list1=balanceDao.findHurryPartBydearid(getCurrDealerId());
		StringBuffer sb1=new StringBuffer();
		String jjnum="";
		if(list1!=null&&list1.size()>0){
			int temp=0;
			for (TtAsPartBorrowPO tb : list1) {
				if(temp==list1.size()-1){
					sb1.append(tb.getBorrowNo()+";");
				}else{
					sb1.append(tb.getBorrowNo());
				}
				temp++;
			}
			jjnum=sb1.toString();
			if(!"".equals(jjnum)){
				act.setOutData("jjnum", jjnum); 
			}
		}
	}
	/**
	 * 查询广宣的
	 * @return
	 */
	private void findGxPartBydealerId() {
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		List<TtPartTransPO> list1=null;//balanceDao.findGxPartBydealerd(getCurrDealerId());
		StringBuffer sb1=new StringBuffer();
		String gxnum="";
		if(list1!=null&&list1.size()>0){
			int temp=0;
			for (TtPartTransPO tb : list1) {
				if(temp==list1.size()-1){
					sb1.append(tb.getTransCode()+";");
				}else{
					sb1.append(tb.getTransCode());
				}
				temp++;
			}
			gxnum=sb1.toString();
			if(!"".equals(gxnum)){
				act.setOutData("gxnum", gxnum);
			}
		}
	}
}
