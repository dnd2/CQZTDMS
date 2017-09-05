package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infoservice.po3.bean.PO;

/**
 * 索赔数量大于单车用量
 * <pre>
 * 前提：
 *     无
 * 规则：
 *     某一申请单中的索赔配件数量如果大于配件主数据定义的单车用量,则退回
 * </pre>
 * @author XZM
 */
public class UntreadAuditingRule12 extends AbstractAuditingRule {

	@Override
	protected String getRuleDesc() {
		return "索赔数量大于单车用量";
	}

	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		return true;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		this.backInfo = "索赔数量大于单车用量";
		
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		
		//查询该索赔申请单对应的配件
		List<PO> partList = orderVO.getPartsList();
		
		if(partList==null || partList.size()<1)//索赔申请单中不存在申请配件则 直接通过
			return result;
		
		StringBuilder sbuilder = new StringBuilder();
		for (int i = 0;i < partList.size(); i++) {
			TtAsWrPartsitemPO partPO = (TtAsWrPartsitemPO) partList.get(i);
			sbuilder.append("'").append(partPO.getPartCode()).append("',");
		}
		
		String parts = sbuilder.toString();
		if(parts.length()>0){
			parts = parts.substring(0, parts.length()-1);
		}
		//查询该索赔申请单中配件对应配件主数据中明细
		String vin = orderVO.getClaimPO().getVin();
		List<Map<String,Object>> basePartList = auditingDao.queryPartsDetailFromPartBase(vin, 
				parts,orderVO.getCompanyId());
		
		if(basePartList==null || basePartList.size()<=0)//没有找到主要数据，不做改规则判断
			return result;
		
		//检测对应索赔数量超过单车数量的
		for (PO po : partList) {
			TtAsWrPartsitemPO partPO = (TtAsWrPartsitemPO)po;
			for (Map<String, Object> map : basePartList) {
				String partCode = (String) map.get("PART_CODE");
				if(partPO.getPartCode().equals(partCode)){
					Integer carAmount = ((BigDecimal) map.get("CAR_AMOUNT")).intValue();
					if(partPO.getQuantity()>carAmount){
						result = true;
						break;
					}
				}
			}
			if(result)
				break;
		}
			
		
		return result;
	}

}
