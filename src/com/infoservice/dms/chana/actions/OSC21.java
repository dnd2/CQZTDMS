package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageDealerDao;
import com.infodms.dms.po.TtAsActivityPO;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeActivityDao;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.ActivityVO;
import com.infoservice.po3.core.util.ContextUtil;
/**
 * 
* @ClassName: OSC21 
* @Description: TODO(下发服务活动,包括车型 车辆 维修项目 活动配件) 
* @author liuqiang 
* @date Aug 3, 2010 2:37:55 PM 
*
 */
public class OSC21 {
	
	private static final Logger LOG = Logger.getLogger(OSC21.class);
	private DeActivityDao activityDao = DeActivityDao.getInstance();
	private ServiceActivityManageDealerDao activityManagerDealerDao = ServiceActivityManageDealerDao.getInstance();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	
	public static void main(String[] args) throws Exception {
		ContextUtil.loadConf();
		OSC21 a = new OSC21();
		TtAsActivityPO po = new TtAsActivityPO();
		po.setActivityId(2010072200069692L);
		a.execute(po);
		//a.handleExecute();
	}

//	public String execute(TtAsActivityPO po) throws Exception {
//		List<ActivityVO> vos = activityDao.queryActivity();
//		if (null == vos || vos.size() == 0) {
//			return null;
//		}
//		DeUtility de = new DeUtility();
//		HashMap<String, Serializable> body = DEUtil.assembleBody(vos);
//		de.sendAllMsg("DRC21", body);
//		System.out.println(vos);
//		return null;
//	}
	
	public String execute(TtAsActivityPO po) {
		LOG.info("====服务活动下发开始====");
		//根据服务活动ID查询服务活动
		List<ActivityVO> vos = activityDao.queryActivity(po.getActivityId());
		if (null == vos || vos.size() == 0) {
			return null;
		}
		DeUtility de = new DeUtility();
		HashMap<String, Serializable> body = DEUtil.assembleBody(vos);
		try {
			//根据服务活动ID查询执行经销商
			List<Map<String, Object>> list = activityManagerDealerDao.queryDealerByActivityId(po.getActivityId());
			//要发数据的经销商列表
			List<String> dealerCodes = new ArrayList<String>();
			for (Map<String, Object> dealer : list) {
				try {
					Map<String, Object> dmsDealer = deCommonDao.getDmsDealerCode(dealer.get("DEALER_CODE").toString());
					//可下发的经销商列表
					dealerCodes.add(dmsDealer.get("DMS_CODE").toString());
					
				} catch (Exception e) {
					LOG.error("Cann't send to " + dealer.get("DEALER_CODE").toString(), e);
				}
			}
			if (dealerCodes.size() > 0) {
				de.sendMsg("DRC21", dealerCodes, body);
				LOG.info("====服务活动下发结束====,下发了(" + body.size() + ")条数据");
			}
		} catch (Exception e) {
			LOG.info("服务活动下发失败", e);
			//throw new RpcException(e);
		}
		return null;
	}
}
