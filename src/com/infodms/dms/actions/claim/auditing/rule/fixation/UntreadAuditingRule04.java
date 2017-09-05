package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrApplicationPO;

/**
 * R04A里程越跑越少(规则1) 或 R04B 里程数过大(规则2,3)
 * <pre>
 * 前提：
 *    无
 * 规则：
 * 1．判断（本次索赔申请单里程数）是否大于或等于
 *    （本次申请单开单日期以前最后一次提交的申请单中(不包含 
 *    “退回”）的里程数），如果是，则进行进一步审核；如果否，
 *    退回该申请单
 * 2.判断（本次索赔申请单里程数） 是否小于或等于（本次申请单
 *    开单日期以后最前一次提交的申请单中的里程数），若是，
 *    则进行进一步的审核；若否，退回该申请单， 
 * 3.判断本次索赔申请单里程数, 是否小于或等于<<车辆>>中的
 *   行驶里程, 若是，则进行进一步的审核；若否，退回该申请单，
 * </pre> 
 * @author XZM
 */
public class UntreadAuditingRule04 extends AbstractAuditingRule {
	
	private String R04A = "里程越跑越少";
	private String R04B = "里程数过大";


	@Override
	protected String getRuleDesc() {
		return "R04A里程越跑越少(规则1) 或 R04B 里程数过大(规则2,3)";
	}

	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		return true;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
		
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		String vin = claimPO.getVin();
		//查询车辆信息
		TmVehiclePO vehiclePO = orderVO.getVehiclePO();
		
		if(vehiclePO==null)//没有车辆信息自动通过
			return result;
		
		Double currentMile = claimPO.getInMileage();//该申请单中里程数
		if(vehiclePO.getMileage()!=null && currentMile>vehiclePO.getMileage()){//车辆里程过大
			this.backInfo = this.R04B;
			result = true;
			return result;
		}
		
		//查询该索赔申请单之前和之后的索赔申请单是否存在
		//2010-06-23 UPDATE XZM 将比较时间修改为上报时间 原 创建时间
		Date claimStartDate = claimPO.getReportDate();
		String status = Constant.CLAIM_APPLY_ORD_TYPE_05 + "," + Constant.CLAIM_APPLY_ORD_TYPE_06;
		List<Map<String,Object>> resList = auditingDao.checkClaimMileage(claimStartDate,
				vin, status,orderVO.getCompanyId());
		if(resList==null || resList.size()<1)//不存在多条状态不是"拒绝"和"退还"的索赔申请单
			return result;
		
		Double beforeMile = null;//该申请单之前提交的索赔申请单最大里程数
		Double afterMile = null;//该申请单之前提交的索赔申请单最小里程数
		
		for (Map<String, Object> map : resList) {
			String type = (String) map.get("TYPE");
			if("BEFORE".equals(type)){
				BigDecimal bm = (BigDecimal) map.get("IN_MILEAGE");
				if(bm!=null)
					beforeMile = bm.doubleValue();
			} else if("AFTER".equals(type)){
				BigDecimal am = (BigDecimal) map.get("IN_MILEAGE");
				if(am!=null)
					afterMile = am.doubleValue();
			}	
		}

		if(beforeMile!=null && currentMile<beforeMile){//车辆越跑里程越少
			this.backInfo = this.R04A;
			result = true;
		}else if(afterMile!=null && currentMile>afterMile){//车辆里程过大
			this.backInfo = this.R04B;
			result = true;
		}
		
		return result;
	}

}
