package com.infodms.dms.job.erp.dealer;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infoservice.dms.chana.actions.AbstractSendTask;

/**
 * ERP经销商主数据同步
 * @author wangsongwei
 */
public class ErpDealerSyncJob extends AbstractSendTask{

	private static final Logger logger = Logger.getLogger(ErpDealerSyncJob.class);
	private ErpDealerSyncJobDao dao = ErpDealerSyncJobDao.getInstance();
			
	@Override
	protected String handleExecute() throws Exception {
		
		List<Map<String, Object>> dealerList = dao.getSyncDealerList();
		
		for(Map<String, Object> dealerMap : dealerList) {
			if(dao.companyExist(dealerMap.get("COMPANYCODE").toString())) {
				
			} else {
				
			}
			
			if(dao.dealerExist(dealerMap.get("DEALERCODE").toString())) {
				
			} else {
				
			}
		}
		
		return null;
	}

}
