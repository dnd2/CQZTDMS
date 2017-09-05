package com.infodms.dms.actions.zotye.service.baseInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.dao.zotye.service.baseInfo.FirstMaintenanceDAO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class FirstMaintenanceAction {
	public Logger logger = Logger.getLogger(FirstMaintenanceAction.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest() ;
	private FirstMaintenanceDAO dao = FirstMaintenanceDAO.getInstance() ;
	
	
	private final String FIRST_MAINTENACE_INIT = "/jsp/zotye/service/baseInfo/firstMaintenanceInit.jsp" ;
	private final String FIRST_MAINTENACE_ADD_INIT = "/jsp/zotye/service/baseInfo/firstMaintenanceAddInit.jsp" ;
	private final String FIRST_MAINTENACE_UPDATE_INIT = "/jsp/zotye/service/baseInfo/firstMaintenanceUpdateInit.jsp" ;
	
	public void firstMaintenaceInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			
			act.setForword(FIRST_MAINTENACE_INIT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"首保信息维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void firstMaintenaceAddInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			
			act.setForword(FIRST_MAINTENACE_ADD_INIT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"首保信息新增页面跳转");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void firstMaintenaceUpdateInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("id")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("id", id) ;
			
			List<Map<String, Object>> list = dao.firstMainQuery(map) ;
			
			act.setOutData("id", id);
			act.setOutData("mainMile", list.get(0).get("END_MILEAGE"));
			act.setOutData("mainDate", list.get(0).get("MAX_DAYS"));
			
			act.setForword(FIRST_MAINTENACE_UPDATE_INIT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"首保信息新增页面跳转");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void firstMaintenaceQuery() {
		AclUserBean logonUser = null ;
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.query(null, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"首保信息查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void firstMaintenaceInsert() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String textMainMile = CommonUtils.checkNull(request.getParamValue("textMainMile")) ;
			String textMainDate = CommonUtils.checkNull(request.getParamValue("textMainDate")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("textMainMile", textMainMile) ;
			map.put("textMainDate", textMainDate) ;
			map.put("userId", logonUser.getUserId().toString()) ;
			
			dao.insert(map) ;
			
			act.setOutData("subFlag", "success") ;
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"首保信息新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void firstMaintenaceUpdate() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("id")) ;
			String textMainMile = CommonUtils.checkNull(request.getParamValue("textMainMile")) ;
			String textMainDate = CommonUtils.checkNull(request.getParamValue("textMainDate")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("id", id) ;
			map.put("textMainMile", textMainMile) ;
			map.put("textMainDate", textMainDate) ;
			map.put("userId", logonUser.getUserId().toString()) ;
			
			dao.update(map) ;
			
			act.setOutData("subFlag", "success") ;
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"首保信息新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
