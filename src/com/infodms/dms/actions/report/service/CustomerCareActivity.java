package com.infodms.dms.actions.report.service;

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
import com.infodms.dms.dao.report.serviceReport.CustomerCareActivityDao;
import com.infodms.dms.dao.report.serviceReport.DealerFinFDetailDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class CustomerCareActivity {

	 private Logger logger = Logger.getLogger(CustomerCareActivity.class);
	 private final CustomerCareActivityDao cdao = CustomerCareActivityDao.getInstance();
	 private final ClaimReportDao dao  = ClaimReportDao.getInstance();
	 private final String DEALER_FINF_DETAIL = "/jsp/report/service/Activity_Customer_Care.jsp";
	 private final String DEALER_FINF_DETAIL_QUERY = "/jsp/report/service/Activity_Customer_Care_del.jsp";
	 	ActionContext act = ActionContext.getContext();
	 	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	 	RequestWrapper request = act.getRequest();
		 
	 /**
	 * 初始化页面
	 */
	public void CustomerCareActivityInit(){

	    	try {
				List<Map<String, Object>> list = dao.getBigOrgList();
				act.setOutData("list", list);
				act.setOutData("serviceactivityType", "");
	    		act.setForword(DEALER_FINF_DETAIL);
	    		
	    	} catch (Exception e) {
	    		BizException e1 = new BizException(act, e,
	    				ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商客户关怀活动总结汇总表");
	    		logger.error(logonUser, e1);
	    		act.setException(e1);
	    	}
	    	
	    }
	
	/**
	 * 查询页面
	 */
	public void CustomerCareActivityQuery(){

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
			PageResult<Map<String, Object>> ps= cdao.RqueryCustomerCareActivity(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
/*            act.setOutData("result", result);  
			List<Map<String, Object>> list = dao.getBigOrgList();

			act.setOutData("serviceactivityType", serviceactivityType);	
			act.setOutData("ButieBh", ButieBh);
			act.setOutData("ButieName", ButieName);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("bigorgId", bigorgId);
			act.setOutData("list", list);*/
			//act.setForword(DEALER_FINF_DETAIL_QUERY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商客户关怀活动总结汇总表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

		}
	
	/**
	 * 导出页面
	 */
	public void CustomerCareActivityExport(){
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
			List<Map<String, Object>> result = cdao.QueryCustomerCareActivity(map);
			ResponseWrapper response = act.getResponse();
			// 导出的文件名
			String fileName = "经销商客户关怀活动总结汇总表.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			WritableWorkbook wbook = null;

		    wbook = Workbook.createWorkbook(act.getResponse().getOutputStream());
			WritableSheet wsheet = wbook.createSheet("经销商客户关怀活动总结汇总表",0);

			// 定义一个集合
			List<List<Object>> list1 = new LinkedList<List<Object>>();
			String[] title =new String[11];
				title[0]="大区";
				title[1]="服务商代码";
				title[2]="服务商名称";
				title[3]="主题编号";
				title[4]="活动主题";
				title[5]="活动总结";
				title[6]="活动改进及今后建议";
				title[7]="进站量（台）";
				title[8]="老客户返厂量（台）";
				title[9]="客单价（元）";
				title[10]="营业额（元）";
				
				String[] title1 =new String[4];
				title1[0]="提升目标数值";
				title1[1]="活动前平均数据";
				title1[2]="活动期间数据";
				title1[3]="增长%";

				WritableFont font3=new WritableFont(WritableFont.createFont("楷体_GB2312"),10,WritableFont.BOLD); 
				WritableCellFormat format1=new WritableCellFormat(font3); 
				//把水平对齐方式指定为居中 
				format1.setAlignment(jxl.format.Alignment.CENTRE); 
				//把垂直对齐方式指定为居中
				format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 

/*			Label excelTitle = new Label(0, 0, title[0],format1); 
			wsheet.addCell(excelTitle);
			wsheet.mergeCells(0,0,0,1);
			Label excelTitle1 = new Label(1, 0, title[1],format1); 
			wsheet.addCell(excelTitle1);
			wsheet.mergeCells(1,0,1,1);*/
				for (int i = 0; i < title.length-4; i++)
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
					wsheet.mergeCells(7,0,10,0);
					for (int j = 0; j < title1.length; j++)
					{  
						Label excelTitle1 = new Label(j+11, 1, title1[j],format1); 
						wsheet.addCell(excelTitle1);
					}
					Label excelTitle1 = new Label(11, 0, title[8],format1); 
					wsheet.addCell(excelTitle1);
					wsheet.mergeCells(11,0,14,0);
				   for (int j = 0; j < title1.length; j++)
					{  
						Label excelTitle2 = new Label(j+15, 1, title1[j],format1); 
						wsheet.addCell(excelTitle2);
					}
					Label excelTitle2 = new Label(15, 0, title[9],format1); 
					wsheet.addCell(excelTitle2);
					wsheet.mergeCells(15,0,18,0);
					for (int j = 0; j < title1.length; j++)
					{  
						Label excelTitle3 = new Label(j+19, 1, title1[j],format1); 
						wsheet.addCell(excelTitle3);
					}
					Label excelTitle3 = new Label(19, 0, title[10],format1); 
					wsheet.addCell(excelTitle3);
					wsheet.mergeCells(19,0,22,0);
					
					List<Map<String, Object>> rslist = result;
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
						  	Label content1 = new Label(0, j,""+mapTemp.get("ROOT_ORG_NAME"),format2);
			            	Label content2 = new Label(1, j,""+mapTemp.get("DEALER_CODE"),format2);
				            Label content3 = new Label(2, j,""+mapTemp.get("DEALER_NAME"),format2);
				            Label content4 = new Label(3, j,""+mapTemp.get("SUBJECT_NO"),format2);
				            Label content5 = new Label(4, j,""+mapTemp.get("SUBJECT_NAME"),format2);
				            Label content6 = new Label(5, j,""+mapTemp.get("EVALUATE"),format2);
				            Label content7 = new Label(6, j,""+mapTemp.get("MEASURES"),format2);
				            
				            Label content8 = new Label(7, j,""+mapTemp.get("PULL_IN_NUM"),format2);
				            Label content9 = new Label(8, j,""+mapTemp.get("PULL_IN_MEAN"),format2);
				            Label content10 = new Label(9, j,""+mapTemp.get("PULL_IN_REGION"),format2);
				            Label content11 = new Label(10, j,""+mapTemp.get("PULL_IN_INCRE"),format2);
				            
				            Label content12 = new Label(11, j,""+mapTemp.get("CUSTOMER_NUM"),format2);
				            Label content13 = new Label(12, j,""+mapTemp.get("CUSTOMER_MEAN"),format2);
				            Label content14 = new Label(13, j,""+mapTemp.get("CUSTOMER_REGION"),format2);
				            Label content15 = new Label(14, j,""+mapTemp.get("CUSTOMER_INCRE"),format2);
				            
				            Label content16 = new Label(15, j,""+mapTemp.get("PRICE_NUM"),format2);
				            Label content17 = new Label(16, j,""+mapTemp.get("PRICE_MEAN"),format2);
				            Label content18 = new Label(17, j,""+mapTemp.get("PRICE_REGION"),format2);
				            Label content19 = new Label(18, j,""+mapTemp.get("PRICE_INCRE"),format2);
				            
				            Label content20 = new Label(19, j,""+mapTemp.get("OPEN_NUM"),format2);
				            Label content21 = new Label(20, j,""+mapTemp.get("OPEN_MEAN"),format2);
				            Label content22 = new Label(21, j,""+mapTemp.get("OPEN_REGION"),format2);
				            Label content23 = new Label(22, j,""+mapTemp.get("OPEN_INCRE"),format2);


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
			            	wsheet.addCell(content22);  
			            	wsheet.addCell(content23);  
			         
			            	j++;
			            
			  

						}

						}

					
		
					
				

	        wbook.write(); 
        	wbook.close(); 
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商客户关怀活动总结汇总表导出");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	   }
	}
	

