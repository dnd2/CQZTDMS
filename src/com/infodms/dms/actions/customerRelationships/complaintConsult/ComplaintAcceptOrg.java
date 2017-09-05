package com.infodms.dms.actions.customerRelationships.complaintConsult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptMgrDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmComplaintDealRecordPO;
import com.infodms.dms.po.TtCrmComplaintPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class ComplaintAcceptOrg {
	private static Logger logger = Logger.getLogger(ComplaintAcceptOrg.class);
	//投诉咨询处理初始化页面  
	private final String ComplaintAcceptQueryUrl = "/jsp/customerRelationships/complaintConsult/complaintAcceptOrgQuery.jsp";
	//处理页面
	private final String waitConsultOrgUrl = "/jsp/customerRelationships/complaintConsult/waitConsultOrg.jsp";
	//咨询处理分派
	private final String ComplaintDealerSetUrl = "/jsp/customerRelationships/complaintConsult/ComplaintDealerSetOrg.jsp";
	
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	private ComplaintAcceptMgrDao dao = ComplaintAcceptMgrDao.getInstance();
	
	// 投诉受理初始化
	public void getComplaintAcceptOrgInit() {
		try {
			act.setForword(ComplaintAcceptQueryUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "咨询投诉处理初始化");
			logger.error(logger, e1);
			act.setException(e1);
		}
	}
	/**
	* 投诉咨询分派（客户专员）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void complaintDealerSetInit() {
		try {
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			String cpid = CommonUtils.checkNull(request.getParamValue("cpId"));
			List<Map<String, Object>> orgList = dao.getOrgList(2, Constant.ORG_TYPE_OEM);
			//分派大区
			act.setOutData("orglist", orgList);
			//ID
			act.setOutData("cpid", cpid);
			//小区
			act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			act.setForword(ComplaintDealerSetUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "投诉咨询分派（客户专员）");
			logger.error(logger, e1);
			act.setException(e1);
		}
	}	
	/**
	 * 处理始化跳转（大区）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void doComplaintOrg(){
		try{	
			String cpid = CommonUtils.checkNull(request.getParamValue("cpId"));
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			act.setOutData("dealRecordList", dao.queryDealRecord(Long.parseLong(cpid)));
			//业务类型
			act.setOutData("bizTypeList", getBizType());
			//投诉对象
			act.setOutData("cpObjectList", commonUtilActions.getOfficeOrg());
			//小区
			act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			Map<String,Object> complaintAcceptMap = dao.queryComplaintInfor(Long.parseLong(cpid));	
			Map<String,Object> map = dao.queryComplaintRecord(Long.parseLong(cpid),String.valueOf(Constant.VOUCHER_STATUS_04));
			act.setOutData("complaintAcceptMap", complaintAcceptMap);
			act.setOutData("map", map);
			act.setForword(waitConsultOrgUrl);			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"处理初始化跳转（大区）");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//业务类型
	private List<Map<String,Object>> getBizType(){
			List<Map<String,Object>> bizTypeList = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id", Constant.VOUCHER_TYPE_01);
			map.put("name", Constant.VOUCHER_COMPLAIN_NAME);
			bizTypeList.add(map);
			map = new HashMap<String, Object>();
			map.put("id", Constant.VOUCHER_TYPE_02);
			map.put("name", Constant.VOUCHER_CONSULT_NAME);
			bizTypeList.add(map);
			return bizTypeList;
		}
	/**
	 * 咨询处理
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void adviceUpdateSubmit(){
		act.getResponse().setContentType("application/json");
		try{			 				
			String dealStatus = CommonUtils.checkNull(request.getParamValue("dealStatus")); //处理状态 				
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont")); 	
			String cpid = CommonUtils.checkNull(request.getParamValue("cpId"));
			int dealStatus1;
			if("0".equals(dealStatus)){
				dealStatus=String.valueOf(Constant.VOUCHER_STATUS_04);
				dealStatus1=Constant.DISPOSE_STATUS_06;
			}else{
				dealStatus=String.valueOf(Constant.VOUCHER_STATUS_06);
				dealStatus1=Constant.DISPOSE_STATUS_02;
			}
			//更新投诉咨询相关内容
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			
			TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)dao.select(ttCrmComplaintPO1).get(0);
			
			//插入投诉咨询回复记录
			TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
			String cdId=SequenceManager.getSequence("");
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintDealRecordPO.setCdId(new Long(cdId));
			ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
			ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCreateDate(new Date());
			ttCrmComplaintDealRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDealRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealDealer());
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			
			ttCrmComplaintPO2.setCpStatus(Integer.parseInt(dealStatus));
			ttCrmComplaintDealRecordPO.setCpStatus(dealStatus1);
			ttCrmComplaintDealRecordPO.setCpContent(ccont);
			ttCrmComplaintPO2.setCpDealId(logonUser.getUserId());
			
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			dao.insert(ttCrmComplaintDealRecordPO);
			act.setOutData("success", "true");
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"咨询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	* 投诉咨询上报（大区）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void complaintReportInit(){
		act.getResponse().setContentType("application/json");
		try{			 				
			String cpid = CommonUtils.checkNull(request.getParamValue("cpId"));
			String dealStatus=String.valueOf(Constant.VOUCHER_STATUS_07);
			//更新投诉咨询相关内容
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			
			TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)dao.select(ttCrmComplaintPO1).get(0);
			
			//插入投诉咨询回复记录
			TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
			String cdId=SequenceManager.getSequence("");
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintDealRecordPO.setCdId(new Long(cdId));
			ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
			ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCreateDate(new Date());
			ttCrmComplaintDealRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDealRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealDealer());
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			
			ttCrmComplaintPO2.setCpStatus(Integer.parseInt(dealStatus));
			ttCrmComplaintDealRecordPO.setCpStatus(Constant.DISPOSE_STATUS_04);
			ttCrmComplaintDealRecordPO.setCpContent(logonUser.getName()+"    大区上报");
			
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			dao.insert(ttCrmComplaintDealRecordPO);
			act.setOutData("success", "true");
			}catch(Exception e){
				BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉咨询上报（大区）");
				logger.error(logger,e1);
				act.setException(e1);
				}
			}
	/**
	* 投诉咨询分派（大区）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void complaintOrgSet(){
		act.getResponse().setContentType("application/json");
		try{			 				
			String cpid = CommonUtils.checkNull(request.getParamValue("cpId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String vehicleCompany = CommonUtils.checkNull(request.getParamValue("vehicleCompany"));
			String dealStatus="";
			//更新投诉咨询相关内容
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			ttCrmComplaintPO2.setCpCloseDate(new Date());
			ttCrmComplaintPO2.setCpCloseUser(logonUser.getUserId());
			TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)dao.select(ttCrmComplaintPO1).get(0);
			
			//插入投诉咨询回复记录
			TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
			String cdId=SequenceManager.getSequence("");
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintDealRecordPO.setCdId(new Long(cdId));
			ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
			ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCreateDate(new Date());
			ttCrmComplaintDealRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDealRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealDealer());
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			dealStatus=String.valueOf(Constant.VOUCHER_STATUS_05);
			ttCrmComplaintPO2.setCpDealId(Long.valueOf(vehicleCompany));
			ttCrmComplaintPO2.setCpStatus(Integer.parseInt(dealStatus));
			ttCrmComplaintDealRecordPO.setCpStatus(Constant.DISPOSE_STATUS_03);
			ttCrmComplaintDealRecordPO.setCpContent(logonUser.getName()+"   大区分派！");
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			dao.insert(ttCrmComplaintDealRecordPO);
			act.setOutData("success", "true");
			}catch(Exception e){
					BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉咨询分派（客户专员）");
					logger.error(logger,e1);
					act.setException(e1);
				}
			}
}
