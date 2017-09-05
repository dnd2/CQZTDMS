package com.infodms.dms.actions.claim.auditing.rule.custom;

import java.util.Iterator;
import java.util.Map;

import com.infodms.dms.po.TtAsWrRuleitemPO;
import com.infodms.dms.po.TtAsWrRulemappingPO;

/**
 * 授权规则维护 中 维护的规则明细
 * @author XZM
 */
public class RuleVO {
	
	/** 规则信息*/
	private TtAsWrRulemappingPO ruleVO;
	/** 规则对应的条件明细 按条件的顺序号存储(Map需要带排序) */
	private Map<Integer, TtAsWrRuleitemPO> conditiongMap;

	public TtAsWrRulemappingPO getRuleVO() {
		return ruleVO;
	}

	public void setRuleVO(TtAsWrRulemappingPO ruleVO) {
		this.ruleVO = ruleVO;
	}

	public Map<Integer, TtAsWrRuleitemPO> getConditiongMap() {
		return conditiongMap;
	}

	public void setConditiongMap(Map<Integer, TtAsWrRuleitemPO> map) {
		this.conditiongMap = map;
	}
	
	/**
	 * 取得对应规则明细
	 * @param itemMap 授权项信息
	 * @param compareOperMap 授权规则运算符
	 * @return
	 */
	public String getRuleReason(Map<String,String> itemMap,
			Map<String,String> compareOperMap){
		
		if(conditiongMap==null || conditiongMap.size()<=0)
			return "";
		
		StringBuilder sbuilder = new StringBuilder();
		
		Iterator<Integer> iter = conditiongMap.keySet().iterator();
		while(iter.hasNext()){
			Integer key = iter.next();
			TtAsWrRuleitemPO tempPO = conditiongMap.get(key);
			
			if(itemMap.containsKey(tempPO.getElementNo().toString())){
				String booleanComparison = tempPO.getBooleanComparison();
				if(booleanComparison==null) 
					booleanComparison = "";
				
				String comparisonOp = tempPO.getComparisonOp();
				if(compareOperMap.containsKey(comparisonOp))
					comparisonOp = compareOperMap.get(comparisonOp);
				
				String element = tempPO.getElementNo().toString();
				if(itemMap.containsKey(element))
					element = itemMap.get(element);
				
				sbuilder.append(booleanComparison).append(" ")//AND | OR
						.append(element).append(" ")//授权项
						.append(comparisonOp).append(" ")//比较符
						.append(tempPO.getElementValue());//目标值
			}
		}
		
        return sbuilder.toString()+"\n";
	}
}
