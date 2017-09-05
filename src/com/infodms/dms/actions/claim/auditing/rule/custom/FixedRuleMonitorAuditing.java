package com.infodms.dms.actions.claim.auditing.rule.custom;

import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrClaimAutoPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

public class FixedRuleMonitorAuditing {
	private Logger logger = Logger.getLogger(FixedRuleMonitorAuditing.class);
	
	public String getRuleDesc() {
		return "固定规则人工审核";
	}

	/**
	 * 根据用户自定义的授权规则，对索赔单进行审核，提取其中需要通过的审核级别
	 * @param orderVO
	 * @return
	 */
	public AuditingVO deal(ClaimOrderVO orderVO) {
		
		AuditingVO resultVO = new AuditingVO();
		
		try{
			ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
			
			//加载 需要人工 进行审核规则
			List<PO> manualRuleL = auditingDao.queryFixAuthRule(Constant.STATUS_ENABLE.toString(),
													Constant.CLAIM_RULE_TYPE_03.toString(),
													orderVO.getCompanyId());
			
			resultVO = this.applyRule(manualRuleL, orderVO);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return resultVO;
	}
	
	/**
	 * 使用规则进行检查,满足对应拒绝或退回规则则修改对应工单状态，同时记录审核记录
	 * @param ruleList
	 * @return true : 不匹配对应条件 false : 匹配对应条件
	 */
	private AuditingVO applyRule(List<PO> ruleList,ClaimOrderVO orderVO){
		
		AuditingVO auditingVO = new AuditingVO();
		if(ruleList==null || ruleList.size()<=0)
			return auditingVO;
		
		StringBuilder rolesBuilder = new StringBuilder();
		StringBuilder reasonBuilder = new StringBuilder();
		
		for (PO po : ruleList) {
			TtAsWrClaimAutoPO rulePO = (TtAsWrClaimAutoPO) po;
			String className = rulePO.getAction();
			if(className!=null && !"".equals(className)){
				try {
					Class<?> c = Class.forName(className);
					AbstractAuditingRule rule = (AbstractAuditingRule) c.newInstance();
					boolean res = rule.auditing(orderVO);
					if(res){//满足条件，按对应规则处理
						rolesBuilder.append(CommonUtils.checkNull(rulePO.getAuthCode()));
						reasonBuilder.append(CommonUtils.checkNull(rule.getBackInfo()));
					}
				} catch (Exception e) {
					logger.error(className + " 不是有效规则名称！" + e.getMessage());
				}
			}
		}
		
		auditingVO.setReasions(reasonBuilder.toString());
		auditingVO.setRoles(rolesBuilder.toString());
		
		return auditingVO;
	}
}
