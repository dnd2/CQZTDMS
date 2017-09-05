package com.infodms.dms.actions.report.partReport.partSalesReport;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.util.CheckUtil;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderInDao;
import com.infodms.dms.dao.parts.salesManager.PartBoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class BoRiskReport implements PTConstants {

	public Logger logger = Logger.getLogger(BoRiskReport.class);
	private PartBoDao dao = PartBoDao.getInstance();
	private PurchaseOrderInDao dao1 = PurchaseOrderInDao.getInstance();

	/**
	 * 
	 * @Title :
	 * @Description: 查询初始化
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-16
	 */
	public void boRiskReportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
		try {
			List list = dao1.getPartWareHouseList(logonUser);// 获取配件库房信息
            List<Map<String, Object>> planerList = dao.getUserPoseLise(1, null);
            request.setAttribute("planerList", planerList);
			act.setOutData("wareHouses", list);
            act.setOutData("old",CommonUtils.getMonthFirstDay());
            act.setOutData("now",CommonUtils.getDate());
			act.setForword(PART_BO_RISK_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件BO风险报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title :
	 * @Description: 查询初始化
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-16
	 */
	public void queryInfoByPartIdInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
		try {
			String partId = CommonUtils.checkNull(request.getParamValue("partId"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			act.setOutData("partId", partId);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setForword(PART_BO_RISK_DTL_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件BO风险报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: BO风险查询
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-17
	 */
	public void queryPartBoRisk() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
            Map<String,Object> map = dao.queryRepSum(request);
            act.setOutData("bozxs",map==null?0:((BigDecimal)map.get("BOZXS")).longValue());
            act.setOutData("salzxs",map==null?0:((BigDecimal)map.get("SALZXS")).longValue());
            act.setOutData("bolv",map==null?0:(String)map.get("BOLV"));
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartBoRiskList(
					request, curPage, Constant.PAGE_SIZE);
			// 分页方法 end

			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "BO风险报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title :
	 * @Description: BO单明细
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-17
	 */
	public void queryPartBoDetail() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					PageResult<Map<String, Object>> ps = dao.queryBoDtlList(
							request,logonUser,curPage, Constant.PAGE_SIZE);
					// 分页方法 end
					
					act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "BO单明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-17
	 */
	public void expPartBoRiskExcel() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String[] head = new String[20];
			head[0] = "配件编码";
			head[1] = "配件名称";
			head[2] = "配件件号";
            head[3] = "配件类型";
			head[4] = "月平均销量";
            head[5] = "当前可用库存";
			head[6] = "安全库存";
			head[7] = "在途数";
			head[8] = "待入库数";
			head[9] = "订货金额";
			head[10] = "订货数量";
			head[11] = "已交货数量";
			head[12] = "BO数量";
			head[13] = "BO金额";
			head[14] = "BO项数";
			head[15] = "BO满足数";
            head[16] = "默认供应商";
			/*head[17] = "销售总项数";
			head[18] = "BO总项数";
			head[19] = "BO率";*/
			head[17] = "计划员";
			head[18] = "配件是否有效";
			List<Map<String, Object>> list = dao.queryPartBoRisk(request);
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[20];
						detail[0] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
						detail[1] = CommonUtils.checkNull(map.get("PART_CNAME"));
						detail[2] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("PART_TYPE"));
                        detail[4] = CommonUtils.checkNull(map.get("AVG_QTY"));
                        detail[5] = CommonUtils.checkNull(map.get("NORMAL_QTY"));
                        detail[6] = CommonUtils.checkNull(map.get("SAFETY_STOCK"));
						detail[7] = CommonUtils.checkNull(map.get("ZT_QTY"));
						detail[8] = CommonUtils.checkNull(map.get("SPAREIN_QTY"));
						detail[9] = CommonUtils.checkNull(map.get("BUY_AMOUNT")).replace(",","");
						detail[10] = CommonUtils.checkNull(map.get("BUY_QTY"));
						detail[11] = CommonUtils.checkNull(map.get("SALES_QTY"));
						detail[12] = CommonUtils.checkNull(map.get("BO_QTY"));
						detail[13] = CommonUtils.checkNull(map.get("BO_AMOUNT")).replace(",","");
						detail[14] = CommonUtils.checkNull(map.get("BOXS"));
						detail[15] = CommonUtils.checkNull(map.get("TOSAL_QTY"));
                        detail[16] = CommonUtils.checkNull(map.get("VENDER_NAME"));
					/*	detail[17] = CommonUtils.checkNull(map.get("SALZXS"));
						detail[18] = CommonUtils.checkNull(map.get("BOZXS"));
						Long boXs = ((BigDecimal)map.get("BOZXS")).longValue();
						Long dhXs = ((BigDecimal)map.get("SALZXS")).longValue();
						BigDecimal b1 = new BigDecimal(Double.toString(boXs));
						BigDecimal b2 = new BigDecimal(Double.toString(dhXs));
						double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
						DecimalFormat df=new DecimalFormat("#.00");
						detail[19] = subZeroAndDot(df.format(s*100))+"%";*/
						detail[17] = CommonUtils.checkNull(map.get("NAME"));
						int state = ((BigDecimal)map.get("STATE")).intValue();
						if(state==Constant.STATUS_ENABLE.intValue()){
							detail[18] = "有效";
						}else{
							detail[18] = "无效";
						}
						list1.add(detail);
					}
				}
				this.exportEx(ActionContext.getContext().getResponse(),
						request, head, list1, 1);
			} else {
				List wareHouses = dao1.getPartWareHouseList(logonUser);// 获取配件库房信息
				act.setOutData("wareHouses", wareHouses);
				BizException e1 = new BizException(act,
						ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
				throw e1;
			}

		} catch (Exception e) {
			BizException e1 = null;
			if (e instanceof BizException) {
				e1 = (BizException) e;
			} else {
				e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
						"文件下载错误");
			}
			logger.error(logonUser, e1);
			act.setException(e1);
			act.setForword(PART_BO_RISK_QUERY_URL);
		}
	}

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list, int flag)
			throws Exception {

		String name = "BO风险报表.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize = list.size() / 30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					/*ws.addCell(new Label(i, z, str[i]));*/ //modify by yuan
                     if(CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])){
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                    }else{
                        ws.addCell(new Label(i, z, str[i]));
                    }

                }
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	public static String subZeroAndDot(String s){  
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }
}
