package com.infodms.dms.actions.crm.basedata;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderaudit.OrderResourceReserveFirst;
import com.infodms.dms.actions.util.LockControl;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.basedata.PcGroupDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcGroupPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class PcGroup {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final PcGroupDao dao = PcGroupDao.getInstance();

	private final String GROUP_QUERY_URL = "/jsp/crm/basedata/pcGroupInit.jsp";// 经销商用户组维护查询页面
	private final String GROUP_ADD_INIT = "/jsp/crm/basedata/pcGroupAdd.jsp";// 经销商用户组维护查询页面
	private final String GROUP_UPDATE_INIT = "/jsp/crm/basedata/pcGroupUpdate.jsp";// 经销商用户组维护查询页面
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
				act.setOutData("funcStr", funcStr);
				act.setForword(GROUP_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	查询经销商所有的组
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void groupQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String status = CommonUtils.checkNull(request.getParamValue("status"));		//经销商用户组的状态
			String groupName =  CommonUtils.checkNull(request.getParamValue("groupName"));			//组名称
			Map<String ,String > map=new HashMap<String,String>();
			map.put("status", status);
			map.put("groupName", groupName);
			map.put("dealerId", logonUser.getDealerId());
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getGroupQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "所有车辆资源查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * 用户组新增界面
	 */
	public void groupAddInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				act.setForword(GROUP_ADD_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商用户组的添加
	 */
	public void groupAdd(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String groupName=CommonUtils.checkNull(request.getParamValue("groupName"));
				TPcGroupPO tgp=new TPcGroupPO();
				String seq=SequenceManager.getSequence("");
				tgp.setGroupId(new Long(seq));
				tgp.setGroupName(groupName);
				tgp.setCreateDate(new Date());
				tgp.setCreateBy(logonUser.getUserId());
				tgp.setDealerId(new Long(logonUser.getDealerId()));
				tgp.setStatus(new Long(10011001));
				int a=0;
				try {
					dao.insert(tgp);
					a=1;
				} catch (Exception e) {
					a=0;
				}
				act.setOutData("flag", a);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 用户组修改初始化界面
	 */
	public void groupUpdateInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String groupId=CommonUtils.checkNull(request.getParamValue("groupId"));
				String status="";
				TPcGroupPO tgp=new TPcGroupPO();
				tgp.setGroupId(new Long(groupId));
				tgp=(TPcGroupPO) dao.select(tgp).get(0);
				act.setOutData("po", tgp);
				status=CommonUtils.getCodeDesc(tgp.getStatus().toString());
				act.setOutData("status", status);
				act.setForword(GROUP_UPDATE_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商用户组的添加
	 */
	public void groupUpdate(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String groupName=CommonUtils.checkNull(request.getParamValue("groupName"));
				String status=CommonUtils.checkNull(request.getParamValue("status"));
				String groupId=CommonUtils.checkNull(request.getParamValue("groupId"));
				TPcGroupPO tgp=new TPcGroupPO();
				tgp.setGroupName(groupName);
				tgp.setUpdateDate(new Date());
				tgp.setUpdateBy(logonUser.getUserId());
				tgp.setStatus(new Long(status));
				TPcGroupPO tgp0=new TPcGroupPO();
				tgp0.setGroupId(new Long(groupId));
				int a=0;
				try {
					a=dao.update(tgp0,tgp);
				} catch (Exception e) {
					a=0;
				}
				act.setOutData("flag", a);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//验证组名称
	public void checkGroupName() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String groupName=request.getParamValue("groupName");
				String groupId=request.getParamValue("groupId");
				Map<String,String> map=new HashMap<String, String>();
				map.put("groupName", groupName);
				map.put("dealerId", logonUser.getDealerId());
				map.put("groupId", groupId);
				int count=dao.checkGroupName(map);
				if(count<1){
					act.setOutData("flag", "1");
				}else{
					act.setOutData("flag", "2");
				}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
