package com.infodms.dms.actions.report.jcafterservicereport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AAABean;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.jcafterservicereport.FinancialCreditReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class FinancialCreditReport {
	private Logger logger = Logger.getLogger(FinancialCreditReport.class);
	private ActionContext act ;
	private RequestWrapper req ;
	private AclUserBean logonUser ;
	
	private FinancialCreditReportDao dao = FinancialCreditReportDao.getInstance() ;
	
	private final String MAIN_URL = "/jsp/report/jcafterservicereport/financialCreditMainUrl.jsp" ;
	
	public void financialCreditMainUrlInit(){
		act = ActionContext.getContext() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
		try{
			act.setForword(MAIN_URL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"报表主页面跳转失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void mainQuery(){
		act = ActionContext.getContext() ;
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
		try{
			String code = req.getParamValue("dealerCode");
			String name = req.getParamValue("dealerName");
			String yieldly = req.getParamValue("yieldly");
			String year = req.getParamValue("year");
			
			StringBuffer con = new StringBuffer();
			StringBuffer con1 = new StringBuffer();
			if(StringUtil.notNull(code)){
				con.append(" and A.dealer_code like '%").append(code).append("%'\n");
				con1.append(" and d.dealer_code like '%").append(code).append("%'\n");
			}
			if(StringUtil.notNull(name)){
				con.append(" and A.dealer_name like '%").append(name).append("%'\n");
				con1.append(" and d.dealer_name like '%").append(name).append("%'\n");
			}
			if(StringUtil.notNull(yieldly))
				con.append(" and A.yile = ").append(yieldly).append("\n");
			//if(StringUtil.notNull(year))
			//	con.append(" and to_char(A.end_date,'yyyy-MM-dd') like '").append(year).append("%'\n");
			
			int pageSize = 1000000 ;
			int curPage = 1 ;
			
			PageResult<Map<String,Object>> list = dao.getFinancialCreditReport(con.toString(),con1.toString(),yieldly,year, pageSize, curPage);
			
			act.setOutData("ps", list);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"报表主页面跳转失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void download(){
		act = ActionContext.getContext();
		act = ActionContext.getContext();
		req = act.getRequest();
		OutputStream os = null;
		try {
			String fileName = "财务挂账报表.csv";
			fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
			act.getResponse().setContentType("Application/text/csv");
			act.getResponse().addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			List<List<Object>> list = new LinkedList<List<Object>>();
			List<Object> titleList = genHead();
			list.add(titleList);
			
			String code = req.getParamValue("dealerCode");
			String name = req.getParamValue("dealerName");
			String yieldly = req.getParamValue("yieldly");
			String year = req.getParamValue("year");
			
			StringBuffer con = new StringBuffer();
			StringBuffer con1 = new StringBuffer();
			if(StringUtil.notNull(code)){
				con.append(" and A.dealer_code like '%").append(code).append("%'\n");
				con1.append(" and d.dealer_code like '%").append(code).append("%'\n");
			}
			if(StringUtil.notNull(name)){
				con.append(" and A.dealer_name like '%").append(name).append("%'\n");
				con1.append(" and d.dealer_name like '%").append(name).append("%'\n");
			}
				con.append(" and A.yile= ").append(yieldly).append("\n");
			//if(StringUtil.notNull(year))
			//	con.append(" and to_char(A.end_date,'yyyy-MM-dd') like '").append(year).append("%'\n");
			
			int pageSize = 1000000 ;
			int curPage = 1 ;
			
			PageResult<Map<String,Object>> ps = dao.getFinancialCreditReport(con.toString(),con1.toString(),yieldly,year, pageSize, curPage);
			
			List<Map<String, Object>> details = ps.getRecords() ;
			
			for (Map<String, Object> detail : details) {
				list.add(genBody(detail));
			}
			os = act.getResponse().getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务车导出功能 ");
			logger.error(logonUser, e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private List<Object> genHead() {
		List<Object> titleList = new LinkedList<Object>();
		titleList.add("经销商代码");
		titleList.add("经销商名称");
		titleList.add("一月结算金额");
		titleList.add("挂账时间");
		titleList.add("二月结算金额");
		titleList.add("挂账时间");
		titleList.add("三月结算金额");
		titleList.add("挂账时间");
		titleList.add("四月结算金额");
		titleList.add("挂账时间");
		titleList.add("五月结算金额");
		titleList.add("挂账时间");
		titleList.add("六月结算金额");
		titleList.add("挂账时间");
		titleList.add("七月结算金额");
		titleList.add("挂账时间");
		titleList.add("八月结算金额");
		titleList.add("挂账时间");
		titleList.add("九月结算金额");
		titleList.add("挂账时间");
		titleList.add("十月结算金额");
		titleList.add("挂账时间");
		titleList.add("十一月结算金额");
		titleList.add("挂账时间");
		titleList.add("十二月结算金额");
		titleList.add("挂账时间");
		return titleList;
	}
	private List<Object> genBody(Map<String, Object> detail) {
		List<Object> dataList = new LinkedList<Object>();
		dataList.add(CommonUtils.checkNull(detail.get("DEALER_CODE")));       
		dataList.add(CommonUtils.checkNull(detail.get("DEALER_NAME")));          
		dataList.add(CommonUtils.checkNull(detail.get("NOTE_AMOUNT_A")));   
		dataList.add(CommonUtils.checkNull(detail.get("GUAZHANG_DATE_A")));
		dataList.add(CommonUtils.checkNull(detail.get("NOTE_AMOUNT_B")));  
		dataList.add(CommonUtils.checkNull(detail.get("GUAZHANG_DATE_B")));	
		dataList.add(CommonUtils.checkNull(detail.get("NOTE_AMOUNT_C")));  
		dataList.add(CommonUtils.checkNull(detail.get("GUAZHANG_DATE_C")));         
		dataList.add(CommonUtils.checkNull(detail.get("NOTE_AMOUNT_D")));  
		dataList.add(CommonUtils.checkNull(detail.get("GUAZHANG_DATE_D")));	
		dataList.add(CommonUtils.checkNull(detail.get("NOTE_AMOUNT_E")));   
		dataList.add(CommonUtils.checkNull(detail.get("GUAZHANG_DATE_E")));        
		dataList.add(CommonUtils.checkNull(detail.get("NOTE_AMOUNT_F")));  
		dataList.add(CommonUtils.checkNull(detail.get("GUAZHANG_DATE_F")));	
		dataList.add(CommonUtils.checkNull(detail.get("NOTE_AMOUNT_G")));   
		dataList.add(CommonUtils.checkNull(detail.get("GUAZHANG_DATE_G")));        
		dataList.add(CommonUtils.checkNull(detail.get("NOTE_AMOUNT_H")));  
		dataList.add(CommonUtils.checkNull(detail.get("GUAZHANG_DATE_H")));
		dataList.add(CommonUtils.checkNull(detail.get("NOTE_AMOUNT_I")));  
		dataList.add(CommonUtils.checkNull(detail.get("GUAZHANG_DATE_I")));
		dataList.add(CommonUtils.checkNull(detail.get("NOTE_AMOUNT_J")));  
		dataList.add(CommonUtils.checkNull(detail.get("GUAZHANG_DATE_J")));
		dataList.add(CommonUtils.checkNull(detail.get("NOTE_AMOUNT_K")));  
		dataList.add(CommonUtils.checkNull(detail.get("GUAZHANG_DATE_K")));
		dataList.add(CommonUtils.checkNull(detail.get("NOTE_AMOUNT_L")));  
		dataList.add(CommonUtils.checkNull(detail.get("GUAZHANG_DATE_L")));
		return dataList;
	}
}
