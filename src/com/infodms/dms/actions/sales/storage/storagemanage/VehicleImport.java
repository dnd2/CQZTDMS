package com.infodms.dms.actions.sales.storage.storagemanage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.storage.storagemanage.VehicleImportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmPoseBusinessAreaPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TmpTmVehiclePO;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

import jxl.Cell;
/**
 * 车辆导入
 * @author syh
 * 2017-7-10
 */
public class VehicleImport  extends BaseImport{
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final VehicleImportDao reDao = VehicleImportDao.getInstance();
	private final String importUrl = "/jsp/sales/storage/storagemanage/vehicleImport/importVehicleInfo.jsp";
	/** 导入成功跳转页面 */
	private static final String VEHICLE_IMPORT_SUCCESS_URL = "/jsp/sales/storage/storagemanage/vehicleImport/importVehicleSucess.jsp";
	/*** 导入失败跳转页面 */
	private static final String VEHICLE_IMPORT_FAIL_URL = "/jsp/sales/storage/storagemanage/vehicleImport/importVehicleFailure.jsp";
	
	private static Map<String, String> provicetMap = new HashMap<String, String>();
	private static Map<String, String> cityMap = new HashMap<String, String>();
	private static Map<String, String> countyMap = new HashMap<String, String>();
	private static Map<String, String> startPalceMap = new HashMap<String, String>();
	private static Map<String, String> transWays = new HashMap<String, String>();
	private static Map<String, String> vehsysMap = new HashMap<String, String>();
	/**
	 * 导入初始化
	 */
	public void importInit(){

		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(importUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"车辆导入初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
	/**下载车辆导入模板**/
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
			String[] titles = {"VIN","物料代码","颜色","发动机号","变速箱号","合格证号","生产日期","下线日期"};
			for(String title : titles){
				listHead.add(title);
			}
			list.add(listHead);
			
			// 导出的文件名
			String fileName = "车辆信息导入模板.xls";
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
	 * 车辆信息导入预览
	 */
	public void vehicleImportConfirm() {
		initSet();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			// 清空临时表中目标的数据
			reDao.update(" truncate table tmp_tm_vehicle", null);

			long maxSize = 1024 * 1024 * 5;
			insertIntoTmp(request, "uploadFile", 8, 3, maxSize);
			List<ExcelErrors> el = getErrList();

			if (null != el && el.size() > 0) {
				act.setOutData("errorList", el);
				act.setForword(VEHICLE_IMPORT_FAIL_URL);
			} else {
				List<Map> list = getMapList();
				// 将数据插入临时表
				insertTmpTmVehicle(list, logonUser);
				// 校验临时表数据
				List<ExcelErrors> errorList = null;
				errorList = checkData(logonUser.getUserId().toString(),logonUser.getCompanyId().toString(),
						logonUser.getPoseId().toString());

				if (null != errorList) {
					act.setOutData("errorList", errorList);
					act.setForword(VEHICLE_IMPORT_FAIL_URL);
				} else {
					act.setForword(VEHICLE_IMPORT_SUCCESS_URL);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE, "文件读取错误");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	private void initSet() {
		provicetMap.clear();
		cityMap.clear();
		countyMap.clear();
		startPalceMap.clear();
		vehsysMap.clear();
		transWays.clear();
	}
	/**
	 * 遍历excel，将数据插入车辆信息表
	 * @param list
	 * @param logonUser
	 */
	private void insertTmpTmVehicle(List<Map> list, AclUserBean logonUser) {
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
		TmpTmVehiclePO po = new TmpTmVehiclePO();
		po.setRowNumber(rowNum.trim());
		try {
			po.setVin(cells.length >= 1 ? subCell(cells[0].getContents().trim()) : "");
			po.setMaterialCode(cells.length >= 2 ? subCell(cells[1].getContents().trim()) : "");
			po.setColor(cells.length >= 3 ? subCell(cells[2].getContents().trim()) : "");
			po.setEngineNo(cells.length >= 4 ? subCell(cells[3].getContents().trim()) : "");
			po.setGearboxNo(cells.length >= 5 ? subCell(cells[4].getContents().trim()) : "");
			po.setHegezhengCode(cells.length >= 6 ? subCell(cells[5].getContents().trim()) : "");
			po.setProductDate(cells.length >= 7 ? subCell(cells[6].getContents().trim()) : "");
			po.setOfflineDate(cells.length >= 8 ? subCell(cells[7].getContents().trim()) : "");
			po.setCreateDate(new Date());
			po.setCreateBy(logonUser.getUserId());
			reDao.insert(po);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆信息导入");
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
	 * 检查车辆导入信息是否符合规范
	 * @param userId
	 * @param companyId
	 * @param poseId
	 * @return
	 */
	private List<ExcelErrors> checkData(String userId,String companyId,String poseId) {
		TmpTmVehiclePO selectPo = new TmpTmVehiclePO();
		selectPo.setCreateBy(Long.valueOf(userId));
		List pos = reDao.select(selectPo);
		ExcelErrors errors = null;

		StringBuffer errorInfo = new StringBuffer("");
		boolean isError = false;
		TmpTmVehiclePO po;
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
			for (int i = 0; i < pos.size(); i++) {
				errors = new ExcelErrors();
				po = (TmpTmVehiclePO) pos.get(i);
				// 取得行号
				String rowNum = po.getRowNumber();
	
				try {
					if (null == po.getVin() || "".equals(po.getVin())) {
						isError = true;
						errorInfo.append("VIN码不能为空！");
					}else if (checkHasChinese(po.getVin())) {
							isError = true;
							errorInfo.append("VIN码不能含有中文字符！");
					}
					
					if (null == po.getMaterialCode() || "".equals(po.getMaterialCode())) {
						isError = true;
						errorInfo.append("物料编码不能为空！");
					}else if (checkHasChinese(po.getMaterialCode())) {
						isError = true;
						errorInfo.append("物料编码不能含有中文字符！");
					}else{
						//根据物料编码查询物料信息
						TmVhclMaterialPO tvmp=new TmVhclMaterialPO();
						tvmp.setMaterialCode(po.getMaterialCode());
						List mlist=reDao.select(tvmp);
						if(mlist==null||mlist.size()==0){
							isError = true;
							errorInfo.append("不存在物料编码为"+po.getMaterialCode()+"的物料信息！");
						}
					}
					if (null == po.getColor() || "".equals(po.getColor())) {
						isError = true;
						errorInfo.append("颜色不能为空！");
					}
//					if (null == po.getEngineNo() || "".equals(po.getEngineNo())) {
//						isError = true;
//						errorInfo.append("发动机号不能为空！");
//					}else if (checkHasChinese(po.getEngineNo())) {
//						isError = true;
//						errorInfo.append("发动机号不能含有中文字符！");
//					}
//					if (null == po.getGearBoxNo()|| "".equals(po.getGearBoxNo())) {
//						isError = true;
//						errorInfo.append("变速箱号不能为空！");
//					}else if (checkHasChinese(po.getGearBoxNo())) {
//						isError = true;
//						errorInfo.append("变速箱号不能含有中文字符！");
//					}
//					if (null == po.getHegezhengCode()|| "".equals(po.getHegezhengCode())) {
//						isError = true;
//						errorInfo.append("合格证号不能为空！");
//					}else if (checkHasChinese(po.getHegezhengCode())) {
//						isError = true;
//						errorInfo.append("合格证号不能含有中文字符！");
//					}
					//验证生产日期和下线日期格式
					String productDate = po.getProductDate();
					String offlineDate = po.getOfflineDate();
					if (XHBUtil.IsNull(productDate)) {
						isError = true;
						errorInfo.append("生产日期不能为空！");
					} else {
						try {
							DateUtil.str2Date(productDate, "-");
						} catch (Exception e) {
							try {
								DateUtil.str2Date(productDate, "/");
							} catch (Exception e2) {
								isError = true;
								errorInfo.append("生产日期格式不正确！");
							}
						}
					}
					if (XHBUtil.IsNull(offlineDate)) {
						isError = true;
						errorInfo.append("下线日期不能为空！");
					} else {
						try {
							DateUtil.str2Date(offlineDate, "-");
						} catch (Exception e) {
							try {
								DateUtil.str2Date(offlineDate, "/");
							} catch (Exception e2) {
								isError = true;
								errorInfo.append("下线日期格式不正确！");
							}
						}
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

		if (errorList.size() > 0) {
			return errorList;
		} else {
			return null;
		}

	}
	/**
	 * 检查字符串中是否含有中文，含有返回true
	 * @param source
	 * @return
	 */
	public static boolean checkHasChinese(String source) {
		final String SEQUECNE_FORMAT_STR5 = "[\u4e00-\u9fa5]";
		Pattern pattern = Pattern.compile(SEQUECNE_FORMAT_STR5);
		Matcher matcher = pattern.matcher(source);
		return matcher.find();
	}
	/**
	 * 车辆导入信息查询
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
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "城市里程维护导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
	public void importVehicleSave() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//根据职位ID获取区域信息
			TmPoseBusinessAreaPO tap=new TmPoseBusinessAreaPO();
			tap.setPoseId(logonUser.getPoseId());
			List alist=reDao.select(tap);
			Long areaId=null;
			if(alist!=null&&alist.size()>0){
				areaId=((TmPoseBusinessAreaPO)alist.get(0)).getAreaId();
			}
			long count = 0;
			StringBuffer eMsg=new StringBuffer();
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 10000;
			PageResult<Map<String, Object>> ps = reDao.importQuery(logonUser.getUserId().toString(), pageSize, curPage);
			List<Map<String, Object>> temList = ps.getRecords();
			for (Map<String, Object> map : temList) {
				String vin=(String)map.get("VIN");
				String materialCode=(String)map.get("MATERIAL_CODE");
				String color=(String)map.get("COLOR");
				String engineNo=(String)map.get("ENGINE_NO");
				String gearboxNo=(String)map.get("GEARBOX_NO");
				String hegezhengCode=(String)map.get("HEGEZHENG_CODE");
				String productDate=map.get("PRODUCT_DATE")+"";
				String offlineDate=map.get("OFFLINE_DATE")+"";
				//根据物料编码查询物料信息
				TmVhclMaterialPO tvmp=new TmVhclMaterialPO();
				TmVhclMaterialPO tvmp2=new TmVhclMaterialPO();
				Map<String, Object> tmap=new HashMap<String,Object>();
				tvmp.setMaterialCode(materialCode);
				List mlist=reDao.select(tvmp);
				if(mlist==null||mlist.size()==0){
					eMsg.append("不存在物料编码为"+materialCode+"的物料信息！");
					//act.setOutData("eMsg", "不存在物料编码为"+materialCode+"的物料信息！");
					break;
				}else{
					tvmp2=(TmVhclMaterialPO)mlist.get(0);
					//获取物料组关系信息（车系、车型、配置）
					tmap=reDao.getMaterialGroup(tvmp2.getMaterialId().toString());
				}
				
				//根据vin查询车辆信息，存在进行修改，否则进行新增
				TmVehiclePO tm=new TmVehiclePO();
				tm.setVin(vin);
				List tlist=reDao.select(tm);
				if(tlist==null||tlist.size()==0){//新增
					TmVehiclePO tm2=new TmVehiclePO();
					tm2.setVehicleId(Long.valueOf(SequenceManager.getSequence("")));
					tm2.setVin(vin);
					tm2.setMaterialId(Long.valueOf(tvmp2.getMaterialId().toString()));
					tm2.setSeriesId(Long.valueOf(tmap.get("SERIESID").toString()));
					tm2.setModelId(Long.valueOf(tmap.get("MODELID").toString()));
					tm2.setPackageId(Long.valueOf(tmap.get("SETID").toString()));
					tm2.setColor(color);
					tm2.setEngineNo(engineNo);
					tm2.setGearboxNo(gearboxNo);
					tm2.setVn(vin.substring(vin.length()-8));//获取车架号后8位
					tm2.setHegezhengCode(hegezhengCode);
					if (productDate.length() <= 8) {
						productDate = DateUtil.getCurrentDateTime("yyyy").substring(0, 2)+productDate;
					}
					tm2.setProductDate(DateUtil.str2Date(productDate, "-"));
					if (offlineDate.length() <= 8) {
						offlineDate = DateUtil.getCurrentDateTime("yyyy").substring(0, 2)+offlineDate;
					}
					tm2.setOfflineDate(DateUtil.str2Date(offlineDate, "-"));
					tm2.setLifeCycle(Constant.VEHICLE_LIFE_01);//设置车辆状态为线上
					tm2.setOemCompanyId(logonUser.getCompanyId());
					tm2.setImportDate(new Date());
					//tm2.setDealerId(Long.valueOf(logonUser.getDealerId()));
					tm2.setCreateBy(logonUser.getUserId());
					tm2.setCreateDate(new Date());
					tm2.setYieldly(areaId);
					tm2.setOrgStorageDate(new Date());
					tm2.setLockStatus(Constant.LOCK_STATUS_01);
					tm2.setRemark("手动导入车辆信息");
					reDao.insert(tm2);
					
				}else{//修改
					TmVehiclePO tm2=(TmVehiclePO)tlist.get(0);//原来车辆信息
					TmVehiclePO tm4=new TmVehiclePO();
					tm4.setVehicleId(tm2.getVehicleId());
					TmVehiclePO tm3=new TmVehiclePO();
					tm3.setVehicleId(tm2.getVehicleId());
					tm3.setMaterialId(Long.valueOf(tvmp2.getMaterialId().toString()));
					tm3.setSeriesId(Long.valueOf(tmap.get("SERIESID").toString()));
					tm3.setModelId(Long.valueOf(tmap.get("MODELID").toString()));
					tm3.setPackageId(Long.valueOf(tmap.get("SETID").toString()));
					tm3.setColor(color);
					tm3.setEngineNo(engineNo);
					tm3.setGearboxNo(gearboxNo);
					//tm3.setVn(vin.substring(vin.length()-8));//获取车架号后8位
					tm3.setHegezhengCode(hegezhengCode);
					if (productDate.length() <= 8) {
						productDate = DateUtil.getCurrentDateTime("yyyy").substring(0, 2)+productDate;
					}
					tm3.setProductDate(DateUtil.str2Date(productDate, "-"));
					if (offlineDate.length() <= 8) {
						offlineDate = DateUtil.getCurrentDateTime("yyyy").substring(0, 2)+offlineDate;
					}
					tm3.setOfflineDate(DateUtil.str2Date(offlineDate, "-"));
					//tm3.setLifeCycle(Constant.VEHICLE_LIFE_02);//设置车辆状态为车厂库存
					tm3.setUpdateBy(logonUser.getUserId());
					tm3.setUpdateDate(new Date());
					//tm3.setYieldly(areaId);
					tm3.setOrgStorageDate(new Date());
					//tm3.setLockStatus(Constant.LOCK_STATUS_01);
					reDao.update(tm4, tm3);
				}
				count++;
			}
			act.setOutData("eMsg",eMsg.toString());//错误信息
			act.setOutData("count", count);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "城市里程维护导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
//	public void openImportVehicle(){
//
//		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//		try {
//			act.setForword(importUrl);
//		} catch (Exception e) {
//			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
//					"车辆导入初始化");
//			logger.error(logonUser, e1);
//			act.setException(e1);
//		}
//	
//	}
}
