package com.infodms.dms.dao.common;

import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.infoservice.po3.bean.PO;

public class PopSelectDAO  extends BaseDao<PO>{
    public static Logger logger = Logger.getLogger(PopSelectDAO.class);
    private static PopSelectDAO dao = new PopSelectDAO();
	public static final PopSelectDAO getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
