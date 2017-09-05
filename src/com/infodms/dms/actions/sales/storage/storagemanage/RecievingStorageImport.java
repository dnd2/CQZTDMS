package com.infodms.dms.actions.sales.storage.storagemanage;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
//import com.infodms.dms.common.erpinterface.ErpInterfaceCommon;
import com.infodms.dms.dao.sales.storage.storagemanage.ReceivingStorageDao;
import com.infodms.dms.exception.BizException;
//import com.infodms.dms.po.TsiExpBusVehStoreDetPO;
//import com.infodms.dms.po.TsiExpBusVehStorePO;
import com.infodms.dms.po.TsiExpBusVehStoreDetPO;
import com.infodms.dms.po.TsiExpBusVehStorePO;
import com.infodms.dms.po.TmpTiStorageVhclPO;
import com.infodms.dms.po.TmpTsiImpStorageVehiclePO;
import com.infodms.dms.po.TmpTsiYeidlyVehicleExpPO;
import com.infodms.dms.po.TsiImpStorageVehiclePO;
import com.infodms.dms.po.TsiImpYeidlyVehiclePO;
import com.infodms.dms.po.TmPoseBusinessAreaPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmpTiYeildlyVhclPO;
import com.infodms.dms.po.TmpTtSalesStoragePO;
import com.infodms.dms.po.TtSalesSitPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;


public class RecievingStorageImport extends BaseImport {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final String IMPORT_QUERY_URL = "/jsp/sales/storage/storagemanage/receivingStorage/importDiffResult.jsp";
	private static final String IMPORT_VEH_URL = "/jsp/sales/storage/storagemanage/receivingStorage/importRecievingStorage.jsp";
	private static final String IMPORT_YEILDLY_VEH_URL = "/jsp/sales/storage/storagemanage/receivingStorage/importYeildlyVehicle.jsp";
	private static final String IMPORT_STORAGE_VEH_URL = "/jsp/sales/storage/storagemanage/receivingStorage/importStorageVehicle.jsp";
	private final ReceivingStorageDao dao = ReceivingStorageDao.getInstance();
	private static final String VEH_IMPORT_FAIL_URL = "/jsp/sales/storage/storagemanage/receivingStorage/importReceivingFailur.jsp";
	private static final String VEH_IMPORT_SUCCESS_URL = "/jsp/sales/storage/storagemanage/receivingStorage/importReceivingSuccess.jsp";
	private static final String VEH_IMPORT_STORAGE_SUCCESS_URL = "/jsp/sales/storage/storagemanage/receivingStorage/importStorageSuccess.jsp";
	private static final String VEH_IMPORT_OUT_SUCCESS_URL = "/jsp/sales/storage/storagemanage/receivingStorage/importReceivingSuccessOut.jsp";
	private static final String PRINT_RESERVOIR_POSITION_INFO = "/jsp/sales/storage/storagemanage/receivingStorage/printReservoirPositionInfo.jsp";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat();
	private static final String dataForamStr = "yyyy-MM-dd HH:mm:ss";
	private static Map<String,Object> orderMap;
	private static Map<String,Object> accPerCodeMap ;
	
	/**
	 * 车辆导入结果查询初始化
	 * 
	 */
	public void importQueryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getWarehouseByPoseId(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			act.setForword(IMPORT_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆导入差异结果查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 车辆导入结果查询初始化
	 * 
	 */
	public void importQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String startdate = CommonUtils.checkNull(request.getParamValue("startdate")); //导入日期开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 导入日期结束
			String _type = CommonUtils.checkNull(request.getParamValue("_type")); // 处理类型(2代表导出)
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("startdate", startdate);
			map.put("endDate", endDate);
			if("2".equals(_type)){//导出
				List<Map<String, Object>> list = dao.importDiffExport(map);
				String[] headExport={"导入类型","VIN","导入用户","导入时间","入库类型","物料编码","物料编码","颜色代码","颜色名称","生产日期","发动机号","合格证号","下线日期"};
				String[] columns={"IMP_TYPE","VIN","NAME","CREATE_DATE","IN_TYPE","MATERIAL_CODE","MATERIAL_NAME","COLOR_CODE","COLOR_NAME","PRODUCT_DATE","ENGINE_NO","HEGEZHENG_CODE","OFFLINE_DATE"};
				ToExcel.toReportExcel(act.getResponse(), request,"车辆导入差异结果列表.xls", headExport,columns,list);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.importDiffQuery(map,curPage,Constant.PAGE_SIZE_MAX);
				act.setOutData("ps", ps);
			}
			act.setForword(IMPORT_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆导入差异结果查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 基地入库管理初始化
	 */
	public void openImportInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
//			//查询基地日志表是否被锁定，如果是，不允许导入，同时为三方仓储表加上锁
//			TsiImpYeidlyVehiclePO tp=new TsiImpYeidlyVehiclePO();
//			tp.setIsLock(Constant.IF_TYPE_YES);
//			List tlist=dao.select(tp);
//			if(tlist!=null&&tlist.size()>0){
//				act.setOutData("lockError", "表被锁定，暂无法导入！");
//			}else{
//				act.setOutData("lockError", "");
//				
//			}
			act.setForword(IMPORT_YEILDLY_VEH_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "基地车辆导入初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 三方仓储入库管理初始化
	 */
	public void storageImportInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			//查询三方仓储日志表是否被锁定，如果是，不允许导入，同时为基地日志表加上锁
			
//			TsiImpStorageVehiclePO tp=new TsiImpStorageVehiclePO();
//			tp.setIsLock(Constant.IF_TYPE_YES);
//			List tlist=dao.select(tp);
//			if(tlist!=null&&tlist.size()>0){
//				act.setOutData("lockError", "表被锁定，暂无法导入！");
//			}else{
//				act.setOutData("lockError", "");
//				
//			}
			act.setForword(IMPORT_STORAGE_VEH_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "三方仓储车辆导入初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 出库管理初始化
	 */
	public void openImportInitOut() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setOutData("importFlag", "2");
			act.setForword(IMPORT_VEH_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "城市里程数初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	public void downloadTemplate() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		OutputStream os = null;
		try {
//			String importFlag = CommonUtils.checkNull(request.getParamValue("importFlag")); //1表示入库导入，2表示出库导入
			ResponseWrapper response = act.getResponse();
			// 用于下载传参的
			List<List<Object>> list = new LinkedList<List<Object>>();

			// 标题
			List<Object> listHead = new LinkedList<Object>();
			
			String[] titles={ "车架号*"};
			for (String title : titles) {
				listHead.add(title);
			}
			
			
			list.add(listHead);
			
			// 导出的文件名
			String fileName = "车辆出库信息导入.xls";
			
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			
			os.flush();	
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE, "文件读取错误");
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
	 * 基地车辆导入下载模板
	 */
	public void downloadTemplateYeildly() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();
			// 用于下载传参的
			List<List<Object>> list = new LinkedList<List<Object>>();

			// 标题
			List<Object> listHead = new LinkedList<Object>();
			
			String[] titles={ "车架号*", "物料编码*","物料名称","颜色代码","颜色名称", "生产日期*", "发动机号","合格证","下线日期*","变速箱号"};
			for (String title : titles) {
				listHead.add(title);
			}
			
			list.add(listHead);
			
			// 导出的文件名
			String fileName = "基地车辆信息导入.xls";
			
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			
			os.flush();	
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE, "文件读取错误");
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
	 * 三方仓储导入下载模板
	 */
	public void downloadTemplateStorage() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();
			// 用于下载传参的
			List<List<Object>> list = new LinkedList<List<Object>>();

			// 标题
			List<Object> listHead = new LinkedList<Object>();
			
			String[] titles={ "车架号*"};
			for (String title : titles) {
				listHead.add(title);
			}
			
			list.add(listHead);
			
			// 导出的文件名
			String fileName = "三方仓储车辆信息导入.xls";
			
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			
			os.flush();	
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE, "文件读取错误");
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
	//下载
	public void createXlsFile(List<List<Object>> content,List<Map<String, Object>> listDear,List<Map<String, Object>> listCarPart,String[] dealerHead,String[] carPartHead, OutputStream os) throws ParseException{
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("下载模板", 0);
			if(dealerHead!=null){
				for(int i=0;i<content.size();i++){
					for(int j = 0;j<content.get(i).size();j++){
							// 添加单元格
							sheet.addCell(new Label(j,i,(content.get(i).get(j) != null ? content.get(i).get(j).toString() : "")));
					}	
				}
				WritableSheet sheet1 = workbook.createSheet("物料信息", 1);
					for (int i = 0; i < dealerHead.length; i++) {
							sheet1.addCell(new Label(i, 0, dealerHead[i]));
					}
				if(listDear != null && listDear.size()>0){
					for(int i = 0 ; i < listDear.size() ; i++){
						Map<String, Object> map = listDear.get(i);
						//sheet1.addCell(new Label(0,i+1,String.valueOf(i+1),wcf1));
						
						sheet1.addCell(new Label(0,i+1,CommonUtils.checkNull(map.get("MATERIAL_NAME"))));
						sheet1.addCell(new Label(1,i+1,CommonUtils.checkNull(map.get("MATERIAL_CODE"))));
					}
				}
				//套用多少行公式
				for(int i = 0 ; i < 5 ; i++){
					insertFormula(sheet,1,i+1,"VLOOKUP(C"+(i+2)+",'物料信息'!A:C,2,FALSE)",getDataCellFormat(CellType.STRING_FORMULA));//套用公式
				}
				
				WritableSheet sheet2 = workbook.createSheet("接车员信息", 2);
				for (int i = 0; i < carPartHead.length; i++) {
						sheet2.addCell(new Label(i, 0, carPartHead[i]));
				}
				if(listCarPart != null && listCarPart.size()>0){
					for(int i = 0 ; i < listCarPart.size() ; i++){
						Map<String, Object> map = listCarPart.get(i);
						//sheet1.addCell(new Label(0,i+1,String.valueOf(i+1),wcf1));
						
						sheet2.addCell(new Label(0,i+1,CommonUtils.checkNull(map.get("PER_NAME"))));
						sheet2.addCell(new Label(1,i+1,CommonUtils.checkNull(map.get("PER_CODE"))));
					}
				}
				//套用多少行公式
				for(int i = 0 ; i < 5 ; i++){
					insertFormula(sheet,8,i+1,"VLOOKUP(J"+(i+2)+",'接车员信息'!A:C,2,FALSE)",getDataCellFormat(CellType.STRING_FORMULA));//套用公式
				}
				
				
			}else{
				for(int i=0;i<content.size();i++){
					for(int j = 0;j<content.get(i).size();j++){
							// 添加单元格
							sheet.addCell(new Label(j,i,(content.get(i).get(j) != null ? content.get(i).get(j).toString() : "")));

					}

				}
			}
			workbook.write();
			workbook.close();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertFormula(WritableSheet sheet, Integer col, Integer row,
			   String formula, WritableCellFormat format) {
		try {
			Formula f = new Formula(col, row, formula);
			 sheet.addCell(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public WritableCellFormat getDataCellFormat(CellType type) {
		WritableCellFormat wcf = null;

		try {
			// 字体样式
	
			if (type == CellType.NUMBER || type == CellType.NUMBER_FORMULA) {// 数字
	
			NumberFormat nf = new NumberFormat("#.00");
	
			wcf = new WritableCellFormat(nf);
	
			} else if (type == CellType.DATE || type == CellType.DATE_FORMULA) {// 日期
	
			jxl.write.DateFormat df = new jxl.write.DateFormat(
	
			"yyyy-MM-dd hh:mm:ss");
	
			wcf = new jxl.write.WritableCellFormat(df);
	
			} else {
	
			WritableFont wf = new WritableFont(WritableFont.TIMES, 10,
	
			WritableFont.NO_BOLD, false);// 最后一个为是否italic
	
			wcf = new WritableCellFormat(wf);
	
			}
	
			// 对齐方式
	
			wcf.setAlignment(Alignment.CENTRE);
	
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
	
			// 边框
	
			wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
	
			wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
	
			wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
	
			// 背景色
	
			wcf.setBackground(Colour.WHITE);
	
			wcf.setWrap(true);// 自动换行

		} catch (WriteException e) {

			e.printStackTrace();

		}
			return wcf;

	}
	

	public void recivingExcelOperate() {
	  //清除缓存数据
		clearCacheMap();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String importFlag = CommonUtils.checkNull(request.getParamValue("importFlag")); //1表示入库导入，2表示出库导入
			act.setOutData("importFlag", "2");
			

			long maxSize = 1024 * 1024 * 10;
			if("1".equals(importFlag)){
				// 清空临时表中目标的数据
				//dao.update(" truncate table TMP_TT_SALES_STORAGE", null);
				insertIntoTmpMore(request, "uploadFile", 9, 3, maxSize,Constant.EXPORT_ONLY_SHEET); 
			}else{
				// 清空临时表中目标的数据
				dao.update(" truncate table TMP_TT_SALES_STORAGE", null);
				insertIntoTmpMore(request, "uploadFile", 1, 3, maxSize,Constant.EXPORT_ONLY_SHEET); 
			}
			
			List<ExcelErrors> el = getErrList();

			if (null != el && el.size() > 0) {
				act.setOutData("errorList", el);
				act.setForword(VEH_IMPORT_FAIL_URL);
			} else {
				List<Map> list = getMapList();
				// 将数据插入临时表
				insertTmpStorage(list, logonUser,importFlag);
			
				// 校验临时表数据
				List<ExcelErrors> errorList = null;
				errorList = checkData(logonUser.getUserId().toString(),importFlag, logonUser.getPoseId());

				if (null != errorList) {
					act.setOutData("errorList", errorList);
					act.setForword(VEH_IMPORT_FAIL_URL);
				} else {
					if("1".equals(importFlag)){
						act.setForword(VEH_IMPORT_SUCCESS_URL);
					}else{
						act.setForword(VEH_IMPORT_OUT_SUCCESS_URL);
					}
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE, "文件读取错误");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 基地车辆信息导入
	 */
	public void YeildlyExcelOperate() {
		  //清除缓存数据
			clearCacheMap();
			ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			try {
				//锁定三方仓储日志表
//				TsiImpStorageVehiclePO tvp=new TsiImpStorageVehiclePO();
//				tvp.setIsLock(Constant.IF_TYPE_NO);
//				TsiImpStorageVehiclePO tvp2=new TsiImpStorageVehiclePO();
//				tvp2.setIsLock(Constant.IF_TYPE_YES);
//				dao.update(tvp, tvp2);
				
				RequestWrapper request = act.getRequest();
				long maxSize = 1024 * 1024 * 10;
				act.setOutData("importFlag", "1");
				// 清空临时表中目标的数据
				dao.update(" truncate table TMP_TSI_YEIDLY_VEHICLE_EXP", null);
				insertIntoTmpMore(request, "uploadFile", 10, 3, maxSize,Constant.EXPORT_ONLY_SHEET); 
				
				List<ExcelErrors> el = getErrList();
				
				if (null != el && el.size() > 0) {
					act.setOutData("errorList", el);
					//解锁三方仓储日志表
//					TsiImpStorageVehiclePO tvp3=new TsiImpStorageVehiclePO();
//					tvp3.setIsLock(Constant.IF_TYPE_NO);
//					TsiImpStorageVehiclePO tvp4=new TsiImpStorageVehiclePO();
//					tvp4.setIsLock(Constant.IF_TYPE_YES);
//					dao.update(tvp3, tvp4);
					act.setForword(VEH_IMPORT_FAIL_URL);
				} else {
					List<Map> list = getMapList();
					// 将数据插入临时日志表
					insertTmpTsiYeildly(list, logonUser);
					
					// 校验临时表数据
					List<ExcelErrors> errorList = null;
					errorList = checkDataYeidly(logonUser.getUserId().toString(), logonUser.getPoseId());
					
					if (null != errorList) {
						act.setOutData("errorList", errorList);
						//解锁三方仓储日志表
//						TsiImpStorageVehiclePO tvp3=new TsiImpStorageVehiclePO();
//						tvp3.setIsLock(Constant.IF_TYPE_NO);
//						TsiImpStorageVehiclePO tvp4=new TsiImpStorageVehiclePO();
//						tvp4.setIsLock(Constant.IF_TYPE_YES);
//						dao.update(tvp3, tvp4);
						act.setForword(VEH_IMPORT_FAIL_URL);
					} else {
						act.setForword(VEH_IMPORT_SUCCESS_URL);
					}
				}
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE, "文件读取错误");
				logger.error(logonUser, e1);
				act.setException(e1);
			}

	}
	/**
	 * 三方仓储车辆信息导入
	 */
	public void storageExcelOperate() {
		  //清除缓存数据
			clearCacheMap();
			ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			try {
				//锁定基地日志表，不允许导入
//				TsiImpYeidlyVehiclePO tvp=new TsiImpYeidlyVehiclePO();
//				tvp.setIsLock(Constant.IF_TYPE_NO);
//				TsiImpYeidlyVehiclePO tvp2=new TsiImpYeidlyVehiclePO();
//				tvp2.setIsLock(Constant.IF_TYPE_YES);
//				dao.update(tvp, tvp2);
				
				RequestWrapper request = act.getRequest();
				long maxSize = 1024 * 1024 * 10;
				act.setOutData("importFlag", "3");
				// 清空临时表中目标的数据
				dao.update(" truncate table TMP_TSI_IMP_STORAGE_VEHICLE", null);
				insertIntoTmpMore(request, "uploadFile", 1, 3, maxSize,Constant.EXPORT_ONLY_SHEET); 
				
				List<ExcelErrors> el = getErrList();
				
				if (null != el && el.size() > 0) {
					act.setOutData("errorList", el);
					//解锁基地日志表
//					TsiImpYeidlyVehiclePO tvp1=new TsiImpYeidlyVehiclePO();
//					tvp1.setIsLock(Constant.IF_TYPE_NO);
//					TsiImpYeidlyVehiclePO tvp3=new TsiImpYeidlyVehiclePO();
//					tvp3.setIsLock(Constant.IF_TYPE_YES);
//					dao.update(tvp1, tvp3);
					act.setForword(VEH_IMPORT_FAIL_URL);
				} else {
					List<Map> list = getMapList();
					// 将数据插入临时日志表
					insertTmpTsiStorage(list, logonUser);
					
					// 校验临时表数据
					List<ExcelErrors> errorList = null;
					errorList = checkDataStorage(logonUser.getUserId().toString(), logonUser.getPoseId());
					
					if (null != errorList) {
						act.setOutData("errorList", errorList);
						//解锁基地日志表
//						TsiImpYeidlyVehiclePO tvp4=new TsiImpYeidlyVehiclePO();
//						tvp4.setIsLock(Constant.IF_TYPE_NO);
//						TsiImpYeidlyVehiclePO tvp5=new TsiImpYeidlyVehiclePO();
//						tvp5.setIsLock(Constant.IF_TYPE_YES);
//						dao.update(tvp4, tvp5);
						act.setForword(VEH_IMPORT_FAIL_URL);
					} else {						
						act.setForword(VEH_IMPORT_STORAGE_SUCCESS_URL);
					}
				}
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE, "文件读取错误");
				logger.error(logonUser, e1);
				act.setException(e1);
			}

	}
	private String getYeidlySql(){
		StringBuffer sql= new StringBuffer();
		sql.append("insert into TSI_IMP_YEIDLY_VEHICLE\n" );
		sql.append("  (ID,\n" );
		sql.append("   VIN,\n" );
		sql.append("   MATERIAL_CODE,\n" );
		sql.append("   MATERIAL_NAME,\n" );
		sql.append("   COLOR_CODE,\n" );
		sql.append("   COLOR_NAME,\n" );
		sql.append("   PRODUCT_DATE,\n" );
		sql.append("   ENGINE_NO,\n" );
		sql.append("   GEARBOX_NO,\n" );
		sql.append("   HEGEZHENG_CODE,\n" );
		sql.append("   OFFLINE_DATE,\n" );
		sql.append("   CREATE_DATE,\n" );
		sql.append("   CREATE_BY,\n" );
		sql.append("   SERIES_ID,\n" );
		sql.append("   MODEL_ID,\n" );
		sql.append("   PACKAGE_ID,\n" );
		sql.append("   MATERIAL_ID)\n" );
		sql.append("  select tt.id,\n" );
		sql.append("         tt.vin,\n" );
		sql.append("         tt.material_code,\n" );
		sql.append("         tt.material_name,\n" );
		sql.append("         tt.color_code,\n" );
		sql.append("         tt.color_name,\n" );
		sql.append("         to_date(tt.product_date, 'yyyy-mm-dd hh24:mi:ss'),\n" );
		sql.append("         tt.engine_no,\n" );
		sql.append("         tt.gearbox_no,\n" );
		sql.append("         tt.hegezheng_code,\n" );
		sql.append("         to_date(tt.offline_date, 'yyyy-mm-dd hh24:mi:ss'),\n" );
		sql.append("         sysdate,\n" );
		sql.append("         tt.create_by,\n" );
		sql.append("         (select mat.SERIES_ID from VW_MATERIAL_GROUP_MAT mat where mat.material_code=tt.material_code),\n" );
		sql.append("         (select mat.MODEL_ID from VW_MATERIAL_GROUP_MAT mat where mat.material_code=tt.material_code),\n" );
		sql.append("         (select mat.PACKAGE_ID from VW_MATERIAL_GROUP_MAT mat where mat.material_code=tt.material_code),\n" );
		sql.append("         (select mat.MATERIAL_ID from VW_MATERIAL_GROUP_MAT mat where mat.material_code=tt.material_code)\n" );
		sql.append("    from tmp_tsi_yeidly_vehicle_exp tt");
		return sql.toString();
	}
	
	private String getStorageSql(){
		StringBuffer sql= new StringBuffer();
		sql.append("insert into tsi_imp_storage_vehicle\n" );
		sql.append("  (ID, VIN, REMARK,CREATE_DATE, CREATE_BY)\n" );
		sql.append("  select tt.id, tt.vin, '',sysdate, tt.create_by\n" );
		sql.append("    from TMP_TSI_IMP_STORAGE_VEHICLE tt");
		return sql.toString();
	}
	private void clearCacheMap() {
		if(null != orderMap){
			orderMap.clear();
		}
	
		if(null != accPerCodeMap){
			accPerCodeMap.clear();
		}
		
	}
	
	private List<ExcelErrors> checkData(String userId,String importFlag, Long poseId) {
		
		
		TmpTtSalesStoragePO selectPo = new TmpTtSalesStoragePO();
		selectPo.setUserId(userId);
		List pos = dao.select(selectPo);
		ExcelErrors errors = null;

		StringBuffer errorInfo = new StringBuffer("");
		boolean isError = false;
		TmpTtSalesStoragePO po;
		List<ExcelErrors> errorList = new LinkedList<ExcelErrors>();
		for (int i = 0; i < pos.size(); i++) {
			errors = new ExcelErrors();
			// 取ddd得TmpYearlyPlanPO
			po = (TmpTtSalesStoragePO) pos.get(i);
			// 取得行号
			String rowNum = po.getRowNumber();
    
			
			try {
				
				if("1".equals(importFlag)){
					checkVin(errorInfo, po, isError, poseId);//车架号入库校验
					// 检查物料代码
					checkMetariel(errorInfo, po, isError);
					// 检查特殊订单号
					checkSpecialOrderNo(errorInfo, po, isError);
					//检查接车员代码
					//checkAccPersonCode(errorInfo, po, isError);
					
					//checkErpOrderId(errorInfo, po, isError);
					
					if(null != po.getOfflineDate() || !"".equals(po.getOfflineDate()) ){
						if (!isValidDate(po.getOfflineDate())) {
							isError = true;
							errorInfo.append("下线时间格式不正确;");
						}
					}else{
						isError = true;
						errorInfo.append("下线时间不能为空;");
					}
						

					if(null != po.getProdtuctDate() || !"".equals(po.getProdtuctDate()) ){
						if (!isValidDate(po.getProdtuctDate())) {
							isError = true;
							errorInfo.append("生产日期格式不正确;");
						}
					}else{
						isError = true;
						errorInfo.append("生产日期不能为空;");
					}
				}else{
					checkVinOut(errorInfo, po, isError, poseId);//车架号出库校验
				}
				
				
				//检查出入库类型
				//checkInOutDesc(errorInfo, po, isError);
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

		if (errorList.size() > 0 || isError ) {
			return errorList;
		} else {
			return null;
		}

	}
	private List<ExcelErrors> checkDataYeidly(String userId, Long poseId) {
		boolean isError = false;
		StringBuffer errorInfo = new StringBuffer("");
		List<ExcelErrors> errorList = new LinkedList<ExcelErrors>();
		ExcelErrors errors = null;
		//查询临时表中是否存在重复车架号的数据
		List<Map<String, Object>> tlist=dao.getTmpRepeatRecord();
		if(tlist!=null&&tlist.size()>0){
			errors = new ExcelErrors();
			isError = true;
			errors.setRowNum(2);
			String info="存在"+tlist.get(0).get("RE_NUM")+"条车架号为："+tlist.get(0).get("VIN")+"的数据";
			errors.setErrorDesc(info);
			errorList.add(errors);
		}else{
			TmpTsiYeidlyVehicleExpPO selectPo = new TmpTsiYeidlyVehicleExpPO();
			selectPo.setCreateBy(Long.parseLong(userId));
			List pos = dao.select(selectPo);
			
			TmpTsiYeidlyVehicleExpPO po;
			
			for (int i = 0; i < pos.size(); i++) {
				errors = new ExcelErrors();
				
				po = (TmpTsiYeidlyVehicleExpPO) pos.get(i);
				// 取得行号
				String rowNum = po.getRowNumber();
				
				try {
						//检查车架号
						checkVinYeidly(errorInfo, po, isError, poseId);//车架号入库校验
						// 检查物料代码
						checkMetarielYeidly(errorInfo, po, isError);
						
						
						if(null != po.getOfflineDate() || !"".equals(po.getOfflineDate()) ){
							if (!isValidDate(po.getOfflineDate())) {
								isError = true;
								errorInfo.append("下线时间格式不正确;");
							}
						}else{
							isError = true;
							errorInfo.append("下线时间不能为空;");
						}
							

						if(null != po.getProductDate() || !"".equals(po.getProductDate()) ){
							if (!isValidDate(po.getProductDate())) {
								isError = true;
								errorInfo.append("生产日期格式不正确;");
							}
						}else{
							isError = true;
							errorInfo.append("生产日期不能为空;");
						}
					
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
		
		

		if (errorList.size() > 0 || isError ) {
			return errorList;
		} else {
			return null;
		}

	}
	/**
	 * 三方仓储导入数据校验
	 * @param userId
	 * @param poseId
	 * @return
	 */
	private List<ExcelErrors> checkDataStorage(String userId, Long poseId) {
		boolean isError = false;
		StringBuffer errorInfo = new StringBuffer("");
		List<ExcelErrors> errorList = new LinkedList<ExcelErrors>();
		ExcelErrors errors = null;
		//查询临时表中是否存在重复车架号的数据
		List<Map<String, Object>> tlist=dao.getTmpRepeatRecordStorage();
		if(tlist!=null&&tlist.size()>0){
			errors = new ExcelErrors();
			isError = true;
			errors.setRowNum(2);
			String info="存在"+tlist.get(0).get("RE_NUM")+"条车架号为："+tlist.get(0).get("VIN")+"的数据";
			errors.setErrorDesc(info);
			errorList.add(errors);
		}else{
			TmpTsiImpStorageVehiclePO selectPo = new TmpTsiImpStorageVehiclePO();
			selectPo.setCreateBy(Long.parseLong(userId));
			List pos = dao.select(selectPo);
			
			TmpTsiImpStorageVehiclePO po;
			
			for (int i = 0; i < pos.size(); i++) {
				errors = new ExcelErrors();
				
				po = (TmpTsiImpStorageVehiclePO) pos.get(i);
				// 取得行号
				String rowNum = po.getRowNumber();
				
				try {
						//检查车架号
						checkVinStorage(errorInfo, po, isError, poseId);//车架号入库校验
						//检查入库类型
						//checkInType(errorInfo, po, isError);
					
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
		
		

		if (errorList.size() > 0 || isError ) {
			return errorList;
		} else {
			return null;
		}

	}
	private void checkErpOrderId(StringBuffer errorInfo,
			TmpTtSalesStoragePO po, boolean isError) {
		String eprOrderNo = po.getErpOrderId();
		if(null == eprOrderNo && !"".equals(eprOrderNo)){
			isError = true;
			errorInfo.append("车辆工单号不能为空;");
		}
		
	}

	private void checkAccPersonCode(StringBuffer errorInfo,
			TmpTtSalesStoragePO po, boolean isError) {
		String accPerCode = po.getAccPerCode();
		if(null != accPerCode &&!"".equals(accPerCode)){
			accPerCodeMap = dao.getAccPersonId(accPerCode);
			if(null == accPerCodeMap || accPerCodeMap.isEmpty()){
				isError = true;
				errorInfo.append("接车员不正确;");
			}
		}else{
			isError = true;
			errorInfo.append("接车员代码不能为空;");
		}
		
	}

	private void checkSpecialOrderNo(StringBuffer errorInfo,
			TmpTtSalesStoragePO po, boolean isError) {
		String orderNo = po.getSpecialOrderNo();
		if(null != orderNo &&!"".equals(orderNo)){
			orderMap = dao.getSepcialNo(orderNo);
			if(null == orderMap || orderMap.isEmpty()){
				isError = true;
				errorInfo.append("特殊订单号不正确;");
			}
		}
	}

	//检验物料代码
	private void checkMetariel(StringBuffer errorInfo, TmpTtSalesStoragePO po,
			boolean isError) {
		String specialOrderNo = po.getSpecialOrderNo();
		String metarielCode = po.getMaterialCode();
		if("".equals(specialOrderNo)){
			if(null == metarielCode || "".equals(metarielCode)){
				isError = true;
				errorInfo.append("物料代码不能为空;");
			}else{
				
				List<Map<String,Object>> mats = dao.getMatarielByCode(metarielCode);
				if(null == mats || mats.size() == 0){
					isError = true;
					errorInfo.append("物料代码不存在，或者物料代码填写错误;");
				}
			}
		}
		
	}
	//检验物料代码
	private void checkMetarielYeidly(StringBuffer errorInfo, TmpTsiYeidlyVehicleExpPO po,
			boolean isError) {
		
		String metarielCode = po.getMaterialCode();
		if(null == metarielCode || "".equals(metarielCode)){
			isError = true;
			errorInfo.append("物料代码不能为空;");
		}else{
			
			List<Map<String,Object>> mats = dao.getMatarielByCode(metarielCode);
			if(null == mats || mats.size() == 0){
				isError = true;
				errorInfo.append("物料代码不存在，或者物料代码填写错误;");
			}
		}
		
		
	}
	//检验颜色代码
	private void checkColorYeidly(StringBuffer errorInfo, TmpTsiYeidlyVehicleExpPO po,
			boolean isError) {
		
		String colorCode = po.getColorCode();
		if(null == colorCode || "".equals(colorCode)){
			isError = true;
			errorInfo.append("颜色代码不能为空;");
		}else{
			
//			List<Map<String,Object>> mats = dao.getMatarielByCode(metarielCode);
//			if(null == mats || mats.size() == 0){
//				isError = true;
//				errorInfo.append("物料代码不存在，或者物料代码填写错误;");
//			}
		}
		
		
	}
	//判断车架号
	private void checkVin(StringBuffer errorInfo, TmpTtSalesStoragePO po, boolean isError, Long posId) throws Exception {
		String vin = po.getVin();
		if(null == vin ||  "".equals(vin)){
			isError = true;
			errorInfo.append("车架号不能为空;");
		}else{
			if(vin.length() != 17){
				isError = true;
				errorInfo.append("VIN长度不正确，必须为17位;");
			}else{
				List<Map<String, Object>>  mapList = dao.getImportVin(vin,posId);
				if(null != mapList && !mapList.isEmpty()){
				   if(mapList.size() > 0){
					   isError = true;
					   errorInfo.append("车架号不能重复;");
					   if(mapList.get(0).get("FLAG").toString().equals("0")) {
						   errorInfo.append("职位无产地权限;");
					   }
				   }
				}
			}
			
		}
		
	}
	private void checkVinYeidly(StringBuffer errorInfo, TmpTsiYeidlyVehicleExpPO po, boolean isError, Long posId) throws Exception {
		String vin = po.getVin();
		if(null == vin ||  "".equals(vin)){
			isError = true;
			errorInfo.append("车架号不能为空;");
		}else{
			if(vin.length() != 17){
				isError = true;
				errorInfo.append("VIN长度不正确，必须为17位;");
			}else{
				List<Map<String, Object>>  mapList = dao.getImportVinYeidly(vin,posId);
				if(null != mapList && !mapList.isEmpty()){
				   if(mapList.size() > 0){
					  
					   //errorInfo.append("车架号不能重复;");
					   if(mapList.get(0).get("FLAG").toString().equals("0")) {
						   isError = true;
						   errorInfo.append("职位无产地权限;");
					   }
				   }
				}
			}
			
		}
		
	}
	private void checkVinStorage(StringBuffer errorInfo, TmpTsiImpStorageVehiclePO po, boolean isError, Long posId) throws Exception {
		String vin = po.getVin();
		if(null == vin ||  "".equals(vin)){
			isError = true;
			errorInfo.append("车架号不能为空;");
		}else{
			if(vin.length() != 17){
				isError = true;
				errorInfo.append("VIN长度不正确，必须为17位;");
			}else{
//				List<Map<String, Object>>  mapList = dao.getImportVinYeidly(vin,posId);
//				if(null != mapList && !mapList.isEmpty()){
//				   if(mapList.size() > 0){
//					  
//					   //errorInfo.append("车架号不能重复;");
//					   if(mapList.get(0).get("FLAG").toString().equals("0")) {
//						   isError = true;
//						   errorInfo.append("职位无产地权限;");
//					   }
//				   }
//				}
			}
			
		}
		
	}
	//出库导入：判断车架号
	private void checkVinOut(StringBuffer errorInfo, TmpTtSalesStoragePO po, boolean isError, Long poseId) throws Exception {
		String vin = po.getVin();
		if(null == vin ||  "".equals(vin)){
			isError = true;
			errorInfo.append("车架号不能为空;");
		}else{
			if(vin.length() != 17){
				isError = true;
				errorInfo.append("VIN长度不正确，必须为17位;");
			}else{
				
				List<Map<String, Object>>  mapList = dao.getImportVin(vin, poseId);
				if(null != mapList && !mapList.isEmpty()&&mapList.size() > 0){
					Map<String, Object> map=mapList.get(0);
					String lifeCycle= map.get("LIFE_CYCLE").toString();//库存状态
					if(!lifeCycle.equals(Constant.VEHICLE_LIFE_02.toString())){
						isError = true;
						errorInfo.append("车架号不是在库状态;");
					}
					if(map.get("FLAG").toString().equals("0")) {
						isError = true;
						errorInfo.append("职位无产地权限;");
					}
				}else{
					isError = true;
					errorInfo.append("车架号在库中不存在");
				}
			}
			
		}
		
	}
	/**
	 * 检查出入库类型
	 * @param errorInfo
	 * @param po
	 * @param isError
	 * @throws Exception
	 */
	private void checkInOutDesc(StringBuffer errorInfo, TmpTtSalesStoragePO po,
				boolean isError) throws Exception {
			String inOutDesc = po.getInOutDesc();
			if(null == inOutDesc ||  "".equals(inOutDesc)){
				isError = true;
				errorInfo.append("出入库类型不能为空");
			}
			
		}
	private void checkInType(StringBuffer errorInfo, TmpTsiImpStorageVehiclePO po,
			boolean isError) throws Exception {
		String inOutDesc = po.getInType();
		if(null == inOutDesc ||  "".equals(inOutDesc)){
			isError = true;
			errorInfo.append("出入库类型不能为空");
		}
		
	}
	public boolean isValidDate(String s) {
		try {
			dateFormat.applyPattern(dataForamStr);
			dateFormat.parse(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	private void insertTmpTsiStorage(List<Map> list, AclUserBean logonUser) {
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
				if(!isEmptyRow(cells)){
					parseCellsStorage(key, cells, logonUser);
				}
				
			}
		}

	}
	private void insertTmpTsiYeildly(List<Map> list, AclUserBean logonUser) {
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
				if(!isEmptyRow(cells)){
					parseCellsYeidly(key, cells, logonUser);
				}
				
			}
		}

	}

	private void insertTmpStorage(List<Map> list, AclUserBean logonUser,String importFlag) {
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
				if(!isEmptyRow(cells)){
					parseCells(key, cells, logonUser,importFlag);
				}
				
			}
		}

	}
	/** 解析某一列的数据 **/
	private void parseCellsYeidly(String rowNum, Cell[] cells, AclUserBean logonUser) {

		
		try {
			TmpTsiYeidlyVehicleExpPO po=new TmpTsiYeidlyVehicleExpPO();
			po.setId(Long.parseLong(SequenceManager.getSequence("")));
			po.setRowNumber(rowNum.trim());
			po.setVin((cells.length >= 1 ? subCell(cells[0].getContents().trim()) : ""));//车架号
			po.setMaterialCode((cells.length >= 2 ? subCell(cells[1].getContents().trim()) : ""));//物料编码
			po.setMaterialName((cells.length >= 3 ? subCell(cells[2].getContents().trim()) : ""));//物料名称
			po.setColorCode(cells.length >= 4 ? subCell(cells[3].getContents().trim()) : "");//颜色代码
			po.setColorName(cells.length >= 5 ? subCell(cells[4].getContents().trim()) : "");//颜色名称
			po.setProductDate(cells.length >= 6 ? parseExlDate(cells[5]) : "");//生产日期
			po.setEngineNo(cells.length >= 7 ? subCell(cells[6].getContents().trim()) : "");//发动机号
			po.setHegezhengCode(cells.length >= 8 ? subCell(cells[7].getContents().trim()) : "");//合格证号
			po.setOfflineDate(cells.length >= 9 ? parseExlDate(cells[8]) : "");//下线日期
			po.setGearboxNo(cells.length >= 10 ? subCell(cells[9].getContents().trim()) : "");//变速箱号
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
			dao.insert(po);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "出入库车辆信息导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/** 解析某一列的数据 **/
	private void parseCellsStorage(String rowNum, Cell[] cells, AclUserBean logonUser) {

		
		try {
			TmpTsiImpStorageVehiclePO po=new TmpTsiImpStorageVehiclePO();
			po.setId(Long.parseLong(SequenceManager.getSequence("")));
			po.setRowNumber(rowNum.trim());
			po.setVin((cells.length >= 1 ? subCell(cells[0].getContents().trim()) : ""));//车架号
			//po.setInType((cells.length >= 2 ? subCell(cells[1].getContents().trim()) : ""));//入库类型
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
			dao.insert(po);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "出入库车辆信息导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/** 解析某一列的数据 **/
	private void parseCells(String rowNum, Cell[] cells, AclUserBean logonUser,String importFlag) {

		
		try {
			if("1".equals(importFlag)){//入库导入
//				TmpTiYeildlyVhclPO po = new TmpTiYeildlyVhclPO();
//				po.setRowNumber(rowNum.trim());
//				po.setVin((cells.length >= 1 ? subCell(cells[0].getContents().trim()) : ""));//车架号
//				po.setMaterialCode((cells.length >= 2 ? subCell(cells[1].getContents().trim()) : ""));//物料编码
//				po.setMaterialName((cells.length >= 3 ? subCell(cells[2].getContents().trim()) : ""));//物料名称
//				po.setColorCode(cells.length >= 4 ? subCell(cells[3].getContents().trim()) : "");//颜色代码
//				po.setColorName(cells.length >= 5 ? subCell(cells[4].getContents().trim()) : "");//颜色名称
//				po.setProductDate(cells.length >= 6 ? parseExlDate(cells[5]) : "");//生产日期
//				po.setEngineNo(cells.length >= 7 ? subCell(cells[6].getContents().trim()) : "");//发动机号
//				po.setHegezhengCode(cells.length >= 8 ? subCell(cells[7].getContents().trim()) : "");//合格证号
//				po.setOfflineDate(cells.length >= 9 ? parseExlDate(cells[8]) : "");//下线日期
//				po.setCreateBy(logonUser.getUserId());
//				po.setCreateDate(new Date());
//				dao.insert(po);
			}else{//出库导入
				TmpTtSalesStoragePO po = new TmpTtSalesStoragePO();
				po.setRowNumber(rowNum.trim());
				po.setVin((cells.length >= 1 ? subCell(cells[0].getContents().trim()) : ""));//车架号
				//po.setInOutDesc(cells.length >= 2 ? subCell(cells[1].getContents().trim()) : "");//出入库类型
				dateFormat.applyPattern(dataForamStr);
				String storageDate = dateFormat.format(new Date());
				po.setOrgStorageDate(storageDate);
				po.setUserId(logonUser.getUserId().toString());
				dao.insert(po);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "出入库车辆信息导入");
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
	
	private String  parseExlDate(Cell c){
		 CellType type = c.getType();  
		  
         if (type.equals(CellType.DATE)) {  
             // 日期 类型的处理  
             DateCell dc = (DateCell) c;  
             Date jxlDate = dc.getDate();  
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");  
             sdf.setTimeZone(TimeZone.getTimeZone("GMT"));  
            return sdf.format(jxlDate);
         }else{
        	 return "errorForamt";
         }
         
	}
	
	public void expressResultSelectY() {

		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 澶勭悊褰撳墠椤�
			int pageSize = 10000;
			PageResult<Map<String, Object>> ps = dao.importQueryYeidly(logonUser
					.getUserId().toString(), pageSize, curPage);
			List<Map<String, Object>> temList = ps.getRecords();
			act.setOutData("temList", temList);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库车辆信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	public void expressResultSelectSto() {

		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			int pageSize = 10000;
			PageResult<Map<String, Object>> ps = dao.importQueryStorage(logonUser.getUserId().toString(), pageSize, curPage);
			List<Map<String, Object>> temList = ps.getRecords();
			act.setOutData("temList", temList);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库车辆信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	public void expressResultSelect() {

		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 10000;
			PageResult<Map<String, Object>> ps = dao.importQuery(logonUser
					.getUserId().toString(), pageSize, curPage);
			List<Map<String, Object>> temList = ps.getRecords();
			act.setOutData("temList", temList);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库车辆信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
	/**
	 * 通过车架号检查车辆状态
	 * @param vin
	 * @return
	 */
	private boolean checkVehicleStatus(String vin, List<TmVehiclePO> tlist) {
		boolean flag=true;
		if(tlist!=null&&tlist.size()>0){
			TmVehiclePO tvp2=(TmVehiclePO) tlist.get(0);
			Integer lifeCycle=tvp2.getLifeCycle();
			if(!lifeCycle.equals(Constant.VEHICLE_LIFE_06)){//验证不通过
				//更新日志表的状态为已处理，并且添加备注为重复入库
				TsiImpYeidlyVehiclePO tivp=new TsiImpYeidlyVehiclePO();
				tivp.setVin(vin);
				TsiImpYeidlyVehiclePO tivp2=new TsiImpYeidlyVehiclePO();
				tivp2.setDealFlag(Constant.IF_TYPE_YES);
				tivp2.setRemark("重复入库");
				dao.update(tivp, tivp2);
				flag=false;
			}
		}
		return flag;
	}
	/**
	 * 基地信息导入保存
	 */
	public void importRebaeSave(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			//确定导入之前插入日志表
			dao.insert(getYeidlySql());
			
			long count = 0;
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			int pageSize = 10000;
			PageResult<Map<String, Object>> ps = dao.importQueryYeidly(logonUser.getUserId().toString(), pageSize, curPage);
			List<Map<String, Object>> temList = ps.getRecords();
			
			for(Map<String,Object> map : temList){
				String vin = map.get("VIN").toString();
				TmVehiclePO tvp=new TmVehiclePO();
				tvp.setVin(vin);
				List<TmVehiclePO> tlist=dao.select(tvp);
				boolean flag=checkVehicleStatus(vin, tlist); //校验车辆状态
				if(flag==true){//针对验证通过的验证
					//------获取待处理字段---start
					String id = map.get("ID").toString();//临时表ID
					String materialCode = map.get("MATERIAL_CODE").toString();
					String materialName = map.get("MATERIAL_NAME") == null ?  "" : map.get("MATERIAL_NAME").toString();
					String colorCode = map.get("COLOR_CODE") == null ?  "" : map.get("COLOR_CODE").toString();
					String colorName = map.get("COLOR_NAME") == null ?  "" : map.get("COLOR_NAME").toString();
					
					Map<String,Object> materailInfo= dao.getVehicleInfo(map.get("MATERIAL_CODE").toString());
					String	materialId = materailInfo.get("MATERIAL_ID") +"";
					//配置
					BigDecimal packageId = (BigDecimal)materailInfo.get("PACKAGE_ID");
					//车型
					BigDecimal modelId = (BigDecimal)materailInfo.get("MODEL_ID");
					//车系
					BigDecimal seriesId = (BigDecimal)materailInfo.get("SERIES_ID");
					
					//生产日期
					String productDateStr =map.get("PRODUCT_DATE").toString();
					Date productDate =dateFormat.parse(productDateStr);
					//发动机号
					String engineNo = map.get("ENGINE_NO") == null ?  "" : map.get("ENGINE_NO").toString() ;
					//合格证号
					String hegezhengCode = map.get("HEGEZHENG_CODE") == null ?  "" : map.get("HEGEZHENG_CODE").toString() ;
					//下线日期
					String offLineString =CommonUtils.checkNull(map.get("OFFLINE_DATE"));
					Date offlineDate =dateFormat.parse(offLineString);
					//变速箱号
					String gearboxNo = map.get("GEARBOX_NO") == null ?  "" : map.get("GEARBOX_NO").toString() ;
					List<Map<String, Object>> alist = dao.getYieldlyAndWarehouse(materialId);
					Long areaId=null;//产地ID
					Long warehouseId=null;//仓库ID
					
					if(alist!=null&&alist.size()>0){
						areaId = Long.parseLong(alist.get(0).get("AREA_ID").toString());
						warehouseId = Long.parseLong(alist.get(0).get("WAREHOUSE_ID").toString());
					}
					
					//------获取待处理字段---end
					TmpTiYeildlyVhclPO ttp=new TmpTiYeildlyVhclPO();
					ttp.setVin(vin);
					List plist=dao.select(ttp);
					if(plist!=null&&plist.size()>0){
						//临时表中存在车架号数据时。表示验证不通过，删除临时表中的记录
						TmpTiYeildlyVhclPO ttpn=new TmpTiYeildlyVhclPO();
						ttpn.setVin(vin);
						dao.delete(ttpn);
						//更新日志表的状态为已处理，记录备注重复导入
						TsiImpYeidlyVehiclePO tivp=new TsiImpYeidlyVehiclePO();
						tivp.setVin(id);
						TsiImpYeidlyVehiclePO tivp2=new TsiImpYeidlyVehiclePO();
						tivp2.setDealFlag(Constant.IF_TYPE_YES);
						tivp2.setRemark("重复导入");
						dao.update(tivp, tivp2);
						//重新插入一条记录到临时表
						//向临时表中插入数据，状态为待处理
						TmpTiYeildlyVhclPO ttvp=new TmpTiYeildlyVhclPO();
						ttvp.setId(id);
						ttvp.setVin(vin);
						ttvp.setMaterialCode(materialCode);
						ttvp.setMaterialName(materialName);
						ttvp.setColorCode(colorCode);
						ttvp.setColorName(colorName);
						ttvp.setProductDate(productDate);
						ttvp.setEngineNo(engineNo);
						ttvp.setGearboxNo(gearboxNo);
						ttvp.setHegezhengCode(hegezhengCode);
						ttvp.setOfflineDate(offlineDate);
						ttvp.setSeriesId(seriesId.longValue());
						ttvp.setModelId(modelId.longValue());
						ttvp.setPackageId(packageId.longValue());
						ttvp.setSeriesId(seriesId.longValue());
						ttvp.setCreateBy(logonUser.getUserId());
						ttvp.setCreateDate(new Date());
						ttvp.setIsDeal(Constant.IF_TYPE_NO);//待处理
						dao.insert(ttvp);
						
					}else{//若临时表中不存在，需验证三方仓储中是否存在
						
						TmpTiStorageVhclPO tsp=new TmpTiStorageVhclPO();
						tsp.setVin(vin);
						List slist=dao.select(tsp);
						if(slist!=null&&slist.size()>0){//存在则验证通过
							
							if(tlist!=null&&tlist.size()>0){//更新
								TmVehiclePO tvpo=new TmVehiclePO();
								tvpo.setVin(vin);
								TmVehiclePO tvpw=new TmVehiclePO();
								
								tvpw.setYieldly(areaId);//产地
								tvpw.setWarehouseId(warehouseId);//仓库
								tvpw.setSeriesId(seriesId.longValue());
								tvpw.setModelId(modelId.longValue());
								tvpw.setPackageId(packageId.longValue());
								tvpw.setMaterialId(Long.parseLong(materialId));
								tvpw.setColor(colorName);
								tvpw.setProductDate(productDate);
								tvpw.setEngineNo(engineNo);
								tvpw.setGearboxNo(gearboxNo);
								tvpw.setHegezhengCode(hegezhengCode);
								tvpw.setOfflineDate(offlineDate);
								tvpw.setLifeCycle(Constant.VEHICLE_LIFE_02);//车厂库存
								tvpw.setOrgStorageDate(new Date());//更新车厂入库时间
								tvpw.setUpdateBy(logonUser.getUserId());
								tvpw.setUpdateDate(new Date());
								dao.update(tvpo, tvpw);
								
							}else{//新增
								//根据职位ID获取区域信息
//								TmPoseBusinessAreaPO tap=new TmPoseBusinessAreaPO();
//								tap.setPoseId(logonUser.getPoseId());
//								List alist=dao.select(tap);
								//根据物料获得产地和仓库
								
								TmVehiclePO tvpw=new TmVehiclePO();
								tvpw.setVehicleId(Long.parseLong(SequenceManager.getSequence("")));
								tvpw.setVin(vin);
								//生命周期
								tvpw.setLifeCycle(Constant.VEHICLE_LIFE_02);
								//车辆状态
								tvpw.setLockStatus(Constant.LOCK_STATUS_01);
								
								tvpw.setYieldly(areaId);//产地
								tvpw.setWarehouseId(warehouseId);//仓库
								tvpw.setSeriesId(seriesId.longValue());
								tvpw.setModelId(modelId.longValue());
								tvpw.setPackageId(packageId.longValue());
								tvpw.setMaterialId(Long.parseLong(materialId));
								tvpw.setColor(colorName);
								tvpw.setProductDate(productDate);
								tvpw.setEngineNo(engineNo);
								tvpw.setGearboxNo(gearboxNo);
								tvpw.setHegezhengCode(hegezhengCode);
								tvpw.setOfflineDate(offlineDate);
								tvpw.setOrgStorageDate(new Date());//添加车厂入库时间
								dao.insert(tvpw);
							}
							//删除两张临时表中的数据，更新日志记录为已处理
							TmpTiYeildlyVhclPO ttpn=new TmpTiYeildlyVhclPO();
							ttpn.setVin(vin);
							dao.delete(ttpn);
							TmpTiStorageVhclPO ttpv=new TmpTiStorageVhclPO();
							ttpv.setVin(vin);
							dao.delete(ttpv);
							//更新日志表 的处理标志
							TsiImpYeidlyVehiclePO tivp=new TsiImpYeidlyVehiclePO();
							tivp.setVin(id);
							TsiImpYeidlyVehiclePO tivp2=new TsiImpYeidlyVehiclePO();
							tivp2.setDealFlag(Constant.IF_TYPE_YES);
							tivp2.setRemark("导入成功");
							dao.update(tivp, tivp2);
						}else{
							//向临时表中插入数据，状态为待处理
							TmpTiYeildlyVhclPO ttvp=new TmpTiYeildlyVhclPO();
							ttvp.setId(id);
							ttvp.setVin(vin);
							ttvp.setMaterialCode(materialCode);
							ttvp.setMaterialName(materialName);
							ttvp.setColorCode(colorCode);
							ttvp.setColorName(colorName);
							ttvp.setProductDate(productDate);
							ttvp.setEngineNo(engineNo);
							ttvp.setGearboxNo(gearboxNo);
							ttvp.setHegezhengCode(hegezhengCode);
							ttvp.setOfflineDate(offlineDate);
							ttvp.setMaterialId(Long.parseLong(materialId));//添加物料ID
							ttvp.setPackageId(packageId.longValue());
							ttvp.setModelId(modelId.longValue());
							ttvp.setSeriesId(seriesId.longValue());
							ttvp.setCreateBy(logonUser.getUserId());
							ttvp.setCreateDate(new Date());
							ttvp.setIsDeal(Constant.IF_TYPE_NO);//待处理
							dao.insert(ttvp);
							
						}
						
					}
					
				}
				
			    count ++;
			}
			//解锁三方仓储日志表
//			TsiImpStorageVehiclePO tvp=new TsiImpStorageVehiclePO();
//			tvp.setIsLock(Constant.IF_TYPE_YES);
//			TsiImpStorageVehiclePO tvp2=new TsiImpStorageVehiclePO();
//			tvp2.setIsLock(Constant.IF_TYPE_NO);
//			dao.update(tvp, tvp2);
			act.setOutData("count", count);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
							ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 三方仓储信息导入保存
	 */
	public void importRebaeStorageSave(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//确定导入后插入日志表
			dao.insert(getStorageSql());
			
			long count = 0;
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; 
			int pageSize = 10000;
			PageResult<Map<String, Object>> ps = dao.importQueryStorage(logonUser.getUserId().toString(), pageSize, curPage);
			List<Map<String, Object>> temList = ps.getRecords();
			
			for(Map<String,Object> map : temList){
				String vin = map.get("VIN").toString();
				TmVehiclePO tvp=new TmVehiclePO();
				tvp.setVin(vin);
				List<TmVehiclePO> tlist=dao.select(tvp);
				boolean flag=checkVehicleStatus(vin, tlist); //校验车辆状态
				
				if(flag==true){//针对验证通过的验证
					//String inType =map.get("IN_TYPE").toString();
					String id = map.get("ID").toString();//临时表ID
					
					TmpTiStorageVhclPO ttp=new TmpTiStorageVhclPO();
					ttp.setVin(vin);
					List plist=dao.select(ttp);
					if(plist!=null&&plist.size()>0){
						//临时表中存在车架号数据时。表示验证不通过，删除临时表中的记录
						TmpTiStorageVhclPO ttpn=new TmpTiStorageVhclPO();
						ttpn.setVin(vin);
						dao.delete(ttpn);
						//更新日志表的状态为已处理，记录备注重复导入
						TsiImpStorageVehiclePO tivp=new TsiImpStorageVehiclePO();
						tivp.setVin(id);
						TsiImpStorageVehiclePO tivp2=new TsiImpStorageVehiclePO();
						tivp2.setDealFlag(Constant.IF_TYPE_YES);
						tivp2.setRemark("重复导入");
						dao.update(tivp, tivp2);
						//重新插入一条记录到临时表
						//向临时表中插入数据，状态为待处理
						TmpTiStorageVhclPO ttvp=new TmpTiStorageVhclPO();
						ttvp.setId(id);
						ttvp.setVin(vin);
						//ttvp.setInType(inType);
						ttvp.setCreateBy(logonUser.getUserId());
						ttvp.setCreateDate(new Date());
						ttvp.setIsDeal(Constant.IF_TYPE_NO);
						dao.insert(ttvp);
						
					}else{//若临时表中不存在，需验证基地临时表中是否存在
						
						TmpTiYeildlyVhclPO tsp=new TmpTiYeildlyVhclPO();
						tsp.setVin(vin);
						List slist=dao.select(tsp);
						if(slist!=null&&slist.size()>0){//存在则验证通过
							//------获取待处理字段---start
							TmpTiYeildlyVhclPO ttsp=(TmpTiYeildlyVhclPO) slist.get(0);
							long materialId =ttsp.getMaterialId();
							//配置
							long packageId =ttsp.getPackageId();
							//车型
							long modelId = ttsp.getModelId();
							//车系
							long seriesId =ttsp.getSeriesId();
							String colorName=ttsp.getColorName();
							Date productDate=ttsp.getProductDate();
							String engineNo=ttsp.getEngineNo();
							String gearboxNo=ttsp.getGearboxNo();
							String hegezhengCode=ttsp.getHegezhengCode();
							Date offlineDate=ttsp.getOfflineDate();
							//根据物料获得产地和仓库
							List<Map<String, Object>> alist = dao.getYieldlyAndWarehouse(materialId+"");
							Long areaId=null;//产地ID
							Long warehouseId=null;//仓库ID
							
							if(alist!=null&&alist.size()>0){
								areaId = Long.parseLong(alist.get(0).get("AREA_ID").toString());
								warehouseId = Long.parseLong(alist.get(0).get("WAREHOUSE_ID").toString());
							}
							
							//------获取待处理字段---end
							if(tlist!=null&&tlist.size()>0){//更新
								TmVehiclePO tvpo=new TmVehiclePO();
								tvpo.setVin(vin);
								TmVehiclePO tvpw=new TmVehiclePO();
								
								tvpw.setYieldly(areaId);//产地
								tvpw.setWarehouseId(warehouseId);//仓库
								tvpw.setSeriesId(seriesId);
								tvpw.setModelId(modelId);
								tvpw.setPackageId(packageId);
								tvpw.setMaterialId(materialId);
								tvpw.setColor(colorName);
								tvpw.setProductDate(productDate);
								tvpw.setEngineNo(engineNo);
								tvpw.setGearboxNo(gearboxNo);
								tvpw.setHegezhengCode(hegezhengCode);
								tvpw.setOfflineDate(offlineDate);
								tvpw.setLifeCycle(Constant.VEHICLE_LIFE_02);//车厂库存
								tvpw.setOrgStorageDate(new Date());//更新车厂入库时间
								tvpw.setUpdateBy(logonUser.getUserId());
								tvpw.setUpdateDate(new Date());
								dao.update(tvpo, tvpw);
								
							}else{//新增
								//根据职位ID获取区域信息
//								TmPoseBusinessAreaPO tap=new TmPoseBusinessAreaPO();
//								tap.setPoseId(logonUser.getPoseId());
//								List alist=dao.select(tap);
//								Long areaId=null;
//								if(alist!=null&&alist.size()>0){
//									areaId=((TmPoseBusinessAreaPO)alist.get(0)).getAreaId();
//								}
								TmVehiclePO tvpw=new TmVehiclePO();
								tvpw.setVehicleId(Long.parseLong(SequenceManager.getSequence("")));
								tvpw.setVin(vin);
								//生命周期
								tvpw.setLifeCycle(Constant.VEHICLE_LIFE_02);
								//车辆状态
								tvpw.setLockStatus(Constant.LOCK_STATUS_01);
								
								tvpw.setYieldly(areaId);//产地
								tvpw.setWarehouseId(warehouseId);//仓库
								tvpw.setSeriesId(seriesId);
								tvpw.setModelId(modelId);
								tvpw.setPackageId(packageId);
								tvpw.setMaterialId(materialId);
								tvpw.setColor(colorName);
								tvpw.setProductDate(productDate);
								tvpw.setEngineNo(engineNo);
								tvpw.setGearboxNo(gearboxNo);
								tvpw.setHegezhengCode(hegezhengCode);
								tvpw.setOfflineDate(offlineDate);
								tvpw.setOrgStorageDate(new Date());//更新车厂入库时间
								dao.insert(tvpw);
							}
							//删除两张临时表中的数据，更新日志记录为已处理
							TmpTiYeildlyVhclPO ttpn=new TmpTiYeildlyVhclPO();
							ttpn.setVin(vin);
							dao.delete(ttpn);
							TmpTiStorageVhclPO ttpv=new TmpTiStorageVhclPO();
							ttpv.setVin(vin);
							dao.delete(ttpv);
							//更新日志表 的处理标志
							TsiImpStorageVehiclePO tivp=new TsiImpStorageVehiclePO();
							tivp.setVin(id);
							TsiImpStorageVehiclePO tivp2=new TsiImpStorageVehiclePO();
							tivp2.setDealFlag(Constant.IF_TYPE_YES);
							tivp2.setRemark("导入成功");
							dao.update(tivp, tivp2);
						}else{
							//向临时表中插入数据，状态为待处理
							TmpTiStorageVhclPO ttvp=new TmpTiStorageVhclPO();
							ttvp.setId(id);
							ttvp.setVin(vin);
							ttvp.setInType(map.get("IN_TYPE").toString());
							ttvp.setCreateBy(logonUser.getUserId());
							ttvp.setCreateDate(new Date());
							ttvp.setIsDeal(Constant.IF_TYPE_NO);//待处理
							dao.insert(ttvp);
							
						}
						
					}
					
				}
				
			    count ++;
			}
			//解锁基地日志表
//			TsiImpYeidlyVehiclePO tvp=new TsiImpYeidlyVehiclePO();
//			tvp.setIsLock(Constant.IF_TYPE_YES);
//			TsiImpYeidlyVehiclePO tvp2=new TsiImpYeidlyVehiclePO();
//			tvp2.setIsLock(Constant.IF_TYPE_NO);
//			dao.update(tvp, tvp2);
			act.setOutData("count", count);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
							ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 出库导入保存
	 */
	public void importRebaeOutSave(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//TODO
			long count = 0;
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 10000;
			PageResult<Map<String, Object>> ps = dao.importQuery(logonUser.getUserId().toString(), pageSize, curPage);
			List<Map<String, Object>> temList = ps.getRecords();
			
			
			for(Map<String,Object> map : temList){
				String vin = map.get("VIN").toString();
				
				TmVehiclePO pOld = new TmVehiclePO();
				pOld.setVin(vin);
				List list=dao.select(pOld);
				TmVehiclePO tvm=(TmVehiclePO) list.get(0);
				TmVehiclePO po = new TmVehiclePO();
				po.setVin(vin);
				
				//出库时间
				Date factoryDate = new Date();
				po.setFactoryDate(factoryDate);
				//修改日期
				po.setUpdateDate(new Date());
				po.setUpdateBy(logonUser.getUserId());
				po.setRemark("出库导入");
				//生命周期
				po.setLifeCycle(Constant.VEHICLE_LIFE_08);//车厂出库
			    dao.update(pOld, po);
			    
				saveErpDataOut(vin, tvm,"" , factoryDate,tvm.getErpOrderId(),tvm.getSpecialOrderNo());
			    count ++;
			}
			
			act.setOutData("count", count);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
							ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 打开打印页面
	 * @author liufazhong
	 */
	public void openPrintView(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String vinP = request.getParamValue("vins");
			if (!XHBUtil.IsNull(vinP)) {
				String [] vins = vinP.split(",");
				List<Map<String, Object>> maps = dao.queryAreaRoadSitInfo(vins);
				List<List<Map<String, Object>>> mapN3 = new ArrayList<List<Map<String,Object>>>();
				if (maps != null && maps.size() > 0) {
					for (int i = 0; i < maps.size(); i++) {
						int nowI = i;
						List<Map<String, Object>> mapNs = new ArrayList<Map<String,Object>>();
						mapNs.add(maps.get(i));
						if (maps.size()-1 >= i+1) {
							mapNs.add(maps.get(i+1));
							nowI = i+1;
						}
						if (maps.size()-1 >= i+2) {
							mapNs.add(maps.get(i+2));
							nowI = i+2;
						}
						mapN3.add(mapNs);
						i = nowI;
					}
				}
				act.setOutData("maps",mapN3);
			}
			act.setForword(PRINT_RESERVOIR_POSITION_INFO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
							ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	private void setStorageDetail(TmVehiclePO po,long count,long materailId) throws Exception {

		//通过VIN好查询到，同物料，同车系的非空道可以入库，库位号
		String vin = po.getVin();
		Long vinLongId = po.getVehicleId();
		String sitName = "";
		List<Object> scode = new ArrayList<Object>();
		String areaName = "";
		String roadName = "";
		Long sitId = null;
		boolean isSpecialFlag = false;
		String sepNo = "";
		//如果特殊订单号不为空，则特殊定单的停在同一个区道为
		if(null != po.getSpecialOrderNo() && !"".equals(po.getSpecialOrderNo())){
			isSpecialFlag = true;
			sepNo = po.getSpecialOrderNo();
		}else{
			isSpecialFlag = false;
		}
		
		List<Map<String,Object>> sitList = dao.getEableSit(vin,isSpecialFlag,sepNo,"import",String.valueOf(materailId));
	
		//如果存在，同物料，同车系的非空道可以入库，库位号，则按照区，道，位从小到大的顺序一次入库
		if(null != sitList && sitList.size() > 0){
			Map<String,Object> postionMap = sitList.get(0);
		    areaName =  (String)postionMap.get("AREA_NAME");
			
		    roadName = (String)postionMap.get("ROAD_NAME");

			BigDecimal bigSitId = (BigDecimal)postionMap.get("SIT_ID");
			 sitId = bigSitId.longValue();
			sitName =  (String)postionMap.get("SIT_NAME");
		}else{//如果不存在，同物料，同车系的非空道可以入库，库位号，说明需要开辟新的库道
				List<Map<String,Object>> roadList = dao.getNullRoadList();
				if(null != roadList && roadList.size() > 0){
					Map<String,Object> postionMap = roadList.get(0);
				    areaName =  (String)postionMap.get("AREA_NAME");
					
				    roadName = (String)postionMap.get("ROAD_NAME");

					BigDecimal bigSitId = (BigDecimal)postionMap.get("SIT_ID");
					 sitId = bigSitId.longValue();
					sitName =  (String)postionMap.get("SIT_NAME");
				}else{
					act.setOutData("status", 3);
					throw new BizException("");
				}
			
		}
		if (XHBUtil.IsNull(sitId)) {
			throw new BizException("");
		}
		scode.add(areaName);
		scode.add(roadName);
		scode.add(sitName);

		
		
		String sitCode = CommonUtils.getSitCode(scode);
		po.setSitCode(sitCode);
		po.setSitId(sitId);
		Long vehId =Long.parseLong(SequenceManager.getSequence(null));
		po.setVehicleId(vehId);
		
		TtSalesSitPO ttSalesSitPO = new TtSalesSitPO();
		ttSalesSitPO.setSitId(sitId);
		
		TtSalesSitPO newSitPo = new TtSalesSitPO();
		newSitPo.setVehicleId(vehId);
		
		dao.update(ttSalesSitPO,newSitPo);
		
	}
	
	@SuppressWarnings("unchecked")
	public void saveErpData(String vin,TmVehiclePO tm,String inOutDesc,
			Date orgStoreageDate,String erpOrderNo, String specialOrderNo){
		Map<String,Object> map = dao.queryVinMater(vin);
		String materialCode = "";
		Integer isSpecialCar=null;//是否特价车
		if(!XHBUtil.IsNull(specialOrderNo)){//如果特殊订单号不为空则物料为超级物料
			Map<String,Object> superMaterialMap = dao.getSuperMaterial(vin,specialOrderNo);
			materialCode = CommonUtils.checkNull(superMaterialMap.get("MATNR"));
			isSpecialCar=Constant.IF_TYPE_YES;
		}else{
			if(!map.isEmpty()){
				materialCode = CommonUtils.checkNull(map.get("MATERIAL_CODE"));
			}
			isSpecialCar=Constant.IF_TYPE_NO;
		}
		
//		TsiExpBusVehStorePO conditions = new TsiExpBusVehStorePO();
//		conditions.setMaterial(materialCode);
//		conditions.setOrderid(erpOrderNo);
//		conditions.setIsRead(0);
		
		//List<TsiExpBusVehStorePO> listPo = dao.select(conditions);
		List<Map<String, Object>> listPo = dao.getTiExpBusVehStore(materialCode, erpOrderNo, "0");
		
		
		//如果存在主数据，则只需要修改主数据的对应物料的数量
		if(null != listPo && listPo.size()> 0){
//			TsiExpBusVehStorePO   storePo = (TsiExpBusVehStorePO) listPo.get(0);
//			Long revId = storePo.getRevId();
			Map<String, Object>   storePo = listPo.get(0);
			Long revId = Long.parseLong(storePo.get("REV_ID").toString());
			
			TsiExpBusVehStorePO ts = new TsiExpBusVehStorePO();
			ts.setRevId(revId);
			List tslist=dao.select(ts);
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
			po.setVehicleId(tm.getVehicleId());
			po.setVin(vin);
			po.setInOutFlag(Constant.VEHICLE_LIFE_08.toString());//出入库标记：出库
			po.setInOutDesc(inOutDesc);
			po.setInOutDate(new Date());
			po.setIsSpecialCar(isSpecialCar);
			dao.insert(po);
			//dao.updateEntryQnt(conditions);
		}else{//如果主数据不存在自
			TsiExpBusVehStorePO  po = new TsiExpBusVehStorePO();
			Long revId = Long.parseLong(SequenceManager.getSequence(null));
			
			po.setRevId(revId);
//			po.setPlant("1000");//1000.整车
//			po.setStgeLoc("3000");//库位,整车
//			po.setMoveType("101");//移动类型.收货
//			po.setEntryQnt(1L);
//			po.setEntryUom("");
			po.setOrderid(erpOrderNo);
			po.setPstngDate(orgStoreageDate);
			//物料
			po.setMaterial(materialCode);
//			po.setHeaderTxt(ErpInterfaceCommon.getServiceDate());
			po.setIsRead(0);
			po.setCreateDate(new Date());
			dao.insert(po);
			
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
			detpo.setVehicleId(tm.getVehicleId());
			detpo.setVin(vin);
			detpo.setInOutFlag(Constant.VEHICLE_LIFE_08.toString());//出入库标记：出库
			detpo.setInOutDesc(inOutDesc);
			detpo.setInOutDate(new Date());
			detpo.setIsSpecialCar(isSpecialCar);
			dao.insert(detpo);
		}
	}
	@SuppressWarnings("unchecked")
	public void saveErpDataOut(String vin,TmVehiclePO tm,String inOutDesc,
			Date orgStoreageDate,String erpOrderNo, String specialOrderNo){
		Map<String,Object> map = dao.queryVinMater(vin);
		String materialCode = "";
		Integer isSpecialCar=null;//是否特价车
		if(!XHBUtil.IsNull(specialOrderNo)){//如果特殊订单号不为空则物料为超级物料
			Map<String,Object> superMaterialMap = dao.getSuperMaterial(vin,specialOrderNo);
			materialCode = CommonUtils.checkNull(superMaterialMap.get("MATNR"));
			isSpecialCar=Constant.IF_TYPE_YES;
		}else{
			if(!map.isEmpty()){
				materialCode = CommonUtils.checkNull(map.get("MATERIAL_CODE"));
			}
			isSpecialCar=Constant.IF_TYPE_NO;
		}
		
		List<Map<String, Object>> listPo = dao.getTiExpBusVehStore(materialCode, erpOrderNo, "0");
		
		
		//如果存在主数据，则只需要修改主数据的对应物料的数量
		if(null != listPo && listPo.size()> 0){
			Map<String, Object>   storePo = listPo.get(0);
			Long revId = Long.parseLong(storePo.get("REV_ID").toString());
			
			TsiExpBusVehStorePO ts = new TsiExpBusVehStorePO();
			ts.setRevId(revId);
			List tslist=dao.select(ts);
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
			po.setVehicleId(tm.getVehicleId());
			po.setVin(vin);
			po.setInOutFlag(Constant.VEHICLE_LIFE_08.toString());//出入库标记：出库
			po.setInOutDesc(inOutDesc);
			po.setInOutDate(new Date());
			po.setIsSpecialCar(isSpecialCar);
			dao.insert(po);
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
			dao.insert(po);
			
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
			detpo.setVehicleId(tm.getVehicleId());
			detpo.setVin(vin);
			detpo.setInOutFlag(Constant.VEHICLE_LIFE_08.toString());//出入库标记：出库
			detpo.setInOutDesc(inOutDesc);
			detpo.setInOutDate(new Date());
			detpo.setIsSpecialCar(isSpecialCar);
			dao.insert(detpo);
		}
		
	}
	
}
