package com.infodms.dms.common;

import com.infodms.dms.dao.common.DBLockDao;
import com.infoservice.po3.core.context.POContext;

/**
 * 在数据库中建立全局锁
 * 例：
 *    boolean isDeal = DBLockUtil.lock(lockId,businessType); 注意：businessType保证业务不同不重复
 *    if(isDeal){
 *        //取得锁处理方法
 *    }else{
 *        //未取的锁处理方法
 *    }
 *    DBLockUtil.freeLock(lockId,businessType);
 */
public class DBLockUtil {
	
	/** 结算单审核(全部审核) */
	public static String BUSINESS_TYPE_01 = "1000";
	/** 经销商端结算 */
	public static String BUSINESS_TYPE_02 = "1001";
	/** 经销商端创建物流单 */
	public static String BUSINESS_TYPE_03 = "1002";
	/** 车厂端签收物流单 */
	public static String BUSINESS_TYPE_04 = "1003";
	/** 生成回运清单同步 */
	public static String BUSINESS_TYPE_05 = "1004";
	/** 装箱同步 */
	public static String BUSINESS_TYPE_21 = "1021";
	/** 补录同步 */
	public static String BUSINESS_TYPE_22 = "1022";
	/** 生成旧件回运物流单同步 */
	public static String BUSINESS_TYPE_06 = "1005";
	/** 旧件抵扣结算 同步 */
	public static String BUSINESS_TYPE_07 = "1006";
	/** 转复合申请审核 同步 */
	public static String BUSINESS_TYPE_08 = "1007";
	/** 生成开票通知单 同步 */
	public static String BUSINESS_TYPE_09 = "1008";
	/** 转行政扣款单 同步 */
	public static String BUSINESS_TYPE_10 = "1009";
	/** 索赔单自动审核 同步 */
	public static String BUSINESS_TYPE_11 = "1010";
	/** 结算单自动审核 同步 */
	public static String BUSINESS_TYPE_12 = "1011";
	/** 生成 同步 */
	public static String BUSINESS_TYPE_13 = "1012";
	/*自动开票*/
	public static String BUSINESS_TYPE_15 = "1014";
	
	/** 防止应税清单汇总报表签收收票时重复提交 */
	public static String BUSINESS_TYPE_14 = "1013";
	
	/** 防止旧件抵扣的时候重复提交 */
	public static String BUSINESS_TYPE_16 = "1015";
	
	//一天一车 自动审核同步
	public static String  BUSINESS_TYPE_17 ="1017";
	// 是否QDV 认证经销商 自动审核同步
	public static String  BUSINESS_TYPE_18 ="1018";
	//预授权自动审核规则审核同步
	public static String  BUSINESS_TYPE_19 ="1019";
	public static String  BUSINESS_TYPE_20 ="1020";//取得索赔单号时,控制不能同时获取同一经销商的同一类型的单号
	/**
	 * 取的数据库锁
	 * 注：该方法主动提交数据库事物，使用时最好调用前没有其他数据库操作
	 * @param lockId 锁标识
	 * @param businessType 业务类型 @see DBLockUtil.BTYPE_XX;
	 * @return boolean true:成功获得锁  false:资源正忙
	 * @throws Exception 
	 */
	public static boolean lock(String lockId,String businessType){
		
		boolean isLock = true;
		if(!Utility.testString(lockId) || !Utility.testString(businessType)){
			isLock = false;
			return isLock;
		}
		try{
			DBLockDao lockDao = new DBLockDao();
			//创建锁
			lockDao.createLock(lockId, businessType);
			//先提交事务，防止锁定其他使用锁的业务
			try{
	            POContext.endTxn(true);		
			}catch(Exception e){
			}finally{
				POContext.cleanTxn();
			}
			//取得锁
			lockDao.getLock(lockId, businessType);
		}catch(Exception e){
			isLock = false;
		}
		return isLock;
	}
	
	/**
	 * 释放锁
	 * @param lockId 锁标识
	 * @param businessType 业务类型
	 */
	public static void freeLock(String lockId,String businessType) {
		
		if(!Utility.testString(lockId) || !Utility.testString(businessType)){
			return;
		}
		try{
		
			DBLockDao lockDao = new DBLockDao();
			//取得锁
			lockDao.getLock(lockId, businessType);
			//释放锁
			lockDao.freeLock(lockId, businessType);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}
