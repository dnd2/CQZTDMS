package com.infodms.dms.dao.partinfo;
import java.sql.ResultSet;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
public class PartInfoItemDao extends BaseDao<PO> {
	private static final PartInfoItemDao dao = new PartInfoItemDao();
	
	public static final PartInfoItemDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}


}
