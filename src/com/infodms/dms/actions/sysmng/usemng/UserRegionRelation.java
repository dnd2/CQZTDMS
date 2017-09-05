package com.infodms.dms.actions.sysmng.usemng;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.usermng.UserRegionDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserRegionRelationPO;
import com.infodms.dms.po.TtIfStandardAuditPO;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 用户与省份关系
 */
public class UserRegionRelation {
	
	public Logger logger = Logger.getLogger(UserRegionRelation.class);
	UserRegionDao dao = new UserRegionDao();
	private final String initUrl = "/jsp/systemMng/userMng/userregionrelation.jsp";
	private final String userUrl = "/jsp/systemMng/userMng/queryrelationbyuser.jsp";
	private final String regionUrl = "/jsp/systemMng/userMng/queryregion.jsp";
	private final String regionUrl1 = "/jsp/systemMng/userMng/queryregion1.jsp";
	private final String modelgroup = "/jsp/systemMng/userMng/modelGroup.jsp";
	
	public void userRegionRelationInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户与省份关系");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryUserRegionRelation(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String acnt = request.getParamValue("acnt");
			String name = request.getParamValue("name");
			String companyId = String.valueOf(logonUser.getCompanyId());
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryUserRegionRelation(acnt, name, companyId, curPage,ActionUtil.getPageSize(request));
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
			// 自定义每页大小 by chenyub@yonyou.com
			ActionUtil.setCustomPageSizeFlag(act, true);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户与省份关系");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryRelationByUserId(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String userId = request.getParamValue("userId");
			Map<String, Object> map = dao.getUserMap(userId);
			act.setOutData("map", map);
			act.setOutData("userId", userId);
			act.setForword(userUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户与省份关系");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryRelationByUserList(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String userId = request.getParamValue("userId");
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryRelationByUserList(userId, curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户与省份关系");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryRegion(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String userId = request.getParamValue("userId");
			act.setOutData("userId", userId);
			act.setForword(regionUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户与省份关系");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-7-7
	public void queryRegion1(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			act.setForword(regionUrl1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户与省份关系");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-7-13
	public void queryModelGroup(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(modelgroup);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户与省份关系");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-7-13
	public void queryModelGroupList(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryModelGroupList(curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车型组");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryRegionList(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String userId = request.getParamValue("userId");
			String regionName = request.getParamValue("regionName");
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryRegionList(userId, regionName, curPage,ActionUtil.getPageSize(request));
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
			// 复选框翻页选中 by chenyub@yonyou.com
			ActionUtil.setCheckedValueToOutData(act);
			// 自定义每页大小 by chenyub@yonyou.com
			ActionUtil.setCustomPageSizeFlag(act, true);
			// 调整列宽功能 by chenyub@yonyou.com
			ActionUtil.setResizeColumnWidthFlag(act, true);
			// 表格列排序功能 by chenyub@yonyou.com
			ActionUtil.setTableSortFlag(act, true);
			// 表格交换列功能 by chenyub@yonyou.com
			ActionUtil.setSwapColumnFlag(act, true);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户与省份关系");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void addUserRegion(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String userId = request.getParamValue("userId");
			String[] codes = request.getParamValues("codes");
			if(codes!=null){
				for(int i=0;i<codes.length;i++){
					TcUserRegionRelationPO po = new TcUserRegionRelationPO();
					po.setId(Long.parseLong(SequenceManager.getSequence("")));
					po.setUserId(Long.parseLong(userId));
					po.setRegionCode(Integer.parseInt(codes[i]));
					po.setCreateBy(logonUser.getUserId());
					po.setCreateDate(new Date());
					dao.insert(po);
				}
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户与省份关系");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void deleteUserRegion(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String[] ids = request.getParamValues("ids");
			if(ids!=null){
				for(int i=0;i<ids.length;i++){
					TcUserRegionRelationPO po = new TcUserRegionRelationPO();
					po.setId(Long.parseLong(ids[i]));
					dao.delete(po);
				}
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户与省份关系");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
