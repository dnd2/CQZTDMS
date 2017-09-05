package com.infodms.dms.actions.report;

import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
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
import com.infodms.dms.dao.report.FollowPrepareDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

import edu.emory.mathcs.backport.java.util.LinkedList;



public class FollowPrepareReport {
	private Logger logger = Logger.getLogger(FollowPrepareReport.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private FollowPrepareDao dao = FollowPrepareDao.getInstance();
	private AclUserBean logonUser = null;
	private final String FollowPrepare_URL = "/jsp/report/totalReport/FollowPrepare.jsp";
	public void getFollowPrepareReport(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String followDate1 = CommonUtils.checkNull(request.getParamValue("followDate1"));
			String followDate2 = CommonUtils.checkNull(request.getParamValue("followDate2"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			
			act.setOutData("followDate1", followDate1);
			act.setOutData("followDate2", followDate2);
			
			map.put("followDate1", followDate1);
			map.put("followDate2", followDate2);
			map.put("orgId", orgId);
			
			act.setOutData("map", map);
			
			List<Map<String, Object>> list_FollowPrepare = dao.getFollowPrepareSelect(map);
			int len = list_FollowPrepare.size();
			int  series_count =0;
			for (int i = 0; i < len; i++) {
				Object count = list_FollowPrepare.get(i).get("SERIES_COUNT");
				int counts = Integer.parseInt(count.toString());
				series_count +=counts;
			}
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			
			act.setOutData("series_count", series_count);
			act.setOutData("list_FollowPrepare", list_FollowPrepare);
			act.setForword(FollowPrepare_URL);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证处理情况");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public  void FollowPrepareReportDownload(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String followDate1 = CommonUtils.checkNull(request.getParamValue("followDate1"));
			String followDate2 = CommonUtils.checkNull(request.getParamValue("followDate2"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			
			map.put("followDate1", followDate1);
			map.put("followDate2", followDate2);
			map.put("orgId", orgId);
			
			// 导出的文件名
			String fileName = "集团客户报备跟进表.csv";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			List<List<Object>> lists = new java.util.LinkedList<List<Object>>();
			List<Object> list = new java.util.LinkedList<Object>();
			list.add("组织");
			list.add("省");
			list.add("提报日期");
			list.add("提报单位");
			list.add("客户名称");
			list.add("客户类型");
			list.add("主营业务");
			list.add("邮编");
			list.add("详细地址");
			list.add("主要联系人");
			list.add("职务");
			list.add("电话");
			list.add("车系");
			list.add("数量");
			list.add("备注");
			list.add("跟进日期");
			list.add("跟进内容");
			lists.add(list);
			List<Map<String, Object>> list_result = dao.getFollowPrepareSelect(map);
			int len =list_result.size();
			for (int i = 0; i < len; i++) {
				Map<String, Object> record = list_result.get(i);
				list = new java.util.LinkedList<Object>();
				list.add(CommonUtils.checkNull(record.get("ORG_NAME")));
				list.add(CommonUtils.checkNull(record.get("REGION_NAME")));
				list.add(CommonUtils.checkNull(record.get("SUBMIT_DATE")));
				list.add(CommonUtils.checkNull(record.get("COMPANY_NAME")));
				list.add(CommonUtils.checkNull(record.get("FLEET_NAME")));
				list.add(CommonUtils.checkNull(record.get("FLEET_TYPE")));
				list.add(CommonUtils.checkNull(record.get("MAIN_BUSINESS")));
				list.add(CommonUtils.checkNull(record.get("ZIP_CODE")));
				list.add(CommonUtils.checkNull(record.get("ADDRESS")));
				list.add(CommonUtils.checkNull(record.get("MAIN_LINKMAN")));
				list.add(CommonUtils.checkNull(record.get("MAIN_JOB")));
				list.add(CommonUtils.checkNull(record.get("MAIN_PHONE")));
				list.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
				list.add(CommonUtils.checkNull(record.get("SERIES_COUNT")));
				list.add(CommonUtils.checkNull(record.get("REQ_REMARK")));
				list.add(CommonUtils.checkNull(record.get("FOLLOW_DATE")));
				list.add(CommonUtils.checkNull(record.get("FOLLOW_REMARK")));
				lists.add(list);
			}
			OutputStream os = response.getOutputStream();
			CsvWriterUtil.writeCsv(lists, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "集团客户报备跟进表下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void download(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String followDate1 = CommonUtils.checkNull(request.getParamValue("followDate1"));
			String followDate2 = CommonUtils.checkNull(request.getParamValue("followDate2"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			
			map.put("followDate1", followDate1);
			map.put("followDate2", followDate2);
			map.put("orgId", orgId);
			
			// 导出的文件名
			String fileName = "集团客户报备跟进表.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("集团客户报备跟进表下载", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			
			
			
			List<Map<String, Object>> list = dao.getFollowPrepareSelect(map);
			int len = list.size();
			int y = 0 ;
			
			int  series_count =0;
			for (int i = 0; i < len; i++) {
				Object count = list.get(i).get("SERIES_COUNT");
				int counts = Integer.parseInt(count.toString());
				series_count +=counts;
			}
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			sheet.mergeCells(0, y, 16, y);
			sheet.addCell(new jxl.write.Label(0, y, "集团客户报备跟进表下载",wcf));
			
			++y;
			sheet.mergeCells(0, y, 16, y);
			sheet.addCell(new jxl.write.Label(0, y, "跟进日期："+followDate1+"--"+followDate2));
			
			++y;
			
			sheet.addCell(new Label(0, y, "组织"));
			sheet.addCell(new Label(1, y, "省"));
			sheet.addCell(new Label(2, y, "提报单位"));
			sheet.addCell(new Label(3, y, "提报日期"));
			sheet.addCell(new Label(4, y, "客户名称"));
			sheet.addCell(new Label(5, y, "客户类型"));
			sheet.addCell(new Label(6, y, "主营业务"));
			sheet.addCell(new Label(7, y, "邮编"));
			sheet.addCell(new Label(8, y, "详细地址"));
			sheet.addCell(new Label(9, y, "主要联系人"));
			sheet.addCell(new Label(10, y, "职务"));
			sheet.addCell(new Label(11, y, "电话"));
			sheet.addCell(new Label(12, y, "车系"));
			sheet.addCell(new Label(13, y, "数量"));
			sheet.addCell(new Label(14, y, "备注"));
			sheet.addCell(new Label(15, y, "跟进日期"));
			sheet.addCell(new Label(16, y, "跟进内容"));
			
			
			for (int i = 0; i < len; i++) {
				++y;
				sheet.addCell(new Label(0, y, list.get(i).get("ORG_NAME")==null ? "":list.get(i).get("ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ? "":list.get(i).get("REGION_NAME").toString()));
				sheet.addCell(new Label(2, y, list.get(i).get("COMPANY_NAME")==null ? "":list.get(i).get("COMPANY_NAME").toString()));
				sheet.addCell(new Label(3, y, list.get(i).get("SUBMIT_DATE")==null ? "":list.get(i).get("SUBMIT_DATE").toString()));
				sheet.addCell(new Label(4, y, list.get(i).get("FLEET_NAME")==null ? "":list.get(i).get("FLEET_NAME").toString()));
				sheet.addCell(new Label(5, y, list.get(i).get("FLEET_TYPE")==null ? "":list.get(i).get("FLEET_TYPE").toString()));
				sheet.addCell(new Label(6, y, list.get(i).get("MAIN_BUSINESS")==null ? "":list.get(i).get("MAIN_BUSINESS").toString()));
				sheet.addCell(new Label(7, y, list.get(i).get("ZIP_CODE")==null ? "":list.get(i).get("ZIP_CODE").toString()));
				sheet.addCell(new Label(8, y, list.get(i).get("ADDRESS")==null ? "":list.get(i).get("ADDRESS").toString()));
				sheet.addCell(new Label(9, y, list.get(i).get("MAIN_LINKMAN")==null ? "":list.get(i).get("MAIN_LINKMAN").toString()));
				sheet.addCell(new Label(10, y, list.get(i).get("MAIN_JOB")==null ? "":list.get(i).get("MAIN_JOB").toString()));
				sheet.addCell(new Label(11, y, list.get(i).get("MAIN_PHONE")==null ? "":list.get(i).get("MAIN_PHONE").toString()));
				sheet.addCell(new Label(12, y, list.get(i).get("GROUP_NAME")==null ? "":list.get(i).get("GROUP_NAME").toString()));
				sheet.addCell(new Label(13, y, list.get(i).get("SERIES_COUNT")==null ? "":list.get(i).get("SERIES_COUNT").toString()));
				sheet.addCell(new Label(14, y, list.get(i).get("REQ_REMARK")==null ? "":list.get(i).get("REQ_REMARK").toString()));
				sheet.addCell(new Label(15, y, list.get(i).get("FOLLOW_DATE")==null ? "":list.get(i).get("FOLLOW_DATE").toString()));
				sheet.addCell(new Label(16, y, list.get(i).get("FOLLOW_REMARK")==null ? "":list.get(i).get("FOLLOW_REMARK").toString()));
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
			sheet.addCell(new Label(12, y, ""));
			
			sheet.addCell(new Label(13, y,String.valueOf(series_count)));
			
			sheet.mergeCells(14, y, 16, y);
			sheet.addCell(new Label(14, y, ""));
			sheet.addCell(new Label(15, y, ""));
			sheet.addCell(new Label(16, y, ""));
			
			
			workbook.write();
			workbook.close();
			
			os.flush();
			os.close();
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "集团客户报备跟进表下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
