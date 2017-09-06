package com.infodms.dms.actions.sales.storage.sendmanage;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.StorageUtil;
import com.infodms.dms.dao.sales.storage.sendManage.DlvPlanManageDao;
import com.infodms.dms.dao.sales.storage.sendManage.DlvWayBillManageDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardSeachDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendDriverSeachDao;
import com.infodms.dms.dao.sales.storage.sendManage.TtVsDlvryErpDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.dao.sales.storage.storagemanage.ReceivingStorageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TmpTmVehiclePO;
import com.infodms.dms.po.TmpTtSalesStoragePO;
import com.infodms.dms.po.TmpTtSalesWayBillPO;
import com.infodms.dms.po.TsiExpBusVehStoreDetPO;
import com.infodms.dms.po.TsiExpBusVehStorePO;
import com.infodms.dms.po.TtSalesBoDetailPO;
import com.infodms.dms.po.TtSalesBoardPO;
import com.infodms.dms.po.TtSalesCityDisPO;
import com.infodms.dms.po.TtSalesWayBillDtlPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.po.TtVsDispatchOrderPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 发运交接单管理
 * @author shuyh
 * 2017/8/2
 */
public class DlvWayBillManage  extends BaseImport {
	public Logger logger = Logger.getLogger(DlvWayBillManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final DlvWayBillManageDao reDao = DlvWayBillManageDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final SendBoardSeachDao ssDao = SendBoardSeachDao.getInstance();
	private final SendBoardDao sbDao = SendBoardDao.getInstance();
	private final ReceivingStorageDao rsdao = ReceivingStorageDao.getInstance();
	private final TtVsDlvryErpDao teDao=TtVsDlvryErpDao.getInstance();
	private final String dlvBillManageInitUtl = "/jsp/sales/storage/sendmanage/dlvBill/dlvBillManageList.jsp";
	private final String seachInitUtl = "/jsp/sales/storage/sendmanage/dlvBill/dlvBillManageDel.jsp";
	private final String importUrl = "/jsp/sales/storage/sendmanage/dlvBill/dlvBillImport.jsp";
	/** 导入成功跳转页面 */
	private static final String BILL_IMPORT_SUCCESS_URL = "/jsp/sales/storage/sendmanage/dlvBill/importBillSucess.jsp";
	/*** 导入失败跳转页面 */
	private static final String BILL_IMPORT_FAIL_URL = "/jsp/sales/storage/sendmanage/dlvBill/importBillFailure.jsp";
	private final String dlvbillPrintUtl = "/jsp/sales/storage/sendmanage/dlvBill/printDlvWaybill.jsp";//交接单打印

	private final String dlvBillQueryInitUtl = "/jsp/sales/storage/sendmanage/dlvBill/dlvBillQueryList.jsp";
	/**
	 * 发运交接单管理初始化
	 */
	public void wayBillManaQueryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=reLDao.getLogiByWarehouse(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//产地LIST
			
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			act.setForword(dlvBillManageInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发运计划发送初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 发运交接单管理查询
	 */
	public void DlvBillManageQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String billNo = CommonUtils.checkNull(request.getParamValue("billNo")); //交接单号
			String orderNo= CommonUtils.checkNull(request.getParamValue("orderNo"));//订单号
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); //发运仓库
			String dealerCode= CommonUtils.checkNull(request.getParamValue("dealerCode"));//经销商ID
			String transType = CommonUtils.checkNull(request.getParamValue("TRANSPORT_TYPE")); //发运方式
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //承运商ID
			String provinceId = CommonUtils.checkNull(request.getParamValue("jsProvince")); //结算省份
			String cityId = CommonUtils.checkNull(request.getParamValue("jsCity")); // 结算城市
			String countyId = CommonUtils.checkNull(request.getParamValue("jsCounty")); // 结算区县
			String startDate = CommonUtils.checkNull(request.getParamValue("START_DATE")); //交接日期开始
			String endDate = CommonUtils.checkNull(request.getParamValue("END_DATE")); // 交接日期结束
			
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); //组板号
			String billStatus = CommonUtils.checkNull(request.getParamValue("BILL_STATUS")); //交接单状态
			String lastStartDate = CommonUtils.checkNull(request.getParamValue("LAST_START_DATE")); //最后交车日期开始
			String lastEndDate = CommonUtils.checkNull(request.getParamValue("LAST_END_DATE")); //最后交车日期结束
			
			String pFlag = CommonUtils.checkNull(request.getParamValue("pFlag")); // 不为空表示发运计划查询
			String common= CommonUtils.checkNull(request.getParamValue("common"));//1表示导出
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("billNo", billNo);
			map.put("orderNo", orderNo);
			map.put("yieldly", yieldly);
			map.put("dealerCode", dealerCode);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("transType", transType);
			map.put("logiName", logiName);
			map.put("provinceId", provinceId);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("boNo", boNo);
			map.put("billStatus", billStatus);
			map.put("lastStartDate", lastStartDate);
			map.put("lastEndDate", lastEndDate);
			
			map.put("provinceId", provinceId);
			map.put("cityId", cityId);
			map.put("countyId", countyId);
			//map.put("pFlag", pFlag);
			//根据职位ID获取是否属于物流商以及物流商ID
			Map<String, Object> pmap=sbDao.getPoseLogiById(logonUser.getPoseId().toString());
			map.put("posBusType", pmap.get("POSE_BUS_TYPE").toString());
			map.put("logiId", (BigDecimal)pmap.get("LOGI_ID"));
			if(common.equals("1")){//导出
				List<Map<String, Object>> list = reDao.getDlvBillQueryExport(map);
				String [] head={"交接单号","订单号","发运仓库","经销商","发运方式","承运商","发运结算地","组板号","交接量","最晚发运日期","最晚到货日期","状态","交接日期","最后交车时间"};
				String [] cols={"BILL_NO","ORDER_NO","WH_NAME","DEALER_NAME","SHIP_NAME","LOGI_NAME","BAL_ADDR","BO_NO","VEH_NUM","DLV_FY_DATE","DLV_JJ_DATE","STATUS_NAME","BILL_CRT_DATE","LAST_CAR_DATE"};//导出的字段名称
				ToExcel.toReportExcel(act.getResponse(),request, "交接单查询列表.xls",head,cols,list);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getDlvBillManaQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
			
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运组板信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 交接单管理查看详情初始化
	 */
	public void showBillDetailInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String billId = CommonUtils.checkNull(request.getParamValue("billId"));//交接单ID
			List<Object> params=new ArrayList<Object>();
			params.add(billId);
			Map<String,Object> map=reDao.getViewMainByBillId(params);//主信息
			List<Map<String, Object>> list = reDao.getBillMatListQuery(params);//明细信息
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setForword(seachInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发运计划发送查看详情初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 交接单导入跳转初始化
	 */
	public void toImportWayBillInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			act.setForword(importUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"交接单导入跳转初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 下载交接单导入模板
	 */
	public void downloadTemplate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			// 用于下载传参的
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();
			String[] titles = {"组板号*","订单号*","车型","配置","颜色","物料代码*","原车架号*","替换车架号"};
			for(String title : titles){
				listHead.add(title);
			}
			list.add(listHead);
			
			// 导出的文件名
			String fileName = "交接单管理导入模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
			
			
			CsvWriterUtil.createXlsFile(list, os);
			
			
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
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
	 * 交接单导入
	 */
	public void billImportConfirm() {
		//initSet();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			// 清空临时表中目标的数据
			reDao.update(" truncate table TMP_TT_SALES_WAY_BILL", null);

			long maxSize = 1024 * 1024 * 5;
			insertIntoTmp(request, "uploadFile", 8, 3, maxSize);
			List<ExcelErrors> el = getErrList();

			if (null != el && el.size() > 0) {
				act.setOutData("errorList", el);
				act.setForword(BILL_IMPORT_FAIL_URL);
			} else {
				List<Map> list = getMapList();
				// 将数据插入临时表
				insertTmpTmBill(list, logonUser);
				// 校验临时表数据
				List<ExcelErrors> errorList = null;
				errorList = checkData(logonUser.getUserId().toString(),logonUser.getCompanyId().toString(),
						logonUser.getPoseId());

				if (null != errorList) {
					act.setOutData("errorList", errorList);
					act.setForword(BILL_IMPORT_FAIL_URL);
				} else {
					act.setForword(BILL_IMPORT_SUCCESS_URL);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE, "文件读取错误");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 遍历excel，将数据插入车辆信息表
	 * @param list
	 * @param logonUser
	 */
	private void insertTmpTmBill(List<Map> list, AclUserBean logonUser) {
		if (null == list) {
			list = new ArrayList();
		}
		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			if (null == map) {
				map = new HashMap<String, Cell[]>();
			}
			Set<String> keys = map.keySet();
			Iterator it = keys.iterator();
			String key = "";
			while (it.hasNext()) {
				key = (String) it.next();
				Cell[] cells = (Cell[]) map.get(key);
				parseCells(key, cells, logonUser);
			}
		}

	}
	/** 解析某一列的数据 **/
	private void parseCells(String rowNum, Cell[] cells, AclUserBean logonUser) {
		TmpTtSalesWayBillPO po = new TmpTtSalesWayBillPO();
		po.setRowNumber(rowNum.trim());
		try {
			po.setBoNo(cells.length >= 1 ? subCell(cells[0].getContents().trim()) : "");
			po.setOrderNo(cells.length >= 2 ? subCell(cells[1].getContents().trim()) : "");
			po.setModelName(cells.length >= 3 ? subCell(cells[2].getContents().trim()) : "");
			po.setPakageName(cells.length >= 4 ? subCell(cells[3].getContents().trim()) : "");
			po.setColorName(cells.length >= 5 ? subCell(cells[4].getContents().trim()) : "");
			po.setMaterialCode(cells.length >= 6 ? subCell(cells[5].getContents().trim()) : "");
			po.setVin(cells.length >= 7 ? subCell(cells[6].getContents().trim()) : "");
			po.setNewVin(cells.length >= 8 ? subCell(cells[7].getContents().trim()) : "");
			po.setCreateDate(new Date());
			po.setCreateBy(logonUser.getUserId());
			reDao.insert(po);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运交接单导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/* 将输入字符截取最多30位 */
	private String subCell(String orgAmt) throws Exception {
		String newAmt = "";
		if (null == orgAmt || "".equals(orgAmt)) {
			return newAmt;
		}
		if (orgAmt.length() > 30) {
			newAmt = orgAmt.substring(0, 30);
		} else {
			newAmt = orgAmt;
		}
		return newAmt;
	}
	/**
	 * 检查交接单导入信息是否符合规范
	 * @param userId
	 * @param companyId
	 * @param poseId
	 * @return
	 */
	private List<ExcelErrors> checkData(String userId,String companyId,Long poseId) {
		TmpTtSalesWayBillPO selectPo = new TmpTtSalesWayBillPO();
		selectPo.setCreateBy(Long.valueOf(userId));
		List pos = reDao.select(selectPo);
		ExcelErrors errors = null;

		StringBuffer errorInfo = new StringBuffer("");
		boolean isError = false;
		TmpTtSalesWayBillPO po;
		List<ExcelErrors> errorList = new LinkedList<ExcelErrors>();
		if (pos == null || pos.size() > 600) {
			isError = true;
			errorInfo.append("导入数据量异常,每次导入数量不能超过600条！");
			String info = errorInfo.substring(0, errorInfo.length() - 1);
			errors = new ExcelErrors();
			errors.setRowNum(1);
			errors.setErrorDesc(info);
			errorList.add(errors);
			errorInfo.delete(0, errorInfo.length());
		} else {
			String errorInfo2="";
			//原车架号判重
			List<Map<String, Object>> vlist=reDao.checkVinRepeat();
			 if(vlist!=null&&vlist.size()>0){//获取错误行号
			 	isError = true;
			 	errorInfo2="原车架号导入重复；";
			 	for(int i=0;i<vlist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=vlist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			 	
			}
			//替换车架号判重
			List<Map<String, Object>> vnlist=reDao.checkNewVinRepeat();
			 if(vnlist!=null&&vnlist.size()>0){//获取错误行号
			 	isError = true;
			 	errorInfo2="替换车架号导入重复；";
			 	for(int i=0;i<vnlist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=vnlist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			 	
			}
			//检查组板号
			List<Map<String, Object>> slist=reDao.checkBoNo("0");
			 if(slist!=null&&slist.size()>0){//获取错误行号
			 	isError = true;
			 	errorInfo2="组板号为空或不存在；";
			 	for(int i=0;i<slist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=slist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			 	
			 }else{//检验是否已生成发运计划
				 List<Map<String, Object>> mlist=reDao.checkBoNo("1");
				 if(mlist!=null&&mlist.size()>0){//获取错误行号
				 	isError = true;
				 	errorInfo2="组板号还未生成发运计划；";
				 	for(int i=0;i<mlist.size();i++){
				 		errors = new ExcelErrors();
				 		Map<String,Object> map=mlist.get(i);
					 	String info = errorInfo2;
						errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
						errors.setErrorDesc(info);
						errorList.add(errors);
				 	}
				 }
				 
			 }
			//检查订单号
			List<Map<String, Object>> olist=reDao.checkOrderNo("0");
			 if(olist!=null&&olist.size()>0){//获取错误行号
			 	isError = true;
			 	errorInfo2="订单号为空或不存在；";
			 	for(int i=0;i<olist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=olist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			 	
			 }
//			 else{//检验对应订单号是否已发运
//				 List<Map<String, Object>> mlist=reDao.checkOrderNo("1");
//				 if(mlist!=null&&mlist.size()>0){//获取错误行号
//				 	isError = true;
//				 	errorInfo2="订单号对应订单未发运；";
//				 	for(int i=0;i<mlist.size();i++){
//				 		errors = new ExcelErrors();
//				 		Map<String,Object> map=mlist.get(i);
//					 	String info = errorInfo2;
//						errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
//						errors.setErrorDesc(info);
//						errorList.add(errors);
//				 	}
//				 }
//					 
//			}
			//根据组板号和订单号校验是否对应
			 List<Map<String, Object>> blist=reDao.checkIsMatchOrder();
			 if(blist!=null&&blist.size()>0){//获取错误行号
			 	isError = true;
			 	errorInfo2="组板号和订单号不匹配；";
			 	for(int i=0;i<blist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=blist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			 }
			//检查物料代码
			List<Map<String, Object>> clist=reDao.getMatarielByCode();
			if(clist!=null&&clist.size()>0){//获取错误行号
			 	isError = true;
			 	errorInfo2="物料代码为空或填写错误；";
			 	for(int i=0;i<clist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=clist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			}
			//检查物料代码和订单号是否匹配
			List<Map<String, Object>> elist=reDao.checkIsMatchOrderMat();
			if(elist!=null&&elist.size()>0){//获取错误行号
			 	isError = true;
			 	errorInfo2="物料代码和订单号不匹配；";
			 	for(int i=0;i<elist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=elist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			}
			//检查原车架号和物料代码是否匹配
			List<Map<String, Object>> dlist=reDao.checkIsMatchVin("0");
			if(dlist!=null&&dlist.size()>0){//获取错误行号
			 	isError = true;
			 	errorInfo2="物料代码和原车架号不匹配；";
			 	for(int i=0;i<dlist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=dlist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			}
			//检查替换车架号和物料代码是否匹配
			List<Map<String, Object>> flist=reDao.checkIsMatchVin("1");
			if(flist!=null&&flist.size()>0){//获取错误行号
			 	isError = true;
			 	errorInfo2="物料代码和替换车架号不匹配；";
			 	for(int i=0;i<flist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=flist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			}
			//检查原车架号对应仓库与订单的发运仓库是否匹配
			List<Map<String, Object>> hlist=reDao.checkIsMatchWare("");
			if(hlist!=null&&hlist.size()>0){//获取错误行号
			 	isError = true;
			 	errorInfo2="原车架号所属仓库与订单发运仓库不匹配；";
			 	for(int i=0;i<hlist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=hlist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			}
			//检查替换车架号对应仓库与订单的发运仓库是否匹配
			List<Map<String, Object>> mlist=reDao.checkIsMatchWare("1");
			if(mlist!=null&&mlist.size()>0){//获取错误行号
			 	isError = true;
			 	errorInfo2="替换车架号所属仓库与订单发运仓库不匹配；";
			 	for(int i=0;i<mlist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=mlist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			}
			for (int i = 0; i < pos.size(); i++) {
				errors = new ExcelErrors();
				po = (TmpTtSalesWayBillPO) pos.get(i);
				// 取得行号
				String rowNum = po.getRowNumber();
	
				try {
					//判断原车架号和替换车架号是否相同
					if(po.getVin()!=null&&!"".equals(po.getVin())&&po.getNewVin()!=null&&!"".equals(po.getNewVin())){
						if(po.getVin().equals(po.getNewVin())){
							isError = true;
							errorInfo.append("原车架号和替换车架号相同;");
						}
					}
					//检验原车架号
					checkVin(errorInfo, po, isError, poseId);
					//检验替换车架号
					checkVinNew(errorInfo, po, isError, poseId);
				} catch (Exception e) {
					e.printStackTrace();
					isError = true;
				}
	
				if (errorInfo.length() > 0) {
					String info = errorInfo.substring(0, errorInfo.length() - 1);
					errors.setRowNum(new Integer(rowNum));
					errors.setErrorDesc(info);
					errorList.add(errors);
					errorInfo.delete(0, errorInfo.length());
				}
			}
		}
		if(isError	== false){
			//获取车辆数量不等于组板数量的数据
			List<Map<String, Object>> glist=reDao.checkTmpSubNum(userId);
			if(glist!=null&&glist.size()>0){//获取错误行号
				String errorInfo2="";
			 	isError = true;
			 	errorInfo2="交接车辆数与组板数不相等；";
			 	for(int i=0;i<glist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=glist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			}
		}
		if (errorList.size() > 0) {
			return errorList;
		} else {
			return null;
		}

	}
	//判断原车架号
	private void checkVin(StringBuffer errorInfo, TmpTtSalesWayBillPO po, boolean isError, Long posId) throws Exception {
		String vin = po.getVin();
		if(null == vin ||  "".equals(vin)){
			isError = true;
			errorInfo.append("原车架号不能为空;");
		}else{
			if(vin.length() != 17){
				isError = true;
				errorInfo.append("原VIN长度不正确，必须为17位;");
			}else{
				List<Map<String, Object>>  mapList = reDao.getImportVin(vin,posId);
				if(null != mapList && !mapList.isEmpty()){
				   if(mapList.size() ==1){
					   
					   if(mapList.get(0).get("FLAG").toString().equals("0")) {
						   isError = true;
						   errorInfo.append("职位无产地权限;");
					   }
					   if(po.getNewVin()!=null&&!"".equals(po.getNewVin())){//替换车架号非空，则原车架号需要为在途，否则为在库
						   //根据订单号判断发运类型，若为调拨单，原车架号需要为车厂调拨在途，否则为车厂出库
						   TtVsDlvryPO tvp=new TtVsDlvryPO();
						   tvp.setOrdNo(po.getOrderNo());
						   tvp=(TtVsDlvryPO) reDao.select(tvp).get(0);
						   if(tvp.getDlvType().toString().equals(Constant.DELIVERY_ORD_TYPE_ALLOCAT.toString())){//调拨单
							   if(!mapList.get(0).get("LIFE_CYCLE").toString().equals(String.valueOf(Constant.VEHICLE_LIFE_12))) {
								   isError = true;
								   errorInfo.append("针对调拨单在途换车，原车架号'"+mapList.get(0).get("VIN")+"'必须为车厂调拨在途状态;");
							   }
						   }else{
							   if(!mapList.get(0).get("LIFE_CYCLE").toString().equals(String.valueOf(Constant.VEHICLE_LIFE_08))) {
								   isError = true;
								   errorInfo.append("原车架号'"+mapList.get(0).get("VIN")+"'非出库状态;");
							   }
						   }
						   
					   }else{
						   if(!mapList.get(0).get("LIFE_CYCLE").toString().equals(String.valueOf(Constant.VEHICLE_LIFE_02))) {
							   isError = true;
							   errorInfo.append("原车架号'"+mapList.get(0).get("VIN")+"'非在库状态;");
						   }
					   }
					   
				   }else{
					   isError = true;
					   errorInfo.append("原车架号填写错误;");
				   }
				}
			}
			
		}
		
	}
	//判断替换车架号
	private void checkVinNew(StringBuffer errorInfo, TmpTtSalesWayBillPO po, boolean isError, Long posId) throws Exception {
		String vin = po.getNewVin();
		if(null != vin &&  !"".equals(vin)){
			if(vin.length() != 17){
				isError = true;
				errorInfo.append("替换VIN长度不正确，必须为17位;");
			}else{
				List<Map<String, Object>>  mapList = reDao.getImportVin(vin,posId);
				if(null != mapList && !mapList.isEmpty()){
				   if(mapList.size() ==1){
					   
					   if(mapList.get(0).get("FLAG").toString().equals("0")) {
						   isError = true;
						   errorInfo.append("职位无产地权限;");
					   }
					   if(!mapList.get(0).get("LIFE_CYCLE").toString().equals(String.valueOf(Constant.VEHICLE_LIFE_02))) {
						   isError = true;
						   errorInfo.append("替换车架号'"+mapList.get(0).get("VIN")+"'非在库状态;");
					   }
				   }
				}
			}
			
		}
		
	}
	/**
	 * 导入查询结果获取
	 */
	public void expressResultSelect() {

		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 10000;
			PageResult<Map<String, Object>> ps = reDao.importQuery(logonUser.getUserId().toString(), pageSize, curPage);
			List<Map<String, Object>> temList = ps.getRecords();
			act.setOutData("temList", temList);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "交接单信息导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 发运交接单导入
	 */
	public void importBillSave() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String userId  = logonUser.getUserId().toString();
			long count = 0;
			//对导入数据进行判重
			List<Map<String, Object>> rlist=reDao.checkRepeatRow();
			if(rlist!=null&&rlist.size()>0){
					Map<String, Object> map=rlist.get(0);
					act.setOutData("eMsg", "存在"+map.get("RENUM")+"条<组板号为"+map.get("BO_NO")+",订单号为"+map.get("ORDER_NO")+",物料代码为"+map.get("MATERIAL_CODE")+",车架号为"+map.get("VIN")+">相同的数据");
					return;
			}
			List bIdList=new ArrayList();
			List dtIdList=new ArrayList();
			/**
			 * 交接单导入判断逻辑：
			 * 1.每个组板号只初始化一次，初始化完毕后，更新处理状态为已发运(9710006)，二次导入时，只会存在替换车情况
			 * 2.组板号判断，若既有新车也有在途换车，直接抛出问题返回
			 * 首次导入校验逻辑：
			 * 1.判断交接单是否生成，若生成，抛出问题返回
			 * 2.判断物料是否匹配，若不匹配，抛出问题返回
			 * 3.判断车架号是否在库，并且未锁定，若不是，抛出问题返回
			 * 二次导入校验逻辑：
			 * 1.判断原车架号是否存在，若不存在，抛出问题返回
			 * 2.判断新车架号与物料匹配时，若不匹配，抛出问题返回
			 * 3.判断新车架号是在库状态，并且未锁定，若不是，抛出问题返回
			 * 
			 * 校验成功后，添加订单回写逻辑
			 * 1.回写资源锁定表发运数量
			 * 2.回写ERP表信息
			 */
			List<Map<String, Object>> boList = reDao.checkBoNoIsNew(userId);
			for (Map<String, Object> map : boList) {
				//如果是第一次导入交接单
				if("0".equals(map.get("IS_NEW").toString())){
					String boNo = (String) map.get("BO_NO");//组板号
					String boId =String.valueOf(map.get("BO_ID"));//组板ID
					String handleStatus = (String) map.get("HANDLE_STATUS");//组板单状态
					//如果组板单状态为已发运，直接抛出问题返回
					if(Constant.HANDLE_STATUS06.toString().equals(handleStatus)){
						act.setOutData("eMsg", "导入失败：组板号'"+map.get("BO_NO")+"'，已经生成发运交接单！");
						return ;
					}
					
					//校验物料或车辆数量
					List<Map<String, Object>> boMatList = reDao.checkMaterialIsExist(boNo);
					for (Map<String, Object> matMap : boMatList) {
						//如果存在数量不匹配，直接抛出问题返回
						if(!"0".equals(matMap.get("RES_FLAG").toString())){
							act.setOutData("eMsg", "导入失败：组板号'"+matMap.get("BO_NO")+"'，存在物料不存在或车辆数量不匹配！");
							return ;
						}
					}
					
					//校验车辆是否在库和是否锁定
					List<Map<String, Object>> boVhlList = reDao.checkVehicleIsReq(boNo);
					for (Map<String, Object> vhlMap : boVhlList) {
						//如果存在车辆状态不符合要求，直接抛出问题返回
						if(!"0".equals(vhlMap.get("RES_VIN_FLAG").toString())){
							act.setOutData("eMsg", "导入失败：组板号'"+vhlMap.get("BO_NO")+"'，车架号'"+vhlMap.get("VIN").toString()+"'库存状态为'"+vhlMap.get("LIFE_CYCLE")+"',锁定状态为'"+vhlMap.get("LOCK_STATUS")+"',不符合交接要求！");
							return ;
						}
					}
						//进入交接单生成环节
						//else{
					//根据组板号获取可生成交接单的数量，按照经销商/仓库，发运方式，结算地分组
					List<Map<String, Object>> blist=reDao.getBillMainNumByBno(boNo);
					if(blist!=null&&blist.size()>0){
						for(int m=0;m<blist.size();m++){
							Map<String, Object> bmap=blist.get(m);
							String billId=SequenceManager.getSequence(null);//交接单ID
							String billNo=CommonUtils.getBusNo(Constant.NOCRT_SEND_ORDER_NO,Long.parseLong(Constant.areaIdJZD));
							TtSalesWaybillPO tsw=new TtSalesWaybillPO();
							tsw.setBillId(Long.parseLong(billId));
							tsw.setBillNo(billNo);
							tsw.setLogiId(Long.parseLong(bmap.get("DLV_LOGI_ID").toString()));
							tsw.setOrDealerId(Long.parseLong(bmap.get("ORD_PUR_DEALER_ID").toString()));
							tsw.setBillCrtDate(new Date());
							tsw.setBillCrtPer(logonUser.getUserId());
							tsw.setStatus(Long.valueOf(Constant.STATUS_ENABLE));//有效
							tsw.setCreateBy(logonUser.getUserId());
							tsw.setCreateDate(new Date());
							tsw.setSendStatus(Long.valueOf(Constant.WAYBILL_STATUS_01));//已导入
							tsw.setwLogiId(Long.parseLong(bmap.get("DLV_LOGI_ID").toString()));
							tsw.setAddressInfo(bmap.get("REQ_REC_ADDR").toString());
							tsw.setDlvBalProvId(Long.parseLong(bmap.get("DLV_BAL_PROV_ID").toString()));
							tsw.setDlvBalCityId(Long.parseLong(bmap.get("DLV_BAL_CITY_ID").toString()));
							tsw.setDlvBalCountyId(Long.parseLong(bmap.get("DLV_BAL_COUNTY_ID").toString()));
							tsw.setBalProvId(Long.parseLong(bmap.get("DLV_BAL_PROV_ID").toString()));
							tsw.setBalCityId(Long.parseLong(bmap.get("DLV_BAL_CITY_ID").toString()));
							tsw.setBalCountyId(Long.parseLong(bmap.get("DLV_BAL_COUNTY_ID").toString()));
							tsw.setDlvShipType(Integer.parseInt(bmap.get("DLV_SHIP_TYPE").toString()));
							reDao.insert(tsw);
							bIdList.add(billId);
							String logiId=bmap.get("DLV_LOGI_ID").toString();
							String dealerId=bmap.get("ORD_PUR_DEALER_ID").toString();
							String shipType=bmap.get("DLV_SHIP_TYPE").toString();
							String provId=bmap.get("DLV_BAL_PROV_ID").toString();
							String cityId=bmap.get("DLV_BAL_CITY_ID").toString();
							String countyId=bmap.get("DLV_BAL_COUNTY_ID").toString();
							List velist=new ArrayList<String>();
							//根据组板号、经销商ID、发运方式、发运结算地获取订单明细信息
							List<Map<String, Object>> dlist=reDao.getBillDetailImport(boNo, logiId, dealerId, shipType, provId, cityId, countyId);
							if(dlist!=null&&dlist.size()>0){
								for(int k=0;k<dlist.size();k++){
									Map<String, Object> tmap=dlist.get(k);
									TtSalesWayBillDtlPO tswb=new TtSalesWayBillDtlPO();
									String dtlId=SequenceManager.getSequence(null);
									tswb.setDtlId(Long.parseLong(dtlId));//明细ID
									tswb.setBillId(Long.parseLong(billId));
									tswb.setVehicleId(Long.parseLong(tmap.get("VEHICLE_ID").toString()));
									tswb.setCreateBy(logonUser.getUserId());
									tswb.setCreateDate(new Date());
									tswb.setOrderNo(tmap.get("ORD_NO").toString());
									tswb.setVin(tmap.get("VIN").toString());
									tswb.setMatId(Long.parseLong(tmap.get("MATERIAL_ID").toString()));
									tswb.setOrderId(Long.parseLong(tmap.get("ORD_ID").toString()));
									tswb.setOrderDetailId(Long.parseLong(tmap.get("ORD_DETAIL_ID").toString()));
									tswb.setDlvWhId(Long.parseLong(tmap.get("DLV_WH_ID").toString()));
									tswb.setDlvIsSd(Integer.parseInt(tmap.get("DLV_IS_SD").toString()));
									tswb.setDlvIsZz(Integer.parseInt(tmap.get("DLV_IS_ZZ").toString()));
									if(tmap.get("ZZ_WH_ID")!=null){
										tswb.setZzWhId(Long.parseLong(tmap.get("ZZ_WH_ID").toString()));
									}
									if(tmap.get("DLV_ZZ_PROV_ID")!=null){
										tswb.setDlvZzProvId(Long.parseLong(tmap.get("DLV_ZZ_PROV_ID").toString()));
									}
									if(tmap.get("DLV_ZZ_CITY_ID")!=null){
										tswb.setDlvZzCityId(Long.parseLong(tmap.get("DLV_ZZ_CITY_ID").toString()));
									}
									if(tmap.get("DLV_ZZ_COUNTY_ID")!=null){
										tswb.setDlvZzCountyId(Long.parseLong(tmap.get("DLV_ZZ_COUNTY_ID").toString()));
									}
									reDao.insert(tswb);
									dtIdList.add(dtlId);
									//更新车辆状态为出库
									TmVehiclePO tvp=new TmVehiclePO();
									tvp.setVin(tmap.get("VIN").toString());
									TmVehiclePO tvp2=new TmVehiclePO();
									
									tvp2.setUpdateBy(logonUser.getUserId());
									tvp2.setUpdateDate(new Date());
									//根据订单ID获取发运类型，若为批售订单，则更新dealerId到车辆表
									TtVsDlvryPO tvdp=new TtVsDlvryPO();
									tvdp.setOrdId(Long.parseLong(tmap.get("ORD_ID").toString()));
									tvdp=(TtVsDlvryPO) reDao.select(tvdp).get(0);
									//针对调拨单更新为车厂调拨单在途，否则为出库
									if(tvdp.getDlvType().toString().equals(Constant.DELIVERY_ORD_TYPE_ORDER.toString())){
										tvp2.setDealerId(Long.parseLong(tmap.get("ORD_DETAIL_ID").toString()));
										tvp2.setLifeCycle(Constant.VEHICLE_LIFE_08);
									}else{
										tvp2.setLifeCycle(Constant.VEHICLE_LIFE_12);
									}
									
									reDao.update(tvp, tvp2);
									//向出库记录表中新增一条出库记录
									saveErpDataOut(tmap.get("VIN").toString(),tmap.get("VEHICLE_ID").toString(),"正常出库",new Date(),"", "",Constant.VEHICLE_LIFE_08.toString());
									//更新交接单ID至组板明细表中
									TtSalesBoDetailPO tsbd=new TtSalesBoDetailPO();
									tsbd.setOrDeId(Long.parseLong(tmap.get("ORD_DETAIL_ID").toString()));
									tsbd.setMatId(Long.parseLong(tmap.get("MATERIAL_ID").toString()));
									tsbd.setBoId(Long.parseLong(boId));
									TtSalesBoDetailPO tsbd2=new TtSalesBoDetailPO();
									tsbd2.setBillId(Long.parseLong(billId));
									tsbd2.setUpdateBy(logonUser.getUserId());
									tsbd2.setUpdateDate(new Date());
									reDao.update(tsbd, tsbd2);
									//更新组板主表中的发运状态
									TtSalesBoardPO mpo=new TtSalesBoardPO();
									mpo.setBoId(Long.parseLong(boId));
									TtSalesBoardPO mpo2=new TtSalesBoardPO();
									mpo2.setHandleStatus(Constant.HANDLE_STATUS06);//已发运
									mpo2.setUpdateBy(Long.parseLong(userId));
									mpo2.setUpdateDate(new Date());
									reDao.update(mpo, mpo2);
									
									velist.add(tmap.get("VEHICLE_ID").toString());
									//更新资源锁定表的数量
									teDao.updateTtVsOrderResouce(Long.parseLong(tmap.get("REQ_DETAIL_ID").toString()), 1, logonUser.getUserId());
								}
							}
							//更新交接单主表中的车辆数
							TtSalesWaybillPO tswo=new TtSalesWaybillPO();
							tswo.setBillId(Long.parseLong(billId));
							TtSalesWaybillPO tswo2=new TtSalesWaybillPO();
							tswo2.setVehNum(dlist.size());
							reDao.update(tswo, tswo2);
							//更新业务表中的交接数量
							//更新发运主表的交接数量
							reDao.updateJjDlvMain(boNo, logonUser.getUserId().toString());
							//更新发运明细表的交接数量
							reDao.updateJjDlvDetail(boNo, logonUser.getUserId().toString());
							//更新调拨单主表的交接数量
							reDao.updateJjDispMain(boNo, logonUser.getUserId().toString());
							
							//向tt_vs_dlvry_erp中新增记录
							List<Object> params=new ArrayList<Object>();
							params.add(billId);
							Map<String,Object> msmap=reDao.getViewMainByBillId(params);
							String erpMid=SequenceManager.getSequence(null);
							msmap.put("SENDCAR_HEADER_ID", Long.parseLong(erpMid));
							msmap.put("CREATE_BY", logonUser.getUserId());
							teDao.insertTtVsDlvryErp(msmap);
							//向tt_vs_dlvry_erp_dtl中新增记录
							for(int v=0;v<velist.size();v++){
								Map<String, Object> ddmap=new HashMap<String,Object>();
								ddmap.put("DETAIL_ID", Long.parseLong(SequenceManager.getSequence(null)));
								ddmap.put("SENDCAR_HEADER_ID", Long.parseLong(erpMid));
								ddmap.put("VEHICLE_ID", Long.parseLong(velist.get(v).toString()));
								ddmap.put("REQ_ID", msmap.get("REQ_ID"));
								ddmap.put("IS_RECEIVE", Long.parseLong(Constant.IS_RECEIVE_0.toString()));
								ddmap.put("CREATE_BY", logonUser.getUserId());
								teDao.insertTtVsDlvryErpDtl(ddmap);
							}
							
						}
					}
						//}						
					//}
					
				}				
				else{
					//如果是在途换车
					if("0".equals(map.get("IS_FLAG").toString())) {
						
						String boNo = (String) map.get("BO_NO");//组板号
						TmpTtSalesWayBillPO tmp=new TmpTtSalesWayBillPO();
						tmp.setBoNo(boNo);
						tmp.setCreateBy(Long.parseLong(userId));
						List tmpList=reDao.select(tmp);
						if(tmpList!=null&&tmpList.size()>0){
							for(int i=0;i<tmpList.size();i++){
								TmpTtSalesWayBillPO temp=(TmpTtSalesWayBillPO) tmpList.get(i);
								String orderNo=temp.getOrderNo();
								String materialCode=temp.getMaterialCode();
								String vin=temp.getVin();
								String newVin=temp.getNewVin();
								//根据原车架号判断是否存在交接单。不存在，返回错误
								List<Map<String, Object>> blist=reDao.getBillRepeatByImport(boNo, orderNo, materialCode, vin);
								if(blist==null||blist.size()==0){
									act.setOutData("eMsg","导入失败，不存在原车架号为"+vin+">的交接单");
									return;
								}else{
										//判断是否为重复导入
										List<Map<String, Object>> nlist=reDao.getBillRepeatByImport(boNo, orderNo, materialCode, newVin);
										if(nlist!=null&&nlist.size()>0){
											act.setOutData("eMsg","导入失败，已存在<组板号为"+boNo+",订单号为"+orderNo+",物料代码为"+materialCode+",替换车架号为"+newVin+">相同的交接单");
											return;
										}
										List<Map<String, Object>> mlist=reDao.getBillInfoByImport(boNo, orderNo, materialCode);
										Map<String, Object> mmap=mlist.get(0);
										String sendStatus=mmap.get("SEND_STATUS").toString();//交接单状态
										if(!sendStatus.equals(String.valueOf(Constant.WAYBILL_STATUS_01))){//非导入状态
											act.setOutData("eMsg","导入失败，<组板号为"+boNo+",订单号为"+orderNo+",物料代码为"+materialCode+">的交接单为非导入状态，无法进行二次导入");
											return;
										}
										//校验原车架号是否在途
										TmVehiclePO tmvp=new TmVehiclePO();
										tmvp.setVin(vin);
										
										List plist=reDao.select(tmvp);
										
										tmvp=(TmVehiclePO) plist.get(0);
										TtVsDlvryPO tvdp=new TtVsDlvryPO();
										tvdp.setOrdNo(orderNo);
										tvdp=(TtVsDlvryPO) reDao.select(tvdp).get(0);
										//针对调拨单更新为车厂调拨单在途，否则为出库
										if(tvdp.getDlvType().toString().equals(Constant.DELIVERY_ORD_TYPE_ORDER.toString())){
											if(!tmvp.getLifeCycle().toString().equals(String.valueOf(Constant.VEHICLE_LIFE_08))){//非出库状态
												act.setOutData("eMsg","导入失败，<原车架号为"+vin+">的车辆非出库状态");
												return;
											}
										}else{
											if(!tmvp.getLifeCycle().toString().equals(String.valueOf(Constant.VEHICLE_LIFE_12))){//非调拨出库状态
												act.setOutData("eMsg","导入失败，针对调拨单，<原车架号为"+vin+">的车辆非车厂调拨出库状态");
												return;
											}
										}
										
										//校验替换车架号是否在库
										TmVehiclePO tmv=new TmVehiclePO();
										tmv.setVin(newVin);
										tmv.setLockStatus(Constant.LOCK_STATUS_01);//正常状态
										List vlist=reDao.select(tmv);
										tmv=(TmVehiclePO) vlist.get(0);
										if(!tmv.getLifeCycle().toString().equals(String.valueOf(Constant.VEHICLE_LIFE_02))){//非在库状态
											act.setOutData("eMsg","导入失败，<替换车架号为"+newVin+">的车辆非在库状态");
											return;
										}
										
										//}
										//以上校验通过之后，更新原交接单所对应车架号，并且将原有车架号的状态更新为在库
										TtSalesWayBillDtlPO tswb=new TtSalesWayBillDtlPO();
										tswb.setOrderNo(orderNo);
										tswb.setVin(vin);
										tswb.setIsTc(Long.valueOf(Constant.IF_TYPE_NO));
										TtSalesWayBillDtlPO tswb2=new TtSalesWayBillDtlPO();
										tswb2.setVin(newVin);
										tswb2.setUpdateBy(logonUser.getUserId());
										tswb2.setUpdateDate(new Date());
										reDao.update(tswb, tswb2);
										//更新原车架号为在库状态
										TmVehiclePO tv=new TmVehiclePO();
										tv.setVin(vin);
										TmVehiclePO tv2=new TmVehiclePO();
										tv2.setLifeCycle(Constant.VEHICLE_LIFE_02);//在库
										tv2.setLockStatus(Constant.LOCK_STATUS_01);//正常状态
										tv2.setUpdateBy(logonUser.getUserId());
										tv2.setUpdateDate(new Date());
										tv2.setDealerId(null);
										reDao.update(tv, tv2);
										List ttlist=reDao.select(tv);
										tv=(TmVehiclePO) ttlist.get(0);
										//删除原车架号的出库记录
										//TsiExpBusVehStoreDetPO det=new TsiExpBusVehStoreDetPO();
										//det.setVehicleId(tv.getVehicleId());
										//det.setVin(vin);
										//reDao.delete(det);
										//针对原车架号增加一条入库记录
										saveErpDataOut(newVin,tv.getVehicleId().toString(),"在途换车",new Date(),"", "",Constant.VEHICLE_LIFE_02.toString());

										//更新替换车架号为出库状态
										TmVehiclePO tv3=new TmVehiclePO();
										tv3.setVin(newVin);
										TmVehiclePO tv4=new TmVehiclePO();
										
										tv4.setUpdateBy(logonUser.getUserId());
										tv4.setUpdateDate(new Date());
										//根据订单ID获取发运类型，若为批售订单，则更新dealerId到车辆表
//										TtVsDlvryPO tvdp=new TtVsDlvryPO();
//										tvdp.setOrdNo(orderNo);
//										tvdp=(TtVsDlvryPO) reDao.select(tvdp).get(0);
										if(tvdp.getDlvType().toString().equals(Constant.DELIVERY_ORD_TYPE_ORDER.toString())){
											tv4.setDealerId(tvdp.getOrdPurDealerId());
											tv4.setLifeCycle(Constant.VEHICLE_LIFE_08);//出库
										}else{//车厂调拨在途
											tv4.setLifeCycle(Constant.VEHICLE_LIFE_12);//车厂调拨在途
										}
										reDao.update(tv3, tv4);
										List tvlist=reDao.select(tv3);
										tv3=(TmVehiclePO) tvlist.get(0);
										//针对替换车架号新增一条出库记录
										saveErpDataOut(newVin,tv3.getVehicleId().toString(),"在途换车",new Date(),"", "",Constant.VEHICLE_LIFE_08.toString());
										//更新TtVsDlvryErpDtlPO中的车辆ID
										teDao.changeVehicle(tvdp.getReqId(),tv.getVehicleId(),tv3.getVehicleId(),logonUser.getUserId());
								}
							}
						}
						
						
					}
					//判断既有首次导入也有在途换车，直接抛出问题返回
					else{
						act.setOutData("eMsg", "导入失败：组板号'"+map.get("BO_NO")+"'，既存在首次导入，也存在途换车！");
						return ;
					}
				}
				//count++;
			}
			//将本次交接单记录存入日志表
			for(int m=0;m<bIdList.size();m++){
				String billId=(String) bIdList.get(m);
				reDao.insert(insertTsiWayBillPo(billId,logonUser.getUserId()));
			}
			for(int m=0;m<dtIdList.size();m++){
				String dtlId=(String) dtIdList.get(m);
				reDao.insert(insertTsiWayBillDtlPo(dtlId,logonUser.getUserId()));
			}
			act.setOutData("eMsg", "success");

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "交接单导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public String insertTsiWayBillPo(String billId,long userId){
		StringBuffer sql= new StringBuffer();
		sql.append("insert into tsi_tt_sales_waybill\n" );
		sql.append("  (BILL_ID,\n" );
		sql.append("   BILL_NO,\n" );
		sql.append("   BAL_ID,\n" );
		sql.append("   LOGI_ID,\n" );
		sql.append("   BO_NUM,\n" );
		sql.append("   VEH_NUM,\n" );
		sql.append("   SEND_DEALER_ID,\n" );
		sql.append("   OR_DEALER_ID,\n" );
		sql.append("   INVOICE,\n" );
		sql.append("   BILL_CRT_DATE,\n" );
		sql.append("   BILL_CRT_PER,\n" );
		sql.append("   BILL_PRINT_DATE,\n" );
		sql.append("   BILL_PRINT_PER,\n" );
		sql.append("   STATUS,\n" );
		sql.append("   BACK_CRM_DATE,\n" );
		sql.append("   BACK_CRM_PER,\n" );
		sql.append("   ARR_DATE,\n" );
		sql.append("   REMARK,\n" );
		sql.append("   CREATE_BY,\n" );
		sql.append("   CREATE_DATE,\n" );
		sql.append("   UPDATE_BY,\n" );
		sql.append("   UPDATE_DATE,\n" );
		sql.append("   AREA_ID,\n" );
		sql.append("   ADDRESS_ID,\n" );
		sql.append("   SEND_STATUS,\n" );
		sql.append("   IS_CONFIRM,\n" );
		sql.append("   CHEPAI_NO,\n" );
		sql.append("   SIJI,\n" );
		sql.append("   TEL,\n" );
		sql.append("   IS_STATUS,\n" );
		sql.append("   W_LOGI_ID,\n" );
		sql.append("   W_LOGI_NAME,\n" );
		sql.append("   SEND_DEALER_NAME,\n" );
		sql.append("   OR_DEALER_NAME,\n" );
		sql.append("   SEND_SHORTDEALER_NAME,\n" );
		sql.append("   OR_SHORTDEALER_NAME,\n" );
		sql.append("   SCP_REMARK,\n" );
		sql.append("   SCP_ISYS,\n" );
		sql.append("   INTERFACE_DATE,\n" );
		sql.append("   ADDRESS_INFO,\n" );
		sql.append("   PCDATETIME,\n" );
		sql.append("   FPDATETIME,\n" );
		sql.append("   CONFIRM_DATE,\n" );
		sql.append("   INVOICE_DATE,\n" );
		sql.append("   INVOICE_STATUS,\n" );
		sql.append("   INVOICE_USER,\n" );
		sql.append("   IS_ACC,\n" );
		sql.append("   LAST_CAR_DATE,\n" );
		sql.append("   PRINT_COUNT,\n" );
		sql.append("   DLV_BAL_PROV_ID,\n" );
		sql.append("   DLV_BAL_CITY_ID,\n" );
		sql.append("   DLV_BAL_COUNTY_ID,\n" );
		sql.append("   DLV_SHIP_TYPE,\n" );
		sql.append("   BILL_AMOUNT,\n" );
		sql.append("   BAL_PROV_ID,\n" );
		sql.append("   BAL_CITY_ID,\n" );
		sql.append("   BAL_COUNTY_ID,\n" );
		sql.append("   SUPPLY_MONEY,\n" );
		sql.append("   DEDUCT_MONEY,\n" );
		sql.append("   IS_CHANGE,\n" );
		sql.append("   OTHER_MONEY,\n" );
		sql.append("   APPLY_ID,\n" );
		sql.append("   APPLY_REMARK)\n" );
		sql.append("  SELECT BILL_ID,\n" );
		sql.append("         BILL_NO,\n" );
		sql.append("         BAL_ID,\n" );
		sql.append("         LOGI_ID,\n" );
		sql.append("         BO_NUM,\n" );
		sql.append("         VEH_NUM,\n" );
		sql.append("         SEND_DEALER_ID,\n" );
		sql.append("         OR_DEALER_ID,\n" );
		sql.append("         INVOICE,\n" );
		sql.append("         BILL_CRT_DATE,\n" );
		sql.append("         BILL_CRT_PER,\n" );
		sql.append("         BILL_PRINT_DATE,\n" );
		sql.append("         BILL_PRINT_PER,\n" );
		sql.append("         STATUS,\n" );
		sql.append("         BACK_CRM_DATE,\n" );
		sql.append("         BACK_CRM_PER,\n" );
		sql.append("         ARR_DATE,\n" );
		sql.append("         REMARK,\n" );
		sql.append("         '"+userId+"',\n" );
		sql.append("         SYSDATE,\n" );
		sql.append("         '',\n" );
		sql.append("         NULL,\n" );
		sql.append("         AREA_ID,\n" );
		sql.append("         ADDRESS_ID,\n" );
		sql.append("         SEND_STATUS,\n" );
		sql.append("         IS_CONFIRM,\n" );
		sql.append("         CHEPAI_NO,\n" );
		sql.append("         SIJI,\n" );
		sql.append("         TEL,\n" );
		sql.append("         IS_STATUS,\n" );
		sql.append("         W_LOGI_ID,\n" );
		sql.append("         W_LOGI_NAME,\n" );
		sql.append("         SEND_DEALER_NAME,\n" );
		sql.append("         OR_DEALER_NAME,\n" );
		sql.append("         SEND_SHORTDEALER_NAME,\n" );
		sql.append("         OR_SHORTDEALER_NAME,\n" );
		sql.append("         SCP_REMARK,\n" );
		sql.append("         SCP_ISYS,\n" );
		sql.append("         INTERFACE_DATE,\n" );
		sql.append("         ADDRESS_INFO,\n" );
		sql.append("         PCDATETIME,\n" );
		sql.append("         FPDATETIME,\n" );
		sql.append("         CONFIRM_DATE,\n" );
		sql.append("         INVOICE_DATE,\n" );
		sql.append("         INVOICE_STATUS,\n" );
		sql.append("         INVOICE_USER,\n" );
		sql.append("         IS_ACC,\n" );
		sql.append("         LAST_CAR_DATE,\n" );
		sql.append("         PRINT_COUNT,\n" );
		sql.append("         DLV_BAL_PROV_ID,\n" );
		sql.append("         DLV_BAL_CITY_ID,\n" );
		sql.append("         DLV_BAL_COUNTY_ID,\n" );
		sql.append("         DLV_SHIP_TYPE,\n" );
		sql.append("         BILL_AMOUNT,\n" );
		sql.append("         BAL_PROV_ID,\n" );
		sql.append("         BAL_CITY_ID,\n" );
		sql.append("         BAL_COUNTY_ID,\n" );
		sql.append("         SUPPLY_MONEY,\n" );
		sql.append("         DEDUCT_MONEY,\n" );
		sql.append("         IS_CHANGE,\n" );
		sql.append("         OTHER_MONEY,\n" );
		sql.append("         APPLY_ID,\n" );
		sql.append("         APPLY_REMARK\n" );
		sql.append("    FROM TT_SALES_WAYBILL T\n" );
		sql.append("   WHERE T.BILL_ID = '"+billId+"'\n");
		return sql.toString();
	}
	public String insertTsiWayBillDtlPo(String dtlId,long userId){
		StringBuffer sql= new StringBuffer();
		sql.append("insert into TSI_TT_SALES_WAY_BILL_DTL\n" );
		sql.append("  (DTL_ID,\n" );
		sql.append("   BILL_ID,\n" );
		sql.append("   VEHICLE_ID,\n" );
		sql.append("   CREATE_DATE,\n" );
		sql.append("   CREATE_BY,\n" );
		sql.append("   ORDER_NO,\n" );
		sql.append("   VIN,\n" );
		sql.append("   IS_ACC,\n" );
		sql.append("   MAT_ID,\n" );
		sql.append("   IS_STATUS,\n" );
		sql.append("   ERP_MATERIAL_CODE,\n" );
		sql.append("   IS_STATUS1,\n" );
		sql.append("   ASS_DATE,\n" );
		sql.append("   ALLOCA_DATE,\n" );
		sql.append("   ORDER_ID,\n" );
		sql.append("   ORDER_DETAIL_ID,\n" );
		sql.append("   IS_TC,\n" );
		sql.append("   STATUS,\n" );
		sql.append("   UPDATE_BY,\n" );
		sql.append("   UPDATE_DATE,\n" );
		sql.append("   DRIVER_USER_ID,\n" );
		sql.append("   DRIVER_BIND_DATE,\n" );
		sql.append("   DRIVER_PHONE,\n" );
		sql.append("   REPORT_ADDRESS,\n" );
		sql.append("   REPORT_DATE,\n" );
		sql.append("   DLV_WH_ID,\n" );
		sql.append("   PRICE,\n" );
		sql.append("   PRICE_FACTOR,\n" );
		sql.append("   MILEAGE,\n" );
		sql.append("   ONE_BILL_AMOUNT,\n" );
		sql.append("   DLV_IS_SD,\n" );
		sql.append("   DLV_IS_ZZ,\n" );
		sql.append("   DLV_ZZ_PROV_ID,\n" );
		sql.append("   DLV_ZZ_CITY_ID,\n" );
		sql.append("   DLV_ZZ_COUNTY_ID,\n" );
		sql.append("   NEW_PRICE,\n" );
		sql.append("   NEW_MILEAGE,\n" );
		sql.append("   NEW_AMOUNT,\n" );
		sql.append("   ZZ_WH_ID,\n" );
		sql.append("   PRICE_ZZ,\n" );
		sql.append("   MILEAGE_ZZ)\n" );
		sql.append("  SELECT DTL_ID,\n" );
		sql.append("         BILL_ID,\n" );
		sql.append("         VEHICLE_ID,\n" );
		sql.append("         SYSDATE,\n" );
		sql.append("         '"+userId+"',\n" );
		sql.append("         ORDER_NO,\n" );
		sql.append("         VIN,\n" );
		sql.append("         IS_ACC,\n" );
		sql.append("         MAT_ID,\n" );
		sql.append("         IS_STATUS,\n" );
		sql.append("         ERP_MATERIAL_CODE,\n" );
		sql.append("         IS_STATUS1,\n" );
		sql.append("         ASS_DATE,\n" );
		sql.append("         ALLOCA_DATE,\n" );
		sql.append("         ORDER_ID,\n" );
		sql.append("         ORDER_DETAIL_ID,\n" );
		sql.append("         IS_TC,\n" );
		sql.append("         STATUS,\n" );
		sql.append("         '',\n" );
		sql.append("         NULL,\n" );
		sql.append("         DRIVER_USER_ID,\n" );
		sql.append("         DRIVER_BIND_DATE,\n" );
		sql.append("         DRIVER_PHONE,\n" );
		sql.append("         REPORT_ADDRESS,\n" );
		sql.append("         REPORT_DATE,\n" );
		sql.append("         DLV_WH_ID,\n" );
		sql.append("         PRICE,\n" );
		sql.append("         PRICE_FACTOR,\n" );
		sql.append("         MILEAGE,\n" );
		sql.append("         ONE_BILL_AMOUNT,\n" );
		sql.append("         DLV_IS_SD,\n" );
		sql.append("         DLV_IS_ZZ,\n" );
		sql.append("         DLV_ZZ_PROV_ID,\n" );
		sql.append("         DLV_ZZ_CITY_ID,\n" );
		sql.append("         DLV_ZZ_COUNTY_ID,\n" );
		sql.append("         NEW_PRICE,\n" );
		sql.append("         NEW_MILEAGE,\n" );
		sql.append("         NEW_AMOUNT,\n" );
		sql.append("         ZZ_WH_ID,\n" );
		sql.append("         PRICE_ZZ,\n" );
		sql.append("         MILEAGE_ZZ\n" );
		sql.append("    FROM TT_SALES_WAY_BILL_DTL TT\n" );
		sql.append("  WHERE TT.DTL_ID = '"+dtlId+"'");
		return sql.toString();
	}
	/**
	 * 新增出库记录
	 * @param vin
	 * @param vehicleId
	 * @param inOutDesc
	 * @param orgStoreageDate
	 * @param erpOrderNo
	 * @param specialOrderNo
	 */
	@SuppressWarnings("unchecked")
	public void saveErpDataOut(String vin,String vehicleId,String inOutDesc,
			Date orgStoreageDate,String erpOrderNo, String specialOrderNo,String inOutFlag){
		Map<String,Object> map = rsdao.queryVinMater(vin);
		String materialCode = "";
		Integer isSpecialCar=null;//是否特价车
		if(!XHBUtil.IsNull(specialOrderNo)){//如果特殊订单号不为空则物料为超级物料
			Map<String,Object> superMaterialMap = rsdao.getSuperMaterial(vin,specialOrderNo);
			materialCode = CommonUtils.checkNull(superMaterialMap.get("MATNR"));
			isSpecialCar=Constant.IF_TYPE_YES;
		}else{
			if(!map.isEmpty()){
				materialCode = CommonUtils.checkNull(map.get("MATERIAL_CODE"));
			}
			isSpecialCar=Constant.IF_TYPE_NO;
		}
		
		List<Map<String, Object>> listPo = rsdao.getTiExpBusVehStore(materialCode, erpOrderNo, "0");
		
		
		//如果存在主数据，则只需要修改主数据的对应物料的数量
		if(null != listPo && listPo.size()> 0){
			Map<String, Object>   storePo = listPo.get(0);
			Long revId = Long.parseLong(storePo.get("REV_ID").toString());
			
			TsiExpBusVehStorePO ts = new TsiExpBusVehStorePO();
			ts.setRevId(revId);
			List tslist=reDao.select(ts);
			TsiExpBusVehStorePO conditions=(TsiExpBusVehStorePO) tslist.get(0);
			//保存详细信息
			TsiExpBusVehStoreDetPO po = new TsiExpBusVehStoreDetPO();
			Long detId = Long.parseLong(SequenceManager.getSequence(null));
			
			po.setDetId(detId);
			//关联的ID
			po.setRevId(revId);
			po.setSerialno(vin);
			po.setMatdocItm("0001");
			po.setIsRead(0);
			po.setCreateDate(new Date());
			po.setVehicleId(Long.parseLong(vehicleId));
			po.setVin(vin);
			po.setInOutFlag(inOutFlag);//出入库标记：出库
			po.setInOutDesc(inOutDesc);
			po.setInOutDate(new Date());
			po.setIsSpecialCar(isSpecialCar);
			reDao.insert(po);
		}else{//如果主数据不存在自
			TsiExpBusVehStorePO  po = new TsiExpBusVehStorePO();
			Long revId = Long.parseLong(SequenceManager.getSequence(null));
			
			po.setRevId(revId);
			po.setOrderid(erpOrderNo);
			po.setPstngDate(orgStoreageDate);
			//物料
			po.setMaterial(materialCode);
			po.setIsRead(0);
			po.setCreateDate(new Date());
			reDao.insert(po);
			
			//保存详细信息
			TsiExpBusVehStoreDetPO detpo = new TsiExpBusVehStoreDetPO();
			Long detId = Long.parseLong(SequenceManager.getSequence(null));
			detpo.setDetId(detId);
			//关联的ID
			detpo.setRevId(revId);
			detpo.setSerialno(vin);
			detpo.setMatdocItm("0001");
			detpo.setIsRead(0);
			detpo.setCreateDate(new Date());
			detpo.setVehicleId(Long.parseLong(vehicleId));
			detpo.setVin(vin);
			detpo.setInOutFlag(inOutFlag);//出入库标记：出库
			detpo.setInOutDesc(inOutDesc);
			detpo.setInOutDate(new Date());
			detpo.setIsSpecialCar(isSpecialCar);
			reDao.insert(detpo);
		}
		
	}
	/**
	 * 发运交接单查询初始化
	 */
	public void wayBillQueryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=reLDao.getLogiByWarehouse(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//产地LIST
			
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			act.setForword(dlvBillQueryInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发运计划发送初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 交接单打印
	 */
	public void dlvBillPrint(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String billId=CommonUtils.checkNullNum(request.getParamValue("billId"));//交接单ID	
			
			//根据交接单ID获取打印主信息
			Map<String,Object> map=reDao.getPrintMainByBillId(billId);
			List<Map<String, Object>> dlist=reDao.getBillDelPrintQuery(billId);
			act.setOutData("map", map);
			act.setOutData("dlist", dlist);
			act.setForword(dlvbillPrintUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"交接单打印");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 交接单打印次数增加
	 */
	public void printCountAdd(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String billId=CommonUtils.checkNullNum(request.getParamValue("billId"));//交接单ID	
			TtSalesWaybillPO po=new TtSalesWaybillPO();
			po.setBillId(Long.parseLong(billId));
			List list=reDao.select(po);
			po=(TtSalesWaybillPO) list.get(0);
			TtSalesWaybillPO tsw=new TtSalesWaybillPO();
			tsw.setBillId(Long.parseLong(billId));
			TtSalesWaybillPO tsw2=new TtSalesWaybillPO();
			tsw2.setPrintCount(po.getPrintCount()+1);
			tsw2.setUpdateBy(logonUser.getUserId());
			tsw2.setUpdateDate(new Date());
			reDao.update(tsw, tsw2);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"交接单打印");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
