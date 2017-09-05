/**********************************************************************
* <pre>
* FILE : OemStorageManageDao.java
* CLASS : OemStorageManageDao
* AUTHOR : 
* FUNCTION : 车厂库存管理
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-07|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.dao.sales.oemstorage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * @Title: 车厂库存管理DAO
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-7
 * @author 
 * @mail   
 * @version 1.0
 * @remark 
 */
public class OemStorageManageDao extends BaseDao{

	public static Logger logger = Logger.getLogger(OemStorageManageDao.class);
	private static final OemStorageManageDao dao = new OemStorageManageDao ();
	public static final OemStorageManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Function         : 仓库查询
	 * @return          : 仓库信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-07
	 */
	public List<Map<String, Object>> getWarehouse(Long companyId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMW.WAREHOUSE_ID, TMW.WAREHOUSE_NAME\n" );
		sql.append("  FROM TM_WAREHOUSE TMW\n" );
		sql.append(" WHERE TMW.STATUS = "+Constant.STATUS_ENABLE+"\n" );
		//sql.append("  AND TMW.WAREHOUSE_TYPE = "+Constant.WAREHOUSE_TYPE_04+"\n");
		sql.append("  AND TMW.COMPANY_ID = "+companyId+"\n");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getWarehouseEnable(Long companyId, String areaIds)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DISTINCT TMW.WAREHOUSE_ID, TMW.WAREHOUSE_NAME\n" );
		sql.append("  FROM TM_WAREHOUSE TMW, TM_BUSINESS_AREA TBA\n" );
		sql.append(" WHERE TMW.ENTITY_CODE = TBA.ERP_CODE\n" );
		sql.append("  AND TMW.STATUS = "+Constant.STATUS_ENABLE+"\n");
		sql.append("  AND TMW.WAREHOUSE_TYPE = "+Constant.WAREHOUSE_TYPE_01+"\n");
		sql.append("  AND TMW.COMPANY_ID = "+companyId+"\n");
		sql.append("  AND TBA.AREA_ID IN ("+areaIds+")\n");
//		sql.append("  AND TMW.WAREHOUSE_LEVEL = "+Constant.WAREHOUSE_LEVEL_02+"\n");

		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getWarehouseEnable_Aim(Long companyId, String areaIds)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DISTINCT TMW.WAREHOUSE_ID, TMW.WAREHOUSE_NAME\n" );
		sql.append("  FROM TM_WAREHOUSE TMW, TM_BUSINESS_AREA TBA\n" );
		sql.append(" WHERE TMW.ENTITY_CODE = TBA.ERP_CODE\n" );
		sql.append("  AND TMW.STATUS = "+Constant.STATUS_ENABLE+"\n");
		sql.append("  AND TMW.WAREHOUSE_TYPE = "+Constant.WAREHOUSE_TYPE_01+"\n");
		sql.append("  AND TMW.COMPANY_ID = "+companyId+"\n");
		sql.append("  AND TMW.WAREHOUSE_LEVEL = "+Constant.WAREHOUSE_LEVEL_02+"\n");
		sql.append("  AND TBA.AREA_ID IN ("+areaIds+")\n");

		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public String getWareHouseAreaSql(String companyId, String areaId, String tableName){
		StringBuffer sql = new StringBuffer();

		sql.append("(SELECT DISTINCT TW.WAREHOUSE_ID, TW.WAREHOUSE_CODE, TW.WAREHOUSE_NAME, TW.WAREHOUSE_TYPE, TW.DEALER_ID\n");
		sql.append("  FROM TM_WAREHOUSE TW, TM_BUSINESS_AREA TBA\n");  
		sql.append(" WHERE TW.ENTITY_CODE = TBA.ERP_CODE\n");  
		sql.append("   AND TW.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND TW.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TBA.AREA_ID IN ("+areaId+")) "+tableName+"");

		return sql.toString();
	}
	
	/**
	 * Function         : 车厂库存查询---汇总查询
	 * @param           : 
	 * @return          : 根据车系分组汇总的库存信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-28
	 */
	public PageResult<Map<String, Object>> getStorageTotal(String wareType, String poseId, String warehouseId,String groupCode,String materialCode,String batchNo,String batchNoA,String vin,String days,Long companyId,String areaId,int curPage,int pageSize)throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		
		sql.append("WITH R AS\n");
		sql.append(" (SELECT ORR.MATERIAL_ID,\n");  
		sql.append("         ORR.WAREHOUSE_ID,\n");  
		sql.append("         VMGR.GROUP_ID AS PACKAGE_ID,\n");  
		sql.append("         (SUM(NVL(ORR.AMOUNT, 0)) - SUM(NVL(ORR.DELIVERY_AMOUNT, 0))) AS RESERVE_AMOUNT,\n");  
		sql.append("         ORR.BATCH_NO\n");

		sql.append("    FROM TT_VS_ORDER_RESOURCE_RESERVE ORR, "+getWareHouseAreaSql(companyId.toString(), areaId, "TEMP")+", TM_VHCL_MATERIAL_GROUP_R VMGR,TM_VHCL_MATERIAL TVM,TM_VHCL_MATERIAL_GROUP  G\n");  
		sql.append("   WHERE ORR.RESERVE_STATUS = "+Constant.RESOURCE_RESERVE_STATUS_01+"\n");  
		sql.append("     AND ORR.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append("     AND ORR.MATERIAL_ID = VMGR.MATERIAL_ID\n"); 
		sql.append("     AND TVM.MATERIAL_ID = ORR.MATERIAL_ID\n");
		sql.append("     AND VMGR.GROUP_ID = G.GROUP_ID\n");
		sql.append("     AND ORR.WAREHOUSE_ID = TEMP.WAREHOUSE_ID\n");
		sql.append("     AND TEMP.WAREHOUSE_CODE = '"+wareType+"'\n");  

		if(!"-1".equals(warehouseId)&&warehouseId!=null&&!"".equals(warehouseId)){
			sql.append("     AND ORR.WAREHOUSE_ID = "+warehouseId+"\n" );
		}
		if (null != batchNo && !"".equals(batchNo)) {
			sql.append("   AND TO_NUMBER(SUBSTR(ORR.BATCH_NO||'000',0,7)) >= " + batchNo + "\n");
//			sql.append("   AND ORR.BATCH_NO LIKE '%" + batchNo.trim() + "%'\n") ;
		}
		if (null != batchNoA && !"".equals(batchNoA)) {
			sql.append("   AND TO_NUMBER(SUBSTR(ORR.BATCH_NO||'000',0,7)) <= " + batchNoA + "\n");
		}
		if(null != materialCode && !"".equals(materialCode)){
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != groupCode && !"".equals(groupCode)) {
			String[] array = groupCode.split(",");
			sql.append("   AND G.GROUP_CODE IN(\n");
			sql.append("      SELECT G.GROUP_CODE\n");
			sql.append("        FROM TM_VHCL_MATERIAL_GROUP G\n");  
			sql.append("          START WITH G.GROUP_CODE IN (\n");  
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append("         )\n");
			sql.append("   CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID\n");
			sql.append(")\n");
		}
		
		sql.append("having (SUM(NVL(ORR.AMOUNT, 0)) - SUM(NVL(ORR.DELIVERY_AMOUNT, 0))) <> 0\n");
		
		sql.append("   GROUP BY ORR.MATERIAL_ID, ORR.WAREHOUSE_ID, VMGR.GROUP_ID ,ORR.BATCH_NO),\n");  
		sql.append("T AS\n");  
		sql.append(" (SELECT TMV.MATERIAL_ID,\n");  
		sql.append("         TMV.WAREHOUSE_ID,\n");  
		sql.append("         TEMP.WAREHOUSE_NAME,\n"); 
		sql.append("         TMV.PACKAGE_ID,\n");  
		sql.append("         NVL(COUNT(TMV.VEHICLE_ID), 0) STOCK_AMOUNT,\n");  
		sql.append("         TMV.BATCH_NO\n");  
		sql.append("    FROM TM_VEHICLE TMV, "+getWareHouseAreaSql(companyId.toString(), areaId, "TEMP")+", TM_VHCL_MATERIAL TVM\n");  
		sql.append("   WHERE TMV.WAREHOUSE_ID = TEMP.WAREHOUSE_ID\n");  
		sql.append("     AND TEMP.WAREHOUSE_CODE = '"+wareType+"'\n");  
		sql.append("     AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_02+"\n");  
		sql.append("     AND TMV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");  
		sql.append("     AND TMV.ORG_ID IS NULL\n");  
		sql.append("     AND TMV.DEALER_ID IS NULL\n");  
		sql.append("     AND TMV.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append("     AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("     AND EXISTS\n");  
		sql.append("   (SELECT 1\n");  
		sql.append("            FROM (SELECT TAG.MATERIAL_GROUP_ID\n");  
		sql.append("                    FROM TM_AREA_GROUP TAG, TM_POSE_BUSINESS_AREA TPBA\n");  
		sql.append("                   WHERE TAG.AREA_ID = TPBA.AREA_ID\n");  
		sql.append("                     AND TPBA.POSE_ID = "+poseId+") BB\n");  
		sql.append("           WHERE BB.MATERIAL_GROUP_ID = TMV.SERIES_ID\n");  
		sql.append("              OR BB.MATERIAL_GROUP_ID = TMV.MODEL_ID\n");  
		sql.append("              OR BB.MATERIAL_GROUP_ID = TMV.PACKAGE_ID)\n"); 
		
		if(!"-1".equals(warehouseId)&&warehouseId!=null&&!"".equals(warehouseId)){
			sql.append("     AND TEMP.WAREHOUSE_ID = "+warehouseId+"\n" );
		}
		/*if (null != batchNo && !"".equals(batchNo)) {
			sql.append("   AND TMV.BATCH_NO LIKE '%" + batchNo.trim() + "%'\n") ;
		}*/
		if (null != batchNo && !"".equals(batchNo)) {
			sql.append("   AND TO_NUMBER(SUBSTR(TMV.BATCH_NO||'000',0,7)) >= " + batchNo + "\n");
		}
		if (null != batchNoA && !"".equals(batchNoA)) {
			sql.append("   AND TO_NUMBER(SUBSTR(TMV.BATCH_NO||'000',0,7)) <= " + batchNoA + "\n");
		}
		if(null != materialCode && !"".equals(materialCode)){
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");

		}
		if (null != groupCode && !"".equals(groupCode)) {
			String[] groupCodes = groupCode.split(",");
			if (null != groupCodes && groupCodes.length>0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < groupCodes.length; i++) {
					buffer.append("'").append(groupCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length()-1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+buffer.toString()+")))) \n");
			
			}
		}
		sql.append("   GROUP BY TMV.MATERIAL_ID, TMV.WAREHOUSE_ID, TMV.PACKAGE_ID,TMV.BATCH_NO, TEMP.WAREHOUSE_NAME),\n");  
		
		sql.append("MOVEO AS\n");
		sql.append(" (select ts.from_warehouse_id warehouse_id,\n");  
		sql.append("		 temp.warehouse_name,\n");
		sql.append("		 tvmg.group_id,\n");
		sql.append("         tvm.material_id,\n");  
		sql.append("         tmv.batch_no,\n");  
		sql.append("         count(tmv.vehicle_id) move_e_amount\n");  
		sql.append("    from tt_vs_dlvry_erp     tvde,\n");  
		sql.append("         tt_vs_dlvry_erp_dtl tvded,\n");  
		sql.append("         tt_sto              ts,\n");  
		sql.append("         tm_vehicle          tmv,\n");  
		sql.append("         tm_vhcl_material    tvm,\n");  
		sql.append("		 tm_vhcl_material_group_r tvmgr,\n");
		sql.append("         tm_vhcl_material_group tvmg,\n");
		sql.append(  		 getWareHouseAreaSql(companyId.toString(), areaId, "TEMP")) ;
		sql.append("   where tvde.delivery_id = ts.sto_id\n");  
		sql.append("     and tvde.sendcar_header_id = tvded.sendcar_header_id\n");  
		sql.append("     and tvded.vin = tmv.vin\n");  
		sql.append("     and tmv.material_id = tvm.material_id\n");  
		sql.append("     and tmv.warehouse_id = ts.from_warehouse_id\n");  
		sql.append("	 and tvm.material_id = tvmgr.material_id\n");
		sql.append("     and tvmgr.group_id = tvmg.group_id\n");
		sql.append("     and ts.from_warehouse_id = temp.warehouse_id\n");
		sql.append("     AND TEMP.WAREHOUSE_CODE = '"+wareType+"'\n");  
		sql.append("     and ts.status <> ").append(Constant.STO_STATUS_04).append("\n") ;
		
		if(!"-1".equals(warehouseId)&&warehouseId!=null&&!"".equals(warehouseId)){
			sql.append("     AND ts.from_warehouse_id = "+warehouseId+"\n" );
		}
		if (null != batchNo && !"".equals(batchNo)) {
			sql.append("   AND TO_NUMBER(SUBSTR(tmv.BATCH_NO||'000',0,7)) >= " + batchNo + "\n");
//			sql.append("   AND ORR.BATCH_NO LIKE '%" + batchNo.trim() + "%'\n") ;
		}
		if (null != batchNoA && !"".equals(batchNoA)) {
			sql.append("   AND TO_NUMBER(SUBSTR(tmv.BATCH_NO||'000',0,7)) <= " + batchNoA + "\n");
		}
		if(null != materialCode && !"".equals(materialCode)){
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != groupCode && !"".equals(groupCode)) {
			String[] array = groupCode.split(",");
			sql.append("   AND TVMG.GROUP_CODE IN(\n");
			sql.append("      SELECT G.GROUP_CODE\n");
			sql.append("        FROM TM_VHCL_MATERIAL_GROUP G\n");  
			sql.append("          START WITH G.GROUP_CODE IN (\n");  
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append("         )\n");
			sql.append("   CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID\n");
			sql.append(")\n");
		}
		
		sql.append("   group by ts.from_warehouse_id, tvm.material_id, tmv.batch_no,tvmg.group_id,temp.warehouse_name)\n");

		
		sql.append("SELECT VW.SERIES_NAME,\n");  
		sql.append("       VW.MODEL_NAME,\n");  
		sql.append("       VW.PACKAGE_CODE,\n");  
		sql.append("       VW.PACKAGE_NAME,\n");  
		sql.append("       M.COLOR_NAME,\n");  
		sql.append("       M.MATERIAL_ID,\n");  
		sql.append("       M.MATERIAL_CODE,\n");  
		sql.append("       M.MATERIAL_NAME,\n");  
		sql.append("       X.STOCK_AMOUNT,\n");  
		sql.append("       X.RESERVE_AMOUNT,\n");  
		sql.append("       X.WAREHOUSE_NAME,\n");
		sql.append("       (X.STOCK_AMOUNT - X.RESERVE_AMOUNT) RESOURCE_AMOUNT,\n");  
		sql.append("	   MOVE_E_AMOUNT,\n");
		sql.append("       X.BATCH_NO\n");  
		sql.append("  FROM (SELECT NVL(NVL(T.MATERIAL_ID, R.MATERIAL_ID),MOVEO.MATERIAL_ID) MATERIAL_ID,\n");  
		sql.append("               NVL(NVL(T.PACKAGE_ID, R.PACKAGE_ID),MOVEO.GROUP_ID) PACKAGE_ID,\n");  
		sql.append("               NVL(T.STOCK_AMOUNT, 0) STOCK_AMOUNT,\n");  
		sql.append("               NVL(R.RESERVE_AMOUNT, 0) RESERVE_AMOUNT,\n");  
		sql.append("			   NVL(MOVEO.MOVE_E_AMOUNT, 0) MOVE_E_AMOUNT,\n");
		sql.append("               NVL(NVL(T.BATCH_NO, R.BATCH_NO), MOVEO.BATCH_NO) BATCH_NO,NVL(NVL(T.WAREHOUSE_NAME,T.WAREHOUSE_NAME), MOVEO.WAREHOUSE_NAME) WAREHOUSE_NAME\n");  
		sql.append("          FROM T\n");  
		sql.append("		  FULL OUTER JOIN R\n");
		sql.append("            ON T.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("           AND T.WAREHOUSE_ID = R.WAREHOUSE_ID\n");  
		sql.append("           AND (T.BATCH_NO IS NULL OR R.BATCH_NO IS NULL OR\n");  
		sql.append("               R.BATCH_NO = T.BATCH_NO)\n");  
		sql.append("          FULL OUTER JOIN MOVEO\n");  
		sql.append("            ON MOVEO.MATERIAL_ID = T.MATERIAL_ID\n");  
		sql.append("           AND MOVEO.WAREHOUSE_ID = T.WAREHOUSE_ID\n");  
		sql.append("           AND (T.BATCH_NO IS NULL OR MOVEO.BATCH_NO IS NULL OR\n");  
		sql.append("               MOVEO.BATCH_NO = T.BATCH_NO)) X,\n");

		sql.append("       VW_MATERIAL_GROUP VW,\n");  
		sql.append("       TM_VHCL_MATERIAL M\n");  
		sql.append(" WHERE X.PACKAGE_ID = VW.PACKAGE_ID\n");  
		sql.append("   AND M.MATERIAL_ID = X.MATERIAL_ID\n");
		sql.append("  ORDER BY VW.SERIES_NAME,M.MATERIAL_CODE,X.BATCH_NO\n");

		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> getStorageTotal_CVS(String wareType, String poseId, String warehouseId,String groupCode,String materialCode,String batchNo,String batchNoA,String vin,String days,Long companyId,String areaId,int curPage,int pageSize)throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		
		sql.append("WITH R AS\n");
		sql.append(" (SELECT ORR.MATERIAL_ID,\n");  
		sql.append("         ORR.WAREHOUSE_ID,\n");  
		sql.append("         VMGR.GROUP_ID AS PACKAGE_ID,\n");  
		sql.append("         (SUM(NVL(ORR.AMOUNT, 0)) - SUM(NVL(ORR.DELIVERY_AMOUNT, 0))) AS RESERVE_AMOUNT,\n");  
		sql.append("         ORR.BATCH_NO\n");

		sql.append("    FROM TT_VS_ORDER_RESOURCE_RESERVE ORR, "+getWareHouseAreaSql(companyId.toString(), areaId, "TEMP")+", TM_VHCL_MATERIAL_GROUP_R VMGR,TM_VHCL_MATERIAL TVM,TM_VHCL_MATERIAL_GROUP  G\n");  
		sql.append("   WHERE ORR.RESERVE_STATUS = "+Constant.RESOURCE_RESERVE_STATUS_01+"\n");  
		sql.append("     AND ORR.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append("     AND ORR.MATERIAL_ID = VMGR.MATERIAL_ID\n"); 
		sql.append("     AND TVM.MATERIAL_ID = ORR.MATERIAL_ID\n");
		sql.append("     AND VMGR.GROUP_ID = G.GROUP_ID\n");
		sql.append("     AND ORR.WAREHOUSE_ID = TEMP.WAREHOUSE_ID\n");
		sql.append("     AND TEMP.WAREHOUSE_CODE = '"+wareType+"'\n");  

		if(!"-1".equals(warehouseId)&&warehouseId!=null&&!"".equals(warehouseId)){
			sql.append("     AND ORR.WAREHOUSE_ID = "+warehouseId+"\n" );
		}
		if (null != batchNo && !"".equals(batchNo)) {
			sql.append("   AND TO_NUMBER(SUBSTR(ORR.BATCH_NO||'000',0,7)) >= " + batchNo + "\n");
//			sql.append("   AND ORR.BATCH_NO LIKE '%" + batchNo.trim() + "%'\n") ;
		}
		if (null != batchNoA && !"".equals(batchNoA)) {
			sql.append("   AND TO_NUMBER(SUBSTR(ORR.BATCH_NO||'000',0,7)) <= " + batchNoA + "\n");
		}
		if(null != materialCode && !"".equals(materialCode)){
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != groupCode && !"".equals(groupCode)) {
			String[] array = groupCode.split(",");
			sql.append("   AND G.GROUP_CODE IN(\n");
			sql.append("      SELECT G.GROUP_CODE\n");
			sql.append("        FROM TM_VHCL_MATERIAL_GROUP G\n");  
			sql.append("          START WITH G.GROUP_CODE IN (\n");  
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append("         )\n");
			sql.append("   CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID\n");
			sql.append(")\n");
		}
		
		sql.append("having (SUM(NVL(ORR.AMOUNT, 0)) - SUM(NVL(ORR.DELIVERY_AMOUNT, 0))) <> 0\n");
		
		sql.append("   GROUP BY ORR.MATERIAL_ID, ORR.WAREHOUSE_ID, VMGR.GROUP_ID ,ORR.BATCH_NO),\n");  
		sql.append("T AS\n");  
		sql.append(" (SELECT TMV.MATERIAL_ID,\n");  
		sql.append("         TMV.WAREHOUSE_ID,\n");  
		sql.append("         TEMP.WAREHOUSE_NAME,\n"); 
		sql.append("         TMV.PACKAGE_ID,\n");  
		sql.append("         NVL(COUNT(TMV.VEHICLE_ID), 0) STOCK_AMOUNT,\n");  
		sql.append("         TMV.BATCH_NO\n");  
		sql.append("    FROM TM_VEHICLE TMV, "+getWareHouseAreaSql(companyId.toString(), areaId, "TEMP")+", TM_VHCL_MATERIAL TVM\n");  
		sql.append("   WHERE TMV.WAREHOUSE_ID = TEMP.WAREHOUSE_ID\n");  
		sql.append("     AND TEMP.WAREHOUSE_CODE = '"+wareType+"'\n");  
		sql.append("     AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_02+"\n");  
		sql.append("     AND TMV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");  
		sql.append("     AND TMV.ORG_ID IS NULL\n");  
		sql.append("     AND TMV.DEALER_ID IS NULL\n");  
		sql.append("     AND TMV.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append("     AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("     AND EXISTS\n");  
		sql.append("   (SELECT 1\n");  
		sql.append("            FROM (SELECT TAG.MATERIAL_GROUP_ID\n");  
		sql.append("                    FROM TM_AREA_GROUP TAG, TM_POSE_BUSINESS_AREA TPBA\n");  
		sql.append("                   WHERE TAG.AREA_ID = TPBA.AREA_ID\n");  
		sql.append("                     AND TPBA.POSE_ID = "+poseId+") BB\n");  
		sql.append("           WHERE BB.MATERIAL_GROUP_ID = TMV.SERIES_ID\n");  
		sql.append("              OR BB.MATERIAL_GROUP_ID = TMV.MODEL_ID\n");  
		sql.append("              OR BB.MATERIAL_GROUP_ID = TMV.PACKAGE_ID)\n"); 
		
		if(!"-1".equals(warehouseId)&&warehouseId!=null&&!"".equals(warehouseId)){
			sql.append("     AND TEMP.WAREHOUSE_ID = "+warehouseId+"\n" );
		}
		/*if (null != batchNo && !"".equals(batchNo)) {
			sql.append("   AND TMV.BATCH_NO LIKE '%" + batchNo.trim() + "%'\n") ;
		}*/
		if (null != batchNo && !"".equals(batchNo)) {
			sql.append("   AND TO_NUMBER(SUBSTR(TMV.BATCH_NO||'000',0,7)) >= " + batchNo + "\n");
		}
		if (null != batchNoA && !"".equals(batchNoA)) {
			sql.append("   AND TO_NUMBER(SUBSTR(TMV.BATCH_NO||'000',0,7)) <= " + batchNoA + "\n");
		}
		if(null != materialCode && !"".equals(materialCode)){
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");

		}
		if (null != groupCode && !"".equals(groupCode)) {
			String[] groupCodes = groupCode.split(",");
			if (null != groupCodes && groupCodes.length>0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < groupCodes.length; i++) {
					buffer.append("'").append(groupCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length()-1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+buffer.toString()+")))) \n");
			
			}
		}
		sql.append("   GROUP BY TMV.MATERIAL_ID, TMV.WAREHOUSE_ID, TMV.PACKAGE_ID,TMV.BATCH_NO, TEMP.WAREHOUSE_NAME)\n");  
		
		/*sql.append("MOVEO AS\n");
		sql.append(" (select ts.from_warehouse_id warehouse_id,\n");  
		sql.append("		 temp.warehouse_name,\n");
		sql.append("		 tvmg.group_id,\n");
		sql.append("         tvm.material_id,\n");  
		sql.append("         tmv.batch_no,\n");  
		sql.append("         count(tmv.vehicle_id) move_e_amount\n");  
		sql.append("    from tt_vs_dlvry_erp     tvde,\n");  
		sql.append("         tt_vs_dlvry_erp_dtl tvded,\n");  
		sql.append("         tt_sto              ts,\n");  
		sql.append("         tm_vehicle          tmv,\n");  
		sql.append("         tm_vhcl_material    tvm,\n");  
		sql.append("		 tm_vhcl_material_group_r tvmgr,\n");
		sql.append("         tm_vhcl_material_group tvmg,\n");
		sql.append(  		 getWareHouseAreaSql(companyId.toString(), areaId, "TEMP")) ;
		sql.append("   where tvde.delivery_id = ts.sto_id\n");  
		sql.append("     and tvde.sendcar_header_id = tvded.sendcar_header_id\n");  
		sql.append("     and tvded.vin = tmv.vin\n");  
		sql.append("     and tmv.material_id = tvm.material_id\n");  
		sql.append("     and tmv.warehouse_id = ts.from_warehouse_id\n");  
		sql.append("	 and tvm.material_id = tvmgr.material_id\n");
		sql.append("     and tvmgr.group_id = tvmg.group_id\n");
		sql.append("     and ts.from_warehouse_id = temp.warehouse_id\n");
		sql.append("     AND TEMP.WAREHOUSE_CODE = '"+wareType+"'\n");  
		sql.append("     and ts.status <> ").append(Constant.STO_STATUS_04).append("\n") ;
		
		if(!"-1".equals(warehouseId)&&warehouseId!=null&&!"".equals(warehouseId)){
			sql.append("     AND ts.from_warehouse_id = "+warehouseId+"\n" );
		}
		if (null != batchNo && !"".equals(batchNo)) {
			sql.append("   AND TO_NUMBER(SUBSTR(tmv.BATCH_NO||'000',0,7)) >= " + batchNo + "\n");
//			sql.append("   AND ORR.BATCH_NO LIKE '%" + batchNo.trim() + "%'\n") ;
		}
		if (null != batchNoA && !"".equals(batchNoA)) {
			sql.append("   AND TO_NUMBER(SUBSTR(tmv.BATCH_NO||'000',0,7)) <= " + batchNoA + "\n");
		}
		if(null != materialCode && !"".equals(materialCode)){
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != groupCode && !"".equals(groupCode)) {
			String[] array = groupCode.split(",");
			sql.append("   AND TVMG.GROUP_CODE IN(\n");
			sql.append("      SELECT G.GROUP_CODE\n");
			sql.append("        FROM TM_VHCL_MATERIAL_GROUP G\n");  
			sql.append("          START WITH G.GROUP_CODE IN (\n");  
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append("         )\n");
			sql.append("   CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID\n");
			sql.append(")\n");
		}
		
		sql.append("   group by ts.from_warehouse_id, tvm.material_id, tmv.batch_no,tvmg.group_id,temp.warehouse_name)\n");*/

		
		sql.append("SELECT VW.SERIES_NAME,\n");  
		sql.append("       VW.MODEL_NAME,\n");  
		sql.append("       VW.PACKAGE_CODE,\n");  
		sql.append("       VW.PACKAGE_NAME,\n");  
		sql.append("       M.COLOR_NAME,\n");  
		sql.append("       M.MATERIAL_ID,\n");  
		sql.append("       M.MATERIAL_CODE,\n");  
		sql.append("       M.MATERIAL_NAME,\n");  
		sql.append("       X.STOCK_AMOUNT,\n");  
		sql.append("       X.RESERVE_AMOUNT,\n");  
		sql.append("       X.WAREHOUSE_NAME,\n");
		sql.append("       (X.STOCK_AMOUNT - X.RESERVE_AMOUNT) RESOURCE_AMOUNT,\n");  
		sql.append("       X.BATCH_NO\n");  
		sql.append("  FROM (SELECT NVL(T.MATERIAL_ID, R.MATERIAL_ID) MATERIAL_ID,\n");  
		sql.append("               NVL(T.PACKAGE_ID, R.PACKAGE_ID) PACKAGE_ID,\n");  
		sql.append("               NVL(T.STOCK_AMOUNT, 0) STOCK_AMOUNT,\n");  
		sql.append("               NVL(R.RESERVE_AMOUNT, 0) RESERVE_AMOUNT,\n");  
		sql.append("               NVL(T.BATCH_NO, R.BATCH_NO) BATCH_NO,NVL(T.WAREHOUSE_NAME,T.WAREHOUSE_NAME) WAREHOUSE_NAME\n");  
		sql.append("          FROM T\n");  
		sql.append("		  FULL OUTER JOIN R\n");
		sql.append("            ON T.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("           AND T.WAREHOUSE_ID = R.WAREHOUSE_ID\n");  
		sql.append("           AND (T.BATCH_NO IS NULL OR R.BATCH_NO IS NULL OR\n");  
		sql.append("               R.BATCH_NO = T.BATCH_NO)) X,\n");  
		/*sql.append("          FULL OUTER JOIN MOVEO\n");  
		sql.append("            ON MOVEO.MATERIAL_ID = T.MATERIAL_ID\n");  
		sql.append("           AND MOVEO.WAREHOUSE_ID = T.WAREHOUSE_ID\n");  
		sql.append("           AND (T.BATCH_NO IS NULL OR MOVEO.BATCH_NO IS NULL OR\n");  
		sql.append("               MOVEO.BATCH_NO = T.BATCH_NO)) X,\n");*/

		sql.append("       VW_MATERIAL_GROUP VW,\n");  
		sql.append("       TM_VHCL_MATERIAL M\n");  
		sql.append(" WHERE X.PACKAGE_ID = VW.PACKAGE_ID\n");  
		sql.append("   AND M.MATERIAL_ID = X.MATERIAL_ID\n");
		sql.append("  ORDER BY VW.SERIES_NAME,M.MATERIAL_CODE,X.BATCH_NO\n");

		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return ps;
	}
	
	/**
	 * Function         : 车厂库存查询---明细查询
	 * @param           : 
	 * @return          : 明细查询的库存信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-28
	 */
	public PageResult<Map<String, Object>> getStorageDetail(String wareType, String batchNo, String batchNoA, String poseId,String warehouseId,String groupCode,String materialCode,String custBatch,String vin,String days,Long companyId,String areaIds,int curPage,int pageSize)throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       TVM.COLOR_NAME,\n" );
		sql.append("       TMV.VIN,\n" );
		sql.append("       TMV.BATCH_NO,\n" );
		sql.append("       TEMP.WAREHOUSE_NAME,\n" );
		sql.append("	   DECODE(TMV.SPECIAL_BATCH_NO,null,' ',TMV.SPECIAL_BATCH_NO) SPECIAL_BATCH_NO,");
		sql.append("       TO_CHAR(TMV.ORG_STORAGE_DATE, 'YYYY-MM-DD') STORAGE_DATE,\n" );
		sql.append("       CEIL(SYSDATE - TMV.ORG_STORAGE_DATE) DAYS\n" );
		sql.append("  FROM TM_VEHICLE       TMV,\n" );
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );	
		}
		sql.append("       TM_VHCL_MATERIAL TVM,\n" );
		sql.append("       "+getWareHouseAreaSql(companyId.toString(), areaIds, "TEMP")+"\n" );
		sql.append(" WHERE TMV.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TMV.WAREHOUSE_ID = TEMP.WAREHOUSE_ID\n" );
		sql.append("   AND TEMP.WAREHOUSE_CODE = '"+wareType+"'\n" );
		sql.append("           and exists (\n" );
		sql.append("           select 1 from (\n" );
		sql.append("           select tag.material_group_id\n" );
		sql.append("           from tm_area_group tag, tm_pose_business_area tpba\n" );
		sql.append("           where tag.area_id = tpba.area_id\n" );
		sql.append("           and tpba.pose_id ="+poseId+" ) bb\n" );
		sql.append("           where bb.material_group_id=tmv.series_id or  bb.material_group_id=tmv.model_id or bb.material_group_id=tmv.package_id\n" );
		sql.append("           )");
		sql.append("   AND TMV.WAREHOUSE_ID = TEMP.WAREHOUSE_ID(+)\n" );
		if(!"-1".equals(warehouseId)&&warehouseId!=null&&!"".equals(warehouseId)){
			sql.append("   AND TMV.WAREHOUSE_ID = "+warehouseId+"\n" );
		}
		if(!"".equals(materialCode)&&materialCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(materialCode, params, "TVM", "MATERIAL_CODE"));
		}
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
			sql.append(" AND (TMV.SERIES_ID=TVMG.GROUP_ID OR  TMV.MODEL_ID=TVMG.GROUP_ID OR TMV.PACKAGE_ID=TVMG.GROUP_ID)");
		}
		if(!"".equals(custBatch)&&custBatch!=null){
			sql.append("   AND TMV.SPECIAL_BATCH_NO LIKE '%"+custBatch+"%'\n" );
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin,"TMV")); 
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.ORG_STORAGE_DATE)>="+days.trim()+")\n");
		}
		/*if (null != batchNo && !"".equals(batchNo)) {
			sql.append("   AND TMV.BATCH_NO LIKE '%" + batchNo + "%'\n") ;
		}*/
		if (null != batchNo && !"".equals(batchNo)) {
			sql.append("   AND TO_NUMBER(SUBSTR(TMV.BATCH_NO||'000',0,7)) >= " + batchNo + "\n");
//			sql.append("   AND ORR.BATCH_NO LIKE '%" + batchNo.trim() + "%'\n") ;
		}
		if (null != batchNoA && !"".equals(batchNoA)) {
			sql.append("   AND TO_NUMBER(SUBSTR(TMV.BATCH_NO||'000',0,7)) <= " + batchNoA + "\n");
		}
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_02+"\n" );
		sql.append("   AND TMV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");
		sql.append("   AND TMV.ORG_ID IS NULL\n" );
		sql.append("   AND TMV.DEALER_ID IS NULL\n" );
		sql.append("   AND TMV.OEM_COMPANY_ID ="+companyId+"\n" );
		sql.append("  ORDER BY TVM.MATERIAL_CODE,TMV.BATCH_NO,STORAGE_DATE" );
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return ps;
	}
	
	
	/**
	 * Function         : 详细车籍查询
	 * @param           : 
	 * @return          : 车辆详细信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-28
	 */
	public PageResult<Map<String, Object>> getVhclDetail(String enginNo, String areaIds,String areaId, String dealerCode,String groupCode,String vin,Long companyId,int curPage,int pageSize)throws Exception{
		List <Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DECODE(TMD.DEALER_CODE, NULL, ' ', TMD.DEALER_CODE) DEALER_CODE,\n");
		sql.append("       DECODE(TMD.DEALER_SHORTNAME, NULL, ' ', TMD.DEALER_SHORTNAME) DEALER_NAME,\n" );
		sql.append("       TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       TVM.COLOR_NAME,\n" );
		sql.append("       TMV.VIN,\n" );
		sql.append("       TMV.VEHICLE_ID,");
		sql.append("       TVM.VHCL_PRICE,\n" );
		sql.append("       TMV.NODE_CODE,\n" );
		sql.append("       TMV.LIFE_CYCLE,TMV.BATCH_NO,\n" );
		sql.append("       TMV.LOCK_STATUS\n" );
		sql.append("  FROM TM_VEHICLE TMV,\n" );
		
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );	
		}
		sql.append("	   TM_VHCL_MATERIAL TVM,\n" );
		sql.append("(SELECT DISTINCT TW.WAREHOUSE_ID, TW.WAREHOUSE_NAME, TW.WAREHOUSE_TYPE\n");
		sql.append("  FROM TM_WAREHOUSE TW, TM_BUSINESS_AREA TBA\n");  
		sql.append(" WHERE TW.ENTITY_CODE = TBA.ERP_CODE\n");  
		sql.append("   AND TW.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND TW.COMPANY_ID = "+companyId+"\n"); 
		if (!"".equals(areaId) && areaId!=null) {
			sql.append("   AND TBA.AREA_ID = "+areaId+"\n");
		}
		sql.append("   AND TBA.AREA_ID IN ("+areaIds+")) TEMP,");
		sql.append("       TM_DEALER TMD\n" );
		sql.append(" WHERE TMD.DEALER_ID(+) = TMV.DEALER_ID\n" );
		sql.append("   AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TMV.WAREHOUSE_ID = TEMP.WAREHOUSE_ID(+)\n");
		sql.append("   AND TMV.OEM_COMPANY_ID = "+companyId+"\n");
		
		if(!CommonUtils.isNullString(enginNo)) {
			sql.append("     and tmv.engine_no = '").append(enginNo).append("'\n") ;
		}
		
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin,"TMV")); 
		}
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
			sql.append(" AND (TMV.SERIES_ID=TVMG.GROUP_ID OR  TMV.MODEL_ID=TVMG.GROUP_ID OR TMV.PACKAGE_ID=TVMG.GROUP_ID)");
		}
		if(!"".equals(dealerCode)&&dealerCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		/*if (!"".equals(areaId)) {
			sql.append("AND TMV.AREA_ID = "+areaId+"\n");
		}
		if (!"".equals(areaIds) && "".equals(areaId)) {
			sql.append("AND TMV.AREA_ID IN ("+areaIds+")\n");
		}*/
		sql.append("ORDER BY  TVM.MATERIAL_CODE,DEALER_CODE,TMV.LIFE_CYCLE");
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return ps;
	}
	/**
	 * Function         : 详细车籍查询
	 * @param           : 
	 * @return          : 车辆详细信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-28
	 */
	public List<Map<String, Object>> getLoadVhclDetail(String enginNo, String areaIds,String areaId, String dealerCode,String groupCode,String vin,Long companyId,int curPage,int pageSize)throws Exception{
		List <Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DECODE(TMD.DEALER_CODE, NULL, ' ', TMD.DEALER_CODE) DEALER_CODE,\n");
		sql.append("       DECODE(TMD.DEALER_SHORTNAME, NULL, ' ', TMD.DEALER_SHORTNAME) DEALER_NAME,\n" );
		sql.append("       TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       TVM.COLOR_NAME,\n" );
		sql.append("       TMV.VIN,\n" );
		sql.append("       TMV.VEHICLE_ID,TMV.BATCH_NO,");
		sql.append("       TVM.VHCL_PRICE,\n" );
		sql.append("       TMV.NODE_CODE,\n" );
		sql.append("       TC1.CODE_DESC LIFE_CYCLE,\n" );
		sql.append("       TC2.CODE_DESC LOCK_STATUS\n" );
		sql.append("  FROM TM_VEHICLE TMV,TC_CODE TC1,TC_CODE TC2,\n" );
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );	
		}
		sql.append("	   TM_VHCL_MATERIAL TVM,\n" );
		sql.append("(SELECT DISTINCT TW.WAREHOUSE_ID, TW.WAREHOUSE_NAME, TW.WAREHOUSE_TYPE\n");
		sql.append("  FROM TM_WAREHOUSE TW, TM_BUSINESS_AREA TBA\n");  
		sql.append(" WHERE TW.ENTITY_CODE = TBA.ERP_CODE\n");  
		sql.append("   AND TW.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND TW.COMPANY_ID = "+companyId+"\n"); 
		if (!"".equals(areaId) && areaId!=null) {
			sql.append("   AND TBA.AREA_ID = "+areaId+"\n");
		}
		sql.append("   AND TBA.AREA_ID IN ("+areaIds+")) TEMP,");
		sql.append("       TM_DEALER TMD\n" );
		sql.append(" WHERE TMD.DEALER_ID(+) = TMV.DEALER_ID\n" );
		sql.append("   AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TMV.WAREHOUSE_ID = TEMP.WAREHOUSE_ID(+)\n");
		sql.append("   AND TMV.OEM_COMPANY_ID = "+companyId+"\n");
		
		if(!CommonUtils.isNullString(enginNo)) {
			sql.append("     and tmv.engine_no = '").append(enginNo).append("'\n") ;
		}
		
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin,"TMV")); 
		}
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
			sql.append(" AND (TMV.SERIES_ID=TVMG.GROUP_ID OR  TMV.MODEL_ID=TVMG.GROUP_ID OR TMV.PACKAGE_ID=TVMG.GROUP_ID)");
		}
		if(!"".equals(dealerCode)&&dealerCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		/*if (!"".equals(areaId)) {
			sql.append("AND TMV.AREA_ID = "+areaId+"\n");
		}
		if (!"".equals(areaIds) && "".equals(areaId)) {
			sql.append("AND TMV.AREA_ID IN ("+areaIds+")\n");
		}*/
		sql.append("AND TC1.CODE_ID=TMV.LIFE_CYCLE  AND TC2.CODE_ID=TMV.LOCK_STATUS\n");
		sql.append("ORDER BY  TVM.MATERIAL_CODE,DEALER_CODE,TMV.LIFE_CYCLE");
		List<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName());
		return ps;
	}
	
	/**
	 * 车厂库存明细
	 */
	public List<Map<String, Object>> getStockDetail(Map<String, Object> map)throws Exception{
		
		String materalId = (String)map.get("materalId");
		String companyId = (String)map.get("companyId");
		String warehouseId = (String)map.get("warehouseId");
		
		StringBuffer sql= new StringBuffer();

		sql.append("SELECT TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TMV.BATCH_NO,\n");  
		sql.append("       COUNT(TMV.VEHICLE_ID) AMOUNT\n");  
		sql.append("  FROM TM_VEHICLE TMV, TM_VHCL_MATERIAL TVM, TM_WAREHOUSE W\n");  
		sql.append(" WHERE TMV.WAREHOUSE_ID = W.WAREHOUSE_ID\n");   
		sql.append("   AND W.WAREHOUSE_TYPE <> "+Constant.WAREHOUSE_TYPE_04+"\n"); 
		sql.append("   AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n"); 
		sql.append("   AND TMV.ORG_TYPE IS NULL\n");  
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_02+"\n");  
		sql.append("   AND TMV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");  
		sql.append("   AND TMV.ORG_ID IS NULL\n");  
		sql.append("   AND TMV.DEALER_ID IS NULL\n");  
		sql.append("   AND TMV.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TMV.MATERIAL_ID = "+materalId+"\n");  
		if(warehouseId != null && !warehouseId.equals("") && !warehouseId.equals("-1")){
			sql.append("   AND TMV.WAREHOUSE_ID = "+warehouseId+"\n");  
		}
		sql.append(" GROUP BY TVM.MATERIAL_ID,\n");  
		sql.append("          TVM.MATERIAL_CODE,\n");  
		sql.append("          TVM.MATERIAL_NAME,\n");  
		sql.append("          TMV.BATCH_NO");

		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	
	public List<Map<String, Object>> getReserveDetail(Map<String, Object> map)throws Exception{
		
		String materalId = (String)map.get("materalId");
		String companyId = (String)map.get("companyId");
		String warehouseId = (String)map.get("warehouseId");
		
		StringBuffer sql= new StringBuffer();

		sql.append("SELECT TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");    
		sql.append("       TVORR.BATCH_NO,\n");
		sql.append("       (SUM(NVL(AMOUNT, 0)) - SUM(NVL(DELIVERY_AMOUNT, 0))) AS AMOUNT\n");  
		sql.append("  FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR, TM_VHCL_MATERIAL TVM\n");  
		sql.append(" WHERE TVORR.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVORR.RESERVE_STATUS = "+Constant.RESOURCE_RESERVE_STATUS_01+"\n");  
		sql.append("   AND TVORR.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TVORR.MATERIAL_ID = "+materalId+"\n");  
		if(warehouseId != null && !warehouseId.equals("") && !warehouseId.equals("-1")){
			sql.append("   AND TVORR.WAREHOUSE_ID = "+warehouseId+"\n");  
		}
		sql.append(" GROUP BY TVM.MATERIAL_ID, TVM.MATERIAL_CODE, TVM.MATERIAL_NAME, TVORR.BATCH_NO");

		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getAvaDetail(Map<String, Object> map)throws Exception{
		
		String materalId = (String)map.get("materalId");
		String companyId = (String)map.get("companyId");
		String warehouseId = (String)map.get("warehouseId");
		
		StringBuffer sql= new StringBuffer();

		sql.append("SELECT A.MATERIAL_ID,\n");
		sql.append("       A.MATERIAL_CODE,\n");  
		sql.append("       A.MATERIAL_NAME,\n");  
		sql.append("       A.BATCH_NO,\n");  
		sql.append("       A.STOCK_AMOUNT - NVL(B.RESERVE_AMOUNT, 0) AMOUNT\n");  
		sql.append("  FROM (SELECT TVM.MATERIAL_ID,\n");  
		sql.append("               TVM.MATERIAL_CODE,\n");  
		sql.append("               TVM.MATERIAL_NAME,\n");  
		sql.append("               TMV.BATCH_NO,\n");  
		sql.append("               COUNT(TMV.VEHICLE_ID) STOCK_AMOUNT\n");  
		sql.append("          FROM TM_VEHICLE TMV, TM_VHCL_MATERIAL TVM, TM_WAREHOUSE W\n");  
		sql.append("         WHERE TMV.WAREHOUSE_ID = W.WAREHOUSE_ID\n");   
		sql.append("           AND W.WAREHOUSE_TYPE <> "+Constant.WAREHOUSE_TYPE_04+"\n"); 
		sql.append("           AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n"); 
		sql.append("           AND TMV.ORG_TYPE IS NULL\n");  
		sql.append("           AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_02+"\n");  
		sql.append("           AND TMV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");  
		sql.append("           AND TMV.ORG_ID IS NULL\n");  
		sql.append("           AND TMV.DEALER_ID IS NULL\n");  
		sql.append("           AND TMV.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append("           AND TMV.MATERIAL_ID = "+materalId+"\n");  
		if(warehouseId != null && !warehouseId.equals("") && !warehouseId.equals("-1")){
			sql.append("           AND TMV.WAREHOUSE_ID = "+warehouseId+"\n");  
		}
		sql.append("         GROUP BY TVM.MATERIAL_ID,\n");  
		sql.append("                  TVM.MATERIAL_CODE,\n");  
		sql.append("                  TVM.MATERIAL_NAME,\n");  
		sql.append("                  TMV.BATCH_NO) A,\n");  
		sql.append("       (SELECT TVM.MATERIAL_ID,\n");  
		sql.append("               TVM.MATERIAL_CODE,\n");  
		sql.append("               TVM.MATERIAL_NAME,\n");  
		sql.append("               TVORR.BATCH_NO,\n");  
		sql.append("               (SUM(NVL(AMOUNT, 0)) - SUM(NVL(DELIVERY_AMOUNT, 0))) AS RESERVE_AMOUNT\n");  
		sql.append("          FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR, TM_VHCL_MATERIAL TVM\n");  
		sql.append("         WHERE TVORR.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("           AND TVORR.RESERVE_STATUS = "+Constant.RESOURCE_RESERVE_STATUS_01+"\n");  
		sql.append("           AND TVORR.MATERIAL_ID = "+materalId+"\n");  
		sql.append("           AND TVORR.OEM_COMPANY_ID = "+companyId+"\n"); 
		if(warehouseId != null && !warehouseId.equals("") && !warehouseId.equals("-1")){
			sql.append("           AND TVORR.WAREHOUSE_ID = "+warehouseId+"\n");  
		} 
		sql.append("         GROUP BY TVM.MATERIAL_ID,\n");  
		sql.append("                  TVM.MATERIAL_CODE,\n");  
		sql.append("                  TVM.MATERIAL_NAME,\n");  
		sql.append("                  TVORR.BATCH_NO) B\n");  
		sql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)");
		sql.append("   AND A.BATCH_NO = B.BATCH_NO(+)");


		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public String getDealerIdByEndName(String endName){
		String dealerId = "";
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DEALER_ID\n" );
		sql.append("FROM TM_WAREHOUSE\n" );
		sql.append("WHERE WAREHOUSE_ID = ").append(endName);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		dealerId = String.valueOf(map.get("DEALER_ID"));
		return dealerId;
	}
	
	public List<Map<String, Object>> getPriceList(Map<String, Object> map) {
		String dealerId = (String) map.get("dealerId");
		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVP.PRICE_ID, TVP.PRICE_CODE, TVP.PRICE_DESC, TDPR.IS_DEFAULT\n");
		sql.append("  FROM vw_TT_VS_PRICE TVP, vw_TM_DEALER_PRICE_RELATION TDPR\n");
		sql.append(" WHERE TVP.PRICE_ID = TDPR.PRICE_ID\n");
		sql.append("   AND TVP.START_DATE <= SYSDATE\n");
		sql.append("   AND TVP.END_DATE >= SYSDATE\n");
		sql.append("   AND TVP.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TDPR.DEALER_ID = " + dealerId + "\n");
		sql.append("   ORDER BY TDPR.IS_DEFAULT ASC	");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public PageResult<Map<String, Object>> getMaterialList(String stName, String materialCode, String materialName, String batchNo, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.BATCH_NO||' - '||A.AVA_STOCK BATCH_NO,\n" );
		sql.append("       B.MATERIAL_CODE, B.MATERIAL_NAME, B.MATERIAL_ID\n" );
		sql.append("FROM(\n" );
		sql.append("    SELECT A.COMPANY_ID,A.MATERIAL_ID, A.BATCH_NO,\n" );
		sql.append("           A.STOCK_AMOUNT,A.SPECIAL_BATCH_NO,\n" );
		sql.append("           NVL(B.RESERVE_AMOUNT, 0) UN_DLVRY_AMOUNT,\n" );
		sql.append("           A.STOCK_AMOUNT - NVL(B.RESERVE_AMOUNT, 0) AVA_STOCK\n" );
		sql.append("      FROM (\n" );
		sql.append("           SELECT TV.OEM_COMPANY_ID COMPANY_ID,TV.MATERIAL_ID,\n" );
		sql.append("                  TV.SPECIAL_BATCH_NO,TV.BATCH_NO,\n" );
		sql.append("                   W.WAREHOUSE_ID,COUNT(TV.VEHICLE_ID) STOCK_AMOUNT\n" );
		sql.append("              FROM TM_VEHICLE TV, TM_WAREHOUSE W\n" );
		sql.append("             WHERE TV.WAREHOUSE_ID = W.WAREHOUSE_ID\n" );
		sql.append("               AND W.WAREHOUSE_TYPE <> ").append(Constant.WAREHOUSE_TYPE_04).append("\n");
		sql.append("               AND TV.LIFE_CYCLE = ").append(Constant.VEHICLE_LIFE_02).append("\n");
		sql.append("               AND TV.LOCK_STATUS = ").append(Constant.LOCK_STATUS_01).append("\n");
		if(Utility.testString(batchNo)){
			sql.append("         AND TV.BATCH_NO = ").append(batchNo);
		}
		sql.append("		 AND W.WAREHOUSE_ID = ").append(stName).append("\n");
		sql.append("             GROUP BY TV.MATERIAL_ID,TV.BATCH_NO,\n" );
		sql.append("                      TV.OEM_COMPANY_ID,\n" );
		sql.append("                      TV.SPECIAL_BATCH_NO,\n" );
		sql.append("                      W.WAREHOUSE_ID\n" );
		sql.append("             ) A,\n" );
		sql.append("           (SELECT OEM_COMPANY_ID COMPANY_ID,\n" );
		sql.append("                   MATERIAL_ID,\n" );
		sql.append("                   BATCH_NO,\n" );
		sql.append("                   WAREHOUSE_ID,\n" );
		sql.append("                   (SUM(NVL(AMOUNT, 0)) - SUM(NVL(DELIVERY_AMOUNT, 0))) RESERVE_AMOUNT\n" );
		sql.append("              FROM TT_VS_ORDER_RESOURCE_RESERVE\n" );
		sql.append("             WHERE RESERVE_STATUS = 11581001\n" );
		sql.append("             GROUP BY OEM_COMPANY_ID, MATERIAL_ID, BATCH_NO,WAREHOUSE_ID) B\n" );
		sql.append("     WHERE A.COMPANY_ID = B.COMPANY_ID(+)\n" );
		sql.append("       AND A.MATERIAL_ID = B.MATERIAL_ID(+)\n" );
		sql.append("       AND A.BATCH_NO = B.BATCH_NO(+)\n" );
		sql.append("       AND A.WAREHOUSE_ID = B.WAREHOUSE_ID(+)\n" );
		sql.append("   ) A, TM_VHCL_MATERIAL B\n" );
		sql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID\n");

		if(Utility.testString(materialCode)){
			sql.append("AND B.MATERIAL_CODE LIKE '%").append(materialCode).append("%'\n");
		}
		if(Utility.testString(materialName)){
			sql.append("AND B.MATERIAL_NAME LIKE '%").append(materialName).append("%'\n");
		}

		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return ps;
	}
	
	public String getSinglePrice(String priceId, String valid){
		String price = "";
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT F_GET_PRICE(\n" );
		sql.append("       ").append(priceId);
		sql.append("       ,\n");
		sql.append("       ").append(valid);
		sql.append(") PRIC FROM DUAL");

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		price = String.valueOf(map.get("PRIC"));
		return price;
	}
	
	public String getMCount(String valid, String endName){
		String mcount = "0";
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TV.OEM_COMPANY_ID COMPANY_ID,\n" );
		sql.append("       TV.MATERIAL_ID,\n" );
		sql.append("       COUNT(TV.VEHICLE_ID) STOCK_AMOUNT\n" );
		sql.append("  FROM TM_VEHICLE TV, TM_WAREHOUSE W\n" );
		sql.append(" WHERE TV.WAREHOUSE_ID = W.WAREHOUSE_ID\n" );
		sql.append("   AND TV.LIFE_CYCLE = ").append(Constant.VEHICLE_LIFE_02).append("\n");
		sql.append("   AND TV.LOCK_STATUS = ").append(Constant.LOCK_STATUS_01).append("\n");
		sql.append("   AND W.WAREHOUSE_ID = ").append(endName);
		sql.append("   AND TV.MATERIAL_ID = ").append(valid);
		sql.append(" GROUP BY TV.MATERIAL_ID,\n" );
		sql.append("          TV.OEM_COMPANY_ID\n" );
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if(map!=null&&map.size()>0){
			mcount = String.valueOf(map.get("STOCK_AMOUNT"));
		}
		return mcount;
	}
	
	public List<Map<String, Object>> getvalids(String code){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT MATERIAL_ID\n" );
		sql.append("FROM TM_VHCL_MATERIAL\n" );
		sql.append("WHERE MATERIAL_CODE IN(").append(code).append(")");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public Long getmaterialIdByCode(String code){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT MATERIAL_ID\n" );
		sql.append("FROM TM_VHCL_MATERIAL\n" );
		sql.append("WHERE MATERIAL_CODE = '").append(code).append("'");

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		Long materialId = Long.parseLong(String.valueOf(map.get("MATERIAL_ID")));
		return materialId;
	}
	
	public PageResult<Map<String, Object>> getSTOList(String orgId, Long companyId,String beginTime, String endTime, String startName,String stoStatus, String endName, String areaIds,String ERP_ORDER_NO ,int curPage, int pageSize) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.ORDER_NO,A.STO_ID, B.WAREHOUSE_NAME FROM_NAME,\n" );
		sql.append("       C.WAREHOUSE_NAME TO_NAME, A.STATUS, A.PRINT_FLAG, \n" );
		sql.append("       TO_CHAR(A.STO_DATE, 'YYYY-MM-DD') STO_DATE,\n" );
		sql.append("       A.ERP_ORDER_NO, A.ERP_MSG, E.AMOUNT,\n" );
		sql.append("	   nvl(f.dlvry_count, 0) dlvry_count,  --发运数量\n");
		sql.append("       nvl(g.exist_count, 0) exist_count,  --入库数量\n");  
		sql.append("       nvl(f.dlvry_count, 0) - nvl(g.exist_count, 0) a_count,  --在途数量\n");  
		sql.append("       decode(E.AMOUNT - nvl(f.dlvry_count, 0),\n");  
		sql.append("              E.AMOUNT,\n");  
		sql.append("              '未发运',\n");  
		sql.append("              0,\n");  
		sql.append("              '发运完成',\n");  
		sql.append("              '部分发运') dlvry_status,  --发运状态\n");  
		sql.append("       decode(E.AMOUNT - nvl(g.exist_count, 0),\n");  
		sql.append("              E.AMOUNT,\n");  
		sql.append("              '未入库',\n");  
		sql.append("              0,\n");  
		sql.append("              '完全入库',\n");  
		sql.append("              '部分入库') exist_status   --收车入库状态\n");

		sql.append("FROM TT_STO A, "+getWareHouseAreaSql(companyId.toString(), areaIds, "B")+", TM_WAREHOUSE C, \n" );
		sql.append("     (\n" );
		sql.append("       SELECT D.STO_ID, SUM(D.AMOUNT) AMOUNT\n" );
		sql.append("       FROM TT_STO_DTL D\n" );
		sql.append("       GROUP BY D.STO_ID\n" );
		sql.append("      ) E,\n" );
		sql.append("(select ts.sto_id, count(tvded.detail_id) dlvry_count\n");
		sql.append("          from tt_sto ts, tt_vs_dlvry_erp tvde, tt_vs_dlvry_erp_dtl tvded\n");  
		sql.append("         where ts.sto_id = tvde.delivery_id\n");  
		sql.append("           and tvde.sendcar_header_id = tvded.sendcar_header_id\n");  
		sql.append("         group by ts.sto_id) F,\n");  
		sql.append("       (select ts.sto_id, count(tvded.detail_id) exist_count\n");  
		sql.append("          from tt_sto              ts,\n");  
		sql.append("               tt_vs_dlvry_erp     tvde,\n");  
		sql.append("               tt_vs_dlvry_erp_dtl tvded,\n");  
		sql.append("               tm_vehicle          tmv\n");  
		sql.append("         where ts.sto_id = tvde.delivery_id\n");  
		sql.append("           and tvde.sendcar_header_id = tvded.sendcar_header_id\n");  
		sql.append("           and tvded.vin = tmv.vin\n");  
		sql.append("           and tmv.warehouse_id = ts.to_warehouse_id\n");  
		sql.append("         group by ts.sto_id) g\n");
		sql.append("WHERE A.FROM_WAREHOUSE_ID = B.WAREHOUSE_ID\n" );
		sql.append("AND A.TO_WAREHOUSE_ID = C.WAREHOUSE_ID\n" );
		sql.append("AND A.STO_ID = E.STO_ID\n" );
		sql.append("and A.Sto_Id = f.sto_id(+)\n");
		sql.append("   and a.sto_id = g.sto_id(+)\n");

		sql.append("AND A.COMPANY_ID = ").append(companyId).append("\n");
		
		if(Utility.testString(stoStatus)){
			sql.append("AND A.STATUS = ").append(stoStatus).append("\n");
		}
		if(Utility.testString(ERP_ORDER_NO)){
			sql.append("AND A.ERP_ORDER_NO = ").append(ERP_ORDER_NO).append("\n");
		}
		if(Utility.testString(beginTime)){
			sql.append("AND A.STO_DATE >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}	
		if(Utility.testString(endTime)){
			sql.append("AND A.STO_DATE <= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(startName)){
			sql.append("AND A.FROM_WAREHOUSE_ID = ").append(startName).append("\n");
		}
		if(Utility.testString(endName)){
			sql.append("AND A.TO_WAREHOUSE_ID =").append(endName).append("\n");
		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("   AND exists (select 1 from vw_org_dealer vod where (vod.dealer_id = c.dealer_id or vod.dealer_id = b.dealer_id) and vod.root_org_id = ").append(orgId).append(") \n");
		}
		
		sql.append("ORDER BY A.ORDER_NO DESC ");
		
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return ps;
	}
	
	public Map<String, Object> getSTOMap(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.STO_ID, B.WAREHOUSE_NAME FROM_NAME,\n" );
		sql.append("       C.WAREHOUSE_NAME TO_NAME, A.STATUS,\n" );
		sql.append("       TO_CHAR(A.STO_DATE, 'YYYY-MM-DD') STO_DATE,\n" );
		sql.append("       A.ERP_ORDER_NO, A.ERP_MSG\n" );
		sql.append("FROM TT_STO A, TM_WAREHOUSE B, TM_WAREHOUSE C\n" );
		sql.append("WHERE A.FROM_WAREHOUSE_ID = B.WAREHOUSE_ID\n" );
		sql.append("AND A.TO_WAREHOUSE_ID = C.WAREHOUSE_ID\n" );
		sql.append("AND A.STO_ID = ").append(id);

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	public List<Map<String, Object>> getSTOInfo(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, B.MATERIAL_CODE, B.MATERIAL_NAME,\n" );
		sql.append("       A.BATCH_NO, A.AMOUNT\n" );
		sql.append("FROM TT_STO_DTL A, TM_VHCL_MATERIAL B\n" );
		sql.append("WHERE A.MATERIAL_ID = B.MATERIAL_ID\n" );
		sql.append("AND A.STO_ID = ").append(id);

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public PageResult<Map<String, Object>> getOrderResourceReserveQueryList(Map<String, Object> map,int curPage,int pageSize)throws Exception{
		String warehouseId = (String) map.get("warehouseId");
		String groupCode = (String) map.get("groupCode");
		String materialCode = (String) map.get("materialCode");
		String batchNo = (String) map.get("batchNo");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();

		sql.append("SELECT TEMP.WAREHOUSE_NAME,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TVORR.BATCH_NO,\n");  
		sql.append("       TVDR.REQ_ID ORDER_ID,\n");  
		sql.append("       TVDR.DLVRY_REQ_NO ORDER_NO,\n");  
		sql.append("       BTD.DEALER_SHORTNAME BEALER_NAME,\n");  
		sql.append("       NVL(TVD.DELIVERY_STATUS, TVDR.REQ_STATUS) ORDER_STATUS,\n");  
		sql.append("       DECODE(TVD.DELIVERY_STATUS, NULL, 'call', 'delivery') RESOURCE_TYPE,\n");  
		sql.append("       SUM(TVORR.AMOUNT - NVL(TVORR.DELIVERY_AMOUNT, 0)) AMOUNT\n");  
		sql.append("  FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR,\n");  
		//sql.append("       TM_WAREHOUSE                 TW,\n");  
		sql.append("       TM_VHCL_MATERIAL             TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP       TVMG,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R     TVMGR,\n");  
		sql.append("       TT_VS_DLVRY_REQ              TVDR,\n");  
		sql.append("       TT_VS_DLVRY_REQ_DTL          TVDRD,\n");  
		sql.append("       TT_VS_ORDER                  TVO,\n");  
		sql.append("       TM_DEALER                    BTD,\n");  
		sql.append("       TT_VS_DLVRY                  TVD,\n"); 
		sql.append("       "+getWareHouseAreaSql(companyId, areaIds, "TEMP")+"\n");  
		//sql.append("       TM_BUSINESS_AREA             TBA\n");  
		sql.append(" WHERE TVORR.WAREHOUSE_ID = TEMP.WAREHOUSE_ID\n");  
		sql.append("   AND TVORR.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");  
		sql.append("   AND TVORR.REQ_DETAIL_ID = TVDRD.DETAIL_ID\n");  
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");  
		sql.append("   AND TVDR.ORDER_ID = TVO.ORDER_ID\n");  
		sql.append("   AND TVO.BILLING_ORG_ID = BTD.DEALER_ID\n");  
		sql.append("   AND TVDR.REQ_ID = TVD.REQ_ID(+)\n");  
		sql.append("   AND TVORR.RESERVE_STATUS = "+Constant.RESOURCE_RESERVE_STATUS_01+"\n");  
		sql.append("   AND TVORR.RESERVE_TYPE = "+Constant.RESERVE_TYPE_01+"\n");  
		sql.append("   AND TVORR.OEM_COMPANY_ID = "+companyId+"\n");  
		if(warehouseId != null && !warehouseId.equals("")){
			sql.append("   AND TVORR.WAREHOUSE_ID = "+warehouseId+"\n" );
		}
		if(groupCode != null && !groupCode.equals("")){
			String[] array = groupCode.split(",");
			String tempStr = "";
			for (int i = 0; i < array.length; i++) {
				tempStr += "'" + array[i] + "'";
				if (i != array.length - 1) {
					tempStr += ",";
				}
			}
			sql.append("   AND TVMG.GROUP_CODE IN\n");  
			sql.append("       (SELECT G.GROUP_CODE\n");  
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP G\n");  
			sql.append("         START WITH G.GROUP_CODE IN ("+tempStr+")\n");  
			sql.append("        CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID)\n");  
		}
		if(materialCode != null && !materialCode.equals("")){
			String[] array = materialCode.split(",");
			String tempStr = "";
			for (int i = 0; i < array.length; i++) {
				tempStr += "'" + array[i] + "'";
				if (i != array.length - 1) {
					tempStr += ",";
				}
			}
			sql.append("   AND TVM.MATERIAL_CODE IN ("+tempStr+")\n");  
		}
		if(batchNo != null && !batchNo.equals("")){
			sql.append("   AND TVORR.BATCH_NO = "+batchNo+"\n" );
		}
		sql.append(" GROUP BY TEMP.WAREHOUSE_NAME,\n");  
		sql.append("          TVM.MATERIAL_CODE,\n");  
		sql.append("          TVM.MATERIAL_NAME,\n");  
		sql.append("          TVORR.BATCH_NO,\n"); 
		sql.append("          TVDR.REQ_ID,\n"); 
		sql.append("          TVDR.DLVRY_REQ_NO,\n");  
		sql.append("          BTD.DEALER_SHORTNAME,\n");  
		sql.append("          NVL(TVD.DELIVERY_STATUS, TVDR.REQ_STATUS),\n");  
		sql.append("          TVDR.REQ_STATUS,\n");  
		sql.append("          DECODE(TVD.DELIVERY_STATUS, NULL, 'call', 'delivery')\n");  
		sql.append("UNION\n");  
		sql.append("SELECT TEMP.WAREHOUSE_NAME,\n");  
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TVORR.BATCH_NO,\n"); 
		sql.append("       TS.STO_ID ORDER_ID,\n");   
		sql.append("       TS.ORDER_NO,\n");  
		sql.append("       NULL BEALER_NAME,\n");  
		sql.append("       TS.STATUS ORDER_STATUS,\n");  
		sql.append("       'sto' RESOURCE_TYPE,\n");  
		sql.append("       SUM(TVORR.AMOUNT - NVL(TVORR.DELIVERY_AMOUNT, 0)) AMOUNT\n");  
		sql.append("  FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR,\n");  
		//sql.append("       TM_WAREHOUSE                 TW,\n");  
		sql.append("       TM_VHCL_MATERIAL             TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP       TVMG,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R     TVMGR,\n");  
		sql.append("       TT_STO                       TS,\n");  
		sql.append("       TT_STO_DTL                   TSD,\n");  
		sql.append("       "+getWareHouseAreaSql(companyId, areaIds, "TEMP")+"\n");  
		//sql.append("       TM_BUSINESS_AREA             TBA\n");  
		sql.append(" WHERE TVORR.WAREHOUSE_ID = TEMP.WAREHOUSE_ID\n");  
		sql.append("   AND TVORR.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");  
		sql.append("   AND TVORR.REQ_DETAIL_ID = TSD.DTL_ID\n");  
		sql.append("   AND TS.STO_ID = TSD.STO_ID\n");  
		sql.append("   AND TVORR.RESERVE_STATUS = "+Constant.RESOURCE_RESERVE_STATUS_01+"\n");  
		sql.append("   AND TVORR.RESERVE_TYPE = "+Constant.RESERVE_TYPE_02+"\n");  
		sql.append("   AND TVORR.OEM_COMPANY_ID = "+companyId+"\n");  
		if(warehouseId != null && !warehouseId.equals("")){
			sql.append("   AND TVORR.WAREHOUSE_ID = "+warehouseId+"\n" );
		}
		if(groupCode != null && !groupCode.equals("")){
			String[] array = groupCode.split(",");
			String tempStr = "";
			for (int i = 0; i < array.length; i++) {
				tempStr += "'" + array[i] + "'";
				if (i != array.length - 1) {
					tempStr += ",";
				}
			}
			sql.append("   AND TVMG.GROUP_CODE IN\n");  
			sql.append("       (SELECT G.GROUP_CODE\n");  
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP G\n");  
			sql.append("         START WITH G.GROUP_CODE IN ("+tempStr+")\n");  
			sql.append("        CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID)\n");  
		}
		if(materialCode != null && !materialCode.equals("")){
			String[] array = materialCode.split(",");
			String tempStr = "";
			for (int i = 0; i < array.length; i++) {
				tempStr += "'" + array[i] + "'";
				if (i != array.length - 1) {
					tempStr += ",";
				}
			}
			sql.append("   AND TVM.MATERIAL_CODE IN ("+tempStr+")\n");  
		}
		if(batchNo != null && !batchNo.equals("")){
			sql.append("   AND TVORR.BATCH_NO = "+batchNo+"\n" );
		}
		sql.append(" GROUP BY TEMP.WAREHOUSE_NAME,\n");  
		sql.append("          TVM.MATERIAL_CODE,\n");  
		sql.append("          TVM.MATERIAL_NAME,\n");  
		sql.append("          TVORR.BATCH_NO,\n"); 
		sql.append("          TS.STO_ID,\n");   
		sql.append("          TS.ORDER_NO,\n");  
		sql.append("          NULL,\n");  
		sql.append("          TS.STATUS,\n");  
		sql.append("          'sto'\n");  
		sql.append(" ORDER BY MATERIAL_CODE, BATCH_NO");
		
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return ps;
	}
	
	
	public List<Map<String, Object>> getOrderResourceReserveExportList(Map<String, Object> map) {

		String warehouseId = (String) map.get("warehouseId");
		String groupCode = (String) map.get("groupCode");
		String materialCode = (String) map.get("materialCode");
		String batchNo = (String) map.get("batchNo");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		
		StringBuffer sql= new StringBuffer();

		sql.append("SELECT TEMP.WAREHOUSE_NAME,\n");
		sql.append("       TEMP.MATERIAL_CODE,\n");  
		sql.append("       TEMP.MATERIAL_NAME,\n");  
		sql.append("       TEMP.BATCH_NO,\n");  
		sql.append("       TEMP.ORDER_NO,\n");  
		sql.append("       TEMP.BEALER_NAME,\n");  
		sql.append("       TC.CODE_DESC ORDER_STATUS,\n");  
		sql.append("       TEMP.RESOURCE_TYPE,\n");  
		sql.append("       TEMP.AMOUNT\n");  
		sql.append("  FROM (SELECT TEMP.WAREHOUSE_NAME,\n");  
		sql.append("               TVM.MATERIAL_CODE,\n");  
		sql.append("               TVM.MATERIAL_NAME,\n");  
		sql.append("               TVORR.BATCH_NO,\n");  
		sql.append("               TVDR.DLVRY_REQ_NO ORDER_NO,\n");  
		sql.append("               BTD.DEALER_SHORTNAME BEALER_NAME,\n");  
		sql.append("               NVL(TVD.DELIVERY_STATUS, TVDR.REQ_STATUS) ORDER_STATUS,\n");  
		sql.append("               DECODE(TVD.DELIVERY_STATUS, NULL, 'call', 'delivery') RESOURCE_TYPE,\n");  
		sql.append("               SUM(TVORR.AMOUNT - NVL(TVORR.DELIVERY_AMOUNT, 0)) AMOUNT\n");  
		sql.append("          FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR,\n");  
		sql.append("               TM_VHCL_MATERIAL             TVM,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP       TVMG,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP_R     TVMGR,\n");  
		sql.append("               TT_VS_DLVRY_REQ              TVDR,\n");  
		sql.append("               TT_VS_DLVRY_REQ_DTL          TVDRD,\n");  
		sql.append("               TT_VS_ORDER                  TVO,\n");  
		sql.append("               TM_DEALER                    BTD,\n");  
		sql.append("               TT_VS_DLVRY                  TVD,\n");    
		sql.append("               "+getWareHouseAreaSql(companyId, areaIds, "TEMP")+"\n"); 
		sql.append("         WHERE TVORR.WAREHOUSE_ID = TEMP.WAREHOUSE_ID\n");  
		sql.append("           AND TVORR.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("           AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
		sql.append("           AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");  
		sql.append("           AND TVORR.REQ_DETAIL_ID = TVDRD.DETAIL_ID\n");  
		sql.append("           AND TVDR.REQ_ID = TVDRD.REQ_ID\n");  
		sql.append("           AND TVDR.ORDER_ID = TVO.ORDER_ID\n");  
		sql.append("           AND TVO.BILLING_ORG_ID = BTD.DEALER_ID\n");  
		sql.append("           AND TVDR.REQ_ID = TVD.REQ_ID(+)\n");  
		sql.append("           AND TVORR.RESERVE_STATUS = "+Constant.RESOURCE_RESERVE_STATUS_01+"\n");  
		sql.append("           AND TVORR.RESERVE_TYPE = "+Constant.RESERVE_TYPE_01+"\n");  
		sql.append("           AND TVORR.OEM_COMPANY_ID = "+companyId+"\n");  
		if(warehouseId != null && !warehouseId.equals("")){
			sql.append("           AND TVORR.WAREHOUSE_ID = "+warehouseId+"\n" );
		}
		if(groupCode != null && !groupCode.equals("")){
			String[] array = groupCode.split(",");
			String tempStr = "";
			for (int i = 0; i < array.length; i++) {
				tempStr += "'" + array[i] + "'";
				if (i != array.length - 1) {
					tempStr += ",";
				}
			}
			sql.append("           AND TVMG.GROUP_CODE IN\n");  
			sql.append("       		   (SELECT G.GROUP_CODE\n");  
			sql.append("          	      FROM TM_VHCL_MATERIAL_GROUP G\n");  
			sql.append("         	     START WITH G.GROUP_CODE IN ("+tempStr+")\n");  
			sql.append("        	    CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID)\n");  
		}
		if(materialCode != null && !materialCode.equals("")){
			String[] array = materialCode.split(",");
			String tempStr = "";
			for (int i = 0; i < array.length; i++) {
				tempStr += "'" + array[i] + "'";
				if (i != array.length - 1) {
					tempStr += ",";
				}
			}
			sql.append("           AND TVM.MATERIAL_CODE IN ("+tempStr+")\n");  
		}
		if(batchNo != null && !batchNo.equals("")){
			sql.append("           AND TVORR.BATCH_NO = "+batchNo+"\n" );
		}
		sql.append("         GROUP BY TEMP.WAREHOUSE_NAME,\n");  
		sql.append("                  TVM.MATERIAL_CODE,\n");  
		sql.append("                  TVM.MATERIAL_NAME,\n");  
		sql.append("                  TVORR.BATCH_NO,\n");  
		sql.append("                  TVDR.DLVRY_REQ_NO,\n");  
		sql.append("                  BTD.DEALER_SHORTNAME,\n");  
		sql.append("                  TVDR.REQ_STATUS,\n");  
		sql.append("                  NVL(TVD.DELIVERY_STATUS, TVDR.REQ_STATUS),\n");  
		sql.append("                  DECODE(TVD.DELIVERY_STATUS, NULL, 'call', 'delivery')\n");  
		sql.append("        UNION\n");  
		sql.append("        SELECT TEMP.WAREHOUSE_NAME,\n");  
		sql.append("               TVM.MATERIAL_CODE,\n");  
		sql.append("               TVM.MATERIAL_NAME,\n");  
		sql.append("               TVORR.BATCH_NO,\n");  
		sql.append("               TS.ORDER_NO,\n");  
		sql.append("               NULL BEALER_NAME,\n");  
		sql.append("               TS.STATUS ORDER_STATUS,\n");  
		sql.append("               'sto' RESOURCE_TYPE,\n");  
		sql.append("               SUM(TVORR.AMOUNT - NVL(TVORR.DELIVERY_AMOUNT, 0)) AMOUNT\n");  
		sql.append("          FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR,\n");  
		sql.append("               TM_VHCL_MATERIAL             TVM,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP       TVMG,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP_R     TVMGR,\n");  
		sql.append("               TT_STO                       TS,\n");  
		sql.append("               TT_STO_DTL                   TSD,\n");  
		sql.append("               "+getWareHouseAreaSql(companyId, areaIds, "TEMP")+"\n"); 
		sql.append("         WHERE TVORR.WAREHOUSE_ID = TEMP.WAREHOUSE_ID\n");  
		sql.append("           AND TVORR.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("           AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
		sql.append("           AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");  
		sql.append("           AND TVORR.REQ_DETAIL_ID = TSD.DTL_ID\n");  
		sql.append("           AND TS.STO_ID = TSD.STO_ID\n");  
		sql.append("           AND TVORR.RESERVE_STATUS = "+Constant.RESOURCE_RESERVE_STATUS_01+"\n");  
		sql.append("           AND TVORR.RESERVE_TYPE = "+Constant.RESERVE_TYPE_02+"\n");  
		sql.append("           AND TVORR.OEM_COMPANY_ID = "+companyId+"\n");  
		if(warehouseId != null && !warehouseId.equals("")){
			sql.append("           AND TVORR.WAREHOUSE_ID = "+warehouseId+"\n" );
		}
		if(groupCode != null && !groupCode.equals("")){
			String[] array = groupCode.split(",");
			String tempStr = "";
			for (int i = 0; i < array.length; i++) {
				tempStr += "'" + array[i] + "'";
				if (i != array.length - 1) {
					tempStr += ",";
				}
			}
			sql.append("           AND TVMG.GROUP_CODE IN\n");  
			sql.append("       		   (SELECT G.GROUP_CODE\n");  
			sql.append("          	      FROM TM_VHCL_MATERIAL_GROUP G\n");  
			sql.append("         	     START WITH G.GROUP_CODE IN ("+tempStr+")\n");  
			sql.append("        	    CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID)\n");  
		}
		if(materialCode != null && !materialCode.equals("")){
			String[] array = materialCode.split(",");
			String tempStr = "";
			for (int i = 0; i < array.length; i++) {
				tempStr += "'" + array[i] + "'";
				if (i != array.length - 1) {
					tempStr += ",";
				}
			}
			sql.append("           AND TVM.MATERIAL_CODE IN ("+tempStr+")\n");  
		}
		if(batchNo != null && !batchNo.equals("")){
			sql.append("           AND TVORR.BATCH_NO = "+batchNo+"\n" );
		}
		sql.append("         GROUP BY TEMP.WAREHOUSE_NAME,\n");  
		sql.append("                  TVM.MATERIAL_CODE,\n");  
		sql.append("                  TVM.MATERIAL_NAME,\n");  
		sql.append("                  TVORR.BATCH_NO,\n");  
		sql.append("                  TS.ORDER_NO,\n");  
		sql.append("                  NULL,\n");  
		sql.append("                  TS.STATUS,\n");  
		sql.append("                  'sto'\n");  
		sql.append(") TEMP,\n");  
		sql.append("       TC_CODE TC\n");  
		sql.append(" WHERE TEMP.ORDER_STATUS = TC.CODE_ID ORDER BY TEMP.MATERIAL_CODE, TEMP.BATCH_NO");


		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public boolean checkQuate(String orgId) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select distinct vod.root_org_id\n");
		sql.append("  from tm_warehouse tw, vw_org_dealer vod\n");  
		sql.append(" where tw.dealer_id = vod.dealer_id\n");  
		sql.append("   and tw.dealer_id is not null\n");  
		sql.append("   and vod.root_org_id = ?\n");
		params.add(orgId) ;
		
		Map<String, Object> orgMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;
		
		if(!CommonUtils.isNullMap(orgMap)) {
			return true ;
		} else {
			return false ;
		}
	}
	
	public List<Map<String, Object>> getVhcl(String stoId) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tmv.vin, tvm.material_code, tvm.material_name, tmv.batch_no\n");
		sql.append("  from tt_sto              ts,\n");  
		sql.append("       tt_vs_dlvry_erp     tvde,\n");  
		sql.append("       tt_vs_dlvry_erp_dtl tvded,\n");  
		sql.append("       tm_vehicle          tmv,\n");  
		sql.append("       tm_vhcl_material    tvm\n");  
		sql.append(" where ts.sto_id = tvde.delivery_id\n");  
		sql.append("   and tvde.sendcar_header_id = tvded.sendcar_header_id\n");  
		sql.append("   and tvded.vin = tmv.vin\n");  
		sql.append("   and tmv.material_id = tvm.material_id\n");  
		sql.append("   and tmv.warehouse_id = ts.from_warehouse_id\n");  
		sql.append("   and ts.sto_id = ?\n");
		params.add(stoId) ;
		
		List<Map<String, Object>> vhclList = dao.pageQuery(sql.toString(), params, dao.getFunName()) ;
		
		return vhclList ;
	}
}
