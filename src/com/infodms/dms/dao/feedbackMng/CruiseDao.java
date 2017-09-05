package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.TAWCruiseAuditBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CruiseDao extends BaseDao{
	private CruiseDao(){}
	public static CruiseDao getInstance(){
		return new CruiseDao();
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PageResult<Map<String,Object>> queryCruiseApply(String con,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select c.* \n");
		sql.append("  from tt_as_wr_cruise c,vw_org_dealer_service s\n");
		sql.append(" where 1=1\n");
		sql.append("   and c.dealer_id = s.dealer_id\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append(" order by c.cr_no desc \n");
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	public PageResult<Map<String,Object>> queryCruiseApply1(String con,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select c.* \n");
		sql.append("  from tt_as_wr_cruise c\n");
		sql.append(" where 1=1\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append(" order by c.cr_no desc \n");
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	public Map<String, Object> getDealerInfo(String dealerId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DEALER_CODE, DEALER_SHORTNAME, DEALER_NAME,\n" );
		sql.append("       TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24') SDATE\n" );
		sql.append("FROM TM_DEALER\n" );
		sql.append("WHERE DEALER_ID = ").append(dealerId);

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	public List<TAWCruiseAuditBean> getAuditInfo(Long id){
		StringBuffer sql = new StringBuffer();
		sql.append("select u.name, a.auditing_date, a.status, a.auditing_opinion\n");
		sql.append("  from tt_as_wr_cruise_auditing a, tc_user u\n");  
		sql.append(" where a.auditing_person = u.user_id\n");  
		sql.append("   and a.cr_id ="+id+"\n");
		sql.append(" order by a.create_date desc\n");
		return this.select(TAWCruiseAuditBean.class, sql.toString(), null);
	}
}
