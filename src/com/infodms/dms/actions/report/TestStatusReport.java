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

import com.infodms.dms.bean.AcUserCountBean;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.report.TestStatusDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class TestStatusReport {
	private Logger logger = Logger.getLogger(TestStatusReport.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act .getResponse();
	private TestStatusDao dao = TestStatusDao.getInstance();
	private AclUserBean logonUser = null;
	
	private final String TestStatus_URL = "/jsp/report/totalReport/TestStatusReport.jsp";
	
	public void getTestStatusReport(){
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
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
			
			map.put("modelId", modelId);
			map.put("areaId", areaId);
			map.put("orgId", orgId);
			map.put("modelId", modelId);
			map.put("dealerId", dealerId);
			act.setOutData("map", map);
			List<Map<String, Object>>  list_TestStatus = dao.getTestStatusDaoSelect(map);
			
			int len = list_TestStatus.size();
			int deing_amount = 0;
			int region_amount = 0;
			for (int i = 0; i < len; i++) {
				Object amount1 = list_TestStatus.get(i).get("DEING_AMOUNT");
				int amounts1 = Integer.parseInt(amount1.toString());
				deing_amount +=amounts1;
			}
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);

			
			
			act.setOutData("region_amount", region_amount);
			act.setOutData("deing_amount", deing_amount);
			act.setOutData("list_TestStatus", list_TestStatus);
			act.setForword(TestStatus_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证处理情况");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void TestStatusReportDownload(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			map.put("modelId", modelId);
			map.put("areaId", areaId);
			map.put("orgId", orgId);
			map.put("modelId", modelId);
			map.put("dealerId", dealerId);

			// 导出的文件名
			String fileName = "经销商库存汇总报表.csv";
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
			list.add("启票未发");
			list.add("发运在途");
			list.add("在库");
			list.add("合计");
			lists.add(list);
			
			
			List<Map<String, Object>> result = dao.getTestStatusDaoSelect(map);
			int len = result.size();
			for (int i = 0; i < len; i++) {
				Map<String, Object> record = result.get(i);
				list = new LinkedList<Object>();
				list.add(CommonUtils.checkNullEx(record.get("ORG_NAME")));
				list.add(CommonUtils.checkNullEx(record.get("REGION_NAME")));
				list.add(CommonUtils.checkNullEx(record.get("ROOT_DEALER_NAME")));
				list.add(CommonUtils.checkNullEx(record.get("DEALER_NAME")));
				list.add(CommonUtils.checkNullEx(record.get("NODE_BILL_AMOUNT")));
				list.add(CommonUtils.checkNullEx(record.get("DEING_AMOUNT")));
				list.add(CommonUtils.checkNullEx(record.get("STOCK_AMOUNT")));
				list.add(CommonUtils.checkNullEx(record.get("SUM_TOTAL")));
				lists.add(list);
			}
			OutputStream os = response.getOutputStream();
			CsvWriterUtil.writeCsv(lists, os);
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商库存汇总报表下载");
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
			
			map.put("modelId", modelId);
			map.put("areaId", areaId);
			map.put("orgId", orgId);
			map.put("modelId", modelId);
			map.put("dealerId", dealerId);

			// 导出的文件名
			String fileName = "经销商库存汇总报表.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("经销商库存汇总报表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			
			List<Map<String, Object>> list = dao.getTestStatusDaoSelect(map);
			int len = list.size();
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			
			int y =0;
			long tnba = 0;
			long tda=0;
			long tsa = 0;
			long tst = 0;
			
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);

			sheet.mergeCells(0, y, 7, y);
			sheet.addCell(new jxl.write.Label(0, y, "经销商库存汇总报表",wcf));
			
			++y;
			sheet.mergeCells(0, y, 7, y);
			sheet.addCell(new jxl.write.Label(0, y, "日期："+String.valueOf(year)+"年"+String.valueOf(month)+"月"+String.valueOf(day)+"日"));
			
			
			
			++y;
			sheet.addCell(new Label(0, y, "大区"));
			sheet.addCell(new Label(1, y, "省份"));
			sheet.addCell(new Label(2, y, "经销商"));
			sheet.addCell(new Label(3, y, "二级经销商"));
			sheet.addCell(new Label(4, y, "启票未发"));
			sheet.addCell(new Label(5, y, "发运在途"));
			sheet.addCell(new Label(6, y, "在库"));
			sheet.addCell(new Label(7, y, "合计"));
			
			if(list!=null){
				long snba = 0;
				long sda=0;
				long ssa = 0;
				long sst = 0;
				
				
				long snba_org = 0;
				long sda_org=0;
				long ssa_org = 0;
				long sst_org = 0;
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
						
						snba_org =nba;
						sda_org =da;
						ssa_org =sa;
						sst_org =st;
						++y;
						sheet.addCell(new Label(0, y, list.get(i).get("ORG_NAME")==null ? "":list.get(i).get("ORG_NAME").toString()));
						sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ? "":list.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new Label(2, y, list.get(i).get("ROOT_DEALER_NAME")==null ? "":list.get(i).get("ROOT_DEALER_NAME").toString()));
						sheet.addCell(new Label(3, y, list.get(i).get("DEALER_NAME")==null ? "":list.get(i).get("DEALER_NAME").toString()));
						sheet.addCell(new Label(4, y, Integer.parseInt(list.get(i).get("NODE_BILL_AMOUNT").toString())==0 ? "":list.get(i).get("NODE_BILL_AMOUNT").toString()));
						sheet.addCell(new Label(5, y, Integer.parseInt(list.get(i).get("DEING_AMOUNT").toString())==0 ? "":list.get(i).get("DEING_AMOUNT").toString()));
						sheet.addCell(new Label(6, y, Integer.parseInt(list.get(i).get("STOCK_AMOUNT").toString())==0 ? "":list.get(i).get("STOCK_AMOUNT").toString()));
						sheet.addCell(new Label(7, y, Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0 ? "":list.get(i).get("SUM_TOTAL").toString()));
					}else{
						String befor = list.get(i-1).get("ROOT_DEALER_NAME").toString();
						String after = list.get(i).get("ROOT_DEALER_NAME").toString();
						
						String befor_rn = list.get(i-1).get("ORG_NAME").toString();
						String after_rn = list.get(i).get("ORG_NAME").toString();
						if(after.equals(befor)&&after_rn.equals(befor_rn)){
							snba += nba;
							sda += da;
							ssa += sa;
							sst += st;
							
							tnba += nba;
							tda += da;
							tsa += sa;
							tst += st;
							
							snba_org +=nba;
							sda_org +=da;
							ssa_org +=sa;
							sst_org +=st;
							++y;
							sheet.addCell(new Label(0, y, list.get(i).get("ORG_NAME")==null ? "":list.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ? "":list.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new Label(2, y, list.get(i).get("ROOT_DEALER_NAME")==null ? "":list.get(i).get("ROOT_DEALER_NAME").toString()));
							sheet.addCell(new Label(3, y, list.get(i).get("DEALER_NAME")==null ? "":list.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new Label(4, y, Integer.parseInt(list.get(i).get("NODE_BILL_AMOUNT").toString())==0 ? "":list.get(i).get("NODE_BILL_AMOUNT").toString()));
							sheet.addCell(new Label(5, y, Integer.parseInt(list.get(i).get("DEING_AMOUNT").toString())==0 ? "":list.get(i).get("DEING_AMOUNT").toString()));
							sheet.addCell(new Label(6, y, Integer.parseInt(list.get(i).get("STOCK_AMOUNT").toString())==0 ? "":list.get(i).get("STOCK_AMOUNT").toString()));
							sheet.addCell(new Label(7, y, Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0 ? "":list.get(i).get("SUM_TOTAL").toString()));
						}else{
							++y;
							sheet.mergeCells(0, y, 3, y);
							sheet.addCell(new Label(0, y, "经销商合计",wcf));
							if(snba==0){
								sheet.addCell(new Label(4, y, ""));
							}
							else{
								sheet.addCell(new Label(4, y, String.valueOf(snba)));
							}
							if(sda==0){
								sheet.addCell(new Label(5, y, ""));
							}
							else{
								sheet.addCell(new Label(5, y, String.valueOf(sda)));
							}
							
							if(ssa==0){
								sheet.addCell(new Label(6, y, ""));
							}
							else{
								sheet.addCell(new Label(6, y, String.valueOf(ssa)));
							}
							
							if(sst==0){
								sheet.addCell(new Label(7, y, ""));
							}
							else{
								sheet.addCell(new Label(7, y, String.valueOf(sst)));
							}
							
							
							
							
							snba = nba;
							sda = da;
							ssa = sa;
							sst = st;
							
							tnba += nba;
							tda += da;
							tsa += sa;
							tst += st;
							
							
							//开始
							
							String before1 = list.get(i-1).get("ORG_NAME").toString();
							String after1 = list.get(i).get("ORG_NAME").toString();
							if(after1.equals(before1)){
								
								snba_org +=nba;
								sda_org +=da;
								ssa_org +=sa;
								sst_org +=st;
							}else{
								++y;
								sheet.mergeCells(0, y, 3, y);
								sheet.addCell(new Label(0, y, "大区合计",wcf));
								if(snba_org==0){
									sheet.addCell(new Label(4, y, ""));
								}
								else{
									sheet.addCell(new Label(4, y, String.valueOf(snba_org)));
								}
								
								if(sda_org==0){
									sheet.addCell(new Label(5, y, ""));
								}
								else{
									sheet.addCell(new Label(5, y, String.valueOf(sda_org)));
								}
								if(ssa_org==0){
									sheet.addCell(new Label(6, y, ""));
								}
								else{
									sheet.addCell(new Label(6, y, String.valueOf(ssa_org)));
								}
								if(sst_org==0){
									sheet.addCell(new Label(7, y, ""));
								}
								else{
									sheet.addCell(new Label(7, y, String.valueOf(sst_org)));
								}
							
							
							
								
								snba_org =0;
								sda_org =0;
								ssa_org =0;
								sst_org =0;
								
								snba_org +=nba;
								sda_org +=da;
								ssa_org +=sa;
								sst_org +=st;
							}
							//结束
							
							++y;
							sheet.addCell(new Label(0, y, list.get(i).get("ORG_NAME")==null ? "":list.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ? "":list.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new Label(2, y, list.get(i).get("ROOT_DEALER_NAME")==null ? "":list.get(i).get("ROOT_DEALER_NAME").toString()));
							sheet.addCell(new Label(3, y, list.get(i).get("DEALER_NAME")==null ? "":list.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new Label(4, y, Integer.parseInt(list.get(i).get("NODE_BILL_AMOUNT").toString())==0 ? "":list.get(i).get("NODE_BILL_AMOUNT").toString()));
							sheet.addCell(new Label(5, y, Integer.parseInt(list.get(i).get("DEING_AMOUNT").toString())==0 ? "":list.get(i).get("DEING_AMOUNT").toString()));
							sheet.addCell(new Label(6, y, Integer.parseInt(list.get(i).get("STOCK_AMOUNT").toString())==0 ? "":list.get(i).get("STOCK_AMOUNT").toString()));
							sheet.addCell(new Label(7, y, Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0 ? "":list.get(i).get("SUM_TOTAL").toString()));
						}
						if(i==len-1){
							++y;
							sheet.mergeCells(0, y, 3, y);
							sheet.addCell(new Label(0, y, "经销商合计",wcf));
							if(snba==0){
								sheet.addCell(new Label(4, y, ""));
							}
							else{
								sheet.addCell(new Label(4, y, String.valueOf(snba)));
							}
							if(sda==0){
								sheet.addCell(new Label(5, y, ""));
							}
							else{
								sheet.addCell(new Label(5, y, String.valueOf(sda)));
							}
							
							if(ssa==0){
								sheet.addCell(new Label(6, y, ""));
							}
							else{
								sheet.addCell(new Label(6, y, String.valueOf(ssa)));
							}
							
							if(sst==0){
								sheet.addCell(new Label(7, y, ""));
							}
							else{
								sheet.addCell(new Label(7, y, String.valueOf(sst)));
							}
							
							
							
							++y;
							sheet.mergeCells(0, y, 3, y);
							sheet.addCell(new Label(0, y, "大区合计",wcf));
							if(snba_org==0){
								sheet.addCell(new Label(4, y, ""));
							}
							else{
								sheet.addCell(new Label(4, y, String.valueOf(snba_org)));
							}
							
							if(sda_org==0){
								sheet.addCell(new Label(5, y, ""));
							}
							else{
								sheet.addCell(new Label(5, y, String.valueOf(sda_org)));
							}
							if(ssa_org==0){
								sheet.addCell(new Label(6, y, ""));
							}
							else{
								sheet.addCell(new Label(6, y, String.valueOf(ssa_org)));
							}
							if(sst_org==0){
								sheet.addCell(new Label(7, y, ""));
							}
							else{
								sheet.addCell(new Label(7, y, String.valueOf(sst_org)));
							}
						}
					}
				}
			}
			++y;
			sheet.mergeCells(0, y, 3, y);
			sheet.addCell(new Label(0, y, "合计",wcf));
			
			if(tnba==0){
				sheet.addCell(new Label(4, y, ""));
			}
			else{
				sheet.addCell(new Label(4, y, String.valueOf(tnba)));
			}
		
			if(tda==0){
				sheet.addCell(new Label(4, y, ""));
			}
			else{
				sheet.addCell(new Label(5, y, String.valueOf(tda)));
			}
			
			if(tsa==0){
				sheet.addCell(new Label(4, y, ""));
			}
			else{
				sheet.addCell(new Label(6, y, String.valueOf(tsa)));
			}
			
			if(tst==0){
				sheet.addCell(new Label(4, y, ""));
			}
			else{
				sheet.addCell(new Label(7, y, String.valueOf(tst)));
			}
			
			
			
			
			
			
			workbook.write();
			workbook.close();
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商库存汇总报表下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
