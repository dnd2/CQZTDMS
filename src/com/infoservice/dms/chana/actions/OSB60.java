package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.DeVehicleOutDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.AllotOutVO;
import com.infoservice.dms.chana.vo.OutVehicleVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

public class OSB60 {
	private Logger logger = Logger.getLogger(OSB60.class);
	public DeVehicleOutDao dao = DeVehicleOutDao.getInstance();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	private int flag;
	/**
	 * @param args
	 */
/*	public static void main(String[] args) {
		ContextUtil.loadConf();
		OSB60 o = new OSB60();
		List<String> vins = new ArrayList();
		vins.add("LS5A2ABEXBA512629");
		vins.add("LS5A2ABE8AA553419");
		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
		o.sendDate(vins, "test", 2010093000861304L, 2010093000863835L);
		POContext.endTxn(true);
	}
	*/
	public int sendDate(List<String> vins,String remak,Long inDealerId,Long outDealerId){
		logger.info("====车辆出库信息下发开始====");
		DeUtility de = new DeUtility();
		try{
			List<OutVehicleVO> vos = new ArrayList<OutVehicleVO>();
			for(String vin:vins){
				OutVehicleVO vvo = new OutVehicleVO();
				vvo.setVin(vin);
				vos.add(vvo);
			}
			Map<String, Object> inDealer = dao.getDealer(inDealerId);
			//Map<String, Object> outDealer = dao.getDealer(outDealerId);
			AllotOutVO vo = new AllotOutVO();
			vo.setConsigneeCode((String)inDealer.get("DEALER_CODE"));
			vo.setConsigneeName((String)inDealer.get("DEALER_NAME"));
			vo.setVins(DEUtil.transType(vos));
			vo.setDownTimestamp(new Date());
			vo.setIsValid(Constant.STATUS_ENABLE);
			Map<String, Object> mapOut = deCommonDao.getDmsDlrCodeNew(outDealerId);
			//不存在对应下端经销商
			if(mapOut==null){
				this.flag = 0;
				return flag;
			}
			Map<String, Object> mapIn = deCommonDao.getDmsDlrCodeNew(inDealerId);
			if(mapIn!=null){
				if(mapIn.get("DMS_CODE").equals(mapOut.get("DMS_CODE"))){
					this.flag = -1;
					return flag;
				}
			}
			HashMap<String, Serializable> body = new HashMap<String, Serializable>();
			body.put("body", vo);
			de.sendAMsg("DRB60", mapOut.get("DMS_CODE").toString(), body);
			logger.info("====车辆出库信息下发结束====, 下发了" + body.size() + "条记录");
		}catch(Exception e) {
			logger.error("车辆出库信息下发失败", e);
		}
		this.flag = 1;
		return flag;
	}
	
	public Map<String, Object> getDmsDlrCode(Long inDealerId){
		StringBuffer sql= new StringBuffer();
		sql.append("select tdr.dms_code\n");
		sql.append("  from tt_same_company    tsc,\n");  
		sql.append("       tm_company         tmc,\n");  
		sql.append("       tm_dealer          tmd,\n");  
		sql.append("       ti_dealer_relation tdr\n");  
		sql.append(" where tmd.company_id = tmc.company_id\n");  
		sql.append("   and tmc.company_code = tsc.low_company_code\n");  
		sql.append("   and tsc.first_company_code = tdr.dcs_code\n");  
		sql.append("   and tmd.dealer_id = "+inDealerId+"\n");
	return dao.pageQueryMap(sql.toString(),null, dao.getFunName());
}
	
}
