package com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage;

import java.sql.ResultSet;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class FleetAuditDao  extends BaseDao{

	private static final FleetAuditDao dao = new FleetAuditDao();
	
	public static final FleetAuditDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
