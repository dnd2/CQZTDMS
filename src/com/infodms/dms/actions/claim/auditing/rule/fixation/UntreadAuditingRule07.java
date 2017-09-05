package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.Map;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.util.CommonUtils;

/**
 * VIN码不合法(R07A)\未报销售,无档案(R07B)\车型填写有误(R07C)
 * <pre>
 * 前提：
 *     无
 * 规则：
 *     规则:R07A:索赔申请单上的VIN 码不在主机厂的车辆档中
 *     规则:R07B 如果索赔类型不为”PDI索赔 “,业务对象<<车辆>>中销售日期为空
 *          (或者为 1971年)以前,则退回 
 *     规则:R07C 如果当前申请单中的车型代码 与<<车辆>>档的车辆代码不一致则退回
 * </pre>
 * [修改] 2010-06-22 XZM 未报销售、无档案 规则
 *    原：判断对应车辆信息中 的 购车时间 是否为空 ，不为空时，时间是否在1971之前
 *        满足上述条件 则无销售档案
 *    新：判断车辆信息中的 销售状态 是否 为 实销
 * [新增] 2010-07-20 XZM
 *    新增 R07C规则判断
 * [修改] 2010-07-26 XZM
 *    不校验车辆是否实际销售
 * @author XZM
 */
public class UntreadAuditingRule07 extends AbstractAuditingRule {

	private String R07A = "VIN码不合法";
	//private String R07B = "未报销售,无档案";
	private String R07C = "车型填写有误";
	
	@Override
	protected String getRuleDesc() {
		return "VIN码不合法/未报销售,无档案/车型填写有误";
	}

	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		return true;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		//检测VIN码合法性
		TmVehiclePO vehiclePO = orderVO.getVehiclePO();
		if(vehiclePO==null){//对应车辆没有记录
			this.backInfo = this.R07A;
			result = true;
			return result;
		}
		
		//检测是否有销售记录
		/*
		Integer status = vehiclePO.getLifeCycle();
		if(!result && !Constant.VEHICLE_LIFE_04.equals(status)){//不时实销状态
			this.backInfo = this.R07B;
			result = true;
		}*/
		
		//车型填写有错误 (现在该项信息是通过索赔申请时检测对应VIN时得到的车型码)
		if(!result){
			TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
			//检测是否同车辆信息中保存相等
			if(vehiclePO.getPackageId()!=null){
				ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
				Map<String,Object> vchlMap = auditingDao.queryVechileDetail(vehiclePO.getPackageId());
				
				if(vchlMap==null || vchlMap.size()==0){
					result = true;
				}else if(!CommonUtils.checkNull(claimPO.getLicenseNo()).equals(CommonUtils.checkNull(vehiclePO.getLicenseNo()))){//车牌号
					result = true;
				}else if(!CommonUtils.checkNull(claimPO.getEngineNo()).equals(CommonUtils.checkNull(vehiclePO.getEngineNo()))){//发动机号
					result = true;
				}else if(!CommonUtils.checkNull(claimPO.getBrandCode()).equals(CommonUtils.checkNull(vchlMap.get("BRAND_CODE")))){//品牌
					result = true;
				}else if(!CommonUtils.checkNull(claimPO.getSeriesCode()).equals(CommonUtils.checkNull(vchlMap.get("SERIES_CODE")))){//车系
					result = true;
				}else if(!CommonUtils.checkNull(claimPO.getModelCode()).equals(CommonUtils.checkNull(vchlMap.get("MODEL_CODE")))){//车型
					result = true;
				}else if(!CommonUtils.checkNull(claimPO.getGearboxNo()).equals(CommonUtils.checkNull(vehiclePO.getGearboxNo()))){//变速箱号
					result = true;
				}else if(!CommonUtils.checkNull(claimPO.getRearaxleNo()).equals(CommonUtils.checkNull(vehiclePO.getRearaxleNo()))){//后桥号
					result = true;
				}else if(!CommonUtils.checkNull(claimPO.getTransferNo()).equals(CommonUtils.checkNull(vehiclePO.getTransferNo()))){//分驱动器号
					result = true;
				}
			}else{//没有配置信息则直接拒绝
				result = true;
			}
			
			if(result)
				this.backInfo = this.R07C;
		}
		
		return result;
	}

}
