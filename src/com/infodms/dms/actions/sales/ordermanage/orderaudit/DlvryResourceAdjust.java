/**
 * 
 */
package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TtVsOrderResourceReservePO;
import com.infodms.dms.po.TtVsResourceReserveLogDtlPO;
import com.infodms.dms.po.TtVsResourceReserveLogPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author Administrator
 * 
 */
public class DlvryResourceAdjust {
	private Logger logger = Logger.getLogger(DlvryResourceAdjust.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final OrderAuditDao dao = OrderAuditDao.getInstance();

	private final String DLVRY_RESOURCE_ADJUST_QUERY_URL = "/jsp/sales/ordermanage/orderaudit/dlvryResourceAdjustQuery.jsp";// 发运指令资源批次调整查询页
	private final String DLVRY_RESOURCE_ADJUST_DETAIL_QUERY_URL = "/jsp/sales/ordermanage/orderaudit/dlvryResourceAdjustDetail.jsp";// 发运指令资源批次调整明细页
	private final String DLVRY_RESOURCE_ADJUST_PATCH_NO_QUERY_URL = "/jsp/sales/ordermanage/orderaudit/dlvryResourceAdjustPatchNoQuery.jsp";// 发运指令资源批次调整批次号选择页

	public void dlvryResourceAdjustQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = dao.getNastyDateList(oemCompanyId);
			List<Map<String, Object>> areaBusList = MaterialGroupManagerDao
					.getPoseIdBusiness(logonUser.getPoseId().toString());
			String duty = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			if (Constant.DUTY_TYPE_DEPT.toString().equals(duty)) {
				orgId = logonUser.getParentOrgId();
			}
			// 回显查询条件
			String dealerCode = request.getParamValue("dealerCode"); // 经销商CODE
			String areaId = request.getParamValue("areaId"); // 业务范围ID
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String orderType = request.getParamValue("orderType"); // 订单类型
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			String deliveryStatus = request.getParamValue("deliveryStatus"); // 状态
			String orgCode = request.getParamValue("orgCode");// 区域代码

			act.setOutData("dateList", dateList);
			act.setOutData("areaBusList", areaBusList);
			act.setOutData("orgId", orgId);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("areaId", areaId);
			act.setOutData("groupCode", groupCode);
			act.setOutData("orderType", orderType);
			act.setOutData("orderNo", orderNo);
			act.setOutData("deliveryStatus", deliveryStatus);
			act.setOutData("orgCode", orgCode);
			act.setForword(DLVRY_RESOURCE_ADJUST_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发运指令资源批次调整查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 订单资源审核查询
	 */
	public void dlvryResourceAdjustQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String areaId = request.getParamValue("areaId"); // 业务范围ID
			String area = request.getParamValue("area"); // 业务范围IDS
			String dealerCode = request.getParamValue("dealerCode"); // 经销商CODE
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String orderType = request.getParamValue("orderType"); // 订单类型
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			String dlvNo = request.getParamValue("dlvNo"); // 发运单号
			String deliveryStatus = request.getParamValue("deliveryStatus"); // 状态
			String orgCode = request.getParamValue("orgCode");// 区域代码
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orgId", orgId);
			map.put("areaId", areaId);
			map.put("area", area);
			map.put("groupCode", groupCode);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderNo", orderNo);
			map.put("dlvNo", dlvNo);
			map.put("companyId", companyId.toString());
			map.put("deliveryStatus", deliveryStatus);
			map.put("orgCode", orgCode);

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getDlvryResourceAdjustQuery(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setOutData("orderType", orderType);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 订单资源审核明细
	 */
/*	public void dlvryResourceAdjustDetailQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("orderId"); // 订单ID
			String reqId = request.getParamValue("reqId"); // 发车申请ID
			String orderYearWeek = request.getParamValue("orderYearWeek"); // 订单年周
			String dealerCode = request.getParamValue("dealerCode"); // 经销商CODE
			String areaId = request.getParamValue("areaId"); // 业务范围ID
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String orderType = request.getParamValue("orderType"); // 订单类型
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			String reqStatus = request.getParamValue("reqStatus"); // 状态
			String orgCode = request.getParamValue("orgCode");// 区域代码

			Map<String, Object> map = dao.getOrderInfoByReqId(reqId);
			String areaGet = ((BigDecimal) map.get("AREA_ID")).toString();
			
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			areaPO.setAreaId(new Long(areaGet));
			List<PO> areaList = dao.select(areaPO);
			Long erpCode = null;
			if(areaList.size() != 0){
				areaPO = (TmBusinessAreaPO)areaList.get(0);
				erpCode = areaPO.getErpCode();
			}
			
			List<Map<String, Object>> list1 = dao.getorderResourceReserveDetailList(null,reqId,
					orderType, logonUser.getCompanyId().toString(), erpCode.toString());
			List<Map<String, Object>> wareHouseList = dao.getWareHouseList(logonUser.getCompanyId()
					.toString(), areaGet);// 仓库列表

			act.setOutData("map", map);
			act.setOutData("orderId", orderId);
			act.setOutData("reqId", reqId);
			act.setOutData("list1", list1);
			act.setOutData("orderYearWeek", orderYearWeek);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("areaId", areaId);
			act.setOutData("groupCode", groupCode);
			act.setOutData("orderType", orderType);
			act.setOutData("orderNo", orderNo);
			act.setOutData("reqStatus", reqStatus);
			act.setOutData("orgCode", orgCode);
			act.setOutData("wareHouseList", wareHouseList);
			act.setOutData("erpCode", erpCode);

			act.setForword(DLVRY_RESOURCE_ADJUST_DETAIL_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}*/

	/**
	 * 
	 */
	public void dlvryResourceAdjustSave() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] materialId = request.getParamValues("materialId");// 物料ID
			String[] batchNo = request.getParamValues("batchNo");// 批次号
			String[] detailId = request.getParamValues("detailId");// 发运申请明细ID
			String reqId = request.getParamValue("reqId"); // 发运申请ID
			String warehouseId = request.getParamValue("warehouseId"); // 仓库id

			// 保留历史
			TtVsResourceReserveLogPO tvrrlpo = new TtVsResourceReserveLogPO();
			tvrrlpo.setLogId(Long.parseLong(SequenceManager.getSequence("")));
			tvrrlpo.setReqId(new Long(reqId));
			tvrrlpo.setCreateBy(logonUser.getUserId());
			tvrrlpo.setCreateDate(new Date());
			dao.insert(tvrrlpo);

			for (int i = 0; i < detailId.length; i++) {
				if (detailId[i] != null && !detailId.equals("")) {

					Map<String, Object> logMap = new HashMap<String, Object>();// 保留历史明细map
					Map<String, Object> oldMap = new HashMap<String, Object>(); // 原资源保留map

					TtVsOrderResourceReservePO tvorrp = new TtVsOrderResourceReservePO();
					tvorrp.setReqDetailId(new Long(detailId[i]));
					List<PO> oldList = dao.select(tvorrp);
					for (int j = 0; j < oldList.size(); j++) {
						TtVsOrderResourceReservePO tvorrpGet = (TtVsOrderResourceReservePO) oldList
								.get(j);

						oldMap.put(tvorrpGet.getBatchNo(), tvorrpGet);

						// 保留历史明细
						TtVsResourceReserveLogDtlPO tvrrldpo = new TtVsResourceReserveLogDtlPO();
						tvrrldpo.setDtlId(Long.parseLong(SequenceManager.getSequence("")));
						tvrrldpo.setLogId(tvrrlpo.getLogId());
						tvrrldpo.setReqDetailId(tvorrpGet.getReqDetailId());
						tvrrldpo.setBatchNo(tvorrpGet.getBatchNo());
						tvrrldpo.setMaterialId(tvorrpGet.getMaterialId());
						tvrrldpo.setOldAmount(tvorrpGet.getAmount());
						tvrrldpo.setNewAmount(new Integer(0));
						tvrrldpo.setCreateBy(logonUser.getUserId());
						tvrrldpo.setCreateDate(new Date());

						logMap.put(tvorrpGet.getBatchNo(), tvrrldpo);
					}
					// 删除资源保留
					dao.delete(tvorrp);

					// 新增资源保留
					if (batchNo[i] != null && !batchNo[i].equals("")) {
						String[] tempArray = batchNo[i].split("\\/");
						for (int j = 0; j < tempArray.length; j++) {
							String tempStr = tempArray[j];
							String[] tempArray2 = tempStr.split("-");
							String patchNO = tempArray2[0];
							String count = tempArray2[1];
							tvorrp = new TtVsOrderResourceReservePO();
							tvorrp.setReserveId(Long.parseLong(SequenceManager.getSequence("")));
							tvorrp.setReqDetailId(new Long(detailId[i]));
							tvorrp.setMaterialId(new Long(materialId[i]));
							tvorrp.setBatchNo(patchNO);
							tvorrp.setAmount(new Integer(count));

							if (oldMap.containsKey(patchNO)) {
								TtVsOrderResourceReservePO tvorrpGet = (TtVsOrderResourceReservePO) oldMap
										.get(patchNO);
								if (tvorrpGet.getAmount().intValue() == tvorrp.getAmount()
										.intValue()) {
									tvorrp.setReserveStatus(tvorrpGet.getReserveStatus());
								} else {
									tvorrp.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_01);
								}
								tvorrp.setDeliveryAmount(tvorrpGet.getDeliveryAmount());

								// 取出保留明细历史
								TtVsResourceReserveLogDtlPO tvrrldpo = (TtVsResourceReserveLogDtlPO) logMap
										.get(patchNO);
								tvrrldpo.setNewAmount(new Integer(count));

							} else {
								tvorrp.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_01);

								// 保留历史明细
								TtVsResourceReserveLogDtlPO tvrrldpo = new TtVsResourceReserveLogDtlPO();
								tvrrldpo.setDtlId(Long.parseLong(SequenceManager.getSequence("")));
								tvrrldpo.setLogId(tvrrlpo.getLogId());
								tvrrldpo.setReqDetailId(tvorrp.getReqDetailId());
								tvrrldpo.setBatchNo(tvorrp.getBatchNo());
								tvrrldpo.setMaterialId(tvorrp.getMaterialId());
								tvrrldpo.setOldAmount(new Integer(0));
								tvrrldpo.setNewAmount(new Integer(count));
								tvrrldpo.setCreateBy(logonUser.getUserId());
								tvrrldpo.setCreateDate(new Date());

								logMap.put(tvorrp.getBatchNo(), tvrrldpo);
							}
							tvorrp.setOemCompanyId(logonUser.getCompanyId());
							tvorrp.setWarehouseId(new Long(warehouseId));
							tvorrp.setReserveType(Constant.RESERVE_TYPE_01);
							tvorrp.setCreateBy(logonUser.getUserId());
							tvorrp.setCreateDate(new Date());
							dao.insert(tvorrp);
						}
					}

					Iterator<String> iter = logMap.keySet().iterator();
					while (iter.hasNext()) {
						String key = iter.next();
						TtVsResourceReserveLogDtlPO tvrrldpo = (TtVsResourceReserveLogDtlPO) logMap
								.get(key);
						dao.insert(tvrrldpo); // 保存历史明细保存
					}
				}
			}

			// 调用发运存储过程
			List<Object> ins = new LinkedList<Object>();
			ins.add(tvrrlpo.getLogId().toString());

			List<Integer> outs = new LinkedList<Integer>();

			dao.callProcedure("P_BATCH_MODIFY", ins, outs);

			act.setOutData("returnValue", 1);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SAVE_FAILURE_CODE,
					"订单资源审核保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 批次号选择
	 */
	public void patchNoSelect() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String wareHouseId = CommonUtils.checkNull(request.getParamValue("wareHouseId"));// 仓库id
			String materalId = CommonUtils.checkNull(request.getParamValue("materalId")); // 订单数量
			String batchNo = CommonUtils.checkNull(request.getParamValue("batchNo")); // 物料id
			String amount = CommonUtils.checkNull(request.getParamValue("amount")); // 已配车数量
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 订单类型
			String specialBatchNo = CommonUtils.checkNull(request.getParamValue("specialBatchNo")); // 特殊批次号
			String initNo = CommonUtils.checkNull(request.getParamValue("initNo")); // 初始批次
			String reqAmount = CommonUtils.checkNull(request.getParamValue("reqAmount")); // 申请数量
			String reqId = CommonUtils.checkNull(request.getParamValue("reqId")); // 申请数量
			Long companyId = logonUser.getCompanyId();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("wareHouseId", wareHouseId);
			map.put("materalId", materalId);
			map.put("orderType", orderType);
			map.put("specialBatchNo", specialBatchNo);
			map.put("companyId", companyId.toString());
			map.put("reqId", reqId);

			List<Map<String, Object>> w_List = dao.getPatchNoSelectQueryForAdjust(map);

			act.setOutData("w_List", w_List);
			act.setOutData("batchNo", batchNo);
			act.setOutData("materalId", materalId);
			act.setOutData("amount", amount);
			act.setOutData("initNo", initNo);
			act.setOutData("reqAmount", reqAmount);
			act.setForword(DLVRY_RESOURCE_ADJUST_PATCH_NO_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"订单资源审核批次号选择");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
