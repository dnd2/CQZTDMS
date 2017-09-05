package com.infodms.dms.actions.sales.salesConsultant;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.sales.salesConsultant.SalesConsultantDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class SalesConsultant {
	public Logger logger = Logger.getLogger(SalesConsultant.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest() ;
	private SalesConsultantDAO dao = SalesConsultantDAO.getInstance() ;
	
	private final String SUBMIT_INIT = "/jsp/sales/salesConsultant/salesConsultantDlrInit.jsp" ;
	private final String DLR_QUERY_INIT = "/jsp/sales/salesConsultant/salesConsultantDlrQueryInit.jsp" ;
	private final String DLR_UPDATE_INIT = "/jsp/sales/salesConsultant/salesConsultantDlrUpdateInit.jsp" ;
	private final String AREA_CHECK_INIT = "/jsp/sales/salesConsultant/salesConsultantAreaCheckInit.jsp" ;
	private final String AREA_QUERY_INIT = "/jsp/sales/salesConsultant/salesConsultantAreaQueryInit.jsp" ;
	private final String OEM_QUERY_INIT = "/jsp/sales/salesConsultant/salesConsultantOemQueryInit.jsp" ;
	private final String ADD_SUBMIT_URL = "/jsp/sales/salesConsultant/salesConsultantAddInit.jsp" ;
	private final String AREA_CHECK_DTL_URL = "/jsp/sales/salesConsultant/salesConsultantAreaCheckDtl.jsp" ;
	
	private final String DTL_INIT = "/jsp/sales/salesConsultant/salesConsultantDtlInit.jsp" ;
	
	/**
	 * 销售顾问经销商申请初始化页面跳转
	 */
	public void submitInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusinessAll(logonUser.getPoseId().toString());
			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areaIds = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areaIds", areaIds) ;
			act.setOutData("areaList", areaList);
			
			act.setForword(SUBMIT_INIT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问经销商申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问经销商查询初始化页面跳转
	 */
	public void dlrSumbitQueryInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusinessAll(logonUser.getPoseId().toString());
			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areaIds = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areaIds", areaIds) ;
			act.setOutData("areaList", areaList);
			
			act.setForword(DLR_QUERY_INIT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问经销商查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问分销中心审核初始化页面跳转
	 */
	public void areaCheckInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areaIds = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areaIds", areaIds) ;
			act.setOutData("areaList", areaList);
			act.setOutData("orgId", logonUser.getOrgId()) ;
			
			act.setForword(AREA_CHECK_INIT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问组织审核初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问分销中心查询页面初始化跳转
	 */
	public void areaQueryInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String orgId = logonUser.getOrgId().toString() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areaIds = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areaIds", areaIds) ;
			act.setOutData("areaList", areaList);
			act.setOutData("orgId", orgId) ;
			
			act.setForword(AREA_QUERY_INIT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问分销中心查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问总部查询页面初始化跳转
	 */
	public void oemQueryInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areaIds = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areaIds", areaIds) ;
			act.setOutData("areaList", areaList);
			
			act.setForword(OEM_QUERY_INIT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问总部查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问经销商申请查询操作
	 */
	public void submitQuery() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String areaIds = CommonUtils.checkNull(request.getParamValue("areaIds")) ;
			String salesConName = CommonUtils.checkNull(request.getParamValue("salesConName")) ;
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId")) ;
			String dealerId = logonUser.getDealerId() ;
			
			if(!CommonUtils.isNullString(areaId)) {
				areaIds = areaId ;
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("areaIds", areaIds) ;
			map.put("salesConName", salesConName) ;
			map.put("dealerId", dealerId) ;
			map.put("status", Constant.SALES_CONSULTANT_STATUS_PASS + "," + Constant.SALES_CONSULTANT_STATUS_RETURN) ;
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.salesConsultantQuery(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问经销商申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问经销商申请页面跳转
	 */
	public void submitAddInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			
			DealerRelation dr = new DealerRelation() ;
			List<Map<String, Object>> dlrList = dr.getDealerPoseRelation(logonUser.getCompanyId(), logonUser.getPoseId()) ;
			
			act.setOutData("dlrList", dlrList) ;
			act.setForword(ADD_SUBMIT_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问经销商申请页面跳转");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问经销商申请操作
	 */
	public void submitAdd() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String salesConName = CommonUtils.checkNull(request.getParamValue("salesConName")) ;
			String sex = CommonUtils.checkNull(request.getParamValue("sex")) ;
			String bornYear = CommonUtils.checkNull(request.getParamValue("bornYear")) ;
			String academicRecords = CommonUtils.checkNull(request.getParamValue("academicRecords")) ;
			String tradeYear = CommonUtils.checkNull(request.getParamValue("tradeYear")) ;
			String chanaTradeYear = CommonUtils.checkNull(request.getParamValue("chanaTradeYear")) ;
			String tel = CommonUtils.checkNull(request.getParamValue("tel")) ;
			String reason = CommonUtils.checkNull(request.getParamValue("reason")) ;
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")) ;
			String identityNumber = CommonUtils.checkNull(request.getParamValue("identityNumber"));	//新增身份证号 2012-09-04 韩晓宇
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("dealerId", dealerId) ;
			map.put("salesConName", salesConName) ;
			map.put("sex", sex) ;
			map.put("bornYear", bornYear) ;
			map.put("academicRecords", academicRecords) ;
			map.put("tradeYear", tradeYear) ;
			map.put("chanaTradeYear", chanaTradeYear) ;
			map.put("tel", tel) ;
			map.put("reason", reason) ;
			map.put("userId", logonUser.getUserId().toString()) ;
			map.put("status", Constant.SALES_CONSULTANT_STATUS_RAISE.toString()) ;
			map.put("identityNumber", identityNumber);
			
			dao.salesConsultantInsert(map) ;
			
			act.setOutData("subFlag", "success") ;
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问经销商申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问经销商修改页面跳转
	 */
	public void submitUpdateInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String headId = CommonUtils.checkNull(request.getParamValue("headId")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("headId", headId) ;
			
			Map<String, Object> dtlMap = dao.salesConsultantDtlQuery(map) ;
			
			List<Map<String, Object>> chkList = dao.salesConsultantChkQuery(map) ;
			
			DealerRelation dr = new DealerRelation() ;
			List<Map<String, Object>> dlrList = dr.getDealerPoseRelation(logonUser.getCompanyId(), logonUser.getPoseId()) ;
			
			act.setOutData("dlrList", dlrList) ;
			act.setOutData("dtlMap", dtlMap) ;
			act.setOutData("chkList", chkList) ;
			
			act.setForword(DLR_UPDATE_INIT) ;
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问经销商修改页面跳转");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问经销商修改操作
	 */
	public void submitUpdate() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String headId = CommonUtils.checkNull(request.getParamValue("headId")) ;
			String salesConName = CommonUtils.checkNull(request.getParamValue("salesConName")) ;
			String sex = CommonUtils.checkNull(request.getParamValue("sex")) ;
			String bornYear = CommonUtils.checkNull(request.getParamValue("bornYear")) ;
			String academicRecords = CommonUtils.checkNull(request.getParamValue("academicRecords")) ;
			String tradeYear = CommonUtils.checkNull(request.getParamValue("tradeYear")) ;
			String chanaTradeYear = CommonUtils.checkNull(request.getParamValue("chanaTradeYear")) ;
			String tel = CommonUtils.checkNull(request.getParamValue("tel")) ;
			String reason = CommonUtils.checkNull(request.getParamValue("reason")) ;
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")) ;
			String identityNumber = CommonUtils.checkNull(request.getParamValue("identityNumber"));
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("headId", headId) ;
			map.put("dealerId", dealerId) ;
			map.put("salesConName", salesConName) ;
			map.put("sex", sex) ;
			map.put("bornYear", bornYear) ;
			map.put("academicRecords", academicRecords) ;
			map.put("tradeYear", tradeYear) ;
			map.put("chanaTradeYear", chanaTradeYear) ;
			map.put("tel", tel) ;
			map.put("reason", reason) ;
			map.put("userId", logonUser.getUserId().toString()) ;
			map.put("status", Constant.SALES_CONSULTANT_STATUS_RAISE.toString()) ;
			map.put("identityNumber", identityNumber);
			dao.salesConsultantUpdate(map) ;
			
			act.setOutData("subFlag", "success") ;
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问经销商申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问经销商取消操作
	 */
	public void submitCancel() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String headId = CommonUtils.checkNull(request.getParamValue("headId")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("headId", headId) ;
			map.put("userId", logonUser.getUserId().toString()) ;
			map.put("status", Constant.SALES_CONSULTANT_STATUS_CANCEL.toString()) ;
			
			dao.salesConsultantUpdate(map) ;
			
			act.setOutData("subFlag", "success") ;
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问经销商取消");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问经销商查询操作
	 */
	public void dlrQuery() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String areaIds = CommonUtils.checkNull(request.getParamValue("areaIds")) ;
			String salesConName = CommonUtils.checkNull(request.getParamValue("salesConName")) ;
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId")) ;
			String status = CommonUtils.checkNull(request.getParamValue("status")) ;
			String dealerId = logonUser.getDealerId() ;
			
			if(!CommonUtils.isNullString(areaId)) {
				areaIds = areaId ;
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("areaIds", areaIds) ;
			map.put("salesConName", salesConName) ;
			map.put("dealerId", dealerId) ;
			map.put("status", status) ;
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.salesConsultantQuery(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问经销商查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dlrDownload(){
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		ResponseWrapper response = act.getResponse() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.getSession().get(Constant.LOGON_USER);
		
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String areaIds = CommonUtils.checkNull(request.getParamValue("areaIds")) ;
		String salesConName = CommonUtils.checkNull(request.getParamValue("salesConName")) ;
		String areaId = CommonUtils.checkNull(request.getParamValue("areaId")) ;
		String status = CommonUtils.checkNull(request.getParamValue("status")) ;
		String dealerId = logonUser.getDealerId() ;
			
		if(!CommonUtils.isNullString(areaId)) {
			areaIds = areaId ;
		}
			
		Map<String, String> map = new HashMap<String, String>() ;
		map.put("areaIds", areaIds) ;
		map.put("salesConName", salesConName) ;
		map.put("dealerId", dealerId) ;
		map.put("status", status) ;
		
		// 导出的文件名
		String fileName = "销售顾问明细.csv";
		// 导出的文字编码
		OutputStream os = null;
		try {
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
		
		response.setContentType("Application/text/csv");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

		List<List<Object>> list = new LinkedList<List<Object>>();

		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("姓名");
		listTemp.add("身份证号");	//新增身份证号 2012-09-04 韩晓宇
		listTemp.add("性别");
		listTemp.add("年龄");
		listTemp.add("学历");
		listTemp.add("从事汽车行业年份");
		listTemp.add("从事长安汽车行业年份");
		listTemp.add("联系电话");
		listTemp.add("申请原因");
		listTemp.add("状态");
		list.add(listTemp);
		
		List<Map<String, Object>> results = dao.salesConsultantDownload(map) ;
		
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("NAME")));
			listTemp.add("=\"" + CommonUtils.checkNull(record.get("IDENTITY_NUMBER")) + "\"");	 //新增身份证号 2012-09-04 韩晓宇
			listTemp.add(CommonUtils.checkNull(record.get("SEX_DESC")));
			listTemp.add(CommonUtils.checkNull(record.get("AGE")));
			listTemp.add(CommonUtils.checkNull(record.get("AR_DESC")));
			listTemp.add(CommonUtils.checkNull(record.get("TRADEYEAR")));
			listTemp.add(CommonUtils.checkNull(record.get("CHANATRADEYEAR")));
			listTemp.add(CommonUtils.checkNull(record.get("TEL")));
			listTemp.add(CommonUtils.checkNull(record.get("REASON")));
			listTemp.add(CommonUtils.checkNull(record.get("STATUS_DESC")));
			list.add(listTemp);
		}
		os = response.getOutputStream();
		CsvWriterUtil.writeCsv(list, os);		
		os.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	/**
	 * 销售顾问明细查询页面跳转
	 */
	public void dtlQuery() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String headId = CommonUtils.checkNull(request.getParamValue("headId")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("headId", headId) ;
			
			Map<String, Object> dtlMap = dao.salesConsultantDtlQuery(map) ;
			
			List<Map<String, Object>> chkList = dao.salesConsultantChkQuery(map) ;
			
			act.setOutData("dtlMap", dtlMap) ;
			act.setOutData("chkList", chkList) ;
			
			act.setForword(DTL_INIT) ;
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问分销中心审核页面跳转");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问分销中心审核查询操作
	 */
	public void areaCheckQuery() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String areaIds = CommonUtils.checkNull(request.getParamValue("areaIds")) ;
			String salesConName = CommonUtils.checkNull(request.getParamValue("salesConName")) ;
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId")) ;
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")) ;
			String orgId = logonUser.getOrgId().toString() ;
			
			if(!CommonUtils.isNullString(areaId)) {
				areaIds = areaId ;
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("areaIds", areaIds) ;
			map.put("salesConName", salesConName) ;
			map.put("dealerId", dealerId) ;
			map.put("orgId", orgId) ;
			map.put("status", Constant.SALES_CONSULTANT_STATUS_RAISE.toString()) ;
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.salesConsultantQuery(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问组织审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问分销中心审核页面跳转
	 */
	public void areaCheckDtlInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String headId = CommonUtils.checkNull(request.getParamValue("headId")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("headId", headId) ;
			
			Map<String, Object> dtlMap = dao.salesConsultantDtlQuery(map) ;
			
			List<Map<String, Object>> chkList = dao.salesConsultantChkQuery(map) ;
			
			act.setOutData("dtlMap", dtlMap) ;
			act.setOutData("chkList", chkList) ;
			
			act.setForword(AREA_CHECK_DTL_URL) ;
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问分销中心审核页面跳转");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问分销中心审核操作
	 */
	public void areaCheck() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String headId = CommonUtils.checkNull(request.getParamValue("headId")) ;
			String status = CommonUtils.checkNull(request.getParamValue("status")) ;
			String depict = CommonUtils.checkNull(request.getParamValue("desc")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("headId", headId) ;
			map.put("status", status) ;
			map.put("depict", depict) ;
			map.put("userId", logonUser.getUserId().toString()) ;
			
			dao.salesConsultantUpdate(map) ;
			
			dao.salesConsultantChkInsert(map) ;
			
			act.setOutData("subFlag", "success") ;
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问分销中心审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问分销中心批量审核操作
	 */
	public void areaChecks() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String headIdStr = CommonUtils.checkNull(request.getParamValue("heIds")) ;
			String status = CommonUtils.checkNull(request.getParamValue("status")) ;
			String depict = CommonUtils.checkNull(request.getParamValue("descr")) ;
			
			if(!CommonUtils.isNullString(headIdStr)) {
				String[] headIds = headIdStr.split(",") ;
				int len = headIds.length ;
				
				for(int i=0; i<len; i++) {
					Map<String, String> map = new HashMap<String, String>() ;
					map.put("headId", headIds[i]) ;
					map.put("status", status) ;
					map.put("depict", depict) ;
					map.put("userId", logonUser.getUserId().toString()) ;
					
					dao.salesConsultantUpdate(map) ;
					
					dao.salesConsultantChkInsert(map) ;
				}
				
				act.setOutData("subFlag", "success") ;
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问分销中心审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售顾问车厂查询操作
	 */
	public void oemQuery() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String areaIds = CommonUtils.checkNull(request.getParamValue("areaIds")) ;
			String salesConName = CommonUtils.checkNull(request.getParamValue("salesConName")) ;
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId")) ;
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")) ;
			String status = CommonUtils.checkNull(request.getParamValue("status")) ;
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")) ;
			
			String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			if(!CommonUtils.isNullString(areaId)) {
				areaIds = areaId ;
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("areaIds", areaIds) ;
			map.put("salesConName", salesConName) ;
			map.put("dealerId", dealerId) ;
			map.put("status", status) ;
			map.put("orgId", orgId) ;
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.salesConsultantQuery(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售顾问经销商申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void oemDownload(){
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		ResponseWrapper response = act.getResponse() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.getSession().get(Constant.LOGON_USER);
		
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String areaIds = CommonUtils.checkNull(request.getParamValue("areaIds")) ;
		String salesConName = CommonUtils.checkNull(request.getParamValue("salesConName")) ;
		String areaId = CommonUtils.checkNull(request.getParamValue("areaId")) ;
		String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")) ;
		String status = CommonUtils.checkNull(request.getParamValue("status")) ;
		String orgId = CommonUtils.checkNull(request.getParamValue("orgId")) ;
		
		String dutyType = logonUser.getDutyType() ;
		
		if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			orgId = logonUser.getOrgId().toString() ;
		}
		
		if(!CommonUtils.isNullString(areaId)) {
			areaIds = areaId ;
		}
		
		Map<String, String> map = new HashMap<String, String>() ;
		map.put("areaIds", areaIds) ;
		map.put("salesConName", salesConName) ;
		map.put("dealerId", dealerId) ;
		map.put("status", status) ;
		map.put("orgId", orgId) ;
		
		// 导出的文件名
		String fileName = "销售顾问明细.csv";
		// 导出的文字编码
		OutputStream os = null;
		try {
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
		
		response.setContentType("Application/text/csv");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

		List<List<Object>> list = new LinkedList<List<Object>>();

		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("组织");
		listTemp.add("经销商全称");
		listTemp.add("姓名");
		listTemp.add("身份证号");	//新增身份证号 2012-09-04 韩晓宇
		listTemp.add("性别");
		listTemp.add("年龄");
		listTemp.add("学历");
		listTemp.add("从事汽车行业年份");
		listTemp.add("从事长安汽车行业年份");
		listTemp.add("联系电话");
		listTemp.add("申请原因");
		listTemp.add("状态");
		list.add(listTemp);
		
		List<Map<String, Object>> results = dao.salesConsultantDownload(map) ;
		
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("ROOT_ORG_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("NAME")));
			listTemp.add("=\"" + CommonUtils.checkNull(record.get("IDENTITY_NUMBER")) + "\""); //新增身份证号 2012-09-04 韩晓宇
			listTemp.add(CommonUtils.checkNull(record.get("SEX_DESC")));
			listTemp.add(CommonUtils.checkNull(record.get("AGE")));
			listTemp.add(CommonUtils.checkNull(record.get("AR_DESC")));
			listTemp.add(CommonUtils.checkNull(record.get("TRADEYEAR")));
			listTemp.add(CommonUtils.checkNull(record.get("CHANATRADEYEAR")));
			listTemp.add(CommonUtils.checkNull(record.get("TEL")));
			listTemp.add(CommonUtils.checkNull(record.get("REASON")));
			listTemp.add(CommonUtils.checkNull(record.get("STATUS_DESC")));
			list.add(listTemp);
		}
		os = response.getOutputStream();
		CsvWriterUtil.writeCsv(list, os);		
		os.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
