package com.infodms.dms.dao.sales.dealer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: CHANADMS
 *
 * @Description:
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company:  www.infoservice.com.cn
 * @Date: 2014-3-4
 *
 * @author yupeng 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class DealerAddressInfoDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(DealerAddressInfoDao.class);
	//private static POFactory factory = POFactoryBuilder.getInstance();
	private static DealerAddressInfoDao dao = new DealerAddressInfoDao ();
	public static final DealerAddressInfoDao getInstance() {
		if (dao == null) {
			dao = new DealerAddressInfoDao();
		}
		return dao;
	}
	private DealerAddressInfoDao() {}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * 根据经销商编号获取经销商地址信息
	 * @param dealer_id
	 * @param mobile_phone 
	 * @param tel 
	 * @param link_man 
	 * @param status 
	 * @param address_type 
	 * @param sex 
	 * @param area 
	 * @param city 
	 * @param province 
	 * @param pageSize 
	 * @param curPage 
	 * @return
	 */
	public PageResult<Map<String, Object>> queryDealerAddressInfo(String dealer_id, String sex, String address_type,
			String status, String link_man, String tel, String mobile_phone, String province, String city, String area, Integer curPage, Integer pageSize) {
		StringBuilder sql = new StringBuilder(100);
		List<Object> params = new LinkedList<Object>();
		
		StringBuilder sql2 = new StringBuilder(50).append("");
		if(!province.equals("")) {
			sql2.append(" and province_id = "+ province);
		}
		
		if(!city.equals("")) {
			sql2.append(" and city_id = "+ city );
		}
		
		if(!area.equals("")) {
			sql2.append(" and area_id ="+ area);
		}
		
		params.add(dealer_id);
		sql.append("SELECT * FROM ( \n");
		sql.append(" SELECT dealer_id, address, sex, link_man, tel, mobile_phone, address_type, id, status \n");
		sql.append(" FROM tm_vs_address_more \n");
		sql.append(" UNION ALL \n");
		sql.append(" SELECT dealer_id, \n");
//		sql.append(" (SELECT region_name FROM tm_region WHERE region_code = province_id)|| \n");
//		sql.append(" (SELECT region_name FROM tm_region WHERE region_code = city_id)|| \n");
//		sql.append(" (SELECT region_name FROM tm_region WHERE region_code = area_id)|| \n");
		sql.append(" address as address, \n");
		sql.append(" sex, link_man, tel, mobile_phone, address_type, id, status \n");
		sql.append(" FROM tm_vs_address where 1=1"+ sql2.toString() +") a \n");
		sql.append(" WHERE a.dealer_id = ? \n");
		
		if(!sex.equals("")) {
			sql.append(" AND a.sex = ?");
			params.add(sex);
		}
		if(!address_type.equals("")) {
			sql.append(" AND a.address_type = ?");
			params.add(address_type);
		}
		if(!status.equals("")) {
			sql.append(" AND a.status = ?");
			params.add(status);
		}
		if(!link_man.equals("")) {
			sql.append(" AND a.link_man = ?");
			params.add(link_man);
		}
		if(!tel.equals("")) {
			sql.append(" AND a.tel = ?");
			params.add(tel);
		}
		if(!mobile_phone.equals("")) {
			sql.append(" AND a.mobile_phone = ?");
			params.add(mobile_phone);
		}
		sql.append(" order by a.status asc \n"); 

		return dao.pageQuery(sql.toString(), params, 
				"com.infodms.dao.sales.dealer.DealerAddressInfoDao.queryDealerAddressInfo", pageSize, curPage);
	}
	
	/**
	 * 根据经销商编号获取经销商地址信息
	 * @param dealer_id
	 * @param mobile_phone 
	 * @param tel 
	 * @param link_man 
	 * @param status 
	 * @param address_type 
	 * @param sex 
	 * @param area 
	 * @param city 
	 * @param province 
	 * @param pageSize 
	 * @param curPage 
	 * @return
	 */
	public PageResult<Map<String, Object>> querySHDealerAddressInfo(String dealer_id, String sex, String address_type,
			String status, String link_man, String tel, String mobile_phone, String province, String city, String area, Integer curPage, Integer pageSize) {
		StringBuilder sql = new StringBuilder(100);
		List<Object> params = new LinkedList<Object>();
		
		StringBuilder sql2 = new StringBuilder(50).append("");
		if(!province.equals("")) {
			sql2.append(" and province_id = "+ province);
		}
		
		if(!city.equals("")) {
			sql2.append(" and city_id = "+ city );
		}
		
		if(!area.equals("")) {
			sql2.append(" and area_id ="+ area);
		}
		
		params.add(dealer_id);
		sql.append("SELECT * FROM ( \n");
		sql.append(" SELECT dealer_id, addr, gender, linkman, tel, mobile_phone, address_type, addr_id, state \n");
		sql.append(" FROM tt_part_address_more \n");
		sql.append(" UNION ALL \n");
		sql.append(" SELECT dealer_id, addr, gender, linkman, tel, mobile_phone, address_type, addr_id, state \n");
		sql.append(" FROM tt_part_addr_define where 1=1"+ sql2.toString() +") a \n");
		sql.append(" WHERE a.dealer_id = ? \n");
		
		if(!sex.equals("")) {
			sql.append(" AND a.sex = ?");
			params.add(sex);
		}
		if(!address_type.equals("")) {
			sql.append(" AND a.address_type = ?");
			params.add(address_type);
		}
		if(!status.equals("")) {
			sql.append(" AND a.state = ?");
			params.add(status);
		}
		if(!link_man.equals("")) {
			sql.append(" AND a.link_man = ?");
			params.add(link_man);
		}
		if(!tel.equals("")) {
			sql.append(" AND a.tel = ?");
			params.add(tel);
		}
		if(!mobile_phone.equals("")) {
			sql.append(" AND a.mobile_phone = ?");
			params.add(mobile_phone);
		}
		
		return dao.pageQuery(sql.toString(), params, 
				"com.infodms.dao.sales.dealer.DealerAddressInfoDao.queryDealerAddressInfo", pageSize, curPage);
	}
	
	/**
	 * 添加经销商地址信息
	 * @param area_id 
	 * @param city_id 
	 * @param province_id 
	 * @param dealer_id
	 * @return
	 */
	public void addNewDealerAddressInfo(String id, String dealer_id,
			String link_man, String sex, String tel, String mobile_phone,
			String address_type, String address, String province_id, String city_id, String area_id, String status) {
		
		StringBuilder sql1 = new StringBuilder(100);
		StringBuilder sql2 = new StringBuilder(100);
		
		if(address_type.equals(Constant.SH_ADDRESS_TYPE_04.toString())) {
			sql1.append("INSERT INTO tm_vs_address");
		} else {
			sql1.append("INSERT INTO tm_vs_address_more");
		}
			
		sql1.append("(id, dealer_id, create_date, create_by");
		sql2.append(" VALUES("+ id +","+ dealer_id +",sysdate,1");
		
		if(!"".equals(link_man)) {
			sql1.append(" ,link_man");
			sql2.append(" ,'"+ link_man +"'");
		}
		if(!"".equals(sex)) {
			sql1.append(" ,sex");
			sql2.append(" ," + sex);
		}
		if(!"".equals(tel)) {
			sql1.append(" ,tel");
			sql2.append(" ,'" + tel + "'");
		}
		if(!"".equals(mobile_phone)) {
			sql1.append(" ,mobile_phone");
			sql2.append(" ,'" + mobile_phone + "'");
		}
		if(!"".equals(address_type)) {
			sql1.append(" ,address_type");
			sql2.append(" ," + address_type);
		}
		if(!"".equals(address)) {
			sql1.append(" ,address");
			sql2.append(" ,'"+ address +"'");
		}
		if(!"".equals(status)) {
			sql1.append(" ,status");
			sql2.append(" ,'"+ status +"'");
		} else {
			sql1.append(" ,status");
			sql2.append(" ,'"+ Constant.STATUS_ENABLE +"'");
		}
		
		if(address_type.equals(Constant.SH_ADDRESS_TYPE_04.toString())) {
			if(!"".equals(province_id)) {
				sql1.append(" ,province_id");
				sql2.append(" ,'"+ province_id +"'");
			}
			if(!"".equals(city_id)) {
				sql1.append(" ,city_id");
				sql2.append(" ,'"+ city_id +"'");
			}
			if(!"".equals(area_id)) {
				sql1.append(" ,area_id");
				sql2.append(" ,'"+ area_id +"'");
			}
		}
		
		sql1.append(") " + sql2.toString() + ")");
		
		dao.insert(sql1.toString());
	}
	
	/**
	 * 根据ID查询经销商地址信息
	 * @param address_type 
	 * @param dealer_id
	 * @return
	 */
	public List<Map<String, Object>> queryDealerAddressInfoById(String id, String address_type) {
		StringBuilder sql = new StringBuilder(100);
		List<Object> params = new LinkedList<Object>();
		params.add(id);
		sql.append("SELECT a.dealer_id,");
		sql.append("a.address,");
		sql.append("a.sex,");
		sql.append("a.link_man,");
		sql.append("a.tel,");
		sql.append("a.mobile_phone,");
		sql.append("a.address_type,");
		sql.append("a.id,");
		sql.append("a.status");
		
		if(Constant.SH_ADDRESS_TYPE_04.toString().equals(address_type)) {
			sql.append(",a.province_id");
			sql.append(",a.city_id");
			sql.append(",a.area_id");
			sql.append(" FROM tm_vs_address a WHERE a.id = ?");
		} else {
			sql.append(" FROM tm_vs_address_more a WHERE a.id = ?");
		}
		
		return dao.pageQuery(sql.toString(), params, 
				"com.infodms.dao.sales.dealer.DealerAddressInfoDao.queryDealerAddressInfoById");
	}
	
	/**
	 * 经销商地址信息修改
	 * @param address_type 
	 * @param dealer_id
	 * @return
	 */
	public void updateDealerAddressInfo(String id, String link_man, String sex,
			String tel, String mobile_phone, String address_type,
			String address, String province_id, String city_id, String area_id,
			String status) {
		StringBuilder sql = new StringBuilder(100);
		List<Object> params = new LinkedList<Object>();
		
		if(Constant.SH_ADDRESS_TYPE_04.toString().equals(address_type)) {
			sql.append("Update tm_vs_address");
		} else {
			sql.append("Update tm_vs_address_more");
		}
		
		sql.append(" SET link_man = ?, sex = ?, tel = ?, mobile_phone = ?, address = ?, status = ?");
		params.add(link_man);
		params.add(sex);
		params.add(tel);
		params.add(mobile_phone);
		params.add(address);
		params.add(status);
		
		if(Constant.SH_ADDRESS_TYPE_04.toString().equals(address_type)) {
			sql.append(",province_id = ?, city_id = ?, area_id = ?");
			params.add(province_id);
			params.add(city_id);
			params.add(area_id);
		}
			
		sql.append(" WHERE id = ?");
		params.add(id);
		
		dao.update(sql.toString(), params);
	}
	
	/**
	 * 根据售后经销商编号获取经销商地址信息
	 * @param dealer_id
	 * @param linkman
	 * @param tel
	 * @param mobile_phone
	 * @param mobileString_phone2 
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> queryDealerCsAddressInfo(
			String dealer_id, String linkman,String gender, String tel, String mobile_phone,
			Integer curPage, Integer pageSize,String address_type,String state) {
		
		StringBuilder sql = new StringBuilder(100);
		List<Object> params = new LinkedList<Object>();
		params.add(dealer_id);
		
		sql.append(" SELECT address_type,dealer_id, addr, gender, linkman, tel, mobile_phone, addr_id, state \n");
		sql.append(" FROM tt_part_addr_define \n");
		sql.append(" WHERE dealer_id = ? \n");
		
		if(!gender.equals("")) {
			sql.append(" AND gender = ?");
			params.add(gender);
		}
		if(!linkman.equals("")) {
			sql.append(" AND linkman = ?");
			params.add(linkman);
		}
		if(!tel.equals("")) {
			sql.append(" AND tel = ?");
			params.add(tel);
		}
		if(!mobile_phone.equals("")) {
			sql.append(" AND mobile_phone = ?");
			params.add(mobile_phone);
		}
		if(!address_type.equals("")) {
			sql.append(" AND address_type = ?");
			params.add(address_type);
		}
		if(!state.equals("")) {
			sql.append(" AND state = ?");
			params.add(state);
		}
		params.add(dealer_id);
		sql.append(" UNION SELECT address_type,dealer_id, addr, gender, linkman, tel, mobile_phone, addr_id, state \n");
		sql.append(" FROM TT_PART_ADDRESS_MORE \n");
		sql.append(" WHERE dealer_id = ? \n");
		
		if(!gender.equals("")) {
			sql.append(" AND gender = ?");
			params.add(gender);
		}
		if(!linkman.equals("")) {
			sql.append(" AND linkman = ?");
			params.add(linkman);
		}
		if(!tel.equals("")) {
			sql.append(" AND tel = ?");
			params.add(tel);
		}
		if(!mobile_phone.equals("")) {
			sql.append(" AND mobile_phone = ?");
			params.add(mobile_phone);
		}
		if(!address_type.equals("")) {
			sql.append(" AND address_type = ?");
			params.add(address_type);
		}
		if(!state.equals("")) {
			sql.append(" AND state = ?");
			params.add(state);
		}
		return dao.pageQuery(sql.toString(), params, 
				"com.infodms.dao.sales.dealer.DealerAddressInfoDao.queryDealerCsAddressInfo", pageSize, curPage);
	}
	
	/**
	 * 售后经销商地址添加
	 * @param addr_id
	 * @param dealer_id
	 * @param linkman
	 * @param gender
	 * @param tel
	 * @param mobile_phone
	 * @param addr
	 * @param dearlerName 
	 * @param dearlerCode 
	 */
	public void addNewDealerCsAddressInfo(String addr_id, String dealer_id,
			String linkman, String gender, String tel, String mobile_phone,
			String addr,String addressType,String status, String dearlerCode, String dearlerName) {
			
			StringBuilder sql1 = new StringBuilder(100);
			StringBuilder sql2 = new StringBuilder(100);
		
		if(addressType.equals(Constant.SHOU_ADDRESS_TYPE_01.toString())){
			sql1.append("INSERT INTO tt_part_addr_define(addr_id, create_date, dealer_id, dealer_code, dealer_name");
			sql2.append(") VALUES(" + addr_id + ", sysdate," + dealer_id+ ",'"+dearlerCode+ "','"+dearlerName+"'");
			
			if(!"".equals(linkman)) {
				sql1.append(", linkman");
				sql2.append(", '" + linkman +"'");
			}
			if(!"".equals(addressType)) {
				sql1.append(", address_Type");
				sql2.append(", '" + addressType +"'");
			}
			if(!"".equals(status)) {
				sql1.append(", state");
				sql2.append(", '" + status +"'");
			}
			if(!"".equals(gender)) {
				sql1.append(", gender");
				sql2.append(", " + gender);
			}
			if(!"".equals(tel)) {
				sql1.append(", tel");
				sql2.append(", '" + tel + "'");
			}
			if(!"".equals(mobile_phone)) {
				sql1.append(", mobile_phone");
				sql2.append(", '" + mobile_phone + "'");
			}
			if(!"".equals(addr)) {
				sql1.append(", addr");
				sql2.append(", '" + addr + "'");
			}
			
			sql1.append(sql2.toString() + ")");
			
		}else{
			sql1.append("INSERT INTO TT_PART_ADDRESS_MORE(ADDR_ID, create_date, dealer_id, dealer_code, dealer_name");
			sql2.append(") VALUES(" + addr_id + ", sysdate," + dealer_id+ ",'"+dearlerCode+ "','"+dearlerName+"'");
			
			if(!"".equals(linkman)) {
				sql1.append(", linkman");
				sql2.append(", '" + linkman +"'");
			}
			if(!"".equals(addressType)) {
				sql1.append(", address_Type");
				sql2.append(", '" + addressType +"'");
			}
			if(!"".equals(status)) {
				sql1.append(", state");
				sql2.append(", '" + status +"'");
			}
			if(!"".equals(gender)) {
				sql1.append(", gender");
				sql2.append(", " + gender);
			}
			if(!"".equals(tel)) {
				sql1.append(", tel");
				sql2.append(", '" + tel + "'");
			}
			if(!"".equals(mobile_phone)) {
				sql1.append(", mobile_phone");
				sql2.append(", '" + mobile_phone + "'");
			}
			if(!"".equals(addr)) {
				sql1.append(", addr");
				sql2.append(", '" + addr + "'");
			}
			
			sql1.append(sql2.toString() + ")");
		}
		
		
		dao.insert(sql1.toString());
		
	}
	
	/**
	 * 售后经销商地址信息根据ID查询
	 * @param addr_id
	 * @return
	 */
	public List<Map<String, Object>> queryDealerCsAddressInfoById(String addr_id,String address_type) {
		StringBuilder sql = new StringBuilder(100);
		List<Object> params = new ArrayList<Object>();
		if(address_type.equals(Constant.SHOU_ADDRESS_TYPE_01.toString())){
			params.add(addr_id);
			sql.append("SELECT address_type,addr_id, dealer_id, addr, linkman, gender, state, mobile_phone, tel FROM tt_part_addr_define WHERE addr_id = ?");
		}else{
			params.add(addr_id);
			sql.append("SELECT address_type,addr_id, dealer_id, addr, linkman, gender, state, mobile_phone, tel FROM TT_PART_ADDRESS_MORE WHERE addr_id = ?");
		}

		return dao.pageQuery(sql.toString(), params, 
				"com.infodms.dao.sales.dealer.DealerAddressInfoDao.queryDealerCsAddressInfoById");
	}
	
	/**
	 * 售后经销商配件收货地址修改
	 * @param addr_id
	 * @param linkman
	 * @param gender
	 * @param tel
	 * @param mobile_phone
	 * @param addr
	 * @param state
	 */
	public void updateDealerCsAddressInfo(String addr_id, String linkman,
			String gender, String tel, String mobile_phone, String addr, String addressType, String state) {
		state = state.trim().equals("") ? Constant.STATUS_ENABLE.toString() : state;
		StringBuilder sql = new StringBuilder(100);
		List<Object> params = new ArrayList<Object>();
		
		if(addressType.equals(Constant.SHOU_ADDRESS_TYPE_01.toString())){
			sql.append("UPDATE tt_part_addr_define SET linkman = ?, gender = ?, tel = ?, mobile_phone = ?, addr = ?, state = ? , address_Type = ?WHERE addr_id = ?");
			params.add(linkman);
			params.add(gender);
			params.add(tel);
			params.add(mobile_phone);
			params.add(addr);
			params.add(state);
			params.add(addressType);
			params.add(addr_id);
		}else{
			sql.append("UPDATE TT_PART_ADDRESS_MORE SET linkman = ?, gender = ?, tel = ?, mobile_phone = ?, addr = ?, state = ? , address_Type = ?WHERE addr_id = ?");
			params.add(linkman);
			params.add(gender);
			params.add(tel);
			params.add(mobile_phone);
			params.add(addr);
			params.add(state);
			params.add(addressType);
			params.add(addr_id);
		}

		
		dao.update(sql.toString(), params);
	}
}
