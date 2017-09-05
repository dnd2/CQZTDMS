package com.infodms.dms.actions.customerRelationships.search;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.customerRelationships.complaint.ComplaintDisposalOEM;
import com.infodms.dms.actions.customerRelationships.export.ExcelUtil;
import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ComplaintDisposalBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.customerRelationships.ComplaintAuditDao;
import com.infodms.dms.dao.customerRelationships.ComplaintDisposalDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrComplaintsAuditPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;


/**
 * 
 * <p>Title:ComplaintSearch.java</p>
 *
 * <p>Description: 客户投诉查询业务逻辑层 </p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-7</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class ComplaintSearch {
	
	private static Logger logger = Logger.getLogger(ComplaintDisposalOEM.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	// 客户投诉查询(总部)初始化页面
	private final String complaintSearchOEMInitUrl = "/jsp/customerRelationships/complaintSearch/complaintSearchOEM.jsp";
	
	// 客户投诉明细页面
	private final String complaintDetailUrl = "/jsp/customerRelationships/complaintSearch/complaintDetail.jsp";
	
	// 客户投诉查询(区域)
	private final String complaintSearchAreaInitUrl = "/jsp/customerRelationships/complaintSearch/complaintSearchArea.jsp";
	
	// 客户投诉查询(服务站)
	private final String complaintSearchDealerInitUrl = "/jsp/customerRelationships/complaintSearch/complaintSearchDealer.jsp";
	
	//审核备注查询
	private final String AuditContentUrl = "/jsp/customerRelationships/complaintSearch/auditContent.jsp";
	
	//显示全部信息页面
	private final String SHOW_ALL_MSG_URL = "/jsp/customerRelationships/complaintSearch/showAllMsg.jsp" ;
	
	//显示备件名称和供应商
	private final String SHOW_PART_URL = "/jsp/customerRelationships/complaintSearch/showPart.jsp" ;
	
	/**
	 * 客户投诉查询(总部)初始化
	 */
	public void complaintSearchOemInit(){
		try{
			act.setForword(complaintSearchOEMInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉查询(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	/**
	 * 查询客户投诉处理信息(总部)
	 */
	public void queryComplaintOem(){
		try{
			ComplaintDisposalBean cdbean = assembleBean();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			PageResult<Map<String, Object>> ps = cddao.queryComplaintDisposal(cdbean,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);     
		}catch(Exception e) {
			//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户投诉查询(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	private ComplaintDisposalBean assembleBean() {
		String linkman = CommonUtils.checkNull(request.getParamValue("linkman"));  				//客户姓名
		String tel = CommonUtils.checkNull(request.getParamValue("tel"));	            		//客户电话
		String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));   		//投诉时间
		String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));       		//投诉时间
		String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));   		//车型
		String complainobject = CommonUtils.checkNull(request.getParamValue("complainobject")); //radio选择
		String ownOrgId = CommonUtils.checkNull(request.getParamValue("ownOrgId"));     		//所属区域id
		String ownOrgCode = CommonUtils.checkNull(request.getParamValue("ownOrgCode")); 		//所属区域code
		String compDealerId = CommonUtils.checkNull(request.getParamValue("compDealerId")); 	//投诉经销商id
		String compDealerCode = CommonUtils.checkNull(request.getParamValue("compDealerCode")); //投诉经销商code
		String compSource = CommonUtils.checkNull(request.getParamValue("compSource")); 		//投诉来源
		String compLevel = CommonUtils.checkNull(request.getParamValue("compLevel"));   		//投诉等级
		String compType = CommonUtils.checkNull(request.getParamValue("compType"));     		//投诉类型
		String compType2 = CommonUtils.checkNull(request.getParamValue("compType2"));     		//投诉类型2
		String compStatus = CommonUtils.checkNull(request.getParamValue("compStatus")); 		//投诉状态
		String auditResult = CommonUtils.checkNull(request.getParamValue("auditResult")); 		//处理中状态
		String compCode = CommonUtils.checkNull(request.getParamValue("compCode")); 			//投诉编号
		String dmsStatus = CommonUtils.checkNull(request.getParamValue("dmsStatus")); 			//DMS处理状态
		String licenseNo = CommonUtils.checkNull(request.getParamValue("licenseNo")); 			//车牌号
		
		ComplaintDisposalBean cdbean = new ComplaintDisposalBean();
		cdbean.setLinkman(linkman);
		cdbean.setTel(tel);
		cdbean.setBeginTime(beginTime);
		cdbean.setEndTime(endTime);
		cdbean.setModelCode(modelCode);
		if("0".equals(complainobject)){
			cdbean.setOwnOrgId(ownOrgId);
			cdbean.setOwnOrgCode(ownOrgCode);
		}else{
			cdbean.setCompDealerId(compDealerId);
			cdbean.setCompDealerCode(compDealerCode);
		}
		
		
		cdbean.setCompSource(compSource);
		cdbean.setCompLevel(compLevel);
		cdbean.setCompType(compType);
		cdbean.setCompType2(compType2);
		cdbean.setStatus(compStatus);
		cdbean.setAuditResult(auditResult);
		cdbean.setCompCode(compCode);
		cdbean.setIntStatus(dmsStatus);
		cdbean.setLicenseNo(licenseNo);
		return cdbean;
	}

	/**
	 * 查看客户投诉处理明细
	 */
	public void viewComplaintDetail(){
		try{
			String compId = CommonUtils.checkNull(request.getParamValue("compId"));
			
//			String status = CommonUtils.checkNull(request.getParamValue("status"));
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			
			// 取得客户投诉表的基本信息
			Map<String, Object> complaintMap = cddao.getComplaintById(compId); 
			
			ComplaintAuditDao  cadao = new ComplaintAuditDao();
			
			
			List<Map<String, Object>> listCheckBox = cddao.getCheckBox();   //多选框
			
			List<Map<String, Object>> detailList = cadao.getAllDetailByCompId(compId); // 通过compId查询所有的处理明细历史
			
			act.setOutData("ListCheckBox", listCheckBox);
			act.setOutData("detailList", detailList);
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId()); //取车系
			act.setOutData("complaintMap", complaintMap);
			
			
			act.setForword(complaintDetailUrl);
			
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户投诉处理明细历史查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 客户投诉处理查询(区域)
	 */
	public void complaintSearchAreaInit(){
		try{
			act.setForword(complaintSearchAreaInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉查询(区域)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	/**
	 * 查询客户投诉处理信息(区域)
	 */
	public void queryComplaintArea(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			String linkman = CommonUtils.checkNull(request.getParamValue("linkman"));  				//客户姓名
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));	            		//客户电话
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));   		//投诉时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));       		//投诉时间
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));   		//车型
			String compDealerId = CommonUtils.checkNull(request.getParamValue("compDealerId")); 	//投诉经销商ID
			String compDealerCode = CommonUtils.checkNull(request.getParamValue("compDealerCode")); //投诉经销商CODE
			String compSource = CommonUtils.checkNull(request.getParamValue("compSource")); 		//投诉来源
			String compLevel = CommonUtils.checkNull(request.getParamValue("compLevel"));   		//投诉等级
			String compType = CommonUtils.checkNull(request.getParamValue("compType"));     		//投诉类型
			String compType2 = CommonUtils.checkNull(request.getParamValue("compType2"));     		//投诉类型2
			String compStatus = CommonUtils.checkNull(request.getParamValue("compStatus")); 		//投诉状态
			String auditResult = CommonUtils.checkNull(request.getParamValue("auditResult")); 		//处理中状态
			String compCode = CommonUtils.checkNull(request.getParamValue("compCode")); 			//投诉编号
			String licenseNo = CommonUtils.checkNull(request.getParamValue("licenseNo")); 			//车牌号
			
			String orgId = String.valueOf(logonUser.getOrgId());                                    //orgId
			
			ComplaintDisposalBean cdbean = new ComplaintDisposalBean();
			cdbean.setLinkman(linkman);
			cdbean.setTel(tel);
			cdbean.setBeginTime(beginTime);
			cdbean.setEndTime(endTime);
			cdbean.setModelCode(modelCode);
			cdbean.setCompDealerId(compDealerId);
			cdbean.setCompDealerCode(compDealerCode);
			cdbean.setCompSource(compSource);
			cdbean.setCompLevel(compLevel);
			cdbean.setCompType(compType);
			cdbean.setCompType2(compType2);
			cdbean.setStatus(compStatus);
			cdbean.setAuditResult(auditResult);
			cdbean.setCompCode(compCode);
			cdbean.setLicenseNo(licenseNo);
			cdbean.setOrgId(orgId);
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String, Object>> ps = cddao.queryComplaintDisposal(cdbean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);     
			
		}catch(Exception e) {
			//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户投诉查询(区域)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 客户投诉处理查询(服务站)
	 */
	public void complaintSearchDealerInit(){
		try{
			act.setForword(complaintSearchDealerInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉查询(服务站)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询客户投诉处理信息(服务站)
	 */
	public void queryComplaintDealer(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			String linkman = CommonUtils.checkNull(request.getParamValue("linkman"));  				//客户姓名
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));	            		//客户电话
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));   		//投诉时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));       		//投诉时间
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));   		//车型
			String compDealerId = CommonUtils.checkNull(request.getParamValue("compDealerId")); 	//投诉经销商id
			String compDealerCode = CommonUtils.checkNull(request.getParamValue("compDealerCode")); //投诉经销商code
			String compSource = CommonUtils.checkNull(request.getParamValue("compSource")); 		//投诉来源
			String compLevel = CommonUtils.checkNull(request.getParamValue("compLevel"));   		//投诉等级
			String compType = CommonUtils.checkNull(request.getParamValue("compType"));     		//投诉类型
			String compType2 = CommonUtils.checkNull(request.getParamValue("compType2"));     		//投诉类型2
			String compStatus = CommonUtils.checkNull(request.getParamValue("compStatus")); 		//投诉状态
			String auditResult = CommonUtils.checkNull(request.getParamValue("auditResult")); 		//处理中状态
			String compCode = CommonUtils.checkNull(request.getParamValue("compCode")); 			//投诉编号
			String licenseNo = CommonUtils.checkNull(request.getParamValue("licenseNo")); 			//车牌号
			String dealerId = String.valueOf(logonUser.getDealerId());
			
			ComplaintDisposalBean cdbean = new ComplaintDisposalBean();
			cdbean.setLinkman(linkman);
			cdbean.setTel(tel);
			cdbean.setBeginTime(beginTime);
			cdbean.setEndTime(endTime);
			cdbean.setModelCode(modelCode);
			cdbean.setCompDealerId(compDealerId);
			cdbean.setCompDealerCode(compDealerCode);
			cdbean.setCompSource(compSource);
			cdbean.setCompLevel(compLevel);
			cdbean.setCompType(compType);
			cdbean.setCompType2(compType2);
			cdbean.setStatus(compStatus);
			cdbean.setAuditResult(auditResult);
			cdbean.setCompCode(compCode);
			cdbean.setLicenseNo(licenseNo);
			cdbean.setDealerId(dealerId);
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String, Object>> ps = cddao.queryComplaintDisposal(cdbean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);     
			
		}catch(Exception e) {
			//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户投诉处理(服务站)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 查看审批备注 
	 */
	public void showAuditContent(){
		try{
			String id = request.getParamValue("id");
			ComplaintAuditDao dao = new ComplaintAuditDao();
			Map<String, Object> map = dao.getContentMap(id);
			act.setOutData("map", map);
			act.setForword(AuditContentUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉查询(服务站)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 显示全剖内容
	 */
	public void showAllMsg(){
		try{
			Long id = Long.parseLong(request.getParamValue("value"));
			TtCrComplaintsAuditPO po = new TtCrComplaintsAuditPO();
			po.setId(id);
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			List<TtCrComplaintsAuditPO> lists = cddao.select(po);
			if(lists.size()>0)
				act.setOutData("msg", lists.get(0).getAuditContent());
			act.setForword(SHOW_ALL_MSG_URL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户投诉查询(服务站)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void showPart() {
		try {
			String partCode = new String(CommonUtils.checkNull(request.getParamValue("partCode")).
					getBytes("iso-8859-1"), "GBK");
			String supplier = new String(CommonUtils.checkNull(request.getParamValue("supplier")).
					getBytes("iso-8859-1"), "GBK");
			act.setOutData("partCode", partCode);
			act.setOutData("supplier", supplier);
			act.setForword(SHOW_PART_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户投诉查询(服务站)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: expAllComplaint 
	* @Description: TODO(导出全部查询记录) 
	* @throws
	 */
	public void expAllComplaint() {
		try {
			ComplaintDisposalBean cdbean = assembleBean();
			ComplaintDisposalDao dao = new ComplaintDisposalDao();
			PageResult<Map<String, Object>> tps = dao.queryComplaintDisposal(cdbean, Integer.MAX_VALUE, 1);
			List<Map<String, Object>> datas = tps.getRecords();
			if (null != datas && datas.size() > 0) {
				String ids = getIds(datas);
				if (Utility.testString(ids)) {
					List<Map<String, Object>> maps = dao.queryExportComplaint(ids);
					ResponseWrapper response = act.getResponse();
					ExcelUtil.expExcel(response, maps);
				}
			}
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉查询(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	private String getIds(List<Map<String, Object>> datas) {
		StringBuffer ids = new StringBuffer();
		for (Map<String, Object> data : datas) {
			ids.append(data.get("COMP_ID")).append(",");
		}
		ids.deleteCharAt(ids.length() - 1);
		return ids.toString();
	}

	
}
