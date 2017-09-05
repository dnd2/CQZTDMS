/**
 * 
 */
package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;

import org.apache.log4j.Logger;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;


public class StandardVipReportDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(StandardVipReportDao.class);
	
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
