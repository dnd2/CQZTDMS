/**
 * @Title: ResourceReserveQuery.java
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import static com.infodms.dms.dao.sales.ordermanage.resourceQuery.ResourceQueryDAO.*;

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
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class ResourceReserveQuery {
	private Logger logger = Logger.getLogger(ResourceReserveSet.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final QuotaAssignDao dao = QuotaAssignDao.getInstance();
	private final OrderQueryDao queryDao = OrderQueryDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();

	private final String RESOURCE_RESERVE_QUERY_URL = "/jsp/sales/planmanage/quotaassign/resourceReserveQuery.jsp";// 预留资源查询页面

	public void resourceReserveQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> serieList = MaterialGroupManagerDao
					.getGroupDropDownBox(poseId.toString(), "2");
			List<String> years = queryDao.getYearList();
			List<String> months = queryDao.getMonthList();

			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(),
					oemCompanyId);

			/*
			 * int year = Integer.parseInt(dateSet.getSetYear()); int month =
			 * Integer.parseInt(dateSet.getSetMonth()) + 1; if (month > 12) {
			 * year++; month = 1; }
			 */

			act.setOutData("years", years);
			act.setOutData("months", months);
			act.setOutData("year", dateSet != null ? dateSet.getSetYear() : "");
			act.setOutData("month", dateSet != null ? dateSet.getSetMonth()
					: "");
			act.setOutData("serieList", serieList);
			act.setForword(RESOURCE_RESERVE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预留资源查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void resourceReserveQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String year = CommonUtils.checkNull(request.getParamValue("year"));
			String month = CommonUtils
					.checkNull(request.getParamValue("month"));
			String serie = CommonUtils
					.checkNull(request.getParamValue("serie"));
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("serie", serie);
			map.put("companyId", companyId);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getResourceReserveQueryList(map, curPage,
							Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预留资源查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public Long insLog(String reqId, Long userId) {
		return insertLog(reqId, userId) ;
	}
	
	public Long insDtlLog(Long logId, String reqDtlId, String batchNo, String newAmount, String oldAmount,String materialId, Long userId) {
		return insertDtlLog(logId, reqDtlId, batchNo, newAmount, oldAmount, materialId, userId) ;
	}
}