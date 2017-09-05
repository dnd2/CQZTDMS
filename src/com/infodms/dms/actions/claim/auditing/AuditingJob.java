package com.infodms.dms.actions.claim.auditing;

import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infoservice.dms.chana.actions.AbstractSendTask;
import com.infoservice.po3.core.context.POContext;

/**
 * 索赔单审核任务处理，防止由于各种中断照成
 * 索赔单状态保存在中间状态("已上报")
 * @author Administrator
 */
public class AuditingJob extends AbstractSendTask {

	private static Logger logger = Logger.getLogger(AuditingJob.class);

	@Override
	protected String handleExecute() throws Exception {
		logger.info("AuditingJob[START]======>");
		//加入同步
		boolean isDeal = DBLockUtil.lock("10000", DBLockUtil.BUSINESS_TYPE_11);
		try{
			if(isDeal){
				ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
				List<TtAsWrApplicationPO> claimList = auditingDao.queryReportedClaim(Constant.CLAIM_APPLY_ORD_TYPE_02,1);
				
				if(claimList!=null){
					for (TtAsWrApplicationPO po : claimList) {
						ClaimAuditing ca = new ClaimAuditing(po.getId().toString());
						ca.run();
					}
				}
			}else{
				System.out.println("[10000]同步放弃本次执行!");
			}
		}catch(Exception e){
		}finally{
			try{
	            POContext.endTxn(true);		
			}catch(Exception e){
			}finally{
				POContext.cleanTxn();
			}
		}
		logger.info("AuditingJob[END]======>");
		return "";
	}

}
