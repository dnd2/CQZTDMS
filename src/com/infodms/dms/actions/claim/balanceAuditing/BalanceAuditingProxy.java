package com.infodms.dms.actions.claim.balanceAuditing;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.infodms.dms.common.Utility;

/**
 * 自动审核代理类，负责创建审核线程,该类已经相关审核规则参数初始化
 * 通过ClaimAuditingComponent实现
 * @author XZM
 */
public class BalanceAuditingProxy {

	/** 线程池 */
	private static ThreadPoolExecutor executor = null;
	private static BlockingQueue<Runnable> threadQueue = null;
	/** 线程池数(默认为100) */
	private static int poolSize = 100;
	/** 队列上线数量（默认为1000） */
	private static int queueSize = 1000;
	/** 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间 */
	private static long keepAliveTime = 60*1000;
	
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
	 * 通知自动审核结算单
	 * @param claimId 索赔单ID
	 * @param balanceId 结算单ID
	 */
	public void auditing(String claimId,String balanceId){
		if(!Utility.testString(claimId))
			return;
		
		//BalanceAuditing cAuditing = new BalanceAuditing(claimId,balanceId);
		BalanceAuditingProxy proxy = new BalanceAuditingProxy();
		proxy.createThreadPool();
		
		//BalanceAuditingProxy.executor.execute(cAuditing);
	}
	
}
