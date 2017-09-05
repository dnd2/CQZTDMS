package com.infodms.dms.actions.config;

import java.net.URLDecoder;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.parts.storageManager.miscManager.MiscManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.config.ConfigDAO;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class ConfigManager {
	public Logger logger = Logger.getLogger(MiscManager.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	ConfigDAO dao = ConfigDAO.getInstance();
	
	/**
	 * @FUNCTION : 页面元素配置获取关联列表
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public void getRelatedList(){
		//act.getResponse().setContentType("text/json;charset=utf-8");
		try{
	        String orginJson =URLDecoder.decode(request.getParamValue("orginJson"),"UTF-8");
	        logger.info("---orginJson="+orginJson);
	        JSONObject orginObject = JSONObject.fromObject(orginJson);
	        PageResult<Map<String, Object>> returnJson = dao.getRelatedList(orginObject,10000,1);
			act.setOutData("returnJson", returnJson);   
		}
		catch (Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "杂项入库初始化错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}	
	
}
