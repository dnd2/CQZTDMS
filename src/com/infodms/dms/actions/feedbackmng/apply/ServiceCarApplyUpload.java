package com.infodms.dms.actions.feedbackmng.apply;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.feedbackMng.ServiceCarApplyUplodaDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtIfServicecarAuditPO;
import com.infodms.dms.po.TtIfServicecarExtPO;
import com.infodms.dms.po.TtIfServicecarPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * <p>ServiceCarApplyUpload.java</p>
 *
 * <p>Description: 服务车申请表资料上传</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-07-27</p>
 *
 * @author subo
 * @version 1.0
 * @remark
 */
public class ServiceCarApplyUpload {
	public Logger logger = Logger.getLogger(StandardVipApplyManager.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String serviceCarApplyUploadURL = "/jsp/feedbackMng/apply/serviceCarApplyUpload.jsp";//主页面url
	private final String ServiceCarApplyUploadMaterialURL = "/jsp/feedbackMng/apply/serviceCarApplyUploadMaterial.jsp";//上传页面
	private final String ServiceCarApplyUploadDetailURL = "/jsp/feedbackMng/apply/serviceCarApplyMaterialDetail.jsp";//明细页面
	private ServiceCarApplyUplodaDAO dao = ServiceCarApplyUplodaDAO.getInstance();
	/**
	 * 
	* @Title: servicecarapplyforward 
	* @Description: TODO(跳转到服务车表资料上传首页) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void serviceInfoApplyUploadInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(serviceCarApplyUploadURL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表资料上传");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: servicecarapplyQuery 
	* @Description: TODO(经销商端查询) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void servicecarapplyUploadQuery() {
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
			String orderId = request.getParamValue("ORDER_ID");
			String strDate = request.getParamValue("CON_APPLY_DATE_START");
			String endDate = request.getParamValue("CON_APPLY_DATE_END");
			String modelId = request.getParamValue("MODEL_ID");
			// 工单号like
			if (orderId != null && !"".equals(orderId)) {
				con.append(" and ORDER_ID like'%" + orderId + "%' "); //
			}
			// 介于开始时间
			if (strDate != null && !"".equals(strDate)) {
				con.append(" and t.create_date >= to_date('" + strDate +" 00:00:00"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 结束时间
			if (endDate != null && !"".equals(endDate)) {
				con.append(" and t.create_date <= to_date('" + endDate + " 23:59:59"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 经销商代码
			if (dealerId != null && !"".equals(dealerId)) {
				con.append(" and t.DEALER_ID='" + dealerId + "' ");
			}
			// 车型
			if (modelId != null && !"".equals(modelId)) {
				con.append(" and T.GROUP_ID='" + modelId + "' ");
			}
			PageResult<TtIfServicecarExtPO> list = dao.applyUploadQuery(con.toString(),null,curPage,Constant.PAGE_SIZE);
			//List ls = list.getRecords();
			act.setOutData("ps", list);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务车申请表资料上传");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: servicecarapplyUploadMaterialDetail 
	* @Description: TODO(经销商端资料明细查看) 
	* @param    设定文件
	* @return void    返回类型 
	* @throws
	 */
	public void servicecarapplyUploadMaterialDetail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("ORDER_ID");//取得要修改的工单号
			String type = request.getParamValue("type");//判断是查看明细还是执行上传
			TtIfServicecarExtPO tisp = dao.queryDetailByOrderId(orderId);
			request.setAttribute("servicecarBean", tisp);
			List<TtIfServicecarExtPO> ls = dao.queryAuditDetailByOrderId(orderId);
			request.setAttribute("auditDetails", ls);
			FsFileuploadPO file = new FsFileuploadPO();
			file.setYwzj(tisp.getId());
			List<FsFileuploadPO> files = dao.select(file);
			act.setOutData("fileList",files);
			if("detail".equals(type)){
				act.setForword(ServiceCarApplyUploadDetailURL);
			}else{
				act.setForword(ServiceCarApplyUploadMaterialURL);
			}
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"服务车申请表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: servicecarapplyUploadMaterial 
	* @Description: TODO(经销商端资料上传) 
	* @param    设定文件
	* @return void    返回类型 
	* @throws
	 */
	public void servicecarapplyUploadMaterial(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		//附近功能：
		String ywzj = request.getParamValue("ywzj");
		String roNo = request.getParamValue("roNo");
		String[] fjids = request.getParamValues("fjid");
		String remark = request.getParamValue("remark");
		try {
			if(null!=fjids&&!"".equals(fjids)){
				FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
				TtIfServicecarPO tisp = new TtIfServicecarPO();
				tisp.setRemark(remark);
				dao.updateRecord(ywzj,Constant.SERVICE_APPLY_ACTIVE_CAR_MATERIAL_UPLOAD,remark);
			
				// 向tt_if_servicecar_audit表里添加数据
				TtIfServicecarAuditPO apo = new TtIfServicecarAuditPO();
				apo.setAuditBy(logonUser.getUserId());
				apo.setAuditDate(new Date());
				apo.setAuditContent(remark);
				apo.setAuditStatus(Constant.SERVICE_APPLY_ACTIVE_CAR_MATERIAL_UPLOAD);
				apo.setId(Utility.getLong(SequenceManager.getSequence("")));
				apo.setOrderId(roNo);
				apo.setOrgId(logonUser.getOrgId());
				dao.insert(apo);
			}
			serviceInfoApplyUploadInit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务车申请表资料上传");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
}
