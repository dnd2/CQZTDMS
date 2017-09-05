package com.infodms.dms.dao.claim.application;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


public class DealerNewKpUpdateDAO extends BaseDao{
	
	public static Logger logger = Logger.getLogger(DealerNewKpUpdateDAO.class);
	private static  DealerNewKpUpdateDAO dao = new DealerNewKpUpdateDAO();

	public static  DealerNewKpUpdateDAO getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/********
	 * 查询界面
	 * @param balanceNo
	 * @param changeStatus
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>>  viewInvoiceMarkChange(String balanceNo,String dealerId,String changeStatus,int curPage,int pageSize){
		StringBuffer sql = new StringBuffer();
		sql.append("select c.*,u.name from Tt_As_Wr_Invoice_Change  c ,tc_user u where c.auditing_person= u.user_id(+) and c.DELAERID="+dealerId+" \n");
		if(Utility.testString(balanceNo)){
			sql.append(" and c.balance_no='"+balanceNo+"'\n");
		}
		if(Utility.testString(changeStatus)){
			sql.append(" and c.status='"+changeStatus+"'\n");
		}
		sql.append("order by c.create_date desc,c.status  \n");
		PageResult<Map<String, Object>> ps =  this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>>  viewInvoiceMarkChangeOem(String balanceNo,String dealerId,String changeStatus,int curPage,int pageSize){
		StringBuffer sql = new StringBuffer();
		sql.append("select c.*,u.name from Tt_As_Wr_Invoice_Change  c ,tc_user u where c.auditing_person= u.user_id(+) \n");
		if(Utility.testString(balanceNo)){
			sql.append(" and c.balance_no='"+balanceNo+"'\n");
		}
		if(Utility.testString(changeStatus)){
			sql.append(" and c.status='"+changeStatus+"'\n");
		}
		sql.append("order by c.create_date desc,c.status  \n");
		PageResult<Map<String, Object>> ps =  this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
		return ps;
	}
	
	/************
	 * 
	 * @param balanceNo
	 * @param changeStatus
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>>  modifyInvoiceMarkChange(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from Tt_As_Wr_Invoice_Change  c where c.id="+id+" \n");
		
		List<Map<String, Object>> list =  this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	
	//zhumingwei 2011-10-28
	public List<Map<String, Object>> getApplicationCount(String lastDate,String wxEndDate,String dealerId,long yieldly) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select count(*) count from tt_as_wr_application a where a.account_date>=to_date('"+lastDate+" 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		sql.append("and a.account_date<=to_date('"+wxEndDate+" 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");  
		sql.append("and a.claim_type in ("+Constant.CLA_TYPE_01+","+Constant.CLA_TYPE_09+")\n");
		sql.append("and a.status="+Constant.CLAIM_APPLY_ORD_TYPE_07+" and nvl(a.balance_amount,0)>0 and a.dealer_id="+dealerId+" and a.yieldly="+yieldly+"\n");
		
		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	//zhumingwei 2011-10-28
	public List<Map<String, Object>> getApplicationDetail(String lastDate,String wxEndDate,String dealerId,long yieldly,int con) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select * from(\n");
		sql.append("select a.id from tt_as_wr_application a where a.account_date>=to_date('"+lastDate+" 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		sql.append("and a.account_date<=to_date('"+wxEndDate+" 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");  
		sql.append("and a.claim_type in ("+Constant.CLA_TYPE_01+","+Constant.CLA_TYPE_09+")\n");
		sql.append("and a.status="+Constant.CLAIM_APPLY_ORD_TYPE_07+" and nvl(a.balance_amount,0)>0 and a.dealer_id="+dealerId+" and a.yieldly="+yieldly+"\n");
		sql.append("order by dbms_random.value)\n");
		sql.append("where rownum<"+con+"\n");
		
		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
}