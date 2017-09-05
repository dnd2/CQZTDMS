package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.UrlFunctionDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.UrlFunctionVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

public class OSS01 {
	private static final Logger LOG = Logger.getLogger(OSS01.class);
	private UrlFunctionDao urlFunctionDao = UrlFunctionDao.getInstance();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();

	public String execute() {
		LOG.info("====URL功能列表下发开始====");
		List<UrlFunctionVO> vos = urlFunctionDao.queryUrlFunction();
		if (null == vos || vos.size() == 0) {
			return null;
		}
		DeUtility de = new DeUtility();
		HashMap<String, Serializable> body = DEUtil.assembleBody(vos);
		try {
			de.sendAllMsg("DRS01", body);
		} catch (Exception e) {
			LOG.error("URL功能列表下发", e);
			//throw new RpcException(e);
		}
		LOG.info("====URL功能列表下发结束====,下发了(" + body.size() + ")条数据");
		return null;
	}
	
	public List<String> sendData(List<String> dealerCodes) throws Exception {
		LOG.info("====URL功能列表下发开始==== " + dealerCodes.size());
		List<UrlFunctionVO> vos = urlFunctionDao.queryUrlFunction();
		if (null == vos || vos.size() == 0) {
			return null;
		}
		List<String> dmsCodes = new ArrayList<String>();
		List<String> errCodes = new ArrayList<String>();
		for (String dealerCode : dealerCodes) {
			try {
				Map<String, Object> dmsDealer = deCommonDao.getDmsDealerCode(dealerCode);
				//可下发的经销商列表
				dmsCodes.add(dmsDealer.get("DMS_CODE").toString());
			} catch (Exception e) {
				LOG.error("Cann't send to " + dealerCode, e);
				errCodes.add(dealerCode);
			}
		}
		HashMap<String, Serializable> body = DEUtil.assembleBody(vos);
		DeUtility de = new DeUtility();
		if (dmsCodes.size() > 0) {
			de.sendMsg("DRS01", dmsCodes, body);
			LOG.info("====物料下发结束====,下发了(" + body.size() + ")条数据");
		}
		return errCodes;
	}
	
	public static void main(String[] args) throws Exception {
		ContextUtil.loadConf();
		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
		OSS01 o = new OSS01();
		o.execute();
		POContext.endTxn(true);
	}
}
