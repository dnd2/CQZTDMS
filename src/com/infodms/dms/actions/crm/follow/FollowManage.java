package com.infodms.dms.actions.crm.follow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderaudit.OrderResourceReserveFirst;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.dealerleadsmanage.DlrLeadsManageDao;
import com.infodms.dms.dao.crm.follow.FollowManageDao;
import com.infodms.dms.dao.crm.invite.InviteManageDao;
import com.infodms.dms.dao.crm.order.OrderManageDao;
import com.infodms.dms.dao.crm.taskmanage.TaskManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PageResult;

public class FollowManage {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final InviteManageDao dao = InviteManageDao.getInstance();

	private final String FOLLOW_QUERY_URL = "/jsp/crm/follow/followInit.jsp";// 跟进查询页面
	private final String FOLLOW_DETAIL_QUERY_URL = "/jsp/crm/follow/followDetailInit.jsp";// 跟进查询详细页面
	
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		DlrLeadsManageDao dao2 = new DlrLeadsManageDao();
		String adviser = null;
		String userId = null;
		String managerLogon = "yes";
		String adviserLogon = "no";
		try {
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				managerLogon = "no";
				adviserLogon = "yes";
			} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
				managerLogon = "no";
				adviserLogon = "no";
				//获取主管下属分组的所有顾问
				userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
			} else if(CommonUtils.judgeDcrcLogin(logonUser.getUserId().toString())){//判断是否DCRC登陆
				managerLogon = "no";
				adviserLogon = "no";
			} 
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				adviser = logonUser.getUserId().toString();
			}
			//获取顾问列表
			List<DynaBean> adviserList = dao2.getAdviserBydealer2(logonUser.getDealerId(),userId);
			//获取分组列表
			List<DynaBean> groupList = dao2.getGroupBydealer(logonUser.getDealerId());
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			act.setOutData("adviserList", adviserList);
			act.setOutData("groupList", groupList);
			act.setOutData("managerLogon", managerLogon);
			act.setOutData("adviserLogon", adviserLogon);
			act.setOutData("funcStr", funcStr);
			act.setForword(FOLLOW_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "进入邀约查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跟进信息查询
	 */
	public void followFindQuery() {
		FollowManageDao dao = new FollowManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String userId = null;
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			userId = logonUser.getUserId().toString();
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		}
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String adviserId = CommonUtils.checkNull(request.getParamValue("adviserId"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String followType = CommonUtils.checkNull(request.getParamValue("follow_type"));
			String ctmRank=CommonUtils.checkNull(request.getParamValue("ctmRank"));
			String finishStatus=CommonUtils.checkNull(request.getParamValue("finishStatus"));
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			String adviser = userId;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.followFindQuery(customerName,
					telephone,startDate,endDate,userDealerId,adviser,adviserId,groupId,followType,ctmRank,finishStatus,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "销售线索查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跟进详细页面
	 */
	public void followDetailInit() {
		ActionContext act = ActionContext.getContext();
		FollowManageDao dao = new FollowManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String followId = act.getRequest().getParamValue("followId");
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			//获取客户基本信息
			List<DynaBean> customerList = dao.getCustomerInfo(followId);
			
			act.setOutData("followId", followId);
			act.setOutData("nowDate", nowDate);
			act.setOutData("customerList", customerList);
			
			act.setForword(FOLLOW_DETAIL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public static void getManager(AclUserBean logonUser, ActionContext act){
		DlrLeadsManageDao dao2 = new DlrLeadsManageDao();
		String userId = null;
		String managerLogon = "yes";
		String adviserLogon = "no";
		// 判断是否顾问登陆
		if (CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			managerLogon = "no";
			adviserLogon = "yes";
		} else if (CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())) {// 判断是否主管登陆
			managerLogon = "no";
			adviserLogon = "no";
			// 获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		} else if (CommonUtils.judgeDcrcLogin(logonUser.getUserId().toString())) {// 判断是否DCRC登陆
			managerLogon = "no";
			adviserLogon = "no";
		}
		// 获取顾问列表
		List<DynaBean> adviserList = dao2.getAdviserBydealer2(logonUser.getDealerId(), userId);
		// 获取分组列表
		List<DynaBean> groupList = dao2.getGroupBydealer(logonUser.getDealerId());
//		Map<String,String> map=new HashMap<String, String>();
//		map.put("dealerId", logonUser.getDealerId());
//		List<Map<String,Object>> userList=CommonUtils.queryUser(map);
//		act.setOutData("userList", userList);
		
		act.setOutData("adviserList", adviserList);
		act.setOutData("groupList", groupList);
		act.setOutData("managerLogon", managerLogon);
		act.setOutData("adviserLogon", adviserLogon);
	}
}