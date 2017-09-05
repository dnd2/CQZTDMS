package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TmVehiclePO;
import com.infoservice.po3.bean.PO;

/**
 * 超过三月未回运索赔申请配件
 * <pre>
 * 前提：
 *     无
 * 规则：
 *     存在三个月未回运的索赔申请单配件(已经结算的)，则直接退回该申请单
 * </pre>
 * @author XZM
 */
public class UntreadAuditingRule16 extends AbstractAuditingRule {
	/** 回运时间限制(月数,默认3个月) */
	public static Integer months = new Integer(3);//（月）

	@Override
	protected String getRuleDesc() {
		return "超过三月未回运索赔申请配件";
	}

	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		return true;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		this.backInfo = "超过三月未回应索赔申请配件";
		
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		
		Long dealerId = orderVO.getClaimPO().getDealerId();
		Integer claimStatus = Constant.CLAIM_APPLY_ORD_TYPE_07;//结付支算
		TmVehiclePO vehiclePO = orderVO.getVehiclePO();
		if(vehiclePO==null)//如果车辆信息不存在，则直接通过
			return false;
		
		List<PO> partList = orderVO.getPartsList();
		
		if(partList!=null && partList.size()>0){//只检查存在索赔配件的申请单
			List<Map<String,Object>> resList = auditingDao.queryNoReturnClaim(dealerId, 
					claimStatus, -months,vehiclePO.getModelId(),orderVO.getCompanyId());
			
			if(resList!=null && resList.size()>0)
				result = true;
		}
		
		return result;
	}

}
