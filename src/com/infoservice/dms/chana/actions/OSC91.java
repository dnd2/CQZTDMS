package com.infoservice.dms.chana.actions;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.infoservice.de.DEException;
import com.infoservice.de.DEMessage;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeVehicleDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.VehicleVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;

/**
 * @Title: DeVehicle.java
 *
 * @Description:同步查询车辆信息接口
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-27
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class OSC91 extends AbstractReceiveAction {
	private static Logger logger = Logger.getLogger(OSC91.class);
	public static DeVehicleDao dao = DeVehicleDao.getInstance();
	@Override
	
	protected DEMessage handleExecutor(DEMessage msg) {
		logger.info("====同步查询车辆信息开始====");
		Map<String, Serializable> bodys = msg.getBody();// 获得来至FRM2的消息数据
		for (Entry<String, Serializable> entry : bodys.entrySet()) {
			VehicleVO vo = new VehicleVO();
			vo = (VehicleVO) entry.getValue();
			try {
				POContext.beginTxn(DBService.getInstance().getDefTxnManager(),
						-1); // 事务开启
				List<VehicleVO> list = dao.getDeVehicle(vo);
				if (null == list || list.size() == 0) {
					return wrapperMsg(new VehicleVO(), "没有在车辆表里找到"
							+ vo.getVin());
				}
				VehicleVO vv = new VehicleVO();
				vv = list.get(0);
				vv.setEntityCode(vo.getEntityCode());
				DEMessage rmsg = wrapperMsg(vv, null);
				POContext.endTxn(true);
				return rmsg;
			} catch (Exception e) {
				POContext.endTxn(false);
				logger.error("同步查询车辆信息失败", e);
				throw new RpcException(e);
			} finally {
				POContext.cleanTxn();
			}
		}
		logger.info("====同步查询车辆信息结束====");
		return null;
	}
	
	private DEMessage wrapperMsg(VehicleVO vo, String msg) {
		if (null != msg) {
			//出错的时候
			vo = DeUtility.wrapperMsg(vo, msg);
		}
		HashMap<String, Serializable> body = new HashMap<String, Serializable>();
		body.put("body", vo);
		DeUtility de = new DeUtility();
		DEMessage rmsg = null;
		try {
			rmsg = de.assembleDEMessage("DRB33", body);
		} catch (DEException e) {
			logger.error(e, e);
		}
		return rmsg;
	}
	
}
