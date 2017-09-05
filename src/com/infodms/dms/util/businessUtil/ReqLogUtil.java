package com.infodms.dms.util.businessUtil;

import java.sql.ResultSet;
import java.util.Date;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtVsReqChngLogPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;

public class ReqLogUtil extends BaseDao {

	private static final ReqLogUtil dao = new ReqLogUtil();
	public static final ReqLogUtil getInstance() {
		return dao;
	}
	
	public static void creatReqLog(Long reqId, int chngType, Long userId){
		TtVsReqChngLogPO reqChngLogPO = new TtVsReqChngLogPO();
		Long logId = Long.parseLong(SequenceManager.getSequence(""));
		if (0 != reqId) {
			reqChngLogPO.setLogId(logId);
			reqChngLogPO.setReqId(reqId);
			reqChngLogPO.setChngType(chngType);
			reqChngLogPO.setChngDate(new Date());
			reqChngLogPO.setUserId(userId);
			
			dao.insert(reqChngLogPO);
		}
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
