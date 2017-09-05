package com.infodms.dms.actions.sales.salesInfoManage;

import java.io.OutputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.salesInfoManage.SalesInfoQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : SalesInfoQuery.java
 * @Package: com.infodms.dms.actions.sales.salesInfoManage
 * @Description:车厂端：实销信息查询
 * @date   : 2010-6-30 
 * @version: V1.0   
 */
@SuppressWarnings("rawtypes")
public class SalesInfoQuery  extends BaseDao{
	public Logger logger = Logger.getLogger(SalesInfoQuery.class);
	private ActionContext act = ActionContext.getContext();
	ResponseWrapper response = act.getResponse();
	private static final SalesInfoQuery dao = new SalesInfoQuery ();
	private final SalesInfoQueryDAO queryDao = SalesInfoQueryDAO.getInstance();
	public static final SalesInfoQuery getInstance() {
		return dao;
	}
	RequestWrapper request = act.getRequest();
	private final String  salesInfoQueryInit = "/jsp/sales/salesInfoManage/salesInfoQueryInit.jsp";
	private final String  salesInfoQueryInit_CVS = "/jsp/sales/salesInfoManage/salesInfoQueryInit_CVS.jsp";
	
	
	
	/** 
	* @Title	  : salesInfoQueryInit 
	* @Description: 实销信息查询页面初始化OEM
	* @throws 
	* @LastUpdate :2010-6-30
	*/
	public void salesInfoQueryInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			Date date_ = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setOutData("date", date);
            act.setOutData("dutyType", logonUser.getDutyType());
            act.setOutData("orgId", logonUser.getOrgId());
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				act.setForword(salesInfoQueryInit);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				act.setForword(salesInfoQueryInit_CVS);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息查询页面初始化OEM");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : salesInfoQuery 
	* @Description: 实销信息查询结果展示
	* @throws 
	* @LastUpdate :2010-6-30
	*/
	public void salesInfoQuery(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			//String orgCode=CommonUtils.checkNull(request.getParamValue("orgCode"));					//选择区域
			String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type"));	//客户类型
			String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name"));	//客户名称
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));						//VIN
			String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone"));	//客户电话
			//String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name"));			//集团客户名称
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));		//选择物料组
			//String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet"));				//是否集团客户
			//String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no"));		//集团客户合同
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));			//上报日期:开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));				//上报日期 ：截止
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));			//经销商代码
			//String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String linkAddr=CommonUtils.checkNull(request.getParamValue("linkAddr"));//联系地址
			String payment=CommonUtils.checkNull(request.getParamValue("payment"));//购置方式
			//String bank=CommonUtils.checkNull(request.getParamValue("bank"));//按揭银行
			//String loansYear=CommonUtils.checkNull(request.getParamValue("loansYear"));//贷款年限
			String vchlPro=CommonUtils.checkNull(request.getParamValue("vchlPro"));//车辆性质
			//String beStatus=CommonUtils.checkNull(request.getParamValue("be_status"));//转介绍类型
			String orgId = "" ;
			String poseId = "";
			String dutyType = logonUser.getDutyType() ;
			orgId = logonUser.getOrgId().toString() ;
			poseId = logonUser.getPoseId().toString();
//			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)||Constant.DUTY_TYPE_SMALLREGION.toString().equals(dutyType)) {
//			}
			/*StringBuffer areaIds = new StringBuffer();
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(poseId);
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
			}*/
			
			Map<String,String> map = new HashMap<String, String>();
			map.put("orgId", orgId);
			map.put("poseId", poseId);
			map.put("dutyType", dutyType);
			//map.put("orgCode", orgCode);
			map.put("customer_type", customer_type);
			map.put("customer_name", customer_name);
			map.put("vin", vin);
			map.put("customer_phone", customer_phone);
			//map.put("fleet_name", fleet_name);
			map.put("materialCode", materialCode);
			//map.put("is_fleet", is_fleet);
			//map.put("contract_no", contract_no);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			//map.put("areaId", lastAreaId);
			map.put("linkAddr", linkAddr);
			map.put("payment", payment);
			//map.put("bank",bank);
			//map.put("loansYear", loansYear);
			map.put("vchlPro", vchlPro);
			//map.put("beStatus", beStatus);
			map.put("dealerSql", dao.getDealerIdByPostSql(logonUser));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			PageResult<Map<String, Object>> ps = null ;
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				ps = SalesInfoQueryDAO.queryReportInfoTableList(map, Constant.PAGE_SIZE, curPage);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				//无微车
				// ps = SalesInfoQueryDAO.queryReportInfoList_CVS(map, Constant.PAGE_SIZE, curPage);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息查询结果展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void cusDownLoad(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String orgCode=CommonUtils.checkNull(request.getParamValue("orgCode"));					//选择区域
			String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type"));	//客户类型
			String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name"));	//客户名称
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));						//VIN
			String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone"));	//客户电话
			String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name"));			//集团客户名称
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));		//选择物料组
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet"));				//是否集团客户
			String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no"));		//集团客户合同
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));			//上报日期:开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));				//上报日期 ：截止
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));			//经销商代码
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String linkAddr=CommonUtils.checkNull(request.getParamValue("linkAddr"));//联系地址
			String payment=CommonUtils.checkNull(request.getParamValue("payment"));//购置方式
			String bank=CommonUtils.checkNull(request.getParamValue("bank"));//按揭银行
			String loansYear=CommonUtils.checkNull(request.getParamValue("loansYear"));//贷款年限
			String beStatus=CommonUtils.checkNull(request.getParamValue("be_status"));//转介绍类型
			String vchlPro=CommonUtils.checkNull(request.getParamValue("vchlPro"));//车辆性质
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			orgId = logonUser.getOrgId().toString() ;
//			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
//			}
			
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
			map.put("orgId", orgId);
			map.put("dutyType", dutyType);
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
			map.put("linkAddr", linkAddr);
			map.put("payment", payment);
			map.put("bank",bank);
			map.put("loansYear", loansYear);
			map.put("beStatus", beStatus);
			map.put("vchlPro", vchlPro);
			map.put("dealerSql", dao.getDealerIdByPostSql(logonUser));

			// 导出的文件名
			String fileName = "实销信息明细.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
			List<List<Object>> list = new LinkedList<List<Object>>();
	
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("区域");
			listTemp.add("省份");
			listTemp.add("经销商代码");
			listTemp.add("经销商名称");
			//listTemp.add("系统开通时间");
			listTemp.add("客户名称");
			listTemp.add("客户类型");
			listTemp.add("是否集团客户");
			listTemp.add("集团客户名称");
			listTemp.add("主要联系电话");
			listTemp.add("车系名称");
			listTemp.add("车型名称");
			listTemp.add("状态名称");
			listTemp.add("颜色");
			listTemp.add("物料代码");
			/*listTemp.add("物料名称");*/
			listTemp.add("VIN");
			listTemp.add("发动机号");
			listTemp.add("车辆性质");
			listTemp.add("实销时间");
			listTemp.add("购置方式");
			listTemp.add("按揭类型");
			listTemp.add("销售价格");
			listTemp.add("贷款银行");
			//listTemp.add("首付比例");
			//listTemp.add("贷款方式");
			listTemp.add("贷款年限");
			listTemp.add("贷款金额");
			listTemp.add("老客户姓名");
			listTemp.add("情报/老客户电话");
			listTemp.add("情报/老客户车架号");
			
			list.add(listTemp);
			List<Map<String, Object>> results = queryDao.queryReportInfoTableDownLoad(map);
			
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("REGION_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
				//listTemp.add(formatDate(CommonUtils.checkNull(record.get("DEALER_CREATE_DATE"))).trim());
				listTemp.add(CommonUtils.checkNull(record.get("CTM_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("CTM_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("IS_FLEET")));
				listTemp.add(CommonUtils.checkNull(record.get("FLEET_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MAIN_PHONE")));
				listTemp.add(CommonUtils.checkNull(record.get("VS")));
				listTemp.add(CommonUtils.checkNull(record.get("VT")));
				String str=CommonUtils.checkNull(record.get("SN"));
				String SN=str.replaceAll(",","，");
				listTemp.add(SN);
				listTemp.add(CommonUtils.checkNull(record.get("COLOR")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				/*listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_NAME")));*/
				listTemp.add(CommonUtils.checkNull(record.get("VIN")));
				listTemp.add(CommonUtils.checkNull(record.get("ENGINE_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("CODE_DESC")));
				listTemp.add(CommonUtils.checkNull(record.get("SALES_DATE")));
				
				listTemp.add(CommonUtils.checkNull(record.get("PAYMENT")));
				listTemp.add(CommonUtils.checkNull(record.get("MORTGAGE_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("SALES_PRICE")));
				listTemp.add(CommonUtils.checkNull(record.get("BANK")));
//				if(record.get("SHOUFU_RATIO")==null){
//					listTemp.add(CommonUtils.checkNull(""));
//				}
//				else{
//					listTemp.add(CommonUtils.checkNull(record.get("SHOUFU_RATIO")+"%"));
//				}
				//listTemp.add(CommonUtils.checkNull(record.get("LOANS_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("LOANS_YEAR")));
				listTemp.add(CommonUtils.checkNull(record.get("MONEY")));
				listTemp.add(CommonUtils.checkNull(record.get("OLD_CUSTOMER_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("OLD_TELEPHONE")));
				listTemp.add(CommonUtils.checkNull(record.get("OLD_VEHICLE_ID")));
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
	
	public void cusDownLoad_CVS(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String orgCode=CommonUtils.checkNull(request.getParamValue("orgCode"));					//选择区域
			String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type"));	//客户类型
			String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name"));	//客户名称
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));						//VIN
			String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone"));	//客户电话
			String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name"));			//集团客户名称
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));		//选择物料组
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet"));				//是否集团客户
			String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no"));		//集团客户合同
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));			//上报日期:开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));				//上报日期 ：截止
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));			//经销商代码
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
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
			map.put("orgId", orgId);
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
			listTemp.add("经销商代码");
			listTemp.add("经销商名称");
			listTemp.add("系统开通时间");
			listTemp.add("客户名称");
			listTemp.add("客户类型");
			listTemp.add("是否大客户");
			listTemp.add("大客户名称");
			listTemp.add("主要联系电话");
			listTemp.add("销售顾问");
			listTemp.add("物料代码");
			listTemp.add("物料名称");
			listTemp.add("VIN");
			listTemp.add("发动机号");
			listTemp.add("车辆性质");
			listTemp.add("实销时间");
			list.add(listTemp);
			List<Map<String, Object>> results = queryDao.queryReportInfoExportList_CVS(map);
			
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("REGION_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
				listTemp.add(formatDate(CommonUtils.checkNull(record.get("DEALER_CREATE_DATE"))).trim());
				listTemp.add(CommonUtils.checkNull(record.get("CTM_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("CTM_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("IS_FLEET")));
				listTemp.add(CommonUtils.checkNull(record.get("FLEET_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MAIN_PHONE")));
				listTemp.add(CommonUtils.checkNull(record.get("SALES_CON_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("VIN")));
				listTemp.add(CommonUtils.checkNull(record.get("ENGINE_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("CODE_DESC")));
				listTemp.add(CommonUtils.checkNull(record.get("SALES_DATE")));
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
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	 public String formatDate(String code){
		  if(code!=null&&code!=""){
			  return code.substring(0,7).replace("-","");
		  }
		  return "";
	  }
}
