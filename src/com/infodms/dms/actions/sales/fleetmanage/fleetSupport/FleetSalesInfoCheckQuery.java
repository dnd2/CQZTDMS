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
import com.infodms.dms.common.GetCommonType;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.fleetmanage.fleetsupport.FleetSupportDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 集团客户实销信息审核查询Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-28
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class FleetSalesInfoCheckQuery {
	
	public Logger logger = Logger.getLogger(FleetSalesInfoCheckQuery.class);   
	FleetSupportDao dao  = FleetSupportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	ResponseWrapper response = act.getResponse();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSalesInfoCheckQuery.jsp";
	private final String dlrCompayUrl = "/jsp/sales/fleetmanage/fleetSupport/queryDealerCompany.jsp";
	
	/**
	 * 集团客户实销信息审核查询页面初始化
	 */
	public void fleetSalesInfoCheckQueryInit(){
		
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户实销信息审核查询页面初始化");
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
//	/**
//	 * 集团客户实销信息审核查询
//	 */
//	public void fleetSalesInfoCheckQuery(){
//		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
//		try {
//			String fleetName = request.getParamValue("fleetName");	//客户名称
//			String fleetType = request.getParamValue("fleetType");	//客户类型
//			String startDate = request.getParamValue("startDate");	//起始时间
//			String endDate = request.getParamValue("endDate");		//结束时间
//			String logsdate = CommonUtils.checkNull(request.getParamValue("logsdate"));
//			String logedate = CommonUtils.checkNull(request.getParamValue("logedate"));
//			String companyId = request.getParamValue("companyId");	//经销商公司ID
//			String groupCode = request.getParamValue("groupCode");	//物料代码
//			String contractNo = request.getParamValue("contractNo");//合同编号
//			String vin = request.getParamValue("vin");				//底盘号
//			String checkStatus = request.getParamValue("checkStatus");//审核状态
//			String dutyType = logonUser.getDutyType();
//			String orgId = String.valueOf(logonUser.getOrgId());
//			String oemCompanyId = String.valueOf(GetOemcompanyId.getOemCompanyId(logonUser));
//			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
//			PageResult<Map<String, Object>> ps = dao.fleetSalesInfoCheckQueryList(dutyType, orgId, oemCompanyId, logsdate, logedate, fleetName, fleetType, startDate, endDate, companyId, groupCode, contractNo, vin,checkStatus, curPage, Constant.PAGE_SIZE);
//			act.setOutData("ps", ps);
//		}catch(Exception e) {//异常方法
//			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户实销信息审核查询");
//			logger.error(logonUser,e1);
//			act.setException(e1);
//		}
//	}
	
	
	/**
	 * 集团客户实销信息审核查询
	 */
	public void fleetSalesInfoCheckQuery(){
		AclUserBean logonUser = null;
		try {
			
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String orgId = String.valueOf(logonUser.getOrgId());
			TmOrgPO po=new TmOrgPO();
			po.setOrgId(Long.parseLong(orgId));
			po.setDutyType(Constant.DUTY_TYPE_LARGEREGION);
			if(dao.select(po).size()!=1){
				orgId="";
			}
			String checkStatus=CommonUtils.checkNull(request.getParamValue("checkStatus"));//审核状态
			String orgCode=CommonUtils.checkNull(request.getParamValue("orgCode"));					//选择区域
			String customer_type = CommonUtils.checkNull(request.getParamValue("fleetType"));	//客户类型
			String customer_name = CommonUtils.checkNull(request.getParamValue("fleetName"));	//客户名称
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));						//VIN
			String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone"));	//客户电话
			String fleet_name = CommonUtils.checkNull(request.getParamValue("fleetName"));			//集团客户名称
			String materialCode = CommonUtils.checkNull(request.getParamValue("groupCode"));		//选择物料组
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet"));				//是否集团客户
			String contract_no = CommonUtils.checkNull(request.getParamValue("contractNo"));		//集团客户合同
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));			//上报日期:开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));				//上报日期 ：截止
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));			//经销商代码
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String companyId=CommonUtils.checkNull(request.getParamValue("companyId"));
			String logsdate=CommonUtils.checkNull(request.getParamValue("logsdate"));
			String logedate=CommonUtils.checkNull(request.getParamValue("logedate"));
			StringBuffer areaIds = new StringBuffer();
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			for (int i = 0; i < areaBusList.size(); i++) {
				if (!"".equals(String.valueOf(areaBusList.get(i).get("AREA_ID")))) {
					areaIds.append(String.valueOf(areaBusList.get(i).get("AREA_ID")));
				}
				if (i<areaBusList.size()-1) {
					areaIds.append(",");
				}
			}
			String lastAreaId = "";
			if (!"".equals(areaId)) {
				lastAreaId = areaId;
			}else{
				lastAreaId = areaIds.toString();
			}
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			Map<String,String> map = new HashMap<String, String>();
			map.put("orgCode", orgCode);
			map.put("customer_type", customer_type);
			map.put("customer_name", customer_name);
			map.put("vin", vin);
			map.put("customer_phone", customer_phone);
			map.put("fleet_name", fleet_name);
			map.put("materialCode", materialCode);
			map.put("is_fleet", is_fleet);
			map.put("contract_no", contract_no);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			map.put("areaId", lastAreaId);
			map.put("checkStatus", checkStatus);
			map.put("companyId", companyId);
			map.put("logsdate", logsdate);
			map.put("logedate", logedate);
			map.put("orgId", orgId);
			PageResult<Map<String, Object>> ps = dao.queryReportInfoList(map,curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息查询失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	/**
	 * 实销信息模板下载
	 * @author zouchao
	 * @since  2010-08-20
	 */
	public void downloadTemple(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String orgId = String.valueOf(logonUser.getOrgId());
			TmOrgPO po=new TmOrgPO();
			po.setOrgId(Long.parseLong(orgId));
			po.setDutyType(Constant.DUTY_TYPE_LARGEREGION);
			if(dao.select(po).size()!=1){
				orgId="";
			}
			String checkStatus=CommonUtils.checkNull(request.getParamValue("checkStatus"));//审核状态
			String orgCode=CommonUtils.checkNull(request.getParamValue("orgCode"));					//选择区域
			String customer_type = CommonUtils.checkNull(request.getParamValue("fleetType"));	//客户类型
			String customer_name = CommonUtils.checkNull(request.getParamValue("fleetName"));	//客户名称
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));						//VIN
			String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone"));	//客户电话
			String fleet_name = CommonUtils.checkNull(request.getParamValue("fleetName"));			//集团客户名称
			String materialCode = CommonUtils.checkNull(request.getParamValue("groupCode"));		//选择物料组
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet"));				//是否集团客户
			String contract_no = CommonUtils.checkNull(request.getParamValue("contractNo"));		//集团客户合同
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));			//上报日期:开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));				//上报日期 ：截止
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));			//经销商代码
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String companyId=CommonUtils.checkNull(request.getParamValue("companyId"));
			String logsdate=CommonUtils.checkNull(request.getParamValue("logsdate"));
			String logedate=CommonUtils.checkNull(request.getParamValue("logedate"));
			StringBuffer areaIds = new StringBuffer();
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			for (int i = 0; i < areaBusList.size(); i++) {
				if (!"".equals(String.valueOf(areaBusList.get(i).get("AREA_ID")))) {
					areaIds.append(String.valueOf(areaBusList.get(i).get("AREA_ID")));
				}
				if (i<areaBusList.size()-1) {
					areaIds.append(",");
				}
			}
			String lastAreaId = "";
			if (!"".equals(areaId)) {
				lastAreaId = areaId;
			}else{
				lastAreaId = areaIds.toString();
			}
			
			Map<String,String> map = new HashMap<String, String>();
			map.put("orgCode", orgCode);
			map.put("customer_type", customer_type);
			map.put("customer_name", customer_name);
			map.put("vin", vin);
			map.put("customer_phone", customer_phone);
			map.put("fleet_name", fleet_name);
			map.put("materialCode", materialCode);
			map.put("is_fleet", is_fleet);
			map.put("contract_no", contract_no);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			map.put("areaId", lastAreaId);
			map.put("checkStatus", checkStatus);
			map.put("companyId", companyId);
			map.put("logsdate", logsdate);
			map.put("logedate", logedate);
			map.put("orgId", orgId);
			// 导出的文件名
			String fileName = "实销信息明细.csv";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
	
			List<List<Object>> list = new LinkedList<List<Object>>();
	
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("区域");
			listTemp.add("省份");
			listTemp.add("市");
			listTemp.add("县");
			listTemp.add("渠道");
			listTemp.add("销售日期");
			listTemp.add("生产日期");
			listTemp.add("启票日期");
			listTemp.add("上级单位");
			listTemp.add("销售单位");
			listTemp.add("车主");
			listTemp.add("联系电话");
			listTemp.add("客户名称");
			listTemp.add("一级客户类型");
			listTemp.add("二级客户类型");
			listTemp.add("合同编号");
			listTemp.add("车型类别");
			listTemp.add("车型系列");
			listTemp.add("车型编码");
			listTemp.add("车型配置");
			listTemp.add("状态");
			listTemp.add("审核人");		//2012-08-20 新增下载时增加审批人字段 韩晓宇
			listTemp.add("审核状态");
			listTemp.add("颜色");
			listTemp.add("底盘号");
			list.add(listTemp);
			List<Map<String, Object>> results = dao.queryReportInfoExportList(map);
			
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("ROOT_ORG_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("REGION_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("CITY_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("TOWN")));
				listTemp.add(CommonUtils.checkNull(record.get("AREA_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("SALES_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("PRODUCT_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("INVOICE_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("ROOT_DEALER_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
				listTemp.add(CommonUtils.checkNull(record.get("CTM_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MAIN_PHONE")));
				listTemp.add(CommonUtils.checkNull(record.get("FLEET_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("PARTFLEET_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("FLEET_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("CONTRACT_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("SERNAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MODNAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MODCODE")));
				listTemp.add(CommonUtils.checkNull(record.get("PACKNAME")));
				listTemp.add(CommonUtils.checkNull(record.get("GROUPSTATUS")));
				listTemp.add(CommonUtils.checkNull(record.get("AUDITNAME")));
				listTemp.add(CommonUtils.checkNull(record.get("FLEETSTATUSNAME")));
				listTemp.add(CommonUtils.checkNull(record.get("COLOR")));
				listTemp.add(CommonUtils.checkNull(record.get("VIN")));
				list.add(listTemp);
			}
			OutputStream os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息查询下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	
}
