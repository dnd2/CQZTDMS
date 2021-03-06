package com.infodms.dms.actions.report.partReport.partPlanReport;

import java.io.OutputStream;
import java.net.URLEncoder;
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
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderBalanceDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderInDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class recvOrderReport implements PTConstants{

	public Logger logger = Logger.getLogger(recvOrderReport.class);
	private PurchaseOrderBalanceDao dao = PurchaseOrderBalanceDao.getInstance();
	private PurchaseOrderInDao dao1 = PurchaseOrderInDao.getInstance();
	
	private static String PART_PLANE_RECV_RPT_URL = "/jsp/report/partPlanRecvReport/partPlanRecvRpt.jsp";
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 计划领用单查询初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-22
	 */
	public void recvOrderReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			List list = dao1.getPartWareHouseList(logonUser);// 获取配件库房信息
			act.setOutData("wareHouses", list);
            act.setOutData("old",CommonUtils.getPreviousXMonthFirst(-3));
            act.setOutData("now",CommonUtils.getDate());
			act.setForword(PART_PLANE_RECV_RPT_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "计划领用单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 计划领用单查询 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-22
	 */
	public void queryPurOrderBalance(){
		 ActionContext act = ActionContext.getContext();
         RequestWrapper request = act.getRequest();
         AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
         try {
             //分页方法 begin
             Integer curPage = request.getParamValue("curPage") != null ? Integer
                     .parseInt(request.getParamValue("curPage"))
                     : 1; // 处理当前页
             PageResult<Map<String, Object>> ps = dao.queryRecvOrderReport(request,
                     curPage, Constant.PAGE_SIZE);
             
             PageResult<Map<String, Object>> psAll = dao.queryRecvOrderReport(request,
                     1, Constant.PAGE_SIZE_MAX);
             int dtlCount = 0;
             long qtyCount = 0l;
             
             if(null != psAll.getRecords() && psAll.getRecords().size() > 0)
             {
            	 List<Map<String, Object>> list = psAll.getRecords();
            	 dtlCount = list.size();
            	 
            	 for(int i = 0; i < dtlCount; i ++)
            	 {
            		 qtyCount += Integer.parseInt(list.get(i).get("BALANCE_QTY").toString());
            	 }
             }
             
             //分页方法 end
             act.setOutData("dtlCount", dtlCount);
             act.setOutData("qtyCount", qtyCount);
             act.setOutData("ps", ps);
         } catch (Exception e) {//异常方法
             BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "计划领用单");
             logger.error(logonUser, e1);
             act.setException(e1);
         }
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询单个领用单明细初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-22
	 */
	public void queryInfoByCodeInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String balanceCode = CommonUtils.checkNull(request.getParamValue("balanceCode"));
			act.setForword(PART_PURORDERBALANCELY_QUERY_URL);
			act.setOutData("balanceCode", balanceCode);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "领用单明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询单个领用单明细 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-22
	 */
	public void queryPurOrderBalanceByCode(){
		 ActionContext act = ActionContext.getContext();
         RequestWrapper request = act.getRequest();
         AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
         try {
             //分页方法 begin
             Integer curPage = request.getParamValue("curPage") != null ? Integer
                     .parseInt(request.getParamValue("curPage"))
                     : 1; // 处理当前页
             PageResult<Map<String, Object>> ps = dao.queryPurOrderBalByCodeList(request,
                     curPage, Constant.PAGE_SIZE);
             //分页方法 end
             act.setOutData("ps", ps);
         } catch (Exception e) {//异常方法
             BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "领用单明细");
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
	 * LastDate    : 2013-9-22
	 */
	public void expPurOrderBalanceExcel(){

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String radioSelect = request.getParamValue("RADIO_SELECT");
			//分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
			String[] head = null;
			List<Map<String, Object>> list;
			if("1".equals(radioSelect)){
				head = new String[20];
				head[0] = "计划领用单号";
				head[1] = "进货单号";
				head[2] = "采购订单号";
				head[3] = "配件编码";
				head[4] = "配件名称";
				head[5] = "件号";
				head[6] = "单位";
				head[7] = "进货数量";
				head[8] = "入库数量";
				head[9] = "开票数量";
				head[10] = "入库退货数量";
				head[11] = "单价";
				head[12] = "金额";
				head[13] = "发票号";
				head[14] = "编制人";
				head[15] = "编制时间";
				head[16] = "供货商名称";
				head[17] = "供货厂家名称";
				head[18] = "状态";
			}else{
				head = new String[15];
				head[0] = "计划领用单号";
				head[1] = "进货单号";
				head[2] = "采购订单号";
				head[3] = "进货数量";
				head[4] = "入库数量";
				head[5] = "开票数量";
				head[6] = "入库退货数量";
				head[7] = "金额";
				head[8] = "发票号";
				head[9] = "编制人";
				head[10] = "编制时间";
				head[11] = "状态";
			}
            PageResult<Map<String, Object>> ps = dao.queryRecvOrderReport(request,
                    curPage, Constant.PAGE_SIZE_MAX);
			list = ps.getRecords();
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = null;
						if("1".equals(radioSelect)){
							detail = new String[20];
							detail[0] = CommonUtils.checkNull(map.get("BALANCE_CODE"));
							detail[1] = CommonUtils.checkNull(map
									.get("CHECK_CODE"));
							detail[2] = CommonUtils
									.checkNull(map.get("ORDER_CODE"));
							detail[3] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
							detail[4] = CommonUtils.checkNull(map.get("PART_CNAME"));
							detail[5] = CommonUtils.checkNull(map.get("PART_CODE"));
							detail[6] = CommonUtils.checkNull(map.get("UNIT"));
							detail[7] = CommonUtils.checkNull(map.get("CHECK_QTY"));
							detail[8] = CommonUtils.checkNull(map.get("IN_QTY"));
							detail[9] = CommonUtils.checkNull(map.get("BALANCE_QTY"));
							detail[10] = CommonUtils.checkNull(map.get("RETURN_QTY"));
							detail[11] = CommonUtils.checkNull(map.get("BUY_PRICE"));
							detail[12] = CommonUtils.checkNull(map.get("BALANCE_AMOUNT"));
							detail[13] = CommonUtils.checkNull(map.get("INVO_NO"));
							detail[14] = CommonUtils.checkNull(map.get("NAME"));
							detail[15] = CommonUtils.checkNull(map.get("CREATE_DATE"));
							detail[16] = CommonUtils.checkNull(map.get("VENDER_NAME"));
							detail[17] = CommonUtils.checkNull(map.get("MAKER_NAME"));
							detail[18] = CommonUtils.checkNull(map.get("CODE_DESC"));
						}else{
							detail = new String[15];
							detail[0] = CommonUtils.checkNull(map.get("BALANCE_CODE"));
							detail[1] = CommonUtils.checkNull(map
									.get("CHECK_CODE"));
							detail[2] = CommonUtils
									.checkNull(map.get("ORDER_CODE"));
							detail[3] = CommonUtils.checkNull(map.get("CHECK_QTY"));
							detail[4] = CommonUtils.checkNull(map.get("IN_QTY"));
							detail[5] = CommonUtils.checkNull(map.get("BALANCE_QTY"));
							detail[6] = CommonUtils.checkNull(map.get("RETURN_QTY"));
							detail[7] = CommonUtils.checkNull(map.get("BALANCE_AMOUNT"));
							detail[8] = CommonUtils.checkNull(map.get("INVO_NO"));
							detail[9] = CommonUtils.checkNull(map.get("NAME"));
							detail[10] = CommonUtils.checkNull(map.get("CREATE_DATE"));
							detail[11] = CommonUtils.checkNull(map.get("CODE_DESC"));
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
			act.setForword(PART_PLANE_RECV_RPT_URL);
		}
	
	}
	
	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list, int flag)
			throws Exception {

		String name = "领用单查询及统计.xls";
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
