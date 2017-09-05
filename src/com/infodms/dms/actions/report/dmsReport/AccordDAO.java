package com.infodms.dms.actions.report.dmsReport;

import java.sql.ResultSet;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;


@SuppressWarnings("rawtypes")
public class AccordDAO extends BaseDao{

	private static final AccordDAO dao = new AccordDAO();
	public static AccordDAO getInstance(){
		if (dao == null) {
			return new AccordDAO();
		}
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

}
