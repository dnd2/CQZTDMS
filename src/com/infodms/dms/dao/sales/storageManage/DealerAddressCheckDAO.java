package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DealerAddressCheckDAO extends BaseDao{
	public static Logger logger = Logger.getLogger(DealerAddressCheckDAO.class);
	private static final DealerAddressCheckDAO dao = new DealerAddressCheckDAO ();
	public static final DealerAddressCheckDAO getInstance() {
		return dao;
	}
//	private DealerAddressCheckDAO() {}

	public static PageResult <Map<String,Object>> getAddressList(String dutyType, String orgId,String dealerCode,String address, int pageSize,int curPage,String poseId,AclUserBean logonUser){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT DISTINCT A.ID,\n");
		sql.append("       TMD.DEALER_SHORTNAME,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_ID,\n");
		sql.append("       TCU.NAME,\n");
		sql.append("       A.ADD_CODE,\n");
		sql.append("       A.ADDRESS,\n");
		sql.append("       A.LINK_MAN,\n");
		sql.append("       A.TEL,\n");
		sql.append("       A.REMARK,\n");
		sql.append("       A.RECEIVE_ORG,\n");
		sql.append("       A.ADDRESS_USE,\n");
		sql.append("       A.LIMIT_TYPE,\n");
//		sql.append("       TBA.AREA_NAME a_name,\n");
		sql.append("       DECODE(A.LIMIT_TYPE, ").append(Constant.ADDRESS_TIME_LIMIT_TEMP).append(", TO_CHAR(A.START_TIME,'YYYY-MM-DD'), '') START_TIME,\n");
		sql.append("       DECODE(A.LIMIT_TYPE, ").append(Constant.ADDRESS_TIME_LIMIT_TEMP).append(", TO_CHAR(A.END_TIME,'YYYY-MM-DD'), '') END_TIME,\n");
		sql.append("       TO_CHAR(A.LOW_CHECK_DATE, 'YYYY-MM-DD') LOW_CHECK_DATE,\n");
		sql.append("       (SELECT TR.REGION_NAME\n");
		sql.append("          FROM TM_REGION TR\n");
		sql.append("         WHERE TR.REGION_CODE = A.PROVINCE_ID and tr.region_type = ").append(Constant.REGION_TYPE_02).append(") PROVINCE_NAME,\n");
		sql.append("       (SELECT TR.REGION_NAME\n");
		sql.append("          FROM TM_REGION TR\n");
		sql.append("         WHERE TR.REGION_CODE = A.CITY_ID and tr.region_type = ").append(Constant.REGION_TYPE_03).append(") CITY_NAME,\n");
		sql.append("       (SELECT TR.REGION_NAME\n");
		sql.append("          FROM TM_REGION TR\n");
		sql.append("         WHERE TR.REGION_CODE = A.AREA_ID and tr.region_type = ").append(Constant.REGION_TYPE_04).append(") AREA_NAME,\n");
		sql.append("       A.STATUS\n");
		sql.append("  FROM TM_VS_ADDRESS A, TM_DEALER TMD, TC_USER TCU \n");
		sql.append(" WHERE A.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND A.LOW_AUDIT = TCU.USER_ID(+)\n");
		sql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("A.DEALER_ID", logonUser));
//		sql.append("   AND A.B_AREA_ID = TBA.AREA_ID(+)\n");
//		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("A.B_AREA_ID", poseId));
		// 若登录用户为大区，则查询已提报的地址信息
		if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			sql.append("   AND A.STATUS = "+Constant.DEALER_ADDRESS_CHANGE_STATUS_02+"\n");
		// 若登录用户为车厂，则查询初审完成的地址信息
		} else if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
			sql.append("   AND A.STATUS = "+Constant.DEALER_ADDRESS_CHANGE_STATUS_FIRST_CHK+"\n");
		}
		
//		if(null != areaIds && !"".equals(areaIds)){
//			sql.append(" AND A.B_AREA_ID = "+areaIds+"\n");
//		}

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

//		if (null != areaIds && !"".equals(areaIds)) {
//			sql.append("   AND A.B_AREA_ID in (").append(areaIds).append(")\n");
			/*sql.append("and exists (select 1\n");
			sql.append("          from tt_address_area_r taar\n");
			sql.append("         where taar.address_id = a.id\n");
			sql.append("           and taar.area_id in (").append(areaIds).append("))\n");

			sql.append("AND EXISTS (SELECT 1\n");
			sql.append("         FROM TM_DEALER_BUSINESS_AREA TDBA\n");
			sql.append("        WHERE TDBA.DEALER_ID = TMD.DEALER_ID\n");
			sql.append("        AND TDBA.AREA_ID IN ("+areaIds+"))\n");*/
//		}

	//	if (!"".equals(orgId)) {
//			sql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("TMD.DEALER_ID",Long.valueOf(orgId)));
//			sql.append("AND EXISTS (SELECT 1\n");
//			sql.append("         FROM VW_ORG_DEALER VOD\n");
//			sql.append("        WHERE VOD.DEALER_ID = TMD.DEALER_ID\n");
//			sql.append("        AND VOD.ROOT_ORG_ID = "+orgId+")\n");
		//}

		sql.append(" ORDER BY A.ID DESC\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	//通过经销商查询该经销商审核通过的地址
	public static PageResult <Map<String,Object>> getPassAddressListByDealerId(String orgId, String dealerId, String areaIds, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ID,\n");
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
		sql.append("  FROM TM_VS_ADDRESS A\n");
		sql.append(" WHERE A.DEALER_ID = "+dealerId+"\n");
		sql.append("   AND A.STATUS = "+Constant.STATUS_ENABLE+"\n");

//		if (null != areaIds && !"".equals(areaIds)) {
//			sql.append("AND EXISTS (SELECT 1\n");
//			sql.append("         FROM TM_DEALER_BUSINESS_AREA TDBA\n");
//			sql.append("        WHERE TDBA.DEALER_ID = A.DEALER_ID\n");
//			sql.append("        AND TDBA.AREA_ID IN ("+areaIds+"))\n");
//		}

		if (!"".equals(orgId)) {
			sql.append("AND EXISTS (SELECT 1\n");
			sql.append("         FROM VW_ORG_DEALER VOD\n");
			sql.append("        WHERE VOD.DEALER_ID = A.DEALER_ID\n");
			sql.append("        AND VOD.ROOT_ORG_ID = "+orgId+")\n");
		}

		sql.append(" ORDER BY A.ID DESC\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}

	public static Map<String,Object> getAddressInfo(String id){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ID,\n");
		sql.append("       TMD.DEALER_SHORTNAME,\n");
		sql.append("       A.ADD_CODE,\n");
		sql.append("       A.ADDRESS,\n");
		sql.append("       A.LINK_MAN,\n");
		sql.append("       A.TEL,\n");
		sql.append("       A.REMARK,\n");
		sql.append("       A.RECEIVE_ORG,\n");
		sql.append("       A.PROVINCE_ID,\n");
		sql.append("       A.CITY_ID,\n");
		sql.append("       A.AREA_ID,\n");
		sql.append("       A.STATUS\n");
		sql.append("  FROM TM_VS_ADDRESS A, TM_DEALER TMD\n");
		sql.append(" WHERE A.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("  AND A.ID = "+id+"\n");

		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}

	public void checkInfoInsert(Map<String, String> map) {
		String status = map.get("status") ;
		String userId = map.get("userId") ;
		String desc = map.get("desc") ;
		String addressId = map.get("addressId") ;

		StringBuffer sql = new StringBuffer("\n") ;
		String checkId = SequenceManager.getSequence("");
		sql.append("insert into tt_vs_address_check values(").append(checkId).append(",").append(addressId).append(",'").append(desc).append("',").append(userId).append(",sysdate,'','',").append(status).append(") \n");

		dao.insert(sql.toString()) ;
	}

	public List<Map<String, Object>> addressCheckQuery(String addressId) {
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select tcu.name,\n");
		sql.append("       tvac.check_desc,\n");
		sql.append("       tvac.check_status,\n");
		sql.append("       to_char(tvac.create_date, 'yyyy-mm-dd') create_date\n");
		sql.append("  from tt_vs_address_check tvac, tc_user tcu\n");
		sql.append(" where tvac.create_by = tcu.user_id\n");
		sql.append(" and tvac.address_id =").append(addressId).append("\n");
		sql.append(" order by tvac.create_date desc\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public static PageResult <Map<String,Object>> getAddressListLogistics(String dutyType, String orgId,String dealerCode,String address, String areaIds, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ID,\n");
		sql.append("       TMD.DEALER_SHORTNAME,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_ID,\n");
		sql.append("       TCU.NAME,\n");
		sql.append("       A.ADD_CODE,\n");
		sql.append("       A.ADDRESS,\n");
		sql.append("       A.LINK_MAN,\n");
		sql.append("       A.TEL,\n");
		sql.append("       A.REMARK,\n");
		sql.append("       A.RECEIVE_ORG,\n");
		sql.append("       A.ADDRESS_USE,\n");
		sql.append("       A.LIMIT_TYPE,\n");
		sql.append("       TBA.AREA_NAME a_name,\n");
		sql.append("       DECODE(A.LIMIT_TYPE, ").append(Constant.ADDRESS_TIME_LIMIT_TEMP).append(", TO_CHAR(A.START_TIME,'YYYY-MM-DD'), '') START_TIME,\n");
		sql.append("       DECODE(A.LIMIT_TYPE, ").append(Constant.ADDRESS_TIME_LIMIT_TEMP).append(", TO_CHAR(A.END_TIME,'YYYY-MM-DD'), '') END_TIME,\n");
		sql.append("       TO_CHAR(A.LOW_CHECK_DATE, 'YYYY-MM-DD') LOW_CHECK_DATE,\n");
		sql.append("       (SELECT TR.REGION_NAME\n");
		sql.append("          FROM TM_REGION TR\n");
		sql.append("         WHERE TR.REGION_CODE = A.PROVINCE_ID and tr.region_type = ").append(Constant.REGION_TYPE_02).append(") PROVINCE_NAME,\n");
		sql.append("       (SELECT TR.REGION_NAME\n");
		sql.append("          FROM TM_REGION TR\n");
		sql.append("         WHERE TR.REGION_CODE = A.CITY_ID and tr.region_type = ").append(Constant.REGION_TYPE_03).append(") CITY_NAME,\n");
		sql.append("       (SELECT TR.REGION_NAME\n");
		sql.append("          FROM TM_REGION TR\n");
		sql.append("         WHERE TR.REGION_CODE = A.AREA_ID and tr.region_type = ").append(Constant.REGION_TYPE_04).append(") AREA_NAME,\n");
		sql.append("       A.STATUS\n");
		sql.append("  FROM TM_VS_ADDRESS A, TM_DEALER TMD, TC_USER TCU, TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE A.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND A.LOW_AUDIT = TCU.USER_ID(+)\n");
		sql.append("   AND A.B_AREA_ID = TBA.AREA_ID(+)\n");


			sql.append("   AND A.STATUS = "+Constant.DEALER_ADDRESS_CHANGE_STATUS_Logistics+"\n");


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
			sql.append("   AND A.B_AREA_ID in (").append(areaIds).append(")\n");

			/*sql.append("and exists (select 1\n");
			sql.append("          from tt_address_area_r taar\n");
			sql.append("         where taar.address_id = a.id\n");
			sql.append("           and taar.area_id in (").append(areaIds).append("))\n");*/

			/*sql.append("AND EXISTS (SELECT 1\n");
			sql.append("         FROM TM_DEALER_BUSINESS_AREA TDBA\n");
			sql.append("        WHERE TDBA.DEALER_ID = TMD.DEALER_ID\n");
			sql.append("        AND TDBA.AREA_ID IN ("+areaIds+"))\n");*/
		}

		if (!"".equals(orgId)) {
			sql.append("AND EXISTS (SELECT 1\n");
			sql.append("         FROM VW_ORG_DEALER VOD\n");
			sql.append("        WHERE VOD.DEALER_ID = TMD.DEALER_ID\n");
			sql.append("        AND VOD.ROOT_ORG_ID = "+orgId+")\n");
		}

		sql.append(" ORDER BY A.ID DESC\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	public static PageResult <Map<String,Object>> getAddressList(String dutyType, String orgId,String dealerCode,String address, String areaIds, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ID,\n");
		sql.append("       TMD.DEALER_SHORTNAME,\n");  
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_ID,\n");
		sql.append("       TCU.NAME,\n");
		sql.append("       A.ADD_CODE,\n");  
		sql.append("       A.ADDRESS,\n");  
		sql.append("       A.LINK_MAN,\n");  
		sql.append("       A.TEL,\n");  
		sql.append("       A.REMARK,\n");  
		sql.append("       A.RECEIVE_ORG,\n");  
		sql.append("       A.ADDRESS_USE,\n"); 
		sql.append("       A.LIMIT_TYPE,\n"); 
		sql.append("       TBA.AREA_NAME a_name,\n"); 
		sql.append("       DECODE(A.LIMIT_TYPE, ").append(Constant.ADDRESS_TIME_LIMIT_TEMP).append(", TO_CHAR(A.START_TIME,'YYYY-MM-DD'), '') START_TIME,\n");
		sql.append("       DECODE(A.LIMIT_TYPE, ").append(Constant.ADDRESS_TIME_LIMIT_TEMP).append(", TO_CHAR(A.END_TIME,'YYYY-MM-DD'), '') END_TIME,\n");
		sql.append("       TO_CHAR(A.LOW_CHECK_DATE, 'YYYY-MM-DD') LOW_CHECK_DATE,\n"); 
		sql.append("       (SELECT TR.REGION_NAME\n");  
		sql.append("          FROM TM_REGION TR\n");  
		sql.append("         WHERE TR.REGION_CODE = A.PROVINCE_ID and tr.region_type = ").append(Constant.REGION_TYPE_02).append(") PROVINCE_NAME,\n");  
		sql.append("       (SELECT TR.REGION_NAME\n");  
		sql.append("          FROM TM_REGION TR\n");  
		sql.append("         WHERE TR.REGION_CODE = A.CITY_ID and tr.region_type = ").append(Constant.REGION_TYPE_03).append(") CITY_NAME,\n");  
		sql.append("       (SELECT TR.REGION_NAME\n");  
		sql.append("          FROM TM_REGION TR\n");  
		sql.append("         WHERE TR.REGION_CODE = A.AREA_ID and tr.region_type = ").append(Constant.REGION_TYPE_04).append(") AREA_NAME,\n");  
		sql.append("       A.STATUS\n");
		sql.append("  FROM TM_VS_ADDRESS A, TM_DEALER TMD, TC_USER TCU, TM_BUSINESS_AREA TBA\n");  
		sql.append(" WHERE A.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND A.LOW_AUDIT = TCU.USER_ID(+)\n");  
		sql.append("   AND A.B_AREA_ID = TBA.AREA_ID(+)\n");  
		
		// 若登录用户为大区，则查询已提报的地址信息
		if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			sql.append("   AND A.STATUS = "+Constant.DEALER_ADDRESS_CHANGE_STATUS_02+"\n");  
		// 若登录用户为车厂，则查询初审完成的地址信息
		} else if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
			sql.append("   AND A.STATUS = "+Constant.DEALER_ADDRESS_CHANGE_STATUS_FIRST_CHK+"\n");  
		}
		
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
			sql.append("   AND A.B_AREA_ID in (").append(areaIds).append(")\n");
			
			/*sql.append("and exists (select 1\n");
			sql.append("          from tt_address_area_r taar\n");  
			sql.append("         where taar.address_id = a.id\n");  
			sql.append("           and taar.area_id in (").append(areaIds).append("))\n");*/

			/*sql.append("AND EXISTS (SELECT 1\n");
			sql.append("         FROM TM_DEALER_BUSINESS_AREA TDBA\n");  
			sql.append("        WHERE TDBA.DEALER_ID = TMD.DEALER_ID\n");
			sql.append("        AND TDBA.AREA_ID IN ("+areaIds+"))\n");*/
		}
		
		if (!"".equals(orgId)) {
			sql.append("AND EXISTS (SELECT 1\n");
			sql.append("         FROM vw_org_dealer VOD\n");  
			sql.append("        WHERE VOD.DEALER_ID = TMD.DEALER_ID\n");
			sql.append("        AND VOD.ROOT_ORG_ID = "+orgId+")\n");
		}
		
		sql.append(" ORDER BY A.ID DESC\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}

}
