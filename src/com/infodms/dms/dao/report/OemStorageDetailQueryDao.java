package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OemStorageDetailQueryDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(BillDetailTicketDao.class);
	public static OemStorageDetailQueryDao dao = new OemStorageDetailQueryDao();
	public static OemStorageDetailQueryDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
public PageResult<Map<String, Object>> getOemStorageDetailInfo(Map<String, Object> map,Integer pageSize,Integer curPage){
	String areaId = (String)map.get("areaId");
	String series = (String)map.get("series");
	String vin = (String)map.get("vin");
	String storageAge = (String)map.get("storageAge");
	String vKind = (String)map.get("vKind");
	StringBuffer sbSql = new StringBuffer();
	sbSql.append("SELECT MAT.SERIES_NAME,\n");
	sbSql.append("       MAT.MODEL_NAME,\n");
	sbSql.append("       MAT.PACKAGE_NAME,\n");
	sbSql.append("       TV.VIN,\n");
	sbSql.append("       TO_CHAR(TV.PRODUCT_DATE,'yyyy-MM-dd') PRODUCT_DATE,\n");
	sbSql.append("       TO_CHAR(TV.ORG_STORAGE_DATE,'yyyy-MM-dd') STORAGE_DATE,\n");
	sbSql.append("       TV.ORG_STORAGE_DATE,\n");
	sbSql.append("       CASE WHEN TV.ORG_STORAGE_DATE > ADD_MONTHS(SYSDATE,-3) THEN '3个月内'\n");
	sbSql.append("       WHEN  TV.ORG_STORAGE_DATE < ADD_MONTHS(SYSDATE,-6) THEN '6个月以上'\n");
	sbSql.append("       ELSE '3个月到6个月' END STORAGE_AGE,\n");
	sbSql.append("       TBA.AREA_NAME,\n");
	sbSql.append("       CASE TBA.AREA_NAME WHEN '合肥' THEN '商用车'\n");
	sbSql.append("                          ELSE '乘用车' END VEHICLE_KIND\n");
	sbSql.append("  FROM TM_VEHICLE TV, VW_MATERIAL_GROUP_MAT MAT , TM_BUSINESS_AREA TBA\n");
	sbSql.append(" WHERE TV.MATERIAL_ID = MAT.MATERIAL_ID\n");
	sbSql.append("   AND TV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_02+" and TV.LOCK_STATUS<>10241008\n"); 
	sbSql.append("   AND TV.YIELDLY = TBA.AREA_ID");  
	List par=new ArrayList();
	if(areaId != null && !"".equals(areaId)){
		sbSql.append("   AND TV.YIELDLY = ?\n");
		par.add(areaId);
	}
	if(vin != null && !"".equals(vin)){
		sbSql.append("   AND TV.VIN LIKE ?\n");
		par.add("%"+vin+"%");
	}
	if(series != null && !"".equals(series)){
		sbSql.append("   AND MAT.SERIES_ID = ?\n");
		par.add(series);
	}
	if(!"".equals(storageAge)){
		if("1".equals(storageAge)){
			sbSql.append("   AND TV.ORG_STORAGE_DATE > ADD_MONTHS(SYSDATE,-3)\n");
		}
		if("2".equals(storageAge)){
			sbSql.append("   AND TV.ORG_STORAGE_DATE >= ADD_MONTHS(SYSDATE,-6) AND TV.ORG_STORAGE_DATE <= ADD_MONTHS(SYSDATE,-3)\n");
		}
		if("3".equals(storageAge)){
			sbSql.append("   AND TV.ORG_STORAGE_DATE < ADD_MONTHS(SYSDATE,-6)\n");
		}
	}
	return pageQuery(sbSql.toString(), par,getFunName(), pageSize, curPage);
}
}
