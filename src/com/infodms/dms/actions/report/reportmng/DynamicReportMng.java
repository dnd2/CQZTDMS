package com.infodms.dms.actions.report.reportmng;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.report.jcafterservicereport.DynamicReportMngDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsReportBakPO;
import com.infodms.dms.po.TtAsReportInputparamPO;
import com.infodms.dms.po.TtAsReportPO;
import com.infodms.dms.po.TtAsReportShowparamPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 动态报表生成器
 * @author nova_zuo
 *
 */
public class DynamicReportMng {
	private Logger logger = Logger.getLogger(DynamicReportMng.class) ;
	private ActionContext act = null ;
	private RequestWrapper req = null ;
	private AclUserBean logonUser = null ;
	private DynamicReportMngDao dao = DynamicReportMngDao.getInstance() ;
	
	//MNG
	private final String MAIN_URL = "/jsp/report/dynamicReportMng/mainUrl.jsp" ;//主查询页面
	private final String ADD_URL = "/jsp/report/dynamicReportMng/addUrl.jsp" ;//新增报表页面
	private final String UPDATE_URL = "/jsp/report/dynamicReportMng/updateUrl.jsp" ;//报表修改页面
	private final String DETAIL_URL = "/jsp/report/dynamicReportMng/detailUrl.jsp" ;//报表明细页面
	
	//USE
	private final String MAIN_URL4USE = "/jsp/report/dynamicReportMng/mainUrl4Use.jsp" ;//主查询页面
	private final String SHOW_URL = "/jsp/report/dynamicReportMng/showUrl.jsp" ;//前台运行结果界面
	
	/*
	 * 动态报表维护主页面初始化
	 */
	public void reportMngInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(MAIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 动态报表的维护与使用均调用此方法
	 * 1、系统默认的参数：userId,dealerid,yieldlys
	 * 2、配置的输入参数：
	 * 对于这两种参数，在SQL转换的时候
	 *                如果有值就直接替换成用户输入数据，
	 *                如果没有输入数据，就替换为默认值。
	 */
	public void reportQuery(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reportName = req.getParamValue("reportName") ;
			String flag = req.getParamValue("flag") ;
			StringBuffer sql = new StringBuffer() ;
			if(StringUtil.notNull(reportName))
				sql.append("and r.report_name like '%").append(reportName).append("%'\n") ;
			if("2".equals(flag))
				sql.append("and r.oem_only = ").append(Constant.STATUS_DISABLE).append("\n") ;
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 20 ;
			PageResult<Map<String,Object>> ps = dao.queryAllReport(sql.toString(), pageSize, curPage) ;
			act.setOutData("ps", ps) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 新增报表页面初始化跳转方法
	 */
	public void addReport(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表新增页面跳转");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 真正实行报表插入操作
	 */
	public void reportInsert(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reportName = req.getParamValue("reportName") ;
			String remark = req.getParamValue("remark") ; //数据说明
			String reportSql = req.getParamValue("sql") ;
			String oemOnly = req.getParamValue("oemOnly") ;
			String remark2 = req.getParamValue("remark2") ;//使用说明
			String person = req.getParamValue("person") ;
			String time = req.getParamValue("mentionTime") ;
			
			String[] otherName = req.getParamValues("otherName") ;//SQL中的别名
			String[] showName = req.getParamValues("showName") ;//显示中的名称
			
			String[] paramCode = req.getParamValues("paramCode") ;//参数代码
			String[] paramName = req.getParamValues("paramName") ;//参数名称
			String[] paramType = req.getParamValues("paramType") ;//输入参数类型
			String[] defaultValue = req.getParamValues("defaultValue") ;//默认值
			
			//报表主数据插入
			TtAsReportPO report = new TtAsReportPO() ;
			Long reportId = Utility.getLong(SequenceManager.getSequence("")) ;
			report.setCreateBy(logonUser.getUserId()) ;
			report.setCreateDate(new Date()) ;
			//report.setMainSql(reportSql) ;
			report.setRemark(remark) ;
			report.setRemark2(remark2) ;
			report.setReportId(reportId) ;
			report.setReportName(reportName) ;
			report.setMentionPerson(person) ;
			if(StringUtil.notNull(time)){
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd") ;
				report.setMentionTime(f.parse(time)) ;
			}
			report.setOemOnly(Integer.parseInt(oemOnly)) ;
			dao.insert(report) ;
			dao.insertMainSql(reportSql, "TT_AS_REPORT", "MAIN_SQL", reportId);
			//报表显示字段插入
			TtAsReportShowparamPO show = null ;
			if(otherName!=null){
				for(int i=0 ;i<otherName.length;i++){
					show = new TtAsReportShowparamPO() ;
					show.setId(Utility.getLong(SequenceManager.getSequence(""))) ;
					show.setReportId(reportId) ;
					show.setOtherName(otherName[i].toUpperCase()) ;
					if(StringUtil.notNull(showName[i]))
						show.setShowName(showName[i]) ;
					else
						show.setShowName(otherName[i].toUpperCase()) ;
					dao.insert(show) ;
				}
			}
			
			//报表参数字段插入
			TtAsReportInputparamPO input = null ;
			if(paramCode!=null){
				int size = paramCode.length ;
				if(paramName!=null)
					size = paramCode.length - paramName.length  ;
				for(int i=0 ;i<paramCode.length;i++){
					input = new TtAsReportInputparamPO() ;
					input.setId(Utility.getLong(SequenceManager.getSequence(""))) ;
					input.setReportId(reportId) ;
					input.setParamCode(paramCode[i]) ;
					if(i-size>=0){
						input.setParamName(paramName[i-size]) ;
						input.setParamType(Integer.parseInt(paramType[i-size]));
						input.setDefaultValue(defaultValue[i-size]) ;
					}
					dao.insert(input) ;
				}
			}
			
			//报表SQL数据备份
			TtAsReportBakPO bak = new TtAsReportBakPO() ;
			bak.setCreateDate(new Date()) ;
			bak.setReportName(reportName) ;
			bak.setReportId(reportId) ;
			//bak.setSql(reportSql) ;
			bak.setUserid(logonUser.getUserId()) ;
			dao.insert(bak) ;
			dao.insertMainSql(reportSql, "TT_AS_REPORT_BAK", "SQL", reportId);
			act.setOutData("flag", true) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ADD_FAILURE_CODE, "动态报表插入操作");
			logger.error(logonUser, e1);
			act.setOutData("flag", false) ;
			act.setException(e1);
		}
	}
	
	/*
	 * 报表修改页面跳转方法
	 */
	public void updateReportUrl(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long id = Long.parseLong(req.getParamValue("id")) ;
			
			TtAsReportPO po = new TtAsReportPO() ;
			po.setReportId(id);
			List list1 = dao.select(po) ;
			if(list1.size()>0)
				po = (TtAsReportPO)list1.get(0) ;
			
			TtAsReportShowparamPO show = new TtAsReportShowparamPO() ;
			show.setReportId(id) ;
			List list2 = dao.select(show) ;
			
			List list3 = dao.getInput(id) ;
			String sql = dao.readMainSql(id);
			act.setOutData("report", po) ;
			act.setOutData("list2", list2) ;
			act.setOutData("list3", list3) ;
			act.setOutData("sql", sql);
			act.setForword(UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表修改跳转");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 真正实行报表修改操作
	 * 注：因为在前台删除报表参数的时候是直接后台删除，所以这里只执行参数的插入，而非修改
	 */
	public void reportUpdate(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long reportId = Long.parseLong(req.getParamValue("reportId")) ;
			String reportName = req.getParamValue("reportName") ;
			String remark = req.getParamValue("remark") ;
			String remark2 = req.getParamValue("remark2") ;
			String oemOnly = req.getParamValue("oemOnly") ;
			String sql = req.getParamValue("sql") ;
			String person = req.getParamValue("person") ;
			String time = req.getParamValue("mentionTime") ;
			
			TtAsReportPO po = new TtAsReportPO() ;
			TtAsReportPO report = new TtAsReportPO() ;
			po.setReportId(reportId) ;
			report.setReportId(reportId) ;
			//report.setMainSql(sql) ;
			report.setReportName(reportName) ;
			report.setRemark(remark) ;
			report.setRemark2(remark2) ;
			report.setMentionPerson(person) ;
			if(StringUtil.notNull(time)){
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd") ;
				report.setMentionTime(f.parse(time)) ;
			}
			report.setOemOnly(Integer.parseInt(oemOnly)) ;
			report.setUpdateBy(logonUser.getUserId()) ;
			report.setUpdateDate(new Date()) ;
			dao.update(po,report) ;
			dao.insertMainSql(sql, "TT_AS_REPORT", "MAIN_SQL", reportId);
			String[] otherName = req.getParamValues("otherName") ;//SQL中的别名
			String[] showName = req.getParamValues("showName") ;//显示中的名称
			
			String[] paramCode = req.getParamValues("paramCode") ;//参数代码
			String[] paramName = req.getParamValues("paramName") ;//参数名称
			String[] paramType = req.getParamValues("paramType") ;//输入参数类型
			String[] defaultValue = req.getParamValues("defaultValue") ;//默认值
			
			//报表显示字段插入
			TtAsReportShowparamPO show = null ;
			if(otherName != null){
				for(int i=0 ;i<otherName.length;i++){
					show = new TtAsReportShowparamPO() ;
					show.setId(Utility.getLong(SequenceManager.getSequence(""))) ;
					show.setReportId(reportId) ;
					show.setOtherName(otherName[i].toUpperCase()) ;
					if(StringUtil.notNull(showName[i]))
						show.setShowName(showName[i]) ;
					else
						show.setShowName(otherName[i].toUpperCase()) ;
					dao.insert(show) ;
				}
			}
			
			//报表参数字段插入
			TtAsReportInputparamPO input = null ;
			if(paramCode != null){
				int size = paramCode.length ;
				if(paramName!=null)
					size = paramCode.length - paramName.length  ;
				for(int i=0 ;i<paramCode.length;i++){
					input = new TtAsReportInputparamPO() ;
					input.setId(Utility.getLong(SequenceManager.getSequence(""))) ;
					input.setReportId(reportId) ;
					input.setParamCode(paramCode[i]) ;
					if(i-size>=0){
						input.setParamName(paramName[i-size]) ;
						input.setParamType(Integer.parseInt(paramType[i-size]));
						input.setDefaultValue(defaultValue[i-size]) ;
					}
					dao.insert(input) ;
				}
			}
			
			//报表SQL数据备份
			TtAsReportBakPO bak = new TtAsReportBakPO() ;
			bak.setCreateDate(new Date()) ;
			bak.setReportName(reportName) ;
			bak.setReportId(reportId) ;
			//bak.setSql(sql) ;
			bak.setUserid(logonUser.getUserId()) ;
			dao.insert(bak) ;
			dao.insertMainSql(sql, "TT_AS_REPORT_BAK", "SQL", reportId);
			act.setOutData("flag", true) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表删除明细");
			logger.error(logonUser, e1);
			act.setOutData("flag", false) ;
			act.setException(e1);
		}
	}
	
	/*
	 * 针对前台操作-删除某参数的时候执行的方法
	 */
	public void deleteRow(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String detailId = req.getParamValue("detailId") ;
			String type = req.getParamValue("type") ;
			String length = req.getParamValue("length") ;
			
			String sql = "delete from tt_as_report_"+type+"param where id="+detailId ;
			dao.delete(sql, null) ;
			
			act.setOutData("flag", true) ;
			act.setOutData("type", type) ;
			act.setOutData("length", length) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表删除明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 针对点击查看操作执行的跳转
	 */
	public void showReport(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long id = Long.parseLong(req.getParamValue("id")) ;
			
			TtAsReportPO po = new TtAsReportPO() ;
			po.setReportId(id);
			List list1 = dao.select(po) ;
			if(list1.size()>0)
				po = (TtAsReportPO)list1.get(0) ;
			
			TtAsReportShowparamPO show = new TtAsReportShowparamPO() ;
			show.setReportId(id) ;
			List list2 = dao.select(show) ;
			
			List list3 = dao.getInput(id) ;
			String sql = dao.readMainSql(id);
			act.setOutData("sql", sql) ;
			act.setOutData("report", po) ;
			act.setOutData("list2", list2) ;
			act.setOutData("list3", list3) ;
			
			//向前台传两个默认参数
			act.setOutData("userId", logonUser.getUserId()) ;
			act.setOutData("dealerId", logonUser.getDealerId()) ;
			
			act.setForword(DETAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表查看跳转");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 针对相应报表所执行的查询、前台显示操作
	 * 
	 */
	public void doQuery(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long id = Long.parseLong(req.getParamValue("reportId")) ;
			
			//取得该用户拥有的产地权限
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId());
			
			TtAsReportPO report = new TtAsReportPO() ;
			report.setReportId(id) ;
			List list = dao.select(report) ;
			if(list.size()>0)
				report = (TtAsReportPO)list.get(0) ;
			String sql = dao.readMainSql(id);
			
			TtAsReportInputparamPO po = new TtAsReportInputparamPO() ;
			po.setReportId(id) ;
			List input = dao.select(po) ;
			for(int i=0;i<input.size();i++){
				po = (TtAsReportInputparamPO)input.get(i) ;
				String str = req.getParamValue(po.getParamCode()) ;
				if("null".equals(str)||str==null){
					if(StringUtil.notNull(po.getDefaultValue())){
						if("sysdate".equals(po.getDefaultValue())){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
							str = sdf.format(new Date()) ;
						}else
							str = po.getDefaultValue() ;	
					}
					else str = "" ;			
				}else if(str.contains(",")){
					str = "'"+str+"'"  ;
					str = str.replaceAll("\\s+", "");
					str = str.replace(",", "','") ;
				}
				if(sql.contains("[yieldlys]")){
					sql = sql.replace("[yieldlys]", yieldlys) ;
				}
				sql = sql.replace("["+po.getParamCode()+"]", str) ;
			}
			
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 15 ;
			
			act.setOutData("ps",dao.doQuery1(sql, pageSize, curPage)) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 数据盒子使用->点运行时执行的跳转方法
	 */
	public void goQuery(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long id = Long.parseLong(req.getParamValue("id")) ;
			
			TtAsReportPO po = new TtAsReportPO() ;
			po.setReportId(id);
			List list1 = dao.select(po) ;
			if(list1.size()>0)
				po = (TtAsReportPO)list1.get(0) ;
			
			TtAsReportShowparamPO show = new TtAsReportShowparamPO() ;
			show.setReportId(id) ;
			List list2 = dao.select(show) ;
			
			List list3 = dao.getInput(id) ;
			
			act.setOutData("reportId", id) ;
			act.setOutData("report", po) ;
			act.setOutData("list2", list2) ;
			act.setOutData("list3", list3) ;
			
			//向前台传两个默认参数
			act.setOutData("userId", logonUser.getUserId()) ;
			act.setOutData("dealerId", logonUser.getDealerId()) ;
			
			act.setForword(SHOW_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表运行跳转");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 前台对SQL进行转换，以验证主SQL与参数配置是否正确
	 * SQL替换规则同查询  - 详见doQuery()
	 */
	public void sqlUpdate(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long id = Long.parseLong(req.getParamValue("reportId")) ;
			
			//取得该用户拥有的产地权限
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId());
			
			TtAsReportPO report = new TtAsReportPO() ;
			report.setReportId(id) ;
			List list = dao.select(report) ;
			if(list.size()>0)
				report = (TtAsReportPO)list.get(0) ;
			
		//	String sql = report.getMainSql() ;
			String sql = dao.readMainSql(id);
			TtAsReportInputparamPO po = new TtAsReportInputparamPO() ;
			po.setReportId(id) ;
			List input = dao.select(po) ;
			for(int i=0;i<input.size();i++){
				po = (TtAsReportInputparamPO)input.get(i) ;
				String str = req.getParamValue(po.getParamCode()) ;
				if("null".equals(str)||str==null){
					if(StringUtil.notNull(po.getDefaultValue())){
						if("sysdate".equals(po.getDefaultValue())){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
							str = sdf.format(new Date()) ;
						}else
							str = po.getDefaultValue() ;	
					}
					else str = "" ;			
				}else if(str.contains(",")){
					str = "'"+str+"'"  ;
					str = str.replaceAll("\\s+", "");
					str = str.replace(",", "','") ;
				}
				if(sql.contains("[yieldlys]")){
					sql = sql.replace("[yieldlys]", yieldlys) ;
				}
				sql = sql.replace("["+po.getParamCode()+"]", str) ;
			}
			
			act.setOutData("flag", sql) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表运行跳转");
			logger.error(logonUser, e1);
			act.setOutData("falg", "转换过程出错") ;
			act.setException(e1);
		}
	}

	/*
	 * 导出为excel操作
	 * SQL替换规则同查询  - 详见doQuery()
	 */
	public void toExcel(){
		act = ActionContext.getContext();
		req = act.getRequest();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			
			Long id = Long.parseLong(req.getParamValue("reportId")) ;
			
			//取得该用户拥有的产地权限
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId());
			
			TtAsReportPO report = new TtAsReportPO() ;
			report.setReportId(id) ;
			List list = dao.select(report) ;
			if(list.size()>0)
				report = (TtAsReportPO)list.get(0) ;
			
			TtAsReportShowparamPO show = new TtAsReportShowparamPO() ;
			show.setReportId(id) ;
			List<TtAsReportShowparamPO> list2 = dao.select(show) ;
			
			//String sql = report.getMainSql() ;
			String sql = dao.readMainSql(id);
			TtAsReportInputparamPO po = new TtAsReportInputparamPO() ;
			po.setReportId(id) ;
			List input = dao.select(po) ;
			for(int i=0;i<input.size();i++){
				po = (TtAsReportInputparamPO)input.get(i) ;
				String str = req.getParamValue(po.getParamCode()) ;
				if("null".equals(str)||str==null){
					if(StringUtil.notNull(po.getDefaultValue())){
						if("sysdate".equals(po.getDefaultValue())){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
							str = sdf.format(new Date()) ;
						}else
							str = po.getDefaultValue() ;	
					}
					else str = "" ;			
				}else if(str.contains(",")){
					str = "'"+str+"'"  ;
					str = str.replaceAll("\\s+", "");
					str = str.replace(",", "','") ;
				}
				if(sql.contains("[yieldlys]")){
					sql = sql.replace("[yieldlys]", yieldlys) ;
				}
				sql = sql.replace("["+po.getParamCode()+"]", str) ;
			}
			
			String fileName = report.getReportName()+".csv";
			int pageSize = 1000000 ;
			int curPage = 1 ;
			
			String[] head=new String[list2.size()];
			for(int i=0;i<list2.size();i++){
				head[i]=list2.get(i).getShowName();
			}
			List<Map<String, Object>> ps = dao.doQuery(sql, pageSize, curPage) ;
			
			  List list1=new ArrayList();
			    if(ps!=null&&ps.size()!=0){
					for(int i=0;i<ps.size();i++){
				    	Map map =(Map)ps.get(i);
				    	if(map!=null&&map.size()!=0){
				    		String[] detail=new String[list2.size()];
							for(int j=0;j<list2.size();j++){
								
									detail[j]=String.valueOf(map.get(""+list2.get(j).getOtherName()+""));
									
								}
							list1.add(detail);
								
							}
							
								
							}
							
				    	
				    }
		  com.infodms.dms.actions.claim.basicData.ToExcel.toNewExcel(ActionContext.getContext().getResponse(), req, head, list1,fileName);
			    
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"报表导出功能 ");
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
	private List<Object> genHead(List list){
		List<Object> titleList = new LinkedList<Object>();
		for(int i = 0 ; i<list.size() ; i++){
			titleList.add(((TtAsReportShowparamPO)list.get(i)).getShowName()) ;
		}
		return titleList;
	}
	private List<Object> genBody(Map<String, Object> detail,List list){
		List<Object> dataList = new LinkedList<Object>();
		for(int i = 0 ; i < list.size() ; i++){
			dataList.add(CommonUtils.checkNull(detail.get(((TtAsReportShowparamPO)list.get(i)).getOtherName())));     
		} 
		return dataList;
	}
	
	/*
	 * 主报表的删除，此时没有相应删除显示参数与输入参数。
	 * 因为主报表不存在，相应的参数信息也失去意义。
	 */
	public void deleteReport(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id") ;
			
			String sql = "delete from tt_as_report where report_id="+id ;
			dao.delete(sql, null) ;
			
			this.reportMngInit() ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表删除明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 厂端，数据盒子使用初始化
	 */
	/**************动态报表的使用**************/
	public void reportUseInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("flag", 1) ;
			act.setForword(MAIN_URL4USE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "数据盒子主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/*
	 * 经销商端，数据盒子使用初始化
	 */
	public void reportUseInit1(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("flag", 2) ;
			act.setForword(MAIN_URL4USE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "数据盒子主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}

