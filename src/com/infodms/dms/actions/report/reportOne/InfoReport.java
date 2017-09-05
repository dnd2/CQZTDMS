package com.infodms.dms.actions.report.reportOne;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.report.jcafterservicereport.SpecialCostReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infoservice.mvc.context.ActionContext;
/**
 * 整车销售报表（InfoReport）
 * @version 1.0 2012-09-06
 * @author 韩晓宇
 */
public class InfoReport {
	
	public Logger logger = Logger.getLogger(InfoReport.class);
	SpecialCostReportDao dao = new SpecialCostReportDao();
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	
	private final String BillDealerReportUrl = "/jsp/report/infoReport/billDealerTicket.jsp";
	private final String ActDealerReportUrl = "/jsp/report/infoReport/actDealerSales.jsp";
	private final String BillReportUrl = "/jsp/report/infoReport/billTicket.jsp";
	private final String ActReportUrl = "/jsp/report/infoReport/actSales.jsp";
	private final String partInStockDetailUrl = "/jsp/report/infoReport/repStoreStatu.jsp";
	private final String carInStockDetailUrl = "/jsp/report/infoReport/repCarStore.jsp";
	private final String dealerStockDetailUrl = "/jsp/report/infoReport/dealerStorage.jsp";
	private final String dealerCarDetailUrl = "/jsp/report/infoReport/dealerCarStorage.jsp";
	private final String FleetSalesUrl = "/jsp/report/infoReport/fleetSalesCvs.jsp";
	private final String FleetSalesLogUrl = "/jsp/report/infoReport/fleetSalesLog.jsp";
	private final String FleetPreparCvsUrl = "/jsp/report/infoReport/fleetPreparCvs.jsp";
	private final String FleetFollowUrl = "/jsp/report/infoReport/fleetFollow.jsp";
	private final String FleetContractUrl = "/jsp/report/infoReport/fleetContract.jsp";
	private final String ActDetailReportUrl = "/jsp/report/infoReport/actDetailSales.jsp";
	private final String ActDealerDetailReportUrl = "/jsp/report/infoReport/dealerActDetaliSales.jsp";
	private final String BillDetailReportUrl = "/jsp/report/infoReport/billDetailTicket.jsp";
	private final String BillDealerDetailReportUrl = "/jsp/report/infoReport/dealerBillDetaliTicket.jsp";
	private final String FleeActTjUrl = "/jsp/report/infoReport/fleetActTj.jsp";
	private final String initPutUrl = "/jsp/report/infoReport/fleetPutReport.jsp";
	private final String initSalesUrl = "/jsp/report/infoReport/fleetSalesReport.jsp";
	private final String FleetSyntheseReportInitUrl = "/jsp/report/infoReport/fleetSynthese.jsp";

	
	// 经销商启票汇总表10021002
	public void getDealerBillTicketReport() {
		try {
			StringBuffer str = new StringBuffer("");
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			String dealerId = logonUser.getDealerId();
			List<Map<String, Object>> dealerIds = dao
					.selectAllDealers(dealerId);
			for (int i = 0; i < dealerIds.size(); i++) {
				str.append(dealerIds.get(i).get("DEALER_ID"));
				str.append(",");
			}
			if (dao.selectDealers(dealerId).size() > 0) {
				str.append(dealerId);
				// Map map=dao.selectDealers(dealerId).get(0);
				// String a=map.get("DEALER_ID").toString();
				// List map1=dao.selectMyDealers(a);
				// //String b=map1.get("ROOT_DEALER_ID").toString();
				// if(map1!=null && map1.size()>0){
				// act.setOutData("dealerId", dealerId);
				// }else{
				act.setOutData("mydealerId", str);
				// }
			}
			act.setOutData("areaList", areaList);
			act.setForword(BillDealerReportUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,	ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 经销商销售汇总表10021002
	public void getDealerActSalesReport() {
		try {
			StringBuffer str = new StringBuffer("");
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			String dealerId = logonUser.getDealerId();

			List<Map<String, Object>> dealerIds = dao
					.selectAllDealers(dealerId);
			for (int i = 0; i < dealerIds.size(); i++) {
				str.append(dealerIds.get(i).get("DEALER_ID"));
				str.append(",");
			}
			if (dao.selectDealers(dealerId).size() > 0) {
				str.append(dealerId);
				// Map map=dao.selectDealers(dealerId).get(0);
				// String a=map.get("DEALER_ID").toString();
				// List map1=dao.selectMyDealers(a);
				// //String b=map1.get("ROOT_DEALER_ID").toString();
				// if(map1!=null && map1.size()>0){
				// act.setOutData("dealerId", dealerId);
				// }else{
				act.setOutData("mydealerId", str);
				// }
			}
			act.setOutData("areaList", areaList);
			act.setForword(ActDealerReportUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,	ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 启票汇总表10021001
	public void getBillTicketReport() {
		try {
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			String orgId = logonUser.getOrgId().toString();
			TmOrgPO po = new TmOrgPO();
			po.setOrgId(Long.parseLong(orgId));
			po.setDutyType(Constant.DUTY_TYPE_LARGEREGION);
			if (dao.select(po).size() == 1) {
				act.setOutData("orgId", orgId);
			}
			act.setOutData("areaList", areaList);
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(), date_.getMonth(), 01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			act.setForword(BillReportUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,	ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 零售汇总表10021001
	public void getActSalesReport() {
		try {
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			String orgId = logonUser.getOrgId().toString();
			TmOrgPO po = new TmOrgPO();
			po.setOrgId(Long.parseLong(orgId));
			po.setDutyType(Constant.DUTY_TYPE_LARGEREGION);
			if (dao.select(po).size() == 1) {
				act.setOutData("orgId", orgId);
			}
			act.setOutData("areaList", areaList);
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(), date_.getMonth(), 01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			act.setForword(ActReportUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,	ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 经销商库存状态表10021001
	public void getStorageCheck() {
		try {
			String orgId = logonUser.getOrgId().toString();
			if (orgId == Constant.OEM_ACTIVITIES || orgId.equals(Constant.OEM_ACTIVITIES)) {
				orgId = "1";
			}
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			act.setOutData("orgId", orgId);
			act.setOutData("areaList", areaList);
			act.setForword(partInStockDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	// 产品库存状态10021001
	public void dealerCarStorageInit() {
		try {
			String orgId = logonUser.getOrgId().toString();
			if (orgId == Constant.OEM_ACTIVITIES || orgId.equals(Constant.OEM_ACTIVITIES)) {
				orgId = "1";
			}
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			act.setOutData("orgId", orgId);
			act.setOutData("areaList", areaList);
			act.setForword(dealerCarDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	// 车型库存状态表10021001
	public void repCarStorageInit() {
		try {
			String orgId = logonUser.getOrgId().toString();
			if (orgId == Constant.OEM_ACTIVITIES || orgId.equals(Constant.OEM_ACTIVITIES)) {
				orgId = "1";
			}
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			act.setOutData("orgId", orgId);
			act.setOutData("areaList", areaList);
			act.setForword(carInStockDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	// 经销商库龄查询10021001
	public void dealerStorageInit() {
		try {
			String orgId = logonUser.getOrgId().toString();
			if (orgId == Constant.OEM_ACTIVITIES || orgId.equals(Constant.OEM_ACTIVITIES)) {
				orgId = "1";
			}
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			act.setOutData("orgId", orgId);
			act.setOutData("areaList", areaList);
			act.setForword(dealerStockDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	// 集团客户实销审核10021001
	public void getFleetSales() {
		try {
			List<Map<String, Object>> produceAddList = MaterialGroupManagerDao.getProduceAdd();
			act.setOutData("produceAddList", produceAddList);
			act.setForword(FleetSalesUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 集团客户实销10021001
	public void getFleetSalesLog() {
		try {
			List<Map<String, Object>> produceAddList = MaterialGroupManagerDao.getProduceAdd();
			act.setOutData("produceAddList", produceAddList);
			act.setForword(FleetSalesLogUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 集团客户报备10021001
	public void getFleetPrepar() {
		try {
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(), date_.getMonth(), 01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			act.setForword(FleetPreparCvsUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,	ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 集团客户报备跟进10021001
	public void getFleetFollow() {
		try {
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(), date_.getMonth(), 01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			act.setForword(FleetFollowUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,	ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 集团客户合同10021001
	public void getFleetContract() {
		try {
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(), date_.getMonth(), 01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			act.setForword(FleetContractUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,	ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 实销明细10021001
	public void getActDetaliSales() {
		try {
			StringBuffer str = new StringBuffer("");
			String dealerId = logonUser.getDealerId();
			List<Map<String, Object>> dealerIds = dao
					.selectAllDealers(dealerId);
			for (int i = 0; i < dealerIds.size(); i++) {
				str.append(dealerIds.get(i).get("DEALER_ID"));
				str.append(",");
			}
			if (dao.selectDealers(dealerId).size() > 0) {
				str.append(dealerId);
				act.setOutData("mydealerId", str);
			}

			String orgId = logonUser.getOrgId().toString();
			TmOrgPO po = new TmOrgPO();
			po.setOrgId(Long.parseLong(orgId));
			po.setDutyType(Constant.DUTY_TYPE_LARGEREGION);
			if (dao.select(po).size() == 1) {
				act.setOutData("orgId", orgId);
			}
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(), date_.getMonth(), 01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getPoseIdBusiness(poseId.toString());
			act.setOutData("areaList", areaList);
			act.setForword(ActDetailReportUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,	ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 经销商实销明细10021002
	public void getDealerActDetaliSales() {

		Date date_ = new Date(System.currentTimeMillis());
		Date date1 = new Date(date_.getYear(), date_.getMonth(), 01);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(date_);
		String date2 = dateFormat.format(date1);
		act.setOutData("date", date);
		act.setOutData("date2", date2);

		StringBuffer str = new StringBuffer("");
		String dealerId = logonUser.getDealerId();
		List<Map<String, Object>> dealerIds = dao.selectAllDealers(dealerId);
		for (int i = 0; i < dealerIds.size(); i++) {
			str.append(dealerIds.get(i).get("DEALER_ID"));
			str.append(",");
		}
		if (dao.selectDealers(dealerId).size() > 0) {
			str.append(dealerId);
			act.setOutData("mydealerId", str);
		}
		Long poseId = logonUser.getPoseId();
		List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
		act.setOutData("areaList", areaList);
		act.setForword(ActDealerDetailReportUrl);
	}

	// 启票明细10021001
	public void getBillDetaliTicket() {
		try {

			String orgId = logonUser.getOrgId().toString();
			TmOrgPO po = new TmOrgPO();
			po.setOrgId(Long.parseLong(orgId));
			po.setDutyType(Constant.DUTY_TYPE_LARGEREGION);
			if (dao.select(po).size() == 1) {
				act.setOutData("orgId", orgId);
			}
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(), date_.getMonth(), 01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			act.setOutData("areaList", areaList);
			act.setForword(BillDetailReportUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 经销商启票明细10021002
	public void getDealerBillDetaliTicket() {

		Date date_ = new Date(System.currentTimeMillis());
		Date date1 = new Date(date_.getYear(), date_.getMonth(), 01);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(date_);
		String date2 = dateFormat.format(date1);
		act.setOutData("date", date);
		act.setOutData("date2", date2);

		StringBuffer str = new StringBuffer("");
		String dealerId = logonUser.getDealerId();
		List<Map<String, Object>> dealerIds = dao.selectAllDealers(dealerId);
		for (int i = 0; i < dealerIds.size(); i++) {
			str.append(dealerIds.get(i).get("DEALER_ID"));
			str.append(",");
		}
		if (dao.selectDealers(dealerId).size() > 0) {
			str.append(dealerId);
			act.setOutData("mydealerId", str);
		}
		Long poseId = logonUser.getPoseId();
		List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
		act.setOutData("areaList", areaList);
		act.setForword(BillDealerDetailReportUrl);
	}

	// 集团客户DMS实销信息统计表10021001
	public void getFleetActTj() {
		try {
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			act.setOutData("areaList", areaList);
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(), date_.getMonth(), 01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			act.setForword(FleeActTjUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 大客户信息报备汇总表10021001
	public void initPutReport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initPutUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "大客户提报报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 大客户信息实销汇总表10021001
	public void initSalesReport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initSalesUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "大客户实销报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户综合查询
	 * 2012-04-09
	 * HXY
	 * */
	public void getFleetSynthese() {
		try {
			act.setForword(FleetSyntheseReportInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
