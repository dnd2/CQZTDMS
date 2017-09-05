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
import com.infodms.dms.actions.util.LockControl;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.basedata.PcGroupDao;
import com.infodms.dms.dao.crm.invite.InviteManageDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcCustomerPO;
import com.infodms.dms.po.TPcGroupPO;
import com.infodms.dms.po.TPcIntentVehiclePO;
import com.infodms.dms.po.TPcInvitePO;
import com.infodms.dms.po.TPcInviteShopPO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class InviteManage {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final InviteManageDao dao = InviteManageDao.getInstance();

	private final String INVITE_QUERY_URL = "/jsp/crm/invite/inviteInit.jsp";// 邀约查询页面
	private final String INVITE_DETAIL_INIT = "/jsp/crm/invite/inviteDetail.jsp";// 邀约查询查询页面
	private final String INVITE_UPDATE_INIT = "/jsp/crm/invite/inviteUpdate.jsp";// 邀约修改页面
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			Map<String,String> map=new HashMap<String, String>();
			map.put("dealerId", logonUser.getDealerId());
			map.put("userId", logonUser.getUserId().toString());
			List<Map<String,Object>> userList=CommonUtils.queryUser(map);
			FollowManage.getManager(logonUser, act);
			act.setOutData("userList", userList);
			act.setOutData("funcStr", funcStr);
			act.setForword(INVITE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "进入邀约查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 审核进入界面
	 */
	public void doUpdateInit() {
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
				 newLevel=CommonUtils.getCodeDesc(tp.getNewLevel().toString());
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
				act.setForword(INVITE_UPDATE_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "审核进入界面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 修改操作
	 */
	public void doUpdate() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String invite_id=CommonUtils.checkNull(request.getParamValue("inviteId"));
				String require=CommonUtils.checkNull(request.getParamValue("requirement"));
				String aim=CommonUtils.checkNull(request.getParamValue("inviteTarget"));
				String tdesign=CommonUtils.checkNull(request.getParamValue("trustDesign"));
				String sdesign=CommonUtils.checkNull(request.getParamValue("sceneDesign"));
				String planInviteDate=CommonUtils.checkNull(request.getParamValue("planInviteDate"));
				String planMeetDate=CommonUtils.checkNull(request.getParamValue("planMeetDate"));
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				
				TPcInvitePO tip0=new TPcInvitePO();
				tip0.setInviteId(new Long(invite_id));
				tip0=(TPcInvitePO) dao.select(tip0).get(0);
				TPcInvitePO tp1=new TPcInvitePO();
				tp1.setInviteId(new Long(invite_id));
				TPcInvitePO tp2=new TPcInvitePO();
				tp2.setRequirement(require);
				tp2.setInviteTarget(aim);
				tp2.setTrustDesign(tdesign);
				tp2.setSceneDesign(sdesign);
				tp2.setDirectorAudit(Constant.DIRECTOR_AUDIT_01);
				tp2.setPlanInviteDate(sdf.parse(planInviteDate));
				tp2.setPlanMeetDate(sdf.parse(planMeetDate));
				dao.update(tp1, tp2);
				//获取客户信息
				TPcCustomerPO tpc=new TPcCustomerPO();
				tpc.setCustomerId(tip0.getCustomerId());
				tpc=(TPcCustomerPO) dao.select(tpc).get(0);
				
				//结束上一个提醒
				CommonUtils.setRemindDone(invite_id,Constant.REMIND_TYPE_15.toString());
				CommonUtils.setRemindDone(invite_id,Constant.REMIND_TYPE_16.toString());
				CommonUtils.setRemindDone(invite_id,Constant.REMIND_TYPE_06.toString());
				//新增提醒信息
				CommonUtils.addRemindInfo(Constant.REMIND_TYPE_15.toString(), invite_id, tip0.getCustomerId().toString(), logonUser.getDealerId(), "", sdf.format(new Date()),"");
				
				
				act.setOutData("flag", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "审核进入界面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	查询邀约信息
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void inviteQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));		//客户电话
			String name =  CommonUtils.checkNull(request.getParamValue("name"));			//客户名称
			String inviteType = CommonUtils.checkNull(request.getParamValue("inviteType"));		//是否邀约类型
			String inviteDate =  CommonUtils.checkNull(request.getParamValue("inviteDate"));//邀约时间
			String inviteDateEnd =  CommonUtils.checkNull(request.getParamValue("inviteDateEnd"));//邀约时间
			String adviser=CommonUtils.checkNull(request.getParamValue("adviser"));//顾问
			String groupId=CommonUtils.checkNull(request.getParamValue("groupId"));//组
			String ctmRank=CommonUtils.checkNull(request.getParamValue("ctmRank"));//客户等级
			String finishStatus=CommonUtils.checkNull(request.getParamValue("finishStatus"));//是否完成
			Map<String ,String > map=new HashMap<String,String>();
			map.put("telephone", telephone);
			map.put("name", name);
			map.put("inviteType", inviteType);
			map.put("inviteDate", inviteDate);
			map.put("inviteDateEnd", inviteDateEnd);
			map.put("dealerId", logonUser.getDealerId());
			map.put("ctmRank", ctmRank);
			map.put("finishStatus", finishStatus);
			String poseRank=CommonUtils.getPoseRank(logonUser);
			if(!Constant.DEALER_USER_LEVEL_05.toString().equals(poseRank)){
				map.put("logonId", logonUser.getUserId().toString());
			}
			map.put("adviser", adviser);
			map.put("groupId", groupId);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize!=null?pageSize:"10";
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getInviteQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "邀约信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * 查看邀约内容
	 */
	public void inviteDetailInit() {
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
				//获取原意向等级
				String oldLevel="";
				if(tp.getOldLevel()!=null&&!"".equals(tp.getOldLevel())){
					 oldLevel=CommonUtils.getCodeDesc(tp.getOldLevel().toString());
				}
				String newLevel="";
				if(tp.getNewLevel()!=null&&!"".equals(tp.getNewLevel())){
					newLevel=CommonUtils.getCodeDesc(tp.getNewLevel().toString());
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
				act.setOutData("inviteShopDate", inviteShopDate);
				act.setOutData("tpp", tpp);
				act.setOutData("tp", tp);
				act.setOutData("tpc", tpc);
				act.setOutData("oldLevel", oldLevel);
				act.setOutData("newLevel", newLevel);
				act.setOutData("inviteType", inviteType);
				act.setOutData("inviteWay", inviteWay);
				act.setOutData("ifShop", ifShop);
				act.setOutData("oldShopLevel", oldShopLevel);
				act.setOutData("newShopLevel", newShopLevel);
				act.setOutData("oldProgress", oldProgress);
				act.setOutData("newProgress", newProgress);
				act.setOutData("actShopDate", actShopDate);
				act.setForword(INVITE_DETAIL_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查看邀约内容");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
