package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.Calendar;
import java.util.Date;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TmDownParameterPO;
import com.infodms.dms.po.TtAsWrApplicationPO;

/**
 * 超过上报期限
 * <pre>
 * 前提:
 *     申请单类型不为: 重新提交
 * 规则：
 *     维修工单的结束日期和索赔单上报日期的时间差超过索赔上报期限
 * </pre>
 * @author XZM
 */
public class RefuseAuditingRule04 extends AbstractAuditingRule {

	private ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
	
	@Override
	protected String getRuleDesc() {
		return "超过上报期限";
	}

	/**
	 * 前提:
	 *     申请单类型不为: 重新提交
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		// TODO 现在申请单类型没有重新提交类型
		return true;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		this.backInfo = "超过上报期限";
		
		TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
		
		//查询索赔有效天数
		TmDownParameterPO downParameterPO = this.auditingDao.queryDownParameter(claimPO.getDealerId(), 
				Constant.CLAIM_BASIC_PARAMETER_04,orderVO.getCompanyId());
		
		String limitDays = downParameterPO.getParameterValue();
		if(limitDays!=null && !"".equals(limitDays)){
			
			Date claimDate = claimPO.getReportDate();//索赔申请单上报时间
			Date roEndDate = claimPO.getRoEnddate();//维修工单结束时间
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(roEndDate);
			calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(limitDays));
			//TODO 没有做到子夜处理
			Date tempDate = calendar.getTime();
			
			if(claimDate.after(tempDate)){
				result = true;
			}
		} else {//TODO 没有设定对应索赔有效天数    报告错误
			result = true;
		}
		
		return result;
	}
}
