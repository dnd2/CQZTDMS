package com.infodms.dms.actions.claim.auditing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.application.BalanceStatusRecord;
import com.infodms.dms.actions.claim.balanceAuditing.BalanceAuditing;
import com.infodms.dms.actions.claim.balanceAuditing.BalanceAuditingNew;
import com.infodms.dms.actions.claim.balanceAuditing.ClaimAuditingNew;
import com.infodms.dms.actions.claim.balanceAuditing.ClaimAuditingNew2;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.dao.claim.application.DealerBalanceDao;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TrGatherBalancePO;
import com.infodms.dms.po.TtAsWrAppauthitemPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrGatherBalancePO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.dms.chana.actions.AbstractSendTask;
import com.infoservice.po3.core.context.POContext;

/**
 * 结算单定时
 * @author Administrator
 *
 */
public class ClaimAuditingJob extends AbstractSendTask {

	private static Logger logger = Logger.getLogger(BalanceAuditingJob.class);
	@SuppressWarnings("unchecked")
	@Override
	protected synchronized String handleExecute() throws Exception {
		logger.info("ClaimAuditingJob[START]======>");
		//加入同步
		boolean isDeal = DBLockUtil.lock("10001", DBLockUtil.BUSINESS_TYPE_12);
		try{
			if(isDeal){ 
					autoAuditing();
			}else{
				System.out.println("[100001]同步放弃本次执行!");
			}
		}catch(Exception e){
		}finally{
		}

		logger.info("ClaimAuditingJob[END]======>");
		return "";
	}
	
	/**
	 * 结算单自动审核
	 * @param balanceId
	 */
	public void autoAuditing() throws Exception{
		
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
//		String balanceIdStr = balanceId.toString();
		//查询结算单对应索赔单
		List<Map<String,Object>> claimList = balanceDao.queryAutoClaim();
		//循环审核每条索赔单
		int count=0;
		for (Map map : claimList) {
			try{
				DealerBalanceDao balanceDao2 = DealerBalanceDao.getInstance();
				ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
				count++;
				String claimId = map.get("ID").toString();
				logger.info("号号号号号第"+count+"+条+"+"="+claimId);
				ClaimAuditingNew2 ba = new ClaimAuditingNew2(claimId);
				ba.run();
				
				//zhumingwei 2012-10-16 begion
				
				//根据索赔单ID找到工时明细
				List<Map<String,Object>> labourCode = balanceDao.queryAutolabourcode(claimId);
				//然后在根据索赔单ID找到车vin码
				TtAsWrApplicationPO po = new TtAsWrApplicationPO();
				po.setId(Long.parseLong(claimId));
				TtAsWrApplicationPO poValue = (TtAsWrApplicationPO)balanceDao2.select(po).get(0);
				String vin = poValue.getVin();
				//然后循环这个索赔单对应工时明细查找作业次数
				for (Map map1 : labourCode) {
					String count1 = balanceDao.queryCount(vin,map1.get("WR_LABOURCODE").toString());
					TtAsWrLabouritemPO popo = new TtAsWrLabouritemPO();
					popo.setLabourId(Long.parseLong(map1.get("LABOUR_ID").toString()));
					TtAsWrLabouritemPO popoValue = new TtAsWrLabouritemPO();
					popoValue.setCount(count1);
					balanceDao2.update(popo, popoValue);
				}
				//zhumingwei 2012-10-16 end
				
//				if(("1".equals(claimMap.get("IS_AUTO").toString()))||
//						("0".equals(claimMap.get("IS_AUTO").toString())&&
//								(Constant.CLA_TYPE_06.toString().equals(claimMap.get("CLAIM_TYPE").toString())||Constant.CLA_TYPE_02.toString().equals(claimMap.get("CLAIM_TYPE").toString())))){
//					
//					TtAsWrApplicationPO cb = new TtAsWrApplicationPO();
//					cb.setId(Long.valueOf(claimId));
//					TtAsWrApplicationPO cbValue = new TtAsWrApplicationPO();
//					cbValue.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_07);
//					cbValue.setAccountDate(new Date());
//					cbValue.setUpdateDate(new Date());
//					balanceDao2.update(cb, cbValue);
//					//记录索赔结算审核的授权记录
//					TtAsWrAppauthitemPO appAuthPO = new TtAsWrAppauthitemPO();
//					appAuthPO.setId(Long.parseLong(claimId));
//					appAuthPO.setApprovalPerson("自动审核"+" [结算审核]");//自动审核 审核人员 固定为100
//		//			appAuthPO.setApprovalLevelCode(role);//
//					appAuthPO.setApprovalDate(new Date());
//					appAuthPO.setApprovalResult(Constant.CLAIM_APPLY_ORD_TYPE_07.toString());//授权结果=索赔申请单状态
//					appAuthPO.setRemark("");//审核通过备注信息为空
//					appAuthPO.setCreateBy(new Long(-1));
//					appAuthPO.setCreateDate(new Date());
//					
//					auditingDao.insertClaimAppAuth(appAuthPO);
//				}else{
//					TtAsWrApplicationPO cb = new TtAsWrApplicationPO();
//					cb.setId(Long.valueOf(claimId));
//					TtAsWrApplicationPO cbValue = new TtAsWrApplicationPO();
//					cbValue.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_08);
//					cbValue.setAccountDate(new Date());
//					cbValue.setUpdateDate(new Date());
//					balanceDao2.update(cb, cbValue);
//					//记录索赔结算审核的授权记录
//					TtAsWrAppauthitemPO appAuthPO = new TtAsWrAppauthitemPO();
//					appAuthPO.setId(Long.parseLong(claimId));
//					appAuthPO.setApprovalPerson("自动审核"+" [结算审核]");//自动审核 审核人员 固定为100
//		//			appAuthPO.setApprovalLevelCode(role);//
//					appAuthPO.setApprovalDate(new Date());
//					appAuthPO.setApprovalResult(Constant.CLAIM_APPLY_ORD_TYPE_08.toString());//授权结果=索赔申请单状态
//					appAuthPO.setRemark("");//审核通过备注信息为空
//					appAuthPO.setCreateBy(new Long(-1));
//					appAuthPO.setCreateDate(new Date());
//					auditingDao.insertClaimAppAuth(appAuthPO);
//				}
				POContext.endTxn(true);
			}catch(Exception e){
				logger.error("审核错误KKKKKKKKKKKKKK:",e);
				e.printStackTrace();
				//POContext.endTxn(false);
				
			}finally{
				POContext.cleanTxn();
			}
		}
	}
}
