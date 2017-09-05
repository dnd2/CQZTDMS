package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

public class BillTicketAllSDao extends BaseDao{

	public static final Logger logger = Logger.getLogger(BillTicketAllSDao.class);
	public static final  BillTicketAllSDao dao = new BillTicketAllSDao();
	public static BillTicketAllSDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, Object>> getDetailList(Map<String, String> map,List<Map<String, Object>> list_series_name){
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		String dealerId = map.get("dealerId");
		String beginTime = map.get("beginTime");
		String endTime = map.get("endTime");
		String areaId = map.get("areaId");
		String campaignModel = map.get("campaignModel");
		
		sql.append("select dcr.org_name,\n");
		sql.append("       dcr.root_dealer_name,dcr.region_name,\n");  
		
		
		if(list_series_name!=null&&list_series_name.size()!=0){
			for (int i = 0; i < list_series_name.size(); i++) {
					sql.append("SUM(DECODE(dcr.SERIES_NAME, '"+list_series_name.get(i).get("SERIES_NAME")+"', dcr.AMOUNT, 0)) BILLAMOUNT"+i+",\n");
			}
		} 
		sql.append("       dcr.dealer_name\n");  
		sql.append("  from day_calculate_report dcr\n");  
		sql.append("  where dcr.data_type = 'BILL'\n");  

		if(!CommonUtils.isNullString(dealerId)){
			sql.append("  AND (DCR.ROOT_DEALER_ID IN(").append(dealerId).append(")\n");    
		}
		if(!CommonUtils.isNullString(dealerId)){
			sql.append("  OR DCR.DEALER_ID IN(").append(dealerId).append("))\n");    
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
		if(!CommonUtils.isNullString(campaignModel)){
			String[] models = campaignModel.split(",");
			sql.append("AND DCR.MODEL_CODE in (");
			for(int i=0; i<models.length; i++) {
				if(i != models.length-1) {
					sql.append("'");
					sql.append(models[i]);
					sql.append("',");
				}else {	
					sql.append("'");
					sql.append(models[i]);
					sql.append("'");
				}
			}
			sql.append(")");
		}
		
		
		sql.append(" group by dcr.org_name, dcr.region_name,dcr.root_dealer_name, dcr.dealer_name\n");
		sql.append("order by dcr.org_name, dcr.region_name,dcr.root_dealer_name, dcr.dealer_name\n");
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list ;
	}
	
	public List<Map<String, Object>> getList(Map<String, String> map){
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		String areaId = map.get("areaId");
		String campaignModel = map.get("campaignModel");
		
		sql.append("select distinct vmg.SERIES_NAME, vmg.SERIES_CODE, vmg.SERIES_ID\n");
		sql.append("  from vw_material_group vmg, tm_area_group tag\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and (vmg.PACKAGE_ID = tag.material_group_id or\n");  
		sql.append("       vmg.MODEL_ID = tag.material_group_id or\n");  
		sql.append("       vmg.SERIES_ID = tag.material_group_id)\n");  
		if(Utility.testString(areaId)){
			sql.append("   and tag.area_id = "+areaId+"\n");
		}
		if(!CommonUtils.isNullString(campaignModel)){
			String[] models = campaignModel.split(",");
			sql.append("AND vmg.MODEL_CODE in (");
			for(int i=0; i<models.length; i++) {
				if(i != models.length-1) {
					sql.append("'");
					sql.append(models[i]);
					sql.append("',");
				}else {	
					sql.append("'");
					sql.append(models[i]);
					sql.append("'");
				}
			}
			sql.append(")");
		}
		
		list = super.pageQuery(sql.toString(),null, getFunName());
		
		return list;
	}
}
