package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.CommonDao;
import com.infoservice.dms.chana.dao.DeActivityDao;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.ActivityVO;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.po3.core.util.ContextUtil;
/**
 * 
* @ClassName: OSC23 
* @Description: TODO(服务车辆完工下发) 
* @author liuqiang 
* @date Aug 3, 2010 2:39:27 PM 
*
 */
public class OSC23 extends AbstractSendTask {
	private DeActivityDao activityDao = DeActivityDao.getInstance();
	private CommonDao commonDao =CommonDao.getInstance();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	private Logger LOG = Logger.getLogger(OSC23.class); 
	
	public static void main(String[] args) throws Exception {
		ContextUtil.loadConf();
		OSC23 a = new OSC23();
		a.execute();
	}

	@Override
	protected String handleExecute() {
		//commonDao.updateSending("", "", "");
		//查询所有修好上报的待下发的服务车辆
		LOG.info("====服务车辆完工下发开始====");
		try {
			List<ActivityVO> vos = activityDao.queryActivityResultVO();
			if (null == vos || vos.size() == 0) {
				return null;
			}
			DeUtility de = new DeUtility();
			HashMap<String, Serializable> body = DEUtil.assembleBody(vos);
			de.sendAllMsg("DRC23", body);
			for (ActivityVO vo : vos) {
				commonDao.updateComplete("TP_AS_SERVICE_VEHICLE", "ACTIVITY_CODE", vo.getActivityCode());
			}
//			for (ActivityVO vo : vos) {
//				Map<String, Object> map = deCommonDao.getDmsDealerCode(vo.getEntityCode());
//				de.sendAMsg("DRC23", map.get("DMS_CODE").toString(), body);
//				commonDao.updateComplete("TP_AS_SERVICE_VEHICLE", "ACTIVITY_CODE", vo.getActivityCode());
//			}
		} catch (Exception e) {
			LOG.error("服务车辆完工下发失败", e);
			throw new RpcException(e);
		}
		LOG.info("====服务车辆完工下发结束====");
		return null;
	}
}
