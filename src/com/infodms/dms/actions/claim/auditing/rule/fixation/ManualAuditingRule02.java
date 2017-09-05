package com.infodms.dms.actions.claim.auditing.rule.fixation;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TmDealerPO;

/**
 * "售前维修"类型的索赔单都需要大区审核
 * <pre>
 * 前提：
 *     无
 * 规则：
 * "售前维修"类型的索赔单都需要大区审核
 * </pre>
 * @author XZM
 */
public class ManualAuditingRule02 extends AbstractAuditingRule {
	
	@Override
	protected String getRuleDesc() {
		return "售前维修 类型的索赔单都需要大区审核(DQV除外)";
	}

	/**
	 * 索赔单类型为 售前维修
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		
		boolean result = true;
		if(orderVO!=null && orderVO.getClaimPO()!=null && orderVO.getClaimPO().getClaimType()!=null){
			if(Constant.CLA_TYPE_07.equals(orderVO.getClaimPO().getClaimType())){//售前维修
				result = true;
			}else{
				result = false;
			}
		}
		return result;
	}

	/**
	 * 不需要检查
	 */
	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = true;
		this.backInfo = "售前维修 类型的索赔单都需要大区审核(DQV除外)";
		return result;
	}
}
