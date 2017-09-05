package com.infodms.dms.dao.sales.storage.storagemanage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @ClassName : VehicleSiteAdjustDao
 * @Description : 车辆位置调整DAO
 * @author : ranjian
 *         CreateDate : 2013-4-14
 */
public class VehicleSiteAdjustDao extends BaseDao<PO> {
	private static final VehicleSiteAdjustDao dao = new VehicleSiteAdjustDao();
	
	public static final VehicleSiteAdjustDao getInstance()
	{
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		// TODO Auto-generated method stub
		return null;
	}
	

	/**
	 * @date 2015.8.28
	 * @author 艾春
	 * @remark 车辆调整历史信息查询
	 * @param 车辆ID
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAdjustHis(String vehicleId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT U.NAME,\n" );
		sql.append("       TO_CHAR(T.CH_DATE, 'yyyy-mm-dd hh24:mi') ADJUST_DATE,\n" );
		sql.append("       T.CH_REMARK ADJUST_REMARK\n" );
		sql.append("  FROM TT_SALES_POS_HIS T, TC_USER U\n" );
		sql.append(" WHERE T.CH_PER = U.USER_ID");
		sql.append("  AND T.VEHICLE_ID = "+vehicleId+"\n" );
		sql.append(" ORDER BY ADJUST_DATE");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 车辆位置调整查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getReceivingStorageQuery(Map<String, Object> map, int curPage, int pageSize)
					throws Exception
	{
		
		String groupCode = (String) map.get("groupCode"); // 物料组
		String materialCode = (String) map.get("materialCode"); // 物料
		String areaName = (String) map.get("areaName"); // 库区
		String roadName = (String) map.get("roadName"); // 库道
		String sitName = (String) map.get("sitName"); // 库位
		String storageDays = (String) map.get("storageDays"); // 超过天数
		String offlineStartDate = (String) map.get("offlineStartDate"); // 下线日期开始
		String offlineEndDate = (String) map.get("offlineEndDate"); // 下线日期结束
		String orgStartDate = (String) map.get("orgStartDate"); // 入库日期开始
		String orgEndDate = (String) map.get("orgEndDate"); // 入库日期结束
		String vin = (String) map.get("vin"); // vin
		String areaIds = (String) map.get("areaIds"); // vin
		String YIELDLY = (String) map.get("YIELDLY"); // vin
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT\n");
		sql.append("TV.VEHICLE_ID,\n");//--车辆ID（操作时候用）
		//sql.append("--列表信息字段\n");
		sql.append("VMG.MODEL_NAME,\n");// --车型
		sql.append(" VMG.PACKAGE_NAME,\n");// --配置
		sql.append("TVM.MATERIAL_CODE,\n");// --物料CODE
		sql.append("TVM.MATERIAL_NAME,\n");// --物料名称
		sql.append("TV.VIN,TV.LOCK_STATUS,\n");	//	--底盘号（VIN）
		sql.append("TV.ENGINE_NO,\n");//--发动机号
		sql.append("C.PER_CODE,\n");//--接车员CODE
		sql.append("TO_CHAR(TV.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\n");// --生产日期
		sql.append("TO_CHAR(TV.OFFLINE_DATE, 'YYYY-MM-DD') OFFLINE_DATE,\n");// --下线日期
		sql.append("TSA.AREA_NAME, \n");//--库区名称
		sql.append("TSR.ROAD_NAME,\n");// --库道名称
		sql.append("TSS.SIT_NAME,\n");// --库位名称
		sql.append("TV.LIFE_CYCLE,\n");//--库存状态
		sql.append("TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD') ORG_STORAGE_DATE,\n");// --入库日期
		sql.append("TV.LAST_ADJUST_REMARK,\n");// --调整最新备注
		sql.append("CEIL(SYSDATE - TV.ORG_STORAGE_DATE) STORAGE_DAYS\n");// --库存天数
		sql.append("FROM TM_VEHICLE               TV,\n");// --车辆信息表
		sql.append("TT_SALES_SIT             TSS,\n");// --库位表
		sql.append("VW_MATERIAL_GROUP        VMG, \n");//--物料试图
		sql.append("TT_SALES_ROAD            TSR,\n");// --库道表
		sql.append("TT_SALES_AREA            TSA, \n");//--库区表
		sql.append("TM_VHCL_MATERIAL         TVM,\n");// --物料表
		sql.append("TM_VHCL_MATERIAL_GROUP_R TVMGR, \n");//--物料物料组关联表
		sql.append("tt_sales_accar_per C,\n");//--接车员
		sql.append("TM_BUSINESS_AREA TBA,TM_POSE_BUSINESS_AREA TPBA ");//--车厂职位业务表
		sql.append("WHERE TV.SIT_ID = TSS.SIT_ID\n");
		sql.append("AND TSS.ROAD_ID = TSR.ROAD_ID\n");
		sql.append("AND TSR.AREA_ID = TSA.AREA_ID\n");
		sql.append("AND TV.PER_ID = C.PER_ID(+)\n");
		sql.append("AND TV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\n");
		sql.append("AND TV.YIELDLY=TBA.AREA_ID AND TBA.AREA_ID=TPBA.AREA_ID AND TPBA.POSE_ID=?\n");//权限控制
		sql.append("AND TV.LIFE_CYCLE=").append(Constant.VEHICLE_LIFE_02);//查询在车厂库存状态
		params.add((Long)map.get("poseId"));
		if (groupCode != null && !"".equals(groupCode))
		{
			sql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar("VMG.PACKAGE_ID", groupCode, params));
		}
		if(!"".equals(YIELDLY)){
			sql.append("AND TPBA.AREA_ID=?\n");
			params.add(Long.valueOf(YIELDLY));
		}
		if (materialCode != null && !"".equals(materialCode))
		{
			sql.append(Utility.getConSqlByParamForEqual(materialCode, params,"TVM", "MATERIAL_CODE"));
		}
		if (areaName != null && !"".equals(areaName))
		{//库区过滤
			sql.append("   AND TSA.AREA_ID = ?\n");
			params.add(areaName);
		}
		if (roadName != null && !"".equals(roadName))
		{//库道过滤
			sql.append("   AND TSR.ROAD_ID = ?\n");
			params.add(roadName);
		}
		if (sitName != null && !"".equals(sitName))
		{//库位过滤
			sql.append("   AND TSS.SIT_ID =?\n");
			params.add(sitName);
		}
		if (storageDays != null && !"".equals(storageDays))
		{//超过天数过滤
			sql.append("   AND CEIL(SYSDATE - TV.ORG_STORAGE_DATE) >?\n");
			params.add(storageDays);
		}
		if (offlineStartDate != null && !"".equals(offlineStartDate))
		{//下线日期开始过滤
			//sql.append("   AND TO_CHAR(TV.OFFLINE_DATE,'YYYY-MM-DD')>=?\n");
			sql.append("   AND TV.OFFLINE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(offlineStartDate+" 00:00:00");
		}
		if (offlineEndDate != null && !"".equals(offlineEndDate))
		{//下线日期结束过滤
//			sql.append("   AND TO_CHAR(TV.OFFLINE_DATE,'YYYY-MM-DD')<=?\n");
//			params.add(offlineEndDate);
			sql.append("   AND TV.OFFLINE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(offlineEndDate+" 23:59:59");
		}
		if (orgStartDate != null && !"".equals(orgStartDate))
		{// 入库日期开始过滤
//			sql.append("   AND TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD')>=?\n");
//			params.add(orgStartDate);
			sql.append("   AND TV.ORG_STORAGE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(orgStartDate+" 00:00:00");
			
		}
		if (orgEndDate != null && !"".equals(orgEndDate))
		{// 入库日期结束过滤
//			sql.append("   AND TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD')<=?\n");
//			params.add(orgEndDate);
			sql.append("   AND TV.ORG_STORAGE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(orgEndDate+" 23:59:59");
		}
		if (vin != null && !"".equals(vin))
		{//vin过滤
			//sql.append(GetVinUtil.getVins(vin, "TV"));
			sql.append("and TV.VIN like ?\n");
			params.add("%"+vin+"%");
		}
		
		sql.append("ORDER BY TSA.AREA_ID,TO_NUMBER(TSR.ROAD_NAME),TO_NUMBER(TSS.SIT_NAME)");//排序
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * @Title :
	 * @Description: 根据物料CODE获取车系ID和对应的产地
	 * @param : @param params 查询参数map
	 * @param : @return 车系ID和产地列表
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public Map<String, Object> getYieldlyAndSeries(List<Object> params)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TAG.AREA_ID AS YIELDLY,\n");//--产地ID
		sql.append("TBA.AREA_NAME YIELDLY_NAME,\n");//--产地名称
		sql.append("VMGMG.SERIES_ID,\n");//--车系ID
		sql.append("VMGMG.SERIES_CODE,\n");//--车系CODE
		sql.append("VMGMG.SERIES_NAME,\n");//--车系名称
		sql.append("VMGMG.MODEL_ID,\n");//--车型ID
		sql.append("VMGMG.MODEL_CODE,\n");//--车型CODE
		sql.append("VMGMG.MODEL_NAME,\n");//--车型名称
		sql.append("VMGMG.PACKAGE_ID,\n");//--配置ID
		sql.append("VMGMG.PACKAGE_CODE,\n");//--配置CODE
		sql.append("VMGMG.PACKAGE_NAME,\n");//--配置名称
		sql.append("VMGMG.MATERIAL_ID,\n");//--物料ID
		sql.append("VMGMG.MATERIAL_CODE,\n");//--物料CODE
		sql.append("VMGMG.MATERIAL_NAME,\n");//--物料名称
		sql.append(" TVM.COLOR_NAME,\n");//--颜色
		sql.append(" TVM.MODEL_YEAR\n");// --年型
		sql.append(" FROM VW_MATERIAL_GROUP_MAT VMGMG, TM_AREA_GROUP TAG,TM_BUSINESS_AREA TBA,TM_VHCL_MATERIAL TVM\n");
		sql.append(" WHERE VMGMG.SERIES_ID = TAG.MATERIAL_GROUP_ID\n");
		sql.append("AND TAG.AREA_ID=TBA.AREA_ID\n");
		sql.append("AND VMGMG.MATERIAL_ID=TVM.MATERIAL_ID\n");
		sql.append("AND VMGMG.MATERIAL_CODE=?\n");
		Map<String, Object> map = pageQueryMap(sql.toString(), params, getFunName());
		return map;
	}
	
	/**
	 * @Title :
	 * @Description: 根据物料车系ID和产地获取库区
	 * @param : @param params 查询参数List
	 * @param : @return 满足条件的库区
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public List<Map<String, Object>> getRegionByYieldlyAndSeries(List<Object> params)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TSA.AREA_ID,TSA.AREA_NAME,TSAM.MAT_ID,TSA.YIELDLY YID,TBA.AREA_NAME YNAME\n");
		sql.append("FROM TT_SALES_AREA TSA,\n");//库区表
		sql.append("TT_SALES_AREA_MAT TSAM,\n");//库区与车系关系表
		sql.append("TM_BUSINESS_AREA TBA\n");//产地表
		sql.append(" WHERE TSA.AREA_ID = TSAM.AREA_ID\n");//获取下能入库的车系关系
		sql.append(" AND TSA.YIELDLY = TBA.AREA_ID\n");//与产地表关联
		sql.append(" AND TSA.IN_STATUS = ?\n");//可以自动入库
		sql.append("AND TSA.YIELDLY = ?\n");//产地第一步取得
		sql.append("AND TSA.STATUS=?\n");//有效的
		sql.append("AND TSAM.MAT_ID=?\n");//车系ID 第一步取得	
		sql.append("ORDER BY TSA.AREA_ID ASC");//根据ID排的序
		List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * @Title :
	 * @Description: 根据库区ID获取库道信息
	 * @param : @param params 查询参数List
	 * @param : @return 满足条件的库道信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public List<Map<String, Object>> getRoadByAreaId(List<Object> params)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TSR.ROAD_ID,TSR.ROAD_NAME FROM TT_SALES_ROAD TSR WHERE TSR.STATUS=? \n");//有效的状态
		sql.append("AND TSR.IN_STATUS=?\n");//--正常，未锁定
		sql.append("AND TSR.AREA_ID=?\n");
		sql.append(" ORDER BY TSR.ROAD_NAME ASC\n");//根据库位name排升序
		List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * @Title :
	 * @Description: 根据道ID获取库位信息
	 * @param : @param params 查询参数List
	 * @param : @return 满足条件的库位信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public List<Map<String, Object>> getSitByRoadId(List<Object> params)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TSS.SIT_ID,\n");
		sql.append("TSS.SIT_NAME,\n");
		sql.append("TSS.VEHICLE_ID,\n");
		sql.append("VMGM.MATERIAL_CODE\n");
		sql.append("FROM TT_SALES_SIT TSS, TM_VEHICLE TV, VW_MATERIAL_GROUP_MAT VMGM\n");
		sql.append(" WHERE  TSS.VEHICLE_ID = TV.VEHICLE_ID(+) \n");
		sql.append("AND TV.MATERIAL_ID = VMGM. MATERIAL_ID(+)\n");
		sql.append(" AND TSS.STATUS=? AND TSS.ROAD_ID=? \n");//有效的状态
		sql.append(" ORDER BY  TSS.SIT_NAME ASC\n");//根据库位name排升序
		List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * @Title :
	 * @Description: 根据道产地获取接车员信息
	 * @param : @param map 查询参数Map
	 * @param : @return 满足条件接车员信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public List<Map<String, Object>> getPerValue(Map<String, Object> map) throws Exception
	{
		Integer status = Integer.parseInt(map.get("status").toString()); // 产地
		String yieldly = (String) map.get("yieldly"); // 产地
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TSAP.PER_ID,TSAP.PER_NAME FROM TT_SALES_ACCAR_PER TSAP WHERE TSAP.STATUS=").append(status);
		sql.append("AND TSAP.YIELDLY in(-999,").append(yieldly).append(")");
		sql.append(" ORDER BY TSAP.PER_ID");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * @Title :
	 * @Description: 调整车辆信息
	 * @param : @param tmVehiclePO
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public void vehicleUpdate(TmVehiclePO seachParam, TmVehiclePO tmVehiclePO)
	{
		dao.update(seachParam, tmVehiclePO);
	}
	
	/**
	 * @Title :
	 * @Description: 根车辆ID获取车辆信息
	 * @param : @param params 查询参数MAP
	 * @param : @return 满足条件的车辆信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-15
	 */
	public Map<String, Object> getVechileByVId(List<Object> params)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TV.VEHICLE_ID,TBA.AREA_ID YIELDLY_ID,TBA.AREA_NAME YIELDLY,\n");//--产地
		sql.append("TV.PLAN_DETAIL_ID,\n");//--生产单号
		sql.append("VMGM.MODEL_CODE,\n");// --车型CODE
		sql.append("VMGM.MODEL_NAME,\n");// --车型名称
		sql.append("VMGM.PACKAGE_CODE,\n");// --配置CODE
		sql.append("VMGM.PACKAGE_NAME,\n");// --配置名称
		sql.append("VMGM.MATERIAL_CODE,\n");// --物料CODE
		sql.append("VMGM.MATERIAL_NAME,\n");// --物料名称
		sql.append("TV.VIN,\n");//--VIN
		sql.append("TV.ENGINE_NO,\n");//--发动机号
		sql.append("TO_CHAR(TV.PRODUCT_DATE,'YYYY-MM-DD') PRODUCT_DATE,\n");// --生产日期
		sql.append("TO_CHAR(TV.OFFLINE_DATE,'YYYY-MM-DD') OFFLINE_DATE,\n");// --下线日期
		sql.append("TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD') ORG_STORAGE_DATE,\n");// --入库日期
		sql.append("TV.PER_ID,\n");// --接车员
		sql.append("TSAP.PER_NAME,\n");// --接车员
		sql.append("TSAP.PER_CODE,\n");// --接车员
		sql.append("TSA.AREA_ID,\n");//--库区ID
		sql.append("TSA.AREA_NAME,\n");//--库区Name
		sql.append("TSR.ROAD_ID,\n");//--库道ID
		sql.append("TSR.ROAD_NAME,\n");//--库道Name
		sql.append("TV.SIT_ID,\n");//--库位ID
		sql.append("TSS.SIT_NAME,\n");//--库位Name
		sql.append("TV.SIT_CODE\n");// --库位码
		sql.append("FROM TM_VEHICLE TV,TM_BUSINESS_AREA TBA,VW_MATERIAL_GROUP_MAT VMGM,\n");//
		sql.append(" TT_SALES_ACCAR_PER TSAP,TT_SALES_SIT TSS,TT_SALES_ROAD TSR,TT_SALES_AREA TSA\n");//
		sql.append("WHERE TV.YIELDLY=TBA.AREA_ID\n");//
		sql.append("AND TV.MATERIAL_ID=VMGM.MATERIAL_ID\n");//
		sql.append("AND TV.PER_ID=TSAP.PER_ID(+)\n");//
		sql.append("AND TV.VEHICLE_ID=TSS.VEHICLE_ID\n");//
		sql.append("AND TSS.ROAD_ID=TSR.ROAD_ID\n");//
		sql.append("AND TSR.AREA_ID=TSA.AREA_ID\n");//
		sql.append("AND TSS.STATUS=?\n");//
		sql.append("AND ((TV.VEHICLE_ID=?) OR (TV.VIN = ?))\n");//
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), params, getFunName());
		return map;
	}
	
	/**
	 * @Title :
	 * @Description: 根库位ID获取库区，库位，库道信息
	 * @param : @param params 查询参数MAP
	 * @param : @return 满足条件的区，道，位信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-15
	 */
	public Map<String, Object> getStorageBySId(List<Object> params)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSS.SIT_NAME, --库位名称\n");
		sbSql.append("       TSS.ROAD_ID, --库道ID\n");
		sbSql.append("       TSS.VEHICLE_ID, --车辆ID\n");
		sbSql.append("       TSR.ROAD_NAME, --库道名称\n");
		sbSql.append("       TSR.IN_STATUS, --入库状态\n");
		sbSql.append("       TSR.OUT_STATUS, --出库状态\n");
		sbSql.append("       TSA.AREA_NAME AREA_NAME, --库区名称\n");
		sbSql.append("       TSA.TYPE AREA_TYPE, --库区类型\n");
		sbSql.append("       TSA.IN_STATUS, --入库状态\n");
		sbSql.append("       TSA.STATUS --库区状态\n");
		sbSql.append("  FROM TT_SALES_SIT TSS, TT_SALES_ROAD TSR, TT_SALES_AREA TSA\n");
		sbSql.append(" WHERE TSS.ROAD_ID = TSR.ROAD_ID\n");
		sbSql.append("   AND TSR.AREA_ID = TSA.AREA_ID\n");
		sbSql.append("   AND TSS.SIT_ID = ?");
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return map;
	}
	
	/**
	 * @Title :
	 * @Description: 根库位ID获取该库位所在的库道共多少库位
	 * @param : @param params 查询参数MAP
	 * @param : @return 获取该库位所在的库道共多少库位
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-15
	 */
	public Map<String, Object> getRoadCountBySId(List<Object> params)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) SITCOUNT FROM TT_SALES_SIT TSS,TT_SALES_SIT TSS1 WHERE \n");
		sql.append("TSS.ROAD_ID=TSS1.ROAD_ID AND TSS1.STATUS=? AND TSS1.SIT_ID = ? ");
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), params, getFunName());
		return map;
	}
	
	/**
	 * @Title :
	 * @Description: 根据道产地获取库道位
	 * @param : @param params 查询参数params
	 * @param : @return 满足条件库道位信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public List<Map<String, Object>> getLeftList(List<Object> params) throws Exception
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSA.AREA_ID,\n");
		sbSql.append("       TSA.AREA_CODE,\n");
		sbSql.append("       TSA.AREA_NAME,\n");
		sbSql.append("       NVL(R.ROADMUM, 0) ROADCOUNT,\n");
		sbSql.append("       NVL(R1.VECOUNT, 0) VECOUNT,\n");
		sbSql.append("       NVL(R2.SITCOUNT, 0) SITCOUNT\n");
		sbSql.append("  FROM TT_SALES_AREA TSA,\n");
		sbSql.append("       (SELECT TSR.AREA_ID, COUNT(1) ROADMUM --库道数\n");
		sbSql.append("          FROM TT_SALES_ROAD TSR\n");
		sbSql.append("         WHERE TSR.STATUS = ?\n");
		sbSql.append("         GROUP BY TSR.AREA_ID) R,\n");
		sbSql.append("       (SELECT TSA.AREA_ID, COUNT(1) VECOUNT --车辆数\n");
		sbSql.append("          FROM TT_SALES_AREA TSA, TT_SALES_ROAD TSR, TT_SALES_SIT TSS\n");
		sbSql.append("         WHERE TSA.AREA_ID = TSR.AREA_ID\n");
		sbSql.append("           AND TSR.ROAD_ID = TSS.ROAD_ID\n");
		sbSql.append("           AND TSS.VEHICLE_ID != -1\n");
		sbSql.append("           AND TSR.STATUS = ?\n");
		sbSql.append("           AND TSS.STATUS = ?\n");
		sbSql.append("         GROUP BY TSA.AREA_ID) R1,\n");
		sbSql.append("       (SELECT TSA.AREA_ID, COUNT(1) SITCOUNT --可用库位\n");
		sbSql.append("          FROM TT_SALES_AREA TSA, TT_SALES_ROAD TSR, TT_SALES_SIT TSS\n");
		sbSql.append("         WHERE TSA.AREA_ID = TSR.AREA_ID\n");
		sbSql.append("           AND TSR.ROAD_ID = TSS.ROAD_ID\n");
		sbSql.append("           AND TSS.VEHICLE_ID = -1\n");
		sbSql.append("           AND TSR.STATUS = ?\n");
		sbSql.append("           AND TSS.STATUS = ?\n");
		sbSql.append("         GROUP BY TSA.AREA_ID) R2\n");
		sbSql.append(" WHERE TSA.AREA_ID = R.AREA_ID(+)\n");
		sbSql.append("   AND TSA.AREA_ID = R1.AREA_ID(+)\n");
		sbSql.append("   AND TSA.AREA_ID = R2.AREA_ID(+)\n");
		sbSql.append("   AND TSA.STATUS = ?\n");
		sbSql.append("   AND TSA.YIELDLY = ?");
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * @Title :
	 * @Description: 根据道库区ID获取库道信息
	 * @param : @param params 查询参数params
	 * @param : @return 满足条件库道信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public List<Map<String, Object>> getAreaIdByRoad(List<Object> params) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TSR.ROAD_ID, TSR.ROAD_NAME,TSR.IN_STATUS,TSR.OUT_STATUS\r\n");
		sql.append("  FROM TT_SALES_ROAD TSR\r\n");
		sql.append(" WHERE TSR.STATUS = ?\r\n");
		sql.append("   AND TSR.AREA_ID = ? ORDER BY TO_NUMBER(TSR.ROAD_NAME)");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * @Title :
	 * @Description: 根据道库道ID获取库位信息
	 * @param : @param params 查询参数params
	 * @param : @return 满足条件库位信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public List<Map<String, Object>> getRoadIdBySit(List<Object> params) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TSS.SIT_ID,\r\n");
		sql.append("       TSS.SIT_NAME,\r\n");
		sql.append("       TSS.VEHICLE_ID,\r\n");
		sql.append("       TSR.IN_STATUS,\r\n");
		sql.append("       TSR.OUT_STATUS,\r\n");
		sql.append("       TSR.ROAD_ID,\r\n");
		sql.append("       TSR.ROAD_NAME,\r\n");
		sql.append("       TSA.AREA_ID,\r\n");
		sql.append("       TSA.AREA_NAME,\r\n");
		sql.append("       TSSI.IMG_URL,\r\n");
		sql.append("       TSSI.IMG_NAME,\r\n");
		sql.append("CASE WHEN TSS.VEHICLE_ID=-1 AND (TSSI.IMG_URL || TSSI.IMG_NAME) IS NULL THEN\n");
		sql.append("          'nullSit'--空库位\n");
		sql.append("     WHEN TSS.VEHICLE_ID!=-1 AND (TSSI.IMG_URL || TSSI.IMG_NAME) IS NULL THEN\n");
		sql.append("          'notPicture'--无图片\n");
		sql.append("     WHEN TSS.VEHICLE_ID=-1 AND (TSSI.IMG_URL || TSSI.IMG_NAME) IS NOT NULL THEN\n");
		sql.append("          'dataError'--数据错误\n");
		sql.append("     ELSE\n");
		sql.append("          TSSI.IMG_URL || TSSI.IMG_NAME--正常\n");
		sql.append("     END RE_URL,");
		sql.append("       DECODE(TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD'),TO_CHAR(SYSDATE,'YYYY-MM-DD'),1,0) TODAY,\r\n");//回退时候用该字段
		sql.append("       VMGM.SERIES_ID,\r\n");//车系ID(配车用)
		sql.append("       TV.MATERIAL_ID,\r\n");//物料ID(配车用)
		sql.append("       TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD') ORG_STORAGE_DATE,\r\n");//入库时间(配车用)
		sql.append("       TV.ENGINE_NO,\r\n");//发动机号(配车用)
		sql.append("       TV.VIN,\r\n");//发动机号(配车用)
		sql.append("       TV.LIFE_CYCLE\r\n,");//车辆生命周期	
		sql.append("       TV.LOCK_STATUS\r\n,");//车辆锁定状态	
		sql.append("       DECODE(TSAD.DETAIL_ID,'',0,1) CHECK_VEHICLE\r\n");//是否已配车
		sql.append("  FROM TT_SALES_SIT          TSS,\r\n");
		sql.append("       TT_SALES_ROAD         TSR,\r\n");
		sql.append("       TT_SALES_AREA         TSA,\r\n");
		sql.append("       TM_VEHICLE            TV,\r\n");
		sql.append("       TT_SALES_ALLOCA_DE    TSAD,\r\n");//已配车明细表
		sql.append("       VW_MATERIAL_GROUP_MAT VMGM,\r\n");
		sql.append("       TT_SALES_SERIES_IMG   TSSI\r\n");
		sql.append(" WHERE TSS.ROAD_ID = TSR.ROAD_ID\r\n");
		sql.append("   AND TSR.AREA_ID = TSA.AREA_ID\r\n");
		sql.append("   AND TV.VEHICLE_ID=TSAD.VEHICLE_ID(+)\r\n");
		sql.append("   AND TSS.VEHICLE_ID = TV.VEHICLE_ID(+)\r\n");
		sql.append("   AND TV.MATERIAL_ID = VMGM.MATERIAL_ID(+)\r\n");
		sql.append("   AND VMGM.SERIES_ID = TSSI.SERIES_ID(+)\r\n");
		sql.append("   AND TSS.STATUS = ?\r\n");
		sql.append("   AND TSS.ROAD_ID = ?\r\n");
		sql.append(" ORDER BY TO_NUMBER(TSS.SIT_NAME)");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
		return list;
	}
}
