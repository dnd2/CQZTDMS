package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.XHBUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;



@SuppressWarnings("unchecked")
public class OrgCustomUserRelationDao extends BaseDao{

	private static final OrgCustomUserRelationDao dao = new OrgCustomUserRelationDao();
	
	public static final OrgCustomUserRelationDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String,Object>> queryOrgCustomUserRelation(String name,String orgid,int pageSize,int curPage){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select distinct a.user_id USERID,a.name NAME ,a.acnt ACNT,replace(za_concat(distinct c.org_name),',',';') ORGNAME \r\n");
		sbSql.append("     from tc_user a\r\n");
		sbSql.append("     left join tm_org_cus_user_relation b on a.user_id= b.user_id\r\n");
		sbSql.append("     left join tm_org_custom c on c.org_id = b.org_id\r\n");
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
	/**根据userid查询用户拥有的组织
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getOrgCustomByOrgId(Long userid){
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.org_id ORG_ID \r\n");
		sql.append(" from tm_org_cus_user_relation t\r\n");
		sql.append(" where t.user_id = " + userid);
		return dao.pageQuery(sql.toString(), null, this.getFunName());
	}

	public void deleteOrgCustomUserRelation(String userid) {
		String sql = "delete from tm_org_cus_user_relation t where t.user_id ='"+userid+"'";
		this.delete(sql, null);
	}
	
	/**
	 * 根据部门查询部门的用户
	 * @param orgId
	 * @return
	 */
	public List<Map<String, Object>> queryCustomerInfo(String orgId){
		StringBuffer sql = new StringBuffer("");
		sql.append("select distinct a.user_id USERID,a.name NAME ,a.acnt ACNT,c.org_name ORGNAME \r\n");
		sql.append("     from tc_user a\r\n");
		sql.append("     inner join tm_org_cus_user_relation b on a.user_id= b.user_id\r\n");
		sql.append("     inner join tm_org_custom c on c.org_id = b.org_id\r\n");
		sql.append("     where a.USER_STATUS = "+Constant.STATUS_ENABLE+" and a.user_type=" +Constant.SYS_USER_SGM );
		List<Object> params = new ArrayList<Object>();
		if (!XHBUtil.IsNull(orgId)) {
			sql.append(" and c.org_id = ?");
			params.add(orgId);
		}
		return this.pageQuery(sql.toString(), params, this.getFunName());
	}
	
	/**
	 * 根据用户ID查询用户所属部门
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> queryCustomerInfoByUserId(String userId){
		StringBuffer sql = new StringBuffer("");
		sql.append("select distinct a.user_id USERID,a.name NAME ,a.acnt ACNT,c.org_id ORGID,c.org_name ORGNAME \r\n");
		sql.append("     from tc_user a\r\n");
		sql.append("     inner join tm_org_cus_user_relation b on a.user_id= b.user_id\r\n");
		sql.append("     inner join tm_org_custom c on c.org_id = b.org_id\r\n");
		sql.append("     where a.USER_STATUS = "+Constant.STATUS_ENABLE+" and a.user_type=" +Constant.SYS_USER_SGM );
		List<Object> params = new ArrayList<Object>();
		if (!XHBUtil.IsNull(userId)) {
			sql.append(" and a.user_id = ?");
			params.add(userId);
		}
		return this.pageQuery(sql.toString(), params, this.getFunName());
	}
	
	/**
	 * 根据用户ID查询用户所属大区信息
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> queryORgByUserId(String userId){
		StringBuffer sql = new StringBuffer("");
		sql.append("select distinct a.user_id USERID,a.name NAME ,a.acnt ACNT,c.org_id ORGID,c.org_name ORGNAME \r\n");
		sql.append("     from tc_user a\r\n");
		sql.append("     inner join tm_org_cus_user_relation b on a.user_id= b.user_id\r\n");
		sql.append("     inner join tm_org c on c.org_id = b.org_id\r\n");
		sql.append("     where a.USER_STATUS = "+Constant.STATUS_ENABLE+" and a.user_type=" +Constant.SYS_USER_SGM );
		List<Object> params = new ArrayList<Object>();
		if (!XHBUtil.IsNull(userId)) {
			sql.append(" and a.user_id = ?");
			params.add(userId);
		}
		return this.pageQuery(sql.toString(), params, this.getFunName());
	}

}
