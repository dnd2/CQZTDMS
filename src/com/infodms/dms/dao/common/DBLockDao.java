package com.infodms.dms.dao.common;

import java.sql.ResultSet;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("unchecked")
public class DBLockDao extends BaseDao {

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	/*
	 * 创建锁
	 * @param lockId
	 * @param businessType 
	 */
	public void createLock(String lockId,String businessType){
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO BUSINESSLOCK(LOCK_ID,BUSINESS_TYPE,CREATEDATE)\n" );
		sql.append("SELECT '"+lockId+"','"+businessType+"',SYSDATE\n" );
		sql.append("FROM DUAL\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND NOT EXISTS (SELECT B.LOCK_ID,B.BUSINESS_TYPE FROM BUSINESSLOCK B\n" );
		sql.append("WHERE B.LOCK_ID = '"+lockId+"'\n" );
		sql.append("AND B.BUSINESS_TYPE = '"+businessType+"')");

		this.update(sql.toString(), null);
	}
	
	/**
	 * 取得锁
	 * @param lockId
	 * @param businessType
	 */
	public void getLock(String lockId,String businessType){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT B.LOCK_ID,B.BUSINESS_TYPE FROM BUSINESSLOCK B\n" );
		sql.append("WHERE B.LOCK_ID = '"+lockId+"'\n" );
		sql.append("AND B.BUSINESS_TYPE = '"+businessType+"'\n");
		sql.append("FOR UPDATE NOWAIT");

		this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	/**
	 * 释放锁
	 * @param lockId
	 * @param businessType
	 */
	public void freeLock(String lockId,String businessType){
		StringBuilder sql= new StringBuilder();
		sql.append("DELETE FROM BUSINESSLOCK B\n" );
		sql.append("WHERE B.LOCK_ID = '"+lockId+"'\n" );
		sql.append("AND B.BUSINESS_TYPE = '"+businessType+"'\n");
		
		this.update(sql.toString(), null);
	}
}
