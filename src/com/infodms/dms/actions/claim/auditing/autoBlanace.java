package com.infodms.dms.actions.claim.auditing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.application.BalanceStatusRecord;
import com.infodms.dms.actions.claim.application.DealerNewKp;
import com.infodms.dms.actions.claim.balanceAuditing.BalanceAuditing;
import com.infodms.dms.actions.claim.balanceAuditing.BalanceAuditingNew;
import com.infodms.dms.actions.claim.balanceAuditing.ClaimAuditingNew;
import com.infodms.dms.actions.claim.balanceAuditing.ClaimAuditingNew2;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.dao.claim.application.DealerBalanceDao;
import com.infodms.dms.dao.claim.application.DealerNewKpUpdateDAO;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.dao.claim.auditing.autoBlanaceDao;
import com.infodms.dms.po.TrGatherBalancePO;
import com.infodms.dms.po.TtAsDealerTypePO;
import com.infodms.dms.po.TtAsWrAppauthitemPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrGatherBalancePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.dms.chana.actions.AbstractSendTask;
import com.infoservice.po3.core.context.POContext;

/**
 * 结算单定时
 * @author Administrator
 *
 */
public class autoBlanace extends AbstractSendTask {

	private static Logger logger = Logger.getLogger(autoBlanace.class);
	private final autoBlanaceDao daoKP = autoBlanaceDao.getInstance();
	@SuppressWarnings("unchecked")
	@Override
	protected synchronized String handleExecute() throws Exception {
		logger.info("autoBlanace[START]======>");
		//加入同步
		boolean isDeal = DBLockUtil.lock("108001", DBLockUtil.BUSINESS_TYPE_15);
		try{
			if(isDeal){	
				autoAuditing();}
			else{
				System.out.println("[108001]同步放弃本次执行!");
			}
		}catch(Exception e){
			logger.info("autoBlanace[END]======>");
	    	e.printStackTrace();
		}finally{
			DBLockUtil.freeLock("108001", DBLockUtil.BUSINESS_TYPE_15);
			try{
	            POContext.endTxn(true);		
			}catch(Exception e){
			}finally{
				POContext.cleanTxn();
			}
		}

			
		

		logger.info("autoBlanace[END]======>");
		return "";
	}
	
	/**
	 * 结算单自动审核
	 * @param balanceId
	 */
	public void autoAuditing() throws Exception{
		TtAsDealerTypePO po = new TtAsDealerTypePO();
	    List<TtAsDealerTypePO> listPo = daoKP.viewDealerType();
	    try{
	    	if(listPo!=null&&listPo.size()>0){
			    for(int i=0;i<listPo.size();i++){
			    	try{
			    	//首先获取该经销的开票起始和截止时间
			    	po = daoKP.showDateAutoBlanace(listPo.get(i).getYieldly().toString(), listPo.get(i).getDealerId().toString());
			    	//判断该经销商是否有金额可以开票
			    	Boolean bo = daoKP.countDealerKp(listPo.get(i).getDealerId(), listPo.get(i).getYieldly(), po);
			    	//如果有需开票的单据 就进入自动生成开票单逻辑
			    	if(bo){
			    		//将开票单信息统计并插入临时表当确定生成时在执行
			    		daoKP.autoBalanceView(po, listPo.get(i).getYieldly().toString(), listPo.get(i).getDealerId().toString());
			    	}
			    	daoKP.updateIsNotice(listPo.get(i));
			    	}catch(Exception e){
				    	logger.info("autoBlanace[END]======>");
				    	e.printStackTrace();
				    }finally{
						try{
				            POContext.endTxn(true);		
						}catch(Exception e){
						}finally{
							POContext.cleanTxn();
						}
					}
			    }
	    	}
	    }catch(Exception e){
	    	logger.info("autoBlanace[END]======>");
	    	e.printStackTrace();
	    }finally{
			try{
	            POContext.endTxn(true);		
			}catch(Exception e){
			}finally{
				POContext.cleanTxn();
			}
		}
	    
	}
	
	
	
}
