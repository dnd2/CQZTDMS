package com.infodms.dms.actions.report.partReport.partPlanReport;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.util.CheckUtil;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderChkDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderInDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class PurOrderChkDtlReport implements PTConstants{

	public Logger logger = Logger.getLogger(PurOrderChkDtlReport.class);
	private PurchaseOrderChkDao dao = PurchaseOrderChkDao.getInstance();
	private PurchaseOrderInDao dao1 = PurchaseOrderInDao.getInstance();
	
	/**
	 * 
	 * @Title :
	 * @Description: 查询初始化
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-16
	 */
	public void purOrderChkDtlReportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			List list = dao1.getPartWareHouseList(logonUser);// 获取配件库房信息
			List<Map<String, Object>> list1 = dao.getCheckkerList();//获取所有的验收人员
			act.setOutData("wareHouses", list);
			act.setOutData("planerList", list1);
            act.setOutData("old",CommonUtils.getPreviousXMonthFirst(-3));
            act.setOutData("now",CommonUtils.getDate());
			act.setForword(PART_PURORDERCHKDTL_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "进货明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 导出 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-21
	 */
	public void expPurOrderChkDtlExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String[] head = new String[25];
			head[0] = "验收单号";
			head[1] = "采购订单号";
			head[2] = "供应商名称";
			head[3] = "制造商名称";
			head[4] = "库房名称";
			head[5] = "进货类型";
			head[6] = "配件类型";
			head[7] = "来源";
			head[8] = "配件编码";
			head[9] = "配件名称";
			head[10] = "件号";
			head[11] = "单价";
			head[12] = "金额";
			head[13] = "库位编号";
			head[14] = "当前可用库存";
			head[15] = "进货数量";
			head[16] = "入库数量";
			head[17] = "转入库单时间";
			head[18] = "转验收单时间";
			head[19] = "入库人";
			head[20] = "计划员";
			head[21] = "是否已转入库单";
			head[22] = "是否入库完成";
			head[23] = "是否打印";
			List<Map<String, Object>> list = dao.queryChkOrderDtl(request);
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[25];
						detail[0] = CommonUtils.checkNull(map.get("CHK_CODE"));
						detail[1] = CommonUtils.checkNull(map
								.get("ORDER_CODE"));
						detail[2] = CommonUtils
								.checkNull(map.get("VENDER_NAME"));
						detail[3] = CommonUtils.checkNull(map.get("MAKER_NAME"));
						detail[4] = CommonUtils.checkNull(map.get("WH_NAME"));
						detail[5] = CommonUtils.checkNull(map.get("PLAN_TYPE"));
						detail[6] = CommonUtils.checkNull(map.get("PART_TYPE"));
						detail[7] = CommonUtils.checkNull(map.get("ORIGIN_TYPE"));
						detail[8] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
						detail[9] = CommonUtils.checkNull(map.get("PART_CNAME"));
						detail[10] = CommonUtils.checkNull(map.get("PART_CODE"));
						detail[11] = CommonUtils.checkNull(map.get("BUY_PRICE")).replace(",","");
						detail[12] = CommonUtils.checkNull(map.get("BUY_AMOUNT")).replace(",","");
						detail[13] = CommonUtils.checkNull(map.get("LOC_CODE"));
						detail[14] = CommonUtils.checkNull(map.get("NORMAL_QTY"));
						detail[15] = CommonUtils.checkNull(map.get("CHECK_QTY"));
						detail[16] = CommonUtils.checkNull(map.get("IN_QTY"));
						detail[17] = CommonUtils.checkNull(map.get("IN_DATE"));
						detail[18] = CommonUtils.checkNull(map.get("CREATE_DATE"));
						detail[19] = CommonUtils.checkNull(map.get("NAME"));
						detail[20] = CommonUtils.checkNull(map.get("PLAN_NAME"));
						int is_incode = ((BigDecimal) map.get("IS_INCODE")).intValue();
						int is_allIn = ((BigDecimal) map.get("IS_ALLIN")).intValue();
						int is_print = ((BigDecimal) map.get("IS_PRINT")).intValue();
						if(Constant.IF_TYPE_YES.intValue()==is_incode){
							detail[21] = "是";
						}else{
							detail[21] = "否";
						}
						if(Constant.IF_TYPE_YES.intValue()==is_allIn){
							detail[22] = "是";
						}else{
							detail[23] = "否";
						}
						if(Constant.IF_TYPE_YES.intValue()==is_print){
							detail[23] = "是";
						}else{
							detail[23] = "否";
						}
						list1.add(detail);
					}
				}
				this.exportEx(ActionContext.getContext().getResponse(),
						request, head, list1, 1);
			} else {
				List wareHouses = dao1.getPartWareHouseList(logonUser);// 获取配件库房信息
				List<Map<String, Object>> list2 = dao.getCheckkerList();//获取所有的验收人员
				act.setOutData("wareHouses", wareHouses);
				act.setOutData("planerList", list2);
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
			act.setForword(PART_PURORDERCHKDTL_QUERY_URL);
		}
	}
	
	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list, int flag)
			throws Exception {

		String name = "进货明细.xls";
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
