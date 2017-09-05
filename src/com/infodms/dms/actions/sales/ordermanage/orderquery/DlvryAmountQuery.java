/**
 * 发运单数量统计
 * */
package com.infodms.dms.actions.sales.ordermanage.orderquery;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.anotherbigidea.io.OutStream;
import com.infodms.dms.base.OrgBase;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.DlvryAmountQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DlvryAmountQuery  extends BaseDao{

	public Logger logger = Logger.getLogger(DlvryAmountQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final DlvryAmountQueryDao dao = new DlvryAmountQueryDao ();
	public static final DlvryAmountQueryDao getInstance() {
		return dao;
	}
	private final String  amountQueryInitUrl = "/jsp/sales/ordermanage/orderquery/amountQueryInit.jsp";
	
	public void amountQueryInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR); 
			int month = calendar.get(Calendar.MONTH)+1; 
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String startDate = year+"-"+month+"-"+"1";
			String endDate = year+"-"+month+"-"+day;
			
			Long orgId = logonUser.getOrgId() ;
			String dutyType = logonUser.getDutyType() ;
			
			List<Map<String, Object>> orgList = OrderDeliveryDao.getOemOrg(dutyType, orgId) ;
			
			act.setOutData("orgList", orgList) ;
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setOutData("areaList", areaList);
			act.setForword(amountQueryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "发运单数量统计页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void amountQueryList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String orderOrgCode = CommonUtils.checkNull(request.getParamValue("orderOrgCode"));//定货方经销商
			String billingOrgCode = CommonUtils.checkNull(request.getParamValue("billingOrgCode"));//开票方经销商
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			
			String beginDate = CommonUtils.checkNull(request.getParamValue("beginDate"));
			String overDate = CommonUtils.checkNull(request.getParamValue("overDate"));
			
			String deliveryStatus = CommonUtils.checkNull(request.getParamValue("deliveryStatus"));
			
			String regionId = CommonUtils.checkNull(request.getParamValue("downtown"));		// 省份
			String pageOrgId = CommonUtils.checkNull(request.getParamValue("orgId"));		// 事业部
			
			String dutyType = logonUser.getDutyType() ;
			Long orgId = logonUser.getOrgId() ;
			
			if(CommonUtils.isNullString(areaId)) {
				String poseId = logonUser.getPoseId().toString() ;
				List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId);
				
//				ActivitiesCamCost acc = new ActivitiesCamCost() ;
//				areaId = acc.getAreaStr(areaList) ;
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getAmountQueryList(regionId, dutyType, pageOrgId, orgId, deliveryStatus,areaId,orderOrgCode, billingOrgCode, startDate, endDate, beginDate, overDate, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "发运单数量统计显示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void amountQueryListDownLoad(){
		AclUserBean logonUser = null;
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();
			Map map = new HashMap();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String orderOrgCode = CommonUtils.checkNull(request.getParamValue("orderOrgCode"));//定货方经销商
			String billingOrgCode = CommonUtils.checkNull(request.getParamValue("billingOrgCode"));//开票方经销商
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String beginDate = CommonUtils.checkNull(request.getParamValue("beginDate"));
			String overDate = CommonUtils.checkNull(request.getParamValue("overDate"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String deliveryStatus = CommonUtils.checkNull(request.getParamValue("deliveryStatus"));
			String regionId = CommonUtils.checkNull(request.getParamValue("downtown"));		// 省份
			String pageOrgId = CommonUtils.checkNull(request.getParamValue("orgId"));		// 事业部
			
			String dutyType = logonUser.getDutyType() ;
			Long orgId = logonUser.getOrgId() ;
			
			if(CommonUtils.isNullString(areaId)) {
				String poseId = logonUser.getPoseId().toString() ;
				List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId);
				
//				ActivitiesCamCost acc = new ActivitiesCamCost() ;
//				areaId = acc.getAreaStr(areaList) ;
			}
			
			// 导出的文件名
			String fileName = "发运指令汇总查询.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 定义一个集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			// 标题
			List<Object> listTemp = new LinkedList<Object>();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getAmountQueryList(regionId, dutyType, pageOrgId, orgId, deliveryStatus,areaId,orderOrgCode, billingOrgCode, startDate, endDate, beginDate, overDate, 99999, curPage);
			listTemp.add("业务范围");
			listTemp.add("开票经销商区域");
			listTemp.add("开票经销商代码");
			listTemp.add("开票经销商名称");
			listTemp.add("开票方系统开通时间");
			listTemp.add("订货经销商代码");
			listTemp.add("订货经销商名称");
			listTemp.add("订货方系统开通时间");
			listTemp.add("状态代码");
			listTemp.add("发运时间");
			listTemp.add("待财务确认");
			listTemp.add("ERP待导入");
			listTemp.add("销售订单生成");
			listTemp.add("已开票");
			listTemp.add("已出库");
			listTemp.add("已验收");
			list.add(listTemp);
			List<Map<String, Object>> rslist = ps.getRecords();
			if(rslist!=null){
			for (int i = 0; i < rslist.size(); i++) {
				map = rslist.get(i);
				List<Object> listValue = new LinkedList<Object>();
				listValue = new LinkedList<Object>();
				listValue.add(map.get("AREA_ID") != null ? map.get("AREA_ID") : "");
				listValue.add(map.get("ORG_NAME") != null ? map.get("ORG_NAME") : "");
				listValue.add(map.get("BILLINGORGCODE") != null ? map.get("BILLINGORGCODE") : "");
				listValue.add(map.get("BILLINGORGNAME") != null ? map.get("BILLINGORGNAME") : "");
				listValue.add(map.get("CREATE_DATE") != null ? formatDate(map.get("CREATE_DATE").toString()).trim() : "");
				listValue.add(map.get("ORDERORGCODE") != null ? map.get("ORDERORGCODE") : "");
				listValue.add(map.get("ORDERORGNAME") != null ? map.get("ORDERORGNAME") : "");
				listValue.add(map.get("ORDER_CREATE_DATE") != null ? formatDate(map.get("ORDER_CREATE_DATE").toString()).trim() : "");
				listValue.add(map.get("STATUSCODE") != null ? map.get("STATUSCODE") : "");
				listValue.add(map.get("DELIVERY_DATE") != null ? map.get("DELIVERY_DATE") : "");
				listValue.add(map.get("FINANCE") != null ? map.get("FINANCE") : "");
				listValue.add(map.get("ERP") != null ? map.get("ERP") : "");
				listValue.add(map.get("ORDERSALES") != null ? map.get("ORDERSALES") : "");
				listValue.add(map.get("HASBILL") != null ? map.get("HASBILL") : "");
				listValue.add(map.get("HASOUT") != null ? map.get("HASOUT") : "");
				listValue.add(map.get("HASCHECK") != null ? map.get("HASCHECK") : "");
				list.add(listValue);
			}
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "发运单数量统计:下载");
			logger.error(logonUser,e1);
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
	
	public void dlvryDtlDownload() {
		AclUserBean logonUser = null ;
		ResponseWrapper response = null ;
		OutputStream os = null ;
		
		try{
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
			
			String orderDlrCode = this.formatDlrCode(request.getParamValue("orderOrgCode"));//定货方经销商
			String billingDlrCode = this.formatDlrCode(request.getParamValue("billingOrgCode")) ;//开票方经销商
			String dlvryStartDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String dlvryEndDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String billingStartDate = CommonUtils.checkNull(request.getParamValue("beginDate"));
			String billingEndDate = CommonUtils.checkNull(request.getParamValue("overDate"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String regionCode = CommonUtils.checkNull(request.getParamValue("downtown"));		// 省份
			
			OrgBase ob = new OrgBase() ;
			
			String orgId = ob.getOrgIdByDuty(logonUser) ;		// 事业部
			
			if(CommonUtils.isNullString(orgId)) {
				orgId = CommonUtils.checkNull(request.getParamValue("orgId")); 
			}
			
			if(CommonUtils.isNullString(areaId)) {
				String poseId = logonUser.getPoseId().toString() ;
				List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId);
				
//				ActivitiesCamCost acc = new ActivitiesCamCost() ;
//				areaId = acc.getAreaStr(areaList) ;
			}
			
			String deliveryStatus = new StringBuffer("")
					.append(Constant.DELIVERY_STATUS_01).append(",")
					.append(Constant.DELIVERY_STATUS_04).append(",")
					.append(Constant.DELIVERY_STATUS_05).append(",")
					.append(Constant.DELIVERY_STATUS_06).append(",")
					.append(Constant.DELIVERY_STATUS_07).append(",")
					.append(Constant.DELIVERY_STATUS_10).append(",")
					.append(Constant.DELIVERY_STATUS_11).append(",")
					.append(Constant.DELIVERY_STATUS_12).toString();
			
			String fileName = "发运指令汇总明细下载.xls" ;
			
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1") ;
			
			response = act.getResponse() ;
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("orderDlrCode", orderDlrCode) ;
			map.put("billingDlrCode", billingDlrCode) ;
			map.put("billingStartDate", billingStartDate) ;
			map.put("billingEndDate", billingEndDate) ;
			map.put("dlvryStartDate", dlvryStartDate) ;
			map.put("dlvryEndDate", dlvryEndDate) ;
			map.put("areaId", areaId) ;
			map.put("regionCode", regionCode) ;
			map.put("orgId", orgId) ;
			map.put("deliveryStatus", deliveryStatus) ;
			
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("业务范围");
			listHead.add("开票经销商区域");
			listHead.add("开票经销商名称");
			listHead.add("开票经销商代码");
			listHead.add("开票方系统开通时间");
			listHead.add("订货经销商代码");
			listHead.add("订货经销商名称");
			listHead.add("订货方系统开通时间");
			listHead.add("发运申请单号");
			listHead.add("ERP订单号");
			listHead.add("订单类型");
			listHead.add("物料编码");
			listHead.add("发运时间");
			listHead.add("待财务确认");
			listHead.add("ERP待导入");
			listHead.add("销售订单生成");
			listHead.add("已开票");
			listHead.add("已出库");
			listHead.add("已验收");

			list.add(listHead);
			
			List<Map<String, Object>> resultList = dao.dlvryDtlDownload(map) ;
			for(int i=0; i<resultList.size(); i++) {
				List<Object> listValue = new LinkedList<Object>();
				
				Map<String, Object> resultMap = resultList.get(i) ;
				
				listValue.add(CommonUtils.checkNull(resultMap.get("AREA_NAME"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("ROOT_ORG_NAME"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("BILL_SHORTNAME"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("BILL_CODE"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("BILL_DATE"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("ORDER_CODE"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("ORDER_SHORTNAME"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("ORDER_SHORTNAME"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("DELIVERY_NO"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("ERP_ORDER"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("ORDER_TYPE"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("MATERIAL_CODE"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("DELIVERY_DATE"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("CWDQR"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("ERPDDR"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("XSDDSC"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("YKP"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("YCK"))) ;
				listValue.add(CommonUtils.checkNull(resultMap.get("YYS"))) ;
				
				list.add(listValue) ;
			}
			os = response.getOutputStream();
			//CsvWriterUtil.writeCsv(list, os);
			CsvWriterUtil.createXlsFileByType(list, os);
			os.flush();		
		} catch(Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "发运单数量统计:下载");
			logger.error(logonUser,e1);
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
	
	public String formatDlrCode(String dlrCode) {
		if(!CommonUtils.isNullString(dlrCode)) {
			return new StringBuffer("").append("'").append(dlrCode.replace(",", "','")).append("'").toString() ;
		} else {
			return null ;
		}
	}
	
	  public String formatDate(String code){
		  if(code!=null&&code!=""){
			  return code.substring(0,7).replace("-","");
		  }
		  return "";
	  }
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
