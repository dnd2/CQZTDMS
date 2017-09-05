	package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;



@SuppressWarnings("unchecked")
public class OrgCustomUserLevelRelationDao extends BaseDao{

	private static final OrgCustomUserLevelRelationDao dao = new OrgCustomUserLevelRelationDao();
	
	public static final OrgCustomUserLevelRelationDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String,Object>> queryOrgCustomUserLevelRelation(String name,String orgid,int pageSize,int curPage){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select distinct a.user_id userId, a.name NAME ,a.acnt ACNT,replace(za_concat(distinct c.org_name),',',';') ORGNAME,\r\n");
		sbSql.append("       replace(za_concat(distinct e.code_desc),',',';') ORGLEVEL\r\n");
		sbSql.append("     from tm_org_cus_user_relation b \r\n");
		sbSql.append("     left join tc_user a on a.user_id= b.user_id\r\n");
		sbSql.append("     left join tm_org_custom c on c.org_id = b.org_id\r\n");
		
		sbSql.append("	   left join tm_org_cus_user_level_relation d on b.org_id = d.org_id and b.user_id = d.user_id\r\n");
		sbSql.append("     left join tc_code e on d.oculr_level  = e.code_id"); 
		
		sbSql.append("     where a.USER_STATUS = "+Constant.STATUS_ENABLE+" and a.user_type=" +Constant.SYS_USER_SGM );
		if(StringUtil.notNull(name)){
			sbSql.append("  and a.name like '%"+name+"%'\n");
		}
		if(StringUtil.notNull(orgid)){
			sbSql.append("  and c.org_id = '"+orgid+"'\n");
		}
		sbSql.append(" group by a.user_id,a.name,a.acnt"); 	

		return (PageResult<Map<String, Object>>)this.pageQuery(sbSql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}


	public void deleteOrgCustomUserRelation(String userid) {
		String sql = "delete from tm_org_cus_user_relation t where t.user_id ='"+userid+"'";
		this.delete(sql, null);
	}
	
	/**根据userid查询用户拥有的组织下的职位权限
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getOrgCustomUserLevelRelationByUserId(
			long userid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.org_id ORG_ID,t.oculr_level ORGLEVEL \r\n");
		sql.append(" from tm_org_cus_user_level_relation t\r\n");
		sql.append(" where t.user_id = " + userid );
		return dao.pageQuery(sql.toString(), null, this.getFunName());
	}

	public void deleteOrgCustomUserLevelRelation(String userid) {
		String sql = "delete from tm_org_cus_user_level_relation t where t.user_id ='"+userid+"'";
		this.delete(sql, null);
	}
	


}
