package com.infodms.dms.actions.report.partReport.partSalesReport;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class PartSalesDirectReport implements PTConstants{

	public Logger logger = Logger.getLogger(PartSalesDirectReport.class);
	private PartBoDao dao = PartBoDao.getInstance();
	

	/**
	 * 
	 * @Title :
	 * @Description: 查询初始化
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-16
	 */
	public void partSalesDirectReportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setOutData("now", CommonUtils.getDate());
			act.setOutData("old", CommonUtils.getMonthFirstDay());
			act.setForword(PART_SALES_REPORTDIRECT_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "直发销售报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
    }
	
	
	public void queryPartSalesDirect(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
		    PageResult<Map<String, Object>> ps = null;
		    
			String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
			if("1".equals(radioSelect)||"2".equals(radioSelect)){
				ps = dao.queryPartSalesDirectList(
						request, 1, Constant.PAGE_SIZE_MAX);
				
				DecimalFormat df=new DecimalFormat("0.00");
	            double totalzfxs = 0;
	            long totalzfsl = 0;
				double totalzfje = 0;
	            double totalfzfxs = 0;
	            long totalfzfsl = 0;
				double totalfzfje = 0;
	            double totalxs = 0;
	            long totalsl = 0;
				double totalje = 0;
	            double totalwrkxs = 0;
	            long totalwrksl = 0;
				double totalwrkje = 0;
				List<Map<String, Object>> list = null;
				list = ps.getRecords();
				if (list != null && list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> map = list.get(i);
						if (map != null && map.size() != 0) {

							if("1".equals(radioSelect)){
								Double zfxs = ((BigDecimal)map.get("ZFXS")).doubleValue();
								totalzfxs =Arith.add(totalzfxs,zfxs);
							}
							long zfsl = ((BigDecimal)map.get("ZFSL")).longValue();
							Double zfje = ((BigDecimal)map.get("ZFJE")).doubleValue();
	                        if("1".equals(radioSelect)){
	                        	Double fzfxs = ((BigDecimal)map.get("FZFXS")).doubleValue();
	                        	totalfzfxs =Arith.add(totalfzfxs,fzfxs);
							}
							long fzfsl = ((BigDecimal)map.get("FZFSL")).longValue();
	                        Double fzfje = ((BigDecimal)map.get("FZFJE")).doubleValue();
	                        if("1".equals(radioSelect)){
	                        	Double xs = ((BigDecimal)map.get("XS")).doubleValue();
	                        	totalxs =Arith.add(totalxs,xs);
							}
							long sl = ((BigDecimal)map.get("SL")).longValue();
	                        Double je = ((BigDecimal)map.get("JE")).doubleValue();
	                        if("1".equals(radioSelect)){
	                        	Double wrkxs = ((BigDecimal)map.get("WRKXS")).doubleValue();
	                        	totalwrkxs =Arith.add(totalwrkxs,wrkxs);
							}
							long wrksl = ((BigDecimal)map.get("WRKSL")).longValue();
	                        Double wrkje = ((BigDecimal)map.get("WRKJE")).doubleValue();

	                        totalzfsl +=zfsl;
	                        totalzfje = Arith.add(totalzfje, zfje);

	                        totalfzfsl +=fzfsl;
	                        totalfzfje = Arith.add(totalfzfje, fzfje);

	                        totalsl +=sl;
	                        totalje = Arith.add(totalje, je);

	                        totalwrksl +=wrksl;
	                        totalwrkje = Arith.add(totalwrkje, wrkje);
							
						}
					}
	                act.setOutData("totalzfxs", totalzfxs);
	                act.setOutData("totalzfsl", totalzfsl);
					act.setOutData("totalzfje", df.format(totalzfje));
	                act.setOutData("totalfzfxs", totalfzfxs);
	                act.setOutData("totalfzfsl", totalfzfsl);
	                act.setOutData("totalfzfje", df.format(totalfzfje));
	                act.setOutData("totalxs", totalxs);
	                act.setOutData("totalsl", totalsl);
	                act.setOutData("totalje", df.format(totalje));
	                act.setOutData("totalwrkxs", totalwrkxs);
	                act.setOutData("totalwrksl", totalwrksl);
	                act.setOutData("totalwrkje", df.format(totalwrkje));
				}
			}else{
				ps = dao.queryPartSalesDirectList(
						request, curPage, Constant.PAGE_SIZE);
			}

			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "直发销售报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void expPartSalesDExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
			
			String[] head = null;
			if("1".equals(radioSelect)){
				head = new String[14];
				head[0] = "服务商编码";
				head[1] = "服务商";
				head[2] = "直发箱数";
				head[3] = "直发数量";
				head[4] = "直发金额(无税)";
				head[5] = "正常箱数";
				head[6] = "正常数量";
				head[7] = "正常金额(无税)";
				head[8] = "总箱数";
				head[9] = "总数量";
				head[10] = "总金额(无税)";
				head[11] = "未入库箱数";
				head[12] = "未入库数量";
				head[13] = "未入库金额(无税)";
			}else if("2".equals(radioSelect)){
				head = new String[11];
				head[0] = "配件编码";
				head[1] = "配件名称";
				head[2] = "配件件号";
				head[3] = "直发数量";
				head[4] = "直发金额";
				head[5] = "正常数量";
				head[6] = "正常金额";
				head[7] = "总数量";
				head[8] = "总金额";
				head[9] = "未入库数量";
				head[10] = "未入库金额";
			}else if("3".equals(radioSelect)){
				head = new String[10];
				head[0] = "服务商编码";
				head[1] = "服务商";
				head[2] = "配件编码";
				head[3] = "配件名称";
				head[4] = "配件件号";
				head[5] = "销售日期";
				head[6] = "销售类型";
				head[7] = "含税单价";
				head[8] = "销售数量";
				head[9] = "销售金额(含税)";
			}else{
				head = new String[8];
				head[0] = "服务商编码";
				head[1] = "服务商";
				head[2] = "配件编码";
				head[3] = "配件名称";
				head[4] = "配件件号";
				head[5] = "审核通过日期";
				head[6] = "销售数量";
				head[7] = "销售金额";
			}
			List<Map<String, Object>> list = dao.queryPartSalesDirect(request,logonUser);
			List list1 = new ArrayList();
			String name = "";
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						
						String[] detail = null;
						if("1".equals(radioSelect)){
							detail = new String[14];
							detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
							detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
							detail[2] = CommonUtils.checkNull(map.get("ZFXS"));
							detail[3] = CommonUtils.checkNull(map.get("ZFSL"));
							detail[4] = CommonUtils.checkNull(map.get("ZFJE"));
							detail[5] = CommonUtils.checkNull(map.get("FZFXS"));
							detail[6] = CommonUtils.checkNull(map.get("FZFSL"));
							detail[7] = CommonUtils.checkNull(map.get("FZFJE"));
							detail[8] = CommonUtils.checkNull(map.get("XS"));
							detail[9] = CommonUtils.checkNull(map.get("SL"));
							detail[10] = CommonUtils.checkNull(map.get("JE"));
							detail[11] = CommonUtils.checkNull(map.get("WRKXS"));
							detail[12] = CommonUtils.checkNull(map.get("WRKSL"));
							detail[13] = CommonUtils.checkNull(map.get("WRKJE"));
							name = "油液销售报表(按单位汇总).xls";
						}else if("2".equals(radioSelect)){
							detail = new String[11];
							detail[0] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
							detail[1] = CommonUtils.checkNull(map.get("PART_CNAME"));
							detail[2] = CommonUtils.checkNull(map.get("PART_CODE"));
							detail[3] = CommonUtils.checkNull(map.get("ZFSL"));
							detail[4] = CommonUtils.checkNull(map.get("ZFJE"));
							detail[5] = CommonUtils.checkNull(map.get("FZFSL"));
							detail[6] = CommonUtils.checkNull(map.get("FZFJE"));
							detail[7] = CommonUtils.checkNull(map.get("SL"));
							detail[8] = CommonUtils.checkNull(map.get("JE"));
							detail[9] = CommonUtils.checkNull(map.get("WRKSL"));
							detail[10] = CommonUtils.checkNull(map.get("WRKJE"));
							name = "油液销售报表(按品种汇总).xls";
						}else if("3".equals(radioSelect)){
							detail = new String[10];
							detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
							detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
							detail[2] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
							detail[3] = CommonUtils.checkNull(map.get("PART_CNAME"));
							detail[4] = CommonUtils.checkNull(map.get("PART_CODE"));
							detail[5] = CommonUtils.checkNull(map.get("CREATE_DATE"));
							detail[6] = CommonUtils.checkNull(map.get("STYPE"));
							detail[7] = CommonUtils.checkNull(map.get("SALE_PRICE"));
							detail[8] = CommonUtils.checkNull(map.get("OUTSTOCK_QTY"));
							detail[9] = CommonUtils.checkNull(map.get("SALE_AMOUNT"));
							name = "油液销售报表(已销售明细).xls";
						}else{
							detail = new String[8];
							detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
							detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
							detail[2] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
							detail[3] = CommonUtils.checkNull(map.get("PART_CNAME"));
							detail[4] = CommonUtils.checkNull(map.get("PART_CODE"));
							detail[5] = CommonUtils.checkNull(map.get("CREATE_DATE"));
							detail[6] = CommonUtils.checkNull(map.get("OUTSTOCK_QTY"));
							detail[7] = CommonUtils.checkNull(map.get("SALE_AMOUNT"));
							name = "油液销售报表(已销售明细).xls";
						}

						list1.add(detail);
					}
				}
				this.exportEx(ActionContext.getContext().getResponse(),
						request, head, list1, name);
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
			logger.error(logonUser, e1);
			act.setException(e1);
			act.setOutData("now", CommonUtils.getDate());
			act.setOutData("old", CommonUtils.getMonthFirstDay());
			act.setForword(PART_SALES_REPORTDIRECT_QUERY_URL);
		}
	}

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list, String title)
			throws Exception {

		String name = title;
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
