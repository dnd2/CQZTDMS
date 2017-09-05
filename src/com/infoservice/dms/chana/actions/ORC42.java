package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.infodms.dms.po.TtPtDlrstockPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.de.DEMessage;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.DePartStockDao;
import com.infoservice.dms.chana.vo.PartStockVO;
import com.infoservice.dms.chana.vo.RepairOrderVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;

/**
 * @Title: DePartStock.java
 *
 * @Description:
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
public class ORC42 extends AbstractReceiveAction {
	private static Logger logger = Logger.getLogger(ORC42.class);
	public static DePartStockDao dao = DePartStockDao.getInstance();
	@Override
	protected DEMessage handleExecutor(DEMessage msg) {
		logger.info("====配件库存信息上报接收开始====");
			Map<String, Serializable> bodys = msg.getBody();// 获得来至FRM2的消息数据
			for (Entry<String, Serializable> entry : bodys.entrySet()) {
				PartStockVO vo = new PartStockVO();
				vo = (PartStockVO) entry.getValue();
				try {
				 POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);	
					untiePartStockVO(vo);
				 POContext.endTxn(true);	
				} catch (Exception e) {
					POContext.endTxn(false);
					logger.error(e, e);
				}finally{
					 POContext.cleanTxn();
				}	
			}
			logger.info("====配件库存信息上报接收结束====");
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void untiePartStockVO(PartStockVO vo) throws Exception { 	   
		TtPtDlrstockPO po = new TtPtDlrstockPO();
		DeCommonDao deDao = new DeCommonDao();
		Map<String, Object> delMap = deDao.getDcsDealerCode(vo.getEntityCode());//取经销商ID
		po.setDealerId(Long.parseLong(String.valueOf(delMap.get("DEALER_ID"))));
		//po.setDealerId(Long.parseLong("2010010100072267"));
		po.setPartId(Long.parseLong(dao.getPartId(vo)));//查询配件ID
		//po.setPartId(Long.parseLong("2010060300000006"));
		String paper = String.valueOf(vo.getPaperQuantity());//截取flaot小数点前
		String actua = String.valueOf(vo.getActualQuantity());
		String pap = paper.substring(0, paper.indexOf("."));
		String act = actua.substring(0, actua.indexOf("."));
		po.setPaperQuantity(Integer.parseInt(pap));
		po.setActualQuantity(Integer.parseInt(act));
		int count = dao.getCount(po);//查看经销商是否有此配件的库存
		if(count==0){//无则添加
			po.setStockId(Long.parseLong(SequenceManager.getSequence("")));
			po.setCreateDate(new Date());
			dao.insert(po);
		}else{//有则更新
			TtPtDlrstockPO pv = new TtPtDlrstockPO();
			pv.setDealerId(po.getDealerId());
			pv.setPartId(po.getPartId());
			po.setUpdateDate(new Date());
			dao.update(pv, po);
		}
	}
}
