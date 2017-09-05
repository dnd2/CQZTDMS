package com.infodms.dms.actions.customerRelationships.complaintConsult;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;
import com.infodms.dms.actions.customerRelationships.baseSetting.seatsSet;
import com.infodms.dms.actions.customerRelationships.baseSetting.typeSet;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptDao;
import com.infodms.dms.dao.customerRelationships.ComplaintConsultDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtCrmComplaintDealRecordPO;
import com.infodms.dms.po.TtCrmComplaintDelayRecordPO;
import com.infodms.dms.po.TtCrmComplaintPO;
import com.infodms.dms.po.TtCrmComplaintReturnRecordPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 投诉查询ACTIONS
 * @ClassName     : ComplaintSearch 
 * @Description   : 投诉查询
 * @author        : wangming
 * CreateDate     : 2013-5-7
 */
public class ComplaintSearch {
	private static Logger logger = Logger.getLogger(ComplaintSearch.class);
	//投诉查询页面
	private final String ComplaintSearch = "/jsp/customerRelationships/complaintConsult/complaintSearch.jsp";
	//待处理管理员页面
	private final String ComplaintSearchUpdate = "/jsp/customerRelationships/complaintConsult/complaintSearchUpdate.jsp";
	//大区已处理管理员页面
	private final String AdminInforDetailComplaint = "/jsp/customerRelationships/complaintConsult/complaintSearchDetailUpdate.jsp";
	//大区已回访管理员页面
	private final String AdminCustomerReturnComplaint = "/jsp/customerRelationships/complaintConsult/complaintSearchReturnUpdate.jsp";
	//大区已审核强制关闭 管理员审核页面
	private final String AdminAplayVerifyComplaint = "/jsp/customerRelationships/complaintConsult/complaintAplayVerifyUpdate.jsp";
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	//投诉受理初始化
	public void complaintSearchInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//省份 艾春9.23修改
//			act.setOutData("proviceList", commonUtilActions.getProvice());
			//大区
			act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//报怨类型
			typeSet ts = new typeSet();
			act.setOutData("bclist", ts.getTypeSelect(Constant.TYPE_COMPLAIN));
			//车型
			act.setOutData("modelList", commonUtilActions.getAllModel());
			
			//处理特殊添加工单状态
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("CODE_ID", "unClose");
			map.put("CODE_DESC", "未关闭");
			List<Map<String, Object>> statuslist =commonUtilActions.getTcCode(Constant.COMPLAINT_STATUS);
			statuslist.add(map);
			act.setOutData("statuslist",statuslist);
			
			act.setForword(ComplaintSearch);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryComplaintSearch(){
		act.getResponse().setContentType("application/json");
		try{
			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
			String name = CommonUtils.checkNull(request.getParamValue("name"));  				
			String tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  	
			String dealUser = CommonUtils.checkNull(request.getParamValue("dealUser"));  	
			String accUser = CommonUtils.checkNull(request.getParamValue("accUser"));  	
			String biztype = CommonUtils.checkNull(request.getParamValue("biztype"));  				
			String status = CommonUtils.checkNull(request.getParamValue("status"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));
			String region = CommonUtils.checkNull(request.getParamValue("region"));  	
			String pro = CommonUtils.checkNull(request.getParamValue("pro"));  				
			String checkStart = CommonUtils.checkNull(request.getParamValue("checkStart"));  	
			String checklEnd = CommonUtils.checkNull(request.getParamValue("checklEnd")); 
			String delaystatus = CommonUtils.checkNull(request.getParamValue("delaystatus")); 
			String repeatComplaint = CommonUtils.checkNull(request.getParamValue("repeatComplaint")); 	
			String cpNo = CommonUtils.checkNull(request.getParamValue("cpNo"));
			String CP_START_DATE = CommonUtils.checkNull(request.getParamValue("CP_START_DATE")); 	
			String CP_END_DATE = CommonUtils.checkNull(request.getParamValue("CP_END_DATE"));
			// 艾春 2013.11.27 添加需考核日期
			String KH_START_DATE = CommonUtils.checkNull(request.getParamValue("KH_START_DATE")); 	
			String KH_END_DATE = CommonUtils.checkNull(request.getParamValue("KH_END_DATE"));				
			String is_close = CommonUtils.checkNull(request.getParamValue("is_close")); 	
			String is_one_close = CommonUtils.checkNull(request.getParamValue("is_one_close"));
			String model = CommonUtils.checkNull(request.getParamValue("model"));
			
			seatsSet seat = new seatsSet();
			boolean isAdmin = seat.isAdmin(logonUser.getUserId());
			
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> complaintAcceptData = dao.queryComplaintInfo(cpNo,vin,name,tele,level,dealUser,accUser,biztype,
					status,dealStart,dealEnd,dateStart,dateEnd,region,pro,checkStart,checklEnd,isAdmin,CP_START_DATE,CP_END_DATE,KH_START_DATE,KH_END_DATE,
					logonUser.getUserId(),delaystatus,repeatComplaint,is_close,is_one_close,model,Constant.PAGE_SIZE,curPage);
			if(complaintAcceptData!=null && complaintAcceptData.getTotalRecords()>0){
				for(Map map :complaintAcceptData.getRecords()){
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
	
	
	public void complaintSearchUpdate(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  				
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid"));  				
			String status = CommonUtils.checkNull(request.getParamValue("status"));  	
			String clstatus = CommonUtils.checkNull(request.getParamValue("clstatus"));  	
			act.setOutData("cpid", cpid);
			act.setOutData("ctmid", ctmid);
			
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			List<Map<String, Object>> verifyRecordList =  complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid));
			String CPDEFERTYPE = "";
			if(verifyRecordList!=null && verifyRecordList.size()>0) 
				CPDEFERTYPE = ((BigDecimal)verifyRecordList.get(0).get("CPDEFERTYPE")).toString();
			//副总审核通过并且强制关闭
			if(clstatus.equals(Constant.PASS_Manager_03.toString()) && CPDEFERTYPE.equals("94081002")){
				try{				
					
					
					ComplaintConsultDao dao = ComplaintConsultDao.getInstance();
					Map<String,Object> complaintConsult = dao.queryComplaintConsult(cpid,ctmid);
					act.setOutData("complaintConsult", complaintConsult);
					act.setOutData("dealRecordList", complaintAcceptDao.queryDealRecord(Long.parseLong(cpid)));
					act.setOutData("returnRecordList", complaintAcceptDao.queryReturnRecord(Long.parseLong(cpid)));
					act.setOutData("verifyRecordList", complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid)));
					act.setOutData("logonUserID", logonUser.getUserId());//增加当前用户可以修改来电信息
					CommonUtilActions commonUtilActions = new CommonUtilActions();
					List<TmOrgPO> tmOrgList = commonUtilActions.getTmOrgPO();
					//大区
					act.setOutData("tmOrgList", tmOrgList);
					List<TmOrgPO> smallTmOrgList = commonUtilActions.getSmallTmOrgPO();
					//小区
					act.setOutData("tmOrgSmallList", smallTmOrgList);
					//省份
					act.setOutData("proviceList", commonUtilActions.getProvice());
					//服务营销处大区
					//act.setOutData("seatAdminDepartList", commonUtilActions.getSeatAdminDepart());
					act.setForword(AdminAplayVerifyComplaint);
					
				}catch(Exception e) {
					BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"坐席已回访审核投诉查询处理");
					logger.error(logonUser,e1);
					act.setException(e1);
				}
			}else{//待处理
				if(status.equals(Constant.COMPLAINT_STATUS_WAIT)){
					
					/*****************针对初始未处理的来电，将投诉内容初始录入****2014-04-22***********************/
					ComplaintConsultDao cDao = ComplaintConsultDao.getInstance();
					Map<String,Object> complaintConsult = cDao.queryComplaintConsult(cpid,ctmid);
					act.setOutData("complaintConsult", complaintConsult);
					act.setOutData("dealRecordList", complaintAcceptDao.queryDealRecord(Long.parseLong(cpid)));
					act.setOutData("returnRecordList", complaintAcceptDao.queryReturnRecord(Long.parseLong(cpid)));
					act.setOutData("verifyRecordList", complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid)));
					/*****************针对初始未处理的来电，将投诉内容初始录入****2014-04-22***********************/
					
					
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
					//抱怨对象
					act.setOutData("cpObjectList", commonUtilActions.getOfficeOrg());
					
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
					
					act.setOutData("logonUserID", logonUser.getUserId());//增加当前用户可以修改来电信息
					//处理方式
					List<Map<String, Object>> dealModelLists = commonUtilActions.getTcCode(Constant.COMPLAINT_PROCESS.toString());
					act.setOutData("dealModelList", dealModelLists);
					//大区
					//act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
					act.setOutData("tmOrgList", commonUtilActions.getAllOrg());
					//省份
					act.setOutData("proviceList", commonUtilActions.getProvice());
					//城市
					act.setOutData("cityList", commonUtilActions.getAllCity());
					//小区
					//act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
					act.setOutData("complaintAcceptMap", complaintAcceptMap);
					
					act.setForword(ComplaintSearchUpdate);
				}else if(status.equals(Constant.COMPLAINT_STATUS_DOING_ALREADY) ){
					try{//大区已处理				
					
						ComplaintConsultDao dao = ComplaintConsultDao.getInstance();
						Map<String,Object> complaintConsult = dao.queryComplaintConsult(cpid,ctmid);
						act.setOutData("complaintConsult", complaintConsult);
						act.setOutData("dealRecordList", complaintAcceptDao.queryDealRecord(Long.parseLong(cpid)));
						act.setOutData("returnRecordList", complaintAcceptDao.queryReturnRecord(Long.parseLong(cpid)));
						act.setOutData("verifyRecordList", complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid)));
						act.setOutData("logonUserID", logonUser.getUserId());//增加当前用户可以修改来电信息
						CommonUtilActions commonUtilActions = new CommonUtilActions();
						List<TmOrgPO> tmOrgList = commonUtilActions.getTmOrgPO();
						//大区
						act.setOutData("tmOrgList", tmOrgList);
						List<TmOrgPO> smallTmOrgList = commonUtilActions.getSmallTmOrgPO();
						//小区
						act.setOutData("tmOrgSmallList", smallTmOrgList);
						//省份
						act.setOutData("proviceList", commonUtilActions.getProvice());
						//服务营销处大区
						act.setOutData("seatAdminDepartList", commonUtilActions.getSeatAdminDepart());
						

						act.setForword(AdminInforDetailComplaint);


						
					} catch(Exception e) {
						BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"大区已处理审核投诉查询处理");
						logger.error(logonUser,e1);
						act.setException(e1);
					}
				}else if(status.equals(Constant.COMPLAINT_STATUS_WAIT_CLOSE)){
					try{//坐席回访已处理				
						
						ComplaintConsultDao dao = ComplaintConsultDao.getInstance();
						Map<String,Object> complaintConsult = dao.queryComplaintConsult(cpid,ctmid);
						act.setOutData("complaintConsult", complaintConsult);
						act.setOutData("dealRecordList", complaintAcceptDao.queryDealRecord(Long.parseLong(cpid)));
						act.setOutData("returnRecordList", complaintAcceptDao.queryReturnRecord(Long.parseLong(cpid)));
						act.setOutData("verifyRecordList", complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid)));
						act.setOutData("logonUserID", logonUser.getUserId());//增加当前用户可以修改来电信息
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
						act.setForword(AdminCustomerReturnComplaint);
						
					}catch(Exception e) {
						BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"坐席已回访审核投诉查询处理");
						logger.error(logonUser,e1);
						act.setException(e1);
					}
				}
			}	
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉查询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void ComplaintSearchUpdateSubmit(){
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
		//待审核的管理员审核
		if(id == null || id == ""){
			try{	
				String pro = CommonUtils.checkNull(request.getParamValue("pro"));
				String citysel = CommonUtils.checkNull(request.getParamValue("citysel"));
				String bizType = CommonUtils.checkNull(request.getParamValue("bizType"));  				
				String contType = CommonUtils.checkNull(request.getParamValue("contType"));  				
				String cplevel = CommonUtils.checkNull(request.getParamValue("cplevel"));  	
				String cplimit = CommonUtils.checkNull(request.getParamValue("cplimit")); 
				String cpObject1 = CommonUtils.checkNull(request.getParamValue("cpObject1")); //报怨处
				String cpObject = CommonUtils.checkNull(request.getParamValue("cpObject")); //报怨所属大区
				String vcPro = CommonUtils.checkNull(request.getParamValue("vcPro")); //报怨所属小区
				String vehicleCompany = CommonUtils.checkNull(request.getParamValue("vehicleCompany")); //报怨经销商  
				String complaintContent = CommonUtils.checkNull(request.getParamValue("complaintContent"));  				
				String dealModel = CommonUtils.checkNull(request.getParamValue("dealModel"));  				
				String ccont = CommonUtils.checkNull(request.getParamValue("ccont")); 
				String tmOrg = CommonUtils.checkNull(request.getParamValue("cpObj"));  				

				ttCrmComplaintDealRecordPO.setCdDate(new Date());
				ttCrmComplaintPO2.setCpProvinceId(Long.parseLong(pro));
				ttCrmComplaintPO2.setCpCityId(Long.parseLong(citysel));
				ttCrmComplaintPO2.setCpBizType(Integer.parseInt(bizType));
				ttCrmComplaintPO2.setCpBizContent(Integer.parseInt(contType));
				if(!"".equals(cplevel)) ttCrmComplaintPO2.setCpLevel(Integer.parseInt(cplevel));
				if(!"".equals(cplimit)){
					
					ttCrmComplaintPO2.setCpLimit(commonUtilActions.turnCodeToDayForComplaintLimit(cplimit));
				}
				
				
				if(!"".equals(vcPro))ttCrmComplaintPO2.setCpObjectSmallOrg(Long.parseLong(vcPro));
				if(!"".equals(vehicleCompany))ttCrmComplaintPO2.setCpObject(Long.parseLong(vehicleCompany));
				if(!"".equals(cpObject1))ttCrmComplaintPO2.setCpObject(Long.parseLong(cpObject1));
				
				if(!"".equals(vcPro) && !"".equals(vehicleCompany) ){
					if(!"".equals(cpObject))ttCrmComplaintPO2.setCpObjectOrg(Long.parseLong(cpObject));
					cpObject = dao.queryPid(Long.parseLong(vcPro)).get(0).get("PARENT_ORG_ID").toString();
					if(!"".equals(cpObject))ttCrmComplaintPO2.setCpObjectOrg(Long.parseLong(cpObject));
				}
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
						
						ttCrmComplaintDealRecordPO.setCpNextDealId(Long.parseLong(tmOrg));
						if(ttCrmComplaintPO3.getCpObjectOrg() == null || "".equals(ttCrmComplaintPO3.getCpObjectOrg()) || ttCrmComplaintPO3.getCpObjectOrg() == 0){
							ttCrmComplaintPO2.setCpObjectOrg(Long.parseLong(tmOrg));
						}
						ttCrmComplaintDealRecordPO.setCpDealOrg(Long.parseLong(tmOrg));
						ttCrmComplaintPO2.setCpTurnUser(logonUser.getUserId());
						ttCrmComplaintPO2.setCpTurnDate(new Date());
					}
				}else if(bizType.equals(Constant.TYPE_CONSULT)){
					//咨询
					//咨询 艾春 2013.11.29 修改投诉转咨询的单号
//					ttCrmComplaintPO2.setCpNo("ZX"+cpid);
					ttCrmComplaintPO2.setCpNo(Utility.GetBillNo("", "D", "ZX"));
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
						ttCrmComplaintPO2.setCpDealUserName(logonUser.getName());
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
		}else if(id.equals("doingAlready")){
			String verifyRadio = CommonUtils.checkNull(request.getParamValue("verifyRadio"));//审核状态
			String orgObj = CommonUtils.checkNull(request.getParamValue("orgObj"));//所属部门
			String userid = CommonUtils.checkNull(request.getParamValue("userid"));//坐席人员
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont"));//备注

			ttCrmComplaintDealRecordPO.setCdDate(new Date());

			//审核通过
			if(verifyRadio.equals(Constant.ADMIN_VERIFY_SUCCESS.toString())){
				//转客户回访 为待反馈
				ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT_FEEDBACK));
				ttCrmComplaintPO2.setCpDealUser(Long.parseLong(userid));
				ttCrmComplaintPO2.setCpDealUserName(commonUtilActions.getDealUserName(Long.parseLong(userid)));
				//设置指定回访人
				ttCrmComplaintPO2.setCpCrUserId(Long.parseLong(userid));
				ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT_FEEDBACK));
				ttCrmComplaintDealRecordPO.setCpNextDealId(Long.parseLong(userid));
			//审核不通过
			}else if(verifyRadio.equals(Constant.ADMIN_VERIFY_FAIL.toString())){
				//驳回
				ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING_REJECT));
				//记录哪个大区处理
				ttCrmComplaintPO2.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
				ttCrmComplaintPO2.setCpDealUser(ttCrmComplaintPO3.getCpDealOrg());
				ttCrmComplaintPO2.setCpDealUserName(commonUtilActions.getDealUserName(ttCrmComplaintPO3.getCpDealOrg()));
				ttCrmComplaintPO2.setCpDealDealer(-1L);
				
				ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING_REJECT));
				ttCrmComplaintDealRecordPO.setCpNextDealId(ttCrmComplaintPO3.getCpDealOrg());
			}
			ttCrmComplaintDealRecordPO.setCpContent(ccont);
			
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			dao.insert(ttCrmComplaintDealRecordPO);
			act.setOutData("success", "true");
		}else if(id.equals("customerReturn")){
			String orgObj = CommonUtils.checkNull(request.getParamValue("orgObj"));//所属部门
			String userid = CommonUtils.checkNull(request.getParamValue("userid"));//坐席人员
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont"));//备注
			String crResult = CommonUtils.checkNull(request.getParamValue("crResult")); //是否满意
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			if(userid != null && userid.length() > 0 && "".equals(crResult))
			{
				// 张宇  转部门
				//*******************************
				ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT_FEEDBACK));
				ttCrmComplaintPO2.setCpDealUser(Long.parseLong(userid));
				ttCrmComplaintPO2.setCpDealUserName(commonUtilActions.getDealUserName(Long.parseLong(userid)));
				//设置指定回访人
				ttCrmComplaintPO2.setCpCrUserId(Long.parseLong(userid));
				ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT_FEEDBACK));
				ttCrmComplaintDealRecordPO.setCpContent(ccont);
				ttCrmComplaintDealRecordPO.setCpNextDealId(Long.parseLong(userid));
				
				dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
				dao.insert(ttCrmComplaintDealRecordPO);
				//*******************************
				
				// 艾春 9.24 得到页面回访明细
				String[] crIds = request.getParamValues("crId"); // 获取回访记录ID
				String[] contents = request.getParamValues("content"); // 获取回访记录内容
				// 艾春 9.24 更新回访记录
				if(null != crIds && crIds.length > 0){
					for (int i = 0; i < crIds.length; i++) {
						TtCrmComplaintReturnRecordPO ttCrmComplaintReturnRecord1 = new TtCrmComplaintReturnRecordPO();
						TtCrmComplaintReturnRecordPO ttCrmComplaintReturnRecord2 = new TtCrmComplaintReturnRecordPO();
						ttCrmComplaintReturnRecord1.setCrId(Long.parseLong(crIds[i]));
						ttCrmComplaintReturnRecord2.setCrContent(contents[i]==null?"":contents[i]);
						
						// 更新管理员变更的回访明细
						dao.update(ttCrmComplaintReturnRecord1, ttCrmComplaintReturnRecord2);
					}
				}
				
			}else
			{
				// 艾春 9.24 得到页面回访明细
				String[] crIds = request.getParamValues("crId"); // 获取回访记录ID
				String[] contents = request.getParamValues("content"); // 获取回访记录内容

			
				// 艾春2013.11.25 更新投诉结果信息，如果为满意，则直接更新为满意，否则不设值，与客户确认过只要是正常关闭的都是满意
				if(!"".equals(crResult) && Constant.PLEASED.equals(crResult)) {
					ttCrmComplaintPO2.setCpSf(Integer.parseInt(Constant.PLEASED));
				}
//				if(!"".equals(crResult)) ttCrmComplaintPO2.setCpSf(Integer.parseInt(crResult));
				// 艾春2013.11.25 更新投诉结果信息，如果为满意，则直接更新为满意，否则不设值，与客户确认过只要是正常关闭的都是满意
				
				//满意 审核通过
				if(crResult.equals(Constant.PLEASED.toString())){
					//关闭状态
					ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_ALREADY_CLOSE));
					ttCrmComplaintPO2.setCpCloseUser(logonUser.getUserId());
					ttCrmComplaintPO2.setCpCloseDate(new Date());
					
					//查询是否有管理员驳回的数据
					List<Map<String,Object>> rejectlist = dao.queryRejectDealRecord(ttCrmComplaintPO3.getCpId());
					if(rejectlist!=null && rejectlist.size()>0){
						ttCrmComplaintPO2.setCpIsOnceSf(Constant.IF_TYPE_NO);
					}else{
						ttCrmComplaintPO2.setCpIsOnceSf(Constant.IF_TYPE_YES);
					}
					//ttCrmComplaintPO2.setCpDealUser(Long.parseLong(orgObj));
					ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_ALREADY_CLOSE));
				//满意 审核不通过
				}else if(crResult.equals(Constant.YAWP.toString())){
					//驳回
					ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING_REJECT));
					//记录哪个大区处理
					ttCrmComplaintPO2.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
					ttCrmComplaintPO2.setCpDealUser(ttCrmComplaintPO3.getCpDealOrg());
					ttCrmComplaintPO2.setCpDealUserName(commonUtilActions.getDealUserName(ttCrmComplaintPO3.getCpDealOrg()));
					ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING_REJECT));
					ttCrmComplaintDealRecordPO.setCpNextDealId(ttCrmComplaintPO3.getCpDealOrg());
				}
				ttCrmComplaintDealRecordPO.setCpContent(ccont);
				
				// 艾春 9.24 更新回访记录
				if(null != crIds && crIds.length > 0){
					for (int i = 0; i < crIds.length; i++) {
						TtCrmComplaintReturnRecordPO ttCrmComplaintReturnRecord1 = new TtCrmComplaintReturnRecordPO();
						TtCrmComplaintReturnRecordPO ttCrmComplaintReturnRecord2 = new TtCrmComplaintReturnRecordPO();
						ttCrmComplaintReturnRecord1.setCrId(Long.parseLong(crIds[i]));
						ttCrmComplaintReturnRecord2.setCrContent(contents[i]==null?"":contents[i]);
						
						// 更新管理员变更的回访明细
						dao.update(ttCrmComplaintReturnRecord1, ttCrmComplaintReturnRecord2);
					}
				}
				
				dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
				dao.insert(ttCrmComplaintDealRecordPO);
			}
			act.setOutData("success", "true");
		}else if(id.equals("aplayVerify")){
			String verifyRadio = CommonUtils.checkNull(request.getParamValue("verifyRadio"));//审核状态
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont"));//备注
			

			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			TtCrmComplaintDelayRecordPO ttCrmComplaintDelayRecordPO = complaintAcceptDao.queryApplayDelayToAdmin(Long.parseLong(cpid));
			//TtCrmComplaintDelayRecordPO ttCrmComplaintDelayRecordPO2 = new TtCrmComplaintDelayRecordPO();
			//ttCrmComplaintDelayRecordPO2.setClId(ttCrmComplaintDelayRecordPO.getClId());
			
			TtCrmComplaintDelayRecordPO ttCrmComplaintDelayRecordPO1 = new TtCrmComplaintDelayRecordPO();
			ttCrmComplaintDelayRecordPO1 = commonUtilActions.copyTtCrmComplaintDelayRecordPO(ttCrmComplaintDelayRecordPO, ttCrmComplaintDelayRecordPO1);
			ttCrmComplaintDelayRecordPO1.setClId(new Long(SequenceManager.getSequence("")));
			ttCrmComplaintDelayRecordPO1.setClVerifyContent(ccont);
			ttCrmComplaintDelayRecordPO1.setClVerifyDate(new Date());
			
			ttCrmComplaintDelayRecordPO1.setClVerifyUser(logonUser.getName());
			ttCrmComplaintDelayRecordPO1.setClVerifyUserId(logonUser.getUserId());
			ttCrmComplaintDelayRecordPO1.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintDelayRecordPO1.setUpdateDate(new Date());
			ttCrmComplaintDelayRecordPO1.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDelayRecordPO1.setCpDealDealer(ttCrmComplaintPO3.getCpDealOrg());

			//审核通过  强制关闭
			if(verifyRadio.equals(Constant.ADMIN_VERIFY_SUCCESS.toString())){
				ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_CLOSE));
				ttCrmComplaintPO2.setCpSf(Integer.parseInt(Constant.YAWP));
				ttCrmComplaintPO2.setCpCloseDate(new Date());
				ttCrmComplaintPO2.setCpCloseUser(logonUser.getUserId());
				ttCrmComplaintPO2.setCpClStatus(Constant.PASS_APPLY);
				ttCrmComplaintPO2.setCpClTurnOrg(-1L);
				ttCrmComplaintDelayRecordPO1.setClVerifyStatus(Constant.PASS_APPLY);
				ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_CLOSE));
			//审核不通过 
			}else if(verifyRadio.equals(Constant.ADMIN_VERIFY_FAIL.toString())){
				ttCrmComplaintPO2.setCpClStatus(Constant.REJECT_APPLY);
				//ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING));
				ttCrmComplaintPO2.setCpClTurnOrg(-1L);
				ttCrmComplaintDelayRecordPO1.setClVerifyStatus(Constant.REJECT_APPLY);
				ttCrmComplaintDelayRecordPO1.setCpNextDealId(ttCrmComplaintPO3.getCpDealOrg());
				//ttCrmComplaintDealRecordPO.setCpStatus(Constant.REJECT_APPLY);
			}
			ttCrmComplaintDealRecordPO.setCpContent(ccont);
			
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			dao.insert(ttCrmComplaintDealRecordPO);
			dao.insert(ttCrmComplaintDelayRecordPO1);
			//complaintAcceptDao.update(ttCrmComplaintDelayRecordPO2, ttCrmComplaintDelayRecordPO1);
			act.setOutData("success", "true");
		}

	}
	public void complaintSearchDownExcel(){
		try{
			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
			String name = CommonUtils.checkNull(request.getParamValue("name"));  				
			String tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  	
			String dealUser = CommonUtils.checkNull(request.getParamValue("dealUser"));  	
			String accUser = CommonUtils.checkNull(request.getParamValue("accUser"));  	
			String biztype = CommonUtils.checkNull(request.getParamValue("biztype"));  				
			String status = CommonUtils.checkNull(request.getParamValue("status"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));
			String region = CommonUtils.checkNull(request.getParamValue("region"));  	
			String pro = CommonUtils.checkNull(request.getParamValue("pro"));  				
			String checkStart = CommonUtils.checkNull(request.getParamValue("checkStart"));  	
			String checklEnd = CommonUtils.checkNull(request.getParamValue("checklEnd")); 
			String delaystatus = CommonUtils.checkNull(request.getParamValue("delaystatus")); 
			String repeatComplaint = CommonUtils.checkNull(request.getParamValue("repeatComplaint")); 	
			String cpNo = CommonUtils.checkNull(request.getParamValue("cpNo"));
			
			String CP_START_DATE = CommonUtils.checkNull(request.getParamValue("CP_START_DATE")); 	
			String CP_END_DATE = CommonUtils.checkNull(request.getParamValue("CP_END_DATE"));
			
			// 艾春 2013.11.27 添加需考核日期
			String KH_START_DATE = CommonUtils.checkNull(request.getParamValue("KH_START_DATE")); 	
			String KH_END_DATE = CommonUtils.checkNull(request.getParamValue("KH_END_DATE"));
			String is_close = CommonUtils.checkNull(request.getParamValue("is_close"));   
		    String is_one_close = CommonUtils.checkNull(request.getParamValue("is_one_close"));
		    String model = CommonUtils.checkNull(request.getParamValue("model"));

			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			seatsSet seat = new seatsSet();
			boolean isAdmin = seat.isAdmin(logonUser.getUserId());
				
			List<Map<String, Object>> complaintAcceptData =  dao.queryComplaintInfo(cpNo,vin,name,tele,level,dealUser,accUser,biztype,
					status,dealStart,dealEnd,dateStart,dateEnd,region,pro,checkStart,checklEnd,isAdmin,CP_START_DATE,CP_END_DATE,KH_START_DATE,KH_END_DATE,
					logonUser.getUserId(),delaystatus,repeatComplaint,is_close,is_one_close,model);
			if(complaintAcceptData!=null && complaintAcceptData.size()>0){
				for(Map<String, Object> map :complaintAcceptData){
					String dealStr = null;
					for(Map<String, Object> dealMap : dao.queryDealRecordList(((BigDecimal)map.get("CPID")).longValue())){
						String tempDate = (String)dealMap.get("CDDATE")==null?"":(String)dealMap.get("CDDATE");
						String tempConTent= (String)dealMap.get("CDCONTENT")==null?"":(String)dealMap.get("CDCONTENT");
						String tempD =tempDate +" "+tempConTent;
						if(dealStr==null)
						dealStr = tempD;
						else dealStr = dealStr + tempD;
					}
					map.put("DEALPRO", dealStr);
					
					String returnStr = null;
					String MAXRETURNDATE  = "";
					String CRUSER = "";
					
					for(Map<String, Object> dealMap : dao.queryReturnRecordList(((BigDecimal)map.get("CPID")).longValue())){
						String tempDate = (String)dealMap.get("CRDATE")==null?"":(String)dealMap.get("CRDATE");
						String tempConTent= (String)dealMap.get("CRCONTENT")==null?"":(String)dealMap.get("CRCONTENT");
						CRUSER = CRUSER + " "+  (String)dealMap.get("CR_USER")==null?"":(String)dealMap.get("CR_USER"); 
						MAXRETURNDATE = MAXRETURNDATE +"  "+ (String)dealMap.get("CRDATE")==null?"":(String)dealMap.get("CRDATE");
						String tempR =tempDate +" "+tempConTent;
						if(returnStr==null)
							returnStr = tempR;
						else returnStr = returnStr +" "+ tempR;
					}
					map.put("RETURNPRO", returnStr);
					map.put("MAXRETURNDATE", MAXRETURNDATE);
					map.put("CRUSER", CRUSER);
					
					//添加申请纪录详情 2013-11-15 wangming
					String verifyStr = null;
					for(Map<String, Object> dealMap : dao.queryVeriftyRecordList(((BigDecimal)map.get("CPID")).longValue())){
						String cldate = (String)dealMap.get("CLDATE")==null?"":(String)dealMap.get("CLDATE");
						String days= (String)dealMap.get("DAYS")==null?"":(String)dealMap.get("DAYS");
						String clcont = (String)dealMap.get("CLCONT")==null?"":(String)dealMap.get("CLCONT");
						String cluser= (String)dealMap.get("CLUSER")==null?"":(String)dealMap.get("CLUSER");
						String clverifyDate = (String)dealMap.get("CLVERIFYDATE")==null?"":(String)dealMap.get("CLVERIFYDATE");
						String clverifyContent = (String)dealMap.get("CLVERIFYCONTENT")==null?"":(String)dealMap.get("CLVERIFYCONTENT");					
						String clverifyUser = (String)dealMap.get("CLVERIFYUSER")==null?"":(String)dealMap.get("CLVERIFYUSER");
						String cpdeferType = (String)dealMap.get("CPDEFERTYPE")==null?"":(String)dealMap.get("CPDEFERTYPE");
						String clverifyStaus = (String)dealMap.get("CLVERIFYSTATUS")==null?"":(String)dealMap.get("CLVERIFYSTATUS");
						String createDate= (String)dealMap.get("CREATEDATE")==null?"":(String)dealMap.get("CREATEDATE");
						
						String tempR ="申请延期至时间:"+cldate +" 申请天数:"+days+"天    申请类型："+cpdeferType+" 申请内容:"+clcont+"  申请人:"
										+cluser +"  申请时间:"+createDate+"  审核时间:"+clverifyDate +"   审核内容:"+clverifyContent
										+"  审核人:"+clverifyUser+"  审核状态:"+clverifyStaus;
						if(verifyStr==null)
							verifyStr = tempR;
						else verifyStr = verifyStr +" "+ tempR;
					}
					map.put("VERIFYPRO", verifyStr);
				}
				complaintAcceptDataToExcel(complaintAcceptData);
				act.setOutData("success", "true");
				act.setForword(ComplaintSearch);
			}else {
				act.setOutData("noresult", "true");
			}	
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void complaintAcceptDataToExcel(List<Map<String, Object>> list) throws Exception{
		String[] head=new String[41];
		head[0]="抱怨单编号";
		head[1]="抱怨级别";
		head[2]="抱怨类别";
		head[3]="省市";
		head[4]="地市";
		head[5]="客户姓名";
		head[6]="联系电话";
		head[7]="抱怨内容";
		head[8]="抱怨对象";
		head[9]="抱怨时间";
		head[10]="受理人";
		head[11]="车种";
		head[12]="VIN号";
		head[13]="购车时间";
		head[14]="行驶里程";
		head[15]="里程范围";
		head[16]="处理部门";
		head[17]="处理人";
		head[18]="签收日期";
		head[19]="转出时间";
		head[20]="回访结果";
		head[21]="回访人";
		head[22]="回访日期";
		head[23]="关闭时间";
		head[24]="处理时长(小时)";
		head[25]="规定处理期限(天)";
		head[26]="延期天数(天)";
		head[27]="超期天数(天)";
		head[28]="第一次延期申请时间";
		head[29]="规定及时关闭时间";
		head[30]="考核截至日期";
		head[31]="是否及时关闭";
		head[32]="是否一次处理满意";
		head[33]="是否正常关闭";
		head[34]="处理过程";
		head[35]="申请过程";
		head[36]="最终反馈日期";
		head[37]="状态";
		head[38]="当前处理人";
		head[39]="考核状态";
		head[40]="延期次数";
	    List list1=new ArrayList();
	    	      
	    TcCodeDao tcCodeDao = TcCodeDao.getInstance();
	    
	    // 艾春 2013.12.5 修改根据TC_CODE的类型从数据库中一次性取出所有的ID与名称的集合
	    List<Map<String, Object>> cpls = tcCodeDao.getTcCodesByType(Constant.COMPLAINT_LEVEL.toString());
	    List<Map<String, Object>> cpns = tcCodeDao.getTcCodesByType(Constant.TYPE_COMPLAIN.toString());
	    List<Map<String, Object>> cpms = tcCodeDao.getTcCodesByType(Constant.MILEAGE_RANGE.toString());
	    List<Map<String, Object>> cpis = tcCodeDao.getTcCodesByType(Constant.IF_TYPE.toString());
	    List<Map<String, Object>> cpss = tcCodeDao.getTcCodesByType(Constant.COMPLAINT_STATUS);
	    
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[41];
					detail[0] = CommonUtils.checkNull(map.get("CPNO"));
					detail[1] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("CPLEVEL")),cpls);
					detail[2] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("BIZCONT")),cpns);
					detail[3] = CommonUtils.checkNull(map.get("PRO"));
					detail[4] = CommonUtils.checkNull(map.get("CITY"));
					detail[5] = CommonUtils.checkNull(map.get("CTMNAME"));
					detail[6] = CommonUtils.checkNull(map.get("PHONE"));
					detail[7] = CommonUtils.checkNull(map.get("CPCONT"));
					detail[8] = CommonUtils.checkNull(map.get("CPOBJECT"));
					detail[9] = CommonUtils.checkNull(map.get("CREATEDATE"));
					detail[10] = CommonUtils.checkNull(map.get("ACUSER"));
					detail[11] = CommonUtils.checkNull(map.get("SNAME"));
					detail[12] = CommonUtils.checkNull(map.get("VIN"));
					detail[13] = CommonUtils.checkNull(map.get("BDATE"));
					detail[14] = CommonUtils.checkNull(map.get("CPMILEAGE"));
					detail[15] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("MILEAGERANGE")),cpms);
					detail[16] = CommonUtils.checkNull(map.get("ORGNAME"));
					detail[17] = CommonUtils.checkNull(map.get("DEALUSER"));
					detail[18] = CommonUtils.checkNull(map.get("CPACCDATE"));
					detail[19] = CommonUtils.checkNull(map.get("TURNDATE"));
					detail[20] = CommonUtils.checkNull(map.get("RETURNPRO"));
					detail[21] = CommonUtils.checkNull(map.get("CRUSER"));
					detail[22] = CommonUtils.checkNull(map.get("MAXRETURNDATE"));
					detail[23] = CommonUtils.checkNull(map.get("CPCDATE"));
					detail[24] = CommonUtils.checkNull(map.get("DEALTIME"));
					detail[25] = CommonUtils.checkNull(map.get("CPLIMIT"));
					detail[26] = CommonUtils.checkNull(map.get("DELAYDATE"));
					detail[27] = CommonUtils.checkNull(map.get("EXCEEDDATE"));
					detail[28] = CommonUtils.checkNull(map.get("CP_CL_ONCE_DATE"));
					detail[29] = CommonUtils.checkNull(map.get("SHOULDCLOSETIME"));
					detail[30] = CommonUtils.checkNull(map.get("CP_CL_DATE"));
					detail[31] = CommonUtils.checkNull(map.get("ISTIMELYCLOSE"));
					detail[32] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("CPISONCESF")),cpis);
					detail[33] = CommonUtils.checkNull(map.get("ISNORMALCLOSE"));
					detail[34] = CommonUtils.checkNull(map.get("DEALPRO"));
					detail[35] = CommonUtils.checkNull(map.get("VERIFYPRO"));
					detail[36] = CommonUtils.checkNull(map.get("LASTDEALDATE"));
					detail[37] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("STATUS")),cpss);
					detail[38] = CommonUtils.checkNull(map.get("DUSER"));
					detail[39] = CommonUtils.checkNull(map.get("ISINOROUT"));
					detail[40] = CommonUtils.checkNull(map.get("TIMES"));
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "抱怨查询清单.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
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
	
}
