package com.infodms.dms.actions.sales.storage.sendmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

//import com.infodms.dms.actions.erpinterfaces.DeliveryNoteService;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import 

com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
//import com.infodms.dms.dao.common.ErpInterfaceConstants;
import com.infodms.dms.dao.sales.storage.sendManage.RebackStockDao;
import com.infodms.dms.dao.sales.storage.sendManage.TransferStockDao;
import 

com.infodms.dms.dao.sales.storage.storagebase.ReservoirPositionDao;
import 

com.infodms.dms.dao.sales.storage.storagemanage.VehicleSiteAdjustDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmPlanDetailPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtSalesAllocaDePO;
import com.infodms.dms.po.TtSalesFinAccPO;
import com.infodms.dms.po.TtSalesRecProcessHisPO;
import com.infodms.dms.po.TtSalesSitPO;
import com.infodms.dms.po.TtVsReturnVehicleReqPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : TransferStock 
 * @Description   : 退回车入库控制类
 * @author        : ranjian
 * CreateDate     : 2013-5-28
 */
public class RebackStock {
	public Logger logger = Logger.getLogger

(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final RebackStockDao reDao = RebackStockDao.getInstance();
	private final ReservoirPositionDao reBDao = ReservoirPositionDao.getInstance();
	private final VehicleSiteAdjustDao vsDao = VehicleSiteAdjustDao.getInstance();
	private final TransferStockDao tsDao = TransferStockDao.getInstance();
	
	private final String rebackStockInitUrl = "/jsp/sales/storage/sendmanage/rebackStock/rebackStockList.jsp";
	private final String rebackStockMainInitUrl = "/jsp/sales/storage/sendmanage/rebackStock/rebackStock.jsp";

	/**
	 * 
	 * @Title      : 
	 * @Description: 退回车入库初始化查询条件
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-28
	 */
	public void rebackStockInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(rebackStockInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"退回车入库查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 退回车入库查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-28
	 */
	public void rebackStockQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			/******************************页面查询字段start**************************/
			String groupCode = request.getParamValue("groupCode"); // 物料组
			String materialCode = request.getParamValue("materialCode"); // 物料
			String offlineStartDate = request.getParamValue("OFFLINE_STARTDATE"); // 下线日期开始
			String offlineEndDate = request.getParamValue("OFFLINE_ENDDATE"); // 下线日期结束
			String productStartDate = request.getParamValue("PRODUCT_STARTDATE"); // 入库日期开始
			String productEndDate = request.getParamValue("PRODUCT_ENDDATE"); // 入库日期结束		
			String updateStartDate = request.getParamValue("UPDATE_STARTDATE"); // 退库日期开始
			String updateEndDate = request.getParamValue("UPDATE_ENDDATE"); // 退库日期结束		
			String vin = request.getParamValue("VIN"); // vin
			String reqNo = request.getParamValue("REQ_NO"); // 退库单号
			String rlockstatus = request.getParamValue("LOCK_STATUS"); // 选择是否退库
			String invoiceStatus = request.getParamValue("INVOICE_STATUS");//开票状态
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("offlineStartDate", offlineStartDate);
			map.put("offlineEndDate", offlineEndDate);
			map.put("productStartDate", productStartDate);
			map.put("productEndDate", productEndDate);	

		
			map.put("updateStartDate", updateStartDate);
			map.put("updateEndDate", updateEndDate);	

		
			map.put("vin", vin);
			map.put("REQ_NO", reqNo);
			map.put("areaIds", areaIds);
			map.put("LOCK_STATUS", rlockstatus);
			map.put("invoiceStatus", invoiceStatus);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getRebackStockQuery(map, curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "退回车入库查询信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 退回车入库初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-28
	 */
	public void rebackStockMainInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String areaIds = MaterialGroupManagerDao
			.getPoseIdBusinessIdStr(logonUser.getPoseId().toString()); 
			/*********************************车辆信息查询START***************************************/
			String vechileId=CommonUtils.checkNull(request.getParamValue("Id"));//车辆ID
			List<Object> verParams=new ArrayList<Object>();
			verParams.add(vechileId);
			Map<String,Object> vehicleMap =tsDao.getVechileByVId(verParams);
			/*********************************END**************************************/
			List<Map<String, Object>> list_An = reBDao.getReservoirValue(areaIds);//获取库区列表			
			act.setOutData("list_An", list_An);//库区信息
			act.setOutData("vehicleMap", vehicleMap);//
			act.setForword(rebackStockMainInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退回车入库初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 保存退车入库信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-28
	 */
	public void saveRebackStock(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		//获取用户所在的职位
		try {
			String sitId = CommonUtils.checkNull(request.getParamValue("SIT_NAME")); // 库位ID
			String vehicleId=CommonUtils.checkNull(request.getParamValue("VEHICLE_ID"));//车辆ID
			List<Object> params= new ArrayList<Object>();
			params.add(sitId);
			//根据库位ID查询库道位信息
			Map<String, Object> map=vsDao.getStorageBySId(params);
			String sitCode="";//库位码
			if(map!=null){
				if(map.get("VEHICLE_ID").toString().equals(Constant.DEFAULT_VALUE.toString())){
					List<Object> scode=new ArrayList<Object>();
					scode.add(map.get("AREA_NAME"));
					scode.add(map.get("ROAD_NAME"));
					scode.add(map.get("SIT_NAME"));
					sitCode=CommonUtils.getSitCode(scode);//获取库位码
					Integer lockStatus=Constant.LOCK_STATUS_01;//正常状态
					if (Integer.parseInt(map.get("AREA_TYPE").toString())==Constant.RES_TYPE_02)
					{//借出区
						lockStatus = Constant.LOCK_STATUS_13;
					}
					if (Integer.parseInt(map.get("AREA_TYPE").toString())==Constant.RES_TYPE_03)
					{//质损区
						lockStatus = Constant.LOCK_STATUS_04;
					}
					if (Integer.parseInt(map.get("AREA_TYPE").toString())==Constant.RES_TYPE_04)
					{//预留区
						lockStatus = Constant.LOCK_STATUS_03;
					}
					TmVehiclePO tvpo=new TmVehiclePO();//修改字段
					tvpo.setSitCode(sitCode);//库位码
					tvpo.setLockStatus(lockStatus);//锁定状态
					tvpo.setSitId(Long.parseLong(sitId));//库位ID
					tvpo.setDealerId(Long.parseLong(Constant.DEFAULT_VALUE.toString()));
					tvpo.setUpdateBy(logonUser.getUserId());
					tvpo.setLifeCycle(Constant.VEHICLE_LIFE_02);//生命周期为车厂库存
					tvpo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
					TmVehiclePO seachTvpo=new TmVehiclePO();////查询条件
					seachTvpo.setVehicleId(Long.parseLong(vehicleId));
					
					
					/*******20130917无生产计划的车辆退库为了挂站点生成一个生产计划beg add by liuxh*******/
					
					TmVehiclePO vePo=new TmVehiclePO();
					vePo.setVehicleId(Long.valueOf(vehicleId));
					vePo=(TmVehiclePO)reBDao.select(vePo).get(0);
					String planDetailId=String.valueOf(vePo.getPlanDetailId());
					String id=SequenceManager.getSequence("");
					if(planDetailId.equals("")||planDetailId.equals("-1")||planDetailId.equals("0")){
						TmPlanDetailPO detail=new TmPlanDetailPO();
						detail.setPlanDetailId(Long.valueOf(id));
						String yieldly=String.valueOf(vePo.getYieldly());
						if(yieldly.equals("2010010100000001")) detail.setPlanId(2013082311876831L);//随便挂一个同产地的生产计划 写死
						if(yieldly.equals("2010010100000002")) detail.setPlanId(2013082311876856L);
						if(yieldly.equals("2010010100000003")) detail.setPlanId(2013082311876245L);
						detail.setTotalOrderNo(CommonUtils.getBusNo(Constant.NOCRT_TOTAL_PRO_NO, 1L));
						detail.setMaiId(vePo.getMaterialId());
						detail.setPlanNum(1L);
						detail.setInNum(1L);
						detail.setOrgId(vePo.getProvId());
						detail.setCreateDate(new java.util.Date());
						detail.setRemark("老车辆退库自动生成订单");
						detail.setCheckStatus(12121002L);
						reDao.insert(detail);	
						tvpo.setPlanDetailId(Long.valueOf(id));
					}
					/*******20130917无生产计划的车辆退库为了挂站点生成一个生产计划beg add by end*******/
					
					vsDao.vehicleUpdate(seachTvpo,tvpo);//修改车辆信息
					TtSalesSitPO tsspoNew=new TtSalesSitPO();
					tsspoNew.setVehicleId(Long.parseLong(vehicleId));//车辆ID
					tsspoNew.setUpdateBy(logonUser.getUserId());
					tsspoNew.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
					TtSalesSitPO seachNew=new TtSalesSitPO();
					seachNew.setSitId(Long.parseLong(sitId));//库位ID
					reBDao.sitUpdate(seachNew, tsspoNew);//修改库位信息
					//记录退车入库信息（保留历史）
					TtSalesRecProcessHisPO rppo=new TtSalesRecProcessHisPO();
					rppo.setRecHisId(Long.parseLong(SequenceManager.getSequence("")));
					rppo.setVehicleId(Long.parseLong(vehicleId));
					rppo.setSitId(Long.parseLong(sitId));
					rppo.setRecType(Constant.REC_TYPE02);//退库类型
					rppo.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
					rppo.setCreateBy(logonUser.getUserId());
					rppo.setRecPer(logonUser.getUserId());
					rppo.setRecDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
					reDao.insert(rppo);
					//修改配车明细表（状态，以前配车状态改为无效）
					TtSalesAllocaDePO adpo=new TtSalesAllocaDePO();
					adpo.setStatus(Constant.STATUS_DISABLE);
					adpo.setUpdateBy(logonUser.getUserId());
					adpo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
					TtSalesAllocaDePO adpoSeach=new TtSalesAllocaDePO();
					adpoSeach.setVehicleId(Long.parseLong(vehicleId));
					adpoSeach.setStatus(Constant.STATUS_ENABLE);
					reDao.update(adpoSeach, adpo);
					act.setOutData("vehicleId", vehicleId);//返回车辆ID
					act.setOutData("returnValue", 1);//成功
					
					/*//当入库成功的时候，进行资金的结算。
					 //资金依据原始单据计算合计金额【资金类型金额+返利金额】退回资金类型
					//退车详细表
					TtVsReturnVehicleReqPO tvrPo = new TtVsReturnVehicleReqPO();
					tvrPo.setVehicleId(Long.parseLong(vehicleId));
					tvrPo.setReturnMoney(null);				
					
					//根据id查询信息
					List<Map<String, Object>list = reDao.queryByVehicleId(vehicleId);
					if(list.size() > 0)
					{
						Map<String, Object> obj = list.get(0);
						TtVsReturnVehicleReqPO trPo = new TtVsReturnVehicleReqPO();
						trPo.setVehicleId(Long.parseLong(vehicleId));
						trPo.setReturnMoney(Double.parseDouble(obj.get("DISCOUNT_S_PRICE").toString()));
						reDao.update(tvrPo,trPo);
						//经销商账户表
						TtSalesFinAccPO  tsfPO = new TtSalesFinAccPO();
						tsfPO.setFinType(Long.parseLong(obj.get("FUND_TYPE_ID").toString()));
						tsfPO.setAmount(Double.parseDouble(obj.get("AMOUNT").toString()));
						TtSalesFinAccPO  tsPO = new TtSalesFinAccPO();
						tsPO.setFinType(Long.parseLong(obj.get("FUND_TYPE_ID").toString()));
						tsPO.setAmount(Double.parseDouble(obj.get("AMOUNT_ALL").toString()));
						reDao.update(tsfPO,tsPO);
					}*/
					TmVehiclePO tm = new TmVehiclePO();
					tm.setVehicleId(Long.parseLong(vehicleId));
					List<PO> tmList = reDao.select(tm);
//					if(tmList.size() > 0 ){
//						TmVehiclePO vh = (TmVehiclePO) tmList.get(0);
//						String vin = vh.getVin();
//						boolean result = DeliveryNoteService.getDeliveryNoteService().sendDeliveryNode(vin, ErpInterfaceConstants.DELIVERY_LGORT_REBACK_VEHICLE);
//						if(!result){
//		            		throw new BizException(act,new RuntimeException(),ErrorCodeConstant.SPECIAL_MEG,"ERP接收失败！");
//		            	}
//						//TODO
//					}				
				}else{
					act.setOutData("returnValue", 3);//该库位已被占用
				}
			}else{
				act.setOutData("returnValue", 2);//库位错误
			}		
		}catch(BizException e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ERP_ERROR, "配件退货");act.setException(e1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"保存退车入库信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

}
		
