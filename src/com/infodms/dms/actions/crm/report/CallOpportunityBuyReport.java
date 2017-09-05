package com.infodms.dms.actions.crm.report;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.report.CallOpportunityBuyDao;
import com.infodms.dms.dao.crm.report.ClueReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;



import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * @Title: 来店契机及构成情况分析表Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date:  2015-02-05
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class   CallOpportunityBuyReport{
	
	public Logger logger = Logger.getLogger(CallOpportunityBuyReport.class);
	CallOpportunityBuyDao dao  = CallOpportunityBuyDao.getInstance();
	ClueReportDao cDao  = ClueReportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
    private ResponseWrapper response = act.getResponse();
	private final String initUrl = "/jsp/crm/report/callOpportunityBuyInit.jsp";
	private final String queryUrl = "/jsp/crm/report/callOpportunitytBalance.jsp";
									 
	/**
	 * 来店契机及构成情况分析表  页面初始化
	 */
	public void doInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String today=sdf.format(new Date());
            act.setOutData("today", today);
            act.setOutData("dutyType",logonUser.getDutyType());
            String  download =null;
            String longUser=CommonUtils.getPoseRank(logonUser);
            if("60281001".equals(longUser) ||"60281002".equals(longUser)){
            	Map<String,String> map =new HashMap<String,String>();
            	map.put("dealerId", logonUser.getDealerId());
            	map.put("userId", logonUser.getUserId().toString());
        		PageResult<Map<String, Object>> ps = cDao.getDealerCode(map,1, 1);
            	if(ps.getTotalPages()> 0){
            		act.setOutData("flag", "flag");
            	}
            	download="true";
			} else if(!"10431005".equals(logonUser.getDutyType())){
				download="true";
			}
            act.setOutData("download", download);
            act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"来店契机及构成情况分析表初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 来店契机及构成情况分析表
	 */
	public void doSearch(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")) ;
            if(CommonUtils.isNullString(dealerCode)){
            	dealerCode = CommonUtils.checkNull(request.getParamValue("cdealerCode")) ;
            }
            String orgId=logonUser.getOrgId().toString();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())){
				orgId="1";
			}
			act.setOutData("orgId", orgId);
			Map<String,String> map=new HashMap<String,String>();		
			String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")) ;
		    map.put("seriesId", seriesId);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			map.put("orgId", orgId);
			map.put("dealerId", logonUser.getDealerId());
			map.put("dutyType", logonUser.getDutyType());
			
			List<Map<String,Object>> balanceList;
			String userId= logonUser.getUserId().toString();
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				userId = logonUser.getUserId().toString();
				map.put("userId", userId);
			} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
				//获取主管下属分组的所有顾问
				userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
				map.put("flag", "flag");
				map.put("userId", userId);
			}else if(CommonUtils.judgeMgrLogin(logonUser.getUserId().toString())){
				map.put("flag", "flag");
				map.put("manager", "manager");
			}else if(CommonUtils.judgeTotalMgrAllLogin(logonUser.getUserId().toString())){
				map.put("flag", "flag");
				map.put("manager", "manager");
			}
			
			if( Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())){ //10431005
			    balanceList=dao.getStatisticsDaoSelect(map);
			    act.setOutData("dutyType",logonUser.getDutyType());
			} else{
				balanceList=dao.getStatisticsDaoSelectAll(map);
				act.setOutData("startDate",startDate);
				act.setOutData("endDate",endDate);
				act.setOutData("dealerCode",dealerCode);
				act.setOutData("seriesId",seriesId);
			}
			int total =0;
			for (int i = 0; i < balanceList.size(); i++) {
				total +=Integer.parseInt(  balanceList.get(i).get("TOTAL").toString());
			}
			int a=0;
			String dutyType=logonUser.getDutyType();
			 if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType) || Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				  a=total/2;
			  }
			  if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				  a=total/3;
			  }
			  if( Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				  a=total/4;
			  }
			
			act.setOutData("total",a);
			act.setOutData("seriesList", dao.getSeriesList());
			act.setOutData("balanceList",balanceList);
            
            act.setForword(queryUrl);

		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"来店契机及构成情况分析表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 展厅接待效率分析报表dcrc经销商详情
	 */
	public void getDealerList(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealer_id = CommonUtils.checkNull(request.getParamValue("dealer_id")) ;
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")) ;
           
            String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")) ;
            String orgId=logonUser.getOrgId().toString();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())){
				orgId="1";
			}
			act.setOutData("orgId", orgId);
			Map<String,String> map=new HashMap<String,String>();		
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			map.put("orgId", orgId);
			map.put("dealerId", dealer_id); 
			map.put("dutyType", "10431005");
			map.put("seriesId", seriesId);
			map.put("flag", "flag");
			map.put("manager", "manager");
			
			List<Map<String,Object>> balanceList=dao.getStatisticsDaoSelect(map);
			    act.setOutData("dutyType","10431005");
			 
			int total =0;
			for (int i = 0; i < balanceList.size(); i++) {
				total +=Integer.parseInt(  balanceList.get(i).get("TOTAL").toString());
			}
			int a=0;
			String dutyType=logonUser.getDutyType();
			 if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType) || Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				  a=total/2;
			  }
			  if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				  a=total/3;
			  }
			  if( Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				  a=total/4;
			  }
			
			act.setOutData("total",a);
			act.setOutData("seriesList", dao.getSeriesList());
			act.setOutData("balanceList",balanceList);
            
            act.setForword(queryUrl);

		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"来店契机及构成情况分析表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void doDownload() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// 导出的文件名
			String fileName = "来店契机及构成情况分析表.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");

			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")) ;
            String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")) ;
            if(CommonUtils.isNullString(dealerCode)){
            	dealerCode = CommonUtils.checkNull(request.getParamValue("cdealerCode")) ;
            }
            Map<String, String> paraMap = new HashMap<String, String>();
            
            paraMap.put("startDate", startDate);
            paraMap.put("endDate", endDate);
            paraMap.put("dealerCode", dealerCode);
            paraMap.put("dutyType", logonUser.getDutyType());
            paraMap.put("orgId", logonUser.getOrgId().toString());
            paraMap.put("dealerId", logonUser.getDealerId());
            paraMap.put("dutyType", logonUser.getDutyType());
            paraMap.put("seriesId", seriesId);
			String userId=logonUser.getUserId().toString();
          //判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				userId = logonUser.getUserId().toString();
				paraMap.put("userId", userId);
			} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
				//获取主管下属分组的所有顾问
				userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
				paraMap.put("userId", userId);
				paraMap.put("flag", "flag");
			}else if(CommonUtils.judgeMgrLogin(logonUser.getUserId().toString())){
				paraMap.put("flag", "flag");
				paraMap.put("manager", "manager");
			}else if(CommonUtils.judgeTotalMgrAllLogin(logonUser.getUserId().toString())){
				paraMap.put("flag", "flag");
				paraMap.put("manager", "manager");
			}
			OutputStream os = response.getOutputStream();
	    	WritableWorkbook workbook = Workbook.createWorkbook(os);
				List<Map<String, Object>> list =null; 
				if(Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())){
					list= dao.getStatisticsDaoSelect(paraMap);
					getExcelName(list,workbook);
				}else{
					list=dao.getStatisticsDaoSelectAll(paraMap);
					getExcel(list, workbook);
				}
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商线索总量效率分析报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 经理导出
	 * @param list
	 * @param workbook
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	public void getExcelName(List<Map<String, Object>> list,WritableWorkbook workbook) 
									throws IOException, RowsExceededException, WriteException{
		List<Map<String,Object>> seriesList=dao.getSeriesList();
		int total =0;
		for (int i = 0; i < list.size(); i++) {
			total +=Integer.parseInt(  list.get(i).get("TOTAL").toString());
		}
		int all=total/2;
		WritableSheet sheet = workbook.createSheet("来店契机及构成情况分析表", 0);
		WritableCellFormat wcf = new WritableCellFormat();
		wcf.setAlignment(Alignment.CENTRE);
		int y = 0;
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
		DisplayFormat displayFormat = NumberFormats.TEXT;
		WritableCellFormat format = new WritableCellFormat(wf,displayFormat);
		format.setAlignment(jxl.format.Alignment.LEFT);
		format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.NONE); 
		
		sheet.addCell(new Label(0, y, "大区"));
		sheet.addCell(new Label(1, y, "省份"));
		sheet.addCell(new Label(2, y, "代码"));
		sheet.addCell(new Label(3, y, "简称"));
		sheet.addCell(new Label(4, y, "来电契机"));
		for (int j = 0; j < 2*seriesList.size()-1 ; j+=2) {
			sheet.addCell(new Label(5+j, y, seriesList.get(j/2).get("SERIES_NAME").toString()));
			sheet.addCell(new Label(6+j, y, "占比"));
		}
		sheet.addCell(new Label(2*seriesList.size()+4 , y, "合计"));
		sheet.addCell(new Label(2*seriesList.size()+5, y, "占比"));
		if (list != null) {
		
			for (int i = 0; i < list.size(); i++) {
				++y;
				sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, CommonUtils.checkNull(list.get(i).get("PQ_ORG_NAME"))));
				sheet.addCell(new Label(2, y, list.get(i).get("DEALER_CODE").toString()));
				sheet.addCell(new Label(3, y, list.get(i).get("DEALER_SHORTNAME")==null?"":list.get(i).get("DEALER_SHORTNAME").toString()));
				sheet.addCell(new Label(4, y, list.get(i).get("CODE_DESC").toString()));
				for (int j = 0; j < 2*seriesList.size()-1; j+=2) {
					sheet.addCell(new Number(5+j, y, Double.parseDouble(list.get(i).get("COUNT"+seriesList.get(j/2).get("SERIES_ID")).toString()),new WritableCellFormat(wf,displayFormat)));
					sheet.addCell(new Label(6+j, y, list.get(i).get("RATE"+seriesList.get(j/2).get("SERIES_ID")).toString())) ;
				}
				sheet.addCell(new Number(2*seriesList.size()+4, y, Double.parseDouble(list.get(i).get("TOTAL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(2*seriesList.size()+5, y, CommonUtils.getPercent(new Object[]{list.get(i).get("TOTAL"), all})));
			}
		}
		++y;
	}
	/**
	 * 车厂导出
	 * @param list
	 * @param workbook
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	public void getExcel(List<Map<String, Object>> list,WritableWorkbook workbook) 
									throws IOException, RowsExceededException, WriteException{
		List<Map<String,Object>> seriesList=dao.getSeriesList();
		WritableSheet sheet = workbook.createSheet("来店契机及构成情况分析表", 0);
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		WritableCellFormat wcf = new WritableCellFormat();
		wcf.setAlignment(Alignment.CENTRE);
		int y = 0;
		int all=0;
		int total =0;
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
		DisplayFormat displayFormat = NumberFormats.TEXT;
		WritableCellFormat format = new WritableCellFormat(wf,displayFormat);
		format.setAlignment(jxl.format.Alignment.LEFT);
		format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.NONE); 
		for (int i = 0; i < list.size(); i++) {
			total +=Integer.parseInt(  list.get(i).get("TOTAL").toString());
		}
		String dutyType=logonUser.getDutyType();
		 if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType) || Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 all=total/2;
		  }
		  if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			  all=total/3;
		  }
		  if( Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			  all=total/4;
		  }
		sheet.addCell(new Label(0, y, "大区"));
		sheet.addCell(new Label(1, y, "省份"));
		sheet.addCell(new Label(2, y, "代码"));
		sheet.addCell(new Label(3, y, "简称"));
		sheet.addCell(new Label(4, y, "来电契机"));
		for (int j = 0; j < 2*seriesList.size()-1; j+=2) {
			sheet.addCell(new Label(5+j, y, seriesList.get(j/2).get("SERIES_NAME").toString()));
			sheet.addCell(new Label(6+j, y, "占比"));
		}
		sheet.addCell(new Label(2*seriesList.size()+4, y, "合计"));
		sheet.addCell(new Label(2*seriesList.size()+5, y, "占比"));
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				++y;
				sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, CommonUtils.checkNull(list.get(i).get("PQ_ORG_NAME"))));
				sheet.addCell(new Label(2, y, list.get(i).get("DEALER_CODE").toString()));
				sheet.addCell(new Label(3, y, list.get(i).get("DEALER_SHORTNAME")==null?"":list.get(i).get("DEALER_SHORTNAME").toString()));
				sheet.addCell(new Label(4, y, list.get(i).get("CODE_DESC").toString()));
				for (int j = 0; j < 2*seriesList.size()-1; j+=2) {
					sheet.addCell(new Number(5+j, y, Double.parseDouble(list.get(i).get("COUNT"+seriesList.get(j/2).get("SERIES_ID")).toString()),new WritableCellFormat(wf,displayFormat)));
					sheet.addCell(new Label(6+j, y, list.get(i).get("RATE"+seriesList.get(j/2).get("SERIES_ID")).toString())) ;
					
				}
				sheet.addCell(new Number(2*seriesList.size()+4, y, Double.parseDouble(list.get(i).get("TOTAL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(2*seriesList.size()+5, y, CommonUtils.getPercent(new Object[]{list.get(i).get("TOTAL"), all})));
			}
		}
		++y;
	}
	

}
