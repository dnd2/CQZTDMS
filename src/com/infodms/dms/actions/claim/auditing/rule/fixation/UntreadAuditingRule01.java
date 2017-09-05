package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infoservice.po3.bean.PO;

/**
 * 索赔工时与故障代码不一致
 * <pre>
 * 前提：
 *   当前索赔类型 不为”免费保养”
 * 规则：
 *   系统检查业务对象《索赔工时与故障代码关系》中是否定义了当前
 * 申请单中某个工时代码与故障代码的关系，如果定义了关系，则检查
 * 当前申请单的故障代码是否在定义的索赔工时与故障代码关系的范围
 * 内，如果不在则退回
 * 如果 索赔类型 为 “免费保养”则不检查此规则
 * 
 * [修改] 2010-07-20 XZM
 *    一张索赔单主要工时现在修改为多个，原为一个，调整该规则
 * </pre>
 * @author XZM
 */
public class UntreadAuditingRule01 extends AbstractAuditingRule {

	@Override
	protected String getRuleDesc() {
		return "索赔工时与故障代码不一致";
	}

	/**
	 * 前提：
     *   当前索赔类型 不为”免费保养”
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
		this.backInfo = "索赔工时与故障代码不一致";
		TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		List<PO> mainLabourList = orderVO.getMainLabourList();
		
		if(mainLabourList==null || mainLabourList.size()<1)//没有主要工时 退出该规则审核
			return result;
		
		for(PO po : mainLabourList){//遍历所有主要工时
			
			//查询主要工时
			String mainLabour = ((TtAsWrLabouritemPO)po).getWrLabourcode();
			//需要判断对应车型组是否存在
			String modelId = orderVO.getWrGroupId();
			if(modelId==null || "".equals(modelId))//没有车型组推出该规则审核
				return result;
			//查询主要工时同故障代码关系信息
			Map<String,Map<String,String>> resultMap = auditingDao.queryRelationOfManhourAndTrouble(
					modelId,claimPO.getId().toString(),orderVO.getCompanyId());
			//该索赔工单中索赔工时对应故障代码关系
			Map<String,String> relationMap = resultMap.get("RELATION");
			//维护过故障代码的工时集合
			Map<String,String> manhourMap = resultMap.get("MANHOUR");
			String main = mainLabour;
			String trouble = ((TtAsWrLabouritemPO)po).getTroubleCode();
	
			if(manhourMap!=null && manhourMap.size()>0 && manhourMap.containsKey(main)){//存在设定过故障代码的索赔工时，同时维护过对应工时的故障代码
				if(!relationMap.containsKey(main+"_#$#$_"+trouble)){//存在某个主要工时没有设定该故障代码
					result = true;
					break;
				}
			}else{//没有设定故障代码关系的则直接通过，不满足该审核条件
				result = false;
			}
		}
		return result;
	}

}
