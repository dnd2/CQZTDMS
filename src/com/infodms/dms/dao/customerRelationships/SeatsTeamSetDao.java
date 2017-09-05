package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCrmSeatsTeamPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class SeatsTeamSetDao extends BaseDao{

	private static final SeatsTeamSetDao dao = new SeatsTeamSetDao();
	
	public static final SeatsTeamSetDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String,Object>> querySeatsTeamSet(String seatsName,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.st_id stid,t.st_name stName,t.st_memo stMemo,t.st_code stCode \n");
		sql.append("  from TT_CRM_SEATS_TEAM t where t.status = "+Constant.STATUS_ENABLE+" \n");

		if(StringUtil.notNull(seatsName)){
			sql.append(" and t.st_name like '%"+seatsName+"%'\n");
		}
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}

	public TtCrmSeatsTeamPO queryTtCrmSeatsTeamPO(TtCrmSeatsTeamPO ttCrmSeatsTeamPO) {
		List<TtCrmSeatsTeamPO> lists = this.select(ttCrmSeatsTeamPO);
		if(lists.size()>0) return lists.get(0);
		return null ;
	}
	
	public List<TtCrmSeatsTeamPO>  querySeatsTeamSet(){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from TT_CRM_SEATS_TEAM t where t.status = "+Constant.STATUS_ENABLE);
		List<TtCrmSeatsTeamPO> list = this.select(TtCrmSeatsTeamPO.class, sql.toString(), null);
		return list;
	}

}
