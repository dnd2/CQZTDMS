package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.po.TmVhclMaterialPO;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.DeMaterialDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.MaterialVO;
/**
 * @Title: DeMaterial.java
 *
 * @Description:物料下发接口
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

public class OSA12 {
	private static Logger logger = Logger.getLogger(OSA12.class);
	public DeMaterialDao dao = DeMaterialDao.getInstance();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	//计划每次发送数
	private final int SEND_COUNT = 1000;
	
	
	public String execute(TmVhclMaterialPO po) {
		try {
			DeUtility de = new DeUtility();
			logger.info("====物料下发开始==== MaterialCode == " + po.getMaterialCode());
			List<MaterialVO> list = dao.getDeMaterial(po);
			if (null == list || list.size() == 0) {
				logger.warn("物料下发,没有找到物料信息 === " + po.getMaterialId());
				return null;
			}
			HashMap<String, Serializable> body = DEUtil.assembleBody(list);
			de.sendAllMsg("DRA12", body);
			logger.info("====物料下发结束====");
		} catch(Exception e) {
			logger.error("物料下发失败 " + po.getMaterialCode(), e);
			//throw new RpcException(e);
		}
		return null;
	}
	
	public List<String> sendData(List<String> dealerCodes) {
		logger.info("====物料下发开始==== " + dealerCodes.size());
		DeUtility de = new DeUtility();
		try{
			List<MaterialVO> list = dao.getDeMaterial();
			//HashMap<String, Serializable> body = DEUtil.assembleBody(list);
			List<String> dmsCodes = new ArrayList<String>();
			List<String> errCodes = new ArrayList<String>();
			for (String dealerCode : dealerCodes) {
				try {
					Map<String, Object> dmsDealer = deCommonDao.getDmsDealerCode(dealerCode);
					//可下发的经销商列表
					dmsCodes.add(dmsDealer.get("DMS_CODE").toString());
				} catch (Exception e) {
					logger.error("Cann't send to " + dealerCode, e);
					errCodes.add(dealerCode);
				}
			}
			//可下发的经销商列表
			/*if (dmsCodes.size() > 0) {
				de.sendMsg("DRA12", dmsCodes, body);
				logger.info("====物料下发结束====,下发了(" + body.size() + ")条数据");
			}*/
			
			if (dmsCodes.size() > 0) {
				while (list.size() > 0) {
					List<MaterialVO> tmps = new ArrayList<MaterialVO>();
					int everySize = list.size() >= SEND_COUNT ? SEND_COUNT : list.size();//每次实际发送的条数
					for (int i = 0; i < everySize; i++) {
						MaterialVO tmp = list.remove(0);
						tmps.add(tmp);
					}
					HashMap<String, Serializable> body = DEUtil.assembleBody(tmps);
					de.sendMsg("DRA12", dmsCodes, body);
					logger.info("====物料下发结束====,下发了(" + body.size() + ")条数据");				}	
			}
			return errCodes;
		} catch(Exception e){
			logger.error("物料下发失败", e);
			throw new RpcException(e);
		}
	}

}
