package com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class FleetFactDao extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(FleetFactDao.class);
	
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PageResult<Map<String, Object>> queryFleetFactList(String factNo, String factName,String companyId, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.PACT_ID, A.PACT_NO, PACT_NAME, A.REMART, A.STATUS\n");
		sql.append("FROM TM_COMPANY_PACT A\n");
		sql.append("WHERE A.OEM_COMPANY_ID = ").append(companyId);

		if(Utility.testString(factNo)){
			sql.append("AND A.PACT_NO LIKE '%").append(factNo).append("%'\n");
		}
		if(Utility.testString(factName)){
			sql.append("AND A.PACT_NAME LIKE '%").append(factName).append("%'\n");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public Map<String, Object> getPactInfo(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.PACT_ID, A.PACT_NO, PACT_NAME, A.REMART, A.STATUS, A.PARENT_FLEET_TYPE, A.IS_ALLOW_APPLY\n");
		sql.append("FROM TM_COMPANY_PACT A\n");
		sql.append("WHERE A.PACT_ID = ").append(id);

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
}
