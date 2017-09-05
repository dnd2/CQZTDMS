package com.infodms.dms.actions.sales.fleetmanage.fleetSupport;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CompanyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.fleetmanage.fleetsupport.FleetSupportDao;
import com.infodms.dms.dao.sales.salesConsultant.SalesConsultantDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 集团客户合同查询Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-28
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class FleetContractsQuery {

	public Logger logger = Logger.getLogger(FleetContractsQuery.class);   
	FleetSupportDao dao  = FleetSupportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetContractsQuery.jsp";
	private final String dlrCompayUrl = "/jsp/sales/fleetmanage/fleetSupport/queryDealerCompany.jsp";
	private final String detailInfoUrl = "/jsp/sales/fleetmanage/fleetSupport/contractsMaintainInfo.jsp";

	/**
	 * 集团客户合同查询页面初始化
	 */
	public void fleetContractsQueryInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			act.setOutData("sysFlag", para) ;
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同查询页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商公司查询初始化
	 */
	public void queryCompany() throws Exception{
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(dlrCompayUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商公司查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商公司查询
	 */
	public void queryCom(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String companyCode = request.getParamValue("companyCode");
			String companyName = request.getParamValue("companyName");
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<CompanyBean> ps = dao.selectCompany(companyCode, companyName, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商公司查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户合同查询
	 */
	public void fleetContractsQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = request.getParamValue("fleetName");	//客户名称
			String fleetType = request.getParamValue("fleetType");	//客户类型
			String startDate = request.getParamValue("startDate");	//起始时间
			String endDate = request.getParamValue("endDate");		//结束时间
			String checkSDate = request.getParamValue("checkSDate");//签订开始时间
			String checkEDate = request.getParamValue("checkEDate");//签订结束时间
			String companyId = request.getParamValue("companyId");	//经销商公司ID
			String contractNo = request.getParamValue("contractNo");//合同编号
			String fleetStatus = request.getParamValue("fleetStatus");
			String auditSDate =request.getParamValue("auditSDate"); //审核开始日期
			String auditEDate = request.getParamValue("auditEDate");//审核结束日期
			String dutyType = logonUser.getDutyType();
			String oemCompanyId = String.valueOf(GetOemcompanyId.getOemCompanyId(logonUser));
			String orgId = String.valueOf(logonUser.getOrgId());
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.fleetContractsQuery(auditSDate,auditEDate,dutyType, oemCompanyId, orgId, fleetStatus, checkSDate, checkEDate, fleetName, fleetType, startDate, endDate, companyId, contractNo,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 集团客户明细查询
	 */
	public void fleetContractsInfo(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");	//客户Id
			String intentId = request.getParamValue("intentId");	//意向Id
			String contractId=request.getParamValue("contractId");//合同Id
			Map<String, Object> map = dao.getFleetInfobyId(fleetId);
			//Map<String, Object> map2 = dao.getFleetIntentInfobyId(fleetId);
			Map<String, Object> cmap = dao.getContractInfobyId(fleetId, intentId,contractId);
			List<Map<String, Object>> list1 = dao.getFleetIntentDetailInfobyId(fleetId, intentId,contractId);
			List<Map<String, Object>> list2 = dao.getFleetIntentCheckInfobyId(fleetId, intentId);
			String id = String.valueOf(cmap.get("CONTRACT_ID"));
			List<Map<String, Object>> fileList = dao.queryAttachFileInfo(id);//查询附件
			act.setOutData("fileFlag", fileList.size());
			act.setOutData("fileFlag", fileList.size());
			act.setOutData("intentList", list1);
			//act.setOutData("intentMap", map2);
			//act.setOutData("intentId", intentId);
			act.setOutData("checkList", list2);
			act.setOutData("cmap", cmap);
			act.setOutData("fleetMap", map);
			act.setOutData("fileList", fileList);
			act.setForword(detailInfoUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void contractsDownload() {
		AclUserBean logonUser = null ;
		OutputStream os = null;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
			String fleetName = request.getParamValue("fleetName");	//客户名称
			String fleetType = request.getParamValue("fleetType");	//客户类型
			String startDate = request.getParamValue("startDate");	//起始时间
			String endDate = request.getParamValue("endDate");		//结束时间
			String checkSDate = request.getParamValue("checkSDate");//签订开始时间
			String checkEDate = request.getParamValue("checkEDate");//签订结束时间
			String companyId = request.getParamValue("companyId");	//经销商公司ID
			String contractNo = request.getParamValue("contractNo");//合同编号
			String fleetStatus = request.getParamValue("fleetStatus");
			String auditSDate =request.getParamValue("auditSDate"); //审核开始日期
			String auditEDate = request.getParamValue("auditEDate");//审核结束日期
			
			String dutyType = logonUser.getDutyType();
			String orgId = null ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = String.valueOf(logonUser.getOrgId());
			}
			
			
			Map<String,String> map = new HashMap<String, String>() ;
			map.put("fleetName", fleetName) ;
			map.put("fleetType", fleetType) ;
			map.put("startDate", startDate) ;
			map.put("endDate", endDate) ;
			map.put("checkSDate", checkSDate) ;
			map.put("checkEDate", checkEDate) ;
			map.put("companyId", companyId) ;
			map.put("contractNo", contractNo) ;
			map.put("fleetStatus", fleetStatus) ;
			map.put("auditSDate", auditSDate) ;
			map.put("auditEDate", auditEDate) ;
			map.put("orgId", orgId) ;
			
			
			String fileName = "集团客户合同明细.xls" ;
			
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1") ;
			ResponseWrapper response = act.getResponse() ;
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("合同编号");
			listTemp.add("区域");
			listTemp.add("渠道（A网/B网）");
			listTemp.add("申请单位");
			listTemp.add("客户名称");
			listTemp.add("客户类型");
			listTemp.add("主要联系人");
			listTemp.add("主要联系人电话");
			listTemp.add("合同签订日期");
			listTemp.add("签约车系");
			listTemp.add("签约数量");
			listTemp.add("有效期");
			listTemp.add("支持点位");
			list.add(listTemp);
			
			List<Map<String, Object>> results = dao.contractsDownload(map) ;
			
			if(!CommonUtils.isNullList(results)) {
				int len = results.size() ;
				
				for(int i=0; i<len; i++) {
					Map<String, Object> record = results.get(i);
					listTemp = new LinkedList<Object>();
					listTemp.add(CommonUtils.checkNull(record.get("CONTRACT_NO")));
					listTemp.add(CommonUtils.checkNull(record.get("ROOT_ORG_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("AREA_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("COMPANY_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("FLEET_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("CODE_DESC")));
					listTemp.add(CommonUtils.checkNull(record.get("MAIN_LINKMAN")));
					listTemp.add(CommonUtils.checkNull(record.get("MAIN_PHONE")));
					listTemp.add(CommonUtils.checkNull(record.get("CHECK_DATE")));
					listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("INTENT_COUNT")));
					listTemp.add(CommonUtils.checkNull(record.get("STATUS_DATE")));
					listTemp.add(CommonUtils.checkNull(record.get("INTENT_POINT")));
					list.add(listTemp);
				}
			}
			
			os = response.getOutputStream();
			//CsvWriterUtil.writeCsv(list, os);
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合同明细下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
