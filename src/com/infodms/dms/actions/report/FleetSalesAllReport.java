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
import com.infodms.dms.dao.report.FleetSalesAllDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class FleetSalesAllReport {
	private Logger logger = Logger.getLogger(FleetSalesAllReport.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	
	private FleetSalesAllDao dao = FleetSalesAllDao.getInstance();
	
	private static final String FleetSalesAll_URL = "/jsp/report/totalReport/FleetSalesAll.jsp";
	
	
	public void getFleetSalesAllReport(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String insertBeginTime = CommonUtils.checkNull(request.getParamValue("insertBeginTime"));
			String insertEndTime = CommonUtils.checkNull(request.getParamValue("insertEndTime"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));
			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			
			//只有大区范围限制	2012-08-29 韩晓宇
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
				orgId = logonUser.getOrgId().toString();
			}
			
			act.setOutData("insertBeginTime", insertBeginTime);
			act.setOutData("insertEndTime", insertEndTime);
			map.put("insertBeginTime", insertBeginTime);
			map.put("insertEndTime", insertEndTime);
			map.put("groupCode", groupCode);
			map.put("dealerId", dealerId);
			map.put("orgId", orgId);
			map.put("fleetType", fleetType);
			map.put("modelId", modelId);
			
			List<Map<String, Object>> list_FleetSalesAll = dao.getFleetSalesAllSelect(map);
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			act.setOutData("list_FleetSalesAll", list_FleetSalesAll);
			act.setForword(FleetSalesAll_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"大客户信息实销汇总表");
			logger.error(logonUser,e1);
			act.setActionReturn(e1);
		}
	}
	
	public void FleetSalesAllReportDownload(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String insertBeginTime = CommonUtils.checkNull(request.getParamValue("insertBeginTime"));
			String insertEndTime = CommonUtils.checkNull(request.getParamValue("insertEndTime"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));
			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			
			map.put("insertBeginTime", insertBeginTime);
			map.put("insertEndTime", insertEndTime);
			map.put("groupCode", groupCode);
			map.put("dealerId", dealerId);
			map.put("orgId", orgId);
			map.put("fleetType", fleetType);
			map.put("modelId", modelId);
			

			// 导出的文件名
			String fileName = "大客户信息实销汇总表.csv";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			List<List<Object>> lists = new LinkedList<List<Object>>();
			List<Object> list = new LinkedList<Object>();
			list.add("大区");
			list.add("省份");
			list.add("服务中心名称");
			list.add("交库日期");
			list.add("客户名称");
			list.add("联系人");
			list.add("电话");
			list.add("客户类型");
			list.add("车系");
			list.add("车型");
			list.add("实际交车数");
			list.add("不合格资料数");
			list.add("享受政策支持");
			list.add("单价");
			list.add("兑现折让总额");
			list.add("审核状态");
			list.add("备注");
			
			lists.add(list);
			
			List<Map<String, Object>> result = dao.getFleetSalesAllSelect(map);
			int len = result.size();
			
			for (int i = 0; i < len; i++) {
				Map<String, Object> record = result.get(i);
				list = new LinkedList<Object>();
				list.add(CommonUtils.checkNull(record.get("ROOT_ORG_NAME") ));
				list.add(CommonUtils.checkNull(record.get("REGION_NAME") ));
				list.add(CommonUtils.checkNull(record.get("DEALER_NAME") ));
				list.add(CommonUtils.checkNull(record.get("CONSIGNATION_DATE") ));
				list.add(CommonUtils.checkNull(record.get("FLEET_NAME") ));
				list.add(CommonUtils.checkNull(record.get("MAIN_LINKMAN") ));
				list.add(CommonUtils.checkNull(record.get("MAIN_PHONE") ));
				list.add(CommonUtils.checkNull(record.get("FLEET_TYPE") ));
				list.add(CommonUtils.checkNull(record.get("GROUP_NAME") ));
				list.add(CommonUtils.checkNull(record.get("GROUPMODEL") ));
				list.add(CommonUtils.checkNull(record.get("SALESAMOUNT") ));
				list.add(CommonUtils.checkNull(record.get("") ));
				list.add(CommonUtils.checkNull(record.get("INTENT_POINT") ));
				list.add(CommonUtils.checkNull(record.get("PRICE") ));
				list.add(CommonUtils.checkNull(record.get("") ));
				list.add(CommonUtils.checkNull(record.get("FLEET_STATUS") ));
				list.add(CommonUtils.checkNull(record.get("REQ_REMARK") ));
				
			
				lists.add(list);
			}
			OutputStream os = response.getOutputStream();
			CsvWriterUtil.writeCsv(lists, os);
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"大客户信息实销汇总表");
			logger.error(logonUser,e1);
			act.setActionReturn(e1);
		}
	}
	
	
	public void download(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String insertBeginTime = CommonUtils.checkNull(request.getParamValue("insertBeginTime"));
			String insertEndTime = CommonUtils.checkNull(request.getParamValue("insertEndTime"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));
			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			
			map.put("insertBeginTime", insertBeginTime);
			map.put("insertEndTime", insertEndTime);
			map.put("groupCode", groupCode);
			map.put("dealerId", dealerId);
			map.put("orgId", orgId);
			map.put("fleetType", fleetType);
			map.put("modelId", modelId);
			

			// 导出的文件名
			String fileName = "大客户信息实销汇总表.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			OutputStream os  = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("大客户信息实销汇总表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			List<Map<String, Object>> list = dao.getFleetSalesAllSelect(map);
			int len = list.size();
			int y = 0 ;
			long tsa = 0;
			
			
		/*	Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			*/
			sheet.mergeCells(0, y, 16, y);
			sheet.addCell(new jxl.write.Label(0, y, "大客户信息实销汇总表",wcf));
			
			++y;
			sheet.mergeCells(0, y, 16, y);
			sheet.addCell(new jxl.write.Label(0, y, "交车起止日期："+insertBeginTime+"--"+insertEndTime));
			
			++y;
			
			sheet.addCell(new Label(0, y, "大区"));
			sheet.addCell(new Label(1, y, "省份"));
			sheet.addCell(new Label(2, y, "服务中心名称"));
			sheet.addCell(new Label(3, y, "交库日期"));
			sheet.addCell(new Label(4, y, "客户名称"));
			sheet.addCell(new Label(5, y, "联系人"));
			sheet.addCell(new Label(6, y, "电话"));
			sheet.addCell(new Label(7, y, "客户类型"));
			sheet.addCell(new Label(8, y, "车系"));
			sheet.addCell(new Label(9, y, "车型"));
			sheet.addCell(new Label(10, y, "实际交车数"));
			sheet.addCell(new Label(11, y, "不合格资料数"));
			sheet.addCell(new Label(12, y, "享受政策支持"));
			sheet.addCell(new Label(13, y, "单价"));
			sheet.addCell(new Label(14, y, "兑现折让总额"));
			sheet.addCell(new Label(15, y, "审核状态"));
			sheet.addCell(new Label(16, y, "备注"));
			
			
			
			
			
			if(list!=null){
				long ssa = 0;
				
				long ssa_rn = 0;
				
				long ssa_ron = 0;
				for (int i = 0; i < len; i++) {
					long sa = Long.parseLong(list.get(i).get("SALESAMOUNT").toString());
					if(i==0){
						ssa = sa;
						
						ssa_rn = sa;
						
						ssa_ron = sa;
						tsa = sa;
						++y;
						sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME") ==null ?"": list.get(i).get("ROOT_ORG_NAME").toString()));
						sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME") ==null ?"": list.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new Label(2, y, list.get(i).get("DEALER_NAME") ==null ?"": list.get(i).get("DEALER_NAME").toString()));
						sheet.addCell(new Label(3, y, list.get(i).get("CONSIGNATION_DATE") ==null ?"": list.get(i).get("CONSIGNATION_DATE").toString()));
						sheet.addCell(new Label(4, y, list.get(i).get("FLEET_NAME") ==null ?"": list.get(i).get("FLEET_NAME").toString()));
						sheet.addCell(new Label(5, y, list.get(i).get("MAIN_LINKMAN") ==null ?"": list.get(i).get("MAIN_LINKMAN").toString()));
						sheet.addCell(new Label(6, y, list.get(i).get("MAIN_PHONE") ==null ?"": list.get(i).get("MAIN_PHONE").toString()));
						sheet.addCell(new Label(7, y, list.get(i).get("FLEET_TYPE") ==null ?"": list.get(i).get("FLEET_TYPE").toString()));
						sheet.addCell(new Label(8, y, list.get(i).get("GROUP_NAME") ==null ?"": list.get(i).get("GROUP_NAME").toString()));
						sheet.addCell(new Label(9, y, list.get(i).get("GROUPMODEL") ==null ?"": list.get(i).get("GROUPMODEL").toString()));
						sheet.addCell(new Label(10, y, list.get(i).get("SALESAMOUNT") ==null ?"": list.get(i).get("SALESAMOUNT").toString()));
						sheet.addCell(new Label(11, y, list.get(i).get("") ==null ?"": list.get(i).get("").toString()));
						sheet.addCell(new Label(12, y, list.get(i).get("INTENT_POINT") ==null ?"": list.get(i).get("INTENT_POINT").toString()));
						sheet.addCell(new Label(13, y, list.get(i).get("PRICE") ==null ?"": list.get(i).get("PRICE").toString()));
						sheet.addCell(new Label(14, y, list.get(i).get("") ==null ?"": list.get(i).get("").toString()));
						sheet.addCell(new Label(15, y, list.get(i).get("FLEET_STATUS") ==null ?"": list.get(i).get("FLEET_STATUS").toString()));
						sheet.addCell(new Label(16, y, list.get(i).get("REQ_REMARK") ==null ?"": list.get(i).get("REQ_REMARK").toString()));
						
					}else{
						String before_da = list.get(i-1).get("DEALER_NAME").toString();
						String after_da = list.get(i).get("DEALER_NAME").toString();
						
						String before_da11 = list.get(i-1).get("REGION_NAME").toString();
						String after_da12 = list.get(i).get("REGION_NAME").toString();
						
						String before_da21 = list.get(i-1).get("ROOT_ORG_NAME").toString();
						String after_da22 = list.get(i).get("ROOT_ORG_NAME").toString();
						if(after_da.equals(before_da)&&after_da12.equals(before_da11)&&after_da22.equals(before_da21)){
							ssa += sa;
							
							ssa_rn += sa;
							
							ssa_ron += sa;
							
							tsa += sa;
							
							
							++y;
							sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME") ==null ?"": list.get(i).get("ROOT_ORG_NAME").toString()));
							sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME") ==null ?"": list.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new Label(2, y, list.get(i).get("DEALER_NAME") ==null ?"": list.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new Label(3, y, list.get(i).get("CONSIGNATION_DATE") ==null ?"": list.get(i).get("CONSIGNATION_DATE").toString()));
							sheet.addCell(new Label(4, y, list.get(i).get("FLEET_NAME") ==null ?"": list.get(i).get("FLEET_NAME").toString()));
							sheet.addCell(new Label(5, y, list.get(i).get("MAIN_LINKMAN") ==null ?"": list.get(i).get("MAIN_LINKMAN").toString()));
							sheet.addCell(new Label(6, y, list.get(i).get("MAIN_PHONE") ==null ?"": list.get(i).get("MAIN_PHONE").toString()));
							sheet.addCell(new Label(7, y, list.get(i).get("FLEET_TYPE") ==null ?"": list.get(i).get("FLEET_TYPE").toString()));
							sheet.addCell(new Label(8, y, list.get(i).get("GROUP_NAME") ==null ?"": list.get(i).get("GROUP_NAME").toString()));
							sheet.addCell(new Label(9, y, list.get(i).get("GROUPMODEL") ==null ?"": list.get(i).get("GROUPMODEL").toString()));
							sheet.addCell(new Label(10, y, list.get(i).get("SALESAMOUNT") ==null ?"": list.get(i).get("SALESAMOUNT").toString()));
							sheet.addCell(new Label(11, y, list.get(i).get("") ==null ?"": list.get(i).get("").toString()));
							sheet.addCell(new Label(12, y, list.get(i).get("INTENT_POINT") ==null ?"": list.get(i).get("INTENT_POINT").toString()));
							sheet.addCell(new Label(13, y, list.get(i).get("PRICE") ==null ?"": list.get(i).get("PRICE").toString()));
							sheet.addCell(new Label(14, y, list.get(i).get("") ==null ?"": list.get(i).get("").toString()));
							sheet.addCell(new Label(15, y, list.get(i).get("FLEET_STATUS") ==null ?"": list.get(i).get("FLEET_STATUS").toString()));
							sheet.addCell(new Label(16, y, list.get(i).get("REQ_REMARK") ==null ?"": list.get(i).get("REQ_REMARK").toString()));
							
						}else{
							++y;
							sheet.mergeCells(0, y, 2, y);
							sheet.addCell(new Label(0, y, "经销商合计",wcf));
							sheet.mergeCells(3, y, 9, y);
							
							sheet.addCell(new Label(10, y, String.valueOf(ssa)));
							sheet.mergeCells(11, y, 16, y);
							
							ssa =sa;
							tsa += sa;
							
							
							//开始1
							String before_rn = list.get(i-1).get("REGION_NAME").toString();
							String after_rn = list.get(i).get("REGION_NAME").toString();
							
							String before_rn31 = list.get(i-1).get("ROOT_ORG_NAME").toString();
							String after_rn32 = list.get(i).get("ROOT_ORG_NAME").toString();
							if(after_rn.equals(before_rn)&&after_rn32.equals(before_rn31)){
								ssa_rn += sa;
								
								ssa_ron += sa;
							}else{
								++y;
								sheet.mergeCells(0, y, 2, y);
								sheet.addCell(new Label(0, y, "省份合计",wcf));
								sheet.mergeCells(3, y, 9, y);
								
								sheet.addCell(new Label(10, y, String.valueOf(ssa_rn)));
								sheet.mergeCells(11, y, 16, y);
								
								ssa_rn = 0;
								ssa_rn += sa;
								//开始2
								String before_ron = list.get(i-1).get("ROOT_ORG_NAME").toString();
								String after_ron = list.get(i).get("ROOT_ORG_NAME").toString();
								if(after_ron.equals(before_ron)){
									ssa_ron += sa;
								}else{
									++y;
									sheet.mergeCells(0, y, 2, y);
									sheet.addCell(new Label(0, y, "大区合计",wcf));
									sheet.mergeCells(3, y, 9, y);
									
									sheet.addCell(new Label(10, y, String.valueOf(ssa_ron)));
									sheet.mergeCells(11, y, 16, y);
									
									ssa_ron = 0;
									ssa_ron += sa;
								}
								//结束2
							}
							//结束1
							++y;
							sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME") ==null ?"": list.get(i).get("ROOT_ORG_NAME").toString()));
							sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME") ==null ?"": list.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new Label(2, y, list.get(i).get("DEALER_NAME") ==null ?"": list.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new Label(3, y, list.get(i).get("CONSIGNATION_DATE") ==null ?"": list.get(i).get("CONSIGNATION_DATE").toString()));
							sheet.addCell(new Label(4, y, list.get(i).get("FLEET_NAME") ==null ?"": list.get(i).get("FLEET_NAME").toString()));
							sheet.addCell(new Label(5, y, list.get(i).get("MAIN_LINKMAN") ==null ?"": list.get(i).get("MAIN_LINKMAN").toString()));
							sheet.addCell(new Label(6, y, list.get(i).get("MAIN_PHONE") ==null ?"": list.get(i).get("MAIN_PHONE").toString()));
							sheet.addCell(new Label(7, y, list.get(i).get("FLEET_TYPE") ==null ?"": list.get(i).get("FLEET_TYPE").toString()));
							sheet.addCell(new Label(8, y, list.get(i).get("GROUP_NAME") ==null ?"": list.get(i).get("GROUP_NAME").toString()));
							sheet.addCell(new Label(9, y, list.get(i).get("GROUPMODEL") ==null ?"": list.get(i).get("GROUPMODEL").toString()));
							sheet.addCell(new Label(10, y, list.get(i).get("SALESAMOUNT") ==null ?"": list.get(i).get("SALESAMOUNT").toString()));
							sheet.addCell(new Label(11, y, list.get(i).get("") ==null ?"": list.get(i).get("").toString()));
							sheet.addCell(new Label(12, y, list.get(i).get("INTENT_POINT") ==null ?"": list.get(i).get("INTENT_POINT").toString()));
							sheet.addCell(new Label(13, y, list.get(i).get("PRICE") ==null ?"": list.get(i).get("PRICE").toString()));
							sheet.addCell(new Label(14, y, list.get(i).get("") ==null ?"": list.get(i).get("").toString()));
							sheet.addCell(new Label(15, y, list.get(i).get("FLEET_STATUS") ==null ?"": list.get(i).get("FLEET_STATUS").toString()));
							sheet.addCell(new Label(16, y, list.get(i).get("REQ_REMARK") ==null ?"": list.get(i).get("REQ_REMARK").toString()));
							
							
						}
						if(i==len-1){
							++y;
							sheet.mergeCells(0, y, 2, y);
							sheet.addCell(new Label(0, y, "经销商合计",wcf));
							sheet.mergeCells(3, y, 9, y);
							
							sheet.addCell(new Label(10, y, String.valueOf(ssa)));
							sheet.mergeCells(11, y, 16, y);
							
							++y;
							sheet.mergeCells(0, y, 2, y);
							sheet.addCell(new Label(0, y, "省份合计",wcf));
							sheet.mergeCells(3, y, 9, y);
							
							sheet.addCell(new Label(10, y, String.valueOf(ssa_rn)));
							sheet.mergeCells(11, y, 16, y);
							
							++y;
							sheet.mergeCells(0, y, 2, y);
							sheet.addCell(new Label(0, y, "大区合计",wcf));
							sheet.mergeCells(3, y, 9, y);
							
							sheet.addCell(new Label(10, y, String.valueOf(ssa_ron)));
							sheet.mergeCells(11, y, 16, y);
						}
					}
				}
			}
			++y;
			sheet.mergeCells(0, y, 2, y);
			sheet.addCell(new Label(0, y, "总计实销数量",wcf));
			sheet.mergeCells(3, y, 9, y);
			
			sheet.addCell(new Label(10, y, String.valueOf(tsa)));
			sheet.mergeCells(11, y, 16, y);
			
		
			
			workbook.write();
			workbook.close();
			
			os.flush();
			os.close();
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"大客户信息实销汇总表");
			logger.error(logonUser,e1);
			act.setActionReturn(e1);
		}
	}
}
