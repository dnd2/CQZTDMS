package com.infodms.dms.actions.claim.auditing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.application.BalanceStatusRecord;
import com.infodms.dms.actions.claim.balanceAuditing.BalanceAuditing;
import com.infodms.dms.actions.claim.balanceAuditing.BalanceAuditingNew;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.dao.claim.application.DealerBalanceDao;
import com.infodms.dms.po.TrGatherBalancePO;
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
public class BalanceAuditingJob extends AbstractSendTask {

	private static Logger logger = Logger.getLogger(BalanceAuditingJob.class);
	@SuppressWarnings("unchecked")
	@Override
	protected synchronized String handleExecute() throws Exception {
		logger.info("BalanceAuditingJob[START]======>");
		//加入同步
		boolean isDeal = DBLockUtil.lock("10001", DBLockUtil.BUSINESS_TYPE_12);
		try{
			if(isDeal){ 
				
					DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
					//0、查询已签收结算单
					TtAsWrGatherBalancePO bPO = new TtAsWrGatherBalancePO();
					bPO.setStatus(Constant.BALANCE_GATHER_TYPE_03);//已签收
					List<TtAsWrGatherBalancePO> needAuditingList =balanceDao.select(bPO);
					
					
					/******add by liuxh 201125 提交事务*****/
					try{
						POContext.endTxn(true);
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						POContext.cleanTxn();
					}
					/******add by liuxh 201125 提交事务*****/
					
					if(needAuditingList!=null && needAuditingList.size()>0){
						for (TtAsWrGatherBalancePO ttAsWrGatherBalancePO : needAuditingList) {
							try{
																
								Long orderId = ttAsWrGatherBalancePO.getId();

								//2、查询结算汇总单对应结算单并自动审核
								TrGatherBalancePO trgbPO = new TrGatherBalancePO();
								trgbPO.setGatherId(orderId);
								List<TrGatherBalancePO> trGatherBalanceList = balanceDao.select(trgbPO);
								if(trGatherBalanceList!=null && trGatherBalanceList.size()>0){
									for (TrGatherBalancePO trGatherBalancePO : trGatherBalanceList) {
										long balanceId=trGatherBalancePO.getBalanceId();
										//1、将结算单状态修改为"结算审核中"
										TtAsWrClaimBalancePO balanceConPO = new TtAsWrClaimBalancePO();
										TtAsWrClaimBalancePO targetPO = new TtAsWrClaimBalancePO();
										balanceConPO.setId(trGatherBalancePO.getBalanceId());
										//11、检测对应结算单是否已经做过收单动作
										List<TtAsWrClaimBalancePO> balanceList = balanceDao.select(balanceConPO);
										if(balanceList!=null && balanceList.size()>0){
											TtAsWrClaimBalancePO balancePO = balanceList.get(0);
											if(Constant.ACC_STATUS_06.equals(CommonUtils.checkNull(balancePO.getStatus()))){
												//2、结算单审核
												this.autoAuditing(trGatherBalancePO.getBalanceId());
												/*****add by liuxh 20101212 将事务改变到索赔单后单独修改结算单审核标志******/	
												long claimCount=balanceDao.getClaimCount(trGatherBalancePO.getBalanceId()); //本结算单下索赔单是否全部审核完成
												/*****add by 20110216 20101212 去掉此限制******/	
											if(claimCount<=0) //没有未审核的索赔单
												{
//							 						targetPO.setStatus(Integer.parseInt(Constant.ACC_STATUS_01));
//													balanceDao.update(balanceConPO, targetPO);
													DealerBalanceDao balanceDao2 = DealerBalanceDao.getInstance();
													String sql="UPDATE TT_AS_WR_CLAIM_BALANCE SET STATUS=? WHERE ID=?";
													List list2=new ArrayList();
													list2.add(Integer.parseInt(Constant.ACC_STATUS_01));
													list2.add(balanceId);
													balanceDao2.update(sql, list2);
													//12、记录审核结果
													BalanceStatusRecord.recordStatus(balanceId, -1L, "自动审核", -1L, BalanceStatusRecord.STATUS_03);
													
												}
													/*****add by xiongchuan 20110216 去掉此限制******/	
												try{
													POContext.endTxn(true);  //提交结算单事务
												}catch(Exception e){
													POContext.endTxn(false);
													e.printStackTrace();
												}finally{
													POContext.cleanTxn();
												}
												/*****add by liuxh 20101212 将事务改变到索赔单后单独修改结算单审核标志******/	
											}
										}
										logger.info("当前正在审核的结算单$$$$$$$$$$$$$$$$$$$$$$$$$$结算单ID:"+trGatherBalancePO.getBalanceId());
									}
								}
								/******add by liuxh 20101212 根据汇总单下的所有结算单自动审核完成后修改汇总单的状态为已审核******/
								long balancerCount=balanceDao.getGatherCount(orderId);//本汇总单下所有结算单是否完全审核完成
								long balCount = balanceDao.getBalCount(orderId);//判断该汇总单下是否所有的结算单状态都在审核中
								if(balCount>0){//如果结算单状态不对强制更新为结算单待收单  add xiongchuan 20110221
									balanceDao.updateBalanceStatus(orderId);
								}
								if(balancerCount<=0&&balCount<=0)//没有未审核的结算单
								{
									//1、修改结算汇总单状态
									TtAsWrGatherBalancePO conditionPO = new TtAsWrGatherBalancePO();
									conditionPO.setId(orderId);
									TtAsWrGatherBalancePO gatherPO = new TtAsWrGatherBalancePO();
									gatherPO.setStatus(Constant.BALANCE_GATHER_TYPE_04);//已审核
									gatherPO.setUpdateDate(new Date());
									balanceDao.update(conditionPO, gatherPO);
									
									
								}
								/******add by liuxh 20101212 根据汇总单下的所有结算单自动审核完成后修改汇总单的状态为已审核******/
								POContext.endTxn(true);//提交汇总单事务
							}catch(Exception e){
								logger.error("定时任务审核结算汇总单发生错误,结算汇总单ID:"+ttAsWrGatherBalancePO.getId());
								logger.error("错错错了了了了");
								logger.error("333333:",e);
								e.printStackTrace();
								POContext.endTxn(false);
							}finally{
								POContext.cleanTxn();
							}
							////////////////////
						}
					}
			}else{
				System.out.println("[100001]同步放弃本次执行!");
			}
		}catch(Exception e){
		}finally{
		}

		logger.info("BalanceAuditingJob[END]======>");
		return "";
	}
	
	/**
	 * 结算单自动审核
	 * @param balanceId
	 */
	public void autoAuditing(Long balanceId) throws Exception{
		
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		
		String balanceIdStr = balanceId.toString();
		//查询结算单对应索赔单
		List<Map<String,Object>> claimList = balanceDao.queryClaimByBalanceId(balanceId);
		//循环审核每条索赔单
		int count=0;
		for (Map<String, Object> claimMap : claimList) {
			count++;
			String claimId = claimMap.get("CLAIM_ID").toString();
			logger.info("号号号号号第"+count+"+条+"+"="+claimId);
			BalanceAuditingNew ba = new BalanceAuditingNew(claimId,balanceIdStr);
			ba.run();
		}
	}
}
