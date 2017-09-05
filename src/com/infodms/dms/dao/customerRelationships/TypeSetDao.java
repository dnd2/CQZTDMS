package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFinReturnTypePO;
import com.infodms.dms.po.TtCrmSeatsTeamPO;
import com.infodms.dms.po.TtDealerPreFinPO;
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

@SuppressWarnings("unchecked")
public class TypeSetDao extends BaseDao{

	private static final TypeSetDao dao = new TypeSetDao();
	
	public static final TypeSetDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	public PageResult<Map<String,Object>> queryTypeSet(String type,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.code_id CODEID,t.type TYPE,t.type_name TYPENAME,t.code_desc CODEDESC \n");
		sql.append("  from tc_code t where t.status = "+Constant.STATUS_ENABLE+" \n");

		if(StringUtil.notNull(type)){
			sql.append(" and t.type = '"+type+"'\n");
		}else{
			sql.append(" and t.type in( '"+Constant.TYPE_COMPLAIN+"' , '"+Constant.TYPE_CONSULT+"' , '"+Constant.TYPE_RETURN_VISIT+"') \n");
		}
		sql.append(" order by t.type,t.code_parent_id,t.num ");
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	public PageResult<Map<String,Object>> queryTypeSetForCredit(String type,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.TYPE_ID CODEID,\n");
		sql.append("       t.TYPE_NAME CODEDESC,\n");
		sql.append("       t.TYPE_code,\n");
		sql.append("       t.type_class,\n");
		sql.append("       t.discount_rate,\n");
		sql.append("       (SELECT distinct FIN_TYPE\n");
		sql.append("          FROM (SELECT a.FIN_TYPE\n");
		sql.append("                 FROM TT_SALES_OTHER_CREDIT a\n");
		sql.append("               UNION all\n");
		sql.append("                SELECT a.FIN_TYPE from TT_SALES_FIN_IN_DET a)\n");
		sql.append("         where FIN_TYPE = t.type_class) as displayFlag\n");
		sql.append("  from tt_vs_account_type t\n");
		sql.append(" where t.status = 10011001\n");
		sql.append(" order by t.TYPE_ID ");
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	public PageResult<Map<String,Object>> queryTypeSetForRebate(String type,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.*,decode(t.IMPORT_TYPE,'1','是','否') as IMPORT_TYPE_name,\n");
		
		sql.append("       (SELECT distinct FIN_TYPE\n");
		sql.append("          FROM (SELECT a.FIN_TYPE\n");
		sql.append("                 FROM TT_DEALER_FIN_RETURN a)\n");
		sql.append("         where FIN_TYPE = t.FIN_RETURN_TYPE) as displayFlag\n");
		
		sql.append("  from TM_FIN_RETURN_TYPE t\n");
		
		sql.append(" where t.status = 10011001\n");
		sql.append(" order by t.FIN_RETURN_TYPE ");
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	public PageResult<Map<String,Object>> discountInputQuery(String type,int pageSize,int curPage, String dealerId, String month, String AUDIT_STATUS){
		StringBuffer sql = new StringBuffer();
		sql.append("select TO_CHAR(t.MONTH,'YYYY-MM') AS MONTH,t.AMOUNT,(select FIN_RETURN_name from TM_FIN_RETURN_TYPE where FIN_RETURN_TYPE = t.FIN_RETURN_TYPE) as FIN_RETURN_NAME"
				+ ",t.pre_id,t.DIS_PERCENT,t.DIS_PERCENT||'%' as DIS_PERCENT_NAME,t.ACTUAL_AMOUNT,t.AUDIT_STATUS,(select code_desc from tc_code where code_id =t.AUDIT_STATUS) AS AUDIT_STATUS_NAME,"
				+ "(SELECT NAME FROM TC_USER WHERE USER_ID = t.CREATE_BY) AS CREATE_BY,t.CREATE_DATE,T.REMARK,T.KP_STATUS,TM.DEALER_NAME\n");
		sql.append("  from TT_DEALER_PRE_FIN t,TM_DEALER TM \n");
		sql.append(" WHERE t.DEALER_ID = TM.DEALER_ID\n");
		
		if(!StringUtil.isNull(dealerId)){
			sql.append(" AND t.DEALER_ID = "+dealerId+"\n");
		}
		if(!StringUtil.isNull(AUDIT_STATUS)){
			sql.append(" AND t.AUDIT_STATUS = "+AUDIT_STATUS+"\n");
		}
		if(!StringUtil.isNull(month)){
			sql.append("and t.MONTH >=\n");
			sql.append("    trunc(to_date('"+month+"', 'yyyy-mm-dd'), 'mm')\n");
			sql.append("and t.MONTH <=\n");
			sql.append("    last_day(to_date('"+month+"', 'yyyy-mm-dd'))\n");
		}
		sql.append(" order by t.dealer_id,t.month \n");
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	public PageResult<Map<String,Object>> discountAuditQuery(String type,int pageSize,int curPage, String dealerId, String month, String AUDIT_STATUS){
		StringBuffer sql = new StringBuffer();
		sql.append("select TO_CHAR(t.MONTH,'YYYY-MM') AS MONTH,t.AMOUNT,(select FIN_RETURN_name from TM_FIN_RETURN_TYPE where FIN_RETURN_TYPE = t.FIN_RETURN_TYPE) as FIN_RETURN_NAME"
				+ ",t.pre_id,t.DIS_PERCENT,t.ACTUAL_AMOUNT,t.AUDIT_STATUS,"
				+ "(SELECT NAME FROM TC_USER WHERE USER_ID = t.CREATE_BY) AS CREATE_BY,t.CREATE_DATE,T.REMARK,T.KP_STATUS,TM.DEALER_NAME\n");
		sql.append("  from TT_DEALER_PRE_FIN t,TM_DEALER TM\n");
		sql.append(" WHERE t.DEALER_ID = TM.DEALER_ID\n");
//		sql.append(" and t.AUDIT_STATUS = 20011002\n");
		if(!StringUtil.isNull(AUDIT_STATUS)){
			sql.append(" AND t.AUDIT_STATUS = "+AUDIT_STATUS+"\n");
		}else{
			sql.append(" AND t.AUDIT_STATUS != 20011001\n");
		}
		if(!StringUtil.isNull(dealerId)){
			sql.append(" AND t.DEALER_ID = "+dealerId+"\n");
		}
		if(!StringUtil.isNull(month)){
			sql.append("and t.MONTH >=\n");
			sql.append("    trunc(to_date('"+month+"', 'yyyy-mm-dd'), 'mm')\n");
			sql.append("and t.MONTH <=\n");
			sql.append("    last_day(to_date('"+month+"', 'yyyy-mm-dd'))\n");
		}
		sql.append(" order by t.dealer_id , t.month \n");
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}

	public TcCodePO queryTcCodePO(TcCodePO tcCodePO) {
		List<TcCodePO> lists = this.select(tcCodePO);
		if(lists.size()>0) return lists.get(0);
		return null ;
	}
	
	public TtVsAccountTypePO queryTtVsAccountTypePO(TtVsAccountTypePO accountTypePO) {
		List<TtVsAccountTypePO> lists = this.select(accountTypePO);
		if(lists.size()>0) return lists.get(0);
		return null ;
	}
	
	public TmFinReturnTypePO queryTmFinReturnTypePO(TmFinReturnTypePO accountTypePO) {
		List<TmFinReturnTypePO> lists = this.select(accountTypePO);
		if(lists.size()>0) return lists.get(0);
		return null ;
	}
	
	public TtDealerPreFinPO queryTtDealerPreFinPO(TtDealerPreFinPO accountTypePO) {
		List<TtDealerPreFinPO> lists = this.select(accountTypePO);
		if(lists.size()>0) return lists.get(0);
		return null ;
	}
	
	public TmDealerPO queryTmDealerPO(TmDealerPO accountTypePO) {
		List<TmDealerPO> lists = this.select(accountTypePO);
		if(lists.size()>0) return lists.get(0);
		return null ;
	}
	
	public List<TtCrmSeatsTeamPO>  querySeatsTeamSet(){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from TT_CRM_SEATS_TEAM t where t.status = "+Constant.STATUS_ENABLE);
		List<TtCrmSeatsTeamPO> list = this.select(TtCrmSeatsTeamPO.class, sql.toString(), null);
		return list;
	}

	public int getSizeByType(String type) {
		String sql = "select to_char(count(distinct t.code_id)) CODEID from tc_code t where t.type like '"+type+"%' ";
		List<String> list = null;
		try {
			list = this.selectTmDataSet(new StringBuffer(sql),"CODEID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list!=null&&list.size()>0) return Integer.parseInt(list.get(0));
		return 0;
	}
	
	public int getSizeByRebate(String type) {
		String sql = "select t.Fin_Return_Type from TM_FIN_RETURN_TYPE t order by t.Fin_Return_Type desc";
		List<Map<String,Object>> list = new LinkedList<Map<String,Object>>(); 
	    list = dao.pageQuery(sql.toString(), null, this.getFunName());
		if (list.size()>0) {
			return Integer.parseInt(list.get(0).get("FIN_RETURN_TYPE").toString());
		}else{
			return 19021000;
		}
	}
	
	public int getSize(String type) {
		String sql = "select t.type_class from tt_vs_account_type t order by t.type_class desc";
		List<Map<String,Object>> list = new LinkedList<Map<String,Object>>(); 
	    list = dao.pageQuery(sql.toString(), null, this.getFunName());
		if (list.size()>0) {
			return Integer.parseInt(list.get(0).get("TYPE_CLASS").toString());
		}else{
			return 10251000;
		}
	}
	
	public boolean haveChildType(String ids){
		StringBuffer sql = new StringBuffer();
		sql.append("select *\r\n");
		sql.append("  from tc_code t\r\n");
		sql.append(" where t.code_parent_id in('"+ids+"')\r\n");
		sql.append("   and t.status = "+Constant.STATUS_ENABLE ); 
		List list = dao.pageQuery(sql.toString(), null, this.getFunName());
		if(list!=null&&list.size()>0)return true;
		else return false;
	}

	public boolean haveChild(String ids){
		StringBuffer sql = new StringBuffer();
		sql.append("select *\r\n");
		sql.append("  from TT_VS_ACCOUNT_TYPE t\r\n");
		sql.append(" where t.type_id in('"+ids+"')\r\n");
		sql.append("   and t.status = "+Constant.STATUS_ENABLE ); 
		List list = dao.pageQuery(sql.toString(), null, this.getFunName());
		if(list!=null&&list.size()>0)return true;
		else return false;
	}
	
	public void deleteTypeSet(String ids) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update tc_code t \n");
		sql.append(" set t.status = "+Constant.STATUS_DISABLE+" \n");
		sql.append(" where t.code_id in('"+ids+"')");
		dao.update(sql.toString(), null);		
	}
	
	public void deleteType(String ids) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update TT_VS_ACCOUNT_TYPE t \n");
		sql.append(" set t.status = "+Constant.STATUS_DISABLE+" \n");
		sql.append(" where t.type_id in('"+ids+"')");
		dao.update(sql.toString(), null);		
	}
	
	public void deleteRebateType(String ids) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update TM_FIN_RETURN_TYPE t \n");
		sql.append(" set t.status = "+Constant.STATUS_DISABLE+" \n");
		sql.append(" where t.return_type_id in('"+ids+"')");
		dao.update(sql.toString(), null);		
	}
	
	public List<Map<String,Object>> queryFinType(String type) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct FIN_TYPE \n");
		sql.append("  FROM (SELECT a.FIN_TYPE \n");
		sql.append("          FROM TT_SALES_OTHER_CREDIT a \n");
		sql.append("       UNION all \n");
		sql.append("        SELECT a.FIN_TYPE from TT_SALES_FIN_IN_DET a) \n");
		sql.append(" where FIN_TYPE is not null \n");
		sql.append(" and FIN_TYPE in ("+type+") \n");
		return dao.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	public List<Map<String,Object>> queryRebateType(String type) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct FIN_TYPE \n");
		sql.append("  FROM (SELECT a.FIN_TYPE \n");
		sql.append("          FROM TT_DEALER_FIN_RETURN a)\n");
		sql.append(" where FIN_TYPE is not null \n");
		sql.append(" and FIN_TYPE in ("+type+") \n");
		return dao.pageQuery(sql.toString(), null, this.getFunName());
	}

	public int getSizeByTypeAndCodeDesc(String typeId, String codeDesc) {
		String sql = "select to_char(count(distinct t.code_id)) CODEID from tc_code t where t.type = '"+typeId+"' and t.code_desc = '"+codeDesc+"' and t.status="+Constant.STATUS_ENABLE;
		List<String> list = null;
		try {
			list = this.selectTmDataSet(new StringBuffer(sql),"CODEID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list!=null&&list.size()>0) return Integer.parseInt(list.get(0));
		return 0;
	}
	
	public List<Map<String,Object>> getTypeSelete(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.code_id CODEID,t.CODE_PARENT_ID codePI,t.type TYPE,t.type_name TYPENAME,t.code_desc CODEDESC,t.code_desc CODEDESCVIEW,level LEV\r\n");
		sql.append(" from tc_code t\r\n");
		sql.append(" where t.status="+Constant.STATUS_ENABLE +"\r\n");
		sql.append(" START WITH t.CODE_PARENT_ID =0\r\n");
		sql.append(" CONNECT BY PRIOR  t.code_id = t.CODE_PARENT_ID \r\n");
		sql.append(" ORDER SIBLINGS BY t.code_id "); 
		return dao.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	public int getMaxLevelTypeSelete(String type){ 
		StringBuffer sql = new StringBuffer();
		sql.append(" select max(LEV) MAXLEVEL from ( \r\n");
		sql.append(" select t.code_id CODEID,t.CODE_PARENT_ID codePI,t.type TYPE,t.type_name TYPENAME,t.code_desc CODEDESC,t.code_desc CODEDESCVIEW,level LEV\r\n");
		sql.append(" from tc_code t\r\n");
		sql.append(" where t.status="+Constant.STATUS_ENABLE +"\r\n");
		sql.append(" START WITH t.CODE_PARENT_ID ='"+type+"' \r\n");
		sql.append(" CONNECT BY PRIOR t.code_id = t.CODE_PARENT_ID \r\n");
		sql.append(" ORDER SIBLINGS BY t.code_id "); 
		sql.append(" ) ");
		return Integer.parseInt(this.pageQueryMap(sql.toString(),
				null,
				this.getFunName()).get("MAXLEVEL").toString());
	}

	public List<Map<String, Object>> getTypeSelete(String type) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.code_id CODEID,t.CODE_PARENT_ID codePI,t.type TYPE,t.type_name TYPENAME,t.code_desc CODEDESC,t.code_desc CODEDESCVIEW,level LEV\r\n");
		sql.append(" from tc_code t\r\n");
		sql.append(" where t.status="+Constant.STATUS_ENABLE +"\r\n");
		sql.append(" START WITH t.CODE_PARENT_ID ='"+type+"' \r\n");
		sql.append(" CONNECT BY PRIOR t.code_id = t.CODE_PARENT_ID \r\n");
		sql.append(" ORDER SIBLINGS BY t.code_id "); 
		return dao.pageQuery(sql.toString(), null, this.getFunName());
	}
	public boolean isExists(String FinReturnType,String dealer,String month) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select *\r\n");
		sql.append(" from tt_dealer_pre_fin t\r\n");
		sql.append(" where t.dealer_id = "+dealer+"\r\n");
		sql.append("and t.MONTH >=\n");
		sql.append("    trunc(to_date('"+month+"', 'yyyy-mm-dd'), 'mm')\n");
		sql.append("and t.MONTH <=\n");
		sql.append("    last_day(to_date('"+month+"', 'yyyy-mm-dd'))\n");
		sql.append("and t.audit_status in(20011003,20011002)\n");
		sql.append("and t.fin_return_type ="+FinReturnType+"\n");

		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, this.getFunName());
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	public boolean isExistsByNoAudit(String FinReturnType,String dealer,String month) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select *\r\n");
		sql.append(" from tt_dealer_pre_fin t\r\n");
		sql.append(" where t.dealer_id = "+dealer+"\r\n");
		sql.append("and t.MONTH >=\n");
		sql.append("    trunc(to_date('"+month+"', 'yyyy-mm-dd'), 'mm')\n");
		sql.append("and t.MONTH <=\n");
		sql.append("    last_day(to_date('"+month+"', 'yyyy-mm-dd'))\n");
		sql.append("and t.audit_status in(20011001,20011004)\n");
		sql.append("and t.fin_return_type ="+FinReturnType+"\n");

		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, this.getFunName());
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
}
