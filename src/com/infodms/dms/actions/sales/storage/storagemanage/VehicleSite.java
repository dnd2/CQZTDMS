package com.infodms.dms.actions.sales.storage.storagemanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storage.storagebase.ReservoirPositionDao;
import com.infodms.dms.dao.sales.storage.storagemanage.VehicleSiteDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
/**
 * 
 * @ClassName     : VehicleSite 
 * @Description   : 车辆信息查询控制类
 * @author        : ranjian
 * CreateDate     : 2013-4-14
 */
public class VehicleSite {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final VehicleSiteDao reDao = VehicleSiteDao.getInstance();
	private final ReservoirPositionDao reBDao = ReservoirPositionDao.getInstance();	

	private final String vehicleSiteInitUrl = "/jsp/sales/storage/storagemanage/vehicleSite/vehicleSiteList.jsp";


	/**
	 * 
	 * @Title      : 
	 * @Description: 车辆信息查询初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-14
	 */
	public void vehicleSiteInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//String areaIds = MaterialGroupManagerDao.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
			//List<Map<String, Object>> list_An = reBDao.getReservoirValue(areaIds);//获取库区列表
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getWarehouseByPoseId(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			//act.setOutData("list_An", list_An);//库区信息
			act.setForword(vehicleSiteInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"详细车籍查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 车辆信息查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-14
	 */
	public void vehicleSiteQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String _type = CommonUtils.checkNull(request.getParamValue("_type")); // 处理类型(2代表导出)
			/******************************页面查询字段start**************************/
			String YIELDLY = CommonUtils.checkNull(request.getParamValue("YIELDLY")); //仓库
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));
			String packageCode = CommonUtils.checkNull(request.getParamValue("packageCode"));
			String productDateStart = CommonUtils.checkNull(request.getParamValue("productDateStart"));//生产日期开始时间
			String productDateEnd = CommonUtils.checkNull(request.getParamValue("productDateEnd"));//生产日期开始时间
			String offlineStartDate = CommonUtils.checkNull(request.getParamValue("OFFLINE_STARTDATE")); // 下线日期开始
			String offlineEndDate = CommonUtils.checkNull(request.getParamValue("OFFLINE_ENDDATE")); // 下线日期结束
			String orgStartDate = CommonUtils.checkNull(request.getParamValue("ORG_STORAGE_STARTDATE")); // 入库日期开始
			String orgEndDate = CommonUtils.checkNull(request.getParamValue("ORG_STORAGE_ENDDATE")); // 入库日期结束
			String vin = CommonUtils.checkNull(request.getParamValue("VIN")); // vin
			String LIFE_CYCLE = CommonUtils.checkNull(request.getParamValue("LIFE_CYCLE")); //产地
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("modelCode", modelCode);
			map.put("packageCode", packageCode);
			map.put("offlineStartDate", offlineStartDate);
			map.put("offlineEndDate", offlineEndDate);
			map.put("orgStartDate", orgStartDate);
			map.put("orgEndDate", orgEndDate);
			map.put("vin", vin);
			map.put("poseId", logonUser.getPoseId());
			map.put("YIELDLY", YIELDLY);	
			map.put("LIFE_CYCLE", LIFE_CYCLE);
			map.put("productDateStart", productDateStart);
			map.put("productDateEnd", productDateEnd);
			if("3".equals(_type)){
				List<Map<String, Object>> list = reDao.getReceivingStorageQueryExport(map);
				String[] headExport={"库存状态","锁定状态","仓库","车型","配置","物料编码","颜色","VIN","生产日期","下线日期","入库日期","出库日期","实销日期","发动机号","变速箱号"};
				String[] columns={"LIFE_CYCLE_NAME","LOCK_NAME","AREA_NAME","MODEL_NAME","PACKAGE_NAME","MATERIAL_CODE","COLOR_NAME","VIN","PRODUCT_DATE","OFFLINE_DATE","ORG_STORAGE_DATE","FACTORY_DATE","PURCHASED_DATE","GEARBOX_NO","ENGINE_NO"};
				ToExcel.toReportExcel(act.getResponse(), request,"详细车籍查询.xls", headExport,columns,list);
			}
			/*else if("2".equals(_type)){
				List<Map<String, Object>> list = reDao.getReceivingStorageQueryExport(map);
				String[] headExport={"入库时间","底盘号"};
				String[] columns={"ORG_STORAGE_DATE","VIN"};
				ToExcel.toReportExcel(act.getResponse(), request,"入库车辆导出.xls", headExport,columns,list);
			}*/else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getReceivingStorageQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 车辆信息查询导出
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-14
	 */
	public void vehicleExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			/******************************页面查询字段start**************************/
			String orgStartDate = request.getParamValue("ORG_STORAGE_STARTDATE"); // 入库日期开始
			String orgEndDate = request.getParamValue("ORG_STORAGE_ENDDATE"); // 入库日期结束
			String YIELDLY = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // vin
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orgStartDate", orgStartDate);
			map.put("orgEndDate", orgEndDate);
			map.put("areaId", areaIds);
			map.put("YIELDLY", YIELDLY);
			List<Map<String, Object>> list = reDao.getReceivingStorageQueryExport(map);
			String[] headExport={"入库时间","底盘号"};
			String[] columns={"ORG_STOTAGE_DATE","VIN"};
			ToExcel.toReportExcel(act.getResponse(), request,"入库车辆导出.xls", headExport,columns,list);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆信息导出");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
