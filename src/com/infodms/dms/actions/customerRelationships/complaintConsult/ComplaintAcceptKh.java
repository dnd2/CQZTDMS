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
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptMgrDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmComplaintDealRecordPO;
import com.infodms.dms.po.TtCrmComplaintPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/**
 * 咨询投诉处理（客户专员）ACTIONS
 * @ClassName     : ComplaintAccept 
 * @Description   : 投诉受理
 * @author        : pengbo
 * CreateDate     : 2017-7-12
 */
public class ComplaintAcceptKh {
	private static Logger logger = Logger.getLogger(ComplaintAcceptKh.class);
	//投诉咨询处理初始化页面  
	private final String ComplaintAcceptQueryUrl = "/jsp/customerRelationships/complaintConsult/complaintAcceptKhQuery.jsp";
	//处理页面
	private final String waitConsultKhUrl = "/jsp/customerRelationships/complaintConsult/waitConsultKh.jsp";
	
	//处理页面
	private final String waitConsultKhUrl2 = "/jsp/customerRelationships/complaintConsult/waitConsultKh2.jsp";
	//咨询处理分派
	private final String ComplaintDealerSetUrl = "/jsp/customerRelationships/complaintConsult/ComplaintDealerSet.jsp";

	
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	private ComplaintAcceptMgrDao dao = ComplaintAcceptMgrDao.getInstance();
	
	
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

	// 投诉受理初始化
	public void getComplaintAcceptKhInit() {
		try {
			act.setForword(ComplaintAcceptQueryUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "咨询投诉处理初始化");
			logger.error(logger, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 处理始化跳转（客户专员）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void doComplaintKH(){
		try{	
			String cpid = CommonUtils.checkNull(request.getParamValue("cpId"));
			String bizType = CommonUtils.checkNull(request.getParamValue("bizType"));
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			act.setOutData("dealRecordList", dao.queryDealRecord(Long.parseLong(cpid)));
			//业务类型
			act.setOutData("bizTypeList", getBizType());
			//投诉对象
			act.setOutData("cpObjectList", commonUtilActions.getOfficeOrg());
			//小区
			act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			Map<String,Object> complaintAcceptMap =null;
			Map<String,Object> map = dao.queryComplaintRecord(Long.parseLong(cpid),String.valueOf(Constant.VOUCHER_STATUS_03));
			act.setOutData("map", map);
			if(Constant.VOUCHER_TYPE_02==Integer.parseInt(bizType)){
				act.setForword(waitConsultKhUrl);
				complaintAcceptMap = dao.queryComplaintInfor(Long.parseLong(cpid));	
			}else{

				complaintAcceptMap = dao.queryComplaintInfor2(Long.parseLong(cpid));
				act.setForword(waitConsultKhUrl2);		
			}
			act.setOutData("complaintAcceptMap", complaintAcceptMap);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"处理初始化跳转（客户专员）");
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
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			String contType = CommonUtils.checkNull(request.getParamValue("contType")); //内容类型			
			String cplevel = CommonUtils.checkNull(request.getParamValue("cplevel"));  	//复杂度
			String cplimit = CommonUtils.checkNull(request.getParamValue("cplimit"));  //处理期限
			String cpObject = CommonUtils.checkNull(request.getParamValue("cpObject")); //报怨所属大区
			String vcPro = CommonUtils.checkNull(request.getParamValue("vcPro")); //报怨所属小区
			String vehicleCompany = CommonUtils.checkNull(request.getParamValue("vehicleCompany")); //报怨经销商  
			String complaintContent = CommonUtils.checkNull(request.getParamValue("complaintContent")); //投诉咨询内容	
			String tmOrg = CommonUtils.checkNull(request.getParamValue("cpObj"));  
			
			String dealStatus = CommonUtils.checkNull(request.getParamValue("dealStatus")); //处理状态 				
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont")); 	//回复内容
			String biztype = CommonUtils.checkNull(request.getParamValue("biztype")); //原单据类型
			String bizType = CommonUtils.checkNull(request.getParamValue("bizType")); //现单据类型
			String cpid = CommonUtils.checkNull(request.getParamValue("cpId"));
			int dealStatus1;
			if("0".equals(dealStatus)){
				dealStatus=String.valueOf(Constant.VOUCHER_STATUS_03);
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
			//判断是否咨询转投诉
			if(biztype.equals(bizType)){
				
			}else{
				if(bizType.equals(String.valueOf(Constant.VOUCHER_TYPE_01))){
					ttCrmComplaintPO2.setCpNo(Utility.GetBillNo("", "", "TS"));
					ttCrmComplaintPO2.setCpBizType(Integer.parseInt(bizType));
					if(!"".equals(cplevel)) ttCrmComplaintPO2.setCpLevel(Integer.parseInt(cplevel));
					if(!"".equals(tmOrg)){ 
						ttCrmComplaintPO2.setCpObject(Long.parseLong(tmOrg));
					}else{
						if(!"".equals(vehicleCompany))ttCrmComplaintPO2.setCpObject(Long.parseLong(vehicleCompany));	
						}
					if(!"".equals(cplimit)){
						ttCrmComplaintPO2.setCpLimit(commonUtilActions.turnCodeToDayForComplaintLimit(cplimit));
					}
					if(!"".equals(cpObject))ttCrmComplaintPO2.setCpObjectOrg(Long.parseLong(cpObject));
					if(!"".equals(vcPro))ttCrmComplaintPO2.setCpObjectSmallOrg(Long.parseLong(vcPro));
					
					ttCrmComplaintPO2.setCpContent(complaintContent);
				}
			}
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			dao.insert(ttCrmComplaintDealRecordPO);
			act.setOutData("success", "true");
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉咨询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	* 投诉咨询上报（客户专员）
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
			ttCrmComplaintDealRecordPO.setCpContent(logonUser.getName()+"   客户专员上报！");
			
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			dao.insert(ttCrmComplaintDealRecordPO);
			act.setOutData("success", "true");
			}catch(Exception e){
					BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉咨询上报（客户专员）");
					logger.error(logger,e1);
					act.setException(e1);
				}
			}
	
	/**
	* 投诉咨询关闭（客户专员）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void complaintKHClose(){
		act.getResponse().setContentType("application/json");
		try{			 				
			String cpid = CommonUtils.checkNull(request.getParamValue("cpId"));
			String dealStatus=String.valueOf(Constant.VOUCHER_STATUS_08);
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
			
			ttCrmComplaintPO2.setCpStatus(Integer.parseInt(dealStatus));
			ttCrmComplaintDealRecordPO.setCpStatus(Constant.DISPOSE_STATUS_05);
			ttCrmComplaintDealRecordPO.setCpContent(logonUser.getName()+"   客户专员关闭！");
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			dao.insert(ttCrmComplaintDealRecordPO);
			act.setOutData("success", "true");
			}catch(Exception e){
					BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉咨询关闭（客户专员）");
					logger.error(logger,e1);
					act.setException(e1);
				}
			}
	
	/**
	* 投诉咨询分派（客户专员）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void complaintKHSet(){
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
			if("".equals(orgId)){
				dealStatus=String.valueOf(Constant.VOUCHER_STATUS_05);
				ttCrmComplaintPO2.setCpDealId(Long.valueOf(vehicleCompany));
			}else{
				dealStatus=String.valueOf(Constant.VOUCHER_STATUS_04);
				ttCrmComplaintPO2.setCpDealId(Long.valueOf(orgId));
			}
			ttCrmComplaintPO2.setCpStatus(Integer.parseInt(dealStatus));
			ttCrmComplaintDealRecordPO.setCpStatus(Constant.DISPOSE_STATUS_03);
			ttCrmComplaintDealRecordPO.setCpContent(logonUser.getName()+"   客户专员分派！");
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
