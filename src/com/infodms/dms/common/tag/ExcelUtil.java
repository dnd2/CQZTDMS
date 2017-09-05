package com.infodms.dms.common.tag;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityRelationCodePO;
import com.infodms.dms.po.TtAsWrReturnedOrderDetailPO;
import com.infodms.dms.util.CheckUtil;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;

public class ExcelUtil {
	
	
	private POFactory factory = POFactoryBuilder.getInstance();
	/**
	 * 除去字符串中指定的分隔符
	 * 
	 * @param s
	 *            字符串
	 * @param val
	 *            指定的分隔符
	 * @return
	 */
	public String toToken(String s, String val) {
		if (s == null || s.trim().equals("")) {
			return s;
		}
		if (val == null || val.equals("")) {
			return s;
		}
		StringBuffer stringBuffer = new StringBuffer();
		String[] result = s.split(val);
		for (int x = 0; x < result.length; x++) {
			stringBuffer.append(" ").append(result[x]);
		}
		return stringBuffer.toString();

	}

	public String excelCharaterDeal(String str) {
		String[] val = { "-", "_", "/" };// 定义特殊字符
		for (String i : val) {
			str = this.toToken(str, i);
		}
		return str;
	}

	/**
	 * 读取Excel文件的内容
	 * 
	 * @param file
	 *            待读取的文件
	 * @return
	 */
	public String readExcel(FileObject uploadFile) {
		StringBuffer sb = new StringBuffer();
		Workbook workBook = null;
		try {
			// 构造Workbook（工作薄）对象
			ByteArrayInputStream is = new ByteArrayInputStream(uploadFile.getContent());//获取文件数据
			workBook = Workbook.getWorkbook(is);
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (workBook == null)
			return null;

		// 获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了
		Sheet[] sheet = workBook.getSheets();

		if (sheet != null && sheet.length > 0) {
			// 对每个工作表进行循环
			for (int i = 0; i < sheet.length; i++) {
				// 得到当前工作表的行数
				int rowNum = sheet[i].getRows();
				for (int j = 1; j < rowNum; j++) {
					// 得到当前行的所有单元格
					Cell[] cells = sheet[i].getRow(j);
					try {
						if (cells != null && cells.length > 0) {
							this.insetData(cells);
						}
					} catch (Exception e) {
						sb.append("解析出错或者数据不完整性");
						e.printStackTrace();
					}
					//stringBuffer.append("\r\n");
				}
				//stringBuffer.append("\r\n");
			}
		}
		// 最后关闭资源，释放内存
		workBook.close();
		return sb.toString();
	}

	private void insetData(Cell[] cells) {
		try {
			// 对每个单元格进行循环
			TtAsActivityRelationCodePO code=new TtAsActivityRelationCodePO();
			TtAsActivityRelationCodePO code1=new TtAsActivityRelationCodePO();
			TtAsActivityRelationCodePO code2=new TtAsActivityRelationCodePO();
			code.setId(DaoFactory.getPkId());
			for (int k = 0; k < cells.length; k++) {
				// 特殊字符处理
				if(k==0){ //经销商代码
					String cellValue = cells[k].getContents();
					code.setDealerCode(cellValue);
					TmDealerPO po=new TmDealerPO();
					po.setDealerCode(cellValue);
					List<TmDealerPO> list = factory.select(po);
					if(list!=null && list.size()>0){
						TmDealerPO tmDealerPO = list.get(0);
						code.setDealerCode(tmDealerPO.getDealerCode());
						code2.setDealerCode(tmDealerPO.getDealerCode());
					}
				}
				if(k==1){//VIN
					String cellValue = cells[k].getContents();
					code.setVin(cellValue);
					code1.setVin(cellValue);
				}
				if(k==2){
					String cellValue = cells[k].getContents();
					TtAsActivityPO ac =new TtAsActivityPO();
					ac.setActivityCode(cellValue);
					List<TtAsActivityPO> list = factory.select(ac);
					if(list!=null && list.size()>0){
						TtAsActivityPO acTemp = list.get(0);
						code.setActivityId(acTemp.getActivityId());
						code1.setActivityId(acTemp.getActivityId());
					}
					code.setCreateDate(new Date());
					code.setActivityCode(cellValue);
				}
			}
			code.setId(DaoFactory.getPkId());
			if(factory.select(code1).size() == 0)
			{
				factory.insert(code);
			}else
			{
				factory.update(code1, code2);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	/**
	 * 导入
	 * @param cells
	 */
	private void insetData1(Cell[] cells) {
		// 对每个单元格进行循环
		TtAsWrReturnedOrderDetailPO po=new TtAsWrReturnedOrderDetailPO();
		TtAsWrReturnedOrderDetailPO po1=new TtAsWrReturnedOrderDetailPO();
		for (int k = 0; k < cells.length; k++) {
			String contents = cells[k].getContents();
			if(k==0){
			po.setId(BaseUtils.ConvertLong(contents));
			}
			if(k==3){
				po1.setSignAmount(BaseUtils.ConvertInt(contents));;
			}
			if(k==5){
				TcCodePO c =new TcCodePO();
				c.setCodeDesc(contents);
				c.setType(String.valueOf(Constant.OLDPART_DEDUCT_TYPE));
				List<TcCodePO> list=factory.select(c);
				if(list!=null && list.size()>0){
					c=list.get(0);
					po1.setDeductRemark(BaseUtils.ConvertInt(c.getCodeId()));
				}else{
					po1.setDeductRemark(0);
				}
			}
			if(k==11){
				po1.setProducerCode(contents);
			}			
		}
		factory.update(po, po1);
	}
	
	/**
	 * 导入
	 * @param file
	 * @return
	 */
	public String readExcel1(File file) {
		StringBuffer sb = new StringBuffer();
		Workbook workBook = null;
		try {
			// 构造Workbook（工作薄）对象
			workBook = Workbook.getWorkbook(file);
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (workBook == null)
			return null;

		// 获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了
		Sheet[] sheet = workBook.getSheets();

		if (sheet != null && sheet.length > 0) {
			// 对每个工作表进行循环
			for (int i = 0; i < sheet.length; i++) {
				// 得到当前工作表的行数
				int rowNum = sheet[i].getRows();
				for (int j = 1; j < rowNum; j++) {
					// 得到当前行的所有单元格
					Cell[] cells = sheet[i].getRow(j);
					try {
						if (cells != null && cells.length > 0) {
							this.insetData1(cells);
						}
					} catch (Exception e) {
						sb.append("解析出错或者数据不完整性");
						e.printStackTrace();
					}
					//stringBuffer.append("\r\n");
				}
				//stringBuffer.append("\r\n");
			}
		}
		// 最后关闭资源，释放内存
		workBook.close();
		return sb.toString();
	}

	/**
	 * 生成一个Excel文件
	 * 
	 * @param fileName
	 *            要生成的Excel文件名
	 */
	public static void writeExcel(String fileName) {
		WritableWorkbook wwb = null;
		try {
			// 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
			wwb = Workbook.createWorkbook(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (wwb != null) {
			// 创建一个可写入的工作表
			// Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置
			WritableSheet ws = wwb.createSheet("sheet1", 0);

			// 下面开始添加单元格
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 5; j++) {
					// 这里需要注意的是，在Excel中，第一个参数表示列，第二个表示行
					Label labelC = new Label(j, i, "这是第" + (i + 1) + "行，第"
							+ (j + 1) + "列");
					try {
						// 将生成的单元格添加到工作表中
						ws.addCell(labelC);
					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}

				}
			}

			try {
				// 从内存中写入文件中
				wwb.write();
				// 关闭资源，释放内存
				wwb.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 搜索某一个文件中是否包含某个关键字
	 * 
	 * @param file
	 *            待搜索的文件
	 * @param keyWord
	 *            要搜索的关键字
	 * @return
	 */
	public static boolean searchKeyWord(File file, String keyWord) {
		boolean res = false;

		Workbook wb = null;
		try {
			// 构造Workbook（工作薄）对象
			wb = Workbook.getWorkbook(file);
		} catch (BiffException e) {
			return res;
		} catch (IOException e) {
			return res;
		}

		if (wb == null)
			return res;

		// 获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了
		Sheet[] sheet = wb.getSheets();

		boolean breakSheet = false;

		if (sheet != null && sheet.length > 0) {
			// 对每个工作表进行循环
			for (int i = 0; i < sheet.length; i++) {
				if (breakSheet)
					break;

				// 得到当前工作表的行数
				int rowNum = sheet[i].getRows();

				boolean breakRow = false;

				for (int j = 0; j < rowNum; j++) {
					if (breakRow)
						break;
					// 得到当前行的所有单元格
					Cell[] cells = sheet[i].getRow(j);
					if (cells != null && cells.length > 0) {
						boolean breakCell = false;
						// 对每个单元格进行循环
						for (int k = 0; k < cells.length; k++) {
							if (breakCell)
								break;
							// 读取当前单元格的值
							String cellValue = cells[k].getContents();
							if (cellValue == null)
								continue;
							if (cellValue.contains(keyWord)) {
								res = true;
								breakCell = true;
								breakRow = true;
								breakSheet = true;
							}
						}
					}
				}
			}
		}
		// 最后关闭资源，释放内存
		wb.close();

		return res;
	}

	/**
	 * 往Excel中插入图片
	 * 
	 * @param dataSheet
	 *            待插入的工作表
	 * @param col
	 *            图片从该列开始
	 * @param row
	 *            图片从该行开始
	 * @param width
	 *            图片所占的列数
	 * @param height
	 *            图片所占的行数
	 * @param imgFile
	 *            要插入的图片文件
	 */
	public static void insertImg(WritableSheet dataSheet, int col, int row,
			int width, int height, File imgFile) {
		WritableImage img = new WritableImage(col, row, width, height, imgFile);
		dataSheet.addImage(img);
	}
	/**
	 * 模板下载
	 * @param response
	 * @param request
	 * @param head
	 * @param list
	 * @param name
	 * @return
	 * @throws Exception
	 */
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
                        /*ws.addCell(new Label(i, z, str[i]));*/ //modify by yuan
					if (CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])) {
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
