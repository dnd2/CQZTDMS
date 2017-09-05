package com.infodms.dms.actions.report.partReport.partSalesReport;

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
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderInDao;
import com.infodms.dms.dao.parts.salesManager.PartBoDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : BoCycleReport 
 * @Description   : BO周报表
 * @author        : chenjunjiang
 * CreateDate     : 2013-9-27
 */
public class BoCycleReport implements PTConstants{

	public Logger logger = Logger.getLogger(BoCycleReport.class);
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
	public void boCycleReportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			List list = dao1.getPartWareHouseList(logonUser);// 获取配件库房信息
			PurchasePlanSettingDao dao2 = PurchasePlanSettingDao.getInstance();
			List<Map<String, Object>> planerList = dao2.getUserPoseLise(1, null);
			act.setOutData("planerList", planerList);
			act.setOutData("wareHouses", list);
			act.setOutData("curUserId", logonUser.getUserId());
			act.setForword(PART_BO_CYCLE_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件BO周报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title :
	 * @Description: BO周报表查询
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-17
	 */
	public void queryPartBoCycle() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartBoCycleList(
					request, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "BO周报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title :
	 * @Description: 导出
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-17
	 */
	public void expPartBoCycleExcel() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String[] head = new String[17];
			head[0] = "配件类型";
			head[1] = "供应商";
			head[2] = "库房";
			head[3] = "配件编码";
			head[4] = "配件名称";
			head[5] = "件号";
			head[6] = "单位";
			head[7] = "BO项数";
			head[8] = "近一个月未满足BO数量";
			head[9] = "近一周未满足数量";
			head[10] = "近一周BO项数";
			head[11] = "在途数量";
			head[12] = "采购订单编号";
			head[13] = "订单编制时间 ";
			head[14] = "计划员";
			head[15] = "BO说明";
			List<Map<String, Object>> list = dao.queryPartBoCycle(request);
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[17];
						int partType = ((BigDecimal)map.get("PART_TYPE")).intValue();
						if(partType==Constant.PART_BASE_PART_TYPES_SELF_MADE.intValue()){
							detail[0] = "自制件";
						}else if(partType==Constant.PART_BASE_PART_TYPES_PURCHASE.intValue()){
							detail[0] = "国产件";
						}else{
							detail[0] = "进口件";
						}
						detail[1] = CommonUtils.checkNull(map
								.get("VENDER_NAME"));
						detail[2] = CommonUtils
								.checkNull(map.get("WH_NAME"));
						detail[3] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
						detail[4] = CommonUtils.checkNull(map.get("PART_CNAME"));
						detail[5] = CommonUtils.checkNull(map.get("PART_CODE"));
						detail[6] = CommonUtils.checkNull(map.get("UNIT"));
						detail[7] = CommonUtils.checkNull(map.get("BOCNT"));
						detail[8] = CommonUtils.checkNull(map.get("MTH_ODDQTY"));
						detail[9] = CommonUtils.checkNull(map.get("WEK_ODDQTY"));
						detail[10] = CommonUtils.checkNull(map.get("WEK_BOCNT"));
						detail[11] = CommonUtils.checkNull(map.get("OR_QTY"));
						detail[12] = CommonUtils.checkNull(map.get("ORDER_CODE"));
						detail[13] = CommonUtils.checkNull(map.get("CREATE_DATE"));
						detail[14] = CommonUtils.checkNull(map.get("NAME"));
						detail[15] = CommonUtils.checkNull(map.get("BO_NOTE"));
						list1.add(detail);
					}
				}
				this.exportEx(ActionContext.getContext().getResponse(),
						request, head, list1, 1);
			} else {
				List wareHouses = dao1.getPartWareHouseList(logonUser);// 获取配件库房信息
				PurchasePlanSettingDao dao2 = PurchasePlanSettingDao.getInstance();
				List<Map<String, Object>> planerList = dao2.getUserPoseLise(1, null);
				act.setOutData("planerList", planerList);
				act.setOutData("wareHouses", wareHouses);
				act.setOutData("curUserId", logonUser.getUserId());
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
			act.setForword(PART_BO_CYCLE_QUERY_URL);
		}
	}

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list, int flag)
			throws Exception {

		String name = "BO周报表.xls";
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
