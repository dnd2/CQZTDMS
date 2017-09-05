package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeMonitorPartDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.MonitorPartVO;
import com.infoservice.po3.core.util.ContextUtil;
/**
 * 
* @ClassName: OSC10 
* @Description: TODO(下发监控配件) 
* @author liuqiang 
* @date Aug 3, 2010 2:37:12 PM 
*
 */
public class OSC10 {
	private DeMonitorPartDao monitorPartDao = DeMonitorPartDao.getInstance();
	private static final Logger LOG = Logger.getLogger(OSC10.class);
	
	public static void main(String[] args) throws Exception {
		ContextUtil.loadConf();
		OSC10 service = new OSC10();
		service.execute("");
	}

	/**
	 * 
	 * @param ids 配件id列表
	 * @return
	 * @throws Exception
	 */
	public String execute(String ids) {
		LOG.info("====监控配件下发开始====");
		List<MonitorPartVO> vos = monitorPartDao.queryMonitorPart(ids);
		if (null == vos || vos.size() == 0) {
			return null;
		}
		DeUtility de = new DeUtility();
		HashMap<String, Serializable> body = DEUtil.assembleBody(vos);
		try {
			de.sendAllMsg("DRC10", body);
		} catch (Exception e) {
			LOG.error("监控配件下发失败", e);
			//throw new RpcException(e);
		}
		LOG.info("====监控配件下发结束====,下发了(" + body.size() + ")条数据");
		return null;
	
	}
}
