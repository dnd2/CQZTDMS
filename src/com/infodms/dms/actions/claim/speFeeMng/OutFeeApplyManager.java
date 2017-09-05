 package com.infodms.dms.actions.claim.speFeeMng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CruiServiceBasicHeaderInfoBean;
import com.infodms.dms.bean.QueryCruiInfoBean;
import com.infodms.dms.bean.SpeFeeDetailInfoBean;
import com.infodms.dms.bean.SpeFeeVehicleListInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.speFeeMng.OutFeeApplyManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmVehicleExtPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 类说明：特殊费用管理--特殊外出费用申报
 * 作者：  赵伦达
 */
public class OutFeeApplyManager {
	public Logger logger = Logger.getLogger(OutFeeApplyManager.class);
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	private RequestWrapper request=null;
	private OutFeeApplyManagerDao dao=null;
	//页面导向
	private final String QUERY_OUT_FEE_APPLY_PAGE = "/jsp/claim/speFeeMng/queryOutFeeApplyPage.jsp";
	private final String ADD_OUT_FEE_APPLY_PAGE = "/jsp/claim/speFeeMng/addOutFeeApplyPage.jsp";
	private final String SHOW_VIN_URL = "/jsp/claim/speFeeMng/showVin.jsp";// VIN选择
	private final String MODIFY_OUT_FEE_APPLY_PAGE = "/jsp/claim/speFeeMng/modifyOutFeeApplyPage.jsp";
	
	/**
	 * Function：特殊外出费用申报--初始化
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
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用申报--初始化");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：特殊外出费用申报--查询功能
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-14 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void queryOutFeeCruiInfoList(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=OutFeeApplyManagerDao.getInstance();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			Map params=new HashMap();
			params.put("dealer_id",logonUserBean.getDealerId());
			params.put("company_id",companyId);
			params.put("xh_order_no",request.getParamValue("xh_order_no"));//单据编号
			params.put("report_start_date",request.getParamValue("report_start_date"));//上报开始日期
			params.put("report_end_date",request.getParamValue("report_end_date"));//上报结束日期
			params.put("xh_aim_area",request.getParamValue("xh_aim_area"));//巡航目的地
			params.put("approve_start_date",request.getParamValue("approve_start_date"));//批复开始日期
			params.put("approve_end_date",request.getParamValue("approve_end_date"));//批复结束日期
			PageResult<QueryCruiInfoBean> ps=dao.queryApprovedCruiInfo(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(QUERY_OUT_FEE_APPLY_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用申报--查询功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：特殊外出费用申报--申报功能
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-14 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void applySpeFeeOrdPage(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=OutFeeApplyManagerDao.getInstance();
			Map params=new HashMap();
			params.put("dealer_id",logonUserBean.getDealerId());
			params.put("company_id",companyId);
			params.put("user_id",logonUserBean.getUserId());
			params.put("ord_id",request.getParamValue("ord_id"));
			CruiServiceBasicHeaderInfoBean beanInfo=dao.getCruiBasicInfo(params);
			if(beanInfo==null){
				act.setOutData("retCode","data_error_001");//无法获取巡航信息
				return;
			}
			String produce_name=dao.getProducerName("CHANA");
			String code_name=dao.getCodeName(Constant.SPE_OUTFEE_CHANNEL_01);
			act.setOutData("beanInfo",beanInfo);
			act.setOutData("produce_name",produce_name);
			act.setOutData("code_name",code_name);
			act.setForword(ADD_OUT_FEE_APPLY_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用申报--申报功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title: getDetailByVinForward
	 * @Description: TODO(选择VIN带出车辆信息跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getDetailByVinForward() {
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			act.setOutData("row_id",request.getParamValue("row_id"));
			act.setForword(SHOW_VIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用申报--选择车辆信息");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}

	}
	/**
	 * 
	 * @Title: getDetailByVin
	 * @Description: TODO(根据VIN和车主姓名查询车辆信息表)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("unused")
	public void getDetailByVin() {
		act=ActionContext.getContext();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		request=act.getRequest();
		String dealerId = logonUserBean.getDealerId();
		StringBuffer con = new StringBuffer();
		Map<String,String> map = new HashMap<String,String>();
		dao=OutFeeApplyManagerDao.getInstance();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			Integer pageSize = 10;
			String vin = request.getParamValue("VIN_PARAM");
			String customer = request.getParamValue("CUSTOMER");
			/*
			 * if (dealerId != null) { con.append(" and v.DEALER_ID = " +
			 * dealerId + " "); }
			 */
			map.put("vin", vin);
			map.put("customer", customer);
			PageResult<TmVehicleExtPO> ps = dao.getVin(map,
					pageSize, curPage);
			act.setOutData("row_id",request.getParamValue("row_id"));
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用申报--选择车辆信息回显");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：特殊外出费用申报--保存功能
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-14 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void saveOutFeeApplyOrd(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=OutFeeApplyManagerDao.getInstance();
			Map params=new HashMap();
			params.put("dealer_id",logonUserBean.getDealerId());
			params.put("company_id",companyId);
			params.put("user_id",logonUserBean.getUserId());
			params.put("xh_ord_id",request.getParamValue("xh_ord_id"));//巡航单id
 			params.put("vin_str",request.getParamValue("vin_str"));//vin添加的字符串
			Map retMap=dao.modifyAndSaveOutFeeOrdInfo(params,request,Constant.SPE_OUTFEE_STATUS_01);
			String retCode=(String)retMap.get("retCode");
			if("success".equals(retCode)){
				// 附件功能
				String ywzj = (String)retMap.get("fileId");
				String[] fjids = request.getParamValues("fjid");
				FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUserBean);
			}
			act.setOutData("retCode", retCode);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用申报--保存功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：特殊外出费用申报--删除车辆信息功能
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-14 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void delVinInfo(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=OutFeeApplyManagerDao.getInstance();
			Map params=new HashMap();
			params.put("dealer_id",logonUserBean.getDealerId());
			params.put("company_id",companyId);
			params.put("user_id",logonUserBean.getUserId());
			params.put("xh_ord_id",request.getParamValue("xh_ord_id"));//巡航单id
			params.put("del_vin",request.getParamValue("del_vin"));//删除的vin
			String retCode=dao.delVinInfo(params);
			act.setOutData("retCode", retCode);
			act.setOutData("row_id", request.getParamValue("row_id"));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用申报--删除车辆信息功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：特殊外出费用申报--修改功能
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-14 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void modifySpeFeeOrdPage(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=OutFeeApplyManagerDao.getInstance();
			Map params=new HashMap();
			params.put("dealer_id",logonUserBean.getDealerId());
			params.put("company_id",companyId);
			params.put("user_id",logonUserBean.getUserId());
			params.put("ord_id",request.getParamValue("ord_id"));//巡航单id
			CruiServiceBasicHeaderInfoBean beanInfo=dao.getCruiBasicInfo(params);
			if(beanInfo==null){
				act.setOutData("retCode","data_error_001");//无法获取巡航信息
				return;
			}
			String produce_name=dao.getProducerName("CHANA");
			String code_name=dao.getCodeName(Constant.SPE_OUTFEE_CHANNEL_01);
			act.setOutData("beanInfo",beanInfo);
			act.setOutData("produce_name",produce_name);
			act.setOutData("code_name",code_name);
			//获得特殊费用详细信息
			SpeFeeDetailInfoBean detailSpeBean=dao.getSpeFeeInfoDetailBean(params);
			act.setOutData("detailSpeBean",detailSpeBean);
			//获得附件列表信息
			List<FsFileuploadPO> fileList=dao.queryAttachFileInfo(request.getParamValue("ord_id"));
			act.setOutData("fileList",fileList);
			//获得特殊费用车辆信息列表
			List<SpeFeeVehicleListInfoBean> vehicleList=dao.querySpeVehicleListInfo(params);
			act.setOutData("vehicleList",vehicleList);
			act.setOutData("ord_id", request.getParamValue("ord_id"));
			act.setForword(MODIFY_OUT_FEE_APPLY_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用申报--修改功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：特殊外出费用申报--上报功能
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-14 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void reportOrdStatus(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=OutFeeApplyManagerDao.getInstance();
			Map params=new HashMap();
			params.put("dealer_id",logonUserBean.getDealerId());
			params.put("company_id",companyId);
			params.put("user_id",logonUserBean.getUserId());
			params.put("xh_ord_id",request.getParamValue("xh_ord_id"));//巡航单id
			String retCode=dao.reportOrdOper(params);
			act.setOutData("retCode", retCode);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "特殊外出费用申报--上报功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
}
