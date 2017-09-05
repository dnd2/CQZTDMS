package com.infodms.dms.actions.parts.purchaseOrderManager;

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
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartOemPoPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : PurchaseOrderInDiffQuery 
 * @Description   : 采购订单入库差异 
 * @author        : chenjunjiang
 * CreateDate     : 2013-5-14
 */
public class PurchaseOrderInDiffQuery implements PTConstants {
	public Logger logger = Logger.getLogger(PurchaseOrderInDiffQuery.class);
	private PurchaseOrderDao dao = PurchaseOrderDao.getInstance();
	/**
	 * 
	 * @Title      : 
	 * @Description: 采购订单入库差异查询初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-14
	 */
	public void purchaseOrderInDiffQueryInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List list = dao.getPartWareHouseList(logonUser);//获取配件库房信息
			act.setOutData("wareHouses", list);
            act.setOutData("old",CommonUtils.getPreviousXMonthFirst(-3));
            act.setOutData("now",CommonUtils.getDate());
			act.setForword(PART_PURCHASEORDER_INDIFF_QUERY_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"采购订单入库差异");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询采购订单入库差异 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-14
	 */
    public void queryPurchaseOrderInDiffInfo(){
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
			if(!"".equals(partName)){
				po.setPartCname(partName);
			}
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPurchaseOrderInDiffList(po,beginTime,endTime,checkBeginTime,checkEndTime
					,inBeginTime,inEndTime,balanceBeginTime,balanceEndTime,chkCode,inCode,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"采购订单入库差异");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    
	}
    /**
     * 
     * @Title      : 
     * @Description: 导出采购订单入库差异 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-5-14
     */
    public void expPurchaseOrderInDiffExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		List wareHouses= new ArrayList();
		try {
			RequestWrapper request = act.getRequest();
			String[] head = new String[21];
			head[0] = "采购订单号";
			head[1] = "验收单号";
			head[2] = "入库单号";
			head[3] = "采购员";
			head[4] = "配件类型";
			head[5] = "配件编码";
			head[6] = "配件名称";
			head[7] = "订购数量";
			head[8] = "已验货数量";
			head[9] = "已入库数量";
			head[10] = "入库差异数量";
			head[11] = "已结算数量";
			head[12] = "结算单价";
			head[13] = "结算金额";
			head[14] = "供应商名称";
			head[15] = "制造商名称";
			head[16] = "库房";
			head[17] = "验货日期";
			head[18] = "入库日期";
			head[19] = "结算日期";
			List<Map<String, Object>> list = dao.queryPurchaseOrderInDiff(request);
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[21];
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
						detail[7] = CommonUtils.checkNull(map.get("BUY_QTY"));
						detail[8] = CommonUtils.checkNull(map.get("CHECK_QTY"));
						detail[9] = CommonUtils.checkNull(map.get("IN_QTY"));
						detail[10] = CommonUtils.checkNull(map.get("INDIFF_QTY"));
						detail[11] = CommonUtils.checkNull(map.get("BALANCE_QTY"));
						detail[12] = CommonUtils.checkNull(map.get("BUY_PRICE"));
						detail[13] = CommonUtils.checkNull(map.get("IN_AMOUNT"));
						detail[14] = CommonUtils.checkNull(map.get("VENDER_NAME"));
						detail[15] = CommonUtils.checkNull(map.get("MAKER_NAME"));
						detail[16] = CommonUtils.checkNull(map.get("WH_NAME"));
						detail[17] = CommonUtils.checkNull(map.get("CREATE_DATE"));
						detail[18] = CommonUtils.checkNull(map.get("IN_DATE"));
						detail[19] = CommonUtils.checkNull(map.get("BALANCE_DATE"));
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
			act.setForword(PART_PURCHASEORDER_INDIFF_QUERY_URL);
		}
	
	}
	
	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "采购订单入库差异.xls";
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
