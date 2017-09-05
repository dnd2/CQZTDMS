package com.infodms.dms.actions.feedbackmng.approve;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtIfServiceCarAuditBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.feedbackMng.ServiceCarApplyUplodaDAO;
import com.infodms.dms.dao.feedbackMng.ServiceCarInfoApproveDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtIfServicecarAuditPO;
import com.infodms.dms.po.TtIfServicecarExtPO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 服务车资料区域签收,售后服务部签收
 * @author Administrator
 *
 */
public class ServiceCarInfoApprove {
	private Logger logger = Logger.getLogger(ServiceCarInfoApprove.class);
	private ActionContext act = null ;
	private RequestWrapper req = null ;
	private AclUserBean user = null ;
	
	//区域签收初始化页面
	private final String AREA_APPROVE_URL = "/jsp/feedbackMng/approve/serviceCarInfoArea.jsp" ;
	//区域签收页面
	private final String AREA_APPROVE_EDIT_URL = "/jsp/feedbackMng/approve/serviceCarInfoAreaEdit.jsp" ;
	//售后服务部初始化页面
	private final String SERVICE_APPROVE_URL = "/jsp/feedbackMng/approve/serviceCarInfoService.jsp" ;
	//售后服务部签收页面
	private final String SERVICE_APPROVE_EDIT_URL = "/jsp/feedbackMng/approve/serviceCarInfoServiceEdit.jsp" ;
	
	private ServiceCarInfoApproveDao dao = ServiceCarInfoApproveDao.getInstance();
	/*
	 * 区域签收页面初始化
	 */
	public void areaApproveUrlInit(){
		act = ActionContext.getContext();
		user = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.setOutData("seriesList", InfoFeedBackMng
					.getVehicleSeriesByDealerId());
			act.setForword(AREA_APPROVE_URL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料区域审核");
			logger.error(user,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务部签收页面初始化
	 */
	public void serviceApproveUrlInit(){
		act = ActionContext.getContext();
		user = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.setOutData("seriesList", InfoFeedBackMng
					.getVehicleSeriesByDealerId());
			act.setForword(SERVICE_APPROVE_URL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料服务部审核");
			logger.error(user,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 区域签收主页面查询
	 */
	public void serviceCarAreaQuery(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		user = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.getResponse().setContentType("application/json");
			
			String orderId = req.getParamValue("ORDER_ID");
			String dealerCode = req.getParamValue("dealerCode");
			String dealerName = req.getParamValue("DEALER_NAME");
			String modelId = req.getParamValue("MODEL_ID");
			String beginDate = req.getParamValue("CON_APPLY_DATE_START");
			String endDate = req.getParamValue("CON_APPLY_DATE_END");
			String flag = req.getParamValue("flag");
			
			StringBuffer con = new StringBuffer();			
			if(StringUtil.notNull(orderId))
				con.append("and t.ORDER_ID like'%" + orderId + "%'\n");
			if(StringUtil.notNull(dealerCode)){
				con.append("and d.dealer_code in ('");
				con.append(dealerCode.replaceAll(",", "','"));
				con.append("')\n");
			}
			if(StringUtil.notNull(dealerName))
				con.append("and d.dealer_name like '%").append(dealerName).append("%'\n");
			if(StringUtil.notNull(modelId))
				con.append("and t.group_id=").append(modelId).append("\n");
			if(StringUtil.notNull(beginDate))
				con.append("and t.app_date >= to_date('" + beginDate
						+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
			if(StringUtil.notNull(endDate))
				con.append("and t.app_date <= to_date('" + endDate
						+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')\n");
			if("1".equals(flag))
				con.append("and t.app_status=").append(Constant.SERVICE_APPLY_ACTIVE_CAR_MATERIAL_UPLOAD).append("\n");
			else if("2".equals(flag))
				con.append("and t.app_status=").append(Constant.SERVICE_APPLY_ACTIVE_CAR_MATERIAL_UPLOAD_GET).append("\n");
			con.append("order by t.order_id\n");
			
			int pageSize = 15 ;
			Integer curPage = req.getParamValue("curPage") != null ? Integer
				.parseInt(req.getParamValue("curPage"))
				: 1; // 处理当前页
			
			PageResult<TtIfServicecarExtPO> ps = dao.getServiceCarInfo(con.toString(), pageSize, curPage);
			
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料服务部审核");
			logger.error(user,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 签收按钮
	 */
	public void queryDetailForApprove(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		user = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String url = AREA_APPROVE_EDIT_URL ;
			
			String orderId = req.getParamValue("ORDER_ID");
			String flag = req.getParamValue("flag");
			Integer status = Constant.SERVICE_APPLY_ACTIVE_CAR_MATERIAL_UPLOAD;
			if("2".equals(flag)){
				url = SERVICE_APPROVE_EDIT_URL;
				status = Constant.SERVICE_APPLY_ACTIVE_CAR_MATERIAL_UPLOAD_GET;
			}
			ServiceCarApplyUplodaDAO dao2 = ServiceCarApplyUplodaDAO.getInstance();
			TtIfServicecarExtPO tisp = dao2.queryDetailByOrderId(orderId);
			req.setAttribute("servicecarBean", tisp);
			List<TtIfServicecarExtPO> ls = dao2
					.queryAuditDetailByOrderId(orderId);
			if (null != tisp.getId()) {
				List<FsFileuploadPO> fileList = dao2.queryAttachFileInfo(tisp
						.getId().toString());
				act.setOutData("fileList", fileList);
			}
			req.setAttribute("auditDetails", ls);
			List<TtIfServiceCarAuditBean> lists = dao2.queryAuditDetail(orderId);
			act.setOutData("list", lists);
			
			act.setForword(url);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料服务部审核");
			logger.error(user,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 签收页面处理
	 */
	public void serviceGetCarapplyDownloadMaterial(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try{
			String ywzj = request.getParamValue("ywzj");// 取得主键id
			String orderId = request.getParamValue("ro_no");
			String type = request.getParamValue("type");
			String remark = request.getParamValue("remark");
			int flag = 1;
			Integer status = Constant.SERVICE_APPLY_ACTIVE_CAR_MATERIAL_UPLOAD_GET;
			if("2".equals(type))
				status = Constant.SERVICE_APPLY_ACTIVE_CAR_AREA_REFUS;
			if("3".equals(type)){
				flag = 2;
				status = Constant.SERVICE_APPLY_ACTIVE_CAR_SERVICE_GET;
			}
			if("4".equals(type)){
				flag = 2 ;
				status = Constant.SERVICE_APPLY_ACTIVE_CAR_SERVICE_REFUS;
			}
			ServiceCarApplyUplodaDAO dao2 = ServiceCarApplyUplodaDAO.getInstance();
			dao2.updateRecord(ywzj,status,null);
			
			// TT_IF_SERVICECAR_AUDIT表增加一条明细
			TtIfServicecarAuditPO po = new TtIfServicecarAuditPO();
			po.setAuditBy(logonUser.getUserId());
			po.setAuditContent(remark);
			po.setAuditDate(new Date());
			po.setAuditStatus(status);
			po.setId(Utility.getLong(SequenceManager.getSequence("")));
			po.setOrderId(orderId);
			po.setOrgId(logonUser.getOrgId());
			dao.insert(po);
			
			if(flag==1)
				this.areaApproveUrlInit();
			else if(flag==2)
				this.serviceApproveUrlInit();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.DETAIL_FAILURE_CODE, "服务车申请表附件签收");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
