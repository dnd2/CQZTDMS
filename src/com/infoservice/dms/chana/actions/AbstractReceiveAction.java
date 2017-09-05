package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infoservice.de.DEException;
import com.infoservice.de.DEMessage;
import com.infoservice.de.action.MsgAction;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;

public abstract class AbstractReceiveAction implements MsgAction {
	
	private static final Logger logger = Logger.getLogger(AbstractReceiveAction.class);
	
	public DEMessage executor(DEMessage msg) throws DEException {
		try {
			//POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
			appendStr(msg);
			Map<String, Serializable> bodys = msg.getBody();
			logger.info("共有======" + bodys.entrySet().size() + " 数据");
			DEMessage rmsg = handleExecutor(msg);
			//POContext.endTxn(true);
			return rmsg;
		} catch (Exception e) {
			logger.error(e, e);
			throw new DEException(e);
		}
	}

	private String appendStr(DEMessage msg) {
		StringBuilder strB = new StringBuilder();
		strB.append("[source ====== ]").append(msg.getSource()).append("\n");
		strB.append("[bizType ====== ]").append(msg.getBizType()).append("\n");// 发送源
		strB.append("[msgId ====== ]").append(msg.getMsgId()).append("\n");
		strB.append("[destination ====== ]").append(msg.getDestination()).append("\n");// 发送目的
		logger.info(strB.toString());
		return strB.toString();
	}
	
	protected abstract DEMessage handleExecutor(DEMessage msg);
	
}
