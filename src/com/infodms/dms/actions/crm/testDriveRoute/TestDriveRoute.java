package com.infodms.dms.actions.crm.testDriveRoute;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.testDriveRoute.TestDriveRouteDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcTestDriveRoutePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class TestDriveRoute {
	private Logger logger = Logger.getLogger(TestDriveRoute.class);
	
	private ActionContext act = ActionContext.getContext();
	
	RequestWrapper request = act.getRequest();
	
	private final TestDriveRouteDao dao = TestDriveRouteDao.getInstance();

	private final String ROUTE_QUERY_URL = "/jsp/crm/testDriveRoute/testDriveRouteInit.jsp";// 试驾路线查询页面
	
	private final String ROUTE_ADD_INIT = "/jsp/crm/testDriveRoute/testDriveRouteAdd.jsp";// 试驾路线添加页面
	
	private final String ROUTE_UPDATE_INIT = "/jsp/crm/testDriveRoute/testDriveRouteUpdate.jsp";// 试驾路线修改页面

	public void doInit() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
		
			String funcStr = CommonUtils.judgeUserHasFunc(logonUser);
			
			act.setOutData("funcStr", funcStr);
			
			act.setForword(ROUTE_QUERY_URL);
			
		} catch (Exception e) {
			
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_ROUTE_CODE, "试驾路线用户组查询");
			
			logger.error(logonUser, e1);
			
			act.setException(e1);
			
		}
	}

	/**
	 * FUNCTION : 查询所有试驾路线
	 * 
	 * @param :
	 * @return :
	 * @throws : routeQueryList :
	 */
	public void routeQueryList() {
		
		AclUserBean logonUser = null;
		
		try {
		
			RequestWrapper request = act.getRequest();
			
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String routeName = CommonUtils.checkNull(request.getParamValue("routeName")); // 试驾路线名称
			
			String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));// 试驾里程数
			
			Map<String, String> map = new HashMap<String, String>();
			
			map.put("routeName", routeName);
			
			map.put("mileage", mileage);
			
			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));
			pageSize = pageSize == null || "".equals(pageSize) ? "10" : pageSize;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.getGroupQueryList(map,Integer.parseInt(pageSize), curPage);
			
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_ROUTE_CODE, "所有试驾路线资源查询");
			
			logger.error(logonUser, e1);
			
			act.setException(e1);
			
		}
	}

	/**
	 * 试驾路线新增界面
	 */
	public void routeAddInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
			
			act.setForword(ROUTE_ADD_INIT);
			
		} catch (Exception e) {
			
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_ROUTE_CODE, "试驾路线查询失败");
			
			logger.error(logonUser, e1);
			
			act.setException(e1);
			
		}
	}
	/**
	 * 试驾路线的添加
	 */
	public void routeAdd(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
				String routeName=CommonUtils.checkNull(request.getParamValue("routeName"));//试驾路线
				
				String mileage=CommonUtils.checkNull(request.getParamValue("mileage"));//里程数
				
				String startLine=CommonUtils.checkNull(request.getParamValue("startLine"));//开始路线名称
				
				String endLine=CommonUtils.checkNull(request.getParamValue("endLine"));//结束路线名称
				
				String remarks=CommonUtils.checkNull(request.getParamValue("remarks"));//备注
				
				int status=Integer.parseInt(CommonUtils.checkNull(request.getParamValue("status")));//状态是否显示
				
				TPcTestDriveRoutePO tdrp=new TPcTestDriveRoutePO();
				
				String seq=SequenceManager.getSequence("");
				
				tdrp.setRouteId(new Long(seq));
				
				tdrp.setRouteName(routeName);
				 
				tdrp.setMileage(mileage); 
				
				tdrp.setStartLine(startLine);
				
				tdrp.setEndLine(endLine);
				
				tdrp.setCreateBy(logonUser.getUserId());
				
				tdrp.setCreateDate(new Date());
				
				tdrp.setRemarks(remarks);
				
				tdrp.setStatus(status);
				
				int a=0;
				
				try 
				{
					dao.insert(tdrp);
				
					a=1;
					
				} catch (Exception e) 
				{
					a=0;
				}
				
				act.setOutData("flag", a);
				
		} catch (Exception e) 
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_ROUTE_CODE, "试驾路线添加");
			
			logger.error(logonUser, e1);
			
			act.setException(e1);
		}
	}
	/**
	 * 试驾路线修改页面
	 */
	public void routeUpdateInit() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
			
			String routeId = CommonUtils.checkNull(request.getParamValue("routeId"));
			
			TPcTestDriveRoutePO tdrp = new TPcTestDriveRoutePO();
			
			tdrp.setRouteId(new Long(routeId));
			
			tdrp = (TPcTestDriveRoutePO) dao.select(tdrp).get(0);
			
			act.setOutData("po", tdrp);
			
			act.setForword(ROUTE_UPDATE_INIT);
			
		} catch (Exception e) 
		{
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_ROUTE_CODE, "试驾路线修改查询失败");
			
			logger.error(logonUser, e1);
			
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商用户组的添加
	 */
	public void routeUpdate(){
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
			
			String routeId=CommonUtils.checkNull(request.getParamValue("routeId"));//试驾路线ID
			
			String routeName=CommonUtils.checkNull(request.getParamValue("routeName"));//试驾路线
			
			String mileage=CommonUtils.checkNull(request.getParamValue("mileage"));//里程数
			
			String startLine=CommonUtils.checkNull(request.getParamValue("startLine"));//开始路线名称
			
			String endLine=CommonUtils.checkNull(request.getParamValue("endLine"));//结束路线名称
			
			String remarks=CommonUtils.checkNull(request.getParamValue("remarks"));//备注
			
			int status=Integer.parseInt(CommonUtils.checkNull(request.getParamValue("status")));//状态是否显示
			
			TPcTestDriveRoutePO tdrp=new TPcTestDriveRoutePO();
			
			tdrp.setRouteName(routeName);
			 
			tdrp.setMileage(mileage); 
			
			tdrp.setStartLine(startLine);
			
			tdrp.setEndLine(endLine);
			
			tdrp.setUpdateBy(logonUser.getUserId());
			
			tdrp.setUpdateDate(new Date());
			
			tdrp.setRemarks(remarks);
			
			tdrp.setStatus(status);
			
			TPcTestDriveRoutePO tdrp1=new TPcTestDriveRoutePO();
			
			tdrp1.setRouteId(new Long(routeId));
			
				int a=0;
				try 
				{
					a=dao.update(tdrp1,tdrp);
				} 
				catch (Exception e) 
				{
					a=0;
				}
				act.setOutData("flag", a);
				
		} catch (Exception e) {
			
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "试驾路线查询");
			
			logger.error(logonUser, e1);
			
			act.setException(e1);
		}
	} 
	
	/**
	 * 加载下拉列表数据试车
	 * 
	 */
	public void routeSelect(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String codeId=request.getParamValue("codeId");
				
				Map<String,String> map=new HashMap<String,String>();
				
				map.put("codeId", codeId);
				
				List<Map<String,Object>> list= TestDriveRouteDao.queryDataList(map);
				
				act.setOutData("dataList", list);
				
				act.setOutData("topID", codeId);
				
		} catch (Exception e) {
			
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			
			logger.error(logonUser, e1);
			
			act.setException(e1);
		}
	}

}
