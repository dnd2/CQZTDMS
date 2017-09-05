package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.dao.DeMonitorLabourDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.MonitorLabourVO;
import com.infoservice.po3.core.util.ContextUtil;
/**
 * 
* @ClassName: OSC09 
* @Description: TODO(下发工时监控) 
* @author liuqiang 
* @date Jul 30, 2010 2:34:31 PM 
*
 */
public class OSC09 {
	private DeMonitorLabourDao monitorLabourDao = DeMonitorLabourDao.getInstance();
	private static final Logger LOG = Logger.getLogger(OSC09.class);
	public static void main(String[] args) throws Exception {
		ContextUtil.loadConf();
		OSC09 service = new OSC09();
		service.execute("");
	}

	/**
	 * 
	 * @param ids 监控工时id列表
	 * @return
	 */
	public String execute(String ids) {
		LOG.info("====监控工时下发开始====");
		List<MonitorLabourVO> vos = monitorLabourDao.queryMonitorLabour(ids);
		if (null == vos || vos.size() == 0) {
			return null;
		}
		DeUtility de = new DeUtility();
		HashMap<String, Serializable> body = DEUtil.assembleBody(vos);
		try {
			de.sendAllMsg("DRC09", body);
		} catch (Exception e) {
			LOG.error("监控工时下发失败", e);
			//throw new RpcException(e);
		}
		LOG.info("====监控工时下发结束====,下发了(" + body.size() + ")条数据");
		return null;
	}

}
