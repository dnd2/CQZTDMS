package com.infodms.dms.actions.report.partReport.partPlanReport;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderChkDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class PurOrderDtlReport implements PTConstants{

	public Logger logger = Logger.getLogger(PurOrderDtlReport.class);
	private PurchaseOrderChkDao dao = PurchaseOrderChkDao.getInstance();
	

	/**
	 * 
	 * @Title :
	 * @Description: 查询初始化
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-16
	 */
	public void purOrderDtlDeliveryInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(PART_PURORDERDTLDELIVERY_QUERY_URL);
            act.setOutData("old",CommonUtils.getPreviousXMonthFirst(-3));
            act.setOutData("now",CommonUtils.getDate());
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单明细及交货率统计");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

	/**
	 * 
	 * @Title      : 
	 * @Description: 采购订单明细及交货率统计
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-22
	 */
	public void queryPurOrderDtlDelivery(){
		 ActionContext act = ActionContext.getContext();
         RequestWrapper request = act.getRequest();
         AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
         try {
             //分页方法 begin
             Integer curPage = request.getParamValue("curPage") != null ? Integer
                     .parseInt(request.getParamValue("curPage"))
                     : 1; // 处理当前页
             PageResult<Map<String, Object>> ps = dao.queryPurOrderDtlDeliveryList(request,
                     curPage, Constant.PAGE_SIZE);
             //分页方法 end
             act.setOutData("ps", ps);
         } catch (Exception e) {//异常方法
             BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单明细及交货率统计");
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
	 * LastDate    : 2013年11月25日
	 */
	public void expPurOrderDtlDceExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String radioSelect = request.getParamValue("RADIO_SELECT");
			String[] head = null;
			List<Map<String, Object>> list;
			String name = "";
			if("1".equals(radioSelect)){
				name = "采购订单明细及交货率统计(交货情况).xls";
				head = new String[19];
				head[0] = "计划单号";
				head[1] = "制单日期";
				head[2] = "预计到货日期";
				head[3] = "总计划金额";
				head[4] = "总计划数量";
				head[5] = "计划类型";
				head[6] = "采购订单号";
				head[7] = "订货品种数";
				head[8] = "订货总数量";
				head[9] = "订单金额";
				head[10] = "供应商";
				head[11] = "订单状态";
				head[12] = "已交货品种数";
				head[13] = "已交货数";
				head[14] = "已交货金额";
				head[15] = "品种满足率";
				head[16] = "数量满足率";
				head[17] = "完成时间";
			}else{
				name = "采购订单明细及交货率统计(未满足情况).xls";
				head = new String[18];
				head[0] = "计划单号";
				head[1] = "制单日期";
				head[2] = "预计到货日期";
				head[3] = "计划类型";
				head[4] = "采购订单号";
				head[5] = "配件编码";
				head[6] = "配件名称";
				head[7] = "配件件号";
				head[8] = "单位";
				head[9] = "计划数量";
				head[10] = "订单数量";
				head[11] = "订单金额";
				head[12] = "已转验收单数量";
				head[13] = "剩余订单数量";
				head[14] = "订单创建日期";
				head[15] = "入库数量";
				head[16] = "入库金额";
				head[17] = "入库日期";
			}
			list = dao.queryPurOrderDtlDelivery(request);
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = null;
						if("1".equals(radioSelect)){
							detail = new String[19];
							detail[0] = CommonUtils.checkNull(map.get("PLAN_CODE"));
							detail[1] = CommonUtils.checkNull(map
									.get("CREATE_DATE"));
							detail[2] = CommonUtils
									.checkNull(map.get("FC_DATE"));
							detail[3] = CommonUtils.checkNull(map.get("PL_AMT"));
							detail[4] = CommonUtils.checkNull(map.get("PL_QTY"));
							detail[5] = CommonUtils.checkNull(map.get("PLAN_TYPE"));
							detail[6] = CommonUtils.checkNull(map.get("ORDER_CODE"));
							detail[7] = CommonUtils.checkNull(map.get("POCNT"));
							detail[8] = CommonUtils.checkNull(map.get("BUY_QTY"));
							detail[9] = CommonUtils.checkNull(map.get("BUY_AMT"));
							detail[10] = CommonUtils.checkNull(map.get("VENDER_NAME"));
							detail[11] = CommonUtils.checkNull(map.get("STATE"));
							detail[12] = CommonUtils.checkNull(map.get("INCNT"));
							detail[13] = CommonUtils.checkNull(map.get("IN_QTY"));
							detail[14] = CommonUtils.checkNull(map.get("IN_AMT"));
							detail[15] = CommonUtils.checkNull(map.get("CNTRT"));
							detail[16] = CommonUtils.checkNull(map.get("QTYRT"));
							detail[17] = CommonUtils.checkNull(map.get("CMPDATE"));
						}else{
							detail = new String[19];
							detail[0] = CommonUtils.checkNull(map.get("PLAN_CODE"));
							detail[1] = CommonUtils.checkNull(map
									.get("CREATE_DATE"));
							detail[2] = CommonUtils
									.checkNull(map.get("FC_DATE"));
							detail[3] = CommonUtils.checkNull(map.get("PLAN_TYPE"));
							detail[4] = CommonUtils.checkNull(map.get("ORDER_CODE"));
							detail[5] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
							detail[6] = CommonUtils.checkNull(map.get("PART_CNAME"));
							detail[7] = CommonUtils.checkNull(map.get("PART_CODE"));
							detail[8] = CommonUtils.checkNull(map.get("UNIT"));
							detail[9] = CommonUtils.checkNull(map.get("CHECK_NUM"));
							detail[10] = CommonUtils.checkNull(map.get("BUY_QTY"));
							detail[11] = CommonUtils.checkNull(map.get("BUY_AMOUNT"));
							detail[12] = CommonUtils.checkNull(map.get("CHECK_QTY"));
							detail[13] = CommonUtils.checkNull(map.get("SPARE_QTY"));
							detail[14] = CommonUtils.checkNull(map.get("O_CREATE_DATE"));
							detail[15] = CommonUtils.checkNull(map.get("IN_QTY"));
							detail[16] = CommonUtils.checkNull(map.get("IN_AMOUNT"));
							detail[17] = CommonUtils.checkNull(map.get("IN_DATE"));
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
			act.setForword(PART_PURORDERDTLDELIVERY_QUERY_URL);
		}
	}
	
	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list, String name)
			throws Exception {

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
