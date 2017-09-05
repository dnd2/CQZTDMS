package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.TmClaimConfirmBillDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.TmClaimConfirmBillVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

public class OSB34 {
	
	private Logger logger = Logger.getLogger(OSB34.class);
	public TmClaimConfirmBillDao dao = TmClaimConfirmBillDao.getInstance();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	
	/**
	 * @param args
	 */
/*	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ContextUtil.loadConf();
		OSB34 o = new OSB34();
		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
		o.sendDate("BO20110512239775", "S06483", 0);
		POContext.endTxn(true);
	}*/
	
	public void sendDate(String balanceNo,String dealerCode,int flag){
		logger.info("====开票单信息下发开始====");
		DeUtility de = new DeUtility();
		try{
			Map<String, Object> map = deCommonDao.getDmsDlrCode(dealerCode);
			//不存在对应下端经销商
			if(map==null){
				logger.info("经销商"+dealerCode+"不存在对应下端经销商");
				return ;
			}
			TmClaimConfirmBillVO vo = new TmClaimConfirmBillVO();

			if(flag==1){//新增开票单
				List<TmClaimConfirmBillVO> list = dao.getTmClaimConfirmBill(balanceNo);
				if(list!=null&&list.size()>0){
					 vo = list.get(0);
				}
				vo.setIsValid(Constant.STATUS_ENABLE);
			}else{//删除开票单
				vo.setBillNo(balanceNo);
				vo.setIsValid(Constant.STATUS_DISABLE);
			}
			vo.setEntityCode(dealerCode);
			vo.setDownTimestamp(new Date());
			HashMap<String, Serializable> body = new HashMap<String, Serializable>();
			body.put("body", vo);
			de.sendAMsg("DRB34", map.get("DMS_CODE").toString(), body);
			logger.info("====开票单信息下发结束====, 下发了" + body.size() + "条记录");
		}catch(Exception e){
			logger.error("开票单信息下发失败", e);
			throw new RpcException(e);
		}
	}
}
