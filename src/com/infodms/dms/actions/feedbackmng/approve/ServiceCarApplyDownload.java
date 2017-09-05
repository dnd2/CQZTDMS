package com.infodms.dms.actions.feedbackmng.approve;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtIfServiceCarAuditBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.common.DealerUtilDAO;
import com.infodms.dms.dao.feedbackMng.ServiceCarApplyUplodaDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtIfServicecarAuditPO;
import com.infodms.dms.po.TtIfServicecarExtPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * <p>Title:ServiceInfoDao.java</p>
 *
 * <p>Description: 服务车申请表资料签收</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-07-28</p>
 *
 * @author subo
 * @version 1.0
 * @remark
 */
public class ServiceCarApplyDownload {
	public Logger logger = Logger.getLogger(ServiceActivityApplyCarAudit.class);
	private ServiceCarApplyUplodaDAO dao = ServiceCarApplyUplodaDAO
			.getInstance();
	private DealerUtilDAO cdao = DealerUtilDAO.getInstance();
	private final String serviceCarApplyTeamUploadURL = "/jsp/feedbackMng/approve/serviceCarApplyCarUpload.jsp";// 主页面
	private final String serviceCarApplyUploadURL = "/jsp/feedbackMng/approve/serviceCarApplyUploadGet.jsp";// 签收页面

	/**
	 * 
	 * @Title: servicecarapplyforward
	 * @Description: TODO(跳转到服务车表资料上传首页)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void serviceCarApplyDownloadInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.setOutData("seriesList", InfoFeedBackMng
					.getVehicleSeriesByDealerId());
			act.setForword(serviceCarApplyTeamUploadURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "服务车申请表资料上传");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: servicecarapplyQuery
	 * @Description: TODO(车厂端查询)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void serviceCarapplyDownloadQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		String dealerId = logonUser.getDealerId();
		LinkedList params = new LinkedList();
		String dealerIds = cdao.getDealerIdsToString(logonUser.getOrgId());

		// System.out.println(logonUser.getOrgId());
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			StringBuffer con = new StringBuffer();
			String right = request.getParamValue("RIGHT");
			String orderId = request.getParamValue("ORDER_ID");
			String strDate = request.getParamValue("CON_APPLY_DATE_START");
			String endDate = request.getParamValue("CON_APPLY_DATE_END");
			String modelId = request.getParamValue("MODEL_ID");
			String dealerName = request.getParamValue("DEALER_NAME");
			String dealerCode = request.getParamValue("dealerCode");
			// dealerId =
			// request.getParamValue("DEALER_ID")==null?null:Long.parseLong(request.getParamValue("DEALER_ID"));
			// 当开始时间和结束时间相同时
			if (null != strDate && !"".equals(strDate) && null != endDate
					&& !"".equals(endDate)) {
				strDate = strDate + " 00:00:00";
				endDate = endDate + " 23:59:59";
			}
			// 工单号like
			if (orderId != null && !"".equals(orderId)) {
				con.append(" and t.ORDER_ID like'%" + orderId + "%' "); //
			}
			// 介于开始时间
			if (strDate != null && !"".equals(strDate)) {
				con.append(" and t.app_date >= to_date('" + strDate
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 结束时间
			if (endDate != null && !"".equals(endDate)) {
				con.append(" and t.app_date <= to_date('" + endDate
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 经销商代码
			if (dealerId != null && !"".equals(dealerId)) {
				con.append(" and t.DEALER_ID='" + dealerId + "' ");
			}
			// 经销商代码
			if (Utility.testString(dealerCode)) {
				con.append(Utility.getConSqlByParamForEqual(dealerCode, params,
						"d", "dealer_code"));
			}
			// 经销商名称
			if (!"".equals(dealerName) && null != dealerName) {
				con.append(" and d.DEALER_NAME LIKE '" + dealerName + "%' ");
			}
			// 车型
			if (modelId != null && !"".equals(modelId)) {
				con.append(" and t.GROUP_ID='" + modelId + "' ");
			}
			if(null!=companyId&&!"".equals(companyId)){
				con.append(" and t.company_id = "+companyId);
			}
			// 经销商代码不为空
			/*
			 * if (!"".equals(dealerIds)&&null!=dealerIds){
			 * con.append(Utility.getConSqlByParamForEqual(dealerIds, param,
			 * "t", "DEALER_ID")); }
			 */
			// if ("TEAM".equals(right)){
			// con.append(" and
			// app_status="+Constant.SERVICE_APPLY_ACTIVE_STATUS_REPORTED+" ");
			// //经销商已上报
			// }else if ("SERVICE".equals(right)) {
			// con.append(" and
			// app_status="+Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS+" ");
			// //大区审核通过
			// }else if ("CAR".equals(right)) {
			// con.append(" and
			// app_status="+Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS+"
			// "); //售后审核通过
			// }
			con.append(" and app_status="
					+ Constant.SERVICE_APPLY_ACTIVE_CAR_SERVICE_GET
					+ " ");
			PageResult<TtIfServicecarExtPO> list = dao.applyUploadGetQuery(con
					.toString(), params, curPage, Constant.PAGE_SIZE);
			// List ls = list.getRecords();
			act.setOutData("ps", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务车申请表轿车事业部审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: serviceCarapplyUploadMaterialGet
	 * @Description: TODO(链接到车厂端附件下载签收页面)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void serviceCarapplyDownloadMaterial() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("ORDER_ID");// 取得要修改的工单号
			TtIfServicecarExtPO tisp = dao.queryDetailByOrderId(orderId);
			request.setAttribute("servicecarBean", tisp);
			List<TtIfServicecarExtPO> ls = dao
					.queryAuditDetailByOrderId(orderId);
			if (null != tisp.getId()) {
				List<FsFileuploadPO> fileList = dao.queryAttachFileInfo(tisp
						.getId().toString());
				act.setOutData("fileList", fileList);
			}
			List<TtIfServiceCarAuditBean> lists = dao.queryAuditDetail(orderId);
			act.setOutData("list", lists);
			request.setAttribute("auditDetails", ls);
			act.setForword(serviceCarApplyUploadURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.DETAIL_FAILURE_CODE, "服务车申请表附件签收");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title: serviceGetCarapplyDownloadMaterial
	 * @Description: TODO(车厂端附件签收)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void serviceGetCarapplyDownloadMaterial(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try{
			String ywzj = request.getParamValue("ywzj");// 取得主键id
			String roNo = request.getParamValue("ro_no");
			String type = request.getParamValue("type");
			String remark = request.getParamValue("remark");
			Integer status = Constant.SERVICE_APPLY_ACTIVE_CAR_AUDIT_GET;
			if("2".equals(type))
				status = Constant.SERVICE_APPLY_ACTIVE_CAR_AUDIT_REFUS;
			dao.updateRecord(ywzj,status,null);
			
			// TT_IF_SERVICECAR_AUDIT表增加一条明细
			TtIfServicecarAuditPO po = new TtIfServicecarAuditPO();
			po.setAuditBy(logonUser.getUserId());
			po.setAuditContent(remark);
			po.setAuditDate(new Date());
			po.setAuditStatus(status);
			po.setId(Utility.getLong(SequenceManager.getSequence("")));
			po.setOrderId(roNo);
			po.setOrgId(logonUser.getOrgId());
			dao.insert(po);
			
			serviceCarApplyDownloadInit();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.DETAIL_FAILURE_CODE, "服务车申请表附件签收");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
