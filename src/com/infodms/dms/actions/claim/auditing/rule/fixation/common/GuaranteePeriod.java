package com.infodms.dms.actions.claim.auditing.rule.fixation.common;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrGamePO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infoservice.po3.bean.PO;

/**
 * 质保期检查
 * [修改] 2010-07-20 XZM
 *       变更：1、一个索赔单对应的主要工时和配件现在为多个，原来为一个
 *             2、现在保养费用的设定使用三包规则，原来的索赔配件质保期维护和保养费用维护功能删除
 * @author XZM
 */
public class GuaranteePeriod {
	
	private ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
	
	/**
	 * 检测对应主损配件是否存在某主损配件过了质保期（主损配件为多个）
	 * @param claimPO
	 * @return boolean true:超过质保期
	 */
	@Deprecated
	public boolean checkOnePart(ClaimOrderVO orderVO){
		
		boolean result = false;
		
		//主损配件列表
		List<PO> mainPartList = orderVO.getMainPartList();
		if(mainPartList==null || mainPartList.size()==0)//索赔单没有配件，则直接通过
			return result;
		//索赔申请单信息
		TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
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
		
		//查询该索赔单已经维护质保期的配件
		List<Map<String, Object>> partGPList = this.auditingDao.queryQualityByPart(Constant.STATUS_ENABLE, 
				claimPO.getId(), vehiclePO.getModelId(), provinceId, purchasedDate,orderVO.getCompanyId());
		
		if(partGPList==null || partGPList.size()<1){//当 三包  业务规则 的质保信息未设定，查询通用规则（系统规则）
			partGPList = this.auditingDao.querySystemQualityByPart(Constant.STATUS_ENABLE, claimPO.getId(),orderVO.getCompanyId());
		}
		
		if(partGPList!=null && partGPList.size()>0){//对应配件维护过质保期
			
			Map<String,Map<String,Object>> partsGPMap = this.changeToMap(partGPList,"PART_CODE");
			
			for (PO po : mainPartList) {//遍历主要配件，检查质保期
				TtAsWrPartsitemPO partPO = (TtAsWrPartsitemPO)po;
				if(partsGPMap.containsKey(partPO.getPartCode())){//设定过质保期
					Map<String,Object> partGPMap = partsGPMap.get(partPO.getPartCode());
					BigDecimal gurnMile = (BigDecimal) partGPMap.get("GURN_MILE");//免费里程
					BigDecimal gurnDate = (BigDecimal) partGPMap.get("GURN_MONTH");//免费保修月数
					
					if(gurnMile==null || gurnDate==null){//没有设定质保里程和保险月数
						result = true;
						break;
					}
						
					Double gpMileage = gurnMile.doubleValue();
					if((claimPO.getStartMileage()+gpMileage)<claimPO.getInMileage()){//验证里程
						result = true;
						break;
					}else{//验证质保时间
						
						Integer gpDate = gurnDate.intValue();//免费保修月数
						Date startDate = claimPO.getGuaranteeDate(); //购车日期
						
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(claimPO.getRoStartdate());
						calendar.add(Calendar.MONTH, -gpDate.intValue());
						Date tempDate = calendar.getTime();//工单开始日期-保修月数
						
						if(!tempDate.before(startDate)){//(工单开始日期-保修月数)是否在 购车日期 开始之后
							result = true;
							break;
						}
					}
				}else{//没有设定质保里程和保险月数则跳过质保检测(存在对应规则检测)
					return false;
				}
			}
		}

		return result;
		
	}
	
	/**
	 * 将List<Map<String, Object>> 转换成 Map<String,Map<String,Object>> ，以原List 
	 * 中 Map<String, Object>中对应key的值作为新key,而原Map<String, Object>作为值
	 * @param targetList 需要转换的List
	 * @param key 原Map<String, Object> 对应的键值
	 * @return Map<String,Map<String,Object>>
	 */
	public Map<String,Map<String,Object>> changeToMap(List<Map<String, Object>> targetList,String key){
		
		Map<String,Map<String,Object>> resMap = new HashMap<String, Map<String,Object>>();
		if(targetList!=null && targetList.size()>0){
			for (Map<String, Object> map : targetList) {
				String tmpKey = (String)map.get(key);
				resMap.put(tmpKey, map);
			}
		}
		return resMap;
	}
	
	/**
	 * 查询对应主损配件是否设定质保信息（现在存在多个主损配件）
	 * 规则：当存在某一个主损配件没有设定质保期则返回false
	 * @param claimPO 
	 * @return boolean true : 全部设定过 false :存在未设定的
	 */
	@Deprecated
	public boolean checkExistGuarantee(ClaimOrderVO orderVO){
		boolean result = true;
		
		//主配件列表
		List<PO> mainPartList = orderVO.getMainPartList();
		if(mainPartList==null || mainPartList.size()==0)//索赔单没有配件，则直接通过
			return result;
		//索赔申请单信息
		TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
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
		
		//查询该索赔单已经维护质保期的配件
		List<Map<String, Object>> partGPList = this.auditingDao.queryQualityByPart(Constant.STATUS_ENABLE, 
				claimPO.getId(), vehiclePO.getModelId(), provinceId, purchasedDate,orderVO.getCompanyId());
		
		if(partGPList==null || partGPList.size()<1){//当 三包  业务规则 的质保信息未设定，查询通用规则（系统规则）
			partGPList = this.auditingDao.querySystemQualityByPart(Constant.STATUS_ENABLE, claimPO.getId(),orderVO.getCompanyId());
		}
		
		if(partGPList==null || partGPList.size()<1){
			result = false;
		}else{
			Map<String,Map<String,Object>> partsGPMap = this.changeToMap(partGPList,"PART_CODE");
			for (PO po : mainPartList) {//遍历主要配件，检查质保期
				TtAsWrPartsitemPO partPO = (TtAsWrPartsitemPO)po;
				if(!partsGPMap.containsKey(partPO.getPartCode())){//未设定过质保期
					result = false;
					break;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param claimPO 索赔单信息
	 * @param partPO 配件信息
	 * @param partsGPMap 该索赔单设定过质保期信息
	 * @return boolean true:超过质保期 false:未超过质保期
	 */
	public boolean isOverGuaranteeLimit(TtAsWrApplicationPO claimPO,TtAsWrPartsitemPO partPO,
			Map<String,Map<String,Object>> partsGPMap){
		boolean result = false;
		
		if(partsGPMap!=null && partsGPMap.containsKey(partPO.getPartCode())){//设定过质保期
			Map<String,Object> partGPMap = partsGPMap.get(partPO.getPartCode());
			BigDecimal gurnMile = (BigDecimal) partGPMap.get("GURN_MILE");//免费里程
			BigDecimal gurnDate = (BigDecimal) partGPMap.get("GURN_MONTH");//免费保修月数
			
			if(gurnMile==null || gurnDate==null){//没有设定质保里程和保险月数
				result = true;
			}else{
				Double gpMileage = gurnMile.doubleValue();
				if((claimPO.getStartMileage()+gpMileage)<claimPO.getInMileage()){//验证里程
					result = true;
				}else{//验证质保时间
					
					Integer gpDate = gurnDate.intValue();//免费保修月数
					Date startDate = claimPO.getGuaranteeDate(); //购车日期(保修开始日期)
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(claimPO.getRoStartdate());
					calendar.add(Calendar.MONTH, -gpDate.intValue());
					Date tempDate = calendar.getTime();//工单开始日期-保修月数
					
					if(!tempDate.before(startDate)){//(工单开始日期-保修月数)是否在 购车日期 开始之后
						result = true;
					}
				}
			}
		}else{//没有设定质保里程和保险月数则跳过质保检测(存在对应规则检测)
			result =  false;
		}
		
		return result;
	}
	
	/**
	 * 查询车辆对应的三包策略
	 * 注意：如果没有查询到则返回 -1
	 * @param purchasedDate 车辆销售日期
	 * @param provinceId 经销商对应省份
	 * @param modelId 车型信息
	 * @param companyId 公司ID(区分轿车和微车)
	 * @return Long 三包策略ID 
	 */
	public Long findThreeGuaranteesStrategy(Date purchasedDate,Long provinceId,
    		Long modelId,Long companyId){
		
		/*
		Long strategyId = new Long(-1L);
		
		if(companyId==null)
			return strategyId;
		
		//根据销售日期、省份信息、车型信息和公司信息查询对应车辆的三包策略(业务三包策略)
		List<TtAsWrGamePO> gameList = auditingDao.queryGuaranteeCount(purchasedDate, provinceId, modelId, companyId);
		if(gameList!=null && gameList.size()>0){
			strategyId = gameList.get(0).getId();
		}else{//如果查询不到业务三包策略,则直接查询基础三包策略
			List<TtAsWrGamePO> sysGameList = auditingDao.querySystemStrategy(companyId);
			if(sysGameList!=null && sysGameList.size()>0){
				strategyId = sysGameList.get(0).getId();
			}
		}
		*/
		
		return this.findThreeGuaranteesStrategy(purchasedDate, provinceId, modelId, companyId, -1);
	}
	
	/**
	 * 查询车辆对应的三包策略
	 * 注意：如果没有查询到则返回 -1
	 * @param purchasedDate 车辆销售日期
	 * @param provinceId 经销商对应省份(省份CODE)
	 * @param modelId 车型信息
	 * @param companyId 公司ID(区分轿车和微车)
	 * @param yieldly 产地
	 * @return Long 三包策略ID 
	 */
	public Long findThreeGuaranteesStrategy(Date purchasedDate,Long provinceId,
    		Long modelId,Long companyId,long yieldly){
		
		Long strategyId = new Long(-1L);
		
		if(companyId==null)
			return strategyId;
		
		//根据销售日期、省份信息、车型信息和公司信息查询对应车辆的三包策略(业务三包策略)
		List<TtAsWrGamePO> gameList = auditingDao.queryGuaranteeCount(purchasedDate, 
				provinceId, modelId, companyId,yieldly);
		if(gameList!=null && gameList.size()>0){
			strategyId = gameList.get(0).getId();
		}else{//如果查询不到业务三包策略,则直接查询基础三包策略
			List<TtAsWrGamePO> sysGameList = auditingDao.querySystemStrategy(companyId);
			if(sysGameList!=null && sysGameList.size()>0){
				strategyId = sysGameList.get(0).getId();
			}
		}
		
		return strategyId;
	}
}
