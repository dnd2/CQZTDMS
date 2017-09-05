package com.infodms.dms.actions.claim.auditing.rule;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;

/**
 * 自动审核规则接口
 * @author XZM
 */
public abstract class AbstractAuditingRule {
	
	private Logger logger = Logger.getLogger(AbstractAuditingRule.class);
	/** 前提条件检查(true :满足前提条件 ，false :不满足) */
	protected abstract boolean preConditionCheck(ClaimOrderVO orderVO);
	/** 
	 * 使用规则检查(true :满足该条件 ，false :不满足) 
	 * 注意：需通过 backInfo 属性返回审核相关信息
	 */
	protected abstract boolean ruleCheck(ClaimOrderVO orderVO);
	/** 规则信息描述 */
	protected abstract String getRuleDesc();
	/** 返回信息 */
	protected String backInfo = "";
	
	public String getBackInfo(){
		return backInfo;
	}
	
	/** 
	 * 使用该规则审核对应工单 
	 * @param claimPO 索赔申请单信息
	 * @return boolean true 满足条件 : false 不满足条件
	 */
	public boolean auditing(ClaimOrderVO orderVO){
		
		logger.info("[" + this.getRuleDesc() + "]审核开始>>>>");
		
		boolean res = false;
		
		try{
			//该规则应用前提条件检查
			if(this.preConditionCheck(orderVO)){//前提条件满足
				//使用具体规则检查
				res = this.ruleCheck(orderVO);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		logger.info("[" + this.getRuleDesc() + "]审核结束<<<<");
		
		return res;
	}
}
