package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
/*
 * 
 * 集团客户报备查询
 * 
 */
public class FleetPrepareDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(FleetPrepareDao.class);
	public static final FleetPrepareDao dao = new FleetPrepareDao();
	public static final FleetPrepareDao getInstance(){
		return dao;
		
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Map<String, Object>> getFleetPrepareSelect(Map<String, String> map){
		String orgId = (String) map.get("orgId");
		String checkDate1 = (String) map.get("checkDate1");
		String checkDate2 = (String) map.get("checkDate2");
		String dealerId = (String) map.get("dealerId");
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT /*+ all_rows */ VOD.ROOT_ORG_NAME,VOD.REGION_NAME,\n");
		sql.append("TO_CHAR(TF.SUBMIT_DATE, 'YYYY-MM-DD') AS SUBMIT_DATE,--报备日期\n");
		sql.append("TCOM.COMPANY_SHORTNAME,\n");
		sql.append(" TF.FLEET_NAME,\n");
		sql.append("TC1.CODE_DESC FLEET_TYPE,\n");
		sql.append(" TC3.CODE_DESC MAIN_BUSINESS,\n");
		sql.append("TF.SUBMIT_USER,\n");
		sql.append("TF.ZIP_CODE,\n");
		sql.append("F.ORG_NAME,\n");
		sql.append("TF.ADDRESS,\n");
		sql.append("TF.MAIN_LINKMAN,\n");
		sql.append("TF.MAIN_JOB,\n");
		sql.append("TF.MAIN_PHONE,\n");
		sql.append(" nvl(D.GROUP_NAME, '全系') GROUP_NAME,\n");
		sql.append("NVL(TF.SERIES_COUNT,0) SERIES_COUNT,\n");
		sql.append("TF.REQ_REMARK,\n");
		sql.append(" TC2.CODE_DESC STATUS,\n");
		sql.append(" TF.AUDIT_REMARK,\n");
		sql.append("TR.NAME,\n");
		sql.append("TO_CHAR(TF.AUDIT_DATE, 'YYYY-MM-DD') AS AUDIT_DATE,\n");
		sql.append(" TF.FLEET_ID\n");
		sql.append("  FROM TM_FLEET TF,\n");
		sql.append(" TC_USER TU,\n");
		sql.append(" TC_USER TR,\n");
		sql.append("TC_CODE TC1,TC_CODE TC2,TC_CODE TC3,\n");
		sql.append("TM_COMPANY TCOM,\n");
		sql.append("TM_VHCL_MATERIAL_GROUP D,\n");
		sql.append(" TM_DEALER E,\n");
		sql.append(" TM_ORG F,\n");
		sql.append(" TM_DEALER_ORG_RELATION G,VW_ORG_DEALER VOD\n");
		sql.append("WHERE TF.IS_DEL = 0\n");
		sql.append(" AND TC1.CODE_ID(+)=TF.FLEET_TYPE\n");
		sql.append(" AND TC2.CODE_ID=TF.STATUS\n");
		sql.append(" AND TC3.CODE_ID(+)=TF.MAIN_BUSINESS\n");
		sql.append(" AND TF.SUBMIT_USER = TU.USER_ID(+)\n");
		sql.append(" AND TF.AUDIT_USER_ID = TR.USER_ID(+)\n");
		sql.append("AND TF.SERIES_ID = D.GROUP_ID(+)\n");
		sql.append(" AND TF.DLR_COMPANY_ID = E.COMPANY_ID\n");
		sql.append("AND VOD.DEALER_ID=E.DEALER_ID   AND VOD.ROOT_DEALER_ID = G.DEALER_ID\n");
		sql.append("AND VOD.ROOT_ORG_ID=F.ORG_ID   AND F.ORG_ID = G.ORG_ID\n");
		sql.append(" AND F.IS_COUNT = 0\n");
		sql.append(" AND TF.DLR_COMPANY_ID = TCOM.COMPANY_ID\n");
		sql.append("AND TCOM.COMPANY_TYPE = 10531002\n");
		sql.append(" AND TF.STATUS <>11021001\n");
		sql.append(" AND TF.STATUS IN(11021003)\n");
		if(Utility.testString(orgId)){
			sql.append("AND  VOD.ROOT_ORG_ID IN ("+orgId+")\n");
		}
		if(Utility.testString(checkDate1)){
			sql.append("AND TO_DATE(TO_CHAR(TF.SUBMIT_DATE,'yyyy-MM-dd'),'yyyy-MM-dd') >= TO_DATE('"+checkDate1+"','yyyy-MM-dd')\n");
		}
		if(Utility.testString(checkDate2)){
			sql.append("AND (TO_DATE('"+checkDate2+"','yyyy-MM-dd'))>= TO_DATE(TO_CHAR(TF.SUBMIT_DATE,'yyyy-MM-dd'),'yyyy-MM-dd')\n");
		}
		if(Utility.testString(dealerId)){
			sql.append("AND VOD.DEALER_ID IN ("+dealerId+")\n");
		}
		sql.append(" order by VOD.ROOT_ORG_NAME,VOD.REGION_NAME,TCOM.COMPANY_SHORTNAME\n");
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
