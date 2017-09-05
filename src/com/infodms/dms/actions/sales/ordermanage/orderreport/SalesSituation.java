/**
 * @Title: UrgentOrderReport.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-27
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.ordermanage.orderreport;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.sales.ordermanage.orderreport.SalesSituationDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVsAddressPO;
import com.infodms.dms.po.TtVsSalesSituationDtlPO;
import com.infodms.dms.po.TtVsSalesSituationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class SalesSituation {
	private Logger logger = Logger.getLogger(SalesSituation.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final SalesSituationDao dao = SalesSituationDao.getInstance();

	private final String URGENT_ORDER_REPORT_QUERY_URL = "/jsp/sales/ordermanage/orderreport/salesSituationInit.jsp";// 预售车辆提报查询页面
	private final String URGENT_ORDER_REPORT_QUERY_ORG_URL = "/jsp/sales/ordermanage/orderreport/salesSituationInitOrg.jsp";// 预售车辆总部查询页面

	private final String URGENT_ORDER_REPORT_ADD_URL = "/jsp/sales/ordermanage/orderreport/salesSituationAdd.jsp";// 预售车辆新增
	private final String URGENT_ORDER_REPORT_MOD_URL = "/jsp/sales/ordermanage/orderreport/salesSituationMod.jsp";// 预售车辆修改页面
	private final String URGENT_ORDER_REPORT_INFO_URL = "/jsp/sales/ordermanage/orderreport/salesSituationQueryInfo.jsp";// 预售车辆查看页面

	public void salesSituationInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			act.setForword(URGENT_ORDER_REPORT_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void salesSituationInitOrg() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			act.setForword(URGENT_ORDER_REPORT_QUERY_ORG_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void salesSituationQueryOrg() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String dutyType = logonUser.getDutyType() ;

			PageResult<Map<String, Object>> ps = dao.getSalesSituationListOrg(dutyType,logonUser.getOrgId().toString(), startDate, endDate, curPage, Constant.PAGE_SIZE);

			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商销售当日情况");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void salesSituationQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));

			PageResult<Map<String, Object>> ps = dao.getSalesSituationList(logonUser.getDealerId(), startDate, endDate, curPage, Constant.PAGE_SIZE);

			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商销售当日情况");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void salesSituationAddInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			act.setForword(URGENT_ORDER_REPORT_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void salesSituationAdd() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Long situationId = Long.parseLong(SequenceManager.getSequence(""));
			String dealerId = logonUser.getDealerId();
			String situationNo = "SS" + situationId;
			TtVsSalesSituationPO po = new TtVsSalesSituationPO();
			po.setDealerId(new Long(dealerId));
			po.setSituationId(situationId);
			po.setSituationNo(situationNo);
			po.setState(Constant.FORECAST_STATUS_UNCONFIRM);
			po.setStatus(Constant.STATUS_ENABLE);
			po.setUpdateBy(-1L);
			po.setCreateDate(new Date());
			po.setCreateBy(logonUser.getUserId());
			dao.insert(po);
			String[] materialIds = request.getParamValues("materialId");
			for (int i = 0; i < materialIds.length; i++) {
				TtVsSalesSituationDtlPO pod = new TtVsSalesSituationDtlPO();
				pod.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
				pod.setSituationId(situationId);
				pod.setUpdateBy(-1L);
				pod.setCreateDate(new Date());
				pod.setCreateBy(logonUser.getUserId());
				pod.setMaterialId(new Long(materialIds[i]));
				String ORDER_AMOUNT = CommonUtils.checkNull(request.getParamValue("ORDER_AMOUNT" + materialIds[i]));
				String RET_AMOUNT = CommonUtils.checkNull(request.getParamValue("RET_AMOUNT" + materialIds[i]));
				String POOR_AMOUNT = CommonUtils.checkNull(request.getParamValue("POOR_AMOUNT" + materialIds[i]));
				pod.setOrderAmount(new Long(ORDER_AMOUNT));
				pod.setRetAmount(new Long(RET_AMOUNT));
				pod.setPoorAmount(new Long(POOR_AMOUNT));
				dao.insert(pod);
			}

			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void salesSituationModInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String situationId = CommonUtils.checkNull(request.getParamValue("situationId"));

			List list = dao.getSalesSituationModList(situationId);
			act.setOutData("list", list);
			act.setOutData("situationId", situationId);

			act.setForword(URGENT_ORDER_REPORT_MOD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void salesSituationQueryInfo() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String situationId = CommonUtils.checkNull(request.getParamValue("situationId"));

			List<Map<String,Object>> list = dao.getSalesSituationModList(situationId);
			act.setOutData("list", list);
			act.setOutData("situationId", situationId);
			//如果是非经销商获取经销商信息
			if(!Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())){
				//获取经销商信息
				String dealerId=list.get(0).get("DEALER_ID").toString();
				TmDealerPO td=new TmDealerPO();
				td.setDealerId(new Long(dealerId));
				td=(TmDealerPO) dao.select(td).get(0);
				act.setOutData("dealer", td);
				TmVsAddressPO tvPO=new TmVsAddressPO();
				tvPO.setDealerId(new Long(dealerId));
				tvPO=(TmVsAddressPO) dao.select(tvPO).get(0);
				act.setOutData("address", tvPO);
			}
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(URGENT_ORDER_REPORT_INFO_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void salesSituationMod() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			Long situationId = Long.parseLong(CommonUtils.checkNull(request.getParamValue("situationId")));
			String[] materialIds = request.getParamValues("materialId");
			String materialIdStr = "";
			for (int i = 0; i < materialIds.length; i++) {

				TtVsSalesSituationDtlPO dtlPO = new TtVsSalesSituationDtlPO();
				dtlPO.setSituationId(situationId);
				dtlPO.setMaterialId(new Long(materialIds[i]));
				Iterator iterator = dao.select(dtlPO).iterator();

				if (iterator.hasNext()) {

					TtVsSalesSituationDtlPO po = new TtVsSalesSituationDtlPO();
					po.setUpdateDate(new Date());
					po.setUpdateBy(logonUser.getUserId());
					String ORDER_AMOUNT = CommonUtils.checkNull(request.getParamValue("ORDER_AMOUNT" + materialIds[i]));
					String RET_AMOUNT = CommonUtils.checkNull(request.getParamValue("RET_AMOUNT" + materialIds[i]));
					String POOR_AMOUNT = CommonUtils.checkNull(request.getParamValue("POOR_AMOUNT" + materialIds[i]));
					po.setOrderAmount(new Long(ORDER_AMOUNT));
					po.setRetAmount(new Long(RET_AMOUNT));
					po.setPoorAmount(new Long(POOR_AMOUNT));
					dao.update(dtlPO, po);

				} else {
					TtVsSalesSituationDtlPO po = new TtVsSalesSituationDtlPO();
					po.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
					po.setSituationId(situationId);
					po.setUpdateBy(-1L);
					po.setCreateDate(new Date());
					po.setCreateBy(logonUser.getUserId());
					po.setMaterialId(new Long(materialIds[i]));
					String ORDER_AMOUNT = CommonUtils.checkNull(request.getParamValue("ORDER_AMOUNT" + materialIds[i]));
					String RET_AMOUNT = CommonUtils.checkNull(request.getParamValue("RET_AMOUNT" + materialIds[i]));
					String POOR_AMOUNT = CommonUtils.checkNull(request.getParamValue("POOR_AMOUNT" + materialIds[i]));
					po.setOrderAmount(new Long(ORDER_AMOUNT));
					po.setRetAmount(new Long(RET_AMOUNT));
					po.setPoorAmount(new Long(POOR_AMOUNT));
					dao.insert(po);
				}
				if (i < materialIds.length - 1) {
					materialIdStr += materialIds[i] + ",";
				} else {
					materialIdStr += materialIds[i];
				}
			}
			StringBuffer sql = new StringBuffer();
			sql.append("DELETE TT_VS_SALES_SITUATION_DTL D\n");
			sql.append(" WHERE D.SITUATION_ID = '" + situationId + "'\n");
			sql.append("   AND D.MATERIAL_ID NOT IN (" + materialIdStr + ")");

			dao.delete(sql.toString(), null);

			act.setOutData("returnValue", 1);
			act.setForword(URGENT_ORDER_REPORT_MOD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void salesSituationDel() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));

			String situationId = CommonUtils.checkNull(request.getParamValue("situationId"));
			TtVsSalesSituationPO po = new TtVsSalesSituationPO();
			po.setSituationId(new Long(situationId));

			TtVsSalesSituationPO pov = new TtVsSalesSituationPO();
			pov.setStatus(Constant.STATUS_DISABLE);
			dao.update(po, pov);

			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void salesSituationSubmit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String situationId = CommonUtils.checkNull(request.getParamValue("situationId"));
			TtVsSalesSituationPO po = new TtVsSalesSituationPO();
			po.setSituationId(new Long(situationId));

			TtVsSalesSituationPO pov = new TtVsSalesSituationPO();
			pov.setState(Constant.FORECAST_STATUS_CONFIRM);
			dao.update(po, pov);

			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
