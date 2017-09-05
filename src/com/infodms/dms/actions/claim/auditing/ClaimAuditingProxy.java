package com.infodms.dms.actions.claim.auditing;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrApplicationPO;

/**
 * 自动审核代理类，负责创建审核线程,该类已经相关审核规则参数初始化
 * 通过ClaimAuditingComponent实现
 * @author XZM
 */
public class ClaimAuditingProxy {
	
	private Logger logger = Logger.getLogger(ClaimAuditingProxy.class);
	
	/** 线程池 */
	private static ThreadPoolExecutor executor = null;
	private static BlockingQueue<Runnable> threadQueue = null;
	/** 线程池数(默认为100) */
	private static int poolSize = 100;
	/** 队列上线数量（默认为1000） */
	private static int queueSize = 1000;
	/** 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间 */
	private static long keepAliveTime = 30*1000;
	
	/**
	 * 创建线程池
	 */
	private void createThreadPool(){
		if(executor==null){
			threadQueue = new LinkedBlockingQueue<Runnable>(queueSize);
			executor = new ThreadPoolExecutor(poolSize,poolSize,keepAliveTime,TimeUnit.SECONDS,threadQueue);
		}
	}
	
	/**
	 * 初始化参数，以组件方式启动,加载配置参数，同时加载未自动审核参数
	 * @param poolSize 线程池大小
	 * @param queueSize 队列大小
	 * @param keepAliveTime 等待时间
	 */
	public void init(Integer poolSize,Integer queueSize,Long keepAliveTime){
        
		logger.info("索赔申请单自动审核初始化开始==>");
		if(poolSize!=null){
			ClaimAuditingProxy.poolSize = poolSize.intValue();
		}
		if(queueSize!=null){
			ClaimAuditingProxy.queueSize = queueSize.intValue();
		}
		if(keepAliveTime!=null){
			ClaimAuditingProxy.keepAliveTime = keepAliveTime.longValue();
		}
		this.createThreadPool();
		
		//this.loadReportedClaim();
		
		logger.info("索赔申请单自动审核初始化结束==<");
	}
	
	/**
	 * 查询已上报状态的索赔申请单信息，并加入索赔处理序列中
	 */
	protected void loadReportedClaim(){
		
		if(ClaimAuditingProxy.executor == null || ClaimAuditingProxy.threadQueue ==null)
			return;
		
		//ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		//List<TtAsWrApplicationPO> claimList = auditingDao.queryReportedClaim(Constant.CLAIM_APPLY_ORD_TYPE_02,1);
		
		//if(claimList!=null){
		//	for (TtAsWrApplicationPO po : claimList) {
		//		TtAsWrApplicationPO poTemp = (TtAsWrApplicationPO)po;
		//		this.auditing(poTemp.getId().toString());
		//	}
		//}
	}
	
	/**
	 * 通知自动审核索赔申请单
	 * @param claimId 索赔申请单ID
	 */
	public void auditing(String claimId){
		System.out.println("上报["+claimId);
		if(!Utility.testString(claimId))
			return;
		
	//	ClaimAuditing cAuditing = new ClaimAuditing(claimId);
		ClaimAuditingProxy proxy = new ClaimAuditingProxy();
		proxy.createThreadPool();
		
		//ClaimAuditingProxy.executor.execute(cAuditing);
	}
	
}
