package com.infodms.dms.actions.claim.oldPart;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.ClaimOldPartOutPreListBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.oldPart.ClaimOldPartStorageManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.po3.bean.PageResult;

/**
 * 类说明：索赔旧件管理--索赔旧件库存查询 作者： 赵伦达
 */
public class ClaimOldPartStorageManager extends BaseAction {
	private ClaimOldPartStorageManagerDao dao = ClaimOldPartStorageManagerDao.getInstance();
	// url导向
	private final String queryOldPartStoreUrl = "/jsp/claim/oldPart/queryOldPartStoreList.jsp";
	private final String queryBarcodeOldPartStoreUrl = "/jsp/claim/oldPart/queryBarcodeOldPartStoreList.jsp";
	private final String queryScanningUrl = "/jsp/claim/oldPart/queryScanning.jsp";
	private final String updateScanningUrl = "/jsp/claim/oldPart/updateScanning.jsp";
	private final String queryStockDetail = "/jsp/claim/oldPart/queryStockDetail.jsp";
	private final String BJMaterialMaintain = "/jsp/claim/oldPart/BJMaterialMaintain.jsp";
	private final String BJMaterialMaintainAdd = "/jsp/claim/oldPart/BJMaterialMaintainAdd.jsp";
	private final String partStoreDetail = "/jsp/claim/oldPart/partStoreDetial.jsp";
	
	/**
	 * Function：索赔旧件库存查询--初始化
	 * 
	 * @param ：
	 * @return:
	 * @throw： LastUpdate： 2010-6-18
	 */
	@SuppressWarnings("unchecked")
	public void queryListPage() {
		try {

			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			List<TmBusinessAreaPO> list = dao.select(new TmBusinessAreaPO());
			System.out.println(loginUser.getPoseBusType());
			act.setOutData("poseType", loginUser.getPoseBusType());
			act.setOutData("yieldly", yieldly);
			act.setOutData("yieldlyList", list);
			act.setForword(queryOldPartStoreUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * Function：索赔旧件库存查询--条件查询
	 * zyw 2014-10-25
	 * @param ：
	 * @return:
	 */
	public void queryCurStoreList() {
		try {
			PageResult<ClaimOldPartOutPreListBean> ps = dao.getCurStoreList(request, getCurrPage(), Constant.PAGE_SIZE_MIDDLE);
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryCurStoreList01() {
		try {
			Map<String, Object>   ps = dao.getCurStoreList01(request, getCurrPage(), Constant.PAGE_SIZE_MIDDLE);
			String totals = ps.get("ALL_OUT_AMOUNT").toString();
			String  totalsMoney=ps.get("PARTPRICE").toString();
			String  totals_kc=ps.get("ALL_AMOUNT_KC").toString();
			act.setOutData("totals", totals);
			act.setOutData("totalsMoney", totalsMoney);
			act.setOutData("totals_kc", totals_kc);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function：索赔旧件库存查询--导出功能
	 * 
	 * @param ：
	 * @return:
	 * @throw： LastUpdate： 2010-7-12
	 */
	@SuppressWarnings("unchecked")
	public void toExcel() {
		OutputStream os = null;
		try {
			response = act.getResponse();
			Map params = new HashMap<String, String>();
			String supply_code = request.getParamValue("supply_code");
			String supply_name = request.getParamValue("supply_name");
			String part_code = request.getParamValue("part_code");
			String part_name = request.getParamValue("part_name");
			String isMainCode = request.getParamValue("IS_MAIN_CODE");
			String yieldly = request.getParamValue("YIELDLY_TYPE");// 产地
			params.put("supply_code", supply_code);
			params.put("supply_name", supply_name);
			params.put("part_code", part_code);
			params.put("part_name", part_name);
			params.put("yieldly", yieldly);
			params.put("isMainCode", isMainCode);
			
			// 导出的文件名
			String fileName = "索赔旧件库存.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			List<List<Object>> list = new LinkedList<List<Object>>();
			// 表头
			List<Object> titleList = new LinkedList<Object>();
			titleList.add("序号");
			titleList.add("供应商代码");
			titleList.add("供应商名称");
			titleList.add("配件代码");
			titleList.add("配件名称");
			titleList.add("库存数");
			titleList.add("索赔金额");
			list.add(titleList);
			List<Map<String, Object>> results = dao.getCurStoreList2(params);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				List<Object> dataList = new LinkedList<Object>();
				dataList.add(i + 1);
				dataList.add(CommonUtils.checkNull(record.get("SUPPLY_CODE")));
				dataList.add(CommonUtils.checkNull(record.get("SUPPLY_NAME")));
				dataList.add(CommonUtils.checkNull(record.get("PART_CODE")));
				dataList.add(CommonUtils.checkNull(record.get("PART_NAME")));
				dataList.add(CommonUtils.checkNull(record.get("ALL_AMOUNT")));
				dataList.add(CommonUtils.checkNull(record.get("PART_PRICE")));
				list.add(dataList);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--导出功能");
			logger.error(loginUser, e1);
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

	// 条码扫描旧件出库查询
	public void queryBarcodeListPage() {
		try {

			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId());
			act.setOutData("yieldly", yieldly);
			act.setForword(queryBarcodeOldPartStoreUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	@SuppressWarnings("unchecked")
	public void queryBarcodeCurStoreList() {
		try {

			Long companyId = GetOemcompanyId.getOemCompanyId(loginUser);

			Map params = new HashMap<String, String>();
			// 处理当前页

			String stock_type = request.getParamValue("stock_type");
			String part_code = request.getParamValue("part_code");
			String part_name = request.getParamValue("part_name");
			String yieldly = request.getParamValue("yieldly");// 产地
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId()); // 该用户拥有的产地权限
			params.put("company_id", companyId);
			params.put("stock_type", stock_type);
			params.put("part_code", part_code);
			params.put("part_name", part_name);// ClaimOldPartRemainStoreListBean
			params.put("yieldly", yieldly);
			params.put("yieldlys", yieldlys);
			PageResult<Map<String, Object>> ps = dao.getBarcodeCurStoreList(
					params, super.getCurrPage(), Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(queryOldPartStoreUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	// 上端抵扣扫描
	public void queryScanning() {
		try {

			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId());
			act.setOutData("yieldly", yieldly);
			act.setForword(queryScanningUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// 上端抵扣扫描
	public void queryScanningList() {
		try {

			// 处理当前页

			String barcodeNo = request.getParamValue("barcodeNo");
			PageResult<Map<String, Object>> ps = dao.queryScanningList(
					barcodeNo, super.getCurrPage(), Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(queryScanningUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void updateScanning() {
		try {

			String barcodeNo = request.getParamValue("barcodeNo1");
			String reasonCode = request.getParamValue("reasonCode");
			act.setOutData("reasonCode", reasonCode);
			act.setOutData("barcodeNo", barcodeNo);
			act.setForword(updateScanningUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void updateScanningSave() {
		try {

			String barcodeNo = request.getParamValue("barcodeNo");
			String CLAIM_TYPE = request.getParamValue("CLAIM_TYPE");
			dao.updateScanningSave(barcodeNo, CLAIM_TYPE);
			act.setForword(queryScanningUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void updateScanningSaveZC() {

		String barcodeNo = request.getParamValue("barcodeNo");
		dao.updateScanningSaveZC(barcodeNo);
		act.setForword(queryScanningUrl);
	}

	public void outOfStore() {
		try {
			ClaimOldPartStorageManagerDao dd = new ClaimOldPartStorageManagerDao();

			String idStr = request.getParamValue("idStr");
			String partCodeStr[] = idStr.split(",");
			String exitScrap = request.getParamValue("exitScrap");
			for (int i = 0; i < partCodeStr.length; i++) {
				String num = request.getParamValue("outStoreNum"
						+ partCodeStr[i]);
				dd.outOfStore(num, exitScrap, partCodeStr[i]);
				dd.outOfStoreDetail(num, exitScrap, partCodeStr[i], loginUser
						.getUserId());
			}
			act.setOutData("updateResult", "updateSuccess");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--条件查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void StockDetail() {
		try {

			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId());
			act.setOutData("yieldly", yieldly);
			act.setForword(queryStockDetail);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	public void StockDetailQuery() {
		try {

			// 处理当前页

			String part_name = request.getParamValue("part_name");
			String part_code = request.getParamValue("part_code");
			String exitScrap = request.getParamValue("exitScrap");
			String create_end_date = request.getParamValue("create_end_date");
			String create_start_date = request
					.getParamValue("create_start_date");
			PageResult<Map<String, Object>> ps = dao.StockDetailQuery(
					part_name, part_code, exitScrap, create_start_date,
					create_end_date, super.getCurrPage(), Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(queryStockDetail);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	// 北京车型组维护
	public void BJMaterialMaintain() {
		try {

			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId());
			act.setOutData("yieldly", yieldly);
			act.setForword(BJMaterialMaintain);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "北京车型组维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void BJMaterialMaintainQuery() {
		try {

			// 处理当前页
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));//
			String groupName = CommonUtils.checkNull(request
					.getParamValue("groupName"));//
			PageResult<Map<String, Object>> ps = dao.BJMaterialMaintainQuery(
					groupCode, groupName, super.getCurrPage(),
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(BJMaterialMaintain);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "北京车型组维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void BJMaterialMaintainAdd() {
		try {

			act.setForword(BJMaterialMaintainAdd);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "北京车型组维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void BJMaterialMaintainAddQuery() {
		try {
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));//
			String groupName = CommonUtils.checkNull(request
					.getParamValue("groupName"));//
			PageResult<Map<String, Object>> ps = dao
					.BJMaterialMaintainAddQuery(groupCode, groupName, super
							.getCurrPage(), 100);
			act.setOutData("ps", ps);
			act.setForword(BJMaterialMaintainAdd);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "北京车型组维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void BJMaterialMaintainAddSave() {
		try {

			String ids = CommonUtils.checkNull(request.getParamValue("ids"));//
			String id[] = ids.split(",");

			for (int i = 0; i < id.length; i++) {
				dao.BJMaterialMaintainAddSave(id[i], loginUser.getUserId());
			}
			act.setForword(BJMaterialMaintain);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "北京车型组维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void BJMaterialMaintainDelete() {
		try {

			String id = CommonUtils.checkNull(request.getParamValue("id"));//
			dao.BJMaterialMaintainDelete(id);
			act.setForword(BJMaterialMaintain);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "北京车型组维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void claimDetail2() {
		try {
			Long companyId = GetOemcompanyId.getOemCompanyId(loginUser);
			String partCode = request.getParamValue("partCode");
			String supCode = request.getParamValue("code");
			String yieldly = request.getParamValue("areaId");
			String main = request.getParamValue("IS_MAIN_CODE");
			// List<ClaimOldPartOutPreListBean> list
			// =dao.getBaseBean(partCode,supCode,yieldly,companyId);
			List<ClaimOldPartOutPreListBean> detail = dao.getCliamDetail(
					partCode, supCode, yieldly, companyId, main);
			System.out.println(detail.size());
			
			act.setOutData("partCode", partCode);
			act.setOutData("supCode", supCode);
			act.setOutData("yieldly", yieldly);
			act.setOutData("main", main);
			act.setOutData("listBean", detail);
			// act.setOutData("baseBean", list.get(0));
			act.setForword(partStoreDetail);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件库存查询--索赔明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void claimDetail3() {
		try {
			Long companyId = GetOemcompanyId.getOemCompanyId(loginUser);
			String partCode = request.getParamValue("partCode");
			String supCode = request.getParamValue("code");
			String yieldly = request.getParamValue("areaId");
			String main = request.getParamValue("IS_MAIN_CODE");
			List<ClaimOldPartOutPreListBean> detail = dao.getCliamDetail(
					partCode, supCode, yieldly, companyId, main);
			System.out.println(detail.size());
			act.setOutData("partCode", partCode);
			act.setOutData("supCode", supCode);
			act.setOutData("yieldly", yieldly);
			act.setOutData("main", main);
			act.setOutData("listBean", detail);
			act.setForword(partStoreDetail);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件库存查询--索赔明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void exportClaimDetail3() {
		try {
			Long companyId = GetOemcompanyId.getOemCompanyId(loginUser);
			String partCode = request.getParamValue("partCode");
			String supCode = request.getParamValue("supCode");
			String yieldly = request.getParamValue("yieldly");
			String main = request.getParamValue("main");
			List<ClaimOldPartOutPreListBean> detail = dao.getCliamDetail(
					partCode, supCode, yieldly, companyId, main);
			dao.exportClaimDetail3(act,detail);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件库存查询--索赔明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 紧急调件库存跳转
	 */
	public void  queryListBorrow(){
		super.sendMsgByUrl(sendUrl(ClaimOldPartStorageManager.class, "queryListBorrow"), "紧急调件库存跳转");
	}
	/**
	 * 紧急调件库存查询
	 */
	public void  queryCurBorrowStoreList(){
		PageResult<ClaimOldPartOutPreListBean> list=dao.queryCurBorrowStoreList(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
	}
	/**
	 * 紧急调件调出页面
	 */
	public void  queryListBorrowCallOut(){
		super.sendMsgByUrl(sendUrl(ClaimOldPartStorageManager.class, "borrowCallOut"), "紧急调件调出页面跳转");
	}
	/**
	 * 紧急调件调出页面
	 */
	public void  borrowCaladd(){
		super.sendMsgByUrl(sendUrl(ClaimOldPartStorageManager.class, "borrowCaladd"), "紧急调件调出页面跳转");
	}
	
	public void queryListBorrowCallOutTempData(){
		PageResult<Map<String, Object>> list=dao.queryListBorrowCallOutTempData(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
	}
	public void queryListBorrowCallOutData(){
		PageResult<Map<String, Object>> list=dao.queryListBorrowCallOut(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
	}
	/**
	 * 调入库存临时表
	 */
	public void callOutById(){
		int res=dao.callOutById(request,loginUser);
		if(res==0){
			act.setOutData("succ", 0);
		}else{
			act.setOutData("succ", -1);
		}
	}
	/**
	 * 调出库存临时表
	 */
	public void callInById(){
		int res=dao.callInById(request,loginUser);
		if(res==0){
			act.setOutData("succ", 0);
		}else{
			act.setOutData("succ", -1);
		}
	}
	/**
	 * 紧急调件调入页面
	 */
	public void  queryListBorrowCallIn(){
		super.sendMsgByUrl(sendUrl(ClaimOldPartStorageManager.class, "borrowCallIn"), "紧急调件调出页面跳转");
	}
	
}
