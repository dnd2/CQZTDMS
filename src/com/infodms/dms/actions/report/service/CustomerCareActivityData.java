package com.infodms.dms.actions.report.service;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.dao.report.serviceReport.CustomerCareActivityDataDao;
import com.infodms.dms.dao.report.serviceReport.CustomerCareActivityDelDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class CustomerCareActivityData {

	 private Logger logger = Logger.getLogger(CustomerCareActivityData.class);
	 private final CustomerCareActivityDataDao cdao = CustomerCareActivityDataDao.getInstance();
	 private final ClaimReportDao dao  = ClaimReportDao.getInstance();
	 private final String DEALER_FINF_DETAIL = "/jsp/report/service/Activity_Customer_Care_Data.jsp";
	
	 	ActionContext act = ActionContext.getContext();
	 	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	 	RequestWrapper request = act.getRequest();
		 
	 /**
	 * 初始化页面
	 */
	public void CustomerCareActivityDatainit(){

	    	try {
				List<Map<String, Object>> list = dao.getBigOrgList();
				act.setOutData("list", list);
				act.setOutData("serviceactivityType", "");
	    		act.setForword(DEALER_FINF_DETAIL);
	    		
	    	} catch (Exception e) {
	    		BizException e1 = new BizException(act, e,
	    				ErrorCodeConstant.QUERY_FAILURE_CODE, "客户关怀活动数据统计表");
	    		logger.error(logonUser, e1);
	    		act.setException(e1);
	    	}
	    	
	    }
	
	/**
	 * 查询页面
	 */
	public void CustomerCareActivityQueryData(){

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String serviceactivityType = CommonUtils.checkNull(request.getParamValue("serviceactivity_type"));//活动类型
			String ButieBh = CommonUtils.checkNull(request.getParamValue("ButieBh"));//主题编号
			String ButieName = CommonUtils.checkNull(request.getParamValue("ButieName"));//主题名称
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealer_code"));//服务商代码
			String bigorgId = CommonUtils.checkNull(request.getParamValue("big_org"));//大区

			map.put("serviceactivityType", serviceactivityType);
			map.put("ButieBh", ButieBh);
			map.put("ButieName", ButieName);
			map.put("dealerCode", dealerCode);
			map.put("bigorgId", bigorgId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps= cdao.RqueryCustomerCareActivityDate(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "客户关怀活动数据统计表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

		}
	

	/**
	 * 导出页面
	 */
	public void CustomerCareActivityDataExport(){
		OutputStream os = null;

	
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String serviceactivityType = CommonUtils.checkNull(request.getParamValue("serviceactivity_type"));//活动类型
			String ButieBh = CommonUtils.checkNull(request.getParamValue("ButieBh"));//主题编号
			String ButieName = CommonUtils.checkNull(request.getParamValue("ButieName"));//主题名称
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealer_code"));//服务商代码
			String bigorgId = CommonUtils.checkNull(request.getParamValue("big_org"));//大区

			map.put("serviceactivityType", serviceactivityType);
			map.put("ButieBh", ButieBh);
			map.put("ButieName", ButieName);
			map.put("dealerCode", dealerCode);
			map.put("bigorgId", bigorgId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps= cdao.RqueryCustomerCareActivityDate(map,99999, curPage);
			ResponseWrapper response = act.getResponse();
			// 导出的文件名
			String fileName = "客户关怀活动数据统计表.xls";
			// 导出的文字编码客户关怀活动数据明细表
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			WritableWorkbook wbook = null;

		    wbook = Workbook.createWorkbook(act.getResponse().getOutputStream());
			WritableSheet wsheet = wbook.createSheet("客户关怀活动数据统计表",0);

			// 定义一个集合
			List<List<Object>> list1 = new LinkedList<List<Object>>();
			String[] title =new String[15];
				title[0]="大区";
				title[1]="服务商代码";
				title[2]="服务商名称";
				title[3]="主题编号";
				title[4]="活动主题";
				title[5]="宣传费用";
				title[6]="活动台次";	
				title[7]="免费检测费用";	
				title[8]="赠送金额";	
				title[9]="赠送礼品费用";	
				title[10]="赠送配件费用";	
				title[11]="赠送保养费用";	
				title[12]="材料优惠费用";	
				title[13]="工时优惠费用";	
				String[] title1 =new String[2];
				title1[0]="台次";
				title1[1]="金额";

				WritableFont font3=new WritableFont(WritableFont.createFont("楷体_GB2312"),10,WritableFont.BOLD); 
				WritableCellFormat format1=new WritableCellFormat(font3); 
				//把水平对齐方式指定为居中 
				format1.setAlignment(jxl.format.Alignment.CENTRE); 
				//把垂直对齐方式指定为居中
				format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
		
				for (int i = 0; i < title.length-7; i++)
				{  
					Label excelTitle = new Label(i, 0, title[i],format1); 
					wsheet.addCell(excelTitle);
					wsheet.mergeCells(i,0,i,1);
				}
				
				for (int j = 0; j < title1.length; j++)
				{  
					Label excelTitle = new Label(j+7, 1, title1[j],format1); 
					wsheet.addCell(excelTitle);
				}

					Label excelTitle = new Label(7, 0, title[7],format1); 
					wsheet.addCell(excelTitle);
					wsheet.mergeCells(7,0,8,0);
					
		        for (int j = 0; j < title1.length; j++)
					{  
						Label excelTitle1 = new Label(j+9, 1, title1[j],format1); 
						wsheet.addCell(excelTitle1);
					}
					Label excelTitle1 = new Label(9, 0, title[8],format1); 
					wsheet.addCell(excelTitle1);
					wsheet.mergeCells(1,0,10,0);
		        for (int j = 0; j < title1.length; j++)
					{  
						Label excelTitle2 = new Label(j+11, 1, title1[j],format1); 
						wsheet.addCell(excelTitle2);
					}
					Label excelTitle2 = new Label(11, 0, title[9],format1); 
					wsheet.addCell(excelTitle2);
					wsheet.mergeCells(11,0,12,0);
				
		        for (int j = 0; j < title1.length; j++)
					{  
						Label excelTitle3 = new Label(j+13, 1, title1[j],format1); 
						wsheet.addCell(excelTitle3);
					}
					Label excelTitle3 = new Label(13, 0, title[10],format1); 
					wsheet.addCell(excelTitle3);
					wsheet.mergeCells(13,0,14,0);
						
					for (int j = 0; j < title1.length; j++)
					{  
						Label excelTitle4 = new Label(j+15, 1, title1[j],format1); 
						wsheet.addCell(excelTitle4);
					}
					Label excelTitle4 = new Label(15, 0, title[11],format1); 
					wsheet.addCell(excelTitle4);
					wsheet.mergeCells(15,0,15,0);
					
					for (int j = 0; j < title1.length; j++)
					{  
						Label excelTitle5 = new Label(j+17, 1, title1[j],format1); 
						wsheet.addCell(excelTitle5);
					}
					Label excelTitle5 = new Label(17, 0, title[12],format1); 
					wsheet.addCell(excelTitle5);
					wsheet.mergeCells(17,0,18,0);
					
					for (int j = 0; j < title1.length; j++)
					{  
						Label excelTitle6 = new Label(j+19, 1, title1[j],format1); 
						wsheet.addCell(excelTitle6);
					}
					Label excelTitle6 = new Label(19, 0, title[13],format1); 
					wsheet.addCell(excelTitle6);
					wsheet.mergeCells(19,0,20,0);
					
	              List<Map<String, Object>> rslist = ps.getRecords();
					Map mapTemp=new HashMap();
					WritableCellFormat format2=new WritableCellFormat(); 
					//把水平对齐方式指定为居中 
					format2.setAlignment(jxl.format.Alignment.CENTRE); 
					//把垂直对齐方式指定为居中
					format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
					int j=2;
					if(rslist!=null){
						for (int i = 0; i < rslist.size(); i++) {
							mapTemp = rslist.get(i);
						//	listValue.add(mapTemp.get("ROOT_ORG_NAME") != null ? mapTemp.get("ROOT_ORG_NAME") : "");""+mapTemp.get("DEALER_CODE")

						  	Label content1 = new Label(0, j, mapTemp.get("ROOT_ORG_NAME").toString() != null ? mapTemp.get("ROOT_ORG_NAME").toString() : "",format2);
			            	Label content2 = new Label(1, j,mapTemp.get("DEALER_CODE") == null ? "":mapTemp.get("DEALER_CODE").toString(),format2);
				            Label content3 = new Label(2, j,mapTemp.get("DEALER_NAME") == null ? "":mapTemp.get("DEALER_NAME").toString(),format2);
				            Label content4 = new Label(3, j,mapTemp.get("SUBJECT_NO") == null ? "":mapTemp.get("SUBJECT_NO").toString(),format2);
				            Label content5 = new Label(4, j,mapTemp.get("SUBJECT_NAME") == null ? "":mapTemp.get("SUBJECT_NAME").toString(),format2);
				            Label content6 = new Label(5, j,""+mapTemp.get("XC_AMOUNT"),format2);
				            Label content7 = new Label(6, j,""+mapTemp.get("TOTAL"),format2);
				            
				            Label content8 = new Label(7, j,""+mapTemp.get("FREE_TOTAL"),format2);
				            Label content9 = new Label(8, j,""+mapTemp.get("FREE_JC_AMOUNT"),format2);
				            
				            Label content10 = new Label(9, j,""+mapTemp.get("ZS_AMOUNT"),format2);
				            Label content11 = new Label(10, j,""+mapTemp.get("ZS_AMOUNT_TOTAL"),format2);
				            
				            Label content12 = new Label(11, j,""+mapTemp.get("LP_AMOUNT"),format2);
				            Label content13 = new Label(12, j,""+mapTemp.get("LP_AMOUNT_TOTAL"),format2);
				            
				            Label content14 = new Label(13, j,""+mapTemp.get("PART_ZS_TOTAL"),format2);
				            Label content15 = new Label(14, j,""+mapTemp.get("PART_ZS_AMOUNT"),format2);
				            
				            Label content16 = new Label(15, j,""+mapTemp.get("BAOYANG_ZS_TOTAL"),format2);
				            Label content17 = new Label(16, j,""+mapTemp.get("BAOYANG_ZS_AMOUNT"),format2);
				            
				            Label content18 = new Label(17, j,""+mapTemp.get("PART_TOTAL"),format2);
				            Label content19 = new Label(18, j,""+mapTemp.get("PART_AMOUNT"),format2);
				            
				            Label content20 = new Label(19, j,""+mapTemp.get("LABOUR_TOTAL"),format2);
				            Label content21 = new Label(20, j,""+mapTemp.get("LABOUR_AMOUNT"),format2);

			             	wsheet.addCell(content1);  
			            	wsheet.addCell(content2);
			            	wsheet.addCell(content3);  
			            	wsheet.addCell(content4);  
			            	wsheet.addCell(content5);  
			            	wsheet.addCell(content6);
			            	wsheet.addCell(content7);  
			            	wsheet.addCell(content8);  
			            	wsheet.addCell(content9);
			            	wsheet.addCell(content10);  
			            	wsheet.addCell(content11);  
			            	wsheet.addCell(content12);  
			            	wsheet.addCell(content13);
			            	wsheet.addCell(content14);  
			            	wsheet.addCell(content15);  
			            	wsheet.addCell(content16);  
			            	wsheet.addCell(content17);
			            	wsheet.addCell(content18);  
			            	wsheet.addCell(content19);  
			            	wsheet.addCell(content20);  
			            	wsheet.addCell(content21);  

			            	j++;

						}

						}

	        wbook.write(); 
        	wbook.close(); 
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商客户关怀活动统计表导出");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
}
