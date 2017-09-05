package com.infodms.dms.actions.customerRelationships.complaintConsult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.customerRelationships.baseSetting.typeSet;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmComplaintDealRecordPO;
import com.infodms.dms.po.TtCrmComplaintPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 待处理咨询查询ACTIONS
 * @ClassName     : WaitConsultSearch 
 * @Description   : 待处理咨询查询
 * @author        : wangming
 * CreateDate     : 2013-4-23
 */
public class WaitConsultSearch {
	private static Logger logger = Logger.getLogger(WaitConsultSearch.class);
	//待处理咨询查询页面
	private final String WaitConsultSearch = "/jsp/customerRelationships/complaintConsult/waitConsultSearch.jsp";
	//待处理咨询查看页面
	private final String WaitConsultSearchWacth = "/jsp/customerRelationships/complaintConsult/waitConsultSearchWatch.jsp";
	//待处理咨询处理页面
	private final String WaitConsultSearchUpdate = "/jsp/customerRelationships/complaintConsult/waitConsultSearchUpdate.jsp";

	
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	//投诉受理初始化
	public void waitConsultSearchInit(){
		try{
			act.setForword(WaitConsultSearch);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"待处理咨询查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryWaitConsultSearch(){
		act.getResponse().setContentType("application/json");
		try{
			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
			String name = CommonUtils.checkNull(request.getParamValue("name"));  				
			String tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			String accuser = CommonUtils.checkNull(request.getParamValue("accuser"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  				
			
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> complaintAcceptData = dao.queryConsultInfo(vin,name,tele,accuser,dateStart,dateEnd,logonUser.getUserId(),Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", complaintAcceptData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理咨询查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void waitConsultSearchWatch(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("id"));  				
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			Map<String,Object> complaintAcceptMap = dao.queryConsultInfoWatch(Long.parseLong(cpid));
			
			act.setOutData("complaintAcceptMap", complaintAcceptMap);
			act.setForword(WaitConsultSearchWacth);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理咨询查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void waitConsultSearchUpdate(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("id"));  				
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			Map<String,Object> complaintAcceptMap = dao.queryConsultInfo(Long.parseLong(cpid));
			//业务类型
			act.setOutData("bizTypeList", getBizType());
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			typeSet ts = new typeSet();
			//内容类型
			act.setOutData("bclist",ts.getTypeSelect(Constant.TYPE_CONSULT));
			//抱怨对象
			act.setOutData("cpObjectList", commonUtilActions.getVehicleCompany());
			//处理方式
			List<Map<String, Object>> dealModelLists = commonUtilActions.getTcCode(Constant.CONSULT_PROCESS.toString());
			List<Map<String, Object>> dealModelList = new ArrayList<Map<String,Object>>();
			for(Map<String, Object> map : dealModelLists){
				if(!map.get("CODE_ID").equals(Constant.CONSULT_PROCESS_SPOT.toString())){
					dealModelList.add(map);
				}
			}
			act.setOutData("dealModelList", dealModelList);
			//大区
			act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//小区
			act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			
			act.setOutData("dealRecordList", dao.queryDealRecord(Long.parseLong(cpid)));
			
			act.setOutData("complaintAcceptMap", complaintAcceptMap);
			act.setForword(WaitConsultSearchUpdate);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理咨询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void waitConsultSearchUpdateSubmit(){
		act.getResponse().setContentType("application/json");
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  				
					
			String bizType = CommonUtils.checkNull(request.getParamValue("bizType"));  				
			String contType = CommonUtils.checkNull(request.getParamValue("contType"));  				
			String cplevel = CommonUtils.checkNull(request.getParamValue("cplevel"));  	
			String cplimit = CommonUtils.checkNull(request.getParamValue("cplimit"));  				
			String cpObject = CommonUtils.checkNull(request.getParamValue("cpObject"));  	
			String vehicleCompany = CommonUtils.checkNull(request.getParamValue("vehicleCompany"));  
			String complaintContent = CommonUtils.checkNull(request.getParamValue("complaintContent"));  				
			String dealModel = CommonUtils.checkNull(request.getParamValue("dealModel"));  				
			String rcont = CommonUtils.checkNull(request.getParamValue("rcont")); 
			String dealuser = CommonUtils.checkNull(request.getParamValue("dealuser"));  				
			String var = CommonUtils.checkNull(request.getParamValue("var"));  				
						
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//更新投诉咨询相关内容
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			
			ttCrmComplaintPO2.setCpBizType(Integer.parseInt(bizType));
			ttCrmComplaintPO2.setCpBizContent(Integer.parseInt(contType));
			if(!"".equals(cplevel)) ttCrmComplaintPO2.setCpLevel(Integer.parseInt(cplevel));				
			if(!"".equals(cplimit)&&cplimit.equals(Constant.RULE_ONE_DAY.toString())){
				ttCrmComplaintPO2.setCpLimit(1);
			}else if(!"".equals(cplimit)&&cplimit.equals(Constant.RULE_THREE_DAY.toString())){
				ttCrmComplaintPO2.setCpLimit(3);
			}else if(!"".equals(cplimit)&&cplimit.equals(Constant.RULE_SEVEN_DAY.toString())){
				ttCrmComplaintPO2.setCpLimit(7);
			}		
			if(!"".equals(cpObject))ttCrmComplaintPO2.setCpObject(Long.parseLong(cpObject));
			if(!"".equals(vehicleCompany))ttCrmComplaintPO2.setCpObject(Long.parseLong(vehicleCompany));
			if(!"".equals(dealuser))ttCrmComplaintPO2.setCpObject(Long.parseLong(dealuser));
			ttCrmComplaintPO2.setCpContent(complaintContent);
			ttCrmComplaintPO2.setCpDealMode(Integer.parseInt(dealModel));
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			
			//插入投诉咨询回复记录
			TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintDealRecordPO.setCdId(new Long(SequenceManager.getSequence("")));
			ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
			ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCpContent(rcont);
			ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCreateDate(new Date());
			if(bizType.equals(Constant.TYPE_COMPLAIN)){
				//投诉
				if(dealModel.equals(Constant.COMPLAINT_PROCESS_TURN.toString())){
					ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT));
					ttCrmComplaintPO2.setCpDealUser(Long.parseLong(dealuser));
					ttCrmComplaintPO2.setCpDealUserName(commonUtilActions.getDealUserName(Long.parseLong(dealuser)));
					ttCrmComplaintDealRecordPO.setCpNextDealId(Long.parseLong(dealuser));
				}
				dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
				dao.insert(ttCrmComplaintDealRecordPO);
				act.setOutData("success", "true");
			}else if(bizType.equals(Constant.TYPE_CONSULT)){				
				//咨询
				//已处理
				if(dealModel.equals(Constant.CONSULT_PROCESS_FINISH.toString())){
					ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_FINISH));
					ttCrmComplaintPO2.setCpDealUser(logonUser.getUserId());
					ttCrmComplaintPO2.setCpDealUserName(logonUser.getName());
					ttCrmComplaintDealRecordPO.setCpContent(rcont);
					ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_FINISH));
				//待处理
				}else if(dealModel.equals(Constant.CONSULT_PROCESS_WAIT.toString())){
					ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_WAIT));
					ttCrmComplaintPO2.setCpDealUser(logonUser.getUserId());
					ttCrmComplaintPO2.setCpDealUserName(logonUser.getName());
					ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_WAIT));
				}
				
				TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)dao.select(ttCrmComplaintPO1).get(0);
				if(ttCrmComplaintPO3.getVar().toString().equals(var)){
					ttCrmComplaintPO2.setVar(Integer.parseInt(var)+1);
					dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
					dao.insert(ttCrmComplaintDealRecordPO);
					act.setOutData("success", "true");
				}else{
					act.setOutData("success", "varFalse");
				}
			}	
			
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理咨询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//业务类型 只处理咨询
	private List<Map<String,Object>> getBizType(){
		List<Map<String,Object>> bizTypeList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("id", Constant.TYPE_COMPLAIN);
//		map.put("name", Constant.TYPE_COMPLAIN_NAME);
//		bizTypeList.add(map);
//		map = new HashMap<String, Object>();
		map.put("id", Constant.TYPE_CONSULT);
		map.put("name", Constant.TYPE_CONSULT_NAME);
		bizTypeList.add(map);
		return bizTypeList;
	}
}
