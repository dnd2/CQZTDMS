package com.infodms.dms.util.csv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.infodms.dms.exception.BizException;

public class CsvWriterUtil {
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		File file = new File("f:\\test123.xls");
		List<List<String>> list = new LinkedList<List<String>>();
		List<String> listTemp = null;
		listTemp = new LinkedList<String>();
		listTemp.add("序号");
		listTemp.add("姓名");
		list.add(listTemp);
		for(int i=0;i<10;i++){
			listTemp = new LinkedList<String>();
			listTemp.add(String.valueOf(i));
			listTemp.add(String.valueOf(((char)(i+65))));
			list.add(listTemp);
		}
		
		try {
			write(list,new FileOutputStream(file));
			//			writeXlsFile(list,file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void write(List<List<String>> content,File file) throws BizException{
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(file,"gbk");
			for(List<String> list: content){
				StringBuffer sb = new StringBuffer(); 
				for(String str:list){
					sb.append(str).append(",");
				}
				pw.println(sb.substring(0,sb.length()-1));
			}
			pw.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new BizException("写流出错");
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}
	
	public static void write(List<List<String>> content,OutputStream os) throws BizException{
		PrintStream pw = null;
		try {
			pw = new PrintStream(os,false,"gbk");
			for(List<String> list: content){
				StringBuffer sb = new StringBuffer(); 
				for(String str:list){
					sb.append(str).append(",");
				}
				System.out.println(sb.substring(0,sb.length()-1));
				pw.println(sb.substring(0,sb.length()-1));
			}
			pw.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new BizException("写流出错");
		} if (null != pw) {
			pw.close();
		}
	}
	
	public static void writeCsv(List<List<Object>> content,OutputStream os) throws BizException{
		PrintStream pw = null;
		try {
			pw = new PrintStream(os,false,"gbk");
			for(List<Object> list: content){
				StringBuffer sb = new StringBuffer(); 
				for(Object str:list)
				{
					//modified by andy.ten@tom.com
					if(str == null || "".equals(str)) str = " ";
					sb.append(str.toString().replaceAll(",", "&")).append(",");
					//end
				}
				//System.out.println(sb.substring(0,sb.length()-1));
				pw.println(sb.substring(0,sb.length()-1));
			}
			pw.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new BizException("写流出错");
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}
	/**
	 * Function：输出Csv文件，可打印基本信息
	 * @param  ：	
	 * @return:		@param printColumnNum 打印出的列数
	 * @return:		@param beanInfo 详细信息的字符串，按照参数splitStr来分割
	 * @return:		@param splitStr分隔符
	 * @return:		@param content 打印的表头和数据
	 * @return:		@param os 打印输出流
	 * @return:		@throws BizException 
	 * @throw：	
	 * LastUpdate：	2010-6-29 ZhaoLunda
	 */
	public static void writeCsvExt(int printColumnNum,
			                       String beanInfo,
			                       String splitStr,
			                       List<List<Object>> content,
			                       OutputStream os) throws BizException{
		PrintStream pw = null;
		try {
			pw = new PrintStream(os,false,"gbk");
			if(printColumnNum<2&&printColumnNum%2==0) printColumnNum=2;//默认值2
			if(splitStr==null||"".equals(splitStr)) splitStr=",";//默认分隔符","
			//打印beanInfo详细信息
			if(beanInfo!=null&&!"".equals(printColumnNum)){
				String[] strArr=beanInfo.split(splitStr);
				for(int count=1;count<=strArr.length;count++){
					if(count%printColumnNum>0){
						pw.print(strArr[count-1]+",");
					}else{
						pw.println(strArr[count-1]);
					}
				}
				pw.println("");
			}
			for(List<Object> list: content){
				StringBuffer sb = new StringBuffer(); 
				for(Object str:list)
				{
					//modified by andy.ten@tom.com
					if(str == null || "".equals(str)) str = " ";
					sb.append(str.toString()).append(",");
					//end
				}
				//System.out.println(sb.substring(0,sb.length()-1));
				pw.println(sb.substring(0,sb.length()-1));
			}
			pw.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new BizException("写流出错");
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}
	
	/**
	 * 生成xls文件
	 * @param content
	 * @param file
	 * @author zouchao
	 * @since  2010-08-20
	 */
	public static void writeXlsFile(List<List<String>> content,File file){
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(file);
			WritableSheet sheet = workbook.createSheet("下载模板", 0);
			for(int i=0;i<content.size();i++){
                
				sheet.addCell(new Label( 0, i,(content.get(i)).get(0)));
				sheet.addCell(new Label( 1, i,(content.get(i)).get(1)));
				
			}
			workbook.write();
			workbook.close();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createXlsFileByType(List<List<Object>> content,OutputStream os){

		try {
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("下载模板", 0);
			for(int i=0;i<content.size();i++){
				
				for(int j = 0;j<content.get(i).size();j++){
					try{
						jxl.write.Number number = null ;
						
						number = new jxl.write.Number(j, i, Long.parseLong(content.get(i).get(j).toString())) ;
						
						sheet.addCell(number);
					} catch(Exception e) {
						Label label = null ;
						
						label = new Label( j, i,content.get(i).get(j).toString()) ;
						
						sheet.addCell(label);
					}
					//Long.parseLong((content.get(i)).get(0).toString())
				}
			}
			workbook.write();
			workbook.close();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 生成xls文件
	 * @param content
	 * @param file
	 * @author zouchao
	 * @since  2010-08-20
	 */
	public static void createXlsFile(List<List<Object>> content,OutputStream os){

		try {
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("下载模板", 0);
			for(int i=0;i<content.size();i++){
				
				for(int j = 0;j<content.get(i).size();j++){
					// 添加单元格
					sheet.addCell(new Label(j,i,(content.get(i).get(j)).toString()));
				}
				
			}
			workbook.write();
			workbook.close();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 生成xls文件
	 * @param content
	 * @param file
	 * @author wenyudan
	 * @since  2013-10-20
	 */
	public static void createXlsFileToName(List<List<Object>> content,OutputStream os,String name){
		
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet(name, 0);
			for(int i=0;i<content.size();i++){
				
				for(int j = 0;j<content.get(i).size();j++){
					// 添加单元格
					sheet.addCell(new Label(j,i,(content.get(i).get(j)).toString()));
				}
				
			}
			workbook.write();
			workbook.close();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
