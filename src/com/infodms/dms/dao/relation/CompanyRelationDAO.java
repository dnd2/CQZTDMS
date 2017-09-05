package com.infodms.dms.dao.relation;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CompanyRelationDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(CompanyRelationDAO.class);
	
	private CompanyRelationDAO() {
		
	}
	
	private static class CompanyRelationDaoSingleton {
		private static CompanyRelationDAO dao = new CompanyRelationDAO() ;
	}
	
	public static CompanyRelationDAO getInstance() {
		return CompanyRelationDaoSingleton.dao ;
	}
	
	public PageResult<Map<String, Object>> getCompanyByOtherQuery(Map<String, String> map,int pageSize,int curPage) {
		String dealerType = map.get("dealerType") ;
		String orgId = map.get("orgId") ;
		String companyCode = map.get("companyCode") ;
		String companyName = map.get("companyName") ;
		String regionCode = map.get("regionCode") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tmc.company_id,\n");
		sql.append("       tmc.company_name,\n");  
		sql.append("       tmc.company_code,\n");  
		sql.append("       tmc.province_id,\n");  
		sql.append("       tmr.region_name\n");  
		sql.append("  from tm_company tmc, tm_region tmr\n");  
		sql.append(" where tmc.province_id = tmr.region_code(+)\n");
		sql.append("   and tmc.status = ").append(Constant.STATUS_ENABLE).append("\n");  
		
		if(!CommonUtils.isNullString(companyCode)) {
			sql.append("   and tmc.company_code like '%").append(companyCode).append("%'\n");  
		}
		
		if(!CommonUtils.isNullString(companyName)) {
			sql.append("   and tmc.company_name like '%").append(companyName).append("%'\n");  
		}
		
		if(!CommonUtils.isNullString(regionCode)  && !"null".equals(regionCode)) {
			sql.append("   and tmc.province_id in (").append(regionCode).append(")\n");  
		}
		
		if(!CommonUtils.isNullString(dealerType) && !"null".equals(dealerType)) {
			sql.append("   and exists (select 1\n");  
			sql.append("          from tm_dealer tmd\n");  
			sql.append("         where tmd.company_id = tmc.company_id\n");  
			sql.append("           and tmd.status = ").append(Constant.STATUS_ENABLE).append("\n");  
			sql.append("           and tmd.dealer_type in (").append(dealerType).append("))\n");  
		}
		/*******区域选择 add by liuxh*******/
		if(!CommonUtils.isNullString(orgId) && !"null".equals(orgId)) {
			sql.append(" AND EXISTS\n");
			sql.append("(    SELECT   1\n");
			sql.append("       FROM   TM_DEALER KK,TM_COMPANY TC\n");
			sql.append("      WHERE   KK.COMPANY_ID=TC.COMPANY_ID AND TC.COMPANY_ID = TMC.COMPANY_ID\n");
			sql.append(" START WITH   DEALER_ID IN\n");
			sql.append("                    (SELECT   RELA.DEALER_ID\n");
			sql.append("                       FROM   TM_DEALER_ORG_RELATION RELA\n");
			sql.append("                      WHERE   EXISTS\n");
			sql.append("                                 (    SELECT   1\n");
			sql.append("                                        FROM   TM_ORG TTO\n");
			sql.append("                                       WHERE   RELA.ORG_ID = TTO.ORG_ID\n");
			sql.append("                                               AND TTO.STATUS = "+Constant.STATUS_ENABLE+"\n");
			sql.append("                                  START WITH   TTO.ORG_ID = "+orgId+"\n");
			sql.append("                                  CONNECT BY   PRIOR TTO.ORG_ID =\n");
			sql.append("                                                  TTO.PARENT_ORG_ID))\n");
			sql.append(" CONNECT BY   PRIOR KK.DEALER_ID = KK.PARENT_DEALER_D)\n");

		}
		
		return super.pageQuery(sql.toString(), null, super.getFunName(), pageSize, curPage) ;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
