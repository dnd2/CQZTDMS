package com.infodms.dms.actions.claim.auditing.rule.fixation;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;

/**
 * R09A:车辆类型填写有误;R09B:车辆类型填写有误;R09C:车辆销售日期填写有误,正确日期为:YYYY-MM-DD
 * <pre>
 * 前提：
 *    无
 * 规则：
 * 1.检查索赔单中中的车辆类型是否在主机厂定义的车型列表中.
 *   如果为否,退回
 * 2.检查当前<<申请单>>中的车辆的车型与业务对象<<车辆>>中的 车型的
 *   车型代码是否一致,如果 不一致 则退回 
 * 3.检查当前《索赔额申请单》中的销售日期与业务对象<<车辆>>中销售日期
 *   是否一致(日期格式为YYYY-MM-DD)，如果不一致，则退回，提示:RO9C 
 *   车辆销售日期填写有误:YYYY-MM-DD(正确的销售日期).
 * </pre>
 * @author XZM
 */
public class UntreadAuditingRule09 extends AbstractAuditingRule {

	@Override
	protected String getRuleDesc() {
		return "车辆类型填写有误/车辆类型填写有误/车辆销售日期填写有误,正确日期为:YYYY-MM-DD";
	}

	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		return true;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		// TODO 该规则暂时没有 使用地方
		//原因：车型信息都是在系统内部填写，根据VIN带出来的
		return false;
	}

}
