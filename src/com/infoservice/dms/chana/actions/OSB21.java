package com.infoservice.dms.chana.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.Serializable;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.DeVehicleStorageDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.VehicleStorageVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

/**
 * @Title: DeVehicleShipping.java
 *
 * @Description:车辆入库信息下发
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-26
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class OSB21 {
	private Logger logger = Logger.getLogger(OSB21.class);
	public DeVehicleStorageDao dao = DeVehicleStorageDao.getInstance();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	private int flag;
	/*public String handleExecute() {
		logger.info("====订单发车记录下发开始====");
		DeUtility de = new DeUtility();
		try {
			int count = 0;//记录发送成功的记录条数
			List<VehicleStorageVO> list = dao.getDeVehicleShipping();
			if (list.size() > 0) {
				for (VehicleStorageVO vo : list) {
					try {
						HashMap<String, Serializable> body = new HashMap<String, Serializable>();
						body.put("1", vo);
						Map<String, Object> map = deCommonDao.getDmsDealerCode(vo.getEntityCode());
						de.sendAMsg("DRB21", map.get("DMS_CODE").toString(), body);
						count++;
						//更新接口状态为已下发
						commonDao.updateComplete("TT_VS_DLVRY_RPC", "DELIVERY_NO", vo.getRemark());
					} catch (Exception e) {
						logger.error("订单发车记录下发失败", e);
					}
				}
			}
			logger.info("====订单发车记录下发结束====, 下发了" + count + "条记录");
		} catch(Exception e) {
			logger.error("订单发车记录下发失败", e);
		}
		return null;
	}*/
	
	public int sendData(List<String> vins,String remark,String stockInType,Long inDealerId,Long outDealerId) {
		logger.info("====车辆入库信息下发开始====");
		DeUtility de = new DeUtility();
		try {
			Map<String, Object> mapIn = deCommonDao.getDmsDlrCodeNew(inDealerId);
				if(mapIn==null){			//不存在对应下端经销商
					this.flag = 0;
					return flag;
				}
			Map<String, Object> mapOut = deCommonDao.getDmsDlrCodeNew(outDealerId);
			if(mapOut!=null){
				if(mapIn.get("DMS_CODE").equals(mapOut.get("DMS_CODE"))){
					this.flag = -1;
					return flag;
				}
			}
			Map<String, Object> outDealer = dao.getDealer(outDealerId);
			List<VehicleStorageVO> vos = dao.getDeVehicleSorage(vins,stockInType,remark,outDealer);
			HashMap<String, Serializable> body = DEUtil.assembleBody(vos);
			de.sendAMsg("DRB21", mapIn.get("DMS_CODE").toString(), body);
			logger.info("====车辆入库信息下发结束====, 下发了" + body.size() + "条记录");
		} catch(Exception e) {
			logger.error("车辆入库信息下发失败", e);
		}
		this.flag = 1;
		return flag;
	}
	
/*	public static void main(String[] args) {
		ContextUtil.loadConf();
		OSB21 o = new OSB21();
		List<String> vins = new ArrayList();
		vins.add("LS4BAB3D3BG262400");
		vins.add("LS4BAB3D2BG262405");
		vins.add("LS4BAB3D0BG265111");
		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
		o.sendData(vins, "13071002","test",4010102908477402L,111111111L);
		POContext.endTxn(true);
	}*/
}
