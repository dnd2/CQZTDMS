package com.infoservice.dms.chana.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsRoAddItemPO;
import com.infodms.dms.po.TtAsRoLabourPO;
import com.infodms.dms.po.TtAsRoManagePO;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infoservice.de.DEException;
import com.infoservice.de.DEMessage;
import com.infoservice.de.convertor.f2.XmlConvertor4YiQiP01;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.DeRepairOrderResultDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.QueryRepairVO;
import com.infoservice.dms.chana.vo.RepairOrderResultVO;
import com.infoservice.dms.chana.vo.RepairOrderVO;
import com.infoservice.dms.chana.vo.RoAddItemVO;
import com.infoservice.dms.chana.vo.RoLabourVO;
import com.infoservice.dms.chana.vo.RoManageVO;
import com.infoservice.dms.chana.vo.RoRepairPartVO;
import com.infoservice.dms.chana.vo.VehicleVO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

public class OSC94 extends AbstractReceiveAction {

	private DeRepairOrderResultDao dao = DeRepairOrderResultDao.getInstance();
	private ClaimBillMaintainDAO claimBillMaintainDAO = ClaimBillMaintainDAO.getInstance();
	private DeCommonDao commonDao = DeCommonDao.getInstance();
	private static final Logger LOG = Logger.getLogger(OSC94.class);
	
	@Override
	protected DEMessage handleExecutor(DEMessage msg) {
		LOG.info("====同步查询工单信息开始====");
		long begin = System.currentTimeMillis();
		try{
			Map<String, Serializable> bodys = msg.getBody();
			for (Entry<String, Serializable> entry : bodys.entrySet()) {
				LOG.info("====事务开启====");
				POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
				QueryRepairVO vo = new QueryRepairVO();
				vo = (QueryRepairVO) entry.getValue();
				//vo.setEntityCode(commonDao.getDcsDealerCode(msg.getSource()).get("DEALER_CODE").toString());
				List<RepairOrderResultVO> list = queryRepairOrder(vo, msg.getSource());
				if (null == list || list.size() == 0) {
					return wrapperMsg(new RepairOrderResultVO(), "没有工单表里找到" + vo.getVin());
				}
				DEMessage rmsg = wrapperMsg(list, null);
				LOG.info("====事务结束====");
				POContext.endTxn(true);
				long end = System.currentTimeMillis();
				LOG.info("====同步查询工单信息结束====");
				LOG.info("任务耗时:"+(end-begin));
				return rmsg;
			}
		} catch(Exception e) {
			LOG.error("同步查询工单信息失败", e);
			throw new RpcException(e);
		}
		return null;
	}
	
/*	public static void main(String[] args) throws Exception {
		ContextUtil.loadConf();
		OSC94 o = new OSC94();
		try {
			 File file = new File("D:/20110930112825937000001601.dat");
			 InputStream is = new FileInputStream(file);
			 byte[] b = new byte[is.available()];
			 is.read(b, 0, b.length);
			 XmlConvertor4YiQiP01 xml = new XmlConvertor4YiQiP01();
			 DEMessage msg = xml.convert(b);
			 is.close();
			 o.handleExecutor(msg);
		 	 }catch (Exception e) {
		        e.printStackTrace();
		     }
	}*/
	
	private List<RepairOrderResultVO> queryRepairOrder(QueryRepairVO vo, String source) throws Exception {
		List<TtAsRepairOrderPO> pos = dao.queryRepairOrder(vo);
		List<RepairOrderResultVO> vos = new ArrayList<RepairOrderResultVO>();
		
		for (TtAsRepairOrderPO po : pos) {
			RepairOrderResultVO orderVO = new RepairOrderResultVO();
			orderVO = assembleVO(orderVO, po);
			orderVO.setEntityCode(source);
			//维修配件
			List<TtAsRoRepairPartPO> parts = claimBillMaintainDAO.queryRepairPart(null, String.valueOf(po.getId()));
			RoRepairPartVO roRepairPartVO = new RoRepairPartVO();
			List<BaseVO> partv = assembleVO(roRepairPartVO, parts);
			orderVO.setRepairPartVoList(DEUtil.transType(partv));
			
			//维修工时
			List<TtAsRoLabourPO> labours = claimBillMaintainDAO.queryRepairitem(null, String.valueOf(po.getId()));
			RoLabourVO roLabourVO = new RoLabourVO();
			List<BaseVO> labourv = assembleVO(roLabourVO, labours);
			orderVO.setLabourVoList(DEUtil.transType(labourv));
			
			//附加项目
			List<TtAsRoAddItemPO> items = claimBillMaintainDAO.queryAddItem(null, String.valueOf(po.getId()));
			RoAddItemVO roAddItemVO = new RoAddItemVO();
			List<BaseVO> itemv = assembleVO(roAddItemVO, items);
			orderVO.setAddItemVoList(DEUtil.transType(itemv));
			
			//维修工单辅料
			List<TtAsRoManagePO> manages = claimBillMaintainDAO.queryManage(po.getId());
			RoManageVO roManageVO = new RoManageVO();
			List<BaseVO> managev = assembleVO(roManageVO, manages);
			orderVO.setManageVoList(DEUtil.transType(managev));
			
			vos.add(orderVO);
		}
		return vos;
	}
	
	private <T extends PO, K extends BaseVO> K assembleVO(K vo, T po) throws Exception {
		BeanUtils.copyProperties(vo, po);
		return vo;
	}
	
	private <T extends PO, K extends BaseVO> List<BaseVO> assembleVO(BaseVO vo, List<T> pos) throws Exception {
		List<BaseVO> vos = new ArrayList<BaseVO>();
		for (T po : pos) {
			vo = assembleVO(vo, po);
			vos.add(vo);
		}
		return vos;
	}
	
	private DEMessage wrapperMsg(List<RepairOrderResultVO> vos, String msg) {
		HashMap<String, Serializable> body = DEUtil.assembleBody(vos);
		DeUtility de = new DeUtility();
		DEMessage rmsg = null;
		try {
			rmsg = de.assembleDEMessage("DRC94", body);
		} catch (DEException e) {
			LOG.error(e, e);
		}
		return rmsg;
	}
	
	private DEMessage wrapperMsg(RepairOrderResultVO vo, String msg) {
		if (null != msg) {
			//出错的时候
			vo = DeUtility.wrapperMsg(vo, msg);
		}
		HashMap<String, Serializable> body = new HashMap<String, Serializable>();
		body.put("body", vo);
		DeUtility de = new DeUtility();
		DEMessage rmsg = null;
		try {
			rmsg = de.assembleDEMessage("DRC94", body);
		} catch (DEException e) {
			LOG.error(e, e);
		}
		return rmsg;
	}
	
}
