package com.infoservice.dms.chana.actions;

import org.apache.log4j.Logger;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;

public abstract class AbstractSendAction {
	private static final Logger logger = Logger.getLogger(AbstractSendAction.class);
	
	public String execute() {
		try {
			POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
			handleExecute();
		} catch (Exception e) {
			logger.error(e, e);
			//throw new RpcException(e);
		} finally {
			POContext.endTxn(true);
		}
		return null;
	}

	protected abstract String handleExecute() throws Exception;
}
