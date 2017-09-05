package com.infodms.dms.actions.claim.other;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.other.BonusDAO;
import com.infodms.dms.dao.sales.planmanage.MonthPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFineNewsPO;
import com.infodms.dms.po.TmpVsMonthlyPlanPO;
import com.infodms.dms.po.TtAsWrFinePO;
import com.infodms.dms.po.TtAsWrFineTemporaryPO;
import com.infodms.dms.po.TtVsMonthlyPlanDetailPO;
import com.infodms.dms.po.TtVsMonthlyPlanPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
* @ClassName: Bonus 
* @Description: TODO(正负激励) 
* @author wangchao 
* @date Jun 16, 2010 4:19:54 PM 
*
 */
public class Bonus  extends BaseImport{
	private Logger logger = Logger.getLogger(Bonus.class);
	private final BonusDAO dao = BonusDAO.getInstance();
	private final String BONUS_URL = "/jsp/claim/other/bonus.jsp";// 主页面（查询）
	private final String ENCOURAGE_URL = "/jsp/claim/other/encourage.jsp"; //处罚页面
	private final String PUNISH_HISTORY_URL = "/jsp/claim/other/punishHistory.jsp"; //查询 处罚历史
	private final String NEW_LIST_URL = "/jsp/claim/other/newsList.jsp"; //首页新闻列表
	private final String BONUS_DLR_URL = "/jsp/claim/other/bonusDlr.jsp";// 经销商主页面（查询）
	private final String BONUS_DLR_DETAIL = "/jsp/claim/other/encourageDetailDlr.jsp";// 经销商主页面（查询）
	private final String BONUS_OEM_DETAIL = "/jsp/claim/other/encourageDetailDlrOEM.jsp";// 车场主页面（查询）
	private final String add_ENCOURAGE_URL = "/jsp/claim/other/addEncourage.jsp"; //奖励页面
	private final String Import_OEM_PANI = "/jsp/claim/other/importEncourage.jsp"; //初始化导入页面
	private final String export_OEM_PANI = "/jsp/claim/other/exportEncourage.jsp"; //初始化导出页面
	private final String IMPORT_OEM_FAILURE = "/jsp/claim/other/importEncourageFailure.jsp"; //导入失败
	private final String IMPORT_OEM_FAILURE_del = "/jsp/claim/other/importEncourageFailureDel.jsp"; //导入失败明细
	private final String IMPORT_OEM_Success = "/jsp/claim/other/importEncourageFailuresuccess.jsp";
	

	/**
	 * 
	* @Title: bonusForward 
	* @Description: TODO(正负激励跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void bonusForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		
		try {
			/*//区分微车和轿车系统
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List list = dao.select(code) ;
			if(list.size()>0){
				code = (TcCodePO)list.get(0);
			}
				act.setOutData("code", code.getCodeId()) ;*/
			//queryg();
			act.setForword(BONUS_URL);
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
	* @Description: TODO(处罚页面跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void punishForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			RequestWrapper request = act.getRequest();
			String dealerId = request.getParamValue("DEALER_ID");
			TmDealerPO dealer = new TmDealerPO();
			dealer.setDealerId(Long.valueOf(dealerId));
			TmDealerPO dealerValue = (TmDealerPO) dao.select(dealer).get(0);
			
			TmBusinessAreaPO po = new TmBusinessAreaPO();
			List<PO> list = dao.select(po);
			act.setOutData("Area", list);
			act.setOutData("DEALER_ID", dealerId);
			act.setOutData("encourageMan", logonUser.getName());
			act.setOutData("dealerName", dealerValue.getDealerName());
			act.setOutData("dealerCode", dealerValue.getDealerCode());
			act.setOutData("encourageDate", sdf.format(new Date()));
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
	* @Title: punishForward 
	* @Description: TODO(奖励页面跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void addPunishForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			RequestWrapper request = act.getRequest();
			String dealerId = request.getParamValue("DEALER_ID");
			TmDealerPO dealer = new TmDealerPO();
			dealer.setDealerId(Long.valueOf(dealerId));
			TmDealerPO dealerValue = (TmDealerPO) dao.select(dealer).get(0);
			TmBusinessAreaPO po = new TmBusinessAreaPO();
			List<PO> list = dao.select(po);
			act.setOutData("Area", list);
			act.setOutData("DEALER_ID", dealerId);
			act.setOutData("encourageMan", logonUser.getName());
			act.setOutData("dealerName", dealerValue.getDealerName());
			act.setOutData("dealerCode", dealerValue.getDealerCode());
			act.setOutData("encourageDate", sdf.format(new Date()));
			act.setForword(add_ENCOURAGE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: punishHistoryForward 
	* @Description: TODO(处罚历史跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void punishHistoryForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			RequestWrapper request = act.getRequest();
			String dealerId = request.getParamValue("DEALER_ID");
			TmBusinessAreaPO po = new TmBusinessAreaPO();
			List<PO> list = dao.select(po);
			act.setOutData("Area", list);
			act.setOutData("DEALER_ID", dealerId);
			act.setForword(PUNISH_HISTORY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: queryDealer 
	* @Description: TODO(查询经销商) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void queryDealer () {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			//从session中取得车厂公司id		
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);		
			String dealerCode = request.getParamValue("DEALER_CODE");
			String dealerName = request.getParamValue("DEALER_NAME");

			Map<String,String> param = new HashMap();
			param.put("dealerCode", dealerCode);
			param.put("dealerName", dealerName);

			param.put("oemCompanyId", String.valueOf(oemCompanyId));
			PageResult<Map<String, Object>> ps =  dao.queryDealer(param, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps); 
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
//导出查询	
	   public void queryOEMDealer () {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			//从session中取得车厂公司id		
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);		
			String dealerCode = request.getParamValue("DEALER_CODE");
			String dealerName = request.getParamValue("DEALER_NAME");
			String deductStartDate = CommonUtils.checkNull(request.getParamValue("deductStartDate"));
			String deductEndDate = CommonUtils.checkNull(request.getParamValue("deductEndDate"));
			String SubsidiesType = CommonUtils.checkNull(request.getParamValue("SubsidiesType"));
			String ButieBh = CommonUtils.checkNull(request.getParamValue("ButieBh"));
			Map<String,String> param = new HashMap();
			param.put("dealerCode", dealerCode);
			param.put("dealerName", dealerName);
			param.put("deductStartDate", deductStartDate);
			param.put("deductEndDate", deductEndDate);
			param.put("SubsidiesType", SubsidiesType);
			param.put("ButieBh", ButieBh);
			param.put("oemCompanyId", String.valueOf(oemCompanyId));
			PageResult<Map<String, Object>> ps  =  dao.exportqueryOemDealer(param, Constant.PAGE_SIZE, curPage);

		//PageResult<Map<String, Object>> ps =  dao.queryOemDealer(param, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);	
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	   
	   public void resourcesAuditDown(){
			ActionContext act = ActionContext.getContext();
			OutputStream os = null;
			RequestWrapper request = act.getRequest();

			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			try{
				
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);		
				String dealerCode = request.getParamValue("DEALER_CODE");
				String dealerName = request.getParamValue("DEALER_NAME");
				String deductStartDate = CommonUtils.checkNull(request.getParamValue("deductStartDate"));
				String deductEndDate = CommonUtils.checkNull(request.getParamValue("deductEndDate"));
				String SubsidiesType = CommonUtils.checkNull(request.getParamValue("SubsidiesType"));
				String ButieBh = CommonUtils.checkNull(request.getParamValue("ButieBh"));

				Map<String,String> param = new HashMap();
				param.put("dealerCode", dealerCode);
				param.put("dealerName", dealerName);
				param.put("deductStartDate", deductStartDate);
				param.put("deductEndDate", deductEndDate);
				param.put("SubsidiesType", SubsidiesType);
				param.put("ButieBh", ButieBh);
				
				param.put("oemCompanyId", String.valueOf(oemCompanyId));

			    Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					PageResult<Map<String, Object>> ps =  dao.exportqueryOemDealer(param, Constant.PAGE_SIZE, curPage);

					Map map=new HashMap();

					
					ResponseWrapper response = act.getResponse();
					// 导出的文件名
					String fileName = "正负激励下载.csv";
					// 导出的文字编码
					fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
					response.setContentType("Application/text/csv");
					response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
					// 定义一个集合
					List<List<Object>> list = new LinkedList<List<Object>>();
					// 标题
					List<Object> listTemp = new LinkedList<Object>();
					listTemp.add("序号");
					listTemp.add("补贴编号");
					listTemp.add("产地");
					listTemp.add("开票通知单号");
					listTemp.add("经销商代码");
					listTemp.add("经销商名称");
					listTemp.add("奖励类型");
					listTemp.add("奖励劳务费");
					listTemp.add("奖励材料费");
					listTemp.add("补贴类型");
					listTemp.add("导入人");
					listTemp.add("导入时间");
					list.add(listTemp);
					List<Map<String, Object>> rslist = ps.getRecords();
					if(rslist!=null){
					for (int i = 0; i < rslist.size(); i++) {
						map = rslist.get(i);
						List<Object> listValue = new LinkedList<Object>();
						listValue = new LinkedList<Object>();
						listValue.add(i+1);
						listValue.add(map.get("LABOUR_BH") != null ? map.get("LABOUR_BH") : "");
						listValue.add(map.get("AREA_NAME") != null ? map.get("AREA_NAME") : "");
						listValue.add(map.get("BALANCE_ODER") != null ? map.get("BALANCE_ODER") : "");
						listValue.add(map.get("DEALER_CODE") != null ? map.get("DEALER_CODE") : "");
						listValue.add(map.get("DEALER_NAME") != null ? map.get("DEALER_NAME") : "");
						String Status=null;
						if(map.get("FINE_TYPE") != null){
							System.err.println(map.get("FINE_TYPE"));
							if(Integer.parseInt(map.get("FINE_TYPE").toString())==Constant.FINE_TYPE_02){
								Status="奖励";
							}else{
								Status="扣款";
							}
							
						}else {
							Status="";
							
						}
						listValue.add(Status);
						listValue.add(map.get("LABOUR_SUM") != null ? map.get("LABOUR_SUM") : "");
						listValue.add(map.get("DATUM_SUM") != null ? map.get("DATUM_SUM") : "");
						listValue.add(map.get("REMARK") != null ? map.get("REMARK") : "");
						listValue.add(map.get("NAME") != null ? map.get("NAME") : "");
						listValue.add(map.get("FINE_DATE") != null ? map.get("FINE_DATE") : "");
						list.add(listValue);
					}
					}
					os = response.getOutputStream();
					CsvWriterUtil.writeCsv(list, os);
					os.flush();			
			} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"省份价格调整下载失败");
				logger.error(logonUser,e1);
				act.setException(e1);
				}
			
			
		}
	/**
	 * 
	* @Title: punish 
	* @Description: TODO(罚款处理) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void punish () {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long createBy = logonUser.getUserId();
		
		try {
			RequestWrapper request = act.getRequest();
			@SuppressWarnings("unused")
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String dealerId = request.getParamValue("DEALER_ID"); //经销商ID
			String fineReason = request.getParamValue("FINE_REASON"); //罚款原因
			String remark = request.getParamValue("remark"); //备注
			String yieldly = request.getParamValue("yieldly");//奖励产地
			String fineType= request.getParamValue("fine_type");
			String labour_sum = request.getParamValue("labour_sum"); //扣款金额
			String labour_type = request.getParamValue("labour_type"); 
			//String ButieBh= request.getParamValue("ButieBh");

			TtAsWrFinePO tawfp = new TtAsWrFinePO();
			tawfp.setFineId(Utility.getLong(SequenceManager.getSequence("")));
			tawfp.setDealerId(Utility.getLong(dealerId));
			if("80641001".equals(fineType)){
				tawfp.setLabourSum(0-Double.parseDouble(labour_sum));
			}else{
				tawfp.setLabourSum(Double.parseDouble(labour_sum));
			}			
			tawfp.setLabourType(Long.parseLong(labour_type));
			tawfp.setFineReason(fineReason);
			tawfp.setFineDate(new Date());
			tawfp.setPayStatus(Utility.getInt(Constant.PAY_STATUS_01));
			tawfp.setPayDate(new Date());
			tawfp.setRemark(remark);
			tawfp.setCreateBy(createBy);
			tawfp.setCreateDate(new Date());
			//tawfp.setLabourBh(Long.parseLong(ButieBh));
			tawfp.setYieldly(Long.parseLong(yieldly));
			tawfp.setFineType(Integer.valueOf(fineType));
			dao.insert(tawfp);
			String[] newsIds = request.getParamValues("newsId");
			if(newsIds!=null){
				for(int i = 0; i < newsIds.length; i++){
					TmFineNewsPO tfn = new TmFineNewsPO();
					tfn.setFineNewsId(Utility.getLong(SequenceManager.getSequence("")));
					tfn.setFineId(tawfp.getFineId());
					tfn.setNewsId(Long.valueOf(newsIds[i]));
					tfn.setCreateBy(logonUser.getUserId());
					tfn.setCreateDate(new Date());
					dao.insert(tfn);
				}
			}
			
			act.setOutData("msg", "00");	
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: punish 
	* @Description: TODO(罚款处理) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void punishModify () {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long createBy = logonUser.getUserId();
		
		try {
			RequestWrapper request = act.getRequest();
			String fineId = request.getParamValue("fine_id");
			String dealerId = request.getParamValue("dealerId"); //经销商ID
			String fineSum = request.getParamValue("FINE_SUM"); //罚款金额
			String fineReason = request.getParamValue("FINE_REASON"); //罚款原因
			String remark = request.getParamValue("REMARK"); //备注
			String yieldly = request.getParamValue("yieldly");//奖励产地
			String fineType= request.getParamValue("fine_type");
			TtAsWrFinePO tawfp = new TtAsWrFinePO();
			TtAsWrFinePO tawfp2 = new TtAsWrFinePO();
			tawfp2.setFineId(Utility.getLong(fineId));
			tawfp.setFineId(Utility.getLong(SequenceManager.getSequence("")));
			tawfp.setDealerId(Utility.getLong(dealerId));
			tawfp.setFineSum(Utility.getDouble(fineSum));
			tawfp.setFineReason(fineReason);
			//tawfp.setPayStatus(Utility.getInt(Constant.PAY_STATUS_01));
			//tawfp.setPayDate(new Date());
			tawfp.setRemark(remark);
			//tawfp.setCreateBy(createBy);
			//tawfp.setCreateDate(new Date());
			//tawfp.setYieldly(Integer.parseInt(yieldly));
			//tawfp.setFineType(Integer.valueOf(fineType));
			dao.update(tawfp2,tawfp);
			bonusForward();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: punishHistory 
	* @Description: TODO(查询罚款历史) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void punishHistory() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String deductStartDate = CommonUtils.checkNull(request.getParamValue("deductStartDate"));
			String deductEndDate = CommonUtils.checkNull(request.getParamValue("deductEndDate"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页 
			String dealerId = request.getParamValue("DEALER_ID");
			TtAsWrFinePO twafp = new TtAsWrFinePO();
			twafp.setDealerId(Utility.getLong(dealerId));
			PageResult<Map<String,Object>> ps =  dao.punishQuery(dealerId,deductStartDate,deductEndDate,status,yieldly, 10, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: punishHistory 
	* @Description: TODO(新闻查询) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void newsQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String newId = CommonUtils.checkNull(request.getParamValue("newId"));
				Map<String,Object> map = dao.queryNewsById(newId);
				act.setOutData("map", map);
			}else{
				act.setForword(NEW_LIST_URL);
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
	* @Title: queryDealer 
	* @Description: TODO(经销商查询正负激励) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void queryDealerByDlr () {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				//queryg();
				String deductStartDate = CommonUtils.checkNull(request.getParamValue("deductStartDate"));
				String deductEndDate = CommonUtils.checkNull(request.getParamValue("deductEndDate"));
				String status = CommonUtils.checkNull(request.getParamValue("status"));
				String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));
				Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
				//从session中取得车厂公司id		
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);		
				String dealerCode = request.getParamValue("DEALER_CODE");
				String dealerName = request.getParamValue("DEALER_NAME");
				Map<String,String> param = new HashMap();
				param.put("dealerId", logonUser.getDealerId());
//				param.put("dealerCode", dealerCode);
//				param.put("dealerName", dealerName);
//				param.put("oemCompanyId", String.valueOf(oemCompanyId));
				PageResult<Map<String,Object>> ps =  
					dao.punishQuery(logonUser.getDealerId(),deductStartDate,deductEndDate,status,yieldly, 10, curPage);
				act.setOutData("ps", ps);
			}else{
				TmBusinessAreaPO po = new TmBusinessAreaPO();
				List<PO> list = dao.select(po);
				act.setOutData("Area", list);
				act.setForword(BONUS_DLR_URL);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/*@SuppressWarnings("unchecked")
	public void queryg () {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
				List<Map<String,Object>> list = dao.Queryg();
				if(list.size()>0){
					for(int i=0;i<list.size();i++){
						if(list.get(i).get("PAY_STATUS").toString().equals(Constant.PAY_STATUS_01)){
							TtAsWrFinePO f=new TtAsWrFinePO();
							f.setFineId(Long.parseLong(list.get(i).get("FINE_ID").toString()));
							TtAsWrFinePO upf=new TtAsWrFinePO();
							upf.setPayStatus(Integer.parseInt(Constant.PAY_STATUS_02));
							dao.update(f, upf);
						}
					}
				}				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}*/
	/**
	 * 
	* @Title: queryDealer 
	* @Description: TODO(经销商查询正负激励明细) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void queryDealerByDlrDetail () {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String fineId = CommonUtils.checkId(request.getParamValue("fineId"));
			Map<String,Object> mapEncourage = dao.dlrQueryEncourageDetail(fineId);
			List<Map<String,Object>> listNews = dao.dlrQueryEncourageNewsDetail(fineId);
			
			act.setOutData("mapEncourage", mapEncourage);
			act.setOutData("listNews", listNews);
			
			if(!logonUser.getUserType().equals(Constant.SYS_USER_SGM)){
				act.setForword(BONUS_DLR_DETAIL);
			}else{
				act.setForword(BONUS_OEM_DETAIL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//删除
	public void delqueryDealerByDlrDetail () {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String fineId = CommonUtils.checkId(request.getParamValue("fineId"));
			TtAsWrFinePO  PO1 = new TtAsWrFinePO();
			PO1.setFineId(Long.parseLong(fineId));
			dao.delete(PO1); 
			
			String dealerId = request.getParamValue("dealerId");
			TmBusinessAreaPO po = new TmBusinessAreaPO();
			List<PO> list = dao.select(po);
			act.setOutData("Area", list);
			act.setOutData("DEALER_ID", dealerId);

		
			act.setOutData("msg", "00");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
    /**
     * 加载正负激励导入页面
     */
    public void ImportDealerByDlrInit(){
    	ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {

				act.setForword(Import_OEM_PANI);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "正负激励导入加载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
    	
    }
    /**
     * 加载正负激励导出页面
     */
    public void exportDealerByDlrInit(){
    	ActionContext act = ActionContext.getContext();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(
    			Constant.LOGON_USER);
    	RequestWrapper request = act.getRequest();
    	try {
    		
    		act.setForword(export_OEM_PANI);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "正负激励导入加载");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
    	
    }
    
 /**
 * 正负激励 导入模板下载
 */
    public void downloadDealerByDlr(){
	  
    	ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();

			
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("补贴编号");
			listHead.add("产地");
			listHead.add("经销商代码");
			listHead.add("奖惩类型（奖励/扣款）");
			listHead.add("奖惩劳务费");
			listHead.add("奖惩材料费");
			listHead.add("奖惩原因");
			listHead.add("补贴类型");

			
			list.add(listHead);

			// 导出的文件名
			String fileName = "正负激励导入模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
    		} catch (Exception e) {
    			BizException e1 = new BizException(act, e,
    					ErrorCodeConstant.QUERY_FAILURE_CODE, "正负激励 导入模板下载");
    			logger.error(logonUser, e1);
    			act.setException(e1);
    		}	
    	
   }
    /**
     * 导入临时表并进行规则校验
     */
    public void exceloadDealerByDlr(){
    	ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
		RequestWrapper request = act.getRequest();

		 long maxSize=1024*1024*5;
		 int a=insertIntoTmp(request, "uploadFile",8,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(IMPORT_OEM_FAILURE);
			}else{
				List<Map> list=getMapList();
				
				TtAsWrFineTemporaryPO po= new TtAsWrFineTemporaryPO();
				dao.delete(po);//删除临时表数据
				//将数据插入临时表
				Map<String,Object> map1 =insertTmpYearlyPlan(list);
				   if(map1!=null&&map1.size()>0){
					   act.setOutData("errorInfo", map1);
					   act.setForword(IMPORT_OEM_FAILURE_del);;
				   }else{
				
					//	List<ExcelErrors> errorList=checkData();//校验数据
					   
					   //查询临时表的数据
					   List<Map<String, Object>>  list2 =dao.QueryFineTemporary();
					   act.setOutData("list", list2);
					   act.setForword(IMPORT_OEM_Success);
					   
			          }
				   }
			}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"正负激励导入临时表读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    	}
    
    public void importExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {

			dao.insertPlan();
			act.setForword(BONUS_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    	
    	
    }
    /*
	 * 把所有导入记录插入tt_as_wr_fine_temporary
	 */
	public Map<String,Object> insertTmpYearlyPlan(List<Map> list) throws Exception{
		Map<String,Object> map1 =new HashMap<String, Object>();
		if(null==list){
			list=new ArrayList();
		}
		String  aa="";
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while(it.hasNext()){
				key=(String)it.next();
				Cell[] cells=(Cell[])map.get(key);
			    aa=parseCells(key, cells);
		
			    if(aa!=null){
			    	map1.put("num",key);
			    	map1.put("err", aa);
					return map1;

			    }
			
			}
		
		}
		return null;
		
	}
	/*
	 * 每一行插入tt_as_wr_fine_temporary（昌河汽车）
	 * 数字只截取30位
	 * modify by wenyd at 2013-07-30
	 * 
	 */
	public String parseCells(String rowNum,Cell[] cells) throws Exception{
    	ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			TtAsWrFineTemporaryPO po= new TtAsWrFineTemporaryPO();
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
            if(CheckUtil.checkFormatNumber(cells[0].getContents())){
    			po.setLabourBh(Long.parseLong(cells[0].getContents()));
            }else{
        		return "补贴编号只能为数字且第一位不能为0";
            }
			TmBusinessAreaPO PO1 =new TmBusinessAreaPO();
			PO1.setAreaName(subCell(cells[1].getContents().trim()));//产地对应第二列
			 List<TmBusinessAreaPO>   areaId=	dao.select(PO1);
			// po.set
			 if(areaId!=null&&areaId.size()>0){
			  po.setYieldly(areaId.get(0).getAreaId());
			}else{
				return "产地在系统中不存在！";
			}

			TmDealerPO PO =new TmDealerPO();
			String fineId=SequenceManager.getSequence("");

			po.setFineId(Long.parseLong(fineId));
			PO.setDealerType(Constant.DEALER_TYPE_DWR);
			PO.setDealerCode(subCell(cells[2].getContents().trim()));
			 List<TmDealerPO>   Dealer=	dao.select(PO);
			 if(Dealer!=null&&Dealer.size()>0){
			po.setDealerId(Dealer.get(0).getDealerId());
			PO.setDealerName(po.getDealerName());
			}else{
				return "经销商编码在系统中不存在！";
			}
			 
           	if(cells[3].getContents().trim().equals("奖励")){
           		po.setFineType(Constant.FINE_TYPE_02);
           	}else if(cells[3].getContents().trim().equals("扣款")){
           		po.setFineType(Constant.FINE_TYPE_01);
           	}else{
           		return "奖惩类型不匹配只能为奖励或者扣款";
           	}
           	po.setLabourType(Long.parseLong(Constant.TAX_RATE_FEE_01.toString()));
           	
            if(CheckUtil.checkFormatNumber1(cells[4].getContents())){
               	po.setLabourSum(Double.parseDouble(cells[4].getContents()));
            }else{
        		return "奖惩劳务金额只能为数字且小数最多两位";
            }
           	po.setPayStatus(Integer.parseInt(Constant.PAY_STATUS_01));
           	po.setDatumType(Long.parseLong(Constant.TAX_RATE_FEE_02.toString()));
            if(CheckUtil.checkFormatNumber1(cells[5].getContents())){
               	po.setDatumSum(Double.parseDouble(cells[5].getContents()));
            }else{
            	return "奖惩材料费只能为数字且小数最多两位";
            }
            if(cells[4].getContents().equals("0")&&cells[5].getContents().equals("0")){
            	return "奖惩材料费和奖惩劳务金额不能同时为0";
            }
            if(cells[6].getContents().trim().equals("")||cells[6].getContents()==null){
            	return "原因是必填项";
            }
           	po.setFineReason(cells[6].getContents().trim());
           	po.setRemark(cells[7].getContents().trim());
           	po.setFineDate(new Date());
    		dao.insert(po);
			return null;
	}
	/*
	 * 将输入字符截取最多30位
	 */
	private String subCell(String orgAmt){
		String newAmt="";
		if(null==orgAmt||"".equals(orgAmt)){
			return newAmt;
		}
		if(orgAmt.length()>30){
			newAmt=orgAmt.substring(0,30);
		}else{
			newAmt=orgAmt;
		}
		return newAmt;
	}

}
