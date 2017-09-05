package com.infodms.dms.common.tag;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

/**
 * 基本方法积累
 * 
 * @author yuewei
 * 
 */
public class BaseUtils {

	public static final String SendUrl = "com.infodms.dms.actions.";

	public static String checkNull(String str) {
		return str != null ? str : "";
	}

	public static Long parseLong(String s) {
		return Long.valueOf(s);
	}

	/**
	 * 判断是否为空或NULL
	 * 
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public static boolean testString(String src) {
		if (src != null && !"".equals(src.trim())
				&& !"null".equalsIgnoreCase(src.trim()))
			return true;
		return false;
	}

	public static boolean notNull(String str) {
		return !(str == null || "".equals(str.trim()));
	}

	public static String printDate(String format, Date date) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	public static String printDateTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);

	}
	/**
	 * string转换成Date
	 * 
	 * @param index
	 *            , i
	 * @return
	 * @throws Exception
	 */
	public static Date getDate(String index, int i) throws Exception {
		DateFormat formatter;
		Date d = new Date();
		if (index == null || index.trim().equals("")) {
			return null;
		}
		if (i == 0) // for filename
		{
			formatter = new SimpleDateFormat("yyyy-MM-dd-HH");
		} else if (i == 1) {
			formatter = new SimpleDateFormat("yyyy-MM-dd");
		} else if (i == 2) {
			formatter = new SimpleDateFormat("yyyy/MM/dd");
		} else if (i == 3) {
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		} else if (i == 4) {
			formatter = new SimpleDateFormat("HH:mm");
		} else if (i == 5) {
			formatter = new SimpleDateFormat("HH:mm:ss");
		} else if (i == 6) {
			formatter = new SimpleDateFormat("yyyy");
		} else if (i == 7) {
			formatter = new SimpleDateFormat("MM");
		} else if (i == 8) {
			formatter = new SimpleDateFormat("dd");
		} else if (i == 9) {
			formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		} else if (i == 10) {
			formatter = new SimpleDateFormat("yyyyMMdd");
		} else if (i == 11) {
			formatter = new SimpleDateFormat("yyyy-MM");
		} else {
			formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		}
		d = formatter.parse(index);
		return d;
	}

	/**
	 * 转化系统时间
	 */
	public static String getSystemDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date());
	}

	/**
	 * 转化系统时间
	 */
	public static String getSystemDateStr2() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * 转化系统时间
	 */
	public static String getSystemDateStr1() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	public static Integer ConvertInt(String str) {
		if (StringUtil.isNull(str)) {
			return null;
		}
		return Integer.parseInt(str);
	}

	public static Long ConvertLong(String str) {
		if (StringUtil.isNull(str)) {
			return null;
		}
		return Long.parseLong(str);
	}

	public static Double ConvertDouble(String str) {
		if (StringUtil.isNull(str)) {
			return null;
		}
		return Double.parseDouble(str);
	}

	/**
	 * 转换 xx,xx,xx
	 * 
	 * @param paramValues
	 * @return
	 */
	public static String Convert(String[] paramValues) {
		StringBuffer sb = JoinInstance();
		if (paramValues == null) {
			return "";
		}
		int len = paramValues.length;
		if (len == 1) {
			sb.append(paramValues[0]);
		}
		if (len > 1) {
			for (int i = 0; i < len; i++) {
				if (i == len - 1) {
					sb.append(paramValues[i]);
				} else {
					sb.append(paramValues[i] + ",");
				}
			}
		}
		return sb.toString();
	}

	public static String checkNull(Object obj) {
		if (obj != null)
			return obj.toString();
		else
			return "";
	}

	/**
	 * 拼接的Str静态创建
	 * 
	 * @return
	 */
	private static StringBuffer JoinInstance() {
		return new StringBuffer();
	}

	/**
	 * @param response
	 * @param request
	 * @param head
	 * @param list
	 * @param name
	 * @param name2
	 * @param strSet
	 * @return
	 * @throws Exception
	 */
	public static Object toExcel(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list,
			String name, String name2, List<Map<Integer, String>> listCount)
			throws Exception {

		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet(name2, 0);
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(Alignment.CENTRE);

			WritableFont font1 = new WritableFont(WritableFont.ARIAL, 12,
					WritableFont.NO_BOLD);
			WritableCellFormat wcf1 = new WritableCellFormat(font1);
			wcf1.setAlignment(Alignment.CENTRE);
			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i], wcf1));
					CellView cellView = new CellView();
					cellView.setAutosize(true); // 设置自动大小
					ws.setColumnView(i, 20);// 根据内容自动设置列宽
				}
			}
			int size = 0;
			if (list != null) {
				size = list.size();
			}
			if (list != null && size >= 0) {
				int length = 0;
				// int pageSize=size/30000;
				for (int z = 1; z < size + 1; z++) {
					String[] str = list.get(z - 1);
					length = str.length;
					for (int i = 0; i < length; i++) {
						ws.addCell(new Label(i, z, str[i], wcf));
					}
				}
				if (listCount != null && listCount.size() > 0) {
					for (Map<Integer, String> map : listCount) {
						Set<Entry<Integer, String>> entrySet = map.entrySet();
						for (Entry<Integer, String> entry : entrySet) {
							Integer key = entry.getKey();
							String value = entry.getValue();
							ws.addCell(new Label(key, size + 1, value, wcf));
						}
					}
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
	
	public static Object toExcelForRebate(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list,
			String name, String name2, List<Map<Integer, String>> listCount)
			throws Exception {

		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet(name2, 0);
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(Alignment.CENTRE);

			WritableFont font1 = new WritableFont(WritableFont.ARIAL, 12,
					WritableFont.NO_BOLD);
			WritableCellFormat wcf1 = new WritableCellFormat(font1);
			wcf1.setAlignment(Alignment.CENTRE);
			int flag = 0;
			List<String> str1 = new ArrayList<String>();	
			List<int[]> str2 = new ArrayList<int[]>();
			
			int flag1 = 0;
			List<String> str3 = new ArrayList<String>();	
			List<int[]> str4 = new ArrayList<int[]>();
			
			int flag2 = 0;
			List<String> str5 = new ArrayList<String>();	
			List<int[]> str6 = new ArrayList<int[]>();
			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i], wcf1));
					CellView cellView = new CellView();
					cellView.setAutosize(true); // 设置自动大小
					ws.setColumnView(i, 20);// 根据内容自动设置列宽
				}
			}
			int size = 0;
			if (list != null) {
				size = list.size();
			}
			if (list != null && size >= 0) {
				int length = 0;
				// int pageSize=size/30000;
				for (int z = 1; z < size + 1; z++) {
					String[] str = list.get(z - 1);
					length = str.length;
					for (int i = 0; i < length; i++) {
						ws.addCell(new Label(i, z, str[i], wcf));
					}
				}
				for (int z = 1; z < size + 1; z++) {
					for (int i = 0; i < size; i++) {
						if(z == 1){
							str1.add(CommonUtils.checkNull(list.get(i)[0]));
						}
						if(z == 2){
							str3.add(CommonUtils.checkNull(list.get(i)[1]));
						}
						if(z == 3){
							str5.add(CommonUtils.checkNull(list.get(i)[2]));
						}
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
					ws.mergeCells(0,(str2.get(i)[0]+1)-(str2.get(i)[1]+1), 0,(str2.get(i)[0]));
				}
			}
			
			str3.add("");
			int str11[] = null;
			for(int k = 0 ; k < str3.size() ; k++){
				if(k>0){
					if(str3.get(k).equals(str3.get(k-1))){
						flag1++;
					}else{
						str11 =new int[]{k,flag1};
						str4.add(str11);
						flag1=0;
					}
				}
			}
			for (int i = 0; i < str4.size(); i++) {
				if(str4.get(i)[1]>0){
					ws.mergeCells(1,(str4.get(i)[0]+1)-(str4.get(i)[1]+1), 1,(str4.get(i)[0]));
				}
			}
			str5.add("");
			int str22[] = null;
			for(int k = 0 ; k < str5.size() ; k++){
				if(k>0){
					if(str5.get(k).equals(str5.get(k-1))){
						flag2++;
					}else{
						str22 =new int[]{k,flag2};
						str6.add(str22);
						flag2=0;
					}
				}
			}
			for (int i = 0; i < str6.size(); i++) {
				if(str6.get(i)[1]>0){
					ws.mergeCells(2,(str6.get(i)[0]+1)-(str6.get(i)[1]+1), 2,(str6.get(i)[0]));
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
}
