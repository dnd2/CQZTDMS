package com.infodms.dms.actions.claim.serviceActivity;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;

import com.infoservice.mvc.context.ResponseWrapper;


public class Download {
	public static boolean downloadTemplate(ResponseWrapper response,String fileName,String filePath,String old) throws Exception{
		boolean flag = false; 
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			
				//File file = new File(filePath+"\\"+fileName);
				//String fileURL = "file:/" + file.getAbsolutePath();
				String pathStr=filePath+fileName;
				URL url = new URL(pathStr);
				InputStream ism=url.openStream();
				bis = new BufferedInputStream(ism);
				//if(file.exists()){
					  //  BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
					    byte[] buffer = new byte[1024];
					    response.setContentType("application/x-rar-compressed");
					    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(old, "utf-8"));
					    os = response.getOutputStream();
					    while(bis.read(buffer) > 0){
					     os.write(buffer);
					    }
					    flag = true;
				//}
		 } finally {
			 if (null != os) {
				 os.close();
			 }
			 if (null != bis) {
				 bis.close();
			 }
		 }
		return flag;
	}
	
	public static boolean downloadTemplateXls(ResponseWrapper response,String fileName,String filePath,String old) throws Exception {
		boolean flag = false;
		String pathStr=filePath + fileName;
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			URL url = new URL(pathStr);
			InputStream ism=url.openStream();
			bis = new BufferedInputStream(ism);
			byte[] buffer = new byte[1024];
			response.setContentType("application/x-rar-compressed");
			response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(old, "utf-8"));
			os = response.getOutputStream();
			Thread.sleep(500);
			while(bis.read(buffer) > 0){
				os.write(buffer);
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != bis) {
				bis.close();
			} 
			if (null != os) {
				os.close();
			}
		}
		return flag;
	}
	
	/**
	 * @author wizard_lee
	 * @param response
	 * @param fileName
	 * @param filePath
	 * @param old
	 * @return
	 * @throws Exception
	 * @for	针对所有附件下载的公共方法
	 * @date 2014-04-15
	 */
	public static boolean downloadAttachment(ResponseWrapper response,String fileName,String filePath,String old) throws Exception{
		boolean flag = false; 
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
				String pathStr=filePath+fileName;
				URL url = new URL(pathStr);
				InputStream ism=url.openStream();
				bis = new BufferedInputStream(ism);
				int size=0;
				
			    byte[] buffer = new byte[512];
			    response.setContentType("application/x-rar-compressed");
			    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(old, "utf-8"));
			    os = response.getOutputStream();
			    while(size != -1){
			     os.write(buffer,0,size);
			     size=bis.read(buffer);
			 }
			flag = true;
		 } finally {
			 if (null != os) {
				 os.close();
			 }
			 if (null != bis) {
				 bis.close();
			 }
		 }
		return flag;
	}
	
}
