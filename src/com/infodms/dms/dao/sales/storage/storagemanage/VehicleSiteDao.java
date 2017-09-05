package com.infodms.dms.dao.sales.storage.storagemanage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : VehicleSiteDao 
 * @Description   : 车辆查询DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-14
 */
public class VehicleSiteDao extends BaseDao<PO>{
	private static final VehicleSiteDao dao = new VehicleSiteDao ();
	public static final VehicleSiteDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 车辆查询查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getReceivingStorageQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		Object[] objArr=getSql(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(String.valueOf(objArr[0]), (List<Object>)objArr[1], getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 车辆库龄查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getStorageDaysQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		Object[] objArr=getSql2(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(String.valueOf(objArr[0]), (List<Object>)objArr[1], getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 车辆库龄查询导出
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getStorageDaysExport(Map<String, Object> map)throws Exception{
		Object[] objArr=getSql2(map);
		List<Map<String, Object>> list= dao.pageQuery(String.valueOf(objArr[0]),(List<Object>)objArr[1], getFunName());
		return list;
	}
	/**
	 * 车辆查询查询(导出)
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getReceivingStorageQueryExport(Map<String, Object> map)throws Exception{
		Object[] objArr=getSql(map);
		List<Map<String, Object>> list= dao.pageQuery(String.valueOf(objArr[0]),(List<Object>)objArr[1], getFunName());
		return list;
	}
	/**
	 * 车辆库龄查询sql
	 * @param map
	 * @return
	 */
	public Object[] getSql2(Map<String, Object> map){
		String groupCode = (String)map.get("groupCode"); //车系
		String materialCode = (String)map.get("materialCode"); // 物料
		String modelCode = (String)map.get("modelCode"); //车型
		String packageCode = (String)map.get("packageCode"); //配置
		String orgStartDate = (String)map.get("orgStartDate"); 
		String orgEndDate = (String)map.get("orgEndDate"); 
		String vin = (String)map.get("vin"); 
		//String poseId = (String) map.get("poseId"); 
		String YIELDLY = (String) map.get("YIELDLY"); 
		String storageDays = (String) map.get("storageDays"); 
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sbSql= new StringBuffer();

		sbSql.append("SELECT\n");
		sbSql.append(" TV.VEHICLE_ID,\n");
		sbSql.append(" VMGM.SERIES_NAME,\n");
		sbSql.append(" VMGM.MODEL_NAME,\n");
		sbSql.append(" VMGM.PACKAGE_NAME,\n");
		sbSql.append(" VMGM.MATERIAL_CODE ,\n");
		sbSql.append(" VMGM.MATERIAL_NAME,\n");
		sbSql.append(" VMGM.COLOR_NAME,\n");
		sbSql.append(" TV.VIN,\n");
		sbSql.append(" TV.LOCK_STATUS,\n");
		sbSql.append(" TV.ENGINE_NO,\n");
		sbSql.append(" TV.GEARBOX_NO,\n");
		sbSql.append(" TO_CHAR(TV.FACTORY_DATE, 'YYYY-MM-DD') FACTORY_DATE,\n");
		sbSql.append(" TO_CHAR(TV.PURCHASED_DATE, 'YYYY-MM-DD') PURCHASED_DATE,\n");
		sbSql.append(" TO_CHAR(TV.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\n");
		sbSql.append(" TO_CHAR(TV.OFFLINE_DATE, 'YYYY-MM-DD') OFFLINE_DATE,\n");
		sbSql.append(" TV.LIFE_CYCLE,\n");
		sbSql.append(" f.code_desc life_cycle_name,\n");
		sbSql.append(" l.code_desc lock_name,\n");
		sbSql.append(" TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD HH24:MI:SS') ORG_STORAGE_DATE,\n");
		sbSql.append(" ROUND(TO_NUMBER(sysdate - TV.ORG_STORAGE_DATE)) STORAGE_DAYS,\n");
		sbSql.append(" WH.WAREHOUSE_NAME AREA_NAME\n");
		sbSql.append("  FROM TM_VEHICLE TV,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMGM,\n");
		sbSql.append("       TM_BUSINESS_AREA TBA,\n");
		sbSql.append("       TM_WAREHOUSE WH,\n");
		sbSql.append("       TM_POSE_BUSINESS_AREA TPBA,\n");
		sbSql.append("       TM_AREA_GROUP AG,\n");
		sbSql.append("       tc_code               f,\n");
		sbSql.append("       tc_code               l\n");
		sbSql.append(" WHERE 1=1\n");		
		sbSql.append("   AND tv.life_cycle = f.code_id\n");
		sbSql.append("   AND tv.lock_status = l.code_id\n");
		sbSql.append("   AND TV.MATERIAL_ID = VMGM.MATERIAL_ID\n");
		sbSql.append("   AND TV.warehouse_id = wh.warehouse_id\n");
		sbSql.append("   AND TBA.AREA_ID = WH.AREA_ID\n");
		sbSql.append("   AND TBA.AREA_ID = AG.AREA_ID\n");	
		sbSql.append("   AND tv.series_id = AG.material_group_id\n");
		sbSql.append("   AND TBA.AREA_ID = TPBA.AREA_ID\n");
		sbSql.append("   AND TV.LIFE_CYCLE ="+Constant.VEHICLE_LIFE_02+"\n");//车厂库存
		sbSql.append("   AND TPBA.POSE_ID=?\n");//权限控制
		params.add((Long)map.get("poseId"));
		if(!"".equals(groupCode)){
			sbSql.append(Utility.getConSqlByParamForEqual(groupCode, params,"VMGM", "SERIES_CODE"));
		}
		if(!"".equals(modelCode)){
			sbSql.append(Utility.getConSqlByParamForEqual(modelCode, params,"VMGM", "MODEL_CODE"));
		}
		if(!"".equals(packageCode)){
			sbSql.append(Utility.getConSqlByParamForEqual(packageCode, params,"VMGM", "PACKAGE_CODE"));
		}
		if(!"".equals(materialCode)){
			sbSql.append(Utility.getConSqlByParamForEqual(materialCode, params,"VMGM", "MATERIAL_CODE"));
		}
		if(!"".equals(YIELDLY)){
			sbSql.append("AND TV.warehouse_id=?\n");
			params.add(Long.valueOf(YIELDLY));
		}
		
		if(orgStartDate!=null&&!"".equals(orgStartDate)){// 入库日期开始过滤
			sbSql.append("   AND TV.ORG_STORAGE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orgStartDate+" 00:00:00");
		}
		if(orgEndDate!=null&&!"".equals(orgEndDate)){// 入库日期结束过滤
			sbSql.append("   AND TV.ORG_STORAGE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orgEndDate+" 23:59:59");
		}
		
		
		if(!"".equals(vin)){//vin过滤
			sbSql.append("   AND TV.VIN LIKE ?\n" );
			params.add("%"+vin+"%");
		}
		if(!"".equals(storageDays)&&null!=storageDays){//库龄
			sbSql.append("  AND ROUND(TO_NUMBER(sysdate - TV.ORG_STORAGE_DATE))=?\n" );
			params.add(storageDays);
		}
		sbSql.append("ORDER BY TV.ORG_STORAGE_DATE DESC");//排序
		
		Object[] obj=new Object[2];
		obj[0]=sbSql;
		obj[1]=params;
		return obj;
	}
	/**
	 * 车辆查询查询(SQL)
	 * @param map
	 */
	public Object[] getSql(Map<String, Object> map){
		String groupCode = (String)map.get("groupCode"); //车系
		String materialCode = (String)map.get("materialCode"); // 物料
		String modelCode = (String)map.get("modelCode"); //车型
		String packageCode = (String)map.get("packageCode"); //配置
		String offlineStartDate = (String)map.get("offlineStartDate");
		String offlineEndDate = (String)map.get("offlineEndDate"); 
		String orgStartDate = (String)map.get("orgStartDate"); 
		String orgEndDate = (String)map.get("orgEndDate"); 
		String vin = (String)map.get("vin"); 
		String YIELDLY = (String) map.get("YIELDLY"); 
		String LIFE_CYCLE = (String) map.get("LIFE_CYCLE"); 
		String productDateStart = (String) map.get("productDateStart");
		String productDateEnd = (String) map.get("productDateEnd"); 
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sbSql= new StringBuffer();

		sbSql.append("SELECT\n");
		sbSql.append("/*+USE_HASH(TV,TSS,VMGM,TSR,TSA,C,TBA,TPBA)*/\n");
		sbSql.append(" TV.VEHICLE_ID,\n");
		sbSql.append(" VMGM.SERIES_NAME,\n");
		sbSql.append(" VMGM.MODEL_NAME,\n");
		sbSql.append(" VMGM.PACKAGE_NAME,\n");
		sbSql.append(" VMGM.MATERIAL_CODE ,\n");
		sbSql.append(" VMGM.MATERIAL_NAME,\n");
		sbSql.append(" VMGM.COLOR_NAME,\n");
		sbSql.append(" TV.VIN,\n");
		sbSql.append(" TV.LOCK_STATUS,\n");
		sbSql.append(" TV.ENGINE_NO,\n");
		sbSql.append(" TV.GEARBOX_NO,\n");
		sbSql.append(" TO_CHAR(TV.FACTORY_DATE, 'YYYY-MM-DD') FACTORY_DATE,\n");
		sbSql.append(" TO_CHAR(TV.PURCHASED_DATE, 'YYYY-MM-DD') PURCHASED_DATE,\n");
		sbSql.append(" TO_CHAR(TV.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\n");
		sbSql.append(" TO_CHAR(TV.OFFLINE_DATE, 'YYYY-MM-DD') OFFLINE_DATE,\n");
		sbSql.append(" TV.LIFE_CYCLE,\n");
		sbSql.append(" f.code_desc life_cycle_name,\n");
		sbSql.append(" l.code_desc lock_name,\n");
		sbSql.append(" TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD HH24:MI:SS') ORG_STORAGE_DATE,\n");
		sbSql.append(" WH.WAREHOUSE_NAME AREA_NAME\n");
		sbSql.append("  FROM TM_VEHICLE TV,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMGM,\n");
		sbSql.append("       TM_BUSINESS_AREA TBA,\n");
		sbSql.append("       TM_WAREHOUSE WH,\n");
		sbSql.append("       TM_AREA_GROUP AG,\n");
		sbSql.append("       TM_POSE_BUSINESS_AREA TPBA,\n");
		sbSql.append("       tc_code               f,\n");
		sbSql.append("       tc_code               l\n");
		sbSql.append(" WHERE 1=1\n");
		sbSql.append("   AND tv.life_cycle = f.code_id\n");
		sbSql.append("   AND tv.lock_status = l.code_id\n");
		sbSql.append("   AND TV.MATERIAL_ID = VMGM.MATERIAL_ID\n");
		sbSql.append("   AND TV.warehouse_id = wh.warehouse_id\n");
		sbSql.append("   AND TBA.AREA_ID = AG.AREA_ID\n");	
		sbSql.append("   AND tv.series_id = AG.material_group_id\n");
		sbSql.append("   AND TBA.AREA_ID = TPBA.AREA_ID\n");
		sbSql.append("   AND TBA.AREA_ID = WH.AREA_ID\n");		
		sbSql.append("   AND TPBA.POSE_ID=?\n");//权限控制
		params.add((Long)map.get("poseId"));
		if(!"".equals(groupCode)){
			sbSql.append(Utility.getConSqlByParamForEqual(groupCode, params,"VMGM", "SERIES_CODE"));
		}
		if(!"".equals(modelCode)){
			sbSql.append(Utility.getConSqlByParamForEqual(modelCode, params,"VMGM", "MODEL_CODE"));
		}
		if(!"".equals(packageCode)){
			sbSql.append(Utility.getConSqlByParamForEqual(packageCode, params,"VMGM", "PACKAGE_CODE"));
		}
		if(!"".equals(materialCode)){
			sbSql.append(Utility.getConSqlByParamForEqual(materialCode, params,"VMGM", "MATERIAL_CODE"));
		}
		if(!"".equals(YIELDLY)){
			sbSql.append("AND WH.WAREHOUSE_ID=?\n");
			params.add(Long.valueOf(YIELDLY));
		}
		if(offlineStartDate!=null&&!"".equals(offlineStartDate)){//下线日期开始过滤
			sbSql.append("   AND TV.OFFLINE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(offlineStartDate+" 00:00:00");
		}
		if(offlineEndDate!=null&&!"".equals(offlineEndDate)){//下线日期结束过滤
			sbSql.append("   TV.OFFLINE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(offlineEndDate +" 23:59:59");
		}
		if(orgStartDate!=null&&!"".equals(orgStartDate)){// 入库日期开始过滤
			sbSql.append("   AND TV.ORG_STORAGE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orgStartDate+" 00:00:00");
		}
		if(orgEndDate!=null&&!"".equals(orgEndDate)){// 入库日期结束过滤
			sbSql.append("   AND TV.ORG_STORAGE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orgEndDate+" 23:59:59");
		}
		
		if(productDateStart!=null&&!"".equals(productDateStart)){// 生产日期开始过滤
			sbSql.append("   AND TV.PRODUCT_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(productDateStart+" 00:00:00");
		}
		if(productDateEnd!=null&&!"".equals(productDateEnd)){// 生产日期结束过滤
			sbSql.append("   AND TV.PRODUCT_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(productDateEnd+" 23:59:59");
		}
		if(!"".equals(vin)){//vin过滤
			sbSql.append("   AND TV.VIN LIKE ?\n" );
			params.add("%"+vin+"%");
		}
		if(!"".equals(LIFE_CYCLE)){//生命周期
			sbSql.append("   AND TV.LIFE_CYCLE = ?\n" );
			params.add(LIFE_CYCLE);
		}
		
		sbSql.append("ORDER BY TV.ORG_STORAGE_DATE DESC,TV.OFFLINE_DATE DESC");//排序
		
		
		Object[] obj=new Object[2];
		obj[0]=sbSql;
		obj[1]=params;
		return obj;
	}
}
