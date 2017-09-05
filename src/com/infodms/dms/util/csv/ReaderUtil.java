package com.infodms.dms.util.csv;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class ReaderUtil {
	public Logger logger = Logger.getLogger(ReaderUtil.class);
	private String separator = null;
	private List<String> list = null;
	private String encoder = null;
	private String sysEncoder = null;
	private boolean isEncoder = false;
	private int blankRow = 0;
	private boolean isHavaCon = false;
	private String lineSeparator = null;
	/**
	 * 传入byte数组
	 * @param filename
	 * @throws IOException
	 */
	public ReaderUtil(byte[] bytes,String separator) throws Exception {
		list = new ArrayList<String>();
		this.separator = separator;
		this.sysEncoder = System.getProperty("file.encoding");
		logger.debug("行分隔符："+System.getProperty("line.separator"));
		lineSeparator = System.getProperty("line.separator");
		byteArrayInit(bytes);
	}
	/**
	 * 
	* 功能说明： 判定是否进行转码
	* 最后修改时间：Sep 3, 2009
	 */
	private void isEncoder(){
		
		logger.debug("文件编码："+encoder);
		if(sysEncoder!=null&&sysEncoder.equals("UTF-8")){
			if(encoder!=null&&!encoder.equals("UTF-8")){
				isEncoder = true;
				return;
			}
		}
		if(sysEncoder!=null&&sysEncoder.equals("GBK")){
			if(encoder!=null&&(!encoder.equals("GBK"))&&(!encoder.equals("GB2312"))&&(!encoder.equals("windows-1252"))){
				isEncoder = true;
				return;
			}
		}
	}
	/**
	 * 
	* 功能说明：对数组进行初使化
	* @param bytes
	* 最后修改时间：Sep 3, 2009
	 * @throws Exception 
	 */
	private void byteArrayInit(byte[] bytes) throws Exception{
		ByteArrayInputStream byteStream = null;
		try{
			logger.debug("数据初使化到内存开始");
			long startTime = System.currentTimeMillis();
			byteStream = new ByteArrayInputStream(bytes);
			int i =1;
			while(this.encoder==null&&i<3){
				//获得文件的编码
				this.encoder = fileEncoder(byteStream);
				logger.debug("尝试获取次数"+i);
				i++;
				
			}
			//对根据编码格式，判断是否进行转码
			isEncoder();
			int buf = 4096;
			byte[] byteTemp = new byte[buf];
			int count = 0;
			byte[] byte2 = null;
			byte[] byte3 = null;
			while((count =byteStream.read(byteTemp,0,buf))!=-1){
				//logger.debug("读取字符："+count+"\n"+new String(byteTemp,encoder));
				if(count<buf){
					if(byte3!=null){
						byte2 = new byte[count+byte3.length];
						System.arraycopy(byte3, 0, byte2, 0, byte3.length);
						System.arraycopy(byteTemp, 0, byte2, byte3.length, count);
					}else{
						byte2 = new byte[count];
						System.arraycopy(byteTemp, 0, byte2, 0, count);
					}
					byte3 = doByteToString(byte2);
				}else{
					if(byte3!=null){
						byte2 = new byte[byte3.length+byteTemp.length];
						System.arraycopy(byte3, 0, byte2, 0, byte3.length);
						System.arraycopy(byteTemp, 0, byte2, byte3.length, byteTemp.length);
						byte3 = doByteToString(byte2);
					}else{
						byte3 = doByteToString(byteTemp);
					}
					
				}
			}
			if(byte3!=null){
				String lastRow = null;
				if(isEncoder){
					lastRow = new String(byte3,encoder);
					if(lastRow!=null&&!(lastRow.trim().equals(""))){
						list.add(lastRow);
						logger.debug(new String(byte3,encoder));
					}
				}else{
					lastRow = new String(byte3);
					if(lastRow!=null&&!(lastRow.trim().equals(""))){
						list.add(lastRow);
						logger.debug(new String(byte3));
					}
				}
			}
			logger.debug("处理："+list.size()+"条，耗时:"+(System.currentTimeMillis()-startTime));
		}catch(Exception e){
			throw e;
		}finally{
			try{
				if(byteStream!=null){
					byteStream.close();
				}
			}catch(Exception e){
				throw e;
			}
	}
	logger.debug("数据初使化到内存结束");
		
	}
	/**
	* 功能说明：处理每次读取的字符
	* @param byteTemp
	* @return byte[] 返回没有处理完成字节数组。
	* 最后修改时间：2009-8-22
	 * @throws UnsupportedEncodingException 
	 */
	private byte[]  doByteToString(byte[] byteTemp) throws UnsupportedEncodingException{
		int outByteSize = 0;
		String[] stemp = null;
		byte[]  byte1= null;
		String string2 = null;
		if(isEncoder){
			if(encoder!=null){
				string2 = new String(byteTemp,encoder);
				//logger.debug("string:"+string2);
			}
		}else{
			string2 = new String(byteTemp);
		}
		stemp = string2.split(lineSeparator,-1);
		if(string2!=null&&!string2.equals("")){
			for(int i=0;i<stemp.length;i++){
				if(i!=(stemp.length-1)){
					if(stemp[i]!=null&&!(stemp[i].trim().equals(""))){
						if(!isHavaCon){
							isHavaCon = true;
						}
						list.add(stemp[i]);
						if(isEncoder){
							outByteSize+=(stemp[i].getBytes(encoder).length+lineSeparator.getBytes().length);
						}else{
							outByteSize+=(stemp[i].getBytes().length+lineSeparator.getBytes().length);
						}
						logger.debug(stemp[i]);
					}else if(!isHavaCon){
						blankRow++;
					}
				}else{
					byte1 = new byte[byteTemp.length-outByteSize];
					System.arraycopy(byteTemp, (byteTemp.length-byte1.length), byte1, 0, byte1.length);
					//logger.debug(new String(byte1,encoder));
				}
			}
		}
		return byte1;
	}
	/**
	* 功能说明：释放内存
	* 最后修改时间：2009-8-23
	 */
	public void destory(){
		if(list!=null){
			list.clear();
		}
		list = null;
	}
	/**
	 * 
	* 功能说明：返回读取的内容封装的list
	* @return
	* @throws IOException
	* 最后修改时间：2009-8-23
	 */
	public List<String> getList() throws IOException {
		return list;
	}
	/**
	 * 
	* 功能说明：获得这个文件有多少行
	* @return
	* 最后修改时间：Sep 3, 2009
	 */
	public int getRowNum() {
		return list.size();
	}
	/**
	 * 
	* 功能说明：获得这个文件有多少列
	* @return
	* 最后修改时间：Sep 3, 2009
	 */
	public int getColNum() {
		if(list.size()>0){
			if (list.get(0).toString().contains(separator)) {
				return list.get(0).toString().split(separator,-1).length;
			} else if (list.get(0).toString().trim().length() != 0) {
				return 1;
			} else {
				return 0;
			}
		}else{
			return 0;
		}
	}
	/**
	 * 
	* 功能说明：获得某一行的值
	* @param index
	* @return
	* 最后修改时间：Sep 3, 2009
	 */
	public String getRow(int index) {
		if (this.list.size() != 0)
			return (String) list.get(index);
		else
			return null;
	}
	/**
	 * 
	* 功能说明：获得某一列的值 
	* @param index
	* @return
	* 最后修改时间：Sep 3, 2009
	 */
	public String getCol(int index) {
		if (this.getColNum() == 0) {
			return null;
		}
		StringBuffer scol = new StringBuffer();
		String temp = null;
		int colnum = this.getColNum();
		if (colnum > 1) {
			for (Iterator<String> it = list.iterator(); it.hasNext();) {
				temp = it.next().toString();

				scol = scol.append(temp.split(separator,-1)[index] + ",");
			}
		} else {
			for (Iterator<String> it = list.iterator(); it.hasNext();) {
				temp = it.next().toString();
				scol = scol.append(temp + ",");
			}
		}
		String str = new String(scol.toString());
		str = str.substring(0, str.length() - 1);
		return str;
	}
	/**
	 * 
	* 功能说明：获得文件中的某一行某一列的值
	* @param row
	* @param col
	* @return
	* 最后修改时间：Sep 3, 2009
	 */
	public String getString(int row, int col) {
		String temp = null;
		int colnum = this.getColNum();
		if (colnum > 1) {
			temp = list.get(row).toString().split(separator,-1)[col];
		} else if (colnum == 1) {
			temp = list.get(row).toString();
		} else {
			temp = null;
		}
		return temp==null?null:temp.trim();
	}
	/**
	 * 获得文件的编码
	* 功能说明：
	* @param bais
	* @return
	* @throws Exception
	* 最后修改时间：Sep 3, 2009
	 */
	public String fileEncoder(ByteArrayInputStream bais) throws Exception{
		
		CodepageDetectorProxy detector =   CodepageDetectorProxy.getInstance();   
		detector.add(new ParsingDetector(false));    
		Charset charset = detector.detectCodepage(bais,100);  
		detector.add(JChardetFacade.getInstance());   
		detector.add(ASCIIDetector.getInstance());   
		detector.add(UnicodeDetector.getInstance());   
		
		if(charset!=null&&(!charset.name().equals("void"))){   
			return charset.name();
		}else{
		   return null;
		}	
	}
	/**
	 * @return the blankRow
	 */
	public int getBlankRow() {
		return blankRow;
	}
	/**
	 * @param blankRow the blankRow to set
	 */
	public void setBlankRow(int blankRow) {
		this.blankRow = blankRow;
	}
}