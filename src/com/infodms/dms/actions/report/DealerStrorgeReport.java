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
import com.infodms.dms.dao.report.DealerStrorgeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class DealerStrorgeReport {
	private Logger logger = Logger.getLogger(DealerStrorgeReport.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private DealerStrorgeDao dao = DealerStrorgeDao.getInstance();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private static final String DealerStrorge_URL = "/jsp/report/totalReport/dealerStrorge.jsp";
	
	public void getDealerStrorgeReport(){
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
			
			List<Map<String, Object>> list_DealerStrorge = dao.getDealerStrorgeSelect(map);
			
			int len = list_DealerStrorge.size();
			int sum_3 =0;
			int sum_6=0;
			int sum_9 =0;
			int sum_12 =0;
			int sum_1_2 = 0;
			int sum_2 =0;
			int sum = 0;
			for (int i = 0; i < len; i++) {
				Object sum1 = list_DealerStrorge.get(i).get("LESS_3_MONTH");
				int sum1s = Integer.parseInt(sum1.toString());
				sum_3 +=sum1s;
				
				Object sum2 = list_DealerStrorge.get(i).get("TO_6_MONTH");
				int sum2s = Integer.parseInt(sum2.toString());
				sum_6 +=sum2s;
				
				Object sum3 = list_DealerStrorge.get(i).get("TO_9_MONTH");
				int sum3s = Integer.parseInt(sum3.toString());
				sum_9 +=sum3s;
				
				Object sum4 = list_DealerStrorge.get(i).get("TO_12_MONTH");
				int sum4s = Integer.parseInt(sum4.toString());
				sum_12 +=sum4s;
				
				Object sum5 = list_DealerStrorge.get(i).get("TO_2_YEAR");
				int sum5s = Integer.parseInt(sum5.toString());
				sum_1_2 +=sum5s;
				
				Object sum6 = list_DealerStrorge.get(i).get("MORE_2_YEAR");
				int sum6s = Integer.parseInt(sum6.toString());
				sum_2 +=sum6s;
				
			}
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			
			act.setOutData("sum_3", sum_3);
			act.setOutData("sum_6", sum_6);
			act.setOutData("sum_9", sum_9);
			act.setOutData("sum_12", sum_12);
			act.setOutData("sum_1_2", sum_1_2);
			act.setOutData("sum_2", sum_2);
			act.setOutData("sum", sum);
			act.setOutData("list_DealerStrorge", list_DealerStrorge);
			act.setForword(DealerStrorge_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商库龄查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void DealerStrorgeReportDownload(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			
			map.put("dealerId", dealerId);
			map.put("orgId", orgId);
			map.put("modelId", modelId);
			
			// 导出的文件名
			String fileName = "经销商库龄查询.csv";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			List<List<Object>> lists = new LinkedList<List<Object>>();
			List<Object> list = new LinkedList<Object>();
			list.add("大区");
			list.add("省份");
			list.add("经销商");
			list.add("二级经销商");
			list.add("三个月一下");
			list.add("3-6个月");
			list.add("6-9个月");
			list.add("9-12个月");
			list.add("1-2年");
			list.add("2年以上");
			list.add("合计");
			lists.add(list);
			
			List<Map<String, Object>> reuslt = dao.getDealerStrorgeSelect(map);
			int len = reuslt.size();
			for (int i = 0; i < len; i++) {
				Map<String, Object> record = reuslt.get(i);
				list = new LinkedList<Object>();
				list.add(CommonUtils.checkNull(record.get("ROOT_ORG_NAME")));
				list.add(CommonUtils.checkNull(record.get("REGION_NAME")));
				list.add(CommonUtils.checkNull(record.get("ROOT_DEALER_NAME")));
				list.add(CommonUtils.checkNull(record.get("DEALER_NAME")));
				list.add(CommonUtils.checkNull(record.get("LESS_3_MONTH")));
				list.add(CommonUtils.checkNull(record.get("TO_6_MONTH")));
				list.add(CommonUtils.checkNull(record.get("TO_9_MONTH")));
				list.add(CommonUtils.checkNull(record.get("TO_12_MONTH")));
				list.add(CommonUtils.checkNull(record.get("TO_2_YEAR")));
				list.add(CommonUtils.checkNull(record.get("MORE_2_YEAR")));
				list.add(CommonUtils.checkNull(record.get("SUM_TOTAL")));
				lists.add(list);
			}
			
			
			//添加合计这一栏
				
			int sum_3 =0;
			int sum_6=0;
			int sum_9 =0;
			int sum_12 =0;
			int sum_1_2 = 0;
			int sum_2 =0;
			int sum = 0;
			for (int i = 0; i < len; i++) {
				Object sum1 = reuslt.get(i).get("LESS_3_MONTH");
				int sum1s = Integer.parseInt(sum1.toString());
				sum_3 +=sum1s;
				
				Object sum2 = reuslt.get(i).get("TO_6_MONTH");
				int sum2s = Integer.parseInt(sum2.toString());
				sum_6 +=sum2s;
				
				Object sum3 = reuslt.get(i).get("TO_9_MONTH");
				int sum3s = Integer.parseInt(sum3.toString());
				sum_9 +=sum3s;
				
				Object sum4 = reuslt.get(i).get("TO_12_MONTH");
				int sum4s = Integer.parseInt(sum4.toString());
				sum_12 +=sum4s;
				
				Object sum5 = reuslt.get(i).get("TO_2_YEAR");
				int sum5s = Integer.parseInt(sum5.toString());
				sum_1_2 +=sum5s;
				
				Object sum6 = reuslt.get(i).get("MORE_2_YEAR");
				int sum6s = Integer.parseInt(sum6.toString());
				sum_2 +=sum6s;
				
				Object sum7 = reuslt.get(i).get("SUM_TOTAL");
				int sum7s = Integer.parseInt(sum7.toString());
				sum +=sum7s;
				
			}
			
			list = new LinkedList<Object>();
			list.add("合计");
			list.add("");
			list.add("");
			list.add("");
			list.add(sum_3);
			list.add(sum_6);
			list.add(sum_9);
			list.add(sum_12);
			list.add(sum_1_2);
			list.add(sum_2);
			list.add(sum);
			lists.add(list);
			//添加合计这一栏到此结束
			
			OutputStream os = response.getOutputStream();
			CsvWriterUtil.writeCsv(lists, os);
			
			os.flush();
			os.close();
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商库龄查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void download(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			
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
			
			map.put("dealerId", dealerId);
			map.put("orgId", orgId);
			map.put("modelId", modelId);
			map.put("areaId", areaId);
			

			// 导出的文件名
			String fileName = "经销商库龄查询.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
				
				OutputStream os = response.getOutputStream();
				WritableWorkbook workbook = Workbook.createWorkbook(os);
				WritableSheet sheet = workbook.createSheet("经销商库龄查询", 0);
				WritableCellFormat wcf = new WritableCellFormat();
				wcf.setAlignment(Alignment.CENTRE);
				
				List<Map<String, Object>> list = dao.getDealerStrorgeSelect(map);
				int y = 0 ;
				int len = list.size();
				
				Calendar  calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH)+1;
				int day =calendar.get(Calendar.DAY_OF_MONTH);
				
				sheet.mergeCells(0, y, 10, y);
				sheet.addCell(new jxl.write.Label(0, y, "经销商库龄查询",wcf));
				
				++y;
				sheet.mergeCells(0, y, 10, y);
				sheet.addCell(new jxl.write.Label(0, y, "日期："+String.valueOf(year)+"年"+String.valueOf(month)+"月"+String.valueOf(day)+"日"));
				
				++y;
				
				sheet.addCell(new Label(0, y, "大区"));
				sheet.addCell(new Label(1, y, "省份"));
				sheet.addCell(new Label(2, y, "经销商"));
				sheet.addCell(new Label(3, y, "二级经销商"));
				sheet.addCell(new Label(4, y, "三个月一下"));
				sheet.addCell(new Label(5, y, "3-6个月"));
				sheet.addCell(new Label(6, y, "6-9个月"));
				sheet.addCell(new Label(7, y, "9-12个月"));
				sheet.addCell(new Label(8, y, "1-2年"));
				sheet.addCell(new Label(9, y, "2年以上"));
				sheet.addCell(new Label(10, y, "合计"));
				
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
					
					long sl3m_ron = 0 ;
					long st6m_ron = 0 ;
					long st9m_ron = 0 ;
					long st12m_ron = 0 ;
					long st2y_ron = 0 ;
					long sm2y_ron = 0 ;
					long sst_ron = 0 ;
					for (int i = 0; i < len; i++) {
						long l3m = Long.parseLong(list.get(i).get("LESS_3_MONTH").toString());
						long t6m = Long.parseLong(list.get(i).get("TO_6_MONTH").toString());
						long t9m = Long.parseLong(list.get(i).get("TO_9_MONTH").toString());
						long t12m = Long.parseLong(list.get(i).get("TO_12_MONTH").toString());
						long t2y = Long.parseLong(list.get(i).get("TO_2_YEAR").toString());
						long m2y = Long.parseLong(list.get(i).get("MORE_2_YEAR").toString());
						long st = Long.parseLong(list.get(i).get("SUM_TOTAL").toString());
						if(i==0){
							sl3m = l3m;
							st6m = t6m;
							st9m = t9m;
							st12m = t12m;
							st2y = t2y;
							sm2y = m2y;
							sst = st;
							
							tl3m = l3m;
							tt6m = t6m;
							tt9m = t9m;
							tt12m = t12m;
							tt2y = t2y;
							tm2y = m2y;
							tst = st;
							
							sl3m_ron = l3m;
							st6m_ron = t6m;
							st9m_ron = t9m;
							st12m_ron = t12m;
							st2y_ron = t2y;
							sm2y_ron = m2y;
							sst_ron = st;
							++y;
							sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")==null ?"":list.get(i).get("ROOT_ORG_NAME").toString()));
							sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ?"":list.get(i).get

	("REGION_NAME").toString()));
							sheet.addCell(new Label(2, y, list.get(i).get("ROOT_DEALER_NAME")==null ?"":list.get(i).get

	("ROOT_DEALER_NAME").toString()));
							sheet.addCell(new Label(3, y, list.get(i).get("DEALER_NAME")==null ?"":list.get(i).get

	("DEALER_NAME").toString()));
							sheet.addCell(new Label(4, y, Integer.parseInt(list.get(i).get("LESS_3_MONTH").toString())==0 ?"":list.get(i).get

									("LESS_3_MONTH").toString()));
							sheet.addCell(new Label(5, y, Integer.parseInt(list.get(i).get("TO_6_MONTH").toString())==0 ?"":list.get(i).get
									
	("TO_6_MONTH").toString()));
							sheet.addCell(new Label(6, y, Integer.parseInt(list.get(i).get("TO_9_MONTH").toString())==0 ?"":list.get(i).get
									
	("TO_9_MONTH").toString()));
							sheet.addCell(new Label(7, y, Integer.parseInt(list.get(i).get("TO_12_MONTH").toString())==0 ?"":list.get(i).get
									
	("TO_12_MONTH").toString()));
							sheet.addCell(new Label(8, y,Integer.parseInt(list.get(i).get("TO_2_YEAR").toString())==0 ?"":list.get(i).get
									
	("TO_2_YEAR").toString()));
							sheet.addCell(new Label(9, y, Integer.parseInt(list.get(i).get("MORE_2_YEAR").toString())==0 ?"":list.get(i).get
									
									("MORE_2_YEAR").toString()));
							sheet.addCell(new Label(10, y,Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0 ?"":list.get(i).get
									
									("SUM_TOTAL").toString()));
							
						}else{
							String befor_regionName = list.get(i-1).get("REGION_NAME").toString();
							String after_regionName = list.get(i).get("REGION_NAME").toString();
							
							
							String befor_ron = list.get(i-1).get("ROOT_ORG_NAME").toString();
							String after_ron = list.get(i).get("ROOT_ORG_NAME").toString();
							if(after_regionName.equals(befor_regionName)&&after_ron.equals(befor_ron)){
								sl3m += l3m;
								st6m += t6m;
								st9m += t9m;
								st12m += t12m;
								st2y += t2y;
								sm2y += m2y;
								sst += st;
								
								tl3m += l3m;
								tt6m += t6m;
								tt9m += t9m;
								tt12m += t12m;
								tt2y += t2y;
								tm2y += m2y;
								tst += st;
								
								
								sl3m_ron += l3m;
								st6m_ron += t6m;
								st9m_ron += t9m;
								st12m_ron += t12m;
								st2y_ron += t2y;
								sm2y_ron += m2y;
								sst_ron += st;
								++y;
								sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")==null ?"":list.get(i).get("ROOT_ORG_NAME").toString()));
								sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ?"":list.get(i).get

		("REGION_NAME").toString()));
								sheet.addCell(new Label(2, y, list.get(i).get("ROOT_DEALER_NAME")==null ?"":list.get(i).get

		("ROOT_DEALER_NAME").toString()));
								sheet.addCell(new Label(3, y, list.get(i).get("DEALER_NAME")==null ?"":list.get(i).get

		("DEALER_NAME").toString()));
								sheet.addCell(new Label(4, y, Integer.parseInt(list.get(i).get("LESS_3_MONTH").toString())==0 ?"":list.get(i).get

										("LESS_3_MONTH").toString()));
								sheet.addCell(new Label(5, y, Integer.parseInt(list.get(i).get("TO_6_MONTH").toString())==0 ?"":list.get(i).get
										
		("TO_6_MONTH").toString()));
								sheet.addCell(new Label(6, y, Integer.parseInt(list.get(i).get("TO_9_MONTH").toString())==0 ?"":list.get(i).get
										
		("TO_9_MONTH").toString()));
								sheet.addCell(new Label(7, y, Integer.parseInt(list.get(i).get("TO_12_MONTH").toString())==0 ?"":list.get(i).get
										
		("TO_12_MONTH").toString()));
								sheet.addCell(new Label(8, y,Integer.parseInt(list.get(i).get("TO_2_YEAR").toString())==0 ?"":list.get(i).get
										
		("TO_2_YEAR").toString()));
								sheet.addCell(new Label(9, y, Integer.parseInt(list.get(i).get("MORE_2_YEAR").toString())==0 ?"":list.get(i).get
										
										("MORE_2_YEAR").toString()));
								sheet.addCell(new Label(10, y,Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0 ?"":list.get(i).get
										
										("SUM_TOTAL").toString()));
							}else{
								++y;
								sheet.mergeCells(0, y, 3, y);
								sheet.addCell(new Label(0, y,"省份合计" ,wcf));
								if(sl3m==0){
									sheet.addCell(new Label(4, y, ""));
								}
								else{
									sheet.addCell(new Label(4, y, String.valueOf(sl3m)));
								}
								if(st6m==0){
									sheet.addCell(new Label(5, y, ""));
								}
								else{
									sheet.addCell(new Label(5, y, String.valueOf(st6m)));
								}
								if(st9m==0){
									sheet.addCell(new Label(6, y, ""));
								}
								else{
									sheet.addCell(new Label(6, y, String.valueOf(st9m)));
								}
								if(st12m==0){
									sheet.addCell(new Label(7, y, ""));
								}
								else{
									sheet.addCell(new Label(7, y, String.valueOf(st12m)));
								}
								if(st2y==0){
									sheet.addCell(new Label(8, y, ""));
								}
								else{
									sheet.addCell(new Label(8, y, String.valueOf(st2y)));
								}
								if(sm2y==0){
									sheet.addCell(new Label(9, y, ""));
								}
								else{
									sheet.addCell(new Label(9, y, String.valueOf(sm2y)));
								}
								if(sst==0){
									sheet.addCell(new Label(10, y, ""));
								}
								else{
									sheet.addCell(new Label(10, y, String.valueOf(sst)));
								}
							
								
								sl3m = l3m;
								st6m = t6m;
								st9m = t9m;
								st12m = t12m;
								st2y = t2y;
								sm2y = m2y;
								sst = st;
								
								tl3m += l3m;
								tt6m += t6m;
								tt9m += t9m;
								tt12m += t12m;
								tt2y += t2y;
								tm2y += m2y;
								tst += st;
								
								//开始
								String befor_regionName1 = list.get(i-1).get("ROOT_ORG_NAME").toString();
								String after_regionName1 = list.get(i).get("ROOT_ORG_NAME").toString();
								
								if(after_regionName1.equals(befor_regionName1)){
									sl3m_ron += l3m;
									st6m_ron += t6m;
									st9m_ron += t9m;
									st12m_ron += t12m;
									st2y_ron += t2y;
									sm2y_ron += m2y;
									sst_ron += st;
								}else{
									++y;
									sheet.mergeCells(0, y, 3, y);
									sheet.addCell(new Label(0, y,"大区合计",wcf ));
									if(sl3m_ron==0){
										sheet.addCell(new Label(4, y, ""));
									}
									else{
										sheet.addCell(new Label(4, y, String.valueOf(sl3m_ron)));
									}
									if(st6m_ron==0){
										sheet.addCell(new Label(5, y, ""));
									}
									else{
										sheet.addCell(new Label(5, y, String.valueOf(st6m_ron)));
									}
									if(st9m_ron==0){
										sheet.addCell(new Label(6, y, ""));
									}
									else{
										sheet.addCell(new Label(6, y, String.valueOf(st9m_ron)));
									}
									if(st12m_ron==0){
										sheet.addCell(new Label(7, y, ""));
									}
									else{
										sheet.addCell(new Label(7, y, String.valueOf(st12m_ron)));
									}
									if(st2y_ron==0){
										sheet.addCell(new Label(8, y, ""));
									}
									else{
										sheet.addCell(new Label(8, y, String.valueOf(st2y_ron)));
									}
									if(sm2y_ron==0){
										sheet.addCell(new Label(9, y, ""));
									}
									else{
										sheet.addCell(new Label(9, y, String.valueOf(sm2y_ron)));
									}
									if(sst_ron==0){
										sheet.addCell(new Label(10, y, ""));
									}
									else{
										sheet.addCell(new Label(10, y, String.valueOf(sst_ron)));
									}
								
									
									sl3m_ron = 0;
									st6m_ron = 0;
									st9m_ron = 0;
									st12m_ron = 0;
									st2y_ron = 0;
									sm2y_ron = 0;
									sst_ron = 0;
									
									sl3m_ron += l3m;
									st6m_ron += t6m;
									st9m_ron += t9m;
									st12m_ron += t12m;
									st2y_ron += t2y;
									sm2y_ron += m2y;
									sst_ron += st;
								}
								//结束
								
								
								
								++y;
								sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")==null ?"":list.get(i).get("ROOT_ORG_NAME").toString()));
								sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ?"":list.get(i).get

		("REGION_NAME").toString()));
								sheet.addCell(new Label(2, y, list.get(i).get("ROOT_DEALER_NAME")==null ?"":list.get(i).get

		("ROOT_DEALER_NAME").toString()));
								sheet.addCell(new Label(3, y, list.get(i).get("DEALER_NAME")==null ?"":list.get(i).get

		("DEALER_NAME").toString()));
								sheet.addCell(new Label(4, y, Integer.parseInt(list.get(i).get("LESS_3_MONTH").toString())==0 ?"":list.get(i).get

										("LESS_3_MONTH").toString()));
								sheet.addCell(new Label(5, y, Integer.parseInt(list.get(i).get("TO_6_MONTH").toString())==0 ?"":list.get(i).get
										
		("TO_6_MONTH").toString()));
								sheet.addCell(new Label(6, y, Integer.parseInt(list.get(i).get("TO_9_MONTH").toString())==0 ?"":list.get(i).get
										
		("TO_9_MONTH").toString()));
								sheet.addCell(new Label(7, y, Integer.parseInt(list.get(i).get("TO_12_MONTH").toString())==0 ?"":list.get(i).get
										
		("TO_12_MONTH").toString()));
								sheet.addCell(new Label(8, y,Integer.parseInt(list.get(i).get("TO_2_YEAR").toString())==0 ?"":list.get(i).get
										
		("TO_2_YEAR").toString()));
								sheet.addCell(new Label(9, y, Integer.parseInt(list.get(i).get("MORE_2_YEAR").toString())==0 ?"":list.get(i).get
										
										("MORE_2_YEAR").toString()));
	sheet.addCell(new Label(10, y,Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0 ?"":list.get(i).get
										
										("SUM_TOTAL").toString()));
							}
							if(i==len-1){
								++y;
								sheet.mergeCells(0, y, 3, y);
								sheet.addCell(new Label(0, y,"省份合计",wcf ));
								if(sl3m==0){
									sheet.addCell(new Label(4, y, ""));
								}
								else{
									sheet.addCell(new Label(4, y, String.valueOf(sl3m)));
								}
								if(st6m==0){
									sheet.addCell(new Label(5, y, ""));
								}
								else{
									sheet.addCell(new Label(5, y, String.valueOf(st6m)));
								}
								if(st9m==0){
									sheet.addCell(new Label(6, y, ""));
								}
								else{
									sheet.addCell(new Label(6, y, String.valueOf(st9m)));
								}
								if(st12m==0){
									sheet.addCell(new Label(7, y, ""));
								}
								else{
									sheet.addCell(new Label(7, y, String.valueOf(st12m)));
								}
								if(st2y==0){
									sheet.addCell(new Label(8, y, ""));
								}
								else{
									sheet.addCell(new Label(8, y, String.valueOf(st2y)));
								}
								if(sm2y==0){
									sheet.addCell(new Label(9, y, ""));
								}
								else{
									sheet.addCell(new Label(9, y, String.valueOf(sm2y)));
								}
								if(sst==0){
									sheet.addCell(new Label(10, y, ""));
								}
								else{
									sheet.addCell(new Label(10, y, String.valueOf(sst)));
								}
								
								++y;
								sheet.mergeCells(0, y, 3, y);
								sheet.addCell(new Label(0, y,"大区合计",wcf ));
								if(sl3m_ron==0){
									sheet.addCell(new Label(4, y, ""));
								}
								else{
									sheet.addCell(new Label(4, y, String.valueOf(sl3m_ron)));
								}
								if(st6m_ron==0){
									sheet.addCell(new Label(5, y, ""));
								}
								else{
									sheet.addCell(new Label(5, y, String.valueOf(st6m_ron)));
								}
								if(st9m_ron==0){
									sheet.addCell(new Label(6, y, ""));
								}
								else{
									sheet.addCell(new Label(6, y, String.valueOf(st9m_ron)));
								}
								if(st12m_ron==0){
									sheet.addCell(new Label(7, y, ""));
								}
								else{
									sheet.addCell(new Label(7, y, String.valueOf(st12m_ron)));
								}
								if(st2y_ron==0){
									sheet.addCell(new Label(8, y, ""));
								}
								else{
									sheet.addCell(new Label(8, y, String.valueOf(st2y_ron)));
								}
								if(sm2y_ron==0){
									sheet.addCell(new Label(9, y, ""));
								}
								else{
									sheet.addCell(new Label(9, y, String.valueOf(sm2y_ron)));
								}
								if(sst_ron==0){
									sheet.addCell(new Label(10, y, ""));
								}
								else{
									sheet.addCell(new Label(10, y, String.valueOf(sst_ron)));
								}
							
							}
						}
					}
				}
					++y;
					sheet.mergeCells(0, y, 3, y);
					sheet.addCell(new Label(0, y,"合计" ,wcf));
					if(tl3m==0){
						sheet.addCell(new Label(4, y, ""));
					}
					else{
						sheet.addCell(new Label(4, y, String.valueOf(tl3m)));
					}
					if(tt6m==0){
						sheet.addCell(new Label(5, y, ""));
					}
					else{
						sheet.addCell(new Label(5, y, String.valueOf(tt6m)));
					}
					if(tt9m==0){
						sheet.addCell(new Label(6, y, ""));
					}
					else{
						sheet.addCell(new Label(6, y, String.valueOf(tt9m)));
					}
					if(tt12m==0){
						sheet.addCell(new Label(7, y, ""));
					}
					else{
						sheet.addCell(new Label(7, y, String.valueOf(tt12m)));
					}
					if(tt2y==0){
						sheet.addCell(new Label(8, y, ""));
					}
					else{
						sheet.addCell(new Label(8, y, String.valueOf(tt2y)));
					}
					if(tm2y==0){
						sheet.addCell(new Label(9, y, ""));
					}
					else{
						sheet.addCell(new Label(9, y, String.valueOf(tm2y)));
					}
					if(tst==0){
						sheet.addCell(new Label(10, y, ""));
					}
					else{
						sheet.addCell(new Label(10, y, String.valueOf(tst)));
					}
				
					
					
						
						workbook.write();
						workbook.close();
						os.flush();
						os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商库龄查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
