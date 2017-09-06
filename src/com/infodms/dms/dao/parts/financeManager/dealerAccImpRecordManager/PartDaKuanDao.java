package com.infodms.dms.dao.parts.financeManager.dealerAccImpRecordManager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @author liu
 * 打款审核--车厂
 */
public class PartDaKuanDao extends BaseDao {

	//public static Logger logger = Logger.getLogger(ThreeCreditMaintenanceDao.class);
	private static final PartDaKuanDao dao = new PartDaKuanDao();

	public static final PartDaKuanDao getInstance() {
		return dao;
	}
	private PartDaKuanDao() {}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	
	/**
	 * 配件打款查询
	 * @param condition
	 * @param curPage
	 * @param i
	 * @return
	 */
	public PageResult<Map<String, Object>> queryMoneyAudit2(
			Map<String, Object> condition, Integer curPage, int i) {
		StringBuilder sbWhere = new StringBuilder(100);
		StringBuilder sbSql1 = new StringBuilder(100);
		StringBuilder sbSql2 = new StringBuilder(100);
		String pz_no = condition.get("pz_no").toString();
		String acount_kind = condition.get("acount_kind").toString();
		String dealerIds = condition.get("dealerIds").toString();
		String status = condition.get("status").toString();
		String orgId = condition.get("orgId").toString();
		List<Object> params = new ArrayList<Object>();
		
		//sbSql.append("union\n");
		
		sbSql2.append("select a.childorg_id dealer_id,\n");
		sbSql2.append("       a.histrory_id detail_id,\n");
		sbSql2.append("       a.parentorg_code parentorg_code,\n");
		sbSql2.append("       a.parentorg_id parentorg_id,\n");
//		sbSql2.append("       Decode(account_purpose,95631001,1,2) fin_type, --1 表示配件款 2 表示精品款\n"); 
		sbSql2.append("       account_purpose fin_type, --1 表示配件款 2 表示精品款\n");
		sbSql2.append("       a.pz_no pz_no,\n");
		sbSql2.append("       to_char(a.dk_date,'yyyy-mm-dd') dk_date,\n");
		sbSql2.append("       a.amount amount,\n");
		sbSql2.append("       a.remark remark,\n");
		sbSql2.append("       a.childorg_name dealer_name,\n");
		sbSql2.append("       a.CREATE_DATE,\n");
		sbSql2.append("       parentorg_name,\n");
		sbSql2.append("       decode(to_number(a.status), 1, 18801002, 18801003) status\n");
		sbSql2.append("  from tt_part_account_import_history a\n");
		sbSql2.append(" where 1 = 1 \n");
		
		if(!"".equals(status)) {
			sbSql2.append("   and a.status = ? --1表示未审理\n"); 
			params.add(status.equals(Constant.TRANSFER_STATUS_02.toString()) ? 1 : 2);
		} else {
			sbSql2.append("   and (a.status = 1 or a.status = 2) --1表示未审理 2审核通过\n"); 
		}
		
		if(!"".equals(pz_no)) {
			sbWhere.append("   and pz_no = ? \n");
			params.add(pz_no);
		}
		
		if(!"".equals(acount_kind)) {
			sbWhere.append("   and fin_type = ? \n");
			params.add(Integer.parseInt(acount_kind));
		}
		
		if(!"".equals(dealerIds)) {
			sbWhere.append("   and dealer_id in (?) \n");
			params.add(dealerIds);
		}
		if(!"".equals(orgId)){
			sbWhere.append("   and parentorg_id in (?) \n");
			params.add(orgId);
		}
		String sql = "select * from ( \n" + sbSql2.toString() + ") where 1 = 1 \n" + sbWhere.toString();
				sql += " order by CREATE_DATE desc";
		return dao.pageQuery(sql, params, this.getFunName(), i, curPage);
	}
	
	
}