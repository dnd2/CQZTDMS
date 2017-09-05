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
import com.infodms.dms.dao.report.serviceReport.QualityInfoReportFormsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
@SuppressWarnings("unchecked")
public class QualityInfoReportForms {

	private Logger logger = Logger.getLogger(DynamicReportMng.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER); ;
	
	//uRL
	private final String qualityInfoReportFormsInit = "/jsp/report/service/qualityInfoReportForms.jsp" ;//主查询页面
	
	public void qualityInfoReportFormsInit(){
		try {
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//重要度
			act.setOutData("importantLevelList", importantLevelInit());
			//车种
			act.setOutData("seriesList", commonUtilActions.getAllSeries());
			//大区
			act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			act.setForword(qualityInfoReportFormsInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量申报信报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryQualityInfoReportForms(){
		try{
			String verifyBy = CommonUtils.checkNull(request.getParamValue("verifyBy"));   
			String AdateStart = CommonUtils.checkNull(request.getParamValue("AdateStart"));   
			String AdateEnd = CommonUtils.checkNull(request.getParamValue("AdateEnd"));  
			String VdateStart = CommonUtils.checkNull(request.getParamValue("VdateStart"));   
			String VdateEnd = CommonUtils.checkNull(request.getParamValue("VdateEnd"));  
			String series = CommonUtils.checkNull(request.getParamValue("series"));   
			String model = CommonUtils.checkNull(request.getParamValue("model"));  
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));   
			String tmorg = CommonUtils.checkNull(request.getParamValue("tmorg"));  
			String tmOrgSmall = CommonUtils.checkNull(request.getParamValue("tmOrgSmall"));   
			String dealersH = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));  
			String importantLevelsH = CommonUtils.checkNull(request.getParamValue("importantLevelsH")); 
			
			if(dealersH!=null&& !"".equals(dealersH)) dealersH = dealersH.replaceAll(",", "','");
			if(importantLevelsH!=null&& !"".equals(importantLevelsH)) importantLevelsH = importantLevelsH.replaceAll(",", "','");
			
			QualityInfoReportFormsDao dao = QualityInfoReportFormsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> complaintCloseReportformsData = dao.queryQualityInfoReportForms(verifyBy,AdateStart,AdateEnd,
						VdateStart,VdateEnd,series,model,dealerName,tmorg,tmOrgSmall,dealersH,importantLevelsH,
						Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", complaintCloseReportformsData);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"质量申报信报表查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	public void qualityInfoReportFormsExcel(){
		try{
			String verifyBy = CommonUtils.checkNull(request.getParamValue("verifyBy"));   
			String AdateStart = CommonUtils.checkNull(request.getParamValue("AdateStart"));   
			String AdateEnd = CommonUtils.checkNull(request.getParamValue("AdateEnd"));  
			String VdateStart = CommonUtils.checkNull(request.getParamValue("VdateStart"));   
			String VdateEnd = CommonUtils.checkNull(request.getParamValue("VdateEnd"));  
			String series = CommonUtils.checkNull(request.getParamValue("series"));   
			String model = CommonUtils.checkNull(request.getParamValue("model"));  
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));   
			String tmorg = CommonUtils.checkNull(request.getParamValue("tmorg"));  
			String tmOrgSmall = CommonUtils.checkNull(request.getParamValue("tmOrgSmall"));   
			String dealersH = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));  
			String importantLevelsH = CommonUtils.checkNull(request.getParamValue("importantLevelsH")); 
			
			if(dealersH!=null&& !"".equals(dealersH)) dealersH = dealersH.replaceAll(",", "','");
			if(importantLevelsH!=null&& !"".equals(importantLevelsH)) importantLevelsH = importantLevelsH.replaceAll(",", "','");
			
			
			QualityInfoReportFormsDao dao = QualityInfoReportFormsDao.getInstance();
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> qualityInfoReportformsData = dao.queryQualityInfoReportForms(verifyBy,AdateStart,AdateEnd,
						VdateStart,VdateEnd,series,model,dealerName,tmorg,tmOrgSmall,dealersH,importantLevelsH,
						Constant.PAGE_SIZE,curPage);
			
			qualityInfoReportFormsDataToExcel(qualityInfoReportformsData.getRecords());
		
			qualityInfoReportFormsInit();
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void qualityInfoReportFormsDataToExcel(List<Map<String, Object>> list) throws Exception{
		String[] head=new String[31];
		head[0]="重要度";
		head[1]="车系名称";
		head[2]="车型";
		head[3]="大区";
		head[4]="小区";
		head[5]="经销商代码";
		head[6]="经销商全称";
		head[7]="故障名称";
		head[8]="条件及现象";
		head[9]="检查结论";
		head[10]="处理方法";
		head[11]="配件代码";
		head[12]="配件名称";
		head[13]="制造商代码";
		head[14]="制造商名称";
		head[15]="补充说明";
		head[16]="上报日期";
		head[17]="销售日期";
		head[18]="故障日期";
		head[19]="VIN代码";
		head[20]="发动机号";
		head[21]="用途";
		head[22]="道路状况";
		head[23]="温度时间";
		head[24]="发生时机速度";
		head[25]="雨水状况";
		head[26]="空调状态";
		head[27]="平时使用状况";
		head[28]="审核状态";
		head[29]="审核时间";
		head[30]="审核人";
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[] detail=new String[31];
					detail[0] = CommonUtils.checkNull(map.get("IMPORTANTLEVEL"));
					detail[1] = CommonUtils.checkNull(map.get("SERIESNAME"));
					detail[2] = CommonUtils.checkNull(map.get("MODELNAME"));
					detail[3] = CommonUtils.checkNull(map.get("ROOTORGNAME"));
					detail[4] = CommonUtils.checkNull(map.get("ORGNAME"));
					detail[5] = CommonUtils.checkNull(map.get("DEALERCODE"));
					detail[6] = CommonUtils.checkNull(map.get("DEALERNAME"));
					detail[7] = CommonUtils.checkNull(map.get("FAULTNAME"));
					detail[8] = CommonUtils.checkNull(map.get("CONDITION"));
					detail[9] = CommonUtils.checkNull(map.get("CHECKRESULT"));
					detail[10] = CommonUtils.checkNull(map.get("CONTENT"));
					detail[11] = CommonUtils.checkNull(map.get("PARTOLDCODE"));
					detail[12] = CommonUtils.checkNull(map.get("PARTCNAME"));
					detail[13] = CommonUtils.checkNull(map.get("MAKERCODE"));
					detail[14] = CommonUtils.checkNull(map.get("MAKERNAME"));
					detail[15] = CommonUtils.checkNull(map.get("REMARK"));
					detail[16] = CommonUtils.checkNull(map.get("APPLYADATE"));
					detail[17] = CommonUtils.checkNull(map.get("PURCHASEDDATE"));
					detail[18] = CommonUtils.checkNull(map.get("FAULTDATE"));
					detail[19] = CommonUtils.checkNull(map.get("VIN"));
					detail[20] = CommonUtils.checkNull(map.get("ENGINENO"));
					detail[21] = CommonUtils.checkNull(map.get("PURPOSE"));
					detail[22] = CommonUtils.checkNull(map.get("ROAD"));
					detail[23] = CommonUtils.checkNull(map.get("TEMP"));
					detail[24] = CommonUtils.checkNull(map.get("HAPPENTIMESPEED"));
					detail[25] = CommonUtils.checkNull(map.get("RAIN"));
					detail[26] = CommonUtils.checkNull(map.get("AIRSTATUS"));
					detail[27] = CommonUtils.checkNull(map.get("USED"));
					detail[28] = CommonUtils.checkNull(map.get("VERIFYSTAUTS"));
					detail[29] = CommonUtils.checkNull(map.get("VERIFYDATE"));
					detail[30] = CommonUtils.checkNull(map.get("VERIFYNAME"));
					
					list1.add(detail);
		    	}
		    }
			String name = "市场质量信息查询报表.xls";
			String sheetName ="市场质量信息查询报表";
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
	 * 重要度判定初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> importantLevelInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "A");
		map.put("value", "A");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "B");
		map.put("value", "B");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "C");
		map.put("value", "C");
		list.add(map);
		
		return list;
	}
}
