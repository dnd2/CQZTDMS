package com.infodms.dms.actions.sales.storage.sendmanage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storage.sendManage.RemovalStorageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtVehiclePdiCheckPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class PDICheck {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final RemovalStorageDao dao = RemovalStorageDao.getInstance();
	private final String PID_CHECK_INIT = "/jsp/sales/storage/sendmanage/removalStorage/pdiCheck.jsp";
	private final String PDI_HISTORY_RECORD = "/jsp/sales/storage/sendmanage/removalStorage/pdiHistoryRecord.jsp";
	
	/**
	 * @Title :
	 * @Description: PDI检查初始化
	 * @param :
	 * @return :
	 * @throws :
	 */
	public void pdiCheckInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly = MaterialGroupManagerDao
					.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);// 产地LIST
			act.setForword(PID_CHECK_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "PDI检查初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * PDI历史检查记录查看
	 */
	public void pdiCheckHistoryRecordQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.pdiCheckHistoryRecordQuery(vin,
					curPage, Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
			act.setForword(PDI_HISTORY_RECORD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "PDI历史检查记录查看");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * PDI检查通过
	 */
	public void pdiCheckPass() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String vins = CommonUtils.checkNull(request.getParamValue("vins"));
			String[] vinArr = vins.split(",");
			TmVehiclePO po = new TmVehiclePO();
			po.setIsPassStatus(1);
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date());
			
			for(int i = 0; i< vinArr.length; i++) {
				TmVehiclePO vehicle = new TmVehiclePO();
				vehicle.setVin(vinArr[i]);
				dao.update(vehicle, po);
			}
			
			act.setOutData("flag", true);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "PDI检查通过");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * PDI检查不通过
	 * 车厂物流在出库前可批量进行PDI检查，审核不通过时必须记录检查信息
	 */
	public void pdiCheckNoPass() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String vins = CommonUtils.checkNull(request.getParamValue("vins"));
			String record = CommonUtils.checkNull(request.getParamValue("record"));
			String[] vinArr = vins.split(",");
			for(int i = 0; i< vinArr.length; i++) {
				TtVehiclePdiCheckPO po = new TtVehiclePdiCheckPO();
				po.setPdiId(Long.parseLong(SequenceManager.getSequence(null)));
				po.setRecord(record);
				po.setVin(vinArr[i]);
				po.setCreateDate(new Date());
				po.setCreateBy(logonUser.getUserId());
				dao.insert(po);
				
				TmVehiclePO condition = new TmVehiclePO();
				condition.setVin(vinArr[i]);
				TmVehiclePO vpo = new TmVehiclePO();
				vpo.setIsPassStatus(0);
				dao.update(condition, vpo);
			}
			
			act.setOutData("flag", true);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "PDI检查通过");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * PDI检查查询
	 */
	public void pdiCheckQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String groupCode = request.getParamValue("groupCode"); // 物料组
			String materialCode = request.getParamValue("materialCode"); // 物料
			String yieldly = CommonUtils.checkNull(request
					.getParamValue("YIELDLY")); // 产地
			String dealerCode = CommonUtils.checkNull(request
					.getParamValue("dealerCode")); // 经销商ID
			String orderType = CommonUtils.checkNull(request
					.getParamValue("ORDER_TYPE")); // 订单类型
			String orderNo = CommonUtils.checkNull(request
					.getParamValue("ORDER_NO")); // 销售订单号
			String invoiceNo = CommonUtils.checkNull(request
					.getParamValue("INVOICE_NO")); // 发票号
			String logiName = CommonUtils.checkNull(request
					.getParamValue("LOGI_NAME")); // 物流公司
			String boStartDate = CommonUtils.checkNull(request
					.getParamValue("BO_STARTDATE")); // 组板日期开始
			String boEndDate = CommonUtils.checkNull(request
					.getParamValue("BO_ENDDATE")); // 组板日期结束
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); // 组板编号
			String allocaStartDate = CommonUtils.checkNull(request
					.getParamValue("ALLOCA_STARTDATE")); // 配车日期开始
			String allocaEndDate = CommonUtils.checkNull(request
					.getParamValue("ALLOCA_ENDDATE")); // 配车日期结束
			String VIN = CommonUtils.checkNull(request.getParamValue("VIN")); // vin
			String checkStatus = CommonUtils.checkNull(request.getParamValue("checkStatus")); //PDI检查状态
			/****************************** 页面查询字段end ***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("yieldly", yieldly);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderNo", orderNo);
			map.put("invoiceNo", invoiceNo);
			map.put("logiName", logiName);
			map.put("boStartDate", boStartDate);
			map.put("boEndDate", boEndDate);
			map.put("boNo", boNo);
			map.put("allocaStartDate", allocaStartDate);
			map.put("allocaEndDate", allocaEndDate);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("VIN", VIN);
			map.put("checkStatus", checkStatus);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.pdiCheckQuery(map,
					curPage, Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "PDI检查查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
