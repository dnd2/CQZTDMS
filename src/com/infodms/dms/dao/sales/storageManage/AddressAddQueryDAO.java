package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class AddressAddQueryDAO  extends BaseDao{

	public static Logger logger = Logger.getLogger(AddressAddQueryDAO.class);
	private static final AddressAddQueryDAO dao = new AddressAddQueryDAO ();
	public static final AddressAddQueryDAO getInstance() {
		return dao;
	}
	
	
	public static PageResult <Map<String,Object>> getChangeList(String areaId, String status,String dealerId,String address, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ID,\n");
		sql.append("       A.ADD_CODE,\n");  
		sql.append("       A.ADDRESS,\n");  
		sql.append("       TCU1.NAME LOW_AUDIT,\n");  
		sql.append("       TCU2.NAME FACTORY_AUDIT,\n"); 
		sql.append("       A.ADDRESS_USE,\n"); 
		sql.append("       A.LIMIT_TYPE,\n"); 
		sql.append("       DECODE(A.LIMIT_TYPE, ").append(Constant.ADDRESS_TIME_LIMIT_TEMP).append(", TO_CHAR(A.START_TIME,'YYYY-MM-DD'), '') START_TIME,\n");
		sql.append("       DECODE(A.LIMIT_TYPE, ").append(Constant.ADDRESS_TIME_LIMIT_TEMP).append(", TO_CHAR(A.END_TIME,'YYYY-MM-DD'), '') END_TIME,\n");
		sql.append("       TO_CHAR(A.LOW_CHECK_DATE, 'YYYY-MM-DD') LOW_CHECK_DATE,\n"); 
		sql.append("       TO_CHAR(A.FACTORY_CHECK_DATE, 'YYYY-MM-DD') FACTORY_CHECK_DATE,\n"); 
		sql.append("       TBA.AREA_NAME,\n");  
		sql.append("       A.LINK_MAN,\n");  
		sql.append("       A.TEL,\n");  
		sql.append("       A.REMARK,\n");  
		sql.append("       A.RECEIVE_ORG,\n");  
		sql.append("       A.STATUS\n");
		sql.append("  FROM TM_VS_ADDRESS A, TC_USER TCU1, TC_USER TCU2, TM_BUSINESS_AREA TBA\n");  
		sql.append(" WHERE 1=1\n");  
		sql.append("   AND A.LOW_AUDIT = TCU1.USER_ID(+)\n");
		sql.append("   AND A.B_AREA_ID = TBA.AREA_ID(+)\n");
		sql.append("   AND A.FACTORY_AUDIT = TCU2.USER_ID(+)\n");
		sql.append("   AND A.DEALER_ID IN ("+dealerId+")\n");  
		sql.append("   AND A.STATUS IN ("+Constant.DEALER_ADDRESS_CHANGE_STATUS_Logistics+","+Constant.DEALER_ADDRESS_CHANGE_STATUS_02+","+Constant.DEALER_ADDRESS_CHANGE_STATUS_03+","+Constant.STATUS_ENABLE+","+Constant.DEALER_ADDRESS_CHANGE_STATUS_FIRST_CHK+","+Constant.STATUS_DISABLE+")\n");
		if (null != address && !"".equals(address)) {
			sql.append("   AND A.ADDRESS LIKE '%"+address.trim()+"%'\n");
		}
		if (null != status && !"".equals(status)) {
			sql.append("   AND A.STATUS = "+status+"\n");
		}
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND A.B_AREA_ID in (").append(areaId).append(")\n");
			
			/*sql.append("and exists (select 1\n");
			sql.append("          from tt_address_area_r taar\n");  
			sql.append("         where taar.address_id = a.id\n");  
			sql.append("           and taar.area_id in (").append(areaId).append("))\n");*/
		}
		sql.append(" ORDER BY A.ADD_CODE, A.B_AREA_ID DESC\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	public PageResult<Map<String, Object>> addressQuery(Map<String, String> map, int pageSize,int curPage) {
		String areaId = map.get("areaId") ;
		String status = map.get("status") ;
		String limit = map.get("limit") ;
		String addressName = map.get("addressName") ;
		String dealerIds = map.get("dealerIds") ;
		String orgId = map.get("orgId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select vod.root_org_name,\n");
		sql.append("       vod.dealer_name || '-' || vod.dealer_code dealer_unit,\n");  
		sql.append("       tva.id,\n");  
		sql.append("       tva.add_code,\n");  
		sql.append("       tva.address,\n");  
		sql.append("       tva.address_use,\n");  
		sql.append("       tva.limit_type,\n");  
		sql.append("       tva.link_man,\n");  
		sql.append("       tba.area_name,\n");  
		sql.append("       tva.tel,\n");  
		sql.append("       tva.remark,\n");  
		sql.append("       decode(tva.limit_type,\n");  
		sql.append("              ").append(Constant.ADDRESS_TIME_LIMIT_TEMP).append(",\n");  
		sql.append("              to_char(tva.start_time, 'yyyy-mm-dd'),\n");  
		sql.append("              '') start_time,\n");  
		sql.append("       decode(tva.limit_type,\n");  
		sql.append("              ").append(Constant.ADDRESS_TIME_LIMIT_TEMP).append(",\n");  
		sql.append("              to_char(tva.end_time, 'yyyy-mm-dd'),\n");  
		sql.append("              '') end_time,\n");  
		sql.append("       tva.receive_org,\n");  
		sql.append("       tva.status,\n");  
		sql.append("       tcu1.name low_audit,\n");  
		sql.append("       to_char(tva.low_check_date, 'yyyy-mm-dd') low_check_date,\n");  
		sql.append("       tcu2.name factory_audit,\n");  
		sql.append("       to_char(tva.factory_check_date, 'yyyy-mm-dd') factory_check_date\n");  
		sql.append("  from tm_vs_address           tva,\n");  
		sql.append("       vw_org_dealer           vod,\n");  
		sql.append("       TM_BUSINESS_AREA        TBA,\n");  
		sql.append("       tc_user                 tcu1,\n");  
		sql.append("       tc_user                 tcu2\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and tva.dealer_id = vod.dealer_id\n");  
		sql.append("   and tva.low_audit = tcu1.user_id(+)\n");  
		sql.append("   and tva.b_area_id = tba.area_id(+)\n");  
		sql.append("   and tva.factory_audit = tcu2.user_id(+)\n");  
		sql.append("   and tva.status in ("+Constant.DEALER_ADDRESS_CHANGE_STATUS_Logistics+","+Constant.DEALER_ADDRESS_CHANGE_STATUS_02+","+Constant.DEALER_ADDRESS_CHANGE_STATUS_03+","+Constant.STATUS_ENABLE+","+Constant.DEALER_ADDRESS_CHANGE_STATUS_FIRST_CHK+","+Constant.STATUS_DISABLE+")\n");
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("   AND tva.B_AREA_ID in (").append(areaId).append(")\n");
			
			/*sql.append("and exists\n");
			sql.append(" (select 1\n");  
			sql.append("          from tt_address_area_r taar\n");  
			sql.append("         where taar.address_id = tva.id\n");  
			sql.append("           and taar.area_id in (").append(areaId).append("))\n");*/
		}
		
		if(!CommonUtils.isNullString(status)) {
			sql.append("   and tva.status = ").append(status).append("\n");  
		}
		
		if(!CommonUtils.isNullString(limit)) {
			sql.append("   and tva.limit_type = ").append(limit).append("\n");  
		}
		
		if(!CommonUtils.isNullString(addressName)) {
			sql.append("   and tva.address like '%").append(addressName).append("%'\n");  
		}
		
		if(!CommonUtils.isNullString(dealerIds)) {
			sql.append("   and vod.dealer_id in (").append(dealerIds).append(")\n");  
		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("   and vod.root_org_id in (").append(orgId).append(")\n");  
		}
		
		sql.append(" order by vod.root_org_id, vod.dealer_code, tva.add_code\n");  

		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
