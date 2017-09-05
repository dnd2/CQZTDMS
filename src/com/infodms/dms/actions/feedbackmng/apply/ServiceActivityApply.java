package com.infodms.dms.actions.feedbackmng.apply;

import java.util.Date;
import java.util.List;
import java.util.Map; 
import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.ServiceActivityApplyDAO;
import com.infodms.dms.dao.feedbackMng.ServiceInfoApplyDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtIfWrActivityExtPO;
import com.infodms.dms.po.TtIfWrActivityPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName: ServiceActivityApply
 * @Description: TODO(服务活动申请表申请)
 * @author wangchao
 * @date May 26, 2010 9:15:10 PM
 * 
 */
public class ServiceActivityApply {

	public Logger logger = Logger.getLogger(ServiceActivityApply.class);
	private final String serviceActivityApplyURL = "/jsp/feedbackMng/apply/serviceActivityApply.jsp";// 主页面
	private final String newServiceActivityApplyURL = "/jsp/feedbackMng/apply/newServiceActivityApply.jsp";// 新增页面
	private final String modifyServiceActivityApplyURL = "/jsp/feedbackMng/apply/modifyServiceActivityApply.jsp";// 修改页面
	private final String detailServiceActivityApplyURL = "/jsp/feedbackMng/apply/serviceActivityApplyDetail.jsp";
	private ServiceActivityApplyDAO dao = ServiceActivityApplyDAO.getInstance();

	/**
	 * 
	 * @Title: serviceactivityapplyforward
	 * @Description: TODO(跳转到服务活动申请表首页)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void serviceactivityapplyforward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.setOutData("seriesList", InfoFeedBackMng
					.getVehicleSeriesByDealerId());
			act.setForword(serviceActivityApplyURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "服务活动申请表首页");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: newServiceActivityApplyforward
	 * @Description: TODO(跳转到新增页面)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void newServiceActivityApplyforward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealerId = String.valueOf(logonUser.getDealerId());
			RequestWrapper request = act.getRequest();
			act.setOutData("seriesList", InfoFeedBackMng
					.getVehicleSeriesByDealerId());
			ServiceInfoApplyDao infoDao = new ServiceInfoApplyDao();
			Map<String, Object> map = infoDao.getDealerInfo(dealerId);
			act.setOutData("map", map);
			act.setForword(newServiceActivityApplyURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "服务活动申请表新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: serviceactivityapplyQuery
	 * @Description: TODO(经销商端查询)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void serviceactivityapplyQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			StringBuffer con = new StringBuffer();
			String queryType = request.getParamValue("queryType");
			String actName = request.getParamValue("ACT_NAME");
			String actType = request.getParamValue("ACT_TYPE");
			String orderId = request.getParamValue("ORDER_ID");
			String strDate = request.getParamValue("CON_APPLY_DATE_START");
			String endDate = request.getParamValue("CON_APPLY_DATE_END");
			// 如果是提报页面的查询queryType=submit则只查询待上报
			if (null != queryType && !"".equals(queryType)
					&& ("submit").equals(queryType)) {
				con.append(" and (t.act_status='"
						+ Constant.SERVICE_APPLY_ACTIVE_STATUS_UNREPORT + "' ");
				con.append(" or t.act_status='"
						+ Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT
						+ "' ");
				con.append(" or t.act_status='"
						+ Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT
						+ "' ");
				con.append(" or t.act_status='"
						+ Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT
						+ "') ");
			}
			// 工单号like
			if (orderId != null && !"".equals(orderId)) {
				con.append(" and ORDER_ID like'%" + orderId + "%' "); //
			}
			// 介于开始时间
			if (strDate != null && !"".equals(strDate)) {
				con.append(" and t.CREATE_DATE >= to_date('" + strDate
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 结束时间
			if (endDate != null && !"".equals(endDate)) {
				con.append(" and t.CREATE_DATE <= to_date('" + endDate
						+ " 23:59:59" + "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 经销商代码
			if (dealerId != null && !"".equals(dealerId)) {
				con.append(" and t.DEALER_ID='" + dealerId + "' ");
			}
			// 活动类型
			if (actType != null && !"".equals(actType)
					&& !("-1").equals(actType)) {
				con.append(" and t.ACT_TYPE='" + actType + "' ");
			}

			if (actName != null && !"".equals(actName)
					&& !("-1").equals(actName)) {
				con.append(" and t.ACT_NAME like '%" + actName + "%' ");
			}
			PageResult<TtIfWrActivityExtPO> list = dao.applyQuery(con
					.toString(), null, curPage, Constant.PAGE_SIZE);
			// List ls = list.getRecords();
			act.setOutData("ps", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动申请表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: serviceactivityapplyAdd
	 * @Description: TODO(新增)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void serviceactivityapplyAdd() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		Long userId = logonUser.getUserId();
		String dealerId = logonUser.getDealerId();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页

			String orderId = request.getParamValue("ORDER_ID");
			String linkMan = request.getParamValue("LINK_MAN");// 联系人姓名
			String tel = request.getParamValue("TEL");// 单位电话
			Integer actType = new Integer(request.getParamValue("ACT_TYPE"));// 活动类型
			String fax = request.getParamValue("FAX");// 传真
			String content = request.getParamValue("CONTENT");// 申请内容
			String actName = request.getParamValue("ACT_NAME"); // 活动名称
			Double actMoney = request.getParamValue("ACT_MONEY") == null ? null
					: Double.parseDouble(request.getParamValue("ACT_MONEY"));
			TtIfWrActivityPO tisp = new TtIfWrActivityPO();
			tisp.setOrderId(SequenceManager.getSequence("FWAO"));
			tisp.setActType(actType);
			tisp.setActName(actName);
			tisp.setActMoney(actMoney);
			tisp.setLinkMan(linkMan); // 单位联系人
			tisp.setTel(tel); // 单位电话
			tisp.setFax(fax); // 单位传真
			tisp.setActContent(content); // 申请内容
			tisp.setActMoney(actMoney); // 金额
			tisp.setCreateDate(new Date()); // 创建日期
			tisp.setCreateBy(userId); // 创建人
			tisp.setCompanyId(companyId);
			tisp.setActStatus(Constant.SERVICE_APPLY_ACTIVE_STATUS_UNREPORT); // 申请状态为待提交
			tisp.setDealerId(Utility.getLong(dealerId));
			// modify by xiayanpeng begin 新增时，插入ID_DEL=0
			tisp.setIsDel(new Integer(Constant.IS_DEL_00));
			// modify by xiayanpeng end
			String ywzj = dao.addRecord(tisp);
			//附近功能：
			String[] fjids = request.getParamValues("fjid");
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);	
			serviceactivityapplyforward();
			// act.setForword(servicecarapplyforward.do");

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ADD_FAILURE_CODE, "服务活动申请表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: serviceactivityapplySubmit
	 * @Description: TODO(上报)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void serviceactivityapplySubmit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderIds = request.getParamValue("orderIds");// 要上报的工单号，以,隔开
			if (orderIds != null && !"".equals(orderIds)) {
				String[] orderIdArray = orderIds.split(","); // 取得所有orderId放在数组中
				dao.submit(orderIdArray, logonUser);
			}
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "服务活动申请表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: serviceactivityapplyDelete
	 * @Description: TODO(删除单子)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void serviceactivityapplyDelete() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderIds = request.getParamValue("orderIds");// 要上报的工单号，以,隔开
			if (orderIds != null && !"".equals(orderIds)) {
				String[] orderIdArray = orderIds.split(","); // 取得所有orderId放在数组中
				dao.deleteRecord(orderIdArray);
			}
			act.setOutData("returnValue", 1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "服务活动申请表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: serviceactivityapplyUpdatePre
	 * @Description: TODO(修改申请表前置操作)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void serviceactivityapplyUpdatePre() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("ORDER_ID");// 取得要修改的工单号
			TtIfWrActivityPO tisp = dao.queryByOrderId(orderId);
			List<FsFileuploadPO> fileList=dao.queryAttachFileInfo(String.valueOf(tisp.getId()));
			act.setOutData("fileList", fileList);
			request.setAttribute("servicecarBean", tisp);
			act.setForword(modifyServiceActivityApplyURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "服务活动申请表修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: serviceactivityapplyUpdate
	 * @Description: TODO(服务活动申请单修改)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void serviceactivityapplyUpdate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long userId = logonUser.getUserId();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String orderId = request.getParamValue("ORDER_ID");
			String id = request.getParamValue("id");
			String linkMan = request.getParamValue("LINK_MAN");// 联系人姓名
			String tel = request.getParamValue("TEL");// 单位电话
			Integer actType = new Integer(request.getParamValue("ACT_TYPE"));// 活动类型
			String fax = request.getParamValue("FAX");// 传真
			String content = request.getParamValue("CONTENT");// 申请内容
			String actName = request.getParamValue("ACT_NAME"); // 活动名称
			Double actMoney = request.getParamValue("ACT_MONEY") == null ? null
					: Double.parseDouble(request.getParamValue("ACT_MONEY"));
			TtIfWrActivityPO tisp = new TtIfWrActivityPO();
			//tisp.setOrderId(SequenceManager.getSequence("FWAO"));
			tisp.setActType(actType);
			tisp.setActName(actName);
			tisp.setActMoney(actMoney);
			tisp.setLinkMan(linkMan); // 单位联系人
			tisp.setTel(tel); // 单位电话
			tisp.setFax(fax); // 单位传真
			tisp.setActContent(content); // 申请内容
			tisp.setActMoney(actMoney); // 金额
			tisp.setUpdateDate(new Date()); // 创建日期
			tisp.setUpdateBy(userId); // 创建人
			tisp.setActStatus(Constant.SERVICE_APPLY_ACTIVE_STATUS_UNREPORT); // 申请状态为待提交
			dao.updateRecord(orderId, tisp);
			request.setAttribute("orderId", orderId);
			//附近功能：
			String ywzj = id;
			String[] fjids = request.getParamValues("fjid");
			//String[] uploadFjid = request.getParamValues("uploadFjid");
			FileUploadManager.delAllFilesUploadByBusiness(ywzj,fjids);//删除附件
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);	//新添加附件
			serviceactivityapplyforward();
			// tisp.setStatus(status);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "服务活动申请表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: serviceactivityapplyDetail
	 * @Description: TODO(通过点击工单号，查询申请表明细)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void serviceactivityapplyDetail() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("ORDER_ID");// 取得要修改的工单号
			TtIfWrActivityExtPO tisp = dao.queryDetailByOrderId(orderId);
			request.setAttribute("servicecarBean", tisp);
			List<TtIfWrActivityExtPO> ls = dao
					.queryAuditDetailByOrderId(orderId);
			List<FsFileuploadPO> fileList=dao.queryAttachFileInfo(String.valueOf(tisp.getId()));
			act.setOutData("fileList", fileList);
			request.setAttribute("auditDetails", ls);
			act.setForword(detailServiceActivityApplyURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.DETAIL_FAILURE_CODE, "服务活动申请表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

}
