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
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptYxDao;
import com.infodms.dms.dao.customerRelationships.ComplaintConsultYxDao;
import com.infodms.dms.dao.customerRelationships.OrgCustomUserRelationDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmIncomingCallPO;
import com.infodms.dms.po.TtCrmIncomingDealRecordPO;
import com.infodms.dms.po.TtCrmIncomingReturnRecordPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
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
public class ComplaintSearchYx {
	private static Logger logger = Logger.getLogger(ComplaintSearchYx.class);
	//投诉查询页面
	private final String ComplaintSearch = "/jsp/customerRelationships/complaintConsult/complaintSearchYx.jsp";
	//待处理管理员页面
	//private final String ComplaintSearchUpdate = "/jsp/customerRelationships/complaintConsult/complaintSearchUpdate.jsp";
	private final String ComplaintSearchUpdate = "/jsp/customerRelationships/complaintConsult/complaintSearchReturnUpdateYx.jsp";//
	private final String ComplaintSearchDetail = "/jsp/customerRelationships/complaintConsult/complaintSearchReturnDetailYx.jsp";//
	
	//大区已处理管理员页面
	//private final String AdminInforDetailComplaint = "/jsp/customerRelationships/complaintConsult/complaintSearchDetailUpdate.jsp";
	private final String AdminInforDetailComplaint = "/jsp/customerRelationships/complaintConsult/complaintSearchReturnUpdateYx.jsp";
	//大区已回访管理员页面
	private final String AdminCustomerReturnComplaint = "/jsp/customerRelationships/complaintConsult/complaintSearchReturnUpdateYx.jsp";
	//大区已审核强制关闭 管理员审核页面
	//private final String AdminAplayVerifyComplaint = "/jsp/customerRelationships/complaintConsult/complaintAplayVerifyUpdate.jsp";
	private final String AdminAplayVerifyComplaint = "/jsp/customerRelationships/complaintConsult/complaintSearchReturnUpdateYx.jsp";
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
			
			ComplaintAcceptYxDao dao = ComplaintAcceptYxDao.getInstance();
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
	
	/**
	 * 跟进处理
	 */
	public void complaintSearchUpdate(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  				
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid"));  				
			String status = CommonUtils.checkNull(request.getParamValue("status"));  	
			act.setOutData("cpid", cpid);
			act.setOutData("ctmid", ctmid);
			
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			ComplaintConsultYxDao cDao = ComplaintConsultYxDao.getInstance();
			ComplaintAcceptYxDao complaintAcceptDao = ComplaintAcceptYxDao.getInstance();
			Map<String,Object> complaintConsult = cDao.queryComplaintConsult(cpid,ctmid);
			List<Map<String, Object>> dealRecordList = complaintAcceptDao.queryDealRecord(Long.parseLong(cpid));
			if(complaintConsult != null && dealRecordList != null && dealRecordList.size() > 0){
				complaintConsult.put("ORGNAME", dealRecordList.get(0).get("NEXTNAME"));
			}
			act.setOutData("complaintConsult", complaintConsult);//投诉咨询主表
			act.setOutData("dealRecordList", dealRecordList);//投诉咨询跟进表
			act.setOutData("returnRecordList", complaintAcceptDao.queryReturnRecord(Long.parseLong(cpid)));//回访记录表
			
			//判断当前登录用类型 0:呼叫监控部，1：主机厂部门，2大区，3经销商
			String logOnUserType = "";
			OrgCustomUserRelationDao orgCustomerDao = OrgCustomUserRelationDao.getInstance();
			List<Map<String, Object>> userCustomer = orgCustomerDao.queryCustomerInfoByUserId(logonUser.getUserId()+"");
			if (userCustomer != null && userCustomer.size() > 0) {
				//为主机厂部门
				logOnUserType = "1";
				if ((userCustomer.get(0).get("ORGID").toString()).equals("2013070519381899")) {
					//为呼叫监控部
					logOnUserType = "0";
				}else if(((userCustomer.get(0).get("ORGID")+"").toString()).equals((complaintConsult.get("CPDEALORGID")+"").toString())) {
					//为当前部门处理
					act.setOutData("isEdit", "1");
				}
			}else {
				if (logonUser.getPoseType().equals(Constant.SYS_USER_SGM)) {
					//主机厂
					if (Integer.parseInt(logonUser.getDutyType()) == Constant.DUTY_TYPE_LARGEREGION) {
						//为大区用户
						logOnUserType = "2";
						//设置大区ID
						act.setOutData("orgId", logonUser.getOrgId());
						if((logonUser.getOrgId()+"").equals(complaintConsult.get("CPDEALORGID")+"")){
							act.setOutData("isEdit", "1");
						}else {
							if(!XHBUtil.IsNull(complaintConsult.get("CPDEALDEALER"))){
								//获取大区下的所有经销商
								String isEdit = "";
								List<Map<String, Object>> dealers = commonUtilActions.getOrgDealer(logonUser.getOrgId()+"");
								if (dealers != null && dealers.size() > 0) {
									for (Map<String, Object> map : dealers) {
										if (complaintConsult.get("CPDEALDEALER").equals(map.get("DEALER_ID"))) {
											isEdit = "1";
											break;
										}
									}
								}
								if (isEdit.equals("1")) {
									act.setOutData("isEdit", "1");
								}else {
									act.setOutData("isEdit", "2");
								}
							}else {
								act.setOutData("isEdit", "2");
							}
						}
					}
				}else {
					//经销商
					logOnUserType = "3";
					//设置经销商ID
					act.setOutData("dealerId", logonUser.getDealerId());
					if(logonUser.getDealerId().equals(complaintConsult.get("CPDEALDEALER")+"")){
						act.setOutData("isEdit", "1");
					}else {
						act.setOutData("isEdit", "2");
					}
				}
			}

			act.setOutData("cpStatus", status);
			act.setOutData("logOnUserType", logOnUserType);
			//业务类型
			act.setOutData("bizTypeList", getBizType());
			//处理大区
			act.setOutData("cpDeptList", commonUtilActions.getOrgByLv("","2"));
			//处理部门
			act.setOutData("cpObjectList", commonUtilActions.getOfficeOrg());
			act.setOutData("logonUserID", logonUser.getUserId());//增加当前用户可以修改来电信息
			//当前部门名称
			if(userCustomer!=null&&userCustomer.size()>0){
				act.setOutData("ORGID", userCustomer.get(0).get("ORGID").toString());
			}else{
				act.setOutData("ORGID", "");
			}
			
			act.setForword(ComplaintSearchUpdate);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉查询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void complaintSearchDetail(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpId"));  				
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid"));  				
//			String status = CommonUtils.checkNull(request.getParamValue("status"));  	
			act.setOutData("cpid", cpid);
			act.setOutData("ctmid", ctmid);
			
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			ComplaintConsultYxDao cDao = ComplaintConsultYxDao.getInstance();
			ComplaintAcceptYxDao complaintAcceptDao = ComplaintAcceptYxDao.getInstance();
			Map<String,Object> complaintConsult = cDao.queryComplaintConsult(cpid,ctmid);
			List<Map<String, Object>> dealRecordList = complaintAcceptDao.queryDealRecord(Long.parseLong(cpid));
			if(complaintConsult != null && dealRecordList != null && dealRecordList.size() > 0){
				complaintConsult.put("ORGNAME", dealRecordList.get(0).get("NEXTNAME"));
			}
			act.setOutData("complaintConsult", complaintConsult);//投诉咨询主表
			act.setOutData("dealRecordList", dealRecordList);//投诉咨询跟进表
			act.setOutData("returnRecordList", complaintAcceptDao.queryReturnRecord(Long.parseLong(cpid)));//回访记录表
			
			//判断当前登录用类型 0:呼叫监控部，1：主机厂部门，2大区，3经销商
			String logOnUserType = "";
			OrgCustomUserRelationDao orgCustomerDao = OrgCustomUserRelationDao.getInstance();
			List<Map<String, Object>> userCustomer = orgCustomerDao.queryCustomerInfoByUserId(logonUser.getUserId()+"");
			if (userCustomer != null && userCustomer.size() > 0) {
				//为主机厂部门
				logOnUserType = "1";
				if ((userCustomer.get(0).get("ORGID").toString()).equals("2013070519381899")) {
					//为呼叫监控部
					logOnUserType = "0";
				}else if(((userCustomer.get(0).get("ORGID")+"").toString()).equals((complaintConsult.get("CPDEALORGID")+"").toString())) {
					//为当前部门处理
					act.setOutData("isEdit", "1");
				}
			}else {
				if (logonUser.getPoseType().equals(Constant.SYS_USER_SGM)) {
					//主机厂
					if (Integer.parseInt(logonUser.getDutyType()) == Constant.DUTY_TYPE_LARGEREGION) {
						//为大区用户
						logOnUserType = "2";
						//设置大区ID
						act.setOutData("orgId", logonUser.getOrgId());
						if((logonUser.getOrgId()+"").equals(complaintConsult.get("CPDEALORGID")+"")){
							act.setOutData("isEdit", "1");
						}else {
							if(!XHBUtil.IsNull(complaintConsult.get("CPDEALDEALER"))){
								//获取大区下的所有经销商
								String isEdit = "";
								List<Map<String, Object>> dealers = commonUtilActions.getOrgDealer(logonUser.getOrgId()+"");
								if (dealers != null && dealers.size() > 0) {
									for (Map<String, Object> map : dealers) {
										if (complaintConsult.get("CPDEALDEALER").equals(map.get("DEALER_ID"))) {
											isEdit = "1";
											break;
										}
									}
								}
								if (isEdit.equals("1")) {
									act.setOutData("isEdit", "1");
								}else {
									act.setOutData("isEdit", "2");
								}
							}else {
								act.setOutData("isEdit", "2");
							}
						}
					}
				}else {
					//经销商
					logOnUserType = "3";
					//设置经销商ID
					act.setOutData("dealerId", logonUser.getDealerId());
					if(logonUser.getDealerId().equals(complaintConsult.get("CPDEALDEALER")+"")){
						act.setOutData("isEdit", "1");
					}else {
						act.setOutData("isEdit", "2");
					}
				}
			}

//			act.setOutData("cpStatus", status);
			act.setOutData("logOnUserType", logOnUserType);
			//业务类型
			act.setOutData("bizTypeList", getBizType());
			//处理大区
			act.setOutData("cpDeptList", commonUtilActions.getOrgByLv("","2"));
			//处理部门
			act.setOutData("cpObjectList", commonUtilActions.getOfficeOrg());
			act.setOutData("logonUserID", logonUser.getUserId());//增加当前用户可以修改来电信息
			act.setForword(ComplaintSearchDetail);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉查询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 保存
	 */
	public void ComplaintSearchUpdateSubmit(){
		act.getResponse().setContentType("application/json");
		try {
			ComplaintAcceptYxDao dao = ComplaintAcceptYxDao.getInstance();
			//主表ID
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));
			//用于验证处理方式 0.呼叫监控部编辑,1.部门编辑,2.大区编辑,3.经销商编辑,4.跟进
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));
			//跟进(回访内容)
			String ccontent = request.getParamValue("ccont");
			Map<String, Object> incoming = dao.queryInComingByCpId(cpid);
			if (incoming == null) {
				return;
			}
			
			//获取跟进数据是否改动
			List<Map<String, Object>> records = dao.queryInComingRecordByCpId(cpid);
			if (records != null && records.size() > 0) {
				for (Map<String, Object> map : records) {
					String cdId = map.get("CD_ID").toString();
					String content = request.getParamValue(cdId);
					if (content != null) {
						TtCrmIncomingDealRecordPO oldPo = new TtCrmIncomingDealRecordPO();
						oldPo.setCdId(Long.parseLong(cdId));
						TtCrmIncomingDealRecordPO newPo = new TtCrmIncomingDealRecordPO();
						newPo.setUpdateBy(logonUser.getUserId());
						newPo.setUpdateDate(new Date());
						newPo.setCpContent(content);
						dao.update(oldPo, newPo);
					}
				}
			}
			
			//跟进数据
			TtCrmIncomingDealRecordPO gjPo = new TtCrmIncomingDealRecordPO();
			gjPo.setCdDate(new Date());
			gjPo.setCdId(new Long(SequenceManager.getSequence("")));
			gjPo.setCdUser(logonUser.getName());
			gjPo.setCdUserId(logonUser.getUserId());
			gjPo.setCpId(Long.parseLong(cpid));
			gjPo.setCreateBy(logonUser.getUserId());
			gjPo.setCreateDate(new Date());
			gjPo.setCpContent(ccontent);
			String dealUser = request.getParamValue("dealUser");
			if (!XHBUtil.IsNull(dealUser)) {
				gjPo.setCpResponsor(Long.parseLong(dealUser));
			}else if(!XHBUtil.IsNull(incoming.get("CP_RESPONSER"))){
				gjPo.setCpResponsor(Long.parseLong(incoming.get("CP_RESPONSER")+""));
			}
			if("0".equals(flag)){
				//呼叫监控部
				String crResult = CommonUtils.checkNull(request.getParamValue("crResult"));//关闭/转其他部门
				if ("0".equals(crResult)) {
					String departmentFlag = CommonUtils.checkNull(request.getParamValue("choise"));//处理部门类型 1车厂 2大区经销商
					updatIncomingCall(gjPo,cpid,departmentFlag,dao);
				} else {
					//关闭
					TtCrmIncomingCallPO ttCallPo = new TtCrmIncomingCallPO();
					ttCallPo.setCpId(Long.parseLong(cpid));
					
					TtCrmIncomingCallPO ttCallUpPo = new TtCrmIncomingCallPO();
					ttCallUpPo.setCpDealUser(logonUser.getUserId());
					ttCallUpPo.setCpDealUserName(logonUser.getName());
					ttCallUpPo.setCpCloseUser(logonUser.getUserId());
					ttCallUpPo.setCpCloseDate(new Date());
					ttCallUpPo.setUpdateBy(logonUser.getUserId());
					ttCallUpPo.setUpdateDate(new Date());
					ttCallUpPo.setCpStatus(Integer.parseInt((Constant.COMPLAINT_STATUS_ALREADY_CLOSE)));
					ttCallUpPo.setCpUpdateContent(ccontent);
					if (!XHBUtil.IsNull(dealUser)) {
						ttCallUpPo.setCpResponser(Long.parseLong(dealUser));
					}
					dao.update(ttCallPo, ttCallUpPo);
					
					//插入回访
					TtCrmIncomingReturnRecordPO trPo = new TtCrmIncomingReturnRecordPO();
					trPo.setCrId(Long.parseLong(SequenceManager.getSequence("")));
					trPo.setCpId(Long.parseLong(cpid));
					trPo.setCpStatus(Integer.parseInt((Constant.COMPLAINT_STATUS_ALREADY_CLOSE)));
					trPo.setCrConfirmOpinion(Integer.parseInt(crResult));
					trPo.setCrId(logonUser.getUserId());
					trPo.setCrUser(logonUser.getName());
					trPo.setCrDate(new Date());
					trPo.setCrContent(ccontent);
					trPo.setCreateBy(logonUser.getUserId());
					trPo.setCreateDate(new Date());
					dao.insert(trPo);
				}
			} else if ("1".equals(flag)) {
				String departmentFlag = CommonUtils.checkNull(request.getParamValue("choise"));//处理部门类型 1车厂 2大区经销商
				updatIncomingCall(gjPo,cpid,departmentFlag,dao);
			} else if("2".equals(flag)){
				String departmentFlag = CommonUtils.checkNull(request.getParamValue("choise"));//处理部门类型 1车厂 2大区经销商
				updatIncomingCall(gjPo,cpid,departmentFlag,dao);
			} else if("3".equals(flag)){
				String departmentFlag = "1";//处理部门类型 1车厂 2大区经销商
				updatIncomingCall(gjPo,cpid,departmentFlag,dao);
			} else if("4".equals(flag) && !XHBUtil.IsNull(ccontent)) {
				//跟进数据
				if (!XHBUtil.IsNull(incoming.get("CP_DEAL_ORG"))) {
					gjPo.setCpDealOrg(Long.parseLong(incoming.get("CP_DEAL_ORG")+""));
				}
				if (!XHBUtil.IsNull(incoming.get("CP_DEAL_DEALER"))) {
					gjPo.setCpDealDealer(Long.parseLong(incoming.get("CP_DEAL_DEALER")+""));
				}
				if (Constant.COMPLAINT_DEALER_STATUS_DOING.equals((incoming.get("CP_STATUS")+"").toString())) {
					gjPo.setCpNextDealId(gjPo.getCpDealDealer());
				}else {
					gjPo.setCpNextDealId(gjPo.getCpDealOrg());
				}
				gjPo.setCpPointStatus(20231001);
				dao.insert(gjPo);
				
				String is_huifang = CommonUtils.checkNull(request.getParamValue("is_huifang"));
				TtCrmIncomingCallPO ttCallPo = new TtCrmIncomingCallPO();
				ttCallPo.setCpId(Long.parseLong(cpid));
				
				TtCrmIncomingCallPO ttCallUpPo = new TtCrmIncomingCallPO();
				if(incoming.get("CP_DEAL_ORG")!=null){
					if(incoming.get("CP_DEAL_ORG").toString().equals("2014050994697313")){
						ttCallUpPo.setIsHuifang("1");
					}
					else{
						if("9506".equals(incoming.get("CP_BIZ_TYPE").toString())){
							ttCallUpPo.setIsHuifang("1");
						}else{
							if("1".equals(is_huifang)){
								ttCallUpPo.setIsHuifang(is_huifang);
							}else{
								ttCallUpPo.setIsHuifang("0");
							}
						}
					}
				}else{
//					if(incoming.get("CP_DEAL_ORG").toString().equals("2014050994697313")){
//						ttCallUpPo.setIsHuifang("1");
//					}else{
						if("9506".equals(incoming.get("CP_BIZ_TYPE").toString())){
							ttCallUpPo.setIsHuifang("1");
						}else{
							if("1".equals(is_huifang)){
								ttCallUpPo.setIsHuifang(is_huifang);
							}else{
								ttCallUpPo.setIsHuifang("0");
							}
						}
//					}
				}
				
				ttCallUpPo.setCpPointStatus(20231001);
				dao.update(ttCallPo,ttCallUpPo);
				
			}else {
				act.setOutData("success", "2");
				return;
			}
			act.setOutData("success", "1");
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"处理保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 流程处理
	 * @param gjPo
	 * @param cpid
	 * @param departmentFlag
	 * @param dao
	 */
	public void updatIncomingCall(TtCrmIncomingDealRecordPO gjPo,String cpid,String departmentFlag,ComplaintAcceptYxDao dao){
		//继续处理
		String dealorg = "";//来电处理责任大区(部门)
		String cpObject1 = CommonUtils.checkNull(request.getParamValue("cpObject")); //所属部门
		String cpObject2 = CommonUtils.checkNull(request.getParamValue("dept")); //所属大区
		if ("1".equals(departmentFlag)) {
			dealorg = cpObject1;
		}else {
			dealorg = cpObject2;
		}
		String dealDealer = CommonUtils.checkNull(request.getParamValue("vehicleCompany"));//来电处理责任经销商ID
		
		TtCrmIncomingCallPO ttCcOldPo = new TtCrmIncomingCallPO();
		ttCcOldPo.setCpId(Long.parseLong(cpid));
		
		TtCrmIncomingCallPO ttCcUpPo = new TtCrmIncomingCallPO();
		ttCcUpPo.setCpDealDepartmentFlag(Integer.parseInt(departmentFlag));
		ttCcUpPo.setCpDealOrg(Long.parseLong(dealorg));
		if (!XHBUtil.IsNull(dealDealer)) {
			ttCcUpPo.setCpDealDealer(Long.parseLong(dealDealer));
		}
		ttCcUpPo.setCpDealUser(logonUser.getUserId());
		ttCcUpPo.setCpDealUserName(logonUser.getName());
		String dealUser = request.getParamValue("dealUser");
		if (!XHBUtil.IsNull(dealUser)) {
			ttCcUpPo.setCpResponser(Long.parseLong(dealUser));
		}
		ttCcUpPo.setCpUpdateContent(gjPo.getCpContent());
		ttCcUpPo.setUpdateBy(logonUser.getUserId());
		ttCcUpPo.setUpdateDate(new Date());
		if("1".equals(departmentFlag)){
			ttCcUpPo.setCpStatus(Integer.parseInt(Constant.COMPLAINT_DEALER_DEPT_DOING));
			//车厂处理
			gjPo.setCpStatus(Integer.parseInt(Constant.COMPLAINT_DEALER_DEPT_DOING));
			gjPo.setCpDealOrg(Long.parseLong(dealorg));
			gjPo.setCpNextDealId(Long.parseLong(dealorg));
		}else {
			if (!XHBUtil.IsNull(dealDealer)) {
				ttCcUpPo.setCpStatus(Integer.parseInt(Constant.COMPLAINT_DEALER_STATUS_DOING));
				//经销商处理
				gjPo.setCpStatus(Integer.parseInt(Constant.COMPLAINT_DEALER_STATUS_DOING));
				gjPo.setCpDealDealer(Long.parseLong(dealDealer));
				gjPo.setCpNextDealId(Long.parseLong(dealDealer));
			}else {
				ttCcUpPo.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING));
				//大区处理
				gjPo.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING));
				gjPo.setCpDealOrg(Long.parseLong(dealorg));
				gjPo.setCpNextDealId(Long.parseLong(dealorg));
			}
		}
		dao.update(ttCcOldPo, ttCcUpPo);
		dao.insert(gjPo);
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

			ComplaintAcceptYxDao dao = ComplaintAcceptYxDao.getInstance();
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
