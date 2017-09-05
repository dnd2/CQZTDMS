package com.infodms.dms.actions.customerRelationships.reportformsManage;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.customerRelationships.baseSetting.seatsSet;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.customerRelationships.ComplaintTypeReportformsDao;
import com.infodms.dms.dao.customerRelationships.IncomingCallDetailReportDao;
import com.infodms.dms.dao.productmanage.ProductManageDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.VwMaterialGroupPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 来电明细统计
 * @author wangming
 *
 */
public class IncomingCallDetailReportforms {
	private static Logger logger = Logger.getLogger(seatsSet.class);
	//来电明细统计初始化页面
	private static final AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();
	private final String incomingCallDetailUrl = "/jsp/customerRelationships/reportformsManage/incomingCallDetailReportforms.jsp";
	private final String incomingCallChanelUrl = "/jsp/customerRelationships/reportformsManage/incomingCallChanelReportforms.jsp";
	private final String incomingCallDayReportUrl = "/jsp/customerRelationships/reportformsManage/incomingCallDayReportforms.jsp";
	private final String VEHICLE_INFO_REPORT_URL = "/jsp/customerRelationships/reportformsManage/VehicleInfoReport.jsp";
	private final String DITCH_REPORT_URL = "/jsp/customerRelationships/reportformsManage/ditchReport.jsp";
	private final String VEHICLE_QUERY_REPORT_URL = "/jsp/customerRelationships/reportformsManage/vehicleQueryReport.jsp";
	private final String OUT_WAREHOUSE_REPORT_URL = "/jsp/customerRelationships/reportformsManage/outWarehouseReport.jsp";
	private final String LARGE_AREA_REPORT_URL = "/jsp/customerRelationships/reportformsManage/largeAreaReport.jsp";
	private final String VEHICLE_TYPE_REPORT_URL = "/jsp/customerRelationships/reportformsManage/vehicleTypeReport.jsp";
	private final String DEALER_REPORT_URL = "/jsp/customerRelationships/reportformsManage/dealerReport.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	public void vehicleInfoReportInit(){
		try{
			act.setForword(VEHICLE_INFO_REPORT_URL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"全国各经销商商品车统计初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void complaintTypeReportformsInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//抱怨对象
			act.setOutData("cpObjectList", commonUtilActions.getTmOrgPOAndTop());
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//报表类型
			act.setOutData("reportTypeList", getReportType());
			//车型
			act.setOutData("modelList", commonUtilActions.getAllModel());
			
			act.setForword(incomingCallDetailUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"来电明细统计初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 来电明细查询 
	 */
	public void queryIncomingCallDetails(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  				
			
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> IncomingReportformsData = IncomingDao.queryIncomingCallDetailReportforms(dealName,dateStart,dateEnd,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", IncomingReportformsData);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"来电统计查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 来电明细导出
	 */
	public void incomingCallDetailsExcelQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  				
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			List<Map<String, Object>> list = IncomingDao
					.incomingCallDetailQueryExport(dealName,dateStart,dateEnd);
			String[] head = { "日期", "方式", "姓名", "地址", "来电号码","联系方式 ","了解渠道","车架号","行驶里程(KM)","购车商家","来电内容","受理人反馈信息",
					"处理状态","关注车型","来电业务类型","来电客户类型","受理人","处理来电部门","大区联系人","商家联系人 ","最新跟进情况 ","最近跟踪人 ","最近跟踪时间 " };
			String[] cols = { "CREATE_DATE", "INCOMING_TYPE", "CP_NAME",
					"CP_PROXY_AREA", "CP_PHONE", "CP_CONTACT","CP_CUSTOMER_KNOWN_CHANEL","CP_VIN","CP_MILEAGE","CP_DEAL_DEALER",
					"CP_CONTENT","CP_SEAT_COMMENT","CP_STATUS","CP_MODEL_ID","CP_BIZ_TYPE","CP_CUSTOMER_TYPE","CREATE_BY","CP_DEAL_ORG","CP_ORG_RESPONSER",
					"CP_DEALER_RESPONSER","CP_UPDATE_CONTENT","UPDATE_BY","UPDATE_DATE"};// 导出的字段名称
			ToExcel.toReportExcel(act.getResponse(), request,
					"来电明细导出.xls", head, cols, list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "来电明细导出查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void chanelReportformsInit(){
		try{
			act.setForword(incomingCallChanelUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"来电明细统计初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryIncomingCallChanelReport(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  				
			
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> IncomingReportformsData = IncomingDao.queryIncomingCallChanelReportforms(dealName,dateStart,dateEnd,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", IncomingReportformsData);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"了解渠道统计查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 来电渠道导出
	 */
	public void incomingCallChanelExcelQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  				
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			List<Map<String, Object>> list = IncomingDao
					.incomingCallChanelQueryExport(dealName,dateStart,dateEnd);
			String[] head = {"了解渠道统计", "投诉","央视", "广播", "北汽员工","朋友介绍 ","车展","数字营销","实体店","汽车之家","凤凰网",
					"官网","易车网","汽车网","搜狐","新浪","腾讯","发布会 ","微信 ","太平洋 ","网上超市 ","网易 ","爱卡","车友网","其它站点" };
			String[] cols = { "INCOMINGTYPE", "CHANEL0","CHANEL1", "CHANEL2",
					"CHANEL3", "CHANEL4", "CHANEL5","CHANEL6","CHANEL7","CHANEL8","CHANEL9",
					"CHANEL10","CHANEL11","CHANEL12","CHANEL13","CHANEL14","CHANEL15","CHANEL16",
					"CHANEL17","CHANEL18","CHANEL19","CHANEL20","CHANEL21","CHANEL22","CHANEL23"};// 导出的字段名称
			ToExcel.toChanelReportExcel(act.getResponse(), request,
					"潜客来电渠道统计.xls", head, cols, list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "潜客来电渠道统计");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryComplaintTypeReportforms(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  
			String tmorg = CommonUtils.checkNull(request.getParamValue("tmorg"));  				
			String tmOrgSmall = CommonUtils.checkNull(request.getParamValue("tmOrgSmall"));  
			String reportType = CommonUtils.checkNull(request.getParamValue("reportType"));
			String model = CommonUtils.checkNull(request.getParamValue("model"));
			
			
			ComplaintTypeReportformsDao dao = ComplaintTypeReportformsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			int tatal = dao.getTotalComplaintTypeReportforms(dealName, dateStart,dateEnd,tmorg,tmOrgSmall,model);
			if(tatal  == 0){
				act.setOutData("ps", new PageResult<Map<String,Object>>());
			}else{
				if(reportType.equals("1")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByDealOrg(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("2")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsBySeries(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("3")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByMileageRange(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("4")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByBizContent(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("5")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByLevel(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("6")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByFaultPart(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("7")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByTmOrg(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("8")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsBySmalltmorg(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("9")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByBuyDateRange(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("10")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByDealRange(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}
			}	
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"咨询类别统计查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void complaintTypeReportformsExcel(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  
			String tmorg = CommonUtils.checkNull(request.getParamValue("tmorg"));  				
			String tmOrgSmall = CommonUtils.checkNull(request.getParamValue("tmOrgSmall"));  
			String reportType = CommonUtils.checkNull(request.getParamValue("reportType"));
			String model = CommonUtils.checkNull(request.getParamValue("model"));
			
			ComplaintTypeReportformsDao dao = ComplaintTypeReportformsDao.getInstance();
			int tatal = dao.getTotalComplaintTypeReportforms(dealName, dateStart,dateEnd,tmorg,tmOrgSmall,model);
			List<Map<String,Object>> complaintTypeReportformsData = null;
			String reportTypeName = "";
			if(reportType.equals("1")){
				reportTypeName = "来源部门";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByDealOrgList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("2")){
				reportTypeName = "车种";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsBySeriesList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("3")){
				reportTypeName = "里程范围";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByMileageRangeList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("4")){
				reportTypeName = "类别";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByBizContentList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("5")){
				reportTypeName = "抱怨级别";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByLevelList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("6")){
				reportTypeName = "故障部件";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByFaultPartList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("7")){
				reportTypeName = "大区";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByTmOrgList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("8")){
				reportTypeName = "省市";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsBySmalltmorgList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("9")){
				reportTypeName = "购买期限";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByBuyDateRangeList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("10")){
				reportTypeName = "处理时长";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByDealRangeList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}
			
			
			if(tatal == 0){
				act.setForword(incomingCallDetailUrl);
			}else if(tatal>10000 && tatal>0) {
				act.setOutData("DataLonger", "数据超过10000条,最高导出10000条数据");
				act.setForword(incomingCallDetailUrl);
			}else{
				complaintTypeReportformsDataToExcel(complaintTypeReportformsData,reportTypeName);
				act.setForword(incomingCallDetailUrl);
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void complaintTypeReportformsDataToExcel(List<Map<String, Object>> list,String reportTypeName) throws Exception{
		TcCodeDao tcCodeDao = TcCodeDao.getInstance();
		String[] head=new String[3];
		head[0]=reportTypeName;
		head[1]="抱怨次数";
		head[2]="占抱怨总量的百分比（%）";
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[3];
					detail[0] = CommonUtils.checkNull(map.get("TYPENAME"));
					detail[1] = CommonUtils.checkNull(map.get("COUNTDESC"));
					detail[2] = CommonUtils.checkNull(map.get("COUNTRATE"));
					
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "抱怨分类统计.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	public List<Map<String,Object>> getReportType(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "按大区统计");
		map.put("value", 7);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按来源部门统计");
		map.put("value", 1);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按车种统计");
		map.put("value", 2);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按里程范围统计");
		map.put("value", 3);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按抱怨类型统计");
		map.put("value", 4);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按抱怨级别统计");
		map.put("value", 5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按故障部件统计");
		map.put("value", 6);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按省市统计");
		map.put("value", 8);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按购买期限统计");
		map.put("value", 9);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按处理时长统计");
		map.put("value", 10);
		list.add(map);
		
		return list;
	}
	
	public void incomingCallDayReport(){
		try{
			act.setForword(incomingCallDayReportUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉咨询汇总统计初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	/**
	 * 导出投诉咨询日报表
	 */
	public void incomingCallDayReportExcel(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  				
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			List<Map<String, Object>> list = IncomingDao
					.incomingCallDayQueryExport(dateStart,dateEnd);
			
			HashMap<String,Object> sumMap = new HashMap<String,Object>();
			HashMap<String,Object> avgMap = new HashMap<String,Object>();
			String[] head = {"日期", "潜在客户","潜在商家","已购车用户", "其他咨询", "投诉","合计 ","S2","S3","不确定","潜在用户","潜在商家","已购车用户",
					"合计","S2","S3","不确定","潜在用户","潜在商家","已购车用户","合计 ","S2 ","S3 ","不确定 ","当日数量总计 ","潜在用户","潜在商家","已购车用户","其他咨询","投诉","S2","S3","不确定" };
			String[] cols = { "CREATE_DATE", "CHANEL1","CHANEL2", "CHANEL3","CHANEL0",
					"NUM1", "SUM1", "MODEL0","MODEL1","MODEL2","CHANELX","CHANELY","CHANELW",
					"SUMW","MODELW","MODELX","MODELY","CHANELB","CHANELC","CHANELA","SUMA",
					"MODELA","MODELB","MODELC","SUMDAY","SUMQK","SUMQS","SUMCUS","SUMINNER","SUMCP","SUMS2","SUMS3","SUMS23"};// 导出的字段名称
			
			String sum_CreateDate = "总值";
			String avg_CreateDate = "平均值";
			double []sumReport = new double[32]; 
			double []avgReport = new double[32]; 
			for (int i=0;i<list.size();i++) {
				Map nextObj = (Map)list.get(i);
				for(int j=0;j<32;j++){
					if(nextObj.containsKey(cols[j+1])){
						if(nextObj.get(cols[j+1])!=null){
//							System.out.println(sumReport[j]);
							sumReport[j] += Double.valueOf(nextObj.get(cols[j+1]).toString());//保存32列数据，每行相加
						}
						avgReport[j] = sumReport[j]/list.size();
					}else{
						sumReport[j] += 0;//保存32列数据，每行相加
						avgReport[j] = sumReport[j]/list.size();
					}
				}
				
			}
			/**
			 * 开始构造汇总数据2行(sum和avg)
			 */
			sumMap.put("CREATE_DATE", sum_CreateDate);
			avgMap.put("CREATE_DATE", avg_CreateDate);
			for(int t=0;t<32;t++){
				sumMap.put(cols[t+1], sumReport[t]);
				DecimalFormat df = new DecimalFormat("#.00");
				avgReport[t] = Double.valueOf(df.format(avgReport[t]));
				avgMap.put(cols[t+1], avgReport[t]);
			}
			list.add(sumMap);
			list.add(avgMap);
			
			ToExcel.toDayReportExcel(act.getResponse(), request,
					"投诉咨询汇总统计.xls", head, cols, list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "汇总统计");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void incomingCallDayReportQuery(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  				
			
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> IncomingDayReportData = IncomingDao.queryIncomingCallDayReportforms(dealName, dateStart, dateEnd, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", IncomingDayReportData);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉咨询汇总统计查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
		
	}
	
	public void VehicleInfoReportQuery(){
		try{
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = IncomingDao.queryVehicleInfoReport(dateStart, dateEnd, dealerCode,Constant.PAGE_SIZE, curPage,logonUser);
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"车辆查询报表");
			logger.error(logger,e1);
			act.setException(e1);
		}
		
	}
	
	public void queryDitchReport(){
		try{
			String PROVINCE_ID = CommonUtils.checkNull(request.getParamValue("PROVINCE_ID"));  	
			String CITY_ID = CommonUtils.checkNull(request.getParamValue("CITY_ID"));  	
			String COUNTIES = CommonUtils.checkNull(request.getParamValue("COUNTIES"));  	
			String DLR_SERVICE_STATUS = CommonUtils.checkNull(request.getParamValue("DLR_SERVICE_STATUS"));  	
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = IncomingDao.queryDitchReport(Constant.PAGE_SIZE, curPage,logonUser,PROVINCE_ID,CITY_ID,COUNTIES,DLR_SERVICE_STATUS);
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"渠道报表查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 全国经销商商品车情况报表
	 */
	public void vehicleInfoReportExcel(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  				
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			String[] head = new String[]{"大区", "经销商代码","省份","地级市", "经销商全称", "经销商简称","累计发车(辆) ","累计在途(辆)","累计库存(辆)","累计终端(辆)","当日终端","最近六天内已发车(辆)","经销商订单(辆)" };//,"账上累计资金总额(元)","账上当前余额资金总额(元)"
			List<Map<String, Object>> list = IncomingDao
					.queryVehicleInfoReport(dateStart,dateEnd,dealCode,logonUser);
			if (CommonUtils.isEmpty(dateStart) && CommonUtils.isEmpty(dateEnd)) {
				
			}else{
				head[11] = dateStart+"至"+dateEnd+"已发车(辆)";
			}
			
			
			String[] cols = { "ROOT_ORG_NAME", "DEALER_CODE","ORG_NAME", "REGION_NAME","DEALER_NAME",
					"DEALER_SHORTNAME", "LEIJIFACHE", "LEIJIZAITU","LEIJIKUCUN","LEIJIZHONGDUAN","DANGTIANSHIXIAO","SEND_NUM","CHK_NUM"};// 导出的字段名称,"AMOUNT","YUE_AMOUNT"
			
			ToExcel.VehicleInfoReportExcel(act.getResponse(), request,
					"全国各经销商商品车统计.xls", head, cols, list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "全国经销商商品车情况报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 渠道报表
	 */
	public void ditchReportExcel(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String PROVINCE_ID = CommonUtils.checkNull(request.getParamValue("PROVINCE_ID"));  	
			String CITY_ID = CommonUtils.checkNull(request.getParamValue("CITY_ID"));  	
			String COUNTIES = CommonUtils.checkNull(request.getParamValue("COUNTIES"));
			String DLR_SERVICE_STATUS = CommonUtils.checkNull(request.getParamValue("DLR_SERVICE_STATUS"));  	
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			String[] head = new String[]{"大区", "省","市","区", "店面级别A数量", 
					"店面级别B数量","店面级别C数量","店面级别D数量","店面级别E数量","店面级别F数量",
					"经销商状态","一级商家数量","二级商家数量","空白地级市数量","空白区县数量"};
			List<Map<String, Object>> list = IncomingDao
					.queryDitchReport(PROVINCE_ID,CITY_ID,COUNTIES,DLR_SERVICE_STATUS);
			String[] cols = { "ROOT_ORG_NAME", "ORG_NAME","CITY_NAME", "REGION_NAME","A_TAB",
					"B_TAB", "C_TAB", "D_TAB","E_TAB","F_TAB",
					"SERVICE_STATUS","YJJXS","EJJXS","KBDJS_TAB","KBQX_TAB"};
			ToExcel.VehicleInfoReportExcel(act.getResponse(), request,
					"渠道报表.xls", head, cols, list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "渠道报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 车辆查询报表
	 */
	public void vehicleQueryReportExcel(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			String[] head = new String[]{"底盘号", "状态","发动机号","大区", "省份", 
					"经销商代码","经销商简称","品牌","车系","车型",
					"配置","颜色","选配","结算价","生产日期",
					"入库日期","合格证日期","合格证号","库位","订单号",
					"发运日期","物流公司","经销商验收日期","实销日期","所在仓库"};
			List<Map<String, Object>> list = IncomingDao
					.vehicleQueryReport(request);
			String[] cols = { "VIN", "LIFE_CYCLE","ENGINE_NO", "ROOT_ORG_NAME","ORG_NAME",
					"DEALER_CODE", "DEALER_SHORTNAME", "PINPAI","CHEXI","CHEXING",
					"PEIZHI","COLOR","XP_NAME","VHCL_PRICE","PRODUCT_DATE",
					"STORAGE_DATE","HGZ_PRINT_DATE","HGZ_NO","SIT_CODE","ORDER_NO",
					"SEND_DATE","LOGI_NAME","ORG_STORAGE_DATE","SALES_DATE","WAREHOUSE_NAME",};
			ToExcel.VehicleInfoReportExcel(act.getResponse(), request,
					"车辆查询报表.xls", head, cols, list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆查询报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void ditchReportInit(){
		try{
			
			List<Object> params = new ArrayList<Object>();
			StringBuffer sbSql = new StringBuffer();
			
			sbSql.append("     select  a.COUNTIES,\n");
			sbSql.append("       a.PROVINCE_ID,\n");
			sbSql.append("       a.CITY_ID\n");
			sbSql.append("  from tm_dealer a"); 
			
			ProductManageDao dao = ProductManageDao.getInstance();
			
			Map<String, Object> dealerData = dao.pageQueryMap(sbSql.toString(), params, dao.getFunName());
			
			act.setOutData("dMap", dealerData);
			act.setForword(DITCH_REPORT_URL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"渠道报表初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void vehicleQueryReportInit(){
		try{
			List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
			act.setOutData("orglist", orgList);
			act.setForword(VEHICLE_QUERY_REPORT_URL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"车辆查询报表初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void vehicleQueryReport(){
		try{
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = IncomingDao.vehicleQueryReport(Constant.PAGE_SIZE, curPage,logonUser,request);
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"车辆查询报表");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void outWarehouseReport(){
		try{
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = IncomingDao.outWarehouseReport(Constant.PAGE_SIZE, curPage,logonUser,request);
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"出库流水账报表查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void outWarehouseReportInit(){
		try{
			List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
			act.setOutData("orglist", orgList);
            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getDate());
			act.setForword(OUT_WAREHOUSE_REPORT_URL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"出库流水账报表初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 出库流水报表
	 */
	public void outWarehouseReportExcel(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			String[] head = new String[]{"大区", "省份","经销商代码","经销商简称", "订单号", 
					"运单号","订车日期","审核日期","分派日期","配车日期","发运日期","验收日期","品牌",
					"车系","车型","配置","颜色","VIN码","发动机号","承运物流商"};
			List<Map<String, Object>> list = IncomingDao.outWarehouseReport(request);
			String[] cols = { "ROOT_ORG_NAME", "ORG_NAME","DEALER_CODE", "DEALER_NAME","ORDER_NO",
					"BILL_NO", "RAISE_DATE", "PLAN_CHK_DATE","ASS_DATE","ALLOCA_DATE",
					"SEND_DATE","STORAGE_DATE","PINPAI","CHEXI","CHEXING",
					"PEIZHI","COLOR","VIN","ENGINE_NO","LOGI_NAME"};
			ToExcel.VehicleInfoReportExcel(act.getResponse(), request,
					"出库流水报表.xls", head, cols, list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "出库流水报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void largeAreaReportInit(){
		try{
			List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
			act.setOutData("orglist", orgList);
            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getDate());
			act.setForword(LARGE_AREA_REPORT_URL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"大区报表初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void largeAreaReport(){
		try{
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = IncomingDao.largeAreaReport(Constant.PAGE_SIZE, curPage,logonUser,request);
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"大区报表查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void vehicleTypeReportInit(){
		try{
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			VwMaterialGroupPO groupPO = new VwMaterialGroupPO();
			
			act.setOutData("all", ajaxDao.select(groupPO));
			act.setOutData("s2", IncomingDao.getPackageIdS2());
			act.setOutData("s3", IncomingDao.getPackageIdS3());
			act.setOutData("VehicleType", IncomingDao.getVehicleType());
            act.setOutData("now", CommonUtils.getDate());
			act.setForword(VEHICLE_TYPE_REPORT_URL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"车型日报表初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void vehicleTypeReport(){
		try{
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = IncomingDao.vehicleTypeReport(Constant.PAGE_SIZE, curPage,logonUser,request);
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"大区报表查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	public void largeAreaReportExcel(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			String[] head = new String[]{"大区", "打款家数","到车家数","S2系统验收数", "S3系统验收数", 
					"合计","当日验收","S2在途数","S3在途数","合计","S2系统发运数","S3系统发运数","合计",
					"当日发运","S2实销数","S3实销数","合计","当日实销","S2库存数","S3库存数","合计"};
			List<Map<String, Object>> list = IncomingDao.largeAreaReport(request);
			String[] cols = { "ORG_NAME", "DKJS","DCJS", "YS_S2","YS_S3",
					"INSPECTION_ALL", "INSPECTION_TODAY", "ZT_S2","ZT_S3","ZTZS",
					"FY_S2","FY_S3","FYS_ALL","FYS_TODAY","SX_S2",
					"SX_S3","SXSJ_ALL","SXSJ_TODAY","KC_S2","KC_S3","KC_ALL"};
			ToExcel.VehicleInfoReportExcel(act.getResponse(), request,
					"大区报表.xls", head, cols, list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "大区报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void vehicleTypeReportExcel(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			String[] head = new String[]{"车型", "配置","生产入库(今日)","生产入库(月累计)", "生产入库(年累计)", 
					"已分配(今日)","已分配(月累计)","已分配(年累计)","厂家当日库存(可用)","厂家当日库存(冻结)","厂家当日库存(合计)","经销商销售(今日)","经销商销售(月累计)",
					"经销商销售(年累计)","经销商库存(在店+在途)","已分配未发运"};
			List<Map<String, Object>> list = IncomingDao.vehicleTypeReport(request);
			String[] cols = { 
					"MODEL_NAME", "PACKAGE_NAME", "DRRK","DYRK","DNRK",
					"DRFP","DYFP","DNFP","DRKY","DRDJ",
					"DRKCHJ","DRXS","DYXS","DNXS","JXSKC","FPWFY"};
			ToExcel.VehicleInfoReportExcel(act.getResponse(), request,
					"车型日报表.xls", head, cols, list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车型日报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dealerReportInit(){
		try{
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			VwMaterialGroupPO groupPO = new VwMaterialGroupPO();
			
			act.setOutData("all", ajaxDao.select(groupPO));
			act.setOutData("s2", IncomingDao.getPackageIdS2());
			act.setOutData("s3", IncomingDao.getPackageIdS3());
			act.setOutData("VehicleType", IncomingDao.getVehicleType());
            act.setOutData("now", CommonUtils.getDate());
			act.setForword(DEALER_REPORT_URL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"经销商日报表初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void dealerReport(){
		try{
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = IncomingDao.dealerReport(Constant.PAGE_SIZE, curPage,logonUser,request);
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"经销商日报表查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void dealerReportExcel(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			IncomingCallDetailReportDao IncomingDao = IncomingCallDetailReportDao.getInstance();
			String[] head = new String[]{"大区", "省份","经销商代码","经销商简称", "形象等级", 
					"地级市","当日提车(S2)","当日提车(S3)","小计","当日实销(S2)","当日实销(S3)","小计","账户可用余额",
					"当月提车(S2)","当月提车(S3)","小计","当月实销(S2)","当月实销(S3)","小计","月任务","月提车",
					"完成率(%)","年任务","年提车","完成率(%)","经销商库存","本月实销(S2)","上月实销(S2)","环比(%)",
					"经销商库存","本月实销(S3)","上月实销(S3)","环比(%)","月累计回款(万)","年累计回款(万)","年度提车(S2)","年度提车(S3)",
					"提车总计","年度实销(S2)","年度实销(S3)","本年实销总计","上年实销总计","环比(%)"};
			List<Map<String, Object>> list = IncomingDao.dealerReport(request);
			String[] cols = { "ROOT_ORG_NAME", "ORG_NAME","DEALER_CODE", "DEALER_SHORTNAME","IMAGE_COMFIRM_LEVEL",
					"CITY_NAME", "S2DRTC", "S3DRTC","DRTCHJ","S2DRSX",
					"S3DRSX","DRSXHJ","KYYE","S2DYTC","S3DYTC",
					"DYTCHJ","S2DYSX","S3DYSX","DYSXHJ","YDRW","DYTCHJ",
					"YDWCL","NDRW","DNTCHJ","NDWCL","JXSKC","S2DYSX",
					"S2SYSX","SYS2SXHB","JXSKC","S3DYSX","S3SYSX","SYS3SXHB",
					"DYHK","DNHK","S2DNTC","S3DNTC","DNTCHJ","S2DNSX",
					"S3DNSX","DNSXHJ","SNSX","SNSXHB"
					
			};
			ToExcel.VehicleInfoReportExcel(act.getResponse(), request,
					"经销商日报表.xls", head, cols, list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商日报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
