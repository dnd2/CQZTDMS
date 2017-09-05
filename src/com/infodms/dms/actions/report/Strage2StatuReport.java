package com.infodms.dms.actions.report;

import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.demo.Write;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;


import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.report.Strage2StatuDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class Strage2StatuReport {
	private Logger logger = Logger.getLogger(Strage2StatuReport.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private Strage2StatuDao dao =Strage2StatuDao.getInstance();
	private static final String Strage2Statu_URL ="/jsp/report/totalReport/Strage2Statu.jsp";
	
	
	public void getStrage2StatuReport(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			
			
			//只有大区范围限制	2012-08-29 韩晓宇
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
				orgId = logonUser.getOrgId().toString();
			}
			//添加业务范围限制	2012-08-29 韩晓宇
			if(areaId == null || "".equals(areaId)) {
				Long poseId = logonUser.getPoseId();
				List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
				for(int i=0; i<areaList.size(); i++) {
					areaId += areaList.get(i).get("AREA_ID");
					if(i != areaList.size()-1){
						areaId +=  ",";
					} 
				}
			}
			
			map.put("areaId", areaId);
			map.put("dealerId", dealerId);
			map.put("orgId", orgId);
			map.put("modelId", modelId);
			map.put("orgCode", orgCode);
			
			List<Map<String, Object>> list_Strage2Statu = dao.getStrage2StatuSelect(map);
			
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			
			
			int len = list_Strage2Statu.size();
			int node_bill_amount = 0;
			int deing_amount = 0;
			int stock_amount = 0;
			String sals_model_group_code = null;
			int sum = 0;
			for (int i = 0; i < len; i++) {
				Object sum_node = list_Strage2Statu.get(i).get("NODE_BILL_AMOUNT");
				int sum_nodes = Integer.parseInt(sum_node.toString());
				node_bill_amount += sum_nodes;
				
				Object sum_deing = list_Strage2Statu.get(i).get("DEING_AMOUNT");
				int sum_deings = Integer.parseInt(sum_deing.toString());
				deing_amount += sum_deings;
				
				Object sum_stock = list_Strage2Statu.get(i).get("STOCK_AMOUNT");
				int sum_stocks = Integer.parseInt(sum_stock.toString());
				stock_amount += sum_stocks;
				
			    //sals_model_group_code = list_Strage2Statu.get(i).get("SALS_MODEL_GROUP_CODE").toString();
				sum = node_bill_amount + deing_amount + stock_amount;
			}
			act.setOutData("sum", sum);
			act.setOutData("node_bill_amount", node_bill_amount);
			act.setOutData("deing_amount", deing_amount);
			act.setOutData("stock_amount", stock_amount);
			act.setOutData("sals_model_group_code", sals_model_group_code);
			
			act.setOutData("list_Strage2Statu", list_Strage2Statu);
			act.setForword(Strage2Statu_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车型库存状态表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void Strage2StatuReportDownload(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			
			map.put("areaId", areaId);
			map.put("dealerId", dealerId);
			map.put("orgId", orgId);
			map.put("modelId", modelId);
			
			
			
			// 导出的文件名
			String fileName = "车型库存状态表.csv";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			
			List<List<Object>> lists = new LinkedList<List<Object>>();
			List<Object> list = new LinkedList<Object>();
			
			list.add("车型系列");
			list.add("车型编码");
			list.add("启票未发");
			list.add("发运在途");
			list.add("在库");
			list.add("合计");
			lists.add(list);
			
			List<Map<String, Object>> result = dao.getStrage2StatuSelect(map);
			int len = result.size();
			for (int i = 0; i < len; i++) {
				Map<String, Object> record = result.get(i);
				list = new LinkedList<Object>();
				list.add(CommonUtils.checkNull(record.get("SALES_MODEL_GROUP_NAME")));
				list.add(CommonUtils.checkNull(record.get("MODEL_CODE")));
				list.add(CommonUtils.checkNull(record.get("NODE_BILL_AMOUNT")));
				list.add(CommonUtils.checkNull(record.get("DEING_AMOUNT")));
				list.add(CommonUtils.checkNull(record.get("STOCK_AMOUNT")));
				list.add(CommonUtils.checkNull(record.get("SUM_TOTAL")));
				lists.add(list);
			}
			
			OutputStream os = response.getOutputStream();
			CsvWriterUtil.writeCsv(lists, os);
			os.flush();
			os.close();
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车型库存状态表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void download(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			
			//只有大区范围限制	2012-08-29 韩晓宇
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
				orgId = logonUser.getOrgId().toString();
			}
			//添加业务范围限制	2012-08-29 韩晓宇
			if(areaId == null || "".equals(areaId)) {
				Long poseId = logonUser.getPoseId();
				List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
				for(int i=0; i<areaList.size(); i++) {
					areaId += areaList.get(i).get("AREA_ID");
					if(i != areaList.size()-1){
						areaId +=  ",";
					} 
				}
			}
			
			map.put("areaId", areaId);
			map.put("dealerId", dealerId);
			map.put("orgId", orgId);
			map.put("modelId", modelId);
			
			
			
			// 导出的文件名
			String fileName = "车型库存状态表.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			List<Map<String, Object>> list = dao.getStrage2StatuSelect(map);
			int len = list.size();
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("车型库存状态表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			int y = 0;
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			sheet.mergeCells(0, y, 5, y);
			sheet.addCell(new jxl.write.Label(0, y, "车型库存状态表",wcf));
			
			++y;
			sheet.mergeCells(0, y, 5, y);
			sheet.addCell(new jxl.write.Label(0, y, "日期："+String.valueOf(year)+"年"+String.valueOf(month)+"月"+String.valueOf(day)+"日"));
			
			++y;
			
			
			sheet.addCell(new Label(0, y, "车型系列"));
			sheet.addCell(new Label(1, y, "车型编码"));
			sheet.addCell(new Label(2, y, "启票未发"));
			sheet.addCell(new Label(3, y, "发运在途"));
			sheet.addCell(new Label(4, y, "在库"));
			sheet.addCell(new Label(5, y, "合计"));
			
			
			long tnba =0;
			long tda = 0;
			long tsa = 0;
			long tst = 0;
			
			if(list!=null){
				long snba =0;
				long sda = 0;
				long ssa = 0;
				long sst = 0;
				
				
				for (int i = 0; i < len; i++) {
					long nba = Long.parseLong(list.get(i).get("NODE_BILL_AMOUNT").toString());
					long da = Long.parseLong(list.get(i).get("DEING_AMOUNT").toString());
					long sa = Long.parseLong(list.get(i).get("STOCK_AMOUNT").toString());
					long st = Long.parseLong(list.get(i).get("SUM_TOTAL").toString());
					if(i==0){
						snba = nba;
						sda = da;
						ssa = sa;
						sst = st;
						
						
						tnba = nba;
						tda = da;
						tsa = sa;
						tst = st;
						
						++y;
						sheet.addCell(new Label(0, y, list.get(i).get("SERIES_NAME").toString()));
						sheet.addCell(new Label(1, y, list.get(i).get("MODEL_CODE").toString()));
						
						if(Integer.valueOf(list.get(i).get("NODE_BILL_AMOUNT").toString())==0){
							sheet.addCell(new Label(2, y,""));		
						}
						else{
							sheet.addCell(new Label(2, y,list.get(i).get("NODE_BILL_AMOUNT").toString()));	
						}			
						
						if(Integer.valueOf(list.get(i).get("DEING_AMOUNT").toString())==0){
							sheet.addCell(new Label(3, y,""));		
						}
						else{
							sheet.addCell(new Label(3, y,list.get(i).get("DEING_AMOUNT").toString()));	
						}	
						
						if(Integer.valueOf(list.get(i).get("STOCK_AMOUNT").toString())==0){
							sheet.addCell(new Label(4, y,""));		
						}
						else{
							sheet.addCell(new Label(4, y,list.get(i).get("STOCK_AMOUNT").toString()));	
						}			
						if(Integer.valueOf(list.get(i).get("SUM_TOTAL").toString())==0){
							sheet.addCell(new Label(5, y,""));		
						}
						else{
							sheet.addCell(new Label(5, y,list.get(i).get("SUM_TOTAL").toString()));	
						}			
					
					}else{
						String befor = list.get(i-1).get("MODEL_CODE").toString();
						String after = list.get(i).get("MODEL_CODE").toString();
						if(after.equals(befor)){
							snba += nba;
							sda += da;
							ssa += sa;
							sst += st;
							
							tnba += nba ;
							tda += da;
							tsa += sa;
							tst += st;
							
							++y;
							sheet.addCell(new Label(0, y, list.get(i).get("SERIES_NAME").toString()));
							sheet.addCell(new Label(1, y, list.get(i).get("MODEL_CODE").toString()));
							if(Integer.valueOf(list.get(i).get("NODE_BILL_AMOUNT").toString())==0){
								sheet.addCell(new Label(2, y,""));		
							}
							else{
								sheet.addCell(new Label(2, y,list.get(i).get("NODE_BILL_AMOUNT").toString()));	
							}			
							
							if(Integer.valueOf(list.get(i).get("DEING_AMOUNT").toString())==0){
								sheet.addCell(new Label(3, y,""));		
							}
							else{
								sheet.addCell(new Label(3, y,list.get(i).get("DEING_AMOUNT").toString()));	
							}	
							
							if(Integer.valueOf(list.get(i).get("STOCK_AMOUNT").toString())==0){
								sheet.addCell(new Label(4, y,""));		
							}
							else{
								sheet.addCell(new Label(4, y,list.get(i).get("STOCK_AMOUNT").toString()));	
							}			
							if(Integer.valueOf(list.get(i).get("SUM_TOTAL").toString())==0){
								sheet.addCell(new Label(5, y,""));		
							}
							else{
								sheet.addCell(new Label(5, y,list.get(i).get("SUM_TOTAL").toString()));	
							}			
						
						
						}else{
							++y;
							sheet.mergeCells(0, y, 1, y);
							sheet.addCell(new Label(0, y, "车型合计",wcf));
						if(snba==0){
							sheet.addCell(new Label(2, y, ""));
						}
						else{
							sheet.addCell(new Label(2, y, String.valueOf(snba)));
						}
						if(sda==0){
							sheet.addCell(new Label(3, y, ""));
						}
						else{
							sheet.addCell(new Label(3, y, String.valueOf(sda)));
						}
						if(ssa==0){
							sheet.addCell(new Label(4, y, ""));
						}
						else{
							sheet.addCell(new Label(4, y, String.valueOf(ssa)));
						}
						if(sst==0){
							sheet.addCell(new Label(5, y, ""));
						}
						else{
							sheet.addCell(new Label(5, y, String.valueOf(sst)));
						}
							
							
							snba = nba;
							sda = da;
							ssa = sa;
							sst = st;
							
							tnba += nba ;
							tda += da;
							tsa += sa;
							tst += st;
							
							
							++y;
							sheet.addCell(new Label(0, y, list.get(i).get("SERIES_NAME").toString()));
							sheet.addCell(new Label(1, y, list.get(i).get("MODEL_CODE").toString()));
						
							if(Integer.valueOf(list.get(i).get("NODE_BILL_AMOUNT").toString())==0){
								sheet.addCell(new Label(2, y,""));		
							}
							else{
								sheet.addCell(new Label(2, y,list.get(i).get("NODE_BILL_AMOUNT").toString()));	
							}			
							
							if(Integer.valueOf(list.get(i).get("DEING_AMOUNT").toString())==0){
								sheet.addCell(new Label(3, y,""));		
							}
							else{
								sheet.addCell(new Label(3, y,list.get(i).get("DEING_AMOUNT").toString()));	
							}	
							
							if(Integer.valueOf(list.get(i).get("STOCK_AMOUNT").toString())==0){
								sheet.addCell(new Label(4, y,""));		
							}
							else{
								sheet.addCell(new Label(4, y,list.get(i).get("STOCK_AMOUNT").toString()));	
							}			
							if(Integer.valueOf(list.get(i).get("SUM_TOTAL").toString())==0){
								sheet.addCell(new Label(5, y,""));		
							}
							else{
								sheet.addCell(new Label(5, y,list.get(i).get("SUM_TOTAL").toString()));	
							}			
						
						
							
						}
						
						if(i==len-1){
							
							++y;
							sheet.mergeCells(0, y, 1, y);
							sheet.addCell(new Label(0, y, "车型合计",wcf));
							if(snba==0){
								sheet.addCell(new Label(2, y, ""));
							}
							else{
								sheet.addCell(new Label(2, y, String.valueOf(snba)));
							}
							if(sda==0){
								sheet.addCell(new Label(3, y, ""));
							}
							else{
								sheet.addCell(new Label(3, y, String.valueOf(sda)));
							}
							if(ssa==0){
								sheet.addCell(new Label(4, y, ""));
							}
							else{
								sheet.addCell(new Label(4, y, String.valueOf(ssa)));
							}
							if(sst==0){
								sheet.addCell(new Label(5, y, ""));
							}
							else{
								sheet.addCell(new Label(5, y, String.valueOf(sst)));
							}
							
							
						}
					}
				}
			}
			
			
			++y;
			sheet.mergeCells(0, y, 1, y);
			sheet.addCell(new Label(0, y, "合计",wcf));
			if(tnba==0){
				sheet.addCell(new Label(2, y,""));
			}
			else{
				sheet.addCell(new Label(2, y, String.valueOf(tnba)));
			}
			
			if(tda==0){
				sheet.addCell(new Label(3, y,""));
			}
			else{
				sheet.addCell(new Label(3, y, String.valueOf(tda)));
			}
			
			if(tsa==0){
				sheet.addCell(new Label(4, y,""));
			}
			else{
				sheet.addCell(new Label(4, y, String.valueOf(tsa)));
			}
			if(tst==0){
				sheet.addCell(new Label(5, y,""));
			}
			else{
				sheet.addCell(new Label(5, y, String.valueOf(tst)));
			}
		
			
			workbook.write();
			workbook.close();
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车型库存状态表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

}
