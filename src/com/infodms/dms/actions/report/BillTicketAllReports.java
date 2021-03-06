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

import com.infodms.dms.dao.report.BillTicketAllSDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class BillTicketAllReports {
	private Logger logger = Logger.getLogger(BillTicketAllReport.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private BillTicketAllSDao dao = BillTicketAllSDao.getInstance();
	
	private static final String BillTicketAllReport_URL = "/jsp/report/BillTicketAllReports.jsp";
	
	
	public void getBillTicketAllReport(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String dealerId = CommonUtils.checkNull(request.getParamValue("mydealerId"));
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String campaignModel = CommonUtils.checkNull(request.getParamValue("campaignModel"));
			
			map.put("dealerId", dealerId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("areaId", areaId);
			map.put("campaignModel", campaignModel);
			
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			
			act.setOutData("beginTime", beginTime);
			act.setOutData("endTime", endTime);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			
			request.setAttribute("", 00);
			
			List<Map<String, Object>> list_series_name = dao.getList(map);
			
			List<Map<String, Object>> list_detail = dao.getDetailList(map, list_series_name);
			
			
			
			List<List<Object>> listss = new LinkedList<List<Object>>();
			List<Object> list = new LinkedList<Object>();
			
			
			
			act.setOutData("list_detail", list_detail);
			act.setOutData("list_series_name", list_series_name);
			
			int len_de = list_detail.size();
			int sum0 = 0;
			int sum1=0;
			int sum2=0;
			int sum3=0;
			int sum4 = 0;
			int sum5 = 0;
			int sum6 =0;
			int sum7 = 0;
			int total = 0;
			
			
			listss.add(list);
			act.setOutData("listss", listss);
			act.setOutData("sum0", sum0);
			act.setOutData("sum1", sum1);
			act.setOutData("sum2", sum2);
			act.setOutData("sum3", sum3);
			act.setOutData("sum4", sum4);
			act.setOutData("sum5", sum5);
			act.setOutData("sum6", sum6);
			act.setOutData("sum7", sum7);
			act.setOutData("total", total);
			//act.setOutData("list_BillTicketAll", list_BillTicketAll);
			act.setForword(BillTicketAllReport_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商启票汇总表");
			logger.error(logonUser,e1);
			act.setActionReturn(e1);
		}
	}
	
	
	public void download(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String dealerId = CommonUtils.checkNull(request.getParamValue("mydealerId"));
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String campaignModel = CommonUtils.checkNull(request.getParamValue("campaignModel"));
			
			map.put("dealerId", dealerId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("areaId", areaId);
			map.put("campaignModel", campaignModel);
			
			// 导出的文件名
			String fileName = "经销商启票汇总表.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("经销商启票汇总表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			
			
			
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			

			List<Map<String, Object>> list_series_name = dao.getList(map);
			
			List<Map<String, Object>> list_detail = dao.getDetailList(map, list_series_name);
			
			int len_detail = list_detail.size();
			int len_name = list_series_name.size();
			long tbm = 0;
			int y= 0;
			
			
			sheet.mergeCells(0, y, 4+len_name, y);
			sheet.addCell(new  Label(0, y, "经销商启票汇总表",wcf));
			
			
			++y;
			sheet.mergeCells(0, y, 4+len_name, y);
			sheet.addCell(new  Label(0, y, "起止日期："+beginTime+"--"+endTime));
			
			
			
			++y;
			sheet.addCell(new  Label(0, y, "大区"));
			sheet.addCell(new  Label(1, y, "省份"));
			sheet.addCell(new  Label(2, y, "一级经销商"));
			sheet.addCell(new  Label(3, y, "二级经销商"));
			for (int i = 0; i < len_name; i++) {
				sheet.addCell(new  Label(i+4, y, list_series_name.get(i).get("SERIES_NAME")==null?"":list_series_name.get(i).get("SERIES_NAME").toString()));
			}
			int q =len_name+4;
			sheet.addCell(new Label(q, y, "合计"));
			
			int billColAmounts =0;
			int total =0;
			
			
			int[] total_dlr = new int[len_name];
			int[] total_region = new int[len_name];
			int[] total_org = new int[len_name];
			int[] total_all = new int[len_name];
			
			
			if(list_detail!=null&&len_detail!=0){
				for(int i=0;i<len_detail;i++){
					int billCol = 0;
					for(int j=0;j<len_name;j++){
						billCol +=Integer.parseInt(list_detail.get(i).get("BILLAMOUNT"+j).toString());
					}
					if(i==0){
						billColAmounts =billCol;
						
						++y;
						sheet.addCell(new  Label(0, y, list_detail.get(i).get("ORG_NAME")==null?"":list_detail.get(i).get("ORG_NAME").toString()));
						sheet.addCell(new  Label(1, y, list_detail.get(i).get("REGION_NAME")==null?"":list_detail.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new  Label(2, y, list_detail.get(i).get("ROOT_DEALER_NAME")==null?"":list_detail.get(i).get("ROOT_DEALER_NAME").toString()));
						sheet.addCell(new  Label(3, y, list_detail.get(i).get("DEALER_NAME")==null?"":list_detail.get(i).get("DEALER_NAME").toString()));
						for (int x = 0; x < len_name; x++) {
							total_dlr[x] +=Integer.parseInt(list_detail.get(i).get("BILLAMOUNT"+x).toString());
							total_region[x] +=Integer.parseInt(list_detail.get(i).get("BILLAMOUNT"+x).toString());
							total_org[x] +=Integer.parseInt(list_detail.get(i).get("BILLAMOUNT"+x).toString());
							total_all[x] +=Integer.parseInt(list_detail.get(i).get("BILLAMOUNT"+x).toString());

							sheet.addCell(new  Label(x+4, y, list_detail.get(i).get("BILLAMOUNT"+x)==null?"":list_detail.get(i).get("BILLAMOUNT"+x).toString()));
						}
						int w =len_name+4;
						sheet.addCell(new Label(w, y, String.valueOf(billCol)));
					}else{
						String before = list_detail.get(i-1).get("ROOT_DEALER_NAME").toString();
						String after = list_detail.get(i).get("ROOT_DEALER_NAME").toString();
						
						String before11 = list_detail.get(i-1).get("REGION_NAME").toString();
						String after12 = list_detail.get(i).get("REGION_NAME").toString();
						
						String before21 = list_detail.get(i-1).get("ORG_NAME").toString();
						String after22 = list_detail.get(i).get("ORG_NAME").toString();
						if(after.equals(before)&&after12.equals(before11)&&after22.equals(before21)){
							billColAmounts +=billCol;
							
							++y;
							sheet.addCell(new  Label(0, y, list_detail.get(i).get("ORG_NAME")==null?"":list_detail.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new  Label(1, y, list_detail.get(i).get("REGION_NAME")==null?"":list_detail.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new  Label(2, y, list_detail.get(i).get("ROOT_DEALER_NAME")==null?"":list_detail.get(i).get("ROOT_DEALER_NAME").toString()));
							sheet.addCell(new  Label(3, y, list_detail.get(i).get("DEALER_NAME")==null?"":list_detail.get(i).get("DEALER_NAME").toString()));
							for (int x = 0; x < len_name; x++) {
								total_dlr[x] +=Integer.parseInt(list_detail.get(i).get("BILLAMOUNT"+x).toString());
								total_region[x] +=Integer.parseInt(list_detail.get(i).get("BILLAMOUNT"+x).toString());
								total_org[x] +=Integer.parseInt(list_detail.get(i).get("BILLAMOUNT"+x).toString());
								total_all[x] +=Integer.parseInt(list_detail.get(i).get("BILLAMOUNT"+x).toString());
								
								sheet.addCell(new  Label(x+4, y, list_detail.get(i).get("BILLAMOUNT"+x)==null?"":list_detail.get(i).get("BILLAMOUNT"+x).toString()));
							}
							int w =len_name+4;
							sheet.addCell(new Label(w, y, String.valueOf(billCol)));
						}else{
							++y;
							total =0;
							sheet.mergeCells(0, y, 3, y);
							sheet.addCell(new Label(0, y, "一级经销商合计",wcf));
							for (int x = 0; x < len_name; x++) {
								sheet.addCell(new  Label(x+4, y, String.valueOf(total_dlr[x])));
								total +=total_dlr[x];
								total_dlr[x] =0;
							}
							
							int w =len_name+4;
							sheet.addCell(new Label(w, y, String.valueOf(total)));
							
							billColAmounts =billCol;
							
							//开始1
							String before1 = list_detail.get(i-1).get("REGION_NAME").toString();
							String after1 = list_detail.get(i).get("REGION_NAME").toString();
							
							String before31 = list_detail.get(i-1).get("ORG_NAME").toString();
							String after32 = list_detail.get(i).get("ORG_NAME").toString();
							if(after1.equals(before1)&&after32.equals(before31)){
								billColAmounts =billCol;
							}else{
								++y;
								total =0;
								sheet.mergeCells(0, y, 3, y);
								sheet.addCell(new Label(0, y, "省份合计",wcf));
								for (int x = 0; x < len_name; x++) {
									sheet.addCell(new  Label(x+4, y, String.valueOf(total_region[x])));
									total +=total_region[x];
									total_region[x] =0;
								}
								int ww =len_name+4;
								sheet.addCell(new Label(ww, y, String.valueOf(total)));
								
								billColAmounts =billCol;
								
								//开始2
								String before2 = list_detail.get(i-1).get("ORG_NAME").toString();
								String after2 = list_detail.get(i).get("ORG_NAME").toString();
								if(after2.equals(before2)){
									billColAmounts =billCol;
								}else{
									total =0;
									++y;
									sheet.mergeCells(0, y, 3, y);
									sheet.addCell(new Label(0, y, "大区合计",wcf));
									for (int x = 0; x < len_name; x++) {
										sheet.addCell(new  Label(x+4, y, String.valueOf(total_org[x])));
										total +=total_org[x];
										total_org[x] = 0;
									}
									int www =len_name+4;
									sheet.addCell(new Label(www, y, String.valueOf(total)));
									
									billColAmounts =billCol;
								}
								//结束2
							}
							//结束1
							
							
							++y;
							sheet.addCell(new  Label(0, y, list_detail.get(i).get("ORG_NAME")==null?"":list_detail.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new  Label(1, y, list_detail.get(i).get("REGION_NAME")==null?"":list_detail.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new  Label(2, y, list_detail.get(i).get("ROOT_DEALER_NAME")==null?"":list_detail.get(i).get("ROOT_DEALER_NAME").toString()));
							sheet.addCell(new  Label(3, y, list_detail.get(i).get("DEALER_NAME")==null?"":list_detail.get(i).get("DEALER_NAME").toString()));
							for (int x = 0; x < len_name; x++) {
								total_dlr[x] +=Integer.parseInt(list_detail.get(i).get("BILLAMOUNT"+x).toString());
								total_region[x] +=Integer.parseInt(list_detail.get(i).get("BILLAMOUNT"+x).toString());
								total_org[x] +=Integer.parseInt(list_detail.get(i).get("BILLAMOUNT"+x).toString());
								total_all[x] +=Integer.parseInt(list_detail.get(i).get("BILLAMOUNT"+x).toString());
								
								sheet.addCell(new  Label(x+4, y, list_detail.get(i).get("BILLAMOUNT"+x)==null?"":list_detail.get(i).get("BILLAMOUNT"+x).toString()));
							}
							int t =len_name+4;
							sheet.addCell(new Label(t, y, String.valueOf(billCol)));
						}
						if(i==len_detail-1){
							++y;
							total =0;
							sheet.mergeCells(0, y, 3, y);
							sheet.addCell(new Label(0, y, "一级经销商合计",wcf));
							for (int x = 0; x < len_name; x++) {
								sheet.addCell(new  Label(x+4, y, String.valueOf(total_dlr[x])));
								total +=total_dlr[x];
								total_dlr[x] =0;
							}
							int w =len_name+4;
							sheet.addCell(new Label(w, y, String.valueOf(total)));
							
							
							++y;
							total =0;
							sheet.mergeCells(0, y, 3, y);
							sheet.addCell(new Label(0, y, "省份合计",wcf));
							for (int x = 0; x < len_name; x++) {
								sheet.addCell(new  Label(x+4, y, String.valueOf(total_region[x])));
								total +=total_region[x];
								total_region[x] =0;
							}
							int ww =len_name+4;
							sheet.addCell(new Label(ww, y, String.valueOf(billColAmounts)));
							
							++y;
							total =0;
							sheet.mergeCells(0, y, 3, y);
							sheet.addCell(new Label(0, y, "大区合计",wcf));
							for (int x = 0; x < len_name; x++) {
								sheet.addCell(new  Label(x+4, y, String.valueOf(total_org[x])));
								total +=total_org[x];
								total_org[x] =0;
							}
							int www =len_name+4;
							sheet.addCell(new Label(www, y, String.valueOf(total)));
						}
					}
				}
			}
			
							++y;
							total =0;
							sheet.mergeCells(0, y, 3, y);
							sheet.addCell(new Label(0, y, "合计",wcf));
							for (int x = 0; x < len_name; x++) {
								sheet.addCell(new  Label(x+4, y, String.valueOf(total_all[x])));
								total +=total_all[x];
								total_all[x] =0;
							}
							int t =len_name+4;
							sheet.addCell(new Label(t, y, String.valueOf(total)));
							
							
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商启票汇总表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
