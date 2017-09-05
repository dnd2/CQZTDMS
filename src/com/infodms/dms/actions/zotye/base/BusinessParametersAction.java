package com.infodms.dms.actions.zotye.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.dao.zotye.base.BusinessParametersDAO;
import com.infodms.dms.dao.zotye.service.baseInfo.FirstMaintenanceDAO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class BusinessParametersAction {
	public Logger logger = Logger.getLogger(BusinessParametersAction.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest() ;
	private BusinessParametersDAO dao = BusinessParametersDAO.getInstance() ;
	
	
	private final String BUSINESS_PARAMETERS_INIT = "/jsp/zotye/base/businessParametersInit.jsp" ;
	private final String BUSINESS_PARAMETERS_ADD_INIT = "/jsp/zotye/base/businessParametersAddInit.jsp" ;
	private final String BUSINESS_PARAMETERS_UPDATE_INIT = "/jsp/zotye/base/businessParametersUpdateInit.jsp" ;
	
	public void businessParametersInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("type", Constant.SYSTEM_MODULE.toString()) ;
			
			act.setOutData("list", dao.businessParamtersQuery(map)) ;
			
			act.setForword(BUSINESS_PARAMETERS_INIT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"参数维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void businessParametersAddInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String selectParametarType = CommonUtils.checkNull(request.getParamValue("selectParametarType")) ;
			String hiddenParametarType = CommonUtils.checkNull(request.getParamValue("hiddenParametarType")) ;
			
			act.setOutData("selectParametarType", selectParametarType);
			act.setOutData("hiddenParametarType", hiddenParametarType);
			act.setForword(BUSINESS_PARAMETERS_ADD_INIT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"参数信息新增页面跳转");
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
			
			List<Map<String, Object>> list = dao.query(map) ;
			
			act.setOutData("id", id);
			act.setOutData("list", list);
			
			act.setForword(BUSINESS_PARAMETERS_UPDATE_INIT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"参数信息新增页面跳转");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void businessParametersQuery() {
		AclUserBean logonUser = null ;
		try {
			String selectSystemModule = CommonUtils.checkNull(request.getParamValue("selectSystemModule")) ;
			String selectSystemBusiness = CommonUtils.checkNull(request.getParamValue("selectSystemBusiness")) ;
			String selectParametarType = CommonUtils.checkNull(request.getParamValue("selectParametarType")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("moduleCode", selectSystemModule) ;
			map.put("businessCode", selectSystemBusiness) ;
			map.put("type", selectParametarType) ;
			map.put("isShow", Constant.IF_TYPE_YES.toString()) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.query(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"参数信息查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void businessParametersInsert() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String type = CommonUtils.checkNull(request.getParamValue("hiddenType")) ;
			String name = CommonUtils.checkNull(request.getParamValue("textName")) ;
			String value = CommonUtils.checkNull(request.getParamValue("textValue")) ;
			String remark = CommonUtils.checkNull(request.getParamValue("textRemark")) ;
			String sort = CommonUtils.checkNull(request.getParamValue("textSort")) ;
			String status = CommonUtils.checkNull(request.getParamValue("selectStatus")) ;
			String isShow = CommonUtils.checkNull(request.getParamValue("selectIsShow")) ;
			String isValue ;
			String code ;
			
			Map<String, String> codeMap = new HashMap<String, String>() ;
			codeMap.put("type", type) ;
			code = dao.businessParamtersCodeByType(codeMap) ;
			
			if(CommonUtils.isNullString(value)) {
				isValue = Constant.IF_TYPE_YES.toString() ;
			} else {
				isValue = Constant.IF_TYPE_NO.toString() ;
			}
			
			if(CommonUtils.isNullString(sort)) {
				sort = code ;
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("type", type) ;
			map.put("name", name) ;
			map.put("value", value) ;
			map.put("remark", remark) ;
			map.put("sort", sort) ;
			map.put("status", status) ;
			map.put("isShow", isShow) ;
			map.put("isValue", isValue) ;
			map.put("code", code) ;
			map.put("userId", logonUser.getUserId().toString()) ;
			
			dao.insert(map) ;
			
			act.setOutData("subFlag", "success") ;
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"参数信息新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void businessParametersUpdate() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("hiddenId")) ;
			String code = CommonUtils.checkNull(request.getParamValue("textCode")) ;
			String value = CommonUtils.checkNull(request.getParamValue("textValue")) ;
			String remark = CommonUtils.checkNull(request.getParamValue("textRemark")) ;
			String sort = CommonUtils.checkNull(request.getParamValue("textSort")) ;
			String status = CommonUtils.checkNull(request.getParamValue("selectStatus")) ;
			String isShow = CommonUtils.checkNull(request.getParamValue("selectIsShow")) ;
			String isValue ;
			
			if(CommonUtils.isNullString(value)) {
				isValue = Constant.IF_TYPE_YES.toString() ;
			} else {
				isValue = Constant.IF_TYPE_NO.toString() ;
			}
			
			if(CommonUtils.isNullString(sort)) {
				sort = code ;
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("id", id) ;
			map.put("value", value) ;
			map.put("remark", remark) ;
			map.put("sort", sort) ;
			map.put("status", status) ;
			map.put("isShow", isShow) ;
			map.put("isValue", isValue) ;
			map.put("userId", logonUser.getUserId().toString()) ;
			
			dao.update(map) ;
			
			act.setOutData("subFlag", "success") ;
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"参数信息修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void systemBusinessQuery() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String selectSystemModule = CommonUtils.checkNull(request.getParamValue("selectSystemModule")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("moduleCode", selectSystemModule) ;
			
			act.setOutData("list", dao.systemBusinessQeury(map)) ;
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统业务参数查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void businessParametarsTypeQuery() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String selectSystemBusiness = CommonUtils.checkNull(request.getParamValue("selectSystemBusiness")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("businessCode", selectSystemBusiness) ;
			
			act.setOutData("list", dao.systemBusinessTypeQeury(map)) ;
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"参数类型查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
