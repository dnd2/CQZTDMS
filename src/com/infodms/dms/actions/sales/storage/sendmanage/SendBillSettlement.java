package com.infodms.dms.actions.sales.storage.sendmanage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
//import com.infodms.dms.actions.erpinterfaces.TransFeeService;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBillManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesAllocaDePO;
import com.infodms.dms.po.TtSalesBalancePO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 运单结算Action
 * @author xieyujia
 * 2013-5-16
 */
public class SendBillSettlement {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private SendBillManageDao sbmDao = SendBillManageDao.getInstance();
	private final String sendBillSettlementInitUrl = "/jsp/sales/storage/sendmanage/sendBillManage/sendBillSettlementQuery.jsp";
	private final String createSettlementAdvice = "/jsp/sales/storage/sendmanage/sendBillManage/sendBillSettlementDetail.jsp";
	
	/**
	 * 运单结算管理查询初始化
	 */
	public void sendBillSettlementInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
		    List<Map<String, Object>> areaList  =	MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
		    String area_ids = "";
		    if(areaList != null && areaList.size()>0){
		    	for(int i = 0 ; i < areaList.size(); i++){
		    		Map<String, Object> map = areaList.get(0);
		    		String areaId = map.get("AREA_ID").toString();
		    		area_ids += areaId + ",";
		    	}
		    }
		    List<Map<String, Object>> logiList = sbmDao.getLogiListByArea("");
		    act.setOutData("logiList", logiList);
		    act.setOutData("areaList", areaList);
			act.setForword(sendBillSettlementInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运单结算管理查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getLogiList(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
		    String areaId = request.getParamValue("areaId");
		    List<Map<String, Object>> logiList = sbmDao.getLogiListByArea(areaId);
		    String result = "";
		    if(logiList != null && logiList.size()>0){
		    	for(int i = 0 ; i < logiList.size(); i++){
		    		String logiId = logiList.get(i).get("LOGI_ID").toString();
		    		String logiName = logiList.get(i).get("LOGI_FULL_NAME").toString();
		    		String str = logiId + "," + logiName;
		    		if(i == logiList.size()-1){
		    			result += str;
		    		}else{
		    			result += str + "--";
		    		}
		    	}
		    }
		    act.setOutData("result", result);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"物流公司");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 已做回厂确认，能做结算单运单明细列表
	 */
	public void sendBillSettlementListQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String type =  CommonUtils.checkNull(request.getParamValue("type")); // 省份
			String dealerCode = request.getParamValue("dealerCode");
			String billNo = request.getParamValue("billNo");
			String areaId = request.getParamValue("areaId");
			String logiId = request.getParamValue("logiId");
			String countyId = CommonUtils.checkNull(request.getParamValue("COUNTY_ID")); // 区县
			String cityId = CommonUtils.checkNull(request.getParamValue("CITY_ID")); // 地市
			String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE")); // 省份
			String hasDistance = CommonUtils.checkNull(request.getParamValue("hasDistance")); // 是否维护成城市里程数
			String isSettlement =  CommonUtils.checkNull(request.getParamValue("isSettlement")); // 是否维护成城市里程数
			String sendStartDate = CommonUtils.checkNull(request.getParamValue("sendStartDate"));
			String sendEndDate = CommonUtils.checkNull(request.getParamValue("sendEndDate"));
			String lastOutDateStart = CommonUtils.checkNull(request.getParamValue("lastOutDateStart"));
			String lastOutDateEnd = CommonUtils.checkNull(request.getParamValue("lastOutDateEnd"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
			Map<String, Object> map = new HashMap<String, Object>();
			StringBuffer xlsTitle = new StringBuffer();
			if(billNo != null && !"".equals(billNo)){
				map.put("billNo", billNo.trim());
			}
			if(dealerCode != null && !"".equals(dealerCode)){
				map.put("dealerCode", dealerCode);
			}
			if(areaId != null && !"".equals(areaId)){
				map.put("areaId", areaId);
			}
			if(logiId != null && !"".equals(logiId)){
				map.put("logiId", logiId);
			}
			
			if(hasDistance != null && !"".equals(hasDistance)){
				if("yes".equals(hasDistance)){
					xlsTitle.append("已维护城市里程数");
				}else if("no".equals(hasDistance)){
					xlsTitle.append("未维护城市里程数");
				}
				map.put("hasDistance", hasDistance);
			}
			
			if(isSettlement != null && !"".equals(isSettlement)){
				if("yes".equals(isSettlement)){
					xlsTitle.append("已经结算");
				}else if("no".equals(isSettlement)){
					xlsTitle.append("未维护城市里程数");
				}
				map.put("isSettlement", isSettlement);
			}
			
			
			map.put("countyId", countyId);
			map.put("CITY_ID", cityId);
			map.put("PROVINCE_ID", provinceId);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("lastOutDateStart", lastOutDateStart);
			map.put("lastOutDateEnd", lastOutDateEnd);
			map.put("sendStartDate", sendStartDate);
			map.put("sendEndDate", sendEndDate);
			map.put("vin", vin);
			
			if("1".equals(type)){
				String title = xlsTitle.length() == 0 ? "待结算车辆明细" : xlsTitle.toString();
				List<Map<String, Object>> list = sbmDao.getSendBillSettlementList(map, 1, Constant.PAGE_SIZE_MAX).getRecords();
				String[] headExport={"运单号","经销商代码","经销商名称","物流公司","发运地址","VIN","回厂确认时间","确认人","车系","车型","配置","颜色"};
				String[] columns={"BILL_NO","DEALER_CODE","DEALER_NAME","LOGI_FULL_NAME","ADDRESS","VIN","BACK_CRM_DATE","BACK_CRM_PER","SERIES_NAME","MODEL_NAME","PACKAGE_NAME","COLOR_NAME"};
				ToExcel.toReportExcel(act.getResponse(), request,title +".xls", headExport,columns,list);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = sbmDao.getSendBillSettlementList(map, curPage, Constant.PAGE_SIZE_MIDDLE);
				act.setOutData("ps", ps);
			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运单结算管理查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 运单结算详细 TODO
	 */
	public void toCreateSettleAdvice(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> vehicle_detail_list = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> series_list = new ArrayList<Map<String,Object>>();
			String vehicleIds = request.getParamValue("vehicleIds");
			String[] bill_ids = request.getParamValues("billIDs");
			String bill_id = "";
			String t_bill_id = "";
			String billIDs = "";
			String logiId = "";
			String areaId = "";
			for(int i = 0; i < bill_ids.length ; i++){
				String result = bill_ids[i];
				String[] temp = result.split("-");
				String billId = temp[0];
				logiId = temp[1];
				areaId = temp[2];
				billIDs += billId + ",";
				List<Map<String, Object>> res = sbmDao.getSpecialFare(billId);
				if(res != null && res.size()>0){
					t_bill_id += billId + ","; //特殊省市运单
				}else{
					bill_id += billId + ",";   //一般省市运单
				}
			}
			//得到按车辆明细和车系统计运费列表
			if(!"".equals(bill_id) && !"".equals(t_bill_id)){
				vehicle_detail_list = sbmDao.getSendBIllSettleVehicleDetail(bill_id.substring(0, bill_id.length()-1),t_bill_id.substring(0, t_bill_id.length()-1)
																			,logonUser.getPoseId().toString(),vehicleIds);
				series_list = sbmDao.getSendBillSettlementDetail(bill_id.substring(0, bill_id.length()-1), t_bill_id.substring(0, t_bill_id.length()-1), logonUser.getPoseId().toString(),vehicleIds);
			}else if("".equals(t_bill_id)){
				vehicle_detail_list = sbmDao.getDetailListNormal(bill_id.substring(0, bill_id.length()-1),vehicleIds);
				series_list = sbmDao.getSeriesListNormal(bill_id.substring(0, bill_id.length()-1),vehicleIds);
			}else if("".equals(bill_id)){
				vehicle_detail_list = sbmDao.getDetailListSpecial(t_bill_id.substring(0, t_bill_id.length()-1),vehicleIds);
				series_list = sbmDao.getSeriesListSpecial(t_bill_id.substring(0, t_bill_id.length()-1),vehicleIds);
			}
			Double settle_amount = 0d; //按明细
			if(vehicle_detail_list != null && vehicle_detail_list.size()>0){
				for(int i = 0; i < vehicle_detail_list.size() ; i++){
					Map<String, Object> map = vehicle_detail_list.get(i);
					Double amount = ((BigDecimal)map.get("SETTLE_AMOUNT")).doubleValue();
					//settle_amount += amount;
					settle_amount=Arith.add(settle_amount, amount);
				}
				DecimalFormat df = new DecimalFormat( "#,###,###,###,###,###.00 ");
				String temp = df.format(settle_amount);
				act.setOutData("settle_amount", temp);
			}
			Double settle_amount_1 = 0d; //按车系
			if(series_list != null && series_list.size()>0){
				for(int i = 0; i < series_list.size() ; i++){
					Map<String, Object> map = series_list.get(i);
					Double amount = Double.valueOf(((String)map.get("COUNT")));
					//settle_amount += amount;
					settle_amount_1=Arith.add(settle_amount_1, amount);
				}
				DecimalFormat df = new DecimalFormat( "#,###,###,###,###,###.00 ");
				String temp = df.format(settle_amount);
				act.setOutData("settle_amount_1",temp );
			}
			act.setOutData("billIDs", billIDs.substring(0,billIDs.length()-1));
			act.setOutData("logiId", logiId);
			act.setOutData("areaId", areaId);
			act.setOutData("vehicleIds", vehicleIds);
			act.setOutData("vehicle_detail_list",vehicle_detail_list );
			act.setOutData("series_list",series_list );
			act.setForword(createSettlementAdvice);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "生成结算单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 结算确认
	 */
	public void settleSubmit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> vehicle_detail_list = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> series_list = new ArrayList<Map<String,Object>>();
			String logiId = request.getParamValue("logiId");//物流商ID
			String areaId = request.getParamValue("areaId");//产地ID
			String vehicleIds = request.getParamValue("vehicleIds");
			String billIDs = request.getParamValue("billIDs");
			String [] kkAmounts = request.getParamValues("kk_amount");
			String [] otherAmounts = request.getParamValues("other_amount");
			String [] balRemarks = request.getParamValues("remark");
			String [] settleAmounts = request.getParamValues("settle_amount");
			String [] balVehicleIds = request.getParamValues("vehicleId");
			if (kkAmounts == null || kkAmounts.length <= 0) {
				throw new BizException("数据获取异常,请重试!");
			}
			String[] bill_attr = billIDs.split(",");
			String t_bill_id ="";
			String bill_id = "";
			for(int i = 0 ; i < bill_attr.length ; i++){
				String billId = bill_attr[i];
				List<Map<String, Object>> res = sbmDao.getSpecialFare(billId);
				if(res != null && res.size()>0){
					t_bill_id += billId + ","; //特殊省市运单
				}else{
					bill_id += billId + ",";  //正常运单
				}
				
			}
			List<Map<String, Object>> balanceList = sbmDao.queryBillType(vehicleIds);
			if (balanceList != null && balanceList.size() > 0) {
				throw new BizException("该车辆已经结算,请避免并发操作!");
			}
			//得到按车辆明细和车系统计运费列表
			if(!"".equals(bill_id) && !"".equals(t_bill_id)){
				vehicle_detail_list = sbmDao.getSendBIllSettleVehicleDetail(bill_id.substring(0, bill_id.length()-1),t_bill_id.substring(0, t_bill_id.length()-1)
																			,logonUser.getPoseId().toString(),vehicleIds);
				series_list = sbmDao.getSendBillSettlementDetail(bill_id.substring(0, bill_id.length()-1), t_bill_id.substring(0, t_bill_id.length()-1), logonUser.getPoseId().toString(),vehicleIds);
			}else if("".equals(t_bill_id)){
				vehicle_detail_list = sbmDao.getDetailListNormal(bill_id.substring(0, bill_id.length()-1),vehicleIds);
				series_list = sbmDao.getSeriesListNormal(bill_id.substring(0, bill_id.length()-1),vehicleIds);
			}else if("".equals(bill_id)){
				vehicle_detail_list = sbmDao.getDetailListSpecial(t_bill_id.substring(0, t_bill_id.length()-1),vehicleIds);
				series_list = sbmDao.getSeriesListSpecial(t_bill_id.substring(0, t_bill_id.length()-1),vehicleIds);
			}
			Double settle_amount = 0d; //按车系
			if(series_list != null && series_list.size()>0){
				for(int i = 0; i < series_list.size() ; i++){
					Map<String, Object> map = series_list.get(i);
					Double amount = Double.valueOf(((String)map.get("COUNT")));
					//settle_amount += amount;
					settle_amount = Arith.add(settle_amount, amount);
				}
			}
			Double kkAmount = 0.0;
			Double otherAmount = 0.0;
			Double settleAmount = 0.0;
			for (int i = 0; i < kkAmounts.length; i++) {
				kkAmount += Double.parseDouble(kkAmounts[i]);
				otherAmount += Double.parseDouble(otherAmounts[i]);
				settleAmount += Double.parseDouble(settleAmounts[i]);
			}
			 // 运单结算单生成
			  TtSalesBalancePO po = new TtSalesBalancePO();
			  Long salesBalId = Long.parseLong(SequenceManager.getSequence(""));
			  po.setBalId(salesBalId);
			  po.setAreaId(Long.valueOf(areaId));
			  po.setBalCount(vehicle_detail_list.size());
			  po.setBalAmount(settleAmount);
			  po.setLogiId(Long.valueOf(logiId));
			  po.setBalDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			  po.setBalPer(logonUser.getUserId());
			  po.setBalNo("LE"+salesBalId);
			  po.setCreateBy(logonUser.getUserId());
			  po.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			  po.setOtherMoney(kkAmount);
			  po.setOtherMoney(otherAmount);
			  sbmDao.insert(po);
			//更新结算ID到回厂确认表
			  sbmDao.updateBalIdToBackCrm(vehicleIds, salesBalId+"");
			  int backCrmCount = 0;
				List<Map<String, Object>> backBalList = sbmDao.queryCountBalByBillId(billIDs);
				if (backBalList != null && backBalList.size() > 0) {
					backCrmCount += Integer.parseInt(backBalList.get(0).get("COUNT_BILL")+"");
				}
				int vehicleNum = 0;
				List<Map<String, Object>> vehicleMaps = sbmDao.queryWayBillById(billIDs);
				if (vehicleMaps != null && vehicleMaps.size() > 0) {
					vehicleNum += Integer.parseInt(vehicleMaps.get(0).get("VEH_NUM")+"");
				}
				if (backCrmCount == vehicleNum) {
					//结算
					//更改运单状态为已结算，及回填对应的结算单ID
				    String sql  = "UPDATE TT_SALES_WAYBILL SET SEND_STATUS = "+Constant.SEND_STATUS_03+",BAL_ID ="+salesBalId+" WHERE BILL_ID IN ("+billIDs+") AND AREA_ID="+areaId;
				    sbmDao.update(sql, null);
				}
			 
		    //将每辆车结算时计算出的：运送里程、里程单价、运费回填入对应的配车明细表
			  if(vehicle_detail_list != null && vehicle_detail_list.size()>0){
				  for(int i = 0 ; i < vehicle_detail_list.size() ; i++ ){
					  Map<String, Object> map = vehicle_detail_list.get(i);
					  String vehicle_id = map.get("VEHICLE_ID").toString();
					  Float amount = ((BigDecimal)map.get("SETTLE_AMOUNT")).floatValue();
					  Long distance = ((BigDecimal)map.get("DISTANCE")).longValue();
					  Float singlePrice = ((BigDecimal)map.get("AMOUNT")).floatValue();
					  Double fuelCoefficient = Double.parseDouble(map.get("FUEL_COEFFICIENT")+"");
					  Double deductMoney = 0.0;
					  Double otherMoney = 0.0;
					  String balRemark = "";
					  Double sAmount = 0.0;
					  for (int j = 0; j < kkAmounts.length; j++) {
						  if (vehicle_id.equals(balVehicleIds[i])) {
							  deductMoney = Double.parseDouble(kkAmounts[i]);
							  otherMoney = Double.parseDouble(otherAmounts[i]);
							  balRemark = balRemarks[i];
							  sAmount = Double.parseDouble(settleAmounts[i]) + fuelCoefficient;
							break;
						}
					  }
					  TtSalesAllocaDePO alloca_po = new TtSalesAllocaDePO();
					  alloca_po.setVehicleId(Long.valueOf(vehicle_id));
					  alloca_po.setSendDistance(distance);
					  alloca_po.setSinglePrice(singlePrice);
					  alloca_po.setFuelCoefficient(fuelCoefficient);
					  alloca_po.setBalAmount(Double.parseDouble(sAmount+""));
					  alloca_po.setDeductMoney(deductMoney);
					  alloca_po.setOtherMoney(otherMoney);
					  alloca_po.setBalRemark(balRemark);
					  
					  TtSalesAllocaDePO alloca_po_ = new TtSalesAllocaDePO();
					  alloca_po_.setVehicleId(Long.valueOf(vehicle_id));
					  sbmDao.update(alloca_po_, alloca_po);
//					  boolean result = TransFeeService.getTransFeeService().send2Erp(vehicle_id);
//					  if(!result){
//	                		throw new BizException(act,new RuntimeException(),ErrorCodeConstant.SPECIAL_MEG,"ERP接收失败！");
//	                 }
					//TODO
				  }
			  }
			  
			  act.setOutData("returnBalNo", po.getBalNo());//结算单号
			  act.setOutData("returnValue", 1);//成功
			//sendBillSettlementInit();
			
		}catch (BizException e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ERP_ERROR, "运单结算");
			logger.error(logonUser, e1);
			act.setException(e1);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算");
			 act.setOutData("returnValue", 2);//失败
			 act.setOutData("message", e.getMessage());
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 运单结算前对选择的运单进行CHECK
	 */
	public void checkSendBill(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<String> bill_id_list = new ArrayList<String>();
			List<String> bill_id_list_1 = new ArrayList<String>();
			String billIds = request.getParamValue("billIds");
			String[] attr = billIds.split(",");
			List<String> list = new ArrayList<String>();
			for(int i = 0 ; i < attr.length ; i++){
				
				String bill_id = attr[i];
				String sql = "SELECT TO_CHAR(AREA_ID) AREA_ID FROM TT_SALES_WAYBILL WHERE BILL_ID = "+bill_id;
				List<Map<String, Object>> bill = sbmDao.pageQuery(sql, null, sbmDao.getFunName());
				String areaId = bill.get(0).get("AREA_ID").toString();
				List<Map<String, Object>> result = sbmDao.checkDistance(bill_id);//如果该运单里程数是否已经维护
				//已经维护
				if(result != null && result.size()>0){
					String distance = (String)result.get(0).get("DISTANCE");
					if(distance != null && !"".equals(distance)){
						/*sList<Map<String, Object>>  res = sbmDao.getSpecialFare(bill_id);//判断该运单运费是否在特殊省市中维护
						//不是特殊省市运费
						if(!(res != null && res.size() > 0)){
							//判断该运单里程在里程管理中是否已经维护
							String sql_1 = "SELECT TO_CHAR(MIL_ID) MIL_ID FROM TT_SALES_MILSET WHERE YIELDLY = "+areaId 
											+" AND MIL_START <= "+distance+" AND MIL_END >= "+distance;
							List<Map<String, Object>> list_1 = sbmDao.pageQuery(sql_1, null, sbmDao.getFunName());
							//如果一般里程数没有维护
							if(!(list_1 != null && list_1.size() > 0)){
								bill_id_list.add(bill_id);
								continue;
							//如果一般里程数有维护，则检查其正常运费有没有设定
							}else{
								String milId =list_1.get(0).get("MIL_ID").toString();
								List<Map<String, Object>> resu = sbmDao.getSeriesByBillId(bill_id);
								if(resu != null && resu.size()>0){
									for(int j = 0 ; j < resu.size() ; j++){
										Map<String, Object> map = resu.get(j);
										String groupId = (String)map.get("GROUP_ID");
										String sql_2 = "SELECT FARE_ID FROM TT_SALES_FARE WHERE MIL_ID = "+milId+" AND GROUP_ID = "+groupId
														+" AND YIELDLY = "+areaId;
										List<Map<String, Object>> list_2 = sbmDao.pageQuery(sql_2, null, sbmDao.getFunName());
										if(!(list_2 != null && list_2.size()>0)){
											bill_id_list_1.add(bill_id);
											continue;
										}
									}
								}
							}			
						}*/
					}else{
						list.add(bill_id);
					}
					
				//没有维护
				}else{
					list.add(bill_id);
				}
			}
			String str_1 = "";
			String str_2 = "";
			String str_3 = "";	
			if(bill_id_list != null && bill_id_list.size()>0){
				String str = "";
				for(String s:bill_id_list){			
					String sql = "SELECT BILL_NO FROM TT_SALES_WAYBILL WHERE BILL_ID = "+s;
					List<Map<String, Object>> result = sbmDao.pageQuery(sql, null , sbmDao.getFunName());
					str += result.get(0).get("BILL_NO").toString() + ",";
				}
				str_1 = "运单号：" + str.substring(0, str.length()-1)+" 运费里程没有设定! ";
				
			}
			if(bill_id_list_1 != null && bill_id_list_1.size()>0){
				String str = "";
				for(String s:bill_id_list_1){			
					String sql = "SELECT BILL_NO FROM TT_SALES_WAYBILL WHERE BILL_ID = "+s;
					List<Map<String, Object>> result = sbmDao.pageQuery(sql, null , sbmDao.getFunName());
					str += result.get(0).get("BILL_NO").toString() + ",";
				}
				str_3 = "运单号：" + str.substring(0, str.length()-1)+"运费没有设定! ";
				
			}
			if(list != null && list.size()>0){
				String str = "";
				for(String s:list){			
					String sql = "SELECT BILL_NO FROM TT_SALES_WAYBILL WHERE BILL_ID = "+s;
					List<Map<String, Object>> result = sbmDao.pageQuery(sql, null , sbmDao.getFunName());
					str += result.get(0).get("BILL_NO").toString() + ",";
				}
				str_2 = "运单号：" + str.substring(0, str.length()-1)+" 没有维护城市里程数!";
				
			}
			
			if("".equals(str_1) && "".equals(str_2) && "".equals(str_3) ){
				act.setOutData("str", "");
			}else{
				act.setOutData("str", str_1+str_2+str_3);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运单检查");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	//判断出发地和目的地是否存在
	public  void  checkBillPlace() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			List<String> msgList = new ArrayList<String>();
			String billIds = request.getParamValue("billIds");
			String[] attr = billIds.split(",");
			for(int i = 0 ; i < attr.length ; i++){
				String desmsg = "";
				String fromMsg = "";
				TtSalesWaybillPO  po = new TtSalesWaybillPO();
				po.setBillId(Long.parseLong(attr[i]));
				List<PO> list = sbmDao.select(po);
				if(list.size() > 0 ){
					po = (TtSalesWaybillPO) list.get(0);
					if(null == po.getAddressId() || "".equals(po.getAddressId())){
						desmsg  = "运单号：" + po.getBillNo() + "目的地未进行维护!";
						msgList.add(desmsg);
					}
					
					if(null == po.getAreaId() || "".equals(po.getAreaId())){
						fromMsg  = "运单号：" + po.getBillNo() + "出发地未进行维护!";
						msgList.add(fromMsg);
					}
					
					
				
				}
				
			}
			
			if(msgList.size() > 0){
				StringBuffer msgs =  new StringBuffer();
				for(String msg : msgList){
					msgs.append(msg).append(";");
				}
				act.setOutData("str", msgs.toString());
			}else{
				act.setOutData("str","ok");
				act.setOutData("billIds", billIds);
			}
			
		}catch(Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运单检查");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**物流公司至少要维护一次在途位置和经销商已收车，否则不允许结算；*/
	public void checkOnWayAndVeicleIn(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String billIDs = request.getParamValue("billIDs");
			String[] bill_attr = billIDs.split(",");
			List<String> msgList = new ArrayList<String>();
			msgList.addAll(checkOnWay(bill_attr));
			msgList.addAll(checkViecleLife(bill_attr));
			//在途位置检测
			StringBuffer msg = new StringBuffer();
			for(String mg : msgList){
				msg.append(mg).append(";");
			}
			act.setOutData("failrecord", msgList.size());
			act.setOutData("msg",msg.toString());
		}catch(Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "在途位置和经销商已收车检查");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	//在途位置维护检测
	private List<String> checkOnWay(String[] billIds){
	    List<String> msgList = new ArrayList<String>();
		//根据运单信息查询出是否在有在途维护的记录
	    for(String billId :billIds){
	    	List<Map<String,Object>> result = sbmDao.getVechileInof(billId);
			if(result.isEmpty()){
				msgList.add("运单号"+ billId + "没有在途 位置维护记录，不能结算");
			}
	    }
		return msgList;
	}
	
	//经销商已收车检测
	private  List<String>  checkViecleLife(String[] billIds){
		  List<String> msgList = new ArrayList<String>();
			//根据运单信息查询出是否在有在途维护的记录
		    for(String billId :billIds){
		    	 List<Map<String, Object>> result = sbmDao.queryVehicleStatus(billId);
				if(result.isEmpty()){
					msgList.add("运单号"+ billId + "经销商还未完全入库，不能结算");
				}else{
					//记录数为验收入库数量
					int record = result.size();
					BigDecimal vhnum = (BigDecimal)result.get(0).get("VEH_NUM");
					Integer vehNum = vhnum.intValue();
					//当验收入库的记录数与运单上车辆数不一致是，则运单上的车辆还没有入库
					if(record != vehNum ){
						msgList.add("运单号"+ billId + "经销商还未完全入库，不能结算");
					}
				}
		    }
			return msgList;
		
	}
}
