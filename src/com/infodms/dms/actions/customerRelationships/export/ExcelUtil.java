package com.infodms.dms.actions.customerRelationships.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.infodms.dms.util.DateTimeUtil;
import com.infoservice.mvc.context.ResponseWrapper;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 
* @ClassName: ExcelUtil 
* @Description: TODO(投诉信息Excel导入导出类) 
* @author liuqiang 
* @date Sep 13, 2010 9:56:38 PM 
*
 */
public class ExcelUtil {
	//Excel表头
	private static final String[] titles = {"大区", "创建时间", "请求编号", "信息来源", "用户名", 
			"省份", "城市", "电话", "请求类型", "问题代码", "问题汇总", "呼叫中心处理情况", "被投经销商", "重要程度", "车型", 
			"跟踪情况", "是否处理完", "处理人", "备件编码", "上级保供单位", "是否需要总部支持", "预计处理完成时间", "投诉下发时间",
			"首次回复时间", "处理完时间", "处理周期", "是否关闭", "是否满意", "不满意的原因", "回访人", "须回复周期", "备注",
			"回访时间", "回访未能关闭", "技术室处理情况", "技术室处理人"
	};
	//投诉信息导出文件名
	private static final String fileName = "投诉信息.xls";
	/**
	 * 
	* @Title: impExcel 
	* @Description: TODO(解析Excel) 
	* @param @param is 要解析的文件
	* @param @param start 从第start列开始解析
	* @param @param end  解析到end列
	* @param @throws Exception    设定文件 
	* @return List<Map<String,String>>    Excel表头和内容映射的集合
	* @throws
	 */
	public static List<Map<String, String>> impExcel(InputStream is, int start, int end) throws Exception {
		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		Workbook workbook = Workbook.getWorkbook(is); 
		Sheet sheet = workbook.getSheet(0);
		int rowSize = sheet.getRows(); //行数
        //int colSize = sheet.getColumns(); //列数  
        for (int i = 1; i < rowSize; i++) {
        	System.out.println("第" + i + "行");
        	Map<String, String> map = new HashMap<String, String>();
        	for (int j = start; j <= end; j++) {
            	Cell til = sheet.getCell(j, 0);  //Excel标题
        		Cell con = sheet.getCell(j, i);   //Excel内容
            	String title = til.getContents().trim();
            	String content = "";
            	if (con.getType() == CellType.DATE) {
            		//解析日期类型 jxl会把日期类型自动解析成 yy-MM-dd
            		DateCell datec11 = (DateCell) con;
                    Date date = datec11.getDate();
                    content = DateTimeUtil.parseDateToDate(date);//yyyy-MM-dd
            	} else {
            		content = con.getContents().trim();
            	}
        		System.out.println("title == " + title + ", content = " + content);
        		map.put(title, content);
        	}
        	maps.add(map);
        }
        return maps;
	}
	
	public static void expExcel(ResponseWrapper response, List<Map<String, Object>> maps) throws Exception {
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			response.setContentType("application/x-rar-compressed");
			response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
		    WritableSheet sheet = workbook.createSheet("投诉信息", 0);
		    genHead(sheet);
		    genBody(sheet, maps);
		    workbook.write(); 
		    workbook.close();
		    os.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != os) {
				os.close();
			}
		}
	}
	
	private static void expExcel(OutputStream os, List<Map<String, Object>> maps) throws Exception {
		WritableWorkbook workbook = Workbook.createWorkbook(os);
	    WritableSheet sheet = workbook.createSheet("投诉信息", 0);
	    genHead(sheet);
	    genBody(sheet, maps);
	    workbook.write(); 
	    workbook.close();
	}
	
	private static void genHead(WritableSheet sheet) throws Exception {
		Label label;
		for (int i = 0; i < titles.length; i++) {
			//Label(列号,行号 ,内容 )
			label = new jxl.write.Label(i + 1, 0, titles[i]); //跳过第一列
			sheet.addCell(label); 
		}
	}
	
	private static void genBody(WritableSheet sheet, List<Map<String, Object>> maps) throws Exception {
		int row  = maps.size();//Excel内容行数
		for (int i = 0; i < row; i++) {
			Map<String, Object> map = maps.get(i);
			Label label = null;
			for (int j = 0; j < titles.length; j++) {
				//Label(列号,行号 ,内容 )
				label = new jxl.write.Label(j + 1, i + 1, getContent(j + 1, map));//跳过第一行 第一列
				sheet.addCell(label); 
			}
		}
	}
	
	private static String getContent(int col, Map<String, Object> map) {
		switch (col) {
			case 1 : 
				return Obj2Str(map.get("ORG_NAME"));		//大区
			case 2 :
				return Obj2Str(map.get("CREATE_DATE"));		//创建时间
			case 3 : 
				return Obj2Str(map.get("COMP_CODE"));		//投诉编号
			case 4 :
				return Obj2Str(map.get("COMP_SOURCE"));     //信息来源
			case 5 : 
				return Obj2Str(map.get("LINK_MAN"));        //用户名
			case 6 :
				return Obj2Str(map.get("PROVINCE"));		//省份
			case 7 : 
				return Obj2Str(map.get("CITY"));			//城市
			case 8 :
				return Obj2Str(map.get("TEL"));     		//电话
			case 9 : 
				return Obj2Str(map.get("COM_TYPE"));		//请求类型
			case 10 :
				return Obj2Str(map.get("COMP_TYPE"));       //问题代码
			case 11 : 
				return Obj2Str(map.get("COMP_CONTENT"));    //问题汇总
			case 12 :
				return Obj2Str(map.get("CALL_CEN_CON"));    //呼叫中心处理情况
			case 13 : 
				return Obj2Str(map.get("DEALER_CODE") != null ? map.get("DEALER_CODE") + "." + map.get("DEALER_NAME")
						: "");     //被投经销商 CODE.NAME
			case 14 :
				return Obj2Str(map.get("COMP_LEVEL"));		//重要程度
			case 15 : 
				return Obj2Str(map.get("MODEL_CODE"));      //车型
			case 16 :
				return Obj2Str(map.get("AUDIT_CONTENT"));	//跟踪情况
			case 17 : 
				return Obj2Str(map.get("AUDIT_RESULT"));	//是否处理完
			case 18 :
				return Obj2Str(map.get("NAME"));     		//处理人
			case 19 : 
				return Obj2Str(map.get("PART_CODE"));		//备件编码
			case 20 :
				return Obj2Str(map.get("SUPPLIER"));		//上级保供单位
			case 21 : 
				return "";									//是否需要总部支持
			case 22 :				
				return "";									//预计处理完成时间
			case 23 : 
				return Obj2Str(map.get("SEND_DATE"));       //投诉下发时间
			case 24 :
				return Obj2Str(map.get("FIRST_CALL_DATE")); //首次回复时间
			case 25 : 
				return Obj2Str(map.get("HANDLE_DATE"));     //处理完时间
			case 26 :
				return Obj2Str(map.get("DAYS_BET"));        //处理周期
			case 27 : 
				return Obj2Str(map.get("SHUT"));			//是否关闭
			case 28 :
				return Obj2Str(map.get("SATISFIED"));		//是否满意
			case 29 : 
				return Obj2Str(map.get("CAUSE"));			//不满意的原因
			case 30 :
				return Obj2Str(map.get("CALL_PERSON"));		//回访人
			case 31 : 
				return Obj2Str(map.get("CALL_CYCLE"));		//须回复周期
			case 32 :
				return Obj2Str(map.get("REMARK")); 			//备注
			case 33 :
				return Obj2Str(map.get("CALL_DATE")); 		//回访时间
			case 34 : 
				return Obj2Str(map.get("CALL_FAIL"));		//回访未能关闭
			case 35 :
				return Obj2Str(map.get("SUPPORT"));			//技术室处理情况
			case 36 : 
				return Obj2Str(map.get("SUPPORT_PERSON"));	//技术室处理人
			default : 
				return"";
		}
	}
	
	private static String Obj2Str(Object obj) {
		return null == obj ? "" : obj.toString();
	}
	
	public static void main(String[] args) throws Exception {
		OutputStream os = new FileOutputStream(new File("c:\\test.xls"));
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 3; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ORG_NAME", i);
			map.put("CREATE_DATE", i);
			maps.add(map);
		}
		ExcelUtil eu = new ExcelUtil();
		eu.expExcel(os, maps);
	}
}
