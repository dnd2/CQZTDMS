package com.infoservice.dms.chana.actions;

import org.apache.log4j.Logger;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.schedule.Task;

public abstract class AbstractSendTask extends Task {
	private static final Logger logger = Logger.getLogger(AbstractSendTask.class);
	@Override
	public String execute() {
		try {
			POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
			handleExecute();
			POContext.endTxn(true);
		} catch (Exception e) {
			POContext.endTxn(false);
			logger.error(e, e);
			//throw new RpcException();
		} finally {
			POContext.cleanTxn();
		}
		return null;
	}

	protected abstract String handleExecute() throws Exception;

}
