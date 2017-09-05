package com.infodms.dms.dao.sales.marketmanage.plancommon;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class BankrollDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(BankrollDAO.class);
	
	public Map<String, Object> sysnAccount(Map<String, String> map) {
		Long orgId = new Long(map.get("orgId")) ;
		Long accountType = new Long(map.get("accountType")) ;
		Long areaId = new Long(map.get("areaId")) ;

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select * from tt_vs_cost tvc where 1 = 1 and tvc.cost_Type = ").append(accountType).append(" and tvc.org_id = ").append(orgId).append(" and tvc.area_id = ").append(areaId).append(" for update nowait\n") ;

		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}

	public void updateAccount(Map<String, String> map) {
		Long orgId = new Long(map.get("orgId")) ;
		Long accountType = new Long(map.get("accountType")) ;
		Long areaId = new Long(map.get("areaId")) ;
		BigDecimal availableAmount = new BigDecimal(map.get("availableAmount")) ;
		BigDecimal totalFreezeAmount = new BigDecimal(map.get("totalFreezeAmount")) ;

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("update tt_vs_cost tvc set tvc.available_amount = ").append(availableAmount).append(", tvc.freeze_amount = ").append(totalFreezeAmount).append(" where 1 = 1 and tvc.cost_Type = ").append(accountType).append(" and tvc.org_id = ").append(orgId).append(" and tvc.area_id = ").append(areaId).append("\n") ;

		super.update(sql.toString(), null) ;
	}
	
	public Map<String, Object> sysnAccountDlr(Map<String, String> map) {
		String dealerId = map.get("dealerId") ;
		Long accountType = new Long(map.get("accountType")) ;
		Long areaId = new Long(map.get("areaId")) ;

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select * from tt_vs_cost tvc where 1 = 1 and tvc.cost_Type = ").append(accountType).append(" and tvc.dealer_id in (").append(dealerId).append(") and tvc.area_id = ").append(areaId).append(" for update nowait\n") ;

		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}

	public void updateAccountDlr(Map<String, String> map) {
		String dealerId = map.get("dealerId") ;
		Long accountType = new Long(map.get("accountType")) ;
		Long areaId = new Long(map.get("areaId")) ;
		BigDecimal availableAmount = new BigDecimal(map.get("availableAmount")) ;
		BigDecimal totalFreezeAmount = new BigDecimal(map.get("totalFreezeAmount")) ;

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("update tt_vs_cost tvc set tvc.available_amount = ").append(availableAmount).append(", tvc.freeze_amount = ").append(totalFreezeAmount).append(" where 1 = 1 and tvc.cost_Type = ").append(accountType).append(" and tvc.dealer_id in (").append(dealerId).append(") and tvc.area_id = ").append(areaId).append("\n") ;

		super.update(sql.toString(), null) ;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
