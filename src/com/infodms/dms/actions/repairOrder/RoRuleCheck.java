package com.infodms.dms.actions.repairOrder;

import org.apache.log4j.Logger;

import com.infodms.dms.common.DBLockUtil;
import com.infoservice.dms.chana.actions.AbstractSendTask;

public class RoRuleCheck extends AbstractSendTask{
	private static Logger logger = Logger.getLogger(RoRuleCheck.class);
	
	private Long  foreId;
	private Long roId;
	public RoRuleCheck(Long  foreId,Long roId){
		this.foreId = foreId;
		this.roId=roId;
	}
	@Override
	protected String handleExecute() throws Exception {
		logger.info("自动审核规则验证开始============>");
		//加入同步
		boolean isDeal = DBLockUtil.lock("10019", DBLockUtil.BUSINESS_TYPE_19);
		try{
			if(isDeal){ 
				autoAuditing();
			}else{
				System.out.println("[100019]同步放弃本次执行!");
			}
		}catch(Exception e){
		}finally{
			DBLockUtil.freeLock("10019", DBLockUtil.BUSINESS_TYPE_19);
		}
		logger.info("自动审核规则验证结束<=====");
		return "";
	}
public void autoAuditing() throws Exception{
		RoRuleAudit ba = new RoRuleAudit(this.foreId,this.roId);
		ba.run();
		}
}
