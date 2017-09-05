package com.infodms.dms.dao.claim.laborList;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtTaxableServiceDetailPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class LaborListForTaxDao extends BaseDao {
	private LaborListForTaxDao(){}
	public static LaborListForTaxDao getInstanct(){
		return new LaborListForTaxDao();
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * 开票通知单查询方法
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> getNotice(String con,int pageSize,int curPage){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select b.id,\n" );
		sql.append("       b.balance_no,\n" );
		sql.append("       b.apply_person_id,\n" );
		sql.append("       b.apply_person_name,\n" );
		sql.append("       b.yieldly,\n" );
		sql.append("       b.dealer_id,\n" );
		sql.append("       c.dealer_code,\n" );
		sql.append("       c.dealer_name,\n" );
		sql.append("       b.status,\n" );
		sql.append("       d.dealer_name as invoice_maker,\n" );
		sql.append("       to_char(b.start_date,'yyyy-mm-dd') start_date,\n");
		sql.append("       to_char(b.end_date,'yyyy-mm-dd') end_date\n");
		sql.append("  from tt_as_wr_claim_balance b,tm_dealer c,tm_dealer d\n" );
		sql.append(" where 1 = 1\n" );
		sql.append(" and b.dealer_id=c.dealer_id and b.kp_dealer_id=d.dealer_id \n");
		sql.append("   and b.note_amount>0\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append(" order by b.balance_no desc");
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}

	public PageResult<Map<String,Object>> getNoticeChange(String con,int pageSize,int curPage){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select b.id,\n" );
		sql.append("       b.balance_no,\n" );
		sql.append("       b.apply_person_id,\n" );
		sql.append("       b.apply_person_name,\n" );
		sql.append("       b.yieldly,\n" );
		sql.append("       b.dealer_id,\n" );
		sql.append("       c.dealer_code,\n" );
		sql.append("       c.dealer_name,\n" );
		sql.append("       b.status,\n" );
		sql.append("       d.dealer_name as invoice_maker,\n" );
		sql.append("       to_char(b.start_date,'yyyy-mm-dd') start_date,\n");
		sql.append("       to_char(b.end_date,'yyyy-mm-dd') end_date\n");
		sql.append("  from tt_as_wr_claim_balance b,tm_dealer c,tm_dealer d\n" );
		sql.append(" where 1 = 1\n" );
		sql.append(" and b.dealer_id=c.dealer_id and b.kp_dealer_id=d.dealer_id and b.status="+Constant.ACC_STATUS_04+"\n");
		sql.append("   and b.note_amount>0\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append(" order by b.balance_no desc");
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}

	
	/*
	 * 应税劳务清单主页面主查询
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> mainQuery(String con,int pageSize,int curPage){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select s.taxable_service_id,\n" );
		sql.append("       s.balance_id,\n" );
		sql.append("       s.balance_no,\n" );
		sql.append("       s.purchaser_id,\n" );
		sql.append("       s.purchaser_name,\n" );
		sql.append("       s.sales_id,\n" );
		sql.append("       d.dealer_code as sales_code,\n");
		sql.append("       d.dealer_name as sales_name,\n" );
		sql.append("       s.invoice_no,\n" );
		sql.append("       s.no,\n" );
		sql.append("       s.tax_rate,\n" );
		sql.append("       to_char(s.claim_start_date,'yyyy-MM-dd') claim_start_date,\n" );
		sql.append("       to_char(s.claim_end_date,'yyyy-MM-dd') claim_end_date,\n" );
		sql.append("       s.dlr_id\n" );
		sql.append("  from tt_taxable_service s,tm_dealer d\n" );
		sql.append(" where s.dlr_id = d.dealer_id\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append(" order by s.balance_no desc\n");
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	/*
	 * 针对生成按钮后的明细查询操作
	 */
	@SuppressWarnings("unchecked")
	public List<TtTaxableServiceDetailPO> getDetail(Long id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select d.* from tt_taxable_service_detail d\n");
		sql.append("where d.taxable_service_id=").append(id).append("\n");
		sql.append("order by d.serial_number asc\n");
		return this.select(TtTaxableServiceDetailPO.class, sql.toString(), null);
	}
	
	/*
	 * 删除TT_TAXABLE_SERVICE_DETAIL表中的数据
	 */
	@SuppressWarnings("unchecked")
	public void delDetail(String id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("delete from tt_taxable_service_detail where taxable_service_id in ");
		sql.append("(select taxable_service_id from tt_taxable_service where balance_id = ").append(id).append(")\n");
		this.delete(sql.toString(),null);
	}
	
	/*
	 * 删除TT_TAXABLE_SERVICE_DETAIL表中的数据
	 */
	@SuppressWarnings("unchecked")
	public void delMain(String id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("delete from tt_taxable_service where balance_id = ").append(id);
		this.delete(sql.toString(), null);
	}
	/***查询生成的应税清单与开票金额是否相符****/
	public List<Map<String,Object>> getTaxGoodsAmount(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT count(1) cou\n" );
		sql.append("  FROM TT_TAXABLE_SERVICE        S,\n" );
		sql.append("       TT_TAXABLE_SERVICE_DETAIL D,\n" );
		sql.append("       Tt_As_Wr_Claim_Balance    CC\n" );
		sql.append(" WHERE S.TAXABLE_SERVICE_ID = D.TAXABLE_SERVICE_ID\n" );
		sql.append("   AND CC.BALANCE_NO = S.BALANCE_NO\n" );
		sql.append("   AND CC.NOTE_AMOUNT <> D.GOODS_EXCL_TAX_AMOUNT\n" );
		sql.append("   AND D.SERIAL_NUMBER = 10\n" );
		sql.append("   and s.balance_id="+id+"");
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
}
