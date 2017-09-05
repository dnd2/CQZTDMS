package com.infodms.dms.actions.claim.auditing.rule.fixation;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TtAsWrApplicationPO;

/**
 * 主动申请人工规则审核（人工审核规则）
 * <pre>
 * 前提：
 *     无
 * 规则：
 * 规则：
 *    经销商主动申请需要人工审核，根据索赔单中人工审核
 * </pre>
 * @author XZM
 */
public class ManualAuditingRule03 extends AbstractAuditingRule {
	
	@Override
	protected String getRuleDesc() {
		return "主动申请人工规则审核";
	}

	/**
	 * 无
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		
		boolean result = true;
		return result;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		this.backInfo = "主动申请人工规则审核 ";
		
		if(orderVO!=null){
			TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
			if(claimPO!=null && claimPO.getIsAudit()!=null){
				if(Constant.IF_TYPE_YES.equals(claimPO.getIsAudit())){
					result = true;
				}
			}
		}
		
		return result;
	}
}
