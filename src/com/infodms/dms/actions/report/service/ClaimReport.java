package com.infodms.dms.actions.report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.report.reportmng.DynamicReportMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.dao.usermng.UserMngDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
@SuppressWarnings("unchecked")
public class ClaimReport {

	private Logger logger = Logger.getLogger(DynamicReportMng.class) ;
	private ActionContext act = null ;
	private RequestWrapper req = null ;
	private AclUserBean logonUser = null ;
	ClaimReportDao dao  = ClaimReportDao.getInstance();
	
	//uRL
	private final String MAIN_URL = "/jsp/report/service/ysqReportPer.jsp" ;//主查询页面
	private final String CLAIM_MAIN_URL = "/jsp/report/service/claimReportPer.jsp" ;//主查询页面
	private final String PART_MAIN_URL = "/jsp/report/service/showPartCode.jsp";// 配件选择
	private final String SUPPLY_MAIN_URL = "/jsp/report/service/showSupplier.jsp";// 供应商选择
	public void foreApplyDetail(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list = dao.getSmallOrgList("");
			act.setOutData("list", list);
			// 艾春 10.08添加 东安数据过滤
            act.setOutData("poseBusType", logonUser.getPoseBusType());
			act.setForword(MAIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("static-access")
	public void ysqReportDetailQuery(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		req = act.getRequest();
		try {
			String type = req.getParamValue("type");
			String vin = req.getParamValue("vin");
			String model_name = req.getParamValue("model_name");
			String dealer_name = req.getParamValue("dealer_name");
			String small_org = req.getParamValue("small_org");
			String part_name = req.getParamValue("part_name");
			String engine_no = req.getParamValue("engine_no");
			String bDate = req.getParamValue("bDate");
			String eDate = req.getParamValue("eDate");
			Map<String, String> map1 =new HashMap();
			map1.put("vin", vin);
			map1.put("model_name", model_name);
			map1.put("dealer_name", dealer_name);
			map1.put("small_org", small_org);
			map1.put("part_name", part_name);
			map1.put("engine_no", engine_no);
			map1.put("bDate", bDate);
			map1.put("eDate", eDate);
			map1.put("type", type);
			act.setOutData("vin", vin);
			act.setOutData("model_name", model_name);
			act.setOutData("dealer_name", dealer_name);
			act.setOutData("small_org", small_org);
			act.setOutData("part_name", part_name);
			act.setOutData("engine_no", engine_no);
			act.setOutData("bDate", bDate);
			act.setOutData("eDate", eDate);
			String name = "预授权申请明细报表.xls";
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage"))
					: 1; // 处理当前页
			List<Map<String, Object>> list = dao.getSmallOrgList("");
			act.setOutData("list", list);
			if("0".equalsIgnoreCase(type)){
				PageResult<Map<String,Object>> pr = dao.getDetailList( map1,Constant.PAGE_SIZE,curPage);
				act.setOutData("ps", pr);
			}else {
				String[] head=new String[29];
				head[0]="工单号";
				head[1]="预授权单号";
				head[2]="地区";
				head[3]="服务商简称";
				head[4]="用户姓名";
				head[5]="联系电话";
				head[6]="车型";
				head[7]="VIN";
				head[8]="发动机号";
				head[9]="生产日期";
				head[10]="购买日期";
				head[11]="单据公里数";
				head[12]="故障名称";
				head[13]="处理方法";
				head[14]="主因件";
				head[15]="上报日期";
				head[16]="技术员";
				head[17]="技术审核状态";
				head[18]="技术审核时间";
				head[19]="主任";
				head[20]="主任审核状态";
				head[21]="处长";
				head[22]="处长审核状态";
				head[23]="副总";
				head[24]="副总审核状态";
				head[25]="批准日期";
				head[26]="工时费";
				head[27]="材料费";
				head[28]="费用合计";
				PageResult<Map<String,Object>> pr = dao.getDetailList( map1,9999999,curPage);
				List<Map<String, Object>> list2 = pr.getRecords();
				
				String setList = "0,1,3,5,7,8,9,10,12,13,14,15,17,18,20,22,24,25";
				 List list1=new ArrayList();
				    if(list2!=null&&list2.size()!=0){
						for(int i=0;i<list2.size();i++){
					    	Map map =(Map)list2.get(i);
							String[]detail=new String[29];
							
							detail[0]=(String) map.get("RO_NO");
							detail[1]=(String) map.get("FO_NO");
							detail[2]=(String)(map.get("ORG_NAME"));
							detail[3]=(String) map.get("DEALER_NAME");
							detail[4]=(String) map.get("CTM_NAME");
							detail[5]=(String) map.get("MAIN_PHONE");
							detail[6]=(String) map.get("MODE_CODE");
							detail[7]=(String) map.get("VIN");
							detail[8]=(String) map.get("ENGINE_NO");
							detail[9]=(String) map.get("PRODUCT_TIME");
							detail[10]=(String)map.get("PURCHASED_TIME");
							detail[11]=String.valueOf( map.get("IN_MILEAGE"));
							detail[12]=(String) map.get("MAL_NAME");
							detail[13]=(String) map.get("REPAIR_METHOD");
							detail[14]=(String) map.get("PART_NAME");
							detail[15]=(String) map.get("APPLY_TIME");
							detail[16]=(String) map.get("DQ_NAME");
							detail[17]=(String) map.get("DQ_RESULT");
							detail[18]=(String) map.get("DQ_TIME");
							detail[19]=(String) map.get("ZR_NAME");
							detail[20]=(String) map.get("ZR_RESULT");
							detail[21]=(String) map.get("CZ_NAME");
							detail[22]=(String) map.get("CZ_RESULT");
							detail[23]=(String) map.get("FZ_NAME");
							detail[24]=(String) map.get("FZ_RESULT");
							detail[25]=(String) map.get("AUDIT_TIME");
							detail[26]=String.valueOf(map.get("LABOUR_AMOUNT")) ;
							detail[27]=String.valueOf(map.get("REPAIR_PART_AMOUNT")) ;
							detail[28]=String.valueOf(map.get("ALL_MOUNT")) ;
							list1.add(detail);
					      }
						//dao.toExceVender(ActionContext.getContext().getResponse(), req, head, list1,name);
					    }
				    dao.toExceVender(ActionContext.getContext().getResponse(), req, head, list1,name,"预授权申请明细",setList);
				    act.setForword(MAIN_URL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 索赔单上报明细数据
	 * 
	 */
	public void claimApplyDetail(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list = dao.getBigOrgList();
			act.setOutData("list", list);
			// 艾春 10.08添加 东安数据过滤
            act.setOutData("poseBusType", logonUser.getPoseBusType());
			act.setForword(CLAIM_MAIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("static-access")
	public void claimApplyDetailQuery(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		req = act.getRequest();
		try {
			String type = req.getParamValue("type");
			String vin = req.getParamValue("vin");
			String model_name = req.getParamValue("model_name");
			String model_group = req.getParamValue("model_group");
			String repairType = req.getParamValue("repairType");
			String dealer_name = req.getParamValue("dealer_name");
			String small_org = req.getParamValue("small_org");
			String YIEDILY = req.getParamValue("YIEDILY");
			String engine_no = req.getParamValue("engine_no");
			String supply_code = req.getParamValue("supply_code");
			String part_code = req.getParamValue("part_code");
			String bDate = req.getParamValue("bDate");
			String eDate = req.getParamValue("eDate");
			String beDate = req.getParamValue("beDate");
			String enDate = req.getParamValue("enDate");
			String big_org = req.getParamValue("big_org");
			String report_date = req.getParamValue("report_date");
			String report_date2 = req.getParamValue("report_date2");
			String balance_date = req.getParamValue("balance_date");
			String balance_date2 = req.getParamValue("balance_date2");
			String banlanceStatus = req.getParamValue("banlanceStatus");
			String feeType = req.getParamValue("feeType");
			Map<String, String> map1 =new HashMap();
			map1.put("vin", vin);
			map1.put("report_date", report_date);
			map1.put("report_date2", report_date2);
			map1.put("balance_date2", balance_date2);
			map1.put("balance_date", balance_date);
			map1.put("banlanceStatus", banlanceStatus);
			
			map1.put("model_name", model_name);
			map1.put("model_group", model_group);
			map1.put("repairType", repairType);
			map1.put("dealer_name", dealer_name);
			map1.put("big_org", big_org);
			map1.put("small_org", small_org);
			map1.put("YIEDILY", YIEDILY);
			map1.put("engine_no", engine_no);
			map1.put("supply_code", supply_code);
			map1.put("part_code", part_code);
			map1.put("bDate", bDate);
			map1.put("eDate", eDate);
			map1.put("beDate", beDate);
			map1.put("enDate", enDate);
			map1.put("feeType", feeType);
			String name = "索赔单上报数据明细.xls";
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage"))
					: 1; // 处理当前页
					if("0".equalsIgnoreCase(type)){
						PageResult<Map<String,Object>> pr = dao.getClaimDetailList( map1,Constant.PAGE_SIZE,curPage,logonUser.getPoseBusType());
						act.setOutData("ps", pr);
					}else {
						String[] head=new String[56];
						head[0]="维修类型";
						head[1]="结算基地";
						head[2]="工单号";
						head[3]="结算单号";
						head[4]="服务站代码";
						head[5]="服务站简称";
						head[6]="服务站名称";
						head[7]="一级站代码";
						head[8]="一级站简称";
						head[9]="一级站名称";
						head[10]="昌铃形象等级";
						head[11]="昌河形象等级";
						head[12]="大区";
						head[13]="小区";
						head[14]="车系";
						head[15]="车型";
						head[16]="配置";
						head[17]="颜色";
						head[18]="车型组";
						head[19]="牌照号";
						head[20]="VIN";
						head[21]="发动机号";
						head[22]="用户名称";
						head[23]="用户电话";
						head[24]="用户地址";
						head[25]="生产日期";
						head[26]="销售日期";
						head[27]="工单开始日期";
						head[28]="工单结算日期";
						head[29]="结算单上报";
						head[30]="行驶里程";
						head[31]="处理方式";
						head[32]="故障名称";
						head[33]="故障描述";
						head[34]="故障件代码";
						head[35]="故障件件号";
						head[36]="故障件名称";
						head[37]="是否主因件";
						head[38]="更换件代码";
						head[39]="更换件名称";
						head[40]="配件单价";
						head[41]="配件数量";
						head[42]="材料费";
						head[43]="主因件工时单价";
						head[44]="主因件工时费";
						head[45]="故障件供制造商代码";
						head[46]="故障件制造商名称";
						head[47]="活动代码";
						head[48]="优惠材料系数";
						head[49]="优惠工时系数";
						head[50]="赠送费用";
						head[51]="外派费用";
						head[52]="保养费用";
						head[53]="保养材料费用";
						head[54]="保养工时费用";
						head[55]="特殊费用申请金额";
						PageResult<Map<String,Object>> pr = dao.getClaimDetailList( map1,99999,curPage,logonUser.getPoseBusType());
						List<Map<String, Object>> list2 = pr.getRecords();
						String claimNo="";
						String claimNo2="";
						String labour="";
						String mainPart="";
						String setList = "2,3,4,5,6,7,8,9,10,11,16,20,21,22,23,24,25,26,27,28,29,32,33,34,35,36,38,39,45,46,47,55";
						 List list1=new ArrayList();
						    if(list2!=null&&list2.size()!=0){
						    	claimNo =(String) list2.get(0).get("CLAIM_NO");
						    	claimNo2 =(String) list2.get(0).get("CLAIM_NO");
						    	labour =(String) list2.get(0).get("WR_LABOURCODE");
						    	mainPart ="是";
								for(int i=0;i<list2.size();i++){
							    	Map map =(Map)list2.get(i);
									String[]detail=new String[56];
									detail[0]=(String)  map.get("REPAIR_TYPE");
									detail[1]=(String)  map.get("BALANCE_YIEDILY");
									detail[2]=(String)  map.get("RO_NO");
									detail[3]=(String)  map.get("CLAIM_NO");
									detail[4]=(String)  map.get("DEALER_CODE2");
									detail[5]=(String)  map.get("DEALER_SHORTNAME2");
									detail[6]=(String)  map.get("DEALER_NAME2");
									detail[7]=(String)  map.get("DEALER_CODE");
									detail[8]=(String)  map.get("DEALER_SHORTNAME");
									detail[9]=(String)  map.get("DEALER_NAME");
									detail[10]=(String) map.get("IMAGE_LEVEL");
									detail[11]=(String) map.get("IMAGE_LEVEL2");
									detail[12]=(String) map.get("ROOT_ORG_NAME");
									detail[13]=(String) map.get("ORG_NAME");
									detail[14]=(String) map.get("SERIES_NAME");
									detail[15]=(String) map.get("MODEL_CODE");
									detail[16]=(String) map.get("PACKAGE_NAME");
									detail[17]=(String) map.get("COLOR_NAME");
									detail[18]=(String) map.get("WRGROUP_NAME");
									detail[19]=(String) map.get("LICENSE_NO");
									detail[20]=(String) map.get("VIN");
									detail[21]=(String) map.get("ENGINE_NO");
									detail[22]=(String) map.get("CTM_NAME");
									detail[23]=(String) map.get("MAIN_PHONE");
									detail[24]=(String) map.get("ADDRESS");
									detail[25]=(String) map.get("PRODUCT_TIME");
									detail[26]=(String) map.get("PURCHASED_TIME") ;
									detail[27]=(String) map.get("RO_START_TIME") ;
									detail[28]=(String) map.get("BALANCE_TIME") ;
									detail[29]=(String) map.get("REPORT_TIME");
									detail[30]=String.valueOf(map.get("IN_MILEAGE")) ;
									detail[31]=(String) map.get("PART_USE_TYPE");
									detail[32]=(String) map.get("MAL_NAME");
									detail[33]=(String) map.get("REMARK");
									detail[34]=(String) map.get("DOWN_PART_CODE");
									detail[35]=(String) map.get("PART_BOX");
									detail[36]=(String) map.get("DOWN_PART_NAME");
									detail[37]=(String) map.get("IS_MAIN_CODE");
									detail[38]=(String) map.get("PART_CODE");
									detail[39]=(String)	map.get("PART_NAME");
									detail[40]=String.valueOf(map.get("PRICE")) ;
									detail[41]=String.valueOf(map.get("QUANTITY")) ;
									detail[42]=String.valueOf(map.get("AMOUNT")) ;
									if(i==0){
										detail[43]=String.valueOf(map.get("LABOUR_PRICE")) ;
										detail[44]=String.valueOf(map.get("LABOUR_AMOUNT")) ;
									}else if((String)map.get("WR_LABOURCODE")!=null&&!"".equalsIgnoreCase((String)map.get("WR_LABOURCODE"))&&claimNo.equalsIgnoreCase((String)  map.get("CLAIM_NO"))&&labour.equalsIgnoreCase((String)map.get("WR_LABOURCODE"))&&mainPart.equalsIgnoreCase((String)map.get("IS_MAIN_CODE"))){
										detail[43]="--" ;
										detail[44]="--" ;
									}else {
										detail[43]=String.valueOf(map.get("LABOUR_PRICE")) ;
										detail[44]=String.valueOf(map.get("LABOUR_AMOUNT")) ;
										claimNo =(String) map.get("CLAIM_NO");
								    	labour =(String)  map.get("WR_LABOURCODE");
									}
									
									detail[45]=(String) map.get("DOWN_PRODUCT_CODE");
									detail[46]=(String) map.get("DOWN_PRODUCT_NAME");
									detail[47]=(String) map.get("ACTIVITY_CODE");
									
									if(i==0){
										detail[48]=String.valueOf(map.get("PART_DOWN")) ;
										detail[49]=String.valueOf(map.get("LABOUR_DOWN")) ;
										detail[50]=String.valueOf(map.get("GIVE_AMOUNT")) ;
										detail[51]=String.valueOf(map.get("OUT_ACOUNT")) ;
									}else if(claimNo2.equalsIgnoreCase((String)map.get("CLAIM_NO"))){
										detail[48]="--";
										detail[49]="--" ;
										detail[50]="--" ;
										detail[51]="--" ;
									}else {
										detail[48]=String.valueOf(map.get("PART_DOWN")) ;
										detail[49]=String.valueOf(map.get("LABOUR_DOWN")) ;
										detail[50]=String.valueOf(map.get("GIVE_AMOUNT")) ;
										detail[51]=String.valueOf(map.get("OUT_ACOUNT")) ;
										claimNo2 =(String) map.get("CLAIM_NO");
									}
									detail[52]=String.valueOf(map.get("BY_AMOUNT")) ;
									detail[53]=String.valueOf(map.get("BY_PART_AMOUNT")) ;
									detail[54]=String.valueOf(map.get("BY_LABOUR_AMOUNT")) ;
									detail[55]=String.valueOf(map.get("DECLARE_SUM1")) ;
									list1.add(detail);
							      }
								//dao.toExceVender(ActionContext.getContext().getResponse(), req, head, list1,name);
							    }
						    dao.toExceVender(ActionContext.getContext().getResponse(), req, head, list1,name,"索赔数据上报明细",setList);	
					
						    act.setForword(CLAIM_MAIN_URL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void changeSmallOrg(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			req = act.getRequest();
			String id = req.getParamValue("ID");
			List<Map<String, Object>> list = dao.getSmallOrgList(id);
			if(!Utility.testString(id)){
				list =null;
			}
			String str = dao.getStr(list);
			act.setOutData("smallList", str);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void openPartPer(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PART_MAIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void openSupply(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(SUPPLY_MAIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void queryPartCode() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		Map<String, String> map = new HashMap<String, String>();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			Integer pageSize = 15;
			String partCode = request.getParamValue("PART_CODE");
			String partName = request.getParamValue("PART_NAME"); // 主页面中的主工时代码
			map.put("partCode", partCode);
			map.put("partName", partName);
			map.put("companyId", companyId.toString());
			PageResult<Map<String,Object>> ps = dao.queryPartCode(logonUser,map, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void showSupplyCode(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Map<String, String> map = new HashMap<String, String>();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String suppCode = CommonUtils.checkNull(request.getParamValue("suppCode"));  		//供应商代码
			String suppName = CommonUtils.checkNull(request.getParamValue("suppName"));		   	//供应商名称
			map.put("suppCode", suppCode);
			map.put("suppName", suppName);
			PageResult<Map<String,Object>> ps = dao.allSuppQuery(suppCode,suppName,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
