package com.infodms.dms.dao.common;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerOrgRelationPO;
import com.infoservice.po3.bean.PO;

public class DealerUtilDAO extends BaseDao {
	private static final DealerUtilDAO dao = new DealerUtilDAO();

	public static final DealerUtilDAO getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TmDealerOrgRelationPO> getDealerIds(Long orgId) {

		LinkedList params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("select  dealer_id ");
		sql.append(" from tm_dealer_org_relation t ");
		sql.append(" where  1=1 ");
		sql.append(" and org_id=? ");
		params.add(orgId.toString());
		List<TmDealerOrgRelationPO> rs = select(TmDealerOrgRelationPO.class, sql.toString(), params);
		return rs;

	}
	//返回一个组织下所有的经销商代码，各经销商以,隔开
	public String getDealerIdsToString(Long orgId) {
		StringBuffer sb = new StringBuffer();
		List<TmDealerOrgRelationPO> rs = getDealerIds(orgId);
		if (rs!=null) {
			for (int i = 0;i<rs.size();i++) {
				sb.append(rs.get(i).getDealerId().toString());
				sb.append(",");
			}
		}
		return sb.toString();
	}
}
