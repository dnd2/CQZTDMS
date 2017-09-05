package com.infoservice.dms.chana.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.dao.repair.RepairOrderDao;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infoservice.dms.chana.dao.CommonDao;
import com.infoservice.dms.chana.po.TtAsRepairOrderTempPO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

public class SendRepairOrder extends AbstractSendTask {
	
	private final Logger LOG = Logger.getLogger(SendRepairOrder.class);
	private RepairOrderDao dao = RepairOrderDao.getInstance();
	private CommonDao commonDao = CommonDao.getInstance();
	@Override
	protected String handleExecute() throws Exception {
		LOG.info("工单维修记录发送开始");
		command();
		LOG.info("工单维修记录发送结束");
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void command() throws Exception {
		List<TtAsRepairOrderPO> pos = dao.queryUnSendRepair();
		for (TtAsRepairOrderPO po : pos) {
			TtAsRepairOrderTempPO tmp = new TtAsRepairOrderTempPO();
			BeanUtils.copyProperties(tmp, po);
			dao.insert(tmp);//插入接口表
			commonDao.updateComplete("TT_AS_REPAIR_ORDER", "RO_NO", tmp.getRoNo());//更新接口状态为已下发
		}
	}
	
	public static void main(String[] args) throws Exception {
		ContextUtil.loadConf();
		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);		
		SendRepairOrder pt = new SendRepairOrder();
		pt.handleExecute();
		POContext.endTxn(true);
	}
	
}
