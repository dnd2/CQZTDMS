package com.infodms.dms.actions.repairOrder;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.auditing.AutoAuditingDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsWrForeapprovalPO;
import com.infodms.dms.po.TtAsWrForeauthdetailPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.AbstractSendTask;

public class RoDealerDqvCheck extends AbstractSendTask{
	private static Logger logger = Logger.getLogger(RoDealerDqvCheck.class);
	
	private String dealerId;
	private Long  foreId;
	private Long roId;
	public RoDealerDqvCheck(String  dealerId,Long foreId,Long roId){
		this.dealerId = dealerId;
		this.foreId = foreId;
		this.roId = roId;
	}
	AutoAuditingDao dao = AutoAuditingDao.getInstance();
	@Override
	protected String handleExecute() throws Exception {
		logger.info("是否为DQV认证经销商自动审核开始======>");
		//加入同步
		boolean isDeal = DBLockUtil.lock("10018", DBLockUtil.BUSINESS_TYPE_18);
		try{
			if(isDeal){ 
				if(!autoAuditing()){//如果不是认证经销商,则需要下一步【审核规则】验证
					RoRuleCheck ck = new RoRuleCheck(this.foreId,this.roId);
					ck.execute();
				}
			}else{
				System.out.println("[100018]同步放弃本次执行!");
			}
		}catch(Exception e){
		}finally{
			DBLockUtil.freeLock("10018", DBLockUtil.BUSINESS_TYPE_18);
		}
		logger.info("是否为DQV认证经销商自动审核结束<=====");
		return "";
	}
	@SuppressWarnings({ "unchecked" })
	public boolean  autoAuditing() throws Exception{
		
			TmDealerPO dp = new TmDealerPO();
			dp.setDealerId(Long.valueOf(this.dealerId));
			dp.setIsDqv(Constant.IF_TYPE_YES);
			List<TmDealerPO> list = dao.select(dp);
			System.out.println("------------come in 2---------");
		if(list!=null&&list.size()>0&&this.foreId!=null){
			TtAsWrForeapprovalPO fp = new TtAsWrForeapprovalPO();
			TtAsWrForeapprovalPO fp2 = new TtAsWrForeapprovalPO();
			if(this.foreId==null) throw new Exception("不能为空");
			fp.setId(this.foreId);
			fp2.setReportStatus(Constant.RO_FORE_02);
			fp2.setUpdateBy(-1L);
			fp2.setAuditPerson(-1L);//将审核人设置为默认的自动审核
			fp2.setUpdateDate(new Date());
			fp2.setOpinion("DQV认证经销商,直接通过");
			dao.update(fp, fp2);
			fp = (TtAsWrForeapprovalPO) dao.select(fp).get(0);
			dao.updateOrder(fp.getRoNo());
			
			//插入 预授权审核明细表
			TtAsWrForeauthdetailPO afp = new TtAsWrForeauthdetailPO();
			afp.setId(Utility.getLong(SequenceManager.getSequence("")));
			afp.setFid(Long.valueOf(this.foreId));
			afp.setApprovalLevelCode("0000");
			afp.setAuditDate(new Date()	);
			afp.setAuditPerson("自动审核");
			afp.setAuditLevelNmae("自动审核");
			afp.setAuditResult(Constant.RO_FORE_02.toString());
			afp.setRemark("DQV认证经销商,直接通过");
			afp.setCreateDate(new Date());
			afp.setCreateBy(-1L);
			dao.insert(afp);
			return true;
			}else{
				return false;
			}
	}
}
