package com.infodms.dms.actions.parts.purchaseOrderManager;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
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
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartOemPoPO;
import com.infodms.dms.po.TtPartPoBalancePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : PurchaseOrderQuery 
 * @Description   : 采购订单查询 
 * @author        : chenjunjiang
 * CreateDate     : 2013-4-22
 */
public class PurchaseOrderQuery implements PTConstants {

	public Logger logger = Logger.getLogger(PurchaseOrderQuery.class);
	private PurchaseOrderDao dao = PurchaseOrderDao.getInstance();
	/**
	 * 
	 * @Title      : 
	 * @Description: 采购订单查询初始化,转到查询页面 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void purchaseOrderQueryInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List list = dao.getPartWareHouseList(logonUser);//获取配件库房信息
			act.setOutData("wareHouses", list);
            act.setOutData("old",CommonUtils.getPreviousXMonthFirst(-3));
            act.setOutData("now",CommonUtils.getDate());
			act.setForword(PART_PURCHASEORDER_QUERY_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"采购订单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 采购订单明细查询 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void queryPurchaseOrderDetailInfo(){
		

    	ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//采购订单号
			//String buyer = CommonUtils.checkNull(request.getParamValue("BUYER"));//采购员
			String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
			String inCode = CommonUtils.checkNull(request.getParamValue("IN_CODE"));//入库单号
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//制单开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//制单结束时间
			String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
			String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件种类
			String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
			String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
			String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
			String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
			String checkBeginTime = CommonUtils.checkNull(request.getParamValue("checkBeginTime"));//验货开始时间
			String checkEndTime = CommonUtils.checkNull(request.getParamValue("checkEndTime"));//验货结束时间
			String inBeginTime = CommonUtils.checkNull(request.getParamValue("inBeginTime"));//入库开始时间
			String inEndTime = CommonUtils.checkNull(request.getParamValue("inEndTime"));//入库结束时间
			String balanceBeginTime = CommonUtils.checkNull(request.getParamValue("balanceBeginTime"));//结算开始时间
			String balanceEndTime = CommonUtils.checkNull(request.getParamValue("balanceEndTime"));//结算结束时间
			
			TtPartOemPoPO po = new TtPartOemPoPO();
			
			po.setOrderCode(orderCode);
			if(!"".equals(whId)){
				po.setWhId(CommonUtils.parseLong(whId));
			}
			if(!"".equals(partType)){
				po.setPartType(CommonUtils.parseInteger(partType));
			}
			if(!"".equals(venderId)){
				po.setVenderId(CommonUtils.parseLong(venderId));
			}
			if(!"".equals(partOldCode)){
				po.setPartOldcode(partOldCode);
			}
			if(!"".equals(partCode)){
				po.setPartCode(partCode);
			}
			if(!"".equals(partName)){
				po.setPartCname(partName);
			}
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPurchaseOrderDetailList(po,beginTime,endTime,checkBeginTime,checkEndTime
					,inBeginTime,inEndTime,balanceBeginTime,balanceEndTime,chkCode,inCode,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"采购订单明细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 导出采购订单明细 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void exportPurchaseOrderExcel(){


		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		List wareHouses= new ArrayList();
		try {
			RequestWrapper request = act.getRequest();
			String[] head = new String[23];
			head[0] = "采购订单号";
			head[1] = "验收单号";
			head[2] = "入库单号";
			head[3] = "采购员";
			head[4] = "配件类型";
			head[5] = "配件编码";
			head[6] = "配件名称";
			head[7] = "配件件号";//add zhumingwei 2013-09-17
            head[8] = "单位";
			head[9] = "订购数量";
			head[10] = "已验货数量";
			head[11] = "入库数量";
            head[12] = "计划价";
            head[13] = "入库金额";
			head[14] = "已结算数量";
			head[15] = "结算单价";
			head[16] = "结算金额";
			head[17] = "供应商名称";
			head[18] = "制造商名称";
			head[19] = "库房";
			head[20] = "验货日期";
			head[21] = "入库日期";
			head[22] = "结算日期";
			List<Map<String, Object>> list = dao.queryPurchaseOrder(request);
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[23];
						detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
						detail[1] = CommonUtils.checkNull(map
								.get("CHK_CODE"));
						detail[2] = CommonUtils
								.checkNull(map.get("IN_CODE"));
						detail[3] = CommonUtils.checkNull(map
								.get("BUYER"));
						detail[4] = CommonUtils.checkNull(map
								.get("PART_TYPE"));
						detail[5] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
						detail[6] = CommonUtils.checkNull(map.get("PART_CNAME"));
						detail[7] = CommonUtils.checkNull(map.get("PART_CODE"));//add zhumingwei 2013-09-17
						detail[8] = CommonUtils.checkNull(map.get("UNIT"));
						detail[9] = CommonUtils.checkNull(map.get("BUY_QTY"));
						detail[10] = CommonUtils.checkNull(map.get("CHECK_QTY"));
						detail[11] = CommonUtils.checkNull(map.get("IN_QTY"));
                        detail[12] = CommonUtils.checkNull(map.get("PLAN_PRICE")).replace(",","");
                        detail[13] = CommonUtils.checkNull(map.get("IN_AMOUNT")).replace(",","");
						detail[14] = CommonUtils.checkNull(map.get("BALANCE_QTY"));
						detail[15] = CommonUtils.checkNull(map.get("BUY_PRICE")).replace(",","");
						detail[16] = CommonUtils.checkNull(map.get("BAL_AMOUNT")).replace(",","");
						detail[17] = CommonUtils.checkNull(map.get("VENDER_NAME"));
						detail[18] = CommonUtils.checkNull(map.get("MAKER_NAME"));
						detail[19] = CommonUtils.checkNull(map.get("WH_NAME"));
						detail[20] = CommonUtils.checkNull(map.get("CREATE_DATE"));
						detail[21] = CommonUtils.checkNull(map.get("IN_DATE"));
						detail[22] = CommonUtils.checkNull(map.get("BALANCE_DATE"));
						list1.add(detail);
					}
				}
				this.exportEx(ActionContext.getContext().getResponse(),
						request, head, list1);
			} else {
				wareHouses = dao.getPartWareHouseList(logonUser);//获取配件库房信息
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
			act.setOutData("wareHouses", wareHouses);
			act.setForword(PART_PURCHASEORDER_QUERY_URL);
		}
	
	}
	
	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "采购订单明细.xls";
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
