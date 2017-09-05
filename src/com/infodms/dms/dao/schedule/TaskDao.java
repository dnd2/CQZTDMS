package com.infodms.dms.dao.schedule;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class TaskDao extends BaseDao<PO> {
	public static final Logger logger = Logger.getLogger(TaskDao.class);
	private static final TaskDao dao = new TaskDao();
	
	public static final TaskDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
