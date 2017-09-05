package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.List;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrApplicationPO;

/**
 * 服务活动未评估
 * <pre>
 * 前提：
 *     索赔类型为服务活动
 * 规则：
 *     1、服务活动未评估
 *     2、服务活动未发布或者已经删除
 *     满足以上条件，则退回该索赔申请单
 * </pre>
 * @author XZM
 */
public class UntreadAuditingRule17 extends AbstractAuditingRule {
	
	@Override
	protected String getRuleDesc() {
		return "服务活动未评估";
	}

	/**
	 * 索赔类型：服务活动
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		boolean result = false;
		if(Constant.CLA_TYPE_06.equals(orderVO.getClaimPO().getClaimType())){//索赔类型为一般索赔
			result = true;
		}
		return result;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		this.backInfo = "服务活动未评估";
		
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
		
		//判断对应服务活动是否已经发布，且已经评估
		List<?> evaluateList = auditingDao.queryActivityEvaluate(claimPO.getCampaignCode(),
				orderVO.getCompanyId(),
				claimPO.getDealerId(),
				Constant.SERVICEACTIVITY_STATUS_02);
		
		if(evaluateList==null || evaluateList.size()<1){//该服务活动未评估或还未发布
			result = true;
		}
		
		return result;
	}

}
