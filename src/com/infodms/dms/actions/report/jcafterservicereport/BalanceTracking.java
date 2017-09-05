package com.infodms.dms.actions.report.jcafterservicereport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.jcafterservicereport.BalanceTrackingDao;
import com.infodms.dms.dao.report.jcafterservicereport.SpecialCostReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsReportShowparamPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class BalanceTracking {
	private Logger logger = Logger.getLogger(BalanceTracking.class) ;
	private ActionContext act = null ;
	private RequestWrapper req = null ;
	private AclUserBean logonUser = null ;
	private BalanceTrackingDao dao = BalanceTrackingDao.getInstance() ;
	
	private final String MAIN_URL_JC = "/jsp/report/jcafterservicereport/balanceTrackingJC.jsp" ;//轿车
	private final String MAIN_URL_WC = "/jsp/report/jcafterservicereport/balanceTrackingWC.jsp" ;//微车
	
	public void mainUrlInit4JC(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
			List<Map<String, Object>> areaList = dao.getAreaList();//区域
			act.setOutData("areaList", areaList);
			act.setForword(MAIN_URL_JC);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算跟踪报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void query4JC(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String code = req.getParamValue("code") ;
			String name = req.getParamValue("name") ;
			String area = req.getParamValue("area") ;
			String province = req.getParamValue("province") ;
			String bDate = req.getParamValue("bDate") ;
			String eDate = req.getParamValue("eDate") ;
			
			StringBuffer sql = new StringBuffer() ;
			if(StringUtil.notNull(code))
				sql.append("and d.dealer_code like '%").append(code).append("%'\n") ;
			if(StringUtil.notNull(name))
				sql.append("and d.dealer_name like '%").append(name).append("%'\n") ;
			if(StringUtil.notNull(area))
				sql.append("and s.root_org_id = ").append(area).append("\n") ;
			if(StringUtil.notNull(province))
				sql.append("and d.province_id = ").append(province).append("\n") ;
			if(StringUtil.notNull(bDate))
				sql.append("and trunc(b.start_date) >= to_date('").append(bDate).append("','yyyy-MM-dd')\n") ;
			if(StringUtil.notNull(eDate))
				sql.append("and trunc(b.end_date) <= to_date('").append(eDate).append("','yyyy-MM-dd')") ;
			
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; 
			int pageSize = 15 ;
					
			PageResult<Map<String, Object>> ps = dao.query4JC(sql.toString(), pageSize, curPage) ;
			
			act.setOutData("ps", ps) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算跟踪报表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void excel4JC(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String code = req.getParamValue("code") ;
			String name = req.getParamValue("name") ;
			String area = req.getParamValue("area") ;
			String province = req.getParamValue("province") ;
			String bDate = req.getParamValue("bDate") ;
			String eDate = req.getParamValue("eDate") ;
			
			StringBuffer sql = new StringBuffer() ;
			if(StringUtil.notNull(code))
				sql.append("and d.dealer_code like '%").append(code).append("%'\n") ;
			if(StringUtil.notNull(name))
				sql.append("and d.dealer_name like '%").append(name).append("%'\n") ;
			if(StringUtil.notNull(area))
				sql.append("and s.root_org_id = ").append(area).append("\n") ;
			if(StringUtil.notNull(province))
				sql.append("and d.province_id = ").append(province).append("\n") ;
			if(StringUtil.notNull(bDate))
				sql.append("and trunc(b.start_date) >= to_date('").append(bDate).append("','yyyy-MM-dd')\n") ;
			if(StringUtil.notNull(eDate))
				sql.append("and trunc(b.end_date) <= to_date('").append(eDate).append("','yyyy-MM-dd')") ;
			
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; 
			int pageSize = 100000 ;
					
			PageResult<Map<String, Object>> ps = dao.query4JC(sql.toString(), pageSize, curPage) ;
			
			String fileName = "服务站结算跟踪报表.csv";
			fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
			act.getResponse().setContentType("Application/text/csv");
			act.getResponse().addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			List<List<Object>> hList = new LinkedList<List<Object>>();
			List<Object> titleList = genHead4JC();
			hList.add(titleList);
			
			List<Map<String, Object>> details = ps.getRecords() ;
			
			for(Map<String, Object> detail : details) {
				hList.add(genBody4JC(detail));
			}
			os = act.getResponse().getOutputStream();
			CsvWriterUtil.writeCsv(hList, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算跟踪报表主页面");
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
	
	private List<Object> genHead4JC(){
		List<Object> titleList = new LinkedList<Object>();
		titleList.add("序号") ;
		titleList.add("一级服务中心代码") ;
		titleList.add("一级服务中心名称") ;
		titleList.add("服务站代码") ;
		titleList.add("服务站名称") ;
		titleList.add("厂家") ;
		titleList.add("结算单号") ;
		titleList.add("大区") ;
		titleList.add("省份") ;
		titleList.add("维修起止时间") ;
		titleList.add("收单时间") ;
		titleList.add("提交复核时间") ;
		titleList.add("开票通知单时间") ;
		titleList.add("汇总报表收票时间") ;
		titleList.add("财务挂账时间") ;
		titleList.add("申请金额") ;
		titleList.add("结算金额") ;
		return titleList;
	}
	private List<Object> genBody4JC(Map<String, Object> detail){
		List<Object> dataList = new LinkedList<Object>();
		dataList.add(detail.get("ROWNUM"));
		dataList.add(detail.get("ROOT_DEALER_CODE"));
		dataList.add(detail.get("ROOT_DEALER_NAME"));
		dataList.add(detail.get("DEALER_CODE"));
		dataList.add(detail.get("DEALER_NAME"));
		dataList.add(detail.get("YIELDLY"));
		dataList.add(detail.get("BALANCE_NO"));
		dataList.add(detail.get("ROOT_ORG_NAME"));
		dataList.add(detail.get("REGION_NAME"));
		dataList.add(detail.get("RO_DATE"));
		dataList.add(detail.get("SIGN_DATE"));
		dataList.add(detail.get("REAUDIT_DATE"));
		dataList.add(detail.get("FANCE_DATE"));
		dataList.add(detail.get("GET_DATE"));
		dataList.add(detail.get("CREDIT_DATE"));
		dataList.add(detail.get("APPLY_AMOUNT"));
		dataList.add(detail.get("NOTE_AMOUNT"));
		return dataList;
	}
	
	public void mainUrlInit4WC(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			SpecialCostReportDao dao = new SpecialCostReportDao();
			List<Map<String, Object>> areaList = dao.getAreaList();//区域
			act.setOutData("areaList", areaList);
			act.setForword(MAIN_URL_WC);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算跟踪报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void query4WC(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String code = req.getParamValue("code") ;
			String name = req.getParamValue("name") ;
			String area = req.getParamValue("area") ;
			String balanceNo = req.getParamValue("balanceNo") ;
			String yieldly = req.getParamValue("yieldly") ;
			String province = req.getParamValue("province") ;
			String bDate = req.getParamValue("bDate") ;
			String eDate = req.getParamValue("eDate") ;
			
			StringBuffer sql = new StringBuffer() ;
			if(StringUtil.notNull(code))
				sql.append("and d.dealer_code like '%").append(code).append("%'\n") ;
			if(StringUtil.notNull(name))
				sql.append("and d.dealer_name like '%").append(name).append("%'\n") ;
			if(StringUtil.notNull(area))
				sql.append("and s.root_org_id = ").append(area).append("\n") ;
			if(StringUtil.notNull(province))
				sql.append("and d.province_id = ").append(province).append("\n") ;
			if(StringUtil.notNull(bDate))
				sql.append("and trunc(b.start_date) >= to_date('").append(bDate).append("','yyyy-MM-dd')\n") ;
			if(StringUtil.notNull(eDate))
				sql.append("and trunc(b.end_date) <= to_date('").append(eDate).append("','yyyy-MM-dd')") ;
			if(StringUtil.notNull(yieldly))
				sql.append("and b.yieldly = ").append(yieldly).append("\n") ;
			if(StringUtil.notNull(balanceNo))
				sql.append("and b.balance_no like '%").append(balanceNo).append("%'\n") ;
			
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1 ; 
			int pageSize = 15 ;
					
			PageResult<Map<String, Object>> ps = dao.query4JC2(sql.toString(),logonUser.getPoseId(), pageSize, curPage) ;
			
			act.setOutData("ps", ps) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算跟踪报表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void excel4WC(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String code = req.getParamValue("code") ;
			String name = req.getParamValue("name") ;
			String area = req.getParamValue("area") ;
			String balanceNo = req.getParamValue("balanceNo") ;
			String yieldly = req.getParamValue("yieldly") ;
			String province = req.getParamValue("province") ;
			String bDate = req.getParamValue("bDate") ;
			String eDate = req.getParamValue("eDate") ;
			
			StringBuffer sql = new StringBuffer() ;
			if(StringUtil.notNull(code))
				sql.append("and d.dealer_code like '%").append(code).append("%'\n") ;
			if(StringUtil.notNull(name))
				sql.append("and d.dealer_name like '%").append(name).append("%'\n") ;
			if(StringUtil.notNull(area))
				sql.append("and s.root_org_id = ").append(area).append("\n") ;
			if(StringUtil.notNull(province))
				sql.append("and d.province_id = ").append(province).append("\n") ;
			if(StringUtil.notNull(bDate))
				sql.append("and trunc(b.start_date) >= to_date('").append(bDate).append("','yyyy-MM-dd')\n") ;
			if(StringUtil.notNull(eDate))
				sql.append("and trunc(b.end_date) <= to_date('").append(eDate).append("','yyyy-MM-dd')") ;
			if(StringUtil.notNull(yieldly))
				sql.append("and b.yieldly = ").append(yieldly).append("\n") ;
			if(StringUtil.notNull(balanceNo))
				sql.append("and b.balance_no like '%").append(balanceNo).append("%'\n") ;
			
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; 
			int pageSize = 100000 ;
					
			PageResult<Map<String, Object>> ps = dao.query4JC(sql.toString(), pageSize, curPage) ;
			
			String fileName = "服务站结算跟踪报表.csv";
			fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
			act.getResponse().setContentType("Application/text/csv");
			act.getResponse().addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			List<List<Object>> hList = new LinkedList<List<Object>>();
			List<Object> titleList = genHead4WC();
			hList.add(titleList);
			
			List<Map<String, Object>> details = ps.getRecords() ;
			
			for(Map<String, Object> detail : details) {
				hList.add(genBody4WC(detail));
			}
			os = act.getResponse().getOutputStream();
			CsvWriterUtil.writeCsv(hList, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算跟踪报表主页面");
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
	
	private List<Object> genHead4WC(){
		List<Object> titleList = new LinkedList<Object>();
		titleList.add("序号") ;
		titleList.add("一级服务中心代码") ;
		titleList.add("一级服务中心名称") ;
		titleList.add("服务站代码") ;
		titleList.add("服务站名称") ;
		titleList.add("厂家") ;
		titleList.add("结算单号") ;
		titleList.add("大区") ;
		titleList.add("省份") ;
		titleList.add("维修起止时间") ;
		titleList.add("收单时间") ;
		titleList.add("收单人") ;
		titleList.add("提交复核时间") ;
		titleList.add("结算员") ;
		titleList.add("开票通知单时间") ;
		titleList.add("通知发出人员") ;
		titleList.add("汇总报表收票时间") ;
		titleList.add("收票人") ;
		titleList.add("财务挂账时间") ;
		titleList.add("挂账人") ;
		titleList.add("申请金额") ;
		titleList.add("结算金额") ;
		return titleList;
	}
	private List<Object> genBody4WC(Map<String, Object> detail){
		List<Object> dataList = new LinkedList<Object>();
		dataList.add(detail.get("ROWNUM"));   
		dataList.add(detail.get("ROOT_DEALER_CODE"));
		dataList.add(detail.get("ROOT_DEALER_NAME"));
		dataList.add(detail.get("DEALER_CODE"));
		dataList.add(detail.get("DEALER_NAME"));
		dataList.add(detail.get("YIELDLY"));
		dataList.add(detail.get("BALANCE_NO"));
		dataList.add(detail.get("ROOT_ORG_NAME"));
		dataList.add(detail.get("REGION_NAME"));
		dataList.add(detail.get("RO_DATE"));
		dataList.add(detail.get("SIGN_DATE"));
		dataList.add(detail.get("SIGN_PERSON"));
		dataList.add(detail.get("REAUDIT_DATE"));
		dataList.add(detail.get("REAUDIT_PERSON"));
		dataList.add(detail.get("FANCE_DATE"));
		dataList.add(detail.get("FANCE_PERSON"));
		dataList.add(detail.get("GET_DATE"));
		dataList.add(detail.get("GET_PERSON"));
		dataList.add(detail.get("CREDIT_DATE"));
		dataList.add(detail.get("CREDIT_PERSON"));
		dataList.add(detail.get("APPLY_AMOUNT"));
		dataList.add(detail.get("NOTE_AMOUNT"));
		return dataList;
	}
}
