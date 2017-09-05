package com.infodms.dms.actions.customerRelationships.complaintConsult;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptDao;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptMgrDao;
import com.infodms.dms.dao.customerRelationships.ComplaintConsultDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 投诉咨询查询ACTIONS
 * @ClassName     : ComplaintQuery 
 * @Description   : 投诉咨询查询
 * @author        : pengbo
 * CreateDate     : 2017-7-11
 */
public class ComplaintQuery {
	private static Logger logger = Logger.getLogger(ComplaintQuery.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	private ComplaintAcceptMgrDao dao=ComplaintAcceptMgrDao.getInstance();
	//投诉咨询页面(坐席)
	private final String ComplaintQueryZxUrl = "/jsp/customerRelationships/complaintConsult/complaintQueryZx.jsp";
	//投诉咨询页面(客户专员)
	private final String ComplaintQueryKhUrl = "/jsp/customerRelationships/complaintConsult/complaintQueryKh.jsp";
	//投诉页面(大区)
	private final String ComplaintQueryOrgUrl = "/jsp/customerRelationships/complaintConsult/complaintQueryOrg.jsp";	
	//投诉页面(服务站)
	private final String ComplaintQueryDrUrl = "/jsp/customerRelationships/complaintConsult/complaintQueryDr.jsp";
	//详细查看页面
	private final String ComplaintQueryDetailUrl = "/jsp/customerRelationships/complaintConsult/complaintConsultDetail.jsp";
	
	/**
	 * 投诉咨询查询跳转初始化(坐席)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getComplaintQueryZxInit() {
			try {
				act.setForword(ComplaintQueryZxUrl);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "投诉咨询查询跳转初始化(坐席)");
				logger.error(logger, e1);
				act.setException(e1);
			}
		}
	/**
	 * 投诉咨询查询跳转初始化(客户专员)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getComplaintQueryKhInit() {
			try {
				act.setForword(ComplaintQueryKhUrl);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "投诉咨询查询跳转初始化(客户专员)");
				logger.error(logger, e1);
				act.setException(e1);
			}
		}	
	
	/**
	 * 投诉查询跳转初始化(大区)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getComplaintQueryOrgInit() {
			try {
				act.setForword(ComplaintQueryOrgUrl);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "投诉查询跳转初始化(大区)");
				logger.error(logger, e1);
				act.setException(e1);
			}
		}	
	/**
	 * 投诉跳转初始化(服务站)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getComplaintQueryDrInit() {
			try {
				act.setForword(ComplaintQueryDrUrl);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "投诉跳转初始化(服务站)");
				logger.error(logger, e1);
				act.setException(e1);
			}
		}		
	
	/**
	 * 投诉咨询信息查询（坐席、客户专员）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryComplaintZxAndKh(){
		act.getResponse().setContentType("application/json");
		try{
			
			String cpNo = CommonUtils.checkNull(request.getParamValue("cpNo"));//单据号  				
			String voucherType = CommonUtils.checkNull(request.getParamValue("voucherType")); //单据类型 				
			String voucherStatus = CommonUtils.checkNull(request.getParamValue("voucherStatus")); //单据状态 				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  	//单据级别
			String acceptStart = CommonUtils.checkNull(request.getParamValue("acceptStart")); //受理时间 	
			String acceptEnd = CommonUtils.checkNull(request.getParamValue("acceptEnd"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  	//关闭时间			
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  	
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cpNo", cpNo);
			map.put("voucherType", voucherType);
			map.put("voucherStatus", voucherStatus);
			map.put("level", level);
			map.put("acceptStart", acceptStart);
			map.put("acceptEnd", acceptEnd);
			map.put("dealStart", dealStart);
			map.put("dealEnd", dealEnd);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = dao.queryConsultInfoZxAndKh(map,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉咨询查询（坐席）");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 投诉信息查询（大区和服务站）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryComplaintOrgAndDr(){
		act.getResponse().setContentType("application/json");
		try{
			
			String cpNo = CommonUtils.checkNull(request.getParamValue("cpNo"));//单据号  				
			String voucherType = CommonUtils.checkNull(request.getParamValue("voucherType")); //单据类型 				
			String voucherStatus = CommonUtils.checkNull(request.getParamValue("voucherStatus")); //单据状态 				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  	//单据级别
			String acceptStart = CommonUtils.checkNull(request.getParamValue("acceptStart")); //受理时间 	
			String acceptEnd = CommonUtils.checkNull(request.getParamValue("acceptEnd"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  	//关闭时间			
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  	
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cpNo", cpNo);
			map.put("voucherType", voucherType);
			map.put("voucherStatus", voucherStatus);
			map.put("level", level);
			map.put("acceptStart", acceptStart);
			map.put("acceptEnd", acceptEnd);
			map.put("dealStart", dealStart);
			map.put("dealEnd", dealEnd);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = dao.queryConsultInfoOrgAndDr(map,logonUser.getUserId(),Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉咨询查询（坐席）");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}		
	
	
	
	/**
	 * 投诉咨询查询（坐席）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryComplaintSearch(){
		act.getResponse().setContentType("application/json");
		try{
			
			String cpNo = CommonUtils.checkNull(request.getParamValue("cpNo"));//单据号  				
			String voucherType = CommonUtils.checkNull(request.getParamValue("voucherType")); //单据类型 				
			String voucherStatus = CommonUtils.checkNull(request.getParamValue("voucherStatus")); //单据状态 				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  	//单据级别
			String acceptStart = CommonUtils.checkNull(request.getParamValue("acceptStart")); //受理时间 	
			String acceptEnd = CommonUtils.checkNull(request.getParamValue("acceptEnd"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  	//关闭时间			
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  	
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cpNo", cpNo);
			map.put("voucherType", voucherType);
			map.put("voucherStatus", voucherStatus);
			map.put("level", level);
			map.put("acceptStart", acceptStart);
			map.put("acceptEnd", acceptEnd);
			map.put("dealStart", dealStart);
			map.put("dealEnd", dealEnd);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = dao.queryConsultInfo(map,logonUser.getUserId(),Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉咨询查询（坐席）");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 投诉咨询查询（客户专员）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryComplaintSearchKh(){
		act.getResponse().setContentType("application/json");
		try{
			
			String cpNo = CommonUtils.checkNull(request.getParamValue("cpNo"));//单据号  				
			String voucherType = CommonUtils.checkNull(request.getParamValue("voucherType")); //单据类型 				
			String voucherStatus = CommonUtils.checkNull(request.getParamValue("voucherStatus")); //单据状态 				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  	//单据级别
			String acceptStart = CommonUtils.checkNull(request.getParamValue("acceptStart")); //受理时间 	
			String acceptEnd = CommonUtils.checkNull(request.getParamValue("acceptEnd"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  	//关闭时间			
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  	
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cpNo", cpNo);
			map.put("voucherType", voucherType);
			map.put("voucherStatus", voucherStatus);
			map.put("level", level);
			map.put("acceptStart", acceptStart);
			map.put("acceptEnd", acceptEnd);
			map.put("dealStart", dealStart);
			map.put("dealEnd", dealEnd);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = dao.queryConsultInfoKh(map,logonUser.getUserId(),Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉咨询查询（大区）");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 投诉咨询查询（大区）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryComplaintSearchOrg(){
		act.getResponse().setContentType("application/json");
		try{
			
			String cpNo = CommonUtils.checkNull(request.getParamValue("cpNo"));//单据号  				
			String voucherType = CommonUtils.checkNull(request.getParamValue("voucherType")); //单据类型 				
			String voucherStatus = CommonUtils.checkNull(request.getParamValue("voucherStatus")); //单据状态 				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  	//单据级别
			String acceptStart = CommonUtils.checkNull(request.getParamValue("acceptStart")); //受理时间 	
			String acceptEnd = CommonUtils.checkNull(request.getParamValue("acceptEnd"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  	//关闭时间			
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  	
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cpNo", cpNo);
			map.put("voucherType", voucherType);
			map.put("voucherStatus", voucherStatus);
			map.put("level", level);
			map.put("acceptStart", acceptStart);
			map.put("acceptEnd", acceptEnd);
			map.put("dealStart", dealStart);
			map.put("dealEnd", dealEnd);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = dao.queryConsultInfoOrg(map,logonUser.getUserId(),Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉咨询查询（大区）");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
		
	/**
	 * 投诉咨询查询（服务站）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryComplaintSearchDr(){
		act.getResponse().setContentType("application/json");
		try{
			
			String cpNo = CommonUtils.checkNull(request.getParamValue("cpNo"));//单据号  				
			String voucherType = CommonUtils.checkNull(request.getParamValue("voucherType")); //单据类型 				
			String voucherStatus = CommonUtils.checkNull(request.getParamValue("voucherStatus")); //单据状态 				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  	//单据级别
			String acceptStart = CommonUtils.checkNull(request.getParamValue("acceptStart")); //受理时间 	
			String acceptEnd = CommonUtils.checkNull(request.getParamValue("acceptEnd"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  	//关闭时间			
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  	
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cpNo", cpNo);
			map.put("voucherType", voucherType);
			map.put("voucherStatus", voucherStatus);
			map.put("level", level);
			map.put("acceptStart", acceptStart);
			map.put("acceptEnd", acceptEnd);
			map.put("dealStart", dealStart);
			map.put("dealEnd", dealEnd);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = dao.queryConsultInfoDr(map,logonUser.getUserId(),Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉咨询查询（服务站）");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	// 投诉咨询记录详细
	public void complaintConsultWatch() {
		try {
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid"));
			String openPage = CommonUtils.checkNull(request.getParamValue("openPage"));
			Map<String, Object> complaintConsult = dao.queryComplaintInfor(Long.valueOf(cpid));
			act.setOutData("dealRecordList", dao.queryDealRecord(Long.parseLong(cpid)));
			act.setOutData("complaintConsult", complaintConsult);
			act.setOutData("openPage", openPage);
			act.setForword(ComplaintQueryDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "投诉咨询记录");
			logger.error(logger, e1);
			act.setException(e1);
		}
	}
}
