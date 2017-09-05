package com.infodms.dms.actions.claim.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TmDownParameterPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrApplicationPO;

/**
 * 工时相关操作工具类
 * @author Administrator
 */
public class LabourUtil {

	public Double getLabourPrice(String claimId,Long companyId){
		
		Double result = 0.0;
		
		//查询车型组信息（车型组ID，代码）、经销商信息（ID）
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		TtAsWrApplicationPO claimPO = auditingDao.queryClaimById(claimId);
		if(claimPO==null)//没有查询到索赔单则返回
			return result;
		
		//加载车辆信息
		String vin = claimPO.getVin();
		TmVehiclePO vehiclePO = null;
		if(vin!=null && !"".equals(vin)){
			vehiclePO = auditingDao.queryVehicle(vin);
		}
		
		if(vehiclePO==null)
			return result;
		
		//加载索赔申请单之索赔车型组和车型信息(工时车型组)
		Map<String,Object> vehicleMap = auditingDao.queryVehicleByVin(claimPO.getVin(),
				Constant.WR_MODEL_GROUP_TYPE_01.toString(),companyId);
		String wrGroupCode = "";
		String wrGroupId = "";
		if(vehicleMap!=null){
			if(vehicleMap.containsKey("WRGROUP_CODE")){
				wrGroupCode = (String) vehicleMap.get("WRGROUP_CODE");//索赔车型组代码
			}
			if(vehicleMap.containsKey("WRGROUP_ID")){
				wrGroupId = ((BigDecimal) vehicleMap.get("WRGROUP_ID")).toString();//索赔车型组ID
			}
		}
		
		if(!Utility.testString(wrGroupCode) ||!Utility.testString(wrGroupId))
			return result;
		
		//查询工时费用
		result = this.getLabourPrice(claimPO.getDealerId(), companyId, wrGroupCode);
		
		return result;
	}
	
	/**
     * 查询对应经销商工时费用
     * @param dealerId 经销商ID
     * @param companyId 公司ID
     * @param wrGroupCode 车型组代码
	 * @return
	 */
	public Double getLabourPrice(Long dealerId,Long companyId,String wrGroupCode){
		
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		List<TmDownParameterPO> labourPriceList = auditingDao.getLabourPrice(dealerId, companyId, wrGroupCode);
		
		Double result = 0.0;
		if(labourPriceList!=null && labourPriceList.size()>0){
			String price = labourPriceList.get(0).getParameterValue();
			if(Utility.testString(price))
				result = Double.parseDouble(price);
		}
		
		return result;
	}
}
