package com.infodms.dms.actions.feedbackmng.approve;

import java.util.Date;
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
import com.infodms.dms.po.TtIfMarketAuditPO;
import com.infodms.dms.po.TtIfMarketPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;


public class MarketQuestionOrderApproveManager {
	public Logger logger = Logger.getLogger(MarketQuestionOrderApproveManager.class);
	private MarketQuesOrderDao mQODao = null;
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	private RequestWrapper request=null;
	
	// 页面导向URL
	private final String queryAreaInitUrl = "/jsp/feedbackMng/approve/marketAreaQuesApply.jsp";
	private final String queryTechInitUrl = "/jsp/feedbackMng/approve/marketTechQuesApply.jsp";
	private final String approveAreaOrderUrl = "/jsp/feedbackMng/approve/approveAreaMarketQuesApply.jsp";
	private final String approveTechOrderUrl = "/jsp/feedbackMng/approve/approveTechMarketQuesApply.jsp";
	private final String queryOrderDetailUrl = "/jsp/feedbackMng/approve/marketOrderDetailInfo.jsp";
	/**
	 * Function：区域查询市场问题处理工单信息
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-5-24
	 */
	public void areaApproveOrderInit(){
		try {
			act=ActionContext.getContext();
			act.setOutData("seriesList", InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(queryAreaInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "市场问题处理工单区域审核");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void areaApproveTechInit(){
		try {
			act=ActionContext.getContext();
			act.setOutData("seriesList", InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(queryTechInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "市场问题处理工单区域审核");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：技术支持室查询市场问题处理工单信息
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-5-24
	 */
	public void techApproveOrderInit(){
		try {
			act=ActionContext.getContext();
			act.setOutData("seriesList", InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(queryTechInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "市场问题处理工单区域审核");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：信息反馈审批--市场问题处理工单区域审批页面查询
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-5-24
	 */
	public void queryAreaApproveOrderInfo(){
		act=ActionContext.getContext();
		request = act.getRequest();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
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
		sqlStr.append("tt.info_type,tt.problem_describe, tt.user_request,tt.advice_deal_mode,to_char(tt.order_date,'YYYY-MM-DD') order_date,tc.code_desc,tt.create_date,tt.create_by,");
		sqlStr.append("tt.update_date,tt.update_by,tt.link_man,tt.tel ");
		sqlStr.append("from tt_if_market tt,tm_dealer td,tc_code tc,tm_org tot,tm_vehicle tmv,tm_vhcl_material_group tvmg ");
		sqlStr.append("where 1=1 and tt.vin=tmv.vin and tmv.series_id=tvmg.group_id ");
		sqlStr.append("and tt.status=tc.code_id and tt.dealer_id=td.dealer_id and td.company_id=tot.company_id ");
		// 如果是区域总监则只查询区域通过的数据
		if(Constant.areaManager.equals(mQODao.getPoseCode(logonUserBean.getPoseId()))){
			sqlStr.append("and tt.is_del='0' and tt.status='"+Constant.MARKET_BACK_STATUS_AREA_PASS+"' ");
			// 对于money<=1000的区域总监不再审核
			sqlStr.append(" and tt.money>1000");
		} else {
			sqlStr.append("and tt.is_del='0' and tt.status='"+Constant.MARKET_BACK_STATUS_REPORTED+"' ");
		}
		// 从request中取出查询条件
		String orderId = request.getParamValue("query_order_no");// 工单号
		String vin = request.getParamValue("query_vin");// 车辆识别码
		String comp_type = request.getParamValue("comp_type");//投诉类型
		String info_type = request.getParamValue("info_type");//信息类别
		String start_date = request.getParamValue("query_start_date");// 提报起始日期
		String end_date = request.getParamValue("query_end_date");// 提报结束日期
		String vehicleType = request.getParamValue("vehicleSeriesList");// 车型
		String dealerCode= request.getParamValue("DEALER_CODE");//申请单位代码
		
		//当开始时间和结束时间相同时
		if(null!=start_date&&!"".equals(start_date)&&null!=end_date&&!"".equals(end_date)){
				start_date = start_date+" 00:00:00";
				end_date = end_date+" 23:59:59";
		}
		if (dealerCode != null && !"".equals(dealerCode))
			whereStr.append(" and td.dealer_code='" + dealerCode + "' ");

		if (orderId != null && !"".equals(orderId))
			whereStr.append(" and tt.order_id like '%" + orderId + "%' ");
		if (vin != null && !"".equals(vin))
			whereStr.append(" and tt.vin like '%" + vin + "%' ");
		if (comp_type != null && !"".equals(comp_type))
			whereStr.append(" and tt.comp_type='" + comp_type + "' ");
		if (info_type != null && !"".equals(info_type))
			whereStr.append(" and tt.info_type='" + info_type + "' ");
		if (start_date != null && !"".equals(start_date))
			whereStr.append(" and tt.order_date>=to_date('" + start_date+ "','YYYY-MM-DD hh24:mi:ss') ");
		if (end_date != null && !"".equals(end_date))
			whereStr.append(" and tt.order_date<=to_date('" + end_date+ "','YYYY-MM-DD hh24:mi:ss') ");
		if (vehicleType != null && !"".equals(vehicleType))
			whereStr.append(" and tvmg.group_id='" + vehicleType + "' ");
		//modify by xiayanpeng begin 加这个没有意义 问题较多，很多地方都需修改，之后不加注释了
		//whereStr.append(" and EXISTS(select company_id from tm_org tmo where tmo.parent_org_id = '"+logonUserBean.getOrgId()+"' and company_id=tmo.company_id)");
		//modify by xiayanpeng end 
		
		String dealerIds = GetOrgIdsOrDealerIdsDAO.getDealerIds(logonUserBean, Constant.DEALER_TYPE_DWR);
		if(!"".equals(dealerIds)){
			whereStr.append(" and tt.dealer_id in (" +dealerIds+")");
		}if(null!=companyId&&!"".equals(companyId)){
			whereStr.append(" and tt.company_id = "+companyId);
		}
		orderByStr.append(" order by tt.create_date desc ");

		PageResult<TtIfMarketBean> ps = mQODao.pageQueryData(sqlStr.toString()
				+ whereStr.toString() + orderByStr.toString(),null, curPage,
				Constant.PAGE_SIZE);
		act.setOutData("ps", ps);
	}
	/**
	 * Function：信息反馈审批--市场问题处理工单技术支持审批页面查询
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-5-24
	 */
	public void queryTechApproveOrderInfo(){
		act=ActionContext.getContext();
		request = act.getRequest();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
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
		sqlStr.append("tt.info_type,tt.problem_describe, tt.user_request,tt.advice_deal_mode,to_char(tt.order_date,'yyyy-mm-dd') order_date,tc.code_desc,tt.create_date,tt.create_by,");
		sqlStr.append("tt.update_date,tt.update_by,tt.link_man,tt.tel ");
		sqlStr.append("from tt_if_market tt,tm_dealer td,tc_code tc,tm_org tot,tm_vehicle tmv,tm_vhcl_material_group tvmg ");
		sqlStr.append("where 1=1 and tt.vin=tmv.vin and tmv.series_id=tvmg.group_id ");
		sqlStr.append("and tt.status=tc.code_id and tt.dealer_id=td.dealer_id and td.company_id=tot.company_id ");
		sqlStr.append("and tt.is_del='0' ");
		// 1000元以下的单子，由区域工程师审核以后直接到服务部审核
		// 1000元以上（含1000）的单据，由区域工程师审核后再由区域经理审核 最后由服务部审核 
		sqlStr.append("and ((tt.status = '"+Constant.MARKET_BACK_STATUS_AREA_PASS+"' and tt.money<=1000) or (tt.money>1000 and tt.status='"+Constant.MARKET_BACK_STATUS_AREA_DIRECTOR_PASS+"')) ");
		// 从request中取出查询条件
		String orderId = request.getParamValue("query_order_no");// 工单号
		String vin = request.getParamValue("query_vin");// 车辆识别码
		String comp_type = request.getParamValue("comp_type");//投诉类型
		String info_type = request.getParamValue("info_type");//信息类别
		String start_date = request.getParamValue("query_start_date");// 提报起始日期
		String end_date = request.getParamValue("query_end_date");// 提报结束日期
		String vehicleType = request.getParamValue("vehicleSeriesList");// 车型
		String dealerCodes = request.getParamValue("dealerCode");//供应商代码
		//当开始时间和结束时间相同时
		if(null!=start_date&&!"".equals(start_date)&&null!=end_date&&!"".equals(end_date)){
			if(start_date.equals(end_date)){
				start_date = start_date+" 00:00:00";
				end_date = end_date+" 23:59:59";
			}
		}
		if (orderId != null && !"".equals(orderId))
			whereStr.append(" and tt.order_id like '%" + orderId + "%' ");
		if (vin != null && !"".equals(vin))
			whereStr.append(" and tt.vin like '%" + vin + "%' ");
		if (comp_type != null && !"".equals(comp_type))
			whereStr.append(" and tt.comp_type='" + comp_type + "' ");
		if (info_type != null && !"".equals(info_type))
			whereStr.append(" and tt.info_type='" + info_type + "' ");
		if(null!=companyId&&!"".equals(companyId))
			whereStr.append(" and tt.company_id="+companyId);
		if (start_date != null && !"".equals(start_date))
			whereStr.append(" and tt.order_date>=to_date('" + start_date+ "','YYYY-MM-DD hh24:mi:ss') ");
		if (end_date != null && !"".equals(end_date))
			whereStr.append(" and tt.order_date<=to_date('" + end_date+ "','YYYY-MM-DD hh24:mi:ss') ");
		if (vehicleType != null && !"".equals(vehicleType))
			whereStr.append(" and tvmg.group_id='" + vehicleType + "' ");
		if (Utility.testString(dealerCodes)){
			whereStr.append(Utility.getConSqlByParamForEqual(dealerCodes, params, "td", "dealer_code")); 
		}
		orderByStr.append(" order by tt.create_date desc ");

		PageResult<TtIfMarketBean> ps = mQODao.pageQueryData(sqlStr.toString()
				+ whereStr.toString() + orderByStr.toString(),params, curPage,
				Constant.PAGE_SIZE);
		act.setOutData("ps", ps);
	}
	public void approveMarketOrderQueryDetailInfo(){
		act=ActionContext.getContext();
		request = act.getRequest();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
		String orderId = request.getParamValue("ORDER_ID");//工单号
		String approveManFlag = request.getParamValue("approveFlag");//审批标志 1：区域 ；2:技术支持室
		// 查询语句
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select tt.order_id,tt.dealer_id,td.dealer_code,td.dealer_shortname,tt.link_man,tt.tel,tt.money,tt.vin,");
		sqlStr.append("tvmg.group_name,tv.engine_no,to_char(tv.factory_date,'yyyy-mm-dd') factory_date,to_char(tas.consignation_date,'yyyy-mm-dd')delivery_date,tv.history_mile,");
		sqlStr.append("tc.ctm_name as customer_name,tc.main_phone as mobile,tc.address as address_desc,");
		sqlStr.append("tt.info_type,to_char(tt.order_date,'yyyy-mm-dd') order_date,tt.comp_type,tt.problem_describe, tt.user_request,tt.advice_deal_mode ");
		sqlStr.append("from tt_if_market tt,TT_DEALER_ACTUAL_SALES tas,tm_vehicle tv,");
		sqlStr.append("tm_vhcl_material_group tvmg,tm_dealer td,tt_customer tc  ");
		sqlStr.append("where tt.vin=tv.vin and tv.vehicle_id=tas.vehicle_id(+) and tv.series_id=tvmg.group_id and tt.dealer_id=td.dealer_id ");
		sqlStr.append(" and tc.ctm_id=tas.ctm_id ");
		sqlStr.append(" and tt.order_id='" + orderId + "'");
		sqlStr.append(" and tt.company_id='" + companyId + "'");
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
		if("1".equals(approveManFlag)){
			act.setForword(approveAreaOrderUrl);
		}else if("2".equals(approveManFlag)){
			act.setForword(approveTechOrderUrl);
		}
		
	}
	/**
	 * Function：区域或技术支持室审批工单功能<br>
	 *           在区域审批时1000元以上的申报金额<br>
	 *           要由区域经理进行审批<br>
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-5-24 赵伦达
	 */
	public void approveMarketOrderInfo(){
		act=ActionContext.getContext();
		request = act.getRequest();
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
		String orderId = request.getParamValue("ORDER_ID");//工单号
		String approveManFlag = request.getParamValue("approveFlag");//审批标志 1：区域 ；2:技术支持室
		String approveFlag = request.getParamValue("audit");//审批标志 p：通过 ；r:驳回
		String approveContent=request.getParamValue("content");//审批意见
        
		// 查询语句
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select tt.order_id,tt.dealer_id,td.dealer_name,tt.link_man,tt.tel,tt.money,tt.vin,");
		sqlStr.append("tvmg.group_name,tv.engine_no,tv.factory_date,tas.consignation_date as delivery_date,tv.history_mile,");
		sqlStr.append("tc.ctm_name as customer_name,tc.main_phone as mobile,tc.address as address_desc,");
		sqlStr.append("tt.info_type,tt.order_date,tt.comp_type,tt.problem_describe, tt.user_request,tt.advice_deal_mode ");
		sqlStr.append("from tt_if_market tt,TT_DEALER_ACTUAL_SALES tas,tm_vehicle tv,");
		sqlStr.append("tm_vhcl_material_group tvmg,tm_dealer td,tt_customer tc  ");
		sqlStr.append("where tt.vin=tv.vin and tv.vehicle_id=tas.vehicle_id(+) and tv.series_id=tvmg.group_id and tt.dealer_id=td.dealer_id ");
		sqlStr.append(" and tc.ctm_id=tas.ctm_id ");
		sqlStr.append(" and tt.order_id='" + orderId + "'");
		sqlStr.append(" and tt.company_id='" + companyId + "'");
		//获得DAO层实例
		mQODao = MarketQuesOrderDao.getInstance();
		TtIfMarketDetailBean detailBean = mQODao.getMarketDetailInfoData(sqlStr.toString(), 1, 1);
		
		TtIfMarketPO timp = new TtIfMarketPO();
		int updateSuccessNum = 0;
		
		String[] orderIdArray = orderId.split(",");
		if("1".equals(approveManFlag)){//区域进行审批
			double money=Double.parseDouble(detailBean.getMoney());
			if(money>1000){//大于1000以上需要区域经理审核
				//if(mQODao.getPoseType(logonUserBean.getPoseId())){
					//更新工单状态并插入审核记录
					timp.setUpdateDate(new Date());//审批时间
					timp.setUpdateBy(logonUserBean.getUserId());//审批人
					
					TtIfMarketAuditPO  timap =new TtIfMarketAuditPO();//奖惩审批明细表
					timap.setAuditDate(new Date());//审核时间
					timap.setAuditBy(logonUserBean.getUserId());//审核人
					if("p".equals(approveFlag)){
						// 如果是区域总监,则此时的通过只针对区域通过的数据,同时状态更改为区域总监通过
						if(Constant.areaManager.equals(mQODao.getPoseCode(logonUserBean.getPoseId()))){
							timp.setStatus(Constant.MARKET_BACK_STATUS_AREA_DIRECTOR_PASS);
							timap.setAuditStatus(Constant.MARKET_BACK_STATUS_AREA_DIRECTOR_PASS);
						} else {
							timp.setStatus(Constant.MARKET_BACK_STATUS_AREA_PASS);
							timap.setAuditStatus(Constant.MARKET_BACK_STATUS_AREA_PASS);
						}
					}else{
						if(Constant.areaManager.equals(mQODao.getPoseCode(logonUserBean.getPoseId()))){
							timp.setStatus(Constant.MARKET_BACK_STATUS_AREA_DIRECTOR_REJECT);
							timap.setAuditStatus(Constant.MARKET_BACK_STATUS_AREA_DIRECTOR_REJECT);
						} else {
							timp.setStatus(Constant.MARKET_BACK_STATUS_AREA_REJECT);
							timap.setAuditStatus(Constant.MARKET_BACK_STATUS_AREA_REJECT);
						}
					}
					timap.setAuditContent(approveContent);
					timap.setOrgId(logonUserBean.getOrgId());
					updateSuccessNum=mQODao.updateMarketOrderStatus(orderIdArray,timp,timap);
					if(updateSuccessNum==orderIdArray.length){
						act.setOutData("approveResult","approveSuccess");
					}else{
						act.setOutData("approveResult", "approveFailure");
					}
				//}else{//没有审批权限
				//	act.setOutData("approveResult", "noApproveAuthor");
				//}
			}else{//小于1000元审批
				//更新工单状态并插入审核记录
				timp.setUpdateDate(new Date());//审批时间
				timp.setUpdateBy(logonUserBean.getUserId());//审批人
				
				TtIfMarketAuditPO  timap =new TtIfMarketAuditPO();//奖惩审批明细表
				timap.setAuditDate(new Date());//审核时间
				timap.setAuditBy(logonUserBean.getUserId());//审核人
				if("p".equals(approveFlag)){
					// 如果是区域总监,则此时的通过只针对区域通过的数据,同时状态更改为区域总监通过
					if(Constant.areaManager.equals(mQODao.getPoseCode(logonUserBean.getPoseId()))){
						timp.setStatus(Constant.MARKET_BACK_STATUS_AREA_DIRECTOR_PASS);
						timap.setAuditStatus(Constant.MARKET_BACK_STATUS_AREA_DIRECTOR_PASS);
					} else {
						timp.setStatus(Constant.MARKET_BACK_STATUS_AREA_PASS);
						timap.setAuditStatus(Constant.MARKET_BACK_STATUS_AREA_PASS);
					}
				}else{
					if(Constant.areaManager.equals(mQODao.getPoseCode(logonUserBean.getPoseId()))){
						timp.setStatus(Constant.MARKET_BACK_STATUS_AREA_DIRECTOR_REJECT);
						timap.setAuditStatus(Constant.MARKET_BACK_STATUS_AREA_DIRECTOR_REJECT);
					} else {
						timp.setStatus(Constant.MARKET_BACK_STATUS_AREA_REJECT);
						timap.setAuditStatus(Constant.MARKET_BACK_STATUS_AREA_REJECT);
					}
				}
				timap.setAuditContent(approveContent);
				timap.setOrgId(logonUserBean.getOrgId());
				updateSuccessNum=mQODao.updateMarketOrderStatus(orderIdArray,timp,timap);
				if(updateSuccessNum==orderIdArray.length){
					act.setOutData("approveResult","approveSuccess");
				}else{
					act.setOutData("approveResult", "approveFailure");
				}
			}
		}else if("2".equals(approveManFlag)){//技术支持室审批
			//更新工单状态并插入审核记录
			timp.setUpdateDate(new Date());//审批时间
			timp.setUpdateBy(logonUserBean.getUserId());//审批人
			
			TtIfMarketAuditPO  timap =new TtIfMarketAuditPO();//奖惩审批明细表
			timap.setAuditDate(new Date());//审核时间
			timap.setAuditBy(logonUserBean.getUserId());//审核人
			if("p".equals(approveFlag)){
				timp.setStatus(Constant.MARKET_BACK_STATUS_TECH_PASS);
				timap.setAuditStatus(Constant.MARKET_BACK_STATUS_TECH_PASS);
			}else{
				timp.setStatus(Constant.MARKET_BACK_STATUS_TECH_REJECT);
				timap.setAuditStatus(Constant.MARKET_BACK_STATUS_TECH_REJECT);
			}
			timap.setAuditContent(approveContent);
			timap.setOrgId(logonUserBean.getOrgId());
			updateSuccessNum=mQODao.updateMarketOrderStatus(orderIdArray,timp,timap);
			if(updateSuccessNum==orderIdArray.length){
				act.setOutData("approveResult","approveSuccess");
			}else{
				act.setOutData("approveResult", "approveFailure");
			}
		}
		request.setAttribute("marketOrderDetailBean", detailBean);
//		areaApproveTechInit();
		if("1".equals(approveManFlag)){
			areaApproveOrderInit();
		}else if("2".equals(approveManFlag)){
			areaApproveTechInit();
		}
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
		sqlStr.append("select tt.order_id,tt.dealer_id,td.dealer_code,td.dealer_shortname,td.dealer_name,tt.link_man,tt.tel,tt.money,tt.vin,");
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
		sqlStr.append("select to_char(tt.audit_date,'YYYY-MM-DD') audit_date,tu.name user_name,tmo.org_name,tc.code_desc status,tt.audit_content ");
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
