package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class VehicleLocationChangeDAO extends BaseDao{
	public static Logger logger = Logger.getLogger(VehicleLocationChangeDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO ();
	public static final CheckVehicleDAO getInstance() {
		return dao;
	}
	public static List<Map<String, Object>> warehouseQuery(String dealerIds,String warehouseType) {
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT TDW.WAREHOUSE_NAME, TDW.WAREHOUSE_ID, TDW.WAREHOUSE_TYPE,TDW.DEALER_ID,tdw.MANAGE_DEALER_ID \n");
		sql.append("  FROM TM_DEALER_WAREHOUSE TDW\n");  
		sql.append(" WHERE 1=1 AND TDW.STATUS = " + Constant.STATUS_ENABLE + "\n"); 
		if(dealerIds != null && !"".equals(dealerIds)) {
			sql.append("         and TDW.DEALER_ID IN (" + dealerIds.trim() + ")\n");
		}
		if(warehouseType!=null && !"".equals(warehouseType)) {
			sql.append("         and TDW.WAREHOUSE_TYPE = " + warehouseType + "\n");
		}
		sql.append("         ORDER BY TDW.WAREHOUSE_NAME,TDW.WAREHOUSE_ID\n");

		
		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		
		return rs ;
	}
	/***
	 *车辆位置变更：查询展示
	 * @param : 
	 * @return
	 * @throws Exception
	 */
	public static PageResult <Map<String,Object>> vehicleLocationChangeQuery(String dealer_Id,String materialCode,String materialCode__, String days, String vin , int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		List<Object> params = new LinkedList<Object>();
		sql.append("SELECT TMV.VEHICLE_ID,\n");  
		sql.append("       G.GROUP_NAME AS MODEL_NAME, --车型\n");  
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");  
		sql.append("       TMVM.MATERIAL_CODE, --物料代码\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n");  
		sql.append("       TMV.VIN, --VIN\n");  
		sql.append("       TMV.AREA_ID,\n"); 
		sql.append("       TBA.AREA_NAME,\n"); 
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");  
		sql.append("       TMD.DEALER_SHORTNAME, --经销商\n");  
		sql.append("       TO_CHAR(TMV.STORAGE_DATE,'yyyy-MM-dd') AS STORAGE_DATE, --入库日期\n");  
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS STORAGE_DAY\n");  
		sql.append("  FROM TM_VEHICLE             TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");  
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");  
		sql.append("       TM_DEALER              TMD,\n"); 
		sql.append("       TM_BUSINESS_AREA              TBA,\n"); 
		sql.append("	   TM_DEALER_WAREHOUSE    TDW\n");
		sql.append(" WHERE TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_03+"\n");  
		sql.append("   AND TMV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");  
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");  
		sql.append("   AND TMV.AREA_ID = TBA.AREA_ID(+)\n");  
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");  
		sql.append("   AND TMV.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND to_number(TMV.vehicle_area) = TDW.WAREHOUSE_ID(+)\n");
		sql.append("   AND TMV.DEALER_ID IN ("+dealer_Id+")\n");
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length>0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length()-1);
				System.out.println(buffer);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
				sql.append("       OR  SUBSTR(G.TREE_CODE, 0, 3) IN\n");
				sql.append("       (SELECT G.TREE_CODE\n");  
				sql.append("           FROM TM_VHCL_MATERIAL_GROUP G\n");  
				sql.append("          WHERE G.GROUP_CODE IN ("+buffer.toString()+")))\n");
			}
			
		}
		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin,"TMV")); 
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>="+days.trim()+")\n");
		}
		sql.append("ORDER BY TMV.STORAGE_DATE DESC, TMV.AREA_ID, G1.GROUP_NAME \n");

		return dao.pageQuery(sql.toString(), params,"com.infodms.dms.dao.sales.storageManage.VehicleLocationChangeDAO.vehicleLocationChangeQuery",pageSize ,curPage );
	}
	
	/**
	 * 通过车辆所有者的id查询和仓库类型查询仓库信息
	 * @param vhclId 车辆id
	 * @param wareType 仓库类型
	 * @return List：满足条件的仓库列表
	 */
    public static List<Map<String, Object>> getWareNew(String vhclId, String wareType) {
    	StringBuffer sql = new StringBuffer("\n") ;
    	
    	sql.append("SELECT TDW.WAREHOUSE_ID, TDW.WAREHOUSE_NAME \n");
    	sql.append("  FROM TM_DEALER_WAREHOUSE TDW, TM_VEHICLE TV\n");  
    	sql.append(" WHERE 1 = 1 AND TDW.IS_INTERFACE!=1\n");  
    	sql.append("   AND TDW.DEALER_ID = TV.DEALER_ID\n");  
    	sql.append("   AND TDW.WAREHOUSE_TYPE = ").append(wareType).append(" \n");  
    	sql.append("   AND STATUS = ").append(Constant.STATUS_ENABLE).append(" \n");  
    	sql.append("   AND TV.VEHICLE_ID = ").append(vhclId).append(" \n");
    	
    	return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
    }
    
    /**
     * 通过传入的两个经销商id判断仓库类型，并查出对应仓库列表。
     * @param dealerId 仓库所有者id
     * @param manageDlr 代管经销商id
     * @return List：仓库列表
     */
    public static List<Map<String, Object>> getDLRWare(String dealerId, String manageDlr) {
    	StringBuffer sql = new StringBuffer("\n") ;
    	
    	sql.append("SELECT TDW.WAREHOUSE_ID, TDW.WAREHOUSE_NAME, TDW.WAREHOUSE_TYPE \n");
    	sql.append("  FROM TM_DEALER_WAREHOUSE TDW\n");  
    	sql.append(" WHERE 1 = 1\n");  
    	sql.append("   AND STATUS = ").append(Constant.STATUS_ENABLE).append(" \n");
    	
    	if (!dealerId.equals(manageDlr)) {
    		sql.append("   AND TDW.DEALER_ID IN (").append(dealerId).append(") \n");  
    		sql.append("   AND TDW.MANAGE_DEALER_ID IN (").append(manageDlr).append(") \n");  
    		sql.append("   AND TDW.WAREHOUSE_TYPE = ").append(Constant.DEALER_WAREHOUSE_TYPE_02).append(" \n");  
    	} else {
    		sql.append("   AND TDW.DEALER_ID IN (").append(dealerId).append(") \n");
    		sql.append("   AND TDW.WAREHOUSE_TYPE = ").append(Constant.DEALER_WAREHOUSE_TYPE_01).append(" \n");  
    	}
    	
    	
    	return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
    }
	
    public static List<Map<String, Object>> getDLRWare(String dealerId) {
    	StringBuffer sql = new StringBuffer("\n") ;
    	
    	sql.append("SELECT TDW.WAREHOUSE_ID, TDW.WAREHOUSE_NAME, TDW.WAREHOUSE_TYPE \n");
    	sql.append("  FROM TM_DEALER_WAREHOUSE TDW\n");  
    	sql.append(" WHERE 1 = 1\n");  
    	sql.append("   AND STATUS = ").append(Constant.STATUS_ENABLE).append(" \n");
    	sql.append("   AND TDW.DEALER_ID IN (").append(dealerId).append(") \n");
		sql.append("   AND TDW.WAREHOUSE_TYPE = ").append(Constant.DEALER_WAREHOUSE_TYPE_01).append(" \n");  
    	
    	
    	
    	return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
    }
    
    
    
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
