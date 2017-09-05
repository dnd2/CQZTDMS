package com.infodms.dms.actions.feedbackmng.query;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtIfMarketAuditListBean;
import com.infodms.dms.bean.TtIfMarketBean;
import com.infodms.dms.bean.TtIfMarketDetailBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.MarketQuesOrderDao;
import com.infodms.dms.dao.orgmng.GetOrgIdsOrDealerIdsDAO;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class MarketQuestionOrderQueryCarManager {
	public Logger logger = Logger.getLogger(MarketQuestionOrderQueryCarManager.class);
	private MarketQuesOrderDao mQODao = null;
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	private RequestWrapper request=null;
	
	// 页面导向URL
	private final String queryUrl = "/jsp/feedbackMng/query/marketQuesTechApply.jsp";
	private final String queryOrderDetailUrl= "/jsp/feedbackMng/query/marketOrderDetailInfo.jsp";
	
	public void marketOrderQueryInit(){
		try {
			act=ActionContext.getContext();
			act.setOutData("seriesList", InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(queryUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "市场问题处理工单车厂查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void queryMarketOrderInfo(){
		act=ActionContext.getContext();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
		RequestWrapper request = act.getRequest();
		List<Object> params = new LinkedList<Object>();

		// 获得DAO层实例
		mQODao = MarketQuesOrderDao.getInstance();
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")) : 1;
		// 查询语句
		StringBuffer sqlStr = new StringBuffer();
		StringBuffer whereStr = new StringBuffer();
		StringBuffer orderByStr = new StringBuffer();
		sqlStr.append("select tt.order_id,td.dealer_code,td.dealer_shortname,tt.vin,tvmg.group_name,tt.money,tt.comp_type,");
		sqlStr.append("tt.info_type,tt.problem_describe, tt.user_request,tt.advice_deal_mode,to_char(tt.order_date,'yyyy-MM-dd') order_date,tc.code_desc,tt.create_date,tt.create_by,");
		sqlStr.append("tt.update_date,tt.update_by,tt.link_man,tt.tel ");
		sqlStr.append("from tt_if_market tt,tm_dealer td,tc_code tc,tm_vehicle tmv,tm_vhcl_material_group tvmg ");
		sqlStr.append("where 1=1 and tt.vin=tmv.vin and tmv.series_id=tvmg.group_id ");
		sqlStr.append("and tt.status=tc.code_id and tt.dealer_id=td.dealer_id ");
		sqlStr.append("and tt.status!="+Constant.MARKET_BACK_STATUS_UNREPORT+" and tt.is_del='0' ");
		// 从request中取出查询条件
		String orderId = request.getParamValue("query_order_no");// 工单号
		String vin = request.getParamValue("query_vin");// 车辆识别码
		String dealerCode= request.getParamValue("dealerCode");//申请单位代码
		String orderStatus=request.getParamValue("status");//工单状态
		String comp_type = request.getParamValue("comp_type");//投诉类型
		String info_type = request.getParamValue("info_type");//信息类别
		String start_date = request.getParamValue("query_start_date");// 提报起始日期
		String end_date = request.getParamValue("query_end_date");// 提报结束日期
		String vehicleType = request.getParamValue("vehicleSeriesList");// 车型

		if (orderId != null && !"".equals(orderId))
			whereStr.append(" and tt.order_id like '%" + orderId + "%'");
		if (Utility.testString(dealerCode))
			whereStr.append(Utility.getConSqlByParamForEqual(dealerCode, params, "td", "dealer_code")); 
		if (orderStatus != null && !"".equals(orderStatus))
			whereStr.append(" and tt.status='" + orderStatus + "' ");
		if (vin != null && !"".equals(vin))
			whereStr.append(" and tt.vin like '%" + vin + "%' ");
		if (comp_type != null && !"".equals(comp_type))
			whereStr.append(" and tt.comp_type like '%" + comp_type + "%' ");
		if (info_type != null && !"".equals(info_type))
			whereStr.append(" and tt.info_type='" + info_type + "' ");
		if (start_date != null && !"".equals(start_date))
			whereStr.append(" and tt.order_date>=to_date('" + start_date+ "','YYYY-MM-DD') ");
		if (end_date != null && !"".equals(end_date))
			whereStr.append(" and tt.order_date<=to_date('" + end_date+ "','YYYY-MM-DD') ");
		if (vehicleType != null && !"".equals(vehicleType))
			whereStr.append(" and tvmg.group_id='" + vehicleType + "' ");
		
		String dealerIds = GetOrgIdsOrDealerIdsDAO.getDealerIds(logonUserBean, Constant.DEALER_TYPE_DWR);
		if(!"".equals(dealerIds)){
			whereStr.append(" and tt.dealer_id in (" +dealerIds+")");
		}
		if(null!=companyId&&!"".equals(companyId)){
			whereStr.append(" and tt.company_id = "+companyId);
		}
		orderByStr.append(" order by tt.create_date desc ");
		PageResult<TtIfMarketBean> ps = mQODao.pageQueryData(sqlStr.toString()
				+ whereStr.toString() + orderByStr.toString(), params,curPage,
				Constant.PAGE_SIZE);
		act.setOutData("ps", ps);
	}
	/**
	 * Function：查询工单明细
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-5-25
	 */
	public void queryOrderDetailInfo(){
		act=ActionContext.getContext();
		request = act.getRequest();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String orderId = request.getParamValue("orderId");//工单号
		// 查询语句
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select tt.order_id,tt.status,tt.dealer_id,td.dealer_code,dealer_shortname,td.dealer_name,tt.link_man,tt.tel,tt.money,tt.vin,");
		sqlStr.append("tvmg.group_name,tv.engine_no,to_char(tv.factory_date,'yyyy-mm-dd') factory_date,to_char(tas.consignation_date,'yyyy-mm-dd') delivery_date,tv.history_mile,");
		sqlStr.append("tc.ctm_name as customer_name,tc.main_phone as mobile,tc.address as address_desc,");
		sqlStr.append("tt.info_type,to_char(tt.order_date,'yyyy-mm-dd') order_date,tt.comp_type,tt.problem_describe, tt.user_request,tt.advice_deal_mode ");
		sqlStr.append("from tt_if_market tt,TT_DEALER_ACTUAL_SALES tas,tm_vehicle tv,");
		sqlStr.append("tm_vhcl_material_group tvmg,tm_dealer td,TT_CUSTOMER TC ");
		sqlStr.append("where tt.vin=tv.vin and tv.vehicle_id=tas.vehicle_id(+) and tv.series_id=tvmg.group_id and tt.dealer_id=td.dealer_id ");
		sqlStr.append(" and tc.ctm_id=tas.ctm_id ");
		sqlStr.append(" and tt.order_id='" + orderId + "'");
		//获得DAO层实例
		mQODao = MarketQuesOrderDao.getInstance();
		TtIfMarketDetailBean detailBean = mQODao.getMarketDetailInfoData(sqlStr.toString(), 1, 1);
		request.setAttribute("marketOrderDetailBean", detailBean);
		
		sqlStr.delete(0, sqlStr.length());
		sqlStr.append("select to_char(tt.audit_date,'yyyy-mm-dd') audit_date,tu.name user_name,tmo.org_name,tc.code_desc status,tt.audit_content ");
		sqlStr.append("from tt_if_market_audit tt,tc_user tu,tm_org tmo,tc_code tc ");
		sqlStr.append("where tt.audit_by=tu.user_id and tt.audit_status=tc.code_id ");
		sqlStr.append("and tc.type='1014' and tt.org_id=tmo.org_id(+) ");
		sqlStr.append(" and tt.order_id='"+orderId+"'");
		sqlStr.append(" order by tt.audit_date desc");
		List<TtIfMarketAuditListBean> list= mQODao.getMarketOrderAuditList(sqlStr.toString(), 1, 100);
		request.setAttribute("auditList", list);
		act.setForword(queryOrderDetailUrl);
	}
}
