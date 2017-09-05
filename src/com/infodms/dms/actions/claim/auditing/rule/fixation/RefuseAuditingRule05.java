package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.List;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsWrApplicationPO;

/**
 * 超过服务活动保养次数
 * <pre>
 * 前提:
 *     申请单类型为: 服务活动
 * 规则：
 *     如果对应服务活动的类型为"免费保养",当对应车参与多次服务活动则拒绝该索赔单
 *     注：不统计的索赔单状态("未上报","审核退回","审核拒绝")
 * </pre>
 * @author XZM
 */
public class RefuseAuditingRule05 extends AbstractAuditingRule {

	private ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
	
	@Override
	protected String getRuleDesc() {
		return "超过服务活动保养次数";
	}

	/**
	 * 前提:
	 *     申请单类型为: 服务活动
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		
		boolean result = true;
		if(orderVO!=null && orderVO.getClaimPO()!=null){
			TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
			if(Constant.CLA_TYPE_06.equals(claimPO.getClaimType())){
				result = true;
			}else{
				result = false;
			}
		}else{
			result = false;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		this.backInfo = "超过服务活动保养次数";
		
		if(orderVO!=null && orderVO.getClaimPO()!=null){
			TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
			//1、查询服务活动
			String activityNO = claimPO.getCampaignCode();
			String vin = claimPO.getVin();
			if(activityNO!=null && !"".equals(activityNO)){
			//2、检测对应该服务活动是否索赔申请多次
				//21、查询服务活动信息
				TtAsActivityPO condition = new TtAsActivityPO();
				condition.setActivityCode(activityNO);
				List<TtAsActivityPO> activityList = auditingDao.select(condition);
			    if(activityList!=null && activityList.size()>0){
			    	TtAsActivityPO activityPO = activityList.get(0);
			    	if(activityPO!=null){
			    		//22、检测对应服务活动类型是否为免费保养
			    		if(Constant.SERVICEACTIVITY_KIND_02.equals(activityPO.getActivityKind())){
			    			//23、检测是否索赔同一辆车索赔多次
			    			if(vin!=null && !"".equals(vin)){
			    				//不统计的索赔单状态("未上报","审核退回","审核拒绝")
			    				String expStatus = ""+Constant.CLAIM_APPLY_ORD_TYPE_01 + "," +
			    				+Constant.CLAIM_APPLY_ORD_TYPE_05+ ","+Constant.CLAIM_APPLY_ORD_TYPE_06;
			    				List<?> checkList = auditingDao.checkModeActivity(vin, activityNO, expStatus);
			    				if(checkList!=null && checkList.size()>1){
			    					result = true;
			    				}
			    			}
			    		}
			    	}
			    }
			}
		}
		
		return result;
	}
}
