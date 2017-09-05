/**********************************************************************
* <pre>
* FILE : DeliveryQuery.java
* CLASS : DeliveryQuery
* AUTHOR : 
* FUNCTION : 订单发运
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-26|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sales.ordermanage.delivery;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.DeliveryAmountDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-26
 * @author  
 * @mail   	
 * @version 1.0
 * @remark 订单发运查询
 */
public class DeliveryAmount {
	
	public Logger logger = Logger.getLogger(DeliveryApply.class);   
	DeliveryAmountDao dao  = DeliveryAmountDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final String initUrl = "/jsp/sales/ordermanage/delivery/deliveryAmountInit.jsp";
	
	/**
	 * 发运申请数量查询页面初始化
	 */
	public void deliveryAmountInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
	            act.setOutData("dutyType",logonUser.getDutyType());
	            //act.setOutData("dealerId",logonUser.getDealerId());
	            act.setOutData("orgId",logonUser.getOrgId());
            	//Map<String,String> map =new HashMap<String,String>();
	            act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订单发运查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 发运申请数量查询
	 */
	public void deliveryAmountQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dlvryNo=request.getParamValue("dlvryNo");	
			String orderNo=request.getParamValue("orderNo");
			String start_date=request.getParamValue("start_date");
			String end_date=request.getParamValue("end_date");
			String dealerCode=request.getParamValue("dealerCode");

			Map<String,String> map=new HashMap<String,String>();			
			map.put("start_date", start_date);
			map.put("end_date", end_date);
			map.put("dlvryNo", dlvryNo);
			map.put("orderNo", orderNo);
			map.put("dealerCode", dealerCode);
			map.put("dealerId", logonUser.getDealerId());
			map.put("dutyType", logonUser.getDutyType());
			map.put("orgId", logonUser.getOrgId().toString());
			map.put("userId", logonUser.getUserId().toString());
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps =  dao.deliveryAmountQuery(map,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订单发运查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 下载
	 */
	public void doDownload() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// 导出的文件名
			String fileName = "初审订单未通过车型统计.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");

			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			String start_date=CommonUtils.checkNull(request.getParamValue("start_date"));
			String end_date=CommonUtils.checkNull(request.getParamValue("end_date"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")) ;
            String dlvryNo = CommonUtils.checkNull(request.getParamValue("dlvryNo")) ;
            String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")) ;
            if(CommonUtils.isNullString(dealerCode)){
            	dealerCode = CommonUtils.checkNull(request.getParamValue("cdealerCode")) ;
            }
            
            Map<String, String> paraMap = new HashMap<String, String>();

            paraMap.put("start_date", start_date);
            paraMap.put("end_date", end_date);
            paraMap.put("dealerCode", dealerCode);
            paraMap.put("dlvryNo", dlvryNo);
            paraMap.put("orderNo", orderNo);
            paraMap.put("dutyType", logonUser.getDutyType());
            paraMap.put("orgId", logonUser.getOrgId().toString());
            paraMap.put("dealerId", logonUser.getDealerId());
            paraMap.put("dutyType", logonUser.getDutyType());
            paraMap.put("userId", logonUser.getUserId().toString());
            OutputStream os = response.getOutputStream();
    		WritableWorkbook workbook = Workbook.createWorkbook(os);
    		PageResult<Map<String, Object>> ps =null; 
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			ps= dao.deliveryAmountQuery(paraMap,curPage, 99999);
			List<Map<String, Object>> list=ps.getRecords();
			getDealerExcel(list,workbook);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "初审订单未通过车型统计");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getDealerExcel(List<Map<String, Object>> list,WritableWorkbook workbook) 
	throws IOException, RowsExceededException, WriteException{
		String codeID="";
		WritableSheet sheet = workbook.createSheet("初审订单未通过车型统计", 0);
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
		DisplayFormat displayFormat = NumberFormats.TEXT;
		WritableCellFormat format = new WritableCellFormat(wf,displayFormat);
		format.setAlignment(jxl.format.Alignment.LEFT);
		format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.NONE); 
		int y = 0;
		sheet.addCell(new Label(0, y, "审核时间"));
		sheet.addCell(new Label(1, y, "大区"));
		sheet.addCell(new Label(2, y, "省份"));
		sheet.addCell(new Label(3, y, "经销商代码"));
		sheet.addCell(new Label(4, y, "经销商名称"));
		sheet.addCell(new Label(5, y, "发运单号"));
		sheet.addCell(new Label(6, y, "订单号"));
		sheet.addCell(new Label(7, y, "物料编码"));
		sheet.addCell(new Label(8, y, "物料名称"));
		sheet.addCell(new Label(9, y, "审核状态"));
		sheet.addCell(new Label(10, y, "未发数量"));
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					++y;
					sheet.addCell(new Label(0, y, CommonUtils.checkNull(list.get(i) .get("AUDIT_TIME"))));
					sheet.addCell(new Label(1, y, CommonUtils.checkNull(list.get(i) .get("ROOT_ORG_NAME"))));
					sheet.addCell(new Label(2, y, CommonUtils.checkNull(list.get(i) .get("PQ_ORG_NAME"))));
					sheet.addCell(new Label(3, y, CommonUtils.checkNull(list.get(i) .get("DEALER_CODE"))));
					sheet.addCell(new Label(4, y, CommonUtils.checkNull(list.get(i) .get("DEALER_SHORTNAME"))));
					sheet.addCell(new Label(5, y, CommonUtils.checkNull(list.get(i) .get("DLVRY_REQ_NO"))));
					sheet.addCell(new Label(6, y, CommonUtils.checkNull(list.get(i).get("ORDER_NO"))));
					sheet.addCell(new Label(7, y, CommonUtils.checkNull(list.get(i) .get("MATERIAL_CODE"))));
					sheet.addCell(new Label(8, y, CommonUtils.checkNull(list.get(i).get("MATERIAL_NAME"))));
					sheet.addCell(new Label(9, y, CommonUtils.checkNull(list.get(i) .get("REQ_STATUS"))));
					sheet.addCell(new Label(10, y, CommonUtils.checkNull(list.get(i).get("AMOUNT"))));
				}
			}
		++y;
		}
	
	
}
