package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class ActDetailSalesDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(BillDetailTicketDao.class);
	public static ActDetailSalesDao dao = new ActDetailSalesDao();
	public static ActDetailSalesDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
public List<Map<String, Object>> getActDetailSalesSelect(Map<String, String> map ){
	String modelId = map.get("modelId");
	String modelId4 = map.get("modelId4");
	String orgId = map.get("orgId");
	String myOrgId = map.get("myOrgId");
	String dealerId = map.get("dealerId");
	String dealerId2 = map.get("dealerId2");
	String mydealerId = map.get("mydealerId");
	String beginTime = map.get("beginTime");
	String endTime = map.get("endTime");
	String areaId = map.get("areaId");
	String groupCode = map.get("groupCode");
	
	List<Map<String, Object>> list = null;
	StringBuffer sql = new StringBuffer();
	
	sql.append("select /*+ all_rows */ dcr.org_name,\n");
	sql.append("       dcr.region_name,\n");  
	sql.append("       dcr.root_dealer_name,\n");  
	sql.append("       dcr.dealer_name,\n");  
	sql.append("       dcr.series_name,\n");  
	sql.append("       dcr.package_code,\n");  
	sql.append("       dcr.package_name,\n");  
	sql.append("       tba.area_name,\n"); 
	sql.append("      sum(dcr.amount) ACTAMOUNT\n");  
	 
	sql.append("  from day_calculate_report dcr, TM_BUSINESS_AREA tba\n");  
	sql.append(" where 1=1 AND dcr.data_type = 'ACT'\n"); 
	sql.append("and dcr.area_id=tba.area_id\n");
	if(Utility.testString(modelId)){
		sql.append("AND DCR.SERIES_ID IN ("+modelId+")\n");
	}
	if(Utility.testString(modelId4)){
		sql.append("AND DCR.PACKAGE_ID IN ("+modelId4+")\n");
	}
	if(Utility.testString(orgId)){
		sql.append("AND DCR.ORG_ID IN ("+orgId+")\n");
	}
	if(Utility.testString(myOrgId)){
		sql.append("AND DCR.ORG_ID IN ("+myOrgId+")\n");
	}
	if(Utility.testString(dealerId)){
		sql.append("AND DCR.root_dealer_id IN ("+dealerId+") AND 10851001=(SELECT DEALER_LEVEL FROM TM_DEALER WHERE DEALER_ID=DCR.root_dealer_id)\n");
	}
	if(Utility.testString(dealerId2)){
		sql.append("AND  DCR.DEALER_ID IN ("+dealerId2+") AND 10851002=(SELECT DEALER_LEVEL FROM TM_DEALER WHERE DEALER_ID=DCR.dealer_id)\n");
	}
	if(Utility.testString(mydealerId)){
		sql.append("AND  DCR.DEALER_ID IN ("+mydealerId+")\n");
	}
	if(Utility.testString(beginTime)){
		sql.append("AND DCR.DDATE >= '"+beginTime+"'\n");
	}
	if(Utility.testString(endTime)){
		sql.append("AND '"+endTime+"'>= DCR.DDATE\n");
	}
	if(Utility.testString(areaId)){
		sql.append("AND DCR.AREA_ID in ("+areaId+")\n");
	}
	if(Utility.testString(groupCode)){
		sql.append("AND DCR.PACKAGE_CODE in ( ");
		String[] groupCodes = groupCode.split(",");
		StringBuffer dc = new StringBuffer();
		for(int i=0; i<groupCodes.length; i++) {
			dc.append("'").append(groupCodes[i]).append("'");
			if(i != groupCodes.length-1) {
				dc.append(",");
			}
		}
		sql.append(dc.toString());
		sql.append(")\n");
	}
	sql.append(" group by dcr.org_name,\n");  
	sql.append("          dcr.region_name,\n");  
	sql.append("          dcr.root_dealer_name,\n");  
	sql.append("          dcr.dealer_name,\n");  
	sql.append("          dcr.series_name,\n");  
	sql.append("          dcr.package_code,\n");  
	sql.append("          dcr.package_name,\n");  
	sql.append("          tba.area_name,\n");  
	sql.append("          dcr.amount\n");  
	sql.append(" order by dcr.org_name,\n");  
	sql.append("          dcr.region_name,\n");  
	sql.append("          dcr.root_dealer_name,\n");  
	sql.append("          dcr.dealer_name,\n");  
	sql.append("          dcr.series_name,\n");  
	sql.append("          dcr.package_code,\n");  
	sql.append("          dcr.package_name,\n");  
	sql.append("          tba.area_name,\n");  
	sql.append("          dcr.amount\n");

	
	
	
	

	list=super.pageQuery(sql.toString(), null, getFunName());
	return list;
}
}
