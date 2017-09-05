package com.infodms.dms.actions.report.service;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;
import com.infodms.dms.actions.report.reportmng.DynamicReportMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.serviceReport.PartReportFormsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
@SuppressWarnings("unchecked")
public class PartReportForms {

	private Logger logger = Logger.getLogger(DynamicReportMng.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER); ;
	
	//uRL
	private final String partReportFormsInit = "/jsp/report/service/partReportForms.jsp" ;//主查询页面
	
	public void partReportFormsInit(){
		try {
			act.setForword(partReportFormsInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件样本申报结算情况表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryPartReportForms(){
		try{
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));   
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));   
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));  
			String partName = CommonUtils.checkNull(request.getParamValue("partName"));   
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));   
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));   
			
			if(partCode!=null&& !"".equals(partCode)) partCode = partCode.replaceAll(",", "','");
			
			PartReportFormsDao dao = PartReportFormsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> partReportFormsDate = dao.queryQualityInfoReportForms(dealerCode, dealerName, partCode, partName, yieldly,dateStart, dateEnd, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", partReportFormsDate);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"配件样本申报结算情况表查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	public void partReportFormsExcel(){
		try{
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));   
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));   
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));  
			String partName = CommonUtils.checkNull(request.getParamValue("partName"));
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));    
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));   
			
			if(partCode!=null&& !"".equals(partCode)) partCode = partCode.replaceAll(",", "','");
			
			PartReportFormsDao dao = PartReportFormsDao.getInstance();
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String,Object>> partReportFormsDate = dao.queryQualityInfoReportForms(dealerCode, dealerName, partCode, partName,yieldly, dateStart, dateEnd, 20000, curPage);

			
			partReportFormsDataToExcel(partReportFormsDate.getRecords());
			
		
			partReportFormsInit();
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void partReportFormsDataToExcel(List<Map<String, Object>> list) throws Exception{
		String[] head=new String[5];
		head[0]="服务商代码";
		head[1]="服务商名称";
		head[2]="配件代码";
		head[3]="配件名称";
		head[4]="换上件数量";
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[] detail=new String[5];
					detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
					detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
					detail[2] = CommonUtils.checkNull(map.get("PART_CODE"));
					detail[3] = CommonUtils.checkNull(map.get("PART_NAME"));
					detail[4] = CommonUtils.checkNull(map.get("QUANTITY"));
				
					list1.add(detail);
		    	}
		    }
			String name = "配件样本申报结算情况表.xls";
			String sheetName ="配件样本申报结算情况表";
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

}
