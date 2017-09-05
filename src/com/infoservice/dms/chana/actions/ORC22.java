package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageVehicleStatusDao;
import com.infodms.dms.po.TpAsServiceVehiclePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.de.DEMessage;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeActivityDao;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.vo.ActivityResultVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;
/**
 * 
* @ClassName: ORC22 
* @Description: TODO(服务车辆完工上报 接受下端的数据) 
* @author liuqiang 
* @date Aug 3, 2010 2:39:00 PM 
*
 */
public class ORC22 extends AbstractReceiveAction {
	private Logger LOG = Logger.getLogger(ORC22.class);
	private ServiceActivityManageVehicleStatusDao dao = ServiceActivityManageVehicleStatusDao.getInstance();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	private DeActivityDao deActivityDao = DeActivityDao.getInstance();
	@Override
	protected DEMessage handleExecutor(DEMessage msg) {
		LOG.info("====服务车辆完工上报接收开始====");
		Map<String, Serializable> bodys = msg.getBody();
		for (Entry<String, Serializable> entry : bodys.entrySet()) {
			ActivityResultVO vo = new ActivityResultVO();
			vo = (ActivityResultVO) entry.getValue();
			try {
				POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);	
				updateActivityVehicle(vo);
				POContext.endTxn(true);
			} catch (Exception e) {
				POContext.endTxn(false);
				LOG.error("服务车辆完工上报接收失败", e);
				throw new RpcException(e);
			}finally{
				 POContext.cleanTxn();
			}	
		}
		LOG.info("====服务车辆完工上报接收结束====");
		return null;
	}
	/**
	 * 
	* @Title: updateActivityVehicle 
	* @Description: TODO(根据上报的车辆信息更新活动车辆表) 
	* @param @param vo   
	* 更新字段： OPERATE_DEALER_CODE:实际维修站编码, CAR_STATUS = 11051203 已经修理完成
	* 条件字段： activityId:服务活动ID,  VIN
	* @return void    返回类型 
	 * @throws Exception 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void updateActivityVehicle(ActivityResultVO vo) throws Exception {
//		TtAsActivityPO activity = new TtAsActivityPO();
//		activity.setActivityCode(vo.getActivityCode());
//		//根据活动编号查询活动ID
//		List<TtAsActivityPO> pos = dao.select(activity, 0);
//		if (pos == null || pos.size() == 0) {
//			throw new IllegalArgumentException("Can't find TtAsActivityPO by activity code == " + vo.getActivityCode());
//		}
//		//条件 activityId, vin
//		TtAsActivityVehiclePO oldpo = new TtAsActivityVehiclePO();
//		oldpo.setActivityId(pos.get(0).getActivityId());
//		oldpo.setVin(vo.getVin());
//		//根据活动Id和VIN查询车辆
//		List<TtAsActivityVehiclePO> vpos = dao.queryVehicleByVin(oldpo);
//		//更新车辆状态 已经修理完成, 实际经销商
//		TtAsActivityVehiclePO newpo = new TtAsActivityVehiclePO();
//		newpo.setCarStatus(Constant.SERVICEACTIVITY_CAR_STATUS_02);
//		Map<String, Object> map = deCommonDao.getDcsDealerCode(vo.getRealEntityCode());
//		newpo.setOperateDealerCode(map.get("DEALER_CODE").toString());
//		dao.update(oldpo, newpo);
		TpAsServiceVehiclePO po = new TpAsServiceVehiclePO();
		po.setActivityCode(vo.getActivityCode());
		po.setVin(vo.getVin());
		po.setCampaignDate(vo.getCampaignDate());
		Map<String, Object> map = deCommonDao.getDcsDealerCode(vo.getRealEntityCode());
		po.setOperateSstCode(map.get("DEALER_CODE").toString());
		//根据活动Id和VIN查询服务活动完工上报的车
		List pos = deActivityDao.select(po, 0);
		if (null == pos || pos.size() == 0) {
			po.setId(Long.parseLong(SequenceManager.getSequence("")));
			po.setIfStatus(DEConstant.IF_STATUS_0);
			deActivityDao.insert(po);
		} else {
			TpAsServiceVehiclePO newpo = new TpAsServiceVehiclePO();
			newpo.setIfStatus(DEConstant.IF_STATUS_0);
			deActivityDao.update(po, newpo);
		}
	}
	
/*	public static void main(String[] args) throws Exception {
		ActivityResultVO vo = new ActivityResultVO();
		vo.setActivityCode("SAN20100722002927");
		vo.setVin("LFV10000000000510");
		vo.setRealEntityCode("CA500100");
		ContextUtil.loadConf();
		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
		ORC22 a = new ORC22();
		a.updateActivityVehicle(vo);
		POContext.endTxn(true);
	}*/

}
