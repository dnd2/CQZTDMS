package com.infodms.dms.actions.crm.invite;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.crm.follow.FollowManage;
import com.infodms.dms.actions.sales.ordermanage.orderaudit.OrderResourceReserveFirst;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.invite.InviteCheckDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcCustomerPO;
import com.infodms.dms.po.TPcIntentVehiclePO;
import com.infodms.dms.po.TPcInvitePO;
import com.infodms.dms.po.TPcInviteShopPO;
import com.infodms.dms.po.TPcOrderPO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class InviteCheck {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final InviteCheckDao dao = InviteCheckDao.getInstance();

	private final String INVITE_CHECK_URL = "/jsp/crm/invite/inviteCheckInit.jsp";// 邀约审核查询界面
	private final String INVITE_CHECK_INIT = "/jsp/crm/invite/inviteCheckDetail.jsp";// 邀约审核界面
	
	/**
	 * 跳转到邀约审核查询
	 */
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			act.setOutData("funcStr", funcStr);
			Map<String,String> map=new HashMap<String, String>();
			map.put("dealerId", logonUser.getDealerId());
			map.put("userId", logonUser.getUserId().toString());
			List<Map<String,Object>> userList=CommonUtils.queryUser(map);
			FollowManage.getManager(logonUser, act);
			act.setOutData("userList", userList);
			act.setForword(INVITE_CHECK_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "跳转到邀约审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	
	 * @param 		:	
	 * @return		:查询所有需要审核的数据
	 * @throws		:	
	 * LastUpdate	:	2014-11-05
	 */
	public void inviteCheckQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));		//客户电话
			String name =  CommonUtils.checkNull(request.getParamValue("name"));			//客户姓名
			String planInviteDate = CommonUtils.checkNull(request.getParamValue("planInviteDate"));		//计划邀约时间
			String planInviteDateEnd = CommonUtils.checkNull(request.getParamValue("planInviteDateEnd"));		//计划邀约时间
			String planMeetDate =  CommonUtils.checkNull(request.getParamValue("planMeetDate"));			//计划见面时间
			String planMeetDateEnd =  CommonUtils.checkNull(request.getParamValue("planMeetDateEnd"));			//计划见面时间
			String groupId=CommonUtils.checkNull(request.getParamValue("groupId"));//组
			String adviser =  CommonUtils.checkNull(request.getParamValue("adviser"));	//顾问
			Map<String ,String > map=new HashMap<String,String>();
			
			map.put("telephone", telephone);
			map.put("name", name);
			map.put("planInviteDate", planInviteDate);
			map.put("planInviteDateEnd", planInviteDateEnd);
			map.put("planMeetDate", planMeetDate);
			map.put("planMeetDateEnd", planMeetDateEnd);
			map.put("dealerId", logonUser.getDealerId());
			map.put("logonId", logonUser.getUserId().toString());
			map.put("groupId", groupId);
			map.put("adviser", adviser);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize!=null?pageSize:"10";
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getInviteCheckQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查询所有需要审核的数据");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * 审核进入界面
	 */
	public void checkInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String inviteId=CommonUtils.checkNull(request.getParamValue("inviteId"));
				TPcInvitePO tp=new TPcInvitePO();
				tp.setInviteId(new Long(inviteId));
				tp=(TPcInvitePO) dao.select(tp).get(0);
				TPcInviteShopPO tpp=new TPcInviteShopPO();
				tpp.setInviteId(new Long(inviteId));
				if(dao.select(tpp).size()>0){
					tpp=(TPcInviteShopPO) dao.select(tpp).get(0);
				}
				
				DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String inviteShopDate=null;
				if(tp.getPlanMeetDate()!=null&&!"".equals(tp.getPlanMeetDate().toString())){
					inviteShopDate=sdf.format(tp.getPlanMeetDate());
				}
				String actShopDate="";
				if(tpp.getShopDate()!=null&&!"".equals(tpp.getShopDate().toString())){
					actShopDate=sdf.format(tpp.getShopDate());
				}
				
				//获取邀约方式
				
				String inviteType=CommonUtils.getCodeDesc(tp.getInviteWay().toString());
				
				//获取邀约类型
				String inviteWay="";
				if(tp.getInviteType()!=null&&!"".equals(tp.getInviteType())){
					inviteWay=CommonUtils.getCodeDesc(tp.getInviteType().toString());
				}
				
				//获取客户信息
				TPcCustomerPO  tpc=new TPcCustomerPO();
				tpc.setCustomerId(tp.getCustomerId());
				tpc=(TPcCustomerPO) dao.select(tpc).get(0);
				//获取销售顾问信息
				TcUserPO  tup=new TcUserPO();
				tup.setUserId(new Long(tpc.getAdviser()));
				tup=(TcUserPO) dao.select(tup).get(0);
				//获取原意向等级
				String oldLevel="";
				if(tp.getOldLevel()!=null&&!"".equals(tp.getOldLevel())){
					 oldLevel=CommonUtils.getCodeDesc(tp.getOldLevel().toString());
				}
				//获取销售顾问名
				String userName=tup.getName();
				String newLevel="";
				if(tp.getNewLevel()!=null&&!"".equals(tp.getNewLevel())){
					 oldLevel=CommonUtils.getCodeDesc(tp.getNewLevel().toString());
				}
				String ifShop="";
				if(tpp.getIfShop()!=null&&!"0".equals(tpp.getIfShop().toString())&&!"".equals(tpp.getIfShop().toString())){
					ifShop=CommonUtils.getCodeDesc(tpp.getIfShop().toString());
				}
				String oldShopLevel="";
				oldShopLevel=CommonUtils.getCodeDesc(tpp.getOldLevel());
				String newShopLevel="";
				newShopLevel=CommonUtils.getCodeDesc(tpp.getNewLevel());
				String oldProgress="";
				oldProgress=CommonUtils.getCodeDesc(tpp.getOldSalesProgress());
				
				String newProgress="";
				newProgress=CommonUtils.getCodeDesc(tpp.getSalesProgress());
				//获取意向车型
				String intentVehicle="";
				TPcIntentVehiclePO tpi=new TPcIntentVehiclePO();
				tpi.setSeriesId(new Long(tpc.getIntentVehicle()));
				tpi=(TPcIntentVehiclePO) dao.select(tpi).get(0);
				intentVehicle=tpi.getSeriesName();
				//获取新意向等级
				act.setOutData("intentVehicle", intentVehicle);
				//获取新意向等级
				act.setOutData("inviteShopDate", inviteShopDate);
				act.setOutData("tpp", tpp);
				act.setOutData("tp", tp);
				act.setOutData("tpc", tpc);
				act.setOutData("oldLevel", oldLevel);
				act.setOutData("newLevel", newLevel);
				act.setOutData("inviteType", inviteType);
				act.setOutData("inviteWay", inviteWay);
				act.setOutData("ifShop", ifShop);
				act.setOutData("userName", userName);//销售顾问名称
				act.setOutData("oldShopLevel", oldShopLevel);
				act.setOutData("newShopLevel", newShopLevel);
				act.setOutData("oldProgress", oldProgress);
				act.setOutData("newProgress", newProgress);
				act.setOutData("actShopDate", actShopDate);
				act.setOutData("actShopDate", actShopDate);
				
				String planInviteDate=sdf.format(tp.getPlanInviteDate());
				String planMeetDate=sdf.format(tp.getPlanMeetDate());
				act.setOutData("planInviteDate", planInviteDate);
				act.setOutData("planMeetDate", planMeetDate);
				act.setForword(INVITE_CHECK_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "审核进入界面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 邀约计划的审核方法
	 */
	public void inviteCheck(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String checkStatus=CommonUtils.checkNull(request.getParamValue("checkStatus"));//获取主管审核值
				String audit_remark=CommonUtils.checkNull(request.getParamValue("auditRemark"));//获取审核意见值
				String invite_id=CommonUtils.checkNull(request.getParamValue("invite_id"));//获取邀约的id
				String planInviteDate=CommonUtils.checkNull(request.getParamValue("planInviteDate"));//计划邀约时间
				String planMeetDate=CommonUtils.checkNull(request.getParamValue("planMeetDate"));//计划见面时间
				String requirement=CommonUtils.checkNull(request.getParamValue("requirement"));//需求分析 
				String inviteTarget=CommonUtils.checkNull(request.getParamValue("inviteTarget"));//邀约目标 
				String trustDesign=CommonUtils.checkNull(request.getParamValue("trustDesign"));//赢得客户信任设计 
				String sceneDesign=CommonUtils.checkNull(request.getParamValue("sceneDesign"));//感动客户情景设计 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String status="";
				//判断计划邀约状态（如若已提交计划邀约审核则抛出异常）
				TPcInvitePO oPo = new TPcInvitePO();
				oPo.setInviteId(Long.parseLong(invite_id));
				List<PO> poxx = dao.select(oPo);
				oPo = (TPcInvitePO)poxx.get(0);
				Integer oStatus = oPo.getDirectorAudit();
				if(oStatus != 60191001) {
					throw new Exception("该条数据已被处理过！");
				}
				//修改邀约表的状态
				TPcInvitePO tip0=new TPcInvitePO();
				tip0.setPlanInviteDate(sdf.parse(planInviteDate));
				tip0.setPlanMeetDate(sdf.parse(planMeetDate));
				tip0.setInviteId(new Long(invite_id));
				tip0.setRequirement(requirement);
				tip0.setInviteTarget(inviteTarget);
				tip0.setTrustDesign(trustDesign);
				tip0.setSceneDesign(sceneDesign);
				
				TPcInvitePO tip=new TPcInvitePO();
				tip.setInviteId(new Long(invite_id));
				
				tip0.setAuditRemark(audit_remark);
				if(checkStatus!=null&&!"".equals(checkStatus)&&"60301001".equals(checkStatus)){
					status=Constant.DIRECTOR_AUDIT_02.toString();
				}else{
					status=Constant.DIRECTOR_AUDIT_03.toString();
				}
				tip0.setDirectorAudit(new Integer(status));
				dao.update(tip, tip0);
				TPcInvitePO tip1=new TPcInvitePO();
				tip1.setInviteId(new Long(invite_id));
				tip1=(TPcInvitePO) dao.select(tip1).get(0);
				
				
				//获取客户信息
				TPcCustomerPO tpc=new TPcCustomerPO();
				tpc.setCustomerId(tip1.getCustomerId());
				tpc=(TPcCustomerPO) dao.select(tpc).get(0);
				//表示审核通过
				tip=(TPcInvitePO) dao.select(tip).get(0);
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(invite_id,Constant.REMIND_TYPE_15.toString());
				if(Constant.AUDIT_RESULT_01.toString().equals(checkStatus)){
					//新增提醒信息
					CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), invite_id, tip.getCustomerId().toString(), logonUser.getDealerId(), tpc.getAdviser().toString(), sdf.format(new Date()),"");
				}else{
					//新增提醒信息
					CommonUtils.addRemindInfo(Constant.REMIND_TYPE_16.toString(), invite_id, tip.getCustomerId().toString(), logonUser.getDealerId(), tpc.getAdviser().toString(), sdf.format(new Date()),"");
				}
				
				act.setOutData("flag", 1);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "邀约计划的审核方法");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
