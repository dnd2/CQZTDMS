package com.infodms.dms.dao.sales.storage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

/**
 * 
 * @ClassName     : ComboBoxUitl 
 * @Description   : 下拉框公共类
 * @author        : ranjian
 * CreateDate     : 20130524
 */
public class ComboBoxUitl extends BaseDao<PO>{
	private static final ComboBoxUitl dao = new ComboBoxUitl ();
	public static final ComboBoxUitl getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function         : 根据产地ID获取库区
	 * @param           : 库区
	 */
	public  List<Map<String, Object>> getYielalyIdByArea(Long yielalyId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sbSql= new StringBuffer();	
		sbSql.append("SELECT TSA.AREA_ID,\n");
		sbSql.append("       TSA.AREA_CODE,\n");
		sbSql.append("       TSA.AREA_NAME,\n");
		sbSql.append("       TSA.TYPE,\n");
		sbSql.append("       TSA.IN_STATUS,\n");
		sbSql.append("       TSA.OUT_STATUS\n");
		sbSql.append("  FROM TT_SALES_AREA TSA\n");
		sbSql.append(" WHERE TSA.YIELDLY = ?\n");
		sbSql.append("   AND TSA.STATUS = ?"); 
		List<Object> params=new ArrayList<Object>();
		params.add(yielalyId);
		params.add(Constant.STATUS_ENABLE);
		list=dao.pageQuery(sbSql.toString(), params, dao.getFunName());
		return list;
	}
	/**
	 * Function         : 根据库区ID获取库道
	 * @param           : areaId 库区ID
	 * @param           : proStatus 处理状态（入口处，seach:查询,other: 其他）
	 */
	public  List<Map<String, Object>> getAreaIdByRoad(Long areaId,String proStatus)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sbSql= new StringBuffer();	
		sbSql.append("SELECT TSR.ROAD_ID, TSR.ROAD_NAME, TSR.IN_STATUS, TSR.OUT_STATUS\n");
		sbSql.append("  FROM TT_SALES_ROAD TSR\n");
		sbSql.append(" WHERE TSR.AREA_ID = ?\n");
		sbSql.append("   AND TSR.STATUS = ?"); 
		sbSql.append("   ORDER BY TO_NUMBER(TSR.ROAD_NAME)"); 
		List<Object> params=new ArrayList<Object>();
		params.add(areaId);
		params.add(Constant.STATUS_ENABLE);
		list=dao.pageQuery(sbSql.toString(), params, dao.getFunName());
		return list;
	}
	/**
	 * Function         : 根据库道ID获取位
	 * @param           : roadId 库道ID
	 * @param           : proStatus 处理状态（入口处，seach:查询,other: 其他）
	 */
	public  List<Map<String, Object>> getRoadIdBySit(Long roadId,String proStatus)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sbSql= new StringBuffer();	
		sbSql.append("SELECT TSS.SIT_ID, TSS.SIT_NAME, TSS.VEHICLE_ID\n");
		sbSql.append("  FROM TT_SALES_SIT TSS\n");
		sbSql.append(" WHERE TSS.ROAD_ID = ?\n");
		sbSql.append("   AND TSS.STATUS = ?"); 
		if(proStatus.equals("other")){
			sbSql.append("  AND TSS.VEHICLE_ID=?"); 
		}
		List<Object> params=new ArrayList<Object>();
		params.add(roadId);
		params.add(Constant.STATUS_ENABLE);
		if(proStatus.equals("other")){
			params.add(Constant.DEFAULT_VALUE);
		}
		sbSql.append("   ORDER BY TO_NUMBER(TSS.SIT_NAME)"); 
		list=dao.pageQuery(sbSql.toString(), params, dao.getFunName());
		return list;
	}
	/**
	 * Function         : 根据库位ID获取库，道，位信息
	 * @param           : sitId 库位ID
	 */
	public  Map<String, Object> getAllBySitId(Long sitId)
	{
		StringBuffer sbSql= new StringBuffer();	
		sbSql.append("SELECT TSA.YIELDLY,\n");
		sbSql.append("         TSA.AREA_ID,\n");
		sbSql.append("         TSA.AREA_NAME,\n");
		sbSql.append("         TSR.ROAD_ID,\n");
		sbSql.append("         TSR.ROAD_NAME,\n");
		sbSql.append("         TSS.SIT_ID,\n");
		sbSql.append("         TSS.SIT_NAME,\n");
		sbSql.append("         TSS.VEHICLE_ID\n");
		sbSql.append("    FROM TT_SALES_AREA TSA, TT_SALES_ROAD TSR, TT_SALES_SIT TSS\n");
		sbSql.append("   WHERE TSA.AREA_ID = TSR.AREA_ID\n");
		sbSql.append("     AND TSR.ROAD_ID = TSS.ROAD_ID\n");
		sbSql.append("   AND TSS.SIT_ID = ?");  
		List<Object> params=new ArrayList<Object>();
		params.add(sitId);
		Map<String, Object> map=dao.pageQueryMap(sbSql.toString(), params, dao.getFunName());
		return map;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据道库区ID获取库道最大库位数
	 * @param      : @param areaId库区ID
	 * @param      : @return     
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-14
	 */
	public Map<String, Object> getMaxByAreaId(Long areaId)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT NVL(MAX(COUNT(1)),0) MAX_ROAD\n");
		sbSql.append("  FROM TT_SALES_ROAD TSR, TT_SALES_SIT TSS\n");
		sbSql.append(" WHERE TSR.ROAD_ID = TSS.ROAD_ID\n");
		sbSql.append("   AND TSR.STATUS = ?\n");
		sbSql.append("   AND TSS.STATUS = ?\n");
		sbSql.append("   AND TSR.AREA_ID = ?\n");
		sbSql.append(" GROUP BY TSR.ROAD_ID"); 
		List<Object> params =new ArrayList<Object>();
		params.add(Constant.STATUS_ENABLE);
		params.add(Constant.STATUS_ENABLE);
		params.add(areaId);
		Map<String, Object> map= dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return map;
	}
}
