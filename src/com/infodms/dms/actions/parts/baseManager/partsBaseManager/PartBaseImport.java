package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class PartBaseImport {

	private FileInputStream fileInputStream;
	private Map<String, Cell[]> cellMap;
	private ByteArrayInputStream byteArrayInputStream;
	private List<Map> mapList=new ArrayList<Map>();
	private List<ExcelErrors> errList=new ArrayList<ExcelErrors>();
	private boolean isEmptyFile;

	

	/**
	 * 前台的form中,<input type="file" name="uploadFile" /> name写死=inputFileName
	 * maxColumn最大列数
	 * blankRows充许最多连续空行数
	 * maxSize充许文件大小
	 * @param request
	 * @throws Exception 
	 */
	public int insertIntoTmp(RequestWrapper request, String inputFileName,int maxColumn,int blankRows,long maxSize) throws Exception {
		FileObject uploadFile = request.getParamObject(inputFileName);//获取导入文件
		if(uploadFile==null){//文件为空报空指针异常
			ExcelErrors error=new ExcelErrors();
			error.setRowNum(new Integer(0));
			error.setErrorDesc("文件不能为空");
			errList.add(error);
			return 3;
		}
			String fileName = uploadFile.getFileName();//获取文件名
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());//截取文件名
			ByteArrayInputStream is = new ByteArrayInputStream(uploadFile.getContent());//获取文件数据
			
			int errNum=checkFile(is, maxColumn, blankRows,maxSize);
			if((mapList==null||mapList.size()==0)&&errNum==0){
				ExcelErrors error=new ExcelErrors();
				error.setRowNum(new Integer(0));
				error.setErrorDesc("文件不能为空");
				errList.add(error);
				errNum=3;
			}else{
				setMapList(mapList);
				setIsEmptyFile();
				if(isEmptyFile&&errList.size()==0){
					ExcelErrors error=new ExcelErrors();
					error.setRowNum(new Integer(0));
					error.setErrorDesc("文件不能为空");
					errList.add(error);
					errNum=3;
				}
				setErrList(errList);
			}
		return errNum;
	}

	/**
	 * 校验文件 errorNum错误号，0无错误，1 列过多，2 空行大于maxRows 3 文件为空
	 * @param inputStream
	 * @param maxColumn
	 * @param maxRows
	 * @return
	 * @throws Exception
	 */
	public int checkFile(InputStream inputStream,int maxColumn,int maxRows,long maxSize) throws Exception {
		Sheet sheet = null;
		//errorNum错误号，0无错误，1 列过多，2 空行大于maxRows 4 文件读取错误
		int errorNum=0;
		try {
			Workbook wb = Workbook.getWorkbook(inputStream);
			inputStream.reset();
			int len=inputStream.read();
			if(len>maxSize){//判断解析文件的大小
				ExcelErrors error=new ExcelErrors();
				error.setRowNum(new Integer(0));
				error.setErrorDesc("文件大小不能超过"+maxSize);
				errList.add(error);
				return 5;
			}
			// 如果多页则遍历
			Sheet[] sheets = wb.getSheets();
			int length = sheets.length;
			for (int i = 0; i < sheets.length; i++) {
				sheet = sheets[i];
				int size = sheet.getColumns();
				errorNum=checkSheet(sheet,maxColumn,maxRows);//判断空行不能超过最多允许，跨列不能超过最高允许行数，并依次将列插入list
				if(errorNum!=0){
					break;
				}
			}
			// 如果只有一页,只选择第一页
			// sheet=wb.getSheet(0);
			
		} catch (BiffException e) {
			e.printStackTrace();
			errorNum = 4;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return errorNum;
	}

	/**
	 * 校验sheet,空行不能多于maxRows，列数不能超过maxRows，并将所有Cell存入list
	 * @param sheet
	 * @param maxColumn
	 * @param maxRows
	 * @return
	 * @throws Exception
	 */
	private int checkSheet(Sheet sheet,int maxColumn,int maxRows) throws Exception {
		int errorNum=0;
		int firstRow = 1;
		int emptyRows = 0;
		int colNum = sheet.getColumns();
		if (colNum != maxColumn&&colNum!=0) {
		  ExcelErrors error=new ExcelErrors();
		  error.setRowNum(new Integer(0));
		  error.setErrorDesc("文件列数不正确");
		  errList.add(error);
		  errorNum=1;
		}
		int totalRows = sheet.getRows();
		//校验文件头是否有过多空行，并定位第一个非空行位置
		for (int i = 0; i < totalRows; i++) {
			Cell[] cells = sheet.getRow(i);
			if (isEmptyRow(cells)) {
				emptyRows++;
				if (emptyRows > maxRows) {
					ExcelErrors error=new ExcelErrors();
					error.setRowNum(new Integer(0));
					error.setErrorDesc("连续的空行不能大于三行");
					errList.add(error);
					errorNum=2;
					break;
				}
			}else{
				firstRow=i;
				break;
			}
		}
		if(errorNum!=0){
			return errorNum;
		}
		LinkedHashMap<String,Cell[]> map = new LinkedHashMap<String, Cell[]>();
		emptyRows=0;
		//JXL从0行开始读文件，第一行，为头标题，跳过不解析
		for (int j = firstRow+1; j < totalRows; j++) {
			Cell[] cells = sheet.getRow(j);
			if(isEmptyRow(cells)){//判断第一行是否为空数据
				emptyRows++;
				if (emptyRows > maxRows) {//判断连续空格是否大于三行
					ExcelErrors error=new ExcelErrors();
					error.setRowNum(new Integer(0));
					error.setErrorDesc("连续的空行不能大于三行");
					errList.add(error);
					errorNum=2;
					break;
				}else{
					continue;
				}
			}
			//解析Cell[]存在Map中
			map.put(j+1+"", cells);
		}
		mapList.add(map);
		return errorNum;
	}

	// 判断是否是空行
	private boolean isEmptyRow(Cell[] cells) {
		boolean bl = true;
		Cell cell = null;
		for (int i = 0; i < cells.length; i++) {
			cell = cells[i];
			if (cell.getContents().trim().length() > 0) {
				bl = false;
				break;
			}
		}
		return bl;
	}
   /*
    * 校验文件是否为空
    * 如果不忽略标题行if(null!=map&&map.size()>0)
    */
	private void setIsEmptyFile(){
		isEmptyFile=true;
		if(null==mapList){
			isEmptyFile=true;
		}else{
			Map map=null;
			for(int i=0;i<mapList.size();i++){
				map=mapList.get(i);
				if(null!=map&&map.size()>=1){
					isEmptyFile=false;
				}
			}
		}
	}
	public boolean getIsEmptyFile(){
		return isEmptyFile;
	}
	public ByteArrayInputStream getByteArrayInputStream() {
		return byteArrayInputStream;
	}

	public void setByteArrayInputStream(ByteArrayInputStream byteArrayInputStream) {
		this.byteArrayInputStream = byteArrayInputStream;
	}
	
	public List<Map> getMapList() {
		return mapList;
	}

	public void setMapList(List<Map> mapList) {
		this.mapList = mapList;
	}

	public Map<String, Cell[]> getCellMap() {
		return cellMap;
	}

	public void setCellMap(Map<String, Cell[]> cellMap) {
		this.cellMap = cellMap;
	}

	public FileInputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(FileInputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	public List<ExcelErrors> getErrList() {
		return errList;
	}

	public void setErrList(List<ExcelErrors> errList) {
		this.errList = errList;
	}
	public static void main(String[] args){
		String s="我a要kill那只鸟";
		System.out.println(s.length());
	}


}
