package com.infodms.dms.util.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.infodms.dms.util.CheckUtil;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

import jxl.write.Label;

/**
 * 使用POI工具包对EXCEL操作的工具类
 * @author chenyu
 *
 */
public class ExcelUtil {
	
	/**
	 * 将输入流读取为2007版本的workbook
	 * @param is 输入流
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public static Workbook getWorkbook2007(InputStream is){
		try {
			Workbook wb = new XSSFWorkbook(is);
			return wb;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("读取excel文件异常!");
		}
	}

	/**
	 * 将输入流读取为2003版本的workbook
	 * @param is 输入流
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public static Workbook getWorkbook2003(InputStream is){
		try {
			Workbook wb = new HSSFWorkbook(is);
			return wb;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("读取excel文件异常!");
		}
	}
	
	/**
	 * 根据文件名读取输入流返回workbook
	 * @param filename
	 * @param is
	 * @return 如果返回null则表示既不是2007的excel也不是2003的excel
	 * @author chenyub@yonyou.com
	 */
	public static Workbook getWorkbookByIS(String filename,InputStream is){
		if (filename.toLowerCase().endsWith(".xls")) {
			return getWorkbook2003(is);
		} else if (filename.toLowerCase().endsWith(".xlsx")) {
			return getWorkbook2007(is);
		} else {
			return null;
		}
	}
	
	/**
	 * 追加行,单元格内容为list的值
	 * @param sheet
	 * @param values
	 * @author chenyub@yonyou.com
	 */
	public static void appendRow(Sheet sheet,String[] values){
		int rn = sheet.getPhysicalNumberOfRows();
		Row nrow = sheet.createRow(rn++);
		int i=0;
		for (String str : values) {
			Cell cell = nrow.createCell(i++);
			cell.setCellValue(str);
		} 
	}
	

	/**
	 * 读取日期值
	 * @param cell
	 * @return
	 * @throws ExceptionUtil
	 */
	public static Date getDateValue(Cell cell)  {
		if (null == cell||Cell.CELL_TYPE_BLANK == cell.getCellType()) {
			return null;
		}
		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()
				&& DateUtil.isCellDateFormatted(cell)) {
			return cell.getDateCellValue();
		} else {
			throw new RuntimeException("读取日期格式出现错误!");
		}
	}

	/**
	 * 读取数值
	 * @param cell
	 * @return
	 * @throws ExceptionUtil
	 */
	public static BigDecimal getNumericValue(Cell cell) {
		if (null == cell||Cell.CELL_TYPE_BLANK == cell.getCellType()) {
			return null;
		}
		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			return new BigDecimal(cell.getNumericCellValue());
		} else {
			throw new RuntimeException("读取数字格式出现错误!");
		}
	}

	/**
	 * 读取字符串
	 * @param cell
	 * @return
	 * @throws ExceptionUtil
	 */
	public static String getStringValue(Cell cell,String cellName) {
		try {
			return getStringValue(cell);
		} catch (Exception e) {
			throw new RuntimeException("读取字符格式出现错误:" + cellName);
		}
	}
	/**
	 * 读取字符串
	 * @param cell
	 * @return
	 * @throws ExceptionUtil
	 */
	public static String getStringValue(Cell cell) {
		if (null == cell||Cell.CELL_TYPE_BLANK == cell.getCellType()) {
			return null;
		}
		if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
			return cell.getStringCellValue();
		} else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			return String.valueOf(cell.getNumericCellValue());
		}else {
			throw new RuntimeException("读取字符格式出现错误!");
		}
	}
	
	
	public static Object exportEx(ResponseWrapper response,RequestWrapper request, String[] head, List<String[]> list,String workName)throws Exception {

		String name = workName;
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = jxl.Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize = list.size() / 30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					/* ws.addCell(new Label(i, z, str[i])); */// modify by yuan
					if (CheckUtil.checkFormatNumber1(str[i] == null ? "": str[i])) {
						ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
					} else {
						ws.addCell(new Label(i, z, str[i]));
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
}
