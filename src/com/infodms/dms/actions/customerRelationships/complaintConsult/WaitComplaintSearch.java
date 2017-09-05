package com.infodms.dms.actions.customerRelationships.complaintConsult;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.customerRelationships.baseSetting.seatsSet;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptDao;
import com.infodms.dms.dao.customerRelationships.ComplaintConsultDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtCrmComplaintDealRecordPO;
import com.infodms.dms.po.TtCrmComplaintPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 待处理投诉查询ACTIONS
 * @ClassName     : WaitComplaintSearch 
 * @Description   : 待处理投诉查询
 * @author        : wangming
 * CreateDate     : 2013-4-26
 */
public class WaitComplaintSearch {
	private static Logger logger = Logger.getLogger(WaitComplaintSearch.class);
	//待处理投诉查询页面
	private final String WaitComplaintSearch = "/jsp/customerRelationships/complaintConsult/waitComplaintSearchYx.jsp";
	//处理中状态页面
	private final String ComplaintProcessingUpdate = "/jsp/customerRelationships/complaintConsult/complaintProcessingUpdate.jsp";
	private final String ComplaintProcessingUpdate2 = "/jsp/customerRelationships/complaintConsult/complaintProcessingUpdate2.jsp";
	//待处理投诉查询处理页面
	private final String WaitProcessComplaintUpdate = "/jsp/customerRelationships/complaintConsult/waitProcessComplaintUpdate.jsp";
	
	//所有转咨询处理页面
	private final String ComplaintTurnAdviceUpdate = "/jsp/customerRelationships/complaintConsult/complaintTurnAdviceUpdate.jsp";

	//所有满意关闭处理页面
	private final String complaintSatisfyCloseUpdate = "/jsp/customerRelationships/complaintConsult/complaintSatisfyCloseUpdate.jsp";

	
	private static String vin = "";  				
	private static String name = "";  				
	private static String tele = "";  				
	private static String accuser = "";  				
	private static String dateStart ="";  				
	private static String dateEnd = "";  	
	private static String dealStart = "";  				
	private static String dealEnd = "";  	
	private static String status = "";  	
	private static String region = "";  				
	private static String pro = ""; 
	private static String delaystatus = "";
	private static Integer curPage = 1;	
	private static String advanced = "";
	private static String model = "";
	
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	//投诉受理初始化
	public void waitComplaintSearchInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();			
			seatsSet seat = new seatsSet();
			//省份 小区
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//大区
			act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			//车型
			act.setOutData("modelList", commonUtilActions.getAllModel());
			logger.info("logon user...................."+logonUser.getUserId());
			boolean isAdmin = seat.isAdmin(logonUser.getUserId());
			//是否是管理员
			act.setOutData("isAdmin", isAdmin);
			List<Map<String, Object>> statusList = commonUtilActions.getTcCode(Constant.COMPLAINT_STATUS);
			List<Map<String, Object>> slist = new ArrayList<Map<String,Object>>();
			if(isAdmin){
				for(Map<String, Object> map : statusList){
					if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_WAIT)){
						slist.add(map);
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_DOING)){
						slist.add(map);
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_DEALER_STATUS_DOING)){
						slist.add(map);
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_DOING_ALREADY)){
						slist.add(map);
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_DOING_REJECT)){
						slist.add(map);
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_WAIT_FEEDBACK)){
						slist.add(map);
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_WAIT_CLOSE)){
						slist.add(map);
					}
				}
			}else{
				for(Map<String, Object> map : statusList){
					if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_WAIT)){
						slist.add(map);
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_DOING)){
						slist.add(map);
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_DEALER_STATUS_DOING)){
						slist.add(map);
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_DOING_ALREADY)){
						slist.add(map);
					// 艾春9.27去掉驳回状态
//					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_DOING_REJECT)){
//						slist.add(map);
					}
				}
			}
			
			//处理特殊添加工单状态
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("CODE_ID", "unClose");
			map.put("CODE_DESC", "未关闭");
			slist.add(map);
			act.setOutData("stautsList", slist);
			
			//处理特殊添加延迟状态
			List<Map<String, Object>> delaystatusList = commonUtilActions.getTcCode(Constant.DELAY_STATUS.toString());
			map = new HashMap<String,Object>();
			map.put("CODE_ID", "delayAdmin");
			map.put("CODE_DESC", "公司副总审核通过（强制关闭）");
			delaystatusList.add(map);
			act.setOutData("delaystatusList", delaystatusList);
			
			
			if("1".equals(request.getParamValue("flagInit"))){
				act.setOutData("vin", vin);
				act.setOutData("name", name); 
				act.setOutData("tele", tele);
				act.setOutData("accuser", accuser);
				act.setOutData("dateStart", dateStart);
				act.setOutData("dateEnd", dateEnd);
				act.setOutData("dealStart", dealStart);
				act.setOutData("dealEnd", dealEnd);
				act.setOutData("status", status);
				act.setOutData("regionS", region);
				act.setOutData("proS", pro);
				act.setOutData("delaystatus", delaystatus);
				act.setOutData("curP",curPage);
				act.setOutData("advanced", advanced);
				act.setOutData("model", model);
			}
			
			act.setForword(WaitComplaintSearch);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"待处理投诉查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryWaitComplaintSearch(){
		act.getResponse().setContentType("application/json");
		try{
			
			 vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
			 name = CommonUtils.checkNull(request.getParamValue("name"));  				
			 tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			 accuser = CommonUtils.checkNull(request.getParamValue("accuser"));  				
			 dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			 dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  	
			 dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			 dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  	
			 status = CommonUtils.checkNull(request.getParamValue("status"));  	
				
			 region = CommonUtils.checkNull(request.getParamValue("region"));  				
			 pro = CommonUtils.checkNull(request.getParamValue("pro"));  	
			 delaystatus = CommonUtils.checkNull(request.getParamValue("delaystatus")); 
			 
			 advanced = CommonUtils.checkNull(request.getParamValue("advanced"));
			 model = CommonUtils.checkNull(request.getParamValue("model"));
			
			
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			CommonUtilActions commonUtilActions = new CommonUtilActions();			
			//分页方法 begin
			curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			act.setOutData("curP", curPage);
			seatsSet seat = new seatsSet();	
			boolean isAdmin = seat.isAdmin(logonUser.getUserId());
			List<Map<String, Object>> statusList = commonUtilActions.getTcCode(Constant.COMPLAINT_STATUS);
			String state = "";
			if(isAdmin){
				for(Map<String, Object> map : statusList){
					if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_WAIT)){
						if(state == "") state = Constant.COMPLAINT_STATUS_WAIT;
						else state = state+","+Constant.COMPLAINT_STATUS_WAIT;
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_DOING)){
						if(state == "") state = Constant.COMPLAINT_STATUS_DOING;
						else state = state+","+Constant.COMPLAINT_STATUS_DOING;
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_DEALER_STATUS_DOING)){
						if(state == "") state = Constant.COMPLAINT_DEALER_STATUS_DOING;
						else state = state+","+Constant.COMPLAINT_DEALER_STATUS_DOING;
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_DOING_ALREADY)){
						if(state == "") state = Constant.COMPLAINT_STATUS_DOING_ALREADY;
						else state = state+","+Constant.COMPLAINT_STATUS_DOING_ALREADY;
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_DOING_REJECT)){
						if(state == "") state = Constant.COMPLAINT_STATUS_DOING_REJECT;
						else state = state+","+Constant.COMPLAINT_STATUS_DOING_REJECT;
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_WAIT_FEEDBACK)){
						if(state == "") state = Constant.COMPLAINT_STATUS_WAIT_FEEDBACK;
						else state = state+","+Constant.COMPLAINT_STATUS_WAIT_FEEDBACK;
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_WAIT_CLOSE)){
						if(state == "") state = Constant.COMPLAINT_STATUS_WAIT_CLOSE;
						else state = state+","+Constant.COMPLAINT_STATUS_WAIT_CLOSE;
					}
				}
			}else{
				for(Map<String, Object> map : statusList){
					if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_WAIT)){
						if(state == "") state = Constant.COMPLAINT_STATUS_WAIT;
						else state = state+","+Constant.COMPLAINT_STATUS_WAIT;
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_DOING)){
						if(state == "") state = Constant.COMPLAINT_STATUS_DOING;
						else state = state+","+Constant.COMPLAINT_STATUS_DOING;
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_DEALER_STATUS_DOING)){
						if(state == "") state = Constant.COMPLAINT_DEALER_STATUS_DOING;
						else state = state+","+Constant.COMPLAINT_DEALER_STATUS_DOING;
					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_DOING_ALREADY)){
						if(state == "") state = Constant.COMPLAINT_STATUS_DOING_ALREADY;
						else state = state+","+Constant.COMPLAINT_STATUS_DOING_ALREADY;
					// 艾春9.27去掉驳回状态
//					}else if(map.get("CODE_ID").equals(Constant.COMPLAINT_STATUS_DOING_REJECT)){
//						if(state == "") state = Constant.COMPLAINT_STATUS_DOING_REJECT;
//						else state = state+"','"+Constant.COMPLAINT_STATUS_DOING_REJECT;
					}
				}
			}
			
			PageResult<Map<String,Object>> complaintAcceptData = dao.queryWaitComplaintInfo(vin,name,tele,accuser,dateStart,dateEnd,dealStart,dealEnd,status,region,pro,state,delaystatus,advanced, model,Constant.PAGE_SIZE,curPage);
			if(complaintAcceptData!=null && complaintAcceptData.getTotalRecords()>0){
				for(Map<String, Object> map :complaintAcceptData.getRecords()){
					map.put("ISADMIN", isAdmin);
				}
			}
			
			
			act.setOutData("ps", complaintAcceptData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理投诉查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//经销商投诉处理界面
	public void waitComplaintSearchUpdate11(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  	
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid"));  	
			String stauts = CommonUtils.checkNull(request.getParamValue("stauts"));  	
			
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			ComplaintConsultDao dao = ComplaintConsultDao.getInstance();
			Map<String,Object> complaintConsult = dao.queryComplaintConsult(cpid,ctmid);
			act.setOutData("complaintConsult", complaintConsult);
			act.setOutData("dealRecordList", complaintAcceptDao.queryDealRecord(Long.parseLong(cpid)));
			act.setOutData("returnRecordList", complaintAcceptDao.queryReturnRecord(Long.parseLong(cpid)));
			act.setOutData("verifyRecordList", complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid)));
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			List<TmOrgPO> tmOrgList = commonUtilActions.getTmOrgPO();
			//大区
			act.setOutData("tmOrgList", tmOrgList);
			List<TmOrgPO> smallTmOrgList = commonUtilActions.getSmallTmOrgPO();
			//小区
			act.setOutData("tmOrgSmallList", smallTmOrgList);
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//坐席
			//SeatsSetDao seatsSetDao = SeatsSetDao.getInstance();
			act.setOutData("ttCrmSeatsList",commonUtilActions.getSeatAdminDepart());
			List<Map<String, Object>> confireList = commonUtilActions.getTcCode(Constant.CONFIRE_TYPE);
			//确认意见数据
			List<Map<String, Object>> tempConfireList = new ArrayList<Map<String,Object>>();

			//处理中
			if(stauts.equals(Constant.COMPLAINT_STATUS_DOING) || stauts.equals(Constant.COMPLAINT_DEALER_STATUS_DOING) || stauts.equals(Constant.COMPLAINT_STATUS_DOING_REJECT)){
				for(Map<String, Object> map : confireList){
					String value = (String)map.get("CODE_ID");
					if(value.equals(Constant.CONFIRE_WAIT_TYPE)){
						tempConfireList.add(map);
					}else if(value.equals(Constant.CONFIRE_TURN_TYPE)){
						tempConfireList.add(map);
					}
//					else if(value.equals(Constant.CONFIRE_ACCEPTE_TYPE)){
//						tempConfireList.add(map);
//					}else if(value.equals(Constant.CONFIRE_CLOSE_TYPE)){
//						tempConfireList.add(map);
//					}
				}
				act.setOutData("seatAdminDepartList", commonUtilActions.getSeatAdminDepart());
				//判断是	业务部门处理还是经销商处理 业务部门为：ODepartment 经销商为：Dealer
				String pageName = CommonUtils.checkNull(request.getParamValue("pageName")); 

				//重设后的大区
				List<TmOrgPO> toList = new ArrayList<TmOrgPO>();
				
				CommonUtilDao commonUtilDao = new CommonUtilDao();
				List<Map<String, Object>> orgDepart = commonUtilDao.getUserOrgForDepart(logonUser.getUserId());	
				List<Map<String, Object>> org = commonUtilDao.getUserOrgForDealer(logonUser.getUserId());
				
				String orgid = "";
				for(Map<String, Object> map : orgDepart){
					if(orgid == ""){
						orgid = map.get("ORGID").toString();
					}else{
						orgid = orgid+"','"+map.get("ORGID").toString();
					}
				}
				for(Map<String, Object> map : org){
					if(orgid == ""){
						orgid = map.get("ORGID").toString();
					}else{
						orgid = orgid+"','"+map.get("ORGID").toString();
					}
				}
				
				
				//查询用户属于哪个大区
				for(TmOrgPO tmOrgPO : tmOrgList){
					if(orgid.contains( tmOrgPO.getOrgId().toString())){
						toList.add(tmOrgPO);
					}
				}
				//重设大区下的小区
				List<TmOrgPO> toSmallList = new ArrayList<TmOrgPO>();
				for(TmOrgPO tmOrgPO : smallTmOrgList){
					if(orgid.contains(tmOrgPO.getParentOrgId().toString())){
						toSmallList.add(tmOrgPO);
					}
				}
				
				//重设大区 缓冲SESSION
				act.setOutData("tmOrgList", toList);
				//重设大区下的小区  缓冲SESSION
//				act.setOutData("tmOrgSmallList", toSmallList);
				
				act.setOutData("pageName", pageName);
				act.setOutData("confireMessage", tempConfireList);
				act.setForword(ComplaintProcessingUpdate2);
			
			}
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理投诉查询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//大区投诉处理 处理界面
	public void waitComplaintSearchUpdate(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  	
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid"));  	
			String stauts = CommonUtils.checkNull(request.getParamValue("stauts"));  	
			
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			ComplaintConsultDao dao = ComplaintConsultDao.getInstance();
			Map<String,Object> complaintConsult = dao.queryComplaintConsult(cpid,ctmid);
			act.setOutData("complaintConsult", complaintConsult);
			act.setOutData("dealRecordList", complaintAcceptDao.queryDealRecord(Long.parseLong(cpid)));
			act.setOutData("returnRecordList", complaintAcceptDao.queryReturnRecord(Long.parseLong(cpid)));
			act.setOutData("verifyRecordList", complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid)));
			
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			List<TmOrgPO> tmOrgList = commonUtilActions.getTmOrgPO();
			//大区
			act.setOutData("tmOrgList", tmOrgList);
			List<TmOrgPO> smallTmOrgList = commonUtilActions.getSmallTmOrgPO();
			//小区
			act.setOutData("tmOrgSmallList", smallTmOrgList);
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//坐席
			//SeatsSetDao seatsSetDao = SeatsSetDao.getInstance();
			act.setOutData("ttCrmSeatsList",commonUtilActions.getSeatAdminDepart());
			List<Map<String, Object>> confireList = commonUtilActions.getTcCode(Constant.CONFIRE_TYPE);
			//确认意见数据
			List<Map<String, Object>> tempConfireList = new ArrayList<Map<String,Object>>();

			//处理中
			if(stauts.equals(Constant.COMPLAINT_STATUS_DOING) || stauts.equals(Constant.COMPLAINT_STATUS_DOING_REJECT)){
				for(Map<String, Object> map : confireList){
					String value = (String)map.get("CODE_ID");
					if(value.equals(Constant.CONFIRE_WAIT_TYPE)){
						tempConfireList.add(map);
					}else if(value.equals(Constant.CONFIRE_TURN_TYPE)){
						tempConfireList.add(map);
					}
//					else if(value.equals(Constant.CONFIRE_ACCEPTE_TYPE)){
//						tempConfireList.add(map);
//					}else if(value.equals(Constant.CONFIRE_CLOSE_TYPE)){
//						tempConfireList.add(map);
//					}
				}
				act.setOutData("seatAdminDepartList", commonUtilActions.getSeatAdminDepart());
				//判断是	业务部门处理还是经销商处理 业务部门为：ODepartment 经销商为：Dealer
				String pageName = CommonUtils.checkNull(request.getParamValue("pageName")); 

				//重设后的大区
				List<TmOrgPO> toList = new ArrayList<TmOrgPO>();
				
				CommonUtilDao commonUtilDao = new CommonUtilDao();
				List<Map<String, Object>> orgDepart = commonUtilDao.getUserOrgForDepart(logonUser.getUserId());	
				List<Map<String, Object>> org = commonUtilDao.getUserOrg(logonUser.getUserId());
				
				String orgid = "";
				for(Map<String, Object> map : orgDepart){
					if(orgid == ""){
						orgid = map.get("ORGID").toString();
					}else{
						orgid = orgid+"','"+map.get("ORGID").toString();
					}
				}
				for(Map<String, Object> map : org){
					if(orgid == ""){
						orgid = map.get("ORGID").toString();
					}else{
						orgid = orgid+"','"+map.get("ORGID").toString();
					}
				}
				
				
				//查询用户属于哪个大区
				for(TmOrgPO tmOrgPO : tmOrgList){
					if(orgid.contains( tmOrgPO.getOrgId().toString())){
						toList.add(tmOrgPO);
					}
				}
				//重设大区下的小区
				List<TmOrgPO> toSmallList = new ArrayList<TmOrgPO>();
				for(TmOrgPO tmOrgPO : smallTmOrgList){
					if(orgid.contains(tmOrgPO.getParentOrgId().toString())){
						toSmallList.add(tmOrgPO);
					}
				}
				
				//重设大区 缓冲SESSION
				act.setOutData("tmOrgList", toList);
				//重设大区下的小区  缓冲SESSION
				act.setOutData("tmOrgSmallList", toSmallList);
				
				act.setOutData("pageName", pageName);
				act.setOutData("confireMessage", tempConfireList);
				act.setForword(ComplaintProcessingUpdate);
			}
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理投诉查询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void waitComplaintUpdate(){
		try{	
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  				
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid"));  				
			String status = CommonUtils.checkNull(request.getParamValue("status"));  	
			String pageId = CommonUtils.checkNull(request.getParamValue("pageId")); 
			String openPage = CommonUtils.checkNull(request.getParamValue("openPage"));  	

			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			Map<String,Object> complaintAcceptMap = dao.queryComplaintInformation(Long.parseLong(cpid));
			SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm");			 
			//重置内容
			complaintAcceptMap.put("CPCONT", complaintAcceptMap.get("CPCONT").toString()+"  "+formate.format(new Date()));
			act.setOutData("complaintAcceptMap", complaintAcceptMap);
			act.setOutData("pageId", pageId);
			act.setOutData("openPage", openPage);
			act.setForword(WaitProcessComplaintUpdate);			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理投诉查询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void waitComplaintUpdateSubmit(){
		try{	
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  				
			String ctmname = CommonUtils.checkNull(request.getParamValue("ctmname"));  				
			String phone = CommonUtils.checkNull(request.getParamValue("phone"));  	
			String complaintContent = CommonUtils.checkNull(request.getParamValue("complaintContent"));  	

			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			ttCrmComplaintPO2.setCpName(ctmname);
			ttCrmComplaintPO2.setCpPhone(phone);
			ttCrmComplaintPO2.setCpContent(complaintContent);
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			
			String pageId = CommonUtils.checkNull(request.getParamValue("pageId")); 
			act.setOutData("pageId", pageId);
			
			act.setOutData("success", "true");
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理投诉查询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//业务类型
	private List<Map<String,Object>> getBizType(){
		List<Map<String,Object>> bizTypeList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", Constant.TYPE_COMPLAIN);
		map.put("name", Constant.TYPE_COMPLAIN_NAME);
		bizTypeList.add(map);
		map = new HashMap<String, Object>();
		map.put("id", Constant.TYPE_CONSULT);
		map.put("name", Constant.TYPE_CONSULT_NAME);
		bizTypeList.add(map);
		return bizTypeList;
	}
	
	//大区投诉处理
	@SuppressWarnings("unchecked")
	public void waitComplaintSearchUpdateSubmit01(){
		act.getResponse().setContentType("application/json");
		try{	
			//区别哪个页面操作
			String id = CommonUtils.checkNull(request.getParamValue("id"));  	
			//投诉咨询ID
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  	
			//确认意见
			String confirmView = CommonUtils.checkNull(request.getParamValue("confirmView"));  	
			//处理反馈内容
			String cdContent = CommonUtils.checkNull(request.getParamValue("cdContent"));  	
			//客户反馈内容	
			String crContent = CommonUtils.checkNull(request.getParamValue("crContent"));  
			//回访结果
			String crResult = CommonUtils.checkNull(request.getParamValue("crResult"));  
			//完结理由
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont"));  
			
			//坐席人员
			String ttCrmSeatsId = CommonUtils.checkNull(request.getParamValue("ttCrmSeatsId"));  
			//处理人所属大区
			String dealorg = CommonUtils.checkNull(request.getParamValue("orgObj"));//所在大区
			String vehicleCompany = CommonUtils.checkNull(request.getParamValue("vehicleCompany"));
			String tmOrg = CommonUtils.checkNull(request.getParamValue("tmOrg"));
			
			
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			
			//投诉咨询
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			
			TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)complaintAcceptDao.select(ttCrmComplaintPO1).get(0);
			
			//投诉咨询处理记录
			TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintDealRecordPO.setCdId(new Long(SequenceManager.getSequence("")));
			ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
			ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCreateDate(new Date());
			ttCrmComplaintDealRecordPO.setCdUserProcess(Integer.parseInt(confirmView));
			ttCrmComplaintDealRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDealRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealOrg());
			
			if(confirmView.equals(Constant.CONFIRE_WAIT_TYPE)){
				//设置投诉状态为待处理
				//ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT));
				int stauts = ((TtCrmComplaintPO)complaintAcceptDao.select(ttCrmComplaintPO1).get(0)).getCpStatus();
				ttCrmComplaintPO2.setCpStatus(stauts);
				//如果是大区 就交给自己
				if(StringUtil.notNull(dealorg)){
					ttCrmComplaintPO2.setCpDealOrg(Long.parseLong(dealorg));
					ttCrmComplaintPO2.setCpDealDealer(-1L);
					
					ttCrmComplaintPO2.setCpDealUser(Long.parseLong(dealorg));
					ttCrmComplaintPO2.setCpDealUserName(commonUtilActions.getDealUserName(Long.parseLong(dealorg)));
					
					ttCrmComplaintDealRecordPO.setCpDealOrg(Long.parseLong(dealorg));
					ttCrmComplaintDealRecordPO.setCpDealDealer(-1L);
					ttCrmComplaintDealRecordPO.setCpNextDealId(Long.parseLong(dealorg));
				}
				//如果是经销商则记录经销商
				if(StringUtil.notNull(vehicleCompany)){
					ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_DEALER_STATUS_DOING));
					ttCrmComplaintPO2.setCpDealDealer(Long.parseLong(vehicleCompany));
					
					ttCrmComplaintPO2.setCpDealUser(Long.parseLong(vehicleCompany));
					ttCrmComplaintPO2.setCpDealUserName(commonUtilActions.getDealUserName(Long.parseLong(vehicleCompany)));
					
					ttCrmComplaintDealRecordPO.setCpDealDealer(Long.parseLong(vehicleCompany));
					ttCrmComplaintDealRecordPO.setCpNextDealId(Long.parseLong(vehicleCompany));
				}
				
				if(StringUtil.notNull(ttCrmSeatsId))ttCrmComplaintPO2.setCpDealUser(Long.parseLong(ttCrmSeatsId));
				
				ttCrmComplaintDealRecordPO.setCpStatus(stauts);
			}else if(confirmView.equals(Constant.CONFIRE_TURN_TYPE)){
				//设置投诉状态为处理中
				ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING_ALREADY));
				ttCrmComplaintPO2.setCpDealUser(Long.parseLong(tmOrg));
				ttCrmComplaintPO2.setCpDealUserName(commonUtilActions.getDealUserName(Long.parseLong(tmOrg)));
				//ttCrmComplaintPO2.setCpTurnUser(logonUser.getUserId());
				//ttCrmComplaintPO2.setCpTurnDate(new Date());
				ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING_ALREADY));
				ttCrmComplaintDealRecordPO.setCpNextDealId(Long.parseLong(tmOrg));
			}
			
		
			if(id.equals("waitProcess")){
				complaintAcceptDao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
				complaintAcceptDao.insert(ttCrmComplaintDealRecordPO);
			}else if(id.equals("processing")){
				ttCrmComplaintDealRecordPO.setCpContent(cdContent);
				complaintAcceptDao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
				complaintAcceptDao.insert(ttCrmComplaintDealRecordPO);
				//处理中 区别哪个页面跳转
				String pageName = CommonUtils.checkNull(request.getParamValue("pageName"));  	
				act.setOutData("pageName", pageName);
			}

			act.setOutData("success", "true");
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理投诉查询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//经销商投诉处理
	public void waitComplaintSearchUpdateSubmit(){
		act.getResponse().setContentType("application/json");
		try{	
			//区别哪个页面操作
			String id = CommonUtils.checkNull(request.getParamValue("id"));  	
			//投诉咨询ID
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  	
			//确认意见
			String confirmView = CommonUtils.checkNull(request.getParamValue("confirmView"));  	
			//处理反馈内容
			String cdContent = CommonUtils.checkNull(request.getParamValue("cdContent"));  	
			//客户反馈内容	
			String crContent = CommonUtils.checkNull(request.getParamValue("crContent"));  
			//回访结果
			String crResult = CommonUtils.checkNull(request.getParamValue("crResult"));  
			//完结理由
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont"));  
			
			String ttCrmSeatsId = CommonUtils.checkNull(request.getParamValue("ttCrmSeatsId"));  
			//处理人所属大区
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			
			//投诉咨询
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			
			TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)complaintAcceptDao.select(ttCrmComplaintPO1).get(0);
			
			//投诉咨询处理记录
			TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintDealRecordPO.setCdId(new Long(SequenceManager.getSequence("")));
			ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
			ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCreateDate(new Date());
			ttCrmComplaintDealRecordPO.setCdUserProcess(Integer.parseInt(confirmView));
			ttCrmComplaintDealRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDealRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealOrg());
			
			int stauts = ((TtCrmComplaintPO)complaintAcceptDao.select(ttCrmComplaintPO1).get(0)).getCpStatus();
			if(confirmView.equals(Constant.CONFIRE_WAIT_TYPE)){
				//设置投诉状态为待处理
				ttCrmComplaintPO2.setCpStatus(stauts);
				ttCrmComplaintDealRecordPO.setCpNextDealId(ttCrmComplaintPO3.getCpDealDealer());
				ttCrmComplaintDealRecordPO.setCpStatus(stauts);
			}else if(confirmView.equals(Constant.CONFIRE_TURN_TYPE) ){
				//设置投诉状态为处理中
				CommonUtilDao commonUtilDao = new CommonUtilDao();
				//List<Map<String,Object>>  org2 = commonUtilDao.getDealerParOrg(logonUser.getDealerId());
				//Map map2 = org2.get(0);
				ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING));
				ttCrmComplaintPO2.setCpDealUser(ttCrmComplaintPO3.getCpDealOrg());
				ttCrmComplaintPO2.setCpDealUserName(commonUtilActions.getDealUserName(ttCrmComplaintPO3.getCpDealOrg()));
				//ttCrmComplaintPO2.setCpDealUser(Long.valueOf(map2.get("ORGID").toString()));
				//ttCrmComplaintPO2.setCpTurnUser(logonUser.getUserId());
				//ttCrmComplaintPO2.setCpTurnDate(new Date());
				//-1代表空
				ttCrmComplaintPO2.setCpDealDealer(-1L);
				ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING));
				ttCrmComplaintDealRecordPO.setCpDealDealer(-1L);
				ttCrmComplaintDealRecordPO.setCpNextDealId(ttCrmComplaintPO3.getCpDealOrg());
				
			}
			
		
			if(id.equals("waitProcess")){
				complaintAcceptDao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
				complaintAcceptDao.insert(ttCrmComplaintDealRecordPO);
			}else if(id.equals("processing")){
				
				ttCrmComplaintDealRecordPO.setCpContent(cdContent);
				complaintAcceptDao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
				complaintAcceptDao.insert(ttCrmComplaintDealRecordPO);
				//处理中 区别哪个页面跳转
				String pageName = CommonUtils.checkNull(request.getParamValue("pageName"));  	
				act.setOutData("pageName", pageName);
			}

			act.setOutData("success", "true");
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理投诉查询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 艾春 9.27 添加 所有投诉转咨询初始化 操作
	 */
	public void complaintTurnAdviceUpdate(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  				
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid"));  				
			act.setOutData("cpid", cpid);
			act.setOutData("ctmid", ctmid);
			
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			Map<String,Object> complaintAcceptMap = dao.queryComplaintInfo(Long.parseLong(cpid));
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//重置处理期限
			if(complaintAcceptMap.get("CPLIMIT")!=null){
				complaintAcceptMap.put("CPLIMIT", commonUtilActions.turnDayToCodeForComplaintLimit(complaintAcceptMap.get("CPLIMIT").toString()));
			}
			
			//业务类型
			act.setOutData("bizTypeList", getBizType());
			//内容类型
			act.setOutData("bclist", commonUtilActions.getTcCode(Constant.TYPE_COMPLAIN));
			//抱怨对象 大区
			act.setOutData("cpObjectOrgList", commonUtilActions.getTmOrgPO());
			CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
			
			//抱怨对象 小区
			if(null != complaintAcceptMap.get("COBJ")){
				act.setOutData("cpObjectSmallOrgList", commonUtilDao.cascadeOrgSmallOrg(Long.parseLong(complaintAcceptMap.get("COBJ").toString())));
			}else{
				act.setOutData("cpObjectSmallOrgList",null);
			}
			
			//抱怨对象 经销商
			if(null != complaintAcceptMap.get("SOBJ")){
				act.setOutData("vcObjList", commonUtilDao.cascadeOrgDealer(Long.parseLong(complaintAcceptMap.get("SOBJ").toString())));
			}else{
				act.setOutData("vcObjList", null);
			}
			
			
			//处理方式
			List<Map<String, Object>> dealModelLists = commonUtilActions.getTcCode(Constant.COMPLAINT_PROCESS.toString());
			act.setOutData("dealModelList", dealModelLists);
			//大区
			//act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			act.setOutData("tmOrgList", commonUtilActions.getAllOrg());
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//小区
			//act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			act.setOutData("complaintAcceptMap", complaintAcceptMap);
			
			act.setForword(ComplaintTurnAdviceUpdate);

		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"所有投诉转咨询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 艾春 9.27 添加 所有投诉转咨询提交 操作
	 */
	public void complaintTurnAdviceUpdateSubmit(){
		act.getResponse().setContentType("application/json");
		String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));
		//标识哪个页面的提交处理
		String id = CommonUtils.checkNull(request.getParamValue("id"));
		
		ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
		//更新投诉咨询相关内容
		TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
		TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
		ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
		
		ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
		ttCrmComplaintPO2.setUpdateDate(new Date());
		
		TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)dao.select(ttCrmComplaintPO1).get(0);
		
		//插入投诉咨询回复记录
		TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
		ttCrmComplaintDealRecordPO.setCdDate(new Date());
		ttCrmComplaintDealRecordPO.setCdId(new Long(SequenceManager.getSequence("")));
		ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
		ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
		ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
		ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
		ttCrmComplaintDealRecordPO.setCreateDate(new Date());
		ttCrmComplaintDealRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
		ttCrmComplaintDealRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealDealer());
		CommonUtilActions commonUtilActions = new CommonUtilActions();
		try{			
			String bizType = CommonUtils.checkNull(request.getParamValue("bizType"));  				
			String contType = CommonUtils.checkNull(request.getParamValue("contType"));  				
			String cplevel = CommonUtils.checkNull(request.getParamValue("cplevel"));  	
			String cplimit = CommonUtils.checkNull(request.getParamValue("cplimit"));  
			String cpObject = CommonUtils.checkNull(request.getParamValue("cpObject")); //报怨所属大区
			String vcPro = CommonUtils.checkNull(request.getParamValue("vcPro")); //报怨所属小区
			String vehicleCompany = CommonUtils.checkNull(request.getParamValue("vehicleCompany")); //报怨经销商  
			String complaintContent = CommonUtils.checkNull(request.getParamValue("complaintContent"));  				
			String dealModel = CommonUtils.checkNull(request.getParamValue("dealModel"));  				
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont")); 
			String tmOrg = CommonUtils.checkNull(request.getParamValue("cpObj"));  				

			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintPO2.setCpBizType(Integer.parseInt(bizType));
			ttCrmComplaintPO2.setCpBizContent(Integer.parseInt(contType));
			if(!"".equals(cplevel)) ttCrmComplaintPO2.setCpLevel(Integer.parseInt(cplevel));
			if(!"".equals(cplimit)){
				ttCrmComplaintPO2.setCpLimit(commonUtilActions.turnCodeToDayForComplaintLimit(cplimit));
			}
			if(!"".equals(cpObject))ttCrmComplaintPO2.setCpObjectOrg(Long.parseLong(cpObject));
			if(!"".equals(vcPro))ttCrmComplaintPO2.setCpObjectSmallOrg(Long.parseLong(vcPro));
			if(!"".equals(vehicleCompany))ttCrmComplaintPO2.setCpObject(Long.parseLong(vehicleCompany));
			//if(!"".equals(dealuser))ttCrmComplaintPO2.setCpObject(Long.parseLong(dealuser));
			ttCrmComplaintPO2.setCpContent(complaintContent);
			ttCrmComplaintPO2.setCpDealMode(Integer.parseInt(dealModel));

			
			ttCrmComplaintDealRecordPO.setCpContent(ccont);

			if(bizType.equals(Constant.TYPE_COMPLAIN)){
				//投诉
				if(dealModel.equals(Constant.COMPLAINT_PROCESS_TURN.toString())){
					//处理中
					ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING));
					ttCrmComplaintPO2.setCpDealUser(Long.parseLong(tmOrg));
					ttCrmComplaintPO2.setCpDealUserName(commonUtilActions.getDealUserName(Long.parseLong(tmOrg)));
					//记录哪个大区处理
					ttCrmComplaintPO2.setCpDealOrg(Long.parseLong(tmOrg));
					ttCrmComplaintDealRecordPO.setCpDealOrg(Long.parseLong(tmOrg));
					ttCrmComplaintDealRecordPO.setCpNextDealId(Long.parseLong(tmOrg));
				}
			}else if(bizType.equals(Constant.TYPE_CONSULT)){
				//咨询
				//咨询
				ttCrmComplaintPO2.setCpNo("ZX"+cpid);
				//设置版本号
				ttCrmComplaintPO2.setVar(0);
				//已处理
				if(dealModel.equals(Constant.CONSULT_PROCESS_FINISH.toString())){
					ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_FINISH));
					ttCrmComplaintPO2.setCpDealUser(logonUser.getUserId());
					ttCrmComplaintPO2.setCpDealUserName(logonUser.getName());
					ttCrmComplaintDealRecordPO.setCpContent(ccont);
					ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_FINISH));
				//待处理
				}else if(dealModel.equals(Constant.CONSULT_PROCESS_WAIT.toString())){
					ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_WAIT));
					ttCrmComplaintPO2.setCpDealUser(ttCrmComplaintPO3.getCpAccUser());
					ttCrmComplaintPO2.setCpDealUserName(commonUtilActions.getDealUserName(ttCrmComplaintPO3.getCpAccUser()));
					ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_WAIT));
				}
			}
			
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			dao.insert(ttCrmComplaintDealRecordPO);
			act.setOutData("success", "true");
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理咨询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 艾春 9.27 添加满意关闭 初始化信息
	 */
	public void complaintSatisfyCloseUpdate(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  				
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid"));  				
			act.setOutData("cpid", cpid);
			act.setOutData("ctmid", ctmid);
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			ComplaintConsultDao dao = ComplaintConsultDao.getInstance();
			Map<String,Object> complaintConsult = dao.queryComplaintConsult(cpid,ctmid);
			act.setOutData("complaintConsult", complaintConsult);
			act.setOutData("dealRecordList", complaintAcceptDao.queryDealRecord(Long.parseLong(cpid)));
			act.setOutData("returnRecordList", complaintAcceptDao.queryReturnRecord(Long.parseLong(cpid)));
			act.setOutData("verifyRecordList", complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid)));
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			List<TmOrgPO> tmOrgList = commonUtilActions.getTmOrgPO();
			//大区
			act.setOutData("tmOrgList", tmOrgList);
			List<TmOrgPO> smallTmOrgList = commonUtilActions.getSmallTmOrgPO();
			//小区
			act.setOutData("tmOrgSmallList", smallTmOrgList);
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			act.setOutData("seatAdminDepartList", commonUtilActions.getSeatAdminDepart());
			//服务营销处大区
			//act.setOutData("seatAdminDepartList", commonUtilActions.getSeatAdminDepart());
			act.setForword(complaintSatisfyCloseUpdate);
			
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"坐席已回访审核投诉查询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 艾春 9.27 添加满意关闭 提交信息
	 */
	public void complaintSatisfyCloseUpdateSubmit(){
		act.getResponse().setContentType("application/json");
		String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));
		
		ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
		//更新投诉咨询相关内容
		TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
		TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
		ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
		
		ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
		ttCrmComplaintPO2.setUpdateDate(new Date());
		
		TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)dao.select(ttCrmComplaintPO1).get(0);
		if(ttCrmComplaintPO3!=null && (ttCrmComplaintPO3.getCpObject()==null ||ttCrmComplaintPO3.getCpObject()==0)){
			act.setOutData("success", "0");
			return;
		}
		
		//插入投诉咨询回复记录
		TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
		ttCrmComplaintDealRecordPO.setCdDate(new Date());
		ttCrmComplaintDealRecordPO.setCdId(new Long(SequenceManager.getSequence("")));
		ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
		ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
		ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
		ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
		ttCrmComplaintDealRecordPO.setCreateDate(new Date());
		ttCrmComplaintDealRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
		ttCrmComplaintDealRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealDealer());
		
		String ccont = CommonUtils.checkNull(request.getParamValue("ccont"));//备注
		ttCrmComplaintDealRecordPO.setCdDate(new Date());
		
		TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO1 = new TtCrmComplaintDealRecordPO();
		ttCrmComplaintDealRecordPO1.setCdDate(new Date());
		ttCrmComplaintDealRecordPO1.setCdId(new Long(SequenceManager.getSequence("")));
		ttCrmComplaintDealRecordPO1.setCdUser(logonUser.getName());
		ttCrmComplaintDealRecordPO1.setCdUserId(logonUser.getUserId());
		ttCrmComplaintDealRecordPO1.setCpId(Long.parseLong(cpid));
		ttCrmComplaintDealRecordPO1.setCreateBy(logonUser.getUserId());
		ttCrmComplaintDealRecordPO1.setCreateDate(new Date());
		ttCrmComplaintDealRecordPO1.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
		ttCrmComplaintDealRecordPO1.setCpDealDealer(ttCrmComplaintPO3.getCpDealDealer());
		ttCrmComplaintDealRecordPO1.setCdDate(new Date());
		
		try{
			//满意 审核通过
			//关闭状态
			ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_ALREADY_CLOSE));
			ttCrmComplaintPO2.setCpCloseUser(logonUser.getUserId());
			ttCrmComplaintPO2.setCpCloseDate(new Date());
			ttCrmComplaintPO2.setCpSf(Integer.parseInt(Constant.PLEASED));
			//查询是否有管理员驳回的数据
			List<Map<String,Object>> rejectlist = dao.queryRejectDealRecord(ttCrmComplaintPO3.getCpId());
			if(rejectlist!=null && rejectlist.size()>0){
				ttCrmComplaintPO2.setCpIsOnceSf(Constant.IF_TYPE_NO);
			}else{
				ttCrmComplaintPO2.setCpIsOnceSf(Constant.IF_TYPE_YES);
			}
			ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING_ALREADY));
			ttCrmComplaintDealRecordPO.setCpContent("满意关闭");
			
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			List<Map<String,Object>> alreadylist = dao.queryAlreadyDealRecord(ttCrmComplaintPO3.getCpId());
			if(alreadylist ==null || alreadylist.size()== 0){
				dao.insert(ttCrmComplaintDealRecordPO);
			}
			ttCrmComplaintDealRecordPO1.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_ALREADY_CLOSE));
			ttCrmComplaintDealRecordPO1.setCpContent(ccont);
			dao.insert(ttCrmComplaintDealRecordPO1);
			act.setOutData("success", "true");
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理咨询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
