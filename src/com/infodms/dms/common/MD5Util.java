package com.infodms.dms.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.po.TcUserPasswordPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;

public class MD5Util {
	public static Logger logger = Logger.getLogger(MD5Util.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	/**
	 * MD5加密    
	 * @param password : 要加密的密码
	 * @return
	 */
    public static String MD5Encryption(String password) {   
        StringBuffer hexString = new StringBuffer();   
        if (password != null && password.trim().length() != 0) {   
            try {   
                MessageDigest md = MessageDigest.getInstance("MD5");   
                md.update(password.getBytes());   
                byte[] hash = md.digest();   
                for (int i = 0; i < hash.length; i++) {   
                    if ((0xff & hash[i]) < 0x10) {   
                        hexString.append("0" + Integer.toHexString((0xFF & hash[i])));   
                    } else {   
                        hexString.append(Integer.toHexString(0xFF & hash[i]));   
                    }   
                }   
            } catch (NoSuchAlgorithmException e) {   
                e.printStackTrace();   
            }   
  
        }   
        return hexString.toString();   
    } 
    
    /** 
     * MD5校验 新密码与老密码是否相同
     * @param password 要校验的密码 
     * @param md5PwdStr 已加密过的的MD5密码
     * @return 
     */  
    public static boolean checkPassword(String password, Long userId) {  
    	//取得用户当前密码信息
    	TcUserPasswordPO passwordPO = new TcUserPasswordPO();
    	passwordPO.setUserId(userId);
    	List<TcUserPasswordPO> list = factory.select(passwordPO);
    	if(list.size() > 0){
    		passwordPO = list.get(0);
    		String s = MD5Encryption(password);  
    	    return s.equals(passwordPO.getNowPassword());
    	}else{
    		return false;
    	}
    }  
    
    /** 
     * MD5校验 近期三次密码是否有相同
     * @param password 要校验的密码 
     * @param md5PwdStr 已加密过的的MD5密码
     * @return 
     */  
    public static boolean checkModifyPassword(String newPassword, Long passId) {  
    	Boolean checkStatus = false;
    	//取得用户当前密码信息
    	TcUserPasswordPO passwordPO = new TcUserPasswordPO();
    	passwordPO.setPassId(passId);
    	passwordPO = factory.select(passwordPO).get(0);
    	
    	String s = MD5Encryption(newPassword); 
    	
		//验证新密码与当前密码是否相同
		checkStatus = s.equals(passwordPO.getNowPassword());
		//如果相同，刚返回true
		if(checkStatus){
			return checkStatus;
		}else {
			//验证新密码与老密码是否相同
			checkStatus = s.equals(passwordPO.getOldPassword());
			//如果相同，刚返回true
			if(checkStatus){
    			return checkStatus;
    		}
			return checkStatus;
		}
    }  
    
    public static void main(String[] args) {
		System.out.println(MD5Encryption("abc123"));
	}

}
