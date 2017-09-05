package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.Date;
import java.util.List;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrGamefeePO;

/**
 * 超过免费保养次数
 * <pre>
 * 前提：
 *    申请单类型为免费保养
 * 原规则(现已不使用)：
 *	 1.如果当前车辆类型为特殊质保车辆
 *	   判断当前车辆的所有索赔类型为“免费保养”的申请单状态为已经”审核同意”
 *     或者”已结算”的申请单的总数量(注意要加上本次)是否大于业务对象
 *     《车辆》中定义免费保养次数 
 *	 2.如果当前车辆不是特殊质保车辆
 *	   判断当前车辆的所有索赔类型为“免费保养”的申请单状态为已经”审核同意”
 *     或者”已结算”的申请单的总数量(注意要加上本次)是否大于1.
 * 以上规则有一为是则拒绝
 * 
 * [修改] 2010-07-20 XZM
 * 新规则：
 *   1、根据三包策略设定的保养次数检测对应索赔车辆是否超过免费保养次数
 *   2、同时检测对应保养费用是否同对应次数对应保养费用是否正确
 *   3、同次的保养是否已经索赔申请过，在非审核拒绝的索赔单中查询
 * </pre>
 * @author XZM
 */
public class RefuseAuditingRule03 extends AbstractAuditingRule {

	@Override
	protected String getRuleDesc() {
		return "超过免费保养次数";
	}

	/**
	 * 前提：
     *    申请单类型为免费保养
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		
        boolean result = false;
        TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
        
        if(Constant.CLA_TYPE_02.equals(claimPO.getClaimType())){//免费保养
        	result = true;
        }
		return result;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
        boolean result = false;
        
        this.backInfo = "超过免费保养次数";
        //索赔申请单信息
        TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
        //申请索赔次数
        Integer applyTimer = claimPO.getFreeMAmount();
        //申请索赔金额
        Double applyAmount = claimPO.getFreeMPrice();
        
        if(applyTimer==null || applyAmount==null)//索赔申请单信息错误
        	return true;
        
		//取得车辆信息的实效日期
		TmVehiclePO vehiclePO = orderVO.getVehiclePO();
		if(vehiclePO==null || vehiclePO.getPurchasedDate()==null)//车辆信息不存在直接跳过该审核（有车辆审核规则）
			return false;
		Date purchasedDate = vehiclePO.getPurchasedDate();
		//取得经销商所属省份
		TmDealerPO dealerPO = orderVO.getDealerPO();
		if(dealerPO==null || dealerPO.getProvinceId()==null)//经销商信息不存在则直接跳过该审核（有经销商审核规则）
			return false;
		Long provinceId = dealerPO.getProvinceId();
        
        ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
        //查询保养费用
        List<TtAsWrGamefeePO> gamefeeList = auditingDao.queryGuaranteeAmount(purchasedDate, provinceId,
        		vehiclePO.getModelId(),orderVO.getCompanyId());
        
        //检测对应保养费用是否维护过，且等于维护的费用
        boolean isExists = false;
        for (TtAsWrGamefeePO ttAsWrGamefeePO : gamefeeList) {
			if(applyTimer.equals(ttAsWrGamefeePO.getMaintainfeeOrder())){//对应保养次数设定过保养费用
				if(!applyAmount.equals(ttAsWrGamefeePO.getManintainFee())){//保养费用不相等
					result = true;
				}
				isExists = true;
				break;
			}
		}
        
        if(!isExists)//对应保养次数没有设定过保养费用
        	result = true;
        
        if(!result){//检测对应保养是否超过维护次数
        	String expClaimStatus = Constant.CLAIM_APPLY_ORD_TYPE_05 + "";
        	List<?> resList = auditingDao.queryGuaranteeRecord(claimPO.getVin(),
        			claimPO.getFreeMAmount(),orderVO.getCompanyId(),expClaimStatus);
        	if(resList!=null && resList.size()>1)//对应次数已经免费保养过
        		result = true;
        }
        
		return result;
	}

}
