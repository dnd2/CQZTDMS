package com.infodms.dms.actions.report.partReport.partSalesReport;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.salesManager.PartBoDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class PartSalesDtlReport implements PTConstants{

	public Logger logger = Logger.getLogger(PartSalesDtlReport.class);
    private PartBoDao dao = PartBoDao.getInstance();
	
	/**
	 * 
	 * @Title :
	 * @Description: 查询初始化
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-16
	 */
	public void partSalesDtlLocReportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setOutData("old", CommonUtils.getMonthFirstDay());
			act.setOutData("now", CommonUtils.getDate());
			act.setForword(PART_SALES_DTL_REPORTLOC_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售明细报表");
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
	public void partSalesDtlGyzxReportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			int flag = 0;//默认是车厂,0表示车厂,1表示供应中心
			if(logonUser.getDealerId()!=null){
				flag = 1;
				TmDealerPO po = new TmDealerPO();
				po.setDealerId(Long.parseLong(logonUser.getDealerId()));
				po = (TmDealerPO) dao.select(po).get(0);
				act.setOutData("venderId", po.getDealerId());
				act.setOutData("venderCode", po.getDealerCode());
				act.setOutData("venderName", po.getDealerName());
				
			}
			act.setOutData("flag", flag);
			act.setOutData("old",CommonUtils.getMonthFirstDay());
            act.setOutData("now",CommonUtils.getDate());
			act.setForword(PART_SALES_DTL_REPORTGYZX_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售明细报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	 * @Title :
	 * @Description: 配件销售明细查询(本部)
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-17
	 */
	public void queryPartSalesDtl() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartSalesDtlList(
					request, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售明细报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	 * @Title :
	 * @Description: 配件销售明细查询(供应中心)
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-17
	 */
	public void queryPartSalesDtl4Gyzx() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					PageResult<Map<String, Object>> ps = dao.queryPartSalesDtl4GyzxList(
							request, curPage, Constant.PAGE_SIZE);
					// 分页方法 end
					act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售明细报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询供应中心
	 */
	public void queryChildOrg(){
		
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryChildOrgList(
					request, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "供应中心");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询服务商
	 */
	public void queryChildOrgByGyzx(){
		
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					PageResult<Map<String, Object>> ps = dao.queryChildOrgByGyzxList(
							request, curPage, Constant.PAGE_SIZE);
					// 分页方法 end
					act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订货单位");
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
	public void expPartSalesDtlExcel() {
		ActionContext act = ActionContext.getContext();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(
    			Constant.LOGON_USER);
    	try {
    		RequestWrapper request = act.getRequest();
    		String[] head = new String[17];
    		head[0] = "销售单号";
    		head[1] = "销售日期";
    		head[2] = "服务商编码";
    		head[3] = "服务商名称";
    		head[4] = "省份";
    		head[5] = "订货单号";
    		head[6] = "订货日期";
    		head[7] = "配件编码";
    		head[8] = "配件名称";
    		head[9] = "配件件号";
    		head[10] = "配件类型";
    		head[11] = "单位";
    		head[12] = "订货数量";
    		head[13] = "销售数量";
    		head[14] = "销售单价";
    		head[15] = "销售金额";
    		List<Map<String, Object>> list = dao.queryPartSalesDtl(request, logonUser);
    		List list1 = new ArrayList();
    		if (list != null && list.size() != 0) {
    			for (int i = 0; i < list.size(); i++) {
    				Map map = (Map) list.get(i);
    				if (map != null && map.size() != 0) {
    					String[] detail = new String[17];
    					detail[0] = CommonUtils
    					.checkNull(map.get("SO_CODE"));
    					detail[1] = CommonUtils
    					.checkNull(map.get("OUT_DATE"));
    					detail[2] = CommonUtils
    					.checkNull(map.get("DEALER_CODE"));
    					detail[3] = CommonUtils
    					.checkNull(map.get("DEALER_NAME"));
    					detail[4] = CommonUtils
    					.checkNull(map.get("REGION_NAME"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("ORDER_CODE"));
    					detail[6] = CommonUtils.checkNull(map
    							.get("ORDER_DATE"));
    					detail[7] = CommonUtils.checkNull(map
    							.get("PART_OLDCODE"));
    					detail[8] = CommonUtils.checkNull(map
    							.get("PART_CNAME"));
    					detail[9] = CommonUtils.checkNull(map
    							.get("PART_CODE"));
    					detail[10] = CommonUtils.checkNull(map
    							.get("PART_TYPE"));
    					detail[11] = CommonUtils.checkNull(map
    							.get("UNIT"));
    					detail[12] = CommonUtils.checkNull(map
    							.get("BUY_QTY"));
    					detail[13] = CommonUtils.checkNull(map
    							.get("OUTSTOCK_QTY"));
    					detail[14] = CommonUtils.checkNull(map
    							.get("SALE_PRICE"));
    					detail[15] = CommonUtils.checkNull(map
    							.get("SALE_AMOUNT")).replace(",","");
    					
    					list1.add(detail);
    				}
    			}
    			this.exportEx(ActionContext.getContext().getResponse(),
    					request, head, list1, 0);
    		} else {
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
			act.setOutData("old",CommonUtils.getMonthFirstDay());
            act.setOutData("now",CommonUtils.getDate());
			logger.error(logonUser, e1);
			act.setException(e1);
			act.setForword(PART_SALES_DTL_REPORTLOC_QUERY_URL);
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
	public void expPartSalesDtl4GyzxExcel() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String[] head = new String[19];
			head[0] = "销售单号";
			head[1] = "销售日期";
			head[2] = "服务商编码";
			head[3] = "服务商名称";
			head[4] = "省份";
			head[5] = "订货单号";
			head[6] = "订货日期";
			head[7] = "供应中心编码";
			head[8] = "供应中心名称";
			head[9] = "配件编码";
			head[10] = "配件名称";
			head[11] = "配件件号";
			head[12] = "配件类型";
			head[13] = "单位";
			head[14] = "订货数量";
			head[15] = "销售数量";
			head[16] = "销售单价";
			head[17] = "销售金额";
			List<Map<String, Object>> list = dao.queryPartSalesDtl4Gyzx(request, logonUser);
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[19];
						detail[0] = CommonUtils
								.checkNull(map.get("SO_CODE"));
						detail[1] = CommonUtils
								.checkNull(map.get("OUT_DATE"));
						detail[2] = CommonUtils
								.checkNull(map.get("DEALER_CODE"));
						detail[3] = CommonUtils
								.checkNull(map.get("DEALER_NAME"));
						detail[4] = CommonUtils
								.checkNull(map.get("REGION_NAME"));
						detail[5] = CommonUtils.checkNull(map
								.get("ORDER_CODE"));
						detail[6] = CommonUtils.checkNull(map
								.get("ORDER_DATE"));
						detail[7] = CommonUtils.checkNull(map
								.get("SELLER_CODE"));
						detail[8] = CommonUtils.checkNull(map
								.get("SELLER_NAME"));
						detail[9] = CommonUtils.checkNull(map
								.get("PART_OLDCODE"));
						detail[10] = CommonUtils.checkNull(map
								.get("PART_CNAME"));
						detail[11] = CommonUtils.checkNull(map
								.get("PART_CODE"));
						detail[12] = CommonUtils.checkNull(map
								.get("PART_TYPE"));
						detail[13] = CommonUtils.checkNull(map
								.get("UNIT"));
						detail[14] = CommonUtils.checkNull(map
								.get("BUY_QTY"));
						detail[15] = CommonUtils.checkNull(map
								.get("OUTSTOCK_QTY"));
						detail[16] = CommonUtils.checkNull(map
								.get("SALE_PRICE"));
						detail[17] = CommonUtils.checkNull(map
								.get("SALE_AMOUNT"));
						
						list1.add(detail);
					}
				}
				this.exportEx(ActionContext.getContext().getResponse(),
						request, head, list1, 1);
			} else {
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
			int flag = 0;//默认是车厂,0表示车厂,1表示供应中心
			if(logonUser.getDealerId()!=null){
				flag = 1;
				TmDealerPO po = new TmDealerPO();
				po.setDealerId(Long.parseLong(logonUser.getDealerId()));
				po = (TmDealerPO) dao.select(po).get(0);
				act.setOutData("venderId", po.getDealerId());
				act.setOutData("venderCode", po.getDealerCode());
				act.setOutData("venderName", po.getDealerName());
				
			}
			act.setOutData("flag", flag);
			act.setOutData("old",CommonUtils.getMonthFirstDay());
            act.setOutData("now",CommonUtils.getDate());
			logger.error(logonUser, e1);
			act.setException(e1);
			act.setForword(PART_SALES_DTL_REPORTGYZX_QUERY_URL);
		}
	}

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list, int flag)
			throws Exception {

		String name;
		if(flag==0){
			name = "销售明细报表(本部).xls";
		}else{
			name = "销售明细报表(供应中心).xls";
		}
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
}
