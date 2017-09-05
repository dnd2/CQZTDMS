package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.List;
import org.apache.log4j.Logger;
import com.infodms.dms.po.TtDealerActualSalesPO;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeDeSalesVehicleApplyResultDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.SalesVehicleApplyResultVO;
import com.infoservice.po3.core.util.ContextUtil;
import com.infodms.dms.common.Constant;
import java.util.HashMap;

/**
 * @Title: DeSalesVehicleApplyResult.java
 *
 * @Description:经销商销售上报结果下发
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
public class OSB32 {
	private static Logger logger = Logger.getLogger(OSB32.class);
	// private DeDeSalesVehicleApplyResultDao dao = DeDeSalesVehicleApplyResultDao.getInstance();
	
	public static void main(String[] args){
		try{
		ContextUtil.loadConf();
		TtDealerActualSalesPO po = new TtDealerActualSalesPO();
		po.setOrderId(2010100103149903L);
		OSB32 o = new OSB32();
		o.execute(po);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void execute(TtDealerActualSalesPO po) throws Exception {
		logger.info("====经销商销售上报结果下发开始==== orderId == " + po.getOrderId());
		try{
			DeDeSalesVehicleApplyResultDao dao = DeDeSalesVehicleApplyResultDao.getInstance();
			DeUtility de = new DeUtility();
			//根据订单ID获取经销商级别
			String dealerLevel = dao.getDealerlevel(po);
			logger.info("dealer level == " + dealerLevel);
			List<SalesVehicleApplyResultVO> list = null;
			HashMap<String, Serializable> body = new HashMap<String, Serializable>();
			SalesVehicleApplyResultVO vo = new SalesVehicleApplyResultVO();
			if (Integer.parseInt(dealerLevel) == Constant.DEALER_LEVEL_01) {//一级经销商
				list = dao.getDealerLevel01Result(po);
				if (null == list || list.size() == 0) {
					throw new IllegalArgumentException("没有找到实销上报单, orderId = " + po.getOrderId());
				}
				vo = list.get(0);
				body.put("body", vo);
				de.sendAMsg("DRB32", vo.getEntityCode(), body);//一级经销商直接通知结果
			} else {//二级经销商
				list = dao.getDealerLevel02Result(po);
				if (null == list || list.size() == 0) {
					throw new IllegalArgumentException("没有找到实销上报单, orderId = " + po.getOrderId());
				}
				vo = list.get(0);
				body.put("body", vo);
				de.sendAMsg("DRB32", vo.getSubEntityCode(), body);//二级经销商先通知结果
				
				/*vo.setBusinessType(DEConstant.BESINESS_TYPE_01);//还要通知一级经销商
				body.put("body", vo);
				de.sendAMsg("DRB32", vo.getEntityCode(), body);*/
			}
			logger.info("====经销商销售上报结果下发结束====" + vo.toString());
		} catch(Exception e) {
			logger.error("经销商销售上报结果下发失败", e);
			throw new RpcException(e);
		}
	}
}
