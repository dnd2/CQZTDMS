/**
 * @Title: DealerQuotaQuery.java
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
public class DealerQuotaQuery {
	private Logger logger = Logger.getLogger(DealerQuotaQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final QuotaAssignDao dao = QuotaAssignDao.getInstance();
	private final OrderQueryDao queryDao = OrderQueryDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();

	private final String DEALER_QUOTA_QUERY_URL = "/jsp/sales/planmanage/quotaassign/dealerQuotaQuery.jsp";// 配额查询页面

	public void dealerQuotaQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<String> years = queryDao.getYearList();
			List<String> months = queryDao.getMonthList();
			List<String> weeks = queryDao.getWeekList();

			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			// 查看日期配置表中当天的记录
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(), oemCompanyId);
			
			List<Map<String, Object>> areaIds = MaterialGroupManagerDao.getDealerBusinessAll(logonUser.getPoseId().toString());

			act.setOutData("years", years);
			act.setOutData("months", months);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear() : "");
			act.setOutData("curMonth", dateSet != null ? dateSet.getSetMonth() : "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek() : "");
			act.setOutData("areaIds", areaIds) ;
			
			act.setForword(DEALER_QUOTA_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配额查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void dealerQuotaQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dateType = CommonUtils.checkNull(request.getParamValue("dateType"));
			String year1 = CommonUtils.checkNull(request.getParamValue("year1"));
			String year2 = CommonUtils.checkNull(request.getParamValue("year2"));
			String month = CommonUtils.checkNull(request.getParamValue("month"));
			String week = CommonUtils.checkNull(request.getParamValue("week"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String areaIds = CommonUtils.checkNull(request.getParamValue("areaIds")) ;
			String isPassZero = CommonUtils.checkNull(request.getParamValue("isPassZero")) ;
			
			String dealerId = logonUser.getDealerId();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			
			if("".equals(areaIds)) {
				areaIds = MaterialGroupManagerDao.getDealerBusinessIdStr(poseId.toString());
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dateType", dateType);
			map.put("year1", year1);
			map.put("year2", year2);
			map.put("month", month);
			map.put("week", week);
			map.put("groupCode", groupCode);
			map.put("dealerId", dealerId);
			map.put("areaIds", areaIds);
			map.put("isPassZero", "".equals(isPassZero) ? Constant.IF_TYPE_NO : isPassZero) ;

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getDealerQuotaQueryList(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配额查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 */
	public void dealerQuotaExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String dateType = CommonUtils.checkNull(request
					.getParamValue("dateType"));
			String year1 = CommonUtils
					.checkNull(request.getParamValue("year1"));
			String year2 = CommonUtils
					.checkNull(request.getParamValue("year2"));
			String month = CommonUtils
					.checkNull(request.getParamValue("month"));
			String week = CommonUtils.checkNull(request.getParamValue("week"));
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));
			String dealerId = logonUser.getDealerId();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getDealerBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dateType", dateType);
			map.put("year1", year1);
			map.put("year2", year2);
			map.put("month", month);
			map.put("week", week);
			map.put("groupCode", groupCode);
			map.put("dealerId", dealerId);
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
			listTemp.add("经销商代码");
			listTemp.add("经销商名称");
			listTemp.add("产品组代码");
			listTemp.add("产品组名称");
			listTemp.add("当前数量");
			list.add(listTemp);

			List<Map<String, Object>> results = dao
					.getDealerQuotaExportList(map);
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
