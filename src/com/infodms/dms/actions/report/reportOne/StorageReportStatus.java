package com.infodms.dms.actions.report.reportOne;


import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.repairOrder.RoMaintainMain;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;


public class StorageReportStatus {
	private Logger logger = Logger.getLogger(RoMaintainMain.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(
			Constant.LOGON_USER);
	private final String partInStockDetailUrl   = "/jsp/report/repStoreStatu.jsp";
	private final String carInStockDetailUrl   = "/jsp/report/repCarStore.jsp";
	private final String dealerStockDetailUrl   = "/jsp/report/dealerStorage.jsp";
	private final String dealerCarDetailUrl   = "/jsp/report/dealerCarStorage.jsp";

	/*
	 * 根据月份搜索
	 */
	public void getStorageCheck(){
		try {
			String orgId=logonUser.getOrgId().toString();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())){
				orgId="1";
			}
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			act.setOutData("orgId", orgId);
			act.setOutData("areaList", areaList);
			act.setForword(partInStockDetailUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
	/*
	 * 根据月份搜索
	 */
	public void repCarStorageInit(){
		try {
			String orgId=logonUser.getOrgId().toString();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())){
				orgId="1";
			}
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			act.setOutData("orgId", orgId);
			act.setOutData("areaList", areaList);
			act.setForword(carInStockDetailUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
	/*
	 * 根据月份搜索
	 */
	public void dealerStorageInit(){
		try {
			String orgId=logonUser.getOrgId().toString();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())){
				orgId="1";
			}
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			act.setOutData("orgId", orgId);
			act.setOutData("areaList", areaList);
			act.setForword(dealerStockDetailUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
	/*
	 * 根据月份搜索
	 */
	public void dealerCarStorageInit(){
		try {
			String orgId=logonUser.getOrgId().toString();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())){
				orgId="1";
			}
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			act.setOutData("orgId", orgId);
			act.setOutData("areaList", areaList);
			act.setForword(dealerCarDetailUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
}
