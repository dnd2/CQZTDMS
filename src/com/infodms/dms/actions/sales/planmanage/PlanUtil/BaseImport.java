package com.infodms.dms.actions.sales.planmanage.PlanUtil;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.write.Label;

import com.infodms.dms.common.Constant;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class BaseImport {

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
				}
				setErrList(errList);
			}
		return errNum;
	}

	/**
     * 前台的form中,<input type="file" name="uploadFile" /> <script>alert(11);</script> name写死=inputFileName<br>
     * maxColumn最大列数<br>
     * blankRows充许最多连续空行数<br>
     * maxSize充许文件大小<br>
     * * sheetNum 控制程序最多读取几个sheet，1代表1个，2代表2个<br>
     *
     * @param request
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	public int insertIntoTmp(RequestWrapper request, String inputFileName, int maxColumn, int blankRows, long maxSize, int sheetNum) throws Exception {
        FileObject uploadFile = request.getParamObject(inputFileName);//获取导入文件
        if (uploadFile == null) {//文件为空报空指针异常
            ExcelErrors error = new ExcelErrors();
            error.setRowNum(new Integer(0));
            error.setErrorDesc("文件不能为空");
            errList.add(error);
            return 3;
        }
        String fileName = uploadFile.getFileName();//获取文件名
        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());//截取文件名
        ByteArrayInputStream is = new ByteArrayInputStream(uploadFile.getContent());//获取文件数据
System.out.println("====aaa=======");
        int errNum = checkFile(is, maxColumn, blankRows, maxSize, sheetNum);

System.out.println("====bbb======="+errNum);
        if ((mapList == null || mapList.size() == 0) && errNum == 0) {
            ExcelErrors error = new ExcelErrors();
            error.setRowNum(new Integer(0));
            error.setErrorDesc("文件不能为空");
            errList.add(error);
            errNum = 3;
        } else {
            setMapList(mapList);
            setIsEmptyFile();
            if (isEmptyFile && errList.size() == 0) {
                ExcelErrors error = new ExcelErrors();
                error.setRowNum(new Integer(0));
                error.setErrorDesc("文件不能为空");
                errList.add(error);
            }
            setErrList(errList);
        }

System.out.println("====ccc======="+errNum);
        List<Map> list = getMapList();
		for (int i = 0; i < list.size(); i++) {
			Map dataMap = list.get(i);
			if(dataMap==null || dataMap.isEmpty()){
				errNum = 3;
				break;
			}
		}

System.out.println("====ddd======="+errNum);      
        return errNum;
    }

    /**
     * 校验文件 errorNum错误号，0无错误，1 列过多，2 空行大于maxRows 3 文件为空
     *
     * @param inputStream
     * @param maxColumn
     * @param maxRows
     * @param maxSize     文件大小
     * @param sheetNum    读取几个sheet
     * @return
     * @throws Exception
     */
    public int checkFile(InputStream inputStream, int maxColumn, int maxRows, long maxSize, int sheetNum) throws Exception {
        Sheet sheet = null;
        //errorNum错误号，0无错误，1 列过多，2 空行大于maxRows 3 文件读取错误
        int errorNum = 0;
        try {
            Workbook wb = Workbook.getWorkbook(inputStream);
            inputStream.reset();
            int len = inputStream.read();
            if (len > maxSize) {//判断解析文件的大小
                ExcelErrors error = new ExcelErrors();
                error.setRowNum(new Integer(0));
                error.setErrorDesc("文件大小不能超过" + maxSize);
                errList.add(error);
                return 5;
            }
            // 如果多页则遍历
            Sheet[] sheets = wb.getSheets();
            int length = sheets.length;
            for (int i = 0; i < sheets.length && i < sheetNum; i++) {
                sheet = sheets[i];
                int size = sheet.getColumns();
                errorNum = checkSheet(sheet, maxColumn, maxRows);//判断空行不能超过最多允许，跨列不能超过最高允许行数，并依次将列插入list
                if (errorNum != 0) {
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
		System.out.println(colNum);
		System.out.println(maxColumn);
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
		Map<String,Cell[]> map=new HashMap<String, Cell[]>();
		emptyRows=0;
		//JXL从0行开始读文件，第一行，为头标题，跳过不解析
		for (int j = firstRow+1; j < totalRows; j++) {
			//Cell[] cells = sheet.getRow(j); //getRow是以每一行最后一个有值的字段为准的，如果最后一列没有值是不会包含在里面的
			Cell[] cells = getCells(sheet, j, maxColumn);
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
	
	/**
	 * 根据行列方式获取数据(防止一行中最后一列为空(不填写任何数据)的时候出现数组越界的情况)
	 * 
	 * @param sheet
	 * @param rows 当前行数
	 * @param maxColumn 列数
	 * @return
	 */
	public Cell[] getCells(Sheet sheet, int rows, int maxColumn) {
		Cell[] cells = new Cell[maxColumn];
		for (int i = 0; i < maxColumn; i++) {
			cells[i] = sheet.getCell(i, rows);
		}
		return cells;
	}
	
	// 判断是否是空行
	protected boolean isEmptyRow(Cell[] cells) {
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
	
	/**
	 * 前台的form中,<input type="file" name="uploadFile" /> name写死=inputFileName
	 * maxColumn最大列数
	 * blankRows充许最多连续空行数
	 * maxSize充许文件大小
	 * @param request
	 * @throws Exception 
	 */
	public int insertIntoTmpMore(RequestWrapper request, String inputFileName,int maxColumn,int blankRows,long maxSize,int type) throws Exception {
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
			
			int errNum=checkFileMore(is, maxColumn, blankRows,maxSize,type);
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
	public int checkFileMore(InputStream inputStream,int maxColumn,int maxRows,long maxSize,int type) throws Exception {
		Sheet sheet = null;
		//errorNum错误号，0无错误，1 列过多，2 空行大于maxRows 3 文件读取错误
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
			if(Constant.EXPORT_MORE_SHEET==type){
				for (int i = 0; i < sheets.length; i++) {
					sheet = sheets[i];
					int size = sheet.getColumns();
					errorNum=checkSheet(sheet,maxColumn,maxRows);//判断空行不能超过最多允许，跨列不能超过最高允许行数，并依次将列插入list
					if(errorNum!=0){
						break;
					}
				}
			}else{
				sheet = sheets[0];//选择第一个SHEET
				errorNum=checkSheet(sheet,maxColumn,maxRows);//判断空行不能超过最多允许，跨列不能超过最高允许行数，并依次将列插入list
			}
			
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
     * 分页导出excel<br>
     * 调用示例：DirectReport.java 类 的 pExportApplianceSalesInfo方法
     *
     * @param response
     * @param fileName  文件名称，如：xxx.xls
     * @param listHead  标题
     * @param listKey   关键字，获取数据里相应的值
     * @param listData  要输出的数据
     * @param sheetSize 每个sheet显示的最大条数(每页不要超过六万，超过打不开)
     * @throws Exception 抛出异常
     * @author fanzhineng
     * @time 2016-05-30
     */
    public void pagingExportExcel(ResponseWrapper response, String fileName, List<String> listHead, List<String> listKey, List<Map<String, Object>> listData, int sheetSize) throws Exception {
        OutputStream os = response.getOutputStream();
        WritableWorkbook workbook = Workbook.createWorkbook(os);
        try {

            //导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            //创建
            if (listData != null && !listData.isEmpty()) {
                int dataSize = listData.size();//数据条数
                double dataSizeTemp = dataSize;
                double sheetSizeTemp = sheetSize;
                double ptemp = Math.ceil(dataSizeTemp / sheetSizeTemp);
                int page = (int) ptemp;//多少页（向上取整）

                //创建多个sheet
                for (int i = 0; i < page; i++) {
                    workbook.createSheet("sheet_" + (i + 1), i);
                }

                for (int i = 0; i < page; i++) {
                    WritableSheet sheet = workbook.getSheet(i);
                    //写入标题
                    for (int j = 0; j < listHead.size(); j++) {
                        listHead.get(j);
                        sheet.addCell(new Label(j, 0, listHead.get(j)));
                    }
                    /*---写入数据start---*/
                    int tempSize = 0;
                    if (dataSize > sheetSize) {
                        tempSize = (i + 1) * sheetSize;
                        if (tempSize > dataSize) {
                            tempSize = dataSize;
                        }
                    } else {
                        tempSize = dataSize;
                    }
                    for (int k = (i * sheetSize); k < tempSize; k++) {

                        Map<String, Object> map = listData.get(k);
                        for (int r = 0; r < listKey.size(); r++) {
                            //Label("r第几个单元格","((k + 1)-(i*sheetSize))从哪一行开始","CommonUtils.checkNull(map.get(listKey.get(r))))写入什么值")
                            if (CheckUtil.checkFormatNumber1(CommonUtils.checkNull(map.get(listKey.get(r))) == null ? "" : CommonUtils.checkNull(map.get(listKey.get(r))))) {
                                if(Double.valueOf(CommonUtils.checkNull(map.get(listKey.get(r))))>Constant.Doubel_base){
                                    sheet.addCell(new Label(r, ((k + 1) - (i * sheetSize)), CommonUtils.checkNull(map.get(listKey.get(r)))));
                                }else
                                    sheet.addCell(new jxl.write.Number(r, ((k + 1) - (i * sheetSize)), Double.valueOf(CommonUtils.checkNull(map.get(listKey.get(r))))));
                            } else
                                sheet.addCell(new Label(r, ((k + 1) - (i * sheetSize)), CommonUtils.checkNull(map.get(listKey.get(r)))));
                        }

                    }
                    /*--- 写入数据end ---*/
                }
            } else {
                //空数据
                WritableSheet sheet = workbook.createSheet("无数据", 0);
                //创建标题
                for (int i = 0; i < listHead.size(); i++) {
                    sheet.addCell(new Label(i, 0, listHead.get(i)));
                }
            }

            //写出数据
            workbook.write();
            os.flush();//刷新缓存
        } catch (Exception e) {
            throw e;//抛出异常
        } finally {
        	//关闭WritableWorkbook
        	 if (null != workbook) {
        		 workbook.close();
             }
        	//关闭流
             if (null != os) {
            	 os.close();
             }
//            workbook.close();
//            
//            os.close();
        }

    }

}
