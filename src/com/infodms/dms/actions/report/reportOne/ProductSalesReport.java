package com.infodms.dms.actions.report.reportOne;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.report.ProductSaleQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 生产销售快报
 * 
 * @author Administrator
 * 
 */
public class ProductSalesReport {
	private Logger logger = Logger.getLogger(ProductSalesReport.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(
			Constant.LOGON_USER);
	private ProductSaleQueryDao dao = ProductSaleQueryDao.getInstance();

	private final String PRODUCT_SALE_REPORT_INIT = "/jsp/report/sales/productSaleQuery.jsp";
	private final String PRODUCT_SALE_REPORT2_INIT = "/jsp/report/sales/productSaleQuery2.jsp";
	private final String PRODUCT_SALE_REPORT2_SET_INIT = "/jsp/report/sales/productSaleQuerySet.jsp";

	/*
	 * 生产销售快报查询初始化
	 */
	public void productSaleReportQuery() {
		try {
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(PRODUCT_SALE_REPORT_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 生产销售快报查询初始化
	 */
	public void productSaleReportQueryNewInit() {
		try {
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(PRODUCT_SALE_REPORT2_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 生产销售快报（新）查询初始化
	 */
	public void productSaleReportQueryNew() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			String chooseDate = CommonUtils.checkNull(request
					.getParamValue("chooseDate"));
			String sysdate = AjaxSelectDao.getInstance()
					.getSimpleCurrentServerTime();
			if (!"".equals(chooseDate) && !chooseDate.equals(sysdate)) {// 如果用户选择的日期不为空且不是当前日期(查询历史记录)
				result = dao.getHistoryProductSalseInfo(chooseDate);
			} else {// 如果为空或者选择的时间为当前日期，则查询实时信息
				result = dao.getProductSaleReportInfoNew(map,
						Constant.PAGE_SIZE_MAX, curPage);
			}
			PageResult<Map<String, Object>> ps = new PageResult<Map<String, Object>>();
			ps.setRecords(result);
			ps.setPageSize(Constant.PAGE_SIZE_MAX);
			ps.setTotalPages(1);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 生产销售快报（新）出现多余的配置
	 */
	public void productSaleReportSet() {
		try {
			act.setForword(PRODUCT_SALE_REPORT2_SET_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "生产销售快报（新）");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 生产销售快报（新）出现多余的配置
	 */
	public void productSaleReportSetQuery() {
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT AP.GROUP_ID,AP.GROUP_CODE,AP.GROUP_NAME FROM \n");
			sbSql.append("(\n");
			sbSql.append("SELECT A.GROUP_ID, A.GROUP_CODE, A.GROUP_NAME\n");
			sbSql.append("  FROM TM_VHCL_MATERIAL_GROUP A,\n");
			sbSql.append("       TM_VHCL_MATERIAL_GROUP M,\n");
			sbSql.append("       TM_VHCL_MATERIAL_GROUP S,\n");
			sbSql.append("       TM_VHCL_MATERIAL_GROUP P\n");
			sbSql.append(" WHERE A.GROUP_LEVEL = 4\n");
			sbSql.append("   AND A.STATUS = 10011001\n");
			sbSql.append("   AND A.PARENT_GROUP_ID = M.GROUP_ID\n");
			sbSql.append("   AND M.PARENT_GROUP_ID = S.GROUP_ID\n");
			sbSql.append("   AND S.PARENT_GROUP_ID = P.GROUP_ID\n");
			sbSql.append("   AND M.STATUS = 10011001\n");
			sbSql.append("   AND S.STATUS = 10011001\n");
			sbSql.append("   AND P.STATUS = 10011001\n"); 
			sbSql.append("	 AND M.FORCAST_FLAG = 1");
			sbSql.append("	 AND S.FORCAST_FLAG = 1");
			sbSql.append("	 AND P.FORCAST_FLAG = 1");
			sbSql.append(") AP\n");
			
			sbSql.append("WHERE AP.GROUP_ID NOT IN (SELECT DISTINCT B.GROUP_ID FROM TM_VEHICLE_MODEL A,TM_VHCL_MATERIAL_GROUP B, TR_VEHICLE_MODEL_PACKAGE C WHERE\n");
			sbSql.append("C.VEHICLE_MODEL_ID = A.VEHICLE_MODEL_ID AND B.GROUP_ID = C.GROUP_ID) ORDER BY AP.GROUP_CODE");

			PageResult<Map<String, Object>> ps = dao.pageQuery(sbSql.toString(), null, dao.getFunName(),
					Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "生产销售快报（新）");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 查询
	 */
	public void getProductSaleReportInfo() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			/*****************************************************************************/
			String chooseDate = CommonUtils.checkNull(request
					.getParamValue("chooseDate"));
			String sysdate = AjaxSelectDao.getInstance()
					.getSimpleCurrentServerTime();
			if (!"".equals(chooseDate) && !chooseDate.equals(sysdate)) {// 如果用户选择的日期不为空且不是当前日期(查询历史记录)
				result = dao.getHistoryProductSalseInfo1(chooseDate);
			} else {// 如果为空或者选择的时间为当前日期，则查询实时信息
				result = dao.getProductSaleReportInfo(map,
						Constant.PAGE_SIZE_MAX, curPage);
			}
			/*****************************************************************************/
			// List<Map<String, Object>> result =
			// dao.getProductSaleReportInfo(map,Constant.PAGE_SIZE_MAX,
			// curPage);
			PageResult<Map<String, Object>> ps = new PageResult<Map<String, Object>>();
			ps.setRecords(result);
			ps.setPageSize(Constant.PAGE_SIZE_MAX);
			ps.setTotalPages(1);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "生产销售快报信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 导出
	 */
	public void toExcel() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			String chooseDate = CommonUtils.checkNull(request
					.getParamValue("chooseDate"));
			String sysdate = AjaxSelectDao.getInstance()
					.getSimpleCurrentServerTime();
			if (!"".equals(chooseDate) && !chooseDate.equals(sysdate)) {
				result = dao.getHistoryProductSalseInfo1(chooseDate);
			} else {
				result = dao.getProductSaleReportInfo(map,
						Constant.PAGE_SIZE_MAX, curPage);
			}
			// List<Map<String, Object>> result =
			// dao.getProductSaleReportInfo(map,Constant.PAGE_SIZE_MAX,
			// curPage);
			String[] excelHead = { "车辆型号", "当日入库", "本月入库", "本年入库", "当日开票",
					"本月开票", "本年开票", "可发车", "当日最终销售", "本月最终销售", "本年最终销售", "中转库",
					"外借", "库存合计" };
			String[] columns = { "GROUP_NAME", "D_AMOUNT", "M_AMOUNT",
					"Y_AMOUNT", "DF_AMOUNT", "MF_AMOUNT", "YF_AMOUNT",
					"SV_AMOUNT", "DS_AMOUNT", "MS_AMOUNT", "YS_AMOUNT",
					"STORAGE_AMOUNT", "B_AMOUNT", "STORAGE_AMOUNT" };
			ToExcel.toReportExcel(act.getResponse(), request, "生产销售快报.xls",
					excelHead, columns, result);
			// act.setOutData("val", "1");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "导出");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 导出
	 */
	public void toExcelNew() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			String chooseDate = CommonUtils.checkNull(request
					.getParamValue("chooseDate"));
			String sysdate = AjaxSelectDao.getInstance()
					.getSimpleCurrentServerTime();
			if (!"".equals(chooseDate) && !chooseDate.equals(sysdate)) {
				result = dao.getHistoryProductSalseInfo(chooseDate);
			} else {
				result = dao.getProductSaleReportInfoNew(map,
						Constant.PAGE_SIZE_MAX, curPage);
			}
			String[] excelHead = { "车辆型号", "当日入库", "本月入库", "本年入库", "当日开票",
					"本月开票", "本年开票", "当日最终销售", "本月最终销售", "本年最终销售", "可发车", "外借",
					"库存合计" };
			String[] columns = { "VEHICLE_MODEL_NAME", "D_COUNT", "M_COUNT",
					"Y_COUNT", "D_CHECK", "M_CHECK", "Y_CHECK", "DS_AMOUNT",
					"MS_AMOUNT", "YS_AMOUNT", "S_AMOUNT", "BORROW",
					"STORAGE_AMOUNT" };
			ToExcel.toReportExcel(act.getResponse(), request, "生产销售快报.xls",
					excelHead, columns, result);
			// act.setOutData("val", "1");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "导出");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
