package com.infodms.dms.actions.claim.serviceActivity;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;

import flex.messaging.io.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class BaseImport {

	private FileInputStream fileInputStream;
	private Map<String, Cell[]> cellMap;
	private ByteArrayInputStream byteArrayInputStream;
	private List<Map> mapList=new ArrayList();

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

	/**
	 * 前台的form中,<input type="file" name="uploadFile" /> name写死=inputFileName
	 * maxColumn最大列数
	 * blankRows充许最多空行数
	 * maxSize充许文件大小
	 * @param request
	 * @throws Exception 
	 */
	public int insertIntoTmp(RequestWrapper request, String inputFileName,int maxColumn,int blankRows,long maxSize) throws Exception {
		FileObject uploadFile = request.getParamObject(inputFileName);
		if(uploadFile==null){
			throw new Exception("读取错误");
		}
			String fileName = uploadFile.getFileName();
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
			ByteArrayInputStream is = new ByteArrayInputStream(uploadFile.getContent());
			
			int errNum=checkFile(is, maxColumn, blankRows,maxSize);
			if((mapList==null||mapList.size()==0)&&errNum==0){
				errNum=3;
			}else{
				setMapList(mapList);
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
		//errorNum错误号，0无错误，1 列过多，2 空行大于maxRows 3 文件读取错误
		int errorNum=0;
		try {
			Workbook wb = Workbook.getWorkbook(inputStream);
			inputStream.reset();
			int len=inputStream.read();
			if(len==0){
				return 4;
			}
			if(len>maxSize){
				return 5;
			}
			// 如果多页则遍历
			Sheet[] sheets = wb.getSheets();
			for (int i = 0; i < sheets.length; i++) {
				sheet = sheets[i];
				errorNum=checkSheet(sheet,maxColumn,maxRows);
				if(errorNum!=0){
					break;
				}
			}
			// 如果只有一页,只选择第一页
			// sheet=wb.getSheet(0);
			
		} catch (BiffException e) {
			e.printStackTrace();
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
		if (colNum > maxColumn) {
		  errorNum=1;
		}
		int totalRows = sheet.getRows();
		//校验文件头是否有过多空行，并定位第一个非空行位置
		/**
		 * 由于第一行为tittle，所以这里从第二行开始取值
		 */
		for (int i = 1; i < totalRows; i++) {
			Cell[] cells = sheet.getRow(i);
			if (isEmptyRow(cells)) {
				emptyRows++;
				if (emptyRows > maxRows) {
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
		Map<String,Cell[]> map=new HashMap<String, Cell[]>();
		emptyRows=0;
		//JXL从0行开始读文件，第一行，为头标题，跳过不解析
		for (int j = firstRow; j < totalRows; j++) {
			Cell[] cells = sheet.getRow(j);
			if(isEmptyRow(cells)){
				emptyRows++;
				if (emptyRows > maxRows) {
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

	public ByteArrayInputStream getByteArrayInputStream() {
		return byteArrayInputStream;
	}

	public void setByteArrayInputStream(ByteArrayInputStream byteArrayInputStream) {
		this.byteArrayInputStream = byteArrayInputStream;
	}

}
