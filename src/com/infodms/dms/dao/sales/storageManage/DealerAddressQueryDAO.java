package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DealerAddressQueryDAO extends BaseDao{
	public static Logger logger = Logger.getLogger(DealerAddressQueryDAO.class);
	private static final DealerAddressQueryDAO dao = new DealerAddressQueryDAO ();
	public static final DealerAddressQueryDAO getInstance() {
		return dao;
	}
	
	public static PageResult <Map<String,Object>> getAddressList(String orgId,String dealerCode,String address, String areaIds, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ID,\n");
		sql.append("       TMD.DEALER_SHORTNAME,\n");  
		sql.append("       TMD.DEALER_CODE,\n");  
		sql.append("       A.ADD_CODE,\n");  
		sql.append("       A.ADDRESS,\n");  
		sql.append("       A.LINK_MAN,\n");  
		sql.append("       A.TEL,\n");  
		sql.append("       A.REMARK,\n");  
		sql.append("       A.RECEIVE_ORG,\n");  
		sql.append("       (SELECT TR.REGION_NAME\n");  
		sql.append("          FROM TM_REGION TR\n");  
		sql.append("         WHERE TR.REGION_CODE = A.PROVINCE_ID) PROVINCE_NAME,\n");  
		sql.append("       (SELECT TR.REGION_NAME\n");  
		sql.append("          FROM TM_REGION TR\n");  
		sql.append("         WHERE TR.REGION_CODE = A.CITY_ID) CITY_NAME,\n");  
		sql.append("       (SELECT TR.REGION_NAME\n");  
		sql.append("          FROM TM_REGION TR\n");  
		sql.append("         WHERE TR.REGION_CODE = A.AREA_ID) AREA_NAME,\n");  
		sql.append("       A.STATUS\n");
		sql.append("  FROM TM_VS_ADDRESS A, TM_DEALER TMD\n");  
		sql.append(" WHERE A.DEALER_ID = TMD.DEALER_ID\n");   
		sql.append(" AND A.STATUS <> ").append(Constant.DEALER_ADDRESS_CHANGE_STATUS_01).append("\n");
		
		if(null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != address && !"".equals(address)) {
			sql.append("   AND A.ADDRESS LIKE '%"+address.trim()+"%'\n");
		}
		if (null != areaIds && !"".equals(areaIds)) {
			sql.append("AND EXISTS (SELECT 1\n");
			sql.append("         FROM TM_DEALER_BUSINESS_AREA TDBA\n");  
			sql.append("        WHERE TDBA.DEALER_ID = TMD.DEALER_ID\n");
			sql.append("        AND TDBA.AREA_ID IN ("+areaIds+"))\n");
		}
		
		if (!"".equals(orgId)) {
			sql.append("AND EXISTS (SELECT 1\n");
			sql.append("         FROM vw_org_dealer VOD\n");  
			sql.append("        WHERE VOD.DEALER_ID = TMD.DEALER_ID\n");
			sql.append("        AND VOD.ROOT_ORG_ID = "+orgId+")\n");
		}
		sql.append(" ORDER BY A.ADD_CODE, A.STATUS\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
