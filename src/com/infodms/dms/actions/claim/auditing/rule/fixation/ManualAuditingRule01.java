package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.List;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infoservice.po3.bean.PO;

/**
 * 索赔配件超过三包期（人工审核规则）
 * <pre>
 * 前提：
 *     无
 * 规则：
 * 规则：
 *    存在超过三包期的配件则直接转人工审核
 * </pre>
 * @author XZM
 */
public class ManualAuditingRule01 extends AbstractAuditingRule {
	
	@Override
	protected String getRuleDesc() {
		return "索赔配件超过三包期";
	}

	/**
	 * 索赔单类型为 "一般维修" 和 "外出维修"
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		
		boolean result = true;
		if(orderVO!=null && orderVO.getClaimPO()!=null && orderVO.getClaimPO().getClaimType()!=null){
			if(Constant.CLA_TYPE_01.equals(orderVO.getClaimPO().getClaimType())){//一般维修
				result = true;
			}else if(Constant.CLA_TYPE_09.equals(orderVO.getClaimPO().getClaimType())){//外出维修
				result = true;
			}else{
				result = false;
			}
		}
		return result;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		/*****add by liuxh 20101217 保养类型修改为不一样****/
		TtAsWrApplicationPO claimPo=orderVO.getClaimPO();
		Integer claimType=claimPo.getClaimType().intValue();
		if(claimType.intValue()==Constant.CLA_TYPE_02.intValue()){
			this.backInfo="未按规定做保养 ";
		}else{
			this.backInfo = "索赔配件超过三包期  ";
		}
		/*****add by liuxh 20101217 保养类型修改为不一样****/
		
		//ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		
		//索赔申请单信息
		//TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
		List<PO> partList = orderVO.getPartsList();
		if(partList==null || partList.size()==0)//如果对应索赔单不存在配件，默认有预授权 
			return false;
		
		for (PO po : partList) {//遍历配件
			TtAsWrPartsitemPO mainPO = (TtAsWrPartsitemPO) po;
			if(!Constant.IS_NO_OVER_GUARANTEES.equals(mainPO.getIsGua())){//超过三包期
				result = true;
				break;
			}
		}
		
		/*// 2010-10-09  三包规则按索赔单中的怕判断结果进行判断，不用再自己判断，故屏蔽自己判断方法
		//取得车辆信息的实效日期
		TmVehiclePO vehiclePO = orderVO.getVehiclePO();
		if(vehiclePO==null)//车辆信息不存在直接跳过该审核
			return false;
		
		Date purchasedDate = vehiclePO.getPurchasedDate();
		//取得经销商所属省份
		TmDealerPO dealerPO = orderVO.getDealerPO();
		if(dealerPO==null || dealerPO.getProvinceId()==null)//经销商信息不存在则直接跳过该审核
			return false;
		Long provinceId = dealerPO.getProvinceId();
		
		//查询该索赔单已经维护质保期的配件
		List<Map<String, Object>> partGPList = null;
		
		if(purchasedDate!=null)//实效日期不为空，先检测对应业务规则
			partGPList = auditingDao.queryQualityByPart(Constant.STATUS_ENABLE, 
					claimPO.getId(), vehiclePO.getModelId(), provinceId,
					purchasedDate,orderVO.getCompanyId());
		
		if(partGPList==null || partGPList.size()<1){//当 三包  业务规则 的质保信息未设定，查询通用规则（系统规则）
			partGPList = auditingDao.querySystemQualityByPart(Constant.STATUS_ENABLE, claimPO.getId(),orderVO.getCompanyId());
		}
		
		GuaranteePeriod gp = new GuaranteePeriod();
		//设定过质保期信息的配件代码
		Map<String,Map<String,Object>> partsGPMap = gp.changeToMap(partGPList,"PART_CODE");
		
		for (PO po : partList) {//遍历配件
			TtAsWrPartsitemPO mainPO = (TtAsWrPartsitemPO) po;
			if(gp.isOverGuaranteeLimit(claimPO, mainPO, partsGPMap)){//超过质保期
				result = true;
				this.backInfo = this.backInfo + " *" + mainPO.getPartName();
				break;
			}
		}
		*/
		return result;
	}
	
protected boolean ruleCheck2(ClaimOrderVO orderVO) {
		
		boolean result = true;
		/*****add by liuxh 20101217 保养类型修改为不一样****/
		TtAsWrApplicationPO claimPo=orderVO.getClaimPO();
		Integer claimType=claimPo.getClaimType().intValue();
		if(claimType.intValue()==Constant.CLA_TYPE_02.intValue()){
			this.backInfo="未按规定做保养 ";
		}else{
			this.backInfo = "索赔配件超过三包期  ";
		}
		/*****add by liuxh 20101217 保养类型修改为不一样****/
		
		//ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		
		//索赔申请单信息
		//TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
		List<PO> partList = orderVO.getPartsList();
		if(partList==null || partList.size()==0)//如果对应索赔单不存在配件，默认有预授权 
			return false;
		
		for (PO po : partList) {//遍历配件
			TtAsWrPartsitemPO mainPO = (TtAsWrPartsitemPO) po;
			if(!Constant.IS_NO_OVER_GUARANTEES.equals(mainPO.getIsGua())){//超过三包期
				result = false;
				break;
			}
		}
		
		/*// 2010-10-09  三包规则按索赔单中的怕判断结果进行判断，不用再自己判断，故屏蔽自己判断方法
		//取得车辆信息的实效日期
		TmVehiclePO vehiclePO = orderVO.getVehiclePO();
		if(vehiclePO==null)//车辆信息不存在直接跳过该审核
			return false;
		
		Date purchasedDate = vehiclePO.getPurchasedDate();
		//取得经销商所属省份
		TmDealerPO dealerPO = orderVO.getDealerPO();
		if(dealerPO==null || dealerPO.getProvinceId()==null)//经销商信息不存在则直接跳过该审核
			return false;
		Long provinceId = dealerPO.getProvinceId();
		
		//查询该索赔单已经维护质保期的配件
		List<Map<String, Object>> partGPList = null;
		
		if(purchasedDate!=null)//实效日期不为空，先检测对应业务规则
			partGPList = auditingDao.queryQualityByPart(Constant.STATUS_ENABLE, 
					claimPO.getId(), vehiclePO.getModelId(), provinceId,
					purchasedDate,orderVO.getCompanyId());
		
		if(partGPList==null || partGPList.size()<1){//当 三包  业务规则 的质保信息未设定，查询通用规则（系统规则）
			partGPList = auditingDao.querySystemQualityByPart(Constant.STATUS_ENABLE, claimPO.getId(),orderVO.getCompanyId());
		}
		
		GuaranteePeriod gp = new GuaranteePeriod();
		//设定过质保期信息的配件代码
		Map<String,Map<String,Object>> partsGPMap = gp.changeToMap(partGPList,"PART_CODE");
		
		for (PO po : partList) {//遍历配件
			TtAsWrPartsitemPO mainPO = (TtAsWrPartsitemPO) po;
			if(gp.isOverGuaranteeLimit(claimPO, mainPO, partsGPMap)){//超过质保期
				result = true;
				this.backInfo = this.backInfo + " *" + mainPO.getPartName();
				break;
			}
		}
		*/
		return result;
	}
	
}
