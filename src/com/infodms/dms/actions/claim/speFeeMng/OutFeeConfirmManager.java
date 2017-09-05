package com.infodms.dms.actions.claim.speFeeMng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.SpeFeeApproveDetailInfoBean;
import com.infodms.dms.bean.SpeFeeApproveLogListBean;
import com.infodms.dms.bean.SpeFeeConfirmInfoListBean;
import com.infodms.dms.bean.SpeFeeVehicleListInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.speFeeMng.OutFeeApplyManagerDao;
import com.infodms.dms.dao.claim.speFeeMng.OutFeeConfirmManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 类说明：特殊费用管理--特殊外出费用确认单
 * 作者：  赵伦达
 */
public class OutFeeConfirmManager {
	public Logger logger = Logger.getLogger(OutFeeConfirmManager.class);
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	private RequestWrapper request=null;
	private OutFeeConfirmManagerDao dao=null;
	private OutFeeApplyManagerDao daoApply=null;
	//页面导向
	private final String QUERY_OUT_FEE_APPLY_PAGE = "/jsp/claim/speFeeMng/queryOutFeeConfirmPage.jsp";
	private final String APPROVE_OUT_FEE_PAGE = "/jsp/claim/speFeeMng/approveOutFeeConfirmPage.jsp";
	/**
	 * Function：特殊外出费用确认单--初始化
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-18 赵伦达
	 */
	public void queryListPage(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(QUERY_OUT_FEE_APPLY_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用确认单--初始化");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：特殊外出费用确认单--查询功能
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-19 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void queryOutFeeConfirmInfoList(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=OutFeeConfirmManagerDao.getInstance();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			Map params=new HashMap();
			params.put("dealer_id",logonUserBean.getDealerId());
			params.put("company_id",companyId);
			params.put("ts_order_no",request.getParamValue("ts_order_no"));//特殊单据编号
			params.put("xh_order_no",request.getParamValue("xh_order_no"));//巡航单据编号
			params.put("dealerCode",request.getParamValue("dealerCode"));//经销商代码
			params.put("dealerName",request.getParamValue("dealerName"));//经销商名称
			params.put("report_start_date",request.getParamValue("report_start_date"));//上报开始日期
			params.put("report_end_date",request.getParamValue("report_end_date"));//上报结束日期
			PageResult<SpeFeeConfirmInfoListBean> ps=dao.querySpeFeeConfirmInfo(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(QUERY_OUT_FEE_APPLY_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用确认单--查询功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：特殊外出费用确认单--审核页面
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-19 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void approveSpeFeePage(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=OutFeeConfirmManagerDao.getInstance();
			daoApply=OutFeeApplyManagerDao.getInstance();
			Map params=new HashMap();
			params.put("company_id",companyId);
			params.put("order_id",request.getParamValue("order_id"));//特殊单据编号
			String produce_name=daoApply.getProducerName("CHANA");
			String code_name=daoApply.getCodeName(Constant.SPE_OUTFEE_CHANNEL_01);
			act.setOutData("produce_name",produce_name);
			act.setOutData("code_name",code_name);
			//获得特殊费用基本信息
			SpeFeeApproveDetailInfoBean detailBean=dao.getSpeFeeApproveDetailInfo(params);
			act.setOutData("detailBean", detailBean);
			//获得附件列表
			List<FsFileuploadPO> fileList=dao.queryAttachFileInfo(request.getParamValue("order_id"));
			act.setOutData("fileList", fileList);
			//获得车辆信息
			List<SpeFeeVehicleListInfoBean> vehicleList=dao.querySpeVehicleListInfo(params);
			act.setOutData("vehicleList", vehicleList);
			//获得审核日志
			List<SpeFeeApproveLogListBean> logList=dao.getApproveLogList(request.getParamValue("order_id"));
			act.setOutData("fjids",request.getParamValue("order_id"));
			act.setOutData("logList", logList);
			act.setForword(APPROVE_OUT_FEE_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用确认单--审核页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：特殊外出费用确认单--通过操作
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-20 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void passSpeOper(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=OutFeeConfirmManagerDao.getInstance();
			Map params=new HashMap();
			params.put("user_id",logonUserBean.getUserId());
			params.put("company_id",companyId);
			params.put("org_id",logonUserBean.getOrgId());
			params.put("order_id",request.getParamValue("order_id"));//特殊单据编号
			params.put("approve_status", Constant.SPE_OUTFEE_STATUS_04);//审核通过状态
			params.put("approve_content",request.getParamValue("approve_content"));//审核内容
			String retCode=dao.approveSpeFeeDataOper(params);
			act.setOutData("retCode",retCode);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用确认单--通过操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：特殊外出费用确认单--驳回操作
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-20 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void rejectSpeOper(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=OutFeeConfirmManagerDao.getInstance();
			Map params=new HashMap();
			params.put("user_id",logonUserBean.getUserId());
			params.put("company_id",companyId);
			params.put("org_id",logonUserBean.getOrgId());
			params.put("order_id",request.getParamValue("order_id"));//特殊单据编号
			params.put("approve_status", Constant.SPE_OUTFEE_STATUS_03);//审核驳回状态
			params.put("approve_content",request.getParamValue("approve_content"));//审核内容
			String retCode=dao.approveSpeFeeDataOper(params);
			act.setOutData("retCode",retCode);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用确认单--驳回操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
}
