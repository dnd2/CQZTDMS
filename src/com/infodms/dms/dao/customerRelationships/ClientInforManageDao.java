package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import com.infodms.dms.bean.ClientSearchCondition;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class ClientInforManageDao extends BaseDao{

	private static final ClientInforManageDao dao = new ClientInforManageDao();
	
	public static final ClientInforManageDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String, Object>> queryClientCheckInfoLog(String order_id,String comm,Integer pageSize,
			Integer curPage){
		StringBuffer sql = new StringBuffer();
		String con = "";
		if(comm.equals("1")){//呼叫中心审核日志
			con = " and a.log_status like '1801%'" ;
		}else
			con = " and a.log_status like '1901%'" ;
		sql.append("select b.dealer_name,b.dealer_id,c.name,\r\n");
		sql.append("a.remark,a.log_date,decode(log_status,18011001,'未审核',\r\n");
		sql.append("      18011002,'已审核', 18011003, '已驳回', 19011002, '已审核',\r\n");
		sql.append("      19011003, '已驳回', 19011001, '未审核') as log_status_desc, log_status\r\n");
		sql.append("  from tt_dealer_actual_sales_log a,tt_dealer_actual_sales     b,tc_user c,\r\n");
		sql.append("  tc_code d where a.order_id = b.order_id  and a.user_id = c.user_id\r\n");
		sql.append("  and d.code_id = a.log_status and a.order_id = "+order_id+" \r\n").append(con);
		sql.append("  order by a.log_date desc");
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	public PageResult<Map<String, Object>> queryClientInforManage(
			ClientSearchCondition clientSearchCondition, Integer pageSize,
			Integer curPage) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select sales.order_id ORDERID,sales.CALLCENTER_CHECK_STATUS,customer.ctm_name CTMNAME, customer.main_phone PHONE,vehicle.vin VIN,mgroup.group_name MODELNAME, ");
		// 艾春 2013.12.3 修改销售经销商的取值
		sql.append(" pro.region_name PROVINCE,city.region_name CITY,tc.code_desc GUESTSTARS,town.region_name TOWN,NVL(dealer.dealer_name,sales.dealer_name) DEALERNAME, TO_CHAR(VEHICLE.PURCHASED_DATE, 'YYYY-MM-DD') BUYDATE ");
		sql.append(" from tt_customer customer ");	
		sql.append("  LEFT JOIN TT_DEALER_ACTUAL_SALES SALES ON SALES.CTM_ID = CUSTOMER.CTM_ID\r\n" );
		sql.append("  LEFT JOIN TM_VEHICLE VEHICLE ON VEHICLE.VEHICLE_ID = SALES.VEHICLE_ID\r\n" );
		sql.append("  LEFT JOIN TM_DEALER DEALER ON DEALER.DEALER_ID = SALES.DEALER_ID\r\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP MGROUP ON MGROUP.GROUP_ID = VEHICLE.MODEL_ID AND MGROUP.GROUP_LEVEL = 3\r\n" );
		sql.append("  LEFT JOIN TM_REGION PRO ON PRO.REGION_CODE = CUSTOMER.PROVINCE AND PRO.REGION_TYPE = '"+Constant.REGION_TYPE_02+"'\r\n" );
		sql.append("  LEFT JOIN TM_REGION CITY ON CITY.REGION_CODE = CUSTOMER.CITY AND CITY.REGION_TYPE = '"+Constant.REGION_TYPE_03+"'\r\n" );
		sql.append(" left join tm_region town on town.region_code = customer.town ");
		sql.append(" left join tc_code tc on tc.code_id = customer.guest_stars");
		sql.append(" where 1=1 and sales.is_return = '"+Constant.IF_TYPE_NO+"' ");

		if(StringUtil.notNull(clientSearchCondition.getPurchasedDateStart())){
			sql.append(" and vehicle.purchased_date >= to_date('"+clientSearchCondition.getPurchasedDateStart()+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(clientSearchCondition.getPurchasedDateEnd())){
			sql.append(" and vehicle.purchased_date<= to_date('"+clientSearchCondition.getPurchasedDateEnd()+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(clientSearchCondition.getCtmName())){
			sql.append("  and customer.ctm_name  like '%"+clientSearchCondition.getCtmName()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getTel())){
			sql.append(" and customer.main_phone like '%"+clientSearchCondition.getTel()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getYieldly())){
			sql.append(" and vehicle.yieldly = '"+clientSearchCondition.getYieldly()+"'");
		}
		if(StringUtil.notNull(clientSearchCondition.getSeries())){
			sql.append(" and vehicle.series_id = '"+clientSearchCondition.getSeries()+"'");
		}
		if(StringUtil.notNull(clientSearchCondition.getModel())){
			sql.append(" and mgroup.group_name like '%"+clientSearchCondition.getModel()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getEngineNo())){
			sql.append(" and vehicle.engine_no like '%"+clientSearchCondition.getEngineNo()+"%'");
		}		
		if(StringUtil.notNull(clientSearchCondition.getVin())){
			sql.append(" and vehicle.vin like '%"+clientSearchCondition.getVin()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getGuestStars())){
			sql.append(" and customer.guest_stars like '%"+clientSearchCondition.getGuestStars()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getProvince())){
			sql.append(" and pro.region_code like '%"+clientSearchCondition.getProvince()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getCity())){
			sql.append("   and city.region_code like '%"+clientSearchCondition.getCity()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getUse())){
			sql.append("   and sales.CAR_CHARACTOR = "+clientSearchCondition.getUse());
		}
		sql.append(" ORDER BY SALES.INVOICE_DATE DESC");
		
		
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	public List<Map<String, Object>> queryClientInforManage(
			ClientSearchCondition clientSearchCondition) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from (");
		sql.append(" select sales.order_id ORDERID,customer.ctm_name CTMNAME, customer.main_phone PHONE,vehicle.vin VIN,mgroup.group_name MODELNAME, ");
		sql.append(" pro.region_name PROVINCE,city.region_name CITY,customer.guest_stars GUESTSTARS,town.region_name TOWN,dealer.dealer_name DEALERNAME ");
		sql.append(" from tt_customer customer ");
		sql.append("  LEFT JOIN TT_DEALER_ACTUAL_SALES SALES ON SALES.CTM_ID = CUSTOMER.CTM_ID\r\n" );
		sql.append("  LEFT JOIN TM_VEHICLE VEHICLE ON VEHICLE.VEHICLE_ID = SALES.VEHICLE_ID\r\n" );
		sql.append("  LEFT JOIN TM_DEALER DEALER ON DEALER.DEALER_ID = SALES.DEALER_ID\r\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP MGROUP ON MGROUP.GROUP_ID = VEHICLE.MODEL_ID AND MGROUP.GROUP_LEVEL = 3\r\n" );
		sql.append("  LEFT JOIN TM_REGION PRO ON PRO.REGION_CODE = CUSTOMER.PROVINCE AND PRO.REGION_TYPE = '"+Constant.REGION_TYPE_02+"'\r\n" );
		sql.append("  LEFT JOIN TM_REGION CITY ON CITY.REGION_CODE = CUSTOMER.CITY AND CITY.REGION_TYPE = '"+Constant.REGION_TYPE_03+"'\r\n" );		
		sql.append(" left join tm_region town on town.region_code = customer.town ");
		sql.append(" where 1=1 and sales.is_return = '"+Constant.IF_TYPE_NO+"' ");

		if(StringUtil.notNull(clientSearchCondition.getPurchasedDateStart())){
			sql.append(" and vehicle.purchased_date >= to_date('"+clientSearchCondition.getPurchasedDateStart()+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(clientSearchCondition.getPurchasedDateEnd())){
			sql.append(" and vehicle.purchased_date<= to_date('"+clientSearchCondition.getPurchasedDateEnd()+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(clientSearchCondition.getCtmName())){
			sql.append("  and customer.ctm_name  like '%"+clientSearchCondition.getCtmName()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getTel())){
			sql.append(" and customer.main_phone like '%"+clientSearchCondition.getTel()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getYieldly())){
			sql.append(" and vehicle.yieldly like '%"+clientSearchCondition.getYieldly()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getSeries())){
			sql.append(" and vehicle.series_id like '%"+clientSearchCondition.getSeries()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getModel())){
			sql.append("  and mgroup.group_name like '%"+clientSearchCondition.getModel()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getEngineNo())){
			sql.append(" and vehicle.engine_no like '%"+clientSearchCondition.getEngineNo()+"%'");
		}		
		if(StringUtil.notNull(clientSearchCondition.getVin())){
			sql.append(" and vehicle.vin like '%"+clientSearchCondition.getVin()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getGuestStars())){
			sql.append(" customer.guest_stars like '%"+clientSearchCondition.getGuestStars()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getProvince())){
			sql.append(" and pro.region_code like '%"+clientSearchCondition.getProvince()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getCity())){
			sql.append("   and city.region_code like '%"+clientSearchCondition.getCity()+"%'");
		}
		sql.append(" ) where ROWNUM <10001 ");
		return pageQuery(sql.toString(), null, getFunName());
	}
	
	public int countClientInforManage(ClientSearchCondition clientSearchCondition){
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT COUNT(CUSTOMER.CTM_ID) ORDERID\r\n" );
		sql.append("  FROM TT_CUSTOMER CUSTOMER\r\n" );
		sql.append("  LEFT JOIN TT_DEALER_ACTUAL_SALES SALES ON SALES.CTM_ID = CUSTOMER.CTM_ID\r\n" );
		sql.append("  LEFT JOIN TM_VEHICLE VEHICLE ON VEHICLE.VEHICLE_ID = SALES.VEHICLE_ID\r\n" );
		sql.append("  LEFT JOIN TM_DEALER DEALER ON DEALER.DEALER_ID = SALES.DEALER_ID\r\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP MGROUP ON MGROUP.GROUP_ID = VEHICLE.MODEL_ID AND MGROUP.GROUP_LEVEL = 3\r\n" );
		sql.append("  LEFT JOIN TM_REGION PRO ON PRO.REGION_CODE = CUSTOMER.PROVINCE AND PRO.REGION_TYPE = '"+Constant.REGION_TYPE_02+"'\r\n" );
		sql.append("  LEFT JOIN TM_REGION CITY ON CITY.REGION_CODE = CUSTOMER.CITY AND CITY.REGION_TYPE = '"+Constant.REGION_TYPE_03+"'\r\n" );
		sql.append(" WHERE SALES.IS_RETURN = '"+Constant.IF_TYPE_NO+"'");

		if(StringUtil.notNull(clientSearchCondition.getPurchasedDateStart())){
			sql.append(" and vehicle.purchased_date >= to_date('"+clientSearchCondition.getPurchasedDateStart()+"','yyyy-MM-dd') ");
		}
		if(StringUtil.notNull(clientSearchCondition.getPurchasedDateEnd())){
			sql.append(" and vehicle.purchased_date<= to_date('"+clientSearchCondition.getPurchasedDateEnd()+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtil.notNull(clientSearchCondition.getCtmName())){
			sql.append("  and customer.ctm_name  like '%"+clientSearchCondition.getCtmName()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getTel())){
			sql.append(" and customer.main_phone like '%"+clientSearchCondition.getTel()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getYieldly())){
			sql.append(" and vehicle.yieldly like '%"+clientSearchCondition.getYieldly()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getSeries())){
			sql.append(" and vehicle.series_id like '%"+clientSearchCondition.getSeries()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getModel())){
			sql.append("  and mgroup.group_name like '%"+clientSearchCondition.getModel()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getEngineNo())){
			sql.append(" and vehicle.engine_no like '%"+clientSearchCondition.getEngineNo()+"%'");
		}		
		if(StringUtil.notNull(clientSearchCondition.getVin())){
			sql.append(" and vehicle.vin like '%"+clientSearchCondition.getVin()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getGuestStars())){
			sql.append(" and customer.guest_stars like '%"+clientSearchCondition.getGuestStars()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getProvince())){
			sql.append(" and pro.region_code like '%"+clientSearchCondition.getProvince()+"%'");
		}
		if(StringUtil.notNull(clientSearchCondition.getCity())){
			sql.append("   and city.region_code like '%"+clientSearchCondition.getCity()+"%'");
		}
		List list = this.selectTmDataSet(sql, "ORDERID");
		if(list != null && list.size()>0) return Integer.parseInt((String)list.get(0));
		return 0;
	}
	
	public Map<String,Object> getClientInforByOrderId(Long orderId){
		StringBuffer sql = new StringBuffer();
		sql.append("select customer.ctm_name CTMNAME,\r\n");
		sql.append("       tctype.code_desc CTMTYPE,\r\n");
		sql.append("       tcss.code_desc SALESADDRESS,\r\n");
		sql.append("       customer.card_num CARDNUM,\r\n");
		sql.append("       pro.region_name PROVINCE,\r\n");
		sql.append("       city.region_name CITY,\r\n");
		sql.append("       customer.post_code POSTCODE,\r\n");
		sql.append("       customer.address ADDRESS,\r\n");
		sql.append("       guestCode.code_desc GUESTSTARS,\r\n");
		sql.append("       customer.company_phone COMPHONE,\r\n");
		sql.append("       customer.other_phone OTHERPHONE,\r\n");
		sql.append("       customer.main_phone PHONE,\r\n");
		sql.append("       sgroup.group_name SERIESNAME,\r\n");
		sql.append("       mgroup.group_name MODELNAME,\r\n");
		sql.append("       vehicle.color COLOR,\r\n");
		sql.append("       tba.area_name YIELDLY,\r\n");
		sql.append("       pgroup.group_name PACKAGENAME,\r\n");
		sql.append("       dpro.region_name AREA,\r\n");
		sql.append("       vehicle.engine_no ENGINENO,\r\n");
		sql.append("       vehicle.vin VIN,\r\n");
		sql.append("       to_char(vehicle.Product_Date, 'yyyy-MM-dd') PDATE,\r\n");
		sql.append("       to_char(vehicle.purchased_date, 'yyyy-MM-dd') PUDATE,\r\n");
		sql.append("       sales.price PRICE,\r\n");
		sql.append("       material.material_code MATERIALCODE,\r\n");
		sql.append("       dealer.dealer_name DEALERNAME,\r\n");
		sql.append("       dcode.code_desc DLEVEL\r\n");
		sql.append("  from tt_customer customer\r\n");
		sql.append("  left join TT_DEALER_ACTUAL_SALES sales\r\n");
		sql.append("    on sales.ctm_id = customer.ctm_id\r\n");
		sql.append("  left join tm_vehicle vehicle\r\n");
		sql.append("    on vehicle.vehicle_id = sales.vehicle_id\r\n");
		sql.append("  left join tm_dealer dealer\r\n");
		sql.append("    on dealer.dealer_id = sales.dealer_id\r\n");
		sql.append("  left join tm_vhcl_material_group mgroup\r\n");
		sql.append("    on mgroup.group_id = vehicle.model_id\r\n");
		sql.append("  left join tm_vhcl_material_group sgroup\r\n");
		sql.append("    on sgroup.group_id = vehicle.series_id\r\n");
		sql.append("  left join tm_vhcl_material_group pgroup\r\n");
		sql.append("    on pgroup.group_id = vehicle.package_id\r\n");
		sql.append("  left join tm_vhcl_material material\r\n");
		sql.append("    on material.material_id = vehicle.material_id\r\n");
		sql.append("  left join tm_business_area tba\r\n");
		sql.append("    on tba.area_id = vehicle.yieldly\r\n");
		sql.append("  left join tm_region dpro\r\n");
		sql.append("    on dpro.region_code = dealer.province_id\r\n");
		sql.append("  left join tm_region pro\r\n");
		sql.append("    on pro.region_code = customer.province\r\n");
		sql.append("  left join tm_region city\r\n");
		sql.append("    on city.region_code = customer.city"); 
		sql.append("  left join tc_code tctype\r\n");
		sql.append("    on tctype.code_id = customer.ctm_type\r\n");
		sql.append("  left join tc_code tcss\r\n");
		sql.append("    on tcss.code_id = sales.sales_address\r\n");
		sql.append("  left join tc_code guestCode\r\n");
		sql.append("    on guestCode.code_id = customer.guest_stars\r\n");
		sql.append("  left join tc_code dcode\r\n");
		sql.append("    on dealer.DEALER_LEVEL = dcode.code_id\r\n");
		sql.append(" where sales.order_id = "+orderId);
		return this.pageQueryMap(sql.toString(), null, getFunName());
	}

	public Map<String,Object> getClientInforByCtmidAndVinAndCpid(long ctmid, String vin,long cpid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select complaint.cp_name CTMNAME,\r\n");
		sql.append("       tctype.code_desc CTMTYPE,\r\n");
		sql.append("       tcss.code_desc SALESADDRESS,\r\n");
		sql.append("       customer.card_num CARDNUM,\r\n");
		sql.append("       pro.region_name PROVINCE,\r\n");
		sql.append("       city.region_name CITY,\r\n");
		sql.append("       customer.post_code POSTCODE,\r\n");
		sql.append("       customer.address ADDRESS,\r\n");
		sql.append("       guestCode.code_desc GUESTSTARS,\r\n");
		sql.append("       customer.company_phone COMPHONE,\r\n");
		sql.append("       customer.other_phone OTHERPHONE,\r\n");
		sql.append("       complaint.cp_phone PHONE,\r\n");
		sql.append("       sgroup.group_name SERIESNAME,\r\n");
		sql.append("       mgroup.group_name MODELNAME,\r\n");
		sql.append("       vehicle.color COLOR,\r\n");
		sql.append("       tba.area_name YIELDLY,\r\n");
		sql.append("       pgroup.group_name PACKAGENAME,\r\n");
		sql.append("       dpro.region_name AREA,\r\n");
		sql.append("       vehicle.engine_no ENGINENO,\r\n");
		sql.append("       vehicle.vin VIN,\r\n");
		sql.append("       to_char(vehicle.Product_Date, 'yyyy-MM-dd') PDATE,\r\n");
		sql.append("       to_char(vehicle.purchased_date, 'yyyy-MM-dd') PUDATE,\r\n");
		sql.append("       sales.price PRICE,\r\n");
		sql.append("       material.material_code MATERIALCODE,\r\n");
		sql.append("       dealer.dealer_name DEALERNAME\r\n");
		sql.append("  from TT_CRM_COMPLAINT complaint \r\n");
		sql.append("  left join tt_customer customer \r\n");
		sql.append("    on complaint.cp_cus_id = customer.ctm_id\r\n");
		sql.append("  left join TT_DEALER_ACTUAL_SALES sales\r\n");
		sql.append("    on sales.ctm_id = customer.ctm_id\r\n");
		sql.append("  left join tm_vehicle vehicle\r\n");
		sql.append("    on vehicle.vehicle_id = sales.vehicle_id\r\n");
		sql.append("  left join tm_dealer dealer\r\n");
		sql.append("    on dealer.dealer_id = sales.dealer_id\r\n");
		sql.append("  left join tm_vhcl_material_group mgroup\r\n");
		sql.append("    on mgroup.group_id = vehicle.model_id\r\n");
		sql.append("  left join tm_vhcl_material_group sgroup\r\n");
		sql.append("    on sgroup.group_id = vehicle.series_id\r\n");
		sql.append("  left join tm_vhcl_material_group pgroup\r\n");
		sql.append("    on pgroup.group_id = vehicle.package_id\r\n");
		sql.append("  left join tm_vhcl_material material\r\n");
		sql.append("    on material.material_id = vehicle.material_id\r\n");
		sql.append("  left join tm_business_area tba\r\n");
		sql.append("    on tba.area_id = vehicle.yieldly\r\n");
		sql.append("  left join tm_region dpro\r\n");
		sql.append("    on dpro.region_code = dealer.province_id\r\n");
		sql.append("  left join tm_region pro\r\n");
		sql.append("    on pro.region_code = complaint.CP_PROVINCE_ID\r\n");
		sql.append("  left join tm_region city\r\n");
		sql.append("    on city.region_code = complaint.CP_CITY_ID\r\n");
		sql.append("  left join tc_code tctype\r\n");
		sql.append("    on tctype.code_id = customer.ctm_type\r\n");
		sql.append("  left join tc_code tcss\r\n");
		sql.append("    on tcss.code_id = sales.sales_address\r\n");
		sql.append("  left join tc_code guestCode\r\n");
		sql.append("    on guestCode.code_id = customer.guest_stars\r\n");
		sql.append(" where complaint.cp_id="+cpid);
		
		if(ctmid != -1){
			sql.append("   and customer.ctm_id= '"+ctmid+"' \r\n");
			if(!"null".equals(vin)) sql.append("   and vehicle.vin= '"+vin+"' \r\n");
		}
		
		return this.pageQueryMap(sql.toString(), null, getFunName());
	}
	
	public Map<String,Object> getClientInforByCtmidAndVinAndCpidYx(long ctmid, String vin,long cpid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select complaint.cp_name CTMNAME,\r\n");
		sql.append("       tctype.code_desc CTMTYPE,\r\n");
		sql.append("       tcss.code_desc SALESADDRESS,\r\n");
		sql.append("       customer.card_num CARDNUM,\r\n");
		sql.append("       pro.region_name PROVINCE,\r\n");
		sql.append("       city.region_name CITY,\r\n");
		sql.append("       customer.post_code POSTCODE,\r\n");
		sql.append("       customer.address ADDRESS,\r\n");
		sql.append("       guestCode.code_desc GUESTSTARS,\r\n");
		sql.append("       customer.company_phone COMPHONE,\r\n");
		sql.append("       customer.other_phone OTHERPHONE,\r\n");
		sql.append("       complaint.cp_phone PHONE,\r\n");
		sql.append("       sgroup.group_name SERIESNAME,\r\n");
		sql.append("       mgroup.group_name MODELNAME,\r\n");
		sql.append("       vehicle.color COLOR,\r\n");
		sql.append("       tba.area_name YIELDLY,\r\n");
		sql.append("       pgroup.group_name PACKAGENAME,\r\n");
		sql.append("       dpro.region_name AREA,\r\n");
		sql.append("       vehicle.engine_no ENGINENO,\r\n");
		sql.append("       vehicle.vin VIN,\r\n");
		sql.append("       to_char(vehicle.Product_Date, 'yyyy-MM-dd') PDATE,\r\n");
		sql.append("       to_char(vehicle.purchased_date, 'yyyy-MM-dd') PUDATE,\r\n");
		sql.append("       sales.price PRICE,\r\n");
		sql.append("       material.material_code MATERIALCODE,\r\n");
		sql.append("       dealer.dealer_name DEALERNAME\r\n");
		sql.append("  from tt_crm_incoming_call complaint \r\n");
		sql.append("  left join tt_customer customer \r\n");
		sql.append("    on complaint.cp_cus_id = customer.ctm_id\r\n");
		sql.append("  left join TT_DEALER_ACTUAL_SALES sales\r\n");
		sql.append("    on sales.ctm_id = customer.ctm_id\r\n");
		sql.append("  left join tm_vehicle vehicle\r\n");
		sql.append("    on vehicle.vehicle_id = sales.vehicle_id\r\n");
		sql.append("  left join tm_dealer dealer\r\n");
		sql.append("    on dealer.dealer_id = sales.dealer_id\r\n");
		sql.append("  left join tm_vhcl_material_group mgroup\r\n");
		sql.append("    on mgroup.group_id = vehicle.model_id\r\n");
		sql.append("  left join tm_vhcl_material_group sgroup\r\n");
		sql.append("    on sgroup.group_id = vehicle.series_id\r\n");
		sql.append("  left join tm_vhcl_material_group pgroup\r\n");
		sql.append("    on pgroup.group_id = vehicle.package_id\r\n");
		sql.append("  left join tm_vhcl_material material\r\n");
		sql.append("    on material.material_id = vehicle.material_id\r\n");
		sql.append("  left join tm_business_area tba\r\n");
		sql.append("    on tba.area_id = vehicle.yieldly\r\n");
		sql.append("  left join tm_region dpro\r\n");
		sql.append("    on dpro.region_code = dealer.province_id\r\n");
		sql.append("  left join tm_region pro\r\n");
		sql.append("    on pro.region_code = complaint.CP_PROVINCE_ID\r\n");
		sql.append("  left join tm_region city\r\n");
		sql.append("    on city.region_code = complaint.CP_CITY_ID\r\n");
		sql.append("  left join tc_code tctype\r\n");
		sql.append("    on tctype.code_id = customer.ctm_type\r\n");
		sql.append("  left join tc_code tcss\r\n");
		sql.append("    on tcss.code_id = sales.sales_address\r\n");
		sql.append("  left join tc_code guestCode\r\n");
		sql.append("    on guestCode.code_id = customer.guest_stars\r\n");
		sql.append(" where complaint.cp_id="+cpid);
		
		if(ctmid != -1){
			sql.append("   and customer.ctm_id= '"+ctmid+"' \r\n");
			if(!"null".equals(vin)) sql.append("   and vehicle.vin= '"+vin+"' \r\n");
		}
		
		return this.pageQueryMap(sql.toString(), null, getFunName());
	}
	
}
