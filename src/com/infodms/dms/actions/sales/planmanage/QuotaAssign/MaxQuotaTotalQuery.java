/**
 * @Title: MaxQuotaTotalQuery.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-21
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
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.planmanage.QuotaAssignDao;
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
public class MaxQuotaTotalQuery {
	private Logger logger = Logger.getLogger(MaxQuotaTotalQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final QuotaAssignDao dao = QuotaAssignDao.getInstance();
	private final OrderQueryDao queryDao = OrderQueryDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();

	private final String MAX_QUOTA_TOTAL_QUERY_URL = "/jsp/sales/planmanage/quotaassign/maxQuotaTotalQuery.jsp";// 最大配额总量查询页面

	public void maxQuotaTotalQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dutyType = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();

			List<String> years = queryDao.getYearList();
			List<String> months = queryDao.getMonthList();

			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(),
					oemCompanyId);

			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getPoseIdBusiness(poseId.toString());

			act.setOutData("years", years);
			act.setOutData("months", months);
			act.setOutData("areaList", areaList);
			act.setOutData("year", dateSet != null ? dateSet.getSetYear() : "");
			act.setOutData("month", dateSet != null ? dateSet.getSetMonth()
					: "");
			act.setOutData("orgId", dutyType.equals(Constant.DUTY_TYPE_DEPT
					.toString()) ? parentOrgId : orgId);
			act.setForword(MAX_QUOTA_TOTAL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "最大配额总量查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void maxQuotaTotalQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String year = CommonUtils.checkNull(request.getParamValue("year"));
			String month = CommonUtils
					.checkNull(request.getParamValue("month"));
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));
			String companyId = logonUser.getCompanyId().toString();
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("groupCode", groupCode);
			map.put("companyId", companyId);
			map.put("areaId", areaId);
			map.put("areaIds", areaIds);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao.getMaxQuotaTotalQueryList(
					map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "最大配额总量查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void getWeekList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String year = CommonUtils.checkNull(request.getParamValue("year"));
			String month = CommonUtils
					.checkNull(request.getParamValue("month"));
			String companyId = logonUser.getCompanyId().toString();
			List<Map<String, Object>> list = dao.getWeekList(year, month,
					companyId);
			act.setOutData("list", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "最大配额总量查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void maxQuotaTotalExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String year = CommonUtils.checkNull(request.getParamValue("year"));
			String month = CommonUtils
					.checkNull(request.getParamValue("month"));
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));
			String companyId = logonUser.getCompanyId().toString();
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("groupCode", groupCode);
			map.put("companyId", companyId);
			map.put("areaId", areaId);
			map.put("areaIds", areaIds);

			List<Map<String, Object>> dateList = dao.getWeekList(year, month,
					companyId);

			// 导出的文件名
			String fileName = "最大配额总量.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("配置代码");
			listTemp.add("配置名称");
			listTemp.add("生产订单(P)");
			listTemp.add("预留资源(R)");
			listTemp.add("最大配额(P-R)");
			for (int i = 0; i < dateList.size(); i++) {
				Map<String, Object> mapGet = (Map<String, Object>) dateList
						.get(i);
				listTemp.add((String) mapGet.get("SET_WEEK") + "周");
			}
			list.add(listTemp);

			List<Map<String, Object>> results = dao
					.getMaxQuotaTotalExportList(map);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("PLAN_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("RESERVE_AMT")));
				listTemp.add(CommonUtils.checkNull(record.get("QUOTA_AMT")));
				for (int j = 0; j < dateList.size(); j++) {
					listTemp.add(CommonUtils.checkNull(record.get("W" + j)));
				}
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "最大配额总量查询");
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
