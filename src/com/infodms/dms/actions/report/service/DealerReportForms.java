package com.infodms.dms.actions.report.service;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.report.reportmng.DynamicReportMng;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.customerRelationships.ComplaintCloseReportformsDao;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.dao.report.serviceReport.DealerReportFormsDao;
import com.infodms.dms.dao.report.serviceReport.OnceQualifiedReportFormsDao;
import com.infodms.dms.dao.report.serviceReport.QualityInfoReportFormsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
@SuppressWarnings("unchecked")
public class DealerReportForms {

	private Logger logger = Logger.getLogger(DynamicReportMng.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER); ;
	
	//uRL
	private final String dealerReportFormsInit = "/jsp/report/service/dealerReportForms.jsp" ;//主查询页面
	
	public void dealerReportFormsInit(){
		try {
			act.setOutData("typeList", typeInit());
			act.setOutData("specialInit", specialInit());
			act.setOutData("activityInit", activityInit());
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//大区
			act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			act.setForword(dealerReportFormsInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商上报材料费情况表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryDealerReportForms(){
		try{
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));   
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); 
			String tmorg = CommonUtils.checkNull(request.getParamValue("tmorg"));   
			String tmOrgSmall = CommonUtils.checkNull(request.getParamValue("tmOrgSmall"));   
			String claimType = CommonUtils.checkNull(request.getParamValue("typeH"));  			 
			String feeType = CommonUtils.checkNull(request.getParamValue("specialH"));   
			String activityType = CommonUtils.checkNull(request.getParamValue("activityH")); 
			
			String AStartDate = CommonUtils.checkNull(request.getParamValue("AStartDate"));   
			String AEndDate = CommonUtils.checkNull(request.getParamValue("AEndDate"));   
			String FStartDate = CommonUtils.checkNull(request.getParamValue("FStartDate"));  
			String FEndDate = CommonUtils.checkNull(request.getParamValue("FEndDate"));
			
			if(claimType!=null&& !"".equals(claimType)) claimType = claimType.replaceAll(",", "','");
			if(feeType!=null&& !"".equals(feeType)) feeType = feeType.replaceAll(",", "','");
			if(activityType!=null&& !"".equals(activityType)) activityType = activityType.replaceAll(",", "','");
			
			DealerReportFormsDao dao = DealerReportFormsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> dealerReportFormsData = dao.queryDealerReportForms(dealerCode, dealerName, tmorg, tmOrgSmall, claimType, feeType, activityType, AStartDate, AEndDate, FStartDate, FEndDate, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", dealerReportFormsData);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务商上报材料费情况表查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	public void dealerReportFormsExcel(){
		try{
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));   
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); 
			String tmorg = CommonUtils.checkNull(request.getParamValue("tmorg"));   
			String tmOrgSmall = CommonUtils.checkNull(request.getParamValue("tmOrgSmall"));   
			String claimType = CommonUtils.checkNull(request.getParamValue("typeH"));  	//索赔类型		 
			String feeType = CommonUtils.checkNull(request.getParamValue("specialH"));  //特殊费用类型 
			String activityType = CommonUtils.checkNull(request.getParamValue("activityH")); //活动类型
			
			String AStartDate = CommonUtils.checkNull(request.getParamValue("AStartDate"));   
			String AEndDate = CommonUtils.checkNull(request.getParamValue("AEndDate"));   
			String FStartDate = CommonUtils.checkNull(request.getParamValue("FStartDate"));  
			String FEndDate = CommonUtils.checkNull(request.getParamValue("FEndDate"));
			
			if(claimType!=null&& !"".equals(claimType)) claimType = claimType.replaceAll(",", "','");
			if(feeType!=null&& !"".equals(feeType)) feeType = feeType.replaceAll(",", "','");
			if(activityType!=null&& !"".equals(activityType)) activityType = activityType.replaceAll(",", "','");
			
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					DealerReportFormsDao dao = DealerReportFormsDao.getInstance();
			
			PageResult<Map<String,Object>> dealerReportFormsData = dao.queryDealerReportForms(dealerCode, dealerName, tmorg, tmOrgSmall, claimType, feeType, 
					activityType, AStartDate, AEndDate, FStartDate, FEndDate, 20000, curPage);

			dealerReportFormsDataToExcel(dealerReportFormsData.getRecords());
		
			dealerReportFormsInit();
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void dealerReportFormsDataToExcel(List<Map<String, Object>> list) throws Exception{
		String[] head=new String[10];
		head[0]="服务站代码";
		head[1]="服务站名称";
		head[2]="一级站代码";
		head[3]="一级站名称";
		head[4]="强保费用";
		head[5]="维修材料费";
		head[6]="强保台数";
		head[7]="定保次数";
		head[8]="结算台次";
		head[9]="维修台次";
		
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[] detail=new String[10];
					detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
					detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
					detail[2] = CommonUtils.checkNull(map.get("ROOT_DEALER_CODE"));
					detail[3] = CommonUtils.checkNull(map.get("ROOT_DEALER_NAME"));
					detail[4] = CommonUtils.checkNull(map.get("FREE_AMOUNT"));
					detail[5] = CommonUtils.checkNull(map.get("PART_AMOUNT"));
					detail[6] = CommonUtils.checkNull(map.get("FREE_TIMES"));
					detail[7] = CommonUtils.checkNull(map.get("CHECK_TIMES"));
					detail[8] = CommonUtils.checkNull(map.get("BALANCE_TIMES"));
					detail[9] = CommonUtils.checkNull(map.get("REPAIR_TIMES"));
					
					list1.add(detail);
		    	}
		    }
			String name = "服务商上报材料费情况表.xls";
			String sheetName ="服务商上报材料费情况表";
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1,name,sheetName);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list,String name,String sheetName)
			throws Exception {
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet(sheetName, 0);

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
	
	/**
	 * 索陪类型判定初始化
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> specialInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "无作业代码");
		map.put("value", Constant.FEE_TYPE_01);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "三包外的外派服务");
		map.put("value", Constant.FEE_TYPE_02);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "补偿用户");
		map.put("value", Constant.FEE_TYPE_03);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "退换车损失");
		map.put("value", Constant.FEE_TYPE_04);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "交通补贴");
		map.put("value", Constant.FEE_TYPE_05);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "质量调查");
		map.put("value", Constant.FEE_TYPE_06);
		list.add(map);

		return list;
	}
	
	/**
	 * 特殊类型判定初始化
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> typeInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "正常维修");
		map.put("value", Constant.CLA_TYPE_01);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "强保定检");
		map.put("value", Constant.CLA_TYPE_02);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "厂家活动");
		map.put("value", Constant.CLA_TYPE_06);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "售前维修");
		map.put("value", Constant.CLA_TYPE_07);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "外派服务");
		map.put("value", Constant.CLA_TYPE_09);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "特殊服务");
		map.put("value", Constant.CLA_TYPE_10);
		list.add(map);
		
		return list;
	}
	
	/**
	 * 活动类型判定初始化
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> activityInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "技术升级");
		map.put("value", Constant.SERVICEACTIVITY_TYPE_01);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "客户关怀");
		map.put("value", Constant.SERVICEACTIVITY_TYPE_02);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "俱乐部活动");
		map.put("value", Constant.SERVICEACTIVITY_TYPE_03);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "流动服务");
		map.put("value", Constant.SERVICEACTIVITY_TYPE_04);
		list.add(map);
		
		return list;
	}
}
