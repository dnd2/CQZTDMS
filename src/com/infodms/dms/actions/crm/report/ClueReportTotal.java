package com.infodms.dms.actions.crm.report;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.report.ClueReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

import jxl.write.Number;
import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: 线索来源效率分析报表Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date:  2015-02-05
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class   ClueReportTotal{
	
	public Logger logger = Logger.getLogger(ClueReportTotal.class);
	ClueReportDao dao  = ClueReportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
    private ResponseWrapper response = act.getResponse();
	private final String initUrl = "/jsp/crm/report/clueReportInit.jsp";
	private final String queryUrl = "/jsp/crm/report/clueReportBalance.jsp";
	private final String initUrlCompany = "/jsp/crm/report/clueReportCompanyTotalInit.jsp";
	private final String queryUrlCompany = "/jsp/crm/report/clueReportCompanyBalance.jsp";
									 
	/**
	 * 线索来源效率分析报表页面初始化
	 */
	public void clueInit(){
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
        		PageResult<Map<String, Object>> ps = dao.getDealerCode(map,10, 1);
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
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"线索来源效率分析报表页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 线索来源效率分析报表汇总表
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
            String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")) ;

            String orgId=logonUser.getOrgId().toString();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())){
				orgId="1";
			}
			
			String codeID="";
			List<Map<String, Object>> codeList = dao.getNameList(codeID);
			act.setOutData("orgId", orgId);
			act.setOutData("codeList", codeList);
			Map<String,String> map=new HashMap<String,String>();			
			 
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			map.put("orgId", orgId);
			map.put("dealerId", logonUser.getDealerId()); 
			map.put("dutyType", logonUser.getDutyType());
			map.put("seriesId", seriesId);
			String userId=logonUser.getUserId().toString();
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				userId = logonUser.getUserId().toString();
				map.put("userId", userId);
			} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
				//获取主管下属分组的所有顾问
				userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
				map.put("userId", userId);
				map.put("flag", "flag");
			}else if(CommonUtils.judgeMgrLogin(logonUser.getUserId().toString())){
				map.put("flag", "flag");
				map.put("manager", "manager");
			}else if(CommonUtils.judgeTotalMgrAllLogin(logonUser.getUserId().toString())){
				map.put("flag", "flag");
				map.put("manager", "manager");
			}
			 
			
			List<Map<String,Object>> balanceList=null;
			if(Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())){
				balanceList=dao.getClueReportDaoSelect(map);
				act.setOutData("dutyType",logonUser.getDutyType());
			}else{
				balanceList=dao.getClueReportDaoSelectAll(map);
				act.setOutData("startDate",startDate);
				act.setOutData("endDate",endDate);
				act.setOutData("dealerCode",dealerCode);
				act.setOutData("seriesId",seriesId);
			}
			act.setOutData("balanceList",balanceList);
            
            act.setForword(queryUrl);

		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"线索来源效率分析报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 线索来源效率分析报表dcrc经销商明细表
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
			
			String codeID="";
			List<Map<String, Object>> codeList = dao.getNameList(codeID);
			act.setOutData("orgId", orgId);
			act.setOutData("codeList", codeList);
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
			
			List<Map<String,Object>>  balanceList=dao.getClueReportDaoSelect(map);
			 act.setOutData("dutyType","10431005");
			 
			act.setOutData("balanceList",balanceList);
            
            act.setForword(queryUrl);

		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"线索来源效率分析报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void doDownload() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// 导出的文件名
			String fileName = "线索来源效率分析报表.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");

			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")) ;
            if(CommonUtils.isNullString(dealerCode)){
            	dealerCode = CommonUtils.checkNull(request.getParamValue("cdealerCode")) ;
            }
            String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")) ;
            
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
				list= dao.getClueReportDaoSelect(paraMap);
				getExcelName(list,workbook);
			}else{
				list=dao.getClueReportDaoSelectAll(paraMap);
				getExcel(list, workbook);
			}
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "线索总量效率分析报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询合作经销商多选列表
	 */
	public void getDealerCode() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);		 
			Map<String,String> map=new HashMap<String,String>();
			map.put("dealerId", logonUser.getDealerId());
			map.put("userId", logonUser.getUserId().toString());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getDealerCode(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, " 查询合作经销商多选列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询车系车型列表多选
	 */
	public void getSeriesListAll() {
		AclUserBean logonUser = null;
		act.getResponse().setContentType("application/json");
		try {
			Map<String,String> map=new HashMap<String,String>();
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"50":pageSize;
			Integer curPage = request.getParamValue("curPage2") != null ? Integer.parseInt(request.getParamValue("curPage2")): 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getSeriesListAll(map,Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, " 查询合作经销商多选列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 查询线索来源列表多选
	 */
	public void getLeadsOriginListAll() {
		AclUserBean logonUser = null;
		act.getResponse().setContentType("application/json");
		try {
			Map<String,String> map=new HashMap<String,String>();
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"30":pageSize;
			Integer curPage = request.getParamValue("curPage2") != null ? Integer.parseInt(request.getParamValue("curPage2")): 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getLeadsOriginListAll(map,Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, " 查询合作经销商多选列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	
	/**
	 * 经销商导出
	 * @param list
	 * @param workbook
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	public void getExcelName(List<Map<String, Object>> list,WritableWorkbook workbook) 
								throws IOException, RowsExceededException, WriteException{
		String codeID="";
		List<Map<String, Object>> codeList = dao.getNameList(codeID);
		WritableSheet sheet = workbook.createSheet("线索总量效率分析报表", 0);
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
		DisplayFormat displayFormat = NumberFormats.TEXT;
		WritableCellFormat format = new WritableCellFormat(wf,displayFormat);
		format.setAlignment(jxl.format.Alignment.LEFT);
		format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.NONE); 
		int y = 0;
		sheet.addCell(new Label(0, y, "大区"));
		sheet.addCell(new Label(1, y, "省份"));
		sheet.addCell(new Label(2, y, "代码"));
		sheet.addCell(new Label(3, y, "简称"));
		sheet.addCell(new Label(4, y, "销售组"));
		sheet.addCell(new Label(5, y, "销售顾问"));
		sheet.addCell(new Label(6, y, "总量"));
		sheet.addCell(new Label(7, y, "未处理"));
		sheet.addCell(new Label(8, y, "处理总量"));
		for (int j = 0; j < 2*codeList.size()-1; j+=2) {
			sheet.addCell(new Label(9+j, y, codeList.get(j/2).get("CODE_DESC").toString()));
			sheet.addCell(new Label(10+j, y, "占比"));
		}
		
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				++y;
				sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, CommonUtils.checkNull(list.get(i).get("PQ_ORG_NAME"))));
				sheet.addCell(new Label(2, y, list.get(i).get("DEALER_CODE").toString()));
				sheet.addCell(new Label(3, y, list.get(i).get("DEALER_SHORTNAME")==null?"":list.get(i).get("DEALER_SHORTNAME").toString()));
				sheet.addCell(new Label(4, y, list.get(i).get("GROUP_NAME").toString()));
				sheet.addCell(new Label(5, y, list.get(i).get("DNAME").toString()));
				sheet.addCell(new Number(6, y, Double.parseDouble(list.get(i).get("XSTOTAL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(7, y, Double.parseDouble(list.get(i).get("WCLTOTAL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(8, y, Double.parseDouble(list.get(i).get("LYTOTAL").toString()),new WritableCellFormat(wf,displayFormat)));

				for (int j = 0; j < 2*codeList.size()-1; j+=2) {  
				  sheet.addCell(new Number(9+j, y, Double.parseDouble(list.get(i).get("COUNT"+codeList.get(j/2).get("CODE_ID")).toString()),new WritableCellFormat(wf,displayFormat)));
				  sheet.addCell(new Label(10+j, y,list.get(i).get("B"+codeList.get(j/2).get("CODE_ID")).toString())); 
				}
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
	public void getExcel(List<Map<String, Object>> list, WritableWorkbook workbook) 
					throws IOException, RowsExceededException, WriteException {
		String codeID = "";
		List<Map<String, Object>> codeList = dao.getNameList(codeID);
		WritableSheet sheet = workbook.createSheet("线索总量效率分析报表", 0);
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
		sheet.addCell(new Label(4, y, "总量"));
		sheet.addCell(new Label(5, y, "未处理"));
		sheet.addCell(new Label(6, y, "处理总量"));
		for (int j = 0; j < 2*codeList.size()-1; j+=2) {
			sheet.addCell(new Label(7+j, y, codeList.get(j/2).get("CODE_DESC").toString()));
			sheet.addCell(new Label(8+j, y, "占比"));
		}
		 

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				++y;
				// sheet.mergeCells(0, y, 1, y);
				sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")== null ? "" :list.get(i).get("ROOT_ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, CommonUtils.checkNull(list.get(i) .get("PQ_ORG_NAME"))));
				sheet.addCell(new Label(2, y, list.get(i).get("DEALER_CODE")== null ? "" :list.get(i).get("DEALER_CODE") .toString()));
				sheet.addCell(new Label(3, y, list.get(i).get( "DEALER_SHORTNAME") == null ? "" : list.get(i).get("DEALER_SHORTNAME").toString()));
				sheet.addCell(new Number(4, y, Double.parseDouble(list.get(i).get("XSTOTAL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(5, y, Double.parseDouble(list.get(i).get("WCLTOTAL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(6, y, Double.parseDouble(list.get(i).get("LYTOTAL").toString()),new WritableCellFormat(wf,displayFormat)));
				for (int j = 0; j < 2*codeList.size()-1; j+=2) {
					sheet.addCell(new Number(7+j, y,Double.parseDouble(list.get(i).get("COUNT"+codeList.get(j/2).get("CODE_ID")).toString()),new WritableCellFormat(wf,displayFormat)));
					sheet.addCell(new Label(8+j, y,list.get(i).get("B"+codeList.get(j/2).get("CODE_ID")).toString())); 
				}
				 
			}
		}
		++y;
	}
	
	/**
	 * 线索总量效率分析报表(公司类)页面初始化
	 */
	public void doInitCompany(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String today=sdf.format(new Date());
            act.setOutData("today", today);
            act.setForword(initUrlCompany);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"线索总量效率分析报表页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 线索总量效率分析报表(公司类)汇总表
	 */
	public void doCompanySearch(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")) ;

            String orgId=logonUser.getOrgId().toString();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())){
				orgId="1";
			}
			 
			String codeID="";
			List<Map<String, Object>> codeList = dao.getNameCompanyList(codeID);
			act.setOutData("orgId", orgId);
			act.setOutData("codeList", codeList);
			Map<String,String> map=new HashMap<String,String>();			
			 
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			map.put("orgId", orgId);
			map.put("dealerId", logonUser.getDealerId());
			map.put("dutyType", logonUser.getDutyType());
			
			List<Map<String,Object>> balanceList=dao.getClueReportCompanySelect(map);
			act.setOutData("balanceList",balanceList);
            
            act.setForword(queryUrlCompany);

		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"全国销售车型颜色汇总表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

}
