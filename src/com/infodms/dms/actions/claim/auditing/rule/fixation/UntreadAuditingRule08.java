package com.infodms.dms.actions.claim.auditing.rule.fixation;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TmDealerPO;

/**
 * 维修站代码填写有误
 * <pre>
 * 前提：
 *    无
 * 规则：
 *    索赔单中的维修站代码不合法，或代码不在当前状态为已营业的维修站列表中
 * </pre>
 * @author XZM
 */
public class UntreadAuditingRule08 extends AbstractAuditingRule {

	@Override
	protected String getRuleDesc() {
		return "维修站代码填写有误";
	}

	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		return true;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		this.backInfo = "维修站代码填写有误";
		
		TmDealerPO dealerPO = orderVO.getDealerPO();
		//验证该经销商状态是否有效
		if(dealerPO==null || !Constant.STATUS_ENABLE.equals(dealerPO.getStatus())){
			result = true;
		}
		
		return result;
	}

}
