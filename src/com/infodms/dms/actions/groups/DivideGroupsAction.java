package com.infodms.dms.actions.groups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.groups.DivideGroupsDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class DivideGroupsAction {
	public Logger logger = Logger.getLogger(DivideGroupsAction.class);
	private ActionContext act = ActionContext.getContext();
	private DivideGroupsDAO dao = DivideGroupsDAO.getInstance() ; 
	RequestWrapper request = act.getRequest();
	
	private static final String DIVIDE_GROUPS_URL = "/jsp/groups/divideGroupsInit.jsp" ;
	private static final String DIVIDE_GROUPS_ADD_URL = "/jsp/groups/divideGroupsAddInit.jsp" ;
	private static final String DIVIDE_GROUPS_UPDATE_URL = "/jsp/groups/divideGroupsUpdateInit.jsp" ;
	private static final String DIVIDE_GROUPS_DIALOG_URL = "/jsp/groups/divideGroupsDialog.jsp" ;
	
	/**
	 * 初始化界面
	 */
	public void divideGroupsInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			
			act.setForword(DIVIDE_GROUPS_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "分组操作页面初始化 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询当前用户已创建的分组
	 */
	public void groupsQuery() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			
			String groupName = CommonUtils.checkNull(request.getParamValue("groupsName")) ;
			String groupType = CommonUtils.checkNull(request.getParamValue("groupsType")) ;
			String groupStatus = CommonUtils.checkNull(request.getParamValue("groupsStatus")) ;
			
			String userId = logonUser.getUserId().toString() ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("groupName", groupName) ;
			map.put("groupType", groupType) ;
			map.put("groupStatus", groupStatus) ;
			map.put("userId", userId) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1 ;
			
			PageResult<Map<String, Object>> ps = dao.queryGroup(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "分组查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增界面初始化
	 */
	public void groupsAddInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			
			act.setForword(DIVIDE_GROUPS_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增分组页面初始化 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增分组
	 */
	public void groupsAdd() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String groupName = CommonUtils.checkNull(request.getParamValue("groupsName")) ;
			String groupType = CommonUtils.checkNull(request.getParamValue("groupsType")) ;
			String groupStatus = CommonUtils.checkNull(request.getParamValue("groupsStatus")) ;
			String groupArea = Constant.DIVIDE_CLASS_SALE.toString() ;
			
			String userId = logonUser.getUserId().toString() ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("groupName", groupName) ;
			map.put("groupType", groupType) ;
			map.put("groupStatus", groupStatus) ;
			map.put("groupArea", groupArea) ;
			map.put("userId", userId) ;
			
			Long headId = dao.insertGroup(map) ;
			
			String dtlIds = CommonUtils.checkNull(request.getParamValue("dtlIds")) ;
			String dtlNames = CommonUtils.checkNull(request.getParamValue("dtlNames")) ;
			String dtlCodes = CommonUtils.checkNull(request.getParamValue("dtlCodes")) ;
			
			String[] aDtlIds = dtlIds.split(",") ;
			String[] aDtlNames = dtlNames.split(",") ;
			String[] aDtlCodes = dtlCodes.split(",") ;
			
			int len = aDtlIds.length ;
			
			for(int i=0; i<len; i++) {
				map = new HashMap<String, String>() ;
				
				map.put("headId", headId.toString()) ;
				map.put("theId", aDtlIds[i]) ;
				map.put("theName", aDtlNames[i]) ;
				map.put("theCode", aDtlCodes[i]) ;
				map.put("userId", userId) ;
				
				dao.insertGroupDtl(map) ;
			}
			
			act.setOutData("flag", "success") ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增分组 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改分组初始化
	 */
	public void groupsUpdateInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			
			String headId = CommonUtils.checkNull(request.getParamValue("headId")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("headId", headId) ;
			
			Map<String, Object> headMap = dao.queryGroupMap(map) ;
			
			List<Map<String, Object>> dtlList = dao.getGroupDtl(map) ;
			
			if(!CommonUtils.isNullList(dtlList)) {
				StringBuffer theId = new StringBuffer("") ;
				StringBuffer theName = new StringBuffer("") ;
				StringBuffer theCode = new StringBuffer("") ;
				
				int len = dtlList.size() ;
				
				for(int i=0; i<len; i++) {
					int strLen = theId.length() ;
					
					if(strLen == 0) {
						theId.append(dtlList.get(i).get("THE_ID")) ;
						theName.append(dtlList.get(i).get("THE_NAME")) ;
						theCode.append(dtlList.get(i).get("THE_CODE")) ;
					} else {
						theId.append(",").append(dtlList.get(i).get("THE_ID")) ;
						theName.append(",").append(dtlList.get(i).get("THE_NAME")) ;
						theCode.append(",").append(dtlList.get(i).get("THE_CODE")) ;
					}
				}
				
				act.setOutData("theId", theId) ;
				act.setOutData("theCode", theCode) ;
				act.setOutData("theName", theName) ;
			}
			
			act.setOutData("headId", headId) ;
			act.setOutData("headMap", headMap) ;
			act.setForword(DIVIDE_GROUPS_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "更新分组操作页面初始化 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void groupsDtlUpdateQuery() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			
			String headId = CommonUtils.checkNull(request.getParamValue("headId")) ;
			String queryName = CommonUtils.checkNull(request.getParamValue("queryName")) ;
			
			String dlrType = null ;
			
			dlrType = Constant.MSG_TYPE_1 ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("headId", headId) ;
			map.put("dlrType", dlrType) ;
			map.put("queryName", queryName) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1 ;
			
			PageResult<Map<String, Object>> ps = dao.queryGroupDtlDlrCompayAll(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "分组明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-21
	public void groupsDtlUpdateQuery111() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			
			String productId = CommonUtils.checkNull(request.getParamValue("productId")) ;
			String regionCode = CommonUtils.checkNull(request.getParamValue("regionCode")) ;
			String queryName = CommonUtils.checkNull(request.getParamValue("queryName")) ;
			
			String dlrType = null ;
			
			dlrType = Constant.MSG_TYPE_1 ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("productId", productId) ;
			map.put("dlrType", dlrType) ;
			map.put("regionCode", regionCode) ;
			map.put("queryName", queryName) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1 ;
			
			PageResult<Map<String, Object>> ps = dao.queryGroupDtlDlrCompayAll111(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐分配");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改分组
	 */
	public void groupsUpdate() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String headId = CommonUtils.checkNull(request.getParamValue("headId")) ;
			String groupName = CommonUtils.checkNull(request.getParamValue("groupsName")) ;
			String groupType = CommonUtils.checkNull(request.getParamValue("groupsType")) ;
			String groupStatus = CommonUtils.checkNull(request.getParamValue("groupsStatus")) ;
			String groupArea = Constant.DIVIDE_CLASS_SALE.toString() ;
			
			String userId = logonUser.getUserId().toString() ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("headId", headId) ;
			map.put("groupName", groupName) ;
			map.put("groupType", groupType) ;
			map.put("groupStatus", groupStatus) ;
			map.put("groupArea", groupArea) ;
			map.put("userId", userId) ;
			
			dao.updateGroup(map) ;
			dao.deleteGroupDtl(map) ;
			
			String dtlIds = CommonUtils.checkNull(request.getParamValue("dtlIds")) ;
			String dtlNames = CommonUtils.checkNull(request.getParamValue("dtlNames")) ;
			String dtlCodes = CommonUtils.checkNull(request.getParamValue("dtlCodes")) ;
			
			String[] aDtlIds = dtlIds.split(",") ;
			String[] aDtlNames = dtlNames.split(",") ;
			String[] aDtlCodes = dtlCodes.split(",") ;
			
			int len = aDtlIds.length ;
			
			for(int i=0; i<len; i++) {
				map = new HashMap<String, String>() ;
				
				map.put("headId", headId.toString()) ;
				map.put("theId", aDtlIds[i]) ;
				map.put("theName", aDtlNames[i]) ;
				map.put("theCode", aDtlCodes[i]) ;
				map.put("userId", userId) ;
				
				dao.insertGroupDtl(map) ;
			}
			
			act.setOutData("flag", "success") ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "修改分组 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-24
	public void packageUpdate() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String productId = CommonUtils.checkNull(request.getParamValue("productId")) ;
			//String groupName = CommonUtils.checkNull(request.getParamValue("groupsName")) ;
			//String groupType = CommonUtils.checkNull(request.getParamValue("groupsType")) ;
			//String groupStatus = CommonUtils.checkNull(request.getParamValue("groupsStatus")) ;
			//String groupArea = Constant.DIVIDE_CLASS_SALE.toString() ;
			
			String userId = logonUser.getUserId().toString() ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("productId", productId) ;
			//map.put("groupName", groupName) ;
			//map.put("groupType", groupType) ;
			//map.put("groupStatus", groupStatus) ;
			//map.put("groupArea", groupArea) ;
			map.put("userId", userId) ;
			
			//dao.updateGroup(map) ;
			dao.deleteProductDistribution(map);//删除关系
			
			String dtlIds = CommonUtils.checkNull(request.getParamValue("dtlIds")) ;
			String dtlNames = CommonUtils.checkNull(request.getParamValue("dtlNames")) ;
			String dtlCodes = CommonUtils.checkNull(request.getParamValue("dtlCodes")) ;
			
			String[] aDtlIds = dtlIds.split(",") ;
			String[] aDtlNames = dtlNames.split(",") ;
			String[] aDtlCodes = dtlCodes.split(",") ;
			
			int len = aDtlIds.length ;
			
			for(int i=0; i<len; i++) {
				map = new HashMap<String, String>() ;
				map.put("productId", productId);
				map.put("theId", aDtlIds[i]);
				map.put("theName", aDtlNames[i]);
				map.put("theCode", aDtlCodes[i]);
				map.put("userId", userId);
				
				dao.insertProductDistribution(map) ;//插入关系
			}
			
			act.setOutData("flag", "success") ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐分配 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dlrCompanyQuery() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			
			String queryName = CommonUtils.checkNull(request.getParamValue("queryName")) ;
			
			String dlrType = null ;
			
			dlrType = Constant.MSG_TYPE_1 ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("dlrType", dlrType) ;
			map.put("queryName", queryName) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1 ;
			
			PageResult<Map<String, Object>> ps = dao.queryDlrCompany(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "分组明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//--//
	public void groupsDialogInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			
			String userId = logonUser.getUserId().toString() ;
			String groupType = CommonUtils.checkNull(request.getParamValue("groupType")) ;
			String groupArea = CommonUtils.checkNull(request.getParamValue("groupArea")) ;
			String inputId = CommonUtils.checkNull(request.getParamValue("inputId")) ;
			String inputCode = CommonUtils.checkNull(request.getParamValue("inputCode")) ;
			String inputName = CommonUtils.checkNull(request.getParamValue("inputName")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("groupType", groupType) ;
			map.put("groupArea", groupArea) ;
			map.put("groupStatus", Constant.STATUS_ENABLE.toString()) ;
			map.put("userId", userId) ;
			
			List<Map<String, Object>> groupList = dao.getGroupByUser(map) ;
			
			act.setOutData("inputId", inputId) ;
			act.setOutData("inputCode", inputCode) ;
			act.setOutData("inputName", inputName) ;
			act.setOutData("groupList", groupList) ;
			
			act.setForword(DIVIDE_GROUPS_DIALOG_URL) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "分组弹出框初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryDtl() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			
			String headId = CommonUtils.checkNull(request.getParamValue("groupsId")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("headId", headId) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1 ;
			
			PageResult<Map<String, Object>> ps = dao.queryGroupDtl(map, Constant.PAGE_SIZE_MAX, curPage) ;
			
			act.setOutData("ps", ps) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "分组弹出框明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商产品套餐分配-维护-查询经销商名称和代码列表
	 * @author HXY
	 * @update 2013-01-29
	 * */
	public void getAllDealerOfPackage() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			
			String productId = CommonUtils.checkNull(request.getParamValue("productId")) ;
			String regionCode = CommonUtils.checkNull(request.getParamValue("regionCode")) ;
			String queryName = CommonUtils.checkNull(request.getParamValue("queryName")) ;
			
			String dlrType = null ;
			
			dlrType = Constant.MSG_TYPE_1 ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("productId", productId) ;
			map.put("dlrType", dlrType) ;
			map.put("regionCode", regionCode) ;
			map.put("queryName", queryName) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1 ;
			
			PageResult<Map<String, Object>> ps = dao.getAllDealerOfPackage(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐分配");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
