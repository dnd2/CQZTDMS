package com.infodms.dms.actions.claim.basicData;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

import flex.messaging.io.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

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
				inputStream.close();
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
		for (int i = 0; i < totalRows; i++) {
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
		for (int j = firstRow+1; j < totalRows; j++) {
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
     * @author 
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
