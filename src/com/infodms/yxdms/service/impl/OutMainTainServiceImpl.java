package com.infodms.yxdms.service.impl;

import java.util.List;
import java.util.Map;

import com.infodms.yxdms.dao.OutMainTainDao;
import com.infodms.yxdms.service.OutMainTainService;
import com.infoservice.po3.bean.PageResult;

public class OutMainTainServiceImpl implements OutMainTainService{
	private OutMainTainDao dao=new OutMainTainDao();
	//private ThreeGuaRegisterDao threeGuaRegisterDao = ThreeGuaRegisterDaoImpl.getInstance();
	/**
	 * 打开外出维修查询
	 * @param paraMap
	 * @return
	 */
	public PageResult<Map<String, Object>> Maintainselect(Map<String, Object> paraMap, int curPage, int pageSize) {
		// TODO Auto-generated method stub
		PageResult<Map<String,Object>> map= dao.Maintainselect(paraMap, curPage, pageSize);
		return map;
	}

	/*public void saveMainTain(TtAsEgressPO po, TtAsCustomerServicePO po1, String newCusFlag) throws Exception {
		 boolean flag = threeGuaRegisterDao.isNewOldCus(po.getVin());
		 po.setClaimId(po1.getId());
		 if("1".equals(newCusFlag)){
			 if(po.getId() != null){
				 dao.updateTAEP(po);
			 }else{
				 po.setId(SequenceManager.getSequence());
				 dao.insertTAEP(po);
			 }
		 }else{
			 if(po.getId() != null){
				 dao.updateTAEP(po);
				 dao.updateTACSP(po1);
			 }else{
				 po.setId(SequenceManager.getSequence());
				 //如果客户表vin已存在，修改客户信息否则添加客户信息
				 if (flag == true) {
					  	dao.insertTAEP(po);
					  	dao.updateTACSP(po1);
					} else {
						//如果是老客户
						 Long ctmId = SequenceManager.getSequence();
						 po1.setId(ctmId);
						 po.setCusmerId(ctmId);
						 dao.insertTAEP(po);
						 dao.insertTACSP(po1);
					}
			 }
		 }
		 
	}*/

	/*public void Auditing(TtAsWrAuditingPO po,String status) throws Exception {
		if(StringUtils.isNotBlank(po.getAuditingReamrk())){
			dao.addData(po);
			dao.updateStatus(po.getClaimId(), status,po.getAuditingReamrk());
		}else{
			dao.updateStatus(po.getClaimId(), status);
		}
		
	}*/
public List<Map<String, Object>> getDealerInfos(String s) throws Exception {
	// TODO Auto-generated method stub
	return dao.getDealerInfos(s);
}
public List<Map<String, Object>> getListUserName(String s) throws Exception {
	// TODO Auto-generated method stub
	return dao.getListUserName(s);
}
public List<Map<String, Object>> specialRecord(String id) throws Exception {
	// TODO Auto-generated method stub
	return dao.specialRecord(id);
}
}
