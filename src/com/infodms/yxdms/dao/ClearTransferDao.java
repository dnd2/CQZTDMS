package com.infodms.yxdms.dao;

import java.sql.ResultSet;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
@SuppressWarnings("rawtypes")
public class ClearTransferDao extends IBaseDao{
	private static ClearTransferDao dao = new ClearTransferDao();
	public static final ClearTransferDao getInstance(){
		dao = (dao==null)?new ClearTransferDao():dao;
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> clearTransferQuery(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct c.*,\n" );
		sql.append("                (c.total_settlement - c.PDI_AMOUNT - c.maintenance_amount -\n" );
		sql.append("                c.balance_part_amount - c.balance_labour_amount) as other_amount\n" );
		sql.append("  from (select tm.dealer_code as DEALER_CODE,\n" );
		sql.append("               tm.dealer_shortname as DEALER_SHORTNAME,\n" );
		sql.append("               c.remark as CLEARING_NUMBER,\n" );
		sql.append("               c.amount_sum as TOTAL_SETTLEMENT,\n" );
		sql.append("               c.start_date,\n" );
		sql.append("               c.end_date,\n" );
		sql.append("               nvl((select sum(a.balance_amount)\n" );
		sql.append("                     from Tt_As_Wr_Application a\n" );
		sql.append("                    where a.balance_no = c.remark\n" );
		sql.append("                      and a.claim_type = 10661011\n" );
		sql.append("                      and a.status != 10791005\n" );
		sql.append("                      and a.status != 10791006),\n" );
		sql.append("                   0) as PDI_AMOUNT,\n" );
		sql.append("               nvl((select sum(a.balance_amount)\n" );
		sql.append("                     from Tt_As_Wr_Application a\n" );
		sql.append("                    where a.balance_no = c.remark\n" );
		sql.append("                      and a.claim_type = 10661002\n" );
		sql.append("                      and a.status != 10791005\n" );
		sql.append("                      and a.status != 10791006),\n" );
		sql.append("                   0) as MAINTENANCE_AMOUNT,\n" );
		sql.append("               nvl((select sum(a.balance_labour_amount)\n" );
		sql.append("                     from Tt_As_Wr_Application a\n" );
		sql.append("                    where a.balance_no = c.remark\n" );
		sql.append("                      and a.claim_type not in (10661011, 10661002)\n" );
		sql.append("                      and a.status != 10791005\n" );
		sql.append("                      and a.status != 10791006),\n" );
		sql.append("                   0) as balance_labour_amount, --工时费用\n" );
		sql.append("               nvl((select sum(a.balance_part_amount)\n" );
		sql.append("                     from Tt_As_Wr_Application a\n" );
		sql.append("                    where a.balance_no = c.remark\n" );
		sql.append("                      and a.claim_type not in (10661011, 10661002)\n" );
		sql.append("                      and a.status != 10791005\n" );
		sql.append("                      and a.status != 10791006),\n" );
		sql.append("                   0) as balance_part_amount --材料费用\n" );
		sql.append("          from Tt_As_Wr_Claim_Balance c, tm_dealer tm\n" );
		sql.append("         where tm.dealer_id = c.dealer_id\n" );
		sql.append("           and c.remark in\n" );
		sql.append("               (select p.balance_oder\n" );
		sql.append("                  from Tt_As_Payment p\n" );
		sql.append("                 where 1 = 1");
		DaoFactory.getsql(sql, "p.TRANSFER_TICKETS_DATE",DaoFactory.getParam(request, "beginTime"), 3);
		DaoFactory.getsql(sql, "p.TRANSFER_TICKETS_DATE",DaoFactory.getParam(request, "endTime"), 4);
		sql.append(")) c");
		return this.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
	}

	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> TransferWithoutTaxQuery(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		StringBuffer sb = new StringBuffer();
		sb.append("select tm.dealer_code as dealer_code,\n");
		sb.append("       tm.dealer_shortname as dealer_shortname,\n");
		sb.append("       c.remark as clearing_number,\n");
		sb.append("       p.TRANSFER_TICKETS_DATE as TRANSFER_TICKETS_DATE,\n");
		sb.append("       p.LABOUR_RECEIPT as LABOUR_RECEIPT,\n");
		sb.append("       p.PART_RECEIPT as PART_RECEIPT,\n");
		sb.append("       p.AMOUNT_OF_MONEY as AMOUNT_OF_MONEY,\n");
		sb.append("       p.TAX_RATE_MONEY as TAX_RATE_MONEY,\n");
		sb.append("       p.AMOUNT_SUM as AMOUNT_SUM\n");
		sb.append("  from Tt_As_Payment p, Tt_As_Wr_Claim_Balance c, tm_dealer tm\n" );
		sb.append(" where tm.dealer_id = c.dealer_id\n");
		sb.append("   and p.balance_oder = c.remark\n" );
		sb.append("   and p.status = 4\n");
		DaoFactory.getsql(sb, "p.TRANSFER_TICKETS_DATE",DaoFactory.getParam(request, "beginTime"), 3);
		DaoFactory.getsql(sb, "p.TRANSFER_TICKETS_DATE",DaoFactory.getParam(request, "endTime"), 4);
		sb.append("   order by c.remark");
		return this.pageQuery(sb.toString(),null, getFunName(), pageSize, currPage);
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> InvoiceCompareQueryList(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("select t.*\n" );
		sql.append("  from (select c.remark,\n" );
		sql.append("               c.amount_sum,\n" );
		sql.append("               c.create_date,\n" );
		sql.append("               (select nvl(sum(p.amount_sum), 0)\n" );
		sql.append("                  from Tt_As_Payment p\n" );
		sql.append("                 where p.balance_oder = c.remark) as Payment_amount,\n" );
		sql.append("               tm.TAXPAYER_NATURE,\n" );
		sql.append("               tm.tax_disrate,\n" );
		sql.append("               tm.dealer_code,\n" );
		sql.append("               tm.dealer_shortname\n" );
		sql.append("          from Tt_As_Wr_Claim_Balance c, tm_dealer tm\n" );
		sql.append("         where c.dealer_id = tm.dealer_id\n" );
		sql.append("           and c.status = 1) t\n" );
		sql.append(" where t.Payment_amount <> t.amount_sum\n" );
		DaoFactory.getsql(sql, "t.create_date", DaoFactory.getParam(request, "RO_STARTDATE"), 3);
		DaoFactory.getsql(sql, "t.create_date", DaoFactory.getParam(request, "RO_ENDDATE"), 4);
		DaoFactory.getsql(sql, "t.REMARK", DaoFactory.getParam(request, "CLAIM_NO"),2);
		DaoFactory.getsql(sql, "t.dealer_shortname", DaoFactory.getParam(request, "DEALER_NAME"),2);
		DaoFactory.getsql(sql, "t.dealer_code", DaoFactory.getParam(request, "DEALER_CODE"),6);
		return this.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
	}

}
