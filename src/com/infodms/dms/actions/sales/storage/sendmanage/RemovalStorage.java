package com.infodms.dms.actions.sales.storage.sendmanage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
//import com.infodms.dms.actions.erpinterfaces.DeliveryNoteService;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
//import com.infodms.dms.dao.common.ErpInterfaceConstants;
import com.infodms.dms.dao.sales.storage.StorageUtil;
import com.infodms.dms.dao.sales.storage.sendManage.RemovalStorageDao;
import com.infodms.dms.dao.sales.storage.sendManage.WaybillManageDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.dao.sales.storage.storagebase.ReservoirRoadDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesBoardPO;
import com.infodms.dms.po.TtSalesRoadPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.po.TtVsOrderResourceReservePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 /**
 * 
 * @ClassName : RemovalStorage
 * @Description : 车辆出库
 * @author : ranjian
 *         CreateDate : 2013-5-15
 */
public class RemovalStorage {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final RemovalStorageDao reDao = RemovalStorageDao.getInstance();
	private final ReservoirRoadDao reRDao = ReservoirRoadDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final StorageUtil stoUtil = StorageUtil.getInstance();
	private final String removalStorageInitUtl = "/jsp/sales/storage/sendmanage/removalStorage/removalStorageList.jsp";
	private final String removalStorageQueryInitUtl = "/jsp/sales/storage/sendmanage/removalStorage/removalStorageQueryList.jsp";
	private final String VHCL_STORAGE_URL = "/jsp/sales/storage/sendmanage/removalStorage/vechlStorageList.jsp";
	
	/**
	 * @Title :
	 * @Description: 车辆出库初始化
	 * @param :
	 * @return :
	 * @throws :
	 *         LastDate : 2013-5-15
	 */
	public void removalStorageInit()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
						.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try
		{
			List<Map<String, Object>> list_yieldly = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			act.setForword(removalStorageInitUtl);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
							"车辆出库初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 方法描述 ：车辆可出库组板查询 <br/>
	 * 
	 * @author wangsongwei
	 */
	public void boardStorage()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try
		{
			HashMap<String, Object> hsMap = new HashMap<String, Object>();
			
			/****************************** 页面查询字段start **************************/
			String groupCode = request.getParamValue("groupCode"); // 物料组
			String materialCode = request.getParamValue("materialCode"); // 物料
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商ID
			String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")); // 订单类型
			String orderNo = CommonUtils.checkNull(request.getParamValue("ORDER_NO")); // 销售订单号
			String invoiceNo = CommonUtils.checkNull(request.getParamValue("INVOICE_NO")); // 发票号
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流公司
			String boStartDate = CommonUtils.checkNull(request.getParamValue("BO_STARTDATE")); //组板日期开始
			String boEndDate = CommonUtils.checkNull(request.getParamValue("BO_ENDDATE")); // 组板日期结束
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); // 组板编号
			String allocaStartDate = CommonUtils.checkNull(request.getParamValue("ALLOCA_STARTDATE")); //配车日期开始
			String allocaEndDate = CommonUtils.checkNull(request.getParamValue("ALLOCA_ENDDATE")); // 配车日期结束
			/****************************** 页面查询字段end ***************************/
			hsMap.put("yieldly", yieldly);
			hsMap.put("dealerCode", dealerCode);
			hsMap.put("orderType", orderType);
			hsMap.put("orderNo", orderNo);
			hsMap.put("invoiceNo", invoiceNo);
			hsMap.put("logiName", logiName);
			hsMap.put("boStartDate", boStartDate);
			hsMap.put("boEndDate", boEndDate);
			hsMap.put("boNo", boNo);
			hsMap.put("allocaStartDate", allocaStartDate);
			hsMap.put("allocaEndDate", allocaEndDate);
			hsMap.put("poseId", logonUser.getPoseId().toString());
			hsMap.put("groupCode", groupCode);
			hsMap.put("materialCode", materialCode);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			
			PageResult<Map<String, Object>> ps = reDao.queryOutStorage(hsMap, curPage,
							Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "出库单据查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 方法描述 ： 出库车辆查询初始化<br/>
	 * 
	 * @author wangsongwei
	 */
	public void boardStorageQueryInit()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try
		{
			String boId = CommonUtils.checkNull(request.getParamValue("boId"));
			
			act.setOutData("boId", boId);
			act.setOutData("userAreaId", areaIds);//产地
			act.setForword(VHCL_STORAGE_URL);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "出库车辆查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 方法描述 ： 根据组板号查询已配车辆<br/>
	 * 
	 * @author wangsongwei
	 */
	public void boardStorageQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try
		{
			String boNo = CommonUtils.checkNull(request.getParamValue("boNo"));
			
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配车查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * @Title :
	 * @Description: 车辆出库查询信息
	 * @param :
	 * @return :
	 * @throws :
	 *         LastDate : 2013-5-15
	 */
	public void removalStorageQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			/****************************** 页面查询字段start **************************/
			String groupCode = request.getParamValue("groupCode"); // 物料组
			String materialCode = request.getParamValue("materialCode"); // 物料
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商ID
			String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")); // 订单类型
			String orderNo = CommonUtils.checkNull(request.getParamValue("ORDER_NO")); // 销售订单号
			String invoiceNo = CommonUtils.checkNull(request.getParamValue("INVOICE_NO")); // 发票号
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流公司
			String boStartDate = CommonUtils.checkNull(request.getParamValue("BO_STARTDATE")); //组板日期开始
			String boEndDate = CommonUtils.checkNull(request.getParamValue("BO_ENDDATE")); // 组板日期结束
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); // 组板编号
			String allocaStartDate = CommonUtils.checkNull(request.getParamValue("ALLOCA_STARTDATE")); //配车日期开始
			String allocaEndDate = CommonUtils.checkNull(request.getParamValue("ALLOCA_ENDDATE")); // 配车日期结束
			String boId = CommonUtils.checkNull(request.getParamValue("boId"));
			
			/****************************** 页面查询字段end ***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("yieldly", yieldly);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderNo", orderNo);
			map.put("invoiceNo", invoiceNo);
			map.put("logiName", logiName);
			map.put("boStartDate", boStartDate);
			map.put("boEndDate", boEndDate);
			map.put("boNo", boNo);
			map.put("allocaStartDate", allocaStartDate);
			map.put("allocaEndDate", allocaEndDate);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("boId", boId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getRemovalStorageQuery(map, curPage,
							Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆出库查询信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * @Title :
	 * @Description: 车辆出库主方法
	 * @param :
	 * @return :
	 * @throws :
	 *         LastDate : 2013-5-15
	 */
	public void removalStorageMain()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String vin=CommonUtils.checkNull(request.getParamValue("vin"));
			//根据VIN号获取对应的组板ID
			List<Map<String, Object>> boList = reDao.getBoIdByVin(vin);
			if (boList!=null && boList.size() > 0) {
				//修改配车详细信息表出库状态
				reDao.updateOutStatus(vin, logonUser.getUserId());
				//判断出库的道是不是已出库完位为空，如果是空则出库解锁
				List<Map<String, Object>> list = reDao.getSitQuery(vin);
				if(Integer.parseInt(list.get(0).get("LCOUNT").toString())!=0){
					if(Integer.parseInt(list.get(0).get("LCOUNT").toString())==1){//库位全空1,表示位最后一位
						//修改入库状态为解锁
						TtSalesRoadPO tspo = new TtSalesRoadPO();
						tspo.setInStatus(Constant.AUTO_IN_STATUS_01.longValue());
						TtSalesRoadPO seachpo = new TtSalesRoadPO();
						seachpo.setRoadId(Long.parseLong(list.get(0).get("ROAD_ID").toString()));
						reRDao.reservoirSalesRoadAdd(seachpo, tspo);
					}else{
						//修改入库状态为锁定
						TtSalesRoadPO tspo = new TtSalesRoadPO();
						tspo.setInStatus(Constant.AUTO_IN_STATUS_02.longValue());
						TtSalesRoadPO seachpo = new TtSalesRoadPO();
						seachpo.setRoadId(Long.parseLong(list.get(0).get("ROAD_ID").toString()));
						reRDao.reservoirSalesRoadAdd(seachpo, tspo);
					}
				}
				//释放库位（库位表车辆ID位-1，相应的库道，库区为正常，入库出库解锁状态）
				reDao.releasePlace(vin);
				//判断是否是自提的单，自提单直接道经销商在途
				List<Object> param =new ArrayList<Object>();
				param.add(vin);
				Map<String,Object> map=reDao.getSendType(param);
				boolean isZT=false;
				if(map.get("SEND_TYPE").toString().equals(Constant.TRANSPORT_TYPE_01.toString())){//自提单直接可以验收
					//修改车辆表状态(生命周期为：经销商在途；锁定状态：配车锁定)
					List<Object> parms1=new ArrayList<Object>();
					parms1.add(Constant.VEHICLE_LIFE_05);//经销商在途
					parms1.add(Constant.LOCK_STATUS_01);//正常状态
					parms1.add(vin);
					//修改车辆表状态(生命周期为：车厂出库；锁定状态：配车锁定)
					reDao.vehicleStatusSet(parms1);
					isZT=true;
				}else{
					List<Object> parms1=new ArrayList<Object>();
					parms1.add(Constant.VEHICLE_LIFE_08);//车辆出库
					parms1.add(Constant.LOCK_STATUS_08);
					parms1.add(vin);
					//修改车辆表状态(生命周期为：车厂出库；锁定状态：配车锁定)
					reDao.vehicleStatusSet(parms1);
				}
				
				//设置流水号
				String passNo = CommonUtils.getBusNo(Constant.PASS_NO, Long.parseLong(Constant.areaIdJZD));
				//判断发运类型
				if (isZT) {
					//自提
					passNo = "JZ"+passNo;
				} else {
					List<Map<String, Object>> sendTypeList = reDao.queryTransWayByVin(vin);
					if (sendTypeList != null && sendTypeList.size() > 0) {
						if ((sendTypeList.get(0).get("TRANS_WAY")+"").equals(Constant.TT_TRANS_WAY_01+"")) {
							//公路运输
							passNo = "JG"+passNo;
						} else if((sendTypeList.get(0).get("TRANS_WAY")+"").equals(Constant.TT_TRANS_WAY_02+"")){
							//铁路运输
							passNo = "JT"+passNo;
						} else if((sendTypeList.get(0).get("TRANS_WAY")+"").equals(Constant.TT_TRANS_WAY_03+"")){
							//水路
							passNo = "JS"+passNo;
						} else if((sendTypeList.get(0).get("TRANS_WAY")+"").equals(Constant.TT_TRANS_WAY_04+"")){
							//其它--默认公路
							passNo = "JG"+passNo;
						} else {
							//其它--默认公路
							passNo = "JG"+passNo;
						}
					} else {
						//默认公路
						passNo = "JG"+passNo;
					}
				}
				//修改流水号
				reDao.vehiclePassNoSet(vin, passNo);
				
				//回填数量
				stoUtil.updateOutStorgeNum(Long.parseLong(boList.get(0).get("BO_ID").toString()), logonUser.getUserId());//修改出库数量
				if(isZT){
					//自提单删除资源预留表数据，释放资源
					List<Map<String, Object>> reserveList =reDao.getReserve(vin);
					if(reserveList.size() >0 && reserveList != null)
					{
						Map<String, Object> obj = reserveList.get(0);
						TtVsOrderResourceReservePO tsPO = new TtVsOrderResourceReservePO();
						Long reserveId = Long.parseLong(obj.get("RESERVE_ID").toString());
						int Amount = Integer.parseInt(obj.get("AMOUNT").toString());		
						tsPO.setReserveId(reserveId);
						if(Amount >1)
						{
							List<Object> params=new ArrayList<Object>();
							params.add(reserveId);
							params.add(Amount);
							reDao.Amount(params);
						}
						else
						{
							reDao.delete(tsPO);
						}
					}
					//修改状态
					reDao.updateSendStatus(vin, logonUser.getUserId());
					TtSalesBoardPO spo=new TtSalesBoardPO();
					spo.setBoId(Long.parseLong(boList.get(0).get("BO_ID").toString()));
					Integer handleType=((TtSalesBoardPO)reDao.select(spo).get(0)).getHandleStatus();//判断是否是最后一辆车出库
					//自提单如果出库是最后一辆车，自动生成发运单(完全出库就生成发运单)
					if(handleType.intValue()==Constant.HANDLE_STATUS05.intValue()){
						List<Map<String, Object>> wayList = WaybillManageDao.getInstance().getInfoForWayBill(boList.get(0).get("BO_ID").toString());
						if(wayList != null && wayList.size() > 0){
							for(int i = 0 ; i < wayList.size(); i++){
								Map<String, Object> wayMap = wayList.get(i);
								BigDecimal recDealerId = (BigDecimal)wayMap.get("REC_DEALER_ID");  //发运经销商ID
								BigDecimal orDealerId = (BigDecimal)wayMap.get("DEALER_ID");  //订货经销商ID
								BigDecimal addressId = (BigDecimal)wayMap.get("ADDRESS_ID");  //发运地址
								String vehicleNum = (String)wayMap.get("VEHICLE_NUM"); //车辆数量
								BigDecimal logiId = (BigDecimal)wayMap.get("LOGI_ID"); //承运商
								BigDecimal areaId = (BigDecimal)wayMap.get("AREA_ID"); //产地
								TtSalesWaybillPO po = new TtSalesWaybillPO();
								String chPrId= SequenceManager.getSequence(null);
								po.setBillId(Long.valueOf(chPrId));
								po.setSendDealerId(recDealerId.longValue());
								po.setOrDealerId(orDealerId.longValue());
								po.setLogiId(logiId.longValue());
								po.setAreaId(areaId.longValue());
								po.setAddressId(addressId.longValue());
								po.setVehNum(Integer.valueOf(vehicleNum));
								po.setStatus(Long.parseLong(Constant.STATUS_ENABLE.toString()));
								po.setBillCrtDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
								po.setBillCrtPer(logonUser.getUserId());
								po.setBillNo(CommonUtils.getBusNo(Constant.NOCRT_SEND_ORDER_NO,areaId.longValue()));
								po.setSendStatus(Long.parseLong(Constant.SEND_STATUS_01.toString()));
								po.setCreateBy(logonUser.getUserId());
								po.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
								po.setRemark("系统自动生成自提单");
								po.setIsConfirm(Constant.IF_TYPE_YES);
								po.setConfirmDate(new Date());
								reDao.insert(po);
								//回写发运组板明细表运单ID
								String sql = "UPDATE TT_SALES_BO_DETAIL SET BILL_ID = "+Long.valueOf(chPrId)+ " WHERE 1 = 1 \n"
											+" AND REC_DEALER_ID = "+recDealerId+" AND ADDRESS_ID = "+addressId+"\n"
											+" AND AREA_ID = "+areaId + " AND LOGI_ID = "+logiId
											+" AND BO_ID IN ("+boList.get(0).get("BO_ID")+")";
								reDao.update(sql,null);
								
								String sql_1 = "UPDATE TT_SALES_ALLOCA_DE SET IS_SEND = "+Constant.IF_TYPE_YES + ", \n"
											+" SEND_DATE = sysdate , SEND_BY = "+logonUser.getUserId()+"\n"
											+" WHERE STATUS="+Constant.STATUS_ENABLE+" AND BO_DE_ID IN (SELECT BO_DE_ID FROM TT_SALES_BO_DETAIL WHERE BO_ID IN ("+boList.get(0).get("BO_ID")+") "
											+" AND REC_DEALER_ID = "+recDealerId+" AND ADDRESS_ID = "+addressId+" AND AREA_ID = "+areaId+" AND LOGI_ID="+logiId+")";
								reDao.update(sql_1,null);
								stoUtil.updateSendNum(Long.parseLong(boList.get(0).get("BO_ID").toString()), logonUser.getUserId());//修改发运数量
								//改变该组板单号下的所有车辆的生命周期为经销商在途
								WaybillManageDao.getInstance().updateVehicleStatusZT(Long.parseLong(boList.get(0).get("BO_ID").toString()),logonUser.getUserId());
//								boolean result = DeliveryNoteService.getDeliveryNoteService().sendDeliveryNode(po.getBillNo(), ErpInterfaceConstants.DELIVERY_LGORT_VEHICLE_TYPE);
//								if(!result){
//				            		throw new BizException(act,new RuntimeException(),ErrorCodeConstant.SPECIAL_MEG,"ERP接收失败！");
//				            	}
								//TODO
							}
						}
					}
				}
				act.setOutData("returnValue", 1);//成功
			}else{
				act.setOutData("returnValue", 0);//失败 
			}
		}
	catch(BizException e){
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.ERP_ERROR, "车辆出库");
		logger.error(logonUser, e1);
		act.setException(e1);
	}catch (Exception e)
		{
			BizException e1 = new BizException(act,e, ErrorCodeConstant.SPECIAL_MEG, 2, "车辆出库失败!" + e.getMessage());
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void removalStorageQueryInit(){

		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
						.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try
		{
			List<Map<String, Object>> list_yieldly = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			act.setForword(removalStorageQueryInitUtl);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
							"车辆出库初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
	
    public void removalStoageVehicleQuery(){

		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String boStartDate = CommonUtils.checkNull(request.getParamValue("BO_STARTDATE")); //组板日期开始
			String boEndDate = CommonUtils.checkNull(request.getParamValue("BO_ENDDATE")); // 组板日期结束
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); // 组板编号
			String vin  = CommonUtils.checkNull(request.getParamValue("VIN")); // VIN
			String orderNo  = CommonUtils.checkNull(request.getParamValue("orderNo")); // 销售清单号
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 经销商名称
			
			String outSDate = CommonUtils.checkNull(request.getParamValue("OUT_SDATE")); //出库日期开始
			String outEDate = CommonUtils.checkNull(request.getParamValue("OUT_EDATE")); // 出库日期结束
			
			String fundType = CommonUtils.checkNull(request.getParamValue("fundType")); // 资金类型
			
			/****************************** 页面查询字段end ***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("yieldly", yieldly);
			map.put("boStartDate", boStartDate);
			map.put("boEndDate", boEndDate);
			map.put("boNo", boNo);
			map.put("VIN", vin);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("orderNo", orderNo);
			map.put("dealerName", dealerName);
			map.put("outSDate", outSDate);
			map.put("outEDate", outEDate);
			map.put("fundType", fundType);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.removelStorageQuery(map, curPage,Constant.PAGE_SIZE_MIDDLE);
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆出库查询信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
    }
    
    /**
     * 导出
     * @author liufazhong
     */
    public void removalStoageVehicleExport(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String boStartDate = CommonUtils.checkNull(request.getParamValue("BO_STARTDATE")); //组板日期开始
			String boEndDate = CommonUtils.checkNull(request.getParamValue("BO_ENDDATE")); // 组板日期结束
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); // 组板编号
			String vin  = CommonUtils.checkNull(request.getParamValue("VIN")); // VIN
			String orderNo  = CommonUtils.checkNull(request.getParamValue("orderNo")); // 销售清单号
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 经销商名称
			
			String outSDate = CommonUtils.checkNull(request.getParamValue("OUT_SDATE")); //出库日期开始
			String outEDate = CommonUtils.checkNull(request.getParamValue("OUT_EDATE")); // 出库日期结束
			
			String fundType = CommonUtils.checkNull(request.getParamValue("fundType")); // 资金类型
			
			/****************************** 页面查询字段end ***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("yieldly", yieldly);
			map.put("boStartDate", boStartDate);
			map.put("boEndDate", boEndDate);
			map.put("boNo", boNo);
			map.put("VIN", vin);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("orderNo", orderNo);
			map.put("dealerName", dealerName);
			
			map.put("outSDate", outSDate);
			map.put("outEDate", outEDate);
			
			map.put("fundType", fundType);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.removelStorageQuery(map, curPage,Constant.PAGE_SIZE_MAX);
			String[] head = {"销售清单号","资金类型","经销商","主板号","主板完成时间","VIN","车系","车型","配置","颜色","启票单价","物料代码","车辆状态","出库时间"};
			String[] columns={"ORDER_NO","FUND_TYPE","DEALER_SHORTNAME","BO_NO","BO_DATE","VIN","SERIES_NAME","MODEL_NAME","PACKAGE_NAME","COLOR_NAME","DISCOUNT_S_PRICE","MATERIAL_CODE","LIFE_CYCLE_DESC","OUT_DATE"};
			List<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
			if (ps.getRecords() != null && ps.getRecords().size() > 0) {
				maps.addAll(ps.getRecords());
			}
			try {
				ToExcel.toReportExcel(ActionContext.getContext().getResponse(), request,"车辆出库数据.xls", head,columns,maps);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		catch (Exception e)
		{// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆出库查询信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
    }
}
