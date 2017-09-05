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
 * R06A:申请单类型填写有误（重复修理索赔）R06B: 申请单类型填写有误（一般索赔）
 * <pre>
 * 前提：
 *    无
 * 规则：
 *    1.同一车辆某一索赔主工时有过1份以上的已支付申请单，但索赔类型不为
 *    重复修理索赔。则返回R06A
 *    2. 同一车辆某一索赔主工时没有1份以上的已支付申请单，但索赔类型不为
 *    重复修理索赔。则返回R06B
 * [修改] 2010-07-26 XZM
 * 新规则：
 *    1、索赔单类型：一般索赔，如果所有主要工时有一个工时索赔次数大于1，
 *       那么则返回R06A
 *    2、索赔单类型：重复索赔，如果所有主要工时的索赔次数都小于等于1，
 *       那么则返回R06B
 * [新增] 2010-08-06 XZM
 * 新规则：
 *    1、索赔单类型：一般索赔，如果所有主要配件有一个工时索赔次数大于1，
 *       那么则返回R06A
 *    2、索赔单类型：重复索赔，如果所有主要配件的索赔次数都小于等于1，
 *       那么则返回R06B
 * 注意：如果工时和配件有一个需要填写索赔类型为"重复修理索赔"那么索赔单要求填写该类型，
 *       如果工时和配件都需要填写索赔类型为"一般索赔"那么索赔单要求填写"一般类型"。
 * </pre>
 * @author XZM
 *
 */
public class UntreadAuditingRule06 extends AbstractAuditingRule {

	private String R06A = "申请单类型填写有误（重复修理索赔）";
	private String R06B = "申请单类型填写有误（一般索赔）";
	
	@Override
	protected String getRuleDesc() {
		return "申请单类型填写有误（重复修理索赔）/申请单类型填写有误（一般索赔）";
	}

	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		return true;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {

        boolean result = false;
 
        TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
        
        //步骤1:检测对应工时是否存在重复索赔，根据检测结果判断索赔类型是否正确
        boolean labourRes = this.checkByLabour(orderVO);
        //步骤2:检测对应配件是否存在重复索赔，根据检测结果判断索赔类型是否正确
        boolean partRes = this.checkByPart(orderVO);
        
        if(labourRes || partRes){//当配件或工时存在一个满足该条件时
	    	if(Constant.CLA_TYPE_04.equals(claimPO.getClaimType())){//重复修理索赔
	    		if(!labourRes || !partRes){//其中某一检测该索赔单应该为重复索赔时
	    			this.backInfo = this.R06A;
	    		}else{
	    			this.backInfo = this.R06B;
	    			result = true;
	    		}
	        }else if(Constant.CLA_TYPE_01.equals(claimPO.getClaimType())){//一般索赔
	        	if(!labourRes && !partRes){//两个都为一般索赔
	        		this.backInfo = this.R06B;
	        	}else{
	        		this.backInfo = this.R06A;
	        		result = true;
	        	}
	        }
        }
        
		return result;
	}

	/**
	 * 检测对应工时是否存在重复修理情况，根据检测结果判断索赔类型是否正确
	 * @param orderVO
	 * @return 
	 */
	private boolean checkByLabour(ClaimOrderVO orderVO) {

        boolean result = false;
        TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
        
        ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
        
        List<PO> mainLabourList = orderVO.getMainLabourList();
        if(mainLabourList==null || mainLabourList.size()<1)//不存在工时则直接退回
        	return result;
        	
        //车辆信息
        String vin = claimPO.getVin();
        String exptClaimStatus = Constant.CLAIM_APPLY_ORD_TYPE_01 + "," + Constant.CLAIM_APPLY_ORD_TYPE_05;
        
        List<Map<String,Object>> labourStatusL = auditingDao.checkRepeatClaimManhour(vin, Constant.IS_MAIN_TROUBLE,
        		orderVO.getCompanyId(),exptClaimStatus,claimPO.getId());
        
        if(Constant.CLA_TYPE_04.equals(claimPO.getClaimType())){//重复修理索赔
        	//R06B: 申请单类型填写有误（一般索赔）默认满足该规则
        	result = true;
        	this.backInfo = this.R06B;
        	for(Map<String,Object> map: labourStatusL){
        		Integer size = ((BigDecimal) map.get("REPAIRCOUNT")).intValue();
	        	if(size>1){//如果存在某工时维修次数大于1次，则允许以重复修理索赔进行维修
	                result = false;    	
	                break;
	        	}
        	}
        }else if(Constant.CLA_TYPE_01.equals(claimPO.getClaimType())){//一般索赔
        	for(Map<String,Object> map: labourStatusL){
        		Integer size = ((BigDecimal) map.get("REPAIRCOUNT")).intValue();
	        	if(size>1){//R06A:申请单类型填写有误（重复修理索赔）,如果存在某个工时维修次数大于1，则应为重复修理索赔
	        		this.backInfo = this.R06A;
	        		result = true;
	        		break;
	        	}
        	}
        }
		return result;
	}
	
	/**
	 * 检测对应配件是否存在重复修理情况，根据检测结果判断索赔类型是否正确
	 * @param orderVO
	 * @return
	 */
	private boolean checkByPart(ClaimOrderVO orderVO) {

        boolean result = false;
        TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
        ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
        
        List<PO> mainPartList = orderVO.getMainPartList();
        if(mainPartList==null || mainPartList.size()<1)//不存在配件则直接退回
        	return result;
        
        //车辆信息
        String vin = claimPO.getVin();
        //排除掉"未上报"和"审核拒绝"状态索赔单
        String exptClaimStatus = Constant.CLAIM_APPLY_ORD_TYPE_01 + "," + Constant.CLAIM_APPLY_ORD_TYPE_05;
        
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
		
        //查询在该索赔单中主损配件的索赔次数
        List<Map<String,Object>> partStatusL = auditingDao.checkRepeatClaimPart(vin, Constant.IS_MAIN_TROUBLE,
        		orderVO.getCompanyId(),exptClaimStatus,claimPO.getId());
        
        //查询该索赔单已经维护质保期的配件
		List<Map<String, Object>> partGPList = null;
		
		if(purchasedDate!=null)//实效日期不为空，先检测对应业务规则
			partGPList = auditingDao.queryQualityByPart(Constant.STATUS_ENABLE, 
					claimPO.getId(), vehiclePO.getModelId(), provinceId,
					purchasedDate,orderVO.getCompanyId());
		
		if(partGPList==null || partGPList.size()<1){//当 三包  业务规则 的质保信息未设定，查询通用规则（系统规则）
			partGPList = auditingDao.querySystemQualityByPart(Constant.STATUS_ENABLE, claimPO.getId(),orderVO.getCompanyId());
		}
		
        if(partStatusL!=null && partGPList!=null && partGPList.size()>0){
        	
        	GuaranteePeriod gp = new GuaranteePeriod();
        	//设定过质保期信息的配件代码
    		Map<String,Map<String,Object>> partsGPMap = gp.changeToMap(partGPList,"PART_CODE");
    		
        	for (Map<String, Object> partMap : partStatusL) {
        		String partCode = (String) partMap.get("PART_CODE");
        		TtAsWrPartsitemPO mainPO = new TtAsWrPartsitemPO();
        		mainPO.setPartCode(partCode);
        		if(!gp.isOverGuaranteeLimit(claimPO, mainPO, partsGPMap)){//当配件未超过质保期时
	        		if(Constant.CLA_TYPE_04.equals(claimPO.getClaimType())){//重复修理索赔
	                	//R06B: 申请单类型填写有误（一般索赔）默认满足该规则
	                	result = true;
	                	this.backInfo = this.R06B;
	            		Integer size = ((BigDecimal) partMap.get("REPAIRCOUNT")).intValue();
	    	        	if(size>1){//如果存在某工时维修次数大于1次，则允许以重复修理索赔进行维修
	    	                result = false;    	
	    	                break;
	    	        	}
	                }else if(Constant.CLA_TYPE_01.equals(claimPO.getClaimType())){//一般索赔
	            		Integer size = ((BigDecimal) partMap.get("REPAIRCOUNT")).intValue();
	    	        	if(size>1){//R06A:申请单类型填写有误（重复修理索赔）,如果存在某个工时维修次数大于1，则应为重复修理索赔
	    	        		this.backInfo = this.R06A;
	    	        		result = true;
	    	        		break;
	    	        	}
	                }
        		}
			}
        }
        
		return result;
	}
}
