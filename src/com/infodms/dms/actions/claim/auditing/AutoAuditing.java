package com.infodms.dms.actions.claim.auditing;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrAppauthitemPO;
import com.infodms.dms.po.TtAsWrClaimAutoPO;
import com.infoservice.po3.bean.PO;


/**
 * 自动审核规则
 * 包括：
 *     1、固定的审核规则（退还）
 *        （索赔自动审核规则管理功能 中 可以启动或停止规则）
 *     2、固定的审核规则（拒绝）
 *        （索赔自动审核规则管理功能 中 可以启动或停止规则）
 * @author XZM
 */
public class AutoAuditing {
	
	private static Logger logger = Logger.getLogger(AutoAuditing.class);
	
	/**
	 * 白名单规则检测,如果满足对应白名单则不检测其他规则
	 * @param claimPO 索赔申请单信息
	 * @return boolean 审核结果
	 *         true : 审核通过
	 *         false : 审核未通过
	 */
	public static boolean whiteRuleAuditing(ClaimOrderVO orderVO){ 
		
		boolean res = true;
		
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		//加载 自动通过规则  进行审核
		List<PO> whiteRuleL = auditingDao.queryFixAuthRule(Constant.STATUS_ENABLE.toString(),
												Constant.CLAIM_RULE_TYPE_04.toString(),
												orderVO.getCompanyId());
		res = applyRule(whiteRuleL,orderVO,Constant.CLAIM_APPLY_ORD_TYPE_04.toString());
		
		return res;
	}
	
	/**
	 * 根据设定规则审核索赔申请工单
	 * @param claimPO 索赔申请单信息
	 * @return boolean 审核结果
	 *         true : 审核通过
	 *         false : 审核未通过
	 */
	public static boolean auditing(ClaimOrderVO orderVO){ 
		
		boolean res = true;
		
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		//加载 退回规则 进行审核
		List<PO> untreadRuleL = auditingDao.queryFixAuthRule(Constant.STATUS_ENABLE.toString(),
												Constant.CLAIM_RULE_TYPE_02.toString(),
												orderVO.getCompanyId());
		res = applyRule(untreadRuleL,orderVO,Constant.CLAIM_APPLY_ORD_TYPE_06.toString());
		
		if(res){//该申请单没有被退回时进行以下验证
			//加载 拒绝规则 进行审核
			List<PO> refuseRuleL = auditingDao.queryFixAuthRule(Constant.STATUS_ENABLE.toString(),
												Constant.CLAIM_RULE_TYPE_01.toString(),
												orderVO.getCompanyId());
			res = applyRule(refuseRuleL,orderVO,Constant.CLAIM_APPLY_ORD_TYPE_05.toString());
		}else{//满足被退回规则,将维修工单上报状态修改为为上报（现在工单转索赔单后就不可以在修改了）
			/*if(orderVO.getClaimPO()!=null){
				DealerClaimReportDao reportDao = new DealerClaimReportDao();
				reportDao.modifyWorkOrderStatus(orderVO.getClaimPO().getRoNo(), Constant.IS_NOT_REPORT,-1L);
			}*/
		}
		
		return res;
	}
	
	/**
	 * 使用规则进行检查,满足对应拒绝或退回规则则修改对应工单状态，同时记录审核记录
	 * @param ruleList
	 * @return true : 不匹配对应条件 false : 匹配对应条件
	 */
	private static boolean applyRule(List<PO> ruleList,ClaimOrderVO orderVO,String status){
		
		boolean flag = true;
		if(ruleList==null || ruleList.size()<=0)
			return flag;
		
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		
		for (PO po : ruleList) {
			TtAsWrClaimAutoPO rulePO = (TtAsWrClaimAutoPO) po;
			String className = rulePO.getAction();
			if(className!=null && !"".equals(className)){
				try {
					Class<?> c = Class.forName(className);
					AbstractAuditingRule rule = (AbstractAuditingRule) c.newInstance();
					boolean res = rule.auditing(orderVO);
					if(res){//满足条件，按对应规则处理
						flag = false;//满足该拒绝或退回规则
						String claimId = orderVO.getClaimPO().getId().toString();
						
						//更新对应索赔申请单状态  （自动审核 将修改人 修改为-1）
						auditingDao.updateClaimOrderStatus(status, Constant.IS_AUTO_AUDITING, 
								"-1", claimId,"",rule.getBackInfo());
						
						//记录索赔申请单审核的授权记录
						TtAsWrAppauthitemPO appAuthPO = new TtAsWrAppauthitemPO();
						appAuthPO.setId(Long.parseLong(claimId));
						appAuthPO.setApprovalPerson("自动审核"+" [授权审核]");//自动审核 审核人员 固定为100
						appAuthPO.setApprovalLevelCode("100");//自动审核 授权角色 固定为100
						appAuthPO.setApprovalDate(new Date());
						appAuthPO.setApprovalResult(status);//授权结果=索赔申请单状态
						appAuthPO.setRemark(rule.getBackInfo());//备注=审核不通过理由
						appAuthPO.setCreateBy(new Long(-1));
						appAuthPO.setCreateDate(new Date());
						auditingDao.insertClaimAppAuth(appAuthPO);
					}
				} catch (Exception e) {
					logger.error(className + " 不是有效规则名称！" + e.getMessage());
				}
			}
			if(!flag)//存在一条规则被匹配则不再进行其他规则验证
				break;
		}
		return flag;
	}
}
