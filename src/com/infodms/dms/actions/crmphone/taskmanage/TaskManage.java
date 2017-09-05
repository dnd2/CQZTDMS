package com.infodms.dms.actions.crmphone.taskmanage;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.crmphone.common.Common;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.dealerleadsmanage.DlrLeadsManageDao;
import com.infodms.dms.dao.crmphone.CrmPhoneDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcCustomerPO;
import com.infodms.dms.po.TPcDefeatfailurePO;
import com.infodms.dms.po.TPcFollowPO;
import com.infodms.dms.po.TPcInvitePO;
import com.infodms.dms.po.TPcInviteShopPO;
import com.infodms.dms.po.TPcLeadsAllotPO;
import com.infodms.dms.po.TPcLeadsPO;
import com.infodms.dms.po.TPcOrderPO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;

public class TaskManage extends BaseImport {
	public Logger logger = Logger.getLogger(TaskManage.class);
	
	/**
	 * 当日未处理的日程数
	 */
	public void getUnsolvedNum() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = null;
		String userId2 = act.getRequest().getParamValue("userId");
		String dealerId = act.getRequest().getParamValue("dealerId");
		String startDate = act.getRequest().getParamValue("startDate");
		String endDate = act.getRequest().getParamValue("endDate");
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(userId2)) {
			userId = userId2;
		} else if(CommonUtils.judgeDirectorLogin(userId2)){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(userId2);
		}
		
		Map strResult = new HashMap();
		Map result = new HashMap();
		try {
			//获取当日未处理线索数量
			List<DynaBean> leadsList = dao.getLeadsNum(userId,dealerId,startDate,endDate);
			DynaBean db = leadsList.get(0);
			result.put("\"leadsSize\"", db.get("NUM"));
			//获取当日未处理跟进数量
			List<DynaBean> followList = dao.getFollowNum(userId,dealerId,startDate,endDate);
			DynaBean db2 = followList.get(0);
			result.put("\"followSize\"", db2.get("NUM"));
			//获取当日未处理邀约数量
			List<DynaBean> inviteList = dao.getInviteNum(userId,dealerId,startDate,endDate);
			DynaBean db3 = inviteList.get(0);
			result.put("\"inviteSize\"", db3.get("NUM"));
			//获取当日未处理订单数量
			List<DynaBean> orderList = dao.getOrderNum(userId,dealerId,startDate,endDate);
			DynaBean db4 = orderList.get(0);
			result.put("\"orderSize\"", db4.get("NUM"));
			//获取当日未处理交车数量
			List<DynaBean> deliveryList = dao.getDeliveryNum(userId,dealerId,startDate,endDate);
			DynaBean db5 = deliveryList.get(0);
			result.put("\"deliverySize\"", db5.get("NUM"));
			//获取当日未处理交车数量
			List<DynaBean> revisitList = dao.getRevisitNum(userId,dealerId,startDate,endDate);
			DynaBean db6 = revisitList.get(0);
			result.put("\"revisitSize\"", db6.get("NUM"));
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", result);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 未处理线索查询
	 */
	public void doQueryLeads() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = null;
		String userId2 = act.getRequest().getParamValue("userId");
		String dealerId = act.getRequest().getParamValue("dealerId");
		String startDate = act.getRequest().getParamValue("startDate");
		String endDate = act.getRequest().getParamValue("endDate");
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(userId2)) {
			userId = userId2;
		} else if(CommonUtils.judgeDirectorLogin(userId2)){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(userId2);
		}
		
		Map strResult = new HashMap();
		List a = new ArrayList();
		try {
			//获取未处理线索信息
			List<DynaBean> leadsList = dao.getLeadsInfo(userId,dealerId,startDate,endDate);
			for(int i=0;i<leadsList.size();i++) {
				DynaBean db = leadsList.get(i);
				Map result = new HashMap();
				if(db.get("CUSTOMER_ID")==null||"".equals(db.get("CUSTOMER_ID"))) {
					result.put("\"customerId\"", "");
				} else {
					result.put("\"customerId\"", db.get("CUSTOMER_ID"));
				}
				if(db.get("CUSTOMER_NAME")==null||"".equals(db.get("CUSTOMER_NAME"))) {
					result.put("\"customerName\"", "");
				} else {
					result.put("\"customerName\"", db.get("CUSTOMER_NAME"));
				}
				if(db.get("TELEPHONE")==null||"".equals(db.get("TELEPHONE"))) {
					result.put("\"telephone\"", "");
				} else {
					result.put("\"telephone\"", db.get("TELEPHONE"));
				}
				result.put("\"allotDate\"", db.get("ALLOT_ADVISER_DATE"));
				result.put("\"leadsOrigin\"", db.get("CODE_DESC"));
				result.put("\"adviser\"", db.get("NAME"));
				result.put("\"leadsAllotId\"", db.get("LEADS_ALLOT_ID"));
				result.put("\"leadsCode\"", db.get("LEADS_CODE"));
				
				a.add(result);
			}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", a);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 未处理跟进查询
	 */
	public void doQueryFollow() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = null;
		String userId2 = act.getRequest().getParamValue("userId");
		String dealerId = act.getRequest().getParamValue("dealerId");
		String startDate = act.getRequest().getParamValue("startDate");
		String endDate = act.getRequest().getParamValue("endDate");
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(userId2)) {
			userId = userId2;
		} else if(CommonUtils.judgeDirectorLogin(userId2)){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(userId2);
		}
		
		Map strResult = new HashMap();
		List a = new ArrayList();
		try {
			//获取未处理跟进信息
			List<DynaBean> followList = dao.getFollowInfo(userId,dealerId,startDate,endDate);
			for(int i=0;i<followList.size();i++) {
				DynaBean db = followList.get(i);
				Map result = new HashMap();
				result.put("\"customerId\"", db.get("CUSTOMER_ID"));
				result.put("\"customerName\"", db.get("CUSTOMER_NAME"));
				result.put("\"telephone\"", db.get("TELEPHONE"));
				result.put("\"intentVehicle\"", db.get("SERIES_NAME"));
				result.put("\"ctmRank\"", db.get("CODE_DESC"));
				result.put("\"adviser\"", db.get("NAME"));
				result.put("\"nextFollowDate\"", db.get("FOLLOW_DATE"));
				result.put("\"taskId\"", db.get("FOLLOW_ID"));
				a.add(result);
			}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", a);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 跟进处理页面信息
	 */
	public void doFollowInit() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String adviserFlag = "10011002";
		String userId = act.getRequest().getParamValue("userId");
		String customerId = act.getRequest().getParamValue("customerId");
		String taskId = act.getRequest().getParamValue("taskId");
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(userId)) {
			adviserFlag = "10011001";
		}
		
		Map strResult = new HashMap();
		Map result = new HashMap();
		try {
			//获取跟进详细信息
			List<DynaBean> followList = dao.getFollowDetailInfo(customerId);
			DynaBean db = followList.get(0);
			Map follow = new HashMap();
			follow.put("\"customerId\"", db.get("CUSTOMER_ID"));
			follow.put("\"dealerId\"", db.get("DEALER_ID"));
			follow.put("\"customerName\"", db.get("CUSTOMER_NAME"));
			follow.put("\"telephone\"", db.get("TELEPHONE"));
			follow.put("\"taskId\"", taskId);
			if(db.get("INTENT_VEHICLE")==null||"".equals(db.get("INTENT_VEHICLE"))) {
				follow.put("\"intentVehicleId\"", "");
			} else {
				follow.put("\"intentVehicleId\"", db.get("INTENT_VEHICLE"));
			}
			if(db.get("SERIES_NAME")==null||"".equals(db.get("SERIES_NAME"))) {
				follow.put("\"intentVehicle\"", "");
			} else {
				follow.put("\"intentVehicle\"", db.get("SERIES_NAME"));
			}
			if(db.get("CTM_RANK")==null||"".equals(db.get("CTM_RANK"))) {
				follow.put("\"ctmRankId\"", "");
			} else {
				follow.put("\"ctmRankId\"", db.get("CTM_RANK"));
			}
			if(db.get("CTM_RANK2")==null||"".equals(db.get("CTM_RANK2"))) {
				follow.put("\"ctmRank\"", "");
			} else {
				follow.put("\"ctmRank\"", db.get("CTM_RANK2"));
			}
			if(db.get("SALES_PROGRESS")==null||"".equals(db.get("SALES_PROGRESS"))) {
				follow.put("\"salesProgressId\"", "");
			} else {
				follow.put("\"salesProgressId\"", db.get("SALES_PROGRESS"));
			}
			if(db.get("SALES_PROGRESS2")==null||"".equals(db.get("SALES_PROGRESS2"))) {
				follow.put("\"salesProgress\"", "");
			} else {
				follow.put("\"salesProgress\"", db.get("SALES_PROGRESS2"));
			}
			if(db.get("BUDGET")==null||"".equals(db.get("BUDGET"))) {
				follow.put("\"buyBudgetId\"", "");
			} else {
				follow.put("\"buyBudgetId\"", db.get("BUDGET"));
			}
			if(db.get("BUDGET2")==null||"".equals(db.get("BUDGET2"))) {
				follow.put("\"buyBudget\"", "");
			} else {
				follow.put("\"buyBudget\"", db.get("BUDGET2"));
			}
			if(db.get("CTM_PROP")==null||"".equals(db.get("CTM_PROP"))) {
				follow.put("\"customerTypeId\"", "");
			} else {
				follow.put("\"customerTypeId\"", db.get("CTM_PROP"));
			}
			if(db.get("CTM_PROP2")==null||"".equals(db.get("CTM_PROP2"))) {
				follow.put("\"customerType\"", "");
			} else {
				follow.put("\"customerType\"", db.get("CTM_PROP2"));
			}
			result.put("\"customerList\"", follow);
			
			//获取意向等级下拉列表
			List<DynaBean> ctmRankList = dao.getCtmRankInfo();
			List ctmRankOP = new ArrayList();
			for(int i=0;i<ctmRankList.size();i++) {
				DynaBean db2 = ctmRankList.get(i);
				Map ctmRank = new HashMap();
				ctmRank.put("\"ctmRankId\"", db2.get("CODE_ID"));
				ctmRank.put("\"ctmRankName\"", db2.get("CODE_DESC"));
				ctmRankOP.add(ctmRank);
			}
			result.put("\"ctmRank\"", ctmRankOP);
			
			//获取意向车型下拉列表
			List<DynaBean> intentVehicleList = dao.getIntentVehicleInfo();
			List intentVehicleOP = new ArrayList();
			for(int i=0;i<intentVehicleList.size();i++) {
				DynaBean db3 = intentVehicleList.get(i);
				Map intentVehicle = new HashMap();
				intentVehicle.put("\"seriesId\"", db3.get("SERIES_ID"));
				intentVehicle.put("\"seriesName\"", db3.get("SERIES_NAME"));
				if(db3.get("UP_SERIES_ID")==null||"".equals(db3.get("UP_SERIES_ID"))) {
					intentVehicle.put("\"upSeriesId\"", "");
				} else {
					intentVehicle.put("\"upSeriesId\"", db3.get("UP_SERIES_ID"));
				}
				intentVehicleOP.add(intentVehicle);
			}
			result.put("\"intentVehicle\"", intentVehicleOP);
			
			//获取销售流程进度下拉列表
			List<DynaBean> salesProgressList = dao.getSalesProgressInfo();
			List salesProgressOP = new ArrayList();
			for(int i=0;i<salesProgressList.size();i++) {
				DynaBean db4 = salesProgressList.get(i);
				Map salesProgress = new HashMap();
				salesProgress.put("\"salesProgressId\"", db4.get("CODE_ID"));
				salesProgress.put("\"salesProgressName\"", db4.get("CODE_DESC"));
				salesProgressOP.add(salesProgress);
			}
			result.put("\"salesProgress\"", salesProgressOP);
			
			//获取购车预算下拉列表
			List<DynaBean> buyBudgetList = dao.getBuyBudgetInfo();
			List buyBudgetOP = new ArrayList();
			for(int i=0;i<buyBudgetList.size();i++) {
				DynaBean db5 = buyBudgetList.get(i);
				Map buyBudget = new HashMap();
				buyBudget.put("\"buyBudgetId\"", db5.get("CODE_ID"));
				buyBudget.put("\"buyBudgetName\"", db5.get("CODE_DESC"));
				buyBudgetOP.add(buyBudget);
			}
			result.put("\"buyBudget\"", buyBudgetOP);
			
			//获取客户类型下拉列表
			List<DynaBean> customerTypeList = dao.getCustomerTypeInfo();
			List customerTypeOP = new ArrayList();
			for(int i=0;i<customerTypeList.size();i++) {
				DynaBean db6 = customerTypeList.get(i);
				Map customerType = new HashMap();
				customerType.put("\"customerTypeId\"", db6.get("CODE_ID"));
				customerType.put("\"customerTypeName\"", db6.get("CODE_DESC"));
				customerTypeOP.add(customerType);
			}
			result.put("\"customerType\"", customerTypeOP);
			
			//获取跟进方式下拉列表
			List<DynaBean> followTypeList = dao.getFollowTypeInfo();
			List followTypeOP = new ArrayList();
			for(int i=0;i<followTypeList.size();i++) {
				DynaBean db7 = followTypeList.get(i);
				Map followType = new HashMap();
				followType.put("\"followTypeId\"", db7.get("CODE_ID"));
				followType.put("\"followTypeName\"", db7.get("CODE_DESC"));
				followTypeOP.add(followType);
			}
			result.put("\"followType\"", followTypeOP);
			
			//获取邀约方式下拉列表
			List<DynaBean> inviteTypeList = dao.getInviteTypeInfo();
			List inviteTypeOP = new ArrayList();
			for(int i=0;i<inviteTypeList.size();i++) {
				DynaBean db8 = inviteTypeList.get(i);
				Map inviteType = new HashMap();
				inviteType.put("\"inviteTypeId\"", db8.get("CODE_ID"));
				inviteType.put("\"inviteTypeName\"", db8.get("CODE_DESC"));
				inviteTypeOP.add(inviteType);
			}
			result.put("\"inviteType\"", inviteTypeOP);
			
			//获取战败原因下拉列表
			List<DynaBean> defeatReasonList = dao.getDefeatReasonInfo();
			List defeatReasonOP = new ArrayList();
			for(int i=0;i<defeatReasonList.size();i++) {
				DynaBean db9 = defeatReasonList.get(i);
				Map defeatReason = new HashMap();
				defeatReason.put("\"defeatReasonId\"", db9.get("CODE_ID"));
				defeatReason.put("\"defeatReasonName\"", db9.get("CODE_DESC"));
				if(db9.get("UP_CODE_ID")==null||"".equals(db9.get("UP_CODE_ID"))) {
					defeatReason.put("\"upCodeId\"", "");
				} else {
					defeatReason.put("\"upCodeId\"", db9.get("UP_CODE_ID"));
				}
				defeatReasonOP.add(defeatReason);
			}
			result.put("\"defeatReason\"", defeatReasonOP);
			
			//获取战败车型下拉列表
			List<DynaBean> defeatVehicleList = dao.getDefeatVehicleInfo();
			List defeatVehicleOP = new ArrayList();
			for(int i=0;i<defeatVehicleList.size();i++) {
				DynaBean db10 = defeatVehicleList.get(i);
				Map defeatVehicle = new HashMap();
				defeatVehicle.put("\"seriesId\"", db10.get("SERIES_ID"));
				defeatVehicle.put("\"seriesName\"", db10.get("SERIES_NAME"));
				if(db10.get("UP_SERIES_ID")==null||"".equals(db10.get("UP_SERIES_ID"))) {
					defeatVehicle.put("\"upSeriesId\"", "");
				} else {
					defeatVehicle.put("\"upSeriesId\"", db10.get("UP_SERIES_ID"));
				}
				defeatVehicleOP.add(defeatVehicle);
			}
			result.put("\"defeatVehicle\"", defeatVehicleOP);
			
			result.put("\"adviserFlag\"", adviserFlag);
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", result);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 跟进处理数据提交
	 * @throws UnsupportedEncodingException 
	 */
	public void doFollow() throws UnsupportedEncodingException {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String customerId = act.getRequest().getParamValue("customerId");
		String taskId = act.getRequest().getParamValue("taskId");
		String userId = act.getRequest().getParamValue("userId");
		String dealerId = act.getRequest().getParamValue("dealerId");
		String intentType = act.getRequest().getParamValue("intentType");
		String salesProgress = act.getRequest().getParamValue("salesProgress");
		String followInfo = act.getRequest().getParamValue("followInfo");
//		if(followInfo!=null&&!"".equals(followInfo)){
//			followInfo = new String(followInfo.getBytes("iso-8859-1"),"gbk");
//		}
		String buyBudget = act.getRequest().getParamValue("buyBudget");
		String intentVehicle = act.getRequest().getParamValue("intentVehicle");
		String customerType = act.getRequest().getParamValue("customerType");
		String nextTaskType = act.getRequest().getParamValue("nextTaskType");
		String nextFollowDate = act.getRequest().getParamValue("nextFollowDate");
		String followType = act.getRequest().getParamValue("followType");
		String followPlan = act.getRequest().getParamValue("followPlan");
//		if(followPlan!=null&&!"".equals(followPlan)){
//			followPlan = new String(followPlan.getBytes("iso-8859-1"),"gbk");
//		}
		String planInviteDate = act.getRequest().getParamValue("planInviteDate");
		String planMeetDate = act.getRequest().getParamValue("planMeetDate");
		String inviteType = act.getRequest().getParamValue("inviteType");
		String orderDate = act.getRequest().getParamValue("orderDate");
		String defeatVehicle = act.getRequest().getParamValue("defeatVehicle");
		String defeatReason = act.getRequest().getParamValue("defeatReason");
		String defeatDate = act.getRequest().getParamValue("defeatDate");
		String defeatRemark = act.getRequest().getParamValue("defeatRemark");
//		if(defeatRemark!=null&&!"".equals(defeatRemark)){
//			defeatRemark = new String(defeatRemark.getBytes("iso-8859-1"),"gbk");
//		}
		String failureDate = act.getRequest().getParamValue("failureDate");
		String failureRemark = act.getRequest().getParamValue("failureRemark");
//		if(failureRemark!=null&&!"".equals(failureRemark)){
//			failureRemark = new String(failureRemark.getBytes("iso-8859-1"),"gbk");
//		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Map strResult = new HashMap();
	
		try {
			//PC和移动端同时处理问题
			int ifHandle = CommonUtils.getTheSameData4(taskId);
			if(ifHandle==10041002) {//未处理的情况
				// 判断是否可以做订单
				//根据CustomerId获取电话号码
				TPcCustomerPO cuspo = new TPcCustomerPO();
				cuspo.setCustomerId(Long.parseLong(customerId));
				TPcCustomerPO cuspo2 = (TPcCustomerPO)dao.select(cuspo).get(0);
				String telephone = cuspo2.getTelephone();
				Map<String,String> map=new HashMap<String,String>();
				map.put("telephone",telephone);
				map.put("dealerId", dealerId);
				if(!CommonUtils.judgeIfOrder(map) && "order".equals(nextTaskType)) { //不可以做
					strResult.put("\"success\"", "false");
					strResult.put("\"msg\"", "该客户无首次到店客流信息，无法生成订车计划!");
					act.setOutData("\"strResult\"", strResult);
				} else {
					//完成本次跟进任务
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
					newCustomerPo.setCtmRank(intentType);
					newCustomerPo.setSalesProgress(salesProgress);
					newCustomerPo.setBuyBudget(buyBudget);
					newCustomerPo.setCtmProp(customerType);
					newCustomerPo.setIntentVehicle(intentVehicle);
					if(nextTaskType=="follow"||"follow".equals(nextTaskType)){//跟进
						newCustomerPo.setNextFollowTime(sdf.parse(nextFollowDate));//下次跟进时间
					} else if(nextTaskType=="invite"||"invite".equals(nextTaskType)) {
						newCustomerPo.setNextFollowTime(sdf.parse(planInviteDate));//下次跟进时间
					}
					dao.update(oldCustomerPo,newCustomerPo);
					
					//增加接触点信息
					CommonUtils.addContackPoint(Constant.POINT_WAY_04, followInfo, customerId, userId, dealerId);
					
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(taskId,Constant.REMIND_TYPE_05.toString());
					
					if(nextTaskType=="follow"||"follow".equals(nextTaskType)) {//点击跟进后保存
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
						followPo.setCreateBy(userId);
						followPo.setPreviousTask(Long.parseLong(taskId));
						followPo.setOldSalesProgress(salesProgress);
						followPo.setDataFrom("60601002");
						dao.insert(followPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), newTaskId, customerId, dealerId, userId, nextFollowDate,"");
					} else if(nextTaskType=="invite"||"invite".equals(nextTaskType)) {//点击邀约后保存
						//新增下次计划邀约任务
						TPcInvitePO invitePo = new TPcInvitePO();
						invitePo.setInviteId(Long.parseLong(newTaskId));
						invitePo.setCustomerId(Long.parseLong(customerId));
						invitePo.setCreateDate(new Date());
						invitePo.setCreateBy(userId);
						invitePo.setPlanInviteDate(sdf.parse(planInviteDate));
						invitePo.setPlanMeetDate(sdf.parse(planMeetDate));
						invitePo.setTaskStatus(Constant.TASK_STATUS_01);
						invitePo.setInviteType(Integer.parseInt(inviteType));
						invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_04);
						invitePo.setIfPlan(Constant.IF_TYPE_NO);
						invitePo.setOldLevel(intentType);
						invitePo.setNewLevel(null);
						invitePo.setPreviousTask(Long.parseLong(taskId));
						invitePo.setOldSalesProgress(salesProgress);
						invitePo.setDataFrom("60601002");
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), newTaskId, customerId, dealerId, userId, planInviteDate,"");
						dao.insert(invitePo);
					} else if(nextTaskType=="order"||"order".equals(nextTaskType)) {//点击订单后保存
						//新增下次订单任务
						TPcOrderPO orderPo = new TPcOrderPO();
						orderPo.setOrderId(Long.parseLong(newTaskId));
						orderPo.setCustomerId(Long.parseLong(customerId));
						orderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
						orderPo.setOldLevel(intentType);
						orderPo.setCreateDate(new Date());
						orderPo.setCreateBy(userId);
						orderPo.setOldSalesProgress(salesProgress);
						orderPo.setPreviousTask(Long.parseLong(taskId));
						orderPo.setTaskStatus(Constant.TASK_STATUS_01);
						orderPo.setOrderDate(sdf.parse(orderDate));
						orderPo.setDataFrom("60601002");
						dao.insert(orderPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_08.toString(), newTaskId, customerId, dealerId, userId, orderDate,"");
					} else if(nextTaskType=="defeat"||"defeat".equals(nextTaskType)) {//点击战败后保存
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
						defeatPo.setDefeatEndDate(sdf.parse(defeatDate));
						defeatPo.setStatus(Constant.STATUS_ENABLE);
						defeatPo.setOldLevel(ctmRank);
						defeatPo.setNewLevel(Constant.INTENT_TYPE_E.toString());
						defeatPo.setOldSalesProgress(sProgress);
						defeatPo.setSalesProgress(Constant.SALSE_PROGRESS_01.toString());
						defeatPo.setReasonAnalysis(defeatRemark);
						defeatPo.setCreateBy(userId);
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(taskId));
						defeatPo.setDefeatType(defeatReason);
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						defeatPo.setDataFrom("60601002");
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为战败
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_E.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_10.toString(), newTaskId, customerId, dealerId, "", sdf.format(new Date()),"");
					} else if(nextTaskType=="failure"||"failure".equals(nextTaskType)) {//点击失效后保存
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
						defeatPo.setCreateBy(userId);
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(taskId));
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						defeatPo.setFailureDate(sdf.parse(failureDate));
						defeatPo.setDataFrom("60601002");
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为失效
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_L.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_11.toString(), newTaskId, customerId, dealerId, "", sdf.format(new Date()),"");
					}
					strResult.put("\"success\"", "true");
					strResult.put("\"msg\"", "操作成功");
					
					act.setOutData("\"strResult\"", strResult);
				}
			} else { //已处理的情况
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(taskId,Constant.REMIND_TYPE_05.toString());
				strResult.put("\"success\"", "false");
				strResult.put("\"msg\"", "该条数据已被执行过！");
				act.setOutData("\"strResult\"", strResult);
			}
			
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	

	/**
	 * 获取意向车型/战败车型下拉列表
	 */
	public void getIntentVehicleList() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String flag = act.getRequest().getParamValue("flag");
		Map strResult = new HashMap();
		List a = new ArrayList();
		List<DynaBean> intentVehicleList = null;
		try {
			//判断传入参数
			if("intent".equals(flag) || flag == "intent") {
				//获取意向车型下拉列表
				intentVehicleList = dao.getIntentVehicleInfo();
			} else {
				//获取战败车型下拉列表
				intentVehicleList = dao.getDefeatVehicleInfo();
			}
			
			for(int i=0;i<intentVehicleList.size();i++) {
				DynaBean db = intentVehicleList.get(i);
				Map result = new HashMap();
				result.put("\"seriesId\"", db.get("SERIES_ID"));
				result.put("\"seriesName\"", db.get("SERIES_NAME"));
				if(db.get("UP_SERIES_ID")==null||"".equals(db.get("UP_SERIES_ID"))) {
					result.put("\"upSeriesId\"", "");
				} else {
					result.put("\"upSeriesId\"", db.get("UP_SERIES_ID"));
				}
				a.add(result);
			}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", a);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 未处理回访查询
	 */
	public void doQueryRevisit() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = null;
		String userId2 = act.getRequest().getParamValue("userId");
		String dealerId = act.getRequest().getParamValue("dealerId");
		String startDate = act.getRequest().getParamValue("startDate");
		String endDate = act.getRequest().getParamValue("endDate");
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(userId2)) {
			userId = userId2;
		} else if(CommonUtils.judgeDirectorLogin(userId2)){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(userId2);
		}
		
		Map strResult = new HashMap();
		List a = new ArrayList();
		try {
			//获取未处理回访信息
			List<DynaBean> revisitList = dao.getRevisitInfo(userId,dealerId,startDate,endDate);
			for(int i=0;i<revisitList.size();i++) {
				DynaBean db = revisitList.get(i);
				Map result = new HashMap();
				result.put("\"customerId\"", db.get("CUSTOMER_ID"));
				result.put("\"customerName\"", db.get("CUSTOMER_NAME"));
				result.put("\"telephone\"", db.get("TELEPHONE"));
				result.put("\"intentVehicle\"", db.get("SERIES_NAME"));
				result.put("\"buyDate\"", db.get("BUY_DATE"));
				result.put("\"revisitType\"", db.get("CODE_DESC"));
				result.put("\"adviser\"", db.get("NAME"));
				if(db.get("REVISIT_DATE")==null||"".equals(db.get("REVISIT_DATE"))) {
					result.put("\"nextRevisitDate\"", "");
				} else {
					result.put("\"nextRevisitDate\"", db.get("REVISIT_DATE"));
				}
				
				a.add(result);
			}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", a);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 未处理订单查询
	 */
	public void doQueryOrder() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = null;
		String userId2 = act.getRequest().getParamValue("userId");
		String dealerId = act.getRequest().getParamValue("dealerId");
		String startDate = act.getRequest().getParamValue("startDate");
		String endDate = act.getRequest().getParamValue("endDate");
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(userId2)) {
			userId = userId2;
		} else if(CommonUtils.judgeDirectorLogin(userId2)){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(userId2);
		}
		
		Map strResult = new HashMap();
		List a = new ArrayList();
		try {
			//获取未处理订单信息
			List<DynaBean> orderList = dao.getOrderInfo(userId,dealerId,startDate,endDate);
			for(int i=0;i<orderList.size();i++) {
				DynaBean db = orderList.get(i);
				Map result = new HashMap();
				result.put("\"customerId\"", db.get("CUSTOMER_ID"));
				result.put("\"customerName\"", db.get("CUSTOMER_NAME"));
				result.put("\"telephone\"", db.get("TELEPHONE"));
				result.put("\"intentVehicle\"", db.get("SERIES_NAME"));
				result.put("\"adviser\"", db.get("NAME"));
				result.put("\"orderDate\"", db.get("ORDER_DATE"));
				
				a.add(result);
			}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", a);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 未处理交车查询
	 */
	public void doQueryDelivery() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = null;
		String userId2 = act.getRequest().getParamValue("userId");
		String dealerId = act.getRequest().getParamValue("dealerId");
		String startDate = act.getRequest().getParamValue("startDate");
		String endDate = act.getRequest().getParamValue("endDate");
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(userId2)) {
			userId = userId2;
		} else if(CommonUtils.judgeDirectorLogin(userId2)){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(userId2);
		}
		
		Map strResult = new HashMap();
		List a = new ArrayList();
		try {
			//获取未处理交车信息
			List<DynaBean> deliveryList = dao.getDeliveryInfo(userId,dealerId,startDate,endDate);
			for(int i=0;i<deliveryList.size();i++) {
				DynaBean db = deliveryList.get(i);
				Map result = new HashMap();
				result.put("\"customerId\"", db.get("CUSTOMER_ID"));
				result.put("\"customerName\"", db.get("CUSTOMER_NAME"));
				result.put("\"telephone\"", db.get("TELEPHONE"));
				result.put("\"intentVehicle\"", db.get("BUYMODEL"));
				result.put("\"adviser\"", db.get("NAME"));
				result.put("\"deliveryDate\"", db.get("DELIVERY_DATE"));
				
				a.add(result);
			}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", a);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 未处理邀约查询
	 */
	public void doQueryInvite() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = null;
		String userId2 = act.getRequest().getParamValue("userId");
		String dealerId = act.getRequest().getParamValue("dealerId");
		String startDate = act.getRequest().getParamValue("startDate");
		String endDate = act.getRequest().getParamValue("endDate");
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(userId2)) {
			userId = userId2;
		} else if(CommonUtils.judgeDirectorLogin(userId2)){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(userId2);
		}
		
		Map strResult = new HashMap();
		List a = new ArrayList();
		try {
			//获取未处理邀约信息
			List<DynaBean> inviteList = dao.getInviteInfo(userId,dealerId,startDate,endDate);
			for(int i=0;i<inviteList.size();i++) {
				DynaBean db = inviteList.get(i);
				Map result = new HashMap();
				result.put("\"customerId\"", db.get("CUSTOMER_ID"));
				result.put("\"customerName\"", db.get("CUSTOMER_NAME"));
				result.put("\"telephone\"", db.get("TELEPHONE"));
				result.put("\"intentVehicle\"", db.get("SERIES_NAME"));
				result.put("\"ctmRank\"", db.get("CODE_DESC"));
				result.put("\"inviteLevel\"", db.get("INVITE_TYPE"));
				result.put("\"adviser\"", db.get("NAME"));
				result.put("\"inviteDate\"", db.get("PLAN_DATE"));
				result.put("\"inviteId\"", db.get("INVITE_ID"));
				result.put("\"inviteShopId\"", db.get("INVITE_SHOP_ID"));
				a.add(result);
			}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", a);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 邀约处理页面信息(电话邀约)
	 */
	public void doInviteInit() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String adviserFlag = "10011002";
		String userId = act.getRequest().getParamValue("userId");
		String customerId = act.getRequest().getParamValue("customerId");
		String inviteId = act.getRequest().getParamValue("inviteId");
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(userId)) {
			adviserFlag = "10011001";
		}
		
		Map strResult = new HashMap();
		Map result = new HashMap();
		try {
			//获取邀约详细信息
			List<DynaBean> inviteList = dao.getInviteDetailInfo(customerId);
			//获取电话邀约信息
			List<DynaBean> inviteList2 = dao.getInviteDetailInfo2(inviteId);
			DynaBean db = inviteList.get(0);
			DynaBean db0 = inviteList2.get(0);
			String inviteType = "";
			String inviteType2 = "";
			if(db0.get("INVITE_TYPE")!=null) {
				inviteType = db0.get("INVITE_TYPE").toString();
			}
			if(db0.get("INVITE_TYPE2")!=null) {
				inviteType2 = db0.get("INVITE_TYPE2").toString();
			}
			Map invite = new HashMap();
			invite.put("\"customerId\"", db.get("CUSTOMER_ID"));
			invite.put("\"dealerId\"", db.get("DEALER_ID"));
			invite.put("\"customerName\"", db.get("CUSTOMER_NAME"));
			invite.put("\"telephone\"", db.get("TELEPHONE"));
			invite.put("\"inviteId\"", inviteId);
			invite.put("\"inviteType\"", inviteType);
			invite.put("\"inviteType2\"", inviteType2);
			if(db.get("INTENT_VEHICLE")==null||"".equals(db.get("INTENT_VEHICLE"))) {
				invite.put("\"intentVehicleId\"", "");
			} else {
				invite.put("\"intentVehicleId\"", db.get("INTENT_VEHICLE"));
			}
			if(db.get("SERIES_NAME")==null||"".equals(db.get("SERIES_NAME"))) {
				invite.put("\"intentVehicle\"", "");
			} else {
				invite.put("\"intentVehicle\"", db.get("SERIES_NAME"));
			}
			if(db.get("CTM_RANK")==null||"".equals(db.get("CTM_RANK"))) {
				invite.put("\"ctmRankId\"", "");
			} else {
				invite.put("\"ctmRankId\"", db.get("CTM_RANK"));
			}
			if(db.get("CTM_RANK2")==null||"".equals(db.get("CTM_RANK2"))) {
				invite.put("\"ctmRank\"", "");
			} else {
				invite.put("\"ctmRank\"", db.get("CTM_RANK2"));
			}
			if(db.get("SALES_PROGRESS")==null||"".equals(db.get("SALES_PROGRESS"))) {
				invite.put("\"salesProgressId\"", "");
			} else {
				invite.put("\"salesProgressId\"", db.get("SALES_PROGRESS"));
			}
			if(db.get("SALES_PROGRESS2")==null||"".equals(db.get("SALES_PROGRESS2"))) {
				invite.put("\"salesProgress\"", "");
			} else {
				invite.put("\"salesProgress\"", db.get("SALES_PROGRESS2"));
			}
			
			result.put("\"customerList\"", invite);
			
			//获取意向等级下拉列表
			List<DynaBean> ctmRankList = dao.getCtmRankInfo();
			List ctmRankOP = new ArrayList();
			for(int i=0;i<ctmRankList.size();i++) {
				DynaBean db2 = ctmRankList.get(i);
				Map ctmRank = new HashMap();
				ctmRank.put("\"ctmRankId\"", db2.get("CODE_ID"));
				ctmRank.put("\"ctmRankName\"", db2.get("CODE_DESC"));
				ctmRankOP.add(ctmRank);
			}
			result.put("\"ctmRank\"", ctmRankOP);
			
			//获取意向车型下拉列表
			List<DynaBean> intentVehicleList = dao.getIntentVehicleInfo();
			List intentVehicleOP = new ArrayList();
			for(int i=0;i<intentVehicleList.size();i++) {
				DynaBean db3 = intentVehicleList.get(i);
				Map intentVehicle = new HashMap();
				intentVehicle.put("\"seriesId\"", db3.get("SERIES_ID"));
				intentVehicle.put("\"seriesName\"", db3.get("SERIES_NAME"));
				if(db3.get("UP_SERIES_ID")==null||"".equals(db3.get("UP_SERIES_ID"))) {
					intentVehicle.put("\"upSeriesId\"", "");
				} else {
					intentVehicle.put("\"upSeriesId\"", db3.get("UP_SERIES_ID"));
				}
				intentVehicleOP.add(intentVehicle);
			}
			result.put("\"intentVehicle\"", intentVehicleOP);
			
			//获取销售流程进度下拉列表
			List<DynaBean> salesProgressList = dao.getSalesProgressInfo();
			List salesProgressOP = new ArrayList();
			for(int i=0;i<salesProgressList.size();i++) {
				DynaBean db4 = salesProgressList.get(i);
				Map salesProgress = new HashMap();
				salesProgress.put("\"salesProgressId\"", db4.get("CODE_ID"));
				salesProgress.put("\"salesProgressName\"", db4.get("CODE_DESC"));
				salesProgressOP.add(salesProgress);
			}
			result.put("\"salesProgress\"", salesProgressOP);
			
			//获取购车预算下拉列表
			List<DynaBean> buyBudgetList = dao.getBuyBudgetInfo();
			List buyBudgetOP = new ArrayList();
			for(int i=0;i<buyBudgetList.size();i++) {
				DynaBean db5 = buyBudgetList.get(i);
				Map buyBudget = new HashMap();
				buyBudget.put("\"buyBudgetId\"", db5.get("CODE_ID"));
				buyBudget.put("\"buyBudgetName\"", db5.get("CODE_DESC"));
				buyBudgetOP.add(buyBudget);
			}
			result.put("\"buyBudget\"", buyBudgetOP);
			
			//获取客户类型下拉列表
			List<DynaBean> customerTypeList = dao.getCustomerTypeInfo();
			List customerTypeOP = new ArrayList();
			for(int i=0;i<customerTypeList.size();i++) {
				DynaBean db6 = customerTypeList.get(i);
				Map customerType = new HashMap();
				customerType.put("\"customerTypeId\"", db6.get("CODE_ID"));
				customerType.put("\"customerTypeName\"", db6.get("CODE_DESC"));
				customerTypeOP.add(customerType);
			}
			result.put("\"customerType\"", customerTypeOP);
			
			//获取跟进方式下拉列表
			List<DynaBean> followTypeList = dao.getFollowTypeInfo();
			List followTypeOP = new ArrayList();
			for(int i=0;i<followTypeList.size();i++) {
				DynaBean db7 = followTypeList.get(i);
				Map followType = new HashMap();
				followType.put("\"followTypeId\"", db7.get("CODE_ID"));
				followType.put("\"followTypeName\"", db7.get("CODE_DESC"));
				followTypeOP.add(followType);
			}
			result.put("\"followType\"", followTypeOP);
			
			//获取邀约方式下拉列表
			List<DynaBean> inviteTypeList = dao.getInviteTypeInfo();
			List inviteTypeOP = new ArrayList();
			for(int i=0;i<inviteTypeList.size();i++) {
				DynaBean db8 = inviteTypeList.get(i);
				Map inviteTypeNew = new HashMap();
				inviteTypeNew.put("\"inviteTypeId\"", db8.get("CODE_ID"));
				inviteTypeNew.put("\"inviteTypeName\"", db8.get("CODE_DESC"));
				inviteTypeOP.add(inviteTypeNew);
			}
			result.put("\"inviteType\"", inviteTypeOP);
			
			//获取战败原因下拉列表
			List<DynaBean> defeatReasonList = dao.getDefeatReasonInfo();
			List defeatReasonOP = new ArrayList();
			for(int i=0;i<defeatReasonList.size();i++) {
				DynaBean db9 = defeatReasonList.get(i);
				Map defeatReason = new HashMap();
				defeatReason.put("\"defeatReasonId\"", db9.get("CODE_ID"));
				defeatReason.put("\"defeatReasonName\"", db9.get("CODE_DESC"));
				if(db9.get("UP_CODE_ID")==null||"".equals(db9.get("UP_CODE_ID"))) {
					defeatReason.put("\"upCodeId\"", "");
				} else {
					defeatReason.put("\"upCodeId\"", db9.get("UP_CODE_ID"));
				}
				defeatReasonOP.add(defeatReason);
			}
			result.put("\"defeatReason\"", defeatReasonOP);
			
			//获取战败车型下拉列表
			List<DynaBean> defeatVehicleList = dao.getDefeatVehicleInfo();
			List defeatVehicleOP = new ArrayList();
			for(int i=0;i<defeatVehicleList.size();i++) {
				DynaBean db10 = defeatVehicleList.get(i);
				Map defeatVehicle = new HashMap();
				defeatVehicle.put("\"seriesId\"", db10.get("SERIES_ID"));
				defeatVehicle.put("\"seriesName\"", db10.get("SERIES_NAME"));
				if(db10.get("UP_SERIES_ID")==null||"".equals(db10.get("UP_SERIES_ID"))) {
					defeatVehicle.put("\"upSeriesId\"", "");
				} else {
					defeatVehicle.put("\"upSeriesId\"", db10.get("UP_SERIES_ID"));
				}
				defeatVehicleOP.add(defeatVehicle);
			}
			result.put("\"defeatVehicle\"", defeatVehicleOP);
			
			//获取邀约类型下拉列表
			List<DynaBean> inviteWayList = dao.getInviteWayInfo();
			List inviteWayOP = new ArrayList();
			for(int i=0;i<inviteWayList.size();i++) {
				DynaBean db9 = inviteWayList.get(i);
				Map inviteWay = new HashMap();
				inviteWay.put("\"inviteWayId\"", db9.get("CODE_ID"));
				inviteWay.put("\"inviteWayName\"", db9.get("CODE_DESC"));
				inviteWayOP.add(inviteWay);
			}
			result.put("\"inviteWay\"", inviteWayOP);
			
			result.put("\"adviserFlag\"", adviserFlag);
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", result);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 邀约处理页面信息(邀约到店)
	 */
	public void doInviteShopInit() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String adviserFlag = "10011002";
		String userId = act.getRequest().getParamValue("userId");
		String customerId = act.getRequest().getParamValue("customerId");
		String inviteShopId = act.getRequest().getParamValue("inviteShopId");
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(userId)) {
			adviserFlag = "10011001";
		}
		
		Map strResult = new HashMap();
		Map result = new HashMap();
		try {
			//获取邀约详细信息
			List<DynaBean> inviteShopList = dao.getInviteDetailInfo(customerId);
			//获取邀约到店信息中预约到店时间和inviteId
			List<DynaBean> inviteShopList3 = dao.getPreMeetDate(inviteShopId);
			DynaBean db00 = inviteShopList3.get(0);
			String inviteId = db00.get("INVITE_ID").toString();;
			//获取邀约到店信息
			List<DynaBean> inviteShopList2 = dao.getInviteDetailInfo2(inviteId);
			
			DynaBean db = inviteShopList.get(0);
			DynaBean db0 = inviteShopList2.get(0);
			
			String inviteType = "";
			String inviteType2 = "";
			String inviteWay = "";
			String inviteWay2 = "";
			String inviteRemartk = "";
			String inviteShopDate = "";
			if(db0.get("INVITE_TYPE")!=null) {
				inviteType = db0.get("INVITE_TYPE").toString();
			}
			if(db0.get("INVITE_TYPE2")!=null) {
				inviteType2 = db0.get("INVITE_TYPE2").toString();
			}
			if(db0.get("INVITE_WAY")!=null) {
				inviteWay = db0.get("INVITE_WAY").toString();
			}
			if(db0.get("INVITE_WAY2")!=null) {
				inviteWay2 = db0.get("INVITE_WAY2").toString();
			}
			if(db0.get("REMARK")!=null) {
				inviteRemartk = db0.get("REMARK").toString();
			}
			if(db00.get("INVITE_SHOP_DATE")!=null) {
				inviteShopDate = db00.get("INVITE_SHOP_DATE").toString();
			}
			Map invite = new HashMap();
			invite.put("\"customerId\"", db.get("CUSTOMER_ID"));
			invite.put("\"dealerId\"", db.get("DEALER_ID"));
			invite.put("\"customerName\"", db.get("CUSTOMER_NAME"));
			invite.put("\"telephone\"", db.get("TELEPHONE"));
			invite.put("\"inviteId\"", inviteId);
			invite.put("\"inviteShopId\"", inviteShopId);
			invite.put("\"inviteType\"", inviteType);
			invite.put("\"inviteType2\"", inviteType2);
			invite.put("\"inviteWay\"", inviteWay);
			invite.put("\"inviteWay2\"", inviteWay2);
			invite.put("\"preInviteShopDate\"", inviteShopDate);
			invite.put("\"inviteInfo\"", inviteRemartk);
			if(db.get("INTENT_VEHICLE")==null||"".equals(db.get("INTENT_VEHICLE"))) {
				invite.put("\"intentVehicleId\"", "");
			} else {
				invite.put("\"intentVehicleId\"", db.get("INTENT_VEHICLE"));
			}
			if(db.get("SERIES_NAME")==null||"".equals(db.get("SERIES_NAME"))) {
				invite.put("\"intentVehicle\"", "");
			} else {
				invite.put("\"intentVehicle\"", db.get("SERIES_NAME"));
			}
			if(db.get("CTM_RANK")==null||"".equals(db.get("CTM_RANK"))) {
				invite.put("\"ctmRankId\"", "");
			} else {
				invite.put("\"ctmRankId\"", db.get("CTM_RANK"));
			}
			if(db.get("CTM_RANK2")==null||"".equals(db.get("CTM_RANK2"))) {
				invite.put("\"ctmRank\"", "");
			} else {
				invite.put("\"ctmRank\"", db.get("CTM_RANK2"));
			}
			if(db.get("SALES_PROGRESS")==null||"".equals(db.get("SALES_PROGRESS"))) {
				invite.put("\"salesProgressId\"", "");
			} else {
				invite.put("\"salesProgressId\"", db.get("SALES_PROGRESS"));
			}
			if(db.get("SALES_PROGRESS2")==null||"".equals(db.get("SALES_PROGRESS2"))) {
				invite.put("\"salesProgress\"", "");
			} else {
				invite.put("\"salesProgress\"", db.get("SALES_PROGRESS2"));
			}
			
			result.put("\"customerList\"", invite);
			
			//获取意向等级下拉列表
			List<DynaBean> ctmRankList = dao.getCtmRankInfo();
			List ctmRankOP = new ArrayList();
			for(int i=0;i<ctmRankList.size();i++) {
				DynaBean db2 = ctmRankList.get(i);
				Map ctmRank = new HashMap();
				ctmRank.put("\"ctmRankId\"", db2.get("CODE_ID"));
				ctmRank.put("\"ctmRankName\"", db2.get("CODE_DESC"));
				ctmRankOP.add(ctmRank);
			}
			result.put("\"ctmRank\"", ctmRankOP);
			
			//获取意向车型下拉列表
			List<DynaBean> intentVehicleList = dao.getIntentVehicleInfo();
			List intentVehicleOP = new ArrayList();
			for(int i=0;i<intentVehicleList.size();i++) {
				DynaBean db3 = intentVehicleList.get(i);
				Map intentVehicle = new HashMap();
				intentVehicle.put("\"seriesId\"", db3.get("SERIES_ID"));
				intentVehicle.put("\"seriesName\"", db3.get("SERIES_NAME"));
				if(db3.get("UP_SERIES_ID")==null||"".equals(db3.get("UP_SERIES_ID"))) {
					intentVehicle.put("\"upSeriesId\"", "");
				} else {
					intentVehicle.put("\"upSeriesId\"", db3.get("UP_SERIES_ID"));
				}
				intentVehicleOP.add(intentVehicle);
			}
			result.put("\"intentVehicle\"", intentVehicleOP);
			
			//获取销售流程进度下拉列表
			List<DynaBean> salesProgressList = dao.getSalesProgressInfo();
			List salesProgressOP = new ArrayList();
			for(int i=0;i<salesProgressList.size();i++) {
				DynaBean db4 = salesProgressList.get(i);
				Map salesProgress = new HashMap();
				salesProgress.put("\"salesProgressId\"", db4.get("CODE_ID"));
				salesProgress.put("\"salesProgressName\"", db4.get("CODE_DESC"));
				salesProgressOP.add(salesProgress);
			}
			result.put("\"salesProgress\"", salesProgressOP);
			
			//获取购车预算下拉列表
			List<DynaBean> buyBudgetList = dao.getBuyBudgetInfo();
			List buyBudgetOP = new ArrayList();
			for(int i=0;i<buyBudgetList.size();i++) {
				DynaBean db5 = buyBudgetList.get(i);
				Map buyBudget = new HashMap();
				buyBudget.put("\"buyBudgetId\"", db5.get("CODE_ID"));
				buyBudget.put("\"buyBudgetName\"", db5.get("CODE_DESC"));
				buyBudgetOP.add(buyBudget);
			}
			result.put("\"buyBudget\"", buyBudgetOP);
			
			//获取客户类型下拉列表
			List<DynaBean> customerTypeList = dao.getCustomerTypeInfo();
			List customerTypeOP = new ArrayList();
			for(int i=0;i<customerTypeList.size();i++) {
				DynaBean db6 = customerTypeList.get(i);
				Map customerType = new HashMap();
				customerType.put("\"customerTypeId\"", db6.get("CODE_ID"));
				customerType.put("\"customerTypeName\"", db6.get("CODE_DESC"));
				customerTypeOP.add(customerType);
			}
			result.put("\"customerType\"", customerTypeOP);
			
			//获取跟进方式下拉列表
			List<DynaBean> followTypeList = dao.getFollowTypeInfo();
			List followTypeOP = new ArrayList();
			for(int i=0;i<followTypeList.size();i++) {
				DynaBean db7 = followTypeList.get(i);
				Map followType = new HashMap();
				followType.put("\"followTypeId\"", db7.get("CODE_ID"));
				followType.put("\"followTypeName\"", db7.get("CODE_DESC"));
				followTypeOP.add(followType);
			}
			result.put("\"followType\"", followTypeOP);
			
			//获取邀约方式下拉列表
			List<DynaBean> inviteTypeList = dao.getInviteTypeInfo();
			List inviteTypeOP = new ArrayList();
			for(int i=0;i<inviteTypeList.size();i++) {
				DynaBean db8 = inviteTypeList.get(i);
				Map inviteTypeNew = new HashMap();
				inviteTypeNew.put("\"inviteTypeId\"", db8.get("CODE_ID"));
				inviteTypeNew.put("\"inviteTypeName\"", db8.get("CODE_DESC"));
				inviteTypeOP.add(inviteTypeNew);
			}
			result.put("\"inviteType\"", inviteTypeOP);
			
			//获取战败原因下拉列表
			List<DynaBean> defeatReasonList = dao.getDefeatReasonInfo();
			List defeatReasonOP = new ArrayList();
			for(int i=0;i<defeatReasonList.size();i++) {
				DynaBean db9 = defeatReasonList.get(i);
				Map defeatReason = new HashMap();
				defeatReason.put("\"defeatReasonId\"", db9.get("CODE_ID"));
				defeatReason.put("\"defeatReasonName\"", db9.get("CODE_DESC"));
				if(db9.get("UP_CODE_ID")==null||"".equals(db9.get("UP_CODE_ID"))) {
					defeatReason.put("\"upCodeId\"", "");
				} else {
					defeatReason.put("\"upCodeId\"", db9.get("UP_CODE_ID"));
				}
				defeatReasonOP.add(defeatReason);
			}
			result.put("\"defeatReason\"", defeatReasonOP);
			
			//获取战败车型下拉列表
			List<DynaBean> defeatVehicleList = dao.getDefeatVehicleInfo();
			List defeatVehicleOP = new ArrayList();
			for(int i=0;i<defeatVehicleList.size();i++) {
				DynaBean db10 = defeatVehicleList.get(i);
				Map defeatVehicle = new HashMap();
				defeatVehicle.put("\"seriesId\"", db10.get("SERIES_ID"));
				defeatVehicle.put("\"seriesName\"", db10.get("SERIES_NAME"));
				if(db10.get("UP_SERIES_ID")==null||"".equals(db10.get("UP_SERIES_ID"))) {
					defeatVehicle.put("\"upSeriesId\"", "");
				} else {
					defeatVehicle.put("\"upSeriesId\"", db10.get("UP_SERIES_ID"));
				}
				defeatVehicleOP.add(defeatVehicle);
			}
			result.put("\"defeatVehicle\"", defeatVehicleOP);
			
			//获取邀约类型下拉列表
			List<DynaBean> inviteWayList = dao.getInviteWayInfo();
			List inviteWayOP = new ArrayList();
			for(int i=0;i<inviteWayList.size();i++) {
				DynaBean db9 = inviteWayList.get(i);
				Map inviteWayNew = new HashMap();
				inviteWayNew.put("\"inviteWayId\"", db9.get("CODE_ID"));
				inviteWayNew.put("\"inviteWayName\"", db9.get("CODE_DESC"));
				inviteWayOP.add(inviteWayNew);
			}
			result.put("\"inviteWay\"", inviteWayOP);
			
			result.put("\"adviserFlag\"", adviserFlag);
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", result);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 邀约处理数据提交(电话邀约)
	 * @throws UnsupportedEncodingException 
	 */
	public void doInvite() throws UnsupportedEncodingException {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String customerId = act.getRequest().getParamValue("customerId");
		String inviteId = act.getRequest().getParamValue("inviteId");
		String userId = act.getRequest().getParamValue("userId");
		String dealerId = act.getRequest().getParamValue("dealerId");
		String inviteType = act.getRequest().getParamValue("inviteType");
		String inviteWay = act.getRequest().getParamValue("inviteWay");
		String intentType = act.getRequest().getParamValue("intentType");
		String salesProgress = act.getRequest().getParamValue("salesProgress");
		String intentVehicle = act.getRequest().getParamValue("intentVehicle");
		String inviteShopDate = act.getRequest().getParamValue("inviteShopDate");
		String inviteInfo = act.getRequest().getParamValue("inviteInfo");
//		if(inviteInfo!=null&&!"".equals(inviteInfo)){
//			inviteInfo = new String(inviteInfo.getBytes("iso-8859-1"),"gbk");
//		}
		String ifInviteSuccess = act.getRequest().getParamValue("ifInviteSuccess");
		String nextTaskType = act.getRequest().getParamValue("nextTaskType");
		String nextFollowDate = act.getRequest().getParamValue("nextFollowDate");
		String followType = act.getRequest().getParamValue("followType");
		String followPlan = act.getRequest().getParamValue("followPlan");
//		if(followPlan!=null&&!"".equals(followPlan)){
//			followPlan = new String(followPlan.getBytes("iso-8859-1"),"gbk");
//		}
		String planInviteDate = act.getRequest().getParamValue("planInviteDate");
		String planMeetDate = act.getRequest().getParamValue("planMeetDate");
		String inviteTypeNew = act.getRequest().getParamValue("inviteTypeNew");
		String orderDate = act.getRequest().getParamValue("orderDate");
		String defeatVehicle = act.getRequest().getParamValue("defeatVehicle");
		String defeatReason = act.getRequest().getParamValue("defeatReason");
		String defeatDate = act.getRequest().getParamValue("defeatDate");
		String defeatRemark = act.getRequest().getParamValue("defeatRemark");
//		if(defeatRemark!=null&&!"".equals(defeatRemark)){
//			defeatRemark = new String(defeatRemark.getBytes("iso-8859-1"),"gbk");
//		}
		String failureDate = act.getRequest().getParamValue("failureDate");
		String failureRemark = act.getRequest().getParamValue("failureRemark");
//		if(failureRemark!=null&&!"".equals(failureRemark)){
//			failureRemark = new String(failureRemark.getBytes("iso-8859-1"),"gbk");
//		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Map strResult = new HashMap();
		try {
			//PC和移动端同时处理问题
			int ifHandle = CommonUtils.getTheSameData2(inviteId);
			if(ifHandle==10041002) {//未处理的情况
				// 判断是否可以做订单
				//根据CustomerId获取电话号码
				TPcCustomerPO cuspo = new TPcCustomerPO();
				cuspo.setCustomerId(Long.parseLong(customerId));
				TPcCustomerPO cuspo2 = (TPcCustomerPO)dao.select(cuspo).get(0);
				String telephone = cuspo2.getTelephone();
				Map<String,String>map=new HashMap<String,String>();
				map.put("telephone", telephone);
				map.put("dealerId", dealerId);
				if(!CommonUtils.judgeIfOrder(map) && "order".equals(nextTaskType)) { //不可以做
					strResult.put("\"success\"", "false");
					strResult.put("\"msg\"", "该客户无首次到店客流信息，无法生成订车计划!");
					act.setOutData("\"strResult\"", strResult);
				} else {
					//完成本次电话邀约任务
					String newTaskId = SequenceManager.getSequence("");
					//标记本次计划邀约任务完成
					TPcInvitePO oldInvitePo = new TPcInvitePO();
					oldInvitePo.setInviteId(Long.parseLong(inviteId));
					TPcInvitePO newInvitePo = new TPcInvitePO();
					newInvitePo.setInviteId(Long.parseLong(inviteId));
					newInvitePo.setRemark(inviteInfo);
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
					} else if(nextTaskType=="follow"||"follow".equals(nextTaskType)){//跟进
						newCustomerPo.setNextFollowTime(sdf.parse(nextFollowDate));//下次跟进时间
					} else if(nextTaskType=="invite"||"invite".equals(nextTaskType)) {//邀约
						newCustomerPo.setNextFollowTime(sdf.parse(planInviteDate));//下次跟进时间
					}
					newCustomerPo.setDefeatRemark("手机端ifInviteSuccess:"+ifInviteSuccess+" nextTaskType:"+nextTaskType); 
					
					dao.update(oldCustomerPo,newCustomerPo);
					
					//增加接触点信息
					CommonUtils.addContackPoint(Constant.POINT_WAY_05, inviteInfo, customerId, userId, dealerId);
					
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(inviteId,Constant.REMIND_TYPE_06.toString());
					//修复接下来没有任务的问题
					if( (ifInviteSuccess==null || "".equals(ifInviteSuccess)) && (nextTaskType==null || "".equals(nextTaskType)) ) {
						//新增下次邀约到店任务表
						TPcInviteShopPO inviteShopPo = new TPcInviteShopPO();
						inviteShopPo.setInviteShopId(Long.parseLong(newTaskId));
						inviteShopPo.setInviteId(Long.parseLong(inviteId));
						inviteShopPo.setCustomerId(Long.parseLong(customerId));
						inviteShopPo.setCreateDate(new Date());
						inviteShopPo.setCreateBy(userId);
						inviteShopPo.setOldLevel(intentType);
						inviteShopPo.setInviteShopDate(sdf.parse(inviteShopDate));
						inviteShopPo.setTaskStatus(Constant.TASK_STATUS_01);
						inviteShopPo.setPreviousTask(Long.parseLong(inviteId));
						inviteShopPo.setOldSalesProgress(salesProgress);
						inviteShopPo.setDataFrom("60601002");
						dao.insert(inviteShopPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_07.toString(), newTaskId, customerId, dealerId, userId, inviteShopDate,"");
						
					}
					
					if(ifInviteSuccess=="10041001"||"10041001".equals(ifInviteSuccess)) {//选择邀约成功后保存
						//新增下次邀约到店任务表
						TPcInviteShopPO inviteShopPo = new TPcInviteShopPO();
						inviteShopPo.setInviteShopId(Long.parseLong(newTaskId));
						inviteShopPo.setInviteId(Long.parseLong(inviteId));
						inviteShopPo.setCustomerId(Long.parseLong(customerId));
						inviteShopPo.setCreateDate(new Date());
						inviteShopPo.setCreateBy(userId);
						inviteShopPo.setOldLevel(intentType);
						inviteShopPo.setInviteShopDate(sdf.parse(inviteShopDate));
						inviteShopPo.setTaskStatus(Constant.TASK_STATUS_01);
						inviteShopPo.setPreviousTask(Long.parseLong(inviteId));
						inviteShopPo.setOldSalesProgress(salesProgress);
						inviteShopPo.setDataFrom("60601002");
						dao.insert(inviteShopPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_07.toString(), newTaskId, customerId, dealerId, userId, inviteShopDate,"");
						
					} else if(nextTaskType=="follow"||"follow".equals(nextTaskType)) { //点击跟进按钮后保存
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
						followPo.setCreateBy(userId);
						followPo.setPreviousTask(Long.parseLong(inviteId));
						followPo.setOldSalesProgress(salesProgress);
						followPo.setDataFrom("60601002");
						dao.insert(followPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), newTaskId, customerId, dealerId, userId, nextFollowDate,"");
						
					} else if(nextTaskType=="invite"||"invite".equals(nextTaskType)) {//点击邀约按钮后保存
						//新增下次计划邀约任务
						TPcInvitePO invitePo = new TPcInvitePO();
						invitePo.setInviteId(Long.parseLong(newTaskId));
						invitePo.setCustomerId(Long.parseLong(customerId));
						invitePo.setCreateDate(new Date());
						invitePo.setCreateBy(userId);
						invitePo.setPlanInviteDate(sdf.parse(planInviteDate));
						invitePo.setPlanMeetDate(sdf.parse(planMeetDate));
						invitePo.setTaskStatus(Constant.TASK_STATUS_01);
						invitePo.setInviteType(Integer.parseInt(inviteTypeNew));
						invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_04);
						invitePo.setIfPlan(Constant.IF_TYPE_NO);
						invitePo.setOldLevel(intentType);
						invitePo.setNewLevel(null);
						invitePo.setPreviousTask(Long.parseLong(inviteId));
						invitePo.setOldSalesProgress(salesProgress);
						invitePo.setDataFrom("60601002");
						dao.insert(invitePo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), newTaskId, customerId, dealerId, userId, planInviteDate,"");
						
					} else if(nextTaskType=="order"||"order".equals(nextTaskType)) {//点击订单后保存
						//新增下次订单任务
						TPcOrderPO orderPo = new TPcOrderPO();
						orderPo.setOrderId(Long.parseLong(newTaskId));
						orderPo.setCustomerId(Long.parseLong(customerId));
						orderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
						orderPo.setOldLevel(intentType);
						orderPo.setCreateDate(new Date());
						orderPo.setCreateBy(userId);
						orderPo.setOldSalesProgress(salesProgress);
						orderPo.setPreviousTask(Long.parseLong(inviteId));
						orderPo.setTaskStatus(Constant.TASK_STATUS_01);
						orderPo.setOrderDate(sdf.parse(orderDate));
						orderPo.setDataFrom("60601002");
						dao.insert(orderPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_08.toString(), newTaskId, customerId, dealerId, userId, orderDate,"");
						
					} else if(nextTaskType=="defeat"||"defeat".equals(nextTaskType)) {//点击战败后保存
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
						defeatPo.setDefeatEndDate(sdf.parse(defeatDate));
						defeatPo.setStatus(Constant.STATUS_ENABLE);
						defeatPo.setOldLevel(ctmRank);
						defeatPo.setNewLevel(Constant.INTENT_TYPE_E.toString());
						defeatPo.setOldSalesProgress(sProgress);
						defeatPo.setSalesProgress(Constant.SALSE_PROGRESS_01.toString());
						defeatPo.setReasonAnalysis(defeatRemark);
						defeatPo.setCreateBy(userId);
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(inviteId));
						defeatPo.setDefeatType(defeatReason);
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						defeatPo.setDataFrom("60601002");
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为战败
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_E.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_10.toString(), newTaskId, customerId, dealerId, "", sdf.format(new Date()),"");
						
					} else if(nextTaskType=="failure"||"failure".equals(nextTaskType)) {//点击失效后保存
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
						defeatPo.setCreateBy(userId);
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(inviteId));
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						defeatPo.setFailureDate(sdf.parse(failureDate));
						defeatPo.setDataFrom("60601002");
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为失效
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_L.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_11.toString(), newTaskId, customerId, dealerId, "", sdf.format(new Date()),"");
						
					}
					strResult.put("\"success\"", "true");
					strResult.put("\"msg\"", "操作成功");
					
					act.setOutData("\"strResult\"", strResult);
				}
			} else {
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(inviteId,Constant.REMIND_TYPE_06.toString());
				strResult.put("\"success\"", "false");
				strResult.put("\"msg\"", "该条数据已被执行过！");
				
				act.setOutData("\"strResult\"", strResult);
			}
			
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 邀约处理数据提交(邀约到店)
	 * @throws UnsupportedEncodingException 
	 */
	public void doInviteShop() throws UnsupportedEncodingException {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String customerId = act.getRequest().getParamValue("customerId");
		String inviteId = act.getRequest().getParamValue("inviteId");
		String inviteShopId = act.getRequest().getParamValue("inviteShopId");
		String userId = act.getRequest().getParamValue("userId");
		String dealerId = act.getRequest().getParamValue("dealerId");
		String inviteShopDate = act.getRequest().getParamValue("inviteShopDate");
		String ifInvite = act.getRequest().getParamValue("ifInvite");
		String intentType = act.getRequest().getParamValue("intentType");
		String salesProgress = act.getRequest().getParamValue("salesProgress");
		String intentVehicle = act.getRequest().getParamValue("intentVehicle");
		String inviteShopInfo = act.getRequest().getParamValue("inviteShopInfo");
//		if(inviteShopInfo!=null&&!"".equals(inviteShopInfo)){
//			inviteShopInfo = new String(inviteShopInfo.getBytes("iso-8859-1"),"gbk");
//		}
		String nextTaskType = act.getRequest().getParamValue("nextTaskType");
		String nextFollowDate = act.getRequest().getParamValue("nextFollowDate");
		String followType = act.getRequest().getParamValue("followType");
		String followPlan = act.getRequest().getParamValue("followPlan");
//		if(followPlan!=null&&!"".equals(followPlan)){
//			followPlan = new String(followPlan.getBytes("iso-8859-1"),"gbk");
//		}
		String planInviteDate = act.getRequest().getParamValue("planInviteDate");
		String planMeetDate = act.getRequest().getParamValue("planMeetDate");
		String inviteTypeNew = act.getRequest().getParamValue("inviteTypeNew");
		String orderDate = act.getRequest().getParamValue("orderDate");
		String defeatVehicle = act.getRequest().getParamValue("defeatVehicle");
		String defeatReason = act.getRequest().getParamValue("defeatReason");
		String defeatDate = act.getRequest().getParamValue("defeatDate");
		String defeatRemark = act.getRequest().getParamValue("defeatRemark");
//		if(defeatRemark!=null&&!"".equals(defeatRemark)){
//			defeatRemark = new String(defeatRemark.getBytes("iso-8859-1"),"gbk");
//		}
		String failureDate = act.getRequest().getParamValue("failureDate");
		String failureRemark = act.getRequest().getParamValue("failureRemark");
//		if(failureRemark!=null&&!"".equals(failureRemark)){
//			failureRemark = new String(failureRemark.getBytes("iso-8859-1"),"gbk");
//		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Map strResult = new HashMap();
		try {
			//PC和移动端同时处理问题
			int ifHandle = CommonUtils.getTheSameData3(inviteShopId);
			if(ifHandle==10041002) {//未处理的情况
				// 判断是否可以做订单
				//根据CustomerId获取电话号码
				TPcCustomerPO cuspo = new TPcCustomerPO();
				cuspo.setCustomerId(Long.parseLong(customerId));
				TPcCustomerPO cuspo2 = (TPcCustomerPO)dao.select(cuspo).get(0);
				String telephone = cuspo2.getTelephone();
				Map<String,String>map=new HashMap<String,String>();
				map.put("telephone", telephone);
				map.put("dealerId", dealerId);
				if(!CommonUtils.judgeIfOrder(map) && "order".equals(nextTaskType)) { //不可以做
					strResult.put("\"success\"", "false");
					strResult.put("\"msg\"", "该客户无首次到店客流信息，无法生成订车计划!");
					act.setOutData("\"strResult\"", strResult);
				} else {
					//完成本次邀约到店任务
					String newTaskId = SequenceManager.getSequence("");
					//标记本次邀约到店任务完成
					TPcInviteShopPO oldInviteShopPo = new TPcInviteShopPO();
					oldInviteShopPo.setInviteShopId(Long.parseLong(inviteShopId));
					TPcInviteShopPO newInviteShopPo = new TPcInviteShopPO();
					newInviteShopPo.setInviteShopId(Long.parseLong(inviteShopId));
					newInviteShopPo.setRemark(inviteShopInfo);
					newInviteShopPo.setNewLevel(intentType);
					newInviteShopPo.setTaskStatus(Constant.TASK_STATUS_02);
					newInviteShopPo.setFinishDate(new Date());
					newInviteShopPo.setNextTask(Long.parseLong(newTaskId));
					newInviteShopPo.setSalesProgress(salesProgress);
					if(ifInvite=="10041001"||"10041001".equals(ifInvite)){//是到店
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
					if(nextTaskType=="followRadio"||"followRadio".equals(nextTaskType)){//跟进
						newCustomerPo.setNextFollowTime(sdf.parse(nextFollowDate));//下次跟进时间
					} else if(nextTaskType=="inviteRadio"||"inviteRadio".equals(nextTaskType)) {//邀约
						newCustomerPo.setNextFollowTime(sdf.parse(planInviteDate));//下次跟进时间
					} 
					dao.update(oldCustomerPo,newCustomerPo);
					
					//增加接触点信息
					CommonUtils.addContackPoint(Constant.POINT_WAY_06, inviteShopInfo, customerId, userId, dealerId);
					
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(inviteShopId,Constant.REMIND_TYPE_07.toString());
					
					if(nextTaskType=="follow"||"follow".equals(nextTaskType)) { //点击跟进按钮后保存
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
						followPo.setCreateBy(userId);
						followPo.setPreviousTask(Long.parseLong(inviteShopId));
						followPo.setOldSalesProgress(salesProgress);
						followPo.setDataFrom("60601002");
						dao.insert(followPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), newTaskId, customerId, dealerId, userId, nextFollowDate,"");
						
					} else if(nextTaskType=="invite"||"invite".equals(nextTaskType)) {//点击邀约按钮后保存
						//新增下次计划邀约任务
						TPcInvitePO invitePo = new TPcInvitePO();
						invitePo.setInviteId(Long.parseLong(newTaskId));
						invitePo.setCustomerId(Long.parseLong(customerId));
						invitePo.setCreateDate(new Date());
						invitePo.setCreateBy(userId);
						invitePo.setPlanInviteDate(sdf.parse(planInviteDate));
						invitePo.setPlanMeetDate(sdf.parse(planMeetDate));
						invitePo.setTaskStatus(Constant.TASK_STATUS_01);
						invitePo.setInviteType(Integer.parseInt(inviteTypeNew));
						invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_04);
						invitePo.setIfPlan(Constant.IF_TYPE_NO);
						invitePo.setOldLevel(intentType);
						invitePo.setNewLevel(null);
						invitePo.setPreviousTask(Long.parseLong(inviteShopId));
						invitePo.setOldSalesProgress(salesProgress);
						invitePo.setDataFrom("60601002");
						dao.insert(invitePo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), newTaskId, customerId, dealerId, userId, planInviteDate,"");
						
					} else if(nextTaskType=="order"||"order".equals(nextTaskType)) {//点击订单后保存
						//新增下次订单任务
						TPcOrderPO orderPo = new TPcOrderPO();
						orderPo.setOrderId(Long.parseLong(newTaskId));
						orderPo.setCustomerId(Long.parseLong(customerId));
						orderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
						orderPo.setOldLevel(intentType);
						orderPo.setCreateDate(new Date());
						orderPo.setCreateBy(userId);
						orderPo.setOldSalesProgress(salesProgress);
						orderPo.setPreviousTask(Long.parseLong(inviteShopId));
						orderPo.setTaskStatus(Constant.TASK_STATUS_01);
						orderPo.setOrderDate(sdf.parse(orderDate));
						orderPo.setDataFrom("60601002");
						dao.insert(orderPo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_08.toString(), newTaskId, customerId, dealerId, userId, orderDate,"");
						
					} else if(nextTaskType=="defeat"||"defeat".equals(nextTaskType)) {//点击战败后保存
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
						defeatPo.setDefeatEndDate(sdf.parse(defeatDate));
						defeatPo.setStatus(Constant.STATUS_ENABLE);
						defeatPo.setOldLevel(ctmRank);
						defeatPo.setNewLevel(Constant.INTENT_TYPE_E.toString());
						defeatPo.setOldSalesProgress(sProgress);
						defeatPo.setSalesProgress(Constant.SALSE_PROGRESS_01.toString());
						defeatPo.setReasonAnalysis(defeatRemark);
						defeatPo.setCreateBy(userId);
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(inviteShopId));
						defeatPo.setDefeatType(defeatReason);
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						defeatPo.setDataFrom("60601002");
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为战败
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_E.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_10.toString(), newTaskId, customerId, dealerId, "", sdf.format(new Date()),"");
						
					} else if(nextTaskType=="failure"||"failure".equals(nextTaskType)) {//点击失效后保存
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
						defeatPo.setCreateBy(userId);
						defeatPo.setCreateDate(new Date());
						defeatPo.setPreviousTask(Long.parseLong(inviteShopId));
						defeatPo.setAuditStatus(Constant.FAILURE_AUDIT_01.longValue());
						defeatPo.setFailureDate(sdf.parse(failureDate));
						defeatPo.setDataFrom("60601002");
						dao.insert(defeatPo);
						
						//修改客户表内客户等级为失效
						TPcCustomerPO oldCustomerPo2 = new TPcCustomerPO();
						oldCustomerPo2.setCustomerId(Long.parseLong(customerId));
						TPcCustomerPO newCustomerPo2 = new TPcCustomerPO();
						newCustomerPo2.setCustomerId(Long.parseLong(customerId));
						newCustomerPo2.setCtmRank(Constant.INTENT_TYPE_L.toString());
						dao.update(oldCustomerPo2, newCustomerPo2);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_11.toString(), newTaskId, customerId, dealerId, "", sdf.format(new Date()),"");
						
					}
					strResult.put("\"success\"", "true");
					strResult.put("\"msg\"", "操作成功");
					
					act.setOutData("\"strResult\"", strResult);
				}
			} else { //已处理的情况
				strResult.put("\"success\"", "false");
				strResult.put("\"msg\"", "该条数据已被执行！");
				
				act.setOutData("\"strResult\"", strResult);
			}
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 待处理提醒列表数据
	 */
	public void getRemindInfo() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = "";
		String userId2 = act.getRequest().getParamValue("userId");
		String dealerId = act.getRequest().getParamValue("dealerId");
		String startDate = act.getRequest().getParamValue("startDate");
		String endDate = act.getRequest().getParamValue("endDate");
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(userId2)) {
			userId = userId2;
		}
		
		Map strResult = new HashMap();
		List a = new ArrayList();
		try {
			//获取提醒信息
			List<DynaBean> remindList = dao.getRemindInfo(userId,dealerId);
			for(int i=0;i<remindList.size();i++) {
				DynaBean db = remindList.get(i);
				Map result = new HashMap();
				if(db.get("CUSTOMER_ID")==null||"".equals(db.get("CUSTOMER_ID"))) {
					result.put("\"customerId\"", "");
				} else {
					result.put("\"customerId\"", db.get("CUSTOMER_ID"));
				}
				if(db.get("CUSTOMER_NAME")==null||"".equals(db.get("CUSTOMER_NAME"))) {
					result.put("\"customerName\"", "");
				} else {
					result.put("\"customerName\"", db.get("CUSTOMER_NAME"));
				}
				if(db.get("A.REMIND_NUM")==null||"".equals(db.get("A.REMIND_NUM"))) {
					result.put("\"remindNum\"", "");
				} else {
					result.put("\"remindNum\"", db.get("A.REMIND_NUM"));
				}
				result.put("\"remindType\"", db.get("REMIND_TYPE"));
				result.put("\"remindTypeName\"", db.get("REMIND_TYPE_NAME"));
				result.put("\"remindDate\"", db.get("REMIND_DATE"));
				a.add(result);
			}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", a);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 顾问录入线索页面
	 */
	public void adviserLeadsInit() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = act.getRequest().getParamValue("userId");
		
		Map strResult = new HashMap();
		Map result = new HashMap();
		try {
			//获取线索来源下拉列表
			List leadsOriginOP = new ArrayList();
			Map leadsOrigin = new HashMap();
			leadsOrigin.put("\"leadsOriginId\"", "60151007");
			leadsOrigin.put("\"leadsOriginName\"", "车展/巡展/路演");
			leadsOriginOP.add(leadsOrigin);
			Map leadsOrigin1 = new HashMap();
			leadsOrigin1.put("\"leadsOriginId\"", "60151019");
			leadsOrigin1.put("\"leadsOriginName\"", "商圈定展");
			leadsOriginOP.add(leadsOrigin1);
			Map leadsOrigin2 = new HashMap();
			leadsOrigin2.put("\"leadsOriginId\"", "60151008");
			leadsOrigin2.put("\"leadsOriginName\"", "品牌体验活动（上市活动、试乘试驾、大篷车等）");
			leadsOriginOP.add(leadsOrigin2);
			Map leadsOrigin3 = new HashMap();
			leadsOrigin3.put("\"leadsOriginId\"", "60151006");
			leadsOrigin3.put("\"leadsOriginName\"", "网络媒体");
			leadsOriginOP.add(leadsOrigin3);
			Map leadsOrigin4 = new HashMap();
			leadsOrigin4.put("\"leadsOriginId\"", "60151020");
			leadsOrigin4.put("\"leadsOriginName\"", "新媒体（移动端APP、移动网站等）");
			leadsOriginOP.add(leadsOrigin4);
			Map leadsOrigin5 = new HashMap();
			leadsOrigin5.put("\"leadsOriginId\"", "60151021");
			leadsOrigin5.put("\"leadsOriginName\"", "社会化媒体（微博、微信、论坛等）");
			leadsOriginOP.add(leadsOrigin5);
			Map leadsOrigin6 = new HashMap();
			leadsOrigin6.put("\"leadsOriginId\"", "60151016");
			leadsOrigin6.put("\"leadsOriginName\"", "亲朋/老客户介绍及其他");
			leadsOriginOP.add(leadsOrigin6);
			Map leadsOrigin7 = new HashMap();
			leadsOrigin7.put("\"leadsOriginId\"", "60151004");
			leadsOrigin7.put("\"leadsOriginName\"", "官网");
			leadsOriginOP.add(leadsOrigin7);
			Map leadsOrigin8 = new HashMap();
			leadsOrigin8.put("\"leadsOriginId\"", "60151003");
			leadsOrigin8.put("\"leadsOriginName\"", "客户中心");
			leadsOriginOP.add(leadsOrigin8);
			Map leadsOrigin9 = new HashMap();
			leadsOrigin9.put("\"leadsOriginId\"", "60151017");
			leadsOrigin9.put("\"leadsOriginName\"", "汽车之家");
			leadsOriginOP.add(leadsOrigin9);
			Map leadsOrigin10 = new HashMap();
			leadsOrigin10.put("\"leadsOriginId\"", "60151018");
			leadsOrigin10.put("\"leadsOriginName\"", "易车网");
			leadsOriginOP.add(leadsOrigin10);
			result.put("\"leadsOrigin\"", leadsOriginOP);
			
			//获取意向车型下拉列表
			List<DynaBean> intentVehicleList = dao.getIntentVehicleInfo();
			List intentVehicleOP = new ArrayList();
			for(int i=0;i<intentVehicleList.size();i++) {
				DynaBean db3 = intentVehicleList.get(i);
				Map intentVehicle = new HashMap();
				intentVehicle.put("\"seriesId\"", db3.get("SERIES_ID"));
				intentVehicle.put("\"seriesName\"", db3.get("SERIES_NAME"));
				if(db3.get("UP_SERIES_ID")==null||"".equals(db3.get("UP_SERIES_ID"))) {
					intentVehicle.put("\"upSeriesId\"", "");
				} else {
					intentVehicle.put("\"upSeriesId\"", db3.get("UP_SERIES_ID"));
				}
				intentVehicleOP.add(intentVehicle);
			}
			result.put("\"intentVehicle\"", intentVehicleOP);
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", result);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 顾问录入线索提交
	 * @throws UnsupportedEncodingException 
	 */
	public void adviserLeads() throws UnsupportedEncodingException {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = act.getRequest().getParamValue("userId");
		String customerName = act.getRequest().getParamValue("customerName");
//		if(customerName!=null&&!"".equals(customerName)){
//			customerName = new String(customerName.getBytes("iso-8859-1"),"UTF-8");
//		}
		String dealerId = act.getRequest().getParamValue("dealerId");
		String telephone = act.getRequest().getParamValue("telephone");
		String sex = act.getRequest().getParamValue("sex");
		String leadsOrigin = act.getRequest().getParamValue("leadsOrigin");
		String intentVehicle = act.getRequest().getParamValue("intentVehicle");
		String describe = act.getRequest().getParamValue("describe");
//		if(describe!=null&&!"".equals(describe)){
//			describe = new String(describe.getBytes("iso-8859-1"),"UTF-8");
//		}
		
		Map strResult = new HashMap();
		try {
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
			
			//保存新增的DCRC录入
			//新增到销售线索表
			TPcLeadsPO tlkeyPo = new TPcLeadsPO();
			//获取销售线索主键
			Long LeadsPK = dao.getLongPK(tlkeyPo);
			TPcLeadsPO tlPo = new TPcLeadsPO();
			tlPo.setLeadsCode(LeadsPK);
			tlPo.setCustomerName(customerName);
			tlPo.setTelephone(telephone);
			tlPo.setLeadsType(Constant.LEADS_TYPE_03.toString());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			tlPo.setLeadsOrigin(leadsOrigin);
			tlPo.setCollectDate(new Date());
			tlPo.setCreateDate(new Date());
			tlPo.setSex(sex);
			tlPo.setLeadsStatus(new Long(Constant.LEADS_STATUS_01));
			tlPo.setCreateBy(userId);
			tlPo.setCustomerDescribe(describe);
			tlPo.setIntentVehicle(intentVehicle);
			tlPo.setDataFrom("60601002");
//			tlPo.setComeMeet(Long.parseLong(comeMeet));
			dao.insert(tlPo);
			
			//新增到销售线索分派表
			TPcLeadsAllotPO tplkeyPo = new TPcLeadsAllotPO();
			//获取销售线索分派主键
			Long LeadsAllotPK = dao.getLongPK(tplkeyPo);
			TPcLeadsAllotPO tplPo = new TPcLeadsAllotPO();
			tplPo.setLeadsAllotId(LeadsAllotPK);
			tplPo.setLeadsCode(LeadsPK);
			tplPo.setDealerId(dealerId);
			tplPo.setAllotDealerDate(new Date());
			tplPo.setAdviser(userId);
			tplPo.setSex(sex);
			tplPo.setAllotAdviserDate(new Date());
			tplPo.setAllotAgain(Constant.IF_TYPE_NO);
			tplPo.setStatus(Constant.STATUS_ENABLE);
			dao.insert(tplPo);
			
			//新增提醒信息
			CommonUtils.addRemindInfo(Constant.REMIND_TYPE_04.toString(), LeadsAllotPK.toString(), "", dealerId, userId, remindDate,"");
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 经理待分派线索列表
	 */
	public void doQueryLeadsAllot() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String dealerId = act.getRequest().getParamValue("dealerId");
		
		Map strResult = new HashMap();
		List a = new ArrayList();
		try {
			//获取经理待分派线索列表
			List<DynaBean> leadsAllotList = dao.getLeadsAllotInfo(dealerId);
			for(int i=0;i<leadsAllotList.size();i++) {
				DynaBean db = leadsAllotList.get(i);
				Map result = new HashMap();
				result.put("\"leadsId\"", db.get("LEADS_CODE"));
				result.put("\"leadsAllotId\"", db.get("LEADS_ALLOT_ID"));
				result.put("\"leadsOrigin\"", db.get("LEADS_ORIGIN"));
				result.put("\"createDate\"", db.get("CREATE_DATE"));
				if(db.get("CUSTOMER_NAME")==null||"".equals(db.get("CUSTOMER_NAME"))) {
					result.put("\"customerName\"", "");
				} else {
					result.put("\"customerName\"", db.get("CUSTOMER_NAME"));
				}
				result.put("\"telephone\"", db.get("TELEPHONE"));
				if(db.get("SERIES_NAME")==null||"".equals(db.get("SERIES_NAME"))) {
					result.put("\"intentVehicle\"", "");
				} else {
					result.put("\"intentVehicle\"", db.get("SERIES_NAME"));
				}
				a.add(result);
			}
			
			//获取顾问下拉列表
			List<DynaBean> adviserList = dao.getAdviserList(dealerId);
			List adviserOP = new ArrayList();
			for(int i=0;i<adviserList.size();i++) {
				DynaBean db = adviserList.get(i);
				Map adviser = new HashMap();
				adviser.put("\"adviserId\"", db.get("USER_ID"));
				adviser.put("\"adviserName\"", db.get("NAME"));
				adviserOP.add(adviser);
			}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", a);
			strResult.put("\"adviser\"", adviserOP);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 经理分派线索提交
	 */
	public void doLeadsAllot() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		CrmPhoneDao dao2 = new CrmPhoneDao();
		String userId = act.getRequest().getParamValue("userId");
		String adviserId = act.getRequest().getParamValue("adviserId");
		String leadsCode = act.getRequest().getParamValue("leadsId");
		String leadsAllotId = act.getRequest().getParamValue("leadsAllotId");
		String dealerId = act.getRequest().getParamValue("dealerId");
		
		Map strResult = new HashMap();
		try {
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
			
			//获取销售线索主表信息
			TPcLeadsPO leadsPo = new TPcLeadsPO();
			leadsPo.setLeadsCode(Long.parseLong(leadsCode));
			TPcLeadsPO leadsPo2 = (TPcLeadsPO)dao.select(leadsPo).get(0);
			//获取线索类别(60141001:车厂下发；60141002：DCRC录入)
			 String leadsType = leadsPo2.getLeadsType();
			 leadsType = leadsType.substring(0, 8);
			
			String remark="手机端经理分派线索方法doLeadsAllot";
			TPcLeadsPO leadsOldPoByDcrc = new TPcLeadsPO();
			leadsOldPoByDcrc.setLeadsCode(Long.parseLong(leadsCode));
			TPcLeadsPO leadsNewPoByDcrc = new TPcLeadsPO();
			leadsNewPoByDcrc.setFailureRemark(remark);
			dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
			// 开始执行分派到选择的顾问
			// 获取销售线索信息
			TPcLeadsAllotPO po = new TPcLeadsAllotPO();
			po.setLeadsAllotId(Long.parseLong(leadsAllotId));
			TPcLeadsAllotPO po2 = (TPcLeadsAllotPO)dao.select(po).get(0);
				//判断是否已建过档案（电话、姓名、经销商ID）
				List<Map<String, Object>> getHasList = null;
				getHasList=dao.getHasCustomer(po2.getTelephone(),po2.getCustomerName(),dealerId);
				//已存在档案，不能进行分派顾问
				if(getHasList.size()>0) {
					remark=remark+"-已建档";
					leadsNewPoByDcrc.setFailureRemark(remark);
					dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
					//获取已建档的分派顾问
					String adviser = getHasList.get(0).get("ADVISER").toString();
					String dealerId2 = getHasList.get(0).get("DEALER_ID").toString();
					String customerId = getHasList.get(0).get("CUSTOMER_ID").toString();
					String ctmType = getHasList.get(0).get("CTM_TYPE").toString();
					//如果已建档的分派顾问等于重新分派的顾问，并且如果客户类型不属于战败、失效、保有则设为无效并增加接触点信息，不等则更新线索中的顾问
					if(adviser.equals(adviserId)) {
						remark=remark+"-重复分派同一顾问";
						leadsNewPoByDcrc.setFailureRemark(remark);
						dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
						//修改销售线索主表状态为无效
						TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
						oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
						TPcLeadsPO newLeadsPo = new TPcLeadsPO();
//						newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
						if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
							newLeadsPo.setIfHandle(10041001);
							newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_05.longValue());
						} else {
							newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
						}
						dao.update(oldLeadsPo, newLeadsPo);
						//修改线索分派表状态为无效且已确认
						TPcLeadsAllotPO oldPo = new TPcLeadsAllotPO();
						oldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
						newPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
							newPo.setStatus(Constant.STATUS_DISABLE);//设置为无效
							newPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);
							newPo.setConfirmDate(new Date());
						} else {
							newPo.setStatus(Constant.STATUS_ENABLE);//设置为有效
							newPo.setIfConfirm(Constant.ADVISER_CONFIRM_01);
							newPo.setAdviser(adviserId);
							newPo.setAllotAdviserDate(new Date());
						}
						dao.update(oldPo, newPo);
						
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_01.toString());
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_02.toString());
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
						if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
							//增加接触点信息
							CommonUtils.addContackPoint(Constant.POINT_WAY_01, "公司类重复线索", customerId, adviser, dealerId2);
							String repeatLeads = SequenceManager.getSequence("");
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_20.toString(), repeatLeads, customerId, dealerId2, adviser, f1.format(new Date()),"");
							//标记线索为重复线索
							CommonUtils.updateIfRepeat(leadsAllotId);
						} else {
							if("60141001".equals(leadsType)) {//车厂线索
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), leadsAllotId, "", dealerId, adviserId, remindDate,"");
							} else {//DCRC线索
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_04.toString(), leadsAllotId, "", dealerId, adviserId, remindDate,"");
							}
						}
					} else {
						remark=remark+"-重复分派不同顾问";
						leadsNewPoByDcrc.setFailureRemark(remark);
						dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
						
						TPcLeadsAllotPO oldPo = new TPcLeadsAllotPO();
						TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
						oldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						newPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						newPo.setAdviser(adviserId);
						newPo.setAllotAdviserDate(new Date());
						dao.update(oldPo, newPo);
						//修改销售线索主表状态为有效
						TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
						oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
						TPcLeadsPO newLeadsPo = new TPcLeadsPO();
						newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
						newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
						dao.update(oldLeadsPo, newLeadsPo);
						
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_01.toString());
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_02.toString());
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
						
						if("60141001".equals(leadsType)) {//车厂线索
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), leadsAllotId, "", dealerId, adviserId, remindDate,"");
						} else {//DCRC线索
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_04.toString(), leadsAllotId, "", dealerId, adviserId, remindDate,"");
						}
					}
					
				} else {//未存在则新增分派到顾问（做修改，增加顾问字段信息）
					remark=remark+"-未建档";
					leadsNewPoByDcrc.setFailureRemark(remark);
					dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
					
					TPcLeadsAllotPO oldPo = new TPcLeadsAllotPO();
					TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
					oldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					newPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
					newPo.setAdviser(adviserId);
					newPo.setAllotAdviserDate(new Date());
					dao.update(oldPo, newPo);
					
					//修改销售线索主表状态为有效
					TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
					oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
					TPcLeadsPO newLeadsPo = new TPcLeadsPO();
					newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
					newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
					dao.update(oldLeadsPo, newLeadsPo);
					
					//标记提醒信息为已完成
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_01.toString());
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_02.toString());
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
					CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
					
					if("60141001".equals(leadsType)) {//车厂线索
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), leadsAllotId, "", dealerId, adviserId, remindDate,"");
					} 
//					else if(po2.getCustomerName()==null||"".equals(po2.getCustomerName())){//DCRC线索
						//新增提醒信息
//						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_02.toString(), leadsAllotId, "", logonUser.getDealerId(), adviserId, remindDate,"");
//					} 
					else {
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_04.toString(), leadsAllotId, "", dealerId, adviserId, remindDate,"");
					}
				}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 日程统计数据
	 */
	public void taskCount() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String dealerId = act.getRequest().getParamValue("dealerId");
		String userId = null;
		String startDate = act.getRequest().getParamValue("startDate");
		String endDate = act.getRequest().getParamValue("endDate");
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(act.getRequest().getParamValue("userId").toString())) {
			userId = act.getRequest().getParamValue("userId").toString();
		} else if(CommonUtils.judgeDirectorLogin(act.getRequest().getParamValue("userId").toString())){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(act.getRequest().getParamValue("userId").toString());
		}
		
		Map strResult = new HashMap();
		Map result = new HashMap();
		try {
			//获取销售线索信息(未确认，有效)
			List<DynaBean> leadsList = dao.getLeadsInfo(userId,dealerId,startDate,endDate);
			String leadsCountFact = leadsList.size()+"";
			//获取所有销售线索的数量
			List<DynaBean> leadsYesList = dao.getAllLeadsInfo(userId,dealerId,startDate,endDate);
			String leadsCount = leadsYesList.size()+"";
			result.put("\"leadsCount\"", leadsCount);
			result.put("\"leadsCountFact\"", leadsCountFact);
			
			//获取跟进任务表信息
			List<DynaBean> followList = dao.getFollowInfo(userId,dealerId,startDate,endDate);
			String followCountFact = followList.size()+"";
			//获取所有跟进的数量
			List<DynaBean> followYesList = dao.getAllFollowInfo(userId,dealerId,startDate,endDate);
			String followCount = followYesList.size()+"";
			result.put("\"followCount\"", followCount);
			result.put("\"followCountFact\"", followCountFact);
			
			//获取邀约任务表信息
			List<DynaBean> inviteList = dao.getInviteInfo(userId,dealerId,startDate,endDate);
			String inviteCountFact = inviteList.size()+"";
			//获取所有邀约的数量
			List<DynaBean> inviteYesList = dao.getAllInviteInfo(userId,dealerId,startDate,endDate);
			String inviteCount = inviteYesList.size()+"";
			result.put("\"inviteCount\"", inviteCount);
			result.put("\"inviteCountFact\"", inviteCountFact);
			
			//获取订单任务表信息
			List<DynaBean> orderList = dao.getOrderInfo(userId,dealerId,startDate,endDate);
			String orderCountFact = orderList.size()+"";
			//获取所有订单的数量
			List<DynaBean> orderYesList = dao.getAllOrderInfo(userId,dealerId,startDate,endDate);
			String orderCount = orderYesList.size()+"";
			result.put("\"orderCount\"", orderCount);
			result.put("\"orderCountFact\"", orderCountFact);
			
			//获取交车任务表信息
			List<DynaBean> deliveryList = dao.getDeliveryInfo(userId,dealerId,startDate,endDate);
			String deliveryCountFact = deliveryList.size()+"";
			//获取所有交车的数量
			List<DynaBean> deliveryYesList = dao.getAllDeliveryInfo(userId,dealerId,startDate,endDate);
			String deliveryCount = deliveryYesList.size()+"";
			result.put("\"deliveryCount\"", deliveryCount);
			result.put("\"deliveryCountFact\"", deliveryCountFact);
			
			//获取回访任务表信息
			List<DynaBean> revisitList = dao.getRevisitInfo(userId,dealerId,startDate,endDate);
			String revisitCountFact = revisitList.size()+"";
			//获取所有回访的数量
			List<DynaBean> revisitYesList = dao.getAllRevisitInfo(userId,dealerId,startDate,endDate);
			String revisitCount = revisitYesList.size()+"";
			result.put("\"revisitCount\"", revisitCount);
			result.put("\"revisitCountFact\"", revisitCountFact);
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", result);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 潜客等级统计数据按车型
	 */
	public void customerInfoBySeries() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = null;
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(act.getRequest().getParamValue("userId").toString())) {
			userId = act.getRequest().getParamValue("userId");
		} else if(CommonUtils.judgeDirectorLogin(act.getRequest().getParamValue("userId").toString())){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(act.getRequest().getParamValue("userId").toString());
		}
		
		String dealerId = act.getRequest().getParamValue("dealerId");
		
		Map strResult = new HashMap();
		List a = new ArrayList();
		List a2 = new ArrayList();
		try {
			//潜客等级统计数据按车型
			List<DynaBean> seriesList = dao.getCustomerInfoBySeries(userId,dealerId);
			for(int i=0;i<seriesList.size();i++) {
				DynaBean db = seriesList.get(i);
				Map result = new HashMap();
				result.put("\"seriesName\"", db.get("SERIES_NAME"));
				result.put("\"o\"", db.get("O").toString());
				result.put("\"h\"", db.get("H").toString());
				result.put("\"a\"", db.get("A").toString());
				result.put("\"b\"", db.get("B").toString());
				result.put("\"c\"", db.get("C").toString());
				result.put("\"e\"", db.get("E").toString());
				result.put("\"l\"", db.get("L").toString());
				a.add(result);
			}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", a);
			
			act.setOutData("\"strResult\"", strResult);
			
			List<DynaBean> dealerList = null;
			
			//潜客等级统计数据按经销商
			//判断经销商为1级还是2级
			if(CommonUtils.judgeDealerLevel(dealerId)) {
				//为1级
				dealerList = dao.getCustomerInfoByDealer(userId,dealerId);
			} else {
				//为2级
				dealerList = dao.getCustomerInfoByDealer2(userId,dealerId);
			}
			
			for(int i=0;i<dealerList.size();i++) {
				DynaBean db = dealerList.get(i);
				Map result2 = new HashMap();
				result2.put("\"dealerName\"", db.get("DEALER_LEVEL"));
				result2.put("\"o\"", db.get("O").toString());
				result2.put("\"h\"", db.get("H").toString());
				result2.put("\"a\"", db.get("A").toString());
				result2.put("\"b\"", db.get("B").toString());
				result2.put("\"c\"", db.get("C").toString());
				result2.put("\"e\"", db.get("E").toString());
				result2.put("\"l\"", db.get("L").toString());
				a2.add(result2);
			}
			
			strResult.put("\"result2\"", a2);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 潜客等级统计数据按经销商
	 */
	public void customerInfoByDealer() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		
		String dealerId = act.getRequest().getParamValue("dealerId");
		String userId = act.getRequest().getParamValue("userId");
		
		List<DynaBean> dealerList = null;
		
		Map strResult = new HashMap();
		List a = new ArrayList();
		try {
			//判断经销商为1级还是2级
			if(CommonUtils.judgeDealerLevel(dealerId)) {
				//为1级
				dealerList = dao.getCustomerInfoByDealer(userId,dealerId);
			} else {
				//为2级
				dealerList = dao.getCustomerInfoByDealer2(userId,dealerId);
			}
			
			for(int i=0;i<dealerList.size();i++) {
				DynaBean db = dealerList.get(i);
				Map result = new HashMap();
				String s = db.get("DEALER_LEVEL").toString();
				result.put("\"dealerName\"", db.get("DEALER_LEVEL").toString());
				result.put("\"o\"", db.get("O").toString());
				result.put("\"h\"", db.get("H").toString());
				result.put("\"a\"", db.get("A").toString());
				result.put("\"b\"", db.get("B").toString());
				result.put("\"c\"", db.get("C").toString());
				result.put("\"e\"", db.get("E").toString());
				result.put("\"l\"", db.get("L").toString());
				a.add(result);
			}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", a);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 线索处理页面信息
	 */
	public void doLeadsInit() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String adviserFlag = "10011002";
		String userId = act.getRequest().getParamValue("userId");
		String customerId = act.getRequest().getParamValue("customerId");
		String leadsCode = act.getRequest().getParamValue("leadsCode");
		String leadsAllotId = act.getRequest().getParamValue("leadsAllotId");
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(userId)) {
			adviserFlag = "10011001";
		}
		
		Map strResult = new HashMap();
		Map result = new HashMap();
		try {
			//获取线索详细信息
			List<DynaBean> leadsList = dao.getNameAndTelephone(leadsCode);
			//获取线索分派详细信息
			List<DynaBean> leadsAllotList = dao.getAdviserName(leadsAllotId);
			DynaBean db = leadsList.get(0);
			DynaBean db22 = leadsAllotList.get(0);
			Map leads = new HashMap();
			leads.put("\"leadsCode\"", leadsCode);
			leads.put("\"leadsAllotId\"", leadsAllotId);
			leads.put("\"leadsOriginId\"", db.get("LEADS_ORIGIN"));
			leads.put("\"leadsOrigin\"", db.get("LEADS_ORIGIN2"));
			if(db.get("COME_DATE")==null||"".equals(db.get("COME_DATE"))) {
				leads.put("\"comeDate\"", "");
			} else {
				leads.put("\"comeDate\"", db.get("COME_DATE"));
			}
			if(db.get("LEAVE_DATE")==null||"".equals(db.get("LEAVE_DATE"))) {
				leads.put("\"leaveDate\"", "");
			} else {
				leads.put("\"leaveDate\"", db.get("LEAVE_DATE"));
			}
			if(db.get("CUSTOMER_NAME")==null||"".equals(db.get("CUSTOMER_NAME"))) {
				leads.put("\"customerName\"", "");
			} else {
				leads.put("\"customerName\"", db.get("CUSTOMER_NAME"));
			}
			if(db.get("TELEPHONE")==null||"".equals(db.get("TELEPHONE"))) {
				leads.put("\"telephone\"", "");
			} else {
				leads.put("\"telephone\"", db.get("TELEPHONE"));
			}
			if(db.get("CUSTOMER_DESCRIBE")==null||"".equals(db.get("CUSTOMER_DESCRIBE"))) {
				leads.put("\"customerDescribe\"", "");
			} else {
				leads.put("\"customerDescribe\"", db.get("CUSTOMER_DESCRIBE"));
			}
			if(db.get("INTENT_VEHICLE_ID")==null||"".equals(db.get("INTENT_VEHICLE_ID"))) {
				leads.put("\"intentVehicleId\"", "");
			} else {
				leads.put("\"intentVehicleId\"", db.get("INTENT_VEHICLE_ID"));
			}
			if(db.get("INTENT_VEHICLE")==null||"".equals(db.get("INTENT_VEHICLE"))) {
				leads.put("\"intentVehicle\"", "");
			} else {
				leads.put("\"intentVehicle\"", db.get("INTENT_VEHICLE"));
			}
			if(db.get("JC_WAY_ID")==null||"".equals(db.get("JC_WAY_ID"))) {
				leads.put("\"jcWayId\"", "");
			} else {
				leads.put("\"jcWayId\"", db.get("JC_WAY_ID"));
			}
			if(db.get("JC_WAY")==null||"".equals(db.get("JC_WAY"))) {
				leads.put("\"jcWay\"", "");
			} else {
				leads.put("\"jcWay\"", db.get("JC_WAY"));
			}
			if(db.get("BUY_BUDGET_ID")==null || "".equals(db.get("BUY_BUDGET_ID"))) {
				leads.put("\"buyBudgetId\"", "");
			} else {
				leads.put("\"buyBudgetId\"", db.get("BUY_BUDGET_ID"));
			}
			if(db.get("BUY_BUDGET")==null || "".equals(db.get("BUY_BUDGET"))) {
				leads.put("\"buyBudget\"", "");
			} else {
				leads.put("\"buyBudget\"", db.get("BUY_BUDGET"));
			}
			if(db.get("TEST_DRIVING_ID")==null || "".equals(db.get("TEST_DRIVING_ID"))) {
				leads.put("\"testDrivingId\"", "");
			} else {
				leads.put("\"testDrivingId\"", db.get("TEST_DRIVING_ID"));
			}
			if(db.get("TEST_DRIVING")==null || "".equals(db.get("TEST_DRIVING"))) {
				leads.put("\"testDriving\"", "");
			} else {
				leads.put("\"testDriving\"", db.get("TEST_DRIVING"));
			}
			
			if(db.get("BUY_TYPE_ID")==null || "".equals(db.get("BUY_TYPE_ID"))) {
				leads.put("\"buyTypeId\"", "");
			} else {
				leads.put("\"buyTypeId\"", db.get("BUY_TYPE_ID"));
			}
			if(db.get("BUY_TYPE")==null || "".equals(db.get("BUY_TYPE"))) {
				leads.put("\"buyType\"", "");
			} else {
				leads.put("\"buyType\"", db.get("BUY_TYPE"));
			}
			if(db.get("CUSTOMER_TYPE_ID")==null || "".equals(db.get("CUSTOMER_TYPE_ID"))) {
				leads.put("\"customerTypeId\"", "");
			} else {
				leads.put("\"customerTypeId\"", db.get("CUSTOMER_TYPE_ID"));
			}
			if(db.get("CUSTOMER_TYPE")==null || "".equals(db.get("CUSTOMER_TYPE"))) {
				leads.put("\"customerType\"", "");
			} else {
				leads.put("\"customerType\"", db.get("CUSTOMER_TYPE"));
			}
			if(db22.get("ADVISER_NAME")==null||"".equals(db22.get("ADVISER_NAME"))) {
				leads.put("\"adviserName\"", "");
			} else {
				leads.put("\"adviserName\"", db22.get("ADVISER_NAME"));
			}
			result.put("\"customerList\"", leads);
			
			//获取意向等级下拉列表
			List<DynaBean> ctmRankList = dao.getCtmRankInfo();
			List ctmRankOP = new ArrayList();
			for(int i=0;i<ctmRankList.size();i++) {
				DynaBean db2 = ctmRankList.get(i);
				Map ctmRank = new HashMap();
				ctmRank.put("\"ctmRankId\"", db2.get("CODE_ID"));
				ctmRank.put("\"ctmRankName\"", db2.get("CODE_DESC"));
				ctmRankOP.add(ctmRank);
			}
			result.put("\"ctmRank\"", ctmRankOP);
			
			//获取意向车型下拉列表
			List<DynaBean> intentVehicleList = dao.getIntentVehicleInfo();
			List intentVehicleOP = new ArrayList();
			for(int i=0;i<intentVehicleList.size();i++) {
				DynaBean db3 = intentVehicleList.get(i);
				Map intentVehicle = new HashMap();
				intentVehicle.put("\"seriesId\"", db3.get("SERIES_ID"));
				intentVehicle.put("\"seriesName\"", db3.get("SERIES_NAME"));
				if(db3.get("UP_SERIES_ID")==null||"".equals(db3.get("UP_SERIES_ID"))) {
					intentVehicle.put("\"upSeriesId\"", "");
				} else {
					intentVehicle.put("\"upSeriesId\"", db3.get("UP_SERIES_ID"));
				}
				intentVehicleOP.add(intentVehicle);
			}
			result.put("\"intentVehicle\"", intentVehicleOP);
			
			//获取销售流程进度下拉列表
			List<DynaBean> salesProgressList = dao.getSalesProgressInfo();
			List salesProgressOP = new ArrayList();
			for(int i=0;i<salesProgressList.size();i++) {
				DynaBean db4 = salesProgressList.get(i);
				Map salesProgress = new HashMap();
				salesProgress.put("\"salesProgressId\"", db4.get("CODE_ID"));
				salesProgress.put("\"salesProgressName\"", db4.get("CODE_DESC"));
				salesProgressOP.add(salesProgress);
			}
			result.put("\"salesProgress\"", salesProgressOP);
			
			//获取购车预算下拉列表
			List<DynaBean> buyBudgetList = dao.getBuyBudgetInfo();
			List buyBudgetOP = new ArrayList();
			for(int i=0;i<buyBudgetList.size();i++) {
				DynaBean db5 = buyBudgetList.get(i);
				Map buyBudget = new HashMap();
				buyBudget.put("\"buyBudgetId\"", db5.get("CODE_ID"));
				buyBudget.put("\"buyBudgetName\"", db5.get("CODE_DESC"));
				buyBudgetOP.add(buyBudget);
			}
			result.put("\"buyBudget\"", buyBudgetOP);
			
			//获取客户类型下拉列表
			List<DynaBean> customerTypeList = dao.getCustomerTypeInfo();
			List customerTypeOP = new ArrayList();
			for(int i=0;i<customerTypeList.size();i++) {
				DynaBean db6 = customerTypeList.get(i);
				Map customerType = new HashMap();
				customerType.put("\"customerTypeId\"", db6.get("CODE_ID"));
				customerType.put("\"customerTypeName\"", db6.get("CODE_DESC"));
				customerTypeOP.add(customerType);
			}
			result.put("\"customerType\"", customerTypeOP);
			
			//获取来店契机下拉列表
			List<DynaBean> collectFashionList = dao.getCollectFashionInfo();
			List collectFashionOP = new ArrayList();
			for(int i=0;i<collectFashionList.size();i++) {
				DynaBean db00 = collectFashionList.get(i);
				Map collectFashion = new HashMap();
				collectFashion.put("\"collectFashionId\"", db00.get("CODE_ID"));
				collectFashion.put("\"collectFashionName\"", db00.get("CODE_DESC"));
				collectFashionOP.add(collectFashion);
			}
			result.put("\"collectFashion\"", collectFashionOP);
			
			//获取集客方式下拉列表
			List<DynaBean> jcWayList = dao.getJcWayInfo();
			List jcWayOP = new ArrayList();
			for(int i=0;i<jcWayList.size();i++) {
				DynaBean db01 = jcWayList.get(i);
				Map jcWay = new HashMap();
				jcWay.put("\"jcWayId\"", db01.get("CODE_ID"));
				jcWay.put("\"jcWayName\"", db01.get("CODE_DESC"));
				jcWayOP.add(jcWay);
			}
			result.put("\"jcWay\"", jcWayOP);
			
			//获取购买类型下拉列表
			List<DynaBean> buyTypeList = dao.getBuyTypeInfo();
			List buyTypeOP = new ArrayList();
			for(int i=0;i<buyTypeList.size();i++) {
				DynaBean db02 = buyTypeList.get(i);
				Map buyType = new HashMap();
				buyType.put("\"buyTypeId\"", db02.get("CODE_ID"));
				buyType.put("\"buyTypeName\"", db02.get("CODE_DESC"));
				buyTypeOP.add(buyType);
			}
			result.put("\"buyType\"", buyTypeOP);
			
			//获取跟进方式下拉列表
			List<DynaBean> followTypeList = dao.getFollowTypeInfo();
			List followTypeOP = new ArrayList();
			for(int i=0;i<followTypeList.size();i++) {
				DynaBean db7 = followTypeList.get(i);
				Map followType = new HashMap();
				followType.put("\"followTypeId\"", db7.get("CODE_ID"));
				followType.put("\"followTypeName\"", db7.get("CODE_DESC"));
				followTypeOP.add(followType);
			}
			result.put("\"followType\"", followTypeOP);
			
			//获取邀约方式下拉列表
			List<DynaBean> inviteTypeList = dao.getInviteTypeInfo();
			List inviteTypeOP = new ArrayList();
			for(int i=0;i<inviteTypeList.size();i++) {
				DynaBean db8 = inviteTypeList.get(i);
				Map inviteType = new HashMap();
				inviteType.put("\"inviteTypeId\"", db8.get("CODE_ID"));
				inviteType.put("\"inviteTypeName\"", db8.get("CODE_DESC"));
				inviteTypeOP.add(inviteType);
			}
			result.put("\"inviteType\"", inviteTypeOP);
			
			//获取战败原因下拉列表
			List<DynaBean> defeatReasonList = dao.getDefeatReasonInfo();
			List defeatReasonOP = new ArrayList();
			for(int i=0;i<defeatReasonList.size();i++) {
				DynaBean db9 = defeatReasonList.get(i);
				Map defeatReason = new HashMap();
				defeatReason.put("\"defeatReasonId\"", db9.get("CODE_ID"));
				defeatReason.put("\"defeatReasonName\"", db9.get("CODE_DESC"));
				if(db9.get("UP_CODE_ID")==null||"".equals(db9.get("UP_CODE_ID"))) {
					defeatReason.put("\"upCodeId\"", "");
				} else {
					defeatReason.put("\"upCodeId\"", db9.get("UP_CODE_ID"));
				}
				defeatReasonOP.add(defeatReason);
			}
			result.put("\"defeatReason\"", defeatReasonOP);
			
			//获取战败车型下拉列表
			List<DynaBean> defeatVehicleList = dao.getDefeatVehicleInfo();
			List defeatVehicleOP = new ArrayList();
			for(int i=0;i<defeatVehicleList.size();i++) {
				DynaBean db10 = defeatVehicleList.get(i);
				Map defeatVehicle = new HashMap();
				defeatVehicle.put("\"seriesId\"", db10.get("SERIES_ID"));
				defeatVehicle.put("\"seriesName\"", db10.get("SERIES_NAME"));
				if(db10.get("UP_SERIES_ID")==null||"".equals(db10.get("UP_SERIES_ID"))) {
					defeatVehicle.put("\"upSeriesId\"", "");
				} else {
					defeatVehicle.put("\"upSeriesId\"", db10.get("UP_SERIES_ID"));
				}
				defeatVehicleOP.add(defeatVehicle);
			}
			result.put("\"defeatVehicle\"", defeatVehicleOP);
			
			result.put("\"adviserFlag\"", adviserFlag);
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", result);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 线索处理数据提交
	 * @throws UnsupportedEncodingException 
	 */
	public void doLeads() throws UnsupportedEncodingException {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String customerName = act.getRequest().getParamValue("customerName");
		String telephone = act.getRequest().getParamValue("telephone");
		String leadsCode = act.getRequest().getParamValue("leadsCode");
		String leadsAllotId = act.getRequest().getParamValue("leadsAllotId");
		String userId = act.getRequest().getParamValue("userId");
		String dealerId = act.getRequest().getParamValue("dealerId");
		String leadsOrigin = act.getRequest().getParamValue("leadsOrigin");
		String intentType = act.getRequest().getParamValue("intentType");
		String salesProgress = act.getRequest().getParamValue("salesProgress");
		String followInfo = act.getRequest().getParamValue("followInfo");
//		if(followInfo!=null&&!"".equals(followInfo)){
//			followInfo = new String(followInfo.getBytes("iso-8859-1"),"gbk");
//		}
		String buyBudget = act.getRequest().getParamValue("buyBudget");
		String intentVehicle = act.getRequest().getParamValue("intentVehicle");
		String customerType = act.getRequest().getParamValue("customerType");
		String collectFashion = act.getRequest().getParamValue("collectFashion");
		String ifDrive = act.getRequest().getParamValue("ifDrive");
		String jcWay = act.getRequest().getParamValue("jcWay");
		String buyType = act.getRequest().getParamValue("buyType");
		String nextTaskType = act.getRequest().getParamValue("nextTaskType");
		String nextFollowDate = act.getRequest().getParamValue("nextFollowDate");
		String followType = act.getRequest().getParamValue("followType");
		String followPlan = act.getRequest().getParamValue("followPlan");
//		if(followPlan!=null&&!"".equals(followPlan)){
//			followPlan = new String(followPlan.getBytes("iso-8859-1"),"gbk");
//		}
		String planInviteDate = act.getRequest().getParamValue("planInviteDate");
		String planMeetDate = act.getRequest().getParamValue("planMeetDate");
		String inviteType = act.getRequest().getParamValue("inviteType");
		String orderDate = act.getRequest().getParamValue("orderDate");
		String defeatVehicle = act.getRequest().getParamValue("defeatVehicle");
		String defeatReason = act.getRequest().getParamValue("defeatReason");
		String defeatDate = act.getRequest().getParamValue("defeatDate");
		String defeatRemark = act.getRequest().getParamValue("defeatRemark");
//		if(defeatRemark!=null&&!"".equals(defeatRemark)){
//			defeatRemark = new String(defeatRemark.getBytes("iso-8859-1"),"gbk");
//		}
		String failureDate = act.getRequest().getParamValue("failureDate");
		String failureRemark = act.getRequest().getParamValue("failureRemark");
//		if(failureRemark!=null&&!"".equals(failureRemark)){
//			failureRemark = new String(failureRemark.getBytes("iso-8859-1"),"gbk");
//		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Map strResult = new HashMap();
		try {
			String remark="手机端处理线索";
			TPcLeadsPO leadsOldPoByOem = new TPcLeadsPO();
			leadsOldPoByOem.setLeadsCode(Long.parseLong(leadsCode));
			TPcLeadsPO leadsNewPoByOem = new TPcLeadsPO();
			leadsNewPoByOem.setFailureRemark(remark);
			dao.update(leadsOldPoByOem, leadsNewPoByOem);
			//PC和移动端同时处理问题
			int	ifHandle = CommonUtils.getTheSameData(leadsCode);
			Map<String,String>map=new HashMap<String,String>();
			map.put("telephone", telephone);
			map.put("dealerId", dealerId);
			if(ifHandle==10041002) {//未处理的情况
				// 判断是否可以做订单
				if(!CommonUtils.judgeIfOrder(map) && "order".equals(nextTaskType)) { //不可以做
					strResult.put("\"success\"", "false");
					strResult.put("\"msg\"", "该客户无首次到店客流信息，无法生成订车计划!");
					act.setOutData("\"strResult\"", strResult);
				} else {
					if("follow".equals(nextTaskType) || "invite".equals(nextTaskType) || "order".equals(nextTaskType)){
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
						customerPo.setTelephone(telephone);
						customerPo.setLeadsOrigin(leadsOrigin);
						customerPo.setJcWay(jcWay);
						customerPo.setBuyBudget(buyBudget);
						customerPo.setCtmProp(customerType);
						customerPo.setIntentVehicle(intentVehicle);
						customerPo.setFirstContactTime(new Date());
						customerPo.setCtmRank(intentType);
						customerPo.setComeReason(collectFashion);
						customerPo.setIfDrive(Long.parseLong(ifDrive));
						customerPo.setBuyType(buyType);
						if("follow".equals(nextTaskType)) {//点跟进时
							customerPo.setNextFollowTime(sdf.parse(nextFollowDate));//下次跟进时间
						} else if("invite".equals(nextTaskType)) {//点邀约时
							customerPo.setNextFollowTime(sdf.parse(planInviteDate));//下次跟进时间
							customerPo.setPreShopTime(sdf.parse(planMeetDate));//预计到店时间
						}
						customerPo.setCtmStatus(Constant.CTM_STATUS_01.toString());//客户状态
						customerPo.setStatus(Constant.STATUS_ENABLE.longValue());
						customerPo.setCreateDate(new Date());
						customerPo.setCreateBy(Long.parseLong(userId));
						customerPo.setDealerId(Long.parseLong(dealerId));
						customerPo.setAdviser(Long.parseLong(userId));
						customerPo.setSalesProgress(salesProgress);
						customerPo.setDataFrom("60601002");
						if(jcWay!=null && !"".equals(jcWay) && ("60021001".equals(jcWay)||"60021003".equals(jcWay)||"60021004".equals(jcWay)||"60021008".equals(jcWay))) {
							customerPo.setComeCount(1L);
						}
						dao.insert(customerPo);
						
						//修改销售线索主表
						TPcLeadsPO leadsOldPo = new TPcLeadsPO();
						leadsOldPo.setLeadsCode(Long.parseLong(leadsCode));
						TPcLeadsPO leadsNewPo = new TPcLeadsPO();
						leadsNewPo.setLeadsCode(Long.parseLong(leadsCode));
						leadsNewPo.setCustomerName(customerName);
						leadsNewPo.setCustomerId(Long.parseLong(customerId));
						leadsNewPo.setTelephone(telephone);
						leadsNewPo.setIntentVehicle(intentVehicle);
						leadsNewPo.setJcWay(jcWay);
						leadsNewPo.setIfHandle(10041001);
						dao.update(leadsOldPo, leadsNewPo);
						
						//修改销售线索分派表
						TPcLeadsAllotPO leadsAllotOldPo = new TPcLeadsAllotPO();
						leadsAllotOldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						TPcLeadsAllotPO leadsAllotNewPo = new TPcLeadsAllotPO();
						//leadsAllotNewPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						leadsAllotNewPo.setCustomerName(customerName);
						leadsAllotNewPo.setCustomerId(Long.parseLong(customerId));
						leadsAllotNewPo.setTelephone(telephone);
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
						follow0Po.setCreateBy(userId);
						follow0Po.setDataFrom("60601002");
						dao.insert(follow0Po);
						
						//增加接触点信息
						CommonUtils.addContackPoint(Constant.POINT_WAY_03, followInfo, customerId, userId, dealerId);
						
						if("follow".equals(nextTaskType)) {//点跟进时
							remark=remark+"-跟进";
							leadsNewPoByOem.setFailureRemark(remark);
							dao.update(leadsOldPoByOem, leadsNewPoByOem);
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
							followPo.setCreateBy(userId);
							followPo.setPreviousTask(Long.parseLong(followId));
							followPo.setDataFrom("60601002");
							dao.insert(followPo);
							
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), nextTaskId, customerId, dealerId, userId, nextFollowDate,"");
						} else if("invite".equals(nextTaskType)) {//点邀约时
							remark=remark+"-邀约";
							leadsNewPoByOem.setFailureRemark(remark);
							dao.update(leadsOldPoByOem, leadsNewPoByOem);
							//新增下次计划邀约任务表
							TPcInvitePO invitePo = new TPcInvitePO();
							invitePo.setInviteId(Long.parseLong(nextTaskId));
							invitePo.setCustomerId(Long.parseLong(customerId));
							invitePo.setCreateDate(new Date());
							invitePo.setCreateBy(userId);
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
							invitePo.setDataFrom("60601002");
							
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), nextTaskId.toString(), customerId, dealerId, userId, planInviteDate,"");
							dao.insert(invitePo);
						} else if("order".equals(nextTaskType)) {//点订车时
							remark=remark+"-订单";
							leadsNewPoByOem.setFailureRemark(remark);
							dao.update(leadsOldPoByOem, leadsNewPoByOem);
							//新增下次订单任务表
							//新增下次订单任务
							TPcOrderPO orderPo = new TPcOrderPO();
							orderPo.setOrderId(Long.parseLong(nextTaskId));
							orderPo.setCustomerId(Long.parseLong(customerId));
							orderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
							orderPo.setOldLevel(intentType);
							orderPo.setCreateDate(new Date());
							orderPo.setCreateBy(userId);
							orderPo.setOldSalesProgress(salesProgress);
							orderPo.setPreviousTask(Long.parseLong(followId));
							orderPo.setTaskStatus(Constant.TASK_STATUS_01);
							orderPo.setOrderDate(sdf.parse(orderDate));
							orderPo.setDataFrom("60601002");
							dao.insert(orderPo);
							
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_08.toString(), nextTaskId.toString(), customerId, dealerId, userId, orderDate,"");
						}
						
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
						
					} else if("defeat".equals(nextTaskType)){//选择战败后保存
						remark=remark+"-战败";
						leadsNewPoByOem.setFailureRemark(remark);
						dao.update(leadsOldPoByOem, leadsNewPoByOem);
						//修改销售线索主表
						TPcLeadsPO leadsOldPo = new TPcLeadsPO();
						leadsOldPo.setLeadsCode(Long.parseLong(leadsCode));
						TPcLeadsPO leadsNewPo = new TPcLeadsPO();
						leadsNewPo.setLeadsCode(Long.parseLong(leadsCode));
						leadsNewPo.setCustomerName(customerName);
						leadsNewPo.setTelephone(telephone);
						leadsNewPo.setIntentVehicle(intentVehicle);
						leadsNewPo.setLeadsStatus(Constant.LEADS_STATUS_02.longValue());//线索状态为 战败线索
						leadsNewPo.setDefeatModel(defeatVehicle);
						leadsNewPo.setDefeatReason(defeatReason);
						leadsNewPo.setDefaultRemark(defeatRemark);
						leadsNewPo.setJcWay(jcWay);
						leadsNewPo.setIfHandle(10041001);
						dao.update(leadsOldPo, leadsNewPo);
						
						//修改销售线索分派表
						TPcLeadsAllotPO leadsAllotOldPo = new TPcLeadsAllotPO();
						leadsAllotOldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						TPcLeadsAllotPO leadsAllotNewPo = new TPcLeadsAllotPO();
						//leadsAllotNewPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						leadsAllotNewPo.setCustomerName(customerName);
						leadsAllotNewPo.setTelephone(telephone);
						leadsAllotNewPo.setStatus(Constant.STATUS_DISABLE);
						leadsAllotNewPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);
						leadsAllotNewPo.setConfirmDate(new Date());
						dao.update(leadsAllotOldPo, leadsAllotNewPo);
						
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
						
					} else if("failure".equals(nextTaskType)){//选择失效后保存
						remark=remark+"-失效";
						leadsNewPoByOem.setFailureRemark(remark);
						dao.update(leadsOldPoByOem, leadsNewPoByOem);
						//修改销售线索主表
						TPcLeadsPO leadsOldPo = new TPcLeadsPO();
						leadsOldPo.setLeadsCode(Long.parseLong(leadsCode));
						TPcLeadsPO leadsNewPo = new TPcLeadsPO();
						leadsNewPo.setLeadsCode(Long.parseLong(leadsCode));
						leadsNewPo.setCustomerName(customerName);
						leadsNewPo.setTelephone(telephone);
						leadsNewPo.setIntentVehicle(intentVehicle);
						leadsNewPo.setLeadsStatus(Constant.LEADS_STATUS_03.longValue());//线索状态为 失效线索
						leadsNewPo.setJcWay(jcWay);
						leadsNewPo.setIfHandle(10041001);
						dao.update(leadsOldPo, leadsNewPo);
						
						//修改销售线索分派表
						TPcLeadsAllotPO leadsAllotOldPo = new TPcLeadsAllotPO();
						leadsAllotOldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						TPcLeadsAllotPO leadsAllotNewPo = new TPcLeadsAllotPO();
						//leadsAllotNewPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						leadsAllotNewPo.setCustomerName(customerName);
						leadsAllotNewPo.setTelephone(telephone);
						leadsAllotNewPo.setStatus(Constant.STATUS_DISABLE);
						leadsAllotNewPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);
						leadsAllotNewPo.setConfirmDate(new Date());
						dao.update(leadsAllotOldPo, leadsAllotNewPo);
						
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
						//标记提醒信息为已完成
						//CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_02.toString());
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
					}else if("invidateRadio".equals(nextTaskType)){
						remark=remark+"-无效";
						leadsNewPoByOem.setFailureRemark(remark);
						dao.update(leadsOldPoByOem, leadsNewPoByOem);
						//选择无效保存
						//修改销售线索主表
						TPcLeadsPO leadsOldPo2 = new TPcLeadsPO();
						leadsOldPo2.setLeadsCode(Long.parseLong(leadsCode));
						TPcLeadsPO leadsNewPo2 = new TPcLeadsPO();
						leadsNewPo2.setLeadsCode(Long.parseLong(leadsCode));
						leadsNewPo2.setCustomerName(customerName);
						leadsNewPo2.setTelephone(telephone);
						leadsNewPo2.setIntentVehicle(intentVehicle);
						leadsNewPo2.setIfHandle(Constant.IF_TYPE_YES);
						leadsNewPo2.setLeadsStatus(Constant.LEADS_STATUS_04.longValue());//线索状态为 战败线索
						dao.update(leadsOldPo2, leadsNewPo2);
						
						//修改销售线索分派表
						TPcLeadsAllotPO leadsAllotOldPo2 = new TPcLeadsAllotPO();
						leadsAllotOldPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
						TPcLeadsAllotPO leadsAllotNewPo2 = new TPcLeadsAllotPO();
						//leadsAllotNewPo2.setLeadsAllotId(Long.parseLong(leadsAllotId));
						leadsAllotNewPo2.setCustomerName(customerName);
						leadsAllotNewPo2.setTelephone(telephone);
						leadsAllotNewPo2.setStatus(Constant.STATUS_DISABLE);
						
						leadsAllotNewPo2.setIfConfirm(Constant.ADVISER_CONFIRM_02);
						leadsAllotNewPo2.setConfirmDate(new Date());
						dao.update(leadsAllotOldPo2, leadsAllotNewPo2);
						
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
						
					}
					
					strResult.put("\"success\"", "true");
					strResult.put("\"msg\"", "操作成功");
					
					act.setOutData("\"strResult\"", strResult);
				}
			} else {
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
				strResult.put("\"success\"", "false");
				strResult.put("\"msg\"", "该条数据已被执行！");
				
				act.setOutData("\"strResult\"", strResult);
			}
			
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 手机号码验证是否建档
	 */
	public void doCheckPhone() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String telephone = act.getRequest().getParamValue("telephone");
		String dealerId = act.getRequest().getParamValue("dealerId");
		Map strResult = new HashMap();
		Map result = new HashMap();
		try {
			//验证是否已建档
			List<Map<String, Object>> getHasList = dao.getHasCustomer(telephone, dealerId);
			
			//未建档
			if(getHasList.size() <= 0) {
				strResult.put("\"success\"", "true");
				strResult.put("\"msg\"", "操作成功");
				strResult.put("\"result\"", result);
				act.setOutData("\"strResult\"", strResult);
			} else {//已建档
				strResult.put("\"success\"", "false");
				strResult.put("\"msg\"", "该客户已有建档数据，逻辑校验很复杂，请在电脑端操作此条数据!");
				act.setOutData("\"strResult\"", strResult);
			}
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 展厅监控报表
	 */
	public void controlReport() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String startDate = act.getRequest().getParamValue("startDate");
        String endDate = act.getRequest().getParamValue("endDate");
        String dealerId = act.getRequest().getParamValue("dealerId");
        String userId = null;
        String dealerCode = act.getRequest().getParamValue("dealerCode");
        if(CommonUtils.isNullString(dealerCode)){
        	dealerCode = act.getRequest().getParamValue("dealerCode") ;
        }
        Map<String,String> map=new HashMap<String,String>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("dealerCode", dealerCode);
		map.put("dealerId", dealerId);
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(act.getRequest().getParamValue("userId"))) {
			userId = act.getRequest().getParamValue("userId");
			map.put("userId", userId);
		} else if(CommonUtils.judgeDirectorLogin(act.getRequest().getParamValue("userId"))){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(act.getRequest().getParamValue("userId"));
			map.put("userId", userId);
		}
		Map strResult = new HashMap();
		List a = new ArrayList();
		List a2 = new ArrayList();
		try {
			//获取报表数据
			List<DynaBean> reportList = dao.getControlReport(map);
			for(int i=0;i<reportList.size();i++) {
				DynaBean db = reportList.get(i);
				Map result = new HashMap();
				result.put("\"leadsSum\"", db.get("CLCOUNT").toString());
				result.put("\"handleSum\"", db.get("WCLCOUNT").toString());
				result.put("\"24handleSum\"", db.get("CECOUNT").toString());
				result.put("\"firstPassenger\"", db.get("SK").toString());
				result.put("\"yyPassenger\"", db.get("YY").toString());
				result.put("\"jkSum\"", db.get("JK").toString());
				result.put("\"orderSum\"", db.get("DD").toString());
				result.put("\"deliverySum\"", db.get("JC").toString());
				a.add(result);
			}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", a);
			
			act.setOutData("\"strResult\"", strResult);
			
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 获取合作经销商列表
	 */
	public void getCompanyGroupList() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		
		String dealerId = act.getRequest().getParamValue("dealerId");
		
		Map strResult = new HashMap();
		List a = new ArrayList();
		try {
			//获取未处理线索信息
			List<DynaBean> list = dao.getCompanyGroupList(dealerId);
			for(int i=0;i<list.size();i++) {
				DynaBean db = list.get(i);
				Map result = new HashMap();
				result.put("\"dealerId\"", db.get("DEALER_IDS"));
				result.put("\"dealerCode\"", db.get("DEALER_CODES"));
				result.put("\"shortName\"", db.get("DEALER_SHORTNAME"));
				
				a.add(result);
			}
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", a);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
}
