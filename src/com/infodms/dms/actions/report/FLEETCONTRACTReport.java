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
import com.infodms.dms.dao.report.FLEETCONTRACTDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class FLEETCONTRACTReport {
	private Logger logger = Logger.getLogger(FLEETCONTRACTReport.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private FLEETCONTRACTDao dao = FLEETCONTRACTDao.getInstance();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER); 
	
	private static final String  FLEETCONTRACTR_URL = "/jsp/report/totalReport/FLEETCONTRACT.jsp";
	
	public void getFLEETCONTRACTReport(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER); 
		try {
			Map<String, String> map = new HashMap<String, String>();
			String checkDate1 = CommonUtils.checkNull(request.getParamValue("checkDate1"));
			String checkDate2 = CommonUtils.checkNull(request.getParamValue("checkDate2"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			
			map.put("checkDate1", checkDate1);
			map.put("checkDate2", checkDate2);
			map.put("orgId", orgId);
			
			List<Map<String, Object>>  list_FLEETCONTRACT = dao.getFLEETCONTRACTDaoSelect(map);
			act.setOutData("list_FLEETCONTRACT", list_FLEETCONTRACT);
			int len = list_FLEETCONTRACT.size();
			
			int intent_count = 0;
			int norm_amount  = 0;
			int count_amount = 0;
			int other_amount = 0;
			int dis_amount = 0;
			int intent_point = 0;
			for (int i = 0; i < len; i++) {
				
				
				Object sum_intent = list_FLEETCONTRACT.get(i).get("INTENT_COUNT");
				Double sum_intents = Double.parseDouble(sum_intent.toString());
				intent_count +=sum_intents;
				
				Object sum_point = list_FLEETCONTRACT.get(i).get("INTENT_POINT");
				Double sum_points = Double.parseDouble(sum_point.toString());
				intent_point +=sum_points;
				
				Object sum_norm = list_FLEETCONTRACT.get(i).get("NORM_AMOUNT");
				Double sum_norms = Double.parseDouble(sum_norm.toString());
				norm_amount +=sum_norms;
				
				Object sum_count = list_FLEETCONTRACT.get(i).get("COUNT_AMOUNT");
				Double sum_counts = Double.parseDouble(sum_count.toString());
				count_amount +=sum_counts;
				
				Object sum_other = list_FLEETCONTRACT.get(i).get("OTHER_AMOUNT");
				Double sum_others = Double.parseDouble(sum_other.toString());
				other_amount +=sum_others;
				
				Object sum_dis = list_FLEETCONTRACT.get(i).get("DIS_AMOUNT");
				Double sum_diss = Double.parseDouble(sum_dis.toString());
				dis_amount +=sum_diss;
			}
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			
			act.setOutData("checkDate1", checkDate1);
			act.setOutData("checkDate2", checkDate2);
			act.setOutData("intent_count", intent_count);//数量
			act.setOutData("norm_amount", norm_amount);//标准价格
			act.setOutData("count_amount", count_amount);//合计金额
			act.setOutData("other_amount", other_amount);//特殊金额
			act.setOutData("dis_amount", dis_amount);//折让总额
			act.setOutData("intent_point", intent_point);
			
			act.setForword(FLEETCONTRACTR_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void FLEETCONTRACTReportDownload(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER); 
		try {
			Map<String, String> map = new HashMap<String, String>();
			String checkDate1 = CommonUtils.checkNull(request.getParamValue("checkDate1"));
			String checkDate2 = CommonUtils.checkNull(request.getParamValue("checkDate2"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			
			map.put("checkDate1", checkDate1);
			map.put("checkDate2", checkDate2);
			map.put("orgId", orgId);
			
			// 导出的文件名
			String fileName = "集团客户合同.csv";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			
			List<List<Object>> lists = new LinkedList<List<Object>>();
			List<Object> list = new LinkedList<Object>();
			
			list.add("组织");
			list.add("省");
			list.add("提报单位");
			list.add("提报日期");
			list.add("客户名称");
			list.add("客户类型");
			list.add("合同编号");
			list.add("买方");
			list.add("卖方");
			list.add("签约日期");
			list.add("有效期起");
			list.add("有效期止");
			list.add("签约车系");
			list.add("数量");
			list.add("标准价");
			list.add("合计金额");
			list.add("折扣点");
			list.add("特殊金额");
			list.add("折让总额");
			list.add("特殊需求");
			list.add("审核日期");
			lists.add(list);
			
			List<Map<String, Object>> result = dao.getFLEETCONTRACTDaoSelect(map);
			int len = result.size();
			for (int i = 0; i < len; i++) {
				Map<String, Object> record = result.get(i);
				list = new LinkedList<Object>();
				list.add(CommonUtils.checkNull(record.get("ORG_NAME")));
				list.add(CommonUtils.checkNull(record.get("REGION_NAME")));
				list.add(CommonUtils.checkNull(record.get("COMPANY_SHORTNAME")));
				list.add(CommonUtils.checkNull(record.get("SUBMIT_DATE")));
				list.add(CommonUtils.checkNull(record.get("FLEET_NAME")));
				list.add(CommonUtils.checkNull(record.get("FLEET_TYPE")));
				list.add(CommonUtils.checkNull(record.get("CONTRACT_NO")));
				list.add(CommonUtils.checkNull(record.get("BUY_FROM")));
				list.add(CommonUtils.checkNull(record.get("SELL_TO")));
				list.add(CommonUtils.checkNull(record.get("CHECK_DATE")));
				list.add(CommonUtils.checkNull(record.get("START_DATE")));
				list.add(CommonUtils.checkNull(record.get("END_DATE")));
				list.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
				list.add(CommonUtils.checkNull(record.get("INTENT_COUNT")));
				list.add(CommonUtils.checkNull(record.get("NORM_AMOUNT")));
				list.add(CommonUtils.checkNull(record.get("COUNT_AMOUNT")));
				list.add(CommonUtils.checkNull(record.get("INTENT_POINT")));
				list.add(CommonUtils.checkNull(record.get("OTHER_AMOUNT")));
				list.add(CommonUtils.checkNull(record.get("DIS_AMOUNT")));
				list.add(CommonUtils.checkNull(record.get("OTHER_REMARK")));
				list.add(CommonUtils.checkNull(record.get("AUDIT_DATE")));
				lists.add(list);
			}
			OutputStream os = response.getOutputStream();
			CsvWriterUtil.writeCsv(lists, os);
			
			os.flush();
			os.close();
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void download(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER); 
		try {
			Map<String, String> map = new HashMap<String, String>();
			String checkDate1 = CommonUtils.checkNull(request.getParamValue("checkDate1"));
			String checkDate2 = CommonUtils.checkNull(request.getParamValue("checkDate2"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			
			act.setOutData("checkDate1", checkDate1);
			act.setOutData("checkDate2", checkDate2);
			
			map.put("checkDate1", checkDate1);
			map.put("checkDate2", checkDate2);
			map.put("orgId", orgId);
			
			// 导出的文件名
			String fileName = "集团客户合同.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			List<Map<String, Object>> list = dao.getFLEETCONTRACTDaoSelect(map);
			int len = list.size();
			int y = 0;
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("集团客户合同", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			sheet.mergeCells(0, y, 20, y);
			sheet.addCell(new Label(0, y, "集团客户合同",wcf));
			
			++y;
			sheet.mergeCells(0, y, 20, y);
			sheet.addCell(new Label(0, y, "审核日期："+checkDate1+"--"+checkDate2));
			
			++y;
			
			sheet.addCell(new Label(0, y, "组织"));
			sheet.addCell(new Label(1, y, "省"));
			sheet.addCell(new Label(2, y, "提报单位"));
			sheet.addCell(new Label(3, y, "提报日期"));
			sheet.addCell(new Label(4, y, "客户名称"));
			sheet.addCell(new Label(5, y, "客户类型"));
			sheet.addCell(new Label(6, y, "合同编号"));
			sheet.addCell(new Label(7, y, "买方"));
			sheet.addCell(new Label(8, y, "卖方"));
			sheet.addCell(new Label(9, y, "签约日期"));
			sheet.addCell(new Label(10, y, "有效日期起"));
			sheet.addCell(new Label(11, y, "有效日期止"));
			sheet.addCell(new Label(12, y, "签约车系"));
			sheet.addCell(new Label(13, y, "数量"));
			sheet.addCell(new Label(14, y, "标准价"));
			sheet.addCell(new Label(15, y, "合计金额"));
			sheet.addCell(new Label(16, y, "折扣点"));
			sheet.addCell(new Label(17, y, "特殊金额"));
			sheet.addCell(new Label(18, y, "折让总额"));
			sheet.addCell(new Label(19, y, "特殊需求"));
			sheet.addCell(new Label(20, y, "审核日期"));
			for (int i = 0; i < len; i++) {
				++y;
				sheet.addCell(new Label(0, y, list.get(i).get("ORG_NAME")==null ? "":list.get(i).get("ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ? "":list.get(i).get("REGION_NAME").toString()));
				sheet.addCell(new Label(2, y, list.get(i).get("COMPANY_SHORTNAME")==null ? "":list.get(i).get("COMPANY_SHORTNAME").toString()));
				sheet.addCell(new Label(3, y, list.get(i).get("SUBMIT_DATE")==null ? "":list.get(i).get("SUBMIT_DATE").toString()));
				sheet.addCell(new Label(4, y, list.get(i).get("FLEET_NAME")==null ? "":list.get(i).get("FLEET_NAME").toString()));
				sheet.addCell(new Label(5, y, list.get(i).get("FLEET_TYPE")==null ? "":list.get(i).get("FLEET_TYPE").toString()));
				sheet.addCell(new Label(6, y, list.get(i).get("CONTRACT_NO")==null ? "":list.get(i).get("CONTRACT_NO").toString()));
				sheet.addCell(new Label(7, y, list.get(i).get("BUY_FROM")==null ? "":list.get(i).get("BUY_FROM").toString()));
				sheet.addCell(new Label(8, y, list.get(i).get("SELL_TO")==null ? "":list.get(i).get("SELL_TO").toString()));
				sheet.addCell(new Label(9, y, list.get(i).get("CHECK_DATE")==null ? "":list.get(i).get("CHECK_DATE").toString()));
				sheet.addCell(new Label(10, y, list.get(i).get("START_DATE")==null ? "":list.get(i).get("START_DATE").toString()));
				sheet.addCell(new Label(11, y, list.get(i).get("END_DATE")==null ? "":list.get(i).get("END_DATE").toString()));
				sheet.addCell(new Label(12, y, list.get(i).get("GROUP_NAME")==null ? "":list.get(i).get("GROUP_NAME").toString()));
				sheet.addCell(new Label(13, y, list.get(i).get("INTENT_COUNT")==null ? "":list.get(i).get("INTENT_COUNT").toString()));
				sheet.addCell(new Label(14, y, Double.parseDouble(list.get(i).get("NORM_AMOUNT").toString())==0 ? "":list.get(i).get("NORM_AMOUNT").toString()));
				sheet.addCell(new Label(15, y, Double.parseDouble(list.get(i).get("COUNT_AMOUNT").toString())==0 ? "":list.get(i).get("COUNT_AMOUNT").toString()));
				sheet.addCell(new Label(16, y, Double.parseDouble(list.get(i).get("INTENT_POINT").toString())==0? "":list.get(i).get("INTENT_POINT").toString()));
				sheet.addCell(new Label(17, y, Double.parseDouble(list.get(i).get("OTHER_AMOUNT").toString())==0 ? "":list.get(i).get("OTHER_AMOUNT").toString()));
				sheet.addCell(new Label(18, y,Double.parseDouble(list.get(i).get("DIS_AMOUNT").toString())==0 ? "":list.get(i).get("DIS_AMOUNT").toString()));
				sheet.addCell(new Label(19, y, list.get(i).get("OTHER_REMARK")==null ? "":list.get(i).get("OTHER_REMARK").toString()));
				sheet.addCell(new Label(20, y, list.get(i).get("AUDIT_DATE")==null ? "":list.get(i).get("AUDIT_DATE").toString()));
			}
			
			
			int intent_count = 0;
			int norm_amount  = 0;
			int count_amount = 0;
			int other_amount = 0;
			int dis_amount = 0;
			int intent_point = 0;
			for (int i = 0; i < len; i++) {
				
				
				Object sum_intent = list.get(i).get("INTENT_COUNT");
				Double sum_intents = Double.parseDouble(sum_intent.toString());
				intent_count +=sum_intents;
				
				Object sum_point = list.get(i).get("INTENT_POINT");
				Double sum_points = Double.parseDouble(sum_point.toString());
				intent_point +=sum_points;
				
				Object sum_norm = list.get(i).get("NORM_AMOUNT");
				Double sum_norms = Double.parseDouble(sum_norm.toString());
				norm_amount +=sum_norms;
				
				Object sum_count = list.get(i).get("COUNT_AMOUNT");
				Double sum_counts = Double.parseDouble(sum_count.toString());
				count_amount +=sum_counts;
				
				Object sum_other = list.get(i).get("OTHER_AMOUNT");
				Double sum_others = Double.parseDouble(sum_other.toString());
				other_amount +=sum_others;
				
				Object sum_dis = list.get(i).get("DIS_AMOUNT");
				Double sum_diss = Double.parseDouble(sum_dis.toString());
				dis_amount +=sum_diss;
			}
			
			++y;
			sheet.mergeCells(0, y, 12, y);
			sheet.addCell(new Label(0, y, "合计",wcf));
			sheet.addCell(new Label(1, y, "合计"));
			sheet.addCell(new Label(2, y, "合计"));
			//sheet.mergeCells(3, y, 12, y);
			sheet.addCell(new Label(3, y, ""));
			sheet.addCell(new Label(4, y, ""));
			sheet.addCell(new Label(5, y, ""));
			sheet.addCell(new Label(6, y, ""));
			sheet.addCell(new Label(7, y, ""));
			sheet.addCell(new Label(8, y, ""));
			sheet.addCell(new Label(9, y, ""));
			sheet.addCell(new Label(10, y, ""));
			sheet.addCell(new Label(11, y, ""));
			
			sheet.addCell(new Label(12, y, String.valueOf(intent_count)));
			sheet.addCell(new Label(13, y,String.valueOf(intent_count) ));
			if(norm_amount==0){
				sheet.addCell(new Label(14, y, ""));
			}
			else{
				sheet.addCell(new Label(14, y, String.valueOf(norm_amount)));
			}
			if(count_amount==0){
				sheet.addCell(new Label(15, y, ""));
			}
			else{
				sheet.addCell(new Label(15, y, String.valueOf(count_amount)));
			}
			
			if(intent_point==0){
				sheet.addCell(new Label(16, y, ""));
			}
			else{
				sheet.addCell(new Label(16, y, String.valueOf(intent_point)));
			}
			if(other_amount==0){
				sheet.addCell(new Label(17, y, ""));
			}
			else{
				sheet.addCell(new Label(17, y, String.valueOf(other_amount)));
			}
			if(dis_amount==0){
				sheet.addCell(new Label(18, y, ""));
			}
			else{
				sheet.addCell(new Label(18, y, String.valueOf(dis_amount)));
			}
			
			
			
			sheet.mergeCells(19, y, 20, y);
			sheet.addCell(new Label(19, y, ""));
			sheet.addCell(new Label(20, y, ""));
			
			
			workbook.write();
			workbook.close();
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
