package com.infodms.dms.actions.claim.speFeeMng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.SpeFeeApproveLogListBean;
import com.infodms.dms.bean.SpeFeeDetailInfoBean;
import com.infodms.dms.bean.SpeFeeQueryListBean;
import com.infodms.dms.bean.SpeFeeVehicleListInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.speFeeMng.OutFeeApplyManagerDao;
import com.infodms.dms.dao.claim.speFeeMng.OutFeeQueryManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class OutFeeQueryManager {
	public Logger logger = Logger.getLogger(OutFeeQueryManager.class);
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	private RequestWrapper request=null;
	private OutFeeQueryManagerDao dao=null;
	private OutFeeApplyManagerDao daoApply=null;
	//页面导向
	private final String QUERY_OUT_FEE_APPLY_PAGE = "/jsp/claim/speFeeMng/queryOutFeeQueryPage.jsp";
	private final String QUERY_OUT_FEE_DETAIL_PAGE = "/jsp/claim/speFeeMng/queryOutFeeDetailPage.jsp";
	
	/**
	 * Function：特殊外出费用查询--初始化
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	public void queryListPage(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(QUERY_OUT_FEE_APPLY_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用查询--初始化");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：特殊外出费用查询--查询功能
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-14 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void queryOutFeeInfoOrdList(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=OutFeeQueryManagerDao.getInstance();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			Map params=new HashMap();
			params.put("dealer_id",logonUserBean.getDealerId());
			params.put("company_id",companyId);
			params.put("ts_order_no",request.getParamValue("ts_order_no"));//特殊外出单据编号
			params.put("xh_order_no",request.getParamValue("xh_order_no"));//巡航单据编号
			params.put("report_start_date",request.getParamValue("report_start_date"));//上报开始日期
			params.put("report_end_date",request.getParamValue("report_end_date"));//上报结束日期
			params.put("ord_status",request.getParamValue("ord_status"));//外出单据状态
			PageResult<SpeFeeQueryListBean> ps=dao.getSpeFeeQueryListInfo(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(QUERY_OUT_FEE_APPLY_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用查询--查询功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：特殊外出费用查询--明细功能
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void queryOrdDetailInfoPage(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			dao=OutFeeQueryManagerDao.getInstance();
			Map params=new HashMap();
			params.put("ord_id",request.getParamValue("ord_id"));
			daoApply=OutFeeApplyManagerDao.getInstance();
			//获得特殊费用详细信息
			SpeFeeDetailInfoBean detailSpeBean=daoApply.getSpeFeeInfoDetailBean(params);
			act.setOutData("detailSpeBean",detailSpeBean);
			//获得附件列表信息
			List<FsFileuploadPO> fileList=daoApply.queryAttachFileInfo(request.getParamValue("ord_id"));
			act.setOutData("fileList",fileList);
			//获得特殊费用车辆信息列表
			List<SpeFeeVehicleListInfoBean> vehicleList=daoApply.querySpeVehicleListInfo(params);
			act.setOutData("vehicleList",vehicleList);
			//获得审核日志
			List<SpeFeeApproveLogListBean> logList=dao.getApproveLogList(request.getParamValue("ord_id"));
			act.setOutData("logList", logList);
			act.setForword(QUERY_OUT_FEE_DETAIL_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用查询--明细功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
}
