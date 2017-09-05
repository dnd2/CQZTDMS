package com.infodms.dms.dao.claim.laborList;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsWrBalanceExtPO;
import com.infodms.dms.po.TtLaborListDetailPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class LaborListDao extends BaseDao {
	
	private LaborListDao(){}

	public static LaborListDao getInstance(){
		return new LaborListDao();
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * 根据DEALER_ID查询经销商信息
	 */
	@SuppressWarnings("unchecked")
	public TmDealerPO getDealer(TmDealerPO po){
		List<TmDealerPO> lists = this.select(po);
		if(lists.size()>0)return lists.get(0);
		return null ;
	}
	
	/*
	 * 弹出框查询方法
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> getNotice(String sql,int pageSize,int curPage){
		return (PageResult<Map<String, Object>>)this.pageQuery(sql, null, this.getFunName(), pageSize, curPage);
	}

	/*
	 * 劳务清单查询方法
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> getLaborList(String sql,int pageSize,int curPage){
		return (PageResult<Map<String, Object>>)this.pageQuery(sql, null, this.getFunName(), pageSize, curPage);
	}
	
	/*
	 * 劳务清单明细查询
	 */
	@SuppressWarnings("unchecked")
	public PageResult<TtLaborListDetailPO> getLaborListDetail(String id,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select d.balance_no,d.invoice_amount,d.dealer_code,d.dealer_name\n");
		sql.append("from tt_labor_list_detail d\n");
		sql.append("where d.report_id =").append(id).append("\n");
		return this.pageQuery(TtLaborListDetailPO.class,sql.toString(),null, pageSize, curPage);
	}
	
	
	public List<Map<String, Object>> getStopAuth(Long id) {
		String sql="select (select name from tc_user where user_id=a.auth_by) name,a.auth_date,(select c.code_desc from tc_code c where c.code_id=a.auth_status) status,a.remark from tt_as_wr_stopAuth a where a.report_id="+id+" order by a.id desc";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	
	public List<Map<String,Object>>  balanceNoOnly(String balanceNo){
		StringBuffer sql = new StringBuffer();
		sql.append("select ld.report_id from tt_labor_list_detail ld where ld.balance_no='"+balanceNo+"'");
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
}
