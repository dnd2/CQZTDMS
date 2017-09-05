package com.infodms.dms.actions.feedbackmng.apply;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TtIfMarketAuditPO;
import com.infodms.dms.po.TtIfMarketPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class MarketQuestionOrderApplyManager {

	public Logger logger = Logger.getLogger(MarketQuestionOrderApplyManager.class);
	private MarketQuesOrderDao mQODao = null;
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	private RequestWrapper request=null;

	// 页面导向URL
	private final String queryInitUrl = "/jsp/feedbackMng/apply/marketQuesApply.jsp";
	private final String addOrderUrl = "/jsp/feedbackMng/apply/addMarketQuesApply.jsp";
	private final String modifyUrl = "/jsp/feedbackMng/apply/modifyMarketQuesApply.jsp";
	private final String detailInfoUrl = "/jsp/feedbackMng/apply/queryDetailMarketQuesApply.jsp";

	public void marketQuesApplyInit() {
		try {
			act=ActionContext.getContext();
			act.setOutData("seriesList", InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(queryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "市场问题处理工单查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}

	/**
	 * Function：根据经销商查询条件获取市场问题处理工单数据
	 * 
	 * @param ：
	 * @return: LastUpdate： 2010-5-18
	 */
	public void queryOrderInfo() {
		act=ActionContext.getContext();
		RequestWrapper request = act.getRequest();

		// 获得DAO层实例
		mQODao = MarketQuesOrderDao.getInstance();
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")) : 1;
		// 查询语句
		StringBuffer sqlStr = new StringBuffer();
		StringBuffer whereStr = new StringBuffer();
		StringBuffer orderByStr = new StringBuffer();
		sqlStr.append("select tt.order_id,tt.dealer_id,td.dealer_name,tt.vin,tvmg.group_name,tt.money,tt.comp_type,");
		sqlStr.append("tt.info_type,to_char(tt.order_date,'YYYY-MM-DD') order_date,tc.code_desc,tt.create_date,tt.create_by,");
		sqlStr.append("tt.update_date,tt.update_by,tt.link_man,tt.tel ");
		sqlStr.append("from tt_if_market tt,tm_dealer td,tc_code tc,tm_vehicle tmv,tm_vhcl_material_group tvmg ");
		sqlStr.append("where 1=1 and tt.vin=tmv.vin and tmv.series_id=tvmg.group_id ");
		sqlStr.append("and tt.status=tc.code_id and tt.dealer_id=td.dealer_id ");
		sqlStr.append("and tt.is_del='0' and (tt.status='"+Constant.MARKET_BACK_STATUS_UNREPORT+"' ");
		sqlStr.append(" or tt.status='"+Constant.MARKET_BACK_STATUS_AREA_REJECT+"' ");
		sqlStr.append(" or tt.status='"+Constant.MARKET_BACK_STATUS_TECH_REJECT+"') ");
		// 从request中取出查询条件
		String orderId = request.getParamValue("query_order_no");// 工单号
		String vin = request.getParamValue("query_vin");// 车辆识别码
		String start_date = request.getParamValue("query_start_date");// 提报起始日期
		String end_date = request.getParamValue("query_end_date");// 提报结束日期
		String vehicleType = request.getParamValue("vehicleSeriesList");// 车型

		if (orderId != null && !"".equals(orderId))
			whereStr.append(" and tt.order_id like '%" + orderId + "%' ");
		if (vin != null && !"".equals(vin))
			whereStr.append(" and tt.vin like '%" + vin + "%' ");
		if (start_date != null && !"".equals(start_date))
			whereStr.append(" and tt.order_date>=to_date('" + start_date+ "','YYYY-MM-DD') ");
		if (end_date != null && !"".equals(end_date))
			whereStr.append(" and tt.order_date<=to_date('" + end_date+ "','YYYY-MM-DD') ");
		if (vehicleType != null && !"".equals(vehicleType))
			whereStr.append(" and tvmg.group_id='" + vehicleType + "' ");
		
		orderByStr.append(" order by tt.create_date desc ");

		PageResult<TtIfMarketBean> ps = mQODao.pageQueryData(sqlStr.toString()
				+ whereStr.toString() + orderByStr.toString(),null, curPage,
				Constant.PAGE_SIZE);
		act.setOutData("ps", ps);
	}

	/**
	 * Function：保存市场工单信息
	 * 
	 * @param ：
	 * @return: LastUpdate： 2010-5-18
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public void goAddMarkerOrder() throws ParseException {
		act=ActionContext.getContext();
		logonUserBean = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
		mQODao = MarketQuesOrderDao.getInstance();
		request = act.getRequest();
		String command = request.getParamValue("command");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		if (command == null || "".equals(command)) {
			if(null!=logonUserBean.getUserId()&&!"".equals(logonUserBean.getUserId()))
			{
				TcUserPO tu = new TcUserPO();
				tu.setUserId(logonUserBean.getUserId());
				List<TcUserPO> tuList = mQODao.select(tu);
				if (tuList!=null){
					if (tuList.size()>0){
						tu = tuList.get(0);
					}
				}
				String phone = tu.getPhone();
				act.setOutData("phone", phone);
			}
			act.setOutData("curSysDate", dateFormat.format(new Date()));
			act.setForword(addOrderUrl);
		} else if ("1".equals(command)) {

			String createOrderId = SequenceManager.getSequence("SCQO");
			long longonDealerId = Long.parseLong(logonUserBean.getDealerId());
			String vin = request.getParamValue("vin");// vin
			double apply_money = Double.parseDouble(request.getParamValue("apply_money"));// 申报金额
			String compType = request.getParamValue("compType");// 投诉类型
			Integer infoType = Integer.parseInt(request.getParamValue("info_type"));// 信息类别
			//modify by xiayanpeng begin 申请内容当用户不输入时为NULL，出现空指针错误
			String problemDescribe = CommonUtils.checkNull(request.getParamValue("question_content"));// 问题描述
			String userRequest = CommonUtils.checkNull(request.getParamValue("user_content"));// 用户要求如何
			String adviceDealMode = CommonUtils.checkNull(request.getParamValue("deal_content"));// 建议处理方式
			//modify by xiayanpeng end
			String send_date = request.getParamValue("send_date");// 发出时间
			long creator_id = logonUserBean.getUserId();
			String link_man = request.getParamValue("DEALER_NAME");// 联系人
			String link_tel = request.getParamValue("link_tel");// 联系电话
			TtIfMarketPO insertObj = new TtIfMarketPO();
			insertObj.setOrderId(createOrderId);
			insertObj.setDealerId(longonDealerId);
			insertObj.setVin(vin);
			insertObj.setMoney(apply_money);
			insertObj.setCompType(compType);
			insertObj.setInfoType(infoType);
//			insertObj.setContent(appContent.trim());
			insertObj.setProblemDescribe(problemDescribe.trim());
			insertObj.setUserRequest(userRequest.trim());
			insertObj.setAdviceDealMode(adviceDealMode.trim());
			insertObj.setOrderDate(dateFormat.parse(send_date));
			insertObj.setStatus(Constant.MARKET_BACK_STATUS_UNREPORT);
			insertObj.setCreateDate(new Date());
			insertObj.setCreateBy(creator_id);
			insertObj.setLinkMan(link_man);
			insertObj.setTel(link_tel);
			insertObj.setCompanyId(companyId);
			//modify by xiayanpeng begin 未插入IS_DEL，查询功能IS_DEL=0导致所有新增数据无法查询
			insertObj.setIsDel(new Integer(Constant.IS_DEL_00));
			//modify by xiayanpeng end 
			// 获得DAO层实例
			mQODao = MarketQuesOrderDao.getInstance();
			mQODao.insert(insertObj);
			marketQuesApplyInit();
		}
	}

	public void modifyOrderDetailInfo() {
		act=ActionContext.getContext();
		request = act.getRequest();
		String orderId = request.getParamValue("ORDER_ID");
		if (orderId != null||!"".equals(orderId)) {
			// 查询语句
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("select tt.order_id,tt.link_man,tt.tel,tt.money,tt.vin,");
			//sqlStr.append("tvmg.group_name,tv.engine_no,to_char(tv.factory_date,'yyyy-mm-dd') factory_date,to_char(tas.delivery_date,'yyyy-mm-dd') delivery_date,tv.history_mile,");
			//sqlStr.append("tas.customer_name,tas.mobile,tas.address_desc,");
			sqlStr.append("tvmg.group_name,tv.engine_no,to_char(tv.factory_date,'yyyy-mm-dd') factory_date,to_char(tas.consignation_date,'yyyy-mm-dd') delivery_date,tv.history_mile,");
			sqlStr.append("tc.ctm_name as customer_name,tc.main_phone as mobile,tc.address as address_desc,");
			sqlStr.append("tt.info_type,to_char(tt.order_date,'yyyy-mm-dd') order_date,tt.comp_type,tt.problem_describe, tt.user_request,tt.advice_deal_mode");
			//sqlStr.append(" from tt_if_market tt,TT_ACTUAL_SALES tas,tm_vehicle tv,");
			sqlStr.append(" from tt_if_market tt,TT_DEALER_ACTUAL_SALES tas,tm_vehicle tv,");
			sqlStr.append("tm_vhcl_material_group tvmg,tt_customer tc  ");
			sqlStr.append("where tt.vin=tv.vin and tv.vehicle_id=tas.vehicle_id(+) and tv.series_id=tvmg.group_id ");
			sqlStr.append("and tc.ctm_id=tas.ctm_id ");
			sqlStr.append(" and tt.order_id='" + orderId + "'");
			System.out.println("=======>"+sqlStr.toString());
			// 获得DAO层实例
			mQODao = MarketQuesOrderDao.getInstance();
			TtIfMarketDetailBean detailBean = mQODao.getMarketDetailInfoData(sqlStr.toString(), 1, 1);
			request.setAttribute("marketOrderDetailBean", detailBean);
			act.setForword(modifyUrl);
		}
	}

	public void delMarketOrder() {
		act=ActionContext.getContext();
		request = act.getRequest();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String orderId = request.getParamValue("orderIds");// 工单号
		TtIfMarketPO delPre = new TtIfMarketPO();
		int delSuccessNum = 0;
		String[] orderIdArray = orderId.split(","); // 取得所有orderId放在数组中;
		try {
			if (orderId != null && !"".equals(orderId)) {
				delPre.setUpdateDate(new Date());// 修改时间
				delPre.setIsDel(Constant.IS_DEL);// 1表示删除
				delPre.setUpdateBy(logonUserBean.getUserId());// 修改人
				mQODao = MarketQuesOrderDao.getInstance();
				delSuccessNum = mQODao.delMarketOrder(orderIdArray, delPre);
			}
			if (delSuccessNum == orderIdArray.length) {
				act.setOutData("returnDelFlag", 1);
			} else {
				act.setOutData("returnDelFlag", -1);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "删除市场问题处理工单");
			logger.error(delPre, e1);
			act.setException(e1);
		}
	}

	public void reportMarketOrder() {
		act=ActionContext.getContext();
		request = act.getRequest();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String orderId = request.getParamValue("orderIds");// 工单号
		TtIfMarketPO timp = new TtIfMarketPO();
		int reportSuccessNum = 0;
		String[] orderIdArray = orderId.split(","); // 取得所有orderId放在数组中
		try {
			if (orderId!=null&&!"".equals(orderId)) {
				timp.setUpdateDate(new Date());//上报时间
				timp.setUpdateBy(logonUserBean.getUserId());//上报人
				timp.setStatus(Constant.MARKET_BACK_STATUS_REPORTED);
				
				TtIfMarketAuditPO  timap =new TtIfMarketAuditPO();//奖惩审批明细表
				timap.setAuditDate(new Date());//审核时间
				timap.setAuditBy(logonUserBean.getUserId());//审核人
				timap.setAuditStatus(Constant.MARKET_BACK_STATUS_REPORTED);
				timap.setAuditContent("经销商上报!");
				timap.setOrgId(logonUserBean.getOrgId());
				mQODao = MarketQuesOrderDao.getInstance();
				reportSuccessNum=mQODao.updateMarketOrderStatus(orderIdArray,timp,timap);
			}
			if(reportSuccessNum==orderIdArray.length){
				act.setOutData("returnReportFlag", 1);
			}else{
				act.setOutData("returnReportFlag", -1);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "上报市场问题处理工单");
			logger.error(timp, e1);
			act.setException(e1);
		}
	}
	public void queryOrderDetailInfo(){
		act=ActionContext.getContext();
		request = act.getRequest();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String orderId=request.getParamValue("orderId");//工单号
		try {
			// 查询语句
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("select tt.order_id,tt.link_man,tt.tel,trim(to_char(tt.money,'999,999,999,999,990.00')) money,tt.vin,");
			sqlStr.append("tvmg.group_name,tv.engine_no,tv.factory_date,tv.history_mile,");
			//sqlStr.append("tas.customer_name,tas.mobile,tas.address_desc,tas.delivery_date,");
			sqlStr.append(" tc.ctm_name as customer_name,tc.main_phone as mobile,tc.address as address_desc,tas.consignation_date as delivery_date, ");
			sqlStr.append("tt.info_type,tt.order_date,tt.comp_type,tt.problem_describe, tt.user_request,tt.advice_deal_mode ");
			sqlStr.append("from tt_if_market tt,TT_DEALER_ACTUAL_SALES tas,tm_vehicle tv,");
			sqlStr.append("tm_vhcl_material_group tvmg,TT_CUSTOMER TC ");
			sqlStr.append("where tt.vin=tv.vin and tv.vehicle_id=tas.vehicle_id(+) and tv.series_id=tvmg.group_id ");
			sqlStr.append(" and tc.ctm_id=tas.ctm_id ");
			sqlStr.append(" and tt.order_id='" + orderId + "'");
			// 获得DAO层实例
			mQODao = MarketQuesOrderDao.getInstance();
			TtIfMarketDetailBean detailBean = mQODao.getMarketDetailInfoData(sqlStr.toString(), 1, 1);
			
			if(null!=detailBean.getOrder_date()&&detailBean.getOrder_date().length()>=10){
			detailBean.setOrder_date(detailBean.getOrder_date().substring(0,10));
			}
			if(null!=detailBean.getFactory_date()&&detailBean.getFactory_date().length()>=10){
				detailBean.setFactory_date(detailBean.getFactory_date().substring(0,10));
			}
			if(null!=detailBean.getDelivery_date()&&detailBean.getDelivery_date().length()>=10){
				detailBean.setDelivery_date(detailBean.getDelivery_date().substring(0,10));
			}
			
			request.setAttribute("marketOrderDetailBean", detailBean);
			sqlStr.delete(0, sqlStr.length());
			sqlStr.append("select to_char(tt.audit_date,'yyyy-mm-dd') audit_date,tu.name user_name,tmo.org_name,tc.code_desc status,tt.audit_content ");
			sqlStr.append("from tt_if_market_audit tt,tc_user tu,tm_org tmo,tc_code tc ");
			sqlStr.append("where tt.audit_by=tu.user_id and tt.audit_status=tc.code_id ");
			sqlStr.append("and tc.type='1014' and tt.org_id=tmo.org_id(+) ");
			sqlStr.append(" and tt.order_id='"+orderId+"'");
			sqlStr.append(" order by tt.audit_date desc");
			List<TtIfMarketAuditListBean> list= mQODao.getMarketOrderAuditList(sqlStr.toString(), 1, 100);
//			System.out.println(list.size());
			request.setAttribute("approveDetailList", list);
			act.setForword(detailInfoUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"市场问题处理工单详细信息");
			logger.error(logonUserBean,e1);
			act.setException(e1);
		}
	}
	public void modifyMarkerOrder(){
		act=ActionContext.getContext();
		request = act.getRequest();
		String orderId = request.getParamValue("ORDER_ID");
		String vin = request.getParamValue("vin");// vin
		double apply_money = Double.parseDouble(request.getParamValue("apply_money"));// 申报金额
		String compType = request.getParamValue("compType");// 投诉类型
		Integer infoType = Integer.parseInt(request.getParamValue("info_type"));// 信息类别
		//String appContent = request.getParamValue("apply_content");// 申请内容
		String problemDescribe = CommonUtils.checkNull(request.getParamValue("question_content"));// 问题描述
		String userRequest = CommonUtils.checkNull(request.getParamValue("user_content"));// 用户要求如何
		String adviceDealMode = CommonUtils.checkNull(request.getParamValue("deal_content"));// 建议处理方式
		String link_man = request.getParamValue("DEALER_NAME");// 联系人
		String link_tel = request.getParamValue("link_tel");// 联系电话
		String send_date=request.getParamValue("send_date");//提报日期
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		TtIfMarketPO updateObj=new TtIfMarketPO();
		int updateSuccessNum = 0;
		try {
			if (orderId != null||!"".equals(orderId)) {
				updateObj.setLinkMan(link_man.trim());
				updateObj.setTel(link_tel.trim());
				updateObj.setVin(vin.trim());
				updateObj.setInfoType(infoType);
				updateObj.setCompType(compType);
				updateObj.setMoney(apply_money);
				updateObj.setOrderDate(dateFormat.parse(send_date));
				updateObj.setCompType(compType);
				updateObj.setProblemDescribe(problemDescribe);
				updateObj.setUserRequest(userRequest);
				updateObj.setAdviceDealMode(adviceDealMode);
//				updateObj.setContent(appContent.trim());
				// 获得DAO层实例
				mQODao = MarketQuesOrderDao.getInstance();
				updateSuccessNum=mQODao.updateSingleOrder(orderId, updateObj);
				if(updateSuccessNum==1){
					act.setOutData("approveResult","approveSuccess");
				}else{
					act.setOutData("approveResult","approveFailure");
				}
				StringBuffer sqlStr=new StringBuffer();
				sqlStr.append("select tt.order_id,tt.link_man,tt.tel,tt.money,tt.vin,");
				sqlStr.append("tvmg.group_name,tv.engine_no,to_char(tv.factory_date,'yyyy-mm-dd') factory_date,to_char(tas.consignation_date,'yyyy-mm-dd') delivery_date,tv.history_mile,");
				//sqlStr.append("tas.customer_name,tas.mobile,tas.address_desc,");
				sqlStr.append(" tc.ctm_name as customer_name,tc.main_phone as mobile,tc.address as address_desc, ");
				sqlStr.append("tt.info_type,to_char(tt.order_date,'yyyy-mm-dd') order_date,tt.comp_type,tt.problem_describe,tt.user_request,tt.advice_deal_mode ");
				sqlStr.append("from tt_if_market tt,TT_DEALER_ACTUAL_SALES tas,tm_vehicle tv,");
				sqlStr.append("tm_vhcl_material_group tvmg,TT_CUSTOMER TC  ");
				sqlStr.append("where tt.vin=tv.vin and tv.vehicle_id=tas.vehicle_id(+) and tv.series_id=tvmg.group_id ");
				sqlStr.append(" and tc.ctm_id=tas.ctm_id ");
				sqlStr.append(" and tt.order_id='" + orderId + "'");
				// 获得DAO层实例
				mQODao = MarketQuesOrderDao.getInstance();
				TtIfMarketDetailBean detailBean = mQODao.getMarketDetailInfoData(sqlStr.toString(), 1, 1);
				request.setAttribute("marketOrderDetailBean", detailBean);
				act.setForword(queryInitUrl);
				marketQuesApplyInit();
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "修改市场问题处理工单");
			logger.error(updateObj, e1);
			act.setException(e1);
		}
	}
}
