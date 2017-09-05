package com.infodms.dms.actions.crm.taskmanage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.crm.dealerleadsmanage.DlrLeadsManage;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.oemleadsmanage.OemLeadsManageDao;
import com.infodms.dms.dao.crm.taskmanage.TaskManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcCustomerPO;
import com.infodms.dms.po.TPcDefeatfailurePO;
import com.infodms.dms.po.TPcFollowPO;
import com.infodms.dms.po.TPcInvitePO;
import com.infodms.dms.po.TPcInviteShopPO;
import com.infodms.dms.po.TPcLeadsAllotPO;
import com.infodms.dms.po.TPcLeadsPO;
import com.infodms.dms.po.TPcLinkManPO;
import com.infodms.dms.po.TPcOrderAuditPO;
import com.infodms.dms.po.TPcOrderDetailPO;
import com.infodms.dms.po.TPcOrderPO;
import com.infodms.dms.po.TPcRemindPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class TaskManage extends BaseImport {

	public Logger logger = Logger.getLogger(DlrLeadsManage.class);
	// 日程管理初始页面
	private final String taskManageinitUrl = "/jsp/crm/taskmanage/taskmanageinit.jsp";
	// 车厂导入线索顾问确认页面
	private final String leadsConfirmByOemUrl = "/jsp/crm/taskmanage/leadsconfirmbyoem.jsp";
	// DCRC导入线索顾问确认页面
	private final String leadsConfirmByDcrcUrl = "/jsp/crm/taskmanage/leadsconfirmbydcrc.jsp";
	// 跟进任务页面
	private final String doTaskFollowUrl = "/jsp/crm/taskmanage/dotaskfollow.jsp";
	// 计划邀约页面
	private final String doTaskInviteUrl = "/jsp/crm/taskmanage/dotaskinvite.jsp";
	// 邀约到店页面
	private final String doTaskInviteShopUrl = "/jsp/crm/taskmanage/dotaskinviteshop.jsp";
	// 订单任务页面
	private final String doTaskOrderUrl = "/jsp/crm/taskmanage/dotaskorder.jsp";
	// 订单任务页面
	private final String doTaskDeliveryUrl = "/jsp/crm/taskmanage/dotaskdelivery.jsp";
	// 订单退单任务页面
	private final String doTaskOrderBackUrl = "/jsp/crm/taskmanage/dotaskorderback.jsp";
	/**
	 * 日程管理初始页面
	 */
	public void doInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TaskManageDao dao = new TaskManageDao();
		String queryFlag = act.getRequest().getParamValue("queryFlag");
		String startDate = act.getRequest().getParamValue("startDate");
		String endDate = act.getRequest().getParamValue("endDate");
		String dealerId = logonUser.getDealerId();
		String userId = null;
		String switchtab = act.getRequest().getParamValue("switchtab"); //回跳指定的tab页
		if(switchtab==null||"".equals(switchtab)) {
			switchtab = "1";
		}
		Date nowDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(startDate==null || "".equals(startDate)) {
			startDate = sdf.format(nowDate);
		}
		if(endDate==null || "".equals(endDate)) {
			endDate = sdf.format(nowDate);
		}
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			userId = logonUser.getUserId().toString();
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		}
		try {
			
			//获取销售线索信息(未确认，有效)
			List<DynaBean> leadsList = dao.getLeadsInfo(dealerId,userId,startDate,endDate);
			//获取跟进任务表信息
			List<DynaBean> followList = dao.getFollowInfo(dealerId,userId,startDate,endDate);
			//获取邀约任务表信息
			List<DynaBean> inviteList = dao.getInviteInfo(dealerId,userId,startDate,endDate);
			//获取回访任务表信息
			List<DynaBean> revisitList = dao.getRevisitInfo(dealerId,userId,startDate,endDate);
			//获取订单任务表信息
			List<DynaBean> orderList = dao.getOrderInfo(dealerId,userId,startDate,endDate);
			//获取交车任务表信息
			List<DynaBean> deliveryList = dao.getDeliveryInfo(dealerId,userId,startDate,endDate);
			//根据顾问获取基盘客户信息
			List<DynaBean> adviserList = null;
			//dao.getCustomerInfoByAdviser(dealerId,userId);
			//获取销售排名信息
			List<DynaBean> rankingList = dao.getAdviserSalesRanking(dealerId,userId);
			
			//未确认及有效的线索数量
			Integer leadsNoListSize = leadsList.size();
			//获取所有销售线索的数量
			List<DynaBean> leadsYesList = dao.getAllLeadsInfo(dealerId,userId,startDate,endDate);
			//未完成及有效的邀约数量
			Integer inviteNoListSize = inviteList.size();
			//获取所有邀约的数量
			List<DynaBean> inviteYesList = dao.getAllInviteInfo(dealerId,userId,startDate,endDate);
			//未完成及有效的回访数量
			Integer revisitNoListSize = revisitList.size();
			//获取所有回访的数量
			List<DynaBean> revisitYesList = dao.getAllRevisitInfo(dealerId,userId,startDate,endDate);
			//未完成及有效的跟进数量
			Integer followNoListSize = followList.size();
			//获取所有跟进的数量
			List<DynaBean> followYesList = dao.getAllFollowInfo(dealerId,userId,startDate,endDate);
			//未完成及有效的订单数量
			Integer orderNoListSize = orderList.size();
			//获取所有订单的数量
			List<DynaBean> orderYesList = dao.getAllOrderInfo(dealerId,userId,startDate,endDate);
			//未完成及有效的交车数量
			Integer deliveryNoListSize = deliveryList.size();
			//获取所有交车的数量
			List<DynaBean> deliveryYesList = dao.getAllDeliveryInfo(dealerId,userId,startDate,endDate);
			
			act.setOutData("nowDate", nowDate);
			act.setOutData("switchtab", switchtab);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setOutData("adviserList", adviserList);
			act.setOutData("rankingList", rankingList);
			act.setOutData("leadsList", leadsList);
			act.setOutData("followList", followList);
			act.setOutData("inviteList", inviteList);
			act.setOutData("revisitList", revisitList);
			act.setOutData("orderList", orderList);
			act.setOutData("deliveryList", deliveryList);
			act.setOutData("leadsListNoSize", leadsNoListSize.toString());
			act.setOutData("leadsListYesSize", leadsYesList.size());
			act.setOutData("inviteNoListSize", inviteNoListSize.toString());
			act.setOutData("inviteYesListSize", inviteYesList.size());
			act.setOutData("followNoListSize", followNoListSize.toString());
			act.setOutData("followYesListSize", followYesList.size());
			act.setOutData("revisitNoListSize", revisitNoListSize.toString());
			act.setOutData("revisitYesList", revisitYesList.size());
			act.setOutData("orderNoListSize", orderNoListSize.toString());
			act.setOutData("orderYesListSize", orderYesList.size());
			act.setOutData("deliveryNoListSize", deliveryNoListSize.toString());
			act.setOutData("deliveryYesListSize", deliveryYesList.size());
			act.setForword(taskManageinitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void doSeriesInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TaskManageDao dao = new TaskManageDao();
		String dealerId = logonUser.getDealerId();
		String userId = null;
		String switchtab = act.getRequest().getParamValue("switchtab"); //回跳指定的tab页
		
		if(switchtab==null||"".equals(switchtab)) {
			switchtab = "1";
		}
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			userId = logonUser.getUserId().toString();
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		}
		
		
		try {
			
		//根据车系获取基盘客户信息
		List<DynaBean> seriesList = dao.getCustomerInfoBySeries(dealerId,userId);
	
		act.setOutData("ps", seriesList);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	
	//得到顾问列表
	public void doUserInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TaskManageDao dao = new TaskManageDao();
		String dealerId = logonUser.getDealerId();
		String userId = null;
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			userId = logonUser.getUserId().toString();
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		}
		
		
		try {
			
		//根据车系获取基盘客户信息
		 List<DynaBean> adviserList =dao.getCustomerInfoByAdviser(dealerId,userId);
	
		act.setOutData("ps", adviserList);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void doDealerInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TaskManageDao dao = new TaskManageDao();
		String dealerId = logonUser.getDealerId();
		String userId = null;
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			userId = logonUser.getUserId().toString();
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		}
		try {
			
		//根据经销商获取基盘客户信息
		List<DynaBean> dealerList = null;

		//判断经销商为1级还是2级
		if(CommonUtils.judgeDealerLevel(dealerId)) {
			//为一级
			dealerList = dao.getCustomerInfoByDealer(dealerId,userId);
		} else {
			//为二级
			dealerList = dao.getCustomerInfoByDealer2(dealerId,userId);
		}
		act.setOutData("ps", dealerList);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 车厂导入线索顾问确认页面
	 */
	public void leadsConfirmByOemInit() {
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		List a[] = new List[10]; 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String leadsCode = act.getRequest().getParamValue("leadsCode");
		String leadsAllotId = act.getRequest().getParamValue("leadsAllotId");
		String dPro = CommonUtils.checkNull(act.getRequest().getParamValue("dPro"));
		String dCity = CommonUtils.checkNull(act.getRequest().getParamValue("dCity"));
		String dArea = CommonUtils.checkNull(act.getRequest().getParamValue("dArea"));
		String adviserLogon = "no";//顾问登陆标志
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			if(leadsCode==null||"".equals(leadsCode)||"null".equals(leadsCode)) {
				//根据leadsAllotId获取leadsCode
				TPcLeadsAllotPO allotPo = new TPcLeadsAllotPO();
				allotPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
				List<PO> po = dao.select(allotPo);
				allotPo = (TPcLeadsAllotPO)po.get(0);
				leadsCode = allotPo.getLeadsCode().toString();
			}
			//从线索中获取客户姓名和联系电话
			List<DynaBean> customerList = dao.getNameAndTelephone(leadsCode);
			String customerName = null;
			String telephone = null;
			String leadsOrigin = null;
			String intentcar=null;
			Iterator it = (Iterator) customerList.iterator();
			while(it.hasNext()) {
				DynaBean db = (DynaBean)it.next();
				if(db.get("CUSTOMER_NAME")!=null&&!"".equals(db.get("CUSTOMER_NAME"))) {
					customerName = db.get("CUSTOMER_NAME").toString();
				} else {
					customerName = "";
				}
				telephone = db.get("TELEPHONE").toString();
				leadsOrigin = db.get("LEADS_ORIGIN").toString();
				if(db.get("INTENT_CAR")!=null && !"".equals(db.get("INTENT_CAR"))){
				intentcar = db.get("INTENT_CAR").toString();
				}else{
				intentcar="";
				}
			}
			//获取意向车型一级列表
			List<DynaBean> menusAList = dao.getIntentVehicleA();
			//获取意向车型二级列表
			List<DynaBean> menusBList = dao.getIntentVehicleB();
			//获取战败车型一级列表
			List<DynaBean> menusAList2 = dao.getDefeatVehicleA();
			//获取战败车型二级列表
			List<DynaBean> menusBList2 = dao.getDefeatVehicleB();
			//获取战败原因一级列表
			List<DynaBean> menusAList3 = dao.getDefeatReasonA();
			//获取战败原因二级列表
			List<DynaBean> menusBList3 = dao.getDefeatReasonB();
			
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				adviserLogon = "yes";
			}
			
			act.setOutData("adviserLogon", adviserLogon);
			act.setOutData("leadsAllotId", leadsAllotId);
			act.setOutData("leadsCode", leadsCode);
			act.setOutData("nowDate", nowDate);
			act.setOutData("customerList", customerList);
			act.setOutData("customerName", customerName);
			act.setOutData("telephone", telephone);
			act.setOutData("leadsOrigin", leadsOrigin);
			act.setOutData("intentcar", intentcar);
			act.setOutData("menusAList", menusAList);
			act.setOutData("menusBList", menusBList);
			act.setOutData("menusAList2", menusAList2);
			act.setOutData("menusBList2", menusBList2);
			act.setOutData("menusAList3", menusAList3);
			act.setOutData("menusBList3", menusBList3);
			act.setForword(leadsConfirmByOemUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 车厂导入线索顾问确认
	 */
	public void leadsConfirmByOem() {
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		OemLeadsManageDao dao2 = new OemLeadsManageDao();
		List a[] = new List[10]; 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String leadsCode = act.getRequest().getParamValue("leadsCode");
		String leadsAllotId = act.getRequest().getParamValue("leadsAllotId");
		String leadsOrigin = act.getRequest().getParamValue("leadsOrigin");
		String typeFlag = act.getRequest().getParamValue("typeFlag");
		String customerName = act.getRequest().getParamValue("customer_name");//客户姓名
		String telePhone = act.getRequest().getParamValue("telephone");//联系电话
		String collectFashion = act.getRequest().getParamValue("collect_fashion");//集客方式
		String buyBudget = act.getRequest().getParamValue("buy_budget");//购车预算
		String customerType = act.getRequest().getParamValue("customer_type");//客户类型(有望/无望)
		String intentVehicle = act.getRequest().getParamValue("intentVehicle");//意向车型
		String followDate = act.getRequest().getParamValue("follow_date");//跟进时间
		String intentType = act.getRequest().getParamValue("intent_type");//意向等级
		String salesProgress = act.getRequest().getParamValue("sales_progress");//销售流程进度
		String followInfo = act.getRequest().getParamValue("follow_info");//跟进说明
		String nextFollowDate = act.getRequest().getParamValue("next_follow_date");//下次跟进时间
		String followType = act.getRequest().getParamValue("follow_type");//跟进方式
		String followPlan = act.getRequest().getParamValue("follow_plan");//跟进计划
		String xqfx = act.getRequest().getParamValue("next_follow_date"); //需求分析
		String yymb = act.getRequest().getParamValue("yymb"); //邀约目标
		String ydkhxrsj = act.getRequest().getParamValue("ydkhxrsj"); //赢得客户信任设计
		String gdkhqjsj = act.getRequest().getParamValue("gdkhqjsj"); //感动客户情景设计
		String planInviteDate = act.getRequest().getParamValue("plan_invite_date"); //计划邀约时间
		String planMeetDate = act.getRequest().getParamValue("plan_meet_date"); //计划见面时间
		String inviteType = act.getRequest().getParamValue("invite_type"); //邀约方式
		String orderDate = act.getRequest().getParamValue("order_date"); //订车时间
		String defeatVehicle = act.getRequest().getParamValue("defeatVehicleB");//战败车型
		String defeatReason = act.getRequest().getParamValue("defeatReasonB");//战败原因
		String defeatInfo = act.getRequest().getParamValue("defeat_info");//战败说明
		String dPro = CommonUtils.checkNull(act.getRequest().getParamValue("dPro"));//省
		String dCity = CommonUtils.checkNull(act.getRequest().getParamValue("dCity"));//市
		String dArea = CommonUtils.checkNull(act.getRequest().getParamValue("dArea"));//区
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			String remark="车厂线索方法ByOem";
			TPcLeadsPO leadsOldPoByOem = new TPcLeadsPO();
			leadsOldPoByOem.setLeadsCode(Long.parseLong(leadsCode));
			TPcLeadsPO leadsNewPoByOem = new TPcLeadsPO();
			leadsNewPoByOem.setFailureRemark(remark);
			dao.update(leadsOldPoByOem, leadsNewPoByOem);
			//PC和移动端同时处理问题
			int ifHandle = CommonUtils.getTheSameData(leadsCode);
			if(ifHandle==10041002) {//未处理的情况
				//验证是否已建立了档案（根据客户名称、联系电话和经销商ID）
				List<Map<String, Object>> getHasList = null;
				List<DynaBean> getHasList2 = null;
				getHasList=dao2.getHasCustomer(telePhone,customerName,logonUser.getDealerId());
				
				//已存在档案，(判断是否为本顾问的客户,是就修改客户档案.否发送错误消息到页面)
				if(getHasList.size()>0) {
					remark=remark+"-已建档";
					leadsNewPoByOem.setFailureRemark(remark);
					dao.update(leadsOldPoByOem, leadsNewPoByOem);
					//判断是否为本顾问的客户
					getHasList2 = dao2.getHasCustomer2(telePhone,customerName,logonUser.getDealerId(),logonUser.getUserId().toString());
					if(getHasList2.size()>0) {// 修改客户档案信息，并标记线索为已确认
						// 修改客户档案信息
						TPcCustomerPO oldCustomerPo = new TPcCustomerPO();
						//得到需修改客户的CUSTOMERID
						DynaBean db = getHasList2.get(0);
						String customerId = db.get("CUSTOMER_ID").toString();
						String adviser = db.get("ADVISER").toString();
						String dealerId = db.get("DEALER_ID").toString();
						String ctmType = "";
						if(db.get("CTM_TYPE")!=null) {
							ctmType = db.get("CTM_TYPE").toString();
						}
						oldCustomerPo.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo = new TPcCustomerPO();
						newCustomerPo.setCustomerId(Long.parseLong(customerId));
						newCustomerPo.setJcWay(collectFashion);
						newCustomerPo.setUpdateDate(new Date());
						newCustomerPo.setUpdateBy(logonUser.getUserId());
						newCustomerPo.setBuyBudget(buyBudget);
						newCustomerPo.setCtmProp(customerType);
						newCustomerPo.setIntentVehicle(intentVehicle);
						if(typeFlag !=null && !"".equals(typeFlag)&&!"''".equals(typeFlag)){
						newCustomerPo.setCtmRank(intentType);
						}
						newCustomerPo.setSalesProgress(salesProgress);
						newCustomerPo.setProviceId(dPro);
						newCustomerPo.setCityId(dCity);
						newCustomerPo.setTownId(dArea);
						if(typeFlag !=null && !"".equals(typeFlag)&&!"''".equals(typeFlag)){
						if("60341001".equals(ctmType)){
							newCustomerPo.setCtmType(Constant.CTM_TYPE_03.toString());
						} else if("60341004".equals(ctmType) || "60341005".equals(ctmType)) {
							newCustomerPo.setCtmType(Constant.CTM_TYPE_02.toString());
						}
						}
						dao.update(oldCustomerPo, newCustomerPo);
						
						//标记该线索为已确认
						//修改销售线索主表
						TPcLeadsPO leadsOldPo = new TPcLeadsPO();
						leadsOldPo.setLeadsCode(Long.parseLong(leadsCode));
						TPcLeadsPO leadsNewPo = new TPcLeadsPO();
						leadsNewPo.setLeadsCode(Long.parseLong(leadsCode));
						leadsNewPo.setCustomerName(customerName);
						leadsNewPo.setCustomerId(Long.parseLong(customerId));
						leadsNewPo.setTelephone(telePhone);
						leadsNewPo.setIntentVehicle(intentVehicle);
						leadsNewPo.setIfHandle(10041001);
						leadsNewPo.setUpdateDate(new Date());
						leadsNewPo.setUpdateBy(logonUser.getUserId());
						dao.update(leadsOldPo, leadsNewPo);
						//修改销售线索分派表
						TPcLeadsAllotPO leadsAllotOldPo = new TPcLeadsAllotPO();
						leadsAllotOldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						TPcLeadsAllotPO leadsAllotNewPo = new TPcLeadsAllotPO();
						//leadsAllotNewPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						leadsAllotNewPo.setCustomerName(customerName);
						leadsAllotNewPo.setCustomerId(Long.parseLong(customerId));
						leadsAllotNewPo.setTelephone(telePhone);
						leadsAllotNewPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);
						leadsAllotNewPo.setConfirmDate(new Date());
						dao.update(leadsAllotOldPo, leadsAllotNewPo);
						
						if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
							//增加接触点信息
							CommonUtils.addContackPoint(Constant.POINT_WAY_01, "公司类重复线索", customerId, adviser, dealerId);
							String repeatLeads = SequenceManager.getSequence("");
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_20.toString(), repeatLeads, customerId, dealerId, adviser, sdf.format(new Date()),"");
							//标记线索为重复线索
							CommonUtils.updateIfRepeat(leadsAllotId);
							CommonUtils.updateLeadStatus(leadsCode);
						}
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
						
						if("followRadio".equals(typeFlag) || "inviteRadio".equals(typeFlag) || "orderRadio".equals(typeFlag)){
							String followId = SequenceManager.getSequence("");//本次跟进任务ID
							Long nextTaskId = Long.parseLong(SequenceManager.getSequence(""));//下次任务ID
							//新增本次跟进任务表
							TPcFollowPO follow0Po = new TPcFollowPO();
							follow0Po.setFollowId(Long.parseLong(followId));
							follow0Po.setCustomerId(Long.parseLong(customerId));
							follow0Po.setFollowDate(new Date());
							follow0Po.setOldLevel(null);
							follow0Po.setNewLevel(intentType);
							follow0Po.setSalesProgress(salesProgress);
							follow0Po.setFollowInfo(followInfo);
							follow0Po.setTaskStatus(Constant.TASK_STATUS_02);
							follow0Po.setFinishDate(new Date());
							follow0Po.setFollowType(Constant.FOLLOW_TYPE_01.toString());
							follow0Po.setCreateDate(new Date());
							follow0Po.setCreateBy(logonUser.getUserId().toString());
							follow0Po.setNextTask(nextTaskId);
							dao.insert(follow0Po);
							
							//增加接触点信息
							CommonUtils.addContackPoint(Constant.POINT_WAY_03, followInfo, customerId, logonUser.getUserId().toString(), logonUser.getDealerId());
							
							if("followRadio".equals(typeFlag)) {//点跟进时
								//新增下次跟进任务表
								TPcFollowPO followPo = new TPcFollowPO();
								followPo.setFollowId(nextTaskId);
								followPo.setCustomerId(Long.parseLong(customerId));
								followPo.setFollowDate(sdf.parse(nextFollowDate));
								followPo.setOldLevel(intentType);
								followPo.setNewLevel(null);
								followPo.setTaskStatus(Constant.TASK_STATUS_01);
								followPo.setFollowType(followType);
								followPo.setFollowPlan(followPlan);
								followPo.setCreateDate(new Date());
								followPo.setCreateBy(logonUser.getUserId().toString());
								followPo.setPreviousTask(Long.parseLong(followId));
								followPo.setOldSalesProgress(salesProgress);
								dao.insert(followPo);
								
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), nextFollowDate,"");
							} else if("inviteRadio".equals(typeFlag)) {//点邀约时
								//新增下次计划邀约任务表
								TPcInvitePO invitePo = new TPcInvitePO();
								invitePo.setInviteId(nextTaskId);
								invitePo.setCustomerId(Long.parseLong(customerId));
								invitePo.setCreateDate(new Date());
								invitePo.setCreateBy(logonUser.getUserId().toString());
								invitePo.setPlanInviteDate(sdf.parse(planInviteDate));
								invitePo.setPlanMeetDate(sdf.parse(planMeetDate));
								invitePo.setTaskStatus(Constant.TASK_STATUS_01);
								invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_04);
								invitePo.setIfPlan(Constant.IF_TYPE_NO);
								invitePo.setOldLevel(intentType);
								invitePo.setNewLevel(null);
								invitePo.setInviteType(Integer.parseInt(inviteType));
								invitePo.setPreviousTask(Long.parseLong(followId));
								invitePo.setOldSalesProgress(salesProgress);
								//判断是否做了计划（4个框有值说明为做了计划）
								if(xqfx!=null&&!"".equals(xqfx)) {
									invitePo.setIfPlan(Constant.IF_TYPE_YES);
									invitePo.setRequirement(xqfx);
									invitePo.setInviteTarget(yymb);
									invitePo.setTrustDesign(ydkhxrsj);
									invitePo.setSceneDesign(gdkhqjsj);
									invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_01);
									//新增提醒信息
									CommonUtils.addRemindInfo(Constant.REMIND_TYPE_15.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
								} else {
									//新增提醒信息
									CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), planInviteDate,"");
								}
								dao.insert(invitePo);
							} else if("orderRadio".equals(typeFlag)) {//点订单时
								//新增下次订单任务表
								//新增下次订单任务
								TPcOrderPO orderPo = new TPcOrderPO();
								orderPo.setOrderId(nextTaskId);
								orderPo.setCustomerId(Long.parseLong(customerId));
								orderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
								orderPo.setOldLevel(intentType);
								orderPo.setCreateDate(new Date());
								orderPo.setCreateBy(logonUser.getUserId().toString());
								orderPo.setOldSalesProgress(salesProgress);
								orderPo.setPreviousTask(Long.parseLong(followId));
								orderPo.setTaskStatus(Constant.TASK_STATUS_01);
								orderPo.setOrderDate(sdf.parse(orderDate));
								dao.insert(orderPo);
								
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_08.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), orderDate,"");
							}
						} else if("defeatRadio".equals(typeFlag)){
							//选择战败后保存
							//修改销售线索主表
							TPcLeadsPO leadsOldPo2 = new TPcLeadsPO();
							leadsOldPo2.setLeadsCode(Long.parseLong(leadsCode));
							TPcLeadsPO leadsNewPo2 = new TPcLeadsPO();
							leadsNewPo2.setLeadsCode(Long.parseLong(leadsCode));
							leadsNewPo2.setCustomerName(customerName);
							leadsNewPo2.setTelephone(telePhone);
							leadsNewPo2.setIntentVehicle(intentVehicle);
							leadsNewPo2.setLeadsStatus(Constant.LEADS_STATUS_02.longValue());//线索状态为 战败线索
							leadsNewPo2.setDefeatModel(defeatVehicle);
							leadsNewPo2.setDefeatReason(defeatReason);
							leadsNewPo2.setDefaultRemark(defeatInfo);
							leadsNewPo2.setIfHandle(10041001);
							leadsNewPo2.setUpdateDate(new Date());
							leadsNewPo2.setUpdateBy(logonUser.getUserId());
							dao.update(leadsOldPo2, leadsNewPo2);
							
							//修改销售线索分派表
							TPcLeadsAllotPO leadsAllotOldPo2 = new TPcLeadsAllotPO();
							leadsAllotOldPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
							TPcLeadsAllotPO leadsAllotNewPo2 = new TPcLeadsAllotPO();
							//leadsAllotNewPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
							leadsAllotNewPo2.setCustomerName(customerName);
							leadsAllotNewPo2.setTelephone(telePhone);
							leadsAllotNewPo2.setStatus(Constant.STATUS_DISABLE);
							leadsAllotNewPo2.setIfConfirm(Constant.ADVISER_CONFIRM_02);//顾问确认标志
							leadsAllotNewPo2.setConfirmDate(new Date());
							dao.update(leadsAllotOldPo2, leadsAllotNewPo2);
							
							//标记提醒信息为已完成
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
							
							act.setOutData("leadsCode", leadsCode);
							act.setOutData("leadsAllotId", leadsAllotId);
							doInit();
							act.setForword(taskManageinitUrl);
						} else if("failureRadio".equals(typeFlag)){
							//选择失效后保存
							//修改销售线索主表
							TPcLeadsPO leadsOldPo2 = new TPcLeadsPO();
							leadsOldPo2.setLeadsCode(Long.parseLong(leadsCode));
							TPcLeadsPO leadsNewPo2 = new TPcLeadsPO();
							leadsNewPo2.setLeadsCode(Long.parseLong(leadsCode));
							leadsNewPo2.setCustomerName(customerName);
							leadsNewPo2.setTelephone(telePhone);
							leadsNewPo2.setIntentVehicle(intentVehicle);
							leadsNewPo2.setLeadsStatus(Constant.LEADS_STATUS_03.longValue());//线索状态为 失效线索
							leadsNewPo2.setIfHandle(10041001);
							leadsNewPo2.setUpdateDate(new Date());
							leadsNewPo2.setUpdateBy(logonUser.getUserId());
							dao.update(leadsOldPo2, leadsNewPo2);
							
							//修改销售线索分派表
							TPcLeadsAllotPO leadsAllotOldPo2 = new TPcLeadsAllotPO();
							leadsAllotOldPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
							TPcLeadsAllotPO leadsAllotNewPo2 = new TPcLeadsAllotPO();
							//leadsAllotNewPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
							leadsAllotNewPo2.setCustomerName(customerName);
							leadsAllotNewPo2.setTelephone(telePhone);
							leadsAllotNewPo2.setStatus(Constant.STATUS_DISABLE);
							leadsAllotNewPo2.setIfConfirm(Constant.ADVISER_CONFIRM_02);//顾问确认标志
							leadsAllotNewPo2.setConfirmDate(new Date());
							dao.update(leadsAllotOldPo2, leadsAllotNewPo2);
							
							//标记提醒信息为已完成
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
							
							act.setOutData("leadsCode", leadsCode);
							act.setOutData("leadsAllotId", leadsAllotId);
							doInit();
							act.setForword(taskManageinitUrl);
						}else if("invidateRadio".equals(typeFlag)){
							//选择无效后保存
							//修改销售线索主表
							TPcLeadsPO leadsOldPo2 = new TPcLeadsPO();
							leadsOldPo2.setLeadsCode(Long.parseLong(leadsCode));
							TPcLeadsPO leadsNewPo2 = new TPcLeadsPO();
							leadsNewPo2.setLeadsCode(Long.parseLong(leadsCode));
							leadsNewPo2.setCustomerName(customerName);
							leadsNewPo2.setTelephone(telePhone);
							leadsNewPo2.setIntentVehicle(intentVehicle);
							leadsNewPo2.setLeadsStatus(Constant.LEADS_STATUS_04.longValue());//线索状态为 无效线索
							leadsNewPo2.setIfHandle(10041001);
							leadsNewPo2.setUpdateDate(new Date());
							leadsNewPo2.setUpdateBy(logonUser.getUserId());
							dao.update(leadsOldPo2, leadsNewPo2);
							
							//修改销售线索分派表
							TPcLeadsAllotPO leadsAllotOldPo2 = new TPcLeadsAllotPO();
							leadsAllotOldPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
							TPcLeadsAllotPO leadsAllotNewPo2 = new TPcLeadsAllotPO();
							//leadsAllotNewPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
							leadsAllotNewPo2.setCustomerName(customerName);
							leadsAllotNewPo2.setTelephone(telePhone);
							leadsAllotNewPo2.setStatus(Constant.STATUS_DISABLE);
							leadsAllotNewPo2.setIfConfirm(Constant.ADVISER_CONFIRM_02);//顾问确认标志
							leadsAllotNewPo2.setConfirmDate(new Date());
							dao.update(leadsAllotOldPo2, leadsAllotNewPo2);
							
							//标记提醒信息为已完成
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
							
							
							act.setOutData("leadsCode", leadsCode);
							act.setOutData("leadsAllotId", leadsAllotId);
							doInit();
							act.setForword(taskManageinitUrl);
						}
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
						
						act.setOutData("leadsCode", leadsCode);
						act.setOutData("leadsAllotId", leadsAllotId);
						doInit();
						act.setForword(taskManageinitUrl);
					} else {//发送错误消息到页面
						act.setOutData("errorMsg", "该客户:"+customerName+",已分派其他顾问,保存失败!");
						act.setOutData("customerName", customerName);
						act.setOutData("telephone", telePhone);
						act.setOutData("nowDate", nowDate);
						leadsConfirmByOemInit();
						act.setOutData("leadsCode", leadsCode);
						act.setOutData("leadsAllotId", leadsAllotId);
//						act.setForword(leadsConfirmByOemUrl);
					}
					//未存在客户档案信息的
				} else if("followRadio".equals(typeFlag) || "inviteRadio".equals(typeFlag) || "orderRadio".equals(typeFlag)){
					remark=remark+"-未建档";
					leadsNewPoByOem.setFailureRemark(remark);
					dao.update(leadsOldPoByOem, leadsNewPoByOem);
					
					//选择跟进或邀约后保存
					//新增客户档案
					TPcCustomerPO customerPo = new TPcCustomerPO();
					String customerId = SequenceManager.getSequence("");
					String customerCode = SequenceManager.getCtmNo();
					customerPo.setCustomerId(Long.parseLong(customerId));
					customerPo.setCustomerCode(customerCode);
					customerPo.setCustomerName(customerName);
					customerPo.setTelephone(telePhone);
					customerPo.setLeadsOrigin(leadsOrigin);
					customerPo.setJcWay(collectFashion);
					customerPo.setBuyBudget(buyBudget);
					customerPo.setCtmProp(customerType);
					customerPo.setIntentVehicle(intentVehicle);
					customerPo.setFirstContactTime(sdf.parse(followDate));
					customerPo.setCtmRank(intentType);
					customerPo.setProviceId(dPro);
					customerPo.setCityId(dCity);
					customerPo.setTownId(dArea);
					if("followRadio".equals(typeFlag)) {//点跟进时
						customerPo.setNextFollowTime(sdf.parse(nextFollowDate));//下次跟进时间
					} else if("inviteRadio".equals(typeFlag)) {//点邀约时
						customerPo.setNextFollowTime(sdf.parse(planInviteDate));//下次跟进时间
						customerPo.setPreShopTime(sdf.parse(planMeetDate));//预计到店时间
					} else if("inviteRadio".equals(typeFlag)) {//点订单时
						customerPo.setNextFollowTime(sdf.parse(orderDate));//下次订单时间
					}
					customerPo.setCtmStatus(Constant.CTM_STATUS_01.toString());//客户状态
					customerPo.setStatus(Constant.STATUS_ENABLE.longValue());
					customerPo.setCreateDate(new Date());
					customerPo.setCreateBy(logonUser.getUserId());
					customerPo.setDealerId(Long.parseLong(logonUser.getDealerId()));
					customerPo.setAdviser(logonUser.getUserId());
					customerPo.setSalesProgress(salesProgress);
					dao.insert(customerPo);
					
					//修改销售线索主表
					TPcLeadsPO leadsOldPo = new TPcLeadsPO();
					leadsOldPo.setLeadsCode(Long.parseLong(leadsCode));
					TPcLeadsPO leadsNewPo = new TPcLeadsPO();
					leadsNewPo.setLeadsCode(Long.parseLong(leadsCode));
					leadsNewPo.setCustomerName(customerName);
					leadsNewPo.setCustomerId(Long.parseLong(customerId));
					leadsNewPo.setTelephone(telePhone);
					leadsNewPo.setIntentVehicle(intentVehicle);
					leadsNewPo.setIfHandle(10041001);
					leadsNewPo.setUpdateDate(new Date());
					leadsNewPo.setUpdateBy(logonUser.getUserId());
					dao.update(leadsOldPo, leadsNewPo);
					
					//修改销售线索分派表
					TPcLeadsAllotPO leadsAllotOldPo = new TPcLeadsAllotPO();
					leadsAllotOldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					TPcLeadsAllotPO leadsAllotNewPo = new TPcLeadsAllotPO();
					//leadsAllotNewPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					leadsAllotNewPo.setCustomerName(customerName);
					leadsAllotNewPo.setCustomerId(Long.parseLong(customerId));
					leadsAllotNewPo.setTelephone(telePhone);
					leadsAllotNewPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);//顾问确认标志
					leadsAllotNewPo.setConfirmDate(new Date());
					dao.update(leadsAllotOldPo, leadsAllotNewPo);
					
					String followId = SequenceManager.getSequence("");//本次跟进任务ID
					Long nextTaskId = Long.parseLong(SequenceManager.getSequence(""));//下次任务ID
					//新增本次跟进任务表
					TPcFollowPO follow0Po = new TPcFollowPO();
					follow0Po.setFollowId(Long.parseLong(followId));
					follow0Po.setCustomerId(Long.parseLong(customerId));
					follow0Po.setFollowDate(new Date());
					follow0Po.setOldLevel(null);
					follow0Po.setNewLevel(intentType);
					follow0Po.setSalesProgress(salesProgress);
					follow0Po.setFollowInfo(followInfo);
					follow0Po.setTaskStatus(Constant.TASK_STATUS_02);
					follow0Po.setFinishDate(new Date());
					follow0Po.setFollowType(Constant.FOLLOW_TYPE_01.toString());
					follow0Po.setCreateDate(new Date());
					follow0Po.setCreateBy(logonUser.getUserId().toString());
					follow0Po.setNextTask(nextTaskId);
					dao.insert(follow0Po);
					
					//增加接触点信息
					CommonUtils.addContackPoint(Constant.POINT_WAY_03, followInfo, customerId, logonUser.getUserId().toString(), logonUser.getDealerId());
					
					if("followRadio".equals(typeFlag)) {//点跟进时
						remark=remark+"-跟进";
						leadsNewPoByOem.setFailureRemark(remark);
						dao.update(leadsOldPoByOem, leadsNewPoByOem);
						
						//新增下次跟进任务表
						TPcFollowPO followPo = new TPcFollowPO();
						followPo.setFollowId(nextTaskId);
						followPo.setCustomerId(Long.parseLong(customerId));
						followPo.setFollowDate(sdf.parse(nextFollowDate));
						followPo.setOldLevel(intentType);
						followPo.setNewLevel(null);
						followPo.setTaskStatus(Constant.TASK_STATUS_01);
						followPo.setFollowType(followType);
						followPo.setFollowPlan(followPlan);
						followPo.setCreateDate(new Date());
						followPo.setCreateBy(logonUser.getUserId().toString());
						followPo.setPreviousTask(Long.parseLong(followId));
						followPo.setOldSalesProgress(salesProgress);
						dao.insert(followPo);
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), nextFollowDate,"");
					} else if("inviteRadio".equals(typeFlag)) {//点邀约时
						remark=remark+"-邀约";
						leadsNewPoByOem.setFailureRemark(remark);
						dao.update(leadsOldPoByOem, leadsNewPoByOem);
						
						//新增下次计划邀约任务表
						TPcInvitePO invitePo = new TPcInvitePO();
						invitePo.setInviteId(nextTaskId);
						invitePo.setCustomerId(Long.parseLong(customerId));
						invitePo.setCreateDate(new Date());
						invitePo.setCreateBy(logonUser.getUserId().toString());
						invitePo.setPlanInviteDate(sdf.parse(planInviteDate));
						invitePo.setPlanMeetDate(sdf.parse(planMeetDate));
						invitePo.setTaskStatus(Constant.TASK_STATUS_01);
						invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_04);
						invitePo.setIfPlan(Constant.IF_TYPE_NO);
						invitePo.setOldLevel(intentType);
						invitePo.setNewLevel(null);
						invitePo.setInviteType(Integer.parseInt(inviteType));
						invitePo.setPreviousTask(Long.parseLong(followId));
						invitePo.setOldSalesProgress(salesProgress);
						//判断是否做了计划（4个框有值说明为做了计划）
						if(xqfx!=null&&!"".equals(xqfx)) {
							invitePo.setIfPlan(Constant.IF_TYPE_YES);
							invitePo.setRequirement(xqfx);
							invitePo.setInviteTarget(yymb);
							invitePo.setTrustDesign(ydkhxrsj);
							invitePo.setSceneDesign(gdkhqjsj);
							invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_01);
							//标记提醒信息为已完成
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_15.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						} else {
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), planInviteDate,"");
						}
						dao.insert(invitePo);
						
						
					} else if("orderRadio".equals(typeFlag)) {//点订单时
						remark=remark+"-订单";
						leadsNewPoByOem.setFailureRemark(remark);
						dao.update(leadsOldPoByOem, leadsNewPoByOem);
						
						//新增下次订单任务表
						//新增下次订单任务
						TPcOrderPO orderPo = new TPcOrderPO();
						orderPo.setOrderId(nextTaskId);
						orderPo.setCustomerId(Long.parseLong(customerId));
						orderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
						orderPo.setOldLevel(intentType);
						orderPo.setCreateDate(new Date());
						orderPo.setCreateBy(logonUser.getUserId().toString());
						orderPo.setOldSalesProgress(salesProgress);
						orderPo.setPreviousTask(Long.parseLong(followId));
						orderPo.setTaskStatus(Constant.TASK_STATUS_01);
						orderPo.setOrderDate(sdf.parse(orderDate));
						dao.insert(orderPo);
						
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_08.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), orderDate,"");
					}
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_01.toString());
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
					
					act.setOutData("leadsCode", leadsCode);
					act.setOutData("leadsAllotId", leadsAllotId);
					doInit();
					act.setForword(taskManageinitUrl);
				} else if("defeatRadio".equals(typeFlag)){
					remark=remark+"-战败";
					leadsNewPoByOem.setFailureRemark(remark);
					dao.update(leadsOldPoByOem, leadsNewPoByOem);
					
					//选择战败后保存
					//修改销售线索主表
					TPcLeadsPO leadsOldPo = new TPcLeadsPO();
					leadsOldPo.setLeadsCode(Long.parseLong(leadsCode));
					TPcLeadsPO leadsNewPo = new TPcLeadsPO();
					leadsNewPo.setLeadsCode(Long.parseLong(leadsCode));
					leadsNewPo.setCustomerName(customerName);
					leadsNewPo.setTelephone(telePhone);
					leadsNewPo.setIntentVehicle(intentVehicle);
					leadsNewPo.setLeadsStatus(Constant.LEADS_STATUS_02.longValue());//线索状态为 战败线索
					leadsNewPo.setDefeatModel(defeatVehicle);
					leadsNewPo.setDefeatReason(defeatReason);
					leadsNewPo.setDefaultRemark(defeatInfo);
					leadsNewPo.setIfHandle(10041001);
					leadsNewPo.setUpdateDate(new Date());
					leadsNewPo.setUpdateBy(logonUser.getUserId());
					dao.update(leadsOldPo, leadsNewPo);
					
					//修改销售线索分派表
					TPcLeadsAllotPO leadsAllotOldPo = new TPcLeadsAllotPO();
					leadsAllotOldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					TPcLeadsAllotPO leadsAllotNewPo = new TPcLeadsAllotPO();
					//leadsAllotNewPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					leadsAllotNewPo.setCustomerName(customerName);
					leadsAllotNewPo.setTelephone(telePhone);
					leadsAllotNewPo.setStatus(Constant.STATUS_DISABLE);
					leadsAllotNewPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);
					leadsAllotNewPo.setConfirmDate(new Date());
					dao.update(leadsAllotOldPo, leadsAllotNewPo);
					
					//插入战败记录
					String failureId = SequenceManager.getSequence("");
					TPcDefeatfailurePO tdp = new TPcDefeatfailurePO();
					tdp.setDefeatfailureId(Long.parseLong(failureId));
					tdp.setDefeatfailureType(Constant.FAILURE_TYPE_01);
					tdp.setDefeatEndDate(new Date());
					tdp.setStatus(Constant.STATUS_ENABLE);
					tdp.setSalesProgress(Constant.BUY_TYPE2_01+"");
					tdp.setCreateBy(logonUser.getUserId()+"");
					tdp.setCreateDate(new Date());
					tdp.setDefeatModel(leadsNewPo.getDefeatModel());
					tdp.setReasonAnalysis(defeatInfo);
					tdp.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
					tdp.setCustomerId(0L);
					tdp.setLeadsCode(leadsOldPo.getLeadsCode());
					dao.insert(tdp);
					
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
					
					act.setOutData("leadsCode", leadsCode);
					act.setOutData("leadsAllotId", leadsAllotId);
					doInit();
					act.setForword(taskManageinitUrl);
				} else if("failureRadio".equals(typeFlag)){
					remark=remark+"-失效";
					leadsNewPoByOem.setFailureRemark(remark);
					dao.update(leadsOldPoByOem, leadsNewPoByOem);
					
					//选择失效后保存
					//修改销售线索主表
					TPcLeadsPO leadsOldPo = new TPcLeadsPO();
					leadsOldPo.setLeadsCode(Long.parseLong(leadsCode));
					TPcLeadsPO leadsNewPo = new TPcLeadsPO();
					leadsNewPo.setLeadsCode(Long.parseLong(leadsCode));
					leadsNewPo.setCustomerName(customerName);
					leadsNewPo.setTelephone(telePhone);
					leadsNewPo.setIntentVehicle(intentVehicle);
					leadsNewPo.setLeadsStatus(Constant.LEADS_STATUS_03.longValue());//线索状态为 失效线索
					leadsNewPo.setIfHandle(10041001);
					leadsNewPo.setUpdateDate(new Date());
					leadsNewPo.setUpdateBy(logonUser.getUserId());
					dao.update(leadsOldPo, leadsNewPo);
					
					//修改销售线索分派表
					TPcLeadsAllotPO leadsAllotOldPo = new TPcLeadsAllotPO();
					leadsAllotOldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					TPcLeadsAllotPO leadsAllotNewPo = new TPcLeadsAllotPO();
					//leadsAllotNewPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					leadsAllotNewPo.setCustomerName(customerName);
					leadsAllotNewPo.setTelephone(telePhone);
					leadsAllotNewPo.setStatus(Constant.STATUS_DISABLE);
					
					leadsAllotNewPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);
					leadsAllotNewPo.setConfirmDate(new Date());
					//leadsAllotNewPo.setCustomerId(Long.parseLong(customerId));
					dao.update(leadsAllotOldPo, leadsAllotNewPo);
					
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_01.toString());
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
					
					act.setOutData("leadsCode", leadsCode);
					act.setOutData("leadsAllotId", leadsAllotId);
					doInit();
					act.setForword(taskManageinitUrl);
				}else if("invidateRadio".equals(typeFlag)){
					remark=remark+"-无效";
					leadsNewPoByOem.setFailureRemark(remark);
					dao.update(leadsOldPoByOem, leadsNewPoByOem);
					
					//选择无效后保存
					//修改销售线索主表
					TPcLeadsPO leadsOldPo2 = new TPcLeadsPO();
					leadsOldPo2.setLeadsCode(Long.parseLong(leadsCode));
					TPcLeadsPO leadsNewPo2 = new TPcLeadsPO();
					leadsNewPo2.setLeadsCode(Long.parseLong(leadsCode));
					leadsNewPo2.setCustomerName(customerName);
					leadsNewPo2.setTelephone(telePhone);
					leadsNewPo2.setIntentVehicle(intentVehicle);
					leadsNewPo2.setLeadsStatus(Constant.LEADS_STATUS_04.longValue());//线索状态为 无效线索
					leadsNewPo2.setIfHandle(10041001);
					leadsNewPo2.setUpdateDate(new Date());
					leadsNewPo2.setUpdateBy(logonUser.getUserId());
					dao.update(leadsOldPo2, leadsNewPo2);
					
					//修改销售线索分派表
					TPcLeadsAllotPO leadsAllotOldPo2 = new TPcLeadsAllotPO();
					leadsAllotOldPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
					TPcLeadsAllotPO leadsAllotNewPo2 = new TPcLeadsAllotPO();
					//leadsAllotNewPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
					leadsAllotNewPo2.setCustomerName(customerName);
					leadsAllotNewPo2.setTelephone(telePhone);
					leadsAllotNewPo2.setStatus(Constant.STATUS_DISABLE);
					leadsAllotNewPo2.setIfConfirm(Constant.ADVISER_CONFIRM_02);//顾问确认标志
					leadsAllotNewPo2.setConfirmDate(new Date());
					dao.update(leadsAllotOldPo2, leadsAllotNewPo2);
					
					TPcRemindPO oldRemindPo = new TPcRemindPO();
					oldRemindPo.setBeremindId(Long.parseLong(leadsAllotId));
					oldRemindPo.setRemindType(Constant.REMIND_TYPE_03.toString());
					TPcRemindPO newRemindPo = new TPcRemindPO();
					//newRemindPo.setBeremindId(Long.parseLong(beRemindId));
					newRemindPo.setRemindStatus(Constant.TASK_STATUS_02);
					//newRemindPo.setRemindType(remindType);
					dao.update(oldRemindPo,newRemindPo);
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_01.toString());
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
					
					act.setOutData("leadsCode", leadsCode);
					act.setOutData("leadsAllotId", leadsAllotId);
					doInit();
					act.setForword(taskManageinitUrl);
				} else { //已处理的情况
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
					act.setOutData("leadsCode", leadsCode);
					act.setOutData("leadsAllotId", leadsAllotId);
					doInit();
					act.setForword(taskManageinitUrl);
				}
			}else { //已处理的情况
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
				act.setOutData("leadsCode", leadsCode);
				act.setOutData("leadsAllotId", leadsAllotId);
				doInit();
				act.setForword(taskManageinitUrl);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * DCRC导入线索顾问确认页面
	 */
	public void leadsConfirmByDcrcInit() {
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		List a[] = new List[10]; 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String leadsCode = act.getRequest().getParamValue("leadsCode");
		String leadsAllotId = act.getRequest().getParamValue("leadsAllotId");
		String dPro = CommonUtils.checkNull(act.getRequest().getParamValue("dPro"));
		String dCity = CommonUtils.checkNull(act.getRequest().getParamValue("dCity"));
		String dArea = CommonUtils.checkNull(act.getRequest().getParamValue("dArea"));
		String customerName = null;
		String telephone = null;
		String buyBudget=null;
		String buyBudget2=null;
		String testDriving=null;
		String testDriving2=null;
		String buyType=null;
		String buyType2=null;
		String customerType=null;
		String customerType2=null;
		String seriesCode = null;
		String oldCustomerName=null;  
		String oldTelephone=null;
		String oldVehicleId=null;
		//String intentType=null;
		//String intentType2=null;
		String adviserLogon = "no";//顾问登陆标志
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			//从线索中获取线索来源
			if(leadsCode=="null"||"null".equals(leadsCode)) {
				leadsCode = "";
			}
			List<DynaBean> leadsList = dao.getNameAndTelephone(leadsCode);
			String leadsOrigin = null;
			String sex = null;
			String jcWay = null;
			String jcWayCode = null;
			Iterator it2 = (Iterator) leadsList.iterator();
			while(it2.hasNext()) {
				DynaBean db = (DynaBean)it2.next();
				leadsOrigin = db.get("LEADS_ORIGIN").toString();
				if(db.get("SEX")!=null && !"".equals(db.get("SEX"))) {
					sex = db.get("SEX").toString();
				}
				if(db.get("JC_WAY")!=null && !"".equals(db.get("JC_WAY"))) {
					jcWay = db.get("JC_WAY").toString();
				}
				if(db.get("JC_WAY_CODE")!=null && !"".equals(db.get("JC_WAY_CODE"))) {
					jcWayCode = db.get("JC_WAY_CODE").toString();
				}
				if(db.get("INTENT_VEHICLE")!=null && !"".equals(db.get("INTENT_VEHICLE"))) {
					seriesCode= db.get("INTENT_VEHICLE").toString();
				}
				if(db.get("OLD_CUSTOMER_NAME")!=null && !"".equals(db.get("OLD_CUSTOMER_NAME"))) {
					oldCustomerName= db.get("OLD_CUSTOMER_NAME").toString();
				}
				if(db.get("OLD_TELEPHONE")!=null && !"".equals(db.get("OLD_TELEPHONE"))) {
					oldTelephone= db.get("OLD_TELEPHONE").toString();
				}
				if(db.get("OLD_VEHICLE_ID")!=null && !"".equals(db.get("OLD_VEHICLE_ID"))) {
					oldVehicleId= db.get("OLD_VEHICLE_ID").toString();
				}
			}
			//从线索分派表中获取信息
			List<DynaBean> leadsAllotList = dao.getLeadsAllotInfo(leadsAllotId);
			String x1 = null;//线索编码
			String x2 = null;//线索来源
			String x3 = null;//来店时间
			String x4 = null;//离店时间
			String x5 = null;//销售顾问
			String x6 = null;//客户描述
			Iterator it = (Iterator) leadsAllotList.iterator();
			while(it.hasNext()) {
				DynaBean db = (DynaBean)it.next();
				x1 = db.get("LEADS_CODE").toString();
				if(leadsCode==null || "".equals(leadsCode) || "null".equals(leadsCode)) {
					leadsCode = x1;
				}
				x2 = db.get("CODE_DESC").toString();
				if(db.get("COME_DATE")==null||"".equals(db.get("COME_DATE"))) {
					x3 = "";
				} else {
					x3 = db.get("COME_DATE").toString();
				}
				if(db.get("LEAVE_DATE")==null||"".equals(db.get("LEAVE_DATE"))) {
					x4 = "";
				} else {
					x4 = db.get("LEAVE_DATE").toString();
				}
				x5 = db.get("NAME").toString();
				if(db.get("CUSTOMER_DESCRIBE")==null) {
					x6 = "";
				} else {
					x6 = db.get("CUSTOMER_DESCRIBE").toString();
				}
				if(db.get("CUSTOMER_NAME")==null) {
					customerName = "";
				} else {
					customerName = db.get("CUSTOMER_NAME").toString();
				}
				if(db.get("TELEPHONE")==null) {
					telephone = "";
				} else {
					telephone = db.get("TELEPHONE").toString();
				}
			}
			//获取意向车型一级列表
			List<DynaBean> menusAList = dao.getIntentVehicleA();
			//获取意向车型二级列表
			List<DynaBean> menusBList = dao.getIntentVehicleB();
			//获取战败车型一级列表
			List<DynaBean> menusAList2 = dao.getDefeatVehicleA();
			//获取战败车型二级列表
			List<DynaBean> menusBList2 = dao.getDefeatVehicleB();
			//获取战败原因一级列表
			List<DynaBean> menusAList3 = dao.getDefeatReasonA();
			//获取战败原因二级列表
			List<DynaBean> menusBList3 = dao.getDefeatReasonB();
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			    adviserLogon = "yes";
			}
			//获取客户基本信息
			List<DynaBean> customerList = dao.getCustomerInfoByLeads(leadsCode);
			//获取客户信息的意向车型和意向等级和销售流程进度
			DynaBean db = customerList.get(0);
			
			if(db.get("INTENT_VEHICLE")!=null && !"".equals(db.get("INTENT_VEHICLE"))) {
				seriesCode = db.get("INTENT_VEHICLE").toString();
			}
			if(db.get("BUY_BUDGET")!=null&&!"".equals(db.get("BUY_BUDGET"))) {
				buyBudget = db.get("BUY_BUDGET").toString();
			} else {
				buyBudget = "";
			}
			if(db.get("BUY_BUDGET2")!=null&&!"".equals(db.get("BUY_BUDGET2"))) {
				buyBudget2 = db.get("BUY_BUDGET2").toString();
			} else {
				buyBudget2 = "";
			}
			if(db.get("TEST_DRIVING")!=null&&!"".equals(db.get("TEST_DRIVING"))) {
				testDriving = db.get("TEST_DRIVING").toString();
			} else {
				testDriving = "";
			}
			if(db.get("TEST_DRIVING2")!=null&&!"".equals(db.get("TEST_DRIVING2"))) {
				testDriving2 = db.get("TEST_DRIVING2").toString();
			} else {
				testDriving2= "";
			}
			
			if(db.get("BUY_TYPE")!=null&&!"".equals(db.get("BUY_TYPE"))) {
				buyType = db.get("BUY_TYPE").toString();
			} else {
				buyType = "";
			}
			if(db.get("BUY_TYPE2")!=null&&!"".equals(db.get("BUY_TYPE2"))) {
				buyType2 = db.get("BUY_TYPE2").toString();
			} else {
				buyType2 = "";
			}
			if(db.get("CUSTOMER_TYPE")!=null&&!"".equals(db.get("CUSTOMER_TYPE"))) {
				customerType = db.get("CUSTOMER_TYPE").toString();
			} else {
				customerType = "";
			}
			if(db.get("CUSTOMER_TYPE2")!=null&&!"".equals(db.get("CUSTOMER_TYPE2"))) {
				customerType2 = db.get("CUSTOMER_TYPE2").toString();
			} else {
				customerType2 = "";
			}
			/*
			if(db.get("INTENT_TYPE")!=null&&!"".equals(db.get("INTENT_TYPE"))) {
				intentType = db.get("INTENT_TYPE").toString();
			} else {
				intentType = "";
			}
			if(db.get("INTENT_TYPE2")!=null&&!"".equals(db.get("INTENT_TYPE2"))) {
				intentType2 = db.get("INTENT_TYPE2").toString();
			} else {
				intentType2 = "";
			}
			*/
			//获取一级意向车型对应二级列表
			List<DynaBean> menusABList = null;
			String upSeriesCode = null;
			List<DynaBean> menusABList2 = null;
			if(seriesCode!=null&&!"".equals(seriesCode)) {
				menusABList = dao.getIntentVehicleAB(seriesCode);
				DynaBean db2 = menusABList.get(0);
				upSeriesCode = db2.get("PARENTID").toString();
				menusABList2 = dao.getIntentVehicleAB2(upSeriesCode);
			}
//			//获取一级意向车型对应二级列表
//			List<DynaBean> menusABList = dao.getIntentVehicleAB(seriesCode);
//			DynaBean db2 = menusABList.get(0);
//			String upSeriesCode = db2.get("PARENTID").toString();
//			List<DynaBean> menusABList2 = dao.getIntentVehicleAB2(upSeriesCode);
			
			act.setOutData("adviserLogon", adviserLogon);
			act.setOutData("leadsAllotId", leadsAllotId);
			act.setOutData("leadsCode", leadsCode);
			act.setOutData("nowDate", nowDate);
			act.setOutData("leadsAllotList", leadsAllotList);
			act.setOutData("x1", x1);
			act.setOutData("x2", x2);
			act.setOutData("x3", x3);
			act.setOutData("x4", x4);
			act.setOutData("x5", x5);
			act.setOutData("x6", x6);
			act.setOutData("customerNameN", customerName);
			act.setOutData("upSeriesCode", upSeriesCode);
			act.setOutData("seriesCode", seriesCode);
			act.setOutData("telephoneN", telephone);
			act.setOutData("leadsOrigin", leadsOrigin);
			act.setOutData("sex", sex);
			act.setOutData("jc_way", jcWay);
			act.setOutData("jc_way_code", jcWayCode);
			act.setOutData("oldCustomerName",oldCustomerName);  
			act.setOutData("oldTelephone",oldTelephone);
			act.setOutData("oldVehicleId",oldVehicleId);
			act.setOutData("customerList", customerList);
			act.setOutData("menusAList", menusAList);
			act.setOutData("menusBList", menusBList);
			act.setOutData("menusAList2", menusAList2);
			act.setOutData("menusBList2", menusBList2);
			act.setOutData("menusAList3", menusAList3);
			act.setOutData("menusBList3", menusBList3);
			act.setOutData("menusABList2", menusABList2);
			act.setOutData("buyType", buyType);
			act.setOutData("buyType2", buyType2);
			act.setOutData("buyBudget", buyBudget);
			act.setOutData("buyBudget2", buyBudget2);
			act.setOutData("testDriving", testDriving);
			act.setOutData("testDriving2", testDriving2);
			act.setOutData("customerType", customerType);
			act.setOutData("customerType2", customerType2);
			/*
			act.setOutData("intentType", intentType);
			act.setOutData("intentType2", intentType2);
			*/
			act.setForword(leadsConfirmByDcrcUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"DCRC导入线索顾问确认页面!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * DCRC导入线索顾问确认
	 */
	public void leadsConfirmByDcrc() {
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		OemLeadsManageDao dao2 = new OemLeadsManageDao();
		List a[] = new List[10]; 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String leadsCode = act.getRequest().getParamValue("leadsCode");
		String leadsAllotId = act.getRequest().getParamValue("leadsAllotId");
		String typeFlag = act.getRequest().getParamValue("typeFlag");
		String customerName = act.getRequest().getParamValue("customer_name");//客户姓名
		String telePhone = act.getRequest().getParamValue("telephone");//联系电话
		String oldCustomerName=act.getRequest().getParamValue("old_customer_name");//老客户姓名
		String oldTelephone=act.getRequest().getParamValue("old_telephone");//老客户联系电话
		String oldVehicleId=act.getRequest().getParamValue("old_vehicle_id");//老客户车架号
		String comeDate = act.getRequest().getParamValue("x3");//来店时间
		String collectFashion = act.getRequest().getParamValue("collect_fashion");//集客方式
		String buyBudget = act.getRequest().getParamValue("buy_budget");//购车预算
		String customerType = act.getRequest().getParamValue("customer_type");//客户类型(有望/无望)
		String intentVehicle = act.getRequest().getParamValue("intentVehicle");//意向车型
		String intentType = act.getRequest().getParamValue("intent_type");//意向等级
		String salesProgress = act.getRequest().getParamValue("sales_progress");//销售流程进度
		String followInfo = act.getRequest().getParamValue("follow_info");//跟进说明
		String leadsOrigin = act.getRequest().getParamValue("leadsOrigin");//线索来源
		String sex = act.getRequest().getParamValue("sex");//客户性别
		String comeReason = act.getRequest().getParamValue("come_reason");//来店契机
		String testDriving = act.getRequest().getParamValue("test_driving");//是否试乘试驾
		String buyType = act.getRequest().getParamValue("buy_type");//购买类型
		String nextFollowDate = act.getRequest().getParamValue("next_follow_date");//下次跟进时间
		String followType = act.getRequest().getParamValue("follow_type");//跟进方式
		String followPlan = act.getRequest().getParamValue("follow_plan");//跟进计划
		String xqfx = act.getRequest().getParamValue("xqfx"); //需求分析
		String yymb = act.getRequest().getParamValue("yymb"); //邀约目标
		String ydkhxrsj = act.getRequest().getParamValue("ydkhxrsj"); //赢得客户信任设计
		String gdkhqjsj = act.getRequest().getParamValue("gdkhqjsj"); //感动客户情景设计
		String planInviteDate = act.getRequest().getParamValue("plan_invite_date"); //计划邀约时间
		String planMeetDate = act.getRequest().getParamValue("plan_meet_date"); //计划见面时间
		String inviteType = act.getRequest().getParamValue("invite_type"); //邀约方式
		String orderDate = act.getRequest().getParamValue("order_date"); //订车时间
		String defeatVehicle = act.getRequest().getParamValue("defeatVehicleB");//战败车型
		String defeatReason = act.getRequest().getParamValue("defeatReasonB");//战败原因
		String defeatInfo = act.getRequest().getParamValue("defeat_info");//战败说明
		
		//9：00-17:00之间分配给经销商的公司类信息，主管当日19:00前完成分配，顾问必须在当日完成处理
		//17:00-次日9:00分配给经销商的公司类信息，主管次日11:00前完成分配，顾问必须在次日12:00前完成处理。
		Date d1 = new Date(); //当前时间
		String d2 = "090000";
		String d3 = "170000";
		String remindDate = null;
		SimpleDateFormat f = new SimpleDateFormat("hhmmss"); //格式化为 hhmmss
		int d1Number = Integer.parseInt(f.format(d1).toString()); //将第一个时间格式化后转为int
		int d2Number = Integer.parseInt(d2); //将第二个时间格式化后转为int
		int d3Number = Integer.parseInt(d3); //将第三个时间格式化后转为int
		
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
							
		if(d1Number>=d2Number && d1Number<=d3Number){
			remindDate = f1.format(d1);
		} else {
			remindDate = f1.format(c.getTime());
		}
		
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			
			//PC和移动端同时处理问题
			int ifHandle = CommonUtils.getTheSameData(leadsCode);
			if(ifHandle==10041002) {//未处理的情况
				String remark="DCRC线索方法ByDcrc";
				TPcLeadsPO leadsOldPoByDcrc = new TPcLeadsPO();
				leadsOldPoByDcrc.setLeadsCode(Long.parseLong(leadsCode));
				TPcLeadsPO leadsNewPoByDcrc = new TPcLeadsPO();
				leadsNewPoByDcrc.setFailureRemark(remark);
				dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
				//验证是否已建立了档案（根据客户名称、联系电话和经销商ID）
				List<Map<String, Object>> getHasList = null;
				List<DynaBean> getHasList2 = null;
				getHasList=dao2.getHasCustomer(telePhone,customerName,logonUser.getDealerId());
				//已存在档案，(判断是否为本顾问的客户,是就修改客户档案.否发送错误消息到页面)
				if(getHasList.size()>0 && (typeFlag!=null&&!"".equals(typeFlag)&&!"''".equals(typeFlag))) {
					remark=remark+"-已建档";
					leadsNewPoByDcrc.setFailureRemark(remark);
					dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
					//判断是否为本顾问的客户
					getHasList2 = dao2.getHasCustomer2(telePhone,customerName,logonUser.getDealerId(),logonUser.getUserId().toString());
					if(getHasList2.size()>0) {// 修改客户档案信息,并标记该线索为已确认
						// 修改客户档案信息
						TPcCustomerPO oldCustomerPo = new TPcCustomerPO();
						//得到需修改客户的CUSTOMERID
						DynaBean db = getHasList2.get(0);
						String customerId = db.get("CUSTOMER_ID").toString();
						String adviser = db.get("ADVISER").toString();
						String dealerId = db.get("DEALER_ID").toString();
						String ctmType = "";
						if(db.get("CTM_TYPE")!=null) {
							ctmType = db.get("CTM_TYPE").toString();
						}
						oldCustomerPo.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo = new TPcCustomerPO();
						newCustomerPo.setCustomerId(Long.parseLong(customerId));
						newCustomerPo.setJcWay(collectFashion);
						newCustomerPo.setUpdateDate(new Date());
						newCustomerPo.setUpdateBy(logonUser.getUserId());
						newCustomerPo.setBuyBudget(buyBudget);
						newCustomerPo.setCtmProp(customerType);
						newCustomerPo.setIntentVehicle(intentVehicle);
						newCustomerPo.setCtmRank(intentType);
						newCustomerPo.setSalesProgress(salesProgress);
						newCustomerPo.setIfDrive(Long.parseLong(testDriving));
						newCustomerPo.setComeReason(comeReason);
						newCustomerPo.setBuyType(buyType);
						// update by yinshunhui 解决线索战败后新来重复线索重启失败
						if("orderRadio".equals(typeFlag)||"followRadio".equals(typeFlag)||"inviteRadio".equals(typeFlag)){
							if("60341001".equals(ctmType)){
								newCustomerPo.setCtmType(Constant.CTM_TYPE_03.toString());
							} else if("60341004".equals(ctmType) || "60341005".equals(ctmType)) {
								newCustomerPo.setCtmType(Constant.CTM_TYPE_02.toString());
							}
						}
						if("60151011".equals(leadsOrigin)){
							CommonUtils.addComeCount(new Long(customerId));
						}
						
						// end
						dao.update(oldCustomerPo, newCustomerPo);
						
						//当集客方式为老客户转介绍的时候添加老客户与车主关系信息
						if("60021008".equals(collectFashion))
						{			
							TPcLinkManPO linkMan=new TPcLinkManPO();
							String linkId = SequenceManager.getSequence("");
							linkMan.setLinkId(Long.parseLong(linkId));
							linkMan.setLinkMan(oldCustomerName.trim());
							linkMan.setLinkPhone(oldTelephone.trim());
							linkMan.setCreateDate(new Date());
							linkMan.setRelationship("老客户推荐");
							linkMan.setCtmId(Long.parseLong(customerId));
							linkMan.setStatus(new Long(Constant.STATUS_ENABLE));
							linkMan.setOldVehicleId(oldVehicleId.trim());
							linkMan.setRelationCode(Constant.linkMan_status_01.toString());
							dao.insert(linkMan);
						}
						//当集客方式为情报转介绍的时候添加老客户与车主关系信息
						if("60021004".equals(collectFashion))
						{			
							TPcLinkManPO linkMan=new TPcLinkManPO();
							String linkId = SequenceManager.getSequence("");
							linkMan.setLinkId(Long.parseLong(linkId));
							linkMan.setLinkMan(oldCustomerName.trim());
							linkMan.setLinkPhone(oldTelephone.trim());
							linkMan.setCreateDate(new Date());
							linkMan.setRelationship("情报转介绍");
							linkMan.setCtmId(Long.parseLong(customerId));
							linkMan.setStatus(new Long(Constant.STATUS_ENABLE));
							linkMan.setRelationCode(Constant.linkMan_status_02.toString());
							dao.insert(linkMan);
						}
						//标记该线索为已确认
						//修改销售线索主表
						TPcLeadsPO leadsOldPo = new TPcLeadsPO();
						leadsOldPo.setLeadsCode(Long.parseLong(leadsCode));
						TPcLeadsPO leadsNewPo = new TPcLeadsPO();
						leadsNewPo.setLeadsCode(Long.parseLong(leadsCode));
						leadsNewPo.setCustomerName(customerName);
						leadsNewPo.setCustomerId(Long.parseLong(customerId));
						leadsNewPo.setTelephone(telePhone);
						leadsNewPo.setIntentVehicle(intentVehicle);
						leadsNewPo.setJcWay(collectFashion);
						leadsNewPo.setIfHandle(10041001);
						leadsNewPo.setUpdateDate(new Date());
						leadsNewPo.setUpdateBy(logonUser.getUserId());
						dao.update(leadsOldPo, leadsNewPo);
						
						//修改销售线索分派表
						TPcLeadsAllotPO leadsAllotOldPo = new TPcLeadsAllotPO();
						leadsAllotOldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						TPcLeadsAllotPO leadsAllotNewPo = new TPcLeadsAllotPO();
						//leadsAllotNewPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						leadsAllotNewPo.setCustomerName(customerName);
						leadsAllotNewPo.setCustomerId(Long.parseLong(customerId));
						leadsAllotNewPo.setTelephone(telePhone);
						leadsAllotNewPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);//顾问确认标志
						leadsAllotNewPo.setConfirmDate(new Date());
						dao.update(leadsAllotOldPo, leadsAllotNewPo);
						
						if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
							// update by yinshunhui
//							TPcLeadsPO leadsPo = new TPcLeadsPO();
//							leadsPo.setLeadsCode(Long.parseLong(leadsCode));
//							leadsPo=(TPcLeadsPO) dao.select(leadsPo).get(0);
//							String leadType=leadsPo.getLeadsType().toString();
//							if(Constant.LEADS_TYPE_03.toString().equals(leadType)){
								CommonUtils.addContackPoint(Constant.POINT_WAY_02, "经销商类重复线索", customerId, adviser, dealerId);
//							}else if(Constant.LEADS_TYPE_02.toString().equals(leadType)){
//								CommonUtils.addContackPoint(Constant.POINT_WAY_02, "经销商类重复线索", customerId, adviser, dealerId);
//							}else{
//								CommonUtils.addContackPoint(Constant.POINT_WAY_16, "经销商类重复线索", customerId, adviser, dealerId);
//							}
							//end
							
							//增加接触点信息
							
							String repeatLeads = SequenceManager.getSequence("");
							//新增提醒信息 
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_20.toString(), repeatLeads, customerId, dealerId, adviser, sdf.format(new Date()),"");
							//标记线索为重复线索
							CommonUtils.updateIfRepeat(leadsAllotId);
							CommonUtils.updateLeadStatus(leadsCode);
						}
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
						//update by yinshunhui结束提醒
						//CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_02.toString());
						
						if("followRadio".equals(typeFlag) || "inviteRadio".equals(typeFlag) || "orderRadio".equals(typeFlag)){
							String followId = SequenceManager.getSequence("");//本次跟进任务ID
							Long nextTaskId = Long.parseLong(SequenceManager.getSequence(""));//下次任务ID
							//新增本次跟进任务表
							TPcFollowPO follow0Po = new TPcFollowPO();
							follow0Po.setFollowId(Long.parseLong(followId));
							follow0Po.setCustomerId(Long.parseLong(customerId));
							follow0Po.setFollowDate(new Date());
							follow0Po.setOldLevel(null);
							follow0Po.setNewLevel(intentType);
							follow0Po.setSalesProgress(salesProgress);
							follow0Po.setFollowInfo(followInfo);
							follow0Po.setTaskStatus(Constant.TASK_STATUS_02);
							follow0Po.setFinishDate(new Date());
							follow0Po.setFollowType(Constant.FOLLOW_TYPE_01.toString());
							follow0Po.setCreateDate(new Date());
							follow0Po.setCreateBy(logonUser.getUserId().toString());
							follow0Po.setNextTask(nextTaskId);
							dao.insert(follow0Po);
							
							//增加接触点信息
							CommonUtils.addContackPoint(Constant.POINT_WAY_03, followInfo, customerId, logonUser.getUserId().toString(), logonUser.getDealerId());
							
							if("followRadio".equals(typeFlag)) {//点跟进时
								//新增下次跟进任务表
								TPcFollowPO followPo = new TPcFollowPO();
								followPo.setFollowId(nextTaskId);
								followPo.setCustomerId(Long.parseLong(customerId));
								followPo.setFollowDate(sdf.parse(nextFollowDate));
								followPo.setOldLevel(intentType);
								followPo.setNewLevel(null);
								followPo.setTaskStatus(Constant.TASK_STATUS_01);
								followPo.setFollowType(followType);
								followPo.setFollowPlan(followPlan);
								followPo.setCreateDate(new Date());
								followPo.setCreateBy(logonUser.getUserId().toString());
								followPo.setPreviousTask(Long.parseLong(followId));
								followPo.setOldSalesProgress(salesProgress);
								dao.insert(followPo);
								
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), nextFollowDate,"");
							} else if("inviteRadio".equals(typeFlag)) {//点邀约时
								//新增下次计划邀约任务表
								TPcInvitePO invitePo = new TPcInvitePO();
								invitePo.setInviteId(nextTaskId);
								invitePo.setCustomerId(Long.parseLong(customerId));
								invitePo.setCreateDate(new Date());
								invitePo.setCreateBy(logonUser.getUserId().toString());
								invitePo.setPlanInviteDate(sdf.parse(planInviteDate));
								invitePo.setPlanMeetDate(sdf.parse(planMeetDate));
								invitePo.setTaskStatus(Constant.TASK_STATUS_01);
								invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_04);
								invitePo.setIfPlan(Constant.IF_TYPE_NO);
								invitePo.setOldLevel(intentType);
								invitePo.setNewLevel(null);
								invitePo.setInviteType(Integer.parseInt(inviteType));
								invitePo.setPreviousTask(Long.parseLong(followId));
								invitePo.setOldSalesProgress(salesProgress);
								//判断是否做了计划（4个框有值说明为做了计划）
								if(xqfx!=null&&!"".equals(xqfx)) {
									invitePo.setIfPlan(Constant.IF_TYPE_YES);
									invitePo.setRequirement(xqfx);
									invitePo.setInviteTarget(yymb);
									invitePo.setTrustDesign(ydkhxrsj);
									invitePo.setSceneDesign(gdkhqjsj);
									invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_01);
									//新增提醒信息
									CommonUtils.addRemindInfo(Constant.REMIND_TYPE_15.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
								} else {
									//新增提醒信息
									CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), planInviteDate,"");
								}
								dao.insert(invitePo);
							} else if("orderRadio".equals(typeFlag)) {//点订车时
								//新增下次订单任务表
								//新增下次订单任务
								TPcOrderPO orderPo = new TPcOrderPO();
								orderPo.setOrderId(nextTaskId);
								orderPo.setCustomerId(Long.parseLong(customerId));
								orderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
								orderPo.setOldLevel(intentType);
								orderPo.setCreateDate(new Date());
								orderPo.setCreateBy(logonUser.getUserId().toString());
								orderPo.setOldSalesProgress(salesProgress);
								orderPo.setPreviousTask(Long.parseLong(followId));
								orderPo.setTaskStatus(Constant.TASK_STATUS_01);
								orderPo.setOrderDate(sdf.parse(orderDate));
								dao.insert(orderPo);
								
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_08.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), orderDate,"");
							}
						} else if("defeatRadio".equals(typeFlag)){
							//选择战败后保存
							//修改销售线索主表
							TPcLeadsPO leadsOldPo2 = new TPcLeadsPO();
							leadsOldPo2.setLeadsCode(Long.parseLong(leadsCode));
							TPcLeadsPO leadsNewPo2 = new TPcLeadsPO();
							leadsNewPo2.setLeadsCode(Long.parseLong(leadsCode));
							leadsNewPo2.setCustomerName(customerName);
							leadsNewPo2.setTelephone(telePhone);
							leadsNewPo2.setIntentVehicle(intentVehicle);
							leadsNewPo2.setLeadsStatus(Constant.LEADS_STATUS_02.longValue());//线索状态为 战败线索
							leadsNewPo2.setUpdateDate(new Date());
							leadsNewPo2.setUpdateBy(logonUser.getUserId());
							leadsNewPo2.setDefeatModel(defeatVehicle);
							leadsNewPo2.setDefeatReason(defeatReason);
							leadsNewPo2.setDefaultRemark(defeatInfo);
							dao.update(leadsOldPo2, leadsNewPo2);
							
							//修改销售线索分派表
							TPcLeadsAllotPO leadsAllotOldPo2 = new TPcLeadsAllotPO();
							leadsAllotOldPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
							TPcLeadsAllotPO leadsAllotNewPo2 = new TPcLeadsAllotPO();
							//leadsAllotNewPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
							leadsAllotNewPo2.setCustomerName(customerName);
							leadsAllotNewPo2.setTelephone(telePhone);
							leadsAllotNewPo2.setStatus(Constant.STATUS_DISABLE);
							leadsAllotNewPo2.setIfConfirm(Constant.ADVISER_CONFIRM_02);
							leadsAllotNewPo2.setConfirmDate(new Date());
							dao.update(leadsAllotOldPo2, leadsAllotNewPo2);
							//标记提醒信息为已完成
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
							
							act.setOutData("leadsCode", leadsCode);
							act.setOutData("leadsAllotId", leadsAllotId);
							doInit();
							act.setForword(taskManageinitUrl);
						} else if("failureRadio".equals(typeFlag)){
							//选择失效后保存
							//修改销售线索主表
							TPcLeadsPO leadsOldPo2 = new TPcLeadsPO();
							leadsOldPo2.setLeadsCode(Long.parseLong(leadsCode));
							TPcLeadsPO leadsNewPo2 = new TPcLeadsPO();
							leadsNewPo2.setLeadsCode(Long.parseLong(leadsCode));
							leadsNewPo2.setCustomerName(customerName);
							leadsNewPo2.setTelephone(telePhone);
							leadsNewPo2.setIntentVehicle(intentVehicle);
							leadsNewPo2.setLeadsStatus(Constant.LEADS_STATUS_03.longValue());//线索状态为 战败线索
							leadsNewPo2.setUpdateDate(new Date());
							leadsNewPo2.setUpdateBy(logonUser.getUserId());
							dao.update(leadsOldPo2, leadsNewPo2);
							
							//修改销售线索分派表
							TPcLeadsAllotPO leadsAllotOldPo2 = new TPcLeadsAllotPO();
							leadsAllotOldPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
							TPcLeadsAllotPO leadsAllotNewPo2 = new TPcLeadsAllotPO();
							//leadsAllotNewPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
							leadsAllotNewPo2.setCustomerName(customerName);
							leadsAllotNewPo2.setTelephone(telePhone);
							leadsAllotNewPo2.setStatus(Constant.STATUS_DISABLE);
							leadsAllotNewPo2.setIfConfirm(Constant.ADVISER_CONFIRM_02);
							leadsAllotNewPo2.setConfirmDate(new Date());
							dao.update(leadsAllotOldPo2, leadsAllotNewPo2);
							
							//标记提醒信息为已完成
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
			
							act.setOutData("leadsCode", leadsCode);
							act.setOutData("leadsAllotId", leadsAllotId);
							doInit();
							act.setForword(taskManageinitUrl);
						}
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
						
						act.setOutData("leadsCode", leadsCode);
						act.setOutData("leadsAllotId", leadsAllotId);
						doInit();
						act.setForword(taskManageinitUrl);
					} else if("invidateRadio".equals(typeFlag)){
						//选择失效后保存
						//修改销售线索主表
						TPcLeadsPO leadsOldPo2 = new TPcLeadsPO();
						leadsOldPo2.setLeadsCode(Long.parseLong(leadsCode));
						TPcLeadsPO leadsNewPo2 = new TPcLeadsPO();
						leadsNewPo2.setLeadsCode(Long.parseLong(leadsCode));
						leadsNewPo2.setCustomerName(customerName);
						leadsNewPo2.setTelephone(telePhone);
						leadsNewPo2.setIntentVehicle(intentVehicle);
						leadsNewPo2.setIfHandle(10041001);
						leadsNewPo2.setLeadsStatus(Constant.LEADS_STATUS_04.longValue());//线索状态为 战败线索
						leadsNewPo2.setUpdateDate(new Date());
						leadsNewPo2.setUpdateBy(logonUser.getUserId());
						dao.update(leadsOldPo2, leadsNewPo2);
						
						//修改销售线索分派表
						TPcLeadsAllotPO leadsAllotOldPo2 = new TPcLeadsAllotPO();
						leadsAllotOldPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
						TPcLeadsAllotPO leadsAllotNewPo2 = new TPcLeadsAllotPO();
						//leadsAllotNewPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
						leadsAllotNewPo2.setCustomerName(customerName);
						leadsAllotNewPo2.setTelephone(telePhone);
						leadsAllotNewPo2.setStatus(Constant.STATUS_DISABLE);
						leadsAllotNewPo2.setIfConfirm(Constant.ADVISER_CONFIRM_02);
						leadsAllotNewPo2.setConfirmDate(new Date());
						dao.update(leadsAllotOldPo2, leadsAllotNewPo2);
						
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
						
						act.setOutData("leadsCode", leadsCode);
						act.setOutData("leadsAllotId", leadsAllotId);
						doInit();
						act.setForword(taskManageinitUrl);
						
					}else {//发送错误消息到页面
						act.setOutData("errorMsg", "该客户:"+customerName+",已分派其他顾问,保存失败!");
						act.setOutData("customerName", customerName);
						act.setOutData("telephone", telePhone);
						act.setOutData("nowDate", nowDate);
						leadsConfirmByDcrcInit();
						act.setOutData("leadsCode", leadsCode);
						act.setOutData("leadsAllotId", leadsAllotId);
//						act.setForword(leadsConfirmByOemUrl);
					}
				} else if("followRadio".equals(typeFlag) || "inviteRadio".equals(typeFlag) || "orderRadio".equals(typeFlag)){
					remark=remark+"-未建档";
					leadsNewPoByDcrc.setFailureRemark(remark);
					dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
					//选择跟进或邀约后保存
					//新增客户档案
					TPcCustomerPO customerPo = new TPcCustomerPO();
					String customerId = SequenceManager.getSequence("");
					String customerCode = SequenceManager.getCtmNo();
					customerPo.setCustomerId(Long.parseLong(customerId));
					customerPo.setCustomerCode(customerCode);
					customerPo.setCustomerName(customerName);
					customerPo.setTelephone(telePhone);
					customerPo.setLeadsOrigin(leadsOrigin);
					customerPo.setJcWay(collectFashion);
					customerPo.setGender(sex);
					customerPo.setBuyBudget(buyBudget);
					customerPo.setCtmProp(customerType);
					customerPo.setIntentVehicle(intentVehicle);
					customerPo.setFirstContactTime(new Date());
					customerPo.setCtmRank(intentType);
					customerPo.setComeReason(comeReason);
					customerPo.setIfDrive(Long.parseLong(testDriving));
					if(comeDate!=null) {
						customerPo.setFirstShopTime(sdf.parse(comeDate));
					}
					customerPo.setBuyType(buyType);
					if("followRadio".equals(typeFlag)) {//点跟进时
						customerPo.setNextFollowTime(sdf.parse(nextFollowDate));//下次跟进时间
					} else if("inviteRadio".equals(typeFlag)) {//点邀约时
						customerPo.setNextFollowTime(sdf.parse(planInviteDate));//下次跟进时间
						customerPo.setPreShopTime(sdf.parse(planMeetDate));//预计到店时间
					}
					customerPo.setCtmStatus(Constant.CTM_STATUS_01.toString());//客户状态
					customerPo.setStatus(Constant.STATUS_ENABLE.longValue());
					customerPo.setCreateDate(new Date());
					customerPo.setCreateBy(logonUser.getUserId());
					customerPo.setDealerId(Long.parseLong(logonUser.getDealerId()));
					customerPo.setAdviser(logonUser.getUserId());
					customerPo.setSalesProgress(salesProgress);
					dao.insert(customerPo);
					//当集客方式为老客户转介绍的时候添加老客户与车主关系信息
					if("60021008".equals(collectFashion))
					{			
						TPcLinkManPO linkMan=new TPcLinkManPO();
						String linkId = SequenceManager.getSequence("");
						linkMan.setLinkId(Long.parseLong(linkId));
						linkMan.setLinkMan(oldCustomerName.trim());
						linkMan.setLinkPhone(oldTelephone.trim());
						linkMan.setCreateDate(new Date());
						linkMan.setRelationship("老客户推荐");
						linkMan.setCtmId(Long.parseLong(customerId));
						linkMan.setStatus(new Long(Constant.STATUS_ENABLE));
						linkMan.setOldVehicleId(oldVehicleId.trim());
						linkMan.setRelationCode(Constant.linkMan_status_01.toString());
						dao.insert(linkMan);
					}
					//当集客方式为情报转介绍的时候添加老客户与车主关系信息
					if("60021004".equals(collectFashion))
					{			
						TPcLinkManPO linkMan=new TPcLinkManPO();
						String linkId = SequenceManager.getSequence("");
						linkMan.setLinkId(Long.parseLong(linkId));
						linkMan.setLinkMan(oldCustomerName.trim());
						linkMan.setLinkPhone(oldTelephone.trim());
						linkMan.setCreateDate(new Date());
						linkMan.setRelationship("情报转介绍");
						linkMan.setCtmId(Long.parseLong(customerId));
						linkMan.setStatus(new Long(Constant.STATUS_ENABLE));
						linkMan.setRelationCode(Constant.linkMan_status_02.toString());
						dao.insert(linkMan);
					}
					//增加一次到店
					if("60151011".equals(leadsOrigin)){
						CommonUtils.addComeCount(new Long(customerId));
					}
					//修改销售线索主表
					TPcLeadsPO leadsOldPo = new TPcLeadsPO();
					leadsOldPo.setLeadsCode(Long.parseLong(leadsCode));
					TPcLeadsPO leadsNewPo = new TPcLeadsPO();
					leadsNewPo.setLeadsCode(Long.parseLong(leadsCode));
					leadsNewPo.setCustomerName(customerName);
					leadsNewPo.setCustomerId(Long.parseLong(customerId));
					leadsNewPo.setTelephone(telePhone);
					leadsNewPo.setIntentVehicle(intentVehicle);
					leadsNewPo.setJcWay(collectFashion);
					leadsNewPo.setIfHandle(10041001);
					leadsNewPo.setUpdateDate(new Date());
					leadsNewPo.setUpdateBy(logonUser.getUserId());
					dao.update(leadsOldPo, leadsNewPo);
					
					//修改销售线索分派表
					TPcLeadsAllotPO leadsAllotOldPo = new TPcLeadsAllotPO();
					leadsAllotOldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					TPcLeadsAllotPO leadsAllotNewPo = new TPcLeadsAllotPO();
					//leadsAllotNewPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					leadsAllotNewPo.setCustomerName(customerName);
					leadsAllotNewPo.setCustomerId(Long.parseLong(customerId));
					leadsAllotNewPo.setTelephone(telePhone);
					leadsAllotNewPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);//顾问确认标志
					leadsAllotNewPo.setConfirmDate(new Date());
					dao.update(leadsAllotOldPo, leadsAllotNewPo);
					
					String followId = SequenceManager.getSequence("");//本次跟进任务ID
					String nextTaskId = SequenceManager.getSequence("");//下次跟进任务ID
					//新增本次跟进任务表
					TPcFollowPO follow0Po = new TPcFollowPO();
					follow0Po.setFollowId(Long.parseLong(followId));
					follow0Po.setCustomerId(Long.parseLong(customerId));
					follow0Po.setFollowDate(new Date());
					follow0Po.setOldLevel(null);
					follow0Po.setNewLevel(intentType);
					follow0Po.setSalesProgress(salesProgress);
					follow0Po.setFollowInfo(followInfo);
					follow0Po.setTaskStatus(Constant.TASK_STATUS_02);
					follow0Po.setFinishDate(new Date());
					follow0Po.setCreateDate(new Date());
					follow0Po.setNextTask(Long.parseLong(nextTaskId));
					follow0Po.setFollowType(Constant.FOLLOW_TYPE_01.toString());
					follow0Po.setCreateBy(logonUser.getUserId().toString());
					dao.insert(follow0Po);
					
					//增加接触点信息
					CommonUtils.addContackPoint(Constant.POINT_WAY_03, followInfo, customerId, logonUser.getUserId().toString(), logonUser.getDealerId());
					
					if("followRadio".equals(typeFlag)) {//点跟进时
						remark=remark+"-跟进";
						leadsNewPoByDcrc.setFailureRemark(remark);
						dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
						//新增下次跟进任务表
						TPcFollowPO followPo = new TPcFollowPO();
						followPo.setFollowId(Long.parseLong(nextTaskId));
						followPo.setCustomerId(Long.parseLong(customerId));
						followPo.setFollowDate(sdf.parse(nextFollowDate));
						followPo.setOldLevel(intentType);
						followPo.setNewLevel(null);
						followPo.setOldSalesProgress(salesProgress);
						followPo.setTaskStatus(Constant.TASK_STATUS_01);
						followPo.setFollowType(followType);
						followPo.setFollowPlan(followPlan);
						followPo.setCreateDate(new Date());
						followPo.setCreateBy(logonUser.getUserId().toString());
						followPo.setPreviousTask(Long.parseLong(followId));
						dao.insert(followPo);
						
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), nextTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), nextFollowDate,"");
					} else if("inviteRadio".equals(typeFlag)) {//点邀约时
						remark=remark+"-邀约";
						leadsNewPoByDcrc.setFailureRemark(remark);
						dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
						//新增下次计划邀约任务表
						TPcInvitePO invitePo = new TPcInvitePO();
						invitePo.setInviteId(Long.parseLong(nextTaskId));
						invitePo.setCustomerId(Long.parseLong(customerId));
						invitePo.setCreateDate(new Date());
						invitePo.setCreateBy(logonUser.getUserId().toString());
						invitePo.setPlanInviteDate(sdf.parse(planInviteDate));
						invitePo.setPlanMeetDate(sdf.parse(planMeetDate));
						invitePo.setTaskStatus(Constant.TASK_STATUS_01);
						invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_04);
						invitePo.setIfPlan(Constant.IF_TYPE_NO);
						invitePo.setInviteType(Integer.parseInt(inviteType));
						invitePo.setOldLevel(intentType);
						invitePo.setNewLevel(null);
						invitePo.setOldSalesProgress(salesProgress);
						invitePo.setPreviousTask(Long.parseLong(followId));
						//判断是否做了计划（4个框有值说明为做了计划）
						if(xqfx!=null) {
							invitePo.setIfPlan(Constant.IF_TYPE_YES);
							invitePo.setRequirement(xqfx);
							invitePo.setInviteTarget(yymb);
							invitePo.setTrustDesign(ydkhxrsj);
							invitePo.setSceneDesign(gdkhqjsj);
							invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_01);
							//标记提醒信息为已完成
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
							
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_15.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						} else {
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), planInviteDate,"");
						}
						dao.insert(invitePo);
					} else if("orderRadio".equals(typeFlag)) {//点订车时
						remark=remark+"-订单";
						leadsNewPoByDcrc.setFailureRemark(remark);
						dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
						//新增下次订单任务表
						//新增下次订单任务
						TPcOrderPO orderPo = new TPcOrderPO();
						orderPo.setOrderId(Long.parseLong(nextTaskId));
						orderPo.setCustomerId(Long.parseLong(customerId));
						orderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
						orderPo.setOldLevel(intentType);
						orderPo.setCreateDate(new Date());
						orderPo.setCreateBy(logonUser.getUserId().toString());
						orderPo.setOldSalesProgress(salesProgress);
						orderPo.setPreviousTask(Long.parseLong(followId));
						orderPo.setTaskStatus(Constant.TASK_STATUS_01);
						orderPo.setOrderDate(sdf.parse(orderDate));
						dao.insert(orderPo);
						
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_08.toString(), nextTaskId.toString(), customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), orderDate,"");
					}
					
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
					
					act.setOutData("leadsCode", leadsCode);
					act.setOutData("leadsAllotId", leadsAllotId);
					doInit();
					act.setForword(taskManageinitUrl);
				} else if("defeatRadio".equals(typeFlag)){//选择战败后保存
					remark=remark+"-战败";
					leadsNewPoByDcrc.setFailureRemark(remark);
					dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
					//修改销售线索主表
					TPcLeadsPO leadsOldPo = new TPcLeadsPO();
					leadsOldPo.setLeadsCode(Long.parseLong(leadsCode));
					TPcLeadsPO leadsNewPo = new TPcLeadsPO();
					leadsNewPo.setLeadsCode(Long.parseLong(leadsCode));
					leadsNewPo.setCustomerName(customerName);
					leadsNewPo.setTelephone(telePhone);
					leadsNewPo.setIntentVehicle(intentVehicle);
					leadsNewPo.setLeadsStatus(Constant.LEADS_STATUS_02.longValue());//线索状态为 战败线索
					leadsNewPo.setDefeatModel(defeatVehicle);
					leadsNewPo.setDefeatReason(defeatReason);
					leadsNewPo.setDefaultRemark(defeatInfo);
					leadsNewPo.setJcWay(collectFashion);
					leadsNewPo.setIfHandle(10041001);
					leadsNewPo.setUpdateDate(new Date());
					leadsNewPo.setUpdateBy(logonUser.getUserId());
					leadsNewPo.setAuditStatus(Constant.FAILURE_AUDIT_01); //是否已审核
					leadsNewPo.setIsDirectDefeat(Constant.STATUS_DISABLE); // 是否是直接战败
					if (!CommonUtils.isEmpty(defeatVehicle)) {
						leadsNewPo.setDefeatModel(defeatVehicle);
					}
					dao.update(leadsOldPo, leadsNewPo);
					
					//修改销售线索分派表
					TPcLeadsAllotPO leadsAllotOldPo = new TPcLeadsAllotPO();
					leadsAllotOldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					TPcLeadsAllotPO leadsAllotNewPo = new TPcLeadsAllotPO();
					//leadsAllotNewPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					leadsAllotNewPo.setCustomerName(customerName);
					leadsAllotNewPo.setTelephone(telePhone);
					leadsAllotNewPo.setStatus(Constant.STATUS_DISABLE);
					leadsAllotNewPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);//顾问确认标志
					leadsAllotNewPo.setConfirmDate(new Date());
					dao.update(leadsAllotOldPo, leadsAllotNewPo);

					//插入战败记录
					String failureId = SequenceManager.getSequence("");
					TPcDefeatfailurePO tdp = new TPcDefeatfailurePO();
					tdp.setDefeatfailureId(Long.parseLong(failureId));
					tdp.setDefeatfailureType(Constant.FAILURE_TYPE_01);
					tdp.setDefeatEndDate(new Date());
					tdp.setStatus(Constant.STATUS_ENABLE);
					tdp.setSalesProgress(Constant.BUY_TYPE2_01+"");
					tdp.setCreateBy(logonUser.getUserId()+"");
					tdp.setCreateDate(new Date());
					tdp.setDefeatModel(leadsNewPo.getDefeatModel());
					tdp.setReasonAnalysis(defeatInfo);
					tdp.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
					tdp.setCustomerId(0L);
					tdp.setLeadsCode(leadsOldPo.getLeadsCode());
					dao.insert(tdp);
					
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
					
					act.setOutData("leadsCode", leadsCode);
					act.setOutData("leadsAllotId", leadsAllotId);
					doInit();
					act.setForword(taskManageinitUrl);
				} else if("failureRadio".equals(typeFlag)){//选择失效后保存
					remark=remark+"-失效";
					leadsNewPoByDcrc.setFailureRemark(remark);
					dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
					//修改销售线索主表
					TPcLeadsPO leadsOldPo = new TPcLeadsPO();
					leadsOldPo.setLeadsCode(Long.parseLong(leadsCode));
					TPcLeadsPO leadsNewPo = new TPcLeadsPO();
					leadsNewPo.setLeadsCode(Long.parseLong(leadsCode));
					leadsNewPo.setCustomerName(customerName);
					leadsNewPo.setTelephone(telePhone);
					leadsNewPo.setIntentVehicle(intentVehicle);
					leadsNewPo.setLeadsStatus(Constant.LEADS_STATUS_03.longValue());//线索状态为 失效线索
					leadsNewPo.setJcWay(collectFashion);
					leadsNewPo.setIfHandle(10041001);
					leadsNewPo.setUpdateDate(new Date());
					leadsNewPo.setUpdateBy(logonUser.getUserId());
					dao.update(leadsOldPo, leadsNewPo);
					
					//修改销售线索分派表
					TPcLeadsAllotPO leadsAllotOldPo = new TPcLeadsAllotPO();
					leadsAllotOldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					TPcLeadsAllotPO leadsAllotNewPo = new TPcLeadsAllotPO();
					//leadsAllotNewPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					leadsAllotNewPo.setCustomerName(customerName);
					leadsAllotNewPo.setTelephone(telePhone);
					leadsAllotNewPo.setStatus(Constant.STATUS_DISABLE);
					leadsAllotNewPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);//顾问确认标志
					leadsAllotNewPo.setConfirmDate(new Date());
					dao.update(leadsAllotOldPo, leadsAllotNewPo);
					
					//标记提醒信息为已完成
					//CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_02.toString());
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
					
					act.setOutData("leadsCode", leadsCode);
					act.setOutData("leadsAllotId", leadsAllotId);
					doInit();
					act.setForword(taskManageinitUrl);
				}else if("invidateRadio".equals(typeFlag)){
					remark=remark+"-无效";
					leadsNewPoByDcrc.setFailureRemark(remark);
					dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
					//修改销售线索主表
					TPcLeadsPO leadsOldPo = new TPcLeadsPO();
					leadsOldPo.setLeadsCode(Long.parseLong(leadsCode));
					TPcLeadsPO leadsNewPo = new TPcLeadsPO();
					leadsNewPo.setLeadsCode(Long.parseLong(leadsCode));
					leadsNewPo.setCustomerName(customerName);
					leadsNewPo.setTelephone(telePhone);
					leadsNewPo.setIntentVehicle(intentVehicle);
					leadsNewPo.setLeadsStatus(Constant.LEADS_STATUS_04.longValue());//线索状态为无效
					leadsNewPo.setJcWay(collectFashion);
					leadsNewPo.setIfHandle(10041001);
					leadsNewPo.setUpdateDate(new Date());
					leadsNewPo.setUpdateBy(logonUser.getUserId());
					dao.update(leadsOldPo, leadsNewPo);
					
					//修改销售线索分派表
					TPcLeadsAllotPO leadsAllotOldPo = new TPcLeadsAllotPO();
					leadsAllotOldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					TPcLeadsAllotPO leadsAllotNewPo = new TPcLeadsAllotPO();
					//leadsAllotNewPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					leadsAllotNewPo.setCustomerName(customerName);
					leadsAllotNewPo.setTelephone(telePhone);
					leadsAllotNewPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);//顾问确认标志
					leadsAllotNewPo.setConfirmDate(new Date());
					leadsAllotNewPo.setStatus(Constant.STATUS_DISABLE);
					dao.update(leadsAllotOldPo, leadsAllotNewPo);
					
					//标记提醒信息为已完成
					//CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_02.toString());
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
					
					act.setOutData("leadsCode", leadsCode);
					act.setOutData("leadsAllotId", leadsAllotId);
					doInit();
					act.setForword(taskManageinitUrl);
				} else {//直接点击保存时
					//补全销售线索主表
					remark=remark+"-直接保存";
					leadsNewPoByDcrc.setFailureRemark(remark);
					dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
					TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
					oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
					TPcLeadsPO newLeadsPo = new TPcLeadsPO();
					newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
					newLeadsPo.setCustomerName(customerName);
					newLeadsPo.setTelephone(telePhone);
					newLeadsPo.setIfHandle(10041001);
					newLeadsPo.setUpdateDate(new Date());
					newLeadsPo.setUpdateBy(logonUser.getUserId());
					dao.update(oldLeadsPo, newLeadsPo);
					//补全销售线索分派表
					TPcLeadsAllotPO oldLeadsAllotPo = new TPcLeadsAllotPO();
					oldLeadsAllotPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					TPcLeadsAllotPO newLeadsAllotPo = new TPcLeadsAllotPO();
					//newLeadsAllotPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					newLeadsAllotPo.setCustomerName(customerName);
					newLeadsAllotPo.setTelephone(telePhone);
					newLeadsAllotPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);//顾问确认标志
					newLeadsAllotPo.setConfirmDate(new Date());
					dao.update(oldLeadsAllotPo, newLeadsAllotPo);
					
					//标记提醒信息为已完成
					//CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_02.toString());
					//新增提醒信息 
					CommonUtils.addRemindInfo(Constant.REMIND_TYPE_04.toString(), leadsAllotId, "", logonUser.getDealerId(), logonUser.getUserId().toString(), remindDate,"");
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
					act.setOutData("leadsCode", leadsCode);
					act.setOutData("leadsAllotId", leadsAllotId);
					doInit();
					act.setForword(taskManageinitUrl);
				}
			} else {
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
				act.setOutData("leadsCode", leadsCode);
				act.setOutData("leadsAllotId", leadsAllotId);
				doInit();
				act.setForword(taskManageinitUrl);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 跟进任务页面
	 */
	public void doTaskFollowInit() {
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		List a[] = new List[10]; 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String customerId = act.getRequest().getParamValue("customerId");
		String taskId = act.getRequest().getParamValue("taskId");
		String adviserLogon = "no";//顾问登陆标志
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			//从客户表中中获取客户姓名和联系电话
			//获取客户基本信息
			List<DynaBean> customerList = dao.getCustomerInfo(customerId);
			//获取客户信息的意向车型和意向等级和销售流程进度
			DynaBean db = customerList.get(0);
			String seriesCode = db.get("INTENT_VEHICLE").toString();
			String ctmRank = db.get("CTM_RANK").toString();
			String ctmRank2 = db.get("CTM_RANK2").toString();
			String salesProgress = null;
			String salesProgress2 = null;
			if(db.get("SALES_PROGRESS")==null) {
				salesProgress = "";
			} else {
				salesProgress = db.get("SALES_PROGRESS").toString();
			}
			if(db.get("SALES_PROGRESS2")==null) {
				salesProgress2 = "";
			} else {
				salesProgress2 = db.get("SALES_PROGRESS2").toString();
			}
			//获取意向车型一级列表
			List<DynaBean> menusAList = dao.getIntentVehicleA();
			//获取意向车型二级列表
			List<DynaBean> menusBList = dao.getIntentVehicleB();
			//获取一级意向车型对应二级列表
			List<DynaBean> menusABList = dao.getIntentVehicleAB(seriesCode);
			DynaBean db2 = menusABList.get(0);
			String upSeriesCode = db2.get("PARENTID").toString();
			List<DynaBean> menusABList2 = dao.getIntentVehicleAB2(upSeriesCode);
			//获取一级意向车型对应二级列表
//			List<DynaBean> menusABList3 = dao.getIntentVehicleAB(seriesCode);
			//获取战败车型一级列表
			List<DynaBean> menusAList2 = dao.getDefeatVehicleA();
			//获取战败车型二级列表
			List<DynaBean> menusBList2 = dao.getDefeatVehicleB();
			//获取战败原因一级列表
			List<DynaBean> menusAList3 = dao.getDefeatReasonA();
			//获取战败原因二级列表
			List<DynaBean> menusBList3 = dao.getDefeatReasonB();
			
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			    adviserLogon = "yes";
			}
			act.setOutData("adviserLogon", adviserLogon);
			act.setOutData("customerId", customerId);
			act.setOutData("taskId", taskId);
			act.setOutData("upSeriesCode", upSeriesCode);
			act.setOutData("customerList", customerList);
			act.setOutData("nowDate", nowDate);
			act.setOutData("ctmRank", ctmRank);
			act.setOutData("ctmRank2", ctmRank2);
			act.setOutData("salesProgress", salesProgress);
			act.setOutData("salesProgress2", salesProgress2);
			act.setOutData("menusAList", menusAList);
			act.setOutData("menusBList", menusBList);
			act.setOutData("menusABList2", menusABList2);
//			act.setOutData("menusABList3", menusABList3);
			act.setOutData("menusAList2", menusAList2);
			act.setOutData("menusBList2", menusBList2);
			act.setOutData("menusAList3", menusAList3);
			act.setOutData("menusBList3", menusBList3);
			act.setForword(doTaskFollowUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 邀约任务页面
	 */
	public void doTaskInviteInit() {
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		List a[] = new List[10]; 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String customerId = act.getRequest().getParamValue("customerId");
		String inviteId = act.getRequest().getParamValue("taskId");
		String inviteIdx = "";
		String inviteShopId = act.getRequest().getParamValue("inviteShopId");
		String adviserLogon = "no";//顾问登陆标志
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			//从客户表中获取客户姓名和联系电话
			//获取客户基本信息
			List<DynaBean> customerList = dao.getCustomerInfo(customerId);
			//获取客户信息的意向车型和意向等级和销售流程进度
			DynaBean db = customerList.get(0);
			String seriesCode = db.get("INTENT_VEHICLE").toString();
			String ctmRank = db.get("CTM_RANK").toString();
			String ctmRank2 = db.get("CTM_RANK2").toString();
			String salesProgress = null;
			String salesProgress2 = null;
			String inviteShopDate = "";
			if(db.get("SALES_PROGRESS")==null) {
				salesProgress = "";
			} else {
				salesProgress = db.get("SALES_PROGRESS").toString();
			}
			if(db.get("SALES_PROGRESS2")==null) {
				salesProgress2 = "";
			} else {
				salesProgress2 = db.get("SALES_PROGRESS2").toString();
			}
			
			//获取邀约到店信息中的预约到店时间和inviteId
			if(inviteShopId!=null&&!"".equals(inviteShopId)&&!"null".equals(inviteShopId)) {
				List<DynaBean> inviteShopList = dao.getInviteShopInfo(inviteShopId);
				DynaBean db4 = inviteShopList.get(0);
				if(db4.get("INVITE_SHOP_DATE")!=null) {
					inviteShopDate = db4.get("INVITE_SHOP_DATE").toString();
				}
				inviteIdx = db4.get("INVITE_ID").toString();
			}
			if(inviteId==null||"".equals(inviteId)||"null".equals(inviteId)) {
				inviteId = inviteIdx;
			}
			//获取计划邀约信息
			List<DynaBean> inviteList = dao.getInviteInfo(inviteId);
			DynaBean db3 = inviteList.get(0);
			String inviteType = "";
			String inviteType2 = "";
			String inviteWay = "";
			String inviteWay2 = "";
			String planMeetDate = "";
			String inviteRemartk = "";
			String auditRemark = "";
			String directorAudit = "";
			String x1 = "";
			String x2 = "";
			String x3 = "";
			String x4 = "";
			if(db3.get("INVITE_TYPE")!=null) {
				inviteType = db3.get("INVITE_TYPE").toString();
			}
			if(db3.get("INVITE_TYPE2")!=null) {
				inviteType2 = db3.get("INVITE_TYPE2").toString();
			}
			if(db3.get("INVITE_WAY")!=null) {
				inviteWay = db3.get("INVITE_WAY").toString();
			}
			if(db3.get("INVITE_WAY2")!=null) {
				inviteWay2 = db3.get("INVITE_WAY2").toString();
			}
			if(db3.get("PLAN_MEET_DATE")!=null) {
				planMeetDate = db3.get("PLAN_MEET_DATE").toString();
			}
			if(db3.get("REMARK")!=null) {
				inviteRemartk = db3.get("REMARK").toString();
			}
			if(db3.get("AUDIT_REMARK")!=null) {
				auditRemark = db3.get("AUDIT_REMARK").toString();
			}
			if(db3.get("DIRECTOR_AUDIT")!=null) {
				directorAudit = db3.get("DIRECTOR_AUDIT").toString();
			}
			if(db3.get("REQUIREMENT")!=null) {
				x1 = db3.get("REQUIREMENT").toString();
			}
			if(db3.get("INVITE_TARGET")!=null) {
				x2 = db3.get("INVITE_TARGET").toString();
			}
			if(db3.get("TRUST_DESIGN")!=null) {
				x3 = db3.get("TRUST_DESIGN").toString();
			}
			if(db3.get("SCENE_DESIGN")!=null) {
				x4 = db3.get("SCENE_DESIGN").toString();
			}
			
			//获取意向车型一级列表
			List<DynaBean> menusAList = dao.getIntentVehicleA();
			//获取意向车型二级列表
			List<DynaBean> menusBList = dao.getIntentVehicleB();
			//获取一级意向车型对应二级列表
			List<DynaBean> menusABList = dao.getIntentVehicleAB(seriesCode);
			DynaBean db2 = menusABList.get(0);
			String upSeriesCode = db2.get("PARENTID").toString();
			List<DynaBean> menusABList2 = dao.getIntentVehicleAB2(upSeriesCode);
			//获取一级意向车型对应二级列表
//			List<DynaBean> menusABList3 = dao.getIntentVehicleAB(seriesCode);
			//获取战败车型一级列表
			List<DynaBean> menusAList2 = dao.getDefeatVehicleA();
			//获取战败车型二级列表
			List<DynaBean> menusBList2 = dao.getDefeatVehicleB();
			//获取战败原因一级列表
			List<DynaBean> menusAList3 = dao.getDefeatReasonA();
			//获取战败原因二级列表
			List<DynaBean> menusBList3 = dao.getDefeatReasonB();
			
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			    adviserLogon = "yes";
			}
			act.setOutData("adviserLogon", adviserLogon);
			act.setOutData("nowDate", nowDate);
			act.setOutData("customerId", customerId);
			act.setOutData("taskId", inviteId);
			act.setOutData("inviteShopId", inviteShopId);
			act.setOutData("upSeriesCode", upSeriesCode);
			act.setOutData("inviteType", inviteType);
			act.setOutData("inviteType2", inviteType2);
			act.setOutData("inviteWay", inviteWay);
			act.setOutData("inviteWay2", inviteWay2);
			act.setOutData("planMeetDate", planMeetDate);
			act.setOutData("inviteRemartk", inviteRemartk);
			act.setOutData("customerList", customerList);
			act.setOutData("ctmRank", ctmRank);
			act.setOutData("ctmRank2", ctmRank2);
			act.setOutData("auditRemark", auditRemark);
			act.setOutData("directorAudit", directorAudit);
			act.setOutData("x1", x1);
			act.setOutData("x2", x2);
			act.setOutData("x3", x3);
			act.setOutData("x4", x4);
			act.setOutData("inviteShopDate", inviteShopDate);
			act.setOutData("salesProgress", salesProgress);
			act.setOutData("salesProgress2", salesProgress2);
			act.setOutData("menusAList", menusAList);
			act.setOutData("menusBList", menusBList);
			act.setOutData("menusABList2", menusABList2);
//			act.setOutData("menusABList3", menusABList3);
			act.setOutData("menusAList2", menusAList2);
			act.setOutData("menusBList2", menusBList2);
			act.setOutData("menusAList3", menusAList3);
			act.setOutData("menusBList3", menusBList3);
			//判断为计划邀约还是邀约到店（inviteShopId为""是计划邀约）
			if("".equals(inviteShopId)||inviteShopId==null||"null".equals(inviteShopId)){
				act.setForword(doTaskInviteUrl);//计划邀约页面
			} else {
				act.setForword(doTaskInviteShopUrl);//邀约到店页面
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取车型颜色
	 */
	public void docolor(){
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		String ifpresell=null;
		String pose_id=act.getRequest().getParamValue("pose_id");
		
		//获取意向颜色列表
		List<DynaBean> colorList = dao.getColorList(pose_id);
		
		//根据车型获取产品是否预售
		List<DynaBean> presellList = dao.getPresellList(pose_id);
		DynaBean db3=presellList.get(0);
		//根据车型来获取车系
		if(db3.get("NEW_PRODUCT_SALE")!=null) {
			ifpresell=db3.get("NEW_PRODUCT_SALE").toString();
		}
		act.setOutData("colorList", colorList);
		act.setOutData("ifpresell", ifpresell);
		
	}
	/**
	 * 获取是否预售
	 */
	public void dopresell(){
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		String ifpresell=null;
		String chexId=act.getRequest().getParamValue("chexId");
		//根据车型获取产品是否预售
		List<DynaBean> presellList = dao.getPresellList(chexId);
		DynaBean db3=presellList.get(0);
		//根据车型来获取车系
		if(db3.get("NEW_PRODUCT_SALE")!=null) {
			ifpresell=db3.get("NEW_PRODUCT_SALE").toString();
		}
		act.setOutData("ifpresell", ifpresell);
		
	}
	/**
	 * 获取该客户是否有过首客
	 */
	public void getFirstGuest(){
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId=logonUser.getDealerId().toString();
			String telephone=act.getRequest().getParamValue("telePhone");
			String leadsCode=act.getRequest().getParamValue("leadsCode");
			if(leadsCode==null){
				leadsCode="";
			}
			//首客数量
			String skCount=null;
			//处理首客数量
			String clCount=null;
			//分派首客的时间
			String adDate=null;
			
			//int skCount=CommonUtils.getShouKeCount(telephone, adviser,"");
			//获取该客户是否存在首客信息
			List<DynaBean> firstGuestList=dao.getShouKeCount(telephone, dealerId, leadsCode);
			DynaBean db = firstGuestList.get(0);
			if(db.get("SKCOUNT")!=null) {
				skCount = db.get("SKCOUNT").toString();
			}
			if(db.get("CLCOUNT")!=null) {
				clCount = db.get("CLCOUNT").toString();
			}
			if(db.get("ADDATE")!=null) {
				adDate = db.get("ADDATE").toString();
			}
			act.setOutData("skCount", skCount);
			act.setOutData("clCount", clCount);
			act.setOutData("adDate", adDate);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "DCRC录入线索保存时是否有过首客信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	} 
	
	/**
	 * 获取该老客户车架号是否存在
	 */
	public void getOldCustomerVin(){
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId=logonUser.getDealerId().toString();
			String vin=act.getRequest().getParamValue("oldVehicleId");
			String telePhone=act.getRequest().getParamValue("telePhone");
			if(vin==null){
				vin="";
			}
			//老客户车架号存不存在
			String oldVin=null;
			String beCount=null;
			String oldCount=null;

			//获取老客户车架号存不存在
			List<DynaBean> oldCustomerVinList=dao.getLaokeVin(vin);
			DynaBean db = oldCustomerVinList.get(0);
			if(db.get("OLDVIN")!=null) {
				oldVin = db.get("OLDVIN").toString();
			}
			
			//获取该客户是否有过老客户推荐
			List<DynaBean> RecommendList=dao.getRecommendCustomer(telePhone);
			DynaBean db2 = RecommendList.get(0);
			if(db2.get("BECOUNT")!=null) {
				beCount = db2.get("BECOUNT").toString();
			}
			//订单页面获取该客户是否有过老客户推荐
			List<DynaBean> OrderOldList=dao.getOrderOldCustomer(vin);
			DynaBean db3 = RecommendList.get(0);
			if(db3.get("OLDCOUNT")!=null) {
				oldCount = db3.get("OLDCOUNT").toString();
			}
			
			
			
			act.setOutData("oldVin", oldVin);
			act.setOutData("beCount", beCount);
			act.setOutData("oldCount", oldCount);
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "DCRC录入线索保存时是否有过首客信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	} 
	/**
	 * 订单任务页面
	 */
	public void doTaskOrderInit() {
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		List a[] = new List[10]; 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String customerId = act.getRequest().getParamValue("customerId");
		String taskId = act.getRequest().getParamValue("taskId");
		String adviserLogon = "no";//顾问登陆标志
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		String beCount=null;
		try {
			//获取客户基本信息2
			List<DynaBean> customerList = dao.getCustomerInfo2(customerId);
			//获取车主基本信息2
			List<DynaBean> ownerList = dao.getOwnerInfo2(taskId);
		
			//获取该客户是否有过老客户推荐
			List<DynaBean> RecommendList=dao.getRecommendCustomerId(customerId);
			DynaBean dbr = RecommendList.get(0);
			if(dbr.get("BECOUNT")!=null) {
				beCount = dbr.get("BECOUNT").toString();
			}
			//获取省市区
			String province = null;
			String city = null;
			String area = null;
			String buyWay = null;
			String buyWay2 = null;
			String address = null;
			String intentVehicle = null;
			String seriesName = null;
			String intentChex=null;
			String ifpresell=null;
			String oldCustomerName=null;
			String oldTelephone=null;
			String oldVehicleId=null;
			String relation_code=null;
			DynaBean db = ownerList.get(0);
			DynaBean db2 = customerList.get(0);
		
			if(db.get("PROVINCE")!=null) {
				province = db.get("PROVINCE").toString();
			}
			if(db.get("CITY")!=null) {
				city = db.get("CITY").toString();
			}
			if(db.get("AREA")!=null) {
				area = db.get("AREA").toString();
			}
			//获取购置方式
			if(db2.get("BUY_WAY")!=null) {
				buyWay = db2.get("BUY_WAY").toString();
			}
			if(db2.get("BUY_WAY2")!=null) {
				buyWay2 = db2.get("BUY_WAY2").toString();
			}
			//获取客户地址
			if(db2.get("ADDRESS")!=null) {
				address = db2.get("ADDRESS").toString();
			}
			//获取意向车型
			if(db2.get("INTENT_VEHICLE")!=null) {
				intentVehicle = db2.get("INTENT_VEHICLE").toString();
			}
			//获取意向车型
			if(db2.get("SERIES_NAME")!=null) {
				seriesName = db2.get("SERIES_NAME").toString();
			}
			
			if(!beCount.equals("0")){
				System.out.println("beCount:"+beCount);
			//获取老客户推荐信息
				List<DynaBean> oldList = dao.getOldCustomerInfo(customerId);
				
				DynaBean dbo = oldList.get(0);
				
				//获取老客户姓名
				if(dbo.get("LINK_MAN")!=null) {
					oldCustomerName = dbo.get("LINK_MAN").toString();
				}
				//获取老客户电话
				if(dbo.get("LINK_PHONE")!=null) {
					oldTelephone = dbo.get("LINK_PHONE").toString();
				}
				//获取老客户车架号
				if(dbo.get("OLD_VEHICLE_ID")!=null) {
					oldVehicleId = dbo.get("OLD_VEHICLE_ID").toString();
				}
				//获取老客户车架号
				if(dbo.get("RELATION_CODE")!=null) {
					relation_code = dbo.get("RELATION_CODE").toString();
					relation_code=relation_code.trim();
				}
				System.out.println("relation_code:"+relation_code);
				
			}
			
		
			
			
			//获取意向车系
			//if(db2.get("INTENT_VEHICLE")!=null) {
			//	intentChex=intentVehicle.substring(0, 4); 
			//}
			//根据车型获取产品是否预售
			List<DynaBean> presellList = dao.getPresellList(intentVehicle);
			DynaBean db3=presellList.get(0);
			//根据车型来获取车系
			if(db3.get("NEW_PRODUCT_SALE")!=null) {
				ifpresell=db3.get("NEW_PRODUCT_SALE").toString();
			}
			System.out.println("意向车系为："+ifpresell);
			//获取意向颜色列表
			List<DynaBean> colorList = dao.getColorList(intentVehicle);
			//获取战败车型一级列表
			List<DynaBean> menusAList2 = dao.getDefeatVehicleA();
			//获取战败车型二级列表
			List<DynaBean> menusBList2 = dao.getDefeatVehicleB();
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			    adviserLogon = "yes";
			}
			
			act.setOutData("adviserLogon", adviserLogon);
			act.setOutData("province", province);
			act.setOutData("city", city);
			act.setOutData("area", area);
			act.setOutData("buyWay", buyWay);
			act.setOutData("buyWay2", buyWay2);
			act.setOutData("address", address);
			act.setOutData("intentVehicle", intentVehicle);
			act.setOutData("seriesName", seriesName);
			act.setOutData("ifpresell", ifpresell);
			act.setOutData("customerId", customerId);
			act.setOutData("taskId", taskId);
			act.setOutData("dealerCode", logonUser.getDealerCode());
			act.setOutData("ownerList", ownerList);
			act.setOutData("customerList", customerList);
			act.setOutData("menusAList2", menusAList2);
			act.setOutData("menusBList2", menusBList2);
			act.setOutData("colorList", colorList);
			act.setOutData("nowDate", nowDate);
			act.setOutData("oldCustomerName", oldCustomerName);
			act.setOutData("oldTelephone", oldTelephone);
			act.setOutData("oldVehicleId", oldVehicleId);
			act.setOutData("relation_code", relation_code);
			act.setForword(doTaskOrderUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订单退单任务页面
	 */
	public void doTaskOrderBackInit() {
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		List a[] = new List[10]; 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String customerId = act.getRequest().getParamValue("customerId");
		String taskId = act.getRequest().getParamValue("taskId");
		String orderStatus = act.getRequest().getParamValue("orderStatus");
		String adviserLogon = "no";//顾问登陆标志
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			//从客户表中中获取客户姓名和联系电话
			//获取车主基本信息2
			List<DynaBean> customerList = dao.getOwnerInfo(taskId);
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			    adviserLogon = "yes";
			}
			//获取战败车型一级列表
			List<DynaBean> menusAList2 = dao.getDefeatVehicleA();
			//获取战败车型二级列表
			List<DynaBean> menusBList2 = dao.getDefeatVehicleB();
			
			act.setOutData("adviserLogon", adviserLogon);
			act.setOutData("customerId", customerId);
			act.setOutData("taskId", taskId);
			act.setOutData("orderStatus", orderStatus);
			act.setOutData("dealerCode", logonUser.getDealerCode());
			act.setOutData("customerList", customerList);
			act.setOutData("menusAList2", menusAList2);
			act.setOutData("menusBList2", menusBList2);
			act.setOutData("nowDate", nowDate);
			act.setForword(doTaskOrderBackUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 交车任务页面
	 */
	public void doTaskDeliveryInit() {
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		List a[] = new List[10]; 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String customerId = act.getRequest().getParamValue("customerId");
		String taskId = act.getRequest().getParamValue("taskId");
		String orderId = act.getRequest().getParamValue("orderId");
		String adviserLogon = "no";//顾问登陆标志
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			if(orderId==null||"".equals(orderId)||"null".equals(orderId)) {
				//根据taskId获取orderId（taskId为detailOrderId）
				TPcOrderDetailPO orderDeatilPo = new TPcOrderDetailPO();
				orderDeatilPo.setOrderDetailId(Long.parseLong(taskId));
				List<PO> po = dao.select(orderDeatilPo);
				orderDeatilPo = (TPcOrderDetailPO)po.get(0);
				orderId = orderDeatilPo.getOrderId().toString();
			}
			//获取订单状态
			TPcOrderPO orderPo = new TPcOrderPO();
			orderPo.setOrderId(Long.parseLong(orderId));
			TPcOrderPO orderPo2 = (TPcOrderPO)dao.select(orderPo).get(0);
			String orderStatus = orderPo2.getOrderStatus().toString();
			
			//从客户表中中获取客户姓名和联系电话
			//获取客户基本信息2
			List<DynaBean> customerList = dao.getCustomerInfo2(customerId);
			//获取车主基本信息
			List<DynaBean> ownerList = dao.getOwnerInfo(orderId);
			//获取Table1信息
			List<DynaBean> table1List = dao.getTable1Info(orderId,orderStatus);
			//获取Table2信息
			List<DynaBean> table2List = dao.getTable2Info(orderId,orderStatus);
			
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			    adviserLogon = "yes";
			}
			
			act.setOutData("customerId", customerId);
			act.setOutData("taskId", taskId);
			act.setOutData("orderId", orderId);
			act.setOutData("dealerCode", logonUser.getDealerCode());
			act.setOutData("customerList", customerList);
			act.setOutData("ownerList", ownerList);
			act.setOutData("table1List", table1List);
			act.setOutData("table2List", table2List);
			act.setOutData("nowDate", nowDate);
			act.setOutData("adviserLogon", adviserLogon);
			act.setForword(doTaskDeliveryUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 开始任务（所有任务通用方法，判断标志:taskType）
	 */
	public void doTask() {
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		OemLeadsManageDao dao2 = new OemLeadsManageDao();
		List a[] = new List[10]; 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String taskType = act.getRequest().getParamValue("taskType"); //当前任务类型
		String customerId = act.getRequest().getParamValue("customerId"); //客户ID
		String taskId = act.getRequest().getParamValue("taskId"); // 任务ID
		String inviteShopId = act.getRequest().getParamValue("inviteShopId"); // 邀约到店任务ID
		String typeFlag = act.getRequest().getParamValue("typeFlag"); //点击按钮类型（跟进、邀约、订车、战败、失效）
		String ifInviteSuccess = act.getRequest().getParamValue("if_invite"); //是否邀约成功
		String customerName = act.getRequest().getParamValue("customer_name");//客户姓名
		String telePhone = act.getRequest().getParamValue("telephone");//联系电话salesProgress
		String salesProgress = act.getRequest().getParamValue("sales_progress");//销售流程进度
		String collectFashion = act.getRequest().getParamValue("collect_fashion");//集客方式
		String buyBudget = act.getRequest().getParamValue("buy_budget");//购车预算
		String customerType = act.getRequest().getParamValue("customer_type");//客户类型
		String intentVehicle = act.getRequest().getParamValue("intentVehicleB");//意向车型
		String intentType = act.getRequest().getParamValue("intent_type");//意向等级
		String followInfo = act.getRequest().getParamValue("follow_info");//跟进说明
		String inviteInfo = act.getRequest().getParamValue("invite_info");//邀约成效
		String inviteShopInfo = act.getRequest().getParamValue("invite_shop_info");//到店结果记录
		String nextFollowDate = act.getRequest().getParamValue("next_follow_date");//下次跟进时间
		String followType = act.getRequest().getParamValue("follow_type");//跟进方式
		String followPlan = act.getRequest().getParamValue("follow_plan");//跟进计划
		String xqfx = act.getRequest().getParamValue("xqfx"); //需求分析
		String yymb = act.getRequest().getParamValue("yymb"); //邀约目标
		String ydkhxrsj = act.getRequest().getParamValue("ydkhxrsj"); //赢得客户信任设计
		String gdkhqjsj = act.getRequest().getParamValue("gdkhqjsj"); //感动客户情景设计
		String inviteType = act.getRequest().getParamValue("invite_type"); //邀约类型
		String inviteWay = act.getRequest().getParamValue("invite_way"); //邀约方式
		String inviteRemark = act.getRequest().getParamValue("invite_info"); //邀约成效
		String inviteShopRemark = act.getRequest().getParamValue("invite_shop_info"); //邀约到店成效
		String planInviteDate = act.getRequest().getParamValue("plan_invite_date"); //计划邀约时间
		String planMeetDate = act.getRequest().getParamValue("plan_meet_date"); //计划见面时间
		String inviteTypeNew = act.getRequest().getParamValue("invite_type_new"); //邀约方式
		String inviteShopDate = act.getRequest().getParamValue("invite_shop_date"); //预约到店时间
		String orderDate = act.getRequest().getParamValue("order_date"); //订车时间
		String defeatVehicle = act.getRequest().getParamValue("defeatVehicleB");//战败车型
		String defeatReason = act.getRequest().getParamValue("defeatReasonB");//战败原因
		String defeatRemark = act.getRequest().getParamValue("reason_analysis");//战败说明
		String defeatEndDate = act.getRequest().getParamValue("defeat_end_date");//战败结束日期
		String failureDate = act.getRequest().getParamValue("failure_date");//失效日期
		String failureRemark = act.getRequest().getParamValue("failure_remark");//失效说明
		String ownerName = act.getRequest().getParamValue("owner_name");//车主姓名
		String ownerPhone = act.getRequest().getParamValue("owner_phone");//车主姓名
		String ownerPaperType = act.getRequest().getParamValue("owner_paper_type");//证件类型
		String ownerPaperNo = act.getRequest().getParamValue("owner_paper_no");//证件号码
		String dPro = CommonUtils.checkNull(act.getRequest().getParamValue("dPro"));//省
		String dCity = CommonUtils.checkNull(act.getRequest().getParamValue("dCity"));//市
		String dArea = CommonUtils.checkNull(act.getRequest().getParamValue("dArea"));//区
		String ownerAddress = act.getRequest().getParamValue("owner_address");//详细地址
		String newProductSale = act.getRequest().getParamValue("new_product_sale");//新产品预售
		String dealType = act.getRequest().getParamValue("deal_type");//成交类型
		String testDriving = act.getRequest().getParamValue("test_driving");//试乘试驾
		
		
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			//PC和移动端同时处理问题
			int	ifHandle = 0;
			int	ifHandle2 = 0;
			int	ifHandle3 = 0;
			if(taskId!=null&&!"".equals(taskId)) {
				ifHandle = CommonUtils.getTheSameData2(taskId);
			}
			if(inviteShopId!=null&&!"".equals(inviteShopId)) {
				ifHandle2 = CommonUtils.getTheSameData3(inviteShopId);
			}
			if(taskId!=null&&!"".equals(taskId)) {
				ifHandle3 = CommonUtils.getTheSameData4(taskId);
			}
			if(ifHandle==10041002||ifHandle2==10041002||ifHandle3==10041002||"order".equals(taskType)||"orderBack".equals(taskType)||"orderYN".equals(taskType)) {//未处理的情况
				//验证是否已建立了档案（根据客户名称、联系电话和经销商ID）
				//根据customerId获取客户姓名和联系电话
				List<DynaBean> tempList = dao.getNameAndTelephoneByCustomerId(customerId);
				String oldCustomerName = null;
				String oldTelephone = null;
				Iterator it = (Iterator) tempList.iterator();
				while(it.hasNext()) {
					DynaBean db = (DynaBean)it.next();
					oldCustomerName = db.get("CUSTOMER_NAME").toString();
					oldTelephone = db.get("TELEPHONE").toString();
				}
				//判断姓名和电话是否有改动，有改动就做 验证是否已建立了档案，没有改动就不做验证
				List<Map<String, Object>> getHasList = new ArrayList<Map<String,Object>>();
				if((!oldCustomerName.trim().equals(customerName.trim()) || !oldTelephone.trim().equals(telePhone.trim())) &&!"orderBack".equals(taskType)) {
					getHasList=dao2.getHasCustomer(telePhone,customerName,logonUser.getDealerId());
				}
				//已存在档案，发送错误消息到页面
				if(getHasList.size()>0) {
					act.setOutData("errorMsg", "该客户:"+customerName+",已存在客户档案,保存失败!");
					
					//判断当前为哪种任务页面
					if(taskType.equals("follow")) {//跟进页面
						act.setOutData("customerId", customerId);
						act.setOutData("taskId", taskId);
						doTaskFollowInit();
					}
					
					
				} else if(taskType.equals("follow")){//跟进页面
					String newTaskId = SequenceManager.getSequence("");
					//标记本次跟进任务表为完成状态
					TPcFollowPO oldFollowPo = new TPcFollowPO();
					oldFollowPo.setFollowId(Long.parseLong(taskId));
					TPcFollowPO newFollowPo = new TPcFollowPO();
					newFollowPo.setFollowId(Long.parseLong(taskId));
					newFollowPo.setNewLevel(intentType);
					newFollowPo.setFinishDate(new Date());
					newFollowPo.setFollowInfo(followInfo);
					newFollowPo.setNextTask(Long.parseLong(newTaskId));
					newFollowPo.setTaskStatus(Constant.TASK_STATUS_02);
					newFollowPo.setSalesProgress(salesProgress);
					newFollowPo.setIfHandle(10041001);
					dao.update(oldFollowPo, newFollowPo);
					
					//修改客户档案信息
					TPcCustomerPO oldCustomerPo = new TPcCustomerPO();
					oldCustomerPo.setCustomerId(Long.parseLong(customerId));
					TPcCustomerPO newCustomerPo = new TPcCustomerPO();
					newCustomerPo.setCustomerId(Long.parseLong(customerId));
					newCustomerPo.setCustomerName(customerName);
					newCustomerPo.setTelephone(telePhone);
					newCustomerPo.setCtmRank(intentType);
					newCustomerPo.setSalesProgress(salesProgress);
//					newCustomerPo.setJcWay(collectFashion);
					newCustomerPo.setBuyBudget(buyBudget);
					newCustomerPo.setCtmProp(customerType);
					newCustomerPo.setIntentVehicle(intentVehicle);
					if(typeFlag=="followRadio"||"followRadio".equals(typeFlag)){//跟进
						newCustomerPo.setNextFollowTime(sdf.parse(nextFollowDate));//下次跟进时间
					} else if(typeFlag=="inviteRadio"||"inviteRadio".equals(typeFlag)) {
						newCustomerPo.setNextFollowTime(sdf.parse(planInviteDate));//下次跟进时间
					}
					dao.update(oldCustomerPo,newCustomerPo);
					
					//增加接触点信息
					CommonUtils.addContackPoint(Constant.POINT_WAY_04, followInfo, customerId, logonUser.getUserId().toString(), logonUser.getDealerId());
					
					if(typeFlag=="followRadio"||"followRadio".equals(typeFlag)) {//点击跟进后保存
						//新增下次跟进任务表
						TPcFollowPO followPo = new TPcFollowPO();
						followPo.setFollowId(Long.parseLong(newTaskId));
						followPo.setCustomerId(Long.parseLong(customerId));
						followPo.setFollowDate(sdf.parse(nextFollowDate));
						followPo.setOldLevel(intentType);
						followPo.setNewLevel(null);
						followPo.setTaskStatus(Constant.TASK_STATUS_01);
						followPo.setFollowType(followType);
						followPo.setFollowPlan(followPlan);
						followPo.setCreateDate(new Date());
						followPo.setCreateBy(logonUser.getUserId().toString());
						followPo.setPreviousTask(Long.parseLong(taskId));
						followPo.setOldSalesProgress(salesProgress);
						dao.insert(followPo);
						
						//增加一条接触点信息
//						TPcContactPointPO pointPo = new TPcContactPointPO();
//						String pointId = SequenceManager.getSequence("");
//						pointPo.setPointId(Long.parseLong(pointId));
//						pointPo.setPointDate(new Date());
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), nextFollowDate,"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="inviteRadio"||"inviteRadio".equals(typeFlag)) {//点击邀约后保存
						//新增下次计划邀约任务
						TPcInvitePO invitePo = new TPcInvitePO();
						invitePo.setInviteId(Long.parseLong(newTaskId));
						invitePo.setCustomerId(Long.parseLong(customerId));
						invitePo.setCreateDate(new Date());
						invitePo.setCreateBy(logonUser.getUserId().toString());
						invitePo.setPlanInviteDate(sdf.parse(planInviteDate));
						invitePo.setPlanMeetDate(sdf.parse(planMeetDate));
						invitePo.setTaskStatus(Constant.TASK_STATUS_01);
						invitePo.setInviteType(Integer.parseInt(inviteTypeNew));
						invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_04);
						invitePo.setIfPlan(Constant.IF_TYPE_NO);
						invitePo.setOldLevel(intentType);
						invitePo.setNewLevel(null);
						invitePo.setPreviousTask(Long.parseLong(taskId));
						invitePo.setOldSalesProgress(salesProgress);
						//判断是否做了计划（4个框有值说明为做了计划）
						if(xqfx!=null&&!"".equals(xqfx)) {
							invitePo.setIfPlan(Constant.IF_TYPE_YES);
							invitePo.setRequirement(xqfx);
							invitePo.setInviteTarget(yymb);
							invitePo.setTrustDesign(ydkhxrsj);
							invitePo.setSceneDesign(gdkhqjsj);
							invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_01);
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_15.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						} else {
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), planInviteDate,"");
						}
						dao.insert(invitePo);
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="orderRadio"||"orderRadio".equals(typeFlag)) {//点击订单后保存
						//新增下次订单任务
						TPcOrderPO orderPo = new TPcOrderPO();
						orderPo.setOrderId(Long.parseLong(newTaskId));
						orderPo.setCustomerId(Long.parseLong(customerId));
						orderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
						orderPo.setOldLevel(intentType);
						orderPo.setCreateDate(new Date());
						orderPo.setCreateBy(logonUser.getUserId().toString());
						orderPo.setOldSalesProgress(salesProgress);
						orderPo.setPreviousTask(Long.parseLong(taskId));
						orderPo.setTaskStatus(Constant.TASK_STATUS_01);
						orderPo.setOrderDate(sdf.parse(orderDate));
						dao.insert(orderPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_08.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), orderDate,"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="defeatRadio"||"defeatRadio".equals(typeFlag)) {//点击战败后保存
						//获取客户当前意向等级和销售流程
						TPcCustomerPO customerPo = new TPcCustomerPO();
						customerPo.setCustomerId(Long.parseLong(customerId));
						List<PO> po = dao.select(customerPo);
						customerPo = (TPcCustomerPO)po.get(0);
						String ctmRank = customerPo.getCtmRank();
						String sProgress = customerPo.getSalesProgress();
						//新增战败数据
						TPcDefeatfailurePO defeatPo = new TPcDefeatfailurePO();
						defeatPo.setDefeatfailureId(Long.parseLong(newTaskId));
						defeatPo.setCustomerId(Long.parseLong(customerId));
						defeatPo.setDefeatfailureType(Constant.FAILURE_TYPE_01);
						defeatPo.setDefeatModel(defeatVehicle);
						defeatPo.setDefeatEndDate(sdf.parse(defeatEndDate));
						defeatPo.setStatus(Constant.STATUS_ENABLE);
						defeatPo.setOldLevel(ctmRank);
						defeatPo.setNewLevel(Constant.INTENT_TYPE_E.toString());
						defeatPo.setOldSalesProgress(sProgress);
						defeatPo.setSalesProgress(Constant.SALSE_PROGRESS_01.toString());
						defeatPo.setReasonAnalysis(defeatRemark);
						defeatPo.setCreateBy(logonUser.getUserId().toString());
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(taskId));
						defeatPo.setDefeatType(defeatReason);
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为战败
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_E.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_10.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="failureRadio"||"failureRadio".equals(typeFlag)) {//点击失效后保存
						//获取客户当前意向等级和销售流程
						TPcCustomerPO customerPo = new TPcCustomerPO();
						customerPo.setCustomerId(Long.parseLong(customerId));
						List<PO> po = dao.select(customerPo);
						customerPo = (TPcCustomerPO)po.get(0);
						String ctmRank = customerPo.getCtmRank();
						String sProgress = customerPo.getSalesProgress();
						//新增失效数据
						TPcDefeatfailurePO defeatPo = new TPcDefeatfailurePO();
						defeatPo.setDefeatfailureId(Long.parseLong(newTaskId));
						defeatPo.setCustomerId(Long.parseLong(customerId));
						defeatPo.setDefeatfailureType(Constant.FAILURE_TYPE_02);
						defeatPo.setStatus(Constant.STATUS_ENABLE);
						defeatPo.setOldLevel(ctmRank);
						defeatPo.setNewLevel(Constant.INTENT_TYPE_L.toString());
						defeatPo.setOldSalesProgress(sProgress);
						defeatPo.setSalesProgress(Constant.SALSE_PROGRESS_01.toString());
						defeatPo.setReasonAnalysis(failureRemark);
						defeatPo.setCreateBy(logonUser.getUserId().toString());
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(taskId));
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						defeatPo.setFailureDate(sdf.parse(failureDate));
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为失效
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_L.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_11.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					}
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(taskId,Constant.REMIND_TYPE_05.toString());
				} else if(taskType.equals("invite")){//计划邀约页面
					String newTaskId = SequenceManager.getSequence("");
					//标记本次计划邀约任务完成
					TPcInvitePO oldInvitePo = new TPcInvitePO();
					oldInvitePo.setInviteId(Long.parseLong(taskId));
					TPcInvitePO newInvitePo = new TPcInvitePO();
					newInvitePo.setInviteId(Long.parseLong(taskId));
					newInvitePo.setRemark(inviteRemark);
					newInvitePo.setInviteType(Integer.parseInt(inviteType.trim()));
					newInvitePo.setInviteWay(Integer.parseInt(inviteWay.trim()));
					newInvitePo.setNewLevel(intentType);
					newInvitePo.setTaskStatus(Constant.TASK_STATUS_02);
					newInvitePo.setFinishDate(new Date());
					newInvitePo.setNextTask(Long.parseLong(newTaskId));
					newInvitePo.setSalesProgress(salesProgress);
					if(ifInviteSuccess=="10041001"||"10041001".equals(ifInviteSuccess)){//邀约成功
						newInvitePo.setIfInvite(Constant.IF_TYPE_YES);
					} else {
						newInvitePo.setIfInvite(Constant.IF_TYPE_NO);
					}
					newInvitePo.setIfHandle(10041001);
					dao.update(oldInvitePo, newInvitePo);
					
					//修改客户档案信息
					TPcCustomerPO oldCustomerPo = new TPcCustomerPO();
					oldCustomerPo.setCustomerId(Long.parseLong(customerId));
					TPcCustomerPO newCustomerPo = new TPcCustomerPO();
					newCustomerPo.setCustomerId(Long.parseLong(customerId));
					newCustomerPo.setCtmRank(intentType);
					newCustomerPo.setSalesProgress(salesProgress);
					if(ifInviteSuccess=="10041001"||"10041001".equals(ifInviteSuccess)){//邀约成功
						newCustomerPo.setNextFollowTime(sdf.parse(inviteShopDate));//下次跟进时间
						newCustomerPo.setPreShopTime(sdf.parse(inviteShopDate));//预计到店时间
					} else if(typeFlag=="followRadio"||"followRadio".equals(typeFlag)){//跟进
						newCustomerPo.setNextFollowTime(sdf.parse(nextFollowDate));//下次跟进时间
					} else if(typeFlag=="inviteRadio"||"inviteRadio".equals(typeFlag)) {//邀约
						newCustomerPo.setNextFollowTime(sdf.parse(planInviteDate));//下次跟进时间
					}
					//判断当前邀约任务的状态情况
				    newCustomerPo.setDefeatRemark("PC端ifInviteSuccess:"+ifInviteSuccess+" typeFlag:"+typeFlag);
					//修复接下来没有任务的问题
					if((ifInviteSuccess==null || "".equals(ifInviteSuccess)) && (typeFlag==null || "".equals(typeFlag)) ) {
						//判断当前邀约任务的状态情况
					    newCustomerPo.setDefeatRemark("PC3y端ifInviteSuccess:"+ifInviteSuccess+" typeFlag:"+typeFlag);
				
						TPcInviteShopPO inviteShopPo = new TPcInviteShopPO();
						inviteShopPo.setInviteShopId(Long.parseLong(newTaskId));
						inviteShopPo.setInviteId(Long.parseLong(taskId));
						inviteShopPo.setCustomerId(Long.parseLong(customerId));
						inviteShopPo.setCreateDate(new Date());
						inviteShopPo.setCreateBy(logonUser.getUserId().toString());
						inviteShopPo.setOldLevel(intentType);
						inviteShopPo.setInviteShopDate(sdf.parse(inviteShopDate));
						inviteShopPo.setTaskStatus(Constant.TASK_STATUS_01);
						inviteShopPo.setPreviousTask(Long.parseLong(taskId));
						inviteShopPo.setOldSalesProgress(salesProgress);
						dao.insert(inviteShopPo);
						
						//增加接触点信息
						CommonUtils.addContackPoint(Constant.POINT_WAY_05, inviteInfo, customerId, logonUser.getUserId().toString(), logonUser.getDealerId());
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_07.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), inviteShopDate,"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					}
			
				    
					if(ifInviteSuccess=="10041001"||"10041001".equals(ifInviteSuccess)) {//选择邀约成功后保存
						//新增下次邀约到店任务表
						
						//判断当前邀约任务的状态情况
					    newCustomerPo.setDefeatRemark("PC2y端ifInviteSuccess:"+ifInviteSuccess+" typeFlag:"+typeFlag);
				
						TPcInviteShopPO inviteShopPo = new TPcInviteShopPO();
						inviteShopPo.setInviteShopId(Long.parseLong(newTaskId));
						inviteShopPo.setInviteId(Long.parseLong(taskId));
						inviteShopPo.setCustomerId(Long.parseLong(customerId));
						inviteShopPo.setCreateDate(new Date());
						inviteShopPo.setCreateBy(logonUser.getUserId().toString());
						inviteShopPo.setOldLevel(intentType);
						inviteShopPo.setInviteShopDate(sdf.parse(inviteShopDate));
						inviteShopPo.setTaskStatus(Constant.TASK_STATUS_01);
						inviteShopPo.setPreviousTask(Long.parseLong(taskId));
						inviteShopPo.setOldSalesProgress(salesProgress);
						dao.insert(inviteShopPo);
						
						//增加接触点信息
						CommonUtils.addContackPoint(Constant.POINT_WAY_05, inviteInfo, customerId, logonUser.getUserId().toString(), logonUser.getDealerId());
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_07.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), inviteShopDate,"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="followRadio"||"followRadio".equals(typeFlag)) { //点击跟进按钮后保存
						//新增下次跟进任务表
						TPcFollowPO followPo = new TPcFollowPO();
						followPo.setFollowId(Long.parseLong(newTaskId));
						followPo.setCustomerId(Long.parseLong(customerId));
						followPo.setFollowDate(sdf.parse(nextFollowDate));
						followPo.setOldLevel(intentType);
						followPo.setNewLevel(null);
						followPo.setTaskStatus(Constant.TASK_STATUS_01);
						followPo.setFollowType(followType);
						followPo.setFollowPlan(followPlan);
						followPo.setCreateDate(new Date());
						followPo.setCreateBy(logonUser.getUserId().toString());
						followPo.setPreviousTask(Long.parseLong(taskId));
						followPo.setOldSalesProgress(salesProgress);
						dao.insert(followPo);
						
						//增加一条接触点信息
//						TPcContactPointPO pointPo = new TPcContactPointPO();
//						String pointId = SequenceManager.getSequence("");
//						pointPo.setPointId(Long.parseLong(pointId));
//						pointPo.setPointDate(new Date());
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), nextFollowDate,"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="inviteRadio"||"inviteRadio".equals(typeFlag)) {//点击邀约按钮后保存
						//新增下次计划邀约任务
						TPcInvitePO invitePo = new TPcInvitePO();
						invitePo.setInviteId(Long.parseLong(newTaskId));
						invitePo.setCustomerId(Long.parseLong(customerId));
						invitePo.setCreateDate(new Date());
						invitePo.setCreateBy(logonUser.getUserId().toString());
						invitePo.setPlanInviteDate(sdf.parse(planInviteDate));
						invitePo.setPlanMeetDate(sdf.parse(planMeetDate));
						invitePo.setTaskStatus(Constant.TASK_STATUS_01);
						invitePo.setInviteType(Integer.parseInt(inviteTypeNew));
						invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_04);
						invitePo.setIfPlan(Constant.IF_TYPE_NO);
						invitePo.setOldLevel(intentType);
						invitePo.setNewLevel(null);
						invitePo.setPreviousTask(Long.parseLong(taskId));
						invitePo.setOldSalesProgress(salesProgress);
						//判断是否做了计划（4个框有值说明为做了计划）
						if(xqfx!=null&&!"".equals(xqfx)) {
							invitePo.setIfPlan(Constant.IF_TYPE_YES);
							invitePo.setRequirement(xqfx);
							invitePo.setInviteTarget(yymb);
							invitePo.setTrustDesign(ydkhxrsj);
							invitePo.setSceneDesign(gdkhqjsj);
							invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_01);
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_15.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						} else {
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), planInviteDate,"");
						}
						dao.insert(invitePo);
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="orderRadio"||"orderRadio".equals(typeFlag)) {//点击订单后保存
						//新增下次订单任务
						TPcOrderPO orderPo = new TPcOrderPO();
						orderPo.setOrderId(Long.parseLong(newTaskId));
						orderPo.setCustomerId(Long.parseLong(customerId));
						orderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
						orderPo.setOldLevel(intentType);
						orderPo.setCreateDate(new Date());
						orderPo.setCreateBy(logonUser.getUserId().toString());
						orderPo.setOldSalesProgress(salesProgress);
						orderPo.setPreviousTask(Long.parseLong(taskId));
						orderPo.setTaskStatus(Constant.TASK_STATUS_01);
						orderPo.setOrderDate(sdf.parse(orderDate));
						dao.insert(orderPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_08.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), orderDate,"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="defeatRadio"||"defeatRadio".equals(typeFlag)) {//点击战败后保存
						//获取客户当前意向等级和销售流程
						TPcCustomerPO customerPo = new TPcCustomerPO();
						customerPo.setCustomerId(Long.parseLong(customerId));
						List<PO> po = dao.select(customerPo);
						customerPo = (TPcCustomerPO)po.get(0);
						String ctmRank = customerPo.getCtmRank();
						String sProgress = customerPo.getSalesProgress();
						//新增战败数据
						TPcDefeatfailurePO defeatPo = new TPcDefeatfailurePO();
						defeatPo.setDefeatfailureId(Long.parseLong(newTaskId));
						defeatPo.setCustomerId(Long.parseLong(customerId));
						defeatPo.setDefeatfailureType(Constant.FAILURE_TYPE_01);
						defeatPo.setDefeatModel(defeatVehicle);
						defeatPo.setDefeatEndDate(sdf.parse(defeatEndDate));
						defeatPo.setStatus(Constant.STATUS_ENABLE);
						defeatPo.setOldLevel(ctmRank);
						defeatPo.setNewLevel(Constant.INTENT_TYPE_E.toString());
						defeatPo.setOldSalesProgress(sProgress);
						defeatPo.setSalesProgress(Constant.SALSE_PROGRESS_01.toString());
						defeatPo.setReasonAnalysis(defeatRemark);
						defeatPo.setCreateBy(logonUser.getUserId().toString());
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(taskId));
						defeatPo.setDefeatType(defeatReason);
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为战败
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_E.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_10.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="failureRadio"||"failureRadio".equals(typeFlag)) {//点击失效后保存
						//获取客户当前意向等级和销售流程
						TPcCustomerPO customerPo = new TPcCustomerPO();
						customerPo.setCustomerId(Long.parseLong(customerId));
						List<PO> po = dao.select(customerPo);
						customerPo = (TPcCustomerPO)po.get(0);
						String ctmRank = customerPo.getCtmRank();
						String sProgress = customerPo.getSalesProgress();
						//新增失效数据
						TPcDefeatfailurePO defeatPo = new TPcDefeatfailurePO();
						defeatPo.setDefeatfailureId(Long.parseLong(newTaskId));
						defeatPo.setCustomerId(Long.parseLong(customerId));
						defeatPo.setDefeatfailureType(Constant.FAILURE_TYPE_02);
						defeatPo.setStatus(Constant.STATUS_ENABLE);
						defeatPo.setOldLevel(ctmRank);
						defeatPo.setNewLevel(Constant.INTENT_TYPE_L.toString());
						defeatPo.setOldSalesProgress(sProgress);
						defeatPo.setSalesProgress(Constant.SALSE_PROGRESS_01.toString());
						defeatPo.setReasonAnalysis(failureRemark);
						defeatPo.setCreateBy(logonUser.getUserId().toString());
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(taskId));
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						defeatPo.setFailureDate(sdf.parse(failureDate));
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为失效
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_L.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_11.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					}
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(taskId,Constant.REMIND_TYPE_06.toString());
					
					//修改客户档案信息
					dao.update(oldCustomerPo,newCustomerPo);
					
					
				} else if(taskType.equals("inviteShop")) {//邀约到店页面
					String newTaskId = SequenceManager.getSequence("");
					//标记本次邀约到店任务完成
					TPcInviteShopPO oldInviteShopPo = new TPcInviteShopPO();
					oldInviteShopPo.setInviteShopId(Long.parseLong(inviteShopId));
					TPcInviteShopPO newInviteShopPo = new TPcInviteShopPO();
					newInviteShopPo.setInviteShopId(Long.parseLong(inviteShopId));
					newInviteShopPo.setRemark(inviteShopRemark);
					newInviteShopPo.setNewLevel(intentType);
					newInviteShopPo.setTaskStatus(Constant.TASK_STATUS_02);
					newInviteShopPo.setFinishDate(new Date());
					newInviteShopPo.setNextTask(Long.parseLong(newTaskId));
					newInviteShopPo.setSalesProgress(salesProgress);
					if(ifInviteSuccess=="10041001"||"10041001".equals(ifInviteSuccess)){//是到店
						newInviteShopPo.setIfShop(Constant.IF_TYPE_YES);
						newInviteShopPo.setShopDate(sdf.parse(inviteShopDate));
					} else {
						newInviteShopPo.setIfShop(Constant.IF_TYPE_NO);
					}
					newInviteShopPo.setIfHandle(10041001);
					dao.update(oldInviteShopPo, newInviteShopPo);
					
					//修改客户档案信息
					TPcCustomerPO oldCustomerPo = new TPcCustomerPO();
					oldCustomerPo.setCustomerId(Long.parseLong(customerId));
					TPcCustomerPO newCustomerPo = new TPcCustomerPO();
					newCustomerPo.setCustomerId(Long.parseLong(customerId));
					newCustomerPo.setCtmRank(intentType);
					newCustomerPo.setIntentVehicle(intentVehicle);
					newCustomerPo.setSalesProgress(salesProgress);
					if(typeFlag=="followRadio"||"followRadio".equals(typeFlag)){//跟进
						newCustomerPo.setNextFollowTime(sdf.parse(nextFollowDate));//下次跟进时间
					} else if(typeFlag=="inviteRadio"||"inviteRadio".equals(typeFlag)) {//邀约
						newCustomerPo.setNextFollowTime(sdf.parse(planInviteDate));//下次跟进时间
					} 
					dao.update(oldCustomerPo,newCustomerPo);
					
					if(typeFlag=="followRadio"||"followRadio".equals(typeFlag)) { //点击跟进按钮后保存
						//新增下次跟进任务表
						TPcFollowPO followPo = new TPcFollowPO();
						followPo.setFollowId(Long.parseLong(newTaskId));
						followPo.setCustomerId(Long.parseLong(customerId));
						followPo.setFollowDate(sdf.parse(nextFollowDate));
						followPo.setOldLevel(intentType);
						followPo.setNewLevel(null);
						followPo.setTaskStatus(Constant.TASK_STATUS_01);
						followPo.setFollowType(followType);
						followPo.setFollowPlan(followPlan);
						followPo.setCreateDate(new Date());
						followPo.setCreateBy(logonUser.getUserId().toString());
						followPo.setPreviousTask(Long.parseLong(taskId));
						followPo.setOldSalesProgress(salesProgress);
						dao.insert(followPo);
						
						//增加接触点信息
						CommonUtils.addContackPoint(Constant.POINT_WAY_06, inviteShopInfo, customerId, logonUser.getUserId().toString(), logonUser.getDealerId());
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), nextFollowDate,"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="inviteRadio"||"inviteRadio".equals(typeFlag)) {//点击邀约按钮后保存
						//新增下次计划邀约任务
						TPcInvitePO invitePo = new TPcInvitePO();
						invitePo.setInviteId(Long.parseLong(newTaskId));
						invitePo.setCustomerId(Long.parseLong(customerId));
						invitePo.setCreateDate(new Date());
						invitePo.setCreateBy(logonUser.getUserId().toString());
						invitePo.setPlanInviteDate(sdf.parse(planInviteDate));
						invitePo.setPlanMeetDate(sdf.parse(planMeetDate));
						invitePo.setTaskStatus(Constant.TASK_STATUS_01);
						invitePo.setInviteType(Integer.parseInt(inviteTypeNew));
						invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_04);
						invitePo.setIfPlan(Constant.IF_TYPE_NO);
						invitePo.setOldLevel(intentType);
						invitePo.setNewLevel(null);
						invitePo.setPreviousTask(Long.parseLong(taskId));
						invitePo.setOldSalesProgress(salesProgress);
						//判断是否做了计划（4个框有值说明为做了计划）
						if(xqfx!=null&&!"".equals(xqfx)) {
							invitePo.setIfPlan(Constant.IF_TYPE_YES);
							invitePo.setRequirement(xqfx);
							invitePo.setInviteTarget(yymb);
							invitePo.setTrustDesign(ydkhxrsj);
							invitePo.setSceneDesign(gdkhqjsj);
							invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_01);
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_15.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						} else {
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), planInviteDate,"");
						}
						dao.insert(invitePo);
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="orderRadio"||"orderRadio".equals(typeFlag)) {//点击订单后保存
						//新增下次订单任务
						TPcOrderPO orderPo = new TPcOrderPO();
						orderPo.setOrderId(Long.parseLong(newTaskId));
						orderPo.setCustomerId(Long.parseLong(customerId));
						orderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
						orderPo.setOldLevel(intentType);
						orderPo.setCreateDate(new Date());
						orderPo.setCreateBy(logonUser.getUserId().toString());
						orderPo.setOldSalesProgress(salesProgress);
						orderPo.setPreviousTask(Long.parseLong(taskId));
						orderPo.setTaskStatus(Constant.TASK_STATUS_01);
						orderPo.setOrderDate(sdf.parse(orderDate));
						dao.insert(orderPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_08.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), orderDate,"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="defeatRadio"||"defeatRadio".equals(typeFlag)) {//点击战败后保存
						//获取客户当前意向等级和销售流程
						TPcCustomerPO customerPo = new TPcCustomerPO();
						customerPo.setCustomerId(Long.parseLong(customerId));
						List<PO> po = dao.select(customerPo);
						customerPo = (TPcCustomerPO)po.get(0);
						String ctmRank = customerPo.getCtmRank();
						String sProgress = customerPo.getSalesProgress();
						//新增战败数据
						TPcDefeatfailurePO defeatPo = new TPcDefeatfailurePO();
						defeatPo.setDefeatfailureId(Long.parseLong(newTaskId));
						defeatPo.setCustomerId(Long.parseLong(customerId));
						defeatPo.setDefeatfailureType(Constant.FAILURE_TYPE_01);
						defeatPo.setDefeatModel(defeatVehicle);
						defeatPo.setDefeatEndDate(sdf.parse(defeatEndDate));
						defeatPo.setStatus(Constant.STATUS_ENABLE);
						defeatPo.setOldLevel(ctmRank);
						defeatPo.setNewLevel(Constant.INTENT_TYPE_E.toString());
						defeatPo.setOldSalesProgress(sProgress);
						defeatPo.setSalesProgress(Constant.SALSE_PROGRESS_01.toString());
						defeatPo.setReasonAnalysis(defeatRemark);
						defeatPo.setCreateBy(logonUser.getUserId().toString());
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(taskId));
						defeatPo.setDefeatType(defeatReason);
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为战败
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_E.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_10.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="failureRadio"||"failureRadio".equals(typeFlag)) {//点击失效后保存
						//获取客户当前意向等级和销售流程
						TPcCustomerPO customerPo = new TPcCustomerPO();
						customerPo.setCustomerId(Long.parseLong(customerId));
						List<PO> po = dao.select(customerPo);
						customerPo = (TPcCustomerPO)po.get(0);
						String ctmRank = customerPo.getCtmRank();
						String sProgress = customerPo.getSalesProgress();
						//新增失效数据
						TPcDefeatfailurePO defeatPo = new TPcDefeatfailurePO();
						defeatPo.setDefeatfailureId(Long.parseLong(newTaskId));
						defeatPo.setCustomerId(Long.parseLong(customerId));
						defeatPo.setDefeatfailureType(Constant.FAILURE_TYPE_02);
						defeatPo.setStatus(Constant.STATUS_ENABLE);
						defeatPo.setOldLevel(ctmRank);
						defeatPo.setNewLevel(Constant.INTENT_TYPE_L.toString());
						defeatPo.setOldSalesProgress(sProgress);
						defeatPo.setSalesProgress(Constant.SALSE_PROGRESS_01.toString());
						defeatPo.setReasonAnalysis(failureRemark);
						defeatPo.setCreateBy(logonUser.getUserId().toString());
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(taskId));
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						defeatPo.setFailureDate(sdf.parse(failureDate));
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为失效
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_L.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_11.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					}
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(inviteShopId,Constant.REMIND_TYPE_07.toString());
				}else if (taskType.equals("orderYN")) {
					
					String intentType2 = act.getRequest().getParamValue("intentType2");//跟进获取的意向等级
					String intentType3 = act.getRequest().getParamValue("intentType3");//邀约获取的意向等级
					//获取客户当前意向等级和销售流程
					
					//判断订单状态（如若已提交订单则抛出异常）
					TPcOrderPO oPo = new TPcOrderPO();
					oPo.setOrderId(Long.parseLong(taskId));
					List<PO> poxx = dao.select(oPo);
					oPo = (TPcOrderPO)poxx.get(0);
					Integer oStatus = oPo.getTaskStatus();
					if(oStatus == 60171002) {
						throw new Exception("该条数据已被处理过！");
					}
					String newTaskId = SequenceManager.getSequence("");
					//标记本次订单任务为已完成
					TPcOrderPO oldOrderPo = new TPcOrderPO();
					oldOrderPo.setOrderId(Long.parseLong(taskId));
					TPcOrderPO newOrderPo = new TPcOrderPO();
					newOrderPo.setOrderId(Long.parseLong(taskId));
					newOrderPo.setTaskStatus(Constant.TASK_STATUS_02);
					newOrderPo.setNextTask(Long.parseLong(newTaskId));//设置订单没有订车成功的下次任务
					if(typeFlag=="followRadio"||"followRadio".equals(typeFlag)) {//下次任务是跟进
						newOrderPo.setNewLevel(intentType2);
					}else if(typeFlag=="inviteRadio"||"inviteRadio".equals(typeFlag)){//下次任务是邀约
						newOrderPo.setNewLevel(intentType3);
					}else if(typeFlag=="defeatRadio"||"defeatRadio".equals(typeFlag)) {//下次任务是战败
					    newOrderPo.setNewLevel(Constant.INTENT_TYPE_E.toString());
					}else if(typeFlag=="failureRadio"||"failureRadio".equals(typeFlag)) {//下次任务是失效
						newOrderPo.setNewLevel(Constant.INTENT_TYPE_L.toString());
					}
					newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_06);
					dao.update(oldOrderPo, newOrderPo);
					//从获取原意向等级和原销售流程用于新任务
					TPcOrderPO preOrderPo = new TPcOrderPO();
					preOrderPo.setOrderId(Long.parseLong(taskId));
					List<PO> xpo = dao.select(preOrderPo);
					preOrderPo = (TPcOrderPO)xpo.get(0);
					String preIntentType = preOrderPo.getOldLevel();
					String preSalesProgress = preOrderPo.getOldSalesProgress();
					if(typeFlag=="followRadio"||"followRadio".equals(typeFlag)) {//点击跟进后保存
						//新增下次跟进任务表
						TPcFollowPO followPo = new TPcFollowPO();
						followPo.setFollowId(Long.parseLong(newTaskId));
						followPo.setCustomerId(Long.parseLong(customerId));
						followPo.setFollowDate(sdf.parse(nextFollowDate));
						followPo.setOldLevel(intentType2);
						followPo.setNewLevel(null);
						followPo.setTaskStatus(Constant.TASK_STATUS_01);
						followPo.setFollowType(followType);
						followPo.setFollowPlan(followPlan);
						followPo.setCreateDate(new Date());
						followPo.setCreateBy(logonUser.getUserId().toString());
						followPo.setPreviousTask(Long.parseLong(taskId));
						followPo.setOldSalesProgress(preSalesProgress);
						dao.insert(followPo);
						TPcCustomerPO customerPo = new TPcCustomerPO();
						//修改客户表内客户等级
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(intentType2);
						dao.update(oldCustomerPo2, newCustomerPo2);
						//增加一条接触点信息
//						TPcContactPointPO pointPo = new TPcContactPointPO();
//						String pointId = SequenceManager.getSequence("");
//						pointPo.setPointId(Long.parseLong(pointId));
//						pointPo.setPointDate(new Date());
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), nextFollowDate,"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="inviteRadio"||"inviteRadio".equals(typeFlag)) {//点击邀约后保存
						//新增下次计划邀约任务
						TPcInvitePO invitePo = new TPcInvitePO();
						invitePo.setInviteId(Long.parseLong(newTaskId));
						invitePo.setCustomerId(Long.parseLong(customerId));
						invitePo.setCreateDate(new Date());
						invitePo.setCreateBy(logonUser.getUserId().toString());
						invitePo.setPlanInviteDate(sdf.parse(planInviteDate));
						invitePo.setPlanMeetDate(sdf.parse(planMeetDate));
						invitePo.setTaskStatus(Constant.TASK_STATUS_01);
						invitePo.setInviteType(Integer.parseInt(inviteTypeNew));
						invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_04);
						invitePo.setIfPlan(Constant.IF_TYPE_NO);
						invitePo.setOldLevel(intentType3);
						invitePo.setNewLevel(null);
						invitePo.setPreviousTask(Long.parseLong(taskId));
						invitePo.setOldSalesProgress(preSalesProgress);
						
						//修改客户表内客户等级
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(intentType3);
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//判断是否做了计划（4个框有值说明为做了计划）
						if(xqfx!=null&&!"".equals(xqfx)) {
							invitePo.setIfPlan(Constant.IF_TYPE_YES);
							invitePo.setRequirement(xqfx);
							invitePo.setInviteTarget(yymb);
							invitePo.setTrustDesign(ydkhxrsj);
							invitePo.setSceneDesign(gdkhqjsj);
							invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_01);
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_15.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						} else {
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), planInviteDate,"");
						}
						dao.insert(invitePo);
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="orderRadio"||"orderRadio".equals(typeFlag)) {//点击订单后保存
						//新增下次订单任务
						TPcOrderPO orderPo = new TPcOrderPO();
						orderPo.setOrderId(Long.parseLong(newTaskId));
						orderPo.setCustomerId(Long.parseLong(customerId));
						orderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
						orderPo.setOldLevel(preIntentType);
						orderPo.setCreateDate(new Date());
						orderPo.setCreateBy(logonUser.getUserId().toString());
						orderPo.setOldSalesProgress(preSalesProgress);
						orderPo.setPreviousTask(Long.parseLong(taskId));
						orderPo.setTaskStatus(Constant.TASK_STATUS_01);
						orderPo.setOrderDate(sdf.parse(orderDate));
						dao.insert(orderPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_08.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), orderDate,"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="defeatRadio"||"defeatRadio".equals(typeFlag)) {//点击战败后保存
						//获取客户当前意向等级和销售流程
						TPcCustomerPO customerPo = new TPcCustomerPO();
						customerPo.setCustomerId(Long.parseLong(customerId));
						List<PO> po = dao.select(customerPo);
						customerPo = (TPcCustomerPO)po.get(0);
						String ctmRank = customerPo.getCtmRank();
						String sProgress = customerPo.getSalesProgress();
						//新增战败数据
						TPcDefeatfailurePO defeatPo = new TPcDefeatfailurePO();
						defeatPo.setDefeatfailureId(Long.parseLong(newTaskId));
						defeatPo.setCustomerId(Long.parseLong(customerId));
						defeatPo.setDefeatfailureType(Constant.FAILURE_TYPE_01);
						defeatPo.setDefeatModel(defeatVehicle);
						defeatPo.setDefeatEndDate(sdf.parse(defeatEndDate));
						defeatPo.setStatus(Constant.STATUS_ENABLE);
						defeatPo.setOldLevel(Constant.INTENT_TYPE_E.toString());
						defeatPo.setNewLevel(Constant.INTENT_TYPE_E.toString());
						defeatPo.setOldSalesProgress(sProgress);
						defeatPo.setSalesProgress(Constant.SALSE_PROGRESS_01.toString());
						defeatPo.setReasonAnalysis(defeatRemark);
						defeatPo.setCreateBy(logonUser.getUserId().toString());
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(taskId));
						defeatPo.setDefeatType(defeatReason);
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为战败
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_E.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_10.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
			
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="failureRadio"||"failureRadio".equals(typeFlag)) {//点击失效后保存
						//获取客户当前意向等级和销售流程
						TPcCustomerPO customerPo = new TPcCustomerPO();
						customerPo.setCustomerId(Long.parseLong(customerId));
						List<PO> po = dao.select(customerPo);
						customerPo = (TPcCustomerPO)po.get(0);
						String ctmRank = customerPo.getCtmRank();
						String sProgress = customerPo.getSalesProgress();
						//新增失效数据
						TPcDefeatfailurePO defeatPo = new TPcDefeatfailurePO();
						defeatPo.setDefeatfailureId(Long.parseLong(newTaskId));
						defeatPo.setCustomerId(Long.parseLong(customerId));
						defeatPo.setDefeatfailureType(Constant.FAILURE_TYPE_02);
						defeatPo.setStatus(Constant.STATUS_ENABLE);
						defeatPo.setOldLevel(Constant.INTENT_TYPE_L.toString());
						defeatPo.setNewLevel(Constant.INTENT_TYPE_L.toString());
						defeatPo.setOldSalesProgress(sProgress);
						defeatPo.setSalesProgress(Constant.SALSE_PROGRESS_01.toString());
						defeatPo.setReasonAnalysis(failureRemark);
						defeatPo.setCreateBy(logonUser.getUserId().toString());
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(taskId));
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						defeatPo.setFailureDate(sdf.parse(failureDate));
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为失效
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_L.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_11.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					}
					
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(taskId,Constant.REMIND_TYPE_08.toString());
					
				}else if(taskType.equals("order")) {//订单页面
					//判断订单状态（如若已提交订单则抛出异常）
					TPcOrderPO oPo = new TPcOrderPO();
					oPo.setOrderId(Long.parseLong(taskId));
					List<PO> poxx = dao.select(oPo);
					oPo = (TPcOrderPO)poxx.get(0);
					Integer oStatus = oPo.getTaskStatus();
					if(oStatus != 60171001) {//有坑
						throw new Exception("该条数据已被处理过！");
					}
					
					//获取订单页面参数
					String table1Length = act.getRequest().getParamValue("table1Length");//未确定车架号tableRow数
					String table2Length = act.getRequest().getParamValue("table2Length");//已确定车架号tableRow数
					String AuditFlag = act.getRequest().getParamValue("AuditFlag");//判断订单是否需要提交经理审核
					String oldName=act.getRequest().getParamValue("old_customer_name");//老客户姓名
					String oldphone=act.getRequest().getParamValue("old_telephone");//老客户联系电话
					String oldVehicleId=act.getRequest().getParamValue("old_vehicle_id");//老客户车架号
					String JsSucessNO=act.getRequest().getParamValue("JsSucessNO");//是否有老客户推荐
				    String laoSucessNO=act.getRequest().getParamValue("laoSucessNO");//老客户推荐还是朋友推荐
				    System.out.println("JsSucessNO:"+JsSucessNO);
				
				   
				    
					if(!AuditFlag.equals("sucess")){
					//保存订单审核表
					String newAuditId = SequenceManager.getSequence("");
					TPcOrderAuditPO orderAuditPo = new TPcOrderAuditPO();
					orderAuditPo.setOrderAuditId(Long.parseLong(newAuditId));
					orderAuditPo.setOrderId(Long.parseLong(taskId));
					orderAuditPo.setCustomerId(Long.parseLong(customerId));
					orderAuditPo.setManagerAudit(Constant.DIRECTOR_AUDIT_01);
					orderAuditPo.setCreateBy(logonUser.getUserId().toString());
					orderAuditPo.setCreateDate(new Date());
					orderAuditPo.setStatus(Constant.STATUS_ENABLE);
			        //修改订单
					orderAuditPo.setUpdateType(Constant.UPDATE_TYPE_01);
					dao.insert(orderAuditPo);
					
					
					
					//标记本次订单主表任务完成
					TPcOrderPO oldOrderPo = new TPcOrderPO();
					oldOrderPo.setOrderId(Long.parseLong(taskId));
					TPcOrderPO newOrderPo = new TPcOrderPO();
					newOrderPo.setOrderId(Long.parseLong(taskId));
					newOrderPo.setNewLevel(intentType);
					newOrderPo.setSalesProgress(salesProgress);
					newOrderPo.setTaskStatus(Constant.TASK_STATUS_02);
					newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_08);
					newOrderPo.setFinishDate(new Date());
					newOrderPo.setOwnerName(ownerName);
					newOrderPo.setOwnerPhone(ownerPhone);
					newOrderPo.setOwnerPaperType(ownerPaperType);
					newOrderPo.setOwnerPaperNo(ownerPaperNo);
					newOrderPo.setOwnerProvince(dPro);
					newOrderPo.setOwnerCity(dCity);
					newOrderPo.setOwnerArea(dArea);
					newOrderPo.setOwnerAddress(ownerAddress);
					newOrderPo.setNewProductSale(Integer.parseInt(newProductSale));
					newOrderPo.setDealType(dealType);
					newOrderPo.setTestDriving(Integer.parseInt(testDriving));
					dao.update(oldOrderPo, newOrderPo);
					
					//修改客户档案信息
					TPcCustomerPO oldCustomerPo = new TPcCustomerPO();
					oldCustomerPo.setCustomerId(Long.parseLong(customerId));
					TPcCustomerPO newCustomerPo = new TPcCustomerPO();
					newCustomerPo.setCustomerId(Long.parseLong(customerId));
					newCustomerPo.setCtmRank(intentType);
					newCustomerPo.setBuyWay(dealType);
					newCustomerPo.setSalesProgress(salesProgress);
					newCustomerPo.setIfDrive(Long.parseLong(testDriving));
					dao.update(oldCustomerPo,newCustomerPo);
					
					Date dateTime = new Date();//当前时间
					//新增下次交车任务
					//取table1里的数据
					for(int i1=1;i1<=Integer.parseInt(table1Length);i1++) {
						String newTaskId = SequenceManager.getSequence("");
						String model = act.getRequest().getParamValue("materialNamerow"+i1);//车型
						String color = act.getRequest().getParamValue("intent_colorrow"+i1);//意向颜色
						String price = act.getRequest().getParamValue("pricerow"+i1);//价格
						String num = act.getRequest().getParamValue("numrow"+i1);//数量
						String amount = act.getRequest().getParamValue("amountrow"+i1);//总金额
						String deposit = act.getRequest().getParamValue("depositrow"+i1);//订金
						String earnest = act.getRequest().getParamValue("earnestrow"+i1);//定金
						String pre_pay_date = act.getRequest().getParamValue("pre_pay_daterow"+i1);//余款付款日期
						String pre_delivery_date = act.getRequest().getParamValue("pre_delivery_daterow"+i1);//交车日期
						if(model!=null&&!"".equals(model)) {
							//保存到订单详细子表
							TPcOrderDetailPO orderDetailPo = new TPcOrderDetailPO();
							orderDetailPo.setOrderDetailId(Long.parseLong(newTaskId));
							orderDetailPo.setOrderId(Long.parseLong(taskId));
							orderDetailPo.setCustomerId(Long.parseLong(customerId));
							orderDetailPo.setIntentModel(model);
							orderDetailPo.setIntentColor(color);
							orderDetailPo.setNum(Integer.parseInt(num));
							orderDetailPo.setCreateDate(dateTime);
							orderDetailPo.setOrderdDate(dateTime);
							orderDetailPo.setCreateBy(logonUser.getUserId().toString());
							orderDetailPo.setDeliveryDate(sdf.parse(pre_delivery_date));
							orderDetailPo.setAmount(Float.parseFloat(amount));
							if(deposit!=null && !"".equals(deposit)) {
								orderDetailPo.setDeposit(Float.parseFloat(deposit));
							} else {
								orderDetailPo.setEarnest(Float.parseFloat(earnest));
							}
							orderDetailPo.setBalanceDate(sdf.parse(pre_pay_date));
							boolean intentFlag=CommonUtils.judgeIfForeign(model);
							if(intentFlag){
								orderDetailPo.setDeliveryNumber(Integer.parseInt(num));
							}else{
								orderDetailPo.setDeliveryNumber(0);
							}
							
							orderDetailPo.setPrice(Float.parseFloat(price));
							orderDetailPo.setTaskStatus(Constant.TASK_STATUS_01);
							orderDetailPo.setRemark("10011002");//设置订单子表备注为无效的订单信息
							orderDetailPo.setPreviousTask(Long.parseLong(taskId));
							dao.insert(orderDetailPo);
							
						}
					}
					
					//取table2里的数据
					for(int i1=1;i1<=Integer.parseInt(table2Length);i1++) {
						String newTaskId2 = SequenceManager.getSequence("");
						String vinId = act.getRequest().getParamValue("hiddenyvinIdyrow"+i1);//VinId
						String model2 = act.getRequest().getParamValue("hiddenymaterialCodeyrow"+i1);//车型
						String color2 = act.getRequest().getParamValue("ytheColoryvinIdyrow"+i1);//意向颜色
						String price2 = act.getRequest().getParamValue("ypriceyrow"+i1);//价格
						String num2 = act.getRequest().getParamValue("ynumyrow"+i1);//数量
						String amount2 = act.getRequest().getParamValue("yamountyrow"+i1);//总金额
						String deposit2 = act.getRequest().getParamValue("ydeposityrow"+i1);//订金
						String earnest2 = act.getRequest().getParamValue("yearnestyrow"+i1);//定金
						String pre_pay_date2 = act.getRequest().getParamValue("ypre_pay_dateyrow"+i1);//余款付款日期
						String pre_delivery_date2 = act.getRequest().getParamValue("ypre_delivery_dateyrow"+i1);//交车日期
						if(vinId!=null&&!"".equals(vinId)) {
							//保存到订单详细子表
							TPcOrderDetailPO orderDetailPo2 = new TPcOrderDetailPO();
							orderDetailPo2.setOrderDetailId(Long.parseLong(newTaskId2));
							orderDetailPo2.setOrderId(Long.parseLong(taskId));
							orderDetailPo2.setCustomerId(Long.parseLong(customerId));
							orderDetailPo2.setVehicleId(Long.parseLong(vinId));
							orderDetailPo2.setMaterial(Long.parseLong(model2));
							orderDetailPo2.setColor(color2);
							orderDetailPo2.setNum(Integer.parseInt(num2));
							orderDetailPo2.setCreateDate(new Date());
							orderDetailPo2.setCreateBy(logonUser.getUserId().toString());
							orderDetailPo2.setDeliveryDate(sdf.parse(pre_delivery_date2));
							orderDetailPo2.setAmount(Float.parseFloat(amount2));
							if(deposit2!=null && !"".equals(deposit2)) {
								orderDetailPo2.setDeposit(Float.parseFloat(deposit2));
							} else {
								orderDetailPo2.setEarnest(Float.parseFloat(earnest2));
							}
							orderDetailPo2.setBalanceDate(sdf.parse(pre_pay_date2));
							orderDetailPo2.setDeliveryNumber(0);
							orderDetailPo2.setPrice(Float.parseFloat(price2));
							orderDetailPo2.setTaskStatus(Constant.TASK_STATUS_01);
							orderDetailPo2.setRemark("10011002");//设置订单子表备注为无效的订单信息
							orderDetailPo2.setPreviousTask(Long.parseLong(taskId));
							dao.insert(orderDetailPo2);
							
							//锁定车辆(修改车辆状态)
							TmVehiclePO oldVehiclePo = new TmVehiclePO();
							oldVehiclePo.setVehicleId(Long.parseLong(vinId));
							TmVehiclePO newVehiclePo = new TmVehiclePO();
							newVehiclePo.setVehicleId(Long.parseLong(vinId));
							newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_10);
							dao.update(oldVehiclePo, newVehiclePo);
							
							}
					}
					
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(taskId,Constant.REMIND_TYPE_08.toString());
					
					//新增订单数量审核提醒信息
					CommonUtils.addRemindInfo(Constant.REMIND_TYPE_21.toString(), taskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
					
					}else{
					//标记本次订单主表任务完成
					TPcOrderPO oldOrderPo = new TPcOrderPO();
					oldOrderPo.setOrderId(Long.parseLong(taskId));
					TPcOrderPO newOrderPo = new TPcOrderPO();
					newOrderPo.setOrderId(Long.parseLong(taskId));
					newOrderPo.setNewLevel(intentType);
					newOrderPo.setSalesProgress(salesProgress);
					newOrderPo.setTaskStatus(Constant.TASK_STATUS_02);
					newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);//设置订单信息为有效
					newOrderPo.setFinishDate(new Date());
					newOrderPo.setOwnerName(ownerName);
					newOrderPo.setOwnerPhone(ownerPhone);
					newOrderPo.setOwnerPaperType(ownerPaperType);
					newOrderPo.setOwnerPaperNo(ownerPaperNo);
					newOrderPo.setOwnerProvince(dPro);
					newOrderPo.setOwnerCity(dCity);
					newOrderPo.setOwnerArea(dArea);
					newOrderPo.setOwnerAddress(ownerAddress);
					newOrderPo.setNewProductSale(Integer.parseInt(newProductSale));
					newOrderPo.setDealType(dealType);
					newOrderPo.setTestDriving(Integer.parseInt(testDriving));
					dao.update(oldOrderPo, newOrderPo);
					
					//修改客户档案信息
					TPcCustomerPO oldCustomerPo = new TPcCustomerPO();
					oldCustomerPo.setCustomerId(Long.parseLong(customerId));
					TPcCustomerPO newCustomerPo = new TPcCustomerPO();
					newCustomerPo.setCustomerId(Long.parseLong(customerId));
					newCustomerPo.setCtmRank(intentType);
					newCustomerPo.setBuyWay(dealType);
					newCustomerPo.setSalesProgress(salesProgress);
					newCustomerPo.setIfDrive(Long.parseLong(testDriving));
					dao.update(oldCustomerPo,newCustomerPo);
					
					//是有老客户和情报推荐信息
					 if(JsSucessNO.equals("10041001")){
					    	//当介绍类型为老客户转介绍的时候添加老客户与车主关系信息
							if("60581001".equals(laoSucessNO))
							{	
								dao.deleteLinkMan(customerId,"60581001"); 
								TPcLinkManPO linkMan=new TPcLinkManPO();
								String linkId = SequenceManager.getSequence("");
								linkMan.setLinkId(Long.parseLong(linkId));
								linkMan.setLinkMan(oldName.trim());
								linkMan.setLinkPhone(oldphone.trim());
								linkMan.setCreateDate(new Date());
								linkMan.setRelationship("老客户推荐");
								linkMan.setCtmId(Long.parseLong(customerId));
								linkMan.setStatus(new Long(Constant.STATUS_ENABLE));
								linkMan.setOldVehicleId(oldVehicleId.trim());
								linkMan.setRelationCode(Constant.linkMan_status_01.toString());
								dao.insert(linkMan);
							}
							//当介绍类型为情报转介绍的时候添加老客户与车主关系信息
							if("60581002".equals(laoSucessNO))
							{	dao.deleteLinkMan(customerId,"60581002"); 		
								TPcLinkManPO linkMan=new TPcLinkManPO();
								String linkId = SequenceManager.getSequence("");
								linkMan.setLinkId(Long.parseLong(linkId));
								linkMan.setLinkMan(oldName.trim());
								linkMan.setLinkPhone(oldphone.trim());
								linkMan.setCreateDate(new Date());
								linkMan.setRelationship("情报转介绍");
								linkMan.setCtmId(Long.parseLong(customerId));
								linkMan.setStatus(new Long(Constant.STATUS_ENABLE));
								linkMan.setRelationCode(Constant.linkMan_status_02.toString());
								dao.insert(linkMan);
							}
					    }else if(JsSucessNO.equals("10041002")){
					    	dao.deleteLinkMan(customerId,"60581001");//如果是否 朋友老客户介绍选择否就删除已经建立的关联关系
					    	dao.deleteLinkMan(customerId,"60581002");//如果是否 朋友老客户介绍选择否就删除已经建立的关联关系 	
					    }
					
					//增加接触点信息
					CommonUtils.addContackPoint(Constant.POINT_WAY_07, "新增订单", customerId, logonUser.getUserId().toString(), logonUser.getDealerId());
					Date dateTime = new Date();//当前时间
					//新增下次交车任务
					//取table1里的数据
					for(int i1=1;i1<=Integer.parseInt(table1Length);i1++) {
						String newTaskId = SequenceManager.getSequence("");
						String model = act.getRequest().getParamValue("materialNamerow"+i1);//车型
						String color = act.getRequest().getParamValue("intent_colorrow"+i1);//意向颜色
						String price = act.getRequest().getParamValue("pricerow"+i1);//价格
						String num = act.getRequest().getParamValue("numrow"+i1);//数量
						String amount = act.getRequest().getParamValue("amountrow"+i1);//总金额
						String deposit = act.getRequest().getParamValue("depositrow"+i1);//订金
						String earnest = act.getRequest().getParamValue("earnestrow"+i1);//定金
						String pre_pay_date = act.getRequest().getParamValue("pre_pay_daterow"+i1);//余款付款日期
						String pre_delivery_date = act.getRequest().getParamValue("pre_delivery_daterow"+i1);//交车日期
						if(model!=null&&!"".equals(model)) {
							//保存到订单详细子表
							TPcOrderDetailPO orderDetailPo = new TPcOrderDetailPO();
							orderDetailPo.setOrderDetailId(Long.parseLong(newTaskId));
							orderDetailPo.setOrderId(Long.parseLong(taskId));
							orderDetailPo.setCustomerId(Long.parseLong(customerId));
							orderDetailPo.setIntentModel(model);
							orderDetailPo.setIntentColor(color);
							orderDetailPo.setNum(Integer.parseInt(num));
							orderDetailPo.setCreateDate(dateTime);
							orderDetailPo.setOrderdDate(dateTime);
							orderDetailPo.setCreateBy(logonUser.getUserId().toString());
							orderDetailPo.setDeliveryDate(sdf.parse(pre_delivery_date));
							orderDetailPo.setAmount(Float.parseFloat(amount));
							if(deposit!=null && !"".equals(deposit)) {
								orderDetailPo.setDeposit(Float.parseFloat(deposit));
							} else {
								orderDetailPo.setEarnest(Float.parseFloat(earnest));
							}
							orderDetailPo.setBalanceDate(sdf.parse(pre_pay_date));
							boolean intentFlag=false;//CommonUtils.judgeIfForeign(model);//是否进口车判断，中泰默认全部为 否
							if(intentFlag){
								orderDetailPo.setDeliveryNumber(Integer.parseInt(num));
							}else{
								orderDetailPo.setDeliveryNumber(0);
							}
							
							orderDetailPo.setPrice(Float.parseFloat(price));
							orderDetailPo.setTaskStatus(Constant.TASK_STATUS_01);
							orderDetailPo.setPreviousTask(Long.parseLong(taskId));
							dao.insert(orderDetailPo);
							
							if(!intentFlag){
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_09.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), pre_delivery_date, num);
							}
						}
					}
					
					//取table2里的数据
					for(int i1=1;i1<=Integer.parseInt(table2Length);i1++) {
						String newTaskId2 = SequenceManager.getSequence("");
						String vinId = act.getRequest().getParamValue("hiddenyvinIdyrow"+i1);//VinId
						String model2 = act.getRequest().getParamValue("hiddenymaterialCodeyrow"+i1);//车型
						String color2 = act.getRequest().getParamValue("ytheColoryvinIdyrow"+i1);//意向颜色
						String price2 = act.getRequest().getParamValue("ypriceyrow"+i1);//价格
						String num2 = act.getRequest().getParamValue("ynumyrow"+i1);//数量
						String amount2 = act.getRequest().getParamValue("yamountyrow"+i1);//总金额
						String deposit2 = act.getRequest().getParamValue("ydeposityrow"+i1);//订金
						String earnest2 = act.getRequest().getParamValue("yearnestyrow"+i1);//定金
						String pre_pay_date2 = act.getRequest().getParamValue("ypre_pay_dateyrow"+i1);//余款付款日期
						String pre_delivery_date2 = act.getRequest().getParamValue("ypre_delivery_dateyrow"+i1);//交车日期
						if(vinId!=null&&!"".equals(vinId)) {
							//保存到订单详细子表
							TPcOrderDetailPO orderDetailPo2 = new TPcOrderDetailPO();
							orderDetailPo2.setOrderDetailId(Long.parseLong(newTaskId2));
							orderDetailPo2.setOrderId(Long.parseLong(taskId));
							orderDetailPo2.setCustomerId(Long.parseLong(customerId));
							orderDetailPo2.setVehicleId(Long.parseLong(vinId));
							orderDetailPo2.setMaterial(Long.parseLong(model2));
							orderDetailPo2.setColor(color2);
							orderDetailPo2.setNum(Integer.parseInt(num2));
							orderDetailPo2.setCreateDate(new Date());
							orderDetailPo2.setCreateBy(logonUser.getUserId().toString());
							orderDetailPo2.setDeliveryDate(sdf.parse(pre_delivery_date2));
							orderDetailPo2.setAmount(Float.parseFloat(amount2));
							if(deposit2!=null && !"".equals(deposit2)) {
								orderDetailPo2.setDeposit(Float.parseFloat(deposit2));
							} else {
								orderDetailPo2.setEarnest(Float.parseFloat(earnest2));
							}
							orderDetailPo2.setBalanceDate(sdf.parse(pre_pay_date2));
							orderDetailPo2.setDeliveryNumber(0);
							orderDetailPo2.setPrice(Float.parseFloat(price2));
							orderDetailPo2.setTaskStatus(Constant.TASK_STATUS_01);
							orderDetailPo2.setPreviousTask(Long.parseLong(taskId));
							dao.insert(orderDetailPo2);
							
							//锁定车辆(修改车辆状态)
							TmVehiclePO oldVehiclePo = new TmVehiclePO();
							oldVehiclePo.setVehicleId(Long.parseLong(vinId));
							TmVehiclePO newVehiclePo = new TmVehiclePO();
							newVehiclePo.setVehicleId(Long.parseLong(vinId));
							newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_10);
							dao.update(oldVehiclePo, newVehiclePo);
							
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_09.toString(), newTaskId2, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), pre_delivery_date2, num2);
						}
					}
					
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(taskId,Constant.REMIND_TYPE_08.toString());
					}
					doInit();
					act.setForword(taskManageinitUrl);
				} else if(taskType.equals("orderBack")){//订单退单页面
					String intentType2 = act.getRequest().getParamValue("intentType2");//跟进获取的意向等级
					String intentType3 = act.getRequest().getParamValue("intentType3");//邀约获取的意向等级
					//判断订单状态（如若已提交订单则抛出异常）
					TPcOrderPO oPo = new TPcOrderPO();
					oPo.setOrderId(Long.parseLong(taskId));
					List<PO> poxx = dao.select(oPo);
					oPo = (TPcOrderPO)poxx.get(0);
					Integer oStatus = oPo.getTaskStatus();
					if(oStatus == 60171002) {
						throw new Exception("该条数据已被处理过！");
					}
					String newTaskId = SequenceManager.getSequence("");
					//标记本次订单任务为已完成
					TPcOrderPO oldOrderPo = new TPcOrderPO();
					oldOrderPo.setOrderId(Long.parseLong(taskId));
					TPcOrderPO newOrderPo = new TPcOrderPO();
					newOrderPo.setOrderId(Long.parseLong(taskId));
					newOrderPo.setNextTask(Long.parseLong(newTaskId));//设置订单任务的下次任务
					if(typeFlag=="followRadio"||"followRadio".equals(typeFlag)) {//下次任务是跟进
						newOrderPo.setNewLevel(intentType2);
					}else if(typeFlag=="inviteRadio"||"inviteRadio".equals(typeFlag)){//下次任务是邀约
						newOrderPo.setNewLevel(intentType3);
					}else if(typeFlag=="defeatRadio"||"defeatRadio".equals(typeFlag)) {//下次任务是战败
					    newOrderPo.setNewLevel(Constant.INTENT_TYPE_E.toString());
					}else if(typeFlag=="failureRadio"||"failureRadio".equals(typeFlag)) {//下次任务是失效
						newOrderPo.setNewLevel(Constant.INTENT_TYPE_L.toString());
					}
					//newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_06);
					newOrderPo.setTaskStatus(Constant.TASK_STATUS_02);
					dao.update(oldOrderPo, newOrderPo);
					//从获取原意向等级和原销售流程用于新任务
					TPcOrderPO preOrderPo = new TPcOrderPO();
					preOrderPo.setOrderId(Long.parseLong(taskId));
					List<PO> xpo = dao.select(preOrderPo);
					preOrderPo = (TPcOrderPO)xpo.get(0);
					String preIntentType = preOrderPo.getOldLevel();
					String preSalesProgress = preOrderPo.getOldSalesProgress();
					
					if(typeFlag=="followRadio"||"followRadio".equals(typeFlag)) {//点击跟进后保存
						//新增下次跟进任务表
						TPcFollowPO followPo = new TPcFollowPO();
						followPo.setFollowId(Long.parseLong(newTaskId));
						followPo.setCustomerId(Long.parseLong(customerId));
						followPo.setFollowDate(sdf.parse(nextFollowDate));
						followPo.setOldLevel(intentType2);
						followPo.setNewLevel(null);
						followPo.setTaskStatus(Constant.TASK_STATUS_01);
						followPo.setFollowType(followType);
						followPo.setFollowPlan(followPlan);
						followPo.setCreateDate(new Date());
						followPo.setCreateBy(logonUser.getUserId().toString());
						followPo.setPreviousTask(Long.parseLong(taskId));
						followPo.setOldSalesProgress(preSalesProgress);
						dao.insert(followPo);
						
						//修改客户表内客户等级
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(intentType2);
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//增加一条接触点信息
//						TPcContactPointPO pointPo = new TPcContactPointPO();
//						String pointId = SequenceManager.getSequence("");
//						pointPo.setPointId(Long.parseLong(pointId));
//						pointPo.setPointDate(new Date());
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), nextFollowDate,"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="inviteRadio"||"inviteRadio".equals(typeFlag)) {//点击邀约后保存
						//新增下次计划邀约任务
						TPcInvitePO invitePo = new TPcInvitePO();
						invitePo.setInviteId(Long.parseLong(newTaskId));
						invitePo.setCustomerId(Long.parseLong(customerId));
						invitePo.setCreateDate(new Date());
						invitePo.setCreateBy(logonUser.getUserId().toString());
						invitePo.setPlanInviteDate(sdf.parse(planInviteDate));
						invitePo.setPlanMeetDate(sdf.parse(planMeetDate));
						invitePo.setTaskStatus(Constant.TASK_STATUS_01);
						invitePo.setInviteType(Integer.parseInt(inviteTypeNew));
						invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_04);
						invitePo.setIfPlan(Constant.IF_TYPE_NO);
						invitePo.setOldLevel(intentType3);
						invitePo.setNewLevel(null);
						invitePo.setPreviousTask(Long.parseLong(taskId));
						invitePo.setOldSalesProgress(preSalesProgress);
						
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(intentType2);
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//判断是否做了计划（4个框有值说明为做了计划）
						if(xqfx!=null&&!"".equals(xqfx)) {
							invitePo.setIfPlan(Constant.IF_TYPE_YES);
							invitePo.setRequirement(xqfx);
							invitePo.setInviteTarget(yymb);
							invitePo.setTrustDesign(ydkhxrsj);
							invitePo.setSceneDesign(gdkhqjsj);
							invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_01);
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_15.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						} else {
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), planInviteDate,"");
						}
						dao.insert(invitePo);
			
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="orderRadio"||"orderRadio".equals(typeFlag)) {//点击订单后保存
						//新增下次订单任务
						TPcOrderPO orderPo = new TPcOrderPO();
						orderPo.setOrderId(Long.parseLong(newTaskId));
						orderPo.setCustomerId(Long.parseLong(customerId));
						orderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
						orderPo.setOldLevel(preIntentType);
						orderPo.setCreateDate(new Date());
						orderPo.setCreateBy(logonUser.getUserId().toString());
						orderPo.setOldSalesProgress(preSalesProgress);
						orderPo.setPreviousTask(Long.parseLong(taskId));
						orderPo.setTaskStatus(Constant.TASK_STATUS_01);
						orderPo.setOrderDate(sdf.parse(orderDate));
						dao.insert(orderPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_08.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), orderDate,"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="defeatRadio"||"defeatRadio".equals(typeFlag)) {//点击战败后保存
						//获取客户当前意向等级和销售流程
						TPcCustomerPO customerPo = new TPcCustomerPO();
						customerPo.setCustomerId(Long.parseLong(customerId));
						List<PO> po = dao.select(customerPo);
						customerPo = (TPcCustomerPO)po.get(0);
						String ctmRank = customerPo.getCtmRank();
						String sProgress = customerPo.getSalesProgress();
						//新增战败数据
						TPcDefeatfailurePO defeatPo = new TPcDefeatfailurePO();
						defeatPo.setDefeatfailureId(Long.parseLong(newTaskId));
						defeatPo.setCustomerId(Long.parseLong(customerId));
						defeatPo.setDefeatfailureType(Constant.FAILURE_TYPE_01);
						defeatPo.setDefeatModel(defeatVehicle);
						defeatPo.setDefeatEndDate(sdf.parse(defeatEndDate));
						defeatPo.setStatus(Constant.STATUS_ENABLE);
						defeatPo.setOldLevel(Constant.INTENT_TYPE_E.toString());
						defeatPo.setNewLevel(Constant.INTENT_TYPE_E.toString());
						defeatPo.setOldSalesProgress(sProgress);
						defeatPo.setSalesProgress(Constant.SALSE_PROGRESS_01.toString());
						defeatPo.setReasonAnalysis(defeatRemark);
						defeatPo.setCreateBy(logonUser.getUserId().toString());
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(taskId));
						defeatPo.setDefeatType(defeatReason);
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为战败
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_E.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_10.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					} else if(typeFlag=="failureRadio"||"failureRadio".equals(typeFlag)) {//点击失效后保存
						//获取客户当前意向等级和销售流程
						TPcCustomerPO customerPo = new TPcCustomerPO();
						customerPo.setCustomerId(Long.parseLong(customerId));
						List<PO> po = dao.select(customerPo);
						customerPo = (TPcCustomerPO)po.get(0);
						String ctmRank = customerPo.getCtmRank();
						String sProgress = customerPo.getSalesProgress();
						//新增失效数据
						TPcDefeatfailurePO defeatPo = new TPcDefeatfailurePO();
						defeatPo.setDefeatfailureId(Long.parseLong(newTaskId));
						defeatPo.setCustomerId(Long.parseLong(customerId));
						defeatPo.setDefeatfailureType(Constant.FAILURE_TYPE_02);
						defeatPo.setStatus(Constant.STATUS_ENABLE);
						defeatPo.setOldLevel(Constant.INTENT_TYPE_L.toString());
						defeatPo.setNewLevel(Constant.INTENT_TYPE_L.toString());
						defeatPo.setOldSalesProgress(sProgress);
						defeatPo.setSalesProgress(Constant.SALSE_PROGRESS_01.toString());
						defeatPo.setReasonAnalysis(failureRemark);
						defeatPo.setCreateBy(logonUser.getUserId().toString());
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(taskId));
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						defeatPo.setFailureDate(sdf.parse(failureDate));
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为失效
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_L.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_11.toString(), newTaskId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
						
						doInit();
						act.setForword(taskManageinitUrl);
					}
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(taskId,Constant.REMIND_TYPE_18.toString());
				}
			} else { //已处理的情况
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(taskId,Constant.REMIND_TYPE_06.toString());
				doInit();
				act.setForword(taskManageinitUrl);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * ajax客户建档信息查询(客户姓名、联系电话、经销商ID、顾问ID)
	 */
	public void customerInfoQuery() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String customerName = act.getRequest().getParamValue("customerName");
//			customerName = new String(customerName.getBytes("ISO-8859-1"),"GBK");
			String telephone = act.getRequest().getParamValue("telePhone");
			String leadsCode = act.getRequest().getParamValue("leadsCode");
//			telephone = new String(telephone.getBytes("ISO-8859-1"),"UTF-8");
			String dealerId = logonUser.getDealerId();
			String adviser = logonUser.getUserId().toString();
			//int comeCount=CommonUtils.getComeCount(telephone, dealerId);
			//int skCount=tdao.getShouKeCount(telephone, adviser,leadsCode);
			//判断有无首客
			//tman.getFirstGuest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			List<Map<String, Object>> ps = dao.customerInfoQuery(customerName,
					telephone,dealerId,adviser,
					curPage, Constant.PAGE_SIZE);
			//是否为保有客户、战败、失效客户
			List<Map<String, Object>> ps4 = dao.customerInfoQuery3(customerName,
					telephone,dealerId,adviser,
					curPage, Constant.PAGE_SIZE);
			//是否存在建档并属于其他顾问
			List<Map<String, Object>> ps3 = dao.customerInfoQuery2(customerName,
					telephone,dealerId,adviser,
					curPage, Constant.PAGE_SIZE);
			//如果存在客户信息,返回当前未完成的任务
			if(ps.size()>0||ps4.size()>0) {
				//得到CUSTOMERID
				String customerId = "";
				if(ps.size()>0) {
					customerId = ps.get(0).get("CUSTOMER_ID").toString();
				} else {
					customerId = ps4.get(0).get("CUSTOMER_ID").toString();
				}
				List<DynaBean> ps2 = dao.unDoTaskQuery(customerId,
						curPage, Constant.PAGE_SIZE);
				act.setOutData("ps2", ps2);
				
				act.setOutData("unDoTask", ps2.size());
			}
			act.setOutData("ps", ps);
			act.setOutData("ps3", ps3);
			act.setOutData("ps4", ps4);
			//act.setOutData("comeCount", comeCount);
			//act.setOutData("skCount", skCount);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "客户建档信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * ajax根据车型获取颜色
	 */
	public void colorInfoBySeries() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String inputId = act.getRequest().getParamValue("inputId");
			String modelCode = act.getRequest().getParamValue(inputId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					List<DynaBean> ps = dao.colorInfoBySeries(modelCode,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setOutData("psSize", ps.size());
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "根据车型获取颜色");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * ajax根据VIN获取车型颜色
	 */
	public void seriesColorInfoByVIN() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String inputId = act.getRequest().getParamValue("inputId");
			String vin = act.getRequest().getParamValue(inputId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					List<DynaBean> ps = dao.seriesColorInfoByVIN(vin,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setOutData("psSize", ps.size());
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "根据车型获取颜色");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * ajax获取提醒信息
	 */
	public void getRemindInfo() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerId = logonUser.getDealerId();
			String adviser = logonUser.getUserId().toString();
			String userId = "";
			
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(adviser)) {
				userId = logonUser.getUserId().toString();
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			List<Map<String, Object>> ps = dao.getRemindInfo(dealerId,userId,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "获取提醒信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * ajax获取总览信息 
	 * 
	 */
	public void overviewInfo() {
		
		OemLeadsManageDao dao = new OemLeadsManageDao();
		
		ActionContext act = ActionContext.getContext();
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try 
		{
			RequestWrapper request = act.getRequest();
			
			String dealerId = logonUser.getDealerId();
			
			String userId = logonUser.getUserId().toString();
			
			Map<String,String> map=new HashMap<String,String>();
			 
			map.put("dealerId", dealerId);
			
			map.put("userId", userId);
			
			List<Map<String,Object>> ps=dao.getOverview(map);
			
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "获取信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * ajax获取详细信息
	 */
	public void showOverviewInfo() { 
		
		OemLeadsManageDao dao = new OemLeadsManageDao();
		
		ActionContext act = ActionContext.getContext();
		
		RequestWrapper request = act.getRequest();
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
			
			String adviser=request.getParamValue("adviser");
			//判断是否是逾期的数据
			String flag=request.getParamValue("flag");
			Map<String,String> map=new HashMap<String,String>();
			map.put("adviser", adviser);
			map.put("flag", flag);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			
			PageResult<Map<String, Object>>  ps = dao.getOverviewInfo(map,curPage, Constant.PAGE_SIZE);
			
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "获取信息");
			
			logger.error(logonUser, e1);
			
			act.setException(e1);
		}
	
	}
	
	public void overviewInfoInit() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		
		ActionContext act = ActionContext.getContext();
		
		RequestWrapper request = act.getRequest();
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
			
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			
			String adviser=request.getParamValue("adviser");
			String flag=request.getParamValue("flag");
			act.setOutData("adviser", adviser);
			act.setOutData("flag", flag);
			
			act.setForword("/jsp/crm/taskmanage/showOverviewInfo.jsp");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * ajaxDCRC录入时验证是否建档并获取顾问
	 */
	public void getAdviserIfHas() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String telephone = act.getRequest().getParamValue("telePhone");
			String adviser = null;
			String adviserName = null;
			String dealerId = logonUser.getDealerId();
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			List<Map<String, Object>> ps = dao.getAdviserIfHas(
					telephone,dealerId,
					curPage, Constant.PAGE_SIZE);
			//如果存在客户信息,返回当前未完成的任务
			if(ps.size()>0) {
				//得到CUSTOMERID
				adviser = ps.get(0).get("ADVISER").toString();
				adviserName = ps.get(0).get("NAME").toString();
			}
			act.setOutData("ps", ps);
			act.setOutData("adviser", adviser);
			act.setOutData("adviserName", adviserName);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "客户建档信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void judgeVinStatus() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String vin = act.getRequest().getParamValue("vin");
			String flag=act.getRequest().getParamValue("flag");
			TmVehiclePO tv=new TmVehiclePO();
			tv.setVin(vin);
			tv=(TmVehiclePO) dao.select(tv).get(0);
			if(Constant.VEHICLE_LIFE_03.toString().equals(tv.getLifeCycle().toString())){
				act.setOutData("flag", 0);
			}else if("1".equals(flag)){
					act.setOutData("flag", 0);
			}else{
				act.setOutData("flag", 1);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "客户建档信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void judgeIfAbleOrder() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerId=logonUser.getDealerId();
			String tel=request.getParamValue("telephone");
			Map<String,String> map=new HashMap<String,String>();
			map.put("dealerId", dealerId);
			map.put("telephone", tel);
			int count =0;
			boolean flag=CommonUtils.judgeIfOrder(map);
			if(flag){
				count=1;
			}
			act.setOutData("flag", count);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "客户建档信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//判断当然是否可以做订单
	public void judgeIfAbleOrderDate() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerId=logonUser.getDealerId();
			String tel=request.getParamValue("telephone");
			Map<String,String> map=new HashMap<String,String>();
			map.put("dealerId", dealerId);
			map.put("telephone", tel);
			int count =0;
			boolean flag=CommonUtils.judgeIfOrderDate(map);
			if(flag){
				count=1;
			}
			act.setOutData("flag", count);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "客户建档信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//判断当然是否可以做交车
	public void judgeIfAbleDelvy() {
		OemLeadsManageDao dao = new OemLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerId=logonUser.getDealerId();
			String orderDetailId=request.getParamValue("orderDetailId");
			Map<String,String> map=new HashMap<String,String>();
			map.put("orderDetailId", orderDetailId);
			int count =0;
			boolean flag=CommonUtils.judgeIfAbleDelvy(map);
			if(flag){
				count=1;
			}
			act.setOutData("flag", count);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "客户建档信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}