/**
 * @Title: OemQuotaQuery.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-22
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.planmanage.QuotaAssign;

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
import com.infodms.dms.dao.orgmng.GetOrgIdsOrDealerIdsDAO;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.planmanage.QuotaAssignDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class OemQuotaQuery {
	private Logger logger = Logger.getLogger(OemQuotaQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final QuotaAssignDao dao = QuotaAssignDao.getInstance();
	private final OrderQueryDao queryDao = OrderQueryDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();

	private final String OEM_QUOTA_QUERY_URL = "/jsp/sales/planmanage/quotaassign/oemQuotaQuery.jsp";// 配额查询页面

	public void oemQuotaQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			String dutyType = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			List<String> years = queryDao.getYearList();
			List<String> months = queryDao.getMonthList();
			List<String> weeks = queryDao.getWeekList();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getPoseIdBusiness(poseId.toString());
			
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			// 查看日期配置表中当天的记录
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(), oemCompanyId);

			act.setOutData("orgId", dutyType.equals(Constant.DUTY_TYPE_DEPT
					.toString()) ? parentOrgId : orgId);
			act.setOutData("years", years);
			act.setOutData("months", months);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear() : "");
			act.setOutData("curMonth", dateSet != null ? dateSet.getSetMonth() : "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek() : "");
			act.setOutData("areaList", areaList);
			act.setOutData("dutyType", dutyType);
			act.setForword(OEM_QUOTA_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配额查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void oemQuotaTotalQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String quotaType = CommonUtils.checkNull(request
					.getParamValue("quotaType"));
			String dateType = CommonUtils.checkNull(request
					.getParamValue("dateType"));
			String year1 = CommonUtils
					.checkNull(request.getParamValue("year1"));
			String year2 = CommonUtils
					.checkNull(request.getParamValue("year2"));
			String month = CommonUtils
					.checkNull(request.getParamValue("month"));
			String week = CommonUtils.checkNull(request.getParamValue("week"));
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));
			String orgCode = CommonUtils.checkNull(request
					.getParamValue("orgCode"));
			String dealerCode = CommonUtils.checkNull(request
					.getParamValue("dealerCode"));
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String orgSql = GetOrgIdsOrDealerIdsDAO.getOrgIdsForOrder(orgId,
					parentOrgId, dutyType, "TOR");
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrderNew(
					orgId, parentOrgId, dutyType, "TDA");
			String companyId = logonUser.getCompanyId().toString();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("quotaType", quotaType);
			map.put("dateType", dateType);
			map.put("year1", year1);
			map.put("year2", year2);
			map.put("month", month);
			map.put("week", week);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("orgCode", orgCode);
			map.put("dealerCode", dealerCode);
			map.put("orgSql", orgSql);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("orgId", logonUser.getOrgId().toString());

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao.getOemQuotaTotalQueryList(
					map, curPage, Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配额查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void oemQuotaDetailQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String quotaType = CommonUtils.checkNull(request.getParamValue("quotaType"));
			String dateType = CommonUtils.checkNull(request.getParamValue("dateType"));
			String year1 = CommonUtils.checkNull(request.getParamValue("year1"));
			String year2 = CommonUtils.checkNull(request.getParamValue("year2"));
			String month = CommonUtils.checkNull(request.getParamValue("month"));
			String week = CommonUtils.checkNull(request.getParamValue("week"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String orgSql = GetOrgIdsOrDealerIdsDAO.getOrgIdsForOrder(orgId, parentOrgId, dutyType, "TOR");
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrderNew(orgId, parentOrgId, dutyType, "TDA");
			String companyId = logonUser.getCompanyId().toString();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getPoseIdBusinessIdStr(poseId.toString());

            String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));


			Map<String, Object> map = new HashMap<String, Object>();
			map.put("quotaType", quotaType);
			map.put("dateType", dateType);
			map.put("year1", year1);
			map.put("year2", year2);
			map.put("month", month);
			map.put("week", week);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("orgCode", orgCode);
			map.put("orgId", orgId);
			map.put("dealerCode", dealerCode);
			map.put("orgSql", orgSql);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOemQuotaDetailQueryList(map, curPage,Integer.parseInt(pageSize));
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配额查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 汇总下载
	 */
	public void oemQuotaTotalExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String quotaType = CommonUtils.checkNull(request
					.getParamValue("quotaType"));
			String dateType = CommonUtils.checkNull(request
					.getParamValue("dateType"));
			String year1 = CommonUtils
					.checkNull(request.getParamValue("year1"));
			String year2 = CommonUtils
					.checkNull(request.getParamValue("year2"));
			String month = CommonUtils
					.checkNull(request.getParamValue("month"));
			String week = CommonUtils.checkNull(request.getParamValue("week"));
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));
			String orgCode = CommonUtils.checkNull(request
					.getParamValue("orgCode"));
			String dealerCode = CommonUtils.checkNull(request
					.getParamValue("dealerCode"));
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String orgSql = GetOrgIdsOrDealerIdsDAO.getOrgIdsForOrder(orgId,
					parentOrgId, dutyType, "TOR");
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrderNew(
					orgId, parentOrgId, dutyType, "TDA");
			String companyId = logonUser.getCompanyId().toString();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("quotaType", quotaType);
			map.put("dateType", dateType);
			map.put("year1", year1);
			map.put("year2", year2);
			map.put("month", month);
			map.put("week", week);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("orgCode", orgCode);
			map.put("dealerCode", dealerCode);
			map.put("orgSql", orgSql);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);

			// 导出的文件名
			String fileName = "配额汇总.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("产品组代码");
			listTemp.add("产品组名称");
			listTemp.add("车型代码");
			listTemp.add("车型名称");
			listTemp.add("配额数量");
			list.add(listTemp);

			List<Map<String, Object>> results = dao
					.getOemQuotaTotalExportList(map);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MODEL_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("MODEL_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("QUOTA_AMT")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配额查询");
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
	 * 明细下载
	 */
	public void oemQuotaDetailExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String quotaType = CommonUtils.checkNull(request
					.getParamValue("quotaType"));
			String dateType = CommonUtils.checkNull(request
					.getParamValue("dateType"));
			String year1 = CommonUtils
					.checkNull(request.getParamValue("year1"));
			String year2 = CommonUtils
					.checkNull(request.getParamValue("year2"));
			String month = CommonUtils
					.checkNull(request.getParamValue("month"));
			String week = CommonUtils.checkNull(request.getParamValue("week"));
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));
			String orgCode = CommonUtils.checkNull(request
					.getParamValue("orgCode"));
			String dealerCode = CommonUtils.checkNull(request
					.getParamValue("dealerCode"));
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String orgSql = GetOrgIdsOrDealerIdsDAO.getOrgIdsForOrder(orgId,
					parentOrgId, dutyType, "TOR");
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrderNew(
					orgId, parentOrgId, dutyType, "TDA");
			String companyId = logonUser.getCompanyId().toString();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("quotaType", quotaType);
			map.put("dateType", dateType);
			map.put("year1", year1);
			map.put("year2", year2);
			map.put("month", month);
			map.put("week", week);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("orgCode", orgCode);
			map.put("dealerCode", dealerCode);
			map.put("orgSql", orgSql);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);

			// 导出的文件名
			String fileName = "配额明细.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("配额周度");
			if (quotaType.equals(Constant.QUOTA_TYPE_01.toString())) {
				listTemp.add("区域代码");
				listTemp.add("区域名称");
			} else if(quotaType.equals(Constant.QUOTA_TYPE_03.toString())){
				listTemp.add("车厂代码");
				listTemp.add("车厂名称");
			}else {
				listTemp.add("经销商代码");
				listTemp.add("经销商名称");
			}
			listTemp.add("产品组代码");
			listTemp.add("产品组名称");
			listTemp.add("配额数量");
			list.add(listTemp);

			List<Map<String, Object>> results = dao
					.getOemQuotaDetailExportList(map);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("QUOTA_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORG_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("QUOTA_AMT")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配额查询");
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
