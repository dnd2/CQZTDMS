/**********************************************************************
* <pre>
* FILE : BackChangeApproveService.java
* CLASS : BackChangeApproveService
* 
* AUTHOR : WangJinBao
*
* FUNCTION : 退换车申请书技术支持室审核Action.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-20| WangJinBao  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.feedbackmng.approve;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.BackChangeApplyMantainBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.BackChangeApproveServiceDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtIfExchangeAuditPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;


/**
 * Function       :  退换车申请书技术支持室审核
 * @author        :  wangjinbao
 * CreateDate     :  2010-05-20
 * @version       :  0.1
 */
public class BackChangeApproveService {
	private Logger logger = Logger.getLogger(BackChangeApproveService.class);
	private final BackChangeApproveServiceDao dao = BackChangeApproveServiceDao.getInstance();
	
	private final String BACK_CHANGE_APPROVE_URL = "/jsp/feedbackMng/approve/backChangeApprovalServiceSearch.jsp";//主页面
	private final String BACK_CHANGE_APPROVE_AUDIT_URL = "/jsp/feedbackMng/approve/backChangeApprovalServiceDo.jsp";//审核页面
	private final String BACK_CHANGE_APPLY_DETAIL_URL = "/jsp/feedbackMng/apply/backChangeApplylDetail.jsp";//明细页面

	/**
	 * 退换车申请书技术支持室审核查询初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void backChangeApproveServiceInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(BACK_CHANGE_APPROVE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书技术支持室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 待技术支持室审核的退换车申请书查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void backChangeApproveServiceQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				// 工单号
				String orderId = request.getParamValue("ORDER_ID");
				//VIN码
				String vin = request.getParamValue("VIN");
				//申报开始时间
				String beginTime = request.getParamValue("beginTime");
				//申报结束时间
				String endTime = request.getParamValue("endTime");
				//申请单位id
				String delearid = request.getParamValue("dealerId");
				//申请单位id
				String delearCode = request.getParamValue("delearCode");
				//经销商名称
				String delearName = request.getParamValue("DEALER_NAME");				
				//退换类型
				String exType = request.getParamValue("EX_TYPE");
				//车系
				String seriesid = request.getParamValue("vehicleSeriesList");
				//当开始时间和结束时间相同时
				if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
						beginTime = beginTime+" 00:00:00";
						endTime = endTime+" 23:59:59";
				}
				if (Utility.testString(orderId)) {
					sb.append(" and t.order_id like ? ");
					params.add("%"+orderId+"%");
				}
				if (Utility.testString(delearCode)) {
					sb.append(Utility.getConSqlByParamForEqual(delearCode, params, "d", "dealer_code"));
				}
				if (Utility.testString(vin)) {
					sb.append(" and t.vin like ? ");
					params.add("%"+vin+"%");
				}
				if (Utility.testString(exType)) {
					sb.append(" and t.ex_type = ? ");
					params.add(exType);
				}
				if(null!=companyId&&!"".equals(companyId)){
					sb.append(" and t.company_id = ? ");
					params.add(companyId);
				}
				if (Utility.testString(seriesid) && !"-1".equals(seriesid)) {
					sb.append(" and g.group_id = ? ");
					params.add(seriesid);
				}
				if (beginTime != null && !"".equals(beginTime.trim())) {
					sb.append(" and t.ex_date >= to_date(?,'yyyy-mm-dd hh24:mi:ss') ");
					params.add(beginTime);
				}
				if (endTime != null && !"".equals(endTime.trim())) {
					sb.append(" and t.ex_date <= to_date(?,'yyyy-mm-dd hh24:mi:ss') ");
					params.add(endTime);
				}
				if (Utility.testString(delearid)) {
					sb.append(" and t.dealer_id = ? ");
					params.add(delearid);
				}
				if (Utility.testString(delearName)) {
					sb.append(" and d.dealer_name like ? ");
					params.add("%"+delearName+"%");
				}				
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.backChangeApproveServiceQuery(Constant.PAGE_SIZE, curPage,sb.toString(),params);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书技术支持室审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 退换车申请书技术支持室审核初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void backChangeApproveServiceAuditInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("ORDER_ID");//取得要审核的工单号
			BackChangeApplyMantainBean bcamBean = dao.queryDetailByOrderId(orderId);//取得基本信息
			List<BackChangeApplyMantainBean> MantainList=dao.getAuditInfoList(orderId);//取得审批信息
			request.setAttribute("MantainBean", bcamBean);
			request.setAttribute("MantainList", MantainList);
			String id = String.valueOf(bcamBean.getId());
			List<FsFileuploadPO> fileList=dao.queryAttachFileInfo(id);
			act.setOutData("fileList", fileList);
			act.setForword(BACK_CHANGE_APPROVE_AUDIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书大区审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 退换车申请书技术支持室审核
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void backChangeApproveServiceAudit(){ 
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String orderId=request.getParamValue("orderid");//工单号
		String auditContent= Utility.getString(request.getParamValue("content"));//审核意见
		String audit=request.getParamValue("audit");//审核通过/驳回;通过：p,驳回：r
		TtIfExchangeAuditPO  tIfExchangeAuditPO =new TtIfExchangeAuditPO();
		try{
				tIfExchangeAuditPO.setAuditDate(new Date());//审核时间
				tIfExchangeAuditPO.setAuditBy(logonUser.getUserId());//审核人
				tIfExchangeAuditPO.setOrgId(logonUser.getOrgId());  //审核人的组织id
				if("p".equals(audit)){//通过：Pass
					tIfExchangeAuditPO.setAuditStatus(Constant.MARKET_BACK_STATUS_TECH_PASS);//审批状态:技术支持室通过
					if(Utility.testString(auditContent)){
						tIfExchangeAuditPO.setAuditContent(auditContent);//审批意见
					}
				}else if("r".equals(audit)){
					tIfExchangeAuditPO.setAuditStatus(Constant.MARKET_BACK_STATUS_TECH_REJECT);//审批状态:技术支持室驳回
					tIfExchangeAuditPO.setAuditContent(auditContent);//审批意见
				}
				dao.ApprovalAudit(orderId, tIfExchangeAuditPO);
				backChangeApproveServiceInit();
		  }catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,"退换车申请书技术支持室审核");
			logger.error(logonUser,e1);
		 	act.setException(e1);
		  }
	}
	
	/**
	 * 退换车申请书详细页面
	 * @param null
	 * @return void
	 * @throws Exception 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void detailBackChangeApply() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("ORDER_ID");//取得要显示的工单号
			BackChangeApplyMantainBean bcamBean = dao.queryDetailByOrderId(orderId);//取得基本信息
			List<BackChangeApplyMantainBean> MantainList=dao.getAuditInfoList(orderId);//取得审批信息
			request.setAttribute("MantainBean", bcamBean);
			request.setAttribute("MantainList", MantainList);		
			String id = String.valueOf(bcamBean.getId());
			List<FsFileuploadPO> fileList=dao.queryAttachFileInfo(id);
			act.setOutData("fileList", fileList);
			act.setForword(BACK_CHANGE_APPLY_DETAIL_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}

}
