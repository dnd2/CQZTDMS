package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.actions.claim.auditing.rule.fixation.common.GuaranteePeriod;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infoservice.po3.bean.PO;

/**
 * 索赔质保期未定义
 * <pre>
 * 前提：
 *    1.申请单类型为一般索赔;
 *    2.索赔车辆不为特殊质保车辆
 *    3.索赔配件没有预授权。
 * 规则：
 *    索赔配件的质保期是否在业务对象《配件质保期定义》定义的范围内
 *	如果当前配件的质保期未定义,则退回
 * </pre>
 * @author XZM
 */
public class UntreadAuditingRule13 extends AbstractAuditingRule {

	@Override
	protected String getRuleDesc() {
		return "索赔质保期未定义";
	}

	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
		
		//PreAuthorization pa = new PreAuthorization();
		if(Constant.CLA_TYPE_01.equals(claimPO.getClaimType())){//索赔类型为一般索赔
			result = true;
		}
		/*//现在去掉特殊质保测标识别检测
        if(result && Constant.IS_SPECIALCASE_FALSE.equals(claimPO.getSpecialFlag())) {//不是特殊质保车
			result = true;
		}else{
			result = false;
		}//现在主损配件为多个配件，不能将该条件放到前置条件中
        if(result && pa.partHasAuth(orderVO)){//不包含索赔预授权
			result = true;
		}else{
			result = false;
		}*/
		return result;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		this.backInfo = "索赔质保期未定义";
		
		//索赔申请单信息
		TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
		//查询该申请单需要授权的配件
		String roNo = claimPO.getRoNo();//索赔申请单 工单号
		String vin = claimPO.getVin();//车辆代码
		
		List<PO> mainPartList = orderVO.getMainPartList();
		if(mainPartList==null || mainPartList.size()==0)//如果对应索赔单不存在配件，默认有预授权 
			return false;
		
		//取得车辆信息的实效日期
		TmVehiclePO vehiclePO = orderVO.getVehiclePO();
		if(vehiclePO==null)//车辆信息不存在直接跳过该审核（有车辆审核规则）
			return false;
		Date purchasedDate = vehiclePO.getPurchasedDate();
		//取得经销商所属省份
		TmDealerPO dealerPO = orderVO.getDealerPO();
		if(dealerPO==null || dealerPO.getProvinceId()==null)//经销商信息不存在则直接跳过该审核（有经销商审核规则）
			return false;
		Long provinceId = dealerPO.getProvinceId();
		
		//查询该申请单已经授权的配件
		List<Map<String,Object>> authList = 
			auditingDao.queryAuthInfoByClaimId(roNo, 
					Constant.PRE_AUTH_ITEM_02.toString(), 
					Constant.PRECLAIM_AUDIT_01.toString(),
					vin,orderVO.getCompanyId());
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
		//保存申请过预售权的配件信息
		Map<String,Map<String,Object>> mainPartMap = gp.changeToMap(authList, "ITEM_CODE");
		
		for (PO po : mainPartList) {//遍历主要配件
			TtAsWrPartsitemPO mainPO = (TtAsWrPartsitemPO) po;
			if(mainPartMap==null || !mainPartMap.containsKey(mainPO.getPartCode())){//主要配件不存在预售权
				if(partsGPMap!=null && partsGPMap.containsKey(mainPO.getPartCode())){//设定过质保期
					Map<String,Object> partGPMap = partsGPMap.get(mainPO.getPartCode());
					BigDecimal gurnMile = (BigDecimal) partGPMap.get("GURN_MILE");//免费里程
					BigDecimal gurnDate = (BigDecimal) partGPMap.get("GURN_MONTH");//免费保修月数
					
					if(gurnMile==null || gurnDate==null){//没有设定质保里程和保险月数
						result = true;
					}
				}else{
					result = true;
				}
			}
			if(result)
				break;
		}
		
		return result;
	}

}
