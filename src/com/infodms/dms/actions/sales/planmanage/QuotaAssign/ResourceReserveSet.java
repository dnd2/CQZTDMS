/**
 * @Title: ResourceReserveSet.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-18
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

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.planmanage.QuotaAssignDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TtVsResourceReservePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class ResourceReserveSet {
	private Logger logger = Logger.getLogger(ResourceReserveSet.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final QuotaAssignDao dao = QuotaAssignDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();

	private final String RESOURCE_RESERVE_SET_QUERY_URL = "/jsp/sales/planmanage/quotaassign/resourceReserveSetQuery.jsp";// 预留资源设定页面

	public void resourceReserveSetQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			// 车系列表
			List<Map<String, Object>> serieList = MaterialGroupManagerDao
					.getGroupDropDownBox(poseId.toString(), "2");
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(),
					oemCompanyId);

			// 获得配额提前下发月份参数
			TmBusinessParaPO paraPo = reportDao.getTmBusinessParaPO(
					Constant.QUTOA_AHEAD_ISSUE_MONTH_PARA, logonUser.getCompanyId());

			int year = Integer.parseInt(dateSet.getSetYear());
			int month = Integer.parseInt(dateSet.getSetMonth())
					+ Integer.parseInt(paraPo.getParaValue());
			if (month > 12) {
				year++;
				month = 1;
			}

			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("serieList", serieList);
			act.setForword(RESOURCE_RESERVE_SET_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预留资源设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void resourceReserveSetQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String year = CommonUtils.checkNull(request.getParamValue("year"));
			String month = CommonUtils
					.checkNull(request.getParamValue("month"));
			String serie = CommonUtils
					.checkNull(request.getParamValue("serie"));
			String companyId = logonUser.getCompanyId().toString();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("serie", serie);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getResourceReserveSetQueryList(map, curPage,
							Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预留资源设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void resourceReserveSetSave() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			int size = Integer.parseInt(CommonUtils.checkNull(request
					.getParamValue("size")));
			for (int i = 0; i < size; i++) {
				String reserveYear = request.getParamValue("reserveYear"
						+ (i + 1));
				String reserveMonth = request.getParamValue("reserveMonth"
						+ (i + 1));
				String reserveWeek = request.getParamValue("reserveWeek"
						+ (i + 1));
				String groupId = request.getParamValue("groupId" + (i + 1));
				String reserveAmt = request.getParamValue("reserveAmt"
						+ (i + 1));

				TtVsResourceReservePO po = new TtVsResourceReservePO();
				po.setCompanyId(logonUser.getCompanyId());
				po.setReserveYear(new Integer(reserveYear));
				po.setReserveWeek(new Integer(reserveWeek));
				po.setGroupId(new Long(groupId));
				po.setStatus(Constant.STATUS_ENABLE);

				List<PO> list = dao.select(po);
				// 判断是否已存在预留资源记录
				if (list.size() != 0) {
					po = (TtVsResourceReservePO) list.get(0);

					TtVsResourceReservePO condition = new TtVsResourceReservePO();
					condition.setReserveId(po.getReserveId());

					TtVsResourceReservePO value = new TtVsResourceReservePO();
					value.setLastReserveAmt(po.getReserveAmt());
					value.setReserveAmt(new Integer(reserveAmt));
					value.setUpdateBy(logonUser.getUserId());
					value.setUpdateDate(new Date());

					dao.update(condition, value);
				} else {
					po = new TtVsResourceReservePO();
					po.setReserveId(new Long(SequenceManager.getSequence("")));
					po.setCompanyId(logonUser.getCompanyId());
					po.setReserveYear(new Integer(reserveYear));
					po.setReserveMonth(new Integer(reserveMonth));
					po.setReserveWeek(new Integer(reserveWeek));
					po.setGroupId(new Long(groupId));
					po.setLastReserveAmt(new Integer(0));
					po.setReserveAmt(new Integer(reserveAmt));
					po.setStatus(Constant.STATUS_ENABLE);
					po.setCreateBy(logonUser.getUserId());
					po.setCreateDate(new Date());

					dao.insert(po);
				}
			}

			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预留资源设定");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
