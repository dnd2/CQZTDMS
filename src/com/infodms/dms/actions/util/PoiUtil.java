package com.infodms.dms.actions.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;


public class PoiUtil {
	private HSSFWorkbook workbook;
	private POIFSFileSystem   fs   =   null; 

	public PoiUtil(File excelFile) throws FileNotFoundException,
			IOException {
		workbook = new HSSFWorkbook(new FileInputStream(excelFile));
	}
	public PoiUtil(InputStream fs) throws FileNotFoundException,
		IOException {
			workbook = new HSSFWorkbook(fs);
	}
	
	public  PoiUtil(FileInputStream fis) throws FileNotFoundException,
	IOException {
		fs   =   new   POIFSFileSystem(fis);
		workbook = new HSSFWorkbook(fs);
}

	/**
	 * 获得表中的数据
	 * @param sheetNumber 表格索引(EXCEL 是多表文档,所以需要输入表索引号)
	 * @return 由LIST构成的行和表
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<List> getDatasInSheet(int sheetNumber)
			throws FileNotFoundException, IOException {
		List<List> result = new ArrayList<List>();

		//获得指定的表
		HSSFSheet sheet = workbook.getSheetAt(sheetNumber);

		//获得数据总行数
		int rowCount = sheet.getLastRowNum();
		if (rowCount < 1) {
			return result;
		}
		
		//逐行读取数据
		int columnCount=0;//已头做为列数
		for (int rowIndex = 0; rowIndex <= rowCount; rowIndex++) {

			//获得行对象
			HSSFRow row = sheet.getRow(rowIndex);

			if (row != null) {

				List<Object> rowData = new ArrayList<Object>();

				//获得本行中单元格的个数
				//int columnCount = row.getLastCellNum(); old20110727
				if(rowIndex==0){
					columnCount = row.getLastCellNum();
				}				
				
				System.out.println("--rowIndex+columnCount--"+rowIndex+"-"+columnCount);
				//获得本行中各单元格中的数据
				for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
					
					HSSFCell cell = row.getCell(columnIndex);

					//获得指定单元格中数据
					Object cellStr = this.getCellString(cell);

					rowData.add(cellStr);

				}

				result.add(rowData);
			}
		}
		return result;
	}

	/**
	 * 获得单元格中的内容
	 * @param cell
	 * @return
	 */
	protected Object getCellString(HSSFCell cell) {
		Object result = null;
		java.text.DecimalFormat	formatter=new java.text.DecimalFormat("########.####");//update by wchq 因为从EXCEL导入是数据的超过8为就会格式化如8.8888888E7(应该得到数字88888888才
		if (cell != null) {

			int cellType = cell.getCellType();

			switch (cellType) {

			case HSSFCell.CELL_TYPE_STRING:
				result = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				  if (DateUtil.isCellDateFormatted(cell)) {
					    result = cell.getDateCellValue();
			        } else {
			        	result = formatter.format(cell.getNumericCellValue());
			        }
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				result = formatter.format(cell.getNumericCellValue());
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				result = null;
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				result = cell.getBooleanCellValue();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				result = null;
				break;
			}
		}
		return result;
	}
	
	/******** 导 出 ********/

	// 定制日期格式
	private static String DATE_FORMAT = " m/d/yy "; // "m/d/yy h:mm"

	// 定制浮点数格式
	private static String NUMBER_FORMAT = " #,##0.00 ";

	private String xlsFileName;

	private HSSFSheet sheet;

	private HSSFRow row;

	/** */
	/**
	 * 初始化Excel
	 * 
	 * @param fileName
	 *            导出文件名
	 */
	public PoiUtil() {
		//this.xlsFileName = fileName;
		this.workbook = new HSSFWorkbook();
		this.sheet = workbook.createSheet();
	}
	public PoiUtil(String fileName) {
		this.xlsFileName = fileName;
		this.workbook = new HSSFWorkbook();
		this.sheet = workbook.createSheet();
	}
	public HSSFWorkbook getWorkbook(){
		return this.workbook;
	}

	/** */
	/**
	 * 导出Excel文件
	 * 
	 * @throws XLSException
	 */
	public void exportXLS() throws Exception {
		try {
			FileOutputStream fOut = new FileOutputStream(xlsFileName);
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			throw new Exception(" 生成导出Excel文件出错! ", e);
		} catch (IOException e) {
			throw new Exception(" 写入Excel文件出错! ", e);
		}

	}
	

	
	
	
	
	
	
	

	/** */
	/**
	 * 增加一行
	 * 
	 * @param index
	 *            行号
	 */
	public void createRow(int index) {
		this.row = this.sheet.createRow(index);
	}

	/** */
	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, String value) {
		HSSFCell cell = row.createCell(index);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);
	}

	/** */
	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, Calendar value) {
		HSSFCell cell = row.createCell(index);
		cell.setCellValue(value.getTime());
		HSSFCellStyle cellStyle = workbook.createCellStyle(); // 建立新的cell样式
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT)); // 设置cell样式为定制的日期格式
		cell.setCellStyle(cellStyle); // 设置该cell日期的显示格式
	}

	/** */
	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, int value) {
		HSSFCell cell = row.createCell(index);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
	}

	/** */
	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, double value) {
		HSSFCell cell = row.createCell(index);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		HSSFCellStyle cellStyle = workbook.createCellStyle(); // 建立新的cell样式
		HSSFDataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat(NUMBER_FORMAT)); // 设置cell样式为定制的浮点数格式
		cell.setCellStyle(cellStyle); // 设置该cell浮点数的显示格式
	}
	/**
	 * 创建通用二级标题
	 * 
	 */
	public void createNormalHead(int index,String headString, int colSum) { 
		
		HSSFRow row = sheet.createRow(index); 

		HSSFCell cell = row.createCell(0);
		row.setHeight((short) 400); 

		// 定义单元格为字符串类型 
		cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
		cell.setCellValue(headString); 

		// 指定合并区域 
		CellRangeAddress cra = new CellRangeAddress(index,index, 0, colSum);
		sheet.addMergedRegion(cra);

		HSSFCellStyle cellStyle = workbook.createCellStyle(); 
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐 
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐 
		cellStyle.setWrapText(true);// 指定单元格自动换行 

		// 设置单元格字体 
		HSSFFont font = workbook.createFont(); 
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); 
		font.setFontName("宋体"); 
		font.setFontHeight((short) 300); 
		cellStyle.setFont(font); 

		cell.setCellStyle(cellStyle); 
		} 


}
