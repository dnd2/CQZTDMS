package com.infodms.dms.actions.report.service;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.report.serviceReport.ActivityBalanceDetailDao;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.dao.report.serviceReport.TechnologyUpgradeDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class TechnologyUpgrade {
	private Logger logger = Logger.getLogger(TechnologyUpgrade.class);
	private final TechnologyUpgradeDao cdao = TechnologyUpgradeDao.getInstance();
	private final ClaimReportDao dao  = ClaimReportDao.getInstance();
	private static final AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();
	private static final TcCodeDao tcDao = TcCodeDao.getInstance();


	private final String TECHNOLOGY_UPGRADES_DEL = "/jsp/report/service/Technology_Upgrades_del.jsp";

	ActionContext act = ActionContext.getContext();
 	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
 	RequestWrapper request = act.getRequest();
	 
 /**
 * 初始化页面
 */
public void TechnologyUpgradeinit(){

    	try {
			List<Map<String, Object>> list = dao.getBigOrgList();
			List<Map<String, Object>> serisList = ajaxDao.getSerisList(logonUser.getPoseId().toString(), 2);
			act.setOutData("serislist", serisList);
			act.setOutData("list", list);
    		act.setForword(TECHNOLOGY_UPGRADES_DEL);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "客户关怀活动数据统计表");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
    	
    }
/**
* 初始化活动编号页面
*/
public void queryActivity(){

   	try {

		String activityCode = CommonUtils.checkNull(request.getParamValue("ACTIVITY_CODE"));//车型ID
		String activityName = CommonUtils.checkNull(request.getParamValue("ACTIVITY_NAME"));//车型code
		Map<String, String> map = new HashMap<String, String>();
		map.put("activityCode", activityCode);
		map.put("activityName", activityName);
		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
				.getParamValue("curPage")) : 1;
		PageResult<Map<String, Object>> ps=cdao.QueryActivity(map, Constant.PAGE_SIZE, curPage);
		act.setOutData("ps", ps);

   		
   	} catch (Exception e) {
   		BizException e1 = new BizException(act, e,
   				ErrorCodeConstant.QUERY_FAILURE_CODE, "客户关怀活动数据统计表");
   		logger.error(logonUser, e1);
   		act.setException(e1);
   	}
   	
   }

/**
 * 查询页面
 */
public void TechnologyUpgradeQueryData(){

	try {
		Map<String, String> map = new HashMap<String, String>();
		String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
		String vin = CommonUtils.checkNull(request.getParamValue("vin"));//Vin
		String serisid = CommonUtils.checkNull(request.getParamValue("serisid"));//车型ID
		String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));//车型code
		String bigorgId = CommonUtils.checkNull(request.getParamValue("big_org"));//大区
		String smallorg = CommonUtils.checkNull(request.getParamValue("small_org"));//小区
		String dealerCode = CommonUtils.checkNull(request.getParamValue("dealer_code"));//服务商代码
		String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//经销商全称
		String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//工单维修开始日期
		String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//工单维修结束日期
		String bgDate = CommonUtils.checkNull(request.getParamValue("bgDate"));//工单结算开始日期
		String egDate = CommonUtils.checkNull(request.getParamValue("egDate"));//工单结算结束日期
		String crbDate = CommonUtils.checkNull(request.getParamValue("crbDate"));//生产开始日期
		String creDate = CommonUtils.checkNull(request.getParamValue("creDate"));//生产结束日期
		String engineNo = CommonUtils.checkNull(request.getParamValue("engine_no"));//发动机
		String activityId = CommonUtils.checkNull(request.getParamValue("activityId"));//活动编号
		String activityName = CommonUtils.checkNull(request.getParamValue("activityName"));//活动名称

		map.put("yieldlyType", yieldlyType);
		map.put("vin", vin);
		map.put("serisid", serisid);
		map.put("groupCode", groupCode);
		map.put("bigorgId", bigorgId);
		map.put("smallorg", smallorg);
		map.put("dealerCode", dealerCode);
		map.put("dealerName", dealerName);
		map.put("bDate", bDate);
		map.put("eDate", eDate);
		map.put("bgDate", bgDate);
		map.put("egDate", egDate);
		map.put("crbDate", crbDate);
		map.put("creDate", creDate);
		map.put("engineNo", engineNo);
		map.put("activityId", activityId);
		map.put("activityName", activityName);
		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
				.getParamValue("curPage")) : 1;
		PageResult<Map<String, Object>> ps=cdao.QueryTechnologyUpgrade(map, Constant.PAGE_SIZE, curPage);
		act.setOutData("ps", ps);
	} catch (Exception e) {
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.QUERY_FAILURE_CODE, "技术升级明细查询");
		logger.error(logonUser, e1);
		act.setException(e1);
	}

	}


/**
 * 导出页面
 */
public void TechnologyUpgradeQueryDataExport(){
	OutputStream os = null;

	try {
		Map<String, String> map = new HashMap<String, String>();
		String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
		String vin = CommonUtils.checkNull(request.getParamValue("vin"));//Vin
		String serisid = CommonUtils.checkNull(request.getParamValue("serisid"));//车型ID
		String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));//车型code
		String bigorgId = CommonUtils.checkNull(request.getParamValue("big_org"));//大区
		String smallorg = CommonUtils.checkNull(request.getParamValue("small_org"));//小区
		String dealerCode = CommonUtils.checkNull(request.getParamValue("dealer_code"));//服务商代码
		String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//经销商全称
		String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//工单维修开始日期
		String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//工单维修结束日期
		String bgDate = CommonUtils.checkNull(request.getParamValue("bgDate"));//工单结算开始日期
		String egDate = CommonUtils.checkNull(request.getParamValue("egDate"));//工单结算结束日期
		String crbDate = CommonUtils.checkNull(request.getParamValue("crbDate"));//生产开始日期
		String creDate = CommonUtils.checkNull(request.getParamValue("creDate"));//生产结束日期
		String engineNo = CommonUtils.checkNull(request.getParamValue("engine_no"));//发动机
		String activityNo = CommonUtils.checkNull(request.getParamValue("activityNo"));//活动编号
		String activityName = CommonUtils.checkNull(request.getParamValue("activityName"));//活动名称

		map.put("yieldlyType", yieldlyType);
		map.put("vin", vin);
		map.put("serisid", serisid);
		map.put("groupCode", groupCode);
		map.put("bigorgId", bigorgId);
		map.put("smallorg", smallorg);
		map.put("dealerCode", dealerCode);
		map.put("dealerName", dealerName);
		map.put("bDate", bDate);
		map.put("eDate", eDate);
		map.put("bgDate", bgDate);
		map.put("egDate", egDate);
		map.put("crbDate", crbDate);
		map.put("creDate", creDate);
		map.put("engineNo", engineNo);
		map.put("activityNo", activityNo);
		map.put("activityName", activityName);
		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
				.getParamValue("curPage")) : 1;
		PageResult<Map<String, Object>> ps=cdao.QueryTechnologyUpgrade(map, 99999, curPage);
		ResponseWrapper response = act.getResponse();
		// 导出的文件名
		String fileName = "技术升级明细查询.xls";
		// 导出的文字编码客户关怀活动数据明细表
		fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
		response.setContentType("Application/text/csv");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		// 定义一个集合
		List<List<Object>> list = new LinkedList<List<Object>>();
		// 标题
		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("结算基地");
		listTemp.add("工单号");
		listTemp.add("结算单号");
		listTemp.add("服务站代码");
		listTemp.add("服务站全称");
		listTemp.add("一级站代码");
		listTemp.add("一级站全称");
		listTemp.add("大区");
		listTemp.add("小区");
		listTemp.add("车系(名称)");
		listTemp.add("车型");
		listTemp.add("牌照号");
		listTemp.add("发动机号");
		listTemp.add("VIN");
		listTemp.add("生产日期");
		listTemp.add("购车日期");
		listTemp.add("车主");
		listTemp.add("车主电话");
		listTemp.add("车主地址");
		listTemp.add("工单维修日期");
		listTemp.add("工单结算日期");
		
		listTemp.add("行驶里程");
		listTemp.add("配件维修类型");
		listTemp.add("故障代码");
		listTemp.add("故障描述");
		listTemp.add("故障件代码");
		listTemp.add("故障件件号");
		listTemp.add("故障件名称");
		listTemp.add("是否主因件");
		listTemp.add("更换件代码");
		listTemp.add("更换件名称");
		listTemp.add("配件单价");
		listTemp.add("配件数量");
		listTemp.add("材料费");
		listTemp.add("主因件工时单价");
		listTemp.add("主因件工时费");
		listTemp.add("换下件制造商代码");
		listTemp.add("换下件制造商名称");
		listTemp.add("活动编号");
		listTemp.add("活动名称");
		listTemp.add("优惠材料系数");
		listTemp.add("优惠工时系数");
		
		listTemp.add("赠送费用");
		listTemp.add("活动开始日期");
		listTemp.add("活动结束日期");
		list.add(listTemp);
		List<Map<String, Object>> rslist = ps.getRecords();
		Map<String, Object> map1 = new HashMap<String, Object>();

		if(rslist!=null){

		for (int i = 0; i < rslist.size(); i++) {
			map1 = rslist.get(i);
			List<Object> listValue = new LinkedList<Object>();
			listValue.add(map1.get("BALANCE_YIELDLY") != null ?tcDao.getCodeDescByCodeId(map1.get("BALANCE_YIELDLY").toString())  : "");
			listValue.add(map1.get("RO_NO") != null ? map1.get("RO_NO") : "");
			listValue.add(map1.get("CLAIM_NO") != null ? map1.get("CLAIM_NO") : "");
			listValue.add(map1.get("DEALER_CODE") != null ? map1.get("DEALER_CODE") : "");
			listValue.add(map1.get("DEALER_NAME") != null ? map1.get("DEALER_NAME") : "");
			listValue.add(map1.get("PDEALER_CODE") != null ? map1.get("PDEALER_CODE") : "");
			listValue.add(map1.get("PDEALER_NAME") != null ? map1.get("PDEALER_NAME") : "");
			listValue.add(map1.get("ROOT_ORG_NAME") != null ? map1.get("ROOT_ORG_NAME") : "");
			listValue.add(map1.get("ORG_NAME") != null ? map1.get("ORG_NAME") : "");
			listValue.add(map1.get("SERIES_NAME") != null ? map1.get("SERIES_NAME") : "");
			listValue.add(map1.get("MODEL_CODE") != null ? map1.get("MODEL_CODE") : "");
			listValue.add(map1.get("LICENSE_NO") != null ? map1.get("LICENSE_NO") : "");
			listValue.add(map1.get("ENGINE_NO") != null ? map1.get("ENGINE_NO") : "");
			listValue.add(map1.get("VIN") != null ? map1.get("VIN") : "");
			listValue.add(map1.get("PRODUCT_DATE") != null ? map1.get("PRODUCT_DATE") : "");
			listValue.add(map1.get("PURCHASED_DATE") != null ? map1.get("PURCHASED_DATE") : "");
			listValue.add(map1.get("CTM_NAME") != null ? map1.get("CTM_NAME") : "");
			listValue.add(map1.get("MAIN_PHONE") != null ? map1.get("MAIN_PHONE") : "");
			listValue.add(map1.get("ADDRESS") != null ? map1.get("ADDRESS") : "");
			listValue.add(map1.get("RO_CREATE_DATE") != null ? map1.get("RO_CREATE_DATE") : "");
			listValue.add(map1.get("FOR_BALANCE_TIME") != null ? map1.get("FOR_BALANCE_TIME") : "");
			
			listValue.add(map1.get("IN_MILEAGE") != null ? map1.get("IN_MILEAGE") : "");
			listValue.add(map1.get("REPAIR_TYPE") != null ? map1.get("REPAIR_TYPE") : "");
			listValue.add(map1.get("MAL_NAME") != null ? map1.get("MAL_NAME") : "");
			listValue.add(map1.get("REMARK") != null ? map1.get("REMARK") : "");
			listValue.add(map1.get("DOWN_PART_CODE") != null ? map1.get("DOWN_PART_CODE") : "");
			listValue.add(map1.get("DOWN_PART_NO") != null ? map1.get("DOWN_PART_NO") : "");
			listValue.add(map1.get("DOWN_PART_NAME") != null ? map1.get("DOWN_PART_NAME") : "");
			listValue.add(map1.get("RESPONSIBILITY_TYPE") != null ?tcDao.getCodeDescByCodeId(map1.get("RESPONSIBILITY_TYPE").toString()):"");
			listValue.add(map1.get("PART_CODE") != null ? map1.get("PART_CODE") : "");
			listValue.add(map1.get("PART_NAME") != null ? map1.get("PART_NAME") : "");
			listValue.add(map1.get("APPLY_PRICE") != null ? map1.get("APPLY_PRICE") : "");
			listValue.add(map1.get("QUANTITY") != null ? map1.get("QUANTITY") : "");
			listValue.add(map1.get("APPLY_AMOUNT") != null ? map1.get("APPLY_AMOUNT") : "");
			listValue.add(map1.get("APPLY_PRICE1") != null ? map1.get("APPLY_PRICE1") : "");
			listValue.add(map1.get("APPLY_AMOUNT1") != null ? map1.get("APPLY_AMOUNT1") : "");
			listValue.add(map1.get("DOWN_PRODUCT_CODE") != null ? map1.get("DOWN_PRODUCT_CODE") : "");
			listValue.add(map1.get("DOWN_PRODUCT_NAME") != null ? map1.get("DOWN_PRODUCT_NAME") : "");
			listValue.add(map1.get("ACTIVITY_CODE") != null ? map1.get("ACTIVITY_CODE") : "");
			listValue.add(map1.get("ACTIVITY_NAME") != null ? map1.get("ACTIVITY_NAME") : "");
			listValue.add(map1.get("FREE_PART") != null ? map1.get("FREE_PART") : "");
			listValue.add(map1.get("FREE_LABOUR") != null ? map1.get("FREE_LABOUR") : "");
			
			listValue.add(map1.get("APPLY_AMOUNT2") != null ? map1.get("APPLY_AMOUNT2") : "");
			listValue.add(map1.get("FACTSTARTDATE") != null ? map1.get("FACTSTARTDATE") : "");
			listValue.add(map1.get("FACTENDDATE") != null ? map1.get("FACTENDDATE") : "");
			list.add(listValue);
		}
		}
		os = response.getOutputStream();
		CsvWriterUtil.createXlsFileToName(list, os,"技术升级明细查询");
		os.flush();	

	} catch (Exception e) {
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.QUERY_FAILURE_CODE, "技术升级明细导出");
		logger.error(logonUser, e1);
		act.setException(e1);
	}

  }

  public static String BaseTcCode(String val){
	  TechnologyUpgradeDao cdao = TechnologyUpgradeDao.getInstance();
	  TcCodePO po =new TcCodePO();
	  po.setCodeId(val);
	  List<TcCodePO> list=cdao.select(po);
    return list.get(0).getCodeDesc(); 
  }

}
