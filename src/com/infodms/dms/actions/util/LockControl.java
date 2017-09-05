package com.infodms.dms.actions.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class LockControl {
	public static List<Map<String,String>> lockData=new ArrayList<Map<String,String>>();
	/**
	 * 返回的数据是0正常执行
	 * 返回的数据是1说明有人操作
	 * @return
	 */
	public static synchronized String checkData(String billId,String sessionId,String userId){
		String flag="0";
		if(lockData!=null){
			for(int i=0;i<lockData.size();i++){
				 Map<String,String> m=lockData.get(i);
				 //表示已经加锁
				 if(billId.equals(m.get("billId"))){
					 flag="1";
					 break;
				 }
			 }
			//表示还没有加锁
			if("0".equals(flag)){
				Map<String,String> m=new HashMap<String,String>();
				m.put("billId", billId);
				m.put("sessionId", sessionId);
				m.put("userId", userId);
				//lockData.add(m);
			}
		}
		return flag;
	}
	/**
	 * 销毁锁定
	 * 如果billId有值就根据billId销毁
	 * 如果billId没有值就根据sessionId销毁
	 * 
	 * @param sessionId
	 */
	public static synchronized void destroyLock(String sessionId,String billId,String userId){
		
			for(int i=0;i<lockData.size();i++){
				Map<String,String> m=lockData.get(i);
				//如果传入的billId有效，就根据billId销毁数据
				if(sessionId!=null&&!"".equals(sessionId)&&billId!=null&&!"".equals(billId)){
					 if(sessionId.equals(m.get("sessionId"))){
						 if(billId.equals(m.get("billId"))){
							 lockData.remove(i);
						 }
					 }
				}else if(billId!=null&&!"".equals(billId)){
					 if(billId.equals(m.get("billId"))){
						 lockData.remove(i);
					 }
				}else if(userId!=null&&!"".equals(userId)){
					if(userId.equals(m.get("userId"))){
						 lockData.remove(i);
					 }
				}else if(sessionId!=null&&!"".equals(sessionId)){
					lockData.remove(i);
				}
			}
	}
	/**
	 * 销毁锁定
	 * 如果billId有值就根据billId销毁
	 * 如果billId没有值就根据sessionId销毁
	 * 
	 * @param sessionId
	 */
	public static synchronized void destroyLockByUser(String userId){
		
			for(int i=0;i<lockData.size();i++){
				Map<String,String> m=lockData.get(i);
				
				if(userId!=null&&!"".equals(userId)){
					 if(userId.equals(m.get("userId"))){
						 if(userId.equals(m.get("userId"))){
							 lockData.remove(i);
						 }
					 }
				}
			}
	}
}
