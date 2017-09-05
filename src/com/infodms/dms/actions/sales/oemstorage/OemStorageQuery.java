/**********************************************************************
 * <pre>
 * FILE : OemStorageQuery.java
 * CLASS : OemStorageQuery
 * AUTHOR : 
 * FUNCTION : 车厂库存管理
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2010-06-07|            | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.actions.sales.oemstorage;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.oemstorage.OemStorageManageDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.dao.sales.ordermanage.reqquery.ReqQueryDao;
import com.infodms.dms.dao.sales.salesConsultant.SalesConsultantDAO;
import com.infodms.dms.dao.sales.storageManage.ReturnVehicleReqDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtStoDtlPO;
import com.infodms.dms.po.TtStoPO;
import com.infodms.dms.po.TtVsOrderResourceReservePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title:
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-7
 * @author
 * @mail
 * @version 1.0
 * @remark 车厂库存查询
 */
public class OemStorageQuery {

	public Logger logger = Logger.getLogger(OemStorageQuery.class);
	OemStorageManageDao dao = OemStorageManageDao.getInstance();
	private final OrderAuditDao auditDao = OrderAuditDao.getInstance();
	//private ReturnVehicleReqDao rvr = ReturnVehicleReqDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/oemstorage/oemStorageQuery.jsp";
	private final String detailUrl = "/jsp/sales/oemstorage/oemStorageQueryDetail.jsp";
	private final String moveUrl = "/jsp/sales/oemstorage/oemStorageMoveQuery.jsp";
	private final String addMoveUrl = "/jsp/sales/oemstorage/addStorageMoveQuery.jsp";
	private final String materUrl = "/jsp/sales/oemstorage/material.jsp";
	private final String infoViewUrl = "/jsp/sales/oemstorage/stoInfoView.jsp";
	private final String VHCL_INFO_INIT = "/jsp/sales/oemstorage/vhclInfo.jsp" ;

	/**
	 * 车厂库存查询页面初始化
	 */
	public void storageQueryInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			List<Map<String, Object>> warehouseList = auditDao.getWareHouseAllList(companyId.toString(), areaIds);
			String wareId = strAdd(warehouseList, "WAREHOUSE_ID") ;
			String wareName = strAdd(warehouseList, "WAREHOUSE_NAME") ;
			String wareCode = strAdd(warehouseList, "WAREHOUSE_CODE") ;
			
			List<Map<String, Object>> areaBusList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			List<Map<String, Object>> batchNOList = auditDao.getBatchNOList();
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				act.setOutData("sysFlag", "jc") ;
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				act.setOutData("sysFlag", "cvs") ;
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			
			act.setOutData("wareId", wareId) ;
			act.setOutData("wareName", wareName) ;
			act.setOutData("wareCode", wareCode) ;
			act.setOutData("warehouseList", warehouseList);
			act.setOutData("areaBusList", areaBusList);
			act.setOutData("batchNOList", batchNOList);
			act.setForword(initUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"车厂库存查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 车厂库存查询---汇总查询
	 */
	public void storageTotalQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// String areaId = request.getParamValue("areaId"); //业务范围ID
			// String area = request.getParamValue("area"); //业务范围IDS
			String warehouseId = request.getParamValue("warehouseId"); // 仓库ID
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String materialCode = request.getParamValue("materialCode"); // 物料CODE
			String batchNo = request.getParamValue("batchNo"); // 开始批次号
			String batchNoA = request.getParamValue("batchNoA"); // 结束批次号
			String vin = request.getParamValue("vin"); // VIN
			String days = request.getParamValue("days"); // 库龄天数
			String wareType = request.getParamValue("wareType") ;
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			if(null != batchNo && !"".equals(batchNo)) {
				batchNo = batchNo.length() == 4 ? batchNo + "000" : batchNo ;
			}
			if(null != batchNoA && !"".equals(batchNoA)) {
				batchNoA = batchNoA.length() == 4 ? batchNoA + "999" : batchNoA ;
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				PageResult<Map<String, Object>> ps = dao.getStorageTotal(wareType, logonUser.getPoseId().toString(), warehouseId, groupCode, materialCode, batchNo, batchNoA, vin, days, companyId, areaIds, curPage, 99999);
				act.setOutData("ps", ps);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				PageResult<Map<String, Object>> ps = dao.getStorageTotal_CVS(wareType, logonUser.getPoseId().toString(), warehouseId, groupCode, materialCode, batchNo, batchNoA, vin, days, companyId, areaIds, curPage, 99999);
				act.setOutData("ps", ps);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"车厂库存汇总查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 车厂库存查询---汇总查询--下载
	 */
	public void storageTotalDownLoad() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();
			Map map = new HashMap();
			// String areaId = request.getParamValue("areaId"); //业务范围ID
			// String area = request.getParamValue("area"); //业务范围IDS
			String warehouseId = request.getParamValue("warehouseId"); // 仓库ID
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String materialCode = request.getParamValue("materialCode"); // 物料CODE
			String batchNo = request.getParamValue("batchNo"); // 开始批次号
			String batchNoA = request.getParamValue("batchNoA"); // 结束批次号
			String vin = request.getParamValue("vin"); // VIN
			String days = request.getParamValue("days"); // 库龄天数
			String wareType = request.getParamValue("wareType"); 
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			if(null != batchNo && !"".equals(batchNo)) {
				batchNo = batchNo.length() == 4 ? batchNo + "000" : batchNo ;
			}
			if(null != batchNoA && !"".equals(batchNoA)) {
				batchNoA = batchNoA.length() == 4 ? batchNoA + "999" : batchNoA ;
			}
			// 导出的文件名
			String fileName = "车厂库存汇总.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 定义一个集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			// 标题
			List<Object> listTemp = new LinkedList<Object>();
			PageResult<Map<String, Object>> ps = null;
			ps = dao.getStorageTotal(wareType, logonUser.getPoseId().toString(), warehouseId, groupCode,
					materialCode, batchNo, batchNoA, vin, days, companyId, areaIds, 1, 99999);
			// listTemp.add("业务范围");
			listTemp.add("车系名称");
			listTemp.add("车型名称");
			// listTemp.add("配置代码");
			listTemp.add("状态名称");
			listTemp.add("物料代码");
			// listTemp.add("物料名称");
			listTemp.add("颜色");
			listTemp.add("批次号");
			listTemp.add("所在仓库");
			listTemp.add("移库在途数量");
			listTemp.add("库存量");
			listTemp.add("保留量");
			listTemp.add("可用库存");
			list.add(listTemp);
			// 将page对象转换成LIST形式
			List<Map<String, Object>> rslist = ps.getRecords();
			for (int i = 0; i < rslist.size(); i++) {
				map = rslist.get(i);
				List<Object> listValue = new LinkedList<Object>();
				listValue = new LinkedList<Object>();
				// listValue.add(map.get("AREA_NAME") != null ?
				// map.get("AREA_NAME") : "");
				listValue.add(map.get("SERIES_NAME") != null ? map.get("SERIES_NAME") : "");
				listValue.add(map.get("MODEL_NAME") != null ? map.get("MODEL_NAME") : "");
				// listValue.add(map.get("PACKAGE_CODE") != null ?
				// map.get("PACKAGE_CODE") : "");
				listValue.add(map.get("PACKAGE_NAME") != null ?
				map.get("PACKAGE_NAME") : "");
				listValue.add(map.get("MATERIAL_CODE") != null ? map.get("MATERIAL_CODE") : "");
				// listValue.add(map.get("MATERIAL_NAME") != null ?
				// map.get("MATERIAL_NAME") : "");
				listValue.add(map.get("COLOR_NAME") != null ? map.get("COLOR_NAME") : "");
				listValue.add(map.get("BATCH_NO") != null ? map.get("BATCH_NO") : "");
				listValue.add(map.get("WAREHOUSE_NAME") != null ? map.get("WAREHOUSE_NAME") : "");
				// listValue.add(map.get("SPECIAL_BATCH_NO") != null ?
				// map.get("SPECIAL_BATCH_NO") : "");
				listValue.add(map.get("MOVE_E_AMOUNT") != null ? map.get("MOVE_E_AMOUNT") : "");
				listValue.add(map.get("STOCK_AMOUNT") != null ? map.get("STOCK_AMOUNT") : "");
				listValue.add(map.get("RESERVE_AMOUNT") != null ? map.get("RESERVE_AMOUNT") : "");
				listValue.add(map.get("RESOURCE_AMOUNT") != null ? map.get("RESOURCE_AMOUNT") : "");
				list.add(listValue);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"车厂库存汇总查询下载");
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

	/**
	 * 车厂库存查询---明细查询
	 */
	public void storageDetailQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// String areaId = request.getParamValue("areaId"); //业务范围ID
			// String area = request.getParamValue("area"); //业务范围IDS
			String warehouseId = request.getParamValue("warehouseId"); // 仓库ID
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String materialCode = request.getParamValue("materialCode"); // 物料CODE
			String custBatch = request.getParamValue("custBatch"); // 批次号
			String batchNo = request.getParamValue("batchNo"); // 开始批次号
			String batchNoA = request.getParamValue("batchNoA"); // 结束批次号
			String vin = request.getParamValue("vin"); // VIN
			String days = request.getParamValue("days"); // 库龄天数
			String wareType = request.getParamValue("wareType"); 
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			if(null != batchNo && !"".equals(batchNo)) {
				batchNo = batchNo.length() == 4 ? batchNo + "000" : batchNo ;
			}
			if(null != batchNoA && !"".equals(batchNoA)) {
				batchNoA = batchNoA.length() == 4 ? batchNoA + "999" : batchNoA ;
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getStorageDetail(wareType, batchNo, batchNoA, logonUser.getPoseId().toString(), warehouseId, groupCode, materialCode, custBatch, vin, days, companyId, areaIds, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"车厂库存明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 车厂库存查询---明细查询--下载
	 */
	public void storageDetailDownLoad() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			ResponseWrapper response = act.getResponse();
			Map map = new HashMap();
			// String areaId = request.getParamValue("areaId"); //业务范围ID
			// String area = request.getParamValue("area"); //业务范围IDS
			String warehouseId = request.getParamValue("warehouseId"); // 仓库ID
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String materialCode = request.getParamValue("materialCode");// 物料CODE
			String custBatch = request.getParamValue("custBatch"); // 批次号
			String batchNo = request.getParamValue("batchNo"); // 开始批次号
			String batchNoA = request.getParamValue("batchNoA"); // 结束批次号
			String vin = request.getParamValue("vin"); // VIN
			String days = request.getParamValue("days"); // 库龄天数
			String wareType = request.getParamValue("wareType"); 
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			if(null != batchNo && !"".equals(batchNo)) {
				batchNo = batchNo.length() == 4 ? batchNo + "000" : batchNo ;
			}
			if(null != batchNoA && !"".equals(batchNoA)) {
				batchNoA = batchNoA.length() == 4 ? batchNoA + "999" : batchNoA ;
			}
			// 导出的文件名
			String fileName = "车厂库存明细.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 定义一个集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			// 标题
			List<Object> listTemp = new LinkedList<Object>();
			PageResult<Map<String, Object>> ps = null;
			ps = dao.getStorageDetail(wareType, batchNo, batchNoA, logonUser.getPoseId().toString(), warehouseId,
					groupCode, materialCode, custBatch, vin, days, companyId, areaIds, 1, 99999);
			listTemp.add("物料代码");
			listTemp.add("物料名称");
			listTemp.add("颜色");
			listTemp.add("订做车批次");
			listTemp.add("底盘号");
			listTemp.add("批次号");
			listTemp.add("所在仓库");
			listTemp.add("入库日期");
			listTemp.add("库龄(天)");
			list.add(listTemp);
			// 将page对象转换成LIST形式
			List<Map<String, Object>> rslist = ps.getRecords();
			for (int i = 0; i < rslist.size(); i++) {
				map = rslist.get(i);
				List<Object> listValue = new LinkedList<Object>();
				listValue = new LinkedList<Object>();
				listValue.add(map.get("MATERIAL_CODE") != null ? map.get("MATERIAL_CODE") : "");
				listValue.add(map.get("MATERIAL_NAME") != null ? map.get("MATERIAL_NAME") : "");
				listValue.add(map.get("COLOR_NAME") != null ? map.get("COLOR_NAME") : "");
				listValue.add(map.get("SPECIAL_BATCH_NO") != null ? map.get("SPECIAL_BATCH_NO")
						: "");
				listValue.add(map.get("VIN") != null ? map.get("VIN") : "");
				listValue.add(map.get("BATCH_NO")!=null?map.get("BATCH_NO"):"");
				listValue.add(map.get("WAREHOUSE_NAME") != null ? map.get("WAREHOUSE_NAME") : "");
				listValue.add(map.get("STORAGE_DATE") != null ? map.get("STORAGE_DATE") : "");
				listValue.add(map.get("DAYS") != null ? map.get("DAYS") : "");
				list.add(listValue);
			}
			OutputStream os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
			os.close();
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"车厂库存明细查询下载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 车厂库存明细
	 */
	public void viewDetail() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String materalId = CommonUtils.checkNull(request.getParamValue("materalId")); // 订单数量
			String type = CommonUtils.checkNull(request.getParamValue("type")); // 类型
			String warehouseId = CommonUtils.checkNull(request.getParamValue("warehouseId")); // 仓库ID
			Long companyId = logonUser.getCompanyId();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("materalId", materalId);
			map.put("type", type);
			map.put("companyId", companyId.toString());
			map.put("warehouseId", warehouseId);

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if (type.equals("1")) {
				list = dao.getStockDetail(map);
			} else if (type.equals("2")) {
				list = dao.getReserveDetail(map);
			} else {
				list = dao.getAvaDetail(map);
			}

			act.setOutData("list", list);
			act.setForword(detailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"经销商订单审核：查询要审核订单详细信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 移库查询
	 */
	public void storageMoveQueryInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			List<Map<String, Object>> list = dao.getWarehouseEnable(companyId, areaIds);
			List<Map<String, Object>> list_aim = dao.getWarehouseEnable_Aim(companyId, areaIds);
			
			/*if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
				boolean flag = dao.checkQuate(logonUser.getOrgId().toString()) ;
				act.setOutData("flag", flag) ;
			}*/
			
			act.setOutData("list", list);
			act.setOutData("list_aim", list_aim);
			
			act.setForword(moveUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "移库查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 新增移库单
	 */
	public void addMoveSto() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			List<Map<String, Object>> list = dao.getWarehouseEnable(companyId, areaIds);// 仓库列表
			List<Map<String, Object>> list_aim = dao.getWarehouseEnable_Aim(companyId, areaIds);
			
			act.setOutData("list", list);
			act.setOutData("list_aim", list_aim);
			
			act.setForword(addMoveUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "移库查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 价格列表联动
	 */
	public void selPrice() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String endName = request.getParamValue("endName");
			String dealerId = dao.getDealerIdByEndName(endName);
			String companyId = String.valueOf(GetOemcompanyId.getOemCompanyId(logonUser));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("companyId", companyId);
			map.put("dealerId", dealerId);
			List<Map<String, Object>> priceList = dao.getPriceList(map);
			act.setOutData("priceList", priceList);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "移库查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 添加物料
	 */
	public void addMaterial() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String stName = CommonUtils.checkNull(request.getParamValue("stName"));// 发运仓库ID
			act.setOutData("stName", stName);
			act.setForword(materUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "移库查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 添加物料
	 */
	public void addMaterialList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String stName = CommonUtils.checkNull(request.getParamValue("stName"));// 发运仓库ID
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String materialName = CommonUtils.checkNull(request.getParamValue("materialName"));
			String batchNo = CommonUtils.checkNull(request.getParamValue("batchNo"));

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getMaterialList(stName, materialCode,
					materialName, batchNo, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "移库查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 根据物料ID查询单价，目的库数量
	 */
	public void selOthers() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));
			String valid = CommonUtils.checkNull(request.getParamValue("valid"));
			String endName = CommonUtils.checkNull(request.getParamValue("endName"));
			String singlePrice = dao.getSinglePrice(priceId, valid);
			String mcount = dao.getMCount(valid, endName);
			Map<String, Object> map = new HashMap<String, Object>();
			if (Utility.testString(singlePrice) || Utility.testString(mcount)) {
				map.put("singlePrice", singlePrice);
				map.put("mcount", mcount);
				act.setOutData("map", map);
			} else {
				map.put("singlePrice", null);
				map.put("mcount", null);
				act.setOutData("map", map);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "移库查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void changePrice() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));
			String codes = CommonUtils.checkNull(request.getParamValue("code"));
			if (!codes.equals("")) {// //截串加单引号
				String[] supp = codes.split(",");
				codes = "";
				for (int i = 0; i < supp.length; i++) {
					supp[i] = "'" + supp[i] + "'";
					if (!codes.equals("")) {
						codes += "," + supp[i];
					} else {
						codes = supp[i];
					}
				}
			}
			List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
			List<Map<String, Object>> singList = new LinkedList<Map<String, Object>>();
			list = dao.getvalids(codes);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String singlePrice = dao.getSinglePrice(priceId, String.valueOf(list.get(i)
							.get("MATERIAL_ID")));
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("singlePrice", singlePrice);
					singList.add(map);
				}
			}
			act.setOutData("singList", singList);

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "移库查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void InsertSTO() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String startName = request.getParamValue("startName");
			String endName = request.getParamValue("endName");
			String priceId = request.getParamValue("priceId");
			String dealerId = dao.getDealerIdByEndName(endName);
			boolean infoFlag = false ;
			
			TmDealerPO dealerPo = new TmDealerPO();
			TmDealerPO dealerPo1 = new TmDealerPO();
			dealerPo.setDealerId(Long.parseLong(dealerId));
			if (dao.select(dealerPo).size() > 0) {
				dealerPo1 = (TmDealerPO) dao.select(dealerPo).get(0);
			}
			StringBuffer beginNo = new StringBuffer("Y");
			String dealerCode = dealerPo1.getDealerCode();
			beginNo.append(dealerCode);
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
			String dStr = fmt.format(new Date());
			beginNo.append(dStr);
			String sql = " select * from tt_sto ts where ts.order_no like '%" + beginNo
					+ "%' order by ts.order_no desc";
			String end = "";
			List<TtStoPO> list = dao.select(TtStoPO.class, sql, null);
			if (list.size() > 0) {
				int a = list.get(0).getOrderNo().length();
				end = String
						.valueOf(Integer.parseInt(list.get(0).getOrderNo().substring(a - 4)) + 1);
				if (end.length() == 1)
					end = "000" + end;
				if (end.length() == 2)
					end = "00" + end;
				if (end.length() == 3)
					end = "0" + end;
			} else {
				end = "0001";
			}

			beginNo.append(end);
			System.out.println(beginNo);
			String cd = request.getParamValue("cd");
			String bo = request.getParamValue("bo");
			String at = request.getParamValue("at");
			String se = request.getParamValue("se");
			String te = request.getParamValue("te");
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TtStoPO po = new TtStoPO();
			po.setOrderNo(beginNo.toString());
			po.setStoId(Long.parseLong(SequenceManager.getSequence("")));
			po.setCompanyId(companyId);
			po.setFromWarehouseId(Long.parseLong(startName));
			po.setToWarehouseId(Long.parseLong(endName));
			po.setStoDate(new Date());
			po.setStatus(Integer.parseInt(Constant.STO_STATUS_01));
			po.setPriceId(Long.parseLong(priceId));
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(po.getStoDate());
			dao.insert(po);// 插入主表
			String[] cdd = null;
			String[] boo = null;
			String[] att = null;
			String[] see = null;
			String[] tee = null;
			if (cd != "" && cd != null) {
				cdd = cd.split(",");
			}
			if (bo != "" && bo != null) {
				boo = bo.split(",");
			}
			if (at != "" && at != null) {
				att = at.split(",");
			}
			if (se != "" && se != null) {
				see = se.split("-");
			}
			if (te != "" && te != null) {
				tee = te.split("-");
			}
			
			for (int i = 0; i < cdd.length; i++) {
				String[] booo = boo[i].split("-");
				
				infoFlag = this.checkAvailableResource(startName, dao.getmaterialIdByCode(cdd[i]), booo[0].trim(), Integer.parseInt(att[i])) ;
				
				if(!infoFlag) {
					throw new RuntimeException("移库数量不能大于库存数量") ;
				}
				
				TtStoDtlPO tp = new TtStoDtlPO();
				tp.setDtlId(Long.parseLong(SequenceManager.getSequence("")));
				tp.setStoId(po.getStoId());
				tp.setMaterialId(dao.getmaterialIdByCode(cdd[i]));
				tp.setBatchNo(booo[0].trim());
				if (att != null) {
					tp.setAmount(Integer.parseInt(att[i]));
				}
				if (see != null) {
					tp.setSinglePrice(Double.parseDouble(see[i].replace(",", "")));
				}
				if (tee != null) {
					tp.setTotalPrice(Double.parseDouble(tee[i].replace(",", "")));
				}
				dao.insert(tp);// 插入子表

				TtVsOrderResourceReservePO vo = new TtVsOrderResourceReservePO();
				vo.setReserveId(Long.parseLong(SequenceManager.getSequence("")));
				vo.setReqDetailId(tp.getDtlId());
				vo.setMaterialId(tp.getMaterialId());
				vo.setBatchNo(tp.getBatchNo());
				vo.setAmount(tp.getAmount());
				vo.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_01);
				vo.setOemCompanyId(companyId);
				vo.setWarehouseId(po.getFromWarehouseId());
				vo.setStoId(po.getStoId());
				vo.setReserveType(Constant.RESERVE_TYPE_02);
				vo.setCreateBy(logonUser.getUserId());
				vo.setCreateDate(new Date());
				dao.insert(vo);// 插入资源保留表
			}
			
			// 调用发运存储过程
			List<Object> ins = new LinkedList<Object>();
			ins.add(po.getStoId().toString());
	
			List<Integer> outs = new LinkedList<Integer>();
	
			dao.callProcedure("P_INSERTSTO_TO_ERP", ins, outs);
	
			act.setOutData("returnValue", 1);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		}
		catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "移库操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void storageStoQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String startName = CommonUtils.checkNull(request.getParamValue("startName"));
			String stoStatus = CommonUtils.checkNull(request.getParamValue("stoStatus"));
			String endName = CommonUtils.checkNull(request.getParamValue("endName"));
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			String ERP_ORDER_NO = CommonUtils.checkNull(request.getParamValue("ERP_ORDER_NO")); //YH 2011.5.20
			String orgId = "" ;
			/*String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}*/
			
			if (Utility.testString(beginTime)) {
				beginTime = beginTime + " 00:00:00";
			}
			if (Utility.testString(endTime)) {
				endTime = endTime + "23:59:59";
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			
			PageResult<Map<String, Object>> ps = dao.getSTOList(orgId, companyId, beginTime, endTime, startName, stoStatus, endName, areaIds,ERP_ORDER_NO ,curPage, Constant.PAGE_SIZE);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "移库查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void selInfoView() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			Map<String, Object> map = dao.getSTOMap(id);
			List<Map<String, Object>> list = dao.getSTOInfo(id);
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setForword(infoViewUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "移库查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void stoGoBack() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			TtStoPO po = new TtStoPO();
			TtStoPO tp = new TtStoPO();
			po.setStoId(Long.parseLong(id));
			tp.setStatus(Integer.parseInt(Constant.STO_STATUS_04));
			tp.setUpdateBy(logonUser.getUserId());
			tp.setUpdateDate(new Date());
			dao.update(po, tp);// 修改取消的状态
			
			ReturnVehicleReqDao rvr = new ReturnVehicleReqDao() ;
			rvr.moveOrderCancel(id) ;
			
			act.setOutData("returnValue", 1);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "移库查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public boolean checkAvailableResource(String warehoursId, Long materialId, String bathNo, Integer amount) {
		ReturnVehicleReqDao rvr = new ReturnVehicleReqDao() ;
		Map<String, Object> map = rvr.checkAvailableResource(warehoursId, materialId, bathNo) ;
		
		if(!CommonUtils.isNullMap(map)) {
			 Double checkAmount = new Double(map.get("AVA_STOCK").toString()) - amount ;
			 
			 if(checkAmount < 0) {
				 return false ;
			 } else {
				 return true ;
		 }
		} else {
			return false ;
		}
	}
	
	public void getVhcl() {
		String stoId = CommonUtils.checkNull(request.getParamValue("stoId")) ;
		
		if(!CommonUtils.isNullString(stoId)) {
			OemStorageManageDao osm = new OemStorageManageDao() ;
			List<Map<String, Object>> vhclList = osm.getVhcl(stoId) ;
			
			act.setOutData("vhclList", vhclList) ;
		}
		
		act.setForword(VHCL_INFO_INIT) ;
	}
	
	public String strAdd(List<Map<String, Object>> list, String para) {
		StringBuffer str = new StringBuffer("") ;
		
		int len = list.size() ;
		
		for(int i=0; i<len; i++) {
			if(str.length() == 0) {
				str.append(list.get(i).get(para)) ;
			} else {
				str.append(",").append(list.get(i).get(para)) ;
			}
		}
		
		return str.toString() ;
	}
}
