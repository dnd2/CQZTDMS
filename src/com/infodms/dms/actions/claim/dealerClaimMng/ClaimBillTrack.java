package com.infodms.dms.actions.claim.dealerClaimMng;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.infodms.dms.actions.claim.application.ClaimManualAuditing;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimListBean;
import com.infodms.dms.bean.DealerCoverPrintBean;
import com.infodms.dms.bean.LabourPartBean;
import com.infodms.dms.bean.TtAsWrApplicationExtBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.application.ClaimManualAuditingDao;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmBusinessChngCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TrBalanceClaimPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityProjectPO;
import com.infodms.dms.po.TtAsDealerCoverDetailPO;
import com.infodms.dms.po.TtAsDealerTypePO;
import com.infodms.dms.po.TtAsRoAddItemPO;
import com.infodms.dms.po.TtAsRoLabourPO;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.po.TtAsWrAppauthitemPO;
import com.infodms.dms.po.TtAsWrApplicationBackupPO;
import com.infodms.dms.po.TtAsWrApplicationCounterPO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrAuthinfoPO;
import com.infodms.dms.po.TtAsWrLabouritemCounterPO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infodms.dms.po.TtAsWrNetitemExtPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrOtherfeePO;
import com.infodms.dms.po.TtAsWrOutrepairPO;
import com.infodms.dms.po.TtAsWrPartsitemCounterPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtAsWrWrauthorizationPO;
import com.infodms.dms.po.TtClaimAccessoryDtlPO;
import com.infodms.dms.service.ClaimBillTrackService;
import com.infodms.dms.service.impl.ClaimBillTrackServiceImpl;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.service.ClaimService;
import com.infodms.yxdms.service.impl.ClaimServiceImpl;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
* @ClassName: ClaimBillTrack 
* @Description: TODO(索赔申请单跟踪) 
* @author wangchao 
* @date Jun 10, 2010 1:26:45 PM 
*
 */
@SuppressWarnings("unchecked")
public class ClaimBillTrack extends BaseAction {
	
	private final ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
	private final String CLAIM_BILL_URL = "/jsp/claim/dealerClaimMng/claimBillTrack.jsp";// 主页面（查询）
	private final String CLAIM_BILL_PRINT_URL = "/jsp/claim/dealerClaimMng/claimBillTrackPrint.jsp";// 导出
	private final String CLAIM_BILL_DEL_URL = "/jsp/claim/dealerClaimMng/claimBillDeleteTrack.jsp";// 删除主页面(查询)Iverson add with 2010-11-12
	private final String CLAIM_BILL_DETAIL_URL = "/jsp/claim/dealerClaimMng/claimBillDetail.jsp";// 明细页面
	private final String CLAIM_BILL_DETAIL_URL2 = "/jsp/claim/dealerClaimMng/claimBillDetail2.jsp";// 明细页面
	private final String CLAIM_BILL_DEL_DETAIL_URL = "/jsp/claim/dealerClaimMng/claimBillDeteleDetail.jsp";// 明细页面
	private final String CLAIM_BILL_DEL_DETAIL_URL2 = "/jsp/claim/dealerClaimMng/claimBillDeteleDetail2.jsp";// 明细页面
	private final String CLAIM_BILL_DEL_DETAIL_URL1 = "/jsp/claim/dealerClaimMng/claimBillDeteleDetail1.jsp";// 明细页面
	private final String CLAIM_BILL_RO_NO_DETAIL_URL = "/jsp/claim/dealerClaimMng/claimBillRoNoDetail.jsp";// 维修历史页面
	private final String CLAIM_BILL_VIN_URL ="/jsp/claim/dealerClaimMng/vehiclehistorydetail.jsp";//VIN码查询
	private final String PRINT_PART_URL = "/jsp/claim/dealerClaimMng/printClaimPartLabel.jsp";//配件标签打印
	
	private final String BARCODE_PRINT = "/jsp/claim/dealerClaimMng/barcode_Print.jsp";//条码打印查询
	
	private final String barcodePrintDoGet= "/jsp/claim/dealerClaimMng/barcode_print_doget.jsp";//条码打印
	private final String dealerCoverPrintPer= "/jsp/claim/dealerClaimMng/dealerCoverPrintPer.jsp";// 
	private final String dealerCoversave= "/jsp/claim/dealerClaimMng/dealerCoversave.jsp";//生成封面
	private final String dealerCoverSearch= "/jsp/claim/dealerClaimMng/dealerCoverSearch.jsp";
	private final String dealerCoversave2= "/jsp/claim/dealerClaimMng/dealerCoversave2.jsp";//生成封面
	private final String CLAIM_COUNTER_SYSTEM="/jsp/claim/dealerClaimMng/claimCounterSystem.jsp";//反索赔管理主页面（查询）
	private final String CLAIM_COUNTER_SELECT="/jsp/claim/dealerClaimMng/claimCounterSelect.jsp"; //反索赔查看主页面（查询）
	private final String CLAIM_SYSTEM_DETAIL_URL = "/jsp/claim/dealerClaimMng/claimSystemDetail.jsp";// 明细页面
	private final String CLAIM_SELECT_DETAIL_URL = "/jsp/claim/dealerClaimMng/claimSelectDetail.jsp";// 明细页面
	
	private final String CLAIM_DETAIL_URL = "/jsp/claim/application/claimDetail.jsp";// 申请明细主页面（查询）
	
	private final String COUNTER_EDIT = "/jsp/claim/dealerClaimMng/claimCounterEdit.jsp";  //新的反索赔页面
	/**
	 * 
	 * @Title: claimBillForword
	 * @Description: TODO(索赔单主页跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void claimBillForward() {
		CommonUtilActions commonUtilActions = new CommonUtilActions();
		try {
			RequestWrapper request = act.getRequest();
			TmBusinessAreaPO p = new TmBusinessAreaPO();
			List<TmBusinessAreaPO> list = dao.select(p);
			request.setAttribute("yieldly", list);
			//车型
			act.setOutData("modelList", commonUtilActions.getAllModel());
			
				act.setForword(CLAIM_BILL_URL);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Iverson add with 2010-11-12
	 * @Title: claimBillDeleteForward
	 * @Description: TODO(索赔单删除主页面跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void claimBillDeleteForward() {
		
		
				
		try {
			act.setForword(CLAIM_BILL_DEL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单删除");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title: applicationQuery
	 * @Description: TODO(查询索赔单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void applicationQuery() {
		
		
				
		String dealerId = loginUser.getDealerId();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);     //公司ID
		StringBuffer con = new StringBuffer();
		Map<String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String claimNo = request.getParamValue("CLAIM_NO");
			String roNo = request.getParamValue("RO_NO");
			String lineNo = request.getParamValue("LINE_NO");
			String claimType = request.getParamValue("CLAIM_TYPE");
			String vin = request.getParamValue("VIN");
			String roStartdate = request.getParamValue("RO_STARTDATE");
			String roEnddate = request.getParamValue("RO_ENDDATE");
			String approveDate = request.getParamValue("approve_date");//审核通过时间查询条件
			String approveDate2 = request.getParamValue("approve_date2");
			String balanceApproveDate = request.getParamValue("balance_approve_date");//结算审核通过时间查询条件
			String balanceApproveDate2 = request.getParamValue("balance_approve_date2");
			String status = request.getParamValue("STATUS");
			String deliverer = request.getParamValue("DELIVERER");//客户姓名
			String delivererPhone = request.getParamValue("DELIVERER_PHONE");//客户电话
			String yieldly = request.getParamValue("YIELDLY_TYPE");//生产基地
			// 艾春 13.11.20 添加是否二级
			String isSecond = request.getParamValue("IS_SECOND"); // 是否二级
			
			String partCode = getParam("partCode");
			String wrLabourCode = getParam("wrLabourCode");
			// 艾春 13.11.20 添加是否二级
			
			String isImport = request.getParamValue("is_import");//是否导入
			String model = request.getParamValue("model");//车型
			String vin1 = "";
			
		if(Utility.testString(vin)){
			String[] vins = vin.split(",");
			for(int i=0;i<vins.length;i++){
				vin1 = vin1+"'"+vins[i]+"',";
			}
			vin1 = vin1.substring(0, vin1.length()-1);
		}
			map.put("dealerId", dealerId);
			map.put("claimNo", claimNo);
			map.put("partCode", partCode);
			map.put("wrLabourCode", wrLabourCode);
			map.put("roNo", roNo);
			map.put("lineNo", lineNo);
			map.put("claimType", claimType);
			map.put("vin", vin1);
			map.put("roStartdate", roStartdate);
			map.put("roEnddate", roEnddate);
			map.put("status", status);
			map.put("oemCompanyId", companyId.toString());
			map.put("track", "track"); //状态跟踪查询
			map.put("isApp", "isApp");
			map.put("delivererPhone", delivererPhone);
			map.put("deliverer", deliverer);
			map.put("approveDate", approveDate);
			map.put("approveDate2",approveDate2);
			map.put("balanceApproveDate", balanceApproveDate);
			map.put("balanceApproveDate2",balanceApproveDate2);
			map.put("yieldly",yieldly);
			map.put("isSecond",isSecond);
			map.put("isImport", isImport);
			map.put("model", model);
			//经销商代码
			if (Utility.testString(dealerId)) {
				con.append(" AND (A.DEALER_ID in  (select d.dealer_id  from tm_dealer d  start with d.dealer_id = "+dealerId+"   connect by PRIOR d.dealer_id = d.parent_dealer_d)or a.second_dealer_id = "+dealerId+" ) \n");
			}
			//索赔单号
			if (Utility.testString(claimNo)) {
				con.append(" AND A.CLAIM_NO LIKE '%"+claimNo+"%' \n");
			}
			//工单号
			if (Utility.testString(roNo)) {
				con.append(" AND A.RO_NO LIKE '%"+roNo+"%' \n");
			}
			//行号
			if (Utility.testString(lineNo)) {
				con.append(" AND A.LINE_NO='"+lineNo+"' \n");
			}
			//索赔类型
			if (Utility.testString(claimType)) {
				con.append(" AND A.CLAIM_TYPE='"+claimType+"' \n");
			}
			//车辆VIN码
			if (Utility.testString(vin)) {
				con.append(" AND A.VIN LIKE '%"+vin+"%' \n");
			}
			//工单开始时间
			if (Utility.testString(roStartdate)) {
				con.append(" AND A.CREATE_DATE >= to_date('"+roStartdate+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') \n");
			}
			//工单结束时间
			if (Utility.testString(roEnddate)) {
				con.append(" AND A.CREATE_DATE <= to_date('"+roEnddate+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
			}
			//申请状态
			if (Utility.testString(status)) {
				con.append(" AND A.STATUS='"+status+"' \n");
			}
			
			PageResult<TtAsWrApplicationExtBean> ps = dao.queryDealerApplication(loginUser, map,null, 10, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void applicationQueryTotal() {
		
		
				
		act.getResponse().setContentType("application/json");
		String dealerId = loginUser.getDealerId();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);     //公司ID
		Map<String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			String claimNo = request.getParamValue("CLAIM_NO");
			String roNo = request.getParamValue("RO_NO");
			String lineNo = request.getParamValue("LINE_NO");
			String claimType = request.getParamValue("CLAIM_TYPE");
			String vin = request.getParamValue("VIN");
			String roStartdate = request.getParamValue("RO_STARTDATE");
			String roEnddate = request.getParamValue("RO_ENDDATE");
			String approveDate = request.getParamValue("approve_date");//审核通过时间查询条件
			String approveDate2 = request.getParamValue("approve_date2");
			String balanceApproveDate = request.getParamValue("balance_approve_date");//结算审核通过时间查询条件
			String balanceApproveDate2 = request.getParamValue("balance_approve_date2");
			String status = request.getParamValue("STATUS");
			String deliverer = request.getParamValue("DELIVERER");//客户姓名
			String delivererPhone = request.getParamValue("DELIVERER_PHONE");//客户电话
			String yieldly = request.getParamValue("YIELDLY_TYPE");//生产基地
			// 艾春 13.11.20 添加是否二级
			String isSecond = request.getParamValue("IS_SECOND"); // 是否二级
			// 艾春 13.11.20 添加是否二级
			
			String isImport = request.getParamValue("is_import");//是否导入
			String model = request.getParamValue("model");//车型
			
			String partCode = getParam("partCode");
			String wrLabourCode = getParam("wrLabourCode");
			String vin1 = "";
			
		if(Utility.testString(vin)){
			String[] vins = vin.split(",");
			for(int i=0;i<vins.length;i++){
				vin1 = vin1+"'"+vins[i]+"',";
			}
			vin1 = vin1.substring(0, vin1.length()-1);
		}
			map.put("dealerId", dealerId);
			map.put("claimNo", claimNo);
			map.put("roNo", roNo);
			map.put("lineNo", lineNo);
			map.put("claimType", claimType);
			map.put("vin", vin1);
			map.put("roStartdate", roStartdate);
			map.put("roEnddate", roEnddate);
			map.put("status", status);
			map.put("oemCompanyId", companyId.toString());
			map.put("track", "track"); //状态跟踪查询
			map.put("isApp", "isApp");
			map.put("delivererPhone", delivererPhone);
			map.put("deliverer", deliverer);
			map.put("approveDate", approveDate);
			map.put("approveDate2",approveDate2);
			map.put("balanceApproveDate", balanceApproveDate);
			map.put("balanceApproveDate2",balanceApproveDate2);
			map.put("yieldly",yieldly);
			map.put("isSecond",isSecond);
			map.put("isImport", isImport);
			map.put("model", model);
			map.put("partCode", partCode);
			map.put("wrLabourCode", wrLabourCode);
			
			List<Map<String,Object>> list = dao.queryDealerApplicationTotal(loginUser, map,null);
			if(list.size()>0){
				act.setOutData("count", list.get(0).get("COUNT"));
				act.setOutData("totalBalanceAmount", list.get(0).get("TOTALBALANCEAMOUNT"));
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void applicationQueryCVS() {
		
		
				
		String dealerId = loginUser.getDealerId();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);     //公司ID
		StringBuffer con = new StringBuffer();
		Map<String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String claimNo = request.getParamValue("CLAIM_NO");
			String roNo = request.getParamValue("RO_NO");
			String lineNo = request.getParamValue("LINE_NO");
			String claimType = request.getParamValue("CLAIM_TYPE");
			String vin = request.getParamValue("VIN");
			String roStartdate = request.getParamValue("RO_STARTDATE");
			String roEnddate = request.getParamValue("RO_ENDDATE");
			String approveDate = request.getParamValue("approve_date");//审核通过时间查询条件
			String approveDate2 = request.getParamValue("approve_date2");
			String status = request.getParamValue("STATUS");
			String deliverer = request.getParamValue("DELIVERER");//客户姓名
			String delivererPhone = request.getParamValue("DELIVERER_PHONE");//客户电话
			String yieldly = request.getParamValue("YIELDLY_TYPE");//生产基地
			map.put("dealerId", dealerId);
			map.put("claimNo", claimNo);
			map.put("roNo", roNo);
			map.put("lineNo", lineNo);
			map.put("claimType", claimType);
			map.put("vin", vin);
			map.put("roStartdate", roStartdate);
			map.put("roEnddate", roEnddate);
			map.put("status", status);
			map.put("oemCompanyId", companyId.toString());
			map.put("track", "track"); //状态跟踪查询
			map.put("isApp", "isApp");
			map.put("delivererPhone", delivererPhone);
			map.put("deliverer", deliverer);
			map.put("approveDate", approveDate);
			map.put("approveDate2",approveDate2);
			map.put("yieldly",yieldly);
			//经销商代码
			if (Utility.testString(dealerId)) {
				con.append(" AND A.DEALER_ID='"+dealerId+"' \n");
			}
			//索赔单号
			if (Utility.testString(claimNo)) {
				con.append(" AND A.CLAIM_NO LIKE '%"+claimNo+"%' \n");
			}
			//工单号
			if (Utility.testString(roNo)) {
				con.append(" AND A.RO_NO LIKE '%"+roNo+"%' \n");
			}
			//行号
			if (Utility.testString(lineNo)) {
				con.append(" AND A.LINE_NO='"+lineNo+"' \n");
			}
			//索赔类型
			if (Utility.testString(claimType)) {
				con.append(" AND A.CLAIM_TYPE='"+claimType+"' \n");
			}
			//车辆VIN码
			if (Utility.testString(vin)) {
				con.append(" AND A.VIN LIKE '%"+vin+"%' \n");
			}
			//工单开始时间
			if (Utility.testString(roStartdate)) {
				con.append(" AND A.REPORT_DATE >= to_date('"+roStartdate+"', 'yyyy-mm-dd hh24:mi:ss') \n");
			}
			//工单结束时间
			if (Utility.testString(roEnddate)) {
				con.append(" AND A.REPORT_DATE <= to_date('"+roEnddate+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
			}
			//申请状态
			if (Utility.testString(status)) {
				con.append(" AND A.STATUS='"+status+"' \n");
			}
			PageResult<TtAsWrApplicationExtBean> ps = dao.queryDealerApplicationCVS(loginUser, map,null, 10, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Iverson add with 2010-11-12
	 * @Title: applicationDeleteQuery
	 * @Description: TODO(查询可删除的索赔单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void applicationDeleteQuery() {
		
		
				
		String dealerId = loginUser.getDealerId();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);     //公司ID
		Map<String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String claimNo = request.getParamValue("CLAIM_NO");
			String roNo = request.getParamValue("RO_NO");
			String claimType = request.getParamValue("CLAIM_TYPE");
			String vin = request.getParamValue("VIN");
			String roStartdate = request.getParamValue("RO_STARTDATE");
			String roEnddate = request.getParamValue("RO_ENDDATE");
			String approveDate = request.getParamValue("approve_date");//审核通过时间查询条件
			String approveDate2 = request.getParamValue("approve_date2");
			map.put("dealerId", dealerId);
			map.put("claimNo", claimNo);
			map.put("roNo", roNo);
			map.put("claimType", claimType);
			map.put("vin", vin);
			map.put("roStartdate", roStartdate);
			map.put("roEnddate", roEnddate);
			map.put("oemCompanyId", companyId.toString());
			map.put("track", "track"); //状态跟踪查询
			map.put("isApp", "isApp");
			map.put("approveDate", approveDate);
			map.put("approveDate2",approveDate2);
			
			PageResult<TtAsWrApplicationExtBean> ps = dao.queryDealerDeleteApplication(loginUser, map,null, 10, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除索赔单");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
   /**
    * 
    * @Title: claimBillModifyForword
    * @Description: TODO(索赔单明细页面跳转)
    * @param 设定文件
    * @return void 返回类型
    * @throws
    */
	public void claimBillDetailForward() {
			String dealerId = "";
			if(null != loginUser){
				dealerId = CommonUtils.checkNull(loginUser.getDealerId());
			}
			String phone = "";
			try {
				TtAsActivityPO activity = new TtAsActivityPO();
				String id = request.getParamValue("ID");
				String type = request.getParamValue("type");
				TtAsWrApplicationPO a = new TtAsWrApplicationPO();
				a.setId(Long.valueOf(id));
				a= (TtAsWrApplicationPO) dao.select(a).get(0);
				List<Map<String, Object>> mainCodeList = dao.queryMainList(id);//得到主因件集合
				
				TtAsWrApplicationExtPO tawep = dao.queryApplicationById(id);
				List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
				List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时
				List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
				List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
				List<Map<String, Object>> appAuthls = dao.queryAppAuthDetail(id);//审核授权信息
				
				List<Map<String, Object>> accessoryDtlList = dao.getclaimAccessoryDtl(a.getClaimNo());
				act.setOutData("accessoryDtlList", accessoryDtlList);
				
				//====================zyw 2014-8-4 补偿费查询明细
				List<Map<String, Object>> compensationMoneyList = dao.getCompensationMoneyAPP(a.getClaimNo());
				if (compensationMoneyList != null && compensationMoneyList.size() >= 0) {
					act.setOutData("compensationMoneyList", compensationMoneyList);
				} 
				//==============================
				TmDealerPO d =  new TmDealerPO();
				d.setDealerCode(tawep.getDealerCode());
				
				List<TmDealerPO> resList = dao.select(d);
				if (resList != null && resList.size() > 0) {
					TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
					phone = dealerPO.getPhone();
				}
				act.setOutData("application", tawep);
				act.setOutData("ACTIVITY", activity);
				act.setOutData("ID", id);
				act.setOutData("itemLs", itemls);
				act.setOutData("partLs", partls);
				act.setOutData("appAuthls",appAuthls);//索赔单之审核信息
				act.setOutData("mainCodeList",mainCodeList);
				act.setOutData("size",mainCodeList.size());
				act.setOutData("otherLs", otherls);
				act.setOutData("attachLs", attachLs);
				act.setOutData("type", type);
				act.setOutData("phone", phone);
				act.setOutData("dealerId", dealerId);
				if(a.getClaimType().toString().equalsIgnoreCase(Constant.CLA_TYPE_02.toString())||a.getClaimType().toString().equalsIgnoreCase(Constant.CLA_TYPE_11.toString())){
					 act.setForword(CLAIM_BILL_DETAIL_URL);
				}else{
					act.setForword(CLAIM_BILL_DETAIL_URL2);
				}
				
	     
	    } catch (Exception e) {
	      BizException e1 = new BizException(act, e,
	          ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
	      logger.error(loginUser, e1);
	      act.setException(e1);
	    }
	 }
	 
	 /**
	    * Iverson add with 2010-11-12
	    * @Title: claimBillDetailDeleteForward
	    * @Description: TODO(删除索赔单跳转至明细页)
	    * @param 设定文件
	    * @return void 返回类型
	    * @throws
	    */
		 @SuppressWarnings("unchecked")
		public void claimBillDetailDeleteForward() {
		    
		    
		        
		    Long provinceId = null;
			String phone = "";
		    try {
		    	RequestWrapper request = act.getRequest();
				TtAsActivityPO activity = new TtAsActivityPO();
				String id = request.getParamValue("ID");
				String type = request.getParamValue("type");
				TtAsWrApplicationPO a = new TtAsWrApplicationPO();
				a.setId(Long.valueOf(id));
				a= (TtAsWrApplicationPO) dao.select(a).get(0);
				List<Map<String, Object>> mainCodeList = dao.queryMainList(id);//得到主因件集合
				
				TtAsWrApplicationExtPO tawep = dao.queryApplicationById(id);
				List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
				List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时
				List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
				List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
				List<Map<String, Object>> appAuthls = dao.queryAppAuthDetail(id);//审核授权信息
				
				List<Map<String, Object>> accessoryDtlList = dao.getclaimAccessoryDtl(a.getClaimNo());
				act.setOutData("accessoryDtlList", accessoryDtlList);
				
				//====================zyw 2014-8-4 补偿费查询明细
				List<Map<String, Object>> compensationMoneyList = dao.getCompensationMoneyAPP(a.getClaimNo());
				if (compensationMoneyList != null && compensationMoneyList.size() >= 0) {
					act.setOutData("compensationMoneyList", compensationMoneyList);
				} 
				//==============================
				TmDealerPO d =  new TmDealerPO();
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
				act.setOutData("appAuthls",appAuthls);//索赔单之审核信息
				act.setOutData("mainCodeList",mainCodeList);
				act.setOutData("size",mainCodeList.size());
				act.setOutData("otherLs", otherls);
				act.setOutData("attachLs", attachLs);
				act.setOutData("type", type);
				act.setOutData("phone", phone);
				if(a.getClaimType().toString().equalsIgnoreCase(Constant.CLA_TYPE_02.toString())||a.getClaimType().toString().equalsIgnoreCase(Constant.CLA_TYPE_11.toString())){
					act.setForword(this.CLAIM_BILL_DEL_DETAIL_URL);
				}else{
					act.setForword(this.CLAIM_BILL_DEL_DETAIL_URL2);
				}
		    } catch (Exception e) {
		      BizException e1 = new BizException(act, e,
		          ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
		      logger.error(loginUser, e1);
		      act.setException(e1);
		    }
		 }
		 
		 /**
		    * Iverson add with 2010-12-16
		    * 废弃索赔单是首先查询此索赔单已经被生成结算单没有
		    * @Title: isCount
		    */
			public void isCount() {
			    
			    
			    try {
			      RequestWrapper request = act.getRequest(); 
			      ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();
			      String id = request.getParamValue("ID");
			      String claimNo = request.getParamValue("claimNo");
			      int count = auditingDao.isCount(id);
			      if(count>0){
			    	  act.setOutData("flag", "false");
			      }
			      act.setOutData("id", id);
			      act.setOutData("claimNo", claimNo);
			    } catch (Exception e) {
			      BizException e1 = new BizException(act, e,
			          ErrorCodeConstant.QUERY_FAILURE_CODE, "废弃索赔单是首先查询此索赔单是否生成结算单");
			      logger.error(loginUser, e1);
			      act.setException(e1);
			    }
			 }
		 
		 /**
		    * Iverson add with 2010-11-15
		    * @Title: claimBillDetailDeleteForward1
		    * @Description: TODO(跳转至删除索赔单明细页)
		    * @param 设定文件
		    * @return void 返回类型
		    * @throws
		    */
			 @SuppressWarnings("unchecked")
			public void claimBillDetailDeleteForward1() {
			    
			    
			        
			    try {
			      RequestWrapper request = act.getRequest(); 
			      ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();
			      String id = request.getParamValue("ID");
			      String isAudit = request.getParamValue("isAudit");//是否是索赔审核（true:是，false:索赔明细查询）
			      TtAsWrApplicationExtPO tawep = auditingDao.queryClaimOrderDetailById11(id);
			      List<Map<String, Object>> customerList = dao.getVinUserName2(id);
			      Map<String,Object> customerMap = new HashMap<String, Object>();
			      if(customerList!=null && customerList.size()>0)
			        customerMap = customerList.get(0);
			      
			      //取的当前需要审核的级别
			      String authCode = tawep.getAuthCode();
			      String authLevel = "--";
			      if(authCode!=null){
			    	  TtAsWrAuthinfoPO conditionPO = new TtAsWrAuthinfoPO();
			    	  conditionPO.setApprovalLevelCode(authCode);
			    	  List<TtAsWrAuthinfoPO> authLevelList = auditingDao.select(conditionPO);
			    	  if(authLevelList!=null && authLevelList.size()>0){
			    		  TtAsWrAuthinfoPO authInfoPO = authLevelList.get(0);
			    		  authLevel = CommonUtils.checkNull(authInfoPO.getApprovalLevelName());
			    	  }
			      }
			      
			      //取得产地名称
			      String yieldly = tawep.getYieldly();
			      String yieldlyName = "";
			      if(yieldly!=null){
			        TcCodePO codePO = CommonUtils.findTcCodeDetailByCodeId(CommonUtils.parseInteger(yieldly));
			        if(codePO!=null && codePO.getCodeDesc()!=null)
			          yieldlyName = codePO.getCodeDesc();
			      }
			      tawep.setYieldlyName(yieldlyName);
			      
			      List<ClaimListBean> partls = dao.queryPartById(id); //取配件信息
			      List<ClaimListBean> itemls = dao.queryItemById(id); //取工时
			      List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);//取其他项目
			      List<FsFileuploadPO> attachLs = dao.queryAttById(id);//取得附件
			      List<TtAsWrAppauthitemPO> appAuthls = dao.queryAppAuthInfo(id);//审核授权信息
			      TtAsWrWrauthorizationPO authReason = dao.queryAuthReason(id);//需要授权原因
			      List<Map<String,Object>> listOutRepair = dao.viewOutRepair(tawep.getRoNo());//根据索赔单的工单号查询工单的外出维修信息
			      if(listOutRepair!=null&&listOutRepair.size()>0){
			    	  
			    	  act.setOutData("listOutRepair",listOutRepair.get(0));
			      }
			      act.setOutData("application", tawep);
			      act.setOutData("attachLs", attachLs);
			      act.setOutData("ID", id);//索赔申请单ID
			      act.setOutData("itemLs", itemls);//索赔单之工时信息
			      act.setOutData("partLs", partls);//索赔单之配件信息
			      act.setOutData("otherLs",otherls);//索赔单之其他项目信息
			      act.setOutData("appAuthls",appAuthls);//索赔单之审核信息
			      act.setOutData("authReason",authReason);//索赔单之审核原因
			      act.setOutData("isAudit", isAudit);//true:审核页面 false:明细查询页面
			      act.setOutData("customerMap", customerMap);//客户信息
			      act.setOutData("authLevel", authLevel);//需要审核的级别
			      
			      /****************************************************************************/
			      /*TtAsWrApplicationPO po = new TtAsWrApplicationPO();
			      po.setId(Long.parseLong(id));
			      List list = dao.selectApplicationById(po);
			      TtAsWrApplicationPO status = (TtAsWrApplicationPO)list.get(0);
			      String statusValue = status.getStatus().toString();
			      String appDel = status.getApplicationDel();
			      act.setOutData("statusValue", statusValue);
			      act.setOutData("appDel", appDel);*/
			      /****************************************************************************/
			      
			      act.setForword(CLAIM_BILL_DEL_DETAIL_URL1);
			    } catch (Exception e) {
			      BizException e1 = new BizException(act, e,
			          ErrorCodeConstant.QUERY_FAILURE_CODE, "审核删除索赔单");
			      logger.error(loginUser, e1);
			      act.setException(e1);
			    }
			 }
	
	/**
	 * 
	 * @Title: getChngCodeStr
	 * @Description: TODO(取得故障代码，质损区域，质损类型，质损程度下拉框)
	 * @param
	 * @param type
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getChngCodeStr(int type) {
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);     //公司ID
		List<TmBusinessChngCodePO> seriesList = dao.queryChngCodeByType(type,companyId,null,null);
		String retStr = "[{";
		//retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TmBusinessChngCodePO bean = new TmBusinessChngCodePO();
			bean = (TmBusinessChngCodePO) seriesList.get(i);
			retStr +=  ",'"+bean.getCode() + "':\'"
					+ bean.getCodeName() + "\'";
		}
		retStr += "}]";
		retStr = retStr.replaceFirst(",", "");
		return retStr;
	}
	
	/**
	 * 
	 * @Title: getOtherfeeStr
	 * @Description: TODO(取得其他费用下拉框)
	 * @param
	 * @param type
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getOtherfeeStr() {
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);     //公司ID
		List<TtAsWrOtherfeePO> seriesList = dao.queryOtherFee(companyId);
		String retStr = "";
		retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TtAsWrOtherfeePO bean = new TtAsWrOtherfeePO();
			bean = (TtAsWrOtherfeePO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getFeeId() + "\'" + "title=\'"
					+ bean.getFeeName() + "\'>" + bean.getFeeCode()
					+ "</option>";
		}
		return retStr;
	}
	
	/**
	 * 
	 * @Title: claimBillRoNoForward
	 * @Description: TODO(根据工单号查询车辆维护记录)
	 * @param
	 * @param type
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public void claimBillRoNoForward(){
		
		
				
		try{
			String roNo = act.getRequest().getParamValue("roNo");
			List<TtAsRoAddItemPO> items = dao.queryAddItem(roNo,null) ;
			List<TtAsRoLabourPO> repairs = dao.queryRepairitem(roNo,null) ;
			List<TtAsRoRepairPartPO> parts = dao.queryRepairPart(roNo,null) ;
			act.setOutData("items", items);
			act.setOutData("repairs", repairs);
			act.setOutData("parts", parts);
			act.setForword(CLAIM_BILL_RO_NO_DETAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "维修记录查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
		/**
	 * 
	 * @Title: claimBillForword
	 * @Description: TODO(车辆维修历史明细（索赔）)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void claimBillTrackForword() {
		
		
		
		
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			Integer pageSize = 10;
			String vin = request.getParamValue("vin");
			//List<LabourPartBean> lists= dao.queryLabourPartBean(vin);
			List<LabourPartBean> pageresults= dao.queryLabourPartBean(vin,pageSize, curPage);
			//System.out.println(lists.size()+lists.toString());
			//act.setOutData("lists",lists);
			act.setOutData("lists", pageresults);
			act.setForword(CLAIM_BILL_VIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆维修历史明细（索赔）");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		}
	
	/**
	 * 打印索赔单配件标签
	 */
	public void printClaimPartLabel(){
		
		
		
		
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			if(id==null)
				id = "-1";
			List<Map<String,Object>> resultList = dao.queryClaimPart(CommonUtils.parseLong(id));
			act.setOutData("claimId", id);
			act.setOutData("pf", resultList);

			//zhumingwei 2011-03-11  此方法用于区分轿车和微车
			TcCodePO codePo= new TcCodePO();
			codePo.setType(Constant.chana+"");
			TcCodePO poValue = (TcCodePO)dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			//zhumingwei 2011-03-11
			act.setForword(this.PRINT_PART_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单配件标签打印");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void toExcel() {
		
		
		
		act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try {
			String fileName = "旧件回收标签列表.csv";
			fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
			act.getResponse().setContentType("Application/text/csv");
			act.getResponse().addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			List<List<Object>> list = new LinkedList<List<Object>>();
			List<Object> titleList = genHead();
			list.add(titleList);
			
			String returnId = request.getParamValue("claimId");//获取回运清单的修改主键
			
			List<Map<String, Object>> details = dao.queryClaimPart(CommonUtils.parseLong(returnId));
			
			for (Map<String, Object> detail : details) {
				list.add(genBody(detail));
			}
			os = act.getResponse().getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务车导出功能 ");
			logger.error(loginUser, e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	* 
	* @Title: genHead 
	* @Description: TODO(生成Excel表头) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private List<Object> genHead() {
		List<Object> titleList = new LinkedList<Object>();
		//titleList.add("序号");
		titleList.add("三包单号");
		titleList.add("VIN码");
		titleList.add("生产厂家");
		titleList.add("车型");
		titleList.add("发动机号");
		titleList.add("行驶里程");
		titleList.add("购车日期");
		titleList.add("服务商代码");
		titleList.add("零件名称");
		titleList.add("零件编码");
		titleList.add("配套厂家");
		titleList.add("故障");
		titleList.add("客户姓名");
		titleList.add("电话");
		return titleList;
	}
	
	private List<Object> genBody(Map<String, Object> detail) {
		List<Object> dataList = new LinkedList<Object>();
		dataList.add(CommonUtils.checkNull(detail.get("CLAIM_NO")));        //三包单号
		dataList.add(CommonUtils.checkNull(detail.get("VIN")));             //VIN码
		dataList.add(CommonUtils.checkNull(detail.get("YIELDLY")));         //生产厂家
		dataList.add(CommonUtils.checkNull(detail.get("MODEL_CODE")));	    //车型
		dataList.add(CommonUtils.checkNull(detail.get("ENGINE_NO")));		//发动机号
		dataList.add(CommonUtils.checkNull(detail.get("IN_MILEAGE")));		//行驶里程
		dataList.add(CommonUtils.checkNull(detail.get("PURCHASED_DATE")));	//购车日期
		dataList.add(CommonUtils.checkNull(detail.get("DEALER_CODE")));		//服务商代码
		dataList.add(CommonUtils.checkNull(detail.get("PART_NAME")));		//零件名称
		dataList.add(CommonUtils.checkNull(detail.get("PART_CODE")));		//零件编码
		dataList.add(CommonUtils.checkNull(detail.get("DC_NAME")));			//配套厂家
		dataList.add(CommonUtils.checkNull(detail.get("REMARK")));          //故障
		dataList.add(CommonUtils.checkNull(detail.get("DELIVERER")));       //客户姓名
		dataList.add(CommonUtils.checkNull(detail.get("DELIVERER_PHONE"))); //电话
		return dataList;
	}

	/**********************Iverson add with 2010-11-12***************************/
	/**
	 * 删除索赔单同时添加索赔单备份表
	 */
	public void deleteApplication(){
		String id = request.getParamValue("ID");//索赔单ID
		TtAsWrApplicationPO po = new TtAsWrApplicationPO();
		po.setId(Long.parseLong(id));
		//新增索赔申请单备份表
		int count = dao.insertApplication(Long.parseLong(id));
		int temp=0;
		if(count>0){
			//更新废弃人和时间
			TtAsWrApplicationBackupPO poBack = new TtAsWrApplicationBackupPO();
			poBack.setId(Long.parseLong(id));
			TtAsWrApplicationBackupPO poBackValue = new TtAsWrApplicationBackupPO();
			poBackValue.setUpdateBy(loginUser.getUserId());
			poBackValue.setUpdateDate(new Date());
			dao.update(poBack, poBackValue);
			//更新废弃人和时间 end
			
			//删除关系表
			TtAsWrPartsitemPO pp = new TtAsWrPartsitemPO();
			pp.setId(Long.parseLong(id));
			dao.delete(pp);
			TtAsWrLabouritemPO lp =new TtAsWrLabouritemPO();
			lp.setId(Long.parseLong(id));
			dao.delete(lp);
			TtAsWrNetitemPO np = new TtAsWrNetitemPO();
			np.setId(Long.parseLong(id));
			dao.delete(np);
			TtAsWrApplicationPO a = new TtAsWrApplicationPO();
			a = (TtAsWrApplicationPO) dao.select(po).get(0);
			TtClaimAccessoryDtlPO dp = new TtClaimAccessoryDtlPO();
			dp.setClaimNo(a.getClaimNo());
			dao.delete(dp);
			TrBalanceClaimPO trPo = new TrBalanceClaimPO();
			trPo.setClaimId(Long.parseLong(id));
			dao.deleteTrBalanceClaim(trPo);
			//删除索赔申请表
			dao.deleteApplication(po);
			temp=1;
		}else{
			temp=0;
		}	
		act.setOutData("ACTION_RESULT", temp);
	}
	/**
	 * 如果是第一次申请删除,就改变索赔单状态
	 */
	public void updateApplication(){
		
		RequestWrapper request = act.getRequest();
		String id = request.getParamValue("ID");//索赔单ID
		TtAsWrApplicationPO po = new TtAsWrApplicationPO();
		po.setId(Long.parseLong(id));
		//新增索赔申请单备份表
		int count = dao.updateApplication(Long.parseLong(id));
		if(count>0){
			this.claimBillDeleteForward();//跳转至首页
		}else{}
	}
	/**
	 * 删除索赔单同时添加索赔单备份表(车厂端)
	 */
	public void deleteApplication1(){
		
		RequestWrapper request = act.getRequest();
		String id = request.getParamValue("ID");//索赔单ID
		TtAsWrApplicationPO po = new TtAsWrApplicationPO();
		po.setId(Long.parseLong(id));
		//新增索赔申请单备份表
		int count = dao.insertApplication(Long.parseLong(id));
		if(count>0){
			//删除索赔申请表
			dao.deleteApplication(po);
			ClaimManualAuditing c = new ClaimManualAuditing();
			c.deleteClaimManualAuditingInit();//跳转至首页
		}else{}	
	}
	/**********************Iverson add with 2010-11-12***************************/
	
	
	//条码打印跳转
	
	public void barCodePrint() {
		
		
				
		try {
			
				act.setForword(BARCODE_PRINT);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title: applicationQuery
	 * @Description: TODO(查询索赔单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void barCodePrintQuery() {
		
		
				
		String dealerId = loginUser.getDealerId();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);     //公司ID
		StringBuffer con = new StringBuffer();
		Map<String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String claimNo = request.getParamValue("CLAIM_NO");
			String roNo = request.getParamValue("RO_NO");
			String lineNo = request.getParamValue("LINE_NO");
			String claimType = request.getParamValue("CLAIM_TYPE");
			String vin = request.getParamValue("VIN");
			String roStartdate = request.getParamValue("RO_STARTDATE");
			String roEnddate = request.getParamValue("RO_ENDDATE");
			String approveDate = request.getParamValue("approve_date");//审核通过时间查询条件
			String approveDate2 = request.getParamValue("approve_date2");
			String balanceApproveDate = request.getParamValue("balance_approve_date");//结算审核通过时间查询条件
			String balanceApproveDate2 = request.getParamValue("balance_approve_date2");
			String status = request.getParamValue("STATUS");
			String deliverer = request.getParamValue("DELIVERER");//客户姓名
			String delivererPhone = request.getParamValue("DELIVERER_PHONE");//客户电话
			String yieldly = request.getParamValue("YIELDLY_TYPE");//生产基地
			String is_print = request.getParamValue("IS_PRINT");//是否打印
			
			map.put("dealerId", dealerId);
			map.put("claimNo", claimNo);
			map.put("roNo", roNo);
			map.put("lineNo", lineNo);
			map.put("claimType", claimType);
			map.put("vin", vin);
			map.put("roStartdate", roStartdate);
			map.put("roEnddate", roEnddate);
			map.put("status", status);
			map.put("oemCompanyId", companyId.toString());
			map.put("track", "track"); //状态跟踪查询
			map.put("isApp", "isApp");
			map.put("delivererPhone", delivererPhone);
			map.put("deliverer", deliverer);
			map.put("approveDate", approveDate);
			map.put("approveDate2",approveDate2);
			map.put("balanceApproveDate", balanceApproveDate);
			map.put("balanceApproveDate2",balanceApproveDate2);
			map.put("yieldly",yieldly);
			map.put("is_print",is_print);
			//经销商代码
			if (Utility.testString(dealerId)) {
				con.append(" AND A.DEALER_ID='"+dealerId+"' \n");
			}
			//索赔单号
			if (Utility.testString(claimNo)) {
				con.append(" AND A.CLAIM_NO LIKE '%"+claimNo+"%' \n");
			}
			//工单号
			if (Utility.testString(roNo)) {
				con.append(" AND A.RO_NO LIKE '%"+roNo+"%' \n");
			}
			//行号
			if (Utility.testString(lineNo)) {
				con.append(" AND A.LINE_NO='"+lineNo+"' \n");
			}
			//索赔类型
			if (Utility.testString(claimType)) {
				con.append(" AND A.CLAIM_TYPE='"+claimType+"' \n");
			}
			//车辆VIN码
			if (Utility.testString(vin)) {
				con.append(" AND A.VIN LIKE '%"+vin+"%' \n");
			}
			//工单开始时间
			if (Utility.testString(roStartdate)) {
				con.append(" AND A.REPORT_DATE >= to_date('"+roStartdate+"', 'yyyy-mm-dd hh24:mi:ss') \n");
			}
			//工单结束时间
			if (Utility.testString(roEnddate)) {
				con.append(" AND A.REPORT_DATE <= to_date('"+roEnddate+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
			}
			//申请状态
			
			if (Utility.testString(status)) {
				con.append(" AND A.STATUS='"+status+"' \n");
			}
			PageResult<Map<String, Object>> ps = dao.barcodePrintQuery(loginUser, map,null, 10, curPage);
			
			List<Map<String, Object>> lis=ps.getRecords();
			
			if(!CommonUtils.isNullList(lis)) {
				StringBuffer theId = new StringBuffer("") ;
				int len = lis.size() ;
				for(int i=0; i<len; i++) {
					int strLen = theId.length() ;
					
					if(strLen == 0) {
						theId.append(lis.get(i).get("THE_ID")) ;
					
					} else {
						theId.append(",").append(lis.get(i).get("THE_ID")) ;
						
					}
				}
				
				act.setOutData("theId", theId) ;
			}
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void barcodePrintDoGet(){
		
		
				
		try{
			RequestWrapper request = act.getRequest();
			String dtlIds = request.getParamValue("dtlIds");
			List<Map<String, Object>> ls= dao.barcodePrintDoGet(dtlIds);
			
			//修改索赔单是否打印的状态
			dao.updatePrint(dtlIds);
	    		if(ls!=null&&ls.size()>0){
	    			act.setOutData("ls1", "轿车"+ls.get(0).get("AREA_NAME")) ;
	    			act.setOutData("yieldly", ls.get(0).get("BALANCE_YIELDLY"));
	    		} else{
	    			act.setOutData("yieldly", "");
	    			act.setOutData("ls1", "轿车") ;
	    		}
	    
			
			act.setOutData("ls", ls) ;
			act.setForword(barcodePrintDoGet);
		}
		 catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	
	}
	/**
	 * 
	 * @param claimId 索赔单状态跟踪打印
	 *
	 */
	public void claimBillForwardPrint(){
		
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("dtlIds");
			String type = request.getParamValue("claimType");
			List<TtAsWrApplicationExtPO> list2 = null;
			Double zhushu = 0.0;
			Double canbu = 0.0;
			Double buzhu = 0.0;
			Double guoqiao = 0.0;
			Double jiaot = 0.0;
			Double partDown = 0.0;
			Double labourDown = 0.0;
			Double outAll=0.0;
			if(Constant.CLA_TYPE_09==Integer.parseInt(type)){
				
				List<TtAsWrOutrepairPO> listO = dao.getOutDetail(id);
				List<TtAsWrNetitemPO> nList = dao.getOther(id);
				if(nList.size()>0){
					for(int i=0;i<nList.size();i++){
						if("QT006".equalsIgnoreCase(nList.get(i).getItemCode())){
							guoqiao+=nList.get(i).getAmount();
						}else if("QT007".equalsIgnoreCase(nList.get(i).getItemCode())){
							jiaot+=nList.get(i).getAmount();
						}else if("QT008".equalsIgnoreCase(nList.get(i).getItemCode())){
							zhushu+=nList.get(i).getAmount();
						}else if("QT009".equalsIgnoreCase(nList.get(i).getItemCode())){
							canbu+=nList.get(i).getAmount();
						}else if("QT010".equalsIgnoreCase(nList.get(i).getItemCode())){
							buzhu+=nList.get(i).getAmount();
						}
						outAll += nList.get(i).getAmount();
					}
				}
				act.setOutData("nList", nList);
				act.setOutData("tawep", listO.get(0));
				act.setOutData("guoqiao", guoqiao);
				act.setOutData("jiaot", jiaot);
				act.setOutData("outAll", outAll);
				act.setOutData("zhushu", zhushu);
				act.setOutData("canbu", canbu);
				act.setOutData("buzhu", buzhu);
			}else if(Constant.CLA_TYPE_06==Integer.parseInt(type)){
				List<TtAsActivityPO> aList = dao.getADetail(id);
				if(aList!=null && aList.size()>0){
					act.setOutData("a", aList.get(0));
					TtAsActivityProjectPO jp = new TtAsActivityProjectPO();
					jp.setActivityId(aList.get(0).getActivityId());
					jp.setProCode(Constant.SERVICEACTIVITY_CAR_cms_06);
					List<TtAsActivityProjectPO> ss= dao.select(jp);
					if(ss!=null&&ss.size()>0){
						labourDown+=ss.get(0).getAmount();
					}
					TtAsActivityProjectPO jp2 = new TtAsActivityProjectPO();
					jp2.setActivityId(aList.get(0).getActivityId());
					jp2.setProCode(Constant.SERVICEACTIVITY_CAR_cms_06);
					List<TtAsActivityProjectPO> ss2= dao.select(jp2);
					if(ss2!=null&&ss2.size()>0){
						partDown+=ss2.get(0).getAmount();
					}
					act.setOutData("labourDown", labourDown);
					act.setOutData("partDown", partDown);
				}
			}
			TtAsWrApplicationPO tawaPo = new TtAsWrApplicationPO();
			tawaPo.setId(Long.valueOf(id));
			List <TtAsWrApplicationPO> tawaPoList = dao.select(tawaPo);
			String claimNo = tawaPoList.get(0).getClaimNo();
			String length = "";
			List<Map<String, Object>> claimNoAccessoryList = dao.getclaimAccessoryDtl(claimNo);
			if(claimNoAccessoryList.size()==0){
				length = "0";
				act.setOutData("length", length);
			}else{
				act.setOutData("length", length);
			}
			act.setOutData("accessoriesPrice", tawaPoList.get(0).getAccessoriesPrice());
			act.setOutData("claimNoAccessoryList", claimNoAccessoryList);
			
			list2 = dao.getDetail(id);
			List<TtAsWrApplicationExtPO> list = dao.getBaseBean(id);
			String repairTimes = dao.getTimes(list.get(0).getVin());
			List<TtAsWrApplicationExtPO> baoy = dao.getbaoy(list.get(0).getVin());
			System.out.println(baoy);
				act.setOutData("bean", list.get(0));
				act.setOutData("list2", list2);
				act.setOutData("repairTimes", repairTimes);
				act.setOutData("baoy", baoy.get(0).getNum());
				act.setOutData("part", baoy.get(0).getPartPrice());
				act.setOutData("labour", baoy.get(0).getLabourPrice());
			act.setForword(this.CLAIM_BILL_PRINT_URL);

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商索赔管理--索赔单打印");
			act.setException(e1);
		}
   }
	/**
	 * 服务站上报封面打印
	 */
	public void dealerCoverPrint(){
		
		
				
		try{
			TmDealerPO d  =new TmDealerPO();
			d.setDealerId(Long.valueOf(loginUser.getDealerId()));
			d = (TmDealerPO) dao.select(d).get(0);
			act.setOutData("dealerType", d.getDealerLevel());
			act.setForword(dealerCoverPrintPer);
		}
		 catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "封面打印");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void dealerPrintDetail(){
		
		
				
		try{
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			//查询索赔申请单
			PageResult<Map<String,Object>> result = dao.getPrintDetail(loginUser.getDealerId(),curPage,
															Constant.PAGE_SIZE);
			act.setOutData("ps", result);
		}
		 catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "封面打印");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void showDate(){
		RequestWrapper request = act.getRequest();
		String yieldly = request.getParamValue("yieldly");
		TtAsDealerTypePO po = new TtAsDealerTypePO();
		po.setDealerId(Long.parseLong(loginUser.getDealerId()));
		po.setYieldly(yieldly);
		
		List list=dao.select(po);
		TtAsDealerTypePO poValue=null;
		if(list.size()>0){
			poValue = (TtAsDealerTypePO)list.get(0);
			Date oldDate = poValue.getBalanceDate();//取出时间
			Calendar calendar = Calendar.getInstance();//公用类，加年月日的
			calendar.setTime(oldDate);
			calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
			oldDate = calendar.getTime();//得到加一天后的值
			/******add 20110509旧件结束时间取上月底******/
			Calendar calendarEnd = Calendar.getInstance();//公用类，加年月日的
			calendarEnd.setTime(new Date());
			calendarEnd.add(Calendar.MONTH, -1);//当前月减1月
			calendarEnd.setTime(lastDayOfMonth(calendarEnd.getTime()));
			act.setOutData("oldDateEnd", calendarEnd.getTime());
			act.setOutData("oldDate", oldDate); 
		}else{
			act.setOutData("oldDate", "noTime");
		}
	}
	public Date lastDayOfMonth(Date date) { 
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
		cal.set(Calendar.DAY_OF_MONTH, 1); 
		cal.roll(Calendar.DAY_OF_MONTH, -1); 
		return cal.getTime(); 
	} 
	//生成封面
	@SuppressWarnings("unchecked")
	public void coverSave(){
		
		
				
		try{
			RequestWrapper request = act.getRequest();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String yieldly = request.getParamValue("YIELDLY_TYPE");
			String starDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			List<DealerCoverPrintBean> list = dao.getCoverDetail(loginUser.getDealerId(), yieldly,starDate+" 00:00:00",endDate+" 23:59:59");
			if(list!=null && list.size()>0){
				act.setOutData("bean", list.get(0));
			}else{
				act.setOutData("bean", null);
			}
			// 开始新增打印记录
			TtAsDealerCoverDetailPO dp = new TtAsDealerCoverDetailPO();
			dp.setId(Utility.getLong(SequenceManager.getSequence("")));
			dp.setBalanceYieldly(Integer.parseInt(yieldly));
			dp.setDealerId(Long.valueOf(loginUser.getDealerId()));
			dp.setEndDate(sdf.parse(endDate));
			dp.setStarDate(sdf.parse(starDate));
			dp.setLabourPrice(list.get(0).getLabourPrice());
			dp.setPartPrice(list.get(0).getPartPrice());
			dp.setPrintBy(Long.valueOf(loginUser.getDealerId()));
			dp.setPrintDate(new Date());
			dp.setPrintTimes(1);
			dp.setLastPrintDate(new Date());
			dao.insert(dp);
			
			//开始更新 下一次打印的开始时间
			TtAsDealerTypePO p = new TtAsDealerTypePO();
			TtAsDealerTypePO p2 = new TtAsDealerTypePO();
			p.setDealerId(Long.valueOf(loginUser.getDealerId()));
			p.setYieldly(yieldly);
			p2.setBalanceDate(sdf.parse(endDate));
			dao.update(p, p2);
			act.setOutData("yieldly", yieldly);
			act.setOutData("starDate", starDate);
			act.setOutData("endDate", endDate);
			act.setOutData("now", sdf.format(new Date()));
			act.setForword(dealerCoversave);
		}
		 catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "封面生成");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void prints(){
		
		
				
		try{
			RequestWrapper request = act.getRequest();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String yieldly = "0";
			String starDate = "0";
			String endDate ="0";
			String id = request.getParamValue("id");
			TtAsDealerCoverDetailPO cdp = new TtAsDealerCoverDetailPO();
			cdp.setId(Long.valueOf(id));
			cdp = (TtAsDealerCoverDetailPO) dao.select(cdp).get(0);
			yieldly = cdp.getBalanceYieldly().toString();
			starDate = sdf.format(cdp.getStarDate());
			endDate = sdf.format(cdp.getEndDate());
			List<DealerCoverPrintBean> list = dao.getCoverDetail(loginUser.getDealerId(), yieldly,starDate+" 00:00:00",endDate+" 23:59:59");
			if(list!=null && list.size()>0){
				act.setOutData("bean", list.get(0));
			}else{
				act.setOutData("bean", null);
			}
			Double price = list.get(0).getBaoyAmount()+list.get(0).getShouqAmount()+list.get(0).getNormalAmount()+list.get(0).getSpecialAmount()+list.get(0).getOutAmount()+list.get(0).getActivityAmount()+list.get(0).getTransAmount()+list.get(0).getOtherAmount()-list.get(0).getPartPrice();
		
			// 开始新增打印记录
			TtAsDealerCoverDetailPO dp = new TtAsDealerCoverDetailPO();
			TtAsDealerCoverDetailPO dp2 = new TtAsDealerCoverDetailPO();
			dp.setId(Long.valueOf(id));
			
			dp2.setPrintBy(Long.valueOf(loginUser.getDealerId()));
			dp2.setPrintTimes(cdp.getPrintTimes()+1);
			dp2.setLastPrintDate(new Date());
			dp2.setLabourPrice(price);
			dp2.setPartPrice(list.get(0).getPartPrice());
			dao.update(dp, dp2);
			
			//开始更新 下一次打印的开始时间
			
			act.setOutData("yieldly", yieldly);
			act.setOutData("starDate", starDate);
			act.setOutData("endDate", endDate);
			act.setOutData("now", sdf.format(new Date()));
			act.setForword(dealerCoversave);
		}
		 catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "封面生成");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//车厂查询服务站的封面
	public void dealerCoverSearch(){
		
		
				
		try{
			act.setForword(dealerCoverSearch);
		}
		 catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "封面打印");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void dealerPrintSearch(){
		
		
				
		try{
			RequestWrapper request = act.getRequest();
			String dealerCode  = request.getParamValue("dealerCode");
			String dealerName = request.getParamValue("dealerName");
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			//查询索赔申请单
			int pos = loginUser.getPoseType();
			PageResult<Map<String,Object>> result = dao.getPrintDetail2(dealerCode,dealerName,pos,curPage,
					ActionUtil.getPageSize(request));
			act.setOutData("ps", result);
			ActionUtil.setCustomPageSizeFlag(act, true);
		}
		 catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "封面打印");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void dealerPrintDetails(){
		
		
				
		try{
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			Map<String, Object> map = dao.queryDealerConver(id);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String yieldly = map.get("BALANCE_YIELDLY").toString();
			String starDate = map.get("STAR_DATE").toString();
			String endDate = map.get("END_DATE").toString();
			List<DealerCoverPrintBean> list = dao.getCoverDetail(map.get("DEALER_ID").toString(), 
					yieldly,sdf.format(sdf.parse(starDate))+" 00:00:00",sdf.format(sdf.parse(endDate))+" 23:59:59");
			if(list!=null && list.size()>0){
				act.setOutData("bean", list.get(0));
			}else{
				act.setOutData("bean", null);
			}
			act.setOutData("yieldly", map.get("BALANCE_YIELDLY"));
			act.setOutData("starDate", map.get("STAR_DATE"));
			act.setOutData("endDate", map.get("END_DATE"));
			act.setOutData("now", map.get("LAST_PRINT_DATE"));
			act.setForword(dealerCoversave2);
		}
		 catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "封面生成");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	public void export(){
		
		
				
		String dealerId = loginUser.getDealerId();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);     //公司ID
		Map<String,String> map2 = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			String claimNo = request.getParamValue("CLAIM_NO");
			String roNo = request.getParamValue("RO_NO");
			String lineNo = request.getParamValue("LINE_NO");
			String claimType = request.getParamValue("CLAIM_TYPE");
			String vin = request.getParamValue("VIN");
			String roStartdate = request.getParamValue("RO_STARTDATE");
			String roEnddate = request.getParamValue("RO_ENDDATE");
			String approveDate = request.getParamValue("approve_date");//审核通过时间查询条件
			String approveDate2 = request.getParamValue("approve_date2");
			String balanceApproveDate = request.getParamValue("balance_approve_date");//结算审核通过时间查询条件
			String balanceApproveDate2 = request.getParamValue("balance_approve_date2");
			String status = request.getParamValue("STATUS");
			String deliverer = request.getParamValue("DELIVERER");//客户姓名
			String delivererPhone = request.getParamValue("DELIVERER_PHONE");//客户电话
			String yieldly = request.getParamValue("YIELDLY_TYPE");//生产基地
			// 艾春 13.11.20 添加是否二级
			String isSecond = request.getParamValue("IS_SECOND"); // 是否二级
			// 艾春 13.11.20 添加是否二级
			
			String vin1 = "";
			
			String partCode = getParam("partCode");
			String wrLabourCode = getParam("wrLabourCode");
			
		if(Utility.testString(vin)){
			String[] vins = vin.split(",");
			for(int i=0;i<vins.length;i++){
				vin1 = vin1+"'"+vins[i]+"',";
			}
			vin1 = vin1.substring(0, vin1.length()-1);
		}
			map2.put("dealerId", dealerId);
			map2.put("claimNo", claimNo);
			map2.put("roNo", roNo);
			map2.put("lineNo", lineNo);
			map2.put("claimType", claimType);
			map2.put("vin", vin1);
			map2.put("roStartdate", roStartdate);
			map2.put("roEnddate", roEnddate);
			map2.put("status", status);
			map2.put("oemCompanyId", companyId.toString());
			map2.put("track", "track"); //状态跟踪查询
			map2.put("isApp", "isApp");
			map2.put("delivererPhone", delivererPhone);
			map2.put("deliverer", deliverer);
			map2.put("approveDate", approveDate);
			map2.put("approveDate2",approveDate2);
			map2.put("balanceApproveDate", balanceApproveDate);
			map2.put("balanceApproveDate2",balanceApproveDate2);
			map2.put("yieldly",yieldly);
			map2.put("isSecond",isSecond);
			map2.put("partCode", partCode);
			map2.put("wrLabourCode", wrLabourCode);
			String name = "服务站索赔单.xls";
			String[] head=new String[30];
			head[0]="服务站代码";
			head[1]="服务站名称";
			head[2]="工单号";
			head[3]="索赔单号";
			head[4]="索赔类型";
			head[5]="车系";
			head[6]="车型";
			head[7]="送修人";
			head[8]="送修人电话";
			head[9]="VIN";
			head[10]="生产基地";
			head[11]="结算基地";
			head[12]="申请日期";
			head[13]="申请状态";
			head[14]="作业代码";
			head[15]="作业名称";
			head[16]="工时数";
			head[17]="工时单价";
			head[18]="工时费";
			
			head[19]="材料费";
			head[20]="外出费";
			head[21]="保养费";
			head[22]="辅料费";
			head[23]="补偿费";
			
			head[24]="配件代码";
			head[25]="配件名称";
			head[26]="配件数量";
			head[27]="配件单价";
			head[28]="配件总金额";
		//	head[29]="主因件";	
			
			
		
			List<Map<String,Object>> list2 = dao.getApplication(loginUser, map2);
			String setList = "1,2,3,8,9,12,13,14,15,20,18,19";
			 List list1=new ArrayList();
			    if(list2!=null&&list2.size()!=0){
					for(int i=0;i<list2.size();i++){
				    	Map map =(Map)list2.get(i);
						String[]detail=new String[30];
						
						detail[0]=(String) map.get("DEALER_CODE");
						detail[1]=(String) map.get("DEALER_SHORTNAME");
						detail[2]=(String)(map.get("RO_NO"));
						detail[3]=(String) map.get("CLAIM_NO");
						detail[4]=(String) map.get("CLAIM_TYPE");
						detail[5]=(String) map.get("SERIES_NAME");
						detail[6]=(String) map.get("MODEL");
						detail[7]=(String) map.get("DELIVERER");
						detail[8]=(String) map.get("DELIVERER_PHONE");
						detail[9]=(String) map.get("VIN");
						detail[10]=(String)map.get("YIELDLY_NAME");
						detail[11]=(String) map.get("BAN_NAME");
						detail[12]=(String) map.get("REPORT_TIME");
						detail[13]=(String) map.get("STATUS_NAME");
						detail[14]=(String) map.get("WR_LABOURCODE");
						detail[15]=(String) map.get("WR_LABOURNAME");
						detail[16]=String.valueOf(map.get("LABOUR_HOURS")) ;
						detail[17]=String.valueOf(map.get("LABOUR_PRICE")) ;
						detail[18]=String.valueOf(map.get("LABOUR_AMOUNT")) ;
						
						detail[19]=String.valueOf(map.get("PART_AMOUNT")) ;
						detail[20]=String.valueOf(map.get("NETITEM_AMOUNT")) ;
						detail[21]=String.valueOf(map.get("FREE_M_PRICE")) ;
						detail[22]=String.valueOf(map.get("ACCESSORIES_PRICE")) ;
						detail[23]=String.valueOf(map.get("COMPENSATION_MONEY")) ;
						
						
						detail[24]=(String) map.get("DOWN_PART_CODE");
						detail[25]=(String) map.get("DOWN_PART_NAME");
						detail[26]=String.valueOf(map.get("QUANTITY")) ;
						detail[27]=String.valueOf(map.get("PRICE")) ;
						detail[28]=String.valueOf(map.get("AMOUNT")) ;
			//			detail[29]=String.valueOf(map.get("RESP_TYPE")) ;
						
						
						list1.add(detail);
				      }
				    }
			    ClaimReportDao dao2  = ClaimReportDao.getInstance();
			    dao2.toExceVender(ActionContext.getContext().getResponse(), request, head, list1,name,"服务站索赔单",setList);
			    act.setForword(CLAIM_BILL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单导出");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 反索赔管理查询跳转页面
	 */
	public void claimCounterSystem() {
		
		
				
		try {
			RequestWrapper request = act.getRequest();
			TmBusinessAreaPO p = new TmBusinessAreaPO();
			List<TmBusinessAreaPO> list = dao.select(p);
			request.setAttribute("yieldly", list);
			
				act.setForword(CLAIM_COUNTER_SYSTEM);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 可索赔单查询结果
	 */
	public void applicationSystemQuery(){
		
		
				
		String dealerId = loginUser.getDealerId();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);     //公司ID
		StringBuffer con = new StringBuffer();
		Map<String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String claimNo = request.getParamValue("CLAIM_NO");
			String roNo = request.getParamValue("RO_NO");
			String lineNo = request.getParamValue("LINE_NO");
			String claimType = request.getParamValue("CLAIM_TYPE");
			String vin = request.getParamValue("VIN");
			String roStartdate = request.getParamValue("RO_STARTDATE");
			String roEnddate = request.getParamValue("RO_ENDDATE");
			String approveDate = request.getParamValue("approve_date");//审核通过时间查询条件
			String approveDate2 = request.getParamValue("approve_date2");
			String balanceApproveDate = request.getParamValue("balance_approve_date");//结算审核通过时间查询条件
			String balanceApproveDate2 = request.getParamValue("balance_approve_date2");
			String status = request.getParamValue("STATUS");
			String deliverer = request.getParamValue("DELIVERER");//客户姓名
			String delivererPhone = request.getParamValue("DELIVERER_PHONE");//客户电话
			String yieldly = request.getParamValue("YIELDLY_TYPE");//生产基地
			String isSecond = request.getParamValue("IS_SECOND"); // 是否二级
			String dealerCode = request.getParamValue("dealerCode"); // 是否二级
			String vin1 = "";
			
		if(Utility.testString(vin)){
			String[] vins = vin.split(",");
			for(int i=0;i<vins.length;i++){
				vin1 = vin1+"'"+vins[i]+"',";
			}
			vin1 = vin1.substring(0, vin1.length()-1);
		}
			map.put("dealerId", dealerId);
			map.put("claimNo", claimNo);
			map.put("roNo", roNo);
			map.put("lineNo", lineNo);
			map.put("claimType", claimType);
			map.put("vin", vin1);
			map.put("roStartdate", roStartdate);
			map.put("roEnddate", roEnddate);
			map.put("status", status);
			map.put("oemCompanyId", companyId.toString());
			map.put("track", "track"); //状态跟踪查询
			map.put("isApp", "isApp");
			map.put("delivererPhone", delivererPhone);
			map.put("deliverer", deliverer);
			map.put("approveDate", approveDate);
			map.put("approveDate2",approveDate2);
			map.put("balanceApproveDate", balanceApproveDate);
			map.put("balanceApproveDate2",balanceApproveDate2);
			map.put("yieldly",yieldly);
			map.put("isSecond",isSecond);
			map.put("dealerCode",dealerCode);
			
			PageResult<TtAsWrApplicationExtBean> ps = dao.applicationSystemQuery(loginUser, map,null, 10, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 反索赔查询页面
	 */
	public void claimCounterSelect() {
		
		
				
		try {
			RequestWrapper request = act.getRequest();
			TmBusinessAreaPO p = new TmBusinessAreaPO();
			List<TmBusinessAreaPO> list = dao.select(p);
			request.setAttribute("yieldly", list);
			
				act.setForword(CLAIM_COUNTER_SELECT);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 反索赔明细页面
	 */
	public void claimSystemDetailForward() {
		try {
			/*
			RequestWrapper request = act.getRequest();
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();
			String id = request.getParamValue("ID");
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			TtAsWrApplicationExtPO tawep = auditingDao
					.queryClaimOrderDetailById(id);
			List<Map<String, Object>> customerList = dao.getVinUserName2(id);
			Map<String, Object> customerMap = new HashMap<String, Object>();
			if (customerList != null && customerList.size() > 0)
				customerMap = customerList.get(0);

			// 取的当前需要审核的级别
			String authCode = tawep.getAuthCode();
			String authLevel = "--";
			if (authCode != null) {
				TtAsWrAuthinfoPO conditionPO = new TtAsWrAuthinfoPO();
				conditionPO.setApprovalLevelCode(authCode);
				List<TtAsWrAuthinfoPO> authLevelList = auditingDao
						.select(conditionPO);
				if (authLevelList != null && authLevelList.size() > 0) {
					TtAsWrAuthinfoPO authInfoPO = authLevelList.get(0);
					authLevel = CommonUtils.checkNull(authInfoPO
							.getApprovalLevelName());
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
			List<Map<String, Object>> appAuthls = dao.queryAppAuthDetail(id);// 审核授权信息
			TtAsWrWrauthorizationPO authReason = dao.queryAuthReason(id);// 需要授权原因
			List<Map<String, Object>> listOutRepair = dao.viewOutRepair(tawep
					.getRoNo());// 根据索赔单的工单号查询工单的外出维修信息
			if (listOutRepair != null && listOutRepair.size() > 0) {

				act.setOutData("listOutRepair", listOutRepair.get(0));
			}
			act.setOutData("application", tawep);
			act.setOutData("attachLs", attachLs);
			act.setOutData("ID", id);// 索赔申请单ID
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("authLevel", authLevel);// 需要审核的级别

			act.setForword(CLAIM_SYSTEM_DETAIL_URL);
			*/

			request.setAttribute("claim_type", DaoFactory.getParam(request, "claim_type"));
			request.setAttribute("type", "view");
			ClaimService claimservice=new ClaimServiceImpl();
			Map<String,Object> data =claimservice.findClaimPoById(request);
			Map<String,Object> outrepair=claimservice.findoutrepair(request);
			Map<String,Object> outrepairmoney=claimservice.findoutrepairmoney(request);
			List<Map<String,Object>> labours= claimservice.findLaboursById(request);
			List<Map<String,Object>> parts= claimservice.findPartsById(request);
			Map<String,Object> dataCom =claimservice.findComPoById(request);
			List<Map<String,Object>> dataAcc =claimservice.findAccPoById(request);
			List<Map<String,Object>> dataCarsystem =claimservice.findCarsystemById(request);
			if(checkListNull(labours)){
				act.setOutData("dataCarsystem", dataCarsystem.get(0));
			}
			if(checkListNull(labours)){
				act.setOutData("labours", labours);
			}
			if(checkListNull(parts)){
				act.setOutData("parts", parts);
			}
			if(checkListNull(dataAcc)){
				act.setOutData("acc", dataAcc);
			}
			String ywzj = DaoFactory.getParam(request, "id");
			List<FsFileuploadPO> fileList = claimservice.queryAttById(ywzj);// 取得附件
			act.setOutData("fileList", fileList);
			act.setOutData("t", data);
			act.setOutData("com", dataCom);
			act.setOutData("o", outrepairmoney);
			act.setOutData("r", outrepair);
			
			StringBuffer sql= new StringBuffer();
			sql.append("select ac.counter_remark,\n" );
			sql.append("a.id,to_char( a.create_date,'yyyy-MM-dd hh24:mi' ) sb_date ,\n" );
			sql.append("b.name sb_name\n" );
			sql.append(", to_char(a.report_date,'yyyy-MM-dd hh24:mi') sh_date,\n" );
			sql.append("d.name sh_name,\n" );
			sql.append("to_char(pf.create_date,'yyyy-MM-dd hh24:mi') hy_date,\n" );
			sql.append("pa.name  hy_name,\n" );
			sql.append("to_char(pf.SIGN_DATE,'yyyy-MM-dd hh24:mi') qs_date,\n" );
			sql.append("pb.name  qs_name,\n" );
			sql.append("to_char(pf.IN_WARHOUSE_DATE,'yyyy-MM-dd hh24:mi') rk_date,\n" );
			sql.append("pf.IN_WARHOUSE_NAME rk_name,\n" );
			sql.append("to_char(pc.create_date,'yyyy-MM-dd hh24:mi') kp_date,\n" );
			sql.append("pc.apply_person_name kp_name,\n" );
			sql.append("to_char(pd.COLLECT_TICKETS_DATE,'yyyy-MM-dd hh24:mi') sp_date,\n" );
			sql.append("pe.name sp_name,\n" );
			sql.append("to_char(pd.CHECK_TICKETS_DATE,'yyyy-MM-dd hh24:mi') yp_date,\n" );
			sql.append("pg.name zz_name,\n" );
			sql.append("to_char(pd.TRANSFER_TICKETS_DATE,'yyyy-MM-dd hh24:mi') zz_date,\n" );
			sql.append("ph.name zz_name,\n" );
			sql.append("to_char(pi.create_date,'yyyy-MM-dd hh24:mi') ck_date,\n" );
			sql.append("pj.name  ck_name,\n" );
			sql.append("to_char(phh.audit_date, 'yyyy-mm-dd hh24:mi') tp_date ,\n" );
			sql.append("ppp.name tp_name\n" );
			sql.append("\n" );
			sql.append("from  tc_user b , tt_as_wr_application a left join\n" );
			sql.append("(select\n" );
			sql.append("k.claim_id , max(k.audit_by) audit_by from\n" );
			sql.append(" tt_as_wr_app_audit_detail k group by  k.claim_id  ) c\n" );
			sql.append(" on a.id = c.claim_id\n" );
			sql.append("left join   tc_user d on  d.user_id = c.audit_by\n" );
			sql.append(" left join tt_as_wr_application_counter ac on ac.id=a.id \n" );
			sql.append("left join  (select aa.claim_id,max(aa.return_id) return_id from   tt_as_wr_old_returned_detail aa group by aa.claim_id ) p   on p.claim_id = a.id\n" );
			sql.append("left join tt_as_wr_old_returned pf on  pf.id = p.return_id\n" );
			sql.append("left join tc_user pa on pa.user_id = pf.create_by\n" );
			sql.append("left join tc_user pb on pb.user_id = pf.sign_person\n" );
			sql.append("left join tt_as_wr_claim_balance pc on pc.start_date < a.report_date and pc.end_date +1 > a.report_date and pc.dealer_id = a.dealer_id\n" );
			sql.append("left join ( select max(COLLECT_TICKETS) COLLECT_TICKETS\n" );
			sql.append(" , max(COLLECT_TICKETS_DATE )COLLECT_TICKETS_DATE ,\n" );
			sql.append(" max(CHECK_TICKETS) CHECK_TICKETS,\n" );
			sql.append(" max(COLLECT_TICKETS_DATE) CHECK_TICKETS_DATE,\n" );
			sql.append(" max(TRANSFER_TICKETS ) TRANSFER_TICKETS,\n" );
			sql.append(" max(TRANSFER_TICKETS_DATE) TRANSFER_TICKETS_DATE , balance_oder  from\n" );
			sql.append(" tt_as_payment  group by balance_oder     ) pd on pd.balance_oder = pc.remark\n" );
			sql.append(" left join tc_user pe on  pe.user_id = pd.COLLECT_TICKETS\n" );
			sql.append(" left join tc_user pg on  pg.user_id = pd.CHECK_TICKETS\n" );
			sql.append(" left join tc_user ph on  ph.user_id = pd.TRANSFER_TICKETS\n" );
			sql.append(" left join  (select claim_no, max(CREATE_DATE) CREATE_DATE , max(CREATE_by ) CREATE_by,max(out_no) out_no from    tt_as_wr_old_out_part group by  claim_no )  pi on pi.claim_no = a.claim_no\n" );
			sql.append(" left join  tc_user pj  on  pj.user_id = pi.CREATE_by\n" );
			sql.append(" left join Tt_As_Wr_Range_Single phh on  phh.out_no = pi.out_no\n" );
			sql.append(" left join tc_user ppp on ppp.user_id = phh.audit_by\n" );
			sql.append("where a.create_by = b.user_id  and  a.id ="+ request.getParamValue("id"));
			List<Map<String, Object>> pg = dao.pageQuery01(sql.toString(), null, dao.getFunName());
			act.setOutData("pg", pg.get(0));
			String claimId = pg.get(0).get("ID").toString();
			StringBuffer sb= new StringBuffer();
			sb.append("select d.audit_date,\n" );
			sb.append("       c.name as user_Name,\n" );
			sb.append("       d.audit_remark,\n" );
			sb.append("       (select tc.code_desc\n" );
			sb.append("          from tc_code tc\n" );
			sb.append("         where tc.code_id = d.audit_result) as audit_result\n" );
			sb.append("  from tt_as_wr_app_audit_detail d, tc_user c\n" );
			sb.append(" where d.audit_by = c.user_id\n");
			DaoFactory.getsql(sb, "d.claim_id", claimId, 1);
			List<Map<String, Object>> list  = dao.pageQuery(sb.toString(), null, dao.getFunName());
			act.setOutData("list", list);
			String opType = ActionUtil.getParamValue(request, "opType");
			if(StringUtils.isNotEmpty(opType)){
				act.setOutData("opType", opType);
			}
			act.setForword(COUNTER_EDIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 增加新表工时的数据，在插入其他数据一起
	 */
	public void claimSystemLabourids(){
		try {
			String[] labourids = request.getParamValues("labourIds");//工时IDS
			String claimid = request.getParamValue("claimid");//索赔单ID
			String username=String.valueOf(loginUser.getUserId());//操作人
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String currtime = sdf.format(new Date());
			HashSet<String> set=new LinkedHashSet<String>();
			for (String labourid : labourids) {
				set.add(labourid);
			}
			Double gstemp=0d;//工时费用
			Double cltemp=0d;//配件费用
			Double accTemp=0d;//辅料费
			Double comTemp=0d;//补偿费
			
			for (String labourid : set) {
				String[] authremark = request.getParamValues("authremark"+labourid);//审核意见
				String Labour = request.getParamValue("Labour"+labourid);
				
				gstemp = gstemp+ Double.parseDouble(Labour) ;  
				String[] partAmount = request.getParamValues("PARTA"+labourid);
				HashSet<String> partamounts=new LinkedHashSet<String>();
				for (String partamount : partAmount) {
					partamounts.add(partamount);
				}
				for (String partamount : partamounts){
					cltemp = cltemp+ Double.parseDouble(partamount) ;
				}
				//=================zyw 2015-3-18 把对应的辅料费和补偿费计算进去
				accTemp=dao.findAccByLabourid(labourid);
				comTemp=dao.findComByLabourid(labourid);
				dao.claimSystemLabourids(labourid,claimid,authremark[0]);
			}
			Double balance_amount = cltemp+gstemp+accTemp+comTemp;
			
			dao.claimSystemLabourids(claimid,balance_amount,gstemp,cltemp,accTemp,comTemp,username,currtime);
			
			TmBusinessAreaPO p = new TmBusinessAreaPO();
			List<TmBusinessAreaPO> list = dao.select(p);
			request.setAttribute("yieldly", list);
			act.setForword(CLAIM_COUNTER_SYSTEM);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void claimCounter() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String claim_no = ActionUtil.getParamValue(request, "claim_no");
			// 反索赔的索赔单号不能为空
			if(StringUtils.isNotEmpty(claim_no)){
				TtAsWrApplicationCounterPO po = new TtAsWrApplicationCounterPO();
				po.setClaimNo(claim_no);
				ClaimBillTrackService cbtService = new ClaimBillTrackServiceImpl();
				String counterRemark = ActionUtil.getParamValue(request, "counter_remark");
				// 进行反索赔的业务处理
				boolean flag = cbtService.claimCounter(po, counterRemark);
				if(flag){
					act.setOutData("hint", "反索赔成功");
					act.setOutData("_success", true);
				} else {
					act.setOutData("hint", "反索赔失败");
					act.setOutData("_success", false);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "保存反索赔操作",e.getMessage());
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询反索赔的信息列表
	 */
	public void applicationSelectQuery(){
		
		
				
		String dealerId = loginUser.getDealerId();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);     //公司ID
		Map<String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String claimNo = request.getParamValue("CLAIM_NO");
			String roNo = request.getParamValue("RO_NO");
			String lineNo = request.getParamValue("LINE_NO");
			String claimType = request.getParamValue("CLAIM_TYPE");
			String vin = request.getParamValue("VIN");
			String roStartdate = request.getParamValue("RO_STARTDATE");
			String roEnddate = request.getParamValue("RO_ENDDATE");
			String approveDate = request.getParamValue("approve_date");//审核通过时间查询条件
			String approveDate2 = request.getParamValue("approve_date2");
			String balanceApproveDate = request.getParamValue("balance_approve_date");//结算审核通过时间查询条件
			String balanceApproveDate2 = request.getParamValue("balance_approve_date2");
			String status = request.getParamValue("STATUS");
			String deliverer = request.getParamValue("DELIVERER");//客户姓名
			String delivererPhone = request.getParamValue("DELIVERER_PHONE");//客户电话
			String yieldly = request.getParamValue("YIELDLY_TYPE");//生产基地
			String isSecond = request.getParamValue("IS_SECOND"); // 是否二级
			String dealerCode = request.getParamValue("dealerCode"); // 是否二级
			String vin1 = "";
			
		if(Utility.testString(vin)){
			String[] vins = vin.split(",");
			for(int i=0;i<vins.length;i++){
				vin1 = vin1+"'"+vins[i]+"',";
			}
			vin1 = vin1.substring(0, vin1.length()-1);
		}
			map.put("dealerId", dealerId);
			map.put("claimNo", claimNo);
			map.put("roNo", roNo);
			map.put("lineNo", lineNo);
			map.put("claimType", claimType);
			map.put("vin", vin1);
			map.put("roStartdate", roStartdate);
			map.put("roEnddate", roEnddate);
			map.put("status", status);
			map.put("oemCompanyId", companyId.toString());
			map.put("track", "track"); //状态跟踪查询
			map.put("isApp", "isApp");
			map.put("delivererPhone", delivererPhone);
			map.put("deliverer", deliverer);
			map.put("approveDate", approveDate);
			map.put("approveDate2",approveDate2);
			map.put("balanceApproveDate", balanceApproveDate);
			map.put("balanceApproveDate2",balanceApproveDate2);
			map.put("yieldly",yieldly);
			map.put("isSecond",isSecond);
			map.put("dealerCode",dealerCode);
			
			
			PageResult<TtAsWrApplicationExtBean> ps = dao.applicationSelectQuery(loginUser, map,null, 10, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 反索赔明细页面
	 */
	public void claimSelectDetailForward() {
	    
	    
	        
	    try {
	      RequestWrapper request = act.getRequest();
	      ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();
	      String id = request.getParamValue("ID");
	      String isAudit = request.getParamValue("isAudit");//是否是索赔审核（true:是，false:索赔明细查询）
	      TtAsWrApplicationExtPO tawep = auditingDao.queryClaimOrderDetailById(id);
	      List<Map<String, Object>> customerList = dao.getVinUserName2(id);
	      Map<String,Object> customerMap = new HashMap<String, Object>();
	      if(customerList!=null && customerList.size()>0)
	        customerMap = customerList.get(0);
	      
	      //取的当前需要审核的级别
	      String authCode = tawep.getAuthCode();
	      String authLevel = "--";
	      if(authCode!=null){
	    	  TtAsWrAuthinfoPO conditionPO = new TtAsWrAuthinfoPO();
	    	  conditionPO.setApprovalLevelCode(authCode);
	    	  List<TtAsWrAuthinfoPO> authLevelList = auditingDao.select(conditionPO);
	    	  if(authLevelList!=null && authLevelList.size()>0){
	    		  TtAsWrAuthinfoPO authInfoPO = authLevelList.get(0);
	    		  authLevel = CommonUtils.checkNull(authInfoPO.getApprovalLevelName());
	    	  }
	      }
	      if(loginUser.getDealerId() != null )
	      {
	    	  act.setOutData("type", 1);
	      }
	      //取得产地名称
	      String yieldly = tawep.getYieldly();
	      String yieldlyName = "";
	      if(yieldly!=null){
	        TmBusinessAreaPO ap = CommonUtils.getName(yieldly);
	        if(ap!=null && ap.getAreaName()!=null)
	          yieldlyName = ap.getAreaName();
	      }
	      tawep.setYieldlyName(yieldlyName);
	      TtAsWrApplicationCounterPO  applicationCounterPO = new TtAsWrApplicationCounterPO();
	      applicationCounterPO.setId(Long.parseLong(id));
	      List<TtAsWrApplicationCounterPO> list= dao.select(applicationCounterPO);
	      act.setOutData("IsInvoice",list.get(0).getIsInvoice());
	      List<ClaimListBean> partls = dao.queryPartCounterById(id); //取配件信息
	      List<ClaimListBean> itemls = dao.queryItemCounterById(id); //取工时
	      List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);//取其他项目
	      List<FsFileuploadPO> attachLs = dao.queryAttById(id);//取得附件
	      List<Map<String, Object>> appAuthls = dao.queryAppAuthDetail(id);//审核授权信息
	      TtAsWrWrauthorizationPO authReason = dao.queryAuthReason(id);//需要授权原因
	      List<Map<String,Object>> listOutRepair = dao.viewOutRepair(tawep.getRoNo());//根据索赔单的工单号查询工单的外出维修信息
	      if(listOutRepair!=null&&listOutRepair.size()>0){
	    	  
	    	  act.setOutData("listOutRepair",listOutRepair.get(0));
	      }
	      act.setOutData("application", tawep);
	      act.setOutData("attachLs", attachLs);
	      act.setOutData("ID", id);//索赔申请单ID
	      act.setOutData("itemLs", itemls);//索赔单之工时信息
	      act.setOutData("partLs", partls);//索赔单之配件信息
	      act.setOutData("otherLs",otherls);//索赔单之其他项目信息
	      act.setOutData("appAuthls",appAuthls);//索赔单之审核信息
	      act.setOutData("authReason",authReason);//索赔单之审核原因
	      act.setOutData("isAudit", isAudit);//true:审核页面 false:明细查询页面
	      act.setOutData("customerMap", customerMap);//客户信息
	      act.setOutData("authLevel", authLevel);//需要审核的级别
	      
	      
	      act.setForword(CLAIM_SELECT_DETAIL_URL);
	    } catch (Exception e) {
	      BizException e1 = new BizException(act, e,
	          ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
	      logger.error(loginUser, e1);
	      act.setException(e1);
	    }
	 }
	
	
	public void claimSelectLabourids() {
		
		
		try {
			RequestWrapper request = act.getRequest();
			String[] labourids = request.getParamValues("labourIds");//工时IDS
			String[] labourIdsCode = request.getParamValues("labourIdsCode");//工时CODE
			
			String countlabourIds = request.getParamValue("countlabourIds");//length
			String claimid = request.getParamValue("claimid");//索赔单ID
			if(countlabourIds.equals( String.valueOf(labourids.length) ))
			{
				TtAsWrApplicationCounterPO counterPO = new TtAsWrApplicationCounterPO();
				counterPO.setId(Long.parseLong(claimid));
				dao.delete(counterPO);
				
				TtAsWrLabouritemCounterPO asWrLabouritemCounterPO = new TtAsWrLabouritemCounterPO();
				asWrLabouritemCounterPO.setId(Long.parseLong(claimid));
				dao.delete(asWrLabouritemCounterPO);
				
				TtAsWrPartsitemCounterPO counterPO2 =  new TtAsWrPartsitemCounterPO();
				counterPO2.setId(Long.parseLong(claimid));
				dao.delete(counterPO2);
				
			}else
			{
				int i = 0;
				Double gstemp=0d;//工时费用
				Double cltemp=0d;//配件费用
				Double accTemp=0d;//辅料费
				Double comTemp=0d;//补偿费
				for (String labourid : labourids) {
					String Labour = request.getParamValue("Labour"+labourid);
					gstemp = gstemp+ Double.parseDouble(Labour) ;  
					String[] PARTA = request.getParamValues("PARTA"+labourid);
					for (String parta : PARTA)
					{
						cltemp = cltemp+ Double.parseDouble(parta) ;
					}
					TtAsWrPartsitemCounterPO asWrPartsitemCounterPO = new TtAsWrPartsitemCounterPO();
					asWrPartsitemCounterPO.setId(Long.parseLong(claimid));
					asWrPartsitemCounterPO.setWrLabourcode(labourIdsCode[i]);
					dao.delete(asWrPartsitemCounterPO);
					
					TtAsWrLabouritemCounterPO asWrLabouritemCounterPO = new TtAsWrLabouritemCounterPO();
					asWrLabouritemCounterPO.setId(Long.parseLong(claimid));
					asWrLabouritemCounterPO.setLabourId(Long.parseLong(labourid));
					dao.delete(asWrLabouritemCounterPO);
					//=================zyw 2015-3-18 把对应的辅料费和补偿费计算进去
					accTemp=dao.findAccByLabourid(labourid);
					comTemp=dao.findComByLabourid(labourid);
					i++;
				}
				Double balance_amount = cltemp+gstemp+accTemp+comTemp;
				dao.updateClaimCounter(balance_amount,claimid,gstemp,cltemp,accTemp,comTemp);
				

			}
			TmBusinessAreaPO p = new TmBusinessAreaPO();
			List<TmBusinessAreaPO> list = dao.select(p);
			request.setAttribute("yieldly", list);
			act.setForword(CLAIM_COUNTER_SELECT);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 索赔单申请明细(含字表)
	 */
	public void claimDetailTc() {
		
		
				
		try {
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			String sql = "SELECT * FROM tm_org WHERE org_level = 2";
			List<Map<String, Object>> org = dao.pageQuery01(sql, null, dao.getFunName());
			act.setOutData("org", org);
			//车型
			act.setOutData("modelList", commonUtilActions.getAllModel());
				act.setForword(CLAIM_DETAIL_URL);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void applicationQuery2() {
		
		
				
		RequestWrapper request = act.getRequest();
		Integer page = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页	
		PageResult<Map<String, Object>> ps = dao.queryApplicationDeatail(request,loginUser, Constant.PAGE_SIZE, page);
		act.setOutData("ps", ps);
	}
	
}
