package com.infodms.dms.actions.sysbusinesparams.funcparameter;




import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;


import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sysbusinesparams.funcparameter.FuncParameterDAO;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class FuncParameterManager {

	public Logger logger = Logger.getLogger(FuncParameterManager.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private static final FuncParameterDAO dao = new FuncParameterDAO ();
	private final String funParameterMainInit = "/jsp/sysbusinesparams/funcparameter/funParameterMain.jsp";
	private final String funParameterAdd = "/jsp/sysbusinesparams/funcparameter/funcParameterAdd.jsp";
	
	/**
	 * @FUNCTION : 参数配置页面初始化
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-6-28
	 */
	public void funcParameterManageInit(){
		try {
			act.setForword(funParameterMainInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "模块参数维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * @FUNCTION : 获取参数列表
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-6-28
	 */	
	public void getParaList(){
		try {
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")) : 1;
			String json = new String(request.getParamValue("json").getBytes("ISO8859-1"), "UTF-8");
	        JSONObject paraObject = JSONObject.fromObject(json);			
			PageResult<Map<String, Object>> ps = dao.getParaList(paraObject, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "模块参数维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * @FUNCTION : 新增参数
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-6-28
	 */	
	public void addParameter(){
		try {
			act.setForword(funParameterAdd);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "模块参数维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}	
	
	
}
