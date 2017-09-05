package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmOrgRegionRelationPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class RegionManageDao extends BaseDao{

	private static final RegionManageDao dao = new RegionManageDao();
	
	public static final RegionManageDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String,Object>> queryRegionManage(String orgName,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" select replace(za_concat(reg.region_name),',',';') REGIONNAME,org.org_name ORGNAME,org.org_id ORGID ");
		sql.append(" from tm_ORG_region_relation relation ");
		sql.append(" left join tm_region reg on relation.region_id = reg.region_id ");
		sql.append(" left join tm_org org on relation.org_id = org.org_id ");
		sql.append(" where relation.status = "+Constant.STATUS_ENABLE );
		

		if(StringUtil.notNull(orgName)){
			sql.append(" and org.org_name like '%"+orgName+"%'\n");
		}
		sql.append(" group by org.org_id,org.org_name ");
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}

	
	public List<TmOrgRegionRelationPO>  queryTmOrgRegionRelation(long orgId){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tm_ORG_region_relation relation where relation.org_id = "+orgId);
		List<TmOrgRegionRelationPO> list = this.select(TmOrgRegionRelationPO.class, sql.toString(), null);
		return list;
	}
	
	public void deleteTmOrgRegionRelationPO(String orgIds){
		String sql = "delete from tm_ORG_region_relation relation where relation.org_id in('"+orgIds+"')";
		this.delete(sql, null);
	}
}
