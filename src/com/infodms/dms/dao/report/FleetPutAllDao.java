package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class FleetPutAllDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(FleetPutAllDao.class);
	public static final FleetPutAllDao dao = new FleetPutAllDao();
	public static final FleetPutAllDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Map<String, Object>> getFleetPutAllSelect(Map<String, Object> map ){
		List<Map<String, Object>> list = null;
		String auditBeginTime = map.get("auditBeginTime").toString();
		String auditEndTime = map.get("auditEndTime").toString();
		String submitBeginTime = map.get("submitBeginTime").toString();
		String submitEndTime = map.get("submitEndTime").toString();
		String groupCode = map.get("groupCode").toString();
		String dealerId = map.get("dealerId").toString();
		String orgId = map.get("orgId").toString();
		String fleetType = map.get("fleetType").toString();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT     /*+ all_rows */    VOD.ROOT_ORG_NAME,\n");
		sql.append("  VOD.REGION_NAME,\n");
		sql.append(" VOD.DEALER_NAME,\n");
		sql.append(" TO_CHAR(TF.SUBMIT_DATE, 'YYYY-MM-DD') AS SUBMIT_DATE,\n");
		sql.append("TO_CHAR(TF.AUDIT_DATE, 'YYYY-MM-DD') AS AUDIT_DATE,\n");
		sql.append(" TF.FLEET_NAME,\n");
		sql.append("TF.MAIN_LINKMAN,\n");
		sql.append("TF.MAIN_PHONE,\n");
		sql.append("TC.CODE_DESC FLEET_TYPE,\n");
		sql.append("DECODE(D.GROUP_NAME,'','全系',D.GROUP_NAME),\n");
		sql.append("TF.SERIES_COUNT,\n");
		sql.append("nvl(TFIN.INTENT_COUNT,0) INTENT_COUNT,\n");
		sql.append("TFIN.INTENT_POINT,\n");
		sql.append(" TF.REQ_REMARK\n");
		sql.append(" FROM TM_FLEET TF,\n");
		sql.append(" TM_VHCL_MATERIAL_GROUP D,\n");
		sql.append(" VW_ORG_DEALER VOD,\n");
		sql.append("TM_DEALER TMD,\n");
		sql.append("TM_COMPANY TMC,\n");
		sql.append(" TM_ORG TMO,\n");
		sql.append("TT_FLEET_INTENT_NEW TFIN,TC_CODE TC\n");
		sql.append("WHERE TF.IS_DEL = 0\n");
		sql.append("AND TF.SERIES_ID = D.GROUP_ID(+)\n");
		sql.append(" AND TMD.COMPANY_ID=TF.DLR_COMPANY_ID\n");
		sql.append("AND TMC.COMPANY_ID=TF.DLR_COMPANY_ID\n");
		sql.append("AND VOD.DEALER_ID=F_GET_PID(TMD.DEALER_ID)\n");
		sql.append("AND TF.STATUS <>11021001\n");
		sql.append("AND TMO.ORG_ID=VOD.ROOT_ORG_ID\n");
		sql.append("AND TMO.IS_COUNT=0\n");
		sql.append("AND TF.FLEET_ID=TFIN.FLEET_ID(+)\n");
		sql.append(" AND TC.CODE_ID=TF.FLEET_TYPE\n");
		if(Utility.testString(auditBeginTime)){
			sql.append("AND TO_DATE(TO_CHAR(TF.AUDIT_DATE, 'YYYY-MM-DD'),'yyyy-mm-dd') >= TO_DATE('"+auditBeginTime+"','yyyy-mm-dd')\n");
		}
		
		if(Utility.testString(auditEndTime)){
			sql.append("AND (TO_DATE('"+auditEndTime+"','yyyy-mm-dd')) >= TO_DATE(TO_CHAR(TF.AUDIT_DATE, 'YYYY-MM-DD'),'yyyy-mm-dd')\n");
		}
		
		if(Utility.testString(submitBeginTime)){
			sql.append("AND TO_DATE(TO_CHAR(TF.SUBMIT_DATE, 'YYYY-MM-DD'),'yyyy-mm-dd') >=  TO_DATE('"+submitBeginTime+"','yyyy-mm-dd')\n");
		}
		
		if(Utility.testString(submitEndTime)){
			sql.append("AND (TO_DATE('"+submitEndTime+"','yyyy-mm-dd')) >=  TO_DATE(TO_CHAR(TF.SUBMIT_DATE, 'YYYY-MM-DD'),'yyyy-mm-dd')\n");
		}
		
		if(Utility.testString(groupCode)){
			sql.append("AND D.GROUP_CODE = '"+groupCode+"'\n");
		}
		
		if(Utility.testString(dealerId)){
			sql.append("AND VOD.DEALER_ID IN ("+dealerId+")\n");
		}
		
		if(Utility.testString(orgId)){
			sql.append("AND VOD.ROOT_ORG_ID IN ("+orgId+")\n");
		}
		
		if(Utility.testString(fleetType)){
			sql.append("AND TF.FLEET_TYPE IN ("+fleetType+")\n");
		}
		
		
		sql.append("GROUP BY     VOD.ROOT_ORG_NAME,\n");
		sql.append(" VOD.REGION_NAME,\n");
		sql.append("VOD.DEALER_NAME,\n");
		sql.append("TF.SUBMIT_DATE,\n");
		sql.append("TF.AUDIT_DATE,\n");
		sql.append("TF.FLEET_NAME,\n");
		sql.append("TF.MAIN_LINKMAN,\n");
		sql.append("TF.MAIN_PHONE,\n");
		sql.append("TC.CODE_DESC,\n");
		sql.append("D.GROUP_NAME,\n");
		sql.append(" TFIN.INTENT_COUNT,\n");
		sql.append("TFIN.INTENT_POINT,\n");
		sql.append(" TF.REQ_REMARK,\n");
		sql.append("TF.SERIES_COUNT\n");
		sql.append("order by ROOT_ORG_NAME,REGION_NAME\n");
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
