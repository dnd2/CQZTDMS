package com.infodms.dms.dao.dealermanage;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmVsAddressPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;

public class DealerMyInfoDao {
	private static final Logger logger = Logger.getLogger(BaseDao.class);
	protected POFactory factory = POFactoryBuilder.getInstance();
	public int myupdate(TmVsAddressPO t1, TmVsAddressPO t2,String myaddress) {
		factory.update("update TM_DEALER set address='"+myaddress+"'",null);
		return factory.update(t1, t2);
	}
}
