package com.infodms.dms.dao.sales.storage.storagemanage;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmVehicleRetreatHistoryPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : VehicleRetreatDao 
 * @Description   : 车辆退回生产线DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-14
 */
public class VehicleRetreatDao extends BaseDao<PO>{
	private static final VehicleRetreatDao dao = new VehicleRetreatDao ();
	public static final VehicleRetreatDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
public PageResult<Map<String, Object>> queryVehicleRetreatData(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		
		String groupCode = (String)map.get("groupCode"); // 物料组
		String materialCode = (String)map.get("materialCode"); // 物料
		String areaName = (String)map.get("areaName"); // 库区
		String roadName = (String)map.get("roadName"); // 库道
		String sitName = (String)map.get("sitName"); // 库位
		String offlineStartDate = (String)map.get("offlineStartDate"); // 下线日期开始
		String offlineEndDate = (String)map.get("offlineEndDate"); // 下线日期结束
		String orgStartDate = (String)map.get("orgStartDate"); // 入库日期开始
		String orgEndDate = (String)map.get("orgEndDate"); // 入库日期结束
		String retreatStartDate = (String)map.get("retreatStartDate"); // 退回日期开始
		String retreatEndDate = (String)map.get("retreatEndDate"); // 退回日期结束
		String vin = (String)map.get("vin"); // vin
		String YIELDLY = (String) map.get("YIELDLY"); // 产地
		String retreatType = (String)map.get("retreatType"); // 2015.8.31 艾春 添加退回类型
		
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TV.VEHICLE_ID,\n" );
		sql.append("       MGM.SERIES_NAME,\n" );//车系
		sql.append("       MGM.MODEL_NAME,\n" );//车型
		sql.append("       MGM.PACKAGE_NAME,\n" );//配置
		sql.append("       MGM.MATERIAL_CODE,\n" );//物料代码
		sql.append("       MGM.COLOR_NAME,\n" );//颜色名称
		sql.append("       TV.VIN,\n" );//车架号
		sql.append("       TV.ENGINE_NO,\n" );//发动机号
		sql.append("       TSA.AREA_NAME,\n" );//产地
		sql.append("       TSR.ROAD_NAME,\n" );//道
		sql.append("       TSS.SIT_NAME,\n" );//位
		sql.append("       RT.CODE_DESC RETREAT_TYPE,\n" );//退回类型
		sql.append("       TU.NAME USER_NAME,\n" );//退回人
		sql.append("       TV.RETREATDES,\n" );//退回原因
		sql.append("       TO_CHAR(TV.RETREAT_DATE, 'YYYY-MM-DD') RETREAT_DATE,\n" );//退回日期
		sql.append("       TO_CHAR(TV.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\n" );//生产日期
		sql.append("       TO_CHAR(TV.OFFLINE_DATE, 'YYYY-MM-DD') OFFLINE_DATE,\n" );//下线日期
		sql.append("       TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD') ORG_STORAGE_DATE\n" );//入库日期
		sql.append("  FROM TM_VEHICLE_RETREAT_HISTORY TV,\n" );
		sql.append("       TT_SALES_SIT               TSS,\n" );
		sql.append("       TT_SALES_ROAD              TSR,\n" );
		sql.append("       TT_SALES_AREA              TSA,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT      MGM,\n" );
		sql.append("       TM_BUSINESS_AREA           TBA,\n" );
		sql.append("       TM_POSE_BUSINESS_AREA      TPBA,\n" );
		sql.append("       TC_USER                    TU,\n" );
		sql.append("       TC_CODE                    RT\n" );
		sql.append(" WHERE TV.SIT_ID = TSS.SIT_ID\n" );
		sql.append("   AND TSS.ROAD_ID = TSR.ROAD_ID\n" );		
		sql.append("   AND TSR.AREA_ID = TSA.AREA_ID\n" );
		sql.append("   AND TV.MATERIAL_ID = MGM.MATERIAL_ID\n" );
		sql.append("   AND TV.YIELDLY = TBA.AREA_ID\n" );
		sql.append("   AND TBA.AREA_ID = TPBA.AREA_ID\n" );
		sql.append("   AND TV.RETREAT_TYPE = RT.CODE_ID\n" );
		sql.append("   AND TV.RETREAT_BY = TU.USER_ID\n" );
		sql.append("   AND TV.ISRETREAT = "+Constant.RETREAT_STATUS02+"\n");
		sql.append("   AND TPBA.POSE_ID = ?\n");
		params.add((Long)map.get("poseId"));
		
		if(!CommonUtils.isEmpty(groupCode)){ // 物料组
			sql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar("MGM.PACKAGE_ID", groupCode, params));
		}
		if(!CommonUtils.isEmpty(materialCode)){ // 物料
			sql.append(Utility.getConSqlByParamForEqual(materialCode, params,"MGM", "MATERIAL_CODE"));
		}
		if(!CommonUtils.isEmpty(YIELDLY)){ // 产地
			sql.append("AND TPBA.AREA_ID=?\n");
			params.add(Long.valueOf(YIELDLY));
		}
		if(!CommonUtils.isEmpty(areaName)){//库区过滤
			sql.append("   AND TSA.AREA_ID = ?\n" );
			params.add(areaName);
		}
		if(!CommonUtils.isEmpty(roadName)){//库道过滤
			sql.append("   AND TSR.ROAD_ID = ?\n" );
			params.add(roadName);
		}
		if(!CommonUtils.isEmpty(sitName)){//库位过滤
			sql.append("   AND TSS.SIT_ID = ?\n" );
			params.add(sitName);
		}
		if(!CommonUtils.isEmpty(retreatStartDate)){//退回日期开始过滤
			sql.append("   AND TV.RETREAT_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(retreatStartDate+" 00:00:00");
			
		}
		if(!CommonUtils.isEmpty(retreatEndDate)){//退回日期结束过滤
			sql.append("   AND TV.RETREAT_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(retreatEndDate+" 23:59:59");
		}		
		if(!CommonUtils.isEmpty(offlineStartDate)){//下线日期开始过滤
			sql.append("   AND TV.OFFLINE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(offlineStartDate+" 00:00:00");
			
		}
		if(!CommonUtils.isEmpty(offlineEndDate)){//下线日期结束过滤
			sql.append("   AND TV.OFFLINE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(offlineEndDate+" 23:59:59");
		}
		if(!CommonUtils.isEmpty(orgStartDate)){// 入库日期开始过滤
			sql.append("   AND TV.ORG_STORAGE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orgStartDate+" 00:00:00");
		}
		if(!CommonUtils.isEmpty(orgEndDate)){// 入库日期结束过滤
			sql.append("   AND TV.ORG_STORAGE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orgEndDate+" 23:59:59");
		}
		if(!CommonUtils.isEmpty(vin)){//vin过滤
			sql.append("   AND TV.VIN LIKE ?\n" );
			params.add("%"+vin+"%");
		}
		if(!CommonUtils.isEmpty(retreatType)){//退回类型过滤
			sql.append("   AND TV.RETREAT_TYPE = ?\n" );
			params.add(retreatType);
		}
		sql.append("ORDER BY TSA.AREA_ID,TO_NUMBER(TSR.ROAD_NAME),TO_NUMBER(TSS.SIT_NAME)");//排序
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	
	/**
	 * 车辆退回生产线信息查询（只可退回当天入库车辆）
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getReceivingStorageQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		
		String groupCode = (String)map.get("groupCode"); // 物料组
		String materialCode = (String)map.get("materialCode"); // 物料
		String areaName = (String)map.get("areaName"); // 库区
		String roadName = (String)map.get("roadName"); // 库道
		String sitName = (String)map.get("sitName"); // 库位
		String storageDays = (String)map.get("storageDays"); // 超过天数
		String offlineStartDate = (String)map.get("offlineStartDate"); // 下线日期开始
		String offlineEndDate = (String)map.get("offlineEndDate"); // 下线日期结束
		String orgStartDate = (String)map.get("orgStartDate"); // 入库日期开始
		String orgEndDate = (String)map.get("orgEndDate"); // 入库日期结束
		String vin = (String)map.get("vin"); // vin
		String areaIds = (String)map.get("areaIds"); // vin
		String YIELDLY = (String) map.get("YIELDLY"); // vin
		String retreatType = (String)map.get("retreatType"); // 2015.8.31 艾春 添加退回类型
		
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT\n");
		sql.append("TV.VEHICLE_ID,\n");//--车辆ID（操作时候用）
		//sql.append("--列表信息字段\n");
		sql.append("VMG.SERIES_NAME,\n");// 车系
		sql.append("VMG.MODEL_NAME,\n");// --车型
		sql.append("VMG.PACKAGE_NAME,\n");// --配置
		sql.append("TVM.MATERIAL_CODE,\n");// --物料CODE
		sql.append("TVM.MATERIAL_NAME,\n");// --物料名称
		sql.append("TVM.COLOR_NAME,\n");// --颜色
		sql.append("TV.VIN,\n");	//	--底盘号（VIN）
		sql.append("TV.ENGINE_NO,\n");//--发动机号
		sql.append("TO_CHAR(TV.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\n");// --生产日期
		sql.append("TO_CHAR(TV.OFFLINE_DATE, 'YYYY-MM-DD') OFFLINE_DATE,\n");// --下线日期
		sql.append("TSA.AREA_NAME, \n");//--库区名称
		sql.append("TSR.ROAD_NAME,\n");// --库道名称
		sql.append("TSS.SIT_NAME,\n");// --库位名称
		sql.append("TV.LIFE_CYCLE,\n");//--库存状态
		sql.append("TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD') ORG_STORAGE_DATE\n");// --入库日期
		sql.append("FROM TM_VEHICLE               TV,\n");// --车辆信息表
		sql.append("TT_SALES_SIT             TSS,\n");// --库位表
		sql.append("VW_MATERIAL_GROUP        VMG, \n");//--物料试图
		sql.append("TT_SALES_ROAD            TSR,\n");// --库道表
		sql.append("TT_SALES_AREA            TSA, \n");//--库区表
		sql.append("TM_VHCL_MATERIAL         TVM,\n");// --物料表
		sql.append("TM_VHCL_MATERIAL_GROUP_R TVMGR, \n");//--物料物料组关联表
		sql.append("TM_BUSINESS_AREA TBA,TM_POSE_BUSINESS_AREA TPBA ");//--车厂职位业务表
		sql.append("WHERE TV.SIT_ID = TSS.SIT_ID\n");
		sql.append("AND TSS.ROAD_ID = TSR.ROAD_ID\n");
		sql.append("AND TSR.AREA_ID = TSA.AREA_ID\n");
		sql.append("AND TV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\n");
		sql.append("AND TV.YIELDLY=TBA.AREA_ID AND TBA.AREA_ID=TPBA.AREA_ID AND TPBA.POSE_ID=?\n");//权限控制
		//sql.append("AND TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD')=").append("TO_CHAR(SYSDATE,'YYYY-MM-DD')");
		//当天的才能退回
		//sql.append("AND TV.ORG_STORAGE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		//sql.append("AND TV.ORG_STORAGE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		
		/*2015.8.31 艾春 添加退回类型控制 开始*/
		if(CommonUtils.isEmpty(retreatType) || Constant.RETREAT_TYPE_REAL.toString().equals(retreatType)) {
			sql.append("AND TV.LIFE_CYCLE IN (").append(Constant.VEHICLE_LIFE_02).append(","+Constant.VEHICLE_LIFE_06+")\n");//查询在车厂库存状态和无效状态（试制试验区的车）
		}else {
			sql.append("AND TV.LIFE_CYCLE = ").append(Constant.VEHICLE_LIFE_02).append("\n");//查询在车厂库存状态
		}
		/*2015.8.31 艾春 添加退回类型控制 结束*/
		
		params.add((Long)map.get("poseId"));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String currDate=sdf.format(new java.util.Date());
//		params.add(currDate+" 00:00:00");
//		params.add(currDate+" 23:59:59");
		if(groupCode!=null&& !"".equals(groupCode)){
			sql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar("VMG.PACKAGE_ID", groupCode, params));
		}
		if(materialCode!=null && !"".equals(materialCode)){
			sql.append(Utility.getConSqlByParamForEqual(materialCode, params,"TVM", "MATERIAL_CODE"));
		}
		if(!"".equals(YIELDLY)){
			sql.append("AND TPBA.AREA_ID=?\n");
			params.add(Long.valueOf(YIELDLY));
		}
		if(areaName!=null&&!"".equals(areaName)){//库区过滤
			sql.append("   AND TSA.AREA_ID = ?\n" );
			params.add(areaName);
		}
		if(roadName!=null&&!"".equals(roadName)){//库道过滤
			sql.append("   AND TSR.ROAD_ID = ?\n" );
			params.add(roadName);
		}
		if(sitName!=null&&!"".equals(sitName)){//库位过滤
			sql.append("   AND TSS.SIT_ID = ?\n" );
			params.add(sitName);
		}
		if(storageDays!=null&&!"".equals(storageDays)){//超过天数过滤
			sql.append("   AND CEIL(SYSDATE - TV.ORG_STORAGE_DATE) > ?\n" );
			params.add(storageDays);
		}
		if(offlineStartDate!=null&&!"".equals(offlineStartDate)){//下线日期开始过滤
			sql.append("   AND TV.OFFLINE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(offlineStartDate+" 00:00:00");
			
		}
		if(offlineEndDate!=null&&!"".equals(offlineEndDate)){//下线日期结束过滤
			sql.append("   AND TV.OFFLINE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(offlineEndDate+" 23:59:59");
		}
		if(orgStartDate!=null&&!"".equals(orgStartDate)){// 入库日期开始过滤
			sql.append("   AND TV.ORG_STORAGE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orgStartDate+" 00:00:00");
		}
		if(orgEndDate!=null&&!"".equals(orgEndDate)){// 入库日期结束过滤
			sql.append("   AND TV.ORG_STORAGE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orgEndDate+" 23:59:59");
		}
		if(vin!=null&&!"".equals(vin)){//vin过滤
			sql.append("   AND TV.VIN LIKE ?\n" );
			params.add("%"+vin+"%");
		}
		//sql.append("AND TV.YIELDLY in(").append(areaIds==""?Constant.DEFAULT_VALUE:areaIds).append(")\n");//权限控制
		sql.append("ORDER BY TSA.AREA_ID,TO_NUMBER(TSR.ROAD_NAME),TO_NUMBER(TSS.SIT_NAME)");//排序
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 车辆退回生产线历史表修改
	 * @param tpo 车辆退回生产线历史表实体
	 * @return
	 * @throws Exception
	 */
	public void updateVehicleRetreatHistory(TmVehicleRetreatHistoryPO seach,TmVehicleRetreatHistoryPO tpo){
		dao.update(seach, tpo);
	}
	/**
	 * 车辆信息查询
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getVehicleQuery(List<Object> params)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TV.VEHICLE_ID,TV.VIN,TV.PLAN_DETAIL_ID,to_char(TV.ORG_STORAGE_DATE,'yyyy-mm-dd hh24:mi:ss') ORG_STORAGE_DATE FROM TM_VEHICLE TV WHERE TV.VIN =?");
		Map<String, Object> map= dao.pageQueryMap(sql.toString(), params, getFunName());
		return map;
	}
	/**
	 * 车辆信息删除
	 * @param params 参数List列表
	 * @throws Exception
	 */
	public void delVehicle(List<Object> params){
		StringBuffer sql= new StringBuffer();
		sql.append("DELETE FROM TM_VEHICLE TV WHERE TV.VEHICLE_ID =? ");
		dao.delete(sql.toString(), params);
	}
	/**
	 * 根据汇总订单号查询车辆表入库数量
	 * @param params 汇总订单号
	 * @return 已入库车辆数
	 * @throws Exception
	 */
	public Map<String, Object> getCountByTotalOrderId(List<Object> params)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT COUNT(1) INCOUNT FROM TM_VEHICLE TV WHERE TV.PLAN_DETAIL_ID=?");
		Map<String, Object> list= dao.pageQueryMap(sql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 修改详细计划表已入库数量
	 * @param      : @param list 参数列表      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-27
	 */
	public void inNumUpdate(List<Object> params) {
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE TM_PLAN_DETAIL  TPD SET TPD.IN_NUM=? WHERE TPD.PLAN_DETAIL_ID=?\n");
		dao.update(sql.toString(), params);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 添加车辆历史记录
	 * @param      : @param list 参数列表      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-27
	 */
	public String insertVehicleHistory(String vehicleId, String retreatType){
		String newVehicleId = "";
		/*2015.8.31 艾春 添加判断如果是试制移库 开始*/
		if(Constant.RETREAT_TYPE_TRIAL.toString().equals(retreatType)) 
			newVehicleId = SequenceManager.getSequence(""); //得到新的车辆ID
		else
			newVehicleId = vehicleId;
		/*2015.8.31 艾春 添加判断如果是试制移库 结束*/
		
		StringBuffer sql=new StringBuffer();
		sql.append("INSERT INTO TM_VEHICLE_RETREAT_HISTORY\r\n");
		sql.append("  (VEHICLE_ID,\r\n");
		sql.append("   ORG_TYPE,\r\n");
		sql.append("   ORG_ID,\r\n");
		sql.append("   DEALER_ID,\r\n");
		sql.append("   MATERIAL_ID,\r\n");
		sql.append("   VN,\r\n");
		sql.append("   VIN,\r\n");
		sql.append("   NODE_CODE,\r\n");
		sql.append("   NODE_DATE,\r\n");
		sql.append("   LIFE_CYCLE,\r\n");
		sql.append("   LOCATION,\r\n");
		sql.append("   VEHICLE_TYPE,\r\n");
		sql.append("   LOCK_STATUS,\r\n");
		sql.append("   REMARK,\r\n");
		sql.append("   LICENSE_NO,\r\n");
		sql.append("   ENGINE_NO,\r\n");
		sql.append("   GEARBOX_NO,\r\n");
		sql.append("   REARAXLE_NO,\r\n");
		sql.append("   SERIES_ID,\r\n");
		sql.append("   MODEL_ID,\r\n");
		sql.append("   PACKAGE_ID,\r\n");
		sql.append("   MODEL_YEAR,\r\n");
		sql.append("   COLOR,\r\n");
		sql.append("   PURCHASED_DATE,\r\n");
		sql.append("   PRODUCT_DATE,\r\n");
		sql.append("   FACTORY_DATE,\r\n");
		sql.append("   LICENSE_DATE,\r\n");
		sql.append("   START_MILEAGE,\r\n");
		sql.append("   MILEAGE,\r\n");
		sql.append("   METER_MILE,\r\n");
		sql.append("   HISTORY_MILE,\r\n");
		sql.append("   VER,\r\n");
		sql.append("   CREATE_BY,\r\n");
		sql.append("   CREATE_DATE,\r\n");
		sql.append("   UPDATE_BY,\r\n");
		sql.append("   UPDATE_DATE,\r\n");
		sql.append("   STORAGE_DATE,\r\n");
		sql.append("   WAREHOUSE_ID,\r\n");
		sql.append("   TRANSFER_NO,\r\n");
		sql.append("   YIELDLY,\r\n");
		sql.append("   ORG_STORAGE_DATE,\r\n");
		sql.append("   SPECIAL_BATCH_NO,\r\n");
		sql.append("   VEHICLE_AREA,\r\n");
		sql.append("   OEM_COMPANY_ID,\r\n");
		sql.append("   BATCH_NO,\r\n");
		sql.append("   CLAIM_TACTICS_ID,\r\n");
		sql.append("   FREE_TIMES,\r\n");
		sql.append("   AREA_ID,\r\n");
		sql.append("   HEGEZHENG_CODE,\r\n");
		sql.append("   WR_END_DATE,\r\n");
		sql.append("   SIT_ID,\r\n");
		sql.append("   PER_ID,\r\n");
		sql.append("   SIT_CODE,\r\n");
		sql.append("   OFFLINE_DATE,\r\n");
		sql.append("   PLAN_DETAIL_ID,\r\n");
		sql.append("   ARRIV_DATE,\r\n");
		sql.append("   OUT_DETAIL_ID,\r\n");
		sql.append("   OUT_STATUS,\r\n");
		sql.append("   ISRETREAT)\r\n");
		sql.append("  SELECT "+newVehicleId+",\r\n");
		sql.append("         T.ORG_TYPE,\r\n");
		sql.append("         T.ORG_ID,\r\n");
		sql.append("         T.DEALER_ID,\r\n");
		sql.append("         T.MATERIAL_ID,\r\n");
		sql.append("         T.VN,\r\n");
		sql.append("         T.VIN,\r\n");
		sql.append("         T.NODE_CODE,\r\n");
		sql.append("         T.NODE_DATE,\r\n");
		sql.append("         T.LIFE_CYCLE,\r\n");
		sql.append("         T.LOCATION,\r\n");
		sql.append("         T.VEHICLE_TYPE,\r\n");
		sql.append("         T.LOCK_STATUS,\r\n");
		sql.append("         T.REMARK,\r\n");
		sql.append("         T.LICENSE_NO,\r\n");
		sql.append("         T.ENGINE_NO,\r\n");
		sql.append("         T.GEARBOX_NO,\r\n");
		sql.append("         T.REARAXLE_NO,\r\n");
		sql.append("         T.SERIES_ID,\r\n");
		sql.append("         T.MODEL_ID,\r\n");
		sql.append("         T.PACKAGE_ID,\r\n");
		sql.append("         T.MODEL_YEAR,\r\n");
		sql.append("         T.COLOR,\r\n");
		sql.append("         T.PURCHASED_DATE,\r\n");
		sql.append("         T.PRODUCT_DATE,\r\n");
		sql.append("         T.FACTORY_DATE,\r\n");
		sql.append("         T.LICENSE_DATE,\r\n");
		sql.append("         T.START_MILEAGE,\r\n");
		sql.append("         T.MILEAGE,\r\n");
		sql.append("         T.METER_MILE,\r\n");
		sql.append("         T.HISTORY_MILE,\r\n");
		sql.append("         T.VER,\r\n");
		sql.append("         T.CREATE_BY,\r\n");
		sql.append("         T.CREATE_DATE,\r\n");
		sql.append("         T.UPDATE_BY,\r\n");
		sql.append("         T.UPDATE_DATE,\r\n");
		sql.append("         T.STORAGE_DATE,\r\n");
		sql.append("         T.WAREHOUSE_ID,\r\n");
		sql.append("         T.TRANSFER_NO,\r\n");
		sql.append("         T.YIELDLY,\r\n");
		sql.append("         T.ORG_STORAGE_DATE,\r\n");
		sql.append("         T.SPECIAL_BATCH_NO,\r\n");
		sql.append("         T.VEHICLE_AREA,\r\n");
		sql.append("         T.OEM_COMPANY_ID,\r\n");
		sql.append("         T.BATCH_NO,\r\n");
		sql.append("         T.CLAIM_TACTICS_ID,\r\n");
		sql.append("         T.FREE_TIMES,\r\n");
		sql.append("         T.AREA_ID,\r\n");
		sql.append("         T.HEGEZHENG_CODE,\r\n");
		sql.append("         T.WR_END_DATE,\r\n");
		sql.append("         T.SIT_ID,\r\n");
		sql.append("         T.PER_ID,\r\n");
		sql.append("         T.SIT_CODE,\r\n");
		sql.append("         T.OFFLINE_DATE,\r\n");
		sql.append("         T.PLAN_DETAIL_ID,\r\n");
		sql.append("         T.ARRIV_DATE,\r\n");
		sql.append("         T.OUT_DETAIL_ID,\r\n");
		sql.append("         T.OUT_STATUS,\r\n");
		sql.append(Constant.RETREAT_STATUS01+"\r\n");
		sql.append("    FROM TM_VEHICLE T\r\n");
		sql.append("   WHERE T.VEHICLE_ID = "+Long.parseLong(vehicleId)); 
		dao.insert(sql.toString());
		return newVehicleId;
	}
	/**
	 * 根据库位ID和车辆ID获取车辆信息
	 * @param params查询参数（staus,sitId,vehicleId）
	 * @return 过滤的车辆信息
	 * @throws Exception
	 */
	public Map<String, Object> getVehicleMap(List<Object> params)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT TSS.VEHICLE_ID, TV.PLAN_DETAIL_ID\n");
		sbSql.append("  FROM TT_SALES_SIT TSS, TM_VEHICLE TV\n");
		sbSql.append(" WHERE TSS.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append("   AND TSS.SIT_ID = ?\n");
		sbSql.append("   AND TSS.VEHICLE_ID = ?\n");
		sbSql.append("   AND TSS.STATUS = ?"); 
		Map<String, Object> map= dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return map;
	}
	/**
	 * 车辆退回生产线历史信息查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getReceivingStorageHisQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		
		String groupCode = (String)map.get("groupCode"); // 物料组
		String materialCode = (String)map.get("materialCode"); // 物料
		String areaName = (String)map.get("areaName"); // 库区
		String roadName = (String)map.get("roadName"); // 库道
		String sitName = (String)map.get("sitName"); // 库位
		//String storageDays = (String)map.get("storageDays"); // 超过天数
		String offlineStartDate = (String)map.get("offlineStartDate"); // 下线日期开始
		String offlineEndDate = (String)map.get("offlineEndDate"); // 下线日期结束
		String orgStartDate = (String)map.get("orgStartDate"); // 入库日期开始
		String orgEndDate = (String)map.get("orgEndDate"); // 入库日期结束
		String retreatStartdate = (String)map.get("retreatStartdate"); // 退库日期开始
		String retreatEnddate = (String)map.get("retreatEnddate"); // 退库日期结束
		String vin = (String)map.get("vin"); // vin
		String areaIds = (String)map.get("areaIds"); // vin
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT\n");
		sql.append("TV.VEHICLE_ID,\n");//--车辆ID（操作时候用）
		sql.append("VMG.MODEL_NAME,\n");// --车型
		sql.append(" VMG.PACKAGE_NAME,\n");// --配置
		sql.append("TVM.MATERIAL_CODE,\n");// --物料CODE
		sql.append("TVM.MATERIAL_NAME,\n");// --物料名称
		sql.append("TV.VIN,\n");	//	--底盘号（VIN）
		sql.append("TV.ENGINE_NO,\n");//--发动机号
		sql.append("TO_CHAR(TV.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\n");// --生产日期
		sql.append("TO_CHAR(TV.OFFLINE_DATE, 'YYYY-MM-DD') OFFLINE_DATE,\n");// --下线日期
		sql.append("TSA.AREA_NAME, \n");//--库区名称
		sql.append("TSR.ROAD_NAME,\n");// --库道名称
		sql.append("TSS.SIT_NAME,\n");// --库位名称
		sql.append("TV.LIFE_CYCLE,\n");//--库存状态
		sql.append("TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD') ORG_STORAGE_DATE,\n");// --入库日期
		sql.append("TO_CHAR(TV.RETREAT_DATE, 'YYYY-MM-DD HH24:MI:SS') RETREAT_DATE,\n");// --退回时间
		sql.append("(SELECT X.NAME FROM TC_USER X WHERE X.USER_ID=TV.RETREAT_BY) RETREAT_BY,\n");// --退回人
		sql.append("TV.RETREATDES\n");//--退回原因
		sql.append("FROM TM_VEHICLE_RETREAT_HISTORY TV,\n");// --车辆信息表
		sql.append("VW_MATERIAL_GROUP        VMG, \n");//--物料试图
		sql.append("TT_SALES_SIT             TSS,\n"); 
		sql.append("TT_SALES_ROAD            TSR,\n");// --库道表
		sql.append("TT_SALES_AREA            TSA, \n");//--库区表
		sql.append("TM_VHCL_MATERIAL         TVM,\n");// --物料表
		sql.append("TM_VHCL_MATERIAL_GROUP_R TVMGR, \n");//--物料物料组关联表
		sql.append("TM_BUSINESS_AREA TBA ");//--车厂职位业务表
		sql.append("WHERE TV.SIT_ID = TSS.SIT_ID\n");
		sql.append("AND TSS.ROAD_ID = TSR.ROAD_ID\n");
		sql.append("AND TSR.AREA_ID = TSA.AREA_ID\n");
		sql.append("AND TV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\n");
		sql.append("AND TV.YIELDLY=TBA.AREA_ID\n");//权限控制
		sql.append("AND TV.ISRETREAT=").append(Constant.RETREAT_STATUS02);//查询在退回的信息
		if(groupCode!=null&& !"".equals(groupCode)){
			sql.append(MaterialGroupManagerDao.getMaterialGroupQuerySql("VMG.PACKAGE_ID", groupCode));
		}
		if(materialCode!=null && !"".equals(materialCode)){
			sql.append(MaterialGroupManagerDao.getMaterialQuerySql("TVM.MATERIAL_ID", materialCode));
		}
		if(areaName!=null&&!"".equals(areaName)){//库区过滤
			sql.append("   AND TSA.AREA_ID = "+areaName+"\n" );
		}
		if(roadName!=null&&!"".equals(roadName)){//库道过滤
			sql.append("   AND TSR.ROAD_ID = "+roadName+"\n" );
		}
		if(sitName!=null&&!"".equals(sitName)){//库位过滤
			sql.append("   AND TSS.SIT_ID ="+sitName+"\n" );
		}
		if(offlineStartDate!=null&&!"".equals(offlineStartDate)){//下线日期开始过滤
			sql.append("   AND TO_CHAR(TV.OFFLINE_DATE,'YYYY-MM-DD')>='"+offlineStartDate+"'\n" );
		}
		if(offlineEndDate!=null&&!"".equals(offlineEndDate)){//下线日期结束过滤
			sql.append("   AND TO_CHAR(TV.OFFLINE_DATE,'YYYY-MM-DD')<='"+offlineEndDate+"'\n" );
		}
		if(orgStartDate!=null&&!"".equals(orgStartDate)){// 入库日期开始过滤
			sql.append("   AND TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD')>='"+orgStartDate+"'\n" );
		}
		if(orgEndDate!=null&&!"".equals(orgEndDate)){// 入库日期结束过滤
			sql.append("   AND TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD')<='"+orgEndDate+"'\n" );
		}
		if(retreatStartdate!=null&&!"".equals(retreatStartdate)){//退回日期开始过滤
			sql.append("   AND TO_CHAR(TV.RETREAT_DATE,'YYYY-MM-DD')>='"+retreatStartdate+"'\n" );
		}
		if(retreatEnddate!=null&&!"".equals(retreatEnddate)){//退回日期结束过滤
			sql.append("   AND TO_CHAR(TV.RETREAT_DATE,'YYYY-MM-DD')<='"+retreatEnddate+"'\n" );
		}
		if(vin!=null&&!"".equals(vin)){//vin过滤
			sql.append(GetVinUtil.getVins(vin, "TV"));
		}
		sql.append("AND TV.YIELDLY in(").append(areaIds==""?Constant.DEFAULT_VALUE:areaIds).append(")\n");//权限控制
		sql.append("ORDER BY TSA.AREA_ID,TO_NUMBER(TSR.ROAD_NAME),TO_NUMBER(TSS.SIT_NAME)");//排序
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	
	public List<Map<String,Object>> getRetreatList(String[] vehIds){
		StringBuffer sql= new StringBuffer();
		String para = "";
		sql.append("SELECT MAT.MATERIAL_CODE,TM.ERP_ORDER_ID,COUNT(1) NUM\n" );
		sql.append("  FROM  TM_VEHICLE TM\n" );
		sql.append("  JOIN  VW_MATERIAL_GROUP_MAT MAT ON TM.MATERIAL_ID = MAT.MATERIAL_ID\n" );
		sql.append("  WHERE  1=1 \n" );
		sql.append("   AND TM.LIFE_CYCLE != "+Constant.VEHICLE_LIFE_06+"\n");
		if(vehIds.length > 0){
			
			for(String s : vehIds){
				para+= "'"+s+"',";
			}
			para = para.substring(0, para.length()-1);
			sql.append("AND TM.VIN IN ("+para+")");
		}
		sql.append("  GROUP BY MAT.MATERIAL_CODE,TM.ERP_ORDER_ID");
		return pageQuery(sql.toString(), null, getFunName());
	}
	
	public List<Map<String,Object>> getRetreatVehicleList(String[] vehIds){
		String para = "";
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT MAT.MATERIAL_CODE,TM.ERP_ORDER_ID,TM.VIN \n" );
		sql.append("  FROM  TM_VEHICLE TM\n" );
		sql.append("  JOIN  VW_MATERIAL_GROUP_MAT MAT ON TM.MATERIAL_ID = MAT.MATERIAL_ID\n" );
		sql.append("  WHERE 1= 1 \n" );
		sql.append("   AND TM.LIFE_CYCLE != "+Constant.VEHICLE_LIFE_06+"\n");
		if(vehIds.length > 0){
			for(String s : vehIds){
				para+= "'"+s+"',";
			}
			para = para.substring(0, para.length()-1);
			sql.append("AND TM.VIN IN ("+para+")");
		}
		return pageQuery(sql.toString(), null, getFunName());
	}
}
