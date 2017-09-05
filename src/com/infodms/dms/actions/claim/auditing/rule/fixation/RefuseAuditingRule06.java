package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.List;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrApplicationPO;

/**
 * 超过免费保养次数
 * <pre>
 * 前提:
 *     申请单类型为: 免费保养
 * 规则：
 *     如果同一辆车超过1次，则退回该索赔单.
 *     注：不统计的索赔单状态("未上报","审核退回","审核拒绝")
 * </pre>
 * @author XZM
 */
public class RefuseAuditingRule06 extends AbstractAuditingRule {

	private ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
	
	@Override
	protected String getRuleDesc() {
		return "超过免费保养次数";
	}

	/**
	 * 前提:
	 *     申请单类型为: 免费保养
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		
		boolean result = true;
		if(orderVO!=null && orderVO.getClaimPO()!=null){
			TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
			if(Constant.CLA_TYPE_02.equals(claimPO.getClaimType())){
				result = true;
			}else{
				result = false;
			}
		}else{
			result = false;
		}
		return result;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		this.backInfo = "超过免费保养次数";
		
		if(orderVO!=null && orderVO.getClaimPO()!=null){
			TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
			//1、查询服务活动
			String vin = claimPO.getVin();
			//2、检测对应该车辆是否索赔申请多次免费保养
			//23、检测是否索赔同一辆车索赔多次免费保养
			if(vin!=null && !"".equals(vin)){
				//不统计的索赔单状态("未上报","审核退回","审核拒绝")
				String expStatus = ""+Constant.CLAIM_APPLY_ORD_TYPE_01 + "," +
				+Constant.CLAIM_APPLY_ORD_TYPE_05+ ","+Constant.CLAIM_APPLY_ORD_TYPE_06;
				
				List<?> checkList = auditingDao.checkModeMaintain(vin, expStatus);
				if(checkList!=null && checkList.size()>1){
					result = true;
				}
			}
		}
		
		return result;
	}
}
