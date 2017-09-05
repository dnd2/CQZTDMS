/**********************************************************************
* <pre>
* FILE : MessageComponent1.java
* CLASS : MessageComponent1
*
* AUTHOR : xianchao zhang
*
* FUNCTION : TODO
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
* 		  |2009-8-19| xianchao zhang| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: MessageComponent.java,v 1.1 2010/08/16 01:44:46 yuch Exp $
*/
package com.infodms.dms.common.component.message;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.component.Component;

/**
 * 功能说明：生成错误信息的管理类，可以加载维护好的错误
 * 典型用法：
 * 示例程序如下：
 * 特殊用法：
 * 创建者：xianchao zhang
 * 创建时间：2009-8-19
 * 修改人：
 * 修改时间：
 * 修改原因：
 * 修改内容：
 * 版本：0.1
 */
public class MessageComponent implements Component{
	private static Logger logger = LogManager.getLogger(MessageComponent.class);
	private static MessageComponent singleton = new MessageComponent();
	
	private static HashMap<String,String> localeMapping = new HashMap<String,String>();
	
	public void init(Map<String,String> params){
		for (String val : params.values() ){
			this.loadResource(val);
		}
	}
	/**
	 * 获得MessageComment的实例
	* 功能说明：
	* @return MessageComponent
	* 最后修改时间：2009-8-19
	 */
	public static MessageComponent getInstance() {
		return singleton;
	}
	
	public void loadResource(String resName) {
		ResourceBundle rb=null;
		String file =null;
		try{
			file = resName+".properties";
			InputStream in =this.getClass().getResourceAsStream("/"+file);

			if ( in!=null ){
				rb = PropertyResourceBundle.getBundle(resName);
				logger.info("find  "+file);
			}else{
				logger.warn("Can't find  "+file);
				return ;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Enumeration<String> enu = rb.getKeys();
		String key = null;
		while ( enu.hasMoreElements() ){
			key = enu.nextElement();
			if ( localeMapping.containsKey(key) ){
				logger.warn("Message key was duplicated : "+key);
			}else{
				localeMapping.put(key, rb.getString(key));
			}
		}
	}
	
	private String getMessageInfo(String key,Object[] objs) throws BizException,Exception{
		try{
			String msg = null;
			if ( msg==null ){
				msg = localeMapping.get(key);
			}		
			if ( msg!=null){
				if ( objs==null || objs.length==0 ){
					return msg;
				}else{
					for (Object obj : objs) {
						if(obj!=null){
							//异常信息提示多次2016-4-21注释
							//msg += ":" + obj.toString();
						}
					}
					return MessageFormat.format(msg,objs);
				}
			}else{
				throw new BizException(ErrorCodeConstant.FIND_ERROR_CODE_MESSAGE,key);
			}
		}catch(BizException e){
			throw e;
		}catch(Exception e){
			throw e;
		}
	}
	
	
	//----------------------------------------
	public String getMessage(String key) throws BizException,Exception {
		return this.getMessageInfo(key,null);
	}
	
	public String getMessage(String key, Object... params) throws BizException,Exception{
		return this.getMessageInfo(key, params);
	}

	/* (non-Javadoc)
	 * @see com.infoservice.mvc.component.Component#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		localeMapping.clear();
		singleton = null;
		localeMapping = null;
	}
}
