/**********************************************************************
* <pre>
* FILE : ClaimAuditing.java
* CLASS : ClaimAuditing
* AUTHOR : XZM
* FUNCTION : 索赔申请上报-自动审核，包括功能：
*            [索赔申请单自动审核]
*            [索赔申请单人工审核处理]
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-05| XZM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ClaimAuditing.java,v 1.12 2011/07/28 03:38:31 xiongc Exp $
 */
package com.infodms.dms.actions.claim.auditing;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrAppauthitemPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.context.POContext;

/**
 * 索赔工单上报索赔单 自动审核
 * 注：加入异常数据处理，当索赔单中存在异常数据时，将索赔单退回
 * @author XZM
 */
public class ClaimAuditing implements Runnable{
	
	private Logger logger = Logger.getLogger(ClaimAuditing.class);
	/** 索赔申请单ID */
	private String claimId = "";
	
	public ClaimAuditing(String claimId){
		this.claimId = claimId;
	}

	/**
	 * 索赔单自动审核处理
	 * 步骤：
	 * 　　　0、对索赔单中需要统计的数据进行处理
	 *      1、根据自动审核规则检测索赔单
	 *      2、根据索赔授权规则（监控工时、监控配件和规则维护）检测对应索赔单是否需要
	 *         人工审核和需要审核的人员
	 * 扩展：
	 *      1、当索赔单审核出现异常，回自动将索赔单退回
	 */
	public void run() {
		logger.info("[索赔工单ID：" + claimId + "]自动审核开始==>");
		//审核标识
		boolean aAuditRes = true;
		boolean isError = false;
		
		ClaimOrderVO orderVO = new ClaimOrderVO();
		
		try{	
			//加载索赔申请单信息
			orderVO = this.loadClaimOrder();
			
			//步骤0:统计索赔单中需要统计数据并回写
			//this.writeBackClaimStatistics(orderVO);
			
			/*检测对应索赔单是否已经审核过
			 * 规则: 1、查询不到该索赔单信息的不在审核
			 *       2、索赔单状态不是"未上报"和"已上报"状态
			 * 满足以上规则的不再进行审核
			 */
			if(orderVO.getClaimPO()==null ){
				logger.error("[索赔单ID：" + this.claimId + "] 未查询到该索赔单信息！");
		    }else if(!(Constant.CLAIM_APPLY_ORD_TYPE_02.equals(orderVO.getClaimPO().getClaimType())||
				     Constant.CLAIM_APPLY_ORD_TYPE_01.equals(orderVO.getClaimPO().getClaimType()))){
		    	logger.info("[索赔单ID：" + this.claimId + "] 该索赔单已经审核完毕");
		    }
			
			//步骤0:自动通过规则检测
			//2010-11-04 添加 满足该类规则的自动通过
			aAuditRes = AutoAuditing.whiteRuleAuditing(orderVO);
			
			//步骤1:自动审核
			if(aAuditRes){
				aAuditRes = AutoAuditing.auditing(orderVO);
			}
			
			if(aAuditRes){//自动审核通过
				//步骤2:人工审核（处理）
				aAuditRes = ManualAuditing.auditing(orderVO);
			}
			
			if(aAuditRes){//经过自动审核和人工审核（处理）[即不需要人工审核]
				
				String status = Constant.CLAIM_APPLY_ORD_TYPE_04.toString();
				//将对应工单标识为审核通过
				ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
				auditingDao.updateClaimOrderStatus(status, Constant.IS_AUTO_AUDITING,
						"-1", claimId,"","");
				
				//记录索赔申请单审核的授权记录
				TtAsWrAppauthitemPO appAuthPO = new TtAsWrAppauthitemPO();
				appAuthPO.setId(Long.parseLong(claimId));
				appAuthPO.setApprovalPerson("自动审核"+" [授权审核]");//自动审核 审核人员 固定为100
				appAuthPO.setApprovalLevelCode("100");//自动审核 授权角色 固定为100
				appAuthPO.setApprovalDate(new Date());
				appAuthPO.setApprovalResult(status);//授权结果=索赔申请单状态
				appAuthPO.setRemark("");//审核通过备注信息为空
				appAuthPO.setCreateBy(new Long(-1));
				appAuthPO.setCreateDate(new Date());
				auditingDao.insertClaimAppAuth(appAuthPO);
			}
		} catch (Exception e) {
			isError = true;
			e.printStackTrace();
		}
		
		try{
			if(isError){//如果存在异常，则将索赔单置成"审核退回",需要用户调整后重新上报
				String status = Constant.CLAIM_APPLY_ORD_TYPE_06.toString();
				//将对应工单标识为审核通过
				ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
				auditingDao.updateClaimOrderStatus(status, Constant.IS_AUTO_AUDITING,
						"-1", claimId,"","处理异常，请修改重新上报");
				
				//记录索赔申请单审核的授权记录
				TtAsWrAppauthitemPO appAuthPO = new TtAsWrAppauthitemPO();
				appAuthPO.setId(Long.parseLong(claimId));
				appAuthPO.setApprovalPerson("自动审核"+" [授权审核]");//自动审核 审核人员 固定为100
				appAuthPO.setApprovalLevelCode("100");//自动审核 授权角色 固定为100
				appAuthPO.setApprovalDate(new Date());
				appAuthPO.setApprovalResult(status);//授权结果=索赔申请单状态
				appAuthPO.setRemark("处理异常，请修改重新上报");//审核通过备注信息为空
				appAuthPO.setCreateBy(new Long(-1));
				appAuthPO.setCreateDate(new Date());
				auditingDao.insertClaimAppAuth(appAuthPO);
			}
			
			//步骤0:统计索赔单中需要统计数据并回写
			this.writeBackClaimStatistics(orderVO);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//提交事务
			try {
				this.cleanTransaction();
			} catch (Exception e) {
			}
		}
		
		logger.info("[索赔工单ID：" + claimId + "]自动审核结束==<");
	}
	
	/**
	 * 处理需要统计的信息，同时回写
	 */
	private void writeBackClaimStatistics(ClaimOrderVO orderVO){
		if(orderVO==null || orderVO.getClaimPO()==null)
			return;
		TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
		//回写作业次数（相同车辆，相同工时索赔申请的次数）
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		String expertStatus = Constant.CLAIM_APPLY_ORD_TYPE_08+","+Constant.CLAIM_APPLY_ORD_TYPE_07;
		auditingDao.statisLabourCount(claimPO.getVin(), claimPO.getId(), expertStatus,claimPO.getOemCompanyId());
	}
	
	/**
	 * 加载索赔申请单信息
	 * @return ClaimOrderVO
	 */
	private ClaimOrderVO loadClaimOrder(){
		
		ClaimOrderVO result = new ClaimOrderVO();
		
		//加载索赔申请单信息
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		TtAsWrApplicationPO claimPO = auditingDao.queryClaimById(this.claimId);
		if(claimPO==null)//没有查询到索赔单则返回
			return result;
		result.setClaimPO(claimPO);
		
		//加载车辆信息
		String vin = claimPO.getVin();
		if(vin!=null && !"".equals(vin)){
			TmVehiclePO vehiclePO = auditingDao.queryVehicle(vin);
			if(vehiclePO!=null){//存在对应车辆信息
				result.setVehiclePO(vehiclePO);
			}
		}
		
		//加载索赔申请单对应经销商信息和公司信息
		List<PO> resList = auditingDao.queryDealerById(claimPO.getDealerId());
		if(resList!=null && resList.size()>0){
			TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
			result.setDealerPO(dealerPO);
			result.setCompanyId(dealerPO.getOemCompanyId());
		}
		
		//加载索赔申请单之配件
		List<PO> partList = auditingDao.queryPartsByClaimid(this.claimId,null);
		if(partList!=null && partList.size()>0){
			result.setPartsList(partList);
		}
		
		//加载索赔申请单之索赔工时
		List<PO> laborList = auditingDao.queryManhourByClaimid(this.claimId,null);
		if(laborList!=null && laborList.size()>0){
			result.setLaborList(laborList);
		}
		
		//加载索赔申请单之主要配件代码
		List<PO> mainPartList = auditingDao.queryPartsByClaimid(this.claimId, Constant.IS_MAIN_TROUBLE);
		if(mainPartList!=null && mainPartList.size()>0){
			result.setMainPartList(mainPartList);
		}
		
		//加载索赔申请单之主要工时代码(一张索赔申请单中只有一个主要工时)
		List<PO> mainLabourList = auditingDao.queryManhourByClaimid(this.claimId, Constant.IS_MAIN_TROUBLE);
		if(mainLabourList!=null && mainLabourList.size()>0){
			result.setMainLabourList(mainLabourList);
		}

		//加载索赔申请单之索赔车型组和车型信息(工时车型组)
		Map<String,Object> vehicleMap = auditingDao.queryVehicleByVin(claimPO.getVin(),
				Constant.WR_MODEL_GROUP_TYPE_01.toString(),result.getCompanyId());
		if(vehicleMap!=null){
			if(vehicleMap.containsKey("WRGROUP_CODE")){
				String wrGroup = (String) vehicleMap.get("WRGROUP_CODE");//索赔车型组代码
				result.setWrGroupCode(wrGroup);
			}
			if(vehicleMap.containsKey("WRGROUP_ID")){
				String wrGroup = ((BigDecimal) vehicleMap.get("WRGROUP_ID")).toString();//索赔车型组ID
				result.setWrGroupId(wrGroup);
			}
			if(vehicleMap.containsKey("WRGROUP_NAME")){
				String wrGroup = (vehicleMap.get("WRGROUP_NAME")).toString();//索赔车型组名称
				result.setWrGroupName(wrGroup);
			}
		}
		
		//加载索赔申请单之索赔车型组和车型信息(配件车型组)
		/*// MODIFY BY XZM 20100917 现在没有配件车型组（长安）
		Map<String,Object> vehicleMap2 = auditingDao.queryVehicleByVin(claimPO.getVin(),
				Constant.WR_MODEL_GROUP_TYPE_02.toString(),result.getCompanyId());
		if(vehicleMap2!=null){
			if(vehicleMap2.containsKey("WRGROUP_CODE")){
				String wrGroup = (String) vehicleMap2.get("WRGROUP_CODE");//索赔车型组代码
				result.setWrPartGroupCode(wrGroup);
			}
			if(vehicleMap2.containsKey("WRGROUP_ID")){
				String wrGroup = ((BigDecimal) vehicleMap2.get("WRGROUP_ID")).toString();//索赔车型组ID
				result.setWrPartGroupId(wrGroup);
			}
		}
		*/
		return result;
	}

	private void cleanTransaction() throws Exception{
		try{
			ActionContext atx = ActionContext.getContext();
			if( atx.getException()!=null ){
				POContext.endTxn(false);
			}else{
				POContext.endTxn(true);
			}			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				POContext.cleanTxn();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
