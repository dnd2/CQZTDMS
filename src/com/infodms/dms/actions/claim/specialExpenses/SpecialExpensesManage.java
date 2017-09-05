package com.infodms.dms.actions.claim.specialExpenses;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.SpeciaExpensesBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.specialExpenses.SpecialExpensesManageDao;
import com.infodms.dms.dao.feedbackMng.StandardVipApplyManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmChananelMileagePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrCruisePO;
import com.infodms.dms.po.TtAsWrSpefeeAuditingPO;
import com.infodms.dms.po.TtAsWrSpefeeClaimPO;
import com.infodms.dms.po.TtAsWrSpefeePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SpecialExpensesManage extends BaseAction{
	public Logger logger = Logger.getLogger(SpecialExpensesManage.class);
	private SpecialExpensesManageDao dao = SpecialExpensesManageDao.getInstance();
	public static SpecialExpensesManage getInstance() {
		return new SpecialExpensesManage();
	}
	private String initUrl = "/jsp/claim/specialExpenses/speciaExpensesQuery.jsp";//特殊费用提报查询页面
	private String add01Url = "/jsp/claim/specialExpenses/addSpeciaExpenses01.jsp";//新增市场工单
	private String add02Url = "/jsp/claim/specialExpenses/addSpeciaExpenses02.jsp";//新增特殊外出费用
	private String info01Url = "/jsp/claim/specialExpenses/speciaExpensesInfo01.jsp";//详细页 市场工单
	private String info02Url = "/jsp/claim/specialExpenses/speciaExpensesInfo02.jsp";//详细页 特殊费用
	private String update01Url = "/jsp/claim/specialExpenses/speciaExpensesUpdate01.jsp";//修改页 市场工单
	private String printUrl = "/jsp/claim/specialExpenses/specialPrint.jsp";//打印特殊费用界面
	private String update02Url = "/jsp/claim/specialExpenses/speciaExpensesUpdate02.jsp";//修改页 特殊费用
	private String auditUrl = "/jsp/claim/specialExpenses/speciaExpensesAudit.jsp";//特殊费用审核查询页面
	private String audit01Url = "/jsp/claim/specialExpenses/speciaExpensesAudit01.jsp";//市场工单审核详细
	private String audit02Url = "/jsp/claim/specialExpenses/speciaExpensesAudit02.jsp";//特殊费用审核详细
	private String auditByOne01Url = "/jsp/claim/specialExpenses/speciaExpensesAuditByOne01.jsp";//市场工单审核详细
	private String auditByOne02Url = "/jsp/claim/specialExpenses/speciaExpensesAuditByOne02.jsp";//特殊费用审核详细
	private String areaAuditUrl = "/jsp/claim/specialExpenses/areaAuditSpeciaExpenses.jsp";//大区审核页面
	private String areaAudit02Url = "/jsp/claim/specialExpenses/areaAuditSpeciaInfo.jsp";//大区审核详细页
	private String queryDealerUrl = "/jsp/claim/specialExpenses/queryDealerSpecia.jsp";//经销商查询页面
	private String queryUrl = "/jsp/claim/specialExpenses/queryOemSpecia.jsp";//车厂查询页面
	private String showClaim = "/jsp/claim/specialExpenses/showClaim.jsp";//特殊外出费用详细页
	private String claimInitUrl = "/jsp/claim/specialExpenses/claimList.jsp";//关联索赔单详细页
	private String roInitUrl = "/jsp/claim/specialExpenses/roList.jsp";//关联工单详细页
	private String print01Url = "/jsp/claim/specialExpenses/print01.jsp";//市场工单打印页
	private String print02Url = "/jsp/claim/specialExpenses/print02.jsp";//特殊外出费用打印页
	private String queryActivity = "/jsp/claim/specialExpenses/queryActivity.jsp";//服务活动选择跳转页面
	private String queryRo = "/jsp/claim/specialExpenses/queryRoClaim.jsp" ;//工单索赔单选择跳转页面
	private String cruiseInit = "/jsp/claim/specialExpenses/queryCruise.jsp" ; // 航行路线选择页面初始化//
	private String auditUrlForword = "/jsp/claim/specialExpenses/speciaExpensesAuditForWord.jsp";//特殊费用审核查询页面
	private String auditUrlForword2 = "/jsp/claim/specialExpenses/speciaExpensesAuditForWord2.jsp";//特殊费用审核查询页面
	private String reauditUrl = "/jsp/claim/specialExpenses/speciaExpensesReAudit.jsp" ;//特殊费用重新审核主查询页面
	private String reauditDo_01 = "/jsp/claim/specialExpenses/speciaExpensesReAudit01.jsp" ;//特殊费用重新审核实现页面
	private String reauditDo_02 = "/jsp/claim/specialExpenses/speciaExpensesReAudit02.jsp" ;//特殊外出费用重新审核实现页面
	
	private String areaAuditWCUrl = "/jsp/claim/specialExpenses/areaAuditSpeciaExpensesWC.jsp";//大区审核页面(微车)
	private String auditUrlWCUrl = "/jsp/claim/specialExpenses/speciaExpensesAuditWC.jsp";//结算室审核页面(微车)
	private String auditWCUrl = "/jsp/claim/specialExpenses/speciaExpensesAuditWC.jsp";//特殊费用审核查询页面
	private String audit01WCUrl = "/jsp/claim/specialExpenses/speciaExpensesAudit01WC.jsp";//市场工单审核详细(事业部)
	private String audit02WCUrl = "/jsp/claim/specialExpenses/speciaExpensesAudit02WC.jsp";//市场工单审核详细(结算室)
	private String areaAudit02WCUrl = "/jsp/claim/specialExpenses/areaAuditSpeciaInfoWC.jsp";//大区审核详细页
	private String auditByOne01WCUrl = "/jsp/claim/specialExpenses/speciaExpensesAuditByOne01WC.jsp";//市场工单审核详细(结算室逐条审核)
	private String reauditWCUrl = "/jsp/claim/specialExpenses/speciaExpensesReAuditWC.jsp" ;//特殊费用重新审核主查询页面(微车)
	private String queryWCUrl = "/jsp/claim/specialExpenses/queryOemSpeciaWC.jsp";//车厂查询页面
	private String reauditDo_01WC = "/jsp/claim/specialExpenses/speciaExpensesReAudit01WC.jsp" ;//（微车）市场工单重新审核实现页面
	
	/****************轿车结算室审核特殊费用变更页面 add by kevinyin 20110530**********************/
	private String auditUrlJC = "/jsp/claim/specialExpenses/speciaExpensesAuditJC.jsp";//特殊费用审核查询页面
	private String auditUrlJCDirector = "/jsp/claim/specialExpenses/speciaExpensesAuditJCDirector.jsp";
	private String audit01UrlJC = "/jsp/claim/specialExpenses/speciaExpensesAudit01JC.jsp";//市场工单审核详细
	private String audit02UrlJC = "/jsp/claim/specialExpenses/speciaExpensesAudit02JC.jsp";//特殊费用审核详细
	/****************轿车结算室审核特殊费用变更页面 add by kevinyin 20110530**********************/
	private String MAINTAIN_HIS_URL = "/jsp/claim/specialExpenses/maintaimHistory.jsp";
	private String OPNE_PART = "/jsp/claim/specialExpenses/openpart.jsp";
	private String OPNE_SUPPLIER = "/jsp/claim/specialExpenses/opensupplier.jsp";
	
	private int type = 0;
	
	/*
	 * 特殊费用提报
	 */
	public void dealerSpProposeFor(){
		
		
		TmBusinessAreaPO po = new TmBusinessAreaPO();
		List<PO> list = dao.select(po);
		act.setOutData("Area", list);
		
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	public void querySpeciaExpenses(){
		
		
		try {
			
			String feeNo = request.getParamValue("feeNo");
			String yieldly = request.getParamValue("yieldly");
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String feeType = request.getParamValue("feeType");
			if(!"".equals(beginTime)){
				beginTime = beginTime + " 00:00:00";
			}
			if(!"".equals(endTime)){
				endTime = endTime + " 23:59:59";
			}
			SpeciaExpensesBean bean = new SpeciaExpensesBean();
			bean.setFeeNo(feeNo);
			bean.setYieldly(yieldly);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFeeType(feeType);
			bean.setDealerId(loginUser.getDealerId());
			bean.setCompanyId(loginUser.getOemCompanyId());
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.querySpeciaExpenses(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	public void comitSpecialExpenses()
	{
		
		
		try 
		{
			
			String id = request.getParamValue("id");
			
			TtAsWrSpefeePO asWrSpefeePO = new TtAsWrSpefeePO();
			asWrSpefeePO.setId(Long.parseLong(id));
			TtAsWrSpefeePO asWrSpefeePO01 =(TtAsWrSpefeePO) dao.select(asWrSpefeePO).get(0);
			
			TtAsWrSpefeePO  ttAsWrSpefeePO = new TtAsWrSpefeePO();
			ttAsWrSpefeePO.setUpdateDate(new Date());
			ttAsWrSpefeePO.setUpdateBy(loginUser.getUserId());
			ttAsWrSpefeePO.setMakeDate(new Date());
			
			
			if(asWrSpefeePO01.getDeclareSum1()<=800 )
			{
				ttAsWrSpefeePO.setOStatus(11841006);
			}else if(asWrSpefeePO01.getDeclareSum1() > 800 && asWrSpefeePO01.getDeclareSum1() <= 2000)
			{
				ttAsWrSpefeePO.setOStatus(11841008);
			}else  if(asWrSpefeePO01.getDeclareSum1() > 2000 && asWrSpefeePO01.getDeclareSum1() <= 10000)
			{
				ttAsWrSpefeePO.setOStatus(11841010);
			}else
			{
				ttAsWrSpefeePO.setOStatus(11841012);
			}
			
			ttAsWrSpefeePO.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_02));
			dao.update(asWrSpefeePO, ttAsWrSpefeePO);
			
			TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
			tp.setId(Long.parseLong(SequenceManager.getSequence("")));
			tp.setFeeId(Long.parseLong(id));
			tp.setAuditingDate(new Date());
			tp.setAuditingPerson(loginUser.getName());
			tp.setPresonDept(loginUser.getOrgId());
			tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_02));
			tp.setCreateBy(loginUser.getUserId());
			tp.setCreateDate(new Date());
			dao.insert(tp);
			
			act.setOutData("returnValue", 1);
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
		
	}
	/*
	 * 经销商查询
	 */
	public void queryDealerSpeciaExpenses(){
		
		
		try {
			
			String feeNo = request.getParamValue("feeNo");
			String yieldly = request.getParamValue("yieldly");
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String feeType = request.getParamValue("feeType");
			String CREATE_DATE_S = CommonUtils.checkNull(request.getParamValue("CREATE_DATE_S"));
			String CREATE_DATE_D = CommonUtils.checkNull(request.getParamValue("CREATE_DATE_D"));
			String status = CommonUtils.checkNull(request.getParamValue("STATUS"));
			if(!"".equals(beginTime)){
				beginTime = beginTime + " 00:00:00";
			}
			if(!"".equals(endTime)){
				endTime = endTime + " 23:59:59";
			}
			SpeciaExpensesBean bean = new SpeciaExpensesBean();
			bean.setFeeNo(feeNo);
			bean.setYieldly(yieldly);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFeeType(feeType);
			bean.setDealerId(loginUser.getDealerId());
			bean.setCompanyId(loginUser.getOemCompanyId());
			bean.setStatus(status);
			bean.setCREATE_DATE_D(CREATE_DATE_D);
			bean.setCREATE_DATE_S(CREATE_DATE_S);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryDealerSpeciaExpenses(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void  open_part()
	{
		
		
		try {
			
			
			 String COMM = request.getParamValue("COMM");
			 String PART_CODE = request.getParamValue("PART_CODE");
			 String PART_NAME = request.getParamValue("PART_NAME");
			 if(COMM != null && COMM.length() >0 )
			 {
				 Integer curPage = request.getParamValue("curPage") != null ? Integer
							.parseInt(request.getParamValue("curPage"))
							: 1; // 处理当前页	
				PageResult<Map<String, Object>> ps = dao.get_part(PART_CODE, PART_NAME, curPage, Constant.PAGE_SIZE);
				 act.setOutData("ps",ps);
			 }else
			 {
				 act.setForword(OPNE_PART); 
			 }
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"新增费用");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	public void open_supplier()
	{
		
		try {
			 String COMM = request.getParamValue("COMM");
			 String supplier_code = request.getParamValue("supplier_code");
			 String supplier_name = request.getParamValue("supplier_name");
			 String part_code = request.getParamValue("part_code");
			 if(COMM != null && COMM.length() >0 ){
				PageResult<Map<String, Object>> ps = dao.get_supplier(supplier_code, supplier_name,part_code, getCurrPage(), Constant.PAGE_SIZE);
				 act.setOutData("ps",ps);
			 }else{
				 if(part_code != null)
				 {
					 act.setOutData("part_code", part_code);
				 }
				 act.setForword(OPNE_SUPPLIER); 
			 }
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"新增费用");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
		
	}
	
	/*
	 * 新增费用
	 */
	public void addSpeciaExpenses(){
		
		
		try {
			
			String feeType = request.getParamValue("feeType");
			String dealerId = loginUser.getDealerId();
			Map<String, Object> map = dao.getDealerInfo(dealerId);
			//查询tc_code 8008 区分轿微车
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List list = dao.select(code) ;
			if(list.size()>0)
				code = (TcCodePO)list.get(0);
			act.setOutData("code", code) ;
			act.setOutData("map", map);
			act.setOutData("feeType", feeType);
			
			TmBusinessAreaPO po = new TmBusinessAreaPO();
			List<PO> list1 = dao.select(po);
			act.setOutData("Area", list1);
			if(feeType.equals(Constant.FEE_TYPE_02)){
				act.setForword(add02Url);
			}else{
				act.setForword(add01Url);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"新增费用");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 新增市场工单费用
	 */
	public void addSpeciaExpensesDO(){
		
		
		try {
			
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));//产地
			String declareSum = CommonUtils.checkNull(request.getParamValue("declareSum"));//申报金额
			String linkman = CommonUtils.checkNull(request.getParamValue("linkman"));   //联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));		    //联系电话
			String feeType = CommonUtils.checkNull(request.getParamValue("feeType"));//费用类型
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String claimNo = CommonUtils.checkNull(request.getParamValue("claimNo"));
			String model = CommonUtils.checkNull(request.getParamValue("model"));
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String feeFlag = request.getParamValue("feeFlag");
			
			String balance_fee_type = request.getParamValue("balance_fee_type");
			String part_name = request.getParamValue("part_name");
			String part_code = request.getParamValue("part_code");
			String supplier_code = request.getParamValue("supplier_code");
			String supplier_name = request.getParamValue("supplier_name");
			
			
			
			
			TtAsWrSpefeePO po = new TtAsWrSpefeePO();
			po.setId(Long.parseLong(SequenceManager.getSequence("")));
			po.setBalanceFeeType(Long.parseLong(balance_fee_type));
			po.setPartName(part_name);
			po.setPartCode(part_code);
			po.setSupplierCode(supplier_code);
			po.setSupplierName(supplier_name);
			
			po.setFeeNo(Utility.GetClaimBillNo("",loginUser.getDealerCode(),"TS"));
			po.setDealerId(Long.parseLong(loginUser.getDealerId()));
			
	        TmDealerPO tmDealerPO = new TmDealerPO();
			tmDealerPO.setDealerId(Long.parseLong(loginUser.getDealerId()));
			List<PO> selectLists = dao.select(tmDealerPO);
			if(selectLists != null && selectLists.size() == 1) {
				TmDealerPO deal = (TmDealerPO)selectLists.get(0);
				po.setDealerName(deal.getDealerName());
				po.setDealerShortname(deal.getDealerShortname());
			}
			
			po.setYield(Long.parseLong(yieldly));
			po.setDeclareSum(Double.parseDouble(declareSum));
			po.setDeclareSum1(Double.parseDouble(declareSum));
			po.setLinkman(linkman);
			po.setLinkmanTel(tel);
			po.setVin(vin);
			po.setClaimNo(claimNo);
			po.setCompanyId(Long.parseLong(loginUser.getOemCompanyId()));
			po.setVModel(model);
			po.setFeeType(Integer.parseInt(feeType));
			Date date = new Date();
			if(feeFlag.equals("1")){
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_01));//未提报
			}else{
				if(Double.parseDouble(declareSum)<=800 )
				{
					po.setOStatus(11841006);
				}else if(Double.parseDouble(declareSum) > 800 && Double.parseDouble(declareSum) <= 2000)
				{
					po.setOStatus(11841008);
				}else  if(Double.parseDouble(declareSum) > 2000 && Double.parseDouble(declareSum) <= 10000)
				{
					po.setOStatus(11841010);
				}else
				{
					po.setOStatus(11841012);
				}
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_02));//已提报
				po.setMakeDate(date);
				
				TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
				tp.setId(Long.parseLong(SequenceManager.getSequence("")));
				tp.setFeeId(po.getId());
				tp.setAuditingDate(date);
				tp.setAuditingPerson(loginUser.getName());
				tp.setPresonDept(loginUser.getOrgId());
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_02));
				tp.setCreateBy(loginUser.getUserId());
				tp.setCreateDate(date);
				dao.insert(tp);
			}
			po.setApplyContent(remark);
			po.setCreateBy(loginUser.getUserId());
			po.setCreateDate(date);
			dao.insert(po);//插入TT_AS_WR_SPEFEE 工单费用

			String ywzj = String.valueOf(po.getId());//插入并获得主键id,插入附件时用
			//附近功能：
			String[] fjids = request.getParamValues("fjid");
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);	
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			 e.getStackTrace();
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 特殊外出费用 添加
	 */
	public void addSpeciaExpensesDO02(){
		
		
		try {
			
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));//产地
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结束时间
			String purposeAddress = CommonUtils.checkNull(request.getParamValue("purposeAddress"));//目的地
			String personNum = CommonUtils.checkNull(request.getParamValue("personNum"));//人员数量
			String personName = CommonUtils.checkNull(request.getParamValue("personName"));//人员姓名
			String SINGLE_MILEAGE = CommonUtils.checkNull(request.getParamValue("SINGLE_MILEAGE"));//单程里程
			String PASS_FEE = request.getParamValue("PASS_FEE")==null?"0":request.getParamValue("PASS_FEE");//过路过桥
			String TRAFFIC_FEE = request.getParamValue("TRAFFIC_FEE")==null?"0":request.getParamValue("TRAFFIC_FEE");//交通补助
			String QUARTER_FEE = request.getParamValue("QUARTER_FEE")==null?"0":request.getParamValue("QUARTER_FEE");//住宿费
			String EAT_FEE = request.getParamValue("EAT_FEE")==null?"0":request.getParamValue("EAT_FEE");//餐补
			String PERSON_SUBSIDE = request.getParamValue("PERSON_SUBSIDE")==null?"0":request.getParamValue("PERSON_SUBSIDE");//人员补助
			String declareSum = request.getParamValue("totalCount")==null?"0":request.getParamValue("totalCount");//总费用
			String FEE_CHANNEL = CommonUtils.checkNull(request.getParamValue("FEE_CHANNEL"));//费用渠道
			String feeType = Constant.FEE_TYPE_02;//费用类型
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));//备注
			String feeFlag = request.getParamValue("feeFlag");
			String outDays = request.getParamValue("out_days");
			String freeCharge = request.getParamValue("vin_num");//三包内用户数
			//如果是巡航服务则添加另两个数据
			if(Constant.FEE_CHANNEL_03.equals(FEE_CHANNEL)){
				purposeAddress = request.getParamValue("address");
				//SINGLE_MILEAGE = request.getParamValue("mileage2");
			}
			String balance_fee_type = request.getParamValue("balance_fee_type");
			String part_name = request.getParamValue("part_name");
			String part_code = request.getParamValue("part_code");
			String supplier_code = request.getParamValue("supplier_code");
			String supplier_name = request.getParamValue("supplier_name");
			
			TtAsWrSpefeePO po = new TtAsWrSpefeePO();
			po.setId(Long.parseLong(SequenceManager.getSequence("")));
			po.setBalanceFeeType(Long.parseLong(balance_fee_type));
			po.setSupplierName(supplier_name);
			po.setPartName(part_name);
			po.setPartCode(part_code);
			po.setSupplierCode(supplier_code);
			po.setFeeNo(Utility.GetClaimBillNo("",loginUser.getDealerCode(),"TS"));
			po.setDealerId(Long.parseLong(loginUser.getDealerId()));
			po.setYield(Long.parseLong(yieldly));
			po.setDeclareSum(Double.parseDouble(declareSum));
			po.setDeclareSum1(Double.parseDouble(declareSum));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			po.setStartDate(formatter.parse(beginTime));
			po.setEndDate(formatter.parse(endTime));
			po.setPurposeAddress(purposeAddress);
			po.setPersonName(personName);
			po.setPersonNum(Long.parseLong(personNum));
			po.setSingleMileage(Double.parseDouble(SINGLE_MILEAGE));
			po.setPassFee(StringUtil.isNull(PASS_FEE)?0:Double.parseDouble(PASS_FEE));
			po.setPassFee1(StringUtil.isNull(PASS_FEE)?0:Double.parseDouble(PASS_FEE));
			po.setQuarterFee(StringUtil.isNull(QUARTER_FEE)?0:Double.parseDouble(QUARTER_FEE));
			po.setQuarterFee1(StringUtil.isNull(QUARTER_FEE)?0:Double.parseDouble(QUARTER_FEE));
			po.setTrafficFee(StringUtil.isNull(TRAFFIC_FEE)?0:Double.parseDouble(TRAFFIC_FEE));
			po.setTrafficFee1(StringUtil.isNull(TRAFFIC_FEE)?0:Double.parseDouble(TRAFFIC_FEE));
			po.setEatFee(StringUtil.isNull(EAT_FEE)?0:Double.parseDouble(EAT_FEE));
			po.setEatFee1(StringUtil.isNull(EAT_FEE)?0:Double.parseDouble(EAT_FEE));
			po.setPersonSubside(StringUtil.isNull(PERSON_SUBSIDE)?0:Double.parseDouble(PERSON_SUBSIDE));
			po.setPersonSubside1(StringUtil.isNull(PERSON_SUBSIDE)?0:Double.parseDouble(PERSON_SUBSIDE));
			po.setFeeChannel(Integer.parseInt(FEE_CHANNEL));
			po.setFeeType(Integer.parseInt(feeType));
			po.setCompanyId(Long.parseLong(loginUser.getOemCompanyId()));
			po.setOutDays(Integer.parseInt(outDays));
			//上门服务的时候，记录一下此次服务的三包内用户数
			if(Constant.FEE_CHANNEL_02.equals(FEE_CHANNEL)){
				if(StringUtil.notNull(freeCharge))
					po.setFreeCharge(Integer.parseInt(freeCharge));
			}
			Date date = new Date();
			if(feeFlag.equals("1")){
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_01));//未提报
			}else{
				if(Double.parseDouble(declareSum)<=800 )
				{
					po.setOStatus(11841006);
				}else if(Double.parseDouble(declareSum) > 800 && Double.parseDouble(declareSum) <= 2000)
				{
					po.setOStatus(11841008);
				}else  if(Double.parseDouble(declareSum) > 2000 && Double.parseDouble(declareSum) <= 10000)
				{
					po.setOStatus(11841010);
				}else
				{
					po.setOStatus(11841012);
				}
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_02));//已提报
				po.setMakeDate(date);
				
				TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
				tp.setId(Long.parseLong(SequenceManager.getSequence("")));
				tp.setFeeId(po.getId());
				tp.setAuditingDate(date);
				tp.setAuditingPerson(loginUser.getName());
				tp.setPresonDept(loginUser.getOrgId());
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_02));
				tp.setCreateBy(loginUser.getUserId());
				tp.setCreateDate(date);
				dao.insert(tp);
			}
			po.setApplyContent(remark);
			po.setCreateBy(loginUser.getUserId());
			po.setCreateDate(date);
			dao.insert(po);//插入TT_AS_WR_SPEFEE 工单费用

			String ywzj = String.valueOf(po.getId());//插入并获得主键id,插入附件时用
			//附近功能：
			String[] fjids = request.getParamValues("fjid");
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);	
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	public void speciaExpensesInfo(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String feeType = request.getParamValue("feeType");
			Map<String, Object> map = dao.getSpeciaExpensesInfo(id);
		    
			Long y = Long.parseLong(""+map.get("YIELD"));
			TmBusinessAreaPO po = new TmBusinessAreaPO();
			po.setAreaId(y);
			List<PO> list1 = dao.select(po);
			po =(TmBusinessAreaPO)list1.get(0);
			String areaName = po.getAreaName();
			List<Map<String, Object>> list = dao.getSpeciaExpensesAudit(id);
			List<Map<String, Object>> claim = dao.getFeeRoList(id);
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
			//查询tc_code 8008 区分轿微车
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List list2 = dao.select(code) ;
			if(list2.size()>0)
				code = (TcCodePO)list2.get(0);
			act.setOutData("code", code) ;
			act.setOutData("areaName", areaName);
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("claim", claim);
			act.setOutData("fileList", fileList);
			if(feeType.equals(Constant.FEE_TYPE_02)){
				act.setForword(info02Url);
			}else{
				act.setForword(info01Url);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊外出费用详细");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 修改工单
	 */
	public void updateSpecialExpenses(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String feeType = request.getParamValue("feeType");
			Map<String, Object> map = dao.getSpeciaExpensesInfo(id);
			String yield = ((BigDecimal)map.get("YIELD")).toString();
			
			
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			TmBusinessAreaPO po = new TmBusinessAreaPO();
			List<PO> list1 = dao.select(po);
			for(int i=0;i<list1.size();i++)
			{
				po =  (TmBusinessAreaPO)list1.get(i);
				System.out.println(po.getAreaId());
				if(yield.equals(""+po.getAreaId()))
				{
					areaPO = (TmBusinessAreaPO)list1.get(0);
					list1.set(0, po);
					list1.set(i, areaPO);
					break;
				}
				
			}
			act.setOutData("Area", list1);
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
			//查询tc_code 8008 区分轿微车
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List list = dao.select(code) ;
			if(list.size()>0)
				code = (TcCodePO)list.get(0);
			act.setOutData("code", code) ;
			act.setOutData("map", map);
			act.setOutData("fileList", fileList);
			if(feeType.equals(Constant.FEE_TYPE_02)){
				List<Map<String, Object>> claim = dao.getFeeRoList(id);
				act.setOutData("claim", claim);
				act.setOutData("nums",claim.size());
				act.setForword(update02Url);
				
			}else{
				act.setForword(update01Url);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"修改");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 修改工单
	 */
	public void specialPrint(){
		
		
		try {
			
			String id = request.getParamValue("id");
			Map<String, Object> map = dao.getSpeciaExpensesPrint(id);
			act.setOutData("map", map);
			act.setForword(printUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"修改");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 修改工单信息
	 */
	public void updateSpeciaExpensesDO(){
		
		try {
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));//产地
			String declareSum = CommonUtils.checkNull(request.getParamValue("declareSum"));//申报金额
			String linkman = CommonUtils.checkNull(request.getParamValue("linkman"));   //联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));		    //联系电话
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String claimNo = CommonUtils.checkNull(request.getParamValue("claimNo"));
			String vModel = CommonUtils.checkNull(request.getParamValue("model"));
			String feeFlag = request.getParamValue("feeFlag");
			String id = request.getParamValue("id");
			
			String part_name = CommonUtils.checkNull(request.getParamValue("part_name"));
			String part_code = CommonUtils.checkNull(request.getParamValue("part_code"));
			String supplier_code = CommonUtils.checkNull(request.getParamValue("supplier_code"));
			String supplier_name = CommonUtils.checkNull(request.getParamValue("supplier_name"));
			String balance_fee_type = CommonUtils.checkNull(request.getParamValue("balance_fee_type"));
			
			
			
			TtAsWrSpefeePO po = new TtAsWrSpefeePO();
			TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
			po.setYield(Long.parseLong(yieldly));
			po.setPartCode(part_code);
			po.setPartName(part_name);
			po.setSupplierCode(supplier_code);
			po.setBalanceFeeType(Long.parseLong(balance_fee_type));
			po.setDeclareSum(Double.parseDouble(declareSum));
			po.setDeclareSum1(Double.parseDouble(declareSum));
			po.setLinkman(linkman);
			po.setLinkmanTel(tel);
			po.setSupplierName(supplier_name);
			/***************特殊费用活动项目********************/
			po.setVin(vin);
			po.setClaimNo(claimNo);
			po.setVModel(vModel);
			Date date = new Date();
			if(feeFlag.equals("1")){
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_01));//未提报
				if(Double.parseDouble(declareSum)<=800 )
				{
					po.setOStatus(11841006);
				}else if(Double.parseDouble(declareSum) > 800 && Double.parseDouble(declareSum) <= 2000)
				{
					po.setOStatus(11841008);
				}else  if(Double.parseDouble(declareSum) > 2000 && Double.parseDouble(declareSum) <= 10000)
				{
					po.setOStatus(11841010);
				}else
				{
					po.setOStatus(11841012);
				}
			}else{
				if(Double.parseDouble(declareSum)<=500 )
				{
					po.setOStatus(11841006);
				}else if(Double.parseDouble(declareSum) > 500 && Double.parseDouble(declareSum) <= 3000)
				{
					po.setOStatus(11841008);
				}else
				{
					po.setOStatus(11841010);
				}
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_02));//已提报
				po.setMakeDate(date);
				TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
				tp.setId(Long.parseLong(SequenceManager.getSequence("")));
				tp.setFeeId(Long.parseLong(id));
				tp.setAuditingDate(date);
				tp.setAuditingPerson(loginUser.getName());
				tp.setPresonDept(loginUser.getOrgId());
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_02));
				tp.setCreateBy(loginUser.getUserId());
				tp.setCreateDate(date);
				dao.insert(tp);
			}
			po.setApplyContent(remark);
			po.setUpdateBy(loginUser.getUserId());
			po.setUpdateDate(date);
			pp.setId(Long.parseLong(id));
			dao.update(pp, po);//修改TT_AS_WR_SPEFEE 工单费用

			//附近功能：
			String ywzj = id;
			String[] fjids = request.getParamValues("fjid");
			FileUploadManager.delAllFilesUploadByBusiness(ywzj,fjids);//删除附件
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);	//新添加附件
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 修改特殊费用
	 */
	public void updateSpeciaExpensesDO02(){
		
		
		try {
			
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));//产地
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结束时间
			String purposeAddress = CommonUtils.checkNull(request.getParamValue("purposeAddress"));//目的地
			String personNum = CommonUtils.checkNull(request.getParamValue("personNum"));//人员数量
			String personName = CommonUtils.checkNull(request.getParamValue("personName"));//人员姓名
			String SINGLE_MILEAGE = CommonUtils.checkNull(request.getParamValue("SINGLE_MILEAGE"));//单程里程
			String PASS_FEE = CommonUtils.checkNull(request.getParamValue("PASS_FEE"));//过路过桥
			String TRAFFIC_FEE = CommonUtils.checkNull(request.getParamValue("TRAFFIC_FEE"));//交通补助
			String QUARTER_FEE = CommonUtils.checkNull(request.getParamValue("QUARTER_FEE"));//住宿费
			String EAT_FEE = CommonUtils.checkNull(request.getParamValue("EAT_FEE"));//餐补
			String PERSON_SUBSIDE = CommonUtils.checkNull(request.getParamValue("PERSON_SUBSIDE"));//人员补助
			String declareSum = CommonUtils.checkNull(request.getParamValue("totalCount"));//总费用
			String FEE_CHANNEL = CommonUtils.checkNull(request.getParamValue("FEE_CHANNEL"));//费用渠道
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));//备注
			String feeFlag = request.getParamValue("feeFlag");
			String id = request.getParamValue("id");
			String freeCharge = request.getParamValue("vin_num");
			//如果是巡航服务则添加另两个数据
			if(Constant.FEE_CHANNEL_03.equals(FEE_CHANNEL)){
				purposeAddress = request.getParamValue("address");
			}
			String part_name = CommonUtils.checkNull(request.getParamValue("part_name"));
			String part_code = CommonUtils.checkNull(request.getParamValue("part_code"));
			String supplier_code = CommonUtils.checkNull(request.getParamValue("supplier_code"));
			String balance_fee_type = CommonUtils.checkNull(request.getParamValue("balance_fee_type"));
			TtAsWrSpefeePO po = new TtAsWrSpefeePO();
			TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
			po.setPartCode(part_code);
			po.setPartName(part_name);
			po.setSupplierCode(supplier_code);
			po.setBalanceFeeType(Long.parseLong(balance_fee_type));
			po.setYield(Long.parseLong(yieldly));
			po.setDeclareSum(Double.parseDouble(declareSum));
			po.setDeclareSum1(Double.parseDouble(declareSum));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			po.setStartDate(formatter.parse(beginTime));
			po.setEndDate(formatter.parse(endTime));
			po.setPurposeAddress(purposeAddress);
			po.setPersonName(personName);
			po.setPersonNum(Long.parseLong(personNum));
			po.setSingleMileage(Double.parseDouble(SINGLE_MILEAGE));
			po.setPassFee(StringUtil.isNull(PASS_FEE)?0:Double.parseDouble(PASS_FEE));
			po.setQuarterFee(StringUtil.isNull(QUARTER_FEE)?0:Double.parseDouble(QUARTER_FEE));
			po.setTrafficFee(StringUtil.isNull(TRAFFIC_FEE)?0:Double.parseDouble(TRAFFIC_FEE));
			po.setEatFee(StringUtil.isNull(EAT_FEE)?0:Double.parseDouble(EAT_FEE));
			po.setPersonSubside(StringUtil.isNull(PERSON_SUBSIDE)?0:Double.parseDouble(PERSON_SUBSIDE));
			po.setPassFee1(StringUtil.isNull(PASS_FEE)?0:Double.parseDouble(PASS_FEE));
			po.setQuarterFee1(StringUtil.isNull(QUARTER_FEE)?0:Double.parseDouble(QUARTER_FEE));
			po.setTrafficFee1(StringUtil.isNull(TRAFFIC_FEE)?0:Double.parseDouble(TRAFFIC_FEE));
			po.setEatFee1(StringUtil.isNull(EAT_FEE)?0:Double.parseDouble(EAT_FEE));
			po.setPersonSubside1(StringUtil.isNull(PERSON_SUBSIDE)?0:Double.parseDouble(PERSON_SUBSIDE));
			po.setFeeChannel(Integer.parseInt(FEE_CHANNEL));
			//上门服务的时候，记录一下此次服务的三包内用户数
			if(Constant.FEE_CHANNEL_02.equals(FEE_CHANNEL)){
				if(StringUtil.notNull(freeCharge))
					po.setFreeCharge(Integer.parseInt(freeCharge));
			}
			Date date = new Date();
			if(feeFlag.equals("1")){
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_01));//未提报
			}else{
				if(Double.parseDouble(declareSum)<=500 )
				{
					po.setOStatus(11841006);
				}else if(Double.parseDouble(declareSum) > 500 && Double.parseDouble(declareSum) <= 3000)
				{
					po.setOStatus(11841008);
				}else
				{
					po.setOStatus(11841010);
				}
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_02));//已提报
				po.setMakeDate(date);
				TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
				tp.setId(Long.parseLong(SequenceManager.getSequence("")));
				tp.setFeeId(Long.parseLong(id));
				tp.setAuditingDate(date);
				tp.setAuditingPerson(loginUser.getName());
				tp.setPresonDept(loginUser.getOrgId());
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_02));
				tp.setCreateBy(loginUser.getUserId());
				tp.setCreateDate(date);
				dao.insert(tp);
			}
			po.setApplyContent(remark);
			po.setUpdateBy(loginUser.getUserId());
			po.setUpdateDate(date);
			pp.setId(Long.parseLong(id));
			dao.update(pp, po);//修改TT_AS_WR_SPEFEE 工单费用

			//附近功能：
			String ywzj = id;
			String[] fjids = request.getParamValues("fjid");
			FileUploadManager.delAllFilesUploadByBusiness(ywzj,fjids);//删除附件
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);	//新添加附件
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 结算室审核
	 */
	public void specialExaminesFor(){
		
		
		try {
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			
			act.setOutData("yieldly", yieldly);
			act.setForword(auditUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 审核查询页面
	 */
	public void auditQuerySpeciaExpenses(){
		
		
		try {
			
			String feeNo = request.getParamValue("feeNo");
			String yieldly = request.getParamValue("yieldly");
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String feeType = request.getParamValue("feeType");
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			if(!dealerCode.equals("")){//截串加单引号
				String[] supp = dealerCode.split(",");
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}
			if(!"".equals(beginTime)){
				beginTime = beginTime + " 00:00:00";
			}
			if(!"".equals(endTime)){
				endTime = endTime + " 23:59:59";
			}
			//取得该用户拥有的产地权限
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			SpeciaExpensesBean bean = new SpeciaExpensesBean();
			bean.setFeeNo(feeNo);
			bean.setYieldly(yieldly);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFeeType(feeType);
			bean.setDealerCode(dealerCode);
			bean.setCompanyId(String.valueOf(loginUser.getCompanyId()));
			bean.setYieldlys(yieldlys);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.auditQuerySpeciaExpenses(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/****
	 * 
	 * 结算单审核页面跳转到特殊费用审核页面
	 * 
	 * 
	 */
	
	public void forwordAuditing(){
		
		
		try {
			
			String feeNo = request.getParamValue("feeNo");
			String yieldly = request.getParamValue("yieldly");
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String feeType = request.getParamValue("feeType");
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String balanceId = request.getParamValue("orderId");
			if(!dealerCode.equals("")){//截串加单引号
				String[] supp = dealerCode.split(",");
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}
			if(!"".equals(beginTime)){
				beginTime = beginTime + " 00:00:00";
			}
			if(!"".equals(endTime)){
				endTime = endTime + " 23:59:59";
			}
			//取得该用户拥有的产地权限
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			SpeciaExpensesBean bean = new SpeciaExpensesBean();
			bean.setFeeNo(feeNo);
			bean.setYieldly(yieldly);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFeeType(feeType);
			bean.setDealerCode(dealerCode);
			bean.setCompanyId(String.valueOf(loginUser.getCompanyId()));
			bean.setYieldlys(yieldlys);
			bean.setBalanceId(balanceId);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.auditQuerySpeciaExpenses3(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("orderId", balanceId);
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void forwordAuditingMain(){
		
		
		try {
			
			String balanceId = request.getParamValue("orderId");
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&:"+balanceId);

			//分页方法 end
			act.setOutData("orderId", balanceId);    //向前台传的list 名称是固定的不可改必须用 ps
			act.setForword(auditUrlForword2);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 车厂端查询页面
	 */
	public void queryOemSpeciaExpenses(){
		
		
		try {
			
			String feeNo = request.getParamValue("feeNo");
			String yieldly = request.getParamValue("yieldly");
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String CREATE_DATE_S = CommonUtils.checkNull(request.getParamValue("CREATE_DATE_S"));
			String CREATE_DATE_D = CommonUtils.checkNull(request.getParamValue("CREATE_DATE_D"));
			String feeType = request.getParamValue("feeType");
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String status = CommonUtils.checkNull(request.getParamValue("STATUS"));
			//取得该用户拥有的产地权限
			if(!dealerCode.equals("")){//截串加单引号
				String[] supp = dealerCode.split(",");
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}
			if(!"".equals(beginTime)){
				beginTime = beginTime + " 00:00:00";
			}
			if(!"".equals(endTime)){
				endTime = endTime + " 23:59:59";
			}
			SpeciaExpensesBean bean = new SpeciaExpensesBean();
			bean.setFeeNo(feeNo);
			bean.setCREATE_DATE_S(CREATE_DATE_S);
			bean.setCREATE_DATE_D(CREATE_DATE_D);
			bean.setYieldly(yieldly);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFeeType(feeType);
			bean.setDealerCode(dealerCode);
			bean.setStatus(status);
			PageResult<Map<String, Object>> ps = dao.queryOmeSpeciaExpenses(bean,getCurrPage(),Constant.PAGE_SIZE);
			act.setOutData("ps", ps);     
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 大区审核查询页面
	 */
	public void areaAuditQuerySpeciaExpenses(){
		
		
		try {
			
			String feeNo = request.getParamValue("feeNo");
			String yieldly = request.getParamValue("YIELDLY");
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String feeType = request.getParamValue("feeType");
			String orgId = String.valueOf(loginUser.getOrgId());
			//取得该用户拥有的产地权限
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			if(!dealerCode.equals("")){//截串加单引号
				String[] supp = dealerCode.split(",");
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}
			if(!"".equals(beginTime)){
				beginTime = beginTime + " 00:00:00";
			}
			if(!"".equals(endTime)){
				endTime = endTime + " 23:59:59";
			}
			SpeciaExpensesBean bean = new SpeciaExpensesBean();
			bean.setFeeNo(feeNo);
			bean.setYieldly(yieldly);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFeeType(feeType);
			bean.setDealerCode(dealerCode);
			bean.setOrgId(orgId);
			bean.setCompanyId(String.valueOf(loginUser.getCompanyId()));
			bean.setYieldlys(yieldlys);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.areaAuditQuerySpeciaExpenses(loginUser.getUserId(),bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 审核详细页
	 */
	public void auditSpecialExpensesInfo(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String feeType = request.getParamValue("feeType");
			Map<String, Object> map = dao.getSpeciaExpensesInfo(id);
			List<Map<String, Object>> list = dao.getSpeciaExpensesAudit(id);
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
			List<Map<String, Object>> claim = dao.getFeeRoList(id);
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("fileList", fileList);
			act.setOutData("claim", claim);
			if(feeType.equals(Constant.FEE_TYPE_01)){
				act.setForword(audit01Url);
			}else{
				act.setForword(audit02Url);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 逐条审核详细页
	 */
	public void auditSpecialExpensesInfoByOne(){
		
		
		try {
			
			
			String feeNo = request.getParamValue("feeNo");
			String yieldly = request.getParamValue("yieldly");
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String feeType = request.getParamValue("feeType");
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			//审核条数记录
			String count = request.getParamValue("count")==null?"0":request.getParamValue("count");
			if(!dealerCode.equals("")){//截串加单引号
				String[] supp = dealerCode.split(",");
				act.setOutData("dealerCode", dealerCode);
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}
			//取得该用户拥有的产地权限
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			SpeciaExpensesBean bean = new SpeciaExpensesBean();
			bean.setFeeNo(feeNo);
			bean.setYieldly(yieldly);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFeeType(feeType);
			bean.setDealerCode(dealerCode);
			bean.setCompanyId(String.valueOf(loginUser.getCompanyId()));
			bean.setYieldlys(yieldlys);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			List<Map<String, Object>> ps = dao.auditQuerySpeciaExpensesByOne(bean);
			//分页方法 end
			if(ps!=null){
				if(ps.size()>Integer.valueOf(count)){
					Map<String, Object> specialMap = ps.get(Integer.valueOf(count));
					     //向前台传的list 名称是固定的不可改必须用 ps
					
					//查询第一条特殊费用单信息
					String id = specialMap.get("ID").toString();
		//			String feeTypeGet = specialMap.get("FEE_TYPE").toString();
					Map<String, Object> map = dao.getSpeciaExpensesInfo(id);
					List<Map<String, Object>> list = dao.getSpeciaExpensesAudit(id);
					StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
					List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
					List<Map<String, Object>> claim = dao.getFeeRoList(id);
					act.setOutData("firstSpecial", specialMap);  //与单条审核区别
					act.setOutData("map", map);
					act.setOutData("list", list);
					act.setOutData("fileList", fileList);
					act.setOutData("claim", claim);
					
					//向下一条传查询条件
					
					act.setOutData("beginTime", beginTime);
					act.setOutData("endTime", endTime);
					act.setOutData("feeNo", feeNo);
					act.setOutData("yieldly", yieldly);
					act.setOutData("feeType", feeType);
					act.setOutData("count", count);
					if(specialMap.get("FEE_TYPE").toString().equals(Constant.FEE_TYPE_01)){
						act.setForword(auditByOne01Url);
					}else{
						act.setForword(auditByOne02Url);
					}
				}else{
					act.setForword(auditUrl);
				}
			}else{
				act.setForword(auditUrl);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 大区审核详细页
	 */
	public void areaAuditSpecialExpenses(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String feeType = request.getParamValue("feeType");
			Map<String, Object> map = dao.getSpeciaExpensesInfo(id);
			List<Map<String, Object>> list = dao.getSpeciaExpensesAudit(id);
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
			List<Map<String, Object>> claim = dao.getFeeRoList(id);
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("fileList", fileList);
			act.setOutData("claim", claim);
			if(feeType.equals(Constant.FEE_TYPE_01)){
				act.setForword(audit01Url);
			}else{
				act.setForword(areaAudit02Url);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"大区审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 审核市场工单费用
	 */
	public void auditSpeciaExpenses01(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String auditFlag = request.getParamValue("auditFlag");
			String feeType = CommonUtils.checkNull(request.getParamValue("feeType"));
			String auditMoney = request.getParamValue("audit_money");//审核同意金额
			Date date = new Date();
			TtAsWrSpefeePO po = new TtAsWrSpefeePO();
			TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
			if(auditFlag.equals("1")){
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//已结算
			}else{
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//驳回
			}
			if(feeType.equals(Constant.FEE_TYPE_02)){
				String PASS_FEE = CommonUtils.checkNull(request.getParamValue("PASS_FEE"));//过路过桥
				String TRAFFIC_FEE = CommonUtils.checkNull(request.getParamValue("TRAFFIC_FEE"));//交通补助
				String QUARTER_FEE = CommonUtils.checkNull(request.getParamValue("QUARTER_FEE"));//住宿费
				String EAT_FEE = CommonUtils.checkNull(request.getParamValue("EAT_FEE"));//餐补
				String PERSON_SUBSIDE = CommonUtils.checkNull(request.getParamValue("PERSON_SUBSIDE"));//人员补助
				String declareSum = CommonUtils.checkNull(request.getParamValue("totalCount"));//总费用 
				po.setPassFee(Double.parseDouble(PASS_FEE));
				po.setTrafficFee(Double.parseDouble(TRAFFIC_FEE));
				po.setQuarterFee(Double.parseDouble(QUARTER_FEE));
				po.setEatFee(Double.parseDouble(EAT_FEE));
				po.setPersonSubside(Double.parseDouble(PERSON_SUBSIDE));
				po.setDeclareSum(Double.parseDouble(declareSum));
			}
			//审核同意金额
			if(StringUtil.notNull(auditMoney)){
				po.setDeclareSum(Double.parseDouble(auditMoney));
			}
			po.setUpdateBy(loginUser.getUserId());
			po.setUpdateDate(date);
			po.setAuditingOpinion(remark);//审核意见
			pp.setId(Long.parseLong(id));
			dao.update(pp, po);//修改TT_AS_WR_SPEFEE 工单费用

			TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
			tp.setId(Long.parseLong(SequenceManager.getSequence("")));
			tp.setFeeId(pp.getId());
			tp.setAuditingDate(date);
			tp.setAuditingPerson(loginUser.getName());
			tp.setPresonDept(loginUser.getOrgId());
			if(auditFlag.equals("1")){//通过
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));
			}else{//驳回
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));
			}
			tp.setCreateBy(loginUser.getUserId());
			tp.setCreateDate(date);
			tp.setAuditingOpinion(remark);
			dao.insert(tp);
			
			/*String balanceId = request.getParamValue("balanceId");
			Integer count = dao.getCount(balanceId);
			TtAsWrClaimBalancePO ac = new TtAsWrClaimBalancePO();
			TtAsWrClaimBalancePO bc = new TtAsWrClaimBalancePO();
			ac.setId(Long.parseLong(balanceId));
			bc.setSpecFeeAuthCount(Long.parseLong(String.valueOf(count)));
			dao.update(ac, bc);*/
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 重新审核
	 */
	public void reauditSpeciaExpenses(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String auditFlag = request.getParamValue("auditFlag");
			String feeType = CommonUtils.checkNull(request.getParamValue("feeType"));
			String auditMoney = request.getParamValue("audit_money");//审核同意金额
			Date date = new Date();
			TtAsWrSpefeePO po = new TtAsWrSpefeePO();
			TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
			if(auditFlag.equals("1")){
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//已结算
			}else{
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//驳回
			}
			if(feeType.equals(Constant.FEE_TYPE_02)){
				String PASS_FEE = CommonUtils.checkNull(request.getParamValue("PASS_FEE"));//过路过桥
				String TRAFFIC_FEE = CommonUtils.checkNull(request.getParamValue("TRAFFIC_FEE"));//交通补助
				String QUARTER_FEE = CommonUtils.checkNull(request.getParamValue("QUARTER_FEE"));//住宿费
				String EAT_FEE = CommonUtils.checkNull(request.getParamValue("EAT_FEE"));//餐补
				String PERSON_SUBSIDE = CommonUtils.checkNull(request.getParamValue("PERSON_SUBSIDE"));//人员补助
				String declareSum = CommonUtils.checkNull(request.getParamValue("totalCount"));//总费用 
				po.setPassFee(Double.parseDouble(PASS_FEE));
				po.setTrafficFee(Double.parseDouble(TRAFFIC_FEE));
				po.setQuarterFee(Double.parseDouble(QUARTER_FEE));
				po.setEatFee(Double.parseDouble(EAT_FEE));
				po.setPersonSubside(Double.parseDouble(PERSON_SUBSIDE));
				po.setDeclareSum(Double.parseDouble(declareSum));
			}
			//审核同意金额
			if(StringUtil.notNull(auditMoney)){
				po.setDeclareSum(Double.parseDouble(auditMoney));
			}
			po.setUpdateBy(loginUser.getUserId());
			po.setUpdateDate(date);
			po.setAuditingOpinion(remark);//审核意见
			pp.setId(Long.parseLong(id));
			dao.update(pp, po);//修改TT_AS_WR_SPEFEE 工单费用

			TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
			tp.setId(Long.parseLong(SequenceManager.getSequence("")));
			tp.setFeeId(pp.getId());
			tp.setAuditingDate(date);
			tp.setAuditingPerson(loginUser.getName());
			tp.setPresonDept(loginUser.getOrgId());
			if(auditFlag.equals("1")){//通过
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));
			}else{//驳回
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));
			}
			tp.setCreateBy(loginUser.getUserId());
			tp.setCreateDate(date);
			tp.setAuditingOpinion(remark);
			dao.insert(tp);
			
			String balanceId = request.getParamValue("balanceId");
			Integer count = dao.getCount(balanceId);
			TtAsWrClaimBalancePO ac = new TtAsWrClaimBalancePO();
			TtAsWrClaimBalancePO bc = new TtAsWrClaimBalancePO();
			ac.setId(Long.parseLong(balanceId));
			bc.setSpecFeeAuthCount(Long.parseLong(String.valueOf(count)));
			dao.update(ac, bc);
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 审核市场工单费用批量审核
	 */
	public void auditSpeciaExpensesBatch01(){
		
		
		try {
			
			String specialId[] = request.getParamValues("specialId");
//			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
//			String auditFlag = request.getParamValue("auditFlag");
			String[] feeType = request.getParamValues("feeTypeT");
			
			String[] mySpecialId = request.getParamValues("mySpecialId");
			String[] passFee = request.getParamValues("PASS_FEE");
			String[] trafficFee = request.getParamValues("TRAFFIC_FEE");
			String[] quarterFee = request.getParamValues("QUARTER_FEE");
			String[] eatFee = request.getParamValues("EAT_FEE");
			String[] personSubside = request.getParamValues("PERSON_SUBSIDE");
			String[] claimBalanceId = request.getParamValues("CLAIMBALANCE_ID");
			
			String selFeeType = "";
			String selPassFee = "";
			String selTrafficFee = "";
			String selQuarterFee = "";
			String selEatFee = "";
			String selPersonSubside = "";
			String selClaimBalanceId = "";
			for(int i = 0; i < specialId.length; i++){
				for(int j = 0 ; j < mySpecialId.length; j++){
					if(mySpecialId[j].equals(specialId[i])){
						selFeeType = feeType[j];
						selPassFee = passFee[j];
						selTrafficFee = trafficFee[j];
						selQuarterFee = quarterFee[j];
						selEatFee = eatFee[j];
						selPersonSubside = personSubside[j];
						selClaimBalanceId = claimBalanceId[j];
						
						Date date = new Date();
						TtAsWrSpefeePO po = new TtAsWrSpefeePO();
						TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
//						if(auditFlag.equals("1")){
							po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//已结算
//						}else{
//							po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//驳回
//						}
						if(selFeeType.equals(Constant.FEE_TYPE_02)){
							Double PASS_FEE = Double.valueOf(selPassFee);//过路过桥
							Double TRAFFIC_FEE = Double.valueOf(selTrafficFee);//交通补助
							Double QUARTER_FEE = Double.valueOf(selQuarterFee);//住宿费
							Double EAT_FEE = Double.valueOf(selEatFee);//餐补
							Double PERSON_SUBSIDE = Double.valueOf(selPersonSubside);//人员补助
							Double declareSum = PASS_FEE+TRAFFIC_FEE+QUARTER_FEE+EAT_FEE+PERSON_SUBSIDE;//总费用 
							po.setPassFee(PASS_FEE);
							po.setTrafficFee(TRAFFIC_FEE);
							po.setQuarterFee(QUARTER_FEE);
							po.setEatFee(EAT_FEE);
							po.setPersonSubside(PERSON_SUBSIDE);
							po.setDeclareSum(declareSum);
						}
						
						po.setUpdateBy(loginUser.getUserId());
						po.setUpdateDate(date);
						po.setAuditingOpinion("");//审核意见
						pp.setId(Long.parseLong(specialId[i]));
						dao.update(pp, po);//修改TT_AS_WR_SPEFEE 工单费用

						TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
						tp.setId(Long.parseLong(SequenceManager.getSequence("")));
						tp.setFeeId(pp.getId());
						tp.setAuditingDate(date);
						tp.setAuditingPerson(loginUser.getName());
						tp.setPresonDept(loginUser.getOrgId());
//						if(auditFlag.equals("1")){//通过
							tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));
//						}else{//驳回
//							tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));
//						}
						tp.setCreateBy(loginUser.getUserId());
						tp.setCreateDate(date);
						tp.setAuditingOpinion("");
						dao.insert(tp);
						
//						String balanceId = request.getParamValue("balanceId");
						Integer count = dao.getCount(selClaimBalanceId);
						TtAsWrClaimBalancePO ac = new TtAsWrClaimBalancePO();
						TtAsWrClaimBalancePO bc = new TtAsWrClaimBalancePO();
						ac.setId(Long.parseLong(selClaimBalanceId));
						bc.setSpecFeeAuthCount(Long.parseLong(String.valueOf(count)));
						dao.update(ac, bc);
					}
					
				}
				
			}
			
			
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	public void areaAuditSpeciaExpenses(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String auditFlag = request.getParamValue("auditFlag");
			Date date = new Date();
			TtAsWrSpefeePO po = new TtAsWrSpefeePO();
			TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
			if(auditFlag.equals("1")){
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_04));//已结算
			}else{
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_03));//驳回
				String balanceId = request.getParamValue("balanceId");
				Integer count = dao.getCount(balanceId);
				TtAsWrClaimBalancePO ac = new TtAsWrClaimBalancePO();
				TtAsWrClaimBalancePO bc = new TtAsWrClaimBalancePO();
				ac.setId(Long.parseLong(id));
				bc.setSpecFeeAuthCount(Long.parseLong(String.valueOf(count)));
				dao.update(ac, bc);
			}
			po.setUpdateBy(loginUser.getUserId());
			po.setUpdateDate(date);
			po.setAuditingOpinion(remark);//审核意见
			pp.setId(Long.parseLong(id));
			dao.update(pp, po);//修改TT_AS_WR_SPEFEE 工单费用

			TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
			tp.setId(Long.parseLong(SequenceManager.getSequence("")));
			tp.setFeeId(pp.getId());
			tp.setAuditingDate(date);
			tp.setAuditingPerson(loginUser.getName());
			tp.setPresonDept(loginUser.getOrgId());
			if(auditFlag.equals("1")){//通过
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_04));
			}else{//驳回
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_03));
			}
			tp.setCreateBy(loginUser.getUserId());
			tp.setCreateDate(date);
			tp.setAuditingOpinion(remark);
			dao.insert(tp);
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	public void sumDayCount(){
		
		
		try {
			
			String val1 = CommonUtils.checkNull(request.getParamValue("day1"));
			String val2 = CommonUtils.checkNull(request.getParamValue("day2"));
			if(val1.equals("")||val2.equals("")){
				act.setOutData("dayCount", 0);
			}else{
				int dayCount = Utility.compareDate(val1, val2, 0);
				act.setOutData("dayCount", dayCount);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 大区审核页面查询初始化
	 */
	public void areaSpecialExaminesFor(){
		
		
		try {
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			
			act.setOutData("yieldly", yieldly);
			act.setForword(areaAuditUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 车厂查询
	 */
	public void specialSerchFor(){
		
		
		try {
			TmBusinessAreaPO po = new TmBusinessAreaPO();
			List<PO> listpo = dao.select(po);
			//查询tc_code 8008 区分轿微车
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List list = dao.select(code) ;
			if(list.size()>0)
				code = (TcCodePO)list.get(0);
			act.setOutData("code", code) ;
			act.setOutData("Area", listpo);
			act.setForword(queryUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 经销商查询
	 */
	public void dealerSpSearchFor(){
		
		
		try {
			TmBusinessAreaPO po = new TmBusinessAreaPO();
			List<PO> list = dao.select(po);
			act.setOutData("Area", list);
			act.setForword(queryDealerUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 特殊(外出)费用 打印
	 */
	public void windowPrintList(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String feeType = request.getParamValue("feeType");
			Map<String, Object> map = dao.getSpeciaExpensesInfo(id);
			List<Map<String, Object>> list = dao.getSpeciaExpensesAudit(id);
			List<Map<String, Object>> claim = dao.getFeeRoList(id);
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("claim", claim);
			act.setOutData("fileList", fileList);
			if(feeType.equals(Constant.FEE_TYPE_01)){
				act.setForword(print01Url);
			}else{
				act.setForword(print02Url);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊外出费用打印");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 与索赔单关联
	 */
	public void showFeeType2Map(){
		
		
		try {
			
			String id = request.getParamValue("id");
			Map<String, Object> map = dao.getSpeciaExpensesInfo(id);
			List<Map<String, Object>> list = dao.getSpeciaExpensesAudit(id);
			List<Map<String, Object>> claim = dao.getFeeRoList(id);
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
			
			//查询tc_code 8008 区分轿微车
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List list2 = dao.select(code) ;
			if(list2.size()>0)
				code = (TcCodePO)list2.get(0);
			act.setOutData("code", code) ;
			
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("claim", claim);
			act.setOutData("fileList", fileList);
			act.setForword(showClaim);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊外出费用详细");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	public void addClaim(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String channel = request.getParamValue("channel");
			String yieldly = request.getParamValue("yieldly");
			act.setOutData("id", id);
			act.setOutData("channel",channel);
			act.setOutData("yieldly",yieldly);
			act.setForword(claimInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	public void addRO(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String channel = request.getParamValue("channel");
			String yieldly = request.getParamValue("yieldly");
			act.setOutData("id", id);
			act.setOutData("channel",channel);
			act.setOutData("yieldly",yieldly);
			act.setForword(roInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	public void applicationQuery() {
		
		AclUserBean loginUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		String dealerId = loginUser.getDealerId();
		Map<String, String> map = new HashMap<String, String>();
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String claimNo = request.getParamValue("CLAIM_NO");
			String vin = request.getParamValue("VIN");
			String roStartdate = request.getParamValue("RO_STARTDATE");
			String roEnddate = request.getParamValue("RO_ENDDATE");
			String channel = request.getParamValue("channel");
			String yieldly = request.getParamValue("yieldly");
			map.put("dealerId", dealerId);
			map.put("claimNo", claimNo);
			map.put("vin", vin);
			map.put("roStartdate", roStartdate);
			map.put("roEnddate", roEnddate);
			map.put("status", String.valueOf(Constant.CLAIM_APPLY_ORD_TYPE_04));
			map.put("oemCompanyId", companyId.toString());
			map.put("channel", channel);
			map.put("yieldly", yieldly);

			PageResult<Map<String, Object>> ps = dao.queryApplication(map, 10, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void roQuery() {
		
		AclUserBean loginUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		String dealerId = loginUser.getDealerId();
		Map<String, String> map = new HashMap<String, String>();
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String roNo = request.getParamValue("RO_NO");
			String vin = request.getParamValue("VIN");
			String roStartdate = request.getParamValue("RO_STARTDATE");
			String roEnddate = request.getParamValue("RO_ENDDATE");
			String channel = request.getParamValue("channel");
			String yieldly = request.getParamValue("yieldly");
			map.put("dealerId", dealerId);
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("roStartdate", roStartdate);
			map.put("roEnddate", roEnddate);
			map.put("oemCompanyId", companyId.toString());
			map.put("channel", channel);
			map.put("yieldly", yieldly);

			PageResult<Map<String, Object>> ps = dao.queryRO(map, 10, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void addAppaction(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String[] ids = request.getParamValues("ids");
			if(ids!=null&&!ids.equals("")){
				String claimId = "";
				for(int i=0;i<ids.length;i++){
					if(claimId.equals("")){
						claimId = ids[i];
					}else{
						claimId = claimId + "," + ids[i];
					}
				}
				List<Map<String, Object>> list = dao.getClaimList(claimId);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String, Object> map = list.get(i);
						TtAsWrSpefeeClaimPO po = new TtAsWrSpefeeClaimPO();
						po.setId(Long.parseLong(SequenceManager.getSequence("")));
						po.setFeeId(Long.parseLong(id));
						po.setClaimId(Long.parseLong(String.valueOf(map.get("CLAIM_ID"))));
						po.setClaimNo(String.valueOf(map.get("CLAIM_NO")));
						po.setSeries(String.valueOf(map.get("SERIES_CODE")));
						po.setModel(String.valueOf(map.get("MODEL_CODE")));
						po.setVin(String.valueOf(map.get("VIN")));
						po.setEngineNo(String.valueOf(map.get("ENGINE_NO")));
						//po.setProductDate(formatter.parse(String.valueOf(map.get("PRODUCT_DATE"))));
						po.setMileage(Double.parseDouble(String.valueOf(map.get("IN_MILEAGE"))));
						po.setCreateBy(loginUser.getUserId());
						po.setCreateDate(new Date());
						po.setClaimDate(po.getCreateDate());
						dao.insert(po);
					}
				}
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	//将工单添加至特殊费用_索赔单表。此时没单独建特殊费用_工单表
	public void addRoToR(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String[] ids = request.getParamValues("ids");
			if(ids!=null&&!ids.equals("")){
				String roId = "";
				for(int i=0;i<ids.length;i++){
					if(roId.equals("")){
						roId = ids[i];
					}else{
						roId = roId + "," + ids[i];
					}
				}
				List<Map<String, Object>> list = dao.getRoList(roId);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String, Object> map = list.get(i);
						TtAsWrSpefeeClaimPO po = new TtAsWrSpefeeClaimPO();
						po.setId(Long.parseLong(SequenceManager.getSequence("")));
						po.setFeeId(Long.parseLong(id));
						po.setClaimId(Long.parseLong(String.valueOf(map.get("RO_ID"))));
						po.setClaimNo(String.valueOf(map.get("RO_NO")));
						po.setSeries(String.valueOf(map.get("SERIES")));
						po.setModel(String.valueOf(map.get("MODEL")));
						po.setVin(String.valueOf(map.get("VIN")));
						po.setEngineNo(String.valueOf(map.get("ENGINE_NO")));
						if(StringUtil.notNull(CommonUtils.checkNull(map.get("PRODUCT_DATE"))))
							po.setProductDate(formatter.parse(String.valueOf(map.get("PRODUCT_DATE"))));
						po.setMileage(Double.parseDouble(String.valueOf(map.get("IN_MILEAGE"))));
						po.setCreateBy(loginUser.getUserId());
						SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						po.setCreateDate(new Date());
						po.setClaimDate(fmt.parse(String.valueOf(map.get("CREATE_DATE"))));
						dao.insert(po);
					}
				}
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	public void delCliam(){
		
		
		try {
			
			String id = request.getParamValue("id");
			TtAsWrSpefeeClaimPO po = new TtAsWrSpefeeClaimPO();
			po.setId(Long.parseLong(id));
			dao.delete(po);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	public void getModel(){
		
		
		try {
			
			String vin = request.getParamValue("vin");
			Map<String, Object> map = dao.getModel(vin);
			act.setOutData("map", map);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
		
	}
	

	/*
	 * 活动工单选择服务活动初始化
	 */
	public void queryActivityInit(){
		
		
		try {
			
			String dealer_code = request.getParamValue("code");
			act.setOutData("code",dealer_code);
			act.setForword(queryActivity);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	public void queryActivity(){
		
		
		try {
			RequestWrapper req = act.getRequest();
			String dealer_code = req.getParamValue("code");
			String a_code = req.getParamValue("activity_code");
			String a_name = req.getParamValue("activity_name");
			StringBuffer con = new StringBuffer();
			if(StringUtil.notNull(a_code)){
				con.append("and a.activity_code like '%"+a_code+"%'\n");
			}
			if(StringUtil.notNull(a_name)){
				con.append("and a.activity_name like '%"+a_name+"%'\n");
			}
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")): 1; // 处理当前页
			int pageSize = 10 ;
			PageResult<Map<String,Object>> ps=dao.getActivity(con.toString(),dealer_code, pageSize, curPage);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	public void queryActivityName(){
		
		
		try {
			
			String id = request.getParamValue("id");
			TtAsActivityPO po = new TtAsActivityPO();
			po.setActivityId(Long.parseLong(id));
			List list = dao.select(po);
			String name = "" ;
			if(list.size()>0){
				name = ((TtAsActivityPO)list.get(0)).getActivityName() ;
			}
			act.setOutData("name", name);
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 工单的查询页面初始化
	 */
	public void queryRoInit(){
		
		
		try {
			
			act.setForword(queryRo);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 特殊费用申报金额限制的判断
	 */
	public void speOutFeeCheck(){
		
		
		try{
			RequestWrapper req = act.getRequest();
			String singleMile = req.getParamValue("SINGLE_MILEAGE");
			String channel = req.getParamValue("FEE_CHANNEL");
			String id = req.getParamValue("id");
			//上门服务类型查询维修工单数
			if(Constant.FEE_CHANNEL_02.equals(channel)){
				List list = dao.getVinNum(id);
				act.setOutData("freeCharge", list.size());
			}
			
			/*if(Constant.FEE_CHANNEL_03.equals(channel))
				singleMile = req.getParamValue("mileage2");*/
	
			TmChananelMileagePO po = new TmChananelMileagePO() ;
			po.setChananelType(Integer.parseInt(channel));
			po.setMileage(Long.parseLong(singleMile));
			List list = dao.select(po);
			boolean flag = false ; //验证是否有此记录 
			if(list.size()>0){
				po = (TmChananelMileagePO)list.get(0);
				act.setOutData("bridge",po.getBridgeFee() );
				act.setOutData("eat",po.getCateringFee() );
				act.setOutData("quarter",po.getExpensesFee() );
				act.setOutData("mileage",po.getMileage() );
				act.setOutData("outDay",po.getOutDays() );
				act.setOutData("person",po.getPersonFee());
				act.setOutData("train",po.getTrainFee());
			}else
				flag = true ;
			act.setOutData("flag", flag);
			act.setOutData("val", req.getParamValue("type"));
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 航行路线选择页面初始化
	 */
	public void queryCruiseInit(){
		
		
		try {
			act.setForword(cruiseInit);			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"航行路线查询");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	public void queryCruise(){
		
		
		try {
			RequestWrapper req = act.getRequest();
			String cr_no = req.getParamValue("cr_no");
			StringBuffer sql = new StringBuffer();
			sql.append("  and c.dealer_id=").append(loginUser.getDealerId());
			if(StringUtil.notNull(cr_no)){
				sql.append("  and c.cr_no like '%").append(cr_no).append("%'\n");
			}
			sql.append("  and (c.status = 13631006 or");
			sql.append(" (c.status = 13631005 and c.suspend_date > sysdate - 3))\n");

			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")): 1; // 处理当前页
			int pageSize = 10 ;
			PageResult<Map<String,Object>> ps = dao.getCruise(sql.toString(), pageSize, curPage);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"航行路线查询");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	public void getCurise(){
		
		
		try {
			RequestWrapper req = act.getRequest();
			String id = req.getParamValue("id");
			TtAsWrCruisePO po = new TtAsWrCruisePO();
			po.setId(Long.parseLong(id));
			List list = dao.select(po);
			if(list.size()>0){
				po = (TtAsWrCruisePO)list.get(0);
				act.setOutData("address", po.getCrWhither());
				act.setOutData("days", po.getCrDay());
				act.setOutData("mileage", po.getCrMileage());
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"航行路线查询");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	//特殊费用的删除
	public void delSpefee(){
		
		
		try {
			RequestWrapper req = act.getRequest();
			String id = req.getParamValue("id");
			String sql1 = "delete from tt_as_wr_spefee_claim where fee_id="+id ;
			dao.delete(sql1,null);
			String sql2 = "delete from tt_as_wr_spefee where id="+id ;
			dao.delete(sql2,null);
			dealerSpProposeFor();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"特殊费用删除");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	//特殊费用重新审核
	public void reauditMainInit(){
		
		
		try {
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			act.setOutData("yieldly", yieldly);
			act.setForword(reauditUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"特殊费用删除");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	//重新审核查询方法
	public void reauditQuerySpeciaExpenses(){
		
		
		try {
			
			String feeNo = request.getParamValue("feeNo");
			String yieldly = request.getParamValue("yieldly");
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String feeType = request.getParamValue("feeType");
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			if(!dealerCode.equals("")){//截串加单引号
				String[] supp = dealerCode.split(",");
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}
			if(!"".equals(beginTime)){
				beginTime = beginTime + " 00:00:00";
			}
			if(!"".equals(endTime)){
				endTime = endTime + " 23:59:59";
			}
			//取得该用户拥有的产地权限
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			SpeciaExpensesBean bean = new SpeciaExpensesBean();
			bean.setFeeNo(feeNo);
			bean.setYieldly(yieldly);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFeeType(feeType);
			bean.setDealerCode(dealerCode);
			bean.setCompanyId(String.valueOf(loginUser.getCompanyId()));
			bean.setYieldlys(yieldlys);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")): 1; 
			PageResult<Map<String, Object>> ps = dao.reauditQuerySpeciaExpenses(bean,curPage,Constant.PAGE_SIZE);
			
			act.setOutData("ps", ps);     
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	//重新审核页面初始化
	public void reauditDoInit(){
		
		RequestWrapper req = act.getRequest() ;
		
		try {
			String id = req.getParamValue("id");
			String feeType = req.getParamValue("feeType");
			Map<String, Object> map = dao.getSpeciaExpensesInfo(id);
			List<Map<String, Object>> list = dao.getSpeciaExpensesAudit(id);
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
			List<Map<String, Object>> claim = dao.getFeeRoList(id);
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("fileList", fileList);
			act.setOutData("claim", claim);
			if(feeType.equals(Constant.FEE_TYPE_01)){
				act.setForword(reauditDo_01);
			}else{
				act.setForword(reauditDo_02);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"特殊费用删除");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	//重新审核实现方法
	public void reauditSpeciaExpenses01(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String auditFlag = request.getParamValue("auditFlag");
			String feeType = CommonUtils.checkNull(request.getParamValue("feeType"));
			String auditMoney = request.getParamValue("audit_money");//审核同意金额
			Date date = new Date();
			TtAsWrSpefeePO po = new TtAsWrSpefeePO();
			TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
			if(auditFlag.equals("1")){
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//已结算
			}else{
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//驳回
			}
			if(feeType.equals(Constant.FEE_TYPE_02)){
				String PASS_FEE = CommonUtils.checkNull(request.getParamValue("PASS_FEE"));//过路过桥
				String TRAFFIC_FEE = CommonUtils.checkNull(request.getParamValue("TRAFFIC_FEE"));//交通补助
				String QUARTER_FEE = CommonUtils.checkNull(request.getParamValue("QUARTER_FEE"));//住宿费
				String EAT_FEE = CommonUtils.checkNull(request.getParamValue("EAT_FEE"));//餐补
				String PERSON_SUBSIDE = CommonUtils.checkNull(request.getParamValue("PERSON_SUBSIDE"));//人员补助
				String declareSum = CommonUtils.checkNull(request.getParamValue("totalCount"));//总费用 
				po.setPassFee(Double.parseDouble(PASS_FEE));
				po.setTrafficFee(Double.parseDouble(TRAFFIC_FEE));
				po.setQuarterFee(Double.parseDouble(QUARTER_FEE));
				po.setEatFee(Double.parseDouble(EAT_FEE));
				po.setPersonSubside(Double.parseDouble(PERSON_SUBSIDE));
				po.setDeclareSum(Double.parseDouble(declareSum));
			}
			//审核同意金额
			if(StringUtil.notNull(auditMoney)){
				po.setDeclareSum(Double.parseDouble(auditMoney));
			}
			po.setUpdateBy(loginUser.getUserId());
			po.setUpdateDate(date);
			po.setAuditingOpinion(remark);//审核意见
			po.setBalanceAuditDate(new Date());
			pp.setId(Long.parseLong(id));
			dao.update(pp, po);//修改TT_AS_WR_SPEFEE 工单费用

			TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
			tp.setId(Long.parseLong(SequenceManager.getSequence("")));
			tp.setFeeId(pp.getId());
			tp.setAuditingDate(date);
			tp.setAuditingPerson(loginUser.getName());
			tp.setPresonDept(loginUser.getOrgId());
			if(auditFlag.equals("1")){//通过
				tp.setStatus(Integer.valueOf(Constant.SPEFEE_STATUS_05));
			}else{//拒绝
				tp.setStatus(Integer.valueOf(Constant.SPEFEE_STATUS_05));
			}
			tp.setCreateBy(loginUser.getUserId());
			tp.setCreateDate(date);
			tp.setAuditingOpinion(remark);
			dao.insert(tp);
			
			/*String balanceId = request.getParamValue("balanceId");
			Integer count = dao.getCount(balanceId);
			TtAsWrClaimBalancePO ac = new TtAsWrClaimBalancePO();
			TtAsWrClaimBalancePO bc = new TtAsWrClaimBalancePO();
			ac.setId(Long.parseLong(balanceId));
			bc.setSpecFeeAuthCount(Long.parseLong(String.valueOf(count)));
			dao.update(ac, bc);*/
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/*************************************微车特殊费用变更 add by kevinyin 20110418********************************************/
	/*
	 * 大区审核查询页面
	 * 市场，活动，特殊费用工单大区审核查询
	 */
	public void areaAuditQuerySpeciaExpensesWC(){
		
		
		try {
			
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String feeNo = request.getParamValue("feeNo");
				String yieldly = request.getParamValue("YIELDLY");
				String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
				String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
				String feeType = request.getParamValue("feeType");
				String feechannel = request.getParamValue("feeChannel");
				String feechannel1 = request.getParamValue("feeChannel_1");
				String orgId = String.valueOf(loginUser.getOrgId());
				//取得该用户拥有的产地权限
				String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
				
				String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
				if(!dealerCode.equals("")){//截串加单引号
					String[] supp = dealerCode.split(",");
					dealerCode = "";
					for(int i=0;i<supp.length;i++){
						supp[i] = "'"+supp[i]+"'";
						if(!dealerCode.equals("")){
							dealerCode += "," + supp[i];
						}else{
							dealerCode = supp[i];
						}
					}
				}
				if(!"".equals(beginTime)){
					beginTime = beginTime + " 00:00:00";
				}
				if(!"".equals(endTime)){
					endTime = endTime + " 23:59:59";
				}
				SpeciaExpensesBean bean = new SpeciaExpensesBean();
				bean.setFeeNo(feeNo);
				bean.setYieldly(yieldly);
				bean.setBeginTime(beginTime);
				bean.setEndTime(endTime);
				bean.setFeeType(feeType);
				bean.setDealerCode(dealerCode);
				bean.setOrgId(orgId);
				bean.setCompanyId(String.valueOf(loginUser.getCompanyId()));
				bean.setYieldlys(yieldlys);
				bean.setFeeChannel(feechannel);
				bean.setFeeChannel1(feechannel1);
				//分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页	
				PageResult<Map<String, Object>> ps = dao.areaAuditQuerySpeciaExpensesWC(loginUser.getUserId(),bean,curPage,Constant.PAGE_SIZE);
				//分页方法 end
				act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
			}else{
				//取得该用户拥有的产地权限			
				String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
				act.setOutData("yieldly", yieldly);
				act.setForword(areaAuditWCUrl);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 微车结算室审核查询页面
	 * 查询所有类型特殊费用
	 */
	public void auditQuerySpeciaExpensesWC(){
		
		
		try {
			
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String feeNo = request.getParamValue("feeNo");
				String yieldly = request.getParamValue("yieldly");
				String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
				String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
				String feeType = request.getParamValue("feeType");
				String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
				if(!dealerCode.equals("")){//截串加单引号
					String[] supp = dealerCode.split(",");
					dealerCode = "";
					for(int i=0;i<supp.length;i++){
						supp[i] = "'"+supp[i]+"'";
						if(!dealerCode.equals("")){
							dealerCode += "," + supp[i];
						}else{
							dealerCode = supp[i];
						}
					}
				}
				if(!"".equals(beginTime)){
					beginTime = beginTime + " 00:00:00";
				}
				if(!"".equals(endTime)){
					endTime = endTime + " 23:59:59";
				}
				//取得该用户拥有的产地权限
				String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
				SpeciaExpensesBean bean = new SpeciaExpensesBean();
				bean.setFeeNo(feeNo);
				bean.setYieldly(yieldly);
				bean.setBeginTime(beginTime);
				bean.setEndTime(endTime);
				bean.setFeeType(feeType);
				bean.setDealerCode(dealerCode);
				bean.setCompanyId(String.valueOf(loginUser.getCompanyId()));
				bean.setYieldlys(yieldlys);
				Long userId = loginUser.getUserId();
				//分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页	
				PageResult<Map<String, Object>> ps = dao.auditQuerySpeciaExpensesWC(userId,bean,curPage,Constant.PAGE_SIZE);
				//分页方法 end
				act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
			}else{
				//取得该用户拥有的产地权限			
				String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
				act.setOutData("yieldly", yieldly);
				act.setForword(auditUrlWCUrl);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 微车事业部审批
	 */
	public void areaAuditSpeciaExpensesWC(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String auditFlag = request.getParamValue("auditFlag");
//			String auditMoney = CommonUtils.checkNull(request.getParamValue("audit_money"));
			Date date = new Date();
			TtAsWrSpefeePO po = new TtAsWrSpefeePO();
			TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
			if(auditFlag.equals("1")){
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_04));//已结算
//				po.setBalanceAuditDate(new Date());//审核时间
			}else{
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_03));//驳回
				String balanceId = request.getParamValue("balanceId");
				Integer count = dao.getCount(balanceId);
				TtAsWrClaimBalancePO ac = new TtAsWrClaimBalancePO();
				TtAsWrClaimBalancePO bc = new TtAsWrClaimBalancePO();
				ac.setId(Long.parseLong(id));
				bc.setSpecFeeAuthCount(Long.parseLong(String.valueOf(count)));
				dao.update(ac, bc);
			}
			
			po.setUpdateBy(loginUser.getUserId());
			po.setUpdateDate(date);
			po.setAuditingOpinion(remark);//审核意见
			po.setAuditingDate(new Date());
			pp.setId(Long.parseLong(id));
			po.setAuditingDate(new Date());
			dao.update(pp, po);//修改TT_AS_WR_SPEFEE 工单费用

			TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
			tp.setId(Long.parseLong(SequenceManager.getSequence("")));
			tp.setFeeId(pp.getId());
			tp.setAuditingDate(date);
			tp.setAuditingPerson(loginUser.getName());
			tp.setPresonDept(loginUser.getOrgId());
			if(auditFlag.equals("1")){//通过
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_04));
				//自动结算（活动工单，特殊外出费用）
				Map<String, Object> map = dao.getSpeciaExpensesInfo(id);
				if(map.get("FEE_CHANNEL")!=null&&map.get("FEE_TYPE")!=null){
					if(map.get("FEE_TYPE").toString().equals(Constant.FEE_TYPE_02)||Integer.valueOf(map.get("FEE_CHANNEL").toString()).equals(Constant.FEE_TYPE1_02)){
						
						auditSpeciaExpenses01BackWC(map);
					}
				}
			}else{//驳回
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_03));
			}
			tp.setCreateBy(loginUser.getUserId());
			tp.setCreateDate(date);
			tp.setAuditingOpinion(remark);
			dao.insert(tp);
		
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 大区审核详细页(页面跟到详细页面)
	 */
	public void areaAuditSpecialExpensesWC(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String feeType = request.getParamValue("feeType");
			Map<String, Object> map = dao.getSpeciaExpensesInfoWC(id);
			List<Map<String, Object>> list = dao.getSpeciaExpensesAudit(id);
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
			List<Map<String, Object>> claim = dao.getFeeRoList(id);
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("fileList", fileList);
			act.setOutData("claim", claim);
			if(feeType.equals(Constant.FEE_TYPE_01)){
				act.setForword(audit01WCUrl);
			}else{
				act.setForword(areaAudit02WCUrl);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"大区审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 市场工单审核详细页
	 */
	public void auditSpecialExpensesInfoWC(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String feeType = request.getParamValue("feeType");
			Map<String, Object> map = dao.getSpeciaExpensesInfoWC(id);
			List<Map<String, Object>> list = dao.getSpeciaExpensesAudit(id);
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
			List<Map<String, Object>> claim = dao.getFeeRoList(id);
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("fileList", fileList);
			act.setOutData("claim", claim);
			if(feeType.equals(Constant.FEE_TYPE_01)){
				act.setForword(audit02WCUrl);
			}else{
				act.setForword(audit02Url);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 审核市场工单费用（结算室审核）
	 */
	public void auditSpeciaExpenses01WC(){
		
		
		try {
			
			String id = request.getParamValue("id");
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String auditFlag = request.getParamValue("auditFlag");
			String feeType = CommonUtils.checkNull(request.getParamValue("feeType"));
			String auditMoney = request.getParamValue("audit_money");//审核同意金额
			Date date = new Date();
			TtAsWrSpefeePO po = new TtAsWrSpefeePO();
			TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
			if(auditFlag.equals("1")){
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//已结算
				//类型为市场工单的市场工单结算室为最后审核，设置通过时间
//				po.setBalanceAuditDate(new Date());
			}else{
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//驳回
			}
			if(feeType.equals(Constant.FEE_TYPE_02)){
				String PASS_FEE = CommonUtils.checkNull(request.getParamValue("PASS_FEE"));//过路过桥
				String TRAFFIC_FEE = CommonUtils.checkNull(request.getParamValue("TRAFFIC_FEE"));//交通补助
				String QUARTER_FEE = CommonUtils.checkNull(request.getParamValue("QUARTER_FEE"));//住宿费
				String EAT_FEE = CommonUtils.checkNull(request.getParamValue("EAT_FEE"));//餐补
				String PERSON_SUBSIDE = CommonUtils.checkNull(request.getParamValue("PERSON_SUBSIDE"));//人员补助
				String declareSum = CommonUtils.checkNull(request.getParamValue("totalCount"));//总费用 
				po.setPassFee(Double.parseDouble(PASS_FEE));
				po.setTrafficFee(Double.parseDouble(TRAFFIC_FEE));
				po.setQuarterFee(Double.parseDouble(QUARTER_FEE));
				po.setEatFee(Double.parseDouble(EAT_FEE));
				po.setPersonSubside(Double.parseDouble(PERSON_SUBSIDE));
				po.setDeclareSum(Double.parseDouble(declareSum));
			}
			//审核同意金额
			if(StringUtil.notNull(auditMoney)){
				po.setDeclareSum(Double.parseDouble(auditMoney));
			}
			po.setUpdateBy(loginUser.getUserId());
			po.setUpdateDate(date);
			po.setAuditingOpinion(remark);//审核意见
			po.setBalanceAuditDate(new Date());  //结算室审核时间
			
			pp.setId(Long.parseLong(id));
			dao.update(pp, po);//修改TT_AS_WR_SPEFEE 工单费用

			TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
			tp.setId(Long.parseLong(SequenceManager.getSequence("")));
			tp.setFeeId(pp.getId());
			tp.setAuditingDate(date);
			tp.setAuditingPerson(loginUser.getName());
			tp.setPresonDept(loginUser.getOrgId());
			if(auditFlag.equals("1")){//通过
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));
			}else{//驳回
				tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));
			}
			tp.setCreateBy(loginUser.getUserId());
			tp.setCreateDate(date);
			tp.setAuditingOpinion(remark);
			dao.insert(tp);
			
//			String balanceId = request.getParamValue("balanceId");
//			Integer count = dao.getCount(balanceId);
//			TtAsWrClaimBalancePO ac = new TtAsWrClaimBalancePO();
//			TtAsWrClaimBalancePO bc = new TtAsWrClaimBalancePO();
//			ac.setId(Long.parseLong(balanceId));
//			bc.setSpecFeeAuthCount(Long.parseLong(String.valueOf(count)));
//			dao.update(ac, bc);
			
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	
	/*
	 * 审核市场工单费用（后台自动结算）
	 */
	public void auditSpeciaExpenses01BackWC(Map map){
		try {
			String remark = CommonUtils.checkNull(request.getParamValue(""));
			String auditMoney = map.get("DECLARE_SUM")==null?"":map.get("DECLARE_SUM").toString();
			String feeType = map.get("FEE_TYPE")==null?"":map.get("FEE_TYPE").toString();
			String id = map.get("ID")==null?"":map.get("ID").toString();
			Date date = new Date();
			TtAsWrSpefeePO po = new TtAsWrSpefeePO();
			TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
			po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//已结算
			if(feeType.equals(Constant.FEE_TYPE_02)){
				String PASS_FEE = map.get("PASS_FEE")==null?"":map.get("PASS_FEE").toString();//过路过桥
				String TRAFFIC_FEE = map.get("TRAFFIC_FEE")==null?"":map.get("TRAFFIC_FEE").toString();//交通补助
				String QUARTER_FEE = map.get("QUARTER_FEE")==null?"":map.get("QUARTER_FEE").toString();//住宿费
				String EAT_FEE = map.get("EAT_FEE")==null?"":map.get("EAT_FEE").toString();//餐补
				String PERSON_SUBSIDE = map.get("PERSON_SUBSIDE")==null?"":map.get("PERSON_SUBSIDE").toString();//人员补助
				String declareSum = map.get("DECLARE_SUM")==null?"":map.get("DECLARE_SUM").toString();//总费用 
				po.setPassFee(Double.parseDouble(PASS_FEE));
				po.setTrafficFee(Double.parseDouble(TRAFFIC_FEE));
				po.setQuarterFee(Double.parseDouble(QUARTER_FEE));
				po.setEatFee(Double.parseDouble(EAT_FEE));
				po.setPersonSubside(Double.parseDouble(PERSON_SUBSIDE));
				po.setDeclareSum(Double.parseDouble(declareSum));
			}
			//审核同意金额
			if(StringUtil.notNull(auditMoney)){
				po.setDeclareSum(Double.parseDouble(auditMoney));
			}
			po.setUpdateBy(loginUser.getUserId());
			po.setUpdateDate(date);
			po.setAuditingOpinion(remark);//审核意见
			po.setBalanceAuditDate(new Date());//审核通过时间
			pp.setId(Long.parseLong(id));
			dao.update(pp, po);//修改TT_AS_WR_SPEFEE 工单费用

			TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
			tp.setId(Long.parseLong(SequenceManager.getSequence("")));
			tp.setFeeId(pp.getId());
			tp.setAuditingDate(date);
			tp.setAuditingPerson("自动审核");
			tp.setPresonDept(loginUser.getOrgId());
			tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));
			tp.setCreateBy(loginUser.getUserId());
			tp.setCreateDate(date);
			tp.setAuditingOpinion(remark);
			dao.insert(tp);
			
//			String balanceId = request.getParamValue("balanceId");
//			Integer count = dao.getCount(balanceId);
//			TtAsWrClaimBalancePO ac = new TtAsWrClaimBalancePO();
//			TtAsWrClaimBalancePO bc = new TtAsWrClaimBalancePO();
//			ac.setId(Long.parseLong(balanceId));
//			bc.setSpecFeeAuthCount(Long.parseLong(String.valueOf(count)));
//			dao.update(ac, bc);
			
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	
	/*
	 * 逐条审核详细页
	 */
	public void auditSpecialExpensesInfoByOneWC(){
		try {
			String feeNo = request.getParamValue("feeNo");
			String yieldly = request.getParamValue("yieldly");
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String feeType = request.getParamValue("feeType");
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			//审核条数记录
			String count = request.getParamValue("count")==null?"0":request.getParamValue("count");
			if(!dealerCode.equals("")){//截串加单引号
				String[] supp = dealerCode.split(",");
				act.setOutData("dealerCode", dealerCode);
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}
			//取得该用户拥有的产地权限
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			SpeciaExpensesBean bean = new SpeciaExpensesBean();
			bean.setFeeNo(feeNo);
			bean.setYieldly(yieldly);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFeeType(feeType);
			bean.setDealerCode(dealerCode);
			bean.setCompanyId(String.valueOf(loginUser.getCompanyId()));
			bean.setYieldlys(yieldlys);
			Long userId = loginUser.getUserId();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			List<Map<String, Object>> ps = dao.auditQuerySpeciaExpensesByOneWC(userId,bean);
			//分页方法 end
			if(ps!=null){
				if(ps.size()>Integer.valueOf(count)){
					Map<String, Object> specialMap = ps.get(Integer.valueOf(count));
					     //向前台传的list 名称是固定的不可改必须用 ps
					
					//查询第一条特殊费用单信息
					String id = specialMap.get("ID").toString();
		//			String feeTypeGet = specialMap.get("FEE_TYPE").toString();
					Map<String, Object> map = dao.getSpeciaExpensesInfo(id);
					List<Map<String, Object>> list = dao.getSpeciaExpensesAudit(id);
					StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
					List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
					List<Map<String, Object>> claim = dao.getFeeRoList(id);
					act.setOutData("firstSpecial", specialMap);  //与单条审核区别
					act.setOutData("map", map);
					act.setOutData("list", list);
					act.setOutData("fileList", fileList);
					act.setOutData("claim", claim);
					
					//向下一条传查询条件
					
					act.setOutData("beginTime", beginTime);
					act.setOutData("endTime", endTime);
					act.setOutData("feeNo", feeNo);
					act.setOutData("yieldly", yieldly);
					act.setOutData("feeType", feeType);
					act.setOutData("count", count);
					if(specialMap.get("FEE_TYPE").toString().equals(Constant.FEE_TYPE_01)){
						act.setForword(auditByOne01WCUrl);
					}else{
						act.setForword(auditByOne02Url);
					}
				}else{
					act.setForword(auditWCUrl);
				}
			}else{
				act.setForword(auditWCUrl);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 审核市场工单费用批量审核
	 */
	public void auditSpeciaExpensesBatch01WC(){
		try {
			String specialId[] = request.getParamValues("specialId");
//			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
//			String auditFlag = request.getParamValue("auditFlag");
			String[] feeType = request.getParamValues("feeTypeT");
			
			String[] mySpecialId = request.getParamValues("mySpecialId");
			String[] passFee = request.getParamValues("PASS_FEE");
			String[] trafficFee = request.getParamValues("TRAFFIC_FEE");
			String[] quarterFee = request.getParamValues("QUARTER_FEE");
			String[] eatFee = request.getParamValues("EAT_FEE");
			String[] personSubside = request.getParamValues("PERSON_SUBSIDE");
			String[] claimBalanceId = request.getParamValues("CLAIMBALANCE_ID");
			
			String selFeeType = "";
			String selPassFee = "";
			String selTrafficFee = "";
			String selQuarterFee = "";
			String selEatFee = "";
			String selPersonSubside = "";
//			String selClaimBalanceId = "";
			for(int i = 0; i < specialId.length; i++){
				for(int j = 0 ; j < mySpecialId.length; j++){
					if(mySpecialId[j].equals(specialId[i])){
						selFeeType = feeType[j];
						selPassFee = passFee[j];
						selTrafficFee = trafficFee[j];
						selQuarterFee = quarterFee[j];
						selEatFee = eatFee[j];
						selPersonSubside = personSubside[j];
//						selClaimBalanceId = claimBalanceId[j];
						
						Date date = new Date();
						TtAsWrSpefeePO po = new TtAsWrSpefeePO();
						TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
//						if(auditFlag.equals("1")){
							po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//已结算
//						}else{
//							po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//驳回
//						}
						if(selFeeType.equals(Constant.FEE_TYPE_02)){
							Double PASS_FEE = Double.valueOf(selPassFee);//过路过桥
							Double TRAFFIC_FEE = Double.valueOf(selTrafficFee);//交通补助
							Double QUARTER_FEE = Double.valueOf(selQuarterFee);//住宿费
							Double EAT_FEE = Double.valueOf(selEatFee);//餐补
							Double PERSON_SUBSIDE = Double.valueOf(selPersonSubside);//人员补助
							Double declareSum = PASS_FEE+TRAFFIC_FEE+QUARTER_FEE+EAT_FEE+PERSON_SUBSIDE;//总费用 
							po.setPassFee(PASS_FEE);
							po.setTrafficFee(TRAFFIC_FEE);
							po.setQuarterFee(QUARTER_FEE);
							po.setEatFee(EAT_FEE);
							po.setPersonSubside(PERSON_SUBSIDE);
							po.setDeclareSum(declareSum);
						}
						
						po.setUpdateBy(loginUser.getUserId());
						po.setUpdateDate(date);
						po.setAuditingOpinion("");//审核意见
						po.setBalanceAuditDate(new Date());
						pp.setId(Long.parseLong(specialId[i]));
						dao.update(pp, po);//修改TT_AS_WR_SPEFEE 工单费用

						TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
						tp.setId(Long.parseLong(SequenceManager.getSequence("")));
						tp.setFeeId(pp.getId());
						tp.setAuditingDate(date);
						tp.setAuditingPerson(loginUser.getName());
						tp.setPresonDept(loginUser.getOrgId());
//						if(auditFlag.equals("1")){//通过
							tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));
//						}else{//驳回
//							tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));
//						}
						tp.setCreateBy(loginUser.getUserId());
						tp.setCreateDate(date);
						tp.setAuditingOpinion("");
						dao.insert(tp);
						
//						String balanceId = request.getParamValue("balanceId");
//						Integer count = dao.getCount(selClaimBalanceId);
//						TtAsWrClaimBalancePO ac = new TtAsWrClaimBalancePO();
//						TtAsWrClaimBalancePO bc = new TtAsWrClaimBalancePO();
//						ac.setId(Long.parseLong(selClaimBalanceId));
//						bc.setSpecFeeAuthCount(Long.parseLong(String.valueOf(count)));
//						dao.update(ac, bc);
					}
					
				}
				
			}
			
			
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	//重新审核查询方法(微车)
	public void reauditQuerySpeciaExpensesWC(){
		try {
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String feeNo = request.getParamValue("feeNo");
				String yieldly = request.getParamValue("yieldly");
				String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
				String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
				String feeType = request.getParamValue("feeType");
				String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
				String spefeeStatus = CommonUtils.checkNull(request.getParamValue("spefeeStatus"));
				String feeChannel = CommonUtils.checkNull(request.getParamValue("feeChannel"));
				String feeChannel1 = CommonUtils.checkNull(request.getParamValue("feeChannel_1"));
				if(!dealerCode.equals("")){//截串加单引号
					String[] supp = dealerCode.split(",");
					dealerCode = "";
					for(int i=0;i<supp.length;i++){
						supp[i] = "'"+supp[i]+"'";
						if(!dealerCode.equals("")){
							dealerCode += "," + supp[i];
						}else{
							dealerCode = supp[i];
						}
					}
				}
				if(!"".equals(beginTime)){
					beginTime = beginTime + " 00:00:00";
				}
				if(!"".equals(endTime)){
					endTime = endTime + " 23:59:59";
				}
				//取得该用户拥有的产地权限
				String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
				SpeciaExpensesBean bean = new SpeciaExpensesBean();
				bean.setFeeNo(feeNo);
				bean.setYieldly(yieldly);
				bean.setBeginTime(beginTime);
				bean.setEndTime(endTime);
				bean.setFeeType(feeType);
				bean.setDealerCode(dealerCode);
				bean.setCompanyId(String.valueOf(loginUser.getCompanyId()));
				bean.setYieldlys(yieldlys);
				bean.setSpefeeStatus(spefeeStatus);
				bean.setFeeChannel(feeChannel);
				bean.setFeeChannel1(feeChannel1);
				
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage")): 1; 
				PageResult<Map<String, Object>> ps = dao.reauditQuerySpeciaExpensesWC(bean,curPage,Constant.PAGE_SIZE);
				
				act.setOutData("ps", ps);  
			}else{
				String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
				act.setOutData("yieldly", yieldly);
				act.setForword(reauditWCUrl);
			}
			   
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 车厂查询(微车)
	 */
	public void specialSerchForWC(){
		try {
			String dutyType = loginUser.getDutyType();
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			
			act.setOutData("yieldly", yieldly);
			act.setOutData("dutyType", dutyType);
			act.setForword(queryWCUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 车厂端查询页面
	 */
	public void queryOemSpeciaExpensesWC(){
		try {
			String feeNo = request.getParamValue("feeNo");
			String yieldly = request.getParamValue("yieldly");
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String feeType = request.getParamValue("feeType");
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			/***add xiongchuan 2011-03-14 增加费用渠道查询*****/
			String feeChannel=CommonUtils.checkNull(request.getParamValue("FEE_CHANNEL"));
			//取得该用户拥有的产地权限
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			if(!dealerCode.equals("")){//截串加单引号
				String[] supp = dealerCode.split(",");
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}
			if(!"".equals(beginTime)){
				beginTime = beginTime + " 00:00:00";
			}
			if(!"".equals(endTime)){
				endTime = endTime + " 23:59:59";
			}
			String dutyType = loginUser.getDutyType();
			String orgId = String.valueOf(loginUser.getOrgId());
			SpeciaExpensesBean bean = new SpeciaExpensesBean();
			bean.setFeeNo(feeNo);
			bean.setYieldly(yieldly);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFeeType(feeType);
			bean.setDealerCode(dealerCode);
			bean.setOrgId(orgId);
			bean.setDutyType(dutyType);
			bean.setCompanyId(String.valueOf(loginUser.getCompanyId()));
			bean.setYieldlys(yieldlys);
			bean.setFeeChannel(feeChannel);
			PageResult<Map<String, Object>> ps = dao.queryOmeSpeciaExpensesWC(bean,getCurrPage(),Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	//重新审核页面初始化
	public void reauditDoInitWC(){
		try {
			String id = request.getParamValue("id");
			String feeType = request.getParamValue("feeType");
			Map<String, Object> map = dao.getSpeciaExpensesInfo(id);
			List<Map<String, Object>> list = dao.getSpeciaExpensesAudit(id);
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
			List<Map<String, Object>> claim = dao.getFeeRoList(id);
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("fileList", fileList);
			act.setOutData("claim", claim);
			if(feeType.equals(Constant.FEE_TYPE_01)){
				act.setForword(reauditDo_01WC);
			}else{
				act.setForword(reauditDo_02);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"特殊费用删除");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	//重新审核实现方法
	public void reauditSpeciaExpenses01WC(){
		try {
			String id = request.getParamValue("id");
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String auditFlag = request.getParamValue("auditFlag");
			String feeType = CommonUtils.checkNull(request.getParamValue("feeType"));
			String auditMoney = request.getParamValue("audit_money");//审核同意金额
			Date date = new Date();
			TtAsWrSpefeePO po = new TtAsWrSpefeePO();
			TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
			if(auditFlag.equals("1")){
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//已结算
//				po.setBalanceAuditDate(new Date());
			}else{
				po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_05));//驳回
			}
			if(feeType.equals(Constant.FEE_TYPE_02)){
				String PASS_FEE = CommonUtils.checkNull(request.getParamValue("PASS_FEE"));//过路过桥
				String TRAFFIC_FEE = CommonUtils.checkNull(request.getParamValue("TRAFFIC_FEE"));//交通补助
				String QUARTER_FEE = CommonUtils.checkNull(request.getParamValue("QUARTER_FEE"));//住宿费
				String EAT_FEE = CommonUtils.checkNull(request.getParamValue("EAT_FEE"));//餐补
				String PERSON_SUBSIDE = CommonUtils.checkNull(request.getParamValue("PERSON_SUBSIDE"));//人员补助
				String declareSum = CommonUtils.checkNull(request.getParamValue("totalCount"));//总费用 
				po.setPassFee(Double.parseDouble(PASS_FEE));
				po.setTrafficFee(Double.parseDouble(TRAFFIC_FEE));
				po.setQuarterFee(Double.parseDouble(QUARTER_FEE));
				po.setEatFee(Double.parseDouble(EAT_FEE));
				po.setPersonSubside(Double.parseDouble(PERSON_SUBSIDE));
				po.setDeclareSum(Double.parseDouble(declareSum));
			}
			//审核同意金额
			if(StringUtil.notNull(auditMoney)){
				po.setDeclareSum(Double.parseDouble(auditMoney));
			}
			po.setUpdateBy(loginUser.getUserId());
			po.setUpdateDate(date);
			po.setAuditingOpinion(remark);//审核意见
			po.setBalanceAuditDate(new Date());  //结算室审核时间
			pp.setId(Long.parseLong(id));
			dao.update(pp, po);//修改TT_AS_WR_SPEFEE 工单费用

			TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
			tp.setId(Long.parseLong(SequenceManager.getSequence("")));
			tp.setFeeId(pp.getId());
			tp.setAuditingDate(date);
			tp.setAuditingPerson(loginUser.getName()+"[重新审核]");
			tp.setPresonDept(loginUser.getOrgId());
			if(auditFlag.equals("1")){//通过
				tp.setStatus(Integer.valueOf(Constant.SPEFEE_STATUS_05));
			}else{//驳回
				tp.setStatus(Integer.valueOf(Constant.SPEFEE_STATUS_05));
			}
			tp.setCreateBy(loginUser.getUserId());
			tp.setCreateDate(date);
			tp.setAuditingOpinion(remark);
			dao.insert(tp);
			
			/*String balanceId = request.getParamValue("balanceId");
			Integer count = dao.getCount(balanceId);
			TtAsWrClaimBalancePO ac = new TtAsWrClaimBalancePO();
			TtAsWrClaimBalancePO bc = new TtAsWrClaimBalancePO();
			ac.setId(Long.parseLong(balanceId));
			bc.setSpecFeeAuthCount(Long.parseLong(String.valueOf(count)));
			dao.update(ac, bc);*/
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	//特殊费用重新审核
	public void reauditMainInitWC(){
		try {
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			act.setOutData("yieldly", yieldly);
			act.setForword(reauditWCUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"特殊费用删除");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/*************************************微车特殊费用变更add by kevinyin 20110418********************************************/
	
	
	/****************************轿车特殊费用变更 add by kevinyin 20110530*********************************************/
	//主页面
	public void specialExaminesForJC(){
		try {
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			TmBusinessAreaPO po = new TmBusinessAreaPO();
			List<PO> list = dao.select(po);
			act.setOutData("Area", list);
			act.setOutData("yieldly", yieldly);
			act.setForword(auditUrlJC);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	public void specialDirector(){
		
		
		try {
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			TmBusinessAreaPO po = new TmBusinessAreaPO();
			List<PO> list = dao.select(po);
			act.setOutData("Area", list);
			act.setOutData("yieldly", yieldly);
			act.setForword(auditUrlJCDirector);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算室审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	//主页面查询 zyw 2014-9-12重构
	public void auditQuerySpeciaExpensesJC(){
		try {
			List<Map<String, Object>> list = this.findAllRigehtByUserId();//查权限
			StringBuffer sb = this.judeBackBuff(list);//拼sql
			SpeciaExpensesBean bean = this.backInfoToSpeciaBean(sb);//插入数据
			PageResult<Map<String, Object>> ps = dao.auditQuerySpeciaExpensesJC(bean,loginUser,getCurrPage(),Constant.PAGE_SIZE);
			act.setOutData("ps", ps);  
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 查找出所有关于当前用户的所有特殊费用权限
	 * @return
	 */
	private List<Map<String, Object>> findAllRigehtByUserId() {
		StringBuffer sql = new StringBuffer();
		sql.append("select B.Spe_Level,B.MIN_AMOUNT,B.MAX_AMOUNT from TT_AS_WR_SPECIAL_CHARGE_AUDIT A,TT_AS_WR_SPECIAL_CHARGE B where B.SPE_ID = A.SPE_ID AND A.User_Id=");
		sql.append(""+this.loginUser.getUserId());
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	/**
	 * 取得数据插入临时bean 里
	 * @param min_amount
	 * @param max_amount
	 * @param type
	 * @param sb
	 * @return
	 */
	private SpeciaExpensesBean backInfoToSpeciaBean(StringBuffer sb) {
		double  min_amount=  0;
		double  max_amount=  0;
		String feeNo = request.getParamValue("feeNo");
		String yieldly = request.getParamValue("yieldly");
		String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
		String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
		String CREATE_DATE_S = CommonUtils.checkNull(request.getParamValue("CREATE_DATE_S"));
		String CREATE_DATE_D = CommonUtils.checkNull(request.getParamValue("CREATE_DATE_D"));
		String feeType = request.getParamValue("feeType");
		String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
		//取得该用户拥有的产地权限
		SpeciaExpensesBean bean = new SpeciaExpensesBean();
		bean.setMin_amount(min_amount);
		bean.setMax_amount(max_amount);
		bean.setFeeNo(feeNo);
		bean.setCREATE_DATE_S(CREATE_DATE_S);
		bean.setCREATE_DATE_D(CREATE_DATE_D);
		bean.setMsg(sb.toString());
		bean.setYieldly(yieldly);
		bean.setBeginTime(beginTime);
		bean.setType(type);
		bean.setEndTime(endTime);
		bean.setFeeType(feeType);
		bean.setDealerCode(dealerCode);
		bean.setAid(""+loginUser.getUserId());
		return bean;
	}
	/**
	 * 判断并返回拼接好的SQL
	 * @param type
	 * @param k
	 * @param list
	 * @param sb
	 * @return
	 */
	private StringBuffer judeBackBuff(List<Map<String,Object>>list){
		StringBuffer sb=new StringBuffer();
		int k = 0;
		if(list!=null && list.size()>0){
		sb.append("  AND(");
		for(int i=0;i<list.size();i++){
			int jude = Integer.parseInt(String.valueOf(list.get(i).get("SPE_LEVEL")));//审核级别
			if(jude ==  Constant.FLEET_JUDE_STATUS_01){//服务经理审核
				if(k == 0){
					sb.append("    (B.Status = "+Constant.SPEFEE_STATUS_02+" or B.Status = "+Constant.SPEFEE_STATUS_05+")\n" );
					k++;
				}else{
					sb.append("   OR (B.Status = "+Constant.SPEFEE_STATUS_02+ " or B.Status = "+Constant.SPEFEE_STATUS_05+" )\n" );
				}
				type = 1;
			}else if(jude ==  Constant.FLEET_JUDE_STATUS_02){//部门总监审核
				if(k == 0){
					sb.append("    (B.Status = "+Constant.SPEFEE_STATUS_06+" or B.Status = "+Constant.SPEFEE_STATUS_09+"\n" );
					sb.append("  AND B.DECLARE_SUM1 >= "+Double.valueOf(String.valueOf(list.get(i).get("MIN_AMOUNT")))+")");
					k++;
				}else{
					sb.append("   OR (B.Status = "+Constant.SPEFEE_STATUS_06+"  or B.Status = "+Constant.SPEFEE_STATUS_09+"\n" );
					sb.append("  AND B.DECLARE_SUM1 >= "+Double.valueOf(String.valueOf(list.get(i).get("MIN_AMOUNT")))+")");
				}
				type = 2;
			}else if(jude ==  Constant.FLEET_JUDE_STATUS_03){//销售公司总经理审核
				if(k == 0){
					sb.append("    ( B.Status = "+Constant.SPEFEE_STATUS_08+" or B.Status = "+Constant.SPEFEE_STATUS_11+"\n" );
					sb.append("   AND B.DECLARE_SUM1 >= "+Double.valueOf(String.valueOf(list.get(i).get("MIN_AMOUNT")))+")");
					k++;
				}else {
					sb.append("   OR ( B.Status = "+Constant.SPEFEE_STATUS_08+" or B.Status = "+Constant.SPEFEE_STATUS_11+"\n" );
					sb.append("   AND B.DECLARE_SUM1 >= "+Double.valueOf(String.valueOf(list.get(i).get("MIN_AMOUNT")))+")");
				}
				type = 3;
			}else if(jude ==  Constant.FLEET_JUDE_STATUS_04){//集团总经理审核
				if(k == 0){
					sb.append("    ( B.Status = "+Constant.SPEFEE_STATUS_10+"\n" );
					sb.append("   AND B.DECLARE_SUM1 >= "+Double.valueOf(String.valueOf(list.get(i).get("MIN_AMOUNT")))+")");
					k++;
				}else {
					sb.append("   OR ( B.Status = "+Constant.SPEFEE_STATUS_10+"\n" );
					sb.append("   AND B.DECLARE_SUM1 >= "+Double.valueOf(String.valueOf(list.get(i).get("MIN_AMOUNT")))+")");
				}
				type = 4;
			}else if(jude ==  Constant.FLEET_JUDE_STATUS_05){//车厂审核
				if(k == 0){
					sb.append("    ( B.Status = "+Constant.SPEFEE_STATUS_04+" or B.Status = "+Constant.SPEFEE_STATUS_07+")\n" );
					k++;
				}else {
					sb.append("   OR ( B.Status = "+Constant.SPEFEE_STATUS_04+" or B.Status = "+Constant.SPEFEE_STATUS_07+")\n" );
				}
				type = 5;
			}else if(jude ==  Constant.FLEET_JUDE_STATUS_06){//最终审核
				if(k == 0){
					sb.append("    ( B.O_Status = "+Constant.SPEFEE_STATUS_14+" and B.Status<>11841014 and B.Status<>11841001 )\n" );
					k++;
				}else{
					sb.append("   OR ( B.O_Status = "+Constant.SPEFEE_STATUS_14+" and B.Status<>11841014 and B.Status<>11841001 )\n" );
				}
				type = 6;
				}
			}
		}
		sb.append(" )");
		return sb;
	}
	
	
	public void auditQuerySpeciaExpensesJCDirector(){
		try {
			String feeNo = request.getParamValue("feeNo");
			String yieldly = request.getParamValue("yieldly");
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String CREATE_DATE_S = CommonUtils.checkNull(request.getParamValue("CREATE_DATE_S"));
			String CREATE_DATE_D = CommonUtils.checkNull(request.getParamValue("CREATE_DATE_D"));
			String feeType = request.getParamValue("feeType");
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			if(!dealerCode.equals("")){//截串加单引号
				String[] supp = dealerCode.split(",");
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}
			if(!"".equals(beginTime)){
				beginTime = beginTime + " 00:00:00";
			}
			if(!"".equals(endTime)){
				endTime = endTime + " 23:59:59";
			}
			//取得该用户拥有的产地权限
			SpeciaExpensesBean bean = new SpeciaExpensesBean();
			bean.setFeeNo(feeNo);
			bean.setCREATE_DATE_S(CREATE_DATE_S);
			bean.setCREATE_DATE_D(CREATE_DATE_D);
			bean.setYieldly(yieldly);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFeeType(feeType);
			bean.setDealerCode(dealerCode);
			bean.setAid(""+loginUser.getUserId());
			PageResult<Map<String, Object>> ps = dao.auditQuerySpeciaExpensesJCDirector(bean,getCurrPage(),Constant.PAGE_SIZE);
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用提报");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 审核市场工单费用 zyw 2014-9-15
	 */
	public void auditSpeciaExpenses01JC(){
		try {
			//String aid = request.getParamValue("aid");
			String auditFlag = request.getParamValue("auditFlag");
			String fid = request.getParamValue("fid");
			this.UpdateTtAsWrSpefeeByProperties(auditFlag, fid);
			this.updateSpefeeAuditingByProperties(fid, auditFlag);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 更新数据
	 * @param aid
	 * @param auditFlag
	 */
	private void updateSpefeeAuditingByProperties(String fid, String auditFlag) {
		TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
		tp.setId(DaoFactory.getPkId());
		tp.setFeeId(Long.parseLong(fid));
		tp.setAuditingDate(new Date());
		tp.setAuditingPerson(loginUser.getName());
		tp.setPresonDept(loginUser.getOrgId());
		this.setPoPerperties(tp,auditFlag);//为PO设置值，并判断审核的状态为其设值，为审核金额设值 根据 当前状态 通过+2 ，驳回-2
		tp.setUpdateBy((loginUser.getUserId()));
		tp.setUpdateDate(new Date());
		dao.insert(tp);
	}
	/**
	 * 更新数据
	 * @param auditFlag
	 * @param fid
	 */
	private void UpdateTtAsWrSpefeeByProperties(String auditFlag,String fid) {
		TtAsWrSpefeePO po = new TtAsWrSpefeePO();
		TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
		po.setUpdateBy(loginUser.getUserId());
		po.setUpdateDate(new Date());
		po.setBalanceAuditDate(new Date());  //结算室审核时间
		this.setPoPerperties(po,auditFlag);//为PO设置值，并判断审核的状态为其设值，为审核金额设值 根据 当前状态 通过+2 ，驳回-2
		pp.setId(Long.parseLong(fid));
		String feeType = CommonUtils.checkNull(request.getParamValue("feeType")); //费用类型
		if(feeType.equals(Constant.FEE_TYPE_02)){
			String PASS_FEE = CommonUtils.checkNull(request.getParamValue("PASS_FEE"));//过路过桥
			String TRAFFIC_FEE = CommonUtils.checkNull(request.getParamValue("TRAFFIC_FEE"));//交通补助
			String QUARTER_FEE = CommonUtils.checkNull(request.getParamValue("QUARTER_FEE"));//住宿费
			String EAT_FEE = CommonUtils.checkNull(request.getParamValue("EAT_FEE"));//餐补
			String PERSON_SUBSIDE = CommonUtils.checkNull(request.getParamValue("PERSON_SUBSIDE"));//人员补助
			po.setPassFee(Double.parseDouble(PASS_FEE));
			po.setTrafficFee(Double.parseDouble(TRAFFIC_FEE));
			po.setQuarterFee(Double.parseDouble(QUARTER_FEE));
			po.setEatFee(Double.parseDouble(EAT_FEE));
			po.setPersonSubside(Double.parseDouble(PERSON_SUBSIDE));
		}
		dao.update(pp, po);//修改TT_AS_WR_SPEFEE 工单费用
	}
	/**
	 * 为PO设置值，并判断审核的状态为其设值， 根据 当前状态 通过+2 ，驳回-2
	 * @param tp
	 * @param auditFlag
	 */
	private void setPoPerperties(TtAsWrSpefeeAuditingPO tp, String auditFlag) {
		String type = request.getParamValue("type");//当前状态
		String o_status = request.getParamValue("o_status");//最终状态
		String remark = CommonUtils.checkNull(request.getParamValue("remark"));
		if(!o_status.equals(type)){
			if(auditFlag.equals("1")){
				if(Integer.parseInt(o_status)==11841014){
					tp.setStatus(11841014);//通过
				}else{
					if(Integer.parseInt(type)%2==0){
						int status = Integer.parseInt(type)+2;
						tp.setStatus(status);//通过
					}else{
						int status = Integer.parseInt(type)-1;
						tp.setStatus(status);//通过
					}
				}
			}else{
				if(type.equals("11841004")||type.equals("11841002")||type.equals("11841003")||type.equals("11841005")||type.equals("11841007")){//特殊情况，打回服务站
					tp.setStatus(11841001);//驳回
				}else{
					if(Integer.parseInt(type)%2==0){
						tp.setStatus(Integer.parseInt(type)+1);//驳回
					}else{
						tp.setStatus(Integer.parseInt(type)-2);//驳回
					}
				}
			}
		}
		tp.setAuditingOpinion(remark);
	}

	/**
	 * 为PO设置值，并判断审核的状态为其设值，为审核金额设值 根据 当前状态 通过+2 ，驳回-2
	 * @param po
	 * @param auditFlag
	 */
	private void setPoPerperties(TtAsWrSpefeePO po, String auditFlag){
		String type = request.getParamValue("type");//当前状态
		String o_status = request.getParamValue("o_status");//最终状态
		String auditMoney = getParam("declareSum");//审核同意金额
		String remark = CommonUtils.checkNull(request.getParamValue("remark"));
		String declareSum = CommonUtils.checkNull(request.getParamValue("totalCount"));//总费用 
		if(StringUtil.notNull(auditMoney)){
			po.setManegerDeclareSum(Double.parseDouble(auditMoney));
		}else{
			po.setManegerDeclareSum(Double.parseDouble(declareSum));
		}
		if(!"".equals(auditMoney)){
			po.setDeclareSum(Double.parseDouble(auditMoney));
		}
		if(!o_status.equals(type)){
			if(auditFlag.equals("1")){
				if(Integer.parseInt(o_status)==11841014){
					po.setStatus(11841014);//通过
				}else{
					if(Integer.parseInt(type)%2==0){
						int status = Integer.parseInt(type)+2;
						po.setStatus(status);//通过
						if(Integer.parseInt(o_status)==status){
							po.setOStatus(11841014);
						}
					}else{
						int status = Integer.parseInt(type)-1;
						po.setStatus(status);//通过
						if(Integer.parseInt(o_status)==status){
							po.setOStatus(11841014);
						}
					}
				}
			}else{
				if(type.equals("11841004")||type.equals("11841002")||type.equals("11841003")||type.equals("11841005")||type.equals("11841007")){//特殊情况，打回服务站
					if(o_status.equals("11841014")){
						po.setOStatus(Integer.parseInt(type));
					}
					po.setStatus(11841001);//驳回
				}else{
					if(Integer.parseInt(type)%2==0){
						po.setStatus(Integer.parseInt(type)+1);//驳回
					}else{
						po.setStatus(Integer.parseInt(type)-2);//驳回
					}
				}
			}
		}
		if(String.valueOf(po.getStatus()).equals("11841003") || String.valueOf(po.getStatus()).equals("11841004")){//服务经理审核意见
			po.setAuditingOpinion(remark);
		}
		if(String.valueOf(po.getStatus()).equals("11841005") || String.valueOf(po.getStatus()).equals("11841006")){//车厂审核
			po.setDirectorAuditingOpinion(remark);
		}
		if(String.valueOf(po.getStatus()).equals("11841007") || String.valueOf(po.getStatus()).equals("11841008")){//部门总监审核意见
			po.setSectionAuditingOpinion(remark);
		}
		if(String.valueOf(po.getStatus()).equals("11841009") || String.valueOf(po.getStatus()).equals("11841010")){//销售公司审核意见
			po.setManegerAuditingOpinion(remark);
		}
		if(String.valueOf(po.getStatus()).equals("11841011") || String.valueOf(po.getStatus()).equals("11841012")){//集体公司审核意见
			po.setOfficeAuditingOpinion(remark);
		}
	}
	/*
	 * 审核详细页
	 */
	public void auditSpecialExpensesInfoJC(){
		try {
			String id = request.getParamValue("aid");
			String status = request.getParamValue("status");
			String o_status = request.getParamValue("o_status");
			String fid = request.getParamValue("fid");
			String feeType = request.getParamValue("feeType");
			Map<String, Object> map = dao.getSpeciaExpensesInfo(fid);
			List<Map<String, Object>> list2 = dao.getSpeciaExpensesAudit(fid);
			String yield = ((BigDecimal)map.get("YIELD")).toString();
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			areaPO.setAreaId(Long.parseLong(yield));
			areaPO =(TmBusinessAreaPO) dao.select(areaPO).get(0);
			act.setOutData("Area",areaPO.getAreaName() );
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(fid);
			//查询tc_code 8008 区分轿微车
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List list = dao.select(code) ;
			if(list.size()>0){
				code = (TcCodePO)list.get(0);
			}
			act.setOutData("code", code) ;
			act.setOutData("fid", fid) ;
			act.setOutData("aid", id) ;
			act.setOutData("map", map);
			act.setOutData("type", status);
			act.setOutData("list", list2);
			act.setOutData("fileList", fileList);
			if(feeType.equals(Constant.FEE_TYPE_02)){
				List<Map<String, Object>> claim = dao.getFeeRoList(fid);
				act.setOutData("claim", claim);
				act.setOutData("nums",claim.size());
				act.setOutData("o_status", o_status);
				act.setForword(audit02UrlJC);
				
			}else{
				act.setOutData("o_status", o_status);
				act.setForword(audit01UrlJC);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"修改");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	//结算室审核通过前加入大区审核通过状态
	public void insertAreaAuditStatusJC(String id,String remark,String auditFlag,String feeType,String feeChannel){
		try {
			//只对市场工单添加大区审核状态
			if(Constant.FEE_TYPE1_03.toString().equals(feeChannel)){
				Date date = new Date();
				TtAsWrSpefeePO po = new TtAsWrSpefeePO();
				TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
				//结算室审核通过
				if(auditFlag.equals("1")){
					po.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_04));//大区审核通过
				}
				pp.setId(Long.parseLong(id));
				dao.update(pp, po);
				//审核记录
				TtAsWrSpefeeAuditingPO tp = new TtAsWrSpefeeAuditingPO();
				tp.setId(Long.parseLong(SequenceManager.getSequence("")));
				tp.setFeeId(pp.getId());
				tp.setAuditingDate(date);
				tp.setAuditingPerson(loginUser.getName());
				tp.setPresonDept(loginUser.getOrgId());
				if(auditFlag.equals("1")){//通过
					tp.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_04));
				}
				tp.setCreateBy(loginUser.getUserId());
				tp.setCreateDate(date);
				tp.setAuditingOpinion(remark);
				dao.insert(tp);
			}


			
//			String balanceId = request.getParamValue("balanceId");
//			Integer count = dao.getCount(balanceId);
//			TtAsWrClaimBalancePO ac = new TtAsWrClaimBalancePO();
//			TtAsWrClaimBalancePO bc = new TtAsWrClaimBalancePO();
//			ac.setId(Long.parseLong(balanceId));
//			bc.setSpecFeeAuthCount(Long.parseLong(String.valueOf(count)));
//			dao.update(ac, bc);
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 审核市场工单费用批量审核
	 */
	public void auditSpeciaExpensesBatch01JC(){
		try {
			String specialId[] = request.getParamValues("specialId");
			String director_AUDITING_OPINION = request.getParamValue("director_AUDITING_OPINION");
			for(int i = 0; i < specialId.length; i++){
				TtAsWrSpefeePO spefeePO = new TtAsWrSpefeePO();
				spefeePO.setId(Long.parseLong(specialId[i]));
				TtAsWrSpefeePO spefeePO1 = new TtAsWrSpefeePO();
				spefeePO1.setStatus(11841010);
				spefeePO1.setDirectorAuditingOpinion(director_AUDITING_OPINION);
				dao.update(spefeePO, spefeePO1);
				
				TtAsWrSpefeeAuditingPO auditingPO = new TtAsWrSpefeeAuditingPO();
				auditingPO.setFeeId(Long.parseLong(specialId[i]));
				TtAsWrSpefeeAuditingPO auditingPO1 = new TtAsWrSpefeeAuditingPO();
				auditingPO1.setStatus(11841010);
				auditingPO1.setAuditingDate(new Date());
				dao.update(auditingPO, auditingPO1);
				
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void auditSpeciaExpensesBatch02JC(){
		try {
			String specialId[] = request.getParamValues("specialId");
			String director_AUDITING_OPINION = request.getParamValue("director_AUDITING_OPINION");
			for(int i = 0; i < specialId.length; i++){
				TtAsWrSpefeePO spefeePO = new TtAsWrSpefeePO();
				spefeePO.setId(Long.parseLong(specialId[i]));
				spefeePO = (TtAsWrSpefeePO)dao.select(spefeePO).get(0);
				TtAsWrSpefeePO spefeePO2 = new TtAsWrSpefeePO();
				spefeePO2.setId(Long.parseLong(specialId[i]));
				TtAsWrSpefeePO spefeePO1 = new TtAsWrSpefeePO();
				spefeePO1.setStatus(spefeePO.getOStatus());
				spefeePO1.setDirectorAuditingOpinion(director_AUDITING_OPINION);
				dao.update(spefeePO2, spefeePO1);
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核操作");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	
	/*
	 * 逐条审核详细页
	 */
	public void auditSpecialExpensesInfoByOneJC(){
		try {
			String feeNo = request.getParamValue("feeNo");
			String yieldly = request.getParamValue("yieldly");
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String feeType = request.getParamValue("feeType");
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			//审核条数记录
			String count = request.getParamValue("count")==null?"0":request.getParamValue("count");
			if(!dealerCode.equals("")){//截串加单引号
				String[] supp = dealerCode.split(",");
				act.setOutData("dealerCode", dealerCode);
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}
			//取得该用户拥有的产地权限
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			SpeciaExpensesBean bean = new SpeciaExpensesBean();
			bean.setFeeNo(feeNo);
			bean.setYieldly(yieldly);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFeeType(feeType);
			bean.setDealerCode(dealerCode);
			bean.setCompanyId(String.valueOf(loginUser.getCompanyId()));
			bean.setYieldlys(yieldlys);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			List<Map<String, Object>> ps = dao.auditQuerySpeciaExpensesByOneJC(bean);
			//分页方法 end
			if(ps!=null){
				if(ps.size()>Integer.valueOf(count)){
					Map<String, Object> specialMap = ps.get(Integer.valueOf(count));
					     //向前台传的list 名称是固定的不可改必须用 ps
					
					//查询第一条特殊费用单信息
					String id = specialMap.get("ID").toString();
		//			String feeTypeGet = specialMap.get("FEE_TYPE").toString();
					Map<String, Object> map = dao.getSpeciaExpensesInfo(id);
					List<Map<String, Object>> list = dao.getSpeciaExpensesAudit(id);
					StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
					List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
					List<Map<String, Object>> claim = dao.getFeeRoList(id);
					act.setOutData("firstSpecial", specialMap);  //与单条审核区别
					act.setOutData("map", map);
					act.setOutData("list", list);
					act.setOutData("fileList", fileList);
					act.setOutData("claim", claim);
					
					//向下一条传查询条件
					
					act.setOutData("beginTime", beginTime);
					act.setOutData("endTime", endTime);
					act.setOutData("feeNo", feeNo);
					act.setOutData("yieldly", yieldly);
					act.setOutData("feeType", feeType);
					act.setOutData("count", count);
					if(specialMap.get("FEE_TYPE").toString().equals(Constant.FEE_TYPE_01)){
						act.setForword(auditByOne01Url);
					}else{
						act.setForword(auditByOne02Url);
					}
				}else{
					act.setForword(auditUrlJC);
				}
			}else{
				act.setForword(auditUrlJC);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/****************************轿车特殊费用变更 add by kevinyin 20110530*********************************************/
	
	public void maintaimHistory(){
	    try {
	    	String vin = request.getParamValue("VIN");
	    	List<Map<String,Object>> maintaimHisList = dao.maintaimHistory(vin);
	    	act.setOutData("maintaimHisList", maintaimHisList);
	    	act.setOutData("VIN", vin);
	    	act.setForword(this.MAINTAIN_HIS_URL);
	    }catch (Exception e) {
	    	BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆历史记录");
	    	act.setOutData("success", false);
	    	logger.error(loginUser, e1);
	    	act.setException(e1);
	    }
	}
	/**
	 * 特殊费用与索赔单号的关联查询 zyw 2014-9-15
	 */
	public void showClaimNo(){
		super.sendMsgByUrl(sendUrl(this.getClass(), "showClaimNo"), "特殊费用与索赔单号的关联查询");
	}
	/**
	 * 特殊费用与索赔单号的关联查询数据 zyw 2014-9-15
	 */
	public void showClaimNoData(){
		PageResult<Map<String, Object>> list=dao.showClaimNoData(request,getCurrDealerId(),getCurrPage(),Constant.PAGE_SIZE);
		act.setOutData("ps", list);
	}
	/**
	 * 特殊费用的删除
	 */
	public void deleteByIdSure(){
		int res=-1;
		try {
			String id = DaoFactory.getParam(request, "id");
			dao.delete("delete from TT_AS_WR_SPEFEE b where b.id="+id, null);
			res=1;
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}finally{
			super.setJsonSuccByres(res);
		}
	}
}
