package com.infodms.dms.actions.util;

import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.claim.basicData.HomePageNewsDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;

public class FileDownLoadDao extends BaseDao {

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static Logger logger = Logger.getLogger(FileDownLoadDao.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final FileDownLoadDao dao = new FileDownLoadDao ();
	public static final FileDownLoadDao getInstance() {
		return dao;
	}

}
