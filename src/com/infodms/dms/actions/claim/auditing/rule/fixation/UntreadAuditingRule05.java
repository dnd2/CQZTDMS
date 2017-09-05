package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.List;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrApplicationPO;

/**
 * 重复递交
 * <pre>
 * 前提:
 *	   申请单类型不为:重复提交
 * 规则：
 *      索赔单的”索赔申请单号ASC”维修站代码、工单号、行号和系统中
 *      已支付或已拒绝的索赔单(不包含 “退回”）完全重复
 * [修改] 2010-07-22 除了 已结算、已拒绝，新增审核通过状态
 * </pre>
 * @author XZM
 *
 */
public class UntreadAuditingRule05 extends AbstractAuditingRule {

	@Override
	protected String getRuleDesc() {
		return "重复递交";
	}

	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		//TODO 现在已经没有   重复提交类型
		return true;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		this.backInfo = "重复递交";
		
		TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
		
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		
		TtAsWrApplicationPO conditionPO = new TtAsWrApplicationPO();
		conditionPO.setRoNo(claimPO.getRoNo());
		conditionPO.setLineNo(claimPO.getLineNo());
		conditionPO.setDealerId(claimPO.getDealerId());
		
		//现在只统计 "已拒绝" 和 "已结算" 状态的申请单
		String status = Constant.CLAIM_APPLY_ORD_TYPE_05+ "," + Constant.CLAIM_APPLY_ORD_TYPE_07 + "," + Constant.CLAIM_APPLY_ORD_TYPE_04;
		
		List<TtAsWrApplicationPO> resList = auditingDao.queryRepeatClaim(conditionPO,status,orderVO.getCompanyId());
		
		if(resList!=null && resList.size()>0){
			for (TtAsWrApplicationPO po : resList) {
				Long id = po.getId(); 
				if(!claimPO.getId().equals(id)){
					result = true;
					break;
				}
			}
		}
		
		return result;
	}

}
