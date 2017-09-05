package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.List;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infoservice.po3.bean.PO;

/**
 * 经销商DQV规则（自动通过规则）
 * <pre>
 * 前提：
 *     索赔单类型不为"免费保养"
 * 规则：
 * 规则：
 *    如果：经销商存在DQV认证，且对应索赔单中的工时费DQV监控工时，那么直接通过技术审核
 * </pre>
 * @author XZM
 */
public class ForwardAuditingRule01 extends AbstractAuditingRule {
	
	@Override
	protected String getRuleDesc() {
		return "经销商DQV规则";
	}

	/**
	 * 索赔单类型不为"免费保养",且经销商未主动申请人工审核
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		
		boolean result = true;
		TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
		if(claimPO!=null && claimPO.getClaimType()!=null){
			if(Constant.CLA_TYPE_02.equals(claimPO.getClaimType())){//该规则不检测免费保养
				result = false;
			}else if(Constant.IF_TYPE_YES.equals(claimPO.getIsAudit())){//不检测主动申请人工审核的索赔单
				result = false;
			}
		}
		return result;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		this.backInfo = "经销商DQV规则 ";
		
		if(orderVO!=null){
			TmDealerPO dealerPO = orderVO.getDealerPO();
			TtAsWrApplicationPO appPo =orderVO.getClaimPO();
			/*********************************************
			* 0、检测是否过三包期
			* 注：三包期检测可以参考对应三包期检测的的规则，逐个配件检测是否过三包期，存在一过保，就为过保。
			* if(超过三包期){//不检测对应是否为DQV
			*      return false;
			* }
			*********************************************/
			
			
		
			//1、检测经销商是否为DQV
			if(dealerPO!=null && Constant.IF_TYPE_YES.equals(dealerPO.getIsDqv())){//经过DQV认证
				if(dealerPO!=null && Constant.CLA_TYPE_07.equals(appPo.getClaimType())){
					ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
					String claimId = "";
					//2、检测对应索赔单中的工时，是否为DQV监控工时
					if(orderVO!=null && orderVO.getClaimPO()!=null)
						claimId=orderVO.getClaimPO().getId().toString();
					List<?> checkList = auditingDao.existsDqvMonitorLabour(claimId);
					if(checkList==null || checkList.size()<1){
						result = true;
					}else{
						result = false;
					}
				}
				else{
					ManualAuditingRule01 manual = new ManualAuditingRule01();
					result = manual.ruleCheck2(orderVO);
					if(result)
					{
					
						ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
						String claimId = "";
						//2、检测对应索赔单中的工时，是否为DQV监控工时
						if(orderVO!=null && orderVO.getClaimPO()!=null)
							claimId=orderVO.getClaimPO().getId().toString();
						List<?> checkList = auditingDao.existsDqvMonitorLabour(claimId);
						if(checkList==null || checkList.size()<1){
							result = true;
						}else{
							result = false;
						}
					}
				}
			}
		}
		
		return result;
	}
}

