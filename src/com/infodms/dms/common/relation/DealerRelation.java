package com.infodms.dms.common.relation;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.relation.DealerRelationDAO;
import com.infodms.dms.util.CommonUtils;

public class DealerRelation {
	public Logger logger = Logger.getLogger(DealerRelation.class);
	DealerRelationDAO dao = DealerRelationDAO.getInstance() ;
	
	/**
	 * 经销商与职位之间的关系查询
	 * @param companyId 对应经销商公司id
	 * @param poseId 对应经销商职位id
	 * @return List
	 */
	public List<Map<String, Object>> getDealerPoseRelation(Long companyId,Long poseId) {
		return dao.getDealerPoseRelation(companyId, poseId) ;
	}
	
	/**
	 * 根据经销商职位查询对应经销商id
	 * @param companyId 对应经销商公司id
	 * @param poseId 对应经销商职位id
	 * @return String
	 */
	public String getDealerIdByPose(Long companyId,Long poseId) {
		StringBuffer dealerIds = new StringBuffer("") ;
		List<Map<String, Object>> dealerList = this.getDealerPoseRelation(companyId, poseId) ;
		
		if(!CommonUtils.isNullList(dealerList)) {
			int len = dealerList.size() ;
			
			for(int i=0; i<len; i++) {
				if(dealerIds.length() == 0) {
					dealerIds.append(dealerList.get(i).get("DEALER_ID").toString()) ;
				} else {
					dealerIds.append(",").append(dealerList.get(i).get("DEALER_ID").toString()) ;
				}
			}
		}
		
		return dealerIds.toString() ;
	}
	
	public String getDealerIdsByCompany(String companyId) {
		StringBuffer dealerIds = new StringBuffer("") ;
		List<Map<String, Object>> dealerList = dao.getDlrByCompany(companyId) ;
		
		if(!CommonUtils.isNullList(dealerList)) {
			int len = dealerList.size() ;
			
			for(int i=0; i<len; i++) {
				if(dealerIds.length() == 0) {
					dealerIds.append(dealerList.get(i).get("DEALER_ID").toString()) ;
				} else {
					dealerIds.append(",").append(dealerList.get(i).get("DEALER_ID").toString()) ;
				}
			}
		}
		
		return dealerIds.toString() ;
	}
}
