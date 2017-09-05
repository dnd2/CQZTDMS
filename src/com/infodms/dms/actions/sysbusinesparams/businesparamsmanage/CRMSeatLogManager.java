package com.infodms.dms.actions.sysbusinesparams.businesparamsmanage;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;


import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sysbusinesparams.businesparamsmanage.CRMSeatLogDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class CRMSeatLogManager {

	public Logger logger = Logger.getLogger(CRMSeatLogManager.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	CRMSeatLogDAO dao = CRMSeatLogDAO.getInstance();
	private final String CRMSeatLogMainInit = "/jsp/sysbusinesparams/businesparamsmanage/CRMSeatLogMain.jsp";
	
	
	/**
	 * @FUNCTION : 登陆日志查询初始化
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public void CRMSeatLogMainInit(){
		try{
			// 获得坐席数据集
			List<Map<String,Object>> seats= dao.getSeats();
			act.setOutData("seats", seats);
			act.setForword(CRMSeatLogMainInit);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * @FUNCTION : 登陆日志查询
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public void getMainList(){
		//act.getResponse().setContentType("text/json;charset=utf-8");
		try{
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")) : 1;
//			String json = new String(request.getParamValue("json").getBytes("ISO8859-1"), "UTF-8");
			//String json = request.getParamValue("json");
//	        logger.info("---json"+json);
//	        JSONObject paraObject = JSONObject.fromObject(json);
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));
			String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate"));
			PageResult<Map<String, Object>> ps = dao.getMainList(id, checkSDate, checkEDate, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);   
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "杂项入库初始化错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}	
	
}
