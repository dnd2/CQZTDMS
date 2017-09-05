package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.List;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infoservice.po3.bean.PO;

/**
 * 车型与工时代码不一致
 * <pre>
 * 前提：
 *     当前索赔类型 不为”免费保养”
 * 规则：
 *     检查当前申请单中车辆的车型与索赔工时是否在业务对象
 * 《索赔工时》中定义的范围内，如果不在则退回
 * 如果 索赔类型 为 “免费保养”则不检查此规则
 * 注：
 *    现在只检测主要工时
 * [修改] 2010-07-20 XZM
 *    一张索赔单主要工时现在修改为多个，原为一个，调整该规则
 * </pre>
 * @author XZM
 */
public class UntreadAuditingRule02 extends AbstractAuditingRule {

	@Override
	protected String getRuleDesc() {
		return "车型与工时代码不一致";
	}

	/**
	 * 前提：
	 *     当前索赔类型 不为”免费保养”
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
		
		if(!Constant.CLA_TYPE_02.equals(claimPO.getClaimType())){//不为免费保养
			result = true;
		}
		return result;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		this.backInfo = "车型与工时代码不一致";
		
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		//查询主要工时(一张索赔申请单中存在多个主要工时)
		List<PO> mainLabourList = orderVO.getMainLabourList();
		
		if(mainLabourList==null || mainLabourList.size()<1)//没有主要工时时自动通过
			return result;
		
		String modelId = orderVO.getWrGroupId();
		if(modelId==null || "".equals(modelId)){//车型组不存在 则直接满足规则
			return true;
		}
		//检测对应索赔单中主要工时是否在工时主信息表中存在
		List<?> labourList = auditingDao.queryLabInfo(modelId,
				orderVO.getClaimPO().getId(),Constant.IS_MAIN_TROUBLE,orderVO.getCompanyId());
		
		if(labourList==null || mainLabourList.size()>labourList.size()){//存在某项主工时不在工时主信息表中
			result = true;
		}
		
		return result;
	}
}
