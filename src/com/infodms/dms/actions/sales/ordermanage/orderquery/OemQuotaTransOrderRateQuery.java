/**
 * @Title: OemQuotaTransOrderRateQuery.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-10
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.ordermanage.orderquery;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.orgmng.GetOrgIdsOrDealerIdsDAO;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class OemQuotaTransOrderRateQuery {
	private Logger logger = Logger.getLogger(OemQuotaTransOrderRateQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final OrderQueryDao dao = OrderQueryDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();

	private final String OEM_QUOTA_TRANS_ORDER_RATE_QUERY_URL = "/jsp/sales/ordermanage/orderquery/oemQuotaTransOrderRateQuery.jsp";// 配额常规订单转化率查询页面

	public void oemQuotaTransOrderRateQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			String dutyType = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			List<String> years = dao.getYearList();
			List<String> weeks = dao.getWeekList();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getPoseIdBusiness(poseId.toString());
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(), oemCompanyId);
			
			List<Map<String, Object>> orgList = OrderDeliveryDao.getInstance().getOemOrg(dutyType, Long.parseLong(orgId)) ;

			act.setOutData("orgId", dutyType.equals(Constant.DUTY_TYPE_DEPT.toString()) ? parentOrgId : orgId);
			act.setOutData("orgList", orgList);
			act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear() : "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek() : "");
			act.setOutData("areaList", areaList);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(OEM_QUOTA_TRANS_ORDER_RATE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配额常规订单转化率查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 配额常规订单转化率查询(总部)-车型汇总查询
	 * */
	public void oemQuotaTransOrderRateTotalQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderYear = CommonUtils.checkNull(request.getParamValue("orderYear"));
			String orderWeek = CommonUtils.checkNull(request.getParamValue("orderWeek"));
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String endWeek = CommonUtils.checkNull(request.getParamValue("endWeek"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")); //新增物料组过滤条件
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String regionId = CommonUtils.checkNull(request.getParamValue("downtown"));		// 省份
			String pageOrgId = CommonUtils.checkNull(request.getParamValue("orgId"));		// 事业部
			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));


			
			if(!"".equals(pageOrgId)) {
				dutyType = Constant.DUTY_TYPE_LARGEREGION.toString() ;
				orgId = pageOrgId ;
			}
			
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(orgId, parentOrgId, dutyType, "TD");
			String companyId = logonUser.getCompanyId().toString();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("endYear", endYear);
			map.put("endWeek", this.getComWeek(endWeek));
			map.put("orderYear", orderYear);
			map.put("orderWeek", this.getComWeek(orderWeek));
			map.put("areaId", areaId);
			map.put("dealerCode", dealerCode);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("regionId", regionId) ;
			map.put("groupCode", groupCode);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = null ;
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				ps = dao.getOemQuotaTransOrderRateTotalQueryList(map, curPage,Integer.parseInt(pageSize));
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				ps = dao.getOemQuotaTransOrderRateTotalQueryListCVS(map, curPage,Integer.parseInt(pageSize));
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			 
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配额常规订单转化率查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void oemQuotaTransOrderRateDetailQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderYear = CommonUtils.checkNull(request.getParamValue("orderYear"));
			String orderWeek = CommonUtils.checkNull(request.getParamValue("orderWeek"));
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String endWeek = CommonUtils.checkNull(request.getParamValue("endWeek"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String regionId = CommonUtils.checkNull(request.getParamValue("downtown"));		// 省份
			String pageOrgId = CommonUtils.checkNull(request.getParamValue("orgId"));		// 事业部
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")); //新增物料组过滤条件
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			if(!"".equals(pageOrgId)) {
				dutyType = Constant.DUTY_TYPE_LARGEREGION.toString() ;
				orgId = pageOrgId ;
			}
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(orgId, parentOrgId, dutyType, "TD");
			String companyId = logonUser.getCompanyId().toString();

            String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));


			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("endYear", endYear);
			map.put("endWeek", this.getComWeek(endWeek));
			map.put("orderYear", orderYear);
			map.put("orderWeek", this.getComWeek(orderWeek));
			map.put("areaId", areaId);
			map.put("dealerCode", dealerCode);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("regionId", regionId) ;
			map.put("groupCode", groupCode);

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			PageResult<Map<String, Object>> ps = null ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				ps = dao.getOemQuotaTransOrderRateDetailQueryList(map, curPage,Integer.parseInt(pageSize));
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				ps = dao.getOemQuotaTransOrderRateDetailQueryListCVS(map, curPage, Integer.parseInt(pageSize));
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配额常规订单转化率查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public String getComWeek(String week) {
		if(week.length() == 1) {
			week = "0" + week ;
		}
		
		return week ;
	}

	/**
	 * 
	 */
	public void oemQuotaTransOrderRateTotalExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String orderYear = CommonUtils.checkNull(request
					.getParamValue("orderYear"));
			String orderWeek = CommonUtils.checkNull(request
					.getParamValue("orderWeek"));
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String dealerCode = CommonUtils.checkNull(request
					.getParamValue("dealerCode"));
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String regionId = CommonUtils.checkNull(request.getParamValue("downtown"));		// 省份
			String pageOrgId = CommonUtils.checkNull(request.getParamValue("orgId"));		// 事业部
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String endWeek = CommonUtils.checkNull(request.getParamValue("endWeek"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")); //新增物料组过滤条件
			
			if(!"".equals(pageOrgId)) {
				dutyType = Constant.DUTY_TYPE_LARGEREGION.toString() ;
				orgId = pageOrgId ;
			}
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(
					orgId, parentOrgId, dutyType, "TD");
			String companyId = logonUser.getCompanyId().toString();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("endYear", endYear);
			map.put("endWeek", this.getComWeek(endWeek));
			map.put("orderYear", orderYear);
			map.put("orderWeek", this.getComWeek(orderWeek));
			map.put("areaId", areaId);
			map.put("dealerCode", dealerCode);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("regionId", regionId) ;
			map.put("groupCode", groupCode);

			// 导出的文件名
			String fileName = "配额订单转化率汇总.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("周次");
			listTemp.add("车系名称");
			listTemp.add("车型名称");
			listTemp.add("配置代码");
			listTemp.add("配置名称");
			listTemp.add("配额数量");
			listTemp.add("订单最小提报量");
			listTemp.add("提报数量");
			listTemp.add("发运申请数量");
			listTemp.add("保留资源数量");
			listTemp.add("开票数量");
			listTemp.add("差异数量");
			listTemp.add("提报率");
			listTemp.add("执行率");
			listTemp.add("常规订单占比");
			list.add(listTemp);

			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			List<Map<String, Object>> results = null;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				results = dao.getOemQuotaTransOrderRateTotalExportList(map);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				results = dao.getOemQuotaTransOrderRateTotalExportListCVS(map);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("QUOTA_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("SERIES_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MODEL_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("QUOTA_AMT")));
				listTemp.add(CommonUtils.checkNull(record.get("MIN_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("YTB")));
				listTemp.add(CommonUtils.checkNull(record.get("REQ_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("RESERVE_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("CY")));
				listTemp.add(CommonUtils.checkNull(record.get("WCL")));
				listTemp.add(CommonUtils.checkNull(record.get("ZXL")));
				listTemp.add(CommonUtils.checkNull(record.get("ZBL")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配额常规订单转化率查询");
			logger.error(logonUser, e1);
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

	/**
	 * 
	 */
	public void oemQuotaTransOrderRateDetailExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String orderYear = CommonUtils.checkNull(request
					.getParamValue("orderYear"));
			String orderWeek = CommonUtils.checkNull(request
					.getParamValue("orderWeek"));
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String dealerCode = CommonUtils.checkNull(request
					.getParamValue("dealerCode"));
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String regionId = CommonUtils.checkNull(request.getParamValue("downtown"));		// 省份
			String pageOrgId = CommonUtils.checkNull(request.getParamValue("orgId"));		// 事业部
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String endWeek = CommonUtils.checkNull(request.getParamValue("endWeek"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")); //新增物料组过滤条件
			if(!"".equals(pageOrgId)) {
				dutyType = Constant.DUTY_TYPE_LARGEREGION.toString() ;
				orgId = pageOrgId ;
			}
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(
					orgId, parentOrgId, dutyType, "TD");
			String companyId = logonUser.getCompanyId().toString();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("endYear", endYear);
			map.put("endWeek", this.getComWeek(endWeek));
			map.put("orderYear", orderYear);
			map.put("orderWeek", this.getComWeek(orderWeek));
			map.put("areaId", areaId);
			map.put("dealerCode", dealerCode);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("regionId", regionId) ;
			map.put("groupCode", groupCode);

			// 导出的文件名
			String fileName = "配额订单转化率明细.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("周次");
			listTemp.add("大区名称");
			listTemp.add("省份");
			listTemp.add("经销商代码");
			listTemp.add("经销商名称");
			listTemp.add("配额数量");
			listTemp.add("订单最小提报量");
			listTemp.add("提报数量");
			listTemp.add("发运申请数量") ;
			listTemp.add("保留资源数量") ;
			listTemp.add("常规订单开票数量") ;
			listTemp.add("补充订单开票数量") ;
			listTemp.add("差异数量");
			listTemp.add("提报率");
			listTemp.add("执行率");
			listTemp.add("常规订单占比");
			list.add(listTemp);
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			List<Map<String, Object>> results = null;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				results = dao.getOemQuotaTransOrderRateDetailExportList(map);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				results = dao.getOemQuotaTransOrderRateDetailExportListCVS(map);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}

			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("QUOTA_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("REGION_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("QUOTA_AMT")));
				listTemp.add(CommonUtils.checkNull(record.get("MIN_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("YTB")));
				listTemp.add(CommonUtils.checkNull(record.get("REQ_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("RESERVE_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("B_DELIVERY_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("CY")));
				listTemp.add(CommonUtils.checkNull(record.get("WCL")));
				listTemp.add(CommonUtils.checkNull(record.get("ZXL")));
				listTemp.add(CommonUtils.checkNull(record.get("ZBL")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配额常规订单转化率查询");
			logger.error(logonUser, e1);
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
