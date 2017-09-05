package com.infodms.dms.actions.claim.auditing;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.infodms.dms.actions.claim.auditing.rule.custom.AuditingVO;
import com.infodms.dms.actions.claim.auditing.rule.custom.CustomMonitorAuditing;
import com.infodms.dms.actions.claim.auditing.rule.custom.FixedRuleMonitorAuditing;
import com.infodms.dms.actions.claim.auditing.rule.custom.MonitorManHourAuditing;
import com.infodms.dms.actions.claim.auditing.rule.custom.MonitorPartAuditing;
import com.infodms.dms.actions.claim.auditing.rule.custom.MonitorPartBigAuditing;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrAppauthitemPO;
import com.infodms.dms.po.TtAsWrWrauthorizationPO;
import com.infodms.dms.util.CommonUtils;


/**
 * 人工审核规则
 * 前提：对应申请单需要通过自动审核
 * 功能：查询并标识对应申请单需要人工审核的级别，可以多个级别按顺序审核
 * @author XZM
 */
public class ManualAuditing {
	
	/**
	 * 人工审核规则处理
	 * 流程：
	 *     1、检测索赔申请单中是否存在需要监控的配件
	 *        (监控配件维护 功能 中定义规则)
	 *     2、检测索赔申请单中是否存在需要监控的工时
	 *        (监控工时维护 功能 中定义规则)
	 *     3、检测索赔申请单是否满足设定规则
	 *        (授权规则维护 功能 中定义规则，非自动拒绝)
	 * @param claimPO 索赔申请单信息
	 * @return boolean true : 不需要人工审核(审核通过) false : 需要人工审核
	 */
	public static boolean auditing(ClaimOrderVO orderVO){
		
		boolean res = true;
		
		try{
			
			TreeSet<Integer> roles = new TreeSet<Integer>();
			StringBuilder sbuilder = new StringBuilder();
			if(preConditon(orderVO)){//测试需要人工审核的条件
				
				//监控工时处理
				MonitorManHourAuditing manhourAuditing = new MonitorManHourAuditing();
				AuditingVO manhourVO = manhourAuditing.deal(orderVO);
				dealRole(roles,manhourVO);
				dealReason(sbuilder,manhourVO);
				//监控配件处理
				MonitorPartAuditing partAuditing = new MonitorPartAuditing();
				AuditingVO partVO = partAuditing.deal(orderVO);
				dealRole(roles,partVO);
				dealReason(sbuilder,partVO);
				
				//监控配件大类处理Iverson update 2010-11-10
				MonitorPartBigAuditing partBigAuditing = new MonitorPartBigAuditing();
				AuditingVO partBigVO = partBigAuditing.deal(orderVO);
				dealRole(roles,partBigVO);
				dealReason(sbuilder,partBigVO);
				
				//自动规则处理
				CustomMonitorAuditing customAuding = new CustomMonitorAuditing();
				AuditingVO customVO = customAuding.deal(orderVO);
				dealRole(roles,customVO);
				dealReason(sbuilder,customVO);
				
			}
			
			//固定规则处理 
			FixedRuleMonitorAuditing fixedAuding = new FixedRuleMonitorAuditing();
			AuditingVO fixedVO = fixedAuding.deal(orderVO);
			dealRole(roles,fixedVO);
			dealReason(sbuilder,fixedVO);
			
			ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
			String claimId = orderVO.getClaimPO().getId().toString();
			
			if(roles!=null && roles.size()>0){//需要人工审核或被自动拒绝
				res = false;
				String role = "";
				Iterator<Integer> iter = roles.iterator();
				
				int i = 1;
				String firstRole = "";
				
				while(iter.hasNext()){//如果存在100则拒绝该工单
					Integer tempRole = iter.next();
					if(i==1)//取得第一个需要审核的级别
						firstRole = tempRole.toString();
					i++;	
					if("100".equals(tempRole.toString())){
						role = "100";
						break;
					}else{
						role = role + tempRole + ",";
					}
				}
				
				String status = "";
				
				if("100".equals(role)){//自动拒绝
					status = Constant.CLAIM_APPLY_ORD_TYPE_05.toString();//审核拒绝
				}else{//需要人工审核
					status = Constant.CLAIM_APPLY_ORD_TYPE_03.toString();//审核中
					
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
				}
				
				//更新对应索赔申请单状态  （自动审核 将修改人 修改为-1）
				auditingDao.updateClaimOrderStatus(status, Constant.IS_AUTO_AUDITING, 
						"-1", claimId,firstRole,"");
				
				//记录索赔申请单审核的授权记录
				TtAsWrAppauthitemPO appAuthPO = new TtAsWrAppauthitemPO();
				appAuthPO.setId(Long.parseLong(claimId));
				appAuthPO.setApprovalPerson("自动审核"+" [授权审核]");//自动审核 审核人员 固定为100
				appAuthPO.setApprovalLevelCode(role);//自动审核 授权角色 
				appAuthPO.setApprovalDate(new Date());
				appAuthPO.setApprovalResult(status);//授权结果=索赔申请单状态
				appAuthPO.setRemark(sbuilder.toString());//备注=审核不通过理由
				appAuthPO.setCreateBy(new Long(-1));
				appAuthPO.setCreateDate(new Date());
				auditingDao.insertClaimAppAuth(appAuthPO);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return res;
	}
	
	/**
	 * 将某一规则需要审核的角色，放入到审核角色容器中
	 * @param set 审核角色容器
	 * @param auditingVO 审核需要角色和原因（某一规则）
	 */
	private static void dealRole(TreeSet<Integer> set,AuditingVO auditingVO){
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
	private static void dealReason(StringBuilder sbuilder,AuditingVO auditingVO){
		if(auditingVO!=null && auditingVO.getReasions()!=null && !"".equals(auditingVO.getReasions())){
			sbuilder.append(auditingVO.getReasions()).append(" \n");
		}
	}
	
	/**
	 * 人工审核前提条件
	 * @param orderVO
	 * @return
	 */
	private static boolean preConditon(ClaimOrderVO orderVO){
		
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
}
