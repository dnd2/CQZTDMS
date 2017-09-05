package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : TransferStockDao 
 * @Description   : 换下车入库调整DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-28
 */
public class TransferStockDao extends BaseDao<PO>{
	private static final TransferStockDao dao = new TransferStockDao ();
	public static final TransferStockDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 *  换下车入库查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getReceivingStorageQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		
		String groupCode = (String)map.get("groupCode"); // 物料组
		String materialCode = (String)map.get("materialCode"); // 物料
		String offlineStartDate = (String)map.get("offlineStartDate"); // 下线日期开始
		String offlineEndDate = (String)map.get("offlineEndDate"); // 下线日期结束
		String orgStartDate = (String)map.get("orgStartDate"); // 入库日期开始
		String orgEndDate = (String)map.get("orgEndDate"); // 入库日期结束
		String vin = (String)map.get("vin"); // vin
		String areaIds = (String)map.get("areaIds"); // 产地
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TV.VEHICLE_ID,\r\n");
		sql.append("       VMG.MODEL_NAME,\r\n");
		sql.append("       VMG.PACKAGE_NAME,\r\n");
		sql.append("       TVM.MATERIAL_CODE,\r\n");
		sql.append("       TVM.MATERIAL_NAME,\r\n");
		sql.append("       TV.VIN,\r\n");
		sql.append("       TV.ENGINE_NO,\r\n");
		sql.append("       TO_CHAR(TV.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\r\n");
		sql.append("       TO_CHAR(TV.OFFLINE_DATE, 'YYYY-MM-DD') OFFLINE_DATE,\r\n");
		sql.append("       TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD HH24:MI:SS') ORG_STORAGE_DATE,\r\n");
		sql.append("       TV.LIFE_CYCLE\r\n");
		sql.append("  FROM TM_VEHICLE               TV,\r\n");
		sql.append("       VW_MATERIAL_GROUP        VMG,\r\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\r\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\r\n");
		sql.append("       TM_BUSINESS_AREA         TBA\r\n");
		sql.append(" WHERE TV.MATERIAL_ID = TVM.MATERIAL_ID\r\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\r\n");
		sql.append("   AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\r\n");
		sql.append("   AND TV.YIELDLY = TBA.AREA_ID\r\n"); 
		sql.append("   AND TV.LIFE_CYCLE =").append(Constant.VEHICLE_LIFE_08).append("\r\n"); 
		/******已出库的车辆需要再次入库*****/
		sql.append("AND EXISTS(SELECT 1 FROM TT_SALES_CHA_HIS TSCH " +
				"WHERE TSCH.OLD_VEHICLE_ID=TV.VEHICLE_ID AND TSCH.IS_RETURN="+Constant.IF_TYPE_NO+")\n");
		if(groupCode!=null&&!"".equals(groupCode)){//物料组过滤
			sql.append(MaterialGroupManagerDao.getMaterialGroupQuerySql("VMG.PACKAGE_ID", groupCode));
		}
		if(materialCode!=null&&!"".equals(materialCode)){//物料过滤
			sql.append(MaterialGroupManagerDao.getMaterialQuerySql("TVM.MATERIAL_ID", materialCode));
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
		if(vin!=null&&!"".equals(vin)){//vin过滤
			sql.append(GetVinUtil.getVins(vin, "TV"));
		//	sql.append("   AND TV.VIN LIKE '%"+vin.trim()+"%'\n" );
		}
		sql.append("AND TV.YIELDLY in(").append(areaIds==""?Constant.DEFAULT_VALUE:areaIds).append(")\n");//权限控制

		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}

	/**
	 * 
	 * @Title      : 
	 * @Description: 根车辆ID获取车辆信息
	 * @param      : @param params 查询参数MAP
	 * @param      : @return     满足条件的车辆信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-15
	 */
	public Map<String, Object> getVechileByVId(List<Object> params){
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT TV.VEHICLE_ID,\n");
		sbSql.append("       TBA.AREA_NAME YIELDLY,\n");
		sbSql.append("        TV.PLAN_DETAIL_ID,\n");
		sbSql.append("       VMGM.MODEL_CODE,\n");
		sbSql.append("       VMGM.MODEL_NAME,\n");
		sbSql.append("       VMGM.PACKAGE_CODE,\n");
		sbSql.append("       VMGM.PACKAGE_NAME,\n");
		sbSql.append("       VMGM.MATERIAL_CODE,\n");
		sbSql.append("       VMGM.MATERIAL_NAME,\n");
		sbSql.append("       TV.VIN, --VIN\n");
		sbSql.append("       TV.ENGINE_NO,\n");
		sbSql.append("       TO_CHAR(TV.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\n");
		sbSql.append("       TO_CHAR(TV.OFFLINE_DATE, 'YYYY-MM-DD') OFFLINE_DATE,\n");
		sbSql.append("       TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD') ORG_STORAGE_DATE,\n");
		sbSql.append("       TV.PER_ID,\n");
		sbSql.append("       TSAP.PER_NAME\n");
		sbSql.append("  FROM TM_VEHICLE            TV,\n");
		sbSql.append("       TM_BUSINESS_AREA      TBA,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMGM,\n");
		sbSql.append("       TT_SALES_ACCAR_PER    TSAP\n");
		sbSql.append(" WHERE TV.YIELDLY = TBA.AREA_ID\n");
		sbSql.append("   AND TV.MATERIAL_ID = VMGM.MATERIAL_ID\n");
		sbSql.append("   AND TV.PER_ID = TSAP.PER_ID(+)\n");
		sbSql.append("AND TV.VEHICLE_ID=?"); 
        Map<String, Object> map= dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return map;
	}
}
