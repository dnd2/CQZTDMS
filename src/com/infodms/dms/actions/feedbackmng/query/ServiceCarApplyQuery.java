package com.infodms.dms.actions.feedbackmng.query;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.ServiceCarApplyDAO;
import com.infodms.dms.dao.orgmng.GetOrgIdsOrDealerIdsDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtIfServicecarExtPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
* @ClassName: ServiceCarApplyQuery 
* @Description: TODO(服务车申请表车厂端查询) 
* @author wangchao 
* @date May 24, 2010 5:25:45 PM 
*
 */
public class ServiceCarApplyQuery {

	public Logger logger = Logger.getLogger(ServiceCarApplyQuery.class);
	private ServiceCarApplyDAO dao = ServiceCarApplyDAO.getInstance();
	private final String serviceCarApplyQueryURL = "/jsp/feedbackMng/query/serviceCarApplyQuery.jsp";// 查询页面
	private final String serviceCarApplyMiniQueryURL = "/jsp/feedbackMng/query/serviceCarApplyMiniQuery.jsp";// 微车查询页面
	/**
	 * 
	* @Title: serviceCarApplyQueryForward 
	* @Description: TODO(查询跳转页面) 
	* @param      
	* @return void    返回类型 
	* @throws
	 */
	public void serviceCarApplyQueryForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(serviceCarApplyQueryURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "服务车申请表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 
	* @Title: applyQuery 
	* @author subo
	* @Description: TODO(微车查询跳转页面) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void serviceCarApplyQueryMiniInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(serviceCarApplyMiniQueryURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "服务车申请表微车查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: applyQuery 
	* @author subo
	* @Description: TODO(微车查询) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void applyMiniQuery() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		String dealerId = logonUser.getDealerId();
		List<Object> params = new LinkedList<Object>();
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
			String appStatus = request.getParamValue("APP_STATUS");
			String dealerName = request.getParamValue("DEALER_NAME");
			String dealerCode = request.getParamValue("dealerCode");
			
			//转换APP_STATUS(由于轿车微车用2套tc_code,有重复代码)--此处查询转换,插入不用再修改
			if(Constant.SERVICE_APPLY_ACTIVE_STATUS_UNREPORT_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_STATUS_UNREPORT.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_STATUS_REPORTED_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_STATUS_REPORTED.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_PASS_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_PASS.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT.toString();
			
			//dealerId = request.getParamValue("DEALER_ID")==null?null:Long.parseLong(request.getParamValue("DEALER_ID"));
			// 工单号like
			if (orderId != null && !"".equals(orderId)) {
				con.append(" and ORDER_ID like'%" + orderId + "%' "); //
			}
			// 介于开始时间
			if (strDate != null && !"".equals(strDate)) {
				con.append(" and APP_DATE >= to_date('" + strDate+" 00:00:00"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 结束时间
			if (endDate != null && !"".equals(endDate)) {
				con.append(" and APP_DATE <= to_date('" + endDate + " 23:59:59"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 经销商代码
			if (dealerId != null && !"".equals(dealerId)) {
				con.append(" and t.DEALER_ID='" + dealerId + "' ");
			}
			//经销商代码
			if (Utility.testString(dealerCode)) {
				con.append(Utility.getConSqlByParamForEqual(dealerCode, params, "d", "dealer_code")); 
			}
			// 经销商代码
			if (dealerName != null && !"".equals(dealerName)) {
				con.append(" and d.DEALER_NAME LIKE '%" + dealerName + "%' ");
			}
			// 车型
			if (modelId != null && !"".equals(modelId)) {
				con.append(" and T.GROUP_ID='" + modelId + "' ");
			}
			if(null!=companyId&&!"".equals(companyId)){
				con.append(" and t.company_id = "+companyId);
			}
			//申请状态
			if (appStatus != null && !"".equals(appStatus)) {
				/*//审核通过：大区审核，售后服务部，轿车事业部
				if (Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS.equals(appStatus)) {
					con.append(" and (T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_PASS + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS + "') ");
				//审核驳回：大区审核，售后服务部，轿车事业部
				}else if (Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT.equals(appStatus)){
					con.append(" and (T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT + "') ");
				}else {*/
					//已上报，待上报
					con.append(" and T.APP_STATUS='" + appStatus + "' ");
			/*	}*/
			}
			//modify by xiayanpeng begin 加入根据用户ORG_ID过滤经销商ID
			String dealerIds = GetOrgIdsOrDealerIdsDAO.getDealerIds(logonUser, Constant.DEALER_TYPE_DWR);
			if(!"".equals(dealerIds)){
				con.append(" and t.dealer_id in (" +dealerIds+")");
			}
			//modify by xiayanpeng end
			PageResult<TtIfServicecarExtPO> list = dao.applyQuery(con
					.toString(), params,curPage, Constant.PAGE_SIZE);
			// List ls = list.getRecords();
			act.setOutData("ps", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务车申请表微车");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 
	* @Title: applyQuery 
	* @Description: TODO(查询) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void applyQuery() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		String dealerId = logonUser.getDealerId();
		List<Object> params = new LinkedList<Object>();
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
			String appStatus = request.getParamValue("APP_STATUS");
			String dealerName = request.getParamValue("DEALER_NAME");
			String dealerCode = request.getParamValue("dealerCode");
			//dealerId = request.getParamValue("DEALER_ID")==null?null:Long.parseLong(request.getParamValue("DEALER_ID"));
			// 工单号like
			if (orderId != null && !"".equals(orderId)) {
				con.append(" and ORDER_ID like'%" + orderId + "%' "); //
			}
			// 介于开始时间
			if (strDate != null && !"".equals(strDate)) {
				con.append(" and APP_DATE >= to_date('" + strDate+" 00:00:00"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 结束时间
			if (endDate != null && !"".equals(endDate)) {
				con.append(" and APP_DATE <= to_date('" + endDate + " 23:59:59"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 经销商代码
			if (dealerId != null && !"".equals(dealerId)) {
				con.append(" and t.DEALER_ID='" + dealerId + "' ");
			}
			//经销商代码
			if (Utility.testString(dealerCode)) {
				con.append(Utility.getConSqlByParamForEqual(dealerCode, params, "d", "dealer_code")); 
			}
			// 经销商代码
			if (dealerName != null && !"".equals(dealerName)) {
				con.append(" and d.DEALER_NAME LIKE '%" + dealerName + "%' ");
			}
			// 车型
			if (modelId != null && !"".equals(modelId)) {
				con.append(" and T.GROUP_ID='" + modelId + "' ");
			}
			//申请状态
			if (appStatus != null && !"".equals(appStatus)) {
				/*//审核通过：大区审核，售后服务部，轿车事业部
				if (Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS.equals(appStatus)) {
					con.append(" and (T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_PASS + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS + "') ");
				//审核驳回：大区审核，售后服务部，轿车事业部
				}else if (Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT.equals(appStatus)){
					con.append(" and (T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT + "') ");
				}else {*/
					//已上报，待上报
					con.append(" and T.APP_STATUS='" + appStatus + "' ");
			/*	}*/
			}
			//modify by xiayanpeng begin 加入根据用户ORG_ID过滤经销商ID
			String dealerIds = GetOrgIdsOrDealerIdsDAO.getDealerIds(logonUser, Constant.DEALER_TYPE_DWR);
			if(!"".equals(dealerIds)){
				con.append(" and t.dealer_id in (" +dealerIds+")");
			}
			if(null!=companyId&&!"".equals(companyId)){
				con.append(" and t.company_id = "+companyId);
			}
			//modify by xiayanpeng end
			PageResult<TtIfServicecarExtPO> list = dao.applyQuery(con
					.toString(), params,curPage, Constant.PAGE_SIZE);
			// List ls = list.getRecords();
			act.setOutData("ps", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务车申请表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	/*
	 * 服务车总部查询导出功能
	 */
	public void toExl(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try{
			String fileName = "服务车查询.csv";
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			act.getResponse().setContentType("Application/text/csv");
			act.getResponse().addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			StringBuffer con = new StringBuffer();
			String orderId = request.getParamValue("ORDER_ID");
			String strDate = request.getParamValue("CON_APPLY_DATE_START");
			String endDate = request.getParamValue("CON_APPLY_DATE_END");
			String appStatus = request.getParamValue("APP_STATUS");
			String dealerName = request.getParamValue("DEALER_NAME");
			String dealerCode = request.getParamValue("dealerCode");
			
			if (StringUtil.notNull(orderId)) {
				con.append("and A.ORDER_ID like'%" + orderId + "%'\n"); //
			}
			if (StringUtil.notNull(strDate)) {
				con.append("and A.APP_DATE >= to_date('" + strDate+" 00:00:00"
						+ "', 'yyyy-mm-dd hh24:mi:ss')\n");
			}
			if (StringUtil.notNull(endDate)) {
				con.append("and A.APP_DATE <= to_date('" + endDate + " 23:59:59"
						+ "', 'yyyy-mm-dd hh24:mi:ss')\n");
			}
			if(StringUtil.notNull(appStatus))
				con.append("and A.APP_STATUS=").append(appStatus).append("\n");
			if(StringUtil.notNull(dealerCode)){
				con.append("and B.DEALER_CODE IN ('").append(dealerCode.replaceAll(",","','")).append("')\n");
			}
			if(StringUtil.notNull(dealerName))
				con.append("and B.DEALER_NAME LIKE '%").append(dealerName).append("%'");
			
			List<Map<String,Object>> lists = dao.getServiceCarExl(con.toString());
			System.out.println(lists.size());
			
			List<List<Object>> list = new LinkedList<List<Object>>();
			List<Object> titleList = new LinkedList<Object>();
			titleList.add("序号");
			//经销商名称、经销商联系人、经销商电话、经销商传真、申请购买车系、申请购买车型、申请车型市场价、状态
			titleList.add("经销商名称");
			titleList.add("经销商联系人");
			titleList.add("经销商电话");
			titleList.add("经销商传真");
			titleList.add("申请购买车系");
			titleList.add("申请购买车型");
			titleList.add("申请车型市场价");
			titleList.add("状态");
			
			list.add(titleList);
			for(int i=0;i<lists.size();i++){
				List<Object> dataList = new LinkedList<Object>();
				dataList.add(i+1);
				dataList.add(CommonUtils.checkNull(lists.get(i).get("DEALER_NAME")));
				dataList.add(CommonUtils.checkNull(lists.get(i).get("LINK_MAN")));
				dataList.add(CommonUtils.checkNull(lists.get(i).get("PHONE")));
				dataList.add(CommonUtils.checkNull(lists.get(i).get("FAX_NO")));
				dataList.add(CommonUtils.checkNull(lists.get(i).get("PARENT_GROUP_NAME")));
				dataList.add(CommonUtils.checkNull(lists.get(i).get("GROUP_NAME")));
				dataList.add(CommonUtils.checkNull(lists.get(i).get("SALE_AMOUNT")));
				dataList.add(CommonUtils.checkNull(lists.get(i).get("STATUS")));
				list.add(dataList);
			}
			
			os = act.getResponse().getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务车导出功能 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
