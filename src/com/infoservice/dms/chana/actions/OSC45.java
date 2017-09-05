package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.DeSignDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.DeliveryOrderVO;

/**
 * 
* @ClassName: OSC45 
* @Description: TODO(配件采购订单签收) 
* @author liuqiang 
* @date Sep 6, 2010 2:33:43 PM 
*
 */
public class OSC45 {
	private final Logger LOG = Logger.getLogger(OSC45.class);
	private DeSignDao signDao = DeSignDao.getInstance();
	private DeCommonDao commonDao = DeCommonDao.getInstance();
	
	public String execute(String signNo) {
		LOG.info("====配件采购订单签收下发开始====" + signNo);
		List<DeliveryOrderVO> vos = signDao.querySign(signNo);
		if (null == vos || vos.size() == 0) {
			return null;
		}
		DeUtility de = new DeUtility();
		HashMap<String, Serializable> body = DEUtil.assembleBody(vos);
		try {
			Map<String, Object> map = commonDao.getDmsDealerCode(vos.get(0).getEntityCode());
			de.sendAMsg("DRC45", map.get("DMS_CODE").toString(), body);
		} catch (Exception e) {
			LOG.error("配件采购订单签收下发开始", e);
		}
		LOG.info("====配件采购订单签收下发开始====,下发了(" + signNo + ")");
		return null;
	
	}
	
	public static void main(String[] args) {
		OSC45 o = new OSC45();
		o.execute("1");
	}
}
