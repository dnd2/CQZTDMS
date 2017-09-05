/**
 * 
 */
package com.infodms.dms.actions.sales.storageManage;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.storageManage.ReturnVehicleReqDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.CuxErpDeliverPO;
import com.infodms.dms.po.CuxErpReturnVinPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TmVsAddressPO;
import com.infodms.dms.po.TtProcInterfaceMonitorPO;
import com.infodms.dms.po.TtReturnVehicleHeadPO;
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infodms.dms.po.TtVsReturnVehicleReqPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 退车Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-9-6
 */
public class ReturnVehicleReq {
	public Logger logger = Logger.getLogger(ReturnVehicleReq.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private AclUserBean logonUser;
	private ResponseWrapper response = act.getResponse();
	private ReturnVehicleReqDao dao = ReturnVehicleReqDao.getInstance();
	// private final String url = "/jsp/sales/storageManage/returnVehicleReq.jsp";
	//退车申请界面
	private final String RETURN_VEHICLE_REQ_URL = "/jsp/sales/storageManage/returnVehicleReqNew.jsp";
	// 添加车辆界面
	private final String CHOOSE_VEHICLE_URL = "/jsp/sales/storageManage/chooseVehicleForReturn.jsp";
	//经销商退车查询界面
	private final String queryUrl = "/jsp/sales/storageManage/queryReturnVehicleReq.jsp";
	//退车区域审核界面
	private final String applyUrl = "/jsp/sales/storageManage/applyReturnVehicleReq.jsp";
	//销售部审核界面
	private final String applyUrlSale = "/jsp/sales/storageManage/applyReturnVehicleReqSales.jsp";
	//储运部审核界面
	private final String applyUrlStorage = "/jsp/sales/storageManage/applyReturnVehicleReqStorage.jsp";
	//财务审核界面
	private final String applyUrlFinfinal = "/jsp/sales/storageManage/applyReturnVehicleReqFinfinal.jsp";
	//退车区域明细审核
	private final String RETURN_DLT_CHECK_INIT = "/jsp/sales/storageManage/returnDtlCheck.jsp";
	//退车销售明细审核
	private final String RETURN_DLT_CHECK_INIT_SALES = "/jsp/sales/storageManage/returnDtlCheckSales.jsp";
	//退车储运明细审核
	private final String RETURN_DLT_CHECK_INIT_STORAGE = "/jsp/sales/storageManage/returnDtlCheckStorage.jsp";
	//退车储运入库
	private final String RETURN_DLT_CHECK_INIT_STORAGE_IN_WARE = "/jsp/sales/storageManage/returnDtlCheckStorageInWare.jsp";
	//退车财务明细审核
    private final String RETURN_DLT_CHECK_INIT_FINFINAL = "/jsp/sales/storageManage/returnDtlCheckFinfinal.jsp";
    //明细页面
	private final String RETURN_DTL_INIT = "/jsp/sales/storageManage/returnDtl.jsp";
	
	/**
	 * 区域退车申请初始化
	 */
	public void returnVehicleReqInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(RETURN_VEHICLE_REQ_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "退车申请初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 车辆验证是否失效
	 * */ 
	public void CheckCode() {
		ActionContext act = ActionContext.getContext();
		String code = act.getRequest().getParamValue("code");
		String[] codes = new String[code.length()];
		codes = code.split(",");
		code = dao.getCheckCode(codes);
		act.setOutData("code", code);
	}
	
	/**
	 * 添加退车
	 * **/
	public void chooseVehicleInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(CHOOSE_VEHICLE_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "退车车辆选择初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 提交退车申请
	 */
	@SuppressWarnings("null")
	public void returnVehicleReqdo() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String[] vehicleIds = request.getParamValues("vehicleIds"); // 申请单号
			// 数组类型
			String reason = CommonUtils.checkNull(request.getParamValue("reason"));// 退车原因
			dao = ReturnVehicleReqDao.getInstance();
			// 循环遍历插入TT_VS_RETURN_VEHICLE_REQ
			if (vehicleIds != null || !vehicleIds.equals("")) {
				for (int i = 0; i < vehicleIds.length; i++) {
					String vehicleId = vehicleIds[i];
					String a = vehicleId.substring(0, vehicleId.indexOf("||"));
					String b = vehicleId.substring(vehicleId.indexOf("||") + 2);
					TtVsReturnVehicleReqPO po = new TtVsReturnVehicleReqPO();
					Long reqId = Long
							.parseLong(SequenceManager.getSequence(""));
					String reqNo = "TC" + reqId;
					po.setReqId(reqId);// ID
					po.setReqNo(reqNo); // No
					po.setVehicleId(Long.parseLong(a));// 车辆表ID
					po.setDealerId(Long.parseLong(b));// 经销商ID
					po.setAreaid(dao.getVAreaId(a));
					po.setReason(reason);// 退车原因
					po.setStatus(Integer.parseInt(Constant.RETURN_STATUS_01));
					po.setRaiseDate(new Date());
					po.setCompanyId(Long.parseLong(logonUser.getOemCompanyId()));
					po.setCreateBy(logonUser.getUserId());
					po.setCreateDate(po.getRaiseDate());
					dao.insert(po);
					TmVehiclePO aa = new TmVehiclePO();
					TmVehiclePO bb = new TmVehiclePO();
					aa.setVehicleId(Long.parseLong(a));
					bb.setLockStatus(Constant.LOCK_STATUS_05);
					bb.setUpdateBy(po.getCreateBy());
					bb.setUpdateDate(po.getRaiseDate());
					dao.update(aa, bb);// 给车辆加退车锁
				}
			}
			act.setOutData("returnValue", 1); // 前台回调函数的参数
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.PUTIN_FAILURE_CODE, "退车申请");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商退车查询界面
	 */
	public void returnVehicleQueryInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			AccountBalanceDetailDao abd = AccountBalanceDetailDao.getInstance();
			List<Map<String, Object>> typeList = abd.getAccountType();
			act.setOutData("typeList", typeList);
			act.setForword(queryUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "退车查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商退车查询数据
	 */
	public void oemReturnQuery() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dutyType = logonUser.getDutyType(logonUser.getDealerId());
			/*Long companyId = logonUser.getCompanyId();
			Long poseId = logonUser.getPoseId();*/
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String returnNo = CommonUtils.checkNull(request.getParamValue("returnNo")); //退单号
			String accountType = CommonUtils.checkNull(request.getParamValue("accountType"));
			// 2012-03-12 新增申请日期和审核日期
			String checkstartDate = CommonUtils.checkNull(request.getParamValue("checkstartDate")); // 审核开始日期
			String checkendDate = CommonUtils.checkNull(request.getParamValue("checkendDate")); // 审核结束日期
			String reqStartDate = CommonUtils.checkNull(request.getParamValue("reqStartDate")); // 申请开始日期
			String reqEndDate = CommonUtils.checkNull(request.getParamValue("reqEndDate")); // 申请开始日期
			Map<String, String> map = new HashMap<String, String>();
			if (dutyType.equals(Constant.DUTY_TYPE_DEALER.toString())) {
				// DealerRelation dr = new DealerRelation();
				// String dealerIds = dr.getDealerIdByPose(companyId, poseId);
				map.put("dealerId", logonUser.getDealerId());
			} else {
				String orgId = logonUser.getOrgId().toString();
				map.put("orgId", orgId);
			}
			map.put("dutyType", dutyType);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			map.put("status", status);
			map.put("returnNo", returnNo);
			map.put("accountType", accountType);
			map.put("checkstartDate", checkstartDate);
			map.put("checkendDate", checkendDate);
			map.put("reqStartDate", reqStartDate);
			map.put("reqEndDate", reqEndDate);
			map.put("reqEndDate", reqEndDate);
			PageResult<Map<String, Object>> ps = dao.returnQueryByDealerOrOrg(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 退车查询
	 */
	public void returnVehicleQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String materialCode = CommonUtils.checkNull(request
					.getParamValue("materialCode"));
			String materialCode__ = CommonUtils.checkNull(request
					.getParamValue("materialCode__")); // 物料
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String status = CommonUtils.checkNull(request
					.getParamValue("status"));
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerId(comId.toString(), poseId.toString());
			String orgId = "";
			String dutyType = logonUser.getDutyType(logonUser.getDealerId());
			if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString();
			}
			String dealerIds__ = "";
			for (int i = 0; i < areaList.size(); i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()
						+ ",";
			}
			if (dealerIds__ != null && !"".equals(dealerIds__)) {
				dealerIds__ = dealerIds__.substring(0,
						(dealerIds__.length() - 1)); // 当前用户职位对应的经销商ID
			}
			String areaIds = MaterialGroupManagerDao
					.getAreaIdsByPoseId(logonUser.getPoseId());
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getQueryReturnVehicleReq(
					orgId, status, dealerIds__, materialCode, materialCode__,
					vin, areaIds, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "退车");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 车厂端查询退车申请
	 */
	public void returnVehicleQueryOEMInit() {
		returnVehicleQueryInit();
	}
	
	/**
	 * 退车区域审核
	 */
	public void returnVehicleApplyInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(applyUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "退车申请审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售部审核
	 */
	public void returnVehicleApplySaleInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(applyUrlSale);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "退车申请审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 储运部审核
	 */
	public void returnVehicleApplyStorageInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(applyUrlStorage);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "退车申请审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 财务审核
	 */
	public void returnVehicleApplyFinFinalInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(applyUrlFinfinal);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "退车申请审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 退车申请列表
	 */
	public void applyReturnVehicleQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String materialCode = CommonUtils.checkNull(request
					.getParamValue("materialCode"));
			String materialCode__ = CommonUtils.checkNull(request
					.getParamValue("materialCode__")); // 物料
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String dealerId = CommonUtils.checkNull(request
					.getParamValue("dealerId"));
			String orgId = "";
			String dutyType = logonUser.getDutyType(logonUser.getDealerId());
			if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString();
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getApplyReturnVehicleReq(
					orgId, dealerId, areaId, materialCode, materialCode__, vin,
					Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "退车");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/*
	 * 退车审核操作 flag = 1 通过 flag = 2 驳回
	 */
	@SuppressWarnings("null")
	public void returnVehicleReqApplydo() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String[] vehicleIds = request.getParamValues("vehicleIds"); // 申请单号
			// 数组类型
			String reason = CommonUtils.checkNull(request
					.getParamValue("reason"));// 退车原因
			String flag = request.getParamValue("flag");
			// 循环遍历插入TT_VS_RETURN_VEHICLE_REQ
			if (vehicleIds != null || !vehicleIds.equals("")) {
				for (int i = 0; i < vehicleIds.length; i++) {
					TtVsReturnVehicleReqPO po = new TtVsReturnVehicleReqPO();
					TtVsReturnVehicleReqPO tp = new TtVsReturnVehicleReqPO();
					String[] vehicleReq = vehicleIds[i].split("-");
					po.setReqId(Long.parseLong(vehicleReq[1]));
					tp.setUpdateBy(logonUser.getUserId());
					tp.setUpdateDate(new Date());
					tp.setCheckDate(tp.getUpdateDate());
					tp.setCheckRemark(reason);
					if (flag.equals("1")) {
						tp.setStatus(Integer
								.parseInt(Constant.RETURN_STATUS_02));
					} else {
						tp.setStatus(Integer
								.parseInt(Constant.RETURN_STATUS_03));
					}
					dao.update(po, tp);// 修改退车申请表
					StringBuffer sql = new StringBuffer();
					sql.append("UPDATE tm_vehicle\n");
					sql.append("   SET update_by   =" + tp.getUpdateBy()
							+ ",\n");
					sql.append("       update_date = sysdate\n");
					if (flag.equals("1")) {
						sql.append("       ,dealer_id   = NULL,\n");
						sql.append("       life_cycle  = "
								+ Constant.VEHICLE_LIFE_02 + "\n");
					}
					sql.append("       ,lock_status = "
							+ Constant.LOCK_STATUS_01 + "\n");
					sql.append(" WHERE vehicle_id ="
							+ Long.parseLong(vehicleReq[0]));
					dao.update(sql.toString(), null);// 解锁并更改车辆生命周期
					if (flag.equals("1")) {
						// 调用发运存储过程
						List<Object> ins = new LinkedList<Object>();
						ins.add(vehicleReq[1]);
						List<Integer> outs = new LinkedList<Integer>();
						dao.callProcedure("p_return_vehicle_req_to_erp", ins,
								outs);
					}
				}
			}
			act.setOutData("returnValue", 1); // 前台回调函数的参数
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.PUTIN_FAILURE_CODE, "退车申请");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 车辆退回基地下载 add by yx 20110228
	 */
	public void returnVehicleToBaseDownLoad() {
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		ResponseWrapper response = act.getResponse();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		// act.getSession().get(Constant.LOGON_USER);
		// 导出的文件名
		String fileName = "车辆退回基地下载.csv";
		// 导出的文字编码
		OutputStream os = null;
		try {
			String materialCode = CommonUtils.checkNull(request
					.getParamValue("materialCode"));
			String materialCode__ = CommonUtils.checkNull(request
					.getParamValue("materialCode__")); // 物料
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerId(comId.toString(), poseId.toString());
			String dealerIds__ = "";
			for (int i = 0; i < areaList.size(); i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()
						+ ",";
			}
			dealerIds__ = dealerIds__.substring(0, (dealerIds__.length() - 1)); // 当前用户职位对应的经销商ID
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			List<List<Object>> list = new LinkedList<List<Object>>();
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("车型名称");
			listTemp.add("配置");
			listTemp.add("物料代码");
			listTemp.add("物料名称");
			listTemp.add("VIN");
			listTemp.add("位置说明");
			listTemp.add("经销商");
			listTemp.add("库存状态");
			listTemp.add("入库日期");
			list.add(listTemp);
			PageResult<Map<String, Object>> ps = dao.getReturnVehicleReq(
					dealerIds__, materialCode, materialCode__, vin, 100000,
					curPage);
			List<Map<String, Object>> results = ps.getRecords();
			if (results != null && !results.equals("")) {
				for (int i = 0; i < results.size(); i++) {
					Map<String, Object> record = results.get(i);
					listTemp = new LinkedList<Object>();
					listTemp.add(CommonUtils.checkNull(record.get("MODEL_NAME")));
					listTemp.add(CommonUtils.checkNull(record
							.get("PACKAGE_NAME")));
					listTemp.add(CommonUtils.checkNull(record
							.get("MATERIAL_CODE")));
					listTemp.add(CommonUtils.checkNull(record
							.get("MATERIAL_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("VIN")));
					listTemp.add(CommonUtils.checkNull(record
							.get("WAREHOUSE_NAME")));
					listTemp.add(CommonUtils.checkNull(record
							.get("DEALER_SHORTNAME")));
					listTemp.add(CommonUtils.checkNull(record
							.get("LIFE_CYCLE_DESC")));
					listTemp.add(CommonUtils.checkNull(record
							.get("STORAGE_TIME")));
					list.add(listTemp);
				}
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退回基地下载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 车辆退车下载 add by yx 20110228
	 */
	public void vhclBackDownLoad() {
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		ResponseWrapper response = act.getResponse();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		// act.getSession().get(Constant.LOGON_USER);
		// 导出的文件名
		String fileName = "车辆退车下载.csv";
		// 导出的文字编码
		OutputStream os = null;
		try {
			String materialCode = CommonUtils.checkNull(request
					.getParamValue("materialCode"));
			String materialCode__ = CommonUtils.checkNull(request
					.getParamValue("materialCode__")); // 物料
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String status = CommonUtils.checkNull(request
					.getParamValue("status"));
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId();
			String orgId = "";
			String dutyType = logonUser.getDutyType(logonUser.getDealerId());
			if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString();
			}
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerId(comId.toString(), poseId.toString());
			String dealerIds__ = "";
			for (int i = 0; i < areaList.size(); i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()
						+ ",";
			}
			if (dealerIds__ != null && !"".equals(dealerIds__)) {
				dealerIds__ = dealerIds__.substring(0,
						(dealerIds__.length() - 1)); // 当前用户职位对应的经销商ID
			}
			String areaIds = MaterialGroupManagerDao
					.getAreaIdsByPoseId(logonUser.getPoseId());
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			List<List<Object>> list = new LinkedList<List<Object>>();
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("退车号");
			listTemp.add("车型名称");
			listTemp.add("配置");
			listTemp.add("物料代码");
			listTemp.add("物料名称");
			listTemp.add("VIN");
			listTemp.add("经销商");
			listTemp.add("申请时间");
			listTemp.add("申请原因");
			listTemp.add("审核时间");
			listTemp.add("审核结果");
			listTemp.add("审核状态");
			list.add(listTemp);
			// List<Map<String, Object>> results =
			// dao.getLoadDealerVhclDetail(areaIds.toString(),dealerIds, vin,
			// companyId, 99999999, Constant.PAGE_SIZE);
			PageResult<Map<String, Object>> ps = dao.getQueryReturnVehicleReq(
					orgId, status, dealerIds__, materialCode, materialCode__,
					vin, areaIds, 100000, curPage);
			List<Map<String, Object>> results = ps.getRecords();
			if (results != null && !results.equals("")) {
				for (int i = 0; i < results.size(); i++) {
					Map<String, Object> record = results.get(i);
					listTemp = new LinkedList<Object>();
					listTemp.add(CommonUtils.checkNull(record.get("REQ_NO")));
					listTemp.add(CommonUtils.checkNull(record.get("MODEL_NAME")));
					listTemp.add(CommonUtils.checkNull(record
							.get("PACKAGE_NAME")));
					listTemp.add(CommonUtils.checkNull(record
							.get("MATERIAL_CODE")));
					listTemp.add(CommonUtils.checkNull(record
							.get("MATERIAL_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("VIN")));
					listTemp.add(CommonUtils.checkNull(record
							.get("DEALER_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("RAISE_DATE")));
					listTemp.add(CommonUtils.checkNull(record.get("REASON")));
					listTemp.add(CommonUtils.checkNull(record.get("CHECK_DATE")));
					listTemp.add(CommonUtils.checkNull(record
							.get("CHECK_REMARK")));
					listTemp.add(CommonUtils.checkNull(record
							.get("REQ_STATUS_DESC")));
					list.add(listTemp);
				}
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车下载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 *查询需要退车的列表
	 */
	public void returnVehicleReqQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String deliveryNo = CommonUtils.checkNull(request.getParamValue("deliveryNo")); //发运单号
			String exVeh = CommonUtils.checkNull(request.getParamValue("exVeh"));
			String typeName = CommonUtils.checkNull(request.getParamValue("typeName"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			//Long poseId = logonUser.getPoseId();
			//Long companyId = logonUser.getCompanyId();
			DealerRelation dr = new DealerRelation();
			//String dealerIds=dao.getDealerIdBySession(logonUser);
			/*if (!CommonUtils.isNullString(dealerId)) {
				dealerIds = dealerId;
			}*/
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Map<String, String> map = new HashMap<String, String>();
			map.put("dealerId", logonUser.getDealerId());
			map.put("vin", vin);
			map.put("deliveryNo", deliveryNo);
			map.put("exVeh", exVeh);
			map.put("typeName", typeName);
			PageResult<Map<String, Object>> ps = dao.ReturnVehicleReqQuery_SUZUKI(map, Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "退车申请");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 退车提交申请
	 * */
	public void returnVehicleApply() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long userId = logonUser.getUserId();
			String oemCompanyId = logonUser.getOemCompanyId();
			String dealerId = logonUser.getDealerId();
			String[] vehicleIds = request.getParamValues("vehicleIds");
			String[] singlePrices = request.getParamValues("singlePrices");
			String[] priceListIds = request.getParamValues("priceListIds");
			// String[] areaIds = request.getParamValues("areaIds");
			String reason = CommonUtils.checkNull(request.getParamValue("remarkTxt"));
			// 获取资金类型的id
			String accountTypeId = CommonUtils.checkNull(request.getParamValue("fundType"));
			String[] accounts = accountTypeId.split("\\|");
			accountTypeId = accounts[0];
			int count = vehicleIds.length;
			Map<String, String> headMap = new HashMap<String, String>();
			headMap.put("dealerId", dealerId);
			headMap.put("userId", userId.toString());
			headMap.put("applyAmount", count + ""); // 计算这次退了几辆车
			headMap.put("reason", reason);
			headMap.put("status", Constant.RETURN_CAR_STATUS_01);
			headMap.put("companyId", logonUser.getOemCompanyId());
			headMap.put("accountTypeId", accountTypeId);
			Long headId = dao.returnHeadInsert(headMap);
			// Long headId = null;
			Map<String, String> detailMap = null;
			for (int i = 0; i < count; i++) {
				detailMap = new HashMap<String, String>();
				detailMap.put("dealerId", dealerId);
				detailMap.put("vehicleId", vehicleIds[i]);
				detailMap.put("reason", reason);
				detailMap.put("status", Constant.RETURN_CAR_STATUS_01);
				detailMap.put("companyId", oemCompanyId);
				detailMap.put("userId", userId.toString());
				detailMap.put("headId", headId.toString());
				detailMap.put("singlePrice", singlePrices[i]);
				detailMap.put("priceListId", priceListIds[i]);
				dao.returnLineInsert(detailMap);
				TmVehiclePO oldTv = new TmVehiclePO();
				TmVehiclePO newTv = new TmVehiclePO();
				oldTv.setVehicleId(Long.parseLong(vehicleIds[i]));
				newTv.setLockStatus(Constant.LOCK_STATUS_05);
				newTv.setLifeCycle(Constant.VEHICLE_LIFE_05);
				newTv.setUpdateBy(userId);
				newTv.setUpdateDate(new Date(System.currentTimeMillis()));
				dao.update(oldTv, newTv);
			}
			returnVehicleReqInit();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车申请");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/***
	 * 区域审核查询退车的列表
	 * */
	public void returnCheckQuery() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String returnNo = CommonUtils.checkNull(request.getParamValue("returnNo"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Map<String, String> map = new HashMap<String, String>();
			map.put("status", Constant.RETURN_CAR_STATUS_01);
			map.put("returnNo", returnNo);
			map.put("dealerId",logonUser.getDealerId());
			map.put("orgId",logonUser.getOrgId()+"");
			map.put("dutyType",logonUser.getDutyType(logonUser.getDealerId()));
			PageResult<Map<String, Object>> ps = dao.returnQuery(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/***
	 * 销售部审核查询退车的列表
	 * */
	public void returnCheckQuerySales() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String returnNo = CommonUtils.checkNull(request.getParamValue("returnNo"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Map<String, String> map = new HashMap<String, String>();
			map.put("status", Constant.RETURN_CAR_STATUS_02);
			map.put("returnNo", returnNo);
			map.put("dealerId",logonUser.getDealerId());
			map.put("orgId",logonUser.getOrgId()+"");
			map.put("dutyType",logonUser.getDutyType(logonUser.getDealerId()));
			PageResult<Map<String, Object>> ps = dao.returnQuery(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/***
	 * 储运部审核查询退车的列表
	 * */
	public void returnCheckQueryStorage() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String returnNo = CommonUtils.checkNull(request.getParamValue("returnNo"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Map<String, String> map = new HashMap<String, String>();
			map.put("status", Constant.RETURN_CAR_STATUS_03);
			map.put("returnNo", returnNo);
			map.put("dealerId",logonUser.getDealerId());
			map.put("orgId",logonUser.getOrgId()+"");
			map.put("dutyType",logonUser.getDutyType(logonUser.getDealerId()));
			PageResult<Map<String, Object>> ps = dao.returnQuery(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/***
	 * 财务审核查询退车的列表
	 * */
	public void returnCheckQueryFinfinal() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String returnNo = CommonUtils.checkNull(request.getParamValue("returnNo"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Map<String, String> map = new HashMap<String, String>();
			map.put("status", Constant.RETURN_CAR_STATUS_04);
			map.put("returnNo", returnNo);
			PageResult<Map<String, Object>> ps = dao.returnFinfinalQuery(map,logonUser,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void returnHeadCheck() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long userId = logonUser.getUserId();
			String[] headIds = request.getParamValues("headIds");
			String status = CommonUtils.checkNull(request
					.getParamValue("status"));
			String remark = CommonUtils.checkNull(request
					.getParamValue("reason"));
			int len = headIds.length;
			for (int i = 0; i < len; i++) {
				Map<String, String> headMap = new HashMap<String, String>();
				headMap.put("status", status);
				headMap.put("headId", headIds[i]);
				headMap.put("userId", userId.toString());
				headMap.put("remark", remark);
				dao.returnHeadUpdate(headMap);
				Map<String, String> detailMap = new HashMap<String, String>();
				detailMap.put("status", status);
				detailMap.put("headId", headIds[i]);
				detailMap.put("userId", userId.toString());
				detailMap.put("remark", remark);
				if (Constant.RETURN_STATUS_02.equals(status)) {
					detailMap.put("oldStatus",
							Constant.RETURN_STATUS_01.toString());
				}
				dao.returnLineUpdate(detailMap);
				StringBuffer sql = new StringBuffer("\n");
				sql.append("UPDATE tm_vehicle\n");
				sql.append("   SET update_by   = ").append(userId)
						.append(",\n");
				sql.append("       update_date = sysdate\n");
				if (Constant.RETURN_STATUS_02.equals(status)) {
					sql.append("       ,dealer_id   = NULL,\n");
					sql.append("       life_cycle  = "
							+ Constant.VEHICLE_LIFE_02 + "\n");
				}
				sql.append("       ,lock_status = " + Constant.LOCK_STATUS_01
						+ "\n");
				sql.append(
						" WHERE vehicle_id in (select tvrvr.vehicle_id from tt_vs_return_vehicle_req tvrvr where tvrvr.head_id = ")
						.append(headIds[i]).append(")");
				dao.update(sql.toString(), null);
				if (Constant.RETURN_STATUS_02.equals(status)) {
					List<Object> ins = new LinkedList<Object>();
					ins.add(headIds[i]);
					List<Integer> outs = new LinkedList<Integer>();
					dao.callProcedure("p_return_vehicle_req_to_erp", ins, outs);
				}
			}
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/***
	 * 区域审核明细审核
	 * */
	@SuppressWarnings("rawtypes")
	public void returnDtlCheckInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String headId = CommonUtils.checkNull(request.getParamValue("headId"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("headId", headId);
			List headList = dao.returnHeadQuery(map);
			TtReturnVehicleHeadPO trvh = (TtReturnVehicleHeadPO) headList.get(0);
			act.setOutData("trvh", trvh);
			act.setOutData("dealerId", trvh.getDealerId());
			map.put("status", Constant.RETURN_CAR_STATUS_01);
			List<Map<String, Object>> dltList = dao.returnDtlQuery_SUZUKI(map);
			act.setOutData("dltList", dltList);
			act.setForword(RETURN_DLT_CHECK_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车明细审核初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/***
	 * 销售部审核明细审核
	 * */
	@SuppressWarnings("rawtypes")
	public void returnDtlCheckInitSales() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String headId = CommonUtils.checkNull(request.getParamValue("headId"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("headId", headId);
			List headList = dao.returnHeadQuery(map);
			TtReturnVehicleHeadPO trvh = (TtReturnVehicleHeadPO) headList.get(0);
			act.setOutData("trvh", trvh);
			act.setOutData("dealerId", trvh.getDealerId());
			map.put("status", Constant.RETURN_CAR_STATUS_02);
			List<Map<String, Object>> dltList = dao.returnDtlQuery_SUZUKI(map);
			act.setOutData("dltList", dltList);
			act.setForword(RETURN_DLT_CHECK_INIT_SALES);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车明细审核初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/***
	 * 储运部审核明细审核
	 * */
	@SuppressWarnings("rawtypes")
	public void returnDtlCheckInitStorage() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String headId = CommonUtils.checkNull(request.getParamValue("headId"));
			//查询仓库
			List<Map<String, Object>> houseList = dao.getWhereHoseList();
			act.setOutData("houseList", houseList);
			Map<String, String> map = new HashMap<String, String>();
			map.put("headId", headId);
			List headList = dao.returnHeadQuery(map);
			TtReturnVehicleHeadPO trvh = (TtReturnVehicleHeadPO) headList.get(0);
			act.setOutData("trvh", trvh);
			act.setOutData("dealerId", trvh.getDealerId());
			map.put("status", Constant.RETURN_CAR_STATUS_03);
			List<Map<String, Object>> dltList = dao.returnDtlQuery_SUZUKI(map);
			act.setOutData("dltList", dltList);
			act.setForword(RETURN_DLT_CHECK_INIT_STORAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车明细审核初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/***
	 * 储运部入库
	 * */
	@SuppressWarnings("rawtypes")
	public void returnDtlCheckInitStorageInWare() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String headId = CommonUtils.checkNull(request.getParamValue("headId"));
			//查询仓库
			List<Map<String, Object>> houseList = dao.getWhereHoseListInWare(headId);
			act.setOutData("houseList", houseList);
			Map<String, String> map = new HashMap<String, String>();
			map.put("headId", headId);
			List headList = dao.returnHeadQuery(map);
			TtReturnVehicleHeadPO trvh = (TtReturnVehicleHeadPO) headList.get(0);
			act.setOutData("trvh", trvh);
			act.setOutData("dealerId", trvh.getDealerId());
			map.put("status", Constant.RETURN_CAR_STATUS_04);
			List<Map<String, Object>> dltList = dao.returnDtlInWare(map);
			act.setOutData("dltList", dltList);
			act.setForword(RETURN_DLT_CHECK_INIT_STORAGE_IN_WARE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车明细审核初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/***
	 * 财务审核明细审核
	 * */
	@SuppressWarnings("rawtypes")
	public void returnDtlCheckInitFinfinal() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String headId = CommonUtils.checkNull(request.getParamValue("headId"));
			//查询账户
			List<Map<String, Object>> houseList = dao.getAcounntDetail(headId);
			act.setOutData("houseList", houseList);
			Map<String, String> map = new HashMap<String, String>();
			map.put("headId", headId);
			List headList = dao.returnHeadQuery(map);
			TtReturnVehicleHeadPO trvh = (TtReturnVehicleHeadPO) headList.get(0);
			act.setOutData("trvh", trvh);
			act.setOutData("dealerId", trvh.getDealerId());
			map.put("status", Constant.RETURN_CAR_STATUS_04);
			List<Map<String, Object>> dltList = dao.returnDtlQuery_SUZUKI(map);
			act.setOutData("dltList", dltList);
			act.setForword(RETURN_DLT_CHECK_INIT_FINFINAL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车明细审核初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 审核通过，包括区域和销售部
	 * */ 
	public void returnDtlPass() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long userId = logonUser.getUserId();
			String headId = request.getParamValue("headId"); //退运单ID
			String reason = request.getParamValue("reason");
			String[] reqIds = request.getParamValues("reqIds"); //退单明细ID
			String reqType = request.getParamValue("reqType"); // 类型  1 区域  2 销售  3 储运  4 财务
			String warHouseId =  request.getParamValue("warHouseId");//仓库ID(储运时才有)
			String accountId = request.getParamValue("accountId"); // 账户ID(财务)
			String infactMoney[] = request.getParamValues("infactMoney"); //实退金额(财务)
			String infactReturnMoneyTotal = request.getParamValue("infactReturnMoneyTotal"); //实退金额总数(财务)
			String dealerId = request.getParamValue("dealerId"); //经销商ID(财务)
			Map<String, String> map = null;
			int len = reqIds.length;
			TtReturnVehicleHeadPO ttReturnVehicleHeadPO = new TtReturnVehicleHeadPO();
			ttReturnVehicleHeadPO.setHeadId(new Long(headId));
			Iterator<PO> iterator = dao.select(ttReturnVehicleHeadPO).iterator();
			if (iterator.hasNext()) {
				ttReturnVehicleHeadPO = (TtReturnVehicleHeadPO) iterator.next();
			}
			// String no = OrderCode.get_TC(ttReturnVehicleHeadPO.getHeadId());
			String no = ttReturnVehicleHeadPO.getReturnVehicleNo();
			//更改明细状态
			for (int i = 0; i < len; i++) {
				map = new HashMap<String, String>();
				map.put("reqId", reqIds[i]);
				if ("2".equals(reqType)) {
					//销售更改的状态
					map.put("status", Constant.RETURN_CAR_STATUS_03);
				} else if ("3".equals(reqType)) {
					//储运部，状态不改变，为了添加如果的操作
					map.put("status", Constant.RETURN_CAR_STATUS_04);
				} else if ("4".equals(reqType)) {
					//财务
					map.put("infactMoney", infactMoney[i]);
					map.put("status", Constant.RETURN_CAR_STATUS_05);
				} else {
					//区域更改的状态
					map.put("status", Constant.RETURN_CAR_STATUS_02);
				}
				map.put("userId", userId.toString());
				map.put("remark", reason);
				map.put("no", no);
				map.put("reqType", reqType);
				dao.returnLineUpdate(map);
				/*TtVsReturnVehicleReqPO ttVsReturnVehicleReqPO = new TtVsReturnVehicleReqPO();
				ttVsReturnVehicleReqPO.setReqId(new Long(reqIds[i]));
				ttVsReturnVehicleReqPO = (TtVsReturnVehicleReqPO) dao.select(ttVsReturnVehicleReqPO).get(0);
				// 3.新增车辆更改日志
				TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
				chngPO.setVhclChangeId(Long.parseLong(SequenceManager.getSequence("")));
				chngPO.setVehicleId(ttVsReturnVehicleReqPO.getVehicleId());
				chngPO.setOrgType(logonUser.getOrgType());
				chngPO.setOrgId(logonUser.getOrgId());
				chngPO.setDealerId(ttVsReturnVehicleReqPO.getDealerId());
				chngPO.setChangeCode(Constant.STORAGE_CHANGE_TYPE);
				chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_09 + "");
				chngPO.setChangeDate(new Date());
				chngPO.setCreateDate(new Date());
				chngPO.setCreateBy(logonUser.getUserId());
				dao.insert(chngPO);*/
				/*StringBuffer sql = new StringBuffer("\n");
				sql.append("UPDATE tm_vehicle\n");
				sql.append("   SET update_by   = ").append(userId).append(",\n");
				sql.append("       update_date = sysdate\n");
				sql.append("       ,dealer_id   = NULL,\n");
				sql.append("       life_cycle  = " + Constant.VEHICLE_LIFE_07+ "\n");
				sql.append("       ,lock_status = " + Constant.LOCK_STATUS_01+ "\n");
				sql.append(" WHERE vehicle_id in (select tvrvr.vehicle_id from tt_vs_return_vehicle_req tvrvr where tvrvr.req_id = ").append(reqIds[i]).append(")");
				dao.update(sql.toString(), null);*/
			}
			/**更改总退单总记录的信息**/
			/*// 获取资金类型的id
			String accountTypeId = CommonUtils.checkNull(request.getParamValue("fundType"));
			String[] accounts = accountTypeId.split("\\|");
			accountTypeId = accounts[0];*/	
			map = new HashMap<String, String>();
			map.put("headId", headId);
			map.put("userId", userId.toString());
			if ("2".equals(reqType)) {
				//销售更改的状态
				map.put("preStatus", Constant.RETURN_CAR_STATUS_02);
				map.put("nextStatus", Constant.RETURN_CAR_STATUS_03);
			} else if ("3".equals(reqType)) {
				//储运部更改状态
				map.put("preStatus", Constant.RETURN_CAR_STATUS_03);
				map.put("nextStatus", Constant.RETURN_CAR_STATUS_03);
				map.put("isWareHousing", Constant.STATUS_ENABLE+"");
			} else if ("4".equals(reqType)) {
				//财务更改状态
				map.put("preStatus", Constant.RETURN_CAR_STATUS_04);
				map.put("nextStatus", Constant.RETURN_CAR_STATUS_05);
			} else {
				//区域更改的状态
				map.put("preStatus", Constant.RETURN_CAR_STATUS_01);
				map.put("nextStatus", Constant.RETURN_CAR_STATUS_02);
			}
			dao.returnHeadUpdate(map);
			//储运审核修改值
			if ("3".equals(reqType)) {
				//如果仓库ID不为空，代表是储运审核
				/*if (!CheckUtil.checkNull(warHouseId)) {
					//根据headId将车辆表仓库ID换成新的仓库ID（车厂的仓库），并把车辆的锁定改为正常，车辆变成车厂库存
					dao.chnageVehiceStatus(warHouseId,headId);
				}*/
			}
			//财务审核修改
			if ("4".equals(reqType)) {
				if (!CheckUtil.checkNull(accountId)) {
					//给退运单修改状态和账户ID
					dao.changAccountReturn(Long.parseLong(headId), Long.parseLong(accountId), logonUser.getUserId());
					if (CommonUtils.isEmpty(infactReturnMoneyTotal)) {
						infactReturnMoneyTotal = 0+"";
					}
					//给当前账户的余额加上总数
					dao.changAccountToatal(Long.parseLong(accountId), logonUser.getUserId(),infactReturnMoneyTotal,dealerId);
				}
			}
			/*if (headId != null && !"".equals(headId)) {
				 插入DMS-> ERP 接口表：CUX_ERP_DELIVER 数据 begin 
				String orderTypeName = "采购退货";
				this.dmsSendToErp(headId, orderTypeName, "", reqIds);
				 插入DMS-> ERP 接口表：CUX_ERP_DELIVER 数据 end 
			}*/
			act.setOutData("flag", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车明细审核驳回");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 储运部审核通过（入库）
	 * */ 
	public void returnDtlPassInWare() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String warHouseId= request.getParamValue("warHouseId");
			String headId = request.getParamValue("headId"); //退运单ID
			if (!CheckUtil.checkNull(warHouseId)) {
				//根据headId将车辆表仓库ID换成新的仓库ID（车厂的仓库），并把车辆的锁定改为正常，车辆变成车厂库存
				dao.chnageVehiceStatus(warHouseId,headId);
			}
			dao.changeRetuenStorageStatus(headId, logonUser.getUserId());
			act.setOutData("flag", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车明细审核驳回");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 审核驳回，包括区域和销售部
	 * */ 
	public void returnDtlRet() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long userId = logonUser.getUserId();
			//退车ID
			String headId = request.getParamValue("headId");
			//退车原因
			String reason = request.getParamValue("reason");
			//车辆明细ID
			String[] reqIds = request.getParamValues("reqIds");
			//审核区域类型
			String reqType = request.getParamValue("reqType");
			Map<String, String> map = null;
			int len = reqIds.length;
			for (int i = 0; i < len; i++) {
				map = new HashMap<String, String>();
				map.put("reqId", reqIds[i]);
				map.put("status", Constant.RETURN_CAR_STATUS_07);
				map.put("userId", userId.toString());
				map.put("remark", reason);
				map.put("reqType", reqType);
				dao.commonChangeReturnDetail(map);
			}
			//更改车辆表和退单表
			dao.commonChangeVechiceStatus(Long.parseLong(headId),userId);
			act.setOutData("flag", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车明细审核驳回");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dtlInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String headId = CommonUtils.checkNull(request
					.getParamValue("headId"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("headId", headId);
			List<Map<String, Object>> dtlList = dao.returnDtlQueryNew(map);
			act.setOutData("dtlList", dtlList);
			act.setForword(RETURN_DTL_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆退车明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 经销商退车查询--下载
	 */
	public void download() {
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dutyType = logonUser.getDutyType(logonUser.getDealerId());
			/*Long companyId = logonUser.getCompanyId();
			Long poseId = logonUser.getPoseId();*/
			String status = CommonUtils.checkNull(request.getParamValue("status")); // 审核状态
			String returnNo = CommonUtils.checkNull(request.getParamValue("returnNo")); // 退车单号
			String accountType = CommonUtils.checkNull(request.getParamValue("accountType")); // 帐户类型
			String checkstartDate = CommonUtils.checkNull(request.getParamValue("checkstartDate")); // 审核开始日期
			String checkendDate = CommonUtils.checkNull(request.getParamValue("checkendDate")); // 审核结束日期
			String reqStartDate = CommonUtils.checkNull(request.getParamValue("reqStartDate")); // 申请开始日期
			String reqEndDate = CommonUtils.checkNull(request.getParamValue("reqEndDate")); // 申请开始日期
			Map<String, String> map = new HashMap<String, String>();
			if (dutyType.equals(Constant.DUTY_TYPE_DEALER.toString())) {
				// DealerRelation dr = new DealerRelation();
				// String dealerIds = dr.getDealerIdByPose(companyId, poseId);
				map.put("dealerId", logonUser.getDealerId());
			//} else if (dutyType.equals(Constant.DUTY_TYPE_LARGEREGION.toString())) {
			} else {
				String orgId = logonUser.getOrgId().toString();
				map.put("orgId", orgId);
			}
			// Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			map.put("status", status);
			map.put("returnNo", returnNo);
			map.put("accountType", accountType);
			map.put("checkstartDate", checkstartDate);
			map.put("checkendDate", checkendDate);
			map.put("reqStartDate", reqStartDate);
			map.put("reqEndDate", reqEndDate);
			// 导出的文件名
			String fileName = "实销退车审核报表.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("经销商退车查询报表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			List<Map<String, Object>> list = dao.returnQueryByDealerOrOrgDowload(map);
			// int len = list.size();
			/*
			 * Calendar calendar = Calendar.getInstance(); int year =
			 * calendar.get(Calendar.YEAR); int month =
			 * calendar.get(Calendar.MONTH)+1; int day
			 * =calendar.get(Calendar.DAY_OF_MONTH);
			 */
			int row = 0;
			/*
			 * act.setOutData("year", year); act.setOutData("month", month);
			 * act.setOutData("day", day);
			 */
			sheet.mergeCells(0, row, 8, row);
			sheet.addCell(new jxl.write.Label(0, row, "经销商退车查询报表", wcf));
			/*
			 * ++row; sheet.mergeCells(0, row, 8, row); sheet.addCell(new
			 * jxl.write.Label(0, row,
			 * "日期："+String.valueOf(year)+"年"+String.valueOf
			 * (month)+"月"+String.valueOf(day)+"日"));
			 */
			++row;
			sheet.addCell(new Label(0, row, "退车号"));
			sheet.addCell(new Label(1, row, "经销商简称"));
			sheet.addCell(new Label(2, row, "资金类型"));
			sheet.addCell(new Label(3, row, "申请数量"));
			sheet.addCell(new Label(4, row, "申请原因"));
			sheet.addCell(new Label(5, row, "申请日期"));
			/*sheet.addCell(new Label(6, row, "审核描述"));*/
			sheet.addCell(new Label(7, row, "审核日期"));
			sheet.addCell(new Label(8, row, "审核状态"));
			status = null;
			for (int i = 0; i < list.size(); i++) {
				++row;
				sheet.addCell(new Label(0, row, list.get(i).get("RETURN_VEHICLE_NO") == null ? "" : list.get(i).get("RETURN_VEHICLE_NO").toString()));
				sheet.addCell(new Label(1, row, list.get(i).get("DEALER_SHORTNAME") == null ? "" : list.get(i).get("DEALER_SHORTNAME").toString()));
				sheet.addCell(new Label(2, row, list.get(i).get("ACCOUNT_TYPE_NAME") == null ? "" : list.get(i).get("ACCOUNT_TYPE_NAME").toString()));
				sheet.addCell(new Label(3, row,list.get(i).get("APPLY_AMOUNT") == null ? "" : list.get(i).get("APPLY_AMOUNT").toString()));
				sheet.addCell(new Label(4, row,list.get(i).get("REASON") == null ? "" : list.get(i).get("REASON").toString()));
				sheet.addCell(new Label(5, row, list.get(i).get("APPLAY_CREATE") == null ? "" : list.get(i).get("APPLAY_CREATE").toString()));
				// sheet.addCell(new Label(6, row,list.get(i).get("CHECK_REMARK") == null ? "" : list.get(i).get("CHECK_REMARK").toString()));
				sheet.addCell(new Label(7, row,list.get(i).get("CHECK_DATE") == null ? "" : list.get(i).get("CHECK_DATE").toString()));
				status = list.get(i).get("STATUS") == null ? "" : list.get(i).get("STATUS").toString();
				if (status == null) {
					status = "";
				} else if (status.equals(Constant.RETURN_CAR_STATUS_05)) {
					status = "审核通过";
				} else if (status.equals(Constant.RETURN_CAR_STATUS_02)) {
					status = "区域审核";
				} else if (status.equals(Constant.RETURN_CAR_STATUS_03)) {
					status = "销售审核";
				}else if (status.equals(Constant.RETURN_CAR_STATUS_04)) {
					status = "储运审核";
				}else if (status.equals(Constant.RETURN_CAR_STATUS_01)) {
					status = "经销商提报退车申请";
				}else if (status.equals(Constant.RETURN_CAR_STATUS_07)) {
					status = "审核驳回";
				}
				sheet.addCell(new Label(8, row, status));
			}
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商退车查询报表下载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 经销商订单发运申请提报表(退单)（CUX_ERP_DELIVER）
	 * 
	 * @param headId
	 *            : 退车ID （TT_RETURN_VEHICLE_HEAD 表 HEAD_ID）
	 * @param orderTypeName
	 *            : 订单类型
	 * @param warehouseId
	 *            : 仓库ID
	 * @param reqIds
	 *            : 退单申请明细ID
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	private void dmsSendToErp(String headId, String orderTypeName,String warehouseId, String[] reqIds) {
		logger.info("====*********************DMS -> ERP 对接口表 经销商订单发运申请提报表(退单)【CUX_ERP_DELIVER】 下发开始 ************************************* ====");
		try {
			TtReturnVehicleHeadPO trvhpo = new TtReturnVehicleHeadPO(); // 退车单PO
			trvhpo.setHeadId(new Long(headId));
			trvhpo = (TtReturnVehicleHeadPO) dao.getPoObject(trvhpo);
			TmDealerPO dpo = new TmDealerPO();// 经销商PO
			dpo.setDealerId(trvhpo.getDealerId());
			dpo = (TmDealerPO) dao.getPoObject(dpo);
			TmVsAddressPO apo = new TmVsAddressPO();// 经销商地址PO
			apo.setDealerId(trvhpo.getDealerId());
			apo = (TmVsAddressPO) dao.getPoObject(apo);
			if (reqIds != null && reqIds.length > 0) {
				if (dpo != null && apo != null && trvhpo != null) {
					List list = dao.getCuxErpReturn(headId);
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							HashMap hm = (HashMap) list.get(i);
							CuxErpDeliverPO cedpo = new CuxErpDeliverPO();// 接口表PO
							cedpo.setCustomerNumber(dpo.getDealerCode());// 经销商代码
							cedpo.setCustomerName(dpo.getDealerName());// 经销商名称
							cedpo.setShipToLocation(apo.getAddCode());// 收货地址编号
							cedpo.setOrderNumber(trvhpo.getReturnVehicleNo());// 编号必须为数字类型
							cedpo.setOrderType("4");// 订单类型（文字)采购退货
							cedpo.setOrderDate(trvhpo.getCreateDate());// 订购日期,
																		// 经销商发运申请提报日期
							cedpo.setRequestDate(trvhpo.getCreateDate());// 请求日期,默认为经销商申请提报日期
							cedpo.setScheduleShipDate(trvhpo.getCreateDate());// 计划发运日期,
																				// 默认为经销商申请提报日期
							cedpo.setReturnReason(trvhpo.getReason());// 退货原因
							if (trvhpo.getAccountTypeId() != null
									&& trvhpo.getAccountTypeId() != 0) {
								TtVsAccountTypePO tpo = new TtVsAccountTypePO();
								tpo.setTypeId(trvhpo.getAccountTypeId());
								tpo = (TtVsAccountTypePO) dao.getPoObject(tpo);
								if (tpo != null) {
									cedpo.setFundsType(tpo.getMarkCode());// 资金类型在ERP
																			// code
								}
							}
							cedpo.setPlateNumber(new BigDecimal("0"));// 临时牌照的数量
							cedpo.setStatus("N");// 处理标识“N”,“Y”
							cedpo.setOrderedQuantity(new BigDecimal(String
									.valueOf(hm.get("APPLY_COUNT"))));// 申请数量
							cedpo.setPriceListId(new BigDecimal(String
									.valueOf(hm.get("PRICE_LIST_ID"))));// 价目表ID（子表中信息）
							cedpo.setItemCode(String.valueOf(hm
									.get("MATERIAL_CODE")));// 整车的物料编码
							cedpo.setCreationDate(new Date());// 创建时间
							cedpo.setSeqId(new BigDecimal(SequenceManager
									.getSequence("")));// 新增 ： seq_id
							cedpo.setLineNumber(new BigDecimal(i + 1));// 行号
							dao.insert(cedpo);
							cedpo = null;
						}
						for (int j = 0; j < reqIds.length; j++) {
							TtVsReturnVehicleReqPO redpo = new TtVsReturnVehicleReqPO();// 退车申请明细PO
							redpo.setReqId(new Long(reqIds[j]));
							redpo = (TtVsReturnVehicleReqPO) dao
									.getPoObject(redpo);
							CuxErpReturnVinPO cevpo = new CuxErpReturnVinPO();// 退车接口明细PO
							TmVehiclePO vehiclepo = new TmVehiclePO();
							vehiclepo.setVehicleId(redpo.getVehicleId());
							vehiclepo = (TmVehiclePO) dao
									.getPoObject(vehiclepo);
							if (vehiclepo != null) {
								TmVhclMaterialPO mpo = new TmVhclMaterialPO();// 物料PO
								mpo.setMaterialId(vehiclepo.getMaterialId());
								mpo = (TmVhclMaterialPO) dao.getPoObject(mpo);
								if (mpo != null) {
									cevpo.setItemCode(mpo.getMaterialCode());// 整车的物料编码
								}
								cevpo.setVin(vehiclepo.getVin());// 弹性字段1，dms定义为“VIN”码，车架号
							}
							cevpo.setOrderNumber(trvhpo.getReturnVehicleNo());
							cevpo.setSeqId(new BigDecimal(redpo.getReqId()));// 新增
																				// ：
																				// seq_id
							dao.insert(cevpo);
							cevpo = null;
						}
						TtProcInterfaceMonitorPO log = new TtProcInterfaceMonitorPO();
						log.setInterfaceName("DMS->ERP 经销商订单发运申请提报表(退单)【CUX_ERP_DELIVER】下发");
						log.setProcName("DMS->ERP【CUX_ERP_DELIVER】 SEND");
						log.setCount(list.size());
						log.setCreateDate(new Date());
						log.setStatus(new Integer(1));
						dao.insert(log);
						logger.info("====*****DMS -> ERP 对接口表 经销商订单发运申请提报表(退单)单号：{"
								+ trvhpo.getReturnVehicleNo()
								+ "}【CUX_ERP_DELIVER】 下发结束,共下发【"
								+ list.size()
								+ "】条主数据，【"
								+ reqIds.length
								+ "】条明细数据 ***************** ====");
					} else {
						throw new RuntimeException(
								"====*****DMS -> ERP 对接口表 经销商订单发运申请提报表(退单) 【CUX_ERP_DELIVER】 下发失败了,【统计物料数报错 为 NULL】 *** ====");
					}
				} else {
					String erro = "";
					if (dpo == null) {
						erro = "经销商信息";
					} else if (apo == null) {
						erro = "经销商地址信息";
					} else if (trvhpo == null) {
						erro = "退车单申请信息";
					} else {
						erro = "未知错误";
					}
					throw new RuntimeException(
							"====*****DMS -> ERP 对接口表 经销商订单发运申请提报表(退单) 【CUX_ERP_DELIVER】 下发失败了,【"
									+ erro + " 为 NULL】 *** ====");
				}
			} else {
				throw new RuntimeException(
						"====*****DMS -> ERP 对接口表 经销商订单发运申请提报表(退单) 【CUX_ERP_DELIVER】 下发失败了,【订单发运申请明细信息长度为 0 】 *** ====");
			}
		} catch (NumberFormatException e) {
			TtProcInterfaceMonitorPO log = new TtProcInterfaceMonitorPO();
			log.setInterfaceName("DMS->ERP 经销商订单发运申请提报表(退单)【CUX_ERP_DELIVER】下发");
			log.setProcName("DMS->ERP【CUX_ERP_DELIVER】 SEND");
			log.setCount(new Integer(0));
			log.setCreateDate(new Date());
			log.setStatus(new Integer(0));
			dao.insert(log);
			e.printStackTrace();
			logger.info("====*****DMS -> ERP 对接口表 经销商订单发运申请提报表(退单)【CUX_ERP_DELIVER】 下发失败了 ****************************** ====");
		}
	}
}
