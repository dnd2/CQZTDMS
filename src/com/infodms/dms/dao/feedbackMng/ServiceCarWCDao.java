package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ServiceCarWCDao extends BaseDao {
	private ServiceCarWCDao(){}
	public static ServiceCarWCDao getInstance(){
		return new ServiceCarWCDao();
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public PageResult<Map<String,Object>> queryServicecar(String con,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select s.*,d.dealer_name,d.dealer_code,dd.dealer_name dealer_name1 from tt_as_servicecar s , tm_dealer d,tm_dealer dd where s.dealer_id2 = dd.dealer_id and s.dealer_id = d.dealer_id\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append(" order by s.apply_no desc\n");
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	public PageResult<Map<String,Object>> areaQuery(String con,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select s.*, d.dealer_name, d.dealer_code\n");
		sql.append("  from tt_as_servicecar s, tm_dealer d, VW_ORG_DEALER_SERVICE v\n");  
		sql.append(" where s.dealer_id = d.dealer_id\n");  
		sql.append("   and d.dealer_id = v.dealer_id\n");  
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append(" order by s.apply_no desc\n");
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	public List<Map<String,Object>> queryAuditDetail(String id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select a.*, u.name\n");
		sql.append("  from tt_as_servicecar_audit a, tc_user u\n");  
		sql.append(" where a.audit_man = u.user_id\n");
		sql.append("   and a.apply_id = ").append(id).append("\n");
		sql.append(" order by a.create_date desc\n");
		return this.pageQuery(sql.toString(), null,this.getFunName());
	}
	
	public List<Map<String,Object>> printAuditDetail(String id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select a.*, u.name\n");
		sql.append("  from tt_as_servicecar_audit a, tc_user u\n");  
		sql.append(" where a.audit_man = u.user_id\n");
		sql.append("   and a.audit_status != "+Constant.SERVICE_CAR_APPLY_02+"\n");
		sql.append("   and a.audit_status like '"+Constant.SERVICE_CAR_APPLY+"%'\n");
		sql.append("   and a.apply_id = ").append(id).append("\n");
		sql.append(" order by a.create_date desc\n");
		return this.pageQuery(sql.toString(), null,this.getFunName());
	}
	
	public List<Map<String,Object>> queryRelation(String id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select distinct s.*,TO_CHAR(A.CREATE_DATE,'yyyy-MM-dd') REPORT,to_char(B.CREATE_DATE,'yyyy-MM-dd') PASS from tt_as_servicecar s ,tt_as_servicecar ss, tm_dealer d,TT_AS_SERVICECAR_AUDIT A,TT_AS_SERVICECAR_AUDIT B\n");
		sql.append(" where s.dealer_id = d.dealer_id\n");  
		sql.append("   AND A.APPLY_ID = S.ID\n");
		sql.append("   AND B.APPLY_ID = S.ID\n"); 
		sql.append("   and s.dealer_id = ss.dealer_id");
		sql.append("   and ss.id="+id+"\n");
		sql.append("   AND A.AUDIT_STATUS = "+Constant.SERVICE_CAR_APPLY_02+"\n");  
		sql.append("   AND B.AUDIT_STATUS = "+Constant.SERVICE_CAR_APPLY_08+"\n");
		sql.append("   and (s.status = "+Constant.SERVICE_CAR_APPLY_10+" or s.status = "+Constant.SERVICE_CAR_APPLY_08+")\n");
		return this.pageQuery(sql.toString(), null,this.getFunName());
	}
}
