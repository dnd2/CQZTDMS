package com.infodms.dms.actions.sales.storage.sendmanage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBillManageDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesBackcrmDetailPO;
import com.infodms.dms.po.TtSalesCityDisPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 运单回厂确认Action
 * @author xieyujia
 * 2013-5-16
 */
public class SendBillReturnCheck {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private SendBillManageDao sbmDao = SendBillManageDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final String sendBillReturnCheckInitUrl = "/jsp/sales/storage/sendmanage/sendBillManage/sendBillReturnCheckQuery.jsp";
	private final String toReturnCheckUrl = "/jsp/sales/storage/sendmanage/sendBillManage/sendBillReturnCheckDetail.jsp";
	
	public void sendBillReturnCheckInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
		    List<Map<String, Object>> areaList  =	MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
		    act.setOutData("areaList", areaList);
		    List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
		    act.setOutData("sqlDate", AjaxSelectDao.getInstance().getSimpleCurrentServerTime());//数据库当前日期
			act.setForword(sendBillReturnCheckInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运单回厂查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询待回厂确认的运单列表
	 */
	public void sendBillReturnListQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String billNo = CommonUtils.checkNull(request.getParamValue("billNo"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME"));
			
			String sendStartDate = CommonUtils.checkNull(request.getParamValue("sendStartDate"));
			String sendEndDate = CommonUtils.checkNull(request.getParamValue("sendEndDate"));
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("billNo", billNo.trim());
			map.put("dealerCode", dealerCode);
			map.put("areaId", areaId);
			map.put("logiName", logiName);
			map.put("poseId", logonUser.getPoseId().toString());
			
			map.put("sendStartDate", sendStartDate);
			map.put("sendEndDate", sendEndDate);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = sbmDao.getSendBillReturnList(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void toReturnCheck(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String billId = request.getParamValue("billId");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(billId != null && !"".equals(billId)){
			 Map<String, Object> result = sbmDao.getSendBillInfoById(Long.valueOf(billId));
				 if(result != null){
					 BigDecimal areaId = (BigDecimal)result.get("AREA_ID");
					 BigDecimal cityId = (BigDecimal)result.get("CITY_ID");
					 Date createDate = (Date)result.get("CREATE_DATE"); //发运单创建时间
					 TtSalesCityDisPO po = new TtSalesCityDisPO();
					 po.setYieldly(areaId.longValue());
					 po.setCityId(cityId.longValue());
					 List<PO> list = sbmDao.select(po);
					 if(list != null && list.size()>0){
						 //根据发运单创建时间和城市里程数中到达天数，得到该发运单预计到达时间
						 TtSalesCityDisPO po_1 = (TtSalesCityDisPO)list.get(0); //城市里程
						 Calendar c = Calendar.getInstance();
						 c.setTimeInMillis(createDate.getTime());
						 c.add(Calendar.DATE,po_1.getArriveDays());
						 Date d=  new Date(c.getTimeInMillis());
						 String expectArriveDate = sdf.format(d);
						 act.setOutData("expectArriveDate", expectArriveDate);
					 }
					 //查询该运单验收的最早时间
					 List<Map<String, Object>> res = sbmDao.getSendBillCheckDate(billId);
					 if(res != null && res.size()>0){
						 String checkDate = res.get(0).get("ACC_DATE").toString();
						 act.setOutData("checkDate", checkDate);
					 }else{
						 act.setOutData("checkDate", "");
					 }
					 act.setOutData("result", result);
					 
					 //查询出运单下的所有车辆信息
					 List<Map<String, Object>> billVehicleLis = sbmDao.queryWayBillByBillIds(billId);
					 act.setOutData("vehicles", billVehicleLis);
				 }
			}
			act.setForword(toReturnCheckUrl);

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "回厂确认查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void checkSubmit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			TtSalesWaybillPO po = new TtSalesWaybillPO();
			TtSalesWaybillPO po_ = new TtSalesWaybillPO();
			String remark = request.getParamValue("remark");
			String arrive_date = request.getParamValue("arrive_date");
			String billId = request.getParamValue("billId");
			String logiId = request.getParamValue("logiId");
			String vehicleIdStr = request.getParamValue("vehicleIds");
			String addressId = request.getParamValue("deliveryAddress");
			if(billId != null && !"".equals(billId)){
				po.setBillId(Long.valueOf(billId));			
				po_.setBillId(Long.valueOf(billId));
			}
			if(remark != null && !"".equals(remark)){
				po.setRemark(remark);
			}
			if(arrive_date != null && !"".equals(arrive_date)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				po.setArrDate(sdf.parse(arrive_date));
			}
			po.setBackCrmDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			po.setBackCrmPer(logonUser.getUserId());
			
			//插入回厂确认明细表
			if(vehicleIdStr != null) {
				String [] vehicleIds = vehicleIdStr.split(",");
				for (int i = 0; i < vehicleIds.length; i++) {
					TtSalesBackcrmDetailPO tbd = new TtSalesBackcrmDetailPO();
					tbd.setBackcrmId(Long.parseLong(SequenceManager.getSequence("")));
					tbd.setBillId(Long.parseLong(billId));
					tbd.setLogiId(Long.parseLong(logiId));
					tbd.setVehicleId(Long.parseLong(vehicleIds[i]));
					tbd.setBackCrmDate(new Date());
					tbd.setBackCrmPer(logonUser.getUserId());
					tbd.setArrDate(CommonUtils.parseDTime(arrive_date));
					tbd.setAddressId(Long.parseLong(addressId));
					tbd.setRemark(remark);
					tbd.setStatus(Constant.STATUS_ENABLE);
					tbd.setCreateBy(logonUser.getUserId());
					tbd.setCreateDate(new Date());
					sbmDao.insert(tbd);
					
				}
			}
			
			//验证是否已经回厂确认完成
			int backCrmCount = 0;
			List<Map<String, Object>> backCrmList = sbmDao.queryCountCrmByBillId(billId);
			if (backCrmList != null && backCrmList.size() > 0) {
				backCrmCount += Integer.parseInt(backCrmList.get(0).get("COUNT_BILL")+"");
			}
			int vehicleNum = 0;
			List<Map<String, Object>> vehicleMaps = sbmDao.queryWayBillById(billId);
			if (vehicleMaps != null && vehicleMaps.size() > 0) {
				vehicleNum += Integer.parseInt(vehicleMaps.get(0).get("VEH_NUM")+"");
			}
			if (backCrmCount == vehicleNum) {
				//验收完成
				po.setSendStatus(Long.valueOf(Constant.SEND_STATUS_02));
			}
		    sbmDao.update(po_, po);
			sendBillReturnCheckInit();
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.FAILURE_CODE, "运单回厂确认");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void batchReturnCheck(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] bill_ids = request.getParamValues("billIDs");
			String arrive_time = CommonUtils.checkNull(request.getParamValue("arrive_time"));
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String bill_id = "";
			if(bill_ids != null){
				for(int i = 0 ; i < bill_ids.length ; i++){
					if(i == bill_ids.length-1){
						bill_id += bill_ids[i] ;
					}else{
						bill_id += bill_ids[i] + ",";
					}
				
				}
			}
			//批量回厂确认
			List<Object> params = new ArrayList<Object>();
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("UPDATE TT_SALES_WAYBILL T SET \n");
			if(!"".equals(remark)){
				sbSql.append("                      T.REMARK = ?,");
				params.add(remark);
			}
			if(!"".equals(arrive_time)){
				sbSql.append("                              T.ARR_DATE = ?,\n");
				params.add(CommonUtils.parseDTime(arrive_time));
			}	
			sbSql.append("                              T.BACK_CRM_DATE = ?,\n");
			sbSql.append("                              T.BACK_CRM_PER = "+logonUser.getUserId()+",\n");
			sbSql.append("                              T.SEND_STATUS = "+Constant.SEND_STATUS_02+"\n");
			sbSql.append("                       WHERE  T.BILL_ID IN ("+bill_id+")\n"); 
			params.add(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			sbmDao.update(sbSql.toString(), params);
			
			//根据ID查询出运单集合
			List<Map<String, Object>> billList = sbmDao.queryWayBillByBillIds(bill_id);
			if (billList != null && billList.size() > 0) {
				for (int i = 0; i < billList.size(); i++) {
					TtSalesBackcrmDetailPO tbd = new TtSalesBackcrmDetailPO();
					tbd.setBackcrmId(Long.parseLong(SequenceManager.getSequence("")));
					tbd.setBillId(Long.parseLong(billList.get(i).get("BILL_ID")+""));
					tbd.setLogiId(Long.parseLong(billList.get(i).get("LOGI_ID")+""));
					tbd.setVehicleId(Long.parseLong(billList.get(i).get("VEHICLE_ID")+""));
					tbd.setBackCrmDate(new Date());
					tbd.setBackCrmPer(logonUser.getUserId());
					tbd.setArrDate(CommonUtils.parseDTime(arrive_time));
					tbd.setAddressId(Long.parseLong(billList.get(i).get("ADDRESS_ID")+""));
					tbd.setRemark(remark);
					tbd.setStatus(Constant.STATUS_ENABLE);
					tbd.setCreateBy(logonUser.getUserId());
					tbd.setCreateDate(new Date());
					sbmDao.insert(tbd);
				}
			}
			act.setOutData("value", 1);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.FAILURE_CODE, "运单回厂确认");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryVinInfo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String vin = request.getParamValue("vin");
			String billId = request.getParamValue("billId");
			List<Map<String, Object>> vinList = sbmDao.queryByVin(billId,vin);
			act.setOutData("vinList", vinList);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.FAILURE_CODE, "运单回厂确认");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
