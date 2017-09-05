package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class AddressAddApplyDAO  extends BaseDao{

	public static Logger logger = Logger.getLogger(AddressAddApplyDAO.class);
	private static final AddressAddApplyDAO dao = new AddressAddApplyDAO ();
	public static final AddressAddApplyDAO getInstance() {
		return dao;
	}
	private AddressAddApplyDAO() {}
	
	public static PageResult <Map<String,Object>> getCanAddList(String dealerId,String address,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ID,\n");
		sql.append("       A.ADD_CODE,\n");  
		sql.append("       A.ADDRESS,\n");  
		sql.append("       A.LINK_MAN,\n");  
		sql.append("       A.TEL,A.MOBILE_PHONE,\n");  
		sql.append("       A.REMARK,\n");  
		sql.append("       A.RECEIVE_ORG,\n");  
		sql.append("       A.STATUS,\n");
//		sql.append("       TBA.AREA_NAME,\n");
		sql.append("       A.DEALER_ID\n");
		sql.append("   FROM TM_VS_ADDRESS A \n");  
		sql.append("   WHERE 1=1\n");  
//		sql.append("   AND A.B_AREA_ID = TBA.AREA_ID(+)\n"); 
		sql.append("   AND A.DEALER_ID ="+dealerId+"\n");  
		sql.append("   AND A.STATUS NOT IN (").append(Constant.DEALER_ADDRESS_CHANGE_STATUS_02).append(",").append(Constant.DEALER_ADDRESS_CHANGE_STATUS_FIRST_CHK).append(")\n"); 
		
		if (null != address && !"".equals(address)) {
			sql.append("   AND A.ADDRESS LIKE '%"+address.trim()+"%'\n");
		}
		
//		if(!CommonUtils.isNullString(onlyAreaId)) {
//			sql.append("   AND A.B_AREA_ID = '").append(onlyAreaId).append("'\n");
//		}
		
		/*if(!CommonUtils.isNullString(onlyAreaId)) {
			sql.append("and exists (select 1\n");
			sql.append("          from tt_address_area_r taar\n");  
			sql.append("         where taar.address_id = a.id\n");  
			sql.append("           and taar.area_id in (").append(onlyAreaId).append("))\n");
		}*/
		
		sql.append(" ORDER BY A.ADD_CODE, A.B_AREA_ID DESC\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	public static Map<String,String> getAddressInfo(String id){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ID,\n");
		sql.append("       A.ADD_CODE,\n");  
		sql.append("       TMD.DEALER_SHORTNAME,\n");  
		sql.append("       A.ADDRESS,\n");  
		sql.append("       A.LINK_MAN,\n");  
		sql.append("       A.TEL,A.MOBILE_PHONE,\n");  
		sql.append("       A.REMARK,\n");  
		sql.append("       A.RECEIVE_ORG,\n");  
		sql.append("       A.PROVINCE_ID,\n");
		sql.append("       A.CITY_ID,\n");  
		sql.append("       to_char(a.dealer_id) dealer_id,\n");  
		sql.append("       A.ADDRESS_USE,\n"); 
		sql.append("       A.is_crossing,\n"); 
		sql.append("       A.LIMIT_TYPE,\n"); 
//		sql.append("       to_char(TBA.AREA_ID) THE_AREA_ID,\n"); 
//		sql.append("       TBA.AREA_NAME,\n"); 
		sql.append("       DECODE(A.LIMIT_TYPE, ").append(Constant.ADDRESS_TIME_LIMIT_TEMP).append(", TO_CHAR(A.START_TIME,'YYYY-MM-DD'), '') START_TIME,\n");
		sql.append("       DECODE(A.LIMIT_TYPE, ").append(Constant.ADDRESS_TIME_LIMIT_TEMP).append(", TO_CHAR(A.END_TIME,'YYYY-MM-DD'), '') END_TIME,\n");
		sql.append("       A.AREA_ID\n");
		sql.append("  FROM TM_VS_ADDRESS A, TM_DEALER TMD \n");  
		sql.append(" WHERE A.DEALER_ID = TMD.DEALER_ID\n"); 
//		sql.append("   AND A.B_AREA_ID = TBA.AREA_ID(+)\n");  
		sql.append("   AND A.ID = "+id+"\n");  
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	} 
	public static List<Map<String,String>> getDealerList(String dealerId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMD.DEALER_ID, TMD.DEALER_NAME\n");
		sql.append("  FROM TM_DEALER TMD\n");  
		sql.append(" START WITH TMD.DEALER_ID IN ("+dealerId+")\n");  
		sql.append("CONNECT BY PRIOR TMD.DEALER_ID = TMD.PARENT_DEALER_D\n");  
		sql.append(" ORDER BY TMD.DEALER_ID, TMD.DEALER_NAME\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	public static Map<String,String> getMaxAddCodeByDealerId(String dealerId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT MAX(A.ADD_CODE)MAX_ADD_CODE\n");
		sql.append("  FROM TM_VS_ADDRESS A\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND A.DEALER_ID IN ("+dealerId+")\n");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	public static List<Map<String,Object>> getAllAddList(String dealerId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ID,\n");
		sql.append("       A.ADDRESS,\n");  
		sql.append("       A.LINK_MAN,\n");  
		sql.append("       A.TEL,\n");  
		sql.append("       A.REMARK,\n");  
		sql.append("(SELECT TR.REGION_NAME\n");
		sql.append("          FROM TM_REGION TR\n");  
		sql.append("         WHERE TR.REGION_CODE = A.PROVINCE_ID) PROVINCE_NAME,\n");  
		sql.append("       (SELECT TR.REGION_NAME\n");  
		sql.append("          FROM TM_REGION TR\n");  
		sql.append("         WHERE TR.REGION_CODE = A.CITY_ID) CITY_NAME,\n");  
		sql.append("       (SELECT TR.REGION_NAME\n");  
		sql.append("          FROM TM_REGION TR\n");  
		sql.append("         WHERE TR.REGION_CODE = A.AREA_ID) AREA_NAME,\n");
		sql.append("       A.STATUS,\n");  
		sql.append("       A.PROVINCE_ID,\n");
		sql.append("       A.CITY_ID,\n");  
		sql.append("       A.AREA_ID\n");
		sql.append("  FROM TM_VS_ADDRESS A,TM_DEALER TMD\n");  
		sql.append(" WHERE A.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND A.DEALER_ID = ("+dealerId+")\n");  
		sql.append(" ORDER BY A.STATUS,A.ID DESC\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	public String getAddName(String regionCode) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT tmr.region_name FROM Tm_Region tmr WHERE 1=1  AND Region_Code=?") ;
		params.add(regionCode) ;
		
		List<Map<String, Object>> addList = dao.pageQuery(sql.toString(), params, dao.getFunName()) ;
		
		if(!CommonUtils.isNullList(addList))
			return regionCode == "" ? "" : addList.get(0).get("REGION_NAME").toString();
		else 
			return "" ;
	}
	
	public Map<String, Object> queryAddName(String addressId) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tva.province_id, tva.city_id, tva.area_id from tm_vs_address tva where tva.id = ? \n");
		params.add(addressId) ;
		
		Map<String, Object> addMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;
		
		return addMap ;
	}
	
	// 编码不规范导致的问题，所以改为一下sql查找最大索引
	public StringBuffer getMaxCode(String dealerCode, String dealerId) {
		StringBuffer maxCode = new StringBuffer("") ;
		
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer() ;
		
		sql.append("select nvl(max(to_number(substr(tva.add_code, instr(tva.add_code, '-', -1) + 1))), 0) max_index\n");
		sql.append("  from tm_vs_address tva\n");  
		sql.append(" where tva.add_code like ?\n");  
		params.add(dealerCode + "-%") ;
		
		sql.append("   and tva.dealer_id = ?\n");
		params.add(dealerId) ;

		Map<String, Object> addressMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;
		
		if(!CommonUtils.isNullMap(addressMap)) {
			maxCode.append(addressMap.get("MAX_INDEX").toString()) ;
		}
		
		return maxCode ;
	}
	
	public Map<String, Object> chkUpdateSame(Map<String, String> map) {
		String addressId = map.get("addressId") ;
		String address = map.get("address") ;
		String receiveOrg = map.get("receiveOrg") ;
		String addressUse = map.get("addressUse") ;
		String limitType = map.get("limitType") ;
		String limitStartDate = map.get("limitStartDate") ;
		String limitEndDate = map.get("limitEndDate") ;
		
		String proviceId = map.get("provice") ;
		String cityId = map.get("city") ;
		String areaId = map.get("area") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tva.id\n");
		sql.append("  from tm_vs_address tva\n");  
		sql.append(" where tva.id = ").append(addressId).append("\n");  
		sql.append("   and tva.address = '").append(address).append("'\n");  
		sql.append("   and tva.receive_org = '").append(receiveOrg).append("'\n");  
		sql.append("   and tva.address_use = '").append(addressUse).append("'\n");  
		sql.append("   and tva.limit_type = ").append(limitType).append("\n");  
		
		if(!CommonUtils.isNullString(proviceId))
			sql.append("   and tva.province_id = ").append(proviceId).append("\n"); 
		if(!CommonUtils.isNullString(cityId))
			sql.append("   and tva.city_id = ").append(cityId).append("\n");  
		if(!CommonUtils.isNullString(areaId))
			sql.append("   and tva.area_id = ").append(areaId).append("\n");  
		
		if(Constant.ADDRESS_TIME_LIMIT_TEMP.toString().equals(limitType)) {
			sql.append("   and tva.start_time = to_date('").append(limitStartDate).append("', 'yyyy-mm-dd')\n");  
			sql.append("   and tva.end_time = to_date('").append(limitEndDate).append("', 'yyyy-mm-dd')\n");
		}

		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> chkSame(Map<String, String> map) {
		String addressId = map.get("addressId") ;
		String address = map.get("address") ;
		String dealerId = map.get("dealerId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tva.id,tva.address,tva.dealer_id \n");
		sql.append("  from tm_vs_address tva \n");  
		sql.append(" where 1 = 1\n");  
//		sql.append("   and tva.b_area_id = tba.area_id(+)\n");  
		sql.append("   and tva.address = '").append(address).append("'\n");  
		sql.append("   and tva.dealer_id = ").append(dealerId).append("\n"); 
//		if(!CommonUtils.isNullString(addressId)) {
//			sql.append("   and tva.id <> ").append(addressId).append("\n");
//		}
		if(null != addressId && !"".equals(addressId)){
			sql.append("   and tva.id <> ").append(addressId).append("\n");
		}
		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> chkAddDlv(String addressId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvd.delivery_id, tvd.delivery_no\n");
		sql.append("  from tt_vs_dlvry tvd\n");  
		sql.append(" where tvd.address_id = ").append(addressId).append("\n");  
		sql.append("   and tvd.delivery_status in\n");  
		sql.append("       (").append(Constant.DELIVERY_STATUS_01).append(", ").append(Constant.DELIVERY_STATUS_02).append(", ").append(Constant.DELIVERY_STATUS_03).append(", ").append(Constant.DELIVERY_STATUS_06).append(", ").append(Constant.DELIVERY_STATUS_08).append(", ").append(Constant.DELIVERY_STATUS_13).append(")\n");

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> chkAddReq(String addressId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvdr.req_id, tvdr.dlvry_req_no\n");
		sql.append("  from tt_vs_dlvry_req tvdr\n");  
		sql.append(" where tvdr.address_id = ").append(addressId).append("\n");  
		sql.append("   and tvdr.req_status in (").append(Constant.ORDER_REQ_STATUS_01).append(",\n");  
		sql.append("                           ").append(Constant.ORDER_REQ_STATUS_02).append(",\n");  
		sql.append("                           ").append(Constant.ORDER_REQ_STATUS_03).append(",\n");  
		sql.append("                           ").append(Constant.ORDER_REQ_STATUS_05).append(",\n");  
		sql.append("                           ").append(Constant.ORDER_REQ_STATUS_08).append(",\n");  
		sql.append("                           ").append(Constant.ORDER_REQ_STATUS_YSH).append(",\n");  
		sql.append("                           ").append(Constant.ORDER_REQ_STATUS_DJCQR).append(")\n");


		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> chkAddOrder(String addressId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvo.order_id, tvo.order_no\n");
		sql.append("  from tt_vs_order tvo\n");  
		sql.append(" where tvo.delivery_address = ").append(addressId).append("\n");  
		sql.append("   and tvo.order_status in (").append(Constant.ORDER_STATUS_01).append(", ").append(Constant.ORDER_STATUS_02).append(", ").append(Constant.ORDER_STATUS_04).append(")\n");

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
