package com.infoservice.dms.chana.service;

import com.infoservice.de.DEAdapter;
import com.infoservice.de.DEException;
import com.infoservice.de.DEMessage;
import com.infoservice.de.DEService;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.po3.core.util.ContextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

import org.apache.log4j.Logger;

public class DeUtility implements DEConstant {
	private static final Logger LOG = Logger.getLogger(DeUtility.class);
	private static DeCommonDao deCommonDao = DeCommonDao.getInstance();
	
	
	public DEMessage assembleDEMessage(String interfaceName, Map<String, Serializable> body) throws DEException {
		//创建消息对象实例
		DEMessage msg = new DEMessage();			
		msg.setAppName(APP_NAME);
		msg.setSource(SOURCE);

		msg.setPriority(5);
		msg.setVersion("v0.9.9");
		msg.setBizType(interfaceName);
		//head
		Map<String, Serializable> head = new HashMap<String, Serializable>();
		head.put("head", System.currentTimeMillis());
		msg.setHead(head);
		msg.setBody(body);
		return msg;
	}
	
	/**
	 * 
	* @Title: sendMsg 
	* @Description: TODO(给指定经销商发送异步消息) 
	* @param interfaceName 下端Action代号
	* @param @param entityCode
	* @param @param body
	* @param @throws Exception    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void sendMsg(String interfaceName, List<String> entityCodes, Map<String, Serializable> body)throws Exception{
		if (body.size() == 0) {
			return;
		}
		DEAdapter deAdapter = DEService.getInstance().getAdapter(INFOX_ADAPTER);
		DEMessage msg = assembleDEMessage(interfaceName, body);
		for (String entityCode : entityCodes) {
			try {
				msg.setDestination(entityCode);
				LOG.info(interfaceName + " Send to " + msg.getDestination());
				deAdapter.sendMsg(msg);
			} catch (Exception e) {
				LOG.error("发送消息失败 interfaceName == " + interfaceName + ", entityCode == " + entityCode);
			}
		}	
	}	
	
	/**
	 * 
	* @Title: sendAllMsg 
	* @Description: TODO(给所有的经销商节点发消息) 
	* @param @param interfaceName 下端Action代号
	* @param @param head
	* @param @param body
	* @param @throws Exception    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void sendAllMsg(String interfaceName, Map<String, Serializable> body)throws Exception {
		if (body.size() == 0) {
			return;
		}
		DEAdapter deAdapter = DEService.getInstance().getAdapter(INFOX_ADAPTER);
		DEMessage msg = assembleDEMessage(interfaceName, body);
		List<Map<String, Object>> dmsCodes = deCommonDao.getAllDmsCode();
		if (null == dmsCodes || dmsCodes.size() == 0) {
			throw new RpcException("经销商没有维护对应关系");
		}
		for (Map<String, Object> dmsCode : dmsCodes) {
			try {
				msg.setDestination(dmsCode.get("DMS_CODE").toString());
				deAdapter.sendMsg(msg);
				LOG.info(interfaceName + " Send to " + msg.getDestination());
			} catch (Exception e) {
				LOG.error("发送消息失败 interfaceName == " + interfaceName + ", entityCode == " + dmsCode.get("DMS_CODE").toString(), e);
			}
		}		
	}
	
	/**
	 * 
	* @Title: sendAMsg 
	* @Description: TODO(给指定经销商发送异步消息) 
	* @param interfaceName 下端Action代号
	* @param @param entityCode
	* @param @param body
	* @param @throws Exception    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void sendAMsg(String interfaceName, String entityCode, Map<String, Serializable> body)throws Exception{
		if (body.size() == 0) {
			return;
		}
		DEAdapter deAdapter = DEService.getInstance().getAdapter(INFOX_ADAPTER);
		DEMessage msg = assembleDEMessage(interfaceName, body);
		msg.setDestination(entityCode);
		LOG.info(interfaceName + " Send to " + msg.getDestination());
		deAdapter.sendMsg(msg);			
	}
	
	/**
	 * 
	 * 功能描述：GMS发送同步消息到dms
	 * 
	 * @param interfaceName
	 * @param entityCode
	 * @param Map
	 */
	public void sendSyncMsg(String interfaceName, String entityCode, Map<String, Serializable> body)throws Exception{
		try {			
			DEAdapter deAdapter = DEService.getInstance().getAdapter(INFOX_ADAPTER);
			DEMessage msg = assembleDEMessage(interfaceName, body);
			msg.setDestination(entityCode);
			deAdapter.sendSyncMsg(msg);			
		} catch (DEException e) {			
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	/**
	 * 
	* @Title: wrapperMsg 
	* @Description: TODO(组装VO错误消息) 
	* @param @param vo
	* @param @param errorMsg 错误消息字符串
	* @return BaseVO    返回类型 
	* @throws
	 */
	public static <T extends BaseVO> T wrapperMsg(T vo, String errorMsg) {
		LOG.error(errorMsg);
		vo.setErrorMsg(errorMsg);
		return vo;
	}
	
}
