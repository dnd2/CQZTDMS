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
import com.infodms.dms.dao.report.serviceReport.OnceQualifiedReportFormsDao;
import com.infodms.dms.dao.report.serviceReport.QualityInfoReportFormsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
@SuppressWarnings("unchecked")
public class OnceQualifiedReportForms {

	private Logger logger = Logger.getLogger(DynamicReportMng.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER); ;
	
	//uRL
	private final String onceQualifiedReportFormsInit = "/jsp/report/service/onceQualifiedReportForms.jsp" ;//主查询页面
	
	public void onceQualifiedReportFormsInit(){
		try {
			act.setOutData("typeList", typeInit());
			act.setForword(onceQualifiedReportFormsInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算一次合格率打分主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryOnceQualifiedReportForms(){
		try{
			String typeH = CommonUtils.checkNull(request.getParamValue("typeH"));   
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));   
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));   
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));  
			
			if(typeH!=null&& !"".equals(typeH)) typeH = typeH.replaceAll(",", "','");
			
			OnceQualifiedReportFormsDao dao = OnceQualifiedReportFormsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> onceQualifiedReportFormsData = dao.queryOnceQualifiedReportForms(typeH,dateStart,dateEnd,
						dealerCode,dealerName,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", onceQualifiedReportFormsData);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"结算一次合格率打分查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	public void onceQualifiedReportFormsExcel(){
		try{
			String typeH = CommonUtils.checkNull(request.getParamValue("typeH"));   
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));   
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));   
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));  
			
			if(typeH!=null&& !"".equals(typeH)) typeH = typeH.replaceAll(",", "','");
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			OnceQualifiedReportFormsDao dao = OnceQualifiedReportFormsDao.getInstance();
			
			PageResult<Map<String,Object>> qualityInfoReportformsData = dao.queryOnceQualifiedReportForms(typeH,dateStart,dateEnd,
					dealerCode,dealerName,20000,curPage);

			onceQualifiedReportFormsDataToExcel(qualityInfoReportformsData.getRecords());
		
			onceQualifiedReportFormsInit();
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void onceQualifiedReportFormsDataToExcel(List<Map<String, Object>> list) throws Exception{
		String[] head=new String[7];
		head[0]="单据类型";
		head[1]="服务站代码";
		head[2]="服务站全称";
		head[3]="不合格数量";
		head[4]="合格数量";
		head[5]="总数量";
		head[6]="合格率";
		
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[] detail=new String[7];
					detail[0] = CommonUtils.checkNull(map.get("CLAIM_TYPE"));
					detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
					detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
					detail[3] = CommonUtils.checkNull(map.get("N_TOTAL"));
					detail[4] = CommonUtils.checkNull(map.get("Y_TOTAL"));
					detail[5] = CommonUtils.checkNull(map.get("A_TOTAL"));
					detail[6] = CommonUtils.checkNull(map.get("Y_PERCENT"));
					
					list1.add(detail);
		    	}
		    }
			String name = "结算一次合格率打分报表.xls";
			String sheetName ="结算一次合格率打分报表";
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
	private List<Map<String,Object>> typeInit(){
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
}
