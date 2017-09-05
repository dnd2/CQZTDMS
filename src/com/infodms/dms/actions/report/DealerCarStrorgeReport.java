package com.infodms.dms.actions.report;

import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
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
import com.infodms.dms.dao.report.DealerCarStrorgeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class DealerCarStrorgeReport {
	private Logger logger = Logger.getLogger(DealerCarStrorgeReport.class);
	private ActionContext act =ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private DealerCarStrorgeDao dao = DealerCarStrorgeDao.getInstance();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	
	
	private static final String DealerCarStrorg_URl = "/jsp/report/totalReport/dealerCarStrorge.jsp";
	
	public void getDealerCarStrorgeReport(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
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
			map.put("orgId", orgId);
			map.put("dealerId", dealerId);
			map.put("modelId", modelId);
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			List<Map<String, Object>> list_DealerCarStrorge = dao.getDealerCarStrorgeSelect(map);
			act.setOutData("list_DealerCarStrorge", list_DealerCarStrorge);
			act.setForword(DealerCarStrorg_URl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"产品库存状态");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void DealerCarStrorgeReportDownload(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			
			map.put("areaId", areaId);
			map.put("orgId", orgId);
			map.put("dealerId", dealerId);
			map.put("modelId", modelId);
			
			// 导出的文件名
			String fileName = "产品库存状态.csv";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			List<List<Object>> lists = new LinkedList<List<Object>>();
			List<Object> list = new LinkedList<Object>();
			
			list.add("车型系列");
			list.add("车型编码");
			list.add("三个月一下");
			list.add("3-6个月");
			list.add("6-9个月");
			list.add("9-12个月");
			list.add("1-2年");
			list.add("2年以上");
			list.add("合计");
			lists.add(list);
			
			List<Map<String, Object>> result = dao.getDealerCarStrorgeSelect(map);
			int len = result.size();
			for (int i = 0; i < len; i++) {
				Map<String, Object> record = result.get(i);
				list = new LinkedList<Object>();
				list.add(CommonUtils.checkNull(record.get("SALES_MODEL_GROUP_NAME")));
				list.add(CommonUtils.checkNull(record.get("SALES_MODEL_GROUP_CODE")));
				list.add(CommonUtils.checkNull(record.get("LESS_3_MONTH")));
				list.add(CommonUtils.checkNull(record.get("T0_6_MONTH")));
				list.add(CommonUtils.checkNull(record.get("TO_9_MONTH")));
				list.add(CommonUtils.checkNull(record.get("TO_12_MONTH")));
				list.add(CommonUtils.checkNull(record.get("TO_2_YEAR")));
				list.add(CommonUtils.checkNull(record.get("MORE_2_YEAR")));
				list.add(CommonUtils.checkNull(record.get("SUM_TOTAL")));
				lists.add(list);
			}
			OutputStream os = response.getOutputStream();
			CsvWriterUtil.writeCsv(lists, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"产品库存状态");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void download(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
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
			map.put("orgId", orgId);
			map.put("dealerId", dealerId);
			map.put("modelId", modelId);
			
			// 导出的文件名
			String fileName = "产品库存状态.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("产品库存状态", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			
			List<Map<String, Object>> list = dao.getDealerCarStrorgeSelect(map);
			int len = list.size();
			int y = 0;
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			sheet.mergeCells(0, y, 8, y);
			sheet.addCell(new jxl.write.Label(0, y, "产品库存状态",wcf));
			
			++y;
			sheet.mergeCells(0, y, 8, y);
			sheet.addCell(new jxl.write.Label(0, y, "日期："+String.valueOf(year)+"年"+String.valueOf(month)+"月"+String.valueOf(day)+"日"));
			
			++y;
			
			sheet.addCell(new Label(0, y, "车型系列"));
			sheet.addCell(new Label(1, y, "车型编码"));
			sheet.addCell(new Label(2, y, "三个月一下"));
			sheet.addCell(new Label(3, y, "3-6个月"));
			sheet.addCell(new Label(4, y, "6-9个月"));
			sheet.addCell(new Label(5, y, "9-12个月"));
			sheet.addCell(new Label(6, y, "1-2年"));
			sheet.addCell(new Label(7, y, "2年以上"));
			sheet.addCell(new Label(8, y, "合计"));
			
			long tl3m = 0 ;
			long tt6m = 0 ;
			long tt9m = 0 ;
			long tt12m = 0 ;
			long tt2y = 0 ;
			long tm2y = 0 ;
			long tst = 0 ;
			
			if(list!=null){
				long sl3m = 0 ;
				long st6m = 0 ;
				long st9m = 0 ;
				long st12m = 0 ;
				long st2y = 0 ;
				long sm2y = 0 ;
				long sst = 0 ;
				for (int i = 0; i < len; i++) {
					
					long l3m = Long.parseLong(list.get(i).get("LESS_3_MONTH").toString()) ;
					long t6m = Long.parseLong(list.get(i).get("TO_6_MONTH").toString()) ;
					long t9m = Long.parseLong(list.get(i).get("TO_9_MONTH").toString()) ;
					long t12m = Long.parseLong(list.get(i).get("TO_12_MONTH").toString()) ;
					long t2y = Long.parseLong(list.get(i).get("TO_2_YEAR").toString()) ;
					long m2y = Long.parseLong(list.get(i).get("MORE_2_YEAR").toString()) ;
					long st = Long.parseLong(list.get(i).get("SUM_TOTAL").toString()) ;
					
					if(i==0){
						tl3m = l3m ;
						tt6m = t6m ;
						tt9m = t9m ;
						tt12m = t12m ;
						tt2y = t2y ;
						tm2y = m2y ;
						tst = st ;
						
						sl3m = l3m ;
						st6m = t6m ;
						st9m = t9m ;
						st12m = t12m ;
						st2y = t2y ;
						sm2y = m2y ;
						sst = st ;
						
						
						++y;
						sheet.addCell(new Label(0, y, list.get(i).get("SERIES_NAME")==null ?"":list.get(i).get("SERIES_NAME").toString()));
						sheet.addCell(new Label(1, y, list.get(i).get("MODEL_CODE")==null ?"":list.get(i).get("MODEL_CODE").toString()));
						sheet.addCell(new Label(2, y,Integer.parseInt(list.get(i).get("LESS_3_MONTH").toString())==0 ?"":list.get(i).get("LESS_3_MONTH").toString()));
						sheet.addCell(new Label(3, y,Integer.parseInt(list.get(i).get("TO_6_MONTH").toString())==0 ?"":list.get(i).get("TO_6_MONTH").toString()));
						sheet.addCell(new Label(4, y,Integer.parseInt(list.get(i).get("TO_9_MONTH").toString())==0  ?"":list.get(i).get("TO_9_MONTH").toString()));
						sheet.addCell(new Label(5, y,Integer.parseInt(list.get(i).get("TO_12_MONTH").toString())==0  ?"":list.get(i).get("TO_12_MONTH").toString()));
						sheet.addCell(new Label(6, y,Integer.parseInt(list.get(i).get("TO_2_YEAR").toString())==0  ?"":list.get(i).get("TO_2_YEAR").toString()));
						sheet.addCell(new Label(7, y, Integer.parseInt(list.get(i).get("MORE_2_YEAR").toString())==0 ?"":list.get(i).get("MORE_2_YEAR").toString()));
						sheet.addCell(new Label(8, y,Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0 ?"":list.get(i).get("SUM_TOTAL").toString()));
					}else{
						
						String before = list.get(i-1).get("MODEL_CODE").toString() ;
						String after = list.get(i).get("MODEL_CODE").toString() ;
						if(after.equals(before)){
							tl3m += l3m ;
							tt6m += t6m ;
							tt9m += t9m ;
							tt12m += t12m ;
							tt2y += t2y ;
							tm2y += m2y ;
							tst += st ;
							
							sl3m += l3m ;
							st6m += t6m ;
							st9m += t9m ;
							st12m += t12m ;
							st2y += t2y ;
							sm2y += m2y ;
							sst += st ;
							
							++y;
							sheet.addCell(new Label(0, y, list.get(i).get("SERIES_NAME")==null ?"":list.get(i).get("SERIES_NAME").toString()));
							sheet.addCell(new Label(1, y, list.get(i).get("MODEL_CODE")==null ?"":list.get(i).get("MODEL_CODE").toString()));
							sheet.addCell(new Label(2, y,Integer.parseInt(list.get(i).get("LESS_3_MONTH").toString())==0 ?"":list.get(i).get("LESS_3_MONTH").toString()));
							sheet.addCell(new Label(3, y,Integer.parseInt(list.get(i).get("TO_6_MONTH").toString())==0 ?"":list.get(i).get("TO_6_MONTH").toString()));
							sheet.addCell(new Label(4, y,Integer.parseInt(list.get(i).get("TO_9_MONTH").toString())==0  ?"":list.get(i).get("TO_9_MONTH").toString()));
							sheet.addCell(new Label(5, y,Integer.parseInt(list.get(i).get("TO_12_MONTH").toString())==0  ?"":list.get(i).get("TO_12_MONTH").toString()));
							sheet.addCell(new Label(6, y,Integer.parseInt(list.get(i).get("TO_2_YEAR").toString())==0  ?"":list.get(i).get("TO_2_YEAR").toString()));
							sheet.addCell(new Label(7, y, Integer.parseInt(list.get(i).get("MORE_2_YEAR").toString())==0 ?"":list.get(i).get("MORE_2_YEAR").toString()));
							sheet.addCell(new Label(8, y,Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0 ?"":list.get(i).get("SUM_TOTAL").toString()));
						
						}else{
							++y;
							sheet.mergeCells(0, y, 1, y);
							sheet.addCell(new Label(0, y, "车型小计",wcf));
							sheet.addCell(new Label(1, y, ""));
							if(sl3m==0){
								sheet.addCell(new Label(2, y,"" ));
							}
							else{
								sheet.addCell(new Label(2, y,String.valueOf(sl3m) ));
							}
							if(st6m==0){
								sheet.addCell(new Label(3, y,"" ));
							}
							else{
								sheet.addCell(new Label(3, y,String.valueOf(st6m) ));
							}
							if(st9m==0){
								sheet.addCell(new Label(4, y,"" ));
							}
							else{
								sheet.addCell(new Label(4, y,String.valueOf(st9m) ));
							}
							if(st12m==0){
								sheet.addCell(new Label(5, y,"" ));
							}
							else{
								sheet.addCell(new Label(5, y,String.valueOf(st12m) ));
							}
							if(st2y==0){
								sheet.addCell(new Label(6, y,"" ));
							}
							else{
								sheet.addCell(new Label(6, y,String.valueOf(st2y) ));
							}
							if(sm2y==0){
								sheet.addCell(new Label(7, y,"" ));
							}
							else{
								sheet.addCell(new Label(7, y,String.valueOf(sm2y) ));
							}
							if(sst==0){
								sheet.addCell(new Label(8, y,"" ));
							}
							else{
								sheet.addCell(new Label(8, y,String.valueOf(sst) ));
							}
							
							
							tl3m += l3m ;
							tt6m += t6m ;
							tt9m += t9m ;
							tt12m += t12m ;
							tt2y += t2y ;
							tm2y += m2y ;
							tst += st ;
							
							sl3m = l3m ;
							st6m = t6m ;
							st9m = t9m ;
							st12m = t12m ;
							st2y = t2y ;
							sm2y = m2y ;
							sst = st ;
							
							++y;
							sheet.addCell(new Label(0, y, list.get(i).get("SERIES_NAME")==null ?"":list.get(i).get("SERIES_NAME").toString()));
							sheet.addCell(new Label(1, y, list.get(i).get("MODEL_CODE")==null ?"":list.get(i).get("MODEL_CODE").toString()));
							sheet.addCell(new Label(2, y,Integer.parseInt(list.get(i).get("LESS_3_MONTH").toString())==0 ?"":list.get(i).get("LESS_3_MONTH").toString()));
							sheet.addCell(new Label(3, y,Integer.parseInt(list.get(i).get("TO_6_MONTH").toString())==0 ?"":list.get(i).get("TO_6_MONTH").toString()));
							sheet.addCell(new Label(4, y,Integer.parseInt(list.get(i).get("TO_9_MONTH").toString())==0  ?"":list.get(i).get("TO_9_MONTH").toString()));
							sheet.addCell(new Label(5, y,Integer.parseInt(list.get(i).get("TO_12_MONTH").toString())==0  ?"":list.get(i).get("TO_12_MONTH").toString()));
							sheet.addCell(new Label(6, y,Integer.parseInt(list.get(i).get("TO_2_YEAR").toString())==0  ?"":list.get(i).get("TO_2_YEAR").toString()));
							sheet.addCell(new Label(7, y, Integer.parseInt(list.get(i).get("MORE_2_YEAR").toString())==0 ?"":list.get(i).get("MORE_2_YEAR").toString()));
							sheet.addCell(new Label(8, y,Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0 ?"":list.get(i).get("SUM_TOTAL").toString()));
						}
						if(i==len-1){
							++y;
							sheet.mergeCells(0, y, 1, y);
							sheet.addCell(new Label(0, y, "车型小计",wcf));
							sheet.addCell(new Label(1, y, ""));
							if(sl3m==0){
								sheet.addCell(new Label(2, y,"" ));
							}
							else{
								sheet.addCell(new Label(2, y,String.valueOf(sl3m) ));
							}
							if(st6m==0){
								sheet.addCell(new Label(3, y,"" ));
							}
							else{
								sheet.addCell(new Label(3, y,String.valueOf(st6m) ));
							}
							if(st9m==0){
								sheet.addCell(new Label(4, y,"" ));
							}
							else{
								sheet.addCell(new Label(4, y,String.valueOf(st9m) ));
							}
							if(st12m==0){
								sheet.addCell(new Label(5, y,"" ));
							}
							else{
								sheet.addCell(new Label(5, y,String.valueOf(st12m) ));
							}
							if(st2y==0){
								sheet.addCell(new Label(6, y,"" ));
							}
							else{
								sheet.addCell(new Label(6, y,String.valueOf(st2y) ));
							}
							if(sm2y==0){
								sheet.addCell(new Label(7, y,"" ));
							}
							else{
								sheet.addCell(new Label(7, y,String.valueOf(sm2y) ));
							}
							if(sst==0){
								sheet.addCell(new Label(8, y,"" ));
							}
							else{
								sheet.addCell(new Label(8, y,String.valueOf(sst) ));
							}
							
						}
					}
				}
			}
			
			++y;
			sheet.mergeCells(0, y, 1, y);
			sheet.addCell(new Label(0, y, "合计",wcf));
			sheet.addCell(new Label(1, y, ""));
			
			if(tl3m==0){
				sheet.addCell(new Label(2, y,"" ));
			}
			else{
				sheet.addCell(new Label(2, y,String.valueOf(tl3m) ));
			}
			if(tt6m==0){
				sheet.addCell(new Label(3, y,"" ));
			}
			else{
				sheet.addCell(new Label(3, y,String.valueOf(tt6m) ));
			}
			if(tt9m==0){
				sheet.addCell(new Label(4, y,"" ));
			}
			else{
				sheet.addCell(new Label(4, y,String.valueOf(tt9m) ));
			}
			if(tt12m==0){
				sheet.addCell(new Label(5, y,"" ));
			}
			else{
				sheet.addCell(new Label(5, y,String.valueOf(tt12m) ));
			}
			if(tt2y==0){
				sheet.addCell(new Label(6, y,"" ));
			}
			else{
				sheet.addCell(new Label(6, y,String.valueOf(tt2y) ));
			}
			if(tm2y==0){
				sheet.addCell(new Label(7, y,"" ));
			}
			else{
				sheet.addCell(new Label(7, y,String.valueOf(tm2y) ));
			}
			if(tst==0){
				sheet.addCell(new Label(8, y,"" ));
			}
			else{
				sheet.addCell(new Label(8, y,String.valueOf(tst) ));
			}
			
			
		
			
			
			workbook.write();
			workbook.close();
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"产品库存状态");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
