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
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.report.ActSalesAllSDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;


public class ActSalesAllReports {
	private Logger logger = Logger.getLogger(ActSalesAllReport.class);
	private ActSalesAllSDao dao = ActSalesAllSDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	
	private static final  String ActSalesAll_URL = "/jsp/report/totalReport/ActSalesAllS.jsp";
	
	
	public void getActSalesAllReport(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String dealerId = CommonUtils.checkNull(request.getParamValue("mydealerId"));
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String campaignModel = CommonUtils.checkNull(request.getParamValue("campaignModel"));	//车型
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			
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
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			map.put("dealerId", dealerId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("areaId", areaId);
			map.put("campaignModel", campaignModel);
			map.put("orgId", orgId);
			
			List<Map<String, Object>> list_series_name = dao.getList(map);
			
			List<Map<String, Object>> list_detail = dao.getDetailList(map, list_series_name);
			act.setOutData("list_detail", list_detail);
			act.setOutData("list_series_name", list_series_name);
			
			
			act.setOutData("beginTime",beginTime );
			act.setOutData("endTime",endTime );
			act.setForword(ActSalesAll_URL);
		} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商销售汇总表");
				logger.error(logonUser,e1);
				act.setException(e1);
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
			String campaignModel = CommonUtils.checkNull(request.getParamValue("campaignModel"));	//车型
			
			map.put("dealerId", dealerId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("areaId", areaId);
			map.put("campaignModel", campaignModel);
			// 导出的文件名
			String fileName = "经销商销售汇总表.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("经销商销售汇总表", 0);
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
			sheet.addCell(new  Label(0, y, "经销商销售汇总表",wcf));
			
			
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
			
			int actColAmounts =0;
			int total =0;
			
			
			int[] total_dlr = new int[len_name];
			int[] total_region = new int[len_name];
			int[] total_org = new int[len_name];
			int[] total_all = new int[len_name];
			
			
			if(list_detail!=null&&len_detail!=0){
				for(int i=0;i<len_detail;i++){
					int actCol = 0;
					for(int j=0;j<len_name;j++){
						actCol +=Integer.parseInt(list_detail.get(i).get("ACTAMOUNT"+j).toString());
					}
					if(i==0){
						actColAmounts =actCol;
						
						++y;
						sheet.addCell(new  Label(0, y, list_detail.get(i).get("ORG_NAME")==null?"":list_detail.get(i).get("ORG_NAME").toString()));
						sheet.addCell(new  Label(1, y, list_detail.get(i).get("REGION_NAME")==null?"":list_detail.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new  Label(2, y, list_detail.get(i).get("ROOT_DEALER_NAME")==null?"":list_detail.get(i).get("ROOT_DEALER_NAME").toString()));
						sheet.addCell(new  Label(3, y, list_detail.get(i).get("DEALER_NAME")==null?"":list_detail.get(i).get("DEALER_NAME").toString()));
						for (int x = 0; x < len_name; x++) {
							total_dlr[x] +=Integer.parseInt(list_detail.get(i).get("ACTAMOUNT"+x).toString());
							total_region[x] +=Integer.parseInt(list_detail.get(i).get("ACTAMOUNT"+x).toString());
							total_org[x] +=Integer.parseInt(list_detail.get(i).get("ACTAMOUNT"+x).toString());
							total_all[x] +=Integer.parseInt(list_detail.get(i).get("ACTAMOUNT"+x).toString());

							sheet.addCell(new  Label(x+4, y, list_detail.get(i).get("ACTAMOUNT"+x)==null?"":list_detail.get(i).get("ACTAMOUNT"+x).toString()));
						}
						int w =len_name+4;
						sheet.addCell(new Label(w, y, String.valueOf(actCol)));
					}else{
						String before = list_detail.get(i-1).get("ROOT_DEALER_NAME").toString();
						String after = list_detail.get(i).get("ROOT_DEALER_NAME").toString();
						
						String before11 = list_detail.get(i-1).get("REGION_NAME").toString();
						String after12 = list_detail.get(i).get("REGION_NAME").toString();
						
						String before21 = list_detail.get(i-1).get("ORG_NAME").toString();
						String after22 = list_detail.get(i).get("ORG_NAME").toString();
						if(after.equals(before)&&after12.equals(before11)&&after22.equals(before21)){
							actColAmounts +=actCol;
							
							++y;
							sheet.addCell(new  Label(0, y, list_detail.get(i).get("ORG_NAME")==null?"":list_detail.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new  Label(1, y, list_detail.get(i).get("REGION_NAME")==null?"":list_detail.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new  Label(2, y, list_detail.get(i).get("ROOT_DEALER_NAME")==null?"":list_detail.get(i).get("ROOT_DEALER_NAME").toString()));
							sheet.addCell(new  Label(3, y, list_detail.get(i).get("DEALER_NAME")==null?"":list_detail.get(i).get("DEALER_NAME").toString()));
							for (int x = 0; x < len_name; x++) {
								total_dlr[x] +=Integer.parseInt(list_detail.get(i).get("ACTAMOUNT"+x).toString());
								total_region[x] +=Integer.parseInt(list_detail.get(i).get("ACTAMOUNT"+x).toString());
								total_org[x] +=Integer.parseInt(list_detail.get(i).get("ACTAMOUNT"+x).toString());
								total_all[x] +=Integer.parseInt(list_detail.get(i).get("ACTAMOUNT"+x).toString());
								
								sheet.addCell(new  Label(x+4, y, list_detail.get(i).get("ACTAMOUNT"+x)==null?"":list_detail.get(i).get("ACTAMOUNT"+x).toString()));
							}
							int w =len_name+4;
							sheet.addCell(new Label(w, y, String.valueOf(actCol)));
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
							
							actColAmounts =actCol;
							
							//开始1
							String before1 = list_detail.get(i-1).get("REGION_NAME").toString();
							String after1 = list_detail.get(i).get("REGION_NAME").toString();
							
							String before31 = list_detail.get(i-1).get("ORG_NAME").toString();
							String after32 = list_detail.get(i).get("ORG_NAME").toString();
							if(after1.equals(before1)&&after32.equals(before31)){
								actColAmounts =actCol;
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
								
								actColAmounts =actCol;
								
								//开始2
								String before2 = list_detail.get(i-1).get("ORG_NAME").toString();
								String after2 = list_detail.get(i).get("ORG_NAME").toString();
								if(after2.equals(before2)){
									actColAmounts =actCol;
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
									
									actColAmounts =actCol;
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
								total_dlr[x] +=Integer.parseInt(list_detail.get(i).get("ACTAMOUNT"+x).toString());
								total_region[x] +=Integer.parseInt(list_detail.get(i).get("ACTAMOUNT"+x).toString());
								total_org[x] +=Integer.parseInt(list_detail.get(i).get("ACTAMOUNT"+x).toString());
								total_all[x] +=Integer.parseInt(list_detail.get(i).get("ACTAMOUNT"+x).toString());
								
								sheet.addCell(new  Label(x+4, y, list_detail.get(i).get("ACTAMOUNT"+x)==null?"":list_detail.get(i).get("ACTAMOUNT"+x).toString()));
							}
							int t =len_name+4;
							sheet.addCell(new Label(t, y, String.valueOf(actCol)));
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
							sheet.addCell(new Label(ww, y, String.valueOf(actColAmounts)));
							
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
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商销售汇总表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
