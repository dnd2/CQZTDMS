package com.infodms.dms.actions.claim.speFeeMng;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CruiServiceDealerInfoBean;
import com.infodms.dms.bean.CruiServiceDetailInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.speFeeMng.CruiServicePathApplyManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrCruisePO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 类说明：特殊费用管理--巡航服务线路申请
 * 作者：  赵伦达
 */
public class CruiServicePathApplyManager {
	public Logger logger = Logger.getLogger(CruiServicePathApplyManager.class);
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	private RequestWrapper request=null;
	private CruiServicePathApplyManagerDao crui_dao=null;
	
	//页面导向
	private final String QUERY_CRUI_SERVICE_APPLY_PAGE = "/jsp/claim/speFeeMng/queryCruiSerApplyPage.jsp";
	private final String ADD_CRUI_SERVICE_APPLY_PAGE = "/jsp/claim/speFeeMng/addCruiSerApplyPage.jsp";
	private final String MODIFY_CRUI_SERVICE_APPLY_PAGE = "/jsp/claim/speFeeMng/modifyCruiSerApplyPage.jsp";
	/**
	 * Function：巡航服务线路申请--初始化
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	public void queryListPage(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(QUERY_CRUI_SERVICE_APPLY_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "巡航服务线路申请--初始化");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：巡航服务线路申请--新增
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void addCruiServiceOrd(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			crui_dao=CruiServicePathApplyManagerDao.getInstance();
			Map params=new HashMap();
			params.put("dealer_id",logonUserBean.getDealerId());
			CruiServiceDealerInfoBean dealerBean=crui_dao.getDealerInfo(params);
			act.setOutData("deal_code", dealerBean==null?"":dealerBean.getDealer_code());
			act.setOutData("deal_name", dealerBean==null?"":dealerBean.getDealer_name());
			act.setOutData("privince_name", dealerBean==null?"":dealerBean.getPrivince_name());
			
			act.setForword(ADD_CRUI_SERVICE_APPLY_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "巡航服务线路申请--新增");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：巡航服务线路申请--查询功能
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void queryUnreportedOrdList(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			crui_dao=CruiServicePathApplyManagerDao.getInstance();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			Map params=new HashMap();
			params.put("dealer_id",logonUserBean.getDealerId());
			params.put("company_id",companyId);
			params.put("xh_order_no",request.getParamValue("xh_order_no"));
			params.put("create_start_date",request.getParamValue("create_start_date"));
			params.put("create_end_date",request.getParamValue("create_end_date"));
			params.put("xh_aim_area",request.getParamValue("xh_aim_area"));
			PageResult<TtAsWrCruisePO> ps=crui_dao.getUnReportCruiInfo(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(QUERY_CRUI_SERVICE_APPLY_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "巡航服务线路申请--查询功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：巡航服务线路申请--保存信息
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void saveNewCuriServiceInfo(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			request=act.getRequest();
			crui_dao=CruiServicePathApplyManagerDao.getInstance();
			Map params=new HashMap();
			params.put("user_id",logonUserBean.getUserId());
			params.put("dealer_id",logonUserBean.getDealerId());
			params.put("company_id",companyId);
			params.put("crui_aim",request.getParamValue("crui_aim"));//巡航目的地
			params.put("crui_km",request.getParamValue("crui_km"));//巡航公里数
			params.put("crui_days",request.getParamValue("crui_days"));//巡航天数
			params.put("crui_man",request.getParamValue("crui_man"));//巡航负责人
			params.put("crui_phone",request.getParamValue("crui_phone"));//巡航服务电话
			params.put("crui_reason",request.getParamValue("crui_reason"));//巡航服务原因
			String retCode=crui_dao.saveCruiServiceInfo(params);
			act.setOutData("retCode", retCode);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "巡航服务线路申请--保存信息");
			logger.error(logonUserBean, e1);
			act.setException(e1);
			act.setOutData("retCode","save_failure");
		}
	}
	/**
	 * Function：巡航服务线路申请--删除
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void delOrder(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			crui_dao=CruiServicePathApplyManagerDao.getInstance();
			Map params=new HashMap();
			params.put("user_id",logonUserBean.getUserId());
			params.put("orderIds",request.getParamValue("orderIds"));
			String retCode=crui_dao.delCruiInfo(params);
			act.setOutData("retCode", retCode);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "巡航服务线路申请--删除");
			logger.error(logonUserBean, e1);
			act.setException(e1);
			act.setOutData("retCode","save_failure");
		}
	}
	/**
	 * Function：巡航服务线路申请--修改
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void modifyOrdPage(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			crui_dao=CruiServicePathApplyManagerDao.getInstance();
			Map params=new HashMap();
			params.put("ord_id",request.getParamValue("ord_id"));
			CruiServiceDetailInfoBean detailBean=crui_dao.getCruiModifyInfo(params);
			if(detailBean==null){
				act.setOutData("retCode", "data_error");
			}else{
				act.setOutData("retCode", "data_success");
				act.setOutData("detailBean", detailBean);
			}
			act.setForword(MODIFY_CRUI_SERVICE_APPLY_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "巡航服务线路申请--修改");
			logger.error(logonUserBean, e1);
			act.setException(e1);
			act.setOutData("retCode","save_failure");
		}
	}
	/**
	 * Function：巡航服务线路申请--修改保存
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void modifyAndSaveCuriServiceInfo(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			crui_dao=CruiServicePathApplyManagerDao.getInstance();
			Map params=new HashMap();
			params.put("ord_id",request.getParamValue("ord_id"));
			params.put("user_id",logonUserBean.getUserId());
			params.put("crui_aim",request.getParamValue("crui_aim"));//巡航目的地
			params.put("crui_km",request.getParamValue("crui_km"));//巡航公里数
			params.put("crui_days",request.getParamValue("crui_days"));//巡航天数
			params.put("crui_man",request.getParamValue("crui_man"));//巡航负责人
			params.put("crui_phone",request.getParamValue("crui_phone"));//巡航服务电话
			params.put("crui_reason",request.getParamValue("crui_reason"));//巡航服务原因
			String retCode=crui_dao.modifyAndSaveCruiDetailInfo(params);
			act.setOutData("retCode", retCode);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "巡航服务线路申请--修改保存");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：巡航服务线路申请--上报
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void reportOrder(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			crui_dao=CruiServicePathApplyManagerDao.getInstance();
			Map params=new HashMap();
			params.put("user_id",logonUserBean.getUserId());
			params.put("orderIds",request.getParamValue("orderIds"));
			String retCode=crui_dao.reportCruiInfo(params);
			act.setOutData("retCode", retCode);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "巡航服务线路申请--上报");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
}
