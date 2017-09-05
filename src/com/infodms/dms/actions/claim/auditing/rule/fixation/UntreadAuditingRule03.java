package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infoservice.po3.bean.PO;

/**
 * 车型与配件不一致
 * <pre>
 * 前提：
 *     无
 * 规则：
 *     检查当前申请单中车辆的车系(车型组)与配件是否在业务对象《配件主数据》中定义的配件适用车系列表的范围内
 * 如果不在，则退回
 * </pre>
 * @author XZM
 */
public class UntreadAuditingRule03 extends AbstractAuditingRule {

	@Override
	protected String getRuleDesc() {
		return "车型与配件不一致";
	}

	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		return true;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		this.backInfo = "车型与配件不一致";
		
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		//查询索赔申请单对应配件信息
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
		//查询索赔申请单中
		String vin = orderVO.getClaimPO().getVin();
		Map<String,String> existPartMap = auditingDao.queryPartsFromPartBase(vin, parts,orderVO.getCompanyId());
		
		//去除配件中重复配件后再比较
		Map<String,String> claimPart = new HashMap<String, String>();
		for (PO po: partList) {
			TtAsWrPartsitemPO partPO = (TtAsWrPartsitemPO) po;
			claimPart.put(partPO.getPartCode(), partPO.getPartCode());
		}
		
		if(claimPart.size()>existPartMap.size()){//存在 在配件主信息表中 没有的配件代码
			result = true;
		}
		
		return result;
	}

}
