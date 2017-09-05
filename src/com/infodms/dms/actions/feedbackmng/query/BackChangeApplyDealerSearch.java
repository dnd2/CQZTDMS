/**********************************************************************
* <pre>
* FILE : BackChangeApplyDealerSearch.java
* CLASS : BackChangeApplyDealerSearch
* 
* AUTHOR : WangJinBao
*
* FUNCTION : 退换车申请书查询（经销商端）Action.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-21| WangJinBao  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.feedbackmng.query;

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
import com.infodms.dms.dao.feedbackMng.BackChangeApplyDealerQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;


/**
 * Function       :  退换车申请书查询（经销商端）
 * @author        :  wangjinbao
 * CreateDate     :  2010-05-21
 * @version       :  0.1
 */
public class BackChangeApplyDealerSearch {
	private Logger logger = Logger.getLogger(BackChangeApplyDealerSearch.class);
	private final BackChangeApplyDealerQueryDao dao = BackChangeApplyDealerQueryDao.getInstance();
	
	private final String BACK_CHANGE_SEARCH_URL = "/jsp/feedbackMng/query/backChangeApplyDealerSearch.jsp";//主页面
	private final String BACK_CHANGE_APPLY_DETAIL_URL = "/jsp/feedbackMng/apply/backChangeApplylDetail.jsp";//明细页面
	
	/**
	 * 退换车申请书查询初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void backChangeApplySearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(BACK_CHANGE_SEARCH_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 退换车申请书查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void backChangeApplyQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
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
				//modify by xiayanpeng begin 从SESSION 中取DEALER_ID为String
				String delearid = logonUser.getDealerId();
				//modify by xiayanpeng end
				//退换类型
				String exType = request.getParamValue("EX_TYPE");
				//车系
				String seriesid = request.getParamValue("vehicleSeriesList");
				//工单状态
				String exStatus = request.getParamValue("EX_STATUS");
				
				//当开始时间和结束时间相同时
				if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
						beginTime = beginTime+" 00:00:00";
						endTime = endTime+" 23:59:59";
				}
				//拼sql的查询条件
				if (Utility.testString(orderId)) {
					sb.append(" and t.order_id like  ? ");
					params.add("%"+orderId+"%");
				}
				if (Utility.testString(vin)) {
					sb.append(" and t.vin like ? ");
					params.add("%"+vin+"%");
				}
				if (Utility.testString(exType)) {
					sb.append(" and t.ex_type = ? ");
					params.add(exType);
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
				//modify by xiayanpeng begin 
				if (delearid != null && !"".equals(delearid)) {
				//modify by xiayanpeng end 
					sb.append(" and t.dealer_id = ? ");
					params.add(delearid);
				}
				if (Utility.testString(exStatus)) {
					sb.append(" and t.ex_status = ? ");
					params.add(exStatus);
				}
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.backChangeApplyQuery(Constant.PAGE_SIZE, curPage,sb.toString(),params);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书查询");
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
			String orderId = request.getParamValue("ORDER_ID");//取得要显示明细的工单号
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
