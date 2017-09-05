package com.infoservice.dms.chana.dao;

import org.apache.log4j.Logger;

public class DePartMonthReportDao extends AbstractIFDao {
	public static Logger logger = Logger.getLogger(DePartMonthReportDao.class);
	private static final DePartMonthReportDao dao = new DePartMonthReportDao ();
	
	public static final DePartMonthReportDao getInstance() {
		return dao;
	}
	
	
}
