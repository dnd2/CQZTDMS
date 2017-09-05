/**********************************************************************
* <pre>
* FILE  : WebServiceComponent.java
* CLASS : WebServiceComponent
*
* AUTHOR : witti
*
* FUNCTION : webservice初始化类
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   | NAME  | REASON  | CHANGE REQ.
*----------------------------------------------------------------------
* 		  |2009-8-21 | witti | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: WebServiceComponent.java,v 1.2 2010/10/26 09:33:01 liuq Exp $
*/
package com.infodms.dms.common.component;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.infodms.dms.common.repairorder.edwclient.EdwWebService;
import com.infoservice.mvc.component.Component;

/**
 * Function：   webservice初始化类：
 * Author：     witti
 * Create_Date：2009-08-21
 * Version：    0.1
 */
public class WebServiceComponent implements Component{
	private static Logger logger = LogManager.getLogger(WebServiceComponent.class);
	
	public void init(Map<String,String> params){
		getResource(params.get("path"));
	}
	
	/**
	* Function：   webservice配置信息初始化
	* @param：     path, 配置文件路径
	* @throws:     Exception 
	* @return:     void
	* update_date: 2009-08-21
	*/
	private static void getResource(String path) {
		Properties props = new Properties();
		String username = null;
		String password = null;
		String url = null;
		InputStream in = null;
		try{
			in = new BufferedInputStream(new FileInputStream(path));
			props.load(in);
			if(in!=null) {
				username = props.getProperty("username");
				password = props.getProperty("password");
				url = props.getProperty("url");
				if(username!=null||password!=null||url!=null) {
					EdwWebService.uName = username;
					EdwWebService.uPwd = password;
					EdwWebService.setUrl(url);
				}
			}else {
				logger.info("获取的Webservice配置信息不正确");
			}
			props = null;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("获取Webservice配置信息时出现错误");
		}finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see com.infoservice.mvc.component.Component#destroy()
	 */
	public void destroy() {
	}
}
