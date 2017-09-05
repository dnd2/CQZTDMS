package com.infodms.dms.actions.claim.speFeeMng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CruiServiceDetailInfoBean;
import com.infodms.dms.bean.QueryCruiInfoBean;
import com.infodms.dms.bean.SpeFeeApproveLogListBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.speFeeMng.CruiServicePathQueryManagerDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 类说明：特殊费用管理--巡航服务线路查询(经销商)
 * 作者：  赵伦达
 */
public class CruiServicePathQueryManager {
	public Logger logger = Logger.getLogger(CruiServicePathQueryManager.class);
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	private RequestWrapper request=null;
	private CruiServicePathQueryManagerDao queryDao=null;
	//页面导向
	private final String QUERY_CRUI_SERVICE_PAGE = "/jsp/claim/speFeeMng/queryCruiSerInfoPage.jsp";
	private final String QUERY_CRUI_SERVICE_DETAIL_PAGE = "/jsp/claim/speFeeMng/queryCruiSerDetailInfoPage.jsp";
	
	/**
	 * Function：巡航服务线路查询--初始化
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	public void queryListPage(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(QUERY_CRUI_SERVICE_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "巡航服务线路查询--初始化");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：巡航服务线路查询--查询功能
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	@SuppressWarnings("unchecked")
	public void queryCruiInfoOrdList(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			request=act.getRequest();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			queryDao=CruiServicePathQueryManagerDao.getInstance();
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
			params.put("ord_status",request.getParamValue("ord_status"));//单据状态
			PageResult<QueryCruiInfoBean> ps=queryDao.queryCruiInfo(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(QUERY_CRUI_SERVICE_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "巡航服务线路申请--查询功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：巡航服务线路查询--明细功能
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
			queryDao=CruiServicePathQueryManagerDao.getInstance();
			Map params=new HashMap();
			params.put("ord_id",request.getParamValue("ord_id"));
			CruiServiceDetailInfoBean detailBean=queryDao.getCruiDetailInfo(params);
			if(detailBean==null){
				act.setOutData("retCode", "data_error");
			}else{
				act.setOutData("retCode", "data_success");
				act.setOutData("detailBean", detailBean);
			}
			//获得审核日志
			List<SpeFeeApproveLogListBean> logList=queryDao.getApproveLogList(request.getParamValue("ord_id"));
			act.setOutData("logList", logList);
			act.setForword(QUERY_CRUI_SERVICE_DETAIL_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "巡航服务线路查询--明细功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
}
