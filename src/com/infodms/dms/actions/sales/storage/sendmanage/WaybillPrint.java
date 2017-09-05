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

//import com.infodms.dms.actions.erpinterfaces.DeliveryNoteService;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
//import com.infodms.dms.dao.common.ErpInterfaceConstants;
import com.infodms.dms.dao.sales.storage.StorageUtil;
import com.infodms.dms.dao.sales.storage.sendManage.RemovalStorageDao;
import com.infodms.dms.dao.sales.storage.sendManage.WaybillManageDao;
import com.infodms.dms.dao.sales.storage.sendManage.WaybillPrintDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesBoDetailPO;
import com.infodms.dms.po.TtSalesCityDisPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.po.TtVsOrderResourceReservePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.POContext;

public class WaybillPrint {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final WaybillPrintDao wpDao = WaybillPrintDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final RemovalStorageDao rsDao = RemovalStorageDao.getInstance();
	private final WaybillManageDao wmDao = WaybillManageDao.getInstance();
	private final String WaybillPrintInitUtl = "/jsp/sales/storage/sendmanage/WaybillPrint/WaybillPrintInit.jsp";
	private final String WaybillPrintUtl = "/jsp/sales/storage/sendmanage/WaybillPrint/printWaybill.jsp";//运单打印
	private final String WaybillPrintUtl2 = "/jsp/sales/storage/sendmanage/WaybillPrint/printWaybill2.jsp";//出门证打印
  
	/**
	 * 
	 * @Title      : 
	 * @Description: 运单打印初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-20
	 */
	public void WaybillPrintInit(){
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

			act.setForword(WaybillPrintInitUtl);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运单打印初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运单打印查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-20
	 */
	public void WaybillPrintQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);


		try {
			/******************************页面查询字段start**************************/
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商ID
			String orderNo = CommonUtils.checkNull(request.getParamValue("ORDER_NO")); //运单号
			String sendStartDate = CommonUtils.checkNull(request.getParamValue("sendStartDate"));
			String sendEndDate = CommonUtils.checkNull(request.getParamValue("sendEndDate"));
			String address = CommonUtils.checkNull(request.getParamValue("address"));
			String countyId = CommonUtils.checkNull(request.getParamValue("COUNTY_ID")); // 区县
			String cityId = CommonUtils.checkNull(request.getParamValue("CITY_ID")); // 地市
			String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE")); // 省份
			String isPrint = CommonUtils.checkNull(request.getParamValue("isPrint")); // 是否打印
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); // 物流商
			String common = CommonUtils.checkNull(request.getParamValue("common")); // common处理类型
			String isConfirm = CommonUtils.checkNull(request.getParamValue("isConfirm"));//运单是否确认
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("dealerCode", dealerCode);
			map.put("orderNo", orderNo);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("sendStartDate", sendStartDate);
			map.put("sendEndDate", sendEndDate);
			map.put("address", address);
			map.put("countyId", countyId);
			map.put("CITY_ID", cityId);
			map.put("PROVINCE_ID", provinceId);
			map.put("isPrint", isPrint);
			map.put("yieldly", yieldly);
			map.put("logiName", logiName);
			map.put("isConfirm", isConfirm);
			
			if("1".equals(common)){//统计 调用
				Map<String, Object> valueMap = wpDao.tgSumPrint(map);
				act.setOutData("valueMap", valueMap);	
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps =wpDao.getWaybillManageQuery(map, curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运单打印查询信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运单取消
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-20
	 */
	public void WaybillPrintCancle(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		
		try {
			/******************************页面查询字段start**************************/
			String bID = CommonUtils.checkNull(request.getParamValue("billID")); // 运单ID
			/******************************页面查询字段end***************************/
			//根据运单ID查询该运单对应的组板下时候有已确认的运单
			List<Map<String, Object>> listConfirmBill= wpDao.getConfirmBillIds(bID);
			if(listConfirmBill!=null && listConfirmBill.size()>0){
				String BillNos="";
				for(int x1= 0 ;x1< listConfirmBill.size(); x1++){
					BillNos+=listConfirmBill.get(x1).get("BILL_NO").toString();
				}
				act.setOutData("returnValue", 2);
				act.setOutData("BillNos", BillNos);
				return;
			}
			//根据运单 查询该组板下所有 运单
			List<Map<String, Object>> listBill= wpDao.getBillIds(bID);
			for(int x= 0 ;x< listBill.size(); x++)
			 {
				 String billID = listBill.get(x).get("BILL_ID").toString();
				//回写配车明细表
				wpDao.updateSendNum(Long.parseLong(billID), logonUser.getUserId());
				//回写运单表状态
				TtSalesWaybillPO po=new TtSalesWaybillPO();
	            po.setBillId(Long.parseLong(billID));
	            TtSalesWaybillPO po2=new TtSalesWaybillPO();
	            po2.setStatus(Long.parseLong(Constant.STATUS_DISABLE.toString()));
	            wpDao.update(po, po2);
	            
	            wpDao.updateVehicleStatus(Long.parseLong(billID),logonUser.getUserId());
				//回写发运组板表状态和数量
				 List<Map<String, Object>> list= wpDao.getBoIds(billID);
				 for(int i= 0 ;i< list.size(); i++)
				 {
					 String boId = list.get(i).get("BO_ID").toString();
					 StorageUtil.getInstance().updateSendNum(Long.parseLong(boId),logonUser.getUserId());
				 }
				 //更改对应组板明细中的BILL_ID
				 TtSalesBoDetailPO bdPo = new TtSalesBoDetailPO();
				 bdPo.setBillId(Long.valueOf(billID));
				 List<PO> bd_list = wpDao.select(bdPo);
				 if(bd_list != null && bd_list.size()>0){
					for(int i = 0 ; i < bd_list.size() ; i++){
						 TtSalesBoDetailPO po_ = new TtSalesBoDetailPO();
						 po_.setBoDeId(((TtSalesBoDetailPO)bd_list.get(i)).getBoDeId());
						 TtSalesBoDetailPO po_2 = new TtSalesBoDetailPO();
						 po_2.setBillId(new Long(-1));
						 wpDao.update(po_, po_2);
					}
				 }
			 }
			act.setOutData("returnValue", 1);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运单取消");
			act.setOutData("returnValue", 1);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运单打印
	 * @param      :       
	 * @return     :    
	 * @throws     : 
	 * LastDate    : 2013-5-30 ranjian
	 */
	public void WayBillPrint(){
		System.out.println("-----------------"+request.getParamValue("op"));
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String wayBillId=CommonUtils.checkNullNum(request.getParamValue("wayBillId"));	
			
			
			Map<String,Object> valueMap=wpDao.billPrintMsg(Long.parseLong(wayBillId),null) ;
			
			
			if(valueMap==null){
				valueMap = new HashMap<String, Object>();
			}
			
			List<Map<String,Object>> valueList=wpDao.billPrintMainMsg(Long.parseLong(wayBillId),null);
			List<String> orderNOs =new ArrayList<String>();
			String logiName="";
			for(Map<String,Object> value:valueList){
			String v= value.get("ORDER_NO")!=null?value.get("ORDER_NO").toString():"";
			logiName= value.get("LOGINAME")!=null?value.get("LOGINAME").toString():"";
			if(!orderNOs.contains(v)){
				orderNOs.add(v);
			}
			
			}
			act.setOutData("BONO", wayBillId);
			act.setOutData("NO", System.currentTimeMillis());
			act.setOutData("orderNOs", orderNOs);
			act.setOutData("logiName", logiName);
			act.setOutData("dealerName", valueMap.get("DEALER_NAME"));
			
			;
			//计算运单到达日期
			Date createDate = (Date)valueMap.get("CREATE_DATE"); //发运单创建时间
			BigDecimal areaId = (BigDecimal)valueMap.get("AREA_ID");
			BigDecimal cityId = (BigDecimal)valueMap.get("CITY_ID");
			 TtSalesCityDisPO po = new TtSalesCityDisPO();
			 po.setYieldly(areaId!=null ?areaId.longValue():0L);
			 po.setCityId(cityId!=null?cityId.longValue():0L);
			 List<PO> list = wpDao.select(po);
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
			act.setOutData("valueMap", valueMap);
			act.setOutData("valueList", valueList);
			
			if("1".equals(request.getParamValue("op"))){
				act.setForword(WaybillPrintUtl);
			}else{
				act.setForword(WaybillPrintUtl2);
			}
		
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运单打印");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运单打印回写
	 * @param      :       
	 * @return     :    
	 * @throws     : 
	 * LastDate    : 2013-9-3 ranjian
	 */
	public void updateWayBill(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String wayBillId=CommonUtils.checkNull(request.getParamValue("wayBillId"));
			TtSalesWaybillPO po=new TtSalesWaybillPO();
			po.setBillPrintDate(new Date());
			po.setBillPrintPer(logonUser.getUserId());//打印人
			
			TtSalesWaybillPO po1=new TtSalesWaybillPO();
			po1.setBillId(Long.parseLong(wayBillId));
			wpDao.update(po1, po);
			act.setOutData("returnValue", 1);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运单打印回写");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运单确认【已确认后才回填发运数量】
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-20
	 */
	public void WayBillConfirmMain() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			/****************************** 页面查询字段start **************************/
			String billId = CommonUtils.checkNull(request
					.getParamValue("billID")); // 运单ID
			// 首先更新运单表确认状态为已确认
			TtSalesWaybillPO tws = new TtSalesWaybillPO();
			tws.setBillId(Long.parseLong(billId));
			TtSalesWaybillPO tw = new TtSalesWaybillPO();
			tw.setConfirmDate(new Date());
			tw.setIsConfirm(Constant.IF_TYPE_YES);// 已确认
			List<Map<String, Object>> bills = wpDao.queryWayBillById(billId);
			if (bills != null && bills.size() > 0 && (Constant.IF_TYPE_NO+"").equals(bills.get(0).get("IS_CONFIRM")+"")) {
				wpDao.update(tws, tw);
				// 修改车辆状态
				wpDao.updateVStatusConf(Long.parseLong(billId),
						logonUser.getUserId());
				// //修改车辆状态
				// List<Map<String,Object>>
				// ls=wpDao.getBoIdByBillId(Long.parseLong(billId));
				// if(ls!=null && ls.size()>0){
				// for(int i=0;i<ls.size();i++){
				// Map<String,Object> t1=(Map<String,Object>)ls.get(i);
				// //修改车辆表状态
				// wpDao.updateVStatusConf(Long.parseLong(t1.get("BO_ID").toString()),logonUser.getUserId());
				// //修改发运数量
				// //StorageUtil.getInstance().updateSendNum(Long.parseLong(t1.get("BO_ID").toString()),logonUser.getUserId());
				// }
				// act.setOutData("returnValue", 1);
				// }else{
				// throw new Exception("该运单下无组板信息!");
				// }

				// 删除资源预留表数据，释放资源
				List<Map<String, Object>> allocaDetails = wmDao
						.queryAllocaDetail(billId);
				if (allocaDetails != null && allocaDetails.size() > 0) {
					for (Map<String, Object> alloca : allocaDetails) {
						List<Map<String, Object>> reserveList = rsDao
								.getReserveByVehicleId(alloca.get("VEHICLE_ID")
										.toString());
						if (reserveList != null && reserveList.size() > 0) {
							Map<String, Object> obj = reserveList.get(0);
							TtVsOrderResourceReservePO tsPO = new TtVsOrderResourceReservePO();
							Long reserveId = Long.parseLong(obj.get(
									"RESERVE_ID").toString());
							int Amount = Integer.parseInt(obj.get("AMOUNT")
									.toString());
							tsPO.setReserveId(reserveId);
							if (Amount > 1) {
								List<Object> params = new ArrayList<Object>();
								params.add(reserveId);
								params.add(Amount);
								rsDao.Amount(params);
							} else {
								rsDao.delete(tsPO);
							}
						}
					}
				}
				TtSalesWaybillPO condition = new TtSalesWaybillPO();
				condition.setBillId(Long.parseLong(billId));
				List<PO> polist = wpDao.select(condition);
//				if (polist.size() > 0) {
//					TtSalesWaybillPO po = (TtSalesWaybillPO) polist.get(0);
//					String billNo = po.getBillNo();
//					boolean result = DeliveryNoteService.getDeliveryNoteService().sendDeliveryNode(billNo,
//							ErpInterfaceConstants.DELIVERY_LGORT_VEHICLE_TYPE);
//					if(!result){
//		            	throw new BizException(act,new RuntimeException(),ErrorCodeConstant.SPECIAL_MEG,"ERP接收失败！");
//					}
//					//TODO
//				} else {
//					BizException e1 = new BizException(act,
//							ErrorCodeConstant.QUERY_FAILURE_CODE, "运单确认发送至ERP");
//					act.setException(e1);
//				}
				act.setOutData("returnValue", 1);
			} else {
				act.setOutData("returnValue", 2);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "运单确认");
			act.setOutData("returnValue", 1);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
