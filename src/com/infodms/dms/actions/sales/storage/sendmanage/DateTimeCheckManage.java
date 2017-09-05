package com.infodms.dms.actions.sales.storage.sendmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storage.sendManage.DateTimeCheckDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.common.Constant;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
/**
 * 
 * @ClassName     : DateTimeCheckManage
 * @Description   : 物流运输考核管理
 * @author        : ranjian
 * CreateDate     : 2013-9-4
 */
public class DateTimeCheckManage {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final DateTimeCheckDao dtDao = DateTimeCheckDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final String dateTimeCheckInitUtl = "/jsp/sales/storage/sendmanage/dateTimeCheck/dateTimeCheckList.jsp";

	/**
	 * 
	 * @Title      : 
	 * @Description: 物流运输考核管理初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-4
	 */
	public void dateTimeCheckInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			act.setForword(dateTimeCheckInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"物流运输考核管理初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 物流运输考核管理(发运及时率)
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-4
	 */
	public void dateTimeCheckQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String common=CommonUtils.checkNull(request.getParamValue("common"));//处理类型
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商ID
			String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")); // 订单类型
			String orderNo = CommonUtils.checkNull(request.getParamValue("ORDER_NO")); // 销售订单号
			String invoiceNo = CommonUtils.checkNull(request.getParamValue("INVOICE_NO")); // 发票号
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流公司
			String assStartDate = CommonUtils.checkNull(request.getParamValue("ASS_STARTDATE")); // 分派日期开始
			String assEndDate = CommonUtils.checkNull(request.getParamValue("ASS_ENDDATE")); // 分派日期结束
			
			String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE")); //省份
			String cityId = CommonUtils.checkNull(request.getParamValue("CITY_ID")); // 城市
			String countyId = CommonUtils.checkNull(request.getParamValue("COUNTY_ID")); // 区县
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("yieldly", yieldly);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderNo", orderNo);
			map.put("invoiceNo", invoiceNo);
			map.put("logiName", logiName);
			map.put("assStartDate", assStartDate);
			map.put("assEndDate", assEndDate);
			map.put("countyId", countyId);
			map.put("CITY_ID", cityId);
			map.put("PROVINCE_ID", provinceId);
			map.put("poseId", logonUser.getPoseId().toString());
			if("1".equals(common)){//统计 调用
				Map<String, Object> valueMap = dtDao.tgSum(map);
				act.setOutData("valueMap", valueMap);	
			}else if("2".equals(common)){//导出 调用
				List<Map<String, Object>> mapList = dtDao.getDateTimeCheckExport(map);
				String [] head={"组板号","运单号","经销商名称","发运省份","发运城市","发运区县","承运车队","承运车牌","承运数量","保险单号","险种","运单时间","到达时间","应到达天数","验收时间","差额天数"};
				String [] cols={"BO_NO","BILL_NO","DEALER_NAME","PC_NAME","PC_NAME1","PC_NAME2","CAR_TEAM","CAR_NO","VEH_NUM","POLICY_NO","POLICY_TYPE","BILL_CRT_DATE","DD_DATE","ARRIVE_DAYS","ACC_DATE","D_DAYS"};
				ToExcel.toReportExcel(act.getResponse(),request, "物流商发运及时率.xls",head,cols,mapList);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dtDao.getDateTimeCheckQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);	
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物流商发运及时率查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 物流运输考核管理(配板及时率)
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-4
	 */
	public void dateTimeCheckQueryPC() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String common=CommonUtils.checkNull(request.getParamValue("common"));//处理类型
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商ID
			String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")); // 订单类型
			String orderNo = CommonUtils.checkNull(request.getParamValue("ORDER_NO")); // 销售订单号
			String invoiceNo = CommonUtils.checkNull(request.getParamValue("INVOICE_NO")); // 发票号
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流公司
			String assStartDate = CommonUtils.checkNull(request.getParamValue("ASS_STARTDATE")); // 分派日期开始
			String assEndDate = CommonUtils.checkNull(request.getParamValue("ASS_ENDDATE")); // 分派日期结束
			
			String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE")); //省份
			String cityId = CommonUtils.checkNull(request.getParamValue("CITY_ID")); // 城市
			String countyId = CommonUtils.checkNull(request.getParamValue("COUNTY_ID")); // 区县
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("yieldly", yieldly);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderNo", orderNo);
			map.put("invoiceNo", invoiceNo);
			map.put("logiName", logiName);
			map.put("assStartDate", assStartDate);
			map.put("assEndDate", assEndDate);
			map.put("countyId", countyId);
			map.put("CITY_ID", cityId);
			map.put("PROVINCE_ID", provinceId);
			map.put("poseId", logonUser.getPoseId().toString());
			if("1".equals(common)){//统计 调用
				Map<String, Object> valueMap = dtDao.tgSumPC(map);
				act.setOutData("valueMap", valueMap);	
			}else if("2".equals(common)){//导出 调用
				List<Map<String, Object>> mapList = dtDao.getDateTimeCheckPCExport(map);
				String [] head={"组板号","运单号","经销商名称","发运省份","发运城市","发运区县","承运车队","承运车牌","承运数量","保险单号","险种","分派时间","组板时间","差额天数"};
				String [] cols={"BO_NO","BILL_NO","DEALER_NAME","PC_NAME","PC_NAME1","PC_NAME2","CAR_TEAM","CAR_NO","VEH_NUM","POLICY_NO","POLICY_TYPE","ASS_DATE","BO_DATE","CS_TIME"};
				ToExcel.toReportExcel(act.getResponse(),request, "物流商发运及时率.xls",head,cols,mapList);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dtDao.getDateTimeCheckQueryPC(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);	
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物流商发运及时率查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 时间统计
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-4
	 */
	public void dateTimeCheckQueryDate() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String common=CommonUtils.checkNull(request.getParamValue("common"));//处理类型
			String vin = CommonUtils.checkNull(request.getParamValue("VIN")); // VIN
			String boardNo = CommonUtils.checkNull(request.getParamValue("BOARD_NO")); // 组板号
			String waybillNo = CommonUtils.checkNull(request.getParamValue("WAYBILL_NO")); //运单号
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("vin", vin);
			map.put("boardNo", boardNo);
			map.put("waybillNo", waybillNo);
			map.put("poseId", logonUser.getPoseId().toString());
			if("1".equals(common)){//统计 调用
				Map<String, Object> valueMap = dtDao.tgSumDate(map);
				act.setOutData("valueMap", valueMap);	
			}else if("2".equals(common)){//导出 调用
				List<Map<String, Object>> mapList = dtDao.getDateTimeCheckDateExport(map);
				String [] head={"组板号","运单号","分派时间","组板时间","运单时间","到达时间","验收时间","应到达天数","差额天数"};
				String [] cols={"BO_NO","BILL_NO","ASS_DATE","BO_DATE","BILL_CRT_DATE","DD_DATE","ACC_DATE","ARRIVE_DAYS","D_DAYS"};
				ToExcel.toReportExcel(act.getResponse(),request, "物流商综合时间统计.xls",head,cols,mapList);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dtDao.getDateTimeCheckQueryDate(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);	
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物流商综合时间统计");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
