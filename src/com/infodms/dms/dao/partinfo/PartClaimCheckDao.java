package com.infodms.dms.dao.partinfo;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPtClaimPO;
import com.infoservice.po3.bean.PO;

public class PartClaimCheckDao extends BaseDao<PO> {
	public static final Logger logger = Logger.getLogger(PartClaimCheckDao.class);
	private static final PartClaimCheckDao dao = new PartClaimCheckDao();
	public static final PartClaimCheckDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Map<String, Object>> queryClaimCheckItems(Long claimId) {
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT TO_CHAR(A.CHECK_DATE, 'YYYY-MM-DD')CHECK_DATE, A.CHECK_STATUS, A.CHECK_REMARK, B.ORG_NAME \n");
		sql.append(" FROM TT_PT_CLAIM_CHECK A, TM_ORG B ");
		sql.append(" WHERE A.CLAIM_ID = ").append(claimId);
		sql.append(" AND A.ORG_ID = B.ORG_ID");
		sql.append(" ORDER BY A.CHECK_ID DESC");
		List<Map<String, Object>> lists = pageQuery(sql.toString(), null, getFunName());
		return lists;
	}

}
