package com.infodms.dms.actions.claim.basicData;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class ToExcel {
	/**
	 * toExcel
	 * 
	 * @param response
	 * @param request
	 * @param head
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static Object toExcel(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "三包规则维护明细模板.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("三包规则维护明细模板", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	/**
	 * 结算单EXCEL导出
	 */
	public static Object toBalanceExcel(ResponseWrapper response,RequestWrapper request,String name,Map<String, Object> balInfo,List<Map<String,Object>> list
										,List<Map<String, Object>> series_list,Long vehicle_num,Double sum,Double fj_sum) throws Exception{
		jxl.write.WritableWorkbook wwkb = null;
		OutputStream out = null;
		DecimalFormat df = new DecimalFormat( "#,###,###,###,###,###.00 ");
		//String sum = df.format(sum_amount);
		//String fj_sum = df.format(sum_fj_amount);
		try {	
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name,"utf-8"));
			out = response.getOutputStream();
			wwkb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet sheet = wwkb.createSheet("运单结算",0);
			//居中，size：10
			WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(Alignment.CENTRE);
			sheet.mergeCells(0,0,9,0);
			sheet.mergeCells(1,1,2,1);
			sheet.mergeCells(8,1,9,1);
			sheet.setColumnView(2,18);
			sheet.setColumnView(3,18);
			//结算单基础信息 
			sheet.addCell(new Label(0,1,"结算车队",wcf));
			sheet.addCell(new Label(1,1,(String)balInfo.get("LOGI_FULL_NAME"),wcf));
			sheet.addCell(new Label(3,1,"结算年度",wcf));
			sheet.addCell(new Label(4,1,(String)balInfo.get("YEAR"),wcf));
			sheet.addCell(new Label(5,1,"结算月份",wcf));
			sheet.addCell(new Label(6,1,(String)balInfo.get("MONTH"),wcf));
			sheet.addCell(new Label(7,1,"结算单号",wcf));
			sheet.addCell(new Label(8,1,(String)balInfo.get("BAL_NO"),wcf));
			sheet.addCell(new Label(0,2,"序号",wcf));
			sheet.addCell(new Label(1,2,"日期",wcf));
			sheet.addCell(new Label(2,2,"发运单号",wcf));
			sheet.addCell(new Label(3,2,"发往单位",wcf));
			sheet.addCell(new Label(4,2,"车系名称",wcf));
			sheet.addCell(new Label(5,2,"数量",wcf));
			sheet.addCell(new Label(6,2,"运送里程",wcf));
			sheet.addCell(new Label(7,2,"里程单价",wcf));
			sheet.addCell(new Label(8,2,"结算金额",wcf));
			sheet.addCell(new Label(9,2,"备注",wcf));
			int count = 3;
			//结算单详细(按运单)
			if(list != null && list.size()>0){
				for(int i = 0 ; i < list.size() ; i++){
					Map<String, Object> map = list.get(i);
					sheet.addCell(new Label(0,i+3,map.get("ROW_NUM").toString(),wcf));
					sheet.addCell(new Label(1,i+3,map.get("CREATE_DATE").toString(),wcf));
					sheet.addCell(new Label(2,i+3,map.get("BILL_NO").toString(),wcf));
					sheet.addCell(new Label(3,i+3,map.get("DEALER_NAME").toString(),wcf));
					sheet.addCell(new Label(4,i+3,map.get("GROUP_NAME").toString(),wcf));
					sheet.addCell(new Label(5,i+3,map.get("COUNT").toString(),wcf));
					sheet.addCell(new Label(6,i+3,map.get("DISTANCE").toString(),wcf));
					sheet.addCell(new Label(7,i+3,map.get("SEND_FARE").toString(),wcf));
					sheet.addCell(new Label(8,i+3,map.get("AMOUNT").toString(),wcf));
					sheet.addCell(new Label(9,i+3,map.get("REGION_NAME").toString(),wcf));
					count ++;
				}
			}
			//结算单详细(按车系)
			sheet.mergeCells(0,count,1,count);
			sheet.mergeCells(2,count,3,count);
			sheet.mergeCells(4,count,5,count);
			//sheet.mergeCells(6,count,7,count);
			sheet.mergeCells(8,count,9,count);
			sheet.addCell(new Label(0,count,"车种",wcf));
			sheet.addCell(new Label(2,count,"车数小计",wcf));
			sheet.addCell(new Label(4,count,"常规运费",wcf));
			sheet.addCell(new Label(6,count,"非常规运费",wcf));
			sheet.addCell(new Label(7,count,"附加运费",wcf));
			sheet.addCell(new Label(8,count,"运费合计",wcf));
			count ++;
			int count_1 = count;
			if(series_list != null && series_list.size()>0){
				for(int j = 0 ; j < series_list.size() ; j++){
					Map<String, Object> map = series_list.get(j);
					sheet.mergeCells(0,count+j,1,count+j);
					sheet.mergeCells(2,count+j,3,count+j);
					sheet.mergeCells(4,count+j,5,count+j);
					//sheet.mergeCells(6,count+j,7,count+j);
					sheet.mergeCells(8,count+j,9,count+j);
					sheet.addCell(new Label(0,j+count,map.get("GROUP_NAME").toString(),wcf));
					sheet.addCell(new Label(2,j+count,map.get("VEHICLE_NUM").toString(),wcf));
					sheet.addCell(new Label(4,j+count,map.get("BAL_AMOUNT").toString(),wcf));
					sheet.addCell(new Label(6,j+count,"0",wcf));
					sheet.addCell(new Label(7,j+count,map.get("FJ_AMOUNT").toString(),wcf));
					sheet.addCell(new Label(8,j+count,df.format(Double.parseDouble(map.get("BAL_AMOUNT").toString())+Double.parseDouble(map.get("FJ_AMOUNT").toString())),wcf));
					count_1 ++;
				}
			}
			//结算单详细按车系汇总
			sheet.mergeCells(0,count_1,1,count_1);
			sheet.mergeCells(2,count_1,3,count_1);
			sheet.mergeCells(4,count_1,5,count_1);
			//sheet.mergeCells(6,count_1,7,count_1);
			sheet.mergeCells(8,count_1,9,count_1);
			sheet.addCell(new Label(0,count_1,"合计",wcf));
			sheet.addCell(new Label(2,count_1,vehicle_num.toString(),wcf));
			sheet.addCell(new Label(4,count_1,df.format(sum),wcf));
			sheet.addCell(new Label(6,count_1,"0",wcf));
			sheet.addCell(new Label(7,count_1,df.format(fj_sum),wcf));
			sheet.addCell(new Label(8,count_1,df.format(Double.parseDouble(sum.toString())+Double.parseDouble(fj_sum.toString())),wcf));
			//签名、盖章处
			WritableFont f = new WritableFont(WritableFont.ARIAL,12,WritableFont.BOLD);
			WritableCellFormat w = new WritableCellFormat(f);
			w.setVerticalAlignment(VerticalAlignment.CENTRE);
			w.setAlignment(Alignment.CENTRE);
			sheet.mergeCells(0,count_1+1,3,count_1+2);
			sheet.mergeCells(4,count_1+1,9,count_1+2);
			Label l = new Label(0,0,"北汽幻速汽车责任有限公司-运费结算单",w); //标题
			sheet.addCell(l);
			sheet.addCell(new Label(0,count_1+1,"甲方审核",w));
			sheet.addCell(new Label(4,count_1+1,"乙方审核",w));
			//甲方经办人
			sheet.mergeCells(6,count_1+3,7,count_1+3);
			sheet.addCell(new Label(2,count_1+3,"经办人",wcf));
			//甲方财务审核、乙方经办人
			sheet.mergeCells(6,count_1+5,7,count_1+5);
			sheet.addCell(new Label(2,count_1+5,"财务审核",wcf));
			sheet.addCell(new Label(6,count_1+5,"经办人",wcf));
			//甲方业务审核
			sheet.mergeCells(6,count_1+7,7,count_1+7);
			sheet.addCell(new Label(2,count_1+7,"业务审核",wcf));
			//甲方物流处处长审核、乙方盖章
			sheet.mergeCells(6,count_1+9,7,count_1+9);
			sheet.addCell(new Label(2,count_1+9,"物流处处长审核",wcf));
			sheet.addCell(new Label(6,count_1+9,"盖章",wcf));
			wwkb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if (null != wwkb) {
				wwkb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	public static Object toNewExcel(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list,String name)
			throws Exception {
		System.out.println("come  in 111..........");
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet[] wsData=new jxl.write.WritableSheet[20];
			int pageSize=list.size()/60000;
			for(int j=-1;j<pageSize;j++){
				wsData[(j+1)] = wwb.createSheet("sheettest"+(j+1), (j+1));		
				if (head != null && head.length > 0) {
					for (int i = 0; i < head.length; i++) {
						wsData[(j+1)].addCell(new Label(i, 0, head[i]));
					}
				}
			}
			
			int counts=0;
			int flag=0;
			int f=0;
			for (int z = 1; z < list.size() + 1; z++) {
				int pageSizess=z/60000;
				counts++;
			
				flag=pageSizess;
				pageSizess=pageSizess<1?0:pageSizess;
				pageSizess=(new BigDecimal(pageSizess).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();
				String[] str = list.get(z - 1);
				
				
				
				
				for (int i = 0; i < str.length; i++) {
						wsData[f].addCell(new Label(i, counts, str[i]));
					
				}
				if(counts>=60000-1){
					f++;
					//z=0;
				//	z--;
					counts=0;
				}
				
				
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	//zhumingwei add by 2011-02-23
	public static Object toExcel111(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "三包结算统计表.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	//zhumingwei 2011-10-25
	public static Object toExcel222(ResponseWrapper response,RequestWrapper request, String[] head, List<String[]> list)throws Exception {
		String name = "经销商产品套餐维护明细.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	public static Object toExcel2(ResponseWrapper response,
			RequestWrapper request, String[] head, List list)
			throws Exception {

		String name = "轎車維修車輛數.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet[] wsData=new jxl.write.WritableSheet[20];
			
			int pageSize=list.size()/30000;
			for(int j=-1;j<pageSize;j++){
				wsData[(j+1)] = wwb.createSheet("sheettest"+(j+1), (j+1));					
			}
			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					wsData[0].addCell(new Label(i, 0, head[i]));
				}
			}
			
			int counts=0;
			int flag=0;
			for (int z = 1; z < list.size() + 1; z++) {
				int pageSizess=z/30000;
				counts++;
				if(pageSizess>flag){
					counts=1;
				}
				flag=pageSizess;
				System.out.println("KKKKK:"+z);
				pageSizess=pageSizess<1?0:pageSizess;
				pageSizess=(new BigDecimal(pageSizess).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();

				Map map=(Map)list.get(z-1);
				String  ROWNUM= String.valueOf(map.get("ROWNUM"));
				String VIN = String.valueOf(map.get("VIN"));
				//wsData[pageSizess].addCell(new Label(0, counts, ROWNUM));
				wsData[pageSizess].addCell(new Label(0, counts, VIN));
//				for (int i = 0; i < str.length; i++) {
//					wsData[pageSizess].addCell(new Label(i, z, str[i]));
//				}
			}
			
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}

	public static Object toPotentialCustomerExcel(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list,List<String[]> list2,List<String[]> list3,String name)
			throws Exception {

		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);
            
			 /**
			 * 定义单元格样式
			 */
		   WritableFont wf = new WritableFont(WritableFont.ARIAL, 11,
		     WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
		     jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
		   
		   WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10,
				     WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
				     jxl.format.Colour.BLACK); 
		   	   
		   WritableCellFormat wcf = new WritableCellFormat(wf); // 单元格定义
		   //wcf.setBackground(jxl.format.Colour.BLUE_GREY); // 设置单元格的背景颜色
		   wcf.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式
		   
		   WritableCellFormat wcf2 = new WritableCellFormat(wf2); 
		   wcf2.setAlignment(jxl.format.Alignment.CENTRE);
		   
		   ws.addCell(new Label(0,0,head[0], wcf));
		   ws.mergeCells(0, 0, head.length-1, 0); // 合并单元格
		   
		   int x = 2; //合并大区x坐标
		   int y = 0; //合并大区y坐标
		   
		   int[] total = new int[25]; //总计数据
		   
			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					if( null != head[i]){
					ws.addCell(new Label(i, 1, head[i+1],wcf)); 
				    }
				}
			}
			int counts=0;
			int flag=0;
			
			for(int s = 0;s<list3.size();s++){ //总计数据
				String[] org = list3.get(s);
				for(int i =3;i<org.length - 3;i++){
					total[i] = total[i] + Integer.parseInt(org[i]);	
				}
			}
					
			for (int z = 2; z < list.size() + 2; z++) {
				
				int pageSizess=z/30000;
				counts++;
				if(pageSizess>flag){
					counts=1;
				}
				flag=pageSizess;
				pageSizess=pageSizess<1?0:pageSizess;
				pageSizess=(new BigDecimal(pageSizess).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();			
					
			  for (int w=0;w<list.size();w++) {
				
				String[] str = list.get(w); //取一条记录				
				String[] str2 =  null ; //取下一条记录				
				if(list.size()>(w+1)){
					str2 = list.get(w+1);
				}
												
				for (int i = 0; i < str.length-3; i++) {					
					ws.addCell(new Label(i, z, str[i])); //写入记录数据												
				 }
 				if(null == str2 || !str[1].equals(str2[1])) {
 					++z;	
					ws.addCell(new Label(1, z, str[1]+"合计",wcf2));
					ws.mergeCells(1, z, 2, z); // 合并省份单元格	
					
					for(int j=0;j<list2.size();j++){
						String[] region = list2.get(j);
						if(str[1].equals(region[2])){
							for(int k=3;k<region.length;k++){
								ws.addCell(new Label(k, z, region[k]));   //写入省份合计		
							}
							
						}					
					}
			   }
				if(null == str2 || !str[0].equals(str2[0])){
					 ++z;
					 y = z -1 ;
					 ws.mergeCells(0, x, 0,y); // 合并大区单元格		
					 ws.addCell(new Label(0, z, "大区合计",wcf2)); 
					 ws.mergeCells(0, z, 2, z); // 合并区合计单元格			 
					 for(int k=0;k<list3.size();k++){
						 String[] org = list3.get(k); 
						 if(str[0].equals(org[2])){
							for (int t=3;t<org.length-3;t++) {
								ws.addCell(new Label(t, z, String.valueOf(org[t])));   //写入区合计								
							}							
					   }									 
				   } 
			   x=z+1;		 
			  }
			   ++z;	
		   } 		
		}
			 ws.addCell(new Label(0, list.size()+list3.size()+list2.size()+2, "总计",wcf2)); 
			 ws.mergeCells(0, list.size()+list3.size()+list2.size()+2, 2, list.size()+list3.size()+list2.size()+2); // 合并总计单元格
			 
			 for(int t=3;t< total.length;t++){						
					ws.addCell(new Label(t, list.size()+list3.size()+list2.size()+2, String.valueOf(total[t])));   //写入总计	
		     } 
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	public static Object toExceVender(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "供应商索赔价格.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	public static Object toExceLabourPrice(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "索赔工时单价设定.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	public static Object toExceLabourPrice1(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "预警规则模板.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	
	public static Object toExceDearle(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "活动维护经销商导入模板.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	
	
	
	
	
	public static Object toExceLabourPrice2(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "导入VIN.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	public static Object toExceUtil(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list,String name)
			throws Exception {

		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	/**
	 * 组板查询信息EXCEL导出
	 */
	public static Object toBoardExcel(ResponseWrapper response,RequestWrapper request,String[] head,List<Map<String,Object>> list,String name) throws Exception{
		jxl.write.WritableWorkbook wwkb = null;
		OutputStream out = null;
		try {	
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name,"utf-8"));
			out = response.getOutputStream();
			wwkb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet sheet = wwkb.createSheet("组板详细信息",0);
			//居中，size：10
			WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(Alignment.CENTRE);
			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					sheet.addCell(new Label(i, 0, head[i]));
				}
			}
			int count = 1;
			//结算单详细(按运单)
			if(list != null && list.size()>0){
				for(int i = 0 ; i < list.size() ; i++){
					Map<String, Object> map = list.get(i);
					sheet.addCell(new Label(0,i+1,String.valueOf(i+1),wcf));
					sheet.addCell(new Label(1,i+1,map.get("BO_NO").toString(),wcf));
					sheet.addCell(new Label(2,i+1,map.get("NAME").toString(),wcf));
					sheet.addCell(new Label(3,i+1,map.get("BO_DATE").toString(),wcf));
					sheet.addCell(new Label(4,i+1,map.get("BO_NUM").toString(),wcf));
					sheet.addCell(new Label(5,i+1,map.get("ALLOCA_NUM").toString(),wcf));
					sheet.addCell(new Label(6,i+1,map.get("OUT_NUM").toString(),wcf));
					sheet.addCell(new Label(7,i+1,map.get("SEND_NUM").toString(),wcf));
					sheet.addCell(new Label(8,i+1,map.get("ACC_NUM").toString(),wcf));
					count ++;
				}
			}
			wwkb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if (null != wwkb) {
				wwkb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	/**
	 * 经销商财务查询导出
	 */
	public static Object toDealerExcel(ResponseWrapper response,RequestWrapper request,List<Map<String,Object>> list_order,List<Map<String,Object>> list_out_order,List<Map<String,Object>> list_inout,List<Map<String,Object>> list_pro,int type,String areaName,String dealerName,String finTypeName) throws Exception{
		jxl.write.WritableWorkbook wwkb = null;
		String[] head1={"序号","订单号","使用人","使用时间","发票号","金额"};//订单
		String[] head2={"序号","订单号","使用人","使用时间","发票号","金额"};//中转库
		String[] head3={"序号","转入经销商","转款单号","凭证号","金额","转款人","转款时间","审核状态 "};//转款
		String[] head4={"序号","扣款金额","扣款时间","扣款人","承诺单号 "};//承诺
		OutputStream out = null;
		String  typeName="冻结";
		String heJi="合计";
		//导出excel名称
		String excelName="经销商账户"+finTypeName+typeName+"详细明细.xls";
		
		if(type==2){//使用
			typeName="使用";
			excelName="经销商账户"+finTypeName+typeName+"详细明细.xls";
		}
		try {	
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName,"utf-8"));
			out = response.getOutputStream();
			wwkb = Workbook.createWorkbook(out);
			
			//-------------------------------------------------------承诺Start-----------------------------//
			if(type==2){//使用
				
				jxl.write.WritableSheet sheet3 = wwkb.createSheet("经销商账户承诺"+typeName+"明细",0);
				//居中，size：10
				WritableFont font3 = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
				WritableCellFormat wcf3 = new WritableCellFormat(font3);
				wcf3.setAlignment(Alignment.CENTRE);
				double hj3=0.0;
				if (head4 != null && head4.length > 0) {
					for (int i = 0; i < head4.length; i++) {
						sheet3.addCell(new Label(i, 0, head4[i]));
					}
				}
				if(list_pro != null && list_pro.size()>0){
					for(int i = 0 ; i < list_pro.size() ; i++){
						Map<String, Object> map = list_pro.get(i);
						sheet3.addCell(new Label(0,i+1,String.valueOf(i+1),wcf3));
						sheet3.addCell(new Label(1,i+1,map.get("DED_AMOUNT").toString(),wcf3));
						sheet3.addCell(new Label(2,i+1,map.get("DED_DATE").toString(),wcf3));
						sheet3.addCell(new Label(3,i+1,map.get("NAME").toString(),wcf3));
						sheet3.addCell(new Label(4,i+1,map.get("PRO_NO").toString(),wcf3));
						hj3+=Double.parseDouble(map.get("DED_AMOUNT").toString());
					}
					//合计
					sheet3.addCell(new Label(0,list_pro.size()+1,heJi,wcf3));
					sheet3.addCell(new Label(1,list_pro.size()+1,String.valueOf(hj3),wcf3));
				}
				
			}
			//-------------------------------------------------------承诺end-----------------------------//	
			
			//-------------------------------------------------------转款Start-----------------------------//
			jxl.write.WritableSheet sheet2 = wwkb.createSheet("经销商账户转款"+typeName+"明细",0);
			//居中，size：10
			WritableFont font2 = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf2 = new WritableCellFormat(font2);
			wcf2.setAlignment(Alignment.CENTRE);
			double hj2=0.0;
			if (head3 != null && head3.length > 0) {
				for (int i = 0; i < head3.length; i++) {
					sheet2.addCell(new Label(i, 0, head3[i]));
				}
			}
			if(list_inout != null && list_inout.size()>0){
				for(int i = 0 ; i < list_inout.size() ; i++){
					Map<String, Object> map = list_inout.get(i);
					sheet2.addCell(new Label(0,i+1,String.valueOf(i+1),wcf2));
					sheet2.addCell(new Label(1,i+1,map.get("DEALER_NAME").toString(),wcf2));
					sheet2.addCell(new Label(2,i+1,map.get("ORDER_NO").toString(),wcf2));
					sheet2.addCell(new Label(3,i+1,map.get("EVIDENCE_NO").toString(),wcf2));
					sheet2.addCell(new Label(4,i+1,map.get("AMOUNT").toString(),wcf2));
					sheet2.addCell(new Label(5,i+1,map.get("NAME").toString(),wcf2));
					sheet2.addCell(new Label(6,i+1,map.get("CREATE_DATE").toString(),wcf2));
					sheet2.addCell(new Label(7,i+1,map.get("CODE_DESC").toString(),wcf2));
					hj2+=Double.parseDouble(map.get("USE_AMOUNT").toString());
				}
				//合计
				sheet2.addCell(new Label(3,list_inout.size()+1,heJi,wcf2));
				sheet2.addCell(new Label(4,list_inout.size()+1,String.valueOf(hj2),wcf2));
			}
			//-------------------------------------------------------转款end-----------------------------//
			
			//-------------------------------------------------------中转库end-----------------------------//
			jxl.write.WritableSheet sheet1 = wwkb.createSheet("经销商账户中转库"+typeName+"明细",0);
			//居中，size：10
			WritableFont font1 = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf1 = new WritableCellFormat(font1);
			wcf1.setAlignment(Alignment.CENTRE);
			double hj1=0.0;
			if (head2 != null && head2.length > 0) {
				for (int i = 0; i < head2.length; i++) {
					sheet1.addCell(new Label(i, 0, head2[i]));
				}
			}
			if(list_out_order != null && list_out_order.size()>0){
				for(int i = 0 ; i < list_out_order.size() ; i++){
					Map<String, Object> map = list_out_order.get(i);
					sheet1.addCell(new Label(0,i+1,String.valueOf(i+1),wcf1));
					sheet1.addCell(new Label(1,i+1,map.get("ORDER_NO").toString(),wcf1));
					sheet1.addCell(new Label(2,i+1,map.get("NAME").toString(),wcf1));
					sheet1.addCell(new Label(3,i+1,map.get("USE_DATE").toString(),wcf1));
					sheet1.addCell(new Label(4,i+1,map.get("INVOICE_NO").toString(),wcf1));
					sheet1.addCell(new Label(5,i+1,map.get("USE_AMOUNT").toString(),wcf1));
					hj1+=Double.parseDouble(map.get("USE_AMOUNT").toString());
				}
				//合计
				sheet1.addCell(new Label(4,list_out_order.size()+1,heJi,wcf1));
				sheet1.addCell(new Label(5,list_out_order.size()+1,String.valueOf(hj1),wcf1));
			}
			
			//-------------------------------------------------------中转库end-----------------------------//
			//-------------------------------------------------------订单end-----------------------------//
			jxl.write.WritableSheet sheet = wwkb.createSheet("经销商账户订单"+typeName+"明细",0);
			//居中，size：10
			WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(Alignment.CENTRE);
			double hj=0.0;
			if (head1 != null && head1.length > 0) {
				for (int i = 0; i < head1.length; i++) {
					sheet.addCell(new Label(i, 0, head1[i]));
				}
			}
			if(list_order != null && list_order.size()>0){
				for(int i = 0 ; i < list_order.size() ; i++){
					Map<String, Object> map = list_order.get(i);
					sheet.addCell(new Label(0,i+1,String.valueOf(i+1),wcf));
					sheet.addCell(new Label(1,i+1,map.get("ORDER_NO").toString(),wcf));
					sheet.addCell(new Label(2,i+1,map.get("NAME").toString(),wcf));
					sheet.addCell(new Label(3,i+1,map.get("USE_DATE").toString(),wcf));
					sheet.addCell(new Label(4,i+1,map.get("INVOICE_NO").toString(),wcf));
					sheet.addCell(new Label(5,i+1,map.get("USE_AMOUNT").toString(),wcf));
					hj+=Double.parseDouble(map.get("USE_AMOUNT").toString());
				}
				//合计
				sheet.addCell(new Label(4,list_order.size()+1,heJi,wcf));
				sheet.addCell(new Label(5,list_order.size()+1,String.valueOf(hj),wcf));
			}
			
			//-------------------------------------------------------订单end-----------------------------//
			wwkb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if (null != wwkb) {
				wwkb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param response
	 * @param request
	 * @param excelName 导出的报表EXCEL名字
	 * @param firstName EXCEL内容第一行名称
	 * @param excelHead EXCEL头部名称数组
	 * @param columns 需要从结果集中取值的字段名
	 * @param result  查询到的结果集
	 * @return
	 * @throws Exception
	 */
	public static Object toReportExcel(ResponseWrapper response,RequestWrapper request,String excelName,String[] excelHead,String[] columns,List<Map<String,Object>> result) throws Exception{
		jxl.write.WritableWorkbook wwkb = null;
		OutputStream out = null;
		try {	
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName,"utf-8"));
			out = response.getOutputStream();
			wwkb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet sheet = wwkb.createSheet(excelName,0);
			//居中，size：10
			WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(Alignment.CENTRE);
			if (excelHead != null && excelHead.length > 0) {
				for (int i = 0; i < excelHead.length; i++) {
					sheet.addCell(new Label(i, 0, excelHead[i],wcf));
					sheet.setColumnView(i, 10);
				}
			}
			if(result != null && result.size()>0){
				for(int i = 0 ; i < result.size() ; i++){
					Map<String, Object> map = result.get(i);
					for(int j = 0 ; j < excelHead.length ; j++){
						//System.out.println("..."+j+"..."+map.get(columns[j]));
						if (CheckUtil.checkFormatNumber1(map.get(columns[j])==null?"":map.get(columns[j]).toString())&&!excelHead[j].equals("证件号码")) {
							sheet.addCell(new jxl.write.Number(j, i+1, Double.parseDouble(map.get(columns[j])==null?"":map.get(columns[j]).toString())));
	                    } else {
	                    	sheet.addCell(new Label(j,i+1,map.get(columns[j])==null?"":map.get(columns[j]).toString(),wcf));
	                    }
//						sheet.addCell(new Label(j,i+1,map.get(columns[j])==null?"":map.get(columns[j]).toString(),wcf));
					}	
				}
			}
			
			wwkb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if (null != wwkb) {
				wwkb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param response
	 * @param request
	 * @param excelName 导出的报表EXCEL名字
	 * @param firstName EXCEL内容第一行名称
	 * @param excelHead EXCEL头部名称数组
	 * @param columns 需要从结果集中取值的字段名
	 * @param result  查询到的结果集
	 * @param constantMap 翻译常量map
	 * @return
	 * @throws Exception
	 */
	public static Object toReportExcelMapTransfer(ResponseWrapper response,
			RequestWrapper request, String excelName, String[] excelHead,
			String[] columns, List<Map<String, Object>> result,
			HashMap<Integer, HashMap> constantMap) throws Exception {
		jxl.write.WritableWorkbook wwkb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(excelName, "utf-8"));
			out = response.getOutputStream();
			wwkb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet sheet = wwkb.createSheet(excelName, 0);
			// 居中，size：10
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(Alignment.CENTRE);
			if (excelHead != null && excelHead.length > 0) {
				for (int i = 0; i < excelHead.length; i++) {
					sheet.addCell(new Label(i, 0, excelHead[i], wcf));
					sheet.setColumnView(i, 10);
				}
			}
			if (result != null && result.size() > 0) {
				for (int i = 0; i < result.size(); i++) {
					Map<String, Object> map = result.get(i);
					for (int j = 0; j < excelHead.length; j++) {
						if (constantMap != null) {
							if (constantMap.containsKey(j)) {
								Map<Integer, String> cMap = constantMap.get(j);
								sheet.addCell(new Label(j, i + 1, map
										.get(columns[j]) == null ? "" : cMap
										.get(Integer.parseInt(map.get(
												columns[j]).toString())), wcf));
							} else
								sheet.addCell(new Label(j, i + 1, map
										.get(columns[j]) == null ? "" : map
										.get(columns[j]).toString(), wcf));
						} else
							sheet.addCell(new Label(j, i + 1, map
									.get(columns[j]) == null ? "" : map.get(
									columns[j]).toString(), wcf));
					}
				}
			}

			wwkb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwkb) {
				wwkb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	public static WritableCellFormat getTitleformat(Colour bgColor, Alignment alignment) {
		WritableCellFormat font = null;
		try {
			WritableFont wfont = new WritableFont(
					WritableFont.createFont("宋体"), 9);
			font = new WritableCellFormat(wfont);
			wfont.setBoldStyle(WritableFont.BOLD);
			font.setBackground(bgColor);
			font.setAlignment(alignment);
			font.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GRAY_50);

			font.setWrap(true);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return font;
	}
	
	public static WritableCellFormat getTitleformat2(Colour bgColor, Alignment alignment) {
		WritableCellFormat font = null;
		try {
			WritableFont wfont = new WritableFont(
					WritableFont.createFont("宋体"), 20);
			font = new WritableCellFormat(wfont);
			wfont.setBoldStyle(WritableFont.BOLD);
			font.setBackground(bgColor);
			font.setAlignment(alignment);
			font.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GRAY_50);

			font.setWrap(true);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return font;
	}
	
	public static WritableCellFormat getTotalformat(Colour bgColor, Alignment alignment) {
		WritableCellFormat font = null;
		try {
			WritableFont wfont = new WritableFont(
					WritableFont.ARIAL,10);
			font = new WritableCellFormat(wfont);
			wfont.setBoldStyle(WritableFont.NO_BOLD);
			font.setBackground(bgColor);
			font.setAlignment(alignment);
			font.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GRAY_50);

			font.setWrap(true);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return font;
	}
	  
	/**
	 * 
	 * @param response
	 * @param request
	 * @param excelName 导出的报表EXCEL名字
	 * @param firstName EXCEL内容第一行名称
	 * @param excelHead EXCEL头部名称数组
	 * @param columns 需要从结果集中取值的字段名
	 * @param result  查询到的结果集
	 * @return
	 * @throws Exception
	 */
	public static Object toChanelReportExcel(ResponseWrapper response,RequestWrapper request,String excelName,String[] excelHead,String[] columns,List<Map<String,Object>> result) throws Exception{
		jxl.write.WritableWorkbook wwkb = null;
		OutputStream out = null;
		try {	
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName,"utf-8"));
			out = response.getOutputStream();
			wwkb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet sheet = wwkb.createSheet(excelName,0);
			//居中，size：10
			WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			
			
			wcf.setAlignment(Alignment.CENTRE);
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
			if (excelHead != null && excelHead.length > 0) {
				for (int i = 9; i < excelHead.length; i++) {
					sheet.addCell(new Label(i, 0, excelHead[i],wcf));
					sheet.setColumnView(i, 10);
				}
				WritableCellFormat titlef = getTitleformat(Colour.ICE_BLUE, Alignment.JUSTIFY);
			    WritableCellFormat titlef1 = getTitleformat(Colour.ICE_BLUE, Alignment.CENTRE);
				//合并前7列的头两行
				for(int t=0;t<9;t++){
					sheet.mergeCells(t, 0, t, 1);
					sheet.addCell(new Label(t, 0, excelHead[t],titlef1));
				}
				
				sheet.mergeCells(9, 0, 24, 0);
				sheet.addCell(new Label(9, 0, "来源网站",titlef1));
				for(int l=9;l<25;l++){
					sheet.addCell(new Label(l, 1, excelHead[l],titlef));
				}
				/*sheet.mergeCells(0, 0, 0, 1);
				sheet.mergeCells(1, 0, 1, 1);
				sheet.mergeCells(2, 0, 2, 1);
				sheet.mergeCells(3, 0, 3, 1);
				sheet.mergeCells(4, 0, 4, 1);
				sheet.mergeCells(5, 0, 5, 1);
				sheet.mergeCells(6, 0, 6, 1);
				sheet.mergeCells(7, 0, 7, 1);
				sheet.addCell(new Label(0, 0, excelHead[0],wcf));
				sheet.addCell(new Label(1, 0, excelHead[1],wcf));
				sheet.addCell(new Label(2, 0, excelHead[2],wcf));
				sheet.addCell(new Label(3, 0, excelHead[3],wcf));
				sheet.addCell(new Label(4, 0, excelHead[4],wcf));
				sheet.addCell(new Label(5, 0, excelHead[5],wcf));
				sheet.addCell(new Label(6, 0, excelHead[6],wcf));
				sheet.addCell(new Label(7, 0, excelHead[7],wcf));
				*/
			}
			if(result != null && result.size()>0){
				for(int i = 0 ; i < result.size() ; i++){
					Map<String, Object> map = result.get(i);
					for(int j = 0 ; j < excelHead.length ; j++){
						System.out.println("..."+j+"..."+map.get(columns[j]));
						sheet.addCell(new Label(j,i+2,map.get(columns[j])==null?"":map.get(columns[j]).toString(),wcf));
					}	
				}
			}
			
			wwkb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if (null != wwkb) {
				wwkb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	/**
	 *
	 * @param response
	 * @param request
	 * @param excelName
	 * @param excelHead
	 * @param columns
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static Object toDayReportExcel(ResponseWrapper response,RequestWrapper request,String excelName,String[] excelHead,String[] columns,List<Map<String,Object>> result) throws Exception{
		jxl.write.WritableWorkbook wwkb = null;
		OutputStream out = null;
		try {	
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName,"utf-8"));
			out = response.getOutputStream();
			wwkb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet sheet = wwkb.createSheet(excelName,0);
			//居中，size：10
			WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			
			
			wcf.setAlignment(Alignment.CENTRE);
			if (excelHead != null && excelHead.length > 0) {
				for (int i = 9; i < excelHead.length; i++) {
					sheet.addCell(new Label(i, 0, excelHead[i],wcf));
					sheet.setColumnView(i, 10);
				}
				WritableCellFormat titlef = getTitleformat(Colour.ICE_BLUE, Alignment.JUSTIFY);
			    WritableCellFormat titlef1 = getTitleformat(Colour.ICE_BLUE, Alignment.CENTRE);
				//合并第1-7列的头3行
				for(int t=1;t<7;t++){
					sheet.mergeCells(t, 1, t, 2);
					sheet.addCell(new Label(t, 1, excelHead[t],titlef1));
				}
				//合并第11列-14列
				for(int t=10;t<14;t++){
					sheet.mergeCells(t, 1, t, 2);
					sheet.addCell(new Label(t, 1, excelHead[t],titlef1));
				}
				//合并第18-20列
				for(int t=17;t<21;t++){
					sheet.mergeCells(t, 1, t, 2);
					sheet.addCell(new Label(t, 1, excelHead[t],titlef1));
				}
				//合并第25-30列
				for(int t=24;t<30;t++){
					sheet.mergeCells(t, 1, t, 2);
					sheet.addCell(new Label(t, 1, excelHead[t],titlef1));
				}
				
				//合并第二行的7-9列
				sheet.mergeCells(7, 1, 9, 1);//来电咨询的关注车型
				sheet.addCell(new Label(7, 1, "关注车型",titlef1));
				
				//合并第二行的14-16列
				sheet.mergeCells(14, 1, 16, 1);//来电咨询的关注车型
				sheet.addCell(new Label(14, 1, "关注车型",titlef1));
				
				//合并第二行的22-24列
				sheet.mergeCells(21, 1, 23, 1);//来电咨询的关注车型
				sheet.addCell(new Label(21, 1, "关注车型",titlef1));
				
				//合并第二行的30-32列
				sheet.mergeCells(30, 1, 32, 1);//来电咨询的关注车型
				sheet.addCell(new Label(30, 1, "关注车型",titlef1));
				
			    sheet.mergeCells(1, 0, 9, 0);
				sheet.addCell(new Label(1, 0, "来电咨询",titlef1));
				sheet.mergeCells(10, 0, 16, 0);
				sheet.addCell(new Label(10, 0, "在线咨询",titlef1));
				sheet.mergeCells(17, 0, 23, 0);
				sheet.addCell(new Label(17, 0, "数字营销转",titlef1));
				sheet.mergeCells(24, 0, 32, 0);
				sheet.addCell(new Label(24, 0, "总计",titlef1));
				sheet.mergeCells(0, 0, 0, 2);//0列 0行 0列 2行
				sheet.addCell(new Label(0, 0, "日期\\内容",titlef1));
				
				for(int l=1;l<33;l++){
					sheet.addCell(new Label(l, 2, excelHead[l],titlef));
				}
				/*sheet.mergeCells(0, 0, 0, 1);
				sheet.mergeCells(1, 0, 1, 1);
				sheet.mergeCells(2, 0, 2, 1);
				sheet.mergeCells(3, 0, 3, 1);
				sheet.mergeCells(4, 0, 4, 1);
				sheet.mergeCells(5, 0, 5, 1);
				sheet.mergeCells(6, 0, 6, 1);
				sheet.mergeCells(7, 0, 7, 1);
				sheet.addCell(new Label(0, 0, excelHead[0],wcf));
				sheet.addCell(new Label(1, 0, excelHead[1],wcf));
				sheet.addCell(new Label(2, 0, excelHead[2],wcf));
				sheet.addCell(new Label(3, 0, excelHead[3],wcf));
				sheet.addCell(new Label(4, 0, excelHead[4],wcf));
				sheet.addCell(new Label(5, 0, excelHead[5],wcf));
				sheet.addCell(new Label(6, 0, excelHead[6],wcf));
				sheet.addCell(new Label(7, 0, excelHead[7],wcf));
				*/
			}
			if(result != null && result.size()>0){
				for(int i = 0 ; i < result.size() ; i++){
					Map<String, Object> map = result.get(i);
					for(int j = 0 ; j < excelHead.length ; j++){
						System.out.println("..."+j+"..."+map.get(columns[j]));
						sheet.addCell(new Label(j,i+3,map.get(columns[j])==null?"":map.get(columns[j]).toString(),wcf));
					}	
				}
			}
			
			wwkb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if (null != wwkb) {
				wwkb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	/**
	 * 
	* @Title: dealerInOutQueryReportExcel 
	* @author: xyfue
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param response
	* @param @param request
	* @param @param excelName
	* @param @param excelHead
	* @param @param columns
	* @param @param result
	* @param @return
	* @param @throws Exception    设定文件 
	* @date 2014年11月10日 下午8:13:52 
	* @return Object    返回类型 
	* @throws
	 */
	public static Object dealerInOutQueryReportExcel(ResponseWrapper response,RequestWrapper request,String excelName,String[] excelHead,String[] columns,List<Map<String,Object>> result,String reportDate,int curMonth) throws Exception{
		jxl.write.WritableWorkbook wwkb = null;
		OutputStream out = null;
		try {	
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName,"utf-8"));
			out = response.getOutputStream();
			wwkb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet sheet = wwkb.createSheet(excelName,0);
			//居中，size：10
			WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			
			int headNum = excelHead.length;
			
			wcf.setAlignment(Alignment.CENTRE);
			WritableCellFormat titlef = getTotalformat(Colour.YELLOW, Alignment.CENTRE);
			if (excelHead != null && excelHead.length > 0) {
				for (int i = 9; i < excelHead.length; i++) {
					sheet.addCell(new Label(i, 0, excelHead[i],wcf));
					sheet.setColumnView(i, 10);
				}
				
			    WritableCellFormat titlef1 = getTitleformat(Colour.WHITE, Alignment.CENTRE);
			    WritableCellFormat titlef2 = getTitleformat2(Colour.WHITE, Alignment.LEFT);
				//以下是设置表头，以及为表头赋值*******************begin ****************************
			    //合并第1-8列的头2到3行
				for(int t=0;t<=7;t++){
					sheet.mergeCells(t, 1, t, 2);
					sheet.addCell(new Label(t, 1, excelHead[t],titlef1));
				}
				
				sheet.mergeCells(8, 1, 11, 1);
				sheet.addCell(new Label(8, 1, "回款(万元)",titlef1));
				for(int t=8;t<=11;t++){
					sheet.mergeCells(t, 2, t, 2);
					sheet.addCell(new Label(t, 2, excelHead[t],titlef1));
				}
				
				sheet.mergeCells(12, 1, 23, 1);
				sheet.addCell(new Label(12, 1, "订单审核(辆)",titlef1));
				for(int t=12;t<=23;t++){
					sheet.mergeCells(t, 2, t, 2);
					sheet.addCell(new Label(t, 2, excelHead[t],titlef1));
				}
				
				sheet.mergeCells(24, 1, 40, 1);
				sheet.addCell(new Label(24, 1, " 累计在途(辆)",titlef1));
				for(int t=24;t<=40;t++){
					sheet.mergeCells(t, 2, t, 2);
					sheet.addCell(new Label(t, 2, excelHead[t],titlef1));
				}
				
				int monthSalesBegin = 41;
				int monthSalesEnd = monthSalesBegin+(curMonth+5)+3+1;//31
				sheet.mergeCells(monthSalesBegin, 1, monthSalesEnd, 1);
				sheet.addCell(new Label(monthSalesBegin, 1, "累计实销(辆)",titlef1));
				for(int t=monthSalesBegin;t<=monthSalesEnd;t++){
					sheet.mergeCells(t, 2, t, 2);
					sheet.addCell(new Label(t, 2, excelHead[t],titlef1));
				}
				
				// 本月验收新增
				sheet.mergeCells(monthSalesEnd+1, 1, monthSalesEnd+7+1, 1);
				sheet.addCell(new Label(monthSalesEnd+1, 1, "本月验收新增",titlef1));
				for(int t=monthSalesEnd+1;t<=monthSalesEnd+8;t++){
					sheet.mergeCells(t, 2, t, 2);
					sheet.addCell(new Label(t, 2, excelHead[t],titlef1));
				}
				
				sheet.mergeCells(monthSalesEnd+9, 1, monthSalesEnd+16, 1);
				sheet.addCell(new Label(monthSalesEnd+9, 1, "库存(辆)",titlef1));
				for(int t=monthSalesEnd+8+1;t<=monthSalesEnd+16;t++){
					sheet.mergeCells(t, 2, t, 2);
					sheet.addCell(new Label(t, 2, excelHead[t],titlef1));
				}
				sheet.mergeCells(monthSalesEnd+17, 1, monthSalesEnd+23, 1);
				sheet.addCell(new Label(monthSalesEnd+17, 1, "经销商订单(辆)",titlef1));
				for(int t=monthSalesEnd+17;t<=monthSalesEnd+23;t++){
					sheet.mergeCells(t, 2, t, 2);
					sheet.addCell(new Label(t, 2, excelHead[t],titlef1));
				}
				
				int monthOrderBegin = monthSalesEnd+24;//38
				int monthOrderEnd = monthOrderBegin+curMonth+3+1;//52
				sheet.mergeCells(monthOrderBegin, 1, monthOrderEnd, 1);
				sheet.addCell(new Label(monthOrderBegin, 1, "任务完成(辆)",titlef1));
				for(int t=monthOrderBegin;t<=monthOrderEnd;t++){
					sheet.mergeCells(t, 2, t, 2);
					sheet.addCell(new Label(t, 2, excelHead[t],titlef1));
				}
				
				sheet.mergeCells(monthOrderEnd+1, 1, monthOrderEnd+6, 1);
				sheet.addCell(new Label(monthOrderEnd+1, 1, "任务完成率",titlef1));
				for(int t = monthOrderEnd+1; t <= monthOrderEnd+6;t++){
					sheet.mergeCells(t, 2, t, 2);
					sheet.addCell(new Label(t, 2, excelHead[t],titlef1));
				}
				
				for(int t=monthOrderEnd+7;t<=monthOrderEnd+7;t++){
					sheet.mergeCells(t, 1, t, 2);
					sheet.addCell(new Label(t, 1, excelHead[t],titlef1));
				}
				
				sheet.mergeCells(monthOrderEnd+8, 1, monthOrderEnd+9, 1);
				sheet.addCell(new Label(monthOrderEnd+8, 1, "三方信贷",titlef1));
				for(int t=monthOrderEnd+8;t<=monthOrderEnd+9;t++){
					sheet.mergeCells(t, 2, t, 2);
					sheet.addCell(new Label(t, 2, excelHead[t],titlef1));
				}
				
				sheet.mergeCells(monthOrderEnd+10, 1, monthOrderEnd+11, 1);
				sheet.addCell(new Label(monthOrderEnd+10, 1, "建店验收",titlef1));
				for(int t=monthOrderEnd+10; t<= monthOrderEnd+11;t++){
					sheet.mergeCells(t, 2, t, 2);
					sheet.addCell(new Label(t, 2, excelHead[t],titlef1));
				}
				
				for(int t=monthOrderEnd+12; t<=monthOrderEnd+12;t++){
					sheet.mergeCells(t, 1, t, 2);
					sheet.addCell(new Label(t, 1, excelHead[t],titlef1));
				}
				
			    sheet.mergeCells(0, 0, headNum-1, 0);
				sheet.addCell(new Label(0, 0, "北汽幻速经销商进销存表 截止到"+reportDate,titlef2));
				//以下是设置表头，以及为表头赋值*******************end ****************************
				
			}
			int cosNum = 1;
			if(result != null && result.size()>0){
				for(int i = 0 ; i < result.size() ; i++){
					Map<String, Object> map = result.get(i);
					for(int j = 0 ; j < excelHead.length ; j++){
						System.out.println("..."+j+"..."+map.get(columns[j]));
						if( j == 0){
							if("2".equals(map.get("ORDER_TYPE"))||"3".equals(map.get("ORDER_TYPE"))){
								sheet.addCell(new Label(j,i+3,"",titlef));
							}else{
								sheet.addCell(new Label(j,i+3,String.valueOf(cosNum),wcf));
								cosNum = cosNum + 1;
							}
						}else{
							
							if("2".equals(map.get("ORDER_TYPE"))||"3".equals(map.get("ORDER_TYPE"))){
								//如果是数字则导出表格式为数字
								if (CheckUtil.checkFormatNumber1(map.get(columns[j])==null?"":map.get(columns[j]).toString())) {
									sheet.addCell(new jxl.write.Number(j,i+3, Double.parseDouble(map.get(columns[j]).toString()),titlef));
			                    } else {
			                    	sheet.addCell(new Label(j,i+3, map.get(columns[j])==null?"":map.get(columns[j]).toString(),titlef));
			                    }
							}else{
								if (CheckUtil.checkFormatNumber1(map.get(columns[j])==null?"":map.get(columns[j]).toString())) {
									sheet.addCell(new jxl.write.Number(j,i+3, Double.parseDouble(map.get(columns[j]).toString()),wcf));
			                    } else {
			                    	sheet.addCell(new Label(j,i+3, map.get(columns[j])==null?"":map.get(columns[j]).toString(),wcf));
			                    }
							}
						}
						
					}	
				}
			}
			
			wwkb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if (null != wwkb) {
				wwkb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	/**
	 *
	 * @param response
	 * @param request
	 * @param excelName
	 * @param excelHead
	 * @param columns
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static Object VehicleInfoReportExcel(ResponseWrapper response,RequestWrapper request,String excelName,String[] excelHead,String[] columns,List<Map<String,Object>> result) throws Exception{
		jxl.write.WritableWorkbook wwkb = null;
		OutputStream out = null;
		try {	
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName,"utf-8"));
			out = response.getOutputStream();
			wwkb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet sheet = wwkb.createSheet(excelName,0);
			//居中，size：10
			WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			
				WritableCellFormat titlef = getTitleformat(Colour.ICE_BLUE, Alignment.JUSTIFY);
				
				for(int l=0;l<excelHead.length;l++){
					sheet.addCell(new Label(l, 0, excelHead[l],titlef));
				}
				int flag = 0;
				List<String> str1 = new ArrayList<String>();	
				List<int[]> str2 = new ArrayList<int[]>();
			if(result != null && result.size()>0){
				for(int i = 0 ; i < result.size() ; i++){
					Map<String, Object> map = result.get(i);
					for(int j = 0 ; j < excelHead.length ; j++){
						if(j == 0){
							str1.add(CommonUtils.checkNull(map.get(columns[0])));
						}
						sheet.addCell(new Label(j,i+1,map.get(columns[j])==null?"":map.get(columns[j]).toString(),wcf));
					}
				}
			}
			str1.add("");
			int str[] = null;
			for(int k = 0 ; k < str1.size() ; k++){
				if(k>0){
					if(str1.get(k).equals(str1.get(k-1))){
						flag++;
					}else{
						str =new int[]{k,flag};
						str2.add(str);
						flag=0;
					}
				}
			}
			for (int i = 0; i < str2.size(); i++) {
				if(str2.get(i)[1]>0){
					sheet.mergeCells(0,(str2.get(i)[0]+1)-(str2.get(i)[1]+1), 0,(str2.get(i)[0]));
				}
			}
			wwkb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if (null != wwkb) {
				wwkb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
}
