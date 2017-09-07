package com.infodms.dms.actions.claim.oldPart;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.report.dmsReport.ApplicationDao;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean;
import com.infodms.dms.bean.ClaimOldPartApproveStoreListBean;
import com.infodms.dms.bean.TtAsWrOldPartBackListDetailBean;
import com.infodms.dms.bean.TtAsWrOldPartDetailListBean;
import com.infodms.dms.bean.TtAsWrOldPartSignDetailListBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.oldPart.ClaimApproveOldPartStoredDao;
import com.infodms.dms.dao.claim.oldPart.ClaimBackListDao;
import com.infodms.dms.dao.claim.oldPart.ClaimReturnReportDao;
import com.infodms.dms.dao.claim.oldPart.ClaimTools;
import com.infodms.dms.dao.feedbackMng.StandardVipApplyManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TrReturnLogisticsPO;
import com.infodms.dms.po.TtAsDealerTypePO;
import com.infodms.dms.po.TtAsWrAuthPricePO;
import com.infodms.dms.po.TtAsWrBarcodePartStockPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.po.TtAsWrOldpartDeductionPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtAsWrReturnAuthitemPO;
import com.infodms.dms.po.TtAsWrReturnedOrderPO;
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.dao.OutStoreDAO;
import com.infodms.yxdms.entity.oldpart.LogUpatePartProductCodePO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PageResult;
/**
 * 类说明：索赔旧件管理--审批入库功能
 * 作者：  赵伦达
 */
@SuppressWarnings("unchecked")
public class ClaimOldPartApporoveStorageManager extends BaseAction{
	private ClaimApproveOldPartStoredDao dao=ClaimApproveOldPartStoredDao.getInstance();
	//url导向
	private final String queryApproveListOrdUrl = "/jsp/claim/oldPart/queryApproveStorageList.jsp";
	private final String approveAndStoredPageUrl = "/jsp/claim/oldPart/approveAndStoredReturnList.jsp";
	
	//zhumingwei 2011-04-15
	private final String queryApproveListOrdUrl12 = "/jsp/claim/oldPart/queryApproveStorageList12.jsp";
	private final String queryApproveListOrdUrl13 = "/jsp/claim/oldPart/queryApproveStorageList13.jsp";
	private final String queryApproveListOrdUrl14 = "/jsp/claim/oldPart/queryApproveStorageList14.jsp";
	
	private final String queryOverdueListUrl = "/jsp/claim/oldPart/queryOverdueListUrl.jsp";
	
	private final String approveAndStoredPageUrl4 = "/jsp/claim/oldPart/approveAndStoredReturnList4.jsp";
	private final String queryDetailPageUrl11 = "/jsp/claim/oldPart/queryStoredDetailInfo11.jsp";
	
	private final String approveAndStoredPageUrl41 = "/jsp/claim/oldPart/approveAndStoredReturnList41.jsp";
	private final String approveAndStoredPageUrl411 = "/jsp/claim/oldPart/approveAndStoredReturnList411.jsp";
	private final String approveAndStoredPageUrl412 = "/jsp/claim/oldPart/approveAndStoredReturnList412.jsp";
	
	private final String approveAndStoredPageUrl51 = "/jsp/claim/oldPart/approveAndStoredReturnList51.jsp";
	//zhumingwei 2011-8-11
	private final String detail = "/jsp/claim/oldPart/detail.jsp";
	
	//yyh 2012-4-18
	private final String storageBoxNoDetail = "/jsp/claim/oldPart/storageBoxNoDetail.jsp";
	private final String storageBoxNoDetail1 = "/jsp/claim/oldPart/storageBoxNoDetail1.jsp";
	
	//KFQ 2013-5-4 16:58
	private final String PART_CLAIM_SET_URL="/jsp/claim/oldPart/partClaimSetMain.jsp";//配件索赔属性设置主页
	private final String PART_RETURN_SET_URL="/jsp/claim/oldPart/partReturnSetMain.jsp";//配件回运设置主页
	
	private final String QUERY_LIST_URL="/jsp/claim/oldPart/queryApproveOldPart.jsp";
	private final String oldPartSignQuery = "/jsp/claim/oldPart/oldPartSignQuery.jsp";//旧件签收查询
	private final String ugencyPartSignQuery = "/jsp/claim/oldPart/ugencyPartSignQuery.jsp";//旧件签收查询
	private final String oldPartSignQueryDetail = "/jsp/claim/oldPart/oldPartSignQueryDetail.jsp";//旧件签收明细
	private final String oldPartSignAuditPer = "/jsp/claim/oldPart/oldPartSignAuditPer.jsp";//旧件签收入库
	private final String oldPartSignAuditZg = "/jsp/claim/oldPart/oldPartSignAuditZg.jsp";//旧件签收入库
	private final String oldPartSignAudit = "/jsp/claim/oldPart/oldPartSignAudit.jsp";//旧件审核数据准备
	private final String oldPartSignAuditsZg = "/jsp/claim/oldPart/oldPartSignAuditsZg.jsp";//旧件审核数据准备
	private final String oldPartSignAuditDetail= "/jsp/claim/oldPart/oldPartSignAuditDetail.jsp";
	//private final String queryBackListPrints = "/jsp/claim/oldPart/queryReturnPrints.jsp";
	private final String queryBackListPrints = "/jsp/claim/oldPart/queryReturnPrint.jsp";
	private final String oldPartAuditPer = "/jsp/claim/oldPart/oldPartAuditPer.jsp";//旧件签收查询
	//二次审核查询页面
	private final String oldSecondAuditQueryInit_URL = "/jsp/claim/oldPart/oldPartSecondAuditQuery.jsp";//旧件签收查询

	private final String chooseProducerWin_URL = "/jsp/claim/oldPart/chooseProducerWin.jsp";//责任供应商选择弹窗
	
	/** 
	 * modify xzm 20100907 
	 * approveAndStoredPageUrl2 替换原来的 approveAndStoredPageUrl 
	 * 需求人员确定长安 针对一张回运单 只做一次签收动作
	 */
	private final String approveAndStoredPageUrl2 = "/jsp/claim/oldPart/approveAndStoredReturnList2.jsp";
	private final String queryDetailPageUrl = "/jsp/claim/oldPart/queryStoredDetailInfo.jsp";
	
	private final Integer pageSize = 100;
	private static final String[] EMPTY_ARRAY= new String[0];
	/**
	 * Function：索赔旧件审批入库--初始化
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-10 赵伦达
	 */
	public void queryListPage(){
		try {
			
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			
			act.setOutData("yieldly", yieldly);
			act.setForword(queryApproveListOrdUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void updateOtherRemark(){
		try {
			String id = request.getParamValue("id");
			String otherRemark = "";
			if (request.getParamValue("otherRemark") != null) {
				otherRemark = new String(request.getParamValue("otherRemark").getBytes("ISO-8859-1"),"gbk");
			}
			

			TtAsWrOldReturnedDetailPO po = new TtAsWrOldReturnedDetailPO();
			TtAsWrOldReturnedDetailPO po2 = new TtAsWrOldReturnedDetailPO();
			po.setId(Long.parseLong(id));
			po2.setOtherRemark(otherRemark);
			dao.update(po, po2);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件审核--其它原因");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-04-15
	public void queryListPage11(){
		try {
			
			
			
			dao  = ClaimApproveOldPartStoredDao.getInstance();
			//request.setAttribute("yieldly", Constant.PART_IS_CHANGHE_01);
			//取得该用户拥有的产地权限
			request=act.getRequest();
	    act.setForword(queryApproveListOrdUrl14);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void queryListPage112(){
		try {
			
			
			
			dao  = ClaimApproveOldPartStoredDao.getInstance();
			request.setAttribute("yieldly", Constant.PART_IS_CHANGHE_02);
			//取得该用户拥有的产地权限
			request=act.getRequest();
	    act.setForword(queryApproveListOrdUrl14);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//旧件签收审核入库操作
	public void oldPartSignAuditPer(){
		try {	
				
				//act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);
				act.getSession().remove("dealerCodeSS");
	    		act.setForword(oldPartSignAuditPer); 
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//旧件签收审核入库操作
	public void oldPartSignAuditZg(){
		try {	
				act=ActionContext.getContext();
				act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);
	    		act.setForword(oldPartSignAuditZg); 
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	//旧件签收审核入库操作
	public void oldPartSignAuditPer2(){
		try {
				
				act.setOutData("yieldly", Constant.PART_IS_CHANGHE_02);
	    		act.setForword(oldPartSignAuditPer); 
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//旧件签收审核查询操作
	public void oldPartAuditQuery(){
		try {
				
				act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);
	    		act.setForword(oldPartAuditPer); 
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//旧件签收审核查询操作
	public void oldPartAuditQuery2(){
		try {
				
				act.setOutData("yieldly", Constant.PART_IS_CHANGHE_02);
	    		act.setForword(oldPartAuditPer); 
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//旧件签收查询初始化
	public void oldPartSignQuery(){
		try {
			
			
			request.setAttribute("yieldly", Constant.PART_IS_CHANGHE_01);
			act.setForword(oldPartSignQuery);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void ugencyPartSignQuery(){
try {
			
			
			request.setAttribute("yieldly", Constant.PART_IS_CHANGHE_01);
			act.setForword(ugencyPartSignQuery);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	//旧件签收查询初始化
	public void oldPartSignQuery2(){
		try {
			
			
			request.setAttribute("yieldly", Constant.PART_IS_CHANGHE_02);
			act.setForword(oldPartSignQuery);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//旧件签收查询
	public void oldPartSignQueryList(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")) : 1;
		try {
			
			String yieldly = request.getParamValue("YIELDLY_TYPE");//查询条件=产地
			
			params.put("company_id",String.valueOf(companyId));
			params.put("i_dealer_code",request.getParamValue("dealerCode"));//经销商代码 
			params.put("i_dealer_name",request.getParamValue("dealerName"));//经销商简称
			params.put("i_return_no",request.getParamValue("back_order_no"));//回运清单号
			params.put("i_submit_start_date",request.getParamValue("report_start_date"));//提报开始时间
			params.put("i_submit_end_date",request.getParamValue("report_end_date"));//提报结束时间
			params.put("i_transport_type",request.getParamValue("freight_type"));//货运方式
			params.put("i_trans_no",request.getParamValue("trans_no"));//货运单号
			params.put("transport_no",request.getParamValue("transport_no"));//货运单号
			
			params.put("yieldly", yieldly);
			
			PageResult<ClaimOldPartApproveStoreListBean> ps = dao.oldPartSignQueryList(params,curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件查询结果--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//旧件签收查询
		public void ugencyPartSignQueryList(){
			
			
			
			dao=ClaimApproveOldPartStoredDao.getInstance();
			Map<String,String> params=new HashMap<String, String>();
			Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			try {
				
				String yieldly = request.getParamValue("YIELDLY_TYPE");//查询条件=产地
				
				params.put("company_id",String.valueOf(companyId));
				params.put("i_dealer_code",request.getParamValue("dealerCode"));//经销商代码 
				params.put("i_dealer_name",request.getParamValue("dealerName"));//经销商简称
				params.put("i_return_no",request.getParamValue("back_order_no"));//回运清单号
				params.put("i_submit_start_date",request.getParamValue("report_start_date"));//提报开始时间
				params.put("i_submit_end_date",request.getParamValue("report_end_date"));//提报结束时间
				params.put("i_transport_type",request.getParamValue("freight_type"));//货运方式
				params.put("i_trans_no",request.getParamValue("trans_no"));//货运单号
				params.put("yieldly", yieldly);
				
				PageResult<ClaimOldPartApproveStoreListBean> ps = dao.ugencyPartSignQueryList(params,curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件查询结果--条件查询");
				logger.error(loginUser, e1);
				act.setException(e1);
			}
		}
	
	
	
	public void queryListPage12(){
		try {
			
			
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			
			act.setOutData("yieldly", yieldly);
			
			//begin
			request=act.getRequest();
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));//
			String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//
			String back_order_no=CommonUtils.checkNull(request.getParamValue("back_order_no"));//
			String yieldly1=CommonUtils.checkNull(request.getParamValue("yieldly"));//
			String freight_type=CommonUtils.checkNull(request.getParamValue("freight_type"));//
			String create_start_date=CommonUtils.checkNull(request.getParamValue("create_start_date"));//
			String create_end_date=CommonUtils.checkNull(request.getParamValue("create_end_date"));//
			String report_start_date=CommonUtils.checkNull(request.getParamValue("report_start_date"));//
			String report_end_date=CommonUtils.checkNull(request.getParamValue("report_end_date"));//
			String back_type=CommonUtils.checkNull(request.getParamValue("back_type"));//
			String trans_no=CommonUtils.checkNull(request.getParamValue("trans_no"));//
			String isReturn=CommonUtils.checkNull(request.getParamValue("isReturn"));//
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("dealerName", dealerName);
			act.setOutData("back_order_no", back_order_no);
			act.setOutData("yieldly1", yieldly1);
			act.setOutData("freight_type", freight_type);
			act.setOutData("create_start_date", create_start_date);
			act.setOutData("create_end_date", create_end_date);
			act.setOutData("report_start_date", report_start_date);
			act.setOutData("report_end_date", report_end_date);
			act.setOutData("back_type", back_type);
			act.setOutData("trans_no", trans_no);
			act.setOutData("isReturn", isReturn);
			//end
			
			act.setForword(queryApproveListOrdUrl12);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//旧件条码入库查询
	public void queryListPage13(){
		try {
			
			
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			
			act.setOutData("yieldly", yieldly);
			
			//begin
			request=act.getRequest();
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));//
			String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//
			String back_order_no=CommonUtils.checkNull(request.getParamValue("back_order_no"));//
			String yieldly1=CommonUtils.checkNull(request.getParamValue("yieldly"));//
			String freight_type=CommonUtils.checkNull(request.getParamValue("freight_type"));//
			String create_start_date=CommonUtils.checkNull(request.getParamValue("create_start_date"));//
			String create_end_date=CommonUtils.checkNull(request.getParamValue("create_end_date"));//
			String report_start_date=CommonUtils.checkNull(request.getParamValue("report_start_date"));//
			String report_end_date=CommonUtils.checkNull(request.getParamValue("report_end_date"));//
			String back_type=CommonUtils.checkNull(request.getParamValue("back_type"));//
			String trans_no=CommonUtils.checkNull(request.getParamValue("trans_no"));//
			String isReturn=CommonUtils.checkNull(request.getParamValue("isReturn"));//
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("dealerName", dealerName);
			act.setOutData("back_order_no", back_order_no);
			act.setOutData("yieldly1", yieldly1);
			act.setOutData("freight_type", freight_type);
			act.setOutData("create_start_date", create_start_date);
			act.setOutData("create_end_date", create_end_date);
			act.setOutData("report_start_date", report_start_date);
			act.setOutData("report_end_date", report_end_date);
			act.setOutData("back_type", back_type);
			act.setOutData("trans_no", trans_no);
			act.setOutData("isReturn", isReturn);
			//end
			
			act.setForword(queryApproveListOrdUrl13);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔件审批入库--条件查询
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-12 赵伦达
	 */
	public void queryApproveList(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")) : 1;
		try {
			
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());//该用户拥有的产地
			String yieldly = request.getParamValue("yieldly");//查询条件=产地
			
			params.put("company_id",String.valueOf(companyId));
			params.put("i_dealer_code",request.getParamValue("dealerCode"));//经销商代码 
			params.put("i_dealer_name",request.getParamValue("dealerName"));//经销商简称
			params.put("i_return_no",request.getParamValue("back_order_no"));//回运清单号
			params.put("i_create_start_date",request.getParamValue("create_start_date"));//建单开始时间 
			params.put("i_create_end_date",request.getParamValue("create_end_date"));//建单结束时间
			params.put("i_submit_start_date",request.getParamValue("report_start_date"));//提报开始时间
			params.put("i_submit_end_date",request.getParamValue("report_end_date"));//提报结束时间
			params.put("i_transport_type",request.getParamValue("freight_type"));//货运方式
			params.put("i_back_type",request.getParamValue("back_type"));//回运单状态
			params.put("i_trans_no",request.getParamValue("trans_no"));//货运单号
			params.put("yieldlys", yieldlys);
			params.put("yieldly", yieldly);
			
			PageResult<ClaimOldPartApproveStoreListBean> ps = dao.queryClaimBackList(params,curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(queryApproveListOrdUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//zhumingwei 2011-04-21
	
	public void queryApproveList11(){
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")) : 1;
		try {
			
			String yieldly = request.getParamValue("YIELDLY_TYPE");//查询条件=产地
			params.put("company_id",String.valueOf(companyId));
			params.put("i_dealer_code",request.getParamValue("dealerCode"));//经销商代码 
			params.put("i_dealer_name",request.getParamValue("dealerName"));//经销商简称
			params.put("i_return_no",request.getParamValue("back_order_no"));//回运清单号
			params.put("i_create_start_date",request.getParamValue("create_start_date"));//建单开始时间 
			params.put("i_create_end_date",request.getParamValue("create_end_date"));//建单结束时间
			params.put("i_submit_start_date",request.getParamValue("report_start_date"));//提报开始时间
			params.put("i_submit_end_date",request.getParamValue("report_end_date"));//提报结束时间
			params.put("i_transport_type",request.getParamValue("freight_type"));//货运方式
			params.put("i_back_type",request.getParamValue("back_type"));//回运单状态
			
			params.put("i_transport_no",request.getParamValue("transport_no"));//货运单号
			params.put("i_type_name",request.getParamValue("type_name"));//货运单号
			params.put("yieldly", yieldly);    
			params.put("return_start_date",request.getParamValue("return_start_date"));//回运开始时间
			params.put("return_end_date",request.getParamValue("return_end_date"));//回运结束时间
			PageResult<ClaimOldPartApproveStoreListBean> ps = dao.queryClaimBackList11(params,curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			//act.setForword(queryApproveListOrdUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//旧件签收入库查询
	public void oldPartSignAuditQuery(){
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		try {
			String yieldly = request.getParamValue("YIELDLY_TYPE");//查询条件=产地
			
			params.put("company_id",String.valueOf(companyId));
			params.put("i_dealer_code",request.getParamValue("dealerCode"));//经销商代码 
			params.put("i_dealer_name",request.getParamValue("dealerName"));//经销商简称
			params.put("i_return_no",request.getParamValue("back_order_no"));//回运清单号
			params.put("i_submit_start_date",request.getParamValue("report_start_date"));//提报开始时间
			params.put("i_submit_end_date",request.getParamValue("report_end_date"));//提报结束时间
			params.put("i_create_start_date",request.getParamValue("create_start_date"));//审核开始时间
			params.put("i_create_end_date",request.getParamValue("create_end_date"));//审核结束时间
			params.put("i_transport_type",request.getParamValue("freight_type"));//货运方式
			params.put("i_back_type",request.getParamValue("back_type"));//回运单状态
			params.put("i_trans_no",request.getParamValue("trans_no"));//货运单号
			params.put("in_warhouse_name",request.getParamValue("in_warhouse_name"));//审核人
			params.put("yieldly", yieldly);
			
			PageResult<ClaimOldPartApproveStoreListBean> ps = dao.oldPartSignAuditQuery(params,getCurrPage(),
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setOutData("currP", getCurrPage());
			act.getSession().set("num", getCurrPage());
			if (request.getParamValue("dealerCode") != null && !("null").equals(request.getParamValue("dealerCode"))) {
			act.getSession().set("dealerCodeSS", request.getParamValue("dealerCode"));
			}
			act.setForword(oldPartSignAuditPer);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	//旧件签收入库查询
	public void oldPartSignAuditQueryZg(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")) : 1;
		try {
			String yieldly = request.getParamValue("YIELDLY_TYPE");//查询条件=产地
			
			params.put("company_id",String.valueOf(companyId));
			params.put("i_dealer_code",request.getParamValue("dealerCode"));//经销商代码 
			params.put("i_dealer_name",request.getParamValue("dealerName"));//经销商简称
			params.put("i_return_no",request.getParamValue("back_order_no"));//回运清单号
			params.put("i_submit_start_date",request.getParamValue("report_start_date"));//提报开始时间
			params.put("i_submit_end_date",request.getParamValue("report_end_date"));//提报结束时间
			params.put("i_create_start_date",request.getParamValue("create_start_date"));//审核开始时间
			params.put("i_create_end_date",request.getParamValue("create_end_date"));//审核结束时间
			params.put("i_transport_type",request.getParamValue("freight_type"));//货运方式
			params.put("i_back_type",request.getParamValue("back_type"));//回运单状态
			params.put("i_trans_no",request.getParamValue("trans_no"));//货运单号
			params.put("in_warhouse_name",request.getParamValue("in_warhouse_name"));//审核人
			params.put("yieldly", yieldly);
			
			PageResult<ClaimOldPartApproveStoreListBean> ps = dao.oldPartSignAuditQueryZg(params,curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	public void queryApproveList12(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")) : 1;
		try {
			
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());//该用户拥有的产地
			String yieldly = request.getParamValue("yieldly");//查询条件=产地
			
			params.put("company_id",String.valueOf(companyId));
			params.put("i_dealer_code",request.getParamValue("dealerCode"));//经销商代码 
			params.put("i_dealer_name",request.getParamValue("dealerName"));//经销商简称
			params.put("i_return_no",request.getParamValue("back_order_no"));//回运清单号
			params.put("i_create_start_date",request.getParamValue("create_start_date"));//建单开始时间 
			params.put("i_create_end_date",request.getParamValue("create_end_date"));//建单结束时间
			params.put("i_submit_start_date",request.getParamValue("report_start_date"));//提报开始时间
			params.put("i_submit_end_date",request.getParamValue("report_end_date"));//提报结束时间
			params.put("i_transport_type",request.getParamValue("freight_type"));//货运方式
			params.put("i_back_type",request.getParamValue("back_type"));//回运单状态
			params.put("i_trans_no",request.getParamValue("trans_no"));//货运单号
			params.put("sfsm",request.getParamValue("sfsm"));//是否扫描
			params.put("sfcq",request.getParamValue("sfcq"));//是否超期
			params.put("sfshz",request.getParamValue("sfshz"));//是否审核中
			params.put("sfcqsh",request.getParamValue("sfcqsh"));//是否经过超期审核
			params.put("yieldlys", yieldlys);
			params.put("yieldly", yieldly);
			
			PageResult<ClaimOldPartApproveStoreListBean> ps = dao.queryClaimBackList12(params,curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(queryApproveListOrdUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔件审批入库--转到入库页面
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-13
	 */
	public void goApproveAndStoredPage(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")) : 1;
		
		//回运要入库的回运清单号
		String claim_id=request.getParamValue("CLAIM_ID");//此处为回运清单中的主键
		params.put("i_claim_id", claim_id);
		try {
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo(params);
			request.setAttribute("returnListBean", returnListBean);
			
			//获取回运清单明细信息List
			PageResult<TtAsWrOldPartSignDetailListBean> ps=dao.queryClaimBackDetailList(params,curPage,
					Constant.PAGE_SIZE);
			request.setAttribute("detailList", (List<TtAsWrOldPartSignDetailListBean>)ps.getRecords());
			act.setOutData("ps", ps);
			act.setForword(approveAndStoredPageUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 索赔件审批入库--转到入库页面
	 * 注：需求人员确定长安 针对一张回运单 只做一次签收动作,页面数据没有分页
	 * 原操作方式：可以多次签收 goApproveAndStoredPage
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-09-07
	 */
	public void goApproveAndStoredPage2(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		
		//回运要入库的回运清单号
		String claim_id=request.getParamValue("CLAIM_ID");//此处为回运清单中的主键
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		try {
			//获取回运清单信息bean                                  getApproveAndStoredReturnInfo11
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			
			act.setOutData("returnListBean", returnListBean);
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			act.setForword(this.approveAndStoredPageUrl2);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void goApproveAndStoredPage5(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		
		//begin
		request=act.getRequest();
		String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));//
		String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//
		String back_order_no=CommonUtils.checkNull(request.getParamValue("back_order_no"));//
		String yieldly1=CommonUtils.checkNull(request.getParamValue("yieldly"));//
		String freight_type=CommonUtils.checkNull(request.getParamValue("freight_type"));//
		String create_start_date=CommonUtils.checkNull(request.getParamValue("create_start_date"));//
		String create_end_date=CommonUtils.checkNull(request.getParamValue("create_end_date"));//
		String report_start_date=CommonUtils.checkNull(request.getParamValue("report_start_date"));//
		String report_end_date=CommonUtils.checkNull(request.getParamValue("report_end_date"));//
		String back_type=CommonUtils.checkNull(request.getParamValue("back_type"));//
		String trans_no=CommonUtils.checkNull(request.getParamValue("trans_no"));//
		String isReturn=CommonUtils.checkNull(request.getParamValue("isReturn"));//
		String types = request.getParamValue("types");
		act.setOutData("dealerCode", dealerCode);
		act.setOutData("dealerName", dealerName);
		act.setOutData("back_order_no", back_order_no);
		act.setOutData("yieldly1", yieldly1);
		act.setOutData("freight_type", freight_type);
		act.setOutData("create_start_date", create_start_date);
		act.setOutData("create_end_date", create_end_date);
		act.setOutData("report_start_date", report_start_date);
		act.setOutData("report_end_date", report_end_date);
		act.setOutData("back_type", back_type);
		act.setOutData("trans_no", trans_no);
		act.setOutData("isReturn", isReturn);
		act.setOutData("types", Integer.parseInt(types));
		
		//end
		
		//回运要入库的回运清单号
		String claim_id=request.getParamValue("CLAIM_ID");//此处为回运清单中的主键
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		try {
			//得到运费
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(claim_id));
			TtAsWrOldReturnedPO popoValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
			act.setOutData("price_con", popoValue.getAuthPrice());
			act.setOutData("price_con1", popoValue.getPrice());
			//获取回运清单信息bean  
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			//查询是否已经签收完成:签收数量不为空,抵扣原因不为空
			int isSure = dao.sureCount(claim_id);
			act.setOutData("isSure", isSure);
			System.out.println(isSure+",,,,,,,,,,,,,");
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			
			act.setOutData("returnListBean", returnListBean);
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			
			act.setForword(this.approveAndStoredPageUrl41);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void goApproveAndStoredPage8(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		
		//begin
		request=act.getRequest();
		String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));//
		String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//
		String back_order_no=CommonUtils.checkNull(request.getParamValue("back_order_no"));//
		String yieldly1=CommonUtils.checkNull(request.getParamValue("yieldly"));//
		String freight_type=CommonUtils.checkNull(request.getParamValue("freight_type"));//
		String create_start_date=CommonUtils.checkNull(request.getParamValue("create_start_date"));//
		String create_end_date=CommonUtils.checkNull(request.getParamValue("create_end_date"));//
		String report_start_date=CommonUtils.checkNull(request.getParamValue("report_start_date"));//
		String report_end_date=CommonUtils.checkNull(request.getParamValue("report_end_date"));//
		String back_type=CommonUtils.checkNull(request.getParamValue("back_type"));//
		String trans_no=CommonUtils.checkNull(request.getParamValue("trans_no"));//
		String isReturn=CommonUtils.checkNull(request.getParamValue("isReturn"));//
		act.setOutData("dealerCode", dealerCode);
		act.setOutData("dealerName", dealerName);
		act.setOutData("back_order_no", back_order_no);
		act.setOutData("yieldly1", yieldly1);
		act.setOutData("freight_type", freight_type);
		act.setOutData("create_start_date", create_start_date);
		act.setOutData("create_end_date", create_end_date);
		act.setOutData("report_start_date", report_start_date);
		act.setOutData("report_end_date", report_end_date);
		act.setOutData("back_type", back_type);
		act.setOutData("trans_no", trans_no);
		act.setOutData("isReturn", isReturn);
		//end
		
		//回运要入库的回运清单号
		String claim_id=request.getParamValue("CLAIM_ID");//此处为回运清单中的主键
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		try {
			//得到运费
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(claim_id));
			TtAsWrOldReturnedPO popoValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
			act.setOutData("price_con", popoValue.getAuthPrice());
			act.setOutData("price_con1", popoValue.getPrice());
			//获取回运清单信息bean  
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			
			act.setOutData("returnListBean", returnListBean);
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			
			act.setForword(this.approveAndStoredPageUrl4);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void goApproveAndStoredPage7(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		
		//begin
		request=act.getRequest();
		String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));//
		String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//
		String back_order_no=CommonUtils.checkNull(request.getParamValue("back_order_no"));//
		String yieldly1=CommonUtils.checkNull(request.getParamValue("yieldly"));//
		String freight_type=CommonUtils.checkNull(request.getParamValue("freight_type"));//
		String create_start_date=CommonUtils.checkNull(request.getParamValue("create_start_date"));//
		String create_end_date=CommonUtils.checkNull(request.getParamValue("create_end_date"));//
		String report_start_date=CommonUtils.checkNull(request.getParamValue("report_start_date"));//
		String report_end_date=CommonUtils.checkNull(request.getParamValue("report_end_date"));//
		String back_type=CommonUtils.checkNull(request.getParamValue("back_type"));//
		String trans_no=CommonUtils.checkNull(request.getParamValue("trans_no"));//
		act.setOutData("dealerCode", dealerCode);
		act.setOutData("dealerName", dealerName);
		act.setOutData("back_order_no", back_order_no);
		act.setOutData("yieldly1", yieldly1);
		act.setOutData("freight_type", freight_type);
		act.setOutData("create_start_date", create_start_date);
		act.setOutData("create_end_date", create_end_date);
		act.setOutData("report_start_date", report_start_date);
		act.setOutData("report_end_date", report_end_date);
		act.setOutData("back_type", back_type);
		act.setOutData("trans_no", trans_no);
		//end
		
		//回运要入库的回运清单号
		String claim_id=request.getParamValue("CLAIM_ID");//此处为回运清单中的主键
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		try {
			//得到运费
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(claim_id));
			TtAsWrOldReturnedPO popoValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
			act.setOutData("price_con", popoValue.getAuthPrice());
			act.setOutData("price_con1", popoValue.getPrice());
			//获取回运清单信息bean  
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			
			act.setOutData("returnListBean", returnListBean);
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			
			act.setForword(this.approveAndStoredPageUrl51);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-04-15
	public void goApproveAndStoredPage4(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		
		//回运要入库的回运清单号
		String claim_id=request.getParamValue("CLAIM_ID");//此处为回运清单中的主键
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		try {
			//得到运费
			TtAsWrAuthPricePO po11 = new TtAsWrAuthPricePO();
			po11.setReturnId(Long.parseLong(claim_id));
			List poCon = dao.select(po11);
			if(poCon.size()<=0 || poCon==null){
				TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
				popo.setId(Long.parseLong(claim_id));
				TtAsWrOldReturnedPO popoValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
				act.setOutData("price_con", popoValue.getPrice());
			}else{
				TtAsWrAuthPricePO poValue11 = (TtAsWrAuthPricePO)poCon.get(0);
				act.setOutData("price_con", poValue11.getNewPrice());
			}
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			
			act.setOutData("returnListBean", returnListBean);
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			
			act.setForword(this.approveAndStoredPageUrl4);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void goApproveAndStoredPage6(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		
		//begin
		request=act.getRequest();
		String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));//
		String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//
		String back_order_no=CommonUtils.checkNull(request.getParamValue("back_order_no"));//
		String yieldly1=CommonUtils.checkNull(request.getParamValue("yieldly"));//
		String freight_type=CommonUtils.checkNull(request.getParamValue("freight_type"));//
		String create_start_date=CommonUtils.checkNull(request.getParamValue("create_start_date"));//
		String create_end_date=CommonUtils.checkNull(request.getParamValue("create_end_date"));//
		String report_start_date=CommonUtils.checkNull(request.getParamValue("report_start_date"));//
		String report_end_date=CommonUtils.checkNull(request.getParamValue("report_end_date"));//
		String back_type=CommonUtils.checkNull(request.getParamValue("back_type"));//
		String trans_no=CommonUtils.checkNull(request.getParamValue("trans_no"));//
		act.setOutData("dealerCode", dealerCode);
		act.setOutData("dealerName", dealerName);
		act.setOutData("back_order_no", back_order_no);
		act.setOutData("yieldly1", yieldly1);
		act.setOutData("freight_type", freight_type);
		act.setOutData("create_start_date", create_start_date);
		act.setOutData("create_end_date", create_end_date);
		act.setOutData("report_start_date", report_start_date);
		act.setOutData("report_end_date", report_end_date);
		act.setOutData("back_type", back_type);
		act.setOutData("trans_no", trans_no);
		//end
		
		//回运要入库的回运清单号
		String claim_id=request.getParamValue("CLAIM_ID");//此处为回运清单中的主键
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		try {
			//得到运费
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(claim_id));
			TtAsWrOldReturnedPO popoValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
			act.setOutData("price_con", popoValue.getAuthPrice());
			act.setOutData("price_con1", popoValue.getPrice());
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			
			/*********旧件回运起止时间 begion**************/
			//List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			TrReturnLogisticsPO poL = new TrReturnLogisticsPO();
			poL.setLogictisId(Long.parseLong(claim_id));
			TrReturnLogisticsPO opLvalue =(TrReturnLogisticsPO)dao.select(poL).get(0);
			TtAsWrReturnedOrderPO poO = new TtAsWrReturnedOrderPO();
			poO.setId(opLvalue.getReturnId());
			TtAsWrReturnedOrderPO poOvalue = (TtAsWrReturnedOrderPO)dao.select(poO).get(0);
			act.setOutData("time", poOvalue.getWrStartDate());
			/**********旧件回运起止时间 end***************/
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			
			act.setOutData("returnListBean", returnListBean);
			act.setOutData("detailList", detailList);
			
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			
			act.setForword(this.approveAndStoredPageUrl4);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void goApproveAndStoredPage41(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		String claim_id=request.getParamValue("CLAIM_ID");//此处为物流单ID
		String types = request.getParamValue("types");
		String temp = request.getParamValue("temp");
		act.setOutData("types",Integer.valueOf(types));
		act.setOutData("temp", temp);
		
		params.put("i_claim_id", claim_id);
		try {
			ClaimBackListDao dao1=ClaimBackListDao.getInstance();
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			
			//获取回运清单明细信息List
			
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("claim_id", claim_id);
			act.setOutData("returnListBean", returnListBean);
			//act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
		
			
			
			String claimId=request.getParamValue("ORDER_ID");//获取回运清单的修改主键
			String oper=request.getParamValue("oper");//获取操作动作
			// 处理当前页
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			//根据回运主键查询索赔配件清单表信息
			StringBuffer sqlStr=new StringBuffer();
			sqlStr.append("select tawor.id,tawor.price,tawor.auth_price,tawor.price_remark,tawor.return_no,to_char(tawor.return_date,'YYYY-MM-DD') return_date,tawor.wr_amount,\n" );
			sqlStr.append("tawor.part_item_amount,tawor.part_amount,tawor.parkage_amount,tawor.arrive_date,\n" );
			sqlStr.append("tawor.return_type,tawor.tel,tccc.code_desc return_desc,tawor.status,tawor.remark,\n" );
			sqlStr.append("tc.code_desc status_desc,tawor.transport_type,tcc.code_desc transport_desc,\n" );
			sqlStr.append("to_char(tawor.create_date,'YYYY-MM-DD') create_date,tu.name creator,tawor.tran_no,TO_CHAR(tawor.SEND_TIME,'YYYY-MM-DD') send_date\n" );
			sqlStr.append("from tt_as_wr_returned_order tawor,tc_code tc,tc_code tcc,\n" );
			sqlStr.append("tc_code tccc,tc_user tu\n" );
			sqlStr.append("where tawor.status=tc.code_id(+) and tawor.transport_type=tcc.code_id(+)\n" );
			sqlStr.append("and tawor.create_by=tu.user_id(+) and tawor.return_type=tccc.code_id(+)\n" );
			sqlStr.append("and tawor.id="+claim_id);
			TtAsWrOldPartBackListDetailBean detailInfoBean=dao1.getClaimBackInfo(sqlStr.toString(),1,1);
			request.setAttribute("claimPartDetailBean", detailInfoBean);
			
			sqlStr.delete(0,sqlStr.length());
			//根据回运主键查询索赔配件明细表信息
			sqlStr.append("select taword.barcode_no,taword.id,to_char((select report_date from tt_as_wr_application_claim where id = taword.claim_id),'YYYY-MM-DD') as report_date,taword.claim_no,taword.vin,taword.part_id,tawp.old_part_code as part_code,\n");
			sqlStr.append("tawp.old_part_cname as part_name,taword.n_return_amount,taword.return_amount,\n");
			sqlStr.append("taword.box_no,taword.warehouse_region,");
			sqlStr.append("decode(taword.deduct_remark,null,'',tc.code_desc) deduct_remark,taword.other_remark,ba.area_name  proc_factory,taword.sign_amount\n");
			sqlStr.append("from tt_as_wr_returned_order_detail taword, tm_business_area ba,\n");
			sqlStr.append("tt_as_wr_app_part tawp,tc_code tc \n");
			sqlStr.append("where taword.claim_part_id=tawp.claim_part_id  \n");
			sqlStr.append("and taword.deduct_remark=tc.code_id(+)\n");
			sqlStr.append("and taword.proc_factory=ba.area_id(+)\n");
			sqlStr.append("and taword.return_id="+claim_id+"\n");
			sqlStr.append(" ORDER BY TAWORD.BOX_NO, TAWORD.CLAIM_NO, TAWORD.PART_NAME, TAWORD.BARCODE_NO\n");
			System.out.println("sql=="+sqlStr);
			List<TtAsWrOldPartDetailListBean> detailList = dao1.queryClaimBackDetailList(sqlStr.toString());
			act.setOutData("detailList", detailList);
			act.setForword(this.approveAndStoredPageUrl41);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//旧件签收入库前数据查询
	public void oldPartSignAudit() throws Exception{
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		String currp = request.getParamValue("curPage");
		String partCode = request.getParamValue("part_code");
		String partName = request.getParamValue("part_name");
		String yieldly = request.getParamValue("YIELDLY_TYPE");
		if(yieldly==null || "".equalsIgnoreCase(yieldly)){
			yieldly = request.getParamValue("yieldly");
		}
		if(Utility.testString(partName)){
			partName = new String(request.getParamValue("part_name").getBytes("ISO-8859-1"),"UTF-8");
		}
		String claim_id=request.getParamValue("CLAIM_ID");//此处为物流单ID
		String types = request.getParamValue("types");
		act.setOutData("types",Integer.valueOf(types));
		//回运要入库的物流单ID
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		params.put("partCode", partCode);
		params.put("partName", partName);
		try {
			//获取回运清单明细信息List
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			act.setOutData("returnListBean", returnListBean);
			act.setOutData("part_code", partCode);
			act.setOutData("part_name", partName);
			act.setOutData("yieldly", yieldly);
			List<Map<String, Object>> list = claimBackDao.getCode();
			act.setOutData("list", list);
			List<Map<String, Object>> deductList = dao.getDeductList();
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			act.setOutData("deductList", deductList);
			act.setOutData("num", act.getSession().get("num"));
			act.setOutData("dealerCodeSS", act.getSession().get("dealerCodeSS"));
			act.setForword(this.oldPartSignAudit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	//主管旧件签收入库前数据查询
	public void oldPartSignAuditsZg() throws Exception{
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		String partCode = request.getParamValue("part_code");
		String partName = request.getParamValue("part_name");
		String yieldly = request.getParamValue("YIELDLY_TYPE");
		if(yieldly==null || "".equalsIgnoreCase(yieldly)){
			yieldly = request.getParamValue("yieldly");
		}
		if(Utility.testString(partName)){
			partName = new String(request.getParamValue("part_name").getBytes("ISO-8859-1"),"UTF-8");
		}
		String claim_id=request.getParamValue("CLAIM_ID");//此处为物流单ID
		String types = request.getParamValue("types");
		act.setOutData("types",Integer.valueOf(types));
		//回运要入库的物流单ID
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		params.put("partCode", partCode);
		params.put("partName", partName);
		try {
			//获取回运清单明细信息List
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailListZg2(params);
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			act.setOutData("returnListBean", returnListBean);
			act.setOutData("part_code", partCode);
			act.setOutData("part_name", partName);
			act.setOutData("yieldly", yieldly);
			List<Map<String, Object>> list = claimBackDao.getCode();
			act.setOutData("list", list);
			List<Map<String, Object>> deductList = dao.getDeductList();
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			act.setOutData("deductList", deductList);
			act.setForword(this.oldPartSignAuditsZg);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	//旧件审核入库查询
	public void oldPartSignAuditSerch(){
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		try {
		Map<String,String> params=new HashMap<String, String>();
		String partCode = request.getParamValue("part_code");
		String partName = request.getParamValue("part_name");
		String yieldly = request.getParamValue("YIELDLY_TYPE");
		String bar_code = request.getParamValue("bar_code");
		String sing_num = request.getParamValue("sing_num");
		String is_import = request.getParamValue("is_import");
		String dealerName = request.getParamValue("dealerName");
		String dealerCode = request.getParamValue("dealerCode");
		String claim_no = request.getParamValue("claim_no");
		String inWarhouseStatus = request.getParamValue("inWarhouseStatus");
		String producerCode = request.getParamValue("producer_code");
		String producerName = request.getParamValue("producer_name");
		String vin = request.getParamValue("vin");
		if(yieldly==null || "".equalsIgnoreCase(yieldly)){
			yieldly = request.getParamValue("yieldly");
		}
		String claim_id=request.getParamValue("CLAIM_ID");//此处为物流单ID
		String types = request.getParamValue("types");
		act.setOutData("types",Integer.valueOf(types));
		//回运要入库的物流单ID
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		params.put("bar_code", bar_code);
		params.put("partCode", partCode);
		params.put("partName", partName);
		params.put("sing_num", sing_num);
		params.put("is_import", is_import);
		params.put("dealerName", dealerName);
		params.put("dealerCode", dealerCode);
		params.put("claim_no", claim_no);
		params.put("vin", vin);
		params.put("inWarhouseStatus", inWarhouseStatus);
		params.put("producerCode", producerCode);
		params.put("producerName", producerName);
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage"))
				: 1; // 处理当前页
				//由于在审核界面，直接点击审核时,没有保存页面上的库位，所以这里先将旧件明细表内的数据更新为系统默认库位。但是只更新旧件明细表库位为空的数据
				//页面在针对每一个旧件修改库位时异步修改单个旧件
//				StringBuffer sql = new StringBuffer();
//				sql.append("UPDATE tt_as_wr_old_returned_detail d\n");
//				sql.append("SET d.local_war_house=(SELECT b.local_war_house FROM tm_pt_part_base b WHERE b.part_code = d.part_code),\n");
//				sql.append("       d.local_war_shel=(SELECT b.local_war_shel FROM tm_pt_part_base b WHERE b.part_code = d.part_code),\n");
//				sql.append("       d.local_war_layer=(SELECT b.local_war_layer FROM tm_pt_part_base b WHERE b.part_code = d.part_code)\n");
//				sql.append("WHERE  (d.local_war_house IS NULL OR d.local_war_shel IS NULL OR d.local_war_layer IS NULL ) and d.return_id="+claim_id); 
//				dao.update(sql.toString(), null);
		
		//获取回运清单明细信息List
		PageResult<Map<String,Object>> detailList =dao.queryClaimBackDetailList3(params,10,curPage);
		act.setOutData("claim_id", claim_id);
		act.setOutData("boxNo", boxNo);
		act.setOutData("detailList", detailList);
		act.setOutData("part_code", partCode);
		act.setOutData("part_name", partName);
		act.setOutData("sing_num", sing_num);
		act.setOutData("is_import", is_import);
		act.setOutData("yieldly", yieldly);
		act.setOutData("pageNum", curPage);
		act.setOutData("ps", detailList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	//旧件审核入库查询
	public void oldPartSignAuditSerchZg(){
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		try {
		Map<String,String> params=new HashMap<String, String>();
		String partCode = request.getParamValue("part_code");
		String partName = request.getParamValue("part_name");
		String yieldly = request.getParamValue("YIELDLY_TYPE");
		String bar_code = request.getParamValue("bar_code");
		String sing_num = request.getParamValue("sing_num");
		String is_import = request.getParamValue("is_import");
		String dealerName = request.getParamValue("dealerName");
		String dealerCode = request.getParamValue("dealerCode");
		String claim_no = request.getParamValue("claim_no");
		String IS_IN_HOUSE = request.getParamValue("IS_IN_HOUSE");
		String vin = request.getParamValue("vin");
		if(yieldly==null || "".equalsIgnoreCase(yieldly)){
			yieldly = request.getParamValue("yieldly");
		}
		String claim_id=request.getParamValue("CLAIM_ID");//此处为物流单ID
		String types = request.getParamValue("types");
		act.setOutData("types",Integer.valueOf(types));
		//回运要入库的物流单ID
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		params.put("bar_code", bar_code);
		params.put("partCode", partCode);
		params.put("partName", partName);
		params.put("sing_num", sing_num);
		params.put("is_import", is_import);
		params.put("dealerName", dealerName);
		params.put("dealerCode", dealerCode);
		params.put("claim_no", claim_no);
		params.put("vin", vin);
		params.put("IS_IN_HOUSE", IS_IN_HOUSE);
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage"))
				: 1; // 处理当前页
				//由于在审核界面，直接点击审核时,没有保存页面上的库位，所以这里先将旧件明细表内的数据更新为系统默认库位。但是只更新旧件明细表库位为空的数据
				//页面在针对每一个旧件修改库位时异步修改单个旧件
				StringBuffer sql = new StringBuffer();
				sql.append("UPDATE tt_as_wr_old_returned_detail d\n");
				sql.append("SET d.local_war_house=(SELECT b.local_war_house FROM tm_pt_part_base b WHERE b.part_code = d.part_code),\n");
				sql.append("       d.local_war_shel=(SELECT b.local_war_shel FROM tm_pt_part_base b WHERE b.part_code = d.part_code),\n");
				sql.append("       d.local_war_layer=(SELECT b.local_war_layer FROM tm_pt_part_base b WHERE b.part_code = d.part_code)\n");
				sql.append("WHERE  (d.local_war_house IS NULL OR d.local_war_shel IS NULL OR d.local_war_layer IS NULL ) and d.return_id="+claim_id); 
				dao.update(sql.toString(), null);
		//获取回运清单明细信息List
		PageResult<Map<String,Object>> detailList =dao.queryClaimBackDetailList4(params,Constant.PAGE_SIZE_MIDDLE,curPage);
		act.setOutData("claim_id", claim_id);
		act.setOutData("boxNo", boxNo);
		act.setOutData("detailList", detailList);
		act.setOutData("part_code", partCode);
		act.setOutData("part_name", partName);
		act.setOutData("sing_num", sing_num);
		act.setOutData("is_import", is_import);
		act.setOutData("yieldly", yieldly);
		act.setOutData("pageNum", curPage);
		act.setOutData("ps", detailList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	//旧件签收明细
	public void oldPartSignQueryDetail(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		String claim_id=request.getParamValue("CLAIM_ID");//此处为物流单ID
		//回运要入库的物流单ID
		params.put("i_claim_id", claim_id);
		try {
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.oldPartSignQueryDetail(params);
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			act.setOutData("detailList1", detailList1);
			act.setOutData("claim_id", claim_id);
			act.setOutData("returnListBean", returnListBean);
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			act.setForword(this.oldPartSignQueryDetail);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到明细页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void goApproveAndStoredPage411(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		
		//begin
		String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));//
		String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//
		String back_order_no=CommonUtils.checkNull(request.getParamValue("back_order_no"));//
		String yieldly=CommonUtils.checkNull(request.getParamValue("yieldly"));//
		String freight_type=CommonUtils.checkNull(request.getParamValue("freight_type"));//
		String create_start_date=CommonUtils.checkNull(request.getParamValue("create_start_date"));//
		String create_end_date=CommonUtils.checkNull(request.getParamValue("create_end_date"));//
		String report_start_date=CommonUtils.checkNull(request.getParamValue("report_start_date"));//
		String report_end_date=CommonUtils.checkNull(request.getParamValue("report_end_date"));//
		String back_type=CommonUtils.checkNull(request.getParamValue("back_type"));//
		String trans_no=CommonUtils.checkNull(request.getParamValue("trans_no"));//
		String isReturn =CommonUtils.checkNull(request.getParamValue("isReturn"));//
		act.setOutData("dealerCode", dealerCode);
		act.setOutData("dealerName", dealerName);
		act.setOutData("back_order_no", back_order_no);
		act.setOutData("yieldly", yieldly);
		act.setOutData("freight_type", freight_type);
		act.setOutData("create_start_date", create_start_date);
		act.setOutData("create_end_date", create_end_date);
		act.setOutData("report_start_date", report_start_date);
		act.setOutData("report_end_date", report_end_date);
		act.setOutData("back_type", back_type);
		act.setOutData("trans_no", trans_no);
		//end
		
		//回运要入库的物流单ID
		String claim_id=request.getParamValue("CLAIM_ID");//此处为物流单ID
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		try {
			//得到运费
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(claim_id));
			TtAsWrOldReturnedPO popoValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
			act.setOutData("price_con", popoValue.getAuthPrice());
			act.setOutData("price_con1", popoValue.getPrice());
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			
			List<Map<String, Object>> listBoxNo1 =claimBackDao.getBoxNo1(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("listBoxNo1", listBoxNo1);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			
			act.setOutData("returnListBean", returnListBean);
			if(isReturn==""){
				TcCodePO code = new TcCodePO() ;
				code.setType("8008") ;
				List list = dao.select(code) ;
				if(list.size()>0){
					code = (TcCodePO)list.get(0);
				}
				if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
					detailList=null;
				}
			}
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			
			act.setForword(this.approveAndStoredPageUrl411);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void goApproveAndStoredPage412(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		
		//begin
		String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));//
		String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//
		String back_order_no=CommonUtils.checkNull(request.getParamValue("back_order_no"));//
		String yieldly=CommonUtils.checkNull(request.getParamValue("yieldly"));//
		String freight_type=CommonUtils.checkNull(request.getParamValue("freight_type"));//
		String create_start_date=CommonUtils.checkNull(request.getParamValue("create_start_date"));//
		String create_end_date=CommonUtils.checkNull(request.getParamValue("create_end_date"));//
		String report_start_date=CommonUtils.checkNull(request.getParamValue("report_start_date"));//
		String report_end_date=CommonUtils.checkNull(request.getParamValue("report_end_date"));//
		String back_type=CommonUtils.checkNull(request.getParamValue("back_type"));//
		String trans_no=CommonUtils.checkNull(request.getParamValue("trans_no"));//
		String isReturn =CommonUtils.checkNull(request.getParamValue("isReturn"));//
		act.setOutData("dealerCode", dealerCode);
		act.setOutData("dealerName", dealerName);
		act.setOutData("back_order_no", back_order_no);
		act.setOutData("yieldly", yieldly);
		act.setOutData("freight_type", freight_type);
		act.setOutData("create_start_date", create_start_date);
		act.setOutData("create_end_date", create_end_date);
		act.setOutData("report_start_date", report_start_date);
		act.setOutData("report_end_date", report_end_date);
		act.setOutData("back_type", back_type);
		act.setOutData("trans_no", trans_no);
		//end
		
		//回运要入库的物流单ID
		String claim_id=request.getParamValue("CLAIM_ID");//此处为物流单ID
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		try {
			//得到运费
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(claim_id));
			TtAsWrOldReturnedPO popoValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
			act.setOutData("price_con", popoValue.getAuthPrice());
			act.setOutData("price_con1", popoValue.getPrice());
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			
			List<Map<String, Object>> listBoxNo1 =claimBackDao.getBoxNo1(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("listBoxNo1", listBoxNo1);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			
			act.setOutData("returnListBean", returnListBean);
			if(isReturn==""){
				TcCodePO code = new TcCodePO() ;
				code.setType("8008") ;
				List list = dao.select(code) ;
				if(list.size()>0){
					code = (TcCodePO)list.get(0);
				}
				if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
					detailList=null;
				}
			}
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			
			act.setForword(this.approveAndStoredPageUrl412);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void goApproveAndStoredPage51(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		
		//begin
		request=act.getRequest();
		String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));//
		String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//
		String back_order_no=CommonUtils.checkNull(request.getParamValue("back_order_no"));//
		String yieldly1=CommonUtils.checkNull(request.getParamValue("yieldly"));//
		String freight_type=CommonUtils.checkNull(request.getParamValue("freight_type"));//
		String create_start_date=CommonUtils.checkNull(request.getParamValue("create_start_date"));//
		String create_end_date=CommonUtils.checkNull(request.getParamValue("create_end_date"));//
		String report_start_date=CommonUtils.checkNull(request.getParamValue("report_start_date"));//
		String report_end_date=CommonUtils.checkNull(request.getParamValue("report_end_date"));//
		String back_type=CommonUtils.checkNull(request.getParamValue("back_type"));//
		String trans_no=CommonUtils.checkNull(request.getParamValue("trans_no"));//
		act.setOutData("dealerCode", dealerCode);
		act.setOutData("dealerName", dealerName);
		act.setOutData("back_order_no", back_order_no);
		act.setOutData("yieldly1", yieldly1);
		act.setOutData("freight_type", freight_type);
		act.setOutData("create_start_date", create_start_date);
		act.setOutData("create_end_date", create_end_date);
		act.setOutData("report_start_date", report_start_date);
		act.setOutData("report_end_date", report_end_date);
		act.setOutData("back_type", back_type);
		act.setOutData("trans_no", trans_no);
		//end
		
		//回运要入库的物流单ID
		String claim_id=request.getParamValue("CLAIM_ID");//此处为物流单ID
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		try {
			//得到运费
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(claim_id));
			TtAsWrOldReturnedPO popoValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
			act.setOutData("price_con", popoValue.getAuthPrice());
			act.setOutData("price_con1", popoValue.getPrice());
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			
			act.setOutData("returnListBean", returnListBean);
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			
			act.setForword(this.approveAndStoredPageUrl51);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void goApproveAndStoredPage22(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		
		String id=request.getParamValue("id");
		String boxNo=request.getParamValue("boxNo");
		params.put("id", id);
		params.put("boxNo", boxNo);
		try {
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList22(params);
			act.setOutData("detailList", detailList);
			act.setOutData("ok", "ok");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "aaaaaaaaaaa");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//判断旧件开始时间不能等于旧件审核日期的话，就不能操作
	public void goApproveAndStoredPage3(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		/*******Iverson add By 根据回运物流单Id更新签收旧件时间********************/
		String id = request.getParamValue("id");
		List list = dao.queryMinDate(id);
		Map map=(Map)list.get(0);
		String oldReviewDate = map.get("WR_START_DATE").toString();//签收时间
		String yieldly = map.get("YIELDLY").toString();//基地
		String dealerId = map.get("DEALER_ID").toString();
		oldReviewDate = oldReviewDate.substring(0,10);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		TtAsDealerTypePO po = new TtAsDealerTypePO();
		po.setDealerId(Long.parseLong(dealerId));
		po.setYieldly(yieldly);
		TtAsDealerTypePO time = (TtAsDealerTypePO)dao.select(po).get(0);
		Date time1 =time.getOldReviewDate();
		try {
			Calendar calendar = Calendar.getInstance();//公用类，加年月日的
			calendar.setTime(time1);
			calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
			time1 = calendar.getTime();//得到加一天后的值
			if(time1.equals(sdf.parse(oldReviewDate))){
				act.setOutData("ok", "ok");
				act.setOutData("id", id);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		/****************************/
	}
	
	
	public void goApproveAndStoredPage31(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		String id = request.getParamValue("id");
		List list = dao.queryMinDate(id);
		Map map=(Map)list.get(0);
		String oldReviewDate = map.get("WR_START_DATE").toString();//签收时间
		String yieldly = map.get("YIELDLY").toString();//基地
		String dealerId = map.get("DEALER_ID").toString();
		oldReviewDate = oldReviewDate.substring(0,10);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		TtAsDealerTypePO po = new TtAsDealerTypePO();
		po.setDealerId(Long.parseLong(dealerId));
		po.setYieldly(yieldly);
		TtAsDealerTypePO time = (TtAsDealerTypePO)dao.select(po).get(0);
		Date time1 =time.getOldReviewDate();
		try {
			Calendar calendar = Calendar.getInstance();//公用类，加年月日的
			calendar.setTime(time1);
			calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
			time1 = calendar.getTime();//得到加一天后的值
			if(time1.equals(sdf.parse(oldReviewDate))){
				act.setOutData("ok", "ok");
				act.setOutData("id", id);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	public void goApproveAndStoredPage311(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		String id = request.getParamValue("id");
		List list = dao.queryMinDate(id);
		Map map=(Map)list.get(0);
		String oldReviewDate = map.get("WR_START_DATE").toString();//签收时间
		String yieldly = map.get("YIELDLY").toString();//基地
		String dealerId = map.get("DEALER_ID").toString();
		oldReviewDate = oldReviewDate.substring(0,10);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		TtAsDealerTypePO po = new TtAsDealerTypePO();
		po.setDealerId(Long.parseLong(dealerId));
		po.setYieldly(yieldly);
		TtAsDealerTypePO time = (TtAsDealerTypePO)dao.select(po).get(0);
		Date time1 =time.getOldReviewDate();
		try {
			Calendar calendar = Calendar.getInstance();//公用类，加年月日的
			calendar.setTime(time1);
			calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
			time1 = calendar.getTime();//得到加一天后的值
			if(time1.equals(sdf.parse(oldReviewDate))){
				act.setOutData("ok", "ok");
				act.setOutData("id", id);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Function：索赔件审批入库--签收操作
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-13 赵伦达
	 */
	public void approveAndStored(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		int updateSuccessNum=0;
		// 处理当前页
		
				
		
		String idStr=request.getParamValue("idStr");//被签收的id串
		
		try {
			params.put("idStr", idStr);
			params.put("return_ord_id", request.getParamValue("return_ord_id"));
			params.put("logonId",String.valueOf(loginUser.getUserId()));
			//取出签收id的参数
			String[] strArr=idStr.split(",");
			for(int count=0;count<strArr.length;count++){
				params.put("signNum"+strArr[count], request.getParamValue("signNum"+strArr[count]));
				params.put("wrHouse"+strArr[count], request.getParamValue("wrHouse"+strArr[count]));
				params.put("deduct"+strArr[count], request.getParamValue("deduct"+strArr[count]));
			}
			params.put("company_id",String.valueOf(companyId));
			updateSuccessNum=dao.SignBackOldPartOper(params);
			if(updateSuccessNum==strArr.length){
				
				//获取回运清单明细信息List
				PageResult<TtAsWrOldPartSignDetailListBean> ps=dao.queryClaimBackDetailList(params,getCurrPage(),
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
				act.setOutData("updateResult","updateSuccess");
			}else{
				act.setOutData("updateResult","updateFailure");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--签收操作");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function：索赔件审批入库--签收操作(批量全部签收)，没有部分签收过程
	 * 原功能连接：approveAndStored
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-09-07 XZM ADD 
	 * 2010-10-13 修改
	 * 描述： 
	 *     库存表中加入产地记录，配件需要按产地区分开来
	 * 影响：
	 *     ClaimApproveOldPartStoredDao.SignFinishOper();
	 */
	public void approveAndStored2(){
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		int updateSuccessNum=0;
		String return_ord_id = request.getParamValue("return_ord_id");
		String boxNo = request.getParamValue("boxNo");
		try {
			params.put("return_ord_id", return_ord_id);
			params.put("logonId",String.valueOf(loginUser.getUserId()));
			
			String[] strArr= request.getParamValues("orderIds");
			int updateNum = 0;
			for(int count=0;count<strArr.length;count++){
				String signNum=ClaimTools.dealParamStr(request.getParamValue("signNum"+strArr[count]));//签收数
				String wrHouseNo=ClaimTools.dealParamStr(request.getParamValue("wrHouse"+strArr[count]));//库区
				String deduct=ClaimTools.dealParamStr(request.getParamValue("deduct"+strArr[count]));//抵扣原因
				
				TtAsWrOldReturnedDetailPO idObj=new TtAsWrOldReturnedDetailPO();
				idObj.setId(Long.parseLong(strArr[count]));
				
				TtAsWrOldReturnedDetailPO vo=new TtAsWrOldReturnedDetailPO();
				vo.setSignAmount(Integer.parseInt(signNum));
				vo.setWarehouseRegion(wrHouseNo);
				vo.setIsSign(1L);//是否签收的标志
				if(!"".equals(deduct)){
					vo.setDeductRemark(Integer.parseInt(deduct));
				}else{
					vo.setDeductRemark(0);
				}
				
				updateNum+=dao.update(idObj,vo);
			}
			params.put("company_id",String.valueOf(companyId));
			//签收回运单，修改回运单明细
			updateSuccessNum=updateNum;
			
			if(updateSuccessNum==strArr.length){
				params.put("i_claim_id", return_ord_id);
				//修改回运单状态（删除部分签收过程）
				TtAsWrOldReturnedPO idObj=new TtAsWrOldReturnedPO();
				idObj.setId(Long.parseLong(return_ord_id));
				TtAsWrOldReturnedPO vo=new TtAsWrOldReturnedPO();
				int count = dao.queryIsSign(Long.parseLong(return_ord_id));//判断是否已经签收完了。
				if(count==0){
					vo.setStatus(Constant.BACK_LIST_STATUS_04);//全部签收
					/*******Inerson add By 根据回运物流单Id更新签收旧件时间********************/
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					List list = dao.queryReturnOrder(return_ord_id);
					for(int i=0;i<list.size();i++){
						Map map=(Map)list.get(i);
						TtAsDealerTypePO po = new TtAsDealerTypePO();
						po.setDealerId(((BigDecimal)map.get("DEALER_ID")).longValue());//经销商ID
						po.setYieldly(map.get("YIELDLY").toString());//基地
						TtAsDealerTypePO poValue = new TtAsDealerTypePO();
						String oldReviewDate = map.get("WR_START_DATE").toString();//签收时间
						oldReviewDate = oldReviewDate.substring(11,oldReviewDate.length());
						TtAsDealerTypePO time = (TtAsDealerTypePO)dao.select(po).get(0);
						Date oldReviewTime = time.getOldReviewDate();//查询本来的时间
						if(sdf.parse(oldReviewDate).after(oldReviewTime) || sdf.parse(oldReviewDate).equals(oldReviewTime)){//比较如果页面的时间在本来的时间之前那么就更新
							poValue.setOldReviewDate(sdf.parse(oldReviewDate));//跟新时间
						}else{
							poValue.setOldReviewDate(oldReviewTime);//跟新时间
						}
						dao.update(po, poValue);
						
						//把回运单的状态改为已签收
						TtAsWrReturnedOrderPO poReturn = new TtAsWrReturnedOrderPO();
						poReturn.setId(((BigDecimal)map.get("ID")).longValue());
						TtAsWrReturnedOrderPO poReturnValue = new TtAsWrReturnedOrderPO();
						poReturnValue.setStatus(Constant.RETURNORDER_TYPE_04);
						poReturnValue.setUpdateBy(loginUser.getUserId());
						poReturnValue.setUpdateDate(new Date());
						dao.update(poReturn, poReturnValue);
					}
					/*******Inerson add By 根据回运单Id更新签收旧件时间********************/
					
					//记录审核人，审核时间信息 Iverson 2010-01-04
					ReturnStatusRecord.recordStatus(Long.parseLong(return_ord_id), loginUser.getUserId(), loginUser.getName(),loginUser.getOrgId(), Constant.BACK_LIST_STATUS_04,boxNo);
				}else{
					vo.setStatus(Constant.BACK_LIST_STATUS_03);//部分签收
					//把回运单的状态改为已签收
					/*List list = dao.queryReturnOrder(return_ord_id);
					for(int i=0;i<list.size();i++){
						Map map=(Map)list.get(i);
						TtAsWrReturnedOrderPO poReturn = new TtAsWrReturnedOrderPO();
						poReturn.setId(((BigDecimal)map.get("ID")).longValue());
						TtAsWrReturnedOrderPO poReturnValue = new TtAsWrReturnedOrderPO();
						poReturnValue.setStatus(Constant.RETURNORDER_TYPE_04);
						poReturnValue.setUpdateBy(loginUser.getUserId());
						poReturnValue.setUpdateDate(new Date());
						dao.update(poReturn, poReturnValue);
					}*/
					
					//记录审核人，审核时间信息 Iverson 2010-01-04
					ReturnStatusRecord.recordStatus(Long.parseLong(return_ord_id), loginUser.getUserId(), loginUser.getName(),loginUser.getOrgId(), Constant.BACK_LIST_STATUS_03,boxNo);
				}
				vo.setUpdateBy(loginUser.getUserId());
				vo.setUpdateDate(new Date());
				dao.update(idObj,vo);
				//获取回运清单明细信息List
				//List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
				//act.setOutData("detailList", detailList);
				//将签收入库的件入库管理（库存）
				Map<String,String> paramsMap=new HashMap<String, String>();
				paramsMap.put("id", return_ord_id);//回运清单ID
				paramsMap.put("logonId", loginUser.getUserId().toString());//登陆用户ID
				paramsMap.put("company_id", companyId.toString());//登陆用户所属公司ID
				dao.SignFinishOper(paramsMap);
				act.setOutData("updateResult","updateSuccess");
			}else{
				act.setOutData("updateResult","updateFailure");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--签收操作");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-04-15
	public void approveAndStored22(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		int updateSuccessNum=0;
		String return_ord_id = request.getParamValue("return_ord_id");
		String boxNo = request.getParamValue("boxNo");
		try {
			params.put("return_ord_id", return_ord_id);
			params.put("logonId",String.valueOf(loginUser.getUserId()));
			
			String[] strArr= request.getParamValues("orderIds");
			int updateNum = 0;
			for(int count=0;count<strArr.length;count++){
				String signNum=ClaimTools.dealParamStr(request.getParamValue("signNum"+strArr[count]));//签收数
				String wrHouseNo=ClaimTools.dealParamStr(request.getParamValue("wrHouse"+strArr[count]));//库区
				String deduct=ClaimTools.dealParamStr(request.getParamValue("deduct"+strArr[count]));//抵扣原因
				
				TtAsWrOldReturnedDetailPO idObj=new TtAsWrOldReturnedDetailPO();
				idObj.setId(Long.parseLong(strArr[count]));
				
				TtAsWrOldReturnedDetailPO vo=new TtAsWrOldReturnedDetailPO();
				vo.setSignAmount(Integer.parseInt(signNum));
				vo.setWarehouseRegion(wrHouseNo);
				vo.setIsSign(1L);//是否签收的标志
				if(!"".equals(deduct)){
					vo.setDeductRemark(Integer.parseInt(deduct));
				}else{
					vo.setDeductRemark(0);
				}
				
				updateNum+=dao.update(idObj,vo);
			}
			params.put("company_id",String.valueOf(companyId));
			//签收回运单，修改回运单明细
			updateSuccessNum=updateNum;
			
			if(updateSuccessNum==strArr.length){
				params.put("i_claim_id", return_ord_id);
				//修改回运单状态（删除部分签收过程）
				TtAsWrOldReturnedPO idObj=new TtAsWrOldReturnedPO();
				idObj.setId(Long.parseLong(return_ord_id));
				TtAsWrOldReturnedPO vo=new TtAsWrOldReturnedPO();
				int count = dao.queryIsSign(Long.parseLong(return_ord_id));//判断是否已经签收完了。
				if(count==0){
					vo.setStatus(Constant.BACK_LIST_STATUS_04);//全部签收
					/*******Inerson add By 根据回运物流单Id更新签收旧件时间********************/
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					List list = dao.queryReturnOrder(return_ord_id);
					for(int i=0;i<list.size();i++){
						Map map=(Map)list.get(i);
						TtAsDealerTypePO po = new TtAsDealerTypePO();
						po.setDealerId(((BigDecimal)map.get("DEALER_ID")).longValue());//经销商ID
						po.setYieldly(map.get("YIELDLY").toString());//基地
						TtAsDealerTypePO poValue = new TtAsDealerTypePO();
						String oldReviewDate = map.get("WR_START_DATE").toString();//签收时间
						oldReviewDate = oldReviewDate.substring(11,oldReviewDate.length());
						TtAsDealerTypePO time = (TtAsDealerTypePO)dao.select(po).get(0);
						Date oldReviewTime = time.getOldReviewDate();//查询本来的时间
						if(sdf.parse(oldReviewDate).after(oldReviewTime) || sdf.parse(oldReviewDate).equals(oldReviewTime)){//比较如果页面的时间在本来的时间之前那么就更新
							poValue.setOldReviewDate(sdf.parse(oldReviewDate));//跟新时间
						}else{
							poValue.setOldReviewDate(oldReviewTime);//跟新时间
						}
						dao.update(po, poValue);
						
						//把回运单的状态改为已签收
						TtAsWrReturnedOrderPO poReturn = new TtAsWrReturnedOrderPO();
						poReturn.setId(((BigDecimal)map.get("ID")).longValue());
						TtAsWrReturnedOrderPO poReturnValue = new TtAsWrReturnedOrderPO();
						poReturnValue.setStatus(Constant.RETURNORDER_TYPE_04);
						poReturnValue.setUpdateBy(loginUser.getUserId());
						poReturnValue.setUpdateDate(new Date());
						dao.update(poReturn, poReturnValue);
					}
					/*******Inerson add By 根据回运单Id更新签收旧件时间********************/
					
					//记录审核人，审核时间信息 Iverson 2010-01-04
					ReturnStatusRecord.recordStatus(Long.parseLong(return_ord_id), loginUser.getUserId(), loginUser.getName(),loginUser.getOrgId(), Constant.BACK_LIST_STATUS_04,boxNo);
				}else{
					vo.setStatus(Constant.BACK_LIST_STATUS_03);//部分签收
					
					//记录审核人，审核时间信息 Iverson 2010-01-04
					ReturnStatusRecord.recordStatus(Long.parseLong(return_ord_id), loginUser.getUserId(), loginUser.getName(),loginUser.getOrgId(), Constant.BACK_LIST_STATUS_03,boxNo);
				}
				vo.setUpdateBy(loginUser.getUserId());
				vo.setUpdateDate(new Date());
				dao.update(idObj,vo);
				//将签收入库的件入库管理（库存）
				Map<String,String> paramsMap=new HashMap<String, String>();
				paramsMap.put("id", return_ord_id);//回运清单ID
				paramsMap.put("logonId", loginUser.getUserId().toString());//登陆用户ID
				paramsMap.put("company_id", companyId.toString());//登陆用户所属公司ID
				dao.SignFinishOper(paramsMap);
				
				//记录运费信息
				String claimId = request.getParamValue("claimId");
				TtAsWrAuthPricePO po11 = new TtAsWrAuthPricePO();
				po11.setReturnId(Long.parseLong(claimId));
				List poCon = dao.select(po11);
				if(poCon.size()<=0 || poCon==null){
					TtAsWrAuthPricePO po = new TtAsWrAuthPricePO();
					po.setId(Long.parseLong(SequenceManager.getSequence("")));
					po.setReturnId(Long.parseLong(claimId));
					TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
					popo.setId(Long.parseLong(claimId));
					TtAsWrOldReturnedPO poValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
					po.setYielyld(poValue.getYieldly());
					po.setOldPrice(Double.parseDouble(poValue.getPrice().toString()));
					po.setNewPrice(Double.parseDouble(request.getParamValue("price")));
					po.setDealerId(poValue.getDealerId());
					TmDealerPO tpo = new TmDealerPO();
					tpo.setDealerId(poValue.getDealerId());
					TmDealerPO tpoValue = (TmDealerPO)dao.select(tpo).get(0);
					po.setDealerCode(tpoValue.getDealerCode());
					po.setDealerName(tpoValue.getDealerName());
					po.setStatus(Constant.OLD_PRICE_1);
					po.setClaimbalanceId(-1L);
					po.setCreateBy(loginUser.getUserId());
					po.setCreateDate(new Date());
					dao.insert(po);
				}
				
				act.setOutData("updateResult","updateSuccess");
			}else{
				act.setOutData("updateResult","updateFailure");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--签收操作");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void approveAndStored222(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		String return_ord_id = request.getParamValue("return_ord_id");
		String boxNo = request.getParamValue("boxNo");
		try {
			params.put("return_ord_id", return_ord_id);
			params.put("logonId",String.valueOf(loginUser.getUserId()));
			params.put("company_id",String.valueOf(companyId));
			params.put("i_claim_id", return_ord_id);
			//修改回运单状态
			TtAsWrOldReturnedPO idObj=new TtAsWrOldReturnedPO();
			idObj.setId(Long.parseLong(return_ord_id));
			TtAsWrOldReturnedPO vo=new TtAsWrOldReturnedPO();
			vo.setStatus(Constant.BACK_LIST_STATUS_04);
			vo.setUpdateBy(loginUser.getUserId());
			vo.setUpdateDate(new Date());
			dao.update(idObj, vo);
			//记录审核人，审核时间信息 Iverson 2010-01-04
			TtAsWrReturnAuthitemPO reA = new TtAsWrReturnAuthitemPO();
			reA.setReturnId(Long.parseLong(return_ord_id));
			reA.setAuthStatus(Constant.BACK_LIST_STATUS_04);
			List list = dao.select(reA);
			if(list.size()>0){
				System.out.println("有入库记录!");
			}else{
				ReturnStatusRecord.recordStatus(Long.parseLong(return_ord_id), loginUser.getUserId(), loginUser.getName(),loginUser.getOrgId(), Constant.BACK_LIST_STATUS_04,boxNo);
			}
			//将签收入库的件入库管理（库存）
			Map<String,String> paramsMap=new HashMap<String, String>();
			paramsMap.put("id", return_ord_id);//回运清单ID
			paramsMap.put("logonId", loginUser.getUserId().toString());//登陆用户ID
			paramsMap.put("company_id", companyId.toString());//登陆用户所属公司ID
			dao.SignFinishOper(paramsMap);
			//记录运费信息
			//String claimId = request.getParamValue("claimId");
			TtAsWrAuthPricePO po11 = new TtAsWrAuthPricePO();
			po11.setReturnId(Long.parseLong(return_ord_id));
			List poCon = dao.select(po11);
			if(poCon.size()<=0 || poCon==null){
				TtAsWrAuthPricePO po = new TtAsWrAuthPricePO();
				po.setId(Long.parseLong(SequenceManager.getSequence("")));
				po.setReturnId(Long.parseLong(return_ord_id));
				
				TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
				popo.setId(Long.parseLong(return_ord_id));
				TtAsWrOldReturnedPO poValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
				po.setYielyld(poValue.getYieldly());
				po.setOldPrice(Double.parseDouble(request.getParamValue("price1")));
				po.setNewPrice(Double.parseDouble(request.getParamValue("price")));
				po.setDealerId(poValue.getDealerId());
				TmDealerPO tpo = new TmDealerPO();
				tpo.setDealerId(poValue.getDealerId());
				TmDealerPO tpoValue = (TmDealerPO)dao.select(tpo).get(0);
				po.setDealerCode(tpoValue.getDealerCode());
				po.setDealerName(tpoValue.getDealerName());
				po.setStatus(Constant.OLD_PRICE_1);
				po.setClaimbalanceId(-1L);
				po.setCreateBy(loginUser.getUserId());
				po.setCreateDate(new Date());
				dao.insert(po);
			}
			act.setOutData("updateResult","updateSuccess");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--签收操作");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 旧件部分审核
	 * @author KFQ
	 * @serialData 2014-7-30 14:04
	 */
	public void oldPartSignAuditIn(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		
		String return_ord_id = request.getParamValue("return_id");
		String type = request.getParamValue("type");
		String IN_WARHOUSE_NAME = request.getParamValue("IN_WARHOUSE_NAME");
		
		String boxNo = request.getParamValue("boxNo");
		boolean flag = true;
		String notice="";
		try {
			/**
			 * 修改供应商备注
			 */
			 dao.updateoldPartSupply(request,loginUser);
			
			/**
			 * 修改入库明细 
			 */
			String[] ids= request.getParamValues("checkId"); 
			if(ids != null && ids.length >0)
			{
				for(String id : ids)
				{ 
					List<Map<String,Object>> ps=dao.oldPartSignAuditIn(id);
					if(ps.size()>0){
						flag = false;
							notice+="索赔单号：【"+ ps.get(0).get("CLAIM_NO").toString()+"】\n";
					}
					if(!flag){
						notice+= " 签收数为0,但是没有选择扣除原因,请修改!";
					}else{
						dao.oldPartSignAuditInUpdate(id);
					}
				}
			}
			/**
			 * 如果所有选择的编号判断无误
			 */
			if(flag){
				//修改物流单状态
				TtAsWrOldReturnedPO idObj=new TtAsWrOldReturnedPO();
				idObj.setId(Long.parseLong(return_ord_id));
				TtAsWrOldReturnedPO vo=new TtAsWrOldReturnedPO();
				vo.setStatus(Constant.BACK_LIST_STATUS_04);
				vo.setUpdateBy(loginUser.getUserId());
				vo.setUpdateDate(new Date());
				vo.setInWarhouseName(IN_WARHOUSE_NAME);
				//部分审核下增加审核时间和审核人
				//wizard_lee
				vo.setInWarhouseDate(new Date());
				vo.setInWarhouseBy(loginUser.getUserId());
				vo.setInWarhouseName(loginUser.getName());
				dao.update(idObj,vo);
				//查询是否已经入库完成并且扣减原因已经填写完成
			      StringBuffer sql= new StringBuffer();
			      sql.append("update TT_AS_WR_OLD_RETURNED_DETAIL a\n" );
			      sql.append("   set a.EXECUTIVE_DIRECTOR_STA = 0\n" );
			      sql.append(" where a.SIGN_AMOUNT=a.RETURN_AMOUNT and  return_id =" + return_ord_id);
			      dao.update(sql.toString(), null );
		      
			      //=========更新切换件标示
			      dao.updateDetailQHJData(return_ord_id,loginUser);
			      //=============
				if("1".equals(type)){//如果没有问题的数据,并且已经如来完成了。
						String sqll = "update tt_as_wr_application a set a.is_old_audit =1 where a.id in (SELECT d.claim_id FROM Tt_As_Wr_Old_Returned_Detail d WHERE d.return_id="+return_ord_id+" GROUP BY d.claim_id) ";
						vo.setStatus(Constant.BACK_LIST_STATUS_05);//同时将单子状态变为已经审核
						vo.setUpdateBy(loginUser.getUserId());
						vo.setInWarhouseDate(new Date());
						vo.setUpdateDate(new Date());
						vo.setAuditNo(Integer.parseInt(getAuditNo()));
						vo.setInWarhouseBy(loginUser.getUserId());
						vo.setInWarhouseName(loginUser.getName());
						dao.update(sqll, null);
				}
				dao.update(idObj,vo);
				//记录审核记录
				ReturnStatusRecord.recordStatus(Long.parseLong(return_ord_id), loginUser.getUserId(), loginUser.getName(),loginUser.getOrgId(), Constant.BACK_LIST_STATUS_04,boxNo);
				//========================
				/**
				 * 加入索赔单数据更新的操作 zyw 2014-8-18
				 */
				//int res = dao.updateApplicationSal(return_ord_id);
				//logger.info("旧件审核时更新数据的结果:"+res);
				//========================
			}
		act.setOutData("updateResult","updateSuccess");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--签收操作");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	 * @author KFQ
	 * @serialData 2014-7-30 14:04
	 */
	public void oldPartSignAuditInZg(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		String boxNo = request.getParamValue("boxNo");
		String return_ord_id = request.getParamValue("return_id");
		String type = request.getParamValue("type");
		String[] Executive_director_ram = request.getParamValues("Executive_director_ram");
		String[] ids= request.getParamValues("checkId"); 
		try {
			
				for(int i = 0 ;i < ids.length; i++)
				{
					TtAsWrOldReturnedDetailPO detailPO = new TtAsWrOldReturnedDetailPO();
					TtAsWrOldReturnedDetailPO detailPO1 = new TtAsWrOldReturnedDetailPO();
					detailPO.setId(Long.parseLong(ids[i]));
					detailPO1.setExecutiveDirectorDate(new Date());
					detailPO1.setExecutiveDirectorRam(Executive_director_ram[i]);
					if(type.equals("1"))
					{
						//同意审核
						detailPO1.setIsInHouse(10041001);
						detailPO1.setExecutiveDirectorSta(1);
						//判断该件对应的单据是否是本次签收的最后一个配件单据
						TtAsWrOldReturnedDetailPO dd= (TtAsWrOldReturnedDetailPO) dao.select(detailPO).get(0);
						List<Map<String,Object>> ps=dao.oldPartSignAuditIn2(return_ord_id,dd.getClaimNo(),dd.getPartId());
						if(ps==null|| ps.size()<1){//主管审核通过的最后一个件更新整张单据为已经审核
							TtAsWrOldReturnedPO vo=new TtAsWrOldReturnedPO();
							//vo.setInWarhouseName(IN_WARHOUSE_NAME);
							vo.setInWarhouseDate(new Date());
							vo.setInWarhouseBy(loginUser.getUserId());
							String sqll = "update tt_as_wr_application a set a.is_old_audit =1 where a.id in (SELECT d.claim_id FROM Tt_As_Wr_Old_Returned_Detail d WHERE d.return_id="+return_ord_id+" GROUP BY d.claim_id) ";
							vo.setStatus(Constant.BACK_LIST_STATUS_05);//同时将单子状态变为已经审核
							vo.setUpdateBy(loginUser.getUserId());
							vo.setInWarhouseDate(new Date());
							vo.setUpdateDate(new Date());
							vo.setAuditNo(Integer.parseInt(getAuditNo()));
							vo.setInWarhouseBy(loginUser.getUserId());
							dao.update(sqll, null);//更新明细表数据状态
							TtAsWrOldReturnedPO idObj=new TtAsWrOldReturnedPO();
							idObj.setId(Long.parseLong(return_ord_id));
							dao.update(idObj,vo);//更新主表状态
							//记录审核记录
							ReturnStatusRecord.recordStatus(Long.parseLong(return_ord_id), loginUser.getUserId(), loginUser.getName(),loginUser.getOrgId(), Constant.BACK_LIST_STATUS_05,dd.getBoxNo());
						}
				    }else
				    {
				    	//驳回审核
					    detailPO1.setExecutiveDirectorSta(2);
					    //zyw 2014-8-27 修改签收数  变为  回运数 
					    TtAsWrOldReturnedDetailPO dd= (TtAsWrOldReturnedDetailPO) dao.select(detailPO).get(0);
					    Integer returnAmount = dd.getReturnAmount();
					    detailPO1.setSignAmount(returnAmount);
				    }
				  dao.update(detailPO, detailPO1);
			   }
		       act.setOutData("updateResult","updateSuccess");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--签收操作");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	
	//一键将签收数为0 的改为1
	public void oldPartSignAll(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		
		String return_ord_id = request.getParamValue("return_ord_id");
		try {
			String sql = "update tt_as_wr_old_returned_detail d set d.sign_amount=1,d.deduct_remark=0 where  d.sign_amount=0 and d.return_id="+return_ord_id+" AND EXISTS (SELECT a.claim_no FROM Tt_As_Wr_Application a WHERE a.claim_no=d.claim_no AND a.is_invoice=0)";
			dao.update(sql, null);
			act.setOutData("msg","updateSuccess");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--签收操作");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public String getSignNo(){
		List<Map<String,Object>> ps=dao.getSignNo("SEQ_OLD_SIGN");
		return ps.get(0).get("NUM").toString();
	}
	public String getSignNo2(){
		List<Map<String,Object>> ps=dao.getSignNo("SEQ_OLD_AUDIT_DONGAN");
		return ps.get(0).get("NUM").toString();
	}
	public void approveAndStored221(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		String no = "";
		String return_ord_id = request.getParamValue("return_ord_id");
		String temp = request.getParamValue("temp");
		String yieldly = request.getParamValue("yieldly");
		Integer status;
		OutStoreDAO outStoreDAO = new OutStoreDAO();
		if(Constant.PART_IS_CHANGHE_01.toString().equalsIgnoreCase(yieldly)){//只有一个产地！重庆
			//no = getSignNo();
			no=outStoreDAO.getNewSignNo();
		}else{
			//no = getSignNo2();
			no=outStoreDAO.getNewSignNo();
		}
		try {
			//记录审核人，审核时间信息 Iverson 2010-01-04
			TtAsWrReturnAuthitemPO reA = new TtAsWrReturnAuthitemPO();
			reA.setReturnId(Long.parseLong(return_ord_id));
			if(temp != null && temp.equals("1"))
			{
				status = Constant.BACK_LIST_STATUS_02;
				reA.setAuthStatus(Constant.BACK_LIST_STATUS_02);
			}else
			{
				status = Constant.BACK_LIST_STATUS_03;
				reA.setAuthStatus(Constant.BACK_LIST_STATUS_03);
			}
			reA.setAuthStatus(Constant.BACK_LIST_STATUS_03);
			List list5 = dao.select(reA);
			if(list5.size()>0){
				System.out.println("有签收记录!");
			}else{
				ReturnStatusRecord.recordStatus(Long.parseLong(return_ord_id), loginUser.getUserId(), loginUser.getName(),loginUser.getOrgId(), Constant.BACK_LIST_STATUS_03,"");
			}
			
			TtAsWrReturnedOrderPO popo = new TtAsWrReturnedOrderPO();
			popo.setId(Long.parseLong(return_ord_id));
			
			//实到箱数，包装情况，故障卡情况，清单情况
			String realBoxNo = request.getParamValue("REAL_BOX_NO");
			String partPackge = request.getParamValue("PART_PAKGE");
			String partMark = request.getParamValue("PART_MARK");
			String partDetail = request.getParamValue("PART_DETAIL");
//			String transportNo = request.getParamValue("TRANSPORT_NO");//发运单号
			String remark = request.getParamValue("signRemark");
			String outPartPackge = request.getParamValue("OUT_PART_PAKGE");
			String price = request.getParamValue("price");//==运费提出来单独审核
			TtAsWrReturnedOrderPO poValue = new TtAsWrReturnedOrderPO();
			poValue.setRealBoxNo(Integer.parseInt(realBoxNo));
//			poValue.setTransportNo(transportNo);
//			poValue.setAuthPrice(Double.valueOf(price));
			poValue.setPartPakge(Integer.parseInt(partPackge));
			poValue.setPartMark(Integer.parseInt(partMark));
			poValue.setPartDetail(Integer.parseInt(partDetail));
			poValue.setSignDate(new Date());
			poValue.setSignNo(no);
			poValue.setSignPerson(loginUser.getUserId());
			poValue.setStatus(status); //这里签收不改变状态为了拿出运费单独审核，在审核时再次改变状态 2014-1-15
			poValue.setUpdateBy(loginUser.getUserId());
			poValue.setUpdateDate(new Date());
			poValue.setSignRemark(remark);
			poValue.setOutPartPackge(Integer.valueOf(outPartPackge));
			dao.update(popo, poValue);
//			dao.updateInHouse(return_ord_id);//改变入库状态
			//end
			act.setOutData("signNo",no);
			act.setOutData("updateResult","updateSuccess");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--签收操作");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public String getAuditNo(){
		List<Map<String,Object>> ps=dao.getSignNo("SEQ_OLD_AUDIT");
		return ps.get(0).get("NUM").toString();
	}
	public String getAuditNo2(){
		List<Map<String,Object>> ps=dao.getSignNo("SEQ_OLD_AUDIT_DONGAN");
		return ps.get(0).get("NUM").toString();
	}
	//旧件签收入库确认操作
	public void approveAndStoredSure221(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		String yieldly = request.getParamValue("yieldly");
		String no = "";
		if(Constant.PART_IS_CHANGHE_01.toString().equalsIgnoreCase(yieldly)){
			 no = getAuditNo();
		}else {
			no = getAuditNo2();;
		}
		boolean flag = true;
		String notice="";
		String return_ord_id = request.getParamValue("return_ord_id");
		String boxNo = request.getParamValue("boxNo");
		List<Map<String,Object>> ps=dao.approveAndStoredSure221(return_ord_id);
		if(ps.size()>0){
			flag = false;
			for(int i=0;i<ps.size();i++){
				notice+="编号：【"+ ps.get(i).get("BARCODE_NO")+"】\n";
			}
			notice+= " 未选择库位或者签收数为0,但是没有选择扣除原因,请修改!";
		}
		
		String updateResult = "updateSuccess";
		try {
			if(flag){
			params.put("return_ord_id", return_ord_id);
			params.put("logonId",String.valueOf(loginUser.getUserId()));
			params.put("company_id",String.valueOf(companyId));
			params.put("i_claim_id", return_ord_id);
			//修改物流单状态
			
			/*******Inerson add By 根据回运物流单Id更新签收旧件时间********************/
			List list = dao.queryReturnOrder(return_ord_id);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				//把回运单的状态改为已签收
				TtAsWrReturnedOrderPO poReturn = new TtAsWrReturnedOrderPO();
				poReturn.setId(((BigDecimal)map.get("ID")).longValue());
				TtAsWrReturnedOrderPO poReturnValue = new TtAsWrReturnedOrderPO();
				poReturnValue.setStatus(Constant.RETURNORDER_TYPE_04);
				poReturnValue.setUpdateBy(loginUser.getUserId());
				poReturnValue.setUpdateDate(new Date());
				poReturnValue.setInWarhouseDate(new Date());
				dao.update(poReturn, poReturnValue);
			}
			//批量更新索赔单的旧件是否审核状态字段
			String sqlupdate =" update TT_AS_WR_OLD_RETURNED_DETAIL d set d.in_date=sysdate, d.is_in_house="+Constant.IF_TYPE_YES+" where d.is_in_house="+Constant.IF_TYPE_NO+" and  d.RETURN_AMOUNT= d.SIGN_AMOUNT and   d.return_id="+return_ord_id+"";
			String sqlupdate1 =" update TT_AS_WR_OLD_RETURNED_DETAIL d set d.in_date=sysdate, d.is_in_house="+Constant.IF_TYPE_YES+" where d.is_in_house="+Constant.IF_TYPE_NO+" and  d.RETURN_AMOUNT != d.SIGN_AMOUNT and d.Executive_director_sta =1  and   d.return_id="+return_ord_id+"";
			dao.update(sqlupdate, null);
			dao.update(sqlupdate1, null);
			//记录审核人，审核时间信息 Iverson 2010-01-04
			
			StringBuffer sql= new StringBuffer();
			sql.append("update TT_AS_WR_OLD_RETURNED_DETAIL a\n" );
			sql.append("   set a.EXECUTIVE_DIRECTOR_STA = 0 , a.Executive_director_ram = null \n" );
			sql.append(" where a.RETURN_AMOUNT = a.SIGN_AMOUNT  and  return_id =" + return_ord_id);
			dao.update(sql.toString(), null );

			
			TtAsWrOldReturnedDetailPO detailPO = new TtAsWrOldReturnedDetailPO();
			detailPO.setReturnId(Long.parseLong(return_ord_id));
			detailPO.setIsInHouse(10041002);
			List<TtAsWrOldReturnedDetailPO>  Detaillist= dao.select(detailPO);
			
			
			
			if(Detaillist.size() == 0)
			{
				TtAsWrOldReturnedPO idObj=new TtAsWrOldReturnedPO();
				idObj.setId(Long.parseLong(return_ord_id));
				TtAsWrOldReturnedPO vo=new TtAsWrOldReturnedPO();
				vo.setStatus(Constant.BACK_LIST_STATUS_05);
				vo.setUpdateBy(loginUser.getUserId());
				vo.setInWarhouseDate(new Date());
				vo.setUpdateDate(new Date());
				vo.setAuditNo(Integer.parseInt(no));
				vo.setInWarhouseBy(loginUser.getUserId());
				dao.update(idObj,vo);
				String sqll = "update tt_as_wr_application a set a.is_old_audit =1 where a.id in (SELECT d.claim_id FROM Tt_As_Wr_Old_Returned_Detail d WHERE d.return_id="+return_ord_id+" GROUP BY d.claim_id) ";
				dao.update(sqll, null);
			}
			ReturnStatusRecord.recordStatus(Long.parseLong(return_ord_id), loginUser.getUserId(), loginUser.getName(),loginUser.getOrgId(), Constant.BACK_LIST_STATUS_05,boxNo);
			
			updateResult = "updateSuccess";
			
			}else{
				updateResult = "updateFailed";
			}
			//========================
			/**
			 * 加入索赔单数据更新的操作 zyw 2014-8-18
			 */
//			if(updateResult.equals("updateSuccess")){
//				int res = dao.updateApplicationSal(return_ord_id);
//				logger.info("旧件审核时更新数据的结果:"+res);
//			}
			//========================
			act.setOutData("auditNo",no);
			act.setOutData("notice",notice);
			act.setOutData("updateResult",updateResult);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--签收操作");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-05-14 修改方法
	public void approveAndStored321(){
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		try {
			//记录审核运费
			String claimId = request.getParamValue("claimId");
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(claimId));
			TtAsWrOldReturnedPO poValue = new TtAsWrOldReturnedPO();
			poValue.setAuthPrice(Double.parseDouble(request.getParamValue("price")));
			poValue.setUpdateBy(loginUser.getUserId());
			poValue.setUpdateDate(new Date());
			dao.update(popo, poValue);
			
			//记录运费备注  begin
			String yes_no = request.getParamValue("yes_no");
			if(Integer.parseInt(yes_no)==10041001){
				String priceRemark = request.getParamValue("priceRemark");
				if(!"".equals(priceRemark) && priceRemark!=null){
					TtAsWrOldReturnedPO po1 = new TtAsWrOldReturnedPO();
					po1.setId(Long.parseLong(claimId));
					TtAsWrOldReturnedPO poValue1 = new TtAsWrOldReturnedPO();
					poValue1.setPriceRemark(priceRemark);
					dao.update(po1, poValue1);
				}
			}else{
				String priceRemark1 = request.getParamValue("priceRemark1");
				if(!"".equals(priceRemark1) && priceRemark1!=null){
					TtAsWrOldReturnedPO po2 = new TtAsWrOldReturnedPO();
					po2.setId(Long.parseLong(claimId));
					TtAsWrOldReturnedPO poValue2 = new TtAsWrOldReturnedPO();
					poValue2.setPriceRemark(priceRemark1);
					dao.update(po2, poValue2);
				}
			}
			//end
			act.setOutData("updateResult","updateSuccess");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--签收操作");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * zyw 2014-10-14 
	 * 签收回运旧件，onbulr时间及时更新
	 */
	public void approveAndStored33(){
		dao=ClaimApproveOldPartStoredDao.getInstance();
		String id=DaoFactory.getParam(request,"id");//明细ID
		String signNum=DaoFactory.getParam(request,"signNum");//签收数
		String deduct=DaoFactory.getParam(request,"deduct");//抵扣原因
		String type=DaoFactory.getParam(request,"type");
		String barCode=DaoFactory.getParam(request,"barCode");
		String pageNum=DaoFactory.getParam(request,"pageNum");
		String result="";
		int count=0;
		TtAsWrOldReturnedDetailPO tempPo=new TtAsWrOldReturnedDetailPO();
		if(Utility.testString(id)){
			tempPo.setId(Long.valueOf(id));
			tempPo = (TtAsWrOldReturnedDetailPO) dao.select(tempPo).get(0);
		}
		TtAsWrOldReturnedDetailPO idObj=new TtAsWrOldReturnedDetailPO();
		TtAsWrOldReturnedDetailPO vo=new TtAsWrOldReturnedDetailPO();
		List<Map<String,String>> labourList = dao.queryDeductLabour(tempPo.getClaimId(), tempPo.getPartId());
		String partCode = tempPo.getPartCode();
		String sql = "select * from Tt_As_Wr_Old_Returned_Detail d where d.claim_no='"+tempPo.getClaimNo()+"' and d.part_code='"+partCode+"' AND d.ID NOT IN ("+id+")  AND d.sign_amount>0";
		List<Map<String,Object>> list = dao.pageQuery(sql, null, dao.getFunName());
		if("0".equalsIgnoreCase(signNum)&&tempPo.getSignAmount()==1){//如果得到的签收数量是0，那么就表示是扣件，则需要对相关联的一些件进行扣件
			
			if(tempPo.getIsMainCode().intValue() == Constant.RESPONS_NATURE_STATUS_01){//如果是主因件,
				//首先判断旧件明细表是否还有这个件的其他条码
				if(list ==null || list.size()<1){ //如果没有其他的数据,说明该单子的主因件被扣完,则还要扣除相应的工时以及其次因件
					if(labourList!=null && labourList.size()>0){
						for(int j=0;j<labourList.size();j++){
							//扣除工时
							 String ll = " UPDATE Tt_As_Wr_Labouritem l SET l.apply_amount=0,l.balance_amount=0,l.apply_quantity=0,l.balance_quantity=0 WHERE l.labour_id="+String.valueOf(labourList.get(j).get("LABOUR_ID"))+" AND l.apply_amount>0";
							 dao.update(ll, null);
							 //扣除配件
							 String pl = "UPDATE Tt_As_Wr_Partsitem p SET  p.apply_quantity=0,p.apply_amount=0,p.balance_amount=0,p.balance_quantity=0 WHERE p.ID="+tempPo.getClaimId()+" AND p.wr_labourcode='"+labourList.get(j).get("WR_LABOURCODE")+"' AND p.apply_amount>0";
							 dao.update(pl, null);
							 
							 
							 StringBuffer sql02= new StringBuffer();
							 sql02.append("update tt_as_wr_netitem t set t.BALANCE_AMOUNT = 0  where t.ID =  " +tempPo.getClaimId() );
							 dao.update(sql02.toString(), null);
							 
							 StringBuffer sql03= new StringBuffer();
							 sql03.append("update   tt_claim_accessory_dtl t set t.PRICE = 0 where t.CLAIM_NO = '"+tempPo.getClaimNo()+"'");
							 DaoFactory.getsql(sql03, "t.main_part_code", partCode, 1);
							 dao.update(sql03.toString(), null);
							 
							 StringBuffer sql04= new StringBuffer();
							 sql04.append("update   TT_AS_WR_COMPENSATION_APP t set t.PASS_PRICE = 0 where t.CLAIM_NO = '"+tempPo.getClaimNo()+"'");
							 DaoFactory.getsql(sql03, "t.part_code", partCode, 1);
							 dao.update(sql04.toString(), null);
							 
							 
							 StringBuffer sql01= new StringBuffer();
							 sql01.append("update TT_AS_WR_APPLICATION t set\n" );
							 sql01.append("\n" );
							 sql01.append("t.BALANCE_NETITEM_AMOUNT = ( SELECT sum(nvl(a.BALANCE_AMOUNT,0)) from TT_AS_WR_NETITEM a where a.ID = t.ID   ),\n" );
							 sql01.append("\n" );
							 sql01.append("t.accessories_price = (  SELECT sum(nvl(b.PRICE,0)) from  tt_claim_accessory_dtl b where b.CLAIM_NO = t.CLAIM_NO  ),\n" );
							 sql01.append("\n" );
							 sql01.append("t.compensation_money  = ( SELECT sum(nvl (c.PASS_PRICE,0  )  ) from TT_AS_WR_COMPENSATION_APP c where c.CLAIM_NO = t.CLAIM_NO  ),\n" );
							 sql01.append("\n" );
							 sql01.append("t.BALANCE_PART_AMOUNT = ( SELECT sum(nvl(d.BALANCE_AMOUNT,0)) from TT_AS_WR_PARTSITEM d where d.ID = t.ID   ),\n" );
							 sql01.append("\n" );
							 sql01.append("t.BALANCE_LABOUR_AMOUNT = ( SELECT sum(nvl(e.BALANCE_AMOUNT,0)) from TT_AS_WR_LABOURITEM e  where e.ID = t.ID    ),\n" );
							 sql01.append("\n" );
							 sql01.append("t.PARTS_COUNT = ( SELECT sum(nvl(f.BALANCE_QUANTITY,0)) from TT_AS_WR_PARTSITEM f where f.ID = t.ID ),\n" );
							 sql01.append("\n" );
							 sql01.append("t.LABOUR_HOURS = ( SELECT sum(nvl(g.BALANCE_QUANTITY,0)) from TT_AS_WR_LABOURITEM g where g.ID = t.ID    )\n" );
							 sql01.append("\n" );
							 sql01.append("\n" );
							 sql01.append("where t.ID =  "+tempPo.getClaimId()+"\n" );
							
							 dao.update(sql01.toString(), null);
							 
							 
							 StringBuffer sql05= new StringBuffer();
							 sql05.append("update TT_AS_WR_APPLICATION t set\n" );
							 sql05.append("t.BALANCE_AMOUNT = nvl(t.BALANCE_NETITEM_AMOUNT,0)+ nvl(t.accessories_price,0)+nvl(t.compensation_money,0)+nvl(t.BALANCE_PART_AMOUNT,0)\n" );
							 sql05.append("+nvl(t.BALANCE_LABOUR_AMOUNT,0)\n" );
							 sql05.append("where t.ID =  "+tempPo.getClaimId()+"\n" );
							 dao.update(sql05.toString(), null);
							 
							 //扣除旧件
							 String ol = "UPDATE Tt_As_Wr_Old_Returned_Detail d SET d.sign_amount=0,d.stock_amount=0,d.is_in_house=10041002,d.deduct_remark="+Constant.OLDPART_DEDUCT_TYPE_22+",d.deduct_part_code='"+partCode+"'  WHERE d.claim_id ="+tempPo.getClaimId()+" AND d.part_id IN (SELECT p.part_id FROM Tt_As_Wr_Partsitem p WHERE p.ID="+tempPo.getClaimId()+"  AND p.wr_labourcode='"+labourList.get(j).get("WR_LABOURCODE")+"' AND  p.part_code NOT IN('"+partCode+"'))";
							 dao.update(ol, null);
						}
					}
				}
			}else{//如果是次因件,件扣完则扣工时,否则不操作
				TtAsWrPartsitemPO tp = new TtAsWrPartsitemPO();
				tp.setPartId(tempPo.getPartId());
				tp.setId(tempPo.getClaimId());
				tp=(TtAsWrPartsitemPO) dao.select(tp).get(0);
				if(list ==null || list.size()<1){
//					//扣除工时,: 首先判断该工时含有其他对应的配件，如果有则不扣工时，否则将工时也相应的扣除
					TtAsWrPartsitemPO pp = new TtAsWrPartsitemPO(); 
					pp.setId(tempPo.getClaimId());
					pp.setWrLabourcode(tp.getWrLabourcode());
					List<Map<String,Object>> lList = dao.select(pp);//查询该次因件对应的工时，索赔单下面是否含有其他的配件
					if(lList!=null&&lList.size()==1){//根据索赔的那ID，工时代码查询，如果查询结果集 只有一条数据则不含其他件。否则就包含其他件
						 String ll = " UPDATE Tt_As_Wr_Labouritem l SET l.apply_amount=0,l.balance_amount=0,l.apply_quantity=0,l.balance_quantity=0 WHERE l.id='"+tp.getId()+"' and l.wr_labourcode='"+tp.getWrLabourcode()+"' AND l.apply_amount>0";
						 dao.update(ll, null);
					}
					 //扣除配件
					 String pl = "UPDATE Tt_As_Wr_Partsitem p SET  p.apply_quantity=0,p.apply_amount=0,p.balance_amount=0,p.balance_quantity=0 WHERE p.part_id ="+tp.getPartId()+"  and p.id="+tp.getId()+" AND p.apply_amount>0";
					 dao.update(pl, null);
					 
					 StringBuffer sql01= new StringBuffer();
					 sql01.append("update TT_AS_WR_APPLICATION t set\n" );
					 sql01.append("\n" );
					 sql01.append("t.BALANCE_NETITEM_AMOUNT = ( SELECT sum(nvl(a.BALANCE_AMOUNT,0)) from TT_AS_WR_NETITEM a where a.ID = t.ID   ),\n" );
					 sql01.append("\n" );
					 sql01.append("t.accessories_price = (  SELECT sum(nvl(b.PRICE,0)) from  tt_claim_accessory_dtl b where b.CLAIM_NO = t.CLAIM_NO  ),\n" );
					 sql01.append("\n" );
					 sql01.append("t.compensation_money  = ( SELECT sum(nvl (c.PASS_PRICE,0  )  ) from TT_AS_WR_COMPENSATION_APP c where c.CLAIM_NO = t.CLAIM_NO  ),\n" );
					 sql01.append("\n" );
					 sql01.append("t.BALANCE_PART_AMOUNT = ( SELECT sum(nvl(d.BALANCE_AMOUNT,0)) from TT_AS_WR_PARTSITEM d where d.ID = t.ID   ),\n" );
					 sql01.append("\n" );
					 sql01.append("t.BALANCE_LABOUR_AMOUNT = ( SELECT sum(nvl(e.BALANCE_AMOUNT,0)) from TT_AS_WR_LABOURITEM e  where e.ID = t.ID    ),\n" );
					 sql01.append("\n" );
					 sql01.append("t.PARTS_COUNT = ( SELECT sum(nvl(f.BALANCE_QUANTITY,0)) from TT_AS_WR_PARTSITEM f where f.ID = t.ID ),\n" );
					 sql01.append("\n" );
					 sql01.append("t.LABOUR_HOURS = ( SELECT sum(nvl(g.BALANCE_QUANTITY,0)) from TT_AS_WR_LABOURITEM g where g.ID = t.ID    )\n" );
					 sql01.append("\n" );
					 sql01.append("\n" );
					 sql01.append("where t.ID =  "+tempPo.getClaimId()+"\n" );
					
					 dao.update(sql01.toString(), null);
					 
					 
					 StringBuffer sql05= new StringBuffer();
					 sql05.append("update TT_AS_WR_APPLICATION t set\n" );
					 sql05.append("t.BALANCE_AMOUNT = nvl(t.BALANCE_NETITEM_AMOUNT,0)+ nvl(t.accessories_price,0)+nvl(t.compensation_money,0)+nvl(t.BALANCE_PART_AMOUNT,0)\n" );
					 sql05.append("+nvl(t.BALANCE_LABOUR_AMOUNT,0)\n" );
					 sql05.append("where t.ID =  "+tempPo.getClaimId()+"\n" );
					 dao.update(sql05.toString(), null);
					 
			   }else{//如果有数据,则说明还有件没有扣完,此时只将索赔单对应的配件数量和价格减去相应的值
					 String pl = "UPDATE Tt_As_Wr_Partsitem p SET  p.apply_quantity=p.apply_quantity-1,p.apply_amount=p.apply_amount-p.price,p.balance_amount=p.balance_amount-p.price,p.balance_quantity=p.balance_quantity-1 WHERE p.part_id ="+tp.getPartId()+"  and p.id="+tp.getId()+" AND p.apply_amount>0";
					 dao.update(pl, null);
					 
					 StringBuffer sql01= new StringBuffer();
					 sql01.append("update TT_AS_WR_APPLICATION t set\n" );
					 sql01.append("\n" );
					 sql01.append("t.BALANCE_NETITEM_AMOUNT = ( SELECT sum(nvl(a.BALANCE_AMOUNT,0)) from TT_AS_WR_NETITEM a where a.ID = t.ID   ),\n" );
					 sql01.append("\n" );
					 sql01.append("t.accessories_price = (  SELECT sum(nvl(b.PRICE,0)) from  tt_claim_accessory_dtl b where b.CLAIM_NO = t.CLAIM_NO  ),\n" );
					 sql01.append("\n" );
					 sql01.append("t.compensation_money  = ( SELECT sum(nvl (c.PASS_PRICE,0  )  ) from TT_AS_WR_COMPENSATION_APP c where c.CLAIM_NO = t.CLAIM_NO  ),\n" );
					 sql01.append("\n" );
					 sql01.append("t.BALANCE_PART_AMOUNT = ( SELECT sum(nvl(d.BALANCE_AMOUNT,0)) from TT_AS_WR_PARTSITEM d where d.ID = t.ID   ),\n" );
					 sql01.append("\n" );
					 sql01.append("t.BALANCE_LABOUR_AMOUNT = ( SELECT sum(nvl(e.BALANCE_AMOUNT,0)) from TT_AS_WR_LABOURITEM e  where e.ID = t.ID    ),\n" );
					 sql01.append("\n" );
					 sql01.append("t.PARTS_COUNT = ( SELECT sum(nvl(f.BALANCE_QUANTITY,0)) from TT_AS_WR_PARTSITEM f where f.ID = t.ID ),\n" );
					 sql01.append("\n" );
					 sql01.append("t.LABOUR_HOURS = ( SELECT sum(nvl(g.BALANCE_QUANTITY,0)) from TT_AS_WR_LABOURITEM g where g.ID = t.ID    )\n" );
					 sql01.append("\n" );
					 sql01.append("\n" );
					 sql01.append("where t.ID =  "+tempPo.getClaimId()+"\n" );
					
					 dao.update(sql01.toString(), null);
					 
					 StringBuffer sql05= new StringBuffer();
					 sql05.append("update TT_AS_WR_APPLICATION t set\n" );
					 sql05.append("t.BALANCE_AMOUNT = nvl(t.BALANCE_NETITEM_AMOUNT,0)+ nvl(t.accessories_price,0)+nvl(t.compensation_money,0)+nvl(t.BALANCE_PART_AMOUNT,0)\n" );
					 sql05.append("+nvl(t.BALANCE_LABOUR_AMOUNT,0)\n" );
					 sql05.append("where t.ID =  "+tempPo.getClaimId()+"\n" );
					 dao.update(sql05.toString(), null);
				}
			}
			
			vo.setDeductPartCode(partCode);
			vo.setStockAmount(1);
			vo.setDeductRemark(Constant.OLDPART_DEDUCT_TYPE_06);
			vo.setSignAmount(Integer.parseInt(signNum));
			dao.oldPartDeduction(tempPo.getClaimId(),id,String.valueOf(labourList.get(0).get("LABOUR_ID")),String.valueOf(labourList.get(0).get("WR_LABOURCODE")),String.valueOf(labourList.get(0).get("WR_LABOURNAME")));
		}else if("1".equalsIgnoreCase(signNum)&&tempPo.getSignAmount()==0){
			//如果得到的签收数量为1，并且数据库中的签收数量为0，说明是扣件完后再次验件入库，此时需要将前期扣件连带扣除的件回滚
			if(tempPo.getStockAmount()==null||tempPo.getStockAmount()==0){//如果是被连带扣的件则不操作
				result="该件是由于连带扣件,不允许更改!";
			}else{
				/**
				 * 首先更新该索赔单下的同一个零件的其他条码
				 
				 String mSql = "UPDATE Tt_As_Wr_Old_Returned_Detail d SET d.sign_amount=1, d.deduct_remark=null,d.stock_amount=2,d.deduct_part_code=null  WHERE d.part_code='"+tempPo.getPartCode()+"' and d.claim_id ="+tempPo.getClaimId()+" AND d.id not in("+id+")";
				 this.update(mSql, null);
				 */
				if(tempPo.getIsMainCode().intValue() == Constant.RESPONS_NATURE_STATUS_01){//如果是主因件,
				if(list==null || list.size()<1){ //如果还有其他签收数大于 0  的数据就不需要进行数据回滚,因为如果有数据在扣件时就表示没有连带扣
					if(labourList!=null && labourList.size()>0){
						for(int j=0;j<labourList.size();j++){
							//增加工时
							 String ll = " UPDATE Tt_As_Wr_Labouritem l SET l.apply_amount=l.labour_amount,l.balance_amount=l.labour_amount,l.apply_quantity=l.labour_quantity,l.balance_quantity=l.labour_quantity WHERE l.labour_id="+String.valueOf(labourList.get(j).get("LABOUR_ID"))+" ";
							 dao.update(ll, null);
							 //增加配件
							 String pl = "UPDATE Tt_As_Wr_Partsitem p SET  p.apply_quantity=p.quantity,p.apply_amount=p.amount,p.balance_amount=p.amount,p.balance_quantity=p.quantity WHERE p.ID="+tempPo.getClaimId()+" AND p.wr_labourcode='"+labourList.get(j).get("WR_LABOURCODE")+"' ";
							 dao.update(pl, null);
							 
							 StringBuffer sql02= new StringBuffer();
							 sql02.append("update tt_as_wr_netitem t set t.BALANCE_AMOUNT = t.APPLY_AMOUNT where t.ID = "+ tempPo.getClaimId());
							 dao.update(sql02.toString(), null);
							 
							 StringBuffer sql03= new StringBuffer();
							 sql03.append("update   tt_claim_accessory_dtl t set t.PRICE = t.APP_PRICE where t.CLAIM_NO = '"+tempPo.getClaimNo()+"'");
							 dao.update(sql03.toString(), null);
							 
							 StringBuffer sql04= new StringBuffer();
							 sql04.append("update   TT_AS_WR_COMPENSATION_APP t set t.PASS_PRICE = t.APPLY_PRICE where t.CLAIM_NO = '"+tempPo.getClaimNo()+"'");
							 dao.update(sql04.toString(), null);
							 
							 
							 StringBuffer sql01= new StringBuffer();
							 sql01.append("update TT_AS_WR_APPLICATION t set\n" );
							 sql01.append("\n" );
							 sql01.append("t.BALANCE_NETITEM_AMOUNT = ( SELECT sum(nvl(a.BALANCE_AMOUNT,0)) from TT_AS_WR_NETITEM a where a.ID = t.ID   ),\n" );
							 sql01.append("\n" );
							 sql01.append("t.accessories_price = (  SELECT sum(nvl(b.PRICE,0)) from  tt_claim_accessory_dtl b where b.CLAIM_NO = t.CLAIM_NO  ),\n" );
							 sql01.append("\n" );
							 sql01.append("t.compensation_money  = ( SELECT sum(nvl (c.PASS_PRICE,0  )  ) from TT_AS_WR_COMPENSATION_APP c where c.CLAIM_NO = t.CLAIM_NO  ),\n" );
							 sql01.append("\n" );
							 sql01.append("t.BALANCE_PART_AMOUNT = ( SELECT sum(nvl(d.BALANCE_AMOUNT,0)) from TT_AS_WR_PARTSITEM d where d.ID = t.ID   ),\n" );
							 sql01.append("\n" );
							 sql01.append("t.BALANCE_LABOUR_AMOUNT = ( SELECT sum(nvl(e.BALANCE_AMOUNT,0)) from TT_AS_WR_LABOURITEM e  where e.ID = t.ID    ),\n" );
							 sql01.append("\n" );
							 sql01.append("t.PARTS_COUNT = ( SELECT sum(nvl(f.BALANCE_QUANTITY,0)) from  TT_AS_WR_PARTSITEM f where f.ID = t.ID ),\n" );
							 sql01.append("\n" );
							 sql01.append("t.LABOUR_HOURS = ( SELECT sum(nvl(g.BALANCE_QUANTITY,0)) from TT_AS_WR_LABOURITEM g where g.ID = t.ID    )\n" );
							 sql01.append("\n" );
							 
							 sql01.append("\n" );
							 sql01.append("where t.ID =  "+tempPo.getClaimId()+"\n" );
							
							 dao.update(sql01.toString(), null);
							 
							 StringBuffer sql05= new StringBuffer();
							 sql05.append("update TT_AS_WR_APPLICATION t set\n" );
							 sql05.append("t.BALANCE_AMOUNT = nvl(t.BALANCE_NETITEM_AMOUNT,0)+ nvl(t.accessories_price,0)+nvl(t.compensation_money,0)+nvl(t.BALANCE_PART_AMOUNT,0)\n" );
							 sql05.append("+nvl(t.BALANCE_LABOUR_AMOUNT,0)\n" );
							 sql05.append("where t.ID =  "+tempPo.getClaimId()+"\n" );
							 dao.update(sql05.toString(), null);
							 
							 //增加旧件
							 String ol = "UPDATE Tt_As_Wr_Old_Returned_Detail d SET d.sign_amount=1,d.is_in_house=10041001, d.deduct_remark=null,d.stock_amount=2,d.deduct_part_code=null  WHERE d.claim_id ="+tempPo.getClaimId()+" AND d.part_id IN (SELECT p.part_id FROM Tt_As_Wr_Partsitem p WHERE p.ID="+tempPo.getClaimId()+"  AND p.wr_labourcode='"+labourList.get(j).get("WR_LABOURCODE")+"' AND  p.part_code NOT IN('"+partCode+"'))";
							 dao.update(ol, null);
							 
						}
					}
				}
				}else{
					TtAsWrPartsitemPO tp = new TtAsWrPartsitemPO();
					tp.setPartId(tempPo.getPartId());
					tp.setId(tempPo.getClaimId());
					tp=(TtAsWrPartsitemPO) dao.select(tp).get(0);
					if(list==null || list.size()<1){//如果是次因件,在没有查询到其他数据的时候,表示以前扣件时已经完全扣掉.现在需要回滚
						//增加工时
						 String ll = " UPDATE Tt_As_Wr_Labouritem l SET l.apply_amount=l.labour_amount,l.balance_amount=l.labour_amount,l.apply_quantity=l.labour_quantity,l.balance_quantity=l.labour_quantity WHERE l.id='"+tp.getId()+"' and l.wr_labourcode='"+tp.getWrLabourcode()+"'";
						 dao.update(ll, null);
						 //增加配件
						 String pl = "UPDATE Tt_As_Wr_Partsitem p SET  p.apply_quantity=p.quantity,p.apply_amount=p.amount,p.balance_amount=p.amount,p.balance_quantity=p.quantity WHERE p.part_id ="+tp.getPartId()+"  and p.id="+tp.getId()+"";
						 dao.update(pl, null);
						 
						 StringBuffer sql01= new StringBuffer();
						 sql01.append("update TT_AS_WR_APPLICATION t set\n" );
						 sql01.append("\n" );
						 sql01.append("t.BALANCE_NETITEM_AMOUNT = ( SELECT sum(nvl(a.BALANCE_AMOUNT,0)) from TT_AS_WR_NETITEM a where a.ID = t.ID   ),\n" );
						 sql01.append("\n" );
						 sql01.append("t.accessories_price = (  SELECT sum(nvl(b.PRICE,0)) from  tt_claim_accessory_dtl b where b.CLAIM_NO = t.CLAIM_NO  ),\n" );
						 sql01.append("\n" );
						 sql01.append("t.compensation_money  = ( SELECT sum(nvl (c.PASS_PRICE,0  )  ) from TT_AS_WR_COMPENSATION_APP c where c.CLAIM_NO = t.CLAIM_NO  ),\n" );
						 sql01.append("\n" );
						 sql01.append("t.BALANCE_PART_AMOUNT = ( SELECT sum(nvl(d.BALANCE_AMOUNT,0)) from TT_AS_WR_PARTSITEM d where d.ID = t.ID   ),\n" );
						 sql01.append("\n" );
						 sql01.append("t.BALANCE_LABOUR_AMOUNT = ( SELECT sum(nvl(e.BALANCE_AMOUNT,0)) from TT_AS_WR_LABOURITEM e  where e.ID = t.ID    ),\n" );
						 sql01.append("\n" );
						 sql01.append("t.PARTS_COUNT = ( SELECT sum(nvl(f.BALANCE_QUANTITY,0)) from TT_AS_WR_PARTSITEM f where f.ID = t.ID ),\n" );
						 sql01.append("\n" );
						 sql01.append("t.LABOUR_HOURS = ( SELECT sum(nvl(g.BALANCE_QUANTITY,0)) from TT_AS_WR_LABOURITEM g where g.ID = t.ID    )\n" );
						 sql01.append("\n" );
						 sql01.append("\n" );
						 sql01.append("where t.ID =  "+tempPo.getClaimId()+"\n" );
						
						 dao.update(sql01.toString(), null);
						 
						 
						 StringBuffer sql05= new StringBuffer();
						 sql05.append("update TT_AS_WR_APPLICATION t set\n" );
						 sql05.append("t.BALANCE_AMOUNT = nvl(t.BALANCE_NETITEM_AMOUNT,0)+ nvl(t.accessories_price,0)+nvl(t.compensation_money,0)+nvl(t.BALANCE_PART_AMOUNT,0)\n" );
						 sql05.append("+nvl(t.BALANCE_LABOUR_AMOUNT,0)\n" );
						 sql05.append("where t.ID =  "+tempPo.getClaimId()+"\n" );
						 dao.update(sql05.toString(), null);
					}else{//否则就将索赔单对应的配件数量,金额增加相应的值
						 String pl = "UPDATE Tt_As_Wr_Partsitem p SET  p.apply_quantity=nvl(p.apply_quantity,0)+1,p.apply_amount=nvl(p.apply_amount,0)+nvl(p.price,0),p.balance_amount=nvl(p.balance_amount,0)+nvl(p.price,0),p.balance_quantity=nvl(p.balance_quantity,0)+1 WHERE p.part_id ="+tp.getPartId()+"  and p.id="+tp.getId()+" and p.apply_quantity<p.quantity";
						 dao.update(pl, null);
						 StringBuffer sql01= new StringBuffer();
						 sql01.append("update TT_AS_WR_APPLICATION t set\n" );
						 sql01.append("\n" );
						 sql01.append("t.BALANCE_NETITEM_AMOUNT = ( SELECT sum(nvl(a.BALANCE_AMOUNT,0)) from TT_AS_WR_NETITEM a where a.ID = t.ID   ),\n" );
						 sql01.append("\n" );
						 sql01.append("t.accessories_price = (  SELECT sum(nvl(b.PRICE,0)) from  tt_claim_accessory_dtl b where b.CLAIM_NO = t.CLAIM_NO  ),\n" );
						 sql01.append("\n" );
						 sql01.append("t.compensation_money  = ( SELECT sum(nvl (c.PASS_PRICE,0  )  ) from TT_AS_WR_COMPENSATION_APP c where c.CLAIM_NO = t.CLAIM_NO  ),\n" );
						 sql01.append("\n" );
						 sql01.append("t.BALANCE_PART_AMOUNT = ( SELECT sum(nvl(d.BALANCE_AMOUNT,0)) from TT_AS_WR_PARTSITEM d where d.ID = t.ID   ),\n" );
						 sql01.append("\n" );
						 sql01.append("t.BALANCE_LABOUR_AMOUNT = ( SELECT sum(nvl(e.BALANCE_AMOUNT,0)) from TT_AS_WR_LABOURITEM e  where e.ID = t.ID    ),\n" );
						 sql01.append("\n" );
						 sql01.append("t.PARTS_COUNT = ( SELECT sum(nvl(f.BALANCE_QUANTITY,0)) from TT_AS_WR_PARTSITEM f where f.ID = t.ID ),\n" );
						 sql01.append("\n" );
						 sql01.append("t.LABOUR_HOURS = ( SELECT sum(nvl(g.BALANCE_QUANTITY,0)) from TT_AS_WR_LABOURITEM g where g.ID = t.ID    )\n" );
						 sql01.append("\n" );
						 sql01.append("\n" );
						 sql01.append("where t.ID =  "+tempPo.getClaimId()+"\n" );
						
						 dao.update(sql01.toString(), null);
						 
						 
						 StringBuffer sql05= new StringBuffer();
						 sql05.append("update TT_AS_WR_APPLICATION t set\n" );
						 sql05.append("t.BALANCE_AMOUNT = nvl(t.BALANCE_NETITEM_AMOUNT,0)+ nvl(t.accessories_price,0)+nvl(t.compensation_money,0)+nvl(t.BALANCE_PART_AMOUNT,0)\n" );
						 sql05.append("+nvl(t.BALANCE_LABOUR_AMOUNT,0)\n" );
						 sql05.append("where t.ID =  "+tempPo.getClaimId()+"\n" );
						 dao.update(sql05.toString(), null);
					}
				}
				
			
				vo.setSignAmount(Integer.parseInt(signNum));
				vo.setDeductRemark(0);
				dao.oldPartDeductionDelete(id);
			}
		}
		if(!"".equals(deduct)&&!"0".equalsIgnoreCase(deduct)){
			if(tempPo.getStockAmount()!=null&&tempPo.getStockAmount().intValue()!=0){
				vo.setDeductRemark(Integer.parseInt(deduct));
			}else{
				result="该件为连带被扣件,不允许修改原因!";
			}
		}else {
			/*if(tempPo.getStockAmount()!=null&&tempPo.getStockAmount().intValue()!=0){
				vo.setDeductRemark(0);
			}else{
				System.out.println("============");
			}*/
			
		}
		//根据传来的type不同采取不同的处理方式
		if("5".equalsIgnoreCase(type)){
			idObj.setBarcodeNo(barCode);
			vo.setIsScan(1);
		}else{
		idObj.setId(Long.parseLong(id));
			
		if("1".equalsIgnoreCase(type)){
			vo.setIsScan(1);
		}else {
			vo.setIsScan(0);
		}
		}
		 count  = dao.update(idObj,vo);
		if(count>0){
			act.setOutData("updateResult","updateSuccess");
		}
		act.setOutData("results",result);
		act.setOutData("pageNum", pageNum);
	}
	
	//签收回运旧件,更新库区
	public void approveAndStored44(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		
		String id=ClaimTools.dealParamStr(request.getParamValue("id"));//明细ID
		String wrHouse=ClaimTools.dealParamStr(request.getParamValue("localWar"));//库区
		String result="updateSuccess";
		
		boolean flag = dao.checkWarHouse( wrHouse);
		if(flag){
			TtAsWrOldReturnedDetailPO idObj=new TtAsWrOldReturnedDetailPO();
			idObj.setId(Long.parseLong(id));
			TtAsWrOldReturnedDetailPO vo=new TtAsWrOldReturnedDetailPO();
			vo.setWarehouseRegion(wrHouse);
			vo.setLocalWarHouse(wrHouse.substring(0,1).toUpperCase());
			vo.setLocalWarShel(wrHouse.substring(1,4));
			vo.setLocalWarLayer(wrHouse.substring(4,5));
			int count  = dao.update(idObj,vo);
			if(count>0){
				act.setOutData("wrHouse",wrHouse.toUpperCase());
			}
		}else{
			result="输入的库位【"+wrHouse.toUpperCase()+"】在系统中不存在!";
		}
		act.setOutData("ID",id);
		act.setOutData("updateResult",result);
	}
	
	/**
	 * Function：索赔件审批入库--签收完成
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-17
	 */
	public void approveAndFinish(){
		
		
		
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		String id=request.getParamValue("id");//回运清单主键
		//String transNo=request.getParamValue("transNo");//货运单号
		int updateSuccessNum=0;
		try {
			params.put("company_id",String.valueOf(companyId));
			params.put("id",id);
			//params.put("transNo",transNo);
			params.put("logonId",String.valueOf(loginUser.getUserId()));
			updateSuccessNum=dao.SignFinishOper(params);//更改签收完成数据操作
			if(updateSuccessNum==1){
				act.setOutData("finishResult","finishSuccess");
			}else{
				act.setOutData("finishResult","finishFailure");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--签收完成");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件审批入库--查询明细
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-9
	 */
	public void queryDetail(){
		try {
			
			
			
			dao=ClaimApproveOldPartStoredDao.getInstance();
			Map<String,String> params=new HashMap<String, String>();
			// 处理当前页
			
			
					
			
			//回运要入库的回运清单号
			String claim_id=request.getParamValue("CLAIM_ID");//此处为回运清单中的主键
			params.put("i_claim_id", claim_id);
			
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo(params);
			request.setAttribute("returnListBean", returnListBean);
			
			//获取回运清单明细信息List
			List<TtAsWrOldPartSignDetailListBean> ps=dao.queryClaimBackDetailList3(params);
			request.setAttribute("detailList", ps);
			act.setOutData("ps", ps);
			
			//取得索赔单时间段
			TrReturnLogisticsPO po = new TrReturnLogisticsPO();
			po.setLogictisId(Long.parseLong(claim_id));
			TrReturnLogisticsPO poValue = (TrReturnLogisticsPO)dao.select(po).get(0);
			TtAsWrReturnedOrderPO popo = new TtAsWrReturnedOrderPO();
			popo.setId(poValue.getReturnId());
			TtAsWrReturnedOrderPO popoValue = (TtAsWrReturnedOrderPO)dao.select(popo).get(0);
			act.setOutData("time", popoValue.getWrStartDate());
			
			act.setForword(queryDetailPageUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--查询明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-04-27
	public void queryDetail11(){
		try {
			
			
			
			dao=ClaimApproveOldPartStoredDao.getInstance();
			Map<String,String> params=new HashMap<String, String>();
			// 处理当前页
			
			
					
			
			//回运要入库的回运清单号
			String claim_id=request.getParamValue("CLAIM_ID");//此处为回运清单中的主键
			params.put("i_claim_id", claim_id);
			
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo111(params);
			request.setAttribute("returnListBean", returnListBean);
			
			//获取回运清单明细信息List
			List<TtAsWrOldPartSignDetailListBean> ps=dao.queryClaimBackDetailList3(params);
			request.setAttribute("detailList", ps);
			act.setOutData("ps", ps);
			
			//得到索赔单提交时间段 begion
			TrReturnLogisticsPO po = new TrReturnLogisticsPO();
			po.setLogictisId(Long.parseLong(claim_id));
			TrReturnLogisticsPO poValue = (TrReturnLogisticsPO)dao.select(po).get(0);
			TtAsWrReturnedOrderPO popo = new TtAsWrReturnedOrderPO();
			popo.setId(poValue.getReturnId());
			TtAsWrReturnedOrderPO popoValue =(TtAsWrReturnedOrderPO)dao.select(popo).get(0);
			act.setOutData("time", popoValue.getWrStartDate());
			//得到索赔单提交时间段 end
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			
			act.setForword(queryDetailPageUrl11);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--查询明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void oldPartSignAuditDetail(){
		try {
			dao=ClaimApproveOldPartStoredDao.getInstance();
			Map<String,String> params=new HashMap<String, String>();
			//回运要入库的回运清单号
			String claim_id=request.getParamValue("CLAIM_ID");//此处为回运清单中的主键
			params.put("i_claim_id", claim_id);
			//获取回运清单明细信息List
			List<TtAsWrOldPartSignDetailListBean> ps=dao.queryClaimBackDetailList3(params);
			request.setAttribute("detailList", ps);
			act.setOutData("ps", ps);
			
			//得到索赔单提交时间段 begion
//			TrReturnLogisticsPO po = new TrReturnLogisticsPO();
//			po.setLogictisId(Long.parseLong(claim_id));
//			TrReturnLogisticsPO poValue = (TrReturnLogisticsPO)dao.select(po).get(0);
//			TtAsWrReturnedOrderPO popo = new TtAsWrReturnedOrderPO();
//			popo.setId(poValue.getReturnId());
//			TtAsWrReturnedOrderPO popoValue =(TtAsWrReturnedOrderPO)dao.select(popo).get(0);
//			act.setOutData("time", popoValue.getWrStartDate());
			//得到索赔单提交时间段 end
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			act.setForword(oldPartSignAuditDetail);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--查询明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件审批入库--在签收完成操作前检查工单是否完成签收操作
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-24 ZLD
	 */
	public void validateIsSignOper(){
		try {
			dao=ClaimApproveOldPartStoredDao.getInstance();
			Map params=new HashMap<String, String>();
			params.put("id", request.getParamValue("id"));
			params.put("idStr", request.getParamValue("idStr"));
			String retCode=dao.validateIsSignOper(params);
			act.setOutData("finishFlag",retCode);
			act.setOutData("id", request.getParamValue("id"));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--在签收完成操作前检查工单是否完成签收操作");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void goApproveAndStoredPageNoPage(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		
		//回运要入库的回运清单号
		String claim_id=request.getParamValue("CLAIM_ID");//此处为回运清单中的主键
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		try {
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =null;
			
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			act.setOutData("returnListBean", returnListBean);
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			act.setForword(this.approveAndStoredPageUrl2);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void detail(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		String claim_id = request.getParamValue("return_id");
		
		Map<String,String> params=new HashMap<String, String>();
		
		//回运要入库的回运清单号
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		try {
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			
			act.setOutData("returnListBean", returnListBean);
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			
			
			
			act.setForword(this.approveAndStoredPageUrl4);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		
		act.setForword(this.detail);
	}
	
	//根据箱号和回运单ID查询明细
	public void boxNoDetail(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		String claim_id = request.getParamValue("id");
		
		String boxNo = request.getParamValue("box_id");
		System.out.println(boxNo);
		Map<String,String> params=new HashMap<String, String>();
		
		String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));//
		String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//
		String back_order_no=CommonUtils.checkNull(request.getParamValue("back_order_no"));//
		String yieldly=CommonUtils.checkNull(request.getParamValue("yieldly"));//
		String freight_type=CommonUtils.checkNull(request.getParamValue("freight_type"));//
		String create_start_date=CommonUtils.checkNull(request.getParamValue("create_start_date"));//
		String create_end_date=CommonUtils.checkNull(request.getParamValue("create_end_date"));//
		String report_start_date=CommonUtils.checkNull(request.getParamValue("report_start_date"));//
		String report_end_date=CommonUtils.checkNull(request.getParamValue("report_end_date"));//
		String back_type=CommonUtils.checkNull(request.getParamValue("back_type"));//
		String trans_no=CommonUtils.checkNull(request.getParamValue("trans_no"));//
		String isReturn =CommonUtils.checkNull(request.getParamValue("isReturn"));//
		act.setOutData("dealerCode", dealerCode);
		act.setOutData("dealerName", dealerName);
		act.setOutData("back_order_no", back_order_no);
		act.setOutData("yieldly", yieldly);
		act.setOutData("freight_type", freight_type);
		act.setOutData("create_start_date", create_start_date);
		act.setOutData("create_end_date", create_end_date);
		act.setOutData("report_start_date", report_start_date);
		act.setOutData("report_end_date", report_end_date);
		act.setOutData("back_type", back_type);
		act.setOutData("trans_no", trans_no);
		act.setOutData("i_claim_id",claim_id);
		act.setOutData("boxNo", boxNo);
		
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		try {
			//得到运费
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(claim_id));
			TtAsWrOldReturnedPO popoValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
			act.setOutData("price_con", popoValue.getAuthPrice());
			act.setOutData("price_con1", popoValue.getPrice());
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			
			
			act.setOutData("listBoxNo", listBoxNo);
		
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			
			act.setOutData("returnListBean", returnListBean);
			if(isReturn==""){
				TcCodePO code = new TcCodePO() ;
				code.setType("8008") ;
				List list = dao.select(code) ;
				if(list.size()>0){
					code = (TcCodePO)list.get(0);
				}
				if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
					detailList=null;
				}
			}
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			
			//yyh 2012-4-18根据箱号和回运单号去查询明细
			List<Map<String, Object>> boxNoDetail= claimBackDao.getBoxNoDetail(claim_id,boxNo);
			
			act.setOutData("boxNoDetail", boxNoDetail);
			
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			
			act.setForword(this.storageBoxNoDetail);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	//根据箱号和回运单ID查询明细
	public void boxNoDetail1(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		String claim_id = request.getParamValue("id");
		
		String boxNo = request.getParamValue("box_id");
		System.out.println(boxNo);
		Map<String,String> params=new HashMap<String, String>();
		
		String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));//
		String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//
		String back_order_no=CommonUtils.checkNull(request.getParamValue("back_order_no"));//
		String yieldly=CommonUtils.checkNull(request.getParamValue("yieldly"));//
		String freight_type=CommonUtils.checkNull(request.getParamValue("freight_type"));//
		String create_start_date=CommonUtils.checkNull(request.getParamValue("create_start_date"));//
		String create_end_date=CommonUtils.checkNull(request.getParamValue("create_end_date"));//
		String report_start_date=CommonUtils.checkNull(request.getParamValue("report_start_date"));//
		String report_end_date=CommonUtils.checkNull(request.getParamValue("report_end_date"));//
		String back_type=CommonUtils.checkNull(request.getParamValue("back_type"));//
		String trans_no=CommonUtils.checkNull(request.getParamValue("trans_no"));//
		String isReturn =CommonUtils.checkNull(request.getParamValue("isReturn"));//
		act.setOutData("dealerCode", dealerCode);
		act.setOutData("dealerName", dealerName);
		act.setOutData("back_order_no", back_order_no);
		act.setOutData("yieldly", yieldly);
		act.setOutData("freight_type", freight_type);
		act.setOutData("create_start_date", create_start_date);
		act.setOutData("create_end_date", create_end_date);
		act.setOutData("report_start_date", report_start_date);
		act.setOutData("report_end_date", report_end_date);
		act.setOutData("back_type", back_type);
		act.setOutData("trans_no", trans_no);
		act.setOutData("i_claim_id",claim_id);
		act.setOutData("boxNo", boxNo);
		
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		try {
			//得到运费
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(claim_id));
			TtAsWrOldReturnedPO popoValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
			act.setOutData("price_con", popoValue.getAuthPrice());
			act.setOutData("price_con1", popoValue.getPrice());
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			
			
			act.setOutData("listBoxNo", listBoxNo);
		
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			
			act.setOutData("returnListBean", returnListBean);
			if(isReturn==""){
				TcCodePO code = new TcCodePO() ;
				code.setType("8008") ;
				List list = dao.select(code) ;
				if(list.size()>0){
					code = (TcCodePO)list.get(0);
				}
				if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
					detailList=null;
				}
			}
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			
			//yyh 2012-4-18根据箱号和回运单号去查询明细
			List<Map<String, Object>> boxNoDetail= claimBackDao.getBoxNoDetail(claim_id,boxNo);
			
			act.setOutData("boxNoDetail", boxNoDetail);
			
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			
			act.setForword(this.storageBoxNoDetail1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//条码扫描部分入库
	public void PartStorage(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		String claim_id = request.getParamValue("id");
		String boxNo = request.getParamValue("box_id");
		
		try {
			//取回运单起止时间
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			detailList1.get(0).get("WR_START_DATE");
			String START_DATE=detailList1.get(0).get("WR_START_DATE").toString().substring(11);
			//1.部分入库，先把状态修改
			dao.updateIsstorage(claim_id, boxNo);
		
			
			//2.判断是否已经完全入库
			
			String allStorage="0";
			List<Map<String,Object>> ls1=dao.queryIsstorage(claim_id);
			for(int i=0;i<ls1.size();i++){
				if(ls1.get(i).get("IS_STORAGE")==null){
					allStorage="1";
				}
			}
		
			
			//3.    0为完全入库（修改状态，经销商审核时间），1为部分入库（修改状态）
			if(allStorage.equals("0")){
				//记录审核运费
			dao.updateReceipt(claim_id,loginUser.getUserId(),request.getParamValue("price"));	
			dao.updateReviewDate(claim_id, START_DATE);
			
			
		/*
			
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(claim_id));
			popo.setInWarhouseDate(new Date());
			TtAsWrOldReturnedPO poValue = new TtAsWrOldReturnedPO();
			poValue.setAuthPrice(Double.parseDouble(request.getParamValue("price")));
			poValue.setUpdateBy(loginUser.getUserId());
			poValue.setUpdateDate(new Date());
			poValue.setInWarhouseDate(new Date());
			dao.update(popo, poValue);
			*/
			
			TtAsWrAuthPricePO po11 = new TtAsWrAuthPricePO();
			po11.setReturnId(Long.parseLong(claim_id));
			List poCon = dao.select(po11);
			if(poCon.size()<=0 || poCon==null){
				TtAsWrAuthPricePO po = new TtAsWrAuthPricePO();
				po.setId(Long.parseLong(SequenceManager.getSequence("")));
				po.setReturnId(Long.parseLong(claim_id));
				
				TtAsWrOldReturnedPO popo1 = new TtAsWrOldReturnedPO();
				popo1.setId(Long.parseLong(claim_id));
				TtAsWrOldReturnedPO poValue1 = (TtAsWrOldReturnedPO)dao.select(popo1).get(0);
				po.setYielyld(poValue1.getYieldly());
				po.setOldPrice(Double.parseDouble(request.getParamValue("price1")));
				po.setNewPrice(Double.parseDouble(request.getParamValue("price")));
				po.setDealerId(poValue1.getDealerId());
				TmDealerPO tpo = new TmDealerPO();
				tpo.setDealerId(poValue1.getDealerId());
				TmDealerPO tpoValue = (TmDealerPO)dao.select(tpo).get(0);
				po.setDealerCode(tpoValue.getDealerCode());
				po.setDealerName(tpoValue.getDealerName());
				po.setStatus(Constant.OLD_PRICE_1);
				po.setClaimbalanceId(-1L);
				po.setCreateBy(loginUser.getUserId());
				po.setCreateDate(new Date());
				dao.insert(po);
			}
			
			
			
			
			//记录运费备注  begin
			String yes_no = request.getParamValue("yes_no");
			if(Integer.parseInt(yes_no)==10041001){
				String priceRemark = request.getParamValue("priceRemark");
				if(!"".equals(priceRemark) && priceRemark!=null){
					TtAsWrOldReturnedPO po1 = new TtAsWrOldReturnedPO();
					po1.setId(Long.parseLong(claim_id));
					TtAsWrOldReturnedPO poValue1 = new TtAsWrOldReturnedPO();
					poValue1.setPriceRemark(priceRemark);
					dao.update(po1, poValue1);
				}
			}else{
				String priceRemark1 = request.getParamValue("priceRemark1");
				if(!"".equals(priceRemark1) && priceRemark1!=null){
					TtAsWrOldReturnedPO po2 = new TtAsWrOldReturnedPO();
					po2.setId(Long.parseLong(claim_id));
					TtAsWrOldReturnedPO poValue2 = new TtAsWrOldReturnedPO();
					poValue2.setPriceRemark(priceRemark1);
					dao.update(po2, poValue2);
				}
				
				
			}
			//end
			
			}
			else if(allStorage.equals("1")){
				dao.updatePartReceipt(claim_id);
			}
			
			//4.加入新的库存表
			/*
		TtAsWrOldReturnedDetailPO tawor=new TtAsWrOldReturnedDetailPO();
		tawor.setReturnId(Long.valueOf(claim_id));
		tawor.setBoxNo(boxNo);
		tawor.setIsUpload(Long.valueOf(Constant.STATUS_ENABLE));
		
		POFactory factory = POFactoryBuilder.getInstance();
		List<TtAsWrOldReturnedDetailPO> ls2= factory.select(tawor);
		*/
			
			List<Map<String,Object>> ls2=dao.queryNotDeduct(claim_id, boxNo);
			
		for(int i=0;i<ls2.size();i++){
			TtAsWrBarcodePartStockPO tawbp=new TtAsWrBarcodePartStockPO();
			
			tawbp.setCreateBy(loginUser.getUserId());
			tawbp.setCreateDate(new Date());
			tawbp.setPartId(Long.valueOf(ls2.get(i).get("PART_ID").toString()));
			tawbp.setPartCode(ls2.get(i).get("PART_CODE").toString());
			tawbp.setPartName(ls2.get(i).get("PART_NAME").toString());
			

				try {
					tawbp.setIsCliam(Long.valueOf(ls2.get(i).get("IS_CLIAM").toString()));
				} catch (Exception e) {
					
				}
			
				tawbp.setIsLibrary(Long.valueOf("0"));
			tawbp.setBarcodeNo(Long.valueOf(ls2.get(i).get("BARCODE_NO").toString()));
			tawbp.setStatus(Long.valueOf(Constant.STATUS_ENABLE));
			dao.insertStock(tawbp);
		}
		
		/*	List<Map<String,Object>> ls2=dao.queryOldDetail(claim_id, boxNo);
			System.out.println(ls2.size());
			for(int i=0;i<ls2.size();i++){
				TtAsWrBarcodePartStockPO tawbp=new TtAsWrBarcodePartStockPO();
				
				tawbp.setCreateBy(loginUser.getUserId());
				tawbp.setCreateDate(new Date());
				if(ls2.get(i).get("DEDUCTIBLE_REASON_CODE")!=null){
				tawbp.setDeductibleReasonCode(Long.valueOf(ls2.get(i).get("DEDUCTIBLE_REASON_CODE").toString()));
				}
				
				tawbp.setReturnId(Long.valueOf(claim_id));
				tawbp.setPartId(Long.valueOf(ls2.get(i).get("PART_ID").toString()));
				tawbp.setPartCode(ls2.get(i).get("PART_CODE").toString());
				tawbp.setPartName(ls2.get(i).get("PART_NAME").toString());
				tawbp.setYieldly(Long.valueOf(ls2.get(i).get("YIELDLY").toString()));
				tawbp.setBarcodeNo(Long.valueOf(ls2.get(i).get("BARCODE_NO").toString()));
				tawbp.setStatus(Long.valueOf(Constant.STATUS_ENABLE));
				dao.insertStock(tawbp);
			}
			*/
			
			Map<String,String> params=new HashMap<String, String>();
			String isReturn="";
			params.put("i_claim_id", claim_id);
			//params.put("boxNo", boxNo);
			//得到运费
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(claim_id));
			TtAsWrOldReturnedPO popoValue = (TtAsWrOldReturnedPO)dao.select(popo).get(0);
			act.setOutData("price_con", popoValue.getAuthPrice());
			act.setOutData("price_con1", popoValue.getPrice());
			//获取回运清单信息bean
			ClaimApproveAndStoredReturnInfoBean returnListBean=dao.getApproveAndStoredReturnInfo11(params);
			
			//获取回运清单明细信息List
			List<Map<String,Object>> detailList =dao.queryClaimBackDetailList2(params);
			
			//List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			
			List<Map<String, Object>> listBoxNo1 =claimBackDao.getBoxNo1(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("listBoxNo1", listBoxNo1);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			
			act.setOutData("returnListBean", returnListBean);
			if(isReturn==""){
				TcCodePO code = new TcCodePO() ;
				code.setType("8008") ;
				List list = dao.select(code) ;
				if(list.size()>0){
					code = (TcCodePO)list.get(0);
				}
				if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
					detailList=null;
				}
			}
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			
			act.setForword(this.approveAndStoredPageUrl411);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		
	}
	
	//条码扫描完全入库
	public void PartAllStorage(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		String claim_id = request.getParamValue("return_ord_id");
		
		
		try{
			//取回运单起止时间
			List<Map<String,Object>> detailList1 =dao.queryClaimBackDetailList3(claim_id);
			detailList1.get(0).get("WR_START_DATE");
			String START_DATE=detailList1.get(0).get("WR_START_DATE").toString().substring(11);
			
			//根据ID取箱号
			List<Map<String,Object>> ls=dao.queryIsstorage(claim_id);
			
			//把所有箱都入库
			for(int i=0;i<ls.size();i++){
				dao.updateIsstorage(claim_id, ls.get(i).get("BOX_NO").toString());
			}
			//记录审核运费
			dao.updateReceipt(claim_id,loginUser.getUserId(),request.getParamValue("price"));	
			dao.updateReviewDate(claim_id, START_DATE);
			
			//记录审核人，审核时间信息 Iverson 2010-01-04  begin
			TtAsWrReturnAuthitemPO reA = new TtAsWrReturnAuthitemPO();
			reA.setReturnId(Long.parseLong(claim_id));
			reA.setAuthStatus(Constant.BACK_LIST_STATUS_04);
			List list5 = dao.select(reA);
			if(list5.size()>0){
				System.out.println("有审核记录!");
			}else{
				ReturnStatusRecord.recordStatus(Long.parseLong(claim_id), loginUser.getUserId(), loginUser.getName(),loginUser.getOrgId(), Constant.BACK_LIST_STATUS_04,"");
			}
			//记录审核人，审核时间信息 Iverson 2010-01-04  end
			
			/*
			
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(claim_id));
			popo.setInWarhouseDate(new Date());
			TtAsWrOldReturnedPO poValue = new TtAsWrOldReturnedPO();
			poValue.setAuthPrice(Double.parseDouble(request.getParamValue("price")));
			poValue.setInWarhouseDate(new Date());
			poValue.setUpdateBy(loginUser.getUserId());
			poValue.setUpdateDate(new Date());
			dao.update(popo, poValue);
			
			*/
			TtAsWrAuthPricePO po11 = new TtAsWrAuthPricePO();
			po11.setReturnId(Long.parseLong(claim_id));
			List poCon = dao.select(po11);
			if(poCon.size()<=0 || poCon==null){
				TtAsWrAuthPricePO po = new TtAsWrAuthPricePO();
				po.setId(Long.parseLong(SequenceManager.getSequence("")));
				po.setReturnId(Long.parseLong(claim_id));
				
				TtAsWrOldReturnedPO popo1 = new TtAsWrOldReturnedPO();
				popo1.setId(Long.parseLong(claim_id));
				TtAsWrOldReturnedPO poValue1 = (TtAsWrOldReturnedPO)dao.select(popo1).get(0);
				po.setYielyld(poValue1.getYieldly());
				po.setOldPrice(Double.parseDouble(request.getParamValue("price1")));
				po.setNewPrice(Double.parseDouble(request.getParamValue("price")));
				po.setDealerId(poValue1.getDealerId());
				TmDealerPO tpo = new TmDealerPO();
				tpo.setDealerId(poValue1.getDealerId());
				TmDealerPO tpoValue = (TmDealerPO)dao.select(tpo).get(0);
				po.setDealerCode(tpoValue.getDealerCode());
				po.setDealerName(tpoValue.getDealerName());
				po.setStatus(Constant.OLD_PRICE_1);
				po.setClaimbalanceId(-1L);
				po.setCreateBy(loginUser.getUserId());
				po.setCreateDate(new Date());
				dao.insert(po);
			}
			
			
			
			
			//记录运费备注  begin
			String yes_no = request.getParamValue("yes_no");
			if(Integer.parseInt(yes_no)==10041001){
				String priceRemark = request.getParamValue("priceRemark");
				if(!"".equals(priceRemark) && priceRemark!=null){
					TtAsWrOldReturnedPO po1 = new TtAsWrOldReturnedPO();
					po1.setId(Long.parseLong(claim_id));
					TtAsWrOldReturnedPO poValue1 = new TtAsWrOldReturnedPO();
					poValue1.setPriceRemark(priceRemark);
					dao.update(po1, poValue1);
				}
			}else{
				String priceRemark1 = request.getParamValue("priceRemark1");
				if(!"".equals(priceRemark1) && priceRemark1!=null){
					TtAsWrOldReturnedPO po2 = new TtAsWrOldReturnedPO();
					po2.setId(Long.parseLong(claim_id));
					TtAsWrOldReturnedPO poValue2 = new TtAsWrOldReturnedPO();
					poValue2.setPriceRemark(priceRemark1);
					dao.update(po2, poValue2);
				}
				
				
			}
			//end
			//根据id取明细
			
			/*
			TtAsWrOldReturnedDetailPO tawor=new TtAsWrOldReturnedDetailPO();
			tawor.setReturnId(Long.valueOf(claim_id));
		
			tawor.setIsUpload(Long.valueOf(Constant.STATUS_ENABLE));
			
			POFactory factory = POFactoryBuilder.getInstance();
			List<TtAsWrOldReturnedDetailPO> ls2= factory.select(tawor);
			
			*/
			
			List<Map<String,Object>> ls2=dao.queryAllNotDeduct(claim_id);
			
				
			for(int i=0;i<ls2.size();i++){
				TtAsWrBarcodePartStockPO tawbp=new TtAsWrBarcodePartStockPO();
				
				tawbp.setCreateBy(loginUser.getUserId());
				tawbp.setCreateDate(new Date());
				
				
				tawbp.setReturnId(Long.valueOf(claim_id));
				
				try {
					tawbp.setIsCliam(Long.valueOf(ls2.get(i).get("IS_CLIAM").toString()));
				} catch (Exception e) {
				}
				
				
				
				tawbp.setPartId(Long.valueOf(ls2.get(i).get("PART_ID").toString()));
				tawbp.setPartCode(ls2.get(i).get("PART_CODE").toString());
				tawbp.setPartName(ls2.get(i).get("PART_NAME").toString());
				tawbp.setIsLibrary(Long.valueOf("0"));
				tawbp.setBarcodeNo(Long.valueOf(ls2.get(i).get("BARCODE_NO").toString()));
				tawbp.setStatus(Long.valueOf(Constant.STATUS_ENABLE));
				dao.insertStock(tawbp);
			}
			/*
			List<Map<String,Object>> ls2=dao.queryDetail(claim_id);
			
			for(int i=0;i<ls2.size();i++){
				TtAsWrBarcodePartStockPO tawbp=new TtAsWrBarcodePartStockPO();
				
				tawbp.setCreateBy(loginUser.getUserId());
				tawbp.setCreateDate(new Date());
				if(ls2.get(i).get("DEDUCTIBLE_REASON_CODE")!=null){
				tawbp.setDeductibleReasonCode(Long.valueOf(ls2.get(i).get("DEDUCTIBLE_REASON_CODE").toString()));
				}
				
				tawbp.setReturnId(Long.valueOf(claim_id));
				tawbp.setPartId(Long.valueOf(ls2.get(i).get("PART_ID").toString()));
				tawbp.setPartName(ls2.get(i).get("PART_NAME").toString());
				tawbp.setYieldly(Long.valueOf(ls2.get(i).get("YIELDLY").toString()));
				tawbp.setBarcodeNo(Long.valueOf(ls2.get(i).get("BARCODE_NO").toString()));
				tawbp.setStatus(Long.valueOf(Constant.STATUS_ENABLE));
				dao.insertStock(tawbp);
			}*/
			
			act.setOutData("updateResult","updateSuccess");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到入库页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//超期审核查询
	public void queryOverdueList(){
		try {
			
			
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			act.setOutData("yieldly", yieldly);
			//begin
			request=act.getRequest();
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));//
			String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//
			String back_order_no=CommonUtils.checkNull(request.getParamValue("back_order_no"));//
			String yieldly1=CommonUtils.checkNull(request.getParamValue("yieldly"));//
			String freight_type=CommonUtils.checkNull(request.getParamValue("freight_type"));//
			String create_start_date=CommonUtils.checkNull(request.getParamValue("create_start_date"));//
			String create_end_date=CommonUtils.checkNull(request.getParamValue("create_end_date"));//
			String report_start_date=CommonUtils.checkNull(request.getParamValue("report_start_date"));//
			String report_end_date=CommonUtils.checkNull(request.getParamValue("report_end_date"));//
			String back_type=CommonUtils.checkNull(request.getParamValue("back_type"));//
			String trans_no=CommonUtils.checkNull(request.getParamValue("trans_no"));//
			String isReturn=CommonUtils.checkNull(request.getParamValue("isReturn"));//
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("dealerName", dealerName);
			act.setOutData("back_order_no", back_order_no);
			act.setOutData("yieldly1", yieldly1);
			act.setOutData("freight_type", freight_type);
			act.setOutData("create_start_date", create_start_date);
			act.setOutData("create_end_date", create_end_date);
			act.setOutData("report_start_date", report_start_date);
			act.setOutData("report_end_date", report_end_date);
			act.setOutData("back_type", back_type);
			act.setOutData("trans_no", trans_no);
			act.setOutData("isReturn", isReturn);
			//end
			
			act.setForword(queryOverdueListUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryOverdueApproveList(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		Map<String,String> params=new HashMap<String, String>();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
				
		try {
			
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());//该用户拥有的产地
			String yieldly = request.getParamValue("yieldly");//查询条件=产地
			
			params.put("company_id",String.valueOf(companyId));
			params.put("i_dealer_code",request.getParamValue("dealerCode"));//经销商代码 
			params.put("i_dealer_name",request.getParamValue("dealerName"));//经销商简称
			params.put("i_return_no",request.getParamValue("back_order_no"));//回运清单号
			params.put("i_create_start_date",request.getParamValue("create_start_date"));//建单开始时间 
			params.put("i_create_end_date",request.getParamValue("create_end_date"));//建单结束时间
			params.put("i_submit_start_date",request.getParamValue("report_start_date"));//提报开始时间
			params.put("i_submit_end_date",request.getParamValue("report_end_date"));//提报结束时间
			params.put("i_transport_type",request.getParamValue("freight_type"));//货运方式
			params.put("i_back_type",request.getParamValue("back_type"));//回运单状态
			params.put("i_trans_no",request.getParamValue("trans_no"));//货运单号
			params.put("sfsm",request.getParamValue("sfsm"));//是否扫描
			params.put("yieldlys", yieldlys);
			params.put("yieldly", yieldly);
			
			PageResult<ClaimOldPartApproveStoreListBean> ps = dao.queryOverdueApproveList(params,getCurrPage(),
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(queryOverdueListUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void updateOverdue(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		try {
		String claim_id=	request.getParamValue("CLAIM_ID");
			
			dao.updateOverdue(claim_id);
			act.setForword(queryOverdueListUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		}
	public void updateOverdueCQ(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		try {
		String claim_id=	request.getParamValue("CLAIM_ID");
			
			dao.updateOverdueCQ(claim_id);
			act.setForword(queryOverdueListUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		}
	/**
	 * 配件索赔属性设置跳转
	 */
	public void partClaimSet(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(PART_CLAIM_SET_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件索赔属性设置");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 配件索赔属性查询
	 */
	public void partClaimQuery(){
		
		
		
		dao=ClaimApproveOldPartStoredDao.getInstance();
		try {
			String code = request.getParamValue("PART_CODE");
			String name = request.getParamValue("PART_NAME");
			String no  = request.getParamValue("PART_NO");
			String types = request.getParamValue("IS_CLAIM");
			PageResult<TmPtPartBasePO> ps= dao.getAllParts(code, name,loginUser.getPoseBusType(),no,types,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件索赔属性设置");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 配件索赔属性修改
	 */
	public void partClaimUpdate(){
		
		
		
		try {
		String id=	request.getParamValue("ID");
		Long types = 0l;
		String[]  ids = id.split(",");
		for(int i=0;i<ids.length;i++){
			TmPtPartBasePO bps = new TmPtPartBasePO();
			bps.setPartId(Long.valueOf(ids[i]));
			bps = (TmPtPartBasePO) dao.select(bps).get(0);
			types = bps.getIsCliam();
		if(types==Constant.IS_CLAIM_01.longValue()){
			types =Constant.IS_CLAIM_02.longValue();
		}else if(types==Constant.IS_CLAIM_02.longValue()){
			types =Constant.IS_CLAIM_01.longValue();
		}
		TmPtPartBasePO bp = new TmPtPartBasePO();
		TmPtPartBasePO bp2 = new TmPtPartBasePO();
		bp.setPartId(Long.valueOf(ids[i]));
		bp2.setIsCliam(types);
		dao.update(bp, bp2);
		}
		act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "配件索赔属性设置");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 配件回运设置跳转
	 */
	public void partReturnSet(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(PART_RETURN_SET_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件回运设置");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 配件回运查询
	 */
	public void partReturnQuery(){
		
		
		
		
		try {
			String code = request.getParamValue("PART_CODE");
			String name = request.getParamValue("PART_NAME");
			String no  = request.getParamValue("PART_NO");
			String types = request.getParamValue("IS_RETURN");
			
					
					
			PageResult<TmPtPartBasePO> ps= dao.getAllReturn(code, name,loginUser.getPoseBusType(),no,types,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件回运设置");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 配件回运修改
	 */
	public void partReturnUpdate(){
		
		try {
		String id=	request.getParamValue("ID");
		Integer types=0;
		String[] ids = id.split(",");
		for(int i=0;i<ids.length;i++){
			TmPtPartBasePO bps = new TmPtPartBasePO();
			bps.setPartId(Long.valueOf(ids[i]));
			bps = (TmPtPartBasePO) dao.select(bps).get(0);
			types = bps.getIsReturn();
			if(types==Constant.IS_RETURN_01.intValue()){
				types =Constant.IS_RETURN_02;
			}else if(types==Constant.IS_RETURN_02.intValue()){
				types =Constant.IS_RETURN_01;
			}
			TmPtPartBasePO bp = new TmPtPartBasePO();
			TmPtPartBasePO bp2 = new TmPtPartBasePO();
			bp.setPartId(Long.valueOf(ids[i]));
			bp2.setIsReturn(types);
			dao.update(bp, bp2);
		}
		act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "配件回运设置");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//旧件签收入库查询
	public void queryList(){
		try {
			
			
			
			dao  = ClaimApproveOldPartStoredDao.getInstance();
			List<TmBusinessAreaPO> list = dao.select(new TmBusinessAreaPO());
			request.setAttribute("yieldlyList", list);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			act.setOutData("yieldly", yieldly);
			
			//begin
			request=act.getRequest();
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));//
			String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//
			String back_order_no=CommonUtils.checkNull(request.getParamValue("back_order_no"));//
			String yieldly1=CommonUtils.checkNull(request.getParamValue("yieldly"));//
			String freight_type=CommonUtils.checkNull(request.getParamValue("freight_type"));//
			String create_start_date=CommonUtils.checkNull(request.getParamValue("create_start_date"));//
			String create_end_date=CommonUtils.checkNull(request.getParamValue("create_end_date"));//
			String report_start_date=CommonUtils.checkNull(request.getParamValue("report_start_date"));//
			String report_end_date=CommonUtils.checkNull(request.getParamValue("report_end_date"));//
			String back_type=CommonUtils.checkNull(request.getParamValue("back_type"));//
			String trans_no=CommonUtils.checkNull(request.getParamValue("trans_no"));//
			String isReturn=CommonUtils.checkNull(request.getParamValue("isReturn"));//
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("dealerName", dealerName);
			act.setOutData("back_order_no", back_order_no);
			act.setOutData("yieldly1", yieldly1);
			act.setOutData("freight_type", freight_type);
			act.setOutData("create_start_date", create_start_date);
			act.setOutData("create_end_date", create_end_date);
			act.setOutData("report_start_date", report_start_date);
			act.setOutData("report_end_date", report_end_date);
			act.setOutData("back_type", back_type);
			act.setOutData("trans_no", trans_no);
			act.setOutData("isReturn", isReturn);
			//end
	    		act.setForword(QUERY_LIST_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件审批入库--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//回运清单打印
	public void roMainPrint(){
		try {
			
			
			ClaimReturnReportDao dao=ClaimReturnReportDao.getInstance();
			String id = request.getParamValue("id");//获取回运单ID 
			String boxNo = request.getParamValue("boxNo");//装箱单号
			
			List<Map<String, Object>>  list = dao.getReturnNo(id);//获取回运通知单的主表信息
			List<Map<String, Object>>  list1 = dao.getReturnNo1(id);
			List<Map<String, Object>>  listDetail = dao.getReturnNoDetail(id,boxNo);//获取回运通知单的主表信息
			
			act.setOutData("list", list.get(0));
			act.setOutData("list1", list1);
			act.setOutData("detailList", listDetail);
			
			act.setOutData("boxNo", boxNo);
			
			act.setForword(queryBackListPrints);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void modSupp(){
		try {
			String id=request.getParamValue("id");
			String code=request.getParamValue("code");
			String oldcode=request.getParamValue("oldcode");
			String name = new String(request.getParamValue("name").getBytes("ISO-8859-1"),"UTF-8");
			String oldname = new String(request.getParamValue("oldname").getBytes("ISO-8859-1"),"UTF-8");
			TtPartMakerDefinePO p = new TtPartMakerDefinePO();
			p.setMakerCode(code);
			List<TtPartMakerDefinePO> list = dao.select(p);
			if(list!=null && list.size()>0){
				name = list.get(0).getMakerName();
			}
			TtAsWrOldReturnedDetailPO d = new TtAsWrOldReturnedDetailPO();
			TtAsWrOldReturnedDetailPO d2 = new TtAsWrOldReturnedDetailPO();
			d.setId(Long.valueOf(id));
			d2.setProducerCode(code);
			d2.setProducerName(name);
			dao.update(d, d2);
			//=================================增加修改日志
			 LogUpatePartProductCodePO codePO = new LogUpatePartProductCodePO();
			 codePO.setId(DaoFactory.getPkId());
			 codePO.setCreateDate(new Date());
			 codePO.setClaimId(Long.valueOf(id));
			 codePO.setPartCode(code);
			 codePO.setOldProducerCode(oldcode);
			 codePO.setOldProducerName(oldname);
			 codePO.setNewProducerCode(code);
			 codePO.setNewProducerName(name);
			 codePO.setUserId(loginUser.getUserId());
			 codePO.setUserName(loginUser.getName());
			 dao.insert(codePO);
			 //================================
			act.setOutData("msg", "succ");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--转到明细页面");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void approveAndStoredBack(){
		String return_ord_id = request.getParamValue("return_ord_id");
		try {
			
			TtAsWrOldReturnedPO popo = new TtAsWrOldReturnedPO();
			popo.setId(Long.parseLong(return_ord_id));
			TtAsWrOldReturnedPO poValue = new TtAsWrOldReturnedPO();
			poValue.setStatus(Constant.BACK_LIST_STATUS_01);
			dao.update(popo, poValue);
			//end
			act.setOutData("updateResult","updateSuccess");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件审批入库--签收操作");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	// 二次审核 初始化跳转
	public void oldSecondAuditQueryInit() {
		try {
			act = ActionContext.getContext();
			act.setForword(oldSecondAuditQueryInit_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二次审核 初始化跳转");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	// 二次抵扣查询
	public void oldSecondAuditQueryList() {
		dao = ClaimApproveOldPartStoredDao.getInstance();
		Map<String, String> params = new HashMap<String, String>();
		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser);
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))
				: 1;
		try {
			String yieldly = request.getParamValue("YIELDLY_TYPE");// 查询条件=产地

			params.put("company_id", String.valueOf(companyId));
			params.put("i_dealer_code", request.getParamValue("dealerCode"));// 经销商代码
			params.put("i_dealer_name", request.getParamValue("dealerName"));// 经销商简称
			params.put("i_return_no", request.getParamValue("back_order_no"));// 回运清单号
			params.put("i_submit_start_date", request.getParamValue("report_start_date"));// 提报开始时间
			params.put("i_submit_end_date", request.getParamValue("report_end_date"));// 提报结束时间
			params.put("i_transport_type", request.getParamValue("freight_type"));// 货运方式
			params.put("i_trans_no", request.getParamValue("trans_no"));// 货运单号
			params.put("transport_no", request.getParamValue("transport_no"));// 货运单号
			params.put("yieldly", yieldly);
			PageResult<ClaimOldPartApproveStoreListBean> ps = dao.oldPartSignQueryList(params, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二次抵扣查询结果--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void signAmountChange(){
		String id = CommonUtils.checkNull(request.getParamValue("id"));//旧件明细ID
		String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));//旧件清单ID
		String signAmount = CommonUtils.checkNull(request.getParamValue("signAmount"));//签收数
//		String deductRemark = CommonUtils.checkNull(request.getParamValue("deductRemark"));//抵扣原因
//		String otherRemark = CommonUtils.checkNull(request.getParamValue("otherRemark"));//其他原因
		String isMainCode = CommonUtils.checkNull(request.getParamValue("isMainCode"));//是否主因件
		String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));//索赔ID
		String claimPartId = CommonUtils.checkNull(request.getParamValue("claimPartId"));//索赔配件ID
		String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件ID
		String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页数
		
		StringBuffer sql = new StringBuffer("") ;
		List params = new ArrayList();
		try{
			if(signAmount.equals("0")){//如果签收数是0
				//修改旧件货运单清单明细的签收数为0,扣件原因默认无件,其他备注默认空
				sql.setLength(0);
				sql.append("UPDATE TT_AS_WR_RETURNED_ORDER_DETAIL A\n") ;
				sql.append("   SET A.SIGN_AMOUNT = 0,\n") ;
				sql.append("       A.DEDUCT_REMARK = ?,\n") ;
				sql.append("       A.OTHER_REMARK = ?\n") ;
				sql.append(" WHERE A.ID = ?\n") ;
				params.clear();
				params.add(Constant.OLDPART_DEDUCT_TYPE_06);//默认无件
				params.add(null);
				params.add(id);
                dao.update(sql.toString(), params);
                //修改索赔配件件表配件结算数量=配件结算数量-1,配件结算金额=配件结算金额减配件单价
                sql.setLength(0);
				sql.append("UPDATE TT_AS_WR_APP_PART A\n") ;
				sql.append("   SET A.PART_SETTLEMENT_NUM    = A.PART_SETTLEMENT_NUM - 1,\n") ;
				sql.append("       A.PART_SETTLEMENT_AMOUNT = A.PART_SETTLEMENT_AMOUNT - SALE_PRICE\n") ;
				sql.append(" WHERE A.CLAIM_PART_ID = ?\n") ;
				params.clear();
				params.add(claimPartId);
                dao.update(sql.toString(), params);
                if(isMainCode.equals(Constant.PART_BASE_FLAG_YES.toString())){//如果当前件是主因件
                	//对应的所有次因件旧件货运单清单明细，签收数改为0,扣件原因默认连带扣件,其他备注默认空
                	sql.setLength(0);
    				sql.append("UPDATE TT_AS_WR_RETURNED_ORDER_DETAIL A\n") ;
    				sql.append("   SET A.SIGN_AMOUNT = 0,\n") ;
    				sql.append("       A.DEDUCT_REMARK = ?,\n") ;
    				sql.append("       A.OTHER_REMARK = ?\n") ;
    				sql.append(" WHERE A.CLAIM_ID = ?\n") ;//同一索赔单
    				sql.append("   AND A.MAIN_PART_CODE = ?\n") ;//关联主因件
    				params.clear();
    				params.add(Constant.OLDPART_DEDUCT_TYPE_22);//默认连带扣件
    				params.add(null);
    				params.add(claimId);
    				params.add(partId);
                    dao.update(sql.toString(), params);
                    //修改索赔配件件表配件结算数量=0,配件结算金额=0
                    sql.setLength(0);
    				sql.append("UPDATE TT_AS_WR_APP_PART A\n") ;
    				sql.append("   SET A.PART_SETTLEMENT_NUM    = 0,\n") ;
    				sql.append("       A.PART_SETTLEMENT_AMOUNT = 0\n") ;
    				sql.append(" WHERE A.APP_CLAIM_ID = ?\n") ;//同一索赔单
    				sql.append("   AND A.RELATION_MAIN_PART = ?\n") ;//关联主因件
    				params.clear();
    				params.add(claimId);
    				params.add(partId);
                    dao.update(sql.toString(), params);
                }
			}else if(signAmount.equals("1")){//如果签收数是1
				//修改旧件货运单清单明细的签收数为0,扣件原因默认空,其他备注默认空
				sql.setLength(0);
				sql.append("UPDATE TT_AS_WR_RETURNED_ORDER_DETAIL A\n") ;
				sql.append("   SET A.SIGN_AMOUNT = 1,\n") ;
				sql.append("       A.DEDUCT_REMARK = ?,\n") ;
				sql.append("       A.OTHER_REMARK = ?\n") ;
				sql.append(" WHERE A.ID = ?\n") ;
				params.clear();
				params.add(null);
				params.add(null);
				params.add(id);
                dao.update(sql.toString(), params);
                //修改索赔配件件表配件结算数量=配件结算数量+1,配件结算金额=配件结算金额加配件单价
                sql.setLength(0);
				sql.append("UPDATE TT_AS_WR_APP_PART A\n") ;
				sql.append("   SET A.PART_SETTLEMENT_NUM    = A.PART_SETTLEMENT_NUM + 1,\n") ;
				sql.append("       A.PART_SETTLEMENT_AMOUNT = A.PART_SETTLEMENT_AMOUNT + SALE_PRICE\n") ;
				sql.append(" WHERE A.CLAIM_PART_ID = ?\n") ;
				params.clear();
				params.add(claimPartId);
                dao.update(sql.toString(), params);
                if(isMainCode.equals(Constant.PART_BASE_FLAG_YES.toString())){//如果当前件是主因件
                	//对应的所有次因件旧件货运单清单明细，签收数改为1,扣件原因默认空,其他备注默认空
                	sql.setLength(0);
    				sql.append("UPDATE TT_AS_WR_RETURNED_ORDER_DETAIL A\n") ;
    				sql.append("   SET A.SIGN_AMOUNT = 1,\n") ;
    				sql.append("       A.DEDUCT_REMARK = ?,\n") ;
    				sql.append("       A.OTHER_REMARK = ?\n") ;
    				sql.append(" WHERE A.CLAIM_ID = ?\n") ;//同一索赔单
    				sql.append("   AND A.MAIN_PART_CODE = ?\n") ;//关联主因件
    				params.clear();
    				params.add(null);
    				params.add(null);
    				params.add(claimId);
    				params.add(partId);
                    dao.update(sql.toString(), params);
                    //修改索赔配件件表配件结算数量=配件申请数量,配件结算金额=配件申请金额
                    sql.delete(0, sql.length()-1);
    				sql.append("UPDATE TT_AS_WR_APP_PART A\n") ;
    				sql.append("   SET A.PART_SETTLEMENT_NUM    = A.PART_NUM,\n") ;
    				sql.append("       A.PART_SETTLEMENT_AMOUNT = A.PART_APPLY_AMOUNT\n") ;
    				sql.append(" WHERE A.APP_CLAIM_ID = ?\n") ;//同一索赔单
    				sql.append("   AND A.RELATION_MAIN_PART = ?\n") ;//关联主因件
    				params.clear();
    				params.add(claimId);
    				params.add(partId);
                    dao.update(sql.toString(), params);
                }
			}
			//核实配件对应索赔单所对应的所有工时，如果工时下所有件结算数都为0，则工时结算金额=0，工时结算定额=0，否则工时结算金额=工时申请金额，工时结算定额=工时申请定额
			sql.setLength(0);
			sql.append("MERGE INTO TT_AS_WR_APP_PROJECT A\n") ;
			sql.append("USING (SELECT TTPROJECT.CLAIM_PROJECT_ID,\n") ;
			sql.append("              DECODE(NVL(SUM(TTPART.PART_SETTLEMENT_NUM), 0),\n") ;
			sql.append("                     0,\n") ;
			sql.append("                     0,\n") ;
			sql.append("                     TTPROJECT.LABOUR_HOUR) LABOUR_HOUR,\n") ;
			sql.append("              DECODE(NVL(SUM(TTPART.PART_SETTLEMENT_NUM), 0),\n") ;
			sql.append("                     0,\n") ;
			sql.append("                     0,\n") ;
			sql.append("                     TTPROJECT.HOURS_APPLY_AMOUNT) HOURS_APPLY_AMOUNT\n") ;
			sql.append("         FROM TT_AS_WR_APP_PROJECT TTPROJECT\n") ;
			sql.append("         JOIN TT_AS_WR_APP_PART TTPART\n") ;
			sql.append("           ON TTPROJECT.LABOUR_ID = TTPART.RELATION_LABOUR\n") ;
			sql.append("          AND TTPROJECT.APP_CLAIM_ID = TTPART.APP_CLAIM_ID\n") ;
			sql.append("          AND TTPROJECT.APP_CLAIM_ID = ?\n") ;
			sql.append("        GROUP BY TTPROJECT.CLAIM_PROJECT_ID,\n") ;
			sql.append("                 TTPROJECT.LABOUR_HOUR,\n") ;
			sql.append("                 TTPROJECT.HOURS_APPLY_AMOUNT) B\n") ;
			sql.append("ON (A.CLAIM_PROJECT_ID = B.CLAIM_PROJECT_ID)\n") ;
			sql.append("WHEN MATCHED THEN\n") ;
			sql.append("  UPDATE\n") ;
			sql.append("     SET A.LABOUR_SETTLEMENT_HOUR  = B.LABOUR_HOUR,\n") ;
			sql.append("         A.HOURS_SETTLEMENT_AMOUNT = B.HOURS_APPLY_AMOUNT\n") ;
			sql.append("   WHERE A.APP_CLAIM_ID = ?\n") ;
			params.clear();
			params.add(claimId);
			params.add(claimId);
            dao.update(sql.toString(), params);
            //核实主因件对应索赔单所对应的所有外出项目，如果外出项目对应的主因件结算金额为0，其结算金额也为0，否则其结算金额=申请金额
            sql.setLength(0);
            sql.append("MERGE INTO TT_AS_WR_APP_OUT A\n") ;
			sql.append("USING (SELECT TTOUT.OUT_ID,\n") ;
			sql.append("              DECODE(NVL(SUM(TTPART.PART_SETTLEMENT_NUM), 0),\n") ;
			sql.append("                     0,\n") ;
			sql.append("                     0,\n") ;
			sql.append("                     TTOUT.FEE_PRICE) FEE_PRICE\n") ;
			sql.append("         FROM TT_AS_WR_APP_OUT TTOUT\n") ;
			sql.append("         JOIN TT_AS_WR_APP_PART TTPART\n") ;
			sql.append("           ON TTOUT.FEE_RELATION_MAIN_PART = TTPART.PART_ID\n") ;
			sql.append("          AND TTOUT.APPCLAIM_ID = TTPART.APP_CLAIM_ID\n") ;
			sql.append("          AND TTOUT.APPCLAIM_ID = ?\n") ;
			sql.append("        GROUP BY TTOUT.OUT_ID, TTOUT.FEE_PRICE) B\n") ;
			sql.append("ON (A.OUT_ID = B.OUT_ID)\n") ;
			sql.append("WHEN MATCHED THEN\n") ;
			sql.append("  UPDATE\n") ;
			sql.append("     SET A.FEE_SETTLEMENT_PRICE = B.FEE_PRICE\n") ;
			sql.append("   WHERE A.APPCLAIM_ID = ?\n") ;
			params.clear();
			params.add(claimId);
			params.add(claimId);
            dao.update(sql.toString(), params);
            //根据工时、配件、外出费用，重新统计更新索赔表工时结算金额、配件结算金额、外出结算金额
            sql.setLength(0);
			sql.append("MERGE INTO TT_AS_WR_APPLICATION_CLAIM A\n") ;
			sql.append("USING (SELECT TTCLAIM.ID,\n") ;
			sql.append("              (SELECT NVL(SUM(TTPROJECT.HOURS_SETTLEMENT_AMOUNT), 0)\n") ;
			sql.append("                 FROM TT_AS_WR_APP_PROJECT TTPROJECT\n") ;
			sql.append("                WHERE TTCLAIM.ID = TTPROJECT.APP_CLAIM_ID) HOURS_SETTLEMENT_AMOUNT, --工时结算费用\n") ;
			sql.append("              (SELECT NVL(SUM(TTPART.PART_SETTLEMENT_AMOUNT), 0)\n") ;
			sql.append("                 FROM TT_AS_WR_APP_PART TTPART\n") ;
			sql.append("                WHERE TTCLAIM.ID = TTPART.APP_CLAIM_ID) PART_SETTLEMENT_AMOUNT, --配件结算费用\n") ;
			sql.append("              (SELECT NVL(SUM(TTOUT.FEE_SETTLEMENT_PRICE), 0)\n") ;
			sql.append("                 FROM TT_AS_WR_APP_OUT TTOUT\n") ;
			sql.append("                WHERE TTCLAIM.ID = TTOUT.APPCLAIM_ID) FEE_SETTLEMENT_PRICE --外出结算费用\n") ;
			sql.append("         FROM TT_AS_WR_APPLICATION_CLAIM TTCLAIM\n") ;
			sql.append("        WHERE 1=1\n") ;
			sql.append("          AND TTCLAIM.ID = ?) B\n") ;
			sql.append("ON (A.ID = B.ID)\n") ;
			sql.append("WHEN MATCHED THEN\n") ;
			sql.append("  UPDATE\n") ;
			sql.append("     SET A.HOURS_SETTLEMENT_AMOUNT   = B.HOURS_SETTLEMENT_AMOUNT,\n") ;
			sql.append("         A.PART_SETTLEMENT_AMOUNT    = B.PART_SETTLEMENT_AMOUNT,\n") ;
			sql.append("         A.OUTWARD_SETTLEMENT_AMOUNT = B.FEE_SETTLEMENT_PRICE\n") ;
			sql.append("   WHERE A.ID = ?\n") ;
			params.clear();
			params.add(claimId);
			params.add(claimId);
            dao.update(sql.toString(), params);
            //根据配件结算金额、工时结算金额、PDI结算金额、外出结算费用、保养结算金额、服务活动折扣后结算金额 重新计算 结算总金额
            sql.setLength(0);
			sql.append("UPDATE TT_AS_WR_APPLICATION_CLAIM A\n") ;
			sql.append("   SET A.SETTLEMENT_TOTAL_AMOUNT = NVL(A.PART_SETTLEMENT_AMOUNT, 0) +\n") ;
			sql.append("                                   NVL(A.HOURS_SETTLEMENT_AMOUNT, 0) +\n") ;
			sql.append("                                   NVL(A.PDI_SETTLEMENT_AMOUNT, 0) +\n") ;
			sql.append("                                   NVL(A.OUTWARD_SETTLEMENT_AMOUNT, 0) +\n") ;
			sql.append("                                   NVL(A.FIRST_SETTLEMENT_AMOUNT, 0) +\n") ;
			sql.append("                                   NVL(A.ACTIVITIE_SETTLEMENT_AMOUNT, 0)\n") ;
			sql.append("WHERE A.ID = ?\n") ;
			params.clear();
			params.add(claimId);
            dao.update(sql.toString(), params);
            //写入、更新旧件抵扣通知单表
            if(signAmount.equals("0")){//如果签收数是0,表示扣除
            	String deductionNo = OrderCodeManager.getOrderCode(92291066);//售后服务工单编码
            	sql.setLength(0);
				sql.append("MERGE INTO TT_AS_WR_OLDPART_DEDUCTION A\n") ;
				sql.append("USING (SELECT TAWAC.ID CLAIM_ID,\n") ;
				sql.append("              TAWAC.DEALER_ID,\n") ;
				sql.append("              NVL(TAWAC.PART_APPLY_AMOUNT, 0) -\n") ;
				sql.append("              NVL(TAWAC.PART_SETTLEMENT_AMOUNT, 0) PART_DEDUCTION_AMOUNT,\n") ;
				sql.append("              NVL(TAWAC.HOURS_APPLY_AMOUNT, 0) -\n") ;
				sql.append("              NVL(TAWAC.HOURS_SETTLEMENT_AMOUNT, 0) HOURS_DEDUCTION_AMOUNT,\n") ;
				sql.append("              NVL(TAWAC.OUTWARD_APPLY_AMOUNT, 0) -\n") ;
				sql.append("              NVL(TAWAC.OUTWARD_SETTLEMENT_AMOUNT, 0) OUTWARD_DEDUCTION_AMOUNT\n") ;
				sql.append("         FROM TT_AS_WR_APPLICATION_CLAIM TAWAC\n") ;
				sql.append("        WHERE 1=1\n") ;
				sql.append("              AND(NVL(TAWAC.PART_APPLY_AMOUNT, 0) -\n") ;
				sql.append("              NVL(TAWAC.PART_SETTLEMENT_AMOUNT, 0) +\n") ;
				sql.append("              NVL(TAWAC.HOURS_APPLY_AMOUNT, 0) -\n") ;
				sql.append("              NVL(TAWAC.HOURS_SETTLEMENT_AMOUNT, 0) +\n") ;
				sql.append("              NVL(TAWAC.OUTWARD_APPLY_AMOUNT, 0) -\n") ;
				sql.append("              NVL(TAWAC.OUTWARD_SETTLEMENT_AMOUNT, 0)) > 0\n") ;
				sql.append("          AND TAWAC.ID = ?) B\n") ;
				sql.append("ON (A.CLAIM_ID = B.CLAIM_ID)\n") ;
				sql.append("WHEN MATCHED THEN\n") ;
				sql.append("  UPDATE\n") ;
				sql.append("     SET A.PART_DEDUCTION_AMOUNT    = B.PART_DEDUCTION_AMOUNT,\n") ;
				sql.append("         A.HOURS_DEDUCTION_AMOUNT   = B.HOURS_DEDUCTION_AMOUNT,\n") ;
				sql.append("         A.OUTWARD_DEDUCTION_AMOUNT = B.OUTWARD_DEDUCTION_AMOUNT,\n") ;
				sql.append("         A.UPDATE_BY                = ?,\n") ;
				sql.append("         A.UPDATE_DATE              = SYSDATE\n") ;
				sql.append("WHEN NOT MATCHED THEN\n") ;
				sql.append("  INSERT\n") ;
				sql.append("    (DEDUCTION_ID,\n") ;
				sql.append("     DEDUCTION_NO,\n") ;
				sql.append("     CLAIM_ID,\n") ;
				sql.append("     DEALER_ID,\n") ;
				sql.append("     DEDUCTION_TYPE,\n") ;
				sql.append("     PART_DEDUCTION_AMOUNT,\n") ;
				sql.append("     HOURS_DEDUCTION_AMOUNT,\n") ;
				sql.append("     OUTWARD_DEDUCTION_AMOUNT,\n") ;
				sql.append("     CREATE_BY,\n") ;
				sql.append("     CREATE_DATE)\n") ;
				sql.append("  VALUES\n") ;
				sql.append("    (F_GETID(),\n") ;
				sql.append("     ?,\n") ;
				sql.append("     B.CLAIM_ID,\n") ;
				sql.append("     B.DEALER_ID,\n") ;
				sql.append("     ?,\n") ;
				sql.append("     B.PART_DEDUCTION_AMOUNT,\n") ;
				sql.append("     B.HOURS_DEDUCTION_AMOUNT,\n") ;
				sql.append("     B.OUTWARD_DEDUCTION_AMOUNT,\n") ;
				sql.append("     ?,\n") ;
				sql.append("     SYSDATE)\n") ;
				params.clear();
				params.add(claimId);
				params.add(loginUser.getUserId());
				params.add(deductionNo);
				params.add(Constant.DEDUCTION_TYPE_01);//一次抵扣
				params.add(loginUser.getUserId());
	            dao.update(sql.toString(), params);
            }else if(signAmount.equals("1")){//如果签收数是1,表示不扣除
            	sql.setLength(0);
            	sql.append("MERGE INTO TT_AS_WR_OLDPART_DEDUCTION A\n") ;
				sql.append("USING (SELECT TAWAC.ID CLAIM_ID,\n") ;
				sql.append("              TAWAC.DEALER_ID,\n") ;
				sql.append("              NVL(TAWAC.PART_APPLY_AMOUNT, 0) -\n") ;
				sql.append("              NVL(TAWAC.PART_SETTLEMENT_AMOUNT, 0) PART_DEDUCTION_AMOUNT,\n") ;
				sql.append("              NVL(TAWAC.HOURS_APPLY_AMOUNT, 0) -\n") ;
				sql.append("              NVL(TAWAC.HOURS_SETTLEMENT_AMOUNT, 0) HOURS_DEDUCTION_AMOUNT,\n") ;
				sql.append("              NVL(TAWAC.OUTWARD_APPLY_AMOUNT, 0) -\n") ;
				sql.append("              NVL(TAWAC.OUTWARD_SETTLEMENT_AMOUNT, 0) OUTWARD_DEDUCTION_AMOUNT\n") ;
				sql.append("         FROM TT_AS_WR_APPLICATION_CLAIM TAWAC\n") ;
				sql.append("        WHERE 1=1\n") ;
				sql.append("          AND TAWAC.ID = ?) B\n") ;
				sql.append("ON (A.CLAIM_ID = B.CLAIM_ID)\n") ;
				sql.append("WHEN MATCHED THEN\n") ;
				sql.append("  UPDATE\n") ;
				sql.append("     SET A.PART_DEDUCTION_AMOUNT    = B.PART_DEDUCTION_AMOUNT,\n") ;
				sql.append("         A.HOURS_DEDUCTION_AMOUNT   = B.HOURS_DEDUCTION_AMOUNT,\n") ;
				sql.append("         A.OUTWARD_DEDUCTION_AMOUNT = B.OUTWARD_DEDUCTION_AMOUNT,\n") ;
				sql.append("         A.UPDATE_BY                = ?,\n") ;
				sql.append("         A.UPDATE_DATE              = SYSDATE\n") ;
				sql.append("  DELETE\n") ;
				sql.append("   WHERE NVL(A.PART_DEDUCTION_AMOUNT, 0) + NVL(A.HOURS_DEDUCTION_AMOUNT, 0) +\n") ;
				sql.append("         NVL(A.OUTWARD_DEDUCTION_AMOUNT, 0) = 0\n") ;

//            	sql.append("DELETE TT_AS_WR_OLDPART_DEDUCTION A\n") ;
//                sql.append(" WHERE 1 = 1\n") ;
//                sql.append("   AND A.CLAIM_ID = ?\n") ;
//                sql.append("   AND EXISTS (SELECT 1\n") ;
//                sql.append("          FROM TT_AS_WR_APPLICATION_CLAIM TAWAC\n") ;
//                sql.append("         WHERE A.CLAIM_ID = TAWAC.ID\n") ;
//                sql.append("           AND NVL(TAWAC.PART_APPLY_AMOUNT, 0) =\n") ;
//                sql.append("               NVL(TAWAC.PART_SETTLEMENT_AMOUNT, 0)\n") ;
//                sql.append("           AND NVL(TAWAC.HOURS_APPLY_AMOUNT, 0) =\n") ;
//                sql.append("               NVL(TAWAC.HOURS_SETTLEMENT_AMOUNT, 0)\n") ;
//                sql.append("           AND NVL(TAWAC.OUTWARD_APPLY_AMOUNT, 0) =\n") ;
//                sql.append("               NVL(TAWAC.OUTWARD_SETTLEMENT_AMOUNT, 0))\n") ;
				params.clear();
				params.add(claimId);
				params.add(loginUser.getUserId());
	            dao.update(sql.toString(), params);
            }
            act.setOutData("curPage", curPage);
        	act.setOutData("code", "succ");
			act.setOutData("msg", "操作成功");
		} catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存售后服务工单信息保存数据异常.");
            act.setException(e1);
            act.setOutData("curPage", curPage);
        	act.setOutData("code", "fail");
			act.setOutData("msg", "保存失败!"+e1);
			e.printStackTrace();
        }
	}
	
	public void deductRemarkChange(){
		String id = CommonUtils.checkNull(request.getParamValue("id"));//旧件明细ID
		String deductRemark = CommonUtils.checkNull(request.getParamValue("deductRemark"));//抵扣原因
		String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页数
		
		StringBuffer sql = new StringBuffer("") ;
		List params = new ArrayList();
		try{
            //修改扣件原因
            sql.setLength(0);
            sql.append("UPDATE TT_AS_WR_RETURNED_ORDER_DETAIL A\n") ;
			sql.append("   SET A.DEDUCT_REMARK = ?,\n") ;
			sql.append("       A.OTHER_REMARK = ?\n") ;
			sql.append(" WHERE A.ID = ?\n") ;
			params.clear();
			params.add(deductRemark);
			params.add(null);
			params.add(id);
            dao.update(sql.toString(), params);
            
            act.setOutData("id", id);
            act.setOutData("deductRemark", deductRemark);
            act.setOutData("curPage", curPage);
        	act.setOutData("code", "succ");
			act.setOutData("msg", "操作成功");
		} catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存售后服务工单信息保存数据异常.");
            act.setException(e1);
            act.setOutData("curPage", curPage);
        	act.setOutData("code", "fail");
			act.setOutData("msg", "保存失败!"+e1);
			e.printStackTrace();
        }
	}
	
	public void otherRemarkChange(){
		String id = CommonUtils.checkNull(request.getParamValue("id"));//旧件明细ID
		String otherRemark = CommonUtils.checkNull(request.getParamValue("otherRemark"));//抵扣原因
		String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页数
		try{
			StringBuffer sql = new StringBuffer("") ;
			List<Object> params = new ArrayList<Object>();
            //修改扣件原因
            sql.setLength(0);
            sql.append("UPDATE TT_AS_WR_RETURNED_ORDER_DETAIL A\n") ;
			sql.append("   SET A.OTHER_REMARK = ?\n") ;
			sql.append(" WHERE A.ID = ?\n") ;
			params.clear();
			params.add(otherRemark);
			params.add(id);
            dao.update(sql.toString(), params);
            
            act.setOutData("curPage", curPage);
        	act.setOutData("code", "succ");
			act.setOutData("msg", "操作成功");
		} catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存售后服务工单信息保存数据异常.");
            act.setException(e1);
            act.setOutData("curPage", curPage);
        	act.setOutData("code", "fail");
			act.setOutData("msg", "保存失败!"+e1);
			e.printStackTrace();
        }
	}
	/**
	 * @description 责任供应商选择弹窗
	 * @Date 2017-09-01
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void chooseProducerWin(){
		try {
			String id = CommonUtils.checkNull(request.getParamValue("id"));//旧件清单明细表ID
			String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));//索赔单ID
			String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件ID
			String isMainCode = CommonUtils.checkNull(request.getParamValue("isMainCode"));//是否主因件
			
			act.setOutData("id", id);
			act.setOutData("claimId", claimId);
			act.setOutData("partId", partId);
			act.setOutData("isMainCode",isMainCode);
			act.setForword(chooseProducerWin_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "责任供应商列表");
			act.setActionReturn(e1);
		}
	}
	/**
	 * @description 责任供应商查询
	 * @Date 2017-09-01
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void producerInfoQuery(){
		try {
			//页面参数
			String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件ID
			String venderCode = CommonUtils.checkNull(request.getParamValue("venderCode"));//责任供应商编码
			String venderName = CommonUtils.checkNull(request.getParamValue("venderName"));//责任供应商名称
			
            Map<String,Object>params = new HashMap<String, Object>();
			
			params.put("partId", partId);
			params.put("venderCode", venderCode);
			params.put("venderName", venderName);
			
			params.put("loginUserId", CommonUtils.checkNull(loginUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(loginUser.getDealerId()));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
		    PageResult<Map<String,Object>>ps = dao.producerInfoQuery(params, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "责任供应商查询");
			act.setActionReturn(e1);
		}
	}
	/**
	 * @description 责任供应商保存
	 * @Date 2017-09-01
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void producerInfoSave(){
//		String id = CommonUtils.checkNull(request.getParamValue("id"));//旧件明细ID
		String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));//索赔单ID
		String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件ID
		String isMainCode = CommonUtils.checkNull(request.getParamValue("isMainCode"));//是否主因件
		String venderCode = CommonUtils.checkNull(request.getParamValue("venderCode"));//责任供应商编码
		String venderName = CommonUtils.checkNull(request.getParamValue("venderName"));//责任供应商名称
		String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页数
		try{
			StringBuffer sql = new StringBuffer("") ;
			List<Object> params = new ArrayList<Object>();
            //修改同一索赔单下所有相同配件责任供应商
            sql.setLength(0);
            sql.append("UPDATE TT_AS_WR_RETURNED_ORDER_DETAIL A\n") ;
			sql.append("   SET A.PRODUCER_CODE = ?,\n") ;
			sql.append("       A.PRODUCER_NAME = ?\n") ;
//			sql.append(" WHERE A.ID = ?\n") ;
			sql.append(" WHERE A.PART_ID = ?\n") ;//同一配件
			sql.append("   AND A.CLAIM_ID = ?\n") ;//同一索赔单
			params.clear();
			params.add(venderCode);
			params.add(venderName);
			params.add(partId);
			params.add(claimId);
            dao.update(sql.toString(), params);
            if(isMainCode.equals(Constant.IF_TYPE_YES.toString())){//如果是主因件
            	//修改同一索赔单下主因件对应所有次因件责任供应商
            	sql.setLength(0);
                sql.append("UPDATE TT_AS_WR_RETURNED_ORDER_DETAIL A\n") ;
    			sql.append("   SET A.PRODUCER_CODE = ?,\n") ;
    			sql.append("       A.PRODUCER_NAME = ?\n") ;
    			sql.append(" WHERE A.MAIN_PART_CODE = ?\n") ;//主因件
    			sql.append("   AND A.CLAIM_ID = ?\n") ;//同一索赔单
    			params.clear();
    			params.add(venderCode);
    			params.add(venderName);
    			params.add(partId);
    			params.add(claimId);
                dao.update(sql.toString(), params);
            }
            act.setOutData("curPage", curPage);
        	act.setOutData("code", "succ");
			act.setOutData("msg", "操作成功");
		} catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存售后服务工单信息保存数据异常.");
            act.setException(e1);
            act.setOutData("curPage", curPage);
        	act.setOutData("code", "fail");
			act.setOutData("msg", "保存失败!"+e1);
			e.printStackTrace();
        }
	}
	/**
	 * @description 审核入库
	 * @Date 2017-09-01
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void inWarhouseStatusSave(){
		String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
		StringBuffer sql = new StringBuffer("") ;
		List<Object> params = new ArrayList<Object>();
		Map<String,Object> parMap = new HashMap<String,Object>();
//		String validateId = "";
		try{
//			String id = CommonUtils.checkNull(request.getParamValue("id"));//旧件明细ID
			String inWarhouseType = CommonUtils.checkNull(request.getParamValue("inWarhouseType"));//审核入库类型
			String claimId = CommonUtils.checkNull(request.getParamValue("CLAIM_ID"));//旧件清单ID
			String[] orderIds = checkNullArray(request.getParamValues("orderIds"));//旧件清单明细ID
			List<String> orderIdList = new ArrayList<String>();
			String orderIdStr = "";
			if(inWarhouseType.equals(Constant.BACK_LIST_STATUS_04.toString())){//部分审核
				//转换为旧件清单ID串
				for(int i=0;i<orderIds.length;i++){
					orderIdStr += "," + orderIds[i];
					if(i>0&&i%5==0){
						orderIdList.add(orderIdStr.substring(1));
						orderIdStr = "";
					}
				}
				if(!orderIdStr.equals("")){
					orderIdList.add(orderIdStr.substring(1));
				}
				//如果存在 抵扣原因是其他 且其他说明为空 的数据 则不更新数据
				for(int i=0;i<orderIdList.size();i++){
					parMap.clear();
					parMap.put("id", orderIdList.get(i));
					List<Map<String, Object>> validateDeductRemark = dao.validateDeductRemark(parMap);
					if(validateDeductRemark!=null&&validateDeductRemark.size()>0){
						act.setOutData("curPage", curPage);
			        	act.setOutData("code", "fail");
			        	act.setOutData("inWarhouseType", "inWarhouseType");
						act.setOutData("msg", "审核入库失败,当前审核数据选择了其他抵扣原因，但未填写具体其他原因.");
						return;
					}
				}
				for(int i=0;i<orderIdList.size();i++){
					//根据传入的旧件清单明细ID更新旧件明细审核入库状态
		            sql.setLength(0);
		            sql.append("UPDATE TT_AS_WR_RETURNED_ORDER_DETAIL A\n") ;
					sql.append("   SET A.IN_WARHOUSE_BY = ?,\n") ;
					sql.append("       A.IN_WARHOUSE_DATE = SYSDATE,\n") ;
					sql.append("       A.IN_WARHOUSE_STATUS = ?\n") ;
					sql.append(" WHERE A.ID IN ("+orderIdList.get(i)+")\n") ;
					params.clear();
					params.add(loginUser.getUserId());
					params.add(Constant.PART_BASE_FLAG_YES);
//					params.add("("+orderIdList.get(i)+")");
		            dao.update(sql.toString(), params);
				}
				sql.setLength(0);
				//根据传入的旧件清单ID，更新清单状态为部分审核入库
	            sql.append("UPDATE TT_AS_WR_RETURNED_ORDER A\n") ;
				sql.append("   SET A.IN_WARHOUSE_BY = ?,\n") ;
				sql.append("       A.IN_WARHOUSE_DATE = SYSDATE,\n") ;
				sql.append("       A.STATUS = ?\n") ;
				sql.append(" WHERE A.ID = ?\n") ;
				params.clear();
				params.add(loginUser.getUserId());
				params.add(inWarhouseType);
				params.add(claimId);
	            dao.update(sql.toString(), params);
			}else if(inWarhouseType.equals(Constant.BACK_LIST_STATUS_05.toString())){//全部审核
				parMap.clear();
				parMap.put("claimId", claimId);
				List<Map<String, Object>> validateDeductRemark = dao.validateDeductRemark(parMap);
				if(validateDeductRemark!=null&&validateDeductRemark.size()>0){
					act.setOutData("curPage", curPage);
		        	act.setOutData("code", "fail");
		        	act.setOutData("inWarhouseType", "inWarhouseType");
					act.setOutData("msg", "审核入库失败,当前审核数据选择了其他抵扣原因，但未填写具体其他原因.");
					return;
				}
				//根据传入的旧件清单ID更新旧件明细审核入库状态
				sql.setLength(0);
	            sql.append("UPDATE TT_AS_WR_RETURNED_ORDER_DETAIL A\n") ;
				sql.append("   SET A.IN_WARHOUSE_BY = ?,\n") ;
				sql.append("       A.IN_WARHOUSE_DATE = SYSDATE,\n") ;
				sql.append("       A.IN_WARHOUSE_STATUS = ?\n") ;
				sql.append(" WHERE A.RETURN_ID = ?\n") ;
				params.clear();
				params.add(loginUser.getUserId());
				params.add(Constant.PART_BASE_FLAG_YES);
				params.add(claimId);
	            dao.update(sql.toString(), params);
	            //根据传入的旧件清单ID，更新清单状态为全部审核入库
	            sql.setLength(0);
	            sql.append("UPDATE TT_AS_WR_RETURNED_ORDER A\n") ;
				sql.append("   SET A.IN_WARHOUSE_BY = ?,\n") ;
				sql.append("       A.IN_WARHOUSE_DATE = SYSDATE,\n") ;
				sql.append("       A.STATUS = ?\n") ;
				sql.append(" WHERE A.ID = ?\n") ;
				params.clear();
				params.add(loginUser.getUserId());
				params.add(inWarhouseType);
				params.add(claimId);
	            dao.update(sql.toString(), params);
			}
            act.setOutData("curPage", curPage);
        	act.setOutData("code", "succ");
        	act.setOutData("inWarhouseType", "inWarhouseType");
			act.setOutData("msg", "审核入库成功.");
		} catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "审核入库失败.");
            act.setException(e1);
            act.setOutData("curPage", curPage);
        	act.setOutData("code", "fail");
        	act.setOutData("inWarhouseType", "inWarhouseType");
			act.setOutData("msg", "审核入库失败!"+e1);
			e.printStackTrace();
        }
	}
	
	/**
	 * @description 公用方法,数组NULL转EMPTY
	 * @Date 2017-08-09
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * @throws ParseException 
	 * */
	public String[] checkNullArray(String[] array) throws ParseException {
		if(array==null){
			array = EMPTY_ARRAY;
		}
		return array;
	}
}
