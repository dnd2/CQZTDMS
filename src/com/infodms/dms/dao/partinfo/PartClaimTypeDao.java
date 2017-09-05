package com.infodms.dms.dao.partinfo;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class PartClaimTypeDao extends BaseDao<PO> {

	private static final PartClaimTypeDao dao = new PartClaimTypeDao();
	
	public static final PartClaimTypeDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Map<String, Object>> getClaimType() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT CLAIM_TYPE_ID, CLAIM_TYPE_CODE, CLAIM_TYPE_NAME FROM TM_PT_CLAIM_TYPE");
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}

}
