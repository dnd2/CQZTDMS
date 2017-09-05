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
 * $Id: ClaimAuditingNew2.java,v 1.5 2012/09/28 07:28:02 xiongc Exp $
 */
package com.infodms.dms.actions.claim.balanceAuditing;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.application.BalanceStatusRecord;
import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.custom.AuditingVO;
import com.infodms.dms.bean.AclUserBean; 
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.application.ClaimManualAuditingDao;
import com.infodms.dms.dao.claim.application.DealerBalanceDao;
import com.infodms.dms.dao.claim.auditing.BalanceAuditingDao;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrAppauthitemPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrWrauthorizationPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.context.POContext;

/**
 * 索赔工单上报索赔单 自动审核
 * 注：加入异常数据处理，当索赔单中存在异常数据时，将索赔单退回
 * @author XZM
 */
public class ClaimAuditingNew2{
	
	private Logger logger = Logger.getLogger(BalanceAuditingNew.class);
	/** 索赔申请单ID */
	private String claimId = "";
	/** 结算单ID */
//	private String balanceId = "";
	
	public ClaimAuditingNew2(String claimId){
		this.claimId = claimId;
//		this.balanceId = balanceId;
	}

	/**
	 * 结算单自动审核处理
	 * 步骤：
	 *      1、根据自动审核规则检测索赔单
	 *      2、根据索赔授权规则（监控工时、监控配件和规则维护）检测对应索赔单是否需要
	 *         人工审核和需要审核的人员
	 * 扩展：
	 *      1、当索赔单审核出现异常，回自动将索赔单退回
	 */
	@SuppressWarnings("unchecked")
	public void run() throws Exception{
		logger.info("[索赔工单ID：" + claimId + "]结算自动审核开始==>");
		
		try{	
			//加载索赔申请单信息
			ClaimOrderVO orderVO = this.loadClaimOrder();
			
			/*检测对应索赔单是否已经审核过
			 * 规则: 1、查询不到该索赔单信息的不在审核
			 * 满足以上规则的不再进行审核
			 */
			if(orderVO.getClaimPO()==null){
				logger.error("[索赔单ID：" + this.claimId + "] 未查询到该索赔单信息！");
				return;
		    }		
			
			BalanceMonitorAuditing balanceAuditing = new BalanceMonitorAuditing();
			AuditingVO auditingVO = balanceAuditing.deal(orderVO);
			TreeSet<Integer> roles = new TreeSet<Integer>();
			StringBuilder sbuilder = new StringBuilder();
			this.dealRole(roles, auditingVO);
			this.dealReason(sbuilder, auditingVO);
			String role = this.getRoleStr(roles);
			
			ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
			BalanceAuditingDao balanceAuditingDao = BalanceAuditingDao.getInstance();
			
			if(role!=null && !"".equals(role)){//需要人工结算审核
				
				//记录需要人工审核的授权级别（按顺序审核）
				String reason = sbuilder.toString();
				reason = CommonUtils.checkNull(reason);
				int limit = 2000/3-1;
				if(reason.length()>limit){//防止信息长度超过数据库字段限制
					reason = reason.substring(0,limit);
				}
				TtAsWrWrauthorizationPO authPO = new TtAsWrWrauthorizationPO();
				authPO.setId(Long.parseLong(claimId));
				authPO.setApprovalLevelCode(role);
				authPO.setApprovalReason(reason);
				authPO.setApprovalLevelBak(role);
				authPO.setCreateDate(new Date());
				authPO.setCreateBy(new Long(-1));//自动审核 创建人为-1
				auditingDao.insertClaimAuth(authPO);
				
				//更新对应索赔申请单首个人工审核角色（自动审核 将修改人 修改为-1）
				String firstRole = "";
				String roleArr[] = role.split(",");
				if(roleArr!=null && roleArr.length>0)
					firstRole = roleArr[0];
				
				if(firstRole==null||firstRole.equals("")){ 
					throw new Exception("firstRole mei you:"+firstRole);
				}
				
				//索赔单审核（5月新规则）
				/****add xiongchuan 2011-7-26 *****售前维修的单据必须经过结算室****/
//				if(!orderVO.getClaimPO().getClaimType().equals(Constant.CLA_TYPE_07)){
					
				
				if(("1".equals(orderVO.getClaimPO().getIsAuto().toString()))||
						("0".equals(orderVO.getClaimPO().getIsAuto().toString())&&
								(Constant.CLA_TYPE_06.toString().equals(orderVO.getClaimPO().getClaimType().toString())||Constant.CLA_TYPE_02.toString().equals(orderVO.getClaimPO().getClaimType().toString())))){
					DealerBalanceDao balanceDao2 = DealerBalanceDao.getInstance();
					TtAsWrApplicationPO cb = new TtAsWrApplicationPO();
					cb.setId(Long.valueOf(claimId));
					TtAsWrApplicationPO cbValue = new TtAsWrApplicationPO();
					cbValue.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_07);
					cbValue.setAccountDate(new Date());
					cbValue.setUpdateDate(new Date());
					balanceDao2.update(cb, cbValue);
					//记录索赔结算审核的授权记录
					TtAsWrAppauthitemPO appAuthPO = new TtAsWrAppauthitemPO();
					appAuthPO.setId(Long.parseLong(claimId));
					appAuthPO.setApprovalPerson("自动审核"+" [结算审核]");//自动审核 审核人员 固定为100
					appAuthPO.setApprovalLevelCode(role);//
					appAuthPO.setApprovalDate(new Date());
					appAuthPO.setApprovalResult(Constant.CLAIM_APPLY_ORD_TYPE_07.toString());//授权结果=索赔申请单状态
					appAuthPO.setRemark("");//审核通过备注信息为空
					appAuthPO.setCreateBy(new Long(-1));
					appAuthPO.setCreateDate(new Date());
					
					auditingDao.insertClaimAppAuth(appAuthPO);
				}else{
					DealerBalanceDao balanceDao2 = DealerBalanceDao.getInstance();
					TtAsWrApplicationPO cb = new TtAsWrApplicationPO();
					cb.setId(Long.valueOf(claimId));
					TtAsWrApplicationPO cbValue = new TtAsWrApplicationPO();
					cbValue.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_08);
//					cbValue.setAccountDate(new Date());
					cbValue.setUpdateDate(new Date());
					balanceDao2.update(cb, cbValue);
					balanceAuditingDao.updateClaimOrderStatus("-1", claimId,firstRole,"");
				}
//			}else{
//				DealerBalanceDao balanceDao2 = DealerBalanceDao.getInstance();
//				TtAsWrApplicationPO cb = new TtAsWrApplicationPO();
//				cb.setId(Long.valueOf(claimId));
//				TtAsWrApplicationPO cbValue = new TtAsWrApplicationPO();
//				cbValue.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_08);
////				cbValue.setAccountDate(new Date());
//				cbValue.setUpdateDate(new Date());
//				balanceDao2.update(cb, cbValue);
//				balanceAuditingDao.updateClaimOrderStatus("-1", claimId,firstRole,"");
//			}
				
				
			}else{//自动审核通过
				TtAsWrApplicationPO claimPO = new TtAsWrApplicationPO();
				claimPO.setAuditType(Constant.IS_MANUAL_AUDITING+"");
				claimPO.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_07);
				claimPO.setUpdateBy(-1L);
				claimPO.setUpdateDate(new Date());
				ClaimManualAuditingDao mAuditingDao = new ClaimManualAuditingDao();
				mAuditingDao.updateClaimInfo(claimPO, Long.parseLong(claimId));
				balanceAuditingDao.updateClaimStatus(Long.parseLong(claimId), Constant.STATUS_ENABLE);
				//判断对应结算单对应索赔单是否都已经审核通过，都通过则修改结算单状态
				//注：现在结算自动审核使用定时时，逐条审核，不需要使用同步
				/*******mod by liuxh 2010101202 自动审核时不改变结算单状态 也不重新计算金额*******/
			    //////this.modifyBalanceStatus(Long.parseLong(balanceId), false,(new AclUserBean()));
			    /*******mod by liuxh 2010101202 自动审核时不改变结算单状态 也不重新计算金额*******/
			}
			
			//记录索赔结算审核的授权记录
			TtAsWrAppauthitemPO appAuthPO = new TtAsWrAppauthitemPO();
			appAuthPO.setId(Long.parseLong(claimId));
			appAuthPO.setApprovalPerson("自动审核"+" [结算审核]");//自动审核 审核人员 固定为100
			appAuthPO.setApprovalLevelCode(role);//
			appAuthPO.setApprovalDate(new Date());
			appAuthPO.setApprovalResult(Constant.CLAIM_APPLY_ORD_TYPE_08.toString());//授权结果=索赔申请单状态
			appAuthPO.setRemark("");//审核通过备注信息为空
			appAuthPO.setCreateBy(new Long(-1));
			appAuthPO.setCreateDate(new Date());
			
			auditingDao.insertClaimAppAuth(appAuthPO);
			
			//////////更新索赔单任务审核标志为已审核///////////
			String sql="UPDATE TT_AS_WR_APPLICATION SET TASK_FLAG='1' WHERE ID=? ";
			List listPar=new ArrayList();
			listPar.add(Long.valueOf(claimId));
			balanceAuditingDao.update(sql, listPar);
			logger.info("更新索赔单审核标志.......");
			POContext.endTxn(true);
		} catch (Exception e) {
			logger.error("自动审核索赔单错误,索赔单ID="+this.claimId);
			logger.error(e);
			POContext.endTxn(false);
			e.printStackTrace();
			throw e;
		}finally{
			POContext.cleanTxn();
		}
		logger.info("[索赔工单ID：" + claimId + "]结算自动审核结束==<");
	}
	
	/**
	 * 将集合中的角色按顺序拼成字符串
	 * 格式：角色1+","+角色2+","+ ...
	 * @param roles
	 * @return
	 */
	private String getRoleStr(TreeSet<Integer> roles){
		String role = "";
		if(roles!=null && roles.size()>0){
			Iterator<Integer> iter = roles.iterator();
			while(iter.hasNext()){//如果存在100则拒绝该工单
				Integer tempRole = iter.next();
				role = role + tempRole + ",";
			}
		}
		return role;
	}
	
	/**
	 * 将某一规则需要审核的角色，放入到审核角色容器中
	 * @param set 审核角色容器
	 * @param auditingVO 审核需要角色和原因（某一规则）
	 */
	private void dealRole(TreeSet<Integer> set,AuditingVO auditingVO){
		if(auditingVO!=null && auditingVO.getRoles()!=null){
			SortedSet<String> ss =auditingVO.rolesToArrays();
			Iterator<String> iter = ss.iterator();
			while(iter.hasNext()){
				try{
					Integer role = Integer.parseInt(iter.next());
					if(!set.contains(role))
						set.add(role);
				}catch(Exception e){
				}
			}
		}
	}
	
	/**
	 * 将某一规则需要审核的原因，放入到审核角色容器中
	 * @param sbuilder 审核原因容器
	 * @param auditingVO 审核需要角色和原因（某一规则）
	 */
	private void dealReason(StringBuilder sbuilder,AuditingVO auditingVO){
		if(auditingVO!=null && auditingVO.getReasions()!=null && !"".equals(auditingVO.getReasions())){
			sbuilder.append(auditingVO.getReasions()).append(" \n");
		}
	}
	
	/**
	 * 加载索赔申请单信息
	 * @return ClaimOrderVO
	 */
	private ClaimOrderVO loadClaimOrder(){
		
		ClaimOrderVO result = new ClaimOrderVO();
		
		//加载索赔申请单信息 1
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		TtAsWrApplicationPO claimPO = auditingDao.queryClaimById(this.claimId);
		if(claimPO==null)//没有查询到索赔单则返回
			return result;
		result.setClaimPO(claimPO);
		
		//加载车辆信息 1
		String vin = claimPO.getVin();
		if(vin!=null && !"".equals(vin)){
			TmVehiclePO vehiclePO = auditingDao.queryVehicle(vin);
			if(vehiclePO!=null){//存在对应车辆信息
				result.setVehiclePO(vehiclePO);
			}
		}
		
		//加载索赔申请单对应经销商信息和公司信息 1
		List<PO> resList = auditingDao.queryDealerById(claimPO.getDealerId());
		if(resList!=null && resList.size()>0){
			TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
			result.setDealerPO(dealerPO);
			result.setCompanyId(dealerPO.getOemCompanyId());
		}
		
		//加载索赔申请单之主要工时代码 1
		List<PO> mainLabourList = auditingDao.queryManhourByClaimid(this.claimId, Constant.IS_MAIN_TROUBLE);
		if(mainLabourList!=null && mainLabourList.size()>0){
			result.setMainLabourList(mainLabourList);
		}

		return result;
	}
	
	/**
	 * 检测对应结算单中索赔单是否都已审核完成，完成后，修改结算单状态
	 * @param balanceId 结算单ID
	 * @param isUpdateData 是否更新结算单中结算金额
	 * @param logonUser 登陆用户信息
	 */
	public void modifyBalanceStatus(Long balanceId,boolean isUpdateData,AclUserBean logonUser){
		//检测是否该结算单对应的索赔单都已经审核完毕
		BalanceAuditingDao balanceAuditingDao = BalanceAuditingDao.getInstance();
		List<?> claimList = balanceAuditingDao.queryClaimByBalanceId(balanceId, Constant.STATUS_DISABLE);
		if(claimList==null || claimList.size()==0){//所有索赔单都审核通过
			//更新结算单状态"结算审核完成"
			balanceAuditingDao.updateBalanceStatus(balanceId, Integer.parseInt(Constant.ACC_STATUS_02), -1L);
			//6、记录审核结果
			BalanceStatusRecord.recordStatus(balanceId, logonUser.getUserId(), logonUser.getName(), 
					logonUser.getOrgId(), BalanceStatusRecord.STATUS_04);
			if(isUpdateData){//更新结算单和索赔单中数据
				DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
				//加载保养费用中固定的工时费用
				Double fixedAmount = balanceDao.queryFreeClaimFixedAmount(Constant.FREE_PARA_TYPE);
				//更新结算主表信息
				balanceDao.reCheckBalanceAmount(balanceId,fixedAmount);
				//更新结算明细表信息
				balanceDao.reCheckBalanceDetail(balanceId,fixedAmount);
			}
		}
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
			POContext.cleanTxn();
		}
	}
}
