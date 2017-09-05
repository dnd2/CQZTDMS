package com.infodms.dms.actions.sales.storage.sendmanage;

import java.util.ArrayList;
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
import com.infodms.dms.dao.sales.storage.sendManage.TransferStockDao;
import com.infodms.dms.dao.sales.storage.storagebase.ReservoirPositionDao;
import com.infodms.dms.dao.sales.storage.storagemanage.VehicleSiteAdjustDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtSalesRecProcessHisPO;
import com.infodms.dms.po.TtSalesSitPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName : TransferStock
 * @Description : 换下车入库控制类
 * @author : ranjian CreateDate : 2013-4-28
 */
public class TransferStock {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final TransferStockDao reDao = TransferStockDao.getInstance();
	private final ReservoirPositionDao reBDao = ReservoirPositionDao
			.getInstance();
	private final VehicleSiteAdjustDao vsDao = VehicleSiteAdjustDao
			.getInstance();

	private final String transferStockInitUrl = "/jsp/sales/storage/sendmanage/transferStock/transferStockList.jsp";
	private final String transferStockMainInitUrl = "/jsp/sales/storage/sendmanage/transferStock/transferStock.jsp";

	/**
	 * 
	 * @Title :
	 * @Description: 换下车入库初始化查询条件
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-28
	 */
	public void transferStockInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(transferStockInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "换下车入库查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 换下车入库查询信息
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-28
	 */
	public void transferStockQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
				.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			/****************************** 页面查询字段start **************************/
			String groupCode = request.getParamValue("groupCode"); // 物料组
			String materialCode = request.getParamValue("materialCode"); // 物料
			String offlineStartDate = request
					.getParamValue("OFFLINE_STARTDATE"); // 下线日期开始
			String offlineEndDate = request.getParamValue("OFFLINE_ENDDATE"); // 下线日期结束
			String orgStartDate = request
					.getParamValue("ORG_STORAGE_STARTDATE"); // 入库日期开始
			String orgEndDate = request.getParamValue("ORG_STORAGE_ENDDATE"); // 入库日期结束
			String vin = request.getParamValue("VIN"); // vin
			/****************************** 页面查询字段end ***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("offlineStartDate", offlineStartDate);
			map.put("offlineEndDate", offlineEndDate);
			map.put("orgStartDate", orgStartDate);
			map.put("orgEndDate", orgEndDate);
			map.put("vin", vin);
			map.put("areaIds", areaIds);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = reDao
					.getReceivingStorageQuery(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 换下车入库初始化
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-28
	 */
	public void transferStockMainInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String areaIds = MaterialGroupManagerDao
					.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
			/********************************* 车辆信息查询START ***************************************/
			String vechileId = CommonUtils.checkNull(request
					.getParamValue("Id"));// 车辆ID
			List<Object> verParams = new ArrayList<Object>();
			verParams.add(vechileId);
			Map<String, Object> vehicleMap = reDao.getVechileByVId(verParams);
			/********************************* END **************************************/
			List<Map<String, Object>> list_An = reBDao
					.getReservoirValue(areaIds);// 获取库区列表
			act.setOutData("list_An", list_An);// 库区信息
			act.setOutData("vehicleMap", vehicleMap);//
			act.setForword(transferStockMainInitUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库新增初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 保存换下车入库信息
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-28
	 */
	public void saveTransferStock() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String vehicleId = CommonUtils.checkNull(request
					.getParamValue("VEHICLE_ID"));// 车辆ID
			String sitId = CommonUtils.checkNull(request
					.getParamValue("SIT_NAME")); // 库位ID

			/******************************* 库位码获取start（不能根据前台选中值获取） ****************************/
			List<Object> newParams = new ArrayList<Object>();
			newParams.add(sitId);
			Map<String, Object> newMap = vsDao.getStorageBySId(newParams);// 获取区道位信息
			String sitCode = "";
			if (newMap != null) {// 如以后库位码变动则需改动此地方（暂时规则：区-道-位）
				if(Integer.parseInt(newMap.get("VEHICLE_ID").toString())==Constant.DEFAULT_VALUE.intValue()){
				List<Object> scode = new ArrayList<Object>();
				scode.add(newMap.get("AREA_NAME"));
				scode.add(newMap.get("ROAD_NAME"));
				scode.add(newMap.get("SIT_NAME"));
				sitCode = CommonUtils.getSitCode(scode);// 获取库位码
				/******************************* 库位码获取end ******************************************************/
				/************************************* 调整车辆信息START *********************************************/
				Integer lockStatus = Constant.LOCK_STATUS_01;// 正常状态
				if (Integer.parseInt(newMap.get("AREA_TYPE").toString())==Constant.RES_TYPE_02)
				{//借出区
					lockStatus = Constant.LOCK_STATUS_13;
				}
				if (Integer.parseInt(newMap.get("AREA_TYPE").toString())==Constant.RES_TYPE_03)
				{//质损区
					lockStatus = Constant.LOCK_STATUS_04;
				}
				if (Integer.parseInt(newMap.get("AREA_TYPE").toString())==Constant.RES_TYPE_04)
				{//预留区
					lockStatus = Constant.LOCK_STATUS_03;
				}
				TmVehiclePO tvpo = new TmVehiclePO();// 修改字段
				tvpo.setSitCode(sitCode);
				tvpo.setLockStatus(lockStatus);// 锁定状态
				tvpo.setLifeCycle(Constant.VEHICLE_LIFE_02);// 车厂库存
				tvpo.setSitId(Long.parseLong(sitId));
				tvpo.setUpdateBy(logonUser.getUserId());
				tvpo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				TmVehiclePO seachTvpo = new TmVehiclePO();// //查询条件
				seachTvpo.setVehicleId(Long.parseLong(vehicleId));
				vsDao.vehicleUpdate(seachTvpo, tvpo);// 修改车辆信息
				/******************************************** 处理库位表Start *******************************/
				TtSalesSitPO tsspoNew = new TtSalesSitPO();
				tsspoNew.setVehicleId(Long.parseLong(vehicleId));// 车辆ID
				tsspoNew.setUpdateBy(logonUser.getUserId());
				tsspoNew.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				TtSalesSitPO seachNew = new TtSalesSitPO();
				seachNew.setSitId(Long.parseLong(sitId));// 库位ID
				reBDao.sitUpdate(seachNew, tsspoNew);// 修改库位信息
				/******************************************** 处理库位表end ********************************/
				/*****处理换车表已退库状态*****/
				StringBuffer sbSql=new StringBuffer();
				sbSql.append("UPDATE TT_SALES_CHA_HIS A SET A.IS_RETURN="+Constant.IF_TYPE_YES+" WHERE A.OLD_VEHICLE_ID="+vehicleId+" AND A.IS_RETURN="+Constant.IF_TYPE_NO+"\n");
				reBDao.update(sbSql.toString(), null);
				//记录退车入库信息（保留历史）
				TtSalesRecProcessHisPO rppo=new TtSalesRecProcessHisPO();
				rppo.setRecHisId(Long.parseLong(SequenceManager.getSequence("")));
				rppo.setSitId(Long.parseLong(sitId));
				rppo.setVehicleId(Long.parseLong(vehicleId));
				rppo.setRecType(Constant.REC_TYPE01);//退库类型
				rppo.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				rppo.setCreateBy(logonUser.getUserId());
				rppo.setRecPer(logonUser.getUserId());
				rppo.setRecDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				reDao.insert(rppo);
				act.setOutData("vehicleId", vehicleId);//返回车辆ID
				act.setOutData("returnValue", 1);// 成功
				}else{
					act.setOutData("returnValue", 3);//该库位已被占用
				}
			} else {
				act.setOutData("returnValue", 2);// 库位错误
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

}
