package com.infodms.dms.actions.claim.auditing.rule.fixation;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.actions.claim.auditing.rule.fixation.common.PreAuthorization;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TtAsWrApplicationPO;

/**
 * 未授权的索赔
 * <pre>
 * 前提：
 *    申请单类型为PDI索赔 或者 保外索赔
 * 规则：
 *    判断申请单中工时是否有预授权
 *    判断申请单中配件是否有预授权
 *    判断申请单的其它项目是否有预授权
 *    以上规则有一为否则拒绝
 * 注：现在只监控对应需要预授权的配件、工时和其他项目，如果都不再监控范围内
 *     不与检查。
 * </pre>
 * @author XZM
 */
public class RefuseAuditingRule02 extends AbstractAuditingRule {

	@Override
	protected String getRuleDesc() {
		return "未授权的索赔";
	}

	/**
	 * 前提：
	 * 申请单类型为PDI索赔 或者 保外索赔
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
		
		if(Constant.CLA_TYPE_07.equals(claimPO.getClaimType())){//PDI索赔
			result = true;
		}else if(Constant.CLA_TYPE_08.equals(claimPO.getClaimType())){//保外索赔
			result = true;
		}
		return result;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		this.backInfo = "未授权的索赔";
		
		PreAuthorization pa = new PreAuthorization();
		result = pa.checkPart(orderVO, false);
		result = result && pa.checkManHour(orderVO, false);
		result = result && pa.checkOhter(orderVO);
		
		result = !result;
		
		return result;
	}

}
