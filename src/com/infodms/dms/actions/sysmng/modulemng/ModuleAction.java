package com.infodms.dms.actions.sysmng.modulemng;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.moduleMng.ModuleActionDAO;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;


public class ModuleAction {
	public Logger logger = Logger.getLogger(ModuleAction.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	ModuleActionDAO dao = ModuleActionDAO.getInstance();
	
	private final String ModuleActionInit = "/jsp/systemMng/moduleMng/moduleAction/ModuleActionMain.jsp";
	private final String ActionModifyInit = "/jsp/systemMng/moduleMng/moduleAction/ModuleActionModify.jsp";
	private final String ActionAddInit = "/jsp/systemMng/moduleMng/moduleAction/ModuleActionAdd.jsp";
	
	/**
	 * @FUNCTION : 模块操作初始化
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-09-02
	 */
	public void ModuleActionInit(){
		try{
			act.setForword(ModuleActionInit);
		}
		catch (Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}	

	/**
	 * @FUNCTION : 模块操作主页面
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-09-02
	 */
	public void getModuleActionList(){
		try{
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")) : 1;
			String json = new String(request.getParamValue("json").getBytes("ISO8859-1"), "UTF-8");
			JSONObject paraObject = JSONObject.fromObject(json);
			logger.info("--------paraObject=" + paraObject);
			PageResult<Map<String, Object>> ps = dao.getModuleActionList(paraObject, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);   
		}
		catch (Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "模块操作初始化错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}	

	/**
	 * @FUNCTION : 模块操作修改初始化
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-09-02
	 */
	public void ActionModifyInit(){
		try{
			String actionId = request.getParamValue("actionId");
			Map<String, Object> dataMap = dao.getViewData(actionId);
			act.setOutData("dataMap", dataMap);
			act.setForword(ActionModifyInit);
		}
		catch (Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @FUNCTION : 模块操作新增初始化
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-09-03
	 */
	public void ActionAddInit(){
		try{
			act.setForword(ActionAddInit);
		}
		catch (Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}	

	/**
	 * @FUNCTION : 模块操作保存
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-09-03
	 */
	public void ActionSave(){
		try {           			
			String json = new String(request.getParamValue("json").getBytes("ISO8859-1"), "UTF-8");			
			JSONObject dataObject = JSONObject.fromObject(json);
			logger.info("---------dataObject="+dataObject);
			dao.ActionSave(logonUser,dataObject);
			act.setOutData("success", "保存成功!");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"模块操作保存出错,请联系管理员!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}	
	
	
	
}
