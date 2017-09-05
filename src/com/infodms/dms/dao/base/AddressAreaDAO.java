package com.infodms.dms.dao.base;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAddressAreaRPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;

public class AddressAreaDAO extends BaseDao{
	public static Logger logger = Logger.getLogger(OrderDAO.class);
	
	private AddressAreaDAO() {
		
	}
	
	private static class AddressAreaDAOSingleton {
		private static AddressAreaDAO dao = new AddressAreaDAO() ;
	}
	
	public static AddressAreaDAO getInstance() {
		return AddressAreaDAOSingleton.dao ;
	}
	
	public void addressAreaInsert(Map<String, String> map) {
		String addressId = map.get("addressId") ;
		String areaId = map.get("areaId") ;
		String userId = map.get("userId") ;
		
		TtAddressAreaRPO taar = new TtAddressAreaRPO() ;
		
		Long id = Long.parseLong(SequenceManager.getSequence("")) ;
		
		taar.setId(id) ;
		taar.setAddressId(Long.parseLong(addressId)) ;
		taar.setAreaId(Long.parseLong(areaId)) ;
		taar.setCreateBy(Long.parseLong(userId)) ;
		taar.setCreateDate(new Date(System.currentTimeMillis())) ;
		
		super.insert(taar) ;
	}
	
	public int addressAreaDelete(Map<String, String> map) {
		String addressId = map.get("addressId") ;
		String areaId = map.get("delAreaId") ;
		
		TtAddressAreaRPO taar = new TtAddressAreaRPO() ;
		
		if(!CommonUtils.isNullString(addressId)) {
			taar.setAddressId(Long.parseLong(addressId)) ;
		}
		
		if(!CommonUtils.isNullString(areaId)) {
			taar.setAreaId(Long.parseLong(areaId)) ;
		}
		
		return super.delete(taar) ;
	}
	
	public int addressAreaDeleteByDlrAndArea(String dealerId, String areaId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("delete tt_address_area_r taar\n");
		sql.append(" where taar.address_id in\n");  
		sql.append("       (select tva.id from tm_vs_address tva where tva.dealer_id in (").append(dealerId).append("))\n");  
		sql.append("   and taar.area_id in (").append(areaId).append(")\n");

		return super.delete(sql.toString(), null) ;
	}
	
	public void addressAreaUpdate(Map<String, String> map) {
		this.addressAreaDelete(map) ;
		
		this.addressAreaInsert(map) ;
	}
	
	public List<TtAddressAreaRPO> addressAreaQuery(String addressId) {
		TtAddressAreaRPO taar = new TtAddressAreaRPO() ;
		taar.setAddressId(Long.parseLong(addressId)) ;
		
		List<TtAddressAreaRPO> taarList = super.select(taar) ;
		
		return taarList ;
	}
	
	public List<Map<String, Object>> addressAreaInfoQuery(String addressId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tba.area_id, tba.area_name\n");
		sql.append("          from tt_address_area_r taar, tm_business_area tba\n");  
		sql.append("         where taar.area_id = tba.area_id\n");  
		sql.append("           and taar.address_id = ").append(addressId).append("\n");
		
		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
