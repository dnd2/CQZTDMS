package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.customerRelationships.ComplaintAuditDao;
import com.infodms.dms.dao.customerRelationships.ComplaintDisposalDao;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityVehiclePO;
import com.infodms.dms.po.TtCrComplaintsAuditPO;
import com.infodms.dms.po.TtCrComplaintsPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.de.DEMessage;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.CommonDao;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.vo.ActivityResultVO;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.CustomerComplaintForDCSVO;
import com.infoservice.dms.chana.vo.CustomerComplaintDetailVO;
import com.infoservice.dms.chana.vo.CustomerComplaintVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;

/**
 * 
* @ClassName: ORC32 
* @Description: TODO(接受下端上报的投诉信息) 
* @author liuqiang 
* @date Jul 30, 2010 2:34:31 PM 
*
 */
public class ORC32 extends AbstractReceiveAction {
	private static final Logger LOG = Logger.getLogger(ORC32.class);
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	private CommonDao commonDao = CommonDao.getInstance();
	private ComplaintDisposalDao complaintDisposalDao = ComplaintDisposalDao.getInstance();
	private ComplaintAuditDao cadao = ComplaintAuditDao.getInstance();
	@Override	
	protected DEMessage handleExecutor(DEMessage msg) {
		LOG.info("====投诉信息上报接收开始====");
		try {
			POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);	
			Map<String, Serializable> bodys = msg.getBody();
			for (Entry<String, Serializable> entry : bodys.entrySet()) {
				CustomerComplaintForDCSVO vo = new CustomerComplaintForDCSVO();
				vo = (CustomerComplaintForDCSVO) entry.getValue();
				updateCustomerComplaint(vo);
			}
			POContext.endTxn(true);
			LOG.info("====投诉信息上报接收结束====");
			return null;
		} catch (Exception e) {
			POContext.endTxn(false);
			LOG.error("投诉信息上报接收失败", e);
			throw new RpcException(e.getMessage());
		}finally{
			 POContext.cleanTxn();
		}	
	}
	
	public void updateCustomerComplaint(CustomerComplaintForDCSVO vo) throws Exception {
		TtCrComplaintsPO oldPo = new TtCrComplaintsPO();
		oldPo.setCompCode(vo.getComplaintNo());
		List<TtCrComplaintsPO> pos = complaintDisposalDao.select(oldPo);
		if (pos == null || pos.size() == 0) {
			throw new IllegalArgumentException("Can't find TtCrComplaintsPO by complaint code == " + vo.getComplaintNo());
		}
		oldPo.setCompId(pos.get(0).getCompId());

		//Map<String, Object> map = deCommonDao.getDcsDealerCode(vo.getEntityCode());
		//String dealerCode = map.get("DEALER_CODE").toString();
		//String dealerId = map.get("DEALER_ID").toString();
		TtCrComplaintsPO newPo = new TtCrComplaintsPO();
		newPo.setUpdateDate(new Date());
		newPo.setStatus(Constant.COMP_STATUS_TYPE_04);
		// 更新主表TT_CR_COMPLAINTS的状态
		complaintDisposalDao.update(oldPo, newPo);
		
		//获取子表信息
		LinkedList<CustomerComplaintDetailVO> vos = (LinkedList<CustomerComplaintDetailVO>) vo.getDealVoList();
		cadao.delByComId(oldPo.getCompId());
		for (CustomerComplaintDetailVO subVo : vos) {
			TtCrComplaintsAuditPO auditPO = new TtCrComplaintsAuditPO();
			auditPO.setId(Long.valueOf(SequenceManager.getSequence("")));
			auditPO.setAuditDate(subVo.getDealDate());
			auditPO.setAssignContent(subVo.getDealResult());
			auditPO.setAuditContent(subVo.getDealResult());
			auditPO.setAuditStatus(Constant.AUDIT_STATUS_TYPE_04);
			auditPO.setCompId(pos.get(0).getCompId());
			auditPO.setIfStatus(DEConstant.IF_STATUS_1);
			Map<String, Object> map = deCommonDao.getDcsDealerCode(vo.getEntityCode());
			Long dealerId = Long.parseLong(String.valueOf(map.get("DEALER_ID")));
			auditPO.setDealerId(dealerId);
			auditPO.setCreateBy(dealerId);
			auditPO.setOrgId(deCommonDao.getOrgIdByDealerId(dealerId));
			auditPO.setCreateDate(new Date());
			cadao.insert(auditPO);
		}
		
	}
	
}
