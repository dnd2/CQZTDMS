package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TtCrmComplaintPO;
import com.infodms.dms.po.TtCrmSeatsTeamPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

@SuppressWarnings("unchecked")
public class IncomingAlertScreenDao extends BaseDao{

	private static final IncomingAlertScreenDao dao = new IncomingAlertScreenDao();
	
	public static final IncomingAlertScreenDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String,Object>> queryIncomingAlertScreen(String incomeTep,
			String vin,String name,String telephone,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT CUSTOMER.CTM_ID     CTMID,\n" );
		sql.append("       SALES.ORDER_ID      ORDERID,\n" );
		sql.append("       CUSTOMER.CTM_NAME   CTMNAME,\n" );
		sql.append("       CUSTOMER.MAIN_PHONE PHONE,\n" );
		sql.append("       CTYPE.CODE_DESC     CTMTYPE,\n" );
		sql.append("       PRO.REGION_NAME     PROVINCE,\n" );
		sql.append("       CITY.REGION_NAME    CITY,\n" );
		sql.append("       GUESTCODE.CODE_DESC GUESTSTARS,\n" );
		sql.append("       MGROUP.GROUP_NAME   MODELNAME,\n" );
		sql.append("       VEHICLE.VIN         VIN,\n" );
		sql.append("       DEALER.DEALER_NAME  DEALERNAME\n" );
		sql.append("  FROM TT_CUSTOMER CUSTOMER\n" );
		sql.append("  LEFT JOIN TT_DEALER_ACTUAL_SALES SALES ON SALES.CTM_ID = CUSTOMER.CTM_ID\n" );
		sql.append("  LEFT JOIN TM_VEHICLE VEHICLE ON VEHICLE.VEHICLE_ID = SALES.VEHICLE_ID\n" );
		sql.append("  LEFT JOIN TM_DEALER DEALER ON DEALER.DEALER_ID = VEHICLE.DEALER_ID\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP MGROUP ON MGROUP.GROUP_ID = VEHICLE.MODEL_ID AND MGROUP.Group_Level = 3\n" );
		sql.append("  LEFT JOIN TM_REGION PRO ON PRO.REGION_CODE = CUSTOMER.PROVINCE AND pro.region_type = "+Constant.REGION_TYPE_02+"\n" );
		sql.append("  LEFT JOIN TM_REGION CITY ON CITY.REGION_CODE = CUSTOMER.CITY AND CITY.region_type = "+Constant.REGION_TYPE_03+"\n" );
		sql.append("  LEFT JOIN TC_CODE CTYPE ON CTYPE.CODE_ID = CUSTOMER.CTM_TYPE AND ctype.TYPE = "+Constant.CUSTOMER_TYPE+"\n" );
		sql.append("  LEFT JOIN TC_CODE GUESTCODE ON GUESTCODE.CODE_ID = CUSTOMER.GUEST_STARS AND GUESTCODE.TYPE = "+Constant.GUEST_STARS+"\n" );
		sql.append(" WHERE SALES.IS_RETURN = "+Constant.IF_TYPE_NO+" \n");
		if(StringUtil.notNull(incomeTep)){
			String incomeTepDeal = incomeTep.substring(1, incomeTep.length());
			if(StringUtil.notNull(incomeTepDeal)){
				sql.append(" and (customer.company_phone like '%"+incomeTepDeal+"%' or customer.other_phone like '%"+incomeTepDeal+"%' or customer.main_phone  like '%"+incomeTepDeal+"%') ");
			}			
		}
		if(StringUtil.notNull(vin)){
			sql.append(" and vehicle.vin like '%"+vin+"%'");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and customer.ctm_name like '%"+name+"%'");
		}
		if(StringUtil.notNull(telephone)){
			sql.append(" and (customer.company_phone like '%"+telephone+"%' or customer.other_phone like '%"+telephone+"%' or customer.main_phone  like '%"+telephone+"%') ");
		}
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	
	public List<DynaBean> queryIncomingAlertScreenNew01(String incomeTep,
			String vin,String name,String telephone){
		StringBuffer sql= new StringBuffer();
		// 艾春9.25添加参数预编译信息
		List<String> list = new ArrayList<String>();
		sql.append("SELECT T.CTM_ID     CTMID,\n");
		sql.append("      T.ORDER_ID   ORDERID,\n"); 
		sql.append("      T.CTM_NAME   CTMNAME,\n"); 
		sql.append("      T.MAIN_PHONE PHONE,\n"); 
		sql.append("      T.CTM_TYPE    CTMTYPE,\n"); 
		sql.append("      T.PROVINCE   PROVINCE,\n"); 
		sql.append("      T.CITY       CITY,\n"); 
		sql.append("      T.RO_DATE  RO_DATE,\n"); 
		sql.append("      T.RO_DNAME       RO_DNAME,\n"); 
		sql.append("      T.BUYDATE       BUYDATE,\n"); 
		sql.append("      T.GUESTSTARS GUESTSTARS,\n"); 
		sql.append("      T.MODELNAME  MODELNAME,\n"); 
		sql.append("      T.VIN        VIN,\n"); 
		sql.append("      T.DEALERNAME DEALERNAME\n"); 
		sql.append("  FROM TT_CRM_CUSTOMER T\n"); 
		sql.append("  WHERE 1 = 1 AND ROWNUM <= 15  \n");
		if(StringUtil.notNull(incomeTep)){
			String incomeTepDeal = "";
			// 判断来电号码是座机还是手机
			int flag = incomeTep.indexOf("-");
			
			// 如果flag是-1, 则表示是手机, 否则是座机
			if(-1!=flag){
				incomeTepDeal = incomeTep.substring(flag+1);
			}else{
				incomeTepDeal = incomeTep.substring(1, incomeTep.length());
			}
			
			if(StringUtil.notNull(incomeTepDeal)){
				// 艾春 11.08 修改来电号码查询方式：从第二位开始取值，在数据库中模糊查询,但是会导致查询速度可能会慢
//				sql.append(" and t.phone = ? ");
//				list.add(incomeTepDeal);
				sql.append(" and t.phone like ? ");
				list.add("%"+incomeTepDeal+"%");
			}			
		}
		if(StringUtil.notNull(vin)){
			sql.append(" and t.vin like ? ");
			list.add("%"+vin+"%");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and t.ctm_name like ?");
			list.add("%"+name+"%");
		}
		if(StringUtil.notNull(telephone)){
			sql.append(" and (t.company_phone like ? or t.other_phone like ? or t.main_phone  like ?) ");
			list.add("%"+telephone+"%");
			list.add("%"+telephone+"%");
			list.add("%"+telephone+"%");
		}
		sql.append("  order by t.buydate desc \n");
		return (List<DynaBean>)this.pageQuery(sql.toString(),
				list,
				this.getFunName());
	}

	
	
	
	/**
	 * 艾春 9.25 添加 来电弹屏查询每日定时获取的客户信息
	 * @param incomeTep 呼入号码
	 * @param vin 车架号
	 * @param name 客户名字
	 * @param telephone 联系电话
	 * @param pageSize 分页
	 * @param curPage 当前页
	 * @return
	 */
	public PageResult<Map<String,Object>> queryIncomingAlertScreenNew(String incomeTep,
			String vin,String name,String telephone,String engineNo,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();
		// 艾春9.25添加参数预编译信息
		List<String> list = new ArrayList<String>();
		sql.append("SELECT T.CTM_ID     CTMID,\n");
		sql.append("      T.ORDER_ID   ORDERID,\n"); 
		sql.append("      T.CTM_NAME   CTMNAME,\n"); 
		sql.append("      T.MAIN_PHONE PHONE,\n"); 
		sql.append("      T.CTM_TYPE    CTMTYPE,\n"); 
		sql.append("      T.PROVINCE   PROVINCE,\n"); 
		sql.append("      T.CITY       CITY,\n"); 
		sql.append("      T.RO_DATE  RO_DATE,\n"); 
		sql.append("      T.RO_DNAME       RO_DNAME,\n"); 
		sql.append("      T.BUYDATE       BUYDATE,\n"); 
		sql.append("      T.GUESTSTARS GUESTSTARS,\n"); 
		sql.append("      T.MODELNAME  MODELNAME,\n"); 
		sql.append("      T.VIN        VIN,\n"); 
		sql.append("      T.ENGINE_NO  ENGINE_NO,\n"); 
		sql.append("      T.DEALERNAME DEALERNAME\n"); 
		sql.append("  FROM TT_CRM_CUSTOMER T\n"); 
		sql.append("  WHERE 1 = 1 \n");
		if(StringUtil.notNull(incomeTep)){
			String incomeTepDeal = "";
			// 判断来电号码是座机还是手机
			int flag = incomeTep.indexOf("-");
			
			// 如果flag是-1, 则表示是手机, 否则是座机
			if(-1!=flag){
				incomeTepDeal = incomeTep.substring(flag+1);
			}else{
				incomeTepDeal = incomeTep.substring(1, incomeTep.length());
			}
			
			if(StringUtil.notNull(incomeTepDeal)){
				// 艾春 11.08 修改来电号码查询方式：从第二位开始取值，在数据库中模糊查询,但是会导致查询速度可能会慢
//				sql.append(" and t.phone = ? ");
//				list.add(incomeTepDeal);
				sql.append(" and t.phone like ? ");
				list.add("%"+incomeTepDeal+"%");
			}			
		}
		if(StringUtil.notNull(vin)){
			sql.append(" and t.vin like ? ");
			list.add("%"+vin+"%");
		}
		if(StringUtil.notNull(name)){
			sql.append(" and t.ctm_name like ?");
			list.add("%"+name+"%");
		}
		if(StringUtil.notNull(telephone)){
			sql.append(" and (t.company_phone like ? or t.other_phone like ? or t.main_phone  like ?) ");
			list.add("%"+telephone+"%");
			list.add("%"+telephone+"%");
			list.add("%"+telephone+"%");
		}
		if (StringUtil.notNull(engineNo)) {
			sql.append(" and T.ENGINE_NO like ?");
			list.add("%"+engineNo+"%");
		}
		sql.append("  order by t.buydate desc \n");
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				list,
				this.getFunName(),
				pageSize,
				curPage);
	}

	public Map<String,Object> queryIncomingAlertScreenInfor(String ctmid,String orderid){
		StringBuffer sql = new StringBuffer();
		// 艾春9.25添加参数预编译信息
		List<Long> list = new ArrayList<Long>();
		sql.append(" select customer.ctm_id CTMID,customer.ctm_name CTMNAME,customer.ctm_type CTMTYPE,sales.sales_address SALESADDRESS, customer.card_num CARDNUM, ");
		sql.append(" customer.province PROVINCE,customer.city CITY,customer.post_code POSTCODE,customer.address ADDRESS, ");
		sql.append(" customer.guest_stars GUESTSTARS,customer.company_phone COMPHONE,customer.other_phone OTHERPHONE, customer.main_phone PHONE, ");
		sql.append(" sgroup.group_name SERIESNAME,mgroup.group_name MODELNAME,vehicle.color COLOR, ");
		sql.append(" tba.area_name YIELDLY,pgroup.group_name PACKAGENAME,dpro.region_name AREA, vehicle.engine_no ENGINENO, ");
		sql.append(" vehicle.vin VIN,to_char(vehicle.Product_Date,'yyyy-MM-dd') PDATE,to_char(vehicle.purchased_date,'yyyy-MM-dd') PUDATE,sales.price PRICE, ");
		// 艾春 2013.12.5 修改 在经销商表中根据Dealer_ID查询的时候,如果查询不到,则直接取实销表中老经销商名称
		sql.append(" material.material_code MATERIALCODE, NVL(dealer.dealer_name,sales.dealer_name) DEALERNAME ");
		// 艾春 2013.12.5 修改 在经销商表中根据Dealer_ID查询的时候,如果查询不到,则直接取实销表中老经销商名称
		sql.append(" from tt_customer customer ");
		sql.append(" left join TT_DEALER_ACTUAL_SALES sales on sales.ctm_id = customer.ctm_id ");
		sql.append(" left join tm_vehicle vehicle on vehicle.vehicle_id = sales.vehicle_id ");
		sql.append(" left join tm_dealer dealer on dealer.dealer_id = sales.dealer_id ");
		sql.append(" left join tm_vhcl_material_group mgroup on mgroup.group_id = vehicle.model_id ");
		sql.append(" left join tm_vhcl_material_group sgroup on sgroup.group_id = vehicle.series_id ");
		sql.append(" left join tm_vhcl_material_group pgroup on pgroup.group_id = vehicle.package_id ");
		sql.append(" left join tm_vhcl_material material on material.material_id = vehicle.material_id ");
		sql.append(" left join tm_business_area tba on tba.area_id = vehicle.yieldly ");
		sql.append(" left join tm_region dpro on dpro.region_code = dealer.province_id ");
		//sql.append(" left join tm_region pro on pro.region_code = customer.province ");
		//sql.append(" left join tm_region city on city.region_code = customer.city ");
		sql.append(" where customer.ctm_id = ?\n");
		list.add(Long.parseLong(ctmid));
		
		if(StringUtil.notNull(orderid)){
			sql.append(" and sales.order_id = ?\n");
			list.add(Long.parseLong(orderid));
		}

		return this.pageQueryMap(sql.toString(), list, this.getFunName());
	}
	/**
	 * 客户查询方法  
	 * @param ctmid 客户ID
	 * @param orderid 实销表ID
	 * @return  Map<String, Object>
	 */
	public Map<String, Object> queryCustomerInfor(String ctmid, String orderid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select crm.engine_no ENGINENO,customer.ctm_id CTMID,customer.ctm_name CTMNAME,customer.main_phone PHONE, ");
		sql.append(" vehicle.vin VIN,vehicle.MILEAGE MILEAGE,customer.MILEAGE_RANGE MILEAGERANGE,sales.sales_address SALESADDRESS,customer.province PROVINCE,customer.city CITY, ");
		sql.append(" vehicle.series_id SERIESID,vehicle.model_id MODELID, ");
		sql.append(" to_char(vehicle.purchased_date,'yyyy-MM-dd') PUDATE,to_char(vehicle.Product_Date,'yyyy-MM-dd') PDATE ");
		sql.append(" from tt_customer customer ");
		sql.append(" left join TT_DEALER_ACTUAL_SALES sales on sales.ctm_id = customer.ctm_id ");
		sql.append(" left join tt_crm_customer crm on crm.ctm_id = customer.ctm_id ");
		sql.append(" left join tm_vehicle vehicle on vehicle.vehicle_id = sales.vehicle_id ");
		sql.append(" where customer.ctm_id = "+ctmid);
		sql.append(" and sales.order_id = " + orderid);
		return this.pageQueryMap(sql.toString(), null, this.getFunName());
	}
	/**
	 * 客户查询方法  
	 * @param vin 汽车底盘号
	 * @return  Map<String, Object>
	 */
	public Map<String, Object> queryCustomerInfor(String vin) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select customer.ctm_id CTMID,customer.ctm_name CTMNAME,customer.main_phone PHONE, ");
		sql.append(" vehicle.vin VIN,vehicle.MILEAGE MILEAGE,customer.MILEAGE_RANGE MILEAGERANGE,sales.sales_address SALESADDRESS,customer.province PROVINCE,customer.city CITY, ");
		sql.append(" vehicle.series_id SERIESID,vehicle.model_id MODELID, ");
		sql.append(" to_char(vehicle.purchased_date,'yyyy-MM-dd') PUDATE,to_char(vehicle.Product_Date,'yyyy-MM-dd') PDATE ");
		sql.append(" from tt_customer customer ");
		sql.append(" join TT_DEALER_ACTUAL_SALES sales on sales.ctm_id = customer.ctm_id ");
		sql.append(" join tm_vehicle vehicle on vehicle.vehicle_id = sales.vehicle_id ");
		sql.append(" where sales.is_return = "+Constant.IF_TYPE_NO+" and vehicle.vin = '"+vin+"'");
		return this.pageQueryMap(sql.toString(), null, this.getFunName());
	}
	
	public Map<String, Object> queryVin(String vin) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tm_vehicle vehicle where vehicle.vin = '"+vin+"'");
		return this.pageQueryMap(sql.toString(), null, this.getFunName());
	}
	
	/**
	 * 更新坐席状态
	 * @param userId 用户ID
	 * @param seatsStatus 坐席状态
	 */
	public void updateSeatStatus(Long userId, Integer seatsStatus) {
		String sql = "UPDATE TT_CRM_SEATS T SET T.Se_Work_Status ="+seatsStatus+" WHERE T.SE_USER_ID ="+userId;
		this.update(sql, null);
	}
}
