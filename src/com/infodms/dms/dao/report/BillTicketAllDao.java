package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import bsh.UtilTargetError;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

public class BillTicketAllDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(BillTicketAllDao.class);
	public static final BillTicketAllDao dao = new BillTicketAllDao();
	public static BillTicketAllDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public List<Map<String, Object>> getBillTicketAllSelect(Map<String, String> map){
		List<Map<String, Object>> list =null;
		StringBuffer sql = new StringBuffer();
		String modelId = map.get("modelId");
		String orgId = map.get("orgId");
		String mydealerId = map.get("mydealerId");
		String dealerId = map.get("dealerId");
		String beginTime = map.get("beginTime");
		String endTime = map.get("endTime");
		String areaId = map.get("areaId");
		sql.append("select DISTINCT A.*\n");
		sql.append("from (SELECT VTS.ORG_NAME, --大区\n");
		sql.append(" VTS.ROOT_DEALER_NAME,\n");
		sql.append("VTS.REGION_NAME,\n");
		
		sql.append("VTS.DEALER_NAME,\n");
		sql.append("VTS.SERIES_ID,\n");
		sql.append("VTS.SERIES_NAME,\n");
		sql.append("SUM(DECODE(VTS.DATA_TYPE, 'BILL', VTS.AMOUNT, 0)) BILLAMOUNT,\n");
		sql.append("SUM(DECODE(VTS.DATA_TYPE, 'ACT', VTS.AMOUNT, 0)) ACTAMOUNT\n");
		sql.append("FROM day_calculate_report VTS WHERE 1 = 1\n");
		if(Utility.testString(modelId)){
			sql.append("AND VTS.model_id IN (" + modelId + ")\n");
		}
		if(Utility.testString(orgId)){
			sql.append("AND VTS.ORG_ID IN (" + orgId + ")\n");
		}
		if(Utility.testString(mydealerId)){
			sql.append("AND VTS.dealer_id IN (" + mydealerId + ")\n");
		}
		if(Utility.testString(dealerId)){
			sql.append(" AND VTS.root_dealer_id IN (" + dealerId + ")\n");
		}
		if(Utility.testString(beginTime)){
			sql.append("AND TO_DATE(VTS.DDATE,'yyyy-mm-dd') >= TO_DATE('" +
                beginTime + "','yyyy-mm-dd')\n");
		}
		if(Utility.testString(endTime)){
			sql.append("AND (TO_DATE('" + endTime +
                "','yyyy-mm-dd'))>= TO_DATE(VTS.DDATE,'yyyy-mm-dd')\n");
		}
		sql.append("  GROUP BY VTS.ORG_NAME,\n");
		sql.append("VTS.ROOT_DEALER_NAME,\n");
		sql.append("VTS.SERIES_NAME,\n");
		sql.append("VTS.REGION_NAME,\n");
		sql.append(" VTS.DEALER_NAME,\n");
		sql.append("SERIES_ID) A,\n");
		sql.append("TM_AREA_GROUP TAG,\n");
		sql.append(" TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append("where A.SERIES_ID = TVMG.GROUP_ID\n");
		sql.append(" AND TAG.MATERIAL_GROUP_ID = TVMG.GROUP_ID\n");
		if(Utility.testString(areaId)){
			sql.append("AND TAG.AREA_ID= '" + areaId + "'\n");
		}
		sql.append("order by ORG_NAME,REGION_NAME,ROOT_DEALER_NAME,DEALER_NAME,SERIES_NAME\n");
		list = super.pageQuery(sql.toString(),null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getList(Map<String, String> map){
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		String areaId = map.get("areaId");
		
		
		sql.append("select /*+ all_rows */ distinct vmg.SERIES_NAME, vmg.SERIES_CODE, vmg.SERIES_ID\n");
		sql.append("  from vw_material_group vmg, tm_area_group tag\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and (vmg.PACKAGE_ID = tag.material_group_id or\n");  
		sql.append("       vmg.MODEL_ID = tag.material_group_id or\n");  
		sql.append("       vmg.SERIES_ID = tag.material_group_id)\n");  
		if(Utility.testString(areaId)){
			sql.append("   and tag.area_id in ("+areaId+" )\n");
		}
		
		//sql.append("group by vmg.SERIES_NAME\n");
		
		list = super.pageQuery(sql.toString(),null, getFunName());
		
		return list;
	}
	
	
	public List<Map<String, Object>> getDetailList(Map<String, String> map,List<Map<String, Object>> list_series_name){
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		String orgId = map.get("orgId");
		String modelId = map.get("modelId");
		String mydealerId = map.get("mydealerId");
		String dealerId = map.get("dealerId");
		String beginTime = map.get("beginTime");
		String endTime = map.get("endTime");
		String areaId = map.get("areaId");
		
		sql.append("select /*+ all_rows */ dcr.org_name,\n");
		sql.append("       dcr.root_dealer_name,dcr.region_name,\n");  
		
		
		if(list_series_name!=null&&list_series_name.size()!=0){
			for (int i = 0; i < list_series_name.size(); i++) {
					sql.append("SUM(DECODE(dcr.SERIES_NAME, '"+list_series_name.get(i).get("SERIES_NAME")+"', dcr.AMOUNT, 0)) BILLAMOUNT"+i+",\n");
			}
		} 
		sql.append("       dcr.dealer_name\n");  
		sql.append("  from day_calculate_report dcr\n");  
		sql.append("  where dcr.data_type = 'BILL'\n");  

		if(!CommonUtils.isNullString(orgId)) {
			sql.append("  AND DCR.ORG_ID IN (").append(orgId).append(")\n");  
		}
		if(!CommonUtils.isNullString(mydealerId)){
			sql.append("  AND DCR.DEALER_ID IN(").append(mydealerId).append(")\n");  
		}
		if(!CommonUtils.isNullString(dealerId)){
			sql.append("  AND DCR.ROOT_DEALER_ID IN(").append(dealerId).append(")\n");    
		}
		if(!CommonUtils.isNullString(beginTime)){
			sql.append("  AND DCR.DDATE >= '"+beginTime+"'\n");  
		}
		if(!CommonUtils.isNullString(endTime)){
			sql.append("  AND DCR.DDATE <= '"+endTime+"'\n");  
			
		}
		if(!CommonUtils.isNullString(areaId)){
			sql.append("  AND DCR.AREA_ID IN(").append(areaId).append(")\n");  
		}
		
		if(!CommonUtils.isNullString(modelId)){
			sql.append("  AND DCR.model_Id IN(").append(modelId).append(")\n");  
		}
		
		sql.append(" group by dcr.org_name, dcr.region_name,dcr.root_dealer_name, dcr.dealer_name\n");
		sql.append("order by dcr.org_name, dcr.region_name,dcr.root_dealer_name, dcr.dealer_name\n");
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list ;
	}
}
